package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 3/6/2017.
 */

public class EnrollmentUploadModel {

    int id;
    int gr_no;
    int school_id;
    int created_by;
    int modified_by;
    String created_on;
    String device_udid;
    String student_name;
    String status;
    String student_id;
    String modified_on;
    transient int school_class_id;
    @SerializedName("gender")
    String student_gender;
    int student_class;
    int student_section;
    int class_section_id;
    ArrayList<EnrollmentImageUploadModel> Enrollment_Image;
    private int server_id;
    private int monthlyfee;

    public EnrollmentUploadModel() {
    }

    public EnrollmentUploadModel(int id, int gr_no, int school_id, int created_by, String created_on, String device_udid, ArrayList<EnrollmentImageUploadModel> enrollment_Image) {
        this.id = id;
        this.gr_no = gr_no;
        this.school_id = school_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.device_udid = device_udid;
        Enrollment_Image = enrollment_Image;
    }
    public EnrollmentUploadModel(int id, int server_id, int gr_no, int school_id, int created_by, String created_on, String device_udid,
                                 ArrayList<EnrollmentImageUploadModel> enrollment_Image, String student_name, int student_class, int student_section, int class_section_id, String student_id, int school_class_id) {
        this.id = id;
        this.server_id = server_id;
        this.gr_no = gr_no;
        this.school_id = school_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.device_udid = device_udid;
        this.student_name = student_name;
        this.student_class = student_class;
        this.student_section = student_section;
        this.class_section_id = class_section_id;
        Enrollment_Image = enrollment_Image;
        this.student_id = student_id;
        this.school_class_id = school_class_id;
    }

    public EnrollmentUploadModel(int id, int server_id, int gr_no, int school_id, int created_by, String created_on, String device_udid,
                                 ArrayList<EnrollmentImageUploadModel> enrollment_Image, String student_name, int student_class,
                                 int student_section, int class_section_id, String student_id, int school_class_id, String student_gender) {
        this.id = id;
        this.server_id = server_id;
        this.gr_no = gr_no;
        this.school_id = school_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.device_udid = device_udid;
        this.student_name = student_name;
        this.student_class = student_class;
        this.student_section = student_section;
        this.class_section_id = class_section_id;
        Enrollment_Image = enrollment_Image;
        this.student_id = student_id;
        this.school_class_id = school_class_id;
        this.student_gender = student_gender;
    }

    public EnrollmentUploadModel(int id, int server_id, int gr_no, int school_id, int created_by, String created_on, String device_udid,
                                 ArrayList<EnrollmentImageUploadModel> enrollment_Image, String student_name, int student_class,
                                 int class_section_id, String student_id, int school_class_id, String student_gender,
                                 int monthly_fee, int modified_by, String modified_on) {
        this.id = id;
        this.server_id = server_id;
        this.gr_no = gr_no;
        this.school_id = school_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.device_udid = device_udid;
        this.student_name = student_name;
        this.student_class = student_class;
        this.student_section = student_section;
        this.class_section_id = class_section_id;
        Enrollment_Image = enrollment_Image;
        this.student_id = student_id;
        this.school_class_id = school_class_id;
        this.student_gender = student_gender;
        this.monthlyfee = monthly_fee;
        this.modified_by = modified_by;
        this.modified_on = modified_on;
    }

    public int getMonthlyfee() {
        return monthlyfee;
    }

    public void setMonthlyfee(int monthlyfee) {
        this.monthlyfee = monthlyfee;
    }

    //Constructor

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public int getStudent_class() {
        return student_class;
    }

    public void setStudent_class(int student_class) {
        this.student_class = student_class;
    }

    public int getStudent_section() {
        return student_section;
    }

    public void setStudent_section(int student_section) {
        this.student_section = student_section;
    }

    public int getClass_section_id() {
        return class_section_id;
    }

    public void setClass_section_id(int class_section_id) {
        this.class_section_id = class_section_id;
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getGr_no() {
        return gr_no;
    }

    public void setGr_no(int gr_no) {
        this.gr_no = gr_no;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
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

    public String getDevice_udid() {
        return device_udid;
    }

    public void setDevice_udid(String device_udid) {
        this.device_udid = device_udid;
    }

    public ArrayList<EnrollmentImageUploadModel> getEnrollment_Image() {
        return Enrollment_Image;
    }

    public void setEnrollment_Image(ArrayList<EnrollmentImageUploadModel> enrollment_Image) {
        Enrollment_Image = enrollment_Image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent_gender() {
        return student_gender;
    }

    public void setStudent_gender(String student_gender) {
        this.student_gender = student_gender;
    }
}
