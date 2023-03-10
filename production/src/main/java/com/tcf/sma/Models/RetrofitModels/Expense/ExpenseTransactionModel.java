package com.tcf.sma.Models.RetrofitModels.Expense;

import java.util.ArrayList;

public class ExpenseTransactionModel {
    public int ID;
    public int schoolID;
    public int academicSessionID;
    public int subHeadID;
    public Integer closingID=null;
    public Integer salaryUserID=null;
    public int categoryID;
    public int bucketID;
    public int flowID;
    public String forDate;
    public double transAmount;
    public long jvNo;
    public String chequeNo;
    public String receiptNo;
    public String remarks;
    public String sqlServerUser;
    public String CreatedOnApp;
    public String CreatedOnServer;
    public int CreatedBy;
    public String ModifiedOnApp;
    public String ModifiedOnServer;
    public int ModifiedBy;
    public Boolean IsActive;
    public String createdFrom;
    public String modifiedFrom;
    public String uploadedOn;

    public String imagePath;
    public String imageCategory;

    transient public ArrayList<ExpenseTransactionModel> etmList;



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public int getAcademicSessionID() {
        return academicSessionID;
    }

    public void setAcademicSessionID(int academicSessionID) {
        this.academicSessionID = academicSessionID;
    }

    public int getSubHeadID() {
        return subHeadID;
    }

    public void setSubHeadID(int subHeadID) {
        this.subHeadID = subHeadID;
    }

    public Integer getClosingID() {
        return closingID;
    }

    public void setClosingID(Integer closingID) {
        this.closingID = closingID;
    }

    public Integer getSalaryUserID() {
        return salaryUserID;
    }

    public void setSalaryUserID(Integer salaryUserID) {
        this.salaryUserID = salaryUserID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getBucketID() {
        return bucketID;
    }

    public void setBucketID(int bucketID) {
        this.bucketID = bucketID;
    }

    public int getFlowID() {
        return flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public long getJvNo() {
        return jvNo;
    }

    public void setJvNo(long jvNo) {
        this.jvNo = jvNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSqlserverUser() {
        return sqlServerUser;
    }

    public void setSqlserverUser(String sqlServerUser) {
        this.sqlServerUser = sqlServerUser;
    }

    public String getCreatedOnApp() {
        return CreatedOnApp;
    }

    public void setCreatedOnApp(String createdOnApp) {
        this.CreatedOnApp = createdOnApp;
    }

    public String getCreatedOnServer() {
        return CreatedOnServer;
    }

    public void setCreatedOnServer(String createdOnServer) {
        this.CreatedOnServer = createdOnServer;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifiedOnApp() {
        return ModifiedOnApp;
    }

    public void setModifiedOnApp(String modifiedOnApp) {
        ModifiedOnApp = modifiedOnApp;
    }

    public String getModifiedOnServer() {
        return ModifiedOnServer;
    }

    public void setModifiedOnServer(String modifiedOnServer) {
        ModifiedOnServer = modifiedOnServer;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public Boolean isActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public String getModifiedFrom() {
        return modifiedFrom;
    }

    public void setModifiedFrom(String modifiedFrom) {
        this.modifiedFrom = modifiedFrom;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }

    public ArrayList<ExpenseTransactionModel> getEtmList() {
        return etmList;
    }

    public void setEtmList(ArrayList<ExpenseTransactionModel> etmList) {
        this.etmList = etmList;
    }
}
