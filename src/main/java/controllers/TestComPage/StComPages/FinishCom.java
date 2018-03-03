package controllers.TestComPage.StComPages;

import data.Competition;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.TestCom.StartCom.StComModel;
import models.TestCom.TestComModel;

public class FinishCom implements ActionListener {
    private StComModel stComModel;
    private TestComModel tcModel;
    private Competition competition;
    public void actionPerformed(ActionEvent e) {
        stComModel = StComModel.getStComModelInstance();
        tcModel = TestComModel.getTestComModelInstance();
                
            if (stComModel.checkAllValues()) {
                //competition is finished and data is not editable
                competition = tcModel.getCompetitions().get(tcModel.selRow());        
                competition.setFinish(true);
                
                stComModel.calculateResults();
                stComModel.delValuesFromComp();
                stComModel.saveResultsToDB();
                stComModel.resultWindow();
            } else {
                return;
            }
    }   
}
