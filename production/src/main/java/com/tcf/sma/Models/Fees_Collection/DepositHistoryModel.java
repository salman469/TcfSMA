package com.tcf.sma.Models.Fees_Collection;

import com.tcf.sma.Models.AppModel;

public class DepositHistoryModel {
    private int deposit_id;
    private String deposited_by;
    private String depost_date;
    private String depost_slipNo;
    private int schoolid;
    private double fees_admission;
    private double fees_copies;
    private double fees_books;
    private double fees_uniform;
    private double fees_monthly;
    private double fees_exam;
    private double total;

    public int getDeposit_id() {
        return deposit_id;
    }

    public void setDeposit_id(int deposit_id) {
        this.deposit_id = deposit_id;
    }

    public String getDeposited_by() {
        return deposited_by;
    }

    public void setDeposited_by(String deposited_by) {
        this.deposited_by = deposited_by;
    }

    public String getDepost_date() {
        if (this.depost_date != null && this.depost_date.isEmpty())
            return AppModel.getInstance().convertDatetoFormat(this.depost_date, "yyyy-MM-dd", "dd-MMM-yy");
        return depost_date;
    }

    public void setDepost_date(String depost_date) {
        this.depost_date = depost_date;
    }

    public String getDepost_slipNo() {
        return depost_slipNo;
    }

    public void setDepost_slipNo(String depost_slipNo) {
        this.depost_slipNo = depost_slipNo;
    }

    public double getFees_admission() {
        return fees_admission;
    }

    public void setFees_admission(double fees_admission) {
        this.fees_admission = fees_admission;
    }

    public double getFees_copies() {
        return fees_copies;
    }

    public void setFees_copies(double fees_copies) {
        this.fees_copies = fees_copies;
    }

    public double getFees_books() {
        return fees_books;
    }

    public void setFees_books(double fees_books) {
        this.fees_books = fees_books;
    }

    public double getFees_uniform() {
        return fees_uniform;
    }

    public void setFees_uniform(double fees_uniform) {
        this.fees_uniform = fees_uniform;
    }

    public double getFees_monthly() {
        return fees_monthly;
    }

    public void setFees_monthly(double fees_monthly) {
        this.fees_monthly = fees_monthly;
    }

    public double getFees_exam() {
        return fees_exam;
    }

    public void setFees_exam(double fees_exam) {
        this.fees_exam = fees_exam;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(int schoolid) {
        this.schoolid = schoolid;
    }


}
