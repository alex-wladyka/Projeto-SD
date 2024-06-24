package Recruiter;

public class Recruiter {


    public Recruiter(String email, String name, String industry, String description) {
        this.email = email;
        this.name = name;
        this.industry = industry;
        this.description = description;
    }

    private String email;
    private String name;
    private String industry;
    private String description;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name;}

    public String getIndustry() {return industry;}

    public void setIndustry(String industry) {this.industry = industry;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    @Override
    public String toString() {
        return  "Nome='" + name +
                " | Industry='" + industry +
                " | Email='" + email +
                " | Descrição='" + description;
    }
}
