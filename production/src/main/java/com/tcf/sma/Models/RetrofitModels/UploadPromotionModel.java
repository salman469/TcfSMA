package com.tcf.sma.Models.RetrofitModels;

import com.tcf.sma.Models.PromotionStudentDBModel;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/21/2017.
 */

public class UploadPromotionModel {

    private int id;
    private int schoolclass_id;
    private int created_by;
    private String created_on;
    private String device_udid;

    private ArrayList<PromotionStudentDBModel> Promotion_Student;

    public UploadPromotionModel() {
    }

    public UploadPromotionModel(int id, int schoolclass_id, int created_by, String created_on, String device_udid, ArrayList<PromotionStudentDBModel> promotion_Student) {
        this.id = id;
        this.schoolclass_id = schoolclass_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.device_udid = device_udid;
        Promotion_Student = promotion_Student;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolclass_id() {
        return schoolclass_id;
    }

    public void setSchoolclass_id(int schoolclass_id) {
        this.schoolclass_id = schoolclass_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    //Setters

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getDevice_udid() {
        return device_udid;
    }

    public void setDevice_udid(String device_udid) {
        this.device_udid = device_udid;
    }

    public ArrayList<PromotionStudentDBModel> getPromotion_Student() {
        return Promotion_Student;
    }

    public void setPromotion_Student(ArrayList<PromotionStudentDBModel> promotion_Student) {
        Promotion_Student = promotion_Student;
    }
}
