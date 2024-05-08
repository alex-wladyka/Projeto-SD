import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {

        PrintWriter out = null;
        BufferedReader in = null;


        int opt;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("IP do Servidor: ");
            Socket socket = new Socket(reader.readLine(),21235);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String token = null;

            while (true) {
                System.out.print("1. Login" +
                               "\n2. Fazer cadastro" +
                               "\n0. Sair" +
                               "\nEscolha uma opcao: ");
                try {
                    opt = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida, digite novamente.");
                    continue;
                }
                switch (opt) {
                    case 1:
                        token = CandidateLogin.LoginProcess(reader,out,in);
                        if (token != null) {
                            while (opt != 0){
                                System.out.print("1. Atualizar dados" +
                                        "\n2. Buscar Conta" +
                                        "\n3. Excluir Conta" +
                                        "\n0. Logout"+
                                        "\nEscolha uma opcao: ");
                                try {
                                    opt = Integer.parseInt(reader.readLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Opção inválida, digite novamente.");
                                    continue;
                                }
                                switch (opt) {
                                    case 1:
                                        token = CandidateUpdate.updateProcess(reader,out,in,token);
                                        break;
                                    case 2:
                                        System.out.println("Teste Buscar Conta");
                                        break;
                                    case 3:
                                        System.out.println("Teste Excluir Conta");
                                        break;
                                    case 0:
                                        System.out.println("Saindo");
                                        token = null;
                                        break;
                                    default:
                                        System.out.println("Opção inválida, digite novamente.");
                                }
                            }
                        }
                        break;


                    case 2:
                        CandidateSignUp.SignupProcess(reader,out,in);
                        break;

                    case 0:
                        System.out.println("Saindo do Programa");
                        socket.close();
                        System.exit(1);

                    default:
                        System.out.println("Opção inválida, digite novamente.");
                        break;
                }
            }
        }catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.exit(1);
        }


    }

}
