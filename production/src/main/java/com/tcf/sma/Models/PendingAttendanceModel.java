package com.tcf.sma.Models;

public class PendingAttendanceModel {

    private String className, section, school, date, totalClassSecCount;
    private int schoolId, classId, schoolclass_id;
    private boolean isAttendaneMarked;

    public PendingAttendanceModel() {
    }

    public PendingAttendanceModel(int schoolId, String className, String section, String school, String date, int schoolclass_id, boolean isAttendaneMarked) {
        this.schoolId = schoolId;
        this.className = className;
        this.section = section;
        this.school = school;
        this.date = date;
        this.schoolclass_id = schoolclass_id;
        this.isAttendaneMarked = isAttendaneMarked;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public String getTotalClassSecCount() {
        return totalClassSecCount;
    }

    public void setTotalClassSecCount(String totalClassSecCount) {
        this.totalClassSecCount = totalClassSecCount;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getSchoolclass_id() {
        return schoolclass_id;
    }

    public void setSchoolclass_id(int schoolclass_id) {
        this.schoolclass_id = schoolclass_id;
    }

    public boolean isAttendaneMarked() {
        return isAttendaneMarked;
    }

    public void setAttendaneMarked(boolean attendaneMarked) {
        isAttendaneMarked = attendaneMarked;
    }
}
