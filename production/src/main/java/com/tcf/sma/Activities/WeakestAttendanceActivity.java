package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.BottomFiveAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeakestAttendanceActivity extends AppCompatActivity {
    RecyclerView rvWeakestAttendance;
    List<BottomFiveStudentsModel> modelList;
    BottomFiveAdapter bottomFiveAdapter;
    String schoolId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weakest_attendance_list);
        handleIntent(getIntent());
        init();
        setRvWeakestAttendanceList();

    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("schoolId")) {
                schoolId = intent.getStringExtra("schoolId");
            }
        }
    }

    private void init() {
        rvWeakestAttendance = (RecyclerView) findViewById(R.id.rvWeakestAttendance);
        rvWeakestAttendance.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvWeakestAttendance.setLayoutManager(llm);


//        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
//        adapter = new SchoolAdapter(this, schoolModel.getSchoolsList());
//        rvWeakestAttendance.setAdapter(adapter);

    }

    private void setRvWeakestAttendanceList() {
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        String month, year;
        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        modelList = DatabaseHelper.getInstance(this).getAllBottomStudent(schoolId, month, year);
        if (modelList != null && modelList.size() != 0) {
            bottomFiveAdapter = new BottomFiveAdapter((ArrayList<BottomFiveStudentsModel>) modelList, this);
            rvWeakestAttendance.setAdapter(bottomFiveAdapter);
        }
    }

}
