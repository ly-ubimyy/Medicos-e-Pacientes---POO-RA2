package leitura;

import excecoes.DadoInvalidoException;
import excecoes.RegistroNaoEncontradoException;
import modelo.BaseDados;
import modelo.Consulta;
import modelo.Medico;
import modelo.Paciente;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorCsv {

    // le os 3 arquivos eretorna na BaseDados
    public static BaseDados carregarBaseDados(
            String caminhoMedicos,
            String caminhoPacientes,
            String caminhoConsultas
    ) throws IOException, DadoInvalidoException, RegistroNaoEncontradoException {

        BaseDados baseDados = new BaseDados();

        // medico -> pacientes -> consultas (ordem pq cosnulta depende das 2)
        lerMedicos(caminhoMedicos, baseDados);
        lerPacientes(caminhoPacientes, baseDados);
        lerConsultas(caminhoConsultas, baseDados);

        return baseDados;
    }

    // le medicos.cvs | formato: codigo, nome
    private static void lerMedicos(String caminhoArquivo, BaseDados baseDados)
            throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {

                //linha vazia = pula
                if (linha.isBlank()) {
                    continue;
                }

                // pula cabeçalho
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(",");

                // se ñ tem os 2 campor, ta errado
                if (campos.length != 2) {
                    throw new IOException("Linha inválida no arquivo de médicos: " + linha);
                }

                int codigo = Integer.parseInt(campos[0].trim());
                String nome = campos[1].trim();

                Medico medico = new Medico(nome, codigo);

                baseDados.adicionarMedico(medico);
            }
        }
    }

    // le pacientes.csv | formato: cpf, nome
    private static void lerPacientes(String caminhoArquivo, BaseDados baseDados)
            throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {

                if (linha.isBlank()) {
                    continue;
                }

                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(",");

                if (campos.length != 2) {
                    throw new IOException("Linha inválida no arquivo de pacientes: " + linha);
                }

                String cpf = campos[0].trim();
                String nome = campos[1].trim();

                Paciente paciente = new Paciente(nome, cpf);

                baseDados.adicionarPaciente(paciente);
            }
        }
    }

    // le consultas.csv | formato: data,horario,codigoMedico,cpfPaciente
    //data no formato dd/mm/aaaa
    private static void lerConsultas(String caminhoArquivo, BaseDados baseDados)
            throws IOException, DadoInvalidoException, RegistroNaoEncontradoException {

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {

                if (linha.isBlank()) {
                    continue;
                }

                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(",");

                // essa tem que ter 4 campos, se não, ta errado
                if (campos.length != 4) {
                    throw new IOException("Linha inválida no arquivo de consultas: " + linha);
                }

                String data = converterData(campos[0].trim());
                String horario = campos[1].trim();
                int codigoMedico = Integer.parseInt(campos[2].trim());
                String cpfPaciente = campos[3].trim();

                // antes de criar consulta confere se o médico e o paciente existem
                baseDados.buscarMedico(codigoMedico);
                baseDados.buscarPaciente(cpfPaciente);

                Consulta consulta = new Consulta(data, horario, codigoMedico, cpfPaciente);

                baseDados.adicionarConsulta(consulta);
            }
        }
    }
    private static String converterData(String data) {
        if (data.matches("\\d{4}-\\d{2}-\\d{2}")) {
            String[] partes = data.split("-");
            return partes[2] + "/" + partes[1] + "/" + partes[0];
        }

        return data;
    }
}