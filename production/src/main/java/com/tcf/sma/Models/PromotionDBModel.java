package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 2/10/2017.
 */

public class PromotionDBModel {
    public static ArrayList<PromotionDBModel> pmList;
    public static PromotionDBModel pModel;
    @SerializedName("id")
    private int Id;
    @SerializedName("created_by")
    private int created_by;
    @SerializedName("schoolclass_id")
    private int school_class_Id;
    @SerializedName("created_on")
    private String created_on;
    @SerializedName("uploaded_on")
    private String uploaded_on;
    @SerializedName("received_on")
    private String received_on;
    @SerializedName("device_udid")
    private String device_udid;
    @SerializedName("device_id")
    private int device_id;
    @SerializedName("modified_on")
    private String modified_on;

    public PromotionDBModel() {
    }

    public PromotionDBModel(int created_by, String created_on, String uploaded_on) {
        this.created_by = created_by;
        this.created_on = created_on;
        this.uploaded_on = uploaded_on;
    }

    public static PromotionDBModel getpModel() {
        return pModel;
    }

    public void setpModel(PromotionDBModel pModel) {
        PromotionDBModel.pModel = pModel;
    }

    //Getters
    public int getCreated_by() {
        return created_by;
    }

    //Setters
    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSchoolClassId() {
        return school_class_Id;
    }

    public void setSchoolClassId(int school_class_Id) {
        this.school_class_Id = school_class_Id;
    }

    public String getReceived_on() {
        return received_on;
    }

    public void setReceived_on(String received_on) {
        this.received_on = received_on;
    }

    public String getDevice_udid() {
        return device_udid;
    }

    public void setDevice_udid(String device_udid) {
        this.device_udid = device_udid;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public ArrayList<PromotionDBModel> getPmList() {
        return pmList;
    }

    public void setPmList(ArrayList<PromotionDBModel> pmList) {
        PromotionDBModel.pmList = pmList;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }
}
