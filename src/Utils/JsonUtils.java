package Utils;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JsonUtils {

    private static final Gson gson = new Gson();

    public static JsonObject createRequest(String operation) {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("operation", operation);
        return requestJson;
    }


    public static JsonObject parseJson(String json) {
        return gson.fromJson(json, JsonObject.class);
    }

    public static String toJsonString(JsonObject jsonObject) {
        return gson.toJson(jsonObject);
    }

    public static String sendRequest(JsonObject requestJson, PrintWriter out, BufferedReader in) throws IOException {
        out.println(requestJson.toString());
        return in.readLine();
    }

    public static JsonObject createResponse(String operation, String status, String token) {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("operation", operation);
        responseJson.addProperty("status", status);
        JsonObject data = new JsonObject();
        if (operation.equals("LOGIN_CANDIDATE") && status.equals("SUCCESS")) {
            data.addProperty("token", token);
        }
        responseJson.add("data", data);
        return responseJson;
    }


    public class JWTValidator {
        private static final String TOKEN_KEY = "DISTRIBUIDOS";
        private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
        private static final JWTVerifier verifier = JWT.require(algorithm).build();

        public static String generateToken(int id, String role) {
            return JWT.create()
                    .withClaim("id", id)
                    .withClaim("role", role)
                    .sign(algorithm);
        }

        public static int getIdClaim(String token) throws JWTVerificationException {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("id").asInt();
        }

        public static String getRoleClaim(String token) throws JWTVerificationException {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("role").asString();
        }
    }
}
