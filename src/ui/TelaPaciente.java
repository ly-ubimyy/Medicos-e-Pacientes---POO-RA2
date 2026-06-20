package ui;

import busca.BuscaPaciente;
import busca.EscritorResultados;
import excecoes.RegistroNaoEncontradoException;
import modelo.BaseDados;
import modelo.Consulta;
import modelo.Medico;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Interface gráfica do Paciente.
 * Permite realizar as 3 pesquisas descritas no enunciado:
 *  1. Médicos do paciente (já consultados ou com consulta agendada).
 *  2. Consultas passadas do paciente com determinado médico.
 *  3. Consultas futuras (agendadas) do paciente.
 *
 * Todo resultado exibido na tela também é gravado automaticamente
 * em um arquivo de log (resultados/log_paciente.txt).
 */
public class TelaPaciente extends JFrame {

    private static final String ARQUIVO_LOG = "resultados/log_paciente.txt";

    private final BaseDados baseDados;

    private JTextField campoCpfPaciente;
    private JTextField campoCodigoMedico;
    private JTextArea areaResultado;

    public TelaPaciente(BaseDados baseDados) {
        super("Médicos e Pacientes - Interface do Paciente");
        this.baseDados = baseDados;
        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 520);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painelPrincipal.add(montarPainelFormulario(), BorderLayout.NORTH);
        painelPrincipal.add(montarPainelResultado(), BorderLayout.CENTER);

        setContentPane(painelPrincipal);
    }

    private JPanel montarPainelFormulario() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createTitledBorder("Dados da busca"));

        // Linha: CPF do paciente (comum às 3 buscas)
        JPanel linhaCpf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaCpf.add(new JLabel("CPF do paciente:"));
        campoCpfPaciente = new JTextField(14);
        linhaCpf.add(campoCpfPaciente);
        painel.add(linhaCpf);

        // Botão 1: listar médicos do paciente
        JPanel linhaBotao1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoMedicos = new JButton("1. Listar médicos do paciente");
        botaoMedicos.addActionListener(e -> buscarMedicosDoPaciente());
        linhaBotao1.add(botaoMedicos);
        painel.add(linhaBotao1);

        painel.add(new JSeparator());

        // Linha: código do médico + botão da busca 2
        JPanel linhaCodigo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaCodigo.add(new JLabel("Código do médico:"));
        campoCodigoMedico = new JTextField(8);
        linhaCodigo.add(campoCodigoMedico);
        painel.add(linhaCodigo);

        JPanel linhaBotao2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoConsultasPassadas = new JButton("2. Consultas passadas com esse médico");
        botaoConsultasPassadas.addActionListener(e -> buscarConsultasPassadasComMedico());
        linhaBotao2.add(botaoConsultasPassadas);
        painel.add(linhaBotao2);

        painel.add(new JSeparator());

        // Botão 3: consultas futuras do paciente (usa só o CPF)
        JPanel linhaBotao3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoConsultasFuturas = new JButton("3. Listar consultas agendadas (futuras)");
        botaoConsultasFuturas.addActionListener(e -> buscarConsultasFuturasDoPaciente());
        linhaBotao3.add(botaoConsultasFuturas);
        painel.add(linhaBotao3);

        return painel;
    }

    private JPanel montarPainelResultado() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Resultado"));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultado);
        painel.add(scroll, BorderLayout.CENTER);

        JLabel rodape = new JLabel("Todo resultado exibido aqui é salvo automaticamente em: " + ARQUIVO_LOG);
        rodape.setFont(new Font("SansSerif", Font.ITALIC, 11));
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    // ---------- Ação 1 ----------
    private void buscarMedicosDoPaciente() {
        try {
            String cpf = lerCpf();

            List<Medico> medicos = BuscaPaciente.listarMedicosDoPaciente(baseDados, cpf);
            String resultado = EscritorResultados.formatarMedicos(medicos);

            exibirEGravar("Médicos do paciente " + cpf, resultado);
        }
        catch (RegistroNaoEncontradoException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ---------- Ação 2 ----------
    private void buscarConsultasPassadasComMedico() {
        try {
            String cpf = lerCpf();
            int codigoMedico = Integer.parseInt(campoCodigoMedico.getText().trim());

            List<Consulta> consultas = BuscaPaciente.listarConsultasPassadasComMedico(
                    baseDados, cpf, codigoMedico
            );
            String resultado = EscritorResultados.formatarConsultas(consultas);

            String titulo = String.format(
                    "Consultas passadas do paciente %s com o médico %d", cpf, codigoMedico
            );
            exibirEGravar(titulo, resultado);
        }
        catch (NumberFormatException e) {
            mostrarErro("Código do médico inválido. Informe um número inteiro.");
        }
        catch (RegistroNaoEncontradoException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ---------- Ação 3 ----------
    private void buscarConsultasFuturasDoPaciente() {
        try {
            String cpf = lerCpf();

            List<Consulta> consultas = BuscaPaciente.listarConsultasAgendadasDoPaciente(baseDados, cpf);
            String resultado = EscritorResultados.formatarConsultas(consultas);

            exibirEGravar("Consultas agendadas (futuras) do paciente " + cpf, resultado);
        }
        catch (RegistroNaoEncontradoException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ---------- Auxiliares ----------

    private String lerCpf() {
        return campoCpfPaciente.getText().trim();
    }

    private void exibirEGravar(String titulo, String resultado) {
        areaResultado.setText("== " + titulo + " ==\n\n" + resultado);

        try {
            EscritorResultados.salvarResultado(ARQUIVO_LOG, titulo, resultado);
        }
        catch (IOException e) {
            mostrarErro("Resultado exibido na tela, mas houve um erro ao salvar o log: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro na busca", JOptionPane.ERROR_MESSAGE);
    }
}
