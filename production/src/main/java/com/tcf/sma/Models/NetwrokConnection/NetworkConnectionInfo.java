package com.tcf.sma.Models.NetwrokConnection;

import com.google.gson.annotations.SerializedName;

public class NetworkConnectionInfo {
    private int id;
    private double longitude;
    private double latitude;
    private String deviceId;
    private String SIM1;
    private String SIM2;
    private String mobileOperator;
    private String connectionType;
    private double downloadSpeed;
    private double uploadSpeed;
    private double latency;

    @SerializedName("IMEI1")
    private String IMEI;

    @SerializedName("IMEI2")
    private String IMEI2;

    @SerializedName("BatteryStats")
    private int batteryStats;

    @SerializedName("DeviceDetails")
    public String manufacturerModel;

    @SerializedName("CreatedOn")
    public String createdOn;

    @SerializedName("AppVersion")
    private String appVersion;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public double getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(double downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public double getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(double uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public String getManufacturerModel() {
        return manufacturerModel;
    }

    public void setManufacturerModel(String manufacturerModel) {
        this.manufacturerModel = manufacturerModel;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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
}
