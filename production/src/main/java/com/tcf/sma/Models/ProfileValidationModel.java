package com.tcf.sma.Models;

/**
 * Created by Zubair Soomro on 2/6/2017.
 */

public class ProfileValidationModel {
    String class_name;
    String total;
    String unapprovd;
    String gr_no;
    String student_name;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUnapprovd() {
        return unapprovd;
    }

    public void setUnapprovd(String unapprovd) {
        this.unapprovd = unapprovd;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGr_no() {
        return gr_no;
    }

    public void setGr_no(String gr_no) {
        this.gr_no = gr_no;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
