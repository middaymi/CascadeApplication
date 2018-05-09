package views.TestCom.StartCom;

import javax.swing.JTextField;
import data.MarkCellData;
import java.awt.Color;
import views.CommonSettings;

import static utils.Layout.calcH;
import static utils.Layout.calcW;

public class MarkTextField extends JTextField{
      
    private MarkCellData data;
    private boolean savedDB;
    private int resultId;
    
    
    public MarkTextField(MarkCellData data) {
        this.savedDB = false;
        this.data = data;
        this.setSize(calcW(200), calcH(100));
        this.setVisible(true);
        this.setOpaque(true);
        this.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingFont30(this);
        CommonSettings.settingLightGrayBorder(this);
        this.setBackground(Color.WHITE);        
    }
    
    public MarkCellData getData() {
        return data;
    }
    public boolean isSaved() {
        return savedDB;
    }
    public int getResultId() {
        return resultId;
    }

    public void setData(MarkCellData data) {
        this.data = data;
    }
    public void saveToDB() {
        savedDB = true;
    } 
    public void setResultId(int resultId) {
        this.resultId = resultId;
    }
}