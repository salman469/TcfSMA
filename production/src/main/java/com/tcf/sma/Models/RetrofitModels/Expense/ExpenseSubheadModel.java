package com.tcf.sma.Models.RetrofitModels.Expense;

public class ExpenseSubheadModel {
    public String subhead_name;
    public int subhead_id;
    public double spent_amount=0,limit_amount=0;

    public ExpenseSubheadModel() {
    }

    public int getSubhead_id() {
        return subhead_id;
    }

    public void setSubhead_id(int subhead_id) {
        this.subhead_id = subhead_id;
    }

    public String getSubhead_name() {
        return subhead_name;
    }

    public void setSubhead_name(String subhead_name) {
        this.subhead_name = subhead_name;
    }

    public double getSpent_amount() {
        return spent_amount;
    }

    public void setSpent_amount(double spent_amount) {
        this.spent_amount = spent_amount;
    }

    public double getLimit_amount() {
        return limit_amount;
    }

    public void setLimit_amount(double limit_amount) {
        this.limit_amount = limit_amount;
    }


    public ExpenseSubheadModel(int subhead_id,String subhead_name) {
        this.subhead_name = subhead_name;
        this.subhead_id = subhead_id;
    }

    @Override
    public String toString() {
        return subhead_name;
    }
}
