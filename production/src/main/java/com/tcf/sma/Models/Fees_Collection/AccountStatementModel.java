package com.tcf.sma.Models.Fees_Collection;

/**
 * Created By Mohammad Haseeb
 */
public class AccountStatementModel {

    private int id;
    private String jvNo;
    private String receiptNo;
    private String depositSlipNo;
    private String date;
    private String depositDate;
    private String description;
    private String amount;
    private String grno;
    private String balance;
    private String transactionType;
    private int transactionCategoryId;
    private String transactiontype_Id;
    public AccountStatementModel() {
    }
    public AccountStatementModel(int id, String date, String description, String amount, String transactionType) {
        this.id = id;
        this.receiptNo = receiptNo;
        this.depositSlipNo = depositSlipNo;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public AccountStatementModel(int id, String jvNo, String receiptNo, String depositSlipNo, String date, String depositDate, String description, String amount, String transactionType, String grNo) {
        this.id = id;
        this.jvNo = jvNo;
        this.receiptNo = receiptNo;
        this.depositSlipNo = depositSlipNo;
        this.date = date;
        this.grno = grNo;
        this.depositDate = depositDate;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public AccountStatementModel(int id, String jvNo, String receiptNo, String depositSlipNo, String date, String depositDate, String description, String amount, int transactionCategoryId, String grNo) {
        this.id = id;
        this.jvNo = jvNo;
        this.receiptNo = receiptNo;
        this.depositSlipNo = depositSlipNo;
        this.date = date;
        this.grno = grNo;
        this.depositDate = depositDate;
        this.description = description;
        this.amount = amount;
        this.transactionCategoryId = transactionCategoryId;

    }

    /**
     * Full argument constructor
     *
     * @param id
     * @param jvNo
     * @param receiptNo
     * @param depositSlipNo
     * @param date
     * @param depositDate
     * @param description
     * @param amount
     * @param balance
     * @param transactionType
     */
    public AccountStatementModel(int id, String jvNo, String receiptNo, String depositSlipNo, String date, String depositDate, String description, String amount, String balance, String transactionType, String additionalGrNo) {
        this.id = id;
        this.jvNo = jvNo;
        this.receiptNo = receiptNo;
        this.depositSlipNo = depositSlipNo;
        this.date = date;
        this.grno = additionalGrNo;
        this.depositDate = depositDate;
        this.description = description;
        this.amount = amount;
        this.balance = balance;
        this.transactionType = transactionType;
    }

    public AccountStatementModel(int id, String jvNo, String receiptNo, String depositSlipNo, String date, String depositDate, String description, String amount, String balance, int transactionCategoryId, String additionalGrNo) {
        this.id = id;
        this.jvNo = jvNo;
        this.receiptNo = receiptNo;
        this.depositSlipNo = depositSlipNo;
        this.date = date;
        this.grno = additionalGrNo;
        this.depositDate = depositDate;
        this.description = description;
        this.amount = amount;
        this.balance = balance;
        this.transactionCategoryId = transactionCategoryId;
    }

    public String getGrno() {
        return grno;
    }

    public void setGrno(String grno) {
        this.grno = grno;
    }


    //Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJvNo() {
        return jvNo;
    }

    public void setJvNo(String jvNo) {
        this.jvNo = jvNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDepositSlipNo() {
        return depositSlipNo;
    }

    public void setDepositSlipNo(String depositSlipNo) {
        this.depositSlipNo = depositSlipNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getTransactionCategoryId() {
        return transactionCategoryId;
    }

    public void setTransactionCategoryId(int transactionCategoryId) {
        this.transactionCategoryId = transactionCategoryId;
    }

    public String getTransactiontype_Id() {
        return transactiontype_Id;
    }

    public void setTransactiontype_Id(String transactiontype_Id) {
        this.transactiontype_Id = transactiontype_Id;
    }
}
