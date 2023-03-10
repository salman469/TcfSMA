package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 1/4/2017.
 */

public class ClassModel {
    private int classId;
    private String name;
    private int rank;
    private ArrayList<ClassModel> cmList;
    private ClassModel cm;
    @SerializedName("modifiedOn")
    private String modified_on;


    public ClassModel() {
    }

    public ClassModel(int id, String className) {
        this.classId = id;
        this.name = className;
    }

    //Getters

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    //Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassModel setClassModel(ClassModel cm) {
        this.cm = cm;
        return this;

    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public ArrayList<ClassModel> getClassesList() {
        return cmList;
    }

    //Methods
    public ClassModel setClassesList(ArrayList<ClassModel> cmList) {
        this.cmList = cmList;
        return this;
    }

    public ClassModel getClasseModel() {
        return this.cm;
    }

    //Parsing Methods
    public ArrayList<ClassModel> parseArray(String json) {
        cmList = new ArrayList<>();
        cm = null;


        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("class");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    cm.setClassId(childObject.getInt("id"));
                if (childObject.has("name"))
                    cm.setName(childObject.getString("name"));

                cmList.add(cm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmList;
    }

    public ClassModel parseObject(String json) {
        cm = new ClassModel();
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("id"))
                cm.setClassId(childObject.getInt("id"));
            if (childObject.has("name"))
                cm.setName(childObject.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cm;
    }


    //to display object as a string in spinner
    public String toString() {
        return getName();
    }
}
