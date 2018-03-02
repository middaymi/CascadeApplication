package data;

public class IsuElementType {

    private int id;
    private String fullName;
    private String description;

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }    
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String toString() {
        return fullName;
    }
}
