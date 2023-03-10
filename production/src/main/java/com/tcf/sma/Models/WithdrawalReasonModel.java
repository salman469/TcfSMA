package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/6/2017.
 */

public class WithdrawalReasonModel {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String name;
    @SerializedName("modified_on")
    private String modified_on;

    private ArrayList<WithdrawalReasonModel> wrList;
    private WithdrawalReasonModel wrm;

    //Constructors
    public WithdrawalReasonModel() {
    }

    public WithdrawalReasonModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public String getReasonName() {
        return name;
    }

    public void setReasonName(String name) {
        this.name = name;
    }

    public ArrayList<WithdrawalReasonModel> getWrmList() {
        return wrList;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    //Methods
    public WithdrawalReasonModel setWrmList(ArrayList<WithdrawalReasonModel> smList) {
        this.wrList = smList;
        return this;
    }

    public WithdrawalReasonModel getWrm() {
        return this.wrm;
    }

    public WithdrawalReasonModel setWrm(WithdrawalReasonModel sm) {
        this.wrm = sm;
        return this;

    }

    //Parsing Methods
    public ArrayList<WithdrawalReasonModel> parseArray(String json) {
        wrList = new ArrayList<>();
        wrm = null;


        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("withdrawal_reason");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    wrm.setId(childObject.getInt("id"));
                if (childObject.has("name"))
                    wrm.setReasonName(childObject.getString("name"));
                wrList.add(wrm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wrList;
    }

    public WithdrawalReasonModel parseObject(String json) {
        wrm = new WithdrawalReasonModel();
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("id"))
                wrm.setId(childObject.getInt("id"));
            if (childObject.has("name"))
                wrm.setReasonName(childObject.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wrm;
    }


    //to display object as a string in spinner
    public String toString() {
        return getReasonName();
    }
}
