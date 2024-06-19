package Recruiter.Jobs;

import Candidate.Skills.Skills;
import Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JobSet_Lookup {

    public static List<Jobs> LookupJobSetProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("LOOKUP_JOBSET");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
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
            int experience = job.get("experience").getAsInt();
            int id = job.get("id").getAsInt();
            jobs.add(new Jobs(id,skillName,experience));
        }

        return jobs;





    }

}
