package data;

import java.util.ArrayList;
import java.util.HashMap;

public class RankCalculation {

    private static final int THRESHOLD = -1;
    private static final int THRESHOLD_V2019 = -2;

    public static boolean calculateRankExecution(ArrayList<ElementIsu> elementsList, HashMap<Integer, ElementData> elements, int rank) {

        ArrayList<ArrayList<IsuElementValue>> valuesList = new ArrayList<ArrayList<IsuElementValue>>();
        for (ElementIsu element: elementsList) {
            ArrayList<IsuElementValue> values = new ArrayList<>();
            ElementData el = elements.get(element.getElementId());
            for (ElementValue val : element.getJudgesValues().values()) {
                IsuElementValue isuValue = new IsuElementValue();
                isuValue.setMark(val.getMark());
                isuValue.setElement(el);
                values.add(isuValue);
            }
            valuesList.add(values);
        }
                        
        boolean result = true;
        /**
         * Разряды
         * 0 - Норма "Юный фигурист"
         * 1 - 3 юношеский спортивный разряд
         * 2 - 2 юношеский спортивный разряд
         * 3 - 1 юношеский спортивный разряд
         * 4 - 2 спортивный разряд
         * 5 - 1 спортивный разряд
         * 6 - КМС
         */

        switch(rank) {
            // Юный фигурист (средняя оценка бригады судей не менее -1)
            case 0: {
                int sum = 0;
                for (ArrayList<IsuElementValue> values : valuesList) {
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                    }
                }
                double average = 1.0 * sum / (valuesList.size() * valuesList.get(0).size());
                if (average < THRESHOLD) result = false;
                break;
            }
            // 3 юношеский (все элементы не менее, чем на -1)
            case 1: {
                for (ArrayList<IsuElementValue> values : valuesList) {
                    int sum = 0;
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                    }
                    double average = 1.0 * sum / valuesList.get(0).size();
                    if (average < THRESHOLD) {
                        result = false;
                        break;
                    }
                }
                break;
            }
            // 2 юношеский (средняя оценка бригады судей не менее -1 + проверка наличия элементов)
            case 2: {

                ArrayList<String> possibleElements = new ArrayList<>();
                possibleElements.add("2T");
                possibleElements.add("2S");
                possibleElements.add("2Lo");
                possibleElements.add("2Lz");
                possibleElements.add("2F");
                possibleElements.add("1A");

                ArrayList<String> executeElements = new ArrayList<>();

                int sum = 0;
                for (ArrayList<IsuElementValue> values : valuesList) {
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                        if (possibleElements.contains(value.getElement().getAbbreviation()) 
                                && !executeElements.contains(value.getElement().getAbbreviation())) {
                            executeElements.add(value.getElement().getAbbreviation());
                        }
                    }
                }
                double average = 1.0 * sum / (valuesList.size() * valuesList.get(0).size());
                if (average < THRESHOLD) result = false;
                if (!executeElements.contains("1A") || !(executeElements.size() >= 3)) 
                    return false;
                break;
            }
            // 1 юношеский (средняя оценка бригады судей не менее -1 + 
            //проверка исполнения ряда элементов не менее, чем на -1)
            case 3: {
                ArrayList<String> possibleElements = new ArrayList<>();
                possibleElements.add("2T");
                possibleElements.add("2S");
                possibleElements.add("2Lo");
                possibleElements.add("2Lz");
                possibleElements.add("2F");
                possibleElements.add("1A");

                ArrayList<String> executeElements = new ArrayList<>();

                int sum = 0;
                for (ArrayList<IsuElementValue> values : valuesList) {
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                    }
                }
                double average = 1.0 * sum / (valuesList.size() * valuesList.get(0).size());
                if (average < THRESHOLD) result = false;


                for (ArrayList<IsuElementValue> values : valuesList) {
                    if (executeElements.contains(values.get(0).getElement().getAbbreviation())) {
                        executeElements.add(values.get(0).getElement().getAbbreviation());
                        int resSum = 0;
                        for (IsuElementValue value : values) {
                            resSum += value.getMark();
                        }
                        double resAverage = 1.0 * resSum / valuesList.get(0).size();
                        if ((values.get(0).getElement().getAbbreviation().equals("1A") && 
                                resAverage < THRESHOLD) || 
                                (resAverage < THRESHOLD && 
                                !values.get(0).getElement().getAbbreviation().equals("1A") 
                                && executeElements.size() <= 3 )) result = false;
                    }
                }

                if (!executeElements.contains("1A") || !(executeElements.size() >= 3)) return false;

                break;
            }
            // 2 спортивный (проверка исполнения ряда элементов не менее, чем на -1)
           /* case 4: {
                int sum = 0;
                for (ArrayList<IsuElementValue> values : valuesList) {
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                    }
                }
                double average = 1.0 * sum / (valuesList.size() * valuesList.get(0).size());
                if (average < THRESHOLD) result = false;
                break;
            }
            // 1 спортивный (проверка исполнения ряда элементов не менее, чем на -1)
            case 5: {
                int sum = 0;
                for (ArrayList<IsuElementValue> values : valuesList) {
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                    }
                }
                double average = 1.0 * sum / (valuesList.size() * valuesList.get(0).size());
                if (average < THRESHOLD) result = false;
                break;
            }
            */
        }

        return result;
    }

    /**
     * @rank - разряд
     * 0 - Норма "Юный фигурист"
     * 1 - 3 юношеский спортивный разряд
     * 2 - 2 юношеский спортивный разряд
     * 3 - 1 юношеский спортивный разряд
     * 4 - 2 спортивный разряд
     * 5 - 1 спортивный разряд
     * 6 - КМС
     * 7 - 3 спортивный разряд
     */
    public static boolean calculateRankExecutionV2019(ArrayList<ElementIsu> elementsList, HashMap<Integer, ElementData> elements, int rank, float elementScore) {

        boolean result = false;
        int rankLimit = 0;

        switch(rank) {
            // Юный фигурист (средняя оценка не меньше -2)
            case 0: {
                ArrayList<ArrayList<IsuElementValue>> valuesList = new ArrayList<ArrayList<IsuElementValue>>();
                for (ElementIsu element: elementsList) {
                    ArrayList<IsuElementValue> values = new ArrayList<>();
                    ElementData el = elements.get(element.getElementId());
                    for (ElementValue val : element.getJudgesValues().values()) {
                        IsuElementValue isuValue = new IsuElementValue();
                        isuValue.setMark(val.getMark());
                        isuValue.setElement(el);
                        values.add(isuValue);
                    }
                    valuesList.add(values);
                }

                int sum = 0;
                for (ArrayList<IsuElementValue> values : valuesList) {
                    for (IsuElementValue value : values) {
                        sum += value.getMark();
                    }
                }
                double average = 1.0 * sum / (valuesList.size() * valuesList.get(0).size());
                if (average < THRESHOLD_V2019) result = false;

                break;
            }
            // 3 юношеский (сумма за технику не меньше 7)
            case 1: {
                rankLimit = 7;
                result = elementScore >= rankLimit;
                break;
            }
            // 2 юношеский (сумма за технику не меньше 11)
            case 2: {
                rankLimit = 11;
                result = elementScore >= rankLimit;
                break;
            }
            // 1 юношеский (сумма за технику не меньше 13)
            case 3: {
                rankLimit = 13;
                result = elementScore >= rankLimit;
                break;
            }
//            // 2 спортивный (сумма за технику не меньше 13/19 )
//            case 4: {
//
//                break;
//            }
//            // 1 спортивный (сумма за технику не меньше 19/30)
//            case 5: {
//
//                break;
//            }
//            // КМС (сумма за технику не меньше )
//            case 6: {
//
//                break;
//            }
            // 3 спортивный (сумма за технику не меньше 16)
            case 7: {
                rankLimit = 16;
                result = elementScore >= rankLimit;
                break;
            }
        }

        return result;
    }
}
