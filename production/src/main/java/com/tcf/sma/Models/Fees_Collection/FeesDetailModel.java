package com.tcf.sma.Models.Fees_Collection;

public class FeesDetailModel {

    private int id;
    private double amount;
    private int feeType_id;
    private int feeHeader_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getFeeType_id() {
        return feeType_id;
    }

    public void setFeeType_id(int feeType_id) {
        this.feeType_id = feeType_id;
    }

    public int getFeeHeader_id() {
        return feeHeader_id;
    }

    public void setFeeHeader_id(int feeHeader_id) {
        this.feeHeader_id = feeHeader_id;
    }
}
