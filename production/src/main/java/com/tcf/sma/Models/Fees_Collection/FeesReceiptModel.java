package com.tcf.sma.Models.Fees_Collection;

public class FeesReceiptModel {

    private int id;
    private String correctionType;
    private String admissionFees;
    private String cashDepositId;
    private String downloadedOn;
    private String tutionFees;
    private String monthlyFees;
    private int sys_id = 0;
    private String copyFees;
    private String othersFees;
    private String bookFees;
    private String uniformFees;
    private String examFees;
    private String deviceId;
    private int studentId;
    private int schoolClassId;
    private int receiptNumber;
    private String createdOn;
    private String uploadedOn;
    private int createdBy;
    private int schoolYearId;

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public String getCashDepositId() {
        return cashDepositId;
    }

    public void setCashDepositId(String cashDepositId) {
        this.cashDepositId = cashDepositId;
    }

    public int getSys_id() {
        return sys_id;
    }

    public void setSys_id(int sys_id) {
        this.sys_id = sys_id;
    }

    public String getOthersFees() {
        return othersFees;
    }

    public void setOthersFees(String othersFees) {
        this.othersFees = othersFees;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getAdmissionFees() {
        return admissionFees;
    }

    public void setAdmissionFees(String admissionFees) {
        this.admissionFees = admissionFees;
    }

    public String getTutionFees() {
        return tutionFees;
    }

    public void setTutionFees(String tutionFees) {
        this.tutionFees = tutionFees;
    }

    public String getMonthlyFees() {
        return monthlyFees;
    }

    public void setMonthlyFees(String monthlyFees) {
        this.monthlyFees = monthlyFees;
    }

    public String getCopyFees() {
        return copyFees;
    }

    public void setCopyFees(String copyFees) {
        this.copyFees = copyFees;
    }

    public String getBookFees() {
        return bookFees;
    }

    public void setBookFees(String bookFees) {
        this.bookFees = bookFees;
    }

    public String getUniformFees() {
        return uniformFees;
    }

    public void setUniformFees(String uniformFees) {
        this.uniformFees = uniformFees;
    }

    public String getExamFees() {
        return examFees;
    }

    public void setExamFees(String examFees) {
        this.examFees = examFees;
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

    public int getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
