package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.StudentAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

public class PromotionSearchActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View view;
    Spinner SchoolSpinner, ClassSpinner, SectionSpinner;
    int SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    Button Search;
    EditText et_gr_no, et_student_Name;
    RecyclerView rv_searchList;
    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_search);
        setToolbar(getString(R.string.promotions), this, false);

        init(view);
        working();
    }

    private void init(View view) {
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        Search = (Button) view.findViewById(R.id.btnsearch);
        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);
        et_gr_no = (EditText) view.findViewById(R.id.et_GrNo);
        et_student_Name = (EditText) view.findViewById(R.id.et_student_name);
        Search.setOnClickListener(this);
        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);
    }

    private void working() {
        initSpinners();
//        setAdapter();
    }


    /**
     * Method to initialize spinners with Meta Data
     * Created by Haseeb
     */
    private void initSpinners() {

        SchoolModel schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
        schoolModel.getSchoolsList().add(0, new SchoolModel(0, "Select School"));
        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        SchoolSpinner.setOnItemSelectedListener(this);

        ClassModel classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, "Select Class"));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);

        SectionModel sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, "Select Section"));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);
    }

    private void setAdapter() {
        if (rv_searchList != null) {
            studentAdapter = new StudentAdapter(StudentModel.getInstance().getStudentsList(), this, 1, ClassSpinnerValue, SectionSpinnerValue);
            rv_searchList.setAdapter(studentAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:
//                studentsList.clear();
//                if (StudentModel.getInstance().getStudentsList() != null)
//                    StudentModel.getInstance().getStudentsList().clear();
                // a list of students will be fetched in response either from api or from internal DB
                //set that list in adapter

//
//                for (int i = 0; i <= 12; i++) {
//                    studentModel = new StudentModel();
//                    studentModel.setName("Zubair Soomro ");
//                    studentModel.setGender("Male");
//                    studentModel.setGrNo("1265" + i);
//                    studentModel.setEnrollmentDate("1 May 2010");
//                    studentModel.setCurrentClass(i);
//                    studentModel.setCurrentSection("A");
//                    studentModel.setDob("16 April 1996");
//                    studentModel.setFormB("12345-1234567-8");
//                    studentModel.setFathersName("Saqib Ahmad");
//                    studentModel.setFatherNic("12345-1234567-8");
//                    studentModel.setFatherOccupation("Doctor");
//                    studentModel.setMotherName("Sania Ahmad");
//                    studentModel.setMotherNic("12345-1234567-8");
//                    studentModel.setMotherOccupation("House wife");
//                    studentModel.setGuardianName("Sania Ahmad");
//                    studentModel.setGuardianNic("12345-1234567-8");
//                    studentModel.setGuardianOccupation("House wife");
//                    studentModel.setPreviousSchoolName("The Educators ");
//                    studentModel.setPreviousSchoolClass("4");
//                    studentModel.setAddress1("Nazimabad ,Karachi, Pakistan");
//                    studentModel.setContactNumbers("0335-5698741");
//                    studentsList.add(studentModel);
//                }

//                StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudents(SurveyAppModel.getInstance().getSelectedSchool(this)));
                studentAdapter = new StudentAdapter(StudentModel.getInstance().getStudentsList(), this, 1, ClassSpinnerValue, SectionSpinnerValue);
                rv_searchList.setAdapter(studentAdapter);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                break;
            case R.id.spn_section:
                SectionSpinnerValue = ((SectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
            case R.id.spn_class_name:
                ClassSpinnerValue = ((ClassModel) adapterView.getItemAtPosition(position)).getClassId();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (studentAdapter != null) {
//            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudents());
            studentAdapter = new StudentAdapter(StudentModel.getInstance().getStudentsList(), this, 1, ClassSpinnerValue, SectionSpinnerValue);
            rv_searchList.setAdapter(studentAdapter);
//            studentAdapter.notifyDataSetChanged();

        }
    }
}
