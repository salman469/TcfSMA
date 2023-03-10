package com.tcf.sma.SyncClasses;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusMasterModel;
import com.tcf.sma.utils.NetworkConnectivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zubair Soomro on 12/26/2016.
 */

public class SyncUtils {
    public static final String syncFinishedIntentFilter = "sync_adapter_finished";
    private static final long SYNC_FREQUENCY = 60 * 120;  //two hour (in seconds)
//    private static final long SYNC_FREQUENCY = 300;  //5 minutes for testing (in seconds)
    private static final String CONTENT_AUTHORITY = AppConstants.CONTENT_AUTHORITY;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";

    /**
     * Create an entry for this application in the system account list, if it isn't already there.
     *
     * @param context Context
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void CreateSyncAccount(Context context) {
        boolean newAccount = false;
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

        // Create account, if it's missing. (Either first run, or user has deleted account.)
        Account account = GenericAccountService.GetAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(account, null, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(
                    account, CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            newAccount = true;
        }

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (newAccount || !setupComplete) {
            Bundle b = new Bundle();
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            int syncType = 0;
            if (DatabaseHelper.getInstance(context).getStudentTableCount() > 0)
                syncType = SyncProgressHelperClass.SYNC_TYPE_BAU_SYNC_ID;
            else
                syncType = SyncProgressHelperClass.SYNC_TYPE_FIRST_SYNC_ID;
            TriggerRefresh(context, b, syncType);
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).apply();
        }
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     * <p>
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public static void TriggerRefresh(Context context, Bundle b, int syncType) {
        if (ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY) ||
                ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
            Log.i("ContentResolver", "SyncPending, canceling");
            ContentResolver.cancelSync(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY);
        }

        getNetworkConnectionInfo(context, syncType, b, true);
    }


    public static void getNetworkConnectionInfo(Context context, int syncType, Bundle b, boolean startSync) {

        if (AppModel.getInstance().isConnectedToInternet(context)) {

            if (SyncProgressHelperClass.getInstance(context).getLastMasterRow() > 0)
                SyncProgressHelperClass.getInstance(context).deleteLastAppSyncMasterRecord((int) SyncProgressHelperClass.getInstance(context).getLastMasterRow());

            AppSyncStatusMasterModel masterModel = new AppSyncStatusMasterModel();

            masterModel.setSyncTypeId(syncType);
//            masterModel.setUserId(DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            masterModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            try {
                @SuppressLint("MissingPermission") Thread uploadInternetSpeedStatus = new Thread(() -> {

                    masterModel.setDeviceId(AppModel.getInstance().getDeviceId(context));
                    masterModel.setConnectionType(NetworkConnectivity.getInstance().getNetworkType(context));

                    List<SubscriptionInfo> si = new ArrayList<>();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                                == PackageManager.PERMISSION_GRANTED) {

                            List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                            if (subscriptionInfos != null) {
                                for (int i = 0; i < subscriptionInfos.size(); i++) {
                                    SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(i);
                                    si.add(lsuSubscriptionInfo);
//                    + " " +
//                            "Network name : "+ lsuSubscriptionInfo.getCarrierName()+ " " +
//                            "Country Iso "+ lsuSubscriptionInfo.getCountryIso());
                                }

                                try {
                                    if (si != null && si.size() > 0) {
                                        String sim1 = si.get(0).getNumber();
                                        if (si.size() == 1) {
                                            masterModel.setSIM1(sim1 != null ? sim1 :
                                                    NetworkConnectivity.getInstance().getTelephonyManager(context).getLine1Number());
//                                                connectionInfo.setMobileOperator((String) si.get(0).getCarrierName());
                                            masterModel.setSIM2("");
                                        } else if (si.size() == 2) {
                                            masterModel.setSIM1(sim1 != null ? sim1 :
                                                    NetworkConnectivity.getInstance().getTelephonyManager(context).getLine1Number());
                                            masterModel.setSIM2(si.get(1).getNumber());
//                                                connectionInfo.setMobileOperator(si.get(0).getCarrierName() + "," + si.get(1).getCarrierName());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String telNumber = "";
                                try {
                                    telNumber = NetworkConnectivity.getInstance().getTelephonyManager(context).getLine1Number();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                masterModel.setSIM1(telNumber);
                            }

                            String[] IMEI = NetworkConnectivity.getInstance().getIMEINumber(context).split(",");

                            if (IMEI.length == 1 && !Strings.isEmptyOrWhitespace(IMEI[0])) {
                                masterModel.setIMEI(IMEI[0]);
                            } else if (IMEI.length == 2 && !Strings.isEmptyOrWhitespace(IMEI[0]) && !Strings.isEmptyOrWhitespace(IMEI[1])) {
                                masterModel.setIMEI(IMEI[0]);
                                masterModel.setIMEI2(IMEI[1]);
                            }
                        }
                    }

//                        if (connectionInfo.getMobileOperator() == null || connectionInfo.getMobileOperator().isEmpty()) {
//                            connectionInfo.setMobileOperator(NetworkConnectivity.getInstance().getNetworkOperatorName(context));
//                        }

                    String mobileOperator = NetworkConnectivity.getInstance().getDataSimOperator(context);
                    String manufacturerModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;

                    masterModel.setMobileOperator(mobileOperator);
                    masterModel.setManufacturerModel(manufacturerModel);
                    masterModel.setAppVersion(AppModel.getInstance().getAppVersionWithBuildNo(context));
                    masterModel.setBatteryStats(AppModel.getInstance().getBatteryPercentage(context));
                    masterModel.setAndroidVersion(AppModel.getInstance().getAndroidVersionName());

                    SyncProgressHelperClass.getInstance(context).insertAppSyncMasterRecords(masterModel);


                    if (startSync) {
//        Bundle b = new Bundle();
//                        Disable sync backoff and ignore sync preferences.In other words...
//                        perform sync NOW !
//                                b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//                        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                        ContentResolver.requestSync(
                                GenericAccountService.GetAccount(),      // Sync account
                                AppConstants.CONTENT_AUTHORITY, // Content authority
                                b);                                      // Extras
                    }
                });

                uploadInternetSpeedStatus.start();
                uploadInternetSpeedStatus.join();

            } catch (Exception e) {
                e.printStackTrace();
                AppModel.getInstance().appendErrorLog(context, "In uploadInternetSpeedStatus() Exception: " + e.getLocalizedMessage());
            }
        }

    }


}

