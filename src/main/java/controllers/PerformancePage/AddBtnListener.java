package controllers.PerformancePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Performance.PerformanceModel;
import views.Manager;

public class AddBtnListener implements ActionListener { 
    private Manager manager = Manager.getManagerInstance();
    private PerformanceModel perModel = PerformanceModel.
                                        getPerformanceModelInstance();          
    public void actionPerformed(ActionEvent e) {                
        if (manager.getPerPage()!= null) {
            perModel.addPerformance();
        } 
    }
}

