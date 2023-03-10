package com.tcf.sma.Survey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HR.EmployeeListingAdapter;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSelector extends DrawerActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public TextView tv_total_employees;
    private View view;
    private Spinner spinner_SelectDesignation;
    private RecyclerView rv_empDetail;
    private RadioGroup rg_cadre, rg_status;
    private ImageView btn_Search;
    private LinearLayout llResults;
    public TextView tv_regionName, tv_areaName, tv_schoolName;

    private SchoolModel schoolModel;
    private ArrayAdapter<EmployeeDesignationModel> DesignationAdapter;
    private List<EmployeeDesignationModel> designationsList;
    private EmployeeListingAdapter employeeEmployeeListingAdapter;
    private List<EmployeeModel> employeeModels = new ArrayList<>();
    //    private int schoolId = 0;
    private int schoolId = SurveyAppModel.getInstance().selectedProject.getSelectedSchool().getId();
    private String cadre = "";
    private String status = "";
    private String designation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_employee_selector);
        setToolbar("Select Employee", this, false);
        init(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (employeeEmployeeListingAdapter != null) {
            employeeModels = EmployeeHelperClass.getInstance(EmployeeSelector.this).getEmployees(schoolId, designation, cadre, status);
            setAdapter();
        }
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
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

    private void init(View view) {

        Intent intent = getIntent();
        if (intent.hasExtra("sm")) {
            schoolModel = intent.getParcelableExtra("sm");
        }else{
            schoolModel = SurveyAppModel.getInstance().selectedProject.getSelectedSchool();
        }


        btn_Search = view.findViewById(R.id.btn_Search);
        btn_Search.setOnClickListener(this);

        tv_regionName = view.findViewById(R.id.tv_regionName);
        tv_areaName = view.findViewById(R.id.tv_areaName);
        tv_schoolName = view.findViewById(R.id.tv_schoolName);

        llResults = view.findViewById(R.id.llResults);
        tv_total_employees = view.findViewById(R.id.tv_total_employees);

        rv_empDetail = view.findViewById(R.id.rv_empDetail);

        spinner_SelectDesignation = view.findViewById(R.id.spinner_SelectDesignation);
        spinner_SelectDesignation.setOnItemSelectedListener(this);

        rv_empDetail.setNestedScrollingEnabled(false);
        rv_empDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_empDetail.setLayoutManager(linearLayoutManager);


        rg_cadre = view.findViewById(R.id.rg_cadre);
        rg_cadre.setOnCheckedChangeListener(this);
        rg_status = view.findViewById(R.id.rg_status);
        rg_status.setOnCheckedChangeListener(this);

        populateSpinners();
        populateDesignationSpinner();
        setCollapsing(false);


    }

    private void populateSpinners() {
        if (schoolModel != null) {
            schoolId = schoolModel.getId();
            tv_schoolName.setText(schoolModel.getId() + "-" + schoolModel.getName());
            AppModel.getInstance().setSchoolInfo(this, schoolId, tv_regionName, tv_areaName);
        }
    }

    private void populateDesignationSpinner() {
        if (schoolId != 0) {
            designationsList = EmployeeHelperClass.getInstance(this).getDesignations(schoolId);
            designationsList.add(0, new EmployeeDesignationModel(0, getString(R.string.select_designation)));
            if (designationsList.size() > 1) {
                designationsList.add(designationsList.size(), new EmployeeDesignationModel(designationsList.size(), "All"));
            }
            DesignationAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout_black, designationsList);
            DesignationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_SelectDesignation.setAdapter(DesignationAdapter);

            setDesigByDefaultValue();
        }
    }

    private void setDesigByDefaultValue() {
        if (designationsList.size() > 1) {
            spinner_SelectDesignation.setSelection(designationsList.size());
            for (EmployeeDesignationModel model : designationsList) {
                if (model.getDesignation_Name().equalsIgnoreCase("All")) {
                    spinner_SelectDesignation.setSelection(designationsList.indexOf(model));
                    break;
                }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        designation = ((EmployeeDesignationModel) adapterView.getItemAtPosition(position)).getDesignation_Name();
        btnSearch();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_faculty) {
            cadre = "Faculty";
            btnSearch();
        } else if (checkedId == R.id.rb_non_faculty) {
            cadre = "Non-Faculty";
            btnSearch();
        } else if (checkedId == R.id.rb_cadre_all) {
            cadre = "";
            btnSearch();
        } else if (checkedId == R.id.rb_active) {
            status = "Active";
            btnSearch();
        } else if (checkedId == R.id.rb_resign) {
            status = "Resigned";
            btnSearch();
        } else if (checkedId == R.id.rb_status_all) {
            status = "";
            btnSearch();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Search) {
            btnSearch();
        }
    }

    private void btnSearch() {
        if (schoolId != 0) {
            if (spinner_SelectDesignation.getSelectedItem().toString().equals(getString(R.string.select_designation))) {
                MessageBox("Please select designation!");
                clearList();
            } else {
                employeeModels = EmployeeHelperClass.getInstance(EmployeeSelector.this).getEmployees(schoolId, designation, cadre, status);
                setAdapter();
            }
        }
    }

    private void setAdapter() {
        if (employeeModels != null && employeeModels.size() > 0) {
            llResults.setVisibility(View.VISIBLE);
            employeeEmployeeListingAdapter = new EmployeeListingAdapter(EmployeeSelector.this, schoolId, employeeModels, o -> {
                try {
                    startActivity(getIntent().setClass(EmployeeSelector.this, SurveyAppModel.getInstance().selectedProject.popActivityFromStack())
                            .putExtra("em", (EmployeeModel) o));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            rv_empDetail.setAdapter(employeeEmployeeListingAdapter);
            setCollapsing(true);
            tv_total_employees.setText(employeeModels.size() + "");
        } else {
            MessageBox("No records found!");
            llResults.setVisibility(View.GONE);
            setCollapsing(false);
            tv_total_employees.setText("0");
        }
    }

    private void clearList() {
        if (employeeEmployeeListingAdapter != null) {
            employeeModels = null;
            llResults.setVisibility(View.GONE);
        }
        setCollapsing(false);
    }


}
