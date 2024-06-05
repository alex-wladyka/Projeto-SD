package Candidate;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.*;

public class CandidateSignUp {

    public static void SignupProcess(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Endereço de Email: ");
        String email = reader.readLine();
        System.out.print("Senha:");
        String password = reader.readLine();
        System.out.print("Nome:");
        String name = reader.readLine();

        JsonObject jsonRequest = JsonUtils.createRequest("SIGNUP_CANDIDATE");
        JsonObject data = new JsonObject();
        //data.addProperty("id",1);
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("name", name);
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println("Server: "+jsonResponse);
    }
}
