package data;

public class CompetitionKind {
    private int id;
    private String fullName;
    private String tableName;
    private String description;

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDescription(String description) {
        this.description = description;
    }  
    
    @Override
    public String toString() {
        return fullName;
    }
}
