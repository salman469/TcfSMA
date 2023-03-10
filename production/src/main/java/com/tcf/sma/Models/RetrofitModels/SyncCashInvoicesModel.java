package com.tcf.sma.Models.RetrofitModels;

public class SyncCashInvoicesModel {
    private int id;
    private int schoolclass_id;
    private int school_year_id;
    private int student_id;
    private String created_from;
    private int created_by;
    private String created_on;
    private String uploaded_on;
    private double fees_admission;
    private double fees_exam;
    private double fees_tution;
    private double fees_books;
    private double fees_copies;
    private double fees_uniform;
    private double fees_others;
    private String device_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
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
}
