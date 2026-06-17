buscaMEDICO
package busca;

import excecoes.RegistroNaoEncontradoException;
import modelo.BaseDados;
import modelo.Consulta;
import modelo.Medico;
import modelo.Paciente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BuscaMedico {

    //busca um médico dentro da base de dados;
    public static Medico buscarMedicoPorCodigo(BaseDados baseDados, int codigoMedico)
            throws RegistroNaoEncontradoException {

        // percorre lista de médicos cadastrados
        for (Medico medico : baseDados.getMedicos()) {
            if (medico.getCodigo() == codigoMedico) {
                return medico;
            }
        }

        // lança RegistroNaoEncontradoException caso nenhum médico seja encontrado
        throw new RegistroNaoEncontradoException(
                "Médico com código " + codigoMedico + " não encontrado."
        );
    }

    // busca paciente por CPF
    // esse método é privado porque será usado apenas internamente dentro da classe BuscaMedico
    private static Paciente buscarPacientePorCpf(BaseDados baseDados, String cpfPaciente)
            throws RegistroNaoEncontradoException {

        // remove pontos, traços ou qualquer coisa que não seja número
        String cpfLimpo = cpfPaciente.replaceAll("[^0-9]", "");

        // percorre lista de pacientes cadastrados
        for (Paciente paciente : baseDados.getPacientes()) {
            if (paciente.getCpf().equals(cpfLimpo)) {
                return paciente;
            }
        }

        throw new RegistroNaoEncontradoException(
                "Paciente com CPF " + cpfPaciente + " não encontrado."
        );
    }

    // aqui é a função que lista os pacientes de um médico específico
    public static List<Paciente> listarPacientesDoMedico(BaseDados baseDados, int codigoMedico)
            throws RegistroNaoEncontradoException {

        buscarMedicoPorCodigo(baseDados, codigoMedico);

        List<Paciente> pacientes = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {

            // consulta pertence ao médico?
            if (consulta.getCodigoMedico() == codigoMedico) {

                // busca o paciente por CPF presente na consulta
                Paciente paciente = buscarPacientePorCpf(baseDados, consulta.getCpfPaciente());

                // aqui é pra evitar add o mesmo paciente varias caso mais consultas com mesmo médico
                if (!pacientes.contains(paciente)) {
                    pacientes.add(paciente);
                }
            }
        }

        return pacientes;
    }

    // lista as consultas de um médico em um período especifico e retorna em ordem crescente
    public static List<Consulta> listarConsultasDoMedicoNoPeriodo(
            BaseDados baseDados,
            int codigoMedico,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) throws RegistroNaoEncontradoException {

        buscarMedicoPorCodigo(baseDados, codigoMedico);

        List<Consulta> consultasEncontradas = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {

            // pertence a esse médico?
            boolean mesmoMedico = consulta.getCodigoMedico() == codigoMedico;

            // usa o método da própria classe 'Consulta'
            boolean dentroDoPeriodo = consulta.estaEntreDatas(dataInicial, dataFinal);

            if (mesmoMedico && dentroDoPeriodo) {
                consultasEncontradas.add(consulta);
            }
        }

        // ordem cresente de data mas se for igual vai pelo horario
        consultasEncontradas.sort(
                Comparator.comparing(Consulta::getData)
                        .thenComparing(Consulta::getHorario)
        );

        return consultasEncontradas;
    }

    // lista pacientes que o médico não atende a mais de X meses 
    public static List<Paciente> listarPacientesSemConsultaHaMaisDeMeses(
            BaseDados baseDados,
            int codigoMedico,
            int meses
    ) throws RegistroNaoEncontradoException {

        buscarMedicoPorCodigo(baseDados, codigoMedico);

        // define a data pra ver se o paciente está há mais X tempo sem consultar
        LocalDate limite = LocalDate.now().minusMonths(meses);

        // aqui busca todos os pacientes que já se consultaram com esse médico 
        List<Paciente> pacientesDoMedico = listarPacientesDoMedico(baseDados, codigoMedico);

        List<Paciente> pacientesSemConsulta = new ArrayList<>();

        for (Paciente paciente : pacientesDoMedico) {
            LocalDate ultimaConsulta = null;

            // procura a última consulta dopaciente com esse médico
            for (Consulta consulta : baseDados.getConsultas()) {
                boolean mesmoMedico = consulta.getCodigoMedico() == codigoMedico;
                boolean mesmoPaciente = consulta.getCpfPaciente().equals(paciente.getCpf());
                boolean consultaPassada = consulta.isPassada();

                if (mesmoMedico && mesmoPaciente && consultaPassada) {

                    // atualiza a última consulta se for a mais recente encontrada até agora
                    if (ultimaConsulta == null || consulta.getData().isAfter(ultimaConsulta)) {
                        ultimaConsulta = consulta.getData();
                    }
                }
            }

           // caso não teve consulta ou a última consulta foi antes do limite, adiciona nessa lista de pacientes sem consulta
            if (ultimaConsulta == null || ultimaConsulta.isBefore(limite)) {
                pacientesSemConsulta.add(paciente);
            }
        }

        return pacientesSemConsulta;
    }
}