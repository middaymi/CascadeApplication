package controllers.TestComEditPages.OfpEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.OfpEditModel;

public class DelAthlete implements ActionListener {
    OfpEditModel ofpEditModel;
    public void actionPerformed(ActionEvent e) {
        ofpEditModel = OfpEditModel.getOfpEditModelInstance();
        ofpEditModel.delAthlete();
    }   
}
