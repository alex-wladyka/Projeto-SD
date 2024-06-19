package Janelas.Candidate;

import Candidate.SearchJobs.SearchJob_All;
import Candidate.SearchJobs.SearchJob_Experience;
import Candidate.SearchJobs.SearchJob_Skill;
import Recruiter.Jobs.Jobs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SearchJobjanela extends JFrame{
    private JPanel panel1;
    private JList<Jobs> jobSetlist;
    private JComboBox comboBox1;
    private JTextField skillExpField;
    private JComboBox comboBox2;
    private JTextField expField;
    private JPanel condition;
    private JPanel conditionExperience;
    private JButton voltarButton;
    private JButton buscarButton;
    DefaultListModel<Jobs> model = new DefaultListModel();
    String filter= null;

    public SearchJobjanela(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Buscar Vagas");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox1.getSelectedIndex() == 0) {
                    condition.setVisible(true);
                }
                else {
                    condition.setVisible(false);
                }
            }
        });

        comboBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!(comboBox2.getSelectedItem().toString().equals("-"))) {
                    conditionExperience.setVisible(true);
                    if (comboBox2.getSelectedIndex() == 1) {
                        filter = "AND";
                    }
                    else if (comboBox2.getSelectedIndex() == 2) {
                        filter = "OR";
                    }
                }
                else {
                    System.out.println("AAAaaaa");
                    conditionExperience.setVisible(false);
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalCandidato(reader,out,in,token);
                setVisible(false);
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                jobSetlist.setModel(model);
                List<Jobs> jobSet = null;
                if(condition.isVisible()){ //Vaga Selecionada
                    if(conditionExperience.isVisible()){ //Condição selecionadas
                        try {
                            jobSet = SearchJob_All.SearchJobAllProcess(reader,out,in,token,skillExpField.getText(),expField.getText(),filter);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    else {
                        try {
                            jobSet = SearchJob_Skill.SearchJob_Skill(reader,out,in,token,skillExpField.getText());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                else {
                    try {
                        jobSet = SearchJob_Experience.SearchJobExperienceProcess(reader,out,in,token,skillExpField.getText());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if (jobSet.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nenhuma vaga Encontrada","Aviso",JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    for (int i = 0; i < jobSet.size(); i++) {
                        model.addElement(jobSet.get(i));
                    }
                    jobSetlist.setModel(model);
                }
            }
        });
    }
}
