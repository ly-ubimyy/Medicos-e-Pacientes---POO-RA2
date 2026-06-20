package ui;

import busca.BuscaMedico;
import busca.EscritorResultados;
import excecoes.RegistroNaoEncontradoException;
import modelo.BaseDados;
import modelo.Consulta;
import modelo.Medico;
import modelo.Paciente;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
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
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final BaseDados baseDados;

    private JTextField campoCodigoMedico;
    private JTextField campoDataInicial;
    private JTextField campoDataFinal;
    private JTextField campoMeses;
    private JTextArea areaResultado;

    public TelaMedico(BaseDados baseDados) {
        super("Médicos e Pacientes - Interface do Médico");
        this.baseDados = baseDados;
        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 560);
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

        // Linha: código do médico (comum às 3 buscas)
        JPanel linhaCodigo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaCodigo.add(new JLabel("Código do médico:"));
        campoCodigoMedico = new JTextField(8);
        linhaCodigo.add(campoCodigoMedico);
        painel.add(linhaCodigo);

        // Botão 1: listar pacientes do médico
        JPanel linhaBotao1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoPacientes = new JButton("1. Listar pacientes do médico");
        botaoPacientes.addActionListener(e -> buscarPacientesDoMedico());
        linhaBotao1.add(botaoPacientes);
        painel.add(linhaBotao1);

        painel.add(new JSeparator());

        // Linha: período (data inicial e final) + botão da busca 2
        JPanel linhaPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaPeriodo.add(new JLabel("Data inicial (dd/MM/yyyy):"));
        campoDataInicial = new JTextField(10);
        linhaPeriodo.add(campoDataInicial);
        linhaPeriodo.add(new JLabel("Data final (dd/MM/yyyy):"));
        campoDataFinal = new JTextField(10);
        linhaPeriodo.add(campoDataFinal);
        painel.add(linhaPeriodo);

        JPanel linhaBotao2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoConsultas = new JButton("2. Listar consultas no período");
        botaoConsultas.addActionListener(e -> buscarConsultasNoPeriodo());
        linhaBotao2.add(botaoConsultas);
        painel.add(linhaBotao2);

        painel.add(new JSeparator());

        // Linha: meses + botão da busca 3
        JPanel linhaMeses = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaMeses.add(new JLabel("Sem consulta há mais de (meses):"));
        campoMeses = new JTextField(5);
        linhaMeses.add(campoMeses);
        painel.add(linhaMeses);

        JPanel linhaBotao3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoSemConsulta = new JButton("3. Pacientes sem consulta recente");
        botaoSemConsulta.addActionListener(e -> buscarPacientesSemConsultaRecente());
        linhaBotao3.add(botaoSemConsulta);
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
    private void buscarPacientesDoMedico() {
        try {
            int codigo = lerCodigoMedico();

            List<Paciente> pacientes = BuscaMedico.listarPacientesDoMedico(baseDados, codigo);
            String resultado = EscritorResultados.formatarPacientes(pacientes);

            exibirEGravar("Pacientes do médico " + codigo, resultado);
        }
        catch (NumberFormatException e) {
            mostrarErro("Código do médico inválido. Informe um número inteiro.");
        }
        catch (RegistroNaoEncontradoException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ---------- Ação 2 ----------
    private void buscarConsultasNoPeriodo() {
        try {
            int codigo = lerCodigoMedico();
            LocalDate dataInicial = lerData(campoDataInicial.getText(), "Data inicial");
            LocalDate dataFinal = lerData(campoDataFinal.getText(), "Data final");

            if (dataFinal.isBefore(dataInicial)) {
                mostrarErro("A data final não pode ser anterior à data inicial.");
                return;
            }

            List<Consulta> consultas = BuscaMedico.listarConsultasDoMedicoNoPeriodo(
                    baseDados, codigo, dataInicial, dataFinal
            );
            String resultado = EscritorResultados.formatarConsultas(consultas);

            String titulo = String.format(
                    "Consultas do médico %d entre %s e %s",
                    codigo, dataInicial.format(FMT_DATA), dataFinal.format(FMT_DATA)
            );
            exibirEGravar(titulo, resultado);
        }
        catch (NumberFormatException e) {
            mostrarErro("Código do médico inválido. Informe um número inteiro.");
        }
        catch (DateTimeParseException e) {
            mostrarErro("Data inválida. Use o formato dd/MM/yyyy.");
        }
        catch (RegistroNaoEncontradoException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ---------- Ação 3 ----------
    private void buscarPacientesSemConsultaRecente() {
        try {
            int codigo = lerCodigoMedico();
            int meses = Integer.parseInt(campoMeses.getText().trim());

            if (meses < 0) {
                mostrarErro("O número de meses não pode ser negativo.");
                return;
            }

            List<Paciente> pacientes = BuscaMedico.listarPacientesSemConsultaHaMaisDeMeses(
                    baseDados, codigo, meses
            );
            String resultado = EscritorResultados.formatarPacientes(pacientes);

            String titulo = String.format(
                    "Pacientes do médico %d sem consulta há mais de %d mês(es)", codigo, meses
            );
            exibirEGravar(titulo, resultado);
        }
        catch (NumberFormatException e) {
            mostrarErro("Verifique o código do médico e o número de meses (devem ser números inteiros).");
        }
        catch (RegistroNaoEncontradoException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ---------- Auxiliares ----------

    private int lerCodigoMedico() {
        return Integer.parseInt(campoCodigoMedico.getText().trim());
    }

    private LocalDate lerData(String texto, String nomeCampo) {
        if (texto == null || texto.isBlank()) {
            throw new DateTimeParseException(nomeCampo + " não informada.", texto == null ? "" : texto, 0);
        }
        return LocalDate.parse(texto.trim(), FMT_DATA);
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
