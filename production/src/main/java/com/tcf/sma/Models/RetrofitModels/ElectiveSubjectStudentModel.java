package com.tcf.sma.Models.RetrofitModels;

public class ElectiveSubjectStudentModel {
    private int id;
    private int electiveSubjectID;
    private int studentID;
    private String createdOn;
    private String modifiedOn;

    public ElectiveSubjectStudentModel() {
    }

    public ElectiveSubjectStudentModel(int electiveSubjectID, int studentID, String createdOn, String modifiedOn) {
        this.electiveSubjectID = electiveSubjectID;
        this.studentID = studentID;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElectiveSubjectID() {
        return electiveSubjectID;
    }

    public void setElectiveSubjectID(int electiveSubjectID) {
        this.electiveSubjectID = electiveSubjectID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
