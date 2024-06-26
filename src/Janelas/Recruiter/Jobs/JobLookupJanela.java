package Janelas.Recruiter.Jobs;

import Recruiter.Jobs.JobLookup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobLookupJanela extends JFrame {
    private JPanel panel1;
    private JTextField IDField;
    private JButton pesquisarButton;
    private JLabel skillLabel;
    private JLabel experienceLabel;
    private JButton voltarButton;
    private JLabel idLabel;
    private JLabel disponivelLabel;
    private JLabel divulgavelLabel;
    private static String skill;
    private static String experience;
    private static String available;
    private static String searchable;

    public JobLookupJanela(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Lookup Jobs");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = JobLookup.LookupSkillProcess(out,in,token,IDField.getText());

                    switch(status) {
                        case "SUCCESS":
                            skillLabel.setText(skill);
                            experienceLabel.setText(experience);
                            idLabel.setText(IDField.getText());
                            disponivelLabel.setText(available);
                            divulgavelLabel.setText(searchable);
                            break;
                        case "JOB_NOT_FOUND":
                            JOptionPane.showMessageDialog(null,"A vaga não pode ser encontrada.","Aviso",JOptionPane.WARNING_MESSAGE);
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
                new MenuJobs(out,in,token);
                setVisible(false);
            }
        });
    }

    public static void setSkill(String skill) {
        JobLookupJanela.skill = skill;
    }

    public static void setExperience(String experience) {
        JobLookupJanela.experience = experience;
    }

    public static void setAvailable(String available) { JobLookupJanela.available = available; }

    public static void setSearchable(String searchable) { JobLookupJanela.searchable = searchable; }
}
