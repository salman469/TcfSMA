package com.tcf.sma.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MyLocation {
    public Context locationActivity;
    public int PermissionCoarseLocation;
    public int PermissionFineLocation;
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(android.location.Location location) {
            if (location != null) {
                if (timer1 != null)
                    timer1.cancel();
                locationResult.gotLocation(location);
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerGps);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(android.location.Location location) {
            if (location != null) {
                if (timer1 != null)
                    timer1.cancel();
                locationResult.gotLocation(location);
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerNetwork);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public MyLocation(Context context) {
        this.locationActivity = context;
        PermissionCoarseLocation = ContextCompat.checkSelfPermission(locationActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        PermissionFineLocation = ContextCompat.checkSelfPermission(locationActivity, Manifest.permission.ACCESS_COARSE_LOCATION);

    }

    public boolean getLocation(Context context, LocationResult result) {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        locationActivity = context;
        try {
            if (PermissionCoarseLocation != PackageManager.PERMISSION_GRANTED && PermissionFineLocation != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        network_enabled = true;
        //don't start listeners if no provider is enabled
        if (!gps_enabled)
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        if (gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 500);
        return true;
    }

    public interface LocationResult {
        public void gotLocation(android.location.Location location);
    }

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {


            if (PermissionCoarseLocation != PackageManager.PERMISSION_GRANTED && PermissionFineLocation != PackageManager.PERMISSION_GRANTED) {

            } else {
                Location passive_loc = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (locationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && locationActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                }
                passive_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null ||
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null ||
                        lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null ) {
                    lm.removeUpdates(locationListenerGps);
                    lm.removeUpdates(locationListenerNetwork);
                }

                Location net_loc = null, gps_loc = null;


                if (network_enabled)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (locationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && locationActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (gps_enabled) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (locationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && locationActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (gps_loc == null && net_loc == null) {
                    if (passive_loc == null)
                        passive_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    locationResult.gotLocation(passive_loc);
                    return;
                }

                //if there are both values use the latest one
                if (gps_loc != null && net_loc != null) {
                    if (gps_loc.getTime() > net_loc.getTime())
                        locationResult.gotLocation(gps_loc);
                    else
                        locationResult.gotLocation(net_loc);
                    return;
                }

                if (gps_loc != null) {
                    locationResult.gotLocation(gps_loc);
                    return;
                }
                if (net_loc != null) {
                    locationResult.gotLocation(net_loc);
                    return;
                }
                locationResult.gotLocation(null);
            }
        }
    }
}
