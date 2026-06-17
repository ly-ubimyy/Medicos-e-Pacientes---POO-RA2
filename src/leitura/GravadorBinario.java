package leitura;

import modelo.BaseDados;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GravadorBinario {

    // salva a BaseDados inteira em arquivo binário
    // é usado no P1 -> P1 le csv e vria os abjetos -> salva em .dat
    public static void salvar(BaseDados baseDados, String caminhoArquivo)
            throws IOException {

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {

            // Aqui o Java grava o objeto inteiro no arquivo
            output.writeObject(baseDados);
        }
    }

    // carrega BaseDados a partir do arquivo bin
    // é usado no P2 -> P2 ñ le csv, só restaura o .dat criado pelo P1
    public static BaseDados carregar(String caminhoArquivo)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(caminhoArquivo))) {

            // le o objeto no arquivo e transforma de volta em BaseDados
            return (BaseDados) input.readObject();
        }
    }
}