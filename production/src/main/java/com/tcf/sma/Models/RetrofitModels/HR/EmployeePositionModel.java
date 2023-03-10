package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeePositionModel {

    transient private int id;

    @SerializedName("schoolId")
    private int SchoolId;

    @SerializedName("userID")
    private int Employee_Personal_Detail_ID;

    @SerializedName("EmployeeCode")
    private String Emp_Code;

    @SerializedName("positionName")
    private String Position_Name;

    @SerializedName("startDate")
    private String Position_Start_Date;

    @SerializedName("endDate")
    private String Position_End_Date;

    private String modifiedOn;

    transient private String Emp_Designation;
    transient private String School_Name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee_Personal_Detail_ID() {
        return Employee_Personal_Detail_ID;
    }

    public void setEmployee_Personal_Detail_ID(int employee_Personal_Detail_ID) {
        Employee_Personal_Detail_ID = employee_Personal_Detail_ID;
    }

    public String getPosition_Name() {
        return Position_Name;
    }

    public void setPosition_Name(String position_Name) {
        Position_Name = position_Name;
    }

    public String getPosition_Start_Date() {
        return Position_Start_Date;
    }

    public void setPosition_Start_Date(String position_Start_Date) {
        Position_Start_Date = position_Start_Date;
    }

    public String getPosition_End_Date() {
        return Position_End_Date;
    }

    public void setPosition_End_Date(String position_End_Date) {
        Position_End_Date = position_End_Date;
    }

    public String getSchool_Name() {
        return School_Name;
    }

    public void setSchool_Name(String school_Name) {
        School_Name = school_Name;
    }

    public String getEmp_Designation() {
        return Emp_Designation;
    }

    public void setEmp_Designation(String emp_Designation) {
        Emp_Designation = emp_Designation;
    }

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public String getEmp_Code() {
        return Emp_Code;
    }

    public void setEmp_Code(String emp_Code) {
        Emp_Code = emp_Code;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
