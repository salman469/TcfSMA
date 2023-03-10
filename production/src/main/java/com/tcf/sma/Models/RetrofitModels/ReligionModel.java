package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;
import com.tcf.sma.Models.ClassSectionModel;

import java.util.ArrayList;

public class ReligionModel {
    @SerializedName("ID")
    private int id;
    private String title;
    private boolean isActive;
    private String createdOn;
    private String modifiedOn;

    transient private ArrayList<ReligionModel> rmList;

    public ReligionModel() {
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

    public ArrayList<ReligionModel> getRmList() {
        return rmList;
    }

    public void setRmList(ArrayList<ReligionModel> rmList) {
        this.rmList = rmList;
    }

    public String toString() {
        return getTitle();
    }
}
