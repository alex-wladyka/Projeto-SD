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
                            UpdateProcess(out, requestJson, "CANDIDATE");
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
                            //LoginProcess(requestJson, out, "RECRUITER");
                            break;
                        case "SIGNUP_RECRUITER":
                            SignUpProcess(out, requestJson, "RECRUITER");
                            break;
                        case "UPDATE_ACCOUNT_RECRUITER":
                            //UpdateProcess(out, requestJson, "RECRUITER");
                            break;
                        case "DELETE_ACCOUNT_RECRUITER":
                            //DeleteProcess(out, requestJson, "RECRUITER");
                            break;
                        case "LOOKUP_ACCOUNT_RECRUITER":
                            //LookUpProcess(out, requestJson, "RECRUITER");
                            break;
                        case "LOGOUT_RECRUITER":
                            LogoutProcess(out, requestJson, "RECRUITER");
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
        //email não está casdastrado
        /*Candidate newUser = new Candidate(String.valueOf(id),email, password, name);
        candidates.add(newUser);
        writeDatabase(candidates);*/



        JsonObject responseJson = JsonUtils.createResponse(op, "SUCCESS", "");
        out.println(JsonUtils.toJsonString(responseJson));
        logWriter("Server",JsonUtils.toJsonString(responseJson));
    }

    private void LoginProcess(JsonObject requestJson, PrintWriter out, String role) throws IOException, SQLException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();

        if ((dataJson.get("email").getAsString() == null || dataJson.get("email").getAsString().isEmpty() ) || (dataJson.get("password").getAsString() == null || dataJson.get("password").getAsString().isEmpty())) {
            JsonObject responseJson = JsonUtils.createResponse("LOGIN_CANDIDATE", "INVALID_FIELD", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;
        }

        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();

        /*List<Candidate> candidates = readDatabase();
        for (Candidate candidate : candidates) {
            if (candidate.getEmail().equals(email) && candidate.getPassword().equals(password)) {
                String token = JsonUtils.JWTValidator.generateToken(Integer.parseInt(candidate.getId()), "CANDIDATE");
                JsonObject responseJson = JsonUtils.createResponse("LOGIN_CANDIDATE", "SUCCESS", token);
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }*/

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM candidate WHERE Email = ? AND senha = ?");
        st.setString(1, email);
        st.setString(2, password);

        ResultSet rs;
        rs = st.executeQuery();


        if (rs.next()) {
            int id = Integer.parseInt(rs.getString("id"));
            String token = JsonUtils.JWTValidator.generateToken(id, "CANDIDATE");
            JsonObject responseJson = JsonUtils.createResponse("LOGIN_CANDIDATE", "SUCCESS", token);
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));
            return;

        }

        JsonObject responseJson = JsonUtils.createResponse("LOGIN_CANDIDATE", "INVALID_LOGIN", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
    }

    private void UpdateProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {
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

        /*List<Candidate> candidates = readDatabase();

        for (Candidate candidate : candidates) {   //teste para ver se o email já foi cadastrado
            if (candidate.getEmail().equals(email)) { //encontrou um email igual
                JsonObject invalidResponse = JsonUtils.createResponse("UPDATE_ACCOUNT_CANDIDATE", "INVALID_EMAIL", "");
                logWriter("Server",JsonUtils.toJsonString(invalidResponse));
                out.println(invalidResponse);
                return;
            }
        }*/

        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);

        /*Optional<Candidate> optionalCandidate = candidates.stream().filter(candidate -> candidate.getId().equals(String.valueOf(candidateId))).findFirst();

        if (optionalCandidate.isPresent()) {
            Candidate candidate = optionalCandidate.get();

            candidate.setEmail(email);
            candidate.setPassword(password);
            candidate.setName(name);

            writeDatabase(candidates);
        }*/

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

    private void DeleteProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {
        String token = requestJson.get("token").getAsString();

        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);
        /*List<Candidate> candidates = readDatabase();

        Optional<Candidate> optionalCandidate = candidates.stream().filter(candidate -> candidate.getId().equals(String.valueOf(candidateId))).findFirst();


        if (optionalCandidate.isPresent()) {
            candidates.remove(optionalCandidate.get());
            writeDatabase(candidates);

            JsonObject responseJson = JsonUtils.createResponse("DELETE_ACCOUNT_CANDIDATE", "SUCCESS", "");
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));

            return;

        }*/

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("DELETE FROM candidate WHERE Id = ?");
        st.setInt(1, candidateId);
        st.executeUpdate();

        JsonObject responseJson = JsonUtils.createResponse("DELETE_ACCOUNT_CANDIDATE", "SUCCESS", "");
        out.println(JsonUtils.toJsonString(responseJson));
        logWriter("Server",JsonUtils.toJsonString(responseJson));
    }

    private void LookUpProcess(PrintWriter out, JsonObject requestJson, String role) throws IOException, SQLException {
        String token = requestJson.get("token").getAsString();
        int candidateId = JsonUtils.JWTValidator.getIdClaim(token);

        /*List<Candidate> candidates = readDatabase();

        Optional<Candidate> optionalCandidate = candidates.stream().filter(candidate -> candidate.getId().equals(String.valueOf(candidateId))).findFirst();

        if (optionalCandidate.isPresent()) {
            String email = optionalCandidate.get().getEmail();
            String password = optionalCandidate.get().getPassword();
            String name = optionalCandidate.get().getName();
            JsonObject data = new JsonObject();
            data.addProperty("email",email);
            data.addProperty("password",password);
            data.addProperty("name",name);

            JsonObject responseJson = JsonUtils.createResponse("LOOKUP_ACCOUNT_CANDIDATE", "SUCCESS", "");
            responseJson.add("data",data);

            logWriter("Server",JsonUtils.toJsonString(responseJson));
            out.println(JsonUtils.toJsonString(responseJson));


            return;

        }*/

        PreparedStatement st;
        st = Conexao.getConexao().prepareStatement("SELECT * FROM candidate WHERE id = ?");
        st.setInt(1, candidateId);

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

            JsonObject responseJson = JsonUtils.createResponse("LOOKUP_ACCOUNT_CANDIDATE", "SUCCESS", "");
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


    /*private List<Candidate> readDatabase() throws IOException {  //método utilizado para gerar uma arrayList com os dados presentes no arquivo da base de dados
        List<Candidate> candidates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Candidate candidate = new Candidate(parts[0], parts[1], parts[2], parts[3]);
                candidates.add(candidate);
            }
        } catch (FileNotFoundException e) {
            System.out.println("O arquivo não foi encontrado, criando um novo");
        }
        return candidates;
    }*/

    /*private void writeDatabase(List<Candidate> candidates) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (Candidate candidate : candidates) {
                String candidateData = candidate.getId() + "," + candidate.getEmail() + "," + candidate.getPassword() + "," + candidate.getName();
                writer.write(candidateData);
                writer.newLine();
            }
        }
    }*/

    private void logWriter(String menssager, String message) throws IOException
    {
        fileWriter.write(menssager + ": " + message);
        fileWriter.newLine();
        fileWriter.flush();
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
