package com.tcf.sma.Models;

import com.tcf.sma.BuildConfig;

/**
 * Created by Zubair Soomro on 12/26/2016.
 */

public class AppConstants {
//    public static final String CONTENT_AUTHORITY = "com.tcf.sma";
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final String CONTENT_PROVIDER_URI = "com.tcf.sma.studentinfoprovider";

    // Content provider authority
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // Account type
    public static final String ACCOUNT_TYPE = "com.example.android.datasync";
    // Account
    public static final String ACCOUNT = "default_account";

    public static final String TCF_SHARED_PREF = "tcf_sharedpref";
    public static final String SHARED_PREF_USER_NAME = "username";
    public static final String SHARED_PREF_PASSWORD = "password";
    public static final String HIDE_FEES_COLLECTION = "hideFeesCollection";
    public static final String Password_Changed = "isPasswordChanged";
    public static final String ROLE_ID = "";
    public static final String HIDE_PROMOTION = "hidePromotion";
    public static final String HIDE_GRADUATION = "hideGraduation";

    public static final String START_KEEPALIVE = "startKeepAlive";
    public static final String imagebaseurlkey = "imagebaseurl";
    public static final String baseurlkey = "baseurl";
    public static final String DaysLeftForLogOut = "daysLeftForLogOut";
    public static final String KEY_CHECKSUM = "check_sum";
    public static final String KEY_CHECKSUM_RAN = "checksumran";
    public static final String KEY_RUN_TIME = "checksum_run_time";
    public static final String LOGIN_TIME = "LOGIN_TIME";
    public static final String KEY_EXPENSE_CHECKSUM = "expense_check_sum";
    public static final String KEY_RUN_TIME_EXPENSE = "expense_checksum_run_time";
    public static final String KEY_CHECKSUM_RAN_EXPENSE = "expensechecksumran";

    //live url as of 05-04-2019
//    public static String BASE_URL = "https://sisappsrv.tcf.org.pk:6443";
//    public static final String BUILD_NO = "";
//    public static final String URL_DOWNLOAD_PROFILE_IMAGES = "http://sms.tcf.org.pk/ePanel/Resources/SIS_APP_IMAGES/";  // used in DownloadStudentPictures method in data sync before

    //multiple device base url UAT
//    public static String BASE_URL = "https://siswebsrv3.tcf.org.pk:3443";
//    public static final String BUILD_NO = ".1";
//    public static final String URL_DOWNLOAD_PROFILE_IMAGES = "https://smsuat.tcf.org.pk:9443/epanel/Resources/SIS_APP_IMAGES/";

    public static final String URL_DOWNLOAD_USER_MANUAL = "https://smsstaging.tcf.org.pk/epanel/Resources/SIS_APP_IMAGES_TEST/";

    //disaster recovery url as of 26-02-2020
//    public static String BASE_URL = "https://sisdr.tcf.org.pk:4443";
//    public static final String BUILD_NO = ".1";
//    public static final String URL_DOWNLOAD_PROFILE_IMAGES = "https://sisdr.tcf.org.pk:4443/ePanel/Resources/SIS_APP_IMAGES_TEST/";

    public static final String URL_CHECKSUM = "api/fees/GetChecksum";
    public static final String URL_LOGIN = "api/register";
    public static final String URL_GET_META_DATA = "api/metadata/get";
    //    public static final String URL_UPLOAD_STUDENTS = "api/studentdata/update";
    public static final String URL_UPLOAD_STUDENTS = "api/studentdata/verifiedupdate";
    public static final String URL_UPLOAD_STUDENT_FOR_TRANSFER = "api/studentdata/Transfer";

    public static final String URL_UPLOAD_ENROLLMENTS = "api/upload/enrollment";
    public static final String URL_UPLOAD_ATTENDANCE = "api/upload/attendance";
    public static final String URL_UPLOAD_PROMOTION = "api/upload/promotion";
    public static final String URL_UPLOAD_WITHDRAWAL = "api/upload/withdrawal";
    public static final String URL_UPLOAD_SCHOOL_AUDITS = "api/upload/schoolaudit";
    public static final String URL_UPLOAD_ENROLLMENT_IMAGES = "/api/upload/enrollmentimage/";
    public static final String URL_UPLOAD_STUDENT_IMAGES = "/api/studentdata/profileimage/";
    public static final String DEATH_CERT_FILE_TYPE = "D";
    public static final String MEDICAL_CERT_FILE_TYPE = "M";
    public static final String BFORM_FILE_TYPE = "B";

    public static final String URL_FEES_RECEIPT = "api/upload/FeesReceipt";
    public static final String URL_FEES_INVOICE = "api/upload/FeesInvoice";

    public static final String URL_CASH_DEPOSIT = "api/upload/CashDeposit";
    public static final String URL_GET_CASH_RECEIPTS = "api/fees/GetReceipts";
    public static final String URL_GET_CASH_DEPOSITS = "api/fees/GetCashDeposits";
    public static final String URL_GET_CASH_INVOICES = "api/fees/GetInvoices";
    public static final String URL_KEEP_ALIVE_TIMER = "api/KeepAlive";

    public static final String URL_UPLOAD_CASH_DEPOSIT_IMAGES = "/api/upload/cashdepositimage/";
    public static final String URL_CHANGE_PASSWORD = "api/changepassword";
    public static final String URL_FORGOT_PASSWORD = "api/forgotpassword-bynumber";

    public static final String URL_SINGLE_STUDENT = "api/GetStudentById/{id}";
//    public static final String URL_SINGLE_STUDENT_FOR_TRANSFER = "api/GetStudentById/{studentId}";

    public static final String PROFILE_INCOMPLETE_KEY = "I";
    public static final String PROFILE_COMPLETE_KEY = "C";
    public static final String PROFILE_APPROVED_KEY = "A";
    public static final String PROFILE_REJECTED_KEY = "R";
    public static final String PROFILE_PENDING = "P";


    // Filters for searching student profile
    public static final String All = "All";
    public static final String NewAdmissions = "NewAdmissions";
    public static final String Readmissions = "Readmissions";
    public static final String Withdrawals = "Withdrawals";
    public static final String Promotions = "Promotions";
    public static final String Graduations = "Graduations";

    public static final String AllStatus = "AllStatus";
    public static final String active = "active";
    public static final String inactive = "inactive";
    public static final String withdrawn = "withdrawn";
    public static final String graduated = "graduated";

    public static final String[] monthArray = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] monthArray2 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static final String URL_GET_FEES_HEADERS = "api/fees/GetTransactions";
    public static final String URL_FEES_HEADER = "api/upload/FeesTransaction";
    public static final String URL_GET_FEES_TRANSACTION_CSV = "api/fees/GetTransactions/Zip";
    public static final String TABLE_NAME_HEADER_FOR_CSV = "header";
    public static final String TABLE_NAME_DETAIL_FOR_CSV = "detail";


    public static final String KEY_STATE = "STATE";
    public static final String VALUE_SCHOOL_SELECT = "SELECT_SCHOOL";
    public static final String VALUE_DASHBOARD = "DASHBOARD";


    //Ranges
    public static final double STUDENT_MONTHLY_FEE_UPPER_RANGE = 1500;
    public static final double STUDENT_MONTHLY_FEE_LOWER_RANGE = 0;
    public static final double STUDENT_OPENING_BALANCE_FEE_UPPER_RANGE = 13000;
    public static final double STUDENT_OPENING_BALANCE_FEE_LOWER_RANGE = -7000;
    public static final String HIDE_Employee = "employee";
    public static final String HIDE_EmployeeListing = "EmployeeListing";
    public static final String HIDE_EmployeeLeaveAndAttend = "EmployeeLeaveAndAttend";
    public static String Action_PendingSyncChanged = BuildConfig.APPLICATION_ID + ".onsyncrecordchanged";
    public static String Action_receiverSyncVisibility = BuildConfig.APPLICATION_ID + ".onsyncvisibility";
    public static final String HIDE_EmployeeResignation = "EmployeeResignation";
    public static final String HIDE_EmployeeTermination = "EmployeeTermination";
    public static final String HIDE_StudentTransfer = "StudentTransfer";
    public static final String HIDE_StudentVisitForm = "StudentVisitForm";
    public static final String HIDE_TCTEntry = "TCTEntry";
    public static final String HIDE_Expense = "Expense";
    public static final String HIDE_Survey = "Survey";
    public static final String HIDE_Set_Fess = "SetFees";

    //Hr Upload Url constants
    public static final String URL_UPLOAD_EMPLOYEE_DETAIL = "api/uploadhrdata/HRUserUpdate";
    public static final String URL_UPLOAD_EMPLOYEE_TEACHER_LEAVES = "api/uploadhrdata/UploadTeacherLeaves";
    public static final String URL_UPLOAD_RESIGNED_RECEIVED = "api/uploadhrdata/UploadResignRecieved";
    public static final String URL_UPLOAD_SEPARATION_APPROVAL = "api/uploadhrdata/UpdateSeparationApprovals";
    public static final String URL_UPLOAD_TEACHER_ATTENDANCE = "api/uploadhrdata/UploadTeacherAttendance";
    public static final String URL_UPLOAD_AuditLogFiles = "/api/upload/AuditLogFiles";
    public static final String URL_UPLOAD_SEPARATION_IMAGES = "/api/uploadhrdata/UploadSeparationImage/";
    public static final String URL_UPLOAD_USER_IMAGES = "/api/uploadhrdata/UploadUserImage/";
    public static final String syncActionPerformedFrom = "syncActionPerformedFrom";

    public static final String URL_UPLOAD_TCT_EMP_TAGGING = "api/hrtct/UploadTCTEmpSubjectTagging";
    public static final String URL_GET_HRTCT_METADATA = "api/hrtct/getmetadata";

    public static final String CHANNEL_ID_Error_Notificaton = "tcf_error_notify";
    public static final String CHANNEL_ID_TCT_Notificaton = "tcf_tct_notify";
    public static final String CHANNEL_ID_REVIEW_FAILED_Notification = "tcf_review_failed_notify";
    public static final int TCTNotifyId = 101;
    public static final int studentInfoReviewFailed = 103;

    public static final String CHANNEL_ID_Sync_Notificaton = "tcf_sync_notify";
    public static final int SYNCNotifyId = 102;

    public static final int GENERAL_MODULE = 0;
    public static final int STUDENT_MODULE = 1;
    public static final int FINANCE_MODULE = 2;
    public static final int EMPLOYEE_MODULE = 3;
    public static final int TCTENTRY_MODULE = 4;
    public static final int SCHOOL_MODULE = 5;
    public static final int IMAGE_MODULE = 6;
    public static final int HELP_MODULE = 7;
    public static final int EXPENSE_MODULE = 8;


    public static final String GeneralModuleValue = "0";
    public static final String StudentModuleValue = "1";
    public static final String FinanceModuleValue = "2";
    public static final String StudentPromotionModuleValue = "3";
    public static final String StudentGraduationModuleValue = "4";
    public static final String CalendarModuleValue = "5";
    public static final String HREmployeeListingModuleValue = "6";
    public static final String HRAttendanceAndLeavesModuleValue = "7";
    public static final String HRResignationModuleValue = "8";
    public static final String HRTerminationModuleValue = "9";
    public static final String StudentTransferModuleValue = "10";
    public static final String StudentVisitFormsModuleValue = "11";
    public static final String TCTModuleValue = "12";
    public static final String ExpenseModuleValue = "13";
    public static final String SuurveyModuleValue = "14";


    public static final int roleId_27_P = 27;
    public static final int roleId_7_AEM = 7;
    public static final int roleId_8_AM = 8;
    public static final int roleId_9_AC = 9;
    public static final int roleId_75_ = 75;
    public static final int roleId_101_ST = 101;
    public static final int roleId_102_AA = 102;
    public static final int roleId_103_V = 103;
    public static final int roleId_109_CM = 109; //Cluster Manager
    public static final int roleId_112_ = 112;

    //Help URL constant
    public static final String GET_HELP_DATA = "api/helpdata";
    public static final String URL_UPLOAD_FEEDBACK = "api/uploadhelpdata/Feedback";
    public static boolean feedbackDialogCanceledPressed = false;
    public static boolean isFeedbackDialogShowing = false;

    //Internet Status URL constant
    public static final String URL_UPLOAD_CONNECTION_INFO_RESULT = "api/upload/ConnectionInfoResult";
    public static final String URL_UPLOAD_CONNECTION_INFO_UPLOAD_TEST = "api/upload/ConnectionInfoUploadTest";

    //Expense constants
    public static final String KEY_TRANSACTION_TYPE = "transaction";
    public static final String VALUE_TRANSACTION_WITHDRAW = "Withdraw";
    public static final String VALUE_TRANSACTION_DEPOSIT = "Deposit";
    public static final String KEY_EXPENSE_TYPE = "expensetype";
    public static final String VALUE_EXPENSE_TYPE_PETTYCASH = "pettycash";
    public static final String VALUE_EXPENSE_TYPE_ADVANCE = "advance";
    public static final String VALUE_EXPENSE_TYPE_SALARY = "salary";
    public static final int SUBHEAD_ID_BANKTOCASH_EXPENSE = 46;
    public static final int SUBHEAD_ID_CASHTOBANK_EXPENSE = 47;
    public static final int SUBHEAD_ID_SALARY_EXPENSE = 48;
    public static final int FLOW_IN_ID_EXPENSE = 1;
    public static final int FLOW_OUT_ID_EXPENSE = 2;
    public static final int BUCKET_BANK_ID_EXPENSE = 1;
    public static final int BUCKET_CASH_ID_EXPENSE = 2;
    public static final int TRANSACTION_CATEGORY_NORMAL_EXPENSE = 1;
    public static final int TRANSACTION_CATEGORY_CORRECTION_EXPENSE = 2;



    public static final String NetworkConnectionFrequencyKey = "NCF";
    public static final String NCI_LastUploadedOn = "NCI_LastUploadedOn";
    public static final String NewLogin = "NewLogin";
    public static final String SyncStartTime = "SyncStartTime";

    //Expense URL
    public static final String URL_EXPENSE_CHECKSUM = "api/expense/GetCheckSum";

    public static final String URL_GET_EXPENSE_EXPENSES_METADATA = "api/expenses/metadata/{schoolId}";
    public static final String URL_GET_EXPENSE_EXPENSES = "api/expenses/{schoolId}";

    public static final String URL_UPLOAD_EXPENSE_TRANSACTION = "api/expenses/upload/{schoolId}";
    public static final String URL_UPLOAD_EXPENSE_TRANSACTION_IMAGE = "/api/uploads/expenseimage/";
    public static final String logoutKey = "logoutKey";


    public static boolean isSchoolAndClassSynced = false;

    public static final String FinanceSyncCompleted = "FinanceSyncCompleted";

    //AppSync Status Constants
    public static final String URL_UPLOAD_APP_SYNC_STATUS = "api/upload/AppSyncStats";
    public static boolean isAppSyncTableFirstTime = false;

    //AppSyncStatusMaster Constants
    public static final String URL_UPLOAD_APP_SYNC_STATUS_MASTER = "api/upload/AppSyncStatsMaster";
}


//Latest
//production
//    public static final String BASE_URL = "https://siswebsrv.tcf.org.pk:7443/";

//non production
//public static final String BASE_URL = "https://sisuatserv.tcf.org.pk:7443/";


//previous url
//    public static final String BASE_URL = "https://sisuatp1.tcf.org.pk:6443";

//    public static final String BASE_URL = "http://sisapp.tcf.org.pk:888/";