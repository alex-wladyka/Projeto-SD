package Recruiter.Jobs;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobUpdateSearch {
    public static String UpdateSearchableProcess(PrintWriter out, BufferedReader in, String token, String id, String searchable) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("SET_JOB_SEARCHABLE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        data.addProperty("searchable", searchable);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        return jsonReturn.get("status").getAsString();

    }
}
