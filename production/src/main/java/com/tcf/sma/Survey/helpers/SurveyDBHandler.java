package com.tcf.sma.Survey.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tcf.sma.Survey.activities.Project;
import com.tcf.sma.Survey.helpers.DatabaseContext;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.model.ApprovalModel;
import com.tcf.sma.Survey.model.CategoryModel;
import com.tcf.sma.Survey.model.MediaModel;
import com.tcf.sma.Survey.model.POI_Model;
import com.tcf.sma.Survey.model.SurveyDataMediaDownloads;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SurveyDBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 18;

    // Database Name
    private static final String DATABASE_NAME = "DSDS_SURVEY";

    // Contacts table name
    private static final String TABLE_SURVEY = "surveys";
    private static final String TABLE_MEDIA = "media";
    private static final String TABLE_PROJECT = "projects";
    private static final String TABLE_DISTRICT = "districts";
    private static final String TABLE_POI = "poi";
    private static final String TABLE_MEDIA_INFO = "media_download";
    private static final String TABLE_SURVEY_MEDIA = "survey_media";
    private static final String TABLE_APPROVAL = "approve_survey";
    private static final String TABLE_CATEGORIES = "categories";

    // Districts Table Column names
    private static final String KEY_DISTRICT_ID = "id";
    private static final String KEY_DISTRICT_NAME = "district_name";
    private static final String KEY_COUNTRY_ID = "country_name";
    private static final String KEY_DISTANCE_COVERED = "dostance_covered";
    private static final String KEY_DISTANCE_TOTAL = "distance_total";
    private static final String KEY_PROJECT_DISTRICT_ID = "project_id";

    // Contacts Table Columns names
    private static final String KEY_SURVEY_ID = "id";
    private static final String KEY_SURVEY_PROJECT_ID = "project_id";
    private static final String KEY_SURVEY_SERVER_ID = "server_id";
    private static final String KEY_SURVEY_FILE_PATH = "file_path";
    private static final String KEY_SURVEY_USER_ID = "user_id";
    private static final String KEY_SURVEY_STATUS = "status";
    private static final String KEY_SURVEY_DATE_SAVED = "date_saved";
    private static final String KEY_SURVEY_DATE_SYNCED = "date_synced";
    private static final String KEY_SURVEY_LAST_QUESTION = "last_question";
//    private static final String KEY_SURVEY_FILE_TO_SYNC = "file_to_sync";

    private static final String KEY_MEDIA_ID = "id";
    private static final String KEY_MEDIA_PROJECT_ID = "project_id";
    private static final String KEY_MEDIA_SURVEY_ID = "survey_id";
    private static final String KEY_MEDIA_FILE_PATH = "file_path";
    private static final String KEY_MEDIA_DATE_SAVED = "date_saved";
    private static final String KEY_MEDIA_DATE_SYNCED = "date_synced";
    private static final String KEY_MEDIA_STATUS = "status";

    private static final String KEY_PROJECT_ID = "id";
    private static final String KEY_PROJECT_SERVER_ID = "p_server_id";
    private static final String KEY_PROJECT_NAME = "p_name";
    private static final String MAP_PROPERTY = "show_map";
    private static final String BUILDING_PROPERTY = "show_building";
    private static final String CATEGORY_PROPERTY = "show_category";
    private static final String SHOULD_SHOW_SCHOOL = "show_school";
    private static final String SHOULD_SHOW_EMPLOYEE = "show_employee";
    private static final String SHOULD_SHOW_COURSE = "show_course";
    private static final String KEY_PROJECT_DATE_SYNCED = "date_synced";
    private static final String submitted_count = "total";
    private static final String submitted_today = "today";
    private static final String submitted_week = "week";
    private static final String submitted_month = "month";

    private static final String POI_SUBJECT_NO = "sbjnum";
    private static final String POI_PROJECT_ID = "project_id";
    private static final String POI_CATEGORY = "category";
    private static final String POI_TITLE = "title";
    private static final String POI_RESPONDENT = "respondent_name";
    private static final String POI_MOBILE = "mobile_number";
    private static final String POI_LATITUDE = "latitude";
    private static final String POI_LONGITUDE = "longitude";
    private static final String POI_DISTRICT_ID = "districtID";
    private static final String POI_SURVEY_JSON = "survey_json";
    private static final String POI_CREATED_ON = "created_on";
    private static final String POI_SURVEYOR_ID = "surveyor_ID";
    private static final String POI_LOCAL_SURVEY_ID = "lacal_db_survey_ID";
    private static final String POI_QC_STATUS = "QCStatus";

    private static final String MEDIA_PROJECT = "project_id";
    private static final String MEDIA_NAME = "filename";
    private static final String MEDIA_CODE = "filecode";
    private static final String MEDIA_TYPE = "filetype";
    private static final String MEDIA_UPLOAD_DATE = "uploadedon";

    private static final String SURVEY_MEDIA_SURVEYOR = "surveyor_id";
    private static final String SURVEY_MEDIA_PROJECT = "project_id";
    private static final String SURVEY_MEDIA_SBJNUM = "survey_id";
    private static final String SURVEY_FIELD_ID = "field_id";
    private static final String SURVEY_MEDIA_NAME = "filename";
    private static final String SURVEY_MEDIA_TYPE = "filetype";
    private static final String SURVEY_MEDIA_STATUS = "filecode";


    private static final String APPROVAL_SURVEY_ID = "sbjnum";
    private static final String APPROVAL_USER = "project_id";
    private static final String APPROVAL_LATITUDE = "lat";
    private static final String APPROVAL_LONGITUDE = "lng";
    private static final String APPROVAL_REJECT_REASON = "reason_id";
    private static final String APPROVAL_QC_STATUS = "qc";
    private static final String APPROVAL_TIME = "field_id";

    private static final String CATEGORY_FIELD_ID = "field_id";
    private static final String CATEGORY_PROJECT_ID = "project_id";
    private static final String CATEGORY_ID = "id";
    private static final String CATEGORY_TITLE = "title";
    private static final String CATEGORY_CODE = "cat_code";
    private static final String CATEGORY_QOUTA = "cat_qouta";
    private static final String CATEGORY_SURVEYED = "cat_surveyed";
//    private static final String MEDIA_UPLOAD_DATE = "uploadedon";

    Context applicationCtx;

    public SurveyDBHandler(Context context) {
        super(new DatabaseContext(context), DATABASE_NAME, null, DATABASE_VERSION);
        //    context.deleteDatabase(DATABASE_NAME);
        applicationCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SURVEY_TABLE = "CREATE TABLE " + TABLE_SURVEY + "("
                + KEY_SURVEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SURVEY_PROJECT_ID + " INTEGER,"
                + KEY_SURVEY_SERVER_ID + " INTEGER,"
                + KEY_SURVEY_USER_ID + " TEXT,"
                + KEY_SURVEY_FILE_PATH + " TEXT,"
                + KEY_SURVEY_STATUS + " TEXT,"
                + KEY_SURVEY_DATE_SAVED + " TEXT,"
                + KEY_SURVEY_DATE_SYNCED + " TEXT,"
                + KEY_SURVEY_LAST_QUESTION + " INTEGER" + ")";

        String CREATE_MEDIA_TABLE = "CREATE TABLE " + TABLE_MEDIA + "("
                + KEY_MEDIA_ID + " INTEGER PRIMARY KEY,"
                + KEY_MEDIA_PROJECT_ID + " INTEGER,"
                + KEY_MEDIA_SURVEY_ID + " INTEGER,"
                + KEY_MEDIA_FILE_PATH + " TEXT,"
                + KEY_MEDIA_STATUS + " TEXT,"
                + KEY_MEDIA_DATE_SAVED + " TEXT,"
                + KEY_MEDIA_DATE_SYNCED + " TEXT" + ")";

        String CREATE_PROJECT_TABLE = "CREATE TABLE " + TABLE_PROJECT + "("
                + KEY_PROJECT_ID + " INTEGER PRIMARY KEY,"
                + KEY_PROJECT_SERVER_ID + " INTEGER,"
                + KEY_PROJECT_NAME + " TEXT,"
                + MAP_PROPERTY + " TEXT,"
                + BUILDING_PROPERTY + " TEXT,"
                + CATEGORY_PROPERTY + " TEXT,"
                + KEY_PROJECT_DATE_SYNCED + " TEXT,"
                + submitted_count + " TEXT,"
                + submitted_today + " TEXT,"
                + submitted_week + " TEXT,"
                + submitted_month + " TEXT,"
                + SHOULD_SHOW_SCHOOL + " INTEGER,"
                + SHOULD_SHOW_EMPLOYEE + " INTEGER,"
                + SHOULD_SHOW_COURSE + " INTEGER"
                + ")";

        String CREATE_DISTRICT_TABLE = "CREATE TABLE " + TABLE_DISTRICT + "("
                + KEY_DISTRICT_ID + " INTEGER,"
                + KEY_PROJECT_DISTRICT_ID + " INTEGER,"
                + KEY_DISTRICT_NAME + " TEXT,"
                + KEY_COUNTRY_ID + " TEXT,"
                + KEY_DISTANCE_COVERED + " TEXT,"
                + KEY_DISTANCE_TOTAL + " TEXT" + ")";

        String CREATE_POI_TABLE = "CREATE TABLE " + TABLE_POI + "("
                + POI_SUBJECT_NO + " INTEGER  UNIQUE,"
                + POI_PROJECT_ID + " INTEGER,"
                + POI_CATEGORY + " TEXT,"
                + POI_TITLE + " TEXT,"
                + POI_RESPONDENT + " TEXT,"
                + POI_MOBILE + " TEXT,"
                + POI_LATITUDE + " TEXT,"
                + POI_LONGITUDE + " TEXT,"
                + POI_CREATED_ON + " DATETIME,"
                + POI_SURVEYOR_ID + " TEXT,"
                + POI_LOCAL_SURVEY_ID + " INTEGER,"
                + POI_QC_STATUS + " INTEGER,"
                + POI_DISTRICT_ID + " INTEGER,"
                + POI_SURVEY_JSON + " TEXT"
                + ")";

        String CREATE_MEDIA_INFO_TABLE = "CREATE TABLE " + TABLE_MEDIA_INFO + "("
                + MEDIA_PROJECT + " INTEGER,"
                + MEDIA_NAME + " TEXT,"
                + MEDIA_CODE + " TEXT,"
                + MEDIA_TYPE + " TEXT,"
                + MEDIA_UPLOAD_DATE + " DATETIME" + ")";

        String CREATE_SURVEY_MEDIA_TABLE = "CREATE TABLE " + TABLE_SURVEY_MEDIA + "("
                + SURVEY_MEDIA_PROJECT + " INTEGER,"
                + SURVEY_FIELD_ID + " INTEGER,"
                + SURVEY_MEDIA_SURVEYOR + " TEXT,"
                + SURVEY_MEDIA_NAME + " TEXT UNIQUE,"
                + SURVEY_MEDIA_TYPE + " TEXT,"
                + SURVEY_MEDIA_SBJNUM + " INTEGER,"
                + SURVEY_MEDIA_STATUS + " TEXT" + ")";

        String CREATE_APPROVAL_TABLE = "CREATE TABLE " + TABLE_APPROVAL + "("
                + APPROVAL_SURVEY_ID + " INTEGER UNIQUE,"
                + APPROVAL_REJECT_REASON + " INTEGER,"
                + APPROVAL_QC_STATUS + " INTEGER,"
                + APPROVAL_USER + " TEXT,"
                + APPROVAL_LATITUDE + " TEXT,"
                + APPROVAL_LONGITUDE + " TEXT,"
                + APPROVAL_TIME + " DATETIME" + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + CATEGORY_PROJECT_ID + " INTEGER,"
                + CATEGORY_CODE + " INTEGER,"
                + CATEGORY_ID + " INTEGER,"
                + CATEGORY_FIELD_ID + " INTEGER,"
                + CATEGORY_TITLE + " TEXT,"
                + CATEGORY_QOUTA + " INTEGER,"
                + CATEGORY_SURVEYED + " INTEGER" + ")";

        db.execSQL(CREATE_SURVEY_TABLE);
        db.execSQL(CREATE_MEDIA_TABLE);
        db.execSQL(CREATE_PROJECT_TABLE);
        db.execSQL(CREATE_DISTRICT_TABLE);
        db.execSQL(CREATE_POI_TABLE);
        db.execSQL(CREATE_MEDIA_INFO_TABLE);
        db.execSQL(CREATE_SURVEY_MEDIA_TABLE);
        db.execSQL(CREATE_APPROVAL_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_MEDIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPROVAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        // Create tables again
        onCreate(db);
    }

    public long saveNewSurvey(String filePath, String user, String status, int project_id) {

        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SURVEY_USER_ID, user);
        values.put(KEY_SURVEY_PROJECT_ID, project_id);
        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(KEY_SURVEY_STATUS, status);
        values.put(KEY_SURVEY_DATE_SAVED, currentDateandTime);

        long id = db.insert(TABLE_SURVEY, null, values);
        return id;
    }


    public boolean isSurveyExist(int sbjnum) {
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_SURVEY + " WHERE " + KEY_SURVEY_SERVER_ID + " = " + sbjnum;
        int count = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            db.close();

            if (count > 0)
                return true;
            else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long saveNewSyncedSurvey(int sbjnum, String filePath, String user, String status, int project_id) {

        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SURVEY_USER_ID, user);
        values.put(KEY_SURVEY_PROJECT_ID, project_id);
        values.put(KEY_SURVEY_SERVER_ID, sbjnum);
        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(KEY_SURVEY_STATUS, status);
        values.put(KEY_SURVEY_DATE_SAVED, currentDateandTime);

        long id = db.insert(TABLE_SURVEY, null, values);
        return id;
    }

    public Project getSyncedSurvey(int sbjnum) {

        Project prj = null;
        String selectQuery = "SELECT * FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_SERVER_ID + " = " + sbjnum + " and " + KEY_SURVEY_STATUS + " = 'QCP'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                prj = new Project(cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_SURVEY_FILE_PATH)), "false", "false", "false");

            } while (cursor.moveToNext());
        }

        // return project list
        return prj;
    }

    public void updateSurveyData(long id, String filePath, String status, int lastQuestion) {

        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(KEY_SURVEY_STATUS, status);
        values.put(KEY_SURVEY_DATE_SAVED, currentDateandTime);
        values.put(KEY_SURVEY_LAST_QUESTION, lastQuestion);

        int numRows = db.update(TABLE_SURVEY, values, KEY_SURVEY_ID + " = " + id + " and " + KEY_SURVEY_FILE_PATH + " = '" + filePath + "'", null);
        int i;
        i = 0;
    }

    public void updateSurveyStatus(long id, String filePath, String status, String currentDateandTime) {

//		String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(KEY_SURVEY_STATUS, status);
        values.put(KEY_SURVEY_DATE_SAVED, currentDateandTime);
        values.put(KEY_SURVEY_FILE_PATH, filePath);

        int numRows = db.update(TABLE_SURVEY, values, KEY_SURVEY_ID + " = " + id + "", null);
    }

    public void updateMediaStatus(long id, String status) {

//		String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(KEY_MEDIA_STATUS, status);

        int numRows = db.update(TABLE_MEDIA, values, KEY_MEDIA_ID + " = " + id + "", null);
        int i;
        i = 0;
    }

    public List<String> getAllSurveys() {
        List<String> dataList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SURVEY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//            	Data data = new Data();            	
//            	data.setSMSNumber(cursor.getString(0));            	
                // Adding contact to list
                //String b = cursor.getString(5);
                dataList.add(cursor.getString(cursor.getColumnIndex(KEY_SURVEY_FILE_PATH)));
            } while (cursor.moveToNext());
        }

        // return project list
        return dataList;
    }

    public ArrayList<Project> getPartialSurveys(int project_id) {
        ArrayList<Project> dataList = new ArrayList<Project>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_PROJECT_ID + " = " + project_id + " and " + KEY_SURVEY_STATUS + " = 'partial'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//            	Data data = new Data();            	
//            	data.setSMSNumber(cursor.getString(0));            	
                // Adding contact to list
//            	String b = cursor.getString(4);
//            	dataList.add(cursor.getString(3));

//shaz 03/10/2015            	
                dataList.add(new Project(cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_SURVEY_FILE_PATH)), "false", "false", "false"));
            } while (cursor.moveToNext());
        }

        // return project list
        return dataList;
    }

    public long saveMedia(long survey_id, String filePath, String status, int project_id) {

        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEDIA_SURVEY_ID, survey_id);
        values.put(KEY_MEDIA_PROJECT_ID, project_id);
        values.put(KEY_MEDIA_FILE_PATH, filePath);
        values.put(KEY_MEDIA_STATUS, status);
        values.put(KEY_MEDIA_DATE_SAVED, currentDateandTime);

        long id = db.insert(TABLE_MEDIA, null, values);
        return id;
    }

    public List<String> getCompletedSurveys(String type) {
        List<String> dataList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_STATUS + " = 'complete' OR " + KEY_SURVEY_STATUS + " = '" + AppConstants.QC_COMPLETE_STATUS + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (type.equals("ids"))
                    dataList.add(cursor.getString(cursor.getColumnIndex(KEY_SURVEY_ID)));
                else if (type.equals("paths"))
                    dataList.add(cursor.getString(cursor.getColumnIndex(KEY_SURVEY_FILE_PATH)));
            } while (cursor.moveToNext());
        }

        // return project list
        return dataList;
    }

    public List<String> getSyncedDate(int project_id) {
        List<String> dataList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_SURVEY_DATE_SYNCED + " FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_PROJECT_ID + " = " + project_id + " and " + KEY_SURVEY_STATUS + " = 'synced'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                System.out.println("count");
                dataList.add(cursor.getString(cursor.getColumnIndex(KEY_SURVEY_DATE_SYNCED)));
            } while (cursor.moveToNext());
        }

        // return project list
        return dataList;
    }

    public List<String> getMedias(String type) {
        List<String> dataList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEDIA + " where " + KEY_SURVEY_STATUS + " = 'local'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (type.equals("ids"))
                    dataList.add(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_ID)));
                else if (type.equals("paths"))
                    dataList.add(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_FILE_PATH)));
                else if (type.equals("projects"))
                    dataList.add(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_PROJECT_ID)));
            } while (cursor.moveToNext());
        }

        // return project list
        return dataList;
    }

    public void updateSurveySynced(long id, String server_id, String status) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(KEY_SURVEY_STATUS, status);
        values.put(KEY_SURVEY_DATE_SYNCED, currentDateandTime);
        values.put(KEY_SURVEY_SERVER_ID, server_id);

        int numRows = db.update(TABLE_SURVEY, values, KEY_SURVEY_ID + " = " + id + " and " + KEY_SURVEY_STATUS + " = " + " 'complete' OR " + KEY_SURVEY_STATUS + " = '" + AppConstants.QC_COMPLETE_STATUS + "'", null);
        int i;
        i = 0;
    }

    public int getPartialSurveyCount(int project_id) {
//		List<String> dataList = new ArrayList<String>();
        // Select All Query
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_PROJECT_ID + " = " + project_id + " and " + KEY_SURVEY_STATUS + " = 'partial'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        // return project list
        return count;
    }

    public int getCompleteSurveyCount(int project_id) {
//		List<String> dataList = new ArrayList<String>();
        // Select All Query
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_PROJECT_ID + " = " + project_id + " and " + KEY_SURVEY_STATUS + " = 'complete'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        // return project list
        return count;
    }

    public int getSyncedSurveyCount(int project_id) {
//		List<String> dataList = new ArrayList<String>();
        // Select All Query
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_PROJECT_ID + " = " + project_id + " and " + KEY_SURVEY_STATUS + " = 'synced'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        // return project list
        return count;
    }

    public int getServerSurveyId(long local_id) {
//		List<String> dataList = new ArrayList<String>();
        // Select All Query
        int count = 0;
        String selectQuery = "SELECT " + KEY_SURVEY_SERVER_ID + " FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_ID + " = " + local_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_SERVER_ID));
        }

        // return project list
        return count;
    }

    public int getSyncedSurveyId(long serverId) {
//		List<String> dataList = new ArrayList<String>();
        // Select All Query
        int count = 0;
        String selectQuery = "SELECT " + KEY_SURVEY_ID + " FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_SERVER_ID + " = " + serverId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_SERVER_ID));
        }

        // return project list
        return count;
    }

    public void deleteSurvey(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SURVEY_ID, id);
//        values.put(KEY_SURVEY_STATUS, status);
//        values.put(KEY_SURVEY_DATE_SYNCED, currentDateandTime);
//        values.put(KEY_SURVEY_SERVER_ID, server_id);

//        int numRows = db.update(TABLE_SURVEY, values, KEY_SURVEY_ID + " = " + id + " and " + KEY_SURVEY_STATUS + " = " + " 'complete'", null);
        db.delete(TABLE_SURVEY, KEY_SURVEY_ID + " = " + id, null);

    }

    public int getLastQuestionSaved(long local_id) {
        int que = 0;
        String selectQuery = "SELECT " + KEY_SURVEY_LAST_QUESTION + " FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_ID + " = " + local_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            que = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_LAST_QUESTION));
        }

        return que;
    }

    public String getLastSavedDate(long local_id) {
        String date = "";
        String selectQuery = "SELECT " + KEY_SURVEY_DATE_SAVED + " FROM " + TABLE_SURVEY + " where " + KEY_SURVEY_ID + " = " + local_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_DATE_SAVED));
        }

        return date;
    }


    /***************************** Projects Table ******************************/

    public void addProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROJECT_SERVER_ID, project.getKey());
        values.put(KEY_PROJECT_NAME, project.getName());
        values.put(MAP_PROPERTY, project.getShowMapScreen());
        values.put(BUILDING_PROPERTY, project.getShowBuildingScreen());
        values.put(CATEGORY_PROPERTY, project.getNeedCategoryScreen());
        values.put(submitted_count, project.getTotalSurveys());
        values.put(submitted_today, project.getToday());
        values.put(submitted_week, project.getWeeek());
        values.put(submitted_month, project.getMonth());
        values.put(SHOULD_SHOW_SCHOOL, project.isShowSchoolSelection() ? 1 : 0);
        values.put(SHOULD_SHOW_EMPLOYEE, project.isShowEmployeeSelection() ? 1 : 0);
        values.put(SHOULD_SHOW_COURSE, project.isShowCourseSelection() ? 1 : 0);


        db.insert(TABLE_PROJECT, null, values);
    }

    public Project getProject(int key, boolean isSetStack) {
        SQLiteDatabase db = this.getReadableDatabase();
        Project project;

        try {
            Cursor cursor = db.query(TABLE_PROJECT, new String[]{KEY_PROJECT_SERVER_ID, KEY_PROJECT_NAME, BUILDING_PROPERTY, MAP_PROPERTY, submitted_count, submitted_today, submitted_week, submitted_month, CATEGORY_PROPERTY, SHOULD_SHOW_SCHOOL, SHOULD_SHOW_EMPLOYEE, SHOULD_SHOW_COURSE}, KEY_PROJECT_SERVER_ID + "=?",
                    new String[]{String.valueOf(key)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            project = new Project(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)
                    , getBooleanFromInt(cursor.getInt(9)), getBooleanFromInt(cursor.getInt(10)), getBooleanFromInt(cursor.getInt(11)),isSetStack);
        } catch (Exception e) {
            e.printStackTrace();
            project = null;
        }
        return project;
    }

    private boolean getBooleanFromInt(int value) {
        return value == 1;
    }

    public ArrayList<Project> getAllProjects() {
        ArrayList<Project> projectList = new ArrayList<Project>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.set_key(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PROJECT_SERVER_ID))));
                project.set_name(cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NAME)));
                project.setShowMapScreen(cursor.getString(cursor.getColumnIndex(MAP_PROPERTY)));
                project.setShowBuildingScreen(cursor.getString(cursor.getColumnIndex(BUILDING_PROPERTY)));
                project.setNeedCategoryScreen(cursor.getString(cursor.getColumnIndex(CATEGORY_PROPERTY)));
                project.setShowSchoolSelection(cursor.getInt(cursor.getColumnIndex(SHOULD_SHOW_SCHOOL)) == 1);
                project.setShowEmployeeSelection(cursor.getInt(cursor.getColumnIndex(SHOULD_SHOW_EMPLOYEE)) == 1);
                project.setShowCourseSelection(cursor.getInt(cursor.getColumnIndex(SHOULD_SHOW_COURSE)) == 1);
                projectList.add(project);
            } while (cursor.moveToNext());
        }
        db.close();
        // return project list
        return projectList;
    }

    // Getting projects Count
    public int getProjectsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PROJECT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteAllProjects() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROJECT, null, null);
        db.close();
    }


    public void updateProjectCount(int id, String total, String today, String week, String month) {

//		String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_SURVEY_FILE_PATH, filePath);
        values.put(submitted_count, total);
        values.put(submitted_today, today);
        values.put(submitted_week, week);
        values.put(submitted_month, month);

        int numRows = db.update(TABLE_PROJECT, values, KEY_PROJECT_SERVER_ID + " = " + id + "", null);
    }


    public void deleteAllDistricts() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_DISTRICT, null, null);
        db.close();
    }

    public void insertPOI(POI_Model poi) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(POI_SUBJECT_NO, poi.getSbjnum());
            values.put(POI_LOCAL_SURVEY_ID, poi.getLocal_survey_ID());
            values.put(POI_PROJECT_ID, poi.getProject_id());
            values.put(POI_CATEGORY, poi.getCategoryName());
            values.put(POI_TITLE, poi.getTitle());
            values.put(POI_LATITUDE, poi.getLatitude());
            values.put(POI_LONGITUDE, poi.getLongitude());
            values.put(POI_DISTRICT_ID, poi.getDistrict_ID());
            values.put(POI_RESPONDENT, poi.getRespondentName());
            values.put(POI_MOBILE, poi.getRespondentMobile());
            values.put(POI_SURVEY_JSON, poi.getSurveyData());
            values.put(POI_CREATED_ON, poi.getCreated_on());
            values.put(POI_SURVEYOR_ID, poi.getSurveyor_ID());
            values.put(POI_QC_STATUS, poi.getQc_status());

            DB.insert(TABLE_POI, null, values);
//			}
//			else
//			{
//				updatePOI(poi);
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<POI_Model> retrieveMatchingPOI(String text) {
        ArrayList<POI_Model> poi_list = new ArrayList<POI_Model>();

        String selectQuery = "SELECT  * FROM " + TABLE_POI + " WHERE " + POI_SUBJECT_NO + " LIKE '%" + text + "%' OR " + POI_TITLE + " LIKE '%" + text + "%' OR " + POI_CATEGORY + " LIKE '%" + text + "%' OR " + POI_CREATED_ON + " LIKE '%" + text + "%'";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    POI_Model poi = new POI_Model();
                    poi.setSbjnum(cursor.getInt(cursor.getColumnIndex(POI_SUBJECT_NO)));
                    poi.setLocal_survey_ID(cursor.getInt(cursor.getColumnIndex(POI_LOCAL_SURVEY_ID)));
                    poi.setProject_id(cursor.getInt(cursor.getColumnIndex(POI_PROJECT_ID)));
                    poi.setCategoryName(cursor.getString(cursor.getColumnIndex(POI_CATEGORY)));
                    poi.setTitle(cursor.getString(cursor.getColumnIndex(POI_TITLE)));
                    poi.setRespondentName(cursor.getString(cursor.getColumnIndex(POI_RESPONDENT)));
                    poi.setRespondentMobile(cursor.getString(cursor.getColumnIndex(POI_MOBILE)));
                    poi.setLatitude(cursor.getDouble(cursor.getColumnIndex(POI_LATITUDE)));
                    poi.setLongitude(cursor.getDouble(cursor.getColumnIndex(POI_LONGITUDE)));
                    poi.setDistrict_ID(cursor.getInt(cursor.getColumnIndex(POI_DISTRICT_ID)));
                    poi.setSurveyData(cursor.getString(cursor.getColumnIndex(POI_SURVEY_JSON)));
                    poi.setCreated_on(cursor.getString(cursor.getColumnIndex(POI_CREATED_ON)));
                    poi.setSurveyor_ID(cursor.getString(cursor.getColumnIndex(POI_SURVEYOR_ID)));
                    poi.setQc_status(cursor.getInt(cursor.getColumnIndex(POI_QC_STATUS)));
//	        	   if(!chk.equals(""))
//	        		   Boolean.parseBoolean(chk);

                    poi_list.add(poi);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poi_list;
    }


    public ArrayList<POI_Model> retrieveAllProjectsPOI(String project_id) {
        ArrayList<POI_Model> poi_list = new ArrayList<POI_Model>();

        String selectQuery = "SELECT  * FROM " + TABLE_POI + " where " + POI_PROJECT_ID + " = " + project_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                POI_Model poi = new POI_Model();
                poi.setSbjnum(cursor.getInt(cursor.getColumnIndex(POI_SUBJECT_NO)));
                poi.setLocal_survey_ID(cursor.getInt(cursor.getColumnIndex(POI_LOCAL_SURVEY_ID)));
                poi.setProject_id(cursor.getInt(cursor.getColumnIndex(POI_PROJECT_ID)));
                poi.setCategoryName(cursor.getString(cursor.getColumnIndex(POI_CATEGORY)));
                poi.setTitle(cursor.getString(cursor.getColumnIndex(POI_TITLE)));
                poi.setRespondentName(cursor.getString(cursor.getColumnIndex(POI_RESPONDENT)));
                poi.setRespondentMobile(cursor.getString(cursor.getColumnIndex(POI_MOBILE)));
                poi.setLatitude(cursor.getDouble(cursor.getColumnIndex(POI_LATITUDE)));
                poi.setLongitude(cursor.getDouble(cursor.getColumnIndex(POI_LONGITUDE)));
                poi.setDistrict_ID(cursor.getInt(cursor.getColumnIndex(POI_DISTRICT_ID)));
                poi.setSurveyData(cursor.getString(cursor.getColumnIndex(POI_SURVEY_JSON)));
                poi.setCreated_on(cursor.getString(cursor.getColumnIndex(POI_CREATED_ON)));
                poi.setSurveyor_ID(cursor.getString(cursor.getColumnIndex(POI_SURVEYOR_ID)));
                poi.setQc_status(cursor.getInt(cursor.getColumnIndex(POI_QC_STATUS)));
//	        	   if(!chk.equals(""))
//	        		   Boolean.parseBoolean(chk);

                poi_list.add(poi);
            } while (cursor.moveToNext());
        }
        db.close();

        return poi_list;
    }


    public POI_Model retrievePOI(int sbjnum) {
        POI_Model poi = new POI_Model();
        String selectQuery = "SELECT  * FROM " + TABLE_POI + " where " + POI_SUBJECT_NO + " = " + sbjnum;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                poi.setSbjnum(cursor.getInt(cursor.getColumnIndex(POI_SUBJECT_NO)));
                poi.setLocal_survey_ID(cursor.getInt(cursor.getColumnIndex(POI_LOCAL_SURVEY_ID)));
                poi.setProject_id(cursor.getInt(cursor.getColumnIndex(POI_PROJECT_ID)));
                poi.setCategoryName(cursor.getString(cursor.getColumnIndex(POI_CATEGORY)));
                poi.setTitle(cursor.getString(cursor.getColumnIndex(POI_TITLE)));
                poi.setRespondentName(cursor.getString(cursor.getColumnIndex(POI_RESPONDENT)));
                poi.setRespondentMobile(cursor.getString(cursor.getColumnIndex(POI_MOBILE)));
                poi.setLatitude(cursor.getDouble(cursor.getColumnIndex(POI_LATITUDE)));
                poi.setLongitude(cursor.getDouble(cursor.getColumnIndex(POI_LONGITUDE)));
                poi.setDistrict_ID(cursor.getInt(cursor.getColumnIndex(POI_DISTRICT_ID)));
                poi.setSurveyData(cursor.getString(cursor.getColumnIndex(POI_SURVEY_JSON)));
                poi.setCreated_on(cursor.getString(cursor.getColumnIndex(POI_CREATED_ON)));
                poi.setSurveyor_ID(cursor.getString(cursor.getColumnIndex(POI_SURVEYOR_ID)));
                poi.setQc_status(cursor.getInt(cursor.getColumnIndex(POI_QC_STATUS)));
//	        	   if(!chk.equals(""))
//	        		   Boolean.parseBoolean(chk);

            } while (cursor.moveToNext());
        }
        db.close();

        return poi;
    }


    public long retrievePOISbjnum(Long id) {
        int sbjnum = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_POI + " where " + POI_LOCAL_SURVEY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                sbjnum = cursor.getInt(cursor.getColumnIndex(POI_SUBJECT_NO));
//	        	   poi.setLocal_survey_ID(cursor.getInt(cursor.getColumnIndex(POI_LOCAL_SURVEY_ID)));
//	        	   poi.setProject_id(cursor.getInt(cursor.getColumnIndex(POI_PROJECT_ID)));
//	        	   poi.setCategoryName(cursor.getString(cursor.getColumnIndex(POI_CATEGORY)));
//	        	   poi.setTitle(cursor.getString(cursor.getColumnIndex(POI_TITLE)));
//	        	   poi.setRespondentName(cursor.getString(cursor.getColumnIndex(POI_RESPONDENT)));
//	        	   poi.setRespondentMobile(cursor.getString(cursor.getColumnIndex(POI_MOBILE)));
//	        	   poi.setLatitude(cursor.getDouble(cursor.getColumnIndex(POI_LATITUDE)));
//	        	   poi.setLongitude(cursor.getDouble(cursor.getColumnIndex(POI_LONGITUDE)));
//	        	   poi.setDistrict_ID(cursor.getInt(cursor.getColumnIndex(POI_DISTRICT_ID)));
//	        	   poi.setSurveyData(cursor.getString(cursor.getColumnIndex(POI_SURVEY_JSON)));
//	        	   poi.setCreated_on(cursor.getString(cursor.getColumnIndex(POI_CREATED_ON)));
//	        	   poi.setSurveyor_ID(cursor.getString(cursor.getColumnIndex(POI_SURVEYOR_ID)));
//	        	   poi.setQc_status(cursor.getInt(cursor.getColumnIndex(POI_QC_STATUS)));
//	        	   if(!chk.equals(""))
//	        		   Boolean.parseBoolean(chk);

            } while (cursor.moveToNext());
        }
        db.close();

        return sbjnum;
    }


    public String retrieveLatestDate(int project_id) {
        String selectQuery = "SELECT max(" + POI_CREATED_ON + ") FROM " + TABLE_POI + " WHERE " + POI_PROJECT_ID + " = " + project_id;
        String date = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    date = cursor.getString(0);

                    if (date != null) {
                        if (!date.equals("")) {
                            date = date.replace("-", "");
                            date = date.substring(0, date.length() - 9);
                        }
                    } else {
                        date = "";
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }


    public boolean checkPOI(int sbjnum) {
        String selectQuery = "SELECT  * FROM " + TABLE_POI + " where " + POI_SUBJECT_NO + " = " + sbjnum;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            int chk = cursor.getCount();

            if (cursor != null)
                cursor.close();

            if (chk > 0)
                return true;

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePOI(POI_Model poi) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(POI_SUBJECT_NO, poi.getSbjnum());
            values.put(POI_PROJECT_ID, poi.getProject_id());
            values.put(POI_CATEGORY, poi.getCategoryName());
            values.put(POI_TITLE, poi.getTitle());
            values.put(POI_LATITUDE, poi.getLatitude());
            values.put(POI_LONGITUDE, poi.getLongitude());
            values.put(POI_DISTRICT_ID, poi.getDistrict_ID());
            values.put(POI_RESPONDENT, poi.getRespondentName());
            values.put(POI_MOBILE, poi.getRespondentMobile());
            values.put(POI_SURVEY_JSON, poi.getSurveyData());
            values.put(POI_CREATED_ON, poi.getCreated_on());
            values.put(POI_SURVEYOR_ID, poi.getSurveyor_ID());
            values.put(POI_QC_STATUS, poi.getQc_status());

            db.update(TABLE_POI, values, POI_SUBJECT_NO + " = " + poi.getSbjnum(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQCStatus(POI_Model poi) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = 0;
        try {
            if (poi.getQc_status() == 0)
                status = 1;

            ContentValues values = new ContentValues();
            values.put(POI_QC_STATUS, status);

            db.update(TABLE_POI, values, POI_SUBJECT_NO + " = " + poi.getSbjnum(), null);
            Toast.makeText(applicationCtx, "Successful", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(applicationCtx, " Error \n Operation Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void deletePOI(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_POI, POI_SUBJECT_NO + " = " + id, null);
        db.close();
    }

    public void deleteLocalPOI(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_POI, POI_LOCAL_SURVEY_ID + " = " + id, null);
        db.close();
    }

    public void updateLocalPOI(long id, String sbjnum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(POI_SUBJECT_NO, sbjnum);

        db.update(TABLE_POI, values, POI_LOCAL_SURVEY_ID + " = " + id, null);
//	       db.delete(TABLE_POI, POI_LOCAL_SURVEY_ID+" = "+id, null);
        db.close();
    }

    public void deleteAllPOI() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_POI, null, null);
        db.close();
    }


    public void insertMedia(MediaModel mm) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(MEDIA_PROJECT, mm.getProject_id());
            values.put(MEDIA_NAME, mm.getFilename());
            values.put(MEDIA_CODE, mm.getFilecode());
            values.put(MEDIA_TYPE, mm.getFileType());
            values.put(MEDIA_UPLOAD_DATE, mm.getUploadedon());

            DB.insert(TABLE_MEDIA_INFO, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMedia(MediaModel mm) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(MEDIA_CODE, mm.getFilecode());
            values.put(MEDIA_TYPE, mm.getFileType());
            values.put(MEDIA_UPLOAD_DATE, mm.getUploadedon());

            DB.update(TABLE_MEDIA_INFO, values, MEDIA_NAME + " = '" + mm.getFilename() + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean mediaExits(String name) {
        try {
            String selectQuery = "SELECT * FROM " + TABLE_MEDIA_INFO + " where " + MEDIA_NAME + " = '" + name + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            int chk = cursor.getCount();

            if (chk > 0)
                return true;

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String latestMediaDate(String name) {
        String selectQuery = "SELECT max(" + MEDIA_UPLOAD_DATE + ") FROM " + TABLE_MEDIA_INFO + " WHERE " + MEDIA_NAME + " = '" + name + "'";

        String date = "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    date = cursor.getString(0);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (date == null) {
            return "";
        }

        return date;
    }


    public void insertSurveyMedia(SurveyDataMediaDownloads s_media) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(SURVEY_MEDIA_SURVEYOR, s_media.getUserID());
            values.put(SURVEY_FIELD_ID, s_media.getField_id());
            values.put(SURVEY_MEDIA_SBJNUM, s_media.getSbjnum());
            values.put(SURVEY_MEDIA_PROJECT, s_media.getProject());
            values.put(SURVEY_MEDIA_NAME, s_media.getField_value());
            values.put(SURVEY_MEDIA_TYPE, s_media.getType());
            values.put(SURVEY_MEDIA_STATUS, s_media.getStatus());

            DB.insert(TABLE_SURVEY_MEDIA, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSurveyMediaStatus(String s_name, String status) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            values.put(SURVEY_MEDIA_STATUS, status);

            DB.update(TABLE_SURVEY_MEDIA, values, SURVEY_MEDIA_NAME + " = '" + s_name + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isSurveyMediaExists(String name) {
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SURVEY_MEDIA + " where " + SURVEY_MEDIA_NAME + " = '" + name + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            int chk = cursor.getCount();

            if (chk > 0)
                return true;

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String surveyMediaStatus(String name) {
        try {
            String selectQuery = "SELECT " + SURVEY_MEDIA_STATUS + " FROM " + TABLE_SURVEY_MEDIA + " where " + SURVEY_MEDIA_NAME + " = '" + name + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst())
                return cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_STATUS));
            else
                return AppConstants.media_exist_no;
        } catch (Exception e) {
            e.printStackTrace();
            return AppConstants.media_exist_no;
        }
    }


    public ArrayList<SurveyDataMediaDownloads> retrieveSurveyMedia(int project_id) {
        ArrayList<SurveyDataMediaDownloads> media_list = new ArrayList<SurveyDataMediaDownloads>();

        String selectQuery = "SELECT  * FROM " + TABLE_SURVEY_MEDIA + " where " + SURVEY_MEDIA_STATUS + " = '" + AppConstants.media_exist_no + "'" + " AND " + SURVEY_MEDIA_PROJECT + " = " + project_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                SurveyDataMediaDownloads media = new SurveyDataMediaDownloads();
                media.setUserID(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_SURVEYOR)));
                media.setProject(cursor.getInt(cursor.getColumnIndex(SURVEY_MEDIA_PROJECT)));
                media.setField_value(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_NAME)));
                media.setField_id(cursor.getInt(cursor.getColumnIndex(SURVEY_FIELD_ID)));
                media.setType(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_TYPE)));
                media.setStatus(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_STATUS)));

                media_list.add(media);
            } while (cursor.moveToNext());
        }
        db.close();

        return media_list;
    }


    public ArrayList<SurveyDataMediaDownloads> retrievePoiSurveyMedia(int sbjnum) {
        ArrayList<SurveyDataMediaDownloads> media_list = new ArrayList<SurveyDataMediaDownloads>();

        String selectQuery = "SELECT  * FROM " + TABLE_SURVEY_MEDIA;//+" where "+SURVEY_MEDIA_SBJNUM+" = "+sbjnum;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                SurveyDataMediaDownloads media = new SurveyDataMediaDownloads();
                media.setUserID(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_SURVEYOR)));
                media.setSbjnum(cursor.getInt(cursor.getColumnIndex(SURVEY_MEDIA_SBJNUM)));
                media.setProject(cursor.getInt(cursor.getColumnIndex(SURVEY_MEDIA_PROJECT)));
                media.setField_value(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_NAME)));
                media.setField_id(cursor.getInt(cursor.getColumnIndex(SURVEY_FIELD_ID)));
                media.setType(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_TYPE)));
                media.setStatus(cursor.getString(cursor.getColumnIndex(SURVEY_MEDIA_STATUS)));

                media_list.add(media);
            } while (cursor.moveToNext());
        }
        db.close();

        for (int i = 0; i < media_list.size(); i++) {
            if (media_list.get(i).getSbjnum() == 7920731) {
                System.out.println("found");
            }
        }

        return media_list;
    }

    public void insertApproval(ApprovalModel ap) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(APPROVAL_SURVEY_ID, ap.getSurveyID());
            values.put(APPROVAL_LATITUDE, ap.getLatitude());
            values.put(APPROVAL_LONGITUDE, ap.getLongitude());
            values.put(APPROVAL_QC_STATUS, ap.getQc_status());
            values.put(APPROVAL_REJECT_REASON, ap.getReasonID());
            values.put(APPROVAL_USER, ap.getUserID());
            values.put(APPROVAL_TIME, ap.getDate());

            DB.insert(TABLE_APPROVAL, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateApproval(ApprovalModel ap) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(APPROVAL_SURVEY_ID, ap.getSurveyID());

            if (ap.getLatitude() != 0.0 && ap.getLongitude() != 0.0) {
                values.put(APPROVAL_LATITUDE, ap.getLatitude());
                values.put(APPROVAL_LONGITUDE, ap.getLongitude());
            }

            if (ap.getQc_status() != 0)
                values.put(APPROVAL_QC_STATUS, ap.getQc_status());
            values.put(APPROVAL_REJECT_REASON, ap.getReasonID());
            values.put(APPROVAL_USER, ap.getUserID());
            values.put(APPROVAL_TIME, ap.getDate());

            DB.update(TABLE_APPROVAL, values, APPROVAL_SURVEY_ID + " = " + ap.getSurveyID(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLatLong(long sbjnum) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            values.put(APPROVAL_LATITUDE, 0.0);
            values.put(APPROVAL_LONGITUDE, 0.0);


            DB.update(TABLE_APPROVAL, values, APPROVAL_SURVEY_ID + " = " + sbjnum, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateApprovalSbjnum(long sbjnum, String new_sbjnum) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            values.put(APPROVAL_SURVEY_ID, new_sbjnum);

            DB.update(TABLE_APPROVAL, values, APPROVAL_SURVEY_ID + " = " + sbjnum, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ApprovalModel> retrieveApproval() {
        String selectQuery = "SELECT * FROM " + TABLE_APPROVAL;

        ArrayList<ApprovalModel> approval_list = new ArrayList<ApprovalModel>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ApprovalModel ap = new ApprovalModel();
                    ap.setSurveyID(cursor.getInt(cursor.getColumnIndex(APPROVAL_SURVEY_ID)));
                    ap.setReasonID(cursor.getInt(cursor.getColumnIndex(APPROVAL_REJECT_REASON)));
                    ap.setQc_status(cursor.getInt(cursor.getColumnIndex(APPROVAL_QC_STATUS)));
                    ap.setUserID(cursor.getString(cursor.getColumnIndex(APPROVAL_USER)));
                    ap.setDate(cursor.getString(cursor.getColumnIndex(APPROVAL_TIME)));
                    ap.setLatitude(cursor.getDouble(cursor.getColumnIndex(APPROVAL_LATITUDE)));
                    ap.setLongitude(cursor.getDouble(cursor.getColumnIndex(APPROVAL_LONGITUDE)));

                    approval_list.add(ap);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return approval_list;
    }

    public boolean checkApproval(int sbjnum) {
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_APPROVAL + " WHERE " + APPROVAL_SURVEY_ID + " = " + sbjnum;
        int count = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            db.close();

            if (count > 0)
                return true;
            else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int Approval_count() {
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_APPROVAL;
        int count = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public void deleteApproval(long id) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_APPROVAL, APPROVAL_SURVEY_ID + " = " + id, null);
    }

    public void insertCategory(CategoryModel cat) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(CATEGORY_PROJECT_ID, cat.getProject_ID());
            values.put(CATEGORY_TITLE, cat.getTitle());
            values.put(CATEGORY_FIELD_ID, cat.getFieldId());
            values.put(CATEGORY_ID, cat.getId());
            values.put(CATEGORY_QOUTA, cat.getQouta());
            values.put(CATEGORY_SURVEYED, cat.getSurveyed());
            values.put(CATEGORY_CODE, cat.getCode());

            DB.insert(TABLE_CATEGORIES, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<CategoryModel> retrieveCategory(long project_ID) {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + CATEGORY_PROJECT_ID + " = " + project_ID;

        List<CategoryModel> category_list = new ArrayList<CategoryModel>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    CategoryModel cat = new CategoryModel();
                    cat.setProject_ID(cursor.getInt(cursor.getColumnIndex(CATEGORY_PROJECT_ID)));
                    cat.setFieldId(cursor.getInt(cursor.getColumnIndex(CATEGORY_FIELD_ID)));
                    cat.setId(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
                    cat.setSurveyed(cursor.getInt(cursor.getColumnIndex(CATEGORY_SURVEYED)));
                    cat.setTitle(cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE)));
                    cat.setQouta(cursor.getInt(cursor.getColumnIndex(CATEGORY_QOUTA)));
                    cat.setCode(cursor.getInt(cursor.getColumnIndex(CATEGORY_CODE)));

                    category_list.add(cat);

                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category_list;
    }

    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_CATEGORIES, CATEGORY_PROJECT_ID + " = " + id, null);
    }
}