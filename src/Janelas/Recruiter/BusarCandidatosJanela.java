package Janelas.Recruiter;

import Candidate.Candidate;
import Recruiter.ChooseCandidate;
import Recruiter.SearchCandidate.SearchCandidate_All;
import Recruiter.SearchCandidate.SearchCandidate_Experience;
import Recruiter.SearchCandidate.SearchCandidate_Skill;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BusarCandidatosJanela extends JFrame {
    private JPanel panel1;
    private JPanel condition;
    private JPanel conditionExperience;
    private JTextField expField;
    private JComboBox filterComboBox;
    private JComboBox skillExpComboBox;
    private JTextField skillExpField;
    private JList<Candidate> candidateSetlist;
    private String selectedValue = null;
    private JButton voltarButton;
    private JButton buscarButton;
    private JButton enviarMensagemButton;
    private JPanel mensagemPanel;
    DefaultListModel<Candidate> model = new DefaultListModel();
    String filter = null;

    public BusarCandidatosJanela(PrintWriter out, BufferedReader in, String token) {
        setContentPane(panel1);
        setTitle("Buscar Candidatos");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        conditionExperience.setVisible(false);
        mensagemPanel.setVisible(false);
        candidateSetlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalRecruiter(out,in,token);
                setVisible(false);
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                candidateSetlist.clearSelection();
                model.clear();
                candidateSetlist.setModel(model);
                List<Candidate> profile = null;
                if(condition.isVisible()){ //Vaga Selecionada
                    if(conditionExperience.isVisible()){ //Condição selecionadas
                        try {
                            profile = SearchCandidate_All.SearchJobAllProcess(out,in,token,skillExpField.getText(),expField.getText(),filter);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    else {
                        try {
                            profile = SearchCandidate_Skill.SearchCandidate_Skill(out,in,token,skillExpField.getText());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                else {
                    try {
                        profile = SearchCandidate_Experience.SearchJobExperienceProcess(out,in,token,skillExpField.getText());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if (profile.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nenhuma vaga Encontrada","Aviso",JOptionPane.INFORMATION_MESSAGE);
                    mensagemPanel.setVisible(false);
                }
                else{
                    for (int i = 0; i < profile.size(); i++) {
                        model.addElement(profile.get(i));
                    }
                    candidateSetlist.setModel(model);
                    mensagemPanel.setVisible(true);
                }
            }
        });
        skillExpComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                condition.setVisible(skillExpComboBox.getSelectedIndex() == 0);
            }
        });
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(filterComboBox.getSelectedItem().toString().equals("-"))) {
                    conditionExperience.setVisible(true);
                    if (filterComboBox.getSelectedIndex() == 1) {
                        filter = "AND";
                    }
                    else if (filterComboBox.getSelectedIndex() == 2) {
                        filter = "OR";
                    }
                }
                else {
                    conditionExperience.setVisible(false);
                }
            }
        });
        enviarMensagemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (candidateSetlist.getSelectedIndex() != -1) {
                    selectedValue = candidateSetlist.getSelectedValue().getId();
                    try {
                        String status = ChooseCandidate.ChooseCandidateProcess(out,in,token,selectedValue);

                        if(status.equals("SUCCESS")){
                          JOptionPane.showMessageDialog(null,"Cadastrado com sucesso");
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"Erro ao mandar mensagem","Erro",JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Nenhum Candidato Selecionado","Aviso",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
