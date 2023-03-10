package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/3/2017.
 */

public class SectionModel {
    private int sectionId;
    private String name;

    private ArrayList<SectionModel> smList;
    private SectionModel sm;
    @SerializedName("modifiedOn")
    private String modified_on;

    //Constructors
    public SectionModel() {
    }

    public SectionModel(int id, String sectionName) {
        this.sectionId = id;
        this.name = sectionName;
    }


    // Getters
    public int getSectionId() {
        return sectionId;
    }

    // Setters
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public ArrayList<SectionModel> getSectionsList() {
        return smList;
    }

    //Methods
    public SectionModel setSectionsList(ArrayList<SectionModel> smList) {
        this.smList = smList;
        return this;
    }

    public SectionModel getSection() {
        return this.sm;
    }

    public SectionModel setSection(SectionModel sm) {
        this.sm = sm;
        return this;

    }

    //Parsing Methods
    public ArrayList<SectionModel> parseArray(String json) {
        smList = new ArrayList<>();
        sm = null;


        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("section");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    sm.setSectionId(childObject.getInt("id"));
                if (childObject.has("name"))
                    sm.setName(childObject.getString("name"));

                smList.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smList;
    }

    public SectionModel parseObject(String json) {
        sm = new SectionModel();
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("id"))
                sm.setSectionId(childObject.getInt("id"));
            if (childObject.has("name"))
                sm.setName(childObject.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sm;
    }

    //to display object as a string in spinner
    public String toString() {
        return getName();
    }
}
