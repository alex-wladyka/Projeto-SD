package Candidate;

import Recruiter.Jobs.Jobs;
import Recruiter.Recruiter;
import Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GetMessages {

    public static List<Recruiter> GetMessagesProcess(PrintWriter out, BufferedReader in, String token) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("GET_COMPANY");
        jsonRequest.addProperty("token", token);
        JsonObject data = new JsonObject();
        jsonRequest.add("data", data);


        System.out.println("Client:"+jsonRequest);
        String jsonResponse = JsonUtils.sendRequest(jsonRequest, out, in);
        System.out.println("Server:"+jsonResponse);

        JsonObject dataResponse = JsonUtils.parseJson(jsonResponse).getAsJsonObject("data");
        JsonArray companySet = dataResponse.getAsJsonArray("company");
        List<Recruiter> companies = new ArrayList<>();

        for (int i = 0; i < dataResponse.get("company_size").getAsInt(); i++) {
            JsonObject company = companySet.get(i).getAsJsonObject();
            String name = company.get("name").getAsString();
            String industry = company.get("industry").getAsString();
            String email = company.get("email").getAsString();
            String description = company.get("description").getAsString();

            companies.add(new Recruiter(email, name, industry, description));
        }

        return companies;
    }
}
