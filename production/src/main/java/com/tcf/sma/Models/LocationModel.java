package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class LocationModel {
    private int locationID;
    private int areaID;
    private String name;
    private boolean IsActive;
    @SerializedName("ModifiedOn")
    private String modifiedOn;

    public LocationModel() {
    }

    public LocationModel(int _id, int _areaId, String _name) {
        this.locationID = _id;
        this.areaID = _areaId;
        this.name = _name;
    }

    //Getters
    public int getId() {
        return locationID;
    }

    public void setId(int id) {
        this.locationID = id;
    }

    public int getAreaId() {
        return areaID;
    }

    //Setters

    public void setAreaId(int _areaId) {
        this.areaID = _areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}

