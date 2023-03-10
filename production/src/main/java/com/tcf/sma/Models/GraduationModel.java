package com.tcf.sma.Models;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class GraduationModel {
    public static ArrayList<GraduationModel> gmList;
    public static GraduationModel gModel;
    private static GraduationModel instance;
    String class_name, gr_no, student_name, student_status, isGraduated;

    public static GraduationModel getInstance() {
        return (instance == null) ? instance = new GraduationModel() : instance;
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

    public String getIsGraduated() {
        return isGraduated;
    }

    public void setIsGraduated(String isGraduated) {
        this.isGraduated = isGraduated;
    }


    public ArrayList<GraduationModel> getGmList() {
        return gmList;
    }

    public GraduationModel getgModel() {
        return gModel;
    }

    public void setAmList(ArrayList<GraduationModel> gmList) {
        this.gmList = gmList;
    }
}
