package com.tcf.sma.Scheduler.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.app.ActivityCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.GpsLocationHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.KeepAliveModel;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
import com.tcf.sma.Scheduler.KeepAliveService;
import com.tcf.sma.utils.MyLocation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewKeepAliveService extends ListenableWorker implements MyLocation.LocationResult  {
    MyLocation loc;
    KeepAliveModel keepAliveModel;
    private Context mContext;

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public NewKeepAliveService(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.mContext = appContext;
    }


    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            // Your method can call set() or setException() on the
            // Completer to signal completion
            startScheduler(completer);

            // This value is used only for debug purposes: it will be used
            // in toString() of returned future or error cases.
            return "startScheduler";
        });
    }

    private void startScheduler(CallbackToFutureAdapter.Completer<Result> completer) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (loc == null) {
                loc = new MyLocation(mContext);
                loc.getLocation(mContext, this);
            }
        } else {
            sendPingToServer(null);
        }
        Log.d("Scheduler", "running");
    }

    @Override
    public void onStopped() {
        loc = null;
        Log.d("Scheduler", "stopped");
    }

    @Override
    public void gotLocation(Location location) {
        if (location != null) {
            Log.d("Lat", location.getLatitude() + "");
            Log.d("Long", location.getLongitude() + "");

        }
        //todo hit the server and send keep alive

        sendPingToServer(location);
    }

    @SuppressLint({"MissingPermission", "HardwareIds", "NewApi"})
    private void sendPingToServer(Location loc) {

        try {
            keepAliveModel = new KeepAliveModel();
            keepAliveModel.setApp_Timestamp(AppModel.getInstance().getDateTime());
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                String version = pInfo.versionName;
                keepAliveModel.setAppVersion(version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            try {

                TelephonyManager tm = (TelephonyManager)
                        mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String telNumber = "";
                try {
                    telNumber = tm.getLine1Number();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                keepAliveModel.setPhoneNumber(telNumber);

                String IMEI = "";
                String IMEI2 = "";
                try {
                    IMEI = tm.getDeviceId(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (IMEI.isEmpty()) {
                    try {
                        IMEI = tm.getDeviceId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    IMEI2 = tm.getDeviceId(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                keepAliveModel.setIMEI(IMEI);
                keepAliveModel.setIMEI2(IMEI2);


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (loc != null) {
                keepAliveModel.setLatitude(loc.getLatitude());
                keepAliveModel.setLongitude(loc.getLongitude());
            }

            keepAliveModel.setDeviceId(AppModel.getInstance().getDeviceId(mContext));

            int userID = DatabaseHelper.getInstance(mContext).getCurrentLoggedInUser().getId();

            if (AppModel.getInstance().getToken(mContext) == null)
                keepAliveModel.setUserId(0);
            else
                keepAliveModel.setUserId(userID);

            AppModel.getInstance().appendLog(mContext, "\nKeep Alive");
            ApiInterface apiInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.pingServer(keepAliveModel);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        AppModel.getInstance().appendLog(mContext, "\nKeep Alive:"
                                + response.code() + "");

                        Log.d("KeepAlive", response.code() + "");
                    } else {
                        AppModel.getInstance().appendLog(mContext, "\nKeep Alive:"
                                + response.code() + "");

                        Log.d("KeepAlive", response.code() + "");
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
