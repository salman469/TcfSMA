package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.SearchAdapter;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SearchModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class SearchActivity extends DrawerActivity implements View.OnClickListener {
    View view;
    Button Search;
    Spinner SchoolSpinner, ClassSpinner, SectionSpinner;
    ArrayAdapter<String> SchoolAdapter, ClassAdapter, SectionAdapter;
    EditText et_gr_no, et_student_Name;
    RecyclerView rv_searchList;
    ArrayList<SearchModel> searchList = new ArrayList<>();
    SearchAdapter searchAdapter;
    SearchModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search2);
        view = setActivityLayout(this, R.layout.activity_search);

        if (AppModel.getInstance().fragmentTag.equals(getString(R.string.student_profile_text)))
            setToolbar(getString(R.string.student_profile_text), this, false);
        else if (AppModel.getInstance().fragmentTag.equals(getString(R.string.student_dropout_text)))
            setToolbar(getString(R.string.student_dropout_text), this, false);
        init(view);
        working();
    }

    private void init(View view) {
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        Search = (Button) view.findViewById(R.id.btnsearch);
        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);
        et_gr_no = (EditText) view.findViewById(R.id.et_GrNo);
        et_student_Name = (EditText) view.findViewById(R.id.et_student_name);
        Search.setOnClickListener(this);
    }

    private void working() {


        {
            rv_searchList.setNestedScrollingEnabled(false);
            rv_searchList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv_searchList.setLayoutManager(llm);
            // setAdapter();

        }
        {
            SchoolAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.schools));
            SchoolSpinner.setAdapter(SchoolAdapter);

            ClassAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.classes));
            ClassSpinner.setAdapter(ClassAdapter);

            SectionAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.section));
            SectionSpinner.setAdapter(SectionAdapter);
        }
    }

//    private void setAdapter() {
//        if (rv_searchList != null) {
//
//                searchAdapter = new SearchAdapter(searchList, this);
//                rv_searchList.setAdapter(searchAdapter);
//
//            } else if (SurveyAppModel.getInstance().fragmentTag.equals(getString(R.string.attendance_text))) {
//
//                ArrayList<AttendanceModel> attendanceModelArrayList = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    AttendanceModel model = new AttendanceModel();
//                    model.setGr_no("" + i);
//                    model.setStudentName("Zubair Soomro ");
//                    model.setFatherName("Mumtaz Ali Soomro ");
//                    attendanceModelArrayList.add(model);
//                }
//                rv_searchList.setAdapter(new AttendanceAdapter(attendanceModelArrayList, this));
//
//            } else if (SurveyAppModel.getInstance().fragmentTag.equals(getString(R.string.promotions))) {
//
//                ArrayList<PromotionModel> promtionList = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    PromotionModel model = new PromotionModel();
//                    model.setGr_no("" + i);
//                    model.setStudent_name("Badar Jamal");
//                    model.setStudent_status("Promoted to next class");
//                    promtionList.add(model);
//                }
//                rv_searchList.setAdapter(new PromotionAdapter(promtionList, this));
//
//                searchAdapter = new SearchAdapter(searchList, this);
//                rv_searchList.setAdapter(searchAdapter);
//            }
//        }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:
                searchList.clear();
                for (int i = 0; i <= 12; i++) {
                    model = new SearchModel();
                    model.setGr_no("1265" + i);
                    model.setStudent_name("Zubair Soomro ");
                    model.setSchool_name("The Educators ");
                    model.setClass_name("" + i);
                    model.setSection("A");
                    searchList.add(model);
                }
                if (searchAdapter != null) {
                    searchAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        setAdapter();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        RecyclerView.Adapter adapter;
//        adapter = rv_searchList.getAdapter();
//        if (adapter != null) {
//            adapter = null;
//        }
//
//
//    }
}
