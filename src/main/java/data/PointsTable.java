package data;

import java.util.ArrayList;

public class PointsTable {
    private Integer elementId;
    private ArrayList<PointsRow> table = new ArrayList<>();

    public PointsTable(Integer elementId) {
        this.elementId = elementId;
        //get value from table
    }

    public int getNormalPoints(int points) {
        if (points == 0) return 0;
        return (points - 1) / 5 + 2;
    }

    public int getPoints(boolean sex, int age, Float value) {
        if (value == null) {
            return -1;
        } else {
            for (PointsRow row : getTable()) {
                if (row.isSex() == sex && row.getAge() == age) {
                    float min = row.getMin();
                    float max = row.getMax();

                    float minMin = (min < max) ? min : max;
                    float maxMax = (min < max) ? max : min;

                    if (min < max) {
                        if (value < minMin) {
                            return 0;
                        }
                    } else {
                        if (value > maxMax) {
                            return 0;
                        }
                    }

                    float step = Math.abs(max - min) / 20;
                    float points = Math.abs(value - min) / step;
                    int res = Math.round(points);
                    return res;
                }
            }
        }
        return -1;
    }

    public Integer getElementId() {
        return elementId;
    }

    public ArrayList<PointsRow> getTable() {
        return table;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public void setTable(ArrayList<PointsRow> table) {
        this.table = table;
    }
}
