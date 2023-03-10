package com.tcf.sma.Survey.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tcf.sma.Survey.model.GPSData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DataHandler extends SQLiteOpenHelper {
    public static final String ProjectId = "_projectId";
    public static final String number = "_number";
    public static final String messageResponse = "_response";
    public static final String messageSendTime = "send_time";
    public static final String messageRecieveTime = "recieve_time";
    public static final String uploadTime = "upload_time";
    // Database Version
    private static final int DATABASE_VERSION = 9;
    // Database Name
    private static final String DATABASE_NAME = "dataManager";
    // Contacts table name
    private static final String TABLE_DATA = "data";
    private static final String TABLE_USER = "user";
    private static final String TABLE_GPS = "gps";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SMS_NUMBER = "sms_number";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String login_status = "login_status";
    private static final String KEY_GPS_LAT = "gps_lat";
    private static final String KEY_GPS_LON = "gps_lon";
    private static final String KEY_GPS_DATETIME = "gps_datetime";
    private static final String KEY_GPS_SYNC_STATUS = "gps_sync_status";
    private static final String KEY_GPS_ACCURACY = "accuracy";
    private static final String KEY_GPS_SPEED = "speed";
    private static final String KEY_GPS_SYNC_DATETIME = "gps_sync_datetime";
    private static final String TABLE_SMS = "sms";
    String CREATE_SMSRESPONSE = "CREATE TABLE " + TABLE_SMS + "("
            + ProjectId + " INTEGER, "
            + number + " TEXT, "
            + messageResponse + " TEXT, "
            + messageSendTime + " TEXT, "
            + messageRecieveTime + " TEXT, "
            + uploadTime + " TEXT " +
            " )";

    public DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //    context.deleteDatabase(DATABASE_NAME);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SMS_NUMBER + " TEXT" + ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY, " + KEY_USER_NAME + " TEXT, " + KEY_USER_PASSWORD + " TEXT, " + login_status + " TEXT )";


        String CREATE_GPS_TABLE = "CREATE TABLE " + TABLE_GPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_GPS_LAT + " TEXT, "
                + KEY_GPS_LON + " TEXT, "
                + KEY_GPS_DATETIME + " TEXT, "
                + KEY_GPS_SYNC_STATUS + " TEXT, "
                + KEY_GPS_SYNC_DATETIME + " TEXT,"
                + KEY_GPS_SPEED + " TEXT,"
                + KEY_GPS_ACCURACY + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_GPS_TABLE);
        db.execSQL(CREATE_SMSRESPONSE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // Adding new project

    // Getting single contact

    // Getting All Projects

    // Getting All Projects

    // Getting projects Count
    public int getProjectsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single contact


    // Deleting all project's data
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DATA, null, null);
        db.close();
    }

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUsername());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(login_status, user.getStatus());

        db.insert(TABLE_USER, null, values);
        db.close();
        Log.d("addUser", "user added " + user.getUsername());
    }


    // Getting user
    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();

        User user;

        try {
            Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USER_NAME, KEY_USER_PASSWORD, login_status}, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                db.close();
                Log.d("getUser", "got user " + cursor.getString(0));
                Log.d("getUser", "got user status " + cursor.getString(cursor.getColumnIndex(login_status)));
                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(cursor.getColumnIndex(login_status)));
                cursor.close();
                return user;
            } else {
                user = new User("", "", "");
                Log.d("getUser", "failed getting user");
                return user;
            }
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("error: " + e);
            user = new User("", "", "");
            Log.d("getUser", "exception getting user");

            return user;

        }

//        return user;
    }

    // checking if user exit
    public String checkUser() {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USER_NAME}, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                db.close();
                Log.d("checkUser", "user available");
                return cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
            } else {
                Log.d("getUser", "user not available");
                return "login failed";
            }
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("error: " + e);
            Log.d("getUser", "exception checking user");
            return "login failed";

        }
    }


    // Deleting all users data
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
    }


    public void saveGPSLocation(String lat, String lon, String acu, String speed) {
        SQLiteDatabase db = this.getWritableDatabase();

        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        ContentValues values = new ContentValues();
        values.put(KEY_GPS_LAT, lat);
        values.put(KEY_GPS_LON, lon);
        values.put(KEY_GPS_DATETIME, currentDateandTime);
        values.put(KEY_GPS_SYNC_STATUS, "local");
        values.put(KEY_GPS_ACCURACY, acu);
        values.put(KEY_GPS_SPEED, speed);

        long id = db.insert(TABLE_GPS, null, values);
        if (id == -1) {
            //SurveyAppModel.getInstance().errorLog.addLog("DataHandler.saveGPSLocation", "error saving gps data in local database. Received data: latitude: "+lat+", longitude: "+lon);

        }
        db.close();
    }

    public ArrayList<GPSData> getGPSData() {
        ArrayList<GPSData> gpsData = new ArrayList<GPSData>();

        String selectQuery = "select * from " + TABLE_GPS + " where " + KEY_GPS_SYNC_STATUS + " = 'local'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
//        		gpsData.add(new GPSData(cursor.getLong(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_GPS_LAT)), cursor.getString(cursor.getColumnIndex(KEY_GPS_LON))));
                GPSData data = new GPSData();
                data.id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
                data.latitude = cursor.getString(cursor.getColumnIndex(KEY_GPS_LAT));
                data.longitude = cursor.getString(cursor.getColumnIndex(KEY_GPS_LON));
                data.saveDate = cursor.getString(cursor.getColumnIndex(KEY_GPS_DATETIME));
                data.syncStatus = cursor.getString(cursor.getColumnIndex(KEY_GPS_SYNC_STATUS));
                data.accuracy = cursor.getString(cursor.getColumnIndex(KEY_GPS_ACCURACY));
                gpsData.add(data);
            }
            while (cursor.moveToNext());
        }

        return gpsData;
    }

    public void insertSmsRecord(ContentValues values) {
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.insert(TABLE_SMS, null, values);
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateSmsRecord(ContentValues values, String n) {
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.update(TABLE_SMS, values, number + " = '" + n + "' AND " + messageResponse + " IS NULL", null);
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean smsExists(String number, int projectID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor;
            String query = "SELECT * FROM " + TABLE_SMS + " WHERE " + number + " = '" + number + "' AND "
                    + ProjectId + " = " + projectID + " AND " + messageResponse + " IS NULL";
            cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteGPSRow(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GPS, KEY_ID + " = " + id, null);
    }

}
