package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

public class ExpenseSchoolPettyCashLimitsModel {

    @SerializedName("ID")
    public int id;
    @SerializedName("SchoolID")
    public int school_id;
    @SerializedName("AllowedFrom")
    public String AllowedFrom;
    @SerializedName("LimitAmount")
    public int limit_amount;
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

    public ExpenseSchoolPettyCashLimitsModel(int id, int school_id, int limit_amount, String created_on, int created_by, String modified_on, int modified_by, boolean isActive) {
        this.id = id;
        this.school_id = school_id;
        this.limit_amount = limit_amount;
        this.created_on = created_on;
        this.created_by = created_by;
        this.modified_on = modified_on;
        this.modified_by = modified_by;
        IsActive = isActive;
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

    public String getAllowedFrom() {
        return AllowedFrom;
    }

    public void setAllowedFrom(String allowedFrom) {
        AllowedFrom = allowedFrom;
    }

    public int getLimit_amount() {
        return limit_amount;
    }

    public void setLimit_amount(int limit_amount) {
        this.limit_amount = limit_amount;
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
}