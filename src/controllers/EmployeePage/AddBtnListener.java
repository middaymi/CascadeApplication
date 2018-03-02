package controllers.EmployeePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Employee.EmployeeModel;
import views.Manager;

public class AddBtnListener implements ActionListener { 
    private Manager manager = Manager.getManagerInstance();
    private EmployeeModel employeeModel;
    public void actionPerformed(ActionEvent e) {
        employeeModel = EmployeeModel.getEmployeeModelInstance(); 
        if (manager.getEmpPage() != null) {
            employeeModel.addEmployee();
        } 
    }
}
