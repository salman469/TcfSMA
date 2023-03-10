package com.tcf.sma.Models.Fees_Collection;

public class FeesHeaderModel {

    private int id;
    private int sys_id = 0;
    private int schoolClassId;
    private int academicSession_id;
    private int schoolYearId;
    private int studentId;
    private int createdBy;
    private String createdOn;
    private String uploadedOn;
    private String deviceId;
    private String downloadedOn;
    private int school_id;
    private String created_ref_id;
    private int category_id;
    private String for_date;
    private double total_amount;
    private int feeType_id;
    private int transactionType_id;
    private double copyFees;
    private double bookFees;
    private double uniformFees;
    private double othersFees;
    private double tutionFees;
    private double admissionFees;
    private double examFees;
    private String cashDeposit_Sys_id;
    private String remarks;
    //Receipt / FeesReceipt
    private long receiptNumber;
    private String cashDepositId;
    //Invoice / FeesDues
    private String correctionType;
    private long receipt_id;
    private String createdOnServer;

    public FeesHeaderModel() {
    }

    public double getCopyFees() {
        return copyFees;
    }

    public void setCopyFees(double copyFees) {
        this.copyFees = copyFees;
    }

    public double getBookFees() {
        return bookFees;
    }

    public void setBookFees(double bookFees) {
        this.bookFees = bookFees;
    }

    public double getUniformFees() {
        return uniformFees;
    }

    public void setUniformFees(double uniformFees) {
        this.uniformFees = uniformFees;
    }

    public double getOthersFees() {
        return othersFees;
    }

    public void setOthersFees(double othersFees) {
        this.othersFees = othersFees;
    }

    public double getTutionFees() {
        return tutionFees;
    }

    public void setTutionFees(double tutionFees) {
        this.tutionFees = tutionFees;
    }

    public double getAdmissionFees() {
        return admissionFees;
    }

    public void setAdmissionFees(double admissionFees) {
        this.admissionFees = admissionFees;
    }

    public double getExamFees() {
        return examFees;
    }

    public void setExamFees(double examFees) {
        this.examFees = examFees;
    }

    public int getTransactionType_id() {
        return transactionType_id;
    }

    public void setTransactionType_id(int transactionType_id) {
        this.transactionType_id = transactionType_id;
    }

    public int getFeeType_id() {
        return feeType_id;
    }

    public void setFeeType_id(int feeType_id) {
        this.feeType_id = feeType_id;
    }

    public String getFor_date() {
        return for_date;
    }

    public void setFor_date(String for_date) {
        this.for_date = for_date;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSys_id() {
        return sys_id;
    }

    public void setSys_id(int sys_id) {
        this.sys_id = sys_id;
    }

    public int getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public int getAcademicSession_id() {
        return academicSession_id;
    }

    public void setAcademicSession_id(int academicSession_id) {
        this.academicSession_id = academicSession_id;
    }

    public int getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public long getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(long receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCashDepositId() {
        return cashDepositId;
    }

    public void setCashDepositId(String cashDepositId) {
        this.cashDepositId = cashDepositId;
    }


    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getCreated_ref_id() {
        return created_ref_id;
    }

    public void setCreated_ref_id(String created_ref_id) {
        this.created_ref_id = created_ref_id;
    }

    public String getCashDeposit_Sys_id() {
        return cashDeposit_Sys_id;
    }

    public void setCashDeposit_Sys_id(String cashDeposit_Sys_id) {
        this.cashDeposit_Sys_id = cashDeposit_Sys_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(long receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getCreatedOnServer() {
        return createdOnServer;
    }

    public void setCreatedOnServer(String createdOnServer) {
        this.createdOnServer = createdOnServer;
    }
}

