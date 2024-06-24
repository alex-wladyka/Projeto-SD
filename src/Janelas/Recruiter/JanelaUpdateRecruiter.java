package Janelas.Recruiter;

import Recruiter.RecruiterUpdate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaUpdateRecruiter extends JFrame {
    private JPanel panel1;
    private JTextField emailField;
    private JTextField senhaField;
    private JTextField nomeField;
    private JTextField industryField;
    private JTextField descricaoField;
    private JButton continuarButton;
    private JButton voltarButton;

    public JanelaUpdateRecruiter(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Update Recruiter");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = RecruiterUpdate.updateProcess(out,in,token,emailField.getText(),senhaField.getText(),nomeField.getText(),industryField.getText(),descricaoField.getText());

                    switch (status) {
                        case "SUCCESS":
                            JOptionPane.showMessageDialog(null, "Dados Alterados com Sucesso!", "Sucesso", JOptionPane.PLAIN_MESSAGE);
                            new MenuPrincipalRecruiter(out,in,token);
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
                new MenuPrincipalRecruiter(out, in, token);
                setVisible(false);
            }
        });
    }
}
