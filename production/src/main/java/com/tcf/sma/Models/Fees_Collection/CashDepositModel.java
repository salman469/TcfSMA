package com.tcf.sma.Models.Fees_Collection;

import java.io.Serializable;

public class CashDepositModel implements Serializable {

    private int id;
    private int sysId;
    private String total;
    private String createdBy;
    private String createdOn;
    public String modified_on;
    private String uploadedOn;
    private int schoolId;
    private String depositSlipNo;
    private double depositAmount;
    private String depositSlipFilePath;
    private String deviceId;
    private String downloadedOn;
    private String remarks;

    public CashDepositModel() {
    }

    public CashDepositModel(int id, String total, String createdBy, String createdOn, String uploadedOn, int schoolId, String depositSlipNo, double depositAmount, String depositSlipFilePath, String deviceId) {
        this.id = id;
        this.total = total;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.uploadedOn = uploadedOn;
        this.schoolId = schoolId;
        this.depositSlipNo = depositSlipNo;
        this.depositAmount = depositAmount;
        this.depositSlipFilePath = depositSlipFilePath;
        this.deviceId = deviceId;
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getDepositSlipNo() {
        return depositSlipNo;
    }

    public void setDepositSlipNo(String depositSlipNo) {
        this.depositSlipNo = depositSlipNo;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDepositSlipFilePath() {
        return depositSlipFilePath;
    }

    public void setDepositSlipFilePath(String depositSlipFilePath) {
        this.depositSlipFilePath = depositSlipFilePath;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSysId() {
        return sysId;
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }
}
