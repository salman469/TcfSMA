package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

public class ExpenseSubheadExceptionLimitsModel {
    @SerializedName("ID")
    public int id;
    @SerializedName("SchoolID")
    public int school_id;
    @SerializedName("subHeadLimitsID")
    public int subheadlimits_id;
    @SerializedName("AllowedFrom")
    public String allowed_from;
    @SerializedName("LimitAmount")
    public int limit_amount;
    @SerializedName("StartDate")
    public String start_date;
    @SerializedName("EndDate")
    public String end_date;
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

    public ExpenseSubheadExceptionLimitsModel(int id, int school_id, int subheadlimits_id, String allowed_from, int limit_amount, String start_date, String end_date, boolean is_restricted, String created_on, int created_by, String modified_on, int modified_by, boolean isActive) {
        this.id = id;
        this.school_id = school_id;
        this.subheadlimits_id = subheadlimits_id;
        this.allowed_from = allowed_from;
        this.limit_amount = limit_amount;
        this.start_date = start_date;
        this.end_date = end_date;
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

    public int getSubheadlimits_id() {
        return subheadlimits_id;
    }

    public void setSubheadlimits_id(int subheadlimits_id) {
        this.subheadlimits_id = subheadlimits_id;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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
