package Janelas.Candidate;

import Candidate.GetMessages;
import Recruiter.Recruiter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class VerificarMensagensJanela extends JFrame {
    private JPanel panel1;
    private JButton voltarButton;
    private JScrollPane skillsetPane;
    private JList<Recruiter> companySetList;
    private JPanel principal;
    private DefaultListModel<Recruiter> model = new DefaultListModel();

    public VerificarMensagensJanela(PrintWriter out, BufferedReader in, String token) {

        setContentPane(principal);
        setTitle("Janela Mensagens");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model.clear();
        companySetList.setModel(model);
        try {
            List<Recruiter> companySet = GetMessages.GetMessagesProcess(out, in, token);
            if(companySet.size() > 0) {
                for (int i = 0; i < companySet.size(); i++) {
                    model.addElement(companySet.get(i));
                }
                companySetList.setModel(model);
            }
            else {
                JOptionPane.showMessageDialog(null, "Nenhuma mensagem encontrada.","Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MenuPrincipalCandidato(out, in, token);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
