package data;

public class Rank {
    private Integer id;
    private int programsCount;
    private String fullName;
    private String requirements;
    private String programStructure;

    public Integer getId() {
        return id;
    }

    public int getProgramsCount() {
        return programsCount;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getProgramStructure() {
        return programStructure;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProgramsCount(int programsCount) {
        this.programsCount = programsCount;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setProgramStructure(String programStructure) {
        this.programStructure = programStructure;
    }
    
    public String toString() {
        return fullName;
    }
    
}
