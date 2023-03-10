package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.Fees_Collection.AttendanceSummary;

import java.util.ArrayList;
import java.util.List;

public class AttendancePercentage {

    public static final String ATTENDACE_PERCENTAGE_TABLE = "AttendancePercentage";
    public static final String ID = "id";
    public static final String SCHOOL_ID = "school_id";
    public static final String ATTENDANCE_PERCENTAGE = "attendance_percentage";
    public static final String TOTAL_STUDENTS = "total_students";
    public static final String SCHOOL_CLASS_ID = "school_class_id";
    public static final String ACADMEIC_SESSION_ID = "academic_sesison_id";
    public static final String TOTAL_PRESENTS = "total_presents";
    public static final String FOR_DATE = "for_date";
    public static final String CREATE_TABLE_ATTENDANCE_PERCENTAGE = "CREATE TABLE " + ATTENDACE_PERCENTAGE_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + SCHOOL_ID + "  INTEGER ,"
            + ATTENDANCE_PERCENTAGE + "  REAL ,"
            + FOR_DATE + "  TEXT ,"
            + ACADMEIC_SESSION_ID + "  INTEGER ,"
            + SCHOOL_CLASS_ID + "  INTEGER ,"
            + TOTAL_PRESENTS + "  INTEGER ,"
            + TOTAL_STUDENTS + "  INTEGER);";
    private static AttendancePercentage instance = null;
    private Context context;

    AttendancePercentage(Context context) {

        this.context = context;
    }

    public static AttendancePercentage getInstance(Context context) {
        if (instance == null)
            instance = new AttendancePercentage(context);
        return instance;
    }

    public long insertAttendanceSummary(AttendanceSummary model) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(SCHOOL_ID, model.getSchoolId());
            cv.put(SCHOOL_CLASS_ID, model.getSchool_class_id());
            cv.put(ATTENDANCE_PERCENTAGE, model.getAttendancePercentage());
            cv.put(FOR_DATE, model.getForDate());
            cv.put(ACADMEIC_SESSION_ID, model.getAcademic_session_id());
            cv.put(TOTAL_PRESENTS, model.getPresentStudents());
            cv.put(TOTAL_STUDENTS, model.getTotalStudents());

            id = db.insert(ATTENDACE_PERCENTAGE_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL("delete from " + ATTENDACE_PERCENTAGE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL("delete from " + ATTENDACE_PERCENTAGE_TABLE + " where school_id=" + schoolId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AttendanceSummary getAttendancePercentageforThisMonth(int schoolId, int academicSessionId, String currentDate) {
        String query = "select count(ap.id) as classes_count,sum(ap.attendance_percentage) as total_attendance_per\n" +
                "from AttendancePercentage ap where ap.school_id = @SchoolID and ap.academic_sesison_id = @AcademicSessionID and IFNULL(ap.school_class_id, 0) = 0\n" +
                "and ap.for_date = '" + currentDate + "' ";

        query = query.replace("@SchoolID", schoolId + "");
        query = query.replace("@AcademicSessionID", academicSessionId + "");
        Cursor cursor = null;
        SQLiteDatabase db = null;
        AttendanceSummary attendanceSummary = null;
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex("total_attendance_per")) == null) {
                    return null;
                }
                attendanceSummary = new AttendanceSummary();
                attendanceSummary.setTotalAttendancePercentage(cursor.getDouble(cursor.getColumnIndex("total_attendance_per")));
                attendanceSummary.setClasses_count(cursor.getInt(cursor.getColumnIndex("classes_count")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendanceSummary;
    }

    public AttendanceSummary getAttendancePercentageforThisSession(int schoolId, int academicSessionId) {
        String query = "select count(ap.id) as classes_count,sum(ap.attendance_percentage) as total_attendance_per\n" +
                "from AttendancePercentage ap where ap.school_id = @SchoolID and ap.academic_sesison_id = @AcademicSessionID and\n" +
                "IFNULL(ap.school_class_id, 0) = 0 and ap.for_date is null or ''";

        AttendanceSummary attendanceSummary = null;
        query = query.replace("@SchoolID", schoolId + "");
        query = query.replace("@AcademicSessionID", academicSessionId + "");
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex("total_attendance_per")) == null) {
                    return null;
                }
                attendanceSummary = new AttendanceSummary();
                attendanceSummary.setTotalAttendancePercentage(cursor.getDouble(cursor.getColumnIndex("total_attendance_per")));
                attendanceSummary.setClasses_count(cursor.getInt(cursor.getColumnIndex("classes_count")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendanceSummary;
    }

    public List<AttendanceSummary> getAttendancePercentageforSelectedMonth(int schoolId, int schoolClassId, int academicSessionId, String selectedDate) {
        String query = "";
        if (schoolClassId == 0) {
            query = "select ap.* from AttendancePercentage ap inner join school_class sc on sc.id=ap.school_class_id where ap.school_id = @SchoolID and ap.academic_sesison_id = @AcademicSessionID and\n" +
                    "IFNULL(ap.school_class_id, 0) > 0 and ap.for_date = '" + selectedDate + "' and sc.is_active=1 order by sc.class_id";

            query = query.replace("@SchoolID", schoolId + "");
            query = query.replace("@AcademicSessionID", academicSessionId + "");
        } else {
            query = "select ap.* from AttendancePercentage ap inner join school_class sc on sc.id=ap.school_class_id where ap.school_id = @SchoolID and ap.academic_sesison_id = @AcademicSessionID and\n" +
                    "ap.school_class_id = @SchoolClassID and ap.for_date = '" + selectedDate + "' and sc.is_active=1";

            query = query.replace("@SchoolID", schoolId + "");
            query = query.replace("@SchoolClassID", schoolClassId + "");
            query = query.replace("@AcademicSessionID", academicSessionId + "");
        }


        List<AttendanceSummary> list = new ArrayList<>();

        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    AttendanceSummary attendanceSummary = new AttendanceSummary();
                    attendanceSummary.setSchoolId(cursor.getInt(cursor.getColumnIndex(SCHOOL_ID)));
                    attendanceSummary.setAttendancePercentage(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_PERCENTAGE)));
                    attendanceSummary.setForDate(cursor.getString(cursor.getColumnIndex(FOR_DATE)));
                    attendanceSummary.setAcademic_session_id(cursor.getInt(cursor.getColumnIndex(ACADMEIC_SESSION_ID)));
                    attendanceSummary.setSchool_class_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
                    attendanceSummary.setPresentStudents(cursor.getInt(cursor.getColumnIndex(TOTAL_PRESENTS)));
                    attendanceSummary.setTotalStudents(cursor.getInt(cursor.getColumnIndex(TOTAL_STUDENTS)));

                    list.add(attendanceSummary);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
