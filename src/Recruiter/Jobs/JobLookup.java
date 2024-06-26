package Recruiter.Jobs;

import Janelas.Candidate.Skills.SkillLookupJanela;
import Janelas.Recruiter.Jobs.JobLookupJanela;
import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JobLookup {

    public static String LookupSkillProcess(PrintWriter out, BufferedReader in, String token, String id) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("LOOKUP_JOB");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject jsonReturn = JsonUtils.parseJson(jsonResponse);

        String status = jsonReturn.get("status").getAsString();

        if (status.equals("SUCCESS")) {
            JsonObject dataResponse = jsonReturn.get("data").getAsJsonObject();
            JobLookupJanela.setSkill(dataResponse.get("skill").getAsString());
            JobLookupJanela.setExperience(dataResponse.get("experience").getAsString());
            JobLookupJanela.setSearchable(dataResponse.get("searchable").getAsString());
            JobLookupJanela.setAvailable(dataResponse.get("available").getAsString());
        }

        return status;

    }

}
