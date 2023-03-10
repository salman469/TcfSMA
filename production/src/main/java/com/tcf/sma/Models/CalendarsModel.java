package com.tcf.sma.Models;

public class CalendarsModel {
    public int calendar_id;
    public int Session_Id;
    public int C_Activity_Id;
    public String C_Activity_Name;
    public String Activity_Start_Date;
    public String Activity_End_Date;
    public String C_Holiday_Type_Name;
    public int C_Holiday_Type_Id;
    public int schoolId;
    public int totalDays;
    public int totalOffDays;
    private int modifiedBy;
    private String modifiedOn;
    private boolean isActive;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
//  Getters


    public int getSession_Id() {
        return Session_Id;
    }

    public void setSession_Id(int session_Id) {
        Session_Id = session_Id;
    }

    public int getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(int calendar_id) {
        this.calendar_id = calendar_id;
    }

    public int getC_Activity_Id() {
        return C_Activity_Id;
    }

    public void setC_Activity_Id(int c_Activity_Id) {
        C_Activity_Id = c_Activity_Id;
    }

    public String getC_Activity_Name() {
        return C_Activity_Name;
    }

    public void setC_Activity_Name(String c_Activity_Name) {
        C_Activity_Name = c_Activity_Name;
    }

    public String getActivity_Start_Date() {
        return Activity_Start_Date;
    }

    public void setActivity_Start_Date(String activity_Start_Date) {
        Activity_Start_Date = activity_Start_Date;
    }

    //Setters

    public String getActivity_End_Date() {
        return Activity_End_Date;
    }

    public void setActivity_End_Date(String activity_End_Date) {
        Activity_End_Date = activity_End_Date;
    }

    public String getC_Holiday_Type_Name() {
        return C_Holiday_Type_Name;
    }

    public void setC_Holiday_Type_Name(String c_Holiday_Type_Name) {
        C_Holiday_Type_Name = c_Holiday_Type_Name;
    }

    public int getC_Holiday_Type_Id() {
        return C_Holiday_Type_Id;
    }

    public void setC_Holiday_Type_Id(int c_Holiday_Type_Id) {
        C_Holiday_Type_Id = c_Holiday_Type_Id;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getTotalOffDays() {
        return totalOffDays;
    }

    public void setTotalOffDays(int totalOffDays) {
        this.totalOffDays = totalOffDays;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
