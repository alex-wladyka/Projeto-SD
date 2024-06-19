package Janelas.Recruiter;


import Janelas.MenuRole;
import Janelas.Recruiter.Jobs.MenuJobs;
import Recruiter.RecruiterDelete;
import Recruiter.RecruiterLogout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MenuPrincipalRecruiter extends JFrame {
    private JPanel panel1;
    private JButton verificarDadosButton;
    private JButton atualizarDadosDeCadastroButton;
    private JButton deletarDadosButton;
    private JButton vagasButton;
    private JButton logoutButton;

    public MenuPrincipalRecruiter(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Menu Principal");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        verificarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new JanelaLookupRecruiter(reader,out,in,token);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        atualizarDadosDeCadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaUpdateRecruiter(reader,out,in,token);
                setVisible(false);
            }
        });
        deletarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RecruiterDelete.deleteProcess(reader,out,in,token);
                    JOptionPane.showMessageDialog(null,"Dados excluidos com sucesso");
                    new MenuRole(reader,out,in);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        vagasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuJobs(reader, out, in, token);
                setVisible(false);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String newToken = RecruiterLogout.logoutProcess(out,in,token);
                    new MenuRole(reader,out,in);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
