package com.tcf.sma.Models.Fees_Collection;

public class ViewReceivablesModels {
    private String title;
    private String balance;
    private String previouslyReceived;
    private boolean isBalanceNegative = false;
    private String todaySales;
    private boolean isSalesGreaterThan5000 = false;
    private boolean isAmountGreaterThan5000 = false;
    private String amountReceived;
    private int FeeTypeId;
    private boolean isSalesDisabled;

    public int getFeeTypeId() {
        return FeeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        FeeTypeId = feeTypeId;
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

    public boolean isSalesDisabled() {
        return isSalesDisabled;
    }

    public void setSalesDisabled(boolean salesDisabled) {
        isSalesDisabled = salesDisabled;
    }

    public boolean isBalanceNegative() {
        return isBalanceNegative;
    }

    public void setBalanceNegative(boolean balanceNegative) {
        isBalanceNegative = balanceNegative;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPreviouslyReceived() {
        return previouslyReceived;
    }

    public void setPreviouslyReceived(String previouslyReceived) {
        this.previouslyReceived = previouslyReceived;
    }

    public String getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(String todaySales) {
        this.todaySales = todaySales;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }
}
