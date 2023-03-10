package com.tcf.sma.Models.Fees_Collection;

public class DashboardReceivableModel {

    private int feeType_id;
    //    private String totalAmount;
    private String totalReceivable;
    private String totalReceived;
    private String totalOpening;
    private String totalClosingActive;
    private String totalClosingInActive;
    //for Correction
    private String forDate;

    public DashboardReceivableModel() {
    }

    public DashboardReceivableModel(int feeType_id, String totalAmount) {
        this.feeType_id = feeType_id;
//        this.totalAmount = totalAmount;
    }

    public int getFeeType_id() {
        return feeType_id;
    }

    public void setFeeType_id(int feeType_id) {
        this.feeType_id = feeType_id;
    }

//    public String getTotalAmount() {
//        return totalAmount;
//    }

//    public void setTotalAmount(String totalAmount) {
//        this.totalAmount = totalAmount;
//    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public String getTotalReceivable() {
        return totalReceivable;
    }

    public void setTotalReceivable(String totalReceivable) {
        this.totalReceivable = totalReceivable;
    }

    public String getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(String totalReceived) {
        this.totalReceived = totalReceived;
    }

    public String getTotalOpening() {
        return totalOpening;
    }

    public void setTotalOpening(String totalOpening) {
        this.totalOpening = totalOpening;
    }

    public String getTotalClosingActive() {
        return totalClosingActive;
    }

    public void setTotalClosingActive(String totalClosingActive) {
        this.totalClosingActive = totalClosingActive;
    }

    public String getTotalClosingInActive() {
        return totalClosingInActive;
    }

    public void setTotalClosingInActive(String totalClosingInActive) {
        this.totalClosingInActive = totalClosingInActive;
    }
}
