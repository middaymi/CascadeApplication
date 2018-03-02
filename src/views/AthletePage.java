package views;

import models.Athlete.AthleteColumnModel;
import models.Athlete.AthleteModel;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import models.MultiLineCell;

public class AthletePage extends JPanel {
    
    private ArrayList athData;
    private AthleteModel athModel;
    private JScrollPane scrlPane;
    private JTable table;
    private JButton changeBtn;
    private JButton delBtn;
    private JButton addBtn;
    private AthleteColumnModel acm;
       
    public AthletePage() { 
        CommonSettings.panelSettings(this); 
        athModel = AthleteModel.getAthleteModelInstance(); 
        athData = athModel.getAthleteDataLink();
        setTableSettings();       
        setScrlPaneSettings();
        //display the result
        athModel.setDataSource(); 
        //set columnModel for table
        acm = new AthleteColumnModel(table); 
        acm.setTableColumnsSettings();
        setChangeBtnSettings();
        setDelBtnSettings();
        setAddBtnSettings(); 
        //set a sort
        table.setRowSorter(new TableRowSorter(athModel));
    }
    //TABLE*********************************************************************
    //table settings
    private void setTableSettings() {
        table = new JTable(athModel);
        table.setVisible(true);
        table.setOpaque(true);
        table.setRowHeight(90);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setEnabled(false);
        CommonSettings.settingFontBold30(table.getTableHeader());
        CommonSettings.settingFont30(table);
        table.setDefaultRenderer(String.class, new MultiLineCell());
    }    
    public JTable getTable() {
        return this.table;
    }
    
    //SCROLL_PANE***************************************************************
    //scroll pane settings
    private void setScrlPaneSettings() {
        scrlPane = new JScrollPane(table);        
        scrlPane.setVisible(true);
        //color
        //scrlPane.setBackground(new Color(80, 80, 80, 30));
        //scrlPane.getViewport().setBackground(new Color(80, 80, 80, 30));
                
        scrlPane.setSize(3000, 1190);
        scrlPane.setLocation(84, 230);
        this.add(scrlPane);
    }  
    
    //BUTTONS*******************************************************************
    private void setChangeBtnSettings() {
        changeBtn = new JButton("Изменить");        
        changeBtn.setBackground(Color.LIGHT_GRAY);
        changeBtn.setSize(250, 100);
        changeBtn.setLocation(2834, 1440);
        CommonSettings.settingFont30(changeBtn);
        this.add(changeBtn);
        changeBtn.addActionListener(new controllers.AthletePage.
                                        AthChangeBtnListener());                                        
    }    
    private void setDelBtnSettings() {
        delBtn = new JButton("Удалить");        
        delBtn.setBackground(Color.LIGHT_GRAY);
        delBtn.setSize(250, 100);
        delBtn.setLocation(2554, 1440);
        delBtn.setVisible(false);
        CommonSettings.settingFont30(delBtn);
        this.add(delBtn);
        delBtn.addActionListener(new controllers.AthletePage.
                                     DelBtnListener());
    }     
    private void setAddBtnSettings() {
        addBtn = new JButton("Добавить");        
        addBtn.setBackground(Color.LIGHT_GRAY);
        addBtn.setSize(250, 100);
        addBtn.setLocation(2274, 1440);
        addBtn.setVisible(false);
        CommonSettings.settingFont30(addBtn);
        this.add(addBtn);
        addBtn.addActionListener(new controllers.AthletePage.
                                     AddBtnListener());
    }     
    public void setBtnsMode(boolean mode) {
        //editable or not regime
        if (mode == true) {changeBtn.setText("Выйти");}
        else {changeBtn.setText("Изменить");}
        delBtn.setVisible(mode);
        addBtn.setVisible(mode);
        table.setEnabled(mode); 
    }
}
