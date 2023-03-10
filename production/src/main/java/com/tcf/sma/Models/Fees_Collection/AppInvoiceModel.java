package com.tcf.sma.Models.Fees_Collection;

/**
 * Created by Mohammad Haseeb
 */
public class AppInvoiceModel {

    private int id;
    private int sysId;
    private int schoolclassId;
    private int schoolYearId;
    private int studentId;
    private String createdFrom;
    private String createdby;
    private String createdOn;
    private String uploadedOn;
    private String isCorrection;
    private double fees_admission;
    private double fees_exam;
    private double fees_tution;
    private double fees_books;
    private double fees_copies;
    private double fees_uniform;
    private double fees_others;
    private String device_id;
    private String downloadedOn;

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public int getSysId() {
        return sysId;
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    public int getSchoolclassId() {
        return schoolclassId;
    }

    public void setSchoolclassId(int schoolclassId) {
        this.schoolclassId = schoolclassId;
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

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
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

    public String getIsCorrection() {
        return isCorrection;
    }

    public void setIsCorrection(String isCorrection) {
        this.isCorrection = isCorrection;
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

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }
}
