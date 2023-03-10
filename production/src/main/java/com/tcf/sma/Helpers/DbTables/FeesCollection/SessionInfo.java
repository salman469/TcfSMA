package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.Fees_Collection.SessionInfoModel;

import java.util.ArrayList;
import java.util.List;

public class SessionInfo {

    public static final String ID = "id";
    public static final String SCHOOL_ID = "school_id";
    public static final String TABLE_SESSION_INFO = "SchoolSSRSummary";
    public static final String SESSION_INFO = "session_info";
    public static final String NEW_ADMISSION = "new_admission";
    public static final String RE_ADMISSION = "re_admission";
    public static final String WITHDRAWL = "withdrawl";
    public static final String GRADUATE = "graduate";
    public static final String TRANSFER = "transfer";
    public static final String CREATE_TABLE_SESSION_INFO = "CREATE TABLE " + TABLE_SESSION_INFO + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SESSION_INFO + " VARCHAR,"
            + SCHOOL_ID + " INTEGER,"
            + NEW_ADMISSION + " INTEGER,"
            + TRANSFER + " INTEGER,"
            + RE_ADMISSION + " INTEGER,"
            + WITHDRAWL + " INTEGER,"
            + GRADUATE + " INTEGER)";
    private static SessionInfo instance = null;
    private Context context;

    SessionInfo(Context context) {

        this.context = context;
    }

    public static SessionInfo getInstance(Context context) {
        if (instance == null)
            instance = new SessionInfo(context);
        return instance;
    }

    public void insertSessionInfoDummy(SQLiteDatabase db) {

        String sql = "INSERT INTO " + TABLE_SESSION_INFO + " ("
                + ID + ","
                + SESSION_INFO + ","
                + SCHOOL_ID + ","
                + NEW_ADMISSION + ","
                + TRANSFER + ","
                + RE_ADMISSION + ","
                + WITHDRAWL + ","
                + GRADUATE + ") VALUES ";

        sql += "(1,'this month',3,13,0,5,6),";
        sql += "(2,'this session',3,3,20,25,6),";
        sql += "(3,'Prev session',3,13,30,25,6),";
        sql += "(4,'this month',1105,13,20,5,26),";
        sql += "(5,'this session',1105,1,10,35,6),";
        sql += "(6,'Prev session',1105,1,9,2,1),";
        sql += "(1,'this month',989,13,0,5,6),";
        sql += "(2,'this session'989,3,20,25,6),";
        sql += "(3,'Prev session',989,13,30,25,6)";

        db.execSQL(sql);
    }

    public long insertSessionInfo(SessionInfoModel sessionInfoModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(SCHOOL_ID, sessionInfoModel.getSchoolid());
            cv.put(SESSION_INFO, sessionInfoModel.getSessionInfo());
            cv.put(NEW_ADMISSION, sessionInfoModel.getNewAdmission());
            cv.put(TRANSFER, sessionInfoModel.getTransfers());
            cv.put(RE_ADMISSION, sessionInfoModel.getReAdmmission());
            cv.put(WITHDRAWL, sessionInfoModel.getWithdrawl());
            cv.put(GRADUATE, sessionInfoModel.getGraduate());

            id = db.insert(TABLE_SESSION_INFO, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public List<SessionInfoModel> getSessionInfoForDashboard(int schoolId) {
        List<SessionInfoModel> armList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query;

        if (schoolId <= 0)
            query = "select id,sum(new_admission) as new_admission,school_id,\n" +
                    "        sum(transfer) as transfer,\n" +
                    "        sum(re_admission) as re_admission,\n" +
                    "        sum(withdrawl) as withdrawl,\n" +
                    "        sum(graduate) as graduate,\n" +
                    "        session_info as session_info\n" +
                    "        from SchoolSSRSummary\n" +
                    "        group by session_info order by id ASC";
        else
            query = "select * from " + TABLE_SESSION_INFO + " where " + SCHOOL_ID + "=" + schoolId + " order by id ASC";

        Log.d("Session Info query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SessionInfoModel model = new SessionInfoModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setSessionInfo(cursor.getString(cursor.getColumnIndex(SESSION_INFO)));
                    model.setGraduate(cursor.getInt(cursor.getColumnIndex(GRADUATE)));
                    model.setNewAdmission(cursor.getInt(cursor.getColumnIndex(NEW_ADMISSION)));
                    model.setReAdmmission(cursor.getInt(cursor.getColumnIndex(RE_ADMISSION)));
                    model.setTransfers(cursor.getInt(cursor.getColumnIndex(TRANSFER)));
                    model.setWithdrawl(cursor.getInt(cursor.getColumnIndex(WITHDRAWL)));
                    if (model.getSessionInfo() != null && !model.getSessionInfo().equals(""))
                        armList.add(model);
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return armList;
    }


    public void delete(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL("delete from " + TABLE_SESSION_INFO + " where school_id=" + schoolId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL("delete from " + TABLE_SESSION_INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropSessionInfoTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table " + TABLE_SESSION_INFO;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSessionInfoTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_SESSION_INFO);
    }
}
