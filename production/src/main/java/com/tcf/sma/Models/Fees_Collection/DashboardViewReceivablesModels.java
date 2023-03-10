package com.tcf.sma.Models.Fees_Collection;

public class DashboardViewReceivablesModels {
    private String title;
    private int balance;
    private int FeeTypeId;
    private String amountReceived;
    private String amountReceivable;
    private String amountOpening;
    private String amountClosingActive;
    private String amountClosingInActive;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getFeeTypeId() {
        return FeeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        FeeTypeId = feeTypeId;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(String amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getAmountOpening() {
        return amountOpening;
    }

    public void setAmountOpening(String amountOpening) {
        this.amountOpening = amountOpening;
    }

    public String getAmountClosingActive() {
        return amountClosingActive;
    }

    public void setAmountClosingActive(String amountClosingActive) {
        this.amountClosingActive = amountClosingActive;
    }

    public String getAmountClosingInActive() {
        return amountClosingInActive;
    }

    public void setAmountClosingInActive(String amountClosingInActive) {
        this.amountClosingInActive = amountClosingInActive;
    }
}
