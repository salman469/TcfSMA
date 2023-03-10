package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeeResignationStatusModel {
    private int id;
    @SerializedName("status")
    private String StatusDescription;

    public EmployeeResignationStatusModel() {
    }

    public EmployeeResignationStatusModel(int id, String statusDescription) {
        this.id = id;
        this.StatusDescription = statusDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }

    @Override
    public String toString() {
        return getStatusDescription();
    }
}
