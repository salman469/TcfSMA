package com.tcf.sma.Models;

import java.util.ArrayList;

/**
 * Created by Haseeb on 2/23/2017.
 */

public class StudentAttendanceReportModel {
    private int id;
    private int createdBy;
    private int schoolId;
    private String forDate;
    private String createdOn;
    private String uploadedOn;
    private int attendanceId;
    private int studentId;
    private boolean isAbsent;
    private String reason;

    public StudentAttendanceReportModel(int id, int createdBy, int schoolId, String forDate, String createdOn, String uploadedOn, int attendanceId, int studentId, boolean isAbsent, String reason) {
        this.id = id;
        this.createdBy = createdBy;
        this.schoolId = schoolId;
        this.forDate = forDate;
        this.createdOn = createdOn;
        this.uploadedOn = uploadedOn;
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.isAbsent = isAbsent;
        this.reason = reason;
        ArrayList<StudentAttendanceReportModel> sarmList;
    }

    public StudentAttendanceReportModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
