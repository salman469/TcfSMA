package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeSeparationDetailModel {
    @SerializedName("ResignationApprovalID")
    public int id;
    @SerializedName("Status")
    public int emp_status;
    @SerializedName("Remarks")
    public String Separation_Remarks;
    @SerializedName("ModifiedBy")
    public int MODIFIED_BY;
    public int employeeResignationId;
    public int approver_userId;
    public String MODIFIED_ON;
    public String deviceId;
    transient public int CREATED_BY;
    transient public String CREATED_ON_APP;
    transient public String CREATED_ON_SERVER;
    transient public String UPLOADED_ON;
    transient public int serverId;
    transient public ArrayList<EmployeeSeparationDetailModel> esdmList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeResignationId() {
        return employeeResignationId;
    }

    public void setEmployeeResignationId(int employeeResignationId) {
        this.employeeResignationId = employeeResignationId;
    }

    public int getApprover_userId() {
        return approver_userId;
    }

    public void setApprover_userId(int approver_userId) {
        this.approver_userId = approver_userId;
    }

    public int getEmp_status() {
        return emp_status;
    }

    public void setEmp_status(int emp_status) {
        this.emp_status = emp_status;
    }

    public String getSeparation_Remarks() {
        return Separation_Remarks;
    }

    public void setSeparation_Remarks(String separation_Remarks) {
        Separation_Remarks = separation_Remarks;
    }

    public int getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(int CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getCREATED_ON_APP() {
        return CREATED_ON_APP;
    }

    public void setCREATED_ON_APP(String CREATED_ON_APP) {
        this.CREATED_ON_APP = CREATED_ON_APP;
    }

    public String getCREATED_ON_SERVER() {
        return CREATED_ON_SERVER;
    }

    public void setCREATED_ON_SERVER(String CREATED_ON_SERVER) {
        this.CREATED_ON_SERVER = CREATED_ON_SERVER;
    }

    public String getMODIFIED_ON() {
        return MODIFIED_ON;
    }

    public void setMODIFIED_ON(String MODIFIED_ON) {
        this.MODIFIED_ON = MODIFIED_ON;
    }

    public int getMODIFIED_BY() {
        return MODIFIED_BY;
    }

    public void setMODIFIED_BY(int MODIFIED_BY) {
        this.MODIFIED_BY = MODIFIED_BY;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUPLOADED_ON() {
        return UPLOADED_ON;
    }

    public void setUPLOADED_ON(String UPLOADED_ON) {
        this.UPLOADED_ON = UPLOADED_ON;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public ArrayList<EmployeeSeparationDetailModel> getEsdmList() {
        return esdmList;
    }

    public void setEsdmList(ArrayList<EmployeeSeparationDetailModel> esdmList) {
        this.esdmList = esdmList;
    }
}
