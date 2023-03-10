package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class SyncCashReceiptsModel {

    public static ArrayList<SyncCashReceiptsModel> amList;
    private static SyncCashReceiptsModel instance;
    private int id;
    @SerializedName("receipt_no")
    private String receiptNo;
    @SerializedName("schoolclass_id")
    private int schoolClassId;
    @SerializedName("school_year_id")
    private int schoolYearId;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("created_by")
    private int createdBy;
    @SerializedName("created_on")
    private String createdOn;
    @SerializedName("uploaded_on")
    private String uploadedOn;
    @SerializedName("is_correction")
    private boolean isCorrection;
    @SerializedName("fees_admission")
    private double fees_admission;
    @SerializedName("fees_exam")
    private double fees_exam;
    @SerializedName("fees_tution")
    private double fees_tution;
    @SerializedName("fees_books")
    private double fees_books;
    @SerializedName("fees_copies")
    private double fees_copies;
    @SerializedName("fees_uniform")
    private double fees_uniform;
    @SerializedName("fees_others")
    private double fees_others;
    @SerializedName("device_id")
    private int device_id;
    @SerializedName("CashDeposit_id")
    private int cashDepositId;

    private int server_id;

    public static SyncCashReceiptsModel getInstance() {
        return (instance == null) ? instance = new SyncCashReceiptsModel() : instance;
    }

    public void setInstance(SyncCashReceiptsModel instance) {
        this.instance = instance;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
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

    public int getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(int _schoolClassId) {
        this.schoolClassId = _schoolClassId;
    }

    public int getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(int _schoolYearId) {
        this.schoolYearId = _schoolYearId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int _studentId) {
        this.studentId = _studentId;
    }

    public boolean getIsCorrection() {
        return isCorrection;
    }

    public void setIsCorrection(boolean _isCorrection) {
        this.isCorrection = _isCorrection;
    }

    public double getFees_admission() {
        return fees_admission;
    }

    public void setFees_admission(double _fees_admission) {
        this.fees_admission = _fees_admission;
    }

    public double getFees_exam() {
        return fees_exam;
    }

    public void setFees_exam(double _fees_exam) {
        this.fees_exam = _fees_exam;
    }

    public double getFees_tution() {
        return fees_tution;
    }

    public void setFees_tution(double _fees_tution) {
        this.fees_tution = _fees_tution;
    }

    public double getFees_books() {
        return fees_books;
    }

    public void setFees_books(double _fees_books) {
        this.fees_books = _fees_books;
    }

    public double getFees_copies() {
        return fees_copies;
    }

    public void setFees_copies(double _fees_copies) {
        this.fees_copies = _fees_copies;
    }

    public double getFees_uniform() {
        return fees_uniform;
    }

    public void setFees_uniform(double _fees_uniform) {
        this.fees_uniform = _fees_uniform;
    }

    public double getFees_others() {
        return fees_others;
    }

    public void setFees_others(double _fees_others) {
        this.fees_others = _fees_others;
    }

    public int getCashDepositId() {
        return cashDepositId;
    }

    public void setCashDepositId(int _cashDepositId) {
        this.cashDepositId = _cashDepositId;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    //Methods
    public ArrayList<SyncCashReceiptsModel> getAmList() {
        return amList;
    }

    public void setAmList(ArrayList<SyncCashReceiptsModel> amList) {
        this.amList = amList;
    }

    //Parsing Methods
    public ArrayList<SyncCashReceiptsModel> parseArray(String json) {
        amList = null;
        instance = null;

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("feesreceipts");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                instance = parseJSONObject(childObject);
                amList.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return amList;
    }

    public SyncCashReceiptsModel parseObject(String json) {
        instance = null;
        try {
            JSONObject childObject = new JSONObject(json);

            instance = parseJSONObject(childObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public SyncCashReceiptsModel parseJSONObject(JSONObject childObject) {
        instance = null;

        try {
            if (childObject.has("id"))
                instance.setServer_id(childObject.getInt("id"));
            if (childObject.has("receipt_no"))
                instance.setReceiptNo(childObject.getString("receipt_no"));
            if (childObject.has("schoolclass_id"))
                instance.setSchoolClassId(childObject.getInt("schoolclass_id"));
            if (childObject.has("school_year_id"))
                instance.setSchoolYearId(childObject.getInt("school_year_id"));
            if (childObject.has("student_id"))
                instance.setStudentId(childObject.getInt("student_id"));
            if (childObject.has("created_by"))
                instance.setCreatedBy(childObject.getInt("created_by"));
            if (childObject.has("created_on"))
                instance.setCreatedOn(childObject.getString("created_on"));
            if (childObject.has("uploaded_on"))
                instance.setUploadedOn(childObject.getString("uploaded_on"));
            if (childObject.has("is_correction"))
                instance.setIsCorrection(childObject.getBoolean("is_correction"));

            if (childObject.has("fees_admission"))
                instance.setFees_admission(childObject.getDouble("fees_admission"));
            if (childObject.has("fees_exam"))
                instance.setFees_exam(childObject.getDouble("fees_exam"));
            if (childObject.has("fees_tution"))
                instance.setFees_tution(childObject.getDouble("fees_tution"));
            if (childObject.has("fees_books"))
                instance.setFees_books(childObject.getDouble("fees_books"));
            if (childObject.has("fees_copies"))
                instance.setFees_copies(childObject.getDouble("fees_copies"));
            if (childObject.has("fees_uniform"))
                instance.setFees_uniform(childObject.getDouble("fees_uniform"));
            if (childObject.has("fees_others"))
                instance.setFees_others(childObject.getDouble("fees_others"));

            if (childObject.has("CashDeposit_id"))
                instance.setCashDepositId(childObject.getInt("CashDeposit_id"));

            return instance;
        } catch (Exception e) {

        }
        return null;
    }

}
