package com.tcf.sma.Models.ViewReceivables;

public class ViewReceivablesTableModel {
    private String type;
    private String created_on;
    private double fees_admission;
    private double fees_exam;
    private double fees_tution;
    private double fees_books;
    private double fees_copies;
    private double fees_uniform;
    private double fees_others;
    private double total;
    private String month;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public double getFees_admission() {
        return fees_admission;
    }

    public void setFees_admission(double fees_admission) {
        this.fees_admission = fees_admission;
    }

    public double getFees_exam() {
        return fees_exam;
    }

    public void setFees_exam(double fees_exam) {
        this.fees_exam = fees_exam;
    }

    public double getFees_tution() {
        return fees_tution;
    }

    public void setFees_tution(double fees_tution) {
        this.fees_tution = fees_tution;
    }

    public double getFees_books() {
        return fees_books;
    }

    public void setFees_books(double fees_books) {
        this.fees_books = fees_books;
    }

    public double getFees_copies() {
        return fees_copies;
    }

    public void setFees_copies(double fees_copies) {
        this.fees_copies = fees_copies;
    }

    public double getFees_uniform() {
        return fees_uniform;
    }

    public void setFees_uniform(double fees_uniform) {
        this.fees_uniform = fees_uniform;
    }

    public double getFees_others() {
        return fees_others;
    }

    public void setFees_others(double fees_others) {
        this.fees_others = fees_others;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
