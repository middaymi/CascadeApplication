package data;

//ElementIsu as data
//this is as view

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lombok.Data;
import models.TestCom.StartCom.IsuComModel;
import models.TestCom.TestComModel;
import views.CommonSettings;
import views.TestCom.StartCom.SingleStComPage;

public class ElementRow extends JPanel {

    private JLabel numbLabel;
    private JComboBox elementTypeCmb;
    private JComboBox elementCmb;
    private JComboBox info;
    private JLabel base;
    private ArrayList<JComboBox> judgeMarks = new ArrayList<>();
    private JLabel score;

    private ArrayList<Judge> judges;
    private HashMap<Integer, ElementData> elements;
    private HashMap<Integer, IsuElementType> types;

    private ElementIsu elementIsu;

    private IsuComModel isuComModel;
    private SingleStComPage singleComPage;

    public ElementRow(ArrayList<Judge> judges,
                      HashMap<Integer, ElementData> elements,
                      HashMap<Integer, IsuElementType> types,
                      int i) {

        elementIsu = new ElementIsu();
        isuComModel = IsuComModel.getModelInstance();
        isuComModel.getCIAR().addToElLst(elementIsu);

        numbLabel = new JLabel((i + 1) + ".");
        elementTypeCmb = new JComboBox();
        elementCmb = new JComboBox();
        info = new JComboBox();
        base = new JLabel();
        score = new JLabel();

        this.judges = judges;
        this.elements = elements;
        this.types = types;

        createNumbLbl();
        createElementTypeCmb("");
        createElementCmb("");
        createInfo("");
        createBase();
        createJudge();
        createResult();

        this.setLayout(null);
        this.setSize(1720 + judges.size() * 140 + 150, 70);
        this.setLocation(0, 70 + i * 70);
        this.setVisible(true);
        this.setFocusable(true);
    }

    public ElementRow(int number, ElementIsu elIsu) {
        IsuComModel model = IsuComModel.getModelInstance();
        numbLabel = new JLabel(number + 1 + ".");

        elementTypeCmb = new JComboBox();
        elementCmb = new JComboBox();
        info = new JComboBox();
        base = new JLabel(String.valueOf(elIsu.getBaseValue()));
        score = new JLabel();

        int judgesNumbers = elIsu.getJudgesValues().size();

        createNumbLbl();
        createElementTypeCmb(model.getAllTypes().get(elIsu.getElementTypeId()).getFullName());
        createElementCmb(model.getAllElements().get(elIsu.getElementId()).getFullNameRus());
        createInfo(elIsu.getInfo());
        createBase();
        createFillJudgeValues(elIsu);
//        createResult();

        this.setLayout(null);
        this.setSize(1720 + judgesNumbers * 140 + 150, 70);
        this.setLocation(0, 70 + number * 70);
        this.setVisible(true);
        this.setFocusable(true);
    }

    private void createNumbLbl() {
        numbLabel.setSize(120, 70);
        numbLabel.setLocation(0, 0);
        CommonSettings.settingFont30(numbLabel);
        CommonSettings.settingNarrowLightGrayBorder(numbLabel);
        this.add(numbLabel);
    }

    public void setTextNumbLbl(String text) {
        numbLabel.setText(text);
    }

    //flag for changing base value by selecting element
    int flag = 0;

    private void changableFlag(int i) {
        flag = i;
    }

    private int changableFlag() {
        return flag;
    }


    private void createElementTypeCmb(String nameOfElementType) {
        elementTypeCmb.setSize(500, 70);
        elementTypeCmb.setLocation(120, 0);
        CommonSettings.settingFont30(elementTypeCmb);
        this.add(elementTypeCmb);

        //get types
        isuComModel = IsuComModel.getModelInstance();

        //select selected item
        if (nameOfElementType.equals("")) {
            for (IsuElementType type : isuComModel.getAllTypes().values()) {
                elementTypeCmb.addItem(type);
            }
            elementTypeCmb.setSelectedItem(null);
        } else {
            elementTypeCmb.addItem(nameOfElementType);
            elementTypeCmb.setSelectedItem(nameOfElementType);
        }

        //if type is selected
        elementTypeCmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setElementTypeId to ElementIsu
                getElementIsu().setElementTypeId(((IsuElementType) (elementTypeCmb.
                        getSelectedItem())).getId());
                changableFlag(0);
                elementCmb.removeAllItems();
                base.setText("");
                for (ElementData element : isuComModel.getAllElements().values()) {
                    if (element.getElementTypeId() ==
                            ((IsuElementType) (elementTypeCmb.
                                    getSelectedItem())).getId()) {
                        elementCmb.addItem(element);
                    }
                }
                elementCmb.setSelectedItem(null);
                changableFlag(1);
            }
        });
    }

    private void createElementCmb(String nameOfElement) {
        elementCmb.setSize(800, 70);
        elementCmb.setLocation(620, 0);
        CommonSettings.settingFont30(elementCmb);
        this.add(elementCmb);

        //select selected item
        if (nameOfElement.equals("")) {
            elementCmb.setSelectedItem(null);
        } else {
            elementCmb.addItem(nameOfElement);
            elementCmb.setSelectedItem(nameOfElement);
        }

        //if type and element are selected
        elementCmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((ElementData) (elementCmb.getSelectedItem()) != null &&
                        changableFlag() == 1) {
                    //get element link
                    ElementData selElement = ((ElementData) (elementCmb.getSelectedItem()));
                    //setElementId to ElementIsu
                    getElementIsu().setElementId(selElement.getId());
                    //setElementName to ElementIsu
                    getElementIsu().setName(selElement.getFullNameRus() +
                            " (" + selElement.getAbbreviation() + ")");
                    //setBaseValue to ElementIsu
                    getElementIsu().setBaseValue(selElement.getBase());
                    base.setText("");
                    base.setText(String.valueOf(((ElementData) elementCmb.
                            getSelectedItem()).getBase()));
                }
            }
        });
    }

    private void createInfo(String nameOfInfo) {
        info.setSize(150, 70);
        info.setLocation(1420, 0);
        CommonSettings.settingFont30(info);
        this.add(info);
        for (String info : IsuElementsData.getInfoValues()) {
            this.info.addItem(info);
        }

        if (!nameOfInfo.equals("")) {
            info.addItem(nameOfInfo);
            info.setSelectedItem(nameOfInfo);
            return;
        }

        info.setSelectedItem(null);
        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getElementIsu().setInfo((String) info.getSelectedItem());
            }
        });
    }

    private void createBase() {
        base.setSize(150, 70);
        base.setLocation(1570, 0);
        CommonSettings.settingFont30(base);
        CommonSettings.settingNarrowLightGrayBorder(base);
        this.add(base);
    }

    private void createFillJudgeValues(ElementIsu elIsu) {
        judgeMarks.clear();
        judges = isuComModel.getJudgesByComp();
        for (int i = 0; i < judges.size(); i++) {
            JComboBox judgeMark = new JComboBox();
            judgeMark.setSize(140, 70);
            judgeMark.setLocation(1720 + i * 140, 0);
            CommonSettings.settingFont30(judgeMark);
            this.add(judgeMark);
            int mark = (elIsu.getJudgesValues().get(judges.get(i).getId())).getMark();
            judgeMark.addItem(mark);
            judgeMark.setSelectedItem(mark);
        }
    }

    private void createJudge() {
        for (int i = 0; i < judges.size(); i++) {
            Judge judge = judges.get(i);
            int index = i;
            JComboBox judgeMark = new JComboBox();
            judgeMark.setSize(140, 70);
            judgeMark.setLocation(1720 + i * 140, 0);
            CommonSettings.settingFont30(judgeMark);
            this.add(judgeMark);
            judgeMarks.add(judgeMark);
            for (String goe : IsuElementsData.getGoe()) {
                judgeMark.addItem(goe);
            }
            ElementValue elementValue = new ElementValue();
            judgeMark.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //athlete is selected and radioBtns are disabled
                    if (elementCmb.getSelectedItem() != null &&
                            !isuComModel.isFinished()) {
                        //to ElementValue                        
                        elementValue.setElementId(
                                ((ElementData) elementCmb.getSelectedItem()).getId());
                        elementValue.setJudgeId(judge.getId());
                        elementValue.setMark(Integer.valueOf(
                                ((String) (judgeMark.getSelectedItem()))));
                        elementValue.setValue((float) 0.0);
                        getElementIsu().getJudgesValues().
                                put(judge.getId(), elementValue);
                        addToDB(elementValue);
                    }
                }
            });
            judgeMark.setSelectedItem(null);
        }
    }

    private void createResult() {
        score.setSize(150, 70);
        score.setLocation(1720 + judges.size() * 140, 0);
        CommonSettings.settingFont30(score);
        CommonSettings.settingNarrowLightGrayBorder(score);
        this.add(score);
    }

    private void addToDB(ElementValue elVal) {
        PreparedStatement prst = null;
        String query;
        try {
            TestComModel tcModel = TestComModel.getTestComModelInstance();
            int selRow = tcModel.selRow();
            tcModel.getCompetitions().get(selRow);
            String info = elementIsu.getInfo();
            float base = elementIsu.getBaseValue();
            int mark = elVal.getMark();
            int elId = elVal.getElementId();
            int judId = elVal.getJudgeId();

            if (!elVal.isSaved()) {
                //insert to database
                query = "INSERT INTO ALL_RESULTS_ELEMENTS " +
                        "VALUES(" + isuComModel.getCIAR().getCompetitionAthlId() + ", " +
                        base + ", '" + info + "', " + mark + ", " + elId + ", " + judId + ");";
                //change saved flag
                elVal.setSaved(true);
            } else {
                query = "UPDATE ALL_RESULTS_ELEMENTS SET Base = " + base +
                        ", Info = '" + info + "', Mark = " + mark +
                        " WHERE IDcompetitionPerformanceAthleteLink = " +
                        isuComModel.getCIAR().getCompetitionAthlId() +
                        " AND IDisuElement = " + elId + " AND IDjudge = " + judId + ";";
            }
            System.out.println(query);
            prst = isuComModel.getDBC().prepareStatement(query);
            prst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ElementRow.class.getName()).
                    log(Level.SEVERE, null, ex);
        } finally {
            try {
                prst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ElementRow.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    public JLabel getScore() {
        return score;
    }

    public void setScoreText(String score) {
        this.score.setText(score);
    }

    public ElementIsu getElementIsu() {
        return elementIsu;
    }

    //setEnabled fields
    public void setEnabledElRowComponents(boolean value) {
        ArrayList<JComponent> fields = new ArrayList<>();
        fields.add(elementTypeCmb);
        fields.add(elementCmb);
        fields.add(info);
        fields.add(base);
        for (JComboBox cmb : judgeMarks) {
            fields.add(cmb);
        }
        fields.add(score);

        for (JComponent c : fields) {
            CommonSettings.settingFontBold30(c);
            c.setEnabled(value);
            c.setForeground(Color.WHITE);
        }
    }

    public ArrayList<JComboBox> getJudgeMarks() {
        return judgeMarks;
    }

    public void setJudgeMarks(ArrayList<JComboBox> judgeMarks) {
        this.judgeMarks = judgeMarks;
    }

    public void setElementTypeCmb(int typeID) {
        elementTypeCmb.setSelectedItem(typeID);
    }

    public void setElementCmb(int elementID) {
        elementCmb.setSelectedItem(elementID);
    }

    public void setInfo(String infoVal) {
        info.setSelectedItem(infoVal);
    }

    public void setBase(String baseVal) {
        base.setText(baseVal);
    }
}
