package Janelas.Candidate.Skills;

import Janelas.Candidate.MenuPrincipalCandidato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class MenuSkills extends JFrame {
    private JPanel panel1;
    private JButton cadastrarSkillButton;
    private JButton deletarSkillButton;
    private JButton modificarSkillButton;
    private JButton verificarSkillSetButton;
    private JButton buscarSkillButton;
    private JButton voltarButton;

    public MenuSkills(BufferedReader reader, PrintWriter out, BufferedReader in, String token) {
        setContentPane(panel1);
        setTitle("Janela Menu Skills");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalCandidato(reader,out,in,token);
                setVisible(false);
            }
        });


        cadastrarSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroSkills(reader,out,in,token);
                setVisible(false);
            }
        });
        buscarSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SkillLookupJanela(reader,out,in,token);
                setVisible(false);
            }
        });
        verificarSkillSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        modificarSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SkillUpdateJanela(reader,out,in,token);
                setVisible(false);
            }
        });
        deletarSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SkillDeleteJanela(reader,out,in,token);
                setVisible(false);
            }
        });
    }
}
