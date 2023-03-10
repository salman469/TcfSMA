package com.tcf.sma.Models.RetrofitModels.HR;

public class EmployeeLeaveStatusModel {
    private int id;
    private String StatusDescription;

    public EmployeeLeaveStatusModel() {
    }

    public EmployeeLeaveStatusModel(int id, String statusDescription) {
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
