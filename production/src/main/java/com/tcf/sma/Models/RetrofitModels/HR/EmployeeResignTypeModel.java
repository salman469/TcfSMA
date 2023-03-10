package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeeResignTypeModel {

    @SerializedName("id")
    private int Id;

    @SerializedName("Type")
    private String ResignType;

    private String modifiedOn;

    public EmployeeResignTypeModel() {
    }


    public EmployeeResignTypeModel(int id, String resignType) {
        Id = id;
        ResignType = resignType;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getResignType() {
        return ResignType;
    }

    public void setResignType(String resignType) {
        ResignType = resignType;
    }

    @Override
    public String toString() {
        return getResignType();
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
