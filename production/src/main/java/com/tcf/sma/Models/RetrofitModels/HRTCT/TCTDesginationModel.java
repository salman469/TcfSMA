package com.tcf.sma.Models.RetrofitModels.HRTCT;

public class TCTDesginationModel {
    public int ID;
    public String Title;
    public boolean isActive;
    public String createdOn;
    public String modifiedOn;

    public TCTDesginationModel() {
    }

    public TCTDesginationModel(int ID, String title) {
        this.ID = ID;
        Title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    @Override
    public String toString() {
        return getTitle();
    }
}
