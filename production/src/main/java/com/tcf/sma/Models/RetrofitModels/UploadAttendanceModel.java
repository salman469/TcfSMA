package com.tcf.sma.Models.RetrofitModels;

import com.google.gson.annotations.SerializedName;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendanceStudentModel;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/20/2017.
 */

public class UploadAttendanceModel {
    @SerializedName("attendance")
    private AttendanceModel am;
    @SerializedName("attendance_students")
    private ArrayList<AttendanceStudentModel> asmList;
    private ArrayList<UploadAttendanceModel> utmList;

    public UploadAttendanceModel() {
    }

    public UploadAttendanceModel(AttendanceModel am, ArrayList<AttendanceStudentModel> asmList) {
        this.am = am;
        this.asmList = asmList;
    }

    public AttendanceModel getAm() {
        return am;
    }

    public void setAm(AttendanceModel am) {
        this.am = am;
    }

    public ArrayList<AttendanceStudentModel> getAsmList() {
        return asmList;
    }

    public void setAsmList(ArrayList<AttendanceStudentModel> asmList) {
        this.asmList = asmList;
    }

    public ArrayList<UploadAttendanceModel> getUtmList() {
        return utmList;
    }

    public void setUtmList(ArrayList<UploadAttendanceModel> utmList) {
        this.utmList = utmList;
    }
}
