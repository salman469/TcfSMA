package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/6/2017.
 */

public class WithdrawalModel {
    @SerializedName("id")
    private int id;
    @SerializedName("student_id")
    private int student_id;
    @SerializedName("reason_id")
    private int reason_id;
    @SerializedName("created_by")
    private int created_by;
    @SerializedName("school_id")
    private int school_id;
    @SerializedName("created_on")
    private String created_on;
    @SerializedName("uploaded_on")
    private String uploaded_on;
    private int device_id;
    private String device_udid;
    @SerializedName("modified_on")
    private String modified_on;


    private ArrayList<WithdrawalModel> wmList;
    private WithdrawalModel wm;

    //Constructors


    public WithdrawalModel() {
    }

    public WithdrawalModel(int id, int student_id, int reason_id, int created_by, String created_on, String uploaded_on) {
        this.id = id;
        this.student_id = student_id;
        this.reason_id = reason_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.uploaded_on = uploaded_on;
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public int getCreated_by() {
        return created_by;
    }

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

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_udid() {
        return device_udid;
    }

    public void setDevice_udid(String device_udid) {
        this.device_udid = device_udid;
    }

    //Methods
    public ArrayList<WithdrawalModel> getWmList() {
        return wmList;
    }

    public void setWmList(ArrayList<WithdrawalModel> wmList) {
        this.wmList = wmList;
    }

    public WithdrawalModel getWm() {
        return wm;
    }

    public void setWm(WithdrawalModel wm) {
        this.wm = wm;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    //Parsing Methods
    public ArrayList<WithdrawalModel> parseArray(String json) {
        wmList = new ArrayList<>();
        wm = new WithdrawalModel();

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("withdrawal");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    wm.setId(childObject.getInt("id"));
                if (childObject.has("student_id"))
                    wm.setStudent_id(childObject.getInt("student_id"));
                if (childObject.has("reason_id"))
                    wm.setReason_id(childObject.getInt("reason_id"));
                if (childObject.has("created_by"))
                    wm.setCreated_by(childObject.getInt("created_by"));
                if (childObject.has("created_on"))
                    wm.setCreated_on(childObject.getString("created_on"));
                if (childObject.has("uploaded_on"))
                    wm.setUploaded_on(childObject.getString("uploaded_on"));
                if (childObject.has("school_id"))
                    wm.setSchool_id(childObject.getInt("school_id"));
                if (childObject.has("modified_on"))
                    wm.setModified_on(childObject.getString("modified_on"));
                wmList.add(wm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wmList;
    }

    public WithdrawalModel parseObject(String json) {
        wm = new WithdrawalModel();
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("id"))
                wm.setId(childObject.getInt("id"));
            if (childObject.has("student_id"))
                wm.setStudent_id(childObject.getInt("student_id"));
            if (childObject.has("reason_id"))
                wm.setReason_id(childObject.getInt("reason_id"));
            if (childObject.has("created_by"))
                wm.setCreated_by(childObject.getInt("created_by"));
            if (childObject.has("created_on"))
                wm.setCreated_on(childObject.getString("created_on"));
            if (childObject.has("uploaded_on"))
                wm.setUploaded_on(childObject.getString("uploaded_on"));
            if (childObject.has("school_id"))
                wm.setSchool_id(childObject.getInt("school_id"));
            if (childObject.has("modified_on"))
                wm.setModified_on(childObject.getString("modified_on"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wm;
    }
}
