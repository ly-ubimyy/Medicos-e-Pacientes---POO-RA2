package modelo;

import excecoes.DadoInvalidoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class PessoaAbstrata implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    protected List<Consulta> consultas;

    public PessoaAbstrata(String nome) {
        this.nome = nome;
        this.consultas = new ArrayList<>();
    }

    public abstract String getResumo();

    public abstract void validarIdentificador() throws DadoInvalidoException;

    @Override
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void adicionarConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s  (id: %s)",
                getClass().getSimpleName(), getNome(), getIdentificador());
    }
}
