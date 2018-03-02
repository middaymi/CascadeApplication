package data;

//storage data class
public class Performance { 
    private int id;    
    private Season season;
    private String fullName;
    private String phonogram;
    private String costumeDesign;
    private String costumePhoto;   
    private String description;    

    public int getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getPhonogram() {
        return phonogram;
    }
    public String getCostumeDesign() {
        return costumeDesign;
    }
    public String getCostumePhoto() {
        return costumePhoto;
    }
    public String getDescription() {
        return description;
    }
     public Season getSeason() {
        return season;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setPhonogram(String phonogram) {
        this.phonogram = phonogram;
    }
    public void setCostumeDesign(String costumeDesign) {
        this.costumeDesign = costumeDesign;
    }
    public void setCostumePhoto(String costumePhoto) {
        this.costumePhoto = costumePhoto;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSeason(int season) {        
        this.season.setId(season);
    }
    public void setSeason(String season) {
        this.season.setPeriod(season);
    }
    public void setSeason(Season season) {
        this.season = season;
    }
}


