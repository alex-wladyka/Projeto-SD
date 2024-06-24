package Janelas.Candidate.Skills;

import Candidate.Skills.SkillInclude;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CadastroSkills extends JFrame {
    private JPanel panel1;
    private JTextField skillField;
    private JSpinner experienceSpinner;
    private JButton voltarButton;
    private JButton cadastrarButton;

    public CadastroSkills(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Cadastro Skills");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = SkillInclude.IncludeSkillProcess(out,in,token,skillField.getText(),experienceSpinner.getValue().toString());

                    switch(status) {
                        case "SUCCESS":
                            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
                            new MenuSkills(out, in, token);
                            setVisible(false);
                            break;
                        case "SKILL_EXISTS":
                            JOptionPane.showMessageDialog(null, "A skill já está cadastrada","Aviso",JOptionPane.WARNING_MESSAGE);
                            break;
                        case "SKILL_NOT_EXIST":
                            JOptionPane.showMessageDialog(null, "A skill não existe","Aviso",JOptionPane.WARNING_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Erro ao fazer o cadastro","Aviso",JOptionPane.ERROR_MESSAGE);
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
}
