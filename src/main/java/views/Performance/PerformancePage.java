package views.Performance;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import models.Performance.PerformanceColumnModel;
import models.Performance.PerformanceModel;
import views.CommonSettings;

import static utils.Layout.calcH;
import static utils.Layout.calcW;

public class PerformancePage extends JPanel {
    
    private ArrayList perData;
    private PerformanceModel perModel;
    private JScrollPane scrlPane;
    private JTable table;
    private PerformanceColumnModel pcm;
    private JButton changeBtn;
    private JButton delBtn;
    private JButton addBtn;
    private JButton editBtn;
       
    public PerformancePage() { 
        CommonSettings.panelSettings(this); 
        perModel = PerformanceModel.getPerformanceModelInstance();
        perData = perModel.getEmployeeDataLink();
        setTableSettings();       
        setScrlPaneSettings();             
        perModel.setDataSource(); //display the result
        pcm = new PerformanceColumnModel(table);
        pcm.setTableColumnsSettings();
        
        //btns
        setChangeBtnSettings();
        setDelBtnSettings();
        setAddBtnSettings();  
        setEditBtnSettings();
    }
    
    //TABLE*********************************************************************
    //table settings
    private void setTableSettings() {
        table = new JTable(perModel);
        table.setVisible(true);
        table.setOpaque(true);
        table.setRowHeight(calcH(50));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setEnabled(false);
        CommonSettings.settingFontBold30(table.getTableHeader());
        CommonSettings.settingFont30(table);
        table.setRowSorter(new TableRowSorter(perModel));        
    } 
    public JTable getTable() {
        return this.table;
    }
    
    //SCROLL_PANE***************************************************************
    //scroll pane settings
    private void setScrlPaneSettings() {
        scrlPane = new JScrollPane(table);        
        scrlPane.setVisible(true);
        scrlPane.setSize(calcW(2000), calcH(1180));
        scrlPane.setLocation(calcW(584), calcH(230));
        this.add(scrlPane);
    } 
    
    //BUTTONS*******************************************************************
    private void setChangeBtnSettings() {
        changeBtn = new JButton("Изменить");        
        changeBtn.setBackground(Color.LIGHT_GRAY);
        changeBtn.setSize(calcW(250), calcH(100));
        changeBtn.setLocation(calcW(2334), calcH(1440));
        CommonSettings.settingFont30(changeBtn);
        this.add(changeBtn);
        changeBtn.addActionListener(new controllers.PerformancePage.
                                        ChangeBtnListener());
    }  
        private void setEditBtnSettings() {
        editBtn = new JButton("Редактировать");        
        editBtn.setBackground(Color.LIGHT_GRAY);
        editBtn.setSize(calcW(250), calcH(100));
        editBtn.setLocation(calcW(2054), calcH(1440));
        editBtn.setVisible(false);
        CommonSettings.settingFont30(editBtn);
        this.add(editBtn);       
        editBtn.addActionListener(new controllers.PerformancePage.
                                        EditBtnListener());
    }     
    private void setDelBtnSettings() {
        delBtn = new JButton("Удалить");        
        delBtn.setBackground(Color.LIGHT_GRAY);
        delBtn.setSize(calcW(250), calcH(100));
        delBtn.setLocation(calcW(1774), calcH(1440));
        delBtn.setVisible(false);
        CommonSettings.settingFont30(delBtn);
        this.add(delBtn);
        delBtn.addActionListener(new controllers.PerformancePage.
                                     DelBtnListener());
    }     
    private void setAddBtnSettings() {
        addBtn = new JButton("Добавить");        
        addBtn.setBackground(Color.LIGHT_GRAY);
        addBtn.setSize(calcW(250), calcH(100));
        addBtn.setLocation(calcW(1494), calcH(1440));
        addBtn.setVisible(false);
        CommonSettings.settingFont30(addBtn);
        this.add(addBtn);
        addBtn.addActionListener(new controllers.PerformancePage.
                                     AddBtnListener());
    }     
    public void setBtnsMode(boolean mode) {
        //editable or not regime
        if (mode == true) {changeBtn.setText("Выйти");}
        else {changeBtn.setText("Изменить");}
        delBtn.setVisible(mode);
        addBtn.setVisible(mode);
        editBtn.setVisible(mode);
        table.setEnabled(mode); 
    }
}
