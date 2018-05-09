package dataBase;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseConnection {
    
    private static DataBaseConnection DataBaseInstance = null;
    private String dbURL = "jdbc:sqlserver://91.123.24.45;databaseName=cascade;integratedSecurity=false;user=sa;password=1908London";
//    private String dbURL = "jdbc:sqlserver://localhost;instanceName=MSSQLSERVER;databaseName=TestDB;integratedSecurity=true;";
    private Connection DBconnection;
    
    private DataBaseConnection() { 
        //register sql drivaer
        try {
            DriverManager.registerDriver(new SQLServerDriver());
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).
                             log(Level.SEVERE,"no register driver", ex);
        }
        //connect to database
        try {
            DBconnection = DriverManager.getConnection(dbURL);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).
                             log(Level.SEVERE,"no connection to database", ex);   
        }        
    }    
    public static DataBaseConnection getInstanceDataBase() {
        if (DataBaseInstance == null)
            DataBaseInstance = new DataBaseConnection();
        return DataBaseInstance;           
    }
    public Connection getDBconnection() {
        return DBconnection;
    }    
}
