package data;

public class Component {
    private Integer id;
    private int rankId;
    private String fullNameENG;
    private String fullNameRUS;
    private String description;

    public Integer getId() {
        return id;
    }

    public int getRankId() {
        return rankId;
    }

    public String getFullNameENG() {
        return fullNameENG;
    }

    public String getFullNameRUS() {
        return fullNameRUS;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    public void setFullNameENG(String fullNameENG) {
        this.fullNameENG = fullNameENG;
    }

    public void setFullNameRUS(String fullNameRUS) {
        this.fullNameRUS = fullNameRUS;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
