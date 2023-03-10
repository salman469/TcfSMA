package com.tcf.sma.Models.SyncProgress;

import java.util.ArrayList;

public class AppSyncStatusModel {
    int statsId;
    int module_id;
    int syncId;
    String startedOn;
    String endedOn;
    int duration;
    double downloaded;
    double uploaded;
    String createdOn;
    String uploadedOn;
    String subModule;

    transient public ArrayList<AppSyncStatusModel> assmList;

    public AppSyncStatusModel() {
    }


    public int getStatsId() {
        return statsId;
    }

    public void setStatsId(int statsId) {
        this.statsId = statsId;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(String startedOn) {
        this.startedOn = startedOn;
    }

    public String getEndedOn() {
        return endedOn;
    }

    public void setEndedOn(String endedOn) {
        this.endedOn = endedOn;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(double downloaded) {
        this.downloaded = downloaded;
    }

    public double getUploaded() {
        return uploaded;
    }

    public void setUploaded(double uploaded) {
        this.uploaded = uploaded;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getSubModule() {
        return subModule;
    }

    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public ArrayList<AppSyncStatusModel> getAssmList() {
        return assmList;
    }

    public void setAssmList(ArrayList<AppSyncStatusModel> assmList) {
        this.assmList = assmList;
    }

    public int getSyncId() {
        return syncId;
    }

    public void setSyncId(int syncId) {
        this.syncId = syncId;
    }
}
