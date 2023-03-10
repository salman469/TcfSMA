package com.tcf.sma.Models;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class PromotionModel {
    private static PromotionModel instance;
    public ArrayList<PromotionModel> pmList;
    public PromotionModel pModel;
    String class_name, gr_no, student_name, student_status, isPromoted;

    public static PromotionModel getInstance() {
        return (instance == null) ? instance = new PromotionModel() : instance;
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

    public String getStudent_status() {
        return student_status;
    }

    public void setStudent_status(String student_status) {
        this.student_status = student_status;
    }

    public String getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(String isPromoted) {
        this.isPromoted = isPromoted;
    }


    public ArrayList<PromotionModel> getPmList() {
        return pmList;
    }

    public PromotionModel getpModel() {
        return pModel;
    }

    public void setAmList(ArrayList<PromotionModel> pmList) {
        this.pmList = pmList;
    }
}
