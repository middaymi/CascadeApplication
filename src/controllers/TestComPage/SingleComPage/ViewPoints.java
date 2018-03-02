package controllers.TestComPage.SingleComPage;

import data.ElementIsu;
import data.ElementRow;
import data.ElementValue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

public class ViewPoints  implements ActionListener {
    IsuComModel isuComModel;
    SingleStComPage singleComPage;
    @Override
    public void actionPerformed(ActionEvent e) {
        isuComModel = IsuComModel.getModelInstance();
        singleComPage = Manager.getSingleComPage();
        
        if (isuComModel.isFinished()) {
            for (ElementRow elRow : singleComPage.getElRows()) {
                ElementIsu elIsu = elRow.getElementIsu();
                //get a value
                ArrayList<ElementValue> elVal = new ArrayList<>();
                elVal.addAll(elIsu.getJudgesValues().values()); 
                //for each combobox
                for (int i = 0; i < elVal.size(); i++) {
                    String value = String.valueOf(elVal.get(i).getValue());
                    elRow.getJudgeMarks().get(i).addItem(value);
                    elRow.getJudgeMarks().get(i).setSelectedItem(value);
                    //elRow.getJudgeMarks().get(i).setEnabled(false);
                }                    
            }
            
        } else {
            JOptionPane.showMessageDialog(Manager.getSingleComPage(), 
            "Для начала подведите итог!", "Ошибка!",
            JOptionPane.INFORMATION_MESSAGE);
        }   
    }    
}
