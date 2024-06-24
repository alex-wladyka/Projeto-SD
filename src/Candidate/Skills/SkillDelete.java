package Candidate.Skills;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SkillDelete {
    public static String deleteProcess(PrintWriter out, BufferedReader in, String token, String Skill) throws IOException {

        JsonObject requestJson = JsonUtils.createRequest("DELETE_SKILL");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("skill", Skill);
        requestJson.add("data", dataJson);

        System.out.println("Client:"+requestJson.toString());
        String responseJson = JsonUtils.sendRequest(requestJson,out,in);
        System.out.println("Server:"+responseJson);

        JsonObject jsonReturn = JsonUtils.parseJson(responseJson);

        return jsonReturn.get("status").getAsString();
    }
}
