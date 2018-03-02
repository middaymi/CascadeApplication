/*
TABLE
SEASONS
*/


package models.Performance;

import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import views.Manager;
import data.Performance;
import data.Season;
import javax.swing.JComboBox;

public class PerformanceModel extends AbstractTableModel {

    private static PerformanceModel performanceModelInstance = null;
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection();  
    //edit or not the table
    private boolean editable;
        
    //DATA**********************************************************************
    private ArrayList <Performance> data = new ArrayList();  
    //array of all seasons
    private ArrayList<Season> seasons = new ArrayList<>(); 
        
    //HEADER********************************************************************
    //service array for original columnNames like in sqlTable
    private ArrayList <String> enColumnNames = new ArrayList();    
    //table headers
    private String titles[] = {"ID", "Название", "Фонограмма", "Дизайн костюма", 
                               "Фото костюма", "Описание", "ID сезона", "Сезон"};     
    //viewing in table column names_rus
    private ArrayList <String> columnNames = new ArrayList();    
    //list of columns type 
    private ArrayList <Object> columnTypes = new ArrayList();     
    
    //combobox of seasons in a table
    private JComboBox comboSeasons = new JComboBox();
    //**************************************************************************
    
    private PerformanceModel() {
        //can edit this table
        setEditable(true);
        //get all seasons
        getAllSeasonsFromDB();
        /*get all seasons and 
        create combobox of them*/
        createComboSeasons();        
    }
    
    //singletone, get an object link
    public static PerformanceModel getPerformanceModelInstance() {
        if (performanceModelInstance == null)
            performanceModelInstance = new PerformanceModel();
        return performanceModelInstance;
    }
    
    //editing
    public boolean isEditable() {
        return editable;
    }       
    
    //TABLE*********************************************************************    
    //OPEN EDIT MODE************************************************************
    public int selRow() {
        int sel = 0;
        JTable perTable = Manager.getPerPage().getTable();
        //get selected row
        sel = perTable.getSelectedRow(); 
        if (sel == -1) {    
                JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Не выбрана постановка для редактирования",
                "Ошибка", JOptionPane.WARNING_MESSAGE);                
        }
        return sel; 
    }
    
    //GET AND SET DATA**********************************************************
    //select all data from db
    private ResultSet getDataFromDB() {
    String query; 
      //select all from performance
      query = "SELECT PERFORMANCE.*, " +
                    "SEASON.ID AS IDseason, " +
                    "SEASON.Period " +
              "FROM PERFORMANCE, SEASON_PERFORMANCE, " +
                    "SEASON " +
              "WHERE PERFORMANCE.ID = SEASON_PERFORMANCE.IDperformance " +
                     "AND " +
                     "SEASON_PERFORMANCE.IDseason = SEASON.ID " +
              "GROUP BY PERFORMANCE.ID, " +
                     "PERFORMANCE.FullName, " +
                     "PERFORMANCE.Phonogram, " +
                       "PERFORMANCE.CostumeDesign, " +
                     "PERFORMANCE.CostumePhoto, " +
                     "PERFORMANCE.Description, " + 
                     "SEASON.ID, " +
                     "SEASON.Period " +
              "ORDER BY PERFORMANCE.FullName DESC;";

        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = DBC.createStatement();            
            rs = stmt.executeQuery(query); 
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).
                             log(Level.SEVERE,"no executed "
                             + "query 'selectAllFromPerformance'", ex); 
        }
        return rs;
    }
        
    /*create values|rows for table
    get selectAllFromPerformance data from ResultSet, 
    push it to the data storage (there:performance)*/
     public void setDataSource() {        
        ResultSet rs = null; 
        Class type = null;
        try {
            //del prev data
            getData().clear();
            getColumnNames().clear();
            getColumnTypes().clear();
            
            rs = getDataFromDB();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            //get info about columns and their types,
            //set values to Performance.class arraylist
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                //add original columnNames to serviceArray
                enColumnNames.add(rsmd.getColumnName(i+1));
                //add titles
                setColumnNames(titles[i]);
                //add columnType
                type = Class.forName(rsmd.getColumnClassName(i+1));
                setColumnTypes(type);                 
            } 
                                     
            //?something for table
            fireTableStructureChanged();
            
            //get row-data
            while (rs.next()) {
                //save a dataclass of a row
                Performance rowPerformance = new Performance();
                Season season = new Season();
                rowPerformance.setSeason(season);
                for (int i = 0; i < columnCount; i++) {                    
                    switch (enColumnNames.get(i)) {
                        case ("ID"):
                            rowPerformance.setId(rs.getInt(i + 1));
                            break;
                        case ("FullName"):
                            rowPerformance.setFullName(rs.getString(i + 1));
                            break;     
                        case ("Phonogram"):
                            rowPerformance.setPhonogram(rs.getString(i + 1));
                            break;
                        case ("CostumeDesign"):
                            rowPerformance.setCostumeDesign(rs.getString(i + 1));
                            break;                            
                        case ("CostumePhoto"):
                            rowPerformance.setCostumePhoto(rs.getString(i + 1));
                            break;                       
                        case ("Description"):
                            rowPerformance.setDescription(rs.getString(i + 1));
                            break;
                        case ("IDseason"):
                            rowPerformance.setSeason(rs.getInt(i + 1));                            
                            break;
                        case ("Period"):
                            rowPerformance.setSeason(rs.getString(i + 1));
                            comboSeasons.setSelectedItem(rowPerformance.getSeason());                            
                            break;
                    }                 
                }
                synchronized (getData()) {                    
                    setData(rowPerformance);
                    //info about added row
                    fireTableRowsInserted(getData().size()-1, 
                                          getData().size()-1);
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).
                             log(Level.SEVERE, 
                             "ResultSet problem", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).
                             log(Level.SEVERE, 
                             "not define class type of data", ex);
        }        
    } 
     
    //get selected value
    public Object getValueAt(int row, int column) {        
        Performance perLink;        
        perLink = (Performance)getData().get(row);        
        String colName = enColumnNames.get(column);
        Object returnField = null;

        switch (colName) {
            case ("ID"): 
                returnField = perLink.getId();
                break;
            case ("FullName"):
                returnField = perLink.getFullName();
                break;     
            case ("Phonogram"):
                returnField = perLink.getPhonogram();
                break;
            case ("CostumeDesign"):
                returnField = perLink.getCostumeDesign();
                break;                            
            case ("CostumePhoto"):
                returnField = perLink.getCostumePhoto();
                break;                       
            case ("Description"):
                returnField = perLink.getDescription();
                break; 
            case ("Period"):
                returnField = perLink.getSeason();                                
                break;
        }                   
        return returnField;        
    }  
    
    //catch changes cell value
    public void setValueAt(Object value, int row, int column) {                        
        //will get an edited class
        Performance setClass;        
        //Performance row-exemplar
        setClass = getDataByIndex(row);         
                            
        switch (getEnColumnNames().get(column)) {                 
            case ("FullName"):                        
                setClass.setFullName(((String)value).trim());
                break;                            
            case ("Phonogram"):
                setClass.setPhonogram(((String)value).trim());
                break;
            case ("CostumeDesign"):
                setClass.setCostumeDesign(((String)value).trim());
                break;                            
            case ("CostumePhoto"):
                setClass.setCostumePhoto(((String)value).trim());
                break;                       
            case ("Description"):
                setClass.setDescription(((String)value).trim());
                break; 
            case ("IDseason"):
                setClass.setSeason(((Season)value).getId());
                break;
            case ("Period"):                
                setClass.setSeason((Season)value);
                break;
        }             
        updateData(row, column, value);      
    }  
    
    //UPDATE DATA***************************************************************
    //update fields in db after edit table cell  
    public void updateData(int row, int column, Object value) {
        String query;
        try { 
            if (enColumnNames.get(column).equals("Period")) {
                query = "UPDATE "  + "SEASON_PERFORMANCE " +
                    "SET IDseason = " + (((Season)value)).getId() + " " +
                    "WHERE IDperformance = " + getValueAt(row, 0) + ";";    
            } else {
            //create updateQuery 
            query = "UPDATE " + "PERFORMANCE " +
                    "SET " + enColumnNames.get(column) + " = " +
                    "'" + value + "'" + " WHERE ID = " + getValueAt(row, 0);
            }
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.executeUpdate(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).
                         log(Level.SEVERE, "Something wrong with SQLquery,"
                                         + "no update", ex);
        } 
    }
    
    //INSERT_ROW****************************************************************
    //to view
    public void addPerformance() {
        //get the least index, bottom add
        int rowIndex = getData().size();
        //create empty data class
        Performance newRow = new Performance();        
        newRow.setFullName("");
        newRow.setPhonogram("");
        newRow.setCostumeDesign("");
        newRow.setCostumePhoto("");
        newRow.setDescription("");        
        setData(newRow);        
        //change table view
        fireTableRowsInserted(rowIndex, rowIndex);
        //insert into db
        insertRowIntoTable(newRow);
    }
    //insert to db
    private void insertRowIntoTable (Performance performance)  { 
        String query;
        String selectID;
        int tempID = 0;
        
        Statement stmt;
        ResultSet rs;
        try {               
            //get ID added empty row
            selectID = "SELECT IDENT_CURRENT('PERFORMANCE')";
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(selectID);
            while (rs.next()) {
                tempID = rs.getInt(1)+ 1;
                performance.setId(tempID);
            }            
            
            //add empty row
            query = "INSERT INTO PERFORMANCE " +
                    "VALUES (" + tempID + ", '', '', '', ''); \n" +
                    "INSERT INTO SEASON_PERFORMANCE " +
                    "VALUES ((SELECT TOP 1 ID from SEASON), NULL, " + tempID + ");";                   

            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.execute();                
          
        pstmt.close();
        stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).log(Level.SEVERE,
                       "Not insert a new row into DB or not get row ID", ex);
        }        
    }
    
   //DELETE_ROW*****************************************************************  
   public void delSelectedRow() {
        int sel = 0;
        String query;
        try {             
            JTable perTable = Manager.getPerPage().getTable();
            //get selected row
            sel = perTable.getSelectedRow();             
                        
            //del a row from DB
            query = "DELETE FROM SEASON_PERFORMANCE " +
                    "WHERE IDperformance = " + getValueAt(sel, 0) + ";\n" +
                    "DELETE FROM PERFORMANCE " +
                    "WHERE ID = " + getValueAt(sel, 0) + ";";                                        
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query); 
            pstmt.executeUpdate();
            this.removeRow(sel);
           
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).log(Level.SEVERE, 
                            "Something wrong with deleting a row", ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (sel == -1) {    
                JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Перед удалением необходимо выделить строку",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }            
        }
    }
    //del a row from holderArrayList and autoRepaint table 
    private void removeRow(int sel) {      
        getData().remove(sel);
        fireTableRowsDeleted(sel, sel);
    } 
        
    //DEL_EMPTY_ROWS************************************************************
    public void delEmptyRows() {        
        try { 
            //for delete from data
            //what ID will del
            String query = "SELECT ID FROM PERFORMANCE "
                         + "WHERE FullName = CAST(ID AS varchar(10));";
            System.out.println(query);
            Statement stmt = DBC.createStatement();
            ResultSet rs = stmt.executeQuery(query); 
                        
            //get id rows for del
            int i = 1;
                        
            //if there are empty rows
            while (rs.next()) {                     
                for (int j = 0; j < getData().size(); j++) {
                    if (((Performance) getDataByIndex(j)).getId() == rs.getInt(i)) {
                        //delete rows from data and table
                        removeRow(j);  
                    }
                }                
            }
            //say user message
            JOptionPane.showMessageDialog(Manager.getPerPage(),
            "Пустые строки были удалены",
            "Информирование", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
        Logger.getLogger(PerformanceModel.class.getName()).log(Level.SEVERE, 
                         "Cannot select empty rows", ex);
        }
        
        //delete from db
        try{           
            String queryDelDB = "DELETE FROM PERFORMANCE "
                              + "WHERE FullName = CAST(ID AS varchar(10));";
            System.out.println(queryDelDB);
            PreparedStatement pstmt = DBC.prepareStatement(queryDelDB);
            pstmt.execute();          
            
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceModel.class.getName()).log(Level.SEVERE, 
                             "Cannot delete empty rows", ex);
        }
    }    
    //**********************************###*************************************
    
    
    //SEASONS*******************************************************************
    //combobox-seasons cell 
    public void createComboSeasons() {  
        comboSeasons.removeAllItems();
        comboSeasons.setEditable(false);
        for (int i = 0; i < seasons.size(); i++) {
            comboSeasons.addItem(seasons.get(i));
        }           
    }

    //GET ALL SEASONS AND ADD THEM TO AN ARRAY**********************************
    public void getAllSeasonsFromDB() {
        seasons.clear();
        Statement stmtAllSeasons = null;
        ResultSet rsAllSeasons = null;
        String allSeasons;
        
        try {   
            allSeasons = "SELECT * FROM SEASON;";
            stmtAllSeasons = DBC.createStatement();
            rsAllSeasons = stmtAllSeasons.executeQuery(allSeasons);
            
            //get all seasons
            while (rsAllSeasons.next()) {
                Season season = new Season();
                season.setId(rsAllSeasons.getInt(1));
                season.setPeriod(rsAllSeasons.getString(2));
                seasons.add(season);              
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceEditModel.class.getName()).
                             log(Level.SEVERE, 
                             "someting wrong with getting all season query, " + 
                             "getAllSeason()", ex);
        } finally {
            try {
                stmtAllSeasons.close();
                rsAllSeasons.close();
            } catch (SQLException ex) {
                Logger.getLogger(PerformanceEditModel.class.getName()).
                                 log(Level.SEVERE, 
                                 "no close connection to DB, getAllSeason()", ex);
            }
        }        
    }
    //**********************************###*************************************
    
  
      
    //GETTERS*******************************************************************
    //return the array of all seasons (for comboboxes)
    public ArrayList<Season> getSeasons() {
        return this.seasons;
    }
    //get all seasons
    public JComboBox getComboSeasons() {
        return comboSeasons;
    }
    //can edit or not
    public boolean isCellEditable(int row, int column) {
        return getCellEditable();
    }  
    
    //DELETE ONE OF THEM
    //get a link of Employee class with set data
    public ArrayList getEmployeeDataLink() {
        return data;	 
    }
    public ArrayList getData() {
        return data;
    } 
    
    //get rows number
    public int getRowCount() {
        synchronized (getData()) {
            return getData().size();
        }
    }        
    public ArrayList getColumnNames() {
        return columnNames;
    }
    public ArrayList <String> getEnColumnNames() {
        return enColumnNames;
    }
    public ArrayList getColumnTypes() {
        return columnTypes;
    }
    public Performance getDataByIndex(int i) {
        return (Performance)data.get(i);
    }   
    public boolean getCellEditable() {
        return editable;
    }
    //get columns number
    public int getColumnCount() {
        return getColumnNames().size();
    }
    //get cell column type
    public Class getColumnClass(int column) { 
        return (Class)getColumnTypes().get(column);
    }
    //get cell name
    public String getColumnName(int column) {
        return (String)getColumnNames().get(column);
    } 
    
    //SETTERS*******************************************************************
    //set editable of a table
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    public void setColumnNames(String str) {
        this.columnNames.add(str);
    }
    public void setColumnTypes(Class cls) {
        this.columnTypes.add(cls);
    }
    public void setData(Performance data) {
        this.data.add(data);
    }
}
