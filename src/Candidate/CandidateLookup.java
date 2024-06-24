package Candidate;

import Janelas.Candidate.JanelaLookupCandidate;
import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CandidateLookup {

    public static void LookupProcess(PrintWriter out, BufferedReader in, String token) throws IOException {
        if(token == null || token.isEmpty()) {
            System.out.println("Você não está logado");
            return;
        }

        JsonObject requestJson = JsonUtils.createRequest("LOOKUP_ACCOUNT_CANDIDATE");
        requestJson.addProperty("token", token);
        JsonObject dataJson = new JsonObject();
        requestJson.add("data", dataJson);

        System.out.println("Client:"+requestJson);
        String responseJson = JsonUtils.sendRequest(requestJson,out,in);
        System.out.println("Server: "+responseJson);

        JsonObject jsonResponse = JsonUtils.parseJson(responseJson);
        JsonObject data = jsonResponse.get("data").getAsJsonObject();

        JanelaLookupCandidate.setEmail(data.get("email").getAsString());
        JanelaLookupCandidate.setSenha(data.get("password").getAsString());
        JanelaLookupCandidate.setNome(data.get("name").getAsString());

    }

}
