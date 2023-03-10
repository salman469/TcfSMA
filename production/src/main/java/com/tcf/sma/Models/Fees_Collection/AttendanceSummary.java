package com.tcf.sma.Models.Fees_Collection;

import com.google.gson.annotations.SerializedName;

public class AttendanceSummary {
    @SerializedName("SchoolId")
    private int schoolId;
    @SerializedName("SchoolClass_id")
    private int school_class_id;
    @SerializedName("AcademicSession_Id")
    private int academic_session_id;
    @SerializedName("fordate")
    private String forDate;
    @SerializedName("AttendancePercentage")
    private double attendancePercentage;
    @SerializedName("PresentStudents")
    private int presentStudents;
    @SerializedName("TotalStudents")
    private int totalStudents;

    private int classes_count;
    private double totalAttendancePercentage;


    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSchool_class_id() {
        return school_class_id;
    }

    public void setSchool_class_id(int school_class_id) {
        this.school_class_id = school_class_id;
    }

    public int getAcademic_session_id() {
        return academic_session_id;
    }

    public void setAcademic_session_id(int academic_session_id) {
        this.academic_session_id = academic_session_id;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getPresentStudents() {
        return presentStudents;
    }

    public void setPresentStudents(int presentStudents) {
        this.presentStudents = presentStudents;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getClasses_count() {
        return classes_count;
    }

    public void setClasses_count(int classes_count) {
        this.classes_count = classes_count;
    }

    public double getTotalAttendancePercentage() {
        return totalAttendancePercentage;
    }

    public void setTotalAttendancePercentage(double totalAttendancePercentage) {
        this.totalAttendancePercentage = totalAttendancePercentage;
    }
}
