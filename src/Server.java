import java.io.*;
import java.net.*;

import Candidate.Candidate;
import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Server extends Thread {

    private Socket clientSocket;
    private BufferedWriter fileWriter;
    private static final String DATABASE_FILE = "database.txt";

    public Server(Socket clientSoc, BufferedWriter writer) {
        clientSocket = clientSoc;
        fileWriter = writer;
        start();
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            boolean running = true;
            while (running) {
                String jsonMessage = in.readLine();

                if (jsonMessage != null && jsonMessage.equalsIgnoreCase("exit")) {
                    running = false;
                    continue;
                }

                if (jsonMessage != null) {
                    logWriter("Client", jsonMessage);
                    JsonObject requestJson = JsonUtils.parseJson(jsonMessage);
                    String operation = requestJson.get("operation").getAsString();


                    switch (operation) {
                        case "LOGIN_CANDIDATE":
                           LoginProcess(requestJson, out, "CANDIDATE");
                            break;
                        case "SIGNUP_CANDIDATE":
                            SignUpProcess(out, requestJson, "CANDIDATE");
                            break;
                        case "UPDATE_ACCOUNT_CANDIDATE":
                            UpdateCandidateProcess(out, requestJson);
                            break;
                        case "DELETE_ACCOUNT_CANDIDATE":
                            DeleteProcess(out, requestJson, "CANDIDATE");
                            break;
                        case "LOOKUP_ACCOUNT_CANDIDATE":
                            LookUpProcess(out, requestJson, "CANDIDATE");
                            break;
                        case "LOGOUT_CANDIDATE":
                            LogoutProcess(out, requestJson, "CANDIDATE");
                            break;
                        case "LOGIN_RECRUITER":
                            LoginProcess(requestJson, out, "RECRUITER");
                            break;
                        case "SIGNUP_RECRUITER":
                            SignUpProcess(out, requestJson, "RECRUITER");
                            break;
                        case "UPDATE_ACCOUNT_RECRUITER":
                            UpdateRecruiterProcess(out, requestJson);
                            break;
                        case "DELETE_ACCOUNT_RECRUITER":
                            DeleteProcess(out, requestJson, "RECRUITER");
                            break;
                        case "LOOKUP_ACCOUNT_RECRUITER":
                            LookUpProcess(out, requestJson, "RECRUITER");
                            break;
                        case "LOGOUT_RECRUITER":
                            LogoutProcess(out, requestJson, "RECRUITER");
                            break;
                        case "INCLUDE_SKILL":
                            IncludeSkillProcess(out, requestJson);
                            break;
                        case "LOOKUP_SKILL":
                            LookupSkillProcess(out, requestJson);
                            break;
                        case "LOOKUP_SKILLSET":
                            //LookupSkillsetProcess(out, requestJson);
                            break;
                        case "DELETE_SKILL":
                            DeleteSkillProcess(out, requestJson);
                            break;
                        case "UPDATE_SKILL":
                            UpdateSkillProcess(out, requestJson);
                            break;
                        case "INCLUDE_JOB":
                            IncludeJobProcess(out, requestJson);
                            break;
                        case "LOOKUP_JOB":
                            LookupJobProcess(out, requestJson);
                            break;
                        case "LOOKUP_JOBSET":
                            //LookupJobsetProcess(out, requestJson);
                            break;
                        case "DELETE_JOB":
                            DeleteJobProcess(out, requestJson);
                            break;
                        case "UPDATE_JOB":
                            UpdateJobProcess(out, requestJson);
                            break;
                        default:
                            JsonObject Response = JsonUtils.createResponse(operation, "INVALID_OPERATION", "");
                            out.println(JsonUtils.toJsonString(Response));
                            logWriter("Server", JsonUtils.toJsonString(Response));
                    }
                }
            }
        } catch (IOException | SQLException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (fileWriter != null) fileWriter.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }



    private void SignUpProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        String op= "";

        String email;
        String password;
        String name;
        String industry;
        String description;

        PreparedStatement st;
        ResultSet rs;


        switch (role) {
            case "CANDIDATE":
                if ((dataJson.get("email").getAsString() == null || dataJson.get("email").getAsString().isEmpty() ) || (dataJson.get("password").getAsString() == null || dataJson.get("password").getAsString().isEmpty()) || (dataJson.get("name").getAsString() == null || dataJson.get("name").getAsString().isEmpty())) {
                    JsonObject responseJson = JsonUtils.createResponse("SIGNUP_CANDIDATE", "INVALID_FIELD", "");
                    out.println(JsonUtils.toJsonString(responseJson));
                    logWriter("Server",JsonUtils.toJsonString(responseJson));
                    return;
                }

                op = "SIGNUP_CANDIDATE";

                email = dataJson.get("email").getAsString();
                password = dataJson.get("password").getAsString();
                name = dataJson.get("name").getAsString();




                st = Conexao.getConexao().prepareStatement("SELECT * FROM candidate WHERE Email = ?");
                st.setString(1, email);

                rs = st.executeQuery();

                if (rs.next()) {
                    JsonObject Response = JsonUtils.createResponse(op, "USER_EXISTS", "");

                    out.println(JsonUtils.toJsonString(Response));
                    logWriter("Server", JsonUtils.toJsonString(Response));
                    return;
                }
                else {

                    st = Conexao.getConexao().prepareStatement("INSERT INTO candidate (Email, Nome, senha) VALUES (?, ?, ?)");
                    st.setString(1, email);
                    st.setString(2, name);
                    st.setString(3, password);
                    st.executeUpdate();

                }
                break;

            case "RECRUITER":
                if ((dataJson.get("email").getAsString() == null || dataJson.get("email").getAsString().isEmpty() ) || (dataJson.get("password").getAsString() == null || dataJson.get("password").getAsString().isEmpty()) || (dataJson.get("name").getAsString() == null || dataJson.get("name").getAsString().isEmpty())) {
                    JsonObject responseJson = JsonUtils.createResponse("SIGNUP_CANDIDATE", "INVALID_FIELD", "");
                    out.println(JsonUtils.toJsonString(responseJson));
                    logWriter("Server",JsonUtils.toJsonString(responseJson));
                    return;
                }

                op = "SIGNUP_RECRUITER";

                email = dataJson.get("email").getAsString();
                password = dataJson.get("password").getAsString();
                name = dataJson.get("name").getAsString();
                industry = dataJson.get("industry").getAsString();
                description = dataJson.get("description").getAsString();


                st = Conexao.getConexao().prepareStatement("SELECT * FROM recruiter WHERE Email = ?");
                st.setString(1, email);

                rs = st.executeQuery();

                if (rs.next()) {
                    JsonObject Response = JsonUtils.createResponse(op, "USER_EXISTS", "");

                    out.println(JsonUtils.toJsonString(Response));
                    logWriter("Server", JsonUtils.toJsonString(Response));
                    return;
                }
                else {

                    st = Conexao.getConexao().prepareStatement("INSERT INTO recruiter (Email, Nome, senha, industry, description) VALUES (?, ?, ?, ?, ?)");
                    st.setString(1, email);
                    st.setString(2, name);
                    st.setString(3, password);
                    st.setString(4, industry);
                    st.setString(5, description);
                    st.executeUpdate();

                }
                break;
        }



        /*List<Candidate> candidates = readDatabase(); //criação da lista de usuarios

        if(candidates.isEmpty()) {
            id = 1;
        }
        else {
            Candidate lastCreated = candidates.get(candidates.size()-1);
            id = Integer.parseInt(lastCreated.getId()) + 1;
        }

        for (Candidate candidate : candidates) {//teste para ver se o email já foi cadastrado
            if (candidate.getEmail().equals(email)) { //encontrou um email igual
                JsonObject Response = JsonUtils.createResponse("SIGNUP_CANDIDATE", "USER_EXISTS", "");

                out.println(JsonUtils.toJsonString(Response));
                logWriter("Server", JsonUtils.toJsonString(Response));
                return;
            }
        }
        /*Candidate newUser = new Candidate(String.valueOf(id),email, password, name);
        candidates.add(newUser);
        writeDatabase(candidates);*/



        JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", "");
        out.println(JsonUtils.toJsonString(responseJson));
        logWriter("Server",JsonUtils.toJsonString(responseJson));
    }

    private void LoginProcess(JsonObject requestJson, PrintWriter out, String role) throws IOException, SQLException {

        String table = "";
        String op = "";

        switch (role){
            case "CANDIDATE":
                op = "LOGIN_CANDIDATE";
                table = "candidate";
                break;
            case "RECRUITER":
                op = "LOGIN_RECRUITER";
                table = "recruiter";
                break;
        }

        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if ((dataJson.get("email").getAsString() == null || dataJson.get("email").getAsString().isEmpty() ) || (dataJson.get("password").getAsString() == null || dataJson.get("password").getAsString().isEmpty())) {
            JsonObject responseJson = JsonUtils.createResponse(op, "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM "+table+" WHERE Email = ? AND senha = ?");
        st.setString(1, email);
        st.setString(2, password);

        ResultSet rs;
        rs = st.executeQuery();


        if (rs.next()) {
            int id = Integer.parseInt(rs.getString("id"));
            String token = JsonUtils.JWTValidator.generateToken(id, role);
            JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", token);
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;

        }

        JsonObject responseJson = JsonUtils.createResponse(op, "INVALID_LOGIN", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
    }

    private void UpdateCandidateProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();



        if ((dataJson.get("email").getAsString() == null || dataJson.get("email").getAsString().isEmpty() ) || (dataJson.get("password").getAsString() == null || dataJson.get("password").getAsString().isEmpty()) || (dataJson.get("name").getAsString() == null || dataJson.get("name").getAsString().isEmpty())) {
            JsonObject responseJson = JsonUtils.createResponse("UPDATE_ACCOUNT_CANDIDATE", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String token = requestJson.get("token").getAsString();
        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();
        String name = dataJson.get("name").getAsString();

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM candidate WHERE Email = ?");
        st.setString(1, email);

        ResultSet rs;
        rs = st.executeQuery();

        if (rs.next()) {
            JsonObject Response = JsonUtils.createResponse("UPDATE_ACCOUNT_CANDIDATE", "INVALID_EMAIL", "");

            out.println(JsonUtils.toJsonString(Response));
            logWriter("Server", JsonUtils.toJsonString(Response));
            return;
        }


        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);


            st = Conexao.getConexao().prepareStatement("UPDATE candidate SET Email = ?, Nome = ?, senha = ? WHERE Id = ?");
            st.setString(1, email);
            st.setString(2, name);
            st.setString(3, password);
            st.setInt(4, candidateId);
            st.executeUpdate();

            JsonObject responseJson = JsonUtils.createResponse("UPDATE_ACCOUNT_CANDIDATE", "SUCCESS", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));



    }

    private void UpdateRecruiterProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();



        if ((dataJson.get("email").getAsString() == null || dataJson.get("email").getAsString().isEmpty() ) || (dataJson.get("password").getAsString() == null || dataJson.get("password").getAsString().isEmpty()) || (dataJson.get("name").getAsString() == null || dataJson.get("name").getAsString().isEmpty()) || (dataJson.get("industry").getAsString() == null || dataJson.get("industry").getAsString().isEmpty()) || (dataJson.get("description").getAsString() == null || dataJson.get("description").getAsString().isEmpty())) {
            JsonObject responseJson = JsonUtils.createResponse("UPDATE_ACCOUNT_RECRUITER", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String token = requestJson.get("token").getAsString();
        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();
        String name = dataJson.get("name").getAsString();
        String industry = dataJson.get("industry").getAsString();
        String description = dataJson.get("description").getAsString();

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM recruiter WHERE Email = ?");
        st.setString(1, email);

        ResultSet rs;
        rs = st.executeQuery();

        if (rs.next()) {
            JsonObject Response = JsonUtils.createResponse("UPDATE_ACCOUNT_RECRUITER", "INVALID_EMAIL", "");

            out.println(JsonUtils.toJsonString(Response));
            logWriter("Server", JsonUtils.toJsonString(Response));
            return;
        }


        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        st = Conexao.getConexao().prepareStatement("UPDATE recruiter SET Email = ?, Nome = ?, senha = ?, industry = ?, description = ? WHERE Id = ?");
        st.setString(1, email);
        st.setString(2, name);
        st.setString(3, password);
        st.setString(4, industry);
        st.setString(5, description);
        st.setInt(6, recruiterId);
        st.executeUpdate();

        JsonObject responseJson = JsonUtils.createResponse("UPDATE_ACCOUNT_RECRUITER", "SUCCESS", "");
        out.println(JsonUtils.toJsonString(responseJson));
        logWriter("Server",JsonUtils.toJsonString(responseJson));



    }

    private void DeleteProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {

        String table = "";
        String op = "";

        switch (role){
            case "CANDIDATE":
                op = "DELETE_ACCOUNT_CANDIDATE";
                table = "candidate";
                break;
            case "RECRUITER":
                op = "DELETE_ACCOUNT_RECRUITER";
                table = "recruiter";
                break;
        }

        String token = requestJson.get("token").getAsString();

        int id = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("DELETE FROM "+table+" WHERE Id = ?");
        st.setInt(1, id);
        st.executeUpdate();



        JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", "");
        out.println(JsonUtils.toJsonString(responseJson));
        logWriter("Server",JsonUtils.toJsonString(responseJson));
    }

    private void LookUpProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {

        String table = "";
        String op = "";

        switch (role){
            case "CANDIDATE":
                op = "LOOKUP_ACCOUNT_CANDIDATE";
                table = "candidate";
                break;
            case "RECRUITER":
                op = "LOOKUP_ACCOUNT_RECRUITER";
                table = "recruiter";
                break;
        }

        String token = requestJson.get("token").getAsString();
        int id = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM "+table+" WHERE id = ?");
        st.setInt(1, id);

        ResultSet rs;
        rs = st.executeQuery();


        if (rs.next()) {
            String email = rs.getString("Email");
            String password = rs.getString("senha");
            String name = rs.getString("Nome");
            JsonObject data = new JsonObject();
            data.addProperty("email",email);
            data.addProperty("password",password);
            data.addProperty("name",name);

            if (role.equals("RECRUITER")){
                String industry = rs.getString("industry");
                String description = rs.getString("description");
                data.addProperty("industry",industry);
                data.addProperty("description",description);
            }

            JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", "");
            responseJson.add("data",data);

            logWriter("Server",JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));
            return;

        }



    }

    private void LogoutProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException {

        String op;

        if (role.equals("CANDIDATE")){
            op = "LOGOUT_CANDIDATE";
        }
        else{
            op = "LOGOUT_RECRUITER";
        }

        String token = requestJson.get("token").getAsString();

        JsonObject data = new JsonObject();
        JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", "");
        responseJson.add("data",data);

        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;
    }

    private void IncludeSkillProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        String token = requestJson.get("token").getAsString();
        String skill = dataJson.get("skill").getAsString();
        int experience = dataJson.get("experience").getAsInt();
        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();

        if (rs.next()) { //Verificação de Existência da Skill
            int skillId = Integer.parseInt(rs.getString("idSkill"));
            st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ? AND idCandidate = ?");
            st.setInt(1, skillId);
            st.setInt(2, candidateId);

            rs = st.executeQuery();

            if (rs.next()) { //Verificação de Cadastro Prévio da Skill
                JsonObject responseJson = JsonUtils.createResponse("INCLUDE_SKILL", "SKILL_EXISTS", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }else{
                st = Conexao.getConexao().prepareStatement("INSERT INTO skills (idCandidate,idSkillDataset,experiencia) VALUES (?, ?, ?)");
                st.setInt(1, candidateId);
                st.setInt(2, skillId);
                st.setInt(3, experience);
                st.executeUpdate();

                JsonObject responseJson = JsonUtils.createResponse("INCLUDE_SKILL", "SUCCESS", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;

            }

        }else{
            JsonObject responseJson = JsonUtils.createResponse("INCLUDE_SKILL", "SKILL_NOT_EXIST", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }



    }

    private void LookupSkillProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String skill = dataJson.get("skill").getAsString();

        String token = requestJson.get("token").getAsString();
        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();

        if (rs.next()) {
            int skillId = Integer.parseInt(rs.getString("idSkill"));
            st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ? AND idCandidate = ?");
            st.setInt(1, skillId);
            st.setInt(2, candidateId);

            rs = st.executeQuery();

            if (rs.next()) {
                JsonObject data = new JsonObject();
                data.addProperty("skill",skill);
                data.addProperty("experience",rs.getString("experiencia"));
                JsonObject responseJson = JsonUtils.createResponse("LOOKUP_SKILL", "SUCCESS", "");
                responseJson.add("data",data);

                logWriter("Server",JsonUtils.toJsonString(responseJson));
                out.println(JsonUtils.toJsonString(responseJson));
                return;

            }
        }
        JsonObject responseJson = JsonUtils.createResponse("LOOKUP_SKILL", "SKILL_NOT_FOUND", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void DeleteSkillProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String skill = dataJson.get("skill").getAsString();

        String token = requestJson.get("token").getAsString();
        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();

        if (rs.next()) {
            int skillId = Integer.parseInt(rs.getString("idSkill"));

            st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ? AND idCandidate = ?");
            st.setInt(1, skillId);
            st.setInt(2, candidateId);

            rs = st.executeQuery();

            if (rs.next()) {
                st = Conexao.getConexao().prepareStatement("DELETE FROM skills WHERE idSkillDataset = ? AND idCandidate = ?");
                st.setInt(1, skillId);
                st.setInt(2, candidateId);
                st.executeUpdate();

                JsonObject responseJson = JsonUtils.createResponse("DELETE_SKILL", "SUCCESS", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }

        }

        JsonObject responseJson = JsonUtils.createResponse("DELETE_SKILL", "SKILL_NOT_FOUND", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void UpdateSkillProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String skill = dataJson.get("skill").getAsString();
        String newSkill = dataJson.get("newSkill").getAsString();
        int experiencia = dataJson.get("experience").getAsInt();

        String token = requestJson.get("token").getAsString();
        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();


        if (rs.next()) { //Verificação de existencia da skill antiga inseria
            int skillId = Integer.parseInt(rs.getString("idSkill"));

            st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ? AND idCandidate = ?");
            st.setInt(1, skillId);
            st.setInt(2, candidateId);

            rs = st.executeQuery();

            if (rs.next()) { //Verificação do cadastro da skill inserida
                st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
                st.setString(1, newSkill);

                rs = st.executeQuery();

                if (rs.next()) { //Verificacao da existencia da skill nova
                    int newSkillId = Integer.parseInt(rs.getString("idSkill"));

                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ? AND idCandidate = ?");
                    st.setInt(1, newSkillId);
                    st.setInt(2, candidateId);

                    rs = st.executeQuery();

                    if (rs.next()) { //Verificacao de cadastro da skill nova

                        JsonObject responseJson = JsonUtils.createResponse("UPDATE_SKILL", "SKILL_EXISTS", "");
                        logWriter("Server",JsonUtils.toJsonString(responseJson));
                        out.println(JsonUtils.toJsonString(responseJson));
                        return;

                    }else {
                        st = Conexao.getConexao().prepareStatement("UPDATE skills SET  idSkillDataset = ?, experiencia = ? WHERE idCandidate = ? AND idSkillDataset = ?");
                        st.setInt(1, newSkillId);
                        st.setInt(2, experiencia);
                        st.setInt(3, candidateId);
                        st.setInt(4, skillId);
                        st.executeUpdate();

                        JsonObject responseJson = JsonUtils.createResponse("UPDATE_SKILL", "SUCCESS", "");
                        logWriter("Server",JsonUtils.toJsonString(responseJson));
                        out.println(JsonUtils.toJsonString(responseJson));

                        return;
                    }
                }
            }
        }
        JsonObject responseJson = JsonUtils.createResponse("UPDATE_SKILL", "SKILL_NOT_FOUND", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;
    }

    private void IncludeJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        String token = requestJson.get("token").getAsString();
        String skill = dataJson.get("skill").getAsString();
        int experience = dataJson.get("experience").getAsInt();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();

        if (rs.next()) { //Verificação de Existência da Skill
            int skillId = Integer.parseInt(rs.getString("idSkill"));
            st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? AND idRecruiter = ?");
            st.setInt(1, skillId);
            st.setInt(2, recruiterId);

            rs = st.executeQuery();

            if (rs.next()) { //Verificação de Cadastro Prévio da Skill
                JsonObject responseJson = JsonUtils.createResponse("INCLUDE_JOB", "JOB_EXISTS", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }else{
                st = Conexao.getConexao().prepareStatement("INSERT INTO JOBS (idRecruiter,idSkillDataset,experiencia) VALUES (?, ?, ?)");
                st.setInt(1, recruiterId);
                st.setInt(2, skillId);
                st.setInt(3, experience);
                st.executeUpdate();

                JsonObject responseJson = JsonUtils.createResponse("INCLUDE_JOB", "SUCCESS", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;

            }

        }else{
            JsonObject responseJson = JsonUtils.createResponse("INCLUDE_JOB", "SKILL_NOT_EXIST", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }



    }

    private void LookupJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String skill = dataJson.get("skill").getAsString();

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();

        if (rs.next()) {
            int skillId = Integer.parseInt(rs.getString("idSkill"));
            st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? AND idRecruiter = ?");
            st.setInt(1, skillId);
            st.setInt(2, recruiterId);

            rs = st.executeQuery();

            if (rs.next()) {
                JsonObject data = new JsonObject();
                data.addProperty("skill",skill);
                data.addProperty("experience",rs.getString("experiencia"));
                data.addProperty("id",rs.getString("idJob"));
                JsonObject responseJson = JsonUtils.createResponse("LOOKUP_JOB", "SUCCESS", "");
                responseJson.add("data",data);

                logWriter("Server",JsonUtils.toJsonString(responseJson));
                out.println(JsonUtils.toJsonString(responseJson));
                return;

            }
        }
        JsonObject responseJson = JsonUtils.createResponse("LOOKUP_JOB", "JOB_NOT_FOUND", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void DeleteJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        int id = dataJson.get("id").getAsInt();

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ?");
        st.setInt(1, id);

        rs = st.executeQuery();

        if (rs.next()) {
            int jobId = Integer.parseInt(rs.getString("idJob"));

                st = Conexao.getConexao().prepareStatement("DELETE FROM jobs WHERE idJob = ?");
                st.setInt(1, jobId);
                st.executeUpdate();

                JsonObject responseJson = JsonUtils.createResponse("DELETE_SKILL", "SUCCESS", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;

        }

        JsonObject responseJson = JsonUtils.createResponse("DELETE_JOB", "JOB_NOT_FOUND", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void UpdateJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        int id = dataJson.get("id").getAsInt();
        String skill = dataJson.get("skill").getAsString();
        String newSkill = dataJson.get("newSkill").getAsString();
        int experiencia = dataJson.get("experience").getAsInt();

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ?");
        st.setInt(1, id);
        rs = st.executeQuery();
        if (rs.next()) {

            st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
            st.setString(1, skill);

            rs = st.executeQuery();


            if (rs.next()) { //Verificação de existencia da skill antiga inseria
                int skillId = Integer.parseInt(rs.getString("idSkill"));

                st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? AND idRecruiter = ?");
                st.setInt(1, skillId);
                st.setInt(2, recruiterId);

                rs = st.executeQuery();

                if (rs.next()) { //Verificação do cadastro da skill inserida
                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
                    st.setString(1, newSkill);

                    rs = st.executeQuery();

                    if (rs.next()) { //Verificacao da existencia da skill nova
                        int newSkillId = Integer.parseInt(rs.getString("idSkill"));

                        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? AND idRecruiter = ?");
                        st.setInt(1, newSkillId);
                        st.setInt(2, recruiterId);

                        rs = st.executeQuery();

                        if (rs.next()) { //Verificacao de cadastro da skill nova

                            JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "JOB_EXISTS", "");
                            logWriter("Server", JsonUtils.toJsonString(responseJson));
                            out.println(JsonUtils.toJsonString(responseJson));
                            return;

                        } else {
                            st = Conexao.getConexao().prepareStatement("UPDATE jobs SET  idSkillDataset = ?, experiencia = ? WHERE idJob = ?");
                            st.setInt(1, newSkillId);
                            st.setInt(2, experiencia);
                            st.setInt(3, id);
                            st.executeUpdate();

                            JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "SUCCESS", "");
                            logWriter("Server", JsonUtils.toJsonString(responseJson));
                            out.println(JsonUtils.toJsonString(responseJson));

                            return;
                        }
                    }
                }
            }
            JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "SKILL_NOT_FOUND", "");
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));
            return;
        }
        JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "JOB_NOT_FOUND", "");
        logWriter("Server", JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;
    }

    private void logWriter(String menssager, String message) throws IOException {
        System.out.println(menssager + ": " + message);
    }


    public static void main(String[] args) {
        int serverPort = 21234;

        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("server_log.txt", true));
            ServerSocket serverSocket = new ServerSocket(serverPort);

            System.out.println("Servidor iniciado com sucesso, porta: " + serverPort);
            while(true){
                while (true) {
                    System.out.println("Esperando a conexão...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("O cliente foi conectado: " + clientSocket);

                    new Server(clientSocket, fileWriter);
                }
            }
        } catch (IOException e){
            System.err.println("Erro ao abrir o servidor: " + e.getMessage());
            System.exit(1);
        }
    }

}
