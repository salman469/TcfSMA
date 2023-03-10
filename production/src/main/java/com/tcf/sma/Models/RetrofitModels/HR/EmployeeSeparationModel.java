package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeSeparationModel {

    transient public ArrayList<EmployeeSeparationModel> ermList;
    @SerializedName("ID")
    private int id;
    @SerializedName("ResignedUserID")
    private int Employee_Personal_Detail_ID;
    @SerializedName("SubReasonID")
    private int Emp_SubReasonID;
    @SerializedName("ResignationDate")
    private String Emp_Resign_Date;
    @SerializedName("LastWorkingDate")
    private String LastWorkingDay;
    @SerializedName("SubReasonText")
    private String subReasonText;
    @SerializedName("CreatedBy")
    private int CREATED_BY;
    @SerializedName("CancelledOn")
    private String CancelledOn;
    @SerializedName("CancelledBy")
    private int CancelledBy;
    @SerializedName("ModifiedOn")
    private String MODIFIED_ON;
    @SerializedName("ModifiedBy")
    private int MODIFIED_BY;
    @SerializedName("ReasonID")
    private int ReasonID;

    @SerializedName("IsActive")
    private boolean isActive;
    private int schoolID;
    private int lwop;
    transient private int SubReasonID;



    transient private String deviceId;


    @SerializedName("DeviceID")
    private int server_id;


    @SerializedName("Status")
    transient private int Emp_Status;
    @SerializedName("resignLetterImg")
    transient private String Emp_Resign_Letter_IMG;
    @SerializedName("resignFormImg")
    transient private String Emp_Resign_Form_IMG;
    @SerializedName("CreatedOn_APP")
    private String CREATED_ON_APP;
    @SerializedName("Remarks")
    private String Emp_Resign_Cancel_Reason;
    @SerializedName("resignType")
    transient private int Emp_Resign_Type;
    transient private String UploadedOn;
    transient private String CREATED_ON_SERVER;
    transient private int sep_status;

    @SerializedName("ImagePath")
    private String imagePath;

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

    public int getEmp_Status() {
        return Emp_Status;
    }

    public void setEmp_Status(int emp_Status) {
        Emp_Status = emp_Status;
    }

    public String getCREATED_ON_APP() {
        return CREATED_ON_APP;
    }

    public void setCREATED_ON_APP(String CREATED_ON_APP) {
        this.CREATED_ON_APP = CREATED_ON_APP;
    }

    public String getEmp_Resign_Cancel_Reason() {
        return Emp_Resign_Cancel_Reason;
    }

    public void setEmp_Resign_Cancel_Reason(String emp_Resign_Cancel_Reason) {
        Emp_Resign_Cancel_Reason = emp_Resign_Cancel_Reason;
    }

    public int getEmp_Resign_Type() {
        return Emp_Resign_Type;
    }

    public void setEmp_Resign_Type(int emp_Resign_Type) {
        Emp_Resign_Type = emp_Resign_Type;
    }

    public String getEmp_Resign_Date() {
        return Emp_Resign_Date;
    }

    public void setEmp_Resign_Date(String emp_Resign_Date) {
        Emp_Resign_Date = emp_Resign_Date;
    }

    public String getEmp_Resign_Letter_IMG() {
        return Emp_Resign_Letter_IMG;
    }

    public void setEmp_Resign_Letter_IMG(String emp_Resign_Letter_IMG) {
        Emp_Resign_Letter_IMG = emp_Resign_Letter_IMG;
    }

    public int getEmp_SubReasonID() {
        return Emp_SubReasonID;
    }

    public void setEmp_SubReasonID(int emp_SubReasonID) {
        Emp_SubReasonID = emp_SubReasonID;
    }

    public String getEmp_Resign_Form_IMG() {
        return Emp_Resign_Form_IMG;
    }

    public void setEmp_Resign_Form_IMG(String emp_Resign_Form_IMG) {
        Emp_Resign_Form_IMG = emp_Resign_Form_IMG;
    }

    public String getLastWorkingDay() {
        return LastWorkingDay;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        LastWorkingDay = lastWorkingDay;
    }


    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }

    public int getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(int CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getCREATED_ON_SERVER() {
        return CREATED_ON_SERVER;
    }

    public void setCREATED_ON_SERVER(String CREATED_ON_SERVER) {
        this.CREATED_ON_SERVER = CREATED_ON_SERVER;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getServerId() {
        return server_id;
    }

    public void setServerId(int ServerId) {
        server_id = ServerId;
    }

    public ArrayList<EmployeeSeparationModel> getErmList() {
        return ermList;
    }

    public void setErmList(ArrayList<EmployeeSeparationModel> ermList) {
        this.ermList = ermList;
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

    public String getCancelledOn() {
        return CancelledOn;
    }

    public void setCancelledOn(String cancelledOn) {
        CancelledOn = cancelledOn;
    }

    public int getCancelledBy() {
        return CancelledBy;
    }

    public void setCancelledBy(int cancelledBy) {
        CancelledBy = cancelledBy;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getSep_status() {
        return sep_status;
    }

    public void setSep_status(int sep_status) {
        this.sep_status = sep_status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public int getLwop() {
        return lwop;
    }

    public void setLwop(int lwop) {
        this.lwop = lwop;
    }

    public int getSubReasonID() {
        return SubReasonID;
    }

    public void setSubReasonID(int subReasonID) {
        SubReasonID = subReasonID;
    }

    public int getReasonID() {
        return ReasonID;
    }

    public void setReasonID(int reasonID) {
        ReasonID = reasonID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSubReasonText() {
        return subReasonText;
    }

    public void setSubReasonText(String subReasonText) {
        this.subReasonText = subReasonText;
    }
}
