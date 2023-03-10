package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Yasir .
 */

public class SyncFeesHeaderModel {

    private int id;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("schoolclass_id")
    private int schoolClassId;
    //    @SerializedName("school_year_id")
//    private int schoolYearId;
    @SerializedName("academicSession_id")
    private int academicSession_id;

    @SerializedName("sys_id")
    private int sysId;
    //    @SerializedName("device_id")
//    private String device_id;
    @SerializedName("forDate")
    private String forDate;
    @SerializedName("TotalAmount")
    private double totalAmount;
    @SerializedName("TypeId")
    private int transactionType_id;
    @SerializedName("CategoryId")
    private int category_id;
    @SerializedName("recieptNo")
    private String receiptNo;
    @SerializedName("depositSlipId")
    private String cashDeposit_id;
    @SerializedName("createdOn_App")
    private String createdOn;
    @SerializedName("createdBy")
    private int createdBy;
    private String uploadedOn;
    private String downloadedOn;
    private String modified_on;
    private String createdOn_Server;


    private ArrayList<FeesDetailUploadModel> fdmList;

//    public SyncFeesHeaderModel(int id, int studentId, int schoolClassId, int schoolYearId, int academicSession_id, int sys_id ,String device_id, String forDate, double totalAmount, int transactionType_id, int category_id, String receiptNo, String cashDeposit_id, String createdOn, int createdBy, ArrayList<FeesDetailUploadModel> fdmList) {
//        this.id = id;
//        this.studentId = studentId;
//        this.schoolClassId = schoolClassId;
//        this.schoolYearId = schoolYearId;
//        this.academicSession_id = academicSession_id;
//        this.sysId = sys_id;
//        this.device_id = device_id;
//        this.forDate = forDate;
//        this.totalAmount = totalAmount;
//        this.transactionType_id = transactionType_id;
//        this.category_id = category_id;
//        this.receiptNo = receiptNo;
//        this.cashDeposit_id = cashDeposit_id;
//        this.createdOn = createdOn;
//        this.createdBy = createdBy;
//        this.fdmList = fdmList;
//    }

    //    @SerializedName("uploaded_on")
//    private String uploadedOn;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

//    public int getSchoolYearId() {
//        return schoolYearId;
//    }

//    public void setSchoolYearId(int schoolYearId) {
//        this.schoolYearId = schoolYearId;
//    }

    public int getAcademicSession_id() {
        return academicSession_id;
    }

    public void setAcademicSession_id(int academicSession_id) {
        this.academicSession_id = academicSession_id;
    }

    public int getSysId() {
        return sysId;
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

//    public String getDevice_id() {
//        return device_id;
//    }
//
//    public void setDevice_id(String device_id) {
//        this.device_id = device_id;
//    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTransactionType_id() {
        return transactionType_id;
    }

    public void setTransactionType_id(int transactionType_id) {
        this.transactionType_id = transactionType_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getCashDeposit_id() {
        return cashDeposit_id;
    }

    public void setCashDeposit_id(String cashDeposit_id) {
        this.cashDeposit_id = cashDeposit_id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public ArrayList<FeesDetailUploadModel> getFdmList() {
        return fdmList;
    }

    public void setFdmList(ArrayList<FeesDetailUploadModel> fdmList) {
        this.fdmList = fdmList;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getCreatedOn_Server() {
        return createdOn_Server;
    }

    public void setCreatedOn_Server(String createdOn_Server) {
        this.createdOn_Server = createdOn_Server;
    }

    public String print() {
        String data = "";
        data += "Id: " + id;
        data += "  studentId: " + studentId;
        data += "  schoolClassId: " + schoolClassId;
        data += "  academicSession_id: " + academicSession_id;
        data += "  sysId: " + sysId;
        return data;
    }
}
