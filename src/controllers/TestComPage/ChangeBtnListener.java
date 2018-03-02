package controllers.TestComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.TestComModel;
import views.Manager;

public class ChangeBtnListener implements ActionListener {
    private TestComModel tcModel;
    private Manager manager;
    private boolean change = false;
    
    public void actionPerformed(ActionEvent e) {        
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();
        if (manager.getTestCompPage() != null) {
            if (change == true) {
                manager.getTestCompPage().setBtnsMode(false);                
                change = false;
                //tcModel.delEmptyRows();
            } else {
            manager.getTestCompPage().setBtnsMode(true);
            change = true;
            }
        }
    }
}
