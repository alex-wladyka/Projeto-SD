package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterLookup {

    public static void LookupProcess(BufferedReader reader, PrintWriter out, BufferedReader in, String token) throws IOException {
        if(token == null || token.isEmpty()) {
            System.out.println("Você não está logado");
            return;
        }

        JsonObject requestJson = JsonUtils.createRequest("LOOKUP_ACCOUNT_RECRUITER");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        requestJson.add("data", dataJson);

        String responseJson = JsonUtils.sendRequest(requestJson,out,in);
        System.out.println("\n"+responseJson+"\n");

        JsonObject jsonResponse = JsonUtils.parseJson(responseJson);
        JsonObject data = jsonResponse.get("data").getAsJsonObject();

        System.out.println("Email: "+data.get("email").getAsString()+
                           "\nSenha: "+data.get("password").getAsString()+
                           "\nNome: "+data.get("name").getAsString()+
                           "\nIndustry: "+data.get("industry").getAsString()+
                           "\nDescrição: "+data.get("description").getAsString());
    }

}
