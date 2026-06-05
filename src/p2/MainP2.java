package p2;

import modelo.BaseDados;
import ui.TelaInicial;
import javax.swing.*;
import java.io.IOException;

public class MainP2 {

    private static final String DEFAULT_BIN = "dados/base.bin";

    public static void main(String[] args) {
        String arqBin = args.length > 0 ? args[0] : DEFAULT_BIN;


        try {


        }
        catch (IOException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,
                "Erro ao carregar a base de dados:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
