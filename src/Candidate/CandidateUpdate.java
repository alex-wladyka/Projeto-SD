package Candidate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;


public class CandidateUpdate {
    public static String updateProcess(PrintWriter out, BufferedReader in, String token, String email, String password, String name) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("UPDATE_ACCOUNT_CANDIDATE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("name", name);
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server: "+jsonResponse);

        JsonObject responseJson = JsonUtils.parseJson(jsonResponse);

        return responseJson.get("status").getAsString();


    }
}
