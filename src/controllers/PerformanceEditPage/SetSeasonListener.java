package controllers.PerformanceEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Performance.PerformanceEditModel;
import views.Manager;

public class SetSeasonListener implements ActionListener { 
    private Manager manager = Manager.getManagerInstance();
    private PerformanceEditModel perEditModel = 
            PerformanceEditModel.getPerformanceEditModelInstance();    
    public void actionPerformed(ActionEvent e) {      
        perEditModel.changeSeason();   
    }
}
