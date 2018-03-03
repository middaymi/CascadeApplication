package models.Athlete;

import data.Athlete;
import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import views.Manager;

public class AthleteModel extends AbstractTableModel {

    private static AthleteModel athleteModelInstance = null;
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection();  
    //edit or not the table
    private boolean editable;
        
    //DATA**********************************************************************
    private ArrayList <Athlete> data = new ArrayList();  
        
    //HEADER********************************************************************
    //service array for original columnNames
    private ArrayList <String> enColumnNames = new ArrayList();    
    //table headers
    private String titles[] = {"ID", "Фамилия", "Имя", "Отчество", "ДР", 
                               "Разряд", "Представитель", "Телефон", "Адрес",
                               "Документ", "Сертификат", "Страховка", "Пол"};     
    //order like in sqlTable; then name, surname are swaped 
    private ArrayList <String> columnNames = new ArrayList();    
    //list of columns type 
    private ArrayList <Object> columnTypes = new ArrayList();     
    //**************************************************************************
    
    //constructor
    private AthleteModel() {
        setEditable(true);      
    }
    
    //singletone, get an object link
    public static AthleteModel getAthleteModelInstance() {
        if (athleteModelInstance == null)
            athleteModelInstance = new AthleteModel();
        return athleteModelInstance;
    }
    
    //editing
    public boolean isEditable() {
        return editable;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }   
    
    //SELECT ALL****************************************************************
    //query: select all data from db
    String selectAllFromAthlete = "select * from athlete";
    private ResultSet getDataFromDB() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(selectAllFromAthlete); 
        } catch (SQLException ex) {
            Logger.getLogger(AthleteModel.class.getName()).
                             log(Level.SEVERE,"no executed "
                             + "query 'selectAllFromAthlete'", ex); 
        }
        return rs;
    }
    
    //CREATE VALUES|ROWS FOR TABLE**********************************************
    //get selectAllFromAthlete data from ResultSet, 
    //push it to the data storage (there:athlete)
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
            //set values to Athlete.class arraylist
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
                Athlete rowAthlete = new Athlete();
                for (int i = 0; i < columnCount; i++) {                    
                    switch (enColumnNames.get(i)) {
                        case ("ID"):
                            rowAthlete.setId(rs.getInt(i + 1));
                            break;                           
                        case ("Surname"):
                            rowAthlete.setSurname(rs.getString(i + 1));
                            break; 
                        case ("Name"):
                            rowAthlete.setName(rs.getString(i + 1));
                            break;
                        case ("Middlename"):
                            rowAthlete.setMiddlename(rs.getString(i + 1));
                            break;
                        case ("Birthday"):
                            rowAthlete.setBirthday(rs.getDate(i + 1));
                            break;                            
                        case ("IDrank"):
                            rowAthlete.setIdrank(rs.getInt(i + 1));
                            break;
                        case ("AssigneeFullName"):
                            rowAthlete.setAssigneeFullName(rs.getString(i + 1));
                            break;
                        case ("PhoneNumber"):
                            rowAthlete.setPhoneNumber(rs.getString(i + 1));
                            break;
                        case ("ActualAddress"):
                            rowAthlete.setActualAddress(rs.getString(i + 1));
                            break;
                        case ("MainDocumentCopy"):
                            rowAthlete.setMainDocumentCopy(rs.getString(i + 1));
                            break;
                        case ("MedicalCertificate"):
                            rowAthlete.setMedicalCertificate(rs.getString(i + 1));
                            break;
                        case ("Insurance"):
                            rowAthlete.setInsurance(rs.getString(i + 1));
                            break;
                        case ("Sex"):
                            rowAthlete.setSex(rs.getBoolean(i + 1));
                            break;
                    }                 
                }                
                synchronized (getData()) {                    
                    setData(rowAthlete);
                    //info about added row
                    fireTableRowsInserted(getData().size()-1, 
                                          getData().size()-1);
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(AthleteModel.class.getName()).
                             log(Level.SEVERE, 
                             "ResultSet problem", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AthleteModel.class.getName()).
                             log(Level.SEVERE, 
                             "not define class type of data", ex);
        }        
    } 
     
    //GET_VALUE*****************************************************************
    public Object getValueAt(int row, int column) {        
        Athlete athLink;        
        athLink = (Athlete)getData().get(row);        
        String colName = enColumnNames.get(column);
        Object returnField = null;

        switch (colName) {            
            case ("ID"):
                returnField = athLink.getId();
                break;                           
            case ("Surname"):
                returnField = athLink.getSurname();
                break; 
            case ("Name"):
                returnField = athLink.getName();
                break;
            case ("Middlename"):
                returnField = athLink.getMiddlename();
                break;
            case ("Birthday"):
                returnField = athLink.getBirthday();
                break;                            
            case ("IDrank"):
                returnField = athLink.getIdrank();
                break;
            case ("AssigneeFullName"):
                returnField = athLink.getAssigneeFullName();
                break;
            case ("PhoneNumber"):
                returnField = athLink.getPhoneNumber();
                break;
            case ("ActualAddress"):
                returnField = athLink.getActualAddress();
                break;
            case ("MainDocumentCopy"):
                returnField = athLink.getMainDocumentCopy();
                break;
            case ("MedicalCertificate"):
                returnField = athLink.getMedicalCertificate();
                break;
            case ("Insurance"):
                returnField = athLink.getInsurance();
                break;
            case ("Sex"):
                returnField = athLink.getSex();
                break;
        }
        return returnField;        
    }  
    
    //CATCH CHANGE CELL VALUE***************************************************
    public void setValueAt(Object value, int row, int column) {                        
        //will get an edited class
        Athlete setClass;        
        //Athlete row-exemplar
        setClass = getDataByIndex(row);         
                          
        switch (getEnColumnNames().get(column)) {                        
            case ("Surname"):
                setClass.setSurname(((String)value).trim());
                break; 
            case ("Name"):
                setClass.setName(((String)value).trim());
                break;
            case ("Middlename"):
                setClass.setMiddlename(((String)value).trim());
                break;
            case ("Birthday"):
                setClass.setBirthday((Date)value);
                break;                            
            case ("IDrank"):
                setClass.setIdrank((int)value);
                break;
            case ("AssigneeFullName"):
                setClass.setAssigneeFullName(((String)value).trim());
                break;
            case ("PhoneNumber"):
                setClass.setPhoneNumber(((String)value).trim());
                break;
            case ("ActualAddress"):
                setClass.setActualAddress(((String)value).trim());
                break;
            case ("MainDocumentCopy"):
                setClass.setMainDocumentCopy(((String)value).trim());
                break;
            case ("MedicalCertificate"):
                setClass.setMedicalCertificate(((String)value).trim());
                break;
            case ("Insurance"):
                setClass.setInsurance(((String)value).trim());
                break;
            case ("Sex"):
                setClass.setSex((boolean)value);
                break;
        }             
        updateData(row, column, value);      
   }  
       
    //UPDATE FIELDS IN DB AFTER EDIT TABLE CELL*********************************
    public void updateData(int row, int column, Object value) {                
        try { 
            //create updateQuery 
            String query = "UPDATE " + "ATHLETE " +
                    "SET " + enColumnNames.get(column) + " = " +
                    "'" + value + "'" + " WHERE ID = " + getValueAt(row, 0);
            System.out.println(query);
            //update
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.executeUpdate(); 
        } catch (SQLException ex) {
            Logger.getLogger(AthleteModel.class.getName()).
                         log(Level.SEVERE, "Something wrong with SQLquery,"
                                         + "no update", ex);
        } 
    }
    
   //DELETE_ROW*****************************************************************  
   public void delSelectedRow() {
        int sel = 0;
        try {             
            JTable athTable = Manager.getAthPage().getTable();
            //get selected row
            sel = athTable.getSelectedRow(); 
            System.out.println("sel " + sel);
                        
            //del a row from DB
            String query = "DELETE " + "FROM ATHLETE " +
                           "WHERE ID = " + getValueAt(sel, 0);
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query); 
            pstmt.executeUpdate();
            this.removeRow(sel);
           
        } catch (SQLException ex) {
            Logger.getLogger(AthleteModel.class.getName()).log(Level.SEVERE, 
                            "Something wrong with deleting a row", ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (sel == -1) {    
                JOptionPane.showMessageDialog(Manager.getAthPage(),
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
    
    //INSERT_ROW****************************************************************
    public void addAthlete() {
        //get the least index, bottom add
        int rowIndex = getData().size();
        //create empty data class
        Athlete newRow = new Athlete();  
        
        newRow.setSurname("");
        newRow.setName("");
        newRow.setMiddlename("");
        //birthday
        //idrank
        newRow.setAssigneeFullName("");
        newRow.setPhoneNumber("");
        newRow.setActualAddress("");
        newRow.setMainDocumentCopy("");
        newRow.setMedicalCertificate("");
        newRow.setInsurance("");
        newRow.setSex(false);

        setData(newRow);        
        //change table view
        fireTableRowsInserted(rowIndex, rowIndex);
        //insert into db
        insertRowIntoTable(newRow);
    }
    
    private void insertRowIntoTable (Athlete athlete)  {        
        try {
            //add empty row
            String query = "INSERT INTO ATHLETE "
                         + "VALUES ('', '', '', '2000-01-01', 1, '', '', '',"
                         +          " '', '', '', 0);";
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.execute();
            
            //get ID added empty row
            String selectID = "SELECT IDENT_CURRENT('ATHLETE')";
            Statement stmt = DBC.createStatement();
            ResultSet rs = stmt.executeQuery(selectID);
            while (rs.next()) {
                athlete.setId(rs.getInt(1));
                System.out.println(rs.getInt(1));
            }       
          
        pstmt.close();
        stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(AthleteModel.class.getName()).log(Level.SEVERE,
                       "Not insert a new row into DB or not get row ID", ex);
        }        
    }
    
    //DEL_EMPTY_ROWS************************************************************
    public void delEmptyRows() {
        //delete from table
        try {            
            String query = "SELECT ID FROM ATHLETE "
                         + "WHERE Name = '' AND Surname = '' AND "
                         + "Middlename = '';";
            System.out.println(query);
            Statement stmt = DBC.createStatement();
            ResultSet rs = stmt.executeQuery(query); 
                        
            //get id rows for del
            int i = 1;
                        
            //if there are empty rows
            while (rs.next()) {                  
                for (int j = 0; j < getData().size(); j++) {                    
                    System.out.println(((Athlete) getDataByIndex(j)).getId());
                    if (((Athlete) getDataByIndex(j)).getId() == rs.getInt(i)) {
                        removeRow(j);  
                    }
                }                
            }
            //say user message
            JOptionPane.showMessageDialog(Manager.getAthPage(),
            "Пустые строки были удалены",
            "Информирование", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
        Logger.getLogger(AthleteModel.class.getName()).log(Level.SEVERE, 
                         "Cannot select empty rows", ex);
        }
        
        //delete from db
        try{           
            String queryDelDB = "DELETE FROM ATHLETE "
                    + "WHERE Name = '' AND Surname = '' AND Middlename = '';";
            System.out.println(queryDelDB);
            PreparedStatement pstmt = DBC.prepareStatement(queryDelDB);
            pstmt.execute();          
            
        } catch (SQLException ex) {
            Logger.getLogger(AthleteModel.class.getName()).log(Level.SEVERE, 
                             "Cannot delete empty rows", ex);
        }
    }
      
    //GETTERS*******************************************************************
    //can edit or not
    public boolean isCellEditable(int row, int column) {
		return getCellEditable();
    }    
    //get a link of Employee class with set data
    public ArrayList getAthleteDataLink() {
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
    public Athlete getDataByIndex(int i) {
        return (Athlete)data.get(i);
    }
    public ArrayList getData() {
        return data;
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
    public void setColumnNames(String str) {
        this.columnNames.add(str);
    }
    public void setColumnTypes(Class cls) {
        this.columnTypes.add(cls);
    }
    public void setData(Athlete data) {
        this.data.add(data);
    }
}
