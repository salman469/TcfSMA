package com.tcf.sma.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/14/2017.
 */

public class UserSchoolModel {
    private int school_id;
    private String last_sync_on;

    private ArrayList<UserSchoolModel> usmList;
    private UserSchoolModel usm;

    //Constructor
    public UserSchoolModel() {
    }

    public UserSchoolModel(int school_id, String last_sync_on) {
        this.school_id = school_id;
        this.last_sync_on = last_sync_on;
    }

    //Getters
    public int getSchool_id() {
        return school_id;
    }

    //Setters
    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getLast_sync_on() {
        return last_sync_on;
    }

    public void setLast_sync_on(String last_sync_on) {
        this.last_sync_on = last_sync_on;
    }

    //Methods

    public ArrayList<UserSchoolModel> getUsmList() {
        return usmList;
    }

    public void setUsmList(ArrayList<UserSchoolModel> usmList) {
        this.usmList = usmList;
    }

    public UserSchoolModel getUsm() {
        return usm;
    }

    public void setUsm(UserSchoolModel usm) {
        this.usm = usm;
    }

    //Parsing Methods

    //Parsing Methods
    public ArrayList<UserSchoolModel> parseArray(String json) {
        ArrayList<UserSchoolModel> usmList = new ArrayList<UserSchoolModel>();
        UserSchoolModel usm = null;


        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("user_school");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                usm = new UserSchoolModel();
                if (childObject.has("school_id"))
                    usm.setSchool_id(childObject.getInt("school_id"));
                if (childObject.has("last_sync_on"))
                    usm.setLast_sync_on(childObject.getString("last_sync_on"));

                usmList.add(usm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usmList;
    }

    public UserSchoolModel parseObject(String json) {
        UserSchoolModel usm = new UserSchoolModel();
        try {
            JSONObject childObject = new JSONObject(json);

            usm = new UserSchoolModel();
            if (childObject.has("school_id"))
                usm.setSchool_id(childObject.getInt("school_id"));
            if (childObject.has("last_sync_on"))
                usm.setLast_sync_on(childObject.getString("last_sync_on"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usm;
    }
}
