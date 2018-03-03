package controllers.TestComEditPages.SfpEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.SfpEditModel;

public class AddAthlete implements ActionListener {
    SfpEditModel sfpEditModel;
    public void actionPerformed(ActionEvent e) {
        sfpEditModel = SfpEditModel.getSfpEditModelInstance();
        sfpEditModel.addAthlete();       
    }   
}
