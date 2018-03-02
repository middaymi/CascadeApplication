package controllers.PerformancePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Performance.PerformanceEditModel;
import models.Performance.PerformanceModel;
import views.Manager;

public class EditBtnListener implements ActionListener {   
    private Manager manager = Manager.getManagerInstance();
    private PerformanceModel perModel = 
            PerformanceModel.getPerformanceModelInstance();
    private PerformanceEditModel perEditModel = 
            PerformanceEditModel.getPerformanceEditModelInstance();
    public void actionPerformed(ActionEvent e) {
        if (perModel.selRow() == -1) {
            return;
        } else {
            manager.choosePanel(41);
            perEditModel.updateInfo();            
        }
    }
}
    
