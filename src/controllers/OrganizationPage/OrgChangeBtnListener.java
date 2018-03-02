package controllers.OrganizationPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.Manager;
import models.OrganizationModel;

public class OrgChangeBtnListener implements ActionListener{ 
    Manager manager = Manager.getManagerInstance();
    OrganizationModel organizationModel = OrganizationModel.getOrganizationModelInstance();
    boolean change = false;
    public void actionPerformed(ActionEvent e) {        
        if (manager.getOrgPage() != null) {
            if (change == true) {
                manager.getOrgPage().changeTextButton(false);
                organizationModel.checkTextFields();
                manager.getOrgPage().setFields();
                organizationModel.updateOrganizationFields();
                manager.getOrgPage().setEditableTextFiels(false);
                change = false;
            } else {
            manager.getOrgPage().setEditableTextFiels(true);
            manager.getOrgPage().changeTextButton(true);
            change = true;
            }
        }       
    }
}