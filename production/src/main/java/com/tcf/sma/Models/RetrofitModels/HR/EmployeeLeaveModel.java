package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeLeaveModel {

    @SerializedName("id")
    private int Employee_Leave_ID;

    @SerializedName("userID")
    private int Employee_Personal_Detail_ID;

    @SerializedName("LeaveType")
    private int Leave_Type_ID;

    @SerializedName("startDate")
    private String Leave_Start_Date;

    @SerializedName("endDate")
    private String Leave_End_Date;

    @SerializedName("LeaveStatus")
    private int Leave_status_id;

    @SerializedName("createdBy")
    private int created_By;

    @SerializedName("createdOnServer")
    private String createdOn_Server;

    @SerializedName("CreatedOnApp")
    private String createdOn_App;

    @SerializedName("deviceId")
    private String device_Id;

    private int server_Id;

    @SerializedName("schoolID")
    private int schoolId;

    private String modifiedOn;


    transient private String UploadedOn;
    transient private String Leave_Reason;
    transient private String Leave_status;
    transient private EmployeeModel employeeModel;
    transient private ArrayList<EmployeeLeaveModel> empLeavesList;
    transient private String leavesLWD;

//    private String Emp_First_Name;
//    private String Emp_Last_Name;

    public int getEmployee_Leave_ID() {
        return Employee_Leave_ID;
    }

    public void setEmployee_Leave_ID(int employee_Leave_ID) {
        Employee_Leave_ID = employee_Leave_ID;
    }

    public int getEmployee_Personal_Detail_ID() {
        return Employee_Personal_Detail_ID;
    }

    public void setEmployee_Personal_Detail_ID(int employee_Personal_Detail_ID) {
        Employee_Personal_Detail_ID = employee_Personal_Detail_ID;
    }

    public String getLeave_status() {
        return Leave_status;
    }

    public void setLeave_status(String leave_status) {
        Leave_status = leave_status;
    }

    public String getLeave_Start_Date() {
        return Leave_Start_Date;
    }

    public void setLeave_Start_Date(String leave_Start_Date) {
        Leave_Start_Date = leave_Start_Date;
    }

    public String getLeave_End_Date() {
        return Leave_End_Date;
    }

    public void setLeave_End_Date(String leave_End_Date) {
        Leave_End_Date = leave_End_Date;
    }

    public int getLeave_Type_ID() {
        return Leave_Type_ID;
    }

    public void setLeave_Type_ID(int leave_Type_ID) {
        Leave_Type_ID = leave_Type_ID;
    }

    public int getCreated_By() {
        return created_By;
    }

    public void setCreated_By(int created_By) {
        this.created_By = created_By;
    }

    public String getCreatedOn_Server() {
        return createdOn_Server;
    }

    public void setCreatedOn_Server(String createdOn_Server) {
        this.createdOn_Server = createdOn_Server;
    }

    public String getCreatedOn_App() {
        return createdOn_App;
    }

    public void setCreatedOn_App(String createdOn_App) {
        this.createdOn_App = createdOn_App;
    }

    public String getDevice_Id() {
        return device_Id;
    }

    public void setDevice_Id(String device_Id) {
        this.device_Id = device_Id;
    }

    public EmployeeModel getEmployeeModel() {
        return employeeModel;
    }

    public void setEmployeeModel(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    public String getLeave_Reason() {
        return Leave_Reason;
    }

    public void setLeave_Reason(String leave_Reason) {
        Leave_Reason = leave_Reason;
    }

    public int getLeave_status_id() {
        return Leave_status_id;
    }

    public void setLeave_status_id(int leave_status_id) {
        Leave_status_id = leave_status_id;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }

    public ArrayList<EmployeeLeaveModel> getEmpLeavesList() {
        return empLeavesList;
    }

    public void setEmpLeavesList(ArrayList<EmployeeLeaveModel> empLeavesList) {
        this.empLeavesList = empLeavesList;
    }

    public int getServer_Id() {
        return server_Id;
    }

    public void setServer_Id(int server_Id) {
        this.server_Id = server_Id;
    }


    public String getLeavesLWD() {
        return leavesLWD;
    }

    public void setLeavesLWD(String leavesLWD) {
        this.leavesLWD = leavesLWD;
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
