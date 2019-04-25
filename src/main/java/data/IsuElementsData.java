package data;

import java.util.ArrayList;
import java.util.HashMap;

public class IsuElementsData {
    private static HashMap<Integer, ElementData> data = new HashMap<>();         
    private static ArrayList<String> infoValues = new ArrayList<>();
    private static HashMap<Integer, Float> factor = new HashMap<>();
    private static ArrayList<String> goe = new ArrayList<>();
    
    static {        
        // add info 
        infoValues.add("");
        infoValues.add("<");
        infoValues.add("<<");
        infoValues.add("!");
        infoValues.add("e");
        infoValues.add("< e");
        infoValues.add("<< e");
        infoValues.add("V");
        infoValues.add("x");
        infoValues.add("x <");
        infoValues.add("x <<");
        infoValues.add("x !");
        infoValues.add("x e");
        
        goe.add("-5");
        goe.add("-4");
        goe.add("-3");
        goe.add("-2");
        goe.add("-1");
        goe.add("0");
        goe.add("+1");
        goe.add("+2");
        goe.add("+3");
        goe.add("+4");
        goe.add("+5");

        factor.put(1, 1.2f);
        factor.put(2, 1.5f);
        factor.put(3, 1.6f);
        factor.put(4, 2.0f);
        factor.put(5, 1.8f);
        factor.put(6, 1.6f);
        factor.put(7, 1.6f);                             
    }
    
    public static void setData(HashMap<Integer, ElementData> elData) {
        data = elData;
    } 
    
    public static ArrayList<String> getInfoValues() {
        return infoValues;
    }
    
    public static float getFactor(int rank) {
        return factor.get(rank);
    }
    
    public static ArrayList<String> getGoe() {
        return goe;
    }
    
    public static float getElementBase(Integer elementId, String info) {
        ElementData element = data.get(elementId);
        
        // calculate base
        float base = element.getBase();                
        
        if (info.equals("<")) {
            base = element.getBaseV();
        } else if (info.equals("<<")) {
            Integer lowId = element.getBaseV2();
            element = data.get(lowId);
            base = element.getBase();            
        } else if (info.equals("e")) {
            base = element.getBaseV();
        } else if (info.equals("!")) {
            base = element.getBase();
        } else if (info.equals("< e")) {
            base = element.getBaseV1();
        } else if (info.equals("V")) {
            base = element.getBaseV();
        } else if (info.equals("<< e")) {
            Integer lowId = element.getBaseV2();
            element = data.get(lowId);
            base = element.getBaseV();
        } else if (info.equals("x") && element.getElementTypeId() == 1) {
            base = 1.1f * element.getBase();  
        } else if (info.equals("x <") && element.getElementTypeId() == 1) {
            base = 1.1f * element.getBaseV();  
        } else if (info.equals("x <<") && element.getElementTypeId() == 1) {
            Integer lowId = element.getBaseV2();
            element = data.get(lowId);            
            base = 1.1f * element.getBase();  
        } else if (info.equals("x !") && element.getElementTypeId() == 1) {
            base = 1.1f * element.getBase();  
        } else if (info.equals("x e") && element.getElementTypeId() == 1) {
            base = 1.1f * element.getBaseV1();  
        }
        return base;
    }
    
    public static float getElementValue(Integer elementId, String info, int mark) {
         
        if (elementId <= 6 && (info.contains("<<"))) {
            return 0;
        }
        
        ElementData element = data.get(elementId);
        
        // get base
        float base = getElementBase(elementId, info);
        
        if (info.contains("<<")) {
            Integer lowId = element.getBaseV2();
            element = data.get(lowId);
        }
        
        //calculate offset
        float offset = 0;
        
        switch (mark) {
            case -3: 
                offset = element.getValueMinus3();
                break;
            case -2: 
                offset = element.getValueMinus2();
                break;
            case -1: 
                offset = element.getValueMinus1();
                break;
            case 1: 
                offset = element.getValuePlus1();
                break;
            case 2: 
                offset = element.getValuePlus2();
                break;
            case 3: 
                offset = element.getValuePlus3();
                break;                
        }
        
        return base + offset;        
    }
}
