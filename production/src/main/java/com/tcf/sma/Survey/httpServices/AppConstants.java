package com.tcf.sma.Survey.httpServices;

import android.content.Context;
import android.os.Environment;

public class AppConstants {


    //public static final String URL_GET_LOCATIONS = "/locations/getLocationsInRectangle"; //POST: latitude, longitude

//    public static final String API_BASE_URL = "http://jira.kcompute.com:28411/api/";
    public static final String API_BASE_URL = "https://sfapi.tcf.org.pk:6443/api/";
    public static final String DSDS_MEDIA_URL = "http://mobility.toyota-indus.com:8004/";
    public static final String API_SURVEY_MEDIA_URL = "http://mobility.toyota-indus.com:8004/Pictures/SurveyMedia/";
    public static final String APP_STORAGE_ROOT = Environment.getExternalStorageDirectory() + "/DSDS/"; // StorageUtil.getSdCardPath(this).getPath() + File.separator + "TCF" + File.separator + "DSDS" + File.separator +
    public static final String APP_MEDIA_ROOT = "SurveyMedia/";
    public static final String APP_SURVEYOR_PICS_ROOT = "pictures/";
    public static final String URL_GET_PROJECTS = "projects/";
    public static final String URL_GET_MEDIA = "GetProjectMediaFiles/";
    public static final String URL_GET_SURVEY_FORM_BY_ID = "project/";
    public static final String URL_POST_SURVEY_FORM = "SendData";
    public static final String URL_POST_SEND_IMAGE = "SendImage";
    public static final String URL_POST_TRACK = "logmylocations";
    public static final String URL_GET_ASSIGNED_SURVEY_DATA = "GetAssignedSurveyData/";
    public static final String URL_GET_SURVEY_PICTURE = "Pictures/";
    public static final String URL_GET_SURVEY_QOUTA = "getcategoryquota/";
    public static final String API_DOMAIN_REPORTS =  "https://sf.tcf.org.pk:2443/";
    public static final String URL_GET_SURVEY_REPORTS =  API_DOMAIN_REPORTS + "Survey/SurveysConductedList/";
    public static final String GET = "GET";
    public static final String POST_JSON_BODY = "POST_JSON_BODY";
    public static final int REQ_GET_LOCATIONS = 7;
    public static final int REQ_POST_SURVEY_FORM = 15;
    public static final int REQ_GET_ALL_DISTRICTS = 20;
    public static final int REQ_GET_PROJECTS = 22;
    public static final int REQ_GET_SURVEY_FORM = 23;
    public static final int REQ_GET_SURVEY_MEDIA = 25;
    public static final int REQ_CHANGE_POSITION = 27;
    public static final int REQ_SEND_REJECTION = 28;
    public static final int REQ_SEND_APPROVAL = 29;
    public static final int REQ_GET_CATEGORIES = 30;
    public static final String APP_VERSION = "0.8.20";
    public static Context applicationContext;
    public static String LANGUAGE = "EN";
    public static String SelectedCountry = "";
    public static boolean syncing = false;
    public static long syncingId = 0;
    //	public static long syncingId_media = 0;
    public static boolean syncing_media = false;
    public static String media_exist_yes = "y";
    public static String media_exist_no = "n";
    public static String media_item_pics = "itemPics";
    public static String media_surveyor_pics = "surveyorPics";
    public static String download_on_demand = "download_on_demand";
    public static String QC_PARTIAL_STATUS = "QCP";
    public static String QC_COMPLETE_STATUS = "QC";
    public static String START_SURVEY_ACTIVITY = "start_new_survey";
}
