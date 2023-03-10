package com.tcf.sma.Helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tcf.sma.R;

import java.util.List;

public class GpsLocationHelper {
    private static GpsLocationHelper instance = null;
    public static GpsLocationHelper getInstance(){
        if (instance == null)
            instance = new GpsLocationHelper();
        return instance;
    }

    protected static final String TAG = "LocationOnOff";
//    private GoogleApiClient googleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean isGpsOn(Context context){
        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(context);

//        // Todo Location Already on  ... end
//
//        if(!hasGPSDevice(MapActivity.this)){
//            Toast.makeText(MapActivity.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
//        }
    }

    public boolean isPermissionGranted(Activity activity){
        return  (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public void enableLoc(Context context) {

        if(isPermissionGranted((Activity) context)) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            ((Activity) context),
                                            LocationRequest.PRIORITY_HIGH_ACCURACY);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                            case LocationSettingsStatusCodes.API_NOT_CONNECTED:
                                new AlertDialog.Builder(context)
                                        .setTitle(R.string.title_location_permission)
                                        .setMessage(R.string.text_location_permission)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //Prompt the user once explanation has been shown
                                                Intent settingintent= new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                ((Activity) context).startActivity(settingintent);
                                            }
                                        })
                                        .create()
                                        .show();
                                break;
                        }
                    }
                }
            });
        }else{
            askPermission((Activity) context);
        }
    }

    public void askPermission(Activity activity){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    public boolean checkLocationPermission(Activity activity) {
        if (!isPermissionGranted(activity)) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                askPermission(activity);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                askPermission(activity);
            }
            return false;
        } else if(!isGpsOn(activity)){
            enableLoc(activity);
            return false;

        }else {
            return true;
        }
    }

    //    private void enableLoc(Context context) {
//
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(context)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                        @Override
//                        public void onConnected(@Nullable Bundle bundle) {
//
//                        }
//
//                        @Override
//                        public void onConnectionSuspended(int i) {
//                            googleApiClient.connect();
//                        }
//                    })
//                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                        @Override
//                        public void onConnectionFailed(ConnectionResult connectionResult) {
//
//                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
//                        }
//                    }).build();
//            googleApiClient.connect();
//        }
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(30 * 1000);
//        locationRequest.setFastestInterval(5 * 1000);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(context, REQUEST_LOCATION);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                }
//            }
//        });
//    }
}
