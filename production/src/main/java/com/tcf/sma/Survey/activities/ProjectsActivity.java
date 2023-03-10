package com.tcf.sma.Survey.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.adapters.ProjectAdapter;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.httpServices.APIServiceManager;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.httpServices.HttpEventsArgs;
import com.tcf.sma.Survey.httpServices.IHttpPayloadRequestEventsHandler;
import com.tcf.sma.Survey.httpServices.IHttpRequester;
import com.tcf.sma.Survey.httpServices.MediaDownloadService;
import com.tcf.sma.Survey.httpServices.ProjectsSyncHandler;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ProjectsActivity extends DrawerActivity implements IHttpRequester, ProjectsSyncHandler, IHttpPayloadRequestEventsHandler {

    public static boolean isProjectCancelled = false;
    public static int sync_project_index = 0;
    public static int sync_media_index = 0;
    public static ProjectsActivity instance;
    static String userName;
    static int survey_pic_index = 0;
    DataHandler dbData;
    User user;
    ProjectAdapter pa;
    ListView projects_list;
    SurveyDBHandler dbSurvey;
    Button btn_sync;
    View view;
    private ProgressDialog progressDialog;
    private int write_Storage = 2;

    public static ProjectsActivity getInstance() {
        if (instance == null) {
            instance = new ProjectsActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_projects);
        setToolbar("Projects", this, false);

        AppConstants.applicationContext = ProjectsActivity.this;


        SurveyAppModel.dbSurvey = new SurveyDBHandler(getApplicationContext());
        SurveyAppModel.dataHandler = new DataHandler(getApplicationContext());
        SurveyAppModel.user = SurveyAppModel.dataHandler.getUser();
        SurveyAppModel.activity_media_download = this;
        dbData = new DataHandler(getApplicationContext());
        dbSurvey = new SurveyDBHandler(getApplicationContext());
        btn_sync = findViewById(R.id.btn_sync);

//        if (!SurveyAppModel.getInstance().getSelectedProject(this).equals("")) {
//            selected_project = dbSurvey.getProject(Integer.parseInt(SurveyAppModel.getInstance().getSelectedProject(this)));
//
//            if (selected_project != null) {
//                Intent dashBoardIntent = new Intent(ProjectsActivity.this, DashboardActivity.class);
//                dashBoardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                dashBoardIntent.putExtra("project_id", selected_project.getKey());
//                dashBoardIntent.putExtra("project_name", selected_project.get_name());
//                startActivity(dashBoardIntent);
//            }
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, write_Storage);
        }

        userName = dbData.getUser().getUsername();
        if (!checkUser())
            return;


        if (dbSurvey.getProjectsCount() > 0) {
            SurveyAppModel.projects.clear();
            SurveyAppModel.projects = dbSurvey.getAllProjects();
        } else {
            progressDialog = ProgressDialog.show(this, "", "Loading...", false, false);
        }

        pa = new ProjectAdapter(this, SurveyAppModel.projects);
        projects_list = findViewById(R.id.projects_list);
        projects_list.setAdapter(pa);
        pa.notifyDataSetChanged();

//		new saveTrackList().execute();

        projects_list.setOnItemClickListener((parent, view, position, id) -> {
            if (SurveyAppModel.projects.get(position).classStack.isEmpty()) {
                SurveyAppModel.projects.get(position).setClassStack();
//                Project.classStack.push(DashboardActivity.class);
//                Project.classStack.push(CourseSelector.class);
//                Project.classStack.push(EmployeeSelector.class);
//                Project.classStack.push(SelectSchoolActivity.class);
            }
//            SurveyAppModel.getInstance().setSelectedProject(getApplicationContext(), SurveyAppModel.projects.get(position).getKey() + "");
            try {
                Intent d_intent;
                d_intent = new Intent(ProjectsActivity.this, SurveyAppModel.projects.get(position).popActivityFromStack());
                SurveyAppModel.getInstance().selectedProject = SurveyAppModel.projects.get(position);
//                d_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(d_intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        btn_sync.setOnClickListener(v -> {
            btn_sync.setText("Syncing ... ");

            if (isNetworkAvailable()) {
                if (!SurveyAppModel.getInstance().isDataSyncing) {
                    btn_sync.setEnabled(false);
                    String url = getString(R.string.SURVEY_BASE_URL) + AppConstants.URL_GET_PROJECTS +
                            DatabaseHelper.getInstance(ProjectsActivity.this).getCurrentLoggedInUser().getUsername().replace("@tcf.org.pk", "")
                            + "/"
                            + AppModel.getInstance().getSpinnerSelectedSchool(this);
//                        + DatabaseHelper.getInstance(ProjectsActivity.this).getSelectedSchoolRegion().getId();
                    //new getProjectsList().execute(url);
                    APIServiceManager task = new APIServiceManager();
                    task.showProgress = false;
                    task.execute(url, AppConstants.REQ_GET_PROJECTS, ProjectsActivity.this, AppConstants.GET, null);
                }
            } else {
                SurveyAppModel.getInstance().isDataSyncing = false;
                btn_sync.setEnabled(true);
                btn_sync.setText("Sync Data");
                Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            if (!SurveyAppModel.getInstance().isDataSyncing) {
                if (btn_sync != null)
                    btn_sync.setText("Syncing...");

                btn_sync.setEnabled(false);

                SurveyAppModel.getInstance().isDataSyncing = true;
                String url = getString(R.string.SURVEY_BASE_URL) + AppConstants.URL_GET_PROJECTS +
                        DatabaseHelper.getInstance(ProjectsActivity.this).getCurrentLoggedInUser().getUsername().replace("@tcf.org.pk", "")
                        + "/"
                        + AppModel.getInstance().getSpinnerSelectedSchool(this);
//                        + DatabaseHelper.getInstance(ProjectsActivity.this).getSelectedSchoolRegion().getId();
                APIServiceManager task = new APIServiceManager();
                task.showProgress = false;
                task.execute(url, AppConstants.REQ_GET_PROJECTS, ProjectsActivity.this, AppConstants.GET, null);
            }
        } else
            btn_sync.setText("Sync Data");

//		onRequestSuccess("", AppConstants.REQ_GET_LOCATIONS); Hardcoded
    }

    public boolean checkUser() {
        if (!dbData.getUser().getUsername().equals("")) {
            Log.d("user", "user exists");
            user = dbData.getUser();
            Log.d("username", user.getUsername());
            Log.d("password", user.getPassword());
            return true;
        }
        return false;
    }

    public void createFile(String project_id, String json, String dir_folder, String path)            // VERSION 0.5.8 SHAZ 01/16/2015
    {
        FileWriter fWriter;

        File dir = new File(dir_folder);
        Log.i("TAG", "isExist : " + dir.exists());

        if (!dir.exists())
            dir.mkdirs();


        try {

            File survey_file = new File(path + project_id + "_" + AppConstants.LANGUAGE + ".txt");
            Log.i("TAG", "isExist : " + survey_file.exists());

            fWriter = new FileWriter(path + project_id + "_" + AppConstants.LANGUAGE + ".txt");
            fWriter.write(json);
            fWriter.flush();
            fWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog Error_alert(String msg) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("No Records Found")
                .setMessage(msg)
                .setPositiveButton("Ok", (dialog, whichButton) -> dialog.dismiss())
                .create();

        Toast.makeText(getApplicationContext(), "Intenet Connection Failure", Toast.LENGTH_LONG).show();
        SurveyAppModel.getInstance().isDataSyncing = false;
        btn_sync.setEnabled(true);
        btn_sync.setText("Sync Completed");

        return myQuittingDialogBox;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void requestPermissionsAndContinue() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, write_Storage);
        } else {
            surveyFormStarted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            surveyFormStarted();
        } else {
            Toast.makeText(this, "Storage permission is needed for saving survey forms, Please give permissions from the app settings.", Toast.LENGTH_LONG).show();
        }
        // permission denied, boo! Disable the
        // functionality that depends on this permission
    }

    @Override
    public void onRequestSuccess(String json, int request_code, Context mContext) {

        if (!isProjectCancelled) {
            switch (request_code) {

                case AppConstants.REQ_GET_PROJECTS:

                    if (json != null) {
                        projects_list.setAdapter(pa);
                        pa.notifyDataSetChanged();
                    } else {
                        if (isNetworkAvailable())
                            Error_alert("No Project(s) Assigned");
                        else
                            Error_alert("Check Your Internet Connection");
                    }

                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                    }
                    requestPermissionsAndContinue();
                    break;


                case AppConstants.REQ_GET_SURVEY_FORM:
                    surveyFormEnded();
                    break;

                case AppConstants.REQ_GET_LOCATIONS:
                    surveyDataEnded();
                    break;


                case AppConstants.REQ_GET_CATEGORIES:
                    projectCategoryQoutaEnded();
                    break;

                case AppConstants.REQ_GET_SURVEY_MEDIA:
                    surveyMediaEnded();
                    break;
            }
        } else {
            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
            SurveyAppModel.projects.get(sync_project_index).setProgress(100);
            isProjectCancelled = false;
            pa.notifyDataSetChanged();

            sync_project_index++;
            requestPermissionsAndContinue();

        }

    }

    @Override
    public void onRequestFailure(int error_code, int request_code) {

        SurveyAppModel.getInstance().errorLog.addLog("Request Failed " + error_code, request_code + "", ProjectsActivity.this);

        switch (request_code) {

            case AppConstants.REQ_GET_PROJECTS:

                if (btn_sync != null)
                    btn_sync.setText("Syncing...");

                if (isNetworkAvailable()) {
                    if (!SurveyAppModel.getInstance().isDataSyncing) {
                        btn_sync.setEnabled(false);

                        SurveyAppModel.getInstance().isDataSyncing = true;
                        String url = AppConstants.URL_GET_PROJECTS +
                                DatabaseHelper.getInstance(ProjectsActivity.this).getCurrentLoggedInUser().getUsername().replace("@tcf.org.pk", "")
                                + "/"
                                + AppModel.getInstance().getSpinnerSelectedSchool(this);
//                        + DatabaseHelper.getInstance(ProjectsActivity.this).getSelectedSchoolRegion().getId();
                        APIServiceManager task = new APIServiceManager();
                        task.showProgress = false;
                        task.execute(url, AppConstants.REQ_GET_PROJECTS, ProjectsActivity.this, AppConstants.GET, null);
                    }
                } else
                    btn_sync.setText("Sync Data");


                break;


            case AppConstants.REQ_GET_SURVEY_FORM:
                surveyFormEnded();
                break;

            case AppConstants.REQ_GET_LOCATIONS:
                surveyDataEnded();
                break;

            case AppConstants.REQ_GET_SURVEY_MEDIA:
                surveyMediaEnded();
                break;
        }
    }

    @Override
    public void surveyFormStarted() {

        String survey_url = "";

        if (SurveyAppModel.projects.size() != 0) {
            if (sync_project_index < SurveyAppModel.projects.size()) {
                ((View) pa.getView(sync_project_index, null, null)).findViewById(R.id.prj_progressBar).setVisibility(View.VISIBLE);
                ((View) pa.getView(sync_project_index, null, null)).findViewById(R.id.completed).setVisibility(View.VISIBLE);
                pa.notifyDataSetChanged();

                APIServiceManager task = new APIServiceManager();
                task.showProgress = false;
                survey_url = AppConstants.URL_GET_SURVEY_FORM_BY_ID + SurveyAppModel.projects.get(sync_project_index).get_key() + "/" + AppConstants.LANGUAGE.toLowerCase();

                SurveyAppModel.getInstance().infoLog.addLog("Requesting Survey Form", "Project ID : " + SurveyAppModel.projects.get(sync_project_index).get_key(), ProjectsActivity.this);

                if (isProjectCancelled) {
                    SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                    SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                    isProjectCancelled = false;
                    pa.notifyDataSetChanged();

                    sync_project_index++;
                    surveyFormStarted();
                } else
                    task.execute(survey_url, AppConstants.REQ_GET_SURVEY_FORM, ProjectsActivity.this, AppConstants.GET, null);
//				SurveyAppModel.task =task;
                SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Downloading Survey");
                SurveyAppModel.projects.get(sync_project_index).setProgress(10);
                pa.notifyDataSetChanged();
            } else {
                SurveyAppModel.getInstance().isDataSyncing = false;
                btn_sync.setEnabled(true);
                btn_sync.setText("Sync Completed");
                sync_project_index = 0;
            }
        }

    }

    @Override
    public void surveyFormEnded() {
        SurveyAppModel.projects.get(sync_project_index).setProgress(30);
        pa.notifyDataSetChanged();
        projectCategoryQoutaStarted();
    }

    @Override
    public void surveyDataStarted() {

        if (sync_project_index < SurveyAppModel.projects.size()) {
            String url_poi = "";
            APIServiceManager task = new APIServiceManager();
            task.showProgress = false;

            if (dbSurvey.retrieveLatestDate(SurveyAppModel.projects.get(sync_project_index).get_key()).equals(""))
                url_poi = AppConstants.URL_GET_ASSIGNED_SURVEY_DATA + SurveyAppModel.projects.get(sync_project_index).get_key() + "/" + user.getUsername();
            else
                url_poi = AppConstants.URL_GET_ASSIGNED_SURVEY_DATA + SurveyAppModel.projects.get(sync_project_index).get_key() + "/" + user.getUsername() + "/" + dbSurvey.retrieveLatestDate(SurveyAppModel.projects.get(sync_project_index).get_key());

            SurveyAppModel.getInstance().infoLog.addLog("Requesting Survey Data", "Project ID : " + SurveyAppModel.projects.get(sync_project_index).get_key(), ProjectsActivity.this);

            if (isProjectCancelled) {
                SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                isProjectCancelled = false;
                pa.notifyDataSetChanged();

                sync_project_index++;
                surveyFormStarted();
            } else
                task.execute(url_poi, AppConstants.REQ_GET_LOCATIONS, ProjectsActivity.this, AppConstants.GET, null);
//	 		SurveySurveyAppModel.task =task;

            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Downloading Survey Data");
            pa.notifyDataSetChanged();
        }
    }

    @Override
    public void surveyDataEnded() {

        SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Survey Data Completed");
        SurveyAppModel.projects.get(sync_project_index).setProgress(55);
        pa.notifyDataSetChanged();

        surveyMediaStarted();
    }

    @Override
    public void surveyMediaStarted() {

        try {
            String url_media = AppConstants.URL_GET_MEDIA + SurveyAppModel.projects.get(sync_project_index).get_key();
//			String url_media = AppConstants.URL_GET_MEDIA+4890;
            APIServiceManager task = new APIServiceManager();
            task.showProgress = false;

            SurveyAppModel.getInstance().infoLog.addLog("Requesting Survey Media", "Project ID : " + SurveyAppModel.projects.get(sync_project_index).get_key() + "", ProjectsActivity.this);

            if (isProjectCancelled) {
                SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                isProjectCancelled = false;
                pa.notifyDataSetChanged();

                sync_project_index++;
                surveyFormStarted();
            } else
                task.execute(url_media, AppConstants.REQ_GET_SURVEY_MEDIA, ProjectsActivity.this, AppConstants.GET, null);
//			SurveySurveyAppModel.task =task;

            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Downloading Media Files");
            pa.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surveyMediaEnded() {

        try {
            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Downloading Media Files");
            SurveyAppModel.projects.get(sync_project_index).setProgress(80);
            pa.notifyDataSetChanged();

            if (SurveyAppModel.media_list.size() != 0) {
                if (sync_media_index < SurveyAppModel.media_list.size()) {
                    String name = SurveyAppModel.media_list.get(sync_media_index).getFilename();
                    name = name.replaceAll(" ", "%20");
                    String url_media_files = "";
                    url_media_files = AppConstants.API_SURVEY_MEDIA_URL + name;


                    String directory = SurveyAppModel.getInstance().getSurveyPath(this) + "SurveyMedia/";

                    File dir = new File(directory);
                    if (!dir.exists())
                        dir.mkdirs();

                    File img = new File(directory + SurveyAppModel.media_list.get(sync_media_index).getFilename().replaceAll(" ", "%20"));
                    if (img.exists()) {
                        if (dbSurvey.mediaExits(SurveyAppModel.media_list.get(sync_media_index).getFilename())) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String last_saved_date = dbSurvey.latestMediaDate(SurveyAppModel.media_list.get(sync_media_index).getFilename());

                            try {
                                Date latest_upload_date = formatter.parse(SurveyAppModel.media_list.get(sync_media_index).getUploadedon());
                                Date local_saved_date = formatter.parse(last_saved_date);

                                if (!local_saved_date.equals(latest_upload_date)) {

                                    MediaDownloadService mus = new MediaDownloadService(this, url_media_files, sync_media_index);

                                    if (isProjectCancelled) {
                                        SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                                        SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                                        isProjectCancelled = false;
                                        pa.notifyDataSetChanged();

                                        sync_project_index++;
                                        surveyFormStarted();
                                    } else
                                        mus.execute();
//										SurveyAppModel.mus = mus;
                                } else {
                                    sync_media_index++;
                                    surveyMediaEnded();
                                }

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            dbSurvey.insertMedia(SurveyAppModel.media_list.get(sync_media_index));
                            sync_media_index++;
                            surveyMediaEnded();
                        }
                    } else {
                        MediaDownloadService mus = new MediaDownloadService(this, url_media_files, AppConstants.media_item_pics);

                        if (isProjectCancelled) {
                            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                            SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                            isProjectCancelled = false;
                            pa.notifyDataSetChanged();

                            sync_project_index++;
                            surveyFormStarted();
                        } else
                            mus.execute();
//						SurveyAppModel.mus = mus;
                    }
                } else {
                    SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Media download Completed");
                    SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                    pa.notifyDataSetChanged();

                    surveyPictureStarted();
                }
            } else {
                SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Media download Completed");
                SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                pa.notifyDataSetChanged();

                surveyPictureStarted();
            }
        } catch (Exception e) {
            e.printStackTrace();
            surveyMediaEnded();
        }
    }

    @Override
    public void onHttpDownloadCompleted(Object sender, HttpEventsArgs args) {

        String imgURL = args.getServiceInstance().getRequestURL();
        String storagePath = SurveyAppModel.getInstance().getSurveyPath(this);
        if (imgURL.endsWith("/"))
            imgURL = imgURL.substring(0, imgURL.length() - 2);

        String filename = imgURL.substring(imgURL.lastIndexOf("/") + 1);

        if (args.getTag().toString().equals(AppConstants.media_item_pics)) {

            storagePath = AppConstants.APP_MEDIA_ROOT;

            if (dbSurvey.mediaExits(SurveyAppModel.media_list.get(sync_media_index).getFilename()))
                dbSurvey.updateMedia(SurveyAppModel.media_list.get(sync_media_index));
            else
                dbSurvey.insertMedia(SurveyAppModel.media_list.get(sync_media_index));

            sync_media_index++;
            surveyMediaEnded();
        }

        if (args.getTag().toString().equals(AppConstants.media_surveyor_pics)) {
            storagePath = AppConstants.APP_SURVEYOR_PICS_ROOT;
//			dbSurvey.updateSurveyMediaStatus(SurveyAppModel.survey_media_list.get(survey_pic_index).getField_value(), AppConstants.media_exist_yes);
//
//			survey_pic_index++;
//			DownloadSurveyPicture();
        }

        if (args.getTag().toString().equals(AppConstants.download_on_demand)) {
            storagePath = AppConstants.APP_SURVEYOR_PICS_ROOT;
            SurveyAppModel.media_download_index++;

            if (SurveyAppModel.media_download_index < SurveyAppModel.media_download_list.size()) {
                String url_survey_pic = AppConstants.DSDS_MEDIA_URL + AppConstants.URL_GET_SURVEY_PICTURE + SurveyAppModel.media_download_list.get(SurveyAppModel.media_download_index).getField_value();
                MediaDownloadService mus = new MediaDownloadService(this, url_survey_pic, AppConstants.download_on_demand);
                mus.execute();
            } else {
                SurveyAppModel.media_download_list.clear();
                SurveyAppModel.media_download_index = 0;
            }
        }
        if (filename != null && !filename.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray((byte[]) args.getEventData(), 0, ((byte[]) args.getEventData()).length);
            SurveyAppModel.storeImage(bitmap, filename, storagePath, ProjectsActivity.this);
            if (new File(SurveyAppModel.getInstance().getSurveyPath(ProjectsActivity.this) + filename).exists()) {

                bitmap = null;

            }
        }
    }

    @Override
    public void onHttpDownloadFailed(Object sender, HttpEventsArgs args) {

        try {
            if (args.getTag().equals(AppConstants.media_surveyor_pics)) {
                SurveyAppModel.survey_media_list.remove(survey_pic_index);
                DownloadSurveyPicture();
            } else {
                surveyMediaEnded();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHttpUploadCompleted(Object sender, HttpEventsArgs args) {
        int i;
        i = 0;
    }

    @Override
    public void onHttpUploadFailed(Object sender, HttpEventsArgs args) {

        Toast.makeText(instance, "HTTP Upload Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surveyPictureStarted() {

        SurveyAppModel.survey_media_list.clear();
        SurveyAppModel.survey_media_list = dbSurvey.retrieveSurveyMedia(SurveyAppModel.projects.get(sync_project_index).get_key());

        sync_media_index = 0;
        sync_project_index++;
        surveyFormStarted();
//		DownloadSurveyPicture();  //Survey Pictures Download functionality has been removed
    }

    @Override
    public void DownloadSurveyPicture() {

        if (SurveyAppModel.survey_media_list.size() != 0) {
            if (survey_pic_index < SurveyAppModel.survey_media_list.size()) {
                String url_survey_pic = AppConstants.DSDS_MEDIA_URL + AppConstants.URL_GET_SURVEY_PICTURE + SurveyAppModel.survey_media_list.get(survey_pic_index).getField_value();

                SurveyAppModel.getInstance().infoLog.addLog("Requesting Survey Picture", "Picture Name : " + SurveyAppModel.survey_media_list.get(survey_pic_index).getField_value(), ProjectsActivity.this);

                MediaDownloadService mus = new MediaDownloadService(this, url_survey_pic, AppConstants.media_surveyor_pics);

                if (isProjectCancelled) {
                    SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                    SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                    isProjectCancelled = false;
                    pa.notifyDataSetChanged();

                    sync_project_index++;
                    surveyFormStarted();
                } else
                    mus.execute();
//				SurveyAppModel.mus = mus;

                SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Downloading Survey Pictures");
                SurveyAppModel.projects.get(sync_project_index).setProgress(85);
                pa.notifyDataSetChanged();
            } else {
                SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Survey Pictures Completed");
                SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                pa.notifyDataSetChanged();

                sync_media_index = 0;
                sync_project_index++;
                surveyFormStarted();
            }
        } else {
            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Survey Pictures Completed");
            SurveyAppModel.projects.get(sync_project_index).setProgress(100);
            pa.notifyDataSetChanged();

            sync_media_index = 0;
            sync_project_index++;
            surveyFormStarted();
        }
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        menu.getItem(1).setEnabled(false);
//        return true;
//    }

//	public void nextProject()
//	{
//		if(sync_project_index<SurveyAppModel.projects.size())
//		{
//			SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
//			SurveyAppModel.projects.get(sync_project_index).setProgress(100);
//			pa.notifyDataSetChanged();
//			
//			sync_media_index = 0;
//			sync_project_index++;
//			surveyFormStarted();
//		}
//	}

    //
    @Override
    public void projectCategoryQoutaStarted() {

        try {
            String qouta_url = "";


            if (SurveyAppModel.projects.size() != 0) {
                if (sync_project_index < SurveyAppModel.projects.size()) {
                    if (SurveyAppModel.projects.get(sync_project_index).getNeedCategoryScreen().equals("1")) {
                        APIServiceManager task = new APIServiceManager();
                        task.showProgress = false;
                        qouta_url = AppConstants.URL_GET_SURVEY_QOUTA + SurveyAppModel.projects.get(sync_project_index).get_key();

                        SurveyAppModel.getInstance().infoLog.addLog("Requesting Project Category Qouta", "Project ID : " + SurveyAppModel.projects.get(sync_project_index).get_key(), ProjectsActivity.this);


                        if (isProjectCancelled) {
                            SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Cancelled");
                            SurveyAppModel.projects.get(sync_project_index).setProgress(100);
                            isProjectCancelled = false;
                            pa.notifyDataSetChanged();

                            sync_project_index++;
                            surveyFormStarted();
                        } else
                            task.execute(qouta_url, AppConstants.REQ_GET_CATEGORIES, ProjectsActivity.this, AppConstants.GET, null);
                        //					SurveyAppModel.task = task;

                        SurveyAppModel.projects.get(sync_project_index).setCurrent_download("Downloading Survey Categories");
                        SurveyAppModel.projects.get(sync_project_index).setProgress(17);
                        pa.notifyDataSetChanged();
                    } else {
                        projectCategoryQoutaEnded();
                    }
                } else {
                    projectCategoryQoutaEnded();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            SurveyAppModel.getInstance().errorLog.addLog("Exception Getting Project Categories", e.getLocalizedMessage(), ProjectsActivity.this);
        }

    }

    //
    @Override
    public void projectCategoryQoutaEnded() {

        SurveyAppModel.projects.get(sync_project_index).setProgress(21);
        pa.notifyDataSetChanged();
        surveyDataStarted();

    }
}
