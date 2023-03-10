package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExpenseSchoolPettyCashMonthlyLimitsModel {

    @SerializedName("ID")
    public int id;
    @SerializedName("SchoolID")
    public int school_id;
    @SerializedName("AllowedFrom")
    public String AllowedFrom;
    @SerializedName("ForDate")
    public String for_date;
    @SerializedName("LimitAmount")
    public int limit_amount;
    @SerializedName("SpentAmount")
    public int Spent_amount;
    @SerializedName("IsRestricted")
    public boolean IsRestricted;
    @SerializedName("CreatedOn")
    public String created_on;
    @SerializedName("CreatedBy")
    public int created_by;
    @SerializedName("ModifiedOn")
    public String modified_on;
    @SerializedName("ModifiedBy")
    public int modified_by;
    @SerializedName("IsActive")
    public boolean IsActive;

    transient String uploadedOn;
    transient public ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> eslmList;

    public ExpenseSchoolPettyCashMonthlyLimitsModel(int id, int school_id, String for_date, int limit_amount, int spent_amount, String created_on, int created_by, String modified_on, int modified_by, boolean isActive) {
        this.id = id;
        this.school_id = school_id;
        this.for_date = for_date;
        this.limit_amount = limit_amount;
        Spent_amount = spent_amount;
        this.created_on = created_on;
        this.created_by = created_by;
        this.modified_on = modified_on;
        this.modified_by = modified_by;
        IsActive = isActive;
    }

    public ExpenseSchoolPettyCashMonthlyLimitsModel(){}

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

    public String getAllowedFrom() {
        return AllowedFrom;
    }

    public void setAllowedFrom(String allowedFrom) {
        AllowedFrom = allowedFrom;
    }

    public String getFor_date() {
        return for_date;
    }

    public void setFor_date(String for_date) {
        this.for_date = for_date;
    }

    public int getLimit_amount() {
        return limit_amount;
    }

    public void setLimit_amount(int limit_amount) {
        this.limit_amount = limit_amount;
    }

    public int getSpent_amount() {
        return Spent_amount;
    }

    public void setSpent_amount(int spent_amount) {
        Spent_amount = spent_amount;
    }

    public boolean isRestricted() {
        return IsRestricted;
    }

    public void setRestricted(boolean restricted) {
        IsRestricted = restricted;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
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

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> getEslmList() {
        return eslmList;
    }

    public void setEslmList(ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> eslmList) {
        this.eslmList = eslmList;
    }
}
