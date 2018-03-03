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
import views.TestCom.GlasialEditPage;

public class GlasialEditModel {    
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection(); 
    
    private TestComModel tcModel;
    private GlasialEditPage glasialEditPage;
    
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
        
    private static GlasialEditModel glasialEditModelInstace = null;
    private GlasialEditModel() {}    
    public static GlasialEditModel getGlasialEditModelInstance() {
        if (glasialEditModelInstace == null) {
            glasialEditModelInstace = new GlasialEditModel();
        }
        return glasialEditModelInstace;
    } 
    
    //*****************************ATHLETES*************************************
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
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }      
        
        //data lst
        //clear data lst
        try { 
            //get links, 
            //clear the array of lst, 
            //clear view of lst
            glasialEditPage = Manager.getGlasialEdtiPage();                                            
            athletesByComp.clear();            
            glasialEditPage.getAthlLstModel().clear(); 

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
                glasialEditPage.getAthlLstModel().addElement(athlete);
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(GlasialEditModel.class.getName()).
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
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            //get links, 
            //clear the array of cmb, 
            //clear view of cmb
            glasialEditPage = Manager.getGlasialEdtiPage();                        
            athlets.clear();
            glasialEditPage.getAthlCombo().removeAllItems();
            
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
                glasialEditPage.getAthlCombo().addItem(athlete);
            }
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }         
    }
    
    //add athlete
    public void addAthlete() {
        glasialEditPage = Manager.getGlasialEdtiPage();
        tcModel = TestComModel.getTestComModelInstance();
        
        //selected athlete
        Athlete newAthl = (Athlete)glasialEditPage.getAthlCombo().
                                                getSelectedItem();        
        
        //add to list
        glasialEditPage.getAthlLstModel().addElement(newAthl);
        //insert to array data
        athletesByComp.add(newAthl);
        
        //del from combo
        glasialEditPage.getAthlCombo().removeItem(newAthl);        
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
           pstmt.close();
        } catch (SQLException ex) {
           Logger.getLogger(SfpEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);        
        } catch (NullPointerException ex) {           
            JOptionPane.showMessageDialog(Manager.getGlasialEdtiPage(),
                        "Список спортсменов, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        } 
    }
    
    //DEL ATHLETE***************************************************************
    public void delAthlete() { 
        glasialEditPage = Manager.getGlasialEdtiPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
        //if athlete is not selected
        if (glasialEditPage.getAthlLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Перед удалением необходимо выделить спортсмена!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }        
        //selected athlete
        Athlete newAthl = (Athlete)glasialEditPage.getAthlLst().
                                             getSelectedValue();        
        //del from list
        glasialEditPage.getAthlLstModel().removeElement(newAthl);
        athletesByComp.remove(newAthl);
        
        //add to combobox
        glasialEditPage.getAthlCombo().addItem(newAthl);
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
           pstmt.close();
        } catch (SQLException ex) {
           Logger.getLogger(SfpEditModel.class.getName()).
                  log(Level.SEVERE, 
                  "Not del athlete from combobox", ex);
        }
    }
    //*********************************JUDGES***********************************
    //GET JUDGES 
    /*get judges, TAKING PART IN COMPETITION from DB
    save to array as data
    view it at list*/
    public void setJudgesList () {
        tcModel = TestComModel.getTestComModelInstance();
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
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "", ex);
        }      
        
        //data lst        
        try { 
            /*get links, clear the array of lst, 
            clear view of lst*/
            glasialEditPage = Manager.getGlasialEdtiPage();                                    
            judgesByComp.clear();            
            glasialEditPage.getJudLstModel().clear();                 
            
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
                glasialEditPage.getJudLstModel().addElement(judge);
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(GlasialEditModel.class.getName()).
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
            Logger.getLogger(TestComModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            /*get links, clear the array of cmb, 
            clear view of cmb*/
            glasialEditPage = Manager.getGlasialEdtiPage();                                    
            glasialEditPage.getJudCombo().removeAllItems();  
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
                glasialEditPage.getJudCombo().addItem(judge);
            }              
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(SfpEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
    }
    
    //add athlete chosen from combobox        
    public void addJudge() {
        glasialEditPage = Manager.getGlasialEdtiPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
        //selected athlete
        Judge newJudge = (Judge)glasialEditPage.getJudCombo().getSelectedItem();        
        
        //add to list
        glasialEditPage.getJudLstModel().addElement(newJudge);
        //insert to array data
        judgesByComp.add(newJudge);
        
        //del from combo
        glasialEditPage.getJudCombo().removeItem(newJudge);        
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
            JOptionPane.showMessageDialog(Manager.getGlasialEdtiPage(),
                        "Список судей, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }   
    }
    
    public void delJudge() { 
        glasialEditPage = Manager.getGlasialEdtiPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
          if (glasialEditPage.getJudLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Перед удалением необходимо выделить судью!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //selected athlete
        Judge newJudge = (Judge)glasialEditPage.getJudLst().getSelectedValue();
        
        //del from list
        glasialEditPage.getJudLstModel().removeElement(newJudge);
        judgesByComp.remove(newJudge);
        
        //add to combobox
        glasialEditPage.getJudCombo().addItem(newJudge);
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
           Logger.getLogger(SfpEditModel.class.getName()).
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
        glasialEditPage = Manager.getGlasialEdtiPage();
        int selRow = tcModel.selRow();
        
        String queryLst;        
        PreparedStatement prstLst = null;        
        ResultSet rsLst = null; 
                        
        //database lst  
        try {           
            queryLst = "SELECT GLASIAL_ELEMENT. * " +
                        "FROM GLASIAL_ELEMENT, TESTS_ELEMENTS_LINK " +
                        "WHERE TESTS_ELEMENTS_LINK.IDisuElement = GLASIAL_ELEMENT.ID " +
                        "AND TESTS_ELEMENTS_LINK.IDcompetition = " +
                        tcModel.getValueAt(selRow, 1) + ";";
            System.out.println(queryLst);
            prstLst = DBC.prepareStatement(queryLst);
            rsLst = prstLst.executeQuery();            
        } catch (SQLException ex) {
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "Not get  elements for list", ex);
        }      
        
        //data lst        
        try { 
            /*get links, clear the array of lst, 
            clear view of lst*/
            glasialEditPage = Manager.getGlasialEdtiPage();                                    
            //elementsByComp.clear();            
            glasialEditPage.getElLstModel().clear();                 
            
            //lst        
            while (rsLst.next()) {            
                Element element = new Element();
                element.setId(rsLst.getInt(1));
                element.setFullName(rsLst.getString(2));
                element.setDescription(rsLst.getString(3));
                //do it for save data in dif arrays 
                //in dif models
                elementsByComp.add(element);
                glasialEditPage.getElLstModel().addElement(element);                
            }
            prstLst.close();
            rsLst.close();
        } catch (SQLException ex) {
                Logger.getLogger(GlasialEditModel.class.getName()).
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
            queryCmb =  "SELECT GLASIAL_ELEMENT. * " +
                        "FROM GLASIAL_ELEMENT " +
                        "WHERE NOT GLASIAL_ELEMENT.ID = ANY( " +
                            "SELECT GLASIAL_ELEMENT.ID " +
                            "FROM GLASIAL_ELEMENT, TESTS_ELEMENTS_LINK " +
                            "WHERE TESTS_ELEMENTS_LINK.IDcompetition = " + 
                                   tcModel.getValueAt(selRow, 1) + " " +
                            "AND TESTS_ELEMENTS_LINK.IDisuElement = GLASIAL_ELEMENT.ID);";
            
            prstCmb = DBC.prepareStatement(queryCmb);
            rsCmb = prstCmb.executeQuery();             
        } catch (SQLException ex) {
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
        try { 
            /*get links, clear the array of cmb, 
            clear view of cmb*/
            glasialEditPage = Manager.getGlasialEdtiPage();                                    
            glasialEditPage.getElCombo().removeAllItems();  
            elements.clear();            
            //cmb
            while (rsCmb.next()) {            
                Element element = new Element();
                element.setId(rsCmb.getInt(1));
                element.setFullName(rsCmb.getString(2));
                element.setDescription(rsCmb.getString(3));
                //do it for save data in dif arrays 
                //in dif models
                elements.add(element);
                glasialEditPage.getElCombo().addItem(element);
            }              
            prstCmb.close();
            rsCmb.close();
        } catch (SQLException ex) {
            Logger.getLogger(GlasialEditModel.class.getName()).
                   log(Level.SEVERE, null, ex);
        }
    }
 
    //add element chosen from combobox        
    public void addElement() {
        glasialEditPage = Manager.getGlasialEdtiPage();
        tcModel = TestComModel.getTestComModelInstance(); 
        
        //selected judge
        Element newElement = (Element)glasialEditPage.getElCombo().getSelectedItem();        
        
        //add to list
        glasialEditPage.getElLstModel().addElement(newElement);
        //insert to array data
        elementsByComp.add(newElement);
        
        //del from combo
        glasialEditPage.getElCombo().removeItem(newElement);        
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
           Logger.getLogger(GlasialEditModel.class.getName()).
                  log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {           
            JOptionPane.showMessageDialog(Manager.getGlasialEdtiPage(),
                        "Список элементов, доступных для добавления пуст!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }   
    }
    
    public void delElement() { 
        glasialEditPage = Manager.getGlasialEdtiPage();
        tcModel = TestComModel.getTestComModelInstance(); 

       if (glasialEditPage.getElLst().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getGlasialEdtiPage(),
                        "Перед удалением необходимо выделить элемент!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //selected athlete
        Element newElement = (Element)glasialEditPage.getElLst().getSelectedValue();

        //del from list
        glasialEditPage.getElLstModel().removeElement(newElement);
        elementsByComp.remove(newElement);

        //add to combobox
        glasialEditPage.getElCombo().addItem(newElement);
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
           Logger.getLogger(GlasialEditModel.class.getName()).
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
