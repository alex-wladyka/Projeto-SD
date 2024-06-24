package Recruiter.SearchCandidate;

import Candidate.Candidate;
import Recruiter.Jobs.Jobs;
import Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SearchCandidate_All {

    public static List<Candidate> SearchJobAllProcess(PrintWriter out, BufferedReader in, String token, String skill, String experience, String filter) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("SEARCH_CANDIDATE");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();

        String [] skills = skill.split(",");
        JsonArray skillArray = new JsonArray();
        for (String skill1 : skills) {
            skillArray.add(skill1);
        }

        data.add("skill", skillArray);
        data.addProperty("experience", experience);
        data.addProperty("filter", filter);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject dataResponse = JsonUtils.parseJson(jsonResponse).getAsJsonObject("data");
        JsonArray jobSet = dataResponse.getAsJsonArray("profile");
        List<Candidate> candidates = new ArrayList<>();

        for (int i = 0; i < dataResponse.get("profile_size").getAsInt(); i++) {
            JsonObject profile = jobSet.get(i).getAsJsonObject();
            String skillName = profile.get("skill").getAsString();
            String experiencia = profile.get("experience").getAsString();
            String id = profile.get("id").getAsString();
            candidates.add(new Candidate(id,skillName,experiencia));
        }

        return candidates;
    }

}
