package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.StudentAttendanceReportAdapter;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceReportDialogManager extends Dialog {

    private Activity activity;

    private RecyclerView rv_attendanceList;
    private StudentAttendanceReportAdapter studentAttendanceReportAdapter;
    private List<StudentModel> smList;
    private int totalWorkingDays;

//    public StudentAttendanceReportDialogManager(Activity activity, List<StudentModel> smList) {
//        super(activity);
//        this.activity = activity;
//        this.smList=smList;
//    }

    public StudentAttendanceReportDialogManager(Activity activity, ArrayList<StudentModel> smList, int totalWorkingDays) {
        super(activity);
        this.activity = activity;
        this.smList = smList;
        this.totalWorkingDays = totalWorkingDays;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_student_attendance_report);
        init();
        working();
    }

    private void working() {
        studentAttendanceReportAdapter = new StudentAttendanceReportAdapter(activity, smList, totalWorkingDays);
        rv_attendanceList.setAdapter(studentAttendanceReportAdapter);
    }

    void init() {
        rv_attendanceList = (RecyclerView) findViewById(R.id.rv_attendanceList);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_attendanceList.setLayoutManager(llm);
        rv_attendanceList.setHasFixedSize(true);
    }
}
