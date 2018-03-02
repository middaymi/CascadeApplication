package models.Employee;

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
import data.Employee;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import views.Manager;

public class EmployeeModel extends AbstractTableModel{

    private static EmployeeModel employeeModelInstance = null;
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection();  
    //edit or not the table
    private boolean editable;
        
    //DATA**********************************************************************
    private ArrayList <Employee> data = new ArrayList();  
        
    //HEADER********************************************************************
    //service array for original columnNames
    private ArrayList <String> enColumnNames = new ArrayList();    
    //table headers
    private String titles[] = {"ID", "Имя", "Фамилия", "Отчество", "ДР", 
                               "Опыт", "Образование", "Должность"};     
    //order like in sqlTable; then name, surname are swaped 
    private ArrayList <String> columnNames = new ArrayList();    
    //list of columns type 
    private ArrayList <Object> columnTypes = new ArrayList();     
    //**************************************************************************
    
    //constructor
    private EmployeeModel() {
        setEditable(true);      
    }
    
    //singletone, get an object link
    public static EmployeeModel getEmployeeModelInstance() {
        if (employeeModelInstance == null)
            employeeModelInstance = new EmployeeModel();
        return employeeModelInstance;
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
    String selectAllFromEmployee = "select * from employee";
    private ResultSet getDataFromDB() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(selectAllFromEmployee); 
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).
                             log(Level.SEVERE,"no executed "
                             + "query 'selectAllFromEmployee'", ex); 
        }
        return rs;
    }
    
    //CREATE VALUES|ROWS FOR TABLE**********************************************
    //get selectAllFromEmployee data from ResultSet, 
    //push it to the data storage (there:employee)
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
            //set values to Employee.class arraylist
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                //add original columnNames to serviceArray
                enColumnNames.add(rsmd.getColumnName(i+1));
                //add titles
                setColumnNames(titles[i]);
                //add columnType
                System.out.println(rsmd.getColumnClassName(i+1));
                if (rsmd.getColumnClassName(i+1).equals("java.sql.Date")) {
                    type = Class.forName("java.util.Date");
                }
                else {
                    type = Class.forName(rsmd.getColumnClassName(i+1));
                }
                
                setColumnTypes(type);                 
            } 
                                     
            //?something for table
            fireTableStructureChanged();
            
            //get row-data
            while (rs.next()) {
                //save a dataclass of a row
                Employee rowEmployee = new Employee();
                for (int i = 0; i < columnCount; i++) {                    
                    switch (enColumnNames.get(i)) {
                        case ("ID"):
                            rowEmployee.setId(rs.getInt(i + 1));
                            break;
                        case ("Name"):
                            rowEmployee.setName(rs.getString(i + 1));
                            break;                            
                        case ("Surname"):
                            rowEmployee.setSurname(rs.getString(i + 1));
                            break;                            
                        case ("Middlename"):
                            rowEmployee.setMiddlename(rs.getString(i + 1));
                            break;
                        case ("Birthday"):
                            //get sql.Date, convert to util.
                            java.util.Date date = new java.util.Date();
                            date.setTime(rs.getDate(i + 1).getTime());                                    
                            rowEmployee.setBirthday(date);
                            break;
                        case ("Experience"):
                            rowEmployee.setExperience(rs.getInt(i + 1));
                            break;
                        case ("Education"):
                            rowEmployee.setEducation(rs.getString(i + 1));
                            break;
                        case ("Post"):
                            rowEmployee.setPost(rs.getString(i + 1));
                            break;
                    }                 
                }
                synchronized (getData()) {                    
                    setData(rowEmployee);
                    //info about added row
                    fireTableRowsInserted(getData().size()-1, 
                                          getData().size()-1);
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).
                             log(Level.SEVERE, 
                             "ResultSet problem", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).
                             log(Level.SEVERE, 
                             "not define class type of data", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                        "not close DB connection in  setDataSource()", ex);
            }
        }        
    } 
     
    //GET_VALUE*****************************************************************
    public Object getValueAt(int row, int column) {        
        Employee empLink;        
        empLink = (Employee)getData().get(row);        
        String colName = enColumnNames.get(column);
        Object returnField = null;

        switch (colName) {
            case ("ID"): 
                returnField = empLink.getId();
                break;
            case ("Name"):
                returnField = empLink.getName(); 
                break;
            case ("Surname"):
                returnField = empLink.getSurname();                                                    
                break;
            case ("Middlename"):
                returnField = empLink.getMiddlename();                        
                break;
            case ("Birthday"):
                returnField = empLink.getBirthday();
                break;
            case ("Experience"):
                returnField = empLink.getExperience();                        
                break;
            case ("Education"):                        
                returnField = empLink.getEducation();
                break;
            case ("Post"):
                returnField = empLink.getPost();
                break;
        }                   
        return returnField;        
    }  
    
    //CATCH CHANGE CELL VALUE***************************************************
    public void setValueAt(Object value, int row, int column) {                        
        //will get an edited class
        //Employee row-exemplar
        Employee setClass = getDataByIndex(row);        
  
        String valueStr = "";
        
        switch (getEnColumnNames().get(column)) {                 
            case ("Name"):              
                valueStr = "'" + ((String)value).trim() + "'";
                setClass.setName(((String)value).trim());
                break;                            
            case ("Surname"):           
                valueStr = "'" + ((String)value).trim() + "'";
                setClass.setSurname(((String)value).trim());
                break;                            
            case ("Middlename"):
                valueStr = "'" + ((String)value).trim() + "'";
                setClass.setMiddlename(((String)value).trim());
                break;
            case ("Education"):
                valueStr = "'" + ((String)value).trim() + "'";
                setClass.setEducation(((String)value).trim());                        
                break;
            case ("Post"):
                valueStr = "'" + ((String)value).trim() + "'";
                setClass.setPost(((String)value).trim());
                break;
            case ("Birthday"):
                long date = ((java.util.Date)value).getTime();
                valueStr = "'" + new java.sql.Date(date) + "'";
                setClass.setBirthday((java.util.Date)value);                        
                break;
            case ("Experience"):
                valueStr = (String)value;
                setClass.setExperience((Integer)value);
                break;            
        }             
        updateData(row, column, valueStr);      
   }  
       
    //UPDATE FIELDS IN DB AFTER EDIT TABLE CELL*********************************
    public void updateData(int row, int column, String value) { 
        String query;
        PreparedStatement pstmt = null;
        try { 
          
            //create updateQuery 
            query = "UPDATE " + "EMPLOYEE " +
                    "SET " + enColumnNames.get(column) + " = " +
                    value + " WHERE ID = " + getValueAt(row, 0) + ";";
            
            System.out.println(query);
            //update
            pstmt = DBC.prepareStatement(query);
            pstmt.executeUpdate(); 
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).
                         log(Level.SEVERE, "Something wrong with SQLquery,"
                                         + "no update", ex);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                                "not close connection in updateData()", ex);
            }
        } 
    }
    
   //DELETE_ROW*****************************************************************  
   public void delSelectedRow() {
        int sel = 0;
        try {             
            JTable empTable = Manager.getEmpPage().getTable();
            //get selected row
            sel = empTable.getSelectedRow(); 
            System.out.println("sel " + sel);
                        
            //del a row from DB
            String query = "DELETE " + "FROM EMPLOYEE " +
                           "WHERE ID = " + getValueAt(sel, 0);
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query); 
            pstmt.executeUpdate();
            this.removeRow(sel);
           
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                            "Something wrong with deleting a row", ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (sel == -1) {    
                JOptionPane.showMessageDialog(Manager.getEmpPage(),
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
    public void addEmployee() {
        //get the least index, bottom add
        int rowIndex = getData().size();
        //create empty data class
        Employee newRow = new Employee();        
        newRow.setName("");
        newRow.setSurname("");
        newRow.setMiddlename("");
        //newRow.setBirthday();
        newRow.setExperience(0);
        newRow.setEducation("");
        newRow.setPost("");
        setData(newRow);        
        //change table view
        fireTableRowsInserted(rowIndex, rowIndex);
        //insert into db
        insertRowIntoTable(newRow);
    }
    
    private void insertRowIntoTable (Employee employee)  {
        Statement stmt = null;
        ResultSet rs;
        try {
            //add empty row
            String query = "INSERT INTO EMPLOYEE "
                         + "VALUES ('', '', '', GETDATE(), 0, '', '')";
            System.out.println(query);
            PreparedStatement pstmt = DBC.prepareStatement(query);
            pstmt.execute();
            
            //get ID added empty row
            String selectID = "SELECT IDENT_CURRENT('EMPLOYEE')";
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(selectID);
            while (rs.next()) {
                employee.setId(rs.getInt(1));
                System.out.println(rs.getInt(1));
            }       
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE,
                       "Not insert a new row into DB or not get row ID", ex);
        } finally {              
            try {
                stmt.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                        "no close DB connection in insertRowIntoTable()", ex);
            }
        }        
    }
    
    //DEL_EMPTY_ROWS************************************************************
    public void delEmptyRows() {
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        //delete from table
        try {            
            String query = "SELECT ID FROM EMPLOYEE "
                         + "WHERE Name = '' AND Surname = '';";
            System.out.println(query);
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(query); 
                        
            //get id rows for del
            int i = 1;
                        
            //if there are empty rows
            while (rs.next()) {                 
                for (int j = 0; j < getData().size(); j++) {                                        
                    if (((Employee) getDataByIndex(j)).getId() == rs.getInt(i)) {
                        removeRow(j);                        
                    }
                }
            }
            //say user message
            JOptionPane.showMessageDialog(Manager.getEmpPage(),
            "Пустые строки были удалены",
            "Информирование", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
        Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                         "Cannot select empty rows", ex);
        }
        
        //delete from db
        try{           
            String queryDelDB = "DELETE FROM EMPLOYEE "
                    + "WHERE Name = '' AND Surname = '' AND Middlename = '';";
            System.out.println(queryDelDB);
            pstmt = DBC.prepareStatement(queryDelDB);
            pstmt.execute();          
            
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                             "Cannot delete empty rows", ex);
        } finally {
            try {
                pstmt.close();
                stmt.close();
                rs.close();       
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeModel.class.getName()).log(Level.SEVERE, 
                                "not close DB connection in delEmptyRows()", ex);
            }      
        }       
    }
      
    //GETTERS*******************************************************************
    //can edit or not
    public boolean isCellEditable(int row, int column) {
		return getCellEditable();
    }    
    //get a link of Employee class with set data
    public ArrayList getEmployeeDataLink() {
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
    public Employee getDataByIndex(int i) {
        return (Employee)data.get(i);
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
    public void setData(Employee data) {
        this.data.add(data);
    }
}