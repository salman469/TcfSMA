package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.Fees_Collection.AppReceiptModel;
import com.tcf.sma.Models.Fees_Collection.FeesReceiptModel;
import com.tcf.sma.Models.SyncCashReceiptsModel;

import java.util.ArrayList;
import java.util.List;

/*
 * BY SAAD SAEED
 * */
public class AppReceipt {
    public static final String APP_RECEIPT_TABLE = "AppReceipt";
    public static final String ID = "id";
    public static final String SYS_ID = "sys_id";
    public static final String RECEIPT_NO = "receipt_no";
    public static final String SCHOOL_CLASS_ID = "schoolclass_id";
    public static final String SCHOOL_YEAR_ID = "school_year_id";
    public static final String STUDENT_ID = "student_id";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_ON = "created_on";
    public static final String UPLOADED_ON = "uploaded_on";
    public static final String IS_CORRECTION = "is_correction";
    public static final String FEES_ADMISSION = "fees_admission";
    public static final String FEES_EXAM = "fees_exam";
    public static final String FEES_TUTION = "fees_tution";
    public static final String FEES_BOOKS = "fees_books";
    public static final String FEES_COPIES = "fees_copies";
    public static final String FEES_UNIFORMS = "fees_uniform";
    public static final String FEES_OTHERS = "fees_others";
    public static final String DEVICE_ID = "device_id";
    public static final String CASH_DEPOSIT_ID = "CashDeposit_id";
    public static final String DOWNLOADED_ON = "downloaded_on";


    // Filter of View Receivables
    public static final String TOTAL_SUM = "total_sum";
    public static final String THIS_MONTH = "this_month";
    public static final String PREVIOUS_MONTH = "previous_month";
    public static final String THIS_YEAR = "this_year";
    public static final String PREVIOUS_YEAR = "previous_year";

    // GET COLUMNS BY KEY...
    public static final String KEY_TOTAL_COLLECTED_THIS_MONTH = "AppReceipt This Month Dues";
    public static final String KEY_TOTAL_COLLECTED_PREVIOUS_MONTH = "AppReceipt Previous Month Dues";
    public static final String KEY_TOTAL_COLLECTED_THIS_YEAR = "AppReceipt This Year Dues";
    public static final String KEY_TOTAL_COLLECTED_PREVIOUS_YEAR = "AppReceipt Previous Year Dues";
    public static final String KEY_GRAPH_TOTAL = "Total";
    public static final String KEY_GRAPH_MONTH = "Month";
    public static final String KEY_GRAPH_STATUS = "Status";


    public static final String CREATE_APP_RECEIPT_TABLE = "CREATE TABLE " + APP_RECEIPT_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SYS_ID + "  INTEGER,"
            + RECEIPT_NO + "  TEXT,"
            + SCHOOL_CLASS_ID + "  INTEGER,"
            + SCHOOL_YEAR_ID + "  INTEGER,"
            + STUDENT_ID + "  INTEGER,"
            + CREATED_BY + "  TEXT,"
            + CREATED_ON + "  TEXT,"
            + UPLOADED_ON + "  TEXT,"
            + IS_CORRECTION + "  INTEGER,"
            + FEES_ADMISSION + "  REAL,"
            + FEES_EXAM + "  REAL,"
            + FEES_TUTION + "  REAL,"
            + FEES_BOOKS + "  REAL,"
            + FEES_COPIES + "  REAL,"
            + FEES_UNIFORMS + "  REAL,"
            + FEES_OTHERS + "  REAL,"
            + DEVICE_ID + "  TEXT,"
            + CASH_DEPOSIT_ID + "  INTEGER,"
            + DOWNLOADED_ON + "  TEXT" + ")";


    public static final String GET_ALL_APP_RECEIPT = "Select * from " + APP_RECEIPT_TABLE;
    public static final String GET_ALL_APP_RECEIPT_FOR_ACCOUNT_STATEMENT = "select * from " + APP_RECEIPT_TABLE + " ar inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolID";

    // GET TOTAL FEES COLLECTED
    public static final String TOTAL_FEES = "Select SUM(ar.fees_admission + \n" +
            "\t\tar.fees_exam +\n" +
            "\t\tar.fees_tution +\n" +
            "\t\tar.fees_books +\n" +
            "\t\tar.fees_copies +\n" +
            "\t\tar.fees_uniform)";

    public static final String SPECIFIC_FEES = "Select SUM(ar.@column_name)";

    public static final String GET_TOTAL_FEES_COLLECTED_THIS_MONTH = " as 'AppReceipt This Month Dues'\n" +
            "\t\tFrom " + APP_RECEIPT_TABLE + " ar\n" +
            "\t\tWhere ar.created_on >= date('now','start of month')";

    public static final String GET_TOTAL_FEES_COLLECTED_PREVIOUS_MONTH = " as 'AppReceipt Previous Month Dues'\n" +
            "\t\tFrom " + APP_RECEIPT_TABLE + " ar\n" +
            "\t\tWhere ar.created_on >= date('now','start of month', '-1 month')\n" +
            "\t\tand ar.created_on < date('now','start of month')";

    public static final String GET_TOTAL_FEES_COLLECTED_THIS_YEAR = " as 'AppReceipt This Year Dues'\n" +
            "\t\tFrom " + APP_RECEIPT_TABLE + " ar\n" +
            "\t\tWhere ar.created_on >= date('now','start of year');";


    public static final String GET_TOTAL_FEES_COLLECTED_PREVIOUS_YEAR = " as 'AppReceipt Previous Year Dues'\n" +
            "\t\tFrom " + APP_RECEIPT_TABLE + " ar\n" +
            "\t\tWhere ar.created_on >= date('now','start of year', '-1 year') and\n" +
            "\t\tar.created_on < date('now','start of year');";

    public static final String GET_GRAPH_FEES_TOTAL = "select SUM(fees_admission+ fees_exam+ fees_tution+ fees_books+ fees_copies+ fees_uniform) as Total, \n" +
            "       strftime(\"%m-%Y\", AppReceipt.created_on) as 'Month' \n" +
            "       from " + APP_RECEIPT_TABLE + " group by strftime(\"%m-%Y\", AppReceipt.created_on) ORDER BY Month ASC";

    // Graph Data Query
    public static final String GET_GRAPH_FEES_TOTAL_v1 = "select SUM(fees_admission+ fees_exam+ fees_tution+ fees_books+ fees_copies+ fees_uniform) as Total, \n" +
            "       strftime(\"%m-%Y\", AppReceipt.created_on) as 'Month', 'Collected' as Status\n" +
            "       from AppReceipt group by strftime(\"%m-%Y\", AppReceipt.created_on)\n" +
            "\t   \n" +
            "\t   UNION ALL\n" +
            "\t   \n" +
            "select SUM(fees_admission+ fees_exam+ fees_tution+ fees_books+ fees_copies+ fees_uniform) as Total, \n" +
            "       strftime(\"%m-%Y\", AppInvoice.created_on) as 'Month' , 'Dues' as Status\n" +
            "       from AppInvoice group by strftime(\"%m-%Y\", AppInvoice.created_on)\n" +
            "\t   ORDER BY Month ASC;";


    private static AppReceipt instance = null;
    private Context context;

    AppReceipt(Context context) {

        this.context = context;
    }

    public static AppReceipt getInstance(Context context) {
        if (instance == null)
            instance = new AppReceipt(context);
        return instance;
    }

    public void insertRawAppReceipt(SQLiteDatabase db) {

        String sql = "INSERT INTO " + APP_RECEIPT_TABLE + " ("
                + SYS_ID + ","
                + RECEIPT_NO + ","
                + SCHOOL_CLASS_ID + ","
                + SCHOOL_YEAR_ID + ","
                + STUDENT_ID + ","
                + CREATED_BY + ","
                + CREATED_ON + ","
                + UPLOADED_ON + ","
                + IS_CORRECTION + ","
                + FEES_ADMISSION + ","
                + FEES_EXAM + ","
                + FEES_TUTION + ","
                + FEES_BOOKS + ","
                + FEES_COPIES + ","
                + FEES_UNIFORMS + ","
                + FEES_OTHERS + ","
                + DEVICE_ID + ","
                + CASH_DEPOSIT_ID + ","
                + DOWNLOADED_ON + ") VALUES ";


        sql += "(121,'121',16337,1,474625,'1112','2018-12-02',NULL,0,120.0,50.0,NULL,80.0,130,NULL,60.0,'20181202',NULL,NULL),";
        sql += "(122,'122',16337,1,474625,'1112','2018-12-02',NULL,0,NULL,20.0,54.0,90.0,90,150,60.0,'20181202',NULL,NULL),";
        sql += "(123,'123',16337,1,474625,'1112','2018-12-02',NULL,0,130.0,NULL,85,70.0,85,49,45,'20181202',NULL,NULL),";
        sql += "(124,'124',16337,1,474625,'1112','2018-12-02',NULL,0,150.0,30.0,160,40.0,NULL,85,60.0,'20181202',NULL,NULL),";
        sql += "(125,'125',16337,1,474625,'1112','2018-12-02',NULL,0,107.0,NULL,NULL,85.0,NULL,50,NULL,'20181202',NULL,NULL),";
        sql += "(126,'126',16337,1,474625,'1112','2018-12-02',NULL,0,321.0,40.0,250,NULL,64,NULL,60.0,'20181202',NULL,NULL),";
        sql += "(127,'127',16337,1,474625,'1112','2018-12-02',NULL,1,85.0,NULL,NULL,80.0,135,135,NULL,'20181202',NULL,NULL),";
        sql += "(128,'128',16337,1,474625,'1112','2018-12-02',NULL,0,1250.0,50.0,45,NULL,NULL,160,60.0,'20181202',NULL,NULL),";
        sql += "(129,'129',16337,1,474625,'1112','2018-12-02',NULL,0,105.0,60.0,NULL,80.0,185,155,NULL,'20181202',NULL,NULL),";
        sql += "(130,'130',16337,1,474625,'1112','2018-12-02',NULL,0,60.0,90.0,75,NULL,35,NULL,60.0,'20181202',NULL,NULL),";
        sql += "(131,'131',16337,1,474625,'1112','2018-12-02',NULL,1,230.0,NULL,NULL,80.0,NULL,175,60.0,'20181202',NULL,NULL),";
        sql += "(132,'132',16337,1,474625,'1112','2018-12-02',NULL,0,170.0,30.0,69,NULL,120,200,60.0,'20181202',NULL,NULL)";

        db.execSQL(sql);
    }


    public long insertCorrection(FeesReceiptModel feesReceiptModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppReceipt.FEES_ADMISSION, feesReceiptModel.getAdmissionFees());
            cv.put(AppReceipt.FEES_BOOKS, feesReceiptModel.getBookFees());
            cv.put(AppReceipt.STUDENT_ID, feesReceiptModel.getStudentId());
            cv.put(AppReceipt.FEES_COPIES, feesReceiptModel.getCopyFees());
            cv.put(AppReceipt.FEES_EXAM, feesReceiptModel.getExamFees());
            cv.put(AppReceipt.FEES_TUTION, feesReceiptModel.getTutionFees());
            cv.put(AppReceipt.FEES_UNIFORMS, feesReceiptModel.getUniformFees());
            cv.put(AppReceipt.SCHOOL_YEAR_ID, feesReceiptModel.getSchoolYearId());
            cv.put(AppReceipt.FEES_OTHERS, feesReceiptModel.getOthersFees());
            cv.put(AppReceipt.CREATED_ON, feesReceiptModel.getCreatedOn());
            cv.put(AppReceipt.CREATED_BY, feesReceiptModel.getCreatedBy());
            cv.put(AppReceipt.SCHOOL_CLASS_ID, feesReceiptModel.getSchoolClassId());
            cv.put(AppReceipt.IS_CORRECTION, feesReceiptModel.getCorrectionType());
            cv.put(AppReceipt.DEVICE_ID, feesReceiptModel.getDeviceId());

            id = db.insert(AppReceipt.APP_RECEIPT_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public long insertFeesReceipt(FeesReceiptModel feesReceiptModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppReceipt.FEES_ADMISSION, feesReceiptModel.getAdmissionFees());
            cv.put(AppReceipt.FEES_BOOKS, feesReceiptModel.getBookFees());
            cv.put(AppReceipt.STUDENT_ID, feesReceiptModel.getStudentId());
            cv.put(AppReceipt.FEES_COPIES, feesReceiptModel.getCopyFees());
            cv.put(AppReceipt.SYS_ID, feesReceiptModel.getSys_id());
            cv.put(AppReceipt.FEES_EXAM, feesReceiptModel.getExamFees());
            cv.put(AppReceipt.FEES_TUTION, feesReceiptModel.getTutionFees());
            cv.put(AppReceipt.FEES_UNIFORMS, feesReceiptModel.getUniformFees());
            cv.put(AppReceipt.FEES_OTHERS, feesReceiptModel.getOthersFees());
            if (feesReceiptModel.getReceiptNumber() > -1)
                cv.put(AppReceipt.RECEIPT_NO, feesReceiptModel.getReceiptNumber());
            cv.put(AppReceipt.CREATED_ON, feesReceiptModel.getCreatedOn());
            cv.put(AppReceipt.CREATED_BY, feesReceiptModel.getCreatedBy());
            cv.put(AppReceipt.SCHOOL_YEAR_ID, feesReceiptModel.getSchoolYearId());
            cv.put(AppReceipt.SCHOOL_CLASS_ID, feesReceiptModel.getSchoolClassId());
            cv.put(AppReceipt.IS_CORRECTION, feesReceiptModel.getCorrectionType());
            cv.put(AppReceipt.DEVICE_ID, feesReceiptModel.getDeviceId());

            id = db.insert(AppReceipt.APP_RECEIPT_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public long insertDownloadedFeesReceipt(FeesReceiptModel feesReceiptModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppReceipt.FEES_ADMISSION, feesReceiptModel.getAdmissionFees());
            cv.put(AppReceipt.FEES_BOOKS, feesReceiptModel.getBookFees());
            cv.put(AppReceipt.STUDENT_ID, feesReceiptModel.getStudentId());
            cv.put(AppReceipt.FEES_COPIES, feesReceiptModel.getCopyFees());
            cv.put(AppReceipt.SYS_ID, feesReceiptModel.getSys_id());
            cv.put(AppReceipt.FEES_EXAM, feesReceiptModel.getExamFees());
            cv.put(AppReceipt.FEES_TUTION, feesReceiptModel.getTutionFees());
            cv.put(AppReceipt.FEES_UNIFORMS, feesReceiptModel.getUniformFees());
            cv.put(AppReceipt.FEES_OTHERS, feesReceiptModel.getOthersFees());
            if (feesReceiptModel.getCashDepositId() != null && !feesReceiptModel.getCashDepositId().isEmpty())
                cv.put(AppReceipt.CASH_DEPOSIT_ID, feesReceiptModel.getCashDepositId());

            if (feesReceiptModel.getReceiptNumber() > -1)
                cv.put(AppReceipt.RECEIPT_NO, feesReceiptModel.getReceiptNumber());
            cv.put(AppReceipt.CREATED_ON, feesReceiptModel.getCreatedOn());
            cv.put(AppReceipt.UPLOADED_ON, feesReceiptModel.getUploadedOn());
            cv.put(AppReceipt.CREATED_BY, feesReceiptModel.getCreatedBy());
            cv.put(AppReceipt.SCHOOL_YEAR_ID, feesReceiptModel.getSchoolYearId());
            cv.put(AppReceipt.SCHOOL_CLASS_ID, feesReceiptModel.getSchoolClassId());
            cv.put(AppReceipt.IS_CORRECTION, feesReceiptModel.getCorrectionType());
            cv.put(AppReceipt.DEVICE_ID, feesReceiptModel.getDeviceId());
            cv.put(AppReceipt.DOWNLOADED_ON, feesReceiptModel.getDownloadedOn());


            id = db.insert(AppReceipt.APP_RECEIPT_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    public void updateFeesReceipt(FeesReceiptModel feesReceiptModel) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(AppReceipt.FEES_ADMISSION, feesReceiptModel.getAdmissionFees());
            cv.put(AppReceipt.FEES_BOOKS, feesReceiptModel.getBookFees());
            cv.put(AppReceipt.FEES_COPIES, feesReceiptModel.getCopyFees());
            cv.put(AppReceipt.FEES_EXAM, feesReceiptModel.getExamFees());
            cv.put(AppReceipt.FEES_OTHERS, feesReceiptModel.getOthersFees());
            cv.put(AppReceipt.CREATED_ON, feesReceiptModel.getCreatedOn());
            cv.put(AppReceipt.CREATED_BY, feesReceiptModel.getCreatedBy());
            cv.put(AppReceipt.SCHOOL_YEAR_ID, feesReceiptModel.getSchoolYearId());
            cv.put(AppReceipt.SCHOOL_CLASS_ID, feesReceiptModel.getSchoolClassId());
            cv.put(AppReceipt.IS_CORRECTION, feesReceiptModel.getCorrectionType());
            cv.put(AppReceipt.DEVICE_ID, feesReceiptModel.getDeviceId());
            cv.put(AppReceipt.FEES_TUTION, feesReceiptModel.getTutionFees());
            cv.put(AppReceipt.FEES_UNIFORMS, feesReceiptModel.getUniformFees());
            cv.put(AppReceipt.SCHOOL_CLASS_ID, feesReceiptModel.getSchoolClassId());

            db.update(AppReceipt.APP_RECEIPT_TABLE, cv,
                    AppReceipt.RECEIPT_NO + " = " + feesReceiptModel.getReceiptNumber(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void genericUpdateMethod(ContentValues values, String deviceId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            int id = db.update(AppReceipt.APP_RECEIPT_TABLE, values, AppReceipt.ID + " = " + deviceId, null);
            Log.d("appReceipt", "" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateAppReceiptForCashDeposit(int cashDepositId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(AppReceipt.CASH_DEPOSIT_ID, cashDepositId);

            db.update(AppReceipt.APP_RECEIPT_TABLE, cv, AppReceipt.CASH_DEPOSIT_ID + " IS NULL ", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean doesFeesReceiptExists(FeesReceiptModel feesReceiptModel, int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select * from AppReceipt ar inner join school_class sc on ar.schoolclass_id = sc.id where ar.receipt_no = '@RNO' and sc.school_id = @SchoolId; ";

        query = query.replace("@RNO", feesReceiptModel.getReceiptNumber() + "");
        query = query.replace("@SchoolId", schoolId + "");

        try {

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<AppReceiptModel> getAllAppReceiptForAccountStatement(int schoolId, String grNo, String fromDate, String toDate) {
        List<AppReceiptModel> armList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query = AppReceipt.GET_ALL_APP_RECEIPT_FOR_ACCOUNT_STATEMENT;
        query = query.replace("@SchoolID", String.valueOf(schoolId));

        if ((fromDate != null && toDate != null) && (!fromDate.isEmpty() && !toDate.isEmpty())) {

            query = query + "and ar.created_on between '@FromDate' and '@ToDate'";
            query = query.replace("@FromDate", fromDate);
            query = query.replace("@ToDate", toDate);
        }

        try {

            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    AppReceiptModel model = new AppReceiptModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(AppReceipt.ID)));
                    model.setSysId(cursor.getInt(cursor.getColumnIndex(AppReceipt.SYS_ID)));
                    model.setReceiptNo(cursor.getString(cursor.getColumnIndex(AppReceipt.RECEIPT_NO)));
                    model.setSchoolclassId(cursor.getInt(cursor.getColumnIndex(AppReceipt.SCHOOL_CLASS_ID)));
                    model.setSchoolYearId(cursor.getInt(cursor.getColumnIndex(AppReceipt.SCHOOL_YEAR_ID)));
                    model.setStudentId(cursor.getInt(cursor.getColumnIndex(AppReceipt.STUDENT_ID)));
                    model.setCreatedby(cursor.getString(cursor.getColumnIndex(AppReceipt.CREATED_BY)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(AppReceipt.CREATED_ON)));
                    model.setUploadedOn(cursor.getString(cursor.getColumnIndex(AppReceipt.UPLOADED_ON)));
                    model.setIsCorrection(cursor.getInt(cursor.getColumnIndex(AppReceipt.IS_CORRECTION)));
                    model.setFees_admission(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_ADMISSION)));
                    model.setFees_exam(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_EXAM)));
                    model.setFees_tution(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_TUTION)));
                    model.setFees_books(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_BOOKS)));
                    model.setFees_copies(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_COPIES)));
                    model.setFees_uniform(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_UNIFORMS)));
                    model.setFees_others(cursor.getDouble(cursor.getColumnIndex(AppReceipt.FEES_OTHERS)));
                    model.setDevice_id(cursor.getString(cursor.getColumnIndex(AppReceipt.DEVICE_ID)));
                    model.setCashDepositId(cursor.getInt(cursor.getColumnIndex(AppReceipt.CASH_DEPOSIT_ID)));
                    model.setDownloadedOn(cursor.getString(cursor.getColumnIndex(AppReceipt.DOWNLOADED_ON)));

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

    public int getMaxSysId() {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(sys_id) as sys_id from AppReceipt";
        String sys_id = "";

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                sys_id = cursor.getString(cursor.getColumnIndex(SYS_ID));
                if (sys_id != null && sys_id != "")
                    return Integer.valueOf(sys_id);
                else
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<SyncCashReceiptsModel> getAllAppReceiptsForUpload(int userId) {
        List<SyncCashReceiptsModel> armList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = "select ar.* from AppReceipt ar  where (ar.sys_id IS NULL OR ar.sys_id = 0) and ar.created_by = @UserId";
        query = query.replace("@UserId", userId + "");
        Log.d("Upload query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    SyncCashReceiptsModel model = new SyncCashReceiptsModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(AppReceipt.ID)));
                    model.setReceiptNo(cursor.getString(cursor.getColumnIndex(AppReceipt.RECEIPT_NO)));
                    model.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(AppReceipt.SCHOOL_CLASS_ID)));
                    model.setSchoolYearId(cursor.getInt(cursor.getColumnIndex(AppReceipt.SCHOOL_YEAR_ID)));
                    model.setStudentId(cursor.getInt(cursor.getColumnIndex(AppReceipt.STUDENT_ID)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(AppReceipt.CREATED_BY)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(AppReceipt.CREATED_ON)));
                    model.setIsCorrection(cursor.getString(cursor.getColumnIndex(AppReceipt.IS_CORRECTION)).equalsIgnoreCase("c"));
                    model.setFees_admission(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_ADMISSION)));
                    model.setFees_exam(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_EXAM)));
                    model.setFees_tution(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_TUTION)));
                    model.setFees_books(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_BOOKS)));
                    model.setFees_copies(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_COPIES)));
                    model.setFees_uniform(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_UNIFORMS)));
                    model.setFees_others(cursor.getInt(cursor.getColumnIndex(AppReceipt.FEES_OTHERS)));
                    model.setDevice_id(cursor.getInt(cursor.getColumnIndex(AppReceipt.DEVICE_ID)));
//                    model.setCashDepositId(cursor.getInt(cursor.getColumnIndex("cashdeposit_sys_id")));

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

    public boolean FindReceipt(String receiptNo) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + APP_RECEIPT_TABLE + " where " + RECEIPT_NO + "='" + receiptNo + "'";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isReceiptUploaded(String receiptNo) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + APP_RECEIPT_TABLE + " where " + RECEIPT_NO + "='" + receiptNo + "' and " + UPLOADED_ON + " is not null";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
