import Janelas.MenuRole;
import Utils.SocketClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ClientJanelas {



    public static void main(String[] args) {

        PrintWriter out;
        BufferedReader in;


        try {

            out = new PrintWriter(SocketClass.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(SocketClass.socket.getInputStream()));
            new MenuRole(out, in);

        }catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.exit(1);
        }



    }

}