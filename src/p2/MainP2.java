package p2;

import modelo.BaseDados;
import leitura.GravadorBinario;
import ui.TelaInicial;
import javax.swing.*;
import java.io.IOException;

public class MainP2 {

    private static final String DEFAULT_BIN = "dados/base.bin";

    public static void main(String[] args) {
        String arqBin = args.length > 0 ? args[0] : DEFAULT_BIN;

        try {
            // P2 não lê CSV: apenas restaura em memória os objetos persistentesque o P1 salvou em formato binário.
            BaseDados base = GravadorBinario.carregar(arqBin);

            // Abre a interface gráfiica passando a base já restaurada
            SwingUtilities.invokeLater(() -> {
                TelaInicial tela = new TelaInicial(base);
                tela.setVisible(true);
            });
        }
        catch (IOException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,
                "Erro ao carregar a base de dados:\n" + e.getMessage() +
                "\n\nVerifique se o programa P1 já foi executado para gerar o arquivo " + arqBin + ".",
                "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
