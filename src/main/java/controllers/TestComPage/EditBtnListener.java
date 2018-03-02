/*
ID:
1 = SFP = 53
2 = OFP = 52
3 = GLASIAL = 51
4 = SINGLE = 54
*/
package controllers.TestComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.TestCom.GlasialEditModel;
import models.TestCom.OfpEditModel;
import models.TestCom.SfpEditModel;
import models.TestCom.SingleEditModel;
import models.TestCom.TestComModel;
import views.Manager;

public class EditBtnListener implements ActionListener{
    private Manager manager;
    private TestComModel tcModel;
    private SfpEditModel sfpEditModel;
    private OfpEditModel ofpEditModel;
    private GlasialEditModel glasialEditModel;    
    private SingleEditModel singleEditModel;
    int sel;
    int kindId;
    
    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();   
        sfpEditModel = SfpEditModel.getSfpEditModelInstance();
        ofpEditModel = OfpEditModel.getOfpEditModelInstance();
        glasialEditModel = GlasialEditModel.getGlasialEditModelInstance();
        singleEditModel = SingleEditModel.getSingleEditModelInstance(); 
        
        sel = tcModel.selRow();
        if (sel == -1) return;
        
        //if not finished
        if (!tcModel.getCompetitions().get(sel).isFinished()) {                    
            try {                      
                kindId = (int)tcModel.getValueAt(sel, 2);         

                switch(kindId) {
                    case(1):
                        manager.choosePanel(53);                
                        sfpEditModel.setAthletesList();
                        sfpEditModel.setAthletesCombo();
                        sfpEditModel.setJudgesList();
                        sfpEditModel.setJudgesCombo();
                        sfpEditModel.setElementsList();
                        sfpEditModel.setElementsCombo();
                        break;
                    case(2):
                        manager.choosePanel(52);
                        ofpEditModel.setAthletesList();
                        ofpEditModel.setAthletesCombo();
                        ofpEditModel.setJudgesList();
                        ofpEditModel.setJudgesCombo();
                        ofpEditModel.setElementsList();
                        ofpEditModel.setElementsCombo();
                        break;
                    case(3):
                        manager.choosePanel(51);
                        glasialEditModel.setAthletesList();
                        glasialEditModel.setAthletesCombo();
                        glasialEditModel.setJudgesList();
                        glasialEditModel.setJudgesCombo();
                        glasialEditModel.setElementsList();
                        glasialEditModel.setElementsCombo();                
                        break;
                    case(4): 
                        manager.choosePanel(54);
                        singleEditModel.setAthletesList();
                        singleEditModel.setAthletesCombo();
                        singleEditModel.setJudgesList();
                        singleEditModel.setJudgesCombo();
                        singleEditModel.setRanksCombo();
                        break;
                    case(5):
                        JOptionPane.showMessageDialog(Manager.getTestCompPage(),
                                "Информация по спортивным балтам доступна " +
                                "для редактирования только в таблице!",
                                "Внимание!", JOptionPane.WARNING_MESSAGE);
                        break;
                }        
                kindId = 0;
                sel = 0;
            } catch (ArrayIndexOutOfBoundsException ex) {
                    return;
            }
        } else {
            JOptionPane.showMessageDialog(Manager.getTestCompPage(),
                                "Соревнование проведено и  " +
                                "данные уже невозможно изменить!\n" +
                                "Для просмотра параметров соревнования, " +
                                "откройте его протокол.",
                                "Ошибка!", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
}
