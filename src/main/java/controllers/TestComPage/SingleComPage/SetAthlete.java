package controllers.TestComPage.SingleComPage;

import data.*;
import models.TestCom.StartCom.IsuComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetAthlete implements ActionListener {

    SingleStComPage singleComPage;
    IsuComModel isuComModel;
    Manager manager;
    int startNumber = 0;

    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        singleComPage = manager.getSingleComPage();
        isuComModel = IsuComModel.getModelInstance();

        //athlete is selected
        if (singleComPage.getAthlCmb().getSelectedItem() != null) {
            Athlete athlete = (Athlete) (singleComPage.getAthlCmb().getSelectedItem());

            //competition is finished for Athlete
            if (isuComModel.isFinishedCompetitionForAthlete(athlete.getId())) {

                isuComModel.clearResults();

                //create new data
                isuComModel.getCIARsFromDB();
                CompetitionIsuAthleteResult CIAR = isuComModel.getCIAR(athlete.getId());
                CIARtoFront(CIAR);
                isuComModel.setCIAR(CIAR);

                //allcommon data was downloaded when was a competition checkout

                isuComModel.getComponentsResultFromDB();
                isuComModel.setComponentResultsToFields();
//                isuComModel.getElementsResultFromDB(athlete.getId());
                return;
            }


            //new for save
            //isuComModel.setFinished(false);
            singleComPage.enableAddElemBtn(true);
            singleComPage.enableFinBtn(true);

            //set disabled radioBtns points/marks
            singleComPage.setEnabledPoints(false);
            singleComPage.setEnabledMarks(false);
            singleComPage.setSelectedRadioBtn(singleComPage.getMarks());
            //singleComPage.setEditableTopPnl(true);

            //create new data
            CompetitionIsuAthleteResult CIAR = new CompetitionIsuAthleteResult(athlete);

            //set satrtNumber to field and data object
            ++startNumber;
            singleComPage.setStartTF(String.valueOf(startNumber));
            singleComPage.getStartTF().setEditable(false);
            CIAR.setStartNumber(startNumber);

            //set competition_performance_athlete_link
            CIAR.setCompetitionAthlId(getCompetitionAthlId(athlete));

            //save main object by selected athlete to isuModel class
            isuComModel.setCIAR(CIAR);

            //set new ComponentIsu for each Component
            for (ComponentRow row : singleComPage.getCompRows()) {
                ComponentIsu compIsu = new ComponentIsu();
                //set ComponentValue for each row fpr each judge
                for (Judge judge : isuComModel.getJudgesByComp()) {
                    ComponentValue compVal = new ComponentValue();
                    compIsu.getJudgesValues().put(judge.getId(), compVal);
                }
                //add componentIsu to ComponentRow
                row.setComponentIsu(compIsu);
                //add to CIAR component list
                isuComModel.getCIAR().addToCompLst(compIsu);
            }

            //add new element-row
            singleComPage.addElementRow();

        } else {
            return;
        }
    }

    private int getCompetitionAthlId(Athlete athlete) {
        PreparedStatement prst = null;
        ResultSet rs = null;
        int id = 0;

        try {
            String str = "SELECT ID FROM COMPETITION_PERFORMANCE_ATHLETE_LINK " +
                    "WHERE IDathlete = " + athlete.getId() + " AND " +
                    "IDcompetition = " + isuComModel.getCompetition().getId() + ";";
            String str1 = "INSERT INTO COMPETITION_PERFORMANCE_ATHLETE_LINK VALUES (" +
                    isuComModel.getCompetition().getId() + ", " + athlete.getId() +
                    ", null);";

            prst = isuComModel.getDBC().prepareStatement(str);
            rs = prst.executeQuery();

            if (!rs.next()) {
                prst = isuComModel.getDBC().prepareStatement(str1);
                prst.executeUpdate();

                prst = isuComModel.getDBC().prepareStatement(str);
                rs = prst.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } else {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SetAthlete.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                prst.close();
            } catch (SQLException ex) {
                Logger.getLogger(SetAthlete.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return id;
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

