package views.Performance;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import views.CommonSettings;

public class PerformanceEditPage extends JPanel { 
   
    //panel's components
    //scrl for a list
    private DefaultListModel listModel;
    private JList list = null;    
    private JScrollPane scrl = null;
    private JComboBox athletesComboBox;
    
    //athletes
    private JLabel athleteLbl;
    private JButton delAthleteBtn;
    private JButton addAthleteBtn;    
    
    //season
    private JLabel seasonLbl;
    private JButton setSeasonBtn;
    private JComboBox seasonComboBox;
    private JButton addSeasonBtn;
    private JButton delSeasonBtn; 
    
    //text
    private JTextField textField;
    
    //edit row of performance
    private int editingRow;
       
    public PerformanceEditPage() {        
        //panel settings
        CommonSettings.panelSettings(this);
        listModel = new DefaultListModel();        
        
        //athletes
        labelAthletes();
        listAthletes();
        scrollAthletes();
        comboAthletes();
        addBtnAthletes();
        delBtnAthletes();
        
        //season
        labelSeason();
        comboSeason(); 
        chooseBtnSeason();
        addBtnSeason();
        delBtnSeason();
        
        setTextField();
    }
            
    //ATHLETE*******************************************************************
    private void labelAthletes() {
        athleteLbl = new JLabel("Список спортсменов-участников");
        athleteLbl.setSize(800, 100);
        athleteLbl.setLocation(400, 200);
        CommonSettings.settingFont30(athleteLbl);
        CommonSettings.settingGrayBorder(athleteLbl);
        athleteLbl.setOpaque(true);
        athleteLbl.setBackground(Color.LIGHT_GRAY);
        this.add(athleteLbl);
    } 
    
    private void listAthletes() {
        setListModel(new DefaultListModel());
        setList(new JList());        
        CommonSettings.settingFont30(getList());
        getList().setVisible(true);        
        getList().setBackground(Color.WHITE);
        getList().setSelectedIndex(0);
        getList().setFocusable(false);    
    }
    
    //scroll for a list
    private void scrollAthletes() {
        scrl = new JScrollPane(getList());
        scrl.setSize(800, 950);
        scrl.setLocation(400, 310);
        this.add(scrl);    
    }
    
    private void comboAthletes() {
        //combobox for choise athlete
        setAthletesComboBox(new JComboBox());
        CommonSettings.settingFont30(getAthletesComboBox());
        getAthletesComboBox().setEditable(true);
        getAthletesComboBox().setSize(690, 100);
        getAthletesComboBox().setLocation(400, 1270);
        this.add(getAthletesComboBox());
    }
    
    private void addBtnAthletes() {
        //button: add athlete to performance
        addAthleteBtn = new JButton("+");
        addAthleteBtn.setFocusable(false);
        addAthleteBtn.setSize(100, 100);
        addAthleteBtn.setLocation(1100, 1270);
        CommonSettings.settingFont30(addAthleteBtn);
        addAthleteBtn.setBackground(Color.LIGHT_GRAY);
        addAthleteBtn.addActionListener(new controllers.PerformanceEditPage.
                                                        AddAthleteListener());
        this.add(addAthleteBtn);   
    }
    
    private void delBtnAthletes() {
        //button: delete athlete from performance
        delAthleteBtn = new JButton("-");
        CommonSettings.settingFont30(delAthleteBtn);
        getDelAthleteBtn().setFocusable(false);
        getDelAthleteBtn().setSize(100, 100);
        getDelAthleteBtn().setLocation(1100, 1380);
        getDelAthleteBtn().setBackground(Color.LIGHT_GRAY);
        getDelAthleteBtn().addActionListener(new controllers.PerformanceEditPage.
                                                        DelAthleteListener());
        this.add(getDelAthleteBtn());
        //if athlete not chosen, not enable btn del       
    }
    
    //SEASON********************************************************************
    private void labelSeason() {
        seasonLbl = new JLabel("<html>Настройка сезона постановки. " +
                               "<p align=center> " +
                               "Для подтверждения смены сезона нажмите ✔. " +
                               "<html>");
        seasonLbl.setSize(800, 100);
        seasonLbl.setLocation(1300, 200);
        CommonSettings.settingFont30(seasonLbl);
        CommonSettings.settingGrayBorder(seasonLbl);
        seasonLbl.setOpaque(true);
        seasonLbl.setBackground(Color.LIGHT_GRAY);
        this.add(seasonLbl);
    }
    
    private void comboSeason() {
        seasonComboBox = new JComboBox();
        seasonComboBox.setEditable(false);
        CommonSettings.settingFont30(getSeasonComboBox());
        seasonComboBox.setSize(690, 100);
        seasonComboBox.setLocation(1300, 310);
        this.add(seasonComboBox);
    }
    
    private void chooseBtnSeason() {
        setSeasonBtn = new JButton("✔");
        setSeasonBtn.setFocusable(false);
        setSeasonBtn.setSize(100, 100);
        setSeasonBtn.setLocation(2000, 310);
        CommonSettings.settingFont30(setSeasonBtn);
        setSeasonBtn.setBackground(Color.LIGHT_GRAY);
        setSeasonBtn.addActionListener(new controllers.PerformanceEditPage.
                                                       SetSeasonListener());
        this.add(setSeasonBtn);
    }
    
    private void addBtnSeason() {
        addSeasonBtn = new JButton("Добавить сезон");
        addSeasonBtn.setFocusable(false);
        addSeasonBtn.setSize(340, 100);
        addSeasonBtn.setLocation(1300, 420);
        CommonSettings.settingFont30(addSeasonBtn);
        addSeasonBtn.setBackground(Color.LIGHT_GRAY);
        addSeasonBtn.addActionListener(new controllers.PerformanceEditPage.
                                                              AddSeasonBtnListener());
        this.add(addSeasonBtn);
    }
    
    private void delBtnSeason() {
        delSeasonBtn = new JButton("Удалить сезон");
        CommonSettings.settingFont30(delSeasonBtn);
        delSeasonBtn.setFocusable(false);
        delSeasonBtn.setSize(340, 100);
        delSeasonBtn.setLocation(1650, 420);
        delSeasonBtn.setBackground(Color.LIGHT_GRAY);
        delSeasonBtn.addActionListener(new controllers.PerformanceEditPage.
                                                    DelSeasonBtnListener());
        this.add(delSeasonBtn);   
    }
    
    //text field for add season
    private void setTextField() {
        textField = new JTextField();
        textField.setSize(690, 100);
        textField.setLocation(1300, 530);
        textField.setVisible(false);
        this.add(textField);  
        textField.addActionListener(new controllers.PerformanceEditPage.AddSeasonCB());
    }
    
    public JList getList() {
        return list;
    }
    public DefaultListModel getListModel() {
        return listModel;
    } 
    public JComboBox getAthletesComboBox() {
        return athletesComboBox;
    }
    public JComboBox getSeasonComboBox() {
        return seasonComboBox;
    }
    public int getEditingRow() {
        return editingRow;
    }

    public void setList(JList list) {
        this.list = list;
    }
    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }
    public void setAthletesComboBox(JComboBox athletesComboBox) {
        this.athletesComboBox = athletesComboBox;
    }
    public void setSeasonComboBox(JComboBox seasonComboBox) {
        this.seasonComboBox = seasonComboBox;
    }
    public void setEditingRow(int editingRow) {
        this.editingRow = editingRow;
    }
    public JButton getDelAthleteBtn() {
        return delAthleteBtn;
    }
    public JTextField getTextField() {
        return textField;
    }
}
