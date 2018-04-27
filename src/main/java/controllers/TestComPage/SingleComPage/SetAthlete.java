package controllers.TestComPage.SingleComPage;

import data.*;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SetAthlete implements ActionListener {

    SingleStComPage singleComPage;
    IsuComModel isuComModel;
    Manager manager;
    int startNumber = 0;

    public void actionPerformed(ActionEvent e) {
        isuComModel.upDoNothingWithListenersFlag();

        manager = Manager.getManagerInstance();
        singleComPage = manager.getSingleComPage();
        isuComModel = IsuComModel.getModelInstance();

        //athlete is selected
        if (singleComPage.getAthlCmb().getSelectedItem() != null) {

            // clear gui
            isuComModel.clearElementAndComponentsRow();

            //get selected athlete
            Athlete athlete = (Athlete) (singleComPage.getAthlCmb().getSelectedItem());
            HashMap<Integer, CompetitionIsuAthleteResult> CIARS = isuComModel.getCIARS();
            //isuComModel.setCIAR(CIARS.get(athlete.getId()));

            // start number
            if (CIARS.get(athlete.getId()).getStartNumber() == 0) {
                CIARS.get(athlete.getId()).setStartNumber(++startNumber);
            }
            singleComPage.setStartTF(String.valueOf(CIARS.get(athlete.getId()).getStartNumber()));
            singleComPage.getStartTF().setEditable(false);

            //components
            //set new ComponentIsu for each Component
            for (ComponentRow row : singleComPage.getCompRows()) {
                ComponentIsu compIsu = row.getComponentIsu();
                for (Judge judge : isuComModel.getJudgesByComp()) {
                    ComponentValue compVal = new ComponentValue();
                    compIsu.getJudgesValues().put(judge.getId(), compVal);
                }
                //add to CIAR component list
                isuComModel.getCIARS().get(athlete.getId()).addToCompLst(compIsu);
            }

            //CHECK COMPONENTS IN DB
            isuComModel.getComponentsResultFromDB(athlete.getId());
            isuComModel.setComponentResultsToFields();

            // CHECK RESULT AND VIEW IF IS
            isuComModel.getCIARsFromDB();
            CIARtoFront(CIARS.get(athlete.getId()));


            //ELEMENTS
//            isuComModel.getElementsResultFromDB(athlete.getId());
//            for (int i = 0; i < CIAR.getElementsList().size(); i++) {
//                singleComPage.addElementRow(i, CIAR.getElementsList().get(i));
//            }
            //add new element-row
            //singleComPage.addElementRow();


            singleComPage.enableAddElemBtn(true);
            singleComPage.enableFinBtn(true);

            //set disabled radioBtns points/marks
            singleComPage.setEnabledPoints(false);
            singleComPage.setEnabledMarks(false);
            singleComPage.setSelectedRadioBtn(singleComPage.getMarks());
            //singleComPage.setEditableTopPnl(true)

            isuComModel.downDoNothingWithListenersFlag();
        }
    }

    private void CIARtoFront(CompetitionIsuAthleteResult ciar) {
        String[] texts = {String.valueOf(ciar.getStartNumber()),
                String.valueOf(ciar.getTotalScore()),
                String.valueOf(ciar.getElementScore()),
                String.valueOf(ciar.getComponentScore()),
                String.valueOf(ciar.getDeductions())};
        singleComPage.setReusltsToTopPnl(texts);
    }
}

