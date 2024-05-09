import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.JsonObject;


public class CandidateUpdate {
    public static void updateProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token) throws IOException {

        if(token == null || token.isEmpty()) {
            System.out.println("Você não está logado");
            return;
        }

        System.out.print("Novo e-mail: ");
        String email = reader.readLine();

        System.out.print("Nova senha: ");
        String password = reader.readLine();

        System.out.print("Novo nome: ");
        String name = reader.readLine();


        JsonObject jsonRequest = JsonUtils.createRequest("UPDATE_ACCOUNT_CANDIDATE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("name", name);
        jsonRequest.add("data", data);



        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("\n"+jsonResponse+"\n");

        JsonObject responseJson = JsonUtils.parseJson(jsonResponse);
        String status = responseJson.get("status").getAsString();

        switch (status) {
            case "SUCCESS":
                System.out.println("Dados alterados com sucesso!");
                return;
            case "INVALID_EMAIL":
                System.out.println("E-mail já cadastrado");
                break;
            default:
                System.out.println("Erro ao fazer atualizacao.");
                break;
        }

    }
}
