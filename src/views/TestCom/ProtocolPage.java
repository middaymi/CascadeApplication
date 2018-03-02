package views.TestCom;

import data.Athlete;
import data.Competition;
import data.CompetitionIsuAthleteResult;
import data.Element;
import data.MarkCellData;
import data.SportsmanResult;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import models.TestCom.StartCom.IsuComModel;
import models.TestCom.StartCom.StComModel;
import models.TestCom.TestComModel;
import pdfreports.PdfResultsCreator;
import views.CommonSettings;

public class ProtocolPage extends JPanel {
    private JButton saveInPdf;
    private JPanel pnl;
    private JScrollPane scrl;
    
    private ArrayList<SportsmanResult> sr = new ArrayList<>();

    public ProtocolPage() {
        CommonSettings.panelSettings(this);
        createSaveInPdfBtn();
        createPnl(sr.size());
    }    
    
    private void createSaveInPdfBtn() {
        saveInPdf = new JButton("Сохранить в PDF");
        CommonSettings.settingFont30(saveInPdf);
        CommonSettings.settingGrayBorder(saveInPdf);
        saveInPdf.setBackground(Color.LIGHT_GRAY);
        saveInPdf.setSize(300, 100);
        saveInPdf.setLocation(2534, 1480);
        this.add(saveInPdf);
        saveInPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestComModel tcModel = TestComModel.getTestComModelInstance(); 
                StComModel stComModel = StComModel.getStComModelInstance();
                IsuComModel isuComModel = IsuComModel.getModelInstance();
                Competition competition = tcModel.getCompetitions().get(tcModel.selRow());
                int kind = competition.getKind().getId();
                
                //arraylist of elements
                ArrayList<Element> els = new ArrayList<>();
                els.addAll(stComModel.getElementsByComp().values());
                
                //arraylist of athlets
                //as <Athlete> and <SportsmenResult>
                ArrayList<Athlete> athls = new ArrayList<>();
                ArrayList<CompetitionIsuAthleteResult> forPdf = new ArrayList<>();
                
                for (SportsmanResult tempRes : sr) {
                    athls.add(tempRes.getAthlete());
                    CompetitionIsuAthleteResult temp = new CompetitionIsuAthleteResult();
                    temp.setAthlete(tempRes.getAthlete());
                    temp.setComponentScore(tempRes.getSumOfAllComponents());
                    temp.setDeductions(tempRes.getDeductions());
                    temp.setElementScore(tempRes.getSumOfAllElements());
                    temp.setIsDone(tempRes.isIsDone());
                    temp.setTotalScore(tempRes.getSumOfMarks());
                    temp.setStartNumber(tempRes.getStartNumber());
                    temp.setPlace(tempRes.getPlace());
                    forPdf.add(temp);                    
                }
                                
                //arrays of results
                ArrayList<ArrayList<MarkCellData>> marks = stComModel.getMarksList();
                 
                PdfResultsCreator pdf = new PdfResultsCreator();
                if (kind == 4) {
                    pdf.PdfResultsSetFinalIsuData(forPdf,
                                                  competition.getFullName(),
                                                  competition.getDate());
                    pdf.createFinalPdfIsu();
                } else {
                    pdf.PdfResultsSetFinalSimpleData(competition.getFullName(), 
                                                     competition.getDate(), sr);
                    pdf.createFinalPdfSimple();
                }               
                
            }
        });
        
    }
    
    private void createPnl(int countAthlets) {
        pnl = new JPanel();
        scrl = new JScrollPane(pnl);
        pnl.setLayout(null);
        pnl.setSize(2500, 70 * countAthlets + 1);
        pnl.setLocation(0, 0);
        scrl.setSize(2500, 1300);
        scrl.setLocation(334, 150);
        this.add(scrl);        
    } 
    
    private void commonDraw(JLabel lbl, boolean header) {
        if (header == true) {
            CommonSettings.settingGrayBorder(lbl);
            CommonSettings.settingFontBold30(lbl);
            lbl.setBackground(Color.GRAY);
        } else {
            CommonSettings.settingNarrowLightGrayBorder(lbl);
            CommonSettings.settingFont30(lbl);
            lbl.setBackground(Color.LIGHT_GRAY);
        }      
    }
    
    private void createResultRow(int count, SportsmanResult...sr) {
        JLabel fio = new JLabel();
        JLabel isDone = new JLabel();
        JLabel sumPlaces = new JLabel();
        JLabel sumRes = new JLabel();
        JLabel place = new JLabel();         
        
        fio.setSize(500, 70);        
        isDone.setSize(500, 70);
        sumPlaces.setSize(500, 70);
        sumRes.setSize(500, 70);
        place.setSize(500, 70);
        
        place.setLocation(0, 70*count);
        fio.setLocation(500*1, 70*count);        
        isDone.setLocation(500*4, 70*count);
        sumPlaces.setLocation(500*3, 70*count);
        sumRes.setLocation(500*2, 70*count);
                
        if (count == 0) {
            fio.setText("Спортсмен");        
            isDone.setText("Сдано/не сдано");
            sumPlaces.setText("Сумма мест");
            sumRes.setText("Сумма баллов");
            place.setText("Место");
            
            commonDraw(fio, true);
            commonDraw(isDone, true);
            commonDraw(sumPlaces, true);
            commonDraw(sumRes, true);
            commonDraw(place, true);            
            
        } else if (sr.length != 0) { 
            fio.setText((sr[0].getAthlete()).toString());        
            isDone.setText(String.valueOf((sr[0].isIsDone()) ? "✔" : "-"));
            sumPlaces.setText(String.valueOf(sr[0].getSumOfRanks()));
            sumRes.setText(String.valueOf(sr[0].getSumOfMarks()));
            place.setText(String.valueOf(sr[0].getPlace()));
            
            commonDraw(fio, false);
            commonDraw(isDone, false);
            commonDraw(sumPlaces, false);
            commonDraw(sumRes, false);
            commonDraw(place, false); 
        }         
        
        pnl.add(fio);        
        pnl.add(isDone);
        pnl.add(sumPlaces);
        pnl.add(sumRes);
        pnl.add(place);
    }
    
    private void createResultRowSingle(int count, SportsmanResult...sr) {
        JLabel fio = new JLabel();
        JLabel stNumber = new JLabel();
        JLabel isDone = new JLabel();
        JLabel sumEl = new JLabel();
        JLabel sumComp = new JLabel();
        JLabel ded = new JLabel();         
        JLabel sumRes = new JLabel();
        JLabel place = new JLabel();          
        
        fio.setSize(312, 70);  
        stNumber.setSize(312, 70);
        isDone.setSize(312, 70);
        sumEl.setSize(312, 70);
        sumComp.setSize(312, 70);
        ded.setSize(312, 70);        
        sumRes.setSize(312, 70);
        place.setSize(312, 70);
        
        fio.setLocation(0, 70*count);        
        stNumber.setLocation(312*1, 70*count);
        isDone.setLocation(312*2, 70*count);        
        sumEl.setLocation(312*3, 70*count);
        sumComp.setLocation(312*4, 70*count);
        ded.setLocation(312*5, 70*count);
        sumRes.setLocation(312*6, 70*count);
        place.setLocation(312*7, 70*count);        
                
        if (count == 0) {
            fio.setText("Спортсмен");        
            stNumber.setText("Стартовый номер");
            isDone.setText("Сдано/не сдано"); 
            sumEl.setText("Сумма по элементам");
            sumComp.setText("Сумма по компонентам");
            ded.setText("Ошибки");
            sumRes.setText("Сумма баллов");
            place.setText("Место");
            
            commonDraw(fio, true);
            commonDraw(stNumber, true);
            commonDraw(isDone, true);
            commonDraw(sumEl, true);
            commonDraw(sumComp, true);
            commonDraw(ded, true);
            commonDraw(sumRes, true);
            commonDraw(place, true);            
            
        } else if (sr.length != 0) { 
            fio.setText((sr[0].getAthlete()).toString()); 
            stNumber.setText(String.valueOf(sr[0].getStartNumber()));
            isDone.setText(String.valueOf((sr[0].isIsDone()) ? "✔" : "-"));
            sumEl.setText(String.valueOf(sr[0].getSumOfAllElements()));
            sumComp.setText(String.valueOf(sr[0].getSumOfAllComponents()));
            ded.setText(String.valueOf(sr[0].getDeductions()));
            sumRes.setText(String.valueOf(sr[0].getSumOfMarks()));
            place.setText(String.valueOf(sr[0].getPlace()));
            
            commonDraw(fio, false);
            commonDraw(stNumber, false);
            commonDraw(isDone, false);
            commonDraw(sumEl, false);
            commonDraw(sumComp, false);
            commonDraw(ded, false);
            commonDraw(sumRes, false);
            commonDraw(place, false); 
        }         
        
        pnl.add(fio);   
        pnl.add(stNumber);
        pnl.add(isDone);
        pnl.add(sumEl);
        pnl.add(sumComp);
        pnl.add(ded);
        pnl.add(sumRes);
        pnl.add(place);
    }
    
    //hook sorted by scores list
    public void createResult(List<SportsmanResult>results, int kindId) {
        int index = 0;
        //if single
        if (kindId == 4) {            
              createResultRowSingle(index++);
              for (SportsmanResult sp : results) { 
                createResultRowSingle(index++, sp);
              }
        } else {
            createResultRow(index++);
            for (SportsmanResult sp : results) { 
                createResultRow(index++, sp);                
            }         
        }      
    }

    public void setSr(ArrayList<SportsmanResult> sr) {
        this.sr = sr;
    }

    public JPanel getPnl() {
        return pnl;
    }
}
