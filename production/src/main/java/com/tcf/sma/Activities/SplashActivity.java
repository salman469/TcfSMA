package com.tcf.sma.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.speedchecker.android.sdk.SpeedcheckerSDK;
import com.tcf.sma.Activities.FeesCollection.CashReceived.CashReceiptActivity;
import com.tcf.sma.Helpers.MarshMallowPermission;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    long delayTime = 3000;
    boolean test = false;

    MarshMallowPermission permission = new MarshMallowPermission(this);

    int noOfTries = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Thankyou!")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                    }).create().show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("We need external storage permission for further processing. Thankyou!")
                    .setPositiveButton("Request permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (noOfTries == 0) {
                                permission.requestPermissionForExternalStorage();
                                noOfTries++;
                            }else{
                                Intent settingintent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                settingintent.setData(Uri.fromParts("package",SplashActivity.this.getPackageName(), null));
                                startActivity(settingintent);
                                noOfTries = 0;
                            }
                        }
                    }).setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).create().show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Status", "Splash");

        AppModel.getInstance().setFullScreenActivity(this);
        setContentView(R.layout.activity_splash);


        SpeedcheckerSDK.init(this);

        if (!permission.checkPermissionForExternalStorage()) {
            permission.requestPermissionForExternalStorage();
        } else {
            work();
        }

        AppModel.getInstance().setCustomDatabaseFiles(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Splash", "onRestart");

        if (!permission.checkPermissionForExternalStorage()) {
            permission.requestPermissionForExternalStorage();
        } else {
            work();
        }
    }

    private void work() {
        //todo bypassing sd card check have to revert back when the time comes
        if (true) {
            if (AppModel.getInstance().getToken(this) == null) {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        //Call intent after the splash delay time
                        if (test) {
                            startActivity(new Intent(SplashActivity.this, CashReceiptActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, delayTime);
            } else {
                String state = getSharedPreferences(AppConstants.TCF_SHARED_PREF, MODE_PRIVATE).getString(AppConstants.KEY_STATE, AppConstants.VALUE_DASHBOARD);
                if (state != null) {
                    if (state.equals(AppConstants.VALUE_SCHOOL_SELECT)) {
                        startActivity(new Intent(SplashActivity.this, School_list_Activity.class));
                        finish();
                    } else if (state.equals(AppConstants.VALUE_DASHBOARD)) {
                        startActivity(new Intent(SplashActivity.this, NewDashboardActivity.class));
                        finish();
                    }
                }

            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("SD card isn't detected, insert SD card and try again.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setCancelable(false).create().show();
        }
    }

    public boolean externalMemoryAvailable() {
        File[] storages = ContextCompat.getExternalFilesDirs(this, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null)
            return true;
        else
            return false;
    }
}
