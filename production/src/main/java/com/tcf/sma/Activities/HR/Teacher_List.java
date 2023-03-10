package com.tcf.sma.Activities.HR;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HR.Teacher_List_Adapter;
import com.tcf.sma.R;

public class Teacher_List extends DrawerActivity {
    RecyclerView rv_teacherDetail;
    Teacher_List_Adapter teacherAdapter;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_teacher_list);
        setToolbar("Teacher List", this, false);
        init(view);
        rv_teacherDetail.setHasFixedSize(true);
        rv_teacherDetail.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_teacherDetail.setLayoutManager(linearLayoutManager);
        rv_teacherDetail.setItemAnimator(new DefaultItemAnimator());
        rv_teacherDetail.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        teacherAdapter = new Teacher_List_Adapter();
        rv_teacherDetail.setAdapter(teacherAdapter);
    }

    private void init(View view) {
        rv_teacherDetail = (RecyclerView) view.findViewById(R.id.rv_teacherDetail);
    }
}
