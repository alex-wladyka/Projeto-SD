package Janelas.Candidate.Skills;

import Candidate.Skills.SkillDelete;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SkillDeleteJanela extends JFrame {
    private JPanel panel1;
    private JTextField skillField;
    private JButton deletarButton;
    private JButton voltarButton;
    private JPanel principal;

    public SkillDeleteJanela(PrintWriter out, BufferedReader in, String token) {

        setContentPane(principal);
        setTitle("Janela Delete Skills");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = SkillDelete.deleteProcess(out,in,token,skillField.getText());

                    switch(status){
                        case "SUCCESS":
                            JOptionPane.showMessageDialog(null,"A skill foi deletada com sucesso");
                            skillField.setText("");
                            break;
                        case "SKILL_NOT_FOUND":
                            JOptionPane.showMessageDialog(null,"A skill não foi encontrada","Aviso",JOptionPane.WARNING_MESSAGE);
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
}
