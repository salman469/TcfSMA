package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeeSchoolModel {

    @SerializedName("userID")
    private int Employee_Personal_Detail_ID;

    private String Employee_Code;

    private int schoolId;

    private String modifiedOn;

    public int getEmployee_Personal_Detail_ID() {
        return Employee_Personal_Detail_ID;
    }

    public void setEmployee_Personal_Detail_ID(int employee_Personal_Detail_ID) {
        Employee_Personal_Detail_ID = employee_Personal_Detail_ID;
    }

    public String getEmployee_Code() {
        return Employee_Code;
    }

    public void setEmployee_Code(String employee_Code) {
        Employee_Code = employee_Code;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
