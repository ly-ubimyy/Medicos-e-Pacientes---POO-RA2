package excecoes;

public class RegistroNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public RegistroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RegistroNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
