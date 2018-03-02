package controllers.CommonButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.Manager;

public class BackBtnListener implements ActionListener{
    private Manager manager;    
    public void actionPerformed(ActionEvent e) { 
        manager = Manager.getManagerInstance();
        switch(manager.chosenPage()) {
        case(41):
            manager.choosePanel(40);
            break;
        case(51):
        case(52):
        case(53):
        case(54):
        case(55):
        case(61): 
        case(62): 
            manager.choosePanel(50);
            break;
        }
    }
}
