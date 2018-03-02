package data;

public class Judge {
    private int id;
    private JudgeType type;
    private String surname;
    private String name;
    private String middlename;
    private String category;

    public int getId() {
        return id;
    }
    public JudgeType getType() {
        return type;
    }
    public String getSurname() {
        return surname;
    }
    public String getName() {
        return name;
    }
    public String getMiddlename() {
        return middlename;
    }
    public String getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setType(JudgeType type) {
        this.type = type;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return surname + " " + name + " " + middlename;
    }
}
