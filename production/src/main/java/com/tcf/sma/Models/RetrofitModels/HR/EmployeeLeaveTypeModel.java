package com.tcf.sma.Models.RetrofitModels.HR;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class EmployeeLeaveTypeModel {

    @SerializedName("Id")
    private int id;

    @SerializedName("LeaveName")
    private String Leave_Name;

    @SerializedName("AllowedWorkingDays")
    private int Leave_AllowedWorkingDays;

    transient private String Emp_gender;

    private String modifiedOn;

    public EmployeeLeaveTypeModel() {
    }

    public EmployeeLeaveTypeModel(int id, String Leave_Name) {
        this.id = id;
        this.Leave_Name = Leave_Name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeave_Name() {
        return Leave_Name;
    }

    public void setLeave_Name(String leave_Name) {
        Leave_Name = leave_Name;
    }

    public int getLeave_AllowedWorkingDays() {
        return Leave_AllowedWorkingDays;
    }

    public void setLeave_AllowedWorkingDays(int leave_AllowedWorkingDays) {
        Leave_AllowedWorkingDays = leave_AllowedWorkingDays;
    }

    public String getEmp_gender() {
        return Emp_gender;
    }

    public void setEmp_gender(String emp_gender) {
        Emp_gender = emp_gender;
    }

    @NonNull
    @Override
    public String toString() {
        return getLeave_Name();
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
