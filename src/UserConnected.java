public class UserConnected {

    public UserConnected(String token, String id, String role) {
        this.token = token;
        this.id = id;
        this.role = role;
    }

    private String token;
    private String id;
    private String role;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Usuario Conectado{" +
                "token='" + token + '\'' +
                ", id='" + id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
