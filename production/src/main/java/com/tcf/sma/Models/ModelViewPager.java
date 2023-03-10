package com.tcf.sma.Models;

/**
 * Created by Zubair Soomro on 1/11/2017.
 */

public class ModelViewPager {

    String studentName, studentGrNo, studentDOB, StudentAdmission, studentFatherName, studentMotherName;

    public ModelViewPager(String studentName, String studentGrNo, String studentDOB, String studentAdmission, String studentFatherName, String studentMotherName) {
        this.studentName = studentName;
        this.studentGrNo = studentGrNo;
        this.studentDOB = studentDOB;
        StudentAdmission = studentAdmission;
        this.studentFatherName = studentFatherName;
        this.studentMotherName = studentMotherName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentGrNo() {
        return studentGrNo;
    }

    public void setStudentGrNo(String studentGrNo) {
        this.studentGrNo = studentGrNo;
    }

    public String getStudentDOB() {
        return studentDOB;
    }

    public void setStudentDOB(String studentDOB) {
        this.studentDOB = studentDOB;
    }

    public String getStudentAdmission() {
        return StudentAdmission;
    }

    public void setStudentAdmission(String studentAdmission) {
        StudentAdmission = studentAdmission;
    }

    public String getStudentFatherName() {
        return studentFatherName;
    }

    public void setStudentFatherName(String studentFatherName) {
        this.studentFatherName = studentFatherName;
    }

    public String getStudentMotherName() {
        return studentMotherName;
    }

    public void setStudentMotherName(String studentMotherName) {
        this.studentMotherName = studentMotherName;
    }

}
