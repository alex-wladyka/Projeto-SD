public class UserConnected {

    public UserConnected(String ip, String id, String role, String email) {
        this.ip = ip;
        this.id = id;
        this.role = role;
        this.email = email;
    }

    private String ip;
    private String id;
    private String role;
    private String email;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        return "Usuario Conectado - { " +
                "Ip='" + ip + '\'' +
                ", id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
