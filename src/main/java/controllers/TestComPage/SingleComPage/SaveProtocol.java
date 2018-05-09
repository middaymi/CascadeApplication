package controllers.TestComPage.SingleComPage;

import data.Competition;
import data.CompetitionIsuAthleteResult;
import data.Component;
import data.ComponentIsu;
import data.ElementIsu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.TestCom.StartCom.IsuComModel;
import models.TestCom.TestComModel;
import pdfreports.PdfResultsCreator;
import views.Manager;
import views.TestCom.StartCom.SingleStComPage;

public class SaveProtocol implements ActionListener {
    int numberOfJudges;
    String athleteName;
    String competitionName;
    String dateOfEvent;
    ArrayList<ElementIsu> elementsIsu = new ArrayList<>();
    int mode;
    String grade;
    float deductions;
    float componentsSum;
    float elementsSum;
    float totalScore;
    ArrayList<Component> components = new ArrayList<>();
    float factor;
    int startNumber;
    ArrayList<ComponentIsu> componentsIsu = new ArrayList<>();
    
    Competition competition;
    IsuComModel isuComModel;
    TestComModel tcModel;
    SingleStComPage singleStComPage;
    CompetitionIsuAthleteResult CIAR;
    
    public void actionPerformed(ActionEvent e) {
        tcModel = TestComModel.getTestComModelInstance();
        isuComModel = IsuComModel.getModelInstance();     
        singleStComPage = Manager.getManagerInstance().getSingleComPage();
        competition = tcModel.getCompetitions().get(tcModel.selRow());
        CIAR = isuComModel.getCIAR();
        
        if (singleStComPage.getAthlCmb().getSelectedItem() != null) {
            numberOfJudges = isuComModel.getJudgesByComp().size();
            athleteName = CIAR.getAthlete().toString();
            competitionName = competition.getFullName();
            dateOfEvent = competition.getDate();
            elementsIsu = CIAR.getElementsList();
            mode = isuComModel.getMode();
            grade = isuComModel.getRank().getFullName();
            deductions = CIAR.getDeductions();
            componentsSum = CIAR.getComponentScore();
            elementsSum = CIAR.getElementScore();
            totalScore = CIAR.getTotalScore();
            components = isuComModel.getComponentsList();
            factor = isuComModel.getFactor();
            startNumber = CIAR.getStartNumber();
            componentsIsu = CIAR.getComponentsList();       

            PdfResultsCreator pdf = new PdfResultsCreator();         
            pdf.PdfResultsSetIsuData(numberOfJudges, competitionName, athleteName, dateOfEvent,
                                     elementsIsu, mode, grade, deductions, componentsSum,
                                     elementsSum, totalScore, components, factor,
                                     startNumber, componentsIsu);
            pdf.createPdf();  
            JOptionPane.showMessageDialog(Manager.getSingleComPage(), 
            "PDF-документ создан!", "Инфо",
            JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(Manager.getSingleComPage(), 
            "Спортсмен не выбран!", "Ошибка!",
            JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }    
}
