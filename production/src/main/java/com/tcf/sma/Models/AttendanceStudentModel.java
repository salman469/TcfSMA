package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/3/2017.
 */

public class AttendanceStudentModel {
    @SerializedName("attendance_id")
    private int attendanceId;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("is_absent")
    private boolean isAbsent;
    @SerializedName("reason")
    private String reason;
    private String fordate_attendance;


    private ArrayList<AttendanceStudentModel> asmList;
    private AttendanceStudentModel asModel;

    //Getters

    public String getFordate_attendance() {
        return fordate_attendance;
    }

    public void setFordate_attendance(String fordate_attendance) {
        this.fordate_attendance = fordate_attendance;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    //Setters

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    //Methods
    public ArrayList<AttendanceStudentModel> getAsmList() {
        return asmList;
    }

    public void setAsmList(ArrayList<AttendanceStudentModel> asmList) {
        this.asmList = asmList;
    }

    public AttendanceStudentModel getAsModel() {
        return asModel;
    }

    public void setAsModel(AttendanceStudentModel asModel) {
        this.asModel = asModel;
    }

    //Parsing Methods
    public ArrayList<AttendanceStudentModel> parseArray(String json) {
        asmList = new ArrayList<>();
        asModel = null;

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("attendance_student");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("attendance_id"))
                    asModel.setAttendanceId(childObject.getInt("attendance_id"));
                if (childObject.has("student_id"))
                    asModel.setStudentId(childObject.getInt("student_id"));
                if (childObject.has("IsAbsent"))
                    asModel.setAbsent(childObject.getBoolean("IsAbsent"));
                if (childObject.has("Reason"))
                    asModel.setReason(childObject.getString("Reason"));
                asmList.add(asModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return asmList;
    }

    public AttendanceStudentModel parseObject(String json) {
        asModel = new AttendanceStudentModel();
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("attendance_id"))
                asModel.setAttendanceId(childObject.getInt("attendance_id"));
            if (childObject.has("student_id"))
                asModel.setStudentId(childObject.getInt("student_id"));
            if (childObject.has("IsAbsent"))
                asModel.setAbsent(childObject.getBoolean("IsAbsent"));
            if (childObject.has("Reason"))
                asModel.setReason(childObject.getString("Reason"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asModel;
    }
}
