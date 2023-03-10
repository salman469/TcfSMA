package com.tcf.sma.Helpers.SyncProgress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusMasterModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusModel;
import com.tcf.sma.Models.SyncProgress.PendingSyncModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;

import java.util.ArrayList;
import java.util.List;

public class SyncProgressHelperClass {
    private static SyncProgressHelperClass instance = null;
    private Context context;
    public static final int SYNC_TYPE_FIRST_SYNC_ID = 1;
    public static final int SYNC_TYPE_BAU_SYNC_ID = 2;
    public static final int SYNC_TYPE_MANUAL_SYNC_ID = 3;
    public static final int SYNC_TYPE_AUTO_SYNC_ID = 4;
    public static final int SYNC_TYPE_SAVE_SYNC_ID = 6;

    public static SyncProgressHelperClass getInstance(Context context) {
        if (instance == null) {
            instance = new SyncProgressHelperClass(context);
        }
        return instance;
    }

    public SyncProgressHelperClass(Context context) {
        this.context = context;
    }

    public List<PendingSyncModel> getPendingSyncList(String schoolIds) {
        List<PendingSyncModel> pendingSyncModelList = new ArrayList<>();
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        PendingSyncModel pendingSyncModel = null;

        Cursor cursor = null;
        try {
            //Enrollment
            String selectQuery = "SELECT name,school_id,created_on from enrollment \n" +
                    "where (uploaded_on is null or uploaded_on = \"\") and school_id IN (@SchoolID) and review_status == 'C' \n" +
                    "GROUP BY id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Enrollment", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("created_on")),
                            "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Attendance
            selectQuery = "SELECT st.student_name,a.created_on,sc.school_id,a.modified_on from attendance a\n" +
                    "INNER JOIN school_class sc on sc.id = a.school_class_id \n" +
                    "INNER JOIN student_attendance sa on sa.attendance_id = a.id\n" +
                    "INNER JOIN student st on st.id = sa.student_id\n" +
                    "WHERE (a.uploaded_on is null or a.uploaded_on = \"\" ) and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY a.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Attendance", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), cursor.getString(cursor.getColumnIndex("created_on"))
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Promotion
            selectQuery = "SELECT st.student_name,p.created_on,sc.school_id from promotion p\n" +
                    "INNER JOIN school_class sc on sc.id = p.school_class_id \n" +
                    "INNER JOIN promotion_student ps on ps.promotion_id = p.id\n" +
                    "INNER JOIN student st on st.id = ps.student_id\n" +
                    "WHERE (p.uploaded_on is null or p.uploaded_on = \"\") and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY p.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Promotion", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), cursor.getString(cursor.getColumnIndex("created_on"))
                            , "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Student
            selectQuery = "SELECT st.student_name,st.modified_on,sc.school_id from student st\n" +
                    "INNER JOIN school_class sc on sc.id = st.schoolclass_id  \n" +
                    "where (st.uploaded_on is null or st.uploaded_on = \"\") and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY st.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Student", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Fee Header
            selectQuery = "SELECT st.student_name,fh.created_on,fh.modified_on,sc.school_id from FeesHeader fh\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id\n" +
                    "INNER JOIN student st on st.id = fh.student_id  \n" +
                    "where (fh.uploaded_on is null or fh.uploaded_on = \"\") and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY fh.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Fee Header", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), cursor.getString(cursor.getColumnIndex("created_on"))
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Cash Deposit
            selectQuery = "SELECT created_on,modified_on,school_id from CashDeposit  where (uploaded_on is null or uploaded_on = \"\") and school_id IN (@SchoolID) \n" +
                    "GROUP BY id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Cash Deposit", cursor.getInt(cursor.getColumnIndex("school_id")),
                            "", cursor.getString(cursor.getColumnIndex("created_on"))
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Employee details
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,emp.modified_on,emp_school.SchoolID from EmployeeDetails emp\n" +
                    "INNER JOIN EmployeeSchool emp_school on emp_school.emp_detail_id = emp.id \n" +
                    "where (emp.uploaded_on is null or emp.uploaded_on = \"\" ) and emp_school.SchoolID IN (@SchoolID) \n" +
                    "GROUP BY emp.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Employee Details", cursor.getInt(cursor.getColumnIndex("SchoolID")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Employee Leaves
            selectQuery = "SELECT emp_leaves.createdOn_app,(emp.first_name || ' ' || emp.last_name) as emp_name,emp_leaves.SchoolID from EmployeesLeaves emp_leaves\n" +
                    "INNER JOIN EmployeeDetails emp on emp.id =  emp_leaves.emp_detail_id\n" +
                    "where (emp_leaves.uploaded_on is null or emp_leaves.uploaded_on = \"\" ) and emp_leaves.SchoolID IN (@SchoolID) \n" +
                    "GROUP BY emp_leaves.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Employee Leaves", cursor.getInt(cursor.getColumnIndex("SchoolID")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), cursor.getString(cursor.getColumnIndex("createdOn_app"))
                            , "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Employee Resignation
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,emp_resg.schoolId,emp_resg.createdOnApp,emp_resg.modified_on from EmployeeResignation emp_resg\n" +
                    "INNER JOIN EmployeeDetails emp on emp.id =  emp_resg.emp_detail_id\n" +
                    "where (emp_resg.uploaded_on is null or emp_resg.uploaded_on = \"\" ) and emp_resg.schoolId IN (@SchoolID) \n" +
                    "GROUP BY emp_resg.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Employee Resignations", cursor.getInt(cursor.getColumnIndex("schoolId")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), cursor.getString(cursor.getColumnIndex("createdOnApp"))
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Employee Pending Separations
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,ps.modified_on from PendingSeparations ps\n" +
                    "INNER JOIN EmployeeResignation emp_resg on emp_resg.server_id = ps.Resignation_id\n" +
                    "INNER JOIN EmployeeDetails emp on emp.id =  emp_resg.emp_detail_id\n" +
                    "where (ps.uploaded_on is null or ps.uploaded_on = \"\")\n" +
                    "GROUP BY ps.id";

//            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,emp_school.SchoolID,ps.modified_on from PendingSeparations ps\n" +
//                    "INNER JOIN EmployeeResignation emp_resg on emp_resg.server_id = ps.Resignation_id\n" +
//                    "INNER JOIN EmployeeSchool emp_school on emp_school.emp_detail_id = emp_resg.emp_detail_id\n" +
//                    "INNER JOIN EmployeeDetails emp on emp.id =  emp_resg.emp_detail_id\n" +
//                    "where (ps.uploaded_on is null or ps.uploaded_on = \"\") and emp_school.SchoolID IN (@SchoolID) \n" +
//                    "GROUP BY ps.id";

//            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Employee Pending Separations", 0,
                            cursor.getString(cursor.getColumnIndex("emp_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Employee Teacher Attendance
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,emp_attend.SchoolID,emp_attend.createdOn_app from EmployeeTeacherAttendance emp_attend \n" +
                    "INNER JOIN EmployeeDetails emp on emp.id =  emp_attend.emp_detail_id\n" +
                    "where (emp_attend.uploaded_on is null or emp_attend.uploaded_on = \"\" ) and emp_attend.SchoolID IN (@SchoolID) \n" +
                    "GROUP BY emp_attend.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Employee Teacher Attendance", cursor.getInt(cursor.getColumnIndex("SchoolID")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), cursor.getString(cursor.getColumnIndex("createdOn_app"))
                            , "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Student Image
            selectQuery = "SELECT st.student_name,st.modified_on,sc.school_id from student st\n" +
                    "join school_class sc on sc.id = st.schoolclass_id  \n" +
                    "where (ifnull(st.image_file_uploaded_on,'') == '') AND (ifnull(st.image_file_name,'') != '' \n" +
                    "AND st.image_file_name not like '%BodyPart%') and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY st.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Student Image", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //TCT Employee Subject Tagging
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,tct_est.SchoolID,tct_est.Modified_On from TCT_EMP_SUBJECT_TAGGING tct_est\n" +
                    "INNER JOIN EmployeeDetails emp on emp.id =  tct_est.userID  \n" +
                    "where (tct_est.Uploaded_On is null or tct_est.Uploaded_On = \"\") and tct_est.SchoolID IN (@SchoolID) \n" +
                    "GROUP BY tct_est.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("TCT Employee Subject Tagging", cursor.getInt(cursor.getColumnIndex("SchoolID")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), ""
                            , cursor.getString(cursor.getColumnIndex("Modified_On")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Separation Images
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,emp_resg.schoolId,emp_resg.createdOnApp from SeparationImages sep_images \n" +
                    "INNER JOIN EmployeeResignation emp_resg on emp_resg.id = sep_images.Resignation_id\n" +
                    "INNER JOIN EmployeeDetails emp on emp.id =  emp_resg.emp_detail_id\n" +
                    "where (sep_images.uploaded_on is null or sep_images.uploaded_on = \"\" ) and emp_resg.schoolId IN (@SchoolID) \n" +
                    "GROUP BY sep_images.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Separation Image", cursor.getInt(cursor.getColumnIndex("schoolId")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), cursor.getString(cursor.getColumnIndex("createdOnApp"))
                            , "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Feedback
            selectQuery = "SELECT (usr.first_name || ' ' || usr.last_name) as usr_name,f.createdOn_app from Feedback f\n" +
                    "INNER JOIN user usr on usr.id =  f.createdBy\n" +
                    "where (f.uploaded_On is null or f.uploaded_On = \"\")\n" +
                    "GROUP BY f.id";

            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Feedback", 0,
                            cursor.getString(cursor.getColumnIndex("usr_name")),
                            cursor.getString(cursor.getColumnIndex("createdOn_app")),
                            "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Transaction
            selectQuery = "SELECT (usr.first_name || ' ' || usr.last_name) || '(' || et.jv_no || ')' as usr_name,et.createdOn_app from ExpenseTransactions et\n" +
                    "INNER JOIN user usr on usr.id =  et.modifiedby\n" +
                    "where (et.uploaded_on is null or et.uploaded_on = \"\")\n" +
                    "GROUP BY et.id";

            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Expense Transaction", 0,
                            cursor.getString(cursor.getColumnIndex("usr_name")),
                            cursor.getString(cursor.getColumnIndex("createdOn_app")),
                            "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Transaction Images
            selectQuery = "SELECT (tr.jv_no) as jvno,tr.school_id,tr.createdOn_App from ExpenseTransactionImages tr_images \n" +
                    "INNER JOIN ExpenseTransactions tr on tr.id = tr_images.transaction_id\n" +
                    "where (tr_images.uploaded_on is null or tr_images.uploaded_on = \"\" ) and tr.school_id IN (@SchoolID) \n" +
                    "GROUP BY tr_images.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Transaction Images", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("jvno")), cursor.getString(cursor.getColumnIndex("createdOn_App"))
                            , "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //SubheadLimitsMonthly
            selectQuery = "SELECT (usr.first_name || ' ' || usr.last_name) || '(' || sh.id || ')' as usr_name,esml.modifiedby from ExpenseSubHeadLimitsMonthly esml\n" +
                    "INNER JOIN user usr on usr.id =  esml.modifiedby\n" +
                    "INNER JOIN ExpenseSubHead sh on sh.id =  esml.subhead_id\n" +
                    "where (esml.uploaded_on is null or esml.uploaded_on = \"\")\n" +
                    "GROUP BY esml.id";

            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Expense Transaction", 0,
                            cursor.getString(cursor.getColumnIndex("usr_name")),
                            cursor.getString(cursor.getColumnIndex("modifiedby")),
                            "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //SchoolPettyCashLimitsMonthly
            selectQuery = "SELECT (usr.first_name || ' ' || usr.last_name) as usr_name,espml.school_id,espml.modifiedby from ExpenseSchoolPettyCashMonthlyLimits espml\n" +
                    "INNER JOIN user usr on usr.id =  espml.modifiedby\n" +
                    "where (espml.uploaded_on is null or espml.uploaded_on = \"\")\n" +
                    "GROUP BY espml.id";

            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Expense Transaction", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("usr_name")),
                            cursor.getString(cursor.getColumnIndex("modifiedby")),
                            "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //User Images
            selectQuery = "SELECT (emp.first_name || ' ' || emp.last_name) as emp_name,emp_school.SchoolID from User_Images ui\n" +
                    "INNER JOIN EmployeeDetails emp on emp.id = ui.emp_detail_id \n" +
                    "INNER JOIN EmployeeSchool emp_school on emp_school.emp_detail_id = ui.emp_detail_id \n" +
                    "where (ui.uploaded_on is null or ui.uploaded_on = \"\" ) and emp_school.SchoolID IN (@SchoolID) \n" +
                    "GROUP BY ui.emp_detail_id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("User Images", cursor.getInt(cursor.getColumnIndex("SchoolID")),
                            cursor.getString(cursor.getColumnIndex("emp_name")), ""
                            , "");

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Student Parents DeathCertificate Image
            selectQuery = "SELECT st.student_name,st.modified_on,sc.school_id from student st\n" +
                    "join school_class sc on sc.id = st.schoolclass_id  \n" +
                    "where (ifnull(st."+DatabaseHelper.getInstance(context).STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON+",'') == '') AND (ifnull(st."+DatabaseHelper.getInstance(context).STUDENT_DEATH_CERT_IMAGE+",'') != '' \n" +
                    "AND st."+DatabaseHelper.getInstance(context).STUDENT_DEATH_CERT_IMAGE+" not like '%BodyPart%') and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY st.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Student Parents Death Certificate Image", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Student BForm Image
            selectQuery = "SELECT st.student_name,st.modified_on,sc.school_id from student st\n" +
                    "join school_class sc on sc.id = st.schoolclass_id  \n" +
                    "where (ifnull(st."+DatabaseHelper.getInstance(context).STUDENT_BFORM_IMAGE_UPLOADED_ON+",'') == '') AND (ifnull(st."+DatabaseHelper.getInstance(context).STUDENT_BFORM_IMAGE+",'') != '' \n" +
                    "AND st."+DatabaseHelper.getInstance(context).STUDENT_BFORM_IMAGE+" not like '%BodyPart%') and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY st.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Student BForm Image", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

            //Student MedicalCertificate Image
            selectQuery = "SELECT st.student_name,st.modified_on,sc.school_id from student st\n" +
                    "join school_class sc on sc.id = st.schoolclass_id  \n" +
                    "where (ifnull(st."+DatabaseHelper.getInstance(context).STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON+",'') == '') AND (ifnull(st."+DatabaseHelper.getInstance(context).STUDENT_MEDICAL_CERT_IMAGE+",'') != '' \n" +
                    "AND st."+DatabaseHelper.getInstance(context).STUDENT_MEDICAL_CERT_IMAGE+" not like '%BodyPart%') and sc.school_id IN (@SchoolID) \n" +
                    "GROUP BY st.id";

            selectQuery = selectQuery.replace("@SchoolID", schoolIds);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    pendingSyncModel = new PendingSyncModel("Student Medical Certificate Image", cursor.getInt(cursor.getColumnIndex("school_id")),
                            cursor.getString(cursor.getColumnIndex("student_name")), ""
                            , cursor.getString(cursor.getColumnIndex("modified_on")));

                    pendingSyncModelList.add(pendingSyncModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return pendingSyncModelList;
    }

    public final String ID = "statsId";
    public final String SYNC_MASTER_ID = "syncId";
    private final String ModuleID = "moduleId";
    private final String SubModule = "subModule";
    private final String StartedOn = "startedOn";
    private final String EndedOn = "endedOn";
    private final String Duration = "duration";
    private final String Downloaded = "downloaded";
    private final String Uploaded = "uploaded";
    private final String CreatedOn = "createdOn";
    public final String UploadedOn = "uploadedOn";

    public String TABLE_APP_SYNC_STATUS = "AppSyncStatus";
    public String CREATE_TABLE_APP_SYNC_STATUS = "CREATE TABLE IF NOT EXISTS " + TABLE_APP_SYNC_STATUS + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SYNC_MASTER_ID + " INTEGER, " +
            ModuleID + " INTEGER, " +
            SubModule + " TEXT, " +
            StartedOn + " TEXT, " +
            EndedOn + " TEXT, " +
            Duration + " INTEGER, " +
            Downloaded + " REAL, " +
            Uploaded + " REAL, " +
            CreatedOn + " TEXT," +
            UploadedOn + " TEXT" +
            ");";

    private final String SyncTypeID = "syncTypeID";
    private final String Longitude = "longitude";
    private final String Latitude = "latitude";
    private final String DeviceId = "deviceId";
    private final String SIM1 = "SIM1";
    public final String SIM2 = "SIM2";
    private final String MobileOperator = "mobileOperator";
    private final String ConnectionType = "connectionType";
    private final String Latency = "latency";
    private final String DeviceDetails = "deviceDetails";
    public final String AppVersion = "appVersion";
    public final String IMEI = "IMEI";
    public final String IMEI2 = "IMEI2";
    public final String UserId = "userId";
    public final String BatteryStats = "batteryStats";
    public final String AndroidVersion = "androidVersion";

    public String TABLE_APP_SYNC_STATUS_MASTER = "AppSyncStatusMaster";
    public String CREATE_TABLE_APP_SYNC_STATUS_MASTER = "CREATE TABLE IF NOT EXISTS " + TABLE_APP_SYNC_STATUS_MASTER + " (" +
            SYNC_MASTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            StartedOn + " TEXT, " +
            EndedOn + " TEXT, " +
            SyncTypeID + " INTEGER, " +
            UserId + " INTEGER, " +
            SIM1 + " TEXT, " +
            SIM2 + " TEXT, " +
            MobileOperator + " TEXT, " +
            ConnectionType + " TEXT, " +
            DeviceId + " TEXT, " +
            DeviceDetails + " TEXT, " +
            AppVersion + " TEXT, " +
            IMEI + " TEXT, " +
            IMEI2 + " TEXT, " +
            BatteryStats + " INTEGER, " +
            UploadedOn + " TEXT, " +
            AndroidVersion + " TEXT" +
            ");";
/*
    public void addAppSyncStatusMaster(AppSyncStatusMasterModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            if (!FindAppSyncStatusMaster(model)) {
                ContentValues values = new ContentValues();
                values.put(StartedOn, model.getStartedOn());
                values.put(EndedOn, model.getEndedOn());
                values.put(SyncTypeID, model.getSyncTypeId());
                values.put(UserId, model.getUserId());
                values.put(SIM1, model.getSIM1());
                values.put(SIM2, model.getSIM2());
                values.put(MobileOperator, model.getMobileOperator());
                values.put(ConnectionType, model.getConnectionType());
                values.put(DeviceId, model.getDeviceId());
                values.put(DeviceDetails, model.getManufacturerModel());
                values.put(AppVersion, model.getAppVersion());
                values.put(IMEI, model.getIMEI());
                values.put(IMEI2, model.getIMEI2());
                values.put(BatteryStats, model.getBatteryStats());
                values.put(UploadedOn, model.getUploadedOn());

                long i = DB.insert(TABLE_APP_SYNC_STATUS_MASTER, null, values);
                if (i == -1)
                    AppModel.getInstance().appendLog(context, "Couldn't insert AppSyncStatusMaster");
                else if (i > 0) {
                    AppModel.getInstance().appendLog(context, "AppSyncStatusMaster inserted");
                }
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addAppSyncStatusMaster Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean FindAppSyncStatusMaster(AppSyncStatusMasterModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_APP_SYNC_STATUS_MASTER
                    + " WHERE " + SYNC_MASTER_ID + " = " + model.getSyncMasterId() + " AND " + StartedOn + " = '" + model.getStartedOn() + "'"
                    + " AND " + SyncTypeID + " = " + model.getSyncTypeId();

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }*/


    public void addAppSyncStatus(AppSyncStatusModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            if (!FindAppSyncStatus(model)) {
                ContentValues values = new ContentValues();
                values.put(ModuleID, model.getModule_id());
                values.put(SubModule, model.getSubModule());
                values.put(StartedOn, model.getStartedOn());
                values.put(EndedOn, model.getEndedOn());
                values.put(Duration, model.getDuration());
                values.put(Downloaded, model.getDownloaded());
                values.put(Uploaded, model.getUploaded());
                values.put(CreatedOn, model.getCreatedOn());
                values.put(UploadedOn, model.getUploadedOn());

                long i = DB.insert(TABLE_APP_SYNC_STATUS, null, values);
                if (i == -1)
                    AppModel.getInstance().appendLog(context, "Couldn't insert AppSyncStatus");
                else if (i > 0) {
                    AppModel.getInstance().appendLog(context, "AppSyncStatus inserted");
                }
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addAppSyncStatus Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean FindAppSyncStatus(AppSyncStatusModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_APP_SYNC_STATUS
                    + " WHERE " + ID + " = " + model.getStatsId() + " AND " + StartedOn + " = '" + model.getStartedOn() + "'"
                    + " AND " + ModuleID + " = " + model.getModule_id();

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public void deletePreviousMonthsAppSyncStatusThatNotUploaded() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String whereClause = CreatedOn + " < DATE_TIME(?) AND ifnull(" + UploadedOn + ",'') != ''";
        String[] whereArgs = {AppModel.getInstance().getCurrentDateTime("dd/MM/yy hh:mm:ss")};
        try {
            long id = DB.delete(TABLE_APP_SYNC_STATUS,
                    whereClause,
                    whereArgs);
            AppModel.getInstance().appendLog(context, "PreviousMonthsAppSyncStatus deleted id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<AppSyncStatusModel> appSyncStatusModelList = new ArrayList<>();
    AppSyncStatusModel appSyncStatusModel = null;
    int modulesCounter = 0;
    double uploaded = 0, downloaded = 0;

    public void saveSyncProgressInLocalDB(SyncDownloadUploadModel syncDownloadUploadModel) {

        if (syncDownloadUploadModel != null) {

            if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.STUDENT_MODULE) {
                if (appSyncStatusModel == null) {
                    appSyncStatusModel = new AppSyncStatusModel();
                    modulesCounter = 1;
                    appSyncStatusModel.setModule_id(syncDownloadUploadModel.getTCFModuleId());
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                }

                if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Downloading")) {
                    appSyncStatusModel.setDownloaded(syncDownloadUploadModel.getCurrentFileSize());
                    downloaded += appSyncStatusModel.getDownloaded();
                    appSyncStatusModel.setDownloaded(downloaded);
                } else if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Uploading")) {
                    appSyncStatusModel.setUploaded(syncDownloadUploadModel.getCurrentFileSize());
                    uploaded += appSyncStatusModel.getUploaded();
                    appSyncStatusModel.setUploaded(uploaded);
                }

            } else if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.FINANCE_MODULE) {
                if (modulesCounter == 1) {
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa")); //Previous Module ended
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(), appSyncStatusModel.getEndedOn()));
                    appSyncStatusModelList.add(appSyncStatusModel); //Previous Module added
                    appSyncStatusModel = null;
                    uploaded = 0;
                    downloaded = 0;
                }

                if (appSyncStatusModel == null) {
                    appSyncStatusModel = new AppSyncStatusModel();
                    modulesCounter = 1;
                    appSyncStatusModel.setModule_id(syncDownloadUploadModel.getTCFModuleId());
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                }

                if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Downloading")) {
                    appSyncStatusModel.setDownloaded(syncDownloadUploadModel.getCurrentFileSize());
                    downloaded += appSyncStatusModel.getDownloaded();
                    appSyncStatusModel.setDownloaded(downloaded);
                } else if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Uploading")) {
                    appSyncStatusModel.setUploaded(syncDownloadUploadModel.getCurrentFileSize());
                    uploaded += appSyncStatusModel.getUploaded();
                    appSyncStatusModel.setUploaded(uploaded);
                }

            } else if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.EMPLOYEE_MODULE) {
                if (modulesCounter == 1) {
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa")); //Previous Module ended
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(), appSyncStatusModel.getEndedOn()));
                    appSyncStatusModelList.add(appSyncStatusModel); //Previous Module added
                    appSyncStatusModel = null;
                    uploaded = 0;
                    downloaded = 0;
                }

                if (appSyncStatusModel == null) {
                    appSyncStatusModel = new AppSyncStatusModel();
                    modulesCounter = 1;
                    appSyncStatusModel.setModule_id(syncDownloadUploadModel.getTCFModuleId());
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                }

                if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Downloading")) {
                    appSyncStatusModel.setDownloaded(syncDownloadUploadModel.getCurrentFileSize());
                    downloaded += appSyncStatusModel.getDownloaded();
                    appSyncStatusModel.setDownloaded(downloaded);
                } else if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Uploading")) {
                    appSyncStatusModel.setUploaded(syncDownloadUploadModel.getCurrentFileSize());
                    uploaded += appSyncStatusModel.getUploaded();
                    appSyncStatusModel.setUploaded(uploaded);
                }

            } else if (syncDownloadUploadModel.getTCFModuleId() == AppConstants.EXPENSE_MODULE) {
                if (modulesCounter == 1) {
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa")); //Previous Module ended
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(), appSyncStatusModel.getEndedOn()));
                    appSyncStatusModelList.add(appSyncStatusModel); //Previous Module added
                    appSyncStatusModel = null;
                    uploaded = 0;
                    downloaded = 0;
                }

                if (appSyncStatusModel == null) {
                    appSyncStatusModel = new AppSyncStatusModel();
                    appSyncStatusModel.setModule_id(syncDownloadUploadModel.getTCFModuleId());
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
//                    modulesCounter = 1;
                }

                if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Downloading")) {
                    appSyncStatusModel.setDownloaded(syncDownloadUploadModel.getCurrentFileSize());
                    downloaded += appSyncStatusModel.getDownloaded();
                    appSyncStatusModel.setDownloaded(downloaded);
                } else if (syncDownloadUploadModel.getSyncType().equalsIgnoreCase("Uploading")) {
                    appSyncStatusModel.setUploaded(syncDownloadUploadModel.getCurrentFileSize());
                    uploaded += appSyncStatusModel.getUploaded();
                    appSyncStatusModel.setUploaded(uploaded);
                }
            }

            if (syncDownloadUploadModel.getProgress() == 101) {
                //Sync Completed
                appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa")); //Previous Module ended
                appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(), appSyncStatusModel.getEndedOn()));
                appSyncStatusModelList.add(appSyncStatusModel); //Previous Module added

                for (AppSyncStatusModel model : appSyncStatusModelList) {
                    insertAppSyncRecords(model);
                }
            }
        }
    }

    public void insertAppSyncMasterRecords(AppSyncStatusMasterModel model) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(StartedOn, model.getStartedOn());
            values.put(EndedOn, model.getEndedOn());
            values.put(SyncTypeID, model.getSyncTypeId());
            values.put(UserId, model.getUserId());
            values.put(SIM1, model.getSIM1());
            values.put(SIM2, model.getSIM2());
            values.put(MobileOperator, model.getMobileOperator());
            values.put(ConnectionType, model.getConnectionType());
            values.put(DeviceId, model.getDeviceId());
            values.put(DeviceDetails, model.getManufacturerModel());
            values.put(AppVersion, model.getAppVersion());
            values.put(IMEI, model.getIMEI());
            values.put(IMEI2, model.getIMEI2());
            values.put(BatteryStats, model.getBatteryStats());
            values.put(UploadedOn, model.getUploadedOn());
            values.put(AndroidVersion,model.getAndroidVersion());

            long id = db.insert(TABLE_APP_SYNC_STATUS_MASTER, null, values);
            if (id > 0) {
                AppModel.getInstance().appendLog(context, "***********Successfully added App Sync Master Record");
            } else {
                AppModel.getInstance().appendLog(context, "***********Did Not Successfully added App Sync Master Record");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAppSyncRecords(AppSyncStatusModel model) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        ContentValues cv = null;
        try {
            cv = new ContentValues();
            cv.put(ModuleID, model.getModule_id());
            cv.put(SubModule, model.getSubModule());
            cv.put(StartedOn, model.getStartedOn());
            cv.put(EndedOn, model.getEndedOn());
            cv.put(Duration, model.getDuration());
            cv.put(Downloaded, model.getDownloaded());
            cv.put(Uploaded, model.getUploaded());
            cv.put(CreatedOn, AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            long id = db.insert(TABLE_APP_SYNC_STATUS, null, cv);
            if (id > 0) {
                AppModel.getInstance().appendLog(context, "***********Successfully added App Sync Record");
            } else {
                AppModel.getInstance().appendLog(context, "***********Did Not Successfully added App Sync Record");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLastMasterRow() {
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT MAX(syncId) as syncID FROM " + TABLE_APP_SYNC_STATUS_MASTER + " WHERE ( " + UploadedOn + " IS NULL OR " + UploadedOn + " = '') AND ( " + EndedOn + " IS NULL OR " + EndedOn + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                Integer id = cursor.getInt(cursor.getColumnIndex("syncID"));
                if (id != null && id > 0) {
                    return id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ArrayList<AppSyncStatusModel> getAllAppSyncStatusForUpload() {
        ArrayList<AppSyncStatusModel> list = new ArrayList<AppSyncStatusModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_APP_SYNC_STATUS + " WHERE ( " + UploadedOn + " IS NULL OR " + UploadedOn + " = '') AND " + SYNC_MASTER_ID + " > 0 ";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String startedOn = AppModel.getInstance()
                            .convertDatetoFormat(cursor.getString(cursor.getColumnIndex(StartedOn)), "dd/MM/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss");

                    String endedOn = AppModel.getInstance()
                            .convertDatetoFormat(cursor.getString(cursor.getColumnIndex(EndedOn)), "dd/MM/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss");

                    String createdOn = AppModel.getInstance()
                            .convertDatetoFormat(cursor.getString(cursor.getColumnIndex(CreatedOn)), "dd/MM/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss");

                    AppSyncStatusModel model = new AppSyncStatusModel();
                    model.setStatsId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setModule_id(cursor.getInt(cursor.getColumnIndex(ModuleID)));
                    model.setSubModule(cursor.getString(cursor.getColumnIndex(SubModule)));
                    model.setDuration(cursor.getInt(cursor.getColumnIndex(Duration)));
                    model.setDownloaded(cursor.getDouble(cursor.getColumnIndex(Downloaded)));
                    model.setUploaded(cursor.getDouble(cursor.getColumnIndex(Uploaded)));
                    model.setStartedOn(startedOn);
                    model.setEndedOn(endedOn);
                    model.setCreatedOn(createdOn);
                    model.setSyncId(cursor.getInt(cursor.getColumnIndex(SYNC_MASTER_ID)));
//                    model.setUploaded(cursor.getDouble(cursor.getColumnIndex(Uploaded)));
                    list.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public ArrayList<AppSyncStatusMasterModel> getAllAppSyncStatusMasterForUpload() {
        ArrayList<AppSyncStatusMasterModel> list = new ArrayList<AppSyncStatusMasterModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_APP_SYNC_STATUS_MASTER + " WHERE ( " + UploadedOn + " IS NULL OR " + UploadedOn + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
//                    String startedOn = AppModel.getInstance()
//                            .convertDatetoFormat(cursor.getString(cursor.getColumnIndex(StartedOn)), "dd/MM/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss");
//
//                    String endedOn = AppModel.getInstance()
//                            .convertDatetoFormat(cursor.getString(cursor.getColumnIndex(EndedOn)), "dd/MM/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss");

                    AppSyncStatusMasterModel model = new AppSyncStatusMasterModel();
                    model.setStartedOn(cursor.getString(cursor.getColumnIndex(StartedOn)));
                    model.setEndedOn(cursor.getString(cursor.getColumnIndex(EndedOn)));
                    model.setSyncMasterId(cursor.getInt(cursor.getColumnIndex(SYNC_MASTER_ID)));
                    model.setSyncTypeId(cursor.getInt(cursor.getColumnIndex(SyncTypeID)));
                    model.setUserId(cursor.getInt(cursor.getColumnIndex(UserId)));
                    model.setSIM1(cursor.getString(cursor.getColumnIndex(SIM1)));
                    model.setSIM2(cursor.getString(cursor.getColumnIndex(SIM2)));
                    model.setMobileOperator(cursor.getString(cursor.getColumnIndex(MobileOperator)));
                    model.setConnectionType(cursor.getString(cursor.getColumnIndex(ConnectionType)));
                    model.setDeviceId(cursor.getString(cursor.getColumnIndex(DeviceId)));
                    model.setManufacturerModel(cursor.getString(cursor.getColumnIndex(DeviceDetails)));
                    model.setAppVersion(cursor.getString(cursor.getColumnIndex(AppVersion)));
                    model.setIMEI(cursor.getString(cursor.getColumnIndex(IMEI)));
                    model.setIMEI2(cursor.getString(cursor.getColumnIndex(IMEI2)));
                    model.setBatteryStats(cursor.getInt(cursor.getColumnIndex(BatteryStats)));
                    model.setUploadedOn(cursor.getString(cursor.getColumnIndex(UploadedOn)));
                    model.setAndroidVersion(cursor.getString(cursor.getColumnIndex(AndroidVersion)));
                    list.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public long updateTableColumns(String tableName, ContentValues cv, String whereClause) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = cv;
            long i = DB.update(tableName, values, whereClause, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    public void deleteAppSyncStats(ArrayList<AppSyncStatusModel> modelList) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            for (AppSyncStatusModel model : modelList) {
                model.setCreatedOn(AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy hh:mm:ss a"));

                long id = DB.delete(TABLE_APP_SYNC_STATUS, CreatedOn + " = '" + model.getCreatedOn() + "' AND "
                        + ID + " = '" + model.getStatsId() + "'", null);
                AppModel.getInstance().appendLog(context, "Network Info deleted id=" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLastAppSyncMasterRecord(int id) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.delete(TABLE_APP_SYNC_STATUS_MASTER, SYNC_MASTER_ID + " = " + id,null);
            AppModel.getInstance().appendLog(context, "App Sync Master record deleted id = " + id);
            AppModel.getInstance().appendErrorLog(context, "App Sync Master record deleted id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
