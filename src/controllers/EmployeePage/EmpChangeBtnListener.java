package controllers.EmployeePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Employee.EmployeeModel;
import views.Manager;

public class EmpChangeBtnListener implements ActionListener{ 
    Manager manager = Manager.getManagerInstance();
    private EmployeeModel employeeModel = EmployeeModel.getEmployeeModelInstance();
   
    boolean change = false;
    public void actionPerformed(ActionEvent e) {        
        if (manager.getEmpPage() != null) {
            if (change == true) {
                manager.getEmpPage().setBtnsMode(false);                
                change = false;
                employeeModel.delEmptyRows();
            } else {
            manager.getEmpPage().setBtnsMode(true);
            change = true;
            }
        }       
    }
}
