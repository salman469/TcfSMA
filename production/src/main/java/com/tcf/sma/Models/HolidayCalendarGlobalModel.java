package com.tcf.sma.Models;

public class HolidayCalendarGlobalModel {

    private int id;
    private String description;
    private String startDate;
    private String endDate;
    private String createdOn;
    private int createdBy;
    private boolean isActive;

    public HolidayCalendarGlobalModel() {
    }

    public HolidayCalendarGlobalModel(int id, String description, String startDate, String endDate, String createdOn, int createdBy, boolean isActive) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
