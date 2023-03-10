package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Badar Jamal on 2/22/2017.
 */

public class SchoolAuditModel {

    int id;
    int device_id;
    @SerializedName("audited_by")
    int approved_by;
    int school_id;
    int classes_count;
    int students_count;
    int server_id;
    @SerializedName("audited_on")
    String visit_date;
    String remarks;
    String uploaded_on;
    String device_udid;
    boolean is_approved;


    public SchoolAuditModel(int id, int approved_by, int school_id, String visit_date, String remarks, String uploaded_on, boolean is_approved) {
        this.is_approved = is_approved;
        this.remarks = remarks;
        this.id = id;
        this.approved_by = approved_by;
        this.school_id = school_id;
        this.visit_date = visit_date;
        this.uploaded_on = uploaded_on;
    }

    public SchoolAuditModel() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(int approved_by) {
        this.approved_by = approved_by;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public boolean is_approved() {
        return is_approved;
    }

    public void setIs_approved(boolean is_approved) {
        this.is_approved = is_approved;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
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

    public String getDevice_udid() {
        return device_udid;
    }

    public void setDevice_udid(String device_udid) {
        this.device_udid = device_udid;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }


}
