package com.tcf.sma.Models;

public class SchoolAttendanceReportModel {

    private double Attendance;
    private String class_section_name;
    private String month, year;
    private String session;
    private int schoolid;
    private int academicSessionId;

    public int getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(int schoolid) {
        this.schoolid = schoolid;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public double getAttendance() {
        return Attendance;
    }

    public void setAttendance(double attendance) {
        Attendance = attendance;
    }

    public String getClass_section_name() {
        return class_section_name;
    }

    public void setClass_section_name(String class_section_name) {
        this.class_section_name = class_section_name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
