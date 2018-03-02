//component values (arrayList of ComponentValue)
package data;

import java.util.HashMap;

public class ComponentIsu {
    
    private int componentId = 0; //+
    private float scores = 0;   
    private HashMap<Integer, ComponentValue> judgesValues = new HashMap<>(); //+    

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public HashMap<Integer, ComponentValue> getJudgesValues() {
        return judgesValues;
    }

    public void setJudgesValues(HashMap<Integer, ComponentValue> judgesValues) {
        this.judgesValues = judgesValues;
    }

    public float getScores() {
        return scores;
    }

    public void setScores(float scores) {
        this.scores = Math.round(scores * 100.0f) / 100.0f;
    }
}
