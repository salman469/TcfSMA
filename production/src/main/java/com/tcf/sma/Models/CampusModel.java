package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class CampusModel {
    private int id;
    private int locationID;
    private String name;
    private boolean IsActive;
    @SerializedName("ModifiedOn")
    private String modifiedOn;

    public CampusModel() {
    }

    public CampusModel(int _id, int _locationId, String _name) {
        this.id = _id;
        this.locationID = _locationId;
        this.name = _name;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationID;
    }

    //Setters

    public void setLocationId(int _locationId) {
        this.locationID = _locationId;
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

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
