package data;

//as data for ElementRow

import java.util.HashMap;

public class ElementIsu {    
    
    private int elementId = 0; //+
    private int elementTypeId = 0; //+
    private String name = ""; //+
    private String info = ""; //+
    private float baseValue = 0.0f; //+
    private HashMap<Integer, ElementValue> judgesValues = new HashMap<>(); //+
    private float scores = 0;    

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    public int getElementTypeId() {
        return elementTypeId;
    }

    public void setElementTypeId(int elementTypeId) {
        this.elementTypeId = elementTypeId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(float baseValue) {
        this.baseValue = baseValue;
    }

    public HashMap<Integer, ElementValue> getJudgesValues() {
        return judgesValues;
    }

    public void setJudgesValues(HashMap<Integer, ElementValue> judgesValues) {
        this.judgesValues = judgesValues;
    }

    public float getScores() {
        return scores;
    }

    public void setScores(float scores) {
        this.scores = Math.round(scores * 100.0f) / 100.0f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
