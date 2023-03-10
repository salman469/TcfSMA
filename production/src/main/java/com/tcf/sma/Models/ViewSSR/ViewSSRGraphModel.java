package com.tcf.sma.Models.ViewSSR;

/*
 * Created By Saad Saeed on 29/11/2018
 * */

public class ViewSSRGraphModel {
    private double mOverall = 0, mMale = 0, mFemale = 0;
    private String mMonth;

    public ViewSSRGraphModel(double male, double female, String month, double overall) {
        setmMale(male);
        setmFemale(female);
        setmMonth(month);
        setmOverall(overall);
    }

    public double getmOverall() {
        return mOverall;
    }

    public void setmOverall(double mOverall) {
        this.mOverall = mOverall;
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

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }
}
