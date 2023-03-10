package com.tcf.sma.Survey.model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.SystemClock;
import android.util.Log;

import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Survey.activities.DataHandler;
import com.tcf.sma.Survey.activities.Project;
import com.tcf.sma.Survey.activities.ProjectsActivity;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.activities.User;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.httpServices.SyncSurveys;
import com.tcf.sma.Survey.logs.ErrorLog;
import com.tcf.sma.Survey.logs.InfoLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SurveyAppModel {

    public static ArrayList<MediaModel> media_list = new ArrayList<MediaModel>();
    public static ArrayList<SurveyDataMediaDownloads> survey_media_list = new ArrayList<SurveyDataMediaDownloads>();
    public static long survey_id;
    public static ArrayList<SurveyDataMediaDownloads> media_download_list = new ArrayList<SurveyDataMediaDownloads>();
    public static Activity activity_media_download;
    public static int media_download_index = 0;
    public static SurveyDBHandler dbSurvey;
    public static DataHandler dataHandler;
    public static ArrayList<Project> projects = new ArrayList<Project>();
    public static User user;
    private static SurveyAppModel instance;
    public String DEVICE_ID = "";
    public String countryDetected = "";
    public boolean forceSync = false;
    public ErrorLog errorLog = new ErrorLog();
    public InfoLog infoLog = new InfoLog();
    public Boolean isSurveySyncStarted = false;
    public int syncedSurveyCount = 0;
    public int syncedMediaLimit = 0;
    public double currentLat = 0.0;
    public double currentLng = 0.0;
    public boolean isGPSDataSyncing = false;
    public boolean isDataSyncing = false;
    public List<DistrictCoordinates> tracks_list = new ArrayList<DistrictCoordinates>();
    private SharedPreferences sp_geoLat, sp_geoLng, sp_project;
    public Project selectedProject;

    private SurveyAppModel() {

    }

    public static SurveyAppModel getInstance() {
        if (instance == null) {
            instance = new SurveyAppModel();
        }
        return instance;
    }

    public static boolean storeImage(Bitmap imageData, String filename, String path, Context context) {
        File sdIconStorageDir = new File(SurveyAppModel.getInstance().getSurveyPath(context) + path);
        if (!sdIconStorageDir.exists() || !sdIconStorageDir.isDirectory()) {
            sdIconStorageDir.mkdirs();
        }

        try {
            /*String filePath = filename;*/
            String filePath = SurveyAppModel.getInstance().getSurveyPath(context) + path + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//			FileOutputStream fileOutputStream = getInstance().applicationContext.openFileOutput(filename, Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            imageData.compress(CompressFormat.JPEG, 85, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        return true;
    }


    public void registerSync(Context context) {
        Intent i = new Intent(context, SyncSurveys.class);

        PendingIntent sender = PendingIntent.getBroadcast(context, 555, i, PendingIntent.FLAG_IMMUTABLE);
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 10 * 1000;//start 10 seconds after first register.

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                15 * 60 * 1000, sender);//15min interval
    }

    public void setSelectedProject(Context context, String pID) {
        sp_project = context.getSharedPreferences("lastProject", Context.MODE_PRIVATE);

        Editor editor = sp_project.edit();
        editor.putString("lastProject", pID);
        editor.commit();
    }

    public String getSelectedProject(Context context) {
        sp_project = context.getSharedPreferences("lastProject", Context.MODE_PRIVATE);
        String pID = sp_project.getString("lastProject", "");

        if (pID == null)
            return "";
        else
            return pID;
    }


    public void setCoordinates(Context context, double latitude, double longitude) {
        sp_geoLat = context.getSharedPreferences("latitude", Context.MODE_PRIVATE);
        sp_geoLng = context.getSharedPreferences("longitude", Context.MODE_PRIVATE);

        Editor editor = sp_geoLat.edit();
        editor.putString("latitude", latitude + "");
        editor.commit();

        Editor editor1 = sp_geoLng.edit();
        editor1.putString("longitude", longitude + "");
        editor1.commit();

    }

    public String getLatitude() {
        String lat = "0.0";
        try {
            sp_geoLat = AppConstants.applicationContext.getSharedPreferences("latitude", Context.MODE_PRIVATE);
            lat = sp_geoLat.getString("latitude", "");

            if (lat == null || lat.equals("")) {
                return "0.0";
            }
        } catch (Exception e) {
            return "0.0";
        }
        return lat;
    }

    public String getLongitude() {
        String lng = "0.0";
        try {
            sp_geoLng = AppConstants.applicationContext.getSharedPreferences("longitude", Context.MODE_PRIVATE);
            lng = sp_geoLng.getString("longitude", "");

            if (lng == null || lng.equals("")) {
                return "0.0";
            }
        } catch (Exception e) {
            return "0.0";
        }
        return lng;
    }


    public void onRequestSuccess(String json, int request_code, Context mContext) {


        if (!ProjectsActivity.isProjectCancelled) {
            switch (request_code) {

                case AppConstants.REQ_GET_PROJECTS:

                    String totalSurveys = "", today = "", week = "", month = "";
                    if (json != null) {
                        if (!json.equals("")) {
                            json = json.replace("\\u002715\\", "").toString();
                            json = json.replace("\\\"", "\"");
                            json = json.substring(1, json.length() - 1);
                            try {

                                dbSurvey.deleteAllProjects();

                                JSONArray arrayProjects = new JSONArray(json);

                                projects.clear();
                                List<String> myList = new ArrayList<String>();
                                String showCategoryScreen = "0";

                                for (int j = 0; j < arrayProjects.length(); j++) {

                                    // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
                                    JSONObject jo = arrayProjects.getJSONObject(j);

                                    int key = jo.getInt("id");
                                    String value = jo.getString("name");
                                    String showBuildingScreen = jo.getString("NeedBuildingSearch");
                                    String showMapScreen = jo.getString("NeedMapInput");
                                    if (jo.has("NeedCategoryInput"))
                                        showCategoryScreen = jo.getString("NeedCategoryInput");
                                    totalSurveys = jo.getString("Total");
                                    today = jo.getString("TotalDay");
                                    week = jo.getString("TotalWeek");
                                    month = jo.getString("TotalMonth");

                                    myList.add(value);

                                    // CONVERT DATA FIELDS TO Project OBJECT
                                    Project prj = new Project(key, value, showBuildingScreen, showMapScreen, totalSurveys, today, week, month, showCategoryScreen,
                                            jo.getBoolean("showSchoolSelection"), jo.getBoolean(("showEmployeeSelection")), jo.getBoolean("showCourseSelection"), true
                                    );
//                                    if (jo.has("showSchoolSelection"))
//                                        prj.setShowSchoolSelection(jo.getBoolean("showSchoolSelection"));
//                                    if (jo.has("showEmployeeSelection"))
//                                        prj.setShowEmployeeSelection(jo.getBoolean("showEmployeeSelection"));
//                                    if (jo.has("showCourseSelection"))
//                                        prj.setShowCourseSelection(jo.getBoolean("showCourseSelection"));
                                    projects.add(prj);

                                    dbSurvey.addProject(prj);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                                SurveyAppModel.getInstance().errorLog.addLog("Invalid Project List", e.getLocalizedMessage(),mContext);
                            }

                        }
                    }

                    break;


                case AppConstants.REQ_GET_SURVEY_FORM:

                    if (ProjectsActivity.sync_project_index < projects.size()) {
                        int pID = projects.get(ProjectsActivity.sync_project_index).get_key();
                        String directory = getSurveyPath(mContext) +"projects";
                        String path = directory + "/prj_";

                        try {
                            if (pID != 0)
                                createFile(String.valueOf(pID), json, directory, path);
                        } catch (Exception e) {
                            e.printStackTrace();
                            SurveyAppModel.getInstance().errorLog.addLog("Error Creating Survey Form Files", e.getLocalizedMessage(),mContext);
                        }
                    }

                    try {
                        if (json != null) {
                            if (!json.equals("")) {
                                json = json.replace("\\\"", "\"").toString();
                                json = json.replace("\\\\" + "r", " ").toString();
                                json = json.replace("\\\\" + "n", " ").toString();
                                json = json.replace("\\\\" + "u", "\\u").toString();
                                json = json.substring(1, json.length() - 1);
                            }
                        }

                        JSONArray jaArray = new JSONArray(json);

                        survey_media_list.clear();

                        for (int i = 0; i < jaArray.length(); i++) {
                            JSONObject jObject = jaArray.getJSONObject(i);

                            if (jObject.has("FieldType")) {
                                if (jObject.getString("FieldType").trim().equals("PIC")) {
                                    SurveyDataMediaDownloads s_media = new SurveyDataMediaDownloads();
                                    if (jObject.has("Id"))
                                        s_media.setField_id(jObject.getInt("Id"));
                                    if (jObject.has("FieldType"))
                                        s_media.setType(jObject.getString("FieldType"));
                                    s_media.setProject(projects.get(ProjectsActivity.sync_project_index).get_key());
                                    survey_media_list.add(s_media);
                                }
                            }
                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        SurveyAppModel.getInstance().errorLog.addLog("Error Parsing Survey Form Media Details", e1.getLocalizedMessage(),mContext);
                    }

//					surveyFormEnded();

                    break;

                case AppConstants.REQ_GET_LOCATIONS:

                    if (json != null && !json.equals("")) {
                        json = json.replace("\\\"", "\"");
                        json = json.replace("\\\\\"", "\"");
                        json = json.replace("\"[{", "[{");
                        json = json.replace("}]\"", "}]");
                    }

                    JSONObject jsonObj;
                    JSONArray ja;

                    try {
                        ja = new JSONArray(json);
                        for (int j = 0; j < ja.length(); j++) {

                            try {
                                jsonObj = ja.getJSONObject(j);

                                POI_Model poi = new POI_Model();
                                if (jsonObj.has("sbjnum")) poi.setSbjnum(jsonObj.getInt("sbjnum"));
                                if (poi.getSbjnum() == 7924896)
                                    System.out.println("Watch");//TEST
                                if (jsonObj.has("DistrictId")) {
                                    String dist_id = jsonObj.getString("DistrictId");

                                    if (dist_id != null && !dist_id.equals("null") && !dist_id.equals(""))
                                        poi.setDistrict_ID(jsonObj.getInt("DistrictId"));
                                    else
                                        poi.setDistrict_ID(-1);
                                }
                                if (jsonObj.has("CreatedOnDate"))
                                    poi.setCreated_on(jsonObj.getString("CreatedOnDate"));
                                if (jsonObj.has("SurveyorId"))
                                    poi.setSurveyor_ID(jsonObj.getString("SurveyorId"));
                                if (jsonObj.has("Latitude"))
                                    poi.setLatitude(jsonObj.getDouble("Latitude"));
                                if (jsonObj.has("Longitude"))
                                    poi.setLongitude(jsonObj.getDouble("Longitude"));
                                if (jsonObj.has("CategoryName"))
                                    poi.setCategoryName(jsonObj.getString("CategoryName"));
                                if (jsonObj.has("Title")) poi.setTitle(jsonObj.getString("Title"));
                                if (jsonObj.has("RespondentName"))
                                    poi.setRespondentName(jsonObj.getString("RespondentName"));
                                if (jsonObj.has("RespondentMobile"))
                                    poi.setRespondentMobile(jsonObj.getString("RespondentMobile"));
                                if (jsonObj.has("QCStatus"))
                                    poi.setQc_status(jsonObj.getInt("QCStatus"));
                                poi.setProject_id(projects.get(ProjectsActivity.sync_project_index).get_key());

                                if (jsonObj.has("SurveyData")) {
                                    poi.setSurveyData(jsonObj.getString("SurveyData"));

                                    JSONArray surveyArray = new JSONArray(jsonObj.getString("SurveyData"));  //LATER

                                    for (int i = 0; i < surveyArray.length(); i++) {

                                        JSONObject fieldObject = surveyArray.getJSONObject(i);

                                        if (fieldObject.has("FieldId")) {
                                            int field_id = fieldObject.getInt("FieldId");

                                            for (int k = 0; k < SurveyAppModel.survey_media_list.size(); k++) {
                                                if (field_id == SurveyAppModel.survey_media_list.get(k).getField_id() && projects.get(ProjectsActivity.sync_project_index).get_key() == SurveyAppModel.survey_media_list.get(k).getProject()) {
                                                    if (fieldObject.has("FieldValue")) {
                                                        String media_name = "";
                                                        media_name = fieldObject.getString("FieldValue");

                                                        SurveyDataMediaDownloads sm = new SurveyDataMediaDownloads();
                                                        sm.setSbjnum(poi.getSbjnum());
                                                        sm.setProject(projects.get(ProjectsActivity.sync_project_index).get_key());
                                                        sm.setUserID(user.getUsername());
                                                        sm.setType(SurveyAppModel.survey_media_list.get(k).getType());
                                                        sm.setField_id(field_id);
                                                        sm.setStatus(AppConstants.media_exist_yes);
                                                        sm.setField_value(fieldObject.getString("FieldValue"));

                                                        if (!media_name.equals("null") && media_name != null && !media_name.equals("")) {
                                                            File dir = new File(SurveyAppModel.getInstance().getSurveyPath(mContext) + "pictures/");
                                                            if (!dir.exists())
                                                                dir.mkdirs();

                                                            File img = new File(dir + "/" + media_name);
                                                            if (img.exists()) {
                                                                if (!dbSurvey.isSurveyMediaExists(media_name)) {
                                                                    dbSurvey.insertSurveyMedia(sm);
                                                                }

                                                                if (dbSurvey.isSurveyMediaExists(media_name) && dbSurvey.surveyMediaStatus(media_name).equals(AppConstants.media_exist_no)) {
                                                                    dbSurvey.updateSurveyMediaStatus(media_name, AppConstants.media_exist_yes);
                                                                }
                                                            } else {
                                                                if (!dbSurvey.isSurveyMediaExists(media_name)) {
                                                                    sm.setStatus(AppConstants.media_exist_no);
                                                                    dbSurvey.insertSurveyMedia(sm);
                                                                } else {

                                                                    dbSurvey.updateSurveyMediaStatus(media_name, AppConstants.media_exist_no);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }

                                if (!dbSurvey.checkPOI(poi.getSbjnum())) {
                                    dbSurvey.insertPOI(poi);
                                } else {
                                    dbSurvey.updatePOI(poi);
                                }
                                System.out.println(j);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                SurveyAppModel.getInstance().errorLog.addLog("Error Parsing POI Data", e.getLocalizedMessage(),mContext);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        SurveyAppModel.getInstance().errorLog.addLog("Error Parsing POI Data", e.getLocalizedMessage(),mContext);
                    }

                    break;


                case AppConstants.REQ_GET_CATEGORIES:

                    if (ProjectsActivity.sync_project_index < projects.size()) {
                        try {
                            if (json != null && !json.equals("")) {
                                json = json.replace("\\", "");
                                json = json.substring(1, json.length() - 1);

                                JSONArray jArray = new JSONArray(json);

                                if (jArray.length() > 0)
                                    dbSurvey.deleteCategory(projects.get(ProjectsActivity.sync_project_index).get_key());

                                for (int i = 0; i < jArray.length(); i++) {

                                    JSONObject joObject = jArray.getJSONObject(i);

                                    CategoryModel cat = new CategoryModel();
                                    cat.setProject_ID(projects.get(ProjectsActivity.sync_project_index).get_key());
                                    if (joObject.has("FieldId"))
                                        cat.setFieldId(joObject.getInt("FieldId"));
                                    if (joObject.has("Id")) cat.setId(joObject.getInt("Id"));
                                    if (joObject.has("Title"))
                                        cat.setTitle(joObject.getString("Title"));
                                    if (joObject.has("Code")) cat.setCode(joObject.getInt("Code"));
                                    if (joObject.has("Quota"))
                                        cat.setQouta(joObject.getInt("Quota"));
                                    if (joObject.has("Surveyed"))
                                        cat.setSurveyed(joObject.getInt("Surveyed"));

                                    dbSurvey.insertCategory(cat);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            SurveyAppModel.getInstance().errorLog.addLog("Error Parsing Districts Data", e.getLocalizedMessage(),mContext);
                        }
                    }

                    break;

                case AppConstants.REQ_GET_SURVEY_MEDIA:

                    try {
                        if (json != null) {
                            if (!json.equals("")) {
                                json = json.substring(1, json.length() - 1);
                                json = json.replace("\\", "");
                            }
                        }

                        ja = new JSONArray(json);
                        for (int j = 0; j < ja.length(); j++) {

                            jsonObj = ja.getJSONObject(j);

                            MediaModel mm = new MediaModel();
                            mm.setProject_id(projects.get(ProjectsActivity.sync_project_index).get_key());
                            if (jsonObj.has("filename"))
                                mm.setFilename(jsonObj.getString("filename"));
                            if (jsonObj.has("filecode"))
                                mm.setFilecode(jsonObj.getString("filecode"));
                            if (jsonObj.has("filetype"))
                                mm.setFileType(jsonObj.getString("filetype"));
                            if (jsonObj.has("uploadedon"))
                                mm.setUploadedon(jsonObj.getString("uploadedon"));

                            SurveyAppModel.media_list.add(mm);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        SurveyAppModel.getInstance().errorLog.addLog("Error Parsing Survey Media Json", e.getLocalizedMessage(),mContext);
                    }

//					surveyMediaEnded();
                    break;
//				}
//		}
//		else
//		{
//			projects.get(sync_project_index).setCurrent_download("Cancelled");
//			projects.get(sync_project_index).setProgress(100);
//			isProjectCancelled = false;
//			pa.notifyDataSetChanged();
//			
//			sync_project_index++;
//			surveyFormStarted();
//
            }
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

    public String getTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-YYY HH:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));

        return date.format(currentLocalTime);
    }

    public String getSurveyPath(Context context){
        if (context == null)
            context = AppConstants.applicationContext;
        return StorageUtil.getSdCardPath(context).getPath() + File.separator + "TCF" + File.separator + "DSDS" + File.separator;
    }

}
