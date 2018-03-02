 //list.setToolTipText("bbliblbiu"); set text by Pointing

package models.Performance;

import data.Athlete;
import data.Season;
import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import views.Manager;
import views.Performance.
        PerformanceEditPage;
import com.microsoft.sqlserver.
        jdbc.SQLServerException;

public class PerformanceEditModel {
   
    private static PerformanceEditModel performanceEditModelInstance = null;
    
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection(); 
    private PerformanceModel perModel = PerformanceModel.
                                        getPerformanceModelInstance();  
    private PerformanceEditPage perEditPage;
    
    //  take part in selected performance
    private ArrayList<Athlete> athletesByPerf = new ArrayList<>();
    
    //athletes without which are in a list 
    private ArrayList<Athlete> athlets = new ArrayList<>();
        
    //edit row of performance
    private int editingRow;
    
    //actual actualSeason
    private Season actualSeason = new Season();
    
    //singletone, get an object link
    public static PerformanceEditModel getPerformanceEditModelInstance() {
        if (performanceEditModelInstance == null)
            performanceEditModelInstance = new PerformanceEditModel();
        return performanceEditModelInstance;
    } 

    private PerformanceEditModel() {};  
    
    //update all fields
     public void updateInfo() {
        //create a list
        setDatatoList();
        //create a combobox of athletes
        getAllAthletes();
        //create a combobox of seasons
        setAllSeasonstoCombobox();
    }  
    
    //ATHLETE*******************************************************************
     
    /*set values for list and arrays
    pick athlets, season*/
    private void setDatatoList() {         
        //a big bad crutch
        perEditPage = Manager.getManagerInstance().getPerEditPage();
        
        //for geting a query
        PreparedStatement pstmtAthletes = null;
        ResultSet rsAthletes = null;
        String perfAthlets;
        
        //get selected row
        editingRow = perModel.selRow();  
        
        //clear all arrays and variables         
        athletesByPerf.clear();        
        perEditPage.getListModel().clear();
        perEditPage.getAthletesComboBox().removeAllItems();
        perEditPage.getSeasonComboBox().removeAllItems();
        
        /*select performance by selected-row id
        which row-settings to afford
        see at the edit panel*/
        perfAthlets =   "SELECT SEASON_PERFORMANCE.IDathlete, " +
                            "ATHLETE.Surname, ATHLETE.Name, " +
                            "ATHLETE.Middlename, " +
                            "SEASON.ID, " +
                            "SEASON.Period " +
                        "FROM SEASON_PERFORMANCE, ATHLETE, SEASON " +
                        "WHERE SEASON_PERFORMANCE.IDperformance = " +
                               perModel.getValueAt(editingRow, 0) + " " +
                               "AND " +
                               "SEASON_PERFORMANCE.IDathlete = ATHLETE.ID " +
                               "AND " + 
                               "SEASON_PERFORMANCE.IDseason = SEASON.ID;";                                
        try {           
            pstmtAthletes = DBC.prepareStatement(perfAthlets);
            rsAthletes = pstmtAthletes.executeQuery();
            
            //temp for get season once 
            int cicleTemp = 0;
            
            //get athletes from selected performance
            while (rsAthletes.next()) {
                //get data of athletes
                Athlete athlete = new Athlete();
                athlete.setId(rsAthletes.getInt(1));
                athlete.setName(rsAthletes.getString(3));
                athlete.setSurname(rsAthletes.getString(2));
                athlete.setMiddlename(rsAthletes.getString(4));

                //get a season of performance
                if(cicleTemp == 0)  {
                    actualSeason.setId(rsAthletes.getInt(5));
                    actualSeason.setPeriod(rsAthletes.getString(6));                    
                }
            
                //not get season later
                cicleTemp++;
                
                //add athles by performance to array
                athletesByPerf.add(athlete);                 
            }
            
            //set athletes to a list view
            for (int i = 0; i < athletesByPerf.size(); i++) {                               
                perEditPage.getListModel().addElement(athletesByPerf.get(i));
            }            
            perEditPage.getList().setModel(perEditPage.getListModel());               
            
        } catch (SQLException ex) {
        Logger.getLogger(PerformanceEditPage.class.getName()).log(Level.SEVERE, 
                "no  create list because of wrong query", ex);
        }
        //close connection to DB
        finally{
            try {
                pstmtAthletes.close();
                rsAthletes.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditPage.class.getName()).
                        log(Level.SEVERE, 
                        "no close connection to DB setDatatoList()", ex);
            }
        }
    }    
    
    /*set values for combobox
    athlets which are not at a performance list
    performance athlet's list + athletes in combobox = all athlets*/
    private void getAllAthletes() {
        athlets.clear();
        perEditPage.getAthletesComboBox().removeAllItems();
        Statement stmtAllAthletes = null;
        ResultSet rsAllAthletes = null;
        String allAthletes;
        try {
            allAthletes = "SELECT DISTINCT " +
                                  "ATHLETE.ID, " +
                                  "ATHLETE.Surname, ATHLETE.Name, " +
                                  "ATHLETE.Middlename " +
                          "FROM	ATHLETE, SEASON_PERFORMANCE " +
                          "WHERE NOT (ATHLETE.ID = ANY(" +
                                     "SELECT SEASON_PERFORMANCE.IDathlete " +
                                     "FROM SEASON_PERFORMANCE, ATHLETE " +
                                     "WHERE SEASON_PERFORMANCE.IDperformance = " +
                                      perModel.getValueAt(editingRow, 0) + " " +
                                     "AND " +
                                     "SEASON_PERFORMANCE.IDathlete = ATHLETE.ID)) " +
                          "ORDER BY ATHLETE.Surname;";
            
            stmtAllAthletes = DBC.createStatement();
            rsAllAthletes = stmtAllAthletes.executeQuery(allAthletes);
            
            //get all needed athlets
            while (rsAllAthletes.next()) {                 
                Athlete athlete = new Athlete();
                athlete.setId(rsAllAthletes.getInt(1));
                athlete.setName(rsAllAthletes.getString(3));
                athlete.setSurname(rsAllAthletes.getString(2));
                athlete.setMiddlename(rsAllAthletes.getString(4));
                
                //push them by one into combobox
                perEditPage.getAthletesComboBox().addItem(athlete);
                //push them by one into array                                
                athlets.add(athlete); 
            }                            
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceEditPage.class.getName()).
                             log(Level.SEVERE, 
                             "no get all athletes, to show in combobox", ex);
        }
        //close connection to DB
        finally{
            try {
                stmtAllAthletes.close();
                rsAllAthletes.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditPage.class.getName()).
                        log(Level.SEVERE, 
                        "no close connection to DB getAllAthletes()", ex);
            }
        }
    } 
    
    //add athlete to performance (db and view)
    public void addAthlete() {
        PreparedStatement pstmtaddAthl = null;
        String addAthl;
        
        if (perEditPage.getAthletesComboBox().getItemCount() == 0) {
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Список спортсменов, доступных для добавления пуст",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {   
            addAthl = "INSERT INTO SEASON_PERFORMANCE " + 
                      "VALUES (" +
                               actualSeason.getId() + ", " +
                               ((Athlete)(perEditPage.getAthletesComboBox().
                                          getSelectedItem())).getId() + ", " +
                               perModel.getValueAt(editingRow, 0)  + " " + ");";
            
            //add to db
            pstmtaddAthl = DBC.prepareStatement(addAthl);
            pstmtaddAthl.execute();
            
            //addd to list
            perEditPage.getListModel().addElement(((Athlete)perEditPage.
                                                 getAthletesComboBox().
                                                 getSelectedItem()));
            
            int index = perEditPage.getListModel().size() - 1;
            perEditPage.getList().ensureIndexIsVisible(index);
            
            //remove athletes in combobox
            getAllAthletes();
            
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceEditModel.class.getName()).
                    log(Level.SEVERE, 
                    "some problems with add athlete, add to db or viewing, " +
                    "addAthlete()", ex);
        } finally {
            try {
                pstmtaddAthl.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                        log(Level.SEVERE, 
                        "no close connection to db, addAthlete()", ex);
            }
        }
    }
    
    //del athletes from performance (db and view)
    public void delAthlete() {
        if (perEditPage.getList().getSelectedValue() == null) {
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Перед удалением необходимо выделить спортсмена",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }        
        PreparedStatement pstmtdelAthl = null;
        String delAthl;
            try {
                delAthl = "DELETE FROM SEASON_PERFORMANCE " +
                          "WHERE SEASON_PERFORMANCE.IDathlete = " +
                               ((Athlete)perEditPage.getList().
                               getSelectedValue()).getId() + ";";
                //del from db
                pstmtdelAthl = DBC.prepareStatement(delAthl);
                pstmtdelAthl.execute();

                //remove from list
                perEditPage.getListModel().remove
                (perEditPage.getList().getSelectedIndex());
                
                //add to combobox
                getAllAthletes();
                
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "some problems with del athlete, add to db or viewing, " +
                   "addAthlete()", ex);
        } finally {
            try {
                pstmtdelAthl.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                       log(Level.SEVERE, 
                       "no close connection to db, delAthlete()", ex);
            }
        }
    }
    //**********************************###*************************************
    
    
    //SEASON********************************************************************
    
    //get values to combobox of seasons
    public void setAllSeasonstoCombobox() {        
        //clear combobox
        perEditPage.getSeasonComboBox().removeAllItems(); 
        
        //add new items
        for (int i = 0; i < perModel.getSeasons().size(); i++ ) {
            perEditPage.getSeasonComboBox().addItem
                                       (perModel.getSeasons().get(i)); 
        }
        //view actual season of performance in combobox
        perEditPage.getSeasonComboBox().setSelectedItem(actualSeason);
    }

    //change season in db and view
    public void changeSeason() {
        PreparedStatement pstmtChangeSeason = null;        
        String chSeason;
        
        try {            
            chSeason= "UPDATE SEASON_PERFORMANCE " +
                      "SET IDseason = " + ((Season) perEditPage.getSeasonComboBox().
                                          getSelectedItem()).getId() + " " +
                      "WHERE SEASON_PERFORMANCE.IDperformance = " +
                      perModel.getValueAt(editingRow, 0) + ";";             
            
            //change season in db
            pstmtChangeSeason = DBC.prepareStatement(chSeason);
            pstmtChangeSeason.executeUpdate();
            
            //change actual season field  (at view)                       
            actualSeason.setPeriod(((Season)perEditPage.getSeasonComboBox().
                                            getSelectedItem()).getPeriod());       
            actualSeason.setId(((Season)(perEditPage.getSeasonComboBox().
                                         getSelectedItem())).getId());
                        
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Сезон успешно изменен!",
                        "Информация", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceEditModel.class.getName()).
                           log(Level.SEVERE, 
                           "not execute chSeason quetry in changeSeason()", ex);
        }
        finally {
            try {
                pstmtChangeSeason.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditModel.class.getName()).log(Level.SEVERE, 
                        "no close connection to DB, changeSeason()", ex);
            }
        }
    }
    
    //if btn "add season" is pressed    
    public void addSeason() {
        JTextField txt = perEditPage.getTextField();
        txt.setVisible(true);              
    }
    
    //get new id for season from db
    public int getNewSeasonID() {
        String query;
        int tempID = 0;

        Statement stmt;
        ResultSet rs;
        try {
            //get ID added empty row
            query = "SELECT IDENT_CURRENT('SEASON')";
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                tempID = rs.getInt(1)+ 1;
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceEditModel.class.getName()).
                         log(Level.SEVERE, 
                             "not get ID for new season", ex);
        }
        return tempID;
    }    
    
    //add new season to db
    public void addSeasonToDB(String value) {
        PreparedStatement pstmt = null;
        String query;        
      try {
            query = "INSERT INTO SEASON VALUES ('" + value + "');";
            System.out.println(query);
            
            //add to db
            pstmt = DBC.prepareStatement(query);
            pstmt.execute();
            
        } catch(SQLException ex){
            Logger.getLogger(PerformanceEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "some problems with add season to db", ex);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                       log(Level.SEVERE, 
                       "no close connection to db, delAthlete()", ex);
            }
        }    
    }
    
    //del selected in combobox season from db
    public boolean delSeasonFromDB(int value) {
        PreparedStatement pstmt = null;
        String query; 
         try {
            query = "DELETE FROM SEASON WHERE ID = " + value + ";";
            System.out.println(query);
            
            //add to db
            pstmt = DBC.prepareStatement(query);
            pstmt.execute();
            
            pstmt.close();
            
        } catch(SQLServerException ex){
            try {
                JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Невозможно удалить используемый сезон",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
                pstmt.close();
                return false;
            } catch (SQLException ex1) {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                             log(Level.SEVERE, null, ex1);
            }
        } catch (SQLException ex) {
            try {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                             log(Level.SEVERE, "", ex);
                pstmt.close();
            } catch (SQLException ex1) {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                             log(Level.SEVERE, null, ex1);
                return false;
            }
        }
         return true;
    }
    //************************************##************************************

    //GETTERS*******************************************************************
    public Season getActualSeason() {
        return actualSeason;
    }  
}
  