package com.tcf.sma.Models.RetrofitModels.HR;

import com.google.gson.annotations.SerializedName;

public class SeparationAttachmentsModel {
    @SerializedName("ID")
    int resignationID;

    @SerializedName("ImagePath")
    String separationAttachment;
    transient int isActive;
    transient int isUploaded;
    transient int serverId;
    transient String uploadedOn;
    transient String modifiedOn;

    public int getResignationID() {
        return resignationID;
    }

    public void setResignationID(int resignationID) {
        this.resignationID = resignationID;
    }

    public String getSeparationAttachment() {
        return separationAttachment;
    }

    public void setSeparationAttachment(String separationAttachment) {
        this.separationAttachment = separationAttachment;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(int isUploaded) {
        this.isUploaded = isUploaded;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
