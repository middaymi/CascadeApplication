package pdfreports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import data.MarkCellData;
import data.SportsmanResult;
import data.Athlete;
import data.CompetitionIsuAthleteResult;
import data.ElementIsu;
import data.Component;
import data.ComponentIsu;
import data.ComponentValue;
import data.ElementValue;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfResultsCreator {

    private int numberOfJudges = 1;
    private String competitionName = "Тестовое соревнование";
    private String sportsmanName = "Тестовый спортсмен";
    private String dateOfEvent = "12 сентября 2014г.";
    private ArrayList<ElementIsu> elements = new ArrayList<>();
    private int mode = 0; //marks=1, values=2, empty=0 
    private String grade = "1 юношеский";
    private int startNo = 0;
    private float deductions = 0;
    private float totalComp = 0;
    private float totalElements = 0;
    private float totalScore = 0;
    private ArrayList<ArrayList<ElementValue>> judgesMarksAndValues = new ArrayList<>();
    private ArrayList<ArrayList<ComponentValue>> judgesCompValues = new ArrayList<>();

    private ArrayList<Component> components = new ArrayList<>();
    private ArrayList<ComponentIsu> componentsIsu = new ArrayList<>();
    private float factorCompnent = 1.f;

    //For final results pdf creation
    ArrayList<SportsmanResult> resultsSimple = new ArrayList<>();
    ArrayList<CompetitionIsuAthleteResult> resultsIsu = new ArrayList<>();

    //For OFP , SFP and Evaluation (aka Zachot)
    ArrayList<Athlete> participantsOfp = new ArrayList<>();
    ArrayList<data.Element> elementsOfp = new ArrayList<>();
    ArrayList<ArrayList<MarkCellData>> resultsOfp = new ArrayList<>();

    private Font myFont;
    private Font myBoldFont;

    public PdfResultsCreator() {

    }

    /**
     * This is a test constructor will be commented in release Build
     */
    /*
    public void PdfResultsSetTestData() {
        elements.add(new ElementIsu("1S (Одинарный Сальхов)", "<", 0.3f));
        elements.add(new ElementIsu("1A (Одинарный Аксель)", "<", 0.33f));
        elements.add(new ElementIsu("1XZ (Элемент ХЗ)", "", 1.3f));
        elements.add(new ElementIsu("2XZ (Двойной ХЗ)", "e <", 2.0f));
        elements.add(new ElementIsu("3XZ (Тройной ХЗ)", "<<", 3.99f));

        resultsSimple.add(new SportsmanResult(1, "Горбачева Наталья", 100, 15.5f, false));
        resultsSimple.add(new SportsmanResult(2, "Милехина Елизавета", 90, 13.5f, false));
        resultsSimple.add(new SportsmanResult(3, "Милехина Анна", 100, 15.5f, false));
        resultsSimple.add(new SportsmanResult(4, "Прохорова Ирина", 100, 15.5f, true));
        resultsSimple.add(new SportsmanResult(5, "Иванова Юлия", 100, 15.5f, false));

        resultsIsu.add(new CompetitionIsuAthletResult(1, "Горбачева Наталья", 120.1f, 15.5f, 45.5f, 1.f, false));
        resultsIsu.add(new CompetitionIsuAthletResult(1, "Милехина Елизавета", 120.1f, 15.5f, 45.5f, 1.f, false));
        resultsIsu.add(new CompetitionIsuAthletResult(1, "Милехина Анна", 120.1f, 15.5f, 45.5f, 1.f, false));
        resultsIsu.add(new CompetitionIsuAthletResult(1, "Прохорова Ирина", 120.1f, 15.5f, 45.5f, 1.f, true));
        resultsIsu.add(new CompetitionIsuAthletResult(1, "Горбачева Наталья", 120.1f, 15.5f, 45.5f, 1.f, false));
        resultsIsu.add(new CompetitionIsuAthletResult(1, "Горбачева Наталья", 120.1f, 15.5f, 45.5f, 1.f, true));
        resultsIsu.add(new CompetitionIsuAthletResult(1, "Горбачева Наталья", 0.1f, 15.5f, 45.5f, 1.f, false));

        participantsOfp.add(new Athlete("Горбачева Наталья"));
        participantsOfp.add(new Athlete("Милехина Елизавета"));
        participantsOfp.add(new Athlete("Милехина Анна"));
        participantsOfp.add(new Athlete("Прохорова Ирина"));
        participantsOfp.add(new Athlete("Горбачева Наталья"));

        elementsOfp.add(new data.Element("Прыжок в длину"));
        elementsOfp.add(new data.Element("Вис на перекладине"));
        elementsOfp.add(new data.Element("Подъем туловища"));
        elementsOfp.add(new data.Element("Съедение макарон"));
        elementsOfp.add(new data.Element("Челночный бег"));
        elementsOfp.add(new data.Element("Наклоны"));
        elementsOfp.add(new data.Element("Наклоны2"));

        for (int i = 0; i < participantsOfp.size(); i++) {
            resultsOfp.add(new ArrayList<MarkCellData>());
            for (int j = 0; j < elementsOfp.size() * 5; j++) {
                resultsOfp.get(i).add(new MarkCellData(j));
            }
        }

        for (int i = 0; i < 5; i++) {
            this.components.add(new Component("Test" + i));
            this.componentsIsu.add(new ComponentIsu(10f));
        }

        for (int i = 0; i < elements.size(); i++) {
            this.judgesMarksAndValues.add(new ArrayList<>());
            for (int j = 0; j < this.numberOfJudges; j++) {
                this.judgesMarksAndValues.get(i).add(new ElementValue());
            }
        }
        for (int i = 0; i < components.size(); i++) {
            this.judgesCompValues.add(new ArrayList<>());
            for (int j = 0; j < this.numberOfJudges; j++) {
                this.judgesCompValues.get(i).add(new ComponentValue());
            }
        }

        myFont = setFont();
        myBoldFont = setBoldFont();
    }
*/
    /**
     *
     * @param participants is ArrayList<Athlete>
     * @param elementsOfp_
     * @param resultsOfp_
     * @param mode empty=0
     */
    public void PdfResultsSetOfpData(String compName, String dateOfComp, ArrayList<Athlete> participants,
            ArrayList<data.Element> elementsOfp_,
            ArrayList<ArrayList<MarkCellData>> resultsOfp_, int mode) {
        this.competitionName = compName;
        this.dateOfEvent = dateOfComp;

        participantsOfp = participants;
        elementsOfp = elementsOfp_;
        if (resultsOfp_ != null) {
            resultsOfp = resultsOfp_;
        } else {
            resultsOfp = new ArrayList<>(elementsOfp_.size());
        }

        this.mode = mode;
        myFont = setFont();
        myBoldFont = setBoldFont();
    }

    /**
     * Constructor for OFP results for empty mode
     */
    public void PdfResultsSetOfpData(String compName, String dateOfComp, ArrayList<Athlete> participants,
            ArrayList<data.Element> elementsOfp_) {

        PdfResultsSetOfpData(compName, dateOfComp, participants, elementsOfp_, null, 0);
    }

    /**
     * Constructor for OFP results for empty mode with Judges
     */
    public void PdfResultsSetOfpData(String compName, String dateOfComp, ArrayList<Athlete> participants,
            ArrayList<data.Element> elementsOfp_, int numberOfJudges) {
        PdfResultsSetOfpData(compName, dateOfComp, participants, elementsOfp_, null, 0);
        this.numberOfJudges = numberOfJudges;
    }

    public void PdfResultsSetIsuData(int numberOfJudges, String competitionName, String sportsmanName, String dateOfEvent,
            HashMap<Integer, ElementIsu> elements, int mode, String grade, float deductions, float totalComp, float totalElements,
            float totalScore, ArrayList<Component> components, float factorComponent, int startNo, HashMap<Integer, ComponentIsu> componentsIsu) {

        ArrayList<ElementIsu> elementsIsu = new ArrayList<>();
        ArrayList<ComponentIsu> componentsForIsu = new ArrayList<>();
        ArrayList<Integer> keysForElemetns = new ArrayList();
        ArrayList<Integer> keysForComponents = new ArrayList();

        for (Integer element : elements.keySet()) {
            keysForElemetns.add(element);
        }
        Collections.sort(keysForElemetns);
        for (Integer key : keysForElemetns) {
            elementsIsu.add(elements.get(key));
        }
        for (Integer component : componentsIsu.keySet()) {
            keysForComponents.add(component);
        }
        Collections.sort(keysForComponents);
        for (Integer key : keysForComponents) {
            elementsIsu.add(elements.get(key));
        }

        PdfResultsSetIsuData(numberOfJudges, competitionName, sportsmanName, dateOfEvent,
                elementsIsu, mode, grade, deductions, totalComp, totalElements,
                totalScore, components, factorComponent, startNo, componentsForIsu);

    }

    /**
     * *
     * For getting ready to ISU protocols
     *
     * @param numberOfJudges
     * @param competitionName
     * @param sportsmanName
     * @param dateOfEvent
     * @param elements
     * @param mode
     * @param grade
     * @param deductions
     * @param totalComp
     * @param totalElements
     * @param totalScore
     * @param components
     * @param factorComponent
     * @param startNo
     * @param componentsIsu
     */
    public void PdfResultsSetIsuData(int numberOfJudges, String competitionName, String sportsmanName, String dateOfEvent,
            ArrayList<ElementIsu> elements, int mode, String grade, float deductions, float totalComp, float totalElements,
            float totalScore, ArrayList<Component> components, float factorComponent, int startNo, ArrayList<ComponentIsu> componentsIsu) {
        myFont = setFont();
        myBoldFont = setBoldFont();
        this.numberOfJudges = numberOfJudges;
        this.competitionName = competitionName;
        this.sportsmanName = sportsmanName;
        this.dateOfEvent = dateOfEvent;
        this.elements = elements;
        this.mode = mode;
        this.grade = grade;
        this.deductions = deductions;
        this.totalComp = totalComp;
        this.totalElements = totalElements;
        this.totalScore = totalScore;
        this.components = components;
        this.factorCompnent = factorComponent;
        this.startNo = startNo;
        this.componentsIsu = componentsIsu;

        for (int i = 0; i < this.elements.size(); i++) {
            ArrayList<ElementValue> elementValue = new ArrayList<ElementValue>();
            if (elements.get(i) != null && elements.get(i).getJudgesValues() != null && !elements.get(i).getJudgesValues().isEmpty()) {
                for (ElementValue value : elements.get(i).getJudgesValues().values()) {
                    elementValue.add(value);
                }
                this.judgesMarksAndValues.add(elementValue);
            }
        }

        for (ComponentIsu component : componentsIsu) {
            ArrayList<ComponentValue> componentValue = new ArrayList<ComponentValue>();
            if (component != null && component.getJudgesValues() != null && !component.getJudgesValues().isEmpty()) {
                for (ComponentValue value : component.getJudgesValues().values()) {
                    componentValue.add(value);
                }
                this.judgesCompValues.add(componentValue);
            }
        }
    }

    /**
     * Creates Isu results table for a single sportsman
     */
    public void createPdf() {
        try {
            processWork();
        } catch (DocumentException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates Final table for non-Isu comp types
     */
    public void createFinalPdfSimple() {
        try {
            createFinalPdfSimpleImpl();
        } catch (DocumentException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates Final table for Isu comp types
     */
    public void createFinalPdfIsu() {
        try {
            createFinalPdfIsuImpl();
        } catch (DocumentException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Work table for OFP & SFP
     */
    public void createWorkPdfOfp() {
        try {
            createWorkPdfOfpImpl(false);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Work table for ice non-Isu Competition
     */
    public void createWorkPdfEvaluation() {
        try {
            createWorkPdfOfpImpl(true);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Font setFont(int size) {
        try {
            BaseFont myAarial = null; //подключаем файл шрифта, который поддерживает кириллицу
            myAarial = BaseFont.createFont("C:\\windows\\fonts\\times.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font myFont = new Font(myAarial, size);
            return myFont;
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Font();
    }

    private Font setFont() {
        return setFont(12);
    }

    private Font setBoldFont() {
        try {
            BaseFont myAarial = null; //подключаем файл шрифта, который поддерживает кириллицу
            myAarial = BaseFont.createFont("C:\\windows\\fonts\\times.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font myFont = new Font(myAarial, 12, Font.BOLD);
            return myFont;
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(PdfResultsCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Font();
    }

    public static void main(String[] args) throws DocumentException, IOException {
//        PdfResultsCreator pdfKa = new PdfResultsCreator();
//        pdfKa.PdfResultsSetTestData();
//        pdfKa.mode = 0;
//        pdfKa.createWorkPdfEvaluation();
    }

    public void processWork() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 20, 20, 40, 20);
        Random r = new Random();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(this.competitionName + "_" + this.sportsmanName + "_" +this.dateOfEvent + ".pdf"));
        document.open();
        addMetaData(document);

        //document.newPage();
        int numberOfColumns = 8; //useds in main layout
        // a table with <numberOfColumns> columns
        PdfPTable table = new PdfPTable(numberOfColumns);
        //setWidth to maximum
        table.setWidthPercentage((float) 100.);

        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(16f);
        cell.setBorder(PdfPCell.NO_BORDER);

        //setWidth to maximum
        table.setWidthPercentage((float) 100.);
        // the cell object
        PdfPCell cellCaption;
        // we add a cell with colspan 3
        String caption = this.competitionName + "\n" + this.dateOfEvent + "\n\n";
        cellCaption = new PdfPCell(new Phrase(caption, myBoldFont));
        cellCaption.setColspan(numberOfColumns);
        cellCaption.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cellCaption);

        PdfPCell athleteAndGrade = new PdfPCell();
        String nameGradeBase = this.sportsmanName + "\n" + this.grade;
        athleteAndGrade.setPhrase(new Phrase(nameGradeBase, myBoldFont));
        athleteAndGrade.setColspan(2);
        athleteAndGrade.setBorder(PdfPCell.NO_BORDER);
        table.addCell(athleteAndGrade);

        cell.setBorder(PdfPCell.BOX);
        cell.setFixedHeight(20f);

        PdfPCell mainResultsCell = createMainResutlsTable();
        mainResultsCell.setColspan(numberOfColumns - 2);
        table.addCell(mainResultsCell);

        //empty lines
        cell.setColspan(numberOfColumns);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        PdfPCell theBigTable = createTheBigTable();
        theBigTable.setBorder(PdfPCell.BOX);
        theBigTable.setColspan(numberOfColumns);

        table.addCell(theBigTable);

        //table.
        document.add(table);

        document.close();
        writer.close();
    }

    private void addMetaData(Document document) {
        document.addTitle("Competition results");
        document.addSubject("Competition results");
        document.addAuthor("Beaver");
        document.addCreator("Beaver");
    }

    private PdfPCell createMainResutlsTable() {

        PdfPCell mainResultsCell = new PdfPCell();
        //mainResultsCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(20f);
        mainResultsCell.setPadding(4);

        mainResultsCell.setBorder(PdfPCell.NO_BORDER);
        //Forming res table
        int numberOfColumnsInresults = 5;
        PdfPTable mainResultsTable = new PdfPTable(numberOfColumnsInresults);
        mainResultsTable.setWidthPercentage(100f);
        mainResultsTable.addCell("Start No");
        mainResultsTable.addCell("Total Score");
        mainResultsTable.addCell("Technical Score");
        mainResultsTable.addCell("Component Score");
        mainResultsTable.addCell("Deduction");
        if (mode != 0) {
            mainResultsTable.addCell(this.startNo + "");
            mainResultsTable.addCell(String.format("%.2f", this.totalScore));
            mainResultsTable.addCell(String.format("%.2f", this.totalElements));
            mainResultsTable.addCell(String.format("%.2f", this.totalComp));
            mainResultsTable.addCell(String.format("%.1f", this.deductions));
        } else {
            cell.setPhrase(new Phrase(" "));
            for (int i = 0; i < numberOfColumnsInresults; i++) {
                mainResultsTable.addCell(cell);
            }
        }
        mainResultsCell.addElement(mainResultsTable);
        return mainResultsCell;
    }

    private PdfPCell createTheBigTable() throws DocumentException {
        PdfPCell cellWithData = new PdfPCell();

        PdfPTable isuTable = new PdfPTable(this.numberOfJudges + 6);
        isuTable.setWidthPercentage(100.f);
        //assigning widths
        String[] headers = new String[this.numberOfJudges + 6];
        //{"#", "Elements", "Info", "Base Value", "Score"};
        int[] widths = new int[this.numberOfJudges + 6];
        //number 
        widths[0] = 10;
        headers[0] = "№";
        //name
        widths[1] = 100;
        headers[1] = "Elements";
        //info
        widths[2] = 14;
        headers[2] = "Info";
        //Base
        widths[3] = 20;
        headers[3] = "Base";

        for (int i = 0; i < this.numberOfJudges; i++) {
            widths[4 + i] = 12;
            headers[4 + i] = "J" + (i + 1);
        }
        widths[widths.length - 2] = 30; // empty column
        headers[widths.length - 2] = " ";
        widths[widths.length - 1] = 20; // Score
        headers[widths.length - 1] = "Score";
        //set widths
        isuTable.setWidths(widths);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setMinimumHeight(12f);
        for (int i = 0; i < this.numberOfJudges + 6; i++) {
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPhrase(new Phrase(headers[i], myBoldFont));
            isuTable.addCell(cell);
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.BOX);
        //Filling in Elements data
        for (int i = 0; i < this.elements.size(); i++) {
            cell.setPhrase(new Phrase((i + 1) + ""));
            isuTable.addCell(cell);
            cell.setPhrase(new Phrase(elements.get(i).getName(), myFont));
            isuTable.addCell(cell);
            if (mode != 0) {
                cell.setPhrase(new Phrase(elements.get(i).getInfo(), myFont));
            } else {
                cell.setPhrase(new Phrase(" "));
            }

            isuTable.addCell(cell);
            cell.setPhrase(new Phrase(String.format("%.2f", elements.get(i).getBaseValue())));
            isuTable.addCell(cell);
            for (int j = 0; j < this.numberOfJudges; j++) {
                String markOrValue = (mode == 0) ? " "
                        : (mode == 1) ? String.format("%d", this.judgesMarksAndValues.get(i).get(j).getMark())
                                : String.format("%.2f", this.judgesMarksAndValues.get(i).get(j).getValue());
                cell.setPhrase(new Phrase(markOrValue));
                isuTable.addCell(cell);
            }
            cell.setPhrase(new Phrase(" "));
            isuTable.addCell(cell);
            if (mode != 0) {
                cell.setPhrase(new Phrase(String.format("%.2f", elements.get(i).getScores())));
            } else {
                cell.setPhrase(new Phrase(" "));
            }
            isuTable.addCell(cell);

        }
        //epmty line
        cell.setPhrase(new Phrase(" "));
        cell.setColspan(this.numberOfJudges + 6);
        cell.setMinimumHeight(20);
        isuTable.addCell(cell);
        //set Heighth back
        cell.setMinimumHeight(12f);
        //Filling in Table with Components
        //1. Captiion
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPhrase(new Phrase("Program Components", myBoldFont));
        isuTable.addCell(cell);
        cell.setColspan(1);
        cell.setPhrase(new Phrase("Factor", myFont));
        isuTable.addCell(cell);
        cell.setColspan(this.numberOfJudges + 2);
        cell.setPhrase(new Phrase(" "));
        isuTable.addCell(cell);

        //Filling In Components
        PdfPCell nameCell = new PdfPCell();
        nameCell.setColspan(2);
        nameCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(1);

        for (int i = 0; i < this.components.size(); i++) {
            isuTable.addCell((i + 1) + "");
            nameCell.setPhrase(new Phrase(this.components.get(i).getFullNameRUS(), myFont));
            isuTable.addCell(nameCell);
            PdfPCell compScore = new PdfPCell();
            Font small = setFont(12);
            String tempStr = String.format("%.2f", this.factorCompnent);
            compScore.setPhrase(new Phrase(tempStr,small));
            isuTable.addCell(compScore);
            for (int j = 0; j < this.numberOfJudges; j++) {
                if (mode != 0) {
                    cell.setPhrase(new Phrase(this.judgesCompValues.get(i).get(j).getValue() + "", small));
                } else {
                    cell.setPhrase(new Phrase(" "));
                }
                isuTable.addCell(cell);
            }
            cell.setPhrase(new Phrase(" "));
            isuTable.addCell(cell);
            if (mode != 0) {
                cell.setPhrase(new Phrase(String.format("%.2f", componentsIsu.get(i).getScores())));
            } else {
                cell.setPhrase(new Phrase(" "));
            }
            isuTable.addCell(cell);
        }
        cellWithData.addElement(isuTable);
        return cellWithData;
    }

    /**
     * Constructor for Simple final results PDF creation mode
     *
     * @param compName
     * @param dateOfComp
     * @param results
     */
    public void PdfResultsSetFinalSimpleData(String compName, String dateOfComp, ArrayList<SportsmanResult> results) {
        this.competitionName = compName;
        this.dateOfEvent = dateOfComp;
        this.resultsSimple = results;
        myFont = setFont();
        myBoldFont = setBoldFont();
    }

    private void createFinalPdfSimpleImpl() throws FileNotFoundException, DocumentException {

        Document document = new Document(PageSize.A4, 20, 20, 40, 20);
        Random r = new Random();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("report-" + this.competitionName + "_" + this.dateOfEvent + ".pdf"));
//      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("report-" + r.nextInt() + ".pdf"));
        document.open();
        addMetaData(document);

        //our temporary cell
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(16f);
        cell.setBorder(PdfPCell.NO_BORDER);

        //main layout - table N*8
        int numberOfColumns = 8; //useds in main layout
        // a table with <numberOfColumns> columns
        PdfPTable table = new PdfPTable(numberOfColumns);
        //setWidth to maximum
        table.setWidthPercentage(100.f);
        // the cell object
        PdfPCell cellCaption;
        // we add a cell with colspan 3
        String caption = this.competitionName + "\n" + this.dateOfEvent + "\n\n";
        cellCaption = new PdfPCell(new Phrase(caption, myBoldFont));
        cellCaption.setColspan(numberOfColumns);
        cellCaption.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cellCaption);

        //Creating main table
        PdfPCell cellWithTable = new PdfPCell();
        cellWithTable.setColspan(6);
        cellWithTable.setBorder(PdfPCell.NO_BORDER);

        PdfPTable tableWithResults = new PdfPTable(5);
        tableWithResults.setWidthPercentage(100.f);
        tableWithResults.setWidths(new int[]{30, 90, 40, 30, 30});
        //creating header row
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.BOX);

        cell.setPhrase(new Phrase("Место", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("ФИО", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Результат", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Средний балл", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Зачет", myFont));
        tableWithResults.addCell(cell);

        //cycle to fill in Results from class field resultsSimple ArrayList
        for (int i = 0; i < this.resultsSimple.size(); i++) {
            cell.setPhrase(new Phrase(this.resultsSimple.get(i).getPlace() + "", myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(this.resultsSimple.get(i).getAthlete().toString(), myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(this.resultsSimple.get(i).getSumOfMarks() + "", myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(this.resultsSimple.get(i).getAverageMark() + "", myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(this.resultsSimple.get(i).isIsDone() ? "+" : " ", myFont));
            tableWithResults.addCell(cell);
        }

        cellWithTable.addElement(tableWithResults);
        table.addCell(cellWithTable);

        //final setup
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPhrase(null);
        cell.setColspan(numberOfColumns - 6);
        table.addCell(cell);

        //Add table to PDF
        document.add(table);
        document.close();
        writer.close();

    }

    /**
     * Constructor for ISU final results PDF creation mode
     *
     * @param compName
     * @param dateOfComp
     * @param resultsIsu
     */
    public void PdfResultsSetFinalIsuData(ArrayList<CompetitionIsuAthleteResult> resultsIsu, String compName, String dateOfComp) {
        this.competitionName = compName;
        this.dateOfEvent = dateOfComp;
        this.resultsIsu = resultsIsu;
        myFont = setFont();
        myBoldFont = setBoldFont();
    }

    private void createFinalPdfIsuImpl() throws FileNotFoundException, DocumentException {

        Document document = new Document(PageSize.A4, 20, 20, 40, 20);
        Random r = new Random();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("report-" + this.competitionName + "_" + this.dateOfEvent + ".pdf"));
//      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("report-" + r.nextInt() + ".pdf"));
        document.open();
        addMetaData(document);

        //our temporary cell
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(16f);
        cell.setBorder(PdfPCell.NO_BORDER);

        //main layout - table N*8
        int numberOfColumns = 8; //useds in main layout
        // a table with <numberOfColumns> columns
        PdfPTable table = new PdfPTable(numberOfColumns);
        //setWidth to maximum
        table.setWidthPercentage(100.f);

        // the cell object
        PdfPCell cellCaption;
        // we add a cell with colspan 3
        String caption = this.competitionName + "\n" + this.dateOfEvent + "\n\n";
        cellCaption = new PdfPCell(new Phrase(caption, myBoldFont));
        cellCaption.setColspan(numberOfColumns);
        cellCaption.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cellCaption);

        //Creating main table
        PdfPCell cellWithTable = new PdfPCell();
        cellWithTable.setColspan(7);
        cellWithTable.setBorder(PdfPCell.NO_BORDER);
        int widthInCells = 7; //max is numberOfColumns
        PdfPTable tableWithResults = new PdfPTable(widthInCells);
        tableWithResults.setWidthPercentage(100.f);
        tableWithResults.setWidths(new int[]{35, 120, 40, 45, 65, 35, 35});
        //creating header row
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.BOX);

        cell.setPhrase(new Phrase("Место", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("ФИО", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Сумма", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Техника", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Компоненты", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Ошибки", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("Разряд", myFont));
        tableWithResults.addCell(cell);

        //cycle to fill in Results from class field resultsSimple ArrayList
        for (int i = 0; i < this.resultsIsu.size(); i++) {
            cell.setPhrase(new Phrase((this.resultsIsu.get(i).getPlace()) + "", myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(this.resultsIsu.get(i).getAthlete().toString(), myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(String.format("%.2f", this.resultsIsu.get(i).getTotalScore()), myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(String.format("%.2f", this.resultsIsu.get(i).getElementScore()), myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(String.format("%.2f", this.resultsIsu.get(i).getComponentScore()), myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(String.format("%.2f", this.resultsIsu.get(i).getDeductions()), myFont));
            tableWithResults.addCell(cell);
            cell.setPhrase(new Phrase(this.resultsIsu.get(i).isDone() ? "+" : " ", myFont));
            tableWithResults.addCell(cell);
        }

        cellWithTable.addElement(tableWithResults);
        table.addCell(cellWithTable);

        //final setup
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPhrase(null);
        cell.setColspan(numberOfColumns - widthInCells);
        table.addCell(cell);

        //Add table to PDF
        document.add(table);
        document.close();
        writer.close();

    }

    private void createWorkPdfOfpImpl(boolean needJudjes) throws DocumentException, FileNotFoundException {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 40, 20);
        Font small = this.setFont(8);
        String fileName = this.competitionName + "_" + this.dateOfEvent + ".pdf";
        fileName = fileName.replaceAll("[()\n]", "");
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        addMetaData(document);

        //our temporary cell
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(16f);
        cell.setBorder(PdfPCell.NO_BORDER);

        //main layout - table N*8
        int numberOfColumns = 16; //useds in main layout
        // a table with <numberOfColumns> columns
        PdfPTable table = new PdfPTable(numberOfColumns);
        //setWidth to maximum
        table.setWidthPercentage(100.f);

        // the cell object
        PdfPCell cellCaption;
        // we add a cell with colspan 3
        String caption = this.competitionName + "\n" + this.dateOfEvent + "\n\n";
        cellCaption = new PdfPCell(new Phrase(caption, myBoldFont));
        cellCaption.setColspan(numberOfColumns);
        cellCaption.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cellCaption);

        //Creating main table and setting a layout
        PdfPCell cellWithTable = new PdfPCell();
        cellWithTable.setColspan(numberOfColumns);
        cellWithTable.setBorder(PdfPCell.NO_BORDER);

        int numberOfCol = this.elementsOfp.size() + 2;
        PdfPTable tableWithResults = new PdfPTable(numberOfCol);
        //tableWithResults.setWidthPercentage(100.f);
        int[] widthsArray = new int[numberOfCol];
        widthsArray[0] = 14;
        widthsArray[1] = 90;
        for (int i = 2; i < numberOfCol; i++) {
            widthsArray[i] = 45;
        }
        tableWithResults.setWidths(widthsArray);
        //creating header row
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.BOX);

        cell.setPhrase(new Phrase("№", myFont));
        tableWithResults.addCell(cell);
        cell.setPhrase(new Phrase("ФИО", myFont));
        tableWithResults.addCell(cell);
        for (data.Element elementsOfp1 : this.elementsOfp) {
            cell.setPhrase(new Phrase(elementsOfp1.getFullName(), myFont));
            tableWithResults.addCell(cell);
        }

        //cycle to fill in Results from class field resultsSimple ArrayList
        for (int i = 0; i < this.participantsOfp.size(); i++) {
            cell.setPhrase(new Phrase((i + 1) + "", myFont));
            tableWithResults.addCell(cell);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setPhrase(new Phrase(this.participantsOfp.get(i).toString(), myFont));
            tableWithResults.addCell(cell);
            int numberOfJudjes = 1;
            if (mode != 0) {
                numberOfJudjes = this.resultsOfp.get(i).size() / this.elementsOfp.size();
            } else {
                numberOfJudjes = this.numberOfJudges;
            }

            for (int j = 0; j < this.elementsOfp.size() * numberOfJudjes; j += numberOfJudjes) {
                if (!needJudjes) { //for OPF and SFP
                    if (mode == 0) {
                        cell.setPhrase(new Phrase(" "));
                    } else {
                        cell.setPhrase(new Phrase(String.format("%.2f", this.resultsOfp.get(i).get(j).getValue())));
                    }
                    tableWithResults.addCell(cell);

                } else {
                    PdfPCell innerJudjesCell = new PdfPCell();
                    PdfPTable innerJudjes = new PdfPTable(numberOfJudjes);
                    PdfPCell innnerMark = new PdfPCell();
                    innnerMark.setBorder(PdfPCell.BOX);
                    innerJudjes.setWidthPercentage(100f);
                    for (int k = j; k < numberOfJudjes + j; k++) {
                        String mark = (mode != 0) ? ( this.resultsOfp.get(i).get(k).getValue() + "") : " ";
                        if (mark.length() == 1) {
                            innnerMark.setPhrase(new Phrase(mark, myFont));
                        } else {
                            innnerMark.setPhrase(new Phrase(mark, small));
                        }
                        innerJudjes.addCell(innnerMark);
                    }

                    innerJudjesCell.addElement(innerJudjes);
                    tableWithResults.addCell(innerJudjesCell);
                }
            }
        }
        cellWithTable.addElement(tableWithResults);
        table.addCell(cellWithTable);

        //Add table to PDF
        document.add(table);
        document.close();
        writer.close();
    }
}
