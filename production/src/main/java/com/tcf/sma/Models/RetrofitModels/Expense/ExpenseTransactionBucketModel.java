package com.tcf.sma.Models.RetrofitModels.Expense;

public class ExpenseTransactionBucketModel {

    public int ID;
    public String bucketName;
    public String CreatedOn;
    public int CreatedBy;
    public String ModifiedOn;
    public int ModifiedBy;
    public boolean IsActive;
    transient private String UploadedOn;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        this.CreatedOn = createdOn;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        this.CreatedBy = createdBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.ModifiedOn = modifiedOn;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.ModifiedBy = modifiedBy;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }
}
