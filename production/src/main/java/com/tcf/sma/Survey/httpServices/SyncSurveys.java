package com.tcf.sma.Survey.httpServices;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tcf.sma.R;
import com.tcf.sma.Survey.activities.DataHandler;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.mediaServices.GenericFileService;
import com.tcf.sma.Survey.model.ApprovalModel;
import com.tcf.sma.Survey.model.GPSData;
import com.tcf.sma.Survey.model.SurveyAppModel;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;


public class SyncSurveys extends WakefulBroadcastReceiver {
    public static int retries = 0;

    SurveyDBHandler dbSurvey;
    DataHandler dbData;
    Context context;
    //when clicked on the sync completed surveys
    boolean isForced;

    SyncRequester syncRequester = (SyncRequester) AppConstants.applicationContext;
    ArrayList<GPSData> gpsData;

    public SyncSurveys() {
        int i;
        i = 0;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            isForced = SurveyAppModel.getInstance().forceSync;
            this.context = context;

            Log.e("REQUESTING SYNC", "REQUESTED");

            if (isNetworkAvailable()) {
                dbSurvey = new SurveyDBHandler(context);
                dbData = new DataHandler(context);

                if (!SurveyAppModel.getInstance().isSurveySyncStarted)
                    sync();
            } else
                syncRequester.onSyncFailed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sync() {

        //this function will always call api for the first value in db
        //when the api call has been executed (either success or failure), this fuction will run again.
        Log.e("SYNC SURVEYS TAG", "SYNC SURVEYS CALLED");

        int surveyCount = 0;

        SurveyAppModel.getInstance().infoLog.addLog("Sync Surveys Called", "Syncing Survey Data",context);


        try {
            ArrayList<ApprovalModel> ap = dbSurvey.retrieveApproval();
//			gpsData = dbData.getGPSData();
//			if(!gpsData.isEmpty()) {
//	//			gpsSyncCount = gpsData.size();
//				syncGPSData();
//			}

            if (!AppConstants.syncing) {
                if (syncRequester != null)
                    syncRequester.onSyncStarted();

                AppConstants.syncingId = 0;
                List<String> surveysList = dbSurvey.getCompletedSurveys("paths");

                if (surveysList.size() > 0)
                    SurveyAppModel.getInstance().infoLog.addLog("Uploading Survey", surveysList.get(0),context);

                surveyCount = surveysList.size();

                List<String> surveysListIds = dbSurvey.getCompletedSurveys("ids");
                JSONObject newJson = new JSONObject();
                if (!surveysList.isEmpty()) {

                    SurveyAppModel.getInstance().isSurveySyncStarted = true;

                    SurveyAppModel.getInstance().infoLog.addLog("Survey Found", surveysList.size() + " surveys in queue",context);


                    //				for(int i=0;i<surveysList.size();i++) {
                    if (!surveysList.get(0).equals("") && new File(surveysList.get(0)).exists()) {
                        AppConstants.syncing = true;
                        AppConstants.syncingId = Long.valueOf(surveysListIds.get(0));

                        String jsonBody = GenericFileService.loadFile(surveysList.get(0));
                        if (!jsonBody.equals("")) {
                            try {
                                newJson = new JSONObject(jsonBody);
                                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                newJson.put("upload_datetime", currentDateandTime);

                                APIServiceManager task = new APIServiceManager();
                                task.showProgress = false;
                                task.jsonBody = newJson.toString();
                                task.execute(AppConstants.URL_POST_SURVEY_FORM, AppConstants.REQ_POST_SURVEY_FORM, AppConstants.applicationContext, AppConstants.POST_JSON_BODY, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                SurveyAppModel.getInstance().errorLog.addLog("Exception in Syncing", e.getLocalizedMessage(),context);
                            }
                        }
                    } else {
                        dbSurvey.deleteSurvey(Long.valueOf(surveysListIds.get(0)));
                    }
                } else {
                    if (syncRequester != null)
                        syncRequester.onSyncCompleted();
                }
            }

            if (surveyCount == 0 && ap.size() == 0) {
                if (!AppConstants.syncing_media) {

                    if (syncRequester != null)
                        syncRequester.onMediaSyncStarted();

                    List<String> mediasList = dbSurvey.getMedias("paths");
                    List<String> mediasListIds = dbSurvey.getMedias("ids");
                    List<String> projectsIds = dbSurvey.getMedias("projects");

                    if (!mediasList.isEmpty()) {
                        SurveyAppModel.getInstance().isSurveySyncStarted = true;
                        for (int i = 0; i < mediasList.size(); i++) {
                            if (!mediasList.get(i).equals("") && new File(mediasList.get(i)).exists()) {
                                if (!isDelayed() || isForced)
                                    if (retries < 3 || isForced) {
                                        AppConstants.syncing_media = true;
                                        MediaUploadService mus = new MediaUploadService(mediasList.get(i), null,
                                                AppConstants.URL_POST_SEND_IMAGE,
                                                (HttpEventsHandler) AppConstants.applicationContext,
                                                mediasListIds.get(i),
                                                AppConstants.applicationContext);
                                        SurveyAppModel.getInstance().infoLog.addLog("Uploading Media", mediasList.get(i),context);
                                        mus.execute();

                                        if (isDelayed()) {
                                            if (mus.responseCode >= 200 && mus.responseCode <= 201) {
                                                resetDelay();
                                            }
                                        }
                                    } else {
                                        if (!isForced) {
                                            SharedPreferences.Editor editor = context.getSharedPreferences("delay", context.MODE_PRIVATE).edit();
                                            editor.putBoolean("isPostponed", true);
                                            editor.putLong("time", System.currentTimeMillis());
                                            editor.commit();
                                            showNotification();
                                        }
                                    }
                            } else {
                                if (syncRequester != null)
                                    syncRequester.onMediaSyncCompleted();
                            }
                        }
                    } else {
                        if (syncRequester != null)
                            syncRequester.onMediaSyncCompleted();
                    }
                }
            }

            if (!AppConstants.syncing && !AppConstants.syncing_media) {
                String lat = "", lng = "";
                int operation = -1;

                if (ap.size() > 0) {
                    syncRequester.onSyncStarted();

                    APIServiceManager task = new APIServiceManager();
                    task.showProgress = false;

                    if (ap.get(0).getLatitude() != 0.0 && ap.get(0).getLongitude() != 0.0) {
                        operation = -1;
                        lat = ap.get(0).getLatitude() + "";
                        lat = lat.replace(".", "_");
                        lng = ap.get(0).getLongitude() + "";
                        lng = lng.replace(".", "_");
                    } else if (ap.get(0).getQc_status() == 1) {
                        operation = 1;
                    } else if (ap.get(0).getQc_status() == 0) {
                        operation = 0;
                    }

                    SurveyAppModel.survey_id = ap.get(0).getSurveyID();

                    String url = "";

                    String date = ap.get(0).getDate();
                    date = date.replaceAll(" ", "%20");
                    date = date.replaceAll(":", "%7C");

                    switch (operation) {
                        case -1:
                            SurveyAppModel.getInstance().infoLog.addLog("Uploading Change Location", ap.get(0).getSurveyID() + "",context);
                            url = context.getString(R.string.SURVEY_BASE_URL) + "changelocation/" + ap.get(0).getSurveyID() + "/" + dbData.getUser().getUsername() + "/" + date + "/" + lng + "/" + lat;
                            task.execute(url, AppConstants.REQ_CHANGE_POSITION, AppConstants.applicationContext, AppConstants.GET, null);
                            break;
                        case 0:
                            SurveyAppModel.getInstance().infoLog.addLog("Uploading POI Rejection", ap.get(0).getSurveyID() + "",context);
                            url = context.getString(R.string.SURVEY_BASE_URL) + "rejectsurvey/" + ap.get(0).getSurveyID() + "/" + dbData.getUser().getUsername() + "/" + date + "/" + ap.get(0).getReasonID();
                            task.execute(url, AppConstants.REQ_SEND_REJECTION, AppConstants.applicationContext, AppConstants.GET, null);
                            break;
                        case 1:
                            SurveyAppModel.getInstance().infoLog.addLog("Uploading POI Approval", ap.get(0).getSurveyID() + "",context);
                            url = context.getString(R.string.SURVEY_BASE_URL) + "approvesurvey/" + ap.get(0).getSurveyID() + "/" + dbData.getUser().getUsername() + "/" + date;
                            task.execute(url, AppConstants.REQ_SEND_APPROVAL, AppConstants.applicationContext, AppConstants.GET, null);
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            SurveyAppModel.getInstance().errorLog.addLog("Survey Found", e.getLocalizedMessage(),context);
        }


        SurveyAppModel.getInstance().forceSync = false;
        isForced = false;
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alert!")
                .setContentText("Unable to sync surveys data to server. Data sync has been delayed for 1 hour, or force sync from the app." +
                        " Please contact authorities if the problem persists.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, mBuilder.build());
    }

    private boolean isDelayed() {
        SharedPreferences preferences = context.getSharedPreferences("delay", context.MODE_PRIVATE);
        boolean isDelayed = preferences.getBoolean("isPostponed", false);
        long syncTime = preferences.getLong("time", 0);
        if (isDelayed) {
            long elapsedTime = (System.currentTimeMillis() - syncTime) / 1000 / 60;
            if (elapsedTime > 120) {
                resetDelay();
                return false;
            } else return true;
        } else {
            return false;
        }
    }

    public void resetDelay() {
        context.getSharedPreferences("delay", context.MODE_PRIVATE).edit().clear().commit();
        retries = 0;
    }
}
