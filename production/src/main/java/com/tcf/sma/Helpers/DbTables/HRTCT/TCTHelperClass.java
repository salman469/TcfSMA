package com.tcf.sma.Helpers.DbTables.HRTCT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.HRTCT.EmployeeTCT_EntryActivity;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.GeneralUploadResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTDesginationModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjTagReasonModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTLeaveTypeModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTPhaseModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTSubjectsModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.UploadTCTEmployeeSubTagModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TCTHelperClass {
    private static TCTHelperClass instance = null;
    private Context context;

    public TCTHelperClass(Context context) {
        this.context = context;
    }

    public static TCTHelperClass getInstance(Context context) {
        if (instance == null)
            instance = new TCTHelperClass(context);
        return instance;
    }

    public static String ID = "id";
    private String TCT_Phase = "tct_phase";
    private String Start_Date = "Start_Date";
    private String End_Date = "End_Date";
    private String ActivityID = "ActivityID";
    public String Mandatory = "Mandatory";
    private String AcademicSession_id = "AcademicSession_id";
    private String Uploaded_On = "Uploaded_On";
    public static String Modified_On = "Modified_On";

    public final String TCT_StartDate = "TCT_StartDate";
    public final String TCT_EndDate = "TCT_EndDate";
    public final String TCT_PostRegStartDate = "TCT_PostRegStartDate";
    public final String TCT_PostRegEndDate = "TCT_PostRegEndDate";
    public final String TCT_TestDate = "TCT_TestDate";
    public static String TABLE_TCT_PHASE = "TCT_PHASE";
    public String CREATE_TABLE_EMPLOYEE_TCT_PHASE = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_PHASE + " (" +
            ID + " INTEGER PRIMARY KEY," +
            TCT_Phase + " TEXT, " +
            Start_Date + " TEXT, " +
            End_Date + " TEXT, " +
            ActivityID + " INTEGER, " +
            Mandatory + " BOOLEAN, " +
            AcademicSession_id + " INTEGER, " +
            TCT_StartDate + " TEXT, " +
            TCT_EndDate + " TEXT, " +
            TCT_PostRegStartDate + " TEXT, " +
            TCT_PostRegEndDate + " TEXT, " +
            Modified_On + " TEXT, " +
            TCT_TestDate + " TEXT " +
            ");";

    public static String TABLE_TCT_DESIGNATIONS = "TCT_DESIGNATIONS";
    public static String TCT_IS_ACTIVE = "is_active";
    public static String TCT_TITLE = "title";
    public static String TCT_CREATED_ON = "created_on";

    public String CREATE_TABLE_TCT_DESIGNATIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_DESIGNATIONS + " (" +
            ID + " INTEGER PRIMARY KEY," +  //Add ID AUTOINCREMENT when api created
            TCT_TITLE + " TEXT , " +
            TCT_IS_ACTIVE + " BOOLEAN , " +
            TCT_CREATED_ON + " TEXT , " +
            Modified_On + " TEXT " +
            ");";


    public static String TABLE_TCT_LEAVES_TYPE = "TCT_LEAVES_TYPE";
    public String CREATE_TABLE_TCT_LEAVES_TYPE = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_LEAVES_TYPE + " (" +
            ID + " INTEGER PRIMARY KEY," +  //Add ID AUTOINCREMENT when api created
            TCT_TITLE + " TEXT , " +
            TCT_IS_ACTIVE + " BOOLEAN , " +
            TCT_CREATED_ON + " TEXT , " +
            Modified_On + " TEXT " +
            ");";


    public List<TCTPhaseModel> getTCTPhases(int schoolId) {
        List<TCTPhaseModel> phasesList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT DISTINCT tctPhase.* from " + TABLE_TCT_PHASE + " tctPhase";
            selectQuery += " INNER JOIN " + TABLE_TCT_EMP_SUBJECT_TAGGING + " empTCT ON empTCT." + TCTPhase_ID + "= tctPhase." + ID;
            selectQuery += " WHERE empTCT." + SchoolID + " = " + schoolId;
            selectQuery += " AND strftime('%Y-%m-%d',date('now')) not between strftime('%Y-%m-%d',tctPhase." + Start_Date + ") and strftime('%Y-%m-%d',tctPhase." + TCT_PostRegEndDate + ")";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    TCTPhaseModel tctPhaseModel = new TCTPhaseModel();
                    boolean mandatory = cursor.getString(cursor.getColumnIndex(Mandatory)).equals("1");

                    tctPhaseModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    tctPhaseModel.setActivityID(cursor.getInt(cursor.getColumnIndex(ActivityID)));
                    tctPhaseModel.setAcademicSession_id(cursor.getInt(cursor.getColumnIndex(AcademicSession_id)));

                    tctPhaseModel.setTCT_Phase(cursor.getString(cursor.getColumnIndex(TCT_Phase)));

                    tctPhaseModel.setStart_Date(cursor.getString(cursor.getColumnIndex(Start_Date)));
                    tctPhaseModel.setEnd_date(cursor.getString(cursor.getColumnIndex(End_Date)));
                    tctPhaseModel.setTctStartDate(cursor.getString(cursor.getColumnIndex(TCT_StartDate)));
                    tctPhaseModel.setTctEndDate(cursor.getString(cursor.getColumnIndex(TCT_EndDate)));
                    tctPhaseModel.setTctPostRegStartDate(cursor.getString(cursor.getColumnIndex(TCT_PostRegStartDate)));
                    tctPhaseModel.setTctPostRegEndDate(cursor.getString(cursor.getColumnIndex(TCT_PostRegEndDate)));
                    tctPhaseModel.setTctTestDate(cursor.getString(cursor.getColumnIndex(TCT_TestDate)));

                    tctPhaseModel.setMandatory(mandatory);

                    phasesList.add(tctPhaseModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return phasesList;
    }

    private String Reason = "Reason";

    public static String TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS = "TCT_EMP_SUBJECT_TAGGING_REASONS";
    public String CREATE_TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Reason + " TEXT ," +
            Modified_On + " TEXT " +
            ");";

    private long insertReason(String reason) {
        long id = 0;
        if (reason != null && !reason.isEmpty()) {
            try {
                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                ContentValues cv = new ContentValues();
                cv.put(Reason, reason);
                id = DB.insert(TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS, null, cv);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    private String User_ID = "userID";
    private String EMP_Code = "EMP_Code";
    private String EMP_Name = "EMP_Name";
    private String Designation_ID = "Designation_ID";
    private String SchoolID = "SchoolID";
    private String TCTPhase_ID = "TCTPhase_ID";
    private String Subject1_ID = "Subject1_ID";
    private String Subject2_ID = "Subject2_ID";
    private String ReasonID = "ReasonID";
    public String Modified_By = "Modified_By";
    private String Comment = "Comment";
    public final String isVerified = "isVerified";
    public final String Cnic = "Cnic";
    public final String regStatus = "reg_status";
    public final String leaveType_id = "leave_type_id";
    public final String newDesignation_id = "new_designation_id";


    public static String TABLE_TCT_EMP_SUBJECT_TAGGING = "TCT_EMP_SUBJECT_TAGGING";
    public String CREATE_TABLE_TCT_EMP_SUBJECT_TAGGING = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_EMP_SUBJECT_TAGGING + " (" +
            ID + " INTEGER PRIMARY KEY," +  //Add ID AUTOINCREMENT when api created
            User_ID + " INTEGER ," +
            EMP_Code + " TEXT , " +
            Cnic + " TEXT , " +
            Designation_ID + " INTEGER , " +
            newDesignation_id + " INTEGER , " +
            leaveType_id + " INTEGER , " +
            regStatus + " INTEGER , " +
            SchoolID + " INTEGER , " +
            TCTPhase_ID + " INTEGER , " +
            Mandatory + " BOOLEAN , " +
            isVerified + " BOOLEAN , " +
            Subject1_ID + " INTEGER , " +
            Subject2_ID + " INTEGER , " +
            ReasonID + " INTEGER , " +
            Comment + " TEXT , " +
            Uploaded_On + " TEXT , " +
            Modified_On + " TEXT , " +
            Modified_By + " INTEGER " +
            ");";
/*
    public void insertTempTCTSubjectsTagging() {
        String insertQuery = "Insert into " + TABLE_TCT_EMP_SUBJECT_TAGGING + " ("
                + ID + ","
                + User_ID + ","
                + EMP_Code + ","
                + EMP_Name + ","
                + Designation_ID + ","
                + SchoolID + ","
                + TCTPhase_ID + ","
                + Mandatory + ") VALUES ";

        insertQuery += "(1,17338,'0610403','Noreen Shahzadi',56,1363,1,'Yes'),";
        insertQuery += "(2,18565,'0001894','Ruqia Baby',57,27,1,'Yes'),";
        insertQuery += "(3,15253,'0608194','Maryam Zahra',57,1363,1,'Yes'),";
        insertQuery += "(4,10692,'0610054','Asia Batool',57,1363,1,'Yes'),";
        insertQuery += "(5,21782,'0028583','Um-E-Hani',56,27,1,'Yes')";

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public boolean anyUnSubmittedTCTEmployeeLeft(int schoolId) {
        int unSubmittedEmployees = 0;
        for(TCTEmpSubjectTaggingModel model : TCTHelperClass.getInstance(context).getEmpTakingTCT(schoolId)) {
            if(model.getRegStatusID() < 2)
                unSubmittedEmployees++;
        }
        return unSubmittedEmployees > 0;
    }

    public List<TCTEmpSubjectTaggingModel> getEmpTakingTCT(int schoolId) {
        List<TCTEmpSubjectTaggingModel> empList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT empTCT.*,empDesg.Designation_Name,empDetail.First_Name,empDetail.Last_Name,tctPhase.End_Date from " + TABLE_TCT_EMP_SUBJECT_TAGGING + " empTCT";
            selectQuery += " INNER JOIN " + EmployeeHelperClass.TABLE_EmployeesDesignation + " empDesg ON empDesg." + ID + "= empTCT." + Designation_ID;
            selectQuery += " INNER JOIN " + EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE + " empDetail ON empDetail." + ID + "= empTCT." + User_ID;
            selectQuery += " INNER JOIN " + TCTHelperClass.getInstance(context).TABLE_TCT_PHASE + " tctPhase ON tctPhase." + ID + "= empTCT." + TCTPhase_ID;
            selectQuery += " WHERE empTCT." + SchoolID + " = " + schoolId;
            selectQuery += " AND strftime('%Y-%m-%d',date('now')) between strftime('%Y-%m-%d',tctPhase.Start_Date) and strftime('%Y-%m-%d',tctPhase." + TCT_PostRegEndDate + ")";
            selectQuery += " ORDER BY empTCT.Mandatory DESC";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    TCTEmpSubjectTaggingModel tctEmpSubjectTaggingModel = new TCTEmpSubjectTaggingModel();
                    boolean mandatory = cursor.getString(cursor.getColumnIndex(Mandatory)).equals("1");

                    tctEmpSubjectTaggingModel.setUserID(cursor.getInt(cursor.getColumnIndex(User_ID)));
                    tctEmpSubjectTaggingModel.setEMP_Code(cursor.getString(cursor.getColumnIndex(EMP_Code)));
                    String empName = cursor.getString(cursor.getColumnIndex("First_Name")) + " " + cursor.getString(cursor.getColumnIndex("Last_Name"));
                    tctEmpSubjectTaggingModel.setEMP_Name(empName);
                    tctEmpSubjectTaggingModel.setDesignation_ID(cursor.getInt(cursor.getColumnIndex(Designation_ID)));
                    tctEmpSubjectTaggingModel.setDesignation_Name(cursor.getString(cursor.getColumnIndex("Designation_Name")));
                    tctEmpSubjectTaggingModel.setSchoolID(cursor.getInt(cursor.getColumnIndex(SchoolID)));
                    tctEmpSubjectTaggingModel.setTCTPhase_ID(cursor.getInt(cursor.getColumnIndex(TCTPhase_ID)));
                    tctEmpSubjectTaggingModel.setMandatory(mandatory);
                    tctEmpSubjectTaggingModel.setTctSubjectsModels(getTCTSubjects(tctEmpSubjectTaggingModel.getDesignation_Name(), schoolId));

                    tctEmpSubjectTaggingModel.setComment(cursor.getString(cursor.getColumnIndex(Comment)));
                    tctEmpSubjectTaggingModel.setCNIC(cursor.getString(cursor.getColumnIndex(Cnic)));
                    tctEmpSubjectTaggingModel.setReasonID(cursor.getInt(cursor.getColumnIndex(ReasonID)));
                    tctEmpSubjectTaggingModel.setSubject1_ID(cursor.getInt(cursor.getColumnIndex(Subject1_ID)));
                    tctEmpSubjectTaggingModel.setSubject2_ID(cursor.getInt(cursor.getColumnIndex(Subject2_ID)));
                    tctEmpSubjectTaggingModel.setNewDesignationId(cursor.getInt(cursor.getColumnIndex(newDesignation_id)));
                    tctEmpSubjectTaggingModel.setLeaveTypeID(cursor.getInt(cursor.getColumnIndex(leaveType_id)));
                    tctEmpSubjectTaggingModel.setRegStatusID(cursor.getInt(cursor.getColumnIndex(regStatus)));
                    tctEmpSubjectTaggingModel.setUploadedOn(cursor.getString(cursor.getColumnIndex(Uploaded_On)));

//                    String testDate = ;
//                    String endDate = cursor.getString(cursor.getColumnIndex(End_Date));

//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date testDate = sdf.parse(cursor.getString(cursor.getColumnIndex(TCT_TestDate)));
//                    Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(cursor.getColumnIndex(End_Date)));

                    String endDate = AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex(End_Date)), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                    Date endDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                    Date currentDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd"));
                    tctEmpSubjectTaggingModel.setViewOnly(currentDateFormatted.after(endDateFormatted) || tctEmpSubjectTaggingModel.getRegStatusID() > 1);

                    empList.add(tctEmpSubjectTaggingModel);
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

    public List<TCTEmpSubjectTaggingModel> getTCTRecord(int schoolId, int phaseId) {
        List<TCTEmpSubjectTaggingModel> empList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT empTCT.*,empTCT.Mandatory as testMandatory,empDesg.Designation_Name,empDetail.First_Name,empDetail.Last_Name,tctPhase.* from " + TABLE_TCT_EMP_SUBJECT_TAGGING + " empTCT";
            selectQuery += " INNER JOIN " + EmployeeHelperClass.TABLE_EmployeesDesignation + " empDesg ON empDesg." + ID + "= empTCT." + Designation_ID;
            selectQuery += " INNER JOIN " + EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE + " empDetail ON empDetail." + ID + "= empTCT." + User_ID;
            selectQuery += " INNER JOIN " + TCTHelperClass.getInstance(context).TABLE_TCT_PHASE + " tctPhase ON tctPhase." + ID + "= empTCT." + TCTPhase_ID;
            selectQuery += " WHERE empTCT." + SchoolID + " = " + schoolId;
            selectQuery += " AND empTCT." + TCTPhase_ID + " = " + phaseId;
//            selectQuery += " AND strftime('%Y-%m-%d',date('now')) between strftime('%Y-%m-%d',tctPhase.Start_Date) and strftime('%Y-%m-%d',tctPhase.End_Date)";
            selectQuery += " ORDER BY empTCT.Mandatory DESC";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    TCTEmpSubjectTaggingModel tctEmpSubjectTaggingModel = new TCTEmpSubjectTaggingModel();
                    boolean mandatory = cursor.getString(cursor.getColumnIndex("testMandatory")).equals("1");

                    tctEmpSubjectTaggingModel.setUserID(cursor.getInt(cursor.getColumnIndex(User_ID)));
                    tctEmpSubjectTaggingModel.setEMP_Code(cursor.getString(cursor.getColumnIndex(EMP_Code)));
                    String empName = cursor.getString(cursor.getColumnIndex("First_Name")) + " " + cursor.getString(cursor.getColumnIndex("Last_Name"));
                    tctEmpSubjectTaggingModel.setEMP_Name(empName);
                    tctEmpSubjectTaggingModel.setDesignation_ID(cursor.getInt(cursor.getColumnIndex(Designation_ID)));
                    tctEmpSubjectTaggingModel.setDesignation_Name(cursor.getString(cursor.getColumnIndex("Designation_Name")));
                    tctEmpSubjectTaggingModel.setSchoolID(cursor.getInt(cursor.getColumnIndex(SchoolID)));
                    tctEmpSubjectTaggingModel.setTCTPhase_ID(cursor.getInt(cursor.getColumnIndex(TCTPhase_ID)));
                    tctEmpSubjectTaggingModel.setMandatory(mandatory);
                    tctEmpSubjectTaggingModel.setTctSubjectsModels(getTCTSubjects(tctEmpSubjectTaggingModel.getDesignation_Name(), schoolId));

                    tctEmpSubjectTaggingModel.setComment(cursor.getString(cursor.getColumnIndex(Comment)));
                    tctEmpSubjectTaggingModel.setCNIC(cursor.getString(cursor.getColumnIndex(Cnic)));
                    tctEmpSubjectTaggingModel.setReasonID(cursor.getInt(cursor.getColumnIndex(ReasonID)));
                    tctEmpSubjectTaggingModel.setLeaveTypeID(cursor.getInt(cursor.getColumnIndex(leaveType_id)));
                    tctEmpSubjectTaggingModel.setRegStatusID(cursor.getInt(cursor.getColumnIndex(regStatus)));
                    tctEmpSubjectTaggingModel.setNewDesignationId(cursor.getInt(cursor.getColumnIndex(newDesignation_id)));
                    tctEmpSubjectTaggingModel.setSubject1_ID(cursor.getInt(cursor.getColumnIndex(Subject1_ID)));
                    tctEmpSubjectTaggingModel.setSubject2_ID(cursor.getInt(cursor.getColumnIndex(Subject2_ID)));
                    tctEmpSubjectTaggingModel.setUploadedOn(cursor.getString(cursor.getColumnIndex(Uploaded_On)));

                    empList.add(tctEmpSubjectTaggingModel);
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

    public long updateEmpSubjectTagging(List<TCTEmpSubjectTaggingModel> empSubjectTaggingModelList, int regStatus) {
        long id = 0;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();
            for (TCTEmpSubjectTaggingModel model : empSubjectTaggingModelList) {
//                int reasonID = (int) insertReason(model.getReason());
                ContentValues cv = new ContentValues();
                if (model.getReasonID() > 0)
                    cv.put(ReasonID, model.getReasonID());
                else
                    cv.putNull(ReasonID);

                if (model.getSubject1_ID() > 0)
                    cv.put(Subject1_ID, model.getSubject1_ID());
                else
                    cv.putNull(Subject1_ID);

                if (model.getSubject2_ID() > 0) //if only one subject emp have
                    cv.put(Subject2_ID, model.getSubject2_ID());
                else
                    cv.putNull(Subject2_ID);

                cv.put(Modified_On, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                cv.put(Modified_By, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
                cv.put(Uploaded_On, model.getUploadedOn());
                cv.put(Comment, model.getComment());

                cv.put(this.regStatus, regStatus);
                cv.put(this.leaveType_id, model.getLeaveTypeID());
                cv.put(Cnic, model.getCNIC());

                if (model.getNewDesignationId() > 0) {
                    cv.put(newDesignation_id, model.getNewDesignationId());
                }

                id = DB.update(TABLE_TCT_EMP_SUBJECT_TAGGING, cv, User_ID + "=" + model.getUserID() +
                        " AND " + SchoolID + " = " + model.getSchoolID(), null);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.endTransaction();
        }
        return id;
    }

    private String Subject_ID = "Subject_ID";
    private String Score = "Score";

    private String TABLE_TCT_SCORE = "TCT_SCORE";
    public String CREATE_TABLE_TCT_SCORE = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_SCORE + " (" +
            ID + " INTEGER ," +
            User_ID + " INTEGER ," +
            EMP_Code + " TEXT ," +
            SchoolID + " INTEGER ," +
            Designation_ID + " INTEGER ," +
            Subject_ID + " INTEGER ," +
            TCTPhase_ID + " INTEGER ," +
            Score + " TEXT ," +
            Modified_On + " TEXT ," +
            Modified_By + " INTEGER " +
            ");";

    private String Subject = "Subject";
    private String SubjectType = "SubjectType";
    private String SchoolType = "SchoolType";
    public static String Designation = "Designation";

    public static String TABLE_TCT_SUBJECTS = "TCT_SUBJECTS";
    public String CREATE_TABLE_TCT_SUBJECTS = "CREATE TABLE IF NOT EXISTS " + TABLE_TCT_SUBJECTS + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            Subject + " TEXT ," +
            SchoolType + " TEXT ," +
            SubjectType + " TEXT ," +
            Designation + " TEXT ," +
            Modified_On + " TEXT ," +
            Modified_By + " INTEGER " +
            ");";

    public void insertTempTCTSubjects() {
        String insertQuery = "Insert OR REPLACE into " + TABLE_TCT_SUBJECTS + " ("
                + ID + ","
                + Subject + ","
                + SchoolType + ","
                + SubjectType + ","
                + Designation + ") VALUES ";

        insertQuery += "(1,'English','Primary','','PELT,PT,ET'),";
        insertQuery += "(2,'Islamiat','Primary','','PT'),";
        insertQuery += "(3,'Math','Primary','','PT'),";
        insertQuery += "(4,'Science','Primary','','PT'),";
        insertQuery += "(5,'Urdu','Primary','','PT'),";
        insertQuery += "(6,'Sindhi','Primary','','PT'),";
        insertQuery += "(7,'Social Studies','Primary','','PT'),";
        insertQuery += "(8,'Science','Pre-Primary','','PT'),";
        insertQuery += "(9,'Math','Pre-Primary','','PT'),";
        insertQuery += "(10,'KG','Pre-Primary','','PT'),";
        insertQuery += "(11,'Nursery','Pre-Primary','NT','NT,PT'),";
        insertQuery += "(12,'English','Secondary','SELT','HSELT,SELT'),";
        insertQuery += "(13,'Social Studies','Middle','ARTS','SAT,HSAT'),";
        insertQuery += "(14,'Urdu','Middle','ARTS','SAT,HSAT'),";
        insertQuery += "(15,'Islamiat','Middle','ARTS','SAT,HSAT'),";
        insertQuery += "(16,'Urdu','Secondary','ARTS','SAT,HSAT'),";
        insertQuery += "(17,'Islamiat','Secondary','ARTS','SAT,HSAT'),";
        insertQuery += "(18,'Sindhi','Secondary','ARTS','SAT,HSAT'),";
        insertQuery += "(19,'Pakistan Studies','Secondary','ARTS','SAT,HSAT'),";
        insertQuery += "(20,'Computer Studies','Middle','Computers','SCT'),";
        insertQuery += "(21,'Computer Studies','Secondary','Computers','SCT'),";
        insertQuery += "(22,'Math','Middle','Science','HSST,SST'),";
        insertQuery += "(23,'Science','Middle','Science','HSST,SST'),";
        insertQuery += "(24,'Math','Secondary','Science','HSST,SST'),";
        insertQuery += "(25,'Chemistry','Secondary','Science','HSST,SST'),";
        insertQuery += "(26,'Biology','Secondary','Science','HSST,SST'),";
        insertQuery += "(27,'Physics','Secondary','Science','HSST,SST')";

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            DB.execSQL(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<EmployeeDesignationModel> getDesignationsForTCT() {
        List<String> allTCTDesignationsList = DatabaseHelper.getInstance(context).getStringListValuesFromDB(
                TCTHelperClass.TABLE_TCT_SUBJECTS,
                TCTHelperClass.Designation,
                ""
        );

        ArrayList<EmployeeDesignationModel> designationModels = new ArrayList<>();
        for (String designation : allTCTDesignationsList) {
            designationModels.add(new EmployeeDesignationModel(
                    DatabaseHelper.getInstance(context).getIntValueFromDB(
                            EmployeeHelperClass.TABLE_EmployeesDesignation,
                            EmployeeHelperClass.ID,
                            EmployeeHelperClass.Designation_Name + " = '" + designation + "'"),
                    designation
            ));
        }

        return designationModels;
    }

    public List<TCTSubjectsModel> getTCTSubjects(String designation_name, int schoolId) {
        SchoolModel sm = DatabaseHelper.getInstance(context).getSchoolById(schoolId);
        boolean isPrimarySchoolHasSecondaryClasses = DatabaseHelper.getInstance(context).isPrimarySchoolHasSecondaryClasses(schoolId);
        String schoolType = "";
        List<TCTSubjectsModel> tctSubjectsModelList = new ArrayList<>();

        if (sm != null) {
            if (sm.getTypeOfSchool() != null && sm.getTypeOfSchool().equals("PR") && isPrimarySchoolHasSecondaryClasses) {
                schoolType = "'Secondary','Middle','Primary','Pre-Primary'";
            } else if (sm.getTypeOfSchool() != null && (sm.getTypeOfSchool().equals("SC") || sm.getTypeOfSchool().equals("HS"))) {
                schoolType = "'Secondary','Middle'";
            } else if (sm.getTypeOfSchool() != null && sm.getTypeOfSchool().equals("PR")) {
                schoolType = "'Primary','Pre-Primary'";
            }

            String selectQuery = "SELECT * FROM " + TABLE_TCT_SUBJECTS
                    + " WHERE " + SchoolType + " IN (@SchoolType)";

            if (!designation_name.isEmpty())
                selectQuery += " AND Designation like '%" + designation_name + "%' ";

            selectQuery = selectQuery.replace("@SchoolType", schoolType);

            Cursor cursor = null;
            try {
                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        TCTSubjectsModel model = new TCTSubjectsModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        model.setSubject(cursor.getString(cursor.getColumnIndex(Subject)));
                        model.setSchoolType(cursor.getString(cursor.getColumnIndex(SchoolType)));
                        model.setSubjectType(cursor.getString(cursor.getColumnIndex(SubjectType)));
                        model.setDesignation(cursor.getString(cursor.getColumnIndex(Designation)));

                        //it will not add sindhi sub if province is other than sindh
                        if ((model.getId() == 6 || model.getId() == 18 || model.getSubject().equals("Sindhi")) && sm.getProvinceId() != 5) {
                            continue;
                        }

                        tctSubjectsModelList.add(model);
                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return tctSubjectsModelList;
    }

    public void addOrUpdateTCTSubjects(ArrayList<TCTSubjectsModel> subjectsModelList, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (TCTSubjectsModel model : subjectsModelList) {
                if (!FindTCTSubjects(model.getId(), model.getSubject())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getId());
                    values.put(Subject, model.getSubject());
                    values.put(SchoolType, model.getSchoolType());
                    values.put(SubjectType, model.getSubjectType());
                    values.put(Designation, model.getDesignation());
                    values.put(Modified_On, model.getModified_on());
                    values.put(Modified_By, model.getModified_by());

                    long i = DB.insert(TABLE_TCT_SUBJECTS, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert tct subject");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "tct subject inserted");
                        downloadedCount++;
                    }
                } else {
                    long i = updateTCTSubject(model);
                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            //remove if not in server
//            removerTCTSubjectsIfNotExists(subjectsModelList);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addOrUpdateTCTSubjects Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private long updateTCTSubject(TCTSubjectsModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getId());
            values.put(Subject, model.getSubject());
            values.put(SubjectType, model.getSubjectType());
            values.put(SchoolType, model.getSchoolType());
            values.put(Designation, model.getDesignation());
            values.put(Modified_On, model.getModified_on());
            values.put(Modified_By, model.getModified_by());

            long i = DB.update(TABLE_TCT_SUBJECTS, values, ID + " = " + model.getId() + " AND " +
                    Subject + " = '" + model.getSubject() + "'", null);
            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update tct subject");
            else
                AppModel.getInstance().appendLog(context, "tct subject updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void removerTCTSubjectsIfNotExists(ArrayList<TCTSubjectsModel> serverTCTSubjects) {
        new Thread(() -> {
            boolean tctSubjectFound;
            List<TCTSubjectsModel> existingTCTSubjectsList = getTCTSubjects();
            List<TCTSubjectsModel> todo = new ArrayList<>();
            if (existingTCTSubjectsList != null && existingTCTSubjectsList.size() > 0) {
                for (TCTSubjectsModel existingTCTSubjects : existingTCTSubjectsList) {
                    tctSubjectFound = false;
                    for (TCTSubjectsModel serverTCTSub : serverTCTSubjects) {
                        if (existingTCTSubjects.getId() == serverTCTSub.getId() &&
                                existingTCTSubjects.getSubject().equals(serverTCTSub.getSubject())) {
                            tctSubjectFound = true;
                        }
                    }
                    if (!tctSubjectFound)
                        todo.add(existingTCTSubjects);
                }
            }
            for (TCTSubjectsModel model : todo) {
                if (removeTCTSubject(model)) {
                    AppModel.getInstance().appendLog(context, "TCT Subject Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove TCT Subject: id = " + model.getId());
                }
            }
        }).start();
    }

    private boolean removeTCTSubject(TCTSubjectsModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_TCT_SUBJECTS, ID + "=" + model.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TCTSubjectsModel> getTCTSubjects() {
        List<TCTSubjectsModel> tctSubjectsModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_TCT_SUBJECTS;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TCTSubjectsModel model = new TCTSubjectsModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setSubject(cursor.getString(cursor.getColumnIndex(Subject)));
                    model.setSubjectType(cursor.getString(cursor.getColumnIndex(SubjectType)));
                    model.setSchoolType(cursor.getString(cursor.getColumnIndex(SchoolType)));
                    model.setDesignation(cursor.getString(cursor.getColumnIndex(Designation)));
                    tctSubjectsModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tctSubjectsModels;
    }

    private boolean FindTCTSubjects(int id, String subject) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_SUBJECTS
                    + " WHERE " + ID + " = " + id
                    + " AND " + Subject + " ='" + subject + "'";

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

    public String getTCTSubjectById(int id) {
        if (id > 0) {
            Cursor cursor = null;
            try {
                String selectQuery = "SELECT * FROM " + TABLE_TCT_SUBJECTS
                        + " WHERE " + ID + " = " + id;

                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    return cursor.getString(cursor.getColumnIndex(Subject));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return "";
    }

    public String getTCTReasonById(int id) {
        if (id > 0) {
            Cursor cursor = null;
            try {
                String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS
                        + " WHERE " + ID + " = " + id;

                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    return cursor.getString(cursor.getColumnIndex(Reason));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return "";
    }

    public boolean removeExistingEmpSubTagging(int schoolid) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_TCT_EMP_SUBJECT_TAGGING, SchoolID + " = " + schoolid, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addEmpSubjectTagging(ArrayList<TCTEmpSubjectTaggingModel> tctEmpSubjectTaggingModels, int schoolid, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (TCTEmpSubjectTaggingModel model : tctEmpSubjectTaggingModels) {
                if (!FindEmpSubjectTagging(model.getUserID(), schoolid, model.getTCTPhase_ID())) {
                    model.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                    ContentValues values = new ContentValues();
                    values.put(ID, model.getId());
                    values.put(User_ID, model.getUserID());
                    values.put(EMP_Code, model.getEMP_Code());
//                    values.put(EMP_Name, model.getEMP_Name());
                    values.put(Designation_ID, model.getDesignation_ID());
                    values.put(newDesignation_id, model.getNewDesignationId());
                    values.put(SchoolID, model.getSchoolID());
                    values.put(TCTPhase_ID, model.getTCTPhase_ID());
                    if (model.isMandatory())
                        values.put(Mandatory, 1);
                    else
                        values.put(Mandatory, 0);

                    if (model.isVerified())
                        values.put(isVerified, 1);
                    else
                        values.put(isVerified, 0);

                    if (model.getSubject1_ID() > 0)
                        values.put(Subject1_ID, model.getSubject1_ID());

                    if (model.getSubject2_ID() > 0)
                        values.put(Subject2_ID, model.getSubject2_ID());

                    if (model.getReasonID() > 0)
                        values.put(ReasonID, model.getReasonID());

                    values.put(Comment, model.getComment());
                    values.put(Cnic, model.getCNIC());
                    values.put(leaveType_id, model.getLeaveTypeID());
                    values.put(regStatus, model.getRegStatusID());

                    if (model.getModified_On() != null) {
                        String modifiedOn = AppModel.getInstance().convertDatetoFormat(model.getModified_On(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setModified_On(modifiedOn);
                        values.put(Modified_On, model.getModified_On());
                    }

                    if (model.getModified_By() > 0)
                        values.put(Modified_By, model.getModified_By());

                    values.put(Uploaded_On, model.getUploadedOn());

                    long i = DB.insert(TABLE_TCT_EMP_SUBJECT_TAGGING, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert tct emp subjects tagging");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "TCT emp subjects tagging inserted");

                        downloadedCount++;
                    }
                } else {
//                    if (!IfEmpSubjectTaggingNotUploaded(model.getUserID(), schoolid)) {
//                        model.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long id = updateEmpSubjectTagging(model);

                    if (id > 0) {
                        downloadedCount++;
                    }
//                }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            }

            //remove if not in server
//            removerEmpSubjectTaggingIfNotExists(schoolid, tctEmpSubjectTaggingModels);

            DB.setTransactionSuccessful();
        } catch (
                Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddOrUpdateTCTEmpSubTag Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }

    }

    private boolean IfEmpSubjectTaggingNotUploaded(int userID, int schoolid) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING
                    + " WHERE " + User_ID + " = " + userID
                    + " AND " + SchoolID + " = " + schoolid
                    + " AND " + Uploaded_On + " IS NULL";

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

    private void removerEmpSubjectTaggingIfNotExists(int schoolid, ArrayList<TCTEmpSubjectTaggingModel> serverEmpSubjectTaggingModels) {
        new Thread(() -> {
            boolean EmpSubjectTaggingFound;
            List<TCTEmpSubjectTaggingModel> existingEmpSubjectTaggingList = getTCTEmpSubTaggings(schoolid);
            List<TCTEmpSubjectTaggingModel> todo = new ArrayList<>();
            if (existingEmpSubjectTaggingList != null && existingEmpSubjectTaggingList.size() > 0) {
                for (TCTEmpSubjectTaggingModel existingEmpSubTag : existingEmpSubjectTaggingList) {
                    EmpSubjectTaggingFound = false;
                    for (TCTEmpSubjectTaggingModel serverEmpTakingTCT : serverEmpSubjectTaggingModels) {
                        if (existingEmpSubTag.getId() == serverEmpTakingTCT.getId()) {
                            EmpSubjectTaggingFound = true;
                        }
                    }
                    if (!EmpSubjectTaggingFound)
                        todo.add(existingEmpSubTag);
                }
            }
            for (TCTEmpSubjectTaggingModel model : todo) {
                if (removeEmpSubTagging(model)) {
                    AppModel.getInstance().appendLog(context, "TCT Employee Subject Tagging Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove TCT Employee Subject Tagging: id = " + model.getId());
                }
            }
        }).start();
    }

    private List<TCTEmpSubjectTaggingModel> getTCTEmpSubTaggings(int schoolid) {
        List<TCTEmpSubjectTaggingModel> tctEmpSubTagModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING
                + " WHERE " + SchoolID + " = " + schoolid;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TCTEmpSubjectTaggingModel model = new TCTEmpSubjectTaggingModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setUserID(cursor.getInt(cursor.getColumnIndex(User_ID)));
                    model.setSchoolID(cursor.getInt(cursor.getColumnIndex(SchoolID)));

                    tctEmpSubTagModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tctEmpSubTagModels;
    }

    private boolean removeEmpSubTagging(TCTEmpSubjectTaggingModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_TCT_EMP_SUBJECT_TAGGING, User_ID + " = " + model.getUserID() + " AND " +
                    SchoolID + " = " + model.getSchoolID(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private long updateEmpSubjectTagging(TCTEmpSubjectTaggingModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getId());
            values.put(User_ID, model.getUserID());
            values.put(EMP_Code, model.getEMP_Code());
//                    values.put(EMP_Name, model.getEMP_Name());
            values.put(Designation_ID, model.getDesignation_ID());
            values.put(SchoolID, model.getSchoolID());
            values.put(TCTPhase_ID, model.getTCTPhase_ID());
            if (model.isMandatory())
                values.put(Mandatory, 1);
            else
                values.put(Mandatory, 0);

            if (model.isVerified())
                values.put(isVerified, 1);
            else
                values.put(isVerified, 0);

            /*if (model.getSubject1_ID() > 0)
                values.put(Subject1_ID, model.getSubject1_ID());

            if (model.getSubject2_ID() > 0)
                values.put(Subject2_ID, model.getSubject2_ID());

            if (model.getReasonID() > 0)
                values.put(ReasonID, model.getReasonID());


            values.put(newDesignation_id, model.getNewDesignationId())

            values.put(leaveType_id, model.getLeaveTypeID());
            values.put(regStatus, model.getRegStatusID());;*/


            values.put(Subject1_ID, Math.max(model.getSubject1_ID(), 0));
            values.put(Subject2_ID, Math.max(model.getSubject2_ID(), 0));
            values.put(ReasonID, Math.max(model.getReasonID(), 0));
            values.put(newDesignation_id, Math.max(model.getNewDesignationId(), 0));
            values.put(regStatus, Math.max(model.getRegStatusID(), 0));
            values.put(leaveType_id, Math.max(model.getLeaveTypeID(), 0));

            values.put(Comment, model.getComment());
            values.put(Cnic, model.getCNIC());

            if (model.getModified_On() != null) {
                String modifiedOn = AppModel.getInstance().convertDatetoFormat(model.getModified_On(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setModified_On(modifiedOn);
                values.put(Modified_On, model.getModified_On());
            }

            if (model.getModified_By() > 0)
                values.put(Modified_By, model.getModified_By());

            if (Strings.isEmptyOrWhitespace(model.getUploadedOn()))
                model.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            else
                values.put(Uploaded_On, model.getUploadedOn());

            long i = DB.update(TABLE_TCT_EMP_SUBJECT_TAGGING, values, ID + " = " + model.getId() + " AND " +
                    SchoolID + " = " + model.getSchoolID(), null);

            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update tct emp subjects tagging");
            else
                AppModel.getInstance().appendLog(context, "TCT emp subjects tagging updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean FindEmpSubjectTagging(int userID, int schoolId, int phaseId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING
                    + " WHERE " + User_ID + " = " + userID
                    + " AND " + SchoolID + " ='" + schoolId + "'"
                    + " AND " + TCTPhase_ID + " ='" + phaseId + "'";

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

    private long updateTCTLeaveType(TCTLeaveTypeModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getID());
            values.put(TCT_TITLE, model.getTitle());
            values.put(Modified_On, model.getModifiedOn());
            values.put(TCT_CREATED_ON, model.getCreatedOn());
            if (model.isActive())
                values.put(TCT_IS_ACTIVE, 1);
            else
                values.put(TCT_IS_ACTIVE, 0);

            long i = DB.update(TABLE_TCT_LEAVES_TYPE, values, ID + " = " + model.getID() + " AND " +
                    TCT_TITLE + " = '" + model.getTitle() + "'", null);
            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update tct leave type");
            else
                AppModel.getInstance().appendLog(context, "TCT leave type updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addOrUpdateTCTLeaveType(ArrayList<TCTLeaveTypeModel> tctLeaveType, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (TCTLeaveTypeModel model : tctLeaveType) {
                model.setModifiedOn(AppModel.getInstance().convertDatetoFormat("01-03-2017", "dd-MM-yyyy", "yyyy-MM-dd'T'hh:mm:ss"));
                if (!FindTCTLeaveType(model.getID(), model.getTitle())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getID());
                    values.put(TCT_TITLE, model.getTitle());
                    values.put(Modified_On, model.getModifiedOn());
                    values.put(TCT_CREATED_ON, model.getCreatedOn());
                    if (model.isActive())
                        values.put(TCT_IS_ACTIVE, 1);
                    else
                        values.put(TCT_IS_ACTIVE, 0);

                    long i = DB.insert(TABLE_TCT_LEAVES_TYPE, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert tct leave type");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "TCT leave type inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updateTCTLeaveType(model);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            }

            //remove if not in server
//            removerTCTEmpSubTagReasonIfNotExists(tctEmpSubjTagReasonModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addOrUpdateTCTLeaveType Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private long updateTCTDesignation(TCTDesginationModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getID());
            values.put(TCT_TITLE, model.getTitle());
            values.put(Modified_On, model.getModifiedOn());
            values.put(TCT_CREATED_ON, model.getCreatedOn());
            if (model.isActive())
                values.put(TCT_IS_ACTIVE, 1);
            else
                values.put(TCT_IS_ACTIVE, 0);

            long i = DB.update(TABLE_TCT_DESIGNATIONS, values, ID + " = " + model.getID() + " AND " +
                    TCT_TITLE + " = '" + model.getTitle() + "'", null);
            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update tct designation");
            else
                AppModel.getInstance().appendLog(context, "TCT designation updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addOrUpdateTCTDesignations(ArrayList<TCTDesginationModel> tctDesignations, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (TCTDesginationModel model : tctDesignations) {
                model.setModifiedOn(AppModel.getInstance().convertDatetoFormat("01-03-2017", "dd-MM-yyyy", "yyyy-MM-dd'T'hh:mm:ss"));
                if (!FindTCTDesignation(model.getID(), model.getTitle())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getID());
                    values.put(TCT_TITLE, model.getTitle());
                    values.put(Modified_On, model.getModifiedOn());
                    values.put(TCT_CREATED_ON, model.getCreatedOn());
                    if (model.isActive())
                        values.put(TCT_IS_ACTIVE, 1);
                    else
                        values.put(TCT_IS_ACTIVE, 0);

                    long i = DB.insert(TABLE_TCT_DESIGNATIONS, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert tct designation");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "TCT designation inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updateTCTDesignation(model);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            }

            //remove if not in server
//            removerTCTEmpSubTagReasonIfNotExists(tctEmpSubjTagReasonModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addOrUpdateTCTDesignations Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    public void addOrUpdateTCTEmpSubTagReason(ArrayList<TCTEmpSubjTagReasonModel> tctEmpSubjTagReasonModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (TCTEmpSubjTagReasonModel model : tctEmpSubjTagReasonModels) {
                model.setModifiedOn(AppModel.getInstance().convertDatetoFormat("01-03-2017", "dd-MM-yyyy", "yyyy-MM-dd'T'hh:mm:ss"));
                if (!FindTCTEmpSubTagReason(model.getId(), model.getReason())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getId());
                    values.put(Reason, model.getReason());
                    values.put(Modified_On, model.getModifiedOn());

                    long i = DB.insert(TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert tct emp sub tag reason");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "TCT emp sub tag reason inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updateTCTEmpSubTagReason(model);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            }

            //remove if not in server
//            removerTCTEmpSubTagReasonIfNotExists(tctEmpSubjTagReasonModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeeResignReason Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private long updateTCTEmpSubTagReason(TCTEmpSubjTagReasonModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getId());
            values.put(Reason, model.getReason());
            values.put(Modified_On, model.getModifiedOn());

            long i = DB.update(TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS, values, ID + " = " + model.getId() + " AND " +
                    Reason + " = '" + model.getReason() + "'", null);
            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update tct emp subject tag reason");
            else
                AppModel.getInstance().appendLog(context, "TCT emp subject tag reason updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void removerTCTEmpSubTagReasonIfNotExists(ArrayList<TCTEmpSubjTagReasonModel> serverTctEmpSubjTagReasonModels) {
        new Thread(() -> {
            boolean tctEmpSubTagReasonFound;
            List<TCTEmpSubjTagReasonModel> existingList = getTCTEmpSubTagReason();
            List<TCTEmpSubjTagReasonModel> todo = new ArrayList<>();
            if (existingList != null && existingList.size() > 0) {
                for (TCTEmpSubjTagReasonModel exlist : existingList) {
                    tctEmpSubTagReasonFound = false;
                    for (TCTEmpSubjTagReasonModel serverList : serverTctEmpSubjTagReasonModels) {
                        if (exlist.getId() == serverList.getId() && exlist.getReason().equals(serverList.getReason())) {
                            tctEmpSubTagReasonFound = true;
                        }
                    }
                    if (!tctEmpSubTagReasonFound)
                        todo.add(exlist);
                }
            }
            for (TCTEmpSubjTagReasonModel model : todo) {
                if (removeTCTEmpSubTagReason(model)) {
                    AppModel.getInstance().appendLog(context, "TCT Emp Sub Tag reason Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove TCT Emp Sub Tag reason: id = " + model.getId());
                }
            }
        }).start();
    }

    private boolean removeTCTEmpSubTagReason(TCTEmpSubjTagReasonModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS, ID + "=" + model.getId() + " AND " +
                    Reason + " ='" + model.getReason() + "'", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TCTEmpSubjTagReasonModel> getTCTEmpSubTagReason() {
        List<TCTEmpSubjTagReasonModel> tctEmpSubTagReasonModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS +
                " ORDER BY Reason ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TCTEmpSubjTagReasonModel model = new TCTEmpSubjTagReasonModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setReason(cursor.getString(cursor.getColumnIndex(Reason)));
                    tctEmpSubTagReasonModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tctEmpSubTagReasonModels;
    }

    public List<TCTDesginationModel> getTCTDesignations() {
        List<TCTDesginationModel> tctDesignationModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_TCT_DESIGNATIONS +
                " ORDER BY ID ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TCTDesginationModel model = new TCTDesginationModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(TCT_TITLE)));
                    tctDesignationModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tctDesignationModels;
    }

    public List<TCTLeaveTypeModel> getTCTLeaveTypes() {
        List<TCTLeaveTypeModel> tctLeaveTypeModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_TCT_LEAVES_TYPE +
                " ORDER BY ID ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TCTLeaveTypeModel model = new TCTLeaveTypeModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(TCT_TITLE)));
                    tctLeaveTypeModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tctLeaveTypeModels;
    }

    private boolean FindTCTEmpSubTagReason(int id, String reason) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS
                    + " WHERE " + ID + " = " + id
                    + " AND " + Reason + " ='" + reason + "'";

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

    private boolean FindTCTDesignation(int id, String title) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_DESIGNATIONS
                    + " WHERE " + ID + " = " + id
                    + " AND " + TCT_TITLE + " ='" + title + "'";

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

    private boolean FindTCTLeaveType(int id, String title) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_LEAVES_TYPE
                    + " WHERE " + ID + " = " + id
                    + " AND " + TCT_TITLE + " ='" + title + "'";

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

    public boolean isAllTCTSubNotTagged(String schoolIds) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT tctSubTag.* FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING + " tctSubTag"
                    + " INNER JOIN " + TABLE_TCT_PHASE + " tctPhase ON tctPhase." + ID + "= tctSubTag." + TCTPhase_ID
                    + " WHERE tctSubTag." + SchoolID + " IN (@SchoolIds) "
                    + " AND strftime('%Y-%m-%d',date('now')) between strftime('%Y-%m-%d',tctPhase.Start_Date) and strftime('%Y-%m-%d',tctPhase.End_Date)"
//                    + " AND (julianday(strftime('%Y-%m-%d',date('now'))) - julianday(strftime('%Y-%m-%d',tctPhase.Start_Date))) BETWEEN 0 AND 13" //2 week duration
                    + " AND (ifnull(tctSubTag.Subject1_ID,0) == 0 AND ifnull(tctSubTag.ReasonID,0) == 0) "
//                    + " || (ifnull(tctSubTag.Subject1_ID,0) > 0 AND ifnull(tctSubTag.Subject2_ID,0) == 0 AND ifnull(tctSubTag.ReasonID,0) == 0) "
                    //   + " || (ifnull(tctSubTag.Uploaded_on,'') == '') AND tctSubTag." + Mandatory + " = 1 AND (ifnull(tctSubTag." + Subject1_ID + ",0) == 0 OR ifnull(tctSubTag." + ReasonID + ",0) == 0)";
                    + " || (ifnull(tctSubTag.Uploaded_on,'') == '')  AND (ifnull(tctSubTag." + Subject1_ID + ",0) == 0 OR ifnull(tctSubTag." + ReasonID + ",0) == 0) AND (ifnull(tctSubTag."+regStatus+",0)==1||ifnull(tctSubTag."+regStatus+",0)==0)";

            selectQuery = selectQuery.replace("@SchoolIds", schoolIds);

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

    public void addOrUpdateTCTPhase(ArrayList<TCTPhaseModel> tctPhaseModels, int schoolid, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (TCTPhaseModel model : tctPhaseModels) {
                model.setModifiedOn(AppModel.getInstance().convertDatetoFormat("01-03-2017", "dd-MM-yyyy", "yyyy-MM-dd'T'hh:mm:ss"));
                if (!FindTCTPhase(model.getId())) {

                    ContentValues values = new ContentValues();
                    values.put(ID, model.getId());
                    values.put(TCT_Phase, model.getTCT_Phase());
                    if (model.getStart_Date() != null) {
                        String startDate = AppModel.getInstance().convertDatetoFormat(model.getStart_Date(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setStart_Date(startDate);
                        values.put(Start_Date, model.getStart_Date());
                    }
                    if (model.getEnd_date() != null) {
                        String endDate = AppModel.getInstance().convertDatetoFormat(model.getEnd_date(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setEnd_date(endDate);
                        values.put(End_Date, model.getEnd_date());
                    }

                    if (model.getTctStartDate() != null) {
                        String tctStartDate = AppModel.getInstance().convertDatetoFormat(model.getTctStartDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setTctStartDate(tctStartDate);
                        values.put(TCT_StartDate, model.getTctStartDate());
                    }
                    if (model.getTctEndDate() != null) {
                        String tctEndDate = AppModel.getInstance().convertDatetoFormat(model.getTctEndDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setTctEndDate(tctEndDate);
                        values.put(TCT_EndDate, model.getTctEndDate());
                    }

                    if (model.getTctPostRegStartDate() != null) {
                        String tctPostRegStartDate = AppModel.getInstance().convertDatetoFormat(model.getTctPostRegStartDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setTctPostRegStartDate(tctPostRegStartDate);
                        values.put(TCT_PostRegStartDate, model.getTctPostRegStartDate());
                    }
                    if (model.getTctPostRegEndDate() != null) {
                        String tctPostRegEndDate = AppModel.getInstance().convertDatetoFormat(model.getTctPostRegEndDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setTctPostRegEndDate(tctPostRegEndDate);
                        values.put(TCT_PostRegEndDate, model.getTctPostRegEndDate());
                    }

                    if (model.getTctTestDate() != null) {
                        String tctTestDate = AppModel.getInstance().convertDatetoFormat(model.getTctTestDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                        model.setTctTestDate(tctTestDate);
                        values.put(TCT_TestDate, model.getTctTestDate());
                    }
                    values.put(Mandatory, model.getMandatory());
                    values.put(ActivityID, model.getActivityID());
                    values.put(AcademicSession_id, model.getAcademicSession_id());
                    values.put(Modified_On, model.getModifiedOn());

                    long i = DB.insert(TABLE_TCT_PHASE, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert tct emp phase");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "TCT emp phase inserted");

                        downloadedCount++;
                    }
                } else {
                    long i = updateTCTPhse(model);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            //remove if not in server
//            removerTCTPhaseIfNotExists(tctPhaseModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddOrUpdateTCTPhase Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private void removerTCTPhaseIfNotExists(ArrayList<TCTPhaseModel> serverTCTPhaseModels) {
        new Thread(() -> {
            boolean TCTPhaseFound;
            List<TCTPhaseModel> existingTCTPhaseList = getTCTPhase();
            List<TCTPhaseModel> todo = new ArrayList<>();
            if (existingTCTPhaseList != null && existingTCTPhaseList.size() > 0) {
                for (TCTPhaseModel existingTCTPh : existingTCTPhaseList) {
                    TCTPhaseFound = false;
                    for (TCTPhaseModel serverTCTPhase : serverTCTPhaseModels) {
                        if (existingTCTPh.getId() == serverTCTPhase.getId()) {
                            TCTPhaseFound = true;
                        }
                    }
                    if (!TCTPhaseFound)
                        todo.add(existingTCTPh);
                }
            }
            for (TCTPhaseModel model : todo) {
                if (removeTCTPhase(model)) {
                    AppModel.getInstance().appendLog(context, "TCT Phase Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove TCT Phase: id = " + model.getId());
                }
            }
        }).start();
    }

    private boolean removeTCTPhase(TCTPhaseModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_TCT_PHASE, ID + " = " + model.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<TCTPhaseModel> getTCTPhase() {
        List<TCTPhaseModel> tctPhaseModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_TCT_PHASE
                + " ORDER BY Start_Date DESC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TCTPhaseModel model = new TCTPhaseModel();
                    boolean mandatory = cursor.getString(cursor.getColumnIndex(Mandatory)).equals("1");

                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setTCT_Phase(cursor.getString(cursor.getColumnIndex(TCT_Phase)));
                    model.setStart_Date(cursor.getString(cursor.getColumnIndex(Start_Date)));
                    model.setEnd_date(cursor.getString(cursor.getColumnIndex(End_Date)));
                    model.setMandatory(mandatory);
                    model.setActivityID(cursor.getInt(cursor.getColumnIndex(ActivityID)));
                    model.setAcademicSession_id(cursor.getInt(cursor.getColumnIndex(AcademicSession_id)));

                    tctPhaseModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tctPhaseModels;
    }

    private long updateTCTPhse(TCTPhaseModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getId());
            values.put(TCT_Phase, model.getTCT_Phase());
            if (model.getStart_Date() != null) {
                String startDate = AppModel.getInstance().convertDatetoFormat(model.getStart_Date(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setStart_Date(startDate);
                values.put(Start_Date, model.getStart_Date());
            }
            if (model.getEnd_date() != null) {
                String endDate = AppModel.getInstance().convertDatetoFormat(model.getEnd_date(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setEnd_date(endDate);
                values.put(End_Date, model.getEnd_date());
            }

            if (model.getTctStartDate() != null) {
                String tctStartDate = AppModel.getInstance().convertDatetoFormat(model.getTctStartDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setTctStartDate(tctStartDate);
                values.put(TCT_StartDate, model.getTctStartDate());
            }
            if (model.getTctEndDate() != null) {
                String tctEndDate = AppModel.getInstance().convertDatetoFormat(model.getTctEndDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setTctEndDate(tctEndDate);
                values.put(TCT_EndDate, model.getTctEndDate());
            }

            if (model.getTctPostRegStartDate() != null) {
                String tctPostRegStartDate = AppModel.getInstance().convertDatetoFormat(model.getTctPostRegStartDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setTctPostRegStartDate(tctPostRegStartDate);
                values.put(TCT_PostRegStartDate, model.getTctPostRegStartDate());
            }
            if (model.getTctPostRegEndDate() != null) {
                String tctPostRegEndDate = AppModel.getInstance().convertDatetoFormat(model.getTctPostRegEndDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setTctPostRegEndDate(tctPostRegEndDate);
                values.put(TCT_PostRegEndDate, model.getTctPostRegEndDate());
            }

            if (model.getTctTestDate() != null) {
                String tctTestDate = AppModel.getInstance().convertDatetoFormat(model.getTctTestDate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                model.setTctTestDate(tctTestDate);
                values.put(TCT_TestDate, model.getTctTestDate());
            }
            values.put(Mandatory, model.getMandatory());
            values.put(ActivityID, model.getActivityID());
            values.put(AcademicSession_id, model.getAcademicSession_id());

            values.put(Modified_On, model.getModifiedOn());

            long i = DB.update(TABLE_TCT_PHASE, values, ID + " = " + model.getId(), null);

            if (i == 0)
                AppModel.getInstance().appendLog(context, "Couldn't update tct phase");
            else
                AppModel.getInstance().appendLog(context, "TCT phase updated");

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean FindTCTPhase(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_PHASE
                    + " WHERE " + ID + " = " + id;

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

    public List<UploadTCTEmployeeSubTagModel> getAllTCTEmpSubTagForUpload(int schoolId) {
        ArrayList<UploadTCTEmployeeSubTagModel> tctEmpSubTagList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TCT_EMP_SUBJECT_TAGGING
                    + " WHERE (" + Uploaded_On + " IS NULL OR " + Uploaded_On + " = '')"
                    + " AND " + SchoolID + " = " + schoolId
                    + " ORDER BY " + ID + " ASC";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    int reasonID = cursor.getInt(cursor.getColumnIndex(ReasonID));
                    int Subj1ID = cursor.getInt(cursor.getColumnIndex(Subject1_ID));
                    int Subj2ID = cursor.getInt(cursor.getColumnIndex(Subject2_ID));


                    String cnic = cursor.getString(cursor.getColumnIndex(Cnic));
                    int leaveTypeId = cursor.getInt(cursor.getColumnIndex(leaveType_id));
                    int regStatusId = cursor.getInt(cursor.getColumnIndex(regStatus));
                    int designationId = 0;

                    if (getTCTReasonById(reasonID).equalsIgnoreCase("designation changed")) {
                        designationId = cursor.getInt(cursor.getColumnIndex(newDesignation_id));
                    }

                    UploadTCTEmployeeSubTagModel model = new UploadTCTEmployeeSubTagModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));

                    if (Subj1ID > 0)
                        model.setSubject1_Id(Subj1ID);
                    if (Subj2ID > 0)
                        model.setSubject2_Id(Subj2ID);
                    if (reasonID > 0)
                        model.setReason_Id(reasonID);
                    if (designationId > 0)
                        model.setDesID(designationId);
                    if (regStatusId > 0)
                        model.setRegStatusID(regStatusId);
                    if (leaveTypeId > 0)
                        model.setLeaveTypeID(leaveTypeId);
                    if (!Strings.isEmptyOrWhitespace(cnic))
                        model.setCNIC(cnic);

                    model.setComments(cursor.getString(cursor.getColumnIndex(Comment)));
                    model.setModified_By(cursor.getInt(cursor.getColumnIndex(Modified_By)));

                    tctEmpSubTagList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return tctEmpSubTagList;
    }

    public void updateUploadedOnInTCTEmpSubTag(ArrayList<GeneralUploadResponseModel> body, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int uploadedCount = 0;
            for (GeneralUploadResponseModel model : body) {
                if (model.server_id > 0) { //must not be null or 0 after upload
                    ContentValues values = new ContentValues();
                    values.put(Uploaded_On, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = DB.update(TABLE_TCT_EMP_SUBJECT_TAGGING, values, ID + " = " + model.server_id, null);
                    if (i > 0) {
                        AppModel.getInstance().appendLog(context, "TCT Employee Subject Tagging Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());

                        uploadedCount++;
                    }
                    AppModel.getInstance().appendLog(context, "TCT Employee Subject Tagging Uploading Successful server id:" + model.server_id);
                } else {
                    AppModel.getInstance().appendErrorLog(context, "TCT Employee Subject Tagging not uploaded!\n" + model.getErrorMessage());
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

    public List<TCTSubjectsModel> getTCTSubjectsWithDesignation(String designation_name, int schoolId) {
        SchoolModel sm = DatabaseHelper.getInstance(context).getSchoolById(schoolId);
        String schoolType = "";
        List<TCTSubjectsModel> tctSubjectsModelList = new ArrayList<>();
        if (sm != null) {
            if (sm.getTypeOfSchool() != null && (sm.getTypeOfSchool().equals("SC") || sm.getTypeOfSchool().equals("HS"))) {
                schoolType = "'Secondary','Middle'";
            } else if (sm.getTypeOfSchool() != null && sm.getTypeOfSchool().equals("PR")) {
                schoolType = "'Primary','Pre-Primary'";
            }

            String selectQuery = "SELECT * FROM " + TABLE_TCT_SUBJECTS
                    + " WHERE " + SchoolType + " IN (@SchoolType)";

            if (!designation_name.isEmpty()) {
                selectQuery += " AND Designation IN (" + designation_name + ") ";
            }

            selectQuery = selectQuery.replace("@SchoolType", schoolType);

            Cursor cursor = null;
            try {
                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        TCTSubjectsModel model = new TCTSubjectsModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        model.setSubject(cursor.getString(cursor.getColumnIndex(Subject)));
                        model.setSchoolType(cursor.getString(cursor.getColumnIndex(SchoolType)));
                        model.setSubjectType(cursor.getString(cursor.getColumnIndex(SubjectType)));
                        model.setDesignation(cursor.getString(cursor.getColumnIndex(Designation)));

                        //it will not add sindhi sub if province is other than sindh
                        if ((model.getId() == 6 || model.getId() == 18 || model.getSubject().equals("Sindhi")) && sm.getProvinceId() != 5) {
                            continue;
                        }

                        tctSubjectsModelList.add(model);
                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return tctSubjectsModelList;
    }
}
