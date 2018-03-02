package data;

import java.util.Date;

//storage data class
public class Employee { 
    private int id;
    private String name;
    private String surname;
    private String middlename;
    private Date birthday;
    private int experience;
    private String education;
    private String post;
    
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getMiddlename() {
        return middlename;
    }
    public Date getBirthday() {
        return birthday;
    }
    public int getExperience() {
        return experience;
    }
    public String getEducation() {
        return education;
    }    
    public String getPost() {
        return post;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setEducation(String education) {
        this.education = education;
    }
    public void setPost(String post) {
        this.post = post;
    }
}


