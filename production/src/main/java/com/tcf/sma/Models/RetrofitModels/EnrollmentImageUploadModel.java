package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 3/7/2017.
 */

public class EnrollmentImageUploadModel {
    private String filename;
    private String filetype;

    public EnrollmentImageUploadModel() {
    }

    public EnrollmentImageUploadModel(String filename, String filetype) {
        this.filename = filename;
        this.filetype = filetype;
    }

    //Getters
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    //Setters

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
}
