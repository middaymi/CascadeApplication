package models.Athlete;

import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import static utils.Layout.calcW;

public class AthleteColumnModel {
    //get a table
    JTable table;
    TableColumnModel columnModel;
    
    public AthleteColumnModel(JTable tbl) {
        this.table = tbl;
    }
    
    //table columns settings (setWidth, remove ID column, move some columns)
    public void setTableColumnsSettings() {
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        columnModel = table.getColumnModel();
        Enumeration e = columnModel.getColumns();
        //del ID column and exchange columns
        columnModel.removeColumn((TableColumn)columnModel.getColumn(0));
        columnModel.moveColumn(11, 3);
        columnModel.moveColumn(8, 6);
        columnModel.moveColumn(7, 8);
        //change columns surname, name        
        while (e.hasMoreElements()) {             
            TableColumn column = (TableColumn)e.nextElement(); 
            //set width for columns
            if (column.getHeaderValue() == "Имя"
               || column.getHeaderValue() == "Фамилия"
               || column.getHeaderValue() == "Отчество") 
                    {column.setPreferredWidth(calcW(230));}
            else if (column.getHeaderValue() == "ДР")
                    {column.setPreferredWidth(calcW(170));}
            else if (column.getHeaderValue() == "Разряд"
                    || column.getHeaderValue() == "Пол")
                    {column.setPreferredWidth(calcW(120));
                    render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
                    table.getColumn("Разряд").setCellRenderer(render);}            
            else if (column.getHeaderValue() == "Представитель")
                    {column.setPreferredWidth(calcW(500));}
            else if (column.getHeaderValue() == "Телефон")
                    {column.setPreferredWidth(calcW(240));}
            else if (column.getHeaderValue() == "Адрес")
                    {column.setPreferredWidth(calcW(600));}
            else if (column.getHeaderValue() == "Документ"
                    || column.getHeaderValue() == "Сертификат"
                    || column.getHeaderValue() == "Страховка")
                    {column.setPreferredWidth(calcW(180));}
        }
        table.setColumnModel(columnModel);
    }
}
