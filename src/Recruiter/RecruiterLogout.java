package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterLogout {
    public static String logoutProcess(PrintWriter out, BufferedReader in, String token) throws IOException {

        if(token == null || token.isEmpty()) {
            System.out.println("Você não está logado");
            return token;
        }

        JsonObject jsonRequest = JsonUtils.createRequest("LOGOUT_RECRUITER");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        jsonRequest.add("data", data);

        JsonUtils.sendRequest(jsonRequest, out, in);

        System.out.println("Client:"+jsonRequest.toString());
        String jsonResponse = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println("Server: "+jsonResponse);

        return null;
    }
}
