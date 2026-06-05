package modelo;

import java.io.Serializable;

public interface Identificavel extends Serializable {
    String getIdentificador();

    String getNome();
}
