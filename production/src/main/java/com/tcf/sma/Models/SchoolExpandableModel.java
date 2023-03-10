package com.tcf.sma.Models;

import java.util.List;

public class SchoolExpandableModel {
    private String regionArea;
    private String area;
    private String campus;
    private String location;
    private String region;
    private List<SchoolModel> schoolModels;

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegionArea() {
        return regionArea;
    }

    public void setRegionArea(String regionArea) {
        this.regionArea = regionArea;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<SchoolModel> getSchoolModels() {
        return schoolModels;
    }

    public void setSchoolModels(List<SchoolModel> schoolModels) {
        this.schoolModels = schoolModels;
    }
}
