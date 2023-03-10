package com.tcf.sma.Models.RetrofitModels.HRTCT;

import com.google.gson.annotations.SerializedName;

public class TCTEmpSubjTagReasonModel {
    @SerializedName("ID")
    private int id;

    private String Reason;

    private String modifiedOn;

    public TCTEmpSubjTagReasonModel(int id, String reason) {
        this.id = id;
        this.Reason = reason;
    }

    public TCTEmpSubjTagReasonModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    //to display object as a string in spinner
    public String toString() {
        return getReason();
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
