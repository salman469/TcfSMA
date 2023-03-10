package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class RSchoolModel {
    private int schoolId;
    private String name;
    private int prinicipalId;

    public RSchoolModel() {
    }

    public RSchoolModel(int schoolId, String name, int prinicipalId) {
        this.schoolId = schoolId;
        this.name = name;
        this.prinicipalId = prinicipalId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrinicipalId() {
        return prinicipalId;
    }

    public void setPrinicipalId(int prinicipalId) {
        this.prinicipalId = prinicipalId;
    }
}
