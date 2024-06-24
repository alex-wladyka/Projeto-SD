package Janelas.Candidate.Skills;

import Candidate.Skills.SkillLookup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SkillLookupJanela extends JFrame {
    private JPanel panel1;
    private JTextField skillField;
    private JLabel skillLabel;
    private JLabel experienceLabel;
    private JButton voltarButton;
    private JButton pesquisarButton;
    private static String skill;
    private static String experience;

    public SkillLookupJanela(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Lookup Skills");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = SkillLookup.LookupSkillProcess(out,in,token,skillField.getText());

                    switch(status) {
                        case "SUCCESS":
                            skillLabel.setText(skill);
                            experienceLabel.setText(experience);
                            break;
                        case "SKILL_NOT_FOUND":
                            JOptionPane.showMessageDialog(null,"A skill não pode ser encontrada.","Aviso",JOptionPane.WARNING_MESSAGE);
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
                new MenuSkills(out, in, token);
                setVisible(false);
            }
        });
    }

    public static void setSkill(String skill) {
        SkillLookupJanela.skill = skill;
    }

    public static void setExperience(String experience) {
        SkillLookupJanela.experience = experience;
    }
}
