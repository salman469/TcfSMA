package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 3/7/2017.
 */

public class AttendanceStudentUploadModel {

    int student_id;
    boolean is_absent;
    String reason;

    public AttendanceStudentUploadModel() {
    }

    public AttendanceStudentUploadModel(int student_id, boolean is_absent, String reason) {
        this.student_id = student_id;
        this.is_absent = is_absent;
        this.reason = reason;
    }

    //Getters
    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public boolean is_absent() {
        return is_absent;
    }

    //Setters

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setIs_absent(boolean is_absent) {
        this.is_absent = is_absent;
    }
}
