package com.tcf.sma.Activities.FeesCollection;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.CollectionReportAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.Fees_Collection.StudentCollectionReportModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class StudentCollectionReport extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    Button search;
    View view;
    List<StudentCollectionReportModel> scrm;
    LinearLayout llResults;
    private EditText et_gr_no;
    private RecyclerView rv_searchList;
    private ClassSectionModel classSectionModel;
    private ArrayAdapter<ClassSectionModel> classSectionAdapter;
    private Spinner classSectionSpinner, schoolSpinner;
    private int class_section_Id = 0, schoolId = 0;
    private int roleID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_collection_report);
        setToolbar(getString(R.string.collectionReport), this, false);

        init(view);
    }

    private void init(View view) {
        classSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
        schoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        search = (Button) view.findViewById(R.id.btnsearch);
        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_collectionList);
        et_gr_no = (EditText) view.findViewById(R.id.et_GrNo);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_searchList.setLayoutManager(llm);
        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setAdapter(new CollectionReportAdapter(this, scrm));
        llResults = (LinearLayout) view.findViewById(R.id.llResults);

        llResults.setVisibility(View.VISIBLE);
        search.setOnClickListener(this);

        initSpinners();

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            schoolSpinner.setEnabled(false);
        }

    }

    private void initSpinners() {

        List<SchoolModel> model = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);
//        List<SchoolModel> model = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();
        model.add(0, new SchoolModel(0, getString(R.string.select_school)));
        schoolSpinner.setAdapter(new ArrayAdapter<SchoolModel>(this, android.R.layout.simple_spinner_dropdown_item
                , model));
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(model, this);
        if (indexOfSelectedSchool > -1) {
            schoolSpinner.setSelection(indexOfSelectedSchool);
            schoolId = AppModel.getInstance().getSpinnerSelectedSchool(this);
        }
        schoolSpinner.setOnItemSelectedListener(this);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        classSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        classSectionSpinner.setAdapter(classSectionAdapter);
        classSectionSpinner.setOnItemSelectedListener(this);

    }


    private void getReportData() {
        AppModel.getInstance().showLoader(this);
        final AppInvoice db = AppInvoice.getInstance(this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    scrm = db.getStudentCollectionReport(schoolId);

                    if (class_section_Id != 0) {
                        List<StudentCollectionReportModel> filteredList = new ArrayList<>();
                        for (StudentCollectionReportModel model : scrm) {
                            if (model.getClassSectionName().toLowerCase().equals((
                                    (ClassSectionModel) classSectionSpinner.getSelectedItem()).getClass_section_name().toLowerCase()))
                                filteredList.add(model);
                        }
                        scrm = filteredList;
                    }

                    if (!et_gr_no.getText().toString().isEmpty()) {
                        List<StudentCollectionReportModel> filteredList = new ArrayList<>();
                        for (StudentCollectionReportModel model : scrm) {
                            if (model.getGrNo() == Integer.parseInt(et_gr_no.getText().toString().trim())) {
                                filteredList.add(model);
                            }
                        }
                        scrm = filteredList;
                    }

                    StudentCollectionReport.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv_searchList.setAdapter(new CollectionReportAdapter(StudentCollectionReport.this, scrm));
                            AppModel.getInstance().hideLoader();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:
                getReportData();
                break;
//                if (classSectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_class_section)) &&
//                        (et_gr_no.getText().toString().trim().length() == 0)) {
//                    MessageBox("Please select search parameters!");
//                    clearList();
//
//                } else {
////                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudents());
//                    StudentModel.getInstance().setStudentsList(
//                            DatabaseHelper.getInstance(this).
//                                    getAllSearchedStudentsInSchool(SurveyAppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), null));
//                    if (StudentModel.getInstance().getStudentsList().size() > 0) {
//
//                    } else {
//                        MessageBox("No results found");
//                        clearList();
//                    }
//
//                }
//                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_class_section_name:
                clearList();
                class_section_Id = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSchoolClassId();
                break;

            case R.id.spn_school:
                clearList();
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);

                classSectionModel = new ClassSectionModel();
                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(schoolId + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                classSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                classSectionSpinner.setAdapter(classSectionAdapter);
                classSectionSpinner.setOnItemSelectedListener(this);

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void clearList() {
        if (scrm != null) {
            scrm.clear();
            rv_searchList.setAdapter(new CollectionReportAdapter(StudentCollectionReport.this, scrm));
        }
    }
}