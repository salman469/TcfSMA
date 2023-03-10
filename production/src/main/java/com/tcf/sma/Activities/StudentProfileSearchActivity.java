package com.tcf.sma.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentProfileSearchActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {
    Parcelable recylerViewState = null;

    ProgressBar search_pb;
    final String filterStdId = "stdId", filterStdGrno = "stdGrno",
            filterStdName = "stdName", filterStdDOA = "stdDOA",
            filterStdClassSection = "stdClassSection",
            filterStdMonthlyFee = "stdMonthlyFee", filterStdCategory = "stdCategory";
    public Spinner SchoolSpinner, ClassSpinner, SectionSpinner, ClassSectionSpinner;
    //    public Spinner sp_filterBy;
    public TextView totalCount;
    public ClassModel classModel;
    public SectionModel sectionModel;
    public ClassSectionModel classSectionModel;
    public boolean isFinanceOpen = false;
    View view;
    LinearLayout llResults;
    //    LinearLayout ll_filterBy;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    Button Search;
    EditText et_gr_no, et_student_Name;
    RecyclerView rv_searchList;
    StudentAdapter studentAdapter;
    int classId, sectionId, studentCount;
    ImageView iv_stdId, iv_grno, iv_stdname, iv_admDate, iv_classSection, iv_monthly_fees, iv_category;
    //    LinearLayout ll_stdId, ll_grno, ll_stdname, ll_admDate, ll_classSection,
//            ll_monthly_fee, ll_category;
    //    private Spinner spn_activity;
    private int SchoolSpinnerValue = 0, ClassSpinnerValue = 0, SectionSpinnerValue = 0;
    private RadioGroup mFilterRadioGroup;
    private String admission_type = null;
    private int is_active = 1; // By default active
    //    private String filterBy;
    private int roleID = 0;
    private LinearLayout ll_radiobuttonStatus, ll_dropdownStatus;
    private RadioGroup rg_status;
    private RadioButton rb_active;

    private AutoCompleteTextView et_std_name_grNo;
    private StudentAutoCompleteAdapter studentAutocompleteAdapter;
    private String stdGrno = "", stdName = "";
    private boolean openFinance = false, isLoadFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_search);
        view = setActivityLayout(this, R.layout.activity_search_for_student);
        setToolbar(getString(R.string.student_gr_register), this, false);

        init(view);
//        populateStudentAutocompleteAdapter();
        et_std_name_grNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String std_grno = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getGrNo();
                String std_name = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getStudent_name();

                stdGrno = std_grno;
                stdName = std_name;

                et_std_name_grNo.setText(std_name);
                AppModel.getInstance().hideSoftKeyboard(StudentProfileSearchActivity.this);
                Search.performClick();
            }
        });
        working();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rv_searchList != null && rv_searchList.getLayoutManager() != null) {
            recylerViewState = rv_searchList.getLayoutManager().onSaveInstanceState();
        }
    }

    private void init(View view) {
        search_pb = view.findViewById(R.id.search_pb);
        totalCount = (TextView) view.findViewById(R.id.tv_total_students);
        llResults = (LinearLayout) view.findViewById(R.id.llResults);
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
//        spn_activity = (Spinner) view.findViewById(R.id.spn_activity);
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

        mFilterRadioGroup = (RadioGroup) view.findViewById(R.id.RadioGroup);
        mFilterRadioGroup.setOnCheckedChangeListener(this);

//        ll_filterBy = (LinearLayout) view.findViewById(R.id.ll_filterBy);
//        sp_filterBy = (Spinner) view.findViewById(R.id.sp_filterBy);
//        sp_filterBy.setOnItemSelectedListener(this);

        iv_stdId = (ImageView) view.findViewById(R.id.iv_stdId);
        iv_grno = (ImageView) view.findViewById(R.id.iv_grno);
        iv_stdname = (ImageView) view.findViewById(R.id.iv_stdname);
        iv_admDate = (ImageView) view.findViewById(R.id.iv_admDate);
        iv_classSection = (ImageView) view.findViewById(R.id.iv_classSection);
        iv_monthly_fees = (ImageView) view.findViewById(R.id.iv_monthly_fees);
        iv_category = (ImageView) view.findViewById(R.id.iv_category);

//        ll_stdId = (LinearLayout) view.findViewById(R.id.ll_stdId);
//        ll_grno = (LinearLayout) view.findViewById(R.id.ll_grno);
//        ll_stdname = (LinearLayout) view.findViewById(R.id.ll_stdname);
//        ll_admDate = (LinearLayout) view.findViewById(R.id.ll_admDate);
//        ll_classSection = (LinearLayout) view.findViewById(R.id.ll_classSection);
//        ll_monthly_fee = (LinearLayout) view.findViewById(R.id.ll_monthly_fees);
//        ll_category = (LinearLayout) view.findViewById(R.id.ll_category);

        ll_dropdownStatus = (LinearLayout) view.findViewById(R.id.ll_dropdownStatus);
        ll_dropdownStatus.setVisibility(View.GONE);

        ll_radiobuttonStatus = (LinearLayout) view.findViewById(R.id.ll_radiobuttonStatus);
        ll_radiobuttonStatus.setVisibility(View.VISIBLE);

        rg_status = (RadioGroup) view.findViewById(R.id.rg_status);
        rg_status.setOnCheckedChangeListener(this);

        rb_active = (RadioButton) view.findViewById(R.id.rb_active);

        et_std_name_grNo = (AutoCompleteTextView) view.findViewById(R.id.et_std_name_grNo);

//        ll_stdId.setOnClickListener(this);
//        ll_grno.setOnClickListener(this);
//        ll_stdname.setOnClickListener(this);
//        ll_admDate.setOnClickListener(this);
//        ll_classSection.setOnClickListener(this);
//        ll_monthly_fee.setOnClickListener(this);
//        ll_category.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            SchoolSpinner.setEnabled(false);
        }

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

        setCollapsing(false);
    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(SchoolSpinnerValue, is_active, admission_type);
        studentAutocompleteAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
                studentsList);
        if (studentsList.size() > 0)
            et_std_name_grNo.setAdapter(studentAutocompleteAdapter);
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    private void working() {
        initSpinners();
        rb_active.setChecked(true);  //By default active is checked
        admission_type = AppConstants.active;
        populateFilterSpinner();
    }

    private void populateFilterSpinner() {
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.studentInfo));

//        sp_filterBy.setAdapter(filterAdapter);
    }


    /**
     * Method to initialize spinners with Meta Data
     * Created by Haseeb
     */
    private void initSpinners() {


        //Used for principal
        SchoolModel schoolModel = new SchoolModel();
        ArrayList<SchoolModel> smList = new ArrayList<>();
        smList = AppModel.getInstance().getSchoolsForLoggedInUser(this);
//        smList = DatabaseHelper.getInstance(this).getAllUserSchools();
        smList.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));
//        smList.add(schoolModel);
        schoolModel.setSchoolsList(smList);
        SchoolSpinnerValue = 0;

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);

        SchoolSpinner.setOnItemSelectedListener(this);


        classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, getString(R.string.select_class)));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);


        sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, getString(R.string.select_section)));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);


        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        if (classSectionModel.getClassAndSectionsList().size() > 1) {
            classSectionModel.getClassAndSectionsList().add(classSectionModel.getClassAndSectionsList().size(),
                    new ClassSectionModel(-1, -1, "All"));
        }
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
//        setSchoolClassByDefaultValue();
        ClassSectionSpinner.setOnItemSelectedListener(this);

//        spn_activity.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.schoolActivity)));
//        spn_activity.setOnItemSelectedListener(this);

//        if (admission_type == null)
//            admission_type = AppConstants.All;
    }

    private void setSchoolClassByDefaultValue() {
//        if (!isLoadFirstTime) {
        if (classSectionModel.getClassAndSectionsList().size() > 1) {
            for (ClassSectionModel model : classSectionModel.getClassAndSectionsList()) {
                if (model.getClass_section_name().equalsIgnoreCase("All")) {
                    ClassSectionSpinner.setSelection(classSectionModel.getClassAndSectionsList().indexOf(model));
//                        isLoadFirstTime = true;
                    break;
                }
            }
        }
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:

                hideKeyboard();

                if (SchoolSpinnerValue == 0) {
                    MessageBox("Please select search parameters!");
                    clearList();
                } else if (ClassSpinnerValue == -1 && SectionSpinnerValue == -1) {
                    clearList();
                    search_pb.setVisibility(View.VISIBLE);
//                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim(), is_active, admission_type, false));
                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, stdGrno, stdName, is_active, admission_type, false));
                    if (StudentModel.getInstance().getStudentsList().size() > 0) {
                        setCollapsing(true);
                        openFinance = DatabaseHelper.getInstance(this).checkSchoolsFinanceModule(SchoolSpinnerValue);
                        if (openFinance) {
//                            ll_monthly_fee.setVisibility(View.VISIBLE);
//                            ll_category.setVisibility(View.VISIBLE);
                        } else {
//                            ll_monthly_fee.setVisibility(View.GONE);
//                            ll_category.setVisibility(View.GONE);
                        }
                        setAdapter();

                    } else {
                        setCollapsing(false);
                        MessageBox("No results found");
                        clearList();
                    }
                } else if (ClassSpinnerValue <= 0 && SectionSpinnerValue <= 0) {
                    MessageBox("Please select search parameters!");
                    clearList();
                } else {
                    search_pb.setVisibility(View.VISIBLE);
//                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim(), is_active, admission_type, false));
                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, stdGrno, stdName, is_active, admission_type, false));
                    if (StudentModel.getInstance().getStudentsList().size() > 0) {
                        setCollapsing(true);
                        openFinance = DatabaseHelper.getInstance(this).checkSchoolsFinanceModule(SchoolSpinnerValue);
                        if (openFinance) {
//                            ll_monthly_fee.setVisibility(View.VISIBLE);
//                            ll_category.setVisibility(View.VISIBLE);
                        } else {
//                            ll_monthly_fee.setVisibility(View.GONE);
//                            ll_category.setVisibility(View.GONE);
                        }
                        setAdapter();

                    } else {
                        setCollapsing(false);
                        MessageBox("No results found");
                        clearList();
                    }

                }
                break;
//            case R.id.ll_stdId:
//                filterBy = filterStdId;
//                if (iv_stdId.getDrawable().getConstantState() ==
//                        iv_stdId.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_stdId.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_stdId.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
//            case R.id.ll_grno:
//                filterBy = filterStdGrno;
//                if (iv_grno.getDrawable().getConstantState() ==
//                        iv_grno.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_grno.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_grno.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
//            case R.id.ll_stdname:
//                filterBy = filterStdName;
//                if (iv_stdname.getDrawable().getConstantState() ==
//                        iv_stdname.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_stdname.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_stdname.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
//            case R.id.ll_admDate:
//                filterBy = filterStdDOA;
//                if (iv_admDate.getDrawable().getConstantState() ==
//                        iv_admDate.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_admDate.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_admDate.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
//            case R.id.ll_classSection:
//                filterBy = filterStdClassSection;
//                if (iv_classSection.getDrawable().getConstantState() ==
//                        iv_classSection.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_classSection.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_classSection.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
//            case R.id.ll_monthly_fees:
//                filterBy = filterStdMonthlyFee;
//                if (iv_monthly_fees.getDrawable().getConstantState() ==
//                        iv_monthly_fees.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_monthly_fees.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_monthly_fees.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
//            case R.id.ll_category:
//                filterBy = filterStdCategory;
//                if (iv_category.getDrawable().getConstantState() ==
//                        iv_category.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
//                    iv_category.setImageResource(android.R.drawable.arrow_down_float);
//                    sortInDscStudent();
//                } else {
//                    iv_category.setImageResource(android.R.drawable.arrow_up_float);
//                    sortInAscStudent();
//                }
//                break;
        }

    }

    private void setAdapter() {
//        studentAdapter = new StudentAdapter(StudentModel.getInstance().getStudentsList(), this, 0, ClassSpinnerValue, SectionSpinnerValue, SchoolSpinnerValue);
        studentAdapter = new StudentAdapter(StudentModel.getInstance().getStudentsList(), this, 0, ClassSpinnerValue, SectionSpinnerValue, SchoolSpinnerValue, openFinance);
        rv_searchList.setAdapter(studentAdapter);
        search_pb.setVisibility(View.GONE);
        llResults.setVisibility(View.VISIBLE);

        try {
            rv_searchList.getLayoutManager().onRestoreInstanceState(recylerViewState);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ll_filterBy.setVisibility(View.VISIBLE);
    }

    private void clearList() {
        if (studentAdapter != null) {
            StudentModel.getInstance().setStudentsList(null);
            setAdapter();
            llResults.setVisibility(View.GONE);
//            ll_filterBy.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolModel model = (SchoolModel) adapterView.getItemAtPosition(position);

                List<String> allowedModules = null;
                if (model.getAllowedModule_App() != null) {
                    allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                }

                isFinanceOpen = allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue);
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                populateStudentAutocompleteAdapter();
                AppModel.getInstance().setSpinnerSelectedSchool(StudentProfileSearchActivity.this,
                        SchoolSpinnerValue);
                classSectionModel = new ClassSectionModel();
                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                if (classSectionModel.getClassAndSectionsList().size() > 1) {
                    classSectionModel.getClassAndSectionsList().add(classSectionModel.getClassAndSectionsList().size(),
                            new ClassSectionModel(-1, -1, "All"));
                }
                ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                setSchoolClassByDefaultValue();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null) {
                    MessageBox(errorModel.getMessage());
                }

//                ClassSectionSpinner.setOnItemSelectedListener(this);
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
//            case R.id.spn_activity:
//                String active_filter = adapterView.getItemAtPosition(position).toString();
//                if (active_filter.equals(getString(R.string.active)))
//                    is_active = 1;
//                else if (active_filter.equals(getString(R.string.inactive)))
//                    is_active = 0;
//                else if (active_filter.equals(getString(R.string.all)))
//                    is_active = 2;
//                break;
            case R.id.sp_filterBy:
//                filterBy = sp_filterBy.getSelectedItem().toString();
//
//                sortInAscStudent();
                break;
        }
    }
/*
    private void sortInAscStudent() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {


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
                                    if (sm1.getGrNo() != null && sm2.getGrNo() != null) {
                                        return Integer.compare(Integer.parseInt(sm1.getGrNo()), Integer.parseInt(sm2.getGrNo()));
                                    }
                                    return -1;
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
                        case filterStdClassSection:
                            Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                                @Override
                                public int compare(StudentModel sm1, StudentModel sm2) {
                                    return Integer.compare(sm1.getSchoolClassId(), sm2.getSchoolClassId());
                                }
                            });
                            break;
                        case filterStdMonthlyFee:
                            Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                                @Override
                                public int compare(StudentModel sm1, StudentModel sm2) {
                                    return Integer.compare((int) sm1.getActualFees(), (int) sm2.getActualFees());
                                }
                            });
                            break;
                        case filterStdCategory:
                            Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                                @Override
                                public int compare(StudentModel sm1, StudentModel sm2) {
                                    ScholarshipCategoryModel catFee1 = Scholarship_Category.getInstance(StudentProfileSearchActivity.this).getScholarshipCategory(
                                            SchoolSpinnerValue,
                                            (int) sm1.getActualFees());

                                    ScholarshipCategoryModel catFee2 = Scholarship_Category.getInstance(StudentProfileSearchActivity.this).getScholarshipCategory(
                                            SchoolSpinnerValue,
                                            (int) sm2.getActualFees());

                                    return Integer.compare(catFee1.getScholarship_category_id(), catFee2.getScholarship_category_id());
                                }
                            });
                            break;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            studentAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortInDscStudent() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {


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
                                    if (sm1.getGrNo() != null && sm2.getGrNo() != null) {
                                        return (Integer.compare(Integer.parseInt(sm2.getGrNo()), Integer.parseInt(sm1.getGrNo())));
                                    }
                                    return -1;
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
                        case filterStdClassSection:
                            Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                                @Override
                                public int compare(StudentModel sm1, StudentModel sm2) {
                                    return Integer.compare(sm2.getSchoolClassId(), sm1.getSchoolClassId());
                                }
                            });
                            break;
                        case filterStdMonthlyFee:
                            Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                                @Override
                                public int compare(StudentModel sm1, StudentModel sm2) {
                                    return Integer.compare((int) sm2.getActualFees(), (int) sm1.getActualFees());
                                }
                            });
                            break;
                        case filterStdCategory:
                            Collections.sort(StudentModel.getInstance().getStudentsList(), new Comparator<StudentModel>() {
                                @Override
                                public int compare(StudentModel sm1, StudentModel sm2) {
                                    ScholarshipCategoryModel catFee1 = Scholarship_Category.getInstance(StudentProfileSearchActivity.this).getScholarshipCategory(
                                            SchoolSpinnerValue,
                                            (int) sm1.getActualFees());

                                    ScholarshipCategoryModel catFee2 = Scholarship_Category.getInstance(StudentProfileSearchActivity.this).getScholarshipCategory(
                                            SchoolSpinnerValue,
                                            (int) sm2.getActualFees());

                                    return Integer.compare(catFee2.getScholarship_category_id(), catFee1.getScholarship_category_id());
                                }
                            });
                            break;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            studentAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Important when any change in table call this method
//        AppModel.getInstance().changeMenuPendingSyncCount(StudentProfileSearchActivity.this, false);

        if (studentAdapter != null) {
            search_pb.setVisibility(View.VISIBLE);
//            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllSearchedStudentsInSchool(SurveyAppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim()));
            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue,
                    SectionSpinnerValue, stdGrno,
                    et_student_Name.getText().toString().trim(), is_active, admission_type, false));
            setAdapter();

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

//        if (checkedId == R.id.rbAll) {
//            admission_type = AppConstants.All;
//        } else if (checkedId == R.id.rbNewAdmissions) {
//            admission_type = AppConstants.NewAdmissions;
//        } else if (checkedId == R.id.rbReadmissions) {
//            admission_type = AppConstants.Readmissions;
//        } else if (checkedId == R.id.rbPromotions) {
//            admission_type = AppConstants.Promotions;
//        } else if (checkedId == R.id.rbGraduations) {
//            admission_type = AppConstants.Graduations;
//        } else
        if (checkedId == R.id.rb_active) {
            is_active = 1;
            admission_type = AppConstants.active;
        } else if (checkedId == R.id.rb_inactive) {
            is_active = 0;
            admission_type = AppConstants.inactive;
        } else if (checkedId == R.id.rb_withdrawn) {
            is_active = 2; //This is temporary and not been used
            admission_type = AppConstants.withdrawn;
        } else if (checkedId == R.id.rb_graduated) {
            is_active = 2; //This is temporary and not been used
            admission_type = AppConstants.graduated;
        } else if (checkedId == R.id.rb_all) {
            is_active = 2; //This is temporary and not been used
            admission_type = AppConstants.AllStatus;
        }

        populateStudentAutocompleteAdapter();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
