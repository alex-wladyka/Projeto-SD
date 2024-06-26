package Candidate;

public class Candidate {


    public Candidate(String id,String skill, String experience, String id_Skill) {
        this.id = id;
        this.skill = skill;
        this.experience = experience;
        this.id_skill = id_Skill;
    }

    private String id;
    private String id_skill;
    private String skill;
    private String experience;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getId_skill() {return id_skill;}

    public void setId_skill(String id_skill) {this.id_skill = id_skill;}

    @Override
    public String toString() {
        return  "ID do Candidato='" + id +
                " | Skill='" + skill +
                " | ID da Skill='" + id_skill +
                " | Experiencia='" + experience;
    }
}
