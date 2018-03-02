package models.TestCom;

import data.Athlete;
import data.Judge;
import data.Rank;
import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import views.Manager;
import views.TestCom.SingleEditPage;

public class SingleEditModel {    
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection();     
    private TestComModel tcModel;
    private SingleEditPage singleEditPage;
    
    /*athlets take part in selected competition
    for view at list*/
    private ArrayList<Athlete> athletesByComp = new ArrayList<>();    
    //athletes without which are in a list 
    private ArrayList<Athlete> athlets = new ArrayList<>();
    
    /*judges take part in selected competition
    for view at list*/
    private ArrayList<Judge> judgesByComp = new ArrayList<>();
    //judges without which are in a list 
    private ArrayList<Judge> judges = new ArrayList<>();
    
    private ArrayList<Rank> ranks = new ArrayList<>();
            
    private static SingleEditModel singleEditModelInstace = null;  
    private SingleEditModel() {}    
    public static SingleEditModel getSingleEditModelInstance() {
        if (singleEditModelInstace == null) {
            singleEditModelInstace = new SingleEditModel();
        }
        return singleEditModelInstace;
    }
    
    //********************************ATHLETES**********************************
    /*get athletes, TAKING PART IN COMPETITION from DB
    save to array as data
    view it at list*/
    public void setAthletesList () {
        tcModel = TestComModel.getTestComModelInstance(); 
        int selRow = tcModel.selRow();
        String queryLst;        
        PreparedStatement prstLst = null;        
        ResultSet rsLst = null; 
                        
        //database lst  
        try {           
            queryLst = "SELECT DISTINCT ATHLETE.ID, " +
                            "ATHLETE.Surname, ATHLETE.Name, " +
                            "ATHLETE.Middlename " +
                        "FROM COMPETITION, COMPETITION_ATHLETE_LINK, " +
                            "ATHLETE " +
                        "WHERE COMPETITION_ATHLETE_LINK.IDcompetition = " + 
                    tcModel.getValueAt(selRow, 1) +  
                    "AND ATHLETE.ID = COMPETITION_ATHLETE_LINK.IDathlete;";
            prstLst = DBC.prepareStatement(queryLst);
            rsLst = prstLst.executeQuery(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }      
        
        //data lst
        //clear data lst
        try { 
            //get links, 
            //clear the array of lst, 
            //clear view of lst
            singleEditPage = Manager.getSingleEditPage();                   
            athletesByComp.clear();            
            singleEditPage.getAthlLstModel().clear();
            
            //lst        
            while (rsLst.next()) {            
                Athlete athlete = new Athlete();
                athlete.setId(rsLst.getInt(1));
                athlete.setName(rsLst.getString(3));
                athlete.setSurname(rsLst.getString(2));
                athlete.setMiddlename(rsLst.getString(4));                
                //do it for save data in dif arrays 
                //in dif models
                athletesByComp.add(athlete);
                singleEditPage.getAthlLstModel().addElement(athlete);
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(SingleEditModel.class.getName()).
                       log(Level.SEVERE, null, ex);
        }        
    }
    
    /*get athletes, DON'T TAKING PART IN COMPETITION from DB
    save to array as data
    view it at combobox*/
    public void setAthletesCombo() {
        tcModel = TestComModel.getTestComModelInstance(); 
        int selRow = tcModel.selRow();
        String queryCmb;
        PreparedStatement prstCmb = null;
        ResultSet rsCmb = null;
                
        //database cmb 
        try {            
            queryCmb = "SELECT DISTINCT ATHLETE.ID, ATHLETE.Surname, " +
                                    "ATHLETE.Name, ATHLETE.Middlename " +
                        "FROM ATHLETE " +
                        "WHERE NOT ATHLETE.ID = ANY(" +
                            "SELECT DISTINCT ATHLETE.ID " +
                            "FROM COMPETITION_ATHLETE_LINK, " +
                                 "ATHLETE " +
                        "WHERE COMPETITION_ATHLETE_LINK.IDcompetition = " + 
                               tcModel.getValueAt(selRow, 1) + " " +
                        "AND ATHLETE.ID = COMPETITION_ATHLETE_LINK.IDathlete);";
            System.out.println(queryCmb);
            prstCmb = DBC.prepareStatement(queryCmb);
            rsCmb = prstCmb.executeQuery(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            //get links, 
            //clear the array of cmb, 
            //clear view of cmb
            singleEditPage = Manager.getSingleEditPage();           
            athlets.clear();
            singleEditPage.getAthlCombo().removeAllItems();

            //cmb
            while (rsCmb.next()) {            
                Athlete athlete = new Athlete();
                athlete.setId(rsCmb.getInt(1));
                athlete.setName(rsCmb.getString(3));
                athlete.setSurname(rsCmb.getString(2));
                athlete.setMiddlename(rsCmb.getString(4));                              
                //do it for save data in dif arrays 
                //in dif models
                athlets.add(athlete);
                singleEditPage.getAthlCombo().addItem(athlete);
            }
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }         
    }
    
    //add athlete chosen from combobox        
    public void addAthlete() {
        singleEditPage = Manager.getSingleEditPage();
        tcModel = TestComModel.getTestComModelInstance();
        
        //selected athlete
        Athlete newAthl = (Athlete)singleEditPage.getAthlCombo().getSelectedItem();
        
        //add to list
        singleEditPage.getAthlLstModel().addElement(newAthl);
        //insert to array data
        athletesByComp.add(newAthl);
        
        //del from combo
        singleEditPage.getAthlCombo().removeItem(newAthl);        
        //del from array data of combo
        athlets.remove(newAthl);       
        
        //insert into db
        try {
           String query = "INSERT INTO COMPETITION_ATHLETE_LINK " +
                   "VALUES (" + newAthl.getId() + ", " + 
                                tcModel.getValueAt(tcModel.selRow(), 1) + ");" ;
           
           System.out.println(query);
           PreparedStatement pstmt = DBC.prepareStatement(query);
           pstmt.execute();
        } catch (SQLException ex) {
           Logger.getLogger(SfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {           
            JOptionPane.showMessageDialog(Manager.getSingleEditPage(),
                        "Список спортсменов, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }     
    }
    
    public void delAthlete() { 
        singleEditPage = Manager.getSingleEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
          if (singleEditPage.getAthlLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Перед удалением необходимо выделить спортсмена",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //selected athlete
        Athlete newAthl = (Athlete)singleEditPage.getAthlLst().getSelectedValue();
        
        //del from list
        singleEditPage.getAthlLstModel().removeElement(newAthl);
        athletesByComp.remove(newAthl);
        
        //add to combobox
        singleEditPage.getAthlCombo().addItem(newAthl);
        athlets.add(newAthl);
        
        //del from database         
        try {
           String query = "DELETE FROM COMPETITION_ATHLETE_LINK " +
                          "WHERE IDathlete = " + newAthl.getId() + " AND " + 
                                "IDcompetition = " + 
                                tcModel.getValueAt(tcModel.selRow(), 1) + ";" ;           
           System.out.println(query);
           PreparedStatement pstmt = DBC.prepareStatement(query);
           pstmt.execute();
        } catch (SQLException ex) {
           Logger.getLogger(SfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        }
    }
    
    //*********************************JUDGES***********************************
    //GET JUDGES 
    /*get judges, TAKING PART IN COMPETITION from DB
    save to array as data
    view it at list*/
    public void setJudgesList () {
        tcModel = TestComModel.getTestComModelInstance();
        singleEditPage = Manager.getSingleEditPage();
        int selRow = tcModel.selRow();
        
        String queryLst;        
        PreparedStatement prstLst = null;        
        ResultSet rsLst = null; 
                        
        //database lst  
        try {           
            queryLst = "SELECT JUDGE.ID, JUDGE.Surname, JUDGE.Name, " +
                              "JUDGE.Middlename " +
                        "FROM JUDGE, COMPETITION_JUDGE_LINK " +
                        "WHERE COMPETITION_JUDGE_LINK.IDjudge = JUDGE.id " +
                              "AND COMPETITION_JUDGE_LINK.IDcompetition = " + 
                              tcModel.getValueAt(selRow, 1) + ";";
                         
            prstLst = DBC.prepareStatement(queryLst);
            rsLst = prstLst.executeQuery(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "", ex);
        }      
        
        //data lst        
        try { 
            /*get links, clear the array of lst, 
            clear view of lst*/
            singleEditPage = Manager.getSingleEditPage();                                    
            judgesByComp.clear();            
            singleEditPage.getJudLstModel().clear();                 
            
            //lst        
            while (rsLst.next()) {            
                Judge judge = new Judge();
                judge.setId(rsLst.getInt(1));
                judge.setSurname(rsLst.getString(2));
                judge.setName(rsLst.getString(3));
                judge.setMiddlename(rsLst.getString(4));                
                //do it for save data in dif arrays 
                //in dif models
                judgesByComp.add(judge);
                singleEditPage.getJudLstModel().addElement(judge);
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(SingleEditModel.class.getName()).
                       log(Level.SEVERE, null, ex);
        }        
    }
    
    /*get judges, DON'T TAKING PART IN COMPETITION from DB
    save to array as data
    view it at combobox*/
    public void setJudgesCombo() {
        tcModel = TestComModel.getTestComModelInstance();
        int selRow = tcModel.selRow();
        
        String queryCmb;
        PreparedStatement prstCmb = null;
        ResultSet rsCmb = null;
                
        //database cmb 
        try {            
            queryCmb =  "SELECT JUDGE.ID, JUDGE.Surname, " +
                               "JUDGE.Name, JUDGE.Middlename " +
                        "FROM JUDGE " +
                        "WHERE NOT JUDGE.ID = ANY (SELECT JUDGE.ID " +
                        "FROM JUDGE, COMPETITION_JUDGE_LINK " +
                        "WHERE COMPETITION_JUDGE_LINK.IDjudge = JUDGE.id " +
                        "AND COMPETITION_JUDGE_LINK.IDcompetition = " +
                        tcModel.getValueAt(selRow, 1) + ");";
            
            prstCmb = DBC.prepareStatement(queryCmb);
            rsCmb = prstCmb.executeQuery();             
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            /*get links, clear the array of cmb, 
            clear view of cmb*/
            singleEditPage = Manager.getSingleEditPage();                                    
            singleEditPage.getJudCombo().removeAllItems();  
            judges.clear();            
            //cmb
            while (rsCmb.next()) {            
                Judge judge = new Judge();
                judge.setId(rsCmb.getInt(1));
                judge.setSurname(rsCmb.getString(2));
                judge.setName(rsCmb.getString(3));
                judge.setMiddlename(rsCmb.getString(4));                               
                //do it for save data in dif arrays 
                //in dif models
                judges.add(judge);
                singleEditPage.getJudCombo().addItem(judge);
            }              
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
    }
    
    //add athlete chosen from combobox        
    public void addJudge() {
        singleEditPage = Manager.getSingleEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
        //selected athlete
        Judge newJudge = (Judge)singleEditPage.getJudCombo().getSelectedItem();        
        
        //add to list
        singleEditPage.getJudLstModel().addElement(newJudge);
        //insert to array data
        judgesByComp.add(newJudge);
        
        //del from combo
        singleEditPage.getJudCombo().removeItem(newJudge);        
        //del from array data of combo
        judges.remove(newJudge);       
        
        //insert into db
        try {
           String query = "INSERT INTO COMPETITION_JUDGE_LINK " +
                   "VALUES (" + tcModel.getValueAt(tcModel.selRow(), 1) + ", " + 
                                newJudge.getId() + ");" ;
           System.out.println(query);
           PreparedStatement pstmt = DBC.prepareStatement(query);
           pstmt.execute();
        } catch (SQLException ex) {
           Logger.getLogger(SfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {           
            JOptionPane.showMessageDialog(Manager.getSingleEditPage(),
                        "Список судей, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }   
    }
    
    public void delJudge() { 
        singleEditPage = Manager.getSingleEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
          if (singleEditPage.getJudLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getSingleEditPage(),
                        "Перед удалением необходимо выделить судью!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //selected athlete
        Judge newJudge = (Judge)singleEditPage.getJudLst().getSelectedValue();
        
        //del from list
        singleEditPage.getJudLstModel().removeElement(newJudge);
        judgesByComp.remove(newJudge);
        
        //add to combobox
        singleEditPage.getJudCombo().addItem(newJudge);
        judges.add(newJudge);
        
        //del from database         
        try {
           String query = "DELETE FROM COMPETITION_JUDGE_LINK " +
                          "WHERE IDjudge = " + newJudge.getId() + " AND " + 
                                "IDcompetition = " + 
                                tcModel.getValueAt(tcModel.selRow(), 1) + ";" ;           
           System.out.println(query);
           PreparedStatement pstmt = DBC.prepareStatement(query);
           pstmt.execute();
        } catch (SQLException ex) {
           Logger.getLogger(SingleEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        }
    }
    
    //RANK**********************************************************************
    public void setRanksCombo() {        
        String queryCmb;
        PreparedStatement prstCmb = null;
        ResultSet rsCmb = null;
        
        try {            
            queryCmb =  "SELECT * FROM RANK;";            
            prstCmb = DBC.prepareStatement(queryCmb);
            rsCmb = prstCmb.executeQuery();  
            
            singleEditPage = Manager.getSingleEditPage(); 
            
            singleEditPage.getRankCombo().removeAllItems();  
            getRanks().clear(); 
            
            //get items
            while (rsCmb.next()) {            
                Rank rank = new Rank();
                rank.setId(rsCmb.getInt(1));
                rank.setFullName(rsCmb.getString(2));
                rank.setRequirements(rsCmb.getString(3));
                rank.setProgramStructure(rsCmb.getString(4)); 
                rank.setProgramsCount(rsCmb.getInt(5)); 
                
                getRanks().add(rank);
                singleEditPage.getRankCombo().addItem(rank);
            }                     
                singleEditPage.getRankCombo().setSelectedItem(setActiveRank());                            
                       
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }    
    }
    
    private Rank setActiveRank() {
        tcModel = TestComModel.getTestComModelInstance();
        int selRow = tcModel.selRow();        
        Integer selRankId = tcModel.getCompetitions().get(selRow).getRankId();
        
        Rank selRank = null;
        
        for (Rank rank : ranks) {            
            if (rank.getId() == selRankId) {
                selRank = rank;
            }
        }
        return selRank;
    }
    
    public void setRank() {
        singleEditPage = Manager.getSingleEditPage();
        tcModel = TestComModel.getTestComModelInstance();
        Rank selRank = (Rank) singleEditPage.getRankCombo().getSelectedItem();
        
        if (selRank == null) {
            return;
        }

        //set new rank to competition
        tcModel.getCompetitions().get(tcModel.selRow()).setRankId(selRank.getId());
            
        try {//update in db
            String query;
            PreparedStatement prst = null;
            query = "UPDATE COMPETITION SET IDrank = " + selRank.getId() + " " +
                    "WHERE COMPETITION.id = " +
                    tcModel.getCompetitions().get(tcModel.selRow()).getId() + ";";
            prst = DBC.prepareStatement(query);
            prst.execute();       
            prst.close();
        } catch (SQLException ex) {
            Logger.getLogger(SingleEditModel.class.getName()).log(Level.SEVERE, 
                   "Do not update rank of competition", ex);
        }
    }   
    
    //GETTERS*******************************************************************
    public ArrayList<Athlete> getAthletesByComp() {
        
        return athletesByComp;
    }
    public ArrayList<Athlete> getAthlets() {
        return athlets;
    }    

    public ArrayList<Rank> getRanks() {
        return ranks;
    }
}
 