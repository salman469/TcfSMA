package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeeResignReasonModel {
    @SerializedName("SeperationSubReasonID")
    private int ID;

    @SerializedName("Reason")
    private String ResignReason;

    @SerializedName("SubReason")
    private String subReason;

    @SerializedName("WorkflowID")
    private int resignTypeID;

    private String modifiedOn;

    public EmployeeResignReasonModel() {
    }

    public EmployeeResignReasonModel(int ID, String resignReason) {
        this.ID = ID;
        ResignReason = resignReason;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getResignReason() {
        return ResignReason;
    }

    public void setResignReason(String resignReason) {
        ResignReason = resignReason;
    }

    public String getSubReason() {
        return subReason;
    }

    public void setSubReason(String subReason) {
        this.subReason = subReason;
    }

    public int getResignTypeID() {
        return resignTypeID;
    }

    public void setResignTypeID(int resignTypeID) {
        this.resignTypeID = resignTypeID;
    }

    @Override
    public String toString() {
        return getResignReason();
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
