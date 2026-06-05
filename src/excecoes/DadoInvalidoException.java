package excecoes;

public class DadoInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public DadoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
