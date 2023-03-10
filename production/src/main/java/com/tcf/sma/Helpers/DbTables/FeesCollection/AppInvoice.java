package com.tcf.sma.Helpers.DbTables.FeesCollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementUnionModel;
import com.tcf.sma.Models.Fees_Collection.AppInvoiceModel;
import com.tcf.sma.Models.Fees_Collection.FeesCollectionReportModel;
import com.tcf.sma.Models.Fees_Collection.FeesDuesModel;
import com.tcf.sma.Models.Fees_Collection.StudentCollectionReportModel;
import com.tcf.sma.Models.RetrofitModels.SyncCashInvoicesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.ViewReceivables.ViewReceivablesTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammad.haseeb on 11/10/2018.
 * edited by saad saeed on 30/Nov/2018
 * Modified Table Strucure and Name to APP_INVOICE by muhammad.haseeb on 11-29-2018
 */

public class AppInvoice {

    public static final String APP_INVOICE_TABLE = "AppInvoice";
    public static final String ID = "id";
    public static final String SYS_ID = "sys_id";
    public static final String SCHOOL_ID = "school_id";
    public static final String SCHOOL_CLASS_ID = "schoolclass_id";
    public static final String SCHOOL_CLASS_YEAR = "school_year_id";
    public static final String STUDENT_ID = "student_id";
    public static final String CREATED_FROM = "created_from";
    public static final String CREATED_REF_ID = "created_ref_id";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_ON = "created_on";
    public static final String UPLOADED_ON = "uploaded_on";
    public static final String CORRECTION_TYPE = "correction_type";
    public static final String FEES_ADMISSION = "fees_admission";
    public static final String FEES_EXAM = "fees_exam";
    public static final String FEES_TUTION = "fees_tution";
    public static final String FEES_BOOKS = "fees_books";
    public static final String FEES_COPIES = "fees_copies";
    public static final String FEES_UNIFORMS = "fees_uniform";
    public static final String FEES_OTHERS = "fees_others";
    public static final String DEVICE_ID = "device_id";
    public static final String JV_NO = "jv_no";
    public static final String RECEIPT_NO = "receipt_no";
    public static final String DEPOSIT_SLIP_NO = "deposit_slip_no";
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
    public static final String CREATE_APP_INVOICE_TABLE = "CREATE TABLE " + APP_INVOICE_TABLE
            + " (" + ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SYS_ID + "  INTEGER,"
            + SCHOOL_CLASS_ID + "  INTEGER,"
            + SCHOOL_CLASS_YEAR + "  INTEGER,"
            + STUDENT_ID + "  INTEGER,"
            + CREATED_FROM + "  TEXT,"
            + CREATED_BY + "  TEXT,"
            + CREATED_ON + "  TEXT,"
            + UPLOADED_ON + "  TEXT,"
            + FEES_ADMISSION + "  REAL,"
            + FEES_EXAM + "  REAL,"
            + FEES_TUTION + "  REAL,"
            + FEES_BOOKS + "  REAL,"
            + FEES_COPIES + "  REAL,"
            + FEES_UNIFORMS + "  REAL,"
            + FEES_OTHERS + "  REAL,"
            + DEVICE_ID + "  TEXT,"
            + DOWNLOADED_ON + "  TEXT);";
    public static final String GET_ALL_FEES_DUES = "Select * from " + APP_INVOICE_TABLE + " WHERE " + SCHOOL_ID + " =@SchoolID";
    public static final String GET_ALL_APP_INVOICE_FOR_ACCOUNT_STATEMENT = "select * from " + APP_INVOICE_TABLE + " ai inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolID";
    public static final String GET_ACCOUNT_STATEMENT = "select fr.id, fr.schoolclass_id, fr.school_year_id, fr.student_id, 'R' as transaction_type, fr.correction_type as created_from, fr.receipt_no as created_ref_id, fr.fees_admission, fr.fees_exam, fr.fees_tution, fr.fees_books, fr.fees_copies, fr.fees_uniform" +
            "from AppReceipt fr" +
            "inner join school_class sc on sc.id = fr.schoolclass_id and sc.school_id = @SchoolID" +
            "inner join student s on s.schoolclass_id =sc.id and s.student_gr_no = @StudentGrNo" +
            "union " +
            "select fd.id, fd.schoolclass_id, fd.school_year_id, fd.student_id, 'D' as transaction_type, fd.created_from, cast(fd.created_ref_id as text) as created_ref_id, fd.fees_admission, fd.fees_exam, fd.fees_tution, fd.fees_books, fd.fees_copies, fd.fees_uniform" +
            "from AppInvoice fd" +
            "inner join school_class sc on sc.id = fd.schoolclass_id and sc.school_id = @SchoolID" +
            "inner join student s on s.schoolclass_id =sc.id and s.student_gr_no = @StudentGrNo";
    public static final String GET_CASH_IN_HAND_DATA = "SELECT (select created_on from CashDeposit order by created_on desc limit 1) as lastDepositedDate,sum(fees_admission * case when ar.is_correction = 0 then -1 else 1 end)\n" +
            "\t\t\tas fees_admission  ,sum(fees_exam* case when ar.is_correction = 0 then -1 else 1 end)\n" +
            "\t\t\tas fees_exam,sum(fees_tution* case when ar.is_correction = 0 then -1 else 1 end)\n" +
            "\t\t\tas fees_tution  ,sum(fees_books* case when ar.is_correction = 0 then -1 else 1 end)\n" +
            "\t\t\tas fees_books,sum(fees_copies* case when ar.is_correction = 0 then -1 else 1 end)\n" +
            "\t\t\tas fees_copies,sum(fees_uniform * case when ar.is_correction = 0 then -1 else 1 end)\n" +
            "\t\t\tas fees_uniform,sum(fees_others* case when ar.is_correction = 0 then -1 else 1 end) \n" +
            "\t\t\tas fees_others,\n" +
            "\t\t\t(SUM(fees_admission* case when ar.is_correction = 0 then -1 else 1 end) +\n" +
            "\t\t\tSUM(fees_exam* case when ar.is_correction = 0 then -1 else 1 end) + \n" +
            "\t\t\tSUM(fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "\t\t\tSUM(fees_books* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
            "\t\t\tSUM(fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "\t\t\tSUM(fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
            "\t\t\tSUM(fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as 'Total'\n" +
            "\t\t\tfrom AppReceipt ar where ar.created_by  = @UserId and ar.cashdeposit_id IS NULL";
    public static final String GET_CASH_IN_HAND_DATA3 = "SELECT (select created_on " +
            "from CashDeposit " +
            "order by created_on desc limit 1) as lastDepositedDate,sum(fees_admission * case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_admission  ,sum(fees_exam* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_exam,sum(fees_tution* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_tution  ,sum(fees_books* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_books,sum(fees_copies* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_copies,sum(fees_uniform * case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_uniform,sum(fees_others* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_others," +
            "(SUM(fees_admission* case when ar.is_correction = 0 then -1 else 1 end) +" +
            "SUM(fees_exam* case when ar.is_correction = 0 then -1 else 1 end) + " +
            "SUM(fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+ " +
            "SUM(fees_books* case when ar.is_correction = 0 then -1 else 1 end)+ " +
            "SUM(fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ " +
            "SUM(fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+" +
            "SUM(fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as 'Total' " +
            "from AppReceipt ar " +
            "INNER JOIN school_class on ar.schoolclass_id = school_class.id and school_class.school_id = @SchoolId " +
            "and ar.cashdeposit_id IS NULL";
    public static final String GET_CASH_IN_HAND_DATA_FOR_ALL_SCHOOL = "SELECT (select created_on " +
            "from CashDeposit " +
            "order by created_on desc limit 1) as lastDepositedDate,sum(fees_admission * case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_admission  ,sum(fees_exam* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_exam,sum(fees_tution* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_tution  ,sum(fees_books* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_books,sum(fees_copies* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_copies,sum(fees_uniform * case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_uniform,sum(fees_others* case when ar.is_correction = 0 then -1 else 1 end)" +
            " as fees_others," +
            "(SUM(fees_admission* case when ar.is_correction = 0 then -1 else 1 end) +" +
            "SUM(fees_exam* case when ar.is_correction = 0 then -1 else 1 end) + " +
            "SUM(fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+ " +
            "SUM(fees_books* case when ar.is_correction = 0 then -1 else 1 end)+ " +
            "SUM(fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ " +
            "SUM(fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+" +
            "SUM(fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as 'Total' " +
            "from AppReceipt ar " +
            "where ar.cashdeposit_id IS NULL";
    public static final String GET_PREV_RECV = "select 'R' as type, " +
            "sum(ar.fees_admission*-1) as fees_admission, sum(ar.fees_exam*-1) as fees_exam, sum(ar.fees_tution*-1) as fees_tution, sum(ar.fees_books*-1) as fees_books, sum(ar.fees_copies*-1) as fees_copies, sum(ar.fees_uniform*-1) as fees_uniform, sum(ar.fees_others*-1) as fees_others " +
            "from AppReceipt ar left outer join CashDeposit cd on ar.cashdeposit_id = cd.id " +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId " +
            "inner join student s on s.id = ar.student_id and s.student_gr_no = @StudentGRNO " +
            "union " +
            "select 'I' as  type," +
            "sum(ai.fees_admission) as fees_admission, sum(ai.fees_exam) as fees_exam, sum(ai.fees_tution) as fees_tution, sum(ai.fees_books) as fees_books, sum(ai.fees_copies) as fees_copies, sum(ai.fees_uniform) as fees_uniform, sum(ai.fees_others) as fees_others " +
            "from AppInvoice ai inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId inner join class c on c.id = sc.class_id " +
            "inner join section sec on sec.id = sc.section_id inner join student s on s.id =ai.student_id and s.student_gr_no = @StudentGRNO " +
            "; ";
    public static final String GET_PREV_RECV3 = "select sum(fees_admission) as fees_admission, sum(fees_exam) as fees_exam, sum(fees_tution) as fees_tution, sum(fees_books) as fees_books, sum(fees_copies) as fees_copies, sum(fees_uniform) as fees_uniform, sum(fees_others) as fees_others " +
            "from " +
            "(select sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  " +
            "from AppReceipt ar " +
            "left outer join CashDeposit cd on ar.cashdeposit_id = cd.id " +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId " +
            "inner join student s on s.id = ar.student_id and s.student_gr_no = @StudentGRNO " +
            "union " +
            "select " +
            "sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, " +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, " +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, " +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, " +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies, " +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, " +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others " +
            "from AppInvoice ai \n" +
            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId \n" +
            "inner join student s on s.id = ai.student_id and s.student_gr_no = @StudentGRNO \n" +
            ")";
    public static final String GET_RECEIVABLES_DUE = "select sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies, \n" +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others from AppInvoice ai \n" +
            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId ";
    public static final String GET_RECEIVABLES_DUE_FOR_SCHOOLS = "select sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies, \n" +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others from AppInvoice ai ";
    public static final String GET_RECEIVABLES_RECEIVED = "select sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others " +
            "from AppReceipt ar " +
            "left outer join CashDeposit cd on ar.cashdeposit_id = cd.id " +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId ";
    public static final String GET_RECEIVABLES_RECEIVED_FOR_SCHOOLS = "select sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  " +
            "from AppReceipt ar " +
            "left outer join CashDeposit cd on ar.cashdeposit_id = cd.id ";
    public static final String GET_PREV_RECV2 = "select sum(fees_admission) as fees_admission, sum(fees_exam) as fees_exam, sum(fees_tution) as fees_tution, sum(fees_books) as fees_books, sum(fees_copies) as fees_copies, sum(fees_uniform) as fees_uniform, sum(fees_others) as fees_others "
            + "from (" + "select sum(ar.fees_admission*-1) as fees_admission, sum(ar.fees_exam*-1) as fees_exam, sum(ar.fees_tution*-1) as fees_tution, sum(ar.fees_books*-1) as fees_books, sum(ar.fees_copies*-1) as fees_copies, sum(ar.fees_uniform*-1) as fees_uniform, sum(ar.fees_others*-1) as fees_others "
            + "from AppReceipt ar "
            + "left outer join CashDeposit cd on ar.cashdeposit_id = cd.id "
            + "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId "
            + "inner join student s on s.id = ar.student_id and s.student_gr_no = @StudentGRNO "
            + "union "
            + "select sum(ai.fees_admission) as fees_admission, sum(ai.fees_exam) as fees_exam, sum(ai.fees_tution) as fees_tution, sum(ai.fees_books) as fees_books, sum(ai.fees_copies) as fees_copies, sum(ai.fees_uniform) as fees_uniform, sum(ai.fees_others) as fees_others "
            + "from AppInvoice ai "
            + "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId "
            + "inner join student s on s.id = ai.student_id and s.student_gr_no = @StudentGRNO "
            + ");";
    public static final String GET_PREV_MONTH_DUES = "select 'R' as type, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ar.created_on < datetime('now','start of month','-1 month') \n" +
            "group by ar.created_on\n" +
            "union \n" +
            "select 'I' as type, ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
            "from AppInvoice ai \n" +
            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ai.created_on < datetime('now','start of month','-1 month')\n" +
            "group by ai.created_on";
    public static final String GET_THIS_MONTH_COLLECTED = "select 'R' as type, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ar.created_on >= datetime('now','start of month')\n" +
            "and ar.created_on <= datetime('now') \n" +
            "group by ar.created_on\n";
    public static final String GET_PREV_MONTH_COLLECTED = "select 'R' as type, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ar.created_on >= datetime('now','start of month','-1 month')\n" +
            "and ar.created_on <= datetime('now','start of month','-1 day') \n" +
            "group by ar.created_on\n";
    public static final String GET_THIS_MONTH_DUES = "select 'R' as type, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ar.created_on < datetime('now','start of month') \n" +
            "group by ar.created_on\n" +
            "union \n" +
            "select 'I' as type, ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
            "from AppInvoice ai \n" +
            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id =  @SchoolId \n" +
            "where ai.created_on < datetime('now','start of month')\n" +
            "group by ai.created_on";
    public static final String GET_ACAD_YEAR_COLLECTED = "select 'R' as type, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ar.created_on >= '@startDate'\n" +
            "and ar.created_on <= '@endDate' \n" +
            "group by ar.created_on\n";
    public static final String GET_ACAD_YEAR_DUES = "select 'R' as type, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
            "where ar.created_on < '@startDate' \n" +
            "group by ar.created_on\n" +
            "union \n" +
            "select 'I' as type, ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
            "from AppInvoice ai \n" +
            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id =  @SchoolId \n" +
            "where ai.created_on < '@startDate'\n" +
            "group by ai.created_on";
//            "union \n" +
//            "select 'I' as type, ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
//            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
//            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
//            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
//            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
//            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
//            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
//            "from AppInvoice ai \n" +
//            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId \n" +
//            "where ai.created_on >= datetime('now','start of month')\n" +
//            "and ai.created_on <= datetime('now') \n" +
//            "group by ai.created_on";
    public static final String TOTAL_FEES = "Select SUM(ft.fees_admission + \n" +
            "\t\tft.fees_exam +\n" +
            "\t\tft.fees_tution +\n" +
            "\t\tft.fees_books +\n" +
            "\t\tft.fees_copies +\n" +
            "\t\tft.fees_uniform)";
//            "union \n" +
//            "select 'I' as type, ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
//            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
//            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
//            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
//            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
//            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
//            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
//            "from AppInvoice ai \n" +
//            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId \n" +
//            "where ai.created_on >= datetime('now','start of month','-1 month')\n" +
//            "and ai.created_on <= datetime('now','start of month','-1 day') \n" +
//            "group by ai.created_on";
    // GET TOTAL FEES COLLECTED
    public static final String TOTAL_FEES_FT = "Select SUM(ft.fees_admission + \n" +
            "\t\tft.fees_exam +\n" +
            "\t\tft.fees_tution +\n" +
            "\t\tft.fees_books +\n" +
            "\t\tft.fees_copies +\n" +
            "\t\tft.fees_uniform)";
    // GET TOTAL FEES COLLECTED
    public static final String TOTAL_FEES_FR = "Select SUM(fr.fees_admission + \n" +
            "\t\tfr.fees_exam +\n" +
            "\t\tfr.fees_tution +\n" +
            "\t\tfr.fees_books +\n" +
            "\t\tfr.fees_copies +\n" +
            "\t\tfr.fees_uniform)";
    public static final String SPECIFIC_FEES = "Select SUM(ft.@column_name)";
    public static final String SPECIFIC_FEES_FT = "Select SUM(ft.@column_name)";
    public static final String SPECIFIC_FEES_FR = "Select SUM(fr.@column_name)";
    public static final String GET_TOTAL_FEES_COLLECTED_THIS_MONTH = " as 'AppReceipt This Month Dues'\n" +
            "\t\tFrom " + APP_INVOICE_TABLE + " ft\n" +
            "\t\tWhere ft.created_on >= date('now','start of month')";
    public static final String GET_TOTAL_FEES_COLLECTED_PREVIOUS_MONTH = " as 'AppReceipt Previous Month Dues'\n" +
            "\t\tFrom " + APP_INVOICE_TABLE + " ft\n" +
            "\t\tWhere ft.created_on >= date('now','start of month', '-1 month')\n" +
            "\t\tand ft.created_on < date('now','start of month')";

    // this is for clarity and to avoid helper class from being overloaded with code...
    public static final String GET_TOTAL_FEES_COLLECTED_THIS_YEAR = " as 'AppReceipt This Year Dues'\n" +
            "\t\tFrom " + APP_INVOICE_TABLE + " ft\n" +
            "\t\tWhere ft.created_on >= date('now','start of year');";
    public static final String GET_TOTAL_FEES_COLLECTED_PREVIOUS_YEAR = " as 'AppReceipt Previous Year Dues'\n" +
            "\t\tFrom " + APP_INVOICE_TABLE + " ft\n" +
            "\t\tWhere ft.created_on >= date('now','start of year', '-1 year') and\n" +
            "\t\tft.created_on < date('now','start of year')";
    // Graph Data Query
    public static final String GET_GRAPH_FEES_TOTAL = "select SUM(fees_admission+ fees_exam+ fees_tution+ fees_books+ fees_copies+ fees_uniform) as Total, \n" +
            "       strftime(\"%m-%Y\", AppInvoice.created_on) as 'Month' \n" +
            "       from " + APP_INVOICE_TABLE + " group by strftime(\"%m-%Y\", AppInvoice.created_on) ORDER BY Month ASC";
    // Graph Data Query
    public static final String GET_GRAPH_FEES_TOTAL_v1 = " as Total, \n" +
            "       strftime(\"%m-%Y\", fr.created_on) as 'Month', 'Collected' as Status\n" +
            "       from AppReceipt fr group by strftime(\"%m-%Y\", fr.created_on) ";
    // Graph Data Query
    public static final String GET_GRAPH_FEES_TOTAL_v2 = " as Total, \n" +
            "       strftime(\"%m-%Y\", ft.created_on) as 'Month' , 'Dues' as Status\n" +
            "       from AppInvoice ft group by strftime(\"%m-%Y\", ft.created_on)\n" +
            "\t   ORDER BY Month ASC";
    private static final String GET_GRAPH_DUES =
            "select 'I' as type,strftime('%Y-%m', datetime(replace(replace(ai.created_on,'AM',''),'PM',''))) as rep_month,ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
                    "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
                    "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
                    "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
                    "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
                    "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
                    "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
                    "from AppInvoice ai \n" +
                    "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId\n" +
                    "where ai.created_on >= '@fromDate 00:00:00 AM' and ai.created_on <= '@toDate 23:59:59 PM'\n" +
                    "group by rep_month\n";
    private static final String GET_GRAPH_COLLECTED = "select 'R' as type,strftime('%Y-%m', datetime(replace(replace(ar.created_on,'AM',''),'PM',''))) as rep_month,ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId\n" +
            "where ar.created_on >= '@fromDate 00:00:00 AM'  \n" +
            "and ar.created_on <= '@toDate 23:59:59 PM'\n" + " group by rep_month\n";
    private static final String STUDENT_COLLECTION_REPORT_QUERY = "select duecoll.student_gr_no,duecoll.student_name,duecoll.classId,duecoll.secId,duecoll.className,duecoll.secName,sum(duecoll.dues) as dues,sum(duecoll.collected) as collected from \n" +
            "(\n" +
            "select student_gr_no,student_name,classId,secId,className,secName,\n" +
            "sum(fees_uniform)+sum(fees_exam)+sum(fees_tution)+sum(fees_books)+sum(fees_copies)+sum(fees_others) as dues,0 as collected\n" +
            "from (select 'R' as type, s.student_gr_no, s.student_name,c.id as classId,sec.id as secId, c.name as className,sec.name as secName, ar.created_on, sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end) as fees_admission, \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end) as fees_exam, \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end) as fees_tution, \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end) as fees_books, \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end) as fees_copies, \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end) as fees_uniform, \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end) as fees_others  \n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId\n" +
            "inner join class c on c.id = sc.class_id \n" +
            "inner join section sec on sec.id = sc.section_id \n" +
            "inner join student s on s.id =ar.student_id\n" +
            "group by s.student_gr_no\n" +
            "union \n" +
            "select 'R' as type,s.student_gr_no,s.student_name,c.id as classId,sec.id as secId,c.name as className,sec.name as secName, ai.created_on, sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_admission, \n" +
            "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_exam, \n" +
            "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end) as fees_tution, \n" +
            "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_books, \n" +
            "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_copies,\n" +
            "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_uniform, \n" +
            "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end) as fees_others \n" +
            "from AppInvoice ai \n" +
            "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId\n" +
            "inner join class c on c.id = sc.class_id \n" +
            " inner join section sec on sec.id = sc.section_id  \n" +
            "inner join student s on s.id =ai.student_id\n" +
            "group by s.student_gr_no\n" +
            ") \n" +
            "group by student_gr_no\n" +
            "union \n" +
            "select s.student_gr_no, s.student_name, c.id as classId,sec.id as secId,c.name as className,sec.name as secName, 0 as dues, (sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
            "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as collected\n" +
            "from AppReceipt ar \n" +
            "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId\n" +
            "inner join class c on c.id = sc.class_id \n" +
            "inner join section sec on sec.id = sc.section_id \n" +
            "inner join student s on s.id =ar.student_id\n" +
            "group by s.student_gr_no\n" +
            ") \n" +
            "duecoll\n" +
            "group by duecoll.student_gr_no\n" +
            "order by cast(student_gr_no as integer) \n";
    private static AppInvoice instance = null;
    private Context context;

    private AppInvoice(Context context) {
        this.context = context;
    }

    public static AppInvoice getInstance(Context context) {
        if (instance == null)
            instance = new AppInvoice(context);

        return instance;
    }

    public void insertRawAppInvoice(SQLiteDatabase db) {

        String sql = "INSERT INTO " + APP_INVOICE_TABLE + " ("
                + SYS_ID + ","
                + SCHOOL_CLASS_ID + ","
                + SCHOOL_CLASS_YEAR + ","
                + STUDENT_ID + ","
                + CREATED_FROM + ","
                + CREATED_BY + ","
                + CREATED_ON + ","
                + UPLOADED_ON + ","
                + FEES_ADMISSION + ","
                + FEES_EXAM + ","
                + FEES_TUTION + ","
                + FEES_BOOKS + ","
                + FEES_COPIES + ","
                + FEES_UNIFORMS + ","
                + FEES_OTHERS + ","
                + DEVICE_ID + ","
                + DOWNLOADED_ON + ") VALUES ";


        sql += "(121,16337,1,474625,'T','1112','2018-12-02',NULL,120.0,50.0,NULL,80.0,130,NULL,60.0,'20181202',NULL),";
        sql += "(122,16337,1,474625,'E','1112','2018-12-02',NULL,NULL,20.0,54.0,90.0,90,150,60.0,'20181202',NULL),";
        sql += "(123,16337,1,474625,'A','1112','2018-12-02',NULL,130.0,NULL,85,70.0,85,49,45,'20181202',NULL),";
        sql += "(124,16337,1,474625,'R','1112','2018-12-02',NULL,150.0,30.0,160,40.0,NULL,85,60.0,'20181202',NULL),";
        sql += "(125,16337,1,474625,'W','1112','2018-12-02',NULL,107.0,NULL,NULL,85.0,NULL,50,NULL,'20181202',NULL),";
        sql += "(126,16337,1,474625,'T','1112','2018-12-02',NULL,321.0,40.0,250,NULL,64,NULL,60.0,'20181202',NULL),";
        sql += "(127,16337,1,474625,'W','1112','2018-12-02',NULL,45.0,NULL,NULL,80.0,135,135,NULL,'20181202',NULL),";
        sql += "(128,16337,1,474625,'R','1112','2018-12-02',NULL,1250.0,50.0,45,NULL,NULL,160,60.0,'20181202',NULL),";
        sql += "(129,16337,1,474625,'A','1112','2018-12-02',NULL,105.0,60.0,NULL,80.0,185,155,NULL,'20181202',NULL),";
        sql += "(130,16337,1,474625,'A','1112','2018-12-02',NULL,68.0,90.0,75,NULL,35,NULL,60.0,'20181202',NULL),";
        sql += "(131,16337,1,474625,'R','1112','2018-12-02',NULL,230.0,NULL,NULL,80.0,NULL,175,60.0,'20181202',NULL),";
        sql += "(132,16337,1,474625,'W','1112','2018-12-02',NULL,170.0,30.0,69,NULL,120,200,60.0,'20181202',NULL);";

        db.execSQL(sql);
    }

    public long insertWavier(FeesDuesModel feesDuesModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppInvoice.STUDENT_ID, feesDuesModel.getStudent_id());
            cv.put(AppInvoice.FEES_EXAM, feesDuesModel.getFees_exam());
            cv.put(AppInvoice.FEES_ADMISSION, feesDuesModel.getFees_admission());
            cv.put(AppInvoice.FEES_TUTION, feesDuesModel.getFees_tution());
            cv.put(AppInvoice.FEES_BOOKS, feesDuesModel.getFees_books());
            cv.put(AppInvoice.FEES_COPIES, feesDuesModel.getFees_copies());
            cv.put(AppInvoice.SCHOOL_CLASS_YEAR, feesDuesModel.getSchool_year_id());
            cv.put(AppInvoice.FEES_OTHERS, feesDuesModel.getFees_other());
            cv.put(AppInvoice.FEES_UNIFORMS, feesDuesModel.getFees_uniform());
            cv.put(AppInvoice.CREATED_ON, feesDuesModel.getCreated_on());
            cv.put(AppInvoice.CREATED_BY, feesDuesModel.getCreated_by());
            cv.put(AppInvoice.CREATED_FROM, feesDuesModel.getCreated_from());
            cv.put(AppInvoice.SCHOOL_CLASS_ID, feesDuesModel.getSchoolclass_id());
            cv.put(AppInvoice.DEVICE_ID, feesDuesModel.getDevice_id());

//            cv.put(AppInvoice.CREATED_REF_ID, feesDuesModel.getCreated_ref_id());

            id = db.insertOrThrow(AppInvoice.APP_INVOICE_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public long insertFeesReceipt(FeesDuesModel feesDuesModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppInvoice.STUDENT_ID, feesDuesModel.getStudent_id());
            cv.put(AppInvoice.FEES_BOOKS, feesDuesModel.getFees_books());
            cv.put(AppInvoice.FEES_COPIES, feesDuesModel.getFees_copies());
            cv.put(AppInvoice.FEES_OTHERS, feesDuesModel.getFees_other());
            cv.put(AppInvoice.SCHOOL_CLASS_YEAR, feesDuesModel.getSchool_year_id());
            cv.put(AppInvoice.FEES_UNIFORMS, feesDuesModel.getFees_uniform());
            cv.put(AppInvoice.CREATED_ON, feesDuesModel.getCreated_on());
            cv.put(AppInvoice.CREATED_BY, feesDuesModel.getCreated_by());
            cv.put(AppInvoice.CREATED_FROM, feesDuesModel.getCreated_from());
            cv.put(AppInvoice.SCHOOL_CLASS_ID, feesDuesModel.getSchoolclass_id());
            cv.put(AppInvoice.DEVICE_ID, feesDuesModel.getDevice_id());

//            cv.put(AppInvoice.CREATED_REF_ID, feesDuesModel.getCreated_ref_id());

            id = db.insertOrThrow(AppInvoice.APP_INVOICE_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public List<AppInvoiceModel> getAllAppInvoiceForAccountStatement(int schoolId, String grNo, String fromDate, String toDate) {
        List<AppInvoiceModel> aimList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String query = AppInvoice.GET_ALL_APP_INVOICE_FOR_ACCOUNT_STATEMENT;
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

                    AppInvoiceModel model = new AppInvoiceModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(AppInvoice.ID)));
                    model.setSysId(cursor.getInt(cursor.getColumnIndex(AppInvoice.SYS_ID)));
                    model.setSchoolclassId(cursor.getInt(cursor.getColumnIndex(AppInvoice.SCHOOL_CLASS_ID)));
                    model.setSchoolYearId(cursor.getInt(cursor.getColumnIndex(AppInvoice.SCHOOL_CLASS_YEAR)));
                    model.setStudentId(cursor.getInt(cursor.getColumnIndex(AppInvoice.STUDENT_ID)));
                    model.setCreatedFrom(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_FROM)));
                    model.setCreatedby(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_BY)));
                    model.setCreatedOn(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_ON)));
                    model.setUploadedOn(cursor.getString(cursor.getColumnIndex(AppInvoice.UPLOADED_ON)));
                    model.setFees_admission(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_ADMISSION)));
                    model.setFees_exam(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_EXAM)));
                    model.setFees_tution(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_TUTION)));
                    model.setFees_books(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_BOOKS)));
                    model.setFees_copies(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_COPIES)));
                    model.setFees_uniform(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_UNIFORMS)));
                    model.setFees_others(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_OTHERS)));
                    model.setDevice_id(cursor.getString(cursor.getColumnIndex(AppInvoice.DEVICE_ID)));
                    model.setDownloadedOn(cursor.getString(cursor.getColumnIndex(AppInvoice.DOWNLOADED_ON)));

                    aimList.add(model);
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
        return aimList;
    }

    public List<AccountStatementUnionModel> getAccountStatement(int schoolId, String grNo, String fromDate, String toDate, String depSlipNo, String receiptNo) {
        List<AccountStatementUnionModel> asmList = new ArrayList<>();
        Cursor cursor = null;
        String q1 = "", q2 = "";
        if (grNo != null && !grNo.isEmpty()) {
            q1 = "select s.student_name as student_name,s.student_gr_no as student_grNo,s.father_name as father_name,c.name as className,sec.name as sectionName,ar.id as id,ar.sys_id as sys_id, ar.schoolclass_id as schoolclass_id, ar.school_year_id as school_year_id, ar.student_id as student_id, 'R' as transaction_type, cast(ar.is_correction as text)as created_from, ar.created_on as created_on,(ar.fees_admission *-1) as fees_admission , (ar.fees_exam*-1) as fees_exam, (ar.fees_tution*-1) as fees_tution, (ar.fees_books*-1) as fees_books, (ar.fees_copies*-1) as fees_copies, (ar.fees_uniform*-1) as fees_uniform,(ar.fees_others*-1) as fees_others,cd.deposit_slip_no as deposit_slip_no,ar.receipt_no as receipt_no,cd.created_on as deposit_date " +
                    "from AppReceipt ar " +
                    "left outer join CashDeposit cd on ar.cashdeposit_id = cd.id " +
                    "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolID ";

            q2 = "select s.student_name as student_name,s.student_gr_no as student_grNo,s.father_name as father_name,c.name as className,sec.name as sectionName,ai.id as id,ai.sys_id as sys_id, ai.schoolclass_id as schoolclass_id, ai.school_year_id as school_year_id, ai.student_id as student_id, 'I' as transaction_type, ai.created_from as created_from, ai.created_on as created_on,ai.fees_admission, ai.fees_exam, ai.fees_tution, ai.fees_books, ai.fees_copies, ai.fees_uniform as fees_uniform,ai.fees_others ,null as deposit_slip_no,null as receipt_no,null as deposit_date " +
                    "from AppInvoice ai " +
                    "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolID ";

            q1 = q1 + "inner join class c on c.id = sc.class_id " +
                    "inner join section sec on sec.id = sc.section_id " +
                    "inner join student s on s.id =ar.student_id and s.student_gr_no = @StudentGrNo ";

            q2 = q2 + "inner join class c on c.id = sc.class_id " +
                    "inner join section sec on sec.id = sc.section_id " +
                    "inner join student s on s.id =ai.student_id and s.student_gr_no = @StudentGrNo ";
            q1 = q1.replace("@StudentGrNo", grNo);
            q2 = q2.replace("@StudentGrNo", grNo);

        } else {
            q1 = "select s.student_gr_no as student_grNo,ar.id as id,ar.sys_id as sys_id, ar.schoolclass_id as schoolclass_id, ar.school_year_id as school_year_id, ar.student_id as student_id, 'R' as transaction_type, cast(ar.is_correction as text)as created_from, ar.created_on as created_on,(ar.fees_admission *-1) as fees_admission , (ar.fees_exam*-1) as fees_exam, (ar.fees_tution*-1) as fees_tution, (ar.fees_books*-1) as fees_books, (ar.fees_copies*-1) as fees_copies, (ar.fees_uniform*-1) as fees_uniform,(ar.fees_others*-1) as fees_others,cd.deposit_slip_no as deposit_slip_no,ar.receipt_no as receipt_no,cd.created_on as deposit_date " +
                    "from AppReceipt ar " +
                    "left outer join CashDeposit cd on ar.cashdeposit_id = cd.id " +
                    "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolID " +
                    "\n" +
                    "inner join student s on s.id =ar.student_id ";


            q2 = "select s.student_gr_no as student_grNo,ai.id as id,ai.sys_id as sys_id, ai.schoolclass_id as schoolclass_id, ai.school_year_id as school_year_id, ai.student_id as student_id, 'I' as transaction_type, ai.created_from as created_from, ai.created_on as created_on,ai.fees_admission, ai.fees_exam, ai.fees_tution, ai.fees_books, ai.fees_copies, ai.fees_uniform as fees_uniform,ai.fees_others ,null as deposit_slip_no,null as receipt_no,null as deposit_date " +
                    "from AppInvoice ai " +
                    "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolID " +
                    "\n" +
                    " inner join student s on s.id =ai.student_id ";

        }
        q1 = q1.replace("@SchoolID", String.valueOf(schoolId));
        q2 = q2.replace("@SchoolID", String.valueOf(schoolId));

//
//        if (grNo != null && !grNo.isEmpty()) {
//            q1 = q1 + "inner join class c on c.id = sc.class_id " +
//                    "inner join section sec on sec.id = sc.section_id " +
//                    "inner join student s on s.id =ar.student_id and s.student_gr_no = @StudentGrNo ";
//
//            q2 = q2 + "inner join class c on c.id = sc.class_id " +
//                    "inner join section sec on sec.id = sc.section_id " +
//                    "inner join student s on s.id =ar.student_id and s.student_gr_no = @StudentGrNo ";
//            q1 = q1.replace("@StudentGrNo", grNo);
//            q2 = q2.replace("@StudentGrNo", grNo);
//        }


        if ((fromDate != null && toDate != null) && (!fromDate.isEmpty() && !toDate.isEmpty())) {
            q1 = q1 + "where ar.created_on between '@FromDate 00:00:00 AM' and '@ToDate 23:59:59 PM'";

            q2 = q2 + "where ai.created_on between '@FromDate 00:00:00 AM' and '@ToDate 23:59:59 PM'";

            q1 = q1.replace("@FromDate", fromDate);
            q1 = q1.replace("@ToDate", toDate);
            q2 = q2.replace("@FromDate", fromDate);
            q2 = q2.replace("@ToDate", toDate);
        }

        if (!depSlipNo.isEmpty()) {
            q1 = q1 + " and deposit_slip_no = @DepSlipNo";
            q2 = q2 + " and deposit_slip_no = @DepSlipNo";

            q1 = q1.replace("@DepSlipNo", depSlipNo);
            q2 = q2.replace("@DepSlipNo", depSlipNo);
        }
        if (!receiptNo.isEmpty()) {
            q1 = q1 + " and receipt_no = @RNo";
            q2 = q2 + " and receipt_no = @RNo";

            q1 = q1.replace("@RNo", receiptNo);
            q2 = q2.replace("@RNo", receiptNo);
        }

        String query = q1.concat(" union " + q2);

        query += " order by created_on asc";

        Log.d("Account statemnt Queery", query);
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    try {
                        AccountStatementUnionModel model = new AccountStatementUnionModel();
                        if (grNo != null && !grNo.isEmpty()) {
                            model.setStudentName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_NAME)));
                            model.setFatherName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.getInstance(context).STUDENT_FATHERS_NAME)));
                            model.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                            model.setSection(cursor.getString(cursor.getColumnIndex("sectionName")));
                        }
                        model.setId(cursor.getInt(cursor.getColumnIndex(AppInvoice.ID)));
                        model.setSysId(cursor.getInt(cursor.getColumnIndex(AppInvoice.SYS_ID)));
                        model.setSchoolclass_id(cursor.getInt(cursor.getColumnIndex(AppInvoice.SCHOOL_CLASS_ID)));
                        model.setSchool_year_id(cursor.getInt(cursor.getColumnIndex(AppInvoice.SCHOOL_CLASS_YEAR)));
                        model.setStudent_id(cursor.getInt(cursor.getColumnIndex(AppInvoice.STUDENT_ID)));
                        model.setGrNo(cursor.getString(cursor.getColumnIndex("student_grNo")));
                        model.setTransaction_type(cursor.getString(cursor.getColumnIndex("transaction_type")));
                        model.setCreated_from(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_FROM)));
                        model.setCreated_on(AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex(AppInvoice.CREATED_ON)), "yyyy-MM-dd", "dd-MM-yy"));
                        model.setFees_admission(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_ADMISSION)));
                        model.setFees_exam(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_EXAM)));
                        model.setFees_tution(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_TUTION)));
                        model.setFees_books(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_BOOKS)));
                        model.setFees_copies(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_COPIES)));
                        model.setFees_uniform(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_UNIFORMS)));
                        model.setFees_others(cursor.getDouble(cursor.getColumnIndex(AppInvoice.FEES_OTHERS)));
                        model.setReceiptNo(cursor.getString(cursor.getColumnIndex("receipt_no")));
                        model.setDeposit_slip_no(cursor.getInt(cursor.getColumnIndex("deposit_slip_no")));
                        model.setDeposit_date(AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex("deposit_date")), "yyyy-MM-dd", "dd-MM-yy"));
                        asmList.add(model);
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

    public int getMaxSysId() {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(sys_id) as sys_id from " + APP_INVOICE_TABLE;
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

    public long insertDownloaedCashInvoices(FeesDuesModel feesDuesModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        long id = -1;

        try {
            ContentValues cv = new ContentValues();
            cv.put(AppInvoice.STUDENT_ID, feesDuesModel.getStudent_id());
            cv.put(AppInvoice.FEES_ADMISSION, feesDuesModel.getFees_admission());
            cv.put(AppInvoice.FEES_TUTION, feesDuesModel.getFees_tution());
            cv.put(AppInvoice.FEES_EXAM, feesDuesModel.getFees_exam());
            cv.put(AppInvoice.FEES_BOOKS, feesDuesModel.getFees_books());
            cv.put(AppInvoice.FEES_COPIES, feesDuesModel.getFees_copies());
            cv.put(AppInvoice.FEES_OTHERS, feesDuesModel.getFees_other());
            cv.put(AppInvoice.SCHOOL_CLASS_YEAR, feesDuesModel.getSchool_year_id());
            cv.put(AppInvoice.FEES_UNIFORMS, feesDuesModel.getFees_uniform());
            cv.put(AppInvoice.UPLOADED_ON, feesDuesModel.getUploaded_on());
            cv.put(AppInvoice.CREATED_ON, feesDuesModel.getCreated_on());
            cv.put(AppInvoice.SYS_ID, feesDuesModel.getSys_id());
            cv.put(AppInvoice.CREATED_BY, feesDuesModel.getCreated_by());
            cv.put(AppInvoice.CREATED_FROM, feesDuesModel.getCreated_from());
            cv.put(AppInvoice.SCHOOL_CLASS_ID, feesDuesModel.getSchoolclass_id());
            cv.put(AppInvoice.DEVICE_ID, feesDuesModel.getDevice_id());
            cv.put(AppInvoice.DOWNLOADED_ON, feesDuesModel.getDownloadedOn());

            id = db.insert(AppInvoice.APP_INVOICE_TABLE, null, cv);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    public List<String> getPrevRecv(String schoolId, String grNo) {
        List<String> prevRecv = new ArrayList<>();
        String query = AppInvoice.GET_PREV_RECV3;
        query = query.replace("@SchoolId", schoolId);
        query = query.replace("@StudentGRNO", grNo);

        Log.d("Prev Recv Query", query);
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                prevRecv.add(c.getString(c.getColumnIndex(FEES_ADMISSION)));//fees_admission
                prevRecv.add(c.getString(c.getColumnIndex(FEES_EXAM))); // fees_exam
                prevRecv.add(c.getString(c.getColumnIndex(FEES_TUTION))); // fees_tutionfees
                prevRecv.add(c.getString(c.getColumnIndex(FEES_BOOKS))); // fees_books
                prevRecv.add(c.getString(c.getColumnIndex(FEES_COPIES))); // fee_copies
                prevRecv.add(c.getString(c.getColumnIndex(FEES_UNIFORMS))); // fees_uniform
                prevRecv.add(c.getString(c.getColumnIndex(FEES_OTHERS))); // fees_others
            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
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

    public List<String> getDashReceivablesDues(int schoolId) {
        List<String> prevRecv = new ArrayList<>();
        String query;
        if (schoolId > 0) {

            query = AppInvoice.GET_RECEIVABLES_DUE;
            query = query.replace("@SchoolId", "" + schoolId);
        } else {
            query = AppInvoice.GET_RECEIVABLES_DUE_FOR_SCHOOLS;
        }
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                prevRecv.add(c.getString(c.getColumnIndex(FEES_ADMISSION)));//fees_admission
                prevRecv.add(c.getString(c.getColumnIndex(FEES_EXAM))); // fees_exam
                prevRecv.add(c.getString(c.getColumnIndex(FEES_TUTION))); // fees_tutionfees
                prevRecv.add(c.getString(c.getColumnIndex(FEES_BOOKS))); // fees_books
                prevRecv.add(c.getString(c.getColumnIndex(FEES_COPIES))); // fee_copies
                prevRecv.add(c.getString(c.getColumnIndex(FEES_UNIFORMS))); // fees_uniform
                prevRecv.add(c.getString(c.getColumnIndex(FEES_OTHERS))); // fees_others
            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
            }

            return prevRecv;

        } catch (Exception e) {
            e.printStackTrace();
            return prevRecv;
        }
    }


    public List<String> getLast30DaysForAverageFeesCollection(int schoolId) {
        List<String> prevRecv = new ArrayList<>();
        String query;
        if (schoolId > 0) {

            query = AppInvoice.GET_RECEIVABLES_RECEIVED;
            query = query + "where ar.created_on  >= datetime('now','-1 month')";
            query = query.replace("@SchoolId", "" + schoolId);
        } else {
            query = AppInvoice.GET_RECEIVABLES_RECEIVED_FOR_SCHOOLS;
            query = query + "where ar.created_on  >= datetime('now','-1 month')";
        }
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                prevRecv.add(c.getString(c.getColumnIndex(FEES_ADMISSION)));//fees_admission
                prevRecv.add(c.getString(c.getColumnIndex(FEES_EXAM))); // fees_exam
                prevRecv.add(c.getString(c.getColumnIndex(FEES_TUTION))); // fees_tutionfees
                prevRecv.add(c.getString(c.getColumnIndex(FEES_BOOKS))); // fees_books
                prevRecv.add(c.getString(c.getColumnIndex(FEES_COPIES))); // fee_copies
                prevRecv.add(c.getString(c.getColumnIndex(FEES_UNIFORMS))); // fees_uniform
                prevRecv.add(c.getString(c.getColumnIndex(FEES_OTHERS))); // fees_others
            } else {
                //else just send a list which contains empty string. Populate the appropiate items.
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
                prevRecv.add("");
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


    public List<SyncCashInvoicesModel> getAllAppInvoicesForUpload() {
        List<SyncCashInvoicesModel> armList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = "select * from " + APP_INVOICE_TABLE + " where " + UPLOADED_ON
                + " is null and " + SYS_ID + "= 0 or " + SYS_ID + " is null";
        Log.d("Upload query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    SyncCashInvoicesModel model = new SyncCashInvoicesModel();

                    model.setId(cursor.getInt(cursor.getColumnIndex(AppReceipt.ID)));
                    model.setSchoolclass_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_ID)));
                    model.setSchool_year_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_YEAR)));
                    model.setStudent_id(cursor.getInt(cursor.getColumnIndex(STUDENT_ID)));
                    model.setCreated_from(cursor.getString(cursor.getColumnIndex(CREATED_FROM)));
                    model.setCreated_by(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                    model.setCreated_on(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
                    model.setFees_admission(cursor.getInt(cursor.getColumnIndex(FEES_ADMISSION)));
                    model.setFees_exam(cursor.getInt(cursor.getColumnIndex(FEES_EXAM)));
                    model.setFees_tution(cursor.getInt(cursor.getColumnIndex(FEES_TUTION)));
                    model.setFees_books(cursor.getInt(cursor.getColumnIndex(FEES_BOOKS)));
                    model.setFees_copies(cursor.getInt(cursor.getColumnIndex(FEES_COPIES)));
                    model.setFees_uniform(cursor.getInt(cursor.getColumnIndex(FEES_UNIFORMS)));
                    model.setFees_others(cursor.getInt(cursor.getColumnIndex(FEES_OTHERS)));
                    model.setDevice_id(cursor.getString(cursor.getColumnIndex(DEVICE_ID)));

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


    public void genericUpdateMethod(ContentValues values, String deviceId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        try {
            int id = db.update(APP_INVOICE_TABLE, values, AppReceipt.ID + " = " + deviceId, null);
            Log.d("appReceipt", "" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public SchoolModel getSchoolForCurrentAcademicYear(int schoolId) {
        SQLiteDatabase db = null;

        Cursor cursor = null;

        String query = "SELECT s.* FROM school s \n" +
                "INNER JOIN user_school us ON us.school_id= s.id\n" +
                "where s.start_date <= datetime('now') and s.end_date >= datetime('now') and s.id = " + schoolId;
        SchoolModel sm = new SchoolModel();
        Log.d("CurrAcadSchool query", query);

        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {

                sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_ACADEMIC_SESSION)));
                sm.setStart_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_START_DATE)));
                sm.setEnd_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_END_DATE)));

                return sm;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sm;
    }

    public SchoolModel getSchoolForPrevAcademicYear(int schoolId) {
        SQLiteDatabase db = null;

        Cursor cursor = null;

        String query = "SELECT s.start_date,s.end_date,s.academic_session FROM school s INNER JOIN user_school us ON us.school_id= s.id where s.start_date < datetime('now') and end_date < datetime('now') and s.id = " + schoolId;

        SchoolModel sm = new SchoolModel();
        Log.d("PrevAcadSchool query", query);

        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_ACADEMIC_SESSION)));
                sm.setStart_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_START_DATE)));
                sm.setEnd_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_END_DATE)));
                return sm;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sm;
    }


    public List<ViewReceivablesTableModel> getViewRecvAcad(int schoolId, int filter, String startDate, String endDate) {

        // 0 for academic year dues
        // 1 for academic year collected

        List<ViewReceivablesTableModel> models = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = "";

        if (filter == 0)
            query = GET_ACAD_YEAR_DUES;
        else if (filter == 1)
            query = GET_ACAD_YEAR_COLLECTED;

        query = query.replace("@SchoolId", schoolId + "");
        query = query.replace("@startDate", startDate + "");
        query = query.replace("@endDate", endDate + "");

        Log.d("getViewRecvAcad query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    ViewReceivablesTableModel model = new ViewReceivablesTableModel();
                    model.setType(cursor.getString(cursor.getColumnIndex("type")));
//                    model.setCreated_on(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
                    model.setFees_admission(cursor.getDouble(cursor.getColumnIndex(FEES_ADMISSION)));
                    model.setFees_exam(cursor.getDouble(cursor.getColumnIndex(FEES_EXAM)));
                    model.setFees_tution(cursor.getDouble(cursor.getColumnIndex(FEES_TUTION)));
                    model.setFees_books(cursor.getDouble(cursor.getColumnIndex(FEES_BOOKS)));
                    model.setFees_copies(cursor.getDouble(cursor.getColumnIndex(FEES_COPIES)));
                    model.setFees_uniform(cursor.getDouble(cursor.getColumnIndex(FEES_UNIFORMS)));
                    model.setFees_others(cursor.getDouble(cursor.getColumnIndex(FEES_OTHERS)));

                    model.setTotal(model.getFees_admission() + model.getFees_exam() + model.getFees_copies() + model.getFees_books()
                            + model.getFees_others() + model.getFees_tution() + model.getFees_uniform());

                    models.add(model);
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
        return models;

    }

    public List<FeesCollectionReportModel> getViewRecvFromAcadSession(int schoolId, String startDate, String endDate) {

        List<FeesCollectionReportModel> duesCollectedList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = "";

//        if (filter == 0)
//            query = "select month,sum(dues) as dues,sum(collected) as collected from (select month,sum(dues) as dues, null as collected from ( select 'R' as type,strftime('%Y-%m', datetime(replace(replace(ar.created_on,'AM',''),'PM',''))) as month,ar.created_on,\n" +
//                    "( sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
//                    "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as dues\n" +
//                    "from AppReceipt ar  \n" +
//                    "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
//                    "where ar.created_on >= '@startDate'\n" +
//                    "and ar.created_on <= '@endDate' \n" +
//                    " group by month\n" +
//                    "union \n" +
//                    "select 'I' as type,strftime('%Y-%m', datetime(replace(replace(ai.created_on,'AM',''),'PM',''))) as month,ai.created_on, (sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end)+\n" +
//                    "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end)+ \n" +
//                    "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end)+\n" +
//                    "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)+\n" +
//                    "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)+\n" +
//                    "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)+\n" +
//                    "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)) as dues\n" +
//                    "from AppInvoice ai \n" +
//                    "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId \n" +
//                    "where ai.created_on >= '@startDate'\n" +
//                    "and ai.created_on <= '@endDate' \n" +
//                    " group by month ) s group by s.month\n" +
//                    "union\n" +
//                    "select strftime('%Y-%m', datetime(replace(replace(ar.created_on,'AM',''),'PM',''))) as month,null as dues,\n" +
//                    "( sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
//                    "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
//                    "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as collected\n" +
//                    "from AppReceipt ar \n" +
//                    "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
//                    "where ar.created_on >= '@startDate'\n" +
//                    "and ar.created_on <= '@endDate' \n" +
//                    " group by month)\n" +
//                    "group by month \n";

        query = "select month,sum(dues) as dues,sum(collected) as collected from (select month,sum(dues) as dues, null as collected from (\n" +
                "select 'I' as type,strftime('%Y-%m', datetime(replace(replace(ai.created_on,'AM',''),'PM',''))) as month,ai.created_on, (sum(ai.fees_admission * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end)+\n" +
                "sum(ai.fees_exam * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end)+ \n" +
                "sum(ai.fees_tution * case when ai.created_from = 'W' or ai.created_from = 'B' then -1 else 1 end)+\n" +
                "sum(ai.fees_books * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)+\n" +
                "sum(ai.fees_copies * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)+\n" +
                "sum(ai.fees_uniform * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)+\n" +
                "sum(ai.fees_others * case when ai.created_from = 'W' or ai.created_from = 'B' or ai.created_from = 'S' then -1 else 1 end)) as dues\n" +
                "from AppInvoice ai \n" +
                "inner join school_class sc on sc.id = ai.schoolclass_id and sc.school_id = @SchoolId \n" +
                "where ai.created_on >= '@startDate'\n" +
                "and ai.created_on <= '@endDate' \n" +
                " group by month ) s group by s.month\n" +
                "union\n" +
                "select strftime('%Y-%m', datetime(replace(replace(ar.created_on,'AM',''),'PM',''))) as month,null as dues,\n" +
                "( sum(ar.fees_admission * case when ar.is_correction = 0 then -1 else 1 end)+\n" +
                "sum(ar.fees_exam* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
                "sum(ar.fees_tution* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
                "sum(ar.fees_books* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
                "sum(ar.fees_copies* case when ar.is_correction = 0 then -1 else 1 end)+ \n" +
                "sum(ar.fees_uniform* case when ar.is_correction = 0 then -1 else 1 end)+\n" +
                "sum(ar.fees_others* case when ar.is_correction = 0 then -1 else 1 end)) as collected\n" +
                "from AppReceipt ar \n" +
                "inner join school_class sc on sc.id = ar.schoolclass_id and sc.school_id = @SchoolId \n" +
                "where ar.created_on >= '@startDate'\n" +
                "and ar.created_on <= '@endDate' \n" +
                " group by month)\n" +
                "group by month \n";

        query = query.replace("@SchoolId", schoolId + "");
        query = query.replace("@startDate", startDate + "");
        query = query.replace("@endDate", endDate + "");

        Log.d("getViewRecvAcad query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    FeesCollectionReportModel model = new FeesCollectionReportModel();
                    model.setDues(cursor.getDouble(cursor.getColumnIndex("dues")));
                    model.setCollected(cursor.getDouble(cursor.getColumnIndex("collected")));
                    model.setMonth(cursor.getString(cursor.getColumnIndex("month")));
//                    model.setTotal(model.getFees_admission() + model.getFees_exam() + model.getFees_copies() + model.getFees_books()
//                            + model.getFees_others() + model.getFees_tution() + model.getFees_uniform());

                    duesCollectedList.add(model);
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
        return duesCollectedList;

    }

    public List<ViewReceivablesTableModel> getViewRecvMonth(int schoolId, int filter) {

        // 0 for this month dues
        // 1 for previous month dues
        // 2 for previous month collected
        // 3 for previous month collected

        List<ViewReceivablesTableModel> models = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = "";

        if (filter == 0)
            query = GET_THIS_MONTH_DUES;
        else if (filter == 1)
            query = GET_PREV_MONTH_DUES;
        else if (filter == 2)
            query = GET_THIS_MONTH_COLLECTED;
        else if (filter == 3)
            query = GET_PREV_MONTH_COLLECTED;

        query = query.replace("@SchoolId", schoolId + "");
        Log.d("Upload query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    ViewReceivablesTableModel model = new ViewReceivablesTableModel();
                    model.setType(cursor.getString(cursor.getColumnIndex("type")));
//                    model.setCreated_on(cursor.getString(cursor.getColumnIndex(CREATED_ON)));
                    model.setFees_admission(cursor.getDouble(cursor.getColumnIndex(FEES_ADMISSION)));
                    model.setFees_exam(cursor.getDouble(cursor.getColumnIndex(FEES_EXAM)));
                    model.setFees_tution(cursor.getDouble(cursor.getColumnIndex(FEES_TUTION)));
                    model.setFees_books(cursor.getDouble(cursor.getColumnIndex(FEES_BOOKS)));
                    model.setFees_copies(cursor.getDouble(cursor.getColumnIndex(FEES_COPIES)));
                    model.setFees_uniform(cursor.getDouble(cursor.getColumnIndex(FEES_UNIFORMS)));
                    model.setFees_others(cursor.getDouble(cursor.getColumnIndex(FEES_OTHERS)));

                    model.setTotal(model.getFees_admission() + model.getFees_exam() + model.getFees_copies() + model.getFees_books()
                            + model.getFees_others() + model.getFees_tution() + model.getFees_uniform());

                    models.add(model);
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
        return models;
    }

    public List<ViewReceivablesTableModel> getGraphReceivables(int schoolId, String fromDate, String toDate, int filter) {
        List<ViewReceivablesTableModel> models = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = "";

        if (filter == 0)
            query = GET_GRAPH_DUES;
        else
            query = GET_GRAPH_COLLECTED;

        query = query.replace("@SchoolId", schoolId + "");
        query = query.replace("@fromDate", fromDate + "");
        query = query.replace("@toDate", toDate + "");

        Log.d("getGraphRecv query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    ViewReceivablesTableModel model = new ViewReceivablesTableModel();
                    model.setFees_admission(cursor.getDouble(cursor.getColumnIndex(FEES_ADMISSION)));
                    model.setFees_exam(cursor.getDouble(cursor.getColumnIndex(FEES_EXAM)));
                    model.setFees_tution(cursor.getDouble(cursor.getColumnIndex(FEES_TUTION)));
                    model.setFees_books(cursor.getDouble(cursor.getColumnIndex(FEES_BOOKS)));
                    model.setFees_copies(cursor.getDouble(cursor.getColumnIndex(FEES_COPIES)));
                    model.setFees_uniform(cursor.getDouble(cursor.getColumnIndex(FEES_UNIFORMS)));
                    model.setFees_others(cursor.getDouble(cursor.getColumnIndex(FEES_OTHERS)));
                    model.setMonth(cursor.getString(cursor.getColumnIndex("rep_month")));
                    model.setTotal(model.getFees_admission() + model.getFees_exam() + model.getFees_copies() + model.getFees_books()
                            + model.getFees_others() + model.getFees_tution() + model.getFees_uniform());

                    models.add(model);
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
        return models;
    }


    public List<StudentCollectionReportModel> getStudentCollectionReport(int schoolId) {
        List<StudentCollectionReportModel> models = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        String query = STUDENT_COLLECTION_REPORT_QUERY;

        query = query.replace("@SchoolId", schoolId + "");

        Log.d("Collection Report query", query);
        try {
            db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {

                    StudentCollectionReportModel model = new StudentCollectionReportModel();
                    model.setGrNo(cursor.getInt(cursor.getColumnIndex("student_gr_no")));
                    model.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                    model.setSectionName(cursor.getString(cursor.getColumnIndex("secName")));
                    model.setCollected(cursor.getDouble(cursor.getColumnIndex("collected")));
                    model.setDues(cursor.getDouble(cursor.getColumnIndex("dues")));
                    model.setStudentName(cursor.getString(cursor.getColumnIndex("student_name")));
                    model.setSecId(cursor.getInt(cursor.getColumnIndex("secId")));
                    model.setClassId(cursor.getInt(cursor.getColumnIndex("classId")));
                    model.setSchoolId(schoolId);
                    models.add(model);
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
        return models;
    }

}
