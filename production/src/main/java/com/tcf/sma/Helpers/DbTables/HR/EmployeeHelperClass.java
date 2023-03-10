package com.tcf.sma.Helpers.DbTables.HR;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.GeneralUploadResponseModel;
import com.tcf.sma.Models.HR.EmployeeAttendanceLast30DaysCountModel;
import com.tcf.sma.Models.HR.EmployeePendingAttendanceModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeAutoCompleteModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveStatusModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePendingApprovalModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePositionModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeQualificationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignationStatusModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSchoolModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeTeacherAttendanceModel;
import com.tcf.sma.Models.RetrofitModels.HR.SeparationAttachmentsModel;
import com.tcf.sma.Models.RetrofitModels.HR.UploadEmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.UserImageModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.Services.BasicImageDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmployeeHelperClass {

    public static final String leaveWD = "leave_working_days";
    private static EmployeeHelperClass instance = null;
    public static final String EMPLOYEE_DETAIL_TABLE = "EmployeeDetails";
    public static final String ID = "id";

    public final String Employee_ID = "Employee_ID";
    public static final String Employee_Code = "Employee_Code";
    public final String Emp_First_Name = "First_Name";
    public final String Emp_Last_Name = "Last_Name";
    public final String Emp_Email = "Email";
    public final String Emp_Mobile_No = "Mobile_No";
    public final String Emp_Father_Name = "Father_Name";
    public final String Emp_NIC_No = "NIC_No";
    public final String Emp_Mother_Name = "Mother_Name";
    public final String Emp_CADRE = "CADRE";
    public final String Emp_Date_Of_Joining = "Date_Of_Joining";
    public final String MODIFIED_BY = "modified_by";
    public static final String MODIFIED_ON = "modified_on";
    public final String UPLOADED_ON = "uploaded_on";
    public final String Emp_LastWorkingDay = "LastWorkingDay";
    public final String Emp_Resign_Date = "resignedOn";
    public final String Emp_Resign_Reason = "resignReason";
    public final String Emp_Resign_Type = "resignType";
    public final String Emp_Resign_Letter_IMG = "resignLetterImg";
    public final String Emp_Resign_Form_IMG = "resignFormImg";
    public final String Emp_Resign_Cancel_Reason = "resignCancelReason";
    public final String CREATED_BY = "createdBy";
    public final String CREATED_ON_APP = "createdOnApp";
    public final String CREATED_ON_SERVER = "createdOnServer";
    public final String Device_ID = "deviceID";
    public final String Emp_Status = "status";
    public final String Cancelled_On = "cancelledOn";
    public final String Cancelled_By = "cancelledBy";
    public final String Emp_SchoolId = "schoolId";
    public final String Leave_Without_Pay = "leave_without_pay";
    public final String Emp_SubReason_Text = "Emp_SubReason_Text";
    public final String isActive = "isActive";
    public static final String EMPLOYEE_RESIGNATION_TABLE = "EmployeeResignation";
    public String Employee_Personal_Detail_ID = "emp_detail_id";
    public String serverId = "server_id";
    public String CREATE_TABLE_EMPLOYEE_RESIGNATION = "CREATE TABLE IF NOT EXISTS " + EMPLOYEE_RESIGNATION_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Employee_Personal_Detail_ID + " INTEGER ," +
            Emp_SchoolId + " INTEGER ," +
            isActive + " INTEGER ," +
            Emp_Resign_Date + " TEXT ," +
            Emp_SubReason_Text + " TEXT ," +
            Emp_Resign_Reason + " INTEGER ," +
            Leave_Without_Pay + " INTEGER ," +
            Emp_Resign_Type + " INTEGER ," +
            Emp_Resign_Letter_IMG + " TEXT ," +
            Emp_Resign_Form_IMG + " TEXT ," +
            Emp_Resign_Cancel_Reason + " TEXT ," +
            Emp_LastWorkingDay + " TEXT ," +
            CREATED_BY + " INTEGER ," +
            CREATED_ON_APP + " TEXT ," +
            CREATED_ON_SERVER + " TEXT ," +
            Device_ID + " TEXT ," +
            Emp_Status + " INTEGER ," +
            MODIFIED_ON + " TEXT ," +
            MODIFIED_BY + " TEXT ," +
            Cancelled_On + " TEXT ," +
            Cancelled_By + " INTEGER ," +
            UPLOADED_ON + " TEXT ," +
            serverId + " INTEGER " +
            ");";
    public String SEPARATION_DETAIL_TABLE = "EmployeeSeparationDetail";
    public String PENDING_SEPARATION_TABLE = "PendingSeparations";
    public String Employee_Resignation_Id = "Resignation_id";
    public String Approver_userId = "approver_userId";
    public String HCM_UpdateStatus = "HCM_UpdateStatus";
    public String app_rank = "app_rank";
    public String Separation_Remarks = "separation_remarks";
    public String CREATE_TABLE_SEPARATION_DETAIL = "CREATE TABLE IF NOT EXISTS " + SEPARATION_DETAIL_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            Employee_Resignation_Id + " INTEGER ," +
            Approver_userId + " INTEGER ," +
            Emp_Status + " INTEGER ," +
            HCM_UpdateStatus + " INTEGER ," +
            app_rank + " INTEGER ," +
            Separation_Remarks + " TEXT ," +
            CREATED_BY + " INTEGER ," +
            CREATED_ON_APP + " TEXT ," +
            CREATED_ON_SERVER + " TEXT ," +
            MODIFIED_ON + " TEXT ," +
            MODIFIED_BY + " INTEGER ," +
            Device_ID + " TEXT ," +
//            Cancelled_On + " TEXT ," +
//            Cancelled_By + " INTEGER ," +
            UPLOADED_ON + " TEXT ," +
            serverId + " INTEGER " +
            ");";

    public String CREATE_TABLE_PENDING_SEPARATION = "CREATE TABLE IF NOT EXISTS " + PENDING_SEPARATION_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            Employee_Resignation_Id + " INTEGER ," +
            Approver_userId + " INTEGER ," +
            Emp_Status + " INTEGER ," +
            Separation_Remarks + " TEXT ," +
            CREATED_BY + " INTEGER ," +
            CREATED_ON_APP + " TEXT ," +
            CREATED_ON_SERVER + " TEXT ," +
            MODIFIED_ON + " TEXT ," +
            MODIFIED_BY + " INTEGER ," +
            Device_ID + " TEXT ," +
            UPLOADED_ON + " TEXT ," +
            serverId + " INTEGER " +
            ");";

    public static String TABLE_EmployeesLeaves = "EmployeesLeaves";
    public String Server_ID = "server_id";
    public static String TABLE_EMPLOYEE_TEACHER_Attendance = "EmployeeTeacherAttendance";
    public String For_Date = "for_date";

    //public final String Employee_Resignation_ID = "Emp_Resignation_ID";
    private Context context;
    private String Emp_Designation = "designation";
    private String Emp_Is_Active = "is_active";
    public String Emp_Gender = "gender";
    public String Emp_Job_Status = "job_status";
    public String CREATE_TABLE_EMPLOYEE = "CREATE TABLE IF NOT EXISTS " + EMPLOYEE_DETAIL_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            Employee_ID + " INTEGER ," +
            Employee_Code + " TEXT ," +
            Emp_Designation + " TEXT ," +
            Emp_First_Name + " TEXT ," +
            Emp_Last_Name + " TEXT ," +
            Emp_Email + " TEXT ," +
            Emp_Mobile_No + " TEXT ," +
            Emp_Father_Name + " TEXT ," +
            Emp_Mother_Name + " TEXT ," +
            Emp_NIC_No + " TEXT ," +
            Emp_CADRE + " TEXT ," +
            Emp_Is_Active + " BOOLEAN ," +
            Emp_Date_Of_Joining + " TEXT ," +
            Emp_Job_Status + " TEXT ," +
            Emp_Gender + " TEXT ," +
            Emp_LastWorkingDay + " TEXT ," +
            MODIFIED_BY + " INTEGER ," +
            MODIFIED_ON + " TEXT ," +
            UPLOADED_ON + " TEXT " +
            ");";
    private String TABLE_EmployeeAddressType = "EmployeeAddressType";
    private String EmployeeAddressType_ID = "emp_address_type_id";
    private String Address_Type = "address_type";
    public static final String IsActive = "is_active";
    public String CRAETE_TABLE_EmployeeAddressType = "CREATE TABLE " + TABLE_EmployeeAddressType + " (" +
            EmployeeAddressType_ID + " INTEGER PRIMARY KEY ," +
            Address_Type + " TEXT," +
            IsActive + " BOOLEAN" +
            ");";

    private String Employee_Address_Detail_ID = "emp_address_detail_id";
    private String Address = "address";
    private String TABLE_EmployeeAddressDetail = "TABLE_EmployeeAddressDetail";
    public String CREATE_TABLE_EmployeeAddressDetail = "CREATE TABLE " + TABLE_EmployeeAddressDetail + " (" +
            Employee_Address_Detail_ID + " INTEGER PRIMARY KEY ," +
            Employee_Personal_Detail_ID + " INTEGER," +
            EmployeeAddressType_ID + " INTEGER," +
            Address + " TEXT" +
            ");";
    public static final String TABLE_SeparationImages = "SeparationImages";
    public static final String SeparationAttachment = "Attachment";
    public static final String isUploaded = "isUploaded";

    public String CREATE_TABLE_EmployeeSeparationImages = "CREATE TABLE IF NOT EXISTS " + TABLE_SeparationImages + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Employee_Resignation_Id + " INTEGER ," +
            SeparationAttachment + " TEXT ," +
            isActive + " INTEGER ," +
            isUploaded + " INTEGER ," +
            CREATED_BY + " INTEGER ," +
            CREATED_ON_APP + " TEXT ," +
            CREATED_ON_SERVER + " TEXT ," +
            MODIFIED_ON + " TEXT ," +
            MODIFIED_BY + " TEXT ," +
            UPLOADED_ON + " TEXT ," +
            serverId + " INTEGER " +
            ");";

    public static final String TABLE_UserImages = "User_Images";
    public static final String ImagePath = "Image_Path";
    public String CREATE_TABLE_User_Images = "CREATE TABLE IF NOT EXISTS " + TABLE_UserImages + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Employee_Personal_Detail_ID + " INTEGER ," +
            ImagePath + " TEXT ," +
            UPLOADED_ON + " TEXT ," +
            MODIFIED_ON + " TEXT ," +
            serverId + " INTEGER " +
            ");";
    private String Position_Name = "position_name";
    private String EmployeePosition_ID = "emp_position_id";
    public static String TABLE_EmployeePosition = "EmployeePosition";
    public String Employee_SchoolId = "SchoolID";
    private String Position_Start_Date = "start_date";
    private String Position_End_Date = "end_date";
    public String CREATE_TABLE_EmployeePosition = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeePosition + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Employee_Personal_Detail_ID + " INTEGER ," +
            Employee_SchoolId + " INTEGER ," +
            Employee_Code + " TEXT," +
            Position_Name + " TEXT," +
            Position_Start_Date + " TEXT," +
            MODIFIED_ON + " TEXT ," +
            Position_End_Date + " TEXT" +
            ");";
    public static String TABLE_EmployeeQualificationDetail = "EmployeeQualificationHistory";
    private String Employee_Qualification_Detail_ID = "emp_qualificationDetail_id";
    private String Institute_Name = "Institute_Name";

    private String Degree_Name = "Degree_Name";
    private String Subject_Name = "Subject_Name";
    private String Passing_Year = "Passing_Year";
    private String Grade_Division = "Grade_Division";
    private String EmployeeQualificationType = "QualificationType";
    private String EmployeeQualificationLevel = "QualificationLevel";
    public String CREATE_TABLE_EmployeeQualificationDetail = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeeQualificationDetail + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            Employee_Personal_Detail_ID + " INTEGER," +
            Employee_SchoolId + " INTEGER," +
            Employee_Code + " TEXT," +
            EmployeeQualificationType + " TEXT," +
            EmployeeQualificationLevel + " TEXT," +
            Emp_Date_Of_Joining + " TEXT," +
            Institute_Name + " TEXT," +
            Degree_Name + " TEXT," +
            Subject_Name + " TEXT," +
            Passing_Year + " TEXT," +
            MODIFIED_ON + " TEXT ," +
            Grade_Division + " TEXT" +
            ");";
    private String Employee_Leave_ID = "Employee_Leave_ID";
    private String Leave_Start_Date = "start_date";

    private String Leave_End_Date = "end_date";
    private String created_By = "createdBy";
    private String createdOn_Server = "createdOn_server";
    private String createdOn_App = "createdOn_app";
    private String device_Id = "device_id";
    private String Leave_Type_ID = "leave_type_id";
    private String Leave_Status_id = "status_id";
    public String CREATE_TABLE_EmployeesLeaves = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeesLeaves + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            Employee_Personal_Detail_ID + " INTEGER," +
            Leave_Type_ID + " INTEGER," +
            Leave_Start_Date + " TEXT," +
            Leave_End_Date + " TEXT," +
            leaveWD + " TEXT," +
            Leave_Status_id + " INTEGER," +
            created_By + " INTEGER," +
            createdOn_Server + " TEXT," +
            createdOn_App + " TEXT," +
            UPLOADED_ON + " TEXT," +
            Server_ID + " INTEGER," +
            device_Id + " TEXT," +
            MODIFIED_ON + " TEXT ," +
            Employee_SchoolId + " INTEGER" +
            ");";
    public static String TABLE_Leaves_Type = "EmployeeLeaveType";
    private String Leave_Name = "Name";
    private String Leave_AllowedWorkingDays = "AllowedWorkingDays";
    private String Emp_gender = "gender";
    public String CREATE_TABLE_Leaves_Type = "CREATE TABLE IF NOT EXISTS " + TABLE_Leaves_Type + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Leave_Name + " TEXT," +
            Leave_AllowedWorkingDays + " INTEGER," +
            Emp_gender + " TEXT, " +
            MODIFIED_ON + " TEXT" +
            ");";
    public static String TABLE_EmployeesDesignation = "EmployeesDesignation";
    private String Employee_Designation_ID = "Employee_Designation_ID";
    public static String Designation_Name = "Designation_Name";
    public String CREATE_TABLE_EmployeesDesignation = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeesDesignation + " (" +
            ID + " INTEGER PRIMARY KEY," +
            Designation_Name + " TEXT, " +
            MODIFIED_ON + " TEXT" +
            ");";
    public static String TABLE_EmployeesResignationReason = "EmployeesResignationReason";
    private String Employee_ResignReason_ID = "Employee_ResignReason_ID";
    private String ResignReason = "ResignReason";
    public String SubReason = "SubReason";
    public String Employee_ResignType_ID = "Employee_ResignType_ID";
    public String CREATE_TABLE_EmployeesResignationReason = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeesResignationReason + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            Employee_ResignType_ID + " INTEGER ," +
            isActive + " INTEGER ," +
            SubReason + " TEXT ," +
            ResignReason + " TEXT, " +
            MODIFIED_ON + " TEXT" +
            ");";
    public static String TABLE_EmployeesResignationType = "EmployeesResignationType";
    private String ResignType = "ResignType";
    public String CREATE_TABLE_EmployeesResignationType = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeesResignationType + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            ResignType + " TEXT, " +
            MODIFIED_ON + " TEXT" +
            ");";
    public static String TABLE_EmployeesLeaveStatus = "EmployeesLeaveStatus";
    private String StatusName = "StatusName";
    public String CREATE_TABLE_EmployeesLeaveStatus = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeesLeaveStatus + " (" +
            ID + " INTEGER PRIMARY KEY ," +
//            MODIFIED_ON + " TEXT ," +
            StatusName + " TEXT" +
            ");";
    private String TABLE_EmployeesResignStatus = "EmployeesResignStatus";
    public String CREATE_TABLE_EmployeesResignStatus = "CREATE TABLE IF NOT EXISTS " + TABLE_EmployeesResignStatus + " (" +
            ID + " INTEGER PRIMARY KEY ," +
            StatusName + " TEXT" +
            ");";
    public static String TABLE_EMPLOYEE_SCHOOL = "EmployeeSchool";
    public String CREATE_TABLE_EMPLOYEE_School = "CREATE TABLE IF NOT EXISTS " + TABLE_EMPLOYEE_SCHOOL + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Employee_Personal_Detail_ID + " INTEGER ," +
            Employee_SchoolId + " INTEGER ," +
            MODIFIED_ON + " TEXT ," +
            Employee_Code + " TEXT " +
            ");";
    private String Reason = "Reason";
    private String Attendance_Type_ID = "attendanceType_id";
    public String CREATE_TABLE_EMPLOYEE_TEACHER_Attendance = "CREATE TABLE IF NOT EXISTS " + TABLE_EMPLOYEE_TEACHER_Attendance + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Employee_Personal_Detail_ID + " INTEGER ," +
            For_Date + " TEXT ," +
            Attendance_Type_ID + " INTEGER ," +
            Leave_Type_ID + " INTEGER ," +
            Reason + " TEXT ," +
            created_By + " INTEGER ," +
            createdOn_Server + " TEXT ," +
            createdOn_App + " TEXT ," +
            UPLOADED_ON + " TEXT ," +
            device_Id + " TEXT ," +
            serverId + " INTEGER ," +
            isActive + " BOOLEAN ," +
            MODIFIED_ON + " VARCHAR ," +
            Employee_SchoolId + " INTEGER " +
            ");";
    private String Attendance_Type = "Type";
    public static String TABLE_EMPLOYEE_TeacherAttendanceType = "Employee_TeacherAttendanceType";
    public String CREATE_TABLE_EMPLOYEE_TeacherAttendanceType = "CREATE TABLE IF NOT EXISTS " + TABLE_EMPLOYEE_TeacherAttendanceType + " (" +
            ID + " INTEGER PRIMARY KEY," +
//            MODIFIED_ON + " TEXT ," +
            Attendance_Type + " TEXT " +
            ");";

    public EmployeeHelperClass(Context context) {
        this.context = context;
    }

    public static EmployeeHelperClass getInstance(Context context) {
        if (instance == null)
            instance = new EmployeeHelperClass(context);
        return instance;
    }

    public long empResignationStatus(EmployeeSeparationModel employeeSeparationModel, EmployeeModel employeeModel, List<String> images, Context context) {
        long id = 0;
        //if (employeeModel.getIs_Active()) {
        if (!employeeIsInactive(employeeModel.getId())) {
            try {
                SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();

                ContentValues values = new ContentValues();
                values.put(Emp_SchoolId,employeeSeparationModel.getSchoolID());
                values.put(Employee_Personal_Detail_ID, employeeSeparationModel.getEmployee_Personal_Detail_ID());
                values.put(Emp_Resign_Date, employeeSeparationModel.getEmp_Resign_Date());
                values.put(Emp_Resign_Reason, employeeSeparationModel.getEmp_SubReasonID());
                values.put(Emp_SubReason_Text, employeeSeparationModel.getSubReasonText());
                values.put(Emp_Resign_Type, employeeSeparationModel.getEmp_Resign_Type());
                values.put(Leave_Without_Pay, employeeSeparationModel.getLwop());
                values.put(CREATED_BY, employeeSeparationModel.getCREATED_BY());

                values.put(CREATED_ON_APP, employeeSeparationModel.getCREATED_ON_APP());
                values.put(CREATED_ON_SERVER, employeeSeparationModel.getCREATED_ON_SERVER());
                values.put(Device_ID, employeeSeparationModel.getDeviceId());
                values.put(Emp_Status, employeeSeparationModel.getEmp_Status());
                values.put(UPLOADED_ON, employeeSeparationModel.getUploadedOn());
                values.put(Emp_LastWorkingDay, employeeSeparationModel.getLastWorkingDay());
                values.put(MODIFIED_BY, employeeSeparationModel.getMODIFIED_BY()+"");
                values.put(MODIFIED_ON, employeeSeparationModel.getMODIFIED_ON());
                values.put(serverId, 0);
                values.put(isActive, 1);
                values.put(Emp_Resign_Cancel_Reason, employeeSeparationModel.getEmp_Resign_Cancel_Reason());


                id = DB.insert(EMPLOYEE_RESIGNATION_TABLE, null, values);

                if(id > 0){
                    for(String image: images){
                        try {
                            ContentValues cv = new ContentValues();
                            cv.put(Employee_Resignation_Id, id);
                            cv.put(SeparationAttachment, image);
                            cv.put(isActive, 1);
                            cv.put(isUploaded, 0);
                            cv.put(UPLOADED_ON, (String)null);
                            cv.put(serverId,0);

                            long i = DB.insert(TABLE_SeparationImages,null,cv);
                            if (i <= 0){
                                try {
                                    String errorReport = "";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        errorReport += "Base OS: " + Build.VERSION.BASE_OS;
                                    }
                                    errorReport += "\nSDK INT: " + Build.VERSION.SDK_INT;
                                    errorReport += "\nRelease: " + Build.VERSION.RELEASE;
                                    errorReport += "\nSeparation Images record not inserted, error ID = " + i;
                                    AppModel.getInstance().appendErrorLog(context,errorReport);
                                    AppModel.getInstance().appendLog(context,errorReport);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            }
                        } catch (Exception e) {
                            try {
                                String errorReport = "";
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    errorReport += "Base OS: " + Build.VERSION.BASE_OS;
                                }
                                errorReport += "\nSDK INT: " + Build.VERSION.SDK_INT;
                                errorReport += "\nRelease: " + Build.VERSION.RELEASE;
                                errorReport += "\nSeparation Images record not inserted, error exception = " + e.getMessage();
                                AppModel.getInstance().appendErrorLog(context,errorReport);
                                AppModel.getInstance().appendLog(context,errorReport);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        String errorReport = "";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            errorReport += "Base OS: " + Build.VERSION.BASE_OS;
                        }
                        errorReport += "\nSDK INT: " + Build.VERSION.SDK_INT;
                        errorReport += "\nRelease: " + Build.VERSION.RELEASE;
                        errorReport += "\nSeparation record not inserted, error ID = " + id;
                        AppModel.getInstance().appendErrorLog(context,errorReport);
                        AppModel.getInstance().appendLog(context,errorReport);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                try {
                    String errorReport = "";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        errorReport += "Base OS: " + Build.VERSION.BASE_OS;
                    }
                    errorReport += "\nSDK INT: " + Build.VERSION.SDK_INT;
                    errorReport += "\nRelease: " + Build.VERSION.RELEASE;
                    errorReport += "\nSeparation record not inserted, error exception = " + e.getMessage();
                    AppModel.getInstance().appendErrorLog(context,errorReport);
                    AppModel.getInstance().appendLog(context,errorReport);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return id;
    }

    public long insertOrUpdateUserImage(UserImageModel userImageModel, Context context) {
        long id = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();

            ContentValues values = new ContentValues();
            values.put(Employee_Personal_Detail_ID, userImageModel.getUser_id());
            values.put(ImagePath, userImageModel.getUser_image_path());
            values.put(UPLOADED_ON, userImageModel.getUploaded_on());
            values.put(serverId, 0);
            if (!Strings.isEmptyOrWhitespace(userImageModel.getModifiedOn()))
                values.put(MODIFIED_ON, userImageModel.getModifiedOn());

            if (!FindUserImages(userImageModel.getUser_id())){
                id = DB.insert(TABLE_UserImages, null, values);
            }else {
                id = updateUserImage(userImageModel,context);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public long updateUserImage(UserImageModel userImageModel, Context context) {
        long id = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();

            ContentValues values = new ContentValues();
            values.put(UPLOADED_ON, userImageModel.getUploaded_on());
            values.put(ImagePath, userImageModel.getUser_image_path());
            values.put(serverId, 0);
            if (!Strings.isEmptyOrWhitespace(userImageModel.getModifiedOn()))
                values.put(MODIFIED_ON, userImageModel.getModifiedOn());

            id = DB.update(TABLE_UserImages, values,Employee_Personal_Detail_ID + " = " + userImageModel.getUser_id(),null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private boolean FindUserImages(int emp_detail_id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_UserImages
                    + " WHERE " + Employee_Personal_Detail_ID + " = " + emp_detail_id;

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

    public String getUserImagePath(int empDetailId) {
        String userImagePath = "";
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_UserImages + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId + " ORDER BY id DESC LIMIT 1";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                userImagePath = cursor.getString(cursor.getColumnIndex(ImagePath));
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userImagePath;
    }

    public long empResignationUpdateStatus(EmployeeModel employeeModel, Context context, String EmpJobEndDate, int leaveWithoutPay) {
        long id = 0;
        if (employeeIsInactive(employeeModel.getId())) {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();

            try {
                ContentValues values = new ContentValues();
                values.put(Emp_LastWorkingDay, EmpJobEndDate);
                values.put(UPLOADED_ON, (String) null);
                values.put(CREATED_ON_SERVER, "");
                values.put(MODIFIED_ON,AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                values.put(MODIFIED_BY,DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
                values.put(Leave_Without_Pay, leaveWithoutPay);

                id = DB.update(EMPLOYEE_RESIGNATION_TABLE, values, Employee_Personal_Detail_ID + " = " + employeeModel.getId() + " AND " + ResignType + " = 1 AND " + isActive + " = 1", null);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return id;

    }

    public long empResignationCancelStatus(EmployeeSeparationModel erm, Context context, boolean uploaded) {
        long id = -1;

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();

            if (uploaded) {
                ContentValues values = new ContentValues();
//                values.put(Separation_Remarks, esdm.getSeparation_Remarks());
//                values.put(Emp_Status, esdm.getEmp_status());
                values.put(UPLOADED_ON, (String) null);
                values.put(MODIFIED_ON,erm.getMODIFIED_ON());
                values.put(MODIFIED_BY,erm.getMODIFIED_BY());
                values.put(Cancelled_On,erm.getMODIFIED_ON());
                values.put(Cancelled_By,erm.getMODIFIED_BY());
                values.put(Emp_Resign_Cancel_Reason, erm.getEmp_Resign_Cancel_Reason());
                values.put(isActive, 0);

                id = DB.update(EMPLOYEE_RESIGNATION_TABLE,values, Employee_Personal_Detail_ID + "=" + erm.getEmployee_Personal_Detail_ID() + " AND " + isActive + " = 1", null);

//                id = DB.update(SEPARATION_DETAIL_TABLE, values, Employee_Resignation_Id + "=" + esdm.getEmployeeResignationId() + " AND " + Approver_userId + " = " + esdm.getMODIFIED_BY(), null);

            } else {
                id = DB.delete(EMPLOYEE_RESIGNATION_TABLE, Employee_Personal_Detail_ID + "=" + erm.getEmployee_Personal_Detail_ID() + " AND " + isActive + " = 1", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        }
        return id;
    }



    public boolean employeeIsInactive(int empDetailId) {
        EmployeeSeparationModel erm = null;
        Cursor cursor = null;
//        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId + " AND " + Emp_Status + " IN (1,2) AND " + ResignType + " = 1";

        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId + " AND " + isActive + " = 1";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            return cursor.getCount() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    public boolean employeeIsTerminated(int empDetailId) {
        EmployeeSeparationModel erm = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId ;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                erm = new EmployeeSeparationModel();
                //boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (erm == null)
            return false;
        else if (erm.getLastWorkingDay() == null || (erm.getLastWorkingDay() == ""))
            return false;
        else
            return true;
    }

    public EmployeeSeparationModel getResignedEmployees(int empDetailId) {
        EmployeeSeparationModel erm = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId + " AND " + isActive + " = 1";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                erm = new EmployeeSeparationModel();
                //boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
                erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                erm.setLwop(cursor.getInt(cursor.getColumnIndex(Leave_Without_Pay)));
                erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
                erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                erm.setLwop(cursor.getInt(cursor.getColumnIndex(Leave_Without_Pay)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return erm;
    }


    public EmployeeSeparationModel getSeparationRecord(int serverID) {
        EmployeeSeparationModel erm = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + serverId + " = " + serverID;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                erm = new EmployeeSeparationModel();
                erm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                //boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                erm.setSubReasonText(cursor.getString(cursor.getColumnIndex(Emp_SubReason_Text)));
                erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
                erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                erm.setLwop(cursor.getInt(cursor.getColumnIndex(Leave_Without_Pay)));
                erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
                erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                erm.setLwop(cursor.getInt(cursor.getColumnIndex(Leave_Without_Pay)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return erm;
    }

    public List<EmployeeSeparationDetailModel> getSeparationHistory(int resignationID) {
        List<EmployeeSeparationDetailModel> esdm = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + SEPARATION_DETAIL_TABLE + " WHERE "
                + Employee_Resignation_Id + " = " + resignationID + " ORDER BY " + ID + " ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {

                do {
                    EmployeeSeparationDetailModel erm = new EmployeeSeparationDetailModel();
                    erm.setSeparation_Remarks(cursor.getString(cursor.getColumnIndex(Separation_Remarks)));
                    erm.setEmp_status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                    erm.setApprover_userId(cursor.getInt(cursor.getColumnIndex(Approver_userId)));
                    erm.setMODIFIED_ON(cursor.getString(cursor.getColumnIndex(MODIFIED_ON)));

                    esdm.add(erm);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return esdm;
    }

    public EmployeeSeparationModel getResignedEmployee(int empDetailId) {
        EmployeeSeparationModel erm = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId + " AND " + isActive + " = 1";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                erm = new EmployeeSeparationModel();
                erm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                erm.setSubReasonText(cursor.getString(cursor.getColumnIndex(Emp_SubReason_Text)));
//                erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
//                erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
//                erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                erm.setLwop(cursor.getInt(cursor.getColumnIndex(Leave_Without_Pay)));
                erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                erm.setUploadedOn(cursor.getString(cursor.getColumnIndex(UPLOADED_ON)));
                erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return erm;
    }

    public List<EmployeeSeparationModel> getInActiveEmployees(int schoolId, int status, int ResignType, String firstName, String lastName) {

        List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();
        Cursor cursor = null;

        try {
//            String selectQuery = "SELECT emp_res.* from " + EMPLOYEE_RESIGNATION_TABLE + " emp_res" +
//                    " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_School ON emp_School." + Employee_Personal_Detail_ID + " = emp_res." + Employee_Personal_Detail_ID;
//
//

            String selectQuery = "SELECT emp_detail.*, emp_res.*, sep_det.status as sep_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + SEPARATION_DETAIL_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + ID +
                    " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_School ON emp_School." + Employee_Personal_Detail_ID + " = emp_detail." + ID;

            selectQuery += " WHERE sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();

            if(schoolId > 0)
                selectQuery += " AND emp_School." + Employee_SchoolId + " = " + schoolId;

            if (ResignType == 1 || ResignType == 2 || ResignType == 3) {
                selectQuery += " AND emp_res." + Emp_Resign_Type + " = '" + ResignType + "'";
            }

            if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
                selectQuery += " AND sep_det." + Emp_Status + " = '" + status + "'";
            }

            if (firstName != null && !firstName.equals("")) {
                selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "'";
                if (lastName != null && !lastName.equals("")) {
                    selectQuery += " AND emp_detail." + Emp_Last_Name + " = '" + lastName + "'";
                }
            }

            selectQuery += " ORDER BY emp_res." + ID;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeSeparationModel erm = new EmployeeSeparationModel();
                    erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                    erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                    erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                    erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
                    erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                    erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                    erm.setEmp_Resign_Cancel_Reason(cursor.getString(cursor.getColumnIndex(Emp_Resign_Cancel_Reason)));
                    erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
                    erm.setSep_status(cursor.getInt(cursor.getColumnIndex("sep_status")));

                    employeeSeparationModelList.add(erm);
//
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return employeeSeparationModelList;
    }


    public List<EmployeeSeparationModel> getViewSeparations(int schoolId, int status, int ResignType, String firstName, String lastName) {
        boolean statusSelected = false;
        boolean schoolSelected = false;
        List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT emp_res.*, sep_det.status as sep_status, MIN(sep_det.app_rank) from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + SEPARATION_DETAIL_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId;

            selectQuery += " GROUP BY  emp_res." + ID;

            if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
                selectQuery += " HAVING sep_det." + Emp_Status + " = " + status;
                statusSelected = true;
            }

            if(schoolId > 0){
                if(statusSelected)
                    selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolId;
                else
                    selectQuery += " HAVING emp_res." + Emp_SchoolId + " = " + schoolId;
                schoolSelected = true;
            }

            if (firstName != null && !firstName.equals("")) {
                if(statusSelected || schoolSelected)
                    selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "'";
                else
                    selectQuery += " HAVING emp_detail." + Emp_First_Name + " = '" + firstName + "'";

                if (lastName != null && !lastName.equals("")) {
                    selectQuery += " AND emp_detail." + Emp_Last_Name + " = '" + lastName + "'";
                }
            }

            selectQuery += " ORDER BY emp_res." + ID + " DESC";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeSeparationModel erm = new EmployeeSeparationModel();
                    erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                    erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                    erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                    erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
                    erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                    erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                    erm.setEmp_Resign_Cancel_Reason(cursor.getString(cursor.getColumnIndex(Emp_Resign_Cancel_Reason)));
                    erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
                    erm.setSep_status(cursor.getInt(cursor.getColumnIndex("sep_status")));

                    employeeSeparationModelList.add(erm);
//
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return employeeSeparationModelList;
    }


    public List<EmployeeSeparationModel> getPendingApprovals(int schoolID, String firstName, String lastName) {

        List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT emp_detail.*, emp_res.*, sep_det.status as sep_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + PENDING_SEPARATION_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId +
                    " WHERE sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId() +
                    " AND sep_det." + Emp_Status + " = 1 AND emp_res.isActive = 1";

            if(schoolID > 0)
                selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolID;

            if (firstName != null && !firstName.equals("")) {
                selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "'";
                if (lastName != null && !lastName.equals("")) {
                    selectQuery += " AND emp_detail." + Emp_Last_Name + " = '" + lastName + "'";
                }
            }

            selectQuery += " ORDER BY emp_res." + ID;


            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeSeparationModel erm = new EmployeeSeparationModel();
                    erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                    erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                    erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                    erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
                    erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
                    erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                    erm.setEmp_Resign_Cancel_Reason(cursor.getString(cursor.getColumnIndex(Emp_Resign_Cancel_Reason)));
                    erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
                    erm.setSep_status(cursor.getInt(cursor.getColumnIndex("sep_status")));

                    employeeSeparationModelList.add(erm);
//
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return employeeSeparationModelList;
    }

    public int getEmployeeEmailStats(int schoolId) {
        int EmailCount = 0;
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT count(EmployeeDetails.Email) as emp_email_count from EmployeeDetails" +
                    " INNER JOIN EmployeeSchool emp_School ON emp_School.emp_detail_id = EmployeeDetails.id" +
                    " WHERE emp_School.SchoolID = " + schoolId +
                    " AND (EmployeeDetails.Email != '')" +
                    " AND (EmployeeDetails.LastWorkingDay IS NULL OR EmployeeDetails.LastWorkingDay = ''" +
                    " OR STRFTIME('%Y-%m-%d',EmployeeDetails.LastWorkingDay) > STRFTIME('%Y-%m-%d',DATE('now')))" +
                    " AND EmployeeDetails.is_active = 1";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                EmailCount = cursor.getInt(cursor.getColumnIndex("emp_email_count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return EmailCount;
    }



    public int getEmployeeMobileStats(int schoolId) {
        int MobileCount = 0;
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT count(EmployeeDetails.Mobile_No) as emp_mobile_count from EmployeeDetails" +
                    " INNER JOIN EmployeeSchool emp_School ON emp_School.emp_detail_id = EmployeeDetails.id" +
                    " WHERE emp_School.SchoolID = " + schoolId +
                    " AND (EmployeeDetails.Mobile_No != '')" +
                    " AND (EmployeeDetails.LastWorkingDay IS NULL OR EmployeeDetails.LastWorkingDay = ''" +
                    " OR STRFTIME('%Y-%m-%d',EmployeeDetails.LastWorkingDay) > STRFTIME('%Y-%m-%d',DATE('now')))" +
                    " AND EmployeeDetails.is_active = 1";

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                MobileCount = cursor.getInt(cursor.getColumnIndex("emp_mobile_count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return MobileCount;
    }

    public List<EmployeeModel> getEmployees(int schoolId, String designation, String cadre, String status) {
        List<EmployeeModel> empList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp.* from " + EMPLOYEE_DETAIL_TABLE + " emp INNER JOIN " + TABLE_EMPLOYEE_SCHOOL
                    + " emp_School ON emp_School." + Employee_Personal_Detail_ID + " = emp." + ID;

            selectQuery += " WHERE emp_School." + Employee_SchoolId + " = " + schoolId;
            selectQuery += " AND (emp." + Emp_LastWorkingDay + " IS NULL OR emp." + Emp_LastWorkingDay + " = '' OR STRFTIME('%Y-%m-%d',emp." + Emp_LastWorkingDay + ") >= STRFTIME('%Y-%m-%d',DATE('now')))";

            selectQuery += " AND emp." + Emp_Is_Active + " = 1";
            if (!designation.isEmpty() && !designation.equals("All")) {
                selectQuery += " AND emp." + Emp_Designation + " = '" + designation + "'";
            }

            if (!cadre.isEmpty()) {
                selectQuery += " AND emp." + Emp_CADRE + " = '" + cadre + "'";
            }

            if (!status.isEmpty() && status.equals("Active")) {
                selectQuery += " AND emp." + Emp_Job_Status + " = 'Active'";
            } else if (!status.isEmpty() && status.equals("Resigned")) {
                selectQuery += " AND emp." + Emp_Job_Status + " != 'Active'";
            }

            selectQuery += " ORDER BY emp." + ID;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    if (cursor.getString(cursor.getColumnIndex(Emp_CADRE)) != null) {
                        EmployeeModel employeeModel = new EmployeeModel();

                        boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                        employeeModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        employeeModel.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                        employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                        employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                        employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                        employeeModel.setFather_Name(cursor.getString(cursor.getColumnIndex(Emp_Father_Name)));
                        employeeModel.setMother_Name(cursor.getString(cursor.getColumnIndex(Emp_Mother_Name)));
                        employeeModel.setDate_Of_Joining(cursor.getString(cursor.getColumnIndex(Emp_Date_Of_Joining)));
                        employeeModel.setCADRE(cursor.getString(cursor.getColumnIndex(Emp_CADRE)));
                        employeeModel.setDesignation(cursor.getString(cursor.getColumnIndex(Emp_Designation)));
                        employeeModel.setEmail(cursor.getString(cursor.getColumnIndex(Emp_Email)));
                        employeeModel.setMobile_No(cursor.getString(cursor.getColumnIndex(Emp_Mobile_No)));
                        employeeModel.setNIC_No(cursor.getString(cursor.getColumnIndex(Emp_NIC_No)));
                        employeeModel.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
//                    employeeModel.setMiddle_Name(cursor.getString(cursor.getColumnIndex(Emp_Middle_Name)));
//                    employeeModel.setJob_End_Date(cursor.getString(cursor.getColumnIndex(Emp_Job_End_Date)));
                        employeeModel.setJob_Status(cursor.getString(cursor.getColumnIndex(Emp_Job_Status)));
                        employeeModel.setGender(cursor.getString(cursor.getColumnIndex(Emp_Gender)));
//                    employeeModel.setDOB(cursor.getString(cursor.getColumnIndex(Emp_DOB)));
//                    employeeModel.setDivision_Name(cursor.getString(cursor.getColumnIndex(Emp_Division_Name)));
                        employeeModel.setIs_Active(b);

                        empList.add(employeeModel);
                    }
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

    public List<EmployeeModel> getSearchedEmployees(int schoolId) {
        List<EmployeeModel> empList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp.* from " + EMPLOYEE_DETAIL_TABLE + " emp INNER JOIN " + TABLE_EMPLOYEE_SCHOOL
                    + " emp_School ON emp_School." + Employee_Personal_Detail_ID + " = emp." + ID
                    + " WHERE emp_School." + Employee_SchoolId + " = " + schoolId
                    + " AND (emp." + Emp_LastWorkingDay + " IS NULL OR emp." + Emp_LastWorkingDay + " = '' OR STRFTIME('%Y-%m-%d',emp." + Emp_LastWorkingDay + ") > STRFTIME('%Y-%m-%d',DATE('now')))"
                    + " AND emp." + Emp_Is_Active + " = 1"
                    + " ORDER BY emp." + Emp_First_Name + " ASC";


//            if (!designation.isEmpty() && !designation.equals("All")) {
//                selectQuery += " AND emp." + Emp_Designation + " = '" + designation + "'";
//            }
//
//            if (!cadre.isEmpty()) {
//                selectQuery += " AND emp." + Emp_CADRE + " = '" + cadre + "'";
//            }


            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeModel employeeModel = new EmployeeModel();

                    boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                    employeeModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    employeeModel.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                    employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                    employeeModel.setFather_Name(cursor.getString(cursor.getColumnIndex(Emp_Father_Name)));
                    employeeModel.setMother_Name(cursor.getString(cursor.getColumnIndex(Emp_Mother_Name)));
                    employeeModel.setDate_Of_Joining(cursor.getString(cursor.getColumnIndex(Emp_Date_Of_Joining)));
                    employeeModel.setCADRE(cursor.getString(cursor.getColumnIndex(Emp_CADRE)));
                    employeeModel.setDesignation(cursor.getString(cursor.getColumnIndex(Emp_Designation)));
                    employeeModel.setEmail(cursor.getString(cursor.getColumnIndex(Emp_Email)));
                    employeeModel.setMobile_No(cursor.getString(cursor.getColumnIndex(Emp_Mobile_No)));
                    employeeModel.setNIC_No(cursor.getString(cursor.getColumnIndex(Emp_NIC_No)));
                    employeeModel.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    employeeModel.setAttendanceType_id(1);      //By default present
                    employeeModel.setIs_Active(b);

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

    public long updateEmployeeDetail(EmployeeModel employeeModel) {
        long id = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            ContentValues cv = new ContentValues();
            cv.put(Emp_Mobile_No, employeeModel.getMobile_No());
            cv.put(Emp_Email, employeeModel.getEmail());
            cv.put(MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            cv.put(MODIFIED_BY, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            cv.put(UPLOADED_ON, (String) null);
            id = DB.update(EMPLOYEE_DETAIL_TABLE, cv, ID + " = " + employeeModel.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<EmployeePositionModel> getPositionHistory(EmployeeModel employeeModel) {
        List<EmployeePositionModel> employeePositionModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT s.school_name,emp_pos.* FROM " + TABLE_EmployeePosition + " emp_pos\n" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_pos.emp_detail_id\n" +
                " INNER JOIN school s ON s.id = emp_pos.SchoolID WHERE emp_pos.emp_detail_id = " + employeeModel.getId() +
                " ORDER BY emp_pos.start_date DESC";
//        String selectQuery = "SELECT s.school_name,emp_pos.position_name,emp_company.* FROM EmployeeCompanyDetail emp_company\n" +
//                " INNER JOIN employee_details emp_detail ON emp_detail.id = emp_company.emp_detail_id\n" +
//                " INNER JOIN EmployeePosition emp_pos ON emp_pos.id = emp_company.emp_position_id\n" +
//                " INNER JOIN EmployeeSchoolDetail emp_school ON emp_school.emp_detail_id = emp_detail.id\n" +
//                " INNER JOIN school s ON s.id = emp_school.SchoolID WHERE emp_company.emp_detail_id = " + employeeModel.getId();
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeePositionModel model = new EmployeePositionModel();
                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    model.setPosition_Start_Date(cursor.getString(cursor.getColumnIndex(Position_Start_Date)));
                    model.setPosition_End_Date(cursor.getString(cursor.getColumnIndex(Position_End_Date)));
                    model.setSchool_Name(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHOOL_NAME)));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
                    model.setEmp_Designation(cursor.getString(cursor.getColumnIndex(Position_Name)));
                    employeePositionModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return employeePositionModels;
    }

    public EmployeeModel getEmployee(int empDetailId) {
        EmployeeModel employeeModel = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + EMPLOYEE_DETAIL_TABLE + " WHERE " + ID + " = " + empDetailId;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                employeeModel = new EmployeeModel();
                boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                employeeModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                employeeModel.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                employeeModel.setFather_Name(cursor.getString(cursor.getColumnIndex(Emp_Father_Name)));
                employeeModel.setMother_Name(cursor.getString(cursor.getColumnIndex(Emp_Mother_Name)));
                employeeModel.setDate_Of_Joining(cursor.getString(cursor.getColumnIndex(Emp_Date_Of_Joining)));
                employeeModel.setCADRE(cursor.getString(cursor.getColumnIndex(Emp_CADRE)));
                employeeModel.setDesignation(cursor.getString(cursor.getColumnIndex(Emp_Designation)));
                employeeModel.setEmail(cursor.getString(cursor.getColumnIndex(Emp_Email)));
                employeeModel.setMobile_No(cursor.getString(cursor.getColumnIndex(Emp_Mobile_No)));
                employeeModel.setNIC_No(cursor.getString(cursor.getColumnIndex(Emp_NIC_No)));
                employeeModel.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
//                employeeModel.setMiddle_Name(cursor.getString(cursor.getColumnIndex(Emp_Middle_Name)));
//                employeeModel.setJob_End_Date(cursor.getString(cursor.getColumnIndex(Emp_Job_End_Date)));
//                employeeModel.setJob_Status(cursor.getString(cursor.getColumnIndex(Emp_Job_Status)));
//                employeeModel.setDOB(cursor.getString(cursor.getColumnIndex(Emp_DOB)));
//                employeeModel.setDivision_Name(cursor.getString(cursor.getColumnIndex(Emp_Division_Name)));
                employeeModel.setGender(cursor.getString(cursor.getColumnIndex(Emp_Gender)));
                employeeModel.setJob_Status(cursor.getString(cursor.getColumnIndex(Emp_Job_Status)));
                employeeModel.setIs_Active(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return employeeModel;
    }

    public List<EmployeeSeparationModel> getResignationHistory(int empDetailId) {
        List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId;

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeSeparationModel erm = new EmployeeSeparationModel();
                    erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                    erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                    erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                    erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
                    employeeSeparationModelList.add(erm);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return employeeSeparationModelList;
    }

    public List<EmployeeQualificationDetailModel> getQualificationHistory(int employee_id) {
        List<EmployeeQualificationDetailModel> employeeQualificationDetailModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT empQual.* FROM " + TABLE_EmployeeQualificationDetail + " empQual\n" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " empDetail ON empDetail.id = empQual.emp_detail_id\n" +
                " WHERE empQual.emp_detail_id = " + employee_id + " AND (ifnull(empQual.QualificationLevel,'') != '' OR ifnull(empQual.QualificationType,'') != '' OR ifnull(empQual.Passing_Year,'') != '') " +
                " ORDER BY empQual.Passing_Year DESC";

//                " INNER JOIN EmployeeQualificationLevel empQualLevel ON  empQualLevel.id = empQual.emp_qualificationLevel_id\n" +
//                " INNER JOIN EmployeeQualificationType empQualType ON empQualType.id = empQual.emp_qualificationType_id\n" +
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeQualificationDetailModel model = new EmployeeQualificationDetailModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setInstitute_Name(cursor.getString(cursor.getColumnIndex(Institute_Name)));
                    model.setSchoolID(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
                    model.setDegree_Name(cursor.getString(cursor.getColumnIndex(Degree_Name)));
                    model.setSubject_Name(cursor.getString(cursor.getColumnIndex(Subject_Name)));
                    model.setPassing_Year(cursor.getString(cursor.getColumnIndex(Passing_Year)));
                    model.setGrade_Division(cursor.getString(cursor.getColumnIndex(Grade_Division)));
                    model.setQualification_Level(cursor.getString(cursor.getColumnIndex(EmployeeQualificationLevel)));
                    model.setQualification_Type(cursor.getString(cursor.getColumnIndex(EmployeeQualificationType)));
//                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
//                    model.setEmployeeQualificationType_ID(cursor.getInt(cursor.getColumnIndex(EmployeeQualificationType_ID)));
//                    model.setEmployeeQualificationLevel_ID(cursor.getInt(cursor.getColumnIndex(EmployeeQualificationLevel_ID)));
                    employeeQualificationDetailModels.add(model);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return employeeQualificationDetailModels;
    }

    public long insertLeave(EmployeeModel employeeModel, EmployeeLeaveModel employeeLeaveModel) {
        long id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(Employee_Personal_Detail_ID, employeeModel.getId());
            cv.put(Leave_Type_ID, employeeLeaveModel.getLeave_Type_ID());
            cv.put(Leave_Start_Date, employeeLeaveModel.getLeave_Start_Date());
            cv.put(Leave_End_Date, employeeLeaveModel.getLeave_End_Date());
            cv.put(Leave_Status_id, 1);      //by default pending
            cv.put(leaveWD, employeeLeaveModel.getLeavesLWD());
            cv.put(created_By, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            cv.put(createdOn_App, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
            cv.put(MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
            cv.put(Server_ID, 0);       //by default 0
            cv.put(device_Id, AppModel.getInstance().getDeviceId(context));
            cv.put(Employee_SchoolId, employeeLeaveModel.getSchoolId());
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            id = DB.insert(TABLE_EmployeesLeaves, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<EmployeeLeaveTypeModel> getLeaveReason() {
        List<EmployeeLeaveTypeModel> leaveTypeModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_Leaves_Type;
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeLeaveTypeModel model = new EmployeeLeaveTypeModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setLeave_Name(cursor.getString(cursor.getColumnIndex(Leave_Name)));
                    model.setLeave_AllowedWorkingDays(cursor.getInt(cursor.getColumnIndex(Leave_AllowedWorkingDays)));
                    model.setEmp_gender(cursor.getString(cursor.getColumnIndex(Emp_gender)));
                    leaveTypeModelList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return leaveTypeModelList;
    }


    public List<EmployeeLeaveModel> getLeavesOfEmployee(EmployeeModel employeeModel,int schoolId) {
        List<EmployeeLeaveModel> leaves = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT emp_leaves.*,emp_status." + StatusName + " FROM " + TABLE_EmployeesLeaves + " emp_leaves " +
                " INNER JOIN " + TABLE_EmployeesLeaveStatus + " emp_status ON emp_status." + ID + " = emp_leaves." + Leave_Status_id +
                " WHERE " + Employee_Personal_Detail_ID + " = " + employeeModel.getId() +
                " AND " + Employee_SchoolId + " = " + schoolId;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeLeaveModel model = new EmployeeLeaveModel();
                    model.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    model.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex(Leave_Start_Date)));
                    model.setLeave_End_Date(cursor.getString(cursor.getColumnIndex(Leave_End_Date)));
                    model.setLeave_status(cursor.getString(cursor.getColumnIndex(StatusName)));
                    leaves.add(model);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return leaves;
    }

    public List<EmployeeLeaveModel> getLeavesOfAllEmployeesBy(int schoolId, String filter_leavesBy, String filter_leavesByStatus) {
        List<EmployeeLeaveModel> leaves = new ArrayList<>();
        Cursor cursor = null;
//        String selectQuery = "SELECT empLeave.*,empDetails.First_Name,empDetails.Last_Name FROM " + TABLE_EmployeesLeaves + " empLeave" +
//                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " empDetails ON empDetails.id = empLeave.emp_detail_id" +
//                " INNER JOIN EmployeeSchoolDetail emp_school ON emp_school.emp_detail_id = empDetails.id" +
//                " WHERE emp_school.SchoolID = @SchoolID";

        String selectQuery = "SELECT empLeave.*,empDetails.First_Name,empDetails.Last_Name, empDetails.Employee_Code,emp_status.StatusName FROM " + TABLE_EmployeesLeaves + " empLeave" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " empDetails ON empDetails.id = empLeave.emp_detail_id" +
                " INNER JOIN " + TABLE_EmployeesLeaveStatus + " emp_status ON emp_status." + ID + " = empLeave." + Leave_Status_id +
                " WHERE empLeave.SchoolID = @SchoolID";

        if (filter_leavesBy.equals("f")) {
            selectQuery += " AND STRFTIME('%Y-%m-%d',start_date) > STRFTIME('%Y-%m-%d',DATE('now'))";
        } else if (filter_leavesBy.equals("c")) {
            selectQuery += " AND STRFTIME('%Y-%m-%d',start_date) <= STRFTIME('%Y-%m-%d',DATE('now')) AND STRFTIME('%Y-%m-%d',end_date) >= STRFTIME('%Y-%m-%d',DATE('now'))";
        } else if (filter_leavesBy.equals("p")) {
            selectQuery += " AND STRFTIME('%Y-%m-%d',start_date) < STRFTIME('%Y-%m-%d',DATE('now')) AND STRFTIME('%Y-%m-%d',end_date) < STRFTIME('%Y-%m-%d',DATE('now'))";
        }


        if (!filter_leavesByStatus.isEmpty() && !filter_leavesByStatus.equals("All")) {
            selectQuery += " AND emp_status." + StatusName + " = '" + filter_leavesByStatus + "'";
        }


        selectQuery += " group by empLeave." + Employee_Personal_Detail_ID + ",empLeave." + Leave_Start_Date;

        selectQuery = selectQuery.replace("@SchoolID", schoolId + "");

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeLeaveModel model = new EmployeeLeaveModel();
                    model.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    model.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex(Leave_Start_Date)));
                    model.setLeave_End_Date(cursor.getString(cursor.getColumnIndex(Leave_End_Date)));
                    model.setLeave_status(cursor.getString(cursor.getColumnIndex(StatusName)));

                    EmployeeModel employeeModel = new EmployeeModel();
                    employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                    employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    model.setEmployeeModel(employeeModel);
                    leaves.add(model);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return leaves;
    }

    public EmployeeLeaveModel getLeavesByEmployeeDetailId(int employeeDetailId) {
        EmployeeLeaveModel model = null;
        Cursor cursor = null;
        String selectQuery = "SELECT emp_leave.*,emp_leave_type.Name as leave_reason FROM " + TABLE_EmployeesLeaves + " emp_leave" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_details ON emp_details." + ID + " = emp_leave." + Employee_Personal_Detail_ID +
                " INNER JOIN " + TABLE_Leaves_Type + " emp_leave_type ON emp_leave_type." + ID + " = emp_leave." + Leave_Type_ID +
                " WHERE emp_details.id = @EmpDetailId" +
                " AND strftime('%s', 'now') BETWEEN strftime('%s', emp_leave.start_date) AND strftime('%s', emp_leave.end_date)";

        selectQuery = selectQuery.replace("@EmpDetailId", employeeDetailId + "");
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                model = new EmployeeLeaveModel();
                model.setLeave_Type_ID(cursor.getInt(cursor.getColumnIndex(Leave_Type_ID)));
                model.setLeave_Reason(cursor.getString(cursor.getColumnIndex("leave_reason")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return model;
    }

    public List<EmployeeDesignationModel> getDesignations() {
        List<EmployeeDesignationModel> designationModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesDesignation;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeDesignationModel model = new EmployeeDesignationModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setDesignation_Name(cursor.getString(cursor.getColumnIndex(Designation_Name)));
                    designationModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return designationModels;
    }

    public List<EmployeeDesignationModel> getDesignations(int schoolId) {
        List<EmployeeDesignationModel> designationModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT DISTINCT empDesg.Designation_Name,empDesg.id FROM " + TABLE_EmployeesDesignation + " empDesg" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp ON emp.designation = empDesg.Designation_Name" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " empSchool ON empSchool.emp_detail_id = emp.id" +
                " WHERE empSchool.SchoolID = " + schoolId + " GROUP BY empDesg.Designation_Name ORDER BY empDesg.Designation_Name ASC";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        EmployeeDesignationModel model = new EmployeeDesignationModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        model.setDesignation_Name(cursor.getString(cursor.getColumnIndex(Designation_Name)));
                        designationModels.add(model);
                    } while (cursor.moveToNext());
                }
            } else {
                cursor = null;
                selectQuery = "SELECT DISTINCT empDesg.Designation_Name,empDesg.id FROM " + TABLE_EmployeesDesignation + " empDesg" +
                        " GROUP BY empDesg.Designation_Name ORDER BY empDesg.Designation_Name ASC";

                try {
                    DB = DatabaseHelper.getInstance(context).getDB();
                    cursor = DB.rawQuery(selectQuery, null);

                    if (cursor.moveToFirst()) {
                        do {
                            EmployeeDesignationModel model = new EmployeeDesignationModel();
                            model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                            model.setDesignation_Name(cursor.getString(cursor.getColumnIndex(Designation_Name)));
                            designationModels.add(model);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return designationModels;
    }

    public long insertIntoTable(ContentValues cv, String table) {
        long id = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            id = DB.insert(table, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


    public List<EmployeeResignReasonModel> getResignReason() {
        List<EmployeeResignReasonModel> resignReasonModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason +
                "  ORDER BY ResignReason";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeResignReasonModel model = new EmployeeResignReasonModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setResignReason(cursor.getString(cursor.getColumnIndex(ResignReason)));
                    resignReasonModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignReasonModels;
    }

    public List<EmployeeResignReasonModel> getResignReasons(int resignType) {
        List<EmployeeResignReasonModel> resignReasonModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason +
                " WHERE " + Employee_ResignType_ID + " = " + resignType +"  GROUP BY ResignReason ORDER BY ResignReason";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeResignReasonModel model = new EmployeeResignReasonModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setResignReason(cursor.getString(cursor.getColumnIndex(ResignReason)));
                    resignReasonModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignReasonModels;
    }


    public List<EmployeeResignReasonModel> getReasonAndSubReason(int subReasonId) {
        List<EmployeeResignReasonModel> resignReasonModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason +
                " WHERE " + ID + " = " + subReasonId;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeResignReasonModel model = new EmployeeResignReasonModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setResignReason(cursor.getString(cursor.getColumnIndex(ResignReason)));
                    model.setSubReason(cursor.getString(cursor.getColumnIndex(SubReason)));
                    resignReasonModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignReasonModels;
    }


    public List<String> getSeparationAttachments(int id) {
        List<String> sepImages = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_SeparationImages +
                " WHERE " + Employee_Resignation_Id + " = " + id;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    sepImages.add(cursor.getString(cursor.getColumnIndex(SeparationAttachment)));
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

    public List<UserImageModel> getUserImagesForUpload() {
        List<UserImageModel> userImages = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_UserImages +
                " WHERE (" + UPLOADED_ON + " IS NULL OR "+ UPLOADED_ON + " = '')";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    UserImageModel userImageModel = new UserImageModel();
                    userImageModel.setUser_image_path(cursor.getString(cursor.getColumnIndex(ImagePath)));
                    userImageModel.setUser_id(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    userImages.add(userImageModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userImages;
    }

    public List<SeparationAttachmentsModel> getSeparationImagesForUpload(int id) {
        List<SeparationAttachmentsModel> sepImages = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_SeparationImages +
                " WHERE " + isUploaded + " = 1 AND (" + UPLOADED_ON + " IS NULL OR "+ UPLOADED_ON + " = '')";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    SeparationAttachmentsModel separationAttachmentsModel = new SeparationAttachmentsModel();
                    separationAttachmentsModel.setSeparationAttachment(cursor.getString(cursor.getColumnIndex(SeparationAttachment)));
                    separationAttachmentsModel.setResignationID(cursor.getInt(cursor.getColumnIndex(Employee_Resignation_Id)));
                    sepImages.add(separationAttachmentsModel);
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


    public List<EmployeeResignReasonModel> getSubReasons(String resignReason) {
        List<EmployeeResignReasonModel> resignReasonModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason +
                " WHERE " + ResignReason + " = '" + resignReason +"' ORDER BY SubReason";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeResignReasonModel model = new EmployeeResignReasonModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setResignReason(cursor.getString(cursor.getColumnIndex(SubReason)));
                    resignReasonModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignReasonModels;
    }

    public EmployeeResignReasonModel getResignReason(int resignReasonID) {
        EmployeeResignReasonModel errm = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason + " WHERE " + ID + " = " + resignReasonID;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                errm = new EmployeeResignReasonModel();
                errm.setID(resignReasonID);
                errm.setResignReason(cursor.getString(cursor.getColumnIndex(ResignReason)));
                errm.setSubReason(cursor.getString(cursor.getColumnIndex(SubReason)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return errm;
    }

    public List<EmployeeResignTypeModel> getResignType() {
        List<EmployeeResignTypeModel> resignTypeModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationType;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeResignTypeModel model = new EmployeeResignTypeModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setResignType(cursor.getString(cursor.getColumnIndex(ResignType)));
                    resignTypeModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignTypeModels;
    }

    public List<EmployeeLeaveStatusModel> getLeaveStatus() {
        List<EmployeeLeaveStatusModel> leaveStatusModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaveStatus;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeLeaveStatusModel model = new EmployeeLeaveStatusModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setStatusDescription(cursor.getString(cursor.getColumnIndex(StatusName)));
                    leaveStatusModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return leaveStatusModels;
    }


    public List<EmployeeResignationStatusModel> getResignStatus() {
        List<EmployeeResignationStatusModel> resignStatusModels = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignStatus;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeResignationStatusModel model = new EmployeeResignationStatusModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setStatusDescription(cursor.getString(cursor.getColumnIndex(StatusName)));
                    resignStatusModels.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignStatusModels;
    }

    public long leaveAlreadyExists(EmployeeModel employeeModel, String startDate, String endDate, String schoolIds) {
        Cursor cursor = null;
//        String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves + " WHERE " + Employee_Personal_Detail_ID + " = " + employeeModel.getId() +
//                " AND " + Leave_Start_Date + " >= '@date' AND " + Leave_End_Date + " <= '@date'";

        String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves + " WHERE " + Employee_Personal_Detail_ID + " = " + employeeModel.getId() +
                " AND (('" + startDate + "' BETWEEN " + Leave_Start_Date + " AND " + Leave_End_Date +") OR ('" + endDate + "' BETWEEN " + Leave_Start_Date +
                " AND " +Leave_End_Date + ") OR (" + Leave_Start_Date + " BETWEEN '" + startDate +"' AND '" + endDate + "') OR (" + Leave_End_Date +
                " BETWEEN '" + startDate + "' AND '" + endDate + "'))" +
                " AND " + Employee_SchoolId + " IN (" + schoolIds+ ")";
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
//            selectQuery = selectQuery.replaceAll("@date", startDate);
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return 1;
            } else {
                selectQuery = selectQuery.replaceAll("@date", endDate);
                cursor = DB.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    return 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public int getPendingLeavesCount(int school_id) {
        int count = 0;
        String selectQuery = "SELECT COUNT(emp_leaves.id) AS pending FROM " + TABLE_EmployeesLeaves + " emp_leaves" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
                " WHERE emp_detail." + IsActive + "= 1 AND emp_leaves.SchoolID = @SchoolID AND emp_leaves." + Leave_Status_id + " = 1";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");

//        String selectQuery = "SELECT COUNT(emp_leaves.id) AS pending FROM "+TABLE_EmployeesLeaves+" emp_leaves" +
//                " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID AND emp_leaves.createdOn_server IS NULL OR emp_leaves.createdOn_server = ''";

//        String selectQuery = "SELECT COUNT(*) AS pending FROM " + TABLE_EmployeesLeaves + " WHERE " + createdOn_Server + " IS NULL OR " + createdOn_Server + " = ''";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("pending"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public int getPendingLeavesCount(int school_id, String attendanceDate) {
        int count = 0;
        String selectQuery = "SELECT COUNT(emp_leaves.id) AS pending FROM " + TABLE_EmployeesLeaves + " emp_leaves" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
                " WHERE emp_detail." + IsActive + "= 1 AND emp_leaves.SchoolID = @SchoolID AND emp_leaves." + Leave_Status_id + " = 1" +
                " AND STRFTIME('%Y-%m-%d','@AttDate') BETWEEN STRFTIME('%Y-%m-%d',emp_leaves.start_date) " +
                " AND STRFTIME('%Y-%m-%d',emp_leaves.end_date)";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");
        selectQuery = selectQuery.replace("@AttDate", attendanceDate);

//        String selectQuery = "SELECT COUNT(emp_leaves.id) AS pending FROM "+TABLE_EmployeesLeaves+" emp_leaves" +
//                " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID AND emp_leaves.createdOn_server IS NULL OR emp_leaves.createdOn_server = ''";

//        String selectQuery = "SELECT COUNT(*) AS pending FROM " + TABLE_EmployeesLeaves + " WHERE " + createdOn_Server + " IS NULL OR " + createdOn_Server + " = ''";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("pending"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public int getApprovedLeavesCount(int school_id) {
        String selectQuery = "SELECT COUNT(emp_leaves.id) AS approved FROM " + TABLE_EmployeesLeaves + " emp_leaves" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
                " WHERE emp_detail." + IsActive + "= 1 AND emp_leaves.SchoolID = @SchoolID AND emp_leaves." + Leave_Status_id + " = 2";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");

//        String selectQuery = "SELECT COUNT(emp_leaves.id) AS approved FROM "+TABLE_EmployeesLeaves+" emp_leaves" +
//                " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID AND emp_leaves.createdOn_server IS NOT NULL";

//        String selectQuery = "SELECT COUNT(*) AS approved FROM "+TABLE_EmployeesLeaves+" WHERE "+createdOn_Server+" IS NOT NULL";
        int count = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("approved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public String getLastApprovedLeave(int school_id) {
        String selectQuery = "SELECT MAX(emp_leaves." + createdOn_App + ") as last_approved FROM " + TABLE_EmployeesLeaves + " emp_leaves" +
                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
                " WHERE emp_detail." + IsActive + "= 1 AND emp_leaves.SchoolID = @SchoolID AND emp_leaves." + Leave_Status_id + " = 2";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");

//        String selectQuery = "SELECT MAX(emp_leaves.createdOn_server) as last_approved FROM "+TABLE_EmployeesLeaves+" emp_leaves" +
//                " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_leaves.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID";

//
//        String selectQuery = "SELECT MAX("+createdOn_Server+") as last_approved FROM "+TABLE_EmployeesLeaves;
        String lastApproved = "";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                lastApproved = cursor.getString(cursor.getColumnIndex("last_approved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastApproved;
    }

    public int getPendingResignationCount(int school_id) {
        int count = 0;
//        String selectQuery = "SELECT COUNT(emp_resign.id) AS pending FROM "+EMPLOYEE_RESIGNATION_TABLE+" emp_resign" +
//               " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_resign.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NULL OR emp_resign.createdOnServer = '' AND emp_resign.status = 1";

        String selectQuery = "SELECT COUNT(sep_det.id) AS pending FROM " + SEPARATION_DETAIL_TABLE + " sep_det" +
                " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign ON emp_resign.id = sep_det.Resignation_id" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_resign.emp_detail_id" +
                //" WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NULL OR emp_resign.createdOnServer = '' AND emp_resign.status = 1 AND emp_resign.resignType = 1";
                " WHERE emp_school.SchoolID = @SchoolID AND sep_det.status = 1 AND emp_resign.resignType = 1" +
                " AND sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();;

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");
//        String selectQuery = "SELECT COUNT(*) AS pending FROM " + TABLE_EmployeesLeaves + " WHERE " + createdOn_Server + " IS NULL OR " + createdOn_Server + " = ''";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("pending"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public int getApprovedResignationCount(int school_id) {
//        String selectQuery = "SELECT COUNT(emp_resign.id) AS approved FROM "+EMPLOYEE_RESIGNATION_TABLE+" emp_resign" +
//                " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_resign.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NOT NULL AND emp_resign.status = 2";

        int count = 0;
        String selectQuery = "SELECT COUNT(sep_det.id) AS approved FROM " + SEPARATION_DETAIL_TABLE + " sep_det" +
                " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign ON emp_resign.id = sep_det.Resignation_id" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_resign.emp_detail_id" +
                //" WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NULL OR emp_resign.createdOnServer = '' AND emp_resign.status = 1 AND emp_resign.resignType = 1";
                " WHERE emp_school.SchoolID = @SchoolID AND sep_det.status = 3 AND emp_resign.resignType = 1" +
                " AND sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();;

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");
//        String selectQuery = "SELECT COUNT(*) AS approved FROM "+TABLE_EmployeesLeaves+" WHERE "+createdOn_Server+" IS NOT NULL";
//        int count = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("approved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public String getLastApprovedResign(int school_id) {
//        String selectQuery = "SELECT MAX(emp_resign.createdOnServer) as last_approved FROM "+EMPLOYEE_RESIGNATION_TABLE+" emp_resign" +
//                " INNER JOIN "+EMPLOYEE_DETAIL_TABLE+" emp_detail ON emp_detail.id = emp_resign.emp_detail_id" +
//                " INNER JOIN "+TABLE_EMPLOYEE_SCHOOL+" emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID and emp_resign.status = 2";

        String selectQuery = "SELECT MAX(sep_det.createdOnServer) as last_approved FROM " + SEPARATION_DETAIL_TABLE + " sep_det" +
                " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign ON emp_resign.id = sep_det.Resignation_id" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_resign.emp_detail_id" +
                " WHERE emp_school.SchoolID = @SchoolID AND sep_det.status = 3 AND emp_resign.resignType = 1" +
                " AND sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();;

//                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_resign.emp_detail_id" +
//                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID and emp_resign.status = 2 and emp_resign.resignType = 1";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");
//
//        String selectQuery = "SELECT MAX("+createdOn_Server+") as last_approved FROM "+TABLE_EmployeesLeaves;
        String lastApproved = "";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                lastApproved = cursor.getString(cursor.getColumnIndex("last_approved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastApproved;
    }

    public int getPendingTerminationCount(int school_id) {
        int count = 0;
        String selectQuery = "SELECT COUNT(sep_det.id) AS pending FROM " + SEPARATION_DETAIL_TABLE + " sep_det" +
                " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign ON emp_resign.id = sep_det.Resignation_id" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_resign.emp_detail_id" +
                //" WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NULL OR emp_resign.createdOnServer = '' AND emp_resign.status = 1 AND emp_resign.resignType = 1";
                " WHERE emp_school.SchoolID = @SchoolID AND sep_det.status = 1 AND emp_resign.resignType = 2" +
                " AND sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();;

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");
//        String selectQuery = "SELECT COUNT(*) AS pending FROM " + TABLE_EmployeesLeaves + " WHERE " + createdOn_Server + " IS NULL OR " + createdOn_Server + " = ''";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("pending"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public int getApprovedTerminationCount(int school_id) {
        String selectQuery = "SELECT COUNT(sep_det.id) AS approved FROM " + SEPARATION_DETAIL_TABLE + " sep_det" +
                " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign ON emp_resign.id = sep_det.Resignation_id" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_resign.emp_detail_id" +
                //" WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NULL OR emp_resign.createdOnServer = '' AND emp_resign.status = 1 AND emp_resign.resignType = 1";
                " WHERE emp_school.SchoolID = @SchoolID AND sep_det.status = 3 AND emp_resign.resignType = 2" +
                " AND sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();;

//        String selectQuery = "SELECT COUNT(emp_resign.id) AS approved FROM " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign" +
//                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_resign.emp_detail_id" +
//                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID AND emp_resign.status = 2 AND emp_resign.resignType = 2";
//        " WHERE emp_school.SchoolID = @SchoolID AND emp_resign.createdOnServer IS NOT NULL AND emp_resign.status = 2 AND emp_resign.resignType = 2";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");
//        String selectQuery = "SELECT COUNT(*) AS approved FROM "+TABLE_EmployeesLeaves+" WHERE "+createdOn_Server+" IS NOT NULL";
        int count = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("approved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public String getLastApprovedTermination(int school_id) {
        String selectQuery = "SELECT MAX(sep_det.createdOnServer) as last_approved FROM " + SEPARATION_DETAIL_TABLE + " sep_det" +
                " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign ON emp_resign.id = sep_det.Resignation_id" +
                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_resign.emp_detail_id" +
                " WHERE emp_school.SchoolID = @SchoolID AND sep_det.status = 3 AND emp_resign.resignType = 2" +

                " AND sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();;
//        String selectQuery = "SELECT MAX(emp_resign.createdOnServer) as last_approved FROM " + EMPLOYEE_RESIGNATION_TABLE + " emp_resign" +
//                " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail.id = emp_resign.emp_detail_id" +
//                " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school.emp_detail_id = emp_detail.id" +
//                " WHERE emp_school.SchoolID = @SchoolID and emp_resign.status = 2 AND emp_resign.resignType = 2";

        selectQuery = selectQuery.replace("@SchoolID", school_id + "");

        String lastApproved = "";
        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                lastApproved = cursor.getString(cursor.getColumnIndex("last_approved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastApproved;
    }

    public List<EmployeeModel> getAttendanceForEmployee(int schoolId, String attendanceDate) {
        List<EmployeeModel> emList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp.*,emp_attend.attendanceType_id from " + EMPLOYEE_DETAIL_TABLE + " emp " +
                    " INNER JOIN " + TABLE_EMPLOYEE_TEACHER_Attendance + " emp_attend ON emp_attend." + Employee_Personal_Detail_ID + "= emp." + ID +
                    " WHERE emp_attend." + Employee_SchoolId + " = " + schoolId +
                    " AND (emp." + Emp_LastWorkingDay + " IS NULL OR emp." + Emp_LastWorkingDay + " = '' OR STRFTIME('%Y-%m-%d',emp." + Emp_LastWorkingDay + ") > STRFTIME('%Y-%m-%d',DATE('now')))" +
                    " AND emp." + Emp_Is_Active + " = 1" +
                    " AND emp_attend." + For_Date + " = '" + attendanceDate + "'" +
                    " ORDER BY emp." + Emp_First_Name + " ASC";


            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeModel employeeModel = new EmployeeModel();

                    boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                    employeeModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    employeeModel.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                    employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                    employeeModel.setFather_Name(cursor.getString(cursor.getColumnIndex(Emp_Father_Name)));
                    employeeModel.setMother_Name(cursor.getString(cursor.getColumnIndex(Emp_Mother_Name)));
                    employeeModel.setDate_Of_Joining(cursor.getString(cursor.getColumnIndex(Emp_Date_Of_Joining)));
                    employeeModel.setCADRE(cursor.getString(cursor.getColumnIndex(Emp_CADRE)));
                    employeeModel.setDesignation(cursor.getString(cursor.getColumnIndex(Emp_Designation)));
                    employeeModel.setEmail(cursor.getString(cursor.getColumnIndex(Emp_Email)));
                    employeeModel.setMobile_No(cursor.getString(cursor.getColumnIndex(Emp_Mobile_No)));
                    employeeModel.setNIC_No(cursor.getString(cursor.getColumnIndex(Emp_NIC_No)));
                    employeeModel.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    employeeModel.setAttendanceType_id(cursor.getInt(cursor.getColumnIndex(Attendance_Type_ID)));
                    employeeModel.setIs_Active(b);

                    emList.add(employeeModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return emList;
    }

    public long changeStatus(EmployeeSeparationDetailModel esm) {
        long id = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
//            valuesForResignation.put(Emp_Status, status);

            ContentValues valuesForSeparaton = new ContentValues();
            valuesForSeparaton.put(UPLOADED_ON, (String) null);
            valuesForSeparaton.put(MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            valuesForSeparaton.put(MODIFIED_BY, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            valuesForSeparaton.put(Emp_Status, esm.getEmp_status());
            valuesForSeparaton.put(Separation_Remarks, esm.getSeparation_Remarks());

            id = DB.update(PENDING_SEPARATION_TABLE, valuesForSeparaton, Employee_Resignation_Id + " = " + esm.getEmployeeResignationId() + " AND " + Emp_Status + " = 1 AND " + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId() , null);
            /*if (id > 0){
                id = 0;
                ContentValues valuesForResignation = new ContentValues();

                valuesForResignation.put(UPLOADED_ON, (String) null);
                valuesForResignation.put(MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                valuesForResignation.put(MODIFIED_BY, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
                if(esm.getEmp_status() == 2){
                    valuesForResignation.put(Cancelled_On, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    valuesForResignation.put(Cancelled_By, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
                    valuesForResignation.put(isActive,false);
                }


                id = DB.update(EMPLOYEE_RESIGNATION_TABLE, valuesForResignation, serverId + " = " + esm.getEmployeeResignationId(), null);

            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;

    }

    public long addEmployeeTeacherAttendance(List<EmployeeTeacherAttendanceModel> teacherAttendanceModelList) {
        long id = 0;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        DB.beginTransaction();
        try {
            for (EmployeeTeacherAttendanceModel attendanceModel : teacherAttendanceModelList) {
                ContentValues cv = new ContentValues();
                cv.put(Employee_Personal_Detail_ID, attendanceModel.getEmployee_Personal_Detail_ID());
                cv.put(Attendance_Type_ID, attendanceModel.getAttendance_Type_ID());
                cv.put(For_Date, attendanceModel.getFor_date());
                cv.put(device_Id, attendanceModel.getDevice_Id());
                cv.put(created_By, attendanceModel.getCreated_By());
                cv.put(createdOn_App, attendanceModel.getCreatedOn_App());
                cv.put(Employee_SchoolId, attendanceModel.getSchoolId());
                cv.put(MODIFIED_ON, attendanceModel.getModifiedOn());
//            cv.put(Leave_Type_ID, attendanceModel.getLeave_Type_ID());
//            cv.put(Leave_Name, attendanceModel.getReason());
                id = DB.insert(TABLE_EMPLOYEE_TEACHER_Attendance, null, cv);
            }
            DB.setTransactionSuccessful();

        } catch (Exception e) {
            id = -1;
            e.printStackTrace();
        } finally {
            DB.endTransaction();
        }
        return id;
    }

    public int findEmployeeAttendanceHeaderId(String Fordate, int empPD_ID, int schoolId) {
        String Query = "SELECT " + ID + " FROM " + TABLE_EMPLOYEE_TEACHER_Attendance +
                " WHERE " + Employee_Personal_Detail_ID + " = " + empPD_ID +
                " AND " + For_Date + " = '" + Fordate + "'" +
                " AND " + Employee_SchoolId + " = " + schoolId;
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

    public long updateAttendance(EmployeeTeacherAttendanceModel attendanceModel, int HeaderId) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(Attendance_Type_ID, attendanceModel.getAttendance_Type_ID());
            values.put(created_By, attendanceModel.getCreated_By());
            values.put(createdOn_App, attendanceModel.getCreatedOn_App());
            values.put(UPLOADED_ON, attendanceModel.getUploaded_on());
            values.put(MODIFIED_ON, attendanceModel.getModifiedOn());
            long i = DB.update(TABLE_EMPLOYEE_TEACHER_Attendance, values, ID + " = " + HeaderId, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<EmployeeLeaveModel> getEmployeesForLeaveApproval(int schoolId, int status, String firstName, String lastName) {

        List<EmployeeLeaveModel> empList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT emp_detail.*,emp_leave.id as leave_id,emp_leave.start_date as leave_startDate,emp_leave.end_date as leave_endDate," +
                    " emp_leave.leave_type_id as leave_type, emp_leave.leave_working_days as leave_wd, emp_leave.status_id as leave_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + TABLE_EmployeesLeaves + " emp_leave ON emp_leave." + Employee_Personal_Detail_ID + " = emp_detail." + ID;

            selectQuery += " WHERE emp_detail." + IsActive + "= 1 AND emp_leave." + Employee_SchoolId + " = " + schoolId;

            if (status == 1 || status == 2 || status != 3) {
                selectQuery += " AND emp_leave." + Leave_Status_id + " = " + status + "";
            }

            if (firstName != null && lastName != null && !firstName.equals("") && !lastName.equals("")) {
                selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "' AND emp_detail." + Emp_Last_Name + " = '" + lastName + "' ";
            }

            selectQuery += " ORDER BY emp_detail." + ID;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeModel employeeModel = new EmployeeModel();

                    boolean b = cursor.getString(cursor.getColumnIndex(Emp_Is_Active)).equals("1");
                    employeeModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    employeeModel.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                    employeeModel.setEmployee_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    employeeModel.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    employeeModel.setLast_Name(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                    employeeModel.setCADRE(cursor.getString(cursor.getColumnIndex(Emp_CADRE)));
                    employeeModel.setDesignation(cursor.getString(cursor.getColumnIndex(Emp_Designation)));

                    EmployeeLeaveModel leaveModel = new EmployeeLeaveModel();
                    leaveModel.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex("leave_id")));
                    leaveModel.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex("leave_startDate")));
                    leaveModel.setLeave_End_Date(cursor.getString(cursor.getColumnIndex("leave_endDate")));
                    leaveModel.setLeave_Type_ID(cursor.getInt(cursor.getColumnIndex("leave_type")));
                    leaveModel.setLeavesLWD(cursor.getString(cursor.getColumnIndex("leave_wd")));
                    leaveModel.setLeave_status_id(cursor.getInt(cursor.getColumnIndex("leave_status")));
                    leaveModel.setEmployeeModel(employeeModel);

                    empList.add(leaveModel);
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

    public EmployeeLeaveModel getEmployeeForLeaveApproval(int empDetailId, int empLeaveId,int schoolId) {
        EmployeeLeaveModel model = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves +
                " WHERE " + Employee_Personal_Detail_ID + " = " + empDetailId +
                " AND " + ID + " = " + empLeaveId +
                " AND " + Employee_SchoolId + " = " + schoolId;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                model = new EmployeeLeaveModel();
                model.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex(ID)));
                model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                model.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex(Leave_Start_Date)));
                model.setLeave_End_Date(cursor.getString(cursor.getColumnIndex(Leave_End_Date)));
                model.setLeave_Type_ID(cursor.getInt(cursor.getColumnIndex(Leave_Type_ID)));
                model.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
//                model.setLeave_status(cursor.getString(cursor.getColumnIndex(StatusName)));
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

    public long setLeaveOfEmployeeApproved(EmployeeLeaveModel model,List<String> daysBetweenStartAndEndDate, int status) {
        long id = 0;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            ContentValues values = new ContentValues();
            values.put(UPLOADED_ON, (String) null);

            if (status == 2) {
                values.put(Leave_Status_id, status); //Approved
                id = DB.update(TABLE_EmployeesLeaves, values, Employee_Personal_Detail_ID + "=" + model.getEmployee_Personal_Detail_ID()
                        + " AND " + ID + " =" + model.getEmployee_Leave_ID()
                        + " AND " + Employee_SchoolId + " =" + model.getSchoolId()
                        + " AND " + Leave_Status_id + " = 1", null);

                if (id > 0) {
                    long attId = insertLeaveIntoAttendanceTable(model, model.getSchoolId(), daysBetweenStartAndEndDate);
                    if (attId > 0)
                        Toast.makeText(context, "Leave is successfully added to Employee Attendance ", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "Something wrong. Leave is not added to Employee Attendance !", Toast.LENGTH_LONG).show();
                }
            } else if (status == 3) {
                values.put(Leave_Status_id, status); //Approved
                id = DB.update(TABLE_EmployeesLeaves, values, Employee_Personal_Detail_ID + "=" + model.getEmployee_Personal_Detail_ID()
                        + " AND " + ID + " =" + model.getEmployee_Leave_ID()
                        + " AND " + Employee_SchoolId + " =" + model.getSchoolId()
                        + " AND " + Leave_Status_id + " = 1", null);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;

    }

    private long insertLeaveIntoAttendanceTable(EmployeeLeaveModel model, int schoolid, List<String> daysBetweenStartAndEndDate) {
        long id = -1;
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        List<EmployeeModel> employeeModelList = getSearchedEmployees(schoolid);
        try {
            ContentValues cv = new ContentValues();
            cv.put(Employee_Personal_Detail_ID, model.getEmployee_Personal_Detail_ID());
            cv.put(Attendance_Type_ID, 4);  //Leave
            cv.put(Leave_Type_ID, model.getLeave_Type_ID());
            cv.put(device_Id, AppModel.getInstance().getDeviceId(context));
            cv.put(created_By, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
            cv.put(createdOn_App, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
//            cv.put(Employee_SchoolId, schoolid);
            for (String fordate : daysBetweenStartAndEndDate) {
                cv.put(For_Date, fordate);

                //check if attendance is present
                if (FindAttendanceRecord(model.getEmployee_Personal_Detail_ID(), fordate , schoolid)) {
                    cv.put(UPLOADED_ON, (String) null);
                    id = DB.update(TABLE_EMPLOYEE_TEACHER_Attendance, cv,
                            Employee_Personal_Detail_ID + " =" + model.getEmployee_Personal_Detail_ID() +
                                    " AND " + For_Date + " = '" + fordate + "'" +
                                    " AND " + Employee_SchoolId + " = " + schoolid, null);
                } else {
                    cv.put(Employee_SchoolId, schoolid);
                    id = DB.insert(TABLE_EMPLOYEE_TEACHER_Attendance, null, cv);
                }

                //Add by default present to remaining employees
//                for (EmployeeModel em: employeeModelList){
//                    if (em.getId() != model.getEmployee_Personal_Detail_ID()){
//                        if (!checkIfAttendanceRecordExist(em.getId(),fordate)){
//
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(Employee_Personal_Detail_ID, em.getId());
//                            contentValues.put(Attendance_Type_ID, 1);  //Present
//                            contentValues.put(Leave_Type_ID, 0);
//                            contentValues.put(device_Id, SurveyAppModel.getInstance().getDeviceId(context));
//                            contentValues.put(created_By, DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
//                            contentValues.put(createdOn_App, SurveyAppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
//                            contentValues.put(For_Date, fordate);
//
//                            id = DB.insert(TABLE_EMPLOYEE_TEACHER_Attendance, null, contentValues);
//                        }
//                    }
//                }
            }

            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean checkIfAttendanceRecordExist(int empDetailId, String for_Date) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp_detail.id as empDetail_id FROM EmployeeDetails emp_detail" +
                    " LEFT JOIN EmployeeTeacherAttendance emp_att ON  emp_att.emp_detail_id = emp_detail.id" +
                    " WHERE empDetail_id =" + empDetailId + " AND emp_att.for_date ='" + for_Date + "'" +
                    " AND emp_att.attendanceType_id IN (4,3,2,1)";

//            String selectQuery = "SELECT emp_att.* FROM " + TABLE_EMPLOYEE_TEACHER_Attendance + " emp_att "
//                    + " INNER JOIN " + EMPLOYEE_DETAIL_TABLE + " emp_detail ON emp_detail." + ID + " = emp_att." + Employee_Personal_Detail_ID
//                    + " WHERE emp_att." + For_Date + " = '" + for_Date + "'"
//                    + " AND emp_detail." + ID + " =" + empDetailId
//                    + " AND emp_att."+Attendance_Type_ID+" NOT IN (4,3,2,1)";

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

    public boolean FindAttendanceRecord(int empDetailId, String for_Date, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_TEACHER_Attendance + " WHERE " + For_Date + " = '" + for_Date + "'"
                    + " AND " + Employee_Personal_Detail_ID + " =" + empDetailId
                    + " AND " + Employee_SchoolId + " =" + schoolId;

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

    public boolean FindEmployeeRecord(int id, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + EMPLOYEE_DETAIL_TABLE + " WHERE " + ID + " = " + id;

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


    public boolean FindApprovalRecord(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + SEPARATION_DETAIL_TABLE + " WHERE " + ID + " = " + id;

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


    public boolean FindPendingSeparationRecord(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + PENDING_SEPARATION_TABLE + " WHERE " + ID + " = " + id;

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

    public long addEmployee(EmployeeModel employee) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            values.put(ID, employee.getId());
            values.put(Employee_ID, employee.getEmployee_ID());
            values.put(Employee_Code, employee.getEmployee_Code());
            values.put(Emp_Designation, employee.getDesignation());
            values.put(Emp_First_Name, employee.getFirst_Name());

            values.put(Emp_Last_Name, employee.getLast_Name());
            values.put(Emp_Email, employee.getEmail());
            values.put(Emp_Mobile_No, employee.getMobile_No());
            values.put(Emp_Father_Name, employee.getFather_Name());
            values.put(Emp_Mother_Name, employee.getMother_Name());
            values.put(Emp_NIC_No, employee.getNIC_No());
            values.put(Emp_CADRE, employee.getCADRE());
            values.put(Emp_Is_Active, employee.getIs_Active());
            values.put(Emp_Date_Of_Joining, employee.getDate_Of_Joining());
            values.put(Emp_LastWorkingDay, employee.getLastWorkingDay());
            values.put(MODIFIED_BY, employee.getModifiedBy());

            values.put(Emp_Job_Status, employee.getJob_Status());
            values.put(Emp_Gender, employee.getGender());

            if (employee.getModifiedOn() != null) {
//                String dateFormat = null;
//                if (employee.getModifiedOn().length() == 8)
//                    dateFormat = "dd/MM/yy";
//                else
//                    dateFormat = "dd/MM/yy HH:mm:ss";
                employee.setModifiedOn(AppModel.getInstance().convertDatetoFormat(employee.getModifiedOn(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd hh:mm:ss"));
                values.put(MODIFIED_ON, employee.getModifiedOn());            }

            values.put(UPLOADED_ON, employee.getUploadedOn());

            long i = DB.insert(EMPLOYEE_DETAIL_TABLE, null, values);
            if (i == -1){
                AppModel.getInstance().appendLog(context, "Couldn't insert employee with Employee Detail ID: " + employee.getId());
            }
            else{

                if(employee.getImagePath() != null && !employee.getImagePath().trim().equals("")){
                    AppModel.getInstance().appendLog(context, "Employee inserted with Employee Detail ID: " + employee.getId());
                    String name = employee.getImagePath();
                    name = name.replace("\\","/");
                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/";
                    employee.setImagePath(fdir + name);

                    UserImageModel userImageModel = new UserImageModel();
                    userImageModel.setUser_id(employee.getId());
                    userImageModel.setUser_image_path(employee.getImagePath());
                    userImageModel.setUploaded_on(employee.getUploadedOn());
                    if (employee.getModifiedOn() != null) {
                        String format = AppModel.getInstance().determineDateFormat(employee.getModifiedOn());
                        if (format != null) {
                            userImageModel.setModifiedOn(AppModel.getInstance().convertDatetoFormat(employee.getModifiedOn(), format, "yyyy-MM-dd hh:mm:ss"));
                        }
                    }else{
                        userImageModel.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
                    }
                    i = insertOrUpdateUserImage(userImageModel,context);

                    if (i == -1){
                        AppModel.getInstance().appendLog(context, "Couldn't insert userImage with Employee Detail ID: " + employee.getId());
                    }
                    else{
                        AppModel.getInstance().appendLog(context, "userImage inserted with Employee Detail ID: " + employee.getId());
                    }
                }

            }

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployees Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }


    public long addSeparationAprrovalRecord(EmployeePendingApprovalModel esdm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            values.put(ID, esdm.getId());
            values.put(Employee_Resignation_Id, esdm.getEmployeeResignationId());
            values.put(Approver_userId, esdm.getApprover_userId());
            values.put(Emp_Status, esdm.getEmp_status());
            values.put(Separation_Remarks, esdm.getSeparation_Remarks());
            values.put(HCM_UpdateStatus, esdm.getHCM_UpdateStatus());
            values.put(app_rank, esdm.getApp_rank());
            values.put(CREATED_BY, esdm.getCREATED_BY());
            values.put(CREATED_ON_APP, esdm.getCREATED_ON_APP());
            values.put(UPLOADED_ON, esdm.getUPLOADED_ON());
            values.put(MODIFIED_ON, esdm.getMODIFIED_ON());
            values.put(MODIFIED_BY, esdm.getMODIFIED_BY());

            long i = DB.insert(SEPARATION_DETAIL_TABLE, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert AllSeparation record with ID: " + esdm.getId());
            else
                AppModel.getInstance().appendLog(context, "AllSeparation record inserted with ID: " + esdm.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSeparationAprrovalRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }


    public long addPendingSeparationRecord(EmployeePendingApprovalModel esdm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            values.put(ID, esdm.getId());
            values.put(Employee_Resignation_Id, esdm.getEmployeeResignationId());
            values.put(Approver_userId, esdm.getApprover_userId());
            values.put(Emp_Status, esdm.getEmp_status());
            values.put(Separation_Remarks, esdm.getSeparation_Remarks());
            values.put(CREATED_BY, esdm.getCREATED_BY());
            values.put(CREATED_ON_APP, esdm.getCREATED_ON_APP());
            values.put(UPLOADED_ON, esdm.getUPLOADED_ON());
            values.put(MODIFIED_ON, esdm.getMODIFIED_ON());
            values.put(MODIFIED_BY, esdm.getMODIFIED_BY());

            long i = DB.insert(PENDING_SEPARATION_TABLE, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert Approver record with ID: " + esdm.getId());
            else
                AppModel.getInstance().appendLog(context, "Approver record inserted with ID: " + esdm.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addSeparationAprrovalRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }


    public boolean IfApprovalRecordNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + SEPARATION_DETAIL_TABLE + " WHERE " + ID + " = " + id
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

    public boolean IfPendingSeparationNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + PENDING_SEPARATION_TABLE + " WHERE " + ID + " = " + id
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

    public boolean IfEmployeeNotUploaded(int id, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + EMPLOYEE_DETAIL_TABLE
                    + " WHERE " + ID + " = " + id
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


    public long updateApprovalRecord(EmployeePendingApprovalModel esdm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, esdm.getId());
            values.put(Employee_Resignation_Id, esdm.getEmployeeResignationId());
            values.put(Approver_userId, esdm.getApprover_userId());
            values.put(Emp_Status, esdm.getEmp_status());
            values.put(Separation_Remarks, esdm.getSeparation_Remarks());
            values.put(CREATED_BY, esdm.getCREATED_BY());
            values.put(CREATED_ON_APP, esdm.getCREATED_ON_APP());
            values.put(UPLOADED_ON, esdm.getUPLOADED_ON());
            values.put(MODIFIED_ON, esdm.getMODIFIED_ON());
            values.put(MODIFIED_BY, esdm.getMODIFIED_BY());

            long i = DB.update(SEPARATION_DETAIL_TABLE, values, ID + " =" + esdm.getId(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update AllSeparation Record ID: " + esdm.getId());
            } else {
                AppModel.getInstance().appendLog(context, "AllSeparation Record updated ID: " + esdm.getId());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateApprovalRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }


    public long updatePendingSeparationRecord(EmployeePendingApprovalModel esdm) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, esdm.getId());
            values.put(Employee_Resignation_Id, esdm.getEmployeeResignationId());
            values.put(Approver_userId, esdm.getApprover_userId());
            values.put(Emp_Status, esdm.getEmp_status());
            values.put(Separation_Remarks, esdm.getSeparation_Remarks());
            values.put(CREATED_BY, esdm.getCREATED_BY());
            values.put(CREATED_ON_APP, esdm.getCREATED_ON_APP());
            values.put(UPLOADED_ON, esdm.getUPLOADED_ON());
            values.put(MODIFIED_ON, esdm.getMODIFIED_ON());
            values.put(MODIFIED_BY, esdm.getMODIFIED_BY());

            long i = DB.update(PENDING_SEPARATION_TABLE, values, ID + " =" + esdm.getId(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Approval Record ID: " + esdm.getId());
            } else {
                AppModel.getInstance().appendLog(context, "Approval Record updated ID: " + esdm.getId());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateApprovalRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public long updateEmployee(EmployeeModel employee) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ID, employee.getId());
            values.put(Employee_ID, employee.getEmployee_ID());
            values.put(Employee_Code, employee.getEmployee_Code());
            values.put(Emp_Designation, employee.getDesignation());
            values.put(Emp_First_Name, employee.getFirst_Name());
            values.put(Emp_Last_Name, employee.getLast_Name());
            values.put(Emp_Email, employee.getEmail());
            values.put(Emp_Mobile_No, employee.getMobile_No());
            values.put(Emp_Father_Name, employee.getFather_Name());
            values.put(Emp_Mother_Name, employee.getMother_Name());
            values.put(Emp_NIC_No, employee.getNIC_No());
            values.put(Emp_CADRE, employee.getCADRE());
            values.put(Emp_Is_Active, employee.getIs_Active());
            values.put(Emp_Date_Of_Joining, employee.getDate_Of_Joining());
            values.put(Emp_LastWorkingDay, employee.getLastWorkingDay());
            values.put(MODIFIED_BY, employee.getModifiedBy());

            values.put(Emp_Job_Status, employee.getJob_Status());
            values.put(Emp_Gender, employee.getGender());

            if (employee.getModifiedOn() != null) {
//                String dateFormat = null;
//                if (employee.getModifiedOn().length() == 8)
//                    dateFormat = "dd/MM/yy";
//                else
//                    dateFormat = "dd/MM/yy HH:mm:ss";
                employee.setModifiedOn(AppModel.getInstance().convertDatetoFormat(employee.getModifiedOn(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd hh:mm:ss"));
                values.put(MODIFIED_ON, employee.getModifiedOn());
//                values.put(UPLOADED_ON, employee.getUploadedOn());
            }

            values.put(UPLOADED_ON, employee.getUploadedOn());

            long i = DB.update(EMPLOYEE_DETAIL_TABLE, values, ID + " =" + employee.getId(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Employee Detail ID: " + employee.getId());
            } else {
                AppModel.getInstance().appendLog(context, "Student updated Employee Detail ID: " + employee.getId());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateEmployee Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public void removeSeparationDetailNotExist(final List<EmployeePendingApprovalModel> serverSepDetList) {
        new Thread(() -> {
            boolean recordFound;
            ArrayList<EmployeePendingApprovalModel> existingSepDetList = getAllSepDetRecords();
            ArrayList<EmployeePendingApprovalModel> todo = new ArrayList<>();
            if (existingSepDetList != null && existingSepDetList.size() > 0) {
                for (EmployeePendingApprovalModel exisitngSepDetRecord : existingSepDetList) {
                    recordFound = false;
                    for (EmployeePendingApprovalModel serverRecord : serverSepDetList) {
                        if (exisitngSepDetRecord.getId() == serverRecord.getId()) {
                            recordFound = true;
                            break;
                        }
                    }
                    if (!recordFound)
                        todo.add(exisitngSepDetRecord);
                }
            }
            for (EmployeePendingApprovalModel model : todo) {
                if (removeSepDetRecord(model)) {
                    AppModel.getInstance().appendLog(context, "AllSeparation Detail record Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove AllSeparation record: id = " + model.getId());
                }
            }
        }).start();
    }



    public void removePendingSeparationNotExist(final List<EmployeePendingApprovalModel> serverSepDetList) {
        new Thread(() -> {
            boolean recordFound;
            ArrayList<EmployeePendingApprovalModel> existingSepDetList = getAllPendingSepRecords();
            ArrayList<EmployeePendingApprovalModel> todo = new ArrayList<>();
            if (existingSepDetList != null && existingSepDetList.size() > 0) {
                for (EmployeePendingApprovalModel exisitngSepDetRecord : existingSepDetList) {
                    recordFound = false;
                    for (EmployeePendingApprovalModel serverRecord : serverSepDetList) {
                        if (exisitngSepDetRecord.getId() == serverRecord.getId()) {
                            recordFound = true;
                            break;
                        }
                    }
                    if (!recordFound)
                        todo.add(exisitngSepDetRecord);
                }
            }
            for (EmployeePendingApprovalModel model : todo) {
                if (removePendingSepRecord(model)) {
                    AppModel.getInstance().appendLog(context, "Pending Separation record Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Pending Separation record: id = " + model.getId());
                }
            }
        }).start();
    }

    public void removeEmployeesNotExists(final int schoolId, final ArrayList<EmployeeModel> serverEmployeeList) {
        new Thread(() -> {
            boolean employeeFound;
            ArrayList<EmployeeModel> existingEmployeesList = getAllEmployeesList(schoolId);
            ArrayList<EmployeeModel> todo = new ArrayList<>();
            if (existingEmployeesList != null && existingEmployeesList.size() > 0) {
                for (EmployeeModel existingEmployee : existingEmployeesList) {
                    employeeFound = false;
                    for (EmployeeModel serverStudent : serverEmployeeList) {
                        if (existingEmployee.getId() == serverStudent.getId()) {
                            employeeFound = true;
                        }
                    }
                    if (!employeeFound)
                        todo.add(existingEmployee);
                }
            }
            for (EmployeeModel model : todo) {
//                if (removeEmployee(model)) {
                if (markEmployeeInActive(model)) {
                    AppModel.getInstance().appendLog(context, "Employee Removed: id = " + model.getId() + " Emp ID = " + model.getEmployee_ID());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Employee: id = " + model.getId() + " Emp ID = " + model.getEmployee_ID());
                }
            }
        }).start();
    }


    private ArrayList<EmployeePendingApprovalModel> getAllSepDetRecords() {
        ArrayList<EmployeePendingApprovalModel> emList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + SEPARATION_DETAIL_TABLE + " WHERE " + Cancelled_On + " IS NULL OR " + Cancelled_On + " = '' ";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeePendingApprovalModel em = new EmployeePendingApprovalModel();
                    em.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    emList.add(em);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return emList;
    }


    private ArrayList<EmployeePendingApprovalModel> getAllPendingSepRecords() {
        ArrayList<EmployeePendingApprovalModel> emList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + PENDING_SEPARATION_TABLE + " WHERE " + Cancelled_On + " IS NULL OR " + Cancelled_On + " = '' ";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeePendingApprovalModel em = new EmployeePendingApprovalModel();
                    em.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    emList.add(em);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return emList;
    }

    private ArrayList<EmployeeModel> getAllEmployeesList(int schoolId) {
        ArrayList<EmployeeModel> emList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT e.* FROM " + EMPLOYEE_DETAIL_TABLE
                    + " e INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " sc ON sc." + Employee_Personal_Detail_ID + " = e." + ID
                    + " WHERE sc." + Employee_SchoolId + " = " + schoolId
                    + " AND e." + Emp_Is_Active + " = '1'";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeModel em = new EmployeeModel();
                    em.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    em.setFirst_Name(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    em.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                    emList.add(em);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return emList;
    }

    private boolean removeEmployee(EmployeeModel employee) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(EMPLOYEE_DETAIL_TABLE, ID + "=" + employee.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean markEmployeeInActive(EmployeeModel employee) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            ContentValues cv = new ContentValues();
            cv.put(Emp_Is_Active, 0);
            return db.update(EMPLOYEE_DETAIL_TABLE, cv,ID + "=" + employee.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean removeSepDetRecord(EmployeePendingApprovalModel sepDet) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(SEPARATION_DETAIL_TABLE, ID + "=" + sepDet.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    private boolean removePendingSepRecord(EmployeePendingApprovalModel sepDet) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(PENDING_SEPARATION_TABLE, ID + "=" + sepDet.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addEmployeeLeaveType(ArrayList<EmployeeLeaveTypeModel> leaveTypeModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeLeaveTypeModel model : leaveTypeModels) {
                model.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd'T'hh:mm:ss"));
                if (!FindEmployeeLeaveType(model.getId(), model.getLeave_Name())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getId());
                    values.put(Leave_Name, model.getLeave_Name());
                    values.put(MODIFIED_ON, model.getModifiedOn());

                    long i = DB.insert(TABLE_Leaves_Type, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert leave type");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Leave type inserted");

                        downloadedCount++;
                    }
                }else {
                    downloadedCount++;
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            }

            //remove if not in server
//            removerLeaveTypeIfNotExists(leaveTypeModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeeLeaveType Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private void removerLeaveTypeIfNotExists(final ArrayList<EmployeeLeaveTypeModel> serverLeaveTypes) {
        new Thread(() -> {
            boolean leaveTypeFound;
            List<EmployeeLeaveTypeModel> existingList = getLeaveReason();
            List<EmployeeLeaveTypeModel> todo = new ArrayList<>();
            if (existingList != null && existingList.size() > 0) {
                for (EmployeeLeaveTypeModel exlist : existingList) {
                    leaveTypeFound = false;
                    for (EmployeeLeaveTypeModel serverList : serverLeaveTypes) {
                        if (exlist.getId() == serverList.getId()) {
                            leaveTypeFound = true;
                        }
                    }
                    if (!leaveTypeFound)
                        todo.add(exlist);
                }
            }
            for (EmployeeLeaveTypeModel model : todo) {
                if (removeLeaveType(model)) {
                    AppModel.getInstance().appendLog(context, "Employee Leave Type Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Employee Leave type: id = " + model.getId());
                }
            }
        }).start();
    }

    private boolean removeLeaveType(EmployeeLeaveTypeModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_Leaves_Type, ID + "=" + model.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean FindEmployeeLeaveType(int id, String leave_type) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_Leaves_Type
                    + " WHERE " + ID + " = " + id
                    + " AND " + Leave_Name + " ='" + leave_type + "'";

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


    public void addEmployeeResignReason(ArrayList<EmployeeResignReasonModel> resignReasonModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeResignReasonModel model : resignReasonModels) {
                model.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd'T'hh:mm:ss"));

                ContentValues values = new ContentValues();
                values.put(ResignReason, model.getResignReason());
                values.put(SubReason, model.getSubReason());
                values.put(Employee_ResignType_ID, model.getResignTypeID());
                values.put(MODIFIED_ON, model.getModifiedOn());

                if (!FindEmployeeResignReason(model.getID())) {
                    values.put(ID, model.getID());

                    long i = DB.insert(TABLE_EmployeesResignationReason, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert resign reason");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Resign reason inserted");

                        downloadedCount++;
                    }
                }else {
                    long i = DatabaseHelper.getInstance(context).updateTableColumns(TABLE_EmployeesResignationReason, values, model.getID());
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't update Resign reason");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Resign reason updated");

                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            //remove if not in server
//            removerResignationReasonIfNotExists(resignReasonModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeeResignReason Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }
/*
    private void removerResignationReasonIfNotExists(final ArrayList<EmployeeResignReasonModel> serverResignReason) {
        new Thread(() -> {
            boolean resignReasonFound;
            List<EmployeeResignReasonModel> existingList = getResignReason();
            List<EmployeeResignReasonModel> todo = new ArrayList<>();
            if (existingList != null && existingList.size() > 0) {
                for (EmployeeResignReasonModel exlist : existingList) {
                    resignReasonFound = false;
                    for (EmployeeResignReasonModel serverList : serverResignReason) {
                        if (exlist.getID() == serverList.getID()) {
                            resignReasonFound = true;
                        }
                    }
                    if (!resignReasonFound)
                        todo.add(exlist);
                }
            }
            for (EmployeeResignReasonModel model : todo) {
                if (removeResignationReason(model)) {
                    AppModel.getInstance().appendLog(context, "Employee Resign reason Removed: id = " + model.getID());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Employee Resign reason: id = " + model.getID());
                }
            }
        }).start();
    }*/

    /*private boolean removeResignationReason(EmployeeResignReasonModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EmployeesResignationReason, ID + "=" + model.getID(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/


    private boolean FindEmployeeResignReason(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason
                    + " WHERE " + ID + " = " + id
//                    + " AND " + ResignReason + " ='" + resignReason + "'"
                    ;

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


    public void addEmployeeResignType(ArrayList<EmployeeResignTypeModel> resignTypeModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeResignTypeModel model : resignTypeModels) {
                model.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd'T'hh:mm:ss"));
                if (!FindEmployeeResignType(model.getId(), model.getResignType())) {
                    ContentValues values = new ContentValues();
                    values.put(ID, model.getId());
                    values.put(ResignType, model.getResignType());
                    values.put(MODIFIED_ON, model.getModifiedOn());

                    long i = DB.insert(TABLE_EmployeesResignationType, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert resign type");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Resign type inserted");

                        downloadedCount++;
                    }
                }else {
                    downloadedCount++;
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }
            //remove if not in server
//            removerResignTypeIfNotExists(resignTypeModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeeResignType Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private void removerResignTypeIfNotExists(final ArrayList<EmployeeResignTypeModel> serverResignTypes) {
        new Thread(() -> {
            boolean resignTypeFound;
            List<EmployeeResignTypeModel> existingList = getResignType();
            List<EmployeeResignTypeModel> todo = new ArrayList<>();
            if (existingList != null && existingList.size() > 0) {
                for (EmployeeResignTypeModel exlist : existingList) {
                    resignTypeFound = false;
                    for (EmployeeResignTypeModel serverList : serverResignTypes) {
                        if (exlist.getId() == serverList.getId()) {
                            resignTypeFound = true;
                        }
                    }
                    if (!resignTypeFound)
                        todo.add(exlist);
                }
            }
            for (EmployeeResignTypeModel model : todo) {
                if (removeResignType(model)) {
                    AppModel.getInstance().appendLog(context, "Employee Resign Type Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Employee Resign Type: id = " + model.getId());
                }
            }
        }).start();
    }

    private boolean removeResignType(EmployeeResignTypeModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EmployeesResignationType, ID + "=" + model.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean FindEmployeeResignType(int id, String resignType) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationType
                    + " WHERE " + ID + " = " + id
                    + " AND " + ResignType + " ='" + resignType + "'";

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


    public void addEmployeeDesignations(ArrayList<EmployeeDesignationModel> designationModels, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeDesignationModel model : designationModels) {
                model.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd'T'hh:mm:ss"));

                ContentValues values = new ContentValues();
                values.put(Designation_Name, model.getDesignation_Name());
                values.put(MODIFIED_ON, model.getModifiedOn());

                if (!FindEmployeeDesignation(model.getId(), model.getDesignation_Name())) {
                    values.put(ID, model.getId());

                    long i = DB.insert(TABLE_EmployeesDesignation, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert designation");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Designation inserted");

                        downloadedCount++;
                    }
                }else {
                    long i = DatabaseHelper.getInstance(context).updateTableColumns(TABLE_EmployeesDesignation, values, model.getId());
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't update designation");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Designation updated");

                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            //remove if not in server
//            removerDesignationIfNotExists(designationModels);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeesDesignation Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }


    private void removerDesignationIfNotExists(final ArrayList<EmployeeDesignationModel> serverEmployeeDesignation) {
        new Thread(() -> {
            boolean employeeDesigFound;
            List<EmployeeDesignationModel> existingEmployeesDesgList = getDesignations();
            List<EmployeeDesignationModel> todo = new ArrayList<>();
            if (existingEmployeesDesgList != null && existingEmployeesDesgList.size() > 0) {
                for (EmployeeDesignationModel existingEmployeeDesig : existingEmployeesDesgList) {
                    employeeDesigFound = false;
                    for (EmployeeDesignationModel serverEmpDesg : serverEmployeeDesignation) {
                        if (existingEmployeeDesig.getId() == serverEmpDesg.getId()) {
                            employeeDesigFound = true;
                        }
                    }
                    if (!employeeDesigFound)
                        todo.add(existingEmployeeDesig);
                }
            }
            for (EmployeeDesignationModel model : todo) {
                if (removeEmployeeDesig(model)) {
                    AppModel.getInstance().appendLog(context, "Employee Designation Removed: id = " + model.getId());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Employee Designation: id = " + model.getId());
                }
            }
        }).start();
    }

    private boolean removeEmployeeDesig(EmployeeDesignationModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EmployeesDesignation, ID + "=" + model.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean FindEmployeeDesignation(int id, String designation_Name) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeesDesignation
                    + " WHERE " + ID + " = " + id
//                    + " AND " + Designation_Name + " ='" + designation_Name
//                    + "'"
                    ;

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

    public void addEmployeeSchool(ArrayList<EmployeeSchoolModel> empSchoolsList, int schoolid, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeSchoolModel model : empSchoolsList) {
                if (!FindEmployeeSchool(model.getEmployee_Personal_Detail_ID(), schoolid)) {
                    ContentValues values = new ContentValues();
                    values.put(Employee_Personal_Detail_ID, model.getEmployee_Personal_Detail_ID());
                    values.put(Employee_SchoolId, model.getSchoolId());
                    values.put(Employee_Code, model.getEmployee_Code());
//                    values.put(MODIFIED_ON, model.getModifiedOn());

                    long i = DB.insert(TABLE_EMPLOYEE_SCHOOL, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert emp schools");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Emp school inserted");

                        downloadedCount++;
                    }
                }else {
                    int empSchoolDeleted = deleteEmployeeSchools(model);
                    if(empSchoolDeleted > 0){
                        ContentValues values = new ContentValues();
                        values.put(Employee_Personal_Detail_ID, model.getEmployee_Personal_Detail_ID());
                        values.put(Employee_SchoolId, model.getSchoolId());
                        values.put(Employee_Code, model.getEmployee_Code());
                        values.put(MODIFIED_ON, model.getModifiedOn());

                        long i = DB.insert(TABLE_EMPLOYEE_SCHOOL, null, values);
                        if (i == -1)
                            AppModel.getInstance().appendLog(context, "Couldn't insert emp schools");
                        else if (i > 0) {
                            AppModel.getInstance().appendLog(context, "Emp school inserted");

                            downloadedCount++;
                        }
                    }
                }


                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeesSchools Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private int deleteEmployeeSchools(EmployeeSchoolModel model) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        int id = 0;
        try {
            id = db.delete(TABLE_EMPLOYEE_SCHOOL,Employee_Personal_Detail_ID +"="+model.getEmployee_Personal_Detail_ID(),null);
        }catch (Exception e){
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context,"Error in deleteEmployeeSchools : "+e.getMessage());
        }
        return id;
    }

    private boolean FindEmployeeSchool(int emp_detail_id, int schoolid) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_SCHOOL
                    + " WHERE " + Employee_Personal_Detail_ID + " = " + emp_detail_id
                    + " AND " + Employee_SchoolId + " =" + schoolid;

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

    public void addEmployeesLeaves(ArrayList<EmployeeLeaveModel> empLeavesList, int schoolid, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeLeaveModel model : empLeavesList) {
                model.setSchoolId(schoolid);
                if (!FindEmpTeacherLeaves(model.getEmployee_Leave_ID(), schoolid)) {

                    model.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                    ContentValues values = new ContentValues();
                    values.put(Employee_Personal_Detail_ID, model.getEmployee_Personal_Detail_ID());
                    values.put(Leave_Type_ID, model.getLeave_Type_ID());
                    values.put(Leave_Start_Date, AppModel.getInstance().convertDatetoFormat(model.getLeave_Start_Date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
                    values.put(Leave_End_Date, AppModel.getInstance().convertDatetoFormat(model.getLeave_End_Date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
                    values.put(Leave_Status_id, model.getLeave_status_id());
                    values.put(created_By, model.getCreated_By());
                    values.put(createdOn_Server, model.getCreatedOn_Server());
                    values.put(createdOn_App, model.getCreatedOn_App());
                    values.put(UPLOADED_ON, model.getUploadedOn());
                    values.put(Server_ID, model.getEmployee_Leave_ID());

                    values.put(device_Id, AppModel.getInstance().getDeviceId(context));
                    values.put(Employee_SchoolId, model.getSchoolId());
                    values.put(MODIFIED_ON, model.getModifiedOn());


                    long i = DB.insert(TABLE_EmployeesLeaves, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert emp leaves");
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Emp leave inserted");

                        downloadedCount++;
                    }
                } else {
                    if (!EmployeeHelperClass.getInstance(context).IfEmployeesLeaveNotUploaded(model.getEmployee_Leave_ID(), schoolid)) {
                        model.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                        long i = EmployeeHelperClass.getInstance(context).updateEmployeesLeave(model);

                        if (i > 0){
                            downloadedCount++;
                        }
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            //remove if not on server
//            removeEmployeesLeavesNotExists(schoolid, empLeavesList);

            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddEmployeesSchools Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }
    }

    private long updateEmployeesLeave(EmployeeLeaveModel model) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(Employee_Personal_Detail_ID, model.getEmployee_Personal_Detail_ID());
            values.put(Leave_Type_ID, model.getLeave_Type_ID());
            values.put(Leave_Start_Date, AppModel.getInstance().convertDatetoFormat(model.getLeave_Start_Date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
            values.put(Leave_End_Date, AppModel.getInstance().convertDatetoFormat(model.getLeave_End_Date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
            values.put(Leave_Status_id, model.getLeave_status_id());
            values.put(created_By, model.getCreated_By());
            values.put(createdOn_Server, model.getCreatedOn_Server());
            values.put(createdOn_App, model.getCreatedOn_App());
            values.put(UPLOADED_ON, model.getUploadedOn());
            values.put(Server_ID, model.getEmployee_Leave_ID());

            values.put(device_Id, AppModel.getInstance().getDeviceId(context));
            values.put(Employee_SchoolId, model.getSchoolId());

            values.put(MODIFIED_ON, model.getModifiedOn());

//            long i = DB.update(TABLE_EmployeesLeaves, values,
//                    Employee_Personal_Detail_ID + " =" + model.getEmployee_Personal_Detail_ID() +
//                            " AND " + Leave_Start_Date + " = '" + model.getLeave_Start_Date() + "'",
//                    null);

            long i = DB.update(TABLE_EmployeesLeaves, values,
                    Server_ID + " =" + model.getEmployee_Leave_ID(),
                    null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Employee Leaves Emp Detail ID: " + model.getEmployee_Personal_Detail_ID());
            } else {
                AppModel.getInstance().appendLog(context, "Student updated Employee Leaves Emp Detail ID: " + model.getEmployee_Personal_Detail_ID());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateEmployeeLeaves Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    private boolean FindEmpTeacherLeaves(int ServerId, int schoolid) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves + " emp_leave"
                    + " WHERE emp_leave." + Server_ID + " = " + ServerId
                    + " AND emp_leave." + Employee_SchoolId + " =" + schoolid;

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

    private boolean IfEmployeesLeaveNotUploaded(int ServerId, int schoolid) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves + " emp_leave"
                    + " WHERE emp_leave." + Server_ID + " = " + ServerId
                    + " AND emp_leave." + Employee_SchoolId + " = " + schoolid
                    + " AND emp_leave." + UPLOADED_ON + " IS NULL";

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

    private void removeEmployeesLeavesNotExists(final int schoolId, final ArrayList<EmployeeLeaveModel> serverEmployeeLeavesList) {
        new Thread(() -> {
            boolean employeeLeavesFound;
            ArrayList<EmployeeLeaveModel> existingEmployeesLeavesList = getLeavesOfAllEmployees(schoolId);
            ArrayList<EmployeeLeaveModel> todo = new ArrayList<>();
            if (existingEmployeesLeavesList != null && existingEmployeesLeavesList.size() > 0) {
                for (EmployeeLeaveModel existingEmpLeaves : existingEmployeesLeavesList) {
                    employeeLeavesFound = false;
                    for (EmployeeLeaveModel serverEmpLeaves : serverEmployeeLeavesList) {
                        serverEmpLeaves.setSchoolId(schoolId);
                        if (existingEmpLeaves.getEmployee_Personal_Detail_ID() == serverEmpLeaves.getEmployee_Personal_Detail_ID()
                                && existingEmpLeaves.getLeave_Start_Date().equals(serverEmpLeaves.getLeave_Start_Date())
                                && existingEmpLeaves.getSchoolId() == serverEmpLeaves.getSchoolId()) {
                            employeeLeavesFound = true;
                        }
                    }
                    if (!employeeLeavesFound)
                        todo.add(existingEmpLeaves);
                }
            }
            for (EmployeeLeaveModel model : todo) {
                if (removeEmployeeLeaves(model)) {
                    AppModel.getInstance().appendLog(context, "Employee Leaves Removed: emp id = " + model.getEmployee_Personal_Detail_ID());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Employee Leaves: emp id = " + model.getEmployee_Personal_Detail_ID());
                }
            }
        }).start();
    }

    private ArrayList<EmployeeLeaveModel> getLeavesOfAllEmployees(int schoolid) {
        ArrayList<EmployeeLeaveModel> leaves = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves + " emp_leave"
                + " WHERE emp_leave." + Employee_SchoolId + " = " + schoolid;

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeLeaveModel model = new EmployeeLeaveModel();
                    model.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    model.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex(Leave_Start_Date)));
                    model.setLeave_End_Date(cursor.getString(cursor.getColumnIndex(Leave_End_Date)));
                    model.setLeave_status(cursor.getString(cursor.getColumnIndex(StatusName)));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
                    leaves.add(model);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return leaves;
    }

    private boolean removeEmployeeLeaves(EmployeeLeaveModel model) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EmployeesLeaves,
                    Employee_Personal_Detail_ID + "=" + model.getEmployee_Personal_Detail_ID() +
                            " AND " + Leave_Start_Date + "=" + model.getLeave_Start_Date() +
                            " AND " + Employee_SchoolId + "=" + model.getSchoolId(),
                    null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean FindSeparationAttachment(int ResignationID, String filePath) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SeparationImages
                    + " WHERE " + Employee_Resignation_Id + " = " + ResignationID + " AND " + SeparationAttachment + " = '" + filePath + "'";

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



    public long addSeparationAttachment(SeparationAttachmentsModel sam) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            values.put(Employee_Resignation_Id, sam.getResignationID());
            values.put(SeparationAttachment, sam.getSeparationAttachment());
            values.put(UPLOADED_ON, sam.getUploadedOn());
            values.put(isUploaded, 1);
            values.put(isActive, 1);
            values.put(MODIFIED_ON, sam.getModifiedOn());

            long i = DB.insert(TABLE_SeparationImages, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert resignation image with ID: " + sam.getResignationID());
            else
                AppModel.getInstance().appendLog(context, "Resignation Image inserted with ID: " + sam.getResignationID());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddSeparationAttachment Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public boolean IfSepAttachmentNotUploaded(int ResignationId, String path) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SeparationImages
                    + " WHERE " + Employee_Resignation_Id + " = " + ResignationId
                    + " AND " + SeparationAttachment + " = '" + path + "'"
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

    public long updateSepImage(SeparationAttachmentsModel sam) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(Employee_Resignation_Id, sam.getResignationID());
            values.put(SeparationAttachment, sam.getSeparationAttachment());
            values.put(UPLOADED_ON, sam.getUploadedOn());
            values.put(isUploaded, 1);
            values.put(isActive, 1);
            values.put(MODIFIED_ON, sam.getModifiedOn());

            long i = DB.update(TABLE_SeparationImages, values, Employee_Resignation_Id + " = " + sam.getResignationID() + " AND " + SeparationAttachment + " = '" + sam.getSeparationAttachment() + "'", null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Resign Image with ID: " + sam.getResignationID());
            } else {
                AppModel.getInstance().appendLog(context, "Record updated Resign Image with ID: " + sam.getResignationID());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateSapImage Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public void removeResignImageNotExists(final int schoolId, final ArrayList<SeparationAttachmentsModel> serverResignImagesList) {
        new Thread(() -> {
            boolean resignImageFound;
            ArrayList<SeparationAttachmentsModel> existingResignImageList = getAllResignedImagesList(schoolId);
            ArrayList<SeparationAttachmentsModel> todo = new ArrayList<>();
            if (existingResignImageList != null && existingResignImageList.size() > 0) {
                for (SeparationAttachmentsModel existingResignImage : existingResignImageList) {

                    try {
                        resignImageFound = false;
                        for (SeparationAttachmentsModel serverImage : serverResignImagesList) {

                            String name = serverImage.getSeparationAttachment();
                            name = name.replace("\\","/");
                            String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/";
                            serverImage.setSeparationAttachment(fdir + name);

                            if (existingResignImage.getResignationID() == serverImage.getResignationID()
                                    && existingResignImage.getSeparationAttachment().equals(serverImage.getSeparationAttachment())) {
                                resignImageFound = true;

                            }
                        }
                        if (!resignImageFound)
                            todo.add(existingResignImage);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            for (SeparationAttachmentsModel rmodel : todo) {
                if (removeResignImage(rmodel)) {
                    AppModel.getInstance().appendLog(context, "Resign Image Removed: id = " + rmodel.getResignationID() + " Path = " + rmodel.getSeparationAttachment());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Resign Image: id = " + rmodel.getResignationID() + " Path = " + rmodel.getSeparationAttachment());
                }
            }
        }).start();


    }

    public ArrayList<UserImageModel> getAllUserImagesForDownload() {
        ArrayList<UserImageModel> ermList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "select max(id), emp_detail_id, Image_Path from User_Images group by emp_detail_id";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    UserImageModel erm = new UserImageModel();
                    erm.setUser_id(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setUser_image_path(cursor.getString(cursor.getColumnIndex(ImagePath)));
                    if(erm.getUser_image_path() != null && !erm.getUser_image_path().trim().equals(""))
                        ermList.add(erm);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return ermList;
    }

    public ArrayList<SeparationAttachmentsModel> getAllResignedImagesList(int schoolId) {
        ArrayList<SeparationAttachmentsModel> ermList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT si.* FROM " + TABLE_SeparationImages
                    + " si INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " rt ON rt." + serverId + " = si." + Employee_Resignation_Id
                    + " WHERE rt." + Emp_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    SeparationAttachmentsModel erm = new SeparationAttachmentsModel();
                    erm.setResignationID(cursor.getInt(cursor.getColumnIndex(Employee_Resignation_Id)));
                    erm.setSeparationAttachment(cursor.getString(cursor.getColumnIndex(SeparationAttachment)));
                    ermList.add(erm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return ermList;
    }

    public boolean removeResignImage(SeparationAttachmentsModel employee) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_SeparationImages, Employee_Resignation_Id + " =" + employee.getResignationID() + " AND " + SeparationAttachment + " = '" + employee.getSeparationAttachment() + "'", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean FindResignedEmployeeRecord(int ServerId, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE
                    + " WHERE " + serverId + " = " + ServerId + "";

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

    public long addResignedEmployee(EmployeeSeparationModel resignedEmployee) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            //values.put(ID, resignedEmployee.getId());
            values.put(serverId, resignedEmployee.getId());
            values.put(Employee_Personal_Detail_ID, resignedEmployee.getEmployee_Personal_Detail_ID());
            values.put(Emp_Resign_Reason, resignedEmployee.getEmp_SubReasonID());
            values.put(Emp_SubReason_Text, resignedEmployee.getSubReasonText());
            values.put(Emp_Status, resignedEmployee.getEmp_Status());
            values.put(Emp_Resign_Date, AppModel.getInstance().convertDatetoFormat(resignedEmployee.getEmp_Resign_Date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));

            values.put(Emp_Resign_Letter_IMG, resignedEmployee.getEmp_Resign_Letter_IMG());
            values.put(Emp_Resign_Form_IMG, resignedEmployee.getEmp_Resign_Form_IMG());
            values.put(CREATED_ON_APP, resignedEmployee.getCREATED_ON_APP());
            values.put(Emp_Resign_Cancel_Reason, resignedEmployee.getEmp_Resign_Cancel_Reason());
            values.put(Emp_Resign_Type, getResignTypeByReason(resignedEmployee.getEmp_SubReasonID()));
            values.put(Emp_LastWorkingDay, AppModel.getInstance().convertDatetoFormat(resignedEmployee.getLastWorkingDay(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
            values.put(CREATED_BY, resignedEmployee.getCREATED_BY());
            values.put(Device_ID, resignedEmployee.getDeviceId());

            values.put(Emp_SchoolId, resignedEmployee.getSchoolID());
            values.put(Leave_Without_Pay, resignedEmployee.getLwop());

            int isActiveInt = 0;
            if(resignedEmployee.isActive())
                isActiveInt = 1;
            values.put(isActive, isActiveInt);

            if (resignedEmployee.getCREATED_ON_SERVER() != null) {
                String dateFormat = null;
                if (resignedEmployee.getCREATED_ON_SERVER().length() == 8)
                    dateFormat = "dd/MM/yy";
                else
                    dateFormat = "dd/MM/yy HH:mm:ss";
                resignedEmployee.setCREATED_ON_SERVER(AppModel.getInstance().convertDatetoFormat(resignedEmployee.getCREATED_ON_SERVER(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                values.put(CREATED_ON_SERVER, resignedEmployee.getCREATED_ON_SERVER());
            }

            values.put(MODIFIED_ON, resignedEmployee.getMODIFIED_ON());
            values.put(UPLOADED_ON, resignedEmployee.getUploadedOn());

            long i = DB.insert(EMPLOYEE_RESIGNATION_TABLE, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert resigned employee with Employee Detail ID: " + resignedEmployee.getId());
            else
                AppModel.getInstance().appendLog(context, "Resigned Employee inserted with Employee Detail ID: " + resignedEmployee.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddResignedEmployees Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public boolean IfResignedEmployeeNotUploaded(int ServerId, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE
                    + " WHERE " + serverId + " = " + ServerId
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

    public long updateResignedEmployee(EmployeeSeparationModel resignedEmployee) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            //values.put(ID, resignedEmployee.getId());
            values.put(serverId, resignedEmployee.getId());
            values.put(Employee_Personal_Detail_ID, resignedEmployee.getEmployee_Personal_Detail_ID());
            values.put(Emp_Resign_Reason, resignedEmployee.getEmp_SubReasonID());
            values.put(Emp_SubReason_Text, resignedEmployee.getSubReasonText());
            values.put(Emp_Status, resignedEmployee.getEmp_Status());
            values.put(Emp_Resign_Date, resignedEmployee.getEmp_Resign_Date());

            values.put(Emp_Resign_Letter_IMG, resignedEmployee.getEmp_Resign_Letter_IMG());
            values.put(Emp_Resign_Form_IMG, resignedEmployee.getEmp_Resign_Form_IMG());
            values.put(CREATED_ON_APP, resignedEmployee.getCREATED_ON_APP());
            values.put(Emp_Resign_Cancel_Reason, resignedEmployee.getEmp_Resign_Cancel_Reason());
            values.put(Emp_Resign_Type, getResignTypeByReason(resignedEmployee.getEmp_SubReasonID()));
            values.put(Emp_LastWorkingDay, resignedEmployee.getLastWorkingDay());
            values.put(CREATED_BY, resignedEmployee.getCREATED_BY());
            values.put(Device_ID, resignedEmployee.getDeviceId());

            values.put(Emp_SchoolId, resignedEmployee.getSchoolID());
            values.put(Leave_Without_Pay, resignedEmployee.getLwop());

            if (resignedEmployee.getCREATED_ON_SERVER() != null) {
                String dateFormat = null;
                if (resignedEmployee.getCREATED_ON_SERVER().length() == 8)
                    dateFormat = "dd/MM/yy";
                else
                    dateFormat = "dd/MM/yy HH:mm:ss";
                resignedEmployee.setCREATED_ON_SERVER(AppModel.getInstance().convertDatetoFormat(resignedEmployee.getCREATED_ON_SERVER(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                values.put(CREATED_ON_SERVER, resignedEmployee.getCREATED_ON_SERVER());
                //values.put(UPLOADED_ON, resignedEmployee.getUploadedOn());
            }

            values.put(UPLOADED_ON, resignedEmployee.getUploadedOn());
            values.put(MODIFIED_ON, resignedEmployee.getMODIFIED_ON());

//            long i = DB.update(EMPLOYEE_RESIGNATION_TABLE, values, CREATED_ON_APP + " =" + resignedEmployee.getCREATED_ON_APP() + " AND " + Employee_Personal_Detail_ID + " =" + resignedEmployee.getEmployee_Personal_Detail_ID(), null);
            long i = DB.update(EMPLOYEE_RESIGNATION_TABLE, values, serverId + " = " + resignedEmployee.getId(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Resigned Employee ID: " + resignedEmployee.getId());
            } else {
                AppModel.getInstance().appendLog(context, "Record updated Resigned Employee ID: " + resignedEmployee.getId());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateResignedEmployee Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public void removeResignedEmployeesNotExists(final int schoolId, final ArrayList<EmployeeSeparationModel> serverResignedEmployeeList) {
        new Thread(() -> {
            boolean resignedEmployeeFound;
            ArrayList<EmployeeSeparationModel> existingResignedEmployeesList = getAllResignedEmployeesList(schoolId);
            ArrayList<EmployeeSeparationModel> todo = new ArrayList<>();
            if (existingResignedEmployeesList != null && existingResignedEmployeesList.size() > 0) {
                for (EmployeeSeparationModel existingResignedEmployee : existingResignedEmployeesList) {

                    try {
                        resignedEmployeeFound = false;
                        for (EmployeeSeparationModel serverStudent : serverResignedEmployeeList) {
                            if (schoolId > 0) {
                                serverStudent.setSchoolID(schoolId);
                            }
                            if (existingResignedEmployee.getCREATED_ON_APP().equals(serverStudent.getCREATED_ON_APP())
                                    && existingResignedEmployee.getEmployee_Personal_Detail_ID() == serverStudent.getEmployee_Personal_Detail_ID()
                                    && existingResignedEmployee.getSchoolID() == serverStudent.getSchoolID()) {
                                resignedEmployeeFound = true;

                            }
                        }
                        if (!resignedEmployeeFound)
                            todo.add(existingResignedEmployee);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            for (EmployeeSeparationModel rmodel : todo) {
                if (removeResignedEmployee(rmodel)) {
                    AppModel.getInstance().appendLog(context, "Resignation Removed: id = " + rmodel.getId() + " Emp ID = " + rmodel.getEmployee_Personal_Detail_ID());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Resignation: id = " + rmodel.getId() + " Emp ID = " + rmodel.getEmployee_Personal_Detail_ID());
                }
            }
        }).start();


    }

    private ArrayList<EmployeeSeparationModel> getAllResignedEmployeesList(int schoolId) {
        ArrayList<EmployeeSeparationModel> ermList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT re.* FROM " + EMPLOYEE_RESIGNATION_TABLE + " re"
                    + " WHERE re." + Emp_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeSeparationModel erm = new EmployeeSeparationModel();
                    erm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setCREATED_ON_APP(cursor.getString(cursor.getColumnIndex(CREATED_ON_APP)));
                    erm.setSchoolID(cursor.getInt(cursor.getColumnIndex(Emp_SchoolId)));
                    //em.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                    ermList.add(erm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return ermList;
    }

    public boolean removeResignedEmployee(EmployeeSeparationModel employee) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(EMPLOYEE_RESIGNATION_TABLE, CREATED_ON_APP + " ='" + employee.getCREATED_ON_APP() +
                    "' AND " + Employee_Personal_Detail_ID + " =" + employee.getEmployee_Personal_Detail_ID() +
                    " AND " + Emp_SchoolId + " =" + employee.getSchoolID(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean FindQualificationHistoryRecord(EmployeeQualificationDetailModel model) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeeQualificationDetail + " WHERE " +
                    Employee_Personal_Detail_ID + " = " + model.getEmployee_Personal_Detail_ID()
                    + " AND (ifnull(Passing_Year,'null')= '" + model.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + model.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + model.getQualification_Level() + "')" ;

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

    public long addQualificationHistoryRecord(EmployeeQualificationDetailModel qualificationHistory) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {
//            if (qualificationHistory.getQualification_Type() != null &&
//                    qualificationHistory.getQualification_Level() != null &&
//                    qualificationHistory.getPassing_Year() != null) {


            if (qualificationHistory.getQualification_Type() != null ||
                    qualificationHistory.getQualification_Level() != null ||
                    qualificationHistory.getPassing_Year() != null) {
                ContentValues values = new ContentValues();
                //values.put(ID, qualificationHistory.getId());
                values.put(Employee_SchoolId, qualificationHistory.getSchoolID());
                values.put(Employee_Personal_Detail_ID, qualificationHistory.getEmployee_Personal_Detail_ID());
                values.put(Institute_Name, qualificationHistory.getInstitute_Name());
                values.put(Degree_Name, qualificationHistory.getDegree_Name());

                values.put(Subject_Name, qualificationHistory.getSubject_Name());
                values.put(Passing_Year, qualificationHistory.getPassing_Year());
                values.put(Grade_Division, qualificationHistory.getGrade_Division());
                values.put(EmployeeQualificationType, qualificationHistory.getQualification_Type());
                values.put(EmployeeQualificationLevel, qualificationHistory.getQualification_Level());
                values.put(Emp_Date_Of_Joining, qualificationHistory.getDateOfJoining());
                values.put(Employee_Code, qualificationHistory.getEmp_Code());
                values.put(MODIFIED_ON, qualificationHistory.getModifiedOn());

                i = DB.insert(TABLE_EmployeeQualificationDetail, null, values);
                if (i == -1)
                    AppModel.getInstance().appendLog(context, "Couldn't insert Qualification history record with ID: " + qualificationHistory.getId());
                else
                    AppModel.getInstance().appendLog(context, "Qualification History record inserted with ID: " + qualificationHistory.getId());

                return i;
            }
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddQualificationHistoryRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
        return i;
    }


    public long updateQualificationHistoryRecord(EmployeeQualificationDetailModel qualificationHistory) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        long i = -1;
        try {

            if (qualificationHistory.getQualification_Type() != null ||
                    qualificationHistory.getQualification_Level() != null ||
                    qualificationHistory.getPassing_Year() != null) {
                ContentValues values = new ContentValues();
                values.put(Employee_SchoolId, qualificationHistory.getSchoolID());
                values.put(Employee_Personal_Detail_ID, qualificationHistory.getEmployee_Personal_Detail_ID());
                values.put(Institute_Name, qualificationHistory.getInstitute_Name());
                values.put(Degree_Name, qualificationHistory.getDegree_Name());

                values.put(Subject_Name, qualificationHistory.getSubject_Name());
                values.put(Passing_Year, qualificationHistory.getPassing_Year());
                values.put(Grade_Division, qualificationHistory.getGrade_Division());
                values.put(EmployeeQualificationType, qualificationHistory.getQualification_Type());
                values.put(EmployeeQualificationLevel, qualificationHistory.getQualification_Level());
                values.put(Emp_Date_Of_Joining, qualificationHistory.getDateOfJoining());
                values.put(Employee_Code, qualificationHistory.getEmp_Code());
                values.put(MODIFIED_ON, qualificationHistory.getModifiedOn());

                i = DB.update(TABLE_EmployeeQualificationDetail,
                        values,
                        Employee_Personal_Detail_ID + " = " + qualificationHistory.getEmployee_Personal_Detail_ID()
                                + " AND (ifnull(Passing_Year,'null')= '" + qualificationHistory.getPassing_Year() + "' AND  ifnull(QualificationType,'null')= '" + qualificationHistory.getQualification_Type() + "' AND  ifnull(QualificationLevel,'null')= '" + qualificationHistory.getQualification_Level() + "')",
                        null
);
                if (i == -1)
                    AppModel.getInstance().appendLog(context, "Couldn't insert Qualification history record with ID: " + qualificationHistory.getId());
                else
                    AppModel.getInstance().appendLog(context, "Qualification History record inserted with ID: " + qualificationHistory.getId());

                return i;
            }
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddQualificationHistoryRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return i;
        }
        return i;
    }

    public void removeQHRecordNotExists(final int schoolId, final ArrayList<EmployeeQualificationDetailModel> serverQualificationHistoryRecordsList) {
        new Thread(() -> {
            boolean QHRecordFound;
            ArrayList<EmployeeQualificationDetailModel> existingRecordsList = getAllRecordsList(schoolId);
            ArrayList<EmployeeQualificationDetailModel> todo = new ArrayList<>();
            if (existingRecordsList != null && existingRecordsList.size() > 0) {
                for (EmployeeQualificationDetailModel existingRecord : existingRecordsList) {
                    QHRecordFound = false;
                    for (EmployeeQualificationDetailModel serverQHRecord : serverQualificationHistoryRecordsList) {
//                        if (existingRecord.getEmployee_Personal_Detail_ID() == serverQHRecord.getEmployee_Personal_Detail_ID() &&
//                                existingRecord.getPassing_Year() != null && existingRecord.getPassing_Year().equals(serverQHRecord.getPassing_Year())) {

                        if((existingRecord.getEmployee_Personal_Detail_ID() == serverQHRecord.getEmployee_Personal_Detail_ID())){
                            if(existingRecord.getPassing_Year() != null && serverQHRecord.getPassing_Year() != null){
                                if(existingRecord.getPassing_Year().equals(serverQHRecord.getPassing_Year()))
                                    QHRecordFound = true;
                            } else if (existingRecord.getQualification_Type() != null && serverQHRecord.getQualification_Type() != null){
                                if(existingRecord.getQualification_Type().equals(serverQHRecord.getQualification_Type()))
                                    QHRecordFound = true;
                            }
                        }
                    }
                    if (!QHRecordFound)
                        todo.add(existingRecord);
                }
            }
            for (EmployeeQualificationDetailModel model : todo) {
                if (removeQHRecord(model)) {
                    AppModel.getInstance().appendLog(context, "Record Removed: id = " + model.getId() + " Emp Code = " + model.getEmp_Code());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove record: id = " + model.getId() + " Emp Code = " + model.getEmp_Code());
                }
            }
        }).start();


    }

    private ArrayList<EmployeeQualificationDetailModel> getAllRecordsList(int schoolId) {
        ArrayList<EmployeeQualificationDetailModel> eqdList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT e.* FROM " + TABLE_EmployeeQualificationDetail + " e"
                    + " WHERE e." + Employee_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeQualificationDetailModel eqd = new EmployeeQualificationDetailModel();
                    eqd.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    eqd.setEmp_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    eqd.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    eqd.setPassing_Year(cursor.getString(cursor.getColumnIndex(Passing_Year)));
                    eqdList.add(eqd);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return eqdList;
    }

    public boolean removeQHRecord(EmployeeQualificationDetailModel qhRecord) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EmployeeQualificationDetail, Employee_Personal_Detail_ID + "=" + qhRecord.getEmployee_Personal_Detail_ID() + " AND " + Passing_Year + " = '" + qhRecord.getPassing_Year() + "'", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean FindPositionHistoryRecord(int userId, String startDate) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EmployeePosition + " WHERE " + Employee_Personal_Detail_ID + " = " + userId
                    + " AND " + Position_Start_Date + " ='" + startDate + "'";

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

    public long addPositionHistoryRecord(EmployeePositionModel positionHistory) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            //values.put(ID, positionHistory.getId());
            values.put(Employee_SchoolId, positionHistory.getSchoolId());
            values.put(Employee_Personal_Detail_ID, positionHistory.getEmployee_Personal_Detail_ID());
            values.put(Position_Name, positionHistory.getPosition_Name());
            values.put(Position_Start_Date, positionHistory.getPosition_Start_Date());
            values.put(Position_End_Date, positionHistory.getPosition_End_Date());
            values.put(Employee_Code, positionHistory.getEmp_Code());
            values.put(MODIFIED_ON, positionHistory.getModifiedOn());

            long i = DB.insert(TABLE_EmployeePosition, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert Position history record with ID: " + positionHistory.getId());
            else
                AppModel.getInstance().appendLog(context, "Position History record inserted with ID: " + positionHistory.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddPositionHistoryRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public long updatePositionHistoryRecord(EmployeePositionModel positionHistory) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {

            ContentValues values = new ContentValues();
            //values.put(ID, positionHistory.getId());
            values.put(Employee_SchoolId, positionHistory.getSchoolId());
            values.put(Employee_Personal_Detail_ID, positionHistory.getEmployee_Personal_Detail_ID());
            values.put(Position_Name, positionHistory.getPosition_Name());
            values.put(Position_Start_Date, positionHistory.getPosition_Start_Date());
            values.put(Position_End_Date, positionHistory.getPosition_End_Date());
            values.put(Employee_Code, positionHistory.getEmp_Code());
            values.put(MODIFIED_ON, positionHistory.getModifiedOn());

            long i = DB.update(TABLE_EmployeePosition,
                    values,
                    Employee_Personal_Detail_ID + " = " + positionHistory.getEmployee_Personal_Detail_ID()
                            + " AND " + Position_Start_Date + " ='" + positionHistory.getPosition_Start_Date() + "'",
                    null);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't update Position history record with ID: " + positionHistory.getId());
            else
                AppModel.getInstance().appendLog(context, "Position History record updated with ID: " + positionHistory.getId());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updatePositionHistoryRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public void removePHRecordNotExists(final int schoolId, final ArrayList<EmployeePositionModel> serverPositionHistoryRecordsList) {
        new Thread(() -> {
            boolean PHRecordFound;
            ArrayList<EmployeePositionModel> existingPRecordsList = getAllPRecordsList(schoolId);
            ArrayList<EmployeePositionModel> todo = new ArrayList<>();
            if (existingPRecordsList != null && existingPRecordsList.size() > 0) {
                for (EmployeePositionModel existingPRecord : existingPRecordsList) {
                    PHRecordFound = false;
                    for (EmployeePositionModel serverPHRecord : serverPositionHistoryRecordsList) {
                        if (existingPRecord.getEmployee_Personal_Detail_ID() == serverPHRecord.getEmployee_Personal_Detail_ID() && existingPRecord.getPosition_Start_Date().equals(serverPHRecord.getPosition_Start_Date())) {
                            PHRecordFound = true;
                        }
                    }
                    if (!PHRecordFound)
                        todo.add(existingPRecord);
                }
            }
            for (EmployeePositionModel model : todo) {
                if (removePHRecord(model)) {
                    AppModel.getInstance().appendLog(context, "Position Record Removed: id = " + model.getId() + " Emp Code = " + model.getEmp_Code());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove position history record: id = " + model.getId() + " Emp Code = " + model.getEmp_Code());
                }
            }
        }).start();


    }

    private ArrayList<EmployeePositionModel> getAllPRecordsList(int schoolId) {
        ArrayList<EmployeePositionModel> epmList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT e.* FROM " + TABLE_EmployeePosition
                    + " WHERE e." + Employee_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeePositionModel epm = new EmployeePositionModel();
                    epm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    epm.setEmp_Code(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    epm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    epm.setPosition_Start_Date(cursor.getString(cursor.getColumnIndex(Position_Start_Date)));
                    epmList.add(epm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return epmList;
    }

    public boolean removePHRecord(EmployeePositionModel phRecord) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EmployeePosition, ID + "=" + phRecord.getEmployee_Personal_Detail_ID() + " AND " + Position_Start_Date + " = '" + phRecord.getPosition_Start_Date() + "'", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean FindEmpTeacherAttendanceRecord(int ServerId, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT ta.* FROM " + TABLE_EMPLOYEE_TEACHER_Attendance + " ta"
                    + " WHERE ta." + Employee_SchoolId + " = " + schoolId
                    + " AND ta." + serverId + " = " + ServerId + "";

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

    public void addEmpTeacherAttendanceRecord(ArrayList<EmployeeTeacherAttendanceModel> etamList, int schoolId, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (EmployeeTeacherAttendanceModel teacherAttendanceModel : etamList) {
                teacherAttendanceModel.setSchoolId(schoolId);
                if (!FindEmpTeacherAttendanceRecord(teacherAttendanceModel.getId(), schoolId)) {
                    teacherAttendanceModel.setUploaded_on(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    ContentValues values = new ContentValues();
                    //values.put(ID, teacherAttendanceModel.getId());
                    values.put(serverId, teacherAttendanceModel.getId());
                    values.put(Employee_Personal_Detail_ID, teacherAttendanceModel.getEmployee_Personal_Detail_ID());
                    values.put(For_Date, AppModel.getInstance().convertDatetoFormat(teacherAttendanceModel.getFor_date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
                    values.put(Attendance_Type_ID, teacherAttendanceModel.getAttendance_Type_ID());
                    values.put(Leave_Type_ID, teacherAttendanceModel.getLeave_Type_ID());

                    values.put(Reason, teacherAttendanceModel.getReason());
                    values.put(created_By, teacherAttendanceModel.getCreated_By());
                    values.put(createdOn_App, teacherAttendanceModel.getCreatedOn_App());
                    values.put(device_Id, teacherAttendanceModel.getDevice_Id());

                    if (teacherAttendanceModel.getCreatedOn_Server() != null) {
                        String dateFormat = null;
                        if (teacherAttendanceModel.getCreatedOn_Server().length() == 8)
                            dateFormat = "dd/MM/yy";
                        else
                            dateFormat = "dd/MM/yy HH:mm:ss";
                        teacherAttendanceModel.setCreatedOn_Server(AppModel.getInstance().convertDatetoFormat(teacherAttendanceModel.getCreatedOn_Server(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                        values.put(createdOn_Server, teacherAttendanceModel.getCreatedOn_Server());
                    }



                    values.put(UPLOADED_ON, teacherAttendanceModel.getUploaded_on());

                    if (teacherAttendanceModel.getModifiedOn() != null) {
                        String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                        teacherAttendanceModel.setModifiedOn(AppModel.getInstance().convertDatetoFormat(teacherAttendanceModel.getModifiedOn(),
                                dateFormat, "yyyy-MM-dd hh:mm:ss"));
                        values.put(MODIFIED_ON, teacherAttendanceModel.getModifiedOn());
                    }

                    values.put(isActive,teacherAttendanceModel.isActive());
                    values.put(Employee_SchoolId,teacherAttendanceModel.getSchoolId());

                    long i = DB.insert(TABLE_EMPLOYEE_TEACHER_Attendance, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert Teacher Attendance with Employee Detail ID: " + teacherAttendanceModel.getEmployee_Personal_Detail_ID());
                    else if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Teacher Attendance inserted with Employee Detail ID: " + teacherAttendanceModel.getEmployee_Personal_Detail_ID());

                        downloadedCount++;
                    }


                } else {
                    if (!IfEmpTeacherAttendanceNotUploaded(teacherAttendanceModel.getId(), schoolId)) {
                        teacherAttendanceModel.setUploaded_on(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long i = updateEmpTeacherAttendance(teacherAttendanceModel);

                        if (i > 0) {
                            downloadedCount++;
                        }

                    }
                }

                //Update sync progress

                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }
//            removeEmpTeacherAttendanceNotExists(schoolId, etamList);
            DB.setTransactionSuccessful();

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In addEmpTeacherAttendanceRecord Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            DataSync.areAllServicesSuccessful = false;
        } finally {
            DB.endTransaction();
        }

    }

    public boolean IfEmpTeacherAttendanceNotUploaded(int ServerId, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT ta.* FROM " + TABLE_EMPLOYEE_TEACHER_Attendance + " ta"
                    + " WHERE ta." + Employee_SchoolId + " = " + schoolId
                    + " AND ta." + serverId + " =" + ServerId + ""
                    + " AND ta." + UPLOADED_ON + " IS NULL";

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

    public long updateEmpTeacherAttendance(EmployeeTeacherAttendanceModel teacherAttendanceModel) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            ContentValues values = new ContentValues();
            //values.put(ID, teacherAttendanceModel.getId());
            values.put(For_Date, AppModel.getInstance().convertDatetoFormat(teacherAttendanceModel.getFor_date(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd"));
            values.put(Employee_Personal_Detail_ID, teacherAttendanceModel.getEmployee_Personal_Detail_ID());
            // values.put(For_Date, teacherAttendanceModel.getFor_date());
            values.put(Attendance_Type_ID, teacherAttendanceModel.getAttendance_Type_ID());
            values.put(Leave_Type_ID, teacherAttendanceModel.getLeave_Type_ID());

            values.put(Reason, teacherAttendanceModel.getReason());
            values.put(created_By, teacherAttendanceModel.getCreated_By());
            values.put(createdOn_App, teacherAttendanceModel.getCreatedOn_App());
            values.put(device_Id, teacherAttendanceModel.getDevice_Id());
            values.put(serverId, teacherAttendanceModel.getId());

            if (teacherAttendanceModel.getCreatedOn_Server() != null) {
                String dateFormat = null;
                if (teacherAttendanceModel.getCreatedOn_Server().length() == 8)
                    dateFormat = "dd/MM/yy";
                else
                    dateFormat = "dd/MM/yy HH:mm:ss";
                teacherAttendanceModel.setCreatedOn_Server(AppModel.getInstance().convertDatetoFormat(teacherAttendanceModel.getCreatedOn_Server(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                values.put(createdOn_Server, teacherAttendanceModel.getCreatedOn_Server());
            }

            values.put(UPLOADED_ON, teacherAttendanceModel.getUploaded_on());

            if (teacherAttendanceModel.getModifiedOn() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                teacherAttendanceModel.setModifiedOn(AppModel.getInstance().convertDatetoFormat(teacherAttendanceModel.getModifiedOn(),
                        dateFormat, "yyyy-MM-dd hh:mm:ss"));
                values.put(MODIFIED_ON, teacherAttendanceModel.getModifiedOn());
            }

            values.put(isActive,teacherAttendanceModel.isActive());
            values.put(Employee_SchoolId,teacherAttendanceModel.getSchoolId());

//            long i = DB.update(TABLE_EMPLOYEE_TEACHER_Attendance, values, For_Date + " =" + teacherAttendanceModel.getFor_date() + " AND " + Employee_Personal_Detail_ID + " =" + teacherAttendanceModel.getEmployee_Personal_Detail_ID(), null);
            long i = DB.update(TABLE_EMPLOYEE_TEACHER_Attendance, values, serverId + " = " + teacherAttendanceModel.getId(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Teacher Attendance with ID: " + teacherAttendanceModel.getId());
            } else {
                AppModel.getInstance().appendLog(context, "Record updated Teacher Attendance with Employee ID: " + teacherAttendanceModel.getId());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateEmpTeacherAttendance Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public void removeEmpTeacherAttendanceNotExists(final int schoolId, final ArrayList<EmployeeTeacherAttendanceModel> serverTeacherAttendanceList) {
        new Thread(() -> {
            boolean teacherAttendanceFound;
            ArrayList<EmployeeTeacherAttendanceModel> existingTeacherAttendanceList = getAllTeacherAttendanceList(schoolId);
            ArrayList<EmployeeTeacherAttendanceModel> todo = new ArrayList<>();
            if (existingTeacherAttendanceList != null && existingTeacherAttendanceList.size() > 0) {
                for (EmployeeTeacherAttendanceModel existingTeacherAttendance : existingTeacherAttendanceList) {
                    teacherAttendanceFound = false;
                    for (EmployeeTeacherAttendanceModel serverTeacherAttendance : serverTeacherAttendanceList) {
                        serverTeacherAttendance.setSchoolId(schoolId);
                        if (existingTeacherAttendance.getFor_date().equals(serverTeacherAttendance.getFor_date())
                                && existingTeacherAttendance.getEmployee_Personal_Detail_ID() == serverTeacherAttendance.getEmployee_Personal_Detail_ID()
                                && existingTeacherAttendance.getSchoolId() == serverTeacherAttendance.getSchoolId()) {
                            teacherAttendanceFound = true;

                        }
                    }
                    if (!teacherAttendanceFound)
                        todo.add(existingTeacherAttendance);
                }
            }
            for (EmployeeTeacherAttendanceModel rmodel : todo) {
                if (removeTeacherAttendance(rmodel)) {
                    AppModel.getInstance().appendLog(context, "Attendance Removed: id = " + rmodel.getId() + " Emp ID = " + rmodel.getEmployee_Personal_Detail_ID());
                } else {
                    AppModel.getInstance().appendLog(context, "Couldn't remove Attendance: id = " + rmodel.getId() + " Emp ID = " + rmodel.getEmployee_Personal_Detail_ID());
                }
            }
        }).start();


    }

    private ArrayList<EmployeeTeacherAttendanceModel> getAllTeacherAttendanceList(int schoolId) {
        ArrayList<EmployeeTeacherAttendanceModel> etamList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT ta.* FROM " + TABLE_EMPLOYEE_TEACHER_Attendance
                    + " WHERE ta." + Employee_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    EmployeeTeacherAttendanceModel etam = new EmployeeTeacherAttendanceModel();
                    etam.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    etam.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    etam.setFor_date(cursor.getString(cursor.getColumnIndex(For_Date)));
                    etam.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
                    //em.setEmployee_ID(cursor.getInt(cursor.getColumnIndex(Employee_ID)));
                    etamList.add(etam);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return etamList;
    }

    private boolean removeTeacherAttendance(EmployeeTeacherAttendanceModel teacherAttendanceModel) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            return db.delete(TABLE_EMPLOYEE_TEACHER_Attendance, For_Date + "='" + teacherAttendanceModel.getFor_date() + "'"+
                    " AND " + Employee_Personal_Detail_ID + " =" + teacherAttendanceModel.getEmployee_Personal_Detail_ID() +
                    " AND " + Employee_SchoolId + " =" + teacherAttendanceModel.getSchoolId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    public ArrayList<UploadEmployeeModel> getAllEmployeesDetailForUpload(int schoolId) {
        ArrayList<UploadEmployeeModel> emList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp.* FROM " + EMPLOYEE_DETAIL_TABLE + " emp"
                    + " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_school ON emp_school." + Employee_Personal_Detail_ID + " = emp." + ID
                    + " WHERE (emp." + UPLOADED_ON + " IS NULL OR "
                    + " emp." + UPLOADED_ON + " = '')"
                    + " AND emp_school." + Employee_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    UploadEmployeeModel em = new UploadEmployeeModel();
                    em.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    em.setMobile_No(cursor.getString(cursor.getColumnIndex(Emp_Mobile_No)));
                    em.setEmail(cursor.getString(cursor.getColumnIndex(Emp_Email)));

                    emList.add(em);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return emList;
    }

    public ArrayList<EmployeeLeaveModel> getAllEmployeesLeavesForUpload(int schoolId) {
        ArrayList<EmployeeLeaveModel> emLeaveList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp_leave.* FROM " + TABLE_EmployeesLeaves + " emp_leave"
                    + " WHERE (emp_leave." + UPLOADED_ON + " IS NULL OR "
                    + " emp_leave." + UPLOADED_ON + " = '')"
                    + " AND emp_leave." + Employee_SchoolId + " = " + schoolId;

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    EmployeeLeaveModel model = new EmployeeLeaveModel();
                    model.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    model.setLeave_Type_ID(cursor.getInt(cursor.getColumnIndex(Leave_Type_ID)));
                    model.setLeave_status_id(cursor.getInt(cursor.getColumnIndex(Leave_Status_id)));
                    model.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex(Leave_Start_Date)));
                    model.setLeave_End_Date(cursor.getString(cursor.getColumnIndex(Leave_End_Date)));
                    model.setCreated_By(cursor.getInt(cursor.getColumnIndex(created_By)));
                    model.setCreatedOn_App(cursor.getString(cursor.getColumnIndex(createdOn_App)));
                    model.setDevice_Id(cursor.getString(cursor.getColumnIndex(device_Id)));
                    model.setServer_Id(cursor.getInt(cursor.getColumnIndex(Server_ID)));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));

                    emLeaveList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return emLeaveList;
    }

    public ArrayList<EmployeeSeparationDetailModel> getAllApprovalsForUpload() {
        ArrayList<EmployeeSeparationDetailModel> ermList = new ArrayList<EmployeeSeparationDetailModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + PENDING_SEPARATION_TABLE + " WHERE ( " + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = '')";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    EmployeeSeparationDetailModel erm = new EmployeeSeparationDetailModel();
                    erm.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    erm.setEmployeeResignationId(cursor.getInt(cursor.getColumnIndex(Employee_Resignation_Id)));
                    erm.setApprover_userId(cursor.getInt(cursor.getColumnIndex(Approver_userId)));
                    erm.setEmp_status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
                    erm.setSeparation_Remarks(cursor.getString(cursor.getColumnIndex(Separation_Remarks)));
                    erm.setCREATED_BY(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                    erm.setMODIFIED_BY(cursor.getInt(cursor.getColumnIndex(MODIFIED_BY)));
                    erm.setMODIFIED_ON(cursor.getString(cursor.getColumnIndex(MODIFIED_ON)));
                    erm.setServerId(cursor.getInt(cursor.getColumnIndex(ID)));
                    erm.setCREATED_ON_APP(cursor.getString(cursor.getColumnIndex(CREATED_ON_APP)));

                    ermList.add(erm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return ermList;
    }

    public ArrayList<EmployeeSeparationModel> getAllResignationsForUpload() {
        ArrayList<EmployeeSeparationModel> ermList = new ArrayList<EmployeeSeparationModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + EMPLOYEE_RESIGNATION_TABLE + " WHERE " + UPLOADED_ON + " IS NULL OR " + UPLOADED_ON + " = ''";


            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    EmployeeSeparationModel erm = new EmployeeSeparationModel();
                    erm.setId(cursor.getInt(cursor.getColumnIndex(serverId)));
                    erm.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    erm.setEmp_Resign_Date(cursor.getString(cursor.getColumnIndex(Emp_Resign_Date)));
                    erm.setEmp_SubReasonID(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Reason)));
                    erm.setLastWorkingDay(cursor.getString(cursor.getColumnIndex(Emp_LastWorkingDay)));
                    erm.setSubReasonText(cursor.getString(cursor.getColumnIndex(Emp_SubReason_Text)));
                    erm.setLwop(cursor.getInt(cursor.getColumnIndex(Leave_Without_Pay)));
                    erm.setCREATED_BY(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                    erm.setMODIFIED_BY(cursor.getInt(cursor.getColumnIndex(MODIFIED_BY)));
                    erm.setMODIFIED_ON(cursor.getString(cursor.getColumnIndex(MODIFIED_ON)));
                    erm.setCancelledBy(cursor.getInt(cursor.getColumnIndex(Cancelled_By)));
                    erm.setCancelledOn(cursor.getString(cursor.getColumnIndex(Cancelled_On)));
                    if(cursor.getInt(cursor.getColumnIndex(isActive)) == 1)
                        erm.setActive(true);
                    else
                        erm.setActive(false);
                    erm.setServer_id(cursor.getInt(cursor.getColumnIndex(ID)));
//                    erm.setEmp_Resign_Type(cursor.getInt(cursor.getColumnIndex(Emp_Resign_Type)));
//                    erm.setEmp_Resign_Letter_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Letter_IMG)));
//                    erm.setEmp_Resign_Form_IMG(cursor.getString(cursor.getColumnIndex(Emp_Resign_Form_IMG)));
                    erm.setEmp_Resign_Cancel_Reason(cursor.getString(cursor.getColumnIndex(Emp_Resign_Cancel_Reason)));
                    erm.setCREATED_ON_APP(cursor.getString(cursor.getColumnIndex(CREATED_ON_APP)));
//                    erm.setCREATED_ON_SERVER(cursor.getString(cursor.getColumnIndex(CREATED_ON_SERVER)));
//                    erm.setEmp_Status(cursor.getInt(cursor.getColumnIndex(Emp_Status)));
//                    erm.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));

                    ermList.add(erm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return ermList;
    }


    public ArrayList<EmployeeTeacherAttendanceModel> getAllTeachersAttendanceForUpload(int schoolId) {
        ArrayList<EmployeeTeacherAttendanceModel> etamList = new ArrayList<EmployeeTeacherAttendanceModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT emp_att.* FROM " + TABLE_EMPLOYEE_TEACHER_Attendance + " emp_att"
                    + " WHERE (emp_att." + UPLOADED_ON + " IS NULL OR emp_att."
                    + UPLOADED_ON + " = '')"
                    + " AND emp_att." + Employee_SchoolId + " = " + schoolId;


            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    EmployeeTeacherAttendanceModel etam = new EmployeeTeacherAttendanceModel();
                    etam.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    etam.setFor_date(cursor.getString(cursor.getColumnIndex(For_Date)));
                    etam.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    etam.setAttendance_Type_ID(cursor.getInt(cursor.getColumnIndex(Attendance_Type_ID)));
                    etam.setLeave_Type_ID(cursor.getInt(cursor.getColumnIndex(Leave_Type_ID)));
                    etam.setReason("");
                    etam.setCreated_By(cursor.getInt(cursor.getColumnIndex(created_By)));
                    etam.setCreatedOn_App(cursor.getString(cursor.getColumnIndex(createdOn_App)));
                    etam.setDevice_Id(cursor.getString(cursor.getColumnIndex(device_Id)));
                    etam.setServerId(cursor.getInt(cursor.getColumnIndex(serverId)));
                    etam.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));


                    etamList.add(etam);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return etamList;
    }


    public void updateUploadedOnInSeparationDetail(GeneralUploadResponseModel model) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();
            if (model.server_id > 0) { //must not be null or 0 after upload
                ContentValues values = new ContentValues();
                values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                values.put(serverId, model.server_id);
                long i = DB.update(SEPARATION_DETAIL_TABLE, values, ID + " = " + model.device_id, null);
                if (i > 0) {
                    AppModel.getInstance().appendLog(context, "Resignation Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());
                }
                AppModel.getInstance().appendLog(context, "Resignation Uploading Successful server id:" + model.server_id);
            } else {
                AppModel.getInstance().appendLog(context, "Resignation or Termination not uploaded");
                AppModel.getInstance().appendErrorLog(context, "Resignation or Termination not uploaded!");
            }


            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DB.endTransaction();
        }
    }

    public void updateServerIdInResignation(ArrayList<GeneralUploadResponseModel> body, SyncDownloadUploadModel syncDownloadUploadModel) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();
            int uploadedCount = 0;

            for (GeneralUploadResponseModel model : body) {
                long i = 0;
                if (model.server_id > 0) { //must not be null or 0 after upload
                    ContentValues values = new ContentValues();
                    values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    values.put(serverId, model.server_id);
                    i = DB.update(EMPLOYEE_RESIGNATION_TABLE, values, ID + " = " + model.device_id, null);
                    if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Resignation Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());
                        uploadedCount++;
                        ContentValues cv = new ContentValues();
                        cv.put(Employee_Resignation_Id,model.server_id);
                        cv.put(isUploaded, 1);
                        i = DB.update(TABLE_SeparationImages, cv, Employee_Resignation_Id + " = " + model.device_id, null);
                        if(i > 0){
                            AppModel.getInstance().appendLog(context, "ResignationId in TABLE_SeparationImages is updated: " + model.device_id);
                        } else
                            AppModel.getInstance().appendLog(context, "FAILED: ResignationId in TABLE_SeparationImages is not updated: " + model.device_id);
                    }
                    AppModel.getInstance().appendLog(context, "Resignation Uploading Successful server id:" + model.server_id);
                } else {
                    AppModel.getInstance().appendLog(context, "Separations not uploaded, Error: " + model.ErrorMessage + " Device id: " + model.device_id);
                    AppModel.getInstance().appendErrorLog(context, "Separations not uploaded, Error: " + model.ErrorMessage + " Device id: " + model.device_id);
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
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


    public void updateServerIdInEmpTeacherLeave(ArrayList<GeneralUploadResponseModel> body, SyncDownloadUploadModel syncDownloadUploadModel) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int uploadedCount = 0;

            for (GeneralUploadResponseModel model : body) {
                if (model.server_id > 0) { //must not be null or 0 after upload
                    ContentValues values = new ContentValues();
                    values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    values.put(serverId, model.server_id);
                    long i = DB.update(TABLE_EmployeesLeaves, values, ID + " = " + model.device_id, null);
                    if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Teacher Leave Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());

                        uploadedCount++;
                    }
                    AppModel.getInstance().appendLog(context, "Leaves Uploading Successful server id:" + model.server_id);
                } else {
                    AppModel.getInstance().appendLog(context, "Leaves not uploaded");
                    AppModel.getInstance().appendErrorLog(context, "Leaves not uploaded!");
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
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

    public void updateServerIdInTeacherAttendance(ArrayList<GeneralUploadResponseModel> body, SyncDownloadUploadModel syncDownloadUploadModel) throws Exception {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        try {
            DB.beginTransaction();

            int uploadedCount = 0;

            for (GeneralUploadResponseModel model : body) {
                if (model.server_id > 0) { //must not be null or 0 after upload
                    ContentValues values = new ContentValues();
                    values.put(UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    values.put(serverId, model.server_id);
                    long i = DB.update(TABLE_EMPLOYEE_TEACHER_Attendance, values, ID + " = " + model.device_id, null);
                    if (i > 0) {
                        AppModel.getInstance().appendLog(context, "Teacher Attendance Uploaded. Id:" + i + " and uploadedOn:" + AppModel.getInstance().getDate());

                        uploadedCount++;
                    }
                    AppModel.getInstance().appendLog(context, "Teacher Attendance Uploading Successful server id:" + model.server_id);
                } else {
                    AppModel.getInstance().appendLog(context, "Teacher Attendance not uploaded");
                    AppModel.getInstance().appendErrorLog(context, "Teacher Attendance not uploaded!");
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
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

    public List<EmployeeAutoCompleteModel> getAllEmployeeForAutocompleteList(int schoolId, int status) {
        List<EmployeeAutoCompleteModel> empACList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT emp_detail.*,emp_leave.id as leave_id from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + TABLE_EmployeesLeaves + " emp_leave ON emp_leave." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + TABLE_EMPLOYEE_SCHOOL + " emp_School ON emp_School." + Employee_Personal_Detail_ID + " = emp_detail." + ID;

            selectQuery += " WHERE emp_detail." + IsActive + "= 1 AND emp_School." + Employee_SchoolId + " = " + schoolId;

            if (status == 1 || status == 2 || status == 3) {
                selectQuery += " AND emp_leave." + Leave_Status_id + " = " + status + "";
            }

            selectQuery += " GROUP BY emp_detail.Employee_Code ORDER BY emp_detail." + ID;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeAutoCompleteModel eam = new EmployeeAutoCompleteModel();
                    eam.setEmpCode(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    eam.setEmpFirstName(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    eam.setEmpLastName(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                    empACList.add(eam);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return empACList;

    }

    public List<EmployeeAutoCompleteModel> getAllExEmpsForAutocompleteList(int schoolID, int status, boolean isViewSeparation) {
        List<EmployeeAutoCompleteModel> empACList = new ArrayList<>();
        Cursor cursor = null;
        boolean statusSelected = false;
        String selectQuery;
        try {
            if(isViewSeparation){
                selectQuery = "SELECT DISTINCT emp_detail.*, emp_res.*, sep_det.status as sep_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                        " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                        " INNER JOIN " + SEPARATION_DETAIL_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId;

                if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
                    selectQuery += " WHERE sep_det." + Emp_Status + " = '" + status + "'";
                    statusSelected = true;
                }

                if(schoolID > 0){
                    if(statusSelected)
                        selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolID;
                    else
                        selectQuery += " WHERE emp_res." + Emp_SchoolId + " = " + schoolID;
                }

                selectQuery += " GROUP BY  emp_res." + ID + " ORDER BY emp_res." + ID;

            } else {
                selectQuery = "SELECT emp_detail.*, emp_res.*, sep_det.status as sep_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                        " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                        " INNER JOIN " + PENDING_SEPARATION_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId +
                        " WHERE sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId() +
                        " AND sep_det." + Emp_Status + " = 1 ";

                if(schoolID > 0)
                    selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolID;

                selectQuery += " ORDER BY emp_res." + ID;

            }

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeAutoCompleteModel eam = new EmployeeAutoCompleteModel();
                    eam.setEmpCode(cursor.getString(cursor.getColumnIndex(Employee_Code)));
                    eam.setEmpFirstName(cursor.getString(cursor.getColumnIndex(Emp_First_Name)));
                    eam.setEmpLastName(cursor.getString(cursor.getColumnIndex(Emp_Last_Name)));
                    empACList.add(eam);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return empACList;

    }


    public List<EmployeeModel> getEmpDesignationInfo(int schoolId) {
        List<EmployeeModel> empList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT EmployeeDetails.designation, count(EmployeeDetails.designation) as emp_count from EmployeeDetails " +
                    "INNER JOIN EmployeeSchool emp_School ON emp_School.emp_detail_id = EmployeeDetails.id";

            selectQuery += " WHERE emp_School." + Employee_SchoolId + " = " + schoolId;
            selectQuery += " AND (EmployeeDetails." + Emp_LastWorkingDay + " IS NULL OR EmployeeDetails." + Emp_LastWorkingDay + " = '' OR STRFTIME('%Y-%m-%d',EmployeeDetails." + Emp_LastWorkingDay + ") > STRFTIME('%Y-%m-%d',DATE('now')))";
            selectQuery += " AND EmployeeDetails." + Emp_Is_Active + " = 1";
            selectQuery += " GROUP BY EmployeeDetails.designation";
            selectQuery += " ORDER BY emp_count DESC";


            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    EmployeeModel employeeModel = new EmployeeModel();

                    employeeModel.setDesignation(cursor.getString(cursor.getColumnIndex("designation")));
                    employeeModel.setEmpCount(cursor.getString(cursor.getColumnIndex("emp_count")));

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

    public String getAttendanceTakenCountFor30Days(String schoolIds) {
        Cursor cursor = null;
        String selectQuery;
        String AttendanceCount = "0";
        try {

            selectQuery = "select DISTINCT attendance.for_date from EmployeeTeacherAttendance attendance " +
                    " where attendance.SchoolID IN ( " + schoolIds +")"+
                    " and for_date BETWEEN date('now','localtime','-30 days') AND date('now','localtime')" +
                    " and attendanceType_id != 4";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                AttendanceCount = String.valueOf(cursor.getCount());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return AttendanceCount;
    }

    public List<EmployeeAttendanceLast30DaysCountModel> getEmpAttendanceTakenCountForLast30Days() {
        List<EmployeeAttendanceLast30DaysCountModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        EmployeeAttendanceLast30DaysCountModel model = null;
        try {

            selectQuery = " WITH RECURSIVE dates(date) AS (\n" +
                    "  VALUES(date('now', 'localtime'))\n" +
                    "  UNION ALL\n" +
                    "  SELECT date(date, '-1 day')\n" +
                    "  FROM dates\n" +
                    "  limit 30\n" +
                    ")\n" +
                    "select count(EmployeeTeacherAttendance.for_date) as count,date,EmployeeTeacherAttendance.for_date,EmployeeTeacherAttendance.SchoolID from dates\n" +
                    "left join EmployeeTeacherAttendance on EmployeeTeacherAttendance.for_date = dates.date\n" +
                    "group by date " +
                    "order by date desc";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    model = new EmployeeAttendanceLast30DaysCountModel();
                    model.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                    model.setDate(cursor.getString(cursor.getColumnIndex("date")));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex("SchoolID")));
                    model.setForDate(cursor.getString(cursor.getColumnIndex("for_date")));

                    list.add(model);
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return list;
    }

    public List<EmployeePendingAttendanceModel> getEmpPendingAttendanceForToday(String schoolIds) {
        List<EmployeePendingAttendanceModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        EmployeePendingAttendanceModel model = null;
        try {

            selectQuery = "select distinct id,school_name as school, date('now', 'localtime') as Date  from school \n" +
                    "where id IN(@SchoolID)" +
                    "order by id";
            selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    model = new EmployeePendingAttendanceModel();
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex("id")));
                    model.setSchool(cursor.getString(cursor.getColumnIndex("school")));
                    model.setDate(cursor.getString(cursor.getColumnIndex("Date")));

                    list.add(model);
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return list;
    }

    public List<Integer> getEmpSchoolIdsForMarkedAttendance(String date) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        try {

            selectQuery = "select SchoolID from EmployeeTeacherAttendance where for_date = '" + date + "'";

            SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int schoolId = cursor.getInt(cursor.getColumnIndex("SchoolID"));
                    list.add(schoolId);
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return list;
    }

    public int getResignTypeByReason(int resignReasonID) {
        int resignType = 0;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesResignationReason + " WHERE " + ID + " = " +  resignReasonID;

        Cursor cursor = null;
        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                resignType = cursor.getInt(cursor.getColumnIndex(Employee_ResignType_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resignType;
    }

    public void deleteOldPendingApprovals() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "delete from " + SEPARATION_DETAIL_TABLE;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteOldPendingSeparations() {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "delete from " + PENDING_SEPARATION_TABLE;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUserImages(int emp_detail_id) {
        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
        String query = "delete from " + TABLE_UserImages + " WHERE id in(Select id from " + TABLE_UserImages + " where " + Employee_Personal_Detail_ID + " = " + emp_detail_id + ")";
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean ifPendingApprovalsExist(int schoolID, String firstName, String lastName) {
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT emp_detail.*, emp_res.*, sep_det.status as sep_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + PENDING_SEPARATION_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId +
                    " WHERE sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId() +
                    " AND sep_det." + Emp_Status + " = 1 AND emp_res.isActive = 1";

            if(schoolID > 0)
                selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolID;

            if (firstName != null && !firstName.equals("")) {
                selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "'";
                if (lastName != null && !lastName.equals("")) {
                    selectQuery += " AND emp_detail." + Emp_Last_Name + " = '" + lastName + "'";
                }
            }

            selectQuery += " ORDER BY emp_res." + ID;


            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            return cursor.getCount() > 0;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }


    public int getPendingApprovalsCount() {
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT emp_detail.*, emp_res.*, sep_det.status as sep_status from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + PENDING_SEPARATION_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId +
                    " WHERE sep_det." + Approver_userId + " = " + DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId() +
                    " AND sep_det." + Emp_Status + " = 1 AND emp_res.isActive = 1";

//            if(schoolID > 0)
//                selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolID;

//            if (firstName != null && !firstName.equals("")) {
//                selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "'";
//                if (lastName != null && !lastName.equals("")) {
//                    selectQuery += " AND emp_detail." + Emp_Last_Name + " = '" + lastName + "'";
//                }
//            }

            selectQuery += " ORDER BY emp_res." + ID;


            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            return cursor.getCount();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return 0;
    }


    public boolean ifViewSeparationExists(int schoolId, int status, int ResignType, String firstName, String lastName) {
        boolean statusSelected = false;
        boolean schoolSelected = false;
        List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT emp_res.*, sep_det.status as sep_status, MIN(sep_det.app_rank) from " + EMPLOYEE_DETAIL_TABLE + " emp_detail " +
                    " INNER JOIN " + EMPLOYEE_RESIGNATION_TABLE + " emp_res ON emp_res." + Employee_Personal_Detail_ID + " = emp_detail." + ID +
                    " INNER JOIN " + SEPARATION_DETAIL_TABLE + " sep_det ON sep_det." + Employee_Resignation_Id + " = emp_res." + serverId;

            selectQuery += " GROUP BY  emp_res." + ID;

            if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
                selectQuery += " HAVING sep_det." + Emp_Status + " = " + status;
                statusSelected = true;
            }

            if(schoolId > 0){
                if(statusSelected)
                    selectQuery += " AND emp_res." + Emp_SchoolId + " = " + schoolId;
                else
                    selectQuery += " HAVING emp_res." + Emp_SchoolId + " = " + schoolId;
                schoolSelected = true;
            }

            if (firstName != null && !firstName.equals("")) {
                if(statusSelected || schoolSelected)
                    selectQuery += " AND emp_detail." + Emp_First_Name + " = '" + firstName + "'";
                else
                    selectQuery += " HAVING emp_detail." + Emp_First_Name + " = '" + firstName + "'";

                if (lastName != null && !lastName.equals("")) {
                    selectQuery += " AND emp_detail." + Emp_Last_Name + " = '" + lastName + "'";
                }
            }

            selectQuery += " ORDER BY emp_res." + ID;

            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);

            return cursor.getCount() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }

    public void DownloadSeparationImage(String separationAttachment) {
        try {
            if (separationAttachment != null) {
                final File picture = new File(separationAttachment);
                if (!picture.exists()) {
                    String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + separationAttachment.substring(separationAttachment.indexOf("HumanResources"));
                    BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                        @Override
                        public void onError(BasicImageDownloader.ImageError error) {
                            Log.d("imageDownloadError", error.getMessage());
//                            SurveyAppModel.getInstance().appendErrorLog(context, "imageDownloadError " + error.getMessage());
//                                areAllServicesSuccessful = false;
                        }

                        @Override
                        public void onProgressChange(int percent) {

                        }

                        @Override
                        public void onComplete(Bitmap result) {
                            BasicImageDownloader.writeToDisk(picture, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                @Override
                                public void onBitmapSaved() {
                                }

                                @Override
                                public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                }
                            }, Bitmap.CompressFormat.JPEG, true);
                        }
                    });
                    downloader.download(url, false);
                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EmployeeSchoolModel> getSchoolsForLeaveContainsSameEmployee(int employeeDetail_id,String schoolIds) {
        List<EmployeeSchoolModel> schoolModelList = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT emp_s.* FROM EmployeeDetails emp\n" +
                "INNER JOIN EmployeeSchool emp_s on emp_s.emp_detail_id = emp.id\n" +
                "WHERE emp_s.SchoolID IN (@SchoolIds) AND emp.id = @empDetailId";

        selectQuery = selectQuery.replace("@SchoolIds",schoolIds);
        selectQuery = selectQuery.replace("@empDetailId", employeeDetail_id + "");

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeSchoolModel model = new EmployeeSchoolModel();
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
                    schoolModelList.add(model);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return schoolModelList;
    }

    public List<EmployeeLeaveModel> getAllSchoolsLeavesForApproval(int employee_personal_detail_id, String schoolIds, String leave_start_date, String leave_end_date) {
        List<EmployeeLeaveModel> leaveModelList = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_EmployeesLeaves + "\n" +
                "WHERE " + Employee_SchoolId + " IN (" + schoolIds + ") AND " + Employee_Personal_Detail_ID + " = " + employee_personal_detail_id + " \n" +
                "AND " + Leave_Start_Date + "='" + leave_start_date + "' AND " + Leave_End_Date + "='" + leave_end_date + "'";

        try {
            SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EmployeeLeaveModel model = new EmployeeLeaveModel();
                    model.setEmployee_Leave_ID(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setEmployee_Personal_Detail_ID(cursor.getInt(cursor.getColumnIndex(Employee_Personal_Detail_ID)));
                    model.setLeave_Start_Date(cursor.getString(cursor.getColumnIndex(Leave_Start_Date)));
                    model.setLeave_End_Date(cursor.getString(cursor.getColumnIndex(Leave_End_Date)));
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex(Employee_SchoolId)));
                    leaveModelList.add(model);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return leaveModelList;
    }
}

