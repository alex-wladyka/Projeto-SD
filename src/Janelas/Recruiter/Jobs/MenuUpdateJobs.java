package Janelas.Recruiter.Jobs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class MenuUpdateJobs extends JFrame {
    private JPanel panel1;
    private JButton alterarInformaçõesDaVagaButton;
    private JButton alterarDisponibilidadeDaVagaButton;
    private JButton voltarButton;

    public MenuUpdateJobs(PrintWriter out, BufferedReader in, String token) {
        setContentPane(panel1);
        setTitle("Janela Menu Update Jobs");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuJobs(out, in, token);
                setVisible(false);
            }
        });


        alterarInformaçõesDaVagaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JobUpdateJanela(out, in, token);
                setVisible(false);
            }
        });
        alterarDisponibilidadeDaVagaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JobUpdateDisponibility(out, in, token);
                setVisible(false);
            }
        });
    }
}
