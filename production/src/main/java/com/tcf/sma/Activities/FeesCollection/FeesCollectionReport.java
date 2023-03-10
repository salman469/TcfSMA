package com.tcf.sma.Activities.FeesCollection;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.FeesCollectionReportAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.FeesCollectionReportModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FeesCollectionReport extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    View view;
    Spinner spnSession, sp_schools;
    RecyclerView rv_searchList;
    TextView tvBalance;
    CardView card3;
    List<FeesCollectionReportModel> mReceivablesModelList;
    private ArrayList<SchoolModel> schoolModelList;
    private int schoolId = 0;
    private SchoolModel schoolModel;
    private String[] academic_sessions;
    private FeesCollectionReportAdapter feesCollectionReportAdapter;
    private Button Search;
    private String startDate, endDate;
    private List<SchoolModel> smList;
    private double balance = 0;
    private List<String> monthsBtwaccademicSession;
    private List<FeesCollectionReportModel> fcrList;
    private LinearLayout ll_view;
    private int roleID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fees_collection_report);
        view = setActivityLayout(this, R.layout.activity_fees_collection_report);
        setToolbar(getString(R.string.feesCollectionReport), this, false);

        init(view);
        populateSpinner();
//        setAdapter();
    }

    private void init(View view) {
//        card3 = (CardView) view.findViewById(R.id.card3);
        ll_view = (LinearLayout) view.findViewById(R.id.ll_view);
        spnSession = (Spinner) view.findViewById(R.id.spnSession);
        spnSession.setOnItemSelectedListener(this);

        sp_schools = (Spinner) view.findViewById(R.id.sp_schools);
        sp_schools.setOnItemSelectedListener(this);

        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

        tvBalance = (TextView)
                view.findViewById(R.id.tvBalance);

        Search = (Button) view.findViewById(R.id.btnsearch);
        Search.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            sp_schools.setEnabled(false);
        }
    }

    private void populateSpinner() {
//        schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
        schoolModelList = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);
//        schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();

        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModelList);
        sp_schools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModelList, this);
        if (indexOfSelectedSchool > -1)
            sp_schools.setSelection(indexOfSelectedSchool);
//        int selectedSchoolIndex = getSelectedSchoolPosition(schoolModelList, schoolId);
//         sp_schools.setSelection(selectedSchoolIndex);
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
        mReceivablesModelList = new ArrayList<>();
        fcrList = new ArrayList<>();
        AppModel.getInstance().showLoader(FeesCollectionReport.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int totaldues = 0;
                int totalcollected = 0;

                mReceivablesModelList = AppInvoice.getInstance(FeesCollectionReport.this).getViewRecvFromAcadSession(schoolId, startDate, endDate);
                if (mReceivablesModelList != null && mReceivablesModelList.size() > 0) {
                    for (FeesCollectionReportModel model : mReceivablesModelList) {
                        totaldues += model.getDues();
                        totalcollected += model.getCollected();
                    }
                    balance = totaldues - totalcollected;

                    try {
                        FeesCollectionReport.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < monthsBtwaccademicSession.size(); i++) {
                                    for (int j = 0; j < mReceivablesModelList.size(); j++) {
                                        if (monthsBtwaccademicSession.get(i).equals(mReceivablesModelList.get(j).getMonth())) {
                                            FeesCollectionReportModel model = new FeesCollectionReportModel();
                                            model.setCollected(mReceivablesModelList.get(j).getCollected() * -1);
                                            model.setDues(mReceivablesModelList.get(j).getDues());
                                            model.setMonth(mReceivablesModelList.get(j).getMonth());
                                            model.setTarget(Double.parseDouble(smList.get(0).getTarget_Amount()));
                                            fcrList.add(model);
                                            break;
                                        } else if (j == mReceivablesModelList.size() - 1) {
                                            FeesCollectionReportModel model = new FeesCollectionReportModel();
                                            model.setCollected(0);
                                            model.setDues(0);
                                            model.setMonth(monthsBtwaccademicSession.get(i));
                                            model.setTarget(Double.parseDouble(smList.get(0).getTarget_Amount()));
                                            fcrList.add(model);
                                        }
                                    }
                                }
                                feesCollectionReportAdapter = new FeesCollectionReportAdapter(FeesCollectionReport.this,
                                        fcrList);
                                rv_searchList.setAdapter(feesCollectionReportAdapter);
//                                card3.setVisibility(View.VISIBLE);
                                ll_view.setVisibility(View.VISIBLE);
                                tvBalance.setText(String.valueOf(balance));
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    FeesCollectionReport.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageBox("No results found");
                            AppModel.getInstance().hideLoader();
                        }
                    });

                }

            }
        }).start();


    }

//    private void clearList() {
//        if (feesCollectionReportAdapter != null) {
//            setAdapter();
//            card3.setVisibility(View.GONE);
//        }
//    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_schools:
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);
                smList = DatabaseHelper.getInstance(this).getAllUserSchoolsById(schoolId);
                initSpinners();
                break;
            case R.id.spnSession:
                if (smList != null) {
                    startDate = smList.get(position).getStart_date();
                    endDate = smList.get(position).getEnd_date();
                    String academicSession = smList.get(position).getAcademic_session();
                    monthsBtwaccademicSession = new ArrayList<>();
                    monthsBtwaccademicSession = MonthsBetween(academicSession);
                }
                break;
        }
    }

    private List<String> MonthsBetween(String academicSession) {
        //Problem data is coming twice
        List<String> months = new ArrayList<>();
        String[] split = academicSession.replaceAll(" ", "").split("\\-");
        String start_date = split[0] + "-01";
        String end_date = split[1] + "-12";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(format.parse(start_date));
            finishCalendar.setTime(format.parse(end_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String date;
        do {
            // add one month to date per loop
            date = format.format(beginCalendar.getTime()).toUpperCase();
//            date = SurveyAppModel.getInstance().convertDatetoFormat(date, "yyyy-MM-dd hh:mm:ss a", "yyyy-MM");
            months.add(date);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        while (beginCalendar.before(finishCalendar));

        if (beginCalendar.equals(finishCalendar)) {
            date = format.format(beginCalendar.getTime()).toUpperCase();
            months.add(date);
        }


        return months;
    }

    private void initSpinners() {

        try {
            academic_sessions = new String[smList.size()];
            for (int i = 0; i < smList.size(); i++) {
                academic_sessions[i] = smList.get(i).getAcademic_session();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, academic_sessions);
            spnSession.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:
                setAdapter();
                break;
        }
    }
}
