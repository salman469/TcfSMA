package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class EmployeeTeacherAttendanceTypeModel {

    private int id;

    @SerializedName("Type")
    private String Attendance_Type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttendance_Type() {
        return Attendance_Type;
    }

    public void setAttendance_Type(String attendance_Type) {
        Attendance_Type = attendance_Type;
    }
}
