package Janelas;

import Candidate.CandidateLogin;
import Janelas.Candidate.MenuPrincipalCandidato;
import Janelas.Recruiter.MenuPrincipalRecruiter;
import Recruiter.RecruiterLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaLogin extends JFrame {
    private JPanel panel1;
    private JTextField emailField;

    private JButton continuarButton;
    private JButton voltarButton;
    private JPasswordField passwordField;

    public JanelaLogin(int role, PrintWriter out, BufferedReader in) {
        setContentPane(panel1);
        setTitle("Janela Login");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(role == 0){
                    try {
                        String tokenLogin = CandidateLogin.LoginProcess(out, in, emailField.getText(), passwordField.getText());
                        if(tokenLogin == null){
                            JOptionPane.showMessageDialog(null, "Email ou senha incorretos","Aviso",JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            new MenuPrincipalCandidato(out, in, tokenLogin);
                            setVisible(false);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    try {
                        String tokenLogin = RecruiterLogin.LoginProcess(out, in, emailField.getText(), passwordField.getText());
                        if(tokenLogin == null){
                            JOptionPane.showMessageDialog(null, "Email ou senha incorretos","Aviso",JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            new MenuPrincipalRecruiter(out, in, tokenLogin);
                            setVisible(false);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuLogin(role, out, in);
                setVisible(false);
            }
        });
    }
}
