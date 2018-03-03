//component value 
package data;

public class ComponentValue {
    private int componentId = 0;//+
    private int judgeId = 0; //+
    private float value = 0; //+
    
    private boolean isSaved = false;

    public int getComponentId() {
        return componentId;
    }

    public int getJudgeId() {
        return judgeId;
    }

    public float getValue() {
        return value;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }
}
