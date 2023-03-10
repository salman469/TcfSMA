package com.tcf.sma.Models.RetrofitModels.Expense;

public class LastTransactionsModel {
    private String jvno,head,sub_head,transactiontype,date,bucket;
    private int amount;

    public LastTransactionsModel(String jvno,String head, String sub_head, String transactiontype, int amount, String date, String bucket) {
        this.jvno = jvno;
        this.head = head;
        this.sub_head = sub_head;
        this.transactiontype = transactiontype;
        this.amount = amount;
        this.date = date;
        this.bucket = bucket;
    }

    public String getJvno() {
        return jvno;
    }

    public void setJvno(String jvno) {
        this.jvno = jvno;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSub_head() {
        return sub_head;
    }

    public void setSub_head(String sub_head) {
        this.sub_head = sub_head;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
