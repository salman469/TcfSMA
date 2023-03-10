package com.tcf.sma.Models.RetrofitModels.HR;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeModel implements Parcelable {
    @SerializedName("userId")
    private int id;

    @SerializedName("EmpId")
    private int Employee_ID;

    private String Employee_Code;

    @SerializedName("firstname")
    private String First_Name;

    @SerializedName("lastname")
    private String Last_Name;

    @SerializedName("email")
    private String Email;

    @SerializedName("mobileNo")
    private String Mobile_No;

    @SerializedName("designation")
    private String Designation;

    @SerializedName("gender")
    private String Gender;

    @SerializedName("father_name")
    private String Father_Name;

    @SerializedName("mother_maiden_name")
    private String Mother_Name;

    //    @SerializedName("cnic")
    @SerializedName("nic_number")
    private String NIC_No;

    @SerializedName("CADRE")
    private String CADRE;

    @SerializedName("IsActive")
    private boolean Is_Active;

    @SerializedName("joining_date")
    private String Date_Of_Joining;

    @SerializedName("JobStatus")
    private String Job_Status;

    private String ModifiedBy;

    private String ModifiedOn;

    @SerializedName("lastWorkingDay")
    private String LastWorkingDay;

    private String ImagePath;

    transient private String UploadedOn;
    transient private int AttendanceType_id;

    transient private String Division_Name;
    transient private String DOB;
    transient private ArrayList<EmployeeModel> employeesList;
    transient private EmployeeLeaveModel employeeLeaveModel;
    transient private String empCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(int employee_ID) {
        Employee_ID = employee_ID;
    }

    public String getEmployee_Code() {
        return Employee_Code;
    }

    public void setEmployee_Code(String employee_Code) {
        Employee_Code = employee_Code;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    public String getFather_Name() {
        return Father_Name;
    }

    public void setFather_Name(String father_Name) {
        Father_Name = father_Name;
    }

    public String getMother_Name() {
        return Mother_Name;
    }

    public void setMother_Name(String mother_Name) {
        Mother_Name = mother_Name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getNIC_No() {
        return NIC_No;
    }

    public void setNIC_No(String NIC_No) {
        this.NIC_No = NIC_No;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getCADRE() {
        return CADRE;
    }

    public void setCADRE(String CADRE) {
        this.CADRE = CADRE;
    }

//    public boolean getIs_Active_Directory() {
//        return Is_Active_Directory;
//    }
//
//    public void setIs_Active_Directory(boolean is_Active_Directory) {
//        Is_Active_Directory = is_Active_Directory;
//    }


    public boolean getIs_Active() {
        return Is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        Is_Active = is_Active;
    }

    public String getDate_Of_Joining() {
        return Date_Of_Joining;
    }

    public void setDate_Of_Joining(String date_Of_Joining) {
        Date_Of_Joining = date_Of_Joining;
    }

    public String getLastWorkingDay() {
        return LastWorkingDay;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        LastWorkingDay = lastWorkingDay;
    }

    public int getAttendanceType_id() {
        return AttendanceType_id;
    }

    public void setAttendanceType_id(int attendanceType_id) {
        AttendanceType_id = attendanceType_id;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public ArrayList<EmployeeModel> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(ArrayList<EmployeeModel> employeesList) {
        this.employeesList = employeesList;
    }

    public EmployeeLeaveModel getEmployeeLeaveModel() {
        return employeeLeaveModel;
    }

    public void setEmployeeLeaveModel(EmployeeLeaveModel employeeLeaveModel) {
        this.employeeLeaveModel = employeeLeaveModel;
    }

    public String getEmpCount() {
        return empCount;
    }

    public void setEmpCount(String empCount) {
        this.empCount = empCount;
    }

    public String getJob_Status() {
        return Job_Status;
    }

    public void setJob_Status(String job_Status) {
        Job_Status = job_Status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.Employee_ID);
        dest.writeString(this.Employee_Code);
        dest.writeString(this.First_Name);
        dest.writeString(this.Last_Name);
        dest.writeString(this.Email);
        dest.writeString(this.Mobile_No);
        dest.writeString(this.Designation);
        dest.writeString(this.Gender);
        dest.writeString(this.Father_Name);
        dest.writeString(this.Mother_Name);
        dest.writeString(this.NIC_No);
        dest.writeString(this.CADRE);
        dest.writeByte(this.Is_Active ? (byte) 1 : (byte) 0);
        dest.writeString(this.Date_Of_Joining);
        dest.writeString(this.ModifiedBy);
        dest.writeString(this.ModifiedOn);
        dest.writeString(this.LastWorkingDay);
    }

    public EmployeeModel() {
    }

    public EmployeeModel(int id, String employee_Code, String first_Name, String last_Name) {
        this.id = id;
        Employee_Code = employee_Code;
        First_Name = first_Name;
        Last_Name = last_Name;
    }

    protected EmployeeModel(Parcel in) {
        this.id = in.readInt();
        this.Employee_ID = in.readInt();
        this.Employee_Code = in.readString();
        this.First_Name = in.readString();
        this.Last_Name = in.readString();
        this.Email = in.readString();
        this.Mobile_No = in.readString();
        this.Designation = in.readString();
        this.Gender = in.readString();
        this.Father_Name = in.readString();
        this.Mother_Name = in.readString();
        this.NIC_No = in.readString();
        this.CADRE = in.readString();
        this.Is_Active = in.readByte() != 0;
        this.Date_Of_Joining = in.readString();
        this.ModifiedBy = in.readString();
        this.ModifiedOn = in.readString();
        this.LastWorkingDay = in.readString();
    }

    public static final Parcelable.Creator<EmployeeModel> CREATOR = new Parcelable.Creator<EmployeeModel>() {
        @Override
        public EmployeeModel createFromParcel(Parcel source) {
            return new EmployeeModel(source);
        }

        @Override
        public EmployeeModel[] newArray(int size) {
            return new EmployeeModel[size];
        }
    };

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    @Override
    public String toString() {
        return First_Name + " " + Last_Name + (Employee_Code != null && !Employee_Code.isEmpty() ? " - " + Employee_Code : "");
    }
}
