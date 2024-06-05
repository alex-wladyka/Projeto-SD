package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterSignUp {

    public static void SignupProcess(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Endereço de Email: ");
        String email = reader.readLine();
        System.out.print("Senha: ");
        String password = reader.readLine();
        System.out.print("Nome: ");
        String name = reader.readLine();
        System.out.print("Industry: ");
        String industry = reader.readLine();
        System.out.print("Descricao: ");
        String description = reader.readLine();


        JsonObject jsonRequest = JsonUtils.createRequest("SIGNUP_RECRUITER");
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("name", name);
        data.addProperty("industry", industry);
        data.addProperty("description", description);
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println("Server: "+jsonResponse);
    }
}
