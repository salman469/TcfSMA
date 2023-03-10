package com.tcf.sma.Models.RetrofitModels.HRTCT;

import android.widget.EditText;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TCTEmpSubjectTaggingModel {
    @SerializedName("ID")
    private int id;

    @SerializedName("userId")
    private int userID;

    @SerializedName("Emp_Code")
    private String EMP_Code;

    transient private String EMP_Name;

    @SerializedName("Designation_Id")
    private int Designation_ID;

    transient private String Designation_Name;

    @SerializedName("SchoolId")
    private int SchoolID ;

    @SerializedName("TCTPhase_Id")
    private int TCTPhase_ID;

    @SerializedName("Subject1_Id")
    private int Subject1_ID;

    @SerializedName("Subject2_Id")
    private int Subject2_ID;

    @SerializedName("Reason_Id")
    private int ReasonID;

    transient private String Reason;

    private String Modified_On;

    private int Modified_By;

    @SerializedName("Comments")
    private String Comment;

    @SerializedName("IsMandatory")
    private boolean Mandatory;

    @SerializedName("IsVerified")
    private boolean isVerified;

    private String CNIC;

    @SerializedName("desID")
    private int newDesignationId;

    private int leaveTypeID;

    private int regStatusID;

    transient private List<TCTSubjectsModel> tctSubjectsModels;
    transient private String UploadedOn;
    transient private boolean isViewOnly;

    transient private EditText cnicEditText;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEMP_Code() {
        return EMP_Code;
    }

    public void setEMP_Code(String EMP_Code) {
        this.EMP_Code = EMP_Code;
    }

    public String getEMP_Name() {
        return EMP_Name;
    }

    public void setEMP_Name(String EMP_Name) {
        this.EMP_Name = EMP_Name;
    }

    public int getDesignation_ID() {
        return Designation_ID;
    }

    public void setDesignation_ID(int designation_ID) {
        Designation_ID = designation_ID;
    }

    public String getDesignation_Name() {
        return Designation_Name;
    }

    public void setDesignation_Name(String designation_Name) {
        Designation_Name = designation_Name;
    }

    public int getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(int schoolID) {
        SchoolID = schoolID;
    }

    public int getTCTPhase_ID() {
        return TCTPhase_ID;
    }

    public void setTCTPhase_ID(int TCTPhase_ID) {
        this.TCTPhase_ID = TCTPhase_ID;
    }

    public int getSubject1_ID() {
        return Subject1_ID;
    }

    public void setSubject1_ID(int subject1_ID) {
        Subject1_ID = subject1_ID;
    }

    public int getSubject2_ID() {
        return Subject2_ID;
    }

    public void setSubject2_ID(int subject2_ID) {
        Subject2_ID = subject2_ID;
    }

    public int getReasonID() {
        return ReasonID;
    }

    public void setReasonID(int reasonID) {
        ReasonID = reasonID;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getModified_On() {
        return Modified_On;
    }

    public void setModified_On(String modified_On) {
        Modified_On = modified_On;
    }

    public int getModified_By() {
        return Modified_By;
    }

    public void setModified_By(int modified_By) {
        Modified_By = modified_By;
    }

    public List<TCTSubjectsModel> getTctSubjectsModels() {
        return tctSubjectsModels;
    }

    public void setTctSubjectsModels(List<TCTSubjectsModel> tctSubjectsModels) {
        this.tctSubjectsModels = tctSubjectsModels;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public boolean isMandatory() {
        return Mandatory;
    }

    public void setMandatory(boolean mandatory) {
        Mandatory = mandatory;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isViewOnly() {
        return isViewOnly;
    }

    public void setViewOnly(boolean viewOnly) {
        isViewOnly = viewOnly;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public int getNewDesignationId() {
        return newDesignationId;
    }

    public void setNewDesignationId(int newDesignationId) {
        this.newDesignationId = newDesignationId;
    }

    public int getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(int leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public int getRegStatusID() {
        return regStatusID;
    }

    public void setRegStatusID(int regStatusID) {
        this.regStatusID = regStatusID;
    }

    public EditText getCnicEditText() {
        return cnicEditText;
    }

    public void setCnicEditText(EditText cnicEditText) {
        this.cnicEditText = cnicEditText;
    }
}
