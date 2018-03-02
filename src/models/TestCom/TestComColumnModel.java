package models.TestCom;

import java.util.Enumeration;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TestComColumnModel {
    
    private JTable table;
    private TableColumnModel columnModel;
    private DefaultTableCellRenderer renderCenter;
    private DefaultTableCellRenderer renderToolTipText;
    
    public TestComColumnModel(JTable tbl) {
        this.table = tbl; 
        setTableColumnsSettings();        
    }
    
    //table columns settings (setWidth, remove ID column, move some columns)
    public void setTableColumnsSettings() {
        renderCenter = new DefaultTableCellRenderer(); 
        renderToolTipText = new DefaultTableCellRenderer(); 
        columnModel = table.getColumnModel();        
        Enumeration e = columnModel.getColumns();
        //del ID column and exchange columns
        columnModel.removeColumn((TableColumn)columnModel.getColumn(1));
        columnModel.removeColumn((TableColumn)columnModel.getColumn(1));
        columnModel.removeColumn((TableColumn)columnModel.getColumn(6));
        columnModel.moveColumn(1, 0);        
        renderCenter.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        renderToolTipText.setToolTipText("Отметка ставится у внешнего соревнования");
        
        table.setColumnModel(columnModel);
        while (e.hasMoreElements()) {
            TableColumn column = (TableColumn)e.nextElement(); 
            if (column.getHeaderValue() == "Название" ||
                column.getHeaderValue() == "Тип" ||
                column.getHeaderValue() == "Дата и время")
                {column.setPreferredWidth(300);} 
            else if (column.getHeaderValue() == "Адрес" ||
                column.getHeaderValue() == "Описание")
                {column.setPreferredWidth(400);}         
        }
        //columnModel.getColumn(2).setHeaderRenderer(renderToolTipText);
        //combobox-seasons cell        
        columnModel.getColumn(1).setCellEditor
        (new DefaultCellEditor(TestComModel.getTestComModelInstance().getComboCompKind()));
    }    
}
