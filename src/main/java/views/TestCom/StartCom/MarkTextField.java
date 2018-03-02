package views.TestCom.StartCom;

import javax.swing.JTextField;
import data.MarkCellData;
import java.awt.Color;
import views.CommonSettings;

public class MarkTextField extends JTextField{
      
    private MarkCellData data;
    private boolean savedDB;
    private int resultId;
    
    
    public MarkTextField(MarkCellData data) {
        this.savedDB = false;
        this.data = data;
        this.setSize(200, 100);        
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
