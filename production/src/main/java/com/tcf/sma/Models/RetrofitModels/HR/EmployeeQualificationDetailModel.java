package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeeQualificationDetailModel {


    private int id;

    @SerializedName("userID")
    private int Employee_Personal_Detail_ID;

    @SerializedName("schoolId")
    private int SchoolID;

    @SerializedName("instituteName")
    private String Institute_Name;

    @SerializedName("degree")
    private String Degree_Name;

    @SerializedName("subject")
    private String Subject_Name;

    @SerializedName("passingYear")
    private String Passing_Year;

    @SerializedName("grade")
    private String Grade_Division;

    @SerializedName("qualificationType")
    private String Qualification_Type;

    @SerializedName("qualificationLevel")
    private String Qualification_Level;

    @SerializedName("dateOfJoining")
    private String DateOfJoining;

    @SerializedName("EmployeeCode")
    private String Emp_Code;

    private String modifiedOn;

//    public int getEmployee_Qualification_Detail_ID() {
//    private int EmployeeQualificationType_ID;
//    private int EmployeeQualificationLevel_ID;

//        return Employee_Qualification_Detail_ID;
//    }
//
//    public void setEmployee_Qualification_Detail_ID(int employee_Qualification_Detail_ID) {
//        Employee_Qualification_Detail_ID = employee_Qualification_Detail_ID;
//    }


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

    public String getInstitute_Name() {
        return Institute_Name;
    }

    public void setInstitute_Name(String institute_Name) {
        Institute_Name = institute_Name;
    }

    public String getDegree_Name() {
        return Degree_Name;
    }

    public void setDegree_Name(String degree_Name) {
        Degree_Name = degree_Name;
    }

    public String getSubject_Name() {
        return Subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        Subject_Name = subject_Name;
    }

    public String getPassing_Year() {
        return Passing_Year;
    }

    public void setPassing_Year(String passing_Year) {
        Passing_Year = passing_Year;
    }

    public String getGrade_Division() {
        return Grade_Division;
    }

    public void setGrade_Division(String grade_Division) {
        Grade_Division = grade_Division;
    }

    public String getQualification_Type() {
        return Qualification_Type;
    }

    public void setQualification_Type(String qualification_Type) {
        Qualification_Type = qualification_Type;
    }

    public String getQualification_Level() {
        return Qualification_Level;
    }

    public void setQualification_Level(String qualification_Level) {
        Qualification_Level = qualification_Level;
    }

    public int getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(int schoolID) {
        SchoolID = schoolID;
    }

    public String getDateOfJoining() {
        return DateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        DateOfJoining = dateOfJoining;
    }

    public String getEmp_Code() {
        return Emp_Code;
    }

    public void setEmp_Code(String emp_Code) {
        Emp_Code = emp_Code;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
