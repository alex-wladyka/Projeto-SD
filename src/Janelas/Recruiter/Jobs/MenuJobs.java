package Janelas.Recruiter.Jobs;

import Janelas.Recruiter.MenuPrincipalRecruiter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class MenuJobs extends JFrame {
    private JPanel panel1;
    private JButton cadastrarJobButton;
    private JButton deletarJobButton;
    private JButton modificarJobButton;
    private JButton verificarJobSetButton;
    private JButton buscarJobButton;
    private JButton voltarButton;

    public MenuJobs(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Menu Jobs");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cadastrarJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroJobs(reader, out, in, token);
                setVisible(false);
            }
        });
        buscarJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JobLookupJanela(reader, out, in, token);
                setVisible(false);
            }
        });
        verificarJobSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LookupJobset_Janela(reader, out, in, token);
                setVisible(false);
            }
        });
        modificarJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JobUpdateJanela(reader, out, in, token);
                setVisible(false);
            }
        });
        deletarJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JobDeleteJanela(reader, out, in, token);
                setVisible(false);
            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalRecruiter(reader, out, in, token);
                setVisible(false);
            }
        });
    }
}
