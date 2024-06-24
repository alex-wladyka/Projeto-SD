package Janelas.Recruiter.Jobs;

import Candidate.Skills.Skills;
import Recruiter.Jobs.JobSet_Lookup;
import Recruiter.Jobs.Jobs;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LookupJobset_Janela extends JFrame {
    private JPanel panel1;
    private JButton voltarButton;
    private JButton pesquisarButton;
    private JScrollPane jobSetPanel;
    DefaultListModel<Jobs> model = new DefaultListModel();
    private JList<Jobs> jobSetList;

    public LookupJobset_Janela(PrintWriter out, BufferedReader in, String token) {

        setContentPane(panel1);
        setTitle("Janela Lookup Jobset");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuJobs(out,in,token);
                setVisible(false);
            }
        });
        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.clear();
                    jobSetList.setModel(model);
                    List<Jobs> jobSet = JobSet_Lookup.LookupJobSetProcess(out,in,token);
                    //JScrollPane jScrollPane = new JScrollPane();

                    if (jobSet.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhuma vaga cadastrada","Aviso",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        for (int i = 0; i < jobSet.size(); i++) {
                            model.addElement(jobSet.get(i));
                        }
                        jobSetList.setModel(model);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
