package Candidate.Skills;

public class Skills {

    private String skill;
    private int experience;

    public Skills(String skill, int experience) {
        this.skill = skill;
        this.experience = experience;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return
                "Nome da Skill='" + skill + " | " +
                "Experiencia=' " + experience + " anos";
    }
}
