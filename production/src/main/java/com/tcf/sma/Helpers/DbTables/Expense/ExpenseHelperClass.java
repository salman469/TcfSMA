package com.tcf.sma.Helpers.DbTables.Expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcf.sma.DataSync.DataSync;
import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ExpenseCheckSumModel;
import com.tcf.sma.Models.Fees_Collection.GeneralUploadResponseModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseAmountClosingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseClosingItem;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseHeadsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashMonthlyLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionBucketModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionCategoryModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionFlowModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionImagesModel;
import com.tcf.sma.Models.RetrofitModels.Expense.SlipModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubHeadsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadExceptionLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsMonthlyModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingSearchModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionUploadResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;

import java.util.ArrayList;
import java.util.List;

public class ExpenseHelperClass {

    private static ExpenseHelperClass instance = null;
    private Context context;
    //ID for All TABLES Primary Keys
    public static final String ID = "id";
    //FOREIGN KEYS USED in Other Tables
    public final String KEY_ID = "id";
    public static final String KEY_SCHOOL_ID = "school_id";
    public final String KEY_SESSION_ID = "session_id";
    //TABLE NAMES for Foreign Keys
    public static final String TABLE_SCHOOL = "school";
    public static final String TABLE_USER = "user";
    public static final String TABLE_ACADEMIC_SESSION = "AcademicSession";
    public final String UPLOADED_ON = "uploaded_on";

    ExpenseHelperClass(Context context) {
        this.context = context;
    }

    public static ExpenseHelperClass getInstance(Context context) {
        if (instance == null)
            instance = new ExpenseHelperClass(context);
        return instance;
    }

    public static final String TABLE_EXPENSE_HEAD = "ExpenseHead";
    public static final String HEAD_NAME = "headname";
    public static final String HEAD_CREATED_ON = "createdon";
    public static final String HEAD_CREATED_BY = "createdby";
    public static final String HEAD_MODIFIED_ON = "modifiedon";
    public static final String HEAD_MODIFIED_BY = "modifiedby";
    public static final String HEAD_IS_ACTIVE = "is_active";
    public final String CREATE_TABLE_EXPENSE_HEAD = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_HEAD
            + " (" + ID + "  INTEGER PRIMARY KEY ,"
            + HEAD_NAME + "  TEXT ,"
            + HEAD_CREATED_ON + "  TEXT ,"
            + HEAD_CREATED_BY + "  INTEGER ,"
            + HEAD_MODIFIED_ON + "  TEXT ,"
            + HEAD_MODIFIED_BY + "  INTEGER ,"
            + KEY_SCHOOL_ID + " INTEGER,"
            + HEAD_IS_ACTIVE + "  BOOLEAN);";


    public long addHeadRecord(ExpenseHeadsModel head) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, head.getId());
            cv.put(HEAD_NAME, head.getHeadName());
            cv.put(HEAD_CREATED_BY, head.getCreatedOn());
            cv.put(HEAD_CREATED_ON, head.getCreatedOn());
            cv.put(HEAD_MODIFIED_BY, head.getModifiedBy());
            cv.put(HEAD_MODIFIED_ON, head.getModifiedOn());
            cv.put(HEAD_IS_ACTIVE, head.getIsActive());
            cv.put(KEY_SCHOOL_ID, head.getSchoolId());
            i = DB.insert(TABLE_EXPENSE_HEAD, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert Head record with ID: " + head.getId());
            else
                AppModel.getInstance().appendLog(context, "Head record inserted with ID: " + head.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addHeadRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindHeads(ExpenseHeadsModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_HEAD + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    /*public boolean removeHeadRecord(HeadsModel hRecord) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EXPENSE_HEAD, ID + "=" + hRecord.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeHeadRecordNotExists(final int schoolId,final ArrayList<HeadsModel> headRecordsList) {
        new Thread(() -> {
            boolean HRecordFound;
            ArrayList<HeadsModel> existingRecordsList = getAllHeadList(schoolId);
            ArrayList<HeadsModel> todo = new ArrayList<>();
            if (existingRecordsList != null && existingRecordsList.size() > 0) {
                for (HeadsModel existingRecord : existingRecordsList) {
                    HRecordFound = false;
                    for (HeadsModel serverQHRecord : headRecordsList) {
//
                        if((existingRecord.getId() == serverQHRecord.getId())){
                            HRecordFound = true;
                        }
                    }
                    if (!HRecordFound)
                        todo.add(existingRecord);
                }
            }
            for (HeadsModel model : todo) {
                if (removeHeadRecord(model)) {
                    AppModel.getInstance().appendLog(context, "Record Removed: id = " + model.getId() + " Exp ID = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove record: id = " + model.getId() + " Exp ID = " + model.getId());
                }
            }
        }).start();
    }


    private ArrayList<HeadsModel> getAllHeadList(int schoolId) {
        ArrayList<HeadsModel> eqdList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT e.* FROM " + TABLE_EXPENSE_HEAD + " WHERE e." + ID + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HeadsModel hm = new HeadsModel();
                    hm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    hm.setCreatedBy(cursor.getInt(cursor.getColumnIndex(HEAD_CREATED_BY)));
                    hm.setCreatedOn(cursor.getString(cursor.getColumnIndex(HEAD_CREATED_ON)));
                    hm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(HEAD_MODIFIED_BY)));
                    hm.setModifiedOn(cursor.getString(cursor.getColumnIndex(HEAD_MODIFIED_ON)));
                    eqdList.add(hm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return eqdList;
    }*/


    public static final String TABLE_EXPENSE_SUBHEAD = "ExpenseSubHead";
    public static final String SUB_HEAD_NAME = "subhead_name";
    public static final String SUBHEAD_HEAD_ID = "head_id";

    public final String CREATE_TABLE_SUBHEAD = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_SUBHEAD + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + SUB_HEAD_NAME + " VARCHAR,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN ,"
            + SUBHEAD_HEAD_ID + " INTEGER,"
            + KEY_SCHOOL_ID + " INTEGER"
            + " )";

    public long addSubHeadRecord(ExpenseSubHeadsModel subhead) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, subhead.getId());
            cv.put(SUBHEAD_HEAD_ID, subhead.getHead_id());
            cv.put(SUB_HEAD_NAME, subhead.getSubHeadName());
            cv.put(HEAD_CREATED_BY, subhead.getCreatedOn());
            cv.put(HEAD_CREATED_ON, subhead.getCreatedOn());
            cv.put(HEAD_MODIFIED_BY, subhead.getModifiedBy());
            cv.put(HEAD_MODIFIED_ON, subhead.getModifiedOn());
            cv.put(HEAD_IS_ACTIVE, subhead.isActive());
            cv.put(KEY_SCHOOL_ID, subhead.getSchoolId());
            i = DB.insert(TABLE_EXPENSE_SUBHEAD, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert SubHead record with ID: " + subhead.getId());
            else
                AppModel.getInstance().appendLog(context, "SubHead record inserted with ID: " + subhead.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addHeadRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindSubHeads(ExpenseSubHeadsModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SUBHEAD + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public static final String TABLE_EXPENSE_SUBHEAD_LIMITS = "ExpenseSubHeadLimits";
    public static final String SUBHEAD_LIMITS_ALLOWED_FROM = "allowedfrom";
    public static final String SUBHEAD_LIMITS_LIMITAMOUNT = "limit_amount";
    public static final String SUBHEAD_LIMITS_ISRESTRICTED = "is_restricted";

    public static final String SUBHEAD_LIMITS_SUBHEAD_ID = "subhead_id";
    public static final String SUBHEAD_LIMITS_SCHOOL_ID = "school_id";
    public final String CREATE_TABLE_SUBHEAD_LIMITS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_SUBHEAD_LIMITS + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + SUBHEAD_LIMITS_ALLOWED_FROM + " VARCHAR,"
            + SUBHEAD_LIMITS_LIMITAMOUNT + " INTEGER,"
            + SUBHEAD_LIMITS_ISRESTRICTED + " BOOLEAN,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN ,"
            + SUBHEAD_LIMITS_SUBHEAD_ID + " INTEGER ,"
            + KEY_SCHOOL_ID + " INTEGER"
            + " )";

    public long addSubHeadLimitsRecord(ExpenseSubheadLimitsModel subheadLimitsModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, subheadLimitsModel.getId());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, subheadLimitsModel.getSubhead_id());
            cv.put(KEY_SCHOOL_ID, subheadLimitsModel.getSchool_id());
            cv.put(SUBHEAD_LIMITS_ALLOWED_FROM, subheadLimitsModel.getAllowed_from());
            cv.put(SUBHEAD_LIMITS_LIMITAMOUNT, subheadLimitsModel.getLimit_amount());
            cv.put(SUBHEAD_LIMITS_ISRESTRICTED, subheadLimitsModel.isIs_restricted());
            cv.put(HEAD_CREATED_ON, subheadLimitsModel.getCreated_on());
            cv.put(HEAD_CREATED_BY, subheadLimitsModel.getCreated_by());
            cv.put(HEAD_MODIFIED_BY, subheadLimitsModel.getModified_by());
            cv.put(HEAD_MODIFIED_ON, subheadLimitsModel.getModified_on());
            cv.put(HEAD_IS_ACTIVE, subheadLimitsModel.isActive());

            i = DB.insert(TABLE_EXPENSE_SUBHEAD_LIMITS, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert SubHeadLimits record with ID: " + subheadLimitsModel.getId());
            else
                AppModel.getInstance().appendLog(context, "SubHeadLimits record inserted with ID: " + subheadLimitsModel.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSubHeadLimitsRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindSubHeadLimits(ExpenseSubheadLimitsModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SUBHEAD_LIMITS + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public static final String TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY = "ExpenseSubHeadLimitsMonthly";
    public static final String SUBHEAD_LIMITS_MONTHLY_ALLOWED_FROM = "allowedfrom";
    public static final String SUBHEAD_LIMITS_MONTHLY_FOR_DATE = "for_date";
    public static final String SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT = "spent_amount";
    public static final String SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID = "salary_user_id";
    public final String CREATE_TABLE_SUBHEAD_LIMITS_MONTHLY = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + SUBHEAD_LIMITS_MONTHLY_ALLOWED_FROM + " VARCHAR,"
            + SUBHEAD_LIMITS_MONTHLY_FOR_DATE + " VARCHAR,"
            + SUBHEAD_LIMITS_LIMITAMOUNT + " INTEGER,"
            + SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT + " INTEGER,"
            + SUBHEAD_LIMITS_ISRESTRICTED + " BOOLEAN,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + "  BOOLEAN ,"
            + UPLOADED_ON + " TEXT ,"
            + SUBHEAD_LIMITS_SUBHEAD_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_SCHOOL_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID + " INTEGER"
            + " )";

    public long addSubHeadLimitsMonthlyRecord(ExpenseSubheadLimitsMonthlyModel subheadLimitsMonthlyModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, subheadLimitsMonthlyModel.getId());
            cv.put(SUBHEAD_LIMITS_MONTHLY_ALLOWED_FROM, subheadLimitsMonthlyModel.getAllowed_from());
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, subheadLimitsMonthlyModel.getFor_date());
            cv.put(SUBHEAD_LIMITS_LIMITAMOUNT, subheadLimitsMonthlyModel.getLimit_amount());
            cv.put(SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT, subheadLimitsMonthlyModel.getSpent_amount());
            cv.put(SUBHEAD_LIMITS_ISRESTRICTED, subheadLimitsMonthlyModel.isIs_restricted());
            cv.put(HEAD_CREATED_ON, subheadLimitsMonthlyModel.getCreated_on());
            cv.put(HEAD_CREATED_BY, subheadLimitsMonthlyModel.getCreated_by());
            cv.put(HEAD_MODIFIED_BY, subheadLimitsMonthlyModel.getModified_by());
            cv.put(HEAD_MODIFIED_ON, subheadLimitsMonthlyModel.getModified_on());
            cv.put(HEAD_IS_ACTIVE, subheadLimitsMonthlyModel.isActive());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, subheadLimitsMonthlyModel.getSubhead_id());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, subheadLimitsMonthlyModel.getSchool_id());
            cv.put(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID, subheadLimitsMonthlyModel.getSalary_userid());
            cv.put(UPLOADED_ON,subheadLimitsMonthlyModel.getUploadedOn());


            i = DB.insert(TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert SubHeadLimitsMonthly record with ID: " + subheadLimitsMonthlyModel.getId());
            else
                AppModel.getInstance().appendLog(context, "SubHeadLimitsMonthly record inserted with ID: " + subheadLimitsMonthlyModel.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSubHeadLimitsRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindSubHeadLimitsMonthly(ExpenseSubheadLimitsMonthlyModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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


    public static final String TABLE_EXPENSE_SUBHEAD_EXCEPTIONS = "ExpenseSubHeadExceptions";
    public static final String SUBHEAD_EXCEPTIONS_START_DATE = "start_date";
    public static final String SUBHEAD_EXCEPTIONS_END_DATE = "end_date";
    public final String CREATE_TABLE_SUBHEAD_EXCEPTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_SUBHEAD_EXCEPTIONS + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + SUBHEAD_LIMITS_ALLOWED_FROM + " VARCHAR,"
            + SUBHEAD_LIMITS_LIMITAMOUNT + " INTEGER,"
            + SUBHEAD_EXCEPTIONS_START_DATE + " VARCHAR,"
            + SUBHEAD_EXCEPTIONS_END_DATE + " VARCHAR,"
            + SUBHEAD_LIMITS_ISRESTRICTED + " BOOLEAN,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN ,"
            + SUBHEAD_LIMITS_SUBHEAD_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_SCHOOL_ID + " INTEGER"
            + " )";

    public long addSubHeadExceptions(ExpenseSubheadExceptionLimitsModel subheadExceptionLimitsModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, subheadExceptionLimitsModel.getId());
            cv.put(SUBHEAD_LIMITS_MONTHLY_ALLOWED_FROM, subheadExceptionLimitsModel.getAllowed_from());
            cv.put(SUBHEAD_LIMITS_LIMITAMOUNT, subheadExceptionLimitsModel.getLimit_amount());
            cv.put(SUBHEAD_EXCEPTIONS_START_DATE, subheadExceptionLimitsModel.getStart_date());
            cv.put(SUBHEAD_EXCEPTIONS_END_DATE, subheadExceptionLimitsModel.getEnd_date());
            cv.put(SUBHEAD_LIMITS_ISRESTRICTED, subheadExceptionLimitsModel.isIs_restricted());
            cv.put(HEAD_CREATED_ON, subheadExceptionLimitsModel.getCreated_on());
            cv.put(HEAD_CREATED_BY, subheadExceptionLimitsModel.getCreated_by());
            cv.put(HEAD_MODIFIED_BY, subheadExceptionLimitsModel.getModified_by());
            cv.put(HEAD_MODIFIED_ON, subheadExceptionLimitsModel.getModified_on());
            cv.put(HEAD_IS_ACTIVE, subheadExceptionLimitsModel.isActive());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, subheadExceptionLimitsModel.getSubheadlimits_id());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, subheadExceptionLimitsModel.getSchool_id());


            i = DB.insert(TABLE_EXPENSE_SUBHEAD_EXCEPTIONS, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert SubHeadExceptions record with ID: " + subheadExceptionLimitsModel.getId());
            else
                AppModel.getInstance().appendLog(context, "SubHeadExceptions record inserted with ID: " + subheadExceptionLimitsModel.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSubHeadExceptions Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindSubHeadExceptions(ExpenseSubheadExceptionLimitsModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SUBHEAD_EXCEPTIONS + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public static final String TABLE_EXPENSE_SCHOOL_PETTYCASH_LIMITS = "ExpenseSchoolPettyCashLimits";
    public final String CREATE_TABLE_SCHOOL_PETTYCASH_LIMITS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_SCHOOL_PETTYCASH_LIMITS + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + SUBHEAD_LIMITS_ALLOWED_FROM + " VARCHAR,"
            + SUBHEAD_LIMITS_LIMITAMOUNT + " INTEGER,"
            + SUBHEAD_LIMITS_ISRESTRICTED + " BOOLEAN,"
            + HEAD_CREATED_ON + "  TEXT ,"
            + HEAD_CREATED_BY + "  INTEGER ,"
            + HEAD_MODIFIED_ON + "  TEXT ,"
            + HEAD_MODIFIED_BY + "  INTEGER ,"
            + HEAD_IS_ACTIVE + "  BOOLEAN ,"
            + SUBHEAD_LIMITS_SCHOOL_ID + "  INTEGER"
            + " )";

    public long addSchoolPettyCashLimits(ExpenseSchoolPettyCashLimitsModel pettyCashLimitsModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, pettyCashLimitsModel.getId());
            cv.put(SUBHEAD_LIMITS_ALLOWED_FROM, pettyCashLimitsModel.getAllowedFrom());
            cv.put(SUBHEAD_LIMITS_LIMITAMOUNT, pettyCashLimitsModel.getLimit_amount());
            cv.put(SUBHEAD_LIMITS_ISRESTRICTED, pettyCashLimitsModel.isRestricted());
            cv.put(HEAD_CREATED_ON, pettyCashLimitsModel.getCreated_on());
            cv.put(HEAD_CREATED_BY, pettyCashLimitsModel.getCreated_by());
            cv.put(HEAD_MODIFIED_BY, pettyCashLimitsModel.getModified_by());
            cv.put(HEAD_MODIFIED_ON, pettyCashLimitsModel.getModified_on());
            cv.put(HEAD_IS_ACTIVE, pettyCashLimitsModel.isActive());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, pettyCashLimitsModel.getSchool_id());

            i = DB.insert(TABLE_EXPENSE_SCHOOL_PETTYCASH_LIMITS, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert SchoolPettyCashLimits record with ID: " + pettyCashLimitsModel.getId());
            else
                AppModel.getInstance().appendLog(context, "SchoolPettyCashLimits record inserted with ID: " + pettyCashLimitsModel.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSchoolPettyCashLimits Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindSchoolPettyCashLimits(ExpenseSchoolPettyCashLimitsModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SCHOOL_PETTYCASH_LIMITS + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public static final String TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS = "ExpenseSchoolPettyCashMonthlyLimits";
    public final String CREATE_TABLE_SCHOOL_PETTYCASH_MONTHLY_LIMITS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + SUBHEAD_LIMITS_ALLOWED_FROM + " VARCHAR,"
            + SUBHEAD_LIMITS_MONTHLY_FOR_DATE + " VARCHAR,"
            + SUBHEAD_LIMITS_LIMITAMOUNT + " INTEGER,"
            + SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT + " INTEGER,"
            + SUBHEAD_LIMITS_ISRESTRICTED + " BOOLEAN,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN ,"
            + UPLOADED_ON + " TEXT ,"
            + SUBHEAD_LIMITS_SCHOOL_ID + " INTEGER"
            + " )";

    public long addSchoolPettyCashMonthlyLimits(ExpenseSchoolPettyCashMonthlyLimitsModel pettyCashMonthlyLimitsModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, pettyCashMonthlyLimitsModel.getId());
            cv.put(SUBHEAD_LIMITS_ALLOWED_FROM, pettyCashMonthlyLimitsModel.getAllowedFrom());
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, pettyCashMonthlyLimitsModel.getFor_date());
            cv.put(SUBHEAD_LIMITS_LIMITAMOUNT, pettyCashMonthlyLimitsModel.getLimit_amount());
            cv.put(SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT, pettyCashMonthlyLimitsModel.getSpent_amount());
            cv.put(SUBHEAD_LIMITS_ISRESTRICTED, pettyCashMonthlyLimitsModel.isRestricted());
            cv.put(HEAD_CREATED_ON, pettyCashMonthlyLimitsModel.getCreated_on());
            cv.put(HEAD_CREATED_BY, pettyCashMonthlyLimitsModel.getCreated_by());
            cv.put(HEAD_MODIFIED_BY, pettyCashMonthlyLimitsModel.getModified_by());
            cv.put(HEAD_MODIFIED_ON, pettyCashMonthlyLimitsModel.getModified_on());
            cv.put(HEAD_IS_ACTIVE, pettyCashMonthlyLimitsModel.isActive());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, pettyCashMonthlyLimitsModel.getSchool_id());
            cv.put(UPLOADED_ON,pettyCashMonthlyLimitsModel.getUploadedOn());

            i = DB.insert(TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert SchoolPettyCashMonthlyLimits record with ID: " + pettyCashMonthlyLimitsModel.getId());
            else
                AppModel.getInstance().appendLog(context, "SchoolPettyCashMonthlyLimits record inserted with ID: " + pettyCashMonthlyLimitsModel.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSchoolPettyCashMonthlyLimits Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindSchoolPettyCashMonthlyLimits(ExpenseSchoolPettyCashMonthlyLimitsModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS + " WHERE " + ID + " = " + model.getId();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public static final String TABLE_EXPENSE_AMOUNT_CLOSING = "ExpenseAmountClosing";
    public static final String AMOUNT_CLOSING_CLOSEAMOUNT = "close_amount";
    public static final String AMOUNT_CLOSING_SQL_SERVER_USER = "sqlserver_user";
    public static final String AMOUNT_CLOSING_CREATED_ON_APP = "createdOn_app";
    public static final String AMOUNT_CLOSING_CREATED_ON_SERVER = "createdOn_server";
    public final String CREATE_TABLE_AMOUNT_CLOSING = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_AMOUNT_CLOSING + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SERVER_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_MONTHLY_FOR_DATE + " VARCHAR,"
            + AMOUNT_CLOSING_CLOSEAMOUNT + " INTEGER,"
            + AMOUNT_CLOSING_SQL_SERVER_USER + " TEXT,"
            + AMOUNT_CLOSING_CREATED_ON_APP + " TEXT,"
            + AMOUNT_CLOSING_CREATED_ON_SERVER + " TEXT,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + UPLOADED_ON + " TEXT ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + SUBHEAD_LIMITS_SUBHEAD_ID + " INTEGER,"
            + SUBHEAD_LIMITS_SCHOOL_ID + " INTEGER"
            + " )";
    //TODO EXPENSE_AMOUNT_CLOSING_MODEL


    public static final String TABLE_EXPENSE_TRANSACTION_CATEGORY = "ExpenseTransactionCategory";
    public static final String TRANSACTION_CATEGORY_CATEGORY_NAME = "category_name";
    public final String CREATE_TABLE_TRANSACTION_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_TRANSACTION_CATEGORY + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + TRANSACTION_CATEGORY_CATEGORY_NAME + " VARCHAR,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN);";

    //TODO EXPENSE_TRANSACTION_CATEGORY_MODEL

    public static final String TABLE_EXPENSE_TRANSACTION_BUCKET = "ExpenseTransactionBucket";
    public static final String TRANSACTION_BUCKET_NAME = "bucket_name";
    public final String CREATE_TABLE_TRANSACTION_BUCKET = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_TRANSACTION_BUCKET + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + TRANSACTION_BUCKET_NAME + " VARCHAR,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN);";

    //TODO EXPENSE_TRANSACTION_BUCKET_MODEL

    public static final String TABLE_EXPENSE_TRANSACTION_FLOW = "ExpenseTransactionFlow";
    public static final String TRANSACTION_FLOW_NAME = "flow_name";
    public final String CREATE_TABLE_TRANSACTION_FLOW = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_TRANSACTION_FLOW + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + TRANSACTION_FLOW_NAME + " VARCHAR,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_IS_ACTIVE + " BOOLEAN);";

    //TODO EXPENSE_TRANSACTION_FLOW_MODEL

    public static final String TABLE_EXPENSE_TRANSACTIONS = "ExpenseTransactions";
    public static final String TRANSACTION_TRANSAMOUNT = "trans_amount";
    public static final String TRANSACTION_JVNO = "jv_no";
    public static final String TRANSACTION_CHEQUENO = "cheque_no";
    public static final String TRANSACTION_RECEIPTNO = "receipt_no";
    public static final String TRANSACTION_REMARKS = "remarks";
    public static final String TRANSACTION_SQLSERVER_USER = "sqlserver_user";
    public static final String TRANSACTION_CREATEDON_APP = "createdOn_app";
    public static final String TRANSACTION_CREATEDON_SERVER = "createdOn_server";
    public static final String TRANSACTION_MODIFIEDON_APP = "modifiedOn_app";
    public static final String TRANSACTION_MODIFIEDON_SERVER = "modifiedOn_server";
    public static final String TRANSACTION_CREATED_FROM = "createdFrom";
    public static final String TRANSACTION_MODIFIED_FROM = "modifiedFrom";
    public static final String TRANSACTIONS_AMOUNT_CLOSING_ID = "closing_id";//user_id
    public static final String TRANSACTIONS_TRANSACTION_CATEGORY_ID = "category_id";
    public static final String TRANSACTIONS_TRANSACTION_BUCKET_ID = "bucket_id";
    public static final String TRANSACTIONS_TRANSACTION_FLOW_ID = "flow_id";
    public static final String SERVER_ID = "server_id";
    public static final String REJECTION_COMMENTS = "rejection_comments";

    public final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_TRANSACTIONS + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SERVER_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_MONTHLY_FOR_DATE + " VARCHAR,"
            + TRANSACTION_TRANSAMOUNT + " INTEGER ,"
            + TRANSACTION_JVNO + " INTEGER ,"
            + TRANSACTION_CHEQUENO + " VARCHAR ,"
            + TRANSACTION_RECEIPTNO + " VARCHAR ,"
            + TRANSACTION_REMARKS + " TEXT ,"
            + TRANSACTION_SQLSERVER_USER + " TEXT,"
            + TRANSACTION_CREATEDON_APP + " TEXT ,"
            + TRANSACTION_CREATEDON_SERVER + " TEXT ,"
            + TRANSACTION_MODIFIEDON_APP + " TEXT ,"
            + TRANSACTION_MODIFIEDON_SERVER + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER ,"
            + HEAD_MODIFIED_BY + " INTEGER ,"
            + HEAD_MODIFIED_ON + " TEXT ,"
            + HEAD_IS_ACTIVE + " BOOLEAN ,"
            + TRANSACTION_CREATED_FROM + " VARCHAR,"
            + TRANSACTION_MODIFIED_FROM + " VARCHAR,"
            + REJECTION_COMMENTS + " VARCHAR,"
            + UPLOADED_ON + " TEXT ,"
            + SUBHEAD_LIMITS_SCHOOL_ID + " INTEGER ,"
            + KEY_SESSION_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_SUBHEAD_ID + " INTEGER ,"
            + TRANSACTIONS_AMOUNT_CLOSING_ID + " INTEGER ,"
            + SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID + " INTEGER ,"
            + TRANSACTIONS_TRANSACTION_CATEGORY_ID + " INTEGER ,"
            + TRANSACTIONS_TRANSACTION_BUCKET_ID + " INTEGER ,"
            + TRANSACTIONS_TRANSACTION_FLOW_ID + " INTEGER"
            + " )";

    //TODO EXPENSE_TRANSACTION_MODEL

    public static final String TABLE_EXPENSE_TRANSACTION_IMAGES = "ExpenseTransactionImages";
    public static final String TRANSACTION_IMAGES_IMAGE_PATH = "imagePath";
    public static final String TRANSACTION_IMAGES_IMAGE_CATEGORY = "image_category";
    public static final String TRANSACTION_IMAGES_UPLOADED_ON = "uploaded_on";
    public static final String TRANSACTIONS_IMAGES_TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_IMAGES_IS_UPLOADED = "isUploaded";

    public final String CREATE_TABLE_TRANSACTION_IMAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_TRANSACTION_IMAGES + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRANSACTION_IMAGES_IMAGE_PATH + " TEXT,"
            + TRANSACTION_IMAGES_IMAGE_CATEGORY + " TEXT,"
            + TRANSACTION_IMAGES_UPLOADED_ON + " TEXT,"
            + TRANSACTION_IMAGES_IS_UPLOADED + " INTEGER,"
            + TRANSACTIONS_IMAGES_TRANSACTION_ID + " INTEGER,"
            + HEAD_MODIFIED_ON + " TEXT"
            + " )";

    //TODO EXPENSE_TRANSACTION_BUCKET_MODEL

    public static final String TABLE_EXPENSE_TRANSACTION_EXTENDED = "ExpenseTransactionExtented";
    public static final String TRANSACTION_EXTENDED_CCC_NO = "ccc_no";
    public static final String TRANSACTION_EXTENDED_PRINCIPAL_ID = "principal_id";
    public static final String TRANSACTION_EXTENDED_ACCOUNTANT_ID = "accountant_id";
    public static final String TRANSACTION_EXTENDED_AEM_ID = "aem_id";
    public static final String TRANSACTION_EXTENDED_AM_ID = "am_id";
    public static final String TRANSACTION_EXTENDED_REM_ID = "rem_id";
    public static final String TRANSACTION_EXTENDED_RM_ID = "rm_id";
    public static final String TRANSACTION_EXTENDED_REGION_ID = "region_id";
    public static final String TRANSACTIONS_EXTENDED_AREA_ID = "area_id";

    public final String CREATE_TABLE_TRANSACTION_EXTENDED = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_TRANSACTION_EXTENDED + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + TRANSACTION_EXTENDED_CCC_NO + " INTEGER,"
            + TRANSACTION_EXTENDED_PRINCIPAL_ID + " INTEGER,"
            + TRANSACTION_EXTENDED_ACCOUNTANT_ID + " INTEGER,"
            + TRANSACTION_EXTENDED_AEM_ID + " INTEGER,"
            + TRANSACTION_EXTENDED_AM_ID + " INTEGER,"
            + TRANSACTION_EXTENDED_REM_ID + " INTEGER,"
            + TRANSACTION_EXTENDED_RM_ID + " INTEGER,"
            + TRANSACTION_EXTENDED_REGION_ID + " INTEGER,"
            + TRANSACTIONS_EXTENDED_AREA_ID + " INTEGER,"
            + HEAD_CREATED_ON + " TEXT ,"
            + HEAD_CREATED_BY + " INTEGER,"
            + TRANSACTIONS_IMAGES_TRANSACTION_ID + " INTEGER"
            + " )";

    //TODO EXPENSE_TRANSACTION_BUCKET_MODEL
    //Head  SubHead  SubHeadLimits SubHeadLimitsMonthly SubHeadExceptions SchoolPettyCashLimits SchoolPettyCashMonthlyLimits

    public String getLatestModifiedOnExpenseMetaData(int schoolId) {
        Cursor c = null;
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            String lmo = null;

            String selectQuery = "SELECT MAX(m.modifiedon) as latestModifiedOn FROM" +
                    " (SELECT " + HEAD_MODIFIED_ON + " FROM " + TABLE_EXPENSE_HEAD + " where school_id = " + schoolId +
                    " UNION all SELECT " + HEAD_MODIFIED_ON + " FROM " + TABLE_EXPENSE_SUBHEAD + " where school_id = " + schoolId +
                    " UNION all SELECT " + HEAD_MODIFIED_ON + " FROM " + TABLE_EXPENSE_TRANSACTION_BUCKET +
                    " UNION all SELECT " + HEAD_MODIFIED_ON + " FROM " + TABLE_EXPENSE_TRANSACTION_CATEGORY +
                    " UNION all SELECT " + HEAD_MODIFIED_ON + " FROM " + TABLE_EXPENSE_TRANSACTION_FLOW +
                    " ) as m";

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                lmo = AppModel.getInstance().convertDatetoFormat(c.getString(c.getColumnIndex("latestModifiedOn")),
                        "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy");
            }

            if (lmo == null) {
                lmo = "";
            }
            return lmo;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public ExpenseCheckSumModel getExpenseCheckSumCount(int schoolId) throws Exception {
        ExpenseCheckSumModel model = new ExpenseCheckSumModel();
        try {

            int subheadLimitsMonthlyCount = DatabaseHelper.getInstance(context).getSubHeadLimitsMonthlyCountForChecksum(schoolId);
            if (subheadLimitsMonthlyCount > 0)
                model.setSubheadLimitsMonthlyCount(subheadLimitsMonthlyCount);

            int schoolPettyCashMonthlyLimitCount = DatabaseHelper.getInstance(context).getSchoolPettyCashMonthlyLimitsCountForChecksum(schoolId);
            if (schoolPettyCashMonthlyLimitCount > 0)
                model.setSchoolPettyCashMonthlyLimitsCount(schoolPettyCashMonthlyLimitCount);

            int transactionsCount = DatabaseHelper.getInstance(context).getTransactionsCountForChecksum(schoolId);
            if (transactionsCount > 0)
                model.setTransactionsCount(transactionsCount);

            int amountClosingCount = DatabaseHelper.getInstance(context).getAmountClosingCountForChecksum(schoolId);
            if (amountClosingCount > 0)
                model.setAmountClosingCount(amountClosingCount);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return model;
    }

    public void deleteSubheadLimitsMonthlyRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "  WHERE school_id IN(" + schoolId + ")";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    db.delete(TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY, KEY_ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "Subhead Limits Monthly Data Flushed Successfully. School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing Subhead Limits Monthly data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
    }

    public void deleteSchoolPettyCashMonthlyLimitsRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS + "  WHERE school_id IN(" + schoolId + ")";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    db.delete(TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS, KEY_ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "School PettyCash Monthly Limits Data Flushed Successfully. School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing School PettyCash Monthly Limits data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
    }

    public void deleteTransactionsRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM " + TABLE_EXPENSE_TRANSACTIONS + "  WHERE school_id IN(" + schoolId + ")";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    db.delete(TABLE_EXPENSE_TRANSACTIONS, KEY_ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "Transactions Data Flushed Successfully. School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing Transactions data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
    }


    public void deleteAmountClosingRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM " + TABLE_EXPENSE_AMOUNT_CLOSING + "  WHERE school_id IN(" + schoolId + ")";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    db.delete(TABLE_EXPENSE_AMOUNT_CLOSING, KEY_ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "AmountClosing Data Flushed Successfully. School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing AmountClosing data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
    }

    public double getAvailableLimitAmountFromTransaction(int schoolId,int bucketId) {
        double limit_amount = 0;
        Cursor cursor = null;

        String selectQuery = "SELECT InAmount.amount - OutAmount.amount as limit_amount\n" +
                "FROM\n" +
                "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions\n" +
                "where flow_id = 1 AND bucket_id = @BucketId \n" +//flowid 1 for In
//                "and strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                "and is_active = 1\n" +
                "and school_id = @SchoolID) InAmount\n" +
                "LEFT JOIN\n" +
                "(Select ifnull(Sum(trans_amount), 0) as amount from ExpenseTransactions\n" +
                "where flow_id = 2 AND bucket_id = @BucketId \n" +//flowid 2 for Out
//                "and strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                "and is_active = 1\n" +
                "and school_id = @SchoolID) OutAmount";

        selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");
        selectQuery = selectQuery.replaceAll("@BucketId", bucketId + "");

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                limit_amount = cursor.getInt(cursor.getColumnIndex("limit_amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return limit_amount;
    }



    public double getTotalSalary(int schoolId) {
        double salary = 0;
        Cursor cursor = null;

    /*    String selectQuery = "SELECT ifnull(Sum(limit_amount), 0) As amount from " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "\n" +
                "WHERE subhead_id = 48\n" +//48 for Employee Salary
                "AND strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                "AND school_id = " + schoolId;*/

        String selectQuery = "SELECT InAmount.amount - OutAmount.amount as limit_amount\n" +
                "FROM\n" +
                "(SELECT ifnull(Sum(limit_amount), 0) As amount from " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "\n"+
                "WHERE subhead_id = 48\n" +//48 for Employee Salary
//                "AND strftime('%m',for_date) = strftime('%m',date('now'))\n"+
                "AND is_active = 1\n"+
                "AND school_id = @SchoolID) InAmount\n" +
                "LEFT JOIN\n" +
                "(SELECT ifnull(Sum(trans_amount), 0) As amount from " + TABLE_EXPENSE_TRANSACTIONS +"\n"+
                "WHERE subhead_id = 48\n" +//48 for Employee Salary
//                "AND strftime('%m',for_date) = strftime('%m',date('now'))\n"+
                "AND is_active = 1\n"+
                "AND flow_id = 2\n"+
                "AND bucket_id = 1\n"+
                "AND salary_user_id > 0\n"+
                "AND school_id = @SchoolID) OutAmount";

        selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                salary = cursor.getInt(cursor.getColumnIndex("limit_amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return salary;
    }

    public int checkSlipno(int schoolId, long receiptno, int allowedfrom) {
        Cursor c = null;
        int i = -1;
        String selectQuery;
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            if (allowedfrom == 1)
                selectQuery = "SELECT id from " + TABLE_EXPENSE_TRANSACTIONS + " where cheque_no = " + receiptno + " and school_id = " + schoolId;
            else
                selectQuery = "SELECT id from " + TABLE_EXPENSE_TRANSACTIONS + " where receipt_no = " + receiptno + " and school_id = " + schoolId;

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                i = c.getColumnIndex("id");
            }

            return i;

        } catch (Exception e) {
            e.printStackTrace();
            return i;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }


    public long insertTransaction(ExpenseTransactionModel transactionModel) {
        long i = -1;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, transactionModel.getForDate());
            cv.put(TRANSACTION_TRANSAMOUNT, transactionModel.getTransAmount());
            cv.put(TRANSACTION_JVNO, transactionModel.getJvNo());
            cv.put(TRANSACTION_CHEQUENO, transactionModel.getChequeNo());
            cv.put(TRANSACTION_RECEIPTNO, transactionModel.getReceiptNo());
            cv.put(TRANSACTION_REMARKS, transactionModel.getRemarks());
            cv.put(TRANSACTION_SQLSERVER_USER, transactionModel.getSqlserverUser());
            cv.put(TRANSACTION_CREATEDON_APP, transactionModel.getCreatedOnApp());
            cv.put(TRANSACTION_CREATEDON_SERVER, transactionModel.getCreatedOnServer());
            cv.put(TRANSACTION_MODIFIEDON_APP, transactionModel.getModifiedOnApp());
            cv.put(TRANSACTION_MODIFIEDON_SERVER, transactionModel.getModifiedOnServer());
            cv.put(HEAD_MODIFIED_BY, transactionModel.getModifiedBy());
            cv.put(HEAD_CREATED_BY, transactionModel.getCreatedBy());
            //IS_ACTIVE COMMENT
            //cv.put(HEAD_IS_ACTIVE, transactionModel.isActive());
            cv.put(TRANSACTION_CREATED_FROM, transactionModel.getCreatedFrom());
            cv.put(TRANSACTION_MODIFIED_FROM, transactionModel.getModifiedFrom());
            cv.put(UPLOADED_ON, transactionModel.getUploadedOn());

            cv.put(KEY_SESSION_ID, transactionModel.getAcademicSessionID());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, transactionModel.getSchoolID());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, transactionModel.getSubHeadID());

            if(transactionModel.getClosingID()!=null && transactionModel.getClosingID()>0)
            cv.put(TRANSACTIONS_AMOUNT_CLOSING_ID, transactionModel.getClosingID());

            if(transactionModel.getSalaryUserID()!=null && transactionModel.getSalaryUserID()>0)
            cv.put(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID, transactionModel.getSalaryUserID());

            cv.put(TRANSACTIONS_TRANSACTION_CATEGORY_ID, transactionModel.getCategoryID());
            cv.put(TRANSACTIONS_TRANSACTION_BUCKET_ID, transactionModel.getBucketID());
            cv.put(TRANSACTIONS_TRANSACTION_FLOW_ID, transactionModel.getFlowID());


            i = DB.insert(TABLE_EXPENSE_TRANSACTIONS, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert Transaction record with ID: " + i);
            else
                AppModel.getInstance().appendLog(context, "Transaction record inserted with ID: " + i);

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addTransaction Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

/*    public long insertTransactionImage(int transaction_id, ExpenseTransactionModel transactionModel) {
        long i = -1;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_IMAGE_PATH, transactionModel.getImagePath());
            //cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_UPLOADED_ON, "");
            cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_IMAGE_CATEGORY, transactionModel.getImageCategory());
            cv.put(ExpenseHelperClass.TRANSACTIONS_IMAGES_TRANSACTION_ID, transaction_id);

            i = DB.insert(ExpenseHelperClass.TABLE_EXPENSE_TRANSACTION_IMAGES, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert TransactionImages record with ID: " + i);
            else
                AppModel.getInstance().appendLog(context, "TransactionImages record inserted with ID: " + i);

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addTransactionImages Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }*/

    public int checkJvno(int schoolId, long jvno) {

        Cursor c = null;
        int i = -1;
        try {

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

            String selectQuery = "SELECT id from " + TABLE_EXPENSE_TRANSACTIONS + " where jv_no = " + jvno + " and school_id = " + schoolId;

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                i = c.getColumnIndex("id");
            }

            return i;

        } catch (Exception e) {
            e.printStackTrace();
            return i;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public ArrayList<ExpenseSubheadModel> getExpenseSubheadsforSchool(int schoolId, int head_id, String allowedFrom) {

        Cursor c = null;
        int i = -1;
        ArrayList<ExpenseSubheadModel> expenseSubheads = new ArrayList<>();
        ExpenseSubheadModel expenseSubhead;

        try {

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

            String selectQuery = "SELECT " + TABLE_EXPENSE_SUBHEAD + "." + ID + "," + TABLE_EXPENSE_SUBHEAD + "." + SUB_HEAD_NAME +
                    "," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "." + SUBHEAD_LIMITS_LIMITAMOUNT + "," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "." + SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT + " from " + TABLE_EXPENSE_SUBHEAD + "\n" +
                    "INNER JOIN " + TABLE_EXPENSE_HEAD + " ON " + TABLE_EXPENSE_HEAD + ".id = " + TABLE_EXPENSE_SUBHEAD + ".head_id\n" +
                    "INNER JOIN " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + " ON " + TABLE_EXPENSE_SUBHEAD + ".id = " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id\n" +
                    " where " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".school_id = " + schoolId +
                    " and " + TABLE_EXPENSE_HEAD + ".id = " + head_id +
                    " and "+ TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".is_active = 1\n"+
                    " and strftime('%m'," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".for_date) = strftime('%m',date('now'))\n"+
                    " and (" + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedfrom = '" + allowedFrom + "'" +
                    " or " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedfrom = 'Both')";

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    expenseSubhead = new ExpenseSubheadModel();
                    expenseSubhead.setSubhead_id(c.getInt(c.getColumnIndex("id")));
                    expenseSubhead.setSubhead_name(c.getString(c.getColumnIndex("subhead_name")));
                    expenseSubhead.setLimit_amount((double) c.getInt(c.getColumnIndex("limit_amount")));
                    expenseSubhead.setSpent_amount((double) c.getInt(c.getColumnIndex("spent_amount")));
                    expenseSubheads.add(expenseSubhead);
                }
                while (c.moveToNext());
            }


            return expenseSubheads;

        } catch (Exception e) {
            e.printStackTrace();
            return expenseSubheads;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public long insertExpenseTransactionImages(int transaction_id, SlipModel slipModel) {
        long i = -1;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_IMAGE_PATH, slipModel.getSlip_path());
            cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_IMAGE_CATEGORY, slipModel.getSlip_category());
            cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_UPLOADED_ON, (String) null);
            cv.put(ExpenseHelperClass.TRANSACTION_IMAGES_IS_UPLOADED, 0);
            cv.put(ExpenseHelperClass.TRANSACTIONS_IMAGES_TRANSACTION_ID, transaction_id);

            i = DB.insert(ExpenseHelperClass.TABLE_EXPENSE_TRANSACTION_IMAGES, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert TransactionImages record with ID: " + i);
            else
                AppModel.getInstance().appendLog(context, "TransactionImages record inserted with ID: " + i);

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addTransactionImages Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }


    public long addExpenseTransactionCategory(ExpenseTransactionCategoryModel transactionCategoryModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, transactionCategoryModel.getID());
            cv.put(TRANSACTION_CATEGORY_CATEGORY_NAME, transactionCategoryModel.getCategoryName());
            cv.put(HEAD_CREATED_ON, transactionCategoryModel.getCreatedOn());
            cv.put(HEAD_CREATED_BY, transactionCategoryModel.getCreatedBy());
            cv.put(HEAD_MODIFIED_BY, transactionCategoryModel.getModifiedBy());
            cv.put(HEAD_MODIFIED_ON, transactionCategoryModel.getModifiedOn());
            cv.put(HEAD_IS_ACTIVE, transactionCategoryModel.isActive());


            i = DB.insert(TABLE_EXPENSE_TRANSACTION_CATEGORY, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert ExpenseTransactionCategory record with ID: " + transactionCategoryModel.getID());
            else
                AppModel.getInstance().appendLog(context, "ExpenseTransactionCategory record inserted with ID: " + transactionCategoryModel.getID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addExpenseTransactionCategory Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindExpenseTransactionCategory(ExpenseTransactionCategoryModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTION_CATEGORY + " WHERE " + ID + " = " + model.getID();

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public long addExpenseTransactionFlowRecord(ExpenseTransactionFlowModel flowModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, flowModel.getID());
            cv.put(TRANSACTION_FLOW_NAME, flowModel.getFlowName());
            cv.put(HEAD_CREATED_ON, flowModel.getCreatedOn());
            cv.put(HEAD_CREATED_BY, flowModel.getCreatedBy());
            cv.put(HEAD_MODIFIED_BY, flowModel.getModifiedBy());
            cv.put(HEAD_MODIFIED_ON, flowModel.getModifiedOn());
            cv.put(HEAD_IS_ACTIVE, flowModel.isActive());

            i = DB.insert(TABLE_EXPENSE_TRANSACTION_FLOW, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert ExpenseTransactionFlow record with ID: " + flowModel.getID());
            else
                AppModel.getInstance().appendLog(context, "ExpenseTransactionFlow record inserted with ID: " + flowModel.getID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addExpenseTransactionFlow Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindExpenseTransactionFlow(ExpenseTransactionFlowModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTION_FLOW + " WHERE " + ID + " = " + model.getID();
            //+ " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public long addExpenseTransactionBucket(ExpenseTransactionBucketModel bucketModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(ID, bucketModel.getID());
            cv.put(TRANSACTION_BUCKET_NAME, bucketModel.getBucketName());
            cv.put(HEAD_CREATED_ON, bucketModel.getCreatedOn());
            cv.put(HEAD_CREATED_BY, bucketModel.getCreatedBy());
            cv.put(HEAD_MODIFIED_BY, bucketModel.getModifiedBy());
            cv.put(HEAD_MODIFIED_ON, bucketModel.getModifiedOn());
            cv.put(HEAD_IS_ACTIVE, bucketModel.isActive());

            i = DB.insert(TABLE_EXPENSE_TRANSACTION_BUCKET, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert ExpenseTransactionbucket record with ID: " + bucketModel.getID());
            else
                AppModel.getInstance().appendLog(context, "ExpenseTransactionBucket record inserted with ID: " + bucketModel.getID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addExpenseTransactionBucket Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindExpenseTransactionBucket(ExpenseTransactionBucketModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTION_BUCKET + " WHERE " + ID + " = " + model.getID();
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public long addExpenseTransaction(ExpenseTransactionModel transactionModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(SERVER_ID, transactionModel.getID());
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, transactionModel.getForDate());
            cv.put(TRANSACTION_TRANSAMOUNT, transactionModel.getTransAmount());
            cv.put(TRANSACTION_JVNO, transactionModel.getJvNo());
            cv.put(TRANSACTION_CHEQUENO, transactionModel.getChequeNo());
            cv.put(TRANSACTION_RECEIPTNO, transactionModel.getReceiptNo());
            cv.put(TRANSACTION_REMARKS, transactionModel.getRemarks());
            cv.put(TRANSACTION_SQLSERVER_USER, transactionModel.getSqlserverUser());
            cv.put(TRANSACTION_CREATEDON_APP, transactionModel.getCreatedOnApp());
            cv.put(TRANSACTION_CREATEDON_SERVER, transactionModel.getCreatedOnServer());
            cv.put(TRANSACTION_MODIFIEDON_APP, transactionModel.getModifiedOnApp());
            cv.put(TRANSACTION_MODIFIEDON_SERVER, transactionModel.getModifiedOnServer());
            cv.put(HEAD_CREATED_BY, transactionModel.getCreatedBy());
            cv.put(HEAD_MODIFIED_BY, transactionModel.getModifiedBy());
            if(transactionModel.isActive()!=null)
                cv.put(HEAD_IS_ACTIVE, transactionModel.isActive());
            cv.put(TRANSACTION_CREATED_FROM, transactionModel.getCreatedFrom());
            cv.put(TRANSACTION_MODIFIED_FROM, transactionModel.getModifiedFrom());
            cv.put(UPLOADED_ON, transactionModel.getUploadedOn());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, transactionModel.getSchoolID());
            cv.put(KEY_SESSION_ID, transactionModel.getAcademicSessionID());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, transactionModel.getSubHeadID());
            if(transactionModel.getClosingID()!=null && transactionModel.getClosingID()>0)
                cv.put(TRANSACTIONS_AMOUNT_CLOSING_ID, transactionModel.getClosingID());
            if(transactionModel.getSalaryUserID()!=null && transactionModel.getSalaryUserID()>0)
                cv.put(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID, transactionModel.getSalaryUserID());
            cv.put(TRANSACTIONS_TRANSACTION_CATEGORY_ID, transactionModel.getCategoryID());
            cv.put(TRANSACTIONS_TRANSACTION_BUCKET_ID, transactionModel.getBucketID());
            cv.put(TRANSACTIONS_TRANSACTION_FLOW_ID, transactionModel.getFlowID());



            i = DB.insert(TABLE_EXPENSE_TRANSACTIONS, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert ExpenseTransaction record with ID: " + transactionModel.getID());
            else
                AppModel.getInstance().appendLog(context, "ExpenseTransaction record inserted with ID: " + transactionModel.getID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addExpenseTransaction Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public boolean FindExpenseTransaction(ExpenseTransactionModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTIONS + " WHERE " + SERVER_ID + " = " + model.getID();
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public long addExpenseTransactionImages(ExpenseTransactionImagesModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            values.put(TRANSACTIONS_IMAGES_TRANSACTION_ID, model.getTransID());
            values.put(TRANSACTION_IMAGES_IMAGE_CATEGORY, model.getImageType());
            values.put(TRANSACTION_IMAGES_IMAGE_PATH,model.getImagePath());
            values.put(UPLOADED_ON, model.getUploadedOn());
            values.put(TRANSACTION_IMAGES_IS_UPLOADED, 1);

            long i = DB.insert(TABLE_EXPENSE_TRANSACTION_IMAGES, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert resignation image with ID: " + model.getTransID());
            else
                AppModel.getInstance().appendLog(context, "Expenses Image inserted with ID: " + model.getTransID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddExpenseImages Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public boolean FindExpenseTransactionImages(int trans_id, String filePath) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTION_IMAGES
                    + " WHERE " + TRANSACTIONS_IMAGES_TRANSACTION_ID + " = " + trans_id + " AND " + TRANSACTION_IMAGES_IMAGE_PATH + " = '" + filePath + "'";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public ArrayList<ExpenseTransactionModel> getAllTransactionsForUpload() {
        ArrayList<ExpenseTransactionModel> etmList = new ArrayList<ExpenseTransactionModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTIONS + " WHERE ( " + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ExpenseTransactionModel etm = new ExpenseTransactionModel();
                    etm.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    etm.setForDate(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_FOR_DATE)));
                    etm.setTransAmount(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TRANSAMOUNT)));
                    etm.setJvNo(cursor.getInt(cursor.getColumnIndex(TRANSACTION_JVNO)));
                    etm.setChequeNo(cursor.getString(cursor.getColumnIndex(TRANSACTION_CHEQUENO)));
                    etm.setReceiptNo(cursor.getString(cursor.getColumnIndex(TRANSACTION_RECEIPTNO)));
                    etm.setRemarks(cursor.getString(cursor.getColumnIndex(TRANSACTION_REMARKS)));
                    etm.setSqlserverUser(cursor.getString(cursor.getColumnIndex(TRANSACTION_SQLSERVER_USER)));
                    etm.setCreatedOnApp(cursor.getString(cursor.getColumnIndex(TRANSACTION_CREATEDON_APP)));
                    etm.setCreatedOnServer(cursor.getString(cursor.getColumnIndex(TRANSACTION_CREATEDON_SERVER)));
                    etm.setModifiedOnApp(cursor.getString(cursor.getColumnIndex(TRANSACTION_MODIFIEDON_APP)));
                    etm.setModifiedOnServer(cursor.getString(cursor.getColumnIndex(TRANSACTION_MODIFIEDON_SERVER)));
                    etm.setCreatedBy(cursor.getInt(cursor.getColumnIndex(HEAD_CREATED_BY)));
                    etm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(HEAD_MODIFIED_BY)));
                    /*boolean b = cursor.getString(cursor.getColumnIndex(HEAD_IS_ACTIVE)).equals("1");
                    etm.setActive(b);*/
                    if(!cursor.isNull (cursor.getColumnIndex(HEAD_IS_ACTIVE))){
                        etm.setClosingID(cursor.getInt(cursor.getColumnIndex(HEAD_IS_ACTIVE)));
                    }
                    etm.setCreatedFrom(cursor.getString(cursor.getColumnIndex(TRANSACTION_CREATED_FROM)));
                    etm.setModifiedFrom(cursor.getString(cursor.getColumnIndex(TRANSACTION_MODIFIED_FROM)));
                    etm.setUploadedOn(cursor.getString(cursor.getColumnIndex(UPLOADED_ON)));
                    etm.setSchoolID(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SCHOOL_ID)));
                    etm.setAcademicSessionID(cursor.getInt(cursor.getColumnIndex(KEY_SESSION_ID)));
                    etm.setSubHeadID(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SUBHEAD_ID)));
                    Integer closing_id,salary_user_id;
                    if(!cursor.isNull (cursor.getColumnIndex(TRANSACTIONS_AMOUNT_CLOSING_ID))){
                         closing_id = cursor.getInt(cursor.getColumnIndex(TRANSACTIONS_AMOUNT_CLOSING_ID));
                        etm.setClosingID(closing_id);
                    }
                    if(!cursor.isNull (cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID))){
                        salary_user_id = cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID));
                        etm.setSalaryUserID(salary_user_id);
                    }
                    /*else {
                        closing_id = null;
                    }*/
                    //etm.setSalaryUserID(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID)));
                    etm.setCategoryID(cursor.getInt(cursor.getColumnIndex(TRANSACTIONS_TRANSACTION_CATEGORY_ID)));
                    etm.setBucketID(cursor.getInt(cursor.getColumnIndex(TRANSACTIONS_TRANSACTION_BUCKET_ID)));
                    etm.setFlowID(cursor.getInt(cursor.getColumnIndex(TRANSACTIONS_TRANSACTION_FLOW_ID)));
                    etmList.add(etm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return etmList;
    }

    public boolean IfTransactionRecordNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTIONS + " WHERE " + SERVER_ID + " = " + id
                    + " AND (" + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = '')";

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

    public long updateTransactionRecord(ExpenseTransactionModel transactionModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, transactionModel.getForDate());
            cv.put(TRANSACTION_TRANSAMOUNT, transactionModel.getTransAmount());
            cv.put(TRANSACTION_JVNO, transactionModel.getJvNo());
            cv.put(TRANSACTION_CHEQUENO, transactionModel.getChequeNo());
            cv.put(TRANSACTION_RECEIPTNO, transactionModel.getReceiptNo());
            cv.put(TRANSACTION_REMARKS, transactionModel.getRemarks());
            cv.put(TRANSACTION_SQLSERVER_USER, transactionModel.getSqlserverUser());
            cv.put(TRANSACTION_CREATEDON_APP, transactionModel.getCreatedOnApp());
            cv.put(TRANSACTION_CREATEDON_SERVER, transactionModel.getCreatedOnServer());
            cv.put(TRANSACTION_MODIFIEDON_APP, transactionModel.getModifiedOnApp());
            cv.put(TRANSACTION_MODIFIEDON_SERVER, transactionModel.getModifiedOnServer());
            cv.put(HEAD_CREATED_BY,transactionModel.getCreatedBy());
            cv.put(HEAD_MODIFIED_BY, transactionModel.getModifiedBy());
            if(transactionModel.isActive()!=null)
                cv.put(HEAD_IS_ACTIVE, transactionModel.isActive());
            cv.put(TRANSACTION_CREATED_FROM, transactionModel.getCreatedFrom());
            cv.put(TRANSACTION_MODIFIED_FROM, transactionModel.getModifiedFrom());
            cv.put(UPLOADED_ON, transactionModel.getUploadedOn());

            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, transactionModel.getSchoolID());
            cv.put(TRANSACTIONS_TRANSACTION_CATEGORY_ID, transactionModel.getCategoryID());
            cv.put(TRANSACTIONS_TRANSACTION_BUCKET_ID, transactionModel.getBucketID());
            cv.put(TRANSACTIONS_TRANSACTION_FLOW_ID, transactionModel.getFlowID());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, transactionModel.getSubHeadID());
            if(transactionModel.getClosingID()!=null && transactionModel.getClosingID()>0)
                cv.put(TRANSACTIONS_AMOUNT_CLOSING_ID, transactionModel.getClosingID());
            if(transactionModel.getSalaryUserID()!=null && transactionModel.getSalaryUserID()>0)
                cv.put(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID, transactionModel.getSalaryUserID());


            long i = DB.update(TABLE_EXPENSE_TRANSACTIONS, cv, SERVER_ID + " =" + transactionModel.getID(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update AllTransaction Record ID: " + transactionModel.getID());
            } else {
                AppModel.getInstance().appendLog(context, "AllTransaction Record updated ID: " + transactionModel.getID());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateTransactionRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public List<ExpenseTransactionImagesModel> getTransactionImagesForUpload() {
        List<ExpenseTransactionImagesModel> sepImages = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * from " + TABLE_EXPENSE_TRANSACTION_IMAGES +
                " WHERE (" + TRANSACTION_IMAGES_UPLOADED_ON + " IS NULL OR " + TRANSACTION_IMAGES_UPLOADED_ON + " = '')";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ExpenseTransactionImagesModel model = new ExpenseTransactionImagesModel();
                    model.setImagePath(cursor.getString(cursor.getColumnIndex(TRANSACTION_IMAGES_IMAGE_PATH)));
                    model.setImageType(cursor.getString(cursor.getColumnIndex(TRANSACTION_IMAGES_IMAGE_CATEGORY)));
                    model.setTransID(cursor.getInt(cursor.getColumnIndex(TRANSACTIONS_IMAGES_TRANSACTION_ID)));
                    sepImages.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sepImages;
    }

    public boolean IfTransactionImageNotUploaded(int transaction_id, String path) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_TRANSACTION_IMAGES
                    + " WHERE " + TRANSACTIONS_IMAGES_TRANSACTION_ID + " = " + transaction_id
                    + " AND " + TRANSACTION_IMAGES_IMAGE_PATH + " = '" + path + "'"
                    + " AND " + UPLOADED_ON + " IS NULL";

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

    public long updateTransactionImage(ExpenseTransactionImagesModel sam) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(TRANSACTION_IMAGES_IMAGE_PATH, sam.getImagePath());
            values.put(TRANSACTION_IMAGES_IMAGE_CATEGORY, sam.getImageType());
            values.put(UPLOADED_ON, sam.getUploadedOn());
            values.put(TRANSACTION_IMAGES_IS_UPLOADED, 1);

            long i = DB.update(TABLE_EXPENSE_TRANSACTION_IMAGES, values, TRANSACTIONS_IMAGES_TRANSACTION_ID + " = " + sam.getTransID() + " AND " + TRANSACTION_IMAGES_IMAGE_PATH + " = '" + sam.getImagePath() + "'", null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Transaction Image with ID: " + sam.getTransID());
            } else {
                AppModel.getInstance().appendLog(context, "Record updated Transaction Image with ID: " + sam.getTransID());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateTransactionImage Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

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

    public double getAvailableAmountSubheadLimitsMOnthly(int schoolId, int subhead_id, String allowedFrom) {
        double limit_amount = 0;
        Cursor cursor = null;

      /*  String selectQuery = "SELECT InAmount.amount - OutAmount.amount as limit_amount\n" +
                "FROM\n" +
                "(Select ifnull(limit_amount, 0)  as amount from " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "\n"+
                "INNER JOIN " + TABLE_EXPENSE_SUBHEAD + " ON " + TABLE_EXPENSE_SUBHEAD + ".id = " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id\n"+
                "INNER JOIN " + TABLE_EXPENSE_HEAD + " ON " + TABLE_EXPENSE_HEAD + ".id = " + TABLE_EXPENSE_SUBHEAD + ".head_id\n"+
                "where " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id = @SubHead_id "+
                "and strftime('%m'," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".for_date) = strftime('%m',date('now')) " +
                "and "+ TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY+ ".school_id = @SchoolID "+
                "and ("+ TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY+ ".allowedFrom = '@AllowedFrom' or "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = 'Both')) InAmount\n" +
                "LEFT JOIN\n" +
                "(Select ifnull(spent_amount, 0)  as amount from " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "\n"+
                "INNER JOIN " + TABLE_EXPENSE_SUBHEAD + " ON " + TABLE_EXPENSE_SUBHEAD + ".id = " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id\n" +
                "INNER JOIN " + TABLE_EXPENSE_HEAD + " ON " + TABLE_EXPENSE_HEAD + ".id = " + TABLE_EXPENSE_SUBHEAD + ".head_id\n" +
                "where " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id = @SubHead_id "+
                "and strftime('%m'," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".for_date) = strftime('%m',date('now')) " +
                "and "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".school_id = @SchoolID "+
                "and ("+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = '@AllowedFrom' or "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = 'Both')) OutAmount";
*/

            String selectQuery = "SELECT IAmount.amount - OAmount.amount as limit_amount\n" +
                    "FROM\n" +
                    "(Select ifnull(limit_amount, 0)  as amount from ExpenseSubHeadLimitsMonthly\n" +
                    "INNER JOIN ExpenseSubHead ON ExpenseSubHead.id = ExpenseSubHeadLimitsMonthly.subhead_id\n" +
                    "INNER JOIN ExpenseHead ON ExpenseHead.id = ExpenseSubHead.head_id\n" +
                    "where ExpenseSubHeadLimitsMonthly.subhead_id = @SubHead_id\n" +
                    "and strftime('%m',ExpenseSubHeadLimitsMonthly.for_date) = strftime('%m',date('now'))\n" +
                    "and ExpenseSubHeadLimitsMonthly.school_id = @SchoolID and ExpenseSubHeadLimitsMonthly.is_active = 1\n" +
                    "and (ExpenseSubHeadLimitsMonthly.allowedFrom = '@AllowedFrom' or ExpenseSubHeadLimitsMonthly.allowedFrom = 'Both')) IAmount\n" +
                    "LEFT JOIN\n" +
                    "(SELECT OutAmount.amount - InAmount.amount as amount\n" +
                    "FROM\n" +
                    "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions\n" +
                    "where strftime('%m',for_date) = strftime('%m',date('now'))and school_id = @SchoolID and flow_id = 2 and subhead_id = @SubHead_id and is_active = 1) OutAmount\n" +
                    "LEFT JOIN\n" +
                    "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions\n" +
                    "where strftime('%m',for_date) = strftime('%m',date('now'))and school_id = @SchoolID and flow_id = 1 and subhead_id = @SubHead_id and is_active = 1) InAmount) OAmount";


        selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");
        selectQuery = selectQuery.replaceAll("@AllowedFrom", allowedFrom + "");
        selectQuery = selectQuery.replaceAll("@SubHead_id", subhead_id + "");


        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                limit_amount = cursor.getInt(cursor.getColumnIndex("limit_amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return limit_amount;
    }

    public double getAvailableAmountPettyCashLimitsMOnthly(int schoolId) {
        double limit_amount = 0;
        Cursor cursor = null;

       /* String selectQuery = "SELECT InAmount.amount - OutAmount.amount as limit_amount\n" +
                "FROM\n" +
                "(Select ifnull(Sum(limit_amount), 0)  as amount from " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS+"\n"+
                "where strftime('%m',for_date) = strftime('%m',date('now'))"+
                "and school_id = @SchoolID) InAmount\n"+
                "LEFT JOIN\n" +
                "(Select ifnull(Sum(spent_amount), 0)  as amount from " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS+"\n"+
                "where strftime('%m',for_date) = strftime('%m',date('now')) "+
                "and school_id = @SchoolID) OutAmount";*/

        String selectQuery2 = "SELECT IAmount.amount - OAmount.amount as limit_amount\n"+
        "FROM (Select ifnull(Sum(limit_amount), 0)  as amount from ExpenseSchoolPettyCashMonthlyLimits\n"+
        "where strftime('%m',for_date) = strftime('%m',date('now')) and is_active = 1 and school_id = @SchoolID) IAmount\n"+
        "LEFT JOIN\n"+
        "(SELECT OutAmount.amount - InAmount.amount as amount\n"+
        "FROM (Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions et inner join  ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id\n"+
        "where strftime('%m',et.for_date) = strftime('%m',date('now'))and et.school_id = @SchoolID and et.flow_id = 2 and esh.head_id = 2 and et.is_active = 1) OutAmount\n"+
        "LEFT JOIN\n"+
        "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions et inner join  ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id\n"+
        "where strftime('%m',et.for_date) = strftime('%m',date('now'))and et.school_id = @SchoolID and et.flow_id = 1 and esh.head_id = 2 and et.is_active = 1) InAmount) OAmount";

         selectQuery2 = selectQuery2.replaceAll("@SchoolID", schoolId + "");


        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery2, null);

            if (cursor.moveToFirst()) {
                limit_amount = cursor.getInt(cursor.getColumnIndex("limit_amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return limit_amount;
    }


    public double getAvailableSpentAmountPettyCashLimitsMOnthly(int schoolId) {
        double spent_amount = 0;
        Cursor cursor = null;

        String selectQuery = "Select ifnull(Sum(spent_amount), 0)  as amount from " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS + "\n" +
                "where strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                "and school_id = @SchoolID";

        selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();

            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                spent_amount = cursor.getInt(cursor.getColumnIndex("amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return spent_amount;
    }

    public long updateSchoolPettyCashMonthlyLimits(Context context, int school_id, double spent_amount) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT,spent_amount);
            values.put(HEAD_MODIFIED_BY,DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            values.put(UPLOADED_ON,(String) null);
            values.put(HEAD_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            long i = DB.update(TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS, values,   "strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                    "and school_id = " + school_id, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateSubheadLimitsMonthly(Context context,int subhead_id,int schoolId,String allowedFrom, double spent_amount) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT,spent_amount);
            values.put(HEAD_MODIFIED_BY,DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            values.put(UPLOADED_ON,(String) null);
            values.put(HEAD_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            long i = DB.update(TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY, values,    TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id = " + subhead_id + "\n" +
                    "and strftime('%m'," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".for_date) = strftime('%m',date('now'))\n" +
                    "and "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".school_id = " + schoolId + "\n" +
                    "and ("+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = '"+allowedFrom+"' or "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = 'Both')", null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean markSalaryPaid(int empId) {

        String updateQuery = "update ExpenseSubHeadLimitsMonthly set spent_amount = limit_amount where id = " + empId;

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL(updateQuery);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public List<EmployeeModel> getEmployeesForSalary(int schoolId) {
        List<EmployeeModel> empList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "select ed.id, ed.Employee_Code, ed.first_name, ed.last_name from EmployeeDetails ed " +
                    "inner join EmployeeSchool emp_school on  emp_school.emp_detail_id = ed.id where ed.id not in \n" +
                    "(SELECT salary_user_id from ExpenseTransactions where salary_user_id > 0 and (is_active = 1 or is_active is NULL))" +
                    " and emp_school.schoolId = " + schoolId;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeModel employeeModel = new EmployeeModel();
                    employeeModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex("Employee_Code")));
                    employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex("First_Name")));
                    employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex("Last_Name")));
                    empList.add(employeeModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return empList;
    }

    public int getSalary(int empId) {
        int salary = 0;
        Cursor cursor = null;
        if (empId > 0) {
            try {
//                String selectQuery = "select SUM(limit_amount) AS USER_SALARY from ExpenseSubHeadLimitsMonthly where salary_user_id = " + empId + " and (spent_amount = 0 OR spent_amount IS NULL)";
                String selectQuery = "select SUM(limit_amount) AS USER_SALARY from ExpenseSubHeadLimitsMonthly where salary_user_id = " + empId;

                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    salary = cursor.getInt(cursor.getColumnIndex("USER_SALARY"));
    //                do {
    //
    //                } while (cursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return salary;
    }



    public String getLatestModifiedOnExpenseData(int schoolId) {
        Cursor c = null;
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            String lmo = null;

            String selectQuery = "SELECT MAX(m.modifiedon) as latestModifiedOn FROM" +
                    " (SELECT "+HEAD_MODIFIED_ON+" FROM "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY+" where school_id = " + schoolId +
                    " UNION all SELECT "+HEAD_MODIFIED_ON+" FROM "+TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS+" where school_id = " + schoolId +
                    " UNION all SELECT "+HEAD_MODIFIED_ON+" FROM "+TABLE_EXPENSE_TRANSACTIONS+" where school_id = " + schoolId +
                    " UNION all SELECT "+HEAD_MODIFIED_ON+" FROM "+TABLE_EXPENSE_AMOUNT_CLOSING+" where school_id = " + schoolId +
                    " ) as m";

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                lmo = AppModel.getInstance().convertDatetoFormat(c.getString(c.getColumnIndex("latestModifiedOn")),
                        "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy");
            }

            if (lmo == null) {
                lmo = "";
            }
            return lmo;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public ArrayList<ExpenseSubheadLimitsMonthlyModel> getAllSubheadLimitsMonthlyForUpload() {
        ArrayList<ExpenseSubheadLimitsMonthlyModel> etmList = new ArrayList<ExpenseSubheadLimitsMonthlyModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + " WHERE ( " + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    ExpenseSubheadLimitsMonthlyModel etm = new ExpenseSubheadLimitsMonthlyModel();
                    etm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    etm.setAllowed_from(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_ALLOWED_FROM)));
                    etm.setFor_date(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_FOR_DATE)));
                    etm.setLimit_amount(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_LIMITAMOUNT)));
                    etm.setSpent_amount(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT)));
                    boolean b = cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_ISRESTRICTED)).equals("1") ? true : false;
                    etm.setIs_restricted(b);
                    etm.setCreated_on(cursor.getString(cursor.getColumnIndex(HEAD_CREATED_ON)));
                    etm.setCreated_by(cursor.getInt(cursor.getColumnIndex(HEAD_CREATED_BY)));
                    etm.setModified_on(cursor.getString(cursor.getColumnIndex(HEAD_MODIFIED_ON)));
                    etm.setModified_by(cursor.getInt(cursor.getColumnIndex(HEAD_MODIFIED_BY)));
                    boolean c = cursor.getString(cursor.getColumnIndex(HEAD_IS_ACTIVE)).equals("1") ? true : false;
                    etm.setActive(c);
                    etm.setUploadedOn(cursor.getString(cursor.getColumnIndex(UPLOADED_ON)));
                    etm.setSubhead_id(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SUBHEAD_ID)));
                    etm.setSchool_id(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SCHOOL_ID)));
                    etm.setSalary_userid(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_USER_SALARY_ID)));
                    etmList.add(etm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return etmList;
    }


    public ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> getAllSchoolPettyCashMonthlyLimitsForUpload() {
        ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> etmList = new ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS + " WHERE ( " + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ExpenseSchoolPettyCashMonthlyLimitsModel etm = new ExpenseSchoolPettyCashMonthlyLimitsModel();
                    etm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    etm.setAllowedFrom(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_ALLOWED_FROM)));
                    etm.setFor_date(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_FOR_DATE)));
                    etm.setLimit_amount(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_LIMITAMOUNT)));
                    etm.setSpent_amount(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_SPENTAMOUNT)));
                    boolean b = cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_ISRESTRICTED)).equals("1") ? true : false;
                    etm.setRestricted(b);
                    etm.setCreated_on(cursor.getString(cursor.getColumnIndex(HEAD_CREATED_ON)));
                    etm.setCreated_by(cursor.getInt(cursor.getColumnIndex(HEAD_CREATED_BY)));
                    etm.setModified_on(cursor.getString(cursor.getColumnIndex(HEAD_MODIFIED_ON)));
                    etm.setModified_by(cursor.getInt(cursor.getColumnIndex(HEAD_MODIFIED_BY)));
                    boolean c = cursor.getString(cursor.getColumnIndex(HEAD_IS_ACTIVE)).equals("1") ? true : false;
                    etm.setActive(c);
                    etm.setUploadedOn(cursor.getString(cursor.getColumnIndex(UPLOADED_ON)));
                    etm.setSchool_id(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SCHOOL_ID)));
                    etmList.add(etm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return etmList;
    }

    public ArrayList<TransactionListingModel> getFilteredTransactions(TransactionListingSearchModel searchModel, boolean isLimited) {
        ArrayList<TransactionListingModel> tlmList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT et.*,et.for_date,eh.headname,esh.subhead_name,etb.bucket_name,etf.flow_name from ExpenseTransactions et INNER JOIN ExpenseSubHead esh on esh.id = et.subhead_id " +
                    "inner join ExpenseHead eh on esh.head_id = eh.id " +
                    "inner join ExpenseTransactionBucket etb on et.bucket_id = etb.id " +
                    "inner join ExpenseTransactionFlow etf on et.flow_id = etf.id " +
                    "WHERE et.school_id = " + searchModel.getSchoolId();
            if(searchModel.getJvNo() > 0)
                selectQuery += " AND et.jv_no = " + searchModel.getJvNo();

            if(!Strings.isEmptyOrWhitespace(searchModel.getStartDate()) || !Strings.isEmptyOrWhitespace(searchModel.getEndDate())){
                selectQuery += " AND et.for_date BETWEEN '" + searchModel.getStartDate() + "' AND '" + searchModel.getEndDate() +"'";
            }

            if(!Strings.isEmptyOrWhitespace(searchModel.getStatus()) && !searchModel.getStatus().contains("all")) {
                boolean more = false;

                selectQuery += " AND (";
                if (searchModel.getStatus().contains("pending")) {
                    selectQuery += " et.is_active IS NULL";
                    more = true;
                }
                if (searchModel.getStatus().contains("active")) {
                    if (more)
                        selectQuery += " OR ";
                    selectQuery += " et.is_active = 1";
                    more = true;
                }
                if (searchModel.getStatus().contains("rejected")) {
                    if (more)
                        selectQuery += " OR ";
                    selectQuery += " et.is_active = 0";
                }

                selectQuery += " )";

            }
            if(!Strings.isEmptyOrWhitespace(searchModel.getBucket()) && !searchModel.getBucket().contains("all")) {
                if(searchModel.getBucket().contains("bank"))
                    selectQuery += " AND et.bucket_id = 1";
                else if(searchModel.getBucket().contains("cash"))
                    selectQuery += " AND et.bucket_id = 2";
            }

            if(searchModel.getSubheadId() == 1 && !Strings.isEmptyOrWhitespace(searchModel.getSubHead())){
                selectQuery += " AND (esh.head_id = 1";
                if (!searchModel.getSubHead().contains("all")) {
                    if (searchModel.getSubHead().contains("salary")) {
                        selectQuery += " OR esh.head_id = 3";
                    }
                    if (searchModel.getSubHead().contains("advance")) {
                        selectQuery += " OR esh.head_id = 4";
                    }
                    if (searchModel.getSubHead().contains("pattycash")) {
                        selectQuery += " OR esh.head_id = 2";
                    }


                } else {
                    selectQuery += " OR esh.head_id = 2 OR esh.head_id = 3 OR esh.head_id = 4";
                }

                selectQuery += " )";


            } else {

                if (searchModel.getSubheadId() == 1) {
                    selectQuery += " AND esh.head_id = 1";
                } else {
                    if (!Strings.isEmptyOrWhitespace(searchModel.getSubHead())) {
                        if (!searchModel.getSubHead().contains("all")) {
                            boolean more = false;

                            selectQuery += " AND (";
                            if (searchModel.getSubHead().contains("salary")) {
                                selectQuery += " esh.head_id = 3";
                                more = true;
                            }
                            if (searchModel.getSubHead().contains("advance")) {
                                if (more)
                                    selectQuery += " OR ";
                                selectQuery += " esh.head_id = 4";
                                more = true;
                            }
                            if (searchModel.getSubHead().contains("pettycash")) {
                                if (more)
                                    selectQuery += " OR ";
                                selectQuery += " esh.head_id = 2";
                            }

                            selectQuery += " )";
                        } else {
                            selectQuery += " AND (esh.head_id = 2 OR esh.head_id = 3 OR esh.head_id = 4)";
                        }

                    }
                }

            }

            if(isLimited)
                selectQuery += " LIMIT 5";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TransactionListingModel tlm = new TransactionListingModel();
                    tlm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    tlm.setDate(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_FOR_DATE)));
                    tlm.setAmount(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TRANSAMOUNT)));
                    tlm.setHead(cursor.getString(cursor.getColumnIndex(HEAD_NAME)));
                    tlm.setSub_head(cursor.getString(cursor.getColumnIndex(SUB_HEAD_NAME)));
                    tlm.setBucket(cursor.getString(cursor.getColumnIndex(TRANSACTION_BUCKET_NAME)));
                    tlm.setI_o(cursor.getString(cursor.getColumnIndex(TRANSACTION_FLOW_NAME)));
                    if(tlm.getHead().toLowerCase().contains("transfer"))
                        tlm.setTransactiontype("Transfer");
                    else
                        tlm.setTransactiontype("Expense");
                    tlm.setJvno(String.valueOf(cursor.getInt(cursor.getColumnIndex(TRANSACTION_JVNO))));
                    tlm.setCheckno(String.valueOf(cursor.getInt(cursor.getColumnIndex(TRANSACTION_CHEQUENO))));
                    tlm.setCategory_id(cursor.getInt(cursor.getColumnIndex(TRANSACTIONS_TRANSACTION_CATEGORY_ID)));
                    tlm.setActive(cursor.getInt(cursor.getColumnIndex(HEAD_IS_ACTIVE)));
//                    erm.setResignationID(cursor.getInt(cursor.getColumnIndex(Employee_Resignation_Id)));
//                    erm.setSeparationAttachment(cursor.getString(cursor.getColumnIndex(SeparationAttachment)));
                    tlmList.add(tlm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return tlmList;
    }

    public boolean FindAmountClosing(ExpenseAmountClosingModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_AMOUNT_CLOSING + " WHERE " + ID + " = " + model.getID();

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
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

    public long addAmountClosing(ExpenseAmountClosingModel amountClosingModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            ContentValues cv = new ContentValues();
            cv.put(SERVER_ID, amountClosingModel.getID());
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, amountClosingModel.getForDate());
            cv.put(AMOUNT_CLOSING_CLOSEAMOUNT, amountClosingModel.getClosingAmount());
            cv.put(AMOUNT_CLOSING_SQL_SERVER_USER, amountClosingModel.getSqlServerUser());
            cv.put(AMOUNT_CLOSING_CREATED_ON_APP, amountClosingModel.getCreatedOnApp());
            cv.put(AMOUNT_CLOSING_CREATED_ON_SERVER, amountClosingModel.getCreatedOnServer());
            cv.put(HEAD_CREATED_BY,amountClosingModel.getCreatedBy());
            cv.put(UPLOADED_ON,amountClosingModel.getUploadedOn());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID,amountClosingModel.getSubhead_id());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID,amountClosingModel.getSchoolID());


            i = DB.insert(TABLE_EXPENSE_AMOUNT_CLOSING, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert AmountClosing record with ID: " + amountClosingModel.getID());
            else
                AppModel.getInstance().appendLog(context, "AmountClosing record inserted with ID: " + amountClosingModel.getID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addAmountClosing Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }


    public ArrayList<ExpenseAmountClosingModel> getAllAmountClosingForUpload() {
        ArrayList<ExpenseAmountClosingModel> etmList = new ArrayList<ExpenseAmountClosingModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_AMOUNT_CLOSING + " WHERE ( " + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ExpenseAmountClosingModel etm = new ExpenseAmountClosingModel();
                    etm.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    etm.setForDate(cursor.getString(cursor.getColumnIndex(SUBHEAD_LIMITS_MONTHLY_FOR_DATE)));
                    etm.setClosingAmount(cursor.getInt(cursor.getColumnIndex(AMOUNT_CLOSING_CLOSEAMOUNT)));
                    etm.setSqlServerUser(cursor.getString(cursor.getColumnIndex(AMOUNT_CLOSING_SQL_SERVER_USER)));
                    etm.setCreatedOnApp(cursor.getString(cursor.getColumnIndex(AMOUNT_CLOSING_CREATED_ON_APP)));
                    etm.setCreatedOnServer(cursor.getString(cursor.getColumnIndex(AMOUNT_CLOSING_CREATED_ON_SERVER)));
                    etm.setCreatedBy(cursor.getInt(cursor.getColumnIndex(HEAD_CREATED_BY)));
                    etm.setUploadedOn(cursor.getString(cursor.getColumnIndex(UPLOADED_ON)));
                    etm.setSubhead_id(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SUBHEAD_ID)));
                    etm.setSchoolID(cursor.getInt(cursor.getColumnIndex(SUBHEAD_LIMITS_SCHOOL_ID)));

                    etmList.add(etm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return etmList;
    }


    public void updateServerIdInTransaction(ArrayList<TransactionUploadResponseModel> body, SyncDownloadUploadModel syncDownloadUploadModel) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();
            int uploadedCount = 0;

            for (TransactionUploadResponseModel model : body) {
                if (model.getServer_id() > 0) {
                    long i;
                    if (Strings.isEmptyOrWhitespace(model.ErrorMessage)) { //must not be null or 0 after upload
                        ContentValues values = new ContentValues();
                        values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        values.put(SERVER_ID, model.server_id);
                        values.put(HEAD_IS_ACTIVE,1);
                        i = DB.update(TABLE_EXPENSE_TRANSACTIONS, values, ID + " = " + model.device_id, null);
                        if(i > 0){
                            AppModel.getInstance().appendLog(context, "TransactionId in TABLE_ransaction is updated: " + model.device_id);
                        } else
                            AppModel.getInstance().appendLog(context, "FAILED: TransactionId in TABLE_Transaction is not updated: " + model.device_id);

                        if (i > 0) {
                            AppModel.getInstance().appendLog(context, "Transactionn Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());
                            uploadedCount++;
                            ContentValues cv = new ContentValues();
                            cv.put(TRANSACTIONS_IMAGES_TRANSACTION_ID,model.server_id);
                            cv.put(TRANSACTION_IMAGES_IS_UPLOADED, 1);
                            i = DB.update(TABLE_EXPENSE_TRANSACTION_IMAGES, cv, TRANSACTIONS_IMAGES_TRANSACTION_ID + " = " + model.device_id, null);
                            if(i > 0){
                                AppModel.getInstance().appendLog(context, "TransactionId in TABLE_ransactionImages is updated: " + model.device_id);
                            } else
                                AppModel.getInstance().appendLog(context, "FAILED: TransactionId in TABLE_TransactionImages is not updated: " + model.device_id);
                        }
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        values.put(SERVER_ID, model.server_id);
                        values.put(HEAD_IS_ACTIVE,0);
                        values.put(REJECTION_COMMENTS,model.ErrorMessage);
                        i = DB.update(TABLE_EXPENSE_TRANSACTIONS, values, ID + " = " + model.device_id, null);
                        if(i > 0){
                            AppModel.getInstance().appendLog(context, "TransactionId in TABLE_ransaction is updated: " + model.device_id);
                        } else
                            AppModel.getInstance().appendLog(context, "FAILED: TransactionId in TABLE_Transaction is not updated: " + model.device_id);

    //                    AppModel.getInstance().appendLog(context, "Transactions not uploaded, Error: " + model.ErrorMessage + " Device id: " + model.device_id);
    //                    AppModel.getInstance().appendErrorLog(context, "Transactions not uploaded, Error: " + model.ErrorMessage + " Device id: " + model.device_id);
    //                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                    }

                    AppModel.getInstance().appendLog(context, "Transactions Uploading Successful server id:" + model.server_id);
                } else {
                    AppModel.getInstance().appendErrorLog(context, "Transactions Upload Failed device id:" + model.getDevice_id() + ", Error Message: " + model.getErrorMessage());
                    AppModel.getInstance().appendLog(context, "Transactions Upload Failed device id:" + model.getDevice_id() + ", Error Message: " + model.getErrorMessage());
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DB.endTransaction();
        }
    }

    public boolean IfAmountClosingRecordNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_AMOUNT_CLOSING + " WHERE " + ID + " = " + id
                    + " AND " + UPLOADED_ON + " IS NULL";

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


    public long updateAmountClosingRecord(ExpenseAmountClosingModel amountClosingModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, amountClosingModel.getForDate());
            cv.put(AMOUNT_CLOSING_CLOSEAMOUNT, amountClosingModel.getClosingAmount());
            cv.put(AMOUNT_CLOSING_SQL_SERVER_USER, amountClosingModel.getSqlServerUser());
            cv.put(AMOUNT_CLOSING_CREATED_ON_APP, amountClosingModel.getCreatedOnApp());
            cv.put(AMOUNT_CLOSING_CREATED_ON_SERVER, amountClosingModel.getCreatedOnServer());
            cv.put(HEAD_CREATED_BY,amountClosingModel.getCreatedBy());
            cv.put(UPLOADED_ON,amountClosingModel.getUploadedOn());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID,amountClosingModel.getSubhead_id());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID,amountClosingModel.getSchoolID());


            long i = DB.update(TABLE_EXPENSE_AMOUNT_CLOSING, cv, SERVER_ID + " =" + amountClosingModel.getID(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update AllAmountClosing Record ID: " + amountClosingModel.getID());
            } else {
                AppModel.getInstance().appendLog(context, "AllAmountClosing Record updated ID: " + amountClosingModel.getID());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateAmountClosingRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public String getClosingDate(int schoolId) {
        String closingDate = "";
        Cursor cursor = null;
        try {
            String selectQuery = "select MIN(for_date) as date from ExpenseAmountClosing where school_id = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                closingDate = cursor.getString(cursor.getColumnIndex("date"));
                if(closingDate != null)
                    return closingDate;
                else
                {
                    try {
                        selectQuery = "SELECT min(for_date) as date from ExpenseTransactions where school_id = " + schoolId + " and (closing_id = 0 or closing_id is null) AND is_active = 1";

                        db = DatabaseHelper.getInstance(context).getDB();
                        cursor = db.rawQuery(selectQuery, null);

                        if (cursor.moveToFirst()) {
                            closingDate = cursor.getString(cursor.getColumnIndex("date"));
                            if(closingDate != null)
                                return closingDate;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return closingDate;
    }


    public List<Integer> getAmountsForClosing(int schoolId, String closingDate) {
        List<Integer> amountList = new ArrayList<>();
        int spentAmount = -1, totalExpense = -1;
        Cursor cursor = null;
        try {
            String selectQuery = "select SUM(limit_amount) as limitAmount , SUM (spent_amount) as spentAmount from ExpenseSubHeadLimitsMonthly eshlm " +
                    "inner join ExpenseSubHead esh on esh.id = eshlm.subhead_id " +
                    "inner join ExpenseHead eh on eh.id = esh.head_id\n" +
                    "where eh.id = 2 and eshlm.for_date > '" + closingDate + "' and eshlm.school_id = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                totalExpense = cursor.getInt(cursor.getColumnIndex("limitAmount"));
                //spentAmount = cursor.getInt(cursor.getColumnIndex("spentAmount"));
                amountList.add(totalExpense);
                //amountList.add(spentAmount);
                return amountList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return amountList;
    }

    public List<ExpenseClosingItem> getPettyCashHeadsForClosing(int schoolId, String closingDate) {
        List<ExpenseClosingItem> expenseClosingItems = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "select et.subhead_id, et.trans_amount, esh.subhead_name, et.flow_id, et.category_id from ExpenseTransactions et " +
                    "inner join ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id " +
                    "where eh.id = 2 and et.for_date > '" + closingDate + "' and et.is_active = 1 and et.school_id = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ExpenseClosingItem expenseClosingItem = new ExpenseClosingItem();
                    expenseClosingItem.setSpentAmount(String.valueOf(cursor.getInt(cursor.getColumnIndex("trans_amount"))));
                    expenseClosingItem.setHeadName(cursor.getString(cursor.getColumnIndex("subhead_name")));
                    expenseClosingItem.setFlow_id(cursor.getInt(cursor.getColumnIndex("flow_id")));
                    expenseClosingItem.setCategory_id(cursor.getInt(cursor.getColumnIndex("category_id")));
                    expenseClosingItems.add(expenseClosingItem);
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return expenseClosingItems;
    }

    public ExpenseTransactionModel getSingleTransaction(int transaction_id){
        ExpenseTransactionModel etm = new ExpenseTransactionModel();
        Cursor cursor = null;
        try {
            String selectQuery = "select * from "+TABLE_EXPENSE_TRANSACTIONS+" where id = "+transaction_id;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    etm.setID(cursor.getInt(cursor.getColumnIndex("server_id")));
                    etm.setJvNo(cursor.getInt(cursor.getColumnIndex("jv_no")));
                    etm.setSchoolID(cursor.getInt(cursor.getColumnIndex("school_id")));
                    etm.setChequeNo(cursor.getString(cursor.getColumnIndex("cheque_no")));
                    etm.setReceiptNo(cursor.getString(cursor.getColumnIndex("receipt_no")));
                    etm.setTransAmount(cursor.getInt(cursor.getColumnIndex("trans_amount")));
                    etm.setSqlserverUser(cursor.getString(cursor.getColumnIndex("sqlserver_user")));
                    etm.setCreatedOnServer(cursor.getString(cursor.getColumnIndex("createdOn_server")));
                    etm.setModifiedOnServer(cursor.getString(cursor.getColumnIndex("modifiedOn_server")));
                    etm.setCreatedBy(cursor.getInt(cursor.getColumnIndex("createdby")));
                    etm.setModifiedBy(cursor.getInt(cursor.getColumnIndex("modifiedby")));
                    etm.setCreatedFrom(cursor.getString(cursor.getColumnIndex("createdFrom")));
                    etm.setModifiedFrom(cursor.getString(cursor.getColumnIndex("modifiedFrom")));
                    etm.setAcademicSessionID(cursor.getInt(cursor.getColumnIndex("session_id")));
                    etm.setSubHeadID(cursor.getInt(cursor.getColumnIndex("subhead_id")));
                    etm.setBucketID(cursor.getInt(cursor.getColumnIndex("bucket_id")));
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return etm;
    }

    public List<SlipModel> getExpenseTransactionImages(int transaction_id){
        List<SlipModel> eti = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "select * from "+TABLE_EXPENSE_TRANSACTION_IMAGES+" where "+TRANSACTIONS_IMAGES_TRANSACTION_ID+" = "+transaction_id;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    SlipModel etim = new SlipModel();
                    etim.setSlip_category(cursor.getString(cursor.getColumnIndex("image_category")));
                    etim.setSlip_path(cursor.getString(cursor.getColumnIndex("imagePath")));
                    eti.add(etim);
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return eti;
    }


    public double getAvailableSpentAmountSubheadLimitsMOnthly(int schoolId, int subhead_id, String allowedFrom) {
        double limit_amount = 0;
        Cursor cursor = null;

        String selectQuery = "Select ifnull(spent_amount, 0)  as amount from " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + "\n"+
                "INNER JOIN " + TABLE_EXPENSE_SUBHEAD + " ON " + TABLE_EXPENSE_SUBHEAD + ".id = " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id\n" +
                "INNER JOIN " + TABLE_EXPENSE_HEAD + " ON " + TABLE_EXPENSE_HEAD + ".id = " + TABLE_EXPENSE_SUBHEAD + ".head_id\n" +
                "where " + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".subhead_id = @SubHead_id "+
                "and strftime('%m'," + TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".for_date) = strftime('%m',date('now')) " +
                "and "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".school_id = @SchoolID "+
                "and ("+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = '@AllowedFrom' or "+TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY + ".allowedFrom = 'Both')";

        selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");
        selectQuery = selectQuery.replaceAll("@AllowedFrom", allowedFrom + "");
        selectQuery = selectQuery.replaceAll("@SubHead_id", subhead_id + "");


        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                limit_amount = cursor.getInt(cursor.getColumnIndex("amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return limit_amount;
    }

    public List<Integer> getSpentandAvailable_amount(int schoolId,int head_id) {
        List<Integer> amountList = new ArrayList<>();
        int spentAmount = -1, totalExpense = -1;
        Cursor cursor = null;
        try {
            String selectQuery = "select SUM(limit_amount) as limitAmount from ExpenseSubHeadLimitsMonthly eshlm " +
                    "inner join ExpenseSubHead esh on esh.id = eshlm.subhead_id\n"+
                    "inner join ExpenseHead eh on eh.id = esh.head_id\n"+
                    "where eh.id = "+head_id+" and strftime('%m',for_date) = strftime('%m',date('now')) and eshlm.school_id = " + schoolId ;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                totalExpense = cursor.getInt(cursor.getColumnIndex("limitAmount"));
                spentAmount = getSpentAmountofHeadfromTransaction(schoolId, head_id);
                amountList.add(totalExpense);
                amountList.add(spentAmount);
                return amountList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return amountList;
    }


    public long insertAmountClosing(ExpenseAmountClosingModel eac) {
        long i = -1;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SUBHEAD_LIMITS_MONTHLY_FOR_DATE, eac.getForDate());
            cv.put(AMOUNT_CLOSING_CLOSEAMOUNT, eac.getClosingAmount());
            cv.put(AMOUNT_CLOSING_SQL_SERVER_USER, eac.getSqlServerUser());
            cv.put(AMOUNT_CLOSING_CREATED_ON_APP, eac.getCreatedOnApp());
            //cv.put(AMOUNT_CLOSING_CREATED_ON_SERVER, eac.getCreatedOnServer());
            cv.put(HEAD_CREATED_BY, eac.getCreatedBy());
            cv.put(SUBHEAD_LIMITS_SUBHEAD_ID, eac.getSubhead_id());
            cv.put(SUBHEAD_LIMITS_SCHOOL_ID, eac.getSchoolID());


            i = DB.insert(TABLE_EXPENSE_AMOUNT_CLOSING, null, cv);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert AmountClosing record with ID: " + i);
            else
                AppModel.getInstance().appendLog(context, "AmountClosing record inserted with ID: " + i);

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In insertAmountClosing Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
    }

    public double getSpentAmountofsubheadfromTransaction(int schoolId, int subhead_id){
        int spentamount = 0;
        String condition;
        if(subhead_id == 1 || subhead_id == 2 || subhead_id == 46 || subhead_id == 47)
            condition = "InAmount.amount - OutAmount.amount";
        else
            condition = "OutAmount.amount - InAmount.amount";
        Cursor cursor = null;
        if (subhead_id > 0) {
            try {
//                String selectQuery = "select SUM(trans_amount) AS subhead_spent_amount from ExpenseTransactions where is_active = 1 and school_id = " + schoolId + " and subhead_id = "+subhead_id;
                String selectQuery = "SELECT " + condition  +" as subhead_spent_amount\n" +
                        "FROM\n" +
                        "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions\n" +
                        "where flow_id = 1 AND subhead_id = @Subhead_Id \n" +//flowid 1 for In
                        "and strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                        "and is_active = 1\n" +
                        "and school_id = @SchoolID) InAmount\n" +
                        "LEFT JOIN\n" +
                        "(Select ifnull(Sum(trans_amount), 0) as amount from ExpenseTransactions\n" +
                        "where flow_id = 2 AND subhead_id = @Subhead_Id \n" +//flowid 2 for Out
                        "and strftime('%m',for_date) = strftime('%m',date('now'))\n" +
                        "and is_active = 1\n" +
                        "and school_id = @SchoolID) OutAmount";



                selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");
                selectQuery = selectQuery.replaceAll("@Subhead_Id", subhead_id + "");

                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    spentamount = cursor.getInt(cursor.getColumnIndex("subhead_spent_amount"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return spentamount;
    }

    public int getSpentAmountofHeadfromTransaction(int schoolId, int head_id){
        int spentamount = 0;
        String condition;
        if(head_id != 1)
            condition = "OutAmount.amount - InAmount.amount";
        else
            condition = "InAmount.amount - OutAmount.amount";
        Cursor cursor = null;
        if (head_id > 0) {
            try {
//                String selectQuery = "select SUM(trans_amount) AS subhead_spent_amount from ExpenseTransactions where is_active = 1 and school_id = " + schoolId + " and subhead_id = "+subhead_id;
                String selectQuery = "SELECT " + condition  +" as subhead_spent_amount FROM \n" +
                        "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions et \n" +
                        "inner join  ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id \n" +
                        "where et.flow_id = 1 and strftime('%m',et.for_date) = strftime('%m',date('now')) and et.is_active = 1 and esh.head_id = @Head_Id and et.school_id = @SchoolID) InAmount\n" +
                        "LEFT JOIN\n" +
                        "(Select ifnull(Sum(trans_amount), 0) as amount from ExpenseTransactions et \n" +
                        "inner join  ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id\n" +
                        "where et.flow_id = 2 and strftime('%m',et.for_date) = strftime('%m',date('now')) and et.is_active = 1 and esh.head_id = @Head_Id and et.school_id = @SchoolID) OutAmount";

                selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");
                selectQuery = selectQuery.replaceAll("@Head_Id", head_id + "");

                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    spentamount = cursor.getInt(cursor.getColumnIndex("subhead_spent_amount"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return spentamount;
    }

    public int getSpentAmountofHeadfromTransactionforClosing(int schoolId, int head_id,String closingDate){
        int spentamount = 0;
        String condition;
        if(head_id != 1)
            condition = "OutAmount.amount - InAmount.amount";
        else
            condition = "InAmount.amount - OutAmount.amount";
        Cursor cursor = null;
        if (head_id > 0) {
            try {
//                String selectQuery = "select SUM(trans_amount) AS subhead_spent_amount from ExpenseTransactions where is_active = 1 and school_id = " + schoolId + " and subhead_id = "+subhead_id;
                String selectQuery = "SELECT " + condition  +" as subhead_spent_amount FROM \n" +
                        "(Select ifnull(Sum(trans_amount), 0)  as amount from ExpenseTransactions et \n" +
                        "inner join  ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id \n" +
                        "where et.flow_id = 1 and et.for_date > '" + closingDate +"' and et.is_active = 1 and esh.head_id = @Head_Id and et.school_id = @SchoolID) InAmount\n" +
                        "LEFT JOIN\n" +
                        "(Select ifnull(Sum(trans_amount), 0) as amount from ExpenseTransactions et \n" +
                        "inner join  ExpenseSubHead esh on esh.id = et.subhead_id inner join ExpenseHead eh on eh.id = esh.head_id\n" +
                        "where et.flow_id = 2 and et.for_date > '" + closingDate +"' and et.is_active = 1 and esh.head_id = @Head_Id and et.school_id = @SchoolID) OutAmount";

                selectQuery = selectQuery.replaceAll("@SchoolID", schoolId + "");
                selectQuery = selectQuery.replaceAll("@Head_Id", head_id + "");

                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    spentamount = cursor.getInt(cursor.getColumnIndex("subhead_spent_amount"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return spentamount;
    }
}
