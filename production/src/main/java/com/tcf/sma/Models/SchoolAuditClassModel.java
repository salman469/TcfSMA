package com.tcf.sma.Models;

/**
 * Created by Haseeb on 2/22/2017.
 */

public class SchoolAuditClassModel {
    int audit_id, class_id, count;
    boolean is_approved;
    String remarks;

    public SchoolAuditClassModel(int audit_id, int class_id, int count, boolean is_approved, String remarks) {
        this.audit_id = audit_id;
        this.class_id = class_id;
        this.count = count;
        this.is_approved = is_approved;
        this.remarks = remarks;
    }

    public SchoolAuditClassModel() {
    }

    public int getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(int audit_id) {
        this.audit_id = audit_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
}
