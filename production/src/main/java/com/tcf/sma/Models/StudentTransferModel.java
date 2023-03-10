package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StudentTransferModel implements Serializable {
    @SerializedName("student_id")
    private int std_Id;
    @SerializedName("name")
    private String std_Name;
    @SerializedName("gr_no")
    private String GrNo;
    @SerializedName("date_of_birth")
    private String Dob;
    @SerializedName("father_name")
    private String FathersName;
    @SerializedName("father_nic_no")
    private String FatherNic;
    @SerializedName("picture_filename")
    private String PictureName;
    @SerializedName("Actual_Fee")
    private int actualFees;
    //is_deleted => NOT is_active
    @SerializedName("is_deleted")
    private Boolean IsActive;
    @SerializedName("is_withdrawl")
    private boolean IsWithdrawal;
    @SerializedName("school_id")
    private int schoolId;
    private String school_name;
    @SerializedName("schoolclass_id")
    private int SchoolClassId;
    @SerializedName("previous_school_name")
    private String PreviousSchoolName;
    @SerializedName("previous_class")
    private String PreviousSchoolClass;
    private String AllowedModule_App;

    public int getStd_Id() {
        return std_Id;
    }

    public void setStd_Id(int std_Id) {
        this.std_Id = std_Id;
    }

    public String getStd_Name() {
        return std_Name;
    }

    public void setStd_Name(String std_Name) {
        this.std_Name = std_Name;
    }

    public String getGrNo() {
        return GrNo;
    }

    public void setGrNo(String grNo) {
        GrNo = grNo;
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

    public String getPictureName() {
        return PictureName;
    }

    public void setPictureName(String pictureName) {
        PictureName = pictureName;
    }

    public int getActualFees() {
        return actualFees;
    }

    public void setActualFees(int actualFees) {
        this.actualFees = actualFees;
    }

    public Boolean isActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public boolean isWithdrawal() {
        return IsWithdrawal;
    }

    public void setWithdrawal(boolean withdrawal) {
        IsWithdrawal = withdrawal;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public int getSchoolClassId() {
        return SchoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        SchoolClassId = schoolClassId;
    }

    public String getPreviousSchoolName() {
        return PreviousSchoolName;
    }

    public void setPreviousSchoolName(String previousSchoolName) {
        PreviousSchoolName = previousSchoolName;
    }

    public String getPreviousSchoolClass() {
        return PreviousSchoolClass;
    }

    public void setPreviousSchoolClass(String previousSchoolClass) {
        PreviousSchoolClass = previousSchoolClass;
    }

    public String getAllowedModule_App() {
        return AllowedModule_App;
    }

    public void setAllowedModule_App(String allowedModule_App) {
        AllowedModule_App = allowedModule_App;
    }
}
