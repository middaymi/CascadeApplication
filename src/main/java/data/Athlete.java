package data;

import java.util.Date;
import java.util.GregorianCalendar;

//storage data class
public class Athlete { 
    private int id;
    private String surname;
    private String name;
    private String middlename;
    private Date birthday;
    private int idrank;
    private String assigneeFullName;
    private String phoneNumber;
    private String actualAddress;
    private String mainDocumentCopy;
    private String  medicalCertificate;
    private String insurance;
    private boolean sex;

    public int getId() {
        return id;
    }
    public String getSurname() {
        return surname;
    }
    public String getName() {
        return name;
    }
    public String getMiddlename() {
        return middlename;
    }
    public Date getBirthday() {
        return birthday;
    }
    public int getIdrank() {
        return idrank;
    }
    public String getAssigneeFullName() {
        return assigneeFullName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getActualAddress() {
        return actualAddress;
    }
    public String getMainDocumentCopy() {
        return mainDocumentCopy;
    }
    public String getMedicalCertificate() {
        return medicalCertificate;
    }
    public String getInsurance() {
        return insurance;
    }
    public boolean getSex() {
        return sex;
    }

    public int getAge() {        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this.birthday);
        return this.calculateYears(calendar);
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }
    public void setIdrank(int idrank) {
        this.idrank = idrank;
    }
    public void setAssigneeFullName(String assigneeFullName) {
        this.assigneeFullName = assigneeFullName;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setActualAddress(String actualAddress) {
        this.actualAddress = actualAddress;
    }
    public void setMainDocumentCopy(String mainDocumentCopy) {
        this.mainDocumentCopy = mainDocumentCopy;
    }
    public void setMedicalCertificate(String medicalCertificate) {
        this.medicalCertificate = medicalCertificate;
    }
    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }
    public void setSex(boolean sex) {
        this.sex = sex;
    }    
    
    @Override
    public String toString() {
        return getSurname() + " " + getName() + " " + getMiddlename();
    }
    
    private int calculateYears(GregorianCalendar birthDay) {        
        GregorianCalendar checkDay = new GregorianCalendar();
        int years = checkDay.get(GregorianCalendar.YEAR) - birthDay.get(GregorianCalendar.YEAR);        
        //if current month lower month of birthday 
        int checkMonth = checkDay.get(GregorianCalendar.MONTH);
        int birthMonth = birthDay.get(GregorianCalendar.MONTH);
        if (checkMonth < birthMonth) {
            years --;
        } else if (checkMonth == birthMonth 
            && checkDay.get(GregorianCalendar.DAY_OF_MONTH) 
            < birthDay.get(GregorianCalendar.DAY_OF_MONTH)) {
            //if months are equals, but day is lower
            years --;
        }
        return years;
    }
}


