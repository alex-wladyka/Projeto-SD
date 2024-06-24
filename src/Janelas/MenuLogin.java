package Janelas;

import Janelas.Candidate.JanelaSignupCandidato;
import Janelas.Recruiter.JanelaSignupRecruiter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MenuLogin extends JFrame {
    private JPanel panel1;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton voltarButton;

    public MenuLogin(int role, PrintWriter out, BufferedReader in) {

        setContentPane(panel1);
        setTitle("Janela Menu Login");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaLogin(role,out, in);
                setVisible(false);
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (role == 0){
                    new JanelaSignupCandidato(out, in);
                    setVisible(false);
                }
                else{
                    new JanelaSignupRecruiter(out, in);
                    setVisible(false);
                }
            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuRole(out,in);
                setVisible(false);
            }
        });
    }
}
