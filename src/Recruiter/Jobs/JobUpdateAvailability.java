package Recruiter.Jobs;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobUpdateAvailability {

    public static String UpdateAvaliabilityProcess(PrintWriter out, BufferedReader in, String token, String id, String avaliabity) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("SET_JOB_AVAILABLE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        data.addProperty("available", avaliabity);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        return jsonReturn.get("status").getAsString();

    }

}
