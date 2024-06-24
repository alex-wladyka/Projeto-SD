package Recruiter.Jobs;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobDelete {
    public static String deleteProcess(PrintWriter out, BufferedReader in, String token, String id) throws IOException {

        JsonObject requestJson = JsonUtils.createRequest("DELETE_JOB");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("id", id);
        requestJson.add("data", dataJson);

        System.out.println("Client:"+requestJson.toString());
        String responseJson = JsonUtils.sendRequest(requestJson,out,in);
        System.out.println("Server:"+responseJson);

        JsonObject jsonReturn = JsonUtils.parseJson(responseJson);

        return jsonReturn.get("status").getAsString();
    }
}
