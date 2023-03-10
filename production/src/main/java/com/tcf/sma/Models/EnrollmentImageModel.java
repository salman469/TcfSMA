package com.tcf.sma.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 2/7/2017.
 */

public class EnrollmentImageModel {

    private int id;
    private long enrollment_id;
    private String filename;
    private String uploaded_on;
    private String review_status;
    private String filetype;

    private ArrayList<EnrollmentImageModel> eimList;
    private EnrollmentImageModel eim;

    public EnrollmentImageModel() {
    }

    public EnrollmentImageModel(long enrollment_id, String filename, String uploaded_on, String review_status, String filetype) {
        this.enrollment_id = enrollment_id;
        this.filename = filename;
        this.uploaded_on = uploaded_on;
        this.review_status = review_status;
        this.filetype = filetype;
    }

    public long getEnrollment_id() {
        return enrollment_id;
    }

    public void setEnrollment_id(long enrollment_id) {
        this.enrollment_id = enrollment_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    //Methods
    public ArrayList<EnrollmentImageModel> getEimList() {
        return eimList;
    }

    public void setEimList(ArrayList<EnrollmentImageModel> eimList) {
        this.eimList = eimList;
    }

    public EnrollmentImageModel getEim() {
        return eim;
    }

    public void setEim(EnrollmentImageModel eim) {
        this.eim = eim;
    }

    //Parsing Methods
    public ArrayList<EnrollmentImageModel> parseArray(String json) {
        eimList = new ArrayList<>();
        eim = null;

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("enrollment_image");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    eim.setId(childObject.getInt("id"));
                if (childObject.has("enrollment_id"))
                    eim.setEnrollment_id(childObject.getInt("enrollment_id"));
                if (childObject.has("filename"))
                    eim.setFilename(childObject.getString("filename"));
                if (childObject.has("uploaded_on"))
                    eim.setUploaded_on(childObject.getString("uploaded_on"));
                if (childObject.has("review_status"))
                    eim.setReview_status(childObject.getString("review_status"));
                if (childObject.has("filetype"))
                    eim.setFiletype(childObject.getString("filetype"));
                eimList.add(eim);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eimList;
    }

    public EnrollmentImageModel parseObject(String json) {
        eim = new EnrollmentImageModel();
        try {
            JSONObject childObject = new JSONObject(json);
            if (childObject.has("id"))
                eim.setId(childObject.getInt("id"));
            if (childObject.has("enrollment_id"))
                eim.setEnrollment_id(childObject.getInt("enrollment_id"));
            if (childObject.has("filename"))
                eim.setFilename(childObject.getString("filename"));
            if (childObject.has("uploaded_on"))
                eim.setUploaded_on(childObject.getString("uploaded_on"));
            if (childObject.has("review_status"))
                eim.setReview_status(childObject.getString("review_status"));
            if (childObject.has("filetype"))
                eim.setFiletype(childObject.getString("filetype"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eim;
    }
}
