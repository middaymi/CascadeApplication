package controllers.TestComPage;

import data.Athlete;
import data.Competition;
import data.SportsmanResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.TestCom.StartCom.IsuComModel;
import models.TestCom.TestComModel;
import views.Manager;
import views.TestCom.ProtocolPage;
   
public class ViewProtocol implements ActionListener{
    Manager manager = null;
    TestComModel tcModel = null;
    IsuComModel isuComModel = null;
    Competition competition = null;
    ProtocolPage protocolPage = null;
    private ArrayList<SportsmanResult>results = new ArrayList<>();
    @Override
    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();
        isuComModel = IsuComModel.getModelInstance();
        
        
        if (tcModel.selRow() != -1) {
            competition = tcModel.getCompetitions().get(tcModel.selRow());
            Manager.choosePanel(55);
            protocolPage = Manager.getProtocolPage();
            
            //clear data and view
            results.clear();
            protocolPage.getPnl().removeAll();
            
            //get results
            getResults(competition.getId(), competition.getKind().getId());            
            
            //hook array of results
            protocolPage.setSr(results);
            
            protocolPage.createResult(results, competition.getKind().getId());               
            
        } else {return;}       
    }
  
    private void getResults(int ID, int kindId) {
        PreparedStatement prst = null;
        ResultSet rs = null;
        String query;
                       
        try {    
            query = "SELECT ATHLETE.Surname, ATHLETE.Name, Athlete.MiddleName, " +
                    "RESULT.*" +
                    "FROM ATHLETE, RESULT " +
                    "WHERE ATHLETE.ID = RESULT.IDathlete AND " +
                    "RESULT.IDcompetition = " + ID + " " +
                    "ORDER BY SumResult DESC;";
            prst = isuComModel.getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            
            while (rs.next()) {
                Athlete athlete = new Athlete();                
                athlete.setSurname(rs.getString(1));
                athlete.setName(rs.getString(2));
                athlete.setMiddlename(rs.getString(3));
                athlete.setId(rs.getInt(6));
                SportsmanResult athleteResult = new SportsmanResult(athlete);

                athleteResult.setPlace(rs.getInt(7));
                athleteResult.setSumOfMarks(rs.getFloat(8));
                athleteResult.setIsDone(rs.getBoolean(9));
                athleteResult.setStartNumber(rs.getInt(10));
                athleteResult.setSumOfAllElements(rs.getFloat(11));
                athleteResult.setSumOfAllComponents(rs.getFloat(12));
                athleteResult.setDeductions(rs.getFloat(13)); 
                athleteResult.setSumOfRanks(rs.getInt(14));
                athleteResult.setAverageMark(rs.getFloat(15));
                results.add(athleteResult);                           
            }
            if (kindId == 4) {
                
                ArrayList<Float> marks = new ArrayList<>();            
                for (int j = 0; j < results.size(); j++) {
                    marks.add(results.get(j).getSumOfMarks());
                }

                Collections.sort(marks);  
                Collections.reverse(marks);

                for (int j = 0; j < results.size(); j++) {                    
                    int place = marks.indexOf(results.get(j).getSumOfMarks()) + 1;
                    //a place by every element done 
                    results.get(j).setPlace(place);                   
                }                                                                      
            }             
            Collections.sort(results);            
            
        } catch (SQLException ex) {
            Logger.getLogger(ViewProtocol.class.getName()).
                   log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                prst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ViewProtocol.class.getName()).
                       log(Level.SEVERE, null, ex);
            }
        }
    }
}
