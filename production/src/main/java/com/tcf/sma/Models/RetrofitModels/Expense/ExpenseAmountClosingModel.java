package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExpenseAmountClosingModel {

    public int ID;
    public int SchoolID;
    public int subhead_id;
    public String forDate;
    public int closingAmount;
    public String sqlServerUser;
    public String createdOnApp;
    public String createdOnServer;
    public int createdBy;
    transient String uploadedOn;
    transient ArrayList<ExpenseAmountClosingModel> eacm;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(int schoolID) {
        SchoolID = schoolID;
    }

    public int getSubhead_id() {
        return subhead_id;
    }

    public void setSubhead_id(int subhead_id) {
        this.subhead_id = subhead_id;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public int getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(int closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getSqlServerUser() {
        return sqlServerUser;
    }

    public void setSqlServerUser(String sqlServerUser) {
        this.sqlServerUser = sqlServerUser;
    }

    public String getCreatedOnApp() {
        return createdOnApp;
    }

    public void setCreatedOnApp(String createdOnApp) {
        this.createdOnApp = createdOnApp;
    }

    public String getCreatedOnServer() {
        return createdOnServer;
    }

    public void setCreatedOnServer(String createdOnServer) {
        this.createdOnServer = createdOnServer;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public ArrayList<ExpenseAmountClosingModel> getEacm() {
        return eacm;
    }

    public void setEacm(ArrayList<ExpenseAmountClosingModel> eacm) {
        this.eacm = eacm;
    }
}
