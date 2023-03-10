package com.tcf.sma.Models.Fees_Collection;

public class ViewReceivablesCorrectionModel {
    private String title;
    private String balance;
    private String newReceived;
    private String newSales;
    private String oldSale;
    private String oldRecieved;
    private String forDate;
    private boolean isBalanceNegative = false;
    private boolean isSalesGreaterThan5000 = false;
    private boolean isAmountGreaterThan5000 = false;
    private int FeeTypeId;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewReceived() {
        return newReceived;
    }

    public void setNewReceived(String newReceived) {
        this.newReceived = newReceived;
    }

    public String getNewSales() {
        return newSales;
    }

    public void setNewSales(String newSales) {
        this.newSales = newSales;
    }

    public String getOldSale() {
        return oldSale;
    }

    public void setOldSale(String oldSale) {
        this.oldSale = oldSale;
    }

    public String getOldRecieved() {
        return oldRecieved;
    }

    public void setOldRecieved(String oldRecieved) {
        this.oldRecieved = oldRecieved;
    }

    public boolean isBalanceNegative() {
        return isBalanceNegative;
    }

    public void setBalanceNegative(boolean balanceNegative) {
        isBalanceNegative = balanceNegative;
    }

    public boolean isSalesGreaterThan5000() {
        return isSalesGreaterThan5000;
    }

    public void setSalesGreaterThan5000(boolean salesGreaterThan5000) {
        isSalesGreaterThan5000 = salesGreaterThan5000;
    }

    public boolean isAmountGreaterThan5000() {
        return isAmountGreaterThan5000;
    }

    public void setAmountGreaterThan5000(boolean amountGreaterThan5000) {
        isAmountGreaterThan5000 = amountGreaterThan5000;
    }

    public int getFeeTypeId() {
        return FeeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        FeeTypeId = feeTypeId;
    }
}