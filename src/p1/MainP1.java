package p1;

import excecoes.DadoInvalidoException;
import modelo.BaseDados;
import excecoes.RegistroNaoEncontradoException;
import leitura.GravadorBinario;
import leitura.LeitorCsv;
import java.io.IOException;

public class MainP1 {

    private static final String DEFAULT_MEDICOS = "dados/medicos.csv";
    private static final String DEFAULT_PACIENTES = "dados/pacientes.csv";
    private static final String DEFAULT_CONSULTAS = "dados/consultas.csv";
    private static final String DEFAULT_BIN = "dados/base.bin";

    public static void main(String[] args) {
        String arqMedicos = args.length > 0 ? args[0] : DEFAULT_MEDICOS;
        String arqPacientes = args.length > 1 ? args[1] : DEFAULT_PACIENTES;
        String arqConsultas = args.length > 2 ? args[2] : DEFAULT_CONSULTAS;
        String arqBin = args.length > 3 ? args[3] : DEFAULT_BIN;

        BaseDados base = new BaseDados();


        try {
            // Lê os arquivos CSV de médicos, pacientes e consultas e monta a base de dados em memória
            base = LeitorCsv.carregarBaseDados(arqMedicos, arqPacientes, arqConsultas);

            // Salva a base de dados em um arquivo binário para ser reutilizada pelo P2
            GravadorBinario.salvar(base, arqBin);

            System.out.println("Base de dados criada com sucesso.");
            System.out.println("Médicos: " + base.getMedicos().size());
            System.out.println("Pacientes: " + base.getPacientes().size());
            System.out.println("Consultas: " + base.getConsultas().size());
            System.out.println("Arquivo binário salvo em: " + arqBin);
        }

        catch (DadoInvalidoException e)
        {
            System.err.println("[ERRO] Dado inválido: " + e.getMessage());
            System.exit(1);
        }
        // Trata o erro caso uma consulta faça referência a um médico ou paciente inexistente
        catch (RegistroNaoEncontradoException e)
        {
            System.err.println("[ERRO] Registro não encontrado: " + e.getMessage());
            System.exit(2);
        }
        catch (IOException e)
        {
            System.err.println("[ERRO] Arquivo: " + e.getMessage());
            System.exit(3);
        }
    }
}
