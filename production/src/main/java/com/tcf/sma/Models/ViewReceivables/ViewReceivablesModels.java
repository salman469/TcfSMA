package com.tcf.sma.Models.ViewReceivables;

/**
 * Created By Saad
 */

public class ViewReceivablesModels {
    private double mDue;
    private double mBalance;
    private double mCollected;

    public ViewReceivablesModels() {
    }

    public ViewReceivablesModels(double totalDue, double balance, double collected) {
        setmDue(totalDue);
        setmBalance(balance);
        setmCollected(collected);
    }


    public double getmBalance() {
        return mBalance;
    }

    public void setmBalance(double mBalance) {
        this.mBalance = mBalance;
    }

    public double getmCollected() {
        return mCollected;
    }

    public void setmCollected(double mCollected) {
        this.mCollected = mCollected;
    }

    public double getmDue() {
        return mDue;
    }

    public void setmDue(double mDue) {
        this.mDue = mDue;
    }
}
