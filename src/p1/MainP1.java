package p1;

import excecoes.DadoInvalidoException;
import modelo.BaseDados;
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

        }
        catch (DadoInvalidoException e)
        {
            System.err.println("[ERRO] Dado inválido: " + e.getMessage());
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("[ERRO] Arquivo: " + e.getMessage());
            System.exit(2);
        }
    }
}
