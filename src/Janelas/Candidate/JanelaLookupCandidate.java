package Janelas.Candidate;

import Candidate.CandidateLookup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaLookupCandidate extends JFrame {
    private JPanel panel1;
    private JLabel emailLabel;
    private JLabel senhaLabel;
    private JLabel nomeLabel;
    private JButton voltarButton;

    private static String email;
    private static String senha;
    private static String nome;


    public JanelaLookupCandidate(PrintWriter out, BufferedReader in, String token) throws IOException {

        CandidateLookup.LookupProcess(out,in,token);

        setContentPane(panel1);
        setTitle("Janela Lookup Candidate");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        emailLabel.setText(email);
        senhaLabel.setText(senha);
        nomeLabel.setText(nome);

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

    public static void setEmail(String emailnew) {
        email = emailnew;
    }

    public static void setSenha(String senhanew) {
        senha = senhanew;
    }

    public static void setNome(String nomenew) {
        nome = nomenew;
    }
}
