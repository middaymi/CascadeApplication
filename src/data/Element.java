package data;

public class Element {
    private int id;
    private String fullName;    
    private String description;
    private String units;  
    private String tableName;

    public int getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getDescription() {
        return description;
    }
    public String getUnits() {
        return units;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUnits(String units) {
        this.units = units;
    }
   
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    @Override
    public String toString() {
        return fullName;
    }
}
