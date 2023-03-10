package com.tcf.sma.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DeviceAdminInfo;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.work.BackoffPolicy;
import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.tcf.sma.Adapters.BottomFiveAreaManagerAdapter;
import com.tcf.sma.Adapters.BottomFiveCardAdapter;
import com.tcf.sma.Adapters.HighestDuesCardAdapter;
import com.tcf.sma.Adapters.ViewPagerAdapter;
import com.tcf.sma.Fragment.DashboardCalendarFragment;
import com.tcf.sma.Fragment.DashboardEmployeeApprovalsFragment;
import com.tcf.sma.Fragment.DashboardExpenseFragment;
import com.tcf.sma.Fragment.DashboardFinanceFragment;
import com.tcf.sma.Fragment.DashboardStudentFragment;
import com.tcf.sma.Fragment.DashboardSurveyFragment;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.UserInfo;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Interfaces.OnDrawerMenuRefresh;
import com.tcf.sma.Managers.Help.FeedbackDialogManager;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendancePercentageModel;
import com.tcf.sma.Models.BottomFiveSchoolAreaManagerModel;
import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.Fees_Collection.SessionInfoModel;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.Models.ViewSSR.ViewSSRTableModel;
import com.tcf.sma.R;

import com.tcf.sma.Scheduler.KeepAliveService;
import com.tcf.sma.Scheduler.WorkManager.InternetStatusWorkManager;
import com.tcf.sma.Scheduler.WorkManager.NciScheduler;
import com.tcf.sma.Scheduler.WorkManager.NewKeepAliveService;
import com.tcf.sma.SyncClasses.GenericAccountService;
import com.tcf.sma.SyncClasses.SyncUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NewDashboardActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, OnChartValueSelectedListener, SwipeRefreshLayout.OnRefreshListener,
        DrawerActivity.InterfaceForSync/*,AppBarLayout.OnOffsetChangedListener*/ {
    private static DatabaseHelper sDbHelper;
    public int schoolId = 0;
    LineChart lineChart;
    View view;
    BottomFiveCardAdapter bottomFiveAdapter;
    HighestDuesCardAdapter highestDuesCardAdapter;
    BottomFiveAreaManagerAdapter bottomFiveAreaManagerAdapter;
    ArrayList<BottomFiveStudentsModel> bfList = new ArrayList<>();
    ArrayList<HighestDuesStudentsModel> hdList = new ArrayList<>();
    ArrayList<BottomFiveSchoolAreaManagerModel> bfstcList = new ArrayList<>();
    RecyclerView rv_bottom5, rvHighestDuesFromStudent;
    LinearLayout llAttendanceTaken_Count, llAttendanceTaken_Last30DaysCount;
    int presentCount = 0;
    int Count;
    int unApprovedEnrollmentsCount;
    int unApprovedCount;
    int tabPosition = 0;
    boolean schoolSelection = false;
    String userSchoolIds = "";
    TextView CVSyncStatus_btnSyncNow, tv_syncDaysSince, maleRatio, femaleRatio, pendingRecordsSync, tv_thisMonthAttendance, tv_maxCapacity, tv_thisSessionAttendance;
    TextView tv_sessionStartDate, tv_sessionEndDate, tv_workingDays, tv_daysElapsed, tv_daysRemaning, tv_sessionInfo, tv_lastthirtydays, CVAttendanceTaken_tvtitle;
    CardView cv_session_info;
    LinearLayout ll_attendance_taken;
    TextView tv_today;
    SwipeRefreshLayout pullToRefresh;
    boolean startService;
    private List<ViewSSRTableModel> mViewSSRTableList;
    private ViewSSRTableModel mViewSSRTableModel;
    private List<SessionInfoModel> sessionInfoModels = new ArrayList<>();
    private TextView CVCashInHand_tvAmount, CVCashInHand_btnDepositAmount, CVCashInHand_tvDepositisDue, tv_regionName, tv_campusName, tv_areaName, CVCashInHand_selectedSchool_tvAmount;
    private TextView CVssr_tvStudentCount, CVssr_tvOperationsUtilization, CVssr_tvBoys, CVssr_tvGirls, CVssr_btnNewEnrollment;
    private TextView CVssr_tvAttendancePercentage, tv_todayAttendance, CVssr_tvtitle, session_head;
    private TextView CVReceivables_tvTotalDue, CVReceivables_tvReceived, CVReceivables_tvBalance;
    private TextView CVAvgFeesCollection_tvTarget, CVAvgFeesCollection_tvLast30Days, CVAvgFeesCollection_tvThisSession, CVAvgFeesCollection_tvOffTarget, CVAttendanceTaken_tvTodayAttendanceCount, CVAttendanceTaken_tv30DaysAttendanceCount, tvWeakestAttendanceSeeAll, tvHighestDuesFromStudentSeeAll, tv_noData, tv_studentdrppercent, tv_studentEnrollmentCount, tv_inCompleteprofiles, tv_school_attendancePercent,
            tv_unApprovedStudentsCount, tv_unApprvedEnrollmentsCounts, tv_notValidateEnrollment, tv_PendingCounts, tv_lastSyncOn, tv_SyncSuccess, bt_forceSync;
    private RelativeLayout rl_accessiblity;
    private Spinner spnSchools;
    private List<SchoolModel> schoolModels;
    // public static int schoolId = 3;//bydefault a school id is coming from login.
    private String year, month, dayOfmonth;
    private LinkedHashMap<String, Integer> studentAttendanceHashmap;
    private ArrayList<AttendanceModel> attendanceHeaderList;
    private ArrayList<AttendancePercentageModel> AttendancePercentList;
    private List<HolidayCalendarSchoolModel> holidays;
    private RecyclerView rv_schoolSSR;
    private int StudentCount = 0;
    private long passwordChangeDaysLeft;
    private boolean isTodayisHoliday = false;
    private boolean isFeesHidden;
    private LinearLayout ll_cash_receivable;
    private CardView cv2, cv_avg_fees_collection, card1;
    private int roleID = 0;
    private LinearLayout ll_attendance_attTaken;
    private boolean firstTime = true;
    private TextView last30DaysAttendanceCount_btn, todayAttendanceCount_btn;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    //    private AppBarLayout mAppBarLayout;
    private LinearLayout ll_region_area;
    private OnDrawerMenuRefresh drawerMenuRefreshListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListenerForSync(this);
        view = setActivityLayout(this, R.layout.activity_dashboard_new);
        setToolbar("Dashboard", this, false);
        init(view);
        AppModel.getInstance().saveState(this, AppConstants.KEY_STATE, AppConstants.VALUE_DASHBOARD);

        startService = AppModel.getInstance()
                .readFromSharedPreferences(this, AppConstants.START_KEEPALIVE).equals("1");
        isFeesHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION).equals("1");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 1);
        }
        //start service first time imedeatly
        if (!startService) {
            /*FirebaseJobDispatcher dispatcher0 = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            Job myJob0 = dispatcher0.newJobBuilder()
                    .setService(KeepAliveService.class)
                    .setTag("my-unique-tag_0")
                    .setRecurring(false) //true mean repeat it
                    .setTrigger(Trigger.executionWindow(30, 60))
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();

            dispatcher0.mustSchedule(myJob0);*/

            Constraints constraints0 = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest request0 =
                    // Tell which work to execute
                    new OneTimeWorkRequest.Builder(NewKeepAliveService.class)
                            // If you want to delay the start of work by 60 seconds
//                            .setInitialDelay(5, TimeUnit.SECONDS)
                            // Set additional constraints
                            .setConstraints(constraints0)
                            .addTag("my-unique-tag_0")
                            .build();
            try {
                WorkManager.getInstance(this).enqueueUniqueWork("my-unique-tag_0", ExistingWorkPolicy.REPLACE, request0);
            } catch (Exception e) {
                e.printStackTrace();
            }


            AppModel.getInstance().writeToSharedPreferences(this, AppConstants.START_KEEPALIVE, "1");
        }

        /*FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(KeepAliveService.class) // the JobService that will be called
                .setTag("my-unique-tag")// uniquely identifies the job
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setLifetime(Lifetime.FOREVER)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(60 * 110, 60 * 120))
                .build();

        dispatcher.mustSchedule(myJob);*/

        Constraints newConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest newRequest =
                new PeriodicWorkRequest.Builder(NewKeepAliveService.class, 120, TimeUnit.MINUTES)
                        .setConstraints(newConstraints)
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .addTag("my-unique-tag")
                        .setInitialDelay(120, TimeUnit.MINUTES)
        .build();

        try {
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("my-unique-tag", ExistingPeriodicWorkPolicy.KEEP,newRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        int NCF = Integer.parseInt(AppModel.getInstance().readFromSharedPreferences(this,AppConstants.NetworkConnectionFrequencyKey));
        //To cancel scheduled word use this
//        WorkManager.getInstance(this).cancelUniqueWork("internet-status-tag");

        //Internet status work manager
        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NciScheduler.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();


        try {
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("nci-status-tag", ExistingPeriodicWorkPolicy.KEEP,request);
        } catch (Exception e) {
            e.printStackTrace();
        }


        populateSchoolSpinner();
        setTabFragmentAdapter();
//        setTabFragmentAdapter();

        showFeedbackDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserModel user = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
        if (user != null && user.getPassword_change_on_login() == 1) {
            startActivity(new Intent(this, ChangePasswordActivity.class).putExtra("isFromLogin", true));
        }
    }

    private void showFeedbackDialog() {
        boolean isShowFeedbackDialog = HelpHelperClass.getInstance(current_activity).isShowFeedbackDialog();
        if (isShowFeedbackDialog && !AppConstants.feedbackDialogCanceledPressed){
            try {
                FeedbackDialogManager feedbackDialog = new FeedbackDialogManager(current_activity);
//              feedbackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                feedbackDialog.show();
            } catch (Exception e){
                AppModel.getInstance().appendErrorLog(current_activity,"Error in dashboard showFeedbackDialogue:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void performActionOnTouchLayout(View view, final Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                performActionOnTouchLayout(innerView, activity);
            }
        }
    }

    private void setSchoolInfo(int SchoolID) {
        if (SchoolID != 0) {
            try {

                //setting the school info box
                SchoolExpandableModel mod = DatabaseHelper.getInstance(this).getSchoolInfo(schoolId);
                tv_regionName.setText(mod.getRegion());
//                tv_campusName.setText(mod.getCampus());
                tv_areaName.setText(mod.getArea());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tv_regionName.setText("");
//            tv_campusName.setText("");
            tv_areaName.setText("");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {

            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {

//              start service first time imedeatly
                if (!startService) {
                    /*FirebaseJobDispatcher dispatcher0 = new FirebaseJobDispatcher(new GooglePlayDriver(this));
                    Job myJob0 = dispatcher0.newJobBuilder()
                            .setService(KeepAliveService.class)
                            .setTag("my-unique-tag_0")
                            .setRecurring(false)
                            .setTrigger(Trigger.executionWindow(30, 60))
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();

                    dispatcher0.mustSchedule(myJob0);*/

                    Constraints constraints0 = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();

                    OneTimeWorkRequest request0 =
                            // Tell which work to execute
                            new OneTimeWorkRequest.Builder(NewKeepAliveService.class)
                                    // If you want to delay the start of work by 60 seconds
//                                    .setInitialDelay(30, TimeUnit.SECONDS)
                                    // Set additional constraints
                                    .setConstraints(constraints0)
                                    .addTag("my-unique-tag_0")
                                    .build();
                    try {
                        WorkManager.getInstance(this).enqueueUniqueWork("my-unique-tag_0", ExistingWorkPolicy.REPLACE, request0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    AppModel.getInstance().writeToSharedPreferences(this, AppConstants.START_KEEPALIVE, "1");
                }
                /*FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
                Job myJob = dispatcher.newJobBuilder()
                        .setService(KeepAliveService.class) // the JobService that will be called
                        .setTag("my-unique-tag")// uniquely identifies the job
                        .setReplaceCurrent(false)
                        .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                        .setLifetime(Lifetime.FOREVER)
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(60 * 110, 60 * 120))
                        .build();

                dispatcher.mustSchedule(myJob);*/

                Constraints newConstraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                PeriodicWorkRequest newRequest =
                        new PeriodicWorkRequest.Builder(NewKeepAliveService.class, 120, TimeUnit.MINUTES)
                                .setConstraints(newConstraints)
                                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                                .addTag("my-unique-tag")
                                .setInitialDelay(120, TimeUnit.MINUTES)
                                .build();

                try {
                    WorkManager.getInstance(this).enqueueUniquePeriodicWork("my-unique-tag", ExistingPeriodicWorkPolicy.KEEP,newRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
//        try {
//            unregisterReceiver(syncFinishReciever);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            registerReceiver(syncFinishReciever, new IntentFilter(SyncUtils.syncFinishedIntentFilter));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            String currentdate = AppModel.getInstance().getDate();
            String DateOfLogIn = AppModel.getInstance().getDateOfLogin(this);
            if (!DateOfLogIn.isEmpty()) {
                long logOffDaysLeft = AppModel.getInstance().getDaysBetweenDates(DateOfLogIn,
                        currentdate, "yyyy-MM-dd");
                int days;
                if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM)
                    days = 30;
                else
                    days = 1;

                if (logOffDaysLeft >= days) {
                    AppModel.getInstance().logOff(this);
                    finish();
                }
            } else {
                AppModel.getInstance().setDateOfLogin(NewDashboardActivity.this,
                        AppModel.getInstance().getDate());
            }

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
                }

            } else if (check == 1) {
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra("requestCode", 1);
                startActivity(intent);
                finish();
            }

//            calculatePendingRecords(schoolId);
            drawerMenuRefreshListener.onMenuRefresh();
//            setTabFragmentAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void init(View view) {
        sDbHelper = DatabaseHelper.getInstance(this);

        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        tv_areaName = view.findViewById(R.id.tv_areaName);
        tv_regionName = view.findViewById(R.id.tv_regionName);

//        mAppBarLayout = view.findViewById(R.id.app_bar);
//        mAppBarLayout.addOnOffsetChangedListener(this);

//        ll_region_area = view.findViewById(R.id.ll_region_area);

        spnSchools = view.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        try {
            roleID = DatabaseHelper.getInstance(NewDashboardActivity.this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this);

//        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    isShow = true;
//                    showOption();
//                } else if (isShow) {
//                    isShow = false;
//                    hideOption();
//                }
//            }
//        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!schoolSelection)
                    tabPosition = tab.getPosition();
                else
                    schoolSelection = false;

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawerMenuRefreshListener = this;
    }

    private void hideOption() {
//        ll_region_area.setVisibility(View.GONE);
    }

    private void showOption() {
//        ll_region_area.setVisibility(View.VISIBLE);
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPagerAdapter.addFrag(new DashboardStudentFragment(), "Student");
        List<String> allowedModules = null;
        if (getAllowedModulesForAppUsingSchoolId(schoolId) != null){
            allowedModules = Arrays.asList(getAllowedModulesForAppUsingSchoolId(schoolId).split(","));
        }

        if (allowedModules != null && (allowedModules.contains(AppConstants.HREmployeeListingModuleValue) ||
                allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue) ||
                allowedModules.contains(AppConstants.HRResignationModuleValue) ||
                allowedModules.contains(AppConstants.HRTerminationModuleValue))){
            viewPagerAdapter.addFrag(new DashboardEmployeeApprovalsFragment(), "Employees");
            /*if(viewPagerAdapter.getCount() > 4)
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);*/
        }

        if (allowedModules != null  && allowedModules.contains(AppConstants.FinanceModuleValue) || schoolId == 0 && !AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION).equals("1")){
            viewPagerAdapter.addFrag(new DashboardFinanceFragment(), "Finance");
            /*if(viewPagerAdapter.getCount() > 4)
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);*/
        }

        if (allowedModules != null  && allowedModules.contains(AppConstants.ExpenseModuleValue) || schoolId == 0 && !AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Expense).equals("1")){
            viewPagerAdapter.addFrag(new DashboardExpenseFragment(), "Expenses");
            /*if(viewPagerAdapter.getCount() > 4)
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);*/
        }

        //if (allowedModules != null  && allowedModules.contains(AppConstants.SuurveyModuleValue) || schoolId == 0 && !AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Survey).equals("1")){
            /*viewPagerAdapter.addFrag(new DashboardSurveyFragment(), "Survey");
            if(viewPagerAdapter.getCount() > 4)
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);*/
        //}
            if (allowedModules != null && allowedModules.contains(AppConstants.CalendarModuleValue)){
                viewPagerAdapter.addFrag(new DashboardCalendarFragment(), "Calendar");
                /*if(viewPagerAdapter.getCount() > 4)
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);*/
            }

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(tabPosition);
        //for the tablayout width
        dynamicSetTabLayoutMode(tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void dynamicSetTabLayoutMode(TabLayout tabLayout) {
        int tabWidth = calculateTabWidth(tabLayout);
        int screenWidth = getScreenWith();
        if (tabWidth <= screenWidth) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private int calculateTabWidth(TabLayout tabLayout) {
        int tabWidth = 0;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            final View view = tabLayout.getChildAt(i);
            view.measure(0, 0);
            tabWidth += view.getMeasuredWidth();
        }
        return tabWidth;
    }

    public int getScreenWith() {
        return getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public void populateSchoolSpinner() {
        firstTime = true;
        schoolId = AppModel.getInstance().getSelectedSchool(this);

        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchools();
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);

        for (SchoolModel model : schoolModels) {
            userSchoolIds = userSchoolIds + model.getId();
            userSchoolIds += ",";
        }
        if (schoolModels != null && schoolModels.size() > 1) {
            schoolModels.add(new SchoolModel(0, "All"));
            schoolId = 0;

//            ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this,R.layout.spinner_item, schoolModels);
//            schoolSelectionAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
            spnSchools.setAdapter(schoolSelectionAdapter);
            int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
            if (indexOfSelectedSchool == -1)
                spnSchools.setSelection(selectedSchoolIndex);
            else
                spnSchools.setSelection(indexOfSelectedSchool);
        } else if (schoolModels.size() > 0) {
//            ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, R.layout.spinner_item, schoolModels);
//            schoolSelectionAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
            spnSchools.setAdapter(schoolSelectionAdapter);
        }

        if (userSchoolIds.length() > 0) {
            userSchoolIds = userSchoolIds.substring(0, userSchoolIds.lastIndexOf(","));
            AppModel.getInstance().setuserSchool(this, userSchoolIds);
        }


    }

    public int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
        int index = 0;
        for (SchoolModel model : schoolModels) {
            if (model.getId() == appSchoolId)
                return index;
            index++;
        }
        return index;
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
                SchoolModel sm = ((SchoolModel) parent.getItemAtPosition(position));
                schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
                schoolSelection = true;

                AppModel.getInstance().setSpinnerSelectedSchool(NewDashboardActivity.this,
                        schoolId);

                setSchoolInfo(schoolId);
//                setSessionInfoTextviews(schoolId);
//                calculatePendingRecords(schoolId);
                drawerMenuRefreshListener.onMenuRefresh();

                boolean isOpenFinance = false;
                for (SchoolModel model : schoolModels) {
                    if (model.getId() != 0 && !model.getName().equalsIgnoreCase("All")) {
                        List<String> allowedModules = null;
                        if (model.getAllowedModule_App() != null){
                            allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                        }
                        if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue)) {
                            isOpenFinance = true;
                            break;
                        }
                    }
                }

//                if(sm.getId() == 0){
//                    isFeesHidden = false;
//                    showFinanceModuleViews();
//                }

                if (sm.getId() == 0 && isOpenFinance) {
                    isFeesHidden = false;
//                    showFinanceModuleViews();
                } else //                    showFinanceModuleViews();
                    //                    hideFinanceModuleViews();
                    if (sm.getId() == 0 && !isOpenFinance) {
                        isFeesHidden = true;
//                    hideFinanceModuleViews();
                    } else
                        isFeesHidden = (sm.getAllowedModule_App() == null || !Arrays.asList(sm.getAllowedModule_App().split(",")).contains(AppConstants.FinanceModuleValue));

                AppModel.getInstance().showLoader(NewDashboardActivity.this);

                setTabFragmentAdapter();
                AppModel.getInstance().hideLoader();
                enableExpandableList();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

                break;
        }
    }

    public void setTabFragmentAdapter() {
        runOnUiThread(() -> setupViewPager());


    }

//    private void calculatePendingRecords(int SchoolID) {
//        int pendingCount = 0;
//        if (SchoolID == 0) {
//            for (SchoolModel model : schoolModels) {
//                if (model.getId() != 0) {
//                    pendingCount += DatabaseHelper.getInstance(this).getAllPendingRecords(model.getId());
//                }
//            }
//        } else {
//            pendingCount = DatabaseHelper.getInstance(this).getAllPendingRecords(SchoolID);
//        }
//
//        //Separation Approval records comes for any school that's why its added separately
//        pendingCount += DatabaseHelper.getInstance(this).getPendingSeparationRecordsForSync();
//        super.pendingRecordsSync.setText(String.valueOf(pendingCount));
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        try {
            refreshData(); // your code
        } catch (Exception e) {
            e.printStackTrace();
        }
        pullToRefresh.setRefreshing(false);
    }

    private void refreshData() {
        SchoolModel sm = ((SchoolModel) spnSchools.getSelectedItem());
        schoolId = sm.getId();

        setSchoolInfo(schoolId);

        schoolSelection = true;
        setTabFragmentAdapter();
        drawerMenuRefreshListener.onMenuRefresh();
//        calculatePendingRecords(schoolId);

        boolean isOpenFinance = false;
        for (SchoolModel model : schoolModels) {
            if (model.getId() != 0 && !model.getName().equalsIgnoreCase("All")) {
                List<String> allowedModules = null;
                if (model.getAllowedModule_App() != null){
                    allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                }
                if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue)) {
                    isOpenFinance = true;
                    break;
                }
            }
        }
        if (sm.getId() == 0 && isOpenFinance) {
            isFeesHidden = false;
//            showFinanceModuleViews();
        } else //            showFinanceModuleViews();
            //            hideFinanceModuleViews();
            if (sm.getId() == 0 && !isOpenFinance) {
                isFeesHidden = true;
//            hideFinanceModuleViews();
            } else
                isFeesHidden = (sm.getAllowedModule_App() == null || !Arrays.asList(sm.getAllowedModule_App().split(",")).contains(AppConstants.FinanceModuleValue));
    }

    private String getAllowedModulesForAppUsingSchoolId(int schoolId) {
        String allowedModules = "";
        try {
            SchoolModel sm = DatabaseHelper.getInstance(this).getSchoolById(schoolId);
            if (sm.getId() != 0 && !sm.getName().equalsIgnoreCase("All")) {
                if (sm.getAllowedModule_App() != null) {
                    return sm.getAllowedModule_App();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return allowedModules;
    }

    @Override
    public void onSync() {
        if (!AppModel.getInstance().isConnectedToInternet(this)) {
            AppModel.getInstance().showErrorNotification(this, "No Internet Connection", 2);
            Toast.makeText(this, "Internet seems to be offline. Please connect to internet", Toast.LENGTH_SHORT).show();
        } else if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
            if (ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                ContentResolver.cancelSync(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY);
            }
            Bundle b = new Bundle();
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            b.putBoolean("forceSync", true);
            SyncUtils.TriggerRefresh(current_activity, b, SyncProgressHelperClass.SYNC_TYPE_MANUAL_SYNC_ID);
            Toast.makeText(current_activity, "Syncing data please wait...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(current_activity, "Sync is already running please wait for it to complete", Toast.LENGTH_SHORT).show();
        }
    }

}
