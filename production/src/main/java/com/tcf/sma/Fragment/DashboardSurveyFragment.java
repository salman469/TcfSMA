package com.tcf.sma.Fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.activities.DataHandler;
import com.tcf.sma.Survey.activities.PendingSurveys;
import com.tcf.sma.Survey.activities.Project;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.activities.TestReceiver;
import com.tcf.sma.Survey.activities.User;
import com.tcf.sma.Survey.activities.WebviewActivity;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.httpServices.HttpEventsArgs;
import com.tcf.sma.Survey.httpServices.HttpEventsHandler;
import com.tcf.sma.Survey.httpServices.IHttpRequester;
import com.tcf.sma.Survey.httpServices.SyncRequester;
import com.tcf.sma.Survey.mediaServices.ImageService;
import com.tcf.sma.Survey.model.SurveyAppModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardSurveyFragment extends Fragment implements View.OnClickListener, IHttpRequester, SyncRequester, HttpEventsHandler {

    SharedPreferences pref;
    DataHandler dbData;
    SurveyDBHandler dbSurvey;
    User user;
    LinearLayout ll_back;
    int project_id;
    String project_name = "";
    public TextView tv_regionName, tv_areaName, tv_schoolName;
    LocationManager locManager;
    String total = "0", today = "0", week = "0", month = "0";
    private int schoolId = 0;
    Intent webViewIntent;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};
    View rootView;

    public static void registerAlarm(Context context) {
        try {

            Intent i = new Intent(context, TestReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 555, i, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            am.cancel(sender);

        } catch (Exception e) {
            //	SurveyAppModel.getInstance().errorLog.addLog("DashboardActivity.registerAlarm", e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            working();

            ((TextView) rootView.findViewById(R.id.tv_large_partial)).setText(String.valueOf(dbSurvey.getPartialSurveyCount(project_id)));
            ((TextView) rootView.findViewById(R.id.tv_large_complete)).setText(String.valueOf(dbSurvey.getCompleteSurveyCount(project_id)));
            ((TextView) rootView.findViewById(R.id.tv_large_others)).setText(String.valueOf(dbSurvey.Approval_count()));

            if (dbSurvey != null) {
                Project project = dbSurvey.getProject(project_id,false);
                ((TextView) rootView.findViewById(R.id.tv_large_partial)).setText(String.valueOf(dbSurvey.getPartialSurveyCount(project_id)));
                ((TextView) rootView.findViewById(R.id.tv_large_complete)).setText(String.valueOf(dbSurvey.getCompleteSurveyCount(project_id)));

                total = project.getTotalSurveys();
                today = project.getToday();
                week = project.getWeeek();
                month = project.getMonth();

                ((TextView) rootView.findViewById(R.id.tv_large_total)).setText(total);
                ((TextView) rootView.findViewById(R.id.tv_large_today)).setText(today);
                ((TextView) rootView.findViewById(R.id.tv_large_week)).setText(week);
                ((TextView) rootView.findViewById(R.id.tv_large_month)).setText(month);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dashboard_survey, container, false);
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*view = setActivityLayout(this, R.layout.activity_dashboard);
        setToolbar("Dashboard", this, false);*/
//        setContentView(R.layout.activity_dashboard);

        //AppConstants.applicationContext = getActivity();        //SHAZ 02/26/2015
        registerAlarm(getActivity());                        //SHAZ 03/10/2015

        System.out.println(SurveyAppModel.getInstance().getSelectedProject(getActivity()));

        dbData = new DataHandler(getActivity().getApplicationContext());
        dbSurvey = new SurveyDBHandler(getActivity().getApplicationContext());
        if (!checkUser())
            return null;

        pref = getActivity().getApplicationContext().getSharedPreferences("DNA_DSDS_pref", 0);


   /*     tv_regionName = getActivity().findViewById(R.id.tv_regionName);
        tv_areaName = getActivity().findViewById(R.id.tv_areaName);
        tv_schoolName = getActivity().findViewById(R.id.tv_schoolName);*/

        ll_back = rootView.findViewById(R.id.back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        working();

        SchoolModel schoolModel = DatabaseHelper.getInstance(getActivity()).getSchoolById(schoolId);
     /*       tv_schoolName.setText(schoolModel.getName());

        ArrayList<SchoolExpandableModel> schoolExpandableModels = DatabaseHelper.getInstance(getActivity()).getSchoolByAreaRegionFromQuery();
        tv_areaName.setText(schoolExpandableModels.get(0).getArea());
        tv_regionName.setText(schoolExpandableModels.get(0).getArea());*/

        // 0
        // -
        // for
        // private
        // mode

        SurveyAppModel.getInstance().registerSync(getActivity());

//		registerAlarm(this);

        if (pref.getString("lang", "").equals("EN")) {
            AppConstants.LANGUAGE = "EN";
        } else if (pref.getString("lang", "").equals("AR")) {
            AppConstants.LANGUAGE = "AR";
        } else {
            AppConstants.LANGUAGE = "EN";
        }

        AppConstants.SelectedCountry = pref.getString("country", "2");
//        getIntent().getExtras().getInt("project_id");
//        getIntent().getExtras().getString("project_name");
        /*project_id = SurveyAppModel.getInstance().selectedProject.get_key();
        project_name = SurveyAppModel.getInstance().selectedProject.get_name();
*/

        rootView.findViewById(R.id.btnBeginSurvey).setOnClickListener(this);
        rootView.findViewById(R.id.btnSyncSurveys).setOnClickListener(this);

        if (AppConstants.syncing) {
            rootView.findViewById(R.id.btnSyncSurveys).setEnabled(false);
            ((Button) rootView.findViewById(R.id.btnSyncSurveys)).setText("Syncing...");
        }

        rootView.findViewById(R.id.wrap_partial).setOnClickListener(this);

        if (!SurveyAppModel.getInstance().countryDetected.equals("")) {
            // &&
            // !SurveyAppModel.getInstance().cityDetected.equals(""))
            // {
//			TextView tv2 = (TextView) findViewById(R.id.tvLocation);
//			if (SurveyAppModel.getInstance().cityDetected == null)
//				SurveyAppModel.getInstance().cityDetected = "";
//			tv2.setText(SurveyAppModel.getInstance().cityDetected + " "
//					+ SurveyAppModel.getInstance().countryDetected);
//
//			findViewById(R.id.btnBeginSurvey).setEnabled(true); // for qatar
        }

        List<String> surveyList = dbSurvey.getAllSurveys();

        ListView lv_stats = (ListView) rootView.findViewById(R.id.listView_stats);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, surveyList);
        lv_stats.setAdapter(adapter);

        locManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


//        heading = findViewById(R.id.project_heading);
//        heading.setText("Dashboard");

//        boolean gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!gpsEnabled) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//            return;
//        }

        return rootView;
    }

    private void working() {
        schoolId = ((NewDashboardActivity) Objects.requireNonNull(getActivity())).schoolId;
    }

    public boolean checkUser() {
        if (dbData.getUser().getUsername() != "") {

            Log.d("user", "user exists");
            user = dbData.getUser();
            Log.d("username", user.getUsername());
            Log.d("password", user.getPassword());
            return true;
        } else {
            getActivity().finish();
//			startActivity(new Intent(DashboardActivity.this, Login.class));
            return false;
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Activity activity;
//        activity = DashboardActivity.this;
//        activity.finish();
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBeginSurvey) {
            getPermissions();

        } else if (v.getId() == R.id.btnSyncSurveys) {
            v.setEnabled(false);
            ((Button) v).setText("Syncing...");
            SurveyAppModel.getInstance().registerSync(getActivity());
            SurveyAppModel.getInstance().forceSync = true;
        } else if (v.getId() == R.id.wrap_partial) {
            Intent intent_partial = new Intent(getActivity(), PendingSurveys.class);
            intent_partial.putExtra("project_id", project_id);
            startActivity(intent_partial);
        }
    }

   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), ProjectsActivity.class);
        startActivity(intent);
        getActivity().finishAffinity();
    }*/

    private void callWebView() {
        File temp_file = new File(ImageService.getNewFileName("frm_cache_", ".frm", getActivity().getApplicationContext()));
        long survey_id = dbSurvey.saveNewSurvey(temp_file.getPath(), dbData.getUser().getUsername(), "partial", project_id);

        //System.out.println(temp_file.getName()+"   "+survey_id+"   "+project_id+"   "+project_name);

        Project info = dbSurvey.getProject(project_id, false);

        if (Boolean.parseBoolean(info.getShowMapScreen())) {
//				webViewIntent = new Intent(this, SurveyMapActivity_New.class);
//            webViewIntent = new Intent(this, SurveyMapActivity.class);
            webViewIntent.putExtra("Building_permission", info.getShowBuildingScreen());
        } else {
            webViewIntent = new Intent(getActivity(), WebviewActivity.class);
        }

        String startDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        webViewIntent.putExtra("cached_file", temp_file.getName());
        webViewIntent.putExtra("local_survey_db_id", survey_id);
        webViewIntent.putExtra("project_id", project_id);
        webViewIntent.putExtra("project_name", project_name);
        webViewIntent.putExtra("DeviceTimestampStart", startDateandTime);
        webViewIntent.putExtra("start_activity_as", AppConstants.START_SURVEY_ACTIVITY);

        // custom dialog
//        if (info.needCategoryScreen.equals("1")) {
//            final Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.custom);
//            dialog.setTitle("Select Category");
//
//            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
//            btn_cancel.setVisibility(View.VISIBLE);
//            btn_cancel.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            catList = dbSurvey.retrieveCategory(project_id);
//
//            TextView tv_count = (TextView) dialog.findViewById(R.id.districtsCount);
//            tv_count.setText("No of Categories :" + catList.size());
//
//            dialog.show();
//        } else {
        startActivity(webViewIntent);
//				changeIntent(webViewIntent);
//        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermissions(getActivity(), permissions)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            callWebView();
        }

    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (hasPermissions(getActivity(), permissions)) {
                    callWebView();
                } else {
                    if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "Storage permission is needed for saving survey forms. Please give permissions from the app settings.", Toast.LENGTH_LONG).show();
                    }

                    if (ActivityCompat.checkSelfPermission(getActivity(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "Location permission is needed for getting user co-ordinates. Please give permissions from the app settings.", Toast.LENGTH_LONG).show();
                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), permissions[2]) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "Camera permission is needed for capturing images. Please give permissions from the app settings.", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    @Override
    public void onRequestSuccess(String json, int request_code, Context mContext) {

        if (json != null) {
            if (!json.equals("")) {
                SurveyAppModel.getInstance().infoLog.addLog("Dashboard Activity respose Json onRequestSuccess", json, mContext);

                json = json.substring(1, json.length() - 1);
                json = json.replace("\\", "");
            }
        }

        SurveyAppModel.getInstance().infoLog.addLog("Dashboard Activity request Code onRequestSuccess", request_code + "",mContext);

        switch (request_code) {

            case AppConstants.REQ_CHANGE_POSITION:
                try {
                    if (json != null) {
                        if (json.equals("OK")) {
                            dbSurvey.updateLatLong(SurveyAppModel.survey_id);
//						Toast.makeText(getApplicationContext(), "Location Changed", Toast.LENGTH_LONG).show();
                        } else {
//						Toast.makeText(getApplicationContext(), "Error in Location Change", Toast.LENGTH_LONG).show();
                            SurveyAppModel.getInstance().errorLog.addLog("QC Operation Failed", json, getActivity().getApplicationContext());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onSyncCompleted();
                SurveyAppModel.getInstance().registerSync(getActivity());
                break;
            case AppConstants.REQ_SEND_REJECTION:
                try {
                    if (json != null) {
                        if (json.equals("OK")) {
//						Toast.makeText(getApplicationContext(), "Survey Rejcted", Toast.LENGTH_LONG).show();
                            dbSurvey.deleteApproval(SurveyAppModel.survey_id);
                        } else {
//						Toast.makeText(getApplicationContext(), "Survey Rejection Failed", Toast.LENGTH_LONG).show();
                            SurveyAppModel.getInstance().errorLog.addLog("QC Operation Failed", json, getActivity().getApplicationContext());
                        }
                    }

                    ((TextView) rootView.findViewById(R.id.tv_large_others)).setText(String.valueOf(dbSurvey.Approval_count()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onSyncCompleted();
                SurveyAppModel.getInstance().registerSync(getActivity());
                break;
            case AppConstants.REQ_SEND_APPROVAL:
                try {
                    if (json != null) {
                        if (json.equals("OK")) {
//						Toast.makeText(getApplicationContext(), "Survey Approved", Toast.LENGTH_LONG).show();
                            dbSurvey.deleteApproval(SurveyAppModel.survey_id);
                        } else {
//						Toast.makeText(getApplicationContext(), "Survey Approval Failed", Toast.LENGTH_LONG).show();
                            SurveyAppModel.getInstance().errorLog.addLog("QC Operation Failed", json, getActivity().getApplicationContext());
                        }
                    }
                    ((TextView) rootView.findViewById(R.id.tv_large_others)).setText(String.valueOf(dbSurvey.Approval_count()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onSyncCompleted();
                SurveyAppModel.getInstance().registerSync(getActivity());
                break;

            case AppConstants.REQ_POST_SURVEY_FORM:
                onSyncCompleted();
//			AppConstants.syncing = false;
//			AppConstants.syncingId = 0;
                String server_id = "";
                try {
                    JSONObject js = new JSONObject(json);
                    if (js.has("sbjnum") && !js.getString("sbjnum").equals("0"))
                        server_id = js.getString("sbjnum");
                } catch (JSONException e) {
                    e.printStackTrace();
                    SurveyAppModel.getInstance().infoLog.addLog("Exception Dashboard Activity onRequestSuccess", e.getLocalizedMessage(),mContext);
                }
                if (!server_id.equals("")) {
                    if (AppConstants.syncingId != 0) {


                        dbSurvey.updateSurveySynced(AppConstants.syncingId, server_id, "synced");
//					dbSurvey.deleteLocalPOI(AppConstants.syncingId);

                        dbSurvey.updateLocalPOI(AppConstants.syncingId, server_id);
                        long sbjnum_old = dbSurvey.retrievePOISbjnum(AppConstants.syncingId);
                        dbSurvey.updateApprovalSbjnum(sbjnum_old, server_id);

                        SurveyAppModel.getInstance().syncedSurveyCount++;

                        total = (Integer.parseInt(total) + 1) + "";
                        today = (Integer.parseInt(today) + 1) + "";
                        week = (Integer.parseInt(week) + 1) + "";
                        month = (Integer.parseInt(month) + 1) + "";

                        dbSurvey.updateProjectCount(project_id, total, today, week, month);

                        ((TextView) rootView.findViewById(R.id.tv_large_complete)).setText(String.valueOf(dbSurvey.getCompleteSurveyCount(project_id)));
                        ((TextView) rootView.findViewById(R.id.tv_large_total)).setText(String.valueOf(dbSurvey.getSyncedSurveyCount(project_id)));
                        ((TextView) rootView.findViewById(R.id.tv_large_total)).setText(total);
                        ((TextView) rootView.findViewById(R.id.tv_large_today)).setText(today);
                        ((TextView) rootView.findViewById(R.id.tv_large_week)).setText(week);
                        ((TextView) rootView.findViewById(R.id.tv_large_month)).setText(month);
                    }
//				AppConstants.syncing = false;
//				findViewById(R.id.btnSyncSurveys).setEnabled(true);
//				((Button)findViewById(R.id.btnSyncSurveys)).setText("Sync Pending\nSurveys");
//				AppConstants.syncingId = 0;
                }
                AppConstants.syncing = false;
                AppConstants.syncingId = 0;
//			registerAlarm(this);
                SurveyAppModel.getInstance().registerSync(getActivity());
                break;

            default:
                break;
        }

    }

    @Override
    public void onRequestFailure(int error_code, int request_code) {

        SurveyAppModel.getInstance().errorLog.addLog("Dashboard Activity respose Json onRequestFailed", error_code + "", getActivity().getApplicationContext());

        /*
         *
         * SurveyAppModel.getInstance().districtsDetected) {
         * if(d.id.equals(p.getTitle())) {
         * p.polygonOptions.fillColor(Color.argb(64, 255, 0, 0)); } } }
         */

        switch (request_code) {
            case AppConstants.REQ_GET_ALL_DISTRICTS:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Failed to load district data.", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case AppConstants.REQ_CHANGE_POSITION:
            case AppConstants.REQ_SEND_REJECTION:
            case AppConstants.REQ_SEND_APPROVAL:
                onSyncCompleted();
                SurveyAppModel.getInstance().registerSync(getActivity());
                break;

            case AppConstants.REQ_POST_SURVEY_FORM:
                onSyncCompleted();
                AppConstants.syncing = false;
                AppConstants.syncingId = 0;
                SurveyAppModel.getInstance().registerSync(getActivity());
                break;

            default:
                break;
        }
    }

    @Override
    public void onSyncStarted() {
        AppConstants.syncing = true;
        rootView.findViewById(R.id.btnSyncSurveys).setEnabled(false);
        ((Button) rootView.findViewById(R.id.btnSyncSurveys)).setText("Syncing...");
    }

    @Override
    public void onSyncCompleted() {
        AppConstants.syncing = false;
        rootView.findViewById(R.id.btnSyncSurveys).setEnabled(true);
        ((Button) rootView.findViewById(R.id.btnSyncSurveys)).setText("Sync Completed\nSurveys");
    }

    @Override
    public void onMediaSyncStarted() {
        AppConstants.syncing_media = true;
//		findViewById(R.id.btnSyncSurveys).setEnabled(false);
//		((Button)findViewById(R.id.btnSyncSurveys)).setText("Syncing...");
    }

    @Override
    public void onMediaSyncCompleted() {
        AppConstants.syncing_media = false;
//		findViewById(R.id.btnSyncSurveys).setEnabled(true);
//		((Button)findViewById(R.id.btnSyncSurveys)).setText("Sync Completed\nSurveys");
    }

    @Override
    public void onHttpDownloadCompleted(Object sender, HttpEventsArgs args) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHttpDownloadFailed(Object sender, HttpEventsArgs args) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHttpUploadCompleted(Object sender, HttpEventsArgs args) {
        onMediaSyncCompleted();
        AppConstants.syncing_media = false;
//		AppConstants.syncingId_media = 0;

        if (args.getTag() instanceof String) {
            String id = args.getTag().toString();
            dbSurvey.updateMediaStatus(Long.valueOf(id), "synced");
        }
//		registerAlarm(this);
        SurveyAppModel.getInstance().registerSync(getActivity());
    }

    @Override
    public void onHttpUploadFailed(Object sender, HttpEventsArgs args) {
        onMediaSyncCompleted();
        AppConstants.syncing_media = false;
//		AppConstants.syncingId_media = 0;
//		registerAlarm(this);
        SurveyAppModel.getInstance().registerSync(getActivity());
    }

    @Override
    public void onSyncFailed() {
        SurveyAppModel.getInstance().errorLog.addLog("DashboardActivity.SyncFailed",
                "Sync Failed due to No Internet Connection", getActivity().getApplicationContext());

        onSyncCompleted();
    }

}
