package com.tcf.sma.Models.HR;

public class EmployeePendingAttendanceModel {

    private String school, date;
    private int schoolId;
    private boolean isAttendanceMarked;

    public EmployeePendingAttendanceModel() {
    }

    public EmployeePendingAttendanceModel(int schoolId , String school, String date, boolean isAttendanceMarked) {
        this.schoolId = schoolId;
        this.school = school;
        this.date = date;
        this.isAttendanceMarked = isAttendanceMarked;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public boolean isAttendanceMarked() {
        return isAttendanceMarked;
    }

    public void setAttendanceMarked(boolean attendanceMarked) {
        isAttendanceMarked = attendanceMarked;
    }
}
