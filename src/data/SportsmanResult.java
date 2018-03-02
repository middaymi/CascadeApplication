package data;

public class SportsmanResult implements Comparable{

    private Athlete athlete = null; //+
    private float sumOfMarks = 0; //sum result
    private int sumOfRanks = 0; //sumOFplaces
    private int place = 0; //final place  //?for single
    private float averageMark = 0.0f;
    private boolean isDone = true;
    private float sumOfAllElements = 0.0f;
    private float sumOfAllComponents = 0.0f;
    private float deductions = 0.0f;    
    private int startNumber = 0;
    
    public SportsmanResult(Athlete athlete) {
        this.athlete = athlete;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public float getSumOfMarks() {
        return sumOfMarks;
    }

    public void setSumOfMarks(float sumOfMarks) {
        this.sumOfMarks = sumOfMarks;
    }

    public int getSumOfRanks() {
        return sumOfRanks;
    }

    public void setSumOfRanks(int sumOfRanks) {
        this.sumOfRanks = sumOfRanks;
    }

    public float getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(float averageMark) {
        this.averageMark = averageMark;
    }

    public boolean isIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public int compareTo(Object o) {
        if (this.place == ((SportsmanResult) o).getPlace()) {
            return 0;
        } else if (this.place > ((SportsmanResult) o).getPlace()) {
            return 1;
        } else {
            return -1;
        }
    }

    public float getSumOfAllElements() {
        return sumOfAllElements;
    }

    public float getSumOfAllComponents() {
        return sumOfAllComponents;
    }

    public float getDeductions() {
        return deductions;
    }

    public void setSumOfAllElements(float sumOfAllElements) {
        this.sumOfAllElements = sumOfAllElements;
    }

    public void setSumOfAllComponents(float sumOfAllComponents) {
        this.sumOfAllComponents = sumOfAllComponents;
    }

    public void setDeductions(float deductions) {
        this.deductions = deductions;
    }    

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }
}
