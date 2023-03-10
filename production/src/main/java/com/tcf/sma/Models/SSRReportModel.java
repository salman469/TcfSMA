package com.tcf.sma.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tcf.sma.Helpers.DatabaseHelper;

public class SSRReportModel {
    private String class_section_name;
    private String month, year;
    private double mMale, mFemale, mOverall;
    private Activity activity;


    public SSRReportModel(Activity activity) {
        this.activity = activity;
    }

    public SSRReportModel(double male, double female, double overall) {
        setmMale(male);
        setmFemale(female);
        setmOverall(overall);
    }

    public String getClass_section_name() {
        return class_section_name;
    }

    public void setClass_section_name(String class_section_name) {
        this.class_section_name = class_section_name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getmMale() {
        return mMale;
    }

    public void setmMale(double mMale) {
        this.mMale = mMale;
    }

    public double getmFemale() {
        return mFemale;
    }

    public void setmFemale(double mFemale) {
        this.mFemale = mFemale;
    }

    public double getmOverall() {
        return mOverall;
    }

    public void setmOverall(double mOverall) {
        this.mOverall = mOverall;
    }

    public SSRReportModel getViewSSRTableDataUsingClassSection(int schoolId, int schoolclass_id, String fromDate, String toDate, int filter) {
        Cursor cursor = null;
        int male = 0, female = 0;
        String sqlquery = "";
        SSRReportModel SSRByGender = null;

        String GET_NEW_ADMISSION = "Select count(s1.enrollment_date) AS 'count', \n" +
                "upper(s1.student_gender) as Gender \n" +
                "FROM student s1 \n" +
                "inner join school_class on s1.schoolclass_id = school_class.id and school_class.school_id = @SchoolId\n" +
                "WHERE s1.enrollment_date >= '@fromDate 00:00:00 AM' and s1.enrollment_date <= '@toDate 23:59:59 PM' \n" +
                "and  s1.schoolclass_id=" + schoolclass_id +
                " GROUP BY s1.student_gender COLLATE NOCASE";

        String GET_WITHDRAWALS = "Select count(s1.withdrawn_on) AS 'count', \n" +
                "upper(s1.student_gender) as Gender\n" +
                "FROM student s1\n" +
                "inner join school_class on s1.schoolclass_id = school_class.id and school_class.school_id = @SchoolId\n" +
                "WHERE  s1.is_withdrawl = 1 and s1.withdrawn_on >= '@fromDate 00:00:00 AM' and s1.withdrawn_on <= '@toDate 23:59:59 AM' \n" +
                "and  s1.schoolclass_id=" + schoolclass_id +
                " GROUP BY s1.student_gender COLLATE NOCASE";

        String GET_GRADUATES = "Select count(s1.withdrawn_on) AS 'count', \n" +
                "upper(s1.student_gender) as Gender\n" +
                "FROM student s1\n" +
                "inner join school_class on s1.schoolclass_id = school_class.id \n" +
                "and school_class.school_id = @SchoolId\n" +
                "WHERE  s1.withdrawal_reason_id = 14\tand\n" +
                "s1.withdrawn_on >= '@fromDate 00:00:00 AM' \n" +
                "and s1.withdrawn_on <= '@toDate 23:59:59 AM' \n" +
                "and  s1.schoolclass_id=" + schoolclass_id +
                " GROUP BY s1.student_gender COLLATE NOCASE";


        if (filter == 1) sqlquery = GET_NEW_ADMISSION;
        else if (filter == 2) sqlquery = GET_WITHDRAWALS;
        else if (filter == 3) sqlquery = GET_GRADUATES;


        sqlquery = sqlquery.replace("@SchoolId", schoolId + "");
        sqlquery = sqlquery.replace("@fromDate", fromDate);
        sqlquery = sqlquery.replace("@toDate", toDate);

        SQLiteDatabase db = DatabaseHelper.getInstance(activity).getDB();
        Log.d(filter + " query", sqlquery.toString());

        try {
            cursor = db.rawQuery(sqlquery, null);

            if (cursor.moveToFirst()) {
                do {
                    int val = cursor.getInt(cursor.getColumnIndex(
                            "count"));
                    if (cursor.getString(cursor.getColumnIndex("Gender")).equals("M")) {
                        male = val;
                    } else {
                        female = val;
                    }

                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                SSRByGender = new SSRReportModel(male, female, (male + female));
                cursor.close();
            }
        }

        return SSRByGender;
    }
}
