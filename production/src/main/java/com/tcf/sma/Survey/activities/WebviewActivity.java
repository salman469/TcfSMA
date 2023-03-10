package com.tcf.sma.Survey.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.R;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.mediaServices.AudioLibrary;
import com.tcf.sma.Survey.mediaServices.ImageService;
import com.tcf.sma.Survey.mediaServices.MediaEventsHandler;
import com.tcf.sma.Survey.model.LocationData;
import com.tcf.sma.Survey.model.POI_Model;
import com.tcf.sma.Survey.model.SurveyAppModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WebviewActivity extends DrawerActivity implements MediaEventsHandler {

    static final String TAG = "WebViewActivity";

    public static int project_id;
    //private AudioRecorder recorder;		// VERSION 0.5.5 SHAZ 01/13/2015
    public static String recorded_file_name = "";
    public static String recorded_file_path = "";
    public static boolean isPreviewStarted;
    //private final String fileExtention;
    //private String currentRecordingFilename;
    static Context context;
    static LocationData location_data_obj_survey_form;
    private static long local_survey_db_id;                //Version 0.5.7 Shaz 01/15/2015
    private static String result;
    private static String values_json;                //Version 0.5.7 Shaz 01/15/2015
    public String cached_file;
    public String project_name;
    View view;
    SurveyDBHandler dbSurvey;
    WebView webView;
    String status = "complete";
    DataHandler dbData;
    //SharedPreferences sharedpreferences;
    Date date1;
    Date date2;
    long timeTaken;
    AudioLibrary lib;
    double currentlat, currentlong;
    LocationManager locManager;
    String imageFileName = "";
    String fieldID_image = "";
    String imageFilePath = "";
    String fieldID_recording = "";
    String user_name = "";
    String DeviceTimestampStart;
    String route, neighborhood, sublocality_level_1, locality, country, postal_code;
    int form_category_id, sub_cats, currentCellId;
    long timeSpentToGetLocation;
    Project projectInfo;
    private ProgressDialog loggin_dialog;
    private Camera mCamera;
    private boolean isRecordingStarted = false;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            byte[] pictureData = new byte[data.length];
            pictureData = data;

            System.out.println(mCamera.getParameters());


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());

            File mediaStorageDir = new File(SurveyAppModel.getInstance().getSurveyPath(WebviewActivity.this));

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                }
            }

            File pictureFile = new File(mediaStorageDir.getPath() + File.separator + "Hidden_" + timeStamp + ".jpg");

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(pictureData);
                fos.close();


            } catch (FileNotFoundException e) {
                Log.d("Error", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Error", "Error accessing file: " + e.getMessage());
            }

        }
    };
    private TextView heading;

    public static void set_location_obj(LocationData ld) {
        location_data_obj_survey_form = ld;
    }

//    public static String GET(String url) {
//        InputStream inputStream = null;
//
//        try {
//
//            HttpClient httpclient = new DefaultHttpClient();
//
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//            inputStream = httpResponse.getEntity().getContent();
//
//            if (inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        createFile(WebviewActivity.this);
//
////		System.out.println(result);
//
//        return result;
//    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null)
            result += line;

        bufferedReader.close();
        inputStream.close();
        return result;

    }

//    public static void createFile() {            // VERSION 0.5.8 SHAZ 01/16/2015
//
//        FileWriter fWriter;
//
//        File dir = new File(SurveyAppModel.getInstance().getSurveyPath(WebviewActivity.this) + "projects");
//        Log.i("TAG", "isExist : " + dir.exists());
//
//        if (!dir.exists())
//            dir.mkdirs();
//
//
//        try {
//            fWriter = new FileWriter(SurveyAppModel.getInstance().getSurveyPath(WebviewActivity.this) + "projects/prj_" + project_id + "_" + AppConstants.LANGUAGE + ".txt");
//            fWriter.write(result);
//            fWriter.flush();
//            fWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void createFile(int id, String cont) {            // VERSION 0.5.8 SHAZ 01/16/2015
//
//        FileWriter fWriter;
//
//        File dir = new File(Environment.getExternalStorageDirectory(), "DSDS/projects");
//        Log.i("TAG", "isExist : " + dir.exists());
//
//        if (!dir.exists())
//            dir.mkdirs();
//
//
//        try {
//            fWriter = new FileWriter("/sdcard/DSDS/projects/prj_" + id + ".txt");
//            fWriter.write(cont);
//            fWriter.flush();
//            fWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void deleteThisSurvey() {                //Version 0.5.7 Shaz 01/15/2015
//
//        if (values_json != null) {
//            if (values_json.equals("")) {
//                SurveyDBHandler dbSurvey = new SurveyDBHandler(context);
//                dbSurvey.deleteSurvey(local_survey_db_id);
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.web_view_activity);
        setToolbar("Survey", this, false);
//        setContentView(R.layout.web_view_activity);

//		getSurveyMediaPath();

        values_json = "";

        //System.out.println(AppConstants.LANGUAGE);
        context = getApplicationContext();

        date1 = new Date();

        dbData = new DataHandler(getApplicationContext());

        dbSurvey = new SurveyDBHandler(getApplicationContext());
//        heading = findViewById(R.id.project_heading);
//        heading.setText("Survey");
        webView = (WebView) this.findViewById(R.id.webview);

        if (getIntent().hasExtra("json_string")) {
            values_json = getIntent().getExtras().getString("json_string");

            // VERSION 0.5.6 SHAZ 01/14/2015
            //webView.loadUrl("javascript:updateValues("+25+")");		  //Version 0.5.6 01/14/2015

            parsePartialSurvey();
        }

        cached_file = getIntent().getExtras().getString("cached_file");
        local_survey_db_id = getIntent().getExtras().getLong("local_survey_db_id");
        project_id = getIntent().getExtras().getInt("project_id");
        project_name = getIntent().getExtras().getString("project_name");

        if (dbData.getUser().getUsername() != "") {
            user_name = dbData.getUser().getUsername();
        }

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        loggin_dialog = ProgressDialog.show(WebviewActivity.this, "Loading Survey",
                "Please wait...", true);

        File file = new File(SurveyAppModel.getInstance().getSurveyPath(this) + "projects/prj_" + project_id + "_" + AppConstants.LANGUAGE + ".txt");

        String alertMsg = "Synce not Completed, Please Complete the projects Sync";
        if (file.exists()) {
            float file_size = file.length() / 1024f;
//            Integer.parseInt(String.valueOf(file.length() / 1024));
            System.out.println(file_size);
            if (file_size > 0)
                loadingWebView();
            else {
                syncFailedAlert(alertMsg);
            }
        } else {
            syncFailedAlert(alertMsg);
        }

        boolean gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }

        if (getIntent().hasExtra("DeviceTimestampStart")) {
            DeviceTimestampStart = getIntent().getExtras().getString("DeviceTimestampStart");
        }

        projectInfo = dbSurvey.getProject(project_id, false);

        if (Boolean.parseBoolean(projectInfo.isShowBuildingScreen())) {
            route = getIntent().getExtras().getString("route");
            neighborhood = getIntent().getExtras().getString("neighborhood");
            sublocality_level_1 = getIntent().getExtras().getString("sublocality_level_1");
            locality = getIntent().getExtras().getString("locality");
            country = getIntent().getExtras().getString("country");
            postal_code = getIntent().getExtras().getString("postal_code");
            form_category_id = getIntent().getExtras().getInt("category_id");
            sub_cats = getIntent().getExtras().getInt("sub_category_id");
            currentCellId = getIntent().getExtras().getInt("cellId");
            timeSpentToGetLocation = getIntent().getExtras().getLong("timeSpentForLocation");
        }

        if (Boolean.parseBoolean(projectInfo.isShowMapScreen())) {
            currentlat = getIntent().getExtras().getDouble("lat");
            currentlong = getIntent().getExtras().getDouble("lon");
        }

    }

    @Override
    public void onBackPressed() {

        try {
            Builder alert = new Builder(WebviewActivity.this);
            alert.setTitle("Exit Survey");
            alert.setMessage("Press Yes to save and close Press No to close without saving Press cancel to go back to the survey");

            alert.setPositiveButton("No", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (values_json != null) {
                        if (values_json.equals("")) {
                            try {
                                dbSurvey.deleteSurvey(local_survey_db_id);

                                Activity activity = new Activity();
                                activity = WebviewActivity.this;
                                activity.finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Activity activity = new Activity();
                            activity = WebviewActivity.this;
                            activity.finish();
                        }
                    } else {
                        Activity activity = new Activity();
                        activity = WebviewActivity.this;
                        activity.finish();
                    }
                }
            });


            alert.setNegativeButton("Yes", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    status = "partial";
                    if (getIntent().hasExtra("sbjnum"))
                        status = AppConstants.QC_PARTIAL_STATUS;

                    webView.loadUrl("javascript:getPartialData()");  //Version 0.5.6 01/14/2015


                }
            });


            alert.setNeutralButton("Cancel", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadingWebView() {
        webView = (WebView) this.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "android");
        webView.loadUrl("file:///android_asset/jsonForm/mobile.html");


        if (getIntent().hasExtra("field_id") && getIntent().hasExtra("id")) {
            webView.loadUrl("javascript:setValues('" + getIntent().getExtras().getInt("field_id") + "', '" + getIntent().getExtras().getInt("id") + "')");
        }

        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("HTML Message", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }

            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                WebviewActivity.this.setProgress(progress * 1000);
            }
        });


        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebviewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });


        if (getIntent().hasExtra("json_string")) {
            if (values_json != null && !values_json.equals("")) {
                webView.setWebViewClient(new WebViewClient() {            //SHAZ 02/16/2015 prefilled values timer problem fixed
                    public void onPageFinished(WebView view, String url) {

                        webView.loadUrl("javascript:updateValues('" + values_json + "')");
                        if (loggin_dialog != null)
                            loggin_dialog.cancel();
                    }
                });
            } else {
                webView.setWebViewClient(new WebViewClient() {            //SHAZ 02/16/2015 prefilled values timer problem fixed
                    public void onPageFinished(WebView view, String url) {

                        if (loggin_dialog != null)
                            loggin_dialog.cancel();
                    }
                });

            }
        } else {
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {

                    if (loggin_dialog != null)
                        loggin_dialog.cancel();
                }
            });
        }

    }

    @JavascriptInterface
    public void alert(String message) {
        Builder alert = new Builder(WebviewActivity.this);
        alert.setTitle("Alert");
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();

        System.out.println(message);

    }

    @JavascriptInterface
    public String getData() {
        Log.d(TAG, "WebViewActivity.getData() called");

        result = readFile();

        try {

            if (result != null) {
                if (result == "")             // VERSION 0.5.8 SHAZ 01/16/2015
                    result = readFile();
//				url = result;
                Log.e("JSON ", result);
            } else {
                result = "";
                Log.e("JSON ", result);
            }

            if (result == "") {
                if (loggin_dialog != null)
                    loggin_dialog.cancel();

                Builder builder = new Builder(WebviewActivity.this);
                builder.setTitle("Survey not Found")
                        .setMessage("Survey not Found on local Device")
                        .setCancelable(false)
                        .setPositiveButton("OK", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
//				        	   loggin_dialog = ProgressDialog.show(WebviewActivity.this, "Retrieving Survey",
//				   					"Please wait...", true);
//
//				        	   new HttpAsyncTask().execute("http://api.dsds.apps.dna.com.sa/api/project/"+project_id+"/"+AppConstants.LANGUAGE);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                result = result.replace("\\\"", "\"").toString();
                result = result.replace("\\\\" + "r", " ").toString();
                result = result.replace("\\\\" + "n", " ").toString();
                result = result.replace("\\\\" + "u", "\\u").toString();
                result = result.replace("%USER%", user_name).toString();
                result = result.substring(1, result.length() - 1);
                Log.e("HTML Messages", result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @JavascriptInterface
    public void setData(String surveyData) throws JSONException {
        Log.d(TAG, "WebViewActivity.getPartialData()");

        System.out.println(surveyData);

        if (!status.equals(AppConstants.QC_PARTIAL_STATUS) && getIntent().hasExtra("sbjnum"))
            status = AppConstants.QC_COMPLETE_STATUS;

        saveSurvey(surveyData);
    }

    @JavascriptInterface
    public String getSurveyMediaPath() {
        String path = SurveyAppModel.getInstance().getSurveyPath(WebviewActivity.this) + "SurveyMedia/";
        File file = new File(path);
        if (!file.exists())
            file.mkdir();

        return path;
    }

    @JavascriptInterface
    public String getSurveyMediaDataPath() {
        String path = SurveyAppModel.getInstance().getSurveyPath(WebviewActivity.this) + "pictures/";
        File file = new File(path);
        if (!file.exists())
            file.mkdir();

        return path;
    }

    @JavascriptInterface
    public void cancelSurvey() {
        Log.d(TAG, "Cancel SUrvey Function");

        Builder builder = new Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbSurvey.deleteSurvey(local_survey_db_id);
                        WebviewActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void saveSurvey(String surveyData) {

        JSONObject mainJson = new JSONObject();

        long currentTimeTaken;
        date2 = new Date();
        currentTimeTaken = date2.getTime() - date1.getTime();
        currentTimeTaken = currentTimeTaken / 1000;

        timeTaken = timeTaken + currentTimeTaken;
        Log.e("TEST", date1.getTime() + " - " + date2.getTime() + " : " + timeTaken);

        String endDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try {
            if (projectInfo == null) projectInfo = dbSurvey.getProject(project_id, false);
            if (Boolean.parseBoolean(projectInfo.isShowBuildingScreen())) {
                mainJson.put("CITY_GPS", locality);
                mainJson.put("DISTRICT_GPS", sublocality_level_1);

                mainJson.put("CITY_GPS", location_data_obj_survey_form.getCity_name());
                mainJson.put("DISTRICT_GPS", location_data_obj_survey_form.getDistrict_name_tv());
                mainJson.put("STREET_NAME_GPS", location_data_obj_survey_form.getStreet_name_tv());
                mainJson.put("ZIPCODE_GPS", location_data_obj_survey_form.getZipCode_tv());
                mainJson.put("ADDITIONAL_CODE_GPS", location_data_obj_survey_form.getAdditional_code_tv());
                mainJson.put("LANDMARK_GPS", location_data_obj_survey_form.getLandMark_tv());
                mainJson.put("DISTRICT_AUDITOR", location_data_obj_survey_form.getDistrict_name_et());
                mainJson.put("BUILDING_NUMBER_AUDITOR", location_data_obj_survey_form.getBuilding_number_et());
                mainJson.put("STREET_NAME_AUDITOR", location_data_obj_survey_form.getStreet_name_et());
                mainJson.put("MOBILY_SHEET_AUDITOR", location_data_obj_survey_form.getMobilySheet_et());
                mainJson.put("STC_SHEET_AUDITOR", location_data_obj_survey_form.getStc_sheet_et());
                mainJson.put("LANDMARK_AUDITOR", location_data_obj_survey_form.getLandMark_et());
                mainJson.put("BUILDING_TYPE_DROPDOWN", location_data_obj_survey_form.getBuilding_type_spinner());
                mainJson.put("STREET_NAME_DROPDOWN", location_data_obj_survey_form.getStreet_name_spinner());
            }

            if (Boolean.parseBoolean(projectInfo.isShowMapScreen())) {
                mainJson.put("latitude", currentlat);
                mainJson.put("longitude", currentlong);
            } else {
                if (getIntent().hasExtra("Latitude") && getIntent().hasExtra("Longitude")) {
                    mainJson.put("latitude", SurveyAppModel.getInstance().getLatitude());
                    mainJson.put("longitude", SurveyAppModel.getInstance().getLongitude());
                } else {
                    mainJson.put("latitude", "0.000");
                    mainJson.put("longitude", "0.000");
                }
            }

            DataHandler dbData = new DataHandler(getApplicationContext());
            if (SurveyAppModel.getInstance().selectedProject.getSelectedSchool() != null)
                mainJson.put("selected_school_id", SurveyAppModel.getInstance().selectedProject.getSelectedSchool().getId());
            if (SurveyAppModel.getInstance().selectedProject.getSelectedEmployeeId() > 0)
                mainJson.put("selected_employee_id", SurveyAppModel.getInstance().selectedProject.getSelectedEmployeeId());
            // TODO: 04/05/2020 Course selection id has to be added here 
            mainJson.put("APP_VERSION", AppConstants.APP_VERSION);
            mainJson.put("local_db_survey_id", local_survey_db_id);
//            mainJson.put("surveyor_id", dbData.getUser().getUsername());
            mainJson.put("surveyor_id", DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getUsername().replace("@tcf.org.pk", ""));
            mainJson.put("project_id", Integer.toString(project_id));
            mainJson.put("device_id", SurveyAppModel.getInstance().DEVICE_ID);
            mainJson.put("start_datetime", DeviceTimestampStart);
            mainJson.put("submit_datetime", endDateandTime);


            //survey info
            mainJson.put("region", getIntent().getStringExtra("region"));
            mainJson.put("area", getIntent().getStringExtra("area"));
            mainJson.put("schoolId", getIntent().getStringExtra("schoolId"));
            mainJson.put("schoolName", getIntent().getStringExtra("schoolName"));
            mainJson.put("emisCode", getIntent().getStringExtra("emisCode"));
            mainJson.put("principalName", getIntent().getStringExtra("principalName"));
            mainJson.put("visitDate", getIntent().getStringExtra("visitDate"));
            mainJson.put("visitorName", getIntent().getStringExtra("visitorName"));
            mainJson.put("visitorDesignation", getIntent().getStringExtra("visitorDesignation"));

            if (getIntent().hasExtra("sbjnum")) {
                mainJson.put("sbjnum", getIntent().getExtras().getInt("sbjnum"));

                if (status.equals(AppConstants.QC_COMPLETE_STATUS)) {
                    POI_Model poi = dbSurvey.retrievePOI(getIntent().getExtras().getInt("sbjnum"));
                    poi.setLocal_survey_ID(local_survey_db_id);
                    poi.setSurveyData(surveyData);
                    poi.setSbjnum(-1);
                    dbSurvey.insertPOI(poi);
//					dbSurvey.deletePOI(getIntent().getExtras().getInt("sbjnum"));
                }
            }
//			else
//			{
//				mainJson.put("sbjnum", dbSurvey.getServerSurveyId(local_survey_db_id));
//			}

//			surveyData = surveyData.replace("\\", "").toString();
            surveyData = surveyData.replace("\"id\"", "\"field_id\"").toString();
            surveyData = surveyData.replace("\"value\"", "\"field_value\"").toString();
//			surveyData = surveyData.substring(1, url.length()-1);

            JSONArray jArray = new JSONArray(surveyData);

//			mainJson.put("survey_form", surveyData);

            mainJson.put("survey_form", jArray);

            Log.e("jsonWeb View Activity", mainJson.toString());

        } catch (JSONException exp) {
            exp.printStackTrace();
        }

        String jsonString = mainJson.toString();

        try {
            File temp_file = new File(ImageService.getNewFileName("survey", ".frm",WebviewActivity.this));

            FileWriter writer = new FileWriter(temp_file);
            writer.append(jsonString);
            writer.flush();
            writer.close();

            if (status.equals("complete")) {
                SurveyAppModel.getInstance().infoLog.addLog("Survey Saved, File Name : ", temp_file.toString(),WebviewActivity.this);
            }

            dbSurvey.updateSurveyStatus(local_survey_db_id, temp_file.getPath(), status, endDateandTime);

        } catch (IOException e) {
            e.printStackTrace();
            SurveyAppModel.getInstance().errorLog.addLog("Error in saving file", e.getLocalizedMessage(),WebviewActivity.this);
        }

        finish();

    }

    public String readFile() {            // VERSION 0.5.8 SHAZ 01/16/2015

        File file = new File(SurveyAppModel.getInstance().getSurveyPath(this) + "projects/prj_" + project_id + "_" + AppConstants.LANGUAGE + ".txt");

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

    private void parsePartialSurvey() {
        try {
            String prev_time;
            JSONObject jobj = new JSONObject(values_json);
            if (jobj.has("Time_Taken")) {
                prev_time = jobj.getString("Time_Taken");

                timeTaken = Long.parseLong(prev_time);

                System.out.println(timeTaken);
            }

            if (jobj.has("survey_form")) {
                values_json = jobj.getString("survey_form");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void imageFunction(String fieldId)  // VERSION 0.5.5 SHAZ 01/13/2015
    {
        try {

            Date time_stamp = new Date();
            Context context = WebviewActivity.this;
            PackageManager pm = context.getPackageManager();

            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                    || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {

                fieldID_image = fieldId;

                // VERSION 0.5.5 SHAZ 01/13/2015
                imageFileName = "_" + user_name + "_" + local_survey_db_id + "_" + Integer.toString(project_id)
                        + "_" + fieldId + "_" + time_stamp.getTime() + ".jpg";

                // VERSION 0.5.6 SHAZ 01/14/2015
                imageFilePath = SurveyAppModel.getInstance().getSurveyPath(WebviewActivity.this) + "pictures/";

                Intent cameraIntent = new Intent(this, CameraActivity.class);
                cameraIntent.putExtra("fileName", imageFileName);
                cameraIntent.putExtra("filePath", imageFilePath);
                startActivity(cameraIntent);

            } else
                Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG)
                        .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void startHiddenRecording(String fieldID)            // VERSION 0.5.5 SHAZ 01/13/2015
    {

        try {
            lib = new AudioLibrary(WebviewActivity.this,WebviewActivity.this);

            lib.setOrCreateLibraryFolder("Prj" + Integer.toString(project_id),WebviewActivity.this);
            fieldID_recording = fieldID;

            if (dbData.getUser().getUsername() != "") {
                AudioLibrary.user = dbData.getUser().getUsername();
                AudioLibrary.projectID = String.valueOf(project_id);
                AudioLibrary.surveyorID = String.valueOf(local_survey_db_id);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        lib.startRecording();    // VERSION 0.5.5 SHAZ 01/13/2015
        isRecordingStarted = true;
    }

    @JavascriptInterface
    public String GetLanguage() {
        return AppConstants.LANGUAGE.toString().toLowerCase();
    }

    @JavascriptInterface
    public void stopHiddenRecording(String fieldID)            // VERSION 0.5.5 SHAZ 01/13/2015
    {
        try {
            if (isRecordingStarted) {
                lib.stopRecording();
                System.out.println(recorded_file_name);
                System.out.println(recorded_file_path);
                System.out.println(fieldID_recording);

                isRecordingStarted = false;

                dbSurvey.saveMedia(local_survey_db_id, recorded_file_path, "local", project_id);        //Version 0.5.9 SHAZ 01/21/2015

                // VERSION 0.5.6 SHAZ 01/14/2015
                webView.loadUrl("javascript:setValues('" + fieldID_recording + "', '" + recorded_file_name + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CameraActivity.isImageCaptured) {
            imageFilePath = imageFilePath + imageFileName;


            System.out.println("image Captured");

            System.out.println(imageFilePath);
            CameraActivity.isImageCaptured = false;

            dbSurvey.saveMedia(local_survey_db_id, imageFilePath, "local", project_id);
            webView.loadUrl("javascript:setValues('" + fieldID_image + "','" + imageFileName + "')");
        }
    }

    void syncFailedAlert(String msg) {
        Builder alert = new Builder(WebviewActivity.this);
        alert.setTitle("Alert");
        alert.setMessage(msg);

        alert.setNegativeButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (values_json != null)
                    if (values_json.equals(""))
                        dbSurvey.deleteSurvey(local_survey_db_id);
                finish();
            }
        });

        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCamera != null)
            mCamera.release();
    }

    @Override
    public void onRecordStarted(String filename) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRecordStoped(String filename) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMediaAdded(String fileTag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayStarted() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayStopped() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayPaused() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayResumed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSeekPositionChanged(int currentPosition, int maxLength) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayCompleted() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onPlayError(int arg1, int arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onRecordError(int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRecordCanceled(String currentRecordingFile) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRecordIntervalElapsed() {
        // TODO Auto-generated method stub

    }

    private class takeHiddenPicture extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            CameraPreview mPreview;
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

            if (getApplicationContext().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                try {
                    mCamera = Camera.open(1);

                    mPreview = new CameraPreview(getApplicationContext(), mCamera);
                    preview.addView(mPreview);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Camera is already in use by some other Application", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                while (!isPreviewStarted) {
                    System.out.println("Preview not started yet");
                }
                //	mCamera.takePicture(null, null, mPicture);
            } catch (Exception e) {
                // TODO: handle exception
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                mCamera.takePicture(null, null, mPicture);
                isPreviewStarted = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
