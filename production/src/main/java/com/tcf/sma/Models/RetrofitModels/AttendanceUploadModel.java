package com.tcf.sma.Models.RetrofitModels;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 3/6/2017.
 */

public class AttendanceUploadModel {

    int id;
    int server_id;
    int schoolclass_id;
    int created_by;
    int modified_by;
    String for_date;
    String created_on;
    String device_udid;
    String modified_on;
    ArrayList<AttendanceStudentUploadModel> Student_Attendance;

    //Constructor
    public AttendanceUploadModel() {
    }

    public AttendanceUploadModel(int id, int schoolclass_id, int created_by, String for_date, String created_on, String device_udid, ArrayList<AttendanceStudentUploadModel> student_Attendance) {
        this.id = id;
        this.schoolclass_id = schoolclass_id;
        this.created_by = created_by;
        this.for_date = for_date;
        this.created_on = created_on;
        this.device_udid = device_udid;
        Student_Attendance = student_Attendance;
    }

    public AttendanceUploadModel(int id, int server_id, int schoolclass_id, int created_by, String for_date, String created_on, String device_udid, ArrayList<AttendanceStudentUploadModel> student_Attendance, String modified_on, int modified_by) {
        this.id = id;
        this.server_id = server_id;
        this.schoolclass_id = schoolclass_id;
        this.created_by = created_by;
        this.for_date = for_date;
        this.created_on = created_on;
        this.device_udid = device_udid;
        this.modified_by = modified_by;
        this.modified_on = modified_on;
        Student_Attendance = student_Attendance;
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

    public int getSchoolclass_id() {
        return schoolclass_id;
    }

    public void setSchoolclass_id(int schoolclass_id) {
        this.schoolclass_id = schoolclass_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getFor_date() {
        return for_date;
    }

    public void setFor_date(String for_date) {
        this.for_date = for_date;
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

    public ArrayList<AttendanceStudentUploadModel> getStudent_Attendance() {
        return Student_Attendance;
    }

    public void setStudent_Attendance(ArrayList<AttendanceStudentUploadModel> student_Attendance) {
        Student_Attendance = student_Attendance;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }
}
