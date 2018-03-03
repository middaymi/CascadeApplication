package views;

import javax.swing.JButton;
import javax.swing.JPanel;

public class StartPage extends JPanel{
        
    private JButton organizationBtn;
    private JButton employeeBtn;
    private JButton athleteBtn;
    private JButton performanceBtn;
    private JButton testCompetitionBtn;
    //private JButton testingBtn;
    
    //private Color myGray = new Color(80, 80, 80, 30);
    
    public StartPage() {  
        CommonSettings.panelSettings(this);
        createButtons();
    }     
      
    //create all buttons
    private void  createButtons() { 
        //create buttons
        organizationBtn = new JButton("Организация");
        employeeBtn = new JButton("Сотрудники");
        athleteBtn = new JButton("Спортсмены");
        performanceBtn = new JButton("Постановки");
        testCompetitionBtn = new JButton("<html><p align=center>Зачеты и "
                                             + "<p align=center>Соревнования"
                                       + "</html>");
                                         
        //buttons settings
        setButtonsSizeLocationAdd(1, 1, organizationBtn);
        setButtonsSizeLocationAdd(1, 2, employeeBtn);
        setButtonsSizeLocationAdd(1, 3, athleteBtn);
        setButtonsSizeLocationAdd(2, 1, performanceBtn);
        setButtonsSizeLocationAdd(2, 2, testCompetitionBtn);     
               
        //add listeners
        organizationBtn.addActionListener(new controllers.StartPage.
                                              OrganizationBtnListener());        
        employeeBtn.addActionListener(new controllers.StartPage.
                                          EmployeeBtnListener());
        athleteBtn.addActionListener(new controllers.StartPage.
                                         AthleteBtnListener());
        performanceBtn.addActionListener(new controllers.StartPage.
                                             PerformanceBtnListener());
        testCompetitionBtn.addActionListener(new controllers.StartPage.
                                                 TestCompetitionBtnListener());             
    } 
    
    //size, location, font
    private void setButtonsSizeLocationAdd(int x, int y, JButton btn) {
        //location: 2 rows(x), 3 columns(y)
        btn.setSize(600, 360);
        CommonSettings.settingFont50(btn);
        CommonSettings.settingLightGrayBorder(btn);
        //btn's painting
        //btn.setBorderPainted(true);
        //btn.setBackground(Color.lightGray);
        //btn.setBackground(myGray);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        
        //pressed and rollover btn's icon
        //btn.setPressedIcon();
        //btn.setRolloverIcon(setSizesBottomPanel(imgPr, iconPr)); 
        
        if (x == 1) {
            switch(y) {
                case 1: btn.setLocation(584, 420);
                        this.add(organizationBtn);
                        break;
                case 2: btn.setLocation(1284, 420);
                        this.add(employeeBtn);
                        break;
                case 3: btn.setLocation(1984, 420);                     
                        this.add(athleteBtn);
                        break;
            }
        }
        if (x == 2) {
            switch(y) {
                case 1: btn.setLocation(934, 880);
                        this.add(performanceBtn);
                        break;
                case 2: btn.setLocation(1634, 880);
                        this.add(testCompetitionBtn);
                        break;
            }
        }
    }
}    
