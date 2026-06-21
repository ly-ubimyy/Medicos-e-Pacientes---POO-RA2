package ui;

import busca.BuscaMedico;
import busca.EscritorResultados;
import excecoes.RegistroNaoEncontradoException;
import modelo.BaseDados;
import modelo.Consulta;
import modelo.Paciente;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.format.DateTimeParseException;

/*
 * Interface gráfica do Médico.
 * Permite realizar as 3 pesquisas descritas no enunciado:
 *  1. Pacientes de um médico.
 *  2. Consultas de um médico em um período.
 *  3. Pacientes que não consultam há mais de X meses.
 *
 * Todo resultado exibido na tela também é gravado automaticamente
 * em um arquivo de log (resultados/log_medico.txt), atendendo ao
 * requisito de o P2 escrever resultados parciais/finais em arquivo.
 */

public class TelaMedico extends JFrame {

    private static final String ARQUIVO_LOG = "resultados/log_medico.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final BaseDados baseDados;

    private JTextField campoCodigo;
    private JTextField campoInicio;
    private JTextField campoFim;
    private JTextField campoMeses;

    private JTextArea areaResultado;

    public TelaMedico(BaseDados baseDados) {
        super("Médicos e Pacientes - Interface do Médico");
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
        setSize(780, 620);
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

        JLabel titulo = new JLabel("🩺 Área do Médico");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(41, 128, 185));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Consultas, pacientes e histórico médico");
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

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        form.add(secaoPacientes());
        form.add(Box.createVerticalStrut(10));
        form.add(secaoConsultasPeriodo());
        form.add(Box.createVerticalStrut(10));
        form.add(secaoPacientesInativos());

        return form;
    }

    private JPanel secaoPacientes() {

        JPanel p = criarSecao("Pacientes do médico");

        campoCodigo = new JTextField(8);

        JButton btn = new JButton("Pesquisar");
        estilizarBotao(btn);
        btn.addActionListener(e -> buscarPacientes());

        p.add(new JLabel("Código:"));
        p.add(campoCodigo);
        p.add(btn);

        return p;
    }

    private JPanel secaoConsultasPeriodo() {

        JPanel p = criarSecao("Consultas por período");

        campoInicio = new JTextField(10);
        campoFim = new JTextField(10);

        JButton btn = new JButton("Pesquisar");
        estilizarBotao(btn);
        btn.addActionListener(e -> buscarConsultasPeriodo());

        p.add(new JLabel("Início:"));
        p.add(campoInicio);

        p.add(new JLabel("Fim:"));
        p.add(campoFim);

        p.add(btn);

        return p;
    }

    private JPanel secaoPacientesInativos() {

        JPanel p = criarSecao("Pacientes sem consulta recente");

        campoMeses = new JTextField(5);

        JButton btn = new JButton("Pesquisar");
        estilizarBotao(btn);
        btn.addActionListener(e -> buscarInativos());

        p.add(new JLabel("Meses:"));
        p.add(campoMeses);
        p.add(btn);

        return p;
    }

    private JPanel criarSecao(String titulo) {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(titulo));

        return p;
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

    private JPanel montarResultado() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Resultado"));
        p.setBackground(Color.WHITE);

        areaResultado = new JTextArea();
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResultado.setEditable(false);
        areaResultado.setBackground(new Color(248, 249, 250));

        p.add(new JScrollPane(areaResultado), BorderLayout.CENTER);

        JLabel rodape = new JLabel(
                "Todo resultado exibido aqui é salvo automaticamente em: "
                        + ARQUIVO_LOG);

        rodape.setFont(
                new Font("SansSerif", Font.ITALIC, 11));

        p.add(rodape, BorderLayout.SOUTH);

        return p;
    }

    // Ação 1
    private void buscarPacientes() {

        try {

            int codigo = lerCodigoMedico();

            List<Paciente> pacientes = BuscaMedico.listarPacientesDoMedico(baseDados, codigo);

            String resultado = EscritorResultados.formatarPacientes(pacientes);

            mostrar(
                    "Pacientes do médico " + codigo,
                    resultado);
        } catch (NumberFormatException e) {
            erro("Código do médico inválido.");
        } catch (RegistroNaoEncontradoException e) {
            erro(e.getMessage());
        }
    }

    // Ação 2
    private void buscarConsultasPeriodo() {

        try {

            int codigo = lerCodigoMedico();

            LocalDate inicio = lerData(campoInicio.getText(), "Data inicial");

            LocalDate fim = lerData(campoFim.getText(), "Data final");

            if (fim.isBefore(inicio)) {
                erro("A data final não pode ser anterior à data inicial.");
                return;
            }

            List<Consulta> consultas = BuscaMedico.listarConsultasDoMedicoNoPeriodo(
                    baseDados,
                    codigo,
                    inicio,
                    fim);

            String resultado = EscritorResultados.formatarConsultas(consultas);

            String titulo = String.format(
                    "Consultas do médico %d entre %s e %s",
                    codigo,
                    inicio.format(FMT),
                    fim.format(FMT));

            mostrar(titulo, resultado);
        } catch (NumberFormatException e) {
            erro("Código do médico inválido.");
        } catch (DateTimeParseException e) {
            erro("Use datas no formato dd/MM/yyyy.");
        } catch (RegistroNaoEncontradoException e) {
            erro(e.getMessage());
        }
    }

    // Ação 3
    private void buscarInativos() {

        try {

            int codigo = lerCodigoMedico();

            int meses = Integer.parseInt(campoMeses.getText().trim());

            if (meses < 0) {
                erro("O número de meses não pode ser negativo.");
                return;
            }

            List<Paciente> pacientes = BuscaMedico.listarPacientesSemConsultaHaMaisDeMeses(
                    baseDados,
                    codigo,
                    meses);

            String resultado = EscritorResultados.formatarPacientes(pacientes);

            String titulo = String.format(
                    "Pacientes do médico %d sem consulta há mais de %d mês(es)",
                    codigo,
                    meses);

            mostrar(titulo, resultado);
        } catch (NumberFormatException e) {
            erro("Código do médico e meses devem ser números inteiros.");
        } catch (RegistroNaoEncontradoException e) {
            erro(e.getMessage());
        }
    }

    // Auxiliares
    private int lerCodigoMedico() {
        return Integer.parseInt(campoCodigo.getText().trim());
    }

    private LocalDate lerData(String texto, String nomeCampo) {

        if (texto == null || texto.isBlank()) {
            throw new DateTimeParseException(
                    nomeCampo + " não informada.",
                    texto == null ? "" : texto,
                    0);
        }

        return LocalDate.parse(texto.trim(), FMT);
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