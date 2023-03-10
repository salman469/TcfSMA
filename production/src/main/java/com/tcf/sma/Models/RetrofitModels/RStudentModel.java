package com.tcf.sma.Models.RetrofitModels;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class RStudentModel {
    public int student_id;
    public String name;
    public String gender;
    public String date_of_birth;
    public String father_name;
    public String father_nic_no;
    public String date_of_admission;
    public String address;
    public String phone;
    public String mobile_no;
    public String gr_no;
    public int schoolclass_id;
    public int sec_class_id;
    public boolean is_withdrawl;
    public String withdrawl_date;
    public int withdrawal_reason_id;
    public boolean is_waittotransfer;


    public RStudentModel() {
    }

    public RStudentModel(int student_id, String name, String gender, String date_of_birth, String father_name, String father_nic_no, String date_of_admission, String address, String phone, String mobile_no, String gr_no, int schoolclass_id, int sec_class_id, boolean is_withdrawl, String withdrawl_date, int withdrawal_reason_id, boolean is_waittotransfer) {
        this.student_id = student_id;
        this.name = name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.father_name = father_name;
        this.father_nic_no = father_nic_no;
        this.date_of_admission = date_of_admission;
        this.address = address;
        this.phone = phone;
        this.mobile_no = mobile_no;
        this.gr_no = gr_no;
        this.schoolclass_id = schoolclass_id;
        this.sec_class_id = sec_class_id;
        this.is_withdrawl = is_withdrawl;
        this.withdrawl_date = withdrawl_date;
        this.withdrawal_reason_id = withdrawal_reason_id;
        this.is_waittotransfer = is_waittotransfer;
    }

    //Getters
    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getFather_nic_no() {
        return father_nic_no;
    }

    public void setFather_nic_no(String father_nic_no) {
        this.father_nic_no = father_nic_no;
    }

    public String getDate_of_admission() {
        return date_of_admission;
    }

    public void setDate_of_admission(String date_of_admission) {
        this.date_of_admission = date_of_admission;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    //Setters

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getGr_no() {
        return gr_no;
    }

    public void setGr_no(String gr_no) {
        this.gr_no = gr_no;
    }

    public int getSchoolclass_id() {
        return schoolclass_id;
    }

    public void setSchoolclass_id(int schoolclass_id) {
        this.schoolclass_id = schoolclass_id;
    }

    public int getSec_class_id() {
        return sec_class_id;
    }

    public void setSec_class_id(int sec_class_id) {
        this.sec_class_id = sec_class_id;
    }

    public boolean is_withdrawl() {
        return is_withdrawl;
    }

    public String getWithdrawl_date() {
        return withdrawl_date;
    }

    public void setWithdrawl_date(String withdrawl_date) {
        this.withdrawl_date = withdrawl_date;
    }

    public int getWithdrawal_reason_id() {
        return withdrawal_reason_id;
    }

    public void setWithdrawal_reason_id(int withdrawal_reason_id) {
        this.withdrawal_reason_id = withdrawal_reason_id;
    }

    public boolean is_waittotransfer() {
        return is_waittotransfer;
    }

    public void setIs_withdrawl(boolean is_withdrawl) {
        this.is_withdrawl = is_withdrawl;
    }

    public void setIs_waittotransfer(boolean is_waittotransfer) {
        this.is_waittotransfer = is_waittotransfer;
    }
}
