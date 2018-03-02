 package views;

import data.Organization;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import models.OrganizationModel;

public class OrganizationPage extends JPanel {
    
    private JButton changeBtn; 
    
    private JLabel fullNameLabel;
    private JLabel phoneNumberLabel;
    private JLabel siteLabel;
    private JLabel eMailLabel;
    private JLabel ogrnLabel;
    private JLabel kppLabel;
    private JLabel innLabel;
    private JLabel accountLabel;
    private JLabel legalAddressLabel;
    private JLabel actualAddressLabel;
    
    private JTextField fullNameField;
    private JTextField phoneNumberField;
    private JTextField siteField;
    private JTextField eMailField;
    private JTextField ogrnField;
    private JTextField kppField;
    private JTextField innField;
    private JTextField accountField;
    private JTextField legalAddressField;
    private JTextField actualAddressField; 
    
    private Organization orgData;
    private OrganizationModel orgModel;
        
    public OrganizationPage() {         
        orgModel = models.OrganizationModel.getOrganizationModelInstance();
        CommonSettings.panelSettings(this);
        createChangeButton();
        createLabels();
        orgData = orgModel.getOrganizationData();        
        createTextFields();
        settingsTextFields();
        setEditableTextFiels(false);
    } 
           
    //CHANGE_BUTTON*************************************************************
    //a button for a possible to change organization panel
    private void createChangeButton() {
        changeBtn = new JButton("Изменить");
        changeBtn.setBackground(Color.LIGHT_GRAY);
        changeBtn.setSize(250, 100);
        changeBtn.setLocation(1984, 1430);
        CommonSettings.settingFont30(changeBtn);
        this.add(changeBtn);
        changeBtn.addActionListener(new controllers.OrganizationPage.
                                        OrgChangeBtnListener());
    }
    public void changeTextButton(boolean mode) {
        if (mode == true) {
            changeBtn.setText("Сохранить");
        } else 
            changeBtn.setText("Изменить");
    }
        
    //LABELS********************************************************************
    //set and apply labels
    private void createLabels() {         
        fullNameLabel = new JLabel("Название: ");
        phoneNumberLabel = new JLabel("Контактный номер: ");
        siteLabel = new JLabel("Веб-сайт: ");
        eMailLabel = new JLabel("e-mail: ");
        ogrnLabel = new JLabel("ОГРН: ");
        kppLabel = new JLabel("КПП: ");
        innLabel = new JLabel("ИНН: ");
        accountLabel = new JLabel("Банковский счет: ");
        legalAddressLabel = new JLabel("Юридический адрес: ");
        actualAddressLabel = new JLabel("Фактический адрес: ");         
        setSizeLocationFontBorderLabel(fullNameLabel, 0);
        setSizeLocationFontBorderLabel(phoneNumberLabel, 1);
        setSizeLocationFontBorderLabel(siteLabel, 2);
        setSizeLocationFontBorderLabel(eMailLabel, 3);
        setSizeLocationFontBorderLabel(ogrnLabel, 4);
        setSizeLocationFontBorderLabel(kppLabel, 5);
        setSizeLocationFontBorderLabel(innLabel, 6);
        setSizeLocationFontBorderLabel(accountLabel, 7);
        setSizeLocationFontBorderLabel(legalAddressLabel, 8);
        setSizeLocationFontBorderLabel(actualAddressLabel, 9);
    }
    //set Labels size, location, font, border
    private void setSizeLocationFontBorderLabel(JLabel lbl, int x) {
        CommonSettings.settingFontBold30(lbl);
        lbl.setSize(320, 100);
        lbl.setLocation(934, 230 + 120 * x);
        CommonSettings.settingGrayBorder(lbl);
        this.add(lbl);
    }
    
    //TEXTFIELDS****************************************************************
    //apply changes
    private void settingsTextFields() {
        setSizeLocationFontBorder(fullNameField, 0);
        setSizeLocationFontBorder(phoneNumberField, 1);
        setSizeLocationFontBorder(siteField, 2);
        setSizeLocationFontBorder(eMailField, 3);
        setSizeLocationFontBorder(ogrnField, 4);
        setSizeLocationFontBorder(kppField, 5);
        setSizeLocationFontBorder(innField, 6);
        setSizeLocationFontBorder(accountField, 7);
        setSizeLocationFontBorder(legalAddressField, 8);
        setSizeLocationFontBorder(actualAddressField, 9);        
    } 
    //set TextFields size, location, font, border
    private void setSizeLocationFontBorder(JTextField txtField, int x){
        CommonSettings.settingFont30(txtField);
        txtField.setSize(960, 100);
        txtField.setLocation(1274, 230 + 120 * x);
        CommonSettings.settingGrayBorder(txtField);
        this.add(txtField);        
    } 
    //create textFields and set a text
    private void createTextFields() {
        fullNameField = new JTextField(orgData.getFullName());
        phoneNumberField = new JTextField(orgData.getPhoneNumber());
        siteField = new JTextField(orgData.getSite());
        eMailField  = new JTextField(orgData.geteMail());
        ogrnField = new JTextField(orgData.getOGRN());
        kppField = new JTextField(orgData.getKPP());
        innField = new JTextField(orgData.getINN());
        accountField = new JTextField(orgData.getAccount());
        legalAddressField = new JTextField(orgData.getLegalAddress());
        actualAddressField = new JTextField(orgData.getActualAddress());         
    }
    //posible to change or not    
    public void setEditableTextFiels(boolean b) {
        fullNameField.setEditable(b);
        phoneNumberField.setEditable(b);
        siteField.setEditable(b);
        eMailField.setEditable(b);
        ogrnField.setEditable(b);
        kppField.setEditable(b);
        innField.setEditable(b);
        accountField.setEditable(b);
        legalAddressField.setEditable(b);
        actualAddressField.setEditable(b);        
    } 
    //write in data-organization object 
    public void setFields() {
        orgData.setFullName(fullNameField.getText());
        orgData.setPhoneNumber(phoneNumberField.getText());
        orgData.setSite(siteField.getText());
        orgData.seteMail(eMailField.getText());
        orgData.setOGRN(ogrnField.getText());
        orgData.setKPP(kppField.getText());
        orgData.setINN(innField.getText());
        orgData.setAccount(accountField.getText());
        orgData.setLegalAddress(legalAddressField.getText());
        orgData.setActualAddress(actualAddressField.getText());      
    }

    public JTextField getFullNameField() {
        return fullNameField;
    }
    public JTextField getPhoneNumberField() {
        return phoneNumberField;
    }
    public JTextField getSiteField() {
        return siteField;
    }
    public JTextField getEMailField() {
        return eMailField;
    }
    public JTextField getOgrnField() {
        return ogrnField;
    }
    public JTextField getKppField() {
        return kppField;
    }
    public JTextField getInnField() {
        return innField;
    }
    public JTextField getAccountField() {
        return accountField;
    }
    public JTextField getLegalAddressField() {
        return legalAddressField;
    }
    public JTextField getActualAddressField() {
        return actualAddressField;
    }    
}
