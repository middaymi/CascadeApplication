package controllers.TestComPage.SingleComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

public class SaveDeduction implements ActionListener {
    IsuComModel isuComModel;
    SingleStComPage singleStComPage;
   
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("IT WORKED");
        isuComModel = IsuComModel.getModelInstance();
        singleStComPage = Manager.getSingleComPage();
        
        //check empty "deductions" and selected athlete
        if(!singleStComPage.getDeductions().getText().isEmpty() &&
            singleStComPage.getAthlCmb().getSelectedItem() != null) {
            JTextField tf = singleStComPage.getDeductions();
            if (isuComModel.checkDeductionsAndComponentsValue(tf.getText())) {
                float deduction = Float.valueOf(tf.getText());
                //set dedutions to element
                isuComModel.getCIAR().setDeductions(deduction);                
            } else {
                JOptionPane.showMessageDialog(Manager.getSingleComPage(),
                "Неверное значение для Deductions!",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
                singleStComPage.setTextDeductions("");
                return;
            }
        } else {
            JOptionPane.showMessageDialog(Manager.getSingleComPage(),
            "Не выбран спортсмен!",
            "Ошибка", JOptionPane.WARNING_MESSAGE);
            singleStComPage.setTextDeductions("");
            return;
        }
    }   
}