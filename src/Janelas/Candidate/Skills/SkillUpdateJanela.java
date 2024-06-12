package Janelas.Candidate.Skills;

import Candidate.Skills.SkillUpdate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SkillUpdateJanela extends JFrame{
    private JPanel panel1;
    private JTextField novaSkillField;
    private JSpinner experienceSpinner;
    private JButton voltarButton;
    private JButton atualizarButton;
    private JTextField skillAtualField;

    public SkillUpdateJanela(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Update Skills");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = SkillUpdate.IUpdateSkillProcess(reader,out,in,token,skillAtualField.getText(),novaSkillField.getText(),experienceSpinner.getValue().toString());

                    switch(status){
                        case "SUCCESS":
                            JOptionPane.showMessageDialog(null,"A skill foi alterada com sucesso");
                            new MenuSkills(reader, out, in, token);
                            setVisible(false);
                            break;
                        case "SKILL_NOT_FOUND":
                            JOptionPane.showMessageDialog(null,"A skill não foi encontrada","Aviso",JOptionPane.WARNING_MESSAGE);
                            break;
                        case "SKILL_EXISTS":
                            JOptionPane.showMessageDialog(null,"A skill já está cadastrada","Aviso",JOptionPane.WARNING_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null,"Houve um erro na atualização","Erro",JOptionPane.ERROR_MESSAGE);
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
                new MenuSkills(reader, out, in, token);
                setVisible(false);
            }
        });
    }
}
