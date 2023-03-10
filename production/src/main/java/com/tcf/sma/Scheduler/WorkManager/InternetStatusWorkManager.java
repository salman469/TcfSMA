package com.tcf.sma.Scheduler.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.android.gms.common.util.Strings;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.speedchecker.android.sdk.Public.SpeedTestListener;
import com.speedchecker.android.sdk.Public.SpeedTestResult;
import com.speedchecker.android.sdk.SpeedcheckerSDK;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DbTables.NetworkConnection.NetworkConnectionHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.NetwrokConnection.NetworkConnectionInfo;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
import com.tcf.sma.utils.MyLocation;
import com.tcf.sma.utils.NetworkConnectivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class InternetStatusWorkManager extends ListenableWorker implements MyLocation.LocationResult, SpeedTestListener {
    MyLocation loc;
    private Context context;
    Location location;
    int retryCount = 0;

    public InternetStatusWorkManager(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);

        this.context = appContext;
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            // Your method can call set() or setException() on the
            // Completer to signal completion
            startSomeAsyncStuff(completer);

            // This value is used only for debug purposes: it will be used
            // in toString() of returned future or error cases.
            return "startSomeAsyncStuff";
        });
    }

    private void startSomeAsyncStuff(CallbackToFutureAdapter.Completer<Result> completer) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (loc == null) {
                    loc = new MyLocation(context);
                    loc.getLocation(context, this);
                }
            }
            /*else {
                uploadInternetSpeedStatus(null);
            }*/
            Log.d("InternetScheduler", "running");
            AppModel.getInstance().appendLog(context, "InternetScheduler running.....");

            completer.set(Result.success());

        } catch (Exception e) {
            completer.set(Result.failure());
            e.printStackTrace();
            Log.e("InternetScheduler", "InternetScheduler stopped: " + e.getMessage());
        }
    }

//    @NonNull
//    @Override
//    public Result doWork() {
//        try {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                if (loc == null) {
//                    if (Looper.myLooper() == null) {
//                        Looper.prepare();
//                    }
//                    loc = new MyLocation(context);
//                    loc.getLocation(context, this);
//                }
//            } else {
//                uploadInternetSpeedStatus(null);
//            }
//            Log.d("InternetScheduler", "running");
//            AppModel.getInstance().appendLog(context, "InternetScheduler running.....");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("InternetScheduler", "InternetScheduler stopped: " + e.getMessage());
//        }
//        return Result.success();
//    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("InternetScheduler", "stopped");
        AppModel.getInstance().appendLog(context, "InternetScheduler stoped");
    }

    @Override
    public void gotLocation(Location location) {
        if (location != null) {
            Log.d("Lat", location.getLatitude() + "");
            Log.d("Long", location.getLongitude() + "");


            SpeedcheckerSDK.SpeedTest.setOnSpeedTestListener(this);
            SpeedcheckerSDK.SpeedTest.startTest(context);
            this.location = location;

//            uploadInternetSpeedStatus(location);
        }
        //todo hit the server and send uploadInternetSpeedStatus()

    }

    public void uploadInternetSpeedStatus(Location loc, SpeedTestResult speedTestResult) {
        boolean isStartInternetStatusTimer = AppModel.getInstance().isCurrentTimeBetween("07:00:00", "22:00:00"); //7am to 10pm
        //Taimur asked to remove this validation and allow throughout the day
        isStartInternetStatusTimer = true;
        if (isStartInternetStatusTimer) {
            if (AppModel.getInstance().isConnectedToInternet(context)) {
                if (speedTestResult != null && (speedTestResult.getDownloadSpeed() > 1000f || speedTestResult.getUploadSpeed() > 1000f)) {
//                    FirebaseCrashlytics.getInstance().setCustomKey("DownloadSpeed", speedTestResult.getDownloadSpeed());
//                    FirebaseCrashlytics.getInstance().setCustomKey("UploadSpeed", speedTestResult.getUploadSpeed());
//                    FirebaseCrashlytics.getInstance().setCustomKey("SpeedTestResult", speedTestResult.toString());

                    if (retryCount > 3)
                        return;
                    else {
                        SpeedcheckerSDK.SpeedTest.setOnSpeedTestListener(this);
                        SpeedcheckerSDK.SpeedTest.startTest(context);
                        retryCount++;
                    }
                }
                /*String image1 = "TCF-logo-lg.JPG";
                String image2 = "TCF-logo-md.JPG";
                String image3 = "TCF-logo-sm.JPG";
                List<String> images = new ArrayList<>();
                images.add(image1);
                images.add(image2);
                images.add(image3);

                List<String> urls = new ArrayList<>();
                for (String img : images) {
                    urls.add(AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + "logos/" + img);
                }*/

                try {
                    NetworkConnectionInfo connectionInfo = new NetworkConnectionInfo();
/*

                    //Downloads -this method should be called first before upload for avg latency value
                    Thread AverageDownloadThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DataSync.getInstance(context).setAverageDownloadTimeAndLatecy(connectionInfo, urls);
                        }
                    });
                    AverageDownloadThread.start();
                    AverageDownloadThread.join();

                    //Uploads
                    Thread AverageUploadThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DataSync.getInstance(context).setAverageUploadTimeAndLatency(connectionInfo, images);
                        }
                    });
                    AverageUploadThread.start();
                    AverageUploadThread.join();
*/


                    @SuppressLint("MissingPermission") Thread uploadInternetSpeedStatus = new Thread(() -> {

                        connectionInfo.setDeviceId(AppModel.getInstance().getDeviceId(context));
                        connectionInfo.setConnectionType(NetworkConnectivity.getInstance().getNetworkType(context));
                        if (Strings.isEmptyOrWhitespace(connectionInfo.getConnectionType()) || connectionInfo.getConnectionType().equalsIgnoreCase("unknown"))
                            connectionInfo.setConnectionType(speedTestResult.getConnectionTypeHuman());
                        if (loc != null) {
                            double latitude = loc.getLatitude();
                            double longitude = loc.getLongitude();
                            connectionInfo.setLatitude(latitude);
                            connectionInfo.setLongitude(longitude);

                        }
                        if (speedTestResult != null) {
                            if (speedTestResult.hasDownloadSpeed())
                                connectionInfo.setDownloadSpeed(speedTestResult.getDownloadSpeed());
                            else
                                connectionInfo.setDownloadSpeed(0);
                            if (speedTestResult.hasUploadSpeed())
                                connectionInfo.setUploadSpeed(speedTestResult.getUploadSpeed());
                            else
                                connectionInfo.setUploadSpeed(0);
                            if (speedTestResult.hasPing())
                                connectionInfo.setLatency(speedTestResult.getPing());
                            else
                                connectionInfo.setLatency(0);
                        } else {
                            connectionInfo.setDownloadSpeed(0);
                            connectionInfo.setUploadSpeed(0);
                            connectionInfo.setLatency(0);
                        }


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
                                                connectionInfo.setSIM1(sim1 != null ? sim1 :
                                                        NetworkConnectivity.getInstance().getTelephonyManager(context).getLine1Number());
//                                                connectionInfo.setMobileOperator((String) si.get(0).getCarrierName());
                                                connectionInfo.setSIM2("");
                                            } else if (si.size() == 2) {
                                                connectionInfo.setSIM1(sim1 != null ? sim1 :
                                                        NetworkConnectivity.getInstance().getTelephonyManager(context).getLine1Number());
                                                connectionInfo.setSIM2(si.get(1).getNumber());
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

                                    connectionInfo.setSIM1(telNumber);
                                }

                                String[] IMEI = NetworkConnectivity.getInstance().getIMEINumber(context).split(",");

                                if (IMEI.length == 1 && !Strings.isEmptyOrWhitespace(IMEI[0])) {
                                    connectionInfo.setIMEI(IMEI[0]);
                                } else if (IMEI.length == 2 && !Strings.isEmptyOrWhitespace(IMEI[0]) && !Strings.isEmptyOrWhitespace(IMEI[1])) {
                                    connectionInfo.setIMEI(IMEI[0]);
                                    connectionInfo.setIMEI2(IMEI[1]);
                                }
                            }
                        }

//                        if (connectionInfo.getMobileOperator() == null || connectionInfo.getMobileOperator().isEmpty()) {
//                            connectionInfo.setMobileOperator(NetworkConnectivity.getInstance().getNetworkOperatorName(context));
//                        }

                        String mobileOperator = NetworkConnectivity.getInstance().getDataSimOperator(context);
                        String manufacturerModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;

                        connectionInfo.setMobileOperator(mobileOperator);
                        connectionInfo.setManufacturerModel(manufacturerModel);
                        connectionInfo.setCreatedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        connectionInfo.setAppVersion(AppModel.getInstance().getAppVersionWithBuildNo(context));
                        connectionInfo.setBatteryStats(AppModel.getInstance().getBatteryPercentage(context));

                        String token = "Bearer " + AppModel.getInstance().getToken(context);

                        try {
                            List<NetworkConnectionInfo> networkConnectionInfoList = NetworkConnectionHelperClass.getInstance(context).getAllNetworkInfoDataForUpload();
                            networkConnectionInfoList.add(connectionInfo);

                            for (NetworkConnectionInfo model : networkConnectionInfoList) {

                                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                                Call<ResponseBody> call = apiInterface.uploadConnectionInfoResult(model, token);
                                final Response<ResponseBody> response = call.execute();

                                if (response.isSuccessful()) {
                                    AppModel.getInstance().appendLog(context, "NetworkConnectionInfo uploaded successfully response code:" + response.code());
                                    NetworkConnectionHelperClass.getInstance(context).deleteNetworkConnectionInfo(model);
                                } else {
                                    JSONObject res = null;
                                    try {
                                        res = !response.errorBody().string().equals("null") &&
                                                !response.errorBody().string().equals("") ?
                                                new JSONObject(response.errorBody().string()) : new JSONObject();

                                        String msg = res.has("Message") ?
                                                res.getString("Message") : "";

                                        AppModel.getInstance().appendErrorLog(context, "Error Uploading NetworkConnectionInfo response code = " + response.code()
                                                + "Error message:" + msg);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        AppModel.getInstance().appendErrorLog(context, "Error Uploading NetworkConnectionInfo response code:" + response.code()
                                                + " IO Error:" + e.getMessage());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        AppModel.getInstance().appendErrorLog(context, "Error Uploading NetworkConnectionInfo response code:" + response.code()
                                                + " JSON Error:" + e.getMessage());
                                    }

                                }
                            }

                        } catch (Exception e) {
                            NetworkConnectionHelperClass.getInstance(context).addNetworkConnectionInfo(connectionInfo);
                            AppModel.getInstance().appendErrorLog(context, "Exception in uploadInternetSpeedStatus method Error:" + e.getMessage());
                            e.printStackTrace();

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

    @Override
    public void onTestStarted() {

    }

    @Override
    public void onFetchServerFailed() {

    }

    @Override
    public void onFindingBestServerStarted() {

    }

    @Override
    public void onTestFinished(SpeedTestResult speedTestResult) {
        try {
            SpeedcheckerSDK.SpeedTest.setOnSpeedTestListener(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uploadInternetSpeedStatus(location, speedTestResult);

    }

    @Override
    public void onPingStarted() {

    }

    @Override
    public void onPingFinished(int i, int i1) {

    }

    @Override
    public void onDownloadTestStarted() {

    }

    @Override
    public void onDownloadTestProgress(int i, double v, double v1) {

    }

    @Override
    public void onDownloadTestFinished(double v) {

    }

    @Override
    public void onUploadTestStarted() {

    }

    @Override
    public void onUploadTestProgress(int i, double v, double v1) {

    }

    @Override
    public void onUploadTestFinished(double v) {

    }

    @Override
    public void onTestWarning(String s) {

    }

    @Override
    public void onTestFatalError(String s) {

    }

    @Override
    public void onTestInterrupted(String s) {

    }
}
