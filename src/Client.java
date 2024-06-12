import Candidate.*;
import Recruiter.*;

import java.io.*;
import java.net.*;

/*public class Client {
    public static void main(String[] args) {

        PrintWriter out = null;
        BufferedReader in = null;


        int opt=6;
        int select;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("IP do Servidor: ");
            Socket socket = new Socket(reader.readLine(),21234);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String token = null;

            while (true) {
                System.out.print("1. Candidato" +
                        "\n2. Empresa" +
                        "\n0. Sair" +
                        "\nEscolha uma opcao: ");
                try {
                    select = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida, digite novamente.");
                    continue;
                }
                if (select == 1 || select == 2) {
                    do {
                        System.out.print("1. Login" +
                                "\n2. Fazer cadastro" +
                                "\n0. Voltar para seleção" +
                                "\nEscolha uma opcao: ");
                        try {
                            opt = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Opção inválida, digite novamente.");
                            continue;
                        }
                        switch (opt) {
                            case 1:
                                token = switch (select) {
                                    case 1 -> CandidateLogin.LoginProcess(reader, out, in,"","");
                                    case 2 -> RecruiterLogin.LoginProcess(reader, out, in);
                                    default -> token;
                                };
                                if (token != null) {
                                    while (opt != 0) {
                                        System.out.print("1. Atualizar dados" +
                                                "\n2. Buscar Conta" +
                                                "\n3. Excluir Conta" +
                                                "\n0. Logout" +
                                                "\nEscolha uma opcao: ");
                                        try {
                                            opt = Integer.parseInt(reader.readLine());
                                        } catch (NumberFormatException e) {
                                            System.out.println("Opção inválida, digite novamente.");
                                            continue;
                                        }
                                        switch (opt) {
                                            case 1:
                                                switch (select) {
                                                    case 1:
                                                        CandidateUpdate.updateProcess(reader, out, in, token);
                                                        break;
                                                    case 2:
                                                        RecruiterUpdate.updateProcess(reader, out, in, token);
                                                        break;
                                                }

                                                break;
                                            case 2:
                                                switch (select) {
                                                    case 1:
                                                        CandidateLookup.LookupProcess(reader, out, in, token);
                                                        break;
                                                    case 2:
                                                        RecruiterLookup.LookupProcess(reader, out, in, token);
                                                        break;
                                                }
                                                break;
                                            case 3:
                                                switch (select) {
                                                    case 1:
                                                        CandidateDelete.deleteProcess(reader, out, in, token);
                                                        break;
                                                    case 2:
                                                        RecruiterDelete.deleteProcess(reader, out, in, token);
                                                        break;
                                                }
                                                token = null;
                                                opt = 0;
                                                break;
                                            case 0:
                                                token = switch (select) {
                                                    case 1 -> CandidateLogout.logoutProcess(out, in, token);
                                                    case 2 -> RecruiterLogout.logoutProcess(out, in, token);
                                                    default -> token;
                                                };

                                                System.out.println("Saindo");
                                                break;
                                            default:
                                                System.out.println("Opção inválida, digite novamente.");
                                        }
                                    }
                                }
                                break;


                            case 2:
                                switch (select) {
                                    case 1:
                                        CandidateSignUp.SignupProcess(reader, out, in);
                                        break;
                                    case 2:
                                        RecruiterSignUp.SignupProcess(reader, out, in);
                                        break;
                                }
                                break;

                            case 0:
                                break;

                            default:
                                System.out.println("Opção inválida, digite novamente.");
                                break;
                        }
                    } while (opt != 0);
                }
                else if (select == 0) {
                    System.out.println("Saindo do Programa");
                    socket.close();
                    System.exit(1);
                }
                else {
                    System.out.println("Opção inválida, digite novamente.");
                }
            }
        }catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.exit(1);
        }


    }


}*/