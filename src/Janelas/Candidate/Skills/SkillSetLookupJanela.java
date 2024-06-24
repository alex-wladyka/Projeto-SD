package Janelas.Candidate.Skills;

import Candidate.Skills.SkillSetLookup;
import Candidate.Skills.Skills;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SkillSetLookupJanela extends JFrame {
    private JPanel panel1;
    private JButton button_lookup;
    private JButton voltarButton;
    private JScrollPane skillsetPane;
    private DefaultListModel<Skills> model = new DefaultListModel();
    private JList<Skills> skillsetList;
    private JLabel skillsetLabel;



    public SkillSetLookupJanela(PrintWriter out, BufferedReader in, String token) {
        setContentPane(panel1);
        setTitle("Janela Lookup Skillset");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button_lookup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                skillsetList.setModel(model);
                try {
                    List<Skills> skillSet = SkillSetLookup.LookupSkillSetProcess(out,in,token);

                    for (int i = 0; i < skillSet.size(); i++) {
                        model.addElement(skillSet.get(i));
                    }
                    skillsetList.setModel(model);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuSkills(out,in,token);
                setVisible(false);
            }
        });
    }
}
