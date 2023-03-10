package com.tcf.sma.Models.Fees_Collection;

/**
 * Created By Mohammad Haseeb
 */
public class AccountStatementModelNewStructure {

    private int id;
    private int sysId;
    private int schoolclass_id;
    private int school_year_id;
    private int student_id;
    private String receiptNo;
    private String transaction_type;
    private int transaction_typeId;
    private int transaction_categoryId;
    private String created_from;
    private String created_on;
    private double fees_admission;
    private double fees_exam;
    private double fees_tution;
    private double fees_books;
    private double fees_copies;
    private String grNo;
    private double fees_uniform;
    private double fees_others;
    private int deposit_slip_no;
    private String deposit_date;
    private String studentName;
    private String fatherName;
    private String className;
    private String section;
    public AccountStatementModelNewStructure() {
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getGrNo() {
        return grNo;
    }

    public void setGrNo(String grNo) {
        this.grNo = grNo;
    }

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

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getCreated_from() {
        return created_from;
    }

    public void setCreated_from(String created_from) {
        this.created_from = created_from;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
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

    public int getDeposit_slip_no() {
        return deposit_slip_no;
    }

    public void setDeposit_slip_no(int deposit_slip_no) {
        this.deposit_slip_no = deposit_slip_no;
    }

    public String getDeposit_date() {
        return deposit_date;
    }

    public void setDeposit_date(String deposit_date) {
        this.deposit_date = deposit_date;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getTransaction_categoryId() {
        return transaction_categoryId;
    }

    public void setTransaction_categoryId(int transaction_categoryId) {
        this.transaction_categoryId = transaction_categoryId;
    }

    public int getTransaction_typeId() {
        return transaction_typeId;
    }

    public void setTransaction_typeId(int transaction_typeId) {
        this.transaction_typeId = transaction_typeId;
    }
}
