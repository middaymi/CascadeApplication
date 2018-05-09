
package data;

import models.TestCom.StartCom.IsuComModel;
import models.TestCom.TestComModel;
import views.CommonSettings;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Layout.calcH;
import static utils.Layout.calcW;

public class ComponentRow extends JPanel {
    private JLabel number;
    private JLabel empty1;
    private JLabel component;
    private JLabel empty2;
    private JLabel factor;
    private ArrayList<Judge> judges;
    private ArrayList<JTextField> judgeMarks = new ArrayList<>();
    private JLabel score;
    private LineBorder redline = new LineBorder(Color.red, 4);

    private IsuComModel isuComModel;
    private SingleStComPage singleStComPage;
    private Component componentData;
    private ComponentIsu componentIsu;

    public ComponentRow(ArrayList<Judge> judges,
                        Component component,
                        int number,
                        float factor) {

        componentIsu = new ComponentIsu();
        isuComModel = IsuComModel.getModelInstance();
        singleStComPage = Manager.getManagerInstance().getSingleComPage();

        this.number = new JLabel(String.valueOf(number + 1));
        this.empty1 = new JLabel();
        this.component = new JLabel(component.getFullNameRUS());
        this.empty2 = new JLabel();
        this.factor = new JLabel(String.valueOf(factor));
        this.judges = judges;
        this.score = new JLabel();

        this.componentData = component;
        componentIsu.setComponentId(componentData.getId());

        createNumberLbl();
        createEmpty1Lbl();
        createComponentLbl();
        createEmpty2Lbl();
        createFactorLbl();
        createJudgesTF();
        createResult();

        this.setLayout(null);
        this.setSize(calcW(1720 + judges.size() * 140 + 150), calcH(70));
        this.setLocation(0, calcH(70 + number * 70));
    }

    private void createNumberLbl() {
        number.setSize(calcW(120), calcH(70));
        number.setLocation(0, 0);
        CommonSettings.settingFont30(number);
        CommonSettings.settingNarrowLightGrayBorder(number);
        this.add(number);
    }

    private void createEmpty1Lbl() {
        empty1.setSize(calcW(500), calcH(70));
        empty1.setLocation(calcW(120), 0);
        CommonSettings.settingNarrowLightGrayBorder(empty1);
        this.add(empty1);
    }

    private void createComponentLbl() {
        component.setSize(calcW(800), calcH(70));
        component.setLocation(calcW(620), 0);
        CommonSettings.settingFont30(component);
        CommonSettings.settingNarrowLightGrayBorder(component);
        this.add(component);
    }

    private void createEmpty2Lbl() {
        empty2.setSize(calcW(150), calcH(70));
        empty2.setLocation(calcW(1420), 0);
        CommonSettings.settingNarrowLightGrayBorder(empty2);
        this.add(empty2);
    }

    private void createFactorLbl() {
        factor.setSize(calcW(150), calcH(70));
        factor.setLocation(calcW(1570), 0);
        CommonSettings.settingFont30(factor);
        CommonSettings.settingNarrowLightGrayBorder(factor);
        this.add(factor);
    }

    public void createJudgesTF() {
        for (int i = 0; i < judges.size(); i++) {
            Judge judge = judges.get(i);
            int index = i;
            JTextField judgeMark = new JTextField();
            judgeMark.setSize(calcW(140), calcH(70));
            judgeMark.setLocation(calcW(1720 + i * 140), 0);
            judgeMark.setText("");
            CommonSettings.settingFont30(judgeMark);
            CommonSettings.settingNarrowLightGrayBorder(judgeMark);
            this.add(judgeMark);
            judgeMarks.add(judgeMark);
            judgeMark.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    Runnable doAssist = new Runnable() {
                        @Override
                        public void run() {
                            if (isuComModel.isDoNothingWithListenersFlagUp()) { return; }

                            if (singleStComPage.getAthlCmb().getSelectedItem() != null) {
                                Athlete athlete = (Athlete) singleStComPage.getAthlCmb().getSelectedItem();

                                if (isuComModel.isFinishedCompetitionForAthlete(athlete.getId())) { return; }

                                ComponentValue componentValue =
                                        componentIsu.getJudgesValues().get(judge.getId());
                                //gettext by entered field
                                String txt = judgeMark.getText();
                                //check entered value
                                if (isuComModel.checkDeductionsAndComponentsValue(txt)) {
                                    CommonSettings.settingNarrowLightGrayBorder(judgeMark);
                                    float value = (Float.valueOf(txt)).floatValue();
                                    //to value
                                    componentValue.setComponentId(componentData.getId());
                                    componentValue.setJudgeId(judge.getId());
                                    componentValue.setValue(value);
                                    //to componentIsu
                                    getComponentIsu().getJudgesValues().
                                            put(judge.getId(), componentValue);
                                    addToDB(componentValue);
                                    //value is bad
                                } else if (txt.equals("")) {
                                } else {
                                    judgeMark.setBorder(redline);
                                    judgeMark.setText("");
                                    JOptionPane.showMessageDialog(Manager.getSingleComPage(),
                                            "Неверное значение компонента!", "Ошибка!",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                }
                                //no selected athlete
                            } else {
                                judgeMark.setText("");
                                JOptionPane.showMessageDialog(Manager.getSingleComPage(),
                                        "Не выбран спортсмен!", "Ошибка!",
                                        JOptionPane.INFORMATION_MESSAGE);

                                return;
                            }
                        }
                    };
                    SwingUtilities.invokeLater(doAssist);
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

    private void addToDB(ComponentValue comVal) {
        PreparedStatement prst = null;
        String query;
        isuComModel = IsuComModel.getModelInstance();
        try {
            TestComModel tcModel = TestComModel.getTestComModelInstance();
            int selRow = tcModel.selRow();
            tcModel.getCompetitions().get(selRow);
            int compId = componentData.getId();
            float value = comVal.getValue();
            int judId = comVal.getJudgeId();

            if (!comVal.isSaved()) {
                //insert to database
                query = "INSERT INTO ALL_RESULTS_COMPONENTS " +
                        "VALUES(" + isuComModel.getCIAR().getCompetitionAthlId() + ", " +
                        compId + ", " + value + ", " + judId + ");";
                //change saved flag
                comVal.setSaved(true);
            } else {
                query = "UPDATE ALL_RESULTS_COMPONENTS SET Value = " + value + " " +
                        "WHERE IDcompetitionPerformanceAthleteLink = " +
                        isuComModel.getCIAR().getCompetitionAthlId() +
                        " AND IDcomponent = " + compId + " AND IDjudge = " + judId + ";";
            }
            System.out.println(query);
            prst = isuComModel.getDBC().prepareStatement(query);
            prst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ElementRow.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error");
        } finally {
            try {
                prst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ElementRow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<JTextField> getJudgeMarks() {
        return judgeMarks;
    }

    public ComponentIsu getComponentIsu() {
        return componentIsu;
    }

    public void setComponentIsu(ComponentIsu componentIsu) {
        this.componentIsu = componentIsu;
    }

    public void setTextJMFields(String str) {
        for (JTextField tf : judgeMarks) {
            tf.setText(str);
        }
    }

    public JLabel getScore() {
        return score;
    }

    public void setScoreText(String score) {
        this.score.setText(score);
    }

    //setEnadled JudgesMarks
    public void setEnabledCompRowComponents(boolean value) {
        for (JTextField txtF : judgeMarks) {
            txtF.setEditable(value);
        }
    }
}