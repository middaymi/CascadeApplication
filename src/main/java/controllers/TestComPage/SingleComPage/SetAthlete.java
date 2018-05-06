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
    int startNumber;

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

            //get selected athlete
            Athlete athlete = (Athlete) (singleComPage.getAthlCmb().getSelectedItem());
            HashMap<Integer, CompetitionIsuAthleteResult> CIARS = isuComModel.getCIARS();
            isuComModel.setCIAR(CIARS.get(athlete.getId()));

            // start number
            if (CIARS.get(athlete.getId()).getStartNumber() == 0) {
                CIARS.get(athlete.getId()).setStartNumber(getStartNumber());
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
            isuComModel.getElementsResultFromDB(athlete.getId());
            for (int i = 0; i < CIARS.get(athlete.getId()).getElementsList().size(); i++) {
                singleComPage.addElementRow(i, CIARS.get(athlete.getId()).getElementsList().get(i));
            }

            //CHECK FOR FINISH
            boolean activateEditBtns = true;
            //if finished
            if (isuComModel.isFinishedCompetitionForAthlete(athlete.getId())) {
                activateEditBtns = false;

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

            } else {
                //add new empty element-row
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

    private int getStartNumber() {
        int currentLastStartNumber = isuComModel.getCIARS().values().stream()
                .map(athlete -> athlete.getStartNumber())
                .sorted((o1, o2) -> -o1.compareTo(o2))
                .findFirst()
                .orElse(0);
        return currentLastStartNumber + 1;
    }
}