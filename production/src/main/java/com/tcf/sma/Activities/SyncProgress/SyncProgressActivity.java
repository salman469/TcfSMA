package com.tcf.sma.Activities.SyncProgress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.R;

import java.util.Objects;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SyncProgressActivity extends DrawerActivity {

    private View view;
    private ProgressBar pbStudent, pbFinance, pbEmployee, pbTCTEntry, pbSchool,pbImage,pbExpense;
    private TextView tvPbStudent, tvPbFinance, tvPbEmployee, tvPbTCTEntry, tvPbSchool, tvPbImage, tvPbExpense;
    private LinearLayout ll_PbStudent, ll_PbFinance, ll_PbEmployee, ll_PbTCTEntry, ll_PbSchool,ll_PbExpense;

    public static final String MESSAGE_PROGRESS = "message_progress";
    private static SyncDownloadUploadModel syncDownloadUploadModel = null;
    private static SyncDownloadUploadModel syncStudentDownloadUploadModel = null;
    private static SyncDownloadUploadModel syncFinanceDownloadUploadModel = null;
    private static SyncDownloadUploadModel syncEmployeeDownloadUploadModel = null;
    private static SyncDownloadUploadModel syncTCTDownloadUploadModel = null;
    private static SyncDownloadUploadModel syncSchoolDownloadModel = null;
    private static SyncDownloadUploadModel syncImageDownloadModel = null;
    private static SyncDownloadUploadModel syncExpenseDownloadModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sync_progress);
        view = setActivityLayout(this, R.layout.activity_sync_progress);
        setToolbar("Syncing Progress", this, false);

        initialize();
        showProgressViewIfModuleEnabled();
        registerReceiver();
    }

    private void showProgressViewIfModuleEnabled() {
        final boolean isFeesCollectionHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION).equals("1");
        final boolean isEmployeeHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_Employee).equals("1");
        final boolean isTCTEntryHidden = AppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_TCTEntry).equals("1");

        if (!isFeesCollectionHidden){
            ll_PbFinance.setVisibility(View.VISIBLE);
        }else {
            ll_PbFinance.setVisibility(View.GONE);
        }

        if (!isEmployeeHidden){
            ll_PbEmployee.setVisibility(View.VISIBLE);
        }else {
            ll_PbEmployee.setVisibility(View.GONE);
        }

        if (!isTCTEntryHidden){
            ll_PbTCTEntry.setVisibility(View.VISIBLE);
        }else {
            ll_PbTCTEntry.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (syncDownloadUploadModel != null)
            updateSyncProgressValues();
    }

    private void initialize() {
        pbStudent = view.findViewById(R.id.pbStudent);
        pbFinance = view.findViewById(R.id.pbFinance);
        pbEmployee = view.findViewById(R.id.pbEmployee);
        pbTCTEntry = view.findViewById(R.id.pbTCTEntry);
        pbSchool = view.findViewById(R.id.pbSchool);
        pbImage = view.findViewById(R.id.pbImage); // added in: 02-06-2020 , requested by: Taha TCF Employee, reason: They want to have a different box for images sync that shows progress of images downloads Currently it merges and gets lost in student sync box
        pbExpense = view.findViewById(R.id.pbExpense); // added in: 06-01-2021

        tvPbStudent = view.findViewById(R.id.tvPbStudent);
        tvPbFinance = view.findViewById(R.id.tvPbFinance);
        tvPbEmployee = view.findViewById(R.id.tvPbEmployee);
        tvPbTCTEntry = view.findViewById(R.id.tvPbTCTEntry);
        tvPbSchool = view.findViewById(R.id.tvPbSchool);
        tvPbImage = view.findViewById(R.id.tvPbImage);
        tvPbExpense = view.findViewById(R.id.tvPbExpense);

        ll_PbStudent = view.findViewById(R.id.ll_PbStudent);
        ll_PbFinance = view.findViewById(R.id.ll_PbFinance);
        ll_PbEmployee = view.findViewById(R.id.ll_PbEmployee);
        ll_PbTCTEntry = view.findViewById(R.id.ll_PbTCTEntry);
        ll_PbSchool = view.findViewById(R.id.ll_PbSchool);
        ll_PbExpense = view.findViewById(R.id.ll_PbExpense);

        hideClosedModules();

    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Objects.requireNonNull(intent.getAction()).equals(MESSAGE_PROGRESS)) {

                syncDownloadUploadModel = intent.getParcelableExtra("download");
                assert syncDownloadUploadModel != null;
                if (syncDownloadUploadModel.getProgress() == 101) {
//                    progress_text.setText(syncDownloadUploadModel.getTCFModuleName() + " " + syncDownloadUploadModel.getSyncType() + " Complete");
//                    LocalBroadcastManager.getInstance(SyncProgressActivity.this).unregisterReceiver(broadcastReceiver);

                } else {
                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.STUDENT_MODULE) {
                        syncStudentDownloadUploadModel = syncDownloadUploadModel;
                    }

                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.FINANCE_MODULE) {
                        syncFinanceDownloadUploadModel = syncDownloadUploadModel;
                    }

                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.EMPLOYEE_MODULE) {
                        syncEmployeeDownloadUploadModel = syncDownloadUploadModel;
                    }

                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.TCTENTRY_MODULE) {
                        syncTCTDownloadUploadModel = syncDownloadUploadModel;
                    }

                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.SCHOOL_MODULE) {
                        syncSchoolDownloadModel = syncDownloadUploadModel;
                    }

                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.IMAGE_MODULE) {
                        syncImageDownloadModel = syncDownloadUploadModel;
                    }

                    if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.EXPENSE_MODULE) {
                        syncExpenseDownloadModel = syncDownloadUploadModel;
                    }


                }

                updateSyncProgressValues();
            }
        }
    };

    private void updateSyncProgressValues() {

        if (syncStudentDownloadUploadModel != null) {
            String school = syncStudentDownloadUploadModel.getSchoolId() > 0 ? "School id: " + syncStudentDownloadUploadModel.getSchoolId() + " " : "";
            String progressStudent = school + syncStudentDownloadUploadModel.getTCFModuleName() + " " + syncStudentDownloadUploadModel.getSyncType() +
                    "(" + syncStudentDownloadUploadModel.getCurrentFileSize() + " /" + syncStudentDownloadUploadModel.getTotalFileSize() + ")";
            pbStudent.setProgress(syncStudentDownloadUploadModel.getProgress());
            tvPbStudent.setText(progressStudent);
        }

        if (syncFinanceDownloadUploadModel != null) {
            String school = syncFinanceDownloadUploadModel.getSchoolId() > 0 ? "School id: " + syncFinanceDownloadUploadModel.getSchoolId() + " " : "";
            String progressFinance = school + syncFinanceDownloadUploadModel.getTCFModuleName() + " " + syncFinanceDownloadUploadModel.getSyncType() +
                    "(" + syncFinanceDownloadUploadModel.getCurrentFileSize() + " /" + syncFinanceDownloadUploadModel.getTotalFileSize() + ")";
            pbFinance.setProgress(syncFinanceDownloadUploadModel.getProgress());
            tvPbFinance.setText(progressFinance);
        }

        if (syncEmployeeDownloadUploadModel != null) {
            String school = syncEmployeeDownloadUploadModel.getSchoolId() > 0 ? "School id: " + syncEmployeeDownloadUploadModel.getSchoolId() + " " : "";
            String progressEmployee = school + syncEmployeeDownloadUploadModel.getTCFModuleName() + " " + syncEmployeeDownloadUploadModel.getSyncType() +
                    "(" + syncEmployeeDownloadUploadModel.getCurrentFileSize() + " /" + syncEmployeeDownloadUploadModel.getTotalFileSize() + ")";
            pbEmployee.setProgress(syncEmployeeDownloadUploadModel.getProgress());
            tvPbEmployee.setText(progressEmployee);
        }

        if (syncTCTDownloadUploadModel != null) {
            String school = syncTCTDownloadUploadModel.getSchoolId() > 0 ? "School id: " + syncTCTDownloadUploadModel.getSchoolId() + " " : "";
            String progressTCTEntry = school + syncTCTDownloadUploadModel.getTCFModuleName() + " " + syncTCTDownloadUploadModel.getSyncType() +
                    "(" + syncTCTDownloadUploadModel.getCurrentFileSize() + " /" + syncTCTDownloadUploadModel.getTotalFileSize() + ")";
            pbTCTEntry.setProgress(syncTCTDownloadUploadModel.getProgress());
            tvPbTCTEntry.setText(progressTCTEntry);
        }

        if (syncSchoolDownloadModel != null) {
            String school = syncSchoolDownloadModel.getSchoolId() > 0 ? "School id: " + syncSchoolDownloadModel.getSchoolId() + " " : "";
            String progressSchool = school + syncSchoolDownloadModel.getTCFModuleName() + " " + syncSchoolDownloadModel.getSyncType() +
                    "(" + syncSchoolDownloadModel.getCurrentFileSize() + " /" + syncSchoolDownloadModel.getTotalFileSize() + ")";
            pbSchool.setProgress(syncSchoolDownloadModel.getProgress());
            tvPbSchool.setText(progressSchool);
        }

        if (syncImageDownloadModel != null) {
            String school = syncImageDownloadModel.getSchoolId() > 0 ? "School id: " + syncImageDownloadModel.getSchoolId() + " " : "";
            String progressSchool = school + syncImageDownloadModel.getTCFModuleName() + " " + syncImageDownloadModel.getSyncType() +
                    "(" + syncImageDownloadModel.getCurrentFileSize() + " /" + syncImageDownloadModel.getTotalFileSize() + ")";
            pbImage.setProgress(syncImageDownloadModel.getProgress());
            tvPbImage.setText(progressSchool);
        }

        if (syncExpenseDownloadModel != null) {
            String school = syncExpenseDownloadModel.getSchoolId() > 0 ? "School id: " + syncExpenseDownloadModel.getSchoolId() + " " : "";
            String progressExpense = school + syncExpenseDownloadModel.getTCFModuleName() + " " + syncExpenseDownloadModel.getSyncType() +
                    "(" + syncExpenseDownloadModel.getCurrentFileSize() + " /" + syncExpenseDownloadModel.getTotalFileSize() + ")";
            pbExpense.setProgress(syncExpenseDownloadModel.getProgress());
            tvPbExpense.setText(progressExpense);
        }
    }

    void hideClosedModules(){
        //Student,Image and School progress are mandatory for all cases

        SharedPreferences sharedPref = getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        boolean isFeesHidden = sharedPref.getString(AppConstants.HIDE_FEES_COLLECTION, "").equals("1");
        boolean isEmployeeHidden = sharedPref.getString(AppConstants.HIDE_Employee, "").equals("1");
        boolean isTCTEntryHidden = sharedPref.getString(AppConstants.HIDE_TCTEntry, "").equals("1");
        boolean isExpenseHidden = sharedPref.getString(AppConstants.HIDE_Expense, "").equals("1");
        if (isFeesHidden)
            ll_PbFinance.setVisibility(View.GONE);
        else
            ll_PbFinance.setVisibility(View.VISIBLE);
        if (isEmployeeHidden)
            ll_PbEmployee.setVisibility(View.GONE);
        else
            ll_PbEmployee.setVisibility(View.VISIBLE);
        if (isTCTEntryHidden)
            ll_PbTCTEntry.setVisibility(View.GONE);
        else
            ll_PbTCTEntry.setVisibility(View.VISIBLE);
        if (isExpenseHidden)
            ll_PbExpense.setVisibility(View.GONE);
        else
            ll_PbExpense.setVisibility(View.VISIBLE);
    }

}


