package Janelas.Candidate;

import Candidate.CandidateLogout;
import Candidate.CandidateUpdate;
import Janelas.MenuRole;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaUpdateCandidato extends JFrame{
    private JPanel panel1;
    private JTextField emailField;
    private JTextField senhaField;
    private JTextField nomeField;
    private JButton continuarButton;
    private JButton voltarButton;

    public JanelaUpdateCandidato(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Update Candidato");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = CandidateUpdate.updateProcess(out,in,token,emailField.getText(),senhaField.getText(),nomeField.getText());

                    switch (status) {
                        case "SUCCESS":
                            JOptionPane.showMessageDialog(null, "Dados Alterados com Sucesso!", "Sucesso", JOptionPane.PLAIN_MESSAGE);
                            String newToken = CandidateLogout.logoutProcess(out,in,token);
                            new MenuRole(out,in);
                            setVisible(false);
                            break;
                        case "INVALID_EMAIL":
                            JOptionPane.showMessageDialog(null, "Email já cadastrado","Aviso", JOptionPane.WARNING_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Erro ao realizar atualização", "Erro", JOptionPane.ERROR_MESSAGE);
                            break;
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MenuPrincipalCandidato(out,in,token);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
        });
    }
}
