package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created By Mohammad Haseeb
 */
public class HolidayCalendarGlobal {


    public static final String HOLIDAY_CALENDAR_GLOBAL_TABLE = "HolidayCalendarGlobal";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String CREATED_ON = "created_on";
    public static final String CREATED_BY = "created_by";
    public static final String IS_ACTIVE = "is_active";
    public static final String CREATE_HOLIDAY_CALENDAR_GLOBAL_TABLE = "CREATE TABLE " + HOLIDAY_CALENDAR_GLOBAL_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + DESCRIPTION + "  TEXT ,"
            + START_DATE + "  TEXT ,"
            + END_DATE + "  TEXT ,"
            + CREATED_ON + "  TEXT ,"
            + CREATED_BY + "  INTEGER ,"
            + IS_ACTIVE + "  BOOLEAN);";
    private static HolidayCalendarGlobal instance = null;
    private Context context;

    HolidayCalendarGlobal(Context context) {

        this.context = context;
    }

    public static HolidayCalendarGlobal getInstance(Context context) {
        if (instance == null)
            instance = new HolidayCalendarGlobal(context);
        return instance;
    }

    /**
     * Added BU Mohammad Haseeb
     *
     * @param db
     */
    public void insertRawHolidayCalendarGlobal(SQLiteDatabase db) {

        String sql = "INSERT INTO " + HOLIDAY_CALENDAR_GLOBAL_TABLE + " ("
                + DESCRIPTION + ","
                + START_DATE + ","
                + END_DATE + ","
                + CREATED_ON + ","
                + CREATED_BY + ","
                + IS_ACTIVE + ") VALUES ";

        sql += "('Winter Holidays','20-12-2018','30-12-2018','30-12-2018',5318,1),";
        sql += "('Summer Holidays','01-06-2018','30-07-2018','30-12-2018',5318,1)";

        db.execSQL(sql);
    }


}
