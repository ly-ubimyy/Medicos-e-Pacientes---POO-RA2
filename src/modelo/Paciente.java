package modelo;

import excecoes.DadoInvalidoException;
import java.io.Serializable;

public class Paciente extends PessoaAbstrata implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpf;

    public Paciente(String nome, String cpf) {
        super(nome);
        this.cpf = cpf.replaceAll("[^0-9]", "");
        try
        {
            validarIdentificador();
        }
        catch (DadoInvalidoException e)
        {
            System.err.println("[Paciente] Dado inválido ao criar paciente: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public String getIdentificador() {
        return cpf;
    }

    @Override
    public void validarIdentificador() throws DadoInvalidoException {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new DadoInvalidoException("CPF inválido para '" + getNome() + "': '" + cpf + "'. Deve conter exatamente 11 dígitos numéricos.");
        }
        if (getNome() == null || getNome().isBlank()) {
            throw new DadoInvalidoException("Nome do paciente não pode ser vazio.");
        }
    }

    @Override
    public String getResumo() {
        return String.format("Paciente: %-30s  CPF: %s  Consultas: %d", getNome(), getCpfFormatado(), getConsultas().size());
    }

    public String getCpf() {
        return cpf;
    }

    public String getCpfFormatado() {
        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
}
