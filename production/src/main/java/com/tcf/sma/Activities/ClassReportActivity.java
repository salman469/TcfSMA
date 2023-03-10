package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Adapters.ClassReportAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassReportModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassReportActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    final String filterClassSection = "classSection", filterMax_capacity = "max_capacity",
            filterActive_student = "active_student", filterCapacity = "capacity",
            filterOps_utl = "ops_utl";
    View view;
    List<SchoolModel> schoolModels;
    CardView card3;
    Button Search;
    ImageView iv_classSection, iv_max_capacity, iv_active_student, iv_capacity, iv_ops_utl;
    LinearLayout ll_classSection, ll_max_capacity, ll_active_student, ll_capacity, ll_ops_utl;
    private Spinner spnSchools, ClassSectionSpinner;
    private int schoolId = 0;
    private ClassSectionModel classSectionModel;
    private ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    private RecyclerView rv_searchList;
    private ClassReportAdapter classReportAdapter;
    private int ClassSpinnerValue = 0, SectionSpinnerValue = 0;
    private List<ClassReportModel> classReportModelList;
    private String filterBy;
    private int roleID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_class_report);
        setToolbar(getString(R.string.classReport), this, false);

        init(view);
        populateSchoolSpinner();
    }

    private void init(View view) {

        spnSchools = (Spinner) view.findViewById(R.id.spnSchools);

        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);

        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);
        rv_searchList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

        card3 = (CardView) view.findViewById(R.id.card3);
        Search = (Button) view.findViewById(R.id.btnsearch);
        Search.setOnClickListener(this);

        iv_classSection = (ImageView) view.findViewById(R.id.iv_classSection);
        iv_max_capacity = (ImageView) view.findViewById(R.id.iv_max_capacity);
        iv_active_student = (ImageView) view.findViewById(R.id.iv_active_student);
        iv_capacity = (ImageView) view.findViewById(R.id.iv_capacity);
        iv_ops_utl = (ImageView) view.findViewById(R.id.iv_ops_utl);

        ll_classSection = (LinearLayout) view.findViewById(R.id.ll_classSection);
        ll_max_capacity = (LinearLayout) view.findViewById(R.id.ll_max_capacity);
        ll_active_student = (LinearLayout) view.findViewById(R.id.ll_active_student);
        ll_capacity = (LinearLayout) view.findViewById(R.id.ll_capacity);
        ll_ops_utl = (LinearLayout) view.findViewById(R.id.ll_ops_utl);

        ll_classSection.setOnClickListener(this);
        ll_max_capacity.setOnClickListener(this);
        ll_active_student.setOnClickListener(this);
        ll_capacity.setOnClickListener(this);
        ll_ops_utl.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }

        setCollapsing(false);
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

    private void populateSchoolSpinner() {
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchools();
        schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));
        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1)
            spnSchools.setSelection(indexOfSelectedSchool);
        spnSchools.setOnItemSelectedListener(this);

        schoolId = ((SchoolModel) spnSchools.getSelectedItem()).getId();
    }

    private void populateClassSectionSpinner() {
        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_classSection:
                filterBy = filterClassSection;
                if (iv_classSection.getDrawable().getConstantState() ==
                        iv_classSection.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_classSection.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscClassSection();
                } else {
                    iv_classSection.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscClassSection();
                }
                break;
            case R.id.ll_max_capacity:
                filterBy = filterMax_capacity;
                if (iv_max_capacity.getDrawable().getConstantState() ==
                        iv_max_capacity.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_max_capacity.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscClassSection();
                } else {
                    iv_max_capacity.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscClassSection();
                }
                break;
            case R.id.ll_active_student:
                filterBy = filterActive_student;
                if (iv_active_student.getDrawable().getConstantState() ==
                        iv_active_student.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_active_student.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscClassSection();
                } else {
                    iv_active_student.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscClassSection();
                }
                break;
            case R.id.ll_capacity:
                filterBy = filterCapacity;
                if (iv_capacity.getDrawable().getConstantState() ==
                        iv_capacity.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_capacity.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscClassSection();
                } else {
                    iv_capacity.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscClassSection();
                }
                break;
            case R.id.ll_ops_utl:
                filterBy = filterOps_utl;
                if (iv_ops_utl.getDrawable().getConstantState() ==
                        iv_ops_utl.getResources().getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_ops_utl.setImageResource(android.R.drawable.arrow_down_float);
                    sortInDscClassSection();
                } else {
                    iv_ops_utl.setImageResource(android.R.drawable.arrow_up_float);
                    sortInAscClassSection();
                }
                break;
            case R.id.btnsearch:
                AppModel.getInstance().showLoader(this);
                try {
                    int activeStudentCount = 0;
                    int capacity = 0;
                    int maxcapacity = 0;
                    double operationUtilization = 0;
                    classReportModelList = new ArrayList<>();

                    if (ClassSpinnerValue != 0 && SectionSpinnerValue != 0) {

                        StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(ClassReportActivity.this).
                                getAllSearchedStudents(schoolId, ClassSpinnerValue,
                                        SectionSpinnerValue, "", ""));

                        for (StudentModel stm : StudentModel.getInstance().getStudentsList()) {
                            if (stm.isActive()) {
                                activeStudentCount++;
                            }
                        }

                        SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(schoolId, ClassSpinnerValue,
                                SectionSpinnerValue);

                        capacity = DatabaseHelper.getInstance(this).getCapacityForSchool(scm.getSchoolId(),
                                scm.getId());

                        maxcapacity = DatabaseHelper.getInstance(this).getMaxCapacityFromSchoolClass(scm.getSchoolId(),
                                scm.getId());

                        String class_section = StudentModel.getInstance()
                                .getStudentsList().get(0).getCurrentClass() + " " +
                                StudentModel.getInstance().getStudentsList().get(0).getCurrentSection();

                        operationUtilization = ((activeStudentCount / (double) capacity) * 100);

                        ClassReportModel classReportModel = new ClassReportModel();

                        classReportModel.setClass_section_name(class_section);
                        classReportModel.setStudentActiveCount(activeStudentCount);
                        classReportModel.setCapacity(capacity);
                        classReportModel.setOptUtls(operationUtilization);
                        classReportModel.setMaxCapacity(maxcapacity);

                        classReportModelList.add(classReportModel);
                    } else {

                        for (int h = 1; h < classSectionModel.getClassAndSectionsList().size(); h++) {

                            activeStudentCount = 0;
                            capacity = 0;
                            maxcapacity = 0;
                            operationUtilization = 0;

                            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(ClassReportActivity.this).
                                    getAllSearchedStudents(schoolId, classSectionModel.getClassAndSectionsList().get(h).getClassId(),
                                            classSectionModel.getClassAndSectionsList().get(h).getSectionId(), "", ""));

                            for (StudentModel stm : StudentModel.getInstance().getStudentsList()) {
                                if (stm.isActive()) {
                                    activeStudentCount++;
                                }
                            }

                            capacity = DatabaseHelper.getInstance(this).getCapacityForSchool(schoolId,
                                    classSectionModel.getClassAndSectionsList().get(h).getSchoolClassId());

                            maxcapacity = DatabaseHelper.getInstance(this).getMaxCapacityFromSchoolClass(schoolId,
                                    classSectionModel.getClassAndSectionsList().get(h).getSchoolClassId());

                            operationUtilization = (double) (activeStudentCount / (double) capacity) * 100;

                            ClassReportModel classReportModel = new ClassReportModel();
                            classReportModel.setClass_section_name(classSectionModel.getClassAndSectionsList().get(h).getClass_section_name());
                            classReportModel.setStudentActiveCount(activeStudentCount);
                            classReportModel.setCapacity(capacity);
                            classReportModel.setOptUtls(operationUtilization);
                            classReportModel.setMaxCapacity(maxcapacity);

                            classReportModelList.add(classReportModel);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().hideLoader();
                }

                if (classReportModelList.size() > 0) {
                    setCollapsing(true);
                    setAdapter();
                    AppModel.getInstance().hideLoader();
                } else {
                    setCollapsing(false);
                    MessageBox("No results found");
                    AppModel.getInstance().hideLoader();
                    clearList();
                }
                break;
        }
    }

    private void sortInDscClassSection() {
        try {
            switch (filterBy) {
                case filterClassSection:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm2.getClass_section_name().compareTo(crm1.getClass_section_name());
                        }
                    });
                    break;
                case filterMax_capacity:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm2.getMaxCapacity() - crm1.getMaxCapacity();
                        }
                    });
                    break;
                case filterActive_student:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm2.getStudentActiveCount() - crm1.getStudentActiveCount();
                        }
                    });
                    break;
                case filterCapacity:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm2.getCapacity() - crm1.getCapacity();
                        }
                    });
                    break;
                case filterOps_utl:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return Double.compare(crm2.getOptUtls(), crm1.getOptUtls());
                        }
                    });
                    break;
            }

            classReportAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortInAscClassSection() {
        try {
            switch (filterBy) {
                case filterClassSection:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm1.getClass_section_name().compareTo(crm2.getClass_section_name());
                        }
                    });
                    break;
                case filterMax_capacity:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm1.getMaxCapacity() - crm2.getMaxCapacity();
                        }
                    });
                    break;
                case filterActive_student:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm1.getStudentActiveCount() - crm2.getStudentActiveCount();
                        }
                    });
                    break;
                case filterCapacity:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return crm1.getCapacity() - crm2.getCapacity();
                        }
                    });
                    break;
                case filterOps_utl:
                    Collections.sort(classReportModelList, new Comparator<ClassReportModel>() {
                        @Override
                        public int compare(ClassReportModel crm1, ClassReportModel crm2) {
                            return Double.compare(crm1.getOptUtls(), crm2.getOptUtls());
                        }
                    });
                    break;
            }

            classReportAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearList() {
        if (classReportAdapter != null) {
            StudentModel.getInstance().setStudentsList(null);
            setAdapter();
            card3.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {

        classReportAdapter = new ClassReportAdapter(this, classReportModelList);
        rv_searchList.setAdapter(classReportAdapter);
        card3.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spnSchools:
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);
                populateClassSectionSpinner();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

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
}
