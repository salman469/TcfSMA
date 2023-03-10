package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.Fees_Collection.AttendanceReportTableModel;

import java.util.ArrayList;
import java.util.List;

public class AttendanceReport {

    public static final String ATTENDANCE_REPORT_TABLE = "AttendanceReportTable";
    public static final String ID = "id";
    public static final String SCHOOL_ID = "school_id";
    public static final String THIS_MONTH_ATTENDANCE = "this_month_attendance";
    public static final String MONTH_YEAR = "month_year";
    public static final String SCHOOL_CLASS_ID = "school_class_id";
    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + ATTENDANCE_REPORT_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + SCHOOL_ID + "  INTEGER ,"
            + THIS_MONTH_ATTENDANCE + "  REAL ,"
            + MONTH_YEAR + "  TEXT ,"
            + SCHOOL_CLASS_ID + "  INTEGER );";
    private static AttendanceReport instance = null;
    private Context context;

    AttendanceReport(Context context) {

        this.context = context;
    }

    public static AttendanceReport getInstance(Context context) {
        if (instance == null)
            instance = new AttendanceReport(context);
        return instance;
    }

    /**
     * Added BU Mohammad Haseeb
     *
     * @param db
     */
    public void insertRawHolidayCalendarSchool(SQLiteDatabase db) {

//        String sql = "INSERT INTO " + HOLIDAY_CALENDAR_SCHOOL_TABLE + " ("
//                + SCHOOL_ID + ","
//                + THIS_MONTH_ATTENDANCE + ","
//                + START_DATE + ","
//                + END_DATE + ","
//                + CREATED_ON + ","
//                + CREATED_BY + ","
//                + IS_ACTIVE + ") VALUES ";
//
//        sql += "(1112,'Winter Holidays','20-12-2018','30-12-2018','30-12-2018',5318,1),";
//        sql += "(1112,'Summer Holidays','01-06-2018','30-07-2018','30-12-2018',5318,1)";
//
//        db.execSQL(sql);
    }


    /**
     * Added by Mohammad Haseeb
     * Method to getHolidays from Db
     *
     * @param schoolID
     * @return
     */
    public List<AttendanceReportTableModel> getAttendanceReport(String schoolID) {
        List<AttendanceReportTableModel> hcgmList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query = ""; // todo complete query
        query = query.replace("@SchoolID", schoolID);

        try {

            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    AttendanceReportTableModel model = new AttendanceReportTableModel();

                    model.setSchool_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ID)));
                    model.setSchool_class_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
                    model.setMonth_year(cursor.getString(cursor.getColumnIndex(MONTH_YEAR)));
                    model.setAttendance_this_month(cursor.getDouble(cursor.getColumnIndex(THIS_MONTH_ATTENDANCE)));

                    hcgmList.add(model);
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
        return hcgmList;
    }

}
