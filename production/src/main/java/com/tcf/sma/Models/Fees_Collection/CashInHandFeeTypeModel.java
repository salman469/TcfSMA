package com.tcf.sma.Models.Fees_Collection;

public class CashInHandFeeTypeModel {

    private int feeType_id;
    private String Amount;
    private String LastDepositDate;


    public CashInHandFeeTypeModel() {
    }

    public CashInHandFeeTypeModel(int feeType_id, String amount) {
        this.feeType_id = feeType_id;
        Amount = amount;
    }

    public int getFeeType_id() {
        return feeType_id;
    }

    public void setFeeType_id(int feeType_id) {
        this.feeType_id = feeType_id;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getLastDepositDate() {
        return LastDepositDate;
    }

    public void setLastDepositDate(String lastDepositDate) {
        LastDepositDate = lastDepositDate;
    }
}
