package modelo;

import excecoes.DadoInvalidoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Medico extends PessoaAbstrata implements Serializable {

    private static final long serialVersionUID = 1L;

    private int codigo;
    private List<Paciente> pacientes;

    public Medico(String nome, int codigo) {
        super(nome);
        this.codigo = codigo;
        this.pacientes = new ArrayList<>();
        try
        {
            validarIdentificador();
        }
        catch (DadoInvalidoException e)
        {
            System.err.println("[Medico] Dado inválido ao criar médico: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public String getIdentificador() {
        return String.valueOf(codigo);
    }

    @Override
    public void validarIdentificador() throws DadoInvalidoException {
        if (codigo <= 0) {
            throw new DadoInvalidoException(
                    "Código do médico deve ser um inteiro positivo. Recebido: " + codigo);
        }
        if (getNome() == null || getNome().isBlank()) {
            throw new DadoInvalidoException("Nome do médico não pode ser vazio.");
        }
    }

    @Override
    public String getResumo() {
        return String.format("Médico: %-30s  Código: %4d  Pacientes: %d  Consultas: %d",
                getNome(), codigo, pacientes.size(), getConsultas().size());
    }

    public int getCodigo() {
        return codigo;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void adicionarPaciente(Paciente paciente) {
        if (!pacientes.contains(paciente)) {
            pacientes.add(paciente);
        }
    }
}
