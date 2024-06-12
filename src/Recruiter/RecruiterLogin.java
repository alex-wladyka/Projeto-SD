package Recruiter;

import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecruiterLogin {

    public static String LoginProcess (BufferedReader reader, PrintWriter out, BufferedReader in, String email, String password) throws IOException {

        JsonObject jsonRequest = JsonUtils.createRequest("LOGIN_RECRUITER");
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        jsonRequest.add("data", data);

        System.out.println("Client:"+jsonRequest.toString());

        String responseJson = JsonUtils.sendRequest(jsonRequest,out,in);

        System.out.println("Server:"+responseJson);

        JsonObject jsonResponse = JsonUtils.parseJson(responseJson);
        String status = jsonResponse.get("status").getAsString();

        switch (status) {
            case "SUCCESS":
                return jsonResponse.getAsJsonObject("data").get("token").getAsString();
            case "INVALID_LOGIN":
                break;
            default:
                break;
        }

        return null; //se o processo de login não ser completado com sucesso

    }

}
