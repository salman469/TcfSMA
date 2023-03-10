package com.tcf.sma.Models;

public class ExpenseCheckSumModel {

    private int subheadLimitsMonthlyCount;
    private int schoolPettyCashMonthlyLimitsCount;
    private int transactionsCount;
    private int amountClosingCount;


    public int getSubheadLimitsMonthlyCount() {
        return subheadLimitsMonthlyCount;
    }

    public void setSubheadLimitsMonthlyCount(int subheadLimitsMonthlyCount) {
        this.subheadLimitsMonthlyCount = subheadLimitsMonthlyCount;
    }

    public int getSchoolPettyCashMonthlyLimitsCount() {
        return schoolPettyCashMonthlyLimitsCount;
    }

    public void setSchoolPettyCashMonthlyLimitsCount(int schoolPettyCashMonthlyLimitsCount) {
        this.schoolPettyCashMonthlyLimitsCount = schoolPettyCashMonthlyLimitsCount;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public int getAmountClosingCount() {
        return amountClosingCount;
    }

    public void setAmountClosingCount(int amountClosingCount) {
        this.amountClosingCount = amountClosingCount;
    }
}
