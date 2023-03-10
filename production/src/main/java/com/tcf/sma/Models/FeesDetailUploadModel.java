package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

public class FeesDetailUploadModel {

    transient private int id;
    @SerializedName("FeeType_Id")
    private int feeType_id;
    @SerializedName("Amount")
    private int amount;
    @SerializedName("feeHeader_Id")
    private int feeHeader_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    public String print() {
        String data = "";
        data += "Id(" + id + ") FeeTypeId(" + feeType_id + ") Amount(" + amount + ") FeeHeaderId(" + feeHeader_id + ")";
        return data;
    }
}
