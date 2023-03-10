package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

public class ScholarshipCategoryModel {

    private int scholarship_category_id;
    private String scholarship_category_description;
    private int School_ID;
    private boolean is_active;
    private double Min_Fee;
    private double Max_Fee;
    //    private double fees_Midtermexam;
//    private double fees_finalexam;
    private double fees_admission;
    @SerializedName("Modified_On")
    private String modified_on;

    public int getScholarship_category_id() {
        return scholarship_category_id;
    }

    public void setScholarship_category_id(int scholarship_category_id) {
        this.scholarship_category_id = scholarship_category_id;
    }

    public String getScholarship_category_description() {
        return scholarship_category_description;
    }

    public void setScholarship_category_description(String scholarship_category_description) {
        this.scholarship_category_description = scholarship_category_description;
    }

    public int getSchool_ID() {
        return School_ID;
    }

    public void setSchool_ID(int school_ID) {
        School_ID = school_ID;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public double getMin_Fee() {
        return Min_Fee;
    }

    public void setMin_Fee(double min_Fee) {
        Min_Fee = min_Fee;
    }

    public double getMax_Fee() {
        return Max_Fee;
    }

    public void setMax_Fee(double max_Fee) {
        Max_Fee = max_Fee;
    }

//    public double getFees_Midtermexam() {
//        return fees_Midtermexam;
//    }
//
//    public void setFees_Midtermexam(double fees_Midtermexam) {
//        this.fees_Midtermexam = fees_Midtermexam;
//    }

//    public double getFees_finalexam() {
//        return fees_finalexam;
//    }
//
//    public void setFees_finalexam(double fees_finalexam) {
//        this.fees_finalexam = fees_finalexam;
//    }

    public double getFees_admission() {
        return fees_admission;
    }

    public void setFees_admission(double fees_admission) {
        this.fees_admission = fees_admission;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }
}
