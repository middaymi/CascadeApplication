package controllers.AthletePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Athlete.AthleteModel;
import views.Manager;

public class AddBtnListener implements ActionListener{
    Manager manager = Manager.getManagerInstance();
    private AthleteModel athModel = AthleteModel.getAthleteModelInstance(); 
    public void actionPerformed(ActionEvent e) {                
        if (manager.getAthPage() != null) {
            athModel.addAthlete();
        } 
    }    
}
