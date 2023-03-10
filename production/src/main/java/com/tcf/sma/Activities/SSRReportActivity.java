package com.tcf.sma.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.SSRReportAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.MonthYearPickerDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.SSRReportModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SSRReportActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    View view;
    private Spinner spnSchools;
    private TextView etMonthYear;
    private RecyclerView rv_searchList;
    private CardView card3;
    private int schoolId = 0;
    private List<SchoolModel> schoolModels;
    private Calendar cal;
    private Button Search;
    private String formatMonth, formatYear;
    private SSRReportAdapter ssrReportAdapter;
    private List<SSRReportModel> ssrReportList;
    private ClassSectionModel classSectionModel;
    private SSRReportModel ssrReportModel;
    private String fromDate, toDate;
    private int roleID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ssrreport);
        view = setActivityLayout(this, R.layout.activity_ssrreport);
        setToolbar(getString(R.string.ssrReport), this, false);

        init(view);
        populateSpinners();
    }

    private void init(View view) {
        spnSchools = (Spinner) view.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        etMonthYear = (TextView) view.findViewById(R.id.etMonthYear);
        etMonthYear.setOnClickListener(this);

        etMonthYear.setText(AppModel.getInstance().getCurrentDateTime("MMMM - yyyy"));
        cal = Calendar.getInstance();

        Search = (Button) view.findViewById(R.id.btnsearch);
        Search.setOnClickListener(this);

        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

        card3 = (CardView) view.findViewById(R.id.card3);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }
    }

    private void populateSpinners() {
//        schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();

        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1)
            spnSchools.setSelection(indexOfSelectedSchool);
//        int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
//        spnSchools.setSelection(selectedSchoolIndex);

    }

    private int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
        int index = 0;
        for (SchoolModel model : schoolModels) {
            if (model.getId() == appSchoolId)
                return index;
            index++;
        }
        return index;
    }

    private void setAdapter() {
        working();
        ssrReportAdapter = new SSRReportAdapter(SSRReportActivity.this, ssrReportList);
        rv_searchList.setAdapter(ssrReportAdapter);
        card3.setVisibility(View.VISIBLE);
    }

    private void working() {
        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ClassSectionModel model : classSectionModel.getClassAndSectionsList()) {
//                      GetNewAdmissionThisMonth
                    ssrReportModel = new SSRReportModel(SSRReportActivity.this).getViewSSRTableDataUsingClassSection(schoolId, model.getSchoolClassId(), fromDate, toDate, 1);

//                    mViewSSRTableList.add(mViewSSRTableModel);
//
//                    //GetSSRWithDrawalThisMonth
//                    mViewSSRTableModel = sDbHelper.getViewSSRTableData(schoolId, fromDate, toDate, 2);
//
//                    mViewSSRTableList.add(mViewSSRTableModel);
//
//                    //GetGraduatesThisMonth
//                    mViewSSRTableModel = sDbHelper.getViewSSRTableData(schoolId, fromDate, toDate, 3);
//
//                    mViewSSRTableList.add(mViewSSRTableModel);
                }

            }
        }).start();
        ssrReportList.add(ssrReportModel);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
        etMonthYear.setText(AppConstants.monthArray[mm] + " - " + yy);
        cal.set(Calendar.MONTH, mm);
        cal.set(Calendar.YEAR, yy);
        cal.set(Calendar.DAY_OF_WEEK, dd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etMonthYear:
                MonthYearPickerDialog mypd = new MonthYearPickerDialog();
                mypd.setListener(this, cal);
                mypd.show(getFragmentManager(), "MonthYearPickerDialog");
                break;
            case R.id.btnsearch:
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");

                formatMonth = sdf1.format(cal.getTime());
                formatYear = sdf2.format(cal.getTime());

                getFromAndToDate(cal);
//                setAdapter();
                break;
        }
    }

    private void getFromAndToDate(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.setTime(firstDayOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfMonth = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fromDate = sdf.format(firstDayOfMonth);
        toDate = sdf.format(lastDayOfMonth);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spnSchools:
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
