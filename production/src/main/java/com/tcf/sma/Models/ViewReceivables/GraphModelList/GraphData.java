package com.tcf.sma.Models.ViewReceivables.GraphModelList;

/*
 * Created By Saad Saeed on 17/Nov/2018
 * */

public class GraphData {
    private double mAmount;
    private String mMonth;
    private String mDataType;

    public GraphData(String month, double amount, String dataType) {
        setAmount(amount);
        setMonth(month);
        setmDataType(dataType);
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public String getmDataType() {
        return mDataType;
    }

    public void setmDataType(String mDataType) {
        this.mDataType = mDataType;
    }
}
