package com.tcf.sma.Survey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.util.ArrayList;
import java.util.List;

public class CourseSelector extends DrawerActivity {
    View view;
    private EmployeeModel employeeModel;
    private ClassSectionModel classSectionModel;
    private ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
//    private Spinner ClassSectionSpinner;
    private int schoolId;
    public TextView tv_regionName, tv_areaName, tv_schoolName;
    private List<EmployeeModel> employeeModels;
//    private RecyclerView rv_tctEntry;
    private ListView csList;

//    private TCTEntryAdapter tctEntryAdapter;
//    private ArrayAdapter listAdapter;
//    private ArrayList<String> subList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_course_selector);

        view = setActivityLayout(this, R.layout.activity_course_selector);
        setToolbar("Course Selector", this, false);
        init(view);

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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (tctEntryAdapter != null){
//            employeeModels = EmployeeHelperClass.getInstance(this).getEmployees(schoolId,null,null,null);
//            setAdapter();
//        }
//    }

//    private void setAdapter() {
//        if (employeeModels != null && employeeModels.size() > 0) {
//            tctEntryAdapter = new TCTEntryAdapter(CourseSelector.this,employeeModels,subList);
//            rv_tctEntry.setAdapter(tctEntryAdapter);
//        } else {
//
//        }
//    }

    public void init(View view){

        tv_regionName = view.findViewById(R.id.tv_regionName);
        tv_areaName = view.findViewById(R.id.tv_areaName);
        tv_schoolName = view.findViewById(R.id.tv_schoolName);


        Intent intent = getIntent();
        if(intent.hasExtra("empDetailId")){
//            int emp = intent.get("empDetailId");
            employeeModel = EmployeeHelperClass.getInstance(this).getEmployee(intent.getIntExtra("empDetailId",0));
        }

        if(intent.hasExtra("schoolId")) {
            schoolId = intent.getIntExtra("schoolId",0);
            SchoolModel schoolModel = DatabaseHelper.getInstance(CourseSelector.this).getSchoolById(schoolId);
            tv_schoolName.setText(schoolModel.getName());
            ArrayList<SchoolExpandableModel> schoolExpandableModels = DatabaseHelper.getInstance(this).getSchoolByAreaRegionFromQuery();
            tv_areaName.setText(schoolExpandableModels.get(0).getArea());
            tv_regionName.setText(schoolExpandableModels.get(0).getArea());
        }


//        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
        csList = view.findViewById(R.id.csList);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getPresentClassSectionBySchoolIdForPromotion(String.valueOf(schoolId)));
//        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classSectionModel.getClassAndSectionsList());
//        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        csList.setAdapter(ClassSectionAdapter);
        csList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(CourseSelector.this, SurveyAppModel.getInstance().selectedProject.popActivityFromStack());
                    intent.putExtra("empDetailId", getIntent().getIntExtra("empDetailId",0));
                    intent.putExtra("schoolId", schoolId);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        ClassSectionSpinner.setOnItemSelectedListener(this);


//        tv_regionName = view.findViewById(R.id.tv_regionName);
//        tv_areaName = view.findViewById(R.id.tv_areaName);

//        rv_tctEntry = view.findViewById(R.id.rv_tctEntry);
//        rv_tctEntry.setNestedScrollingEnabled(false);
//        rv_tctEntry.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        rv_tctEntry.setLayoutManager(linearLayoutManager);

//        subList = new ArrayList<>();
//        subList.add("Select Sub.");
//        subList.add("Physics");
//        subList.add("Maths");
//        subList.add("Computer Sc.");
//        subList.add("Chemistry");
//        subList.add("Biology");
//        subList.add("Urdu");
//        subList.add("English");
//        subList.add("Arabic");

    }

}
