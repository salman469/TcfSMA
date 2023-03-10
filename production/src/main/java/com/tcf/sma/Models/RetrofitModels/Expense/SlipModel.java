package com.tcf.sma.Models.RetrofitModels.Expense;

public class SlipModel {
    public String slip_path;
    public long slip_no;
    //1 for bank 2 for cash
    public String slip_category;

    public long getSlip_no() {
        return slip_no;
    }

    public void setSlip_no(long slip_no) {
        this.slip_no = slip_no;
    }

    public String getSlip_category() {
        return slip_category;
    }

    public void setSlip_category(String slip_category) {
        this.slip_category = slip_category;
    }

    public String getSlip_path() {
        return slip_path;
    }

    public void setSlip_path(String slip_path) {
        this.slip_path = slip_path;
    }
}
