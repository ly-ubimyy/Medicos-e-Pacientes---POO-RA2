package ui;

import modelo.BaseDados;

import javax.swing.*;
import java.awt.*;

/*
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

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(500, 350);
                setLocationRelativeTo(null);
                setResizable(false);

                JPanel painelPrincipal = new JPanel();
                painelPrincipal.setLayout(new BorderLayout(15, 15));
                painelPrincipal.setBorder(
                                BorderFactory.createEmptyBorder(25, 25, 25, 25));
                painelPrincipal.setBackground(Color.WHITE);

                JPanel painelTopo = new JPanel();
                painelTopo.setBackground(Color.WHITE);
                painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

                JLabel icone = new JLabel("🏥");
                icone.setFont(new Font("SansSerif", Font.PLAIN, 48));
                icone.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel titulo = new JLabel("Sistema Médicos e Pacientes");
                titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
                titulo.setForeground(new Color(41, 128, 185));
                titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel subtitulo = new JLabel("Selecione o perfil de acesso");
                subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
                subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

                painelTopo.add(icone);
                painelTopo.add(Box.createVerticalStrut(10));
                painelTopo.add(titulo);
                painelTopo.add(Box.createVerticalStrut(5));
                painelTopo.add(subtitulo);

                JLabel resumo = new JLabel(
                                String.format(
                                                "<html><center>" +
                                                                "<b>%d</b> médico(s)<br>" +
                                                                "<b>%d</b> paciente(s)<br>" +
                                                                "<b>%d</b> consulta(s)" +
                                                                "</center></html>",
                                                baseDados.getMedicos().size(),
                                                baseDados.getPacientes().size(),
                                                baseDados.getConsultas().size()),
                                SwingConstants.CENTER);

                resumo.setFont(new Font("SansSerif", Font.PLAIN, 14));

                JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 0, 10));
                painelBotoes.setBackground(Color.WHITE);

                JButton botaoMedico = new JButton("Sou Médico");
                estilizarBotao(botaoMedico);
                botaoMedico.addActionListener(e -> abrirTelaMedico());

                JButton botaoPaciente = new JButton("Sou Paciente");
                estilizarBotao(botaoPaciente);
                botaoPaciente.addActionListener(e -> abrirTelaPaciente());

                painelBotoes.add(botaoMedico);
                painelBotoes.add(botaoPaciente);

                painelPrincipal.add(painelTopo, BorderLayout.NORTH);
                painelPrincipal.add(resumo, BorderLayout.CENTER);
                painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

                setContentPane(painelPrincipal);
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

        private void abrirTelaMedico() {
                TelaMedico tela = new TelaMedico(baseDados);
                tela.setVisible(true);
        }

        private void abrirTelaPaciente() {
                TelaPaciente tela = new TelaPaciente(baseDados);
                tela.setVisible(true);
        }
}
