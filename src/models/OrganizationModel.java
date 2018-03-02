package models;

import data.Organization;
import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import views.Manager;
import views.OrganizationPage;

public class OrganizationModel {
    
    private static OrganizationModel organizationModelInstance = null;
    private Organization organization = new Organization();
    private Manager manager =  Manager.getManagerInstance();
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection();
    //get a link for other objects
    private OrganizationModel() {}    
    public static OrganizationModel getOrganizationModelInstance() {
        if (organizationModelInstance == null)
            organizationModelInstance = new OrganizationModel();
        return organizationModelInstance;
    } 
    
    //select all information about organization from DB
    String selectAllFromOrganization = "select * from organization";   
    private ResultSet getOrganizationFields() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = DBC.createStatement();
            rs = stmt.executeQuery(selectAllFromOrganization); 
        } catch (SQLException ex) {
            Logger.getLogger(OrganizationModel.class.getName()).
                             log(Level.SEVERE,"no executed "
                             + "query 'selectAllFromOrganization'", ex); 
        }
        return rs;
    }
    
    //fill data about organization
    public Organization getOrganizationData() {                  
         ResultSet res = getOrganizationFields();        
         try {                        
            while (res.next()) {
                organization.setFullName(res.getString("fullName"));
                organization.setPhoneNumber(res.getString("PhoneNumber"));
                organization.setSite(res.getString("Site"));
                organization.seteMail(res.getString("EMail"));
                organization.setOGRN(res.getString("OGRN"));
                organization.setKPP(res.getString("KPP"));
                organization.setINN(res.getString("INN"));
                organization.setAccount(res.getString("Account"));
                organization.setLegalAddress(res.getString("LegalAddress"));
                organization.setActualAddress(res.getString("ActualAddress")); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrganizationPage.class.getName()).
                             log(Level.SEVERE, "no setText on textFields", ex);
        }
        return organization;
    }
  
    //update organization data
    String updateOrganization = 
            "update organization set FullName = ?, PhoneNumber = ?, Site = ?, "
                                  + "EMail = ?, OGRN = ?, KPP = ?, INN = ?, "
                                  + "Account = ?, LegalAddress = ?, "
                                  + "ActualAddress = ?";  
    public void updateOrganizationFields() {
        PreparedStatement psOrganization;
        try {
            psOrganization = DBC.prepareStatement(updateOrganization) ;
            psOrganization.setString(1, organization.getFullName());
            psOrganization.setString(2, organization.getPhoneNumber());
            psOrganization.setString(3, organization.getSite());
            psOrganization.setString(4, organization.geteMail());
            psOrganization.setString(5, organization.getOGRN());
            psOrganization.setString(6, organization.getKPP());
            psOrganization.setString(7, organization.getINN());
            psOrganization.setString(8, organization.getAccount());
            psOrganization.setString(9, organization.getLegalAddress());
            psOrganization.setString(10, organization.getActualAddress());
            psOrganization.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrganizationModel.class.getName()).
                             log(Level.SEVERE, "no update Organization", ex);
        }        
    }

    //check some textFields values     
    public void checkTextFields() {
        //fullName, phoneNumber, site, eMail, ogrnaccount, legalAddress, 
        //actualAddress, kpp, inn are not checking 
        if (manager.getOrgPage().getPhoneNumberField().getText().length() > 35) {
            JOptionPane.showMessageDialog(manager.getOrgPage(),
            "Проверьте значение поля 'Контактный номер', "
            + "не должно быть более 35 символов, включая знаки разделения",
            "Ошибка", JOptionPane.WARNING_MESSAGE);
            manager.getOrgPage().getPhoneNumberField().
                    setText(organization.getPhoneNumber());
        }
        if (manager.getOrgPage().getOgrnField().getText().length() > 13) {
            JOptionPane.showMessageDialog(manager.getOrgPage(),
            "Проверьте значение поля 'ОГРН', "
            + "не должно быть более 13 символов",
            "Ошибка", JOptionPane.WARNING_MESSAGE);
            manager.getOrgPage().getOgrnField().
                    setText(organization.getOGRN());
        }
        if (manager.getOrgPage().getKppField().getText().length() > 9) {
            JOptionPane.showMessageDialog(manager.getOrgPage(),
            "Проверьте значение поля 'КПП', "
            + "не должно быть более 9 символов",
            "Ошибка", JOptionPane.WARNING_MESSAGE);
            manager.getOrgPage().getKppField().
                    setText(organization.getKPP());
        }
        if (manager.getOrgPage().getInnField().getText().length() > 12) {
            JOptionPane.showMessageDialog(manager.getOrgPage(),
            "Проверьте значение поля 'ИНН', "
            + "не должно быть более 12 символов",
            "Ошибка", JOptionPane.WARNING_MESSAGE);
            manager.getOrgPage().getInnField().
                    setText(organization.getINN());
        }           
    }
}
