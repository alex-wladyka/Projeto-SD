package Candidate.Skills;

import Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SkillSetLookup {
    public static List<Skills> LookupSkillSetProcess(PrintWriter out, BufferedReader in, String token) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("LOOKUP_SKILLSET");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject dataResponse = JsonUtils.parseJson(jsonResponse).getAsJsonObject("data");
        JsonArray skillSet = dataResponse.getAsJsonArray("skillset");
        List<Skills> skills = new ArrayList<Skills>();



        for (int i = 0; i < dataResponse.get("skillset_size").getAsInt(); i++) {
            JsonObject skill = skillSet.get(i).getAsJsonObject();
            String skillName = skill.get("skill").getAsString();
            int experience = skill.get("experience").getAsInt();
            skills.add(new Skills(skillName,experience));
        }

        return skills;



    }
}
