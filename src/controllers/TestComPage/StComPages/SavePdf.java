package controllers.TestComPage.StComPages;

import data.Athlete;
import data.Competition;
import data.Element;
import data.MarkCellData;
import data.SportsmanResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.TestCom.StartCom.StComModel;
import models.TestCom.TestComModel;
import pdfreports.PdfResultsCreator;
import views.Manager;

public class SavePdf implements ActionListener {
    private TestComModel tcModel; 
    private Competition competition;
    private int competitionKind;
    private StComModel stComModel;    
    
    private String competitionName;
    private String dateOfCompetition;
    private ArrayList<Athlete> athlets = new ArrayList<>();
    private ArrayList<Element> elements = new ArrayList<>();
    private ArrayList<ArrayList<MarkCellData>> results = new ArrayList<>();
    private int mode;
    private int numberOfJudges;

    @Override
    public void actionPerformed(ActionEvent e) {
        tcModel = TestComModel.getTestComModelInstance();
        competition = tcModel.getCompetitions().get(tcModel.selRow());
        stComModel = StComModel.getStComModelInstance();
        competitionKind = competition.getKind().getId();        
        
        competitionName = competition.getFullName();
        dateOfCompetition = competition.getDate();
        elements.addAll(stComModel.getElementsByComp().values());
        results = stComModel.getMarksList();
        mode = stComModel.getMode();
        numberOfJudges = stComModel.getJudgesByComp().size();
        for (SportsmanResult sr : stComModel.getAthletesByComp().values()) {
            athlets.add(sr.getAthlete());
        }
        
        PdfResultsCreator pdf = new PdfResultsCreator();
        //SINGLE TYPE will never get there
        //GLASIAL
        if (competitionKind == 3) {
            //if empty with judges 
            if (mode == 0) {
                pdf.PdfResultsSetOfpData(competitionName, dateOfCompetition, 
                                         athlets, elements, numberOfJudges);
            } else if (mode == 1) {
                pdf.PdfResultsSetOfpData(competitionName, dateOfCompetition, athlets,
                                         elements, results, 2);  
            }
            pdf.createWorkPdfEvaluation();
        } 
        //OFP, SFP 
        else {
            //if empty with judges 
            if (mode == 0) {
                pdf.PdfResultsSetOfpData(competitionName, dateOfCompetition, 
                                         athlets, elements);

            } else if (mode == 1) {
                pdf.PdfResultsSetOfpData(competitionName, dateOfCompetition, athlets,
                                         elements, results, 2);  
            }
            pdf.createWorkPdfOfp();
        }      
        JOptionPane.showMessageDialog(Manager.getStComPage(), 
        "Pdf-документ создан!", "Инфо",
        JOptionPane.INFORMATION_MESSAGE);
    }    
}
