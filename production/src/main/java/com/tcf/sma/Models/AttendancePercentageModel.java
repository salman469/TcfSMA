package com.tcf.sma.Models;

/**
 * Created by Zubair Soomro on 4/6/2017.
 */

public class AttendancePercentageModel {
    private String forDate;
    private float percentage;

    public AttendancePercentageModel(String forDate, float percentage) {
        this.forDate = forDate;
        this.percentage = percentage;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
