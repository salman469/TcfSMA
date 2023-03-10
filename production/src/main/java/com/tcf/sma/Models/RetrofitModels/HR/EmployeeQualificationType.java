package com.tcf.sma.Models.RetrofitModels.HR;

public class EmployeeQualificationType {

    private int EmployeeQualificationType_ID;
    private boolean IsActive;
    private String Qualification_Type;

    public int getEmployeeQualificationType_ID() {
        return EmployeeQualificationType_ID;
    }

    public void setEmployeeQualificationType_ID(int employeeQualificationType_ID) {
        EmployeeQualificationType_ID = employeeQualificationType_ID;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getQualification_Type() {
        return Qualification_Type;
    }

    public void setQualification_Type(String qualification_Type) {
        Qualification_Type = qualification_Type;
    }
}
