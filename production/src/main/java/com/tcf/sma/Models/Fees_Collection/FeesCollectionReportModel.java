package com.tcf.sma.Models.Fees_Collection;

public class FeesCollectionReportModel {
    private double dues;
    private double collected;
    private double target;
    private double total;
    private String month;
    private String startDate;
    private String endDate;
    private double balance;

    //    public double getTotal() {
//        return this.fees_admission + this.fees_books + this.fees_exam + this.fees_monthly+
//                this.fees_copies+this.fees_uniform;
//    }
//
//    public void setTotal(double total) {
//        this.total = total;
//    }
    public FeesCollectionReportModel() {
    }

    public double getDues() {
        return dues;
    }

    public void setDues(double dues) {
        this.dues = dues;
    }

    public double getCollected() {
        return collected;
    }

    public void setCollected(double collected) {
        this.collected = collected;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getTotal() {
        return this.collected + this.dues;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
