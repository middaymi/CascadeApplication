package models.Employee;

import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class EmployeeColumnModel {
    
    private JTable table;
    private TableColumnModel columnModel;
    
    public EmployeeColumnModel(JTable tbl) {
        this.table = tbl;
    }
    
    //table columns settings (setWidth, remove ID column, move some columns)
    public void setTableColumnsSettings() {
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        columnModel = table.getColumnModel();
        Enumeration e = columnModel.getColumns();
        //del ID column and exchange columns
        columnModel.removeColumn((TableColumn)columnModel.getColumn(0));
        columnModel.moveColumn(0, 1);
        columnModel.moveColumn(6, 4);
        render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        while (e.hasMoreElements()) {            
            TableColumn column = (TableColumn)e.nextElement();
            if (column.getHeaderValue() == "Опыт") {
               table.getColumn("Опыт").setCellRenderer(render);
               column.setPreferredWidth(50);
            } else if (column.getHeaderValue() == "ДР") {
               table.getColumn("ДР").setCellRenderer(render);
            } else if (column.getHeaderValue() == "Образование") {
                column.setPreferredWidth(500);
            }            
        }
        table.setColumnModel(columnModel);
    }       
}
