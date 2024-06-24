package Janelas.Recruiter.Jobs;

import Recruiter.Jobs.JobUpdateAvailability;
import Recruiter.Jobs.JobUpdateSearch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobUpdateDisponibility extends JFrame {
    private JPanel panel1;
    private JCheckBox disponivelCheckBox;
    private JCheckBox divulgavelCheckBox;
    private JTextField idField;
    private JButton voltarButton;
    private JButton alterarButton;
    private String disponivel;
    private String procuravel;

    public JobUpdateDisponibility(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Modificar Disponibilidade Jobs");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuUpdateJobs(out, in, token);
                setVisible(false);
            }
        });
        alterarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (disponivelCheckBox.isSelected()) {
                    disponivel = "YES";
                }
                else{
                    disponivel = "NO";
                }

                if (divulgavelCheckBox.isSelected()) {
                    procuravel = "YES";
                }
                else{
                    procuravel = "NO";
                }

                try {
                    String status = JobUpdateAvailability.UpdateAvaliabilityProcess(out,in,token,idField.getText(),disponivel);

                    switch (status){
                        case "SUCCESS":
                           status = JobUpdateSearch.UpdateSearchableProcess(out,in,token,idField.getText(),procuravel);
                           if(status.equals("SUCCESS")){
                               JOptionPane.showMessageDialog(null, "Informações alteradas com sucesso!");
                           }
                        break;
                        case "JOB_NOT_FOUND":
                            JOptionPane.showMessageDialog(null, "A vaga não foi encontrada","Aviso", JOptionPane.WARNING_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Erro ao realizar alterações","Erro", JOptionPane.ERROR_MESSAGE);
                            break;
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
