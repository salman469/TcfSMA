package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NationalityModel {
    @SerializedName("ID")
    private int id;
    private String title;
    private boolean isActive;
    private String createdOn;
    private String modifiedOn;

    transient private ArrayList<NationalityModel> nmList;

    public NationalityModel() {
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

    public ArrayList<NationalityModel> getNmList() {
        return nmList;
    }

    public void setNmList(ArrayList<NationalityModel> nmList) {
        this.nmList = nmList;
    }

    public String toString() {
        return getTitle();
    }
}
