package com.tcf.sma.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.navigation.NavigationView;
import com.speedchecker.android.sdk.SpeedcheckerSDK;
import com.tcf.sma.Activities.Expense.AddNewRecordActivity;
import com.tcf.sma.Activities.Expense.ClosingActivity;
import com.tcf.sma.Activities.Expense.TransactionListing;
import com.tcf.sma.Activities.Expense.TransferToHoActivity;
import com.tcf.sma.Activities.FeesCollection.CashInHandAndDeposit.CashInHandActivity;
import com.tcf.sma.Activities.FeesCollection.CashInHandReportActivity;
import com.tcf.sma.Activities.FeesCollection.CashReceived.CashCorrectionActivity;
import com.tcf.sma.Activities.FeesCollection.CashReceived.CashReceiptActivity;
import com.tcf.sma.Activities.FeesCollection.CashReceived.WavierActivity;
import com.tcf.sma.Activities.FeesCollection.DepositHistoryReport;
import com.tcf.sma.Activities.FeesCollection.ReceiptBook;
import com.tcf.sma.Activities.FeesCollection.ReceivableStatementActivity;
import com.tcf.sma.Activities.HR.EmployeeDetailsActivity;
import com.tcf.sma.Activities.HR.EmployeeListing;
import com.tcf.sma.Activities.HR.ManageResignationsActivity;
import com.tcf.sma.Activities.HRTCT.EmployeeTCT_EntryActivity;
import com.tcf.sma.Activities.HRTCT.PhasesListingActivity;
import com.tcf.sma.Activities.Help.AboutUsActivity;
import com.tcf.sma.Activities.Help.FrequentlyAskQuestionsActivity;
import com.tcf.sma.Activities.Help.UserManualActivity;
import com.tcf.sma.Activities.SyncProgress.PendingSyncActivity;
import com.tcf.sma.Activities.SyncProgress.SyncProgressActivity;
import com.tcf.sma.Adapters.ExpandableListViewAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Helpers.DbTables.FeesCollection.ErrorLog;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.SessionInfo;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Interfaces.FragmentChangeListener;
import com.tcf.sma.Interfaces.OnDrawerMenuRefresh;
import com.tcf.sma.Managers.Help.FeedbackDialogManager;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.EnabledModules;
import com.tcf.sma.Models.MenuItemsModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.activities.ProjectsActivity;
import com.tcf.sma.Survey.activities.SurveyReportActivity;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.SyncClasses.GenericAccountService;
import com.tcf.sma.SyncClasses.SyncUtils;
import com.tcf.sma.utils.ActionBarToggle;
import com.tcf.sma.utils.FinanceCheckSum;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class DrawerActivity extends AppCompatActivity implements ActionBarToggle.DrawerSlide, NavigationView.OnNavigationItemSelectedListener, FragmentChangeListener, OnDrawerMenuRefresh {
    private static boolean isOverrideAppModules = false;
    Toolbar toolbar;
    TextView mTitle;
    ImageView backButton, editProfileBtn;
    Activity current_activity;
    CircleImageView iv_capture;
    TextView tvUserName, tvRole, tvSchoolName, tvSchoolCode, tv_daysSince, tv_lastSyncOn, tv_syncDaysSince, pendingRecordsSync,tv_lastSyncLabel,pendingSyncRecordLabel
            ,tv_version_name;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Objects.requireNonNull(intent.getAction()).equals(AppConstants.Action_PendingSyncChanged)) {
                if (intent.hasExtra("schoolid")) {
                    int schoolid = intent.getIntExtra("schoolid", 0);
                    calPendingRecords(schoolid);

                    if (intent.hasExtra("startSync") && intent.getBooleanExtra("startSync", false)) {
                        autoSyncStartWhenUploadIsPendingInQueue();
                    }
                }

            }
        }
    };


    private ExpandableListView expListView;
    private List<ArrayList<String>> childItems;
    private ExpandableListViewAdapter listAdapter;
    private ArrayList<MenuItemsModel> parentItems;
    private DrawerLayout drawer;
    private ProgressBar circleProgress;
    BroadcastReceiver receiverSyncVisibility = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                DrawerActivity.this.runOnUiThread(() -> circleProgress.setVisibility(View.VISIBLE));

            } else {
                DrawerActivity.this.runOnUiThread(() -> circleProgress.setVisibility(View.GONE));
                if (SyncProgressHelperClass.getInstance(context).getLastMasterRow() > 0)
                    Log.d("TAG", "onReceive: " );
            }
//            if (intent != null && Objects.requireNonNull(intent.getAction()).equals(AppConstants.Action_receiverSyncVisibility)) {
//
//            }
        }
    };
    private int lastExpandedPosition = -1;
    private TextView btn_change_school;
    private int roleID;
    private InterfaceForSync interfaceForSync;
    private ImageView CVSyncStatus_btnSyncNow;
    private int schoolid = 0;
    private String globalMessage = " Because your app version is lower than our latest version";
    private BroadcastReceiver syncFinishReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int recordsFailed = ErrorLog.getInstance(current_activity).getNoOfFailedRecords();
            if (recordsFailed > 0) {
                AppModel.getInstance().showErrorNotification(context, "Error in syncing " + recordsFailed + " records");
            }

            setAndCalLastSyncSince();
            calPendingRecords(AppModel.getInstance().getSpinnerSelectedSchool(current_activity));
            enableExpandableList();

//            DataSync.getInstance(context).onSyncProgressComplete();

            if (current_activity.getClass() == NewDashboardActivity.class) {
                if (!current_activity.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getString("syncstatus", "true").equals("false")) {
//            populateGraphs();
                    ((NewDashboardActivity) current_activity).setTabFragmentAdapter();
                }
            }


            //If checksum fail then start sync
            try {
                if (!FinanceCheckSum.Instance(new WeakReference<>(current_activity)).isCheckSumSuccessfull()) {
                    Toast.makeText(current_activity, "Checksum failed syncing again...", Toast.LENGTH_SHORT).show();
                    AppModel.getInstance().appendErrorLog(current_activity, "Downloading data again after checksum failed");
                    AppModel.getInstance().startSyncService(current_activity, SyncProgressHelperClass.SYNC_TYPE_AUTO_SYNC_ID);
                }else {
                    AppModel.getInstance().writeBooleanToSharedPreferences(current_activity, AppConstants.FinanceSyncCompleted, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppModel.getInstance().showTCTReminderNotification(current_activity);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._drawer_activity);
        setToolbar("", this, false);
//        DebugDB.getAddressLog();
        try {
            init();
            setHeaderView();
            enableExpandableList();

            SpeedcheckerSDK.init(this);
//            SpeedcheckerSDK.askPermissions(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        roleID = DatabaseHelper.getInstance(DrawerActivity.this).getCurrentLoggedInUser().getRoleId();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.brainstorm_layout);
        circleProgress = (ProgressBar) findViewById(R.id.pb_circle);
        backButton = (ImageView) findViewById(R.id.toolbar_backbutton);
        tvUserName = (TextView) headerView.findViewById(R.id.tvUserName);
        iv_capture = headerView.findViewById(R.id.iv_capture);
        tvRole = (TextView) headerView.findViewById(R.id.tvRole);
        tvSchoolName = (TextView) headerView.findViewById(R.id.tvSchoolName);
        tvSchoolCode = (TextView) headerView.findViewById(R.id.tvSchoolCode);
        tv_lastSyncLabel = findViewById(R.id.tv_lastSyncLabel);
        tv_lastSyncLabel.setPaintFlags(tv_lastSyncLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_lastSyncLabel.setOnClickListener(v -> {
            Intent notifyIntent = new Intent(current_activity, SyncProgressActivity.class);
            startActivity(notifyIntent);
        });

        //for testing crashlytics
        /*ImageView tcfLogo = findViewById(R.id.tcf_logo);
        tcfLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });*/


        editProfileBtn = headerView.findViewById(R.id.btn_edit_profileImg);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(current_activity,UserProfileActivity.class));
                if(EmployeeHelperClass.getInstance(current_activity).getEmployee(DatabaseHelper.getInstance(current_activity).getCurrentLoggedInUser().getId()) != null){
                    Intent intent = new Intent(current_activity, EmployeeDetailsActivity.class);
                    intent.putExtra("empDetailId", DatabaseHelper.getInstance(current_activity).getCurrentLoggedInUser().getId());
                    intent.putExtra("schoolId", AppModel.getInstance().getSpinnerSelectedSchool(current_activity));
                    startActivity(intent);
                } else
                    ShowToast("Employee Data not found.");

            }
        });

//        tv_daysSince = (TextView) findViewById(R.id.tv_daysSince);
//
//        tv_lastSyncOn = (TextView) findViewById(R.id.tv_lastSyncOn);
//        tv_syncDaysSince = (TextView) findViewById(R.id.tv_syncDaysSince);
//        pendingRecordsSync = (TextView) findViewById(R.id.pendingRecordsSync);
        tv_daysSince = (TextView) findViewById(R.id.tv_daysSince);

        tv_lastSyncOn = (TextView) findViewById(R.id.tv_lastSyncOn);
        tv_syncDaysSince = (TextView) findViewById(R.id.tv_syncDaysSince);
        pendingRecordsSync = (TextView) findViewById(R.id.pendingRecordsSync);
        pendingSyncRecordLabel = (TextView) findViewById(R.id.pendingSyncRecordLabel);
        pendingSyncRecordLabel.setPaintFlags(pendingSyncRecordLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        pendingSyncRecordLabel.setOnClickListener(v -> startActivity( new Intent(current_activity, PendingSyncActivity.class)));

        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        setVersionName();

        CVSyncStatus_btnSyncNow = (ImageView) headerView.findViewById(R.id.CVSyncStatus_btnSyncNow);

        btn_change_school = (TextView) headerView.findViewById(R.id.btn_change_school);
        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            btn_change_school.setVisibility(View.VISIBLE);
            findViewById(R.id.ll_daysSince).setVisibility(View.GONE);
//            headerView.findViewById(R.id.ll_recordsPending).setVisibility(View.INVISIBLE);
//            findViewById(R.id.ll_daysSince).setVisibility(View.INVISIBLE);
//            findViewById(R.id.ll_recordsPending).setVisibility(View.INVISIBLE);
        } else {
            btn_change_school.setVisibility(View.GONE);
        }

        int selectedSchoolId = AppModel.getInstance().getSpinnerSelectedSchool(this);
        if (selectedSchoolId > -1) {
            calPendingRecords(selectedSchoolId);
            setAndCalLastSyncSince();
            schoolid = selectedSchoolId;
        }
        btn_change_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(current_activity, SearchSchoolActivity.class));
                drawer.closeDrawer(GravityCompat.START);
//                drawer.closeDrawer(GravityCompat.END);
            }
        });

        CVSyncStatus_btnSyncNow.setOnClickListener(v -> {
            AppModel.getInstance().startSyncService(current_activity, SyncProgressHelperClass.SYNC_TYPE_MANUAL_SYNC_ID);
            /*if (!AppModel.getInstance().isConnectedToInternet(current_activity)) {
                AppModel.getInstance().showErrorNotification(this, "No Internet Connection", 2);
                Toast.makeText(current_activity, "Internet seems to be offline. Please connect to internet", Toast.LENGTH_SHORT).show();
            } else if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {

                if (ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                    ContentResolver.cancelSync(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY);
                }
                Bundle b = new Bundle();
                b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                b.putBoolean("forceSync", true);
                SyncUtils.TriggerRefresh(b);
                Toast.makeText(current_activity, "Syncing data please wait...", Toast.LENGTH_SHORT).show();

                //this will tell sync button clicked
                AppModel.getInstance().writeToSharedPreferences(DrawerActivity.this, AppConstants.syncActionPerformedFrom, "syncbutton");

                startMenuSyncRotationAnimation();

            } else {
                Toast.makeText(current_activity, "Sync is already running please wait for it to complete", Toast.LENGTH_SHORT).show();
            }*/
//                if (interfaceForSync != null)
//                    interfaceForSync.onSync();
        });

        if (AppModel.getInstance().getSelectedSchool(this) > 0) {
            tvSchoolName.setText(DatabaseHelper.getInstance(this).getSchoolById(AppModel.getInstance().getSelectedSchool(this)).getName());
//            tvSchoolCode.setText(DatabaseHelper.getInstance(this).getSchoolById(SurveyAppModel.getInstance().getSelectedSchool(this)).getEMIS());
            tvSchoolCode.setText(DatabaseHelper.getInstance(this).getSchoolById(AppModel.getInstance().getSelectedSchool(this)).getId() + "");
        }
        navigationView.setNavigationItemSelectedListener(this);
        AppModel.getInstance().setStatusBarColor(this);

//        Intent intent = new Intent(AppConstants.Action_receiverSyncVisibility);
//        sendBroadcast(intent);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                    DrawerActivity.this.runOnUiThread(() -> circleProgress.setVisibility(View.VISIBLE));

                } else {
                    DrawerActivity.this.runOnUiThread(() -> circleProgress.setVisibility(View.GONE));

                    //If checksum fail then start sync
//                    try {
//                        if (!FinanceCheckSum.Instance(new WeakReference<>(current_activity)).isCheckSumSuccessfull()){
//                            SurveyAppModel.getInstance().startSyncService(current_activity);
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 2000);
    }

    private void setVersionName() {
        tv_version_name.setText("Ver "+AppModel.getInstance().getAppVersionWithBuildNo(DrawerActivity.this));
    }

    private void startMenuSyncRotationAnimation() {
        new Thread(() -> {
            Animation rotation = AnimationUtils.loadAnimation(DrawerActivity.this, R.anim.sync_rotation);
            rotation.setFillAfter(true);
            runOnUiThread(() -> CVSyncStatus_btnSyncNow.startAnimation(rotation));
        }).start();
    }

    private void setHeaderView() {
        UserModel userModel = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
        if (userModel != null) {
            SchoolModel schoolModel = DatabaseHelper.getInstance(this).getSchoolById(userModel.getDefault_school_id());
            switch (userModel.getRole().toUpperCase()) {
                case "P":
                    tvRole.setText(R.string.principal);
                    break;
                case "AM":
                    tvRole.setText(R.string.area_manager);
                    break;
                case "AC":
                    tvRole.setText(R.string.area_coordinator);
                    break;
                case "ST":
                    tvRole.setText("Senior Teacher");
                    break;
                case "AA":
                    tvRole.setText("Admin Assistant");
                    break;
                case "V":
                    tvRole.setText("Viewer");
                    break;
                case "AEM":
                    tvRole.setText("Area Education Manager");
                    break;

            }
            tvUserName.setText(userModel.getFirstname() + " " + userModel.getLastname());

            try {
                File userImagePath = new File(EmployeeHelperClass.getInstance(current_activity).getUserImagePath(userModel.getId()));

                byte[] data = AppModel.getInstance().bitmapToByte(Compressor.getDefault(current_activity).compressToBitmap(userImagePath));
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv_capture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (current_activity != null) {
            if (current_activity.getClass() != NewDashboardActivity.class) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    if (lastExpandedPosition != -1) {
                        expListView.collapseGroup(lastExpandedPosition);
                    }
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    super.onBackPressed();
            } else {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    if (lastExpandedPosition != -1) {
                        expListView.collapseGroup(lastExpandedPosition);
                    }
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Exit");
                    builder.setMessage("Are you sure you want to exit?");
                    builder.setPositiveButton("Yes", (dialog, which) -> finish());
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        item.setCheckable(true);
//        item.setChecked(true);
        return true;
    }

    /**
     * Load fragment
     *
     * @param title       title of fragment
     * @param fragment    fragment object
     * @param AppmodelTag fragment tag to determine the adapter for search
     */
    private void loadFragment(final String AppmodelTag, final String title, final Fragment fragment) {
        DrawerActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment newFragment = fragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .detach(newFragment)
                        .attach(newFragment)
                        .commit();
                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                fragmentTransaction.commit();
                toolbar.setTitle(title);
                AppModel.getInstance().fragmentTag = AppmodelTag;
                drawer.closeDrawer(GravityCompat.START);
            }
        });


    }

    /**
     * @param title
     * @param fragment
     * @param tag
     */
    private void loadFragment(String title, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .detach(newFragment)
                .attach(newFragment)
                .commit();
//        fragmentTransaction.add(R.id.fragment_container, newFragment,tag);
        fragmentTransaction.replace(R.id.fragment_container, newFragment, tag);
//        fragmentTransaction.addToBackStack(newFragment.getClass().getName());
        fragmentTransaction.commit();
        toolbar.setTitle(title);
        AppModel.getInstance().fragmentTag = tag;
        drawer.closeDrawer(GravityCompat.START);
    }

    public void enableExpandableList() {
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
        expListView.setGroupIndicator(null);

        prepareListData();
        listAdapter = new ExpandableListViewAdapter(this, parentItems, childItems);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        final String isFeesCollectionHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION);
        final boolean isEmployeeHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Employee).equals("1");
        final boolean isStudentVisitFormHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_StudentVisitForm).equals("1");
        final boolean isExpenseHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Expense).equals("1");
        EnabledModules enabledModules = AppModel.getInstance().getEnabledModules(new WeakReference<>(current_activity));
//        final int roleID = DatabaseHelper.getInstance(DrawerActivity.this).getCurrentLoggedInUser().getRoleId();

        final boolean ifThreeModulesOpenAndScVisitOpen = !isEmployeeHidden && !isFeesCollectionHidden.equals("1") && !isExpenseHidden
                && !isStudentVisitFormHidden;

        final boolean anyTwoModulesOpen = ((!isEmployeeHidden && !isFeesCollectionHidden.equals("1") && isExpenseHidden) ||
                (isEmployeeHidden && !isFeesCollectionHidden.equals("1") && !isExpenseHidden) ||
                (!isEmployeeHidden && isFeesCollectionHidden.equals("1") && !isExpenseHidden));
        final boolean ifAnyTwoModulesOpenAndScVisitOpen = anyTwoModulesOpen && !isStudentVisitFormHidden;

        final boolean ifAnyTwoModulesOpenAndScVisitClose = anyTwoModulesOpen && isStudentVisitFormHidden;

        final boolean anyOneModuleOpen = ((!isEmployeeHidden && isFeesCollectionHidden.equals("1") && isExpenseHidden) ||
                (isEmployeeHidden && !isFeesCollectionHidden.equals("1") && isExpenseHidden) ||
                (isEmployeeHidden && isFeesCollectionHidden.equals("1") && !isExpenseHidden));

        final boolean ifAnyOneModuleOpenAndScVisitOpen = anyOneModuleOpen && !isStudentVisitFormHidden;

        final boolean ifOnlyOneModuleOpenAndScVisitClose = anyOneModuleOpen && isStudentVisitFormHidden;

        final boolean ifThreeModulesCloseAndScVisitOpen = (isEmployeeHidden && isFeesCollectionHidden.equals("1") && isExpenseHidden && !isStudentVisitFormHidden);

        final boolean ifThreeModulesCloseAndScVisitClose = (isEmployeeHidden && isFeesCollectionHidden.equals("1") && isExpenseHidden && isStudentVisitFormHidden);

        final  boolean ifThreeModulesOpenAndScVisitClose = !isEmployeeHidden && !isFeesCollectionHidden.equals("1") && !isExpenseHidden
                && isStudentVisitFormHidden;

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (parentItems.get(groupPosition).getItem_name()) {
                    case "Dashboard":
                        if (current_activity.getClass() != NewDashboardActivity.class)
                            startActivity(new Intent(current_activity, NewDashboardActivity.class));
                        else
                            drawer.closeDrawer(GravityCompat.START);
                        break;

                    case "Finance":
                        boolean FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(current_activity,
                                AppConstants.FinanceSyncCompleted, false);

                        if (!FinanceSyncCompleted) {
                            Toast.makeText(current_activity, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        break;

                    case "School Forms":
                        setSchoolVisitMenus(enabledModules);
                        break;

                    case "Change Password":
                        setChangePasswordMenus();
                        break;

                    case "Logout":
                        setLogoutMenus();
                        break;
                }

                /*switch (groupPosition) {
                    case 0:
//                        switch (childPosition) {
//                            case 0:
//                        if (roleID == 27 || roleID == 101 || roleID == 102) {
                        if (current_activity.getClass() != NewDashboardActivity.class)
                            startActivity(new Intent(current_activity, NewDashboardActivity.class));
                        else
                            drawer.closeDrawer(GravityCompat.START);
//                        }

//                                else
//                                    drawer.closeDrawer(GravityCompat.START);
//                                break;
//                            case 1:
//                                startActivity(new Intent(current_activity, School_list_Activity.class));
//                                drawer.closeDrawer(GravityCompat.START);
//                                break;
////                            case 2:
//                                startActivity(new Intent(current_activity, NewDashboardActivity.class));
//                                drawer.closeDrawer(GravityCompat.START);
//                                break;

                        break;
                    case 2:
                        break;

                    case 3:
                        if (ifThreeModulesCloseAndScVisitOpen) {
                            setSchoolVisitMenus(enabledModules);
//                            setChangePasswordMenus();
                        } else if (ifThreeModulesCloseAndScVisitClose){
                            setChangePasswordMenus();
                        }
                        break;
                    case 4:
                        if (ifAnyOneModuleOpenAndScVisitOpen) {
                            setSchoolVisitMenus(enabledModules);
                        }
                        else if (ifThreeModulesCloseAndScVisitOpen || ifOnlyOneModuleOpenAndScVisitClose ) {
                            setChangePasswordMenus();
                        }
//                        else if (ifThreeModulesCloseAndScVisitClose){
//                            setAboutUsMenus();
//                        }

                        break;
                    case 5:
                        if (ifAnyTwoModulesOpenAndScVisitOpen) {
                            setSchoolVisitMenus(enabledModules);
                        } else if (ifAnyTwoModulesOpenAndScVisitClose || ifAnyOneModuleOpenAndScVisitOpen) {
                            setChangePasswordMenus();
                        }
//                        else if (ifThreeModulesCloseAndScVisitOpen || ifOnlyOneModuleOpenAndScVisitClose ) {
//                            setAboutUsMenus();
//                        }
                        else if (ifThreeModulesCloseAndScVisitClose){
                            setLogoutMenus();
                        }

                        break;
                    case 6:
                        if (ifThreeModulesOpenAndScVisitOpen) {
                            setSchoolVisitMenus(enabledModules);
                        } else if (ifAnyTwoModulesOpenAndScVisitOpen || ifThreeModulesOpenAndScVisitClose) {
                            setChangePasswordMenus();
                        }
//                        else if (ifAnyTwoModulesOpenAndScVisitClose || ifAnyOneModuleOpenAndScVisitOpen) {
//                            setAboutUsMenus();
//                        }
                        else if (ifThreeModulesCloseAndScVisitOpen || ifOnlyOneModuleOpenAndScVisitClose){
                            setLogoutMenus();
                        }

                        break;
                    case 7:
                        if (ifThreeModulesOpenAndScVisitOpen) {
                            setChangePasswordMenus();
                        }
//                        else if (ifAnyTwoModulesOpenAndScVisitOpen || ifThreeModulesOpenAndScVisitClose){
//                            setAboutUsMenus();
//                        }
                        else if (ifAnyTwoModulesOpenAndScVisitClose || ifAnyOneModuleOpenAndScVisitOpen){
                            setLogoutMenus();
                        }

                        break;
                    case 8:
//                        if (ifThreeModulesOpenAndScVisitOpen) {
//                            setAboutUsMenus();
//                        }else
                        if (ifThreeModulesOpenAndScVisitClose || ifAnyTwoModulesOpenAndScVisitOpen) {
                            setLogoutMenus();
                        }
                        break;
                    case 9:
                        setLogoutMenus();
                        break;
                }*/
                return false;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                EnabledModules enabledModules = AppModel.getInstance().getEnabledModules(new WeakReference<>(current_activity));

                switch (parentItems.get(groupPosition).getItem_name()) {
                    case "Dashboard":
                        break;

                    case "Student":
                        setStudentMenus(groupPosition, childPosition, enabledModules);
                        break;

                    case "Employees":
                        setEmployeeMenus(groupPosition, childPosition, enabledModules);
                        break;

                    case "Finance":
                        setFinanceMenus(groupPosition, childPosition, enabledModules);
                        break;

                    case "Help":
                        setAboutUsMenus(groupPosition, childPosition);
                        break;

                    case "Reports":
//                        if (enabledModules.isModuleFinanceEnabled())
//                            setReportMenusForFinance(groupPosition, childPosition, isFeesCollectionHidden, enabledModules);
//                        else
                            setReportMenusIfFinanceDisabled(groupPosition, childPosition, enabledModules);
                        break;

                    case "Expenses":
                        setExpenseMenus(childPosition, enabledModules);
                        break;

                }

                /*switch (groupPosition) {
                    case 0:
//                        if (roleID == 103){
//                            switch (childPosition){
//                                case 0:
//                                    if (current_activity.getClass() != NewDashboardActivity.class){
//                                        startActivity(new Intent(current_activity, NewDashboardActivity.class));
//                                        drawer.closeDrawer(GravityCompat.START);
//                                    }
//                                    else
//                                        drawer.closeDrawer(GravityCompat.START);
//                                    break;
//                                case 1:
//                                    startActivity(new Intent(current_activity, SearchSchoolActivity.class));
//                                    drawer.closeDrawer(GravityCompat.START);
//                                    break;
//                            }
//                        }
                        break;
                    case 1:
                        setStudentMenus(childPosition, enabledModules);
                        break;
                    case 2:
                        if (!isEmployeeHidden) {
                            setEmployeeMenus(childPosition, enabledModules);
                        } else if (!isFeesCollectionHidden.equals("1")) {
                            setFinanceMenus(childPosition, enabledModules);
                        } else if (!isExpenseHidden) {
                            setExpenseMenus(childPosition, enabledModules);
                        } else {
                            setReportMenusIfFinanceDisabled(childPosition, enabledModules);
                        }
                        break;

                    case 3:
                        if (!isEmployeeHidden && !isFeesCollectionHidden.equals("1")) {
                            setFinanceMenus(childPosition, enabledModules);
                        } else if ((!isEmployeeHidden || !isFeesCollectionHidden.equals("1"))
                                && !isExpenseHidden) {
                            setExpenseMenus(childPosition, enabledModules);
                        } else if (!isFeesCollectionHidden.equals("1")) {
                            setReportMenusForFinance(childPosition, isFeesCollectionHidden, enabledModules);
                        } else if (isFeesCollectionHidden.equals("1")) {
                            setReportMenusIfFinanceDisabled(childPosition, enabledModules);
                        }

                        break;
                    case 4:
                        if (!isFeesCollectionHidden.equals("1") && !isEmployeeHidden && !isExpenseHidden) {
                            setExpenseMenus(childPosition, enabledModules);
                        } else if (ifThreeModulesCloseAndScVisitClose) {
                            setAboutUsMenus(childPosition);
                        } else if (!isFeesCollectionHidden.equals("1")) {
                            setReportMenusForFinance(childPosition, isFeesCollectionHidden, enabledModules);
                        } else if (isFeesCollectionHidden.equals("1")) {
                            setReportMenusIfFinanceDisabled(childPosition, enabledModules);
                        }
                        break;
                    case 5:

                        if (ifThreeModulesCloseAndScVisitOpen || ifOnlyOneModuleOpenAndScVisitClose) {
                            setAboutUsMenus(childPosition);
                        } else if (!isFeesCollectionHidden.equals("1")) {
                            setReportMenusForFinance(childPosition, isFeesCollectionHidden, enabledModules);
                        } else if (isFeesCollectionHidden.equals("1")) {
                            setReportMenusIfFinanceDisabled(childPosition, enabledModules);
                        }

                        break;
                    case 6:
                        if (ifAnyTwoModulesOpenAndScVisitClose || ifAnyOneModuleOpenAndScVisitOpen) {
                            setAboutUsMenus(childPosition);
                        }
                        break;
                    case 7:
                        if (ifAnyTwoModulesOpenAndScVisitOpen || ifThreeModulesOpenAndScVisitClose) {
                            setAboutUsMenus(childPosition);
                        }
                        break;
                    case 8:
                        if (ifThreeModulesOpenAndScVisitOpen) {
                            setAboutUsMenus(childPosition);
                        }
                        break;

                }*/
                if (current_activity.getClass() != NewDashboardActivity.class && !AppConstants.isFeedbackDialogShowing) {
                    current_activity.finish();
                }


                return false;
            }
        });
    }

    private void prepareListData() {
        {
            parentItems.clear();
            childItems.clear();
            ArrayList<String> child;
            MenuItemsModel ParentItemsModel;

            //id role ==27 then user is principal,101 Senior Teacher,102 Admin Assistance,103 Viewer and if role ==8 or 9 than area manager and area co-ordinator respectively.
            int roleID = DatabaseHelper.getInstance(DrawerActivity.this).getCurrentLoggedInUser().getRoleId();

            ParentItemsModel = new MenuItemsModel("Dashboard", getResources().getDrawable(R.mipmap.school));
            parentItems.add(ParentItemsModel);
            child = new ArrayList<>();
//            if (roleID == 103){
//                child.add("Dashboard");
//                child.add("Search School");
//            }
            childItems.add(child);


            ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.student), getResources().getDrawable(R.mipmap.students));
            parentItems.add(ParentItemsModel);
            child = new ArrayList<>();

            if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST ||  roleID == AppConstants.roleId_109_CM || roleID == AppConstants.roleId_102_AA) {
                child.add("GR Register");
                child.add("New Enrollment");
                if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_StudentTransfer).equals("1")) {
                    child.add("Student Transfer");
                }
                child.add("Attendance");
                if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_PROMOTION).equals("1")) {
                    child.add("Promotions");
                }
                if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_GRADUATION).equals("1")) {
                    child.add("Graduations");
                }
                child.add("Withdrawal / Drop-Off");

                if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Set_Fess).equals("1")
                        && FeesCollection.getInstance(this).isMonthlyFeesNeedToAddForSchools(AppModel.getInstance().getAllUserSchoolsCommaSeparated(this)))
                    child.add("Set Fees");
//                child.add("View SSR");
            } else if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                child.add("View Profile");
//                child.add("View SSR");
            }
//            else {
//                child.add("View Profile");
//            }
            childItems.add(child);

            if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Employee).equals("1")) {
                ParentItemsModel = new MenuItemsModel(getString(R.string.employee), getResources().getDrawable(R.mipmap.employees));
                parentItems.add(ParentItemsModel);
                child = new ArrayList<>();
                if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM || roleID == AppConstants.roleId_102_AA ||
                        roleID == AppConstants.roleId_7_AEM || roleID == AppConstants.roleId_9_AC) {
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) {
                        child.add("View Employees");
                    }
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                            && (EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0,"",""))) {
                        child.add("Separation Approval");
                    }
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                        child.add("View Separation");
                    }
                    /*if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")) {
                        child.add("Manage Leaves");
                    }*/
                    /*if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")) {
                        child.add("Employee Attendance");
                    }*/
                    if ((roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM) && !AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_TCTEntry).equals("1")) {
                        child.add("TCT Entry");
                        child.add("Previous TCT Registrations");
                    }

                } else if (roleID == AppConstants.roleId_103_V ){
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) {
                        child.add("View Employees");
                    }
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                            && (EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0,"",""))) {
                        child.add("Separation Approval");
                    }
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                        child.add("View Separation");
                    }
                }
                childItems.add(child);
            }

            if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION).equals("1")) {
                ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.feesCollection), getResources().getDrawable(R.mipmap.finance_new));
                parentItems.add(ParentItemsModel);
                child = new ArrayList<>();
                if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM) {
                    if (FeesCollection.getInstance(this).getClassSectionForFeeEntryBySchoolId(AppModel.getInstance().getAllUserSchoolsForFinance(this)).size() == 0) {
                        if (FeesCollection.getInstance(this).getUnuploadedCountFeeEntry(AppModel.getInstance().getAllUserSchoolsForFinance(this)) == 0) {
                            child.add("Receive Cash");
//                            child.add("Account Statement");
                            child.add("Cash In Hand");
//                    child.add("View Receivables");
                            child.add("Receipt Correction");
                            child.add("Waiver");
                        } else
                            child.add("Fee Entry");
                    } else
                        child.add("Fee Entry");
                } else if (roleID == AppConstants.roleId_102_AA) {
                    if (FeesCollection.getInstance(this).getClassSectionForFeeEntryBySchoolId(AppModel.getInstance().getAllUserSchoolsForFinance(this)).size() == 0) {
                        if (FeesCollection.getInstance(this).getUnuploadedCountFeeEntry(AppModel.getInstance().getAllUserSchoolsForFinance(this)) == 0) {
                            child.add("Receive Cash");
//                            child.add("Account Statement");
                            child.add("Cash In Hand");
//                    child.add("View Receivables");
                        } else
                            child.add("Fee Entry");
                    } else
                        child.add("Fee Entry");
                }

                //no option should show when its viewer
                /*else if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                    child.add("Account Statement");
//                    child.add("View Receivables");
                }*/

                childItems.add(child);
            }

            if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Expense).equals("1") &&
                    (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM || roleID == AppConstants.roleId_102_AA ||
                            roleID == AppConstants.roleId_7_AEM)) {
                ParentItemsModel = new MenuItemsModel(getString(R.string.expenses), getResources().getDrawable(R.mipmap.expense_menu));
                parentItems.add(ParentItemsModel);
                child = new ArrayList<>();
                child.add("New Transactions");
                child.add("View Transactions");
                child.add("Petty Cash Closing");
                child.add("Transfer to Ho");


                childItems.add(child);
            }

            boolean isFeesHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION).equals("1");
//
//            if (!isFeesHidden) {
//                ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.reports),
//                        getResources().getDrawable(R.mipmap.reports));
//                parentItems.add(ParentItemsModel);
//                child = new ArrayList<>();
//                child.add("Student Reports");
//                child.add("School Reports");
//                child.add("Student Collection Report");
//                child.add("Deposit History");
//                child.add("Fees Collection Report");
//                child.add("SSR Report");
//                child.add("Profile Validation");
//                child.add("Student Strength Validation");
//                child.add("Log report");
//
////                child.add("Fees Collection Report");
////            child.add("Validation Reports");
//                childItems.add(child);
//            }

            if (roleID == AppConstants.roleId_27_P ||
                    roleID == AppConstants.roleId_101_ST ||
                    roleID == AppConstants.roleId_109_CM ||
                    roleID == AppConstants.roleId_102_AA ||
                    roleID == AppConstants.roleId_103_V ||
                    roleID == AppConstants.roleId_7_AEM) {
                ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.reports),
                        getResources().getDrawable(R.mipmap.reports_new));
                parentItems.add(ParentItemsModel);
                child = new ArrayList<>();
                if (AppModel.getInstance().getEnabledModules(new WeakReference<>(current_activity)).isModuleFinanceEnabled())
                    child.add("Receivable Statement");
                child.add("Class");
                child.add("Attendance");
                if (!isFeesHidden) {
                    child.add("Receipt Book");
                }
                if (!isFeesHidden) {
                    child.add("Deposit History");
                }

                child.add("Log");
                child.add("Student Error Log");
                if (!isFeesHidden) {
                    child.add("Fees Count(For Debugging)");
                }
                if (!isFeesHidden) {
                    child.add("Cash in Hand Report");
                }

                child.add("Taaluq Student Data");
                child.add("Speed Test");


                childItems.add(child);
            }

            if (!AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_StudentVisitForm).equals("1")) {
                ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.schoolvisit),
                        getResources().getDrawable(R.mipmap.task));
                parentItems.add(ParentItemsModel);
                child = new ArrayList<>();
                childItems.add(child);
            }

            ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.changePassword),
                    getResources().getDrawable(R.mipmap.change_password_new_2));
            parentItems.add(ParentItemsModel);
            child = new ArrayList<>();
            childItems.add(child);

            ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.help),
                    getResources().getDrawable(R.mipmap.help_icon));
            parentItems.add(ParentItemsModel);
            child = new ArrayList<>();
//           child.add("Policies");
            child.add(getString(R.string.user_manual));
            child.add(getString(R.string.faqs));
//            child.add(getString(R.string.feedback));
            child.add(getString(R.string.rate_us));
            child.add(getString(R.string.aboutUs));
            childItems.add(child);

            ParentItemsModel = new MenuItemsModel(getResources().getString(R.string.logout),
                    getResources().getDrawable(R.mipmap.logout));
            parentItems.add(ParentItemsModel);
            child = new ArrayList<>();
            childItems.add(child);

        }
    }

    @Override
    public void Replacefragment(Fragment fragment, String Title) {
        if (Title.equals(getString(R.string.new_enrollment))) {
            loadFragment(Title, fragment, Title);
        } else
            loadFragment(Title, Title, fragment);
    }

    public View setActivityLayout(Activity context, int layout) {
        FrameLayout frameLayout = (FrameLayout) context.findViewById(R.id.fragment_container);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View screen = layoutInflater.inflate(layout, null, false);
        frameLayout.addView(screen);
        return screen;
    }

    public void changeTitle(String title) {
        toolbar.setTitle(title);
    }

    public void setToolbar(String Title, Activity activity, boolean enableAddButton) {
        current_activity = activity;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(Title);
//        mTitle = findViewById(R.id.toolbar_title);
//        if (!Title.isEmpty())
//            mTitle.setText(Title);
//        mTitle.setGravity(Gravity.CENTER);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarToggle toggle = new ActionBarToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
//        toggle.setHomeAsUpIndicator(R.drawable.ic_menu); //set your own

        drawer.setDrawerListener(toggle);
        toggle.setOnDrawerSlide(this);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastExpandedPosition != -1) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
//                if (drawer.isDrawerOpen(GravityCompat.END)) {
//                    drawer.closeDrawer(GravityCompat.END);
//                } else {
//                    drawer.openDrawer(GravityCompat.END);
//                }
            }
        });
//        if (enableAddButton) {
//            buttonAdd.setVisibility(View.VISIBLE);
    }

    public void MessageBox(String message) {
        MessageBox(message, false, null);
    }

    public void MessageBox(String message, final boolean finishOnClose) {
        MessageBox(message, finishOnClose, null);
    }

    public void ShowToast(String message) {
        Toast.makeText(current_activity, message, Toast.LENGTH_SHORT).show();
    }

    public void MessageBox(String message, final boolean finishOnClose, final Intent intentToStart) {
        android.app.AlertDialog msg = new android.app.AlertDialog.Builder(this).create();
//        msg.setTitle("Warning");
        msg.setCancelable(false);
        msg.setMessage(message);
        msg.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (intentToStart != null) startActivity(intentToStart);
                if (finishOnClose) finish();
            }
        });
        try {
            msg.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSlide() {
        try {
            AppModel.getInstance().hideSoftKeyboard(DrawerActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosed() {
        if (lastExpandedPosition != -1) {
            expListView.collapseGroup(lastExpandedPosition);
        }
    }

    public void setStudentMenus(int parentPosition, int childPosition, EnabledModules enabledModules) {
        if (enabledModules.isModuleStudentEnabled()) {
            switch (childItems.get(parentPosition).get(childPosition)) {
                case "GR Register":
                    startActivity(new Intent(current_activity, StudentProfileSearchActivity.class));
                    AppModel.getInstance().fragmentTag = getString(R.string.student_gr_register);
                    drawer.closeDrawer(GravityCompat.START);
                    break;

                case "New Enrollment":
                    startActivity(new Intent(current_activity, EnrollmentStatusActivity.class));
                    break;

                case "Student Transfer":
                    if (enabledModules.isModuleStudentTransferEnabled()) {
                        startActivity(new Intent(current_activity, StudentSearchForTransferActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.student_transfer_text);
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Transfer is disabled" + globalMessage);
                    }
                    break;

                case "Attendance":
                    startActivity(new Intent(current_activity, AttendanceActivity.class));
                    break;

                case "Promotions":
                    if (enabledModules.isModuleStudentPromotionEnabled()) {
                        startActivity(new Intent(current_activity, PromotionActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.promotions);
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Promotion is disabled" + globalMessage);
                    }
                    break;

                case "Graduations":
                    if (enabledModules.isModuleStudentGraduationEnabled()) {
                        startActivity(new Intent(current_activity, GraduationActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.Graduations);
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Graduation is disabled" + globalMessage);
                    }
                    break;

                case "Withdrawal / Drop-Off":
                    startActivity(new Intent(current_activity, StudentDropoutSearchActivity.class));
                    AppModel.getInstance().fragmentTag = getString(R.string.student_dropout_text);
                    break;

                case "View Profile":
                    startActivity(new Intent(current_activity, StudentProfileSearchActivity.class));
                    AppModel.getInstance().fragmentTag = getString(R.string.student_gr_register);
                    break;

                case "Set Fees":
                    startActivity(new Intent(current_activity, StudentFeeSetActivity.class));
                    AppModel.getInstance().fragmentTag = getString(R.string.student_fee_set_text);
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
        }
    }

    /*public void setStudentMenus(int childPosition, EnabledModules enabledModules) {
        if (enabledModules.isModuleStudentEnabled()) {
            if (roleID == AppConstants.roleId_27_P ||
                    roleID == AppConstants.roleId_101_ST ||
                    roleID == AppConstants.roleId_109_CM ||
                    roleID == AppConstants.roleId_102_AA) {
                switch (childPosition) {
                    case 0:
                        startActivity(new Intent(current_activity, StudentProfileSearchActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.student_gr_register);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case 1:
                        startActivity(new Intent(current_activity, EnrollmentStatusActivity.class));
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_StudentTransfer).equals("1")) {
                            if (enabledModules.isModuleStudentTransferEnabled()) {
                                startActivity(new Intent(current_activity, StudentSearchForTransferActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.student_transfer_text);
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Transfer is disabled" + globalMessage);
                            }
                        } else {
                            startActivity(new Intent(current_activity, AttendanceActivity.class));
//                          SurveyAppModel.getInstance().fragmentTag = getString(R.string.attendance_text);
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case 3:
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_StudentTransfer).equals("1")) {
                            startActivity(new Intent(current_activity, AttendanceActivity.class));
//                          SurveyAppModel.getInstance().fragmentTag = getString(R.string.attendance_text);
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_PROMOTION).equals("1")) {
                            if (enabledModules.isModuleStudentPromotionEnabled()) {
                                startActivity(new Intent(current_activity, PromotionActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.promotions);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Promotion is disabled" + globalMessage);
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_GRADUATION).equals("1")) {
                            if (enabledModules.isModuleStudentGraduationEnabled()) {
                                startActivity(new Intent(current_activity, GraduationActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.Graduations);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Graduation is disabled" + globalMessage);
                            }
                        } else {
                            startActivity(new Intent(current_activity, StudentDropoutSearchActivity.class));
                            AppModel.getInstance().fragmentTag = getString(R.string.student_dropout_text);
                        }
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_StudentTransfer).equals("1")
                                && !AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_PROMOTION).equals("1")) {
                            if (enabledModules.isModuleStudentPromotionEnabled()) {
                                startActivity(new Intent(current_activity, PromotionActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.promotions);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Promotion is disabled" + globalMessage);
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_GRADUATION).equals("1")) {
                            if (enabledModules.isModuleStudentGraduationEnabled()) {
                                startActivity(new Intent(current_activity, GraduationActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.Graduations);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Graduation is disabled" + globalMessage);
                            }
                        } else {
                            startActivity(new Intent(current_activity, StudentDropoutSearchActivity.class));
                            AppModel.getInstance().fragmentTag = getString(R.string.student_dropout_text);
                        }

                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_StudentTransfer).equals("1")
                                && !AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_GRADUATION).equals("1")) {
                            if (enabledModules.isModuleStudentGraduationEnabled()) {
                                startActivity(new Intent(current_activity, GraduationActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.Graduations);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Graduation is disabled" + globalMessage);
                            }
                        } else {
                            startActivity(new Intent(current_activity, StudentDropoutSearchActivity.class));
                            AppModel.getInstance().fragmentTag = getString(R.string.student_dropout_text);
                            drawer.closeDrawer(GravityCompat.START);
                        }

                        break;
                    case 6:
//                        if (!SurveyAppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_StudentTransfer).equals("1")) {
                        startActivity(new Intent(current_activity, StudentDropoutSearchActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.student_dropout_text);
                        drawer.closeDrawer(GravityCompat.START);
//                        }
                        break;


                }
            } else if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                switch (childPosition) {
                    case 0:
                        startActivity(new Intent(current_activity, StudentProfileSearchActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.student_gr_register);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
            } else {

                switch (childPosition) {
                    case 0:
                        startActivity(new Intent(current_activity, StudentProfileSearchActivity.class));
                        AppModel.getInstance().fragmentTag = getString(R.string.student_gr_register);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }

            }
        } else {
            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
        }
    }*/

    public void setFinanceMenus(int parentPosition, int childPosition, EnabledModules enabledModules) {
        boolean FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(current_activity,
                AppConstants.FinanceSyncCompleted, false);

        if (FinanceSyncCompleted) {
            if (enabledModules.isModuleFinanceEnabled()) {
                if (!FinanceCheckSum.Instance(new WeakReference<>(current_activity)).isCheckSumApplicable()) {
                    if (FinanceCheckSum.Instance(new WeakReference<>(current_activity)).isCheckSumSuccessfull()) {
//      int roleID = DatabaseHelper.getInstance(DrawerActivity.this).getCurrentLoggedInUser().getRoleId();

                        switch (childItems.get(parentPosition).get(childPosition)) {
                            case "Receive Cash":
                                startActivity(new Intent(DrawerActivity.this, CashReceiptActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.cashReceived);
                                drawer.closeDrawer(GravityCompat.START);
                                break;

                            case "Cash In Hand":
                                startActivity(new Intent(DrawerActivity.this, CashInHandActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.cash_in_hand);
                                drawer.closeDrawer(GravityCompat.START);
                                break;

                            case "Receipt Correction":
                                startActivity(new Intent(DrawerActivity.this, CashCorrectionActivity.class));
                                AppModel.getInstance().fragmentTag = getString(R.string.cashCorrection);
                                drawer.closeDrawer(GravityCompat.START);
                                break;

                            case "Waiver":
                                startActivity(new Intent(current_activity, WavierActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                                break;

                            case "Fee Entry":
                                startActivity(new Intent(current_activity, FeeEntryActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                                break;
                        }

                        /*if (FeesCollection.getInstance(DrawerActivity.this).getClassSectionForFeeEntryBySchoolId(AppModel.getInstance().getAllUserSchoolsForFinance(DrawerActivity.this)).size() == 0) {

                            if (FeesCollection.getInstance(DrawerActivity.this).getUnuploadedCountFeeEntry(AppModel.getInstance().getAllUserSchoolsForFinance(DrawerActivity.this)) == 0)
                                switch (childPosition) {
                                    case 0:
                                        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                                            startActivity(new Intent(DrawerActivity.this, AccountStatementNewActivity.class));
                                            AppModel.getInstance().fragmentTag = getString(R.string.accountStatement);
                                        } else {
                                            startActivity(new Intent(DrawerActivity.this, CashReceiptActivity.class));
                                            AppModel.getInstance().fragmentTag = getString(R.string.cashReceived);
                                        }

                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 1:
                                        startActivity(new Intent(DrawerActivity.this, AccountStatementNewActivity.class));
                                        AppModel.getInstance().fragmentTag = getString(R.string.accountStatement);
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 2:
                                        startActivity(new Intent(DrawerActivity.this, CashInHandActivity.class));
                                        AppModel.getInstance().fragmentTag = getString(R.string.cash_in_hand);
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 3:
                                        startActivity(new Intent(DrawerActivity.this, CashCorrectionActivity.class));
                                        AppModel.getInstance().fragmentTag = getString(R.string.cashCorrection);
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 4:
                                        if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM) {
                                            startActivity(new Intent(current_activity, WavierActivity.class));
                                        }
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;

                                }
                            else {
                                if (roleID != AppConstants.roleId_103_V && roleID != AppConstants.roleId_7_AEM) {
                                    startActivity(new Intent(current_activity, FeeEntryActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }
                        } else {
                            if (roleID != AppConstants.roleId_103_V && roleID != AppConstants.roleId_7_AEM) {
                                startActivity(new Intent(current_activity, FeeEntryActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        }*/
                    } else
                        Toast.makeText(current_activity, "Disabled due to checksum failure/", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(current_activity, "Check sum is pending, please wait for the sync to complete.", Toast.LENGTH_SHORT).show();
                }
            } else {
                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
            }
        } else {
            Toast.makeText(current_activity, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /*public void setFinanceMenus(int childPosition, EnabledModules enabledModules) {
//        boolean NewLogin = AppModel.getInstance().readBooleanFromSharedPreferences(current_activity,
//                AppConstants.NewLogin, true);
        boolean FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(current_activity,
                AppConstants.FinanceSyncCompleted, false);

        if (FinanceSyncCompleted) {
            if (enabledModules.isModuleFinanceEnabled()) {
                if (!FinanceCheckSum.Instance(new WeakReference<>(current_activity)).isCheckSumApplicable()) {
                    if (FinanceCheckSum.Instance(new WeakReference<>(current_activity)).isCheckSumSuccessfull()) {
//      int roleID = DatabaseHelper.getInstance(DrawerActivity.this).getCurrentLoggedInUser().getRoleId();
                        if (FeesCollection.getInstance(DrawerActivity.this).getClassSectionForFeeEntryBySchoolId(AppModel.getInstance().getAllUserSchoolsForFinance(DrawerActivity.this)).size() == 0) {

                            if (FeesCollection.getInstance(DrawerActivity.this).getUnuploadedCountFeeEntry(AppModel.getInstance().getAllUserSchoolsForFinance(DrawerActivity.this)) == 0)
                                switch (childPosition) {
                                    case 0:
                                        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                                            startActivity(new Intent(DrawerActivity.this, AccountStatementNewActivity.class));
                                            AppModel.getInstance().fragmentTag = getString(R.string.accountStatement);
                                        } else {
                                            startActivity(new Intent(DrawerActivity.this, CashReceiptActivity.class));
                                            AppModel.getInstance().fragmentTag = getString(R.string.cashReceived);
                                        }

                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 1:
                                        startActivity(new Intent(DrawerActivity.this, AccountStatementNewActivity.class));
                                        AppModel.getInstance().fragmentTag = getString(R.string.accountStatement);
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 2:
                                        startActivity(new Intent(DrawerActivity.this, CashInHandActivity.class));
                                        AppModel.getInstance().fragmentTag = getString(R.string.cash_in_hand);
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 3:
                                        startActivity(new Intent(DrawerActivity.this, CashCorrectionActivity.class));
                                        AppModel.getInstance().fragmentTag = getString(R.string.cashCorrection);
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;
                                    case 4:
                                        if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM) {
                                            startActivity(new Intent(current_activity, WavierActivity.class));
                                        }
                                        drawer.closeDrawer(GravityCompat.START);
                                        break;

                                }
                            else {
                                if (roleID != AppConstants.roleId_103_V && roleID != AppConstants.roleId_7_AEM) {
                                    startActivity(new Intent(current_activity, FeeEntryActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }
                        } else {
                            if (roleID != AppConstants.roleId_103_V && roleID != AppConstants.roleId_7_AEM) {
                                startActivity(new Intent(current_activity, FeeEntryActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        }
                    } else
                        Toast.makeText(current_activity, "Disabled due to checksum failure/", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(current_activity, "Check sum is pending, please wait for the sync to complete.", Toast.LENGTH_SHORT).show();
                }
            } else {
                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
            }
        } else {
            Toast.makeText(current_activity, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void setEmployeeMenus(int parentPosition, int childPosition, EnabledModules enabledModules) {
        switch (childItems.get(parentPosition).get(childPosition)) {
            case "View Employees":
                if (enabledModules.isModuleHREmployeeListingEnabled()) {
                    startActivity(new Intent(current_activity, EmployeeListing.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Listing is disabled" + globalMessage);
                }
                break;
            case "Separation Approval":
                if (enabledModules.isModuleHRResignationEnabled()) {
                    startActivity(new Intent(current_activity, ManageResignationsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Resignations is disabled" + globalMessage);
                }
                break;
            case "View Separation":
                if (enabledModules.isModuleHRResignationEnabled()) {
                    Intent intent = new Intent(current_activity, ManageResignationsActivity.class);
                    intent.putExtra("isViewSeparation", true);
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Terminations is disabled" + globalMessage);
                }
                break;
            /*case "Manage Leaves":
                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                    startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Leaves is disabled" + globalMessage);
                }
                break;*/
            /*case "Employee Attendance":
                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                    startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                }
                break;*/
            case "TCT Entry":
                if (enabledModules.isModuleTCTEntryEnabled()) {
                    startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                }
                drawer.closeDrawer(GravityCompat.START);
                break;
            case "Previous TCT Registrations":
                if (enabledModules.isModuleTCTEntryEnabled()) {
                    startActivity(new Intent(current_activity, PhasesListingActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                }
                break;
        }
    }

    /*public void setEmployeeMenus(int childPosition, EnabledModules enabledModules) {
        boolean isTCTEntryHidden = AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_TCTEntry).equals("1");
        if (enabledModules.isModuleHREnabled()) {
            switch (childPosition) {
                case 0:
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) {
                        if (enabledModules.isModuleHREmployeeListingEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeListing.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Listing is disabled" + globalMessage);
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1") &&
                            EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", "")) {
                        if (enabledModules.isModuleHRResignationEnabled()) {
                            startActivity(new Intent(current_activity, ManageResignationsActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Resignations is disabled" + globalMessage);
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                        if (enabledModules.isModuleHRResignationEnabled()) {
                            Intent intent = new Intent(current_activity, ManageResignationsActivity.class);
                            intent.putExtra("isViewSeparation", true);
                            startActivity(intent);
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Terminations is disabled" + globalMessage);
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                            && roleID != AppConstants.roleId_103_V) {
                        if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Leaves is disabled" + globalMessage);
                        }
                    } else if (!isTCTEntryHidden) {
                        if (enabledModules.isModuleTCTEntryEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                        }
                    }
                    break;


                case 1:
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                                && EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", "")) {
                            if (enabledModules.isModuleHRResignationEnabled()) {
                                startActivity(new Intent(current_activity, ManageResignationsActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Resignation is disabled" + globalMessage);
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                            if (enabledModules.isModuleHRResignationEnabled()) {
                                Intent intent = new Intent(current_activity, ManageResignationsActivity.class);
                                intent.putExtra("isViewSeparation", true);
                                startActivity(intent);
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Termination is disabled" + globalMessage);
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && roleID != AppConstants.roleId_103_V) {
                            if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Leaves is disabled" + globalMessage);
                            }
                        } else if (!isTCTEntryHidden) {
                            if (enabledModules.isModuleTCTEntryEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                            }
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                            && (EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", ""))) {
                        if (enabledModules.isModuleHRResignationEnabled()) {
                            Intent intent = new Intent(current_activity, ManageResignationsActivity.class);
                            intent.putExtra("isViewSeparation", true);
                            startActivity(intent);
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Termination is disabled" + globalMessage);
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && roleID != AppConstants.roleId_103_V) {
                            if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Leaves is disabled" + globalMessage);
                            }
                        } else if (!isTCTEntryHidden) {
                            if (enabledModules.isModuleTCTEntryEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                            }
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                            && roleID != AppConstants.roleId_103_V) {
                        if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                        }
                    } else if (!isTCTEntryHidden) {
                        if (enabledModules.isModuleTCTEntryEnabled()) {
                            //view previous tct
                            startActivity(new Intent(current_activity, PhasesListingActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT View All is disabled" + globalMessage);
                        }
                    }
                    break;
                case 2:
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                                && (EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", ""))) {
                            if (enabledModules.isModuleHRResignationEnabled()) {
                                Intent intent = new Intent(current_activity, ManageResignationsActivity.class);
                                intent.putExtra("isViewSeparation", true);
                                startActivity(intent);
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Termination is disabled" + globalMessage);
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                            if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                    && roleID != AppConstants.roleId_103_V) {
                                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Leave is disabled" + globalMessage);
                                }
                            } else if (!isTCTEntryHidden) {
                                if (enabledModules.isModuleTCTEntryEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                                }
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && roleID != AppConstants.roleId_103_V) {
                            if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                            }
                        } else if (!isTCTEntryHidden) {
                            if (enabledModules.isModuleTCTEntryEnabled()) {
                                //view previous tct
                                startActivity(new Intent(current_activity, PhasesListingActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                            }

                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                            && (EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", ""))) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && roleID != AppConstants.roleId_103_V) {
                            if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Leave is disabled" + globalMessage);
                            }
                        } else if (!isTCTEntryHidden) {
                            if (enabledModules.isModuleTCTEntryEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                            }
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && roleID != AppConstants.roleId_103_V) {
                            if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                            }
                        } else if (!isTCTEntryHidden) {
                            if (enabledModules.isModuleTCTEntryEnabled()) {
                                //view previous tct
                                startActivity(new Intent(current_activity, PhasesListingActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                            }
                        }
                    }
                    break;
                case 3:
                    if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                                && EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", "")) {
                            if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                    && roleID != AppConstants.roleId_103_V) {
                                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeManageLeavesActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Manage Leaves is disabled" + globalMessage);
                                }
                            } else if (!isTCTEntryHidden) {
                                if (enabledModules.isModuleTCTEntryEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                                }
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                            if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                    && roleID != AppConstants.roleId_103_V) {
                                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                                }
                            }
                        } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                            if (!isTCTEntryHidden) {
                                if (enabledModules.isModuleTCTEntryEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                                }
                            }
                        }

                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                            && EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", "")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && roleID != AppConstants.roleId_103_V) {
                            if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                                startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                            }
                        }
                    } else if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) {
                        if (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                                && enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                            if (!isTCTEntryHidden) {
                                if (enabledModules.isModuleTCTEntryEnabled()) {
                                    startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                                }
                            }
                        }
                    }

                    break;
                case 4:
                    if ((!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) &&
                            (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                                    && EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", "")) &&
                            (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) &&
                            !AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")
                            && roleID != AppConstants.roleId_103_V) {
                        if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeAttendanceActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Employee Attendance is disabled" + globalMessage);
                        }
                    } else if (!isTCTEntryHidden) {
                        if (enabledModules.isModuleTCTEntryEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                        }
                    }
                    break;
                case 5:
                    if ((!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeListing).equals("1")) &&
                            (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")
                                    && EmployeeHelperClass.getInstance(current_activity).ifPendingApprovalsExist(0, "", "")) &&
                            (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeResignation).equals("1")) &&
                            (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_EmployeeLeaveAndAttend).equals("1")) &&
                            (!AppModel.getInstance().readFromSharedPreferences(current_activity, AppConstants.HIDE_TCTEntry).equals("1"))
                            && roleID != AppConstants.roleId_103_V) {
                        if (enabledModules.isModuleTCTEntryEnabled()) {
                            startActivity(new Intent(current_activity, EmployeeTCT_EntryActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                        }
                    } else if (!isTCTEntryHidden) {
                        if (enabledModules.isModuleTCTEntryEnabled()) {
                            startActivity(new Intent(current_activity, PhasesListingActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                        }
                    }
                    break;

                case 6:
                    if (!isTCTEntryHidden) {
                        if (enabledModules.isModuleTCTEntryEnabled()) {
                            startActivity(new Intent(current_activity, PhasesListingActivity.class));
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "TCT Entry is disabled" + globalMessage);
                        }
                    }
                    break;
            }
        } else {
            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "HR Module is disabled" + globalMessage);
        }
    }*/

    public void setReportMenusIfFinanceDisabled(int parentPosition, int childPosition, EnabledModules enabledModules) {
        switch (childItems.get(parentPosition).get(childPosition)) {
            case "Class":
                if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, ClassReportActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
                }
                break;

            case "Attendance":
                if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, SchoolReportActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
                }
                break;

            case "Receipt Book":
                if (enabledModules.isModuleFinanceEnabled()) {
                    startActivity(new Intent(current_activity, ReceiptBook.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;

            case "Deposit History":
                if (enabledModules.isModuleFinanceEnabled()) {
                    startActivity(new Intent(current_activity, DepositHistoryReport.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;

            case "Fees Count(For Debugging)":
                if (enabledModules.isModuleFinanceEnabled()) {
//                    if (!isFeesCollectionHidden.equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(current_activity);
                        builder.setTitle("Fees Count");
                        String[] array = FeesCollection.getInstance(getParent()).getFeesCount(AppModel.getInstance().getAllUserSchoolsForFinance(current_activity));
                        builder.setMessage("Student Count = " + array[0] + "\nSchool Class Count = " + array[1]
                                + "\nFees header count = " + array[2] + "\nFee Detail Count = " + array[3]
                                + "\nCash Deposit Count = " + array[4]);
                        builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
//                    }
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;

            case "Cash in Hand Report":
                if (enabledModules.isModuleFinanceEnabled()) {
//                    if (!isFeesCollectionHidden.equals("1")) {
                        startActivity(new Intent(current_activity, CashInHandReportActivity.class));
                        drawer.closeDrawer(GravityCompat.START);
//                    }
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;

            case "Taaluq Student Data":
                startActivity(new Intent(current_activity, SurveyReportActivity.class).putExtra(
                        "PowerBI_Link",
                        "https://app.powerbi.com/view?r=eyJrIjoiMjIzMWI0OGYtZmRmYy00Mzc1LWJkOWEtOWQwZTdhM2Y0ODM1IiwidCI6IjBlZDdhY2Y5LWQ2ODQtNDQ0Yi1hYjdmLWM1NzlhYjFlMWYzZiIsImMiOjl9"
                ));
                break;

            case "Speed Test":
                startActivity(new Intent(current_activity, GeneralWebviewActivity.class));
                /*startActivity(new Intent(current_activity, SurveyReportActivity.class).putExtra(
                        "PowerBI_Link",
                        "https://fast.com/"
                ));*/
                break;

            case "Receivable Statement":
//                if (!isFeesCollectionHidden.equals("1") && enabledModules.isModuleFinanceEnabled()) {
                    startActivity(new Intent(current_activity, ReceivableStatementActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
//                }
                break;

            case "Log":
                startActivity(new Intent(current_activity, LogActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case "Student Error Log":
                startActivity(new Intent(current_activity, StudentErrorActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;




        }
        /*switch (childPosition) {
            case 0:
                if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, ClassReportActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
                }
                break;
            case 1:
                if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, SchoolReportActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
                }
                break;
            case 2:
                startActivity(new Intent(current_activity, LogActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case 3:
//                if (enabledModules.isModuleStudentEnabled()) {
                startActivity(new Intent(current_activity, StudentErrorActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
        }*/

    }

    public void setReportMenusForFinance(int parentPosition, int childPosition, String isFeesCollectionHidden, EnabledModules enabledModules) {
        switch (childPosition) {
            case 0:
                if (!isFeesCollectionHidden.equals("1") && enabledModules.isModuleFinanceEnabled()) {
                    startActivity(new Intent(current_activity, ReceivableStatementActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;
            case 1:
                if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, ClassReportActivity.class));
                    drawer.closeDrawer(GravityCompat.START);

//                if (!isFeesCollectionHidden.equals("1")) {
//                    startActivity(new Intent(current_activity, StudentReportsActivity.class));
//                    SurveyAppModel.getInstance().fragmentTag = getString(R.string.Student_Report);
//                } else {
//                    startActivity(new Intent(current_activity, SchoolReportActivity.class));
//                }
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
                }
                break;
            case 2:
                if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, SchoolReportActivity.class));
                    drawer.closeDrawer(GravityCompat.START);

//                if (!isFeesCollectionHidden.equals("1")) {
//                    startActivity(new Intent(current_activity, SchoolReportActivity.class));
//                    SurveyAppModel.getInstance().fragmentTag = getString(R.string.School_Report);
//                } else {
//                    startActivity(new Intent(current_activity, ClassReportActivity.class));
//                }
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
                }
                break;
            case 3:
                if (!isFeesCollectionHidden.equals("1")) {
                    if (enabledModules.isModuleFinanceEnabled()) {
                        startActivity(new Intent(current_activity, ReceiptBook.class));
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                    }
                } else {
                    startActivity(new Intent(current_activity, LogActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }


//                if (!isFeesCollectionHidden.equals("1")) {
//                    startActivity(new Intent(current_activity, StudentCollectionReport.class));
//                } else {
//                    startActivity(new Intent(current_activity, LogActivity.class));
//                }
                break;

            case 4:
                if (!isFeesCollectionHidden.equals("1")) {
                    if (enabledModules.isModuleFinanceEnabled()) {
                        startActivity(new Intent(current_activity, DepositHistoryReport.class));
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                    }
                } else {
//                    if (enabledModules.isModuleStudentEnabled()) {
                    startActivity(new Intent(current_activity, StudentErrorActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
//                    }else {
//                        SurveyAppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
//                    }
                }
                break;

            case 5:
                startActivity(new Intent(current_activity, LogActivity.class));

//                if (!isFeesCollectionHidden.equals("1"))
//                    startActivity(new Intent(current_activity, ReceiptBook.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case 6:
//                if (enabledModules.isModuleStudentEnabled()) {
                startActivity(new Intent(current_activity, StudentErrorActivity.class));
                drawer.closeDrawer(GravityCompat.START);
//                }else {
//                    SurveyAppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Module is disabled" + globalMessage);
//                }

//                if (!isFeesCollectionHidden.equals("1")) {
//                    startActivity(new Intent(current_activity, FeesCollectionReport.class));
//                }
                break;
            case 7:
                if (enabledModules.isModuleFinanceEnabled()) {
                    if (!isFeesCollectionHidden.equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(current_activity);
                        builder.setTitle("Fees Count");
                        String[] array = FeesCollection.getInstance(getParent()).getFeesCount(AppModel.getInstance().getAllUserSchoolsForFinance(current_activity));
                        builder.setMessage("Student Count = " + array[0] + "\nSchool Class Count = " + array[1]
                                + "\nFees header count = " + array[2] + "\nFee Detail Count = " + array[3]
                                + "\nCash Deposit Count = " + array[4]);
                        builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;
            case 8:
                if (enabledModules.isModuleFinanceEnabled()) {
                    if (!isFeesCollectionHidden.equals("1")) {
                        startActivity(new Intent(current_activity, CashInHandReportActivity.class));
                        drawer.closeDrawer(GravityCompat.START);
                    }
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Finance Module is disabled" + globalMessage);
                }
                break;
        }

    }


    public void setSchoolVisitMenus(EnabledModules enabledModules) {
        if (enabledModules.isModuleStudentVisitFormsEnabled()) {
            SurveyDBHandler dbSurvey = new SurveyDBHandler(getApplicationContext());
            if(!CollectionUtils.isEmpty(dbSurvey.getAllProjects()) || AppModel.getInstance().isConnectedToInternet(DrawerActivity.this)) {
//            if (AppModel.getInstance().isConnectedToInternet(DrawerActivity.this)) {
                Intent intent = new Intent(this, ProjectsActivity.class);
                startActivity(intent);
//            } else
//                Toast.makeText(DrawerActivity.this, "Internet is required", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(DrawerActivity.this, "Please connect to internet to download survey projects", Toast.LENGTH_LONG).show();
        } else {
            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Student Visit Form Module is disabled" + globalMessage);
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    public void setChangePasswordMenus() {
        if (AppModel.getInstance().isConnectedToInternet(DrawerActivity.this))
            startActivity(new Intent(DrawerActivity.this, ChangePasswordActivity.class));
        else
            Toast.makeText(DrawerActivity.this, "Internet is required to change password", Toast.LENGTH_SHORT).show();
        drawer.closeDrawer(GravityCompat.START);
    }

//    public void setAboutUsMenus() {
//    }

    public void setAboutUsMenus(int parentPosition, int childPosition) {
        switch (childItems.get(parentPosition).get(childPosition)) {
            case "Policies":
                startActivity(new Intent(DrawerActivity.this, UserManualActivity.class).putExtra("isPolicy", true));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case "User Manual":
                startActivity(new Intent(DrawerActivity.this, UserManualActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case "FAQ's":
                startActivity(new Intent(DrawerActivity.this, FrequentlyAskQuestionsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case "Feedback":
                showFeedbackDialogue();
//                drawer.closeDrawer(GravityCompat.START);
                break;
            case "Rate Us":
                openAppInPlayStore();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case "About us":
                startActivity(new Intent(DrawerActivity.this, AboutUsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
        /*switch (childPosition) {
            case 0:
                startActivity(new Intent(DrawerActivity.this, UserManualActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case 1:
                startActivity(new Intent(DrawerActivity.this, FrequentlyAskQuestionsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case 2:
                showFeedbackDialogue();
//                drawer.closeDrawer(GravityCompat.START);
                break;
            case 3:
                openAppInPlayStore();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case 4:
                startActivity(new Intent(DrawerActivity.this, AboutUsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
        }*/
    }

    public void setLogoutMenus() {
        //TODO any change here should also copy paste on AppModel logOff method
        Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        AppModel.getInstance().disposeToken(DrawerActivity.this);
        AppModel.getInstance().clearAllowedModuleToken(DrawerActivity.this);
        AppModel.getInstance().removeSyncAccount(DrawerActivity.this);
        SharedPreferences prefs = getSharedPreferences("syncPrefs", Context.MODE_PRIVATE);
//                        String syncDate= prefs.getString("syncSuccessTime", "Nill");
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        AppModel.getInstance().clearSpinnerSelectedSchool(DrawerActivity.this);
        SessionInfo.getInstance(DrawerActivity.this).delete();
        AttendancePercentage.getInstance(DrawerActivity.this).delete();
        AppModel.getInstance().clearSelectedSchool(DrawerActivity.this);
        AppModel.getInstance().writeBooleanToSharedPreferences(DrawerActivity.this, AppConstants.logoutKey, true);
        finish();
    }

    public void setExpenseMenus(int childPosition, EnabledModules enabledModules) {
        if (enabledModules.isModuleExpenseEnabled()) {
            if (roleID == AppConstants.roleId_27_P ||
                    roleID == AppConstants.roleId_101_ST ||
                    roleID == AppConstants.roleId_109_CM ||
                    roleID == AppConstants.roleId_102_AA ||
//                    roleID == AppConstants.roleId_103_V ||
                    roleID == AppConstants.roleId_7_AEM) {
                switch (childPosition) {
                    case 0:
                        startActivity(new Intent(current_activity, AddNewRecordActivity.class));
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        startActivity(new Intent(current_activity, TransactionListing.class));
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case 2:
                        startActivity(new Intent(current_activity, ClosingActivity.class));
//                        Toast.makeText(current_activity, "Closing is disabled during these dates", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        startActivity(new Intent(current_activity, TransferToHoActivity.class));
//                        Toast.makeText(current_activity, "Closing is disabled during these dates", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        } else {
            AppModel.getInstance().showMessage(new WeakReference<>(current_activity), "Info!", "Expense Module is disabled" + globalMessage);
        }
    }

    void setListenerForSync(InterfaceForSync interfaceForSync) {
        this.interfaceForSync = interfaceForSync;
    }

    @Override
    protected void onPause() {
        super.onPause();

//        try {
//            unregisterReceiver(receiverSyncVisibility);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            unregisterReceiver(syncFinishReciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AppModel.getInstance().getDateWhenLogin(current_activity).isEmpty()) {
            AppModel.getInstance().logOff(current_activity);
        } else {
//            final boolean isTimeMatch = SurveyAppModel.getInstance().getDateWhenLogin(current_activity).equals(SurveyAppModel.getInstance().getDate());
            final boolean isFutureDate = AppModel.getInstance().isDateIsFuture(AppModel.getInstance().getDateWhenLogin(current_activity),
                    AppModel.getInstance().getDate(), "yyyy-MM-dd");
            if (isFutureDate) {


//                try {
//                    registerReceiver(receiverSyncVisibility, new IntentFilter(AppConstants.Action_receiverSyncVisibility));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                try {
                    registerReceiver(receiver, new IntentFilter(AppConstants.Action_PendingSyncChanged));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    registerReceiver(syncFinishReciever, new IntentFilter(SyncUtils.syncFinishedIntentFilter));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                overrideAppModules();

//                autoSyncStartWhenUploadIsPendingInQueue();
            } else {
                Toast.makeText(current_activity, "Date is incorrect", Toast.LENGTH_SHORT).show();
                AppModel.getInstance().logOff(current_activity);
            }
        }

        //if metadata not downloaded for all schools then logout
//        boolean isMetaDataDownloadedForAllSchools = DatabaseHelper.getInstance(this).checkmetadataRecordDownlodedSuccessfully();
//        if (!AppModel.getInstance().isConnectedToInternet(this) &&
//                !ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)
//                && !isMetaDataDownloadedForAllSchools){
//            Toast.makeText(this, "Data not downloaded successfully due to network connectivity. Please check your internet and login again", Toast.LENGTH_LONG).show();
//            AppModel.getInstance().logOff(this);
//        }
    }

    private void overrideAppModules() {
        if (!isOverrideAppModules) {
            Thread performOperationsThread = new Thread(() -> {
                UserModel um = DatabaseHelper.getInstance(DrawerActivity.this).getCurrentLoggedInUser();
                ArrayList<SchoolModel> schoolModels = DatabaseHelper.getInstance(DrawerActivity.this).getAllUserSchools();
                AppModel.getInstance().saveAllowedModuleToPreference(DrawerActivity.this, schoolModels, um);
                isOverrideAppModules = true;
            });
            performOperationsThread.start();
        }
    }

    protected void calPendingRecords(int SchoolID) {
        List<SchoolModel> schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
        int pendingCount = 0;
        if (SchoolID == 0) {
            for (SchoolModel model : schoolModels) {
                if (model.getId() != 0) {
                    pendingCount += DatabaseHelper.getInstance(this).getAllPendingRecords(model.getId());
                }
            }
        } else {
            pendingCount = DatabaseHelper.getInstance(this).getAllPendingRecords(SchoolID);
        }
        //Separation Approval records comes for any school that's why its added separately
        pendingCount += DatabaseHelper.getInstance(this).getPendingSeparationRecordsForSync();

        //Feedback records comes for any school that's why its added separately
        pendingCount+= HelpHelperClass.getInstance(this).getPendingFeedbackRecordsForSync();

        pendingRecordsSync.setText(String.valueOf(pendingCount));
    }

    private void setAndCalLastSyncSince() {
        try {
            SharedPreferences prefs = getSharedPreferences("syncPrefs", Context.MODE_PRIVATE);
            String syncedSuccessfull = prefs.getString("syncSuccessTime", null);
            if (syncedSuccessfull == null) {
                tv_lastSyncOn.setText("Pending");
            }
            String currentdate = AppModel.getInstance().getDate();
            String syncDate = AppModel.getInstance().
                    convertDatetoFormat(prefs.getString("syncSuccessTime", "Nill"), " dd-MM-yyyy hh:mm a", " yyyy-MM-dd");
            long syncSince = AppModel.getInstance().getDaysBetweenDates(syncDate,
                    currentdate, "yyyy-MM-dd");

            if (syncSince > 0) {
                tv_daysSince.setTextColor(getResources().getColor(R.color.light_red));
                tv_syncDaysSince.setTextColor(getResources().getColor(R.color.light_red));
//                rl_syncStatus.setBackgroundColor(getResources().getColor(R.color.light_red_color));
//                CVSyncStatus_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_red));
            } else {
//                rl_syncStatus.setBackgroundColor(Color.WHITE);
                tv_daysSince.setTextColor(getResources().getColor(R.color.gray_cell));
                tv_syncDaysSince.setTextColor(getResources().getColor(R.color.gray_cell));
//                CVSyncStatus_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
            }

            if (this.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getString("syncstatus", "true").equals("false"))
                tv_lastSyncOn.setText("Pending");
            else {
                tv_lastSyncOn.setText(AppModel.getInstance().
                        convertDatetoFormat(prefs.getString("syncSuccessTime", "Pending"), " dd-MM-yyyy hh:mm a", " dd-MMM-yy hh:mm a"));
                tv_syncDaysSince.setText(String.valueOf(syncSince));
//                pendingRecordsSync.setText(String.valueOf(pendingCount));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMenuRefresh() {
        calPendingRecords(AppModel.getInstance().getSpinnerSelectedSchool(current_activity));
        setAndCalLastSyncSince();
    }


    public interface InterfaceForSync {
        void onSync();
    }

    private void openAppInPlayStore() {
        Uri uri = Uri.parse("market://details?id="+ this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(goToMarket);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id"+this.getPackageName())));
        }
    }

    private void showFeedbackDialogue() {
        try {
            boolean shouldUserGiveFeedbackToday = HelpHelperClass.getInstance(DrawerActivity.this).isFeedbackGivenToday();
            if (shouldUserGiveFeedbackToday) {
                FeedbackDialogManager feedbackDialog = new FeedbackDialogManager(DrawerActivity.this);
//              feedbackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                feedbackDialog.show();

            } else{
                MessageBox("We have already received your feedback today and would love to hear from you again, you may try again tomorrow.");
            }
            AppConstants.isFeedbackDialogShowing = true;
        } catch (Exception e){
            AppModel.getInstance().appendErrorLog(DrawerActivity.this,"Error in showFeedbackDialogue:" + e.getMessage());
            AppConstants.isFeedbackDialogShowing = false;
        }
    }

    protected void autoSyncStartWhenUploadIsPendingInQueue() {
        //auto sync uploads as soon as they come in the que
        try {
            String pendingSync = pendingRecordsSync.getText().toString();
            if (!pendingSync.isEmpty()) {
                int pendCount = Integer.parseInt(pendingSync);
                if (pendCount > 0)
                    AppModel.getInstance().startSyncService(DrawerActivity.this, SyncProgressHelperClass.SYNC_TYPE_SAVE_SYNC_ID);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String changeStringCase(String s) {

        final String DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString(); }
}

