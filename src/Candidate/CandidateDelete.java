package Candidate;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CandidateDelete {

    public static void deleteProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token) throws IOException {

        JsonObject requestJson = JsonUtils.createRequest("DELETE_ACCOUNT_CANDIDATE");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        requestJson.add("data", dataJson);

        System.out.println("Client:"+requestJson.toString());
        String responseJson = JsonUtils.sendRequest(requestJson,out,in);
        System.out.println("Server: "+responseJson);
    }

}
