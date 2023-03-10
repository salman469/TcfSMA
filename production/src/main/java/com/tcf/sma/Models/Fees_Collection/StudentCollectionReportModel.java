package com.tcf.sma.Models.Fees_Collection;

public class StudentCollectionReportModel {
    private double dues;
    private double collected;
    private double balance;
    private int classId;
    private int schoolId;
    private double remaining;
    private int secId;
    private int grNo;
    private String className;
    private String sectionName;
    private String classSectionName;
    private String studentName;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    public double getDues() {
        return dues;
    }

    public void setDues(double dues) {
        this.dues = dues;
    }

    public double getCollected() {
        return collected;
    }

    public void setCollected(double collected) {
        this.collected = collected;
    }

    public double getBalance() {
        return this.dues - this.collected;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getGrNo() {
        return grNo;
    }

    public void setGrNo(int grNo) {
        this.grNo = grNo;
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

    public String getClassSectionName() {
        return this.className + " " + this.sectionName;
    }

    public void setClassSectionName(String classSectionName) {
        this.classSectionName = classSectionName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
