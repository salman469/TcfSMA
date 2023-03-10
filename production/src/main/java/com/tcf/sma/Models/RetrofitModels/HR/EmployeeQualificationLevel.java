package com.tcf.sma.Models.RetrofitModels.HR;

public class EmployeeQualificationLevel {
    private int EmployeeQualificationLevel_ID;
    private boolean IsActive;
    private String Qualification_Level;

    public int getEmployeeQualificationLevel_ID() {
        return EmployeeQualificationLevel_ID;
    }

    public void setEmployeeQualificationLevel_ID(int employeeQualificationLevel_ID) {
        EmployeeQualificationLevel_ID = employeeQualificationLevel_ID;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getQualification_Level() {
        return Qualification_Level;
    }

    public void setQualification_Level(String qualification_Level) {
        Qualification_Level = qualification_Level;
    }
}
