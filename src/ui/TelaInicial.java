package ui;

import modelo.BaseDados;

import javax.swing.*;
import java.awt.*;

/**
 * Tela inicial do P2.
 * Mostra dois botões: "Sou Médico" e "Sou Paciente".
 * Cada botão abre sua própria janela (TelaMedico ou TelaPaciente),
 * passando a BaseDados já restaurada do arquivo binário pelo MainP2.
 */
public class TelaInicial extends JFrame {

    private final BaseDados baseDados;

    public TelaInicial(BaseDados baseDados) {
        super("Médicos e Pacientes - Tela Inicial");
        this.baseDados = baseDados;

        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Quem está acessando o sistema?", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        painel.add(titulo, BorderLayout.NORTH);

        JLabel resumo = new JLabel(
                String.format(
                        "<html><center>Base carregada: %d médico(s), %d paciente(s), %d consulta(s)</center></html>",
                        baseDados.getMedicos().size(),
                        baseDados.getPacientes().size(),
                        baseDados.getConsultas().size()
                ),
                SwingConstants.CENTER
        );
        painel.add(resumo, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 15, 0));

        JButton botaoMedico = new JButton("Sou Médico");
        botaoMedico.setFont(new Font("SansSerif", Font.PLAIN, 14));
        botaoMedico.addActionListener(e -> abrirTelaMedico());

        JButton botaoPaciente = new JButton("Sou Paciente");
        botaoPaciente.setFont(new Font("SansSerif", Font.PLAIN, 14));
        botaoPaciente.addActionListener(e -> abrirTelaPaciente());

        painelBotoes.add(botaoMedico);
        painelBotoes.add(botaoPaciente);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    private void abrirTelaMedico() {
        TelaMedico tela = new TelaMedico(baseDados);
        tela.setVisible(true);
    }

    private void abrirTelaPaciente() {
        TelaPaciente tela = new TelaPaciente(baseDados);
        tela.setVisible(true);
    }
}
