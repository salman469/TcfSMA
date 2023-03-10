package com.tcf.sma.Models;

import java.util.ArrayList;

public class StudentImageModel {

    private long student_id;
    private String filename;
    private String uploaded_on;
    private String review_status;
    private String filetype;

    private ArrayList<StudentImageModel> simList;
    private StudentImageModel sim;

    public StudentImageModel() {
    }

    public StudentImageModel(long student_id, String filename, String uploaded_on, String review_status, String filetype) {
        this.student_id = student_id;
        this.filename = filename;
        this.uploaded_on = uploaded_on;
        this.review_status = review_status;
        this.filetype = filetype;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    //Methods

    public ArrayList<StudentImageModel> getSimList() {
        return simList;
    }

    public void setSimList(ArrayList<StudentImageModel> simList) {
        this.simList = simList;
    }

    public StudentImageModel getSim() {
        return sim;
    }

    public void setSim(StudentImageModel sim) {
        this.sim = sim;
    }
}
