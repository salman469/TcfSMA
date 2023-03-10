package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 3/6/2017.
 */

public class EnrollmentImageRequestModel {
    int enrollment_id; // (id of enrollment_student child table record)
    String filetype;


    //Constructor
    public EnrollmentImageRequestModel() {
    }

    public EnrollmentImageRequestModel(int enrollment_id, String filetype) {
        this.enrollment_id = enrollment_id;
        this.filetype = filetype;
    }

    //Getters

    public int getEnrollment_id() {
        return enrollment_id;
    }

    public void setEnrollment_id(int enrollment_id) {
        this.enrollment_id = enrollment_id;
    }

    //Setters

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
}
