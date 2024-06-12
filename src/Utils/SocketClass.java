package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketClass {

    public static Socket socket;

    static {
        try {
            socket = SocketClass.getSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket getSocket() throws IOException {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("IP do Servidor: ");

            return new Socket(reader.readLine(), 21234);
        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}
