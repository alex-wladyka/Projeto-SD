package Candidate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

public class CandidateLogin {

    public static String LoginProcess (PrintWriter out, BufferedReader in, String email, String password ) throws IOException {


        JsonObject jsonRequest = JsonUtils.createRequest("LOGIN_CANDIDATE");
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest);
        String responseJson = JsonUtils.sendRequest(jsonRequest,out,in);
        System.out.println("Server: "+responseJson);

        JsonObject jsonResponse = JsonUtils.parseJson(responseJson);
        String status = jsonResponse.get("status").getAsString();

        switch (status) {
            case "SUCCESS":
                return jsonResponse.getAsJsonObject("data").get("token").getAsString();
            case "INVALID_LOGIN":
                break;
        }

        return null; //se o processo de login não ser completado com sucesso

    }

}
