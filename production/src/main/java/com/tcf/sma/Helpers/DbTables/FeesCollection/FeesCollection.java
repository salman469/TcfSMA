package com.tcf.sma.Helpers.DbTables.FeesCollection;

import static com.tcf.sma.Helpers.DatabaseHelper.SCHOOL_ACADEMIC_SESSION_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.Strings;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CheckSumModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.FeesDetailUploadModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModelNewStructure;
import com.tcf.sma.Models.Fees_Collection.AccountStatementSchoolModel;
import com.tcf.sma.Models.Fees_Collection.CashDepositModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandFeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandModel;
import com.tcf.sma.Models.Fees_Collection.DashboardReceivableModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.Fees_Collection.GeneralUploadResponseModel;
import com.tcf.sma.Models.Fees_Collection.PreviousReceivableModel;
import com.tcf.sma.Models.Fees_Collection.ReceiptListModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesCorrectionModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.SyncFeesHeaderModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.Models.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FeesCollection {

    public static final String GET_PREV_RECEIVABLES = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2  THEN -1 *  fd.fee_amount\n" +
            "\tELSE CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount\t\n" +
            "\tELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END \n" +
            "\tEND END \n" +
            ") AS totalAmount,fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SchoolId\n" +
            "inner join student s on s.id = fh.student_id and s.student_gr_no = @StudentGRNO\n" +
            "GROUP BY fd.feeType_id";
    public static final String GET_DEPOSIT_RECORD = "SELECT SUM(fd.fee_amount) AS totalAmount,fd.feeType_id\n" +
            "        FROM CashDeposit cd\n" +
            "        INNER JOIN FeesHeader fh on (fh.CashDeposit_id_Type = 'S' and fh.CashDeposit_id = cd.sys_id) || (fh.CashDeposit_id_Type = 'L' and fh.CashDeposit_id = cd.id) \n" +
            "        INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "        INNER JOIN school_class sc ON sc.id = fh.schoolclass_id\n" +
            "        WHERE cd.deposit_slip_no = '@slipNo' AND sc.school_id IN(@schoolId)        \n" +
            "        AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 2 \n" +
            "        GROUP BY fd.feeType_id";
    public static final String GET_PREV_RECEIVABLES_CORRECTION = "SELECT SUM(CASE WHEN fh.Category_Id = 2 THEN fd.fee_amount \n" +
            "\tELSE CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount\n" +
            "\tELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END \n" +
            "\tEND END" +
            ") AS totalAmount,fd.feeType_id, fh.*\n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SCHOOL_ID" +
            " INNER JOIN student s on s.id = fh.student_id and  fh.id = (SELECT fh1.id FROM FeesHeader fh1" +
            " INNER JOIN FeesDetail fd1 ON fh1.id = fd1.feesHeader_id"+
            " where fh1.receipt_no = @RECEIPT_NO and fh1.category_id = 2" +
            " and fh1.schoolclass_id in (SELECT id FROM school_class WHERE school_id = @SCHOOL_ID)" +
            " ORDER BY fh1.id DESC LIMIT 1) " +
            " GROUP BY fd.feeType_id ORDER BY fh.created_on asc";

//    public static final String GET_PREV_RECEIVABLES_CORRECTION = "SELECT SUM(fd.fee_amount) AS totalAmount," +
//        " fd.feeType_id,\n" +
//        " fh.category_id\n" +
//        " FROM FeesHeader fh \n" +
//        " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
//        " INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SCHOOL_ID \n" +
//        " INNER JOIN student s on s.id = fh.student_id\n" +
//        " where  fh.receipt_no = @RECEIPT_NO\n" +
//        " group by fh.category_id";

    public static final String GET_CASH_IN_HAND_DATA3 = "SELECT R.totalAmount - D.totalDeposit as cashInHand\n" +
            " FROM " +
            " (SELECT IFNULL(SUM(fd.fee_amount),0) AS totalAmount " +
            " FROM FeesHeader fh " +
            " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id " +
            " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id " +
            " WHERE fh.Category_Id = 2 AND sc.school_id IN (@school_ids) ) R " +
            " LEFT JOIN " +
            " (SELECT IFNULL( SUM(cd.deposit_amount),0) AS totalDeposit FROM CashDeposit cd " +
            " WHERE cd.school_id IN (@school_ids)) D";
    public static final String GET_CASH_IN_HAND_DATA = "SELECT SUM(fd.fee_amount) AS totalAmount, fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN school_class sc ON sc.id = fh.schoolclass_id\n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "WHERE sc.school_id IN (@school_ids) and fh.Category_Id = 2 and fh.CashDeposit_id IS NULL\n" +
            "AND fd.feeType_id IN(1,2,3,4,5,6,7)\n" +
            "GROUP BY fd.feeType_id";
    public static final String NEW_GET_CASH_IN_HAND_DATA = "SELECT fd.feeType_id , fd.fee_amount, fh.created_on \n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN school_class sc ON sc.id = fh.schoolclass_id\n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "WHERE sc.school_id IN (@school_ids) and fh.Category_Id = 2 " +
//            "and fh.CashDeposit_id IS NULL\n" +
            "and fh.created_on > '@MaxDate'\n" +
            "AND fd.feeType_id IN(1,2,3,4,5,6,7)";
    //            "GROUP BY fd.feeType_id";
    public static final String GET_CASH_IN_HAND_DATA_FOR_ALL_SCHOOL = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 1 THEN -1 * fd.fee_amount\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 2 THEN fd.fee_amount\t\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t\n" +
            "\tELSE 0 END END END\n" +
            ") AS totalAmount, fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "WHERE fh.created_by = @UserId and fh.CashDeposit_id IS NULL\n" +
            "GROUP BY fd.feeType_id";
   /* public static final String GET_PREV_RECEIVABLES = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 1 THEN -1 *  fd.fee_amount\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 2 THEN fd.fee_amount\t\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t\n" +
            "\tELSE CASE WHEN fh.Category_Id = 1 AND fh.TransactionType_Id = 1 THEN fd.fee_amount\n" +
            "\tELSE CASE WHEN fh.Category_Id = 1 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t\n" +
            "\tELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END \n" +
            "\tEND END END END END\n" +
            ") AS totalAmount,fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SchoolId\n" +
            "inner join student s on s.id = fh.student_id and s.student_gr_no = @StudentGRNO\n" +
            "GROUP BY fd.feeType_id";*/
    //Table fee type
    public static final String type_id = "id";
    public static final String fee_type_name = "feeType_name";
    public static final String TABLE_FEE_TYPE = "FeeType";
    public static final String CREATE_TABLE_FEE_TYPE = "CREATE TABLE " + TABLE_FEE_TYPE + " ("
            + type_id + " INTEGER,"
            + fee_type_name + " VARCHAR"
            + " )";
    //Table transaction type
    public static final String transaction_type_id = "id";
    public static final String transaction_type_name = "type_name";
    public static final String TABLE_TRANSACTION_TYPE = "TransactionType";
    public static final String CREATE_TABLE_TRANSACTION_TYPE = "CREATE TABLE " + TABLE_TRANSACTION_TYPE + " ("
            + transaction_type_id + " INTEGER,"
            + transaction_type_name + " VARCHAR"
            + " )";
    //Table transaction category
    public static final String transaction_category_id = "id";

    /*public static final String GET_CASH_IN_HAND_DATA3 = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 1 THEN -1 * fd.fee_amount\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 2 THEN fd.fee_amount\t\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t\n" +
            "\tELSE 0 END END END\n" +
            ") AS totalAmount, fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            " INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SchoolId\n" +
            "WHERE fh.created_by = @UserId and fh.CashDeposit_id IS NULL\n" +
            "GROUP BY fd.feeType_id";*/
    public static final String transaction_category_name = "category_name";

    /*public static final String GET_CASH_IN_HAND_DATA = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 1 THEN -1 * fd.fee_amount\t--Normal Receipt\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 2 THEN fd.fee_amount\t--Correction Receipt\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t--Waiver Receipt\n" +
            "\tELSE 0 END END END\n" +
            ") AS totalAmount,  (select created_on \n" +
            "            from CashDeposit \n" +
            "            order by created_on desc limit 1) as lastDepositedDate, fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "WHERE fh.created_by = @UserId and fh.CashDeposit_id IS NULL\n" +
            "GROUP BY fd.feeType_id";*/
    public static final String TABLE_TRANSACTION_CATEGORY = "TransactionCategory";
    public static final String CREATE_TABLE_TRANSACTION_CATEGORY = "CREATE TABLE " + TABLE_TRANSACTION_CATEGORY + " ("
            + transaction_category_id + " INTEGER,"
            + transaction_category_name + " VARCHAR"
            + " )";
    //Table fees Header type
    public static final String ID = "id";
    public static final String SYS_ID = "sys_id";
    public static final String RECEIPT_NO = "receipt_no";
    public static final String RECEIPT_FLAG = "receipt_flag";
    public static final String SCHOOL_CLASS_ID = "schoolclass_id";
    public static final String SCHOOL_YEAR_ID = "school_year_id";
    public static final String ACADEMIC_SESSION_ID = "academic_session_id";
    public static final String STUDENT_ID = "student_id";
    public static final String CREATED_FROM = "created_from";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_ON = "created_on";
    public static final String UPLOADED_ON = "uploaded_on";
    public static final String IS_CORRECTION = "is_correction";
    public static final String RECEIPT_ID = "receipt_id";
    public static final String FEES_ADMISSION = "fees_admission";
    public static final String FEES_EXAM = "fees_exam";
    public static final String FEES_TUTION = "fees_tution";
    public static final String FEES_BOOKS = "fees_books";
    public static final String FEES_COPIES = "fees_copies";
    public static final String FEES_UNIFORMS = "fees_uniform";
    public static final String FEES_OTHERS = "fees_others";
    public static final String DEVICE_ID = "device_id";
    public static final String CASH_DEPOSIT_ID = "CashDeposit_id";
    public static final String CASH_DEPOSIT_ID_TYPE = "CashDeposit_id_Type";
    public static final String DOWNLOADED_ON = "downloaded_on";
    public static final String MODIFIED_ON = "modified_on";
    public static final String REMARKS = "remarks";
    public static final String FOR_DATE = "for_date";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String FEE_TYPE_ID = "feeType_id";
    public static final String CATEGORY_ID = "Category_Id";
    public static final String TRANSACTION_TYPE_ID = "TransactionType_Id";
    public static final String CREATED_ON_SERVER = "createdOn_server";
    public static final String TABLE_FEES_HEADER = "FeesHeader";
    public static final String CREATE_TABLE_FEES_HEADER = "CREATE TABLE " + TABLE_FEES_HEADER + " ("

            + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
            + STUDENT_ID + "  INTEGER,"
            + SCHOOL_CLASS_ID + "  INTEGER,"
            + SCHOOL_YEAR_ID + "  INTEGER,"
            + ACADEMIC_SESSION_ID + "  INTEGER,"
            + SYS_ID + " INTEGER,"
            + DEVICE_ID + "  TEXT,"
            + FOR_DATE + " TEXT,"
            + TOTAL_AMOUNT + "  REAL,"
            + TRANSACTION_TYPE_ID + "  INTEGER,"
            + CATEGORY_ID + "  INTEGER,"
            + RECEIPT_NO + "  TEXT,"
            + CASH_DEPOSIT_ID + "  INTEGER,"
            + CASH_DEPOSIT_ID_TYPE + "  TEXT,"
            + CREATED_ON + "  TEXT,"
            + CREATED_BY + "  TEXT,"
            + UPLOADED_ON + "  TEXT,"
            + DOWNLOADED_ON + "  TEXT,"
            + MODIFIED_ON + "  TEXT,"
            + REMARKS + " TEXT,"
            + IS_CORRECTION + "  INTEGER,"
            + RECEIPT_ID + "  INTEGER,"
            + CREATED_ON_SERVER + "  TEXT"
            + ")";
    //Table fees detail type
    public static final String fees_header_id = "feesHeader_id";
    public static final String fees_type_id = "feeType_id";
    public static final String fee_amount = "fee_amount";
    public static final String fee_transaction_type_id = "transactionType_id";
    public static final String TABLE_FEES_DETAIL = "FeesDetail";
    public static final String CREATE_TABLE_FEES_DETAIL = "CREATE TABLE " + TABLE_FEES_DETAIL + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + fee_amount + " REAL,"
            + fees_type_id + " INTEGER,"
            + fees_header_id + " INTEGER"
            + " )";
    public static final String GET_RECEIVABLES_RECEIVED = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 1 THEN -1 * fd.fee_amount\t--Normal Receipt\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 2 THEN fd.fee_amount\t--Correction Receipt\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t--Waiver Receipt\n" +
            "\tELSE 0 END END END\n" +
            ") AS totalAmount,fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SchoolId\n" +
            "left outer join CashDeposit cd on fh.CashDeposit_id = cd.id\n";
    public static final String GET_RECEIVABLES_RECEIVED_FOR_SCHOOLS = "SELECT SUM(\n" +
            "\tCASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 1 THEN -1 * fd.fee_amount\t--Normal Receipt\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 2 THEN fd.fee_amount\t--Correction Receipt\n" +
            "\tELSE CASE WHEN fh.Category_Id = 2 AND fh.TransactionType_Id = 3 THEN -1 * fd.fee_amount\t--Waiver Receipt\n" +
            "\tELSE 0 END END END\n" +
            ") AS totalAmount,fd.feeType_id\n" +
            "FROM FeesHeader fh \n" +
            "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
            "left outer join CashDeposit cd on fh.CashDeposit_id = cd.id\n";
    private static FeesCollection instance = null;
    private final String STUDENT_Actual_Fees = "Actual_Fees";
    private final String TABLE_STUDENT = "student";
    private Context context;
    public FeesCollection(Context context) {
        this.context = context;
    }

    public static FeesCollection getInstance(Context context) {
        if (instance == null)
            instance = new FeesCollection(context);
        return instance;
    }

    public static void makeSysIdUnique() {
    }

//    public int isReceiptDeposited(int schoolId, int receiptNo) {
//        String query = "Select cd.* FROM FeesHeader fh \n" +
//                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
//                "INNER JOIN CashDeposit cd on cd.id = fh.CashDeposit_id\n" +
//                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @school_id \n" +
//                "INNER JOIN student s on s.id = fh.student_id \n" +
//                "WHERE fh.receipt_no = @receipt_no AND fh.CashDeposit_id IS NOT NULL AND fh.CashDeposit_id != ''\n" +
//                "GROUP BY fh.CashDeposit_id";
//
//        query = query.replace("@school_id", String.valueOf(schoolId));
//        query = query.replace("@receipt_no", String.valueOf(receiptNo));
//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
//        Cursor c = null;
//        try {
//            c = db.rawQuery(query, null);
//
//            if (c.getCount() > 0) {
//                if (c.moveToFirst()) {
//                    return c.getInt(c.getColumnIndex(CashDeposit.DEPOSIT_SLIP_NO));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return 0;
//    }

    public int isReceiptDeposited(int schoolId, long receiptNo) {
        String query = "Select fh.* FROM FeesHeader fh \n" +
                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
//                "INNER JOIN CashDeposit cd on cd.id = fh.CashDeposit_id\n" +
                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @school_id \n" +
                "INNER JOIN student s on s.id = fh.student_id \n" +
                "WHERE fh.receipt_no = @receipt_no AND fh.CashDeposit_id IS NOT NULL AND fh.CashDeposit_id != ''\n" +
                "GROUP BY fh.CashDeposit_id";

        query = query.replace("@school_id", String.valueOf(schoolId));
        query = query.replace("@receipt_no", String.valueOf(receiptNo));
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);

            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndex(CASH_DEPOSIT_ID));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return 0;
    }

    public List<ReceiptListModel> getPreviousReceipts(int schoolId, String fromDate, String toDate, String receiptNo, String deposited, String DepositSlipNo) {
        List<ReceiptListModel> list = new ArrayList<>();

        String query = "SELECT fh.id,fh.receipt_no,fh.student_id,fh.schoolclass_id,sc.school_id,ifnull((SELECT student_gr_no from student WHERE id = fh.student_id),0) as student_gr_no, SUM(fd.fee_amount) as total_amount,fh.created_by,\n" +
                "fh.created_on,\n" +
                "(CASE WHEN fh.CashDeposit_id IS NULL OR fh.CashDeposit_id = 0 then 0 \n" +
                "ELSE CASE WHEN fh.CashDeposit_id  NOTNULL THEN 1 \n" +
                "END END) as isDeposited,(SELECT deposit_slip_no FROM CashDeposit \n" +
                "where (fh.CashDeposit_id_Type = 'S' and sys_id = fh.CashDeposit_id) || (fh.CashDeposit_id_Type = 'L' and id = fh.CashDeposit_id)) as deposit_slip_no\n" +
                "FROM FeesHeader fh \n" +
                "INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id\n" +
                "INNER JOIN school_class sc ON sc.id = fh.schoolclass_id\n" +
//                "INNER JOIN student s ON s.id = fh.student_id\n" +
                "WHERE sc.school_id IN (@schoolId) AND fd.fee_amount != 0\n" +
//                "AND fh.TransactionType_Id = 1 AND fh.Category_Id = 2\n";
                "AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 2\n";


        if (!receiptNo.equals("") || !receiptNo.isEmpty()) {
            query += "AND fh.receipt_no = '@ReceiptNo'\n";
            query = query.replace("@ReceiptNo", receiptNo);
        } else {
            query += "AND (fh.receipt_no IS NOT NULL OR fh.receipt_no != '')\n";
            query += "AND  fh.created_on >= '@fromDate 00:00:00 AM' and fh.created_on <= '@toDate 23:59:59 PM'\n";
//            query += "AND  fh.for_date >= '@fromDate 00:00:00 AM' and fh.for_date <= '@toDate 23:59:59 PM'\n";
            query = query.replace("@fromDate", fromDate);
            query = query.replace("@toDate", toDate);
        }

        if (!DepositSlipNo.equals("") || !DepositSlipNo.isEmpty()) {
            query += " AND deposit_slip_no = '@DepSlipNo'";

            query = query.replace("@DepSlipNo", DepositSlipNo);
        }

        if (deposited.equals("deposited")) {
            query += "AND (fh.CashDeposit_id  NOTNULL)\n";
        } else if (deposited.equals("notdeposited")) {
            query += "AND (fh.CashDeposit_id IS NULL OR fh.CashDeposit_id = 0)\n";
        }

        if (schoolId > 0) {
            query = query.replace("@schoolId", schoolId + "");
        } else {
            query = query.replace("@schoolId", AppModel.getInstance().getuserSchoolIDS(context));
        }


//        query += "group by fh.id HAVING fh.id = MAX(fh.id) ORDER BY fh.created_on DESC";
        query += "group by fh.receipt_no,school_id HAVING fh.id = MAX(fh.id) ORDER BY fh.created_on DESC";

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    ReceiptListModel model = new ReceiptListModel();
                    model.setReceiptId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setReceiptNo(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex("school_id")));
                    model.setStudent_gr_no(cursor.getInt(cursor.getColumnIndex("student_gr_no")));
                    model.setTotalAmount(cursor.getDouble(cursor.getColumnIndex("total_amount")));
                    model.setCreateBy(cursor.getString(cursor.getColumnIndex("created_by")));
                    model.setCreateOn(cursor.getString(cursor.getColumnIndex("created_on")));
                    model.setIsDeposited(cursor.getInt(cursor.getColumnIndex("isDeposited")));
                    model.setDeposit_slip_no(cursor.getString(cursor.getColumnIndex(CashDeposit.DEPOSIT_SLIP_NO)));
                    model.setStudentId(cursor.getInt(cursor.getColumnIndex("student_id")));
                    model.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
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

    public List<PreviousReceivableModel> getPrevReceivableWithReceiptNO(String schoolId, String ReceiptNo) {
        List<PreviousReceivableModel> prevRecv = new ArrayList<>();
        String query = GET_PREV_RECEIVABLES_CORRECTION;
        query = query.replace("@SCHOOL_ID", schoolId);
        query = query.replace("@RECEIPT_NO", ReceiptNo);

        Log.d("Prev Recv Query", query);
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        Cursor c = null;
        boolean added = false;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    if (c.getInt(c.getColumnIndex(fees_type_id)) == 3 || c.getInt(c.getColumnIndex(fees_type_id)) == 8) {
                        for (PreviousReceivableModel p : prevRecv) {
                            if (p.getFeeType_id() == 3) {
                                String amount = c.getString(c.getColumnIndex("totalAmount"));
                                if (amount != null && p.getTotalAmount() != null) {
                                    int total = Integer.parseInt(amount) + Integer.parseInt(p.getTotalAmount());
                                    p.setTotalAmount("" + total);
                                    p.setForDate(c.getString(c.getColumnIndex(FOR_DATE)));
                                    added = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!added) {
                        PreviousReceivableModel prm = new PreviousReceivableModel();
                        prm.setFeeType_id(c.getInt(c.getColumnIndex(fees_type_id)) == 8 ? 3 : c.getInt(c.getColumnIndex(fees_type_id)));
                        prm.setTotalAmount(c.getString(c.getColumnIndex("totalAmount")));
//                        prm.setForDate(c.getString(c.getColumnIndex(FOR_DATE)));
                        prevRecv.add(prm);
                    }
                } while (c.moveToNext());

            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add(new PreviousReceivableModel(1, ""));
                prevRecv.add(new PreviousReceivableModel(2, ""));
                prevRecv.add(new PreviousReceivableModel(3, ""));
                prevRecv.add(new PreviousReceivableModel(4, ""));
                prevRecv.add(new PreviousReceivableModel(5, ""));
                prevRecv.add(new PreviousReceivableModel(6, ""));
                prevRecv.add(new PreviousReceivableModel(7, ""));
            }

            return prevRecv;

        } catch (Exception e) {
            e.printStackTrace();
            return prevRecv;
        } finally {
            if (c != null)
                c.close();
        }
    }

    public List<PreviousReceivableModel> getPrevReceivableWithReceiptNOForReceiptDetails(String schoolId, String ReceiptNo) {
        List<PreviousReceivableModel> prevRecv = new ArrayList<>();
        String query = "SELECT SUM(CASE WHEN fh.Category_Id = 2 THEN fd.fee_amount \n" +
                "\tELSE CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount\n" +
                "\tELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END \n" +
                "\tEND END" +
                ") AS totalAmount,fd.feeType_id, fh.*\n" +
                "FROM FeesHeader fh \n" +
                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = @SCHOOL_ID" +
//                " INNER JOIN student s on s.id = fh.student_id " +
                " and  fh.id = (SELECT fh1.id FROM FeesHeader fh1" +
                " INNER JOIN FeesDetail fd1 ON fh1.id = fd1.feesHeader_id"+
                " where fh1.receipt_no = @RECEIPT_NO and fh1.category_id = 2" +
                " and fh1.schoolclass_id in (SELECT id FROM school_class WHERE school_id = @SCHOOL_ID)" +
                " ORDER BY fh1.id DESC LIMIT 1) " +
                " GROUP BY fd.feeType_id ORDER BY fh.created_on asc";

        query = query.replace("@SCHOOL_ID", schoolId);
        query = query.replace("@RECEIPT_NO", ReceiptNo);

        Log.d("Prev Recv Query", query);
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        Cursor c = null;
        boolean added = false;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    if (c.getInt(c.getColumnIndex(fees_type_id)) == 3 || c.getInt(c.getColumnIndex(fees_type_id)) == 8) {
                        for (PreviousReceivableModel p : prevRecv) {
                            if (p.getFeeType_id() == 3) {
                                String amount = c.getString(c.getColumnIndex("totalAmount"));
                                if (amount != null && p.getTotalAmount() != null) {
                                    int total = Integer.parseInt(amount) + Integer.parseInt(p.getTotalAmount());
                                    p.setTotalAmount("" + total);
                                    p.setForDate(c.getString(c.getColumnIndex(FOR_DATE)));
                                    added = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!added) {
                        PreviousReceivableModel prm = new PreviousReceivableModel();
                        prm.setFeeType_id(c.getInt(c.getColumnIndex(fees_type_id)) == 8 ? 3 : c.getInt(c.getColumnIndex(fees_type_id)));
                        prm.setTotalAmount(c.getString(c.getColumnIndex("totalAmount")));
//                        prm.setForDate(c.getString(c.getColumnIndex(FOR_DATE)));
                        prevRecv.add(prm);
                    }
                } while (c.moveToNext());

            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add(new PreviousReceivableModel(1, ""));
                prevRecv.add(new PreviousReceivableModel(2, ""));
                prevRecv.add(new PreviousReceivableModel(3, ""));
                prevRecv.add(new PreviousReceivableModel(4, ""));
                prevRecv.add(new PreviousReceivableModel(5, ""));
                prevRecv.add(new PreviousReceivableModel(6, ""));
                prevRecv.add(new PreviousReceivableModel(7, ""));
            }

            return prevRecv;

        } catch (Exception e) {
            e.printStackTrace();
            return prevRecv;
        } finally {
            if (c != null)
                c.close();
        }
    }

    public List<PreviousReceivableModel> getPrevReceivableWithFeeType(String schoolId, String grNo) {
        List<PreviousReceivableModel> prevRecv = new ArrayList<>();
        String query = GET_PREV_RECEIVABLES;
        query = query.replace("@SchoolId", schoolId);
        query = query.replace("@StudentGRNO", grNo);

        Log.d("Prev Recv Query", query);
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        boolean added = false;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    if (c.getInt(c.getColumnIndex(fees_type_id)) == 3 || c.getInt(c.getColumnIndex(fees_type_id)) == 8) {
                        for (PreviousReceivableModel p : prevRecv) {
                            if (p.getFeeType_id() == 3) {
                                String amount = c.getString(c.getColumnIndex("totalAmount"));
                                if (amount != null && p.getTotalAmount() != null) {
                                    int total = Integer.parseInt(amount) + Integer.parseInt(p.getTotalAmount());
                                    p.setTotalAmount("" + total);

                                    added = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!added) {
                        PreviousReceivableModel prm = new PreviousReceivableModel();
                        prm.setFeeType_id(c.getInt(c.getColumnIndex(fees_type_id)) == 8 ? 3 : c.getInt(c.getColumnIndex(fees_type_id)));
                        prm.setTotalAmount(c.getString(c.getColumnIndex("totalAmount")));
                        prevRecv.add(prm);
                    }
                } while (c.moveToNext());

            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add(new PreviousReceivableModel(1, ""));
                prevRecv.add(new PreviousReceivableModel(2, ""));
                prevRecv.add(new PreviousReceivableModel(3, ""));
                prevRecv.add(new PreviousReceivableModel(4, ""));
                prevRecv.add(new PreviousReceivableModel(5, ""));
                prevRecv.add(new PreviousReceivableModel(6, ""));
                prevRecv.add(new PreviousReceivableModel(7, ""));
            }

            return prevRecv;

        } catch (Exception e) {
            e.printStackTrace();
            return prevRecv;
        }
//        finally {
//            c.close();
//        }
    }

    public List<PreviousReceivableModel> getCashDepositRecord(String slipNo, String schoolIds) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        List<PreviousReceivableModel> prevRecv = new ArrayList<>();
        String query = GET_DEPOSIT_RECORD;
        query = query.replace("@slipNo", slipNo);
        query = query.replace("@schoolId", schoolIds);


        Log.d("Prev Recv Query", query);
        Cursor c = null;
        boolean added = false;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    if (c.getInt(c.getColumnIndex(fees_type_id)) == 3 || c.getInt(c.getColumnIndex(fees_type_id)) == 8) {
                        for (PreviousReceivableModel p : prevRecv) {
                            if (p.getFeeType_id() == 3) {
                                String amount = c.getString(c.getColumnIndex("totalAmount"));
                                if (amount != null && p.getTotalAmount() != null) {
                                    int total = Integer.parseInt(amount) + Integer.parseInt(p.getTotalAmount());
                                    p.setTotalAmount("" + total);

                                    added = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!added) {
                        PreviousReceivableModel prm = new PreviousReceivableModel();
                        prm.setFeeType_id(c.getInt(c.getColumnIndex(fees_type_id)) == 8 ? 3 : c.getInt(c.getColumnIndex(fees_type_id)));
                        prm.setTotalAmount(c.getString(c.getColumnIndex("totalAmount")));
                        prevRecv.add(prm);
                    }
                } while (c.moveToNext());

            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add(new PreviousReceivableModel(1, ""));
                prevRecv.add(new PreviousReceivableModel(2, ""));
                prevRecv.add(new PreviousReceivableModel(3, ""));
                prevRecv.add(new PreviousReceivableModel(4, ""));
                prevRecv.add(new PreviousReceivableModel(5, ""));
                prevRecv.add(new PreviousReceivableModel(6, ""));
                prevRecv.add(new PreviousReceivableModel(7, ""));
            }

            return prevRecv;

        } catch (Exception e) {
            e.printStackTrace();
            return prevRecv;
        }
//        finally {
//            c.close();
//        }
    }

    public CashDepositModel getCashDepositRecordBy(String slipNo, String schoolId) {
        String query = "SELECT cd.* FROM CashDeposit cd" +
                " WHERE cd.deposit_slip_no = @slipNo AND cd.school_id IN(@schoolId)";

        query = query.replace("@slipNo", slipNo);
        query = query.replace("@schoolId", schoolId);

        CashDepositModel cashDepositModel = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                cashDepositModel = new CashDepositModel();
                cashDepositModel.setRemarks(c.getString(c.getColumnIndex("remarks")));
            }

            return cashDepositModel;

        } catch (Exception e) {
            e.printStackTrace();
            return cashDepositModel;
        } finally {
            if (c != null)
                c.close();
        }
    }

    public CashInHandModel getCashInHandData(String schoolIds) {
        CashInHandModel model = new CashInHandModel();
        UserModel userModel = DatabaseHelper.getInstance(context).getCurrentLoggedInUser();
        Cursor cursor = null;
        String sqlquerry;
//        if (schoolId > 0) {
        sqlquerry = GET_CASH_IN_HAND_DATA3;

        sqlquerry = sqlquerry.replace("@school_ids", schoolIds);
//            sqlquerry = sqlquerry.replace("@UserId", userModel.getId() + "");
//        } else {
//            sqlquerry = GET_CASH_IN_HAND_DATA_FOR_ALL_SCHOOL;
//            sqlquerry = sqlquerry.replace("@UserId", userModel.getId() + "");
//        }

        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(sqlquerry, null);
            double total = 0.0;
            String ids = "";
            if (cursor.moveToFirst()) {

                do {
                    total += cursor.getDouble(cursor.getColumnIndex("cashInHand"));
                } while (cursor.moveToNext());

                model.setTotal((int) total + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return model;

    }

    public CashInHandModel getCashInHandData(int userId) {
        CashInHandModel model = new CashInHandModel();
        Cursor cursor = null;
        String sqlquerry;
        if (userId > 0) {
            sqlquerry = GET_CASH_IN_HAND_DATA;
            sqlquerry = sqlquerry.replace("@UserId", userId + "");
        } else {
            sqlquerry = GET_CASH_IN_HAND_DATA_FOR_ALL_SCHOOL;
            sqlquerry = sqlquerry.replace("@UserId", userId + "");
        }

        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(sqlquerry, null);
            double total = 0.0;

            if (cursor.moveToFirst()) {

                model.setLastDepositDate(cursor.getString(cursor.getColumnIndex("lastDepositedDate")));
                do {
                    int feeType_id = cursor.getInt(cursor.getColumnIndex(FEE_TYPE_ID));
                    String amount = cursor.getString(cursor.getColumnIndex("totalAmount"));
                    if (feeType_id == 1)
                        model.setAdmissionFees(amount);
                    else if (feeType_id == 2)
                        model.setExamFees(amount);
                    else if (feeType_id == 3)
                        model.setMonthlyFees(amount);
                    else if (feeType_id == 4)
                        model.setBooks(amount);
                    else if (feeType_id == 5)
                        model.setCopies(amount);
                    else if (feeType_id == 6)
                        model.setUniforms(amount);
                    else if (feeType_id == 7)
                        model.setOthers(amount);


                    total += cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                } while (cursor.moveToNext());


                model.setTotal((int) total + "");
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return model;

    }

    public List<CashInHandFeeTypeModel> getCashInHandDataFeeType(String school_ids) {
        Cursor cursor = null;
        String sqlquerry;
        List<CashInHandFeeTypeModel> cashInHandFeeTypeModelList = new ArrayList<>();
//        if (userId > 0) {
        String maxCreatedOnOfCashDeposit = getMaxCreatedOnOfCashDeposit();
        if(!Strings.isEmptyOrWhitespace(maxCreatedOnOfCashDeposit)) {
            String updatedMaxCreatedOnOfCashDeposit = AppModel.getInstance().convertDatetoFormat(
                    maxCreatedOnOfCashDeposit,
                    "yyyy-MM-dd hh:mm:ss a",
                    "yyyy-MM-dd");

            sqlquerry = NEW_GET_CASH_IN_HAND_DATA;
            sqlquerry = sqlquerry.replace("@school_ids", school_ids);
            sqlquerry = sqlquerry.replace("@MaxDate", updatedMaxCreatedOnOfCashDeposit);

            ArrayList<FeesHeaderModel> models = new ArrayList<>();
            try {


                SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

                cursor = db.rawQuery(sqlquerry, null);
                if (cursor.moveToFirst()) {
                    do {
                        FeesHeaderModel model = new FeesHeaderModel();
                        model.setFeeType_id(cursor.getInt(cursor.getColumnIndex("feeType_id")));
                        model.setTotal_amount(cursor.getLong(cursor.getColumnIndex("fee_amount")));
                        model.setCreatedOn(cursor.getString(cursor.getColumnIndex("created_on")));
                        models.add(model);
                    } while (cursor.moveToNext());

                    Date maxDepositDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(maxCreatedOnOfCashDeposit);

                    if (!CollectionUtils.isEmpty(models)) {

                        // Remove courses that haven’t got the latest date
                        Iterator<FeesHeaderModel> iterator = models.iterator();
                        while (iterator.hasNext()) {
                            FeesHeaderModel model = iterator.next();
                            if (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(model.getCreatedOn()).before(maxDepositDate)) {
                                iterator.remove();
                            }
                        }

                    }

                    if (!CollectionUtils.isEmpty(models)) {
                        for (int i = 1; i <= 8; i++) {
                            CashInHandFeeTypeModel cihfmodel = new CashInHandFeeTypeModel();
                            int feeType_id = i;
                            long sum = (long) models.stream().filter(c -> c.getFeeType_id() == feeType_id).mapToDouble(FeesHeaderModel::getTotal_amount).sum();
                            if (feeType_id != 8) {
                                cihfmodel.setFeeType_id(feeType_id == 8 ? 3 : feeType_id);
                                cihfmodel.setAmount(String.valueOf(sum));
                                cashInHandFeeTypeModelList.add(cihfmodel);
                            } else {
                                for (CashInHandFeeTypeModel cin : cashInHandFeeTypeModelList) {
                                    if (cin.getFeeType_id() == 3) {
                                        if (cin.getAmount() != null && sum > 0) {
                                            long total = sum + Long.parseLong(cin.getAmount());
                                            cin.setAmount("" + total);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
//                        cashInHand = (long) feesHeaderModels.stream().mapToDouble(FeesHeaderModel::getTotal_amount).sum();
                    }

                }
                 else {
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(1, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(2, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(3, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(4, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(5, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(6, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(7, "0"));
                }


            } catch (
                    Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        } else {
            sqlquerry = GET_CASH_IN_HAND_DATA;
            sqlquerry = sqlquerry.replace("@school_ids", school_ids);

            try {
                SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
                cursor = db.rawQuery(sqlquerry, null);
                boolean added = false;
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getInt(cursor.getColumnIndex(fees_type_id)) == 3 || cursor.getInt(cursor.getColumnIndex(fees_type_id)) == 8) {
                            for (CashInHandFeeTypeModel cin : cashInHandFeeTypeModelList) {
                                if (cin.getFeeType_id() == 3) {
                                    String amount = cursor.getString(cursor.getColumnIndex("totalAmount"));
                                    if (amount != null && cin.getAmount() != null) {
                                        int total = Integer.parseInt(amount) + Integer.parseInt(cin.getAmount());
                                        cin.setAmount("" + total);
                                        added = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!added) {
                            CashInHandFeeTypeModel cihfmodel = new CashInHandFeeTypeModel();

                            int feeType_id = cursor.getInt(cursor.getColumnIndex(FEE_TYPE_ID));
                            String amount = cursor.getString(cursor.getColumnIndex("totalAmount"));
                            cihfmodel.setFeeType_id(feeType_id == 8 ? 3 : feeType_id);
                            cihfmodel.setAmount(amount);
                            cashInHandFeeTypeModelList.add(cihfmodel);
                        }

                    } while (cursor.moveToNext());
                } else {
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(1, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(2, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(3, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(4, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(5, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(6, "0"));
                    cashInHandFeeTypeModelList.add(new CashInHandFeeTypeModel(7, "0"));
                }


            } catch (
                    Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
        return cashInHandFeeTypeModelList;

    }

    public boolean updateAppReceiptForCashDeposit(int schoolId, int cashDepositId) {

        String updateQuery = "UPDATE FeesHeader SET CashDeposit_id = " + cashDepositId +
                " ,CashDeposit_id_Type = 'L'" +
                " WHERE id IN (SELECT fh.id FROM FeesHeader fh" +
                " INNER JOIN school_class sc on sc.id = fh.schoolclass_id" +
                " where sc.school_id = @SchoolID AND (fh.CashDeposit_id IS NULL" +
                " OR fh.CashDeposit_id = '')) AND Category_Id IN(2)";

        updateQuery = updateQuery.replace("@SchoolID", schoolId + "");

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL(updateQuery);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
//        db.beginTransaction();
//        List<Integer> list = getAllFeesHeaderForDeposit(schoolId, db);
//        try {
//            for (int i : list) {
//                ContentValues values = new ContentValues();
//                values.put(CASH_DEPOSIT_ID, cashDepositId);
//                db.update(TABLE_FEES_HEADER, values, ID + " = " + i + " AND " + CATEGORY_ID + " IN(2)", null);
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            db.endTransaction();
//        }
//        return true;
    }

    public boolean updateAppReceiptForCashDeposit(int cashDepositId, String ids) {

        String updateQuery = "UPDATE FeesHeader SET CashDeposit_id = " + cashDepositId +
                " ,CashDeposit_id_Type = 'L'" +
                " WHERE id IN (@ids) AND Category_Id IN(2)";

        updateQuery = updateQuery.replace("@ids", ids);

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            db.execSQL(updateQuery);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
//        db.beginTransaction();
//        List<Integer> list = getAllFeesHeaderForDeposit(schoolId, db);
//        try {
//            for (int i : list) {
//                ContentValues values = new ContentValues();
//                values.put(CASH_DEPOSIT_ID, cashDepositId);
//                db.update(TABLE_FEES_HEADER, values, ID + " = " + i + " AND " + CATEGORY_ID + " IN(2)", null);
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            db.endTransaction();
//        }
//        return true;
    }

    public void createFeeTypeTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_FEE_TYPE);
    }

    public void createFeeTypeTable(SQLiteDatabase DB) {
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_FEE_TYPE);
    }

    public void insertTempDataInFeeTypeTable() {
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_FEE_TYPE + " ("
                    + type_id + ","
                    + fee_type_name + ") VALUES ";

            insertQuery += "(1,'Admission Fees'),";
            insertQuery += "(2,'Examination Fees'),";
            insertQuery += "(3,'Monthly Fees'),";
            insertQuery += "(4,'Books'),";
            insertQuery += "(5,'Copies'),";
            insertQuery += "(6,'Uniform'),";
            insertQuery += "(7,'Others'),";
            insertQuery += "(8,'TCF Scholarship')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTempDataInFeeTypeTable(SQLiteDatabase DB) {
        try {
//            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_FEE_TYPE + " ("
                    + type_id + ","
                    + fee_type_name + ") VALUES ";

            insertQuery += "(1,'Admission Fees'),";
            insertQuery += "(2,'Examination Fees'),";
            insertQuery += "(3,'Monthly Fees'),";
            insertQuery += "(4,'Books'),";
            insertQuery += "(5,'Copies'),";
            insertQuery += "(6,'Uniform'),";
            insertQuery += "(7,'Others'),";
            insertQuery += "(8,'TCF Scholarship')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempFeeTypeTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_FEE_TYPE;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempFeeTypeTable(SQLiteDatabase DB) {
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_FEE_TYPE;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FeeTypeModel> getFeeTypes() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        List<FeeTypeModel> feeTypeModelList = new ArrayList<>();
        String query = "Select * from " + TABLE_FEE_TYPE;
        Cursor cr = null;
        try {
            cr = DB.rawQuery(query, null);
            if (cr.moveToFirst()) {
                do {
                    FeeTypeModel feeTypeModel = new FeeTypeModel();
                    feeTypeModel.setId(cr.getInt(cr.getColumnIndex(type_id)));
                    feeTypeModel.setName(cr.getString(cr.getColumnIndex(fee_type_name)));

                    feeTypeModelList.add(feeTypeModel);
                } while (cr.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cr != null) {
                cr.close();
            }
        }
        return feeTypeModelList;
    }

    public List<FeeTypeModel> getFeeTypesForReceivables() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        List<FeeTypeModel> feeTypeModelList = new ArrayList<>();
        String query = "Select * from " + TABLE_FEE_TYPE;
        Cursor cr = null;
        try {
            cr = DB.rawQuery(query, null);
            if (cr.moveToFirst()) {
                do {
                    if (cr.getInt(cr.getColumnIndex(type_id)) != 8) {
                        FeeTypeModel feeTypeModel = new FeeTypeModel();
                        feeTypeModel.setId(cr.getInt(cr.getColumnIndex(type_id)));
                        feeTypeModel.setName(cr.getString(cr.getColumnIndex(fee_type_name)));

                        feeTypeModelList.add(feeTypeModel);
                    }
                } while (cr.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cr != null) {
                cr.close();
            }
        }
        return feeTypeModelList;
    }

    public void createTransactionTypeTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_TRANSACTION_TYPE);
    }

    public void createTransactionTypeTable(SQLiteDatabase DB) {
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_TRANSACTION_TYPE);
    }

    public void insertTempDataInTransactionTypeTable() {
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_TRANSACTION_TYPE + " ("
                    + transaction_type_id + ","
                    + transaction_type_name + ") VALUES ";

            insertQuery += "(1,'Normal'),";
            insertQuery += "(2,'Correction'),";
            insertQuery += "(3,'Waiver')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTempDataInTransactionTypeTable(SQLiteDatabase DB) {
        try {
//            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_TRANSACTION_TYPE + " ("
                    + transaction_type_id + ","
                    + transaction_type_name + ") VALUES ";

            insertQuery += "(1,'Normal'),";
            insertQuery += "(2,'Correction'),";
            insertQuery += "(3,'Waiver')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public long insertFeesReceipt(FeesHeaderModel feesHeaderModel) {
//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
//        long id = -1;
//
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(SYS_ID, feesHeaderModel.getSys_id());
//            if (feesHeaderModel.getReceiptNumber() > -1) {
//                cv.put(RECEIPT_NO, feesHeaderModel.getReceiptNumber());
//            }
//            cv.put(RECEIPT_FLAG, feesHeaderModel.getReciptFlag());
//            cv.put(STUDENT_ID, feesHeaderModel.getStudentId());
//            cv.put(SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
//            cv.put(SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
//            cv.put(DEVICE_ID, feesHeaderModel.getDeviceId());
//            cv.put(FEES_ADMISSION, feesHeaderModel.getAdmissionFees());
//            cv.put(FEES_BOOKS, feesHeaderModel.getBookFees());
//            cv.put(FEES_COPIES, feesHeaderModel.getCopyFees());
//            cv.put(FEES_EXAM, feesHeaderModel.getExamFees());
//            cv.put(FEES_TUTION, feesHeaderModel.getTutionFees());
//            cv.put(FEES_UNIFORMS, feesHeaderModel.getUniformFees());
//            cv.put(FEES_OTHERS, feesHeaderModel.getOthersFees());
//            cv.put(CREATED_ON, feesHeaderModel.getCreatedOn());
//            cv.put(CREATED_BY, feesHeaderModel.getCreatedBy());
//            cv.put(CREATED_FROM, feesHeaderModel.getCreated_from());
//            cv.put(IS_CORRECTION, feesHeaderModel.getCorrectionType());
//
//            id = db.insertOrThrow(TABLE_FEES_HEADER, null, cv);
//            return id;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return id;
//    }

//    public static final String CREATE_TABLE_FEES_HEADER = "CREATE TABLE " + TABLE_FEES_HEADER + " ("
//
//            + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + SYS_ID + "  INTEGER,"
//            + RECEIPT_NO + "  TEXT,"
//            + RECEIPT_FLAG + "  TEXT,"
//            + SCHOOL_CLASS_ID + "  INTEGER,"
//            + SCHOOL_YEAR_ID + "  INTEGER,"
//            + STUDENT_ID + "  INTEGER,"
//            + CREATED_FROM + "  TEXT,"
//            + CREATED_BY + "  TEXT,"
//            + CREATED_ON + "  TEXT,"
//            + UPLOADED_ON + "  TEXT,"
//            + IS_CORRECTION + "  INTEGER,"
//            + FEES_ADMISSION + "  REAL,"
//            + FEES_EXAM + "  REAL,"
//            + FEES_TUTION + "  REAL,"
//            + FEES_BOOKS + "  REAL,"
//            + FEES_COPIES + "  REAL,"
//            + FEES_UNIFORMS + "  REAL,"
//            + FEES_OTHERS + "  REAL,"
//            + DEVICE_ID + "  TEXT,"
//            + CASH_DEPOSIT_ID + "  INTEGER,"
//            + DOWNLOADED_ON + "  TEXT" + ")";

//    public long insertFeesReceipt(FeesHeaderModel feesHeaderModel) {
//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
//        long id = -1;
//
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(SYS_ID, feesHeaderModel.getSys_id());
//            if (feesHeaderModel.getReceiptNumber() > -1) {
//                cv.put(RECEIPT_NO, feesHeaderModel.getReceiptNumber());
//            }
//            cv.put(RECEIPT_FLAG, feesHeaderModel.getReciptFlag());
//            cv.put(STUDENT_ID, feesHeaderModel.getStudentId());
//            cv.put(SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
//            cv.put(SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
//            cv.put(DEVICE_ID, feesHeaderModel.getDeviceId());
//            cv.put(FEES_ADMISSION, feesHeaderModel.getAdmissionFees());
//            cv.put(FEES_BOOKS, feesHeaderModel.getBookFees());
//            cv.put(FEES_COPIES, feesHeaderModel.getCopyFees());
//            cv.put(FEES_EXAM, feesHeaderModel.getExamFees());
//            cv.put(FEES_TUTION, feesHeaderModel.getTutionFees());
//            cv.put(FEES_UNIFORMS, feesHeaderModel.getUniformFees());
//            cv.put(FEES_OTHERS, feesHeaderModel.getOthersFees());
//            cv.put(CREATED_ON, feesHeaderModel.getCreatedOn());
//            cv.put(CREATED_BY, feesHeaderModel.getCreatedBy());
//            cv.put(CREATED_FROM, feesHeaderModel.getCreated_from());
//            cv.put(IS_CORRECTION, feesHeaderModel.getCorrectionType());
//
//            id = db.insertOrThrow(TABLE_FEES_HEADER, null, cv);
//            return id;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return id;
//    }

    public void dropTempTransactionTypeTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_TRANSACTION_TYPE;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempTransactionTypeTable(SQLiteDatabase DB) {
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_TRANSACTION_TYPE;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTransactionCategoryTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_TRANSACTION_CATEGORY);
    }

    public void createTransactionCategoryTable(SQLiteDatabase DB) {
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.execSQL(CREATE_TABLE_TRANSACTION_CATEGORY);
    }

    public void insertTempDataInTransactionCategoryTable() {
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_TRANSACTION_CATEGORY + " ("
                    + transaction_category_id + ","
                    + transaction_category_name + ") VALUES ";

            insertQuery += "(1,'Invoice'),";
            insertQuery += "(2,'Receipt'),";
            insertQuery += "(3,'Opening Balance'),";
            insertQuery += "(4,'Closing Balance')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTempDataInTransactionCategoryTable(SQLiteDatabase DB) {
        try {
//            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            String insertQuery = "Insert into " + TABLE_TRANSACTION_CATEGORY + " ("
                    + transaction_category_id + ","
                    + transaction_category_name + ") VALUES ";

            insertQuery += "(1,'Invoice'),";
            insertQuery += "(2,'Receipt'),";
            insertQuery += "(3,'Opening Balance'),";
            insertQuery += "(4,'Closing Balance')";
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempTransactionCategoryTable() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_TRANSACTION_CATEGORY;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTempTransactionCategoryTable(SQLiteDatabase DB) {
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "Drop table if exists " + TABLE_TRANSACTION_CATEGORY;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long insertFeesHeader(FeesHeaderModel feesHeaderModel) {
        long i = -1;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            cv.put(STUDENT_ID, feesHeaderModel.getStudentId());
            cv.put(SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
            cv.put(SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
            cv.put(ACADEMIC_SESSION_ID, feesHeaderModel.getAcademicSession_id());
            cv.put(SYS_ID, feesHeaderModel.getSys_id());
            cv.put(DEVICE_ID, feesHeaderModel.getDeviceId());
            cv.put(FOR_DATE, feesHeaderModel.getFor_date());
            cv.put(TOTAL_AMOUNT, feesHeaderModel.getTotal_amount());
            cv.put(TRANSACTION_TYPE_ID, feesHeaderModel.getTransactionType_id());
            cv.put(CATEGORY_ID, feesHeaderModel.getCategory_id());
            if (feesHeaderModel.getReceiptNumber() > -1) {
                cv.put(RECEIPT_NO, feesHeaderModel.getReceiptNumber());
            }
            cv.put(CREATED_ON, feesHeaderModel.getCreatedOn());
            cv.put(CREATED_BY, feesHeaderModel.getCreatedBy());
            if (feesHeaderModel.getReceipt_id() > 0)
                cv.put(RECEIPT_ID, feesHeaderModel.getReceipt_id());
//                cv.put(FEE_TYPE_ID, feesHeaderModel.getFeeType_id());
//            cv.put(IS_CORRECTION, feesHeaderModel.getCorrectionType());

            i = db.insertOrThrow(TABLE_FEES_HEADER, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return i;
    }

    public long insertCorrection(FeesHeaderModel feesHeaderModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(STUDENT_ID, feesHeaderModel.getStudentId());
            cv.put(SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
            cv.put(SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
            cv.put(ACADEMIC_SESSION_ID, feesHeaderModel.getAcademicSession_id());
            cv.put(CREATED_ON, feesHeaderModel.getCreatedOn());
            cv.put(CREATED_BY, feesHeaderModel.getCreatedBy());
            cv.put(DEVICE_ID, feesHeaderModel.getDeviceId());
            cv.put(FOR_DATE, feesHeaderModel.getFor_date());
            cv.put(TOTAL_AMOUNT, feesHeaderModel.getTotal_amount());
            cv.put(TRANSACTION_TYPE_ID, feesHeaderModel.getTransactionType_id());
            cv.put(CATEGORY_ID, feesHeaderModel.getCategory_id());
            cv.put(RECEIPT_NO, feesHeaderModel.getReceiptNumber());
            cv.put(REMARKS, feesHeaderModel.getRemarks());
            if (feesHeaderModel.getReceipt_id() > 0)
                cv.put(RECEIPT_ID, feesHeaderModel.getReceipt_id());


            if (feesHeaderModel.getTotal_amount() != 0){
                id = db.insert(TABLE_FEES_HEADER, null, cv);
            }
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public long insertWavier(FeesHeaderModel feesHeaderModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(STUDENT_ID, feesHeaderModel.getStudentId());
            cv.put(SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
            cv.put(SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
            cv.put(ACADEMIC_SESSION_ID, feesHeaderModel.getAcademicSession_id());
            cv.put(CREATED_ON, feesHeaderModel.getCreatedOn());
            cv.put(CREATED_BY, feesHeaderModel.getCreatedBy());
            cv.put(DEVICE_ID, feesHeaderModel.getDeviceId());
            cv.put(FOR_DATE, feesHeaderModel.getFor_date());
            cv.put(TOTAL_AMOUNT, feesHeaderModel.getTotal_amount());
            cv.put(TRANSACTION_TYPE_ID, feesHeaderModel.getTransactionType_id());
            cv.put(CATEGORY_ID, feesHeaderModel.getCategory_id());
            cv.put(RECEIPT_NO, feesHeaderModel.getReceiptNumber());
            cv.put(REMARKS, feesHeaderModel.getRemarks());

            id = db.insert(TABLE_FEES_HEADER, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public boolean doesFeesReceiptExists(FeesHeaderModel feesHeaderModel, int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select * from " + TABLE_FEES_HEADER + " fh inner join school_class sc on fh.schoolclass_id = sc.id where fh.receipt_no = '@RNO' and sc.school_id = @SchoolId; ";

        query = query.replace("@RNO", feesHeaderModel.getReceiptNumber() + "");
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

    public boolean isFeesReceiptForSameStudent(FeesHeaderModel feesHeaderModel, int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select * from " + TABLE_FEES_HEADER + " fh inner join school_class sc on fh.schoolclass_id = sc.id where fh.receipt_no = '@RNO' and sc.school_id = @SchoolId \n" +
                "and fh.stuednt_id = @StudentId";

        query = query.replace("@RNO", feesHeaderModel.getReceiptNumber() + "");
        query = query.replace("@SchoolId", schoolId + "");
        query = query.replace("@StudentId", feesHeaderModel.getStudentId() + "");

        try {

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int findFeesHeaderId(String date, String SchoolClassID) {
        String Query = "SELECT " + ID + " FROM " + TABLE_FEES_HEADER + " WHERE " + SCHOOL_CLASS_ID + " = " + SchoolClassID +
                " AND " + CREATED_ON + " = '" + date + "'";
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        int HeaderId = 0;
        try {
            cursor = db.rawQuery(Query, null);
            if (cursor.moveToFirst()) {
                HeaderId = cursor.getInt(cursor.getColumnIndex(ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return HeaderId;
    }

    public void insertFeesDetails(long feesHeader_id, List<ViewReceivablesModels> viewReceivablesModels, int transactionCategory) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id;
        try {
            for (int i = 0; i < viewReceivablesModels.size(); i++) {
                ContentValues cv = new ContentValues();
                double amount = 0.0;

                /* For AppInvoice "1" For AppReceipt "2" */
                if (transactionCategory == 1) {
                    if (viewReceivablesModels.get(i).getTodaySales() != null && !viewReceivablesModels.get(i).getTodaySales().equals("")) {
                        amount = Double.valueOf(viewReceivablesModels.get(i).getTodaySales());
                        cv.put(fee_amount, amount);
                        cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                        cv.put(fees_header_id, (int) feesHeader_id);
                        id = db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                    }
//                    else
//                        cv.put(fee_amount, amount);
                } else if (transactionCategory == 2) {
                    if (viewReceivablesModels.get(i).getAmountReceived() != null && !viewReceivablesModels.get(i).getAmountReceived().equals("")) {
                        amount = Double.valueOf(viewReceivablesModels.get(i).getAmountReceived());
                        cv.put(fee_amount, amount);
                        cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                        cv.put(fees_header_id, (int) feesHeader_id);
                        id = db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                    }
//                    else
//                        cv.put(fee_amount, amount);
                }
//                cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
//                cv.put(fees_header_id, (int) feesHeader_id);
//                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFeesDetail(long feesHeader_id, ViewReceivablesModels viewReceivablesModels) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues cv = new ContentValues();
            double amount = 0.0;

            if (viewReceivablesModels.getTodaySales() != null && !viewReceivablesModels.getTodaySales().equals("")) {
                amount = Double.valueOf(viewReceivablesModels.getTodaySales());
                cv.put(fee_amount, amount);
                cv.put(fees_type_id, viewReceivablesModels.getFeeTypeId());
                cv.put(fees_header_id, (int) feesHeader_id);
                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFeesDetailsCorrection(long feesHeader_id, List<ViewReceivablesCorrectionModel> viewReceivablesModels, int transactionCategory) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            for (int i = 0; i < viewReceivablesModels.size(); i++) {
                ContentValues cv = new ContentValues();
                double amount = 0.0;

//                if (viewReceivablesModels.get(i).getNewReceived().equals(viewReceivablesModels.get(i).getOldRecieved())) {
//                    continue;
//                }

                /* For AppInvoice "1" For AppReceipt "2" */
                if (transactionCategory == 1) {
                    if (viewReceivablesModels.get(i).getNewSales() != null && !viewReceivablesModels.get(i).getNewSales().equals("")) {
                        amount = Double.valueOf(viewReceivablesModels.get(i).getNewSales());
                        if (amount != 0) {
                            cv.put(fee_amount, amount);
                            cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                            cv.put(fees_header_id, (int) feesHeader_id);
                            db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                        }
                    }
//                    else
//                        cv.put(fee_amount, amount);
                } else if (transactionCategory == 2) {
                    if (viewReceivablesModels.get(i).getNewReceived() != null && !viewReceivablesModels.get(i).getNewReceived().equals("")) {
                        amount = Double.valueOf(viewReceivablesModels.get(i).getNewReceived());
                        if (amount != 0) {
                            cv.put(fee_amount, amount);
                            cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                            cv.put(fees_header_id, (int) feesHeader_id);
                            db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                        }
                    }
//                    else
//                        cv.put(fee_amount, amount);
                }
//                cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
//                cv.put(fees_header_id, (int) feesHeader_id);
//                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFeesDetailsCorrectionOld(long feesHeader_id, List<ViewReceivablesCorrectionModel> viewReceivablesModels, int transactionCategory) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            for (int i = 0; i < viewReceivablesModels.size(); i++) {
                ContentValues cv = new ContentValues();
                double amount = 0.0;


//                if (viewReceivablesModels.get(i).getOldRecieved().equals(viewReceivablesModels.get(i).getNewReceived())) {
//                    continue;
//                }

                /* For AppInvoice "1" For AppReceipt "2" */
                if (transactionCategory == 1) {
                    if (viewReceivablesModels.get(i).getOldSale() != null && !viewReceivablesModels.get(i).getOldSale().equals("")) {
                        amount = Double.valueOf(viewReceivablesModels.get(i).getOldSale());
                        if (amount != 0) {
                            cv.put(fee_amount, -1 * amount);
                            cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                            cv.put(fees_header_id, (int) feesHeader_id);
                            db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                        }
                    }
//                    else
//                        cv.put(fee_amount, amount);
                } else if (transactionCategory == 2) {
                    if (viewReceivablesModels.get(i).getOldRecieved() != null && !viewReceivablesModels.get(i).getOldRecieved().equals("")) {
                        amount = Double.valueOf(viewReceivablesModels.get(i).getOldRecieved());
                        if (amount != 0) {
                            cv.put(fee_amount, -1 * amount);
                            cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                            cv.put(fees_header_id, (int) feesHeader_id);
                            db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                        }
                    }
//                    else
//                        cv.put(fee_amount, amount);
                }
//                cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
//                cv.put(fees_header_id, (int) feesHeader_id);
//                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FeesHeaderModel> getAllFeesHeaderForUpload() {
        List<FeesHeaderModel> fhmList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

//        String query = "select fh.* from FeesHeader fh  where (fh.sys_id IS NULL OR fh.sys_id = 0) and fh.created_by = @UserId";
        String query = "select fh.*,cd.sys_id as cashdeposit_sys_id from FeesHeader fh\n" +
                "left join CashDeposit cd on cd.id = fh.CashDeposit_id\n" +
                "where (fh.uploaded_on IS NULL OR fh.uploaded_on = '') ";

//        query = query.replace("@UserId", userId + "");
        Log.d("Upload query", query);

        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    FeesHeaderModel model = new FeesHeaderModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setStudentId(cursor.getInt(cursor.getColumnIndex(STUDENT_ID)));
                    model.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
                    model.setSchoolYearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
                    model.setAcademicSession_id(cursor.getInt(cursor.getColumnIndex(ACADEMIC_SESSION_ID)));
                    model.setDeviceId(cursor.getString(cursor.getColumnIndex(DEVICE_ID)));
                    model.setFor_date(cursor.getString(cursor.getColumnIndex(FOR_DATE)));
                    model.setTotal_amount(cursor.getInt(cursor.getColumnIndex(TOTAL_AMOUNT)));
                    model.setTransactionType_id(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
                    model.setCategory_id(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
                    model.setReceiptNumber(cursor.getLong(cursor.getColumnIndex(RECEIPT_NO)));
                    model.setCashDepositId(cursor.getString(cursor.getColumnIndex(CASH_DEPOSIT_ID)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
                    model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                    model.setRemarks(cursor.getString(cursor.getColumnIndex(REMARKS)));

                    String cashDepositSysId = cursor.getInt(cursor.getColumnIndex("cashdeposit_sys_id")) + "";
                    if (cashDepositSysId.equals("0") || cashDepositSysId.isEmpty())
                        model.setCashDeposit_Sys_id("NULL");
                    else
                        model.setCashDeposit_Sys_id(cashDepositSysId);

                    long receiptId = cursor.getInt(cursor.getColumnIndex(RECEIPT_ID));
                    if (receiptId > 0) {
                        model.setReceipt_id(receiptId);
                    }


                    if (cursor.getInt(cursor.getColumnIndex(SYS_ID)) > 0)
                        model.setSys_id(cursor.getInt(cursor.getColumnIndex(SYS_ID)));

                    fhmList.add(model);


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
        return fhmList;
    }

    public List<Integer> getAllFeesHeaderForDeposit(int SchoolId, SQLiteDatabase db) {
        List<Integer> fhmList = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT fh.id FROM FeesHeader fh \n" +
                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id \n" +
                "where sc.school_id = @schoolId AND (fh.CashDeposit_id IS NULL\n" +
                "OR fh.CashDeposit_id = '')";

        query = query.replace("@schoolId", String.valueOf(SchoolId));


        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    fhmList.add(cursor.getInt(cursor.getColumnIndex(ID)));
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
        return fhmList;
    }

    public int getMaxSysId(int schoolId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = " select max(sys_id) as sys_id from FeesHeader fh \n" +
                " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id \n" +
                " WHERE sc.school_id = " + schoolId;
        String sys_id = "";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                sys_id = cursor.getString(cursor.getColumnIndex(SYS_ID));
                if (sys_id != null && !sys_id.isEmpty())
                    return Integer.valueOf(sys_id);
                else
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return 0;
    }

    public String getLatestModifiedOn(int schoolId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = " select fh.modified_on from FeesHeader fh" +
                " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id" +
                " WHERE sc.school_id = " + schoolId +
                " order by fh.modified_on desc limit 1";
        String modified_on = "";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                modified_on = cursor.getString(cursor.getColumnIndex(MODIFIED_ON));
                if (modified_on != null && !modified_on.isEmpty())
                    return modified_on;
                else {
//                    return "2000-01-01 00:00:00 AM";        //by default
                    return "2000-01-01T12:00:00";        //by default
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return modified_on;
    }

    public ArrayList<FeesDetailUploadModel> getAllFeesDetailForUpload(int FeesHeaderId) {
        ArrayList<FeesDetailUploadModel> fdmList = new ArrayList<FeesDetailUploadModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "select * from " + TABLE_FEES_DETAIL + " where " + fees_header_id + "= " + FeesHeaderId;
//            String selectQuery = "select distinct fd.* from " + TABLE_FEES_HEADER + " fh,"
//                    + TABLE_FEES_DETAIL + " fd  where fh.id = " + FeesHeaderId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    FeesDetailUploadModel fdm = new FeesDetailUploadModel();
//                    fdm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    fdm.setFeeHeader_id(cursor.getInt(cursor.getColumnIndex(fees_header_id)));
                    fdm.setFeeType_id(cursor.getInt(cursor.getColumnIndex(fees_type_id)));
                    fdm.setAmount(cursor.getInt(cursor.getColumnIndex(fee_amount)));
                    fdmList.add(fdm);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return fdmList;
    }

    public void genericUpdateMethod(ContentValues values, String deviceId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            int id = db.update(TABLE_FEES_HEADER, values, ID + " = " + deviceId, null);
            Log.d("appFeesHeader", "" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public long updateTableColumns(String tableName, ContentValues cv, int id) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = cv;
            long i = DB.update(tableName, values, ID + " = " + id, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertDownloadedFeesHeader(FeesHeaderModel feesHeaderModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppReceipt.STUDENT_ID, feesHeaderModel.getStudentId());
            cv.put(AppReceipt.SYS_ID, feesHeaderModel.getSys_id());
            if (feesHeaderModel.getCashDepositId() != null && !feesHeaderModel.getCashDepositId().isEmpty())
                cv.put(AppReceipt.CASH_DEPOSIT_ID, feesHeaderModel.getCashDepositId());

            if (feesHeaderModel.getReceiptNumber() > -1)
                cv.put(AppReceipt.RECEIPT_NO, feesHeaderModel.getReceiptNumber());
            cv.put(AppReceipt.CREATED_ON, feesHeaderModel.getCreatedOn());
            cv.put(AppReceipt.UPLOADED_ON, feesHeaderModel.getUploadedOn());
            cv.put(AppReceipt.CREATED_BY, feesHeaderModel.getCreatedBy());
            cv.put(AppReceipt.SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
            cv.put(AppReceipt.SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
            cv.put(AppReceipt.DEVICE_ID, feesHeaderModel.getDeviceId());
            cv.put(AppReceipt.DOWNLOADED_ON, feesHeaderModel.getDownloadedOn());


            id = db.insert(TABLE_FEES_HEADER, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public int FindFeesHeaderRecord(int headerId) throws Exception {
        Cursor cursor = null;
        FeesHeaderModel model = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_FEES_HEADER + " WHERE sys_id = " + headerId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
//                model = new FeesHeaderModel();
//
//                model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
//                model.setSys_id(cursor.getInt(cursor.getColumnIndex(SYS_ID)));
//                model.setStudentId(cursor.getInt(cursor.getColumnIndex(STUDENT_ID)));
//                model.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
//                model.setSchoolYearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
//                model.setAcademicSession_id(cursor.getInt(cursor.getColumnIndex(ACADEMIC_SESSION_ID)));
//                model.setDeviceId(cursor.getString(cursor.getColumnIndex(ID)));
//                model.setFor_date(cursor.getString(cursor.getColumnIndex(FOR_DATE)));
//                model.setTotal_amount(cursor.getInt(cursor.getColumnIndex(TOTAL_AMOUNT)));
//                model.setTransactionType_id(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
//                model.setCategory_id(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
//                model.setReceiptNumber(cursor.getInt(cursor.getColumnIndex(RECEIPT_NO)));
//                model.setCashDepositId(cursor.getString(cursor.getColumnIndex(CASH_DEPOSIT_ID)));
//                model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
//                model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
//                model.setUploadedOn(SurveyAppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public int FindCashDepositRecord(int Id,int schoolId) throws Exception {
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + CashDeposit.CASH_DEPOSIT_TABLE + " WHERE sys_id = " + Id + " AND school_id = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public void insertDownloadedFeesDetails(SQLiteDatabase db, long feesHeader_id, ArrayList<FeesDetailUploadModel> list) throws Exception {
        try {
            for (FeesDetailUploadModel model : list) {
                ContentValues cv = new ContentValues();
                cv.put(fee_amount, model.getAmount());
                cv.put(fees_type_id, model.getFeeType_id());
                cv.put(fees_header_id, (int) feesHeader_id);

                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public long addDownloadedFeesHeader(SyncFeesHeaderModel model) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            if (model.getCashDeposit_id() != null && !model.getCashDeposit_id().isEmpty()) {
                cv.put(CASH_DEPOSIT_ID, model.getCashDeposit_id());
                cv.put(CASH_DEPOSIT_ID_TYPE, "S");
            }

            if (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty()) {
                if (Long.parseLong(model.getReceiptNo()) > -1) {
                    cv.put(RECEIPT_NO, model.getReceiptNo());
                }
            }

            if (model.getForDate() != null) {
                model.setForDate(AppModel.getInstance().convertDatetoFormat(model.getForDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a"));
                cv.put(FOR_DATE, model.getForDate());
            }

            if (model.getCreatedOn() != null) {
                String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

                model.setCreatedOn(createdOn);
                cv.put(CREATED_ON, model.getCreatedOn());

            }

            if (model.getCreatedOn_Server() != null) {
                String createdOnServer = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn_Server(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

                model.setCreatedOn_Server(createdOnServer);
                cv.put(CREATED_ON_SERVER, model.getCreatedOn_Server());

            }
            if (model.getModified_on() != null) {
//                String modifiedOn = AppModel.getInstance().convertDatetoFormat(model.getModified_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

//                model.setModified_on(modifiedOn);
                cv.put(MODIFIED_ON, model.getModified_on());

            }

            cv.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

            String downloadedOn = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");
            model.setDownloadedOn(downloadedOn);
            cv.put(DOWNLOADED_ON, downloadedOn);

            cv.put(SYS_ID, model.getId());
//            cv.put(UPLOADED_ON, model.getUploadedOn());
            cv.put(CREATED_BY, model.getCreatedBy());
            cv.put(STUDENT_ID, model.getStudentId());
            cv.put(SCHOOL_YEAR_ID, model.getAcademicSession_id());
            cv.put(SCHOOL_CLASS_ID, model.getSchoolClassId());
            cv.put(SCHOOL_ACADEMIC_SESSION_ID, model.getAcademicSession_id());
            cv.put(RECEIPT_NO, model.getReceiptNo());
            cv.put(TOTAL_AMOUNT, model.getTotalAmount());
            cv.put(CATEGORY_ID, model.getCategory_id());
            cv.put(TRANSACTION_TYPE_ID, model.getTransactionType_id());

            id = db.insert(TABLE_FEES_HEADER, null, cv);
            return id;

        } catch (Exception e) {
            throw e;
        }
    }

    public long insertDownloadedFeesDetails(long feesHeader_id, FeesDetailUploadModel model) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(fee_amount, model.getAmount());
            cv.put(fees_type_id, model.getFeeType_id());
            cv.put(fees_header_id, (int) feesHeader_id);

            id = db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return id;
    }

    public long updateDownloadedFeesDetails(long feesHeader_id, FeesDetailUploadModel model) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        int id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(fee_amount, model.getAmount());
            cv.put(fees_type_id, model.getFeeType_id());
            cv.put(fees_header_id, (int) feesHeader_id);
            id = db.update(TABLE_FEES_DETAIL, cv, fees_header_id + " = " + feesHeader_id + " AND " + fees_type_id + " = " + model.getFeeType_id(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public boolean FindFeesDetailRecord(int headerId, int feeType_id) throws Exception {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_FEES_DETAIL
                    + " WHERE " + fees_header_id + " = " + headerId + " AND " + fees_type_id + " = " + feeType_id;
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public boolean IfFeesHeaderNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_FEES_HEADER
                    + " WHERE " + SYS_ID + " = " + id
                    + " AND " + UPLOADED_ON + " IS NULL"
                    + " OR " + UPLOADED_ON + " =''";

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

    public void updateFeesHeader(SyncFeesHeaderModel fhm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            if (fhm.getForDate() != null) {
                fhm.setForDate(AppModel.getInstance().convertDatetoFormat(fhm.getForDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a"));
                values.put(FOR_DATE, fhm.getForDate());
            }

            if (fhm.getCreatedOn() != null) {
                fhm.setCreatedOn(AppModel.getInstance().convertDatetoFormat(fhm.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a"));
                values.put(CREATED_ON, fhm.getCreatedOn());
            }

            values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
            values.put(CREATED_BY, fhm.getCreatedBy());
            values.put(SCHOOL_CLASS_ID, fhm.getSchoolClassId());
            values.put(SYS_ID, fhm.getId());
            values.put(STUDENT_ID, fhm.getStudentId());
//            values.put(SCHOOL_YEAR_ID, fhm.);
            values.put(RECEIPT_NO, fhm.getReceiptNo());
            values.put(ACADEMIC_SESSION_ID, fhm.getAcademicSession_id());
            values.put(TOTAL_AMOUNT, fhm.getTotalAmount());
            values.put(TRANSACTION_TYPE_ID, fhm.getTransactionType_id());
            values.put(CATEGORY_ID, fhm.getCategory_id());
            values.put(CASH_DEPOSIT_ID, fhm.getCashDeposit_id());

            long i = DB.update(TABLE_FEES_HEADER, values, SYS_ID + " =" + fhm.getId(), null);


        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendLog(context, "In Fees Header Update method. Exception occurred: " + e.getMessage());
        }
    }

    public List<AccountStatementModelNewStructure> getAccountStatement(int schoolId, String grNo, String fromDate, String toDate, String depSlipNo, String receiptNo) {
        List<AccountStatementModelNewStructure> asmList = new ArrayList<>();
        Cursor cursor = null;
        String q = "";
//        if (grNo != null && !grNo.isEmpty()) {

        q = "select fh.*, fd.fee_amount, fd.feeType_Id,ft.id as 'newFeeTypeId',\n" +
                "s.student_name as student_name,s.student_gr_no as student_grNo,s.father_name as father_name,\n" +
                "c.name as className,sec.name as sectionName,\n" +
                "cd.deposit_slip_no as deposit_slip_no, cd.created_on as deposit_date \n" +
                "from FeesHeader fh \n" +
                "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                "  INNER JOIN school_class sc on sc.id = fh.schoolclass_id\n" +
                "\tLEFT OUTER JOIN CashDeposit cd on fh.cashdeposit_id = cd.id\n" +
                "\tinner join class c on c.id = sc.class_id\n" +
                "\tinner join section sec on sec.id = sc.section_id\n" +
                "\tinner join student s on s.id = fh.student_id\n" +
                "\tcross join FeeType ft ";

//        } else {
//            q = "select fh.*, fd.fee_amount, fd.feeType_Id,\n" +
//                    "s.student_name as student_name,s.student_gr_no as student_grNo,s.father_name as father_name,\n" +
//                    "c.name as className,sec.name as sectionName,\n" +
//                    "cd.deposit_slip_no as deposit_slip_no, cd.created_on as deposit_date \n" +
//                    "from FeesHeader fh \n" +
//                    "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
//                    "    INNER JOIN school_class sc on sc.id = fh.schoolclass_id\n" +
//                    "\tLEFT OUTER JOIN CashDeposit cd on fh.cashdeposit_id = cd.id\n" +
//                    "\tinner join class c on c.id = sc.class_id\n" +
//                    "\tinner join section sec on sec.id = sc.section_id\n" +
//                    "\tinner join student s on s.id = fh.student_id \n";
//
//        }


        q = q + " where sc.school_ID = @SchoolID ";
        q = q.replace("@SchoolID", String.valueOf(schoolId));

        if ((fromDate != null && toDate != null) && (!fromDate.isEmpty() && !toDate.isEmpty())) {

            q = q + " and fh.created_on between '@FromDate 00:00:00 AM' and '@ToDate 23:59:59 PM'";

            q = q.replace("@FromDate", fromDate);
            q = q.replace("@ToDate", toDate);
        }

        if (grNo != null && !grNo.isEmpty()) {

            q += " and s.student_gr_no = @StudentGrNo ";
            q = q.replace("@StudentGrNo", grNo);
        }

        if (!depSlipNo.isEmpty()) {

            q = q + " and cd.deposit_slip_no = @DepSlipNo";
            q = q.replace("@DepSlipNo", depSlipNo);
        }
        if (!receiptNo.isEmpty()) {

            q = q + " and fh.receipt_no = @RNo";
            q = q.replace("@RNo", receiptNo);
        }

        q += " order by fh.created_on asc, fh.student_id, fd.feesHeader_id,feeType_id";

        Log.d("Account statement Query", q);
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(q, null);

            if (cursor.moveToFirst()) {
                AccountStatementModelNewStructure model = new AccountStatementModelNewStructure();
                do {
                    try {
                        int headerId = cursor.getInt(cursor.getColumnIndex(ID));
                        if (grNo != null && !grNo.isEmpty()) {
                            model.setStudentName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_NAME)));
                            model.setFatherName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_FATHERS_NAME)));
                            model.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                            model.setSection(cursor.getString(cursor.getColumnIndex("sectionName")));
                        }
                        model.setId(headerId);
                        model.setSysId(cursor.getInt(cursor.getColumnIndex(SYS_ID)));
                        model.setSchoolclass_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
//                        model.setSchool_year_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_YEAR)));
                        model.setStudent_id(cursor.getInt(cursor.getColumnIndex(STUDENT_ID)));
                        model.setGrNo(cursor.getString(cursor.getColumnIndex("student_grNo")));
//                        model.setTransaction_type(cursor.getString(cursor.getColumnIndex("transaction_type")));
                        model.setTransaction_categoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));

//                        if (model.getTransaction_categoryId() == 1){  //Invoice
//
//                        }else if (model.getTransaction_categoryId() == 2){  //Receipt
//
//                        }
//                        model.setCreated_from(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_FROM)));
                        model.setTransaction_typeId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
                        model.setCreated_on(AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex(CREATED_ON)), "yyyy-MM-dd hh:mm:ss a", "dd-MM-yy"));
                        model.setReceiptNo(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                        model.setDeposit_slip_no(cursor.getInt(cursor.getColumnIndex("deposit_slip_no")));
                        model.setDeposit_date(AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex("deposit_date")), "yyyy-MM-dd hh:mm:ss a", "dd-MM-yy"));

                        int feeType_id = cursor.getInt(cursor.getColumnIndex(FEE_TYPE_ID));
                        double amount = cursor.getDouble(cursor.getColumnIndex(fee_amount));
                        int newFeeTypeId = cursor.getInt(cursor.getColumnIndex("newFeeTypeId"));

                        if (feeType_id == newFeeTypeId && feeType_id == 1) {
                            model.setFees_admission(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 2) {
                            model.setFees_exam(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 3) {
                            model.setFees_tution(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 4) {
                            model.setFees_books(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 5) {
                            model.setFees_copies(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 6) {
                            model.setFees_uniform(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 7) {
                            model.setFees_others(amount);
                        }

                        if (newFeeTypeId == 8) {
                            asmList.add(model);
                            model = new AccountStatementModelNewStructure();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        return asmList;

    }


    /*public List<AccountStatementModelNewStructure> getReceivableStatement(int schoolId, String grNo, String fromDate, String toDate) {
        List<AccountStatementModelNewStructure> asmList = new ArrayList<>();
        Cursor cursor = null;
        String q = "";
//        if (grNo != null && !grNo.isEmpty()) {

        q = "select fh.*, fd.fee_amount, fd.feeType_Id,ft.id as 'newFeeTypeId',\n" +
                "s.student_name as student_name,s.student_gr_no as student_grNo,s.father_name as father_name,\n" +
                "c.name as className,sec.name as sectionName,\n" +
                "cd.deposit_slip_no as deposit_slip_no, cd.created_on as deposit_date \n" +
                "from FeesHeader fh \n" +
                "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                "  INNER JOIN school_class sc on sc.id = fh.schoolclass_id\n" +
                "\tLEFT OUTER JOIN CashDeposit cd on fh.cashdeposit_id = cd.id\n" +
                "\tinner join class c on c.id = sc.class_id\n" +
                "\tinner join section sec on sec.id = sc.section_id\n" +
                "\tinner join student s on s.id = fh.student_id\n" +
                "\tcross join FeeType ft ";

//        } else {
//            q = "select fh.*, fd.fee_amount, fd.feeType_Id,\n" +
//                    "s.student_name as student_name,s.student_gr_no as student_grNo,s.father_name as father_name,\n" +
//                    "c.name as className,sec.name as sectionName,\n" +
//                    "cd.deposit_slip_no as deposit_slip_no, cd.created_on as deposit_date \n" +
//                    "from FeesHeader fh \n" +
//                    "\tINNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
//                    "    INNER JOIN school_class sc on sc.id = fh.schoolclass_id\n" +
//                    "\tLEFT OUTER JOIN CashDeposit cd on fh.cashdeposit_id = cd.id\n" +
//                    "\tinner join class c on c.id = sc.class_id\n" +
//                    "\tinner join section sec on sec.id = sc.section_id\n" +
//                    "\tinner join student s on s.id = fh.student_id \n";
//
//        }


        q = q + " where sc.school_ID = @SchoolID ";
        q = q.replace("@SchoolID", String.valueOf(schoolId));

        if ((fromDate != null && toDate != null) && (!fromDate.isEmpty() && !toDate.isEmpty())) {

            q = q + " and fh.created_on between '@FromDate 00:00:00 AM' and '@ToDate 23:59:59 PM'";

            q = q.replace("@FromDate", fromDate);
            q = q.replace("@ToDate", toDate);
        }

        if (grNo != null && !grNo.isEmpty()) {

            q += " and s.student_gr_no = @StudentGrNo ";
            q = q.replace("@StudentGrNo", grNo);
        }

        if (!depSlipNo.isEmpty()) {

            q = q + " and cd.deposit_slip_no = @DepSlipNo";
            q = q.replace("@DepSlipNo", depSlipNo);
        }
        if (!receiptNo.isEmpty()) {

            q = q + " and fh.receipt_no = @RNo";
            q = q.replace("@RNo", receiptNo);
        }

        q += " order by fh.created_on asc, fh.student_id, fd.feesHeader_id,feeType_id";

        Log.d("Account statement Query", q);
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(q, null);

            if (cursor.moveToFirst()) {
                AccountStatementModelNewStructure model = new AccountStatementModelNewStructure();
                do {
                    try {
                        int headerId = cursor.getInt(cursor.getColumnIndex(ID));
                        if (grNo != null && !grNo.isEmpty()) {
                            model.setStudentName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_NAME)));
                            model.setFatherName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_FATHERS_NAME)));
                            model.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                            model.setSection(cursor.getString(cursor.getColumnIndex("sectionName")));
                        }
                        model.setId(headerId);
                        model.setSysId(cursor.getInt(cursor.getColumnIndex(SYS_ID)));
                        model.setSchoolclass_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
//                        model.setSchool_year_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_YEAR)));
                        model.setStudent_id(cursor.getInt(cursor.getColumnIndex(STUDENT_ID)));
                        model.setGrNo(cursor.getString(cursor.getColumnIndex("student_grNo")));
//                        model.setTransaction_type(cursor.getString(cursor.getColumnIndex("transaction_type")));
                        model.setTransaction_categoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));

//                        if (model.getTransaction_categoryId() == 1){  //Invoice
//
//                        }else if (model.getTransaction_categoryId() == 2){  //Receipt
//
//                        }
//                        model.setCreated_from(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_FROM)));
                        model.setTransaction_typeId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
                        model.setCreated_on(AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex(CREATED_ON)), "yyyy-MM-dd hh:mm:ss a", "dd-MM-yy"));
                        model.setReceiptNo(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                        model.setDeposit_slip_no(cursor.getInt(cursor.getColumnIndex("deposit_slip_no")));
                        model.setDeposit_date(AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex("deposit_date")), "yyyy-MM-dd hh:mm:ss a", "dd-MM-yy"));

                        int feeType_id = cursor.getInt(cursor.getColumnIndex(FEE_TYPE_ID));
                        double amount = cursor.getDouble(cursor.getColumnIndex(fee_amount));
                        int newFeeTypeId = cursor.getInt(cursor.getColumnIndex("newFeeTypeId"));

                        if (feeType_id == newFeeTypeId && feeType_id == 1) {
                            model.setFees_admission(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 2) {
                            model.setFees_exam(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 3) {
                            model.setFees_tution(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 4) {
                            model.setFees_books(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 5) {
                            model.setFees_copies(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 6) {
                            model.setFees_uniform(amount);
                        } else if (feeType_id == newFeeTypeId && feeType_id == 7) {
                            model.setFees_others(amount);
                        }

                        if (newFeeTypeId == 8) {
                            asmList.add(model);
                            model = new AccountStatementModelNewStructure();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        return asmList;

    }*/

    public List<PreviousReceivableModel> getLast30DaysForAverageFeesCollection(int schoolId) {
        List<PreviousReceivableModel> prevRecv = new ArrayList<>();
        String query;
        if (schoolId > 0) {

            query = GET_RECEIVABLES_RECEIVED;
            query = query + " where fh.created_on  >= datetime('now','-1 month')";
            query = query.replace("@SchoolId", "" + schoolId);
        } else {
            query = GET_RECEIVABLES_RECEIVED_FOR_SCHOOLS;
            query = query + " where fh.created_on  >= datetime('now','-1 month')";
        }
        query = query + " GROUP BY fd.feeType_id";
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    PreviousReceivableModel prm = new PreviousReceivableModel();
                    prm.setFeeType_id(c.getInt(c.getColumnIndex(fees_type_id)));
                    prm.setTotalAmount(c.getString(c.getColumnIndex("totalAmount")));
                    prevRecv.add(prm);
                } while (c.moveToNext());

            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add(new PreviousReceivableModel(1, ""));
                prevRecv.add(new PreviousReceivableModel(2, ""));
                prevRecv.add(new PreviousReceivableModel(3, ""));
                prevRecv.add(new PreviousReceivableModel(4, ""));
                prevRecv.add(new PreviousReceivableModel(5, ""));
                prevRecv.add(new PreviousReceivableModel(6, ""));
                prevRecv.add(new PreviousReceivableModel(7, ""));
            }

            return prevRecv;

        } catch (Exception e) {
            e.printStackTrace();
            return prevRecv;
        }
//        finally {
//            c.close();
//        }
    }

    public int getUnuploadedCountFeeEntry(String allUserSchoolsForFinance) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        String query = "SELECT count(s.uploaded_on) as count FROM student s \n" +
                "INNER JOIN school_class sc on sc.id = s.schoolclass_id\n" +
                "INNER JOIN FeesHeader fh on fh.student_id = s.id\n" +
                "WHERE sc.school_id IN (@schools) AND sc.is_active = 1 AND s.is_active = 1 \n" +
                "AND (s.Actual_Fees >= 10 OR s.Actual_Fees <= 1500) \n" +
                "AND (s.uploaded_on IS NULL OR s.uploaded_on = '')\n" +
                "AND (fh.uploaded_on IS NULL OR fh.uploaded_on = '')\n" +
                "AND (fh.Category_Id = 3)";

        query = query.replace("@schools", allUserSchoolsForFinance);

        try {
            c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                return c.getInt(c.getColumnIndex("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    public ArrayList<ClassSectionModel> getClassSectionForFeeEntryBySchoolId(String SchoolID) {
        ArrayList<ClassSectionModel> csmList = new ArrayList<ClassSectionModel>();
        Cursor cursor = null;
        try {

//            String selectQuery = "select distinct sc.id as schoolclass_id,c.id as class_id,c.name as class_name, s.id as section_id,s.name as section_name,st.Actual_Fees\n" +
//                    "from school_class sc inner join class c on sc.class_id = c.id \n" +
//                    "inner join section s on sc.section_id = s.id\n" +
//                    "inner join student st on st.schoolclass_id = sc.id\n" +
//                    "where sc.school_id IN (@SchoolID) and sc.is_active = 1 and st.is_active = 1 and st.Actual_Fees < 10 OR st.Actual_Fees > 1500 OR st.Actual_Fees IS NULL order by c.id asc";
            String selectQuery = "select distinct sc.id as schoolclass_id,c.id as class_id,c.name as class_name, s.id as section_id,s.name as section_name\n" +
//                    ",(Select  ifnull(fd.fee_amount,0) as A from FeesHeader fh inner join FeesDetail fd on fd.feesHeader_id = fh.id where fh.schoolclass_id = sc.id and fh.Category_Id = 3 and fh.TransactionType_Id = 1) openingAmount\n" +
                    ",(Select  ifnull(fd.fee_amount,0) as A from FeesHeader fh inner join FeesDetail fd on fd.feesHeader_id = fh.id where fh.student_id = st.server_id and fh.Category_Id = 3 and fh.TransactionType_Id = 1) openingAmount\n" +
                    "from school_class sc inner join class c on sc.class_id = c.id \n" +
                    "inner join section s on sc.section_id = s.id\n" +
                    "inner join student st on st.schoolclass_id = sc.id\n" +
                    "where sc.school_id IN (@SchoolID) and sc.is_active = 1 and st.is_active = 1 \n" +
                    "and (st.Actual_Fees < 10 OR st.Actual_Fees > 1500 OR st.Actual_Fees IS NULL OR openingAmount IS NULL) \n" +
                    "order by c.id asc";


            selectQuery = selectQuery.replace("@SchoolID", SchoolID);

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ClassSectionModel sm = new ClassSectionModel();
                    sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
                    sm.setClassId(cursor.getInt(cursor.getColumnIndex("class_id")));
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex("section_id")));

                    sm.setClass_section_name(cursor.getString(cursor.getColumnIndex("class_name")) + " " + cursor.getString(cursor.getColumnIndex("section_name")));

                    csmList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return csmList;
    }

    public void updateStudentsMonthlyFees(List<StudentModel> student) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            for (StudentModel sm : student) {
                ContentValues values = new ContentValues();

                values.put(STUDENT_Actual_Fees, sm.getMonthlyfee());
                if (sm.getActualFees() < 10) {
                    values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, (String) null);
                }

                DB.update(TABLE_STUDENT, values, ID + " =" + String.valueOf(sm.getId()), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public double getReceivables(String schoolid, int flag) {
        double value = 0.0;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String query = "";

        if (flag == 0) { //Receivables
            query = "SELECT SUM(\n" +
                    "\tCASE WHEN fh.Category_Id = 1 THEN fd.fee_amount \n" +
                    "\tELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END \n" +
                    "\tEND \n" +
                    ") AS totalAmount\n" +
                    "FROM FeesHeader fh \n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)";
        } else if (flag == 1) { //Received
//            query = "SELECT SUM(fd.fee_amount) AS totalAmount\n" +
//                    "FROM FeesHeader fh \n" +
//                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
//                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id \n" +
//                    "WHERE fh.Category_Id = 2 OR fh.Category_Id = 4 AND sc.school_ID IN (@SchoolID)";
            query = "SELECT SUM(fd.fee_amount) AS totalAmount\n" +
                    "FROM FeesHeader fh \n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id \n" +
                    "WHERE fh.Category_Id IN (2,4) AND sc.school_ID IN (@SchoolID)";
        }

        query = query.replace("@SchoolID", schoolid);

        try {
            cursor = DB.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                value = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return value;
    }

    public String[] getFeesCount(String schoolId) {
        String[] array = new String[5];

        String getStudentCount = "SELECT COUNT(*) as studentCount FROM student WHERE schoolclass_id IN(SELECT id from school_class where school_id IN(@school_ids))";

        String getSchoolClassCount = "SELECT COUNT(*) as schoolClassCount FROM school_class WHERE school_id IN(@school_ids)";

        String getCashDepositCount = "SELECT COUNT(*) as cashDepositCount FROM CashDeposit WHERE school_id IN(@school_ids)";

        String getHeaderCount = "SELECT COUNT(*) as headerCount FROM FeesHeader WHERE schoolclass_id IN(SELECT id from school_class where school_id IN(@school_ids))";

        String getDetailCount = "SELECT COUNT(*) as detailCount FROM FeesDetail fd INNER JOIN FeesHeader fh ON fh.id = fd.feesHeader_id\n" +
                "WHERE fh.schoolclass_id IN(SELECT id from school_class where school_id IN(@school_ids))";

        getStudentCount = getStudentCount.replace("@school_ids", schoolId);
        getSchoolClassCount = getSchoolClassCount.replace("@school_ids", schoolId);
        getHeaderCount = getHeaderCount.replace("@school_ids", schoolId);
        getDetailCount = getDetailCount.replace("@school_ids", schoolId);
        getCashDepositCount = getCashDepositCount.replace("@school_ids", schoolId);

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(getStudentCount, null);
            if (cursor.moveToFirst())
                array[0] = String.valueOf(cursor.getInt(cursor.getColumnIndex("studentCount")));
            cursor = db.rawQuery(getSchoolClassCount, null);
            if (cursor.moveToFirst())
                array[1] = String.valueOf(cursor.getInt(cursor.getColumnIndex("schoolClassCount")));

            cursor = db.rawQuery(getHeaderCount, null);
            if (cursor.moveToFirst())
                array[2] = String.valueOf(cursor.getInt(cursor.getColumnIndex("headerCount")));
            cursor = db.rawQuery(getDetailCount, null);
            if (cursor.moveToFirst())
                array[3] = String.valueOf(cursor.getInt(cursor.getColumnIndex("detailCount")));
            cursor = db.rawQuery(getCashDepositCount, null);
            if (cursor.moveToFirst())
                array[4] = String.valueOf(cursor.getInt(cursor.getColumnIndex("cashDepositCount")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return array;
    }

    public CheckSumModel getCheckSumCount(int schoolId) throws Exception {
        CheckSumModel model = new CheckSumModel();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String query;
        try {
            {
                query = "SELECT COUNT(*) as header FROM FeesHeader WHERE (sys_id NOTNULL AND sys_id != '' AND sys_id != 0) AND schoolclass_id IN(SELECT id from school_class where school_id IN(@schoolid))";
                query = query.replace("@schoolid", String.valueOf(schoolId));
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.moveToFirst())
                    model.setFeesHeaderCount(cursor.getInt(cursor.getColumnIndex("header")));

                if (cursor != null)
                    cursor.close();
            }
//            {
//                query = "SELECT COUNT(*) as detail FROM FeesDetail WHERE feesHeader_id IN(SELECT id FROM FeesHeader WHERE schoolclass_id IN(SELECT id from school_class where school_id IN(@schoolid)))";
//                query = query.replace("@schoolid", String.valueOf(schoolId));
//                cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.moveToFirst())
//                    model.setFeesDetailCount(cursor.getInt(cursor.getColumnIndex("detail")));
//
//                if (cursor != null)
//                    cursor.close();
//            }
            {
                query = "SELECT COUNT(*) as deposits FROM CashDeposit WHERE (sys_id NOTNULL AND sys_id != '' AND sys_id != 0) AND school_id IN(@schoolid)";
                query = query.replace("@schoolid", String.valueOf(schoolId));
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.moveToFirst())
                    model.setFeesDepositCount(cursor.getInt(cursor.getColumnIndex("deposits")));
            }

            int feesDetailCount = DatabaseHelper.getInstance(context).getFeesDetailCountForChecksum(schoolId);
            if (feesDetailCount > 0)
                model.setFeesDetailCount(feesDetailCount);

            int studentCount = DatabaseHelper.getInstance(context).getStudentCountForChecksum(schoolId);
            if (studentCount > 0)
                model.setStudentCount(studentCount);

            int schoolClassCount = DatabaseHelper.getInstance(context).getSchoolClassCountForChecksum(schoolId);
            if (schoolClassCount > 0)
                model.setSchoolClassCount(schoolClassCount);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return model;
    }

    public int getNoOfMonthlyFeesRecords(int schoolid,int academic_session_id) {
        String selectQuery = "SELECT IFNULL( SUM(fd.fee_amount),0) AS totalFDAmount FROM FeesDetail fd" +
                " INNER JOIN FeesHeader fh on fh.id = fd.feesHeader_id" +
                " INNER JOIN school_class sc on sc.id = fh.schoolclass_id" +
                " WHERE fh.academic_session_id = @ACSID AND fd.feeType_id = 3 AND fh.Category_Id = 2" +
                " AND sc.school_id = @SchoolID";

//        String selectQuery = "SELECT COUNT(fd.id) as 'count_fee_detail' FROM FeesDetail fd \n" +
//                "INNER JOIN FeesHeader fh on fh.id = fd.feesHeader_id\n" +
//                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id\n" +
//                "INNER JOIN student s on s.id = fh.student_id\n" +
//                "WHERE fd.feeType_id = 3 AND s.is_active = 1\n" +
//                "AND sc.school_id IN(@SchoolID)";

        selectQuery = selectQuery.replace("@SchoolID", schoolid + "");
        selectQuery = selectQuery.replace("@ACSID", academic_session_id + "");
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        int amount = 0;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                amount = cursor.getInt(cursor.getColumnIndex("totalFDAmount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return amount;
    }


    public void deleteSysIDRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM FeesHeader WHERE (sys_id != '' OR sys_id NOTNULL) AND schoolclass_id IN(SELECT id from school_class where school_id IN(" + schoolId + "))";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ID));
                    db.delete(TABLE_FEES_HEADER, ID + " = " + id, null);
                    db.delete(TABLE_FEES_DETAIL, fees_header_id + " = " + id, null);
                } while (cursor.moveToNext());
            }

            AppModel.getInstance().appendErrorLog(context, "Fees Header and Fees Detail Data Flushed Successfully. School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing Fees Header and Fees Detail data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }

    }

    public void deleteFeeDetailsRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT fd.id FROM FeesDetail fd" +
                " INNER JOIN FeesHeader fh ON fh.id = fd.feesHeader_id" +
                " WHERE (fh.sys_id != '' OR fh.sys_id NOTNULL) AND fh.schoolclass_id IN(SELECT id from school_class where school_id IN(" + schoolId + "))";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ID));
                    db.delete(TABLE_FEES_DETAIL, ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }

    }

    public void updateTableColumnsBulk(ArrayList<GeneralUploadResponseModel> body, SyncDownloadUploadModel syncDownloadUploadModel) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int uploadedCount = 0;
            for (GeneralUploadResponseModel model : body) {
                if (model.server_id > 0) {
                    DataSync.isFinanceSyncSuccessful = true;
                    ContentValues values = new ContentValues();
                    values.put(FeesCollection.UPLOADED_ON, AppModel.getInstance().getDateTime());
                    values.put(FeesCollection.SYS_ID, model.server_id);
                    long i = DB.update(TABLE_FEES_HEADER, values, ID + " = " + model.device_id, null);
                    if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Fees Header Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());

                        uploadedCount++;
                    }

                } else if (model.server_id == -2) {       //For duplicate record removal // Also used for multiple device issue error
                    DataSync.isFinanceSyncSuccessful = true;
                    deleteTableFeeHeaderAndDetailRows(model);
                } else if (model.server_id == -1) {
                    DataSync.isFinanceSyncSuccessful = false;
                    String errorMessage = model.ErrorMessage != null && !model.ErrorMessage.isEmpty() ? model.ErrorMessage : "";
                    AppModel.getInstance().appendErrorLog(context, "After Upload Fees Header sys_id=" + model.server_id +
                            " for device id=" + model.device_id
                            + " Error message:" + errorMessage);
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

    private void deleteTableFeeHeaderAndDetailRows(GeneralUploadResponseModel model) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            long i = DB.delete(TABLE_FEES_HEADER, ID + " = " + model.device_id, null);
            if (i > 0) {
                AppModel.getInstance().appendLog(context, "Fees Header Deleted. Id:" + model.device_id +
                        " and deletedOn:" + AppModel.getInstance().getDate() + " Message: " + model.ErrorMessage);

                AppModel.getInstance().appendErrorLog(context, "Fees Header Deleted. Id:" + model.device_id +
                        " and deletedOn:" + AppModel.getInstance().getDate() + " Message: " + model.ErrorMessage);

                long j = DB.delete(TABLE_FEES_DETAIL, fees_header_id + " = " + model.device_id, null);
                if (j > 0) {
                    AppModel.getInstance().appendLog(context, "Fees Detail Deleted. Id:" + model.device_id + " and deletedOn:" + AppModel.getInstance().getDate());
                    AppModel.getInstance().appendErrorLog(context, "Fees Detail Deleted. Id:" + model.device_id + " and deletedOn:" + AppModel.getInstance().getDate());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<DashboardReceivableModel> getReceivableForDashboard(String schoolid, String filterReceivableBy) {

        //Receivables
        List<DashboardReceivableModel> receivableModelList = new ArrayList<>();
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String query = "";

        query = "select ftype.id as feeType_id,ftype.feetype_name, ifnull(rcvble.totalreceivable,0)totalReceivable ,ifnull(rcvd.totalreceived,0)totalReceived @OPENING from feetype ftype\n" +
                "left join\n" +
                "--TOTAL RCVBLE fro active students\n" +
                "(\n" +
                "SELECT\n" +
                "    IFNULL(SUM(totalReceivable),0) AS totalReceivable,\n" +
                "    feeType_id\n" +
                "from (\n" +
                "SELECT\n" +
                "    IFNULL(SUM(fdd.fee_amount),0) AS totalReceivable,\n" +
                "    fdd.feeType_id FROM\n" +
                " (SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                "FROM FeesHeader fh\n" +
                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)  WHERE \n" +
                "fh.Category_Id in (1) @FORDATE\n" +
                ") fdd GROUP BY fdd.feeType_id)\n" +
                "rcvbl1\n" +
                "GROUP BY feeType_id\n" +
                ") Rcvble  on rcvble.feetype_id = ftype.id\n" +
                "left join\n" +
                "(--TOTAL RCVD FRO all students\n" +
                "SELECT IFNULL(SUM(fdd2.fee_amount),0) AS totalReceived,\n" +
                "fdd2.feeType_id FROM\n" +
                " (SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                "FROM FeesHeader fh\n" +
                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)  WHERE\n" +
                "fh.Category_Id in (2) @FORDATE\n" +
                ") fdd2 GROUP BY fdd2.feeType_id) rcvd\n" +
                "on ftype.id = rcvd.feetype_id\n";

        if (filterReceivableBy.equals("m")) {
            query = query.replaceAll("@FORDATE"," AND STRFTIME('%Y-%m',replace(replace(upper(fh.for_date),'AM',''),'PM','')) = STRFTIME('%Y-%m',date('now','start of month'))");
            query = query.replaceAll("@OPENING","");
        } else if (filterReceivableBy.equals("t")) {
            query = query.replaceAll("@FORDATE"," AND STRFTIME('%Y-%m-%d',replace(replace(upper(fh.for_date),'AM',''),'PM','')) = STRFTIME('%Y-%m-%d','now')");
            query = query.replaceAll("@OPENING","");
        }else if (filterReceivableBy.equals("s")){
            query +="LEFT JOIN\n" +
                    "(--TOTAL Opening FOR all students\n" +
                    "SELECT IFNULL(SUM(fdd3.fee_amount),0) AS totalReceivable,\n" +
                    "3 feeType_id FROM\n" +
                    " (SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)  WHERE \n" +
                    "fh.Category_Id in (3) @FORDATE ) fdd3 \n" +
                    ") opening\n" +
                    "on ftype.id = opening.feetype_id\n";

            query = query.replaceAll("@FORDATE","");
            query = query.replaceAll("@OPENING",",ifnull(opening.totalreceivable,0)totalOpening");
        }else if (filterReceivableBy.equals("c")){
            query = "select ftype.id as feeType_id,ftype.feetype_name,\n" +
                    "ifnull(rcvbleActive.totalreceivable,0) activeRcvble\n" +
                    ",ifnull(rcvdActive.totalreceived,0) activeRecvd,\n" +
                    "ifnull(openingActive.totalreceivable,0) activeOpening,\n" +
                    "ifnull(rcvbleInactive.totalreceivable,0) inactiveRcvble\n" +
                    ",ifnull(rcvdInactive.totalreceived,0) inactiveRecvd,\n" +
                    "ifnull(openingInactive.totalreceivable,0) inactiveOpening\n" +
                    ",(ifnull(openingActive.totalreceivable,0) + ifnull(rcvbleActive.totalreceivable,0) - ifnull(rcvdActive.totalreceived,0)) AS closingActive\n" +
                    ",(ifnull(openingInactive.totalreceivable,0) + ifnull(rcvbleInactive.totalreceivable,0) - ifnull(rcvdInactive.totalReceived,0)) AS closingInactive\n" +
                    "from feetype ftype\n" +
                    "\n" +
                    "left join\n" +
                    "--TOTAL RCVBLE fro active students\n" +
                    "(\n" +
                    "SELECT IFNULL(SUM(totalReceivable),0) AS totalReceivable, feeType_id\n" +
                    "from \n" +
                    "(\n" +
                    "SELECT IFNULL(SUM(fdd.fee_amount),0) AS totalReceivable, fdd.feeType_id \n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)\n" +
                    "WHERE fh.Category_Id in (1) AND s.is_active = 1\n" +
                    ") fdd\n" +
                    "GROUP BY fdd.feeType_id\n" +
                    ") rcvbl1\n" +
                    "GROUP BY feeType_id\n" +
                    ") rcvbleActive  on rcvbleActive.feetype_id = ftype.id\n" +
                    "left join\n" +
                    "(--TOTAL RCVD For active students\n" +
                    "SELECT IFNULL(SUM(fdd2.fee_amount),0) AS totalReceived, fdd2.feeType_id \n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)\n" +
                    "WHERE fh.Category_Id in (2) AND s.is_active = 1\n" +
                    ") fdd2\n" +
                    "GROUP BY fdd2.feeType_id\n" +
                    ") rcvdActive on ftype.id = rcvdActive.feetype_id\n" +
                    "LEFT JOIN\n" +
                    "(--TOTAL Opening FOR active students\n" +
                    "SELECT IFNULL(SUM(fdd3.fee_amount),0) AS totalReceivable, 3 feeType_id \n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)\n" +
                    "WHERE fh.Category_Id in (3)  AND s.is_active = 1\n" +
                    ") fdd3 \n" +
                    ") openingActive\n" +
                    "on ftype.id = openingActive.feetype_id\n" +
                    "\n" +
                    "\n" +
                    "left join\n" +
                    "--TOTAL RCVBLE fro inactive students\n" +
                    "(\n" +
                    "SELECT IFNULL(SUM(totalReceivable),0) AS totalReceivable, feeType_id\n" +
                    "from \n" +
                    "(\n" +
                    "SELECT IFNULL(SUM(fdd.fee_amount),0) AS totalReceivable, fdd.feeType_id \n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)\n" +
                    "WHERE fh.Category_Id in (1) AND s.is_active = 0\n" +
                    ") fdd\n" +
                    "GROUP BY fdd.feeType_id\n" +
                    ") rcvbl1\n" +
                    "GROUP BY feeType_id\n" +
                    ") rcvbleInactive  on rcvbleInactive.feetype_id = ftype.id\n" +
                    "left join\n" +
                    "(--TOTAL RCVD For inactive students\n" +
                    "SELECT IFNULL(SUM(fdd2.fee_amount),0) AS totalReceived, fdd2.feeType_id \n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)  \n" +
                    "WHERE fh.Category_Id in (2) AND s.is_active = 0\n" +
                    ") fdd2\n" +
                    "GROUP BY fdd2.feeType_id\n" +
                    ") rcvdInactive on ftype.id = rcvdInactive.feetype_id\n" +
                    "LEFT JOIN\n" +
                    "(--TOTAL Opening FOR inactive students\n" +
                    "SELECT IFNULL(SUM(fdd3.fee_amount),0) AS totalReceivable, 3 feeType_id \n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT DISTINCT fd.id,fd.fee_amount,fd.feeType_id,fd.feesHeader_id\n" +
                    "FROM FeesHeader fh\n" +
                    "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id\n" +
                    "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)\n" +
                    "INNER JOIN student s on (s.schoolclass_id =  sc.id and s.id =  fh.student_id)\n" +
                    "WHERE fh.Category_Id in (3)  AND s.is_active = 0\n" +
                    ") fdd3 \n" +
                    ") openingInactive\n" +
                    "on ftype.id = openingInactive.feetype_id\n";
        }

        //        query = "select ftype.id as feeType_id,ftype.feetype_name, ifnull(rcvble.totalreceivable,0)totalReceivable ,ifnull(rcvd.totalreceived,0)totalReceived from feetype ftype \n" +
//                "left join\n" +
//                "--TOTAL RCVBLE fro active students\n" +
//                "(SELECT \n" +
//                "\tIFNULL(SUM(fd.fee_amount),0) AS totalReceivable,\n" +
//                "\tfd.feeType_id  \n" +
//                "FROM FeesHeader fh   \n" +
//                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id  \n" +
//                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID) \n" +
//                "INNER JOIN student s on s.id =  fh.student_id  WHERE s.is_active = 1\n" +
//                "and fh.Category_Id in (1,3) @FORDATE \n" +
//                "GROUP BY fd.feeType_id\n" +
//                ") Rcvble  on rcvble.feetype_id = ftype.id\n" +
//                "left join \n" +
//                "(--TOTAL RCVD FRO all students\n" +
//                "SELECT IFNULL(SUM(fd.fee_amount),0) AS totalReceived,\n" +
//                "fd.feeType_id  \n" +
//                "FROM FeesHeader fh   \n" +
//                "INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id  \n" +
//                "INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID) \n" +
//                "INNER JOIN student s on s.id =  fh.student_id  WHERE \n" +
//                "fh.Category_Id in (2) @FORDATE \n" +
//                "GROUP BY fd.feeType_id) rcvd\n" +
//                "on ftype.id = rcvd.feetype_id";


//        query = "SELECT SUM(CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount " +
//                " ELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END " +
//                " END) AS totalReceivable,SUM(CASE WHEN fh.Category_Id = 2 THEN fd.fee_amount " +
//                " ELSE CASE WHEN fh.Category_Id = 4 THEN fd.fee_amount ELSE 0 END " +
//                " END) AS totalReceived,fd.feeType_id " +
//                " FROM FeesHeader fh  " +
//                " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id " +
//                " INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)" +
//                " INNER JOIN student s on s.id =  fh.student_id " +
//                " WHERE s.is_withdrawl != 1 AND (s.withdrawal_reason_id = 0 OR s.withdrawal_reason_id is null)";

//        query = "SELECT SUM(CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount" +
//                " ELSE CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount ELSE 0 END" +
//                " END) AS totalAmount,fd.feeType_id" +
//                " FROM FeesHeader fh " +
//                " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id" +
//                " INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)";

//        if (filterReceivableBy.equals("m")) {
//            query += " AND STRFTIME('%Y-%m',replace(replace(fh.for_date,'AM',''),'PM','')) = STRFTIME('%Y-%m',date('now','start of month'))";
//        } else if (filterReceivableBy.equals("t")) {
//            query += " AND STRFTIME('%Y-%m-%d',replace(replace(fh.for_date,'AM',''),'PM','')) = STRFTIME('%Y-%m-%d','now')";
//        }
//
//        query += " GROUP BY fd.feeType_id";
        query = query.replace("@SchoolID", schoolid);

        try {
            cursor = DB.rawQuery(query, null);
            double totalAmount = 0.0;
            if (cursor.moveToFirst()) {
                do {
                    int typeID = cursor.getInt(cursor.getColumnIndex(fees_type_id));

                    if (filterReceivableBy.equals("c")){
                        int addedClosing = 0;
                        if (typeID == 3 || typeID == 8) {
                            for (DashboardReceivableModel r : receivableModelList) {
                                if (r.getFeeType_id() == 3) {
                                    String amountActive = cursor.getString(cursor.getColumnIndex("closingActive"));
                                    String amountInactive = cursor.getString(cursor.getColumnIndex("closingInactive"));
                                    if (amountActive != null && r.getTotalClosingActive() != null) {
                                        double tcf_scholorship_feeActive = cursor.getDouble(cursor.getColumnIndex("closingActive"));
                                        if (tcf_scholorship_feeActive < 0)
                                            tcf_scholorship_feeActive = -1 * tcf_scholorship_feeActive;

//                                    int total = Integer.parseInt(amount) + Integer.parseInt(r.getTotalReceivable());
                                        double total = Double.parseDouble(r.getTotalClosingActive()) - tcf_scholorship_feeActive;
                                        r.setTotalClosingActive("" + total);

                                        addedClosing++;
                                    }

                                    if (amountInactive != null && r.getTotalClosingInActive() != null) {
                                        double tcf_scholorship_feeInactive = cursor.getDouble(cursor.getColumnIndex("closingInactive"));

                                        if (tcf_scholorship_feeInactive < 0)
                                            tcf_scholorship_feeInactive = -1 * tcf_scholorship_feeInactive;

                                        double total = Double.parseDouble(r.getTotalClosingInActive()) - tcf_scholorship_feeInactive;
                                        r.setTotalClosingInActive("" + total);

                                        addedClosing++;
                                    }

                                    if (addedClosing > 0){
                                        break;
                                    }
                                }
                            }

                        }

                        if (addedClosing == 0) {
                            DashboardReceivableModel model = new DashboardReceivableModel();
                            model.setFeeType_id(cursor.getInt(cursor.getColumnIndex(fees_type_id)) == 8 ? 3 : cursor.getInt(cursor.getColumnIndex(fees_type_id)));
                            model.setTotalClosingActive("" + cursor.getDouble(cursor.getColumnIndex("closingActive")));
                            model.setTotalClosingInActive("" + cursor.getDouble(cursor.getColumnIndex("closingInactive")));
                            receivableModelList.add(model);
                        }
                    }else {
                        boolean added = false;

                        if (typeID == 3 || typeID == 8) {
                            for (DashboardReceivableModel r : receivableModelList) {
                                if (r.getFeeType_id() == 3) {
                                    String amount = cursor.getString(cursor.getColumnIndex("totalReceivable"));
                                    if (amount != null && r.getTotalReceivable() != null) {
                                        double tcf_scholorship_fee = cursor.getDouble(cursor.getColumnIndex("totalReceivable"));
                                        if (tcf_scholorship_fee < 0)
                                            tcf_scholorship_fee = -1 * tcf_scholorship_fee;

//                                    int total = Integer.parseInt(amount) + Integer.parseInt(r.getTotalReceivable());
                                        double total = Double.valueOf(r.getTotalReceivable()) - tcf_scholorship_fee;
                                        r.setTotalReceivable("" + total);

                                        added = true;
                                        break;
                                    }
                                }
                            }

                        }

                        if (!added) {
                            DashboardReceivableModel model = new DashboardReceivableModel();
                            model.setFeeType_id(cursor.getInt(cursor.getColumnIndex(fees_type_id)) == 8 ? 3 : cursor.getInt(cursor.getColumnIndex(fees_type_id)));
                            model.setTotalReceivable("" + cursor.getDouble(cursor.getColumnIndex("totalReceivable")));
                            model.setTotalReceived("" + cursor.getDouble(cursor.getColumnIndex("totalReceived")));
                            if (filterReceivableBy.equals("s")) {
                                model.setTotalOpening("" + cursor.getDouble(cursor.getColumnIndex("totalOpening")));
                            }
                            receivableModelList.add(model);
                        }
                    }

                } while (cursor.moveToNext());
            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                receivableModelList.add(new DashboardReceivableModel(1, ""));
                receivableModelList.add(new DashboardReceivableModel(2, ""));
                receivableModelList.add(new DashboardReceivableModel(3, ""));
                receivableModelList.add(new DashboardReceivableModel(4, ""));
                receivableModelList.add(new DashboardReceivableModel(5, ""));
                receivableModelList.add(new DashboardReceivableModel(6, ""));
                receivableModelList.add(new DashboardReceivableModel(7, ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return receivableModelList;
    }

//    public List<DashboardReceivableModel> getReceivedForDashboard(String schoolid, String filterReceivableBy) {
//
//        //Received
//        List<DashboardReceivableModel> receivableModelList = new ArrayList<>();
//        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
//        Cursor cursor = null;
//        String query = "";
//
//        query = "SELECT SUM(fd.fee_amount) AS totalAmount,fd.feeType_id" +
//                " FROM FeesHeader fh " +
//                " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id" +
//                " INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID IN (@SchoolID)" +
//                " where fh.Category_Id IN(2,4)";
//
//        if (filterReceivableBy.equals("m")) {
//            query += " AND STRFTIME('%Y-%m',replace(replace(fh.for_date,'AM',''),'PM','')) = STRFTIME('%Y-%m',date('now','start of month'))";
//        } else if (filterReceivableBy.equals("t")) {
//            query += " AND STRFTIME('%Y-%m-%d',replace(replace(fh.for_date,'AM',''),'PM','')) = STRFTIME('%Y-%m-%d','now')";
//        }
//
//        query += " GROUP BY fd.feeType_id";
//        query = query.replace("@SchoolID", schoolid);
//
//        try {
//            cursor = DB.rawQuery(query, null);
//            double totalAmount = 0.0;
//            boolean added = false;
//            if (cursor.moveToFirst()) {
//                do {
//                    DashboardReceivableModel model = new DashboardReceivableModel();
//
//                    int typeID = cursor.getInt(cursor.getColumnIndex(fees_type_id));
//                    totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
//
//                    model.setFeeType_id(typeID);
//                    model.setTotalAmount(totalAmount + "");
//                    receivableModelList.add(model);
//                } while (cursor.moveToNext());
//            }
////            else {
////                //else just send a list which contains empty string. Populate the appropiate items.
////                receivableModelList.add(new DashboardReceivableModel(1, ""));
////                receivableModelList.add(new DashboardReceivableModel(2, ""));
////                receivableModelList.add(new DashboardReceivableModel(3, ""));
////                receivableModelList.add(new DashboardReceivableModel(4, ""));
////                receivableModelList.add(new DashboardReceivableModel(5, ""));
////                receivableModelList.add(new DashboardReceivableModel(6, ""));
////                receivableModelList.add(new DashboardReceivableModel(7, ""));
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed())
//                cursor.close();
//        }
//
//        return receivableModelList;
//    }

    public List<AccountStatementSchoolModel> getAccountStatementForStudent(int schoolId, String fromDate, String toDate, int grNo) {
        List<AccountStatementSchoolModel> list = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        String query = "SELECT c.name as class,sec.name as 'section',s.*,s.id, strftime('%Y-%m-%d',replace(replace(upper(fh.for_date),'AM',''),'PM','')) as FORDATE,ft.feeType_name,IFNULL(SUM(CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount END),0) AS opening_balance,\n" +
                "IFNULL(SUM(CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount END),0) AS invoice,\n" +
                "IFNULL(SUM(CASE WHEN fh.Category_Id = 2 THEN fd.fee_amount END),0) AS receipt,\n" +
                "IFNULL(SUM(CASE WHEN fh.Category_Id = 4 THEN fd.fee_amount END),0) as closing_balance\n" +
                "FROM FeesHeader AS fh \n" +
                "INNER JOIN FeesDetail AS fd ON fd.feesHeader_id = fh.id \n" +
                "INNER JOIN FeeType AS ft ON ft.id = fd.feeType_id \n" +
                "INNER JOIN school_class AS sc ON sc.id = fh.schoolclass_id \n" +
                "INNER JOIN student as s ON s.id = fh.student_id\n" +
                "INNER JOIN class c on c.id = sc.class_id\n" +
                "INNER JOIN 'section' sec on sec.id = sc.section_id" +
                " WHERE sc.school_id = @school_id AND s.student_gr_no = @student_gr ";

        query = query.replace("@school_id", "" + schoolId);
        query = query.replace("@student_gr", "" + grNo);

        if ((fromDate != null && toDate != null) && (!fromDate.isEmpty() && !toDate.isEmpty())) {

            query += " and fh.created_on between '@FromDate 00:00:00 AM' and '@ToDate 23:59:59 PM'";

            query = query.replace("@FromDate", fromDate);
            query = query.replace("@ToDate", toDate);
        }
        query += " GROUP BY FORDATE,ft.feeType_name";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    AccountStatementSchoolModel model = new AccountStatementSchoolModel();
                    model.setStudentName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_NAME)));
                    model.setFatherName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_FATHERS_NAME)));
                    model.setForDate(cursor.getString(cursor.getColumnIndex("FORDATE")));
                    model.setFeeTypeName(cursor.getString(cursor.getColumnIndex(fee_type_name)));
                    model.setOpeningBalance(cursor.getInt(cursor.getColumnIndex("opening_balance")));
                    model.setInvoice(cursor.getInt(cursor.getColumnIndex("invoice")));
                    model.setReceipt(cursor.getInt(cursor.getColumnIndex("receipt")));
                    model.setClassName(cursor.getString(cursor.getColumnIndex("class")));
                    model.setSection(cursor.getString(cursor.getColumnIndex("section")));
                    model.setGrNo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_GR_NO)));
                    model.setClosingBalance(cursor.getInt(cursor.getColumnIndex("closing_balance")));
                    list.add(model);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<AccountStatementSchoolModel> getAccountStatementForSchool(int schoolId, String fromDate, String toDate) {
        List<AccountStatementSchoolModel> list = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        String query = "SELECT strftime('%Y-%m-%d',replace(replace(upper(fh.for_date),'AM',''),'PM','')) as FORDATE,ft.feeType_name,IFNULL(SUM(CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount END),0) AS opening_balance,\n" +
                "IFNULL(SUM(CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount END),0) AS invoice,\n" +
                "IFNULL(SUM(CASE WHEN fh.Category_Id = 2 THEN fd.fee_amount END),0) AS receipt\n" +
                "FROM FeesHeader AS fh INNER JOIN\n" +
                "FeesDetail AS fd ON fd.feesHeader_id = fh.id INNER JOIN \n" +
                "FeeType AS ft ON ft.id = fd.feeType_id INNER JOIN\n" +
                "school_class AS sc ON sc.id = fh.schoolclass_id\n" +
                "WHERE sc.school_id = @school_id";
        query = query.replace("@school_id", "" + schoolId);
        if ((fromDate != null && toDate != null) && (!fromDate.isEmpty() && !toDate.isEmpty())) {

            query += " and fh.created_on between '@FromDate 00:00:00 AM' and '@ToDate 23:59:59 PM'";

            query = query.replace("@FromDate", fromDate);
            query = query.replace("@ToDate", toDate);
        }
        query += " GROUP BY FORDATE,ft.feeType_name";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    AccountStatementSchoolModel model = new AccountStatementSchoolModel();
                    model.setForDate(cursor.getString(cursor.getColumnIndex("FORDATE")));
                    model.setFeeTypeName(cursor.getString(cursor.getColumnIndex(fee_type_name)));
                    model.setOpeningBalance(cursor.getInt(cursor.getColumnIndex("opening_balance")));
                    model.setInvoice(cursor.getInt(cursor.getColumnIndex("invoice")));
                    model.setReceipt(cursor.getInt(cursor.getColumnIndex("receipt")));
//                    model.setClosingBalance(cursor.getInt(cursor.getColumnIndex("closing_balance")));
                    list.add(model);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public long updateDownloadedFeesHeader(SyncFeesHeaderModel model) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            if (model.getCashDeposit_id() != null && !model.getCashDeposit_id().isEmpty()) {
                cv.put(CASH_DEPOSIT_ID, model.getCashDeposit_id());
                cv.put(CASH_DEPOSIT_ID_TYPE, "S");
            }

            if (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty()) {
                if (Long.parseLong(model.getReceiptNo()) > -1) {
                    cv.put(RECEIPT_NO, model.getReceiptNo());
                }
            }

            if (model.getForDate() != null) {
                model.setForDate(AppModel.getInstance().convertDatetoFormat(model.getForDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a"));
                cv.put(FOR_DATE, model.getForDate());
            }

            if (model.getCreatedOn() != null) {
                String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

                model.setCreatedOn(createdOn);
                cv.put(CREATED_ON, model.getCreatedOn());

            }

            if (model.getCreatedOn_Server() != null) {
                String createdOnServer = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn_Server(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

                model.setCreatedOn_Server(createdOnServer);
                cv.put(CREATED_ON_SERVER, model.getCreatedOn_Server());

            }

            if (model.getModified_on() != null) {
//                String modifiedOn = AppModel.getInstance().convertDatetoFormat(model.getModified_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

//                model.setModified_on(modifiedOn);
                cv.put(MODIFIED_ON, model.getModified_on());

            }

            cv.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

            String downloadedOn = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");
            model.setDownloadedOn(downloadedOn);
            cv.put(DOWNLOADED_ON, downloadedOn);

            cv.put(SYS_ID, model.getId());
//            cv.put(UPLOADED_ON, model.getUploadedOn());
            cv.put(CREATED_BY, model.getCreatedBy());
            cv.put(STUDENT_ID, model.getStudentId());
            cv.put(SCHOOL_YEAR_ID, model.getAcademicSession_id());
            cv.put(SCHOOL_CLASS_ID, model.getSchoolClassId());
            cv.put(SCHOOL_ACADEMIC_SESSION_ID, model.getAcademicSession_id());
            cv.put(RECEIPT_NO, model.getReceiptNo());
            cv.put(TOTAL_AMOUNT, model.getTotalAmount());
            cv.put(CATEGORY_ID, model.getCategory_id());
            cv.put(TRANSACTION_TYPE_ID, model.getTransactionType_id());

            id = db.update(TABLE_FEES_HEADER, cv, SYS_ID + "=" + model.getId(), null);
            return id;

        } catch (Exception e) {
            throw e;
        }
    }

    public int getFeesHeaderId(int headerId) {
        Cursor cursor = null;
        String selectQuery = "SELECT id FROM " + TABLE_FEES_HEADER + " WHERE sys_id = " + headerId;

        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public void insertIntoErrorLog(int schoolId, long depositId) {
        Cursor cursor = null;
        String selectQuery = "SELECT sc.school_id,fh.CashDeposit_id,IFNULL(SUM(fd.fee_amount),0) as FDAmount," +
                " (SELECT IFNULL( SUM(cd.deposit_amount),0) AS totalDeposit FROM CashDeposit cd" +
                " WHERE cd.school_id IN (" + schoolId + ") AND id = " + depositId + ") as CDAmount FROM FeesHeader fh" +
                " INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id" +
                " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id" +
                " WHERE fh.Category_Id = 2 AND sc.school_id IN (" + schoolId + ") AND fh.CashDeposit_id = " + depositId;

        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                int cdID = cursor.getInt(cursor.getColumnIndex(CASH_DEPOSIT_ID));
                int scID = cursor.getInt(cursor.getColumnIndex(CashDeposit.SCHOOL_ID));
                String feesDetailAmount = cursor.getString(cursor.getColumnIndex("FDAmount"));
                String cashDepositAmount = cursor.getString(cursor.getColumnIndex("CDAmount"));
                AppModel.getInstance().appendErrorLog(context,
                        "After inserting cash deposit count of fee detail amount is:" + feesDetailAmount
                                + " and cash deposit amount is:" + cashDepositAmount + " with cash deposit id=" + cdID
                                + " and school id=" + scID);
//                SurveyAppModel.getInstance().appendErrorLog(context,"Query to get total amount:"+selectQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean getFeesHeaderCount(int schoolID) {
        boolean isExists = false;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String selectQuery = "SELECT count(*) fhCount FROM " + TABLE_FEES_HEADER + " WHERE "
                + SCHOOL_CLASS_ID + " IN (SELECT id FROM school_class WHERE school_id = @SchoolId)";

        selectQuery = selectQuery.replace(" @SchoolId", String.valueOf(schoolID));
        try {
            cursor = db.rawQuery(selectQuery,null);
            if (cursor.moveToFirst()){
                isExists = cursor.getInt(cursor.getColumnIndex("fhCount")) > 0;
            }

        } catch (Exception e){
            AppModel.getInstance().appendErrorLog(context,"Error in getFeesHeaderCount: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return isExists;
    }

    public boolean getFeesDetailCount(int schoolID) {
        boolean isExists = false;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        String selectQuery = "SELECT count(fd.id) fdCount FROM " + TABLE_FEES_DETAIL + " fd\n"
                + " INNER JOIN " + TABLE_FEES_HEADER + " fh ON fh." + ID + "= fd." + fees_header_id
                + " WHERE fh." + SCHOOL_CLASS_ID + " IN (SELECT id FROM school_class WHERE school_id = @SchoolId)";

        selectQuery = selectQuery.replace(" @SchoolId", String.valueOf(schoolID));
        try {
            cursor = db.rawQuery(selectQuery,null);
            if (cursor.moveToFirst()){
                isExists = cursor.getInt(cursor.getColumnIndex("fdCount")) > 0;
            }

        } catch (Exception e){
            AppModel.getInstance().appendErrorLog(context,"Error in getFeesDetailCount: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return isExists;
    }

    public FeesHeaderModel getFeesHeader(int schoolId, int feeHeaderSysId, SQLiteDatabase db) {
        List<FeesHeaderModel> fhmList = new ArrayList<>();
        Cursor cursor = null;
        FeesHeaderModel model = new FeesHeaderModel();

        String selectQuery = "SELECT * FROM " + TABLE_FEES_HEADER + " WHERE "
                + SCHOOL_CLASS_ID + " IN (SELECT id FROM school_class WHERE school_id = @SchoolId) \n"
                + "AND " + SYS_ID + " = " + feeHeaderSysId;

        selectQuery = selectQuery.replace(" @SchoolId", String.valueOf(schoolId));

        try {
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                model.setSys_id(cursor.getInt(cursor.getColumnIndex(SYS_ID)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return model;
    }

    public void addCSVDownloadedFeesHeader(List<SyncFeesHeaderModel> fhrm, int schoolID, SyncDownloadUploadModel syncDownloadUploadModel) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1, feeCount = 0;
        int downloadedCount = 0;

        try {
//            db.beginTransaction();  it is commented because it is lagging the app
            for (SyncFeesHeaderModel model : fhrm) {
//                if (FindFeesHeaderRecord(model.getId()) == 0) {
                ContentValues cv = new ContentValues();
                if (model.getCashDeposit_id() != null && !model.getCashDeposit_id().isEmpty()) {
                    cv.put(CASH_DEPOSIT_ID, model.getCashDeposit_id());
                    cv.put(CASH_DEPOSIT_ID_TYPE, "S");
                }

                if (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty()) {
                    if (Long.parseLong(model.getReceiptNo()) > -1) {
                        cv.put(RECEIPT_NO, model.getReceiptNo());
                    }
                }

                if (model.getForDate() != null) {
                    model.setForDate(AppModel.getInstance().convertDatetoFormat(model.getForDate(), "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd hh:mm:ss a"));
                    cv.put(FOR_DATE, model.getForDate());
                }

                if (model.getCreatedOn() != null) {
                    String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd hh:mm:ss a");

                    model.setCreatedOn(createdOn);
                    cv.put(CREATED_ON, model.getCreatedOn());

                }

                if (model.getCreatedOn_Server() != null) {
                    String createdOnServer = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn_Server(), "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd hh:mm:ss a");

                    model.setCreatedOn_Server(createdOnServer);
                    cv.put(CREATED_ON_SERVER, model.getCreatedOn_Server());

                }


                if (model.getModified_on() != null) {
                    String modifiedOn = AppModel.getInstance().convertDatetoFormat(model.getModified_on(), "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd'T'HH:mm:ss");

                    model.setModified_on(modifiedOn);
                    cv.put(MODIFIED_ON, model.getModified_on());

                }


                String downloadedOn = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");
                model.setDownloadedOn(downloadedOn);
                cv.put(DOWNLOADED_ON, downloadedOn);
                cv.put(UPLOADED_ON, downloadedOn);

                cv.put(SYS_ID, model.getId());
//            cv.put(UPLOADED_ON, model.getUploadedOn());
                cv.put(CREATED_BY, model.getCreatedBy());
                cv.put(STUDENT_ID, model.getStudentId());
                cv.put(SCHOOL_YEAR_ID, model.getAcademicSession_id());
                cv.put(SCHOOL_CLASS_ID, model.getSchoolClassId());
                cv.put(SCHOOL_ACADEMIC_SESSION_ID, model.getAcademicSession_id());
                cv.put(RECEIPT_NO, model.getReceiptNo());
                cv.put(TOTAL_AMOUNT, model.getTotalAmount());
                cv.put(CATEGORY_ID, model.getCategory_id());
                cv.put(TRANSACTION_TYPE_ID, model.getTransactionType_id());
                id = db.insert(TABLE_FEES_HEADER, null, cv);
                if (id > 0) {
                    feeCount++;
                    downloadedCount++;
                } else {
                    AppModel.getInstance().appendErrorLog(context, "In addCSVDownloadedFeesHeader While insertion FeesHeader record: " + model.print());
                }

//                    try {
//                        insertDownloadedFeesDetails(db, id, model.getFdmList());
//                    } catch (Exception e) {
//                        throw e;
//                    }
//                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }


//            db.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addCSVDownloadedFeesHeader While insertion FeesHeader record Error: " + e.getMessage());
            throw e;
        } finally {
            Log.i("feecount", ":When lastServer id 0---->>>>" + feeCount + " school id:---->>>" + schoolID);
//            db.endTransaction();
        }
    }

    public long addCSVDownloadedFeesDetail(int feeHeaderDeviceId, FeesDetailUploadModel model, SQLiteDatabase db) {
        long id = 0;
        try {
            if (!FeesCollection.getInstance(context).FindCSVDownloadedFeesDetailRecord(feeHeaderDeviceId, model.getFeeType_id(),db))
                id = FeesCollection.getInstance(context).insertCSVDownloadedFeesDetails(feeHeaderDeviceId, model, db);
            else {
                id = FeesCollection.getInstance(context).updateCSVDownloadedFeesDetails(feeHeaderDeviceId, model ,db);
            }

        }catch (Exception e){
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In addCSVDownloadedFeesDetail Error: " + e.getMessage());
        }
        return id;
    }

    public long insertCSVDownloadedFeesDetails(long feesHeader_id, FeesDetailUploadModel model, SQLiteDatabase db) throws Exception {
        long id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(fee_amount, model.getAmount());
            cv.put(fees_type_id, model.getFeeType_id());
            cv.put(fees_header_id, (int) feesHeader_id);

            id = db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);

            if (id <= 0){
                AppModel.getInstance().appendLog(context, "Cannot insert record insertCSVDownloadedFeesDetails While insertion FeesDetail record");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In insertCSVDownloadedFeesDetails Error: "+e.getMessage());
            throw e;
        }
        return id;
    }

    public long updateCSVDownloadedFeesDetails(long feesHeader_id, FeesDetailUploadModel model, SQLiteDatabase db) {
        int id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(fee_amount, model.getAmount());
            cv.put(fees_type_id, model.getFeeType_id());
            cv.put(fees_header_id, (int) feesHeader_id);
            id = db.update(TABLE_FEES_DETAIL, cv, fees_header_id + " = " + feesHeader_id + " AND " + fees_type_id + " = " + model.getFeeType_id(), null);

            if (id <= 0){
                AppModel.getInstance().appendLog(context, "Cannot update record updateCSVDownloadedFeesDetails While update FeesDetail record");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In updateCSVDownloadedFeesDetails Error: "+e.getMessage());
        }
        return id;
    }

    public boolean FindCSVDownloadedFeesDetailRecord(int headerId, int feeType_id, SQLiteDatabase db) throws Exception {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_FEES_DETAIL
                    + " WHERE " + fees_header_id + " = " + headerId + " AND " + fees_type_id + " = " + feeType_id;
            cursor = db.rawQuery(selectQuery, null);
            boolean count = cursor.getCount() > 0;
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public boolean isSchoolForFeeEntry(SchoolModel sm) {
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
//        String selectQuery = "SELECT count(st.id) as stCount FROM student st\n" +
//                "INNER JOIN school_class sc on sc.id = st.schoolclass_id\n" +
//                "WHERE sc.school_id = @SchoolID and sc.is_active = 1 and st.is_active = 1 \n" +
//                "and st.Actual_Fees < 10 OR st.Actual_Fees > 1500 OR st.Actual_Fees IS NULL";


        String selectQuery = "SELECT count(st.id) as stCount FROM student st\n" +
                "INNER JOIN school_class sc on sc.id = st.schoolclass_id\n" +
                "WHERE sc.school_id = @SchoolID and sc.is_active = 1 and st.is_active = 1 \n" +
                "and (st.Actual_Fees < 10 OR st.Actual_Fees > 1500 OR st.Actual_Fees IS NULL OR \n" +
                "(Select  ifnull(fd.fee_amount,0) as A from FeesHeader fh inner join FeesDetail fd on fd.feesHeader_id = fh.id where fh.student_id = st.server_id and fh.Category_Id = 3 and fh.TransactionType_Id = 1) IS NULL)";


        selectQuery = selectQuery.replaceAll("@SchoolID",sm.getId()+"");

        try {
            cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("stCount")) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public boolean isFeeCorrectionAlreadyExists(int schoolId, long receiptNo) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String selectQuery = "SELECT count(*) as correctionCount FROM " + TABLE_FEES_HEADER + " \n" +
                " WHERE " + SCHOOL_CLASS_ID + " in (SELECT id FROM school_class WHERE school_id = @SchoolId)\n" +
                " AND " + RECEIPT_NO + " = @ReceiptNo AND " + TRANSACTION_TYPE_ID + " = 2";

        selectQuery = selectQuery.replace("@SchoolId",schoolId+"");
        selectQuery = selectQuery.replace("@ReceiptNo",receiptNo+"");

        boolean isExists = false;
        Cursor cursor = null;
        try {
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()){
                do {
                    int count = cursor.getInt(cursor.getColumnIndex("correctionCount"));
                    if (count > 0){
                        isExists = true;
                    }else {
                        isExists = false;
                    }

                }while (cursor.moveToNext());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return isExists;
    }

    public int getFeeHeaderIdForUpdate(long receiptNo, int schoolID, int categoryID) {
        int id = 0;
        String query = "SELECT fh." + ID + " , sum(ifnull(fd." + fee_amount + ",0)) FROM " + TABLE_FEES_HEADER + " fh \n" +
                "INNER JOIN " + TABLE_FEES_DETAIL + " fd on fd." + fees_header_id + " = fh." + ID + " \n" +
                "WHERE fh." + TRANSACTION_TYPE_ID + " = 2 and fh." + CATEGORY_ID + " = " + categoryID + " \n" +
                "AND fh." + RECEIPT_NO + "= " + receiptNo + " AND fh." + SCHOOL_CLASS_ID + " IN (SELECT " + ID + " FROM " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS + "\n" +
                "WHERE " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + schoolID + ") \n" +
                "GROUP BY fh." + ID + " HAVING sum(ifnull(fd." + fee_amount + ",0)) > 0";

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex(ID));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null)
                cursor.close();
        }

        return id;
    }

    public int getFeeHeaderSysIdForUpdate(long receiptNo, int schoolID, int categoryID) {
        int id = 0;
        String query = "SELECT fh." + SYS_ID + " , sum(ifnull(fd." + fee_amount + ",0)) FROM " + TABLE_FEES_HEADER + " fh \n" +
                "INNER JOIN " + TABLE_FEES_DETAIL + " fd on fd." + fees_header_id + " = fh." + ID + " \n" +
                "WHERE fh." + TRANSACTION_TYPE_ID + " = 2 and fh." + CATEGORY_ID + " = " + categoryID + " \n" +
                "AND fh." + RECEIPT_NO + "= " + receiptNo + " AND fh." + SCHOOL_CLASS_ID + " IN (SELECT " + ID + " FROM " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS + "\n" +
                "WHERE " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + schoolID + ") \n" +
                "GROUP BY fh." + ID + " HAVING sum(ifnull(fd." + fee_amount + ",0)) > 0";

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex(SYS_ID));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null)
                cursor.close();
        }

        return id;
    }

    public long updateCorrection(int fhID , FeesHeaderModel feesHeaderModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(MODIFIED_ON, feesHeaderModel.getCreatedOn());
            cv.put(TOTAL_AMOUNT, feesHeaderModel.getTotal_amount());
            cv.put(UPLOADED_ON, (String) null);
            if (feesHeaderModel.getReceipt_id() > 0)
                cv.put(RECEIPT_ID, feesHeaderModel.getReceipt_id());
//            cv.put(STUDENT_ID, feesHeaderModel.getStudentId());
//            cv.put(SCHOOL_CLASS_ID, feesHeaderModel.getSchoolClassId());
//            cv.put(SCHOOL_YEAR_ID, feesHeaderModel.getSchoolYearId());
//            cv.put(ACADEMIC_SESSION_ID, feesHeaderModel.getAcademicSession_id());
//            cv.put(CREATED_ON, feesHeaderModel.getCreatedOn());
//            cv.put(CREATED_BY, feesHeaderModel.getCreatedBy());
//            cv.put(REMARKS, feesHeaderModel.getRemarks());

            id = db.update(TABLE_FEES_HEADER, cv, ID + "=" + fhID, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public long untagReceipts(int cashDepositID) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.putNull(CASH_DEPOSIT_ID);
            cv.putNull(CASH_DEPOSIT_ID_TYPE);
            id = db.update(TABLE_FEES_HEADER, cv, "( " + CASH_DEPOSIT_ID + " = " + cashDepositID + " AND " + CASH_DEPOSIT_ID_TYPE + " = 'L' )", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public void updateFeesDetailsCorrection(long feesHeader_id, List<ViewReceivablesCorrectionModel> viewReceivablesModels, int transactionCategory) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            for (int i = 0; i < viewReceivablesModels.size(); i++) {
                ContentValues cv = new ContentValues();
                double amount = 0.0;

                /* For AppInvoice "1" For AppReceipt "2" */
                if (transactionCategory == 1) {
                    if (viewReceivablesModels.get(i).getNewSales() != null && !viewReceivablesModels.get(i).getNewSales().equals("")) {
                        amount = Double.parseDouble(viewReceivablesModels.get(i).getNewSales());
//                        if (amount != 0) {
                        if (amount >= 0) {
                            if (isFeeDetailRecordPresent(feesHeader_id,viewReceivablesModels.get(i).getFeeTypeId())){
                                cv.put(fee_amount, amount);
                                db.update(TABLE_FEES_DETAIL, cv,
                                        fees_header_id + "=" + feesHeader_id + " AND " + fees_type_id + "=" + viewReceivablesModels.get(i).getFeeTypeId(),
                                        null);
                            }else {
                                cv.put(fee_amount, amount);
                                cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                                cv.put(fees_header_id, (int) feesHeader_id);
                                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                            }
                        }
                    }
                } else if (transactionCategory == 2) {
                    if (viewReceivablesModels.get(i).getNewReceived() != null && !viewReceivablesModels.get(i).getNewReceived().equals("")) {
                        amount = Double.parseDouble(viewReceivablesModels.get(i).getNewReceived());
                        //if (amount != 0) {
                        if (amount >= 0) {
                            if (isFeeDetailRecordPresent(feesHeader_id,viewReceivablesModels.get(i).getFeeTypeId())) {
                                cv.put(fee_amount, amount);
                                db.update(TABLE_FEES_DETAIL, cv,
                                        fees_header_id + "=" + feesHeader_id + " AND " + fees_type_id + "=" + viewReceivablesModels.get(i).getFeeTypeId(),
                                        null);
                            }else {
                                cv.put(fee_amount, amount);
                                cv.put(fees_type_id, viewReceivablesModels.get(i).getFeeTypeId());
                                cv.put(fees_header_id, (int) feesHeader_id);
                                db.insertOrThrow(TABLE_FEES_DETAIL, null, cv);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFeeDetailRecordPresent(long feesHeader_id, int feeTypeId) {
        String query = "SELECT  * FROM " + TABLE_FEES_DETAIL + " WHERE " + fees_header_id + " = " + feesHeader_id + " AND " + fees_type_id + " = " + feeTypeId;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        boolean isPresent = false;
        try {
            cursor = db.rawQuery(query, null);
            isPresent = cursor.getCount() > 0;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null)
                cursor.close();
        }
        return isPresent;
    }

    public List<ReceiptListModel> getPreviousInvoicesForReceivableReport(int schoolId, String fromDate, String toDate, String grNo) {
        List<ReceiptListModel> list = new ArrayList<>();

        String query = "SELECT * FROM (\n" +
                "SELECT fh.id,fh.receipt_no,fh.category_id, fh.TransactionType_Id, ifnull((SELECT student_gr_no from student \n" +
                "WHERE id = fh.student_id),0) as student_gr_no, SUM(fd.fee_amount) as total_amount,fh.created_by, fh.created_on \n" +
                "FROM FeesHeader fh INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id INNER JOIN school_class sc ON sc.id = fh.schoolclass_id \n" +
                "WHERE sc.school_id IN (@schoolId) AND fd.fee_amount != 0 AND fd.fee_amount > 0 AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 1 \n" +
                "AND  fh.created_on >= '@fromDate 00:00:00 AM' and fh.created_on <= '@toDate 23:59:59 PM' AND  \n" +
                "student_gr_no = '@grNo' group by fh.id HAVING fh.id = MAX(fh.id)\n" +
                "\n" +
                "UNION ALL SELECT fh.id,fh.receipt_no,fh.category_id, fh.TransactionType_Id, ifnull((SELECT student_gr_no from student \n" +
                "WHERE id = fh.student_id),0) as student_gr_no, SUM(fd.fee_amount) as total_amount,fh.created_by, fh.createdOn_server as created_on  \n" +
                "FROM FeesHeader fh INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id INNER JOIN school_class sc ON sc.id = fh.schoolclass_id \n" +
                "WHERE sc.school_id IN (@schoolId) AND fd.fee_amount != 0 AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 1 \n" +
                "AND  fh.createdOn_server >= '@fromDate 00:00:00 AM' and fh.createdOn_server <= '@toDate 23:59:59 PM' AND  \n" +
                "student_gr_no = '@grNo' AND ifnull(fh.created_on,'') = '' group by fh.id HAVING fh.id = MAX(fh.id)\n" +
                ") as tb1 ORDER BY created_on DESC";

//        String query = "SELECT fh.id,fh.receipt_no,fh.category_id, fh.TransactionType_Id, " +
//                "ifnull((SELECT student_gr_no from student WHERE id = fh.student_id),0) as student_gr_no, " +
//                "SUM(fd.fee_amount) as total_amount,fh.created_by, " +
//                "fh.created_on " +
//                "FROM FeesHeader fh " +
//                "INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id " +
//                "INNER JOIN school_class sc ON sc.id = fh.schoolclass_id " +
//                "WHERE sc.school_id IN (@schoolId) AND fd.fee_amount != 0 AND fd.fee_amount > 0 " +
//                "AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 1 " +
//                "AND  fh.created_on >= '@fromDate 00:00:00 AM' and fh.created_on <= '@toDate 23:59:59 PM' " +
//                "AND  student_gr_no = '@grNo' " +
//                "group by fh.id HAVING fh.id = MAX(fh.id) " +
//                "ORDER BY fh.created_on DESC";

        if(!Strings.isEmptyOrWhitespace(grNo))
            query = query.replace("@grNo", grNo );
        else
            query = query.replace("AND  student_gr_no = '@grNo' ", "");


        if (schoolId > 0) {
            query = query.replace("@schoolId", schoolId + "");
        } else {
            query = query.replace("@schoolId", AppModel.getInstance().getuserSchoolIDS(context));
        }

        query = query.replace("@fromDate", fromDate);
        query = query.replace("@toDate", toDate);

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    ReceiptListModel model = new ReceiptListModel();
                    model.setReceiptId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setReceiptNo(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                    model.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
                    model.setTypeId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
                    model.setTotalAmount(cursor.getDouble(cursor.getColumnIndex("total_amount")));
                    model.setCreateBy(cursor.getString(cursor.getColumnIndex("created_by")));
                    model.setCreateOn(cursor.getString(cursor.getColumnIndex("created_on")));
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

    public List<ReceiptListModel> getPreviousReceiptsForReceivableReport(int schoolId, String fromDate, String toDate, String grNo) {
        List<ReceiptListModel> list = new ArrayList<>();

        String query = "SELECT fh.id,fh.receipt_no,fh.category_id, fh.TransactionType_Id, school_id, fh.student_id,fh.schoolclass_id, " +
                "ifnull((SELECT student_gr_no from student WHERE id = fh.student_id),0) as student_gr_no, " +
                "SUM(fd.fee_amount) as total_amount,fh.created_by, " +
                "fh.created_on, " +
                "(CASE WHEN fh.CashDeposit_id IS NULL OR fh.CashDeposit_id = 0 then 0 \n" +
                "ELSE CASE WHEN fh.CashDeposit_id  NOTNULL THEN 1 \n" +
                "END END) as isDeposited,(SELECT deposit_slip_no FROM CashDeposit \n" +
                "where (fh.CashDeposit_id_Type = 'S' and sys_id = fh.CashDeposit_id) || (fh.CashDeposit_id_Type = 'L' and id = fh.CashDeposit_id)) as deposit_slip_no\n" +
                "FROM FeesHeader fh " +
                "INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id " +
                "INNER JOIN school_class sc ON sc.id = fh.schoolclass_id " +
                "WHERE sc.school_id IN (@schoolId) AND fd.fee_amount != 0 " +
                "AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 2 " +
                "AND (fh.receipt_no IS NOT NULL OR fh.receipt_no != '') " +
                "AND  fh.created_on >= '@fromDate 00:00:00 AM' and fh.created_on <= '@toDate 23:59:59 PM' " +
                "AND  student_gr_no = '@grNo' " +
                "group by fh.receipt_no,school_id HAVING fh.id = MAX(fh.id) " +
                "ORDER BY fh.created_on DESC";

        if(!Strings.isEmptyOrWhitespace(grNo))
            query = query.replace("@grNo", grNo);
        else
            query = query.replace("AND  student_gr_no = '@grNo' ", "");


        if (schoolId > 0) {
            query = query.replace("@schoolId", schoolId + "");
        } else {
            query = query.replace("@schoolId", AppModel.getInstance().getuserSchoolIDS(context));
        }

        query = query.replace("@fromDate", fromDate);
        query = query.replace("@toDate", toDate);

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    ReceiptListModel model = new ReceiptListModel();
                    model.setReceiptId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setReceiptNo(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                    model.setCategoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
                    model.setTypeId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
                    model.setTotalAmount(cursor.getDouble(cursor.getColumnIndex("total_amount")));
                    model.setCreateBy(cursor.getString(cursor.getColumnIndex("created_by")));
                    model.setCreateOn(cursor.getString(cursor.getColumnIndex("created_on")));
                    model.setIsDeposited(cursor.getInt(cursor.getColumnIndex("isDeposited")));
                    model.setDeposit_slip_no(cursor.getString(cursor.getColumnIndex(CashDeposit.DEPOSIT_SLIP_NO)));

                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex("school_id")));
                    model.setStudent_gr_no(cursor.getInt(cursor.getColumnIndex("student_gr_no")));
                    model.setStudentId(cursor.getInt(cursor.getColumnIndex("student_id")));
                    model.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
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

    public ReceiptListModel getOpeningForReceivableReport(int schoolId, String fromDate, String toDate, String grNo) {
        ReceiptListModel model = new ReceiptListModel();

        String query = "SELECT fh.id,fh.receipt_no,fh.category_id, fh.TransactionType_Id, school_id, fh.student_id,fh.schoolclass_id, " +
                "ifnull((SELECT student_gr_no from student WHERE id = fh.student_id),0) as student_gr_no, " +
                "SUM(fd.fee_amount) as total_amount,fh.created_by, " +
                "fh.created_on, " +
                "(CASE WHEN fh.CashDeposit_id IS NULL OR fh.CashDeposit_id = 0 then 0 \n" +
                "ELSE CASE WHEN fh.CashDeposit_id  NOTNULL THEN 1 \n" +
                "END END) as isDeposited,(SELECT deposit_slip_no FROM CashDeposit \n" +
                "where (fh.CashDeposit_id_Type = 'S' and sys_id = fh.CashDeposit_id) || (fh.CashDeposit_id_Type = 'L' and id = fh.CashDeposit_id)) as deposit_slip_no\n" +
                "FROM FeesHeader fh " +
                "INNER JOIN FeesDetail fd ON fd.feesHeader_id = fh.id " +
                "INNER JOIN school_class sc ON sc.id = fh.schoolclass_id " +
                "WHERE sc.school_id IN (@schoolId) AND fd.fee_amount != 0 " +
                "AND fh.TransactionType_Id IN (1,2) AND fh.Category_Id = 3 " +
                "AND (fh.receipt_no IS NOT NULL OR fh.receipt_no != '') " +
                "AND  fh.created_on >= '@fromDate 00:00:00 AM' and fh.created_on <= '@toDate 23:59:59 PM' " +
                "AND  student_gr_no = '@grNo' " +
                "group by fh.receipt_no,school_id HAVING fh.id = MAX(fh.id) " +
                "ORDER BY fh.created_on DESC";

        if(!Strings.isEmptyOrWhitespace(grNo))
            query = query.replace("@grNo", grNo);
        else
            query = query.replace("AND  student_gr_no = '@grNo' ", "");


        if (schoolId > 0) {
            query = query.replace("@schoolId", schoolId + "");
        } else {
            query = query.replace("@schoolId", AppModel.getInstance().getuserSchoolIDS(context));
        }

        query = query.replace("@fromDate", fromDate);
        query = query.replace("@toDate", toDate);

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                model.setReceiptId(cursor.getInt(cursor.getColumnIndex(ID)));
                model.setReceiptNo(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                model.setCategoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
                model.setTypeId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE_ID)));
                model.setTotalAmount(cursor.getDouble(cursor.getColumnIndex("total_amount")));
                model.setCreateBy(cursor.getString(cursor.getColumnIndex("created_by")));
                model.setCreateOn(cursor.getString(cursor.getColumnIndex("created_on")));
                model.setIsDeposited(cursor.getInt(cursor.getColumnIndex("isDeposited")));
                model.setDeposit_slip_no(cursor.getString(cursor.getColumnIndex(CashDeposit.DEPOSIT_SLIP_NO)));

                model.setSchoolId(cursor.getInt(cursor.getColumnIndex("school_id")));
                model.setStudent_gr_no(cursor.getInt(cursor.getColumnIndex("student_gr_no")));
                model.setStudentId(cursor.getInt(cursor.getColumnIndex("student_id")));
                model.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return model;
    }

    public long getCashInHand(int schoolId, int academicSessionId) {
        long cashInHand = 0;
        String maxCreatedOnOfCashDeposit = getMaxCreatedOnOfCashDeposit();
        if(!Strings.isEmptyOrWhitespace(maxCreatedOnOfCashDeposit)) {
            String updatedMaxCreatedOnOfCashDeposit = AppModel.getInstance().convertDatetoFormat(
                    maxCreatedOnOfCashDeposit,
                    "yyyy-MM-dd hh:mm:ss a",
                    "yyyy-MM-dd");

            String query = " select fd.id, fd.fee_amount, fh." + CREATED_ON + " from FeesDetail fd\n" +
                    " INNER JOIN FeesHeader fh ON fh.id = fd.feesHeader_id \n" +
                    " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id \n" +
                    " WHERE \n" +
                    " fh.Category_Id = 2 AND sc.school_id IN (@SchoolID) \n" +
                    " and\n" +
                    " fh." + CREATED_ON + " > '@MaxDate' ";

            if (academicSessionId > 0)
                query += " and fh.academic_session_id = @ASId";

            query += " ORDER by fh.created_on";


            if (schoolId > 0) {
                query = query.replace("@SchoolID", schoolId + "");
            } else {
                query = query.replace("@SchoolID", AppModel.getInstance().getAllUserSchoolsForFinance(context));
            }

            query = query.replace("@MaxDate", updatedMaxCreatedOnOfCashDeposit);
            if (academicSessionId > 0) {
                query = query.replace("@ASId", academicSessionId + "");
            }

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            Cursor cursor = null;
            ArrayList<FeesHeaderModel> feesHeaderModels = new ArrayList<>();

            try {
                cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        FeesHeaderModel model = new FeesHeaderModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        model.setTotal_amount(cursor.getLong(cursor.getColumnIndex("fee_amount")));
                        model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
                        feesHeaderModels.add(model);
                    } while (cursor.moveToNext());

                    Date maxDepositDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(maxCreatedOnOfCashDeposit);

                    if(!CollectionUtils.isEmpty(feesHeaderModels)) {

                        // Remove courses that haven’t got the latest date
                        Iterator<FeesHeaderModel> iterator = feesHeaderModels.iterator();
                        while (iterator.hasNext()) {
                            FeesHeaderModel model = iterator.next();
                            if (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(model.getCreatedOn()).before(maxDepositDate)) {
                                iterator.remove();
                            }
                        }

                    }

                    if(!CollectionUtils.isEmpty(feesHeaderModels)) {
                        cashInHand = (long) feesHeaderModels.stream().mapToDouble(FeesHeaderModel::getTotal_amount).sum();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }

        } else {
            if (schoolId > 0) {
                cashInHand = Long.parseLong(getCashInHandData(schoolId + "").getTotal());
            } else {
                cashInHand = Long.parseLong(getCashInHandData(AppModel.getInstance().getAllUserSchoolsForFinance(context)).getTotal());
            }
        }

        return cashInHand;
    }

    public ArrayList<FeesHeaderModel> getCashInHandDataAfterMaxDeposit(int schoolId, int academicSessionId, String maxCreatedOnOfCashDeposit) {
        ArrayList<FeesHeaderModel> feesHeaderModels = new ArrayList<>();
        String updatedMaxCreatedOnOfCashDeposit = AppModel.getInstance().convertDatetoFormat(
                maxCreatedOnOfCashDeposit,
                "yyyy-MM-dd hh:mm:ss a",
                "yyyy-MM-dd");

        String query = " select fd.id, fd.fee_amount, fh." + CREATED_ON + ", fh.id as final_id from FeesDetail fd\n" +
                " INNER JOIN FeesHeader fh ON fh.id = fd.feesHeader_id \n" +
                " INNER JOIN school_class sc ON sc.id = fh.schoolclass_id \n" +
                " WHERE \n" +
                " fh.Category_Id = 2 AND sc.school_id IN (@SchoolID) \n" +
                " and\n" +
                " fh." + CREATED_ON + " > '@MaxDate' " +
//                    " created_on > strftime('%Y-%m-%d', @MaxDate) " +
                " and fh.academic_session_id = @ASId group by fh.id ORDER by fh.created_on";
//                    " and (CashDeposit_id is null or CashDeposit_id = '')"


        if (schoolId > 0) {
            query = query.replace("@SchoolID", schoolId + "");
        } else {
            query = query.replace("@SchoolID", AppModel.getInstance().getAllUserSchoolsForFinance(context));
        }

        query = query.replace("@MaxDate", updatedMaxCreatedOnOfCashDeposit);
        query = query.replace("@ASId", academicSessionId + "");

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    FeesHeaderModel model = new FeesHeaderModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex("final_id")));
                    model.setTotal_amount(cursor.getLong(cursor.getColumnIndex("fee_amount")));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
                    feesHeaderModels.add(model);
                } while (cursor.moveToNext());

                Date maxDepositDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(maxCreatedOnOfCashDeposit);

                if (!CollectionUtils.isEmpty(feesHeaderModels)) {

                    // Remove courses that haven’t got the latest date
                    Iterator<FeesHeaderModel> iterator = feesHeaderModels.iterator();
                    while (iterator.hasNext()) {
                        FeesHeaderModel model = iterator.next();
                        if (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(model.getCreatedOn()).before(maxDepositDate)) {
                            iterator.remove();
                        }
                    }

                }

                return feesHeaderModels;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }


        return feesHeaderModels;
    }

    public String getMaxCreatedOnOfCashDeposit() {
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();

        String query = "select created_on from CashDeposit where school_id in (@SchoolID)";
        query = query.replace("@SchoolID", AppModel.getInstance().getAllUserSchoolsForFinance(context));

        try {
            cursor = db.rawQuery(query, null);
            List<String> createdOnList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    createdOnList.add(AppModel.getInstance()
                            .convertDatetoFormat(cursor.getString(cursor.getColumnIndex(CashDeposit.CREATED_ON)),
                                    "yyyy-MM-dd hh:mm:ss a","yyyy-MM-dd'T'HH:mm:ss"));
                }while (cursor.moveToNext());
            }

            if (createdOnList.size() > 0) {
                Date maxCreatedOnDate = createdOnList.stream().map(u -> AppModel.getInstance().convertStringToDate(u, "yyyy-MM-dd'T'HH:mm:ss")).max(Date::compareTo).get();
                String maxCreatedOn = AppModel.getInstance().convertDateToString(maxCreatedOnDate, "yyyy-MM-dd hh:mm:ss a");
                return maxCreatedOn;
            }

//            query = "select max(created_on) as date from CashDeposit where school_id in (@SchoolID)";
//            cursor = db.rawQuery(query, null);
//            if (cursor.moveToFirst()) {
//                return cursor.getString(cursor.getColumnIndex("date"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";

    }

    public boolean isSchoolForFeeSet(SchoolModel sm) {
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String selectQuery = "SELECT count(st.id) as stCount FROM student st\n" +
                "INNER JOIN school_class sc on sc.id = st.schoolclass_id\n" +
                "WHERE sc.school_id = @SchoolID and sc.is_active = 1 and st.is_active = 1 \n" +
                "and ifnull(st.Actual_Fees,0) = 0";

        selectQuery = selectQuery.replaceAll("@SchoolID",sm.getId()+"");

        try {
            cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()){
                return cursor.getInt(cursor.getColumnIndex("stCount")) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public boolean isMonthlyFeesNeedToAddForSchools(String schoolIds) {
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String selectQuery = "SELECT count(st.id) as stCount FROM student st\n" +
                "INNER JOIN school_class sc on sc.id = st.schoolclass_id\n" +
                "WHERE sc.school_id in (@SchoolID) and sc.is_active = 1 and st.is_active = 1 \n" +
                "and ifnull(st.Actual_Fees,0) = 0";

        selectQuery = selectQuery.replaceAll("@SchoolID",schoolIds);

        try {
            cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()){
                return cursor.getInt(cursor.getColumnIndex("stCount")) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public boolean updateStdMonthlyFees(List<StudentModel> student) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = 0;
        try {
            for (StudentModel sm : student) {
                ContentValues values = new ContentValues();

                values.put(STUDENT_Actual_Fees, sm.getMonthlyfee());
                values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, (String) null);

                int id = DB.update(TABLE_STUDENT, values, ID + " =" + String.valueOf(sm.getId()), null);
                if (id > 0 ){
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return i == student.size();
    }
}

