package models.TestCom;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import data.Competition;
import data.CompetitionKind;
import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import models.Performance.PerformanceModel;
import views.Manager;

public class TestComModel extends AbstractTableModel {
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                                      getDBconnection();     
    
    //service array for original columnNames like in sqlTable
    private ArrayList <String> enColumnNames = new ArrayList();    
    //table headers
    private String titles[] = {"Тип", "IDсор", "IDтипа", "Название", 
                               "<html>Внешн/<p>внутр<html>", "Дата и время", 
                               "Адрес", "Описание", "IDРазряда", "isFinished"};
    //viewing in table column names_rus
    private ArrayList <String> columnNames = new ArrayList();    
    //list of columns type 
    private ArrayList <Object> columnTypes = new ArrayList();
    
    //all competitions
    private ArrayList<Competition> competitions = new ArrayList();
    
    //competition kinds for combobox
    private ArrayList<CompetitionKind> compKind = new ArrayList();
    //combobox of seasons in a table
    private JComboBox comboCompKind = new JComboBox();
            
    //edit or not the table
    private boolean editable;
    
    private static TestComModel testComModelInstance = null;
    
    private TestComModel() {
        //can edit this table
        setEditable(true);
        createComboCompKind();
    }
    
    public static TestComModel getTestComModelInstance() {
        if (testComModelInstance == null) 
            testComModelInstance = new TestComModel();
        return testComModelInstance;
    }
        
    //editing
    public boolean isEditable() {
        return editable;
    }
    
    //SELECTED ROW**************************************************************
      public int selRow() {
        int sel = 0;
        JTable perTable = Manager.getTestCompPage().getTable();
        //get selected row
        sel = perTable.getSelectedRow();           
        if (sel == -1) {    
                JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Соревнование не выбрано!",
                "Ошибка", JOptionPane.WARNING_MESSAGE);                
        }
        return sel; 
    }
    
    //TABLE*********************************************************************
    //get table data from db
    private ResultSet getDataFromDB() {
        String query; 
        Statement stmt;
        ResultSet rs = null;
        try {
            //select all from 
            //(ID = COMPETITION.ID)
            query = "SELECT COMPETITION_KIND.FullName as FullNameKind, " +
                           "COMPETITION.* " +
                    "FROM COMPETITION, COMPETITION_KIND " +
                    "WHERE COMPETITION.IDcompetitionKind = COMPETITION_KIND.ID;";
            stmt = DBC.createStatement();            
            rs = stmt.executeQuery(query); 
        } catch (SQLException ex) {
            Logger.getLogger(TestComModel.class.getName()).
                             log(Level.SEVERE, 
                             "not get data from db", ex); 
        }
        return rs;
    }
    
    /*create values|rows for table
    get selectAllFromCompetition data from ResultSet, 
    push it to the data storage (there:competition)*/
     public void setDataSource() {        
        ResultSet rs = null; 
        Class type = null;
        try {
            //del prev data
            getCompetitions().clear();
            columnNames.clear();
            columnTypes.clear();
            
            rs = getDataFromDB();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            //get info about columns and their types,
            //set values to Performance.class arraylist
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                //add original columnNames to serviceArray
                enColumnNames.add(rsmd.getColumnName(i+1));                
                //add titles
                columnNames.add(titles[i]);
                //add columnType
                type = Class.forName(rsmd.getColumnClassName(i+1));
                columnTypes.add(type);                              
            }             
            
            //?something for table
            fireTableStructureChanged();
            
            //get row-data
            while (rs.next()) {
                //save a dataclass of a row
                Competition rowCompetition = new Competition();
                CompetitionKind comKind = new CompetitionKind();
                rowCompetition.setKind(comKind);
                
                for (int i = 0; i < columnCount; i++) {                    
                    switch (getEnColumnNames().get(i)) {
                        case ("IDcompetitionKind"):
                            comKind.setId(rs.getInt(i + 1));  
                            break;
                        case ("FullNameKind"):
                            comKind.setFullName(rs.getString(i + 1));
                            break;     
                        case ("ID"):
                            rowCompetition.setId(rs.getInt(i + 1));
                            break;
                        case ("FullName"):
                            rowCompetition.setFullName(rs.getString(i + 1));
                            break;                            
                        case ("InternalExternal"):
                            rowCompetition.setType(rs.getBoolean(i + 1));
                            break;                       
                        case ("DateTime"):
                            rowCompetition.setDateTime(rs.getTimestamp(i + 1));
                            break;
                        case ("Address"):
                            rowCompetition.setAddress(rs.getString(i + 1)); 
                            break;
                        case ("Description"):
                            rowCompetition.setDescription(rs.getString(i + 1));                            
                            break;
                        case ("IDrank"):
                            rowCompetition.setRankId(rs.getInt(i + 1));                            
                            break;
                        case ("isFinished"):
                            rowCompetition.setFinish(rs.getBoolean(i + 1));
                            break;
                    }                        
                }
                synchronized (competitions) {                    
                    competitions.add(rowCompetition);                     
                    //info about added row
                    fireTableRowsInserted(competitions.size()-1, 
                                          competitions.size()-1);
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(TestComModel.class.getName()).
                             log(Level.SEVERE, 
                             "ResultSet problem", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestComModel.class.getName()).
                             log(Level.SEVERE, 
                             "not define class type of data", ex);
        } 
    }  
    //************************************###***********************************       
    
     
     public Object getValueAt(int row, int column) { 
        Object returnField = null;        
        Competition comLink;        
        comLink = (Competition)competitions.get(row);       
        String colName = enColumnNames.get(column);

        switch (colName) {
            case ("IDcompetitionKind"):
                returnField = comLink.getKind().getId();
                break;
            case ("FullNameKind"):
                returnField = comLink.getKind().getFullName();                
                break;     
            case ("ID"):
                returnField = comLink.getId();                
                break;
            case ("FullName"):
                returnField = comLink.getFullName();                
                break;                            
            case ("InternalExternal"):
                returnField = comLink.isType();                
                break;                       
            case ("DateTime"):
                returnField = comLink.getDateTime();                
                break;
            case ("Address"):
                returnField = comLink.getAddress();
                break;
            case ("Description"):
                returnField = comLink.getDescription();
                break; 
            case ("IDrank"):
                returnField = comLink.getRankId();
                break; 
        }                  
        return returnField;        
    }
     
    //catch changes cell value
    public void setValueAt(Object value, int row, int column) {                        
        //will get an edited class
        Competition setClass;        
        //Performance row-exemplar
        setClass = competitions.get(row);         
                            
        switch (getEnColumnNames().get(column)) {                 
            case ("FullNameKind"):                        
                setClass.getKind().setFullName(((CompetitionKind)value).getFullName());
                setClass.getKind().setId(((CompetitionKind)value).getId());
                break; 
            case ("FullName"):
                setClass.setFullName(((String)value).trim());
                break;
            case ("InternalExternal"):
                setClass.setType(((boolean)value));
                break;                            
            case ("DateTime"):
                setClass.setDateTime(((Timestamp)value));
                break;                       
            case ("Address"):
                setClass.setAddress(((String)value).trim());
                break; 
            case ("Description"):
                setClass.setDescription(((String)value).trim());
                break;
            case ("IDrank"):
                setClass.setRankId((int)value);
                break;
           }             
        updateData(row, column, value);      
    }  
    
    //UPDATE DATA***************************************************************
    //update fields in db after edit table cell  
    public void updateData(int row, int column, Object value) {
        String query;
        try { 
            if (enColumnNames.get(column).equals("FullNameKind")) {                
                query = "UPDATE "  + "COMPETITION " +
                    "SET IDcompetitionKind = " + (((CompetitionKind)value)).getId() + " " +
                    "WHERE ID = " + getValueAt(row, 1) + ";"; 
                //comboCompKind.setEnabled(false);
               
            } else {
            //create updateQuery 
            query = "UPDATE " + "COMPETITION " +
                    "SET " + enColumnNames.get(column) + " = " +
                    "'" + value + "'" + " WHERE ID = " + getValueAt(row, 1);            
            }
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.executeUpdate(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(TestComModel.class.getName()).
                         log(Level.SEVERE, "Something wrong with SQLquery,"
                                         + "no update", ex);
        } 
    }
    
    //ADD COMPETITION***********************************************************    
    public void addCompetition()  { 
        String query;
        String selectID;
        int tempID = 0;
              
        Statement stmt;
        ResultSet rs;
        
        int rowIndex = 0;
        Competition newRow = null;
        CompetitionKind kind = null;
        try { 
            //view
            //get the least index, bottom add
            rowIndex = competitions.size();
            //create empty data class
            newRow = new Competition(); 
            kind = new CompetitionKind();
            newRow.setKind(kind);
            //add new row to Array
            competitions.add(newRow);        
            //change table view
            fireTableRowsInserted(rowIndex, rowIndex);
            
            //get ID added empty row
            selectID = "SELECT IDENT_CURRENT('COMPETITION')";
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(selectID);
            while (rs.next()) {
                tempID = rs.getInt(1)+ 1;
                newRow.setId(tempID);
                System.out.println("NEW ID + " + tempID);
            }            
            
            //add empty row
            query = "INSERT INTO COMPETITION " +
                    "VALUES (1, '', 0, GETDATE(), '', '', null, 0);";

            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.execute();                
          
        pstmt.close();
        stmt.close();
         } catch (SQLServerException ex) {
            removeRow(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
            Logger.getLogger(TestComModel.class.getName()).log(Level.SEVERE,
                       "not unique name of competition", ex);
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                        "Добавляйте соревнования по-одному!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (SQLException ex) {
            Logger.getLogger(TestComModel.class.getName()).log(Level.SEVERE,
                       "Not insert a new row into DB or not get row ID", ex);
            return;            
        }
    }
    
    //DEL COMPETITION***********************************************************
    public void delSelectedRow() {
        int sel = 0;
        int competitionId = 0;
        String query;
        try {                        
            //get selected row and competitionId
            sel = selRow();
            competitionId = competitions.get(sel).getId();                       
                        
            //del a row from DB
            query = "\n" + 
                    "DECLARE @IDCompet int = " + competitionId + "\n" +                    
                    "DELETE FROM REQUEST WHERE IDcompetition = @IDCompet;\n" +
                    "DELETE FROM COMPETITION_JUDGE_LINK WHERE IDcompetition = @IDCompet;\n" +
                    "DELETE FROM RESULT WHERE IDcompetition = @IDCompet;\n" +
                    //
                    "DELETE FROM ALL_RESULTS where IDtestsElementsLilk = ANY\n" +
                    "(select ID from TESTS_ELEMENTS_LINK where IDcompetition = @IDCompet);\n" +
                    "DELETE FROM TESTS_ELEMENTS_LINK WHERE IDcompetition = @IDCompet;\n" +
                    //
                    "DELETE FROM ALL_RESULTS_ELEMENTS WHERE IDcompetitionPerformanceAthleteLink = ANY\n" +
                    "(select ID FROM COMPETITION_PERFORMANCE_ATHLETE_LINK WHERE IDcompetition = @IDCompet);\n" +
                    "DELETE FROM ALL_RESULTS_COMPONENTS WHERE IDcompetitionPerformanceAthleteLink = ANY\n" +
                    "(select ID FROM COMPETITION_PERFORMANCE_ATHLETE_LINK WHERE IDcompetition = @IDCompet);\n" +
                    "DELETE FROM COMPETITION_PERFORMANCE_ATHLETE_LINK WHERE IDcompetition = @IDCompet;\n" +
                    //
                    "DELETE FROM COMPETITION_ATHLETE_LINK WHERE IDcompetition = @IDCompet;\n" +
                    "DELETE FROM COMPETITION where ID = @IDCompet;";                                       
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query); 
            pstmt.executeUpdate();
            removeRow(sel);
           
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).log(Level.SEVERE, 
                            "Something wrong with deleting a row", ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (sel == -1) {    
                JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Перед удалением необходимо выделить строку!",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }            
        }
    }
    //del a row from holderArrayList and autoRepaint table 
    private void removeRow(int sel) {      
        competitions.remove(sel);
        fireTableRowsDeleted(sel, sel);
    }
    
     
    //COMBOBOX_COMPETITION KIND*************************************************
    public void createComboCompKind() {
        String query; 
        Statement stmt;
        ResultSet rs = null;
        try {
            //select all competition kind         
            query = "SELECT * FROM COMPETITION_KIND;";
            stmt = DBC.createStatement();            
            rs = stmt.executeQuery(query); 
            
            while (rs.next())  {
                CompetitionKind ck = new CompetitionKind();
                ck.setId(rs.getInt(1));
                ck.setFullName(rs.getString(2));
                ck.setTableName(rs.getString(3));
                ck.setDescription(rs.getString(4)); 
                compKind.add(ck);
                comboCompKind.addItem(ck);
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(TestComModel.class.getName()).
                             log(Level.SEVERE, 
                             "not get data from db", ex); 
        }        
    }
     
    //**********************************###*************************************
     
    //GETTERS*******************************************************************        
    public int getRowCount() {  
         synchronized (competitions) {
            return competitions.size();
        }
    }    
    public int getColumnCount() {  
        return columnNames.size();
    }
    public String getColumnName(int column) {
        return columnNames.get(column);
    }
    public Class getColumnClass(int column) {
        return (Class)columnTypes.get(column);
    }
    public ArrayList<String>getEnColumnNames() {
        return enColumnNames;
    }
    public ArrayList<Competition> getCompetitions() {
        return competitions;
    }
    public JComboBox getComboCompKind() {
        return comboCompKind;
    }
    public boolean getCellEditable() {
        return editable;
    }
    //can edit or not
    public boolean isCellEditable(int row, int column) {
        return getCellEditable();
    } 
    
    //SETTERS*******************************************************************
    //set editable of a table
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}