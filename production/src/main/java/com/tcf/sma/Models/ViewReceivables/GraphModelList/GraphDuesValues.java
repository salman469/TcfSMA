package com.tcf.sma.Models.ViewReceivables.GraphModelList;

public class GraphDuesValues {
    private double mAmount;
    private String mMonth;

    public GraphDuesValues(String month, double amount) {
        setAmount(amount);
        setMonth(month);
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
}
