package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.EnrollmentModel;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/22/2017.
 */

public class UploadEnrollmentModel {
    @SerializedName("enrollment_model")
    private EnrollmentModel em;
    @SerializedName("enrollment_images")
    private ArrayList<EnrollmentImageModel> eimList;

    public UploadEnrollmentModel() {
    }

    public UploadEnrollmentModel(EnrollmentModel em, ArrayList<EnrollmentImageModel> eimList) {
        this.em = em;
        this.eimList = eimList;
    }

    public EnrollmentModel getEm() {
        return em;
    }

    public void setEm(EnrollmentModel em) {
        this.em = em;
    }

    public ArrayList<EnrollmentImageModel> getEimList() {
        return eimList;
    }

    public void setEimList(ArrayList<EnrollmentImageModel> eimList) {
        this.eimList = eimList;
    }
}
