package com.tcf.sma.Activities.HR;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HR.EmployeeAutoCompleteAdapter;
import com.tcf.sma.Adapters.HR.ResignationHistoryAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeAutoCompleteModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignationStatusModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class Approval_Dashboard_Listing extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private View view;
    private Spinner spinner_SelectSchool, spinner_SelectStatus;
    private RecyclerView rv_empDetail;
    private Button btn_Search, clear;
    private HorizontalScrollView hsw;

    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private ArrayAdapter<EmployeeResignationStatusModel> StatusAdapter;
    private List<EmployeeResignationStatusModel> StatusList;
    private ResignationHistoryAdapter employeeResignationAdapter;
    private List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();
    private int resignType = 0, schoolId = 0;
    private int status, approvalStatus;

    private AutoCompleteTextView et_std_name_grNo;
    private EmployeeAutoCompleteAdapter employeeAutocompleteAdapter;
    private String empFirstName = "", empLastName = "", empEmpCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_approval__dashboard__listing);

        view = setActivityLayout(this, R.layout.activity_approval__dashboard__listing);
        setToolbar("Approval List", this, false);
        init(view);
        working();
    }

    private void working() {
        populateSpinners();
        populateStatusSpinner();
        populateEmployeeAutocompleteAdapter();

        et_std_name_grNo.setOnItemClickListener(Approval_Dashboard_Listing.this);
        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() != 0) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    empEmpCode = "";
                    empFirstName = "";
                    empLastName = "";
                }
            }
        });

    }

    private void init(View view) {
        // rv_empDetail = (RecyclerView)view.findViewById(R.id.rv_empDetail);
        btn_Search = (Button) view.findViewById(R.id.btn_Search);
        btn_Search.setOnClickListener(this);
        rv_empDetail = (RecyclerView) view.findViewById(R.id.rv_empDetail);
        hsw = view.findViewById(R.id.horizontalScrollView);

        clear = (Button) findViewById(R.id.clear);
        clear.setVisibility(View.INVISIBLE);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_std_name_grNo.setText("");
                clear.setVisibility(View.GONE);

                clearList();
            }
        });

        spinner_SelectSchool = (Spinner) view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
        spinner_SelectStatus = (Spinner) view.findViewById(R.id.spinner_SelectStatus);
        spinner_SelectStatus.setOnItemSelectedListener(this);

        et_std_name_grNo = view.findViewById(R.id.et_std_name_grNo);

//        rv_empDetail.setLayoutManager(new LinearLayoutManager(this));
        rv_empDetail.setNestedScrollingEnabled(false);
        rv_empDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_empDetail.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("resignType")) {
            resignType = intent.getIntExtra("resignType", 0);
            if (resignType == 1)
                setToolbar("Resignation Approval List", this, false);
            else if (resignType == 2)
                setToolbar("Termination Approval List", this, false);

        }

        //Intent intent = getIntent();
        if (intent != null && intent.hasExtra("approvalStatus")) {
            status = intent.getIntExtra("approvalStatus", 0);
        }


    }

    private void populateEmployeeAutocompleteAdapter() {
        if (schoolId < 1) {
            schoolId = getIntent().getIntExtra("schoolId", 0);
        }
        final List<EmployeeAutoCompleteModel> employeesList = EmployeeHelperClass.getInstance(this).getAllExEmpsForAutocompleteList(schoolId, status,true);
        employeeAutocompleteAdapter = new EmployeeAutoCompleteAdapter(this, R.layout.employee_autocomplete_view,
                employeesList);
        et_std_name_grNo.setAdapter(employeeAutocompleteAdapter);

    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForEmployeeResignationAndTermination();
        schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectSchool.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            spinner_SelectSchool.setSelection(indexOfSelectedSchool);
        }

    }

    private void populateStatusSpinner() {
        StatusList = EmployeeHelperClass.getInstance(this).getResignStatus();
        StatusList.add(0, new EmployeeResignationStatusModel(0, getString(R.string.select_status)));

        if (StatusList.size() > 1) {
            StatusList.add(StatusList.size(), new EmployeeResignationStatusModel(StatusList.size(), "All"));
        }

        StatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, StatusList);
        StatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectStatus.setAdapter(StatusAdapter);

       /* int indexOfSelectedStatus = SurveyAppModel.getInstance().getStatusPosition(this);
        if (indexOfSelectedStatus > -1) {
            spinner_SelectStatus.setSelection(indexOfSelectedStatus);
        }*/

        int indexOfSelectedApprovalStatus = getStatusIndex();
        spinner_SelectStatus.setSelection(indexOfSelectedApprovalStatus);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
            AppModel.getInstance().setSpinnerSelectedSchool(this,
                    schoolId);
        } else if (adapterView.getId() == R.id.spinner_SelectStatus) {
            status = ((EmployeeResignationStatusModel) adapterView.getItemAtPosition(position)).getId();
        }

//        clear.setVisibility(View.VISIBLE);
        populateEmployeeAutocompleteAdapter();

    }

    private int getStatusIndex() {
        int index = 0;
        for (EmployeeResignationStatusModel model : StatusList) {
            if (model.getId() == status)
                return index;
            index++;
        }
        return index;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        clear.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Search) {

            searchClicked(v);
        }
    }

    private void searchClicked(View v) {
        if (spinner_SelectSchool.getSelectedItem().toString().equals(getString(R.string.select_school))) {
            MessageBox("Please select school!");
            clearList();
        } else if (spinner_SelectStatus.getSelectedItem().toString().equals(getString(R.string.select_status))) {
            MessageBox("Please select status!");
            clearList();
        } else {
            if (resignType == 1 || resignType == 2)
                employeeSeparationModelList = EmployeeHelperClass.getInstance(Approval_Dashboard_Listing.this).getInActiveEmployees(schoolId, status, resignType, empFirstName, empLastName);
            //employeeModels = EmployeeHelperClass.getInstance(Approval_Dashboard_Listing.this).getEmployees(schoolId, designation, cadre, status);
            setAdapter();
        }

        AppModel.getInstance().hideSoftKeyboard(this);
    }

    private void setAdapter() {
        if (employeeSeparationModelList != null && employeeSeparationModelList.size() > 0) {
            hsw.setVisibility(View.VISIBLE);
            employeeResignationAdapter = new ResignationHistoryAdapter(Approval_Dashboard_Listing.this, employeeSeparationModelList, false);
            rv_empDetail.setAdapter(employeeResignationAdapter);
        } else {
            MessageBox("No records found!");
            hsw.setVisibility(View.GONE);
        }
    }

    private void clearList() {
        if (employeeResignationAdapter != null) {
            employeeSeparationModelList = null;
            hsw.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String emp_Code = ((EmployeeAutoCompleteModel) adapterView.getItemAtPosition(i)).getEmpCode();
        String emp_FirstName = ((EmployeeAutoCompleteModel) adapterView.getItemAtPosition(i)).getEmpFirstName();
        String emp_LastName = ((EmployeeAutoCompleteModel) adapterView.getItemAtPosition(i)).getEmpLastName();


        empEmpCode = emp_Code;
        empFirstName = emp_FirstName;
        empLastName = emp_LastName;
//        employeeCode = Integer.valueOf(emp_Code);

        et_std_name_grNo.setText(emp_FirstName + " " + emp_LastName);
//        SurveyAppModel.getInstance().hideSoftKeyboard(this);
        searchClicked(view);

    }
}

