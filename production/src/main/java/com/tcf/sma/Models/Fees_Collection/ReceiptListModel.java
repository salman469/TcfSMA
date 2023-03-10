package com.tcf.sma.Models.Fees_Collection;

public class ReceiptListModel {
    private String receiptNo;
    private int isDeposited;
    private String createBy;
    private String createOn;
    private double totalAmount;
    private int schoolId;
    private int student_gr_no;
    private int studentId;
    private int receiptId;
    private int schoolClassId;
    private String deposit_slip_no;
    private int categoryId;
    private int typeId;
    private double receivables;


    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public boolean getIsDeposited() {
        return isDeposited == 1;
    }

    public void setIsDeposited(int isDeposited) {
        this.isDeposited = isDeposited;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getStudent_gr_no() {
        return student_gr_no;
    }

    public void setStudent_gr_no(int student_gr_no) {
        this.student_gr_no = student_gr_no;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getDeposit_slip_no() {
        return deposit_slip_no;
    }

    public void setDeposit_slip_no(String deposit_slip_no) {
        this.deposit_slip_no = deposit_slip_no;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public double getReceivables() {
        return receivables;
    }

    public void setReceivables(double receivables) {
        this.receivables = receivables;
    }
}
