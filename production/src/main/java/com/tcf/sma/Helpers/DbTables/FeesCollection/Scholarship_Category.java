package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ScholarshipCategoryModel;

public class Scholarship_Category {
    public static final String SCHOLARSHIP_CAT = "Scholarship_Category";
    public static final String ID = "scholarship_category_id";
    public static final String SCHOLARSHIP_CATEGORY_DESCRIPTION = "scholarship_category_description";
    public static final String SCHOOL_ID = "school_id";
    public static final String IS_ACTIVE = "is_active";
    public static final String MIN_FEE = "Min_Fee";
    public static final String MAX_FEE = "Max_Fee";
    public static final String FEES_MIDTERMEXAM = "fees_Midtermexam";
    public static final String FEES_FINALEXAM = "fees_finalexam";
    public static final String FEES_ADMISSION = "fees_admission";
    public static final String SCHOLARSHIP_CATEGORY_MODIFIED_ON = "modified_on";
    public static final String CREATE_SCHOLARHSIP_CAT_TABLE = "CREATE TABLE " + SCHOLARSHIP_CAT
            + " (" + ID + "  INTEGER PRIMARY KEY,"
            + SCHOLARSHIP_CATEGORY_DESCRIPTION + "  TEXT ,"
            + IS_ACTIVE + "  INTEGER ,"
            + MIN_FEE + "  REAL ,"
            + MAX_FEE + "  REAL ,"
//            + FEES_MIDTERMEXAM + "  REAL,"
//            + FEES_FINALEXAM + "  REAL ,"
            + SCHOOL_ID + "  INTEGER ,"
            + FEES_ADMISSION + "  REAL,"
            + SCHOLARSHIP_CATEGORY_MODIFIED_ON + " TEXT"+
            " );";
    private static Scholarship_Category instance = null;
    private Context context;

    Scholarship_Category(Context context) {

        this.context = context;
    }

    public static Scholarship_Category getInstance(Context context) {
        if (instance == null)
            instance = new Scholarship_Category(context);
        return instance;
    }

    public static void insertRawSchoolYear(SQLiteDatabase db) {

        String sql = "INSERT INTO " + SCHOLARSHIP_CAT + " ("
                + ID + ","
                + SCHOLARSHIP_CATEGORY_DESCRIPTION + ","
                + SCHOOL_ID + ","
                + IS_ACTIVE + ","
                + MIN_FEE + ","
                + MAX_FEE + ","
                + FEES_MIDTERMEXAM + ","
                + FEES_ADMISSION + ","
                + FEES_FINALEXAM + ") VALUES ";

        sql += "(1,'Regular',1105,1,1500,2000,0,0,0),";
        sql += "(2,'Regular',1112,1,1500,2000,0,0,0),";
        sql += "(3,'40% Scholarship',1105,1,300,600,0,0,0),";
        sql += "(4,'40% Scholarship',1112,1,300,600,0,0,0),";
        sql += "(5,'Full Scholarship',1112,1,0,100,0,0,0),";
        sql += "(6,'Full Scholarship',1105,1,0,100,0,0,0)";

        db.execSQL(sql);
    }

    public long insert_Scholarship_Categories(ScholarshipCategoryModel scm) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(ID, scm.getScholarship_category_id());
            cv.put(SCHOLARSHIP_CATEGORY_DESCRIPTION, scm.getScholarship_category_description());
            cv.put(SCHOOL_ID, scm.getSchool_ID());
            cv.put(IS_ACTIVE, (scm.isIs_active() ? 1 : 0));
            cv.put(MIN_FEE, scm.getMin_Fee());
            cv.put(MAX_FEE, scm.getMax_Fee());
//            cv.put(FEES_MIDTERMEXAM, scm.getFees_Midtermexam());
            cv.put(FEES_ADMISSION, scm.getFees_admission());

            cv.put(SCHOLARSHIP_CATEGORY_MODIFIED_ON, scm.getModified_on());

//            cv.put(FEES_FINALEXAM, scm.getFees_finalexam());

            id = db.insert(SCHOLARSHIP_CAT, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;

    }

    public ScholarshipCategoryModel getScholarshipCategory(int schoolId, int fee) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select * from Scholarship_Category where Scholarship_Category.school_id = @SchoolId and Scholarship_Category.Min_Fee <= @Fee and Scholarship_Category.Max_Fee >=@Fee";
        String desc = "";
        query = query.replace("@Fee", fee + "");
        query = query.replace("@SchoolId", schoolId + "");
        Cursor c;
        ScholarshipCategoryModel model = new ScholarshipCategoryModel();
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                desc = c.getString(c.getColumnIndex(SCHOLARSHIP_CATEGORY_DESCRIPTION));
                model.setScholarship_category_description(desc);
                model.setScholarship_category_id(c.getInt(c.getColumnIndex(ID)));
                model.setMax_Fee(c.getDouble(c.getColumnIndex(MAX_FEE)));
                model.setMin_Fee(c.getDouble(c.getColumnIndex(MIN_FEE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    public String getScholarshipCategoryDescription(int scholarShipCatId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select * from Scholarship_Category where " + ID + "=" + scholarShipCatId;
        String desc = "";
        Cursor c;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                desc = c.getString(c.getColumnIndex(SCHOLARSHIP_CATEGORY_DESCRIPTION));
                return desc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return desc;
    }

    public long update_Scholarship_Categories(ScholarshipCategoryModel scm) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
//            cv.put(ID, scm.getScholarship_category_id());
            cv.put(SCHOLARSHIP_CATEGORY_DESCRIPTION, scm.getScholarship_category_description());
            cv.put(SCHOOL_ID, scm.getSchool_ID());
            cv.put(IS_ACTIVE, (scm.isIs_active() ? 1 : 0));
            cv.put(MIN_FEE, scm.getMin_Fee());
            cv.put(MAX_FEE, scm.getMax_Fee());
//            cv.put(FEES_MIDTERMEXAM, scm.getFees_Midtermexam());
            cv.put(FEES_ADMISSION, scm.getFees_admission());

            cv.put(SCHOLARSHIP_CATEGORY_MODIFIED_ON, scm.getModified_on());

//            cv.put(FEES_FINALEXAM, scm.getFees_finalexam());

            id = db.update(SCHOLARSHIP_CAT, cv,ID + "=" + scm.getScholarship_category_id(),null);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;

    }
}
