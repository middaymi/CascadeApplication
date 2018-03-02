package controllers.TestComPage.SingleComPage;

import data.CompetitionIsuAthleteResult;
import data.ElementIsu;
import data.ElementRow;
import data.ElementValue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

public class ViewMarks implements ActionListener{
    IsuComModel isuComModel;
    SingleStComPage singleComPage;
    CompetitionIsuAthleteResult CIAR;
    @Override
    public void actionPerformed(ActionEvent e) {
        isuComModel = IsuComModel.getModelInstance();
        singleComPage = Manager.getSingleComPage();
        CIAR = isuComModel.getCIAR();       
        
        if (isuComModel.isFinished()) {
            System.out.println("MARKS");
            for (ElementRow elRow : singleComPage.getElRows()) {
                ElementIsu elIsu = elRow.getElementIsu();
                ArrayList<ElementValue> elVal = new ArrayList<>();
                elVal.addAll(elIsu.getJudgesValues().values());                                
                for (int i = 0; i < elVal.size(); i++) {                    
                    //combobox
                    JComboBox selCmb = elRow.getJudgeMarks().get(i);
                    //setted value at combobox
                    String settedValue = (String)selCmb.getSelectedItem();
                    //remove this item from combobox
                    selCmb.removeItem(settedValue);                    
                    //get mark value 
                    String value = String.valueOf(elVal.get(i).getMark());
                    //set this value                    
                    elRow.getJudgeMarks().get(i).setSelectedItem(value);
                    //setEnadled to edit field
                    //elRow.getJudgeMarks().get(i).setEnabled(false);
                }                    
            }        
        } else {
            JOptionPane.showMessageDialog(Manager.getSingleComPage(), 
            "Для начала подведите итог!", "Ошибка!",
            JOptionPane.INFORMATION_MESSAGE);
            return;            
        }        
    }    
}
