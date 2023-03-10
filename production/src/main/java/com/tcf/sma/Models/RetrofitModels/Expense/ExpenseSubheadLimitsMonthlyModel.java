package com.tcf.sma.Models.RetrofitModels.Expense;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExpenseSubheadLimitsMonthlyModel {
    @SerializedName("ID")
    public int id;
    @SerializedName("SchoolID")
    public int school_id;
    @SerializedName("SubheadID")
    public int subhead_id;
    @SerializedName("SalaryUserID")
    public int salary_userid;
    @SerializedName("AllowedFrom")
    public String allowed_from;
    @SerializedName("ForDate")
    public String for_date;
    @SerializedName("LimitAmount")
    public int limit_amount;
    @SerializedName("SpentAmount")
    public int Spent_amount;
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

    transient String uploadedOn;
    transient public ArrayList<ExpenseSubheadLimitsMonthlyModel> eslmList;

    public ExpenseSubheadLimitsMonthlyModel(){
    }

    public ExpenseSubheadLimitsMonthlyModel(int id, int school_id, int subhead_id, int salary_userid, String allowed_from, String for_date, int limit_amount, int spent_amount, boolean is_restricted, String created_on, int created_by, String modified_on, int modified_by, boolean isActive) {
        this.id = id;
        this.school_id = school_id;
        this.subhead_id = subhead_id;
        this.salary_userid = salary_userid;
        this.allowed_from = allowed_from;
        this.for_date = for_date;
        this.limit_amount = limit_amount;
        Spent_amount = spent_amount;
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

    public int getSalary_userid() {
        return salary_userid;
    }

    public void setSalary_userid(int salary_userid) {
        this.salary_userid = salary_userid;
    }

    public String getAllowed_from() {
        return allowed_from;
    }

    public void setAllowed_from(String allowed_from) {
        this.allowed_from = allowed_from;
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

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public ArrayList<ExpenseSubheadLimitsMonthlyModel> getEslmList() {
        return eslmList;
    }

    public void setEslmList(ArrayList<ExpenseSubheadLimitsMonthlyModel> eslmList) {
        this.eslmList = eslmList;
    }
}
