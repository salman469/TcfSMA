package com.tcf.sma.utils;

import android.content.Context;
import android.widget.Toast;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Helpers.DbTables.FeesCollection.CashDeposit;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CheckSumModel;
import com.tcf.sma.Models.ExpenseCheckSumModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class ExpenseCheckSum {
    private static WeakReference<Context> mContext;
    private static ExpenseCheckSum object;
    private boolean flushFeesHeaderTable = false;
    private boolean flushFeesDetailTable = false;
    private boolean flushCashDeposit = false;
    private boolean flushStudentTable = false;
    private boolean flushSchoolClassTable = false;
    private boolean flushSubheadLimitsMonthlyTable = false;
    private boolean flushSchoolPettyCashMonthlyLimitsTable = false;
    private boolean flushTransactionsTable = false;
    private boolean flushAmountClosingTable = false;


    private ExpenseCheckSum(WeakReference<Context> mContext) {
        this.mContext = mContext;
    }

    public static ExpenseCheckSum Instance(WeakReference<Context> context) {
        if (object == null || mContext == null)
            object = new ExpenseCheckSum(context);

        return object;
    }

    public void invokeCheckSum(boolean isForceSync) {
        if (isForceSync || isCheckSumApplicable() && checkPendingRecord()) {
            try {
                checkSum();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CheckSumModel getLocalCheckSumModel(int schoolId) {
        try {
            return FeesCollection.getInstance(mContext.get()).getCheckSumCount(schoolId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isCheckSumApplicable() {
        boolean checkSumApplicable = false;
        try {
            checkSumApplicable = mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE)
                    .getBoolean(AppConstants.KEY_EXPENSE_CHECKSUM, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        boolean NewLogin = AppModel.getInstance().readBooleanFromSharedPreferences(mContext.get(),
//                AppConstants.NewLogin, true);
//
//        if (NewLogin) {
//            return false;
//        }

        if (!checkSumApplicable) {
            long elapsedTime = Calendar.getInstance().getTimeInMillis() - lastRanTime();
            long elapsedHours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
            return elapsedHours > 23;
        }
        return true;
    }

    public void setCheckSumApplicable(boolean checkSum) {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putBoolean(AppConstants.KEY_EXPENSE_CHECKSUM, checkSum).apply();
    }

    private long lastRanTime() {
        return mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).getLong(AppConstants.KEY_RUN_TIME_EXPENSE, 0);

    }

    public void setCurrentTimeAsRunTime() {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putLong(AppConstants.KEY_RUN_TIME_EXPENSE, Calendar.getInstance().getTimeInMillis()).apply();
    }

    public void setCurrentTimeAsRunTime(long timeInMilliSecond) {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putLong(AppConstants.KEY_RUN_TIME_EXPENSE, timeInMilliSecond).apply();
    }

    public boolean isCheckSumSuccessfull() {
        return mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).getBoolean(AppConstants.KEY_CHECKSUM_RAN_EXPENSE, false);
    }

    public void checkSumSuccessful(boolean isSuccessfull) {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putBoolean(AppConstants.KEY_RUN_TIME_EXPENSE, isSuccessfull).apply();
    }

    private boolean checkPendingRecord() {
        int transaction = ExpenseHelperClass.getInstance(mContext.get()).getAllTransactionsForUpload().size();
        int schoolpettycash = ExpenseHelperClass.getInstance(mContext.get()).getAllSchoolPettyCashMonthlyLimitsForUpload().size();
        int subheadlimitsmonthly = ExpenseHelperClass.getInstance(mContext.get()).getAllSubheadLimitsMonthlyForUpload().size();
        int amountclosing = ExpenseHelperClass.getInstance(mContext.get()).getAllAmountClosingForUpload().size();
        return transaction == 0 && schoolpettycash == 0 && subheadlimitsmonthly == 0 && amountclosing == 0;
    }

    private void logger(String message) {
        AppModel.getInstance().appendErrorLog(mContext.get(), message);
    }

    private void flushTables(int id) {

        if (flushSubheadLimitsMonthlyTable) {
            ExpenseHelperClass.getInstance(mContext.get()).deleteSubheadLimitsMonthlyRecords(id);
        }

        if (flushSchoolPettyCashMonthlyLimitsTable) {
            ExpenseHelperClass.getInstance(mContext.get()).deleteSchoolPettyCashMonthlyLimitsRecords(id);
        }

        if (flushTransactionsTable) {
            ExpenseHelperClass.getInstance(mContext.get()).deleteTransactionsRecords(id);
        }

        if (flushAmountClosingTable) {
            ExpenseHelperClass.getInstance(mContext.get()).deleteAmountClosingRecords(id);
        }

        setCurrentTimeAsRunTime();
        setCheckSumApplicable(false);
        logger("Checksum Ended...");

        //Start sync if any checksum failed
//        if (flushFeesHeaderTable || flushFeesDetailTable || flushCashDeposit || flushStudentTable || flushSchoolClassTable ) {
//            SurveyAppModel.getInstance().writeToSharedPreferences(mContext.get(),checksumFailSyncRequired,"1");
//        }
    }


    private void checkSum() {
        ArrayList<SchoolModel> sm = AppModel.getInstance().getSchoolsForLoggedInUser(mContext.get());
        int checkSumFailed = 0;
        for (SchoolModel model : sm) {
            ApiInterface apiInterface = ApiClient.getClient(mContext.get()).create(ApiInterface.class);
            Call<ExpenseCheckSumModel> call = apiInterface.getExpenseCheckSum(model.getId(), model.getAcademic_Session_Id(), "Bearer " + AppModel.getInstance().getToken(mContext.get()));
            try {

                ExpenseCheckSumModel localCheckSum = getLocalExpenseCheckSumModel(model.getId());
                Response<ExpenseCheckSumModel> response = call.execute();
                if (response.isSuccessful()) {
                    logger("Expense Checksum Started...");
                    ExpenseCheckSumModel BOCheckSum = response.body();

                    List<String> allowedModules = null;
                    if (model.getAllowedModule_App() != null) {
                        allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                    }

                    assert localCheckSum != null;

                    if (allowedModules != null && allowedModules.contains(AppConstants.ExpenseModuleValue)) {
                        if (localCheckSum.getSubheadLimitsMonthlyCount() != BOCheckSum.getSubheadLimitsMonthlyCount()) {
                            logger("SubheadLimitsMonthly Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getSubheadLimitsMonthlyCount()
                                    + " Server Records: " + BOCheckSum.getSubheadLimitsMonthlyCount() + " School ID: " + model.getId() + " starting flush");
                            flushSubheadLimitsMonthlyTable = true;
                        } else {
                            logger("SubheadLimitsMonthly Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getSubheadLimitsMonthlyCount()
                                    + " Server Records: " + BOCheckSum.getSubheadLimitsMonthlyCount() + " School ID: " + model.getId());
                            flushSubheadLimitsMonthlyTable = false;
                        }

                        if (localCheckSum.getSchoolPettyCashMonthlyLimitsCount() != BOCheckSum.getSchoolPettyCashMonthlyLimitsCount()) {
                            logger("SchoolPettyCashMonthlyLimits Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getSchoolPettyCashMonthlyLimitsCount()
                                    + " Server Records: " + BOCheckSum.getSchoolPettyCashMonthlyLimitsCount() + " School ID: " + model.getId() + " starting flush");
                            flushSchoolPettyCashMonthlyLimitsTable = true;
                        } else {
                            logger("SubheadLimitsMonthly Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getSchoolPettyCashMonthlyLimitsCount()
                                    + " Server Records: " + BOCheckSum.getSchoolPettyCashMonthlyLimitsCount() + " School ID: " + model.getId());
                            flushSchoolPettyCashMonthlyLimitsTable = false;
                        }

                        if (localCheckSum.getTransactionsCount() != BOCheckSum.getTransactionsCount()) {
                            logger("Transactions Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getTransactionsCount()
                                    + " Server Records: " + BOCheckSum.getTransactionsCount() + " School ID: " + model.getId() + " starting flush");
                            flushTransactionsTable = true;
                        } else {
                            logger("Transactions Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getTransactionsCount()
                                    + " Server Records: " + BOCheckSum.getTransactionsCount() + " School ID: " + model.getId());
                            flushTransactionsTable = false;
                        }

                        if (localCheckSum.getAmountClosingCount() != BOCheckSum.getAmountClosingCount()) {
                            logger("AmountClosing Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getAmountClosingCount()
                                    + " Server Records: " + BOCheckSum.getAmountClosingCount() + " School ID: " + model.getId() + " starting flush");
                            flushAmountClosingTable = true;
                        } else {
                            logger("AmountClosing Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getAmountClosingCount()
                                    + " Server Records: " + BOCheckSum.getAmountClosingCount() + " School ID: " + model.getId());
                            flushAmountClosingTable = false;
                        }

                        if (flushSchoolPettyCashMonthlyLimitsTable || flushSubheadLimitsMonthlyTable || flushAmountClosingTable || flushTransactionsTable) {
                            checkSumSuccessful(false);
                            checkSumFailed++;
                        } else {
                            checkSumSuccessful(true);
                        }
                    }

                    flushTables(model.getId());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (checkSumFailed > 0) {
            checkSumSuccessful(false);
        } else {
            checkSumSuccessful(true);
        }

        //this will clear value
        AppModel.getInstance().writeToSharedPreferences(mContext.get(), AppConstants.syncActionPerformedFrom, "");
    }

    private String getActionPerformed() {
        String action = AppModel.getInstance().readFromSharedPreferences(mContext.get(), AppConstants.syncActionPerformedFrom, "");
        if (!action.isEmpty() && action.equalsIgnoreCase("login"))
            return "when Login sync started.";
        else if (!action.isEmpty() && action.equalsIgnoreCase("syncbutton"))
            return "when Manually sync started.";
//        else
//            return "when Scheduled sync started.";
        return "";
    }

    public boolean isChecksumSuccess(Context context, boolean showFailureMessage) {
        int i = 0;
        if (isCheckSumApplicable()) {
            i++;
            if (showFailureMessage)
                Toast.makeText(context, "Check sum is pending, please wait for the sync to complete.", Toast.LENGTH_SHORT).show();
        } else if (!isCheckSumSuccessfull()) {
            i++;
            if (showFailureMessage)
                Toast.makeText(context, "Disabled due to checksum failure/", Toast.LENGTH_SHORT).show();
        }

        try {
//            Viewer is not allowed to edit finance
            int roleId = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();
            if (roleId == AppConstants.roleId_103_V || roleId == AppConstants.roleId_7_AEM) {
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return i == 0;
    }

    public CheckSumModel getServerCheckSum(int schoolId, int academicSession_id) {
        ApiInterface apiInterface = ApiClient.getClient(mContext.get()).create(ApiInterface.class);
        Call<CheckSumModel> call = apiInterface.getCheckSum(schoolId, academicSession_id, "Bearer " + AppModel.getInstance().getToken(mContext.get()));
        CheckSumModel checkSumModel = null;
        try {
            Response<CheckSumModel> response = call.execute();
            if (response.isSuccessful()) {
//                logger("Checksum Started...");
                checkSumModel = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkSumModel;
    }


    private void Expensechecksum(SchoolModel model) {

    }

    public ExpenseCheckSumModel getLocalExpenseCheckSumModel(int schoolId) {
        try {
            return ExpenseHelperClass.getInstance(mContext.get()).getExpenseCheckSumCount(schoolId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
