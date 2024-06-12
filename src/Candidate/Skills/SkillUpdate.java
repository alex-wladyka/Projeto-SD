package Candidate.Skills;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SkillUpdate {
    public static String IUpdateSkillProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token, String skill, String novaSkill, String experience) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("UPDATE_SKILL");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("skill", skill);
        data.addProperty("experience", experience);
        data.addProperty("newSkill", novaSkill);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        return jsonReturn.get("status").getAsString();
    }
}
