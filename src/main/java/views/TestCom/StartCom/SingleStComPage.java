package views.TestCom.StartCom;

import data.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList; 
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import models.TestCom.StartCom.IsuComModel;
import views.CommonSettings;
import views.Manager;

public class SingleStComPage extends JPanel {
    
    private JLabel fulllNamelbl;
    private JLabel  rankLbl;
    
    //top panel
    //***************************    
    private JLabel start;
    private JLabel totalScore;
    private JLabel elementScore;
    private JLabel componentScore;
    private JLabel deductions;
        
    private JTextField startTF;
    private JTextField totalScoreTF;
    private JTextField elementScoreTF;
    private JTextField componentScoreTF;
    private JTextField deductionsTF;
    //*******************************
      
    private JComboBox athlCmb;
    private JButton athlBtn;

    //element header labels    
    private JLabel n;
    private JLabel elementTypes;
    private JLabel elements;
    private JLabel info;
    private JLabel baseValue;
    private JLabel scores;
    //************************
    
    //elements and components scrl panes    
    private JScrollPane elScrl;
    private JPanel elPanel; 
    private JScrollPane compScrl;
    private JPanel compPanel;
    //***************************
    
    //radioBtns
    private JRadioButton points;
    private JRadioButton marks;
    private ButtonGroup group;
    
    //btns
    private JButton savePrBtn;
    private JButton addElemBtn;
    private JButton finBtn;
    
    //array of gui elements
    private ArrayList<ElementRow> elRows = new ArrayList<>();    
    //array of gui components
    private ArrayList<ComponentRow> compRows = new ArrayList<>();
    
    //judge lst model (for right judges list)
    private DefaultListModel lstModel = new DefaultListModel();
      
    public SingleStComPage() {
        CommonSettings.panelSettings(this);
        
        //for additing athlete
        athlCmb = new JComboBox();
        athlBtn = new JButton();      
        
        createWelLbls(); 
        createAthlChoose();  
        createJud();
        createLblsTF(start, startTF, 1);
        createLblsTF(totalScore,totalScoreTF, 2);
        createLblsTF(elementScore, elementScoreTF, 3);
        createLblsTF(componentScore,componentScoreTF, 4);
        createLblsTF(deductions, deductionsTF, 5);        
        createElScrl();
        createCompScrl();
        createLbls();        
        createFinishBtn();
        createRadioBtns();
        createSaveProtocolBtn();             
        createAddElemBtn();
        setEditableTopPnl(false);        
        deductionsTF.setEditable(true);
    }
    
    //full name competition and rank lbl
    private void createWelLbls() {
        fulllNamelbl = new JLabel();    
        fulllNamelbl.setSize(1050, 70);
        fulllNamelbl.setLocation(250, 30);
        CommonSettings.settingFont30(fulllNamelbl);
        CommonSettings.settingGrayBorder(fulllNamelbl);
        fulllNamelbl.setBackground(Color.LIGHT_GRAY);
        fulllNamelbl.setOpaque(true);
        this.add(fulllNamelbl);                
                
        rankLbl = new JLabel();        
        rankLbl.setSize(720, 70);
        rankLbl.setLocation(50, 120);
        CommonSettings.settingFont30(rankLbl);
        CommonSettings.settingGrayBorder(rankLbl);
        rankLbl.setBackground(Color.LIGHT_GRAY);
        rankLbl.setOpaque(true);
        this.add(rankLbl);
    }  
    
    private void createAthlChoose() {
        //lbl 
        JLabel athl = new JLabel("Спортсмен: ");
        athl.setSize(720, 50);
        athl.setLocation(50, 200);
        CommonSettings.settingFont30(athl);        
        this.add(athl);
           
        //combo
        athlCmb = new JComboBox();        
        CommonSettings.settingFont30(athlCmb);        
        athlCmb.setSize(650, 70);
        athlCmb.setLocation(50, 250);
        athlCmb.setEditable(false);        
        this.add(athlCmb); 
        athlCmb.setSelectedItem(null);    
    }
           
    private void createLblsTF(JLabel lbl, JTextField tf, int i) {
        lbl = new JLabel();
        tf = new JTextField();
        
        lbl.setHorizontalTextPosition(JLabel.CENTER);
        lbl.setSize(270, 70);
        lbl.setLocation(1000 + i * 270, 180);
        lbl.setVisible(true);
        lbl.setOpaque(true);
        lbl.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingGrayBorder(lbl);
        CommonSettings.settingFont30(lbl);
                
        tf.setSize(270, 70);
        tf.setLocation(1000 + i*270, 250);
        tf.setVisible(true);
        tf.setOpaque(true);        
        CommonSettings.settingGrayBorder(tf);
        CommonSettings.settingFont30(tf);
        
        switch(i) {
            case(1):
                lbl.setText("Start");
                start = lbl;
                startTF = tf;
                break;
            case(2):
                lbl.setText("Total score");
                totalScore = lbl;
                totalScoreTF = tf;                                
                break;
            case(3):                
                lbl.setText("Element score");
                elementScore = lbl;
                elementScoreTF = tf; 
                break;
            case(4):                
                lbl.setText("Component score");
                componentScore = lbl;
                componentScoreTF = tf;                                
                break;
            case(5):                
                lbl.setText("Deductions");
                deductions = lbl;
                deductionsTF = tf;
                deductionsTF.addActionListener(new controllers.TestComPage.
                                                 SingleComPage.SaveDeduction());
                break;
        }
        this.add(lbl);
        this.add(tf);
    }
    
    private void createJud() {
        JLabel judLbl = new JLabel("Список судей");
        judLbl.setHorizontalTextPosition(JLabel.CENTER);
        judLbl.setSize(430, 70);
        judLbl.setLocation(2688, 180);
        judLbl.setVisible(true);
        judLbl.setOpaque(true);
        judLbl.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingGrayBorder(judLbl);
        CommonSettings.settingFont30(judLbl);
        this.add(judLbl);
        
        JList judLst = new JList();
        JScrollPane judScrl = new JScrollPane(judLst);        
        CommonSettings.settingFont30(judLst);        
        judLst.setModel(lstModel);        
        judScrl.setSize(430, 400);
        judScrl.setLocation(2688, 250);        
        this.add(judScrl);          
    }
    
    private void createElScrl() {
        elPanel = new JPanel();
        getElPanel().setLayout(null); 
        getElPanel().setMinimumSize(new Dimension(2595, 700));
        elScrl = new JScrollPane(getElPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);          
        elScrl.setLocation(50, 350); 
        getElPanel().setLocation(0, 0);
        elScrl.setVisible(true);
        this.add(elScrl);
    }
     
    private void createCompScrl() {
        compPanel = new JPanel();
        compPanel.setLayout(null); 
        compPanel.setMinimumSize(new Dimension(2595, 450));
        compScrl = new JScrollPane(compPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);         
        compScrl.setLocation(50, 1160); 
        compPanel.setLocation(0, 0);
        compScrl.setVisible(true);        
        this.add(compScrl);
    }
    
    private void createHeaderLabels(int number, int judgesCount) {
        n = new JLabel("№");
        elementTypes = new JLabel("Element type");
        elements = new JLabel("Elements");
        info = new JLabel("Info");
        baseValue = new JLabel("BaseValue");
        scores = new JLabel("Scores");
        
        n.setSize(120, 70);
        elementTypes.setSize(500, 70);
        elements.setSize(800, 70);
        info.setSize(150, 70);
        baseValue.setSize(150, 70);
        scores.setSize(150, 70);
        
        n.setLocation(0, 0);
        elementTypes.setLocation(120, 0);
        elements.setLocation(620, 0);
        info.setLocation(1420, 0);
        baseValue.setLocation(1570, 0);
        
        if (number == 1) {            
            getElPanel().add(n);
            getElPanel().add(elementTypes);
            getElPanel().add(elements);
            getElPanel().add(info);
            getElPanel().add(baseValue);            
            createElJudLabels(1, judgesCount);        
            
        } else if (number == 2) { 
            elementTypes.setText("");
            elements.setText("Components");
            info.setText("");
            baseValue.setText("Factor");
            
            getCompPanel().add(n);
            getCompPanel().add(elementTypes);
            getCompPanel().add(elements);
            getCompPanel().add(info);
            getCompPanel().add(baseValue); 
            createElJudLabels(2, judgesCount);
        }
               
        CommonSettings.settingGrayBorder(n);
        CommonSettings.settingGrayBorder(elementTypes);
        CommonSettings.settingGrayBorder(elements);
        CommonSettings.settingGrayBorder(info);
        CommonSettings.settingGrayBorder(baseValue);
        CommonSettings.settingGrayBorder(scores);
        
        CommonSettings.settingFont30(n);
        CommonSettings.settingFont30(elementTypes);
        CommonSettings.settingFont30(elements);
        CommonSettings.settingFont30(info);
        CommonSettings.settingFont30(baseValue);
        CommonSettings.settingFont30(scores);
        
        n.setOpaque(true);
        elementTypes.setOpaque(true);
        elements.setOpaque(true);
        info.setOpaque(true);
        baseValue.setOpaque(true);
        scores.setOpaque(true);

        n.setBackground(Color.LIGHT_GRAY);
        elementTypes.setBackground(Color.LIGHT_GRAY);
        elements.setBackground(Color.LIGHT_GRAY);
        info.setBackground(Color.LIGHT_GRAY);
        baseValue.setBackground(Color.LIGHT_GRAY);
        scores.setBackground(Color.LIGHT_GRAY);
    }
            
    private void createElJudLabels(int i, int judgesCount) {
        //i = element(1) or component labels(2)
        //j = count of judges
        for (int k = 1; k <= judgesCount; k++) {
            JLabel lbl = new JLabel(); 
            lbl.setText("J" + k); 
            lbl.setSize(140, 70);              
            lbl.setOpaque(true);
            lbl.setBackground(Color.LIGHT_GRAY);
            CommonSettings.settingGrayBorder(lbl);
            CommonSettings.settingFont30(lbl); 
            lbl.setBackground(Color.LIGHT_GRAY);
            lbl.setLocation(1720 + (k - 1) * 140, 0);
            if (i == 1) {getElPanel().add(lbl);}
            else if (i == 2) {getCompPanel().add(lbl);}
        } 
        scores.setLocation(1720 + judgesCount * 140, 0);
        if (i == 1) {                         
            getElPanel().setPreferredSize
                      (new Dimension(1720 + judgesCount * 140 + 150, 10000));                          
            getElPanel().add(scores);             
        } else if (i == 2) { 
            getCompPanel().setPreferredSize
                      (new Dimension(1720 + judgesCount * 140 + 150, 
                      getCompPanel().getHeight())); 
            getCompPanel().add(scores);           
        }  
        
        elScrl.setSize(1720 + judgesCount * 140 + 150 + 20, 700);                                   
    }  
          
    private void createFinishBtn() {
        finBtn = new JButton("<html>Подвести итог<p align=center>" +
                                                     "по спортсмену<html>");
        finBtn.setSize(430, 100);
        finBtn.setLocation(2688, 700);
        finBtn.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingGrayBorder(finBtn);
        CommonSettings.settingFontBold30(finBtn);
        this.add(finBtn);
        finBtn.addActionListener(new controllers.TestComPage.SingleComPage.
                                                        FinishComByAthlete());
    }
    
    private void createSaveProtocolBtn() {
        savePrBtn = new JButton("<html>Сохранить<p align=center>" +
                                            "протокол в PDF<html>");
        savePrBtn.setSize(430, 100);
        savePrBtn.setLocation(2688, 960);
        savePrBtn.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingGrayBorder(savePrBtn);
        CommonSettings.settingFontBold30(savePrBtn);
        this.add(savePrBtn);
        savePrBtn.addActionListener(new controllers.TestComPage.SingleComPage.
                                                        SaveProtocol());
    }
    
    private void createRadioBtns() {
        JPanel rbPanel = new JPanel();
        group = new ButtonGroup();
        marks = new JRadioButton("Оценки", true);
        marks.setEnabled(false);
        group.add(marks);
        points = new JRadioButton("Баллы", false);
        points.setEnabled(false);
        group.add(points);
        rbPanel.add(marks);
        rbPanel.add(points);
        
        points.addActionListener(new controllers.TestComPage.SingleComPage.ViewPoints());
        marks.addActionListener(new controllers.TestComPage.SingleComPage.ViewMarks());        
        
        CommonSettings.settingFont30(marks);
        CommonSettings.settingFont30(points);
        CommonSettings.settingGrayBorder(rbPanel);
        rbPanel.setBackground(Color.LIGHT_GRAY);
        rbPanel.setSize(430, 100);
        rbPanel.setLocation(2688, 830);
        rbPanel.setVisible(true);
        rbPanel.setOpaque(true);         
        this.add(rbPanel);      
    }           
    
    public void addElementRow() {
        ElementRow el = 
            new ElementRow(IsuComModel.getModelInstance().getJudgesByComp(),
                           IsuComModel.getModelInstance().getAllElements(), 
                           IsuComModel.getModelInstance().getAllTypes(), elRows.size());
        elPanel.add(el);
        elPanel.repaint();
        elPanel.updateUI();
        elRows.add(el);
    }

    public void addElementRow(int number, ElementIsu elIsu) {
        ElementRow el = new ElementRow(number, elIsu);
        elPanel.add(el);
        elPanel.repaint();
        elPanel.updateUI();
        elRows.add(el);
    }
    
    public void createAddElemBtn() {
        addElemBtn = new JButton("Добавить элемент");
        addElemBtn.setSize(300, 70);
        addElemBtn.setLocation(50, 1060);
        addElemBtn.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingFont30(addElemBtn);
        this.add(addElemBtn);

        addElemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (athlCmb.getSelectedItem() != null) {
                    addElementRow(); 
                } else {
                    JOptionPane.showMessageDialog(Manager.getSingleComPage(),
                    "Для начала укажите спортсмена!",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
                    return;
                }                                                
            }
        });
    }
    
    private void addComponentRows(ArrayList<Judge>judges,
                                  ArrayList<Component>components,
                                  float factor) {
        int index = 0;
        for (Component component : components) {
            ComponentRow componentRow = new ComponentRow(judges, component, 
                                                          index++, factor);
            compPanel.add(componentRow); 
            compRows.add(componentRow);            
        }
        compScrl.setSize(1720 + judges.size() * 140 + 150 + 10, 
                        (components.size() + 1) * 70 + 10);        
    }
    
    public void createLbls() {  
        getElPanel().removeAll();
        getCompPanel().removeAll(); 
        createHeaderLabels(1, IsuComModel.getModelInstance().
                               getJudgesByComp().size());
        createHeaderLabels(2, IsuComModel.getModelInstance().
                                getJudgesByComp().size());
        addComponentRows(IsuComModel.getModelInstance().getJudgesByComp(),
                         IsuComModel.getModelInstance().getComponentsList(),
                         IsuComModel.getModelInstance().getFactor());
    }

    //enable/disable add element function
    public void enableAddElemBtn(boolean value) {
        addElemBtn.setEnabled(value);
    }

    public void enableFinBtn(boolean value) { finBtn.setEnabled(value); }

    //GETTERS|SETTERS***********************************************************
    public DefaultListModel getLstModel() {
        return lstModel;
    }

    public void setLstModel(DefaultListModel lstModel) {
        this.lstModel = lstModel;
    }

    public void setFullName(String fullName) {
        this.fulllNamelbl.setText("Название: " + fullName);
    }

    public void setRank(String rank) {        
        this.rankLbl.setText("Разряд: " + rank);
    }

    public JComboBox getAthlCmb() {
        return athlCmb;
    }
        
    public JPanel getElPanel() {
        return elPanel;
    }

    public JPanel getCompPanel() {
        return compPanel;
    }

    public ArrayList<ElementRow> getElRows() {
        return elRows;
    }
    
    public ArrayList<ComponentRow>  getCompRows() {
        return compRows;
    }

    public JScrollPane getCompScrl() {
        return compScrl;
    }

    public JButton getAthlBtn() {
        return athlBtn;
    }

    public JTextField getStartTF() {
        return startTF;
    }

    public void setStartTF(String number) {
        this.startTF.setText(number);
    }

    public void setTextDeductions(String str) {
        this.deductionsTF.setText(str);
    }

    public JTextField getDeductions() {
        return deductionsTF;
    }

    public JButton getSavePrBtn() {
        return savePrBtn;
    }

    public JButton getAddElemBtnBtn() { return addElemBtn; }

    public void setEnabledSavePrBtn(boolean value) {
        this.savePrBtn.setEnabled(value);
    }

    public JRadioButton getPoints() {
        return points;
    }

    public JRadioButton getMarks() {
        return marks;
    }

    public void setEnabledPoints(boolean value) {
        this.points.setEnabled(value);
    }

    public void setEnabledMarks(boolean value) {
        this.marks.setEnabled(value);
    }

    public void setSelectedRadioBtn(JRadioButton btn) {
        btn.setSelected(true);
    }
    
    public void setEditableTopPnl(boolean value) {
        ArrayList<JTextField> fields = new ArrayList<>();
        fields.add(startTF);
        fields.add(totalScoreTF);
        fields.add(elementScoreTF);
        fields.add(componentScoreTF);
        fields.add(deductionsTF);
        for (JTextField tf : fields) {
            tf.setEditable(value);
        }        
    }

    public void setReusltsToTopPnl(String[] textOfFields) {
        startTF.setText(textOfFields[0]);
        totalScoreTF.setText(textOfFields[1]);
        elementScoreTF.setText(textOfFields[2]);
        componentScoreTF.setText(textOfFields[3]);
        deductionsTF.setText(textOfFields[4]);
    }
    
    public void setTotalScore(String str) {
        this.totalScoreTF.setText(str);
    }

    public void setElementScore(String str) {
        this.elementScoreTF.setText(str);
    }

    public void setComponentScore(String str) {
        this.componentScoreTF.setText(str);
    }

    public void setStartNumber(String str) { this.startTF.setText(str); }

    public void setComponentsRow(ComponentRow row) {
        compRows.add(row);
    }
}
