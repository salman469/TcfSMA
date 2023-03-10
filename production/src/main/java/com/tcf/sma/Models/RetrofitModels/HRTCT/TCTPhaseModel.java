package com.tcf.sma.Models.RetrofitModels.HRTCT;
import com.google.gson.annotations.SerializedName;
public class TCTPhaseModel {
    @SerializedName("ID")
    private int id;
    @SerializedName("tct_phase")
    private String TCT_Phase;
    private String Start_Date;
    @SerializedName("End_Date")
    private String End_date;
    @SerializedName("Activity_Id")
    private int ActivityID;
    private boolean Mandatory;
    @SerializedName("AcademicSession_Id")
    private int AcademicSession_id;
    @SerializedName("TCT_Start_date")
    private String tctStartDate;
    @SerializedName("TCT_end_date")
    private String tctEndDate;
    @SerializedName("post_registration_start_date")
    private String tctPostRegStartDate;
    @SerializedName("post_registration_end_date")
    private String tctPostRegEndDate;
    @SerializedName("TestDate")
    private String tctTestDate;
    private String modifiedOn;
    public TCTPhaseModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTCT_Phase() {
        return TCT_Phase;
    }

    public void setTCT_Phase(String TCT_Phase) {
        this.TCT_Phase = TCT_Phase;
    }

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getEnd_date() {
        return End_date;
    }

    public void setEnd_date(String end_date) {
        End_date = end_date;
    }

    public int getActivityID() {
        return ActivityID;
    }

    public void setActivityID(int activityID) {
        ActivityID = activityID;
    }

    public boolean getMandatory() {
        return Mandatory;
    }

    public void setMandatory(boolean mandatory) {
        Mandatory = mandatory;
    }

    public int getAcademicSession_id() {
        return AcademicSession_id;
    }

    public void setAcademicSession_id(int academicSession_id) {
        AcademicSession_id = academicSession_id;
    }

    public boolean isMandatory() {
        return Mandatory;
    }

    public String getTctStartDate() {
        return tctStartDate;
    }

    public void setTctStartDate(String tctStartDate) {
        this.tctStartDate = tctStartDate;
    }

    public String getTctEndDate() {
        return tctEndDate;
    }

    public void setTctEndDate(String tctEndDate) {
        this.tctEndDate = tctEndDate;
    }

    public String getTctPostRegStartDate() {
        return tctPostRegStartDate;
    }

    public void setTctPostRegStartDate(String tctPostRegStartDate) {
        this.tctPostRegStartDate = tctPostRegStartDate;
    }

    public String getTctPostRegEndDate() {
        return tctPostRegEndDate;
    }

    public void setTctPostRegEndDate(String tctPostRegEndDate) {
        this.tctPostRegEndDate = tctPostRegEndDate;
    }

    public String getTctTestDate() {
        return tctTestDate;
    }

    public void setTctTestDate(String tctTestDate) {
        this.tctTestDate = tctTestDate;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    //to display object as a string in spinner
//    public String toString() {
//        return getSubject();
//    }
}