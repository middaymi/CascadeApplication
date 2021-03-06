package data;

//ElementIsu as data
//this is as view

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
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
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

import static utils.Layout.calcH;
import static utils.Layout.calcW;

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
    private SingleStComPage singleComPage = Manager.getSingleComPage();

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
        createElementTypeCmb(null);
        createElementCmb(null);
        createInfo("");
        createBase();
        createJudge(null);
        createResult();

        this.setLayout(null);
        this.setSize(calcW(1720 + judges.size() * 140 + 150), calcH(70));
        this.setLocation(0, calcH(70 + i * 70));
        this.setVisible(true);
        this.setFocusable(true);
    }

    public ElementRow(int number, ElementIsu elIsu) {
        IsuComModel model = IsuComModel.getModelInstance();
        HashMap<Integer, ElementData> elements = model.getAllElements();
        HashMap<Integer, IsuElementType> types = model.getAllTypes();
        elementIsu = elIsu;

        numbLabel = new JLabel(number + 1 + ".");

        elementTypeCmb = new JComboBox();
        elementCmb = new JComboBox();
        info = new JComboBox();
        base = new JLabel(String.valueOf(elIsu.getBaseValue()));
        score = new JLabel();

        int judgesNumbers = elIsu.getJudgesValues().size();
        this.judges = model.getJudgesByComp();
        this.elements = elements;
        this.types = types;

        createNumbLbl();
        createElementTypeCmb(types.get(elIsu.getElementTypeId()));
        createElementCmb(elements.get(elIsu.getElementId()));
        changableFlag(1);
        createInfo(elIsu.getInfo());
        createBase();
        createJudge(elIsu);
        createResult();

        this.setLayout(null);
        this.setSize(calcW(1720 + judges.size() * 140 + 150), calcH(70));
        this.setLocation(0, calcH(70 + number * 70));
        this.setVisible(true);
        this.setFocusable(true);
    }

    private void createNumbLbl() {
        numbLabel.setSize(calcW(120), calcH(70));
        numbLabel.setLocation(0, 0);
        CommonSettings.settingFont30(numbLabel);
        CommonSettings.settingNarrowLightGrayBorder(numbLabel);
        this.add(numbLabel);
    }

    //flag for changing base value by selecting element
    int flag = 0;

    private void changableFlag(int i) {
        flag = i;
    }

    private int changableFlag() {
        return flag;
    }


    private void createElementTypeCmb(IsuElementType elementType) {
        elementTypeCmb.setSize(calcW(500), calcH(70));
        elementTypeCmb.setLocation(calcW(120), 0);
        CommonSettings.settingFont30(elementTypeCmb);
        this.add(elementTypeCmb);

        //get types
        isuComModel = IsuComModel.getModelInstance();

        //add all types
        for (IsuElementType type : isuComModel.getAllTypes().values()) {
            elementTypeCmb.addItem(type);
        }

        //select selected item
        if (elementType == null) {
            elementTypeCmb.setSelectedItem(null);
        } else {
            elementTypeCmb.setSelectedItem(elementType);
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

    private void createElementCmb(ElementData element) {
        elementCmb.setSize(calcW(800), calcH(70));
        elementCmb.setLocation(calcW(620), 0);
        CommonSettings.settingFont30(elementCmb);
        this.add(elementCmb);

        for (ElementData elem : isuComModel.getAllElements().values()) {
            elementCmb.addItem(elem);
        }

        //select selected item
        if (element == null) {
            elementCmb.setSelectedItem(null);
        } else {
            elementCmb.setSelectedItem(element);
            elementIsu.setSaved(true);
        }

        //if type and element are selected
        elementCmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((ElementData) (elementCmb.getSelectedItem()) != null &&
                        changableFlag() == 1) {

                    //get element link
                    ElementData selElement = ((ElementData) (elementCmb.getSelectedItem()));

                    //get prevElementId
                    int prevElemId = elementIsu.getElementId();

                    //setElementId to ElementIsu
                    elementIsu.setElementId(selElement.getId());
                    //setElementName to ElementIsu
                    elementIsu.setName(selElement.getFullNameRus() +
                            " (" + selElement.getAbbreviation() + ")");
                    //setBaseValue to ElementIsu
                    elementIsu.setBaseValue(selElement.getBase());
                    base.setText("");
                    base.setText(String.valueOf(((ElementData) elementCmb.
                            getSelectedItem()).getBase()));
                    addToDBElementWithNullMarks((Athlete) singleComPage.getAthlCmb().getSelectedItem(), elementIsu, prevElemId);
                }
            }
        });
    }

    private void createInfo(String nameOfInfo) {
        info.setSize(calcW(150), calcH(70));
        info.setLocation(calcW(1420), 0);
        CommonSettings.settingFont30(info);
        this.add(info);
        for (String info : IsuElementsData.getInfoValues()) {
            this.info.addItem(info);
        }

        if (!nameOfInfo.equals("")) {
            info.addItem(nameOfInfo);
            info.setSelectedItem(nameOfInfo);
        } else {
            info.setSelectedItem(null);
        }

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String infoVal = (String) info.getSelectedItem();
                    elementIsu.setInfo(infoVal);
                    addInfoToDB(infoVal);
            }
        });
    }

    private void addInfoToDB(String infoVal) {
        String query = String.format("UPDATE ALL_RESULTS_ELEMENTS SET INFO = '%s' WHERE IDcompetitionPerformanceAthleteLink = %d and IDisuElement = %d",
                infoVal,
                isuComModel.getCIARS().get(((Athlete) singleComPage.getAthlCmb().getSelectedItem()).getId()).getCompetitionAthlId(),
                elementIsu.getElementId());
        PreparedStatement prst = null;
        try {
            prst = isuComModel.getDBC().prepareStatement(query);
            prst.execute();
            System.out.println(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBase() {
        base.setSize(calcW(150), calcH(70));
        base.setLocation(calcW(1570), 0);
        CommonSettings.settingFont30(base);
        CommonSettings.settingNarrowLightGrayBorder(base);
        this.add(base);
    }

    private void createJudge(ElementIsu elemIsu) {
        judgeMarks.clear();
        for (int i = 0; i < judges.size(); i++) {
            Judge judge = judges.get(i);
            JComboBox judgeMark = new JComboBox();
            judgeMark.setSize(calcW(140), calcH(70));
            judgeMark.setLocation(calcW(1720 + i * 140), 0);
            CommonSettings.settingFont30(judgeMark);

            this.add(judgeMark);
            judgeMarks.add(judgeMark);

            for (String goe : IsuElementsData.getGoe()) {
                judgeMark.addItem(goe);
            }

            ElementValue elementValue;

            if (elemIsu == null || elemIsu.getJudgesValues().get(judge.getId()) == null) {
                elementValue = new ElementValue();
                elementValue.setJudgeId(judge.getId());
                elementValue.setMark(null);
                getElementIsu().getJudgesValues().
                        put(judge.getId(), elementValue);
                judgeMark.setSelectedItem(null);
            } else {
                Integer mark = elemIsu.getJudgesValues().get(judge.getId()).getMark();
                if (mark == null) {
                    judgeMark.setSelectedItem(null);
                } else if (mark == 0) {
                    judgeMark.setSelectedItem("0");
                } else {
                    judgeMark.setSelectedItem(mark > 0 ? "+" + String.valueOf(mark) : String.valueOf(mark));
                }
            }

            judgeMark.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isuComModel.isDoNothingWithListenersFlagUp()) {
                        return;
                    }
                    ElementValue elementValue = getElementIsu().getJudgesValues().get(judge.getId());
                    //athlete is selected and radioBtns are disabled
                    if (elementCmb.getSelectedItem() != null) {
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
        }
    }

    private void createResult() {
        score.setSize(calcW(150), calcH(70));
        score.setLocation(calcW(1720 + judges.size() * 140), 0);
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

            query = "UPDATE ALL_RESULTS_ELEMENTS SET Base = " + base +
                    ", Info = '" + info + "', Mark = " + mark +
                    " WHERE IDcompetitionPerformanceAthleteLink = " +
                    isuComModel.getCIAR().getCompetitionAthlId() +
                    " AND IDisuElement = " + elId + " AND IDjudge = " + judId + ";";
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

    private void addToDBElementWithNullMarks(Athlete athlete, ElementIsu elementIsu, int prevElemId) {
        String query = "";
        PreparedStatement prst = null;
        String insert = "";
        boolean elementsWasDeleted = false;

        for (ElementValue elemValue : elementIsu.getJudgesValues().values()) {
            insert = " INSERT INTO ALL_RESULTS_ELEMENTS VALUES (" +
                    isuComModel.getCIARS().get(athlete.getId()).getCompetitionAthlId() + ", " +
                    elementIsu.getBaseValue() + ", " +
                    "\'\'" + ", " + //info
                    null + ", " + //mark
                    elementIsu.getElementId() + ", " + //newId
                    elemValue.getJudgeId() + "); ";

            if (elementIsu.isSaved()) {
                if (!elementsWasDeleted) {
                    query = "DELETE FROM ALL_RESULTS_ELEMENTS WHERE IDcompetitionPerformanceAthleteLink = "
                            + isuComModel.getCIARS().get(athlete.getId()).getCompetitionAthlId() +
                            " AND IDisuElement = " + prevElemId + ";" +
                            insert;
                    elementsWasDeleted = true;
                } else {
                    query = insert;
                }
            } else {
                query = insert;
            }

            System.out.println(query);
            try {
                prst = isuComModel.getDBC().prepareStatement(query);
                prst.execute();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        //clear marks on gui and in data
        for (ElementRow row : singleComPage.getElRows()) {
            if (row.getElementIsu().getElementId() == elementIsu.getElementId()) {
                for (ElementValue dataMarks : elementIsu.getJudgesValues().values()) {
                    dataMarks.setMark(null);
                }
                for (JComboBox guiMarks : row.getJudgeMarks()) {
                    guiMarks.setSelectedItem("");
                }
                break;
            }
        }
        elementIsu.setSaved(true);
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