package Janelas.Recruiter.Jobs;

import Recruiter.Jobs.JobDelete;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobDeleteJanela extends JFrame {
    private JPanel panel1;
    private JTextField IDField;
    private JButton deletarButton;
    private JButton voltarButton;

    public JobDeleteJanela(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Delete Jobs");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = JobDelete.deleteProcess(reader,out,in,token,IDField.getText());

                    switch(status){
                        case "SUCCESS":
                            JOptionPane.showMessageDialog(null,"A vaga foi deletada com sucesso");
                            IDField.setText("");
                            break;
                        case "JOB_NOT_FOUND":
                            JOptionPane.showMessageDialog(null,"A vaga não foi encontrada","Aviso",JOptionPane.WARNING_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null,"Houve um erro na pesquisa","Erro",JOptionPane.ERROR_MESSAGE);
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
                new MenuJobs(reader,out,in,token);
                setVisible(false);
            }
        });
    }
}
