package controllers.TestComPage.StComPages;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import models.TestCom.StartCom.StComModel;
import models.TestCom.TestComModel;
import views.CommonSettings;
import views.Manager;
import views.TestCom.StartCom.MarkTextField;

public class SaveValue implements ActionListener {
    LineBorder redline = new LineBorder(Color.red, 4);
    MarkTextField field;
    TestComModel tcModel;
    StComModel stComModel;
    int kind;
    float value;
        
    public SaveValue(MarkTextField field) {
        this.field = field;
    }
    
    public void actionPerformed(ActionEvent e) {
        field.setBorder(null);
        CommonSettings.settingLightGrayBorder(field);
        tcModel = TestComModel.getTestComModelInstance();
        stComModel = StComModel.getStComModelInstance();  
        kind = (int) tcModel.getValueAt(tcModel.selRow(), 2); 
        
        
        if (checkWithRegExp(field.getText().trim())) {
            //set mode = 1 competition mode
            stComModel.setMode(1);
            
            //get value as float
            value = Float.valueOf(field.getText().trim());
            //save value to data class
            field.getData().setValue(value);            
            stComModel.saveDataToDB(field);
            System.out.println("SET_VALUE: " + value);

        } else {
            if (checkFloat(field.getText().trim())) {
                JOptionPane.showMessageDialog(Manager.getStComPage(),
                    "Неверное значение!\n" +
                    "Используйте точку вместо запятой.",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
            }else {                
                JOptionPane.showMessageDialog(Manager.getStComPage(),
                    "Неверное значение!",
                    "Ошибка", JOptionPane.WARNING_MESSAGE); 
            }
            field.setBorder(redline);   
            if (field.getData().getValue() == null) {
                field.setText("");
            } else {
                field.setText(String.valueOf(field.getData().getValue())); 
            }                    
        }
    }
    
    private boolean checkWithRegExp(String enterValue){  
        Pattern p = Pattern.compile("^-?[0-9]{1,3}((\\.)[0-9]{0,2})?$");  
        Matcher m = p.matcher(enterValue);  
        return m.matches();  
    }
    
     private boolean checkFloat(String enterValue){  
        Pattern p = Pattern.compile("^-?[0-9]{1,3}((\\,)[0-9]{0,2})?$");  
        Matcher m = p.matcher(enterValue);  
        return m.matches();  
    }
}
