package data;

public class ElementData {
    private Integer id;
    private int elementTypeId;
    private String fullNameEng;
    private String fullNameRus;
    private String abbreviation;
    private int level;
    private float base;	
    private Integer baseV;
    private Integer baseV1;
    private Integer baseV2;
    private float valuePlus1;	
    private float valuePlus2;
    private float valuePlus3;
    private float valuePlus4;
    private float valuePlus5;
    private float valueMinus1;
    private float valueMinus2;
    private float valueMinus3;
    private float valueMinus4;
    private float valueMinus5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElementTypeId() {
        return elementTypeId;
    }

    public void setElementTypeId(int elementTypeId) {
        this.elementTypeId = elementTypeId;
    }

    public String getFullNameEng() {
        return fullNameEng;
    }

    public void setFullNameEng(String fullNameEng) {
        this.fullNameEng = fullNameEng;
    }

    public String getFullNameRus() {
        return fullNameRus;
    }

    public void setFullNameRus(String fullNameRus) {
        this.fullNameRus = fullNameRus;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
	
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public int getBaseV() {
        return baseV;
    }

    public void setBaseV(int baseV) {
        this.baseV = baseV;
    }

    public int getBaseV1() {
        return baseV1;
    }

    public void setBaseV1(int baseV1) {
        this.baseV1 = baseV1;
    }

    public int getBaseV2() {
        return baseV2;
    }

    public void setBaseV2(int baseV2) {
        this.baseV2 = baseV2;
    }

    public float getValuePlus1() {
        return valuePlus1;
    }

    public void setValuePlus1(float valuePlus1) {
        this.valuePlus1 = valuePlus1;
    }

    public float getValuePlus2() {
        return valuePlus2;
    }
	
    public void setValuePlus2(float valuePlus2) {
        this.valuePlus2 = valuePlus2;
    }

    public float getValuePlus3() {
        return valuePlus3;
    }

    public void setValuePlus3(float valuePlus3) {
        this.valuePlus3 = valuePlus3;
    }

    public float getValuePlus4() {
        return valuePlus4;
    }

    public void setValuePlus4(float valuePlus4) {
        this.valuePlus4 = valuePlus4;
    }

    public float getValuePlus5() {
        return valuePlus5;
    }

    public void setValuePlus5(float valuePlus5) {
        this.valuePlus5 = valuePlus5;
    }

    public float getValueMinus1() {
        return valueMinus1;
    }

    public void setValueMinus1(float valueMinus1) {
        this.valueMinus1 = valueMinus1;
    }
	
    public float getValueMinus2() {
        return valueMinus2;
    }

    public void setValueMinus2(float valueMinus2) {
        this.valueMinus2 = valueMinus2;
    }

    public float getValueMinus3() {
        return valueMinus3;
    }

    public void setValueMinus3(float valueMinus3) {
        this.valueMinus3 = valueMinus3;
    }

    public float getValueMinus4() {
        return valueMinus4;
    }

    public void setValueMinus4(float valueMinus4) {
        this.valueMinus4 = valueMinus4;
    }

    public float getValueMinus5() {
        return valueMinus5;
    }

    public void setValueMinus5(float valueMinus5) {
        this.valueMinus5 = valueMinus5;
    }
    @Override
    public String toString() {
        return this.fullNameRus + " (" + this.abbreviation + ")";
    }    
}
