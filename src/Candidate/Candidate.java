package Candidate;

public class Candidate {


    public Candidate(String id,String skill, String experience, String id_Skill, String name) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.experience = experience;
        this.id_skill = id_Skill;
    }

    private String id;
    private String id_skill;
    private String skill;
    private String experience;
    private String name;

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

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return  "ID do Candidato='" + id +
                "' | Nome='"+ name +
                "' | Skill='" + skill +
                "' | ID da Skill='" + id_skill +
                "' | Experiencia='" + experience;
    }
}
