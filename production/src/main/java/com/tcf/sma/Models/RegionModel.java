package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class RegionModel {
    private int regionID;
    private String name;
    private int regionManagerID;
    private boolean IsActive;
    @SerializedName("ModifiedOn")
    private String modifiedOn;

    public RegionModel() {
    }

    public RegionModel(int _id, String _name, int _rmID) {
        this.regionID = _id;
        this.name = _name;
        this.regionManagerID = _rmID;
    }

    //Getters
    public int getId() {
        return regionID;
    }

    public void setId(int id) {
        this.regionID = id;
    }

    public String getName() {
        return name;
    }

    //Setters

    public void setName(String _name) {
        this.name = _name;
    }

    public int getRegionManagerId() {
        return regionManagerID;
    }

    public void setRegionManagerId(int _regionManagerId) {
        this.regionID = _regionManagerId;
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
