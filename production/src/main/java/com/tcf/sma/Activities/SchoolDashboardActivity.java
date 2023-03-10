package com.tcf.sma.Activities;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tcf.sma.Adapters.BottomFiveAdapter;
import com.tcf.sma.Adapters.BottomFiveAreaManagerAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.UserInfo;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.MarshMallowPermission;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendancePercentageModel;
import com.tcf.sma.Models.BottomFiveSchoolAreaManagerModel;
import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.SyncClasses.GenericAccountService;
import com.tcf.sma.SyncClasses.SyncUtils;
import com.tcf.sma.Views.MyMarkerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchoolDashboardActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnChartValueSelectedListener {
    LineChart lineChart;
    View view;
    BottomFiveAdapter bottomFiveAdapter;
    BottomFiveAreaManagerAdapter bottomFiveAreaManagerAdapter;
    ArrayList<BottomFiveStudentsModel> bfList = new ArrayList<>();
    ArrayList<BottomFiveSchoolAreaManagerModel> bfstcList = new ArrayList<>();
    RecyclerView rv_bottom5;
    LinearLayout bottomFiveAreaManagerHeader, bottomFiveStudetnsHeader;
    int presentCount = 0;
    int Count;
    int unApprovedEnrollmentsCount;
    int unApprovedCount;
    private TextView tv_studentdrppercent, tv_studentEnrollmentCount, tv_inCompleteprofiles, tv_school_attendancePercent,
            tv_unApprovedStudentsCount, tv_unApprvedEnrollmentsCounts, tv_notValidateEnrollment, tv_PendingCounts, tv_lastSyncOn, tv_SyncSuccess, bt_forceSync, tv_school;
    private Spinner spnSchools;
    private List<SchoolModel> schoolModels;
    private int schoolId = 0;

    //    public static int schoolId = 3;//bydefault a school id is coming from login.
    private String year, month, dayOfmonth;
    private LinkedHashMap<String, Integer> studentAttendanceHashmap;
    private ArrayList<AttendanceModel> attendanceHeaderList;
    private ArrayList<AttendancePercentageModel> AttendancePercentList;
    private int StudentCount = 0;
    private long passwordChangeDaysLeft;
    private BroadcastReceiver syncFinishReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUpdateAlertsNotification();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Status", "Dashboard");
//        setContentView(R.layout.activity_school_dashboard);
        view = setActivityLayout(this, R.layout.activity_school_dashboard);
        setToolbar("Dashboard", this, false);
        init(view);

//        SurveyAppModel.getInstance().exportDatabse(this);

        populateSchoolSpinner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(syncFinishReciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            registerReceiver(syncFinishReciever, new IntentFilter(SyncUtils.syncFinishedIntentFilter));

            String currentdate = AppModel.getInstance().getDate();
            String PasswordChangeDate = UserInfo.getInstance(this).dateOfPasswordChange(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
            if (PasswordChangeDate != null) {
                passwordChangeDaysLeft = AppModel.getInstance().getDaysBetweenDates(PasswordChangeDate,
                        currentdate, "yyyy-MM-dd");
            } else {
                passwordChangeDaysLeft = 0;
            }
            //check if password is changed or not
            int check = UserInfo.getInstance(this).getPasswordChangeOnLogin(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
            if (check == 0) {
                //Check Expiry date for password
                if (passwordChangeDaysLeft >= 180) {
                    Intent intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("requestCode", 0);
                    startActivity(intent);
                    finish();
                } else {
                    MarshMallowPermission permission = new MarshMallowPermission(this);
                    if (!permission.checkPermissionForExternalStorage()) {
                        permission.requestPermissionForExternalStorage();
                    }

                    StudentCount = DatabaseHelper.getInstance(this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(this));
//        setDropoutRateSection();  // This line has been commented out because the view visibility was set to GONE
                    setEnrollmentCountSection();
                    setUpdateAlertsNotification();
                    setSchoolAttendancePercent();
                    int roleId = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
                    if (roleId == AppConstants.roleId_27_P || roleId == AppConstants.roleId_101_ST || roleId == AppConstants.roleId_109_CM ) {
                        setSchoolBottomFiveCount();
                    } else {
                        setSchoolBottomFiveAreaManagerCount();
                    }
                    setlineChart();
                }
            } else if (check == 1) {
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra("requestCode", 1);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(View view) {

        lineChart = (LineChart) view.findViewById(R.id.barChart);
        lineChart.setOnChartValueSelectedListener(this);


        bt_forceSync = (TextView) view.findViewById(R.id.bt_forceSync);
        tv_PendingCounts = (TextView) view.findViewById(R.id.tv_syncCount);
        tv_SyncSuccess = (TextView) view.findViewById(R.id.tv_successSync);
        tv_lastSyncOn = (TextView) view.findViewById(R.id.tv_lastSyncOn);
        tv_studentdrppercent = (TextView) view.findViewById(R.id.tv_studentdrppercent);
        tv_studentEnrollmentCount = (TextView) view.findViewById(R.id.tv_studentCount);
        tv_inCompleteprofiles = (TextView) view.findViewById(R.id.update_1);
        tv_school_attendancePercent = (TextView) view.findViewById(R.id.tv_school_attendance_percent);
        tv_unApprovedStudentsCount = (TextView) findViewById(R.id.update_3);
        tv_unApprovedStudentsCount.setOnClickListener(this);
        tv_inCompleteprofiles.setOnClickListener(this);
        tv_unApprvedEnrollmentsCounts = (TextView) findViewById(R.id.update_2);
        tv_unApprvedEnrollmentsCounts.setOnClickListener(this);
        tv_notValidateEnrollment = (TextView) findViewById(R.id.update_4);
        tv_notValidateEnrollment.setOnClickListener(this);
        bottomFiveAreaManagerHeader = (LinearLayout) findViewById(R.id.bottom_five_header_am);
        bottomFiveStudetnsHeader = (LinearLayout) findViewById(R.id.bottom_five_header);
        rv_bottom5 = (RecyclerView) findViewById(R.id.rv_bottom_five);
        spnSchools = (Spinner) view.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);
//        tv_school = (TextView) view.findViewById(R.id.tv_school);

        bt_forceSync.setOnClickListener(this);

        working();
    }

    private void populateSchoolSpinner() {
        schoolId = AppModel.getInstance().getSelectedSchool(this);

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchools();
        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
        spnSchools.setSelection(selectedSchoolIndex);


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

    private void setSchoolBottomFiveCount() {
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        String month, year;
        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        bottomFiveAreaManagerHeader.setVisibility(View.GONE);
        bfList = DatabaseHelper.getInstance(this).getBottomFiveStudent(String.valueOf(AppModel.getInstance().getSelectedSchool(this)), month, year);
        if (bfList != null && bfList.size() != 0) {
            bottomFiveAdapter = new BottomFiveAdapter(bfList, this);
            rv_bottom5.setAdapter(bottomFiveAdapter);
        }
    }

    private void setSchoolBottomFiveAreaManagerCount() {
        bottomFiveStudetnsHeader.setVisibility(View.GONE);
        bottomFiveAreaManagerHeader.setVisibility(View.VISIBLE);
        bfstcList = DatabaseHelper.getInstance(this).getBottomFiveAreaMAngerSchool();
        bottomFiveAreaManagerAdapter = new BottomFiveAreaManagerAdapter(bfstcList, this);
        rv_bottom5.setAdapter(bottomFiveAreaManagerAdapter);
    }

    private void setDropoutRateSection() {
        int schoolStudentsStrength, schoolWithdrawalStudentsStrength;

        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        String month, year;
        double dropOutPercentage;

        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        schoolStudentsStrength = DatabaseHelper.getInstance(this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(this));
        schoolWithdrawalStudentsStrength = DatabaseHelper.getInstance(this).getAllWithdrawalStudentsCount(AppModel.getInstance().getSelectedSchool(this), month, year);
        if (schoolWithdrawalStudentsStrength > 0) {
            dropOutPercentage = ((schoolWithdrawalStudentsStrength / (double) StudentCount) * 100);
            tv_studentdrppercent.setText(df.format(dropOutPercentage) + "%");
        } else {
            tv_studentdrppercent.setText("0%");
        }
    }


    private void setSchoolAttendancePercent() {
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfDay = new SimpleDateFormat("dd");
        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        dayOfmonth = dfDay.format(Calendar.getInstance().getTime());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        try {
            if (DatabaseHelper.getInstance(this).AttendanceExists(AppModel.getInstance().getSelectedSchool(this), year + "-" + month + "-" + dayOfmonth)) {
                int Totalabsents = DatabaseHelper.getInstance(this).getStudentAttendancebyMonth(AppModel.getInstance().getSelectedSchool(this), month, year, dayOfmonth);
                int TotalPresents = StudentCount - Totalabsents;
                double MonthPercent = ((double) TotalPresents / (double) StudentCount) * 100;
                tv_school_attendancePercent.setText((df.format(MonthPercent) + "%").equals("NaN%") ? "0%" : df.format(MonthPercent) + "%");
            } else {
                tv_school_attendancePercent.setText("0%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpdateAlertsNotification() {
        SharedPreferences prefs = getSharedPreferences("syncPrefs", Context.MODE_PRIVATE);
        String visitDate = "";
        int pendingCount = DatabaseHelper.getInstance(this).getAllPendingRecords(AppModel.getInstance().getSelectedSchool(this));
        //Separation Approval records comes for any school that's why its added separately
        pendingCount += DatabaseHelper.getInstance(this).getPendingSeparationRecordsForSync();
        //Feedback records
        pendingCount+= HelpHelperClass.getInstance(this).getPendingFeedbackRecordsForSync();

        int SchoolID = AppModel.getInstance().getSelectedSchool(this);
        SchoolAuditModel model = DatabaseHelper.getInstance(this).getSSrNotValidateVisitDate(SchoolID);
        visitDate = AppModel.getInstance().convertDatetoFormat(model.getVisit_date(), "yyyy-MM-dd", "dd-MM-yyyy");
        Count = DatabaseHelper.getInstance(this).getIncompleteProfileCounts(SchoolID);
        unApprovedCount = DatabaseHelper.getInstance(this).getAllUnapprovedStudentsCountDashboard(SchoolID);
        unApprovedEnrollmentsCount = DatabaseHelper.getInstance(this).getPendingEnrollmentscounts(SchoolID);


        //tv_unApprovedStudentsCount.setText(unApprovedCount + " Profiles not validated");
        tv_unApprovedStudentsCount.setVisibility(View.GONE);

        if (unApprovedEnrollmentsCount != -1) {
            tv_unApprvedEnrollmentsCounts.setText(unApprovedEnrollmentsCount + " Forms rejected");
        }
        if (Count > 0) {
            tv_inCompleteprofiles.setText(Count + " Incomplete profiles");
        } else {
            tv_inCompleteprofiles.setText(Count + " Incomplete profile");
        }

        tv_notValidateEnrollment.setVisibility(View.GONE);
//        if (visitDate != null) {
//            if (model.is_approved()) {
//                tv_notValidateEnrollment.setText(visitDate + " Student strength validated");
//            } else {
//                tv_notValidateEnrollment.setText(visitDate + " Student strength not validated");
//            }
//        } else {
//            tv_notValidateEnrollment.setVisibility(View.GONE);
//        }
//        tv_lastSyncOn.setText("Last sync attempt: " + prefs.getString("syncSuccessTime", "Nill"));
        tv_lastSyncOn.setText("Last sync attempt: " + AppModel.getInstance().
                convertDatetoFormat(prefs.getString("syncSuccessTime", "Nill"), " dd-MM-yyyy hh:mm a", " dd-MMM-yy hh:mm a"));
        if (this.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).contains("syncSuccess")) {
            boolean isSyncSuccess = this.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getBoolean("syncSuccess", false);
            tv_SyncSuccess.setText(isSyncSuccess ? "Sync status: Pass" : "Sync status: Fail");
        } else {
            tv_SyncSuccess.setText("Sync status: Nill");
        }
        tv_PendingCounts.setText("Pending records: " + pendingCount);
    }

    private void setEnrollmentCountSection() {
        if (StudentCount > 0) {
            tv_studentEnrollmentCount.setText((StudentCount) + " Students");
        } else {
            tv_studentEnrollmentCount.setText("0 Students");
        }
    }

    public void setlineChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    studentAttendanceHashmap = new LinkedHashMap<>();
                    attendanceHeaderList = DatabaseHelper.getInstance(SchoolDashboardActivity.this).getAllAttendanceHeader(AppModel.getInstance().getSelectedSchool(SchoolDashboardActivity.this));
                    AttendancePercentList = new ArrayList<>();

                    for (int i = 0; i < attendanceHeaderList.size(); i++) {
                        int studentAttendance = DatabaseHelper.getInstance(SchoolDashboardActivity.this).getStudenAttendancebyDate(AppModel.getInstance().getSelectedSchool(SchoolDashboardActivity.this), attendanceHeaderList.get(i).getForDate());
                        studentAttendanceHashmap.put(attendanceHeaderList.get(i).getForDate(), StudentCount - studentAttendance);
                    }
                    for (Map.Entry<String, Integer> entry : studentAttendanceHashmap.entrySet()) {
                        presentCount = entry.getValue();
                        float attendancePercentage = ((float) presentCount / StudentCount) * 100;
                        AttendancePercentList.add(new AttendancePercentageModel(AppModel.getInstance().convertDatetoFormat(entry.getKey(), "yyyy-MM-dd", "dd-MM-yy"), attendancePercentage));
                    }
                    if (AttendancePercentList.size() > 0) {
                        setData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyMarkerView mv = new MyMarkerView(SchoolDashboardActivity.this, R.layout.custom_marker_view_chart, AttendancePercentList);
                                mv.setChartView(lineChart); // For bounds control
                                lineChart.setMarker(mv);
                            }
                        });
                    }
                    attendanceHeaderList = null;
                    studentAttendanceHashmap = null;
                    System.gc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        lineChart.animateXY(2500, 2500);


    }

    private void setData() {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < AttendancePercentList.size(); i++) {
            values.add(new Entry(i + 1, AttendancePercentList.get(i).getPercentage()));
        }

        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Attendance ( from " + AttendancePercentList.get(0).getForDate() + " - " + AttendancePercentList.get(AttendancePercentList.size() - 1).getForDate() + " )");
            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9.5f);
            set1.setDrawFilled(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            set1.setFillColor(Color.GREEN);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);


            // set data
            lineChart.getXAxis().setEnabled(false);
            Description description = new Description();
            description.setText("");
            lineChart.setDescription(description);
            lineChart.setData(data);
        }
    }

    private void working() {
        rv_bottom5.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_bottom5.setLayoutManager(llm);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_forceSync:
                if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY) && !ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                    Bundle b = new Bundle();
                    b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    b.putBoolean("forceSync", true);
                    SyncUtils.TriggerRefresh(current_activity, b, SyncProgressHelperClass.SYNC_TYPE_MANUAL_SYNC_ID);
                    Toast.makeText(current_activity, "Syncing data please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(current_activity, "Sync is already running please wait for it to complete", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_1:
                if (Count > 0) {
                    Intent intent1 = new Intent(this, EnrollmentStatusActivity.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(this, "No Incomplete Profiles to Show", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.update_2:
                if (unApprovedEnrollmentsCount > 0) {
                    Intent intent2 = new Intent(this, EnrollmentStatusActivity.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(this, "No Revised Forms Requested", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.update_3:
                if (unApprovedCount > 0) {
                    Intent intent3 = new Intent(this, DSNUnapprovedStudentsActivity.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(this, "No  Profiles Marked Unapproved", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.update_4:
                if (tv_notValidateEnrollment.getText().toString().trim().equals("No Enrollment Validate")) {
                    Toast.makeText(this, "No Enrollment Validate", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(this, PrincipalSSRValidation.class));
                }
                break;
        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        Log.d("onValueSelected", e.toString() + "----- " + h.toString());
//        Toast.makeText(current_activity, attendanceHeaderList.get((int) e.getX() - 1).getForDate(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSchools:
                schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSelectedSchool(SchoolDashboardActivity.this, schoolId);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
