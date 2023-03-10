package com.tcf.sma.Models.RetrofitModels.HRTCT;

import java.util.List;

public class UploadTCTEmployeeSubTagModel {
    public int ID;
    private int Subject1_Id;
    private int Subject2_Id;
    private int Reason_Id;
    private int Modified_By;
    private int desID;
    private int leaveTypeID;
    private int regStatusID;
    private String CNIC;
    private String Comments;
    private List<UploadTCTEmployeeSubTagModel> tctEmployeeSubTagModelList;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSubject1_Id() {
        return Subject1_Id;
    }

    public void setSubject1_Id(int subject1_Id) {
        Subject1_Id = subject1_Id;
    }

    public int getSubject2_Id() {
        return Subject2_Id;
    }

    public void setSubject2_Id(int subject2_Id) {
        Subject2_Id = subject2_Id;
    }

    public int getReason_Id() {
        return Reason_Id;
    }

    public void setReason_Id(int reason_Id) {
        Reason_Id = reason_Id;
    }

    public int getModified_By() {
        return Modified_By;
    }

    public void setModified_By(int modified_By) {
        Modified_By = modified_By;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public List<UploadTCTEmployeeSubTagModel> getTctEmployeeSubTagModelList() {
        return tctEmployeeSubTagModelList;
    }

    public void setTctEmployeeSubTagModelList(List<UploadTCTEmployeeSubTagModel> tctEmployeeSubTagModelList) {
        this.tctEmployeeSubTagModelList = tctEmployeeSubTagModelList;
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

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public int getDesID() {
        return desID;
    }

    public void setDesID(int desID) {
        this.desID = desID;
    }
}
