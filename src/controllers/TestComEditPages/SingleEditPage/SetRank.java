package controllers.TestComEditPages.SingleEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.SingleEditModel;

public class SetRank implements ActionListener {    
    SingleEditModel singleEditModel;    
    public void actionPerformed(ActionEvent e) {
        singleEditModel = singleEditModel.getSingleEditModelInstance();
        singleEditModel.setRank();
    }    
}
