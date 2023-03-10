package com.tcf.sma.Survey.model;

public class GPSData {
    public long id;
    public String latitude;
    public String longitude;
    public String saveDate;
    public String syncDate;
    public String syncStatus;
    public String accuracy;


    public GPSData() {

    }

    public GPSData(long id, String lat, String lon, String acu) {
        this.id = id;
        this.latitude = lat;
        this.longitude = lon;
        this.accuracy = acu;
    }

    public GPSData(String lat, String lon, String acu) {
        this.latitude = lat;
        this.longitude = lon;
        this.accuracy = acu;
    }

}
