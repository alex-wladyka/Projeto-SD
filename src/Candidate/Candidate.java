package Candidate;

public class Candidate {


    public Candidate(String id,String skill, String experience) {
        this.id = id;
        this.skill = skill;
        this.experience = experience;
    }

    private String id;
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

    @Override
    public String toString() {
        return "ID='" + id +
                " | Skill='" + skill +
                " | Experiencia='" + experience;
    }
}
