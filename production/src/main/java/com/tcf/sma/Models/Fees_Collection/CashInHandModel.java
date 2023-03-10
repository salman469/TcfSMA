package com.tcf.sma.Models.Fees_Collection;

import java.io.Serializable;

public class CashInHandModel implements Serializable {

    private String LastDepositDate;
    private String TodayDate;
    private String AdmissionFees;
    private String MonthlyFees;
    private String ExamFees;
    private String books;
    private String copies;
    private String uniforms;
    private String others;
    private String total;
    private String remarks;
    private String reconcileAmount;

    public CashInHandModel() {
    }

    public CashInHandModel(String lastDepositDate, String todayDate, String admissionFees, String monthlyFees, String examFees, String books, String copies, String uniforms, String others, String total) {
        LastDepositDate = lastDepositDate;
        TodayDate = todayDate;
        AdmissionFees = admissionFees;
        MonthlyFees = monthlyFees;
        ExamFees = examFees;
        this.books = books;
        this.copies = copies;
        this.uniforms = uniforms;
        this.others = others;
        this.total = total;
    }

    //Getters
    public String getLastDepositDate() {
        return LastDepositDate;
    }

    //Setters
    public void setLastDepositDate(String lastDepositDate) {
        LastDepositDate = lastDepositDate;
    }

    public String getTodayDate() {
        return TodayDate;
    }

    public void setTodayDate(String todayDate) {
        TodayDate = todayDate;
    }

    public String getAdmissionFees() {
        return AdmissionFees;
    }

    public void setAdmissionFees(String admissionFees) {
        AdmissionFees = admissionFees;
    }

    public String getMonthlyFees() {
        return MonthlyFees;
    }

    public void setMonthlyFees(String monthlyFees) {
        MonthlyFees = monthlyFees;
    }

    public String getExamFees() {
        return ExamFees;
    }

    public void setExamFees(String examFees) {
        ExamFees = examFees;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getCopies() {
        return copies;
    }

    public void setCopies(String copies) {
        this.copies = copies;
    }

    public String getUniforms() {
        return uniforms;
    }

    public void setUniforms(String uniforms) {
        this.uniforms = uniforms;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReconcileAmount() {
        return reconcileAmount;
    }

    public void setReconcileAmount(String reconcileAmount) {
        this.reconcileAmount = reconcileAmount;
    }
}
