package Janelas.Recruiter;

import Candidate.CandidateSignUp;
import Janelas.JanelaLogin;
import Janelas.MenuLogin;
import Recruiter.RecruiterSignUp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaSignupRecruiter extends JFrame {
    private JPanel panel1;
    private JTextField emailField;
    private JTextField senhaField;
    private JTextField nameField;
    private JTextField industryField;
    private JTextField descricaoField;
    private JButton continuarButton;
    private JButton voltarButton;



    public JanelaSignupRecruiter(PrintWriter out, BufferedReader in) {

        setContentPane(panel1);
        setTitle("Janela SignUp Recruiter");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = RecruiterSignUp.SignupProcess(out,in,emailField.getText(),senhaField.getText(),nameField.getText(),industryField.getText(),descricaoField.getText());
                    if (status.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Conta cadastrada com sucesso!");
                        new JanelaLogin(1,out,in);
                        setVisible(false);
                    }
                    else if (status.equals("USER_EXISTS")) {
                        JOptionPane.showMessageDialog(null, "Email já cadastrado","Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Erro ao cadastrar","Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuLogin(1,out,in);
                setVisible(false);
            }
        });
    }


}
