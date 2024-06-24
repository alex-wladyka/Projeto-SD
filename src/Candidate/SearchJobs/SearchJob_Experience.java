package Candidate.SearchJobs;

import Recruiter.Jobs.Jobs;
import Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SearchJob_Experience {
    public static List<Jobs> SearchJobExperienceProcess(PrintWriter out, BufferedReader in, String token, String experience) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("SEARCH_JOB");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();


        data.addProperty("experience", experience);
        jsonRequest.add("data", data);
        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject dataResponse = JsonUtils.parseJson(jsonResponse).getAsJsonObject("data");
        JsonArray jobSet = dataResponse.getAsJsonArray("jobset");
        List<Jobs> jobs = new ArrayList<Jobs>();

        for (int i = 0; i < dataResponse.get("jobset_size").getAsInt(); i++) {
            JsonObject job = jobSet.get(i).getAsJsonObject();
            String skillName = job.get("skill").getAsString();
            int experiencia = job.get("experience").getAsInt();
            int id = job.get("id").getAsInt();
            jobs.add(new Jobs(id,skillName,experiencia));
        }

        return jobs;
    }
}
