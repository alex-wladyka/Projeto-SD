package Recruiter.Jobs;

public class Jobs {
    private int idJob;
    private String nameSkill;
    private int experience;
    private String available;

    public Jobs(int idJob, String nameSkill, int experience, String available) {
        this.idJob = idJob;
        this.nameSkill = nameSkill;
        this.experience = experience;
        this.available = available;
    }

    public int getIdJob() {
        return idJob;
    }

    public void setIdJob(int idJob) {
        this.idJob = idJob;
    }

    public String getNameSkill() {
        return nameSkill;
    }

    public void setNameSkill(String nameSkill) {
        this.nameSkill = nameSkill;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getAvailable() { return available;}

    public void setAvailable(String available) { this.available = available;}

    @Override
    public String toString() {
        return "ID='" + idJob +
                " | Skill='" + nameSkill +
                " | Experiencia='" + experience +
                " | Available='" + available;
    }
}
