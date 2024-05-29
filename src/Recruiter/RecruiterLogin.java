package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterLogin {

    public static String LoginProcess (BufferedReader reader, PrintWriter out, BufferedReader in ) throws IOException {

        System.out.print("Digite o seu e-mail: ");
        String email = reader.readLine();

        System.out.print("Digite a senha: ");
        String password = reader.readLine();

        JsonObject jsonRequest = JsonUtils.createRequest("LOGIN_RECRUITER");
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        jsonRequest.add("data", data);

        String responseJson = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println("\n"+responseJson+"\n");

        JsonObject jsonResponse = JsonUtils.parseJson(responseJson);
        String status = jsonResponse.get("status").getAsString();

        switch (status) {
            case "SUCCESS":
                System.out.println("\nLogin feito com sucesso!");
                return jsonResponse.getAsJsonObject("data").get("token").getAsString();
            case "INVALID_LOGIN":
                System.out.println("\nUsuário ou senha incorretas. Tente novamente.");
                break;
            default:
                System.out.println("\nErro ao fazer login.");
                break;
        }

        return null; //se o processo de login não ser completado com sucesso

    }

}