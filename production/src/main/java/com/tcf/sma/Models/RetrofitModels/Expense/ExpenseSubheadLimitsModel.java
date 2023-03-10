package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

public class ExpenseSubheadLimitsModel {
    @SerializedName("ID")
    public int id;
    @SerializedName("SchoolID")
    public int school_id;
    @SerializedName("SubheadID")
    public int subhead_id;
    @SerializedName("AllowedFrom")
    public String allowed_from;
    @SerializedName("LimitAmount")
    public int limit_amount;
    @SerializedName("IsRestricted")
    public boolean is_restricted;
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

    public ExpenseSubheadLimitsModel(int id, int school_id, int subhead_id, String allowed_from, int limit_amount, boolean is_restricted, String created_on, int created_by, String modified_on, int modified_by, boolean isActive) {
        this.id = id;
        this.school_id = school_id;
        this.subhead_id = subhead_id;
        this.allowed_from = allowed_from;
        this.limit_amount = limit_amount;
        this.is_restricted = is_restricted;
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

    public int getSubhead_id() {
        return subhead_id;
    }

    public void setSubhead_id(int subhead_id) {
        this.subhead_id = subhead_id;
    }

    public String getAllowed_from() {
        return allowed_from;
    }

    public void setAllowed_from(String allowed_from) {
        this.allowed_from = allowed_from;
    }

    public int getLimit_amount() {
        return limit_amount;
    }

    public void setLimit_amount(int limit_amount) {
        this.limit_amount = limit_amount;
    }

    public boolean isIs_restricted() {
        return is_restricted;
    }

    public void setIs_restricted(boolean is_restricted) {
        this.is_restricted = is_restricted;
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
