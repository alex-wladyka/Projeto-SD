package Candidate;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.*;

public class CandidateSignUp {

    public static String SignupProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String email, String password, String name) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("SIGNUP_CANDIDATE");
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("name", name);
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        return jsonReturn.get("status").getAsString();
    }
}
