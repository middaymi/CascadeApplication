  package models.TestCom.StartCom;

import data.SportsmanResult;
import data.Athlete;
import data.Competition;
import data.Element;
import data.Judge;
import dataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import models.TestCom.SfpEditModel;
import models.TestCom.TestComModel;
import views.CommonSettings;
import views.Manager;
import views.TestCom.StartCom.StComPage;
import data.MarkCellData;
import data.PointsRow;
import data.PointsTable;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import views.TestCom.StartCom.MarkTextField;

import static utils.Layout.calcH;

  public class StComModel {
    private final Connection DBC = DataBaseConnection.getInstanceDataBase().
                                   getDBconnection(); 
    private StComPage stComPage;        
    protected TestComModel tcModel;
    private int mode = 0;
    
    //athlets take part in selected competition    
    private HashMap<Integer, SportsmanResult> athletesByComp = new HashMap<>();
        
    //judges take part in selected competition    
    private ArrayList<Judge> judgesByComp = new ArrayList<>();
    
    //elements of selected competition
    protected HashMap<Integer, Element> elementsByComp = new HashMap<>(); 
    
    private HashMap<Integer, PointsTable> standards = new HashMap<>();
    
    private ArrayList<ArrayList<MarkCellData>> marksList = new ArrayList();
    
    Competition competition;
    
    protected static StComModel stComModelInstance = null;
    protected StComModel() {
        tcModel = TestComModel.getTestComModelInstance();
    }    
    public static StComModel getStComModelInstance() {
        if (stComModelInstance == null) {
            stComModelInstance = new StComModel();
        }
        return stComModelInstance;
    }  
    
    //get data
    public void setTextToWelcomeLbl(String str) {
        JLabel welLbl = Manager.getStComPage().getWelcome();
        welLbl.setText(str);
        welLbl.setHorizontalTextPosition(JLabel.CENTER);
        CommonSettings.settingFont30(welLbl);
    }
    
    //is there the competition in DB?
    private boolean checkCompInDB() {
        String query = "";
        PreparedStatement prst = null;
        ResultSet rs = null;
        
        try {    
            query = "SELECT ALL_RESULTS.*, TESTS_ELEMENTS_LINK.IDcompetition, \n" +
                    "TESTS_ELEMENTS_LINK.IDisuElement \n" +
                    "FROM ALL_RESULTS, TESTS_ELEMENTS_LINK\n" +
                    "WHERE ALL_RESULTS.IDtestsElementsLilk = TESTS_ELEMENTS_LINK.ID AND \n" +
                    "TESTS_ELEMENTS_LINK.IDcompetition = " + competition.getId() + ";";
            prst = DBC.prepareStatement(query);
            rs = prst.executeQuery();
            
            while (rs.next()) {                 
                this.setMode(1);
                return true;
            }           
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private ArrayList<ArrayList<MarkCellData>> setCompetition() {
        ArrayList<ArrayList<MarkCellData>> allMarks = 
                new ArrayList<ArrayList<MarkCellData>>();
         for (SportsmanResult sr : getAthletesByComp().values()) {
             ArrayList<MarkCellData> athlMarks = new ArrayList<>();
             for (Element el : elementsByComp.values()) {
                 float value = 0.0f;
                 //MarkCellData mark = null;
                 if (competition.getKind().getId() == 1 ||
                     competition.getKind().getId() == 2) { 
                     value = getValueMCD(sr.getAthlete().getId(), el.getId());
                     MarkCellData mark = new MarkCellData(sr.getAthlete().getId(),
                                        el.getId(), null, value);
                     athlMarks.add(mark);
                 } else if (competition.getKind().getId() == 3) {
                     for (Judge judge : getJudgesByComp()) {
                         value = getValueMCD(sr.getAthlete().getId(), el.getId(), judge.getId());
                         MarkCellData mark = new MarkCellData(sr.getAthlete().getId(),
                                        el.getId(), getJudgesByComp().size(), value);
                         athlMarks.add(mark);
                     }
                 }
             }
             allMarks.add(athlMarks);
         }
         return allMarks;
    }
        
    private float getValueMCD(int athleteId, int elId, int...judId) {
        String query = "";
        PreparedStatement prst = null;
        ResultSet rs = null;
        float value = 0.0f;
        try {    
            if (competition.getKind().getId() == 1 ||
                competition.getKind().getId() == 2) {
                query = "SELECT ts.ResultValue FROM " +
                        
                        "(SELECT ALL_RESULTS.*, " +
                        "TESTS_ELEMENTS_LINK.IDcompetition, \n" +
                        "TESTS_ELEMENTS_LINK.IDisuElement \n" +
                        "FROM ALL_RESULTS, TESTS_ELEMENTS_LINK\n" +
                        "WHERE ALL_RESULTS.IDtestsElementsLilk = " +
                        "TESTS_ELEMENTS_LINK.ID AND \n" +
                        "TESTS_ELEMENTS_LINK.IDcompetition = " +
                        competition.getId() + ") as ts\n" +
                        
                        "WHERE ts.IDathlete = " + athleteId + 
                        " AND ts.IDisuElement = " + elId + ";";
            } else if (competition.getKind().getId() == 3) {
                query = "SELECT ts.ResultValue FROM " +
                        
                        "(SELECT ALL_RESULTS.*, " +
                        "TESTS_ELEMENTS_LINK.IDcompetition, \n" +
                        "TESTS_ELEMENTS_LINK.IDisuElement \n" +
                        "FROM ALL_RESULTS, TESTS_ELEMENTS_LINK\n" +
                        "WHERE ALL_RESULTS.IDtestsElementsLilk = " +
                        "TESTS_ELEMENTS_LINK.ID AND \n" +
                        "TESTS_ELEMENTS_LINK.IDcompetition = " +
                        competition.getId() + ") as ts\n" +
                        
                        "WHERE ts.IDathlete = " + athleteId + 
                        " AND ts.IDisuElement = " + elId + 
                        " AND ts.IDjudge = " + judId[0] + ";";
            }
            prst = DBC.prepareStatement(query);
            rs = prst.executeQuery();
            
            while (rs.next()) {
                value = rs.getFloat(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                   log(Level.SEVERE, null, ex);
            return value;            
        }
        return value;  
    }   
    
    public void setAllData() {    
        competition = tcModel.getCompetitions().get(tcModel.selRow());
        //clearing arrays are at these methods
        setAthletes();
        setJudges();
        setElements();
        marksList.clear();
        //remove all components at
        stComPage.getMainPanel().removeAll();
        int competitionKind = (int) tcModel.getValueAt(tcModel.selRow(), 2);

        if (competitionKind == 2) {
            loadOFPTables();
        }
        
        if (checkCompInDB()) {
            marksList.addAll(setCompetition());
        } else {
            createMarkCellsArray();
        }

        if (competitionKind == 3) {
            stComPage.setSportsmanLabels(athletesByComp.values(), 1);
            stComPage.setElementWithJudgesLabels(elementsByComp.values(), getJudgesByComp());
            stComPage.setFields(marksList, calcH(200));
        }
        else{
            stComPage.setSportsmanLabels(athletesByComp.values(), 0);
            stComPage.setElementLabels(elementsByComp.values());
            stComPage.setFields(marksList, calcH(100));
        }
    }

    public void setAllDataForFinishedCompetition() {
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
            prst = DBC.prepareStatement(query);
            rs = prst.executeQuery();           
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                   log(Level.SEVERE, 
           "Do not take athlets from DB for SFPstartComPage", ex);
        } 
        //data
        try {
            //clear the data
            getAthletesByComp().clear();            
            //get data        
            while (rs.next()) {            
                Athlete athlete = new Athlete();
                athlete.setId(rs.getInt(1));
                athlete.setName(rs.getString(3));
                athlete.setSurname(rs.getString(2));
                athlete.setMiddlename(rs.getString(4)); 
                athlete.setBirthday(rs.getDate(5));
                getAthletesByComp().put(athlete.getId(), 
                                        new SportsmanResult(athlete));                                                                
            }
            rs.close();
            prst.close();
        } catch (SQLException ex) {
                Logger.getLogger(StComModel.class.getName()).
                       log(Level.SEVERE, 
               "Do not set athlets for Competition", ex);
        }        
    }
    
    //judges***
    /*get judges, TAKING PART IN COMPETITION from DB
    save to array as data*/
    private void setJudges() {
        tcModel = TestComModel.getTestComModelInstance();        
        int selRow = tcModel.selRow();   
        stComPage = Manager.getStComPage();
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
            System.out.println(query);
            prst = DBC.prepareStatement(query);
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
            stComPage.getListModel().clear();
            //get data   
            int i = 1;
            while (rs.next()) {            
                Judge judge = new Judge();
                judge.setId(rs.getInt(1));
                judge.setSurname(rs.getString(2));
                judge.setName(rs.getString(3));
                judge.setMiddlename(rs.getString(4));                
                getJudgesByComp().add(judge);  
                String str = "<html>"+ i + ". " + judge.getSurname() + " " +
                                        judge.getName() +
                                        "<p align=left>" + 
                                        judge.getMiddlename() + 
                             "</html>";                         
                stComPage.getListModel().addElement(str);                
                i++;
            }
            prst.close();
            rs.close();
        } catch (SQLException ex) {
                Logger.getLogger(StComModel.class.getName()).
                       log(Level.SEVERE, 
                "Do not set judges for SFPstartComPage", ex);
        }        
    }
    
    //elements***
    /*get elements in COMPETITION from DB
    save to array as data*/
    private void setElements () {
        tcModel = TestComModel.getTestComModelInstance();        
        int selRow = tcModel.selRow();        
        String query;   
        String queryTableName;
        
        PreparedStatement prstTableName = null;
        ResultSet rsTableName = null;
        PreparedStatement prst = null;        
        ResultSet rs = null;                         
        //database 
        try {           
             queryTableName = "SELECT COMPETITION_KIND.TableName " +
                              "FROM COMPETITION_KIND, COMPETITION " +
                              "WHERE COMPETITION_KIND.ID = " +
                                     "COMPETITION.IDcompetitionKind " +
                              "AND COMPETITION_KIND.ID = " + 
                                    tcModel.getValueAt(selRow, 2) + ";";                
            prstTableName = DBC.prepareStatement(queryTableName);
            rsTableName = prstTableName.executeQuery(); 
            String tableName = "";
            while(rsTableName.next()) {
                tableName = rsTableName.getString(1);
            }            
            
            query = "SELECT " + tableName + ". * " + 
                    "FROM " + tableName + ", TESTS_ELEMENTS_LINK " +
                    "WHERE TESTS_ELEMENTS_LINK.IDisuElement = " + tableName + ".ID " +
                    "AND TESTS_ELEMENTS_LINK.IDcompetition = " +
                     tcModel.getValueAt(selRow, 1) + ";";
            System.out.println(query);
                        
            prst = DBC.prepareStatement(query);
            rs = prst.executeQuery(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(SfpEditModel.class.getName()).
                   log(Level.SEVERE, 
                   "Do not take elements from DB for SFPstartComPage", ex);
        }        
        //data        
        try {             
            //clear data
            getElementsByComp().clear();                                       
            //get data      
            while (rs.next()) {            
                Element element = new Element();
                element.setId(rs.getInt(1));
                //if type is OFP
                if ((int)tcModel.getValueAt(selRow, 2) == 2) {
                    element.setUnits(rs.getString(2));
                    element.setDescription(rs.getString(3)); 
                    element.setFullName(rs.getString(4));
                    //work with standarts of OFP
                    element.setTableName("VALUES_" + element.getId());
                } else {
                    element.setFullName(rs.getString(2));
                    element.setDescription(rs.getString(3)); 
                }         
                getElementsByComp().put(element.getId(), element);                   
            }
            prst.close();
            rs.close();
        } catch (SQLException ex) {
                Logger.getLogger(StComModel.class.getName()).
                       log(Level.SEVERE, 
                "Do not set elements for SFPstartComPage", ex);
        }        
    }
    
    /**
     * TODO
     */
    private void loadOFPTables(){       
        PreparedStatement prst = null;        
        ResultSet rs = null;        
        String query;
            
        for(Element element : getElementsByComp().values()) { 
            PointsTable table = new PointsTable(element.getId());
            try {
                query = "SELECT Sex, Age, MinValue, MaxValue " +
                        " FROM OFP_STANDARTS WHERE IDstandart = " +
                        element.getId() + ";";
                prst = DBC.prepareStatement(query);
                rs = prst.executeQuery();
                
                // load from DB to pointsTablesList
                ArrayList<PointsRow> rows = new ArrayList<PointsRow>();
                while (rs.next()) {
                    PointsRow row = new PointsRow();
                    row.setSex(rs.getBoolean(1));
                    row.setAge(rs.getInt(2));
                    row.setMin(rs.getFloat(3));
                    row.setMax(rs.getFloat(4));
                    rows.add(row);
                }
                table.setTable(rows);
                prst.close();
                rs.close();             
                                
                standards.put(element.getId(), table);
                
            } catch (SQLException ex) {
                Logger.getLogger(StComModel.class.getName()).log(Level.SEVERE, 
                        "OFP_STANDARTS not loaded", ex);
            }
        }
    }

    private void createMarkCellsArray() {         
        marksList.clear();
        int competitionKind = (int) tcModel.getValueAt(tcModel.selRow(), 2);       
        for(SportsmanResult sportsman : getAthletesByComp().values()) {
            ArrayList<MarkCellData> list = new ArrayList<>();
            for(Element element : getElementsByComp().values()) {
                MarkCellData markCellData = null;
                if (competitionKind == 3) {
                    for(Judge judge : getJudgesByComp()) {
                        markCellData = new MarkCellData(sportsman.getAthlete().
                                   getId(), element.getId(), judge.getId(), null);
                        list.add(markCellData);
                    }
                }
                else {
                    markCellData = new MarkCellData(sportsman.getAthlete().
                                        getId(), element.getId(), null, null);
                    list.add(markCellData);
                }
            }
            marksList.add(list);
        }
    }
    
    public void saveDataToDB(MarkTextField field) {
        PreparedStatement prst = null;                
        String query;
        
        //if not saved yet - insert
        if (!field.isSaved()){
            tcModel = TestComModel.getTestComModelInstance();        
            int selRow = tcModel.selRow();  
            
            //if glasial
            String judgeId;
            if (tcModel.getCompetitions().get(tcModel.selRow()).getKind().getId() == 3) {
                judgeId = String.valueOf(field.getData().getJudgeId()) + ", null";
            } else {
                judgeId = "null, null";                
            }
                       
            //insert to database 
            try {               
                query = "INSERT INTO ALL_RESULTS " +
                        "VALUES ((SELECT ID FROM TESTS_ELEMENTS_LINK " +
                                 "WHERE IDcompetition = " + 
                                  tcModel.getValueAt(selRow, 1) + " " +
                                 "AND IDisuElement = " +
                                  field.getData().getElementId() + "), " +
                                  field.getData().getAthleteId() + ", " +
                                  field.getData().getValue() + ", " + judgeId + ");";
                                                
                System.out.println(query);
                prst = DBC.prepareStatement(query);
                prst.execute(); 
                prst.close();
                
                //change saved flag
                field.saveToDB();

                //get ID of result
                String selectID = "SELECT IDENT_CURRENT('ALL_RESULTS')";
                Statement stmt = DBC.createStatement();
                ResultSet rs = stmt.executeQuery(selectID);
                while (rs.next()) {
                    field.setResultId(rs.getInt(1));                   
                }                
                
            } catch (SQLException ex) {
                Logger.getLogger(StComModel.class.getName()).
                       log(Level.SEVERE, 
                       "Do not save data of athlete and value into ALL_RESULT", ex);
            }           
        }
        //if already insert to db - update
        else if (field.isSaved()) {
            try {
                query = "UPDATE ALL_RESULTS " +
                        "SET ResultValue = " + field.getData().getValue() + " " +
                        "WHERE ID = " + field.getResultId() + ";";
                
                System.out.println(query);
                prst = DBC.prepareStatement(query);
                prst.execute(); 
                prst.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(StComModel.class.getName()).log(Level.SEVERE, 
                        "Do not update value", ex);
            }
        }
    }

    // after click button CalculateResults
    public void calculateResults() {
        /*
            +0. Перевод результатов в баллы (для СФП и льда они равны, для ОФП смотрим таблицы)
            +1. Расчет суммы баллов по каждому человеку
            +2. Расчет мест по каждому элементу
            (если судья один - все просто, если несколько - по судьям и элементам, т.е. каждому столбцу)
            +3. Расчет суммы мест по человеку
            +4. Расчет итоговых мест по людям
            +[5. Расчет среднего балла по человеку]
            +[6. Определение сдачи/не сдачи зачета по проходному] - ?
            +7. Сохранение результаов в БД
         */
        normalizeValues();
        calculateMarksSum();    
        calculatePlacesByElement();
        calculatePlacesSum();
        checkResults();
    }
        
    //by every element for all athletes
    private void calculatePlacesByElement() {
        //amount of elements 
        int columnsCount = marksList.get(0).size();
        
        for (int i = 0; i < columnsCount; i++) {            
            ArrayList<Integer> marks = new ArrayList<>();            
            for (int j = 0; j < marksList.size(); j++) {
                MarkCellData data = marksList.get(j).get(i);
                marks.add(data.getNormalMark());
            }
            
            Collections.sort(marks);  
            Collections.reverse(marks);
           
            for (int j = 0; j < marksList.size(); j++) {
                MarkCellData data = marksList.get(j).get(i);
                int place = marks.indexOf(data.getNormalMark()) + 1;
                //a place by every element done 
                data.setPlace(place);                   
            }           
        }               
    }
    
    //+
    private void calculateMarksSum() {
        int index = 0;
        for (SportsmanResult result : athletesByComp.values()) {
            int sum = 0;            
            for (MarkCellData data : marksList.get(index)) {
                sum += data.getNormalMark();                
            }
            index++;
            result.setSumOfMarks(sum);            
            result.setAverageMark((float)1.0 * sum / marksList.get(0).size());
            int kind = (int) tcModel.getValueAt(tcModel.selRow(), 2);
            if (kind == 3) {
                result.setIsDone(result.getAverageMark() >= 6 ? true : false);
            }
            else {
                result.setIsDone(true);
            }
        }
    }
    
    private void calculatePlacesSum() {
        ArrayList<Integer> places = new ArrayList<Integer>();
        int index1 = 0;
        for (SportsmanResult result : athletesByComp.values()) {
            int sum = 0;
            for (MarkCellData data : marksList.get(index1)) {
                sum += data.getPlace();
            }
            index1++;
            //set sum of places
            result.setSumOfRanks(sum);
            places.add(sum);           
        }   
            Collections.sort(places);                                   
                       
        for (SportsmanResult result : athletesByComp.values()) {                        
            result.setPlace(places.indexOf(result.getSumOfRanks()) + 1);
        }
    }
     
    private void checkResults() {
        int index = 0;
        for (SportsmanResult result : athletesByComp.values()) {                       
            for (MarkCellData data : marksList.get(index)) {                    
            }
            index++;            
        }
    }
    
    public void resultWindow() { 
        String str = "Соревнование завершено! \n";       
        List<SportsmanResult> marks = new ArrayList<SportsmanResult>();

        for (SportsmanResult result : athletesByComp.values()) {           
            marks.add(result);
        }                
        Collections.sort(marks);
        for (SportsmanResult result : marks) {           
            str += result.getAthlete() + " - " + result.getPlace() + " место \n";
        }
        JOptionPane.showMessageDialog(stComPage, str, "Итоги: ",
        JOptionPane.INFORMATION_MESSAGE);
    }
    
    public boolean checkAllValues() {
        int index = 0;   
        if (marksList.size() != 0) {
            for (MarkCellData data : marksList.get(index)) {
                if ((Float)data.getValue() == null) {
                    JOptionPane.showMessageDialog(stComPage, 
                    "Введите все значения для расчета", "Ошибка!",
                    JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }        
            }
        }
        else {
            return false;
        }
        return true;                 
    }
    
    public void saveResultsToDB() {
        PreparedStatement prst = null;                
        String query;        
     
        tcModel = TestComModel.getTestComModelInstance();        
        int selRow = tcModel.selRow();
        int compId = (int)tcModel.getValueAt(selRow, 1);

        //insert to database 
        try {
            for (SportsmanResult athlete : athletesByComp.values()) {
            query = "INSERT INTO RESULT " +
                    "VALUES (" + compId + ", " + athlete.getAthlete().getId() + ", " +
                    athlete.getPlace() + ", " + athlete.getSumOfMarks() + ", " +
                    ((athlete.isIsDone()) ? 1 : 0) + ", " + "null, null, null, null , " + 
                    athlete.getSumOfRanks() + ", " + athlete.getAverageMark() + ");";
             
            System.out.println(query);
            prst = DBC.prepareStatement(query);
            prst.execute(); 
            prst.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).
                   log(Level.SEVERE, 
                   "Do not save results into RESULT TABLE", ex);
        }           
    }

    /**
     * Нормализация результатов, п. 0 calculateResults
     */
    public void normalizeValues() {        
        int competitionKind = (int) tcModel.getValueAt(tcModel.selRow(), 2);        
        for (ArrayList<MarkCellData> marks : getMarksList()) {
            for (MarkCellData mark : marks) {
                if (competitionKind == 2) { // ОФП
                    Element element = getElementsByComp().get(mark.getElementId()); // какой это был элемент
                    PointsTable standard = standards.get(element.getId());  // таблица с нормативами этого элемента
                    Athlete athlete = getAthletesByComp().get(mark.getAthleteId()).getAthlete(); // какой спортсмен
                    int value = standard.getPoints(athlete.getSex(), athlete.getAge(), mark.getValue()); // получение нормализованной оценки
                    int normalMark = standard.getNormalPoints(value);
                    mark.setNormalMark(normalMark); // установка нормализованной оценки
                }
                else if (competitionKind == 1 || competitionKind == 3 ) { // СФП + ледовые зачеты
                    mark.setNormalMark((int)mark.getValue().floatValue());
                }
            }
        }
    }
    
    public void delValuesFromComp() {
         try {
            //delete from DB
            String query = "DELETE FROM RESULT WHERE IDcompetition = " + 
                    tcModel.getValueAt(tcModel.selRow(), 1) + ";";
            System.out.println(query);
            PreparedStatement prst = DBC.prepareStatement(query);
            prst.execute();
            prst.close();
         } catch (SQLException ex) {
            Logger.getLogger(StComModel.class.getName()).log(Level.SEVERE, 
                    "Do not clear data in DB it table RESULT", ex);
        } 
    }
    
    public HashMap<Integer, SportsmanResult> getAthletesByComp() {
        return athletesByComp;
    }    

    public HashMap<Integer, Element> getElementsByComp() {
        return elementsByComp;
    }

    public ArrayList<ArrayList<MarkCellData>> getMarksList() {
        return marksList;
    }

    public ArrayList<Judge> getJudgesByComp() {
        return judgesByComp;
    }

    public Connection getDBC() {
        return DBC;
    }

    public int getMode() {
        return mode;
    }
    
    public void setMode(int mode) {
        this.mode = mode;
    }

    public void clearElementAndComponentsRow() {}
}
