package controllers.TestComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.TestComModel;

public class AddBtnListener implements ActionListener{
    TestComModel tcModel;
    public void actionPerformed(ActionEvent e) {
        tcModel = TestComModel.getTestComModelInstance();
        tcModel.addCompetition();        
    }    
}
