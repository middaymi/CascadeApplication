package views.TestCom;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import models.TestCom.TestComModel;
import views.CommonSettings;
import views.Manager;

public class SingleEditPage extends JPanel {
    private JLabel welcome;
    private Manager manager;
    private TestComModel tcModel;
    private TestComPage tcPage;
    int sel;
         
    private JLabel athl;
    private JLabel jud;
    private JLabel rank;
        
    private JList athlLst;
    private JList judLst;    
    private DefaultListModel athlLstModel;
    private DefaultListModel judLstModel;
        
    private JButton setRankBtn;    
    private JButton athlDelBtn;
    private JButton judDelBtn;        
    private JButton athlAddBtn;
    private JButton judAddBtn;
        
    private JComboBox athlCombo;
    private JComboBox judCombo;
    private JComboBox rankCombo;
    
    public SingleEditPage() {
        CommonSettings.panelSettings(this);
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();
        tcPage = manager.getTestCompPage();
        sel = tcModel.selRow();
        
        //create labels
        createWelcomeLabel();        
        createLabel(1, athl);
        createLabel(2, jud);
        createLabel(3, rank);
                        
        createList(1, athlLst);
        createList(2, judLst); 
                
        createDelBtn(1, athlDelBtn);
        createDelBtn(2, judDelBtn);
        
        createComboAndBtn(1, athlCombo, athlAddBtn);
        createComboAndBtn(2, judCombo, judAddBtn);
        createRanksComponents();
    }

    private void createWelcomeLabel() {
        String str = tcModel.getValueAt(sel, 0) + ". " +
                     tcModel.getValueAt(sel, 3);
        welcome = new JLabel(str);
        welcome.setSize(800, 70);
        welcome.setLocation(2084, 30);
        welcome.setVisible(true);
        welcome.setOpaque(true);
        welcome.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingFont30(welcome);
        CommonSettings.settingGrayBorder(welcome);
        this.add(welcome);
    }
    
     private void createLabel(int i, JLabel lbl) {
        lbl = new JLabel();        
        if (i == 1) lbl.setText("Список спортсменов");
        if (i == 2) lbl.setText("Список судей");
        if (i == 3) lbl.setText("<html>Разряды <p>" + 
                                "Для подтверждения выбора разряда нажмите ✔.</html>");
        lbl.setVisible(true);
        lbl.setSize(800, 100);
        lbl.setLocation(284 + (i-1)*900, 200);
        CommonSettings.settingFont30(lbl);
        CommonSettings.settingGrayBorder(lbl);
        lbl.setOpaque(true);
        lbl.setBackground(Color.LIGHT_GRAY);
        this.add(lbl);
    }
    
    private void createList(int i, JList lst) {
        lst = new JList();
        CommonSettings.settingFont30(lst);
        lst.setVisible(true);        
        lst.setBackground(Color.WHITE);
        lst.setSelectedIndex(0);
        lst.setFocusable(false);  
        
        JScrollPane scrl = new JScrollPane(lst);
        scrl.setSize(800, 950);
        scrl.setLocation(284 + (i-1)*900, 310);
        this.add(scrl);
        if (i == 1) {
            athlLstModel = new DefaultListModel();
            lst.setModel(athlLstModel);
            athlLst = lst;
        }
        else if (i == 2) {
            judLstModel = new DefaultListModel();
            lst.setModel(judLstModel);
            judLst = lst;
        }
    } 
    
    private void createDelBtn(int i, JButton btn) {
        btn = new JButton("-");
        CommonSettings.settingFont30(btn);
        btn.setFocusable(false);
        btn.setSize(100, 100);
        btn.setLocation(284 + i*900 - 200, 1380);
        btn.setBackground(Color.LIGHT_GRAY);
        this.add(btn);
        if (i == 1) {
            athlDelBtn = btn;
            athlDelBtn.addActionListener(new controllers.TestComEditPages.
                                             SingleEditPage.DelAthlete());
        }
        else if (i == 2) {
            judDelBtn = btn;
            judDelBtn.addActionListener(new controllers.TestComEditPages.
                                              SingleEditPage.DelJudge());
        }
    }
    
    private void createComboAndBtn(int i, JComboBox cmb, JButton btn) {
        //combo
        cmb = new JComboBox();        
        CommonSettings.settingFont30(cmb);
        cmb.setEditable(false);
        cmb.setSize(690, 100);
        cmb.setLocation(284 + (i-1)*900, 1270);
        this.add(cmb);        
        if (i == 1) athlCombo = cmb;
        else if (i == 2) judCombo = cmb;
        
        //btns
        btn = new JButton("+");      
        btn.setFocusable(false);
        btn.setSize(100, 100);
        btn.setLocation(284 + i*900 - 200, 1270);
        CommonSettings.settingFont30(btn);
        btn.setBackground(Color.LIGHT_GRAY);
        this.add(btn); 
        if (i == 1) {
            athlAddBtn = btn;
            athlAddBtn.addActionListener(new controllers.TestComEditPages.
                                             SingleEditPage.AddAthlete());
        }
        else if (i == 2) {
            judAddBtn = btn;
            judAddBtn.addActionListener(new controllers.TestComEditPages.
                                              SingleEditPage.AddJudge());
        }        
    }
    
    private void createRanksComponents() {
        //combo
        rankCombo = new JComboBox();        
        CommonSettings.settingFont30(getRankCombo());
        getRankCombo().setEditable(false);
        getRankCombo().setSize(690, 100);
        getRankCombo().setLocation(2084, 320);        
        this.add(getRankCombo());        
        
        //btn
        setRankBtn = new JButton("✔");      
        setRankBtn.setFocusable(false);
        setRankBtn.setSize(100, 100);
        setRankBtn.setLocation(2784, 320);
        CommonSettings.settingFont30(setRankBtn);
        setRankBtn.setBackground(Color.LIGHT_GRAY);
        this.add(setRankBtn); 
        setRankBtn.addActionListener(new controllers.TestComEditPages.
                                             SingleEditPage.SetRank());
    }

    //GETTERS*******************************************************************
    public JList getAthlLst() {
        return athlLst;
    }
    public JList getJudLst() {
        return judLst;
    }
    public DefaultListModel getAthlLstModel() {
        return athlLstModel;
    }
    public DefaultListModel getJudLstModel() {
        return judLstModel;
    }
    public JComboBox getAthlCombo() {
        return athlCombo;
    }
    public JComboBox getJudCombo() {
        return judCombo;
    }

    //SETTERS*******************************************************************
    public void setAthlLst(JList athlLst) {
        this.athlLst = athlLst;
    }
    public void setJudLst(JList judLst) {
        this.judLst = judLst;
    }

    public void setAthlLstModel(DefaultListModel athlLstModel) {
        this.athlLstModel = athlLstModel;
    }
    public void setJudLstModel(DefaultListModel judLstModel) {
        this.judLstModel = judLstModel;
    }

    public void setAthlCombo(JComboBox athlCombo) {
        this.athlCombo = athlCombo;
    }
    public void setJudCombo(JComboBox judCombo) {
        this.judCombo = judCombo;
    }

    public JComboBox getRankCombo() {
        return rankCombo;
    }
}
