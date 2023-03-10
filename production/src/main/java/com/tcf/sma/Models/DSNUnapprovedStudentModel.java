package com.tcf.sma.Models;

/**
 * Created by Badar Arain on 3/27/2017.
 */

public class DSNUnapprovedStudentModel {
    int studetn_grno, unapproved_count = 0;
    String Student_name, student_class_name;

    public DSNUnapprovedStudentModel() {
    }

    public int getStudetn_grno() {
        return studetn_grno;
    }

    public void setStudetn_grno(int studetn_grno) {
        this.studetn_grno = studetn_grno;
    }

    public int getUnapproved_count() {
        return unapproved_count;
    }

    public void setUnapproved_count(int unapproved_count) {
        this.unapproved_count = unapproved_count;
    }

    public String getStudent_name() {
        return Student_name;
    }

    public void setStudent_name(String student_name) {
        Student_name = student_name;
    }

    public String getStudent_class_name() {
        return student_class_name;
    }

    public void setStudent_class_name(String student_class_name) {
        this.student_class_name = student_class_name;
    }
}
