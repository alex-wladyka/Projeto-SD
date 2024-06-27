import java.io.*;
import java.net.*;

import Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Server extends Thread {

    private Socket clientSocket;
    private BufferedWriter fileWriter;

    private static List<String> usuariosLogados = new ArrayList<>();
    private static List<String> ipLogados = new ArrayList<>();

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
                try {
                    String jsonMessage = in.readLine();

                    if (jsonMessage == null || jsonMessage.equalsIgnoreCase("exit")) {
                        running = false;
                        continue;
                    }

                    logWriter("Client", jsonMessage);
                    JsonObject requestJson = JsonUtils.parseJson(jsonMessage);
                    String operation = requestJson.get("operation").getAsString();

                    if (requestJson.has("token")) {
                        if (requestJson.get("token").isJsonNull() || requestJson.get("token").getAsString().isEmpty()) {
                            JsonObject responseJson = JsonUtils.createResponse(operation, "INVALID_TOKEN", "");
                            logWriter("Server", JsonUtils.toJsonString(responseJson));
                            out.println(JsonUtils.toJsonString(responseJson));
                            continue;
                        }
                    }

                    handleOperation(operation, requestJson, out);
                } catch (IOException e) {
                    System.err.println("Erro na leitura da mensagem: " + e.getMessage());
                    running = false;
                } catch (SQLException e) {
                    System.err.println("Erro no banco de dados: " + e.getMessage());
                    running = false;
                } catch (Exception e) {
                    System.err.println("Erro inesperado: " + e.getMessage());
                    running = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        } finally {
            closeResources(in, out, clientSocket);

        }
    }

    private void handleOperation(String operation, JsonObject requestJson, PrintWriter out) throws SQLException, IOException {
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
                LookupSkillsetProcess(out, requestJson);
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
                LookupJobsetProcess(out, requestJson);
                break;
            case "DELETE_JOB":
                DeleteJobProcess(out, requestJson);
                break;
            case "UPDATE_JOB":
                UpdateJobProcess(out, requestJson);
                break;
            case "SEARCH_JOB":
                SearchJobProcess(out, requestJson);
                break;
            case "SET_JOB_AVAILABLE":
                UpdateJobAvaliabityProcess(out, requestJson);
                break;
            case "SET_JOB_SEARCHABLE":
                UpdateJobSearchableProcess(out, requestJson);
                break;
            case "SEARCH_CANDIDATE":
                SearchCandidateProcess(out, requestJson);
                break;
            case "CHOOSE_CANDIDATE":
                ChooseCandidateProcess(out, requestJson);
                break;
            case "GET_COMPANY":
                GetMessagesProcess(out, requestJson);
                break;
            default:
                JsonObject Response = JsonUtils.createResponse(operation, "INVALID_OPERATION", "");
                out.println(JsonUtils.toJsonString(Response));
                logWriter("Server", JsonUtils.toJsonString(Response));
        }
    }

    private void closeResources(BufferedReader in, PrintWriter out, Socket clientSocket) {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
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




                st = Conexao.getConexao().prepareStatement("SELECT email FROM candidate WHERE Email = ? UNION SELECT email FROM recruiter WHERE Email = ?");
                st.setString(1, email);
                st.setString(2, email);
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


                st = Conexao.getConexao().prepareStatement("SELECT email FROM candidate WHERE Email = ? UNION SELECT email FROM recruiter WHERE Email = ?");
                st.setString(1, email);
                st.setString(2, email);
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
            usuariosLogados.add(token);
            ipLogados.add(clientSocket.getInetAddress().toString());
            ListaLogin.updateLista(usuariosLogados,ipLogados);
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
        ResultSet rs;
        st = Conexao.getConexao().prepareStatement("SELECT email FROM candidate WHERE Email = ? UNION SELECT email FROM recruiter WHERE Email = ?");
        st.setString(1, email);
        st.setString(2, email);
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
        ResultSet rs;
        st = Conexao.getConexao().prepareStatement("SELECT email FROM candidate WHERE Email = ? UNION SELECT email FROM recruiter WHERE Email = ?");
        st.setString(1, email);
        st.setString(2, email);
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

    private void LogoutProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {

        String op;

        if (role.equals("CANDIDATE")){
            op = "LOGOUT_CANDIDATE";
        }
        else{
            op = "LOGOUT_RECRUITER";
        }

        String token = requestJson.get("token").getAsString();

        usuariosLogados.remove(token);
        ipLogados.remove(clientSocket.getInetAddress().toString());
        ListaLogin.updateLista(usuariosLogados, ipLogados);

        JsonObject data = new JsonObject();
        JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", "");
        responseJson.add("data",data);

        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;
    }

    private void IncludeSkillProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if ((dataJson.get("skill").getAsString() == null || dataJson.get("skill").getAsString().isEmpty() ) || (dataJson.get("experience").getAsString() == null || dataJson.get("experience").getAsString().isEmpty() || !dataJson.get("experience").getAsString().matches("[0-9]+"))){
            JsonObject responseJson = JsonUtils.createResponse("INCLUDE_SKILL", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

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

        if(dataJson.get("skill").getAsString() == null || dataJson.get("skill").getAsString().isEmpty()){
            JsonObject responseJson = JsonUtils.createResponse("LOOKUP_SKILL", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

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

        if(dataJson.get("skill").getAsString() == null || dataJson.get("skill").getAsString().isEmpty()){
            JsonObject responseJson = JsonUtils.createResponse("DELETE_SKILL", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

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

        if ((dataJson.get("skill").getAsString() == null || dataJson.get("skill").getAsString().isEmpty() ) || (dataJson.get("newSkill").getAsString() == null || dataJson.get("newSkill").getAsString().isEmpty() ) || (dataJson.get("experience").getAsString() == null || dataJson.get("experience").getAsString().isEmpty() || !dataJson.get("experience").getAsString().matches("[0-9]+"))){
            JsonObject responseJson = JsonUtils.createResponse("UPDATE_SKILL", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

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

    private void LookupSkillsetProcess(PrintWriter out, JsonObject requestJson) throws SQLException, IOException {
        String token = requestJson.get("token").getAsString();
        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);
        String nameSkill,experiencia;

        JsonObject data = new JsonObject();
        var skillsArray = new JsonArray();

        PreparedStatement st;
        ResultSet rs,rs2;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idCandidate = ?");
        st.setInt(1, candidateId);
        rs = st.executeQuery();




        while (rs.next()) {
            st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
            st.setInt(1, rs.getInt("idSkillDataset"));

            rs2 = st.executeQuery();
            if (rs2.next()) {
                nameSkill = rs2.getString("nameSkill");
            }
            else {
                nameSkill = "";
            }

            experiencia = rs.getString("experiencia");

            var skillObject = new JsonObject();
            skillObject.addProperty("skill", nameSkill);
            skillObject.addProperty("experience", experiencia);
            skillsArray.add(skillObject);

        }

        data.addProperty("skillset_size",skillsArray.size());
        data.add("skillset", skillsArray);

        JsonObject responseJson = JsonUtils.createResponse("LOOKUP_SKILLSET", "SUCCESS", "");
        responseJson.add("data",data);
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void SearchJobProcess(PrintWriter out, JsonObject requestJson) throws SQLException, IOException {


        String token = requestJson.get("token").getAsString(); //Pega o token do candidato

        String nameSkill = "",experiencia,idJob,available;

        var jobArray = new JsonArray(); //Cria a variável da lista de empregos
        JsonObject data = requestJson.get("data").getAsJsonObject();

        if(data.has("skill")){
            if(data.get("skill").getAsJsonArray().isEmpty()){
                JsonObject responseJson = JsonUtils.createResponse("SEARCH_JOB", "INVALID_FIELD", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }
        if(data.has("experience")){
            if((data.get("experience").getAsString() == null || data.get("experience").getAsString().isEmpty() || !data.get("experience").getAsString().matches("[0-9]+"))){
                JsonObject responseJson = JsonUtils.createResponse("SEARCH_JOB", "INVALID_FIELD", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }
        if(data.has("filter")){
            if((data.get("filter").getAsString() == null || data.get("filter").getAsString().isEmpty()) || !(data.get("filter").getAsString().equals("AND") || data.get("filter").getAsString().equals("OR"))){
                JsonObject responseJson = JsonUtils.createResponse("SEARCH_JOB", "INVALID_FIELD", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }

        PreparedStatement st;
        ResultSet rs,rs2;

        if(!data.has("skill")){ //Se o JSON não possuir o campo skill, então a busca é por experiência

            int experienciaSearch = data.get("experience").getAsInt();

            st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE experiencia <= ? and searchable = ?"); //faz um select no banco de dados para as vegas com experencia menor ou igual a selecionada
            st.setInt(1, experienciaSearch);
            st.setBoolean(2,true);
            rs = st.executeQuery();

            while (rs.next()) { // enquanto houver um resultado na busca feita
                st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                st.setInt(1, rs.getInt("idSkillDataset")); //Pegar o nome da experiencia

                rs2 = st.executeQuery();
                if (rs2.next()) {
                    nameSkill = rs2.getString("nameSkill");
                }

                experiencia = rs.getString("experiencia");
                idJob = rs.getString("idJob");
                available = rs.getString("available");

                var jobObject = new JsonObject();
                jobObject.addProperty("skill", nameSkill);
                jobObject.addProperty("experience", experiencia);
                jobObject.addProperty("id", idJob);
                jobObject.addProperty("available", available);
                jobArray.add(jobObject); // adicionar no array de vagas

            }
        }
        else if (!data.has("experience")) { // se o json conter o campo "skill mas" não conter o campo "experience" então a busca é por skills
            JsonArray skillArray = data.get("skill").getAsJsonArray(); //recebe o array de skills pesquisadas
            int [] idSkills = new int[skillArray.size()]; //vetor que guarda os ids das skills
            for(int i = 0 ; i<skillArray.size() ; i++){
                st = Conexao.getConexao().prepareStatement("SELECT idSkill FROM skilldataset WHERE nameSkill = ?");
                st.setString(1, skillArray.get(i).getAsString());


                rs = st.executeQuery();

                if (rs.next()) {
                    idSkills[i] = rs.getInt("idSkill");
                };

            }


            for (int i = 0 ; i<idSkills.length ; i++){ //for para pesquisar vagas na quantidade que foi pesquisada

                st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? and searchable = ?"); //faz um select no banco de dados para as vagas que tenham o id da vaga pesquiada
                st.setInt(1, idSkills[i]);
                st.setBoolean(2,true);
                rs = st.executeQuery();

                while (rs.next()) { //mesmo processo da busca por experiencia
                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                    st.setInt(1, rs.getInt("idSkillDataset"));

                    rs2 = st.executeQuery();
                    if (rs2.next()) {
                        nameSkill = rs2.getString("nameSkill");
                    }

                    experiencia = rs.getString("experiencia");
                    idJob = rs.getString("idJob");

                    var jobObject = new JsonObject();
                    jobObject.addProperty("skill", nameSkill);
                    jobObject.addProperty("experience", experiencia);
                    jobObject.addProperty("id", idJob);
                    jobArray.add(jobObject);

                }
            }
        }
        else { //se o json possuir os campos "skill" e "experience"
            if(data.get("filter").getAsString().equals("AND")){ //se o filtro for E

                JsonArray skillArray = data.get("skill").getAsJsonArray();
                int experienciaSearch = data.get("experience").getAsInt();
                int [] idSkills = new int[skillArray.size()];

                for(int i = 0 ; i<skillArray.size() ; i++){
                    st = Conexao.getConexao().prepareStatement("SELECT idSkill FROM skilldataset WHERE nameSkill = ?");
                    st.setString(1, skillArray.get(i).getAsString());

                    rs = st.executeQuery();

                    if (rs.next()) {
                        idSkills[i] = rs.getInt("idSkill");
                    };

                }


                for (int i = 0 ; i<idSkills.length ; i++){

                    st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? AND experiencia <= ? and searchable = ?"); //faz um select no banco de dados para vagas que tenham a skill pesquisada E experiencia menor ou igual a inserida
                    st.setInt(1, idSkills[i]);
                    st.setInt(2, experienciaSearch);
                    st.setBoolean(3,true);
                    rs = st.executeQuery();

                    while (rs.next()) { //mesmo processo de insersão no array igual as outras pesquisas
                        st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                        st.setInt(1, rs.getInt("idSkillDataset"));

                        rs2 = st.executeQuery();
                        if (rs2.next()) {
                            nameSkill = rs2.getString("nameSkill");
                        }

                        experiencia = rs.getString("experiencia");
                        idJob = rs.getString("idJob");

                        var jobObject = new JsonObject();
                        jobObject.addProperty("skill", nameSkill);
                        jobObject.addProperty("experience", experiencia);
                        jobObject.addProperty("id", idJob);
                        jobArray.add(jobObject);

                    }
                }
            }
            else if(data.get("filter").getAsString().equals("OR")){ // se o filtro for OU

                JsonArray skillArray = data.get("skill").getAsJsonArray();
                int experienciaSearch = data.get("experience").getAsInt();
                int [] idSkills = new int[skillArray.size()];
                for(int i = 0 ; i<skillArray.size() ; i++){
                    st = Conexao.getConexao().prepareStatement("SELECT idSkill FROM skilldataset WHERE nameSkill = ?");
                    st.setString(1, skillArray.get(i).getAsString());

                    rs = st.executeQuery();

                    if (rs.next()) {
                        idSkills[i] = rs.getInt("idSkill");
                    };

                }


                for (int i = 0 ; i<idSkills.length ; i++){ //a pesquisa aqui é separada, pois como se trata de um OU incluir os dois no mesmo select poderia incluir vagas duplicadas quando fosse pesquisada duas skills

                    st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idSkillDataset = ? and searchable = ?");
                    st.setInt(1, idSkills[i]);
                    st.setBoolean(2,true);
                    rs = st.executeQuery();

                    while (rs.next()) {
                        st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                        st.setInt(1, rs.getInt("idSkillDataset"));

                        rs2 = st.executeQuery();
                        if (rs2.next()) {
                            nameSkill = rs2.getString("nameSkill");
                        }

                        experiencia = rs.getString("experiencia");
                        idJob = rs.getString("idJob");

                        var jobObject = new JsonObject();
                        jobObject.addProperty("skill", nameSkill);
                        jobObject.addProperty("experience", experiencia);
                        jobObject.addProperty("id", idJob);
                        jobArray.add(jobObject);

                    }
                }

                boolean isPresent = false;

                st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE experiencia <= ? and searchable = ?");
                st.setInt(1, experienciaSearch);
                st.setBoolean(2,true);

                rs = st.executeQuery();

                while (rs.next()) {
                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                    st.setInt(1, rs.getInt("idSkillDataset"));

                    rs2 = st.executeQuery();
                    if (rs2.next()) {
                        nameSkill = rs2.getString("nameSkill");
                    }

                    experiencia = rs.getString("experiencia");
                    idJob = rs.getString("idJob");


                    for (int i = 0; i < jobArray.size(); i++) {
                        JsonObject jsonObject = jobArray.get(i).getAsJsonObject();
                        if (jsonObject.get("id").getAsString().equals(idJob)) {
                            isPresent = true;
                            break;
                        }
                        isPresent = false;
                    }

                    if(!isPresent) {
                        var jobObject = new JsonObject();
                        jobObject.addProperty("skill", nameSkill);
                        jobObject.addProperty("experience", experiencia);
                        jobObject.addProperty("id", idJob);
                        jobArray.add(jobObject);
                    }

                }


            }
        }

        JsonObject dataArray = new JsonObject();

        dataArray.addProperty("jobset_size", jobArray.size());
        dataArray.add("jobset", jobArray);

        JsonObject responseJson = JsonUtils.createResponse("SEARCH_JOB", "SUCCESS", "");
        responseJson.add("data",dataArray);
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
    }

    private void IncludeJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if ((dataJson.get("skill").getAsString() == null || dataJson.get("skill").getAsString().isEmpty() ) || (dataJson.get("experience").getAsString() == null || dataJson.get("experience").getAsString().isEmpty() || !dataJson.get("experience").getAsString().matches("[0-9]+")) || !(dataJson.get("searchable").getAsString().equals("YES") || dataJson.get("searchable").getAsString().equals("NO")) || !(dataJson.get("available").getAsString().equals("YES") || dataJson.get("available").getAsString().equals("NO"))){
            JsonObject responseJson = JsonUtils.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String token = requestJson.get("token").getAsString();
        String skill = dataJson.get("skill").getAsString();
        int experience = dataJson.get("experience").getAsInt();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);
        boolean available = dataJson.get("available").getAsString().equals("YES");
        boolean searchable = dataJson.get("searchable").getAsString().equals("YES");
        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
        st.setString(1, skill);

        rs = st.executeQuery();

        if (rs.next()) { //Verificação de Existência da Skill
            int skillId = Integer.parseInt(rs.getString("idSkill"));

            st = Conexao.getConexao().prepareStatement("INSERT INTO JOBS (idRecruiter,idSkillDataset,experiencia,searchable,available) VALUES (?, ?, ?, ?, ?)");
            st.setInt(1, recruiterId);
            st.setInt(2, skillId);
            st.setInt(3, experience);
            st.setBoolean(4,searchable);
            st.setBoolean(5,available);
            st.executeUpdate();

            JsonObject responseJson = JsonUtils.createResponse("INCLUDE_JOB", "SUCCESS", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            return;

        }else{
            JsonObject responseJson = JsonUtils.createResponse("INCLUDE_JOB", "SKILL_NOT_EXIST", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }



    }

    private void LookupJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if (dataJson.get("id").getAsString() == null || dataJson.get("id").getAsString().isEmpty() || !dataJson.get("id").getAsString().matches("[0-9]+")) {
            JsonObject responseJson = JsonUtils.createResponse("LOOKUP_JOB", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        int id = dataJson.get("id").getAsInt();

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        String available,searchable;

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ? AND idRecruiter = ?");
        st.setInt(1, id);
        st.setInt(2, recruiterId);

        rs = st.executeQuery();



        if (rs.next()) {
                int idSkill = rs.getInt("idSkillDataset");
                String experience = rs.getString("experiencia");

                st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                st.setInt(1,idSkill);
                ResultSet rs2 = st.executeQuery();
                String nameSkill = null;
                if (rs2.next()) {
                    nameSkill = rs2.getString("nameSkill");
                }

            if(rs.getString("available").equals("0")){
                available = "NO";
            }
            else {
                available = "YES";
            }

            if(rs.getString("searchable").equals("0")){
                searchable = "NO";
            }
            else {
                searchable = "YES";
            }

                JsonObject data = new JsonObject();
                data.addProperty("skill",nameSkill);
                data.addProperty("experience",experience);
                data.addProperty("id",rs.getString("idJob"));
                data.addProperty("available",available);
                data.addProperty("searchable",searchable);
                JsonObject responseJson = JsonUtils.createResponse("LOOKUP_JOB", "SUCCESS", "");
                responseJson.add("data",data);

                logWriter("Server",JsonUtils.toJsonString(responseJson));
                out.println(JsonUtils.toJsonString(responseJson));
                return;


        }
        JsonObject responseJson = JsonUtils.createResponse("LOOKUP_JOB", "JOB_NOT_FOUND", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void DeleteJobProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if (dataJson.get("id").getAsString() == null || dataJson.get("id").getAsString().isEmpty() || !dataJson.get("id").getAsString().matches("[0-9]+")) {
            JsonObject responseJson = JsonUtils.createResponse("DELETE_JOB", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        int id = dataJson.get("id").getAsInt();

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ? AND idRecruiter = ?");
        st.setInt(1, id);
        st.setInt(2, recruiterId);

        rs = st.executeQuery();

        if (rs.next()) {
            int jobId = Integer.parseInt(rs.getString("idJob"));

                st = Conexao.getConexao().prepareStatement("DELETE FROM jobs WHERE idJob = ?");
                st.setInt(1, jobId);
                st.executeUpdate();

                JsonObject responseJson = JsonUtils.createResponse("DELETE_JOB", "SUCCESS", "");
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

        if (dataJson.get("id").getAsString() == null || dataJson.get("id").getAsString().isEmpty() || !dataJson.get("id").getAsString().matches("[0-9]+") || (dataJson.get("skill").getAsString() == null || dataJson.get("skill").getAsString().isEmpty()) || (dataJson.get("experience").getAsString() == null || dataJson.get("experience").getAsString().isEmpty() || !dataJson.get("experience").getAsString().matches("[0-9]+"))) {
            JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        int id = dataJson.get("id").getAsInt();
        String skill = dataJson.get("skill").getAsString();
        int experiencia = dataJson.get("experience").getAsInt();

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ? AND idRecruiter = ?");
        st.setInt(1, id);
        st.setInt(2, recruiterId);
        rs = st.executeQuery();
        if (rs.next()) {

            st = Conexao.getConexao().prepareStatement("SELECT * FROM skillDataset WHERE nameSkill = ?");
            st.setString(1, skill);

            rs = st.executeQuery();

            if (rs.next()) { //Verificação de existencia da skill antiga inseria
                int skillId = Integer.parseInt(rs.getString("idSkill"));

                st = Conexao.getConexao().prepareStatement("UPDATE jobs SET  idSkillDataset = ?, experiencia = ? WHERE idJob = ?");
                st.setInt(1, skillId);
                st.setInt(2, experiencia);
                st.setInt(3, id);
                st.executeUpdate();

                JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "SUCCESS", "");
                logWriter("Server", JsonUtils.toJsonString(responseJson));
                out.println(JsonUtils.toJsonString(responseJson));

                return;
            }else {
                JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "SKILL_NOT_FOUND", "");
                logWriter("Server", JsonUtils.toJsonString(responseJson));
                out.println(JsonUtils.toJsonString(responseJson));
                return;
            }
        }else {
            JsonObject responseJson = JsonUtils.createResponse("UPDATE_JOB", "JOB_NOT_FOUND", "");
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));
            return;
        }
    }

    private void LookupJobsetProcess(PrintWriter out, JsonObject requestJson) throws SQLException, IOException {
        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);
        String nameSkill,experiencia,idJob,available,searchable;

        JsonObject data = new JsonObject();
        var jobArray = new JsonArray();

        PreparedStatement st;
        ResultSet rs,rs2;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idRecruiter = ?");
        st.setInt(1, recruiterId);
        rs = st.executeQuery();

        while (rs.next()) {
            st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
            st.setInt(1, rs.getInt("idSkillDataset"));

            rs2 = st.executeQuery();
            if (rs2.next()) {
                nameSkill = rs2.getString("nameSkill");
            }
            else {
                nameSkill = "";
            }

            experiencia = rs.getString("experiencia");
            idJob = rs.getString("idJob");

            if(rs.getString("available").equals("0")){
                available = "NO";
            }
            else {
                available = "YES";
            }

            if(rs.getString("searchable").equals("0")){
                searchable = "NO";
            }
            else {
                searchable = "YES";
            }


            var jobObject = new JsonObject();
            jobObject.addProperty("skill", nameSkill);
            jobObject.addProperty("experience", experiencia);
            jobObject.addProperty("id", idJob);
            jobObject.addProperty("available", available);
            jobObject.addProperty("searchable", searchable);
            jobArray.add(jobObject);

        }

        data.addProperty("jobset_size", jobArray.size());
        data.add("jobset", jobArray);

        JsonObject responseJson = JsonUtils.createResponse("LOOKUP_JOBSET", "SUCCESS", "");
        responseJson.add("data",data);
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
        return;

    }

    private void UpdateJobAvaliabityProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if((dataJson.get("id").getAsString().isEmpty() || dataJson.get("id").getAsString() == null || !dataJson.get("id").getAsString().matches("[0-9]+")) || !(dataJson.get("available").getAsString().equals("YES") || dataJson.get("available").getAsString().equals("NO") )){
            JsonObject responseJson = JsonUtils.createResponse("SET_JOB_AVAILABLE", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);
        int jobId = dataJson.get("id").getAsInt();

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ? AND idRecruiter = ?");
        st.setInt(1, jobId);
        st.setInt(2, recruiterId);
        rs = st.executeQuery();

        if (rs.next()) {

            boolean available = dataJson.get("available").getAsString().equals("YES");

            st = Conexao.getConexao().prepareStatement("UPDATE jobs SET available = ? WHERE idJob = ?");
            st.setBoolean(1, available);
            st.setInt(2, jobId);
            st.executeUpdate();

            JsonObject responseJson = JsonUtils.createResponse("SET_JOB_AVAILABLE", "SUCCESS", "");
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));

            return;
        }
        else {
            JsonObject responseJson = JsonUtils.createResponse("SET_JOB_AVAILABLE", "JOB_NOT_FOUND", "");
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));
            return;
        }


    }

    private void UpdateJobSearchableProcess(PrintWriter out, JsonObject requestJson) throws IOException, SQLException{
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if((dataJson.get("id").getAsString().isEmpty() || dataJson.get("id").getAsString() == null || !dataJson.get("id").getAsString().matches("[0-9]+")) || !(dataJson.get("searchable").getAsString().equals("YES") || dataJson.get("searchable").getAsString().equals("NO"))){
            JsonObject responseJson = JsonUtils.createResponse("SET_JOB_SEARCHABLE", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String token = requestJson.get("token").getAsString();
        int recruiterId = JsonUtils.JWTValidator.getIdClaim(token);
        int jobId = dataJson.get("id").getAsInt();

        PreparedStatement st;
        ResultSet rs;

        st = Conexao.getConexao().prepareStatement("SELECT * FROM jobs WHERE idJob = ? AND idRecruiter = ?");
        st.setInt(1, jobId);
        st.setInt(2, recruiterId);
        rs = st.executeQuery();

        if (rs.next()) {


            boolean searchable = dataJson.get("searchable").getAsString().equals("YES");

            st = Conexao.getConexao().prepareStatement("UPDATE jobs SET searchable = ? WHERE idJob = ?");
            st.setBoolean(1, searchable);
            st.setInt(2, jobId);
            st.executeUpdate();

            JsonObject responseJson = JsonUtils.createResponse("SET_JOB_SEARCHABLE", "SUCCESS", "");
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));

        }
        else {
            JsonObject responseJson = JsonUtils.createResponse("SET_JOB_SEARCHABLE", "JOB_NOT_FOUND", "");
            logWriter("Server", JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));
        }
        return;


    }

    private void SearchCandidateProcess(PrintWriter out, JsonObject requestJson) throws SQLException, IOException {


        String token = requestJson.get("token").getAsString(); //Pega o token do recruiter

        String nameSkill = "",experiencia,idCandidate,nameCandidate;

        var candidateArray = new JsonArray(); //Cria a variável da lista de candidatos

        JsonObject data = requestJson.get("data").getAsJsonObject();

        if(data.has("skill")){
            if(data.get("skill").getAsJsonArray().isEmpty()){
                JsonObject responseJson = JsonUtils.createResponse("SEARCH_CANDIDATE", "INVALID_FIELD", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }
        if(data.has("experience")){
            if((data.get("experience").getAsString() == null || data.get("experience").getAsString().isEmpty() || !data.get("experience").getAsString().matches("[0-9]+"))){
                JsonObject responseJson = JsonUtils.createResponse("SEARCH_CANDIDATE", "INVALID_FIELD", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }
        if(data.has("filter")){
            if((data.get("filter").getAsString() == null || data.get("filter").getAsString().isEmpty()) || !(data.get("filter").getAsString().equals("AND") || data.get("filter").getAsString().equals("OR"))){
                JsonObject responseJson = JsonUtils.createResponse("SEARCH_CANDIDATE", "INVALID_FIELD", "");
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }

        PreparedStatement st;
        ResultSet rs,rs2;

        if(!data.has("skill")){ //Se o JSON não possuir o campo skill, então a busca é por experiência

            int experienciaSearch = data.get("experience").getAsInt();

            st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE experiencia <= ?"); //faz um select no banco de dados para os candidatos com experencia menor ou igual a selecionada
            st.setInt(1, experienciaSearch);
            rs = st.executeQuery();

            while (rs.next()) { // enquanto houver um resultado na busca feita

                st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                st.setInt(1, rs.getInt("idSkillDataset")); //Pegar o nome da skill
                rs2 = st.executeQuery();
                rs2.next();
                nameSkill = rs2.getString("nameSkill");

                experiencia = rs.getString("experiencia");
                idCandidate = rs.getString("idCandidate");

                st = Conexao.getConexao().prepareStatement("SELECT Nome FROM candidate WHERE id = ?");
                st.setInt(1, Integer.parseInt(idCandidate));
                rs2 = st.executeQuery();
                rs2.next();
                nameCandidate = rs2.getString("Nome");

                var jobObject = new JsonObject();
                jobObject.addProperty("skill", nameSkill);
                jobObject.addProperty("experience", experiencia);
                jobObject.addProperty("id", rs.getInt("idSkillDataset"));
                jobObject.addProperty("id_user", idCandidate);
                jobObject.addProperty("name", nameCandidate);
                candidateArray.add(jobObject); // adicionar no array de candidatos

            }
        }
        else if (!data.has("experience")) { // se o json conter o campo "skill mas" não conter o campo "experience" então a busca é por skills
            JsonArray skillArray = data.get("skill").getAsJsonArray(); //recebe o array de skills pesquisadas
            int [] idSkills = new int[skillArray.size()]; //vetor que guarda os ids das skills
            for(int i = 0 ; i<skillArray.size() ; i++){
                st = Conexao.getConexao().prepareStatement("SELECT idSkill FROM skilldataset WHERE nameSkill = ?");
                st.setString(1, skillArray.get(i).getAsString());


                rs = st.executeQuery();

                if (rs.next()) {
                    idSkills[i] = rs.getInt("idSkill");
                };

            }


            for (int i = 0 ; i<idSkills.length ; i++){ //for para pesquisar vagas na quantidade que foi pesquisada

                st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ?"); //faz um select no banco de dados para as vagas que tenham o id da vaga pesquiada
                st.setInt(1, idSkills[i]);
                rs = st.executeQuery();

                while (rs.next()) { //mesmo processo da busca por experiencia
                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                    st.setInt(1, rs.getInt("idSkillDataset"));

                    rs2 = st.executeQuery();
                    if (rs2.next()) {
                        nameSkill = rs2.getString("nameSkill");
                    }

                    experiencia = rs.getString("experiencia");
                    idCandidate = rs.getString("idCandidate");

                    st = Conexao.getConexao().prepareStatement("SELECT Nome FROM candidate WHERE id = ?");
                    st.setInt(1, Integer.parseInt(idCandidate));
                    rs2 = st.executeQuery();
                    rs2.next();
                    nameCandidate = rs2.getString("Nome");

                    var jobObject = new JsonObject();
                    jobObject.addProperty("skill", nameSkill);
                    jobObject.addProperty("experience", experiencia);
                    jobObject.addProperty("id", rs.getInt("idSkillDataset"));
                    jobObject.addProperty("id_user", idCandidate);
                    jobObject.addProperty("name", nameCandidate);
                    candidateArray.add(jobObject);

                }
            }
        }
        else { //se o json possuir os campos "skill" e "experience"
            if(data.get("filter").getAsString().equals("AND")){ //se o filtro for E

                JsonArray skillArray = data.get("skill").getAsJsonArray();
                int experienciaSearch = data.get("experience").getAsInt();
                int [] idSkills = new int[skillArray.size()];

                for(int i = 0 ; i<skillArray.size() ; i++){
                    st = Conexao.getConexao().prepareStatement("SELECT idSkill FROM skilldataset WHERE nameSkill = ?");
                    st.setString(1, skillArray.get(i).getAsString());

                    rs = st.executeQuery();

                    if (rs.next()) {
                        idSkills[i] = rs.getInt("idSkill");
                    };

                }


                for (int i = 0 ; i<idSkills.length ; i++){

                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ? AND experiencia <= ?"); //faz um select no banco de dados para vagas que tenham a skill pesquisada E experiencia menor ou igual a inserida
                    st.setInt(1, idSkills[i]);
                    st.setInt(2, experienciaSearch);
                    rs = st.executeQuery();

                    while (rs.next()) { //mesmo processo de insersão no array igual as outras pesquisas
                        st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                        st.setInt(1, rs.getInt("idSkillDataset"));

                        rs2 = st.executeQuery();
                        if (rs2.next()) {
                            nameSkill = rs2.getString("nameSkill");
                        }

                        experiencia = rs.getString("experiencia");
                        idCandidate = rs.getString("idCandidate");

                        st = Conexao.getConexao().prepareStatement("SELECT Nome FROM candidate WHERE id = ?");
                        st.setInt(1, Integer.parseInt(idCandidate));
                        rs2 = st.executeQuery();
                        rs2.next();
                        nameCandidate = rs2.getString("Nome");

                        var jobObject = new JsonObject();
                        jobObject.addProperty("skill", nameSkill);
                        jobObject.addProperty("experience", experiencia);
                        jobObject.addProperty("id", rs.getInt("idSkillDataset"));
                        jobObject.addProperty("id_user", idCandidate);
                        jobObject.addProperty("name", nameCandidate);
                        candidateArray.add(jobObject);

                    }
                }
            }
            else if(data.get("filter").getAsString().equals("OR")){ // se o filtro for OU

                JsonArray skillArray = data.get("skill").getAsJsonArray();
                int experienciaSearch = data.get("experience").getAsInt();
                int [] idSkills = new int[skillArray.size()];
                for(int i = 0 ; i<skillArray.size() ; i++){
                    st = Conexao.getConexao().prepareStatement("SELECT idSkill FROM skilldataset WHERE nameSkill = ?");
                    st.setString(1, skillArray.get(i).getAsString());

                    rs = st.executeQuery();

                    if (rs.next()) {
                        idSkills[i] = rs.getInt("idSkill");
                    };

                }


                for (int i = 0 ; i<idSkills.length ; i++){ //a pesquisa aqui é separada, pois como se trata de um OU incluir os dois no mesmo select poderia incluir vagas duplicadas quando fosse pesquisada duas skills

                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE idSkillDataset = ?");
                    st.setInt(1, idSkills[i]);
                    rs = st.executeQuery();

                    while (rs.next()) {
                        st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                        st.setInt(1, rs.getInt("idSkillDataset"));

                        rs2 = st.executeQuery();
                        if (rs2.next()) {
                            nameSkill = rs2.getString("nameSkill");
                        }

                        experiencia = rs.getString("experiencia");
                        idCandidate = rs.getString("idCandidate");

                        st = Conexao.getConexao().prepareStatement("SELECT Nome FROM candidate WHERE id = ?");
                        st.setInt(1, Integer.parseInt(idCandidate));
                        rs2 = st.executeQuery();
                        rs2.next();
                        nameCandidate = rs2.getString("Nome");

                        var jobObject = new JsonObject();
                        jobObject.addProperty("skill", nameSkill);
                        jobObject.addProperty("experience", experiencia);
                        jobObject.addProperty("id", rs.getInt("idSkillDataset"));
                        jobObject.addProperty("id_user", idCandidate);
                        jobObject.addProperty("name", nameCandidate);
                        candidateArray.add(jobObject);

                    }
                }

                boolean isPresent = false;

                st = Conexao.getConexao().prepareStatement("SELECT * FROM skills WHERE experiencia <= ?");
                st.setInt(1, experienciaSearch);

                rs = st.executeQuery();

                while (rs.next()) {
                    st = Conexao.getConexao().prepareStatement("SELECT * FROM skilldataset WHERE idSkill = ?");
                    st.setInt(1, rs.getInt("idSkillDataset"));

                    rs2 = st.executeQuery();
                    if (rs2.next()) {
                        nameSkill = rs2.getString("nameSkill");
                    }

                    experiencia = rs.getString("experiencia");
                    idCandidate = rs.getString("idCandidate");

                    st = Conexao.getConexao().prepareStatement("SELECT Nome FROM candidate WHERE id = ?");
                    st.setInt(1, Integer.parseInt(idCandidate));
                    rs2 = st.executeQuery();
                    rs2.next();
                    nameCandidate = rs2.getString("Nome");


                    for (int i = 0; i < candidateArray.size(); i++) {
                        JsonObject jsonObject = candidateArray.get(i).getAsJsonObject();
                        if (jsonObject.get("id_user").getAsString().equals(idCandidate) && jsonObject.get("id").getAsString().equals(rs.getString("idSkillDataset"))) {
                            isPresent = true;
                            break;
                        }
                        isPresent = false;
                    }

                    if(!isPresent) {
                        var jobObject = new JsonObject();
                        jobObject.addProperty("skill", nameSkill);
                        jobObject.addProperty("experience", experiencia);
                        jobObject.addProperty("id", rs.getInt("idSkillDataset"));
                        jobObject.addProperty("id_user", idCandidate);
                        jobObject.addProperty("name", nameCandidate);
                        candidateArray.add(jobObject);
                    }

                }


            }
        }

        JsonObject dataArrray = new JsonObject();

        dataArrray.addProperty("profile_size", candidateArray.size());
        dataArrray.add("profile", candidateArray);

        JsonObject responseJson = JsonUtils.createResponse("SEARCH_CANDIDATE", "SUCCESS", "");
        responseJson.add("data",dataArrray);
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
    }

    private void ChooseCandidateProcess(PrintWriter out, JsonObject requestJson) throws SQLException, IOException {

        String token = requestJson.get("token").getAsString();
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if (dataJson.get("id_user").getAsString() == null || dataJson.get("id_user").getAsString().isEmpty() || !dataJson.get("id_user").getAsString().matches("[0-9]+")) {
            JsonObject responseJson = JsonUtils.createResponse("CHOOSE_CANDIDATE", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        JsonObject responseJson;
        int idRecruiter = JsonUtils.JWTValidator.getIdClaim(token);
        int idCandidate = dataJson.get("id_user").getAsInt();

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM candidate WHERE id = ?");
        st.setInt(1, idCandidate);

        ResultSet rs;
        rs = st.executeQuery();

        if (rs.next()) {
            st = Conexao.getConexao().prepareStatement("INSERT INTO messages (idRecruiter, idCandidate) VALUES (?, ?)");
            st.setInt(1,idRecruiter);
            st.setInt(2,idCandidate);
            st.executeUpdate();
            responseJson = JsonUtils.createResponse("CHOOSE_CANDIDATE", "SUCCESS", "");
        }
        else {
            responseJson = JsonUtils.createResponse("CHOOSE_CANDIDATE", "CANDIDATE_NOT_FOUND", "");
        }

        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));

    }

    private void GetMessagesProcess(PrintWriter out, JsonObject requestJson) throws SQLException, IOException {
        String token = requestJson.get("token").getAsString();
        int idCandidate = JsonUtils.JWTValidator.getIdClaim(token);

        String nameRecruiter,industry,email,description;


        var companyArray = new JsonArray();

        PreparedStatement st;
        ResultSet rs,rs2;

        st = Conexao.getConexao().prepareStatement("SELECT idRecruiter FROM messages WHERE idCandidate = ?");
        st.setInt(1, idCandidate);
        rs = st.executeQuery();

        while (rs.next()) {
            int idRecruiter = rs.getInt("idRecruiter");

            st = Conexao.getConexao().prepareStatement("SELECT * FROM recruiter WHERE id = ?");
            st.setInt(1,idRecruiter);

            rs2 = st.executeQuery();

            rs2.next();

            nameRecruiter = rs2.getString("nome");
            industry = rs2.getString("industry");
            email = rs2.getString("email");
            description = rs2.getString("description");

            /*if (rs.next()){
                rs.
            }*/

            var companyObject = new JsonObject();
            companyObject.addProperty("name", nameRecruiter);
            companyObject.addProperty("industry", industry);
            companyObject.addProperty("email", email);
            companyObject.addProperty("description", description);
            companyArray.add(companyObject);

        }

        JsonObject data = new JsonObject();
        data.addProperty("company_size", companyArray.size());
        data.add("company", companyArray);

        JsonObject responseJson = JsonUtils.createResponse("GET_COMPANY", "SUCCESS", "");
        responseJson.add("data",data);
        logWriter("Server",JsonUtils.toJsonString(responseJson));
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
            ListaLogin frame = new ListaLogin();
            while(true) {
                try {
                    System.out.println("Esperando a conexão...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("O cliente foi conectado: " + clientSocket);
                    new Server(clientSocket, fileWriter);
                } catch (IOException e) {
                    System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        } catch (IOException e){
            System.err.println("Erro ao abrir o servidor: " + e.getMessage());
            System.exit(1);
        }
    }

}
