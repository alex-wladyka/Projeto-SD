package Recruiter;

import Janelas.Recruiter.JanelaLookupRecruiter;
import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterLookup {

    public static void LookupProcess(PrintWriter out, BufferedReader in, String token) throws IOException {

        JsonObject requestJson = JsonUtils.createRequest("LOOKUP_ACCOUNT_RECRUITER");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        requestJson.add("data", dataJson);

        System.out.println("Client:"+requestJson);
        String responseJson = JsonUtils.sendRequest(requestJson,out,in);
        System.out.println("Server: "+responseJson);

        JsonObject jsonResponse = JsonUtils.parseJson(responseJson);
        JsonObject data = jsonResponse.get("data").getAsJsonObject();

        JanelaLookupRecruiter.setEmail(data.get("email").getAsString());
        JanelaLookupRecruiter.setSenha(data.get("password").getAsString());
        JanelaLookupRecruiter.setNome(data.get("name").getAsString());
        JanelaLookupRecruiter.setIndustry(data.get("industry").getAsString());
        JanelaLookupRecruiter.setDescricao(data.get("description").getAsString());
    }

}
