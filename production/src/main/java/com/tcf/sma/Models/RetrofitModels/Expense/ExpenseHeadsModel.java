package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExpenseHeadsModel {
    @SerializedName("ID")
    public int id;
    @SerializedName("HeadName")
    public String HeadName;
    @SerializedName("CreatedOn")
    public String CreatedOn;
    @SerializedName("CreatedBy")
    public int CreatedBy;
    @SerializedName("ModifiedOn")
    public String ModifiedOn;
    @SerializedName("ModifiedBy")
    public int ModifiedBy;
    @SerializedName("IsActive")
    public String IsActive;

    transient public int schoolId;

    transient public ArrayList<ExpenseHeadsModel> esdmList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadName() {
        return HeadName;
    }

    public void setHeadName(String headName) {
        HeadName = headName;
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

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public ArrayList<ExpenseHeadsModel> getEsdmList() {
        return esdmList;
    }

    public void setEsdmList(ArrayList<ExpenseHeadsModel> esdmList) {
        this.esdmList = esdmList;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}