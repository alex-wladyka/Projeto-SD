package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChooseCandidate {
    public static String ChooseCandidateProcess(PrintWriter out, BufferedReader in, String token, String id) throws IOException {
        JsonObject jsonRequest = JsonUtils.createRequest("CHOOSE_CANDIDATE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest.toString());
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server: "+jsonResponse);

        JsonObject responseJson = JsonUtils.parseJson(jsonResponse);

        return responseJson.get("status").getAsString();

    }
}
