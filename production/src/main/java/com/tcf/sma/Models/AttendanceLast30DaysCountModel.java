package com.tcf.sma.Models;

public class AttendanceLast30DaysCountModel {

    private String date, forDate;
    private int count, schoolclass_id;

    public AttendanceLast30DaysCountModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSchoolclass_id() {
        return schoolclass_id;
    }

    public void setSchoolclass_id(int schoolclass_id) {
        this.schoolclass_id = schoolclass_id;
    }
}
