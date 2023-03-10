package com.tcf.sma.Models;

/**
 * Created by Badar Arain on 3/16/2017.
 */

public class HighestDuesStudentsModel {


    String schoolName, studentGr_NO;
    String studentsID, amount;
    String studntsName, className, sectionName;
    private int classRank;

    public HighestDuesStudentsModel() {
    }

    public HighestDuesStudentsModel(String schoolName, String studentGr_NO, String studentsID, String amount, String studntsName, String className, String sectionName) {
        this.schoolName = schoolName;
        this.studentGr_NO = studentGr_NO;
        this.studentsID = studentsID;
        this.amount = amount;
        this.studntsName = studntsName;
        this.className = className;
        this.sectionName = sectionName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStudentGr_NO() {
        return studentGr_NO;
    }

    public void setStudentGr_NO(String studentGr_NO) {
        this.studentGr_NO = studentGr_NO;
    }

    public String getStudentsID() {
        return studentsID;
    }

    public void setStudentsID(String studentsID) {
        this.studentsID = studentsID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStudntsName() {
        return studntsName;
    }

    public void setStudntsName(String studntsName) {
        this.studntsName = studntsName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getClassRank() {
        return classRank;
    }

    public void setClassRank(int classRank) {
        this.classRank = classRank;
    }
}
