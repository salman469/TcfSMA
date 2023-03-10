package com.tcf.sma.Models.RetrofitModels.HR;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class EmployeeDesignationModel {

    @SerializedName("id")
    private int id;

    @SerializedName("HCMDesignation")
    private String Designation_Name;

    private String modifiedOn;

    public EmployeeDesignationModel() {
    }

    public EmployeeDesignationModel(int id, String designation_Name) {
        this.id = id;
        Designation_Name = designation_Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation_Name() {
        return Designation_Name;
    }

    public void setDesignation_Name(String designation_Name) {
        Designation_Name = designation_Name;
    }


    //to display object as a string in spinner
    @NonNull
    @Override
    public String toString() {
        return getDesignation_Name();
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
