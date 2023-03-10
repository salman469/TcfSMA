package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class AttendanceModel {
    public static ArrayList<AttendanceModel> amList;
    public static AttendanceModel aModel;
    private static AttendanceModel instance;
    @SerializedName("id")
    private int id;
    @SerializedName("server_id")
    private int server_id;
    @SerializedName("device_id")
    private int device_id;
    @SerializedName("created_by")
    private int createdBy;
    @SerializedName("schoolclass_id")
    private int schoolId;
    @SerializedName("for_date")
    private String forDate;
    @SerializedName("created_on")
    private String createdOn;
    @SerializedName("uploaded_on")
    private String uploadedOn;
    private String Attendance;
    private String ClassSectionName;
    @SerializedName("modified_on")
    private String modified_on;
    @SerializedName("modified_by")
    private int modified_by;

    public static AttendanceModel getInstance() {
        return (instance == null) ? instance = new AttendanceModel() : instance;
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    //Methods
    public ArrayList<AttendanceModel> getAmList() {
        return amList;
    }

    public void setAmList(ArrayList<AttendanceModel> amList) {
        this.amList = amList;
    }

    public AttendanceModel getaModel() {
        return aModel;
    }

    public void setaModel(AttendanceModel aModel) {
        this.aModel = aModel;
    }

    public String getClassSectionName() {
        return ClassSectionName;
    }

    public void setClassSectionName(String classSectionName) {
        ClassSectionName = classSectionName;
    }

    //Parsing Methods
    public ArrayList<AttendanceModel> parseArray(String json) {
        amList = null;
        aModel = null;

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("attendance");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    aModel.setId(childObject.getInt("id"));
                if (childObject.has("for_date"))
                    aModel.setForDate(childObject.getString("for_date"));
                if (childObject.has("created_by"))
                    aModel.setCreatedBy(childObject.getInt("created_by"));
                if (childObject.has("created_on"))
                    aModel.setCreatedOn(childObject.getString("created_on"));
                if (childObject.has("uploaded_on"))
                    aModel.setUploadedOn(childObject.getString("uploaded_on"));
                if (childObject.has("school_id"))
                    aModel.setSchoolId(childObject.getInt("school_id"));
                amList.add(aModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return amList;
    }

    public AttendanceModel parseObject(String json) {
        aModel = null;
        try {
            JSONObject childObject = new JSONObject(json);
            if (childObject.has("id"))
                aModel.setId(childObject.getInt("id"));
            if (childObject.has("for_date"))
                aModel.setForDate(childObject.getString("for_date"));
            if (childObject.has("created_by"))
                aModel.setCreatedBy(childObject.getInt("created_by"));
            if (childObject.has("created_on"))
                aModel.setCreatedOn(childObject.getString("created_on"));
            if (childObject.has("uploaded_on"))
                aModel.setUploadedOn(childObject.getString("uploaded_on"));
            if (childObject.has("school_id"))
                aModel.setSchoolId(childObject.getInt("school_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aModel;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }
}
