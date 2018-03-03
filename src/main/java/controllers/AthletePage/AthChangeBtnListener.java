package controllers.AthletePage;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Athlete.AthleteModel;
import views.Manager;

public class AthChangeBtnListener implements ActionListener{ 
    Manager manager = Manager.getManagerInstance();
    private AthleteModel athModel = AthleteModel.getAthleteModelInstance();   
    boolean change = false;
    public void actionPerformed(ActionEvent e) {        
        if (manager.getAthPage() != null) {
            if (change == true) {
                manager.getAthPage().setBtnsMode(false);                
                change = false;
                athModel.delEmptyRows();                
            } else {
            manager.getAthPage().setBtnsMode(true);
            change = true;
            }
        }       
    }
}
