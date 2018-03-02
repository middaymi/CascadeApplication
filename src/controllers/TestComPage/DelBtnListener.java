package controllers.TestComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.TestComModel;
import views.Manager;

public class DelBtnListener implements ActionListener {
    private Manager manager;
    private TestComModel tcModel;
    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();
        if (manager.getTestCompPage() != null) {
            tcModel.delSelectedRow();
        } 
    }    
}
