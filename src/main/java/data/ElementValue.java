package data;

public class ElementValue {
    
    private int elementId;
    private int judgeId;
    private int mark;
    private float value;
    
    private boolean isSaved = false;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Math.round(value * 100.0f) / 100.0f;
    }

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    public int getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
    }    

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }
}
