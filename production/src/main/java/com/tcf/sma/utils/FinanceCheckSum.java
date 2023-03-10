package com.tcf.sma.utils;

import android.content.Context;
import android.widget.Toast;

import com.tcf.sma.DataSync.DataSync;
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

public class FinanceCheckSum {
    private static WeakReference<Context> mContext;
    private static FinanceCheckSum object;
    private boolean flushFeesHeaderTable = false;
    private boolean flushFeesDetailTable = false;
    private boolean flushCashDeposit = false;
    private boolean flushStudentTable = false;
    private boolean flushSchoolClassTable = false;



    private FinanceCheckSum(WeakReference<Context> mContext) {
        this.mContext = mContext;
    }

    public static FinanceCheckSum Instance(WeakReference<Context> context) {
        if (object == null || mContext == null)
            object = new FinanceCheckSum(context);

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
                    .getBoolean(AppConstants.KEY_CHECKSUM, false);
        }catch (Exception e){
            e.printStackTrace();
        }

        boolean NewLogin = AppModel.getInstance().readBooleanFromSharedPreferences(mContext.get(),
                AppConstants.NewLogin, true);

        if (NewLogin){
            return false;
        }

        if (!checkSumApplicable) {
            long elapsedTime = Calendar.getInstance().getTimeInMillis() - lastRanTime();
            long elapsedHours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
            return elapsedHours > 23;
        }
        return true;
    }

    public void setCheckSumApplicable(boolean checkSum) {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putBoolean(AppConstants.KEY_CHECKSUM, checkSum).apply();
    }

    private long lastRanTime() {
        return mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).getLong(AppConstants.KEY_RUN_TIME, 0);

    }

    public void setCurrentTimeAsRunTime() {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putLong(AppConstants.KEY_RUN_TIME, Calendar.getInstance().getTimeInMillis()).apply();
    }

    public void setCurrentTimeAsRunTime(long timeInMilliSecond) {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putLong(AppConstants.KEY_RUN_TIME, timeInMilliSecond).apply();
    }

    public boolean isCheckSumSuccessfull() {
        return mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).getBoolean(AppConstants.KEY_CHECKSUM_RAN, false);
    }

    public void checkSumSuccessful(boolean isSuccessfull) {
        mContext.get().getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit().putBoolean(AppConstants.KEY_CHECKSUM_RAN, isSuccessfull).apply();
    }

    private boolean checkPendingRecord() {
        int feesPendingCount = FeesCollection.getInstance(mContext.get()).getAllFeesHeaderForUpload().size();
        int cashDepositPendingCount = CashDeposit.getInstance(mContext.get()).getAllCashDepositsForUpload().size();
        return feesPendingCount == 0 && cashDepositPendingCount == 0;
    }

    private void logger(String message) {
        AppModel.getInstance().appendErrorLog(mContext.get(), message);
    }

    private void flushTables(int id) {

        if (flushFeesHeaderTable || flushFeesDetailTable || flushCashDeposit) {
            FeesCollection.getInstance(mContext.get()).deleteSysIDRecords(id);
            CashDeposit.getInstance(mContext.get()).deleteSysIDRecords(id);
        }

//        if (flushFeesHeaderTable) {
//            FeesCollection.getInstance(mContext.get()).deleteSysIDRecords(id);
//        }
//        if (flushFeesDetailTable) {
//            FeesCollection.getInstance(mContext.get()).deleteSysIDRecords(id);
////            FeesCollection.getInstance(mContext.get()).deleteFeeDetailsRecords(id);
//        }
//        if (flushCashDeposit) {
//            CashDeposit.getInstance(mContext.get()).deleteSysIDRecords(id);
//        }

        if (flushStudentTable) {
            DatabaseHelper.getInstance(mContext.get()).deleteStudentRecords(id);
        }

        if (flushSchoolClassTable) {
            DatabaseHelper.getInstance(mContext.get()).deleteSchoolClassRecords(id);
        }

    /*    if (flushSubheadLimitsMonthlyTable) {
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
        }*/

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
            Call<CheckSumModel> call = apiInterface.getCheckSum(model.getId(), model.getAcademic_Session_Id(), "Bearer " + AppModel.getInstance().getToken(mContext.get()));
            try {
                CheckSumModel localCheckSum = getLocalCheckSumModel(model.getId());
                Response<CheckSumModel> response = call.execute();
                if (response.isSuccessful()) {
                    logger("Checksum Started...");
                    CheckSumModel BOCheckSum = response.body();

                    List<String> allowedModules = null;
                    if (model.getAllowedModule_App() != null){
                        allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                    }

                    assert localCheckSum != null;

                    //Student checksum
                    if (localCheckSum.getStudentCount() != BOCheckSum.getStudentCount()) {
                        logger("Student Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getStudentCount()
                                + " Server Records: " + BOCheckSum.getStudentCount() + " School ID: " + model.getId() + " starting flush");
                        flushStudentTable = true;
                    } else {
                        logger("Student Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getStudentCount()
                                + " Server Records: " + BOCheckSum.getStudentCount() + " School ID: " + model.getId());
                        flushStudentTable = false;
                    }

                    // School class checksum
                    if (localCheckSum.getSchoolClassCount() != BOCheckSum.getSchoolClassCount()) {
                        logger("School class Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getSchoolClassCount()
                                + " Server Records: " + BOCheckSum.getSchoolClassCount() + " School ID: " + model.getId() + " starting flush");
                        flushSchoolClassTable = true;
                    } else {
                        logger("School class Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getSchoolClassCount()
                                + " Server Records: " + BOCheckSum.getSchoolClassCount() + " School ID: " + model.getId());
                        flushSchoolClassTable = false;
                    }

                    //Feesheader ,feesdetail and cash deposit checksum
                    if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue) && DataSync.isFinanceSyncSuccessful) {
                        if (localCheckSum.getFeesHeaderCount() != BOCheckSum.getFeesHeaderCount()) {
                            logger("Fees Header Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getFeesHeaderCount()
                                    + " Server Records: " + BOCheckSum.getFeesHeaderCount() + " School ID: " + model.getId() + " starting flush");
                            flushFeesHeaderTable = true;
                        } else {
                            logger("Fees Header Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getFeesHeaderCount()
                                    + " Server Records: " + BOCheckSum.getFeesHeaderCount() + " School ID: " + model.getId());
                            flushFeesHeaderTable = false;
                        }

                        if (localCheckSum.getFeesDetailCount() != BOCheckSum.getFeesDetailCount()) {
                            logger("Fees Detail Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getFeesDetailCount()
                                    + " Server Records: " + BOCheckSum.getFeesDetailCount() + " School ID: " + model.getId() + " starting flush");
                            flushFeesDetailTable = true;
                        } else {
                            logger("Fees Detail Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getFeesDetailCount()
                                    + " Server Records: " + BOCheckSum.getFeesDetailCount() + " School ID: " + model.getId());
                            flushFeesDetailTable = false;
                        }

                        if (localCheckSum.getFeesDepositCount() != BOCheckSum.getFeesDepositCount()) {
                            logger("Cash Deposit Checksum Failed " + getActionPerformed() + " Local records: " + localCheckSum.getFeesDepositCount()
                                    + " Server Records: " + BOCheckSum.getFeesDepositCount() + " School ID: " + model.getId() + " starting flush");
                            flushCashDeposit = true;
                        } else {
                            logger("Cash Deposit Checksum Passed " + getActionPerformed() + " Local records: " + localCheckSum.getFeesDepositCount()
                                    + " Server Records: " + BOCheckSum.getFeesDepositCount() + " School ID: " + model.getId());
                            flushCashDeposit = false;
                        }

                    }
                    //Expense Check Sum
                    //Expensechecksum(model);

                    if (flushFeesHeaderTable || flushFeesDetailTable || flushCashDeposit || flushStudentTable || flushSchoolClassTable) {
//                        checkSumSuccessful(false);
                        checkSumFailed++;
                    }

                   /* if (flushSchoolPettyCashMonthlyLimitsTable || flushSubheadLimitsMonthlyTable || flushAmountClosingTable || flushTransactionsTable) {
//                        checkSumSuccessful(false);
                        checkSumFailed++;
                    }*/
//                    else {
//                        checkSumSuccessful(true);
//                    }

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
        } else if (FeesCollection.getInstance(context).getClassSectionForFeeEntryBySchoolId(AppModel.getInstance().getAllUserSchoolsForFinance(context)).size() != 0) {
            i++;
            if (showFailureMessage)
                Toast.makeText(context, "Enter fee entry first", Toast.LENGTH_SHORT).show();
        } else if (FeesCollection.getInstance(context).getUnuploadedCountFeeEntry(AppModel.getInstance().getAllUserSchoolsForFinance(context)) != 0) {
            i++;
            if (showFailureMessage)
                Toast.makeText(context, "Enter fee entry first", Toast.LENGTH_SHORT).show();
        }

        try {
//            Viewer is not allowed to edit finance
            int roleId = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();
            if (roleId == AppConstants.roleId_103_V || roleId == AppConstants.roleId_7_AEM){
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return i == 0;
    }

    public CheckSumModel getServerCheckSum(int schoolId, int academicSession_id){
        ApiInterface apiInterface = ApiClient.getClient(mContext.get()).create(ApiInterface.class);
        Call<CheckSumModel> call = apiInterface.getCheckSum(schoolId, academicSession_id, "Bearer " + AppModel.getInstance().getToken(mContext.get()));
        CheckSumModel checkSumModel = null;
        try {
            Response<CheckSumModel> response = call.execute();
            if (response.isSuccessful()) {
//                logger("Checksum Started...");
                checkSumModel = response.body();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return checkSumModel;
    }
}
