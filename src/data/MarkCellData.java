/*data for startCom!
For saving data of competition? for startCom classes.
It is used for each crossing of athlete and element, for glassial competitions
judges are added
 */
package data;

public class MarkCellData {

    private Float value = null;
    private Integer athleteId = null; 
    private Integer elementId = null;
    private Integer judgeId = null;
    private int normalMark = 0;
    private int place = 0; //by every element

    public MarkCellData(Integer athleteId, Integer elementId, Integer judgeId, Float value) {
        this.athleteId = athleteId;
        this.elementId = elementId;
        this.judgeId = judgeId;
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Integer sportsmenId) {
        this.athleteId = sportsmenId;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public Integer getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(Integer judgeId) {
        this.judgeId = judgeId;
    }

    public int getNormalMark() {
        return normalMark;
    }

    public void setNormalMark(int normalMark) {
        this.normalMark = normalMark;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}