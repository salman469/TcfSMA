package com.tcf.sma.Models.ViewSSR;

public class ViewSSRTableModel {
    private double mMale, mFemale, mOverall;
    private String rep_month;
    private String gender;

    public ViewSSRTableModel(double male, double female, double overall) {
        setmMale(male);
        setmFemale(female);
        setmOverall(overall);
    }

    public ViewSSRTableModel() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRep_month() {
        return rep_month;
    }

    public void setRep_month(String rep_month) {
        this.rep_month = rep_month;
    }

    public double getmMale() {
        return mMale;
    }

    public void setmMale(double mMale) {
        this.mMale = mMale;
    }

    public double getmFemale() {
        return mFemale;
    }

    public void setmFemale(double mFemale) {
        this.mFemale = mFemale;
    }

    public double getmOverall() {
        return mOverall;
    }

    public void setmOverall(double mOverall) {
        this.mOverall = mOverall;
    }

}
