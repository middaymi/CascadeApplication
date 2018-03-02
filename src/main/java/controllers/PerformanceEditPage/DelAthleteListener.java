package controllers.PerformanceEditPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import models.Performance.PerformanceEditModel;
import views.Manager;
import views.Performance.PerformanceEditPage;

public class DelAthleteListener implements ActionListener {
    private PerformanceEditModel perEditModel = 
            PerformanceEditModel.getPerformanceEditModelInstance();
    private PerformanceEditPage perEditPage;            
    public void actionPerformed(ActionEvent e) {
        perEditPage = Manager.getManagerInstance().getPerEditPage();
        perEditModel.delAthlete();
        
        perEditPage.getList().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (perEditPage.getList().getSelectedIndex() >= 0) {
                    perEditPage.getDelAthleteBtn().setEnabled(true);
                } else {
                    perEditPage.getDelAthleteBtn().setEnabled(false);
                }
            }
       });
    }    
}
