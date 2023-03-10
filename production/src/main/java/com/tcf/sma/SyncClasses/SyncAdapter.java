package com.tcf.sma.SyncClasses;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.ErrorLog;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.SyncNotificationHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Interfaces.IProcessComplete;
import com.tcf.sma.Interfaces.NotificationProgressInterface;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SyncProgress.NotificationProgressModel;
import com.tcf.sma.R;
import com.tcf.sma.Scheduler.KeepAliveService;
import com.tcf.sma.Scheduler.WorkManager.NewKeepAliveService;
import com.tcf.sma.utils.ExpenseCheckSum;
import com.tcf.sma.utils.FinanceCheckSum;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zubair Soomro on 12/23/2016.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter implements IProcessComplete, NotificationProgressInterface {
    public static final String TAG = "SyncAdapter";
    /**
     * Project used when querying content provider. Returns all known fields.
     */

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ENTRY_ID = 1;
    public static final int COLUMN_TITLE = 2;
    public static final int COLUMN_LINK = 3;
    public static final int COLUMN_PUBLISHED = 4;
    /**
     * URL to fetch content from during a sync.
     * <p>
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";
    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds
    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds
    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;
    Context context;
    private boolean forceSync = false;

    private NotificationProgressInterface progressInterface = this;
    int currentProgress = 0;
    NotificationProgressModel notificationProgressModel;
    int noOfModulesOnn;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d(TAG, "SyncAdapter Constructor");
        mContentResolver = context.getContentResolver();
        this.context = context;
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        Log.d(TAG, "SyncAdapter Constructor");
        mContentResolver = context.getContentResolver();
        this.context = context;
    }

    @Override
    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
        super.onSecurityException(account, extras, authority, syncResult);
        Log.d(TAG, "Security Exception Occured");
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        boolean isKeepAliveRunning = AppModel.getInstance()
                .readFromSharedPreferences(context, AppConstants.START_KEEPALIVE).equals("1");
        if (!isKeepAliveRunning) {
            /*FirebaseJobDispatcher dispatcher0 = new FirebaseJobDispatcher(new GooglePlayDriver(context));
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
//                            .setInitialDelay(30, TimeUnit.SECONDS)
                            // Set additional constraints
                            .setConstraints(constraints0)
                            .addTag("my-unique-tag_0")
                            .build();
            try {
                WorkManager.getInstance(context).enqueueUniqueWork("my-unique-tag_0", ExistingWorkPolicy.REPLACE, request0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppModel.getInstance().writeToSharedPreferences(context, AppConstants.START_KEEPALIVE, "1");
        }

        forceSync = extras.getBoolean("forceSync", false);

        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        boolean isLoggedOut = AppModel.getInstance().readBooleanFromSharedPreferences(context, AppConstants.logoutKey, false);

        if (!sharedPref.getString("username", "").equals("")) {
            if ((forceSync || !delay()) && !isLoggedOut) {

                AppModel.getInstance().appendLog(getContext(), "\n*\n*\nSync Started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                try {
//                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID_Sync_Notificaton);

                    int roleId = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();

                    //Sync Notification started
                    SyncNotificationHelperClass.getInstance(getContext()).startSyncNotification();
                    notificationProgressModel = new NotificationProgressModel();
                    currentProgress = 0;
                    progressInterface.onNotificationProgressStarted();

                    if (SyncProgressHelperClass.getInstance(context).getLastMasterRow() <= 0) {
                        SyncUtils.getNetworkConnectionInfo(context, SyncProgressHelperClass.SYNC_TYPE_AUTO_SYNC_ID, null, false);
                    }


                    boolean isFeesHidden = sharedPref.getString(AppConstants.HIDE_FEES_COLLECTION, "").equals("1");
                    boolean isEmployeeHidden = sharedPref.getString(AppConstants.HIDE_Employee, "").equals("1");
                    boolean isTCTEntryHidden = sharedPref.getString(AppConstants.HIDE_TCTEntry, "").equals("1");
                    boolean isExpenseHidden = sharedPref.getString(AppConstants.HIDE_Expense, "").equals("1");
//                                boolean isEmployeeAttendanceHidden = sharedPref.getString(AppConstants.HIDE_EmployeeAttendance,"").equals("1");

                    DataSync.failureCountForAllModules = 0;
                    DataSync.areAllServicesSuccessful = true;
                    DataSync.isFinanceSyncSuccessful = true;

                    if (roleId == AppConstants.roleId_27_P || roleId == AppConstants.roleId_101_ST || roleId == AppConstants.roleId_102_AA || roleId == AppConstants.roleId_109_CM) {

//                        startNotificationForSyncProgress(notificationManager, builder, AppConstants.CHANNEL_ID_Sync_Notificaton,
//                                AppConstants.SYNCNotifyId,true);

                        if (!DataSync.isSyncRunning) {
                            try {

                                DataSync.isSyncRunning = true;
                                final DataSync ds = new DataSync(getContext());

                                ErrorLog.getInstance(context).deleteStudentErrorLogTable(); // clear ErrorLog table for student

                                Thread checkServerThread = new Thread(ds::checkServerWithKeepAlive);

                                checkServerThread.start();
                                checkServerThread.join();

                                //Get the sync start time
                                AppModel.getInstance().writeLongToSharedPreferences(context, AppConstants.SyncStartTime, Calendar.getInstance().getTimeInMillis());

                                //uploading

                                if (DataSync.isSyncSuccessfull) {
                                    Thread uploadStudentsThread = new Thread(() -> {
                                        AppConstants.isAppSyncTableFirstTime = false;
//                                        progressInterface.onNotificationProgressChanged("Student", AppConstants.STUDENT_MODULE, false);
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (SchoolModel model : schoolList) {
                                            ds.UploadStudents(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                        }

                                    });
                                    uploadStudentsThread.start();
                                    uploadStudentsThread.join();

                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread UploadStudentImageThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (SchoolModel model : schoolList) {
                                            ds.uploadStudentImages(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                        }

                                    });
                                    UploadStudentImageThread.start();
                                    UploadStudentImageThread.join();
                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread UploadDeathCertImageThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (SchoolModel model : schoolList) {
                                            ds.UploadDeathCertImage(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                        }
                                    });
                                    UploadDeathCertImageThread.start();
                                    UploadDeathCertImageThread.join();

                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread UploadMedicalCertImageThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (SchoolModel model : schoolList) {
                                            ds.UploadMedicalCertImage(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                        }
                                    });
                                    UploadMedicalCertImageThread.start();
                                    UploadMedicalCertImageThread.join();

                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread UploadBFormImageThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (SchoolModel model : schoolList) {
                                            ds.UploadBFormImage(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                        }
                                    });
                                    UploadBFormImageThread.start();
                                    UploadBFormImageThread.join();

                                }


                                if (DataSync.isSyncSuccessfull) {
                                    Thread UploadEnrollmentsImageThread = new Thread(() -> {
                                        ds.uploadUserSchoolsEnrollmentImages(SyncAdapter.this);

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    UploadEnrollmentsImageThread.start();
                                    UploadEnrollmentsImageThread.join();

                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread uploadAttendanceThread = new Thread(() -> {
                                        ds.UploadAttendance();

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    uploadAttendanceThread.start();
                                    uploadAttendanceThread.join();

                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread uploadPromotion = new Thread(() -> {
                                        ds.UploadPromotion();

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    uploadPromotion.start();
                                    uploadPromotion.join();
                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread uploadWithdrawlThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (SchoolModel model : schoolList) {
                                            ds.UploadWithdrawal(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                        }

                                    });
                                    uploadWithdrawlThread.start();
                                    uploadWithdrawlThread.join();
                                }

                                if (DataSync.isSyncSuccessfull) {
                                    Thread uploadSchoolAuditsThread = new Thread(() -> {
                                        ds.UploadSchoolAudits();

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    uploadSchoolAuditsThread.start();
                                    uploadSchoolAuditsThread.join();
                                }

                                AppModel.getInstance().appendLog(getContext(), "Sync Adapter Uploading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                                if (DataSync.isSyncSuccessfull && !isFeesHidden) {
                                    Thread uploadChashDepositImageThread = new Thread(() -> {
                                        ds.uploadUserSchoolsCashDepositImage(SyncAdapter.this);

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    uploadChashDepositImageThread.start();
                                    uploadChashDepositImageThread.join();
                                }

                                if (DataSync.isSyncSuccessfull && !isFeesHidden) {
                                    Thread uploadFeesHeaderThread = new Thread(() -> {
//                                        progressInterface.onNotificationProgressChanged("Finance", AppConstants.FINANCE_MODULE, false);
                                        try {
                                            boolean isUploadedFH = false;
//                                            while (!isUploadedFH && !Thread.currentThread().isInterrupted()) {
                                            isUploadedFH = ds.uploadFeesHeader();
//                                            }

                                            AppConstants.isAppSyncTableFirstTime = false;
                                        }
//                                        catch (InterruptedException ie){
////                                            Thread.currentThread().interrupt();
//                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    uploadFeesHeaderThread.start();
                                    uploadFeesHeaderThread.join();
                                }

                                if (DataSync.isSyncSuccessfull && DataSync.isFinanceSyncSuccessful && !isFeesHidden) {
                                    Thread uploadCashDepositsThread = new Thread(() -> {
                                        ds.uploadCashDeposits();

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    uploadCashDepositsThread.start();
                                    uploadCashDepositsThread.join();
                                }

                                if (DataSync.isSyncSuccessfull && !isEmployeeHidden) {
                                    Thread uploadEmployeeDetailThread = new Thread(() -> {
//                                        progressInterface.onNotificationProgressChanged("HR", AppConstants.EMPLOYEE_MODULE, false);
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployee();
                                        for (SchoolModel model : schoolList) {
                                            if (model.getAllowedModule_App() != null && Arrays.asList(model.getAllowedModule_App().split(",")).contains(AppConstants.HREmployeeListingModuleValue)) {
                                                ds.UploadEmployeeDetail(model.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted

                                            }
                                        }

                                    });
                                    uploadEmployeeDetailThread.start();
                                    uploadEmployeeDetailThread.join();

                                }

                                if (DataSync.isSyncSuccessfull && !isEmployeeHidden) {
                                    Thread uploadEmployeeLeavesThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployee();
                                        for (SchoolModel model : schoolList) {
                                            if (model.getAllowedModule_App() != null && Arrays.asList(model.getAllowedModule_App().split(",")).contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                                ds.UploadEmployeeTeacherLeaves(model.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                            }
                                        }

                                    });
                                    uploadEmployeeLeavesThread.start();
                                    uploadEmployeeLeavesThread.join();

                                }


                                Thread uploadPendingApprovalThread = new Thread(() -> {
                                    ds.uploadPendingApprovals();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadPendingApprovalThread.start();
                                uploadPendingApprovalThread.join();

                                Thread uploadResignReceivedThread = new Thread(() -> {
                                    ds.uploadResignation();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadResignReceivedThread.start();
                                uploadResignReceivedThread.join();

                                Thread uploadSeparationImagesThread = new Thread(() -> {
                                    ds.uploadSeparationImage();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadSeparationImagesThread.start();
                                uploadSeparationImagesThread.join();

                                Thread uploadUserImagesThread = new Thread(() -> {
                                    ds.uploadUserImages();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadUserImagesThread.start();
                                uploadUserImagesThread.join();

                                if (DataSync.isSyncSuccessfull && !isEmployeeHidden) {
                                    Thread uploadTeacherAttendanceThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployee();
                                        for (SchoolModel model : schoolList) {
                                            if (model.getAllowedModule_App() != null && Arrays.asList(model.getAllowedModule_App().split(",")).contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                                ds.uploadTeacherAttendance(model.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                            }
                                        }

                                    });
                                    uploadTeacherAttendanceThread.start();
                                    uploadTeacherAttendanceThread.join();

                                }

                                //Upload if user is principal only
                                if ((roleId == AppConstants.roleId_27_P || roleId == AppConstants.roleId_109_CM || roleId == AppConstants.roleId_101_ST) && !isTCTEntryHidden) {
                                    Thread uploadTCTEmpSubTagThread = new Thread(() -> {
//                                        progressInterface.onNotificationProgressChanged("TCT Entry", AppConstants.TCTENTRY_MODULE, false);
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchoolsForTCTEntry();
                                        for (SchoolModel model : schoolList) {
                                            ds.UploadTCTEmployeeSubTag(model.getId());
                                            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted

                                        }

                                    });
                                    uploadTCTEmpSubTagThread.start();
                                    uploadTCTEmpSubTagThread.join();

                                }

                                Thread uploadFeedback = new Thread(ds::UploadFeedback);
                                uploadFeedback.start();
                                uploadFeedback.join();

                                //TODO uncomment when expense module will be On
                                if (!isExpenseHidden) {
                                    Thread uploadTransactions = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            progressInterface.onNotificationProgressChanged("Expense", AppConstants.EXPENSE_MODULE, false);
                                            List<SchoolModel> smList = DatabaseHelper.getInstance(context).getAllUserSchoolsForExpense();
                                            for (SchoolModel sm : smList) {
                                                ds.UploadExpenseTransactions(sm.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted

                                            }

                                        }
                                    });

                                    uploadTransactions.start();
                                    uploadTransactions.join();
                                }

                                if (DataSync.isSyncSuccessfull && !isExpenseHidden) {
                                    Thread uploadTransactionImage = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<SchoolModel> smList = DatabaseHelper.getInstance(context).getAllUserSchoolsForExpense();
                                            for (SchoolModel sm : smList) {
                                                ds.uploadExpenseTransactionImages(sm.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                            }

                                        }
                                    });

                                    uploadTransactionImage.start();
                                    uploadTransactionImage.join();
                                }

                                //----------------------------------------------------------------------------------------------
                                //downloading routines
                                if (DataSync.isSyncSuccessfull) {
                                    Thread syncStudentThread = new Thread(() -> {
                                        progressInterface.onNotificationProgressChanged("Student", AppConstants.STUDENT_MODULE, true);
                                        List<SchoolModel> schoolModels = DatabaseHelper.getInstance(context).getAllUserSchools();
                                        for (final SchoolModel model : schoolModels) {
                                            ds.syncStudentData(getContext(), model.getId(),
                                                    DatabaseHelper.getInstance(getContext()).getLatestModifiedOn(model.getId()));
                                        }

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    syncStudentThread.start();
                                    syncStudentThread.join();

                                }


                                if (DataSync.isSyncSuccessfull) {
                                    Thread syncAllUserSchoolsThread = new Thread(() -> {
                                        ds.SyncAllUserSchools();
                                    });
                                    syncAllUserSchoolsThread.start();
                                    syncAllUserSchoolsThread.join();
                                }


                                if (DataSync.isSyncSuccessfull) {
                                    Thread summaryThread = new Thread(ds::getSummaryForSchools);
                                    summaryThread.start();
                                    summaryThread.join();
                                }

                                if (DataSync.isSyncSuccessfull && DataSync.isFinanceSyncSuccessful && !isFeesHidden) {
                                    Thread feesHeaderThread = new Thread(() -> {
                                        ds.getFeesHeader();
                                        progressInterface.onNotificationProgressChanged("Finance", AppConstants.FINANCE_MODULE, true);

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    feesHeaderThread.setPriority(Thread.MAX_PRIORITY);
                                    feesHeaderThread.start();
                                    feesHeaderThread.join();
                                }

                                if (DataSync.isSyncSuccessfull && DataSync.isFinanceSyncSuccessful && !isFeesHidden) {
                                    Thread cashdepositThread = new Thread(() -> {
                                        ds.getCashDeposits();
                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    cashdepositThread.start();
                                    cashdepositThread.join();
                                }


                                Thread getSeparationThread = new Thread(() -> {
                                    ds.syncSeparationDetailData(getContext());
                                    progressInterface.onNotificationProgressChanged("HR", AppConstants.EMPLOYEE_MODULE, true);

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                getSeparationThread.start();
                                getSeparationThread.join();

                                Thread getEmployeeThread = new Thread(() -> {
                                    ds.syncEmployeeData(getContext());
                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                getEmployeeThread.start();
                                getEmployeeThread.join();


                                Thread studentImageDownloadThread = new Thread(() -> {
                                    List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
                                    for (SchoolModel model : schoolList) {
                                        ds.DownloadStudentPictures(model.getId());
                                    }
                                    AppConstants.isAppSyncTableFirstTime = false;

                                });
                                studentImageDownloadThread.start();
                                studentImageDownloadThread.join();


                                //Download if user is principal only
                                if ((roleId == AppConstants.roleId_27_P || roleId == AppConstants.roleId_109_CM || roleId == AppConstants.roleId_101_ST) && !isTCTEntryHidden) {

                                    Thread TCTEntryThread = new Thread(() -> {
                                        List<SchoolModel> schoolModels = DatabaseHelper.getInstance(context).getAllUserSchoolsForTCTEntry();
                                        for (final SchoolModel model : schoolModels) {
                                            ds.syncTCTEmployeeData(getContext(), model.getId());
                                        }
//                                        progressInterface.onNotificationProgressChanged("TCT Entry", AppConstants.TCTENTRY_MODULE, true);

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    TCTEntryThread.start();
                                    TCTEntryThread.join();
                                }

                                //TODO uncomment when expense module will be On
                                if (!isExpenseHidden) {
                                    Thread getExpenseThread = new Thread(() -> {
                                        ds.syncExpenseData(getContext());
                                        progressInterface.onNotificationProgressChanged("Expense", AppConstants.EXPENSE_MODULE, true);

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    getExpenseThread.start();
                                    getExpenseThread.join();
                                }

                                //Starting Checksum
                                Thread checkSumThread = new Thread(() -> {

                                    startCheckSum();

                                    //Should be in last after every sync complete
//                                    DataSync.getInstance(context).onSyncProgressComplete();
                                });
                                checkSumThread.setPriority(Thread.MAX_PRIORITY);
                                checkSumThread.start();
                                checkSumThread.join();


                                //TODO uncomment when expense module will be On
                                //Starting Checksum
                             /*   Thread ExpensecheckSumThread = new Thread(() -> {

                                    startExpenseCheckSum();

                                    //Should be in last after every sync complete
//                                    DataSync.getInstance(context).onSyncProgressComplete();
                                });
                                ExpensecheckSumThread.setPriority(Thread.MAX_PRIORITY);
                                ExpensecheckSumThread.start();
                                ExpensecheckSumThread.join();*/

                                //UploadAppSyncStatus
                                /*Thread appSyncStatusThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.uploadAppSyncStatus();
                                    }
                                });
                                appSyncStatusThread.join();
                                appSyncStatusThread.start();*/

                                //UploadAppSyncStatusMaster
                                Thread appSyncStatusMasterThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.uploadAppSyncStatusMaster();
                                    }
                                });
                                appSyncStatusMasterThread.join();
                                appSyncStatusMasterThread.start();


                                if (DataSync.isSyncSuccessfull) {
                                    setDelay(false);
                                } else {
                                    setDelay(true);
//                                if (elapsedTime() > 180) {
//                                    SurveyAppModel.getInstance().appendLog(getContext(), "Notification showed at " + SurveyAppModel.getInstance().getCurrentDateTime(" dd-MM-yyy hh:mm:ss a"));
//                                    showNotification();
//                                }
                                }
                                AppModel.getInstance().appendLog(getContext(), "\nSync Adapter finished at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n*\n*\n");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                AppModel.getInstance().appendErrorLog(context, "Main Syncing Service " + ex.getLocalizedMessage());
                                AppModel.getInstance().appendErrorLog(context, "Main Syncing Service " + ex.getMessage());
                            }
                        }
                    } else if (roleId == AppConstants.roleId_103_V || roleId == AppConstants.roleId_7_AEM) {
                        //Here sumary api is called only and set areAllServicesSuccessful = true
                        if (!DataSync.isSyncRunning) {
                            try {

//                                final ArrayList<SchoolModel> sms = DatabaseHelper.getInstance(context).getAllUserSchools();
                                final int schoolID = AppModel.getInstance().getSearchedSchoolId(context);
                                SchoolModel model = DatabaseHelper.getInstance(context).getSchoolById(schoolID);

                                if (schoolID > 0) {
                                    DataSync.areAllServicesSuccessful = true;
                                } else {
                                    DataSync.areAllServicesSuccessful = false;
                                }

                                DataSync.isSyncRunning = true;
                                final DataSync ds = new DataSync(getContext());

                                //Get the sync start time
                                AppModel.getInstance().writeLongToSharedPreferences(context, AppConstants.SyncStartTime, Calendar.getInstance().getTimeInMillis());

                                //Teacher Attendance and leave upload only for AEM role id
                                if (roleId == AppConstants.roleId_7_AEM && !isEmployeeHidden) {
                                    Thread uploadEmployeeLeavesThread = new Thread(() -> {
                                        AppConstants.isAppSyncTableFirstTime = false;

                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployee();
                                        for (SchoolModel schoolModel : schoolList) {
                                            if (schoolModel.getAllowedModule_App() != null && Arrays.asList(schoolModel.getAllowedModule_App().split(",")).contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                                ds.UploadEmployeeTeacherLeaves(schoolModel.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                            }
                                        }

                                    });
                                    uploadEmployeeLeavesThread.start();
                                    uploadEmployeeLeavesThread.join();

                                    Thread uploadTeacherAttendanceThread = new Thread(() -> {
                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployee();
                                        for (SchoolModel schoolModel : schoolList) {
                                            if (schoolModel.getAllowedModule_App() != null && Arrays.asList(schoolModel.getAllowedModule_App().split(",")).contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                                ds.uploadTeacherAttendance(schoolModel.getId());
                                                AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
                                            }
                                        }

                                    });
                                    uploadTeacherAttendanceThread.start();
                                    uploadTeacherAttendanceThread.join();
                                }

                                Thread uploadPendingApprovalThread = new Thread(() -> {
                                    ds.uploadPendingApprovals();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadPendingApprovalThread.start();
                                uploadPendingApprovalThread.join();

                                Thread uploadResignReceivedThread = new Thread(() -> {
//                                    progressInterface.onNotificationProgressChanged("HR", AppConstants.EMPLOYEE_MODULE, false);
                                    ds.uploadResignation();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadResignReceivedThread.start();
                                uploadResignReceivedThread.join();

                                Thread uploadSeparationImagesThread = new Thread(() -> {
                                    ds.uploadSeparationImage();

                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                uploadSeparationImagesThread.start();
                                uploadSeparationImagesThread.join();

                                Thread uploadFeedback = new Thread(ds::UploadFeedback);
                                uploadFeedback.start();
                                uploadFeedback.join();

                                Thread syncAllUserSchoolsThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.SyncSelectedUserSchools();

                                    }
                                });
                                syncAllUserSchoolsThread.start();
                                syncAllUserSchoolsThread.join();


                                Thread syncStudentThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressInterface.onNotificationProgressChanged("Student", AppConstants.STUDENT_MODULE, true);
                                        ds.syncSelectedSchoolStudentData(getContext(), DatabaseHelper.getInstance(getContext()).getLatestModifiedOn(schoolID));
                                        AppConstants.isAppSyncTableFirstTime = false;
                                    }
                                });
                                syncStudentThread.start();
                                syncStudentThread.join();

                                Thread studentImageDownloadThread = new Thread(() -> {
                                    ds.DownloadStudentPictures(schoolID);
                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                studentImageDownloadThread.start();
                                studentImageDownloadThread.join();

                                Thread summaryThread = new Thread(() -> ds.getSummary(context, schoolID));
                                summaryThread.start();
                                summaryThread.join();

                                if (DataSync.isFinanceSyncSuccessful && !isFeesHidden) {
                                    Thread feesHeaderThread = new Thread(() -> {

//                                  for (SchoolModel model : sms) {
                                        if (model != null) {
                                            if (model.getId() == schoolID) {
                                                ds.syncFeesHeader(model.getAcademic_Session_Id(), model.getId());

                                                AppConstants.isAppSyncTableFirstTime = false;
                                            }
                                        }
                                        progressInterface.onNotificationProgressChanged("Finance", AppConstants.FINANCE_MODULE, true);

//                                  }

                                    });
                                    feesHeaderThread.start();
                                    feesHeaderThread.join();
                                }


//                                if (!isFeesHidden) {
//                                    Thread cashReceiptsThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            for (SchoolModel model : sms) {
//                                                if (model.getId() == schoolID) {
//                                                    ds.syncCashReceipts(model.getSchool_yearId(), AppReceipt.getInstance(context).getMaxSysId());
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    });
//                                    cashReceiptsThread.start();
//                                    cashReceiptsThread.join();
//                                }

//                                if (!isFeesHidden) {
//                                    Thread invoicesThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            for (SchoolModel model : sms) {
//                                                if (model.getId() == schoolID) {
//                                                    ds.syncCashInvoices(model.getSchool_yearId(), AppInvoice.getInstance(context).getMaxSysId());
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    });
//                                    invoicesThread.start();
//                                    invoicesThread.join();
//                                }

                                if (DataSync.isFinanceSyncSuccessful && !isFeesHidden) {
                                    Thread cashdepositThread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (model != null) {
//                                                for (SchoolModel model : sms) {
                                                if (model.getId() == schoolID) {
                                                    ds.syncCashDeposits(model.getId());

                                                    AppConstants.isAppSyncTableFirstTime = false;
//                                                        break;
                                                }
//                                                }
                                            }

                                        }
                                    });
                                    cashdepositThread.start();
                                    cashdepositThread.join();

                                }

                                Thread getSeparationThread = new Thread(() -> {
                                    ds.syncSeparationDetailData(getContext());
                                    AppConstants.isAppSyncTableFirstTime = false;
                                });
                                getSeparationThread.start();
                                getSeparationThread.join();

//                                if (DataSync.isSyncSuccessfull && !isEmployeeHidden) {
                                if (!isEmployeeHidden) {
//                                        int schoolId = AppModel.getInstance().getSelectedSchool(context);
//                                    Thread getEmployeeThread = new Thread(() -> ds.syncEmployeeData(getContext(), DatabaseHelper.getInstance(getContext()).getLatestModifiedOn()));
                                    if (model != null) {
                                        Thread getEmployeeThread = new Thread(() -> {
                                            ds.syncEmployeeDataForSingleSchool(getContext(), model);
                                            progressInterface.onNotificationProgressChanged("HR", AppConstants.EMPLOYEE_MODULE, true);

                                            AppConstants.isAppSyncTableFirstTime = false;
                                        });
                                        getEmployeeThread.start();
                                        getEmployeeThread.join();
                                    }
                                }

                                //TODO uncomment when expense module will be On
                                if (!isExpenseHidden) {
                                    //Expenses Data for Single School
                                    Thread getExpenseThread = new Thread(() -> {
                                        ds.syncExpenseDataForSingleSchool(getContext(), model);
                                        progressInterface.onNotificationProgressChanged("Expense", AppConstants.EXPENSE_MODULE, true);

                                        AppConstants.isAppSyncTableFirstTime = false;
                                    });
                                    getExpenseThread.start();
                                    getExpenseThread.join();
                                }

                                /*Thread separationImageDownloadThread = new Thread(() -> ds.DownloadSeparationImages(model.getId()));
                                separationImageDownloadThread.start();
                                separationImageDownloadThread.join();*/

                                //Starting Checksum

                                Thread checkSumThread = new Thread(() -> {

                                    startCheckSum();

                                    //Should be in last after every sync ccomplete
//                                    DataSync.getInstance(context).onSyncProgressComplete();
                                });
                                checkSumThread.setPriority(Thread.MAX_PRIORITY);
                                checkSumThread.start();
                                checkSumThread.join();


                                //TODO uncomment when expense module will be On
                                //Starting Expense Checksum
                                /*Thread expensecheckSumThread = new Thread(() -> {

                                    startExpenseCheckSum();

                                    //Should be in last after every sync ccomplete
//                                    DataSync.getInstance(context).onSyncProgressComplete();
                                });
                                expensecheckSumThread.setPriority(Thread.MAX_PRIORITY);
                                expensecheckSumThread.start();
                                expensecheckSumThread.join();*/

                                //UploadAppSyncStatus
                                /*Thread appSyncStatusThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.uploadAppSyncStatus();
                                    }
                                });
                                appSyncStatusThread.join();
                                appSyncStatusThread.start();*/

                                //UploadAppSyncStatusMaster
                                Thread appSyncStatusMasterThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.uploadAppSyncStatusMaster();
                                    }
                                });
                                appSyncStatusMasterThread.join();
                                appSyncStatusMasterThread.start();

                                AppModel.getInstance().appendLog(getContext(), "\nSync Adapter finished at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n*\n*\n");


                                if (DataSync.areAllServicesSuccessful) {
                                    context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncSuccessTime", AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm a")).apply();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(context, "Exception occured in SyncAdapter: " + e.getLocalizedMessage());
                }
            }
            DataSync.isSyncRunning = false;
        }


        if (DataSync.areAllServicesSuccessful)
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "true").commit();
        else
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "false").commit();

        AppModel.getInstance().appendLog(getContext(), "\n*\n*\nSync Ended at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");
        //Sync Completed
        progressInterface.onNotificationProgressCompleted();
        DataSync.getInstance(context).onSyncProgressComplete();

        Intent i = new Intent(SyncUtils.syncFinishedIntentFilter);
        context.sendBroadcast(i);

    }

    private void startCheckSum() {
        WeakReference<Context> weakContext = new WeakReference<>(context);

        boolean isSyncTakesAnHour = isSyncTakesAnHour();
        if (DataSync.isSyncSuccessfull && DataSync.failureCountForAllModules == 0 && !isSyncTakesAnHour) {
            FinanceCheckSum.Instance(weakContext).invokeCheckSum(forceSync);
        }

        boolean NewLogin = AppModel.getInstance().readBooleanFromSharedPreferences(context,
                AppConstants.NewLogin, true);

        if (NewLogin) {
            AppModel.getInstance().writeBooleanToSharedPreferences(context, AppConstants.NewLogin, false);
            FinanceCheckSum.Instance(weakContext).checkSumSuccessful(true);
            FinanceCheckSum.Instance(weakContext).setCheckSumApplicable(false);
            FinanceCheckSum.Instance(weakContext).setCurrentTimeAsRunTime();
        }
    }

    private void startExpenseCheckSum() {
        WeakReference<Context> weakContext = new WeakReference<>(context);

//        boolean shouldChecksumTrigger  = isSyncTakesAnHour();
//        if (shouldChecksumTrigger){
        ExpenseCheckSum.Instance(weakContext).invokeCheckSum(forceSync);
//        }
    }

    private boolean isSyncTakesAnHour() {
        long SyncStartTime = AppModel.getInstance().readLongFromSharedPreferences(context, AppConstants.SyncStartTime, 0);
        long elapsedTime = Calendar.getInstance().getTimeInMillis() - SyncStartTime;
        long elapsedHours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        return elapsedHours >= 1;
    }

    private void startNotificationForSyncProgress(NotificationManagerCompat notificationManager,
                                                  NotificationCompat.Builder builder, String CHANNEL_ID, int notificationId,
                                                  boolean isShowProgress) {
        builder.setContentTitle("Syncing Progress")
                .setContentText("Sync Started")
                .setSmallIcon(R.mipmap.ic_launcher_white)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setPriority(NotificationCompat.PRIORITY_LOW);

        AppModel.getInstance().createNotificationChannel(context, CHANNEL_ID, "sync", "sync", false);

        int PROGRESS_CURRENT = 0;
        if (isShowProgress) {
            builder.setProgress(100, PROGRESS_CURRENT, false);
        } else {
            builder.setProgress(0, 0, true);
        }
        notificationManager.notify(notificationId, builder.build());
    }

    private void updateNotificationForSync(NotificationManagerCompat notificationManager,
                                           NotificationCompat.Builder builder, int notificationId, String text,
                                           int PROGRESS_CURRENT, boolean isShowProgress) {
        if (isShowProgress) {
            builder.setContentText(text)
                    .setProgress(100, PROGRESS_CURRENT, false);
            notificationManager.notify(notificationId, builder.build());
        } else {
            builder.setContentText(text);
            notificationManager.notify(notificationId, builder.build());
        }
    }

    private void stopNotificationForSync(NotificationManagerCompat notificationManager,
                                         NotificationCompat.Builder builder, int notificationId, String text) {
        builder.setContentText(text)
                .setProgress(0, 0, false);
        notificationManager.notify(notificationId, builder.build());
    }

    private void setDelay(boolean isDelayed) {
        int pendingCount = DatabaseHelper.getInstance(context).getAllPendingRecords(AppModel.getInstance().getSelectedSchool(context));
        //Separation Approval records comes for any school that's why its added separately
        pendingCount += DatabaseHelper.getInstance(context).getPendingSeparationRecordsForSync();
        //Feedback records
        pendingCount += HelpHelperClass.getInstance(context).getPendingFeedbackRecordsForSync();

        if (pendingCount > 0) {
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncSuccessTime", AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm a")).apply();
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putBoolean("syncSuccess", false).apply();

            if (isDelayed) {
                int retries = context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getInt("retries", 0) + 1;
                AppModel.getInstance().appendLog(getContext(), "\n*\n*\n Attempt --->" + retries + "\n*\n*\n");
                context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putInt("retries", retries).apply();
                if (retries >= 3 && !forceSync && DataSync.failureCount > 0) {
                    AppModel.getInstance().appendLog(getContext(), "Data syncing failed, now delayed for one hour.");
                    context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putInt("retries", 0).apply();
                    context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putBoolean("isdelayed", isDelayed).apply();
                    showNotification();
                    setLastSynctime();
                }
            }
        }

        if (!isDelayed && pendingCount == 0) {
            setLastSynctime();
            AppModel.getInstance().appendLog(getContext(), "Synced All Data Successfully");
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putBoolean("isdelayed", isDelayed).apply();
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putBoolean("syncSuccess", true).apply();
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putInt("retries", 0).apply();
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncSuccessTime", AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm a")).apply();
        }
    }

    private boolean delay() {
        boolean isDelayed = getContext().getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getBoolean("isdelayed", false);
        if (isDelayed && elapsedTime() >= 60) {
            return false;
        } else if (!isDelayed) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onProcessCompleted() {
        DataSync ds = new DataSync(getContext());
//        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();
//        for (SchoolModel model:schoolList){
        AppConstants.isAppSyncTableFirstTime = false;
        ds.UploadEnrollments();
//        }

        if (DataSync.areAllServicesSuccessful)
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "true").commit();
        else
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "false").commit();


//        ds.UploadEnrollments();
    }

    public boolean SyncDelay() {
        long lastSyncOn = getContext().getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getLong("synctime", 0);
        if (lastSyncOn > 0) {
            long dateNow = System.currentTimeMillis();
            long elapsedInMins = (dateNow - lastSyncOn) / 1000 / 60;
            if (elapsedInMins > 180) {
                return true;
            } else {
                return false;
            }
        } else
            return true;
    }

    public void showNotification() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Data syncing error")
                .setContentText("Error while syncing data, please contact IT department")
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void setLastSynctime() {

        if (DataSync.areAllServicesSuccessful)
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "true").apply();
        else
            context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "false").apply();


//        context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putLong("synctime", System.currentTimeMillis()).apply();


    }

    public long elapsedTime() {
        long lastSyncOn = getContext().getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).getLong("synctime", 0);
        if (lastSyncOn == 0) {
            return 0;
        } else {
            long dateNow = System.currentTimeMillis();
            return (dateNow - lastSyncOn) / 1000 / 60;
        }
    }

    @Override
    public void onNotificationProgressStarted() {

        SyncNotificationHelperClass.isSyncNotificationDeleted = false;

        noOfModulesOnn = 1;  // 1 is by default because student module is always onn
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        boolean isFeesHidden = sharedPref.getString(AppConstants.HIDE_FEES_COLLECTION, "").equals("1");
        boolean isEmployeeHidden = sharedPref.getString(AppConstants.HIDE_Employee, "").equals("1");
//        boolean isTCTEntryHidden = sharedPref.getString(AppConstants.HIDE_TCTEntry, "").equals("1"); //It is commented because it is a submodule of employee module
        boolean isExpenseHidden = sharedPref.getString(AppConstants.HIDE_Expense, "").equals("1");
        if (!isFeesHidden) {
            noOfModulesOnn++;
        }
        if (!isEmployeeHidden) {
            noOfModulesOnn++;
        }
//        if (!isTCTEntryHidden) {
//            noOfModulesOnn++;
//        }
        if (!isExpenseHidden) {
            noOfModulesOnn++;
        }
    }

    @Override
    public void onNotificationProgressChanged(String module, int moduleId, boolean isDownloading) {
        //total of 9 downloading threads and 20 Uploading Threads of 5 modules only
        if (currentProgress > 0) {
            Log.i("noti","isSyncNotificationDeleted:"+ SyncNotificationHelperClass.isSyncNotificationDeleted);
        }

        if (!SyncNotificationHelperClass.isSyncNotificationDeleted) {
            if (currentProgress < 100) {
                if (noOfModulesOnn == 5) {
                    currentProgress += 20;
                } else if (noOfModulesOnn == 4) {
                    currentProgress += 25;
                } else if (noOfModulesOnn == 3) {
                    currentProgress += 33;
                } else if (noOfModulesOnn == 2) {
                    currentProgress += 50;
                } else if (noOfModulesOnn == 1) {
                    currentProgress = 100;
                }


            } else {
                currentProgress = 100;
            }

            notificationProgressModel.setProgress(currentProgress);
            notificationProgressModel.setTCFModuleName(module);
//        notificationProgressModel.setSyncType(isDownloading ? "Downloading" : "Uploading");
            SyncNotificationHelperClass.getInstance(context).sendProgressNotification(notificationProgressModel);
        }
    }

    @Override
    public void onNotificationProgressCompleted() {
        SyncNotificationHelperClass.getInstance(context).onSyncNotificationProgressComplete();
        currentProgress = 0;
        SyncNotificationHelperClass.isSyncNotificationDeleted = true;
    }

    @Override
    public void onNotificationProgressCanceled() {
//        currentProgress = 0;
//        SyncNotificationHelperClass.isSyncNotificationDeleted = true;
    }

//    private class AsyncClass extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
//
//            if (!sharedPref.getString("username", "").equals("")) {
//                if (forceSync || !delay()) {
//
//                    SurveyAppModel.getInstance().appendLog(getContext(), "\n*\n*\nSync Adapter called at: " + SurveyAppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");
//
//                    if (!DataSync.isSyncRunning) {
//                        try {
//                            DataSync.isSyncRunning = true;
//                            DataSync ds = new DataSync(getContext());
//
//                            ds.checkServer();
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.syncStudentData(getContext(), SurveyAppModel.getInstance().getSelectedSchool(getContext()), DatabaseHelper.getInstance(getContext()).getLatestModifiedOn());
//                            }
//
//                            SurveyAppModel.getInstance().appendLog(getContext(), "Sync Adapter Uploading started at: " + SurveyAppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.UploadStudents();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.UploadEnrollmentsImage(SyncAdapter.this);
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.uploadCashDepositImage(SyncAdapter.this);
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.UploadAttendance();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.UploadPromotion();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.UploadWithdrawal();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.UploadSchoolAudits();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.SyncAllUserSchools();
//                            }
//
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.uploadAppReceipts();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.uploadCashDeposits();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.getCashReceipts();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.getInvoices();
//                            }
//
//                            if (DataSync.isSyncSuccessfull) {
//                                ds.getCashDeposits();
//                            }
//
//                            ds.DownloadStudentPictures();
//
//                            if (DataSync.isSyncSuccessfull) {
//                                setDelay(false);
//                            } else {
//                                setDelay(true);
////                                if (elapsedTime() > 180) {
////                                    SurveyAppModel.getInstance().appendLog(getContext(), "Notification showed at " + SurveyAppModel.getInstance().getCurrentDateTime(" dd-MM-yyy hh:mm:ss a"));
////                                    showNotification();
////                                }
//                            }
//
//                            SurveyAppModel.getInstance().appendLog(getContext(), "\nSync Adapter finished at: " + SurveyAppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n*\n*\n");
//                            ds = null;
//                            System.gc();
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//                DataSync.isSyncRunning = false;
//            }
//
//            Intent i = new Intent(SyncUtils.syncFinishedIntentFilter);
//            context.sendBroadcast(i);
//            return null;
//        }
//    }

}
