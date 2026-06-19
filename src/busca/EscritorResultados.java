package busca;

import modelo.Consulta;
import modelo.Medico;
import modelo.Paciente;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EscritorResultados {

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Escreve um resultado em arquivo txt.
    public static void salvarResultado(String caminhoArquivo, String tituloBusca, String resultado)
            throws IOException {

        File arquivo = new File(caminhoArquivo);
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write("========================================");
            writer.newLine();
            writer.write("Busca: " + tituloBusca);
            writer.newLine();
            writer.write("Data/hora: " + LocalDateTime.now().format(FORMATO_DATA_HORA));
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            if (resultado == null || resultado.isBlank()) {
                writer.write("Nenhum resultado encontrado.");
            } else {
                writer.write(resultado);
            }

            writer.newLine();
            writer.newLine();
        }
    }

    public static void salvarTexto(String caminhoArquivo, String texto)
            throws IOException {

        File arquivo = new File(caminhoArquivo);
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write(texto);
            writer.newLine();
        }
    }

    public static String formatarMedicos(List<Medico> medicos) {
        if (medicos == null || medicos.isEmpty()) {
            return "Nenhum médico encontrado.";
        }

        StringBuilder sb = new StringBuilder();

        for (Medico medico : medicos) {
            sb.append(medico.getResumo()).append(System.lineSeparator());
        }

        return sb.toString();
    }

    public static String formatarPacientes(List<Paciente> pacientes) {
        if (pacientes == null || pacientes.isEmpty()) {
            return "Nenhum paciente encontrado.";
        }

        StringBuilder sb = new StringBuilder();

        for (Paciente paciente : pacientes) {
            sb.append(paciente.getResumo()).append(System.lineSeparator());
        }

        return sb.toString();
    }

    public static String formatarConsultas(List<Consulta> consultas) {
        if (consultas == null || consultas.isEmpty()) {
            return "Nenhuma consulta encontrada.";
        }

        StringBuilder sb = new StringBuilder();

        for (Consulta consulta : consultas) {
            sb.append(consulta).append(System.lineSeparator());
        }

        return sb.toString();
    }
}