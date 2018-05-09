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

import static utils.Layout.calcH;
import static utils.Layout.calcW;

public class GlasialEditPage extends JPanel {
private JLabel welcome;
    private Manager manager;
    private TestComModel tcModel;
    private TestComPage tcPage;
    int sel;
    
    private JLabel el; 
    private JLabel athl;
    private JLabel jud;
    
    private JList elLst;
    private JList athlLst;
    private JList judLst;
    private DefaultListModel elLstModel;
    private DefaultListModel athlLstModel;
    private DefaultListModel judLstModel;
    
    private JButton elDelBtn;
    private JButton athlDelBtn;
    private JButton judDelBtn;
    
    private JButton elAddBtn;
    private JButton athlAddBtn;
    private JButton judAddBtn;
    
    private JComboBox elCombo;
    private JComboBox athlCombo;
    private JComboBox judCombo;
    
    public GlasialEditPage() {
        CommonSettings.panelSettings(this);
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();
        tcPage = manager.getTestCompPage();
        sel = tcModel.selRow();
        
        //create labels
        createWelcomeLabel();
        createLabel(1, el);
        createLabel(2, athl);
        createLabel(3, jud);
        
        //create lists in scrollpane
        createList(1, elLst);
        createList(2, athlLst);
        createList(3, judLst); 
        
        createDelBtn(1, elDelBtn);
        createDelBtn(2, athlDelBtn);
        createDelBtn(3, judDelBtn);
        
        createComboAndBtn(1, elCombo, elAddBtn);
        createComboAndBtn(2, athlCombo, athlAddBtn);
        createComboAndBtn(3, judCombo, judAddBtn);
    }

  private void createWelcomeLabel() {
        String str = tcModel.getValueAt(sel, 0) + ". " +
                     tcModel.getValueAt(sel, 3);
        welcome = new JLabel(str);
        welcome.setSize(calcW(800), calcH(70));
        welcome.setLocation(calcW(2084), calcH(30));
        welcome.setVisible(true);
        welcome.setOpaque(true);
        welcome.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingFont30(welcome);
        CommonSettings.settingGrayBorder(welcome);
        this.add(welcome);
    }
    
     private void createLabel(int i, JLabel lbl) {
        lbl = new JLabel();
        if (i == 1) lbl.setText("Список элементов");
        if (i == 2) lbl.setText("Список спортсменов");
        if (i == 3) lbl.setText("Список судей");
        lbl.setVisible(true);
        lbl.setSize(calcW(800), calcH(100));
        lbl.setLocation(calcW(284 + (i-1)*900), calcH(200));
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
        scrl.setSize(calcW(800), calcH(950));
        scrl.setLocation(calcW(284 + (i-1)*900), calcH(310));
        this.add(scrl);
        if (i == 1) {
            elLstModel = new DefaultListModel();            
            lst.setModel(elLstModel);
            elLst = lst;
        }
        else if (i == 2) {
            athlLstModel = new DefaultListModel();
            lst.setModel(athlLstModel);
            athlLst = lst;
        }
        else if (i == 3) {
            judLstModel = new DefaultListModel();
            lst.setModel(judLstModel);
            judLst = lst;
        }
    } 
    
    private void createDelBtn(int i, JButton btn) {
        btn = new JButton("-");
        CommonSettings.settingFont30(btn);
        btn.setFocusable(false);
        btn.setSize(calcW(100), calcH(100));
        btn.setLocation(calcW(284 + i*900 - 200), calcH(1380));
        btn.setBackground(Color.LIGHT_GRAY);
        this.add(btn);
        if (i == 1) {
            elDelBtn = btn;
            elDelBtn.addActionListener(new controllers.TestComEditPages.
                                          GlasialEditPage.DelElement());
        }
        else if (i == 2) {
            athlDelBtn = btn;
            athlDelBtn.addActionListener(new controllers.TestComEditPages.
                                             GlasialEditPage.DelAthlete());
        }
        else if (i == 3) {
            judDelBtn = btn;
            judDelBtn.addActionListener(new controllers.TestComEditPages.
                                            GlasialEditPage.DelJudge());
        }
    }
    
    private void createComboAndBtn(int i, JComboBox cmb, JButton btn) {
        //combo
        cmb = new JComboBox();        
        CommonSettings.settingFont30(cmb);
        cmb.setEditable(false);
        cmb.setSize(calcW(690), calcH(100));
        cmb.setLocation(calcW(284 + (i-1)*900), calcH(1270));
        this.add(cmb);
        if (i == 1) elCombo = cmb;
        else if (i == 2) athlCombo = cmb;
        else if (i == 3) judCombo = cmb;
        
        //btns
        btn = new JButton("+");      
        btn.setFocusable(false);
        btn.setSize(calcW(100), calcH(100));
        btn.setLocation(calcW(284 + i*900 - 200), calcH(1270));
        CommonSettings.settingFont30(btn);
        btn.setBackground(Color.LIGHT_GRAY);
        this.add(btn); 
        if (i == 1) {
            elAddBtn = btn;
            elAddBtn.addActionListener(new controllers.TestComEditPages.
                                          GlasialEditPage.AddElement());
        }
        else if (i == 2) {
            athlAddBtn = btn;
            athlAddBtn.addActionListener(new controllers.TestComEditPages.
                                             GlasialEditPage.AddAthlete());
        }
        else if (i == 3) {
            judAddBtn = btn;
            judAddBtn.addActionListener(new controllers.TestComEditPages.
                                            GlasialEditPage.AddJudge());
        }        
    } 
    
    //GETTERS*******************************************************************
     public JList getElLst() {
        return elLst;
    }
    public JList getAthlLst() {
        return athlLst;
    }
    public JList getJudLst() {
        return judLst;
    }

    public DefaultListModel getElLstModel() {
        return elLstModel;
    }
    public DefaultListModel getAthlLstModel() {
        return athlLstModel;
    }
    public DefaultListModel getJudLstModel() {
        return judLstModel;
    }
    
    public JComboBox getElCombo() {
        return elCombo;
    }
    public JComboBox getAthlCombo() {
        return athlCombo;
    }
    public JComboBox getJudCombo() {
        return judCombo;
    }

    //SETTERS*******************************************************************
    public void setElLst(JList elLst) {
        this.elLst = elLst;
    }
    public void setAthlLst(JList athlLst) {
        this.athlLst = athlLst;
    }
    public void setJudLst(JList judLst) {
        this.judLst = judLst;
    }

    public void setElLstModel(DefaultListModel elLstModel) {
        this.elLstModel = elLstModel;
    }
    public void setAthlLstModel(DefaultListModel athlLstModel) {
        this.athlLstModel = athlLstModel;
    }
    public void setJudLstModel(DefaultListModel judLstModel) {
        this.judLstModel = judLstModel;
    }

    public void setElCombo(JComboBox elCombo) {
        this.elCombo = elCombo;
    }
    public void setAthlCombo(JComboBox athlCombo) {
        this.athlCombo = athlCombo;
    }
    public void setJudCombo(JComboBox judCombo) {
        this.judCombo = judCombo;
    }
}
