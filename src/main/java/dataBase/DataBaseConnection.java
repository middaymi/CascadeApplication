package dataBase;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseConnection {
    
    private static DataBaseConnection DataBaseInstance = null;
    private String dbURL = "jdbc:sqlserver://localhost;instanceName=MSSQLSERVER;databaseName=TestDB;integratedSecurity=true;";
    private Connection DBconnection;
    private final String PROPERTIES_PATH = "application.properties";

    private Properties prop = new Properties();

    private void loadProperties() {
        try {
            InputStream in = new FileInputStream(PROPERTIES_PATH);
            prop.load(in);
            in.close();

            dbURL = "jdbc:sqlserver://" + prop.getProperty("db.connection") +
                    ";databaseName=" + prop.getProperty("db.name") +
                    ";integratedSecurity=" + prop.getProperty("db.integratedSecurity") +
                    ";user=" + prop.getProperty("db.username") +
                    ";password=" + prop.getProperty("db.password");
        } catch (IOException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).
                    log(Level.SEVERE,"no database properties, use default properties for localhost", ex);
        }
    }

    private DataBaseConnection() {

        loadProperties();


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
