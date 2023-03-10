package com.tcf.sma.Models.Fees_Collection;

public class AttendanceReportTableModel {
    private int school_id;
    private int school_class_id;
    private double attendance_this_month;
    private String month_year;
    private String class_section;

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getSchool_class_id() {
        return school_class_id;
    }

    public void setSchool_class_id(int school_class_id) {
        this.school_class_id = school_class_id;
    }

    public double getAttendance_this_month() {
        return attendance_this_month;
    }

    public void setAttendance_this_month(double attendance_this_month) {
        this.attendance_this_month = attendance_this_month;
    }

    public String getMonth_year() {
        return month_year;
    }

    public void setMonth_year(String month_year) {
        this.month_year = month_year;
    }

    public String getClass_section() {
        return class_section;
    }

    public void setClass_section(String class_section) {
        this.class_section = class_section;
    }
}
