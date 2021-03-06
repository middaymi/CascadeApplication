package controllers.TestComPage.SingleComPage;

import data.*;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SetAthlete implements ActionListener {

    SingleStComPage singleComPage;
    IsuComModel isuComModel;
    Manager manager;

    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        singleComPage = manager.getSingleComPage();
        isuComModel = IsuComModel.getModelInstance();

        if (isuComModel.isDoNothingWithListenersFlagUp()) {
            return;
        }

        //athlete is selected
        if (singleComPage.getAthlCmb().getSelectedItem() != null) {
            isuComModel.upDoNothingWithListenersFlag();

            // clear gui
            isuComModel.clearElementAndComponentsRow();

            isuComModel.setMode(0);

            //get selected athlete
            Athlete athlete = (Athlete) (singleComPage.getAthlCmb().getSelectedItem());
            HashMap<Integer, CompetitionIsuAthleteResult> CIARS = isuComModel.getCIARS();
            isuComModel.setCIAR(CIARS.get(athlete.getId()));

            if (CIARS.get(athlete.getId()).getStartNumber() == 0) {
                CIARS.get(athlete.getId()).setStartNumber(isuComModel.getStartNumber());
            }

            singleComPage.setStartTF(String.valueOf(CIARS.get(athlete.getId()).getStartNumber()));
            singleComPage.getStartTF().setEditable(false);

            //components
            //set new ComponentIsu for each Component
            isuComModel.getCIARS().get(athlete.getId()).getComponentsList().clear();
            for (ComponentRow row : singleComPage.getCompRows()) {
                ComponentIsu compIsu = row.getComponentIsu();
                compIsu.getJudgesValues().clear();
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
            isuComModel.getCIARsFromDB(athlete);
            CIARtoFront(CIARS.get(athlete.getId()));

            //ELEMENTS
            isuComModel.getElementsResultFromDB(athlete.getId());
            for (int i = 0; i < CIARS.get(athlete.getId()).getElementsList().size(); i++) {
                singleComPage.addElementRow(i, CIARS.get(athlete.getId()).getElementsList().get(i));
            }

            //CHECK FOR FINISH
            boolean activateEditBtns = true;
            //if finished
            if (isuComModel.isFinishedCompetitionForAthlete(athlete.getId())) {
                activateEditBtns = false;
                isuComModel.setMode(1);
                singleComPage.setEditableTopPnl(activateEditBtns);

                //to do not editable components
                for (ComponentRow row : singleComPage.getCompRows()) {
                    for (JTextField mark : row.getJudgeMarks()) {
                        mark.setEditable(activateEditBtns);
                    }
                }

                //to do not editable elements
                for (ElementRow row : singleComPage.getElRows()) {
                    row.setEnabledElRowComponents(activateEditBtns);
                }

                //SCORES
                CIARS.get(athlete.getId()).calculate(isuComModel.getFactor());
                //enter scores to fields
                int index = 0;
                for (ElementIsu elData : CIARS.get(athlete.getId()).getElementsList()) {
                    ElementRow elRow = singleComPage.getElRows().get(index++);
                    elRow.setScoreText(String.valueOf(elData.getScores()));
                }
                index = 0;
                for (ComponentIsu compData : CIARS.get(athlete.getId()).getComponentsList()) {
                    ComponentRow compRow = singleComPage.getCompRows().get(index++);
                    compRow.setScoreText(String.valueOf(compData.getScores()));
                }

            }

            //add new row if don't have any rows from db
            if (CIARS.get(athlete.getId()).getElementsList().size() == 0) {
                singleComPage.addElementRow();
            }

            singleComPage.enableAddElemBtn(activateEditBtns);
            singleComPage.enableFinBtn(activateEditBtns);

            //set disabled radioBtns points/marks
            singleComPage.setEnabledPoints(false);
            singleComPage.setEnabledMarks(false);
            singleComPage.setSelectedRadioBtn(singleComPage.getMarks());

            isuComModel.downDoNothingWithListenersFlag();
        }
    }

    private void CIARtoFront(CompetitionIsuAthleteResult ciar) {
        String[] texts = {
                String.valueOf(ciar.getStartNumber()).equals("0.0") ? "" : String.valueOf(ciar.getStartNumber()),
                String.valueOf(ciar.getTotalScore()).equals("0.0") ? "" : String.valueOf(ciar.getTotalScore()),
                String.valueOf(ciar.getElementScore()).equals("0.0") ? "" : String.valueOf(ciar.getElementScore()),
                String.valueOf(ciar.getComponentScore()).equals("0.0") ? "" : String.valueOf(ciar.getComponentScore()),
                String.valueOf(ciar.getDeductions()).equals("0.0") ? "" : String.valueOf(ciar.getDeductions())
        };
        singleComPage.setReusltsToTopPnl(texts);
    }
}