package com.tcf.sma.Survey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.SchoolExpandableAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.util.ArrayList;

import androidx.appcompat.widget.SearchView;


public class SelectSchoolActivity extends DrawerActivity {
    ExpandableListView el_schools;
    SchoolExpandableAdapter adapter;
    ArrayList<SchoolExpandableModel> schoolExpandableModels;
    SearchView sv_school;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_school);
        setActivityLayout(this, R.layout.activity_select_school);
        setToolbar("Select School", this, false);

        init();
    }

    @Override
    public void onBackPressed() {
        try {
            SurveyAppModel.projects.get(0).classStack.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
//        Intent intent = new Intent(this, ProjectsActivity.class);
//        startActivity(intent);
        finish();
    }

    private void init() {
        sv_school = findViewById(R.id.sv_school);
        el_schools = findViewById(R.id.el_schoollist);

        schoolExpandableModels = DatabaseHelper.getInstance(this).getSchoolByAreaRegionFromQuery();

        adapter = new SchoolExpandableAdapter(this, schoolExpandableModels, v -> {
//            try {
//                startActivity(new Intent(SelectSchoolActivity.this, SurveyAppModel.getInstance().selectedProject.popActivityFromStack())
//                        .putExtra("sm", schoolExpandableModels.get(0).getSchoolModels(adapter.get)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }, true);
        el_schools.setAdapter(adapter);

        for (SchoolExpandableModel sm : schoolExpandableModels) {
            el_schools.expandGroup(schoolExpandableModels.indexOf(sm));
        }

        sv_school.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.filterData(query);
                return false;
            }
        });

    }
}
