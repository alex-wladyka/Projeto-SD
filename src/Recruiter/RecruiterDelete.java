package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterDelete {

    public static void deleteProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token) throws IOException {
        if(token == null || token.isEmpty()) {
            System.out.println("Você não está logado");
            return;
        }

        JsonObject requestJson = JsonUtils.createRequest("DELETE_ACCOUNT_RECRUITER");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        requestJson.add("data", dataJson);

        System.out.println("Client:"+requestJson);

        String responseJson = JsonUtils.sendRequest(requestJson,out,in);

        System.out.println("Server: "+responseJson);
    }

}
