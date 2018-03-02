package controllers.StartPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.Manager;

public class TestCompetitionBtnListener implements ActionListener{        
    public void actionPerformed(ActionEvent e) { 
        Manager.getManagerInstance().choosePanel(50);
    }
}
