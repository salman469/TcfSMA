package com.tcf.sma.Scheduler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.KeepAliveModel;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
import com.tcf.sma.utils.MyLocation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeepAliveService extends JobService implements MyLocation.LocationResult {
    MyLocation loc;
    KeepAliveModel keepAliveModel;

    @Override
    public boolean onStartJob(JobParameters job) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (loc == null) {
                loc = new MyLocation(this);
                loc.getLocation(this, this);
            }
        } else {
            sendPingToServer(null);
        }
        Log.d("Scheduler", "running");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        loc = null;
        Log.d("Scheduler", "stopped");
        return false;
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
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                keepAliveModel.setAppVersion(version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            try {

                TelephonyManager tm = (TelephonyManager)
                        getSystemService(Context.TELEPHONY_SERVICE);
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

            keepAliveModel.setDeviceId(AppModel.getInstance().getDeviceId(this));

            int userID = DatabaseHelper.getInstance(KeepAliveService.this).getCurrentLoggedInUser().getId();

            if (AppModel.getInstance().getToken(this) == null)
                keepAliveModel.setUserId(0);
            else
                keepAliveModel.setUserId(userID);

            AppModel.getInstance().appendLog(this, "\nKeep Alive");
            ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.pingServer(keepAliveModel);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        AppModel.getInstance().appendLog(KeepAliveService.this, "\nKeep Alive:"
                                + response.code() + "");

                        Log.d("KeepAlive", response.code() + "");
                    } else {
                        AppModel.getInstance().appendLog(KeepAliveService.this, "\nKeep Alive:"
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
