package busca;

import excecoes.RegistroNaoEncontradoException;
import modelo.BaseDados;
import modelo.Consulta;
import modelo.Medico;
import modelo.Paciente;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BuscaPaciente {

    public static Paciente buscarPacientePorCpf(BaseDados baseDados, String cpfPaciente)
            throws RegistroNaoEncontradoException {

        if (cpfPaciente == null || cpfPaciente.isBlank()) {
            throw new RegistroNaoEncontradoException("CPF do paciente não informado.");
        }

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

    public static List<Medico> listarMedicosDoPaciente(BaseDados baseDados, String cpfPaciente)
            throws RegistroNaoEncontradoException {

        Paciente paciente = buscarPacientePorCpf(baseDados, cpfPaciente);
        List<Medico> medicos = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {
            boolean mesmoPaciente = consulta.getCpfPaciente().equals(paciente.getCpf());

            if (mesmoPaciente) {
                Medico medico = baseDados.buscarMedico(consulta.getCodigoMedico());

                if (!medicos.contains(medico)) {
                    medicos.add(medico);
                }
            }
        }

        medicos.sort(Comparator.comparing(Medico::getNome));
        return medicos;
    }

    public static List<Consulta> listarConsultasPassadasComMedico(
            BaseDados baseDados,
            String cpfPaciente,
            int codigoMedico
    ) throws RegistroNaoEncontradoException {

        Paciente paciente = buscarPacientePorCpf(baseDados, cpfPaciente);
        baseDados.buscarMedico(codigoMedico);

        List<Consulta> consultasEncontradas = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {
            boolean mesmoPaciente = consulta.getCpfPaciente().equals(paciente.getCpf());
            boolean mesmoMedico = consulta.getCodigoMedico() == codigoMedico;
            boolean consultaPassada = consulta.isPassada();

            if (mesmoPaciente && mesmoMedico && consultaPassada) {
                consultasEncontradas.add(consulta);
            }
        }

        consultasEncontradas.sort(
                Comparator.comparing(Consulta::getData)
                        .thenComparing(Consulta::getHorario)
        );

        return consultasEncontradas;
    }

    public static List<Consulta> listarConsultasRealizadasComMedico(
            BaseDados baseDados,
            String cpfPaciente,
            int codigoMedico
    ) throws RegistroNaoEncontradoException {
        return listarConsultasPassadasComMedico(baseDados, cpfPaciente, codigoMedico);
    }


    public static List<Consulta> listarConsultasAgendadasDoPaciente(
            BaseDados baseDados,
            String cpfPaciente
    ) throws RegistroNaoEncontradoException {

        Paciente paciente = buscarPacientePorCpf(baseDados, cpfPaciente);
        List<Consulta> consultasEncontradas = new ArrayList<>();

        for (Consulta consulta : baseDados.getConsultas()) {
            boolean mesmoPaciente = consulta.getCpfPaciente().equals(paciente.getCpf());
            boolean consultaFutura = consulta.isFutura();

            if (mesmoPaciente && consultaFutura) {
                consultasEncontradas.add(consulta);
            }
        }

        consultasEncontradas.sort(
                Comparator.comparing(Consulta::getData)
                        .thenComparing(Consulta::getHorario)
        );

        return consultasEncontradas;
    }

    public static List<Consulta> listarConsultasFuturasDoPaciente(
            BaseDados baseDados,
            String cpfPaciente
    ) throws RegistroNaoEncontradoException {
        return listarConsultasAgendadasDoPaciente(baseDados, cpfPaciente);
    }
}