package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 2/10/2017.
 */

public class PromotionStudentDBModel {
    @SerializedName("promotion_id")
    private long promotion_id;
    @SerializedName("student_id")
    private long student_id;
    @SerializedName("new_schoolclass_id")
    private int new_schoolclass_id;
    @SerializedName("new_session")
    private int new_session;

    private ArrayList<PromotionStudentDBModel> psmList;
    private PromotionStudentDBModel psModel;

    public PromotionStudentDBModel() {
    }

    public PromotionStudentDBModel(long promotion_id, long student_id, int new_schoolclass_id) {
        this.promotion_id = promotion_id;
        this.student_id = student_id;
        this.new_schoolclass_id = new_schoolclass_id;

    }

    public long getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(long promotion_id) {
        this.promotion_id = promotion_id;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public int getNew_schoolclass_id() {
        return new_schoolclass_id;
    }

    public void setNew_schoolclass_id(int new_schoolclass_id) {
        this.new_schoolclass_id = new_schoolclass_id;
    }

    public int getNew_session() {
        return new_session;
    }

    public void setNew_session(int new_session) {
        this.new_session = new_session;
    }

    public ArrayList<PromotionStudentDBModel> getPsmList() {
        return psmList;
    }

    public void setPsmList(ArrayList<PromotionStudentDBModel> psmList) {
        this.psmList = psmList;
    }

    public PromotionStudentDBModel getPsModel() {
        return psModel;
    }

    public void setPsModel(PromotionStudentDBModel psModel) {
        this.psModel = psModel;
    }

}
