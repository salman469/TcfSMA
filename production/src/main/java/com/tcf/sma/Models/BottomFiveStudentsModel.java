package com.tcf.sma.Models;

/**
 * Created by Badar Arain on 3/16/2017.
 */

public class BottomFiveStudentsModel {


    int studentsAbsentCounting;
    int studentsID;
    int totalAbsents;
    int schoolId;
    int studentGr_NO;
    int schoolClassId;
    String studntsName, className, sectionName, schoolName;
    boolean isAbsent;
    private String contactNumber;

    public BottomFiveStudentsModel() {
    }

    public int getTotalAbsents() {
        return totalAbsents;
    }

    public void setTotalAbsents(int totalAbsents) {
        this.totalAbsents = totalAbsents;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getStudentsAbsentCounting() {
        return studentsAbsentCounting;
    }

    public void setStudentsAbsentCounting(int studentsAbsentCounting) {
        this.studentsAbsentCounting = studentsAbsentCounting;
    }

    public int getStudentsID() {
        return studentsID;
    }

    public void setStudentsID(int studentsID) {
        this.studentsID = studentsID;
    }

    public String getStudntsName() {
        return studntsName;
    }

    public void setStudntsName(String studntsName) {
        this.studntsName = studntsName;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
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

    public int getStudentGr_NO() {
        return studentGr_NO;
    }

    public void setStudentGr_NO(int studentGr_NO) {
        this.studentGr_NO = studentGr_NO;
    }

    public int getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
