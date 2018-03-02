package data;

public class Season {
    private int id;
    private String period;

    public int getId() {
        return id;
    }
    public String getPeriod() {
        return period;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public void setPeriod(String period) {
        this.period = period;
    }
    @Override
    public String toString() {
        return getPeriod();
    }
    
}
