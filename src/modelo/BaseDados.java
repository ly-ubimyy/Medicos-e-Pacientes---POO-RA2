package modelo;

import excecoes.RegistroNaoEncontradoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseDados implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Consulta> consultas;

    public BaseDados() {
        this.medicos = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.consultas = new ArrayList<>();
    }

    /**
     * Busac um médico pelo cpdigo.
     * Da throws RegistroNaoEncontradoException se nenhum médico tiver esse código
     */

    public Medico buscarMedico(int codigo) throws RegistroNaoEncontradoException {
        return medicos.stream()
                .filter(m -> m.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new RegistroNaoEncontradoException("Médico com código " + codigo + " não encontrado."));
    }

    /**
     * O Msm com pacientes
     */
    public Paciente buscarPaciente(String cpf) throws RegistroNaoEncontradoException {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return pacientes.stream()
                .filter(p -> p.getCpf().equals(cpfLimpo))
                .findFirst()
                .orElseThrow(() -> new RegistroNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado."));
    }

    public void adicionarMedico(Medico m) { medicos.add(m); }
    public void adicionarPaciente(Paciente p) { pacientes.add(p); }
    public void adicionarConsulta(Consulta c) { consultas.add(c); }

    public List<Medico> getMedicos() { return medicos; }
    public List<Paciente> getPacientes() { return pacientes; }
    public List<Consulta> getConsultas() { return consultas; }
}
