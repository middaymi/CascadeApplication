package controllers.TestComPage;

import data.Competition;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.TestCom.StartCom.IsuComModel;
import models.TestCom.StartCom.StComModel;
import models.TestCom.TestComModel;
import views.Manager;

public class StartCom implements ActionListener {
    private Manager manager;
    private TestComModel tcModel;    
    private StComModel stComModel;    
    
    Competition competition;
    int sel;
    int kindId;
    
    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance(); 
        competition = tcModel.getCompetitions().get(tcModel.selRow());
                
        try {       
            sel = tcModel.selRow();
            kindId = competition.getKind().getId(); 

            switch(kindId) {                
                 case(1):
                 case(2):
                 case(3):
                     manager.choosePanel(61);
                     stComModel = StComModel.getStComModelInstance();
                     stComModel.setTextToWelcomeLbl(
                            tcModel.getValueAt(sel, 0) + ". " +
                            tcModel.getValueAt(sel, 3) + ".   " +
                            tcModel.getValueAt(sel, 5));
                     stComModel.setAllData();                     
                     break;
                 //SINGLE
                 case(4): 
                     stComModel = IsuComModel.getModelInstance();
                     
                     if (tcModel.getCompetitions().get(sel).getRankId() == 0) {
                        JOptionPane.showMessageDialog(Manager.getTestCompPage(),
                        "Установите разряд в форме редактирования для проведения соревнования!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
                           return;
                     } else {
                         manager.choosePanel(62);
                         stComModel.setAllData();
                         break;
                     }
                 //SPBALET
                 case(5):
                    JOptionPane.showMessageDialog(Manager.getTestCompPage(),
                    "Проведение соревнования по спортивному балету недоступно!",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
                     break;
             }        
             kindId = 0;
             sel = 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return;
        }
    }
}
