package Janelas.Recruiter;

import Recruiter.RecruiterLookup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaLookupRecruiter extends JFrame {
    private JPanel panel1;
    private JLabel emailLabel;
    private JLabel descricaoLabel;
    private JLabel nomeLabel;
    private JLabel senhaLabel;
    private JLabel industryLabel;
    private JButton voltarButton;

    private static String email;
    private static String senha;
    private static String name;
    private static String industry;
    private static String descricao;

    public JanelaLookupRecruiter(PrintWriter out, BufferedReader in, String token) throws IOException {

        RecruiterLookup.LookupProcess(out,in,token);

        setContentPane(panel1);
        setTitle("Janela Lookup Recruiter");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        emailLabel.setText(email);
        senhaLabel.setText(senha);
        nomeLabel.setText(name);
        industryLabel.setText(industry);
        descricaoLabel.setText(descricao);

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalRecruiter(out, in, token);
                setVisible(false);
            }
        });
    }

    public static void setEmail(String email) {
        JanelaLookupRecruiter.email = email;
    }

    public static void setSenha(String senha) {
        JanelaLookupRecruiter.senha = senha;
    }

    public static void setNome(String name) {
        JanelaLookupRecruiter.name = name;
    }

    public static void setIndustry(String industry) {
        JanelaLookupRecruiter.industry = industry;
    }

    public static void setDescricao(String descricao) {
        JanelaLookupRecruiter.descricao = descricao;
    }
}
