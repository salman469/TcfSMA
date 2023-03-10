package com.tcf.sma.Models.Fees_Collection;

/**
 * Created By Mohammad Haseeb
 */
public class FeesDuesModel {
    private int id;
    private String jvNo;
    private String date;
    private String description;
    private int sys_id;
    private String downloadedOn;
    private String amount;
    private int schoolclass_id;
    private String deviceId;
    private int school_id;
    private int school_year_id;
    private int academicSession_id;
    private int student_id;
    private String created_from;
    private String created_ref_id;
    private String created_by;
    private String created_on;
    private String uploaded_on;
    private String correction_type;
    private double fees_admission;
    private double fees_exam;
    private double fees_tution;
    private double fees_other;
    private double fees_books;
    private double fees_copies;
    private double fees_uniform;
    private String device_id;
    private String receiptNo;
    private String deposit_slip_no;

    public FeesDuesModel() {
    }

    public String getDownloadedOn() {

        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public int getSys_id() {
        return sys_id;
    }

    public void setSys_id(int sys_id) {
        this.sys_id = sys_id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getFees_other() {
        return fees_other;
    }

    public void setFees_other(double fees_other) {
        this.fees_other = fees_other;
    }

    //Getters
    public String getJvNo() {
        return jvNo;
    }

    //Setters
    public void setJvNo(String jvNo) {
        this.jvNo = jvNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getSchoolclass_id() {
        return schoolclass_id;
    }

    public void setSchoolclass_id(int schoolclass_id) {
        this.schoolclass_id = schoolclass_id;
    }

    public int getSchool_year_id() {
        return school_year_id;
    }

    public void setSchool_year_id(int school_year_id) {
        this.school_year_id = school_year_id;
    }

    public int getAcademicSession_id() {
        return academicSession_id;
    }

    public void setAcademicSession_id(int academicSession_id) {
        this.academicSession_id = academicSession_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getCreated_from() {
        return created_from;
    }

    public void setCreated_from(String created_from) {
        this.created_from = created_from;
    }

    public String getCreated_ref_id() {
        return created_ref_id;
    }

    public void setCreated_ref_id(String created_ref_id) {
        this.created_ref_id = created_ref_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUploaded_on() {
        return uploaded_on;
    }

    public void setUploaded_on(String uploaded_on) {
        this.uploaded_on = uploaded_on;
    }

    public String getCorrection_type() {
        return correction_type;
    }

    public void setCorrection_type(String correction_type) {
        this.correction_type = correction_type;
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

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDeposit_slip_no() {
        return deposit_slip_no;
    }

    public void setDeposit_slip_no(String deposit_slip_no) {
        this.deposit_slip_no = deposit_slip_no;
    }
}
