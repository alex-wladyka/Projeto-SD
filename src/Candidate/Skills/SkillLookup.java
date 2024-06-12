package Candidate.Skills;

import Janelas.Candidate.Skills.SkillLookupJanela;
import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SkillLookup {
    public static String LookupSkillProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token, String skill) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("LOOKUP_SKILL");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("skill", skill);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        String status = jsonReturn.get("status").getAsString();

        if (status.equals("SUCCESS")) {
            JsonObject dataResponse = jsonReturn.get("data").getAsJsonObject();
            SkillLookupJanela.setSkill(dataResponse.get("skill").getAsString());
            SkillLookupJanela.setExperience(dataResponse.get("experience").getAsString());
        }

        return status;

    }
}
