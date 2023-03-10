package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.ClassAdapter;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class ClassListActivity extends DrawerActivity {
    View view;
    RecyclerView rvClassList;
    ClassAdapter classAdapter;
    ClassModel cm;
    ArrayList<ClassModel> cmList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_class_list);
        view = setActivityLayout(this, R.layout.activity_class_list);
        setToolbar("Select Class", this, false);
        init(view);
    }

    private void init(View view) {
        rvClassList = (RecyclerView) view.findViewById(R.id.rvClassList);
        rvClassList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvClassList.setLayoutManager(llm);
        cmList = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            cm = new ClassModel();
            cm.setName("Class 3");
            cmList.add(cm);
        }
        classAdapter = new ClassAdapter(this, cmList);
        rvClassList.setAdapter(classAdapter);

    }
}
