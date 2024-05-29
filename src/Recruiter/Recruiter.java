package Recruiter;

public class Recruiter {


    public Recruiter(String id, String email, String password, String name, String industry, String description) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.industry = industry;
        this.description = description;
    }

    private String id;
    private String email;
    private String password;
    private String name;
    private String industry;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name;}

    public String getIndustry() {return industry;}

    public void setIndustry(String industry) {this.industry = industry;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}
}
