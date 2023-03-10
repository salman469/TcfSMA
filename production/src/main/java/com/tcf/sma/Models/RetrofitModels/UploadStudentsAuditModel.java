package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 3/30/2017.
 */

public class UploadStudentsAuditModel {
    int id;
    int school_id;
    int device_id;
    int audited_by;
    String audited_on;
    String device_udid;
    int classes_count;
    int students_count;
    int server_id;
    String remarks;
    boolean is_approved;

    public UploadStudentsAuditModel() {
    }

    public UploadStudentsAuditModel(int id, int school_id, int device_id, int audited_by, String audited_on, String device_udid, int classes_count, int students_count) {
        this.id = id;
        this.school_id = school_id;
        this.device_id = device_id;
        this.audited_by = audited_by;
        this.audited_on = audited_on;
        this.device_udid = device_udid;
        this.classes_count = classes_count;
        this.students_count = students_count;
    }

    //Getters

    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getAudited_by() {
        return audited_by;
    }

    public void setAudited_by(int audited_by) {
        this.audited_by = audited_by;
    }

    public String getAudited_on() {
        return audited_on;
    }

    public void setAudited_on(String audited_on) {
        this.audited_on = audited_on;
    }

    public String getDevice_udid() {
        return device_udid;
    }

    public void setDevice_udid(String device_udid) {
        this.device_udid = device_udid;
    }

    public int getClasses_count() {
        return classes_count;
    }

    public void setClasses_count(int classes_count) {
        this.classes_count = classes_count;
    }

    public int getStudents_count() {
        return students_count;
    }

    public void setStudents_count(int students_count) {
        this.students_count = students_count;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean is_approved() {
        return is_approved;
    }

    public void setIs_approved(boolean is_approved) {
        this.is_approved = is_approved;
    }
}
