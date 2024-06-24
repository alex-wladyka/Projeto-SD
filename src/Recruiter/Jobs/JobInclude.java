package Recruiter.Jobs;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobInclude {
    public static String IncludeSkillProcess(PrintWriter out, BufferedReader in, String token, String skill, String experience) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("INCLUDE_JOB");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("skill", skill);
        data.addProperty("experience", experience);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        return jsonReturn.get("status").getAsString();
    }

}
