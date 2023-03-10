package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Badar Arain on 2/6/2017.
 */

public class EnrollmentModel {
    @SerializedName("id")
    private int ID;
    @SerializedName("server_id")
    private int server_id;
    @SerializedName("device_id")
    private int deviceId;
    @SerializedName("gr_no")
    private int ENROLLMENT_GR_NO;
    @SerializedName("school_id")
    private int ENROLLMENT_SCHOOL_ID;
    @SerializedName("created_by")
    private int ENROLLMENT_CREATED_BY;
    @SerializedName("created_on")
    private String ENROLLMENT_CREATED_ON;
    @SerializedName("review_status")
    private String ENROLLMENT_REVIEW_STATUS;
    @SerializedName("review_comments")
    private String ENROLLMENT_REVIEW_COMMENTS;
    @SerializedName("review_completed_on")
    private String ENROLLMENT_REVIEW_COMPLETED_ON;
    @SerializedName("data_downloaded_on")
    private String ENROLLMENT_DATA_DOWNLOADED_ON;
    @SerializedName("uploaded_on")
    private String ENROLLMENT_UPLOADED_ON;
    @SerializedName("student_name")
    private String studentName;
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("schoolclass_id")
    private int class_section_id;
    @SerializedName("gender")
    private String student_gender;
    @SerializedName("monthlyfee")
    private double monthly_fee;
    @SerializedName("modified_by")
    private int modified_by;
    @SerializedName("modified_on")
    private String modified_on;
    private ArrayList<EnrollmentModel> emList;
    private EnrollmentModel em;

    public EnrollmentModel() {
    }

    public EnrollmentModel(int ENROLLMENT_GR_NO, int ENROLLMENT_SCHOOL_ID, int ENROLLMENT_CREATED_BY, String ENROLLMENT_CREATED_ON, String ENROLLMENT_REVIEW_STATUS, String ENROLLMENT_REVIEW_COMMENTS, String ENROLLMENT_REVIEW_COMPLETED_ON, String ENROLLMENT_DATA_DOWNLOADED_ON) {
        this.ENROLLMENT_GR_NO = ENROLLMENT_GR_NO;
        this.ENROLLMENT_SCHOOL_ID = ENROLLMENT_SCHOOL_ID;
        this.ENROLLMENT_CREATED_BY = ENROLLMENT_CREATED_BY;
        this.ENROLLMENT_CREATED_ON = ENROLLMENT_CREATED_ON;
        this.ENROLLMENT_REVIEW_STATUS = ENROLLMENT_REVIEW_STATUS;
        this.ENROLLMENT_REVIEW_COMMENTS = ENROLLMENT_REVIEW_COMMENTS;
        this.ENROLLMENT_REVIEW_COMPLETED_ON = ENROLLMENT_REVIEW_COMPLETED_ON;
        this.ENROLLMENT_DATA_DOWNLOADED_ON = ENROLLMENT_DATA_DOWNLOADED_ON;

    }

    public EnrollmentModel(int ENROLLMENT_GR_NO, int ENROLLMENT_SCHOOL_ID, int ENROLLMENT_CREATED_BY, String ENROLLMENT_CREATED_ON, String ENROLLMENT_REVIEW_STATUS, String ENROLLMENT_REVIEW_COMMENTS, String ENROLLMENT_REVIEW_COMPLETED_ON, String ENROLLMENT_DATA_DOWNLOADED_ON, String studentId) {
        this.ENROLLMENT_GR_NO = ENROLLMENT_GR_NO;
        this.ENROLLMENT_SCHOOL_ID = ENROLLMENT_SCHOOL_ID;
        this.ENROLLMENT_CREATED_BY = ENROLLMENT_CREATED_BY;
        this.ENROLLMENT_CREATED_ON = ENROLLMENT_CREATED_ON;
        this.ENROLLMENT_REVIEW_STATUS = ENROLLMENT_REVIEW_STATUS;
        this.ENROLLMENT_REVIEW_COMMENTS = ENROLLMENT_REVIEW_COMMENTS;
        this.ENROLLMENT_REVIEW_COMPLETED_ON = ENROLLMENT_REVIEW_COMPLETED_ON;
        this.ENROLLMENT_DATA_DOWNLOADED_ON = ENROLLMENT_DATA_DOWNLOADED_ON;
        this.studentId = studentId;

    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getMonthly_fee() {
        return monthly_fee;
    }

    public void setMonthly_fee(double monthly_fee) {
        this.monthly_fee = monthly_fee;
    }

    public String getGender() {
        return student_gender;
    }

    public void setGender(String student_gender) {
        this.student_gender = student_gender;
    }

    public int getClass_section_id() {
        return class_section_id;
    }

    public void setClass_section_id(int class_section_id) {
        this.class_section_id = class_section_id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getENROLLMENT_GR_NO() {
        return ENROLLMENT_GR_NO;
    }

    public void setENROLLMENT_GR_NO(int ENROLLMENT_GR_NO) {
        this.ENROLLMENT_GR_NO = ENROLLMENT_GR_NO;
    }

    public int getENROLLMENT_SCHOOL_ID() {
        return ENROLLMENT_SCHOOL_ID;
    }

    public void setENROLLMENT_SCHOOL_ID(int ENROLLMENT_SCHOOL_ID) {
        this.ENROLLMENT_SCHOOL_ID = ENROLLMENT_SCHOOL_ID;
    }

    public int getENROLLMENT_CREATED_BY() {
        return ENROLLMENT_CREATED_BY;
    }

    public void setENROLLMENT_CREATED_BY(int ENROLLMENT_CREATED_BY) {
        this.ENROLLMENT_CREATED_BY = ENROLLMENT_CREATED_BY;
    }

    public String getENROLLMENT_CREATED_ON() {
        return ENROLLMENT_CREATED_ON;
    }

    public void setENROLLMENT_CREATED_ON(String ENROLLMENT_CREATED_ON) {
        this.ENROLLMENT_CREATED_ON = ENROLLMENT_CREATED_ON;
    }

    public String getENROLLMENT_REVIEW_STATUS() {
        return ENROLLMENT_REVIEW_STATUS;
    }

    public void setENROLLMENT_REVIEW_STATUS(String ENROLLMENT_REVIEW_STATUS) {
        this.ENROLLMENT_REVIEW_STATUS = ENROLLMENT_REVIEW_STATUS;
    }

    public String getENROLLMENT_REVIEW_COMMENTS() {
        return ENROLLMENT_REVIEW_COMMENTS;
    }

    public void setENROLLMENT_REVIEW_COMMENTS(String ENROLLMENT_REVIEW_COMMENTS) {
        this.ENROLLMENT_REVIEW_COMMENTS = ENROLLMENT_REVIEW_COMMENTS;
    }

    public String getENROLLMENT_REVIEW_COMPLETED_ON() {
        return ENROLLMENT_REVIEW_COMPLETED_ON;
    }

    public void setENROLLMENT_REVIEW_COMPLETED_ON(String ENROLLMENT_REVIEW_COMPLETED_ON) {
        this.ENROLLMENT_REVIEW_COMPLETED_ON = ENROLLMENT_REVIEW_COMPLETED_ON;
    }

    public String getENROLLMENT_DATA_DOWNLOADED_ON() {
        return ENROLLMENT_DATA_DOWNLOADED_ON;
    }

    public void setENROLLMENT_DATA_DOWNLOADED_ON(String ENROLLMENT_DATA_DOWNLOADED_ON) {
        this.ENROLLMENT_DATA_DOWNLOADED_ON = ENROLLMENT_DATA_DOWNLOADED_ON;
    }

    public String getENROLLMENT_UPLOADED_ON() {
        return ENROLLMENT_UPLOADED_ON;
    }

    public void setENROLLMENT_UPLOADED_ON(String ENROLLMENT_UPLOADED_ON) {
        this.ENROLLMENT_UPLOADED_ON = ENROLLMENT_UPLOADED_ON;
    }

    //Methods


    public ArrayList<EnrollmentModel> getEmList() {
        return emList;
    }

    public void setEmList(ArrayList<EnrollmentModel> emList) {
        this.emList = emList;
    }

    public EnrollmentModel getEm() {
        return em;
    }

    public void setEm(EnrollmentModel em) {
        this.em = em;
    }

    //Parsing Methods
    public ArrayList<EnrollmentModel> parseArray(String json) {
        emList = new ArrayList<>();
        em = new EnrollmentModel();

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("enrollment");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    em.setID(childObject.getInt("id"));
                if (childObject.has("grno"))
                    em.setENROLLMENT_GR_NO(childObject.getInt("grno"));
                if (childObject.has("school_id"))
                    em.setENROLLMENT_SCHOOL_ID(childObject.getInt("school_id"));
                if (childObject.has("created_by"))
                    em.setENROLLMENT_CREATED_BY(childObject.getInt("created_by"));
                if (childObject.has("created_on"))
                    em.setENROLLMENT_CREATED_ON(childObject.getString("created_on"));
                if (childObject.has("review_status"))
                    em.setENROLLMENT_REVIEW_STATUS(childObject.getString("review_status"));
                if (childObject.has("review_comments"))
                    em.setENROLLMENT_REVIEW_COMMENTS(childObject.getString("review_comments"));
                if (childObject.has("review_completed_on"))
                    em.setENROLLMENT_REVIEW_COMPLETED_ON(childObject.getString("review_completed_on"));
                if (childObject.has("data_downloaded_on"))
                    em.setENROLLMENT_DATA_DOWNLOADED_ON(childObject.getString("data_downloaded_on"));
                emList.add(em);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emList;
    }

    public EnrollmentModel parseObject(String json) {
        em = new EnrollmentModel();
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("id"))
                em.setID(childObject.getInt("id"));
            if (childObject.has("grno"))
                em.setENROLLMENT_GR_NO(childObject.getInt("grno"));
            if (childObject.has("school_id"))
                em.setENROLLMENT_SCHOOL_ID(childObject.getInt("school_id"));
            if (childObject.has("created_by"))
                em.setENROLLMENT_CREATED_BY(childObject.getInt("created_by"));
            if (childObject.has("created_on"))
                em.setENROLLMENT_CREATED_ON(childObject.getString("created_on"));
            if (childObject.has("review_status"))
                em.setENROLLMENT_REVIEW_STATUS(childObject.getString("review_status"));
            if (childObject.has("review_comments"))
                em.setENROLLMENT_REVIEW_COMMENTS(childObject.getString("review_comments"));
            if (childObject.has("review_completed_on"))
                em.setENROLLMENT_REVIEW_COMPLETED_ON(childObject.getString("review_completed_on"));
            if (childObject.has("data_downloaded_on"))
                em.setENROLLMENT_DATA_DOWNLOADED_ON(childObject.getString("data_downloaded_on"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return em;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }
}
