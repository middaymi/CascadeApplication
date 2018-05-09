/*VALUES OF THE 'CURRENT PANEL':
0  - START PAGE
10 - ORGANIZATION PAGE
20 - EMPLOYEE PAGE
30 - ATHLETE PAGE
40 - PERFORMANCE PAGE
     41 - PERFORMANCE_EDIT PAGE
50 - COMPETITION AND TESTING PAGE
*/

package views;

import views.TestCom.StartCom.SingleStComPage;
import views.TestCom.TestComPage;
import views.Performance.PerformancePage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import views.Performance.*;
import views.TestCom.StartCom.StComPage;
import views.TestCom.*;

public class Manager {
    
    private static Manager managerInstance = null;
    
    private static MainFrame mFrame = null;
    private static CommonButtonsPanel comBtnPanel = null;
    private static StartPage stPage = null;
    private static OrganizationPage orgPage = null;
    private static EmployeePage empPage = null;
    private static AthletePage athPage = null;
    private static PerformancePage perPage = null;
    private static PerformanceEditPage perEditPage = null;
    private static TestComPage testCompPage = null;
    private static OfpEditPage ofpEditPage = null;
    private static SfpEditPage sfpEditPage = null;
    private static SingleEditPage singleEditPage = null;
    private static GlasialEditPage glasialEditPage = null;  
    private static StComPage stComPage = null;
    private static SingleStComPage singleComPage = null;
    private static ProtocolPage protocolPage = null;
    
    
    private static int width;
    private static int height;
    private static int insetsTopBottom;
    private static int insetsLeftRight;
    
    private static int chosenPage = -1;
           
    private Manager() { 
        width = getResolution().width;
        height = getResolution().height;
        mFrame = new MainFrame();
        insetsTopBottom = mFrame.getInsetBottom();
        insetsLeftRight = mFrame.getInsetLeftRight();
        createCommonBtnPanel();
        createStartPage();         
    } 
    
    public static Manager getManagerInstance() {
        if (managerInstance == null) {
            managerInstance = new Manager();
        }
        return managerInstance;
    }
      
    //GET_SCREEN_RESOLUTION*****************************************************
    private Dimension getResolution() {
      return Toolkit.getDefaultToolkit().getScreenSize();   
    }
    public static int getWidth() {
        return width;
    }
    public static int getHeight() {
        return height;
    }
    public static int getWidthWithInsets() {
        return (width - insetsLeftRight);
    }
    public static int getHeightWithInsets() {        
        return (height - insetsTopBottom);
    }
    
    //COMMON_BTN_PANEL**********************************************************
    private void createCommonBtnPanel() {
        comBtnPanel = new CommonButtonsPanel();
        getComBtnPanel().setVisible(true);
        getmFrame().getContentPane().add(getComBtnPanel(), BorderLayout.CENTER);        
    }
    
    //START_PAGE****************************************************************
    private void createStartPage(){
        stPage = new StartPage();
        getComBtnPanel().useBtns(0);
        getmFrame().getContentPane().add(getStPage(), BorderLayout.CENTER);       
    }
    
    //ORGANIZATION_PAGE*********************************************************
     private static void createOrganizationPage(){
        orgPage = new OrganizationPage();
        getmFrame().getContentPane().add(getOrgPage(), BorderLayout.CENTER);
    }
     
    //EMPLOYEE_PAGE*************************************************************
    private static void createEmployeePage() {
        empPage = new EmployeePage();
        getmFrame().getContentPane().add(getEmpPage(), BorderLayout.CENTER);
    }
    
    //ATHLETE_PAGE**************************************************************
    private static void createAthletePage() {
        athPage = new AthletePage();
        getmFrame().getContentPane().add(getAthPage(), BorderLayout.CENTER);
    }
    
    //PERFORMANCE_PAGE**********************************************************
    private static void createPerformancePage() {
        perPage = new PerformancePage();
        getmFrame().getContentPane().add(getPerPage(), BorderLayout.CENTER);
    }
    private static void createPerEditPage() {
        perEditPage = new PerformanceEditPage();
        getmFrame().getContentPane().add(getPerEditPage(), BorderLayout.CENTER);
    }

    //TESTING_AND_COMPETITION_PAGE**********************************************
    private static void createTestingCompetitionPage() {
        testCompPage = new TestComPage();
        getmFrame().getContentPane().add(testCompPage, BorderLayout.CENTER);
    } 
    private static void createGlasialEditPage() {
        glasialEditPage = new GlasialEditPage();
        getmFrame().getContentPane().add(glasialEditPage, BorderLayout.CENTER);
    }
    private static void createOfpEditPage() {
        ofpEditPage = new OfpEditPage();
        getmFrame().getContentPane().add(ofpEditPage, BorderLayout.CENTER);
    }
    private static void createSfpEditPage() {
        sfpEditPage = new SfpEditPage();
        getmFrame().getContentPane().add(sfpEditPage, BorderLayout.CENTER);
    }
    private static void createSingleEditPage() {
        singleEditPage = new SingleEditPage();
        getmFrame().getContentPane().add(singleEditPage, BorderLayout.CENTER);
    }
    
    //START_AND_PROTOCOL_PANELS*************************************************
    private static void createStComPage() {
        stComPage = new StComPage();
        getmFrame().getContentPane().add(stComPage, BorderLayout.CENTER);
    }    
    private static void createSingleComPage() {
        singleComPage = new SingleStComPage();
        getmFrame().getContentPane().add(singleComPage, BorderLayout.CENTER);
    }    
    private static void createProtocolPage() {
        protocolPage = new ProtocolPage();
        getmFrame().getContentPane().add(protocolPage, BorderLayout.CENTER);
    }
       
    //NOT_VISIBLE_FOR_ALL_CREATED_PANELS****************************************
    private static void hideAllPanels() {
        if (getStPage() != null) getStPage().setVisible(false);
        if (getOrgPage() != null) getOrgPage().setVisible(false);
        if (getEmpPage() != null) getEmpPage().setVisible(false);
        if (getAthPage() != null) getAthPage().setVisible(false);
        if (getPerPage() != null) getPerPage().setVisible(false);
        if (getPerEditPage() != null) getPerEditPage().setVisible(false);
        if (getTestCompPage() != null) getTestCompPage().setVisible(false);
        if (glasialEditPage != null) glasialEditPage.setVisible(false);
        if (ofpEditPage != null) ofpEditPage.setVisible(false);
        if (sfpEditPage != null) sfpEditPage.setVisible(false);
        if (singleEditPage != null) singleEditPage.setVisible(false);
        if (stComPage != null) stComPage.setVisible(false);
        if (singleComPage != null) singleComPage.setVisible(false);
        if (protocolPage != null) protocolPage.setVisible(false);
    }
    
    //CHOOSE_PANELS*************************************************************
    public static void choosePanel(int currentNumber) {
        hideAllPanels();
        switch(currentNumber) {
            case(0):
                getStPage().setVisible(true);
                getComBtnPanel().useBtns(0);
                chosenPage = 0;
                break;                 
            case(10):
                if (getOrgPage() == null) createOrganizationPage();
                getOrgPage().setVisible(true);
                getComBtnPanel().useBtns(10);
                chosenPage = 10;
                break;
            case(20):
                if (getEmpPage() == null) createEmployeePage();
                getEmpPage().setVisible(true); 
                getComBtnPanel().useBtns(20);
                chosenPage = 20;
                break;
            case(30):
                if (getAthPage() == null) createAthletePage();
                getAthPage().setVisible(true);
                getComBtnPanel().useBtns(20);
                chosenPage = 30;
                break;
            case(40):
                if (getPerPage() == null) createPerformancePage();
                getPerPage().setVisible(true);
                getComBtnPanel().useBtns(20);
                chosenPage = 40;
                break;
            case(41):                
                if (getPerEditPage() == null) createPerEditPage();
                getPerEditPage().setVisible(true);                
                getComBtnPanel().useBtns(30);
                chosenPage = 41;
                break;
            case(50):
                if (getTestCompPage() == null) createTestingCompetitionPage();
                getTestCompPage().setVisible(true);
                getComBtnPanel().useBtns(20);
                chosenPage = 50;
                break;
            case(51):
                if (glasialEditPage == null) createGlasialEditPage();
                glasialEditPage.setVisible(true);                
                getComBtnPanel().useBtns(30);
                chosenPage = 51;
                break;
            case(52):
                if (ofpEditPage == null) createOfpEditPage();
                ofpEditPage.setVisible(true);
                getComBtnPanel().useBtns(30);
                chosenPage = 52;
                break;
            case(53):
                if (sfpEditPage == null) createSfpEditPage();
                sfpEditPage.setVisible(true);
                getComBtnPanel().useBtns(30);
                chosenPage = 53;
                break;
            case(54):
                if (singleEditPage == null) createSingleEditPage();
                singleEditPage.setVisible(true);
                getComBtnPanel().useBtns(30);
                chosenPage = 54;
                break;
            case(55):                
                if (protocolPage == null) createProtocolPage();
                protocolPage.setVisible(true);
                getComBtnPanel().useBtns(30);
                chosenPage = 55;
                break;
            case(61):
                if (stComPage == null) createStComPage();
                stComPage.setVisible(true);
                getComBtnPanel().useBtns(30);
                chosenPage = 61;
                break;
            case(62):
                if (singleComPage == null) createSingleComPage();
                singleComPage.setVisible(true);
                getComBtnPanel().useBtns(30);
                chosenPage = 62;
                break;
        }        
    } 
    public int chosenPage() {
        return chosenPage;
    }

    //get panels
    public static MainFrame getmFrame() {
        return mFrame;
    }
    public static StartPage getStPage() {
        return stPage;
    }
    public static CommonButtonsPanel getComBtnPanel() {
        return comBtnPanel;
    }
    public static OrganizationPage getOrgPage() {
        return orgPage;
    }
    public static EmployeePage getEmpPage() {
        return empPage;
    }
    public static AthletePage getAthPage() {
        return athPage;
    }
    public static PerformancePage getPerPage() {
        return perPage;
    }
    public static PerformanceEditPage getPerEditPage() {
        return perEditPage;
    }            
    public static TestComPage getTestCompPage() {
        return testCompPage;
    }  
    public static SfpEditPage getSfpEditPage() {
            return sfpEditPage;
    }
    public static OfpEditPage getOfpEditPage() {
        return ofpEditPage;
    }
    public static GlasialEditPage getGlasialEdtiPage() {
        return glasialEditPage;
    }
    public static SingleEditPage getSingleEditPage() {
        return singleEditPage;
    }
    public static StComPage getStComPage() {
        return stComPage;
    }
    public static SingleStComPage getSingleComPage() {
        return singleComPage;
    }
    public static ProtocolPage getProtocolPage() {
        return protocolPage;
    }
}
