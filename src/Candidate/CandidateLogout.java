package Candidate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

public class CandidateLogout {
    public static String logoutProcess(PrintWriter out, BufferedReader in, String token) throws IOException {

        if(token == null || token.isEmpty()) {
            System.out.println("Você não está logado");
            return token;
        }

        JsonObject jsonRequest = JsonUtils.createRequest("LOGOUT_CANDIDATE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        jsonRequest.add("data", data);

        JsonUtils.sendRequest(jsonRequest, out, in);

        String jsonResponse = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println(jsonResponse);

        return null;
    }
}
