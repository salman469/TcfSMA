package com.tcf.sma.Models.Fees_Collection;

public class PreviousReceivableModel {

    private int feeType_id;
    private String totalAmount;
    //for Correction
    private String forDate;

    public PreviousReceivableModel() {
    }

    public PreviousReceivableModel(int feeType_id, String totalAmount) {
        this.feeType_id = feeType_id;
        this.totalAmount = totalAmount;
    }

    public int getFeeType_id() {
        return feeType_id;
    }

    public void setFeeType_id(int feeType_id) {
        this.feeType_id = feeType_id;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }
}
