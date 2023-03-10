package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class AreaModel {
    private int areaID;
    private int regionID;
    private String name;
    private int areaManagerID;
    @SerializedName("isActve")
    private boolean IsActive;
    @SerializedName("ModifiedOn")
    private String modifiedOn;

    public AreaModel() {
    }

    public AreaModel(int _id, int _regionId, String _name, int _amID) {
        this.areaID = _id;
        this.regionID = _regionId;
        this.name = _name;
        this.areaManagerID = _amID;
    }

    //Getters
    public int getId() {
        return areaID;
    }

    public void setId(int id) {
        this.areaID = id;
    }

    public int getRegionId() {
        return regionID;
    }

    public void setRegionId(int _regionId) {
        this.regionID = _regionId;
    }

    //Setters

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public int getAreaManagerId() {
        return areaManagerID;
    }

    public void setAreaManagerId(int _amId) {
        this.areaManagerID = _amId;
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
