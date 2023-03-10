package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UploadEmployeeModel {
    @SerializedName("userId")
    private int id;

    @SerializedName("MobileNumber")
    private String Mobile_No;

    //    @SerializedName("EmpId")
    transient private int Employee_ID;

    transient private String Employee_Code;

    //    @SerializedName("firstname")
    transient private String First_Name;

    //    @SerializedName("lastname")
    transient private String Last_Name;

    @SerializedName("EmployeeEmail")
    private String Email;


    //    @SerializedName("designation")
    transient private String Designation;

    //    @SerializedName("gender")
    transient private String Gender;

    //    @SerializedName("father_name")
    transient private String Father_Name;

    //    @SerializedName("mother_maiden_name")
    transient private String Mother_Name;

    //    @SerializedName("nic_number")
    transient private String NIC_No;

    //    @SerializedName("CADRE")
    transient private String CADRE;

    //    @SerializedName("IsActive")
    transient private boolean Is_Active;

    //    @SerializedName("joining_date")
    transient private String Date_Of_Joining;

    transient private String ModifiedBy;

    transient private String ModifiedOn;

    //    @SerializedName("lastWorkingDay")
    transient private String LastWorkingDay;

    transient private String UploadedOn;
    transient private int AttendanceType_id;

    transient private String Division_Name;
    transient private String DOB;
    transient private ArrayList<UploadEmployeeModel> employeesList;
    transient private EmployeeLeaveModel employeeLeaveModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(int employee_ID) {
        Employee_ID = employee_ID;
    }

    public String getEmployee_Code() {
        return Employee_Code;
    }

    public void setEmployee_Code(String employee_Code) {
        Employee_Code = employee_Code;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    public String getFather_Name() {
        return Father_Name;
    }

    public void setFather_Name(String father_Name) {
        Father_Name = father_Name;
    }

    public String getMother_Name() {
        return Mother_Name;
    }

    public void setMother_Name(String mother_Name) {
        Mother_Name = mother_Name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getNIC_No() {
        return NIC_No;
    }

    public void setNIC_No(String NIC_No) {
        this.NIC_No = NIC_No;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getCADRE() {
        return CADRE;
    }

    public void setCADRE(String CADRE) {
        this.CADRE = CADRE;
    }

//    public boolean getIs_Active_Directory() {
//        return Is_Active_Directory;
//    }
//
//    public void setIs_Active_Directory(boolean is_Active_Directory) {
//        Is_Active_Directory = is_Active_Directory;
//    }


    public boolean getIs_Active() {
        return Is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        Is_Active = is_Active;
    }

    public String getDate_Of_Joining() {
        return Date_Of_Joining;
    }

    public void setDate_Of_Joining(String date_Of_Joining) {
        Date_Of_Joining = date_Of_Joining;
    }

    public String getLastWorkingDay() {
        return LastWorkingDay;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        LastWorkingDay = lastWorkingDay;
    }

    public int getAttendanceType_id() {
        return AttendanceType_id;
    }

    public void setAttendanceType_id(int attendanceType_id) {
        AttendanceType_id = attendanceType_id;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public ArrayList<UploadEmployeeModel> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(ArrayList<UploadEmployeeModel> employeesList) {
        this.employeesList = employeesList;
    }

    public EmployeeLeaveModel getEmployeeLeaveModel() {
        return employeeLeaveModel;
    }

    public void setEmployeeLeaveModel(EmployeeLeaveModel employeeLeaveModel) {
        this.employeeLeaveModel = employeeLeaveModel;
    }
}
