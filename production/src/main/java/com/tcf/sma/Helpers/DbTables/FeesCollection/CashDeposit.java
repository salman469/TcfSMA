package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.Fees_Collection.CashDepositModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandReportModel;
import com.tcf.sma.Models.Fees_Collection.DepositHistoryModel;
import com.tcf.sma.Models.RetrofitModels.SyncCashDepositModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammad.haseeb on 11/10/2018.
 */

public class CashDeposit {

    public static final String CASH_DEPOSIT_TABLE = "CashDeposit";
    public static final String ID = "id";
    public static final String SYS_ID = "sys_id";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_ON = "created_on";
    public static final String MODIFIED_ON = "modified_on";
    public static final String UPLOADED_ON = "uploaded_on";
    public static final String SCHOOL_ID = "school_id";
    public static final String DEPOSIT_SLIP_NO = "deposit_slip_no";
    public static final String DEPOSIT_AMOUNT = "deposit_amount";
    public static final String PICTURE_SLIP_FILENAME = "picture_slip_filename";
    public static final String DEVICE_ID = "device_id";
    public static final String DOWNLOADED_ON = "downloaded_on";
    public static final String IMAGE_UPLOADED_ON = "image_uploaded_on";
    public static final String REMARKS = "remarks";
    public static final String CREATE_CASH_DEPOSIT_TABLE = "CREATE TABLE " + CASH_DEPOSIT_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + SYS_ID + "  INTEGER ,"
            + CREATED_BY + "  TEXT ,"
            + CREATED_ON + "  TEXT ,"
            + MODIFIED_ON + "  TEXT ,"
            + UPLOADED_ON + "  TEXT ,"
            + SCHOOL_ID + "  INTEGER ,"
            + DEPOSIT_SLIP_NO + "  TEXT ,"
            + DEPOSIT_AMOUNT + "  REAL ,"
            + PICTURE_SLIP_FILENAME + "  TEXT ,"
            + DEVICE_ID + "  TEXT ,"
            + DOWNLOADED_ON + "  TEXT,"
            + REMARKS + "  TEXT," +
            IMAGE_UPLOADED_ON + " Text);";
    public static final String GET_ALL_CASH_DEPOSITS = "Select * from " + CASH_DEPOSIT_TABLE;
    public static final String GET_ALL_CASH_DEPOSITS_FOR_ACCOUNT_STATEMENT = "select * from " + CASH_DEPOSIT_TABLE + " cd where school_id = @SchoolID";
    private static final String DEPOSIT_HISTORY_QUERY = "SELECT cd.school_id,strftime('%Y-%m-%d',replace(replace(cd.created_on,'AM',''),'PM','')) as deposit_date,cd.created_by,SUM(deposit_amount) as total_amount,cd.* \n" +
            "    FROM CashDeposit cd where school_id IN (@schoolId) AND  cd.created_on >= '@fromDate 00:00:00 AM' and cd.created_on <= '@toDate 23:59:59 PM' GROUP BY deposit_slip_no ORDER BY cd.created_on DESC";
    private static final String DEPOSIT_HISTORY_QUERY2 = "SELECT cd.school_id,strftime('%Y-%m-%d',replace(replace(cd.created_on,'AM',''),'PM','')) as deposit_date,cd.created_by,SUM(deposit_amount) as total_amount,cd.* \n" +
            "    FROM CashDeposit cd where school_id IN (@schoolId) AND cd.deposit_slip_no = '@DepSlipNo' GROUP BY deposit_slip_no ORDER BY cd.created_on DESC";
    private static CashDeposit instance = null;
    private Context context;

    private CashDeposit(Context context) {
        this.context = context;
    }

    public static CashDeposit getInstance(Context context) {
        if (instance == null)
            instance = new CashDeposit(context);

        return instance;
    }

    public boolean ifDepositSlipExist(String depositSlipNo) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        String query = "select * from " + CASH_DEPOSIT_TABLE + " where " + DEPOSIT_SLIP_NO + " = '@DepositSlipNo'";
        query = query.replace("@DepositSlipNo", depositSlipNo);
        Cursor cursor = null;
        cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public long genericUpdateMethod(ContentValues values, String deviceId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = 0;
        try {
            id = db.update(CashDeposit.CASH_DEPOSIT_TABLE, values, CashDeposit.ID + " = " + deviceId, null);
            if (id > 0) {
                AppModel.getInstance().appendErrorLog(context, "In genericUpdateMethod Cash Deposit Successfully Updated in local db after upload where device id=" + deviceId);
            } else {
                AppModel.getInstance().appendErrorLog(context, "In genericUpdateMethod Cash Deposit Not Updated in local db after upload where device id=" + deviceId);
            }
            Log.d("cashDeposit", "" + id);
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In genericUpdateMethod Cash Deposit Not Updated in local db after upload due to:" + e.getMessage());
        }
        return id;
    }


    public int genericUpdateMethodForImage(ContentValues values, String filename) {
        int id = -1;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            id = db.update(CashDeposit.CASH_DEPOSIT_TABLE, values, CashDeposit.PICTURE_SLIP_FILENAME + " = '" + filename + "'", null);
            Log.d("cashDeposit", "" + id);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;

    }

    public List<SyncCashDepositModel> getAllCashDepositsForUpload() {
        List<SyncCashDepositModel> cdmList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query = "Select * from " + CASH_DEPOSIT_TABLE + " where " + SYS_ID + " is null and " + IMAGE_UPLOADED_ON + " is not null";

        try {

            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    SyncCashDepositModel model = new SyncCashDepositModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(CashDeposit.ID)));
                    model.setSchool_id(cursor.getInt(cursor.getColumnIndex(CashDeposit.SCHOOL_ID)));
                    model.setCreated_by(cursor.getInt(cursor.getColumnIndex(CashDeposit.CREATED_BY)));
                    model.setCreated_on(cursor.getString(cursor.getColumnIndex(CashDeposit.CREATED_ON)));
                    model.setUploaded_on(cursor.getString(cursor.getColumnIndex(CashDeposit.UPLOADED_ON)));
                    model.setDeposit_slip_no(cursor.getString(cursor.getColumnIndex(CashDeposit.DEPOSIT_SLIP_NO)));
                    model.setDeposit_slip_amount(cursor.getDouble(cursor.getColumnIndex(CashDeposit.DEPOSIT_AMOUNT)));
                    model.setPicture_slip_filename(cursor.getString(cursor.getColumnIndex(CashDeposit.PICTURE_SLIP_FILENAME)));
                    model.setDevice_id(cursor.getString(cursor.getColumnIndex(CashDeposit.DEVICE_ID)));
                    model.setRemarks(cursor.getString(cursor.getColumnIndex(CashDeposit.REMARKS)));
//                    String ids = getReceiptIdForUpload(model.getId());
                    String ids = getFeesHeaderIdForUpload(model.getId());
                    if (ids == null || ids.isEmpty()) {
                        String whereClause = CashDeposit.ID + "=?";
                        String[] whereArgs = {String.valueOf(model.getId())};

                        boolean deleted = DatabaseHelper.getInstance(context).deleteRecordsFromTable(
                                CashDeposit.CASH_DEPOSIT_TABLE, whereClause, whereArgs) > 0;

                        if(deleted) {
                            AppModel.getInstance().appendLog(context, "Cash Deposit having id=" + model.getId()
                                    + " Deleted because its tagged receipts already deposited");
                        }
                        continue;
                    }
                    else
                        model.setReceipt_ids(getFeesHeaderIdForUpload(model.getId()));
//                    else
//                        model.setReceipt_ids(getReceiptIdForUpload(model.getId()));

                    File file = new File(model.getPicture_slip_filename());
                    model.setPicture_slip_filename(file.getName());

                    try {
                        int sysid = cursor.getInt(cursor.getColumnIndex(SYS_ID));

                        AppModel.getInstance().appendErrorLog(context, "In getAllCashDepositsForUpload() method: "
                                + "Cash Deposit id =" + model.getId() + ",SysID =" + sysid +
                                ",deposit slip no = " + model.getDeposit_slip_no() + ", school id =" + model.getSchool_id() + " is added for upload");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    cdmList.add(model);
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in getAllCashDepositsForUpload() method:" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cdmList;
    }


    private String getReceiptIdForUpload(int cashDepositId) {

        String receiptIds = "";
        Cursor cursor = null;
        SQLiteDatabase db = null;
//        String query = "Select group_concat(sys_id,',') as ids from AppReceipt ar \n" +
//                "where CashDeposit_id = @Cid and downloaded_on is null" ;

        String query = "Select group_concat(sys_id,',') as ids from AppReceipt ar \n" +
                "where CashDeposit_id = @Cid and downloaded_on is null";

        query = query.replace("@Cid", cashDepositId + "");

        try {

            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                receiptIds = cursor.getString(cursor.getColumnIndex("ids"));

                String[] vals = receiptIds.split(",");
                if (vals.length > 0) {
                    for (int i = 0; i < vals.length; i++) {
                        if (vals[i].equals("0"))
                            return "";
                    }
                }
                return receiptIds;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return receiptIds;
    }

    public String getLastModifiedOn(int schoolId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(modified_on) as modified_on from " + CASH_DEPOSIT_TABLE +
                " WHERE school_id = " + schoolId;
        String modified_on = "";

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                modified_on = cursor.getString(cursor.getColumnIndex("modified_on"));
                if (modified_on != null && modified_on != "")
                    return modified_on;
                else
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private String getFeesHeaderIdForUpload(int cashDepositId) {

        String receiptIds = "";
        Cursor cursor = null;
        SQLiteDatabase db = null;
//        String query = "Select group_concat(sys_id,',') as ids from AppReceipt ar \n" +
//                "where CashDeposit_id = @Cid and downloaded_on is null" ;

        String query = "Select group_concat(sys_id,',') as ids from FeesHeader fh \n" +
                "where CashDeposit_id = @Cid ";
//        and downloaded_on is null";

        query = query.replace("@Cid", cashDepositId + "");

        try {

            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                receiptIds = cursor.getString(cursor.getColumnIndex("ids"));

                if (receiptIds != null && !receiptIds.isEmpty()) {
                    String[] vals = receiptIds.split(",");
                    if (vals.length > 0) {
                        for (int i = 0; i < vals.length; i++) {
                            if (vals[i].equals("0"))
                                return "";
                        }
                    }
                }
                return receiptIds;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return receiptIds;
    }

    public ArrayList<EnrollmentImageModel> getAllCashDepositImages() {
        ArrayList<EnrollmentImageModel> eimList = new ArrayList<>();
        Cursor cursor = null;
        try {


            String query = "Select * from " + CASH_DEPOSIT_TABLE + " where " + IMAGE_UPLOADED_ON + " is null and " +
                    DOWNLOADED_ON + " is null limit 5";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentImageModel eim = new EnrollmentImageModel();
                    eim.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    eim.setFilename(cursor.getString(cursor.getColumnIndex(PICTURE_SLIP_FILENAME)));
                    eim.setUploaded_on(cursor.getString(cursor.getColumnIndex(IMAGE_UPLOADED_ON)));
//                    eim.setReview_status(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_REVIEW_STATUS)));
//                    eim.setFiletype(cursor.getString(cursor.getColumnIndex(ENROLLMENT_FILE_TYPE)));
                    eimList.add(eim);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return eimList;
    }


    public List<CashDepositModel> getAllCashDepositsForAccountStatement(int schoolId, String grNo, String fromDate, String toDate) {
        List<CashDepositModel> cdmList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query = CashDeposit.GET_ALL_CASH_DEPOSITS_FOR_ACCOUNT_STATEMENT;
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

                    CashDepositModel model = new CashDepositModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(CashDeposit.ID)));
                    model.setSysId(cursor.getInt(cursor.getColumnIndex(CashDeposit.SYS_ID)));
                    model.setCreatedBy(cursor.getString(cursor.getColumnIndex(CashDeposit.CREATED_BY)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CashDeposit.CREATED_ON)));
                    model.setUploadedOn(cursor.getString(cursor.getColumnIndex(CashDeposit.UPLOADED_ON)));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex(CashDeposit.SCHOOL_ID)));
                    model.setDepositSlipNo(cursor.getString(cursor.getColumnIndex(CashDeposit.DEPOSIT_SLIP_NO)));
                    model.setDepositAmount(cursor.getDouble(cursor.getColumnIndex(CashDeposit.DEPOSIT_AMOUNT)));
                    model.setDepositSlipFilePath(cursor.getString(cursor.getColumnIndex(CashDeposit.PICTURE_SLIP_FILENAME)));
                    model.setDeviceId(cursor.getString(cursor.getColumnIndex(CashDeposit.DEVICE_ID)));
                    model.setDownloadedOn(cursor.getString(cursor.getColumnIndex(CashDeposit.DOWNLOADED_ON)));

                    cdmList.add(model);
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
        return cdmList;
    }

    public long insertDownloadedCashDeposit(CashDepositModel model) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(CashDeposit.CREATED_BY, model.getCreatedBy());
            values.put(CashDeposit.CREATED_ON, model.getCreatedOn());
            values.put(CashDeposit.MODIFIED_ON, model.getModified_on());
            values.put(CashDeposit.SCHOOL_ID, model.getSchoolId());
            values.put(CashDeposit.SYS_ID, model.getSysId());
            values.put(CashDeposit.UPLOADED_ON, model.getUploadedOn());
            values.put(CashDeposit.DOWNLOADED_ON, model.getDownloadedOn());
            values.put(CashDeposit.DEPOSIT_SLIP_NO, model.getDepositSlipNo());
            values.put(CashDeposit.DEPOSIT_AMOUNT, model.getDepositAmount());
            values.put(CashDeposit.PICTURE_SLIP_FILENAME, model.getDepositSlipFilePath());
            values.put(CashDeposit.DEVICE_ID, model.getDeviceId());

            long id = db.insert(CashDeposit.CASH_DEPOSIT_TABLE, null, values);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }


    }

    public long updateDownloadedCashDeposit(CashDepositModel model) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(CashDeposit.CREATED_BY, model.getCreatedBy());
            values.put(CashDeposit.CREATED_ON, model.getCreatedOn());
            values.put(CashDeposit.MODIFIED_ON, model.getModified_on());
            values.put(CashDeposit.SCHOOL_ID, model.getSchoolId());
            values.put(CashDeposit.SYS_ID, model.getSysId());
            values.put(CashDeposit.UPLOADED_ON, model.getUploadedOn());
            values.put(CashDeposit.DOWNLOADED_ON, model.getDownloadedOn());
            values.put(CashDeposit.DEPOSIT_SLIP_NO, model.getDepositSlipNo());
            values.put(CashDeposit.DEPOSIT_AMOUNT, model.getDepositAmount());
            values.put(CashDeposit.PICTURE_SLIP_FILENAME, model.getDepositSlipFilePath());
            values.put(CashDeposit.DEVICE_ID, model.getDeviceId());

            long id = db.update(CashDeposit.CASH_DEPOSIT_TABLE,values, CashDeposit.SYS_ID + "=" + model.getSysId() + " AND " + CashDeposit.SCHOOL_ID + "=" + model.getSchoolId(), null);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }


    }

    public List<DepositHistoryModel> getDepositHistory(String schoolId, String fromDate, String toDate, String DepositSlipNo) {
        List<DepositHistoryModel> dhm = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "";

        if (!DepositSlipNo.equals("") || !DepositSlipNo.isEmpty()) {
            query = DEPOSIT_HISTORY_QUERY2;

            query = query.replace("@DepSlipNo", DepositSlipNo);
        } else {
            query = DEPOSIT_HISTORY_QUERY;

            query = query.replace("@fromDate", fromDate);
            query = query.replace("@toDate", toDate);
        }

        query = query.replace("@schoolId", schoolId);


        Log.d("Deposit History Query", query);
        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    DepositHistoryModel model = new DepositHistoryModel();
                    model.setDeposit_id(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setDepost_date(cursor.getString(cursor.getColumnIndex("deposit_date")));
                    model.setDepost_slipNo(cursor.getString(cursor.getColumnIndex(DEPOSIT_SLIP_NO)));
                    model.setDeposited_by(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
                    model.setTotal(cursor.getDouble(cursor.getColumnIndex("total_amount")));
                    model.setSchoolid(cursor.getInt(cursor.getColumnIndex("school_id")));

                    dhm.add(model);
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
        return dhm;
    }

    public int getMaxSysId(int schoolId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(sys_id) as sys_id from " + CASH_DEPOSIT_TABLE +
                " WHERE school_id = " + schoolId;
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


    public void deleteSysIDRecords(int school_id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM CashDeposit WHERE (sys_id != '' OR sys_id NOTNULL) AND school_id IN(" + school_id + ")";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ID));
                    db.delete(CASH_DEPOSIT_TABLE, ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "Cash Deposit Data Flushed Successfully. School ID:" + school_id);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing Cash Deposit data !\nError: " + e.getMessage() +
                    " School ID:" + school_id);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
    }

    public CashInHandReportModel getCashInHandReport(String fromDate, String toDate) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String schoolIds = AppModel.getInstance().getuserSchoolIDS(context);
        String opCashInHandQuery = "SELECT R.totalAmnt - D.totalDeposit as openingCashInHand\n" +
                " FROM  (SELECT IFNULL(SUM(fd.fee_amount),0) AS totalAmnt  FROM FeesHeader fh  \n" +
                " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id  \n" +
                " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id  WHERE fh.Category_Id = 2 \n" +
                " AND sc.school_id IN (@schoolId) \n" +
                " AND fh.for_date in (SELECT fh1.for_date FROM FeesHeader fh1\n" +
                " INNER JOIN school_class sc1 ON sc1.id = fh1.schoolclass_id  \n" +
                " INNER JOIN FeesDetail fd1 ON fh1.id = fd1.feesHeader_id  WHERE fh1.Category_Id = 2 \n" +
                " AND sc1.school_id IN (@schoolId) AND strftime('%Y-%m-%d',replace(replace(upper(fh1.for_date),'AM',''),'PM','')) < strftime('%Y-%m-%d','@FromDate')\n" +
                " )\n" +
                " ) R  \n" +
                " LEFT JOIN  (SELECT IFNULL( SUM(cd.deposit_amount),0) AS totalDeposit FROM CashDeposit cd  \n" +
                " WHERE cd.school_id IN (@schoolId) AND cd.created_on in (SELECT cd1.created_on FROM CashDeposit cd1  \n" +
                " WHERE cd1.school_id IN (@schoolId) AND strftime('%Y-%m-%d',replace(replace(upper(cd1.created_on),'AM',''),'PM','')) < strftime('%Y-%m-%d','@FromDate')) ) D\n";

        /*String cashReceivedQuery = "SELECT IFNULL(SUM(fd.fee_amount),0) AS cashReceived FROM FeesHeader fh  \n" +
                " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id  \n" +
                " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id  WHERE fh.Category_Id = 2 \n" +
                " AND sc.school_id IN (@schoolId) \n" +
                " AND strftime('%Y-%m-%d',replace(replace(upper(fh.for_date),'AM',''),'PM','')) BETWEEN strftime('%Y-%m-%d','@FromDate') AND\n" +
                " strftime('%Y-%m-%d','@ToDate')";*/

        String cashDepositQuery = "SELECT IFNULL( SUM(cd.deposit_amount),0) AS cashDeposit FROM CashDeposit cd  \n" +
                " WHERE cd.school_id IN (@schoolId) AND strftime('%Y-%m-%d',replace(replace(upper(cd.created_on),'AM',''),'PM','')) BETWEEN strftime('%Y-%m-%d','@FromDate') AND\n" +
                " strftime('%Y-%m-%d','@ToDate')";

        CashInHandReportModel model = new CashInHandReportModel();
        try {
            opCashInHandQuery = opCashInHandQuery.replace("@schoolId", schoolIds);
            opCashInHandQuery = opCashInHandQuery.replace("@FromDate", fromDate);
            opCashInHandQuery = opCashInHandQuery.replace("@ToDate", toDate);
            cursor = db.rawQuery(opCashInHandQuery, null);
            if (cursor.moveToFirst()) {
                model.setOpeningCashInHand(cursor.getDouble(cursor.getColumnIndex("openingCashInHand")));
            }

            /*cashReceivedQuery = cashReceivedQuery.replace("@schoolId", schoolIds);
            cashReceivedQuery = cashReceivedQuery.replace("@FromDate", fromDate);
            cashReceivedQuery = cashReceivedQuery.replace("@ToDate", toDate);
            cursor = db.rawQuery(cashReceivedQuery, null);
            if (cursor.moveToFirst()) {
                model.setCashReceived(cursor.getDouble(cursor.getColumnIndex("cashReceived")));
            }*/

            cashDepositQuery = cashDepositQuery.replace("@schoolId", schoolIds);
            cashDepositQuery = cashDepositQuery.replace("@FromDate", fromDate);
            cashDepositQuery = cashDepositQuery.replace("@ToDate", toDate);
            cursor = db.rawQuery(cashDepositQuery, null);
            if (cursor.moveToFirst()) {
                model.setCashDeposit(cursor.getDouble(cursor.getColumnIndex("cashDeposit")));
            }

            /*double closingCashInHand = model.getOpeningCashInHand() + model.getCashReceived() - model.getCashDeposit();
            model.setClosingCashInHand(closingCashInHand);*/

            double closingCashInHand = FeesCollection.getInstance(context).getCashInHand(0,0);
            model.setClosingCashInHand(closingCashInHand);
            double cashReceived = model.getCashDeposit() + model.getClosingCashInHand() - model.getOpeningCashInHand();
            model.setCashReceived(cashReceived);



            return model;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
