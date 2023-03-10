package com.tcf.sma.Models.RetrofitModels.Expense;

public class TransactionListingModel {
    //Advance params
    private String balance;
    //Salary params
    private String employeename,employeecode;
    private String expensehead,jvno,school,checkno;
    private String head,sub_head,transactiontype,i_o,bucket,date;
    private int id,amount, category_id;
    private int isActive;

//    public TransactionListingModel(String balance, String employeename, String employeecode, String expensehead, String jvno,
//                                   String school, String checkno,String head, String sub_head, String transactiontype, int amount,
//                                   String date, String bucket) {
//        this.balance = balance;
//        this.employeename = employeename;
//        this.employeecode = employeecode;
//        this.expensehead = expensehead;
//        this.jvno = jvno;
//        this.school = school;
//        this.checkno = checkno;
//        this.head = head;
//        this.sub_head = sub_head;
//        this.transactiontype = transactiontype;
//        this.amount = amount;
//        this.date = date;
//        this.bucket = bucket;
//    }


    public int isActive() {
        return isActive;
    }

    public void setActive(int active) {
        isActive = active;
    }

    public TransactionListingModel() {

    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getExpensehead() {
        return expensehead;
    }

    public void setExpensehead(String expensehead) {
        this.expensehead = expensehead;
    }

    public String getJvno() {
        return jvno;
    }

    public void setJvno(String jvno) {
        this.jvno = jvno;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCheckno() {
        return checkno;
    }

    public void setCheckno(String checkno) {
        this.checkno = checkno;
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

    public String getI_o() {
        return i_o;
    }

    public void setI_o(String i_o) {
        this.i_o = i_o;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
