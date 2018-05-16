package models.TestCom.StartCom;

import controllers.TestComPage.SingleComPage.SetAthlete;
import data.*;
import models.TestCom.TestComModel;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IsuComModel extends StComModel {

    //athlets take part in selected competition  and their results   
    private List<Athlete> athletesByComp = new ArrayList<>();
    private Competition competition;
    private ArrayList<Component> components = new ArrayList<>();
    private HashMap<Integer, ElementData> allElements = new HashMap<>();
    private HashMap<Integer, IsuElementType> allTypes = new HashMap<>();
    private float factor = 0;
    private Rank rank = new Rank();
    //generate empty or full protocol
    private int mode;
    private boolean isFinished = false;

    private SingleStComPage singleComPage;

    private CompetitionIsuAthleteResult CIAR;
    private HashMap<Integer, CompetitionIsuAthleteResult> CIARS = new HashMap<>();

    private static boolean doNothingWithListenersFlag = false;
    public boolean isDeductionSaved = false;

    private IsuComModel() {
    }

    private static IsuComModel stComModelInstance = null;

    public static IsuComModel getModelInstance() {
        if (stComModelInstance == null) {
            stComModelInstance = new IsuComModel();
        }
        return stComModelInstance;
    }

    public boolean isFinishedCompetitionForAthlete(int athleteID) {
        //if finished = true(1)
        String query = String.format("select isFinished from COMPETITION_PERFORMANCE_ATHLETE_LINK where IDcompetition = %d and IDathlete = %d",
                competition.getId(), athleteID);

        PreparedStatement prst = null;
        ResultSet rs = null;

        try {
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return false;
    }

    //clear gui
    public void clearElementAndComponentsRow() {
        //elements
        for (ElementRow row : singleComPage.getElRows()) {
            singleComPage.getElPanel().remove(row);
            //row.setScoreText("");
        }
        singleComPage.getElPanel().updateUI();
        singleComPage.getElPanel().repaint();
        singleComPage.getElRows().clear();

        //components
        for (ComponentRow row : singleComPage.getCompRows()) {
            row.setTextJMFields("");
            row.setEnabledCompRowComponents(true);
            row.setScoreText("");
            singleComPage.getCompPanel().repaint();
        }

        //top panel
        singleComPage.clearTopPanelResults();
    }

    private Competition getSelCompetition() {
        int index = TestComModel.getTestComModelInstance().selRow();
        competition = TestComModel.getTestComModelInstance().
                getCompetitions().get(index);
        return competition;
    }

    //judges***
    /*get judges, TAKING PART IN COMPETITION from DB
    save to array as data*/
    private void setJudges() {
        tcModel = TestComModel.getTestComModelInstance();
        int selRow = tcModel.selRow();
        singleComPage = Manager.getSingleComPage();
        String query;
        PreparedStatement prst = null;
        ResultSet rs = null;
        //database 
        try {
            query = "SELECT JUDGE.ID, JUDGE.Surname, JUDGE.Name, " +
                    "JUDGE.Middlename " +
                    "FROM JUDGE, COMPETITION_JUDGE_LINK " +
                    "WHERE COMPETITION_JUDGE_LINK.IDjudge = JUDGE.id " +
                    "AND COMPETITION_JUDGE_LINK.IDcompetition = " +
                    tcModel.getValueAt(selRow, 1) + ";";
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not take judges from DB for SFPstartComPage", ex);
        }
        //data        
        try {
            //clear data
            getJudgesByComp().clear();
            singleComPage.getLstModel().clear();
            //get data   
            int i = 1;
            while (rs.next()) {
                Judge judge = new Judge();
                judge.setId(rs.getInt(1));
                judge.setSurname(rs.getString(2));
                judge.setName(rs.getString(3));
                judge.setMiddlename(rs.getString(4));
                getJudgesByComp().add(judge);
                String str = "<html>" + i + ". " + judge.getSurname() + " " +
                        judge.getName() +
                        "<p align=left>" +
                        judge.getMiddlename() +
                        "</html>";
                singleComPage.getLstModel().addElement(str);
                i++;
            }
            prst.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not set judges for Competition", ex);
        }
    }

    //athlets***
    /*get athletes, TAKING PART IN COMPETITION from DB
    save to array as data*/
    private void setAthletes() {
        tcModel = TestComModel.getTestComModelInstance();
        int selRow = tcModel.selRow();
        String query;
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            query = "SELECT DISTINCT ATHLETE.ID, " +
                    "ATHLETE.Surname, ATHLETE.Name, " +
                    "ATHLETE.Middlename, ATHLETE.Birthday " +
                    "FROM COMPETITION, COMPETITION_ATHLETE_LINK, " +
                    "ATHLETE " +
                    "WHERE COMPETITION_ATHLETE_LINK.IDcompetition = " +
                    tcModel.getValueAt(selRow, 1) +
                    "AND ATHLETE.ID = COMPETITION_ATHLETE_LINK.IDathlete;";
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not take athlets from DB for SFPstartComPage", ex);
        }
        //data
        try {
            //clear the data
            athletesByComp.clear();
            CIARS.clear();
            singleComPage.getAthlCmb().removeAllItems();

            //get data        
            while (rs.next()) {
                Athlete athlete = new Athlete();
                athlete.setId(rs.getInt(1));
                athlete.setName(rs.getString(3));
                athlete.setSurname(rs.getString(2));
                athlete.setMiddlename(rs.getString(4));
                athlete.setBirthday(rs.getDate(5));
                CompetitionIsuAthleteResult CIAR = new CompetitionIsuAthleteResult(athlete);
                CIARS.put(athlete.getId(), CIAR);
                athletesByComp.add(athlete);
                singleComPage.getAthlCmb().addItem(athlete);
            }
            rs.close();
            prst.close();
            singleComPage.getAthlCmb().setSelectedItem(null);
            singleComPage.getAthlCmb().addActionListener(new controllers.TestComPage.SingleComPage.SetAthlete());

        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not set athlets for SingleStartCompetition class", ex);
        }
    }

    //element types***
    /*get element types from DB
    save to array as data*/
    private void setTypes() {
        String query;
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            query = "SELECT * FROM ISU_ELEMENT_TYPE";
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            while (rs.next()) {
                IsuElementType type = new IsuElementType();
                type.setId(rs.getInt(1));
                type.setFullName(rs.getString(2));
                type.setDescription(rs.getString(3));
                getAllTypes().put(type.getId(), type);
            }
            prst.close();
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not take element's types from DB", ex);
        }
    }

    //elements***
    /*get all elements from DB
    save to array as data*/
    private void setElements() {
        String query;
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            query = "SELECT * FROM ISU_ELEMENT";
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            while (rs.next()) {
                ElementData element = new ElementData();
                element.setId(rs.getInt(1));
                element.setAbbreviation(rs.getString(2));
                element.setFullNameEng(rs.getString(3));
                element.setFullNameRus(rs.getString(4));
                element.setLevel(rs.getInt(5));
                element.setElementTypeId(rs.getInt(6));
                element.setValuePlus3(rs.getFloat(7));
                element.setValuePlus2(rs.getFloat(8));
                element.setValuePlus1(rs.getFloat(9));
                element.setBase(rs.getFloat(10));
                element.setBaseV(rs.getFloat(11));
                element.setBaseV1(rs.getFloat(12));
                element.setBaseV2(rs.getInt(13));
                element.setValueMinus1(rs.getFloat(14));
                element.setValueMinus2(rs.getFloat(15));
                element.setValueMinus3(rs.getFloat(16));
                allElements.put(element.getId(), element);
            }
            IsuElementsData.setData(allElements);
            rs.close();
            prst.close();
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not set elementsIsu", ex);
        }
    }

    //selected rank***
    private void setRank() {
        tcModel = TestComModel.getTestComModelInstance();
        int selRow = tcModel.selRow();
        String query;
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            query = "SELECT * FROM RANK WHERE ID = " +
                    tcModel.getCompetitions().get(selRow).getRankId() + ";";
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            //get items
            while (rs.next()) {
                rank.setId(rs.getInt(1));
                rank.setFullName(rs.getString(2));
                rank.setRequirements(rs.getString(3));
                rank.setProgramStructure(rs.getString(4));
                rank.setProgramsCount(rs.getInt(5));
            }
            prst.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not take selected rank from DB", ex);
        }
    }

    //components***
    private void setComponents() {
        components.clear();
        singleComPage.getCompPanel().removeAll();
        String query;
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            query = "SELECT COMPONENT_RANK_LINK.IDcomponent, " +
                    "COMPONENT_RANK_LINK.IDrank, " +
                    "COMPONENT.FullNameENG, COMPONENT.FullNameRUS, " +
                    "COMPONENT.Description " +
                    "FROM COMPONENT_RANK_LINK, COMPONENT " +
                    "WHERE IDrank = " + competition.getRankId() + " " +
                    "AND COMPONENT_RANK_LINK.IDcomponent = COMPONENT.ID;";
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            while (rs.next()) {
                Component component = new Component();
                component.setId(rs.getInt(1));
                component.setRankId(rs.getInt(2));
                component.setFullNameENG(rs.getString(3));
                component.setFullNameRUS(rs.getString(4));
                component.setDescription(rs.getString(5));
                components.add(component);
            }
            prst.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                    log(Level.SEVERE,
                            "Do not take selected rank from DB", ex);
        }
    }

    public void setAllData() {
        //get the last link of panel
        singleComPage = Manager.getSingleComPage();

        upDoNothingWithListenersFlag();

        //set mode empty
        mode = 0;

        CIARS.clear();
        CIAR = null;
        singleComPage.clearTopPanelResults();

        //get and set a competition
        getSelCompetition();

        //set name of competition and rank
        singleComPage.setFullName(competition.getFullName());
        setRank();
        singleComPage.setRank(rank.getFullName());

        //clearing arrays are at these methods
        singleComPage.getElRows().clear();
        singleComPage.getCompRows().clear();

        /*add to competition_performance_athlete_link
        and set ciar for all athletes by competition*/
        setAthletes();
        for (Athlete athlete : athletesByComp) {
            CIARS.get(athlete.getId()).setCompetitionAthlId(getCompetitionAthlId(athlete));
        }

        setJudges();
        setTypes();
        setElements();
        setComponents();

        this.factor = IsuElementsData.getFactor(this.competition.getRankId());
        singleComPage.createLbls();
        downDoNothingWithListenersFlag();
    }

    public boolean checkDeductionsAndComponentsValue(String deductions) {
        Pattern p = Pattern.compile("^\\d+?((\\.|\\,)[0-9]{0,2})?$");
        Matcher m = p.matcher(deductions);
        return m.matches();
    }

    public void getCIARsFromDB(Athlete athlete) {
        //all final results by competition
        PreparedStatement prst = null;
        ResultSet rs = null;
        String query;

        try {
            query = String.format("select R.IDathlete, A.Surname, A.Name, A.Middlename, A.IDrank, A.Sex, " +
                    "R.ID as IDresult, R.Place, R.SumResult, R.isDone, R.StartNumber, R.SumOfAllElements, " +
                    "R.SumOfAllComponents, R.Deductions, CPAL.isFinished " +
                    "from ATHLETE as A " +
                    "join RESULT as R on A.ID = R.IDathlete " +
                    "join COMPETITION_PERFORMANCE_ATHLETE_LINK as CPAL on " +
                    "R.IDcompetition = CPAL.IDcompetition and A.ID = CPAL.idAthlete " +
                    "where R.IDcompetition = %d and R.IDathlete = %d", competition.getId(), athlete.getId());

            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            System.out.println(query);

            while (rs.next()) {
                athlete.setId(rs.getInt(1));
                athlete.setSurname(rs.getString(2));
                athlete.setName(rs.getString(3));
                athlete.setMiddlename(rs.getString(4));
                athlete.setIdrank(rs.getInt(5));
                athlete.setSex(rs.getBoolean(6));

                CompetitionIsuAthleteResult CIAR = CIARS.get(athlete.getId());

                //result field
                CIAR.setResultId(rs.getInt(7));
                CIAR.setPlace(rs.getInt(8));
                CIAR.setTotalScore(rs.getFloat(9));
                CIAR.setIsDone(rs.getBoolean(10));
                CIAR.setStartNumber(rs.getInt(11));
                CIAR.setElementScore(rs.getFloat(12));
                CIAR.setComponentScore(rs.getFloat(13));
                CIAR.setDeductions(rs.getFloat(14));
                CIAR.setFinished(rs.getBoolean(15));
            }

        } catch (SQLException ex) {
            Logger.getLogger(IsuComModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Integer> getJudgesIDs() {
        return getJudgesByComp().stream()
                .map(Judge::getId)
                .collect(Collectors.toList());
    }

    //by selected athlete
    public void getComponentsResultFromDB(int athleteId) {
        PreparedStatement prst = null;
        ResultSet rs = null;
        String query = String.format("select IDcomponent, Value, IDjudge " +
                "from ALL_RESULTS_COMPONENTS as ARC " +
                "inner join COMPETITION_PERFORMANCE_ATHLETE_LINK as CPAL on " +
                "ARC.IDcompetitionPerformanceAthleteLink = CPAL.ID " +
                "where CPAL.IDcompetition = %d and CPAL.IDathlete = %d;", competition.getId(), athleteId);
        try {
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();

            float score;
            for (ComponentRow compRow : singleComPage.getCompRows()) {
                int judgesNumbers = getJudgesByComp().size();

                List<Integer> judgesIDs = getJudgesIDs();

                score = 0;
                ComponentIsu compIsu = compRow.getComponentIsu();
                while (rs.next()) {
                    //values
                    ComponentValue compValue = compRow.getComponentIsu().getJudgesValues().get(judgesIDs.get(getJudgesByComp().size() - judgesNumbers));
                    compValue.setComponentId(rs.getInt(1));
                    compValue.setValue(rs.getFloat(2));
                    compValue.setJudgeId(rs.getInt(3));
                    compValue.setSaved(true);

                    //data
                    compIsu.setComponentId(compValue.getComponentId());

                    score += compValue.getValue();
                    if (--judgesNumbers == 0) break;
                }
                compIsu.setScores(score);

                for (ComponentIsu ci : CIARS.get(athleteId).getComponentsList()) {
                    if (ci.getComponentId() == compIsu.getComponentId()) {
                        ci = compIsu;
                        break;
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(IsuComModel.class.getName()).log(Level.SEVERE, "ERROR WITH GETTING COMPONENTS RESULT", ex);
        }
    }

    public void setComponentResultsToFields() {
        List<Integer> judgesIDs = getJudgesIDs();
        for (ComponentRow compRow : singleComPage.getCompRows()) {
            int index = 0;
            for (JTextField tf : compRow.getJudgeMarks()) {
                String text = String.valueOf(compRow.getComponentIsu().getJudgesValues().get(judgesIDs.get(index++)).getValue());
                if (text.equals("0.0")) text = "";
                tf.setText(text);
            }
            String scoreValue = String.valueOf(compRow.getComponentIsu().getScores());
            compRow.setScoreText(scoreValue.equals("0.0") ? "" : scoreValue);
        }
    }

    public int getCompetitionAthlId(Athlete athlete) {
        PreparedStatement prst = null;
        ResultSet rs = null;
        int id = 0;

        try {
            String str = "SELECT ID FROM COMPETITION_PERFORMANCE_ATHLETE_LINK " +
                    "WHERE IDathlete = " + athlete.getId() + " AND " +
                    "IDcompetition = " + this.getCompetition().getId() + ";";
            String str1 = "INSERT INTO COMPETITION_PERFORMANCE_ATHLETE_LINK VALUES (" +
                    this.getCompetition().getId() + ", " + athlete.getId() +
                    ", null, 0);";

            prst = this.getDBC().prepareStatement(str);
            rs = prst.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            } else {
                prst = this.getDBC().prepareStatement(str1);
                prst.executeUpdate();

                prst = this.getDBC().prepareStatement(str);
                rs = prst.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SetAthlete.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                prst.close();
            } catch (SQLException ex) {
                Logger.getLogger(SetAthlete.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return id;
    }

    public void getElementsResultFromDB(int IDathlete) {
        PreparedStatement prst = null;
        ResultSet rs = null;
        float score;
        int elementNumber = 1;

        String withMarks = String.format("select IDType, IDisuElement, Info, Base, IDjudge, Mark " +
                "from (select IDcompetitionPerformanceAthleteLink, Base, Info, Mark, IDjudge, IDcompetition, IDathlete, IDisuElement " +
                "from ALL_RESULTS_ELEMENTS as ARE " +
                "join COMPETITION_PERFORMANCE_ATHLETE_LINK as CPAL on ARE.IDcompetitionPerformanceAthleteLink = CPAL.ID " +
                "where CPAL.IDcompetition = %d and IDathlete = %d) as tech " +
                "join (select E.FullNameRUS, E.Abbreviation, T.ID as IDType, T.FullName, E.ID from ISU_ELEMENT as E join ISU_ELEMENT_TYPE as T on E.IDelementType = T.ID) as elem " +
                "on tech.IDisuElement = elem.ID", competition.getId(), IDathlete);
        try {
            System.out.println(withMarks);
            CIARS.get(IDathlete).getElementsList().clear();
            prst = getDBC().prepareStatement(withMarks);
            rs = prst.executeQuery();

            ElementIsu elIsu = null;

            int elementTypeId = 0;
            int elementId = 0;
            Integer judgeId = null;
            Integer mark = null;

            int judgesNumbers = 0;
            int judges = singleComPage.getLstModel().getSize();

            while (rs.next()) {

                if (judgesNumbers == 0) {
                    judgesNumbers = judges;
                    elIsu = new ElementIsu();

                    //to data
                    elementTypeId = rs.getInt(1);
                    elIsu.setElementTypeId(elementTypeId);

                    elementId = rs.getInt(2);
                    elIsu.setElementId(elementId);
                    elIsu.setName(allElements.get(elementId).toString());

                    elIsu.setInfo(rs.getString(3));
                    elIsu.setBaseValue(rs.getFloat(4));

                    //to athlete ciar
                    CIARS.get(IDathlete).getElementsList().add(elIsu);
                    elIsu.setSaved(true);
                }

                //fill elementVal
                ElementValue elVal = new ElementValue();
                judgeId = rs.getInt(5);
                mark = (Integer) rs.getObject(6);

                elVal.setElementId(elementId);
                elVal.setJudgeId(judgeId);
                elVal.setMark(mark);
                elVal.setSaved(true);

                //add a mark to result row
                elIsu.addJudgeValue(judgeId, elVal);

                judgesNumbers--;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                prst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getStartNumber() {
        PreparedStatement prst = null;
        ResultSet rs = null;
        String query = String.format("select StartNumber from result where IDcompetition = %d", competition.getId());
        System.out.println(query);
        List<Integer> startNumbers = new ArrayList<>();
        int startNumber = 0;

        try {
            prst = getDBC().prepareStatement(query);
            rs = prst.executeQuery();
            while (rs.next()) {
                startNumbers.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                prst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (CompetitionIsuAthleteResult ciar :CIARS.values()) {
            startNumbers.add(ciar.getStartNumber());
        }

        startNumber = startNumbers.stream()
                        .distinct()
                        .sorted((o1, o2) -> -o1.compareTo(o2))
                        .findFirst()
                        .orElse(0);
        return startNumber + 1;
    }


    public Competition getCompetition() {
        return competition;
    }

    public ArrayList<Component> getComponentsList() {
        return components;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public HashMap<Integer, ElementData> getAllElements() {
        return allElements;
    }

    public HashMap<Integer, IsuElementType> getAllTypes() {
        return allTypes;
    }

    public CompetitionIsuAthleteResult getCIAR() {
        return CIAR;
    }

    public CompetitionIsuAthleteResult getCIAR(int IDathlete) {
        return CIARS.get(IDathlete);
    }

    public CompetitionIsuAthleteResult addCIARtoCIARs(int IDathlete, CompetitionIsuAthleteResult ciar) {
        return CIARS.put(IDathlete, ciar);
    }

    public HashMap<Integer, CompetitionIsuAthleteResult> getCIARS() {
        return CIARS;
    }

    public void setCIAR(CompetitionIsuAthleteResult CIAR) {
        this.CIAR = CIAR;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public static boolean isDoNothingWithListenersFlagUp() {
        return doNothingWithListenersFlag;
    }

    public static void upDoNothingWithListenersFlag() {
        doNothingWithListenersFlag = true;
    }

    public static void downDoNothingWithListenersFlag() { doNothingWithListenersFlag = false; }

    public void setDeductionSaved(boolean value) { isDeductionSaved = value; }

    public boolean isDeductionSaved() {
        String query = String.format("select ID from RESULT where RESULT.IDathlete = %d and RESULT.IDcompetition = %d",
                ((Athlete)singleComPage.getAthlCmb().getSelectedItem()).getId(), competition.getId());

        try {
            PreparedStatement prst = getDBC().prepareStatement(query);
            ResultSet rs = prst.executeQuery();

            if (rs.next()) {
                return true;
            }
            prst.close();
            rs.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return false;
    }
}
