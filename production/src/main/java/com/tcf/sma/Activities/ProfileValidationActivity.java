package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.ProfileValidationAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class ProfileValidationActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public View view;
    Button btnSearch;
    TextView totalCount, unApprovedCount;
    RecyclerView rv_profilevalidationList;
    ProfileValidationAdapter profileValidationAdapter;
    ArrayList<EnrollmentModel> enrModel = new ArrayList<>();
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    Spinner SchoolSpinner, ClassSpinner, SectionSpinner, ClassSectionSpinner, spn_activity;
    int SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue;
    private EditText et_gr_no, et_student_Name;
    private LinearLayout llResults, ll_schoolSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_validation);
        view = setActivityLayout(this, R.layout.activity_profile_validation);
        setToolbar(getString(R.string.profile_validation), this, false);
        init(view);
        working();


    }

    private void working() {
        {
            llResults = (LinearLayout) view.findViewById(R.id.llResults);
            rv_profilevalidationList.setNestedScrollingEnabled(false);
            rv_profilevalidationList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv_profilevalidationList.setLayoutManager(llm);
//            profileValidationAdapter = new ProfileValidationAdapter(StudentModel.getInstance().getStudentsList(), this);
//            rv_profilevalidationList.setAdapter(profileValidationAdapter);
        }
        initSpinners();
    }

    private void initSpinners() {
// Used for Area Manager
//        SchoolModel schoolModel = new SchoolModel();
//        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllSchools());
//        schoolModel.getSchoolsList().add(0, new SchoolModel(0, getString(R.string.select_school));
//        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
//        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SchoolSpinner.setAdapter(SchoolAdapter);
//        SchoolSpinner.setOnItemSelectedListener(this);


        //Used for principal
        SchoolModel schoolModel = new SchoolModel();
        schoolModel = DatabaseHelper.getInstance(this).getSchoolById(AppModel.getInstance().getSelectedSchool(this));
        ArrayList<SchoolModel> smList = new ArrayList<>();
        smList.add(schoolModel);
        schoolModel.setSchoolsList(smList);
        SchoolSpinnerValue = schoolModel.getId();

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        SchoolSpinner.setOnItemSelectedListener(this);


        ClassModel classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, getString(R.string.select_class)));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);

        SectionModel sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, getString(R.string.select_section)));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);

        ClassSectionModel classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolModel.getId())));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);

//        spn_activity.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.schoolActivity)));
    }

//    private void setData() {
//        enrModel = DatabaseHelper.getInstance(this).getPendingEnrollments();
//        profileValidationAdapter.updateReceiptsList(enrModel);
//        totalCount.setText(profileValidationAdapter.getItemCount() + "");
//        setUnApprovedCount();
//    }

    public void init(View v) {
        rv_profilevalidationList = (RecyclerView) v.findViewById(R.id.rv_profile_validation);
        btnSearch = (Button) v.findViewById(R.id.btnsearch);
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
//        spn_activity=(Spinner)view.findViewById(R.id.spn_activity);
        et_gr_no = (EditText) view.findViewById(R.id.et_GrNo);
        et_gr_no.setVisibility(View.GONE);
        et_student_Name = (EditText) view.findViewById(R.id.et_student_name);
        et_student_Name.setVisibility(View.GONE);
        totalCount = (TextView) v.findViewById(R.id.tv_totat);

        unApprovedCount = (TextView) v.findViewById(R.id.tv_unapproved);
        btnSearch.setOnClickListener(this);

        ll_schoolSpinner = (LinearLayout) v.findViewById(R.id.ll_schoolSpinner);
//        ll_schoolSpinner.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsearch:
                if (ClassSectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_class_section)) &&
                        (et_gr_no.getText().toString().trim().length() == 0) &&
                        (et_student_Name.getText().toString().trim().length() == 0)) {
                    MessageBox("Please select search parameters!");
                    clearList();
                } else {
//                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudents());
                    StudentModel.getInstance().setStudentsList(
                            DatabaseHelper.getInstance(this).
                                    getAllSearchedUnapprovedStudents(AppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim()));
                    if (StudentModel.getInstance().getStudentsList().size() > 0) {
                        setAdapter();
                        setUnApprovedCount();
                    } else {
                        MessageBox("No results found");
                        clearList();
                    }
                }

//                if (ClassSpinner.getSelectedItem().toString().equals(getString(R.string.select_class)) &&
//                        (SectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_section))) &&
//                        (et_gr_no.getText().toString().trim().length() == 0) &&
//                        (et_student_Name.getText().toString().trim().length() == 0)) {
//                    MessageBox("Please select search parameters!");
//                    clearList();
//                } else {
////                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudents());
//                    StudentModel.getInstance().setStudentsList(
//                            DatabaseHelper.getInstance(this).
//                                    getAllSearchedUnapprovedStudents(SurveyAppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim()));
//                    if (StudentModel.getInstance().getStudentsList().size() > 0) {
//                        setAdapter();
//                        setUnApprovedCount();
//                    } else {
//                        MessageBox("No results found");
//                        clearList();
//                    }
//                }
                break;
        }
    }

    private void clearList() {
        if (profileValidationAdapter != null) {
            StudentModel.getInstance().setStudentsList(null);
            setAdapter();
            llResults.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

                break;
            case R.id.spn_section:
                SectionSpinnerValue = ((SectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
            case R.id.spn_class_name:
                ClassSpinnerValue = ((ClassModel) adapterView.getItemAtPosition(position)).getClassId();
                break;
            case R.id.spn_class_section_name:
                ClassSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                SectionSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUnApprovedCount();
        if (profileValidationAdapter != null) {
            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllSearchedUnapprovedStudents(AppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim()));
            setAdapter();
        }
    }

    public void setUnApprovedCount() {
        int count = DatabaseHelper.getInstance(this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(this));
        int unApproved = DatabaseHelper.getInstance(this).getAllUnapprovedStudentsCount(AppModel.getInstance().getSelectedSchool(this));
        totalCount.setText("" + count);
        unApprovedCount.setText("" + unApproved);
    }


    private void setAdapter() {
        profileValidationAdapter = new ProfileValidationAdapter(StudentModel.getInstance().getStudentsList(), this);
        rv_profilevalidationList.setAdapter(profileValidationAdapter);
        llResults.setVisibility(View.VISIBLE);


    }
}
