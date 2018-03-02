package data;

public class Organization {
    private String fullName = "";  
    private String phoneNumber = "";  
    private String site = "";  
    private String eMail = "";  
    private String OGRN = "";  
    private String KPP = "";  
    private String INN = ""; 
    private String account = ""; 
    private String actualAddress = "";
    private String legalAddress = "";

    public String getFullName() {
        return fullName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getSite() {
        return site;
    }
    public String geteMail() {
        return eMail;
    }
    public String getOGRN() {
        return OGRN;
    }
    public String getKPP() {
        return KPP;
    }
    public String getINN() {
        return INN;
    }
    public String getAccount() {
        return account;
    }
    public String getActualAddress() {
        return actualAddress;
    }
    public String getLegalAddress() {
        return legalAddress;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public void setOGRN(String OGRN) {
        this.OGRN = OGRN;
    }
    public void setKPP(String KPP) {
        this.KPP = KPP;
    }
    public void setINN(String INN) {
        this.INN = INN;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public void setActualAddress(String actualAddress) {
        this.actualAddress = actualAddress;
    }
    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }
}
