package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;

public class StudentTransferUploadModel {

    @SerializedName("student_id")
    private int student_Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("date_of_birth")
    private String Dob;
    @SerializedName("father_name")
    private String FathersName;
    @SerializedName("father_nic_no")
    private String FatherNic;
    @SerializedName("gr_no")
    private String GrNo;
    @SerializedName("school_id")
    private int schoolId;
    @SerializedName("schoolclass_id")
    private int SchoolClassId;
    @SerializedName("Actual_Fee")
    private int actualFees;
    @SerializedName("date_of_admission")
    private String EnrollmentDate;
    @SerializedName("is_withdrawl")
    private boolean IsWithdrawal;
    @SerializedName("withdrawal_reason_id")
    private int WithdrawalReasonId;
    @SerializedName("modified_by")
    private int ModifiedBy;
    @SerializedName("modified_on")
    private String ModifiedOn;
    private String review_status;
    private String deviceId;

    public int getStudent_Id() {
        return student_Id;
    }

    public void setStudent_Id(int student_Id) {
        this.student_Id = student_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String fathersName) {
        FathersName = fathersName;
    }

    public String getFatherNic() {
        return FatherNic;
    }

    public void setFatherNic(String fatherNic) {
        FatherNic = fatherNic;
    }

    public String getGrNo() {
        return GrNo;
    }

    public void setGrNo(String grNo) {
        GrNo = grNo;
    }

    public int getSchoolClassId() {
        return SchoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        SchoolClassId = schoolClassId;
    }

    public int getActualFees() {
        return actualFees;
    }

    public void setActualFees(int actualFees) {
        this.actualFees = actualFees;
    }

    public String getEnrollmentDate() {
        return EnrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        EnrollmentDate = enrollmentDate;
    }

    public boolean isWithdrawal() {
        return IsWithdrawal;
    }

    public void setWithdrawal(boolean withdrawal) {
        IsWithdrawal = withdrawal;
    }

    public int getWithdrawalReasonId() {
        return WithdrawalReasonId;
    }

    public void setWithdrawalReasonId(int withdrawalReasonId) {
        WithdrawalReasonId = withdrawalReasonId;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
