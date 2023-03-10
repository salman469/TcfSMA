package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeTeacherAttendanceModel {
    private int id;

    @SerializedName("userId")
    private int Employee_Personal_Detail_ID;

    @SerializedName("attendanceType")
    private int Attendance_Type_ID;

    @SerializedName("leaveType")
    private int Leave_Type_ID;

    @SerializedName("forDate")
    private String for_date;

    private String Reason;

    @SerializedName("createdBy")
    private int created_By;

    @SerializedName("createdOnServer")
    private String createdOn_Server;

    @SerializedName("createdOnApp")
    private String createdOn_App;

    @SerializedName("device_id")
    private String device_Id;

    transient private String uploaded_on;

    @SerializedName("server_id")
    private int serverId;

    transient private ArrayList<EmployeeTeacherAttendanceModel> etamList;

    private boolean isActive;

    private String modifiedOn;

    private String modifiedBy;

    @SerializedName("schoolID")
    private int schoolId;


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

    public int getAttendance_Type_ID() {
        return Attendance_Type_ID;
    }

    public void setAttendance_Type_ID(int attendance_Type_ID) {
        Attendance_Type_ID = attendance_Type_ID;
    }

    public int getLeave_Type_ID() {
        return Leave_Type_ID;
    }

    public void setLeave_Type_ID(int leave_Type_ID) {
        Leave_Type_ID = leave_Type_ID;
    }

    public String getFor_date() {
        return for_date;
    }

    public void setFor_date(String for_date) {
        this.for_date = for_date;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
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

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public ArrayList<EmployeeTeacherAttendanceModel> getEtamList() {
        return etamList;
    }

    public void setEtamList(ArrayList<EmployeeTeacherAttendanceModel> etamList) {
        this.etamList = etamList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
