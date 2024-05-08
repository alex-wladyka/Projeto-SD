import java.io.*;
import java.net.*;
import com.google.gson.JsonObject;
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
                           LoginProcess(requestJson, out);
                            break;
                        case "SIGNUP_CANDIDATE":
                            SignUpProcess(out, requestJson);
                            break;
                        case "UPDATE_ACCOUNT_CANDIDATE":
                            UpdateProcess(out, requestJson);
                            break;
                        case "DELETE_ACCOUNT_CANDIDATE":
                            //handleDelete(requestJson, out);
                            break;
                    }
                }
            }
        } catch (IOException e) {
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



    private void SignUpProcess(PrintWriter out,JsonObject requestJson) throws IOException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();
        String name = dataJson.get("name").getAsString();

        List<Candidate> candidates = readDatabase(); //criação da lista de usuarios

        for (Candidate candidate : candidates) {   //teste para ver se o email já foi cadastrado
            if (candidate.getEmail().equals(email)) { //encontrou um email igual
                JsonObject Response = JsonUtils.createResponse("SIGNUP_CANDIDATE", "USER_EXISTS", "");

                out.println(JsonUtils.toJsonString(Response));
                logWriter("Server",JsonUtils.toJsonString(Response));
                return;
            }
        }
        //email não está casdastrado
        Candidate newUser = new Candidate(email, password, name);
        candidates.add(newUser);
        writeDatabase(candidates);



        JsonObject responseJson = JsonUtils.createResponse("SIGNUP_CANDIDATE", "SUCCESS", "");
        out.println(JsonUtils.toJsonString(responseJson));
        logWriter("Server",JsonUtils.toJsonString(responseJson));
    }

    private void LoginProcess(JsonObject requestJson, PrintWriter out) throws IOException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();
        //int id = 0;

        List<Candidate> candidates = readDatabase();
        for (Candidate candidate : candidates) {
            //id++;
            if (candidate.getEmail().equals(email) && candidate.getPassword().equals(password)) {
                String token = JsonUtils.JWTValidator.generateToken(candidate.getEmail(), "CANDIDATE");
                JsonObject responseJson = JsonUtils.createResponse("LOGIN_CANDIDATE", "SUCCESS", token);
                out.println(JsonUtils.toJsonString(responseJson));
                logWriter("Server",JsonUtils.toJsonString(responseJson));
                return;
            }
        }
        JsonObject responseJson = JsonUtils.createResponse("LOGIN_CANDIDATE", "INVALID_LOGIN", "");
        logWriter("Server",JsonUtils.toJsonString(responseJson));
        out.println(JsonUtils.toJsonString(responseJson));
    }

    private void UpdateProcess(PrintWriter out, JsonObject requestJson) throws IOException {
        JsonObject dataJson = requestJson.get("data").getAsJsonObject();
        String email = dataJson.get("email").getAsString();
        String password = dataJson.get("password").getAsString();
        String name = dataJson.get("name").getAsString();
        String token = dataJson.get("token").getAsString();

        List<Candidate> candidates = readDatabase();

        for (Candidate candidate : candidates) {   //teste para ver se o email já foi cadastrado
            if (candidate.getEmail().equals(email)) { //encontrou um email igual
                JsonObject invalidResponse = JsonUtils.createResponse("UPDATE_ACCOUNT_CANDIDATE", "INVALID_EMAIL", "");
                logWriter("Server",JsonUtils.toJsonString(invalidResponse));
                out.println(invalidResponse);
                return;
            }
        }

        String candidateId = JsonUtils.JWTValidator.getIdClaim(token);
        System.out.println(candidateId);

        Optional<Candidate> optionalCandidate = candidates.stream().filter(candidate -> candidate.getEmail().equals(candidateId)).findFirst();

        if (optionalCandidate.isPresent()) {
            Candidate candidate = optionalCandidate.get();

            candidate.setEmail(email);
            candidate.setPassword(password);
            candidate.setName(name);

            writeDatabase(candidates);

            String newToken = JsonUtils.JWTValidator.generateToken(candidate.getEmail(), "CANDIDATE");

            JsonObject responseJson = JsonUtils.createResponse("UPDATE_ACCOUNT_CANDIDATE", "SUCCESS", newToken);
            out.println(JsonUtils.toJsonString(responseJson));
            logWriter("Server",JsonUtils.toJsonString(responseJson));


        }
    }


    private List<Candidate> readDatabase() throws IOException {  //método utilizado para gerar uma arrayList com os dados presentes no arquivo da base de dados
        List<Candidate> candidates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Candidate candidate = new Candidate(parts[0], parts[1], parts[2]);
                candidates.add(candidate);
            }
        } catch (FileNotFoundException e) {
            System.out.println("O arquivo não foi encontrado");
        }
        return candidates;
    }

    private void writeDatabase(List<Candidate> candidates) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (Candidate candidate : candidates) {
                String candidateData = candidate.getEmail() + "," + candidate.getPassword() + "," + candidate.getName();
                writer.write(candidateData);
                writer.newLine();
            }
        }
    }

    private void logWriter(String menssager, String message) throws IOException
    {
        fileWriter.write(menssager + ": " + message);
        fileWriter.newLine();
        fileWriter.flush();
    }
    public static void main(String[] args) {
        int serverPort = 21235;

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
