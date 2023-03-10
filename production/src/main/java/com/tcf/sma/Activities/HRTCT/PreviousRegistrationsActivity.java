package com.tcf.sma.Activities.HRTCT;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Adapters.HRTCT.HRTCT_PreviousRegistrationAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class PreviousRegistrationsActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener{
    private View view;
    private Spinner sp_school;
    private RecyclerView rv_tctEntry;
    private TextView tv_regionName, tv_areaName, errorMessage, noDataFound;
    private LinearLayout ll_tctEntry;

    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private HRTCT_PreviousRegistrationAdapter tctRegistrationAdapter;
    private List<TCTEmpSubjectTaggingModel> empSubjectTaggingModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_previous_registrations);
        setToolbar("TCT Previous Registrations", this, false);
        init(view);
    }

    private void init(View view) {
        sp_school = view.findViewById(R.id.spinner_SelectSchool);
        sp_school.setOnItemSelectedListener(this);


        tv_regionName = view.findViewById(R.id.tv_regionName);
        tv_areaName = view.findViewById(R.id.tv_areaName);

        noDataFound = view.findViewById(R.id.noDataFound);

        ll_tctEntry = view.findViewById(R.id.ll_tctEntry);

        rv_tctEntry = view.findViewById(R.id.rv_tctEntry);
        rv_tctEntry.setNestedScrollingEnabled(false);
        rv_tctEntry.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_tctEntry.setLayoutManager(linearLayoutManager);

        populateSpinners();
        working();
    }

    private void working() {
        AppModel.getInstance().setSchoolInfo(this, schoolId, tv_regionName, tv_areaName);
        setAdapter();
    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForTCTEntry();
        schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_school.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            sp_school.setSelection(indexOfSelectedSchool);
        }

        schoolId = ((SchoolModel) sp_school.getSelectedItem()).getId();
//        setAdapter();
    }

    private void setAdapter() {
        empSubjectTaggingModelList = TCTHelperClass.getInstance(this).getTCTRecord(schoolId, getIntent().getIntExtra("phaseId", 0));

        if (empSubjectTaggingModelList != null && empSubjectTaggingModelList.size() > 0) {
            tctRegistrationAdapter = new HRTCT_PreviousRegistrationAdapter(empSubjectTaggingModelList);
            rv_tctEntry.setAdapter(tctRegistrationAdapter);
            showData();
        } else {
            hideData();
        }
    }

    private void hideData() {
        ll_tctEntry.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    private void showData() {
        ll_tctEntry.setVisibility(View.VISIBLE);
        noDataFound.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
            AppModel.getInstance().setSpinnerSelectedSchool(this,
                    schoolId);
            AppModel.getInstance().setSchoolInfo(this, schoolId, tv_regionName, tv_areaName);
            setAdapter();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
