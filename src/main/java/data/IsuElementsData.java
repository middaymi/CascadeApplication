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
        infoValues.add("+REP");
        
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
    
    public static ElementData getElementBase(Integer elementId, String info) {

        ElementData element = data.get(elementId);
        ElementData elementRes = null;

        // calculate base
//        float base = element.getBase();

        // Заменить по правилам ИСУ смену базы + уточнить *1.1
        if (info.equals("<")) {
            elementRes = data.get(element.getBaseV());
        } else if (info.equals("<<")) {
            elementRes = data.get(element.getBaseV2());
        } else if (info.equals("e")) {
            elementRes = data.get(element.getBaseV());
        } else if (info.equals("!")) {
            // save element
        } else if (info.equals("< e")) {
            elementRes = data.get(element.getBaseV1());
        } else if (info.equals("V")) {
            elementRes = data.get(element.getBaseV());
        } else if (info.equals("<< e")) {
            Integer idElement = data.get(element.getBaseV2()).getBaseV();
            if (idElement != 0) elementRes = data.get(idElement);
        }

        if (elementRes != null) {
            element = elementRes;
        }

        return element;
    }

    public static float getElementValue(Integer elementId, String info, int mark) {
         
        if (elementId <= 6 && (info.contains("<<"))) {
            return 0;
        }
        
        ElementData element = getElementBase(elementId, info);

        float base = element.getBase();
        
        //calculate offset
        float offset = 0;
        
        switch (mark) {
            case -5:
                offset = element.getValueMinus5();
                break;
            case -4:
                offset = element.getValueMinus4();
                break;
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
            case 4:
                offset = element.getValuePlus4();
                break;
            case 5:
                offset = element.getValuePlus5();
                break;
        }

        if (info.equals("+REP")) {
            base = base * 0.7f;
        }
        else if (info.contains("x") && element.getElementTypeId() == 1) {
            base = 1.1f * element.getBase();
        }

        return base + offset;        
    }
}
