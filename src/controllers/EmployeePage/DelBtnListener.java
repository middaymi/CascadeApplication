package controllers.EmployeePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Employee.EmployeeModel;
import views.Manager;

public class DelBtnListener implements ActionListener { 
    private Manager manager = Manager.getManagerInstance();
    private EmployeeModel employeeModel = EmployeeModel.getEmployeeModelInstance();          
    public void actionPerformed(ActionEvent e) {                
        if (manager.getEmpPage() != null) {
                employeeModel.delSelectedRow();
        } 
    }
}

