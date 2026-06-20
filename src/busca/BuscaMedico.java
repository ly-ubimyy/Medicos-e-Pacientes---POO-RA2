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

    public static Medico buscarMedicoPorCodigo(BaseDados baseDados, int codigoMedico)
            throws RegistroNaoEncontradoException {
        for (Medico medico : baseDados.getMedicos()) {
            if (medico.getCodigo() == codigoMedico) {
                return medico;
            }
        }

        throw new RegistroNaoEncontradoException(
                "Médico com código " + codigoMedico + " não encontrado."
        );
    }

    private static Paciente buscarPacientePorCpf(BaseDados baseDados, String cpfPaciente)
            throws RegistroNaoEncontradoException {

        String cpfLimpo = cpfPaciente.replaceAll("[^0-9]", "");

        for (Paciente paciente : baseDados.getPacientes()) {
            if (paciente.getCpf().equals(cpfLimpo)) {
                return paciente;
            }
        }

        throw new RegistroNaoEncontradoException(
                "Paciente com CPF " + cpfPaciente + " não encontrado."
        );
    }

    public static List<Paciente> listarPacientesDoMedico(BaseDados baseDados, int codigoMedico)
            throws RegistroNaoEncontradoException {

        buscarMedicoPorCodigo(baseDados, codigoMedico);

        List<Paciente> pacientes = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {

            if (consulta.getCodigoMedico() == codigoMedico) {

                Paciente paciente = buscarPacientePorCpf(baseDados, consulta.getCpfPaciente());

                if (!pacientes.contains(paciente)) {
                    pacientes.add(paciente);
                }
            }
        }

        return pacientes;
    }

    public static List<Consulta> listarConsultasDoMedicoNoPeriodo(
            BaseDados baseDados,
            int codigoMedico,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) throws RegistroNaoEncontradoException {

        buscarMedicoPorCodigo(baseDados, codigoMedico);

        List<Consulta> consultasEncontradas = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {

            boolean mesmoMedico = consulta.getCodigoMedico() == codigoMedico;

            boolean dentroDoPeriodo = consulta.estaEntreDatas(dataInicial, dataFinal);

            if (mesmoMedico && dentroDoPeriodo) {
                consultasEncontradas.add(consulta);
            }
        }

        consultasEncontradas.sort(
                Comparator.comparing(Consulta::getData)
                        .thenComparing(Consulta::getHorario)
        );

        return consultasEncontradas;
    }

    public static List<Paciente> listarPacientesSemConsultaHaMaisDeMeses(
            BaseDados baseDados,
            int codigoMedico,
            int meses
    ) throws RegistroNaoEncontradoException {

        buscarMedicoPorCodigo(baseDados, codigoMedico);

        LocalDate limite = LocalDate.now().minusMonths(meses);

        List<Paciente> pacientesDoMedico = listarPacientesDoMedico(baseDados, codigoMedico);

        List<Paciente> pacientesSemConsulta = new ArrayList<>();

        for (Paciente paciente : pacientesDoMedico) {
            LocalDate ultimaConsulta = null;

            for (Consulta consulta : baseDados.getConsultas()) {
                boolean mesmoMedico = consulta.getCodigoMedico() == codigoMedico;
                boolean mesmoPaciente = consulta.getCpfPaciente().equals(paciente.getCpf());
                boolean consultaPassada = consulta.isPassada();

                if (mesmoMedico && mesmoPaciente && consultaPassada) {

                    if (ultimaConsulta == null || consulta.getData().isAfter(ultimaConsulta)) {
                        ultimaConsulta = consulta.getData();
                    }
                }
            }

            if (ultimaConsulta == null || ultimaConsulta.isBefore(limite)) {
                pacientesSemConsulta.add(paciente);
            }
        }

        return pacientesSemConsulta;
    }
}