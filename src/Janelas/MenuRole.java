package Janelas;

import Utils.SocketClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MenuRole extends JFrame {
    private JPanel principal;
    private JButton candidatoButton;
    private JButton recrutadorButton;
    private JButton sairButton;

    public MenuRole(BufferedReader reader, PrintWriter out, BufferedReader in) {
        setContentPane(principal);
        setTitle("Janela Menu Role");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SocketClass.socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(1);
            }
        });

        candidatoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuLogin(0,reader, out, in);
                setVisible(false);
            }
        });

        recrutadorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuLogin(1,reader, out, in);
                setVisible(false);
            }
        });
    }
}
