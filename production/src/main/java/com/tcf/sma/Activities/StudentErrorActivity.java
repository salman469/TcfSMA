package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.StudentErrorLogAdapter;
import com.tcf.sma.Helpers.DbTables.FeesCollection.ErrorLog;
import com.tcf.sma.R;

public class StudentErrorActivity extends DrawerActivity {


    RecyclerView rv_studentErrorLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = setActivityLayout(this, R.layout.activity_student_error_log);
        setToolbar("Student Error Log", this, false);
        rv_studentErrorLog = (RecyclerView) view.findViewById(R.id.rv_studentErrorLog);
        rv_studentErrorLog.setLayoutManager(new LinearLayoutManager(this));

        rv_studentErrorLog.setAdapter(new StudentErrorLogAdapter(ErrorLog.getInstance(this).getFailedRecords()
                , this));


    }
}
