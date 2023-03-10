package com.tcf.sma.Models.SyncProgress;

import com.google.gson.annotations.SerializedName;
import com.tcf.sma.Models.RetrofitModels.UploadCashInHandModel;

import java.util.ArrayList;

public class AppSyncStatusMasterModel {

    @SerializedName("syncId")
    private int syncMasterId;
    private String startedOn;
    private String endedOn;

    @SerializedName("syncTypeID")
    private int syncTypeId;

    @SerializedName("userId")
    private int userId;
    private String SIM1;
    private String SIM2;
    private String mobileOperator;
    private String connectionType;
    private String deviceId;

    @SerializedName("IMEI1")
    private String IMEI;

    @SerializedName("IMEI2")
    private String IMEI2;

    @SerializedName("BatteryStats")
    private int batteryStats;

    @SerializedName("DeviceDetails")
    public String manufacturerModel;

    @SerializedName("AppVersion")
    private String appVersion;


    private String uploadedOn;
    private String androidVersion;

    public int getSyncMasterId() {
        return syncMasterId;
    }

    public void setSyncMasterId(int syncMasterId) {
        this.syncMasterId = syncMasterId;
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

    public int getSyncTypeId() {
        return syncTypeId;
    }

    public void setSyncTypeId(int syncTypeId) {
        this.syncTypeId = syncTypeId;
    }

    public String getSIM1() {
        return SIM1;
    }

    public void setSIM1(String SIM1) {
        this.SIM1 = SIM1;
    }

    public String getSIM2() {
        return SIM2;
    }

    public void setSIM2(String SIM2) {
        this.SIM2 = SIM2;
    }

    public String getMobileOperator() {
        return mobileOperator;
    }

    public void setMobileOperator(String mobileOperator) {
        this.mobileOperator = mobileOperator;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getIMEI2() {
        return IMEI2;
    }

    public void setIMEI2(String IMEI2) {
        this.IMEI2 = IMEI2;
    }

    public int getBatteryStats() {
        return batteryStats;
    }

    public void setBatteryStats(int batteryStats) {
        this.batteryStats = batteryStats;
    }

    public String getManufacturerModel() {
        return manufacturerModel;
    }

    public void setManufacturerModel(String manufacturerModel) {
        this.manufacturerModel = manufacturerModel;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }
}
