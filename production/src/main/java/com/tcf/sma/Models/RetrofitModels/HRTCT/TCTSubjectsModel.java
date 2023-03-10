package com.tcf.sma.Models.RetrofitModels.HRTCT;

import com.google.gson.annotations.SerializedName;

public class TCTSubjectsModel {
    @SerializedName("ID")
    private int id;

    private String Subject;
    private String SchoolType;
    private String SubjectType;
    private String Designation;
    private String modified_on;
    private int modified_by;

    public TCTSubjectsModel(int id, String Subject) {
        this.id = id;
        this.Subject = Subject;
    }

    public TCTSubjectsModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSchoolType() {
        return SchoolType;
    }

    public void setSchoolType(String schoolType) {
        SchoolType = schoolType;
    }

    public String getSubjectType() {
        return SubjectType;
    }

    public void setSubjectType(String subjectType) {
        SubjectType = subjectType;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    //to display object as a string in spinner
    public String toString() {
//        return getSubject();
        return getId() > 0 ? getSubject() + " (" + getSchoolType() + ")" : getSubject();
    }
}
