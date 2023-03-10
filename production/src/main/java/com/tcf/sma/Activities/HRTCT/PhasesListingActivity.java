package com.tcf.sma.Activities.HRTCT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HRTCT.HRTCT_PhasesListingAdapter;
import com.tcf.sma.Adapters.HRTCT.HRTCT_PreviousRegistrationAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTPhaseModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PhasesListingActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener{
    private View view;
    private Spinner sp_school;
    private RecyclerView rv_tctPhases;
    private TextView tv_regionName, tv_areaName, errorMessage, noDataFound;
    private LinearLayout ll_tctPhases;

    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private HRTCT_PhasesListingAdapter hrtct_phasesListingAdapter;
    private List<TCTPhaseModel> tctPhasesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_phases_listing);
        setToolbar("TCT Phases", this, false);
        init(view);
    }

    private void init(View view) {
        sp_school = view.findViewById(R.id.spinner_SelectSchool);
        sp_school.setOnItemSelectedListener(this);


        tv_regionName = view.findViewById(R.id.tv_regionName);
        tv_areaName = view.findViewById(R.id.tv_areaName);

        noDataFound = view.findViewById(R.id.noDataFound);

        ll_tctPhases = view.findViewById(R.id.ll_tctPhases);

        rv_tctPhases = view.findViewById(R.id.rv_tctPhases);
        rv_tctPhases.setNestedScrollingEnabled(false);
        rv_tctPhases.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_tctPhases.setLayoutManager(linearLayoutManager);

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
        tctPhasesList = TCTHelperClass.getInstance(this).getTCTPhases(schoolId);

        if (tctPhasesList != null && tctPhasesList.size() > 0   ) {
            String year=AppModel.getInstance().convertDatetoFormat(tctPhasesList.get(0).getTctPostRegEndDate(), "yyyy-MM-dd hh:mm:ss", "yyyy");
            String month=AppModel.getInstance().convertDatetoFormat(tctPhasesList.get(0).getTctPostRegEndDate(), "yyyy-MM-dd hh:mm:ss", "MM");
            String day=AppModel.getInstance().convertDatetoFormat(tctPhasesList.get(0).getTctPostRegEndDate(), "yyyy-MM-dd hh:mm:ss", "dd");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, (Integer.parseInt(month)-1));
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            cal.add(Calendar.DAY_OF_MONTH, 28); // add 28 days
            Date date = cal.getTime();
            LocalDateTime now = LocalDateTime.now();
            String  condDate=df.format(date);
            String currentDate=AppModel.getInstance().convertDatetoFormat(now.toString(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd");

            long totalDays = ChronoUnit.DAYS.between(LocalDate.parse(condDate),LocalDate.parse(currentDate))  ;
            if(totalDays<=0){

                hrtct_phasesListingAdapter = new HRTCT_PhasesListingAdapter(tctPhasesList);
                rv_tctPhases.setAdapter(hrtct_phasesListingAdapter);
                showData();

            }



        } else {
            hideData();
        }
    }

    private void hideData() {
        ll_tctPhases.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    private void showData() {
        ll_tctPhases.setVisibility(View.VISIBLE);
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
