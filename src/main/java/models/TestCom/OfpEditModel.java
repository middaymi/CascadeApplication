package models.TestCom;

import data.Athlete;
import data.Element;
import data.Judge;
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
import views.TestCom.OfpEditPage;

public class OfpEditModel {  
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection();  
    private TestComModel tcModel;
    private OfpEditPage ofpEditPage;
    
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
    
    /*elements selected competition
    for view at list*/
    private ArrayList<Element> elementsByComp = new ArrayList<>();
    //judges without which are in a list 
    private ArrayList<Element> elements = new ArrayList<>();
      
    private static OfpEditModel ofpEditModelInstace = null;  
    private OfpEditModel() {}    
    public static OfpEditModel getOfpEditModelInstance() {
        if (ofpEditModelInstace == null) {
            ofpEditModelInstace = new OfpEditModel();
        }
        return ofpEditModelInstace;
    }
       
    //**********************************ATHLETES********************************
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
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }      
        
        //data lst
        //clear data lst
        try { 
            //get links, 
            //clear the array of lst, 
            //clear view of lst            
            ofpEditPage = Manager.getOfpEditPage();            
            athletesByComp.clear();            
            ofpEditPage.getAthlLstModel().clear();
                    
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
                ofpEditPage.getAthlLstModel().addElement(athlete);                                
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(OfpEditModel.class.getName()).
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
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            //get links, 
            //clear the array of cmb, 
            //clear view of cmb
            ofpEditPage = Manager.getOfpEditPage();                   
            athlets.clear();
            ofpEditPage.getAthlCombo().removeAllItems();
                    
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
                ofpEditPage.getAthlCombo().addItem(athlete);
            }
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }         
    }

    //add athlete chosen from combobox        
    public void addAthlete() {
        ofpEditPage = Manager.getOfpEditPage();
        tcModel = TestComModel.getTestComModelInstance();
        
        //selected athlete
        Athlete newAthl = (Athlete)ofpEditPage.getAthlCombo().getSelectedItem();
        
        //add to list
        ofpEditPage.getAthlLstModel().addElement(newAthl);
        //insert to array data
        athletesByComp.add(newAthl);
        
        //del from combo
        ofpEditPage.getAthlCombo().removeItem(newAthl);        
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
            JOptionPane.showMessageDialog(Manager.getOfpEditPage(),
                        "Список спортсменов, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }   
    }
    
    public void delAthlete() { 
        ofpEditPage = Manager.getOfpEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
          if (ofpEditPage.getAthlLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getOfpEditPage(),
                        "Перед удалением необходимо выделить спортсмена",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //selected athlete
        Athlete newAthl = (Athlete)ofpEditPage.getAthlLst().getSelectedValue();
        
        //del from list
        ofpEditPage.getAthlLstModel().removeElement(newAthl);
        athletesByComp.remove(newAthl);
        
        //add to combobox
        ofpEditPage.getAthlCombo().addItem(newAthl);
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
        ofpEditPage = Manager.getOfpEditPage();
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
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "", ex);
        }      
        
        //data lst        
        try { 
            /*get links, clear the array of lst, 
            clear view of lst*/
            ofpEditPage = Manager.getOfpEditPage();                                    
            judgesByComp.clear();            
            ofpEditPage.getJudLstModel().clear();                 
            
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
                ofpEditPage.getJudLstModel().addElement(judge);
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(OfpEditModel.class.getName()).
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
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            /*get links, clear the array of cmb, 
            clear view of cmb*/
            ofpEditPage = Manager.getOfpEditPage();                                    
            ofpEditPage.getJudCombo().removeAllItems();  
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
                ofpEditPage.getJudCombo().addItem(judge);
            }              
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
    }
    
    //add athlete chosen from combobox        
    public void addJudge() {
        ofpEditPage = Manager.getOfpEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
        //selected athlete
        Judge newJudge = (Judge)ofpEditPage.getJudCombo().getSelectedItem();        
        
        //add to list
        ofpEditPage.getJudLstModel().addElement(newJudge);
        //insert to array data
        judgesByComp.add(newJudge);
        
        //del from combo
        ofpEditPage.getJudCombo().removeItem(newJudge);        
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
           Logger.getLogger(OfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {           
            JOptionPane.showMessageDialog(Manager.getOfpEditPage(),
                        "Список судей, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }   
    }
    
    public void delJudge() { 
        ofpEditPage = Manager.getOfpEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
          if (ofpEditPage.getJudLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getOfpEditPage(),
                        "Перед удалением необходимо выделить судью!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //selected athlete
        Judge newJudge = (Judge)ofpEditPage.getJudLst().getSelectedValue();
        
        //del from list
        ofpEditPage.getJudLstModel().removeElement(newJudge);
        judgesByComp.remove(newJudge);
        
        //add to combobox
        ofpEditPage.getJudCombo().addItem(newJudge);
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
           Logger.getLogger(OfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        }
    }
    
    //*******************************ELEMENTS***********************************
    //GET ELEMENTS 
    /*get elements in COMPETITION from DB
    save to array as data
    view it at list*/
    public void setElementsList () {
        tcModel = TestComModel.getTestComModelInstance();
        ofpEditPage = Manager.getOfpEditPage();
        int selRow = tcModel.selRow();
        
        String queryLst;        
        PreparedStatement prstLst = null;        
        ResultSet rsLst = null; 
                        
        //database lst  
        try {           
            queryLst = "SELECT OFP_SPECIFICATION. * " +
                        "FROM OFP_SPECIFICATION, TESTS_ELEMENTS_LINK " +
                        "WHERE TESTS_ELEMENTS_LINK.IDisuElement = OFP_SPECIFICATION.ID " +
                        "AND TESTS_ELEMENTS_LINK.IDcompetition = " +
                        tcModel.getValueAt(selRow, 1) + ";";
            System.out.println(queryLst);
            prstLst = DBC.prepareStatement(queryLst);
            rsLst = prstLst.executeQuery();            
        } catch (SQLException ex) {
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "Not get  elements for list", ex);
        }      
        
        //data lst        
        try { 
            /*get links, clear the array of lst, 
            clear view of lst*/
            ofpEditPage = Manager.getOfpEditPage();                                    
            //elementsByComp.clear();            
            ofpEditPage.getElLstModel().clear();                 
            
            //lst        
            while (rsLst.next()) {            
                Element element = new Element();
                element.setId(rsLst.getInt(1));
                element.setUnits(rsLst.getString(2));
                element.setDescription(rsLst.getString(3));
                element.setFullName(rsLst.getString(4));               
                //do it for save data in dif arrays 
                //in dif models
                elementsByComp.add(element);
                ofpEditPage.getElLstModel().addElement(element);                
                System.out.println(element);
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(SfpEditModel.class.getName()).
                       log(Level.SEVERE, null, ex);
        }        
    }
    
    /*get elements, not in competition from DB
    save to array as data
    view it at combobox*/
    public void setElementsCombo() {
        tcModel = TestComModel.getTestComModelInstance();
        int selRow = tcModel.selRow();
        
        String queryCmb;
        PreparedStatement prstCmb = null;
        ResultSet rsCmb = null;
                
        //database  
        try {            
            queryCmb =  "SELECT OFP_SPECIFICATION. * " +
                        "FROM OFP_SPECIFICATION " +
                        "WHERE NOT OFP_SPECIFICATION.ID = ANY( " +
                            "SELECT OFP_SPECIFICATION.ID " +
                            "FROM OFP_SPECIFICATION, TESTS_ELEMENTS_LINK " +
                            "WHERE TESTS_ELEMENTS_LINK.IDcompetition = " + 
                                   tcModel.getValueAt(selRow, 1) + " " +
                            "AND TESTS_ELEMENTS_LINK.IDisuElement = OFP_SPECIFICATION.ID);";
            
            prstCmb = DBC.prepareStatement(queryCmb);
            rsCmb = prstCmb.executeQuery();             
        } catch (SQLException ex) {
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            /*get links, clear the array of cmb, 
            clear view of cmb*/
            ofpEditPage = Manager.getOfpEditPage();                                    
            ofpEditPage.getElCombo().removeAllItems();  
            elements.clear();            
            //cmb
            while (rsCmb.next()) {            
                Element element = new Element();
                element.setId(rsCmb.getInt(1));
                element.setUnits(rsCmb.getString(2));
                element.setDescription(rsCmb.getString(3));
                element.setFullName(rsCmb.getString(4));               
                //do it for save data in dif arrays 
                //in dif models
                elements.add(element);
                ofpEditPage.getElCombo().addItem(element);
            }              
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(OfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
    }
 
    //add element chosen from combobox        
    public void addElement() {
        ofpEditPage = Manager.getOfpEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
        //selected judge
        Element newElement = (Element)ofpEditPage.getElCombo().getSelectedItem();        
        
        //add to list
        ofpEditPage.getElLstModel().addElement(newElement);
        //insert to array data
        elementsByComp.add(newElement);
        
        //del from combo
        ofpEditPage.getElCombo().removeItem(newElement);        
        //del from array data of combo
        elements.remove(newElement);       
        
        //insert into db
        try {
           String query = "INSERT INTO TESTS_ELEMENTS_LINK VALUES (" +
                   tcModel.getValueAt(tcModel.selRow(), 1) + ", " +
                   newElement.getId() + ")";
           
           System.out.println(query);
           PreparedStatement pstmt = DBC.prepareStatement(query);
           pstmt.execute();
        } catch (SQLException ex) {
           Logger.getLogger(OfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {           
            JOptionPane.showMessageDialog(Manager.getOfpEditPage(),
                        "Список элементов, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }   
    }
    
    public void delElement() { 
        ofpEditPage = Manager.getOfpEditPage();
        tcModel = TestComModel.getTestComModelInstance(); 

       if (ofpEditPage.getElLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getOfpEditPage(),
                        "Перед удалением необходимо выделить элемент!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //selected athlete
        Element newElement = (Element)ofpEditPage.getElLst().getSelectedValue();

        //del from list
        ofpEditPage.getElLstModel().removeElement(newElement);
        elementsByComp.remove(newElement);

        //add to combobox
        ofpEditPage.getElCombo().addItem(newElement);
        elements.add(newElement);

        //del from database         
        try {
           String query =  "DELETE FROM TESTS_ELEMENTS_LINK " +
                           "WHERE IDcompetition =  " +
                            tcModel.getValueAt(tcModel.selRow(), 1) + " AND " +
                            "IDisuElement = " + newElement.getId() + ";";
           System.out.println(query);
           PreparedStatement pstmt = DBC.prepareStatement(query);
           pstmt.execute();
        } catch (SQLException ex) {
           Logger.getLogger(OfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        }
    }
    
    //GETTERS*******************************************************************
    public ArrayList<Athlete> getAthletesByComp() {
        return athletesByComp;
    }

    public ArrayList<Athlete> getAthlets() {
        return athlets;
    }
}
