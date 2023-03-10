package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SchoolYear {

    public static final String SCHOOL_YEAR_TABLE = "SchoolYear";
    public static final String ID = "id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String ABBREV = "abbrev";
    public static final String EXAM_MIDTERM_START = "exam_midterm_start";
    public static final String EXAM_MIDTERM_END = "exam_midterm_end";
    public static final String FINAL_EXAM_START = "final_exam_start";
    public static final String FINAL_EXAM_END = "final_exam_end";
    public static final String SCHOOL_ID = "school_id";
    public static final String ACADEMIC_SESSION_ID = "academic_session_id";
    public static final String CREATE_SCHOOL_YEAR_TABLE = "CREATE TABLE " + SCHOOL_YEAR_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
            + START_DATE + "  TEXT ,"
            + END_DATE + "  TEXT ,"
            + ABBREV + "  TEXT ,"
            + EXAM_MIDTERM_START + "  TEXT ,"
            + EXAM_MIDTERM_END + "  TEXT ,"
            + FINAL_EXAM_START + "  TEXT ,"
            + FINAL_EXAM_END + "  TEXT ,"
            + SCHOOL_ID + "  INTEGER ,"
            + ACADEMIC_SESSION_ID + "  INTEGER);";
    private static SchoolYear instance = null;
    private Context context;

    SchoolYear(Context context) {

        this.context = context;
    }

    public static SchoolYear getInstance(Context context) {
        if (instance == null)
            instance = new SchoolYear(context);
        return instance;
    }

    public void insertRawSchoolYear(SQLiteDatabase db) {

        String sql = "INSERT INTO " + SCHOOL_YEAR_TABLE + " ("
                + START_DATE + ","
                + END_DATE + ","
                + ABBREV + ","
                + EXAM_MIDTERM_START + ","
                + EXAM_MIDTERM_END + ","
                + FINAL_EXAM_START + ","
                + FINAL_EXAM_END + ","
                + SCHOOL_ID + ","
                + ACADEMIC_SESSION_ID + ") VALUES ";

        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5),";
        sql += "('2018-01-02','2018-12-30','A','2018-03-02','2018-03-10','2018-01-02','2018-01-02','1112',5)";

        db.execSQL(sql);
    }
}
