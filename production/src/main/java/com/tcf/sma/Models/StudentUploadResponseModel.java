package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

public class StudentUploadResponseModel {
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("ErrorMessage")
    private String errorMessage;
    @SerializedName("Student")
    private StudentModel student;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }
}
