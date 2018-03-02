
package controllers.PerformanceEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Performance.PerformanceEditModel;

public class AddAthleteListener implements ActionListener {    
    private PerformanceEditModel perEditModel = 
            PerformanceEditModel.getPerformanceEditModelInstance();
    public void actionPerformed(ActionEvent e) {
        perEditModel.addAthlete(); 
    }
}
