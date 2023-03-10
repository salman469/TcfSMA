package com.tcf.sma.Models;

public class ErrorResponseModel {
    private int studentId;
    private int code;
    private String name;
    private String schoolClass_id;
    private String gr_no;
    private String activityOfError;
    private String message;
    private String created_on;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolClass_id() {
        return schoolClass_id;
    }

    public void setSchoolClass_id(String schoolClass_id) {
        this.schoolClass_id = schoolClass_id;
    }

    public String getGr_no() {
        return gr_no;
    }

    public void setGr_no(String gr_no) {
        this.gr_no = gr_no;
    }

    public String getActivityOfError() {
        return activityOfError;
    }

    public void setActivityOfError(String activityOfError) {
        this.activityOfError = activityOfError;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
