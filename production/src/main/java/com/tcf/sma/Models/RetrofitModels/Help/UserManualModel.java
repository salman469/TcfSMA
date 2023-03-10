package com.tcf.sma.Models.RetrofitModels.Help;

import com.google.gson.annotations.SerializedName;

public class UserManualModel {

    private int ID;
    private String Manual_Name;
    private String Filename;
    private String Filepath;
    private int SortRank;
    private String CreatedOn;
    private int CreatedBy;
    private String ModifiedOn;
    private int ModifiedBy;
    private boolean IsActive;

    @SerializedName("Ver")
    private String Version;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getManual_Name() {
        return Manual_Name;
    }

    public void setManual_Name(String manual_Name) {
        Manual_Name = manual_Name;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getFilepath() {
        return Filepath;
    }

    public void setFilepath(String filepath) {
        Filepath = filepath;
    }

    public int getSortRank() {
        return SortRank;
    }

    public void setSortRank(int sortRank) {
        SortRank = sortRank;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}
