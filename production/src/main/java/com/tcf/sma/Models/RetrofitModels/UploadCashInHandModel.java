package com.tcf.sma.Models.RetrofitModels;

public class UploadCashInHandModel {
    private int schoolID;
    private long cashInHand;

    public UploadCashInHandModel(int schoolID, long cashInHand) {
        this.schoolID = schoolID;
        this.cashInHand = cashInHand;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public long getCashInHand() {
        return cashInHand;
    }

    public void setCashInHand(long cashInHand) {
        this.cashInHand = cashInHand;
    }
}
