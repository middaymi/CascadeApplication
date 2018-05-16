package controllers.TestComPage.SingleComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.Athlete;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

public class SaveDeduction implements ActionListener {
    IsuComModel isuComModel;
    SingleStComPage singleStComPage;
   
    @Override
    public void actionPerformed(ActionEvent e) {
        isuComModel = IsuComModel.getModelInstance();
        singleStComPage = Manager.getSingleComPage();
        
        //check empty "deductions" and selected athlete
        if(!singleStComPage.getDeductions().getText().isEmpty() &&
            singleStComPage.getAthlCmb().getSelectedItem() != null) {
            JTextField tf = singleStComPage.getDeductions();
            if (isuComModel.checkDeductionsAndComponentsValue(tf.getText())) {
                float deduction = Float.valueOf(tf.getText());
                //set deductions to element
                isuComModel.getCIAR().setDeductions(deduction);
                addDeductionToDB(deduction);
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

    private void addDeductionToDB(float deduction) {
        PreparedStatement prst = null;
        String query;

        int compId = isuComModel.getCompetition().getId();
        int athleteId = ((Athlete)singleStComPage.getAthlCmb().getSelectedItem()).getId();
        int startNumber = isuComModel.getCIARS().get(athleteId).getStartNumber();

        try {
            if (!isuComModel.isDeductionSaved()) {
                query = "INSERT INTO RESULT VALUES ( " +
                        compId + ", " + athleteId + ", null, " +
                        "0 ,  0 ,"  + startNumber + ", " +
                        "null ,  null , " +
                        deduction + ", null, null);";
                isuComModel.setDeductionSaved(true);
            } else {
                query = "UPDATE RESULT SET Deductions = " + deduction +
                        " WHERE IDathlete = " + athleteId + " AND " +
                        "IDcompetition = " + compId + ";";
            }
            System.out.println(query);
            prst = isuComModel.getDBC().prepareStatement(query);
            prst.execute();
            prst.close();
        } catch (SQLException ex) {
            Logger.getLogger(FinishComByAthlete.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}