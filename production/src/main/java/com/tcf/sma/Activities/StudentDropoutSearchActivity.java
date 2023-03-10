package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Adapters.StudentAdapter;
import com.tcf.sma.Adapters.StudentAutoCompleteAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentDropoutSearchActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    final String filterStdId = "stdId", filterStdGrno = "stdGrno",
            filterStdName = "stdName", filterStdDOA = "stdDOA";
    public TextView tv_total_students;
    View view;
    LinearLayout llResults, ll_schoolSpinner;
    Spinner SchoolSpinner, ClassSpinner, SectionSpinner, ClassSectionSpinner, spn_activity;
    int SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    Button Search;
    EditText et_gr_no, et_student_Name;
    RecyclerView rv_searchList;
    StudentAdapter studentAdapter;
    ImageView iv_stdId, iv_grno, iv_stdname, iv_admDate, iv_classSection;
    LinearLayout ll_stdId, ll_grno, ll_stdname, ll_admDate;
    private String filterBy;
    private AutoCompleteTextView et_std_name_grNo;
    private StudentAutoCompleteAdapter studentAutocompleteAdapter;
    private String stdGrno = "", stdName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_search2);
        setToolbar(getString(R.string.student_dropout_text), this, false);

        init(view);
        working();
    }

    private void init(View view) {
        llResults = view.findViewById(R.id.llResults);

        SchoolSpinner = view.findViewById(R.id.spn_school);
        SectionSpinner = view.findViewById(R.id.spn_section);
        ClassSpinner = view.findViewById(R.id.spn_class_name);
        ClassSectionSpinner = view.findViewById(R.id.spn_class_section_name);
//        spn_activity = (Spinner)view.findViewById(R.id.spn_activity);
        Search = view.findViewById(R.id.btnsearch);
        rv_searchList = view.findViewById(R.id.rv_searchList);
        et_gr_no = view.findViewById(R.id.et_GrNo);
        et_student_Name = view.findViewById(R.id.et_student_name);
        Search.setOnClickListener(this);
        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

        ll_schoolSpinner = view.findViewById(R.id.ll_schoolSpinner);
        ll_schoolSpinner.setVisibility(View.VISIBLE);

        tv_total_students = view.findViewById(R.id.tv_total_students);

        iv_stdId = view.findViewById(R.id.iv_stdId);
        iv_grno = view.findViewById(R.id.iv_grno);
        iv_stdname = view.findViewById(R.id.iv_stdname);
        iv_admDate = view.findViewById(R.id.iv_admDate);
        iv_classSection = view.findViewById(R.id.iv_classSection);
        iv_classSection.setVisibility(View.GONE);

        ll_stdId = view.findViewById(R.id.ll_stdId);
        ll_grno = view.findViewById(R.id.ll_grno);
        ll_stdname = view.findViewById(R.id.ll_stdname);
        ll_admDate = view.findViewById(R.id.ll_admDate);

        ll_stdId.setOnClickListener(this);
        ll_grno.setOnClickListener(this);
        ll_stdname.setOnClickListener(this);
        ll_admDate.setOnClickListener(this);

        et_std_name_grNo = view.findViewById(R.id.et_std_name_grNo);

        setCollapsing(false);
    }

    private void working() {
        initSpinners();
//        setAdapter();
    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(SchoolSpinnerValue);
        studentAutocompleteAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
                studentsList);
        et_std_name_grNo.setAdapter(studentAutocompleteAdapter);
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    /**
     * Method to initialize spinners with Meta Data
     * Created by Haseeb
     */
    private void initSpinners() {

        SchoolModel schoolModel = new SchoolModel();
        schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
        schoolModel.getSchoolsList().add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);
        SchoolSpinner.setOnItemSelectedListener(this);
        SchoolSpinnerValue = ((SchoolModel) SchoolSpinner.getSelectedItem()).getId();

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

        ClassSectionModel classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);

        populateStudentAutocompleteAdapter();
        et_std_name_grNo.setOnItemClickListener(this);

        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    stdGrno = "";
                    stdName = "";
                }
            }
        });

//        spn_activity.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.schoolActivity)));
    }

    private void setAdapter() {
        if (!CollectionUtils.isEmpty(StudentModel.getInstance().getStudentsList())) {
            studentAdapter = new StudentAdapter(StudentModel.getInstance().getStudentsList(), this, 1, ClassSpinnerValue, SectionSpinnerValue);
            rv_searchList.setAdapter(studentAdapter);
            llResults.setVisibility(View.VISIBLE);
        }

    }

    private void clearList() {
        if (studentAdapter != null) {
            StudentModel.getInstance().setStudentsList(null);
            setAdapter();
            llResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:
                searchClicked();
                break;
            case R.id.ll_stdId:
                filterBy = filterStdId;
                if (iv_stdId.getDrawable().getConstantState() ==
                        iv_stdId.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_stdId.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscStudent();
                } else {
                    iv_stdId.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscStudent();
                }
                break;
            case R.id.ll_grno:
                filterBy = filterStdGrno;
                if (iv_grno.getDrawable().getConstantState() ==
                        iv_grno.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_grno.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscStudent();
                } else {
                    iv_grno.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscStudent();
                }
                break;
            case R.id.ll_stdname:
                filterBy = filterStdName;
                if (iv_stdname.getDrawable().getConstantState() ==
                        iv_stdname.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_stdname.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscStudent();
                } else {
                    iv_stdname.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscStudent();
                }
                break;
            case R.id.ll_admDate:
                filterBy = filterStdDOA;
                if (iv_admDate.getDrawable().getConstantState() ==
                        iv_admDate.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_admDate.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscStudent();
                } else {
                    iv_admDate.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscStudent();
                }
                break;
        }

    }

    private void searchClicked() {
        if (ClassSectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_class_section)) &&
                (et_gr_no.getText().toString().trim().length() == 0) &&
                (et_student_Name.getText().toString().trim().length() == 0)) {
            MessageBox("Please select search parameters!");
            clearList();

        } else {
            StudentModel.getInstance().setStudentsList(
                    DatabaseHelper.getInstance(this).
                            getAllSearchedStudents(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, stdGrno, stdName));
            if (StudentModel.getInstance().getStudentsList().size() > 0) {
                setCollapsing(true);
                setAdapter();
            } else {
                setCollapsing(false);
                MessageBox("No results found");
                clearList();
            }

        }
    }

    private void sortInAscStudent() {
        try {
            switch (filterBy) {
                case filterStdId:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            return Integer.compare(sm1.getId(), sm2.getId());
                        }
                    });
                    break;
                case filterStdGrno:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            return sm1.getGrNo().compareToIgnoreCase(sm2.getGrNo());
                        }
                    });
                    break;
                case filterStdName:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            return sm1.getName().compareToIgnoreCase(sm2.getName());
                        }
                    });
                    break;
                case filterStdDOA:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            if (sm1.getEnrollmentDate() == null) {
                                return -1;
                            } else if (sm2.getEnrollmentDate() == null) {
                                return -1;
                            }
                            return sm1.getEnrollmentDate().compareToIgnoreCase(sm2.getEnrollmentDate());
                        }
                    });
                    break;
            }

            studentAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortInDscStudent() {
        try {
            switch (filterBy) {
                case filterStdId:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            return Integer.compare(sm2.getId(), sm1.getId());
                        }
                    });
                    break;
                case filterStdGrno:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            return sm2.getGrNo().compareToIgnoreCase(sm1.getGrNo());
                        }
                    });
                    break;
                case filterStdName:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            return sm2.getName().compareToIgnoreCase(sm1.getName());
                        }
                    });
                    break;
                case filterStdDOA:
                    Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                        @Override
                        public int compare(StudentModel sm1, StudentModel sm2) {
                            if (sm1.getEnrollmentDate() == null) {
                                return -1;
                            } else if (sm2.getEnrollmentDate() == null) {
                                return -1;
                            }

                            return sm2.getEnrollmentDate().compareToIgnoreCase(sm1.getEnrollmentDate());
                        }
                    });
                    break;
            }

            studentAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                populateStudentAutocompleteAdapter();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        SchoolSpinnerValue);
                ClassSectionModel classSectionModel = new ClassSectionModel();
                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                ClassSectionSpinner.setOnItemSelectedListener(this);

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
        //Important when any change in table call this method
//        AppModel.getInstance().changeMenuPendingSyncCount(StudentDropoutSearchActivity.this, true);

        if (studentAdapter != null) {
            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllSearchedStudents(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, stdGrno, stdName));
            setAdapter();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String std_grno = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getGrNo();
        String std_name = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getStudent_name();

        stdGrno = std_grno;
        stdName = std_name;

        et_std_name_grNo.setText(std_name);
        AppModel.getInstance().hideSoftKeyboard(StudentDropoutSearchActivity.this);
        searchClicked();
    }
}
