package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ElectiveSubjectModel {
    @SerializedName("ID")
    private int id;
    private String title;
    private boolean isActive;
    private String createdOn;
    private String modifiedOn;

    transient private ArrayList<ElectiveSubjectModel> esmList;

    public ElectiveSubjectModel() {
    }

    public ElectiveSubjectModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public ArrayList<ElectiveSubjectModel> getEsmList() {
        return esmList;
    }

    public void setEsmList(ArrayList<ElectiveSubjectModel> esmList) {
        this.esmList = esmList;
    }

    public String toString() {
        return getTitle();
    }
}
