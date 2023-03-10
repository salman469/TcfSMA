package com.tcf.sma.Models;

public class StudentErrorFetchModel {
    private String studentName;
    private String classSection;
    private String message;
    private String studentGr;
    private String created_on;
    private String enrollmentGr; // for enrollment only
    private String enrollmentName;//for enrollment only

    public String getEnrollmentGr() {
        return enrollmentGr;
    }

    public void setEnrollmentGr(String enrollmentGr) {
        this.enrollmentGr = enrollmentGr;
    }

    public String getEnrollmentName() {
        return enrollmentName;
    }

    public void setEnrollmentName(String enrollmentName) {
        this.enrollmentName = enrollmentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStudentGr() {
        return studentGr;
    }

    public void setStudentGr(String studentGr) {
        this.studentGr = studentGr;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
