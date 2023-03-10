package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

public class ExpenseSubHeadsModel {

    @SerializedName("ID")
    public int id;
    @SerializedName("HeadID")
    public int head_id;
    @SerializedName("SubHeadName")
    public String SubHeadName;
    @SerializedName("CreatedOn")
    public String CreatedOn;
    @SerializedName("CreatedBy")
    public int CreatedBy;
    @SerializedName("ModifiedOn")
    public String ModifiedOn;
    @SerializedName("ModifiedBy")
    public int ModifiedBy;
    @SerializedName("IsActive")
    public boolean IsActive;

    transient public int schoolId;


    public ExpenseSubHeadsModel(int id, int head_id, String subHeadName, String createdOn, int createdBy, String modifiedOn, int modifiedBy, boolean isActive) {
        this.id = id;
        this.head_id = head_id;
        SubHeadName = subHeadName;
        CreatedOn = createdOn;
        CreatedBy = createdBy;
        ModifiedOn = modifiedOn;
        ModifiedBy = modifiedBy;
        IsActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHead_id() {
        return head_id;
    }

    public void setHead_id(int head_id) {
        this.head_id = head_id;
    }

    public String getSubHeadName() {
        return SubHeadName;
    }

    public void setSubHeadName(String subHeadName) {
        SubHeadName = subHeadName;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
