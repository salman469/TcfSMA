package com.tcf.sma.Survey.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.tcf.sma.R;
import com.tcf.sma.Survey.httpServices.APIServiceManager;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.model.DistrictCoordinates;
import com.tcf.sma.Survey.model.GPSData;
import com.tcf.sma.Survey.model.SurveyAppModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.legacy.content.WakefulBroadcastReceiver;


public class TestReceiver extends WakefulBroadcastReceiver {

    public static Context applicationContext;
    public static LocationManager locManager;
    public static LocationListener locListener;
    private static double lat, lang;
    private static long lastSavedTime;
    private static int previousActivity = 0;
    private static int previousActivityAccuracy = 0;
    ArrayList<GPSData> gpsData;
    DataHandler dbData;
    String GPSDataString;
    User user;
    BroadcastReceiver broadcastReceiver;
    Date currentTime;
    private int minTimeToSave = 5;

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("GPS Logger", "Wakeful class Called");

        SurveyAppModel.getInstance().infoLog.addLog("GPSlogger Started", "Backgroung Class Called",context);

        if (!SurveyAppModel.getInstance().isGPSDataSyncing) {

            try {
                applicationContext = context;

                System.out.println(SurveyAppModel.getInstance().getSelectedProject(applicationContext));

                dbData = new DataHandler(applicationContext);

                if (dbData.getUser().getStatus().equals("yes")) {

                    if (locManager != null) {
                        System.out.println("Remove Location updates called");
                        locManager.removeUpdates(locListener);
                        locManager = null;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getPeriodicLocation();
                    }

                    if (isNetworkAvailable())
                        syncGPSData();
                    else
                        System.out.println("Network not Availavle");
                }

            } catch (Exception e) {
                SurveyAppModel.getInstance().errorLog.addLog(
                        "TestReciever.onRecieve.create_DataHandler_object",
                        e.getLocalizedMessage(),context);
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPeriodicLocation() {

        Log.d("Location Manager", "Getting Location");

        locListener = new LocationListener() {

            public void onLocationChanged(Location location) {

//				Log.i("Location Changed", "User Location Changed");
//				Log.d("New Location",
//						location.getLatitude() + "  " + location.getLongitude());

                currentTime = new Date();

//				Toast.makeText(applicationContext, "Location Changed", Toast.LENGTH_SHORT).show();

                try {
                    if (location != null) {

                        SurveyAppModel.getInstance().setCoordinates(applicationContext, location.getLatitude(), location.getLongitude());
                        SurveyAppModel.getInstance().currentLat = location.getLatitude();
                        SurveyAppModel.getInstance().currentLng = location.getLongitude();

                        if (!(location.getLatitude() == lat && location
                                .getLongitude() == lang))
                            if ((currentTime.getTime() - lastSavedTime) / 1000 > minTimeToSave) {
                                DataHandler dbHandler = new DataHandler(
                                        applicationContext);

                                dbHandler
                                        .saveGPSLocation(Double
                                                .toString(location
                                                        .getLatitude()), Double
                                                .toString(location
                                                        .getLongitude()), Float
                                                .toString(location
                                                        .getAccuracy()), Double
                                                .toString(location.getSpeed()));

                                lastSavedTime = currentTime.getTime();
                                lat = location.getLatitude();
                                lang = location.getLongitude();

//								Toast.makeText(applicationContext,
//										lat + " " + lang, Toast.LENGTH_SHORT)
//										.show();

                                System.out.println("Location Saved : " + lat + "," + lang);

                                SurveyAppModel.getInstance().infoLog
                                        .addLog("GPS Logger onLocationChanged",
                                                "Data Saved Locally",applicationContext);
                                dbHandler.close();
                            }
                    }

                } catch (Exception e) {
                    SurveyAppModel.getInstance().errorLog.addLog(
                            "TestReciever.onRecieve.onLocationChanged",
                            e.getLocalizedMessage(),applicationContext);
                    e.printStackTrace();
                }

            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("", "Provider Status: " + status);
            }
        };

        locManager = (LocationManager) applicationContext
                .getSystemService(Context.LOCATION_SERVICE);

        if (applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && applicationContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locListener);

    }

    private boolean isNetworkAvailable() {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            connectivityManager
                    .setNetworkPreference(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();

            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            SurveyAppModel.getInstance().errorLog.addLog(
                    "TestReciever Exception in isNetworkAvailable ", e.getLocalizedMessage(),applicationContext);
            return false;
        }
    }

    public void syncGPSData() {

        Log.d("Syncing GPS DATA", "Syncing...");

        gpsData = new ArrayList<GPSData>();

        // Adding all Gps data into an Json Arry and then Pass it to the api
        gpsData = dbData.getGPSData();

        String size = Integer.toString(gpsData.size());

//		Log.d("IMI TAG", "Found " + size + " records to be uploaded");

        SurveyAppModel.getInstance().infoLog.addLog("GPS Logger", "Found " + size + " record(s) to be uploaded",applicationContext);

        if (!gpsData.isEmpty()) {

//			JSONArray jarray = new JSONArray();
//			for (int i = 0; i < gpsData.size(); i++) {
//
//				if (gpsData.get(i) != null) {
//					JSONObject jobj = new JSONObject();
//
//					try {
//						jobj.put("latitude", gpsData.get(i).latitude);
//						jobj.put("longitude", gpsData.get(i).longitude);
//						jobj.put("saveDate", gpsData.get(i).saveDate);
//						jobj.put("accuracy", gpsData.get(i).accuracy);
//					} catch (JSONException e) {
//						 SurveyAppModel.getInstance().errorLog.addLog(
//						 "DashboardActivity.onSuccess.speedTest",
//						 e.getLocalizedMessage());
//						e.printStackTrace();
//					}
//					jarray.put(jobj);
//				}
//			}
//
//			final String GPSDataString = jarray.toString();

            AsyncTask<Object, Void, Boolean> task = new AsyncTask<Object, Void, Boolean>() {

                int responseCode = 0;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    SurveyAppModel.getInstance().isGPSDataSyncing = true;
                }


                @Override
                protected Boolean doInBackground(Object... arg0) {


                    gpsData = dbData.getGPSData();

                    String size = Integer.toString(gpsData.size());

                    Log.d("IMI TAG", "Found " + size + " records to be uploaded");

//					
//					if (!gpsData.isEmpty()) {

                    JSONArray jarray = new JSONArray();
                    for (int i = 0; i < gpsData.size(); i++) {

                        if (gpsData.get(i) != null) {
                            JSONObject jobj = new JSONObject();

                            try {
                                jobj.put("latitude", gpsData.get(i).latitude);
                                jobj.put("longitude", gpsData.get(i).longitude);
                                jobj.put("saveDate", gpsData.get(i).saveDate);
                                jobj.put("accuracy", gpsData.get(i).accuracy);
                            } catch (JSONException e) {
                                SurveyAppModel.getInstance().errorLog.addLog(
                                        "DashboardActivity.onSuccess.speedTest",
                                        e.getLocalizedMessage(),applicationContext);
                                e.printStackTrace();
                            }
                            jarray.put(jobj);
                        }
                    }

                    final String GPSDataString = jarray.toString();


                    DefaultHttpClient client = APIServiceManager
                            .getNewHttpClient();

                    String url = applicationContext.getString(R.string.SURVEY_BASE_URL)
                            + AppConstants.URL_POST_TRACK + "/"
                            + dbData.getUser().getUsername().toString();
                    Log.e("Sending Records on", url);

                    HttpPost httpPost = new HttpPost(url);

                    String str = GPSDataString.toString();
                    // String str1 =
                    // SurveyAppModel.getInstance().GPSDataString.toString();

                    try {
                        httpPost.setEntity(new StringEntity(str, "UTF8"));
                        httpPost.setHeader("Content-type", "application/json");

                        HttpResponse response = client.execute(httpPost);

                        Log.d("ServerResponse", response.toString());


                        responseCode = response.getStatusLine().getStatusCode();

//						System.out.println(response.getEntity().toString());

                        if (responseCode == 200 || responseCode == 204)
                            return true;
                        else
                            return false;

                    } catch (ClientProtocolException e) {

                        Log.e("IMI ERROR", "GPS data not uploaded");

                        SurveyAppModel.getInstance().errorLog
                                .addLog("TestReciever.syncGPSData Client Protocol Exception",
                                        e.getLocalizedMessage(),applicationContext);
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {

                        Log.e("IMI ERROR", "GPS data not uploaded");

                        SurveyAppModel.getInstance().errorLog.addLog(
                                "TestReciever.syncGPSData IOException",
                                e.getLocalizedMessage(),applicationContext);
                        e.printStackTrace();
                        return false;
                    } catch (Exception e) {

                        Log.e("IMI ERROR", "GPS data not uploaded");

                        SurveyAppModel.getInstance().errorLog.addLog(
                                "TestReciever.syncGPSData",
                                e.getLocalizedMessage(),applicationContext);
                        e.printStackTrace();
                        return false;
                    }

                    // return true;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    DataHandler dbData = new DataHandler(applicationContext);
                    String dir = SurveyAppModel.getInstance().getSurveyPath(applicationContext) + "Tracks/";    //Hardcoded Project ID
                    String path = SurveyAppModel.getInstance().getSurveyPath(applicationContext) + "Tracks/trk_" + dbData.getUser().getUsername() + "_" + SurveyAppModel.getInstance().getSelectedProject(applicationContext) + "_" + AppConstants.LANGUAGE.toString() + ".txt";
                    File file = new File(path);
                    Date dt = new Date();
                    long currentTime = dt.getTime();

                    try {
                        if (!file.exists()) {
                            createFile(SurveyAppModel.getInstance().getSelectedProject(applicationContext), "\"[]\"", dir, path);
                        }

                        if (file.exists()) {
                            if (result) {

                                String currentRecords = ",";
                                String tracks = readFile();

                                tracks = tracks.substring(0, tracks.length() - 1);
                                tracks = tracks.substring(0, tracks.length() - 1);

                                if (tracks.length() == 2) {
                                    currentRecords = "";
                                }

                                for (int i = 0; i < gpsData.size(); i++) {

                                    try {
                                        if (tracks != null) {
                                            JSONObject mainJson = new JSONObject();


                                            double x = Double.parseDouble(gpsData.get(i).latitude);
                                            double y = Double.parseDouble(gpsData.get(i).longitude);
                                            DistrictCoordinates dc = new DistrictCoordinates(x, y);

//									tm.setX(Double.parseDouble(gpsData.get(i).latitude));
//									tm.setY(Double.parseDouble(gpsData.get(i).longitude));
                                            SurveyAppModel.getInstance().tracks_list.add(dc);

                                            mainJson.put("id", currentTime);
                                            mainJson.put("x", Double.parseDouble(gpsData.get(i).latitude));
                                            mainJson.put("y", Double.parseDouble(gpsData.get(i).longitude));

                                            currentRecords = currentRecords + mainJson.toString() + ",";

//									System.out.println(mainJson.toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }


//							tracks = tracks+"]";
                                    //HARCODED Project ID 04/09/2015

                                }
                                currentRecords = currentRecords.substring(0, currentRecords.length() - 1);
                                currentRecords = currentRecords.replace("\"", "\\\"").toString();


                                tracks = tracks + currentRecords + "]\"";
                                System.out.println(currentRecords);
                                createFile(SurveyAppModel.getInstance().getSelectedProject(applicationContext), tracks, dir, path);    //HARCODED Project ID 04/09/2015

                                Log.d("TAG", "GPS data Uploded Sucessfully");
                                SurveyAppModel.getInstance().infoLog.addLog(
                                        "TestReciever.Uploading",
                                        "Uploaded Successfully",applicationContext);

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (responseCode == 200 || responseCode == 204) {
                        for (int i = 0; i < gpsData.size(); i++) {

                            try {
                                GPSData gpsDataRow = gpsData.get(i);
                                dbData.deleteGPSRow(gpsDataRow.id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    gpsData.clear();
                    SurveyAppModel.getInstance().isGPSDataSyncing = false;

                }

            };

            task.execute();
        } else {

            SurveyAppModel.getInstance().isGPSDataSyncing = false;
            Log.d("TAG(Manul sync)", "No GPS data in DB");
            // log.setText(log.getText()+ "\n" + "No GPS data in DB");

        }

    }


    public void createFile(String project_id, String json, String dir_folder, String path)            // VERSION 0.5.8 SHAZ 01/16/2015
    {
        FileWriter fWriter;

        File dir = new File(dir_folder);
        Log.i("TAG", "isExist : " + dir.exists());

        if (!dir.exists())
            dir.mkdirs();


        try {

            File survey_file = new File(path);
            Log.i("TAG", "isExist : " + survey_file.exists());

            fWriter = new FileWriter(path);
            fWriter.write(json);
            fWriter.flush();
            fWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String readFile() {            // VERSION 0.5.8 SHAZ 01/16/2015

        //HardCoded project ID
        String fname = SurveyAppModel.getInstance().getSurveyPath(applicationContext) + "Tracks/trk_" + dbData.getUser().getUsername() + "_" + SurveyAppModel.getInstance().getSelectedProject(applicationContext) + "_" + AppConstants.LANGUAGE.toString() + ".txt";

        File file = new File(fname);

        FileInputStream fin = null;
        String s = "";

        try {

            // create FileInputStream object
            fin = new FileInputStream(file);

            byte fileContent[] = new byte[(int) file.length()];

            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);
            //create string from byte array
            s = new String(fileContent);

            //System.out.println("File content: " + s);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }

        return s;
    }

}