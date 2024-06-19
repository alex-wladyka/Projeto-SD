package Janelas.Candidate;

import Candidate.CandidateDelete;
import Candidate.CandidateLogout;
import Janelas.Candidate.Skills.MenuSkills;
import Janelas.MenuRole;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MenuPrincipalCandidato extends JFrame {
    private JPanel panel1;
    private JButton verificarDadosButton;
    private JButton atualizarDadosDeCadastroButton;
    private JButton deletarDadosButton;
    private JButton skillsButton;
    private JButton logoutButton;
    private JButton buscarVagasButton;


    public MenuPrincipalCandidato(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Menu Principal");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String newToken = CandidateLogout.logoutProcess(out,in,token);
                    new MenuRole(reader,out,in);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        verificarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new JanelaLookupCandidate(reader,out,in,token);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
        });
        atualizarDadosDeCadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaUpdateCandidato(reader, out, in, token);
                setVisible(false);
            }
        });
        deletarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CandidateDelete.deleteProcess(reader,out,in,token);
                    JOptionPane.showMessageDialog(null,"Dados excluidos com sucesso");
                    new MenuRole(reader,out,in);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        skillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuSkills(reader, out, in, token);
                setVisible(false);
            }
        });
        buscarVagasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchJobjanela(reader, out, in, token);
                setVisible(false);
            }
        });
    }
}
