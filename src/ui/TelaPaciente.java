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

/*
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

        configurarLookAndFeel();
        montarTela();
    }

    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void montarTela() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 600);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBackground(Color.WHITE);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        principal.add(montarTopo(), BorderLayout.NORTH);
        principal.add(montarCentro(), BorderLayout.CENTER);

        setContentPane(principal);
    }

    private JPanel montarTopo() {

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("👤 Área do Paciente");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(41, 128, 185));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Consultas e médicos vinculados ao paciente");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(5));
        topo.add(subtitulo);

        return topo;
    }

    private JPanel montarCentro() {

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(Color.WHITE);

        centro.add(montarFormulario(), BorderLayout.NORTH);
        centro.add(montarResultado(), BorderLayout.CENTER);

        return centro;
    }

    // Form
    private JPanel montarFormulario() {

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);

        painel.add(secaoMedicos());
        painel.add(Box.createVerticalStrut(10));
        painel.add(secaoConsultasPassadas());
        painel.add(Box.createVerticalStrut(10));
        painel.add(secaoConsultasFuturas());

        return painel;
    }

    private JPanel secaoMedicos() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder("Médicos do paciente"));

        campoCpfPaciente = new JTextField(14);

        JButton btn = new JButton("Pesquisar");
        estilizarBotao(btn);
        btn.addActionListener(e -> buscarMedicosDoPaciente());

        p.add(new JLabel("CPF:"));
        p.add(campoCpfPaciente);
        p.add(btn);

        return p;
    }

    private JPanel secaoConsultasPassadas() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder("Consultas passadas"));

        campoCodigoMedico = new JTextField(8);

        JButton btn = new JButton("Pesquisar");
        estilizarBotao(btn);
        btn.addActionListener(e -> buscarConsultasPassadasComMedico());

        p.add(new JLabel("Médico (código):"));
        p.add(campoCodigoMedico);
        p.add(btn);

        return p;
    }

    private JPanel secaoConsultasFuturas() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder("Consultas futuras"));

        JButton btn = new JButton("Pesquisar");
        estilizarBotao(btn);
        btn.addActionListener(e -> buscarConsultasFuturasDoPaciente());

        p.add(btn);

        return p;
    }

    private JPanel montarResultado() {

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Resultado"));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResultado.setBackground(new Color(248, 249, 250));
        areaResultado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaResultado);
        painel.add(scroll, BorderLayout.CENTER);

        JLabel rodape = new JLabel("Log: " + ARQUIVO_LOG);
        rodape.setFont(new Font("SansSerif", Font.ITALIC, 11));
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private void estilizarBotao(JButton btn) {
        btn.setBackground(new Color(41, 128, 185));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
    }

    // Ação 1
    private void buscarMedicosDoPaciente() {
        try {
            String cpf = lerCpf();

            List<Medico> medicos = BuscaPaciente.listarMedicosDoPaciente(baseDados, cpf);
            String resultado = EscritorResultados.formatarMedicos(medicos);

            mostrar("Médicos do paciente " + cpf, resultado);

        } catch (RegistroNaoEncontradoException e) {
            erro(e.getMessage());
        } catch (IllegalArgumentException e) {
            erro(e.getMessage());
        }
    }

    // Ação 2
    private void buscarConsultasPassadasComMedico() {
        try {
            String cpf = lerCpf();
            int cod = Integer.parseInt(campoCodigoMedico.getText().trim());

            List<Consulta> consultas = BuscaPaciente.listarConsultasPassadasComMedico(baseDados, cpf, cod);

            String resultado = EscritorResultados.formatarConsultas(consultas);

            mostrar(
                    "Consultas passadas do paciente " + cpf +
                            " com médico " + cod,
                    resultado);

        } catch (NumberFormatException e) {
            erro("Código do médico inválido.");
        } catch (RegistroNaoEncontradoException e) {
            erro(e.getMessage());
        } catch (IllegalArgumentException e) {
            erro(e.getMessage());
        }
    }

    // Ação 3
    private void buscarConsultasFuturasDoPaciente() {
        try {
            String cpf = lerCpf();

            List<Consulta> consultas = BuscaPaciente.listarConsultasAgendadasDoPaciente(baseDados, cpf);

            String resultado = EscritorResultados.formatarConsultas(consultas);

            mostrar(
                    "Consultas futuras do paciente " + cpf,
                    resultado);

        } catch (RegistroNaoEncontradoException e) {
            erro(e.getMessage());
        } catch (IllegalArgumentException e) {
            erro(e.getMessage());
        }
    }

    // Auxiliares

    private String lerCpf() {

        String cpf = campoCpfPaciente.getText().trim();

        if (cpf.isBlank()) {
            throw new IllegalArgumentException("Informe o CPF do paciente.");
        }

        return cpf;
    }

    private void mostrar(String titulo, String texto) {
        areaResultado.setText("== " + titulo + " ==\n\n" + texto);

        // Requisito RA3:
        // grava resultados parciais e finais em arquivo texto
        try {
            EscritorResultados.salvarResultado(ARQUIVO_LOG, titulo, texto);
        } catch (IOException e) {
            erro("Resultado exibido na tela, mas houve erro ao salvar o log: " + e.getMessage());
        }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}