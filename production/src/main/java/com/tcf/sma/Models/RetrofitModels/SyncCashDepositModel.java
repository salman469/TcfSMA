package com.tcf.sma.Models.RetrofitModels;

public class SyncCashDepositModel {
    public int id;
    public int school_id;
    public int created_by;
    public String created_on;
    public String modified_on;
    public String uploaded_on;
    public String deposit_slip_no;
    public double deposit_amount;
    public String picture_slip_filename;
    public String device_id;
    public String remarks;
    public String receipt_ids; //comma separated system ids of app receipts.

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceipt_ids() {
        return receipt_ids;
    }

    public void setReceipt_ids(String receipt_ids) {
        this.receipt_ids = receipt_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
    }

    public String getDeposit_slip_no() {
        return deposit_slip_no;
    }

    public void setDeposit_slip_no(String deposit_slip_no) {
        this.deposit_slip_no = deposit_slip_no;
    }

    public double getDeposit_slip_amount() {
        return deposit_amount;
    }

    public void setDeposit_slip_amount(double deposit_slip_amount) {
        this.deposit_amount = deposit_slip_amount;
    }

    public String getPicture_slip_filename() {
        return picture_slip_filename;
    }

    public void setPicture_slip_filename(String picture_slip_filename) {
        this.picture_slip_filename = picture_slip_filename;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String print() {
        String data = "";
        data += "sysId: " + id;
        data += "  schoolId: " + school_id;
        data += "  deposit_slip_no: " + deposit_slip_no;
        return data;
    }
}
