package com.tcf.sma.Models.Fees_Collection;

public class CashInHandReportModel {
    private double openingCashInHand;
    private double cashReceived;
    private double cashDeposit;
    private double closingCashInHand;

    public double getOpeningCashInHand() {
        return openingCashInHand;
    }

    public void setOpeningCashInHand(double openingCashInHand) {
        this.openingCashInHand = openingCashInHand;
    }

    public double getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(double cashReceived) {
        this.cashReceived = cashReceived;
    }

    public double getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(double cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public double getClosingCashInHand() {
        return closingCashInHand;
    }

    public void setClosingCashInHand(double closingCashInHand) {
        this.closingCashInHand = closingCashInHand;
    }
}
