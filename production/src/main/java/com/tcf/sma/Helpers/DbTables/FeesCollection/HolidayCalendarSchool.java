package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Mohammad Haseeb
 */
public class HolidayCalendarSchool {

    public static final String HOLIDAY_CALENDAR_SCHOOL_TABLE = "HolidayCalendarSchool";
    public static final String ID = "id";
    public static final String SCHOOL_ID = "school_id";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String CREATED_ON = "created_on";
    public static final String CREATED_BY = "created_by";
    public static final String IS_ACTIVE = "is_active";
    public static final String CREATE_HOLIDAY_CALENDAR_SCHOOL_TABLE = "CREATE TABLE " + HOLIDAY_CALENDAR_SCHOOL_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + SCHOOL_ID + "  INTEGER ,"
            + DESCRIPTION + "  TEXT ,"
            + START_DATE + "  TEXT ,"
            + END_DATE + "  TEXT ,"
            + CREATED_ON + "  TEXT ,"
            + CREATED_BY + "  INTEGER ,"
            + IS_ACTIVE + "  BOOLEAN);";
    private static final String GET_ALL_HOLIDAYS = "select id,\"\" as school_id,description,start_date,end_date,created_on,created_by,is_active from HolidayCalendarGlobal where is_active = 1 " +
            "union " +
            "Select id,school_id,description,start_date,end_date,created_on,created_by,is_active from HolidayCalendarSchool where is_active = 1 and school_id IN( @SchoolID)";
    private static HolidayCalendarSchool instance = null;
    private Context context;

    HolidayCalendarSchool(Context context) {

        this.context = context;
    }

    public static HolidayCalendarSchool getInstance(Context context) {
        if (instance == null)
            instance = new HolidayCalendarSchool(context);
        return instance;
    }

    /**
     * Added BU Mohammad Haseeb
     *
     * @param db
     */
    public void insertRawHolidayCalendarSchool(SQLiteDatabase db) {

        String sql = "INSERT INTO " + HOLIDAY_CALENDAR_SCHOOL_TABLE + " ("
                + SCHOOL_ID + ","
                + DESCRIPTION + ","
                + START_DATE + ","
                + END_DATE + ","
                + CREATED_ON + ","
                + CREATED_BY + ","
                + IS_ACTIVE + ") VALUES ";

        sql += "(1112,'Winter Holidays','20-12-2018','30-12-2018','30-12-2018',5318,1),";
        sql += "(1112,'Summer Holidays','01-06-2018','30-07-2018','30-12-2018',5318,1)";

        db.execSQL(sql);
    }


    /**
     * Added by Mohammad Haseeb
     * Method to getHolidays from Db
     *
     * @param schoolID
     * @return
     */
    public List<HolidayCalendarSchoolModel> getAllHolidays(String schoolID) {
        List<HolidayCalendarSchoolModel> hcgmList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query = HolidayCalendarSchool.GET_ALL_HOLIDAYS;
        query = query.replace("@SchoolID", schoolID);

        try {

            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    HolidayCalendarSchoolModel model = new HolidayCalendarSchoolModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(HolidayCalendarSchool.ID)));
                    model.setSchoolId(cursor.getString(cursor.getColumnIndex(HolidayCalendarSchool.SCHOOL_ID)));
                    model.setDescription(cursor.getString(cursor.getColumnIndex(HolidayCalendarSchool.DESCRIPTION)));
                    model.setStartDate(cursor.getString(cursor.getColumnIndex(HolidayCalendarSchool.START_DATE)));
                    model.setEndDate(cursor.getString(cursor.getColumnIndex(HolidayCalendarSchool.END_DATE)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(HolidayCalendarSchool.CREATED_ON)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(HolidayCalendarSchool.CREATED_BY)));

                    boolean isActive = cursor.getString(cursor.getColumnIndex(HolidayCalendarSchool.IS_ACTIVE)).equals("1");
                    model.setActive(isActive);

                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(HolidayCalendarSchool.CREATED_BY)));

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
