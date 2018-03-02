package controllers.TestComEditPages.SfpEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.SfpEditModel;

public class DelElement implements ActionListener {
    SfpEditModel sfpEditModel;
    public void actionPerformed(ActionEvent e) {
        sfpEditModel = SfpEditModel.getSfpEditModelInstance();
        sfpEditModel.delElement();       
    }   
}
