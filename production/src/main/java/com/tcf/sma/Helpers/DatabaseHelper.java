package com.tcf.sma.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AcademicSession;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppReceipt;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Helpers.DbTables.FeesCollection.CashDeposit;
import com.tcf.sma.Helpers.DbTables.FeesCollection.ErrorLog;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarGlobal;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarSchool;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Helpers.DbTables.FeesCollection.SchoolYear;
import com.tcf.sma.Helpers.DbTables.FeesCollection.SessionInfo;
import com.tcf.sma.Helpers.DbTables.Global.GlobalHelperClass;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.DbTables.NetworkConnection.NetworkConnectionHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AcademicSessionModel;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AreaModel;
import com.tcf.sma.Models.AttendanceLast30DaysCountModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendanceStudentModel;
import com.tcf.sma.Models.BottomFiveSchoolAreaManagerModel;
import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.CampusModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.Fees_Collection.CashDepositModel;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.Models.LocationModel;
import com.tcf.sma.Models.PendingAttendanceModel;
import com.tcf.sma.Models.PromotionDBModel;
import com.tcf.sma.Models.PromotionStudentDBModel;
import com.tcf.sma.Models.RegionModel;
import com.tcf.sma.Models.RetrofitModels.AttendanceStudentUploadModel;
import com.tcf.sma.Models.RetrofitModels.EnrollmentImageUploadModel;
import com.tcf.sma.Models.RetrofitModels.UploadStudentsAuditModel;
import com.tcf.sma.Models.SchoolAuditClassModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentAttendanceReportModel;
import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.Models.StudentImageModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.Models.ViewSSR.ViewSSRTableModel;
import com.tcf.sma.Models.WithdrawalModel;
import com.tcf.sma.Models.WithdrawalReasonModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Zubair Soomro on 12/19/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SIS_DBV2";
    private static final int DATABASE_VERSION = 41;
    public static final String KEY_PASSWORD_CHANGE_ON_DATETIME = "password_change_on";
    public static final String KEY_LAST_UPLOADED_ON = "last_uploaded_on";
    //TABLE_SCHOOL
    public static final String TABLE_SCHOOL = "school";
    public static final String SCHOOL_YEAR_ID = "school_year_id";
    public static final String SCHOOL_NAME = "school_name";
    public static final String SCHOOL_REGION = "region";
    public static final String SCHOOL_DISTRICT = "district";
    public static final String SCHOOL_AREA = "area";
    public static final String SCHOOL_EMIS = "emis";
    public static final String CAMPUS_ID = "campus_id";
    public static final String SCHOOL_START_DATE = "start_date";
    public static final String SCHOOL_END_DATE = "end_date";
    public static final String SCHOOL_ACADEMIC_SESSION = "academic_session";
    public static final String SCHOOL_TARGET_AMOUNT = "target_amount";
    public static final String SCHOOL_ACADEMIC_SESSION_ID = "academic_session_id";
    public static final String SCHOOL_AllowedModule_App = "allowedModule_app";
    //TABLE_LOCATION
    public static final String LOCATION_NAME = "location_name";
    public static final String AREA_ID = "area_id";
    public static final String LOCATION_ID = "location_id";
    public static final String TABLE_LOCATION = "Location";
    //TABLE_CAMPUS
    public static final String CAMPUS_NAME = "campus_name";
    public static final String TABLE_CAMPUS = "Campus";
    public static final String IsActive = "isActive";
    //TABLE_AREA
    public static final String AREA_NAME = "area_name";
    public static final String AREA_MANAGER_ID = "area_manager_id";
    public static final String REGION_ID = "region_id";
    public static final String TABLE_AREA = "Area";
    //TABLE_REGION
    public static final String REGION_NAME = "region_name";
    public static final String REGION_MANAGER_ID = "region_manager_id";
    public static final String TABLE_REGION = "Region";
    //TABLE_ENROLLMENT
    public static final String TABLE_ENROLLMENT = "enrollment";
    public static final String ENROLLMENT_REVIEW_STATUS = "review_status";
    //TABLE_CALENDAR
    public static final String TABLE_CALENDAR = "calendar";
    public static final String CALENDAR_Id = "calendar_id";
    public static final String CALENDAR_Academic_Session_Id = "academic_session_id";
    public static final String CALENDAR_Academic_session = "academic_session";
    public static final String CALENDAR_SessionStartDate = "session_start_date";
    public static final String CALENDAR_SessionEndDate = "session_end_date";
    public static final String CALENDAR_Activity_Id = "activity_id";
    public static final String CALENDAR_Activity_Name = "activity_name";
    public static final String CALENDAR_Activity_Start_Date = "activity_start_date";
    public static final String CALENDAR_Activity_End_Date = "activity_end_date";
    public static final String CALENDAR_Holiday_Type_Name = "holiday_type_name";
    public static final String CALENDAR_Holiday_Type_Id = "holiday_type_id";
    public static final String MODIFIED_ON = "modified_on";
    public static final String MODIFIED_BY = "modified_by";

    private static final String KEY_LAST_PASSWORD_1 = "LastPassword_1";
    private static final String KEY_LAST_PASSWORD_2 = "LastPassword_2";
    private static final String KEY_LAST_PASSWORD_3 = "LastPassword_3";
    private static final String KEY_PASSWORD_CHANGE_ON_LOGIN = "password_change_onlogin";
    private static final String SCHOOL_ID = "school_id";
    public static final String CREATE_TABLE_CALENDAR = "CREATE TABLE " + TABLE_CALENDAR + " ("
            + CALENDAR_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CALENDAR_Academic_Session_Id + " INTEGER,"
            + CALENDAR_Activity_Id + " INTEGER,"
            + CALENDAR_Activity_Name + " VARCHAR,"
            + CALENDAR_Activity_Start_Date + " VARCHAR,"
            + CALENDAR_Activity_End_Date + " VARCHAR,"
            + CALENDAR_Holiday_Type_Id + " INTEGER,"
            + CALENDAR_Holiday_Type_Name + " VARCHAR,"
            + SCHOOL_ID + " INTEGER,"
            + IsActive + " BOOLEAN,"
            + MODIFIED_BY + " INTEGER,"
            + MODIFIED_ON + " VARCHAR"
            + " )";
    private static DatabaseHelper dInstance = null;
    public final String KEY_ID = "id";
    public final String KEY_FCM_TOKEN = "fcmtoken";
    //TABLE_STUDENT
    public final String TABLE_STUDENT = "student";
    public final String STUDENT_NAME = "student_name";
    public final String STUDENT_GENDER = "student_gender";
    public final String STUDENT_GR_NO = "student_gr_no";
    public final String STUDENT_PREVIOUS_STUDENT_ID = "Previous_STUDENT_ID";
    public final String STUDENT_ENROLLMENT_DATE = "enrollment_date";
    public final String STUDENT_DOB = "dob";
    public final String STUDENT_FORM_B = "form_b";
    public final String STUDENT_FATHERS_NAME = "father_name";
    public final String STUDENT_FATHER_OCCUPATION = "father_occupation";
    public final String STUDENT_FATHER_NIC = "father_nic";
    public final String STUDENT_MOTHERS_NAME = "mother_name";
    public final String STUDENT_MOTHERS_OCCUPATION = "mother_occupation";
    public final String STUDENT_MOTHERS_NIC = "mother_nic";
    public final String STUDENT_GUARDIANS_NAME = "guardian_name";
    public final String STUDENT_GUARDIANS_OCCUPATION = "guardian_occupation";
    public final String STUDENT_GUARDIANS_NIC = "guardian_nic";
    public final String STUDENT_PREVIOUS_SCHOOL_NAME = "previous_school_name";
    public final String STUDENT_CLASS_PREVIOUS_SCHOOL = "previous_class_school";
    public final String STUDENT_ADDRESS1 = "address1";
    public final String STUDENT_ADDRESS2 = "address2";
    public final String STUDENT_CONTACT_NUMBERS = "contact_numbers";
    public final String STUDENT_CURRENT_SESSION = "current_session";
    public final String STUDENT_MODIFIED_BY = "modified_by";
    public final String STUDENT_MODIFIED_ON = "modified_on";
    public final String STUDENT_UPLOADED_ON = "uploaded_on";
    public final String STUDENT_SCHOOL_CLASS_ID = "schoolclass_id";
    public final String STUDENT_IS_WITHDRAWN = "is_withdrawl";
    public final String STUDENT_WITHDRAWN_ON = "withdrawn_on";
    public final String STUDENT_WITHDRAWN_REASON_ID = "withdrawal_reason_id";
    public final String STUDENT_IS_ACTIVE = "is_active";
    public final String STUDENT_IS_APPROVED = "is_approved";
    public final String STUDENT_UNAPPROVED_COMMENTS = "un_approved";
    public final String STUDENT_APPROVED_BY = "approved_by";
    public final String STUDENT_APPROVED_ON = "approved_on";
    public final String STUDENT_PICTURE_NAME = "image_file_name";
    public final String STUDENT_PICTURE_UPLOADED_ON = "image_file_uploaded_on";
    public final String STUDENT_Actual_Fees = "Actual_Fees";
    public final String STUDENT_ScholarshipCategory_ID = "ScholarshipCategory_ID";
    public final String STUDENT_Maxallow_fees_tution = "maxallow_fees_tution";
    public final String STUDENT_Maxallow_fees_admission = "maxallow_fees_admission";
    public final String STUDENT_Maxallow_fees_exam = "maxallow_fees_exam";
    public final String STUDENT_Maxallow_fees_books = "maxallow_fees_books";
    public final String STUDENT_Maxallow_copies = "maxallow_fees_copies";
    public final String STUDENT_Maxallow_uniform = "maxallow_fees_uniform";
    public final String STUDENT_Maxallow_others = "maxallow_fees_others";
    public final String STUDENT_SERVER_ID = "server_id";
    public final String STUDENT_IS_ORPHAN = "is_orphan";
    public final String STUDENT_IS_DISABLED = "is_disabled";
    public final String STUDENT_EMAIL = "student_email";
    public final String STUDENT_RELIGION = "religion";
    public final String STUDENT_NATIONALITY = "nationality";
    public final String STUDENT_ELECTIVE_SUBJECT_ID = "elective_subject_id";
    public final String STUDENT_DEATH_CERT_IMAGE = "death_cert_image";
    public final String STUDENT_MEDICAL_CERT_IMAGE = "medical_cert_image";
    public final String STUDENT_BFORM_IMAGE = "bform_image";
    public final String STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON = "death_cert_image_file_uploaded_on";
    public final String STUDENT_BFORM_IMAGE_UPLOADED_ON = "bform_image_file_uploaded_on";
    public final String STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON = "medical_cert_image_file_uploaded_on";
    public final String STUDENT_PROMOTION_COMMENTS = "promotion_comments";
    public final String STUDENT_PROMOTION_STATUS = "promotion_status";
    //TABLE_STUDENT_DOCUMENT
    public final String TABLE_STUDENT_DOCUMENT = "student_document";
    public final String DOCUMENT_STUDENT_ID = "id";
    public final String DOCUMENT_FILENAME = "filename";
    public final String DOCUMENT_LAST_SYNC_TIME = "last_sync_time";
    public final String ENROLLMENT_SERVER_ID = "server_id";
    public final String ENROLLMENT_GR_NO = "gr_no";
    public final String ENROLLMENT_SCHOOL_ID = "school_id";
    public final String ENROLLMENT_CREATED_BY = "created_by";
    public final String ENROLLMENT_CREATED_ON = "created_on";
    public final String ENROLLMENT_REVIEW_COMMENTS = "review_comments";
    public final String ENROLLMENT_REVIEW_COMPLETED_ON = "review_completed_on";
    public final String ENROLLMENT_DATA_DOWNLOADED_ON = "data_downloaded_on";
    public final String ENROLLMENT_UPLOADED_ON = "uploaded_on";
    public final String ENROLLMENT_NAME = "name";
    public final String ENROLLMENT_CLASS = "class_id";
    public final String ENROLLMENT_SECTION = "section_id";
    public final String ENROLLMENT_STUDENT_ID = "student_id";
    public final String ENROLLMENT_CLASS_SECTION_ID = "class_section_id";
    public final String ENROLLMENT_GENDER = "student_gender";
    public final String ENROLLMENT_MONTHLY_FEE = "monthly_fee";
    public final String ENROLLMENT_MODIFIED_ON = "modified_on";
    public final String ENROLLMENT_MODIFIED_BY = "modified_by";
    //TABLE_ENROLLMENT_IMAGE
    public final String TABLE_ENROLLMENT_IMAGE = "enrollment_image";
    public final String ENROLLMENT_ID = "enrollment_id";
    public final String ENROLLMENT_IMAGE_FILENAME = "filename";
    public final String ENROLLMENT_IMAGE_UPLOADED_ON = "uploaded_on";
    public final String ENROLLMENT_IMAGE_REVIEW_STATUS = "review_status";
    public final String ENROLLMENT_FILE_TYPE = "filetype";
    //TABLE_CLASS
    public final String TABLE_CLASS = "class";
    public final String CLASS_NAME = "name";
    public final String CLASS_RANK = "rank";
    public final String CLASS_MODIFIED_ON = "modified_on";
    //TABLE_SCHOOL_CLASS
    public final String TABLE_SCHOOL_CLASS = "school_class";
    public final String SCHOOL_CLASS_SCHOOLID = "school_id";
    public final String SCHOOL_CLASS_CLASSID = "class_id";
    public final String SCHOOL_CLASS_SECTIONID = "section_id";
    public final String SCHOOL_CLASS_IS_ACTIVE = "is_active";
    public final String MAX_CAPACITY = "max_capacity";
    public final String CAPACITY = "capacity";
    public final String SCHOOL_CLASS_MODIFIED_ON = "modified_on";
    //TABLE_SECTION
    public final String TABLE_SECTION = "section";
    public final String SECTION_NAME = "name";
    public final String SECTION_MODIFIED_ON = "modified_on";
    //TABLE_ATTENDANCE
    public final String TABLE_ATTENDANCE = "attendance";
    public final String ATTENDANCE_SERVER_ID = "server_id";
    public final String ATTENDANCE_FOR_DATE = "for_date";
    public final String ATTENDANCE_CREATED_BY = "created_by";
    public final String ATTENDANCE_CREATED_ON = "created_on";
    public final String ATTENDANCE_UPLOADED_ON = "uploaded_on";
    public final String ATTENDANCE_SCHOOL_CLASS_ID = "school_class_id";
    public final String ATTENDANCE_MODIFIED_ON = "modified_on";
    public final String ATTENDANCE_MODIFIED_BY = "modified_by";
    //TABLE_ATTENDANCE_STUDENT
    public final String TABLE_ATTENDANCE_STUDENT = "student_attendance";
    public final String ATTENDANCE_ID = "attendance_id";
    public final String ATTENDANCE_STUDENT_ID = "student_id";
    public final String ATTENDANCE_IS_ABSENT = "isabsent";
    public final String ATTENDANCE_REASON = "reason";
    //TABLE_PROMOTION
    public final String TABLE_PROMOTION = "promotion";
    public final String PROMOTION_CREATED_BY = "created_by";
    public final String PROMOTION_CREATED_ON = "created_on";
    public final String PROMOTION_UPLOADED_ON = "uploaded_on";
    public final String PROMOTION_SCHOOL_CLASS_ID = "school_class_id";
    public final String PROMOTION_MODIFIED_ON = "modified_on";
    //TABLE_PROMOTION_STUDENT
    public final String TABLE_PROMOTION_STUDENT = "promotion_student";
    public final String PROMOTION_ID = "promotion_id";
    public final String PROMOTION_STUDENT_STUDENT_ID = "student_id";
    public final String PROMOTION_STUDENT_NEW_SCHOOLCLASS_ID = "new_schoolclass_id";
    //TABLE_WITHDRAWAL_REASON
    public final String TABLE_WITHDRAWAL_REASON = "withdrawal_reason";
    public final String WITHDRAWAL_REASON_NAME = "name";
    public final String WITHDRAWAL_REASON_MODIFIED_ON = "modified_on";
    //TABLE_WITHDRAWAL
    public final String TABLE_WITHDRAWAL = "withdrawal";
    public final String WITHDRAWAL_STUDENT_ID = "student_id";
    public final String WITHDRAWAL_REASON_ID = "reason_id";
    public final String WITHDRAWAL_CREATED_BY = "created_by";
    public final String WITHDRAWAL_CREATED_ON = "created_on";
    public final String WITHDRAWAL_UPLOADED_ON = "uploaded_on";
    public final String WITHDRAWAL_MODIFIED_ON = "modified_on";
    public final String WITHDRAWAL_SCHOOL_ID = "school_id";
    public final String TABLE_SCHOOL_AUDIT = "school_audit";
    public final String SCHOOL_AUDIT_VISIT_DATE = "visit_date";
    public final String SCHOOL_AUDIT_APPROVED_BY = "approed_by";
    public final String SCHOOL_AUDIT_SCHOOL_ID = "school_id";
    public final String SCHOOL_AUDIT_IS_APPROVED = "is_approved";
    public final String SCHOOL_AUDIT_REMARKS = "remarks";
    public final String SCHOOL_AUDIT_CLASSES_COUNT = "classes_count";
    public final String SCHOOL_AUDIT_STUDENT_COUNT = "students_count";
    public final String SCHOOL_AUDIT_UPLOADED_ON = "uploaded_on";
    public final String SCHOOL_AUDIT_SERVER_ID = "server_id";
    public final String TABLE_SCHOOL_AUDIT_CLASS = "School_class_audit";
    public final String SCHOOL_AUDIT_CLASS_AUDIT_ID = "audit_id";
    public final String SCHOOL_AUDIT_CLASS_ID = "class_id";
    public final String SCHOOL_AUDIT_CLASS_COUNT = "SCHOOL_AUDIT_CLASS_COUNT";
    public final String SCHOOL_AUDIT_CLASS_ISAPROVED_BY = "approved_by";
    public final String SCHOOL_AUDIT_CLASS_REMARKS = "remarks";
    public final String USERNAMES_USERNAME = "username";
    // TABLE_USERS
    private final String TABLE_USERS = "user";
    private final String KEY_FIRST_NAME = "first_name";
    private final String KEY_LAST_NAME = "last_name";
    private final String KEY_EMAIL = "email";
    private final String KEY_STATUS = "status";
    private final String KEY_DESIGNATION = "designation";
    private final String KEY_ROLE = "role";
    private final String KEY_ROLE_ID = "role_Id";
    private final String KEY_DEPARTMENT_ID = "department_Id";
    private final String KEY_DEFAULT_SCHOOL_ID = "default_school_id";
    private final String KEY_USERNAME = "username";


    //TABLE_SCHOOL_AUDIT
    private final String KEY_LAST_PASSWORD = "last_password";
    private final String KEY_LAST_LOGIN_TIME = "last_login";
    private final String KEY_LAST_METADATA_SYNC_ON = "last_metadata_sync";
    private final String KEY_SESSION_TOKEN = "token";
    private final String SCHOOL_TYPE = "school_type";
    private final String SCHOOL_PROVINCE_ID = "province_id";
    private final String SCHOOL_PROVINCE_NAME = "province_name";
    private final String SCHOOL_SHIFT = "shift";
    private final String PRINCIPAL_FIRST_NAME = "principal_first_name";
    private final String PRINCIPAL_LAST_NAME = "principal_last_name";
    private final String CREATE_TABLE_SCHOOL = "CREATE TABLE " + TABLE_SCHOOL + " ("
            + SCHOOL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID + " INTEGER,"
            + SCHOOL_NAME + " VARCHAR,"
            + SCHOOL_REGION + " VARCHAR,"
            + SCHOOL_DISTRICT + " VARCHAR,"
            + SCHOOL_AREA + " VARCHAR,"
            + SCHOOL_EMIS + " VARCHAR,"
            + CAMPUS_ID + " INTEGER,"
            + SCHOOL_YEAR_ID + " INTEGER,"
            + SCHOOL_START_DATE + " VARCHAR,"
            + SCHOOL_END_DATE + " VARCHAR,"
            + SCHOOL_TARGET_AMOUNT + " REAL,"
            + SCHOOL_AllowedModule_App + " VARCHAR,"
            + SCHOOL_ACADEMIC_SESSION_ID + " INTEGER,"
            + SCHOOL_ACADEMIC_SESSION + " VARCHAR,"
            + SCHOOL_TYPE + " TEXT,"
            + SCHOOL_SHIFT + " TEXT,"
            + PRINCIPAL_FIRST_NAME + " TEXT,"
            + PRINCIPAL_LAST_NAME + " TEXT,"
            + SCHOOL_PROVINCE_ID + " INTEGER,"
            + SCHOOL_PROVINCE_NAME + " TEXT"
            + " )";
    private final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + AREA_ID + " INTEGER,"
            + LOCATION_NAME + " VARCHAR,"
            + LOCATION_ID + " INTEGER,"
            + IsActive + " BOOLEAN,"
            + MODIFIED_ON + " VARCHAR"
            + " )";
    private final String CREATE_TABLE_CAMPUS = "CREATE TABLE " + TABLE_CAMPUS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CAMPUS_ID + " INTEGER,"
            + CAMPUS_NAME + " VARCHAR,"
            + LOCATION_ID + " INTEGER,"
            + IsActive + " BOOLEAN,"
            + MODIFIED_ON + " VARCHAR"
            + " )";
    private final String CREATE_TABLE_AREA = "CREATE TABLE " + TABLE_AREA + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + AREA_ID + " INTEGER,"
            + REGION_ID + " INTEGER,"
            + AREA_NAME + " VARCHAR,"
            + AREA_MANAGER_ID + " INTEGER,"
            + IsActive + " BOOLEAN,"
            + MODIFIED_ON + " VARCHAR"
            + " )";
    private final String CREATE_TABLE_REGION = "CREATE TABLE " + TABLE_REGION + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + REGION_ID + " INTEGER,"
            + REGION_NAME + " VARCHAR,"
            + REGION_MANAGER_ID + " INTEGER,"
            + IsActive + " BOOLEAN,"
            + MODIFIED_ON + " VARCHAR"
            + " )";
    //TABLE_USER_SCHOOL
    private final String TABLE_USER_SCHOOL = "user_school";
    private final String CREATE_TABLE_USER_SCHOOL = "CREATE TABLE " + TABLE_USER_SCHOOL + " ("
            + SCHOOL_ID + " INTEGER PRIMARY KEY"
            + " )";

    //TABLE_SCHOOL_AUDIT_CLASS
    private final String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_STUDENT + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + STUDENT_NAME + " VARCHAR,"
            + STUDENT_SERVER_ID + " INTEGER,"
            + STUDENT_GENDER + " VARCHAR,"
            + STUDENT_GR_NO + " VARCHAR NOT NULL,"
            + STUDENT_ENROLLMENT_DATE + " VARCHAR,"
            + STUDENT_DOB + " VARCHAR,"
            + STUDENT_FORM_B + " VARCHAR,"
            + STUDENT_FATHERS_NAME + " VARCHAR,"
            + STUDENT_FATHER_OCCUPATION + " VARCHAR,"
            + STUDENT_FATHER_NIC + " VARCHAR,"
            + STUDENT_MOTHERS_NAME + " VARCHAR,"
            + STUDENT_MOTHERS_OCCUPATION + " VARCHAR,"
            + STUDENT_MOTHERS_NIC + " VARCHAR,"
            + STUDENT_GUARDIANS_NAME + " VARCHAR,"
            + STUDENT_GUARDIANS_OCCUPATION + " VARCHAR,"
            + STUDENT_GUARDIANS_NIC + " VARCHAR,"
            + STUDENT_PREVIOUS_SCHOOL_NAME + " VARCHAR,"
            + STUDENT_CLASS_PREVIOUS_SCHOOL + " VARCHAR,"
            + STUDENT_ADDRESS1 + " VARCHAR,"
            + STUDENT_ADDRESS2 + " VARCHAR,"
            + STUDENT_CONTACT_NUMBERS + " VARCHAR,"
            + STUDENT_CURRENT_SESSION + " INTEGER,"
            + STUDENT_MODIFIED_BY + " INTEGER,"
            + STUDENT_MODIFIED_ON + " VARCHAR,"
            + STUDENT_UPLOADED_ON + " VARCHAR,"
            + STUDENT_SCHOOL_CLASS_ID + " INTEGER NOT NULL,"
            + STUDENT_IS_WITHDRAWN + " BOOLEAN,"
            + STUDENT_WITHDRAWN_ON + " VARCHAR,"
            + STUDENT_WITHDRAWN_REASON_ID + " INTEGER,"
            + STUDENT_IS_ACTIVE + " BOOLEAN,"
            + STUDENT_IS_APPROVED + " BOOLEAN,"
            + STUDENT_UNAPPROVED_COMMENTS + " VARCHAR, "
            + STUDENT_APPROVED_BY + " INTEGER, "
            + STUDENT_APPROVED_ON + " VARCHAR, "
            + STUDENT_PICTURE_NAME + " VARCHAR, "
            + STUDENT_PICTURE_UPLOADED_ON + " VARCHAR, "
            + STUDENT_PREVIOUS_STUDENT_ID + " INTEGER, "
            + STUDENT_Actual_Fees + " REAL, "
            + STUDENT_ScholarshipCategory_ID + " INTEGER, "
            + STUDENT_Maxallow_fees_tution + " INTEGER, "
            + STUDENT_Maxallow_fees_admission + " INTEGER, "
            + STUDENT_Maxallow_uniform + " INTEGER, "
            + STUDENT_Maxallow_copies + " INTEGER, "
            + STUDENT_Maxallow_fees_exam + " INTEGER, "
            + STUDENT_Maxallow_others + " INTEGER, "
            + STUDENT_Maxallow_fees_books + " INTEGER, "
            + STUDENT_RELIGION + " TEXT, "
            + STUDENT_NATIONALITY + " TEXT, "
            + STUDENT_IS_ORPHAN + " TEXT, "
            + STUDENT_IS_DISABLED + " TEXT, "
            + STUDENT_EMAIL + " TEXT, "
            + STUDENT_ELECTIVE_SUBJECT_ID + " INTEGER, "
            + STUDENT_DEATH_CERT_IMAGE + " TEXT, "
            + STUDENT_MEDICAL_CERT_IMAGE + " TEXT, "
            + STUDENT_BFORM_IMAGE + " TEXT, "
            + STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON + " TEXT, "
            + STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON + " TEXT, "
            + STUDENT_BFORM_IMAGE_UPLOADED_ON + " TEXT, "
            + STUDENT_PROMOTION_COMMENTS + " TEXT, "
            + STUDENT_PROMOTION_STATUS + " TEXT, "
            + "UNIQUE (" + STUDENT_GR_NO + ", " + STUDENT_SCHOOL_CLASS_ID + ")"
            + " )";
    private final String CREATE_TABLE_STUDENT_DOCUMENT = "CREATE TABLE " + TABLE_STUDENT_DOCUMENT + " ("
            + DOCUMENT_STUDENT_ID + " INTEGER,"
            + DOCUMENT_FILENAME + " VARCHAR,"
            + DOCUMENT_LAST_SYNC_TIME + " VARCHAR,"
            + "FOREIGN KEY(" + DOCUMENT_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + "(" + KEY_ID + ")"
            + " )";
    private final String CREATE_TABLE_ENROLLMENT = "CREATE TABLE " + TABLE_ENROLLMENT + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ENROLLMENT_SERVER_ID + " INTEGER,"
            + ENROLLMENT_GR_NO + " INTEGER NOT NULL,"
            + ENROLLMENT_NAME + " VARCHAR,"
            + ENROLLMENT_GENDER + " VARCHAR,"
            + ENROLLMENT_CLASS + " INTEGER,"
            + ENROLLMENT_SECTION + " INTEGER,"
            + ENROLLMENT_CLASS_SECTION_ID + " INTEGER,"
            + ENROLLMENT_MONTHLY_FEE + " INTEGER,"
            + ENROLLMENT_SCHOOL_ID + " INTEGER NOT NULL,"
            + ENROLLMENT_CREATED_BY + " INTEGER,"
            + ENROLLMENT_STUDENT_ID + " INTEGER,"
            + ENROLLMENT_CREATED_ON + " VARCHAR,"
            + ENROLLMENT_REVIEW_STATUS + " VARCHAR,"
            + ENROLLMENT_REVIEW_COMMENTS + " VARCHAR,"
            + ENROLLMENT_REVIEW_COMPLETED_ON + " VARCHAR,"
            + ENROLLMENT_DATA_DOWNLOADED_ON + " VARCHAR,"
            + ENROLLMENT_MODIFIED_ON + " VARCHAR,"
            + ENROLLMENT_MODIFIED_BY + " INTEGER,"
            + ENROLLMENT_UPLOADED_ON + " VARCHAR "
            + " )";
    private final String CREATE_TABLE_ENROLLMENT_IMAGE = "CREATE TABLE " + TABLE_ENROLLMENT_IMAGE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ENROLLMENT_ID + " INTEGER,"
            + ENROLLMENT_IMAGE_FILENAME + " VARCHAR,"
            + ENROLLMENT_IMAGE_UPLOADED_ON + " VARCHAR,"
            + ENROLLMENT_IMAGE_REVIEW_STATUS + " VARCHAR,"
            + ENROLLMENT_FILE_TYPE + " VARCHAR,"
            + " FOREIGN KEY (" + ENROLLMENT_ID + ") REFERENCES " + TABLE_ENROLLMENT + " (" + KEY_ID + ")"
            + " )";
    private final String CREATE_TABLE_CLASS = "CREATE TABLE " + TABLE_CLASS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + CLASS_NAME + " VARCHAR,"
            + CLASS_RANK + " INTEGER,"
            + CLASS_MODIFIED_ON + " TEXT"
            + " )";
    private final String CREATE_TABLE_SCHOOL_CLASS = "CREATE TABLE " + TABLE_SCHOOL_CLASS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + SCHOOL_CLASS_SCHOOLID + " INTEGER,"
            + SCHOOL_CLASS_CLASSID + " INTEGER,"
            + SCHOOL_CLASS_SECTIONID + " INTEGER,"
            + MAX_CAPACITY + " INTEGER,"
            + CAPACITY + " INTEGER,"
            + SCHOOL_CLASS_IS_ACTIVE + " BOOLEAN,"
            + SCHOOL_CLASS_MODIFIED_ON + " TEXT"
            + " )";
    private final String CREATE_TABLE_SECTION = "CREATE TABLE " + TABLE_SECTION + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + SECTION_NAME + " VARCHAR,"
            + SECTION_MODIFIED_ON + " TEXT"
            + " )";
    private final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_ATTENDANCE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ATTENDANCE_SERVER_ID + " INTEGER,"
            + ATTENDANCE_FOR_DATE + " VARCHAR,"
            + ATTENDANCE_CREATED_BY + " INTEGER,"
            + ATTENDANCE_CREATED_ON + " VARCHAR,"
            + ATTENDANCE_UPLOADED_ON + " VARCHAR,"
            + ATTENDANCE_SCHOOL_CLASS_ID + " INTEGER,"
            + ATTENDANCE_MODIFIED_ON + " VARCHAR,"
            + ATTENDANCE_MODIFIED_BY + " INTEGER"
            + " )";
    private final String CREATE_TABLE_ATTENDANCE_STUDENT = "CREATE TABLE " + TABLE_ATTENDANCE_STUDENT + " ("
            + ATTENDANCE_ID + " INTEGER,"
            + ATTENDANCE_STUDENT_ID + " INTEGER,"
            + ATTENDANCE_IS_ABSENT + " INTEGER,"
            + ATTENDANCE_REASON + " VARCHAR,"
            + " FOREIGN KEY (" + ATTENDANCE_ID + ") REFERENCES " + TABLE_ATTENDANCE + " (" + KEY_ID + "),"
            + " FOREIGN KEY (" + ATTENDANCE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + " (" + KEY_ID + ")"
            + " )";
    private final String CREATE_TABLE_PROMOTION = "CREATE TABLE " + TABLE_PROMOTION + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PROMOTION_CREATED_BY + " INTEGER,"
            + PROMOTION_CREATED_ON + " VARCHAR,"
            + PROMOTION_UPLOADED_ON + " VARCHAR,"
            + PROMOTION_SCHOOL_CLASS_ID + " INTEGER,"
            + PROMOTION_MODIFIED_ON + " TEXT"
            + " )";
    private final String CREATE_TABLE_PROMOTION_STUDENT = "CREATE TABLE " + TABLE_PROMOTION_STUDENT + " ("
            + PROMOTION_ID + " INTEGER,"
            + PROMOTION_STUDENT_STUDENT_ID + " INTEGER,"
            + PROMOTION_STUDENT_NEW_SCHOOLCLASS_ID + " INTEGER,"
            + " FOREIGN KEY (" + PROMOTION_ID + ") REFERENCES " + TABLE_PROMOTION + " (" + KEY_ID + "),"
            + " FOREIGN KEY (" + PROMOTION_STUDENT_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + " (" + KEY_ID + ")"
            + " )";
    private final String CREATE_TABLE_WITHDRAWAL_REASON = "CREATE TABLE " + TABLE_WITHDRAWAL_REASON + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + WITHDRAWAL_REASON_NAME + " VARCHAR,"
            + WITHDRAWAL_REASON_MODIFIED_ON + " TEXT"
            + " )";
    private final String CREATE_TABLE_WITHDRAWAL = "CREATE TABLE " + TABLE_WITHDRAWAL + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + WITHDRAWAL_STUDENT_ID + " INTEGER,"
            + WITHDRAWAL_REASON_ID + " INTEGER,"
            + WITHDRAWAL_CREATED_BY + " INTEGER,"
            + WITHDRAWAL_CREATED_ON + " VARCHAR,"
            + WITHDRAWAL_UPLOADED_ON + " VARCHAR,"
            + WITHDRAWAL_MODIFIED_ON + " TEXT,"
            + WITHDRAWAL_SCHOOL_ID + " INTEGER,"
            + " FOREIGN KEY (" + WITHDRAWAL_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + " (" + KEY_ID + "),"
            + " FOREIGN KEY (" + WITHDRAWAL_REASON_ID + ") REFERENCES " + TABLE_WITHDRAWAL_REASON + " (" + KEY_ID + ")"
            + " )";
    private final String CREATE_TABLE_SCHOOL_AUDIT = "CREATE TABLE " + TABLE_SCHOOL_AUDIT + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SCHOOL_AUDIT_VISIT_DATE + " VARCHAR,"
            + SCHOOL_AUDIT_APPROVED_BY + " INTEGER,"
            + SCHOOL_AUDIT_SCHOOL_ID + " INTEGER,"
            + SCHOOL_AUDIT_IS_APPROVED + " BOOLEAN,"
            + SCHOOL_AUDIT_REMARKS + " VARCHAR,"
            + SCHOOL_AUDIT_CLASSES_COUNT + " INTEGER,"
            + SCHOOL_AUDIT_STUDENT_COUNT + " INTEGER,"
            + SCHOOL_AUDIT_UPLOADED_ON + " VARCHAR,"
            + SCHOOL_AUDIT_SERVER_ID + " INTEGER,"
            + " FOREIGN KEY (" + SCHOOL_AUDIT_SCHOOL_ID + ") REFERENCES " + TABLE_SCHOOL + " (" + KEY_ID + ")"
            + " )";
    private final String CREATE_TABLE_CLASS_AUDIT = "CREATE TABLE " + TABLE_SCHOOL_AUDIT_CLASS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SCHOOL_AUDIT_CLASS_AUDIT_ID + " INTEGER,"
            + SCHOOL_AUDIT_CLASS_ID + " INTEGER,"
            + SCHOOL_AUDIT_CLASS_COUNT + " INTEGER,"
            + SCHOOL_AUDIT_CLASS_ISAPROVED_BY + " VARCHAR,"
            + SCHOOL_AUDIT_CLASS_REMARKS + " VARCHAR,"
            + " FOREIGN KEY (" + SCHOOL_AUDIT_CLASS_AUDIT_ID + ") REFERENCES " + TABLE_SCHOOL_AUDIT + " (" + KEY_ID + "),"
            + " FOREIGN KEY (" + SCHOOL_AUDIT_CLASS_ID + ") REFERENCES " + TABLE_CLASS + " (" + KEY_ID + ")"
            + " )";
    private final String TABLE_USERNAMES = "search_usernames";
    private final String CREATE_TABLE_USERNAMES = "CREATE TABLE " + TABLE_USERNAMES + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USERNAMES_USERNAME + " VARCHAR"
            + " )";
    private final String GET_PREV_MONTH_SSR = "Select count(s1.enrollment_date) AS 'count', \n" +
            "upper(s1.student_gender) as Gender\n" +
            "FROM student s1\n" +
            "inner join school_class on s1.schoolclass_id = school_class.id \n" +
            "and school_class.school_id = @SchoolId\n" +
            "WHERE  s1.withdrawn_on is null\tand\n" +
            "s1.enrollment_date >= datetime('now','start of month','-1 month')\n" +
            "and s1.enrollment_date <= datetime('now','start of month','-1 day')\n" +
            "GROUP BY s1.student_gender COLLATE NOCASE";
    private final String GET_NEW_ADMISSION = "Select count(s1.enrollment_date) AS 'count', \n" +
            "upper(s1.student_gender) as Gender \n" +
            "FROM student s1 \n" +
            "inner join school_class on s1.schoolclass_id = school_class.id and school_class.school_id = @SchoolId\n" +
            "WHERE s1.enrollment_date >= '@fromDate 00:00:00 AM' and s1.enrollment_date <= '@toDate 23:59:59 PM' \n" +
            "GROUP BY s1.student_gender COLLATE NOCASE";
    private final String GET_WITHDRAWALS = "Select count(s1.withdrawn_on) AS 'count', \n" +
            "upper(s1.student_gender) as Gender\n" +
            "FROM student s1\n" +
            "inner join school_class on s1.schoolclass_id = school_class.id and school_class.school_id = @SchoolId\n" +
            "WHERE  s1.is_withdrawl = 1 and s1.withdrawn_on >= '@fromDate 00:00:00 AM' and s1.withdrawn_on <= '@toDate 23:59:59 AM' \n" +
            "GROUP BY s1.student_gender COLLATE NOCASE";
    private final String GET_GRADUATES = "Select count(s1.withdrawn_on) AS 'count', \n" +
            "upper(s1.student_gender) as Gender\n" +
            "FROM student s1\n" +
            "inner join school_class on s1.schoolclass_id = school_class.id \n" +
            "and school_class.school_id = @SchoolId\n" +
            "WHERE  s1.withdrawal_reason_id = 14\tand\n" +
            "s1.withdrawn_on >= '@fromDate 00:00:00 AM' \n" +
            "and s1.withdrawn_on <= '@toDate 23:59:59 AM' \n" +
            "GROUP BY s1.student_gender COLLATE NOCASE";
    private final String GET_GRAPH_WITHDRAWALS = "Select strftime('%Y-%m', datetime(s1.withdrawn_on)) as rep_month,count(s1.withdrawn_on) AS 'count', \n" +
            "    upper(s1.student_gender) as Gender\n" +
            "    FROM student s1\n" +
            "    inner join school_class on s1.schoolclass_id = school_class.id \n" +
            "    and school_class.school_id = @SchoolId\n" +
            "    WHERE  s1.is_withdrawl = 1\tand\n" +
            "    s1.withdrawn_on >= datetime('@fromDate')\n" +
            "  and s1.withdrawn_on <= datetime('@toDate')\n" +
            "group by rep_month,Gender";
    private final String GET_GRAPH_NEW_ADMISSION = "Select strftime('%Y-%m', datetime(s1.enrollment_date)) as rep_month,count(s1.enrollment_date) AS 'count', \n" +
            "    upper(s1.student_gender) as Gender\n" +
            "    FROM student s1\n" +
            "    inner join school_class on s1.schoolclass_id = school_class.id \n" +
            "    and school_class.school_id = @SchoolId\n" +
            "    WHERE  s1.is_withdrawl = 0\tand s1.withdrawn_on is null and\n" +
            "    s1.enrollment_date >= datetime('@fromDate')\n" +
            "    and s1.enrollment_date <= datetime('@toDate')\n" +
            " group by rep_month,Gender\n";
    private final String GET_GRAPH_TOTAL_SSR = "Select strftime('%Y-%m', datetime(s1.enrollment_date)) as rep_month,count(s1.enrollment_date) AS 'count', \n" +
            "    upper(s1.student_gender) as Gender\n" +
            "    FROM student s1\n" +
            "    inner join school_class on s1.schoolclass_id = school_class.id \n" +
            "    and school_class.school_id = @SchoolId\n" +
            "    WHERE \n" +
            "    (s1.enrollment_date >= datetime('@fromDate')\n" +
            "    and s1.enrollment_date <= datetime('@toDate'))\n" +
            "\tgroup by rep_month,Gender\t\n" +
            "\t\n" +
            "\t\n" +
            "\t\n" +
            "\t\n";
    String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_FIRST_NAME + " VARCHAR,"
            + KEY_LAST_NAME + " VARCHAR,"
            + KEY_EMAIL + " VARCHAR,"
            + KEY_STATUS + " VARCHAR,"
            + KEY_DESIGNATION + " VARCHAR,"
            + KEY_ROLE + " VARCHAR,"
            + KEY_ROLE_ID + " INTEGER,"
            + KEY_DEFAULT_SCHOOL_ID + " INTEGER,"
            + KEY_DEPARTMENT_ID + " INTEGER,"
            + KEY_USERNAME + " VARCHAR,"
            + KEY_LAST_PASSWORD + " VARCHAR,"
            + KEY_LAST_LOGIN_TIME + " VARCHAR,"
            + KEY_LAST_METADATA_SYNC_ON + " VARCHAR,"
            + KEY_SESSION_TOKEN + " VARCHAR,"
            + KEY_FCM_TOKEN + " VARCHAR,"
            + KEY_LAST_PASSWORD_1 + " VARCHAR,"
            + KEY_LAST_PASSWORD_2 + " VARCHAR,"
            + KEY_LAST_PASSWORD_3 + " VARCHAR,"
            + KEY_PASSWORD_CHANGE_ON_DATETIME + " VARCHAR,"
            + KEY_PASSWORD_CHANGE_ON_LOGIN + " INTEGER,"
            + KEY_LAST_UPLOADED_ON + " VARCHAR"
            + ")";
    private Context context;
    private SQLiteDatabase syncronizedDb;

    private DatabaseHelper(Context context) {
        super(new DatabaseContext(context), DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Retrieves a thread-safe instance of the singleton object {@link DatabaseHelper} and opens the database
     * with writing permissions.
     *
     * @param context the context to set.
     * @return the singleton instance.
     */

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dInstance == null) {
            dInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return dInstance;

    }

    public SQLiteDatabase getDB() {
        synchronized (this) {
            try {
                if (syncronizedDb == null || !syncronizedDb.isOpen() || syncronizedDb.isReadOnly())
                    syncronizedDb = this.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return syncronizedDb;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_USER_SCHOOL);
            db.execSQL(CREATE_TABLE_STUDENT);
            db.execSQL(CREATE_TABLE_STUDENT_DOCUMENT);
            db.execSQL(CREATE_TABLE_SCHOOL);
            db.execSQL(CREATE_TABLE_SCHOOL_CLASS);
            db.execSQL(CREATE_TABLE_ENROLLMENT);
            db.execSQL(CREATE_TABLE_ENROLLMENT_IMAGE);
            db.execSQL(CREATE_TABLE_CLASS);
            db.execSQL(CREATE_TABLE_SECTION);
            db.execSQL(CREATE_TABLE_ATTENDANCE);
            db.execSQL(CREATE_TABLE_ATTENDANCE_STUDENT);
            db.execSQL(CREATE_TABLE_PROMOTION);
            db.execSQL(CREATE_TABLE_PROMOTION_STUDENT);
            db.execSQL(CREATE_TABLE_WITHDRAWAL_REASON);
            db.execSQL(CREATE_TABLE_WITHDRAWAL);
            db.execSQL(CREATE_TABLE_SCHOOL_AUDIT);
            db.execSQL(CREATE_TABLE_CLASS_AUDIT);
            db.execSQL(CREATE_TABLE_USERNAMES);
            db.execSQL(CREATE_TABLE_AREA);
            db.execSQL(CREATE_TABLE_CAMPUS);
            db.execSQL(CREATE_TABLE_REGION);
            db.execSQL(CREATE_TABLE_LOCATION);


            //Fees Collection

            //Added by Haseeb
            db.execSQL(Scholarship_Category.CREATE_SCHOLARHSIP_CAT_TABLE);
            db.execSQL(AppInvoice.CREATE_APP_INVOICE_TABLE);
            db.execSQL(AppReceipt.CREATE_APP_RECEIPT_TABLE);

            db.execSQL(CashDeposit.CREATE_CASH_DEPOSIT_TABLE);
            db.execSQL(HolidayCalendarGlobal.CREATE_HOLIDAY_CALENDAR_GLOBAL_TABLE);
            db.execSQL(HolidayCalendarSchool.CREATE_HOLIDAY_CALENDAR_SCHOOL_TABLE);
            db.execSQL(AcademicSession.CREATE_ACADEMIC_SESSION_TABLE);
            db.execSQL(SchoolYear.CREATE_SCHOOL_YEAR_TABLE);
            db.execSQL(SessionInfo.CREATE_TABLE_SESSION_INFO);
            db.execSQL(AttendancePercentage.CREATE_TABLE_ATTENDANCE_PERCENTAGE);
            db.execSQL(ErrorLog.CREATE_ERROR_LOG_TABLE);

//            db.execSQL(FeesCollection.CREATE_TABLE_FEE_TYPE);
//            db.execSQL(FeesCollection.CREATE_TABLE_TRANSACTION_TYPE);
//            db.execSQL(FeesCollection.CREATE_TABLE_TRANSACTION_CATEGORY);
            db.execSQL(FeesCollection.CREATE_TABLE_FEES_HEADER);
            db.execSQL(FeesCollection.CREATE_TABLE_FEES_DETAIL);
//
//            FeesCollection.getInstance(context).insertTempDataInFeeTypeTable();
//            FeesCollection.getInstance(context).insertTempDataInTransactionTypeTable();
//            FeesCollection.getInstance(context).insertTempDataInTransactionCategoryTable();

            SchoolYear.getInstance(context).insertRawSchoolYear(db);
            HolidayCalendarGlobal.getInstance(context).insertRawHolidayCalendarGlobal(db);
            HolidayCalendarSchool.getInstance(context).insertRawHolidayCalendarSchool(db);

            //HR Module Tables
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeeQualificationDetail);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeePosition);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesDesignation);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesLeaves);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_Leaves_Type);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_RESIGNATION);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesResignationReason);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesResignationType);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeeSeparationImages);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_User_Images);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesLeaveStatus);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesResignStatus);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_School);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_TEACHER_Attendance);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_TeacherAttendanceType);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_SEPARATION_DETAIL);
            db.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_PENDING_SEPARATION);
            db.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_EMP_SUBJECT_TAGGING);
            db.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_SUBJECTS);
            db.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS);
            db.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_TCT_PHASE);
            db.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_DESIGNATIONS);
            db.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_LEAVES_TYPE);

            //Help Tables
            db.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_FEEDBACK);
            db.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_USER_MANUAL);
            db.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_POLICIES);
            db.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_FAQs);

            //Expense Tables
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_EXPENSE_HEAD);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD_LIMITS);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD_LIMITS_MONTHLY);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD_EXCEPTIONS);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SCHOOL_PETTYCASH_LIMITS);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SCHOOL_PETTYCASH_MONTHLY_LIMITS);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTIONS);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_IMAGES);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_EXTENDED);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_AMOUNT_CLOSING);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_CATEGORY);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_BUCKET);
            db.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_FLOW);

            //Network Connection
            db.execSQL(NetworkConnectionHelperClass.getInstance(context).CREATE_TABLE_NETWORK_CONNECTION_INFO);

            //App Sync Status
            db.execSQL(SyncProgressHelperClass.getInstance(context).CREATE_TABLE_APP_SYNC_STATUS);
            db.execSQL(SyncProgressHelperClass.getInstance(context).CREATE_TABLE_APP_SYNC_STATUS_MASTER);

            //Global Tables
            db.execSQL(GlobalHelperClass.getInstance(context).CREATE_TABLE_RELIGION);
            db.execSQL(GlobalHelperClass.getInstance(context).CREATE_TABLE_NATIONALITY);
            db.execSQL(GlobalHelperClass.getInstance(context).CREATE_TABLE_ELECTIVE_SUBJECTS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {

                //New Tables should be added here and in on create method
                //HR Module Tables
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeeQualificationDetail);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeePosition);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesDesignation);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesLeaves);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_Leaves_Type);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_RESIGNATION);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesResignationReason);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesResignationType);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeeSeparationImages);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_User_Images);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesLeaveStatus);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EmployeesResignStatus);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_School);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_TEACHER_Attendance);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_TeacherAttendanceType);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_SEPARATION_DETAIL);
                sqLiteDatabase.execSQL(EmployeeHelperClass.getInstance(context).CREATE_TABLE_PENDING_SEPARATION);
                sqLiteDatabase.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_EMP_SUBJECT_TAGGING);
                sqLiteDatabase.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_SUBJECTS);
                sqLiteDatabase.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS);
                sqLiteDatabase.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_EMPLOYEE_TCT_PHASE);
                sqLiteDatabase.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_DESIGNATIONS);
                sqLiteDatabase.execSQL(TCTHelperClass.getInstance(context).CREATE_TABLE_TCT_LEAVES_TYPE);

                //Help Tables
                sqLiteDatabase.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_FEEDBACK);
                sqLiteDatabase.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_USER_MANUAL);
                sqLiteDatabase.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_POLICIES);
                sqLiteDatabase.execSQL(HelpHelperClass.getInstance(context).CREATE_TABLE_FAQs);

                //Expense Tables
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_EXPENSE_HEAD);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD_LIMITS);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD_LIMITS_MONTHLY);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SUBHEAD_EXCEPTIONS);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SCHOOL_PETTYCASH_LIMITS);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_SCHOOL_PETTYCASH_MONTHLY_LIMITS);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTIONS);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_IMAGES);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_EXTENDED);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_AMOUNT_CLOSING);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_CATEGORY);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_BUCKET);
                sqLiteDatabase.execSQL(ExpenseHelperClass.getInstance(context).CREATE_TABLE_TRANSACTION_FLOW);

                //Network Connection
                sqLiteDatabase.execSQL(NetworkConnectionHelperClass.getInstance(context).CREATE_TABLE_NETWORK_CONNECTION_INFO);

                //App Sync Status
                sqLiteDatabase.execSQL(SyncProgressHelperClass.getInstance(context).CREATE_TABLE_APP_SYNC_STATUS);
                sqLiteDatabase.execSQL(SyncProgressHelperClass.getInstance(context).CREATE_TABLE_APP_SYNC_STATUS_MASTER);

                //Global Tables
                sqLiteDatabase.execSQL(GlobalHelperClass.getInstance(context).CREATE_TABLE_RELIGION);
                sqLiteDatabase.execSQL(GlobalHelperClass.getInstance(context).CREATE_TABLE_NATIONALITY);
                sqLiteDatabase.execSQL(GlobalHelperClass.getInstance(context).CREATE_TABLE_ELECTIVE_SUBJECTS);

            } catch (Exception e) {
                e.printStackTrace();
            }
//---------------------------------------------------------------------------------------------------
            //Fields should be added here if table already exists
            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_ENROLLMENT, ENROLLMENT_MODIFIED_BY)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_ENROLLMENT + " ADD " + ENROLLMENT_MODIFIED_BY + " VARCHAR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_ENROLLMENT, ENROLLMENT_MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_ENROLLMENT + " ADD " + ENROLLMENT_MODIFIED_ON + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_ATTENDANCE, ATTENDANCE_MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_ATTENDANCE + " ADD " + ATTENDANCE_MODIFIED_ON + " VARCHAR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_ATTENDANCE, ATTENDANCE_MODIFIED_BY)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_ATTENDANCE + " ADD " + ATTENDANCE_MODIFIED_BY + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!isColumnExists(sqLiteDatabase, FeesCollection.TABLE_FEES_HEADER, FeesCollection.REMARKS)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + FeesCollection.TABLE_FEES_HEADER + " ADD " + FeesCollection.REMARKS + " VARCHAR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_CLASS, CLASS_RANK)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_CLASS + " ADD " + CLASS_RANK + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, ExpenseHelperClass.TABLE_EXPENSE_TRANSACTIONS, ExpenseHelperClass.REJECTION_COMMENTS)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + ExpenseHelperClass.TABLE_EXPENSE_TRANSACTIONS + " ADD " + ExpenseHelperClass.REJECTION_COMMENTS + " VARCHAR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).TABLE_EmployeesLeaves, EmployeeHelperClass.leaveWD)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).TABLE_EmployeesLeaves + " ADD " + EmployeeHelperClass.leaveWD + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_STUDENT, STUDENT_PICTURE_UPLOADED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_STUDENT + " ADD " + STUDENT_PICTURE_UPLOADED_ON + " TEXT");
                    if (newVersion == 19) {
                        addStudentImageUploadedOn(sqLiteDatabase);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE, EmployeeHelperClass.getInstance(context).Emp_Job_Status)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Emp_Job_Status + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE, EmployeeHelperClass.getInstance(context).Emp_Gender)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Emp_Gender + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).MODIFIED_ON + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).Emp_SubReason_Text)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Emp_SubReason_Text + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).MODIFIED_BY)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).MODIFIED_BY + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).Cancelled_On)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Cancelled_On + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            addNewColumn(sqLiteDatabase, EmployeeHelperClass.getInstance(context).SEPARATION_DETAIL_TABLE,EmployeeHelperClass.getInstance(context).Cancelled_On, "TEXT");
//            addNewColumn(sqLiteDatabase, EmployeeHelperClass.getInstance(context).SEPARATION_DETAIL_TABLE,EmployeeHelperClass.getInstance(context).Cancelled_By, "INTEGER");


            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).Cancelled_By)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Cancelled_By + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).Emp_SchoolId)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Emp_SchoolId + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).isActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).isActive + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE, EmployeeHelperClass.getInstance(context).Leave_Without_Pay)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).EMPLOYEE_RESIGNATION_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).Leave_Without_Pay + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).SEPARATION_DETAIL_TABLE, EmployeeHelperClass.getInstance(context).HCM_UpdateStatus)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).SEPARATION_DETAIL_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).HCM_UpdateStatus + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).SEPARATION_DETAIL_TABLE, EmployeeHelperClass.getInstance(context).app_rank)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).SEPARATION_DETAIL_TABLE + " ADD " + EmployeeHelperClass.getInstance(context).app_rank + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).TABLE_EmployeesResignationReason, EmployeeHelperClass.getInstance(context).Employee_ResignType_ID)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).TABLE_EmployeesResignationReason + " ADD " + EmployeeHelperClass.getInstance(context).Employee_ResignType_ID + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).TABLE_EmployeesResignationReason, EmployeeHelperClass.getInstance(context).isActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).TABLE_EmployeesResignationReason + " ADD " + EmployeeHelperClass.getInstance(context).isActive + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).TABLE_EmployeesResignationReason, EmployeeHelperClass.getInstance(context).SubReason)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).TABLE_EmployeesResignationReason + " ADD " + EmployeeHelperClass.getInstance(context).SubReason + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, FeesCollection.TABLE_FEES_HEADER, FeesCollection.CASH_DEPOSIT_ID_TYPE)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + FeesCollection.TABLE_FEES_HEADER + " ADD " + FeesCollection.CASH_DEPOSIT_ID_TYPE + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, FeesCollection.TABLE_FEES_HEADER, FeesCollection.MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + FeesCollection.TABLE_FEES_HEADER + " ADD " + FeesCollection.MODIFIED_ON + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, CashDeposit.CASH_DEPOSIT_TABLE, CashDeposit.MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + CashDeposit.CASH_DEPOSIT_TABLE + " ADD " + CashDeposit.MODIFIED_ON + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_SCHOOL, SCHOOL_TYPE)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_SCHOOL + " ADD " + SCHOOL_TYPE + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            addNewColumn(sqLiteDatabase, TABLE_SCHOOL, SCHOOL_SHIFT, "TEXT");

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_CAMPUS, IsActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_CAMPUS + " ADD " + IsActive + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_LOCATION, IsActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_LOCATION + " ADD " + IsActive + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_AREA, IsActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_AREA + " ADD " + IsActive + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_REGION, IsActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_REGION + " ADD " + IsActive + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_CALENDAR, IsActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_CALENDAR + " ADD " + IsActive + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_CALENDAR, MODIFIED_BY)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_CALENDAR + " ADD " + MODIFIED_BY + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_CALENDAR, MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_CALENDAR + " ADD " + MODIFIED_ON + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance, EmployeeHelperClass.IsActive)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance + " ADD " + EmployeeHelperClass.IsActive + " BOOLEAN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance, EmployeeHelperClass.MODIFIED_ON)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance + " ADD " + EmployeeHelperClass.MODIFIED_ON + " VARCHAR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance, EmployeeHelperClass.getInstance(context).Employee_SchoolId)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance + " ADD " + EmployeeHelperClass.getInstance(context).Employee_SchoolId + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, EmployeeHelperClass.getInstance(context).TABLE_EmployeesLeaves, EmployeeHelperClass.getInstance(context).Employee_SchoolId)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + EmployeeHelperClass.getInstance(context).TABLE_EmployeesLeaves + " ADD " + EmployeeHelperClass.getInstance(context).Employee_SchoolId + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TCTHelperClass.getInstance(context).TABLE_TCT_SUBJECTS, TCTHelperClass.getInstance(context).Modified_On)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TCTHelperClass.getInstance(context).TABLE_TCT_SUBJECTS + " ADD " + TCTHelperClass.getInstance(context).Modified_On + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TCTHelperClass.getInstance(context).TABLE_TCT_SUBJECTS, TCTHelperClass.getInstance(context).Modified_By)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TCTHelperClass.getInstance(context).TABLE_TCT_SUBJECTS + " ADD " + TCTHelperClass.getInstance(context).Modified_By + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_SCHOOL, SCHOOL_PROVINCE_ID)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_SCHOOL + " ADD " + SCHOOL_PROVINCE_ID + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, TABLE_SCHOOL, SCHOOL_PROVINCE_NAME)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_SCHOOL + " ADD " + SCHOOL_PROVINCE_NAME + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, FeesCollection.TABLE_FEES_HEADER, FeesCollection.RECEIPT_ID)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + FeesCollection.TABLE_FEES_HEADER + " ADD " + FeesCollection.RECEIPT_ID + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO,
                        NetworkConnectionHelperClass.getInstance(context).AppVersion)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO
                            + " ADD " + NetworkConnectionHelperClass.getInstance(context).AppVersion + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO,
                        NetworkConnectionHelperClass.getInstance(context).IMEI)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO
                            + " ADD " + NetworkConnectionHelperClass.getInstance(context).IMEI + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO,
                        NetworkConnectionHelperClass.getInstance(context).IMEI2)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO
                            + " ADD " + NetworkConnectionHelperClass.getInstance(context).IMEI2 + " TEXT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO,
                        NetworkConnectionHelperClass.getInstance(context).BatteryStats)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + NetworkConnectionHelperClass.getInstance(context).TABLE_NETWORK_CONNECTION_INFO
                            + " ADD " + NetworkConnectionHelperClass.getInstance(context).BatteryStats + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!isColumnExists(sqLiteDatabase, SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS,
                        SyncProgressHelperClass.getInstance(context).SYNC_MASTER_ID)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS
                            + " ADD " + SyncProgressHelperClass.getInstance(context).SYNC_MASTER_ID + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            addNewColumn(sqLiteDatabase, TABLE_AREA, MODIFIED_ON, "VARCHAR");
            addNewColumn(sqLiteDatabase, TABLE_LOCATION, MODIFIED_ON, "VARCHAR");
            addNewColumn(sqLiteDatabase, TABLE_REGION, MODIFIED_ON, "VARCHAR");
            addNewColumn(sqLiteDatabase, TABLE_CAMPUS, MODIFIED_ON, "VARCHAR");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_UserImages, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeePosition, MODIFIED_ON, "TEXT");

            try {
                if (!isColumnExists(sqLiteDatabase, SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS_MASTER,
                        SyncProgressHelperClass.getInstance(context).UserId)) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS_MASTER
                            + " ADD " + SyncProgressHelperClass.getInstance(context).UserId + " INTEGER");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EMPLOYEE_TeacherAttendanceType, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EMPLOYEE_SCHOOL, MODIFIED_ON, "TEXT");
//            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeesLeaveStatus, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeesResignationType, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeesResignationReason, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeesDesignation, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_Leaves_Type, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeesLeaves, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, EmployeeHelperClass.TABLE_EmployeeQualificationDetail, MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_PHASE, TCTHelperClass.Modified_On, "TEXT");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS, TCTHelperClass.Modified_On, "TEXT");
            addNewColumn(sqLiteDatabase, ExpenseHelperClass.TABLE_EXPENSE_TRANSACTIONS, ExpenseHelperClass.HEAD_MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, ExpenseHelperClass.TABLE_EXPENSE_TRANSACTION_IMAGES, ExpenseHelperClass.HEAD_MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, ExpenseHelperClass.TABLE_EXPENSE_AMOUNT_CLOSING, ExpenseHelperClass.HEAD_MODIFIED_ON, "TEXT");
            addNewColumn(sqLiteDatabase, TABLE_PROMOTION, PROMOTION_MODIFIED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_WITHDRAWAL, WITHDRAWAL_MODIFIED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_CLASS, CLASS_MODIFIED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_SECTION, SECTION_MODIFIED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_SCHOOL_CLASS, SCHOOL_CLASS_MODIFIED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, Scholarship_Category.SCHOLARSHIP_CAT, Scholarship_Category.SCHOLARSHIP_CATEGORY_MODIFIED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_WITHDRAWAL_REASON, WITHDRAWAL_REASON_MODIFIED_ON, " TEXT");

            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING, TCTHelperClass.getInstance(context).Mandatory, " TEXT");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_PHASE, TCTHelperClass.getInstance(context).Mandatory, " TEXT");
            addNewColumn(sqLiteDatabase, FeesCollection.TABLE_FEES_HEADER, FeesCollection.CREATED_ON_SERVER, " TEXT");
            addNewColumn(sqLiteDatabase, SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS_MASTER, SyncProgressHelperClass.getInstance(context).AndroidVersion, " TEXT");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING, TCTHelperClass.getInstance(context).Cnic, " TEXT");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING, TCTHelperClass.getInstance(context).regStatus, " INTEGER");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING, TCTHelperClass.getInstance(context).newDesignation_id, " INTEGER");
            addNewColumn(sqLiteDatabase, TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING, TCTHelperClass.getInstance(context).leaveType_id, " INTEGER");

            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_RELIGION, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_NATIONALITY, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_ELECTIVE_SUBJECT_ID, " INTEGER");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_EMAIL, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_IS_ORPHAN, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_IS_DISABLED, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_DEATH_CERT_IMAGE, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_MEDICAL_CERT_IMAGE, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_BFORM_IMAGE, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_BFORM_IMAGE_UPLOADED_ON, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_PROMOTION_COMMENTS, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_STUDENT, STUDENT_PROMOTION_STATUS, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_SCHOOL, PRINCIPAL_FIRST_NAME, " TEXT");
            addNewColumn(sqLiteDatabase, TABLE_SCHOOL, PRINCIPAL_LAST_NAME, " TEXT");


            switch (oldVersion) {
                case 13:
                    try {

                        if (!isColumnExists(sqLiteDatabase, FeesCollection.TABLE_FEES_HEADER, FeesCollection.REMARKS)) {
                            sqLiteDatabase.execSQL("ALTER TABLE " + FeesCollection.TABLE_FEES_HEADER + " ADD " + FeesCollection.REMARKS + " VARCHAR");
                        }

                        if (!isColumnExists(sqLiteDatabase, TABLE_CLASS, CLASS_RANK)) {
                            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_CLASS + " ADD " + CLASS_RANK + " INTEGER");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + CashDeposit.CASH_DEPOSIT_TABLE);
                        sqLiteDatabase.execSQL(CashDeposit.CREATE_CASH_DEPOSIT_TABLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + TABLE_ENROLLMENT);
                        sqLiteDatabase.execSQL(CREATE_TABLE_ENROLLMENT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + CashDeposit.CASH_DEPOSIT_TABLE);
                        sqLiteDatabase.execSQL(CashDeposit.CREATE_CASH_DEPOSIT_TABLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 10:
                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + TABLE_ENROLLMENT);
                        sqLiteDatabase.execSQL(CREATE_TABLE_ENROLLMENT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + AppInvoice.APP_INVOICE_TABLE);
                        sqLiteDatabase.execSQL(AppInvoice.CREATE_APP_INVOICE_TABLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + AppReceipt.APP_RECEIPT_TABLE);
                        sqLiteDatabase.execSQL(AppReceipt.CREATE_APP_RECEIPT_TABLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + CashDeposit.CASH_DEPOSIT_TABLE);
                        sqLiteDatabase.execSQL(CashDeposit.CREATE_CASH_DEPOSIT_TABLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + FeesCollection.TABLE_FEES_HEADER);
                        sqLiteDatabase.execSQL(FeesCollection.CREATE_TABLE_FEES_HEADER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        sqLiteDatabase.execSQL("Drop table IF EXISTS " + FeesCollection.TABLE_FEES_DETAIL);
                        sqLiteDatabase.execSQL(FeesCollection.CREATE_TABLE_FEES_DETAIL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //Temperory creating and inserting
                    FeesCollection.getInstance(context).dropTempFeeTypeTable(sqLiteDatabase);
                    FeesCollection.getInstance(context).createFeeTypeTable(sqLiteDatabase);
                    FeesCollection.getInstance(context).insertTempDataInFeeTypeTable(sqLiteDatabase);

                    FeesCollection.getInstance(context).dropTempTransactionTypeTable(sqLiteDatabase);
                    FeesCollection.getInstance(context).createTransactionTypeTable(sqLiteDatabase);
                    FeesCollection.getInstance(context).insertTempDataInTransactionTypeTable(sqLiteDatabase);

                    FeesCollection.getInstance(context).dropTempTransactionCategoryTable(sqLiteDatabase);
                    FeesCollection.getInstance(context).createTransactionCategoryTable(sqLiteDatabase);
                    FeesCollection.getInstance(context).insertTempDataInTransactionCategoryTable(sqLiteDatabase);
                    break;

            }
            try {
                sqLiteDatabase.execSQL(ErrorLog.CREATE_ERROR_LOG_TABLE);
            } catch (Exception e) {

            }
        }
    }

    private void addNewColumn(SQLiteDatabase sqLiteDatabase, String tableName, String columnName, String dataType) {
        try {
            if (!isColumnExists(sqLiteDatabase, tableName,
                    columnName)) {
                sqLiteDatabase.execSQL("ALTER TABLE " + tableName
                        + " ADD " + columnName + " " + dataType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStudentImageUploadedOn(SQLiteDatabase DB) {
        try {
            ContentValues values = new ContentValues();
            values.put(STUDENT_PICTURE_UPLOADED_ON, AppModel.getInstance().getDateTime());
            DB.update(TABLE_STUDENT, values, STUDENT_PICTURE_UPLOADED_ON + " is null or " +
                    STUDENT_PICTURE_UPLOADED_ON + "= \"\" ", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isColumnExists(SQLiteDatabase Db, String table, String column) {
        boolean isExists = false;
        Cursor cursor = null;
        try {
            cursor = Db.rawQuery("PRAGMA table_info(" + table + ")", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    if (column.equalsIgnoreCase(name)) {
                        isExists = true;
                        break;
                    }
                }
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return isExists;
    }

    //Global Methods

    public long updateTableColumns(String tableName, ContentValues cv, int id) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = cv;
            long i = DB.update(tableName, values, KEY_ID + " = " + id, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //TABLE_USERS
    public long addUser(UserModel sm) {
        SQLiteDatabase DB = getDB();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ID, sm.getId());
            values.put(KEY_FIRST_NAME, sm.getFirstname());
            values.put(KEY_LAST_NAME, sm.getLastname());
            values.put(KEY_EMAIL, sm.getEmail());
            values.put(KEY_STATUS, sm.getStatus());
            values.put(KEY_DESIGNATION, sm.getDesignation());
            values.put(KEY_ROLE, sm.getRole());
            values.put(KEY_ROLE_ID, sm.getRoleId());
            values.put(KEY_DEFAULT_SCHOOL_ID, sm.getDefault_school_id());
            values.put(KEY_DEPARTMENT_ID, sm.getDepartment_Id());
            values.put(KEY_USERNAME, sm.getUsername());
            values.put(KEY_LAST_PASSWORD, sm.getLastpassword());
            values.put(KEY_LAST_LOGIN_TIME, sm.getLastlogin_time());
            values.put(KEY_LAST_METADATA_SYNC_ON, sm.getMetadata_sync_on());
            values.put(KEY_SESSION_TOKEN, sm.getSession_token());
            values.put(KEY_LAST_PASSWORD_3, sm.getLastpassword_3());
            values.put(KEY_LAST_PASSWORD_2, sm.getLastpassword_2());
            values.put(KEY_LAST_PASSWORD_1, sm.getLastpassword_1());
            values.put(KEY_PASSWORD_CHANGE_ON_LOGIN, sm.getPassword_change_on_login());

            long i = DB.insert(TABLE_USERS, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateUser(String TableColumn, String value) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(TableColumn, value);
            long i = DB.update(TABLE_USERS, values, null, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteAllUser() {
        SQLiteDatabase DB = getDB();
        try {
            long id;

            id = DB.delete(TABLE_USERS, null, null);
            System.out.print(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserModel getCurrentLoggedInUser() {
        Cursor cursor = null;
        UserModel um = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS +
                    " ORDER BY " + KEY_ID + " DESC";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                um = new UserModel();

                um.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                um.setFirstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                um.setLastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                um.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                um.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                um.setDesignation(cursor.getString(cursor.getColumnIndex(KEY_DESIGNATION)));
                um.setRole(cursor.getString(cursor.getColumnIndex(KEY_ROLE)));
                um.setRoleId(cursor.getInt(cursor.getColumnIndex(KEY_ROLE_ID)));
                um.setDefault_school_id(cursor.getInt(cursor.getColumnIndex(KEY_DEFAULT_SCHOOL_ID)));
                um.setDepartment_Id(cursor.getInt(cursor.getColumnIndex(KEY_DEPARTMENT_ID)));
                um.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                um.setLastpassword(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD)));
                um.setLastlogin_time(cursor.getString(cursor.getColumnIndex(KEY_LAST_LOGIN_TIME)));
                um.setMetadata_sync_on(cursor.getString(cursor.getColumnIndex(KEY_LAST_METADATA_SYNC_ON)));
                um.setSession_token(cursor.getString(cursor.getColumnIndex(KEY_SESSION_TOKEN)));
                um.setLastpassword_3(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_3)));
                um.setLastpassword_2(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_2)));
                um.setLastpassword_1(cursor.getString(cursor.getColumnIndex(KEY_LAST_PASSWORD_1)));
                um.setPassword_change_on_login(cursor.getInt(cursor.getColumnIndex(KEY_PASSWORD_CHANGE_ON_LOGIN)));
                return um;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //TABLE_USER_SCHOOL
    public void addUserSchool(List<SchoolModel> list) {
        SQLiteDatabase DB = getDB();
        DB.beginTransaction();
        try {
            for (SchoolModel sm : list) {
                ContentValues values = new ContentValues();
                values.put(SCHOOL_ID, sm.getId());
                long i = DB.insert(TABLE_USER_SCHOOL, null, values);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.endTransaction();
        }
    }


    public ArrayList<Integer> getAllUserSchool() {
        ArrayList<Integer> userSchoolIds = new ArrayList<Integer>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_USER_SCHOOL;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(SCHOOL_ID));
                    userSchoolIds.add(id);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return userSchoolIds;
    }

    public void deleteAllUserSchool() {
        SQLiteDatabase DB = getDB();
        try {
            long id;

            id = DB.delete(TABLE_USER_SCHOOL, null, null);
            System.out.print(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Students count for specific school
     *
     * @param schoolId
     * @return
     */
    public int getAllStudentsCount(int schoolId) {
        int studentCount = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT COUNT(*) AS RECCOUNT FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1' ";


            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                return studentCount = cursor.getInt(cursor.getColumnIndex("RECCOUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return studentCount;
    }

    /**
     * \
     * <p>
     * Studnets count for all school
     *
     * @return
     */
    public int getAllStudentsCount(String schoolId) {
        int studentCount = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT COUNT(*) AS RECCOUNT FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " IN(" + schoolId + ")"
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1' ";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                return studentCount = cursor.getInt(cursor.getColumnIndex("RECCOUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return studentCount;
    }

    public ViewSSRTableModel getSSRForDashboard(int schoolId) {
        Cursor cursor = null;
        String selectQuery;
        ViewSSRTableModel sm = null;
        try {
            if (schoolId > 0) {

                selectQuery = "select count(*) as Total, \n" +
                        "(select count (student_gender) from student s inner join school_class sc on s.schoolclass_id =sc.id and sc.school_id = @SchoolID and upper(s.student_gender) ='M' and s.is_active = 1) as Male,\n" +
                        "(select count (student_gender) from student s inner join school_class sc on s.schoolclass_id =sc.id and sc.school_id = @SchoolID and upper(s.student_gender) ='F' and s.is_active = 1) as Female\n" +
                        "from student s inner join school_class sc on s.schoolclass_id =sc.id and sc.school_id = @SchoolID where s.is_active = 1";
                selectQuery = selectQuery.replace("@SchoolID", String.valueOf(schoolId));
            } else {
                selectQuery = "select count(*) as Total, (select count (student_gender) from student where upper(student_gender) ='M' and is_active = 1) as Male,\n" +
                        "(select count (student_gender) from student where upper(student_gender) ='F' and is_active = 1) as Female\n" +
                        "from student where is_active = 1";

            }

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                sm = new ViewSSRTableModel();
                sm.setmOverall(cursor.getDouble(cursor.getColumnIndex("Total")));
                sm.setmMale(cursor.getDouble(cursor.getColumnIndex("Male")));
                sm.setmFemale(cursor.getDouble(cursor.getColumnIndex("Female")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return sm;
    }

    public List<PendingAttendanceModel> getPendingAttendanceForToday(String schoolIds) {
        List<PendingAttendanceModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        PendingAttendanceModel model = null;
        try {

            selectQuery = "select distinct school.id,(class.id) as class_id, school_class.id as 'schoolclass_id', (class.name) as class ,(section.name) as section,school.school_name as school, date('now', 'localtime') as Date  from school_class \n" +
                    "inner join class on class.id = school_class.class_id\n" +
                    "inner join section on section.id = school_class.section_id\n" +
                    "inner join school on school.id = school_class.school_id\n" +
                    "where school.id IN(@SchoolID) and is_active = 1 " +
                    "order by class_id";
            selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    model = new PendingAttendanceModel();
                    model.setSchoolId(cursor.getInt(cursor.getColumnIndex("id")));
                    model.setSchoolclass_id(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
                    model.setClassName(cursor.getString(cursor.getColumnIndex("class")));
                    model.setSection(cursor.getString(cursor.getColumnIndex("section")));
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

    public List<AttendanceLast30DaysCountModel> getAttendanceTakenCountForLast30Days() {
        List<AttendanceLast30DaysCountModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        AttendanceLast30DaysCountModel model = null;
        try {

            selectQuery = " WITH RECURSIVE dates(date) AS (\n" +
                    "  VALUES(date('now', 'localtime'))\n" +
                    "  UNION ALL\n" +
                    "  SELECT date(date, '-1 day')\n" +
                    "  FROM dates\n" +
                    "  limit 30\n" +
                    ")\n" +
                    "select count(attendance.for_date) as count,date,attendance.for_date,attendance.school_class_id from dates\n" +
                    "left join attendance on attendance.for_date = dates.date\n" +
                    "group by date " +
                    "order by date desc";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    model = new AttendanceLast30DaysCountModel();
                    model.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                    model.setSchoolclass_id(cursor.getInt(cursor.getColumnIndex("school_class_id")));
                    model.setDate(cursor.getString(cursor.getColumnIndex("date")));
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

    public List<AttendanceLast30DaysCountModel> getAttendanceTakenCountForToday() {
        List<AttendanceLast30DaysCountModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        AttendanceLast30DaysCountModel model = null;
        try {

            selectQuery = "select count (*)as count, date('now','localtime') as date from attendance where for_date = date('now' ,'localtime');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    model = new AttendanceLast30DaysCountModel();
                    model.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                    model.setDate(cursor.getString(cursor.getColumnIndex("date")));

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

    public List<Integer> getSchoolClassIdsForMarkedAttendance(String date) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        try {

            selectQuery = "select school_class_id from attendance where for_date = '" + date + "'";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int schoolClassId = cursor.getInt(cursor.getColumnIndex("school_class_id"));
                    list.add(schoolClassId);
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

    public String getAttendanceTakenCountForToday(String schoolIds) {
        List<PendingAttendanceModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        String AttendanceCount = "0";
        try {

            selectQuery = "select count(*) as AttendanceCount  from attendance \n" +
                    "inner join school_class sc on sc.id = attendance.school_class_id  \n" +
                    "inner join school on school.id = sc.school_id\n" +
                    "where school.id IN( @SchoolID) and is_active = 1\n" +
                    "and for_date = date('now','localtime')";
            selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                AttendanceCount = cursor.getString(cursor.getColumnIndex("AttendanceCount"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return AttendanceCount;
    }

    public String getAttendanceTakenCountFor30Days(String schoolIds) {
        List<PendingAttendanceModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        String AttendanceCount = "0";
        try {

            selectQuery = "select DISTINCT attendance.for_date from attendance \n" +
                    "inner join school_class sc on sc.id = attendance.school_class_id  \n" +
                    "inner join school on school.id = sc.school_id\n" +
                    "where school.id IN( @SchoolID) and is_active = 1\n" +
                    "and for_date >= date('now', 'localtime','-1 month')";

//            selectQuery = "select count(*) as AttendanceCount  from attendance \n" +
//                    "inner join school_class sc on sc.id = attendance.school_class_id  \n" +
//                    "inner join school on school.id = sc.school_id\n" +
//                    "where school.id IN( @SchoolID) and is_active = 1\n" +
//                    "and for_date >= date('now', 'localtime','-1 month')";
            selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                AttendanceCount = String.valueOf(cursor.getCount());
            }

//            if (cursor.moveToFirst()) {
//
//                AttendanceCount = cursor.getString(cursor.getColumnIndex("AttendanceCount"));
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return AttendanceCount;

    }

    public Float getAttendanceOfstudent30days(int student_id) {
        Cursor cursor = null;
        String selectQuery;
        float AttendanceCount = 0;
        try {

          /*  selectQuery = "select student_attendance.isabsent from student_attendance,attendance where student_attendance.student_id = " + student_id +
            " and student_attendance.isabsent = 1 and attendance.for_date >= date('now', 'localtime','-1 month')";
          */
            selectQuery = "select student_attendance.isabsent from attendance , student, student_attendance \n" +
                    "where attendance.id = student_attendance.attendance_id and student_attendance.student_id = student.id and student.id = " + student_id +
                    " and student_attendance.isabsent = 1 and attendance.for_date >= date('now', 'localtime','-1 month')";

           /* selectQuery = "select DISTINCT attendance.for_date from attendance \n" +
                    "inner join school_class sc on sc.id = attendance.school_class_id  \n" +
                    "inner join school on school.id = sc.school_id\n" +
                    "inner join student_attendance sa on sa.attendance_id = attendance.id \n" +
                    "where school.id IN( @SchoolID) and is_active = 1 and sa.student_id = " + student_id +"\n"+
                    "and for_date >= date('now', 'localtime','-1 month')";
*/
            //selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                AttendanceCount = (float) cursor.getCount();
            }

            /*if (cursor.moveToFirst()) {
                AttendanceCount = cursor.getString(cursor.getColumnIndex("isabsent"));
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return AttendanceCount;
    }

    public String getAttendanceTotalCountForToday(String schoolIds) {
        List<PendingAttendanceModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        String AttendanceCount = "0";
        try {

            selectQuery = "select count(class.name) as Total from school_class \n" +
                    "inner join class on class.id = school_class.class_id\n" +
                    "inner join section on section.id = school_class.section_id\n" +
                    "inner join school on school.id = school_class.school_id\n" +
                    "where school.id IN(@SchoolID) and is_active = 1";
            selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                AttendanceCount = cursor.getString(cursor.getColumnIndex("Total"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return AttendanceCount;
    }

    public String getAttendanceTotalCountFor30Days(String schoolIds) {
        List<PendingAttendanceModel> list = new ArrayList<>();
        Cursor cursor = null;
        String selectQuery;
        String AttendanceCount = "0";
        try {

            selectQuery = "select count(class.name) *30 as Total from school_class \n" +
                    "inner join class on class.id = school_class.class_id\n" +
                    "inner join section on section.id = school_class.section_id\n" +
                    "inner join school on school.id = school_class.school_id\n" +
                    "where school.id IN(@SchoolID) and is_active = 1";
            selectQuery = selectQuery.replace("@SchoolID", schoolIds);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                AttendanceCount = cursor.getString(cursor.getColumnIndex("Total"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }

        return AttendanceCount;
    }


    public ArrayList<StudentModel> getAllStudentsList(int schoolId, boolean picture) {
        ArrayList<StudentModel> smList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1'";

            if (picture)
                selectQuery += " AND s." + STUDENT_PICTURE_NAME + " IS NOT NULL";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    StudentModel sm = new StudentModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                    sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public int getAllUnapprovedStudentsCount(int schoolId) {
        int studentCount = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT COUNT(*) AS RECCOUNT FROM "
                    + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS
                    + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + STUDENT_IS_APPROVED + " = " + 0
                    + " AND s." + STUDENT_APPROVED_BY + " IS NOT NULL"
                    + " AND s." + STUDENT_IS_ACTIVE + " = " + 1;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                studentCount = cursor.getInt(cursor.getColumnIndex("RECCOUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return studentCount;
    }


    public int getAllUnapprovedStudentsCountDashboard(int schoolId) {
        int studentCount = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT COUNT(*) AS RECCOUNT FROM student s " +
                    "INNER JOIN school_class sc ON sc.id = s.schoolclass_id " +
                    "WHERE sc.school_id = " + schoolId + " AND s.is_approved = 0  AND s.is_active = 1 " +
                    "AND s.approved_by not in ('null','0','')";
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                studentCount = cursor.getInt(cursor.getColumnIndex("RECCOUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return studentCount;
    }

    public int getSchoolYearId(int schoolId) {
        int schoolYearId = 0;

        Cursor cursor = null;
        String query = "select school_year_id as schoolYearId from school where id = @SchoolId";
        SQLiteDatabase db = getDB();
        query = query.replace("@SchoolId", schoolId + "");

        try {

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                schoolYearId = cursor.getInt(cursor.getColumnIndex("schoolYearId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return schoolYearId;

    }

    public ArrayList<StudentModel> getDashboardUnapprovedStudents(int schoolId, int studentID) {
        ArrayList<StudentModel> dsnuList = new ArrayList<>();
        StudentModel dsnmodel;
        Cursor cursor = null;
        try {
            String selectQuery1 = "SELECT * FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON c." + KEY_ID + " = sc." + SCHOOL_CLASS_CLASSID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + STUDENT_IS_APPROVED + " = 0"
                    + " AND s." + STUDENT_APPROVED_BY + " NOT IN ('null','0','')"
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1'";


            if (studentID > 0) {
                selectQuery1 += " AND s.id = " + studentID;
            }

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery1, null);

            if (cursor.moveToFirst()) {
                do {
                    dsnmodel = new StudentModel();
                    dsnmodel.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    dsnmodel.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    dsnmodel.setCurrentClass(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));
                    dsnmodel.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                    dsnmodel.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                    dsnmodel.setCurrentSection(cursor.getString(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                    dsnmodel.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                    dsnmodel.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                    dsnmodel.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                    dsnmodel.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                    dsnmodel.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                    dsnmodel.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                    dsnmodel.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                    dsnmodel.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                    dsnmodel.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                    dsnmodel.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                    dsnmodel.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                    dsnmodel.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                    dsnmodel.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                    dsnmodel.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                    dsnmodel.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                    dsnmodel.setUnapprovedComments(cursor.getString(cursor.getColumnIndex(STUDENT_UNAPPROVED_COMMENTS)));
                    dsnmodel.setApproved_on(cursor.getString(cursor.getColumnIndex(STUDENT_APPROVED_ON)));
                    dsnmodel.setApproved_by(cursor.getInt(cursor.getColumnIndex(STUDENT_APPROVED_BY)));
                    dsnuList.add(dsnmodel);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return dsnuList;
    }

    public int getAllWithdrawalStudentsCount(int schoolId, String month, String year) {
        int withdrawalCount = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT COUNT(w." + KEY_ID + ") AS COUNT FROM " + TABLE_WITHDRAWAL + " w," + TABLE_SCHOOL + " s " +
                    "WHERE s." + KEY_ID + "=" + schoolId +
                    " AND strftime('%m', w." + WITHDRAWAL_CREATED_ON + ") = '" + month + "'" +
                    " AND strftime('%Y', w." + WITHDRAWAL_CREATED_ON + ") = '" + year + "'";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                return withdrawalCount = cursor.getInt(cursor.getColumnIndex("COUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return withdrawalCount;
    }

    public ArrayList<StudentModel> getAllSearchedStudents(int schoolId, int classId, int SectionId, String grNo, String studentName) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;

            if (classId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_CLASSID + " = " + classId;
            }
            if (SectionId > 0) {
                SectionModel sectionModel = getSectionById(SectionId);
                selectQuery += " AND sc." + SCHOOL_CLASS_SECTIONID + " = " + SectionId;
            }

            if (grNo != null && grNo.length() > 0) {
                selectQuery += " AND s." + STUDENT_GR_NO + "= " + grNo;
            }
            if (studentName != null && studentName.length() > 0) {
                selectQuery += " AND s." + STUDENT_NAME + " LIKE '%" + studentName + "%'";
            }

            selectQuery += " ORDER BY s." + STUDENT_GR_NO;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1") ? true : false;
                    if (b) {
                        StudentModel sm = new StudentModel();
                        sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                        sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                        sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                        sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                        sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                        sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                        sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                        sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                        sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                        sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                        sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                        sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                        sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                        sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                        sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                        sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                        sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                        sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                        sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                        sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                        sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                        sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                        sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                        sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                        sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                        sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                        sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                        sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                        sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                        sm.setActive(b);
                        smList.add(sm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<StudentModel> getAllSearchedStudentsforPromotion(int schoolId, int classId, int SectionId, String grNo, String studentName) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " and s.id not in (SELECT distinct student_id\n" +
                    "from promotion_student ps inner join promotion p on ps.promotion_id = p.id\n" +
                    "where JulianDay(date('now')) - JulianDay(p.created_on) < 30)";

            if (classId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_CLASSID + " = " + classId;
            }
            if (SectionId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_SECTIONID + " = " + SectionId;
            }

            if (grNo != null && grNo.length() > 0) {
                selectQuery += " AND s." + STUDENT_GR_NO + "= " + grNo;
            }
            if (studentName != null && studentName.length() > 0) {
                selectQuery += " AND s." + STUDENT_NAME + " LIKE '%" + studentName + "%'";
            }

            selectQuery += " ORDER BY s." + STUDENT_GR_NO;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1") ? true : false;
                    if (b) {
                        StudentModel sm = new StudentModel();
                        sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                        sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                        sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                        sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                        sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                        sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                        sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                        sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                        sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                        sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                        sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                        sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                        sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                        sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                        sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                        sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                        sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                        sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                        sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                        sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                        sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                        sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                        sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                        sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                        sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                        sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                        sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                        sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                        sm.setActive(b);
                        smList.add(sm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    //    TODO must use this function in onResume method
    public ArrayList<StudentModel> getAllStudentsInSchoolUsing(int schoolId, int classId, int SectionId, String grNo, String studentName, int is_active, String admission_type, boolean isFeeEntry) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {
            String openingBalance = isFeeEntry ? ",(Select ifnull(fd.fee_amount,0) as A from FeesHeader fh inner join FeesDetail fd on fd.feesHeader_id = fh.id where fh.student_id = s.server_id and fh.Category_Id = 3 and fh.TransactionType_Id = 1) openingAmount\n" : "";
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + openingBalance
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID;
            if (admission_type.equalsIgnoreCase(AppConstants.Promotions) || admission_type.equalsIgnoreCase(AppConstants.NewAdmissions)) {
                selectQuery += " INNER JOIN " + TABLE_SCHOOL + " ts ON sc." + SCHOOL_CLASS_SCHOOLID + " = ts." + KEY_ID;

                selectQuery += " INNER JOIN promotion_student ps on ps.student_id = s.id ";
            }


            selectQuery += " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;

            if (classId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_CLASSID + " = " + classId;
            }
            if (SectionId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_SECTIONID + " = " + SectionId;
            }

            if (grNo != null && grNo.length() > 0) {
                selectQuery += " AND s." + STUDENT_GR_NO + "= " + grNo;
            }
            if (studentName != null && studentName.length() > 0) {
                selectQuery += " AND s." + STUDENT_NAME + " LIKE '%" + studentName + "%'";
            }
            if (is_active == 0 || is_active == 1) {
                selectQuery += " AND s." + STUDENT_IS_ACTIVE + "= " + is_active;
            }
            if (admission_type.equalsIgnoreCase(AppConstants.NewAdmissions)) {
                selectQuery += " AND ts.start_date <= datetime('now') and ts.end_date >= datetime('now') " +
                        " AND s.enrollment_date >= ts.start_date " +
                        " AND s.enrollment_date <= ts.end_date ";
            }
            if (admission_type.equalsIgnoreCase(AppConstants.Readmissions)) {
                selectQuery += " AND s.Previous_STUDENT_ID IS NOT NULL " +
                        "AND s.Previous_STUDENT_ID != ''";
            }

            if (admission_type.equalsIgnoreCase(AppConstants.Withdrawals)) {
                selectQuery += " AND s.is_withdrawl = 1";
            }

            if (admission_type.equals(AppConstants.Graduations)) {
                selectQuery += " AND s.is_withdrawl = 0 AND withdrawal_reason_id = 14";
            }
            if (admission_type.equalsIgnoreCase(AppConstants.Promotions)) {
                selectQuery += " AND ts.start_date <= datetime('now') and ts.end_date >= datetime('now') ";
            }
            if (isFeeEntry) {
                selectQuery += " AND (s.Actual_Fees < 10 OR s.Actual_Fees > 1500 OR s.Actual_Fees IS NULL OR openingAmount IS NULL)";
            }

            if (admission_type.equals(AppConstants.graduated)) {
                selectQuery += "  AND withdrawal_reason_id = 14 AND s.is_active = 0";
            }

            if (admission_type.equalsIgnoreCase(AppConstants.withdrawn)) {
                selectQuery += " AND s.withdrawal_reason_id != 14 AND s.is_active = 0";
            }

            selectQuery += " ORDER BY CAST(s." + STUDENT_GR_NO + " as INTEGER) ASC";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");

                    StudentModel sm = new StudentModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                    sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                    sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                    sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                    sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                    sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                    sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                    sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                    sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                    sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                    sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                    sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                    sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                    sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                    sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                    sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                    sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                    sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                    sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                    sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                    sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                    sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                    sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                    sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                    sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                    sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                    sm.setWithdrawalReasonId(cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID)));
                    sm.setActive(b);
                    sm.setActualFees(cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees)));

                    sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                    sm.setPictureUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_UPLOADED_ON)));
                    if (isFeeEntry)
                        sm.setOpeningBalance(cursor.getInt(cursor.getColumnIndex("openingAmount")));

                    sm.setStudent_promotionStatus(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_STATUS)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<StudentModel> getAllStudentsInSchoolForAvgFee(int schoolId) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID;

            selectQuery += " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;
            selectQuery += " AND s." + STUDENT_IS_ACTIVE + "= " + 1;
            selectQuery += " AND ifnull(s.Actual_Fees,0) > 10";

            selectQuery += " ORDER BY s.student_gr_no";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");

                    StudentModel sm = new StudentModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                    sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                    sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                    sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                    sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                    sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                    sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                    sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                    sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                    sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                    sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                    sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                    sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                    sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                    sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                    sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                    sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                    sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                    sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                    sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                    sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                    sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                    sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                    sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                    sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                    sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                    sm.setWithdrawalReasonId(cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID)));
                    sm.setActive(b);
                    sm.setActualFees(cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees)));
                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<Double> getStudentsFees(int schoolId, int classId, int SectionId, boolean isFeeEntry) {
        ArrayList<Double> smList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID;


            selectQuery += " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;

            if (classId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_CLASSID + " = " + classId;
            }
            if (SectionId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_SECTIONID + " = " + SectionId;
            }
//            if (isFeeEntry)
            selectQuery += " AND s.Actual_Fees > 10 ";

            selectQuery += " AND s." + STUDENT_IS_ACTIVE + "= " + 1;
            selectQuery += " ORDER BY s." + STUDENT_GR_NO;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    smList.add(cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees)));
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<StudentModel> getAllSearchedStudentsInSchool(int schoolId, int classId, int SectionId, String grNo, String studentName) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;

            if (classId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_CLASSID + " = " + classId;
            }
            if (SectionId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_SECTIONID + " = " + SectionId;
            }

            if (grNo != null && grNo.length() > 0) {
                selectQuery += " AND s." + STUDENT_GR_NO + "= " + grNo;
            }
            if (studentName != null && studentName.length() > 0) {
                selectQuery += " AND s." + STUDENT_NAME + " LIKE '%" + studentName + "%'";
            }

            selectQuery += " ORDER BY s." + STUDENT_GR_NO;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");
                    StudentModel sm = new StudentModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                    sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                    sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                    sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                    sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                    sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                    sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                    sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                    sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                    sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                    sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                    sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                    sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                    sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                    sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                    sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                    sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                    sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                    sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                    sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                    sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                    sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                    sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                    sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                    sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                    sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                    sm.setActive(b);
                    smList.add(sm);

                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<StudentModel> getAllSearchedUnapprovedStudents(int schoolId, int classId, int SectionId, String grNo, String studentName) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT s.*, c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;

            if (classId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_CLASSID + " = " + classId;
            }
            if (SectionId > 0) {
                selectQuery += " AND sc." + SCHOOL_CLASS_SECTIONID + " = " + SectionId;
            }

            if (grNo != null && grNo.length() > 0) {
                selectQuery += " AND s." + STUDENT_GR_NO + "= " + grNo;
            }
            if (studentName != null && studentName.length() > 0) {
                selectQuery += " AND s." + STUDENT_NAME + " LIKE '%" + studentName + "%'";
            }
            selectQuery += " AND ( s." + STUDENT_IS_APPROVED + " IS NULL or " + STUDENT_IS_APPROVED + " = '0' )";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1") ? true : false;
                    if (b) {
                        StudentModel sm = new StudentModel();
                        sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                        sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                        sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                        sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                        sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                        sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                        sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                        sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                        sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                        sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                        sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                        sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                        sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                        sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                        sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                        sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                        sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                        sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                        sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                        sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                        sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                        sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                        sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                        sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                        sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                        sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                        sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                        sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                        sm.setActive(b);
                        sm.setApproved(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(STUDENT_IS_APPROVED))));
                        smList.add(sm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<StudentModel> getAllStudentsForUpload(int schoolId) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        String uploadedOn = null;
        try {

            String selectQuery = "SELECT s.* FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS
                    + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " and  s." + STUDENT_UPLOADED_ON + " IS NULL";


            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    boolean shouldUpload = true;
                    uploadedOn = cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON));

                    if (uploadedOn != null && uploadedOn.length() > 0)
                        shouldUpload = false;

                    if (shouldUpload) {

                        StudentModel sm = new StudentModel();
                        sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sm.setServerId(cursor.getInt(cursor.getColumnIndex(STUDENT_SERVER_ID)));
                        sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                        sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                        sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                        sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                        if (sm.getEnrollmentDate() != null) {
                            String format = AppModel.getInstance().determineDateFormat(sm.getEnrollmentDate());
                            if (format != null)
                                sm.setEnrollmentDate(AppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), format, "dd/MM/yy"));
                        }


                        sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                        if (sm.getDob() != null)
                            sm.setDob(AppModel.getInstance().convertDatetoFormat(sm.getDob(), "yyyy-MM-dd", "dd/MM/yy"));


                        sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                        sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                        sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                        sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                        sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                        sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                        sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                        sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                        sm.setPreviousStudentID(cursor.getInt(cursor.getColumnIndex(STUDENT_PREVIOUS_STUDENT_ID)));
                        sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                        if (cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)) != null)
                            sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));

                        sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                        sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                        sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                        sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                        sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                        sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                        if (sm.getModifiedOn() != null)
                            sm.setModifiedOn(AppModel.getInstance().convertDatetoFormat(sm.getModifiedOn(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss"));


                        sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                        if (sm.getUploadedOn() != null)
                            sm.setUploadedOn(AppModel.getInstance().convertDatetoFormat(sm.getUploadedOn(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yy HH:mm:ss"));


                        sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                        sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                        int withdrawalId = cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID));
                        if (withdrawalId > 0)
                            sm.setWithdrawalReasonId(withdrawalId);
                        if (sm.getWithdrawnOn() != null)
                            sm.setWithdrawnOn(AppModel.getInstance().convertDatetoFormat(sm.getWithdrawnOn(), "yyyy-MM-dd", "dd/MM/yyyy"));

                        boolean isWithdraw = cursor.getString(cursor.getColumnIndex(STUDENT_IS_WITHDRAWN)).equals("1");
                        sm.setWithdrawal(isWithdraw);
                        boolean isActive = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");
                        sm.setActive(!isActive);
                        String isApproved = cursor.getString(cursor.getColumnIndex(STUDENT_IS_APPROVED));
                        isActive = (isApproved != null) && isApproved.equals("1");
                        sm.setApproved(isActive);
                        sm.setApproved_by(cursor.getInt(cursor.getColumnIndex(STUDENT_APPROVED_BY)));
                        sm.setActualFees(cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees)));
                        sm.setScholarshipCategoryId(cursor.getInt(cursor.getColumnIndex(STUDENT_ScholarshipCategory_ID)));
                        sm.setApproved_on(cursor.getString(cursor.getColumnIndex(STUDENT_APPROVED_ON)));
                        if (sm.getApproved_on() != null)
                            sm.setApproved_on(AppModel.getInstance().convertDatetoFormat(sm.getApproved_on(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss"));

                        sm.setUnapprovedComments(cursor.getString(cursor.getColumnIndex(STUDENT_UNAPPROVED_COMMENTS)));
                        sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                        sm.setPictureUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_UPLOADED_ON)));

                        sm.setReligion(cursor.getString(cursor.getColumnIndex(STUDENT_RELIGION)));
                        sm.setNationality(cursor.getString(cursor.getColumnIndex(STUDENT_NATIONALITY)));
                        sm.setElectiveSubjectId(cursor.getInt(cursor.getColumnIndex(STUDENT_ELECTIVE_SUBJECT_ID)));
                        sm.setOrphan(cursor.getString(cursor.getColumnIndex(STUDENT_IS_ORPHAN)));
                        sm.setDisabled(cursor.getString(cursor.getColumnIndex(STUDENT_IS_DISABLED)));
                        sm.setEmail(cursor.getString(cursor.getColumnIndex(STUDENT_EMAIL)));

                        String deathCertImage = cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE));
                        if (!Strings.isEmptyOrWhitespace(deathCertImage) && deathCertImage.toLowerCase().contains("bodypart")) {
                            sm.setDeathCert_Image(deathCertImage);
                        } else {
                            sm.setDeathCert_Image("");
                        }
                        String medicalCertImage = cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE));
                        if (!Strings.isEmptyOrWhitespace(medicalCertImage) && medicalCertImage.toLowerCase().contains("bodypart")) {
                            sm.setMedicalCert_Image(medicalCertImage);
                        } else {
                            sm.setMedicalCert_Image("");
                        }
                        String bFormImage = cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE));
                        if (!Strings.isEmptyOrWhitespace(bFormImage) && bFormImage.toLowerCase().contains("bodypart")) {
                            sm.setbForm_Image(bFormImage);
                        } else {
                            sm.setbForm_Image("");
                        }

                        sm.setStudent_promotionComments(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_COMMENTS)));
                        sm.setStudent_promotionStatus(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_STATUS)));

                        smList.add(sm);
                    }
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {

            e.printStackTrace();


        } finally {

            if (cursor != null)
                cursor.close();

        }

        return smList;
    }


    public ArrayList<StudentModel> getAttendanceForStudents(int schoolId, int classId, int SectionId, String attendanceDate) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name, COALESCE(NULLIF(sa.isabsent,''), 'p') as getAttendance "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " LEFT JOIN " + TABLE_ATTENDANCE_STUDENT + " sa ON s." + KEY_ID + " = sa." + ATTENDANCE_STUDENT_ID
                    + " LEFT JOIN " + TABLE_ATTENDANCE + " a ON a." + KEY_ID + " = sa." + ATTENDANCE_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND a." + ATTENDANCE_FOR_DATE + " = '" + attendanceDate + "'"
                    + " AND c." + KEY_ID + " = " + classId
                    + " AND cs." + KEY_ID + " = " + SectionId
                    + " ORDER BY s." + STUDENT_GR_NO;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1") ? true : false;
                    if (b) {
                        StudentModel sm = new StudentModel();
                        sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                        sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                        sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                        sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                        sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                        sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                        sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                        sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                        sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                        sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                        sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                        sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                        sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                        sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                        sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                        sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                        sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                        sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                        sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                        sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                        sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                        sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));

                        sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                        sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                        sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                        sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                        sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                        sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                        sm.setActive(b);
                        sm.setAttendanceStatus(cursor.getString(cursor.getColumnIndex("getAttendance")));
                        smList.add(sm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }


    public ArrayList<StudentModel> getAttendanceForSchoolReport(int schoolId, int classId, int SectionId, String month, String year) {
        ArrayList<StudentModel> smList = new ArrayList<StudentModel>();
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name, COALESCE(NULLIF(sa.isabsent,''), 'p') as getAttendance "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " LEFT JOIN " + TABLE_ATTENDANCE_STUDENT + " sa ON s." + KEY_ID + " = sa." + ATTENDANCE_STUDENT_ID
                    + " LEFT JOIN " + TABLE_ATTENDANCE + " a ON a." + KEY_ID + " = sa." + ATTENDANCE_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND strftime('%m',a.for_date) = '" + month + "'"
                    + " AND strftime('%Y',a.for_date) = '" + year + "'";
            if (classId > 0 && SectionId > 0) {
                selectQuery += " AND c." + KEY_ID + " = " + classId
                        + " AND cs." + KEY_ID + " = " + SectionId;
            }

            selectQuery += " ORDER BY s." + STUDENT_GR_NO;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1") ? true : false;
                    if (b) {
                        StudentModel sm = new StudentModel();
                        sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                        sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                        sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                        sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                        sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                        sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                        sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                        sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                        sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                        sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                        sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                        sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                        sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                        sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                        sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                        sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                        sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                        sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                        sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                        sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                        sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                        sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));

                        sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                        sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                        sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                        sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                        sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                        sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                        sm.setActive(b);
                        sm.setAttendanceStatus(cursor.getString(cursor.getColumnIndex("getAttendance")));
                        smList.add(sm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return smList;
    }

    public ArrayList<AttendanceModel> getAllAttendanceHeader(int SchoolId) {
        ArrayList<AttendanceModel> attendanceModels = new ArrayList<>();


        String query = "SELECT a.* FROM " + TABLE_ATTENDANCE + " a INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = a." + ATTENDANCE_SCHOOL_CLASS_ID +
                " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + SchoolId + " GROUP BY a." + ATTENDANCE_FOR_DATE + " ORDER BY a." + ATTENDANCE_FOR_DATE + " asc limit 30 ";
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    AttendanceModel attendanceModel = new AttendanceModel();
                    attendanceModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    attendanceModel.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                    attendanceModel.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                    attendanceModel.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                    attendanceModel.setUploadedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_UPLOADED_ON)));
                    attendanceModel.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                    attendanceModels.add(attendanceModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return attendanceModels;
    }


    public int getMaxCapacityForSchool(int SchoolId) {

        String query = "select sum(capacity) as max_capacity from school s inner join school_class sc on s.id = sc.school_id where s.id  = @SchoolId and sc.is_active = 1";
        query = query.replace("@SchoolId", SchoolId + "");
        int maxCapacity = 0;
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                maxCapacity = cursor.getInt(cursor.getColumnIndex("max_capacity"));
                return maxCapacity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return maxCapacity;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return maxCapacity;
    }

    public int getCapacityForSchool(int SchoolId, int SchoolClassId) {

        String query = "select capacity from school_class sc" +
                " where sc.school_id  = @SchoolId and sc.is_active = 1 and sc.id = @SchoolClassId";
        query = query.replace("@SchoolId", SchoolId + "");
        query = query.replace("@SchoolClassId", SchoolClassId + "");
        int capacity = 0;
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                capacity = cursor.getInt(cursor.getColumnIndex("capacity"));
                return capacity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return capacity;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return capacity;
    }

    public int getAllAttendanceHeaderCountForMonth(int SchoolId, String month, String year) {
        int count = 0;
        String query = "Select a.* from " + TABLE_ATTENDANCE + " a INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = a." + ATTENDANCE_SCHOOL_CLASS_ID +
                " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + SchoolId
                + " And strftime('%m', a." + ATTENDANCE_FOR_DATE + ") = '" + month + "'"
                + " And strftime('%Y', a." + ATTENDANCE_FOR_DATE + ") = '" + year + "'"
                + " GROUP BY a." + ATTENDANCE_FOR_DATE;
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
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

    public int getAllAttendanceHeaderCountmonthforClass(int schoolClassID, String month, String year) {
        int count = 0;
        String query = "Select * from attendance" +
                " WHERE school_class_id = " + schoolClassID +
                " And strftime('%m', for_date) = '" + month + "'" +
                " And strftime('%Y', for_date) = '" + year + "'" +
                " group by for_date";

        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
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

    public int getAllAttendanceHeaderCountYearforClass(int schoolClassID, String year) {
        int count = 0;
        String query = "Select * from attendance" +
                " WHERE school_class_id = " + schoolClassID +
                " And strftime('%Y', for_date) = '" + year + "'" +
                " group by for_date";

        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
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

    public int getAllAttendanceHeaderCountforClassOrSchool(int schoolID, int schoolClassID, String month, String year) {
        int count = 0;

        String selectquery = "Select * from attendance a" +
                " inner join school_class sc on sc.id=a.school_class_id" +
                " WHERE sc.school_id=" + schoolID +
                " And strftime('%m', a.for_date) = '" + month + "'" +
                " And strftime('%Y', a.for_date) = '" + year + "'";

        if (schoolClassID > 0) {
            selectquery += " And school_class_id =" + schoolClassID;
        }

        selectquery += " group by for_date";

        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(selectquery, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
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

    public int getStudenAttendancebyDate(int SchoolId, String Date) {
        int absentCount = 0;


        String query = "SELECT a.*, sc.class_id " +
                " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID +
                " LEFT JOIN " + TABLE_ATTENDANCE_STUDENT + " sa ON s." + KEY_ID + " = sa." + ATTENDANCE_STUDENT_ID +
                " LEFT JOIN " + TABLE_ATTENDANCE + " a ON a." + KEY_ID + " = sa." + ATTENDANCE_ID +
                " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + SchoolId
                + " AND s." + STUDENT_IS_ACTIVE + " = 1 "
                + " And a." + ATTENDANCE_FOR_DATE + " = '" + Date
                + "' Group By s." + KEY_ID;

        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                absentCount = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return absentCount;
    }

    public boolean AttendanceExists(int schoolId, String fordate) {
        String query = "SELECT * FROM " + TABLE_ATTENDANCE + " a " +
                " INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = a." + ATTENDANCE_SCHOOL_CLASS_ID +
                " WHERE a." + ATTENDANCE_FOR_DATE + " = '" + fordate + "'" +
                " AND sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public int getStudentAttendancebyMonth(int SchoolId, String month, String Year, String day) {
        int presentCount = 0;

        String query = "SELECT a.*, sc." + SCHOOL_CLASS_CLASSID + " FROM " + TABLE_STUDENT + " s"
                + " INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                + " LEFT JOIN " + TABLE_ATTENDANCE_STUDENT + " sa ON s." + KEY_ID + " = sa." + ATTENDANCE_STUDENT_ID
                + " LEFT JOIN " + TABLE_ATTENDANCE + " a ON a." + KEY_ID + " = sa." + ATTENDANCE_ID
                + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + SchoolId
                + " And strftime('%m', a." + ATTENDANCE_FOR_DATE + ") = '" + month + "'"
                + " And strftime('%Y', a." + ATTENDANCE_FOR_DATE + ") = '" + Year + "'";

        if (!day.equals("")) {
            query += " And strftime('%d', a." + ATTENDANCE_FOR_DATE + ") = '" + day + "'";
        }
        query += " And s." + STUDENT_IS_ACTIVE + " = 1";

        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                presentCount = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return presentCount;
    }

    public long editStudent(StudentModel student, int id) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(STUDENT_ENROLLMENT_DATE, student.getEnrollmentDate());
            values.put(STUDENT_GENDER, student.getGender());
            values.put(STUDENT_SCHOOL_CLASS_ID, student.getSchoolClassId());
            values.put(STUDENT_FATHERS_NAME, student.getFathersName());
            values.put(STUDENT_FATHER_NIC, student.getFatherNic());
            values.put(STUDENT_FATHER_OCCUPATION, student.getFatherOccupation());
            values.put(STUDENT_MOTHERS_NAME, student.getMotherName());
            values.put(STUDENT_MOTHERS_NIC, student.getMotherNic());
            values.put(STUDENT_MOTHERS_OCCUPATION, student.getMotherOccupation());
            values.put(STUDENT_GUARDIANS_NAME, student.getGuardianName());
            values.put(STUDENT_GUARDIANS_NIC, student.getGuardianNic());
            values.put(STUDENT_GUARDIANS_OCCUPATION, student.getGuardianOccupation());
            values.put(STUDENT_PREVIOUS_SCHOOL_NAME, student.getPreviousSchoolName());
            values.put(STUDENT_CLASS_PREVIOUS_SCHOOL, student.getPreviousSchoolClass());
            values.put(STUDENT_ADDRESS1, student.getAddress1());
            values.put(STUDENT_CONTACT_NUMBERS, student.getContactNumbers());
            values.put(STUDENT_FORM_B, student.getFormB());
            values.put(STUDENT_GR_NO, student.getGrNo());
            values.put(STUDENT_DOB, student.getDob());
            values.put(STUDENT_NAME, student.getName());
            values.put(STUDENT_Actual_Fees, student.getActualFees());
            values.put(STUDENT_ScholarshipCategory_ID, student.getScholarshipCategoryId());
            if (student.getPictureName() != null && !student.getPictureName().isEmpty()) {
                values.put(STUDENT_PICTURE_NAME, student.getPictureName());
                values.put(STUDENT_PICTURE_UPLOADED_ON, student.getPictureUploadedOn());
            }


            values.put(STUDENT_IS_ACTIVE, student.isActive());

            values.put(STUDENT_IS_ORPHAN, student.isOrphan());
            values.put(STUDENT_IS_DISABLED, student.isDisabled());
            values.put(STUDENT_RELIGION, student.getReligion());
            values.put(STUDENT_NATIONALITY, student.getNationality());
            values.put(STUDENT_EMAIL, student.getEmail());

            if (student.getElectiveSubjectId() > 0) {
                values.put(STUDENT_ELECTIVE_SUBJECT_ID, student.getElectiveSubjectId());
            }

            values.put(STUDENT_DEATH_CERT_IMAGE, student.getDeathCert_Image());
            values.put(STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON, student.getDeathCert_Image_UploadedOn());
            values.put(STUDENT_MEDICAL_CERT_IMAGE, student.getMedicalCert_Image());
            values.put(STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON, student.getMedicalCert_Image_UploadedOn());
            values.put(STUDENT_BFORM_IMAGE, student.getbForm_Image());
            values.put(STUDENT_BFORM_IMAGE_UPLOADED_ON, student.getbForm_Image_UploadedOn());
            values.put(STUDENT_PROMOTION_COMMENTS, student.getStudent_promotionComments());
            values.put(STUDENT_PROMOTION_STATUS, student.getStudent_promotionStatus());

            long i = DB.update(TABLE_STUDENT, values, KEY_ID + "=" + id, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addStudent(ArrayList<StudentModel> smList, int schoolId, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;

            for (StudentModel sm : smList) {
                if (sm.isActive() == null) {
                    sm.setActive(true);
                } else {
                    boolean isActive = sm.isActive();
                    sm.setActive(!isActive);
                }
                //Insert
//                if (!DatabaseHelper.getInstance(context).FindStudentRecord(sm.getId(), SurveyAppModel.getInstance().getSelectedSchool(context)))
                if (!FindStudentRecord(sm.getId(), schoolId)) {
                    sm.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                    ContentValues values = new ContentValues();
                    values.put(KEY_ID, sm.getId());
                    values.put(STUDENT_SERVER_ID, sm.getId());
                    values.put(STUDENT_NAME, sm.getName());
                    values.put(STUDENT_GENDER, sm.getGender());
                    values.put(STUDENT_GR_NO, sm.getGrNo());

                    if (sm.getEnrollmentDate() != null) {
                        sm.setEnrollmentDate(AppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), "dd/MM/yy", "yyyy-MM-dd"));
                        values.put(STUDENT_ENROLLMENT_DATE, sm.getEnrollmentDate());
                    }
                    if (sm.getDob() != null) {
                        sm.setDob(AppModel.getInstance().convertDatetoFormat(sm.getDob(), "dd/MM/yy", "yyyy-MM-dd"));
                        values.put(STUDENT_DOB, sm.getDob());
                    }

                    values.put(STUDENT_FORM_B, sm.getFormB());
                    values.put(STUDENT_FATHERS_NAME, sm.getFathersName());
                    values.put(STUDENT_FATHER_NIC, sm.getFatherNic());
                    values.put(STUDENT_FATHER_OCCUPATION, sm.getFatherOccupation());
                    values.put(STUDENT_MOTHERS_NAME, sm.getMotherName());
                    values.put(STUDENT_MOTHERS_NIC, sm.getMotherNic());
                    values.put(STUDENT_MOTHERS_OCCUPATION, sm.getMotherOccupation());
                    values.put(STUDENT_GUARDIANS_NAME, sm.getGuardianName());
                    values.put(STUDENT_GUARDIANS_NIC, sm.getGuardianNic());
                    values.put(STUDENT_GUARDIANS_OCCUPATION, sm.getGuardianOccupation());
                    values.put(STUDENT_PREVIOUS_SCHOOL_NAME, sm.getPreviousSchoolName());
                    values.put(STUDENT_CLASS_PREVIOUS_SCHOOL, sm.getPreviousSchoolClass());
                    values.put(STUDENT_ADDRESS1, sm.getAddress1());
                    values.put(STUDENT_ADDRESS2, sm.getAddress2());
                    values.put(STUDENT_CONTACT_NUMBERS, sm.getContactNumbers());
                    values.put(STUDENT_CURRENT_SESSION, sm.getCurrentSession());
                    values.put(STUDENT_MODIFIED_BY, sm.getModifiedBy());

                    if (sm.getPictureName() != null && !sm.getPictureName().isEmpty()) {
                        String picturePathName = sm.getPictureName().replace("\\", "/");
                        sm.setPictureName(picturePathName);
                    }
                    values.put(STUDENT_PICTURE_NAME, sm.getPictureName());
                    values.put(STUDENT_PICTURE_UPLOADED_ON, AppModel.getInstance().getDateTime());

                    if (sm.getModifiedOn() != null) {
                        String dateFormat = null;
                        if (sm.getModifiedOn().length() == 8)
                            dateFormat = "dd/MM/yy";
                        else
                            dateFormat = "dd/MM/yy HH:mm:ss";
                        sm.setModifiedOn(AppModel.getInstance().convertDatetoFormat(sm.getModifiedOn(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                        values.put(STUDENT_MODIFIED_ON, sm.getModifiedOn());
                        values.put(STUDENT_UPLOADED_ON, sm.getUploadedOn());
                    }

                    values.put(STUDENT_UPLOADED_ON, sm.getUploadedOn());

                    values.put(STUDENT_SCHOOL_CLASS_ID, sm.getSchoolClassId());
                    if (sm.getWithdrawnOn() != null) {
                        sm.setWithdrawnOn(AppModel.getInstance().convertDatetoFormat(sm.getWithdrawnOn(), "dd/MM/yy", "yyyy-MM-dd"));
                        values.put(STUDENT_WITHDRAWN_ON, sm.getWithdrawnOn());
                    }
                    values.put(STUDENT_IS_WITHDRAWN, sm.isWithdrawal());
                    values.put(STUDENT_WITHDRAWN_REASON_ID, sm.getWithdrawalReasonId());
                    values.put(STUDENT_IS_ACTIVE, sm.isActive());
                    values.put(STUDENT_IS_APPROVED, sm.isApproved());
                    values.put(STUDENT_APPROVED_BY, sm.getApproved_by());


                    values.put(STUDENT_Actual_Fees, sm.getActualFees());
                    values.put(STUDENT_ScholarshipCategory_ID, sm.getScholarshipCategoryId());
                    values.put(STUDENT_Maxallow_fees_admission, sm.getMax_allow_fees_admission());
                    values.put(STUDENT_Maxallow_copies, sm.getMax_allow_fees_copies());
                    values.put(STUDENT_Maxallow_fees_books, sm.getMax_allow_fees_books());
                    values.put(STUDENT_Maxallow_fees_exam, sm.getMax_allow_fees_exam());
                    values.put(STUDENT_Maxallow_others, sm.getMax_allow_fees_others());
                    values.put(STUDENT_Maxallow_fees_tution, sm.getMax_allow_fees_tution());
                    values.put(STUDENT_Maxallow_uniform, sm.getMax_allow_fees_uniform());

                    values.put(STUDENT_EMAIL, sm.getEmail());
                    values.put(STUDENT_RELIGION, sm.getReligion());
                    values.put(STUDENT_NATIONALITY, sm.getNationality());
                    if (sm.getElectiveSubjectId() == 0)
                        values.putNull(STUDENT_ELECTIVE_SUBJECT_ID);
                    else
                        values.put(STUDENT_ELECTIVE_SUBJECT_ID, sm.getElectiveSubjectId());
                    values.put(STUDENT_DEATH_CERT_IMAGE, sm.getDeathCert_Image());
                    values.put(STUDENT_MEDICAL_CERT_IMAGE, sm.getMedicalCert_Image());
                    values.put(STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDateTime());
                    values.put(STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDateTime());
                    values.put(STUDENT_BFORM_IMAGE, sm.getbForm_Image());
                    values.put(STUDENT_BFORM_IMAGE_UPLOADED_ON, AppModel.getInstance().getDateTime());
                    values.put(STUDENT_IS_ORPHAN, sm.isOrphan());
                    values.put(STUDENT_IS_DISABLED, sm.isDisabled());
                    values.put(STUDENT_PROMOTION_STATUS, sm.getStudent_promotionStatus());
                    values.put(STUDENT_PROMOTION_COMMENTS, sm.getStudent_promotionComments());

                    long i = DB.insert(TABLE_STUDENT, null, values);
                    if (i == -1)
                        AppModel.getInstance().appendLog(context, "Couldn't insert student with GR: " + sm.getGrNo());
                    else
                        AppModel.getInstance().appendLog(context, "Student inserted with GR: " + sm.getGrNo());

                    if (i > 0) {
                        downloadedCount++;
                    }


//                    return i;
                }
                //Update
                else {
//                    if (!DatabaseHelper.getInstance(context).IfStudentNotUploaded(sm.getId(), SurveyAppModel.getInstance().getSelectedSchool(context)))
                    if (!IfStudentNotUploaded(sm.getId(), schoolId)) {
                        sm.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long i = updateStudent(sm);

                        if (i > 0) {
                            downloadedCount++;
                        }

                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

//            removeStudentsNotExists(schoolId, smList);
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddStudents Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
//            return -1;
        } finally {
            DB.endTransaction();
        }
    }

    private void removeStudentsNotExists(final int schoolId, final ArrayList<StudentModel> serverStudentList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean studentFound;
                ArrayList<StudentModel> existingStudentsList = getAllStudentsList(schoolId, false);
                ArrayList<StudentModel> todo = new ArrayList<>();
                if (existingStudentsList != null && existingStudentsList.size() > 0) {
                    for (StudentModel existingStudent : existingStudentsList) {
                        studentFound = false;
                        for (StudentModel serverStudent : serverStudentList) {
                            if (existingStudent.equals(serverStudent)) {
                                studentFound = true;
                            }
                        }
                        if (!studentFound)
                            todo.add(existingStudent);
                    }
                }
                for (StudentModel model : todo) {
                    if (removeStudent(model)) {
                        AppModel.getInstance().appendLog(context, "Student Removed: id = " + model.getId() + " Gr = " + model.getGrNo());
                    } else {
                        AppModel.getInstance().appendLog(context, "Couldn't remove Student: id = " + model.getId() + " Gr = " + model.getGrNo());
                    }
                }
            }
        }).start();


    }

    public long reAdmitStudentUpdate(StudentModel student, int id) {
        SQLiteDatabase DB = getDB();
        Cursor cursor = null;
        try {
//            int id = -1;
//            String queryMaxId = "select max(id) as Id from Student";
//            cursor = db.rawQuery(queryMaxId, null);
//            if (cursor.moveToFirst()) {
//                id = cursor.getInt(cursor.getColumnIndex("Id"));
//                cursor.close();
//            }
            ContentValues values = new ContentValues();
            values.put(STUDENT_NAME, student.getName());
            values.put(STUDENT_GENDER, student.getGender());
            values.put(STUDENT_GR_NO, student.getGrNo());

            if (student.getEnrollmentDate() != null)
                values.put(STUDENT_ENROLLMENT_DATE, student.getEnrollmentDate());

            if (student.getDob() != null)
                values.put(STUDENT_DOB, student.getDob());


            values.put(STUDENT_FORM_B, student.getFormB());
            values.put(STUDENT_FATHERS_NAME, student.getFathersName());
            values.put(STUDENT_FATHER_NIC, student.getFatherNic());
            values.put(STUDENT_FATHER_OCCUPATION, student.getFatherOccupation());
            values.put(STUDENT_MOTHERS_NAME, student.getMotherName());
            values.put(STUDENT_MOTHERS_NIC, student.getMotherNic());
            values.put(STUDENT_MOTHERS_OCCUPATION, student.getMotherOccupation());
            values.put(STUDENT_GUARDIANS_NAME, student.getGuardianName());
            values.put(STUDENT_GUARDIANS_NIC, student.getGuardianNic());
            values.put(STUDENT_GUARDIANS_OCCUPATION, student.getGuardianOccupation());
            values.put(STUDENT_PREVIOUS_SCHOOL_NAME, student.getPreviousSchoolName());
            values.put(STUDENT_CLASS_PREVIOUS_SCHOOL, student.getPreviousSchoolClass());
            values.put(STUDENT_ADDRESS1, student.getAddress1());
            values.put(STUDENT_ADDRESS2, student.getAddress2());
            values.put(STUDENT_CONTACT_NUMBERS, student.getContactNumbers());
            values.put(STUDENT_CURRENT_SESSION, student.getCurrentSession());
            values.put(STUDENT_MODIFIED_BY, student.getModifiedBy());
            values.put(STUDENT_PICTURE_NAME, student.getPictureName());


//            if (student.getModifiedOn() != null) {
//                String dateFormat = null;
//                if (student.getModifiedOn().length() == 8)
//                    dateFormat = "dd/MM/yy";
//                else
//                    dateFormat = "dd/MM/yy HH:mm:ss";
//                student.setModifiedOn(SurveyAppModel.getInstance().convertDatetoFormat(student.getModifiedOn(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
//                values.put(STUDENT_MODIFIED_ON, student.getModifiedOn());
//            }

            values.put(STUDENT_SCHOOL_CLASS_ID, student.getSchoolClassId());
            values.put(STUDENT_Actual_Fees, student.getActualFees());
            if (student.getWithdrawnOn() != null) {
                student.setWithdrawnOn(AppModel.getInstance().convertDatetoFormat(student.getWithdrawnOn(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_WITHDRAWN_ON, student.getWithdrawnOn());
            }
            values.put(STUDENT_IS_WITHDRAWN, false);
            values.put(STUDENT_WITHDRAWN_REASON_ID, 0);
            values.put(STUDENT_IS_ACTIVE, student.isActive());
            values.put(STUDENT_PREVIOUS_STUDENT_ID, student.getPreviousStudentID());

//            long i = DB.insert(TABLE_STUDENT, null, values);
            long i = DB.update(TABLE_STUDENT, values, KEY_ID + "=" + id, null);
            if (i == -1) {
                AppModel.getInstance().appendLog(context, "Couldn't insert student with GR: " + student.getGrNo());
            } else {
                AppModel.getInstance().appendLog(context, "Student Readmitted with GR: " + student.getGrNo());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In AddStudents Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public long updateStudent(StudentModel student) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, student.getId());
            values.put(STUDENT_SERVER_ID, student.getId());
            values.put(STUDENT_NAME, student.getName());
            values.put(STUDENT_GENDER, student.getGender());
            values.put(STUDENT_GR_NO, student.getGrNo());
            if (student.getEnrollmentDate() != null) {
                student.setEnrollmentDate(AppModel.getInstance().convertDatetoFormat(student.getEnrollmentDate(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_ENROLLMENT_DATE, student.getEnrollmentDate());
            }
            if (student.getDob() != null) {
                student.setDob(AppModel.getInstance().convertDatetoFormat(student.getDob(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_DOB, student.getDob());
            }
            values.put(STUDENT_FORM_B, student.getFormB());
            values.put(STUDENT_FATHERS_NAME, student.getFathersName());
            values.put(STUDENT_FATHER_NIC, student.getFatherNic());
            values.put(STUDENT_FATHER_OCCUPATION, student.getFatherOccupation());
            values.put(STUDENT_MOTHERS_NAME, student.getMotherName());
            values.put(STUDENT_MOTHERS_NIC, student.getMotherNic());
            values.put(STUDENT_MOTHERS_OCCUPATION, student.getMotherOccupation());
            values.put(STUDENT_GUARDIANS_NAME, student.getGuardianName());
            values.put(STUDENT_GUARDIANS_NIC, student.getGuardianNic());
            values.put(STUDENT_GUARDIANS_OCCUPATION, student.getGuardianOccupation());
            values.put(STUDENT_PREVIOUS_SCHOOL_NAME, student.getPreviousSchoolName());
            values.put(STUDENT_CLASS_PREVIOUS_SCHOOL, student.getPreviousSchoolClass());
            values.put(STUDENT_ADDRESS1, student.getAddress1());
            values.put(STUDENT_ADDRESS2, student.getAddress2());
            values.put(STUDENT_CONTACT_NUMBERS, student.getContactNumbers());
            values.put(STUDENT_CURRENT_SESSION, student.getCurrentSession());
            values.put(STUDENT_MODIFIED_BY, student.getModifiedBy());
//            values.put(STUDENT_PICTURE_NAME, student.getPictureName());

            try {
                String picture_name = getStudentwithGRNoAndSCId(Integer.parseInt(student.getGrNo()), student.getSchoolClassId()).getPictureName();
                if (picture_name != null && !picture_name.isEmpty()) {
                    if (picture_name.contains("BodyPart")) {
                        if (student.getPictureName() != null && !student.getPictureName().isEmpty()) {
                            String picturePathName = student.getPictureName().replace("\\", "/");
                            student.setPictureName(picturePathName);
                        }
                        values.put(STUDENT_PICTURE_NAME, student.getPictureName());
                        values.put(STUDENT_PICTURE_UPLOADED_ON, AppModel.getInstance().getDateTime());
                    } else {
                        values.put(STUDENT_PICTURE_UPLOADED_ON, (String) null);
                    }
                } else {
                    if (student.getPictureName() != null && !student.getPictureName().isEmpty()) {
                        String picturePathName = student.getPictureName().replace("\\", "/");
                        student.setPictureName(picturePathName);
                    }
                    values.put(STUDENT_PICTURE_NAME, student.getPictureName());
                    values.put(STUDENT_PICTURE_UPLOADED_ON, AppModel.getInstance().getDateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (student.getModifiedOn() != null) {
                String dateFormat = null;
                if (student.getModifiedOn().length() == 8)
                    dateFormat = "dd/MM/yy";
                else
                    dateFormat = "dd/MM/yy HH:mm:ss";

                student.setModifiedOn(AppModel.getInstance().convertDatetoFormat(student.getModifiedOn(), dateFormat, "yyyy-MM-dd HH:mm:ss"));
                values.put(STUDENT_MODIFIED_ON, student.getModifiedOn());
                values.put(STUDENT_UPLOADED_ON, student.getUploadedOn());
            }


            values.put(STUDENT_UPLOADED_ON, student.getUploadedOn());


            values.put(STUDENT_SCHOOL_CLASS_ID, student.getSchoolClassId());
            if (student.getWithdrawnOn() != null) {
                student.setWithdrawnOn(AppModel.getInstance().convertDatetoFormat(student.getWithdrawnOn(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_WITHDRAWN_ON, student.getWithdrawnOn());
            }
            values.put(STUDENT_IS_WITHDRAWN, student.isWithdrawal());
            values.put(STUDENT_WITHDRAWN_REASON_ID, student.getWithdrawalReasonId());
            values.put(STUDENT_IS_ACTIVE, student.isActive());
            values.put(STUDENT_IS_APPROVED, student.isApproved());
            values.put(STUDENT_APPROVED_BY, student.getApproved_by());

            values.put(STUDENT_Actual_Fees, student.getActualFees());
            values.put(STUDENT_ScholarshipCategory_ID, student.getScholarshipCategoryId());
            values.put(STUDENT_Maxallow_fees_admission, student.getMax_allow_fees_admission());
            values.put(STUDENT_Maxallow_copies, student.getMax_allow_fees_copies());
            values.put(STUDENT_Maxallow_fees_books, student.getMax_allow_fees_books());
            values.put(STUDENT_Maxallow_fees_exam, student.getMax_allow_fees_exam());
            values.put(STUDENT_Maxallow_others, student.getMax_allow_fees_others());
            values.put(STUDENT_Maxallow_fees_tution, student.getMax_allow_fees_tution());
            values.put(STUDENT_Maxallow_uniform, student.getMax_allow_fees_uniform());

            values.put(STUDENT_EMAIL, student.getEmail());
            values.put(STUDENT_RELIGION, student.getReligion());
            values.put(STUDENT_NATIONALITY, student.getNationality());
            if (student.getElectiveSubjectId() == 0)
                values.putNull(STUDENT_ELECTIVE_SUBJECT_ID);
            else
                values.put(STUDENT_ELECTIVE_SUBJECT_ID, student.getElectiveSubjectId());
            values.put(STUDENT_DEATH_CERT_IMAGE, student.getDeathCert_Image());
            values.put(STUDENT_MEDICAL_CERT_IMAGE, student.getMedicalCert_Image());
            values.put(STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDateTime());
            values.put(STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDateTime());
            values.put(STUDENT_BFORM_IMAGE, student.getbForm_Image());
            values.put(STUDENT_BFORM_IMAGE_UPLOADED_ON, AppModel.getInstance().getDateTime());
            values.put(STUDENT_IS_ORPHAN, student.isOrphan());
            values.put(STUDENT_IS_DISABLED, student.isDisabled());
            values.put(STUDENT_PROMOTION_COMMENTS, student.getStudent_promotionComments());
            values.put(STUDENT_PROMOTION_STATUS, student.getStudent_promotionStatus());

            long i = DB.update(TABLE_STUDENT, values, KEY_ID + " =" + String.valueOf(student.getId()), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update student GR: " + student.getGrNo());
            } else {
                AppModel.getInstance().appendLog(context, "Student updated GR: " + student.getGrNo());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateStudents Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }


    public long updateFailedStudent(StudentModel student) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
//            values.put(KEY_ID, student.getId());
//            values.put(STUDENT_SERVER_ID, student.getId());
            values.put(STUDENT_NAME, student.getName());
            values.put(STUDENT_GENDER, student.getGender());
//            values.put(STUDENT_GR_NO, student.getGrNo());
            if (student.getEnrollmentDate() != null) {
                student.setEnrollmentDate(AppModel.getInstance().convertDatetoFormat(student.getEnrollmentDate(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_ENROLLMENT_DATE, student.getEnrollmentDate());
            }
            if (student.getDob() != null) {
                student.setDob(AppModel.getInstance().convertDatetoFormat(student.getDob(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_DOB, student.getDob());
            }
            values.put(STUDENT_FORM_B, student.getFormB());
            values.put(STUDENT_FATHERS_NAME, student.getFathersName());
            values.put(STUDENT_FATHER_NIC, student.getFatherNic());
            values.put(STUDENT_FATHER_OCCUPATION, student.getFatherOccupation());
            values.put(STUDENT_MOTHERS_NAME, student.getMotherName());
            values.put(STUDENT_MOTHERS_NIC, student.getMotherNic());
            values.put(STUDENT_MOTHERS_OCCUPATION, student.getMotherOccupation());
            values.put(STUDENT_GUARDIANS_NAME, student.getGuardianName());
            values.put(STUDENT_GUARDIANS_NIC, student.getGuardianNic());
            values.put(STUDENT_GUARDIANS_OCCUPATION, student.getGuardianOccupation());
            values.put(STUDENT_PREVIOUS_SCHOOL_NAME, student.getPreviousSchoolName());
            values.put(STUDENT_CLASS_PREVIOUS_SCHOOL, student.getPreviousSchoolClass());
            values.put(STUDENT_ADDRESS1, student.getAddress1());
            values.put(STUDENT_ADDRESS2, student.getAddress2());
            values.put(STUDENT_CONTACT_NUMBERS, student.getContactNumbers());
            values.put(STUDENT_CURRENT_SESSION, student.getCurrentSession());
            values.put(STUDENT_MODIFIED_BY, student.getModifiedBy());
//            values.put(STUDENT_PICTURE_NAME, student.getPictureName());

            if (student.getModifiedOn() != null) {
                String dateFormat = null;
                if (student.getModifiedOn().length() == 8)
                    dateFormat = "dd/MM/yy";
                else
                    dateFormat = "dd/MM/yy HH:mm:ss";

                student.setModifiedOn(AppModel.getInstance().convertDatetoFormat(student.getModifiedOn(), dateFormat, "yyyy-MM-dd HH:mm:ss"));
                values.put(STUDENT_MODIFIED_ON, student.getModifiedOn());
                values.put(STUDENT_UPLOADED_ON, student.getUploadedOn());
            }


            values.put(STUDENT_UPLOADED_ON, student.getUploadedOn());


            values.put(STUDENT_SCHOOL_CLASS_ID, student.getSchoolClassId());
            if (student.getWithdrawnOn() != null) {
                student.setWithdrawnOn(AppModel.getInstance().convertDatetoFormat(student.getWithdrawnOn(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(STUDENT_WITHDRAWN_ON, student.getWithdrawnOn());
            }
            values.put(STUDENT_IS_WITHDRAWN, student.isWithdrawal());
            values.put(STUDENT_WITHDRAWN_REASON_ID, student.getWithdrawalReasonId());
            values.put(STUDENT_IS_ACTIVE, !student.isActive());
            values.put(STUDENT_IS_APPROVED, student.isApproved());
            values.put(STUDENT_APPROVED_BY, student.getApproved_by());

            values.put(STUDENT_Actual_Fees, student.getActualFees());
            values.put(STUDENT_ScholarshipCategory_ID, student.getScholarshipCategoryId());
            values.put(STUDENT_Maxallow_fees_admission, student.getMax_allow_fees_admission());
            values.put(STUDENT_Maxallow_copies, student.getMax_allow_fees_copies());
            values.put(STUDENT_Maxallow_fees_books, student.getMax_allow_fees_books());
            values.put(STUDENT_Maxallow_fees_exam, student.getMax_allow_fees_exam());
            values.put(STUDENT_Maxallow_others, student.getMax_allow_fees_others());
            values.put(STUDENT_Maxallow_fees_tution, student.getMax_allow_fees_tution());
            values.put(STUDENT_Maxallow_uniform, student.getMax_allow_fees_uniform());

            long i = DB.update(TABLE_STUDENT, values, KEY_ID + " =" + String.valueOf(student.getId()), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update student GR: " + student.getGrNo());
            } else {
                AppModel.getInstance().appendLog(context, "Student updated GR: " + student.getGrNo());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateStudents Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }


    public long updateStudentByColumns(ContentValues cv, int id) {
        SQLiteDatabase DB = getDB();
        try {
            long i = DB.update(TABLE_STUDENT, cv, KEY_ID + "=" + id, null);
            AppModel.getInstance().appendLog(context, "In updateStudentByColumns Method. Student uploadedOn field updated. id return:" + i);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Exception occured while updating the student record" + id);
            return -1;
        }
    }

    //TABLE_ENROLLMENT

    public int getPendingEnrollmentscounts(int SchoolId) {
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_ENROLLMENT + " WHERE " + ENROLLMENT_REVIEW_STATUS + " = '"
                    + AppConstants.PROFILE_REJECTED_KEY + "' And " + ENROLLMENT_SCHOOL_ID + " = " + SchoolId;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                return cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return -1;
    }

    public long FindGRNOSTUDENTPROFILE(String value, String schoolId) {

        String query = "Select * from student s inner join school_class sc on sc.id = s.schoolclass_id " +
                " where s.student_gr_no = " + value + " AND sc.school_id = " + schoolId;

        SQLiteDatabase db = getDB();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0 && c.moveToFirst()) {
            c.close();
            return 1;
        } else {
            query = "SELECT * FROM " + TABLE_ENROLLMENT + " WHERE " + ENROLLMENT_GR_NO + " = " + value +
                    " AND " + ENROLLMENT_SCHOOL_ID + " = " + schoolId;
            c = db.rawQuery(query, null);
            if (c.getCount() > 0 && c.moveToFirst()) {
                return 1;
            } else {
                c.close();
                return -1;
            }
        }
    }


    public int getNewEnrollmentCount(int schoolid, int class_sectionId) {
        String query = "select e.*,count(e.id) as new_enrollments_count from enrollment e" +
                " where e.school_id=" + schoolid + " and e.class_section_id=" + class_sectionId +
                " and e.review_status NOT IN ('R')";
        SQLiteDatabase db = getDB();
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("new_enrollments_count"));
        }
        return count;
    }

    public int getMaxCapacityFromSchoolClass(int schoolid, int schoolClass_id) {
        String query = "select max_capacity from school_class\n" +
                "where school_id=" + schoolid + " and is_active = 1 and id=" + schoolClass_id;

        SQLiteDatabase db = getDB();
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("max_capacity"));
        }
        return count;
    }


    public long insertEnrollment(EnrollmentModel em) {
        long lastID;

        SQLiteDatabase db = getDB();
        ContentValues values = new ContentValues();
        values.put(ENROLLMENT_GR_NO, em.getENROLLMENT_GR_NO());
        values.put(ENROLLMENT_SCHOOL_ID, em.getENROLLMENT_SCHOOL_ID());
        values.put(ENROLLMENT_CREATED_BY, em.getENROLLMENT_CREATED_BY());
        values.put(ENROLLMENT_CREATED_ON, em.getENROLLMENT_CREATED_ON());
        values.put(ENROLLMENT_REVIEW_STATUS, em.getENROLLMENT_REVIEW_STATUS());
        values.put(ENROLLMENT_REVIEW_COMMENTS, em.getENROLLMENT_REVIEW_COMMENTS());
        values.put(ENROLLMENT_REVIEW_COMPLETED_ON, em.getENROLLMENT_REVIEW_COMPLETED_ON());
        values.put(ENROLLMENT_STUDENT_ID, em.getStudentId());
        values.put(ENROLLMENT_DATA_DOWNLOADED_ON, em.getENROLLMENT_DATA_DOWNLOADED_ON());
        values.put(ENROLLMENT_NAME, em.getStudentName());
        values.put(ENROLLMENT_CLASS_SECTION_ID, em.getClass_section_id());
        values.put(ENROLLMENT_GENDER, em.getGender());
        values.put(ENROLLMENT_MONTHLY_FEE, em.getMonthly_fee());
        values.put(ENROLLMENT_MODIFIED_BY, getCurrentLoggedInUser().getId());
        values.put(ENROLLMENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd"));
        lastID = db.insert(TABLE_ENROLLMENT, null, values);
        return lastID;
    }

    public long insertDownloadedEnrollment(EnrollmentModel em) {
        long lastID;
        SQLiteDatabase db = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ENROLLMENT_GR_NO, em.getENROLLMENT_GR_NO());
            values.put(ENROLLMENT_SCHOOL_ID, em.getENROLLMENT_SCHOOL_ID());
            values.put(ENROLLMENT_CREATED_BY, em.getENROLLMENT_CREATED_BY());
            values.put(ENROLLMENT_GENDER, em.getGender());

            if (em.getENROLLMENT_CREATED_ON() != null) {
                em.setENROLLMENT_CREATED_ON(AppModel.getInstance().convertDatetoFormat(em.getENROLLMENT_CREATED_ON(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ENROLLMENT_CREATED_ON, em.getENROLLMENT_CREATED_ON());
                values.put(ENROLLMENT_UPLOADED_ON, em.getENROLLMENT_CREATED_ON());
            }

            if (em.getENROLLMENT_REVIEW_STATUS() != null)
                values.put(ENROLLMENT_REVIEW_STATUS, em.getENROLLMENT_REVIEW_STATUS());
            values.put(ENROLLMENT_REVIEW_COMMENTS, em.getENROLLMENT_REVIEW_COMMENTS());

            if (em.getENROLLMENT_REVIEW_COMPLETED_ON() != null) {
                em.setENROLLMENT_REVIEW_COMPLETED_ON(AppModel.getInstance().convertDatetoFormat(em.getENROLLMENT_REVIEW_COMPLETED_ON(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ENROLLMENT_REVIEW_COMPLETED_ON, em.getENROLLMENT_REVIEW_COMPLETED_ON());
            }

            if (em.getENROLLMENT_DATA_DOWNLOADED_ON() != null) {
                em.setENROLLMENT_DATA_DOWNLOADED_ON(AppModel.getInstance().convertDatetoFormat(em.getENROLLMENT_DATA_DOWNLOADED_ON(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ENROLLMENT_DATA_DOWNLOADED_ON, em.getENROLLMENT_DATA_DOWNLOADED_ON());
            }

            values.put(ENROLLMENT_NAME, em.getStudentName());
            values.put(ENROLLMENT_CLASS_SECTION_ID, em.getClass_section_id());

            values.put(ENROLLMENT_SERVER_ID, em.getID());
            values.put(ENROLLMENT_MONTHLY_FEE, em.getMonthly_fee());
            values.put(ENROLLMENT_MODIFIED_BY, getCurrentLoggedInUser().getId());
//            values.put(ENROLLMENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd"));
            if (em.getModified_on() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                em.setModified_on(AppModel.getInstance().convertDatetoFormat(em.getModified_on(), dateFormat, "yyyy-MM-dd"));
                values.put(ENROLLMENT_MODIFIED_ON, em.getModified_on());
            }

            lastID = db.insert(TABLE_ENROLLMENT, null, values);
            if (lastID == -1) {
                AppModel.getInstance().appendErrorLog(context, "Couldn't insert enrollment with GR: " + em.getENROLLMENT_GR_NO());
            } else {
                AppModel.getInstance().appendLog(context, "Enrollment inserted with GR: " + em.getENROLLMENT_GR_NO());
            }
            return lastID;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In insert Enrollment Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }


    }

    public long updateDownloadEnrollment(EnrollmentModel em, int id) {
        long lastID;
        SQLiteDatabase db = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ENROLLMENT_GR_NO, em.getENROLLMENT_GR_NO());
            values.put(ENROLLMENT_GENDER, em.getGender());
            values.put(ENROLLMENT_SCHOOL_ID, em.getENROLLMENT_SCHOOL_ID());
            values.put(ENROLLMENT_CREATED_BY, em.getENROLLMENT_CREATED_BY());
            if (em.getENROLLMENT_CREATED_ON() != null) {
                em.setENROLLMENT_CREATED_ON(AppModel.getInstance().convertDatetoFormat(em.getENROLLMENT_CREATED_ON(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ENROLLMENT_CREATED_ON, em.getENROLLMENT_CREATED_ON());
            }
            if (em.getENROLLMENT_REVIEW_STATUS() != null && !em.getENROLLMENT_REVIEW_STATUS().equals("")) {
                values.put(ENROLLMENT_REVIEW_STATUS, em.getENROLLMENT_REVIEW_STATUS());
                values.put(ENROLLMENT_REVIEW_COMMENTS, em.getENROLLMENT_REVIEW_COMMENTS());
            }

            if (em.getENROLLMENT_REVIEW_COMPLETED_ON() != null) {
                em.setENROLLMENT_REVIEW_COMPLETED_ON(AppModel.getInstance().convertDatetoFormat(em.getENROLLMENT_REVIEW_COMPLETED_ON(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ENROLLMENT_REVIEW_COMPLETED_ON, em.getENROLLMENT_REVIEW_COMPLETED_ON());
            }
            if (em.getENROLLMENT_DATA_DOWNLOADED_ON() != null) {
                em.setENROLLMENT_DATA_DOWNLOADED_ON(AppModel.getInstance().convertDatetoFormat(em.getENROLLMENT_DATA_DOWNLOADED_ON(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ENROLLMENT_DATA_DOWNLOADED_ON, em.getENROLLMENT_DATA_DOWNLOADED_ON());
            }


            values.put(ENROLLMENT_NAME, em.getStudentName());
            values.put(ENROLLMENT_CLASS_SECTION_ID, em.getClass_section_id());
            values.put(ENROLLMENT_MONTHLY_FEE, em.getMonthly_fee());


            values.put(ENROLLMENT_MODIFIED_BY, getCurrentLoggedInUser().getId());
            if (em.getModified_on() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                em.setModified_on(AppModel.getInstance().convertDatetoFormat(em.getModified_on(), dateFormat, "yyyy-MM-dd"));
                values.put(ENROLLMENT_MODIFIED_ON, em.getModified_on());
            }


            lastID = db.update(TABLE_ENROLLMENT, values, ENROLLMENT_SERVER_ID + "=" + id, null);
            if (lastID == 0)
                AppModel.getInstance().appendErrorLog(context, "Couldn't update enrollment with GR: " + em.getENROLLMENT_GR_NO());
            else
                AppModel.getInstance().appendLog(context, "Enrollment updated with GR: " + em.getENROLLMENT_GR_NO());

            return lastID;

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In update Enrollment Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteEnrollment(long id) {
        SQLiteDatabase db = getDB();
        db.delete(TABLE_ENROLLMENT, KEY_ID + "=" + id, null);
    }

    public int updateEnrollment(EnrollmentModel em, long id) {
        SQLiteDatabase db = getDB();
        ContentValues values = new ContentValues();
        values.put(ENROLLMENT_NAME, em.getStudentName());
        values.put(ENROLLMENT_GENDER, em.getGender());
        values.put(ENROLLMENT_CLASS_SECTION_ID, em.getClass_section_id());
        values.put(ENROLLMENT_GR_NO, em.getENROLLMENT_GR_NO());
        values.put(ENROLLMENT_SCHOOL_ID, em.getENROLLMENT_SCHOOL_ID());
        values.put(ENROLLMENT_REVIEW_STATUS, em.getENROLLMENT_REVIEW_STATUS());
        values.put(ENROLLMENT_UPLOADED_ON, em.getENROLLMENT_UPLOADED_ON());
        values.put(ENROLLMENT_MONTHLY_FEE, em.getMonthly_fee());


        values.put(ENROLLMENT_MODIFIED_BY, getCurrentLoggedInUser().getId());
        values.put(ENROLLMENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd"));

        int a = db.update(TABLE_ENROLLMENT, values, KEY_ID + "=" + id, null);
        return a;
    }

    public long deleteApprovedRecordsEnrollment() {
        String query = "Delete from enrollment where review_status NOT IN ('R','I','C')";
        Cursor c = null;
        try {
            SQLiteDatabase db = getDB();
            c = db.rawQuery(query, null);
            if (c != null)
                return c.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return -1;
    }

    public ArrayList<EnrollmentModel> getIncompleteEnrollments(int schoolId) {
        ArrayList<EnrollmentModel> enrModel = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM " + TABLE_ENROLLMENT + " WHERE (" + ENROLLMENT_REVIEW_STATUS
                    + " NOT IN ('E','A') OR "
                    + ENROLLMENT_REVIEW_STATUS + " IS NULL OR "
                    + ENROLLMENT_REVIEW_STATUS + " ='') "
                    + "AND " + ENROLLMENT_SCHOOL_ID + " = " + schoolId
                    + " ORDER BY CASE WHEN " + ENROLLMENT_REVIEW_STATUS + " ='R'"
                    + " THEN 1 ELSE CASE WHEN " + ENROLLMENT_REVIEW_STATUS + " ='I'"
                    + " THEN 2 ELSE CASE WHEN " + ENROLLMENT_REVIEW_STATUS + " = 'C'"
                    + " THEN 3 ELSE 4 END END END";
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentModel model = new EnrollmentModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    model.setENROLLMENT_GR_NO(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_GR_NO)));
                    model.setENROLLMENT_SCHOOL_ID(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_SCHOOL_ID)));
                    model.setENROLLMENT_CREATED_BY(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_CREATED_BY)));
                    model.setENROLLMENT_CREATED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_CREATED_ON)));
                    model.setENROLLMENT_REVIEW_STATUS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_STATUS)));
                    model.setENROLLMENT_REVIEW_COMMENTS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_COMMENTS)));
                    model.setENROLLMENT_REVIEW_COMPLETED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_COMPLETED_ON)));
                    model.setENROLLMENT_DATA_DOWNLOADED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_DATA_DOWNLOADED_ON)));
                    model.setENROLLMENT_UPLOADED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_UPLOADED_ON)));
                    model.setServer_id(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_SERVER_ID)));
                    model.setStudentName(cursor.getString(cursor.getColumnIndex(ENROLLMENT_NAME)));
                    model.setClass_section_id(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_CLASS_SECTION_ID)));
                    model.setGender(cursor.getString(cursor.getColumnIndex(ENROLLMENT_GENDER)));

                    enrModel.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return enrModel;
    }


    public ArrayList<EnrollmentModel> getAllEnrollments() {
        ArrayList<EnrollmentModel> enrModel = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_ENROLLMENT
                    + " WHERE (" + ENROLLMENT_UPLOADED_ON + " IS NULL OR " + ENROLLMENT_UPLOADED_ON + " = '') "
                    + " AND (" + ENROLLMENT_REVIEW_STATUS + " NOT IN ('I'))"
                    + " AND id not in (select enrollment_id from enrollment_image where uploaded_on is null OR uploaded_on = '')";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    //todo get student id in enrollment table

                    EnrollmentModel model = new EnrollmentModel();
                    model.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    model.setServer_id(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_SERVER_ID)));
                    model.setENROLLMENT_GR_NO(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_GR_NO)));
                    model.setENROLLMENT_SCHOOL_ID(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_SCHOOL_ID)));
                    model.setENROLLMENT_CREATED_BY(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_CREATED_BY)));
                    model.setENROLLMENT_CREATED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_CREATED_ON)));
                    model.setENROLLMENT_REVIEW_STATUS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_STATUS)));
                    model.setStudentId(cursor.getString(cursor.getColumnIndex(ENROLLMENT_STUDENT_ID)));
                    model.setENROLLMENT_REVIEW_COMMENTS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_COMMENTS)));
                    model.setENROLLMENT_REVIEW_COMPLETED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_COMPLETED_ON)));
                    model.setStudentName(cursor.getString(cursor.getColumnIndex(ENROLLMENT_NAME)));

                    model.setClass_section_id(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_CLASS_SECTION_ID)));
                    model.setGender(cursor.getString(cursor.getColumnIndex(ENROLLMENT_GENDER)));
                    model.setMonthly_fee(cursor.getDouble(cursor.getColumnIndex(ENROLLMENT_MONTHLY_FEE)));

                    model.setModified_by(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_MODIFIED_BY)));
                    model.setModified_on(cursor.getString(cursor.getColumnIndex(ENROLLMENT_MODIFIED_ON)));

                    enrModel.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return enrModel;
    }

    //TABLE_ENROLLMENT_IMAGE
    public long insertEnrollmentImage(EnrollmentImageModel eim) {
        SQLiteDatabase db = getDB();
        String fileName = eim.getFilename() != null ? new File(eim.getFilename()).getName() : null;
        try {
            ContentValues values = new ContentValues();
            values.put(ENROLLMENT_ID, eim.getEnrollment_id());
            values.put(ENROLLMENT_IMAGE_FILENAME, fileName);
//            values.put(ENROLLMENT_IMAGE_FILENAME, eim.getFilename());
            values.put(ENROLLMENT_IMAGE_UPLOADED_ON, eim.getUploaded_on());
            values.put(ENROLLMENT_IMAGE_REVIEW_STATUS, eim.getReview_status());
            values.put(ENROLLMENT_FILE_TYPE, eim.getFiletype());
            long i = db.insert(TABLE_ENROLLMENT_IMAGE, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertDownloadedEnrollmentImage(EnrollmentImageModel eim, int headerId) {
        SQLiteDatabase db = getDB();
        try {
            String fdir = StorageUtil.getSdCardPath(context) + "/TCF/TCF Images/";
            String name = fdir + eim.getFilename();

            ContentValues values = new ContentValues();
            values.put(ENROLLMENT_ID, headerId);
            values.put(ENROLLMENT_IMAGE_FILENAME, eim.getFilename());
            values.put(ENROLLMENT_IMAGE_REVIEW_STATUS, eim.getReview_status());
            values.put(ENROLLMENT_FILE_TYPE, eim.getFiletype());

            String uploadedOn = AppModel.getInstance().getDate();
            values.put(ENROLLMENT_IMAGE_UPLOADED_ON, uploadedOn);
            long i = db.insert(TABLE_ENROLLMENT_IMAGE, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert enrollmentImage with id:" + headerId);
            else
                AppModel.getInstance().appendLog(context, "EnrollmentImage inserted with id:" + headerId);

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In insertDownloadedEnrollmentImage Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public int updateEnrollmentImage(EnrollmentImageModel eim) {
        String fileName = eim.getFilename() != null ? new File(eim.getFilename()).getName() : null;
        SQLiteDatabase db = getDB();
        ContentValues values = new ContentValues();
        values.put(ENROLLMENT_IMAGE_FILENAME, fileName);
//        values.put(ENROLLMENT_IMAGE_FILENAME, eim.getFilename());
        values.put(ENROLLMENT_IMAGE_UPLOADED_ON, eim.getUploaded_on());
        values.put(ENROLLMENT_IMAGE_REVIEW_STATUS, eim.getReview_status());
        values.put(ENROLLMENT_FILE_TYPE, eim.getFiletype());
        int a = db.update(TABLE_ENROLLMENT_IMAGE, values, ENROLLMENT_ID + " = '" + eim.getEnrollment_id() + "'" + " AND " + ENROLLMENT_FILE_TYPE + " = '" + eim.getFiletype() + "'", null);
        return a;
    }

    public int updateDownloadedEnrollmentImage(EnrollmentImageModel eim, int headerId) {
        SQLiteDatabase db = getDB();
        try {
            String fdir = StorageUtil.getSdCardPath(context) + "/TCF/TCF Images/";
            ContentValues values = new ContentValues();
            values.put(ENROLLMENT_ID, headerId);
            values.put(ENROLLMENT_IMAGE_FILENAME, eim.getFilename());
            values.put(ENROLLMENT_IMAGE_REVIEW_STATUS, eim.getReview_status());
            values.put(ENROLLMENT_FILE_TYPE, eim.getFiletype());
            int i = db.update(TABLE_ENROLLMENT_IMAGE, values, ENROLLMENT_ID + "=" + headerId + " AND " + ENROLLMENT_FILE_TYPE + " = '" + eim.getFiletype() + "'", null);
            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update EnrollmentImage with id:" + eim.getEnrollment_id());
            } else {
                AppModel.getInstance().appendLog(context, "EnrollmentImage updated with id:" + eim.getEnrollment_id());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In updateDownloadedEnrollmentImage Method. exception occurred: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public int getEnrollmentIdfromGR(int grnum, int schoolId) {
        Cursor cursor = null;
        int enrollmentId = -1;
        try {
            String query = "SELECT * FROM " + TABLE_ENROLLMENT + " WHERE " + ENROLLMENT_GR_NO + "=" + "'" + grnum + "' AND "
                    + ENROLLMENT_SCHOOL_ID + " = " + schoolId;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                enrollmentId = cursor.getInt(cursor.getColumnIndex(KEY_ID));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return enrollmentId;
    }

    public EnrollmentModel getEnrollmentfromGR(int grnum, int schoolId) {
        Cursor cursor = null;
        EnrollmentModel em = new EnrollmentModel();
        int enrollmentId = -1;
        try {
            String query = "SELECT * FROM " + TABLE_ENROLLMENT + " WHERE " + ENROLLMENT_GR_NO + "=" + "'" + grnum + "' AND "
                    + ENROLLMENT_SCHOOL_ID + " = " + schoolId;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                em.setMonthly_fee(cursor.getDouble(cursor.getColumnIndex(ENROLLMENT_MONTHLY_FEE)));
                em.setENROLLMENT_REVIEW_STATUS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_STATUS)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return em;
    }

    public EnrollmentImageModel getEnrollmentImage(int id, String filetype) {
        EnrollmentImageModel model = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_ENROLLMENT_IMAGE + " WHERE " + ENROLLMENT_ID + "=" + "'" + id + "'" + " AND " + ENROLLMENT_FILE_TYPE + "=" + "'" + filetype + "'";
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                model = new EnrollmentImageModel(
                        cursor.getInt(cursor.getColumnIndex(ENROLLMENT_ID)),
                        cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_FILENAME)),
                        cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_UPLOADED_ON)),
                        cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_REVIEW_STATUS)),
                        cursor.getString(cursor.getColumnIndex(ENROLLMENT_FILE_TYPE)));

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

    public ArrayList<EnrollmentImageModel> getAllEnrollmentImage() {
        ArrayList<EnrollmentImageModel> eimList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT ei.* FROM " + TABLE_ENROLLMENT + " e LEFT JOIN "
                    + TABLE_ENROLLMENT_IMAGE + " ei ON e." + KEY_ID + " = ei." + ENROLLMENT_ID + " "
                    + "WHERE (ei." + ENROLLMENT_IMAGE_UPLOADED_ON + " IS NULL OR ei." + ENROLLMENT_IMAGE_UPLOADED_ON + " = '')";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentImageModel eim = new EnrollmentImageModel();
                    eim.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    eim.setEnrollment_id(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_ID)));
                    eim.setFilename(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_FILENAME)));
                    eim.setUploaded_on(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_UPLOADED_ON)));
                    eim.setReview_status(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_REVIEW_STATUS)));
                    eim.setFiletype(cursor.getString(cursor.getColumnIndex(ENROLLMENT_FILE_TYPE)));
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

    public ArrayList<StudentImageModel> getStudentImageForUpload(int schoolId) {
        ArrayList<StudentImageModel> smList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM student"
                    + " WHERE schoolclass_id IN (select id from school_class where school_id IN (@SchoolId))"
                    + " AND (ifnull(image_file_uploaded_on,'') == '') AND (ifnull(student.image_file_name,'') != '' AND student.image_file_name not like '%BodyPart%')";

            query = query.replace("@SchoolId", schoolId + "");

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    StudentImageModel sim = new StudentImageModel();
                    sim.setStudent_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sim.setFilename(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                    sim.setUploaded_on(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_UPLOADED_ON)));
                    smList.add(sim);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return smList;
    }

    public ArrayList<EnrollmentImageUploadModel> getAllEnrollmentImagesForUpload(int id) {
        ArrayList<EnrollmentImageUploadModel> eimList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT DISTINCT ei.* FROM " + TABLE_ENROLLMENT_IMAGE + " ei, " + TABLE_ENROLLMENT + " e "
                    + "WHERE ei." + ENROLLMENT_ID + " = " + id;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentImageUploadModel eim = new EnrollmentImageUploadModel();
                    eim.setFilename(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_FILENAME)));
                    eim.setFiletype(cursor.getString(cursor.getColumnIndex(ENROLLMENT_FILE_TYPE)));
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

    //TABLE_PROMOTION_STUDENT METHODS
    public long insertPromotionStudent(PromotionStudentDBModel psm) {
        SQLiteDatabase db = getDB();
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(PROMOTION_ID, psm.getPromotion_id());
            values.put(PROMOTION_STUDENT_STUDENT_ID, psm.getStudent_id());
            values.put(PROMOTION_STUDENT_NEW_SCHOOLCLASS_ID, psm.getNew_schoolclass_id());

            id = db.insert(TABLE_PROMOTION_STUDENT, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public long addPromotionStudent(PromotionStudentDBModel psm, int promotionId) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(PROMOTION_ID, promotionId);
            values.put(PROMOTION_STUDENT_STUDENT_ID, psm.getStudent_id());
            values.put(PROMOTION_STUDENT_NEW_SCHOOLCLASS_ID, psm.getNew_schoolclass_id());

            long i = DB.insert(TABLE_PROMOTION_STUDENT, null, values);
            AppModel.getInstance().appendLog(context, "In addPromotionStudent Method. Promotion Student inserted with id:" + i);
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendLog(context, "In addPromotionStudent Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long updatePromotionStudent(PromotionStudentDBModel psm, int promotionId) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(PROMOTION_ID, promotionId);
            values.put(PROMOTION_STUDENT_STUDENT_ID, psm.getStudent_id());
            values.put(PROMOTION_STUDENT_NEW_SCHOOLCLASS_ID, psm.getNew_schoolclass_id());

            int i = DB.update(TABLE_PROMOTION_STUDENT, values, PROMOTION_ID + " = " + promotionId + " AND " + PROMOTION_STUDENT_STUDENT_ID + " =" + psm.getStudent_id(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //TABLE_ATTENDANCE
    public long addAttendance(AttendanceModel am) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_FOR_DATE, am.getForDate());
            values.put(ATTENDANCE_CREATED_BY, am.getCreatedBy());
            values.put(ATTENDANCE_CREATED_ON, am.getCreatedOn());
            values.put(ATTENDANCE_SCHOOL_CLASS_ID, am.getSchoolId());
            values.put(ATTENDANCE_MODIFIED_BY, getCurrentLoggedInUser().getId());
            values.put(ATTENDANCE_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

            long i = DB.insert(TABLE_ATTENDANCE, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long addDownloadedAttendance(AttendanceModel am) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            if (am.getForDate() != null) {
                am.setForDate(AppModel.getInstance().convertDatetoFormat(am.getForDate(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ATTENDANCE_FOR_DATE, am.getForDate());
            }
            values.put(ATTENDANCE_CREATED_BY, am.getCreatedBy());

            if (am.getCreatedOn() != null) {
                am.setCreatedOn(AppModel.getInstance().convertDatetoFormat(am.getCreatedOn(), "dd/MM/yy", "yyyy-MM-dd hh:mm:ss a"));
                values.put(ATTENDANCE_CREATED_ON, am.getCreatedOn());

                values.put(ATTENDANCE_UPLOADED_ON, am.getCreatedOn());
            }


            values.put(ATTENDANCE_SCHOOL_CLASS_ID, am.getSchoolId());
            values.put(ATTENDANCE_SERVER_ID, am.getId());
            values.put(ATTENDANCE_MODIFIED_BY, getCurrentLoggedInUser().getId());
//            values.put(ATTENDANCE_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd"));
            if (am.getModified_on() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                am.setModified_on(AppModel.getInstance().convertDatetoFormat(am.getModified_on(), dateFormat, "yyyy-MM-dd hh:mm:ss a"));
                values.put(ATTENDANCE_MODIFIED_ON, am.getModified_on());
            }

            long i = DB.insert(TABLE_ATTENDANCE, null, values);
            if (i == -1)
                AppModel.getInstance().appendLog(context, "Couldn't insert Attendance for date: " + am.getForDate());
            else
                AppModel.getInstance().appendLog(context, "Attendance inserted for date: " + am.getForDate());

            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In insert Attendance Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public int findAttendanceHeaderId(String Fordate, String SchoolClassID) {
        String Query = "SELECT " + KEY_ID + " FROM " + TABLE_ATTENDANCE + " WHERE " + ATTENDANCE_SCHOOL_CLASS_ID + " = " + SchoolClassID +
                " AND " + ATTENDANCE_FOR_DATE + " = '" + Fordate + "'";
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        int HeaderId = 0;
        try {
            cursor = db.rawQuery(Query, null);
            if (cursor.moveToFirst()) {
                HeaderId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
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

    public long updateAttendance(AttendanceModel am, int HeaderId) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_CREATED_BY, am.getCreatedBy());
            values.put(ATTENDANCE_CREATED_ON, am.getCreatedOn());
            values.put(ATTENDANCE_UPLOADED_ON, am.getUploadedOn());
            values.put(ATTENDANCE_MODIFIED_BY, getCurrentLoggedInUser().getId());
            values.put(ATTENDANCE_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
            long i = DB.update(TABLE_ATTENDANCE, values, KEY_ID + " = " + HeaderId, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateAttendance(AttendanceModel am) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            if (am.getForDate() != null) {
                am.setForDate(AppModel.getInstance().convertDatetoFormat(am.getForDate(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ATTENDANCE_FOR_DATE, am.getForDate());
            }
            values.put(ATTENDANCE_CREATED_BY, am.getCreatedBy());

            if (am.getCreatedOn() != null) {
                am.setCreatedOn(AppModel.getInstance().convertDatetoFormat(am.getCreatedOn(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(ATTENDANCE_CREATED_ON, am.getCreatedOn());
            }

            values.put(ATTENDANCE_SCHOOL_CLASS_ID, am.getSchoolId());
            values.put(ATTENDANCE_SERVER_ID, am.getId());
            values.put(ATTENDANCE_MODIFIED_BY, getCurrentLoggedInUser().getId());

            if (am.getModified_on() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                am.setModified_on(AppModel.getInstance().convertDatetoFormat(am.getModified_on(), dateFormat, "yyyy-MM-dd hh:mm:ss a"));
                values.put(ATTENDANCE_MODIFIED_ON, am.getModified_on());

            }

            long i = DB.update(TABLE_ATTENDANCE, values, ATTENDANCE_SERVER_ID + " =" + am.getId(), null);

            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Attendance server id: " + am.getId());
            } else {
                AppModel.getInstance().appendLog(context, "Attendance updated with server id: " + am.getId());
            }
            return i;

        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendLog(context, "In Attendance Update method. Exception occurred: " + e.getMessage());
            return -1;
        }
    }


    public ArrayList<AttendanceModel> getAllAttendanceForUpload() {
        ArrayList<AttendanceModel> amList = new ArrayList<AttendanceModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE
                    + " WHERE " + ATTENDANCE_UPLOADED_ON + " IS NULL OR "
                    + ATTENDANCE_UPLOADED_ON + " = ''";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    AttendanceModel am = new AttendanceModel();
                    am.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    am.setServer_id(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SERVER_ID)));
                    am.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                    am.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                    am.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                    am.setUploadedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_UPLOADED_ON)));
                    am.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                    am.setModified_by(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_MODIFIED_BY)));
                    am.setModified_on(cursor.getString(cursor.getColumnIndex(ATTENDANCE_MODIFIED_ON)));

                    amList.add(am);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return amList;
    }

    public AttendanceModel getLastAttendanceRecord() {
        AttendanceModel attModel = null;
        Cursor cursor = null;
        String query = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_ID + " = (SELECT MAX(" + KEY_ID + ")  FROM " + TABLE_ATTENDANCE + ")";
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    attModel = new AttendanceModel();
                    attModel.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                    attModel.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                    attModel.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                    attModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    attModel.setUploadedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_UPLOADED_ON)));
                    attModel.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return attModel;
    }


    //TABLE_ATTENDANCE_STUDENT
    public long addAttendanceStudent(AttendanceStudentModel asm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_ID, asm.getAttendanceId());
            values.put(ATTENDANCE_STUDENT_ID, asm.getStudentId());
            values.put(ATTENDANCE_IS_ABSENT, asm.isAbsent());
            values.put(ATTENDANCE_REASON, asm.getReason());

            long i = DB.insert(TABLE_ATTENDANCE_STUDENT, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addDownloadedAttendanceStudent(ArrayList<AttendanceStudentModel> asmList, int attendanceId, int amId) {
        SQLiteDatabase DB = getDB();
        try {
            DB.beginTransaction();
            for (AttendanceStudentModel asm : asmList) {
                if (asm.getAttendanceId() == amId) {
                    //Insert
                    if (!FindAttendanceStudentRecord(attendanceId, asm.getStudentId())) {

                        ContentValues values = new ContentValues();
                        values.put(ATTENDANCE_ID, attendanceId);
                        values.put(ATTENDANCE_STUDENT_ID, asm.getStudentId());
                        values.put(ATTENDANCE_IS_ABSENT, asm.isAbsent());
                        values.put(ATTENDANCE_REASON, asm.getReason());

                        long i = DB.insert(TABLE_ATTENDANCE_STUDENT, null, values);
                        if (i == -1) {
                            AppModel.getInstance().appendLog(context, "Couldn't insert Record of student id:" + asm.getStudentId());
                        } else {
                            AppModel.getInstance().appendLog(context, "Record inserted with student id:" + asm.getStudentId());
                        }
//                    return i;
                    }
                    //Update
                    else {
                        if (!IfAttendanceNotUploaded(attendanceId))
                            updateDownloadedAttendanceStudent(asm, attendanceId);
                    }
                }
            }
            DB.setTransactionSuccessful();

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In Insert student attendance Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
//            return -1;
        } finally {
            DB.endTransaction();
        }
    }

    public long deleteStudentAttendance(int stdntId, int hid) {
        long i = -1;
        try {
            SQLiteDatabase db = getDB();
            i = db.delete(TABLE_ATTENDANCE_STUDENT, ATTENDANCE_STUDENT_ID + " = " + stdntId + " And " + ATTENDANCE_ID + " = " + hid, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public long updateAttendanceStudent(AttendanceStudentModel asm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_IS_ABSENT, asm.isAbsent());
            values.put(ATTENDANCE_REASON, asm.getReason());

            long i = DB.update(TABLE_ATTENDANCE_STUDENT, values, ATTENDANCE_STUDENT_ID + " = " + asm.getStudentId() + " AND " + ATTENDANCE_ID + " = " + asm.getAttendanceId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateDownloadedAttendanceStudent(AttendanceStudentModel asm, int attendanceId) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_ID, attendanceId);
            values.put(ATTENDANCE_STUDENT_ID, asm.getStudentId());
            values.put(ATTENDANCE_IS_ABSENT, asm.isAbsent());
            values.put(ATTENDANCE_REASON, asm.getReason());

            long i = DB.update(TABLE_ATTENDANCE_STUDENT, values, ATTENDANCE_ID + " =" + attendanceId + " AND " + ATTENDANCE_STUDENT_ID + " =" + asm.getStudentId(), null);
            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update Record of student id:" + asm.getStudentId());
            } else {
                AppModel.getInstance().appendLog(context, "Record updated with student id:" + asm.getStudentId());
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In update student attendance Method. exception occurs: " + e.getMessage());
            return -1;
        }
    }

    //TABLE_PROMOTION_STUDENT
    public ArrayList<PromotionStudentDBModel> getAllPromotionStudentsForUpload(int PromotionId) {
        ArrayList<PromotionStudentDBModel> psmList = new ArrayList<PromotionStudentDBModel>();
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT DISTINCT ps.* FROM " + TABLE_PROMOTION + " p,"
                    + TABLE_PROMOTION_STUDENT + " ps "
                    + "WHERE ps." + PROMOTION_ID + " = " + PromotionId;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {


                    PromotionStudentDBModel psm = new PromotionStudentDBModel();
                    psm.setStudent_id(cursor.getInt(cursor.getColumnIndex(PROMOTION_STUDENT_STUDENT_ID)));
                    psm.setNew_schoolclass_id(cursor.getInt(cursor.getColumnIndex(PROMOTION_STUDENT_NEW_SCHOOLCLASS_ID)));
                    psmList.add(psm);


                } while (cursor.moveToNext());
            }

        } catch (
                Exception e
        ) {
            e.printStackTrace();

        } finally {
            if (cursor != null)
                cursor.close();
        }


        return psmList;
    }


    //TABLE_PROMOTION
    public long insertPromotion(PromotionDBModel pm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(PROMOTION_CREATED_BY, pm.getCreated_by());
            values.put(PROMOTION_CREATED_ON, pm.getCreated_on());
            values.put(PROMOTION_UPLOADED_ON, pm.getUploaded_on());
            values.put(PROMOTION_SCHOOL_CLASS_ID, pm.getSchoolClassId());

            long i = DB.insert(TABLE_PROMOTION, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


    public long insertDownloadedPromotion(PromotionDBModel pm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(PROMOTION_CREATED_BY, pm.getCreated_by());
            if (pm.getCreated_on() != null) {
                pm.setCreated_on(AppModel.getInstance().convertDatetoFormat(pm.getCreated_on(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(PROMOTION_CREATED_ON, pm.getCreated_on());
                values.put(PROMOTION_UPLOADED_ON, pm.getCreated_on());
            }

            values.put(PROMOTION_MODIFIED_ON, pm.getModified_on());


            values.put(PROMOTION_SCHOOL_CLASS_ID, pm.getSchoolClassId());

            long i = DB.insert(TABLE_PROMOTION, null, values);
            if (i == -1) {
                AppModel.getInstance().appendLog(context, "Couldn't insert Promotion with schoolClassId: " + pm.getSchoolClassId());
            } else {
                AppModel.getInstance().appendLog(context, "Promotion inserted with schoolClassId:" + pm.getSchoolClassId());
            }
            return i;
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In insert Promotion Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public void insertUpdateBulkDownloadedPromotion(List<PromotionDBModel> pmList, ArrayList<PromotionStudentDBModel> psmList, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = getDB();
        ContentValues values = new ContentValues();
        int downloadedCount = 0;
        try {
            DB.beginTransaction();
            for (PromotionDBModel pm : pmList) {
                try {
                    values.put(PROMOTION_CREATED_BY, pm.getCreated_by());
                    if (pm.getCreated_on() != null) {
                        pm.setCreated_on(AppModel.getInstance().convertDatetoFormat(pm.getCreated_on(), "dd/MM/yy", "yyyy-MM-dd"));
                        values.put(PROMOTION_CREATED_ON, pm.getCreated_on());
                    }
                    values.put(PROMOTION_MODIFIED_ON, pm.getModified_on());
                    values.put(PROMOTION_SCHOOL_CLASS_ID, pm.getSchoolClassId());

                    int headerId = pm.getDevice_id();
                    //Insert Promotion
                    if (!FindPromotionRecord(headerId)) {
                        values.put(PROMOTION_UPLOADED_ON, pm.getCreated_on());

                        headerId = (int) DB.insert(TABLE_PROMOTION, null, values);
                        if (headerId == -1) {
                            AppModel.getInstance().appendLog(context, "Couldn't insert Promotion with schoolClassId: " + pm.getSchoolClassId());
                        } else {
                            AppModel.getInstance().appendLog(context, "Promotion inserted with schoolClassId:" + pm.getSchoolClassId());
                        }

                        if (headerId > 0) {
                            downloadedCount++;
                        }

                    }
                    //Update Promotion
                    else {
                        if (!IfPromotionNotUploaded(pm.getDevice_id())) {
                            long i = DB.update(TABLE_PROMOTION, values, KEY_ID + " =" + pm.getDevice_id(), null);
                            if (i == 0) {
                                AppModel.getInstance().appendLog(context, "Couldn't update promotion with id: " + pm.getId());
                            } else {
                                AppModel.getInstance().appendLog(context, "promotion updated with with id:" + pm.getId());
                            }

                            if (i > 0) {
                                downloadedCount++;
                            }

                        }
                    }

                    //Inserting PromotionStudent
                    for (PromotionStudentDBModel psm : psmList) {
                        try {
                            if (psm.getPromotion_id() == pm.getId()) {
                                if (!FindPromotionStudentRecord(headerId, (int) psm.getStudent_id())) {
                                    addPromotionStudent(psm, headerId);
                                } else {
                                    if (!IfPromotionNotUploaded(headerId))
                                        updatePromotionStudent(psm, headerId);
                                }
                            }
                        } catch (Exception e) {
                            AppModel.getInstance().appendLog(context, "In insertUpdateBulkDownloadedPromotion Method. exception occurs: " + e.getMessage());
                            e.printStackTrace();
                            DataSync.areAllServicesSuccessful = false;
                        }
                    }
                } catch (Exception e) {
                    AppModel.getInstance().appendErrorLog(context, "In insertUpdateBulkDownloadedPromotion Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                    DataSync.areAllServicesSuccessful = false;
                }
                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Exception in insertUpdateBulkDownloadedPromotion error:" + e.getMessage());
        } finally {
            DB.endTransaction();
        }
    }

    public long updateDownloadedPromotion(PromotionDBModel pm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(PROMOTION_CREATED_BY, pm.getCreated_by());
            if (pm.getCreated_on() != null) {
                pm.setCreated_on(AppModel.getInstance().convertDatetoFormat(pm.getCreated_on(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(PROMOTION_CREATED_ON, pm.getCreated_on());
            }
            values.put(PROMOTION_MODIFIED_ON, pm.getModified_on());
            values.put(PROMOTION_SCHOOL_CLASS_ID, pm.getSchoolClassId());

            long i = DB.update(TABLE_PROMOTION, values, KEY_ID + " =" + pm.getDevice_id(), null);
            if (i == 0) {
                AppModel.getInstance().appendLog(context, "Couldn't update promotion with id: " + pm.getId());
            } else {
                AppModel.getInstance().appendLog(context, "promotion updated with with id:" + pm.getId());
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In update Promotion Method. exception occurs: " + e.getMessage());
            return -1;
        }
    }

    public ArrayList<PromotionDBModel> getAllPromotionForUpload() {
        ArrayList<PromotionDBModel> pmList = new ArrayList<PromotionDBModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PROMOTION
                    + " WHERE " + PROMOTION_UPLOADED_ON + " IS NULL OR "
                    + PROMOTION_UPLOADED_ON + " = ''";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    PromotionDBModel pm = new PromotionDBModel();
                    pm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    pm.setCreated_by(cursor.getInt(cursor.getColumnIndex(PROMOTION_CREATED_BY)));
                    pm.setCreated_on(cursor.getString(cursor.getColumnIndex(PROMOTION_CREATED_ON)));
                    pm.setUploaded_on(cursor.getString(cursor.getColumnIndex(PROMOTION_UPLOADED_ON)));
                    pm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(PROMOTION_SCHOOL_CLASS_ID)));

                    pmList.add(pm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return pmList;
    }

    public ArrayList<AttendanceStudentUploadModel> getAllAttendanceStudentsForUpload(int AttendanceId) {
        ArrayList<AttendanceStudentUploadModel> asmList = new ArrayList<AttendanceStudentUploadModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT DISTINCT sa.* FROM " + TABLE_ATTENDANCE + " a,"
                    + TABLE_ATTENDANCE_STUDENT + " sa "
                    + "WHERE sa." + ATTENDANCE_ID + " = " + AttendanceId;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    AttendanceStudentUploadModel asm = new AttendanceStudentUploadModel();
//                    asm.setAttendanceId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID)));
                    asm.setStudent_id(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_STUDENT_ID)));
                    boolean b = cursor.getString(cursor.getColumnIndex(ATTENDANCE_IS_ABSENT)).equals("1") ? true : false;
                    asm.setIs_absent(b);
                    asm.setReason(cursor.getString(cursor.getColumnIndex(ATTENDANCE_REASON)));
                    asmList.add(asm);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return asmList;
    }

    // TABLE_SCHOOL Methods
    public long addSchool(SchoolModel sm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, sm.getId());
            values.put(SCHOOL_NAME, sm.getName());

            sm.setArea(sm.getArea() == null || sm.getArea().isEmpty() ? "NA" : sm.getArea());
            sm.setRegion(sm.getRegion() == null || sm.getRegion().isEmpty() ? "NA" : sm.getRegion());

            values.put(SCHOOL_REGION, sm.getRegion());
            values.put(SCHOOL_DISTRICT, sm.getDistrict());
            values.put(SCHOOL_AREA, sm.getArea());
            values.put(SCHOOL_YEAR_ID, sm.getSchool_yearId());
            values.put(SCHOOL_ACADEMIC_SESSION, sm.getAcademic_session());
            values.put(SCHOOL_START_DATE, AppModel.getInstance().convertDatetoFormat(sm.getStart_date(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss a"));
            values.put(SCHOOL_END_DATE, AppModel.getInstance().convertDatetoFormat(sm.getEnd_date(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss a"));
            values.put(SCHOOL_EMIS, sm.getEMIS());
            values.put(CAMPUS_ID, sm.getCampusId());

            values.put(SCHOOL_TARGET_AMOUNT, sm.getTarget_Amount());
            values.put(SCHOOL_ACADEMIC_SESSION_ID, sm.getAcademic_Session_Id());
            values.put(SCHOOL_AllowedModule_App, sm.getAllowedModule_App());

            long i = DB.insert(TABLE_SCHOOL, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public void addSchool(List<SchoolModel> list) {
        SQLiteDatabase DB = getDB();
        DB.beginTransaction();
        try {
            for (SchoolModel sm : list) {
                ContentValues values = new ContentValues();
                values.put(KEY_ID, sm.getId());
                values.put(SCHOOL_NAME, sm.getName());

                sm.setArea(sm.getArea() == null || sm.getArea().isEmpty() ? "NA" : sm.getArea());
                sm.setRegion(sm.getRegion() == null || sm.getRegion().isEmpty() ? "NA" : sm.getRegion());

                values.put(SCHOOL_REGION, sm.getRegion());
                values.put(SCHOOL_DISTRICT, sm.getDistrict());
                values.put(SCHOOL_AREA, sm.getArea());
                values.put(SCHOOL_YEAR_ID, sm.getSchool_yearId());
                values.put(SCHOOL_ACADEMIC_SESSION, sm.getAcademic_session());
                values.put(SCHOOL_START_DATE, AppModel.getInstance().convertDatetoFormat(sm.getStart_date(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss a"));
                values.put(SCHOOL_END_DATE, AppModel.getInstance().convertDatetoFormat(sm.getEnd_date(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss a"));
                values.put(SCHOOL_EMIS, sm.getEMIS());
                values.put(CAMPUS_ID, sm.getCampusId());

                values.put(SCHOOL_TARGET_AMOUNT, sm.getTarget_Amount());
                values.put(SCHOOL_ACADEMIC_SESSION_ID, sm.getAcademic_Session_Id());
                values.put(SCHOOL_AllowedModule_App, sm.getAllowedModule_App());
                values.put(SCHOOL_TYPE, sm.getTypeOfSchool());
                values.put(SCHOOL_PROVINCE_ID, sm.getProvinceId());
                values.put(SCHOOL_PROVINCE_NAME, sm.getProvinceName());

                values.put(PRINCIPAL_FIRST_NAME, sm.getPrincipalFirstName());
                values.put(PRINCIPAL_LAST_NAME, sm.getPrincipalLastName());

                values.put(SCHOOL_SHIFT, sm.getSchoolShift());


                long i = DB.insert(TABLE_SCHOOL, null, values);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.endTransaction();
        }

    }

    public int getNoAttendanceDays(int schoolId, String fromDate, String toDate) {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        int noAttendanceDays = 0;
        try {
            String selectQuery = "select count(*) as no_attendanceDays from (select * from \n" +
                    "(select * from \n" +
                    "(select * from (WITH RECURSIVE\n" +
                    "  cnt(x) AS (\n" +
                    "     SELECT 0\n" +
                    "     UNION ALL\n" +
                    "     SELECT x+1 FROM cnt\n" +
                    "      LIMIT (SELECT ((julianday('@fromDate') - julianday('@toDate'))) + 1)\n" +
                    "  )\n" +
                    "SELECT date(julianday('@fromDate'), '+' || x || ' days') as date FROM cnt) dates\n" +
                    "left join attendance a on a.for_Date = dates.date) tot_attend\n" +
                    "left join school_class sc on tot_attend.school_class_id = sc.id)\n" +
                    "where school_id is null or @SchoolId) full_attendance where full_attendance.id is null \n" +
                    "and full_attendance.for_date is null";

            selectQuery = selectQuery.replace("@SchoolId", schoolId + "");
            selectQuery = selectQuery.replace("@fromDate", fromDate);
            selectQuery = selectQuery.replace("@toDate", toDate);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                noAttendanceDays = cursor.getInt(cursor.getColumnIndex("no_attendanceDays"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return noAttendanceDays;
    }


    public void deleteSchool(List<SchoolModel> list) {
        SQLiteDatabase DB = getDB();
        long i = -1;
        DB.beginTransaction();
        try {
            for (SchoolModel sm : list) {
                i = DB.delete(TABLE_SCHOOL, KEY_ID + " = " + sm.getId(), null);
            }
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.endTransaction();
        }

    }


    public long updateSchool(SchoolModel sm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, sm.getId());
            values.put(SCHOOL_NAME, sm.getName());

            sm.setArea(sm.getArea() == null || sm.getArea().isEmpty() ? "NA" : sm.getArea());
            sm.setRegion(sm.getRegion() == null || sm.getRegion().isEmpty() ? "NA" : sm.getRegion());

            values.put(SCHOOL_REGION, sm.getRegion());
            values.put(SCHOOL_DISTRICT, sm.getDistrict());
            values.put(SCHOOL_AREA, sm.getArea());
            values.put(SCHOOL_YEAR_ID, sm.getSchool_yearId());
            values.put(SCHOOL_EMIS, sm.getEMIS());
            values.put(CAMPUS_ID, sm.getCampusId());
            values.put(SCHOOL_ACADEMIC_SESSION, sm.getAcademic_session());
            values.put(SCHOOL_START_DATE, AppModel.getInstance().convertDatetoFormat(sm.getStart_date(), "dd/mm/yyyy", "yyyy-MM-dd hh:mm:ss a"));
            values.put(SCHOOL_END_DATE, AppModel.getInstance().convertDatetoFormat(sm.getEnd_date(), "dd/mm/yyyy", "yyyy-MM-dd hh:mm:ss a"));
            values.put(SCHOOL_TARGET_AMOUNT, sm.getTarget_Amount());

            long i = DB.update(TABLE_SCHOOL, values, KEY_ID + " = " + sm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateSchoolTargetAmount(int targetAmount, int schoolId) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(SCHOOL_TARGET_AMOUNT, targetAmount);

            long i = DB.update(TABLE_SCHOOL, values, KEY_ID + " = " + schoolId, null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public ArrayList<SchoolModel> getAllSchools() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SCHOOL;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setSchool_yearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    sm.setSchoolShift(cursor.getString(cursor.getColumnIndex(SCHOOL_SHIFT)));
                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }


    public ArrayList<SchoolExpandableModel> getSchoolByAreaRegionFromQuery() {
        Cursor cursor = null;
        SchoolExpandableModel sm = new SchoolExpandableModel();
        ArrayList<SchoolExpandableModel> sem = new ArrayList<>();
        try {
            String selectQuery = "SELECT distinct a.area_name,r.region_name, (a.area_name || '  ' || r.region_name) as ra\n" +
                    "                    FROM  school s\n" +
                    "                    left join Campus c on  s.campus_id = c.campus_id\n" +
                    "                    left join Location l  on c.location_id = l.location_id\n" +
                    "                    left join Area a on a.area_id = l.area_id AND a.isActive = 1\n" +
                    "                    left join Region r on r.region_id = a.region_id AND r.isActive = 1\n" +
                    " where s.id in (select school_id from user_school) ";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                do {

                    sm = new SchoolExpandableModel();
                    sm.setRegion(cursor.getString(cursor.getColumnIndex("region_name")));
                    sm.setArea(cursor.getString(cursor.getColumnIndex("area_name")));
                    sm.setRegionArea(cursor.getString(cursor.getColumnIndex("ra")));
                    sm.setSchoolModels(getUserSchoolRegionAndAreaByQuery(sm.getArea(), sm.getRegion()));
//                    Log.e("schoolCount","All:=>> "+ getAllUserSchools().size() +"Area Wise :=>> "+sm.getArea()+" ="+sm.getSchoolModels().size());
//                    Log.e("schoolCount","All Campus:=>> "+ getAllCampus() +"All Location:=>> "+ getAllLocation() + "All Area:=>> "+ getAllArea() + "All Region:=>> "+ getAllRegion());

//                    Log.e("getcursor",sm.getRegion() +"--" + sm.getArea() +"--" + sm.getRegionArea() +"--");
                    if (sm.getRegion() == null || sm.getArea() == null) {
                        continue;
                    }
                    sem.add(sm);

                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sem;
    }


    public SchoolExpandableModel getSchoolInfo(int schoolId) {
        Cursor cursor = null;
        SchoolExpandableModel sm = new SchoolExpandableModel();
        try {
            String selectQuery = "SELECT a.area_name,r.region_name, a.area_name || '  ' || r.region_name as ra,s.*,l.location_name,c.campus_name \n" +
                    "FROM  school s \n" +
                    "left join Campus c on  s.campus_id = c.campus_id\n" +
                    "left join Location l  on c.location_id = l.location_id\n" +
                    "left join Area a on a.area_id = l.area_id AND a.isActive = 1 \n" +
                    "left join Region r on r.region_id = a.region_id AND r.isActive = 1\n" +
                    "where s.id = @Sid ";

            selectQuery = selectQuery.replace("@Sid", schoolId + "");

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                sm.setRegion(cursor.getString(cursor.getColumnIndex("region_name")));
                sm.setArea(cursor.getString(cursor.getColumnIndex("area_name")));
                sm.setLocation(cursor.getString(cursor.getColumnIndex("location_name")));
                sm.setCampus(cursor.getString(cursor.getColumnIndex("campus_name")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sm;
    }


    private ArrayList<SchoolModel> getUserSchoolRegionAndAreaByQuery(String area, String region) {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery;


            if ((area != null && !area.isEmpty()) && (region != null && !region.isEmpty())) {
                selectQuery = "SELECT a.area_name,r.region_name, a.area_name || '  ' || r.region_name as ra,s.*\n" +
                        "                    FROM  school s\n" +
                        "                    left join Campus c on  s.campus_id = c.campus_id\n" +
                        "                    left join Location l  on c.location_id = l.location_id\n" +
                        "                    left join Area a on a.area_id = l.area_id AND a.isActive = 1\n" +
                        "                    left join Region r on r.region_id = a.region_id AND r.isActive = 1\n" +
                        " where s.id in (select school_id from user_school)\n" +
                        " and a.area_name = '@A' and r.region_name = '@R' order by s.id";

                selectQuery = selectQuery.replace("@R", region);
                selectQuery = selectQuery.replace("@A", area);
            } else {
                selectQuery = "select * from school inner join user_school on school.id = user_school.school_id order by school.id";
            }

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(region == null || region.isEmpty() ? "NA" : region);
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(area == null || area.isEmpty() ? "NA" : area);
                    sm.setSchool_yearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolExpandableModel> getSchoolsListGroupedByRegionAndArea() {
        ArrayList<SchoolExpandableModel> smList = new ArrayList<SchoolExpandableModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "select distinct region,area, region || ' - ' || area as ra from school order by ra";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {


                    SchoolExpandableModel sem = new SchoolExpandableModel();
                    sem.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sem.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sem.setRegionArea(cursor.getString(cursor.getColumnIndex("ra")));
                    sem.setSchoolModels(getUserSchoolByAreaAndRegion(sem.getArea(), sem.getRegion()));


//                    SchoolModel sm = new SchoolModel();
//                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
//                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
//                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
//                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
//                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
//                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
//                    sm.setSchool_yearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
//                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));
                    smList.add(sem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getUserSchoolByAreaAndRegion(String area, String region) {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM  school s \n" +
                    "                     INNER JOIN  user_school  us ON us.\n" +
                    "                     school_id = s.id \n" +
                    "                    where s.region = '@R' and s.area = '@A'";

            selectQuery = selectQuery.replace("@R", region);
            selectQuery = selectQuery.replace("@A", area);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setSchool_yearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchools() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setSchool_yearId(cursor.getInt(cursor.getColumnIndex(SCHOOL_YEAR_ID)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForFinance() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + "|| ',') LIKE '%,2,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForPromotion() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,3,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForGraduation() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,4,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForSchoolForms() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,14,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForEmployee() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,6,%' OR" +
                    " (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,7,%' OR" +
                    " (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,8,%' OR" +
                    " (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,9,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForEmployeeListing() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,6,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForEmployeeLeavesAndAttend() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,7,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForEmployeeResignation() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,8,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForEmployeeTermination() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,9,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForEmployeeResignationAndTermination() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,8,%' OR" +
                    " (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,9,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsById(int schoolid) {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
//                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
//                    + SCHOOL_ID + " = s." + KEY_ID +
                    + " WHERE s.id = " + schoolid;
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setTarget_Amount(cursor.getString(cursor.getColumnIndex(SCHOOL_TARGET_AMOUNT)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));
                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForTCTEntry() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + " || ',') LIKE '%,12,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public int getStudentCount() {

        SchoolModel sm = null;
        Cursor cursor = null;
        int count = 0;
        try {
            String selectQuery = "select count(st.id) as count from student st\n" +
                    "        inner join school_class sc on sc.id = st.schoolclass_id\n" +
                    "        inner join user_school us on us.school_id= sc.school_id";
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
                return count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return count;
    }

    public SchoolModel getSchoolById(int id) {
        SchoolModel sm = null;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SCHOOL + " WHERE " + KEY_ID + " = " + id;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setTarget_Amount(cursor.getString(cursor.getColumnIndex(SCHOOL_TARGET_AMOUNT)));
                    sm.setTypeOfSchool(cursor.getString(cursor.getColumnIndex(SCHOOL_TYPE)));
                    sm.setProvinceId(cursor.getInt(cursor.getColumnIndex(SCHOOL_PROVINCE_ID)));
                    sm.setProvinceName(cursor.getString(cursor.getColumnIndex(SCHOOL_PROVINCE_NAME)));
                    sm.setPrincipalFirstName(cursor.getString(cursor.getColumnIndex(PRINCIPAL_FIRST_NAME)));
                    sm.setPrincipalLastName(cursor.getString(cursor.getColumnIndex(PRINCIPAL_LAST_NAME)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sm;
    }

    // TABLE_CLASSES Methods
    public long addClass(ClassModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, cm.getClassId());
            values.put(CLASS_NAME, cm.getName());
            values.put(CLASS_RANK, cm.getRank());
            values.put(CLASS_MODIFIED_ON, cm.getModified_on());

            long i = DB.insert(TABLE_CLASS, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    // TABLE_SCHOOL_CLASSES Methods
    public long addSchoolClass(SchoolClassesModel scm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, scm.getId());
            values.put(SCHOOL_CLASS_SCHOOLID, scm.getSchoolId());
            values.put(SCHOOL_CLASS_CLASSID, scm.getClassId());
            values.put(SCHOOL_CLASS_SECTIONID, scm.getSectionId());
            values.put(SCHOOL_CLASS_IS_ACTIVE, scm.is_active());
            values.put(CAPACITY, scm.getCapacity());
            values.put(MAX_CAPACITY, scm.getMaxCapacity());
            values.put(SCHOOL_CLASS_MODIFIED_ON, scm.getModified_on());


            long i = DB.insert(TABLE_SCHOOL_CLASS, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateSchoolClass(SchoolClassesModel scm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, scm.getId());
            values.put(SCHOOL_CLASS_SCHOOLID, scm.getSchoolId());
            values.put(SCHOOL_CLASS_CLASSID, scm.getClassId());
            values.put(SCHOOL_CLASS_SECTIONID, scm.getSectionId());
            values.put(SCHOOL_CLASS_IS_ACTIVE, scm.is_active());
            values.put(CAPACITY, scm.getCapacity());
            values.put(MAX_CAPACITY, scm.getMaxCapacity());
            values.put(SCHOOL_CLASS_MODIFIED_ON, scm.getModified_on());

            long i = DB.update(TABLE_SCHOOL_CLASS, values, KEY_ID + " = " + scm.getId(), null);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public SchoolClassesModel getSchoolClassByIds(int schoolId, int classId, int sectionId) {
        SchoolClassesModel scm = null;
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT * FROM " + TABLE_SCHOOL_CLASS + " sc"
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND SC." + SCHOOL_CLASS_CLASSID + " =" + classId
                    + " AND SC." + SCHOOL_CLASS_SECTIONID + " =" + sectionId;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    scm = new SchoolClassesModel();
                    scm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    scm.setSchoolId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SCHOOLID)));
                    scm.setClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_CLASSID)));
                    scm.setSectionId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SECTIONID)));
                    scm.setActive(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_IS_ACTIVE)) == 1);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return scm;
    }

    public SchoolClassesModel getNewSchoolClass(int schoolId, int classId, int sectionId) {
        SchoolClassesModel scm = null;
        Cursor cursor = null;
        try {
            classId++;
            String selectQuery = "SELECT sc.*,c.rank FROM school_class sc \n" +
                    "INNER JOIN class c on c.id = sc.class_id\n" +
                    "WHERE sc.school_id = @schoolid AND c.rank = @rank";

            selectQuery = selectQuery.replace("@schoolid", String.valueOf(schoolId));
            selectQuery = selectQuery.replace("@rank", String.valueOf(classId));

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SECTIONID)) == sectionId) {
                        scm = new SchoolClassesModel();
                        scm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        scm.setSchoolId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SCHOOLID)));
                        scm.setClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_CLASSID)));
                        scm.setSectionId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SECTIONID)));
                        scm.setActive(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_IS_ACTIVE)) == 1);
                        scm.setRank(cursor.getInt(cursor.getColumnIndex(CLASS_RANK)));
                    }
                } while (cursor.moveToNext());
                if (scm == null) {
                    if (cursor.moveToFirst()) {
                        scm = new SchoolClassesModel();
                        scm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        scm.setSchoolId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SCHOOLID)));
                        scm.setClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_CLASSID)));
                        scm.setSectionId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SECTIONID)));
                        scm.setActive(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_IS_ACTIVE)) == 1);
                        scm.setRank(cursor.getInt(cursor.getColumnIndex(CLASS_RANK)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return scm;
    }

    public ClassSectionModel getSchoolClassBySchoolClassId(int schoolId, int schoolClassId) {
        ClassSectionModel csm = null;
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT sc.*,c.name as Class,s.name as Section FROM " + TABLE_SCHOOL_CLASS + " sc"
                    + " INNER JOIN class c on c.id = sc.class_id"
                    + " INNER JOIN section s on s.id = sc.section_id"
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND SC." + KEY_ID + " =" + schoolClassId;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    csm = new ClassSectionModel();
                    csm.setClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_CLASSID)));
                    csm.setSectionId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SECTIONID)));
                    csm.setClass_section_name(cursor.getString(cursor.getColumnIndex("Class")) + " " + cursor.getString(cursor.getColumnIndex("Section")));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return csm;
    }


    public long updateClass(ClassModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, cm.getClassId());
            values.put(CLASS_NAME, cm.getName());
            values.put(CLASS_RANK, cm.getRank());
            values.put(CLASS_MODIFIED_ON, cm.getModified_on());

            long i = DB.update(TABLE_CLASS, values, KEY_ID + " = " + cm.getClassId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public ArrayList<ClassModel> getAllClasses() {
        ArrayList<ClassModel> cmList = new ArrayList<ClassModel>();
        Cursor cursor = null;
        try {
//            String selectQuery = "SELECT DISTINCT c.id,c.name FROM " + TABLE_CLASS
//                    + " c INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON c." + KEY_ID
//                    + " = sc." + SCHOOL_CLASS_CLASSID;


            String selectQuery = "SELECT DISTINCT c.id,c.name FROM " + TABLE_SCHOOL_CLASS + " sc  INNER JOIN " + TABLE_CLASS + " c ON c." + KEY_ID + "= sc." + SCHOOL_CLASS_CLASSID
                    + " AND sc." + SCHOOL_CLASS_SCHOOLID + " = " + AppModel.getInstance().getSelectedSchool(context) + " AND sc." + SCHOOL_CLASS_IS_ACTIVE + " = 1 ORDER BY c.id";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ClassModel cm = new ClassModel();
                    cm.setClassId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    cm.setName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));

                    cmList.add(cm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return cmList;
    }


    public ArrayList<ClassModel> getAllClassesBySchoolId(int schoolId) {
        ArrayList<ClassModel> cmList = new ArrayList<ClassModel>();
        Cursor cursor = null;
        try {
//            String selectQuery = "SELECT DISTINCT c.id,c.name FROM " + TABLE_CLASS
//                    + " c INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON c." + KEY_ID
//                    + " = sc." + SCHOOL_CLASS_CLASSID;


            String selectQuery = "SELECT DISTINCT c.id,c.name FROM " + TABLE_SCHOOL_CLASS + " sc  INNER JOIN " + TABLE_CLASS + " c ON c." + KEY_ID + "= sc." + SCHOOL_CLASS_CLASSID
                    + " AND sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId + " AND sc." + SCHOOL_CLASS_IS_ACTIVE + " = 1 ORDER BY c.id";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ClassModel cm = new ClassModel();
                    cm.setClassId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    cm.setName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));

                    cmList.add(cm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return cmList;
    }


    public ClassModel getClassById(int id) {
        ClassModel cm = null;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_CLASS + " WHERE " + KEY_ID + " = " + id;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    cm = new ClassModel();
                    cm.setClassId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    cm.setName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return cm;
    }

    public ClassModel getClassByName(String className) {
        ClassModel cm = null;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_CLASS + " WHERE " + CLASS_NAME + " = '" + className + "'";
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    cm = new ClassModel();
                    cm.setClassId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    cm.setName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return cm;
    }


    // TABLE_SECTION Methods
    public long addSection(SectionModel sm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, sm.getSectionId());
            values.put(SECTION_NAME, sm.getName());
            values.put(SECTION_MODIFIED_ON, sm.getModified_on());

            long i = DB.insert(TABLE_SECTION, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateSection(SectionModel sm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, sm.getSectionId());
            values.put(SECTION_NAME, sm.getName());
            values.put(SECTION_MODIFIED_ON, sm.getModified_on());

            long i = DB.update(TABLE_SECTION, values, KEY_ID + " = " + sm.getSectionId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public ArrayList<ClassSectionModel> getClassSectionBySchoolId(String SchoolID) {
        ArrayList<ClassSectionModel> csmList = new ArrayList<ClassSectionModel>();
        Cursor cursor = null;
        try {

            String selectQuery = "select sc.id as schoolclass_id,c.id as class_id,c.name as class_name,c.rank, s.id as section_id,s.name as section_name " +
                    "from school_class sc " +
                    "inner join class c on sc.class_id = c.id " +
                    "inner join section s on sc.section_id = s.id " +
                    "where sc.school_id=" + SchoolID + " and sc.is_active=1 order by c.rank asc";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ClassSectionModel sm = new ClassSectionModel();
                    sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
                    sm.setClassId(cursor.getInt(cursor.getColumnIndex("class_id")));
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex("section_id")));
                    sm.setRank(cursor.getInt(cursor.getColumnIndex(CLASS_RANK)));

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


    public ArrayList<ClassSectionModel> getPresentClassSectionBySchoolIdForPromotion(String SchoolID) {
        ArrayList<ClassSectionModel> csmList = new ArrayList<ClassSectionModel>();
        Cursor cursor = null;
        try {

//            String selectQuery = "select sc.id as schoolclass_id,c.id as class_id,c.name as class_name,c.rank, s.id as section_id,s.name as section_name " +
//                    "from school_class sc inner join class c on sc.class_id = c.id " +
//                    "inner join section s on sc.section_id = s.id where sc.school_id=@SchoolId and sc.is_active=1 \n" +
//                    "and sc.class_id < (select max(class_id) from school_class where school_id = @SchoolId and is_active = 1)\n" +
//                    "order by c.rank asc";

            String selectQuery = "select sc.id as schoolclass_id,c.id as class_id,c.name as class_name,c.rank, s.id as section_id,s.name as section_name \n" +
                    "from school_class sc \n" +
                    "inner join class c on sc.class_id = c.id \n" +
                    "inner join section s on sc.section_id = s.id \n" +
                    "where sc.school_id = @SchoolId and sc.is_active = 1 \n" +
                    "and c.rank < (select max(c.rank) from school_class sc inner join class c on sc.class_id = c.id where sc.school_id = @SchoolId AND sc.is_active = 1)\n" +
                    "order by c.rank asc";

            selectQuery = selectQuery.replace("@SchoolId", SchoolID);

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ClassSectionModel sm = new ClassSectionModel();
                    sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
                    sm.setClassId(cursor.getInt(cursor.getColumnIndex("class_id")));
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex("section_id")));
                    sm.setRank(cursor.getInt(cursor.getColumnIndex("rank")));

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

    public ArrayList<SectionModel> getAllSections() {
        ArrayList<SectionModel> smList = new ArrayList<SectionModel>();
        Cursor cursor = null;
        try {

//            String selectQuery = "SELECT DISTINCT s.id,s.name FROM " + TABLE_SECTION
//                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON s." + KEY_ID
//                    + " = sc." + SCHOOL_CLASS_SECTIONID;
            String selectQuery = "SELECT DISTINCT s.id,s.name FROM " + TABLE_SCHOOL_CLASS
                    + " sc INNER JOIN " + TABLE_SECTION + " s ON s." + KEY_ID
                    + " = sc." + SCHOOL_CLASS_SECTIONID
                    + " AND sc." + SCHOOL_CLASS_SCHOOLID + " = " + AppModel.getInstance().getSelectedSchool(context)
                    + " AND sc." + SCHOOL_CLASS_IS_ACTIVE + " = 1 ORDER BY s.id";

//            String selectQuery = "SELECT * FROM " + TABLE_SECTION;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SectionModel sm = new SectionModel();
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SECTION_NAME)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public SectionModel getSectionById(int id) {
        SectionModel sm = null;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SECTION + " WHERE " + KEY_ID + " = " + id;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    sm = new SectionModel();
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SECTION_NAME)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sm;
    }

    public SectionModel getSectionByName(String sectionName) {
        SectionModel sm = null;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SECTION + " WHERE " + SECTION_NAME + " = '" + sectionName + "'";
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    sm = new SectionModel();
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SECTION_NAME)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sm;
    }


    //TABLE_WITHDRAWAL_REASON Methods
    public long addWithdrawalReason(WithdrawalReasonModel wrm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, wrm.getId());
            values.put(WITHDRAWAL_REASON_NAME, wrm.getReasonName());
            values.put(WITHDRAWAL_REASON_MODIFIED_ON, wrm.getModified_on());


            long i = DB.insert(TABLE_WITHDRAWAL_REASON, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public long updateWithdrawalReason(WithdrawalReasonModel wrm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, wrm.getId());
            values.put(WITHDRAWAL_REASON_NAME, wrm.getReasonName());
            values.put(WITHDRAWAL_REASON_MODIFIED_ON, wrm.getModified_on());

            long i = DB.update(TABLE_WITHDRAWAL_REASON, values, KEY_ID + " = " + wrm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public ArrayList<WithdrawalReasonModel> getWithdrawalReasons(boolean includeGraduatedFromSchool) {
        ArrayList<WithdrawalReasonModel> wrList = new ArrayList<WithdrawalReasonModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_WITHDRAWAL_REASON;
            if (includeGraduatedFromSchool) {
                selectQuery += " where " + WITHDRAWAL_REASON_NAME + " is not 'Graduated from School'";
            }
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    WithdrawalReasonModel wrm = new WithdrawalReasonModel();
                    wrm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    wrm.setReasonName(cursor.getString(cursor.getColumnIndex(WITHDRAWAL_REASON_NAME)));

                    wrList.add(wrm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return wrList;
    }


    //TABLE_WITHDRAWAL Methods

    /**
     * Used from Withdrawal UI
     *
     * @param wm
     * @return
     */
    public long withdrawStudent(WithdrawalModel wm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(WITHDRAWAL_STUDENT_ID, wm.getStudent_id());
            values.put(WITHDRAWAL_REASON_ID, wm.getReason_id());
            values.put(WITHDRAWAL_CREATED_BY, wm.getCreated_by());
            values.put(WITHDRAWAL_CREATED_ON, wm.getCreated_on());
            values.put(WITHDRAWAL_SCHOOL_ID, wm.getSchool_id());


            long i = DB.insert(TABLE_WITHDRAWAL, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addWithdrawal(ArrayList<WithdrawalModel> wmList, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase DB = getDB();
        try {
            DB.beginTransaction();

            int downloadedCount = 0;
            for (WithdrawalModel wmodel : wmList) {
                if (!FindWithdrawalRecord(wmodel.getId(), wmodel.getStudent_id())) {

                    ContentValues values = new ContentValues();
                    values.put(WITHDRAWAL_STUDENT_ID, wmodel.getStudent_id());
                    values.put(WITHDRAWAL_REASON_ID, wmodel.getReason_id());
                    values.put(WITHDRAWAL_CREATED_BY, wmodel.getCreated_by());
                    if (wmodel.getCreated_on() != null) {
                        wmodel.setCreated_on(AppModel.getInstance().convertDatetoFormat(wmodel.getCreated_on(), "dd/MM/yy", "yyyy-MM-dd"));
                        values.put(WITHDRAWAL_CREATED_ON, wmodel.getCreated_on());
                        values.put(WITHDRAWAL_UPLOADED_ON, wmodel.getCreated_on());
                    }
                    values.put(WITHDRAWAL_MODIFIED_ON, wmodel.getModified_on());

                    values.put(WITHDRAWAL_SCHOOL_ID, wmodel.getSchool_id());

                    long i = DB.insert(TABLE_WITHDRAWAL, null, values);

                    if (i > 0) {
                        downloadedCount++;
                    }

//                return i;
                } /*else {
                    DatabaseHelper.getInstance(context).updateWithdrawal(wmodel);
                }*/

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }
            DB.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
//            return -1;
        } finally {
            DB.endTransaction();
        }
    }

    public long updateWithdrawal(WithdrawalModel wm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(WITHDRAWAL_STUDENT_ID, wm.getStudent_id());
            values.put(WITHDRAWAL_REASON_ID, wm.getReason_id());
            values.put(WITHDRAWAL_CREATED_BY, wm.getCreated_by());

            if (wm.getCreated_on() != null) {
                wm.setCreated_on(AppModel.getInstance().convertDatetoFormat(wm.getCreated_on(), "dd/MM/yy", "yyyy-MM-dd"));
                values.put(WITHDRAWAL_CREATED_ON, wm.getCreated_on());
            }
            values.put(WITHDRAWAL_SCHOOL_ID, wm.getSchool_id());

            long i = DB.update(TABLE_WITHDRAWAL, values, KEY_ID + " = " + wm.getId() + " AND " + WITHDRAWAL_STUDENT_ID + " = " + wm.getStudent_id(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long undoWithdrawalRecord() {
        SQLiteDatabase DB = getDB();
        try {
            String query = "SELECT * FROM " + TABLE_WITHDRAWAL
                    + " WHERE " + KEY_ID + " = (SELECT MAX(" + KEY_ID + ")  FROM " + TABLE_WITHDRAWAL + ")";
            Cursor cursor = DB.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                long i = DB.delete(TABLE_WITHDRAWAL, KEY_ID + " = " + id, null);
                return i;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }


    public ArrayList<WithdrawalModel> getAllWithdrawalForUpload(int schoolId) {
        ArrayList<WithdrawalModel> wmList = new ArrayList<WithdrawalModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_WITHDRAWAL
                    + " WHERE " + WITHDRAWAL_UPLOADED_ON + " IS NULL OR "
                    + WITHDRAWAL_UPLOADED_ON + " = ''";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    WithdrawalModel pm = new WithdrawalModel();
                    pm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    pm.setStudent_id(cursor.getInt(cursor.getColumnIndex(WITHDRAWAL_STUDENT_ID)));
                    pm.setReason_id(cursor.getInt(cursor.getColumnIndex(WITHDRAWAL_REASON_ID)));
                    pm.setCreated_by(cursor.getInt(cursor.getColumnIndex(WITHDRAWAL_CREATED_BY)));
                    pm.setCreated_on(cursor.getString(cursor.getColumnIndex(WITHDRAWAL_CREATED_ON)));
                    pm.setUploaded_on(cursor.getString(cursor.getColumnIndex(WITHDRAWAL_UPLOADED_ON)));
                    pm.setSchool_id(cursor.getInt(cursor.getColumnIndex(WITHDRAWAL_SCHOOL_ID)));

                    wmList.add(pm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return wmList;
    }

    public ArrayList<UploadStudentsAuditModel> getAllSchoolAuditsForUpload() {
        ArrayList<UploadStudentsAuditModel> samList = new ArrayList<UploadStudentsAuditModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SCHOOL_AUDIT
                    + " WHERE " + SCHOOL_AUDIT_UPLOADED_ON + " IS NULL OR "
                    + SCHOOL_AUDIT_UPLOADED_ON + " = ''";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    try {
                        boolean b = cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_IS_APPROVED)).equals("1") ? true : false;
                        UploadStudentsAuditModel sam = new UploadStudentsAuditModel();
                        sam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        sam.setServer_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_SERVER_ID)));
                        sam.setAudited_on(cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_VISIT_DATE)));
                        sam.setAudited_by(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_APPROVED_BY)));
                        sam.setSchool_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_SCHOOL_ID)));
                        sam.setIs_approved(b);
                        sam.setRemarks(cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_REMARKS)));
                        sam.setClasses_count(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_CLASSES_COUNT)));
                        sam.setStudents_count(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_STUDENT_COUNT)));
//                        sam.setUploaded_on(cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_UPLOADED_ON)));

                        samList.add(sam);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return samList;
    }


    public boolean FindRecord(String Table, String key, Integer ID) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + Table + " WHERE " + key + " = " + ID;
            SQLiteDatabase db = getDB();
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

    public boolean doesMorningShiftExist() {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SCHOOL + " WHERE " + SCHOOL_SHIFT + " = 'M' AND ID IN (" + AppModel.getInstance().getAllUserSchoolsCommaSeparated(context) + ")";
            SQLiteDatabase db = getDB();
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

    public boolean FindStudentRecord(int id, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS
                    + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + KEY_ID + " =" + id;

            SQLiteDatabase db = getDB();
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

    public boolean IfStudentNotUploaded(int id, int schoolId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS
                    + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + KEY_ID + " =" + id
                    + " AND s." + STUDENT_UPLOADED_ON + " IS NULL";

            SQLiteDatabase db = getDB();
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

    public boolean FindAttendance(String strForDate, int schoolId) {
        Cursor cursor = null;
        boolean count = false;
        try {
            String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_ATTENDANCE + " WHERE "
                    + ATTENDANCE_FOR_DATE + " = '" + strForDate + "'"
                    + " AND " + ATTENDANCE_SCHOOL_CLASS_ID + " = " + schoolId;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            count = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return count;
    }

    public AttendanceModel FindAttendanceRecord(int headerId) {
        Cursor cursor = null;
        AttendanceModel model = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE server_id = " + headerId;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                model = new AttendanceModel();
                model.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                model.setDevice_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                model.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                model.setUploadedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_UPLOADED_ON)));
                model.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                model.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                model.setServer_id(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SERVER_ID)));
                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean FindAttendanceStudentRecord(int headerId, int studentId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE_STUDENT
                    + " WHERE " + ATTENDANCE_ID + " = " + headerId + " AND " + ATTENDANCE_STUDENT_ID + " = " + studentId;
            SQLiteDatabase db = getDB();
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

    public boolean IfAttendanceNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE
                    + " WHERE server_id = " + id
                    + " AND " + ATTENDANCE_UPLOADED_ON + " IS NULL"
                    + " OR " + ATTENDANCE_UPLOADED_ON + " =''";

            SQLiteDatabase db = getDB();
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

    public boolean FindPromotionStudentRecord(int headerId, int studentId) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PROMOTION_STUDENT
                    + " WHERE " + PROMOTION_ID + " = " + headerId + " AND " + PROMOTION_STUDENT_STUDENT_ID + " = " + studentId;
            SQLiteDatabase db = getDB();
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

    public boolean FindPromotionRecord(int headerId) {
        Cursor cursor = null;
        try {
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            Date dtForDate = df.parse(createdOn);
//            createdOn = DateFormat.format("yyyy-MM-dd", dtForDate).toString();
//            String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_PROMOTION + " WHERE "
//                    + PROMOTION_CREATED_ON + " = '" + createdOn + "'"
//                    + " AND " + PROMOTION_SCHOOL_ID + " = " + schoolId;

            String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_PROMOTION + " WHERE "
                    + KEY_ID + " = " + headerId;
            SQLiteDatabase db = getDB();
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

    public boolean IfPromotionNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PROMOTION
                    + " WHERE " + KEY_ID + " = " + id
                    + " AND " + PROMOTION_UPLOADED_ON + " IS NULL"
                    + " OR " + PROMOTION_UPLOADED_ON + " =''";

            SQLiteDatabase db = getDB();
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

    public boolean FindWithdrawalRecord(int Id, int studentId) {
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_WITHDRAWAL + " WHERE "
//                    + KEY_ID + " = " + Id + " AND "
                    + WITHDRAWAL_STUDENT_ID + " = " + studentId;

            SQLiteDatabase db = getDB();
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

    public EnrollmentModel FindEnrollmentRecord(int id) {
        Cursor cursor = null;
        EnrollmentModel model = null;
        try {

//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            Date dtForDate = df.parse(createdOn);
//            createdOn = DateFormat.format("yyyy-MM-dd", dtForDate).toString();
            String selectQuery = "SELECT * FROM " + TABLE_ENROLLMENT + " WHERE "
                    + ENROLLMENT_SERVER_ID + " = " + id;
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                model = new EnrollmentModel();
                model.setServer_id(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_SERVER_ID)));
                model.setENROLLMENT_GR_NO(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_GR_NO)));
                model.setENROLLMENT_SCHOOL_ID(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_SCHOOL_ID)));
                model.setENROLLMENT_CREATED_BY(cursor.getInt(cursor.getColumnIndex(ENROLLMENT_CREATED_BY)));
                model.setENROLLMENT_CREATED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_CREATED_ON)));
                model.setENROLLMENT_REVIEW_STATUS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_STATUS)));
                model.setENROLLMENT_REVIEW_COMMENTS(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_COMMENTS)));
                model.setENROLLMENT_REVIEW_COMPLETED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_REVIEW_COMPLETED_ON)));
                model.setENROLLMENT_DATA_DOWNLOADED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_DATA_DOWNLOADED_ON)));
                model.setENROLLMENT_UPLOADED_ON(cursor.getString(cursor.getColumnIndex(ENROLLMENT_UPLOADED_ON)));
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return model;
    }

    public boolean FindEnrollmentImageRecord(int headerId, String fileType) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_ENROLLMENT_IMAGE
                    + " WHERE " + ENROLLMENT_ID + " = " + headerId + " AND " + ENROLLMENT_FILE_TYPE + " = '" + fileType + "'";
            SQLiteDatabase db = getDB();
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

    public boolean IfEnrollmentNotUploaded(int id) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_ENROLLMENT
                    + " WHERE " + KEY_ID + " = " + id
                    + " AND " + ENROLLMENT_UPLOADED_ON + " IS NULL"
                    + " OR " + ENROLLMENT_UPLOADED_ON + " =''";

            SQLiteDatabase db = getDB();
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

    public boolean FindSchoolAuditRecord(int Id, int schoolId) {
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT " + SCHOOL_AUDIT_SERVER_ID + " FROM " + TABLE_SCHOOL_AUDIT + " WHERE "
                    + SCHOOL_AUDIT_SERVER_ID + " = " + Id + " AND " + SCHOOL_AUDIT_SCHOOL_ID + " = " + schoolId;

            SQLiteDatabase db = getDB();
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

    public int getStudentClassCount(String SchoolId, int ClassId) {
        int studentCount = 0;
        Cursor cursor = null;

        String selectQuery = "SELECT COUNT (*) AS RECCOUNT"
                + " FROM " + TABLE_STUDENT
                + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = '"
                + SchoolId + "' AND c." + KEY_ID + " = " + ClassId
                + " AND s." + STUDENT_IS_ACTIVE + " =1";

        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                return studentCount = cursor.getInt(cursor.getColumnIndex("RECCOUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return studentCount;
    }

    public long insertSchoolAuditRecordHeader(SchoolAuditModel schoolAuditModel) {
        SQLiteDatabase db = getDB();
        long id = -1;
        try {
            ContentValues values = new ContentValues();

            values.put(SCHOOL_AUDIT_VISIT_DATE, schoolAuditModel.getVisit_date());
            values.put(SCHOOL_AUDIT_APPROVED_BY, schoolAuditModel.getApproved_by());
            values.put(SCHOOL_AUDIT_SCHOOL_ID, schoolAuditModel.getSchool_id());
            values.put(SCHOOL_AUDIT_IS_APPROVED, schoolAuditModel.is_approved());
            values.put(SCHOOL_AUDIT_REMARKS, schoolAuditModel.getRemarks());
            values.put(SCHOOL_AUDIT_STUDENT_COUNT, schoolAuditModel.getStudents_count());
            values.put(SCHOOL_AUDIT_CLASSES_COUNT, schoolAuditModel.getClasses_count());
            id = db.insert(TABLE_SCHOOL_AUDIT, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return id;
    }

    public void insertDownloadedSchoolAudit(ArrayList<SchoolAuditModel> samList, SyncDownloadUploadModel syncDownloadUploadModel) {
        SQLiteDatabase db = getDB();
        try {
            db.beginTransaction();

            int downloadedCount = 0;
            for (SchoolAuditModel saModel : samList) {
                //Insert
                if (!FindSchoolAuditRecord(saModel.getId(), saModel.getSchool_id())) {

                    ContentValues values = new ContentValues();
                    values.put(SCHOOL_AUDIT_SERVER_ID, saModel.getId());
                    values.put(SCHOOL_AUDIT_SCHOOL_ID, saModel.getSchool_id());
                    values.put(SCHOOL_AUDIT_APPROVED_BY, saModel.getApproved_by());
                    if (saModel.getVisit_date() != null) {
                        saModel.setVisit_date(AppModel.getInstance().convertDatetoFormat(saModel.getVisit_date(), "dd/MM/yy", "yyyy-MM-dd"));
                        values.put(SCHOOL_AUDIT_VISIT_DATE, saModel.getVisit_date());
                        values.put(SCHOOL_AUDIT_UPLOADED_ON, saModel.getVisit_date());
                    }
                    values.put(SCHOOL_AUDIT_VISIT_DATE, saModel.getVisit_date());
                    values.put(SCHOOL_AUDIT_IS_APPROVED, saModel.is_approved());
                    values.put(SCHOOL_AUDIT_REMARKS, saModel.getRemarks());
                    values.put(SCHOOL_AUDIT_CLASSES_COUNT, saModel.getClasses_count());
                    values.put(SCHOOL_AUDIT_STUDENT_COUNT, saModel.getStudents_count());


                    long id = db.insert(TABLE_SCHOOL_AUDIT, null, values);
//                return id;

                    if (id > 0)
                        downloadedCount++;
                }
                //Update
                else {
                    long i = updateDownloadedSchoolAudit(saModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
//            return -1;
        } finally {
            db.endTransaction();
        }

    }

    public long updateDownloadedSchoolAudit(SchoolAuditModel schoolAuditModel) {
        SQLiteDatabase db = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(SCHOOL_AUDIT_APPROVED_BY, schoolAuditModel.getApproved_by());
            values.put(SCHOOL_AUDIT_IS_APPROVED, schoolAuditModel.is_approved());
            values.put(SCHOOL_AUDIT_REMARKS, schoolAuditModel.getRemarks());
            values.put(SCHOOL_AUDIT_CLASSES_COUNT, schoolAuditModel.getClasses_count());
            values.put(SCHOOL_AUDIT_STUDENT_COUNT, schoolAuditModel.getStudents_count());
            long id = db.update(TABLE_SCHOOL_AUDIT, values, SCHOOL_AUDIT_SERVER_ID + " = " + schoolAuditModel.getId(), null);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
        }

    }


    public void insertSchoolAuditRecord(SchoolAuditClassModel auditClassModel) {
        SQLiteDatabase db = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(SCHOOL_AUDIT_CLASS_AUDIT_ID, auditClassModel.getAudit_id());
            values.put(SCHOOL_AUDIT_CLASS_ID, auditClassModel.getClass_id());
            values.put(SCHOOL_AUDIT_CLASS_COUNT, auditClassModel.getCount());
            values.put(SCHOOL_AUDIT_CLASS_ISAPROVED_BY, auditClassModel.is_approved());
            values.put(SCHOOL_AUDIT_CLASS_REMARKS, auditClassModel.getRemarks());
            db.insert(TABLE_SCHOOL_AUDIT_CLASS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public SchoolAuditModel getSSrNotValidateVisitDate(int schoolId) {
        String visitDate = null;
        Cursor cursor = null;
        SchoolAuditModel model = new SchoolAuditModel();
        String sqlquerry = "SELECT sa." + SCHOOL_AUDIT_VISIT_DATE + ", sa." + SCHOOL_AUDIT_SCHOOL_ID + ", sa."
                + SCHOOL_AUDIT_IS_APPROVED + " From " + TABLE_SCHOOL_AUDIT + " sa where"
                + " sa." + SCHOOL_AUDIT_SCHOOL_ID + " = " + schoolId + " and " + SCHOOL_AUDIT_IS_APPROVED + " <> 1";
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);
            if (cursor.moveToFirst()) {
                do {
                    boolean b = cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_IS_APPROVED)).equals("1");
                    visitDate = cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_VISIT_DATE));
                    model.setIs_approved(b);
                    model.setVisit_date(visitDate);
                } while (cursor.moveToNext());
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

    public ArrayList<StudentAttendanceReportModel> getStudentAttendanceCount(int StudentId, int SchoolId, String month, String Year) {
        ArrayList<StudentAttendanceReportModel> list = new ArrayList<>();
        StudentAttendanceReportModel sarm;
        Cursor cursor = null;

        String sqlquerry = "SELECT * FROM " + TABLE_ATTENDANCE +
                " a, " + TABLE_ATTENDANCE_STUDENT + " sa WHERE a." + KEY_ID +
                " = sa." + ATTENDANCE_ID +
                " AND sa." + ATTENDANCE_STUDENT_ID + " = " + StudentId +
                " AND a." + ATTENDANCE_SCHOOL_CLASS_ID + " = " + SchoolId +
                " AND STRFTIME('%m', a." + ATTENDANCE_CREATED_ON + ") = '" + month + "'" +
                " AND STRFTIME('%Y', a." + ATTENDANCE_CREATED_ON + ") = '" + Year + "'";

        String query = "SELECT sa.*,a.* FROM student s " +
                " INNER JOIN school_class sc ON sc.id = s.schoolclass_id " +
                " LEFT JOIN student_attendance sa ON s.id = sa.student_id " +
                " LEFT JOIN attendance a ON a.id = sa.attendance_id " +
                " WHERE sc.school_id =  " + SchoolId +
                " AND s.id = " + StudentId +
                " And strftime('%m', a.for_date) = '" + month + "' And strftime('%Y', a.for_date) = '" + Year + "'";
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    sarm = new StudentAttendanceReportModel();
                    boolean b = cursor.getString(cursor.getColumnIndex(ATTENDANCE_IS_ABSENT)).equals("1") ? true : false;
                    sarm.setAbsent(b);
                    sarm.setAttendanceId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID)));
                    sarm.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                    sarm.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                    sarm.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                    sarm.setStudentId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_STUDENT_ID)));
                    sarm.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                    sarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sarm.setReason(cursor.getString(cursor.getColumnIndex(ATTENDANCE_REASON)));
                    list.add(sarm);
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
        return list;
    }

    public ArrayList<StudentAttendanceReportModel> getStudentOneYearAttendanceCount(int StudentId, int SchoolId, String Year) {
        ArrayList<StudentAttendanceReportModel> list = new ArrayList<>();
        StudentAttendanceReportModel sarm;
        Cursor cursor = null;

        String query = "SELECT sa.*,a.* FROM student s " +
                " INNER JOIN school_class sc ON sc.id = s.schoolclass_id " +
                " LEFT JOIN student_attendance sa ON s.id = sa.student_id " +
                " LEFT JOIN attendance a ON a.id = sa.attendance_id " +
                " WHERE sc.school_id =  " + SchoolId +
                " AND s.id = " + StudentId +
                " And strftime('%Y', a.for_date) = '" + Year + "'";
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    sarm = new StudentAttendanceReportModel();
                    boolean b = cursor.getString(cursor.getColumnIndex(ATTENDANCE_IS_ABSENT)).equals("1") ? true : false;
                    sarm.setAbsent(b);
                    sarm.setAttendanceId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID)));
                    sarm.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                    sarm.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                    sarm.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                    sarm.setStudentId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_STUDENT_ID)));
                    sarm.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                    sarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sarm.setReason(cursor.getString(cursor.getColumnIndex(ATTENDANCE_REASON)));
                    list.add(sarm);
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
        return list;
    }

    public ArrayList<StudentAttendanceReportModel> getClassSectionAttendanceCount(int SchoolId, int class_section_id, String month, String Year) {
        ArrayList<StudentAttendanceReportModel> list = new ArrayList<>();
        StudentAttendanceReportModel sarm;
        Cursor cursor = null;

        String query = "SELECT sa.*,a.* FROM student s " +
                " INNER JOIN school_class sc ON sc.id = s.schoolclass_id " +
                " LEFT JOIN student_attendance sa ON s.id = sa.student_id " +
                " LEFT JOIN attendance a ON a.id = sa.attendance_id " +
                " WHERE sc.school_id =  " + SchoolId +
                " And strftime('%m', a.for_date) = '" + month + "' And strftime('%Y', a.for_date) = '" + Year + "'";
        if (class_section_id > 0) {
            query += " AND s.schoolclass_id = " + class_section_id;
        }

        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    sarm = new StudentAttendanceReportModel();
                    boolean b = cursor.getString(cursor.getColumnIndex(ATTENDANCE_IS_ABSENT)).equals("1") ? true : false;
                    sarm.setAbsent(b);
                    sarm.setAttendanceId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID)));
                    sarm.setCreatedBy(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_CREATED_BY)));
                    sarm.setCreatedOn(cursor.getString(cursor.getColumnIndex(ATTENDANCE_CREATED_ON)));
                    sarm.setForDate(cursor.getString(cursor.getColumnIndex(ATTENDANCE_FOR_DATE)));
                    sarm.setStudentId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_STUDENT_ID)));
                    sarm.setSchoolId(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_SCHOOL_CLASS_ID)));
                    sarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sarm.setReason(cursor.getString(cursor.getColumnIndex(ATTENDANCE_REASON)));
                    list.add(sarm);
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
        return list;
    }

    public ArrayList<BottomFiveStudentsModel> getWeakestAttendanceForDashboard(String schoolID, String fromDate, String toDate) {
        ArrayList<BottomFiveStudentsModel> list = new ArrayList<>();
        BottomFiveStudentsModel sbfm;
        Cursor cursor = null;


        String sqlquerry = "select *,((select work_days from ( " +
                "select (Total_days - off_days) as work_days from (select -JulianDay(strftime('%Y-%m-%d',date('now','start of month'))) + JulianDay(strftime('%Y-%m-%d',date('now','start of month','+1 month'))) as Total_days,\n" +
                "sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days \n" +
                "from calendar c \n" +
                "INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id\n" +
                "where s.id = @SchoolId and c.activity_start_date >= datetime('@fromDate') \n" +
                "and c.activity_end_date <= datetime('@toDate')\n" +
                "and holiday_type_id in (2,3,4) and c.isActive = 1 \n" +
                "group by s.start_date, s.end_date) working_days\n" +
                ")) - summ) / (select work_days from(\n" +
                "select (Total_days - off_days) as work_days from (select -JulianDay(strftime('%Y-%m-%d',date('now','start of month'))) + JulianDay(strftime('%Y-%m-%d',date('now','start of month','+1 month'))) as Total_days,\n" +
                "sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days \n" +
                "from calendar c \n" +
                "INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id\n" +
                "where s.id = @SchoolId and c.activity_start_date >= datetime('@fromDate') \n" +
                "and c.activity_end_date <= datetime('@toDate')\n" +
                "and holiday_type_id in (2,3,4) and c.isActive = 1\n" +
                "group by s.start_date, s.end_date) working_days\n" +
                ")) *100 as attPerct from (\n" +
                "select *, total_abesents + (select * from(select count(*) as no_attendanceDays from (select * from \n" +
                "(select * from \n" +
                "(select * from (select dates from (WITH RECURSIVE\n" +
                "  cnt(x) AS (\n" +
                "     SELECT 0\n" +
                "     UNION ALL\n" +
                "     SELECT x+1 FROM cnt\n" +
                "      LIMIT (SELECT ((julianday('@toDate') - julianday('@fromDate'))) + 1)\n" +
                "  )\n" +
                "SELECT date(julianday('@fromDate'), '+' || x || ' days') as dates FROM cnt) AS DYN_DATES\n" +
                "where not exists (\n" +
                "select * from calendar where date(calendar.activity_start_date) >= DYN_DATES.dates and date(calendar.activity_end_date)\n" +
                "<=DYN_DATES.dates and school_id = @SchoolId and isActive = 1\n" +
                ")) dates\n" +
                "left join attendance a on a.for_Date = dates.dates) tot_attend\n" +
                "left join school_class sc on tot_attend.school_class_id = sc.id\n" +
                ")\n" +
                "where school_id is null or @SchoolId) full_attendance where full_attendance.id is null \n" +
                "and full_attendance.for_date is null\n" +
                ")) as summ from (\n" +
                "select *,sum(getAttendance) as total_abesents from\n" +
                "(SELECT s.*,sl.id as school_id,sl.school_name, sc.class_id, c.name class_name, cs.name section_name,\n" +
                " COALESCE(NULLIF(sa.isabsent,''), 'p') as getAttendance  \n" +
                " FROM student s \n" +
                " INNER JOIN school_class sc ON sc.id = s.schoolclass_id\n" +
                " INNER JOIN school sl ON sl.id = sc.school_id \n" +
                " INNER JOIN class c ON sc.class_id = c.id \n" +
                " INNER JOIN section cs ON sc.section_id = cs.id\n" +
                " LEFT JOIN student_attendance sa ON s.id = sa.student_id \n" +
                " LEFT JOIN attendance a ON a.id = sa.attendance_id WHERE sc.school_id = @SchoolId\n" +
                " AND a.for_date >= '@fromDate' and a.for_date <= '@toDate' and  sa.isabsent is not 0\n" +
                " ORDER BY s.student_gr_no) ss\n" +
                " group by ss.id\n" +
                " )\n" +
                " )  where attPerct < 85 \n" +
                " order by attPerct limit 5\n" +
                "\n";


        sqlquerry = sqlquerry.replace("@SchoolId", schoolID);
        sqlquerry = sqlquerry.replace("@toDate", toDate);
        sqlquerry = sqlquerry.replace("@fromDate", fromDate);


        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                do {

                    sbfm = new BottomFiveStudentsModel();
                    sbfm.setStudentsAbsentCounting(cursor.getInt(cursor.getColumnIndex("attPerct"))); //no attendance days to be added up with total absents
                    sbfm.setStudentsID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sbfm.setStudntsName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sbfm.setStudentGr_NO(cursor.getInt(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sbfm.setSchoolId(cursor.getInt(cursor.getColumnIndex("school_id")));
                    sbfm.setClassName(cursor.getString(cursor.getColumnIndex("class_name")));
                    sbfm.setSectionName(cursor.getString(cursor.getColumnIndex("section_name")));
                    sbfm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                    sbfm.setSchoolName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    list.add(sbfm);
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
        return list;
    }

    public ArrayList<BottomFiveStudentsModel> getBottomFiveStudent(String schoolID, String month, String Year) {
        ArrayList<BottomFiveStudentsModel> list = new ArrayList<>();
        BottomFiveStudentsModel sbfm;
        Cursor cursor = null;


        String sqlquerry = "SELECT s.id ,s.schoolclass_id , s.student_name , s.student_gr_no , s.is_active, sc.school_id  , \n" +
                "c.name AS CLASS_NAME ,absentPercent, sec.name AS SECTION_NAAME, sch.school_name \n" +
                "FROM student s \n" +
                "INNER JOIN (\n" +
                "SELECT  100 - (AVG(1.00*daysAbsent/classStrength)*100) AS absentPercent, student_id FROM (\n" +
                "\tSELECT COUNT(*) AS daysAbsent, (SELECT COUNT(*) FROM Student WHERE schoolclass_id = sc.id) AS classStrength, a.for_date, sc.id, s.id AS student_id\n" +
                "\tFROM Student s INNER JOIN School_Class sc ON sc.id = s.schoolclass_id\n" +
                "\tINNER JOIN Student_Attendance sa ON sa.student_id = s.id\n" +
                "\tINNER JOIN Attendance a ON a.id = sa.attendance_id AND a.school_class_id = sc.id \n" +
                "\tWHERE COALESCE(NULLIF(sa.isabsent,''), 'P') <> 'P'\n" +
                " AND strftime('%m',a.for_date) = '@month' AND strftime('%Y',a.for_date) = '@Year' GROUP BY a.for_date, sc.id, s.id) AS absentCount\n" +
                "\tGROUP BY student_id\n" +
                "\tORDER BY absentPercent DESC LIMIT 5) AS studentAttendance ON studentAttendance.student_id = s.id\n" +
                "INNER JOIN school_class sc on sc.id = s.schoolclass_id \n" +
                "INNER JOIN class c on c.id = sc.class_id \n" +
                "INNER JOIN section sec on sec.id = sc.section_id \n" +
                "INNER JOIN school sch on sch.id = sc.school_id \n" +
                "WHERE sc.school_id \n" +
                "IN(@schoolID) AND s.is_active = '1' AND absentPercent < 85\n \n";


        sqlquerry = sqlquerry.replace("@schoolID", schoolID);
        sqlquerry = sqlquerry.replace("@Year", schoolID);
        sqlquerry = sqlquerry.replace("@month", schoolID);

        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                do {

                    sbfm = new BottomFiveStudentsModel();
                    sbfm.setStudentsAbsentCounting(cursor.getInt(cursor.getColumnIndex("absentPercent")));
                    sbfm.setStudentsID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sbfm.setStudntsName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sbfm.setStudentGr_NO(cursor.getInt(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sbfm.setSchoolId(cursor.getInt(cursor.getColumnIndex("school_id")));
                    sbfm.setClassName(cursor.getString(cursor.getColumnIndex("CLASS_NAME")));
                    sbfm.setSectionName(cursor.getString(cursor.getColumnIndex("SECTION_NAAME")));
                    sbfm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                    sbfm.setSchoolName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    list.add(sbfm);
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
        return list;
    }

    public ArrayList<BottomFiveStudentsModel> getAllBottomStudent(String schoolID, String month, String Year) {
        ArrayList<BottomFiveStudentsModel> list = new ArrayList<>();
        BottomFiveStudentsModel sbfm;
        Cursor cursor = null;

        String sqlquerry = "SELECT s." + KEY_ID + " ,s." + STUDENT_SCHOOL_CLASS_ID + " , s." + STUDENT_NAME + " , s." + STUDENT_GR_NO + " , s." + STUDENT_IS_ACTIVE
                + ", sa." + ATTENDANCE_IS_ABSENT + " , c." + CLASS_NAME + " AS CLASS_NAME , sec." + SECTION_NAME
                + " AS SECTION_NAAME , count(*) AS  counting , s." + STUDENT_CONTACT_NUMBERS + " FROM " + TABLE_STUDENT + " s "
                + " INNER JOIN " + TABLE_ATTENDANCE + " a on a." + KEY_ID + " = sa." + ATTENDANCE_ID
                + " INNER JOIN " + TABLE_ATTENDANCE_STUDENT + " sa on sa." + ATTENDANCE_STUDENT_ID + " = s." + KEY_ID
                + " INNER JOIN " + TABLE_SCHOOL_CLASS + " sc on sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                + " LEFT JOIN " + TABLE_CLASS + " c on c." + KEY_ID + " = sc." + SCHOOL_CLASS_CLASSID
                + " LEFT JOIN " + TABLE_SECTION + " sec on sec." + KEY_ID + " = sc." + SCHOOL_CLASS_SECTIONID
                + " where sc." + SCHOOL_CLASS_SCHOOLID + " IN(" + schoolID + ")"
                + " AND strftime('%m',a." + ATTENDANCE_FOR_DATE + ") = '" + month + "'"
                + " AND strftime('%Y',a." + ATTENDANCE_FOR_DATE + ") = '" + Year + "'"
                + " AND s." + STUDENT_IS_ACTIVE + " = '1'"
                + " GROUP BY s." + STUDENT_NAME + " Order by counting desc";

        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                do {

                    sbfm = new BottomFiveStudentsModel();
                    sbfm.setStudentsAbsentCounting(cursor.getInt(cursor.getColumnIndex("counting")));
                    sbfm.setStudentsID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sbfm.setStudntsName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    sbfm.setStudentGr_NO(cursor.getInt(cursor.getColumnIndex(STUDENT_GR_NO)));
                    sbfm.setClassName(cursor.getString(cursor.getColumnIndex("CLASS_NAME")));
                    sbfm.setSectionName(cursor.getString(cursor.getColumnIndex("SECTION_NAAME")));
                    sbfm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID)));
                    sbfm.setContactNumber(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                    list.add(sbfm);
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
        return list;
    }

    public ArrayList<HighestDuesStudentsModel> gethighestDuesStudent(String schoolID) {
        ArrayList<HighestDuesStudentsModel> list = new ArrayList<>();
        HighestDuesStudentsModel sbfm;
        Cursor cursor = null;
        String sqlquerry;
//        if (schoolID > 0) {

//        sqlquerry = "select sch.school_name as School,s.student_name as Name,student_gr_no as GRNo,c.name as Class,sec.name as Section,c.rank as class_rank," +
//                " SUM(fd.fee_amount) as Amount" +
//                " FROM FeesHeader fh " +
//                " inner join FeesDetail fd ON fh.id = fd.feesHeader_id" +
//                " inner join school_class sc on sc.id = fh.schoolclass_id " +
//                " inner join student s on s.id = fh.student_id " +
//                " inner join class c on c.id = sc.class_id " +
//                " inner join section sec on sec.id = sc.section_id " +
//                " inner join school sch on sch.id = sc.school_id " +
//                " and sch.id IN(@SchoolID)" +
//                " group by s.id" +
//                " order by amount desc limit 5";

        sqlquerry = "select sch.school_name as School,s.student_name as Name,student_gr_no as GRNo,c.name as Class,sec.name as Section,c.rank as class_rank," +
                " SUM(CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 1 THEN 1 * fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 2 THEN -1 * fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 4 THEN -1 * fd.fee_amount" +
                " END END END END) as Amount" +
                " FROM FeesHeader fh " +
                " inner join FeesDetail fd ON fh.id = fd.feesHeader_id" +
                " inner join school_class sc on sc.id = fh.schoolclass_id  " +
                " inner join student s on s.id = fh.student_id " +
                " inner join class c on c.id = sc.class_id " +
                " inner join section sec on sec.id = sc.section_id " +
                " inner join school sch on sch.id = sc.school_id " +
                " and sch.id IN(@SchoolID)" +
                " group by s.id" +
                " order by amount desc limit 5";

//        sqlquerry = "select sch.school_name as School,s.student_name as Name,student_gr_no as GRNo,c.name as Class,sec.name as Section," +
//                " SUM(CASE WHEN fh.Category_Id = 2 THEN -1 * fd.fee_amount" +
//                " ELSE CASE WHEN fh.Category_Id = 1 THEN fd.fee_amount END END) as Amount" +
//                " FROM FeesHeader fh " +
//                " inner join FeesDetail fd ON fh.id = fd.feesHeader_id" +
//                " inner join school_class sc on sc.id = fh.schoolclass_id  " +
//                " inner join student s on s.id = fh.student_id " +
//                " inner join class c on c.id = sc.class_id " +
//                " inner join section sec on sec.id = sc.section_id " +
//                " inner join school sch on sch.id = sc.school_id " +
//                " and sch.id IN(@SchoolID)" +
//                " group by s.id" +
//                " order by amount desc limit 5";

//        sqlquerry = "select sch.school_name as School,s.student_name as Name,student_gr_no as GRNo,c.name as Class,sec.name as Section,\n" +
//                "(ifnull(fees_admission,0)+ifnull(fees_exam,0)+ifnull(fees_tution,0)+ifnull(fees_books,0)+ifnull(fees_copies,0)+ifnull(fees_uniform,0)+ifnull(fees_others,0)) as Amount\n" +
//                "from AppInvoice ar\n" +
//                "inner join school_class sc on sc.id = ar.schoolclass_id  \n" +
//                "inner join student s on s.id = ar.student_id \n" +
//                "inner join class c on c.id = sc.class_id \n" +
//                "inner join section sec on sec.id = sc.section_id \n" +
//                "inner join school sch on sch.id = sc.school_id \n" +
//                "and sch.id IN(@SchoolID)\n" +
//                "group by s.id\n" +
//                "order by amount desc limit 5\n";

        sqlquerry = sqlquerry.replace("@SchoolID", String.valueOf(schoolID));

        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(cursor.getColumnIndex("Amount")) > 0) {
                        sbfm = new HighestDuesStudentsModel();
                        sbfm.setSchoolName(cursor.getString(cursor.getColumnIndex("School")));
                        sbfm.setStudntsName(cursor.getString(cursor.getColumnIndex("Name")));
                        sbfm.setStudentGr_NO(cursor.getString(cursor.getColumnIndex("GRNo")));
                        sbfm.setClassName(cursor.getString(cursor.getColumnIndex("Class")));
                        sbfm.setSectionName(cursor.getString(cursor.getColumnIndex("Section")));
                        sbfm.setAmount(cursor.getString(cursor.getColumnIndex("Amount")));
                        sbfm.setClassRank(cursor.getInt(cursor.getColumnIndex("class_rank")));
                        list.add(sbfm);
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
        return list;
    }

    public ArrayList<HighestDuesStudentsModel> getAllhighestDuesStudent(String schoolID) {
        ArrayList<HighestDuesStudentsModel> list = new ArrayList<>();
        HighestDuesStudentsModel sbfm;
        Cursor cursor = null;
        String sqlquerry;
//        if (schoolID > 0) {

        sqlquerry = "select sch.school_name as School,s.student_name as Name,student_gr_no as GRNo,c.name as Class,sec.name as Section,c.rank as class_rank," +
                " SUM(CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 1 THEN 1 * fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 2 THEN -1 * fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 4 THEN -1 * fd.fee_amount" +
                " END END END END) as Amount" +
                " FROM FeesHeader fh " +
                " inner join FeesDetail fd ON fh.id = fd.feesHeader_id" +
                " inner join school_class sc on sc.id = fh.schoolclass_id  " +
                " inner join student s on s.id = fh.student_id " +
                " inner join class c on c.id = sc.class_id " +
                " inner join section sec on sec.id = sc.section_id " +
                " inner join school sch on sch.id = sc.school_id " +
                " and sch.id IN(@SchoolID)" +
                " group by s.id" +
                " order by amount desc";

        sqlquerry = sqlquerry.replace("@SchoolID", String.valueOf(schoolID));

        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                do {

                    sbfm = new HighestDuesStudentsModel();
                    sbfm.setSchoolName(cursor.getString(cursor.getColumnIndex("School")));
                    sbfm.setStudntsName(cursor.getString(cursor.getColumnIndex("Name")));
                    sbfm.setStudentGr_NO(cursor.getString(cursor.getColumnIndex("GRNo")));
                    sbfm.setClassName(cursor.getString(cursor.getColumnIndex("Class")));
                    sbfm.setSectionName(cursor.getString(cursor.getColumnIndex("Section")));
                    sbfm.setAmount(cursor.getString(cursor.getColumnIndex("Amount")));
                    sbfm.setClassRank(cursor.getInt(cursor.getColumnIndex("class_rank")));
                    list.add(sbfm);
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
        return list;
    }

    public int getEnrollmentsCount(int School_id, String month, String year) {
        Cursor cursor = null;
        String query = "SELECT COUNT(" + KEY_ID + ") as COUNT FROM "
                + TABLE_ENROLLMENT + " WHERE " + ENROLLMENT_SCHOOL_ID
                + " = '" + School_id + "'" +
                " AND strftime ('%m', "
                + ENROLLMENT_CREATED_ON + ")= '"
                + month + "'" +
                " AND strftime ('%Y', "
                + ENROLLMENT_CREATED_ON + ")= '"
                + year + "'";
        int studentCount = 0;
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                studentCount = cursor.getInt(cursor.getColumnIndex("COUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return studentCount;
    }

    public int getAllEnrollmentsCount(int School_id) {
        Cursor cursor = null;
        String query = "SELECT COUNT(" + KEY_ID + ") as COUNT FROM "
                + TABLE_ENROLLMENT + " WHERE " + ENROLLMENT_SCHOOL_ID
                + " = '" + School_id + "'";
        int studentCount = 0;
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                studentCount = cursor.getInt(cursor.getColumnIndex("COUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return studentCount;
    }

    public int getIncompleteProfileCounts(int SchoolId) {
        Cursor cursor = null;
        int InCompleteProfileCount = 0;

        String query = "SELECT COUNT(*) AS COUNT FROM "
                + TABLE_ENROLLMENT + " WHERE "
                + ENROLLMENT_SCHOOL_ID + "= "
                + SchoolId
                + " AND " + ENROLLMENT_REVIEW_STATUS + "= '"
                + AppConstants.PROFILE_INCOMPLETE_KEY + "'";
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                InCompleteProfileCount = cursor.getInt(cursor.getColumnIndex("COUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return InCompleteProfileCount;
    }

    public int getStudentFromStudentId(String studentId) {
        Cursor cursor = null;
        int school_class_id = 0;

        String query = "SELECT * FROM "
                + TABLE_STUDENT + " WHERE "
                + KEY_ID + "= "
                + studentId;
        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                school_class_id = cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return school_class_id;
    }

    public int getSchoolAbsentCountToday(int Schoolid, String month, String year, String Day) {
        int count = 0;

        String query = "SELECT COUNT(*) AS COUNT FROM " + TABLE_ATTENDANCE_STUDENT + " sa "
                + " INNER JOIN " + TABLE_ATTENDANCE + " a on sa." + ATTENDANCE_ID + " = a." + KEY_ID
                + " LEFT JOIN " + TABLE_SCHOOL_CLASS + " sc on sc." + KEY_ID + " = a." + ATTENDANCE_SCHOOL_CLASS_ID
                + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + Schoolid
                + " AND strftime ('%m', a." + ATTENDANCE_FOR_DATE + ")= '" + month + "'"
                + " AND strftime ('%Y', a." + ATTENDANCE_FOR_DATE + ") = '" + year + "'";


        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("COUNT"));
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

    public ArrayList<BottomFiveSchoolAreaManagerModel> getBottomFiveAreaMAngerSchool() {
        Cursor cursor = null;
        ArrayList<BottomFiveSchoolAreaManagerModel> list = new ArrayList<>();
        int count = 0;
        String query = "SELECT us." + SCHOOL_ID + " , s." + SCHOOL_NAME + " from " + TABLE_USER_SCHOOL + " us "
                + " INNER JOIN " + TABLE_SCHOOL + " s on s." + KEY_ID + " = us." + SCHOOL_ID
                + " GROUP BY s." + SCHOOL_NAME;


        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    BottomFiveSchoolAreaManagerModel schoolModel = new BottomFiveSchoolAreaManagerModel();
                    schoolModel.setSchoolID(cursor.getInt(cursor.getColumnIndex(SCHOOL_ID)));
                    schoolModel.setStudentSchoolName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    list.add(schoolModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public int getPromotedSchoolClassId(int classId) {
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        int schoolClassID = -1;
        try {
            String query = "SELECT " + KEY_ID + " FROM " + TABLE_SCHOOL_CLASS + " WHERE " + SCHOOL_CLASS_CLASSID + " = " + classId;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                schoolClassID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return schoolClassID;
    }


    public SchoolAuditModel getlastVisitedSchoolAudit(int SchoolId) {
        SchoolAuditModel sam = null;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SCHOOL_AUDIT
                    + " WHERE " + SCHOOL_AUDIT_SCHOOL_ID + " =" + SchoolId
                    + " ORDER BY " + KEY_ID + " DESC";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                sam = new SchoolAuditModel();
                sam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                sam.setVisit_date(cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_VISIT_DATE)));
                sam.setApproved_by(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_APPROVED_BY)));
                sam.setSchool_id(cursor.getInt(cursor.getColumnIndex(SCHOOL_AUDIT_SCHOOL_ID)));
                boolean b = cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_IS_APPROVED)).equals("1") ? true : false;
                sam.setIs_approved(b);
                sam.setRemarks(cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_REMARKS)));
                sam.setUploaded_on(cursor.getString(cursor.getColumnIndex(SCHOOL_AUDIT_UPLOADED_ON)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sam;

    }

    public long setSchoolClassActive(int id) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(SCHOOL_CLASS_IS_ACTIVE, true);

            long i = DB.update(TABLE_SCHOOL_CLASS, values, KEY_ID + " = " + id, null);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public StudentModel getStudentWithReceiptNo(long receiptNO, int schoolId) {
        String selectQuery = "SELECT s.*,sc.class_id, c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name  FROM FeesHeader fh \n" +
                " INNER JOIN FeesDetail fd ON fh.id = fd.feesHeader_id " +
                " INNER JOIN school_class sc on sc.id = fh.schoolclass_id and sc.school_ID = " + schoolId +
                " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID +
                " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID +
                " INNER JOIN student s on s.id = fh.student_id and fh.receipt_no = " + receiptNO +
                " GROUP BY s.id";
        Cursor cursor = null;
        StudentModel sm = new StudentModel();
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                sm.setWithdrawalReasonId(cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID)));
                sm.setScholarshipCategoryId((cursor.getInt(cursor.getColumnIndex(STUDENT_ScholarshipCategory_ID))));
                sm.setActualFees((cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees))));
                boolean isWithdraw = cursor.getString(cursor.getColumnIndex(STUDENT_IS_WITHDRAWN)).equals("1");
                sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                sm.setWithdrawal(isWithdraw);
                boolean isActive = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");
                sm.setActive(isActive);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sm;
    }


    public String getStudentImageWithGR(int grNO, int schoolClassid) {
        String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolClassid + " AND s." + STUDENT_GR_NO + " = " + grNO;

//        String query = "SELECT * FROM " + TABLE_STUDENT + " WHERE " + STUDENT_GR_NO + " = " + grNO + " AND " + STUDENT_SCHOOL_CLASS_ID + " = " + schoolClassid;
        Cursor cursor = null;
        String studentImage = "";
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                studentImage = cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return studentImage;
    }


    public StudentModel getStudentwithGR(int grNO, int schoolClassid) {
        String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolClassid + " AND s." + STUDENT_GR_NO + " = " + grNO;

//        String query = "SELECT * FROM " + TABLE_STUDENT + " WHERE " + STUDENT_GR_NO + " = " + grNO + " AND " + STUDENT_SCHOOL_CLASS_ID + " = " + schoolClassid;
        Cursor cursor = null;
        StudentModel sm = new StudentModel();
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                sm.setWithdrawalReasonId(cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID)));
                sm.setScholarshipCategoryId((cursor.getInt(cursor.getColumnIndex(STUDENT_ScholarshipCategory_ID))));
                sm.setActualFees((cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees))));
                boolean isWithdraw = cursor.getString(cursor.getColumnIndex(STUDENT_IS_WITHDRAWN)).equals("1");
                sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                sm.setWithdrawal(isWithdraw);
                boolean isActive = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");
                sm.setActive(isActive);

                sm.setOrphan(cursor.getString(cursor.getColumnIndex(STUDENT_IS_ORPHAN)));
                sm.setDisabled(cursor.getString(cursor.getColumnIndex(STUDENT_IS_DISABLED)));
                sm.setReligion(cursor.getString(cursor.getColumnIndex(STUDENT_RELIGION)));
                sm.setNationality(cursor.getString(cursor.getColumnIndex(STUDENT_NATIONALITY)));
                sm.setElectiveSubjectId(cursor.getInt(cursor.getColumnIndex(STUDENT_ELECTIVE_SUBJECT_ID)));
                sm.setEmail(cursor.getString(cursor.getColumnIndex(STUDENT_EMAIL)));
                sm.setDeathCert_Image(cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE)));
                sm.setMedicalCert_Image(cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE)));
                sm.setbForm_Image(cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE)));
                sm.setDeathCert_Image_UploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON)));
                sm.setMedicalCert_Image_UploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON)));
                sm.setbForm_Image_UploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE_UPLOADED_ON)));
                sm.setStudent_promotionComments(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_COMMENTS)));
                sm.setStudent_promotionStatus(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_STATUS)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sm;
    }

    public StudentModel getStudentwithGRNoAndSCId(int grNO, int schoolClassid) {
        String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                + " WHERE sc." + KEY_ID + " = " + schoolClassid + " AND s." + STUDENT_GR_NO + " = " + grNO;

//        String query = "SELECT * FROM " + TABLE_STUDENT + " WHERE " + STUDENT_GR_NO + " = " + grNO + " AND " + STUDENT_SCHOOL_CLASS_ID + " = " + schoolClassid;
        Cursor cursor = null;
        StudentModel sm = new StudentModel();
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                sm.setWithdrawalReasonId(cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID)));
                sm.setScholarshipCategoryId((cursor.getInt(cursor.getColumnIndex(STUDENT_ScholarshipCategory_ID))));
                sm.setActualFees((cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees))));
                boolean isWithdraw = cursor.getString(cursor.getColumnIndex(STUDENT_IS_WITHDRAWN)).equals("1");
                sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                sm.setWithdrawal(isWithdraw);
                boolean isActive = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");
                sm.setActive(isActive);

                sm.setNationality(cursor.getString(cursor.getColumnIndex(STUDENT_NATIONALITY)));
                sm.setReligion(cursor.getString(cursor.getColumnIndex(STUDENT_RELIGION)));
                sm.setEmail(cursor.getString(cursor.getColumnIndex(STUDENT_EMAIL)));
                sm.setElectiveSubjectId(cursor.getInt(cursor.getColumnIndex(STUDENT_ELECTIVE_SUBJECT_ID)));
                sm.setDeathCert_Image(cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE)));
                sm.setDeathCert_Image_UploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON)));
                sm.setMedicalCert_Image(cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE)));
                sm.setMedicalCert_Image_UploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON)));
                sm.setbForm_Image(cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE)));
                sm.setbForm_Image_UploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE_UPLOADED_ON)));
                sm.setStudent_promotionComments(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_COMMENTS)));
                sm.setStudent_promotionStatus(cursor.getString(cursor.getColumnIndex(STUDENT_PROMOTION_STATUS)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sm;
    }

    public int getAllPendingRecords(int schoolId) {
        int count = 0;
        String query = "select uploaded_on from enrollment where (uploaded_on is null or uploaded_on = \"\") and enrollment.school_id = " + schoolId +
                " and review_status == 'C'" +
                " union all select uploaded_on from attendance join school_class on school_class.id = attendance.school_class_id " +
                " where (uploaded_on is null or uploaded_on = \"\" ) and school_class.school_id = " + schoolId +
                " union all select uploaded_on from promotion join school_class on school_class.id = promotion.school_class_id " +
                " where (uploaded_on is null or uploaded_on = \"\") and school_class.school_id = " + schoolId +
                " union all select uploaded_on from school_audit where (uploaded_on is null or uploaded_on = \"\") and school_audit.school_id = " + schoolId +
                " union all select uploaded_on from student join school_class on school_class.id = student.schoolclass_id " +
                " where (uploaded_on is null or uploaded_on = \"\") and school_class.school_id = " + schoolId +
                " union all select uploaded_on from FeesHeader join school_class on school_class.id = FeesHeader.schoolclass_id " +
                " where (uploaded_on is null or uploaded_on = \"\") and school_class.school_id = " + schoolId +
                " union all select uploaded_on from CashDeposit  where (uploaded_on is null or uploaded_on = \"\") and school_id = " + schoolId +
                " union all select uploaded_on from withdrawal where (uploaded_on is null or uploaded_on = \"\") and withdrawal.school_id = " + schoolId +
                " union all select uploaded_on from EmployeeDetails join EmployeeSchool on EmployeeSchool.emp_detail_id = EmployeeDetails.id" +
                " where (uploaded_on is null or uploaded_on = \"\" ) and EmployeeSchool.SchoolID = " + schoolId +
                " union all select uploaded_on from EmployeesLeaves " +
                " where (uploaded_on is null or uploaded_on = \"\" ) and EmployeesLeaves.SchoolID = " + schoolId +
                " union all select uploaded_on from EmployeeResignation" +
                " where (uploaded_on is null or uploaded_on = \"\" ) and EmployeeResignation.schoolId = " + schoolId +
                " union all select uploaded_on from EmployeeTeacherAttendance" +
                " where (uploaded_on is null or uploaded_on = \"\" ) and EmployeeTeacherAttendance.SchoolID = " + schoolId +
                " union all select image_file_uploaded_on from student join school_class on school_class.id = student.schoolclass_id " +
                " where (ifnull(image_file_uploaded_on,'') == '') AND (ifnull(student.image_file_name,'') != '' AND student.image_file_name not like '%BodyPart%') and school_class.school_id = " + schoolId +
                " union all select Uploaded_On from TCT_EMP_SUBJECT_TAGGING " +
                " where (Uploaded_On is null or Uploaded_On = \"\") and SchoolID = " + schoolId +
                " union all select SeparationImages.uploaded_on from SeparationImages join EmployeeResignation on EmployeeResignation.id = SeparationImages.Resignation_id" +
                " where (SeparationImages.uploaded_on is null or SeparationImages.uploaded_on = \"\" ) and EmployeeResignation.schoolId = " + schoolId +
                " union all select ExpenseTransactions.uploaded_on from ExpenseTransactions" +
                " where (ExpenseTransactions.uploaded_on is null or ExpenseTransactions.uploaded_on = \"\" ) and ExpenseTransactions.school_id = " + schoolId +
                " union all select ExpenseTransactionImages.uploaded_on from ExpenseTransactionImages join ExpenseTransactions on ExpenseTransactions.id = ExpenseTransactionImages.transaction_id" +
                " where (ExpenseTransactionImages.uploaded_on is null or ExpenseTransactionImages.uploaded_on = \"\" ) and ExpenseTransactions.school_id = " + schoolId +
                " union all select ExpenseSubHeadLimitsMonthly.uploaded_on from ExpenseSubHeadLimitsMonthly" +
                " where (ExpenseSubHeadLimitsMonthly.uploaded_on is null or ExpenseSubHeadLimitsMonthly.uploaded_on = \"\" ) and ExpenseSubHeadLimitsMonthly.school_id = " + schoolId +
                " union all select ExpenseSchoolPettyCashMonthlyLimits.uploaded_on from ExpenseSchoolPettyCashMonthlyLimits" +
                " where (ExpenseSchoolPettyCashMonthlyLimits.uploaded_on is null or ExpenseSchoolPettyCashMonthlyLimits.uploaded_on = \"\" ) and ExpenseSchoolPettyCashMonthlyLimits.school_id = " + schoolId +
                " union all select ExpenseAmountClosing.uploaded_on from ExpenseAmountClosing" +
                " where (ExpenseAmountClosing.uploaded_on is null or ExpenseAmountClosing.uploaded_on = \"\" ) and ExpenseAmountClosing.school_id = " + schoolId +
                " union all select User_Images.uploaded_on from User_Images join EmployeeSchool on EmployeeSchool.emp_detail_id = User_Images.emp_detail_id" +
                " where (uploaded_on is null or uploaded_on = \"\" ) and EmployeeSchool.SchoolID = " + schoolId +
                " union all select " + STUDENT_BFORM_IMAGE_UPLOADED_ON + " from student join school_class on school_class.id = student.schoolclass_id " +
                " where (ifnull(" + STUDENT_BFORM_IMAGE_UPLOADED_ON + ",'') == '') AND (ifnull(student." + STUDENT_BFORM_IMAGE + ",'') != '' AND student." + STUDENT_BFORM_IMAGE + " not like '%BodyPart%') and school_class.school_id = " + schoolId +
                " union all select " + STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON + " from student join school_class on school_class.id = student.schoolclass_id " +
                " where (ifnull(" + STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON + ",'') == '') AND (ifnull(student." + STUDENT_DEATH_CERT_IMAGE + ",'') != '' AND student." + STUDENT_DEATH_CERT_IMAGE + " not like '%BodyPart%') and school_class.school_id = " + schoolId +
                " union all select " + STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON + " from student join school_class on school_class.id = student.schoolclass_id " +
                " where (ifnull(" + STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON + ",'') == '') AND (ifnull(student." + STUDENT_MEDICAL_CERT_IMAGE + ",'') != '' AND student." + STUDENT_MEDICAL_CERT_IMAGE + " not like '%BodyPart%') and school_class.school_id = " + schoolId;


        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return count;
        }

    }

    public int getPendingSeparationRecordsForSync() {
        int count = 0;
        String query = "select ps.uploaded_on from PendingSeparations ps join user usr on usr.id = ps.approver_userId\n" +
                " where (ps.uploaded_on is null or ps.uploaded_on = \"\")";

        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return count;
        }

    }


    public String getLastModifiedOnForEmployees(int schoolId) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(modified_on) as modified_on from EmployeeDetails inner join EmployeeSchool emp_school ON emp_school.emp_detail_id = emp_detail_id" +
                " WHERE emp_school.SchoolID = " + schoolId;
        String modified_on = "";

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                modified_on = AppModel.getInstance().convertDatetoFormat(cursor.getString(cursor.getColumnIndex("modified_on")), "yyyy-MM-dd hh:mm:ss", "dd-MM-yyyy");
            }
            if (modified_on == null || modified_on.isEmpty())
                modified_on = "01-03-2017";
            return modified_on;
        } catch (Exception e) {
            e.printStackTrace();
            return "01-03-2017";
        }
    }


    public String getLatestModifiedOn(int schoolId) {
        Cursor c = null;
        try {
            SQLiteDatabase db = getDB();
            String lmo = null;
            String selectQuery = "Select s.modified_on from student s" +
                    " WHERE s.schoolclass_id IN (select id from school_class where school_id IN (@SchoolId)) order by s.modified_on  desc  LIMIT 1";

            selectQuery = selectQuery.replace("@SchoolId", schoolId + "");

            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                lmo = AppModel.getInstance().convertDatetoFormat(c.getString(c.getColumnIndex(STUDENT_MODIFIED_ON)), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy");
            }

            if (lmo == null) {
                lmo = "";
//                lmo = "01-01-2000";
            }
            return lmo;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
//            return "01-01-2000";
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public long insertIntoCashDeposit(CashDepositModel model) {

        SQLiteDatabase db = getDB();
        String fileName = model.getDepositSlipFilePath() != null ? new File(model.getDepositSlipFilePath()).getName() : null;
        try {
            ContentValues values = new ContentValues();
            values.put(CashDeposit.CREATED_BY, model.getCreatedBy());
            values.put(CashDeposit.CREATED_ON, model.getCreatedOn());
            values.put(CashDeposit.SCHOOL_ID, model.getSchoolId());
            values.put(CashDeposit.DEPOSIT_SLIP_NO, model.getDepositSlipNo());
            values.put(CashDeposit.DEPOSIT_AMOUNT, model.getDepositAmount());
            values.put(CashDeposit.PICTURE_SLIP_FILENAME, fileName);
//            values.put(CashDeposit.PICTURE_SLIP_FILENAME, model.getDepositSlipFilePath());
            values.put(CashDeposit.DEVICE_ID, model.getDeviceId());
            values.put(CashDeposit.REMARKS, model.getRemarks());

            return db.insert(CashDeposit.CASH_DEPOSIT_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
        }


    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = getDB();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }

    }


    public List<ViewSSRTableModel> getViewSSRGraphData(int schoolId, String fromDate, String toDate, int filter) {

        Cursor cursor = null;
        int male = 0, female = 0;
        String sqlquery = "";
        List<ViewSSRTableModel> viewSSRTableModels = new ArrayList<>();
        String rep_month = "";

        if (filter == 0) sqlquery = GET_GRAPH_TOTAL_SSR;
        else if (filter == 1) sqlquery = GET_GRAPH_NEW_ADMISSION;
        else if (filter == 2) sqlquery = GET_GRAPH_WITHDRAWALS;


        sqlquery = sqlquery.replace("@SchoolId", schoolId + "");
        sqlquery = sqlquery.replace("@fromDate", fromDate);
        sqlquery = sqlquery.replace("@toDate", toDate);


        Log.d(filter + " SSR graph query", sqlquery);

        try {
            SQLiteDatabase db = getDB();

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

                    rep_month = cursor.getString(cursor.getColumnIndex("rep_month"));
                    ViewSSRTableModel model = new ViewSSRTableModel(male, female, (male + female));
                    model.setRep_month(rep_month);
                    model.setGender(cursor.getString(cursor.getColumnIndex("Gender")));
                    viewSSRTableModels.add(model);

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

        return viewSSRTableModels;

    }

    public ViewSSRTableModel getViewSSRTableData(int schoolId, String fromDate, String toDate, int filter) {
        Cursor cursor = null;
        int male = 0, female = 0;
        String sqlquery = "";
        ViewSSRTableModel previousMonthSSRByGender = null;

        if (filter == 0) sqlquery = GET_PREV_MONTH_SSR;
        else if (filter == 1) sqlquery = GET_NEW_ADMISSION;
        else if (filter == 2) sqlquery = GET_WITHDRAWALS;
        else if (filter == 3) sqlquery = GET_GRADUATES;


        sqlquery = sqlquery.replace("@SchoolId", schoolId + "");
        sqlquery = sqlquery.replace("@fromDate", fromDate);
        sqlquery = sqlquery.replace("@toDate", toDate);


        Log.d(filter + " query", sqlquery.toString());

        try {
            SQLiteDatabase db = getDB();

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
                previousMonthSSRByGender = new ViewSSRTableModel(male, female, (male + female));
                cursor.close();
            }
        }

        return previousMonthSSRByGender;
    }

    public boolean removeStudent(StudentModel student) {
        try {
            SQLiteDatabase db = getDB();
            return db.delete(TABLE_STUDENT, KEY_ID + "=" + student.getId(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public CalendarsModel checkDateForAttendance(String date, String schoolId) {
        String query = "select cal.* from calendar cal inner join school s on s.id = cal.school_id and cal.academic_session_id = s.academic_session_id\n" +
                "where date(activity_start_date) <= '@Date'  and   date(activity_end_date) >= '@Date' and cal.school_id = @SchoolId and holiday_type_id in (4,3,2)\n" +
                "and cal.isActive = 1";

        query = query.replace("@SchoolId", schoolId);
        query = query.replace("@Date", date);

        SQLiteDatabase db = getDB();


        CalendarsModel cm = null;

        try {

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                cm = new CalendarsModel();
                cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                cm.setActivity_Start_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date)));
                cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return cm;
    }

    public List<CalendarsModel> getAllDatesForHolidays(String schoolId) {
        String query = "select cal.* from calendar cal inner join school s on s.id = cal.school_id and cal.academic_session_id = s.academic_session_id\n" +
                "where cal.school_id = @SchoolId and holiday_type_id in (4,3,2) and cal.isActive = 1";

        query = query.replace("@SchoolId", schoolId);

        SQLiteDatabase db = getDB();
        List<CalendarsModel> calendarsModels = new ArrayList<>();

        try {

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    String startDate = cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date));
                    String endDate = cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date));
                    if (!startDate.equals(endDate)) {
                        List<String> dateList = AppModel.getInstance().getDatesBetweenTwoDates(startDate, endDate, "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'00:00:00");
                        for (String start : dateList) {
                            CalendarsModel cm = new CalendarsModel();
                            cm.setActivity_Start_Date(start);
                            cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                            cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                            cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                            cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                            cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                            cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                            cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));
                            calendarsModels.add(cm);
                        }

                    } else {
                        CalendarsModel cm = new CalendarsModel();
                        cm.setActivity_Start_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date)));
                        cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                        cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                        cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                        cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                        cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                        cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                        cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));
                        calendarsModels.add(cm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendarsModels;
    }

    public List<CalendarsModel> getAllDatesForHolidaysForEmployee(String schoolId) {
        String query = "select cal.* from calendar cal inner join school s on s.id = cal.school_id and cal.academic_session_id = s.academic_session_id\n" +
                "where cal.school_id = @SchoolId and holiday_type_id in (4,3) and cal.isActive = 1";

        query = query.replace("@SchoolId", schoolId);

        SQLiteDatabase db = getDB();
        List<CalendarsModel> calendarsModels = new ArrayList<>();

        try {

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    String startDate = cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date));
                    String endDate = cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date));
                    if (!startDate.equals(endDate)) {
                        List<String> dateList = AppModel.getInstance().getDatesBetweenTwoDates(startDate, endDate, "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'00:00:00");
                        for (String start : dateList) {
                            CalendarsModel cm = new CalendarsModel();
                            cm.setActivity_Start_Date(start);
                            cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                            cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                            cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                            cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                            cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                            cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                            cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));
                            calendarsModels.add(cm);
                        }

                    } else {
                        CalendarsModel cm = new CalendarsModel();
                        cm.setActivity_Start_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date)));
                        cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                        cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                        cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                        cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                        cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                        cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                        cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));
                        calendarsModels.add(cm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendarsModels;
    }

    public CalendarsModel checkDateForEmployeeTeacherAttendance(String date, String schoolId) {
        String query = "select cal.* from calendar cal inner join school s on s.id = cal.school_id and cal.academic_session_id = s.academic_session_id\n" +
                "where date(activity_start_date) <= '@Date'  and   date(activity_end_date) >= '@Date' and cal.school_id = @SchoolId and holiday_type_id in (4,3)\n" +
                "and cal.isActive = 1";

        query = query.replace("@SchoolId", schoolId);
        query = query.replace("@Date", date);

        SQLiteDatabase db = getDB();
        CalendarsModel cm = null;

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                cm = new CalendarsModel();
                cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                cm.setActivity_Start_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date)));
                cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return cm;
    }

    public long addCalendar(CalendarsModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(CALENDAR_Academic_Session_Id, cm.getSession_Id());
            values.put(CALENDAR_Activity_Id, cm.getC_Activity_Id());
            values.put(CALENDAR_Activity_Name, cm.getC_Activity_Name());
            values.put(CALENDAR_Activity_Start_Date, cm.getActivity_Start_Date());
            values.put(CALENDAR_Activity_End_Date, cm.getActivity_End_Date());
            values.put(CALENDAR_Holiday_Type_Id, cm.getC_Holiday_Type_Id());
            values.put(CALENDAR_Holiday_Type_Name, cm.getC_Holiday_Type_Name());
            values.put(SCHOOL_ID, cm.getSchoolId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_BY, cm.getModifiedBy());
            if (cm.getModifiedOn() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                cm.setModifiedOn(AppModel.getInstance().convertDatetoFormat(cm.getModifiedOn(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                values.put(MODIFIED_ON, cm.getModifiedOn());
            }

            long i = DB.insert(TABLE_CALENDAR, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateCalendar(CalendarsModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(CALENDAR_Academic_Session_Id, cm.getSession_Id());
            values.put(CALENDAR_Activity_Id, cm.getC_Activity_Id());
            values.put(CALENDAR_Activity_Name, cm.getC_Activity_Name());
            values.put(CALENDAR_Activity_Start_Date, cm.getActivity_Start_Date());
            values.put(CALENDAR_Activity_End_Date, cm.getActivity_End_Date());
            values.put(CALENDAR_Holiday_Type_Id, cm.getC_Holiday_Type_Id());
            values.put(CALENDAR_Holiday_Type_Name, cm.getC_Holiday_Type_Name());
            values.put(SCHOOL_ID, cm.getSchoolId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_BY, cm.getModifiedBy());
            if (cm.getModifiedOn() != null) {
                String dateFormat = "yyyy-MM-dd'T'hh:mm:ss";

                cm.setModifiedOn(AppModel.getInstance().convertDatetoFormat(cm.getModifiedOn(), dateFormat, "yyyy-MM-dd hh:mm:ss"));
                values.put(MODIFIED_ON, cm.getModifiedOn());
            }

            long i = DB.update(TABLE_CALENDAR, values, SCHOOL_ID + "=" + cm.getSchoolId() +
                    " AND " + CALENDAR_Activity_Start_Date + "= '" + cm.getActivity_Start_Date() + "'", null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean FindCalendarRecord(int schoolId, String startDate) {
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_CALENDAR + " WHERE " + SCHOOL_ID + " = " + schoolId
                    + " AND " + CALENDAR_Activity_Start_Date + " ='" + startDate + "'";

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


    public long insertCampus(CampusModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(CAMPUS_ID, cm.getId());
            values.put(CAMPUS_NAME, cm.getName());
            values.put(LOCATION_ID, cm.getLocationId());
            values.put(IsActive, cm.isActive());

            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.insert(TABLE_CAMPUS, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateCampus(CampusModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(CAMPUS_ID, cm.getId());
            values.put(CAMPUS_NAME, cm.getName());
            values.put(LOCATION_ID, cm.getLocationId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.update(TABLE_CAMPUS, values, CAMPUS_ID + "=" + cm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertLocation(LocationModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(LOCATION_ID, cm.getId());
            values.put(LOCATION_NAME, cm.getName());
            values.put(AREA_ID, cm.getAreaId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.insert(TABLE_LOCATION, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateLocation(LocationModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(LOCATION_ID, cm.getId());
            values.put(LOCATION_NAME, cm.getName());
            values.put(AREA_ID, cm.getAreaId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.update(TABLE_LOCATION, values, LOCATION_ID + "=" + cm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertArea(AreaModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(AREA_ID, cm.getId());
            values.put(AREA_NAME, cm.getName());
            values.put(AREA_MANAGER_ID, cm.getAreaManagerId());
            values.put(REGION_ID, cm.getRegionId());
            values.put(IsActive, cm.isActive());

            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.insert(TABLE_AREA, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateArea(AreaModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(AREA_ID, cm.getId());
            values.put(AREA_NAME, cm.getName());
            values.put(AREA_MANAGER_ID, cm.getAreaManagerId());
            values.put(REGION_ID, cm.getRegionId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.update(TABLE_AREA, values, AREA_ID + "=" + cm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertRegion(RegionModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(REGION_ID, cm.getId());
            values.put(REGION_NAME, cm.getName());
            values.put(REGION_MANAGER_ID, cm.getRegionManagerId());
            values.put(IsActive, cm.isActive());

            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.insert(TABLE_REGION, null, values);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateRegion(RegionModel cm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(REGION_ID, cm.getId());
            values.put(REGION_NAME, cm.getName());
            values.put(REGION_MANAGER_ID, cm.getRegionManagerId());
            values.put(IsActive, cm.isActive());
            values.put(MODIFIED_ON, cm.getModifiedOn());

            long i = DB.update(TABLE_REGION, values, REGION_ID + "=" + cm.getId(), null);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertAcademicSession(AcademicSessionModel asm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(AcademicSession.SESSION_ID, asm.getSession_id());
            values.put(AcademicSession.SESSION, asm.getSession());

            return DB.insert(AcademicSession.ACADEMIC_SESSION_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateAcademicSession(AcademicSessionModel asm) {
        SQLiteDatabase DB = getDB();
        try {
            ContentValues values = new ContentValues();
            values.put(AcademicSession.SESSION_ID, asm.getSession_id());
            values.put(AcademicSession.SESSION, asm.getSession());

            return DB.update(AcademicSession.ACADEMIC_SESSION_TABLE, values, AcademicSession.SESSION_ID + "=" + asm.getSession_id(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void dropTable(String tableName) {
        SQLiteDatabase DB = getDB();
        String query = "Drop table if exists " + tableName;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCalendar() {
        SQLiteDatabase DB = getDB();
        String query = "delete from " + TABLE_CALENDAR;
        try {
            DB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableCreationQuery) {
        try {
            SQLiteDatabase DB = getDB();
            DB.execSQL(tableCreationQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStudentCount(int schoolClass_id) {

        String query = "select count(s.id) as student_count from student s\n" +
                "where s.schoolclass_id=" + schoolClass_id + " and s.is_active=1\n" +
                "and s.is_withdrawl=0";

        SQLiteDatabase db = getDB();
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("student_count"));
        }
        return count;
    }

    public ArrayList<ClassSectionModel> getClassSectionForGraduation(int school_id) {
        Cursor cursor = null;
        SQLiteDatabase db = getDB();
        ArrayList<ClassSectionModel> list = new ArrayList<>();

        String query = "select sc.*,c.name as class_name,cs.name as section_name from school_class sc\n" +
                "                inner join class c on c.id = sc.class_id\n" +
                "                inner join section cs on cs.id = sc.section_id\n" +
                "                where school_id = @schoolID and is_active = 1 \n" +
                "                and c.rank = (select max(c.rank) from school_class sc inner join class c on sc.class_id = c.id where sc.school_id = @schoolID AND sc.is_active = 1)";

        query = query.replace("@schoolID", String.valueOf(school_id));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    ClassSectionModel csm = new ClassSectionModel();
                    csm.setClass_section_name(cursor.getString(cursor.getColumnIndex("class_name")) + "-" +
                            cursor.getString(cursor.getColumnIndex("section_name")));
                    csm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    csm.setClassId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_CLASSID)));
                    csm.setSectionId(cursor.getInt(cursor.getColumnIndex(SCHOOL_CLASS_SECTIONID)));
                    list.add(csm);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public CalendarsModel getTotalDaysAndOffDaysFromSession(int school_id, int academic_session_id) {
        CalendarsModel calendarsModel = null;
        String query = "select 1 + JulianDay(strftime('%Y-%m-%d',trim(s.end_date,'AM/PM'))) - JulianDay(strftime('%Y-%m-%d',trim(s.start_date,'AM/PM'))) as Total_days,\n" +
                "sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days \n" +
                "from calendar c INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id\n" +
                "where s.id = " + school_id + " and s.academic_session_id =" + academic_session_id + " and holiday_type_id in (2,3,4)\n" +
                "and c.isActive = 1\n" +
                "group by s.start_date, s.end_date";

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                calendarsModel = new CalendarsModel();
                calendarsModel.setTotalDays(cursor.getInt(cursor.getColumnIndex("Total_days")));
                calendarsModel.setTotalOffDays(cursor.getInt(cursor.getColumnIndex("off_days")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendarsModel;
    }


    public CalendarsModel getTotalDaysAndOffDaysFromThisMonth(int school_id) {
        CalendarsModel calendarsModel = null;
        String query = "select JulianDay(strftime('%Y-%m-%d',date('now','start of month'))) - JulianDay(strftime('%Y-%m-%d',date('now','start of month','+1 month'))) as Total_days,\n" +
                "sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days \n" +
                "from calendar c INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id\n" +
                "where s.id = @SchoolId and c.activity_start_date >= datetime('now','start of month') \n" +
                "and c.activity_end_date <= datetime('now','start of month','+1 month') and holiday_type_id in (2,3,4)\n" +
                "and cal.isActive = 1\n" +
                "group by s.start_date, s.end_date";

        query = query.replace("@SchoolId", school_id + "");

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                calendarsModel = new CalendarsModel();
                calendarsModel.setTotalDays(cursor.getInt(cursor.getColumnIndex("Total_days")));
                calendarsModel.setTotalOffDays(cursor.getInt(cursor.getColumnIndex("off_days")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendarsModel;
    }


    public double getAbsentPercentageForThisMonth(int school_id) {
        String query = "SELECT AVG(1.00*daysAbsent/classStrength)*100 AS absentPercent FROM (\n" +
                "SELECT COUNT(*) AS daysAbsent, (SELECT COUNT(*) FROM Student WHERE schoolclass_id = sc.id) AS classStrength, a.for_date, sc.id\n" +
                "FROM Student s INNER JOIN School_Class sc ON sc.id = s.schoolclass_id\n" +
                "INNER JOIN Student_Attendance sa ON sa.student_id = s.id\n" +
                "INNER JOIN Attendance a ON a.id = sa.attendance_id AND a.school_class_id = sc.id\n" +
                "WHERE COALESCE(NULLIF(sa.isabsent,''), 'P') <> 'P' AND sc.school_id = @SchoolId\n" +
                "AND a.for_date >= datetime('now','start of month')  AND a.for_date <= datetime('now','start of month','+1 month')\n" +
                "GROUP BY a.for_date, sc.id) AS absentCount";

        query = query.replace("@SchoolId", school_id + "");
        double absentPercentage = 0;

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                absentPercentage = cursor.getInt(cursor.getColumnIndex("absentPercent"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return absentPercentage;
        }
        return absentPercentage;
    }


    public double getAbsentPercentageForThisSession(int school_id, String start_date, String end_date) {
        String query = "SELECT AVG(1.00*daysAbsent/classStrength)*100 AS absentPercent FROM (\n" +
                "SELECT COUNT(*) AS daysAbsent, (SELECT COUNT(*) FROM Student WHERE schoolclass_id = sc.id) AS classStrength, a.for_date, sc.id\n" +
                "FROM Student s INNER JOIN School_Class sc ON sc.id = s.schoolclass_id\n" +
                "INNER JOIN Student_Attendance sa ON sa.student_id = s.id\n" +
                "INNER JOIN Attendance a ON a.id = sa.attendance_id AND a.school_class_id = sc.id\n" +
                "WHERE COALESCE(NULLIF(sa.isabsent,''), 'P') <> 'P' AND sc.school_id = @SchoolId\n" +
                "AND a.for_date >= date(trim('@StartDate','AM/PM'))  AND a.for_date <= date(trim('@EndDate','AM/PM'))\n" +
                "GROUP BY a.for_date, sc.id) AS absentCount";

        query = query.replace("@SchoolId", school_id + "");
        query = query.replace("@StartDate", start_date);
        query = query.replace("@EndDate", end_date);

        double absentPercentage = 0;

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                absentPercentage = cursor.getInt(cursor.getColumnIndex("absentPercent"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return absentPercentage;
        }
        return absentPercentage;
    }

    public CalendarsModel getLast30WorkingDays(int school_id) {
        CalendarsModel calendarsModel = null;
        String query = "select JulianDay(strftime('%Y-%m-%d',date('now','start of month'))) - JulianDay(strftime('%Y-%m-%d',date('now','start of month','+1 month'))) as Total_days," +
                "                    sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days " +
                "                   from calendar c INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id" +
                "                  where s.id = @SchoolId and c.activity_start_date >= datetime('now','-30 days') and c.activity_end_date <= datetime('now') and holiday_type_id in (2,3,4)\n" +
                "and c.isActive = 1\n" +
                "     group by s.start_date, s.end_date";

        query = query.replace("@SchoolId", school_id + "");

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                calendarsModel = new CalendarsModel();
                calendarsModel.setTotalDays(cursor.getInt(cursor.getColumnIndex("Total_days")));
                calendarsModel.setTotalOffDays(cursor.getInt(cursor.getColumnIndex("off_days")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (calendarsModel == null) {
            calendarsModel = new CalendarsModel();
            calendarsModel.setTotalDays(30);
            calendarsModel.setTotalOffDays(0);
        }
        return calendarsModel;
    }

    public CalendarsModel getTeacherLast30WorkingDays(int school_id) {
        CalendarsModel calendarsModel = null;
        String query = "select JulianDay(strftime('%Y-%m-%d',date('now','start of month'))) - JulianDay(strftime('%Y-%m-%d',date('now','start of month','+1 month'))) as Total_days," +
                " sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days " +
                " from calendar c INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id" +
                " where s.id = @SchoolId and c.activity_start_date >= datetime('now','-30 days') and c.activity_end_date <= datetime('now') and holiday_type_id in (3,4)\n" +
                " and c.isActive = 1\n" +
                " group by s.start_date, s.end_date";

        query = query.replace("@SchoolId", school_id + "");

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                calendarsModel = new CalendarsModel();
                calendarsModel.setTotalDays(cursor.getInt(cursor.getColumnIndex("Total_days")));
                calendarsModel.setTotalOffDays(cursor.getInt(cursor.getColumnIndex("off_days")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendarsModel;
    }


    public CalendarsModel getThisMonthWorkingDays(int school_id) {
        CalendarsModel calendarsModel = null;
        String query = "select -JulianDay(strftime('%Y-%m-%d',date('now','start of month'))) + JulianDay(strftime('%Y-%m-%d',date('now','start of month','+1 month'))) as Total_days,\n" +
                "sum(1+JulianDay(c.activity_end_date) - JulianDay(c.activity_start_date)) as off_days \n" +
                "from calendar c \n" +
                "INNER JOIN school s ON c.school_id = s.id and c.academic_session_id = s.academic_session_id\n" +
                "where s.id = @SchoolId and c.activity_start_date >= datetime('now','start of month') \n" +
                "and c.activity_end_date <= datetime('now','start of month','+1 month')\n" +
                "and holiday_type_id in (2,3,4) and c.isActive = 1\n" +
                "group by s.start_date, s.end_date";

        query = query.replace("@SchoolId", school_id + "");

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                calendarsModel = new CalendarsModel();
                calendarsModel.setTotalDays(cursor.getInt(cursor.getColumnIndex("Total_days")));
                calendarsModel.setTotalOffDays(cursor.getInt(cursor.getColumnIndex("off_days")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendarsModel;
    }


    public int getAttendanceForThisMonth(String schoolId) {
        String query = "select count(*) as attendance_count,sa.* from attendance a\n" +
                "inner join school_class sc on sc.id = a.school_class_id\n" +
                "inner join school s on s.id = sc.school_id\n" +
                "inner join student_attendance sa on sa.attendance_id = a.id\n" +
                "where s.id =@SchoolId and sc.is_active=1 and sa.isabsent = 1\n" +
                "and a.for_date >= date('now','start of month') and a.for_date <= date('now','start of month','+1 month','-1 day')";

        query = query.replace("@SchoolId", schoolId);

        int count = 0;
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("attendance_count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public void deletePromotion(int promotionId) {
        SQLiteDatabase db = getDB();

        String query1 = "delete from " + TABLE_PROMOTION + " where " + KEY_ID + "=" + promotionId;
        String query2 = "delete from " + TABLE_PROMOTION_STUDENT + " where " + PROMOTION_ID + "=" + promotionId;

        try {
            db.execSQL(query1);
            db.execSQL(query2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteWithdrawals(int withdrawalId) {
        SQLiteDatabase db = getDB();

        String query1 = "delete from " + TABLE_WITHDRAWAL + " where " + KEY_ID + "=" + withdrawalId;

        try {
            db.execSQL(query1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEnrollments(int enrollmentId) {
        SQLiteDatabase db = getDB();

        String query1 = "delete from " + TABLE_ENROLLMENT + " where " + KEY_ID + "=" + enrollmentId;
        String query2 = "delete from " + TABLE_ENROLLMENT_IMAGE + " where " + ENROLLMENT_ID + "=" + enrollmentId;

        try {
            db.execSQL(query1);
            db.execSQL(query2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getAttendanceForToday(String schoolId) {

        String query = "select count(*) as attendance_count,sa.* from attendance a\n" +
                "inner join school_class sc on sc.id = a.school_class_id\n" +
                "inner join school s on s.id = sc.school_id\n" +
                "inner join student_attendance sa on sa.attendance_id = a.id\n" +
                "where s.id =@SchoolId and sc.is_active=1 and sa.isabsent = 1\n" +
                "and a.for_date = date('now')";

        query = query.replace("@SchoolId", schoolId);

        int count = 0;
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("attendance_count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ClassSectionModel getClassSectionBySchoolIdAndSchoolClassId(int SchoolID, int schoolClassID) {
        ClassSectionModel sm = null;
        Cursor cursor = null;
        try {

            String selectQuery = "select sc.id as schoolclass_id,c.id as class_id,c.name as class_name," +
                    " s.id as section_id,s.name as section_name from school_class sc inner join class c on sc.class_id = c.id" +
                    " inner join section s on sc.section_id = s.id where sc.school_id=" + SchoolID + " and sc.is_active=1 and sc.id=" + schoolClassID + "";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    sm = new ClassSectionModel();
                    sm.setSchoolClassId(cursor.getInt(cursor.getColumnIndex("schoolclass_id")));
                    sm.setClassId(cursor.getInt(cursor.getColumnIndex("class_id")));
                    sm.setSectionId(cursor.getInt(cursor.getColumnIndex("section_id")));
                    sm.setClass_section_name(cursor.getString(cursor.getColumnIndex("class_name")) + " " + cursor.getString(cursor.getColumnIndex("section_name")));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return sm;
    }

    public List<StudentAutoCompleteModel> getAllStudentsForAutocompleteList(int schoolId) {
        List<StudentAutoCompleteModel> stdMList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1' order by s." + STUDENT_GR_NO + " ASC";


            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int schoolClassId = cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID));
                    ClassSectionModel csm = getClassSectionBySchoolIdAndSchoolClassId(schoolId, schoolClassId);

                    StudentAutoCompleteModel stm = new StudentAutoCompleteModel();
                    stm.setStudent_name(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    stm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    stm.setClass_section(csm.getClass_section_name());
                    stdMList.add(stm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return stdMList;
    }

    public List<StudentAutoCompleteModel> getAllStudentsForAutocompleteList(int schoolId, int is_active, String admission_type) {
        List<StudentAutoCompleteModel> stdMList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId;

            if (is_active == 2) {
                if (admission_type.equalsIgnoreCase(AppConstants.withdrawn)) {
                    selectQuery += " AND s." + STUDENT_IS_WITHDRAWN + " = 1 AND " + STUDENT_WITHDRAWN_REASON_ID + " != 14";
                    selectQuery += " AND s." + STUDENT_IS_ACTIVE + " = '0'";
                } else if (admission_type.equals(AppConstants.graduated)) {
                    selectQuery += " AND s." + STUDENT_IS_WITHDRAWN + " = 1 AND withdrawal_reason_id = 14";
                    selectQuery += " AND s." + STUDENT_IS_ACTIVE + " = '0'";
                }
            } else
                selectQuery += " AND s." + STUDENT_IS_ACTIVE + " = '" + is_active + "'";

            selectQuery += " order by s." + STUDENT_GR_NO + " ASC";


            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int schoolClassId = cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID));
                    ClassSectionModel csm = getClassSectionBySchoolIdAndSchoolClassId(schoolId, schoolClassId);

                    StudentAutoCompleteModel stm = new StudentAutoCompleteModel();
                    stm.setStudent_name(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    stm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                    stm.setClass_section(csm.getClass_section_name());
                    stdMList.add(stm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return stdMList;
    }

    public int getAcademicSessionId(int schoolId) {
        int academicSessionId = 0;

        Cursor cursor = null;
        String query = "select " + FeesCollection.ACADEMIC_SESSION_ID + " from school where id = @SchoolId";
        SQLiteDatabase db = getDB();
        query = query.replace("@SchoolId", schoolId + "");

        try {

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                academicSessionId = cursor.getInt(cursor.getColumnIndex(FeesCollection.ACADEMIC_SESSION_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return academicSessionId;

    }

    public String getAcademicSession(int academicSessionId) {
        String academicSession = "";
        Cursor cursor = null;
        String query = "select session from AcademicSession where session_id = @AcademicSessionId";
        SQLiteDatabase db = getDB();
        query = query.replace("@AcademicSessionId", academicSessionId + "");

        try {

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                academicSession = cursor.getString(cursor.getColumnIndex("session"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return academicSession;

    }

    public boolean checkSchoolsFinanceModule(int schoolid) {
        boolean openFinance = false;
        Cursor cursor = null;
        try {
            String selectQuery = "select * from school where id = " + schoolid + " and (',' || allowedModule_app  || ',') LIKE '%,2,%'";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                openFinance = true;
            } else {
                openFinance = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return openFinance;
    }

    public String getHighestGrNO(String schoolid) {
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        String highestGrNo = "";
        String selectQuery = "SELECT cast(student_gr_no as int) as new_gr FROM student s \n" +
                " INNER JOIN school_class sc ON sc.id = s.schoolclass_id\n" +
                " WHERE sc.school_id = @SchoolID\n" +
                " UNION ALL SELECT gr_no as gr FROM enrollment e INNER JOIN school_class sc ON sc.id = e.class_section_id WHERE sc.school_id = @SchoolID order by gr desc LIMIT 1";
        selectQuery = selectQuery.replaceAll("@SchoolID", schoolid);

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                highestGrNo = cursor.getString(cursor.getColumnIndex("new_gr"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return highestGrNo.equals("") ? highestGrNo : String.valueOf(Integer.parseInt(highestGrNo) + 1);
    }

    public int getMonthsBetweenCurrentDateAndFeesTransStartDate(int schoolid, int academic_session_id) {
        int months = 0;
        SQLiteDatabase DB = getDB();
        Cursor cursor = null;
//        String selecetQuery = "SELECT round((julianday(Date('now')) - julianday(replace(replace(start_date,'AM',''),'PM','')))/30) as MonthsDiff\n" +
//                "FROM school where school.id = @SchoolID";

        String selectQuery = "SELECT ifnull(round((julianday(Date('now')) - julianday(min(replace(replace(upper(fh.created_on),'AM',''),'PM','')) ,'start of month'))/30),0) as MonthsDiff FROM FeesHeader fh\n" +
                "WHERE fh.schoolclass_id IN (SELECT id FROM school_class WHERE school_id = @SchoolID) AND fh.academic_session_id = @ACSID\n" +
                "AND ifnull(fh.created_on,'') != ''";

        selectQuery = selectQuery.replace("@SchoolID", schoolid + "");
        selectQuery = selectQuery.replace("@ACSID", academic_session_id + "");
        try {
            cursor = DB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                months = cursor.getInt(cursor.getColumnIndex("MonthsDiff"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return months;
    }

    public int getStudentCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as studentCount FROM student WHERE schoolclass_id IN(SELECT id from school_class where school_id IN(@schoolid))";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        query = query.replace("@schoolid", String.valueOf(schoolId));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("studentCount"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public int getFeesDetailCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as feesDetailCount FROM FeesDetail fd" +
                " INNER JOIN FeesHeader fh ON fh.id = fd.feesHeader_id" +
                " WHERE (fh.sys_id NOTNULL AND fh.sys_id != '' AND fh.sys_id != 0) AND fh.schoolclass_id IN(SELECT id from school_class where school_id IN(" + schoolId + "))";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("feesDetailCount"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public int getSchoolClassCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as schoolClass_count FROM school_class  WHERE school_id IN(@schoolid)";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        query = query.replace("@schoolid", String.valueOf(schoolId));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("schoolClass_count"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public void deleteStudentRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id,student_gr_no FROM student WHERE schoolclass_id IN(SELECT id from school_class where school_id IN(" + schoolId + "))";
        Cursor cursor = null;
        String grNo = "";
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    grNo += cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)) + ",";

                    db.delete(TABLE_STUDENT, KEY_ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "Student Data Flushed Successfully. School ID:" + schoolId);
            AppModel.getInstance().appendErrorLog(context, "Student records that are deleted (" + grNo + ") for School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing Student data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }

    }

    public void deleteSchoolClassRecords(int schoolId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "SELECT id FROM school_class  WHERE school_id IN(" + schoolId + ")";
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    db.delete(TABLE_SCHOOL_CLASS, KEY_ID + " = " + id, null);
                } while (cursor.moveToNext());
            }
            AppModel.getInstance().appendErrorLog(context, "School Class Data Flushed Successfully. School ID:" + schoolId);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in flushing School Class data !\nError: " + e.getMessage() +
                    " School ID:" + schoolId);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }

    }

    public ArrayList<StudentModel> getStudentUsing(int schoolId, String grNo, String stdId, boolean isStudentActive) {
        Cursor cursor = null;
        ArrayList<StudentModel> smList = new ArrayList<>();
        try {
            String selectQuery = "SELECT s.*, sc." + SCHOOL_CLASS_CLASSID + ", c." + CLASS_NAME + " class_name, cs." + SECTION_NAME + " section_name "
                    + " FROM " + TABLE_STUDENT + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " INNER JOIN " + TABLE_CLASS + " c ON sc." + SCHOOL_CLASS_CLASSID + " = c." + KEY_ID
                    + " INNER JOIN " + TABLE_SECTION + " cs ON sc." + SCHOOL_CLASS_SECTIONID + " = cs." + KEY_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " = " + schoolId
                    + " AND s." + KEY_ID + " = " + stdId;

            if (grNo != null && grNo.length() > 0) {
                selectQuery += " AND s." + STUDENT_GR_NO + "= " + grNo;
            }

            if (isStudentActive) {
                selectQuery += " AND s." + STUDENT_IS_ACTIVE + " = 1"
                        + " AND s." + STUDENT_IS_WITHDRAWN + " = 0";
            } else {
                selectQuery += " AND s." + STUDENT_IS_ACTIVE + " = 0"
                        + " AND s." + STUDENT_IS_WITHDRAWN + " = 1";
            }

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                StudentModel sm = new StudentModel();
                sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                sm.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                sm.setGender(cursor.getString(cursor.getColumnIndex(STUDENT_GENDER)));
                sm.setGrNo(cursor.getString(cursor.getColumnIndex(STUDENT_GR_NO)));
                sm.setEnrollmentDate(cursor.getString(cursor.getColumnIndex(STUDENT_ENROLLMENT_DATE)));
                sm.setDob(cursor.getString(cursor.getColumnIndex(STUDENT_DOB)));
                sm.setFormB(cursor.getString(cursor.getColumnIndex(STUDENT_FORM_B)));
                sm.setFathersName(cursor.getString(cursor.getColumnIndex(STUDENT_FATHERS_NAME)));
                sm.setFatherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_OCCUPATION)));
                sm.setFatherNic(cursor.getString(cursor.getColumnIndex(STUDENT_FATHER_NIC)));
                sm.setMotherName(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NAME)));
                sm.setMotherOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_OCCUPATION)));
                sm.setMotherNic(cursor.getString(cursor.getColumnIndex(STUDENT_MOTHERS_NIC)));
                sm.setGuardianName(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NAME)));
                sm.setGuardianOccupation(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_OCCUPATION)));
                sm.setGuardianNic(cursor.getString(cursor.getColumnIndex(STUDENT_GUARDIANS_NIC)));
                sm.setPreviousSchoolName(cursor.getString(cursor.getColumnIndex(STUDENT_PREVIOUS_SCHOOL_NAME)));
                sm.setPreviousSchoolClass(cursor.getString(cursor.getColumnIndex(STUDENT_CLASS_PREVIOUS_SCHOOL)));
                sm.setAddress1(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS1)));
                sm.setAddress2(cursor.getString(cursor.getColumnIndex(STUDENT_ADDRESS2)));
                sm.setContactNumbers(cursor.getString(cursor.getColumnIndex(STUDENT_CONTACT_NUMBERS)));
                sm.setCurrentSession(cursor.getInt(cursor.getColumnIndex(STUDENT_CURRENT_SESSION)));
                sm.setCurrentClass(cursor.getString(cursor.getColumnIndex("class_name")));
                sm.setCurrentSection(cursor.getString(cursor.getColumnIndex("section_name")));
                sm.setModifiedBy(cursor.getInt(cursor.getColumnIndex(STUDENT_MODIFIED_BY)));
                sm.setModifiedOn(cursor.getString(cursor.getColumnIndex(STUDENT_MODIFIED_ON)));
                sm.setUploadedOn(cursor.getString(cursor.getColumnIndex(STUDENT_UPLOADED_ON)));
                sm.setSchoolClassId((cursor.getInt(cursor.getColumnIndex(STUDENT_SCHOOL_CLASS_ID))));
                sm.setWithdrawnOn(cursor.getString(cursor.getColumnIndex(STUDENT_WITHDRAWN_ON)));
                sm.setWithdrawalReasonId(cursor.getInt(cursor.getColumnIndex(STUDENT_WITHDRAWN_REASON_ID)));
                sm.setScholarshipCategoryId((cursor.getInt(cursor.getColumnIndex(STUDENT_ScholarshipCategory_ID))));
                sm.setActualFees((cursor.getDouble(cursor.getColumnIndex(STUDENT_Actual_Fees))));
                boolean isWithdraw = cursor.getString(cursor.getColumnIndex(STUDENT_IS_WITHDRAWN)).equals("1");
                sm.setPictureName(cursor.getString(cursor.getColumnIndex(STUDENT_PICTURE_NAME)));
                sm.setWithdrawal(isWithdraw);
                boolean isActive = cursor.getString(cursor.getColumnIndex(STUDENT_IS_ACTIVE)).equals("1");
                sm.setActive(isActive);
                smList.add(sm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public boolean isPrimarySchoolHasSecondaryClasses(int schoolId) {
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        boolean isHasSecondary = false;
        String selectQuery = "SELECT count(sc.id) classCount FROM school_class sc\n" +
                "INNER JOIN class c on c.id=sc.class_id\n" +
                "WHERE sc.school_id = @SchoolId and c.rank >= (Select c.rank from class where id=8)";
        selectQuery = selectQuery.replace("@SchoolId", String.valueOf(schoolId));

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                isHasSecondary = cursor.getInt(cursor.getColumnIndex("classCount")) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return isHasSecondary;
    }


    public int getDatesofHolidaysInLast30days(String schoolId) {
        String query = "select cal.* from calendar cal inner join school s on s.id = cal.school_id and cal.academic_session_id = s.academic_session_id\n" +
                "where cal.school_id = 232 and holiday_type_id in (4,3,2) and cal.isActive = 1 and strftime('%Y-%m-%d',cal.activity_start_date) >= strftime('%Y-%m-%d',date('now', 'localtime','-1 month'))\n" +
                "and strftime('%Y-%m-%d',cal.activity_end_date) <= strftime('%Y-%m-%d',date('now'))";

        query = query.replace("@SchoolId", schoolId);

        SQLiteDatabase db = getDB();
        List<CalendarsModel> calendarsModels = new ArrayList<>();

        try {

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    String startDate = cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date));
                    String endDate = cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date));
                    if (!startDate.equals(endDate)) {
                        List<String> dateList = AppModel.getInstance().getDatesBetweenTwoDates(startDate, endDate, "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'00:00:00");
                        for (String start : dateList) {
                            CalendarsModel cm = new CalendarsModel();
                            cm.setActivity_Start_Date(start);
                            cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                            cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                            cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                            cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                            cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                            cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                            cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));
                            calendarsModels.add(cm);
                        }

                    } else {
                        CalendarsModel cm = new CalendarsModel();
                        cm.setActivity_Start_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Start_Date)));
                        cm.setActivity_End_Date(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_End_Date)));
                        cm.setC_Activity_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Activity_Id)));
                        cm.setC_Activity_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Activity_Name)));
                        cm.setC_Holiday_Type_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Holiday_Type_Id)));
                        cm.setC_Holiday_Type_Name(cursor.getString(cursor.getColumnIndex(CALENDAR_Holiday_Type_Name)));
                        cm.setCalendar_id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Id)));
                        cm.setSession_Id(cursor.getInt(cursor.getColumnIndex(CALENDAR_Academic_Session_Id)));
                        calendarsModels.add(cm);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 30 - calendarsModels.size();
    }

    public String getAllhighestDuesOfStudent(String schoolID, String studentID) {
        String Amount = "";
        HighestDuesStudentsModel sbfm;
        Cursor cursor = null;
        String sqlquerry;
//        if (schoolID > 0) {

        sqlquerry = "select sch.school_name as School,s.student_name as Name,student_gr_no as GRNo,c.name as Class,sec.name as Section,c.rank as class_rank," +
                " SUM(CASE WHEN fh.Category_Id = 3 THEN fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 1 THEN 1 * fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 2 THEN -1 * fd.fee_amount" +
                " ELSE CASE WHEN fh.Category_Id = 4 THEN -1 * fd.fee_amount" +
                " END END END END) as Amount" +
                " FROM FeesHeader fh " +
                " inner join FeesDetail fd ON fh.id = fd.feesHeader_id" +
                " inner join school_class sc on sc.id = fh.schoolclass_id  " +
                " inner join student s on s.id = fh.student_id " +
                " inner join class c on c.id = sc.class_id " +
                " inner join section sec on sec.id = sc.section_id " +
                " inner join school sch on sch.id = sc.school_id " +
                " and sch.id IN(@SchoolID)" +
                " and s.id IN(@studentID)" +
                " group by s.id" +
                " order by amount desc";

        sqlquerry = sqlquerry.replace("@SchoolID", String.valueOf(schoolID));
        sqlquerry = sqlquerry.replace("@studentID", String.valueOf(studentID));


        try {
            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(sqlquerry, null);

            if (cursor.moveToFirst()) {
                do {
                    Amount = cursor.getString(cursor.getColumnIndex("Amount"));
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
        return Amount;
    }

    public ArrayList<SchoolModel> getAllUserSchoolsForExpense() {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT s.* FROM " + TABLE_SCHOOL + " s "
                    + "INNER JOIN " + TABLE_USER_SCHOOL + " us ON us."
                    + SCHOOL_ID + " = s." + KEY_ID +
                    " where (',' || s." + SCHOOL_AllowedModule_App + "|| ',') LIKE '%,13,%'" +
                    " group by s.id";
//                    " where s.start_date <= datetime('now') and s.end_date >= datetime('now');";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    SchoolModel sm = new SchoolModel();
                    sm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    sm.setName(cursor.getString(cursor.getColumnIndex(SCHOOL_NAME)));
                    sm.setRegion(cursor.getString(cursor.getColumnIndex(SCHOOL_REGION)));
                    sm.setDistrict(cursor.getString(cursor.getColumnIndex(SCHOOL_DISTRICT)));
                    sm.setArea(cursor.getString(cursor.getColumnIndex(SCHOOL_AREA)));
                    sm.setEMIS(cursor.getString(cursor.getColumnIndex(SCHOOL_EMIS)));
                    sm.setStart_date(cursor.getString(cursor.getColumnIndex(SCHOOL_START_DATE)));
                    sm.setEnd_date(cursor.getString(cursor.getColumnIndex(SCHOOL_END_DATE)));
                    sm.setAcademic_Session_Id(cursor.getInt(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION_ID)));
                    sm.setAcademic_session(cursor.getString(cursor.getColumnIndex(SCHOOL_ACADEMIC_SESSION)));
                    sm.setAllowedModule_App(cursor.getString(cursor.getColumnIndex(SCHOOL_AllowedModule_App)));

                    smList.add(sm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smList;
    }

    public int getSubHeadLimitsMonthlyCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as subheadlimitsmonthly_count FROM SubHeadLimitsMonthly  WHERE school_id IN(@schoolid)";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        query = query.replace("@schoolid", String.valueOf(schoolId));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("subheadlimitsmonthly_count"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public int getSchoolPettyCashMonthlyLimitsCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as schoolpettycashmonthlylimits_count FROM SchoolPettyCashMonthlyLimits  WHERE school_id IN(@schoolid)";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        query = query.replace("@schoolid", String.valueOf(schoolId));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("schoolpettycashmonthlylimits_count"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public int getTransactionsCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as transactions_count FROM Transactions  WHERE school_id IN(@schoolid)";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        query = query.replace("@schoolid", String.valueOf(schoolId));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("transactions_count"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public int getAmountClosingCountForChecksum(int schoolId) {
        String query = "SELECT count(*) as amountclosing_count FROM AmountClosing  WHERE school_id IN(@schoolid)";
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        query = query.replace("@schoolid", String.valueOf(schoolId));
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
                return cursor.getInt(cursor.getColumnIndex("amountclosing_count"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public int getClassSectionCountBySchoolId(String SchoolID) {
        Cursor cursor = null;
        int count = 0;
        try {

            String selectQuery = "select count(sc.id) as scCount from school_class sc \n" +
                    "inner join class c on sc.class_id = c.id \n" +
                    "inner join section s on sc.section_id = s.id \n" +
                    "where sc.school_id=" + SchoolID + " and sc.is_active=1 order by c.rank asc";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("scCount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return count;
    }

    public boolean checkmetadataRecordDownlodedSuccessfully() {
        Cursor cursor = null;
        int count = 0;
        try {
            List<SchoolModel> schoolModelList = getAllUserSchools();
            for (SchoolModel model : schoolModelList) {

                String selectQuery = "select count(sc.id) as scCount from school_class sc \n" +
                        "inner join class c on sc.class_id = c.id \n" +
                        "inner join section s on sc.section_id = s.id \n" +
                        "where sc.school_id=" + model.getId() + " and sc.is_active=1 order by c.rank asc";

                SQLiteDatabase db = getDB();
                cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        count = cursor.getInt(cursor.getColumnIndex("scCount"));
                        if (count == 0)
                            return false;

                    } while (cursor.moveToNext());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return true;
    }

    public int deleteRecordsFromTable(String tableName, String whereClause, String[] whereArgs) {
        /* ex: delete(DATABASE_TABLE, KEY_NAME + "=" + name, null) OR
         delete(DATABASE_TABLE, KEY_NAME + "=?", new String[]{name}) */

        int i = 0;
        SQLiteDatabase DB = getDB();
        try {
            i = DB.delete(tableName, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    private int getAllCampus() {
        Cursor cursor = null;
        int count = 0;
        try {
            String selectQuery = "select count(*) as cCount from campus";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex("cCount"));
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }
        return count;
    }

    private int getAllLocation() {
        Cursor cursor = null;
        int count = 0;
        try {
            String selectQuery = "select count(*) as cCount from location";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex("cCount"));
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }
        return count;
    }

    private int getAllArea() {
        Cursor cursor = null;
        int count = 0;
        try {
            String selectQuery = "select count(*) as cCount from area";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex("cCount"));
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }
        return count;
    }

    private int getAllRegion() {
        Cursor cursor = null;
        int count = 0;
        try {
            String selectQuery = "select count(*) as cCount from region";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex("cCount"));
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }
        return count;
    }

    public void insertOrUpdateCampusBulk(ArrayList<CampusModel> campusModels) {
        SQLiteDatabase DB = getDB();

        if (campusModels != null && campusModels.size() > 0) {

            try {
                DB.beginTransaction();

                AppModel.getInstance().appendErrorLog(context, "Campus count:" + campusModels.size());

                AppModel.getInstance().appendLog(context, "Campus count:" + campusModels.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(campusModels.size(),
                        "Campus", AppConstants.SCHOOL_MODULE, "Downloading", 0);


                int downloadedCount = 0;

                for (CampusModel scm : campusModels) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                TABLE_CAMPUS,
                                CAMPUS_ID,
                                scm.getId())) {

                            try {
                                ContentValues values = new ContentValues();
                                values.put(CAMPUS_ID, scm.getId());
                                values.put(CAMPUS_NAME, scm.getName());
                                values.put(LOCATION_ID, scm.getLocationId());
                                values.put(IsActive, scm.isActive());

                                values.put(MODIFIED_ON, scm.getModifiedOn());

                                long i = DB.insert(TABLE_CAMPUS, null, values);
                                if (i > 0) {
                                    downloadedCount++;
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateCampus(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }
                    } catch (Exception e) {
                        DataSync.areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncCampus error:" + e.getMessage());
                    }

                    //Update sync progress
                    DataSync.getInstance(context).syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }

                DB.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
                AppModel.getInstance().appendErrorLog(context, "Exception in SyncCampus error:" + e.getMessage());
            } finally {
                DB.endTransaction();
            }
        } else {
            AppModel.getInstance().appendErrorLog(context, "No Campus record found");
        }

    }

    public int getStudentTableCount() {
        Cursor cursor = null;
        int count = 0;
        try {
            String selectQuery = "select count(*) as cCount from student";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex("cCount"));
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();

        }
        return count;
    }

    public boolean isRegionIsInFlagShipSchools() {
        boolean isPresent = false;
        SQLiteDatabase DB = getDB();
        String query = "SELECT distinct a.area_name,r.region_name, (a.area_name || '  ' || r.region_name) as ra\n" +
                "FROM  school s\n" +
                "left join Campus c on  s.campus_id = c.campus_id\n" +
                "left join Location l  on c.location_id = l.location_id\n" +
                "left join Area a on a.area_id = l.area_id AND a.isActive = 1\n" +
                "left join Region r on r.region_id = a.region_id AND r.isActive = 1\n" +
                "where s.id in (select school_id from user_school) and r.region_id in (1,2,3,4,20)";
        try (Cursor cursor = DB.rawQuery(query, null)) {
            isPresent = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPresent;
    }

    public boolean isRegionIsInFlagShipSchool(int schoolId) {
        boolean isPresent = false;
        SQLiteDatabase DB = getDB();
        String query = "SELECT distinct a.area_name,r.region_name, (a.area_name || '  ' || r.region_name) as ra\n" +
                "FROM  school s\n" +
                "left join Campus c on  s.campus_id = c.campus_id\n" +
                "left join Location l  on c.location_id = l.location_id\n" +
                "left join Area a on a.area_id = l.area_id AND a.isActive = 1\n" +
                "left join Region r on r.region_id = a.region_id AND r.isActive = 1\n" +
                "where s.id = @SchoolId and r.region_id in (1,2,3,4,20)";

        query = query.replace("@SchoolId", "" + schoolId);
        try (Cursor cursor = DB.rawQuery(query, null)) {
            isPresent = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPresent;
    }

    public long isBformNoExist(String value, String schoolId) {
        String query = "Select * from student s inner join school_class sc on sc.id = s.schoolclass_id \n" +
                "where s.form_b = " + value + " AND sc.school_id = " + schoolId;

        SQLiteDatabase db = getDB();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0 && c.moveToFirst()) {
            c.close();
            return 1;
        } else {
            c.close();
            return -1;
        }
    }

    public boolean isNineClass(int schoolId, int classId, int sectionId) {
        boolean isPresent = false;
        SQLiteDatabase DB = getDB();
        String query;
        query = "select  distinct sc.*,c.name as class_name,s.name as section_name\n" +
                "from school_class sc\n" +
                "inner join class c on c.id = sc.class_id\n" +
                "inner join section s on s.id = sc.section_id \n" +
//                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ( 'Class-8', 'Class-9')\n" +
                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ( 'Class-9')\n" +
//                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ('Class-9')\n" +
                "order by class_id";

        query = query.replace("@SchoolID", schoolId + "");
        query = query.replace("@ClassID", classId + "");
        query = query.replace("@SectionID", sectionId + "");
        try (Cursor cursor = DB.rawQuery(query, null)) {
            isPresent = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPresent;
    }

    public boolean isNineOrTenClass(int schoolId, int classId, int sectionId) {
        boolean isPresent = false;
        SQLiteDatabase DB = getDB();
        String query;
        query = "select  distinct sc.*,c.name as class_name,s.name as section_name\n" +
                "from school_class sc\n" +
                "inner join class c on c.id = sc.class_id\n" +
                "inner join section s on s.id = sc.section_id \n" +
//                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ( 'Class-8', 'Class-9')\n" +
                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ( 'Class-9', 'Class-10')\n" +
//                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ('Class-9')\n" +
                "order by class_id";

        query = query.replace("@SchoolID", schoolId + "");
        query = query.replace("@ClassID", classId + "");
        query = query.replace("@SectionID", sectionId + "");
        try (Cursor cursor = DB.rawQuery(query, null)) {
            isPresent = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPresent;
    }

    public boolean isEightClass(int schoolId, int classId, int sectionId) {
        boolean isPresent = false;
        SQLiteDatabase DB = getDB();
        String query;
        query = "select  distinct sc.*,c.name as class_name,s.name as section_name\n" +
                "from school_class sc\n" +
                "inner join class c on c.id = sc.class_id\n" +
                "inner join section s on s.id = sc.section_id \n" +
//                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ( 'Class-8', 'Class-9')\n" +
                "where sc.school_id = @SchoolID and sc.is_active = 1 and sc.class_id = @ClassID and sc.section_id = @SectionID  and c.name in ('Class-8')\n" +
                "order by class_id";

        query = query.replace("@SchoolID", schoolId + "");
        query = query.replace("@ClassID", classId + "");
        query = query.replace("@SectionID", sectionId + "");
        try (Cursor cursor = DB.rawQuery(query, null)) {
            isPresent = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPresent;
    }

    public ArrayList<EnrollmentImageModel> getAllDeathCertImage() {
        ArrayList<EnrollmentImageModel> eimList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM " + TABLE_STUDENT
                    + " WHERE (" + STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON + " IS NULL OR " + STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON + " = '')";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentImageModel eim = new EnrollmentImageModel();
                    eim.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    eim.setEnrollment_id(cursor.getInt(cursor.getColumnIndex(STUDENT_SERVER_ID)));
                    eim.setFilename(cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE)));
                    eim.setUploaded_on(cursor.getString(cursor.getColumnIndex(STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON)));
//                    eim.setReview_status(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_REVIEW_STATUS)));
                    eim.setFiletype(AppConstants.DEATH_CERT_FILE_TYPE);
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

    public ArrayList<EnrollmentImageModel> getAllMedicalCertImage() {
        ArrayList<EnrollmentImageModel> eimList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM " + TABLE_STUDENT
                    + " WHERE (" + STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON + " IS NULL OR " + STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON + " = '')";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentImageModel eim = new EnrollmentImageModel();
                    eim.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    eim.setEnrollment_id(cursor.getInt(cursor.getColumnIndex(STUDENT_SERVER_ID)));
                    eim.setFilename(cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE)));
                    eim.setUploaded_on(cursor.getString(cursor.getColumnIndex(STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON)));
//                    eim.setReview_status(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_REVIEW_STATUS)));
                    eim.setFiletype(AppConstants.MEDICAL_CERT_FILE_TYPE);
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

    public ArrayList<EnrollmentImageModel> getAllBFormImage() {
        ArrayList<EnrollmentImageModel> eimList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM " + TABLE_STUDENT
                    + " WHERE (" + STUDENT_BFORM_IMAGE_UPLOADED_ON + " IS NULL OR " + STUDENT_BFORM_IMAGE_UPLOADED_ON + " = '' ) AND (" + STUDENT_BFORM_IMAGE + " IS NOT NULL AND " + STUDENT_BFORM_IMAGE + " != '' )";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EnrollmentImageModel eim = new EnrollmentImageModel();
                    eim.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    eim.setEnrollment_id(cursor.getInt(cursor.getColumnIndex(STUDENT_SERVER_ID)));
                    eim.setFilename(cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE)));
                    eim.setUploaded_on(cursor.getString(cursor.getColumnIndex(STUDENT_BFORM_IMAGE_UPLOADED_ON)));
//                    eim.setReview_status(cursor.getString(cursor.getColumnIndex(ENROLLMENT_IMAGE_REVIEW_STATUS)));
                    eim.setFiletype(AppConstants.BFORM_FILE_TYPE);
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

    public RegionModel getSelectedSchoolRegion() {
        int schoolId = AppModel.getInstance().getSpinnerSelectedSchool(context);
        RegionModel model = null;
        SQLiteDatabase DB = getDB();
        String query = "SELECT r.* FROM  school s\n" +
                "left join Campus c on  s.campus_id = c.campus_id\n" +
                "left join Location l  on c.location_id = l.location_id\n" +
                "left join Area a on a.area_id = l.area_id AND a.isActive = 1\n" +
                "left join Region r on r.region_id = a.region_id AND r.isActive = 1\n" +
                "where s.id = @SchoolId";

        query = query.replace("@SchoolId", "" + schoolId);
        Cursor cursor = null;
        try {
            cursor = DB.rawQuery(query, null);
            if (cursor.moveToNext()) {
                model = new RegionModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(REGION_ID)));
                model.setName(cursor.getString(cursor.getColumnIndex(REGION_NAME)));
                model.setActive(cursor.getString(cursor.getColumnIndex(IsActive)).equals("1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return model;
    }


    public String getStringValueFromDB(String TABLE_NAME, String columnName, String whereClause) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select " + columnName + " as required_field from " + TABLE_NAME;

        if (!Strings.isEmptyOrWhitespace(whereClause))
            query += " WHERE " + whereClause;

        String requiredField = "";

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                if (!Strings.isEmptyOrWhitespace(cursor.getString(cursor.getColumnIndex("required_field"))))
                    requiredField = cursor.getString(cursor.getColumnIndex("required_field"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requiredField;
    }

    public List<String> getStringListValuesFromDB(String TABLE_NAME, String columnName, String whereClause) {
        List<String> requiredList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select " + columnName + " as required_field from " + TABLE_NAME;

        if (!Strings.isEmptyOrWhitespace(whereClause))
            query += " WHERE " + whereClause;

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    String requiredField = cursor.getString(cursor.getColumnIndex("required_field"));
                    if (requiredField.contains(",")) {
                        List<String> items = Stream.of(requiredField.split(","))
                                .map(String::trim)
                                .collect(Collectors.toList());
                        requiredList.addAll(items);
                    } else {
                        requiredList.add(requiredField);
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        requiredList = requiredList.stream().distinct().collect(Collectors.toList());
        return requiredList;
    }

    public int getIntValueFromDB(String TABLE_NAME, String columnName, String whereClause) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select " + columnName + " as required_field from " + TABLE_NAME;

        if (!Strings.isEmptyOrWhitespace(whereClause))
            query += " WHERE " + whereClause;

        int requiredField = 0;

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex("required_field")) > 0)
                    requiredField = cursor.getInt(cursor.getColumnIndex("required_field"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requiredField;
    }

    public String getStudentReviewStatusCount(String status, int schoolId) {
        int statusCount = 0;
        Cursor cursor = null;
        try {

            String query = "SELECT COUNT(*) AS RECOUNT FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " IN(" + schoolId + ")"
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1' AND s." + STUDENT_PROMOTION_STATUS + " = '" + status + "'";

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                statusCount = cursor.getInt(cursor.getColumnIndex("RECOUNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return String.valueOf(statusCount);
    }

    public boolean checkIfBFormExistsInSchool(String bForm, int schoolId, int id) {
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM " + TABLE_STUDENT
                    + " s INNER JOIN " + TABLE_SCHOOL_CLASS + " sc ON sc." + KEY_ID + " = s." + STUDENT_SCHOOL_CLASS_ID
                    + " WHERE sc." + SCHOOL_CLASS_SCHOOLID + " IN(" + schoolId + ")"
                    + " AND s." + STUDENT_IS_ACTIVE + " = '1' AND s." + STUDENT_FORM_B + " = '" + bForm + "'"
                    + " AND s." + KEY_ID + " != " + id;

            SQLiteDatabase db = getDB();
            cursor = db.rawQuery(query, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

}
