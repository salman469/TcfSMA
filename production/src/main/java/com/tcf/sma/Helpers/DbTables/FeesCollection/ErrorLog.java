package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.ErrorResponseModel;
import com.tcf.sma.Models.StudentErrorFetchModel;
//import com.tcf.sma.Survey.httpServices.AppConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ErrorLog {
    public static final String ERROR_LOG_TABLE = "ErrorLog";
    public static final String ID = "id";
    public static final String STUDENT_ID = "student_id";
    public static final String ERROR_MESSAGE = "message";
    public static final String ERROR_CREATED_ON = "created_on";
    public static final String ERROR_CODE = "code";
    public static final String ACTIVITY = "activity";
    public static final String STUDENT_NAME = "student_name";
    public static final String SCHOOLCLASS_ID = "school_class_id";
    public static final String GR_NO = "gr_no";
    public static final String CREATE_ERROR_LOG_TABLE = "CREATE TABLE " + ERROR_LOG_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + STUDENT_ID + "  INTEGER ,"
            + ERROR_MESSAGE + "  TEXT ,"
            + ERROR_CREATED_ON + "  TEXT ,"
            + ACTIVITY + " TEXT,"
            + STUDENT_NAME + " TEXT,"
            + SCHOOLCLASS_ID + " TEXT,"
            + GR_NO + " TEXT,"
            + ERROR_CODE + "  INTEGER );";
    private static ErrorLog instance = null;
    private Context context;

    public ErrorLog() {
    }

    ErrorLog(Context context) {

        this.context = context;
    }

    public static ErrorLog getInstance(Context context) {
        if (instance == null)
            instance = new ErrorLog(context);
        return instance;
    }

    public long insertErrorResponse(ErrorResponseModel erm) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(STUDENT_ID, erm.getStudentId());
            cv.put(ERROR_MESSAGE, erm.getMessage());
            cv.put(ERROR_CREATED_ON, erm.getCreated_on());
            cv.put(ERROR_CODE, erm.getCode());
            cv.put(ACTIVITY, erm.getActivityOfError());
            cv.put(SCHOOLCLASS_ID, erm.getSchoolClass_id());
            cv.put(STUDENT_NAME, erm.getName());
            cv.put(GR_NO, erm.getGr_no());

            id = db.insert(ERROR_LOG_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;

    }


    public int getNoOfFailedRecords() {
        int count = 0;

        SQLiteDatabase db;
        Cursor cursor;
        String query = "select count(*) as errors from " + ERROR_LOG_TABLE;
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("errors"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }


    public void getEnrollemntRecord(int id, StudentErrorFetchModel model) {

        SQLiteDatabase db;
        Cursor cursor;
        String query = "select * from " + ERROR_LOG_TABLE + " where id =" + id;

        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {

                model.setStudentName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                model.setStudentGr(cursor.getString(cursor.getColumnIndex(GR_NO)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<StudentErrorFetchModel> getFailedRecords() {
        ArrayList<StudentErrorFetchModel> list = new ArrayList<>();

        SQLiteDatabase db;
        Cursor cursor;
        String query = "select s.student_name as studentName,s.student_gr_no as grNo,\n" +
                "            (c.name || ' - ' || se.name) as classSection, e.message as message,e.id,e.student_id,e.created_on \n" +
                "    from ErrorLog e left join student s on s.id = e.student_id\n" +
                "    left join school_class sc on s.schoolClass_id = sc.id\n" +
                "    left join Class c  on sc.class_id =  c.id\n" +
                "    left join Section se  on sc.section_id=  se.id";

        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    StudentErrorFetchModel model = new StudentErrorFetchModel();

                    model.setStudentName(cursor.getString(cursor.getColumnIndex("studentName")));
                    model.setStudentGr(cursor.getString(cursor.getColumnIndex("grNo")));
                    model.setClassSection(cursor.getString(cursor.getColumnIndex("classSection")));
                    model.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                    model.setCreated_on(cursor.getString(cursor.getColumnIndex("created_on")));

                    int student_id = cursor.getInt(cursor.getColumnIndex("student_id"));
                    int record_id = cursor.getInt(cursor.getColumnIndex("id"));


                    if (student_id <= 0)
                        getEnrollemntRecord(record_id, model);

                    list.add(model);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteStudentErrorLogTable() {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL("delete from " + ERROR_LOG_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void addLog(String caller, String message) {
//        FileWriter writer;
//        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
//        String datetime = s.format(new Date());
//        try {
//            writer = new FileWriter(getLogFile(), true);
//            writer.append(datetime + ": " + caller + ": " + message + ", \n");
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            // TODO - popup message on screen including message description and caller with OK button
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    private File getLogFile() {
//        SimpleDateFormat s = new SimpleDateFormat("yyMMdd");
//        String datetime = s.format(new Date());
//
//        String filename = AppConstants.APP_STORAGE_ROOT + "Log/error_" + datetime + ".log";
//
//        if (new File(filename).exists()) {
//            return new File(filename);
//        } else {
//            File ImgDir = new File(AppConstants.APP_STORAGE_ROOT + "Log/");
//            if (!ImgDir.exists() || !ImgDir.isDirectory()) {
//                ImgDir.mkdirs();
//            }
//            return new File(filename);
//        }
//    }
}
