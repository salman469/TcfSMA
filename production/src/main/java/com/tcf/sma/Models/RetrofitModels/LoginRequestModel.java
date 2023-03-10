package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 2/13/2017.
 */

public class LoginRequestModel {
    private String username, token, DeviceId, NotificationKey, appDatetime, appVersion;

    //Constructors
    public LoginRequestModel() {
    }

    public LoginRequestModel(String username, String token, String deviceId, String notificationKey) {
        this.username = username;
        this.token = token;
        DeviceId = deviceId;
        NotificationKey = notificationKey;
    }

    //Getters
    public String getUsername() {
        return username;
    }

    //Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        this.DeviceId = deviceId;
    }

    public String getNotificationKey() {
        return NotificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        NotificationKey = notificationKey;
    }

    public String getAppDatetime() {
        return appDatetime;
    }

    public void setAppDatetime(String appDatetime) {
        this.appDatetime = appDatetime;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
