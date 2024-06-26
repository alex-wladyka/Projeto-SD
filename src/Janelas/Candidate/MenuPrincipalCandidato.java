package Janelas.Candidate;

import Candidate.CandidateDelete;
import Candidate.CandidateLogout;
import Candidate.GetMessages;
import Janelas.Candidate.Skills.MenuSkills;
import Janelas.MenuRole;
import Recruiter.Recruiter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MenuPrincipalCandidato extends JFrame {
    private JPanel panel1;
    private JButton verificarDadosButton;
    private JButton atualizarDadosDeCadastroButton;
    private JButton deletarDadosButton;
    private JButton skillsButton;
    private JButton logoutButton;
    private JButton buscarVagasButton;
    private JButton verMensagensButton;


    public MenuPrincipalCandidato(PrintWriter out, BufferedReader in, String token) throws IOException {

        List<Recruiter> companySet = GetMessages.GetMessagesProcess(out, in, token);

        if (!companySet.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você tem novas Mensagens","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }

        setContentPane(panel1);
        setTitle("Menu Principal");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String newToken = CandidateLogout.logoutProcess(out,in,token);
                    new MenuRole(out,in);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        verificarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new JanelaLookupCandidate(out,in,token);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
        });
        atualizarDadosDeCadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaUpdateCandidato(out, in, token);
                setVisible(false);
            }
        });
        deletarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CandidateDelete.deleteProcess(out,in,token);
                    JOptionPane.showMessageDialog(null,"Dados excluidos com sucesso");
                    String newToken = CandidateLogout.logoutProcess(out,in,token);
                    new MenuRole(out,in);
                    setVisible(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        skillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuSkills(out, in, token);
                setVisible(false);
            }
        });
        buscarVagasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchJobjanela(out, in, token);
                setVisible(false);
            }
        });
        verMensagensButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VerificarMensagensJanela(out, in, token);
                setVisible(false);
            }
        });
    }
}
