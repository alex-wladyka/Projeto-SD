package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class RecruiterUpdate {
    public static String updateProcess(PrintWriter out, BufferedReader in, String token, String email, String password, String name, String industry, String description) throws IOException {


        JsonObject jsonRequest = JsonUtils.createRequest("UPDATE_ACCOUNT_RECRUITER");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("name", name);
        data.addProperty("industry", industry);
        data.addProperty("description", description);
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest.toString());
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server: "+jsonResponse);

        JsonObject responseJson = JsonUtils.parseJson(jsonResponse);
        String status = responseJson.get("status").getAsString();

        return status;

    }
}
