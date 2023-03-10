package com.tcf.sma.Models.RetrofitModels.HR;

public class EmployeeApprovalStatusModel {
    private int id;
    private String StatusDescription;

    public EmployeeApprovalStatusModel() {
    }

    public EmployeeApprovalStatusModel(int id, String statusDescription) {
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
