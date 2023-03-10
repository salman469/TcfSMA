package com.tcf.sma.DataSync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.tcf.sma.Activities.ChangePasswordActivity;
import com.tcf.sma.Activities.HRTCT.EmployeeTCT_EntryActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Activities.StudentProfileSearchActivity;
import com.tcf.sma.Activities.SyncProgress.SyncProgressActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseDataResponseModel;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AcademicSession;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppReceipt;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Helpers.DbTables.FeesCollection.CashDeposit;
import com.tcf.sma.Helpers.DbTables.FeesCollection.ErrorLog;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Helpers.DbTables.FeesCollection.SessionInfo;
import com.tcf.sma.Helpers.DbTables.FeesCollection.UserInfo;
import com.tcf.sma.Helpers.DbTables.Global.GlobalHelperClass;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Helpers.UnzipUtility;
import com.tcf.sma.HttpServices.HttpConnectionClass;
import com.tcf.sma.Interfaces.IProcessComplete;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AcademicSessionModel;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AreaModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendanceStudentModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.CampusModel;
import com.tcf.sma.Models.CheckSumModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.ErrorResponseModel;
import com.tcf.sma.Models.FeesDetailUploadModel;
import com.tcf.sma.Models.FeesHeaderUploadModel;
import com.tcf.sma.Models.Fees_Collection.AttendanceSummary;
import com.tcf.sma.Models.Fees_Collection.CashDepositModel;
import com.tcf.sma.Models.Fees_Collection.FeesDuesModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.Fees_Collection.FeesReceiptModel;
import com.tcf.sma.Models.Fees_Collection.GeneralUploadResponseModel;
import com.tcf.sma.Models.Fees_Collection.KeepAliveModel;
import com.tcf.sma.Models.Fees_Collection.SessionInfoModel;
import com.tcf.sma.Models.Fees_Collection.SummaryModel;
import com.tcf.sma.Models.LocationModel;
import com.tcf.sma.Models.NetwrokConnection.NetworkConnectionInfo;
import com.tcf.sma.Models.OnSyncUploadModel;
import com.tcf.sma.Models.PromotionDBModel;
import com.tcf.sma.Models.PromotionStudentDBModel;
import com.tcf.sma.Models.RegionModel;
import com.tcf.sma.Models.RetrofitModels.AttendanceStudentUploadModel;
import com.tcf.sma.Models.RetrofitModels.AttendanceUploadModel;
import com.tcf.sma.Models.RetrofitModels.ElectiveSubjectModel;
import com.tcf.sma.Models.RetrofitModels.EnrollmentImageUploadModel;
import com.tcf.sma.Models.RetrofitModels.EnrollmentUploadModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseAmountClosingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseHeadsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashMonthlyLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubHeadsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadExceptionLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsMonthlyModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionBucketModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionCategoryModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionFlowModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionImagesModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionUploadResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePendingApprovalModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePositionModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeQualificationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSchoolModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeTeacherAttendanceModel;
import com.tcf.sma.Models.RetrofitModels.HR.SeparationAttachmentsModel;
import com.tcf.sma.Models.RetrofitModels.HR.SeparationDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.UploadEmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.UserImageModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTDesginationModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjTagReasonModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTLeaveTypeModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTPhaseModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTSubjectsModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.UploadTCTEmployeeSubTagModel;
import com.tcf.sma.Models.RetrofitModels.Help.FAQsModel;
import com.tcf.sma.Models.RetrofitModels.Help.FeedbackModel;
import com.tcf.sma.Models.RetrofitModels.Help.HelpDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.Help.UploadFeedbackModel;
import com.tcf.sma.Models.RetrofitModels.Help.UserManualModel;
import com.tcf.sma.Models.RetrofitModels.LoginRequestModel;
import com.tcf.sma.Models.RetrofitModels.LoginResponseModel;
import com.tcf.sma.Models.RetrofitModels.MetaDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.NationalityModel;
import com.tcf.sma.Models.RetrofitModels.ReligionModel;
import com.tcf.sma.Models.RetrofitModels.StudentDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.SyncCashDepositModel;
import com.tcf.sma.Models.RetrofitModels.SyncCashInvoicesModel;
import com.tcf.sma.Models.RetrofitModels.UploadCashInHandModel;
import com.tcf.sma.Models.RetrofitModels.UploadPromotionModel;
import com.tcf.sma.Models.RetrofitModels.UploadStudentsAuditModel;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentImageModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.StudentUploadResponseModel;
import com.tcf.sma.Models.SyncCashReceiptsModel;
import com.tcf.sma.Models.SyncFeesHeaderModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusMasterModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusModel;
import com.tcf.sma.Models.SyncProgress.SyncDownloadUploadModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.Models.WithdrawalModel;
import com.tcf.sma.Models.WithdrawalReasonModel;
import com.tcf.sma.R;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
import com.tcf.sma.Services.BasicImageDownloader;
import com.tcf.sma.SyncClasses.SyncAdapter;
import com.tcf.sma.utils.FinanceCheckSum;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass.ImagePath;
import static com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass.SeparationAttachment;

/**
 * Created by Mohammad.Haseeb on 2/15/2017.
 */

public class DataSync {

    public static boolean isSyncRunning = false;
    public static boolean isSyncSuccessfull = false;
    public static boolean areAllServicesSuccessful = true;
    public static int failureCount = 0;
    private Context context;
    private int error_count = 0;
    private int feeDetailCount = 0;

    public static boolean isFinanceSyncSuccessful = true;
    public static int failureCountForAllModules = 0;
    private static DataSync instance;

    public DataSync(Context context) {
        this.context = context;
    }

    public static DataSync getInstance(Context context) {
        if (instance == null)
            instance = new DataSync(context);

        return instance;
    }

    public void syncMetaData(final Context context, final int schoolId) {

        try {
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            String classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_CLASS,
                    DatabaseHelper.getInstance(context).CLASS_MODIFIED_ON,
                    null,
                    Integer.parseInt(AppConstants.GeneralModuleValue));

            String section_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_SECTION,
                    DatabaseHelper.getInstance(context).SECTION_MODIFIED_ON,
                    null,
                    Integer.parseInt(AppConstants.GeneralModuleValue));

            String school_classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS,
                    DatabaseHelper.getInstance(context).SCHOOL_CLASS_MODIFIED_ON,
                    DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + "=" + schoolId,
                    Integer.parseInt(AppConstants.GeneralModuleValue));

            String scholarship_category_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    Scholarship_Category.SCHOLARSHIP_CAT,
                    Scholarship_Category.SCHOLARSHIP_CATEGORY_MODIFIED_ON,
                    Scholarship_Category.SCHOOL_ID + "=" + schoolId,Integer.parseInt(AppConstants.GeneralModuleValue));

            String withdrawal_reasons_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL_REASON,
                    DatabaseHelper.getInstance(context).WITHDRAWAL_REASON_MODIFIED_ON,
                    null,Integer.parseInt(AppConstants.GeneralModuleValue));

            String calendars_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.TABLE_CALENDAR,
                    DatabaseHelper.MODIFIED_ON,
                    null,Integer.parseInt(AppConstants.GeneralModuleValue));

            String campus_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.TABLE_CAMPUS,
                    DatabaseHelper.MODIFIED_ON,
                    null,Integer.parseInt(AppConstants.GeneralModuleValue));

            String location_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.TABLE_LOCATION,
                    DatabaseHelper.MODIFIED_ON,
                    null,Integer.parseInt(AppConstants.GeneralModuleValue));

            String areas_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.TABLE_AREA,
                    DatabaseHelper.MODIFIED_ON,
                    null,Integer.parseInt(AppConstants.GeneralModuleValue));

            String region_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.TABLE_REGION,
                    DatabaseHelper.MODIFIED_ON,
                    null,Integer.parseInt(AppConstants.GeneralModuleValue));

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<MetaDataResponseModel> call = apiInterface.getMetaData(schoolId,
                    classes_modifiedOn,section_modifiedOn,school_classes_modifiedOn,
                    scholarship_category_modifiedOn,withdrawal_reasons_modifiedOn,calendars_modifiedOn,
                    campus_modifiedOn,location_modifiedOn,areas_modifiedOn,region_modifiedOn,token);

            call.enqueue(new Callback<MetaDataResponseModel>() {
                @Override
                public void onResponse(Call<MetaDataResponseModel> call, Response<MetaDataResponseModel> response) {
                    if (response.isSuccessful()) {
                        MetaDataResponseModel mdrs = response.body();
                        SyncClasses(mdrs.getTCFClass());
                        SyncSections(mdrs.getSection());
                        SyncSchoolClasses(mdrs.getSchool_Classes(), schoolId);
                        syncScholarshipCategory(mdrs.getScholarship_Category(), schoolId);
                        SyncWithdrawalReasons(mdrs.getWithdrawal_Reason());
//                        SyncSchoolSSRSummary(mdrs.getSchoolSSRSummary(), schoolId);
                    } else {
                        areAllServicesSuccessful = false;
                    }
                }

                @Override
                public void onFailure(Call<MetaDataResponseModel> call, Throwable t) {
                    areAllServicesSuccessful = false;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            areAllServicesSuccessful = false;
        }
    }

    public void getSummaryForSchools() {
        ArrayList<SchoolModel> sms = DatabaseHelper.getInstance(context).getAllUserSchools();
        for (SchoolModel model : sms) {
            getSummary(context, model.getId());
        }
    }

    public void getSummary(final Context context, final int schoolId) {

        try {
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            ApiInterface apiInterface = ApiClient.getClient(10, 0, context).create(ApiInterface.class);
            Call<SummaryModel> call = apiInterface.getSummary(schoolId, token);
            call.enqueue(new Callback<SummaryModel>() {
                @Override
                public void onResponse(Call<SummaryModel> call, Response<SummaryModel> response) {
                    if (response.isSuccessful()) {
                        SummaryModel mdrs = response.body();

                        if (mdrs != null) {
                            if (mdrs.getSchoolSSRSummary() != null && mdrs.getSchoolSSRSummary().size() > 0) {
                                SyncSchoolSSRSummary(mdrs.getSchoolSSRSummary(), schoolId);
                            }

                            if (mdrs.getAttendanceSummary() != null && mdrs.getAttendanceSummary().size() > 0) {
                                SyncAttendanceSummary(mdrs.getAttendanceSummary(), schoolId);
                            }
                        }

                        AppModel.getInstance().appendLog(context, "Summary fetched..");

                    } else {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "Error in getting summary. Repsonse Code: " + response.code());

                    }
                }

                @Override
                public void onFailure(Call<SummaryModel> call, Throwable t) {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "Error in getting summary. Error: " + t.toString());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Exception raised in getting summary");
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
        }

    }

    public void syncStudentData(final Context context, final int schoolId, String dateFrom) {

        AppModel.getInstance().appendLog(context, "\nSyncing Student Data");
        AppModel.getInstance().setSelectedSchool(context, schoolId);
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;

        final AppSyncStatusModel appSyncStatusModel = new AppSyncStatusModel();

        if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
//            appSyncStatusModel = new AppSyncStatusModel();
            appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
            appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
            appSyncStatusModel.setSubModule("Student");
        }

        String student_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_STUDENT,
                DatabaseHelper.getInstance(context).STUDENT_MODIFIED_ON,
                DatabaseHelper.getInstance(context).STUDENT_SCHOOL_CLASS_ID
                        + " IN (Select id from " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS
                        + " where " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + schoolId + ")",
                Integer.parseInt(AppConstants.StudentModuleValue));

        String attendance_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_ATTENDANCE,
                DatabaseHelper.getInstance(context).ATTENDANCE_FOR_DATE,
                DatabaseHelper.getInstance(context).ATTENDANCE_SCHOOL_CLASS_ID
                        + " IN (Select id from " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS
                        + " where " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + schoolId + ")",
                Integer.parseInt(AppConstants.StudentModuleValue));

        String promotion_modifiedOn = AppModel.getInstance().getLastModifiedOnInDateTimeFormat(
                context,
                DatabaseHelper.getInstance(context).TABLE_PROMOTION,
                DatabaseHelper.getInstance(context).PROMOTION_MODIFIED_ON,
                DatabaseHelper.getInstance(context).PROMOTION_SCHOOL_CLASS_ID
                        + " IN (Select id from " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS
                        + " where " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + schoolId + ")",
                Integer.parseInt(AppConstants.StudentModuleValue));

//        String withdrawal_modifiedOn = AppModel.getInstance().getLastModifiedOn(
//                context,
//                DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL,
//                DatabaseHelper.getInstance(context).WITHDRAWAL_MODIFIED_ON,
//                DatabaseHelper.getInstance(context).WITHDRAWAL_SCHOOL_ID + "=" + schoolId,
//                Integer.parseInt(AppConstants.StudentModuleValue));

        String enrollments_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_ENROLLMENT,
                "CASE WHEN (" + DatabaseHelper.getInstance(context).ENROLLMENT_CREATED_ON + " > " + DatabaseHelper.getInstance(context).ENROLLMENT_REVIEW_COMPLETED_ON + ")" +
                        " THEN " + DatabaseHelper.getInstance(context).ENROLLMENT_CREATED_ON+" ELSE " + DatabaseHelper.getInstance(context).ENROLLMENT_REVIEW_COMPLETED_ON+" END",
                DatabaseHelper.getInstance(context).ENROLLMENT_SCHOOL_ID + "=" + schoolId,
                Integer.parseInt(AppConstants.StudentModuleValue));

//        String schoolaudits_modifiedOn = AppModel.getInstance().getLastModifiedOn(
//                context,
//                DatabaseHelper.getInstance(context).TABLE_SCHOOL_AUDIT,
//                DatabaseHelper.getInstance(context).SCHOOL_AUDIT_MODIFIED_ON,
//                DatabaseHelper.getInstance(context).SCHOOL_AUDIT_SCHOOL_ID + "=" + schoolId);

        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<StudentDataResponseModel> call = apiInterface.getStudentData(schoolId, student_modifiedOn,
                attendance_modifiedOn,
                promotion_modifiedOn,
//                withdrawal_modifiedOn,
                enrollments_modifiedOn, token);
//        Call<StudentDataResponseModel> call = apiInterface.getStudentData(schoolId, dateFrom, token);
        try {
            Response<StudentDataResponseModel> sdrmResponse = call.execute();
            final StudentDataResponseModel sdrm = sdrmResponse.body();
            AppModel.getInstance().appendLog(context, "Student Data Sync Called got Response code :" + sdrmResponse.code() + "\n");
            if (sdrmResponse.isSuccessful()) {
                isSyncSuccessfull = true;

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(sdrmResponse.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                if (sdrm != null) {

                    try {
                        Thread StudentThread = new Thread(() -> {
                            AppModel.getInstance().appendLog(context, "Sync Students Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");
                            SyncStudents(sdrm.getStudent(), schoolId);
                            SyncAttendance(sdrm.getAttendance(), sdrm.getStudent_Attendance(), schoolId);
                            SyncPromotion(sdrm.getPromotion(), sdrm.getPromotion_Student(), schoolId);
                            SyncWithdrawal(sdrm.getWithdrawal(), schoolId);
                            SyncEnrollment(sdrm.getEnrollment(), sdrm.getEnrollment_Image(), schoolId);
                            SyncSchoolAudits(sdrm.getSchoolAudit(), schoolId);
                            AppModel.getInstance().appendLog(context, "Sync Students Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                        });
                        StudentThread.start();
//                        StudentThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"Sync Students Data Error: "+e.getMessage());
                    }
                }
//                else {
//                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
//                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
//                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
//                                appSyncStatusModel.getEndedOn()));
//                        appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(sdrmResponse.raw().body().contentLength(), false));
//                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
//                        AppConstants.isAppSyncTableFirstTime = true;
//                    }
//                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "Student Data Sync Called got Response code :" + sdrmResponse.code() + " Message " + sdrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Student data " + e.getMessage());
        }
    }

    public void syncSelectedSchoolStudentData(final Context context, String dateFrom) {

        List<SchoolModel> schoolModels = DatabaseHelper.getInstance(context)
                .getAllUserSchoolsById(AppModel.getInstance().getSearchedSchoolId(context));
        for (final SchoolModel model : schoolModels) {


            AppModel.getInstance().appendLog(context, "\nSyncing Student Data");
            AppModel.getInstance().setSelectedSchool(context, model.getId());
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                appSyncStatusModel.setSubModule("Student");
            }

            String student_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_STUDENT,
                    DatabaseHelper.getInstance(context).STUDENT_MODIFIED_ON,
                    DatabaseHelper.getInstance(context).STUDENT_SCHOOL_CLASS_ID
                            + " IN (Select id from " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS
                            + " where " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + model.getId() + ")",
                    Integer.parseInt(AppConstants.StudentModuleValue));

            String attendance_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_ATTENDANCE,
                    DatabaseHelper.getInstance(context).ATTENDANCE_FOR_DATE,
                    DatabaseHelper.getInstance(context).ATTENDANCE_SCHOOL_CLASS_ID
                            + " IN (Select id from " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS
                            + " where " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + model.getId() + ")",
                    Integer.parseInt(AppConstants.StudentModuleValue));

            String promotion_modifiedOn = AppModel.getInstance().getLastModifiedOnInDateTimeFormat(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_PROMOTION,
                    DatabaseHelper.getInstance(context).PROMOTION_MODIFIED_ON,
                    DatabaseHelper.getInstance(context).PROMOTION_SCHOOL_CLASS_ID
                            + " IN (Select id from " + DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS
                            + " where " + DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + " = " + model.getId() + ")",
                    Integer.parseInt(AppConstants.StudentModuleValue));

//            String withdrawal_modifiedOn = AppModel.getInstance().getLastModifiedOn(
//                    context,
//                    DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL,
//                    DatabaseHelper.getInstance(context).WITHDRAWAL_MODIFIED_ON,
//                    DatabaseHelper.getInstance(context).WITHDRAWAL_SCHOOL_ID + "=" + model.getId(),
//                    Integer.parseInt(AppConstants.StudentModuleValue));

            String enrollments_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.TABLE_ENROLLMENT,
                    "CASE WHEN (" + DatabaseHelper.getInstance(context).ENROLLMENT_CREATED_ON + " > " + DatabaseHelper.getInstance(context).ENROLLMENT_REVIEW_COMPLETED_ON + ")" +
                            " THEN " + DatabaseHelper.getInstance(context).ENROLLMENT_CREATED_ON+" ELSE " + DatabaseHelper.getInstance(context).ENROLLMENT_REVIEW_COMPLETED_ON+" END",
                    DatabaseHelper.getInstance(context).ENROLLMENT_SCHOOL_ID + "=" + model.getId(),
                    Integer.parseInt(AppConstants.StudentModuleValue));


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<StudentDataResponseModel> call = apiInterface.getStudentData(model.getId(), student_modifiedOn,
                    attendance_modifiedOn,
                    promotion_modifiedOn,
//                    withdrawal_modifiedOn,
                    enrollments_modifiedOn, token);
//            Call<StudentDataResponseModel> call = apiInterface.getStudentData(model.getId(), dateFrom, token);
            try {
                Response<StudentDataResponseModel> sdrmResponse = call.execute();
                final StudentDataResponseModel sdrm = sdrmResponse.body();
                AppModel.getInstance().appendLog(context, "Student Data Sync Called got Response code :" + sdrmResponse.code() + "\n");
                if (sdrmResponse.isSuccessful()) {
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(sdrmResponse.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    if (sdrm != null) {
                        try{
                            Thread StudentThread = new Thread(() -> {
                                AppModel.getInstance().appendLog(context, "Sync Students Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");
                                SyncStudents(sdrm.getStudent(), model.getId());
                                SyncAttendance(sdrm.getAttendance(), sdrm.getStudent_Attendance(), model.getId());
                                SyncPromotion(sdrm.getPromotion(), sdrm.getPromotion_Student(), model.getId());
                                SyncWithdrawal(sdrm.getWithdrawal(), model.getId());
                                SyncEnrollment(sdrm.getEnrollment(), sdrm.getEnrollment_Image(), model.getId());
                                SyncSchoolAudits(sdrm.getSchoolAudit(), model.getId());
                                AppModel.getInstance().appendLog(context, "Sync Students Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                            });
                            StudentThread.start();
//                            StudentThread.join();
                        }catch (Exception e){
                            e.printStackTrace();
                            AppModel.getInstance().appendErrorLog(context,"syncSelectedSchoolStudentData Error: "+e.getMessage());
                        }
                    }
                } else {
                    AppModel.getInstance().appendErrorLog(context, "Student Data Sync Called got Response code :" + sdrmResponse.code() + " Message " + sdrmResponse.message() + "\n");
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;

                }
            } catch (IOException e) {
                e.printStackTrace();
                areAllServicesSuccessful = false;
                failureCountForAllModules++;
                AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Student data " + e.getMessage());
            }
        }
    }

    public void getCashReceipts() {
        ArrayList<SchoolModel> sms = DatabaseHelper.getInstance(context).getAllUserSchools();
        for (SchoolModel model : sms) {
            if (model.getAllowedModule_App() != null && model.getAllowedModule_App().contains(AppConstants.FinanceModuleValue))
                syncCashReceipts(model.getSchool_yearId(), AppReceipt.getInstance(context).getMaxSysId());
        }
    }

    public void getFeesHeader() {
        ArrayList<SchoolModel> sms = DatabaseHelper.getInstance(context).getAllUserSchools();
        for (SchoolModel model : sms) {
            if (model.getAllowedModule_App() != null) {
                List<String> allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                    syncFeesHeader(model.getAcademic_Session_Id(), model.getId());
                    AppConstants.isAppSyncTableFirstTime = false;
                }
            }
        }
    }

    public void getInvoices() {
        ArrayList<SchoolModel> sms = DatabaseHelper.getInstance(context).getAllUserSchools();
        for (SchoolModel model : sms) {
            if (model.getAllowedModule_App() != null && model.getAllowedModule_App().contains(AppConstants.FinanceModuleValue))
                syncCashInvoices(model.getSchool_yearId(), AppInvoice.getInstance(context).getMaxSysId());
        }
    }

    public void syncCashInvoices(int schoolYearId, int lastServerId) {
        AppModel.getInstance().appendLog(context, "\nSyncing Cash Reciepts");
//        SurveyAppModel.getInstance().setSelectedSchool(context, schoolId);
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<ArrayList<SyncCashInvoicesModel>> call = apiInterface.getInvoices(schoolYearId, lastServerId, token);
        call.enqueue(new Callback<ArrayList<SyncCashInvoicesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SyncCashInvoicesModel>> call, Response<ArrayList<SyncCashInvoicesModel>> response) {
                if (response.isSuccessful()) {
                    AppModel.getInstance().appendLog(context, "Student Data Sync Called got Response code :" + response.code() + "\n");

                    for (SyncCashInvoicesModel cdm : response.body()) {
                        try {

                            AppInvoice.getInstance(context).insertDownloaedCashInvoices(changeFeesDueModel(cdm));

                        } catch (Exception e) {
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Exception is thrown while inserting in Syncing Cash Deposits\n");
                        }
                    }
                } else {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "Student Data Sync Called got Response code :" + response.code() + " Message " + response.message() + "\n");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<SyncCashInvoicesModel>> call, Throwable t) {
                areAllServicesSuccessful = false;
            }
        });
    }

    private FeesDuesModel changeFeesDueModel(SyncCashInvoicesModel scim) {
        FeesDuesModel model = new FeesDuesModel();

        String createdOn = AppModel.getInstance().convertDatetoFormat(scim.getCreated_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");
        String uploadedOn = AppModel.getInstance().convertDatetoFormat(scim.getUploaded_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");
        String downloadedOn = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");


        model.setUploaded_on(uploadedOn);
        model.setCreated_on(createdOn);
        model.setDownloadedOn(downloadedOn);
        model.setCreated_by(scim.getCreated_by() + "");
        model.setFees_admission(scim.getFees_admission());
        model.setFees_copies(scim.getFees_copies());
        model.setFees_books(scim.getFees_books());
        model.setFees_tution(scim.getFees_tution());
        model.setFees_other(scim.getFees_others());
        model.setFees_uniform(scim.getFees_uniform());
        model.setFees_exam(scim.getFees_exam());

        model.setSys_id(scim.getId());
        model.setSchool_year_id(scim.getSchool_year_id());
        model.setDeviceId(scim.getDevice_id());
        model.setSchoolclass_id(scim.getSchoolclass_id());
        model.setStudent_id(scim.getStudent_id());

        model.setCreated_from(scim.getCreated_from());


        return model;
    }

    public void getCashDeposits() {
        ArrayList<SchoolModel> sms = DatabaseHelper.getInstance(context).getAllUserSchools();
        for (SchoolModel model : sms) {
            List<String> allowedModules = null;
            if (model.getAllowedModule_App() != null) {
                allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
            }
            if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue)) {
                syncCashDeposits(model.getId());
                AppConstants.isAppSyncTableFirstTime = false;
            }
        }
    }

    public void syncCashDeposits(int schoolId) {


        int cashDepositServerCount = 1, lastIdOfLastResponse = 0;
        int downloadedCount = 0, downloadedTotalCount = 0;

        long startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "syncCashDeposits", true);

        try {

            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            //if Download limit in server 100

            while (cashDepositServerCount != 0) {

                int lastServerId = CashDeposit.getInstance(context).getMaxSysId(schoolId);
                String lastModifiedOn = CashDeposit.getInstance(context).getLastModifiedOn(schoolId);
                if (lastModifiedOn == null)
                    lastModifiedOn = "";


                AppSyncStatusModel appSyncStatusModel = null;

                if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                    appSyncStatusModel = new AppSyncStatusModel();
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                    appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.FinanceModuleValue));
                    appSyncStatusModel.setSubModule("FC Deposit");
                }


                AppModel.getInstance().appendLog(context, "\nSyncing Cash Deposits");
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                Call<ArrayList<SyncCashDepositModel>> call = apiInterface.getCashDeposits(schoolId, lastServerId, lastModifiedOn, token);
                try {
                    final Response<ArrayList<SyncCashDepositModel>> response = call.execute();
                    final ArrayList<SyncCashDepositModel> cdrm = response.body();
                    AppModel.getInstance().appendLog(context, "Syncing Cash Deposits Called got Response code :" + response.code() + "\n");
                    if (response.isSuccessful()) {
                        isFinanceSyncSuccessful = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                    appSyncStatusModel.getEndedOn()));
                            appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        if (cdrm != null) {
                            cashDepositServerCount = cdrm.size();

                            if (cashDepositServerCount > 0) {

                                int lastRecordIndex = cdrm.size() - 1;
                                if (cdrm.get(lastRecordIndex).getId() == lastIdOfLastResponse)
                                    break;

                                downloadedTotalCount += cdrm.size();
                                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(downloadedTotalCount,
                                        "Cash Deposit", AppConstants.FINANCE_MODULE, "Downloading", schoolId);

                                lastIdOfLastResponse = cdrm.get(lastRecordIndex).getId();

                                isSyncSuccessfull = true;
                                for (SyncCashDepositModel cdm : cdrm) {
                                    try {
                                        if (FeesCollection.getInstance(context).FindCashDepositRecord(cdm.getId(), cdm.getSchool_id()) == 0) {
                                            long headerId = CashDeposit.getInstance(context).insertDownloadedCashDeposit(changeCashDepositModel(cdm));
                                            if (headerId < 1) {
                                                AppModel.getInstance().appendErrorLog(context, "While inserting CashDeposit couldn't insert record: " + cdm.print());
                                            }

                                            if (headerId > 0) {

                                                downloadedCount++;
                                            }
                                        } else {
                                            long headerId = CashDeposit.getInstance(context).updateDownloadedCashDeposit(changeCashDepositModel(cdm));
                                            if (headerId < 1) {
                                                AppModel.getInstance().appendErrorLog(context, "While updating CashDeposit couldn't update record: " + cdm.print());
                                            }

                                            if (headerId > 0) {

                                                downloadedCount++;
                                            }
                                        }

                                    } catch (Exception e) {
                                        areAllServicesSuccessful = false;
                                        AppModel.getInstance().appendErrorLog(context, "Exception is thrown while inserting in Syncing Cash Deposits\n");
                                    }

                                    //Update sync progress
                                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                                }
                            }
                        } else {
                            cashDepositServerCount = 0;
                        }
                    } else {
                        isSyncSuccessfull = false;
                        areAllServicesSuccessful = false;
                        isFinanceSyncSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "Syncing Cash Deposits Called got Response code :" + response.code() + " Message " + response.message() + "\n");
                        break;
                    }

                } catch (Exception e) {
                    areAllServicesSuccessful = false;
                    isFinanceSyncSuccessful = false;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Cash Deposits " + e.getMessage());
                    break;
                }

//        call.enqueue(new Callback<ArrayList<SyncCashDepositModel>>() {
//            @Override
//            public void onResponse(Call<ArrayList<SyncCashDepositModel>> call, Response<ArrayList<SyncCashDepositModel>> response) {
//                if (response.isSuccessful()) {
//                    SurveyAppModel.getInstance().appendLog(context, "Syncing Cash Deposits Called got Response code :" + response.code() + "\n");
//
//                    for (SyncCashDepositModel cdm : response.body()) {
//                        try {
//
//                            CashDeposit.getInstance(context).insertDownloadedCashDeposit(changeCashDepositModel(cdm));
//
//                        } catch (Exception e) {
//                            areAllServicesSuccessful = false;
//                            SurveyAppModel.getInstance().appendErrorLog(context, "Exception is thrown while inserting in Syncing Cash Deposits\n");
//                        }
//                    }
//                } else {
//                    areAllServicesSuccessful = false;
//                    SurveyAppModel.getInstance().appendErrorLog(context, "Syncing Cash Deposits Called got Response code :" + response.code() + " Message " + response.message() + "\n");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<SyncCashDepositModel>> call, Throwable t) {
//                areAllServicesSuccessful = false;
//            }
//        });
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in get cash deposit method: " + e.getMessage());
        }

        syncingLogsTime(startTime, schoolId, "syncCashDeposits", false);
    }

    private CashDepositModel changeCashDepositModel(SyncCashDepositModel cdm) {

        CashDepositModel model = new CashDepositModel();

        String createdOn = AppModel.getInstance().convertDatetoFormat(cdm.getCreated_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");
        String uploadedOn = AppModel.getInstance().convertDatetoFormat(cdm.getUploaded_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");
        String downloadedOn = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");
        String modifiedOn = AppModel.getInstance().convertDatetoFormat(cdm.getModified_on(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");

        model.setModified_on(modifiedOn);
        model.setCreatedOn(createdOn);
        model.setCreatedBy(cdm.getCreated_by() + "");
        model.setDownloadedOn(downloadedOn);
        model.setSysId(cdm.getId());
        model.setUploadedOn(uploadedOn);
        model.setDeviceId(cdm.getDevice_id());
        model.setDepositAmount(cdm.getDeposit_slip_amount());
        model.setDepositSlipNo(cdm.getDeposit_slip_no());
        model.setDepositSlipFilePath(cdm.getPicture_slip_filename());
        model.setSchoolId(cdm.getSchool_id());

        return model;


    }

    public void syncFeesHeader(int academicSessionId, int schoolID) {

        long startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolID, "syncFeesHeader", true);


        boolean isFeeHeaderRecordsExists = FeesCollection.getInstance(context).getFeesHeaderCount(schoolID);
//        boolean isFeeDetailRecordsExists = FeesCollection.getInstance(context).getFeesDetailCount(schoolID);
        if (!isFeeHeaderRecordsExists) {
            try {

                AppSyncStatusModel appSyncStatusModel = null;

                if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                    appSyncStatusModel = new AppSyncStatusModel();
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                    appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.FinanceModuleValue));
                    appSyncStatusModel.setSubModule("Fee Collection");
                }

                AppSyncStatusModel finalAppSyncStatusModel = appSyncStatusModel;
                Thread feeHeaderThread = new Thread(() -> downloadFeeTransactionCSV(schoolID, AppConstants.TABLE_NAME_HEADER_FOR_CSV,finalAppSyncStatusModel));
                feeHeaderThread.start();
                feeHeaderThread.join();

                Thread feeDetailThread = new Thread(() -> downloadFeeTransactionCSV(schoolID, AppConstants.TABLE_NAME_DETAIL_FOR_CSV,finalAppSyncStatusModel));
                feeDetailThread.start();
                feeDetailThread.join();

                if (!AppConstants.isAppSyncTableFirstTime && finalAppSyncStatusModel != null) { //For AppSyncStatus
                    finalAppSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    finalAppSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(finalAppSyncStatusModel.getStartedOn(),
                            finalAppSyncStatusModel.getEndedOn()));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(finalAppSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            feeDetailCount = 0;
            int count = 0, updateFeeCount = 0;

            int feeHeaderDownloadedCount = 1, lastIdOfLastResponse = 0;
            int downloadedCount = 0, downloadedTotalCount = 0;
            SyncDownloadUploadModel syncDownloadUploadModel = null;
//        String lastModifiedDate = "";
            try {

                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;

                CheckSumModel serverCheckSum = FinanceCheckSum.Instance(new WeakReference<>(context))
                        .getServerCheckSum(schoolID, academicSessionId);

                AppSyncStatusModel appSyncStatusModel = null;

                if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                    appSyncStatusModel = new AppSyncStatusModel();
                    appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                    appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.FinanceModuleValue));
                    appSyncStatusModel.setSubModule("Fee Collection");
                }

                //if Download limit in server 100

                while (feeHeaderDownloadedCount != 0) {

                    int lastServerId = FeesCollection.getInstance(context).getMaxSysId(schoolID);
                    String lastModifiedDate = FeesCollection.getInstance(context).getLatestModifiedOn(schoolID);


                    AppModel.getInstance().appendLog(context, "\nSyncing Fees Header last server id " + lastServerId + " School Id " + schoolID);


                    ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                    Call<ArrayList<SyncFeesHeaderModel>> call = apiInterface.getFeesHeader(academicSessionId, schoolID, lastServerId, lastModifiedDate, token);

                    try {
                        final Response<ArrayList<SyncFeesHeaderModel>> response = call.execute();
                        final ArrayList<SyncFeesHeaderModel> fhrm = response.body();
                        AppModel.getInstance().appendLog(context, "Fees Header Sync Called got Response code :" + response.code() + "\n");
                        if (response.isSuccessful()) {
                            isSyncSuccessfull = true;
                            isFinanceSyncSuccessful = true;

                            if (fhrm != null) {
                                feeHeaderDownloadedCount = fhrm.size();
                                if (feeHeaderDownloadedCount > 0) {

                                    int lastRecordIndex = fhrm.size() - 1;
                                    if (fhrm.get(lastRecordIndex).getId() == lastIdOfLastResponse)
                                        break;

//                                if (fhrm.get(lastRecordIndex).getModified_on() != null && !fhrm.get(lastRecordIndex).getModified_on().isEmpty()) {
//                                    lastModifiedDate = fhrm.get(lastRecordIndex).getModified_on();
//                                }else {
////                                    lastModifiedDate = "2000-01-01 00:00:00 AM";
//                                }

//                                downloadedTotalCount += fhrm.size();

                                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                                        AppConstants.isAppSyncTableFirstTime = true;
                                    }

                                    appSyncStatusModel.setDownloaded(appSyncStatusModel.getDownloaded()
                                            + AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));

                                    if (syncDownloadUploadModel == null) {
                                        downloadedTotalCount = serverCheckSum.getFeesHeaderCount();
                                        syncDownloadUploadModel = new SyncDownloadUploadModel(downloadedTotalCount,
                                                "Fees Header", AppConstants.FINANCE_MODULE, "Downloading", schoolID);
                                    }

                                    lastIdOfLastResponse = fhrm.get(lastRecordIndex).getId();

//                                SyncProgressActivity.model.isStudentProgressEnable().setValue(true);

                                    AppModel.getInstance().appendLog(context, "Headers count " + response.body().size() + " after lastServerId " + lastServerId);
                                    System.out.println("Fees_Header_count---> " + fhrm.size());
//                            if (lastServerId != 0) {
                                    for (SyncFeesHeaderModel model : fhrm) {
                                        if (FeesCollection.getInstance(context).FindFeesHeaderRecord(model.getId()) == 0) {
                                            int headerId = 0;
                                            try {
                                                headerId = (int) FeesCollection.getInstance(context).addDownloadedFeesHeader(model);
                                                if (headerId < 1) {
                                                    System.out.println("FeesHeader ---> " + model.print());
                                                    AppModel.getInstance().appendErrorLog(context, "While inserting FeesHeader couldn't insert record: " + model.print());
                                                }
                                                if (headerId > 0) {
                                                    count++;
                                                    SyncFeesDetail(model.getId(), headerId, model.getFdmList());

                                                    downloadedCount++;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                AppModel.getInstance().appendErrorLog(context, "While inserting FeesHeader Exception occurred: " + e.getLocalizedMessage());
                                            }
                                        } else {
                                            int i = (int) FeesCollection.getInstance(context).updateDownloadedFeesHeader(model);
                                            if (i > 0) {
                                                updateFeeCount++;
                                                int headerId = FeesCollection.getInstance(context).getFeesHeaderId(model.getId());
                                                if (headerId > 0)
                                                    SyncFeesDetail(model.getId(), headerId, model.getFdmList());

                                                downloadedCount++;
                                            } else {
                                                System.out.println("FeesHeader ---> " + model.print());
                                                AppModel.getInstance().appendErrorLog(context, "While updating FeesHeader couldn't update record: " + model.print());
                                            }
                                        }

                                        //Update sync progress
                                        syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                                    }
                                }
//                            }
//                            else {
//                                //records are empty thats why inserting all records as bulk
//                                try {
//                                    FeesCollection.getInstance(context).addDownloadedFeesHeader(fhrm,schoolID);
//                                } catch (Exception e) {
//                                    areAllServicesSuccessful = false;
//                                    AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Fees Header " + e.getMessage());
//                                    e.printStackTrace();
//                                    break;
//                                }
//                            }
                            } else {
                                feeHeaderDownloadedCount = 0;
                            }

                        } else {
                            isSyncSuccessfull = false;
                            isFinanceSyncSuccessful = false;
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Fees Header Sync Called got Response code :" + response.code() + " Message " + response.message() + "\n");
                            break;
                        }
                    } catch (IOException e) {
                        areAllServicesSuccessful = false;
                        isFinanceSyncSuccessful = false;
                        failureCountForAllModules++;
                        AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Fees Header " + e.getMessage());
                        break;
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        isFinanceSyncSuccessful = false;
                        failureCountForAllModules++;
                        AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while syncing Fees Header " + e.getMessage());
                        e.printStackTrace();
                        break;
                    }
                    System.out.println("Fees_detail_count ---> " + feeDetailCount);
                    AppModel.getInstance().appendLog(context, "Fees Detail Count " + feeDetailCount);

                }


                appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                        appSyncStatusModel.getEndedOn()));
                SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);


            } catch (Exception e) {
                e.printStackTrace();
                AppModel.getInstance().appendErrorLog(context, "Error in get fee header method: " + e.getMessage());
            }
            Log.i("feecount", ":---->>>>" + count + " feeUpdateCount:" + updateFeeCount + " school id:---->>>" + schoolID);

//        SyncProgressActivity.model.isStudentProgressEnable().setValue(false);
        }

        syncingLogsTime(startTime, schoolID, "syncFeesHeader", false);
    }

    public void SyncFeesDetail(int serverHeaderID, int deviceHeaderId, ArrayList<FeesDetailUploadModel> fdmList) {
        feeDetailCount += fdmList.size();
        try {
            Log.d("Fees_detail_count", fdmList.size() + "");
            for (FeesDetailUploadModel fdm : fdmList) {
                if (fdm.getFeeHeader_id() == serverHeaderID) {

                    if (!FeesCollection.getInstance(context).FindFeesDetailRecord(deviceHeaderId, fdm.getFeeType_id()))
                        FeesCollection.getInstance(context).insertDownloadedFeesDetails(deviceHeaderId, fdm);
                    else {
                        FeesCollection.getInstance(context).updateDownloadedFeesDetails(deviceHeaderId, fdm);
//                        System.out.println("FeesHeader ---> " + fdm.print());
//                        SurveyAppModel.getInstance().appendErrorLog(context, "Detail Already exists: " + fdm.print());
                    }
                }
            }
        } catch (Exception e) {
            AppModel.getInstance().appendLog(context, "In SyncFeesDetail Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
        }
    }


    public void syncCashReceipts(int schoolYearId, int lastServerId) {
        AppModel.getInstance().appendLog(context, "\nSyncing Cash Receipts");
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        Call<ArrayList<SyncCashReceiptsModel>> call = apiInterface.getFeesReceipts(schoolYearId, lastServerId, token);
        call.enqueue(new Callback<ArrayList<SyncCashReceiptsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SyncCashReceiptsModel>> call, Response<ArrayList<SyncCashReceiptsModel>> response) {
                if (response.isSuccessful()) {
                    AppModel.getInstance().appendLog(context, "Cash Receipts Sync Called got Response code :" + response.code() + "\n");
                    ArrayList<SyncCashReceiptsModel> models = response.body();

                    for (SyncCashReceiptsModel model : models) {

                        try {
                            AppReceipt.getInstance(context).insertDownloadedFeesReceipt(changeFeesReceiptModel(model));
                        } catch (Exception e) {
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Exception is thrown while inserting in Cash Receipts Sync \n");
                        }
                    }

                } else {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "Cash Receipts Sync Called got Response code :" + response.code() + " Message " + response.message() + "\n");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SyncCashReceiptsModel>> call, Throwable t) {
                areAllServicesSuccessful = false;
            }
        });
    }


    private FeesReceiptModel changeFeesReceiptModel(SyncCashReceiptsModel model) {

        FeesReceiptModel feesModel =
                new FeesReceiptModel();

        String createdOn = AppModel.getInstance().convertDatetoFormat(model.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");
        String uploadedOn = AppModel.getInstance().convertDatetoFormat(model.getUploadedOn(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm:ss a");
        String downloadedOn = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");
        feesModel.setUploadedOn(uploadedOn);
        feesModel.setCreatedOn(createdOn);
        feesModel.setDownloadedOn(downloadedOn);
        feesModel.setCreatedBy(model.getCreatedBy());
        feesModel.setAdmissionFees(model.getFees_admission() + "");
        feesModel.setCopyFees(model.getFees_copies() + "");
        feesModel.setBookFees(model.getFees_books() + "");
        feesModel.setTutionFees(model.getFees_tution() + "");
        feesModel.setOthersFees(model.getFees_others() + "");
        feesModel.setUniformFees(model.getFees_uniform() + "");
        feesModel.setExamFees(model.getFees_exam() + "");
        if (model.getCashDepositId() > 0)
            feesModel.setCashDepositId(model.getCashDepositId() + "");

        feesModel.setSys_id(model.getId());
        feesModel.setSchoolYearId(model.getSchoolYearId());
        feesModel.setDeviceId(model.getDevice_id() + "");
        feesModel.setSchoolClassId(model.getSchoolClassId());
        feesModel.setStudentId(model.getStudentId());
        try {
            feesModel.setReceiptNumber(Integer.valueOf(model.getReceiptNo()));
        } catch (Exception e) {
            feesModel.setReceiptNumber(-1);
            e.printStackTrace();
        }

        feesModel.setCorrectionType(model.getIsCorrection() ? "1" : "0");

        return feesModel;


    }

    public void DownloadStudentPictures(int schoolId) {

        try {
//        ArrayList<StudentModel> AllStudents = DatabaseHelper.getInstance(context).getAllStudentsList(SurveyAppModel.getInstance().getSelectedSchool(context));
            ArrayList<StudentModel> AllStudents = DatabaseHelper.getInstance(context).getAllStudentsList(schoolId, true);
            String path = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/";

            if (AllStudents != null && AllStudents.size() > 0) {

                long startTime = System.currentTimeMillis();
                syncingLogsTime(startTime, schoolId, "DownloadStudentPictures", true);

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(0,
                        "Student Image", AppConstants.IMAGE_MODULE, "Downloading", schoolId);


                int[] downloadedCount = {0};
                int totalSize = 0;
                AppSyncStatusModel appSyncStatusModel = null;

                for (StudentModel student : AllStudents) {
                    if (student.getPictureName() != null) {
                        final File picture = new File(path + student.getPictureName());
                        if (!picture.exists()) {
                            totalSize++;
                            syncDownloadUploadModel.setTotalFileSize(totalSize);
                            String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + student.getPictureName();


                            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                                appSyncStatusModel = new AppSyncStatusModel();
                                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                                appSyncStatusModel.setSubModule("Student Images");
                            }

                            AppSyncStatusModel finalAppSyncStatusModel = appSyncStatusModel;
                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    Log.d("imageDownloadError", error.getMessage());

                                    //Update sync progress
                                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount[0], false);
                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    downloadedCount[0]++;

                                    if (!AppConstants.isAppSyncTableFirstTime && finalAppSyncStatusModel != null) { //For AppSyncStatus
                                        finalAppSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                                        finalAppSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(finalAppSyncStatusModel.getStartedOn(),
                                                finalAppSyncStatusModel.getEndedOn()));
                                        finalAppSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(picture.length(), false));
                                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(finalAppSyncStatusModel);
                                        AppConstants.isAppSyncTableFirstTime = true;
                                    }

                                    //Update sync progress
                                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount[0], false);

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

                }

                syncingLogsTime(startTime, schoolId, "DownloadStudentPictures", false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SyncStudents(ArrayList<StudentModel> smList, int schoolId) {
        if (smList == null || smList.size() == 0)
            return;

        long startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncStudents", true);

        AppModel.getInstance().appendLog(context, "Total Students:" + smList.size());

        try {
            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(smList.size(),
                    "Student", AppConstants.STUDENT_MODULE, "Downloading", schoolId);

            DatabaseHelper.getInstance(context).addStudent(smList, schoolId, syncDownloadUploadModel);

        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendErrorLog(context, "In SyncStudents Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

        long count = smList.stream().filter(obj -> "F".equals(obj.getStudent_promotionStatus())).count();
        if(count > 0) {
            Intent intent = new Intent(context, StudentProfileSearchActivity.class);
            AppModel.getInstance().fragmentTag = context.getString(R.string.student_gr_register);

            String title = "Validation Failed for " + count + " students";
            String msg = "Please update the details of students with failed status.";
            if(count == 1) {
                title = "Validation Failed for only one student";
                msg = "Please update the details of the student with failed status.";
            }
            AppModel.getInstance().showTCTNotification(context, title, msg, AppConstants.studentInfoReviewFailed, intent, AppConstants.CHANNEL_ID_REVIEW_FAILED_Notification);
        }

        syncingLogsTime(startTime, schoolId, "SyncStudents", false);
    }

    public void SyncEnrollment(ArrayList<EnrollmentModel> emList, ArrayList<EnrollmentImageModel> eimList, int schoolId) {
        long startTime = 0;

        SyncDownloadUploadModel syncDownloadUploadModel = null;
        if (emList != null && emList.size() > 0) {

            startTime = System.currentTimeMillis();
            syncingLogsTime(startTime, schoolId, "SyncEnrollment", true);

            syncDownloadUploadModel = new SyncDownloadUploadModel(emList.size(),
                    "Enrollment", AppConstants.STUDENT_MODULE, "Downloading", schoolId);
        }

        int downloadedCount = 0;
        for (EnrollmentModel em : emList) {
            try {
                EnrollmentModel rec = DatabaseHelper.getInstance(context).FindEnrollmentRecord(em.getID());
                if (rec == null) {
                    int headerId = (int) DatabaseHelper.getInstance(context).insertDownloadedEnrollment(em);
                    SyncEnrollmentImage(em.getID(), headerId, eimList);

                    if (headerId > 0) {
                        downloadedCount++;
                    }

                } else {
                    //Update enrollment header with review_status fields
                    if (rec.getENROLLMENT_UPLOADED_ON() != null && rec.getENROLLMENT_UPLOADED_ON().length() > 0) {
                        long i = DatabaseHelper.getInstance(context).updateDownloadEnrollment(em, em.getID());
                        SyncEnrollmentImage(em.getID(), rec.getDeviceId(), eimList);

                        if (i > 0) {
                            downloadedCount++;
                        }

                    } else {
                        Log.d("Enrollment_sync", "");
                    }
                }
            } catch (Exception e) {
                AppModel.getInstance().appendLog(context, "In SyncEnrollment Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            } finally {
                flushApprovedEnrollments();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

        }
        flushApprovedEnrollments();

        if (emList != null && emList.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncEnrollment", false);
        }
    }

    public void flushApprovedEnrollments() {
        long i = DatabaseHelper.getInstance(context).deleteApprovedRecordsEnrollment();
        if (i > -1) {
            AppModel.getInstance().appendLog(context, "Deleted enrollments affected rows" + i);
        } else {
            AppModel.getInstance().appendLog(context, "Couldn't delete enrollments");
        }

    }

    public void SyncEnrollmentImage(int eimId, int headerId, ArrayList<EnrollmentImageModel> eimList) {

        AppModel.getInstance().appendLog(context, "\nIn SyncEnrollmentImage Method");
        //headerId = e.device_id or (if not found) new headerid
        for (EnrollmentImageModel eim : eimList) {
            try {
                if (eim.getEnrollment_id() == eimId) {
                    if (!DatabaseHelper.getInstance(context)
                            .FindEnrollmentImageRecord(headerId, eim.getFiletype()))
                        DatabaseHelper.getInstance(context).insertDownloadedEnrollmentImage(eim, headerId);
                    else {
                        //Update
                        if (!DatabaseHelper.getInstance(context).IfEnrollmentNotUploaded(headerId))
                            DatabaseHelper.getInstance(context).updateDownloadedEnrollmentImage(eim, headerId);
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncEnrollmentImage Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void SyncAttendance(ArrayList<AttendanceModel> amList, ArrayList<AttendanceStudentModel> asmList, int schoolId) {

        long startTime = 0;

        SyncDownloadUploadModel syncDownloadUploadModel = null;
        if (amList != null && amList.size() > 0) {
            startTime = System.currentTimeMillis();
            syncingLogsTime(startTime, schoolId, "SyncEnrollment", true);
            syncDownloadUploadModel = new SyncDownloadUploadModel(amList.size(),
                    "Attendance", AppConstants.STUDENT_MODULE, "Downloading", schoolId);
        }

        int downloadedCount = 0;
        for (AttendanceModel am : amList) {
            try {
                AttendanceModel rec = DatabaseHelper.getInstance(context).FindAttendanceRecord(am.getId());
                if (rec == null) {
                    int headerId = (int) DatabaseHelper.getInstance(context).addDownloadedAttendance(am);
                    SyncAttendanceStudent(am.getId(), headerId, asmList);

                    if (headerId > 0) {
                        downloadedCount++;
                    }

                } else {
                    if (rec.getUploadedOn() != null && rec.getUploadedOn().length() > 0) {
                        SyncAttendanceStudent(am.getId(), rec.getDevice_id(), asmList);
                        long i = DatabaseHelper.getInstance(context).updateAttendance(am);

                        if (i > 0) {
                            downloadedCount++;
                        }

                    } else {
                        Log.d("sync_attendance", "");
                    }
                }
            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "In SyncAttendance Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
                areAllServicesSuccessful = false;
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);

            if (amList != null && amList.size() > 0) {
                syncingLogsTime(startTime, schoolId, "SyncEnrollment", false);
            }
        }

    }

    public void syncScholarshipCategory(List<ScholarshipCategoryModel> scms, int schoolId) {

        long startTime = 0;

        Scholarship_Category SC = Scholarship_Category.getInstance(context);

        if (scms != null && scms.size() > 0) {

            startTime = System.currentTimeMillis();
            syncingLogsTime(startTime, schoolId, "syncScholarshipCategory", true);
            AppModel.getInstance().appendErrorLog(context, "ScholarshipCategory count:" + scms.size() + " school id:" + schoolId);

            AppModel.getInstance().appendLog(context, "ScholarshipCategory count:" + scms.size() + " school id:" + schoolId);

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(scms.size(),
                    "Scholarship Category", AppConstants.SCHOOL_MODULE, "Downloading", schoolId);

            int downloadedCount = 0;
            for (ScholarshipCategoryModel scm : scms) {
                scm.setSchool_ID(schoolId);
                if (!DatabaseHelper.getInstance(context).FindRecord(
                        Scholarship_Category.SCHOLARSHIP_CAT,
                        Scholarship_Category.ID,
                        scm.getScholarship_category_id())) {
                    long i = SC.insert_Scholarship_Categories(scm);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    long i = SC.update_Scholarship_Categories(scm);
                    if (i > 0) {
                        downloadedCount++;
                    }
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }

            syncingLogsTime(startTime, schoolId, "syncScholarshipCategory", false);

        } else {
            AppModel.getInstance().appendErrorLog(context, "No ScholarshipCategory record found");
        }

    }

    public void SyncAttendanceStudent(int amId, int headerId, ArrayList<AttendanceStudentModel> asmList) {

        AppModel.getInstance().appendLog(context, "\nIn SyncAttendanceStudent Method");
        try {
            DatabaseHelper.getInstance(context).addDownloadedAttendanceStudent(asmList, headerId, amId);
        } catch (Exception e) {
            AppModel.getInstance().appendLog(context, "In SyncAttendanceStudent Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
        }
    }

    public void SyncPromotion(ArrayList<PromotionDBModel> pmList, ArrayList<PromotionStudentDBModel> psmList, int schoolId) {

        long startTime = 0;


        SyncDownloadUploadModel syncDownloadUploadModel = null;

        if (pmList != null && pmList.size() > 0) {

            startTime = System.currentTimeMillis();
            syncingLogsTime(startTime, schoolId, "SyncPromotion", true);

            syncDownloadUploadModel = new SyncDownloadUploadModel(pmList.size(),
                    "Promotion", AppConstants.STUDENT_MODULE, "Downloading", schoolId);
        }

        int downloadedCount = 0;
        for (PromotionDBModel pm : pmList) {
            try {
                int headerId = pm.getDevice_id();
                if (!DatabaseHelper.getInstance(context).FindPromotionRecord(headerId)) {
                    headerId = (int) DatabaseHelper.getInstance(context).insertDownloadedPromotion(pm);

                    if (headerId > 0) {
                        downloadedCount++;
                    }

                } else {
                    if (!DatabaseHelper.getInstance(context).IfPromotionNotUploaded(pm.getDevice_id())) {
                        long i = DatabaseHelper.getInstance(context).updateDownloadedPromotion(pm);

                        if (i > 0) {
                            downloadedCount++;
                        }

                    }
                }
                SyncPromotionStudent(pm.getId(), headerId, psmList);
            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "In SyncPromotion Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
                areAllServicesSuccessful = false;
            }
            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }

        //This is causing hang in device so dont use this
//        DatabaseHelper.getInstance(context).insertUpdateBulkDownloadedPromotion(pmList,psmList,syncDownloadUploadModel);

        if (pmList != null && pmList.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncPromotion", false);
        }
    }

    public void SyncPromotionStudent(int pmId, int headerId, ArrayList<PromotionStudentDBModel> psmList) {

        AppModel.getInstance().appendLog(context, "\nIn SyncPromotionStudent Method");
        for (PromotionStudentDBModel psm : psmList) {
            try {
                if (psm.getPromotion_id() == pmId) {
                    if (!DatabaseHelper.getInstance(context)
                            .FindPromotionStudentRecord(headerId, (int) psm.getStudent_id())) {
                        DatabaseHelper.getInstance(context).addPromotionStudent(psm, headerId);
                    } else {
                        if (!DatabaseHelper.getInstance(context).IfPromotionNotUploaded(headerId))
                            DatabaseHelper.getInstance(context).updatePromotionStudent(psm, headerId);
                    }
                }
            } catch (Exception e) {
                AppModel.getInstance().appendLog(context, "In SyncPromotionStudent Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
                areAllServicesSuccessful = false;
            }
        }

    }

    public void SyncWithdrawal(ArrayList<WithdrawalModel> wmList, int schoolId) {

        if (wmList == null || wmList.size() == 0)
            return;

        long startTime = 0;
        try {
            SyncDownloadUploadModel syncDownloadUploadModel = null;
            if (wmList != null && wmList.size() > 0) {

                startTime = System.currentTimeMillis();
                syncingLogsTime(startTime, schoolId, "SyncWithdrawal", true);

                syncDownloadUploadModel = new SyncDownloadUploadModel(wmList.size(),
                        "Withdrawal", AppConstants.STUDENT_MODULE, "Downloading", schoolId);
            }

            DatabaseHelper.getInstance(context).addWithdrawal(wmList, syncDownloadUploadModel);
        } catch (Exception e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
        }
        if (wmList != null && wmList.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncWithdrawal", false);
        }
    }

    public void SyncSchoolAudits(ArrayList<SchoolAuditModel> samList, int schoolId) {

        if (samList == null || samList.size() == 0)
            return;

        long startTime = 0;
        try {
            SyncDownloadUploadModel syncDownloadUploadModel = null;
            if (samList != null && samList.size() > 0) {
                startTime = System.currentTimeMillis();
                syncingLogsTime(startTime, schoolId, "SyncSchoolAudits", true);
                syncDownloadUploadModel = new SyncDownloadUploadModel(samList.size(),
                        "School Audits", AppConstants.STUDENT_MODULE, "Downloading", schoolId);
            }

            DatabaseHelper.getInstance(context).insertDownloadedSchoolAudit(samList, syncDownloadUploadModel);
        } catch (Exception e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
        }
        if (samList != null && samList.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncSchoolAudits", false);
        }

    }

    // Download Meta Data Methods
    public void SyncSchool(ArrayList<SchoolModel> smList) {
        try {


            for (SchoolModel sm : smList) {
                if (!DatabaseHelper.getInstance(context).FindRecord(
                        DatabaseHelper.getInstance(context).TABLE_SCHOOL,
                        DatabaseHelper.getInstance(context).KEY_ID,
                        sm.getId())) {
                    DatabaseHelper.getInstance(context).addSchool(sm);
                } else {
                    DatabaseHelper.getInstance(context).updateSchool(sm);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SyncClasses(final ArrayList<ClassModel> cmList) {
        new Thread(() -> {
            if (cmList != null && cmList.size() > 0) {

                String startDate, endDate;

                AppModel.getInstance().appendErrorLog(context, "Classes count:" + cmList.size());

                AppModel.getInstance().appendLog(context, "Classes count:" + cmList.size());


                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(cmList.size(),
                        "Classes", AppConstants.SCHOOL_MODULE, "Downloading", 0);
                int downloadedCount = 0;

                for (ClassModel cm : cmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                DatabaseHelper.getInstance(context).TABLE_CLASS,
                                DatabaseHelper.getInstance(context).KEY_ID,
                                cm.getClassId())) {
                            long i = DatabaseHelper.getInstance(context).addClass(cm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateClass(cm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncClasses error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Classes record found");
            }
        }).start();

    }

    public void SyncAttendanceSummary(ArrayList<AttendanceSummary> attendancePercentageModels, int schoolId) {

        long startTime = 0;

        try {
            AttendancePercentage.getInstance(context).delete(schoolId);
            if (attendancePercentageModels != null && attendancePercentageModels.size() > 0) {

                startTime = System.currentTimeMillis();
                syncingLogsTime(startTime, schoolId, "SyncAttendanceSummary", true);

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(attendancePercentageModels.size(),
                        "Attendance Summary", AppConstants.STUDENT_MODULE, "Downloading", schoolId);

                int downloadedCount = 0;
                for (AttendanceSummary sim : attendancePercentageModels) {
                    long i = AttendancePercentage.getInstance(context).insertAttendanceSummary(sim);

                    if (i > 0) {
                        downloadedCount++;
                    }

                    //Update sync progress
                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            }
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            e.printStackTrace();
        }

        if (attendancePercentageModels != null && attendancePercentageModels.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncAttendanceSummary", false);
        }
    }

    public void SyncSchoolSSRSummary(ArrayList<SessionInfoModel> sessionInfoModels, int schoolId) {

        long startTime = 0;

        try {
            SessionInfo.getInstance(context).delete(schoolId);
            if (sessionInfoModels != null && sessionInfoModels.size() > 0) {

                startTime = System.currentTimeMillis();
                syncingLogsTime(startTime, schoolId, "SyncAttendanceSummary", true);

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(sessionInfoModels.size(),
                        "School SSR Summary", AppConstants.STUDENT_MODULE, "Downloading", schoolId);

                int downloadedCount = 0;

                for (SessionInfoModel sim : sessionInfoModels) {
                    if (sim.getSessionInfo() != null && !sim.getSessionInfo().equals(""))
                        sim.setSchoolid(schoolId);
                    long i = SessionInfo.getInstance(context).insertSessionInfo(sim);

                    if (i > 0) {
                        downloadedCount++;
                    }

                    //Update sync progress
                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            }
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            e.printStackTrace();
        }

        if (sessionInfoModels != null && sessionInfoModels.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncAttendanceSummary", false);
        }
    }

    public void SyncCampus(final ArrayList<CampusModel> campusModels) {
        new Thread(() -> {
            DatabaseHelper.getInstance(context).insertOrUpdateCampusBulk(campusModels);
//            if (campusModels != null && campusModels.size() > 0) {
//
//                AppModel.getInstance().appendErrorLog(context, "Campus count:" + campusModels.size());
//
//                AppModel.getInstance().appendLog(context, "Campus count:" + campusModels.size());
//
//                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(campusModels.size(),
//                        "Campus", AppConstants.SCHOOL_MODULE, "Downloading", 0);
//
//                int downloadedCount = 0;
//
//                for (CampusModel scm : campusModels) {
//                    try {
//                        if (!DatabaseHelper.getInstance(context).FindRecord(
//                                DatabaseHelper.getInstance(context).TABLE_CAMPUS,
//                                DatabaseHelper.getInstance(context).CAMPUS_ID,
//                                scm.getId())) {
//                            long i = DatabaseHelper.getInstance(context).insertCampus(scm);
//
//                            if (i > 0) {
//                                downloadedCount++;
//                            }
//                        } else {
//                            long i = DatabaseHelper.getInstance(context).updateCampus(scm);
//
//                            if (i > 0) {
//                                downloadedCount++;
//                            }
//                        }
//                    } catch (Exception e) {
//                        areAllServicesSuccessful = false;
//                        e.printStackTrace();
//                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncCampus error:" + e.getMessage());
//                    }
//
//                    //Update sync progress
//
//                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
//                }
//            } else {
//                AppModel.getInstance().appendErrorLog(context, "No Campus record found");
//            }

        }).start();


    }

    public void SyncRegion(final ArrayList<RegionModel> scmList) {
        new Thread(() -> {
            if (scmList != null && scmList.size() > 0) {

                AppModel.getInstance().appendErrorLog(context, "Region count:" + scmList.size());

                AppModel.getInstance().appendLog(context, "Region count:" + scmList.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(scmList.size(),
                        "Region", AppConstants.SCHOOL_MODULE, "Downloading", 0);

                int downloadedCount = 0;

                for (RegionModel scm : scmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                DatabaseHelper.getInstance(context).TABLE_REGION,
                                DatabaseHelper.getInstance(context).REGION_ID,
                                scm.getId())) {
                            long i = DatabaseHelper.getInstance(context).insertRegion(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateRegion(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }

                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncRegion error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Region record found");
            }

        }).start();


    }

    public void SyncAcademicSessions(final ArrayList<AcademicSessionModel> asmList) {
        new Thread(() -> {
            if (asmList != null && asmList.size() > 0) {

                AppModel.getInstance().appendErrorLog(context, "Academic Session count:" + asmList.size());

                AppModel.getInstance().appendLog(context, "Academic Session count:" + asmList.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(asmList.size(),
                        "Academic Session", AppConstants.SCHOOL_MODULE, "Downloading", 0);

                int downloadedCount = 0;

                for (AcademicSessionModel asm : asmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                AcademicSession.ACADEMIC_SESSION_TABLE,
                                AcademicSession.SESSION_ID,
                                asm.getSession_id())) {
                            long i = DatabaseHelper.getInstance(context).insertAcademicSession(asm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateAcademicSession(asm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }

                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncAcademic Session error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Academic Session record found");
            }

        }).start();


    }

    public void SyncAreas(final ArrayList<AreaModel> scmList) {
        new Thread(() -> {
            if (scmList != null && scmList.size() > 0) {

                AppModel.getInstance().appendErrorLog(context, "Areas count:" + scmList.size());

                AppModel.getInstance().appendLog(context, "Areas count:" + scmList.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(scmList.size(),
                        "Areas", AppConstants.SCHOOL_MODULE, "Downloading", 0);

                int downloadedCount = 0;

                for (AreaModel scm : scmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                DatabaseHelper.getInstance(context).TABLE_AREA,
                                DatabaseHelper.getInstance(context).AREA_ID,
                                scm.getId())) {
                            long i = DatabaseHelper.getInstance(context).insertArea(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateArea(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }

                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncAreas error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Areas record found");
            }

        }).start();


    }

    public void SyncLocation(final ArrayList<LocationModel> scmList) {
        new Thread(() -> {
            if (scmList != null && scmList.size() > 0) {

                AppModel.getInstance().appendErrorLog(context, "Location count:" + scmList.size());

                AppModel.getInstance().appendLog(context, "Location count:" + scmList.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(scmList.size(),
                        "Location", AppConstants.SCHOOL_MODULE, "Downloading", 0);

                int downloadedCount = 0;

                for (LocationModel scm : scmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                DatabaseHelper.getInstance(context).TABLE_LOCATION,
                                DatabaseHelper.getInstance(context).LOCATION_ID,
                                scm.getId())) {
                            long i = DatabaseHelper.getInstance(context).insertLocation(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateLocation(scm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }

                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncLocation error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Location record found");
            }

        }).start();


    }

    public void SyncSchoolClasses(ArrayList<SchoolClassesModel> scmList, int schoolId) {

        long startTime = 0;


        if (scmList != null && scmList.size() > 0) {

            startTime = System.currentTimeMillis();
            syncingLogsTime(startTime, schoolId, "SyncSchoolClasses", true);

            AppModel.getInstance().appendErrorLog(context, "SchoolClasses count:" + scmList.size() + " school id:" + schoolId);

            AppModel.getInstance().appendLog(context, "SchoolClasses count:" + scmList.size() + " school id:" + schoolId);

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(scmList.size(),
                    "School Classes", AppConstants.SCHOOL_MODULE, "Downloading", schoolId);

            int downloadedCount = 0;
            for (SchoolClassesModel scm : scmList) {
                try {
                    if (!DatabaseHelper.getInstance(context).FindRecord(
                            DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS,
                            DatabaseHelper.getInstance(context).KEY_ID,
                            scm.getId())) {
                        long i = DatabaseHelper.getInstance(context).addSchoolClass(scm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    } else {
                        long i = DatabaseHelper.getInstance(context).updateSchoolClass(scm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }

                } catch (Exception e) {
                    areAllServicesSuccessful = false;
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(context, "Exception in SyncSchoolClasses error:" + e.getMessage());
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
            }
        } else {
            AppModel.getInstance().appendErrorLog(context, "No SchoolClasses record found");
        }

        if (scmList != null && scmList.size() > 0) {
            syncingLogsTime(startTime, schoolId, "SyncSchoolClasses", false);
        }
    }

    public void SyncSections(final ArrayList<SectionModel> smList) {
        new Thread(() -> {
            if (smList != null && smList.size() > 0) {

                AppModel.getInstance().appendErrorLog(context, "Sections count:" + smList.size());

                AppModel.getInstance().appendLog(context, "Sections count:" + smList.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(smList.size(),
                        "Sections", AppConstants.SCHOOL_MODULE, "Downloading", 0);

                int downloadedCount = 0;

                for (SectionModel sm : smList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                DatabaseHelper.getInstance(context).TABLE_SECTION,
                                DatabaseHelper.getInstance(context).KEY_ID,
                                sm.getSectionId())) {
                            long i = DatabaseHelper.getInstance(context).addSection(sm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateSection(sm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }

                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncSections error:" + e.getMessage());
                    }
                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Sections record found");
            }
        }).start();


    }

    public void SyncWithdrawalReasons(final ArrayList<WithdrawalReasonModel> wrmList) {
        new Thread(() -> {
            if (wrmList != null && wrmList.size() > 0) {

                AppModel.getInstance().appendErrorLog(context, "WithdrawalReasons count:" + wrmList.size());

                AppModel.getInstance().appendLog(context, "WithdrawalReasons count:" + wrmList.size());

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(wrmList.size(),
                        "Withdrawal Reasons", AppConstants.STUDENT_MODULE, "Downloading", 0);

                int downloadedCount = 0;

                for (WithdrawalReasonModel wrm : wrmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL_REASON,
                                DatabaseHelper.getInstance(context).KEY_ID,
                                wrm.getId())) {
                            long i = DatabaseHelper.getInstance(context).addWithdrawalReason(wrm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = DatabaseHelper.getInstance(context).updateWithdrawalReason(wrm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }

                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncWithdrawalReasons error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No WithdrawalReason record found");
            }

        }).start();
    }

    // Data upload Methods
//    public void UploadStudents(int schoolId) {
//        error_count = 0;
//        SurveyAppModel.getInstance().appendLog(context, "\nIn UploadStudents Method" + "\n");
//        try {
//
//            StudentModel.getInstance().setStudentsList(
//                    DatabaseHelper.getInstance(context).getAllStudentsForUpload(
//                            schoolId));
//            if (StudentModel.getInstance().getStudentsList().size() == 0) {
//                SurveyAppModel.getInstance().appendLog(context, "No Student record found for upload" + "\n");
//            }
//            for (StudentModel sm : StudentModel.getInstance().getStudentsList()) {
//                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//                String token = SurveyAppModel.getInstance().getToken(context);
//                token = "Bearer " + token;
//                int orignalStudentID = sm.getId();
//                final int studentId = sm.getServerId();
//                sm.setId(studentId);
//                SurveyAppModel.getInstance().appendLog(context, "Uploading Student with id:" + sm.getId());
//                Call<ResponseBody> call = apiInterface.uploadStudents(sm, token);
//                Response<ResponseBody> response = call.execute();
//
//                SurveyAppModel.getInstance().appendLog(context, "Got responseCode:" + response.code());
//                if (response.isSuccessful()) {
//                    try {
//                        String resp = response.body().string();
//
//                        resp = resp.length() != 0 ? resp.replace("\"", "") : resp;
//                        error_count = 0;
//                        isSyncSuccessfull = true;
//                        SurveyAppModel.getInstance().appendLog(context, "Student uploaded with id:" + sm.getId());
//                        ContentValues values = new ContentValues();
//                        if (resp != null && resp.equals("")) {
//                            values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, SurveyAppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//
//                        } else if (resp != null) {
//                            values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, SurveyAppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//                            values.put(DatabaseHelper.getInstance(context).KEY_ID, resp);
//                            values.put(DatabaseHelper.getInstance(context).STUDENT_SERVER_ID, resp);
//                        }
//
//                        DatabaseHelper.getInstance(context).updateStudentByColumns(values, orignalStudentID);
//                    } catch (Exception e) {
//                        areAllServicesSuccessful = false;
//                        SurveyAppModel.getInstance().appendErrorLog(context, "In UploadStudents Method. exception occurs: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                } else {
//
//                    SurveyAppModel.getInstance().appendErrorLog(context, "In UploadStudents responseCode: " + response.code() + " message: " + response.message());
//                    insertFailedStudent(response.code(),response.message(),studentId,"Upload Students");
//                    if (response.code() == 502) {
//                        getSingleStudent(sm.getId());
//                        continue;
//                    }
//
//                    error_count++;
//                    failureCount++;
//                    areAllServicesSuccessful = false;
////                    if (error_count >= 3) {
////                        areAllServicesSuccessful = false;
////                        isSyncSuccessfull = false;
////                        break;
////                    }
//                }
//            }
//        } catch (Exception e) {
//            failureCount++;
//            areAllServicesSuccessful = false;
//            isSyncSuccessfull = false;
//            SurveyAppModel.getInstance().appendErrorLog(context, "In UploadStudents Method. exception occurs: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void UploadStudents(int schoolId) {
        error_count = 0;
        AppModel.getInstance().appendLog(context, "\nIn UploadStudents Method" + "\n");
        try {

            StudentModel.getInstance().setStudentsList(
                    DatabaseHelper.getInstance(context).getAllStudentsForUpload(
                            schoolId));
            if (StudentModel.getInstance().getStudentsList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Student record found for upload" + "\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(StudentModel.getInstance().getStudentsList().size(),
                    "Student", AppConstants.STUDENT_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;

            int uploadedCount = 0;
            long appSyncStatusTotalUploadedSize = 0;

            for (StudentModel sm : StudentModel.getInstance().getStudentsList()) {
                try {
                    ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                    String token = AppModel.getInstance().getToken(context);
                    token = "Bearer " + token;
                    int orignalStudentID = sm.getId();
                    final int studentId = sm.getServerId();
                    sm.setId(studentId);
                    AppModel.getInstance().appendLog(context, "Uploading Student with id:" + sm.getId());

                    if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                        appSyncStatusModel = new AppSyncStatusModel();
                        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                        appSyncStatusModel.setSubModule("Edit Student");
                    }

                    Call<StudentUploadResponseModel> call = apiInterface.uploadStudents(sm, token);
                    Response<StudentUploadResponseModel> response = call.execute();

                    AppModel.getInstance().appendLog(context, "Got responseCode:" + response.code());
                    if (response.isSuccessful()) {
                        try {
                            String resp = response.body().getStudentId();

                            if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                                AppConstants.isAppSyncTableFirstTime = true;
                            }
                            appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                            resp = resp != null && resp.length() != 0 ? resp.replace("\"", "") : resp;
                            error_count = 0;
                            isSyncSuccessfull = true;
                            AppModel.getInstance().appendLog(context, "Student uploaded with id:" + sm.getId());
                            ContentValues values = new ContentValues();
                            if (resp != null && resp.equals("")) {
                                values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                            } else if (resp != null) {
                                values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                values.put(DatabaseHelper.getInstance(context).KEY_ID, resp);
                                values.put(DatabaseHelper.getInstance(context).STUDENT_SERVER_ID, resp);
                            }

                            long i = DatabaseHelper.getInstance(context).updateStudentByColumns(values, orignalStudentID);

                            if (i > 0) {
                                uploadedCount++;
                            }

//                            if (sm.getPictureName() != null && sm.getPictureUploadedOn() == null){
//                                uploadStudentImages();
//                            }
                        } catch (Exception e) {
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "In UploadStudents Method. exception occurs: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {

                        StudentUploadResponseModel surm =
                                new Gson().fromJson(response.errorBody().string(), StudentUploadResponseModel.class);

                        if (response.code() == 502) {

                            AppModel.getInstance().appendErrorLog(context, "In UploadStudents responseCode: " + response.code() + " message: " + surm.getErrorMessage());
                            insertFailedStudent(response.code(), surm.getErrorMessage(), studentId, "Upload Students");

                            StudentModel mod = surm.getStudent();
                            mod.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                            DatabaseHelper.getInstance(context).updateFailedStudent(mod);
                            continue;
                        }

                        AppModel.getInstance().appendErrorLog(context, "In UploadStudents responseCode: " + response.code() + " message: " + response.message());
                        insertFailedStudent(response.code(), response.message(), studentId, "Upload Students");


                        error_count++;
                        failureCount++;
                        areAllServicesSuccessful = false;
//                    if (error_count >= 3) {
//                        areAllServicesSuccessful = false;
//                        isSyncSuccessfull = false;
//                        break;
//                    }
                    }
                } catch (Exception e) {
                    failureCountForAllModules++;
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            failureCount++;
            areAllServicesSuccessful = false;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "In UploadStudents Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void getSingleStudent(int studentId) {
        try {
            AppModel.getInstance().appendLog(context, "\nGetting Single Student Record");
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<StudentModel> call = apiInterface.getSingleStudent(studentId, token);
            Response<StudentModel> response = call.execute();
            if (response.isSuccessful()) {
                AppModel.getInstance().appendLog(context, "Single Student api Called got Response code :" + response.code() + "\n");

                if (response.body() != null) {
                    StudentModel sm = response.body();
                    sm.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    DatabaseHelper.getInstance(context).updateStudent(sm);
                }
            } else {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "Getting single student Called got Response code :" + response.code() + " Message " + response.message() + "\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In getSingleStudentMethod Exception Occured: " + e.getMessage());
        }
    }

    public void UploadEnrollments() {
        try {
            error_count = 0;
            AppModel.getInstance().appendLog(context, "In UploadEnrollments Method");
            ArrayList<EnrollmentUploadModel> uemList = new ArrayList<>();
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            EnrollmentModel enrollmentModel = new EnrollmentModel();
//            enrollmentModel.setEmList(DatabaseHelper.getInstance(context).getAllEnrollments(SurveyAppModel.getInstance().getSelectedSchool(context)));
            enrollmentModel.setEmList(DatabaseHelper.getInstance(context).getAllEnrollments());
            for (EnrollmentModel em : enrollmentModel.getEmList()) {
                try {
                    ArrayList<EnrollmentImageUploadModel> eim = DatabaseHelper.getInstance(context).getAllEnrollmentImagesForUpload(em.getID());
                    int school_class_id = DatabaseHelper.getInstance(context).getStudentFromStudentId(em.getStudentId());
                    if (school_class_id == 0)
                        school_class_id = em.getClass_section_id();
                    uemList.add(new EnrollmentUploadModel(em.getID(), em.getServer_id(), em.getENROLLMENT_GR_NO(),
                            em.getENROLLMENT_SCHOOL_ID(), em.getENROLLMENT_CREATED_BY(),
                            em.getENROLLMENT_CREATED_ON(), androidId, eim, em.getStudentName(), 0,
                            school_class_id, em.getStudentId(), school_class_id, em.getGender(), (int) em.getMonthly_fee(), em.getModified_by(), em.getModified_on()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (uemList.size() == 0) {
                AppModel.getInstance().appendLog(context, "No enrollments record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(uemList.size(),
                    "Enrollment", AppConstants.STUDENT_MODULE, "Uploading", 0);

            int uploadedCount = 0;
            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                appSyncStatusModel.setSubModule("Student Enrollment");
            }

            for (EnrollmentUploadModel eum : uemList) {
                try {
                    ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                    String token = AppModel.getInstance().getToken(context);
                    token = "Bearer " + token;

//                TODO hardcoded status pending is set
                    eum.setStatus("P");
                    Call<ResponseBody> call = apiInterface.uploadEnrollments(eum, token);
                    final int Hid = eum.getId();
                    AppModel.getInstance().appendLog(context, "In UploadEnrollments Method. Uploading Enrollment with id:" + Hid);
                    Response<ResponseBody> response = call.execute();
                    AppModel.getInstance().appendLog(context, "In UploadEnrollments onResponse Method.responseCode:" + response.code());
                    if (response.isSuccessful()) {
                        try {
                            error_count = 0;
                            isSyncSuccessfull = true;

                            if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                                AppConstants.isAppSyncTableFirstTime = true;
                            }

                            appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                            String Sresponse = AppModel.getInstance().getResponseString(context, response);
                            int server_id = Integer.valueOf(Sresponse);
                            if (server_id > 0) {

                                ContentValues values = new ContentValues();
                                values.put(DatabaseHelper.getInstance(context).ENROLLMENT_UPLOADED_ON, AppModel.getInstance().getDate());
                                values.put(DatabaseHelper.getInstance(context).ENROLLMENT_SERVER_ID, server_id);
                                long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                        DatabaseHelper.getInstance(context).TABLE_ENROLLMENT, values, Hid);
                                if (id > 0) {
                                    AppModel.getInstance().appendLog(context, "In UploadEnrollments Method.Image Uploaded. Enrollment Uploaded. id:" + id + " and uploadedOn:" + AppModel.getInstance().getDate());

                                    uploadedCount++;
                                }
                            } else {
                                AppModel.getInstance().appendErrorLog(context, "In UploadEnrollments Method.Image Uploaded. Enrollment not uploaded. id:" + enrollmentModel.getENROLLMENT_GR_NO());

                            }


                        } catch (Exception e) {
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendLog(context, "In UploadEnrollments Method. exception occurs: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {


                        if (response.code() == 502) {


                            JSONObject res = !response.errorBody().string().equals("null") &&
                                    !response.errorBody().string().equals("") ?
                                    new JSONObject(response.errorBody().string()) : new JSONObject();

                            String msg = res.has("Message") ?
                                    res.getString("Message") : "";

                            AppModel.getInstance().appendErrorLog(context, "In UploadEnrollments onFailure Method. Message: " +
                                    msg);

                            insertFailedStudent(response.code(), msg,
                                    (enrollmentModel.getStudentId() == null || enrollmentModel.getStudentId().isEmpty()) ? 0 : Integer.parseInt(enrollmentModel.getStudentId()),
                                    "Enrollment", eum.getStudent_name(), eum.getGr_no() + "", eum.getClass_section_id() + "");

                            DatabaseHelper.getInstance(context).deleteEnrollments(eum.getId());

                            continue;

                        }


                        failureCount++;
                        error_count++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendLog(context, "In UploadEnrollments onFailure Method");
                        if (error_count >= 3) {
                            isSyncSuccessfull = false;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            failureCount++;
            areAllServicesSuccessful = false;
            isSyncSuccessfull = false;
            AppModel.getInstance().appendLog(context, "In UploadEnrollments Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void uploadUserSchoolsEnrollmentImages(SyncAdapter syncAdapter) {
//        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();

//        for (SchoolModel model : schoolList) {
        UploadEnrollmentsImage(syncAdapter, 0);
//        }
    }

    public void UploadEnrollmentsImage(SyncAdapter syncAdapter, int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method");
            EnrollmentImageModel enrollmentImageModel = new EnrollmentImageModel();
//            enrollmentImageModel.setEimList(
//                    DatabaseHelper.getInstance(context).getAllEnrollmentImage(SurveyAppModel.getInstance().getSelectedSchool(context)));
            enrollmentImageModel.setEimList(
                    DatabaseHelper.getInstance(context).getAllEnrollmentImage());
            if (enrollmentImageModel.getEimList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Records found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(enrollmentImageModel.getEimList().size(),
                    "Enrollments Image", AppConstants.IMAGE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            int uploadedCount = 0;
            long appSyncStatusTotalUploadedSize = 0;

            for (EnrollmentImageModel eim : enrollmentImageModel.getEimList()) {
                try {
//                    eim = enrollmentImageModel.getEimList().get(0);
                    if (eim.getFilename() == null)
                        continue;

                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method.Uploading Image:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                        appSyncStatusModel = new AppSyncStatusModel();
                        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                        appSyncStatusModel.setSubModule("Enrollment Image");
                    }


                    int responseCode = connectionClass.uploadFile(fdir + "/" + eim.getFilename(),
                            context.getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_ENROLLMENT_IMAGES + "?enrollment_id=" + eim.getId() + "&filetype=" + eim.getFiletype());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(fdir + "/" + eim.getFilename()).length();


                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");

                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).ENROLLMENT_IMAGE_FILENAME, name);
                        values.put(DatabaseHelper.getInstance(context).ENROLLMENT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDate());
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_ENROLLMENT_IMAGE, values, eim.getId());
                        if (id > 0) {
                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(fdir + "/" + eim.getFilename());
                            if (sourceFilename.exists()) {
                                if (eim.getFiletype().toLowerCase().equals("p")) {
                                    File updatedFileName = new File(fdir + "/" + name);
                                    if (!updatedFileName.exists()) {
                                        try {
                                            String rootPath = updatedFileName.getParent();
                                            String fileName = updatedFileName.getName();


                                            assert rootPath != null;
                                            File root = new File(rootPath);
                                            if (!root.exists()) {
                                                root.mkdirs();
                                            }

                                            //It will move the file to the path and renamed it
                                            boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                            if (isNameChanged) {
                                                AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
                                            }

                                            AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image path changed in local.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
                                } else {
                                    sourceFilename.delete();
                                }
                            }

                            uploadedCount++;
                        }
                    } else {
                        if (responseCode == 0) {
                            File image = new File(eim.getFilename());
                            if (!image.exists()) {
                                AppModel.getInstance().appendErrorLog(context, "Enrollment Image does not exists " + image.getName());
                                ContentValues values = new ContentValues();
                                values.put(DatabaseHelper.ENROLLMENT_REVIEW_STATUS, AppConstants.PROFILE_INCOMPLETE_KEY);
                                DatabaseHelper.getInstance(context).updateTableColumns(DatabaseHelper.TABLE_ENROLLMENT, values, (int) eim.getEnrollment_id());
                            }
                        } else {
                            failureCount++;
                            error_count++;
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                            if (error_count == 3) {
                                isSyncSuccessfull = false;
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

            IProcessComplete pc = (IProcessComplete) syncAdapter;
            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void uploadStudentImages(int schoolId) {
        UploadStudentImage(schoolId);
    }

    private void UploadStudentImage(int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In UploadStudentImage Method");
            StudentImageModel studentImageModel = new StudentImageModel();
            studentImageModel.setSimList(DatabaseHelper.getInstance(context).getStudentImageForUpload(schoolId));
            if (studentImageModel.getSimList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Record found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(studentImageModel.getSimList().size(),
                    "Student Image", AppConstants.IMAGE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;

            int uploadedCount = 0;
            long appSyncStatusTotalUploadedSize = 0;

            for (StudentImageModel sim : studentImageModel.getSimList()) {
                try {
                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadStudentImage Method.Uploading Image:" + sim.getFilename() + " with student id:" + sim.getStudent_id());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                        appSyncStatusModel = new AppSyncStatusModel();
                        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                        appSyncStatusModel.setSubModule("Edit Student Image");
                    }

                    int responseCode = connectionClass.uploadFile(fdir + "/" + sim.getFilename(),
                            context.getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_STUDENT_IMAGES + "?id=" + sim.getStudent_id());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadStudentImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(fdir + "/" + sim.getFilename()).length();


                        String name = HttpConnectionClass.responseJson;
                        name = name.replace('\\', '/');

                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).STUDENT_PICTURE_NAME, name);
                        values.put(DatabaseHelper.getInstance(context).STUDENT_PICTURE_UPLOADED_ON, AppModel.getInstance().getDateTime());
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_STUDENT, values, (int) sim.getStudent_id());
                        if (id > 0) {
                            error_count = 0;

                            //for progress update
                            uploadedCount++;

                            AppModel.getInstance().appendLog(context, "In UploadStudentImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(fdir + "/" + sim.getFilename());
                            File updatedFileName = new File(fdir + "/" + name);
                            if (!updatedFileName.exists()) {
                                try {
                                    String rootPath = updatedFileName.getParent();
                                    String fileName = updatedFileName.getName();


                                    assert rootPath != null;
                                    File root = new File(rootPath);
                                    if (!root.exists()) {
                                        root.mkdirs();
                                    }

                                    //It will move the file to the path and renamed it
                                    boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                    if (isNameChanged) {
                                        AppModel.getInstance().appendLog(context, "In UploadStudentImage Method. Image name changed in local.");
                                    }

                                    AppModel.getInstance().appendLog(context, "In UploadStudentImage Method. Image path changed in local.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
//                            File Filename = new File(sim.getFilename());
//                            if (Filename.exists()) {
//                                boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                if (isNameChanged)
//                                    AppModel.getInstance().appendLog(context, "In UploadStudentImage Method. Image name changed in local.");
//                            }

                        }
                    } else {
                        if (responseCode == 0) {
                            File image = new File(sim.getFilename());
                            if (!image.exists()) {
                                AppModel.getInstance().appendErrorLog(context, "StudentImage Image does not exists " + image.getName());
//                                ContentValues values = new ContentValues();
//                                values.put(DatabaseHelper.ENROLLMENT_REVIEW_STATUS, AppConstants.PROFILE_INCOMPLETE_KEY);
//                                DatabaseHelper.getInstance(context).updateTableColumns(DatabaseHelper.TABLE_ENROLLMENT, values, (int) eim.getEnrollment_id());
                            }
                        } else {
                            failureCount++;
                            error_count++;
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                            if (error_count == 3) {
                                isSyncSuccessfull = false;
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadStudentImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);


        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadStudentImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void UploadAttendance() {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "\nIn UploadAttendance Method");
            ArrayList<AttendanceUploadModel> utmList = new ArrayList<>();
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            AttendanceModel.getInstance().setAmList(DatabaseHelper.getInstance(context).getAllAttendanceForUpload());
            if (AttendanceModel.getInstance().getAmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No attendance record found for upload\n");
                return;
            }
            for (AttendanceModel am : AttendanceModel.getInstance().getAmList()) {
                ArrayList<AttendanceStudentUploadModel> asumList = DatabaseHelper.getInstance(context)
                        .getAllAttendanceStudentsForUpload(am.getId());
                utmList.add(new AttendanceUploadModel(am.getId(), am.getServer_id(), am.getSchoolId(),
                        am.getCreatedBy(), am.getForDate(), am.getCreatedOn(), androidId, asumList, am.getModified_on(), am.getModified_by()));
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(utmList.size(),
                    "Attendance", AppConstants.STUDENT_MODULE, "Uploading", 0);


            int uploadedCount = 0;
            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;


            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                appSyncStatusModel.setSubModule("Student Attendance");
            }

            for (AttendanceUploadModel uam : utmList) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                final Call<ResponseBody> cal1 = apiInterface.uploadAttendance(uam, token);
                final int hId = uam.getId();
                AppModel.getInstance().appendLog(context, "Uploading Attendance with id: " + hId + " For the date of " + uam.getFor_date());
                Response<ResponseBody> response = cal1.execute();
                AppModel.getInstance().appendLog(context, "Got responseCode: " + response.code() + " id: " + hId);
                if (response.isSuccessful()) {
                    try {
                        error_count = 0;
                        isSyncSuccessfull = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                        String Sresponse = AppModel.getInstance().getResponseString(context, response);
                        int server_id = Integer.valueOf(Sresponse);
                        if (server_id > 0) {
                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.getInstance(context).ATTENDANCE_UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("dd/MM/yy HH:mm:ss"));
                            values.put(DatabaseHelper.getInstance(context).ATTENDANCE_SERVER_ID, server_id);
                            long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                    DatabaseHelper.getInstance(context).TABLE_ATTENDANCE, values, hId);
                            if (id > 0) {
                                AppModel.getInstance().appendLog(context, "Attendance Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getDate());

                                uploadedCount++;
                            }
                        }
                    } catch (Exception e) {
                        AppModel.getInstance().appendErrorLog(context, "In UploadAttendance Method. exception occurs: " + e.getMessage());
                        e.printStackTrace();
                        areAllServicesSuccessful = false;
                    }
                } else {
                    error_count++;
                    failureCount++;
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In UploadAttendance responseCode " + response.code() + " message: " + response.message());
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                        break;
                    }
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            failureCount++;
            isSyncSuccessfull = false;
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "In UploadAttendance Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void UploadPromotion() {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "\nIn UploadPromotion Method");
            ArrayList<UploadPromotionModel> upmList = new ArrayList<>();
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            PromotionDBModel promotionDBModel = new PromotionDBModel();
            promotionDBModel.setPmList(DatabaseHelper.getInstance(context).getAllPromotionForUpload());

            if (promotionDBModel.getPmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No promotion record found for upload\n");
                return;
            }
            for (PromotionDBModel pm : promotionDBModel.getPmList()) {
                PromotionStudentDBModel psm = new PromotionStudentDBModel();
                psm.setPsmList(DatabaseHelper.getInstance(context).getAllPromotionStudentsForUpload(pm.getId()));
                upmList.add(new UploadPromotionModel(pm.getId(), pm.getSchoolClassId(), pm.getCreated_by(),
                        pm.getCreated_on(), androidId, psm.getPsmList()));
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(upmList.size(),
                    "Promotion", AppConstants.STUDENT_MODULE, "Uploading", 0);

            int uploadedCount = 0;

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;


            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentPromotionModuleValue));
                appSyncStatusModel.setSubModule("Student Promotion");
            }

            for (UploadPromotionModel upm : upmList) {

                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<ResponseBody> call = apiInterface.uploadPromotion(upm, token);
                final int hId = upm.getId();
                AppModel.getInstance().appendLog(context, "In UploadPromotion Method. Uploading Promotion with id:" + hId);
                Response<ResponseBody> response = call.execute();
                AppModel.getInstance().appendLog(context, "In UploadPromotion onResponse Method.responseCode:" + response.code() + " id:" + hId);
                if (response.isSuccessful()) {
                    try {
                        error_count = 0;
                        isSyncSuccessfull = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).PROMOTION_UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("dd/MM/yy HH:mm:ss"));
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_PROMOTION, values, hId);
                        if (id > 0) {
                            AppModel.getInstance().appendLog(context, "In UploadPromotion Method.Promotion Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getDate());

                            uploadedCount++;
                        }

                    } catch (Exception e) {
                        AppModel.getInstance().appendErrorLog(context, "In UploadPromotion Method. exception occurs: " + e.getMessage());
                        e.printStackTrace();
                        areAllServicesSuccessful = false;
                    }
                } else {


                    if (response.code() == 502) {


                        JSONObject res = !response.errorBody().string().equals("null") &&
                                !response.errorBody().string().equals("") ?
                                new JSONObject(response.errorBody().string()) : new JSONObject();

                        String msg = res.has("Message") ?
                                res.getString("Message") : "";

                        AppModel.getInstance().appendErrorLog(context, "In UploadPromotion responseCode " + response.code()
                                + " message: " + msg);
                        for (PromotionStudentDBModel sm : upm.getPromotion_Student()) {
                            insertFailedStudent(response.code(), msg, (int) sm.getStudent_id(), "Promotion");
                            getSingleStudent((int) sm.getStudent_id());
                        }

                        DatabaseHelper.getInstance(context).deletePromotion(upm.getId());

                        continue;

                    }
                    error_count++;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    AppModel.getInstance().appendErrorLog(context, "In UploadPromotion responseCode " + response.code() + " message: " + response.message());
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                        break;
                    }
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            failureCount++;
            isSyncSuccessfull = false;
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "In UploadPromotion Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
        DataSync.isSyncRunning = false;
    }

    public void UploadWithdrawal(int schoolId) {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadWithdrawal Method");
            ArrayList<WithdrawalModel> upmList = new ArrayList<>();
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            WithdrawalModel withdrawalModel = new WithdrawalModel();

//            withdrawalModel.setWmList(DatabaseHelper.getInstance(context).getAllWithdrawalForUpload(SurveyAppModel.getInstance().getSelectedSchool(context)));
            withdrawalModel.setWmList(DatabaseHelper.getInstance(context).getAllWithdrawalForUpload(schoolId));
            if (withdrawalModel.getWmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Withdrawal record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(withdrawalModel.getWmList().size(),
                    "Withdrawal", AppConstants.STUDENT_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;


            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                appSyncStatusModel.setSubModule("Student Withdrawal");
            }


            int uploadedCount = 0;

            for (WithdrawalModel wm : withdrawalModel.getWmList()) {
                wm.setDevice_udid(androidId);
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<ResponseBody> call = apiInterface.uploadWithdrawal(wm, token);
                final int hId = wm.getId();
                AppModel.getInstance().appendLog(context, "In UploadWithdrawal Method. Uploading Withdrawal with id:" + hId);
                Response<ResponseBody> response = call.execute();
                AppModel.getInstance().appendLog(context, "In UploadWithdrawal onResponse Method.responseCode:" + response.code() + " id:" + hId);
                if (response.isSuccessful()) {
                    try {
                        error_count = 0;
                        isSyncSuccessfull = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).WITHDRAWAL_UPLOADED_ON, AppModel.getInstance().getDate());
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL, values, hId);
                        if (id > 0) {
                            AppModel.getInstance().appendLog(context, "In UploadWithdrawal Method.Withdrawal Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getDate());

                            uploadedCount++;
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In UploadWithdrawal Method. exception occurs: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {

                    if (response.code() == 502) {


                        JSONObject res = !response.errorBody().string().equals("null") &&
                                !response.errorBody().string().equals("") ?
                                new JSONObject(response.errorBody().string()) : new JSONObject();

                        String msg = res.has("Message") ?
                                res.getString("Message") : "";


                        AppModel.getInstance().appendErrorLog(context, "In UploadWithdrawal responseCode " + response.code() + " message: " + msg);


                        insertFailedStudent(response.code(), msg, wm.getStudent_id(), "Withdrawal");
                        getSingleStudent(wm.getStudent_id());
                        DatabaseHelper.getInstance(context).deleteWithdrawals(wm.getId());

                        continue;
                    }

                    error_count++;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    AppModel.getInstance().appendErrorLog(context, "In UploadWithdrawal responseCode " + response.code() + " message: " + response.message());
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                        break;
                    }
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            failureCount++;
            areAllServicesSuccessful = false;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "In UploadWithdrawal Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void UploadSchoolAudits() {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadSchoolAudits Method");
            ArrayList<UploadStudentsAuditModel> samList = new ArrayList<>();
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            samList = DatabaseHelper.getInstance(context).getAllSchoolAuditsForUpload();


            if (samList.size() == 0) {
                AppModel.getInstance().appendLog(context, "No Audit record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(samList.size(),
                    "School Audits", AppConstants.STUDENT_MODULE, "Uploading", 0);


            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;


            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                appSyncStatusModel.setSubModule("Student Audit");
            }


            int uploadedCount = 0;

            for (UploadStudentsAuditModel saModel : samList) {
                saModel.setDevice_udid(androidId);
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<ResponseBody> call = apiInterface.uploadSchoolAudits(saModel, token);
                final int hId = saModel.getId();
                AppModel.getInstance().appendLog(context, "In UploadSchoolAudits Method. Uploading Audit with id:" + hId);
                Response<ResponseBody> response = call.execute();
                AppModel.getInstance().appendLog(context, "In UploadSchoolAudits onResponse Method.responseCode:" + response.code() + " id:" + hId);
                if (response.isSuccessful()) {
                    try {
                        error_count = 0;
                        isSyncSuccessfull = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += response.raw().body().contentLength();


                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).SCHOOL_AUDIT_UPLOADED_ON, AppModel.getInstance().getDate());
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_SCHOOL_AUDIT, values, hId);
                        if (id > 0) {
                            AppModel.getInstance().appendLog(context, "In UploadSchoolAudits Method.Withdrawal Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getDate());

                            uploadedCount++;
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In UploadSchoolAudits Method. exception occurs: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    error_count++;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    AppModel.getInstance().appendErrorLog(context, "In UploadSchoolAudits responseCode " + response.code() + " message: " + response.message());
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                        break;
                    }
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In UploadSchoolAudits Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
        }
    }

    public void uploadAppReceipts() {
        try {
            int userId = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();
            List<SyncCashReceiptsModel> list = AppReceipt.getInstance(context).getAllAppReceiptsForUpload(userId);

            if (list.size() <= 0) return;

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadFeesReceipt(list, "Bearer " + AppModel.getInstance().getToken(context));
            call.enqueue(new Callback<ArrayList<GeneralUploadResponseModel>>() {
                @Override
                public void onResponse(Call<ArrayList<GeneralUploadResponseModel>> call, Response<ArrayList<GeneralUploadResponseModel>> response) {
                    if (response.isSuccessful()) {
                        AppModel.getInstance().appendLog(context, "App Receipt Uploading Successfull Code: " + response.code());
                        for (GeneralUploadResponseModel model : response.body()) {
                            ContentValues values = new ContentValues();
                            values.put(AppReceipt.SYS_ID, model.server_id);
                            values.put(AppReceipt.UPLOADED_ON, AppModel.getInstance().getDateTime());
                            AppReceipt.getInstance(context).genericUpdateMethod(values, String.valueOf(model.device_id));
                            if (model.ErrorMessage != null && !model.ErrorMessage.isEmpty()) {
                                AppModel.getInstance().appendLog(context, "App Receipt Error Message Successfull Code: " + model.ErrorMessage);
                            }
                        }
                    } else {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadAppReceipts responseCode " + response.code() + " message: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GeneralUploadResponseModel>> call, Throwable t) {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadAppReceipts " + t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            e.printStackTrace();
        }
    }

    public boolean uploadFeesHeader() {
        error_count = 0;
        try {
            int userId = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();
            List<FeesHeaderModel> fhlist = FeesCollection.getInstance(context).getAllFeesHeaderForUpload();
            ArrayList<FeesHeaderUploadModel> fhFinalList = new ArrayList<>();
            int feeHeaderCount = 0;

            ArrayList<FeesHeaderUploadModel> utmList = new ArrayList<>();
            if (fhlist.size() <= 0) return true;

            //Adding Opening balance
            List<FeesHeaderModel> filteredOpBalList = fhlist.stream().filter(fh -> fh.getCategory_id() == 3).collect(Collectors.toList());
            for (FeesHeaderModel fhm : filteredOpBalList) {
//                if (fhm.getCategory_id() == 3) {
                    ArrayList<FeesDetailUploadModel> fdmList = FeesCollection.getInstance(context)
                            .getAllFeesDetailForUpload(fhm.getId());
                    fhFinalList.add(new FeesHeaderUploadModel(fhm.getId(), fhm.getStudentId(), fhm.getSchoolClassId(),
                            fhm.getAcademicSession_id(), fhm.getFor_date(), (int) fhm.getTotal_amount(),
                            fhm.getTransactionType_id(), fhm.getCategory_id(), fhm.getReceiptNumber() + "", fhm.getCashDeposit_Sys_id(),
                            fhm.getCreatedOn(), fhm.getCreatedBy(), fhm.getSys_id(), (int) fhm.getReceipt_id(), fhm.getDeviceId(), fdmList));
//                }

            }

            if (fhFinalList == null || fhFinalList.size() == 0) {

                //Adding receipts
                List<FeesHeaderModel> filteredReceiptList = fhlist.stream().filter(fh -> fh.getCategory_id() == 2).collect(Collectors.toList());
                for (FeesHeaderModel fhm : filteredReceiptList) {
//                    if (fhm.getCategory_id() == 2) {
                        feeHeaderCount++;
                        //Limit upload to maximum 10 records to upload
                        if (feeHeaderCount > 0 && feeHeaderCount <= 10) {
                            ArrayList<FeesDetailUploadModel> fdmList = FeesCollection.getInstance(context)
                                    .getAllFeesDetailForUpload(fhm.getId());
                            utmList.add(new FeesHeaderUploadModel(fhm.getId(), fhm.getStudentId(), fhm.getSchoolClassId(),
                                    fhm.getAcademicSession_id(), fhm.getFor_date(), (int) fhm.getTotal_amount(),
                                    fhm.getTransactionType_id(), fhm.getCategory_id(), fhm.getReceiptNumber() + "", fhm.getCashDeposit_Sys_id(),
                                    fhm.getCreatedOn(), fhm.getCreatedBy(), fhm.getSys_id(), (int) fhm.getReceipt_id(), fhm.getDeviceId(), fdmList));
                        } else {
                            break;
                        }
//                    }
                }

                //Adding Invoices
                ArrayList<FeesHeaderUploadModel> fhReceiptsList = new ArrayList<>(utmList);  //Contains all receipts
                List<FeesHeaderModel> filteredInvoiceList = fhlist.stream().filter(fh -> fh.getCategory_id() == 1).collect(Collectors.toList());
                for (FeesHeaderModel fhm : filteredInvoiceList) {

                    boolean isInvoiceFound = false;
                    for (FeesHeaderUploadModel fhrm : fhReceiptsList) {

                        if (fhm.getReceipt_id() == fhrm.getSys_id() || fhm.getReceipt_id() == fhrm.getId()) {
                            isInvoiceFound = true;
                            ArrayList<FeesDetailUploadModel> fdmList = FeesCollection.getInstance(context)
                                    .getAllFeesDetailForUpload(fhm.getId());
                            utmList.add(new FeesHeaderUploadModel(fhm.getId(), fhm.getStudentId(), fhm.getSchoolClassId(),
                                    fhm.getAcademicSession_id(), fhm.getFor_date(), (int) fhm.getTotal_amount(),
                                    fhm.getTransactionType_id(), fhm.getCategory_id(), fhm.getReceiptNumber() + "", fhm.getCashDeposit_Sys_id(),
                                    fhm.getCreatedOn(), fhm.getCreatedBy(), fhm.getSys_id(), (int) fhm.getReceipt_id(), fhm.getDeviceId(), fdmList));

                            break;

                        }
                    }
                    if (!isInvoiceFound) {
                        AppModel.getInstance().appendErrorLog(context, "Invoice not found for receipt no:" + fhm.getReceiptNumber()
                                + " Id:" + fhm.getId() + " Student id:" + fhm.getStudentId());
                    }
                }


                //Adding Wavier
                for (FeesHeaderModel fhm : filteredInvoiceList) {
                    if (fhm.getCategory_id() == 1 && fhm.getTransactionType_id() == 3) { //category_id = 1(Invoice) and transactionType_ID = 3 (Wavier)
                        ArrayList<FeesDetailUploadModel> fdmList = FeesCollection.getInstance(context)
                                .getAllFeesDetailForUpload(fhm.getId());
                        utmList.add(new FeesHeaderUploadModel(fhm.getId(), fhm.getStudentId(), fhm.getSchoolClassId(),
                                fhm.getAcademicSession_id(), fhm.getFor_date(), (int) fhm.getTotal_amount(),
                                fhm.getTransactionType_id(), fhm.getCategory_id(), fhm.getReceiptNumber() + "", fhm.getCashDeposit_Sys_id(),
                                fhm.getCreatedOn(), fhm.getCreatedBy(), fhm.getSys_id(), (int) fhm.getReceipt_id(), fhm.getDeviceId(), fdmList));
                    } else {
                        continue;
                    }
                }

                //if some case any invoice or receipt is remaning then upload that record
                if(CollectionUtils.isEmpty(fhFinalList) && CollectionUtils.isEmpty(utmList))
                {
                    List<FeesHeaderModel> fhRemaingList = FeesCollection.getInstance(context).getAllFeesHeaderForUpload();
                    for (FeesHeaderModel fhm : fhRemaingList) {
                        ArrayList<FeesDetailUploadModel> fdmList = FeesCollection.getInstance(context)
                                .getAllFeesDetailForUpload(fhm.getId());
                        utmList.add(new FeesHeaderUploadModel(fhm.getId(), fhm.getStudentId(), fhm.getSchoolClassId(),
                                fhm.getAcademicSession_id(), fhm.getFor_date(), (int) fhm.getTotal_amount(),
                                fhm.getTransactionType_id(), fhm.getCategory_id(), fhm.getReceiptNumber() + "", fhm.getCashDeposit_Sys_id(),
                                fhm.getCreatedOn(), fhm.getCreatedBy(), fhm.getSys_id(), (int) fhm.getReceipt_id(), fhm.getDeviceId(), fdmList));
                    }
                }
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(utmList.size(),
                    "Fees Header", AppConstants.FINANCE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.FinanceModuleValue));
                appSyncStatusModel.setSubModule("FC Transaction");
            }


            //Final fh upload list
            fhFinalList.addAll(utmList);
            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadFeesHeader(fhFinalList, "Bearer " + AppModel.getInstance().getToken(context));
            Response<ArrayList<GeneralUploadResponseModel>> response = call.execute(); // works in the foreground.
            if (response.isSuccessful()) {
                feeHeaderCount = 0;
                error_count = 0;
                isSyncSuccessfull = true;
                Log.i("FeesHeader", "isSuccessful");
                AppModel.getInstance().appendLog(context, "Fees Headers Uploaded Successfully Response Code: " + response.code());

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                try {
                    FeesCollection.getInstance(context).updateTableColumnsBulk(response.body(), syncDownloadUploadModel);
                } catch (Exception e) {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadFeesHeader " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

                //Upload again if still records remaining
                if (DataSync.isFinanceSyncSuccessful) {
                    uploadFeesHeader();
                } else {
                    return true;
                }

            } else {
                error_count++;
                failureCount++;
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In uploadFeesHeader responseCode " + response.code() + " message: " + response.message());
                if (error_count >= 3) {
                    isSyncSuccessfull = false;
                }
            }
        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadFeesHeader " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
        }
        return false;
    }

    public void uploadAppInvoices() {
        try {
            List<SyncCashInvoicesModel> list = AppInvoice.getInstance(context).getAllAppInvoicesForUpload();

            if (list.size() <= 0) return;

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadFeesInvoices(list, "Bearer " + AppModel.getInstance().getToken(context));
            call.enqueue(new Callback<ArrayList<GeneralUploadResponseModel>>() {
                @Override
                public void onResponse(Call<ArrayList<GeneralUploadResponseModel>> call, Response<ArrayList<GeneralUploadResponseModel>> response) {
                    if (response.isSuccessful()) {
                        AppModel.getInstance().appendLog(context, "App Inovices Uploading Successfull Code: " + response.code());
                        for (GeneralUploadResponseModel model : response.body()) {
                            if (model.server_id > 0) {
                                ContentValues values = new ContentValues();
                                values.put(AppInvoice.SYS_ID, model.server_id);
                                values.put(AppInvoice.UPLOADED_ON, AppModel.getInstance().getDate());
                                AppInvoice.getInstance(context).genericUpdateMethod(values, String.valueOf(model.device_id));
                            }
                            if (model.ErrorMessage != null && !model.ErrorMessage.isEmpty()) {
                                AppModel.getInstance().appendErrorLog(context, "App Invoices Error Message Successfull Code: " + model.ErrorMessage);
                            }
                        }
                    } else {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadAppInvoice responseCode " + response.code() + " message: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GeneralUploadResponseModel>> call, Throwable t) {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadAppInvoice " + t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            e.printStackTrace();
        }
    }

    public void uploadCashDeposits() {
        error_count = 0;
        try {
            List<SyncCashDepositModel> list = CashDeposit.getInstance(context).getAllCashDepositsForUpload();
            List<SyncCashDepositModel> cdUploadList = new ArrayList<>();
            if (list.size() <= 0) return;

            int cashDepositCount = 0;

            for (SyncCashDepositModel cdm : list) {
                cashDepositCount++;
                //Limit upload to maximum 5 records to upload
                if (cashDepositCount > 0 && cashDepositCount <= 5) {
                    cdUploadList.add(cdm);
                } else {
                    break;
                }
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(cdUploadList.size(),
                    "Cash Deposit", AppConstants.FINANCE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.FinanceModuleValue));
                appSyncStatusModel.setSubModule("FC Deposit");
            }


            int uploadedCount = 0;

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadCashDeposit(cdUploadList, "Bearer " + AppModel.getInstance().getToken(context));
            Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
            if (response.isSuccessful()) {
                cashDepositCount = 0;
                error_count = 0;
                isSyncSuccessfull = true;
                AppModel.getInstance().appendLog(context, "Cash Deposits Uploaded Successfully Response Code: " + response.code());
                AppModel.getInstance().appendErrorLog(context, "Cash Deposits Uploaded Successfully Response Code: " + response.code());

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                for (GeneralUploadResponseModel model : response.body()) {
                    if (model.server_id > 0) {
                        DataSync.isFinanceSyncSuccessful = true;
                        ContentValues values = new ContentValues();
                        values.put(CashDeposit.SYS_ID, model.server_id);
                        values.put(CashDeposit.UPLOADED_ON, AppModel.getInstance().getDateTime());
                        AppModel.getInstance().appendErrorLog(context, "Cash Deposit Updating in local db with sys_id=" + model.server_id +
                                ",device id=" + model.device_id);
                        long i = CashDeposit.getInstance(context).genericUpdateMethod(values, String.valueOf(model.device_id));
                        if (i > 0) {

                            uploadedCount++;
                        }
                    } else if (model.server_id == -1) {
                        DataSync.isFinanceSyncSuccessful = false;
                        String errorMessage = model.ErrorMessage != null && !model.ErrorMessage.isEmpty() ? model.ErrorMessage : "";
                        AppModel.getInstance().appendErrorLog(context, "After Upload Cash Deposit sys_id=" + model.server_id +
                                " for device id=" + model.device_id
                                + " Error message:" + errorMessage);
                    } else if (model.server_id == -2) {       //Used for multiple device issue error    //remove receipt?   //untag receipts also?
                        DataSync.isFinanceSyncSuccessful = true;
                        String tableName = CashDeposit.CASH_DEPOSIT_TABLE;
                        String whereClause = CashDeposit.ID + "=?";
                        String[] whereArgs = {String.valueOf(model.device_id)};

                        AppModel.getInstance().appendErrorLog(context, "Server id = " + model.server_id +
                                " comes in uploadCashDeposits method");

                        boolean deleted = DatabaseHelper.getInstance(context).deleteRecordsFromTable(
                                tableName, whereClause, whereArgs) > 0;

                        if(deleted){
                            AppModel.getInstance().appendLog(context, "Cash Deposit having id=" + model.device_id
                                    + " Deleted because of server id = " + model.server_id);

                            //untag receipts for deleted deposit slip (Multiple Device)
                            ContentValues cv = new ContentValues();
                            cv.putNull(FeesCollection.CASH_DEPOSIT_ID);
                            cv.putNull(FeesCollection.CASH_DEPOSIT_ID_TYPE);

                            FeesCollection.getInstance(context).untagReceipts(model.getDevice_id());
//                            DatabaseHelper.getInstance(context).updateTableColumns(FeesCollection.TABLE_FEES_HEADER, cv,  model.device_id);

                        } else {
                            AppModel.getInstance().appendLog(context, "Cash Deposit having id=" + model.device_id
                                    + " Not Deleted");
                        }

                    }
                    if (model.ErrorMessage != null && !model.ErrorMessage.isEmpty()) {
                        AppModel.getInstance().appendLog(context, "Cash Deposit Error Message Successfull Code: " + model.ErrorMessage);
                        AppModel.getInstance().appendErrorLog(context, "Cash Deposit Error Message After successful upload: " + model.ErrorMessage);
                    }

                    //Update sync progress
                    syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
                }

                //Upload again if still records remaining
                if (DataSync.isFinanceSyncSuccessful) {
                    uploadCashDeposits();
                }

            } else {
                areAllServicesSuccessful = false;
                error_count++;
                failureCount++;
                AppModel.getInstance().appendErrorLog(context, "In uploadCashDeposit responseCode " + response.code() + " message: " + response.message());
                if (error_count >= 3) {
                    isSyncSuccessfull = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In uploadCashDeposit " + e.getLocalizedMessage());
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
        }
    }

    public void checkServerWithKeepAlive() {
        try {
            KeepAliveModel keepAliveModel = new KeepAliveModel();
            keepAliveModel.setApp_Timestamp(AppModel.getInstance().getDateTime());
            keepAliveModel.setAppVersion(AppModel.getInstance().getApplicationVersion(context));
            keepAliveModel.setDeviceId(AppModel.getInstance().getDeviceId(context));

            int userID = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();

            if (AppModel.getInstance().getToken(context) == null)
                keepAliveModel.setUserId(0);
            else
                keepAliveModel.setUserId(userID);

            AppModel.getInstance().appendLog(context, "\nKeep Alive");
            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.pingServer(keepAliveModel);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                AppModel.getInstance().appendLog(context, "Server responding " + response.code());
                isSyncSuccessfull = true;
            } else {
                AppModel.getInstance().appendErrorLog(context, "Failed! Got this response code " + response.code() + ", Message: " + response.message());
                failureCount++;
                isSyncSuccessfull = false;
                failureCountForAllModules++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "Exception occurred at check server method : " + e.getMessage());
            AppModel.getInstance().showErrorNotification(context, "Connection Timed Out", 1);
        }
    }

    public void checkServer() {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            LoginRequestModel rlm = new LoginRequestModel();
            rlm.setUsername(sharedPref.getString("username", ""));
            rlm.setToken(sharedPref.getString("password", ""));
            rlm.setDeviceId(androidId);
            rlm.setNotificationKey(AppModel.getInstance().getFCMToken(context));

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<LoginResponseModel> call = apiInterface.loginAPI(rlm);
            AppModel.getInstance().appendLog(context, "Checking server if responding");
            Response<LoginResponseModel> response = call.execute();
            if (response.isSuccessful()) {
                AppModel.getInstance().appendLog(context, "Server responding " + response.code());
                isSyncSuccessfull = true;
            } else {
                AppModel.getInstance().appendErrorLog(context, "Failed! Got this response code " + response.code() + ", Message: " + response.message());
                failureCount++;
                isSyncSuccessfull = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
            AppModel.getInstance().appendErrorLog(context, "Exception occurred at check server method : " + e.getMessage());

            AppModel.getInstance().showErrorNotification(context, "Connection Timed Out", 1);

        }
    }

    public void SyncAllUserSchools() {
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        DatabaseHelper.getInstance(context).dropTable(DatabaseHelper.TABLE_CALENDAR);
        DatabaseHelper.getInstance(context).createTable(DatabaseHelper.CREATE_TABLE_CALENDAR);

        ArrayList<SchoolModel> list = DatabaseHelper.getInstance(context).getAllUserSchools();

        String classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_CLASS,
                DatabaseHelper.getInstance(context).CLASS_MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String section_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_SECTION,
                DatabaseHelper.getInstance(context).SECTION_MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String withdrawal_reasons_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL_REASON,
                DatabaseHelper.getInstance(context).WITHDRAWAL_REASON_MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String calendars_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_CALENDAR,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String campus_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_CAMPUS,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String location_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_LOCATION,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String areas_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_AREA,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String region_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_REGION,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        for (final SchoolModel model : list) {

            String school_classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS,
                    DatabaseHelper.getInstance(context).SCHOOL_CLASS_MODIFIED_ON,
                    DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + "=" + model.getId(),Integer.parseInt(AppConstants.GeneralModuleValue));

            String scholarship_category_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    Scholarship_Category.SCHOLARSHIP_CAT,
                    Scholarship_Category.SCHOLARSHIP_CATEGORY_MODIFIED_ON,
                    Scholarship_Category.SCHOOL_ID + "=" + model.getId(),Integer.parseInt(AppConstants.GeneralModuleValue));

            Call<MetaDataResponseModel> schoolIdcall = apiInterface.getMetaData(model.getId(),
                    classes_modifiedOn,section_modifiedOn,school_classes_modifiedOn,
                    scholarship_category_modifiedOn,withdrawal_reasons_modifiedOn,calendars_modifiedOn,
                    campus_modifiedOn,location_modifiedOn,areas_modifiedOn,region_modifiedOn, token);

            try {

                final Response<MetaDataResponseModel> modelResponse = schoolIdcall.execute();

                if (modelResponse.isSuccessful()) {
//                    SyncSchoolSSRSummary(modelResponse.body().getSchoolSSRSummary(), model.getId());
                    DatabaseHelper.getInstance(context).updateSchoolTargetAmount(modelResponse.body().getTargetFee(), model.getId());
                    SyncSchoolClasses(modelResponse.body().getSchool_Classes(), model.getId());
                    syncCalendar(modelResponse.body().getCalendars(), model.getId());
                    syncScholarshipCategory(modelResponse.body().getScholarship_Category(), model.getId());

                }
            } catch (IOException e) {
                areAllServicesSuccessful = false;
                failureCountForAllModules++;
                e.printStackTrace();
            }
        }
    }

    public void SyncSelectedUserSchools() {
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        DatabaseHelper.getInstance(context).dropTable(DatabaseHelper.TABLE_CALENDAR);
        DatabaseHelper.getInstance(context).createTable(DatabaseHelper.CREATE_TABLE_CALENDAR);

        String classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_CLASS,
                DatabaseHelper.getInstance(context).CLASS_MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String section_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_SECTION,
                DatabaseHelper.getInstance(context).SECTION_MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String withdrawal_reasons_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL_REASON,
                DatabaseHelper.getInstance(context).WITHDRAWAL_REASON_MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String calendars_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_CALENDAR,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String campus_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_CAMPUS,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String location_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_LOCATION,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String areas_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_AREA,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        String region_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_REGION,
                DatabaseHelper.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        ArrayList<SchoolModel> list = DatabaseHelper.getInstance(context)
                .getAllUserSchoolsById(AppModel.getInstance().getSearchedSchoolId(context));

        for (final SchoolModel model : list) {

            String school_classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS,
                    DatabaseHelper.getInstance(context).SCHOOL_CLASS_MODIFIED_ON,
                    DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + "=" + model.getId(),Integer.parseInt(AppConstants.GeneralModuleValue));

            String scholarship_category_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    Scholarship_Category.SCHOLARSHIP_CAT,
                    Scholarship_Category.SCHOLARSHIP_CATEGORY_MODIFIED_ON,
                    Scholarship_Category.SCHOOL_ID + "=" + model.getId(),Integer.parseInt(AppConstants.GeneralModuleValue));

            Call<MetaDataResponseModel> schoolIdcall = apiInterface.getMetaData(model.getId(),
                    classes_modifiedOn,section_modifiedOn,school_classes_modifiedOn,
                    scholarship_category_modifiedOn,withdrawal_reasons_modifiedOn,calendars_modifiedOn,
                    campus_modifiedOn,location_modifiedOn,areas_modifiedOn,region_modifiedOn,
                    token);

            try {

                final Response<MetaDataResponseModel> modelResponse = schoolIdcall.execute();

                if (modelResponse.isSuccessful()) {
//                    SyncSchoolSSRSummary(modelResponse.body().getSchoolSSRSummary(), model.getId());
                    DatabaseHelper.getInstance(context).updateSchoolTargetAmount(modelResponse.body().getTargetFee(), model.getId());
                    SyncSchoolClasses(modelResponse.body().getSchool_Classes(), model.getId());
                    syncCalendar(modelResponse.body().getCalendars(), model.getId());
                    syncScholarshipCategory(modelResponse.body().getScholarship_Category(), model.getId());
//                    Call<StudentDataResponseModel> call = apiInterface.getStudentData(model.getId(), "01-03-2017", token);
//                    call.enqueue(new Callback<StudentDataResponseModel>() {
//                        @Override
//                        public void onResponse(Call<StudentDataResponseModel> call, final Response<StudentDataResponseModel> response) {
//                            if (response.isSuccessful()) {
//                                try {
//                                    final StudentDataResponseModel sdrm = response.body();
//                                    if (sdrm != null) {
//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                DataSync ds = new DataSync(context);
//                                                ds.SyncStudents(sdrm.getStudent(), model.getId());
////                                                ds.SyncEnrollment(sdrm.getEnrollment(), sdrm.getEnrollment_Image());
//                                                ds.SyncAttendance(sdrm.getAttendance(), sdrm.getStudent_Attendance(), model.getId());
//                                                ds.SyncPromotion(sdrm.getPromotion(), sdrm.getPromotion_Student(), model.getId());
//                                                ds.SyncWithdrawal(sdrm.getWithdrawal(), model.getId());
//                                                ds.SyncSchoolAudits(sdrm.getSchoolAudit(), model.getId());
//                                            }
//                                        }).start();
//                                    }
//                                } catch (Exception ex) {
//                                    areAllServicesSuccessful = false;
//                                    ex.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<StudentDataResponseModel> call, Throwable t) {
//                            areAllServicesSuccessful = false;
//                        }
//                    });
                }
            } catch (IOException e) {
                areAllServicesSuccessful = false;
                failureCountForAllModules++;
                e.printStackTrace();
            }
        }
    }

    public void uploadUserSchoolsCashDepositImage(SyncAdapter syncAdapter) {
        List<SchoolModel> schoolList = DatabaseHelper.getInstance(context).getAllUserSchools();

        for (SchoolModel model : schoolList) {
            uploadCashDepositImage(syncAdapter, model.getId());
            AppConstants.isAppSyncTableFirstTime = false; //so other school record also inserted
        }
    }

    public void uploadCashDepositImage(SyncAdapter syncAdapter, int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method");
            ArrayList<EnrollmentImageModel> list;
//            list = CashDeposit.getInstance(context).getAllCashDepositImages(SurveyAppModel.getInstance().getSelectedSchool(context));
            list = CashDeposit.getInstance(context).getAllCashDepositImages();
            if (list.size() == 0) {
                AppModel.getInstance().appendLog(context, "No Records found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(list.size(),
                    "Cash Deposit Image", AppConstants.IMAGE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;


            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.FinanceModuleValue));
                appSyncStatusModel.setSubModule("FC Deposit Image");
            }


            int uploadedCount = 0;
            List<String> previousImageFileName = new ArrayList<>();

            for (EnrollmentImageModel eim : list) {
                try {
                    if (eim.getFilename() == null)
                        continue;

                    if (previousImageFileName != null && previousImageFileName.size() > 0) {
                        if (previousImageFileName.contains(eim.getFilename())) { //if cash deposit image is same then dont upload again
                            continue;
                        } else {
                            previousImageFileName.add(eim.getFilename());
                        }
                    } else {
                        previousImageFileName.add(eim.getFilename());
                    }

                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method.Uploading Image:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);
                    int responseCode = connectionClass.uploadFile(fdir + "/" + eim.getFilename(),
                            context.getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_CASH_DEPOSIT_IMAGES + "?id=" + eim.getId());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        Log.d("Success", "zz");
                        AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(fdir + "/" + eim.getFilename()).length();


                        String name = HttpConnectionClass.responseJson;
                        name = name.replace('\\', '/');

                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(CashDeposit.PICTURE_SLIP_FILENAME, name);
                        values.put(CashDeposit.IMAGE_UPLOADED_ON, AppModel.getInstance().getDate());
//                        values.put(CashDeposit.DOWNLOADED_ON, AppModel.getInstance().getDate());
                        int id = CashDeposit.getInstance(context).genericUpdateMethodForImage(
                                values, eim.getFilename());
                        if (id > 0) {
                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());

                            File sourceFilename = new File(fdir + "/" + eim.getFilename());
                            File updatedFileName = new File(fdir + "/" + name);
                            if (!updatedFileName.exists()) {
                                try {
                                    String rootPath = updatedFileName.getParent();
                                    String fileName = updatedFileName.getName();


                                    assert rootPath != null;
                                    File root = new File(rootPath);
                                    if (!root.exists()) {
                                        root.mkdirs();
                                    }

                                    //It will move the file to the path and renamed it
                                    boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                    if (isNameChanged) {
                                        AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method. Image name changed in local.");
                                    }

                                    AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method. Image path changed in local.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            uploadedCount++;
                        }
                    } else {
                        failureCount++;
                        error_count++;
                        areAllServicesSuccessful = false;
                        Log.d("HERe", "true" + responseCode);
                        AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                        if (error_count == 3) {
                            areAllServicesSuccessful = false;
                            isSyncSuccessfull = false;
                            break;
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    Log.d("HERe", "true" + e.toString());
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In uploadCashDepositImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void uploadNewPassword(String newpassword) {
//        boolean passswordSuccessfullyChanged = false;
//        boolean isPasswordChanged = false;
//        isPasswordChanged = SurveyAppModel.getInstance().readFromSharedPreferences(context, AppConstants.Password_Changed).equals("1");
//        if (isPasswordChanged) {
        try {
            int id = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId();
            final List<UserModel> userModelList = UserInfo.getInstance(context).getUserData(id);

//        UploadUserModel uum = new UploadUserModel();
            int counter = 0;
//            String lastpassword = userModelList.get(0).getLastpassword();
            String NewPass = newpassword;

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.uploadNewPassword(NewPass, "Bearer " + userModelList.get(0).getSession_token());
            try {

                final Response<ResponseBody> modelResponse = call.execute();

                if (modelResponse.code() == 404 || modelResponse.code() == 401) {
                    AppModel.getInstance().appendErrorLog(context, "In uploadNewPassword responseCode " + modelResponse.code() + " message: " + "User unable to authenticate!");
                    ((ChangePasswordActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "ResponseCode " + modelResponse.code() + "\nmessage: " + "User unable to authenticate!", Toast.LENGTH_LONG).show();
                        }
                    });

//                    passswordSuccessfullyChanged=false;
                } else if (modelResponse.code() == 502) {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadNewPassword responseCode " + modelResponse.code() + " message: " + "Unable to save password on server!");
                    ((ChangePasswordActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "ResponseCode " + modelResponse.code() + "\nmessage: " + "Unable to save password on server!", Toast.LENGTH_LONG).show();
                        }
                    });

//                    passswordSuccessfullyChanged=false;
                } else if (modelResponse.isSuccessful()) {
//                    ContentValues values = new ContentValues();
//                    values.put(UserInfo.KEY_LAST_UPLOADED_ON, SurveyAppModel.getInstance().getDateTime());
//                    values.put(UserInfo.KEY_PASSWORD_CHANGE_ON_DATETIME, SurveyAppModel.getInstance().getDateTime());
//                    UserInfo.getInstance(context).updateMethod(values, String.valueOf(userModelList.get(0).getId()));
//                    SurveyAppModel.getInstance().writeToSharedPreferences(context, AppConstants.Password_Changed, "0");
                    AppModel.getInstance().appendLog(context, "New Password Uploaded Successfully response: " + modelResponse.code());

                    long i = UserInfo.getInstance(context).changePassword(
                            NewPass,
                            id);

                    long j = UserInfo.getInstance(context).setPasswordChangeOnLogin(0,
                            id);

//                    context.startActivity(new Intent(context, NewDashboardActivity.class));

                    ((ChangePasswordActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "New Password Uploaded Successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                    context.startActivity(new Intent(context, NewDashboardActivity.class));

                } else {
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadNewPassword responseCode " + modelResponse.code() + " message: " + modelResponse.message());
                    ((ChangePasswordActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "ResponseCode " + modelResponse.code() + "\nmessage: " + modelResponse.message(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                e.printStackTrace();
            }

        } catch (Exception e) {
            areAllServicesSuccessful = false;
            e.printStackTrace();
        }
//        }
    }

    public void syncCalendar(ArrayList<CalendarsModel> calendars, int schoolId) {
        long startTime = 0;

        //Add Calendar Data
        if (calendars != null && calendars.size() > 0) {


            startTime = System.currentTimeMillis();
            syncingLogsTime(startTime, schoolId, "syncCalendar", true);

            AppModel.getInstance().appendErrorLog(context, "Calendar count:" + calendars.size() + " school id:" + schoolId);

            AppModel.getInstance().appendLog(context, "Calendar count:" + calendars.size() + " school id:" + schoolId);

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(calendars.size(),
                    "Calendar", AppConstants.SCHOOL_MODULE, "Downloading", schoolId);
            int downloadedCount = 0;

            try {
                for (CalendarsModel cm : calendars) {
                    cm.setSchoolId(schoolId);
                    if (!DatabaseHelper.getInstance(context).FindCalendarRecord(cm.getSchoolId(), cm.getActivity_Start_Date())) {
                        long i = DatabaseHelper.getInstance(context).addCalendar(cm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    } else {
                        long i = DatabaseHelper.getInstance(context).updateCalendar(cm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }


                    //Update sync progress
                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }

            } catch (Exception e) {
                areAllServicesSuccessful = false;
                e.printStackTrace();
                AppModel.getInstance().appendErrorLog(context, "Exception in syncCalendar error:" + e.getMessage());
            }

            syncingLogsTime(startTime, schoolId, "syncCalendar", false);

        } else {
            AppModel.getInstance().appendErrorLog(context, "No Calendar record found");
        }

    }

    private void insertFailedStudent(int responseCode, String responseMessage, int studentId, String activity) {
        ErrorResponseModel erm = new ErrorResponseModel();
        erm.setCode(responseCode);
        erm.setActivityOfError(activity);
        erm.setMessage(responseMessage);
        erm.setStudentId(studentId);
        erm.setCreated_on(AppModel.getInstance().getDateTimeIn24Format());
        ErrorLog.getInstance(context).insertErrorResponse(erm);
    }

    private void insertFailedStudent(int responseCode, String responseMessage, int studentId, String activity, String name, String grNo, String schoolClassId) {
        ErrorResponseModel erm = new ErrorResponseModel();
        erm.setCode(responseCode);
        erm.setActivityOfError(activity);
        erm.setMessage(responseMessage);
        erm.setName(name);
        erm.setGr_no(grNo);
        erm.setSchoolClass_id(schoolClassId);
        erm.setStudentId(studentId);
        erm.setCreated_on(AppModel.getInstance().getDateTimeIn24Format());
        ErrorLog.getInstance(context).insertErrorResponse(erm);
    }

    private void checkSumRule() {
        // TODO: 01/08/2019 need to get the checksum boolean from sharedpreferences
        boolean isCheckSumAllowed = false;
        if (isCheckSumAllowed) {

        }

    }

    // HR Get Data

    public void syncEmployeeMetaData(final Context context) {

//        String dateFrom = DatabaseHelper.getInstance(context).getLastModifiedOnForEmployees(0);
        //25-02-2021 by Ahmad Meghani
        //removing modifiedOn logic as requested by Taimur bhai of TCF. Doing this because
        // the employee records we don't get from API are going to get marked as inactive.
        String dateFrom = "01-03-2017";
        AppModel.getInstance().appendLog(context, "\nSyncing Employee Meta Data");

        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;

        AppSyncStatusModel appSyncStatusModel = null;

        if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
            appSyncStatusModel = new AppSyncStatusModel();
            appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
            appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            appSyncStatusModel.setSubModule("Employees Metadata");
        }


        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        String designation_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EmployeesDesignation,
                EmployeeHelperClass.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String leaveType_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_Leaves_Type,
                EmployeeHelperClass.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String resignReason_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EmployeesResignationReason,
                EmployeeHelperClass.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String resignType_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EmployeesResignationType,
                EmployeeHelperClass.MODIFIED_ON,
                null,Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(0,
                "",
                designation_ModifiedOn, leaveType_ModifiedOn,
                resignReason_ModifiedOn, resignType_ModifiedOn, token);
//        Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(0, dateFrom, token);
        try {
            Response<EmployeeDataResponseModel> edrmResponse = call.execute();
            AppModel.getInstance().appendLog(context, "Employee Meta Data Sync Called got Response code :" + edrmResponse.code() + "\n");
            if (edrmResponse.isSuccessful()) {
                final EmployeeDataResponseModel edrm = edrmResponse.body();
                isSyncSuccessfull = true;

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(edrmResponse.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                if (edrm != null) {

                    try{
                        Thread EmployeeMetadataThread = new Thread(() -> {
                            AppModel.getInstance().appendLog(context, "Sync Employee Meta Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                            SyncEmployeeDesignation(edrm.getDesignation());
                            SyncEmployeeLeaveType(edrm.getLeaveType());
                            SyncEmployeeResignReason(edrm.getResignReason());
                            SyncEmployeeResignType(edrm.getResignType());
                            AppModel.getInstance().appendLog(context, "Sync Employee Meta Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));
                        });
                        EmployeeMetadataThread.start();
//                        EmployeeMetadataThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"syncEmployeeMetaData Error: "+e.getMessage());
                    }
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "Employee Meta Data Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Employee meta data " + e.getMessage());
        }


    }

    public void syncSeparationDetailData(final Context context) {
        AppModel.getInstance().appendLog(context, "\nSyncing Separation Detail Data");
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;

        AppSyncStatusModel appSyncStatusModel = null;

        if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
            appSyncStatusModel = new AppSyncStatusModel();
            appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
            appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            appSyncStatusModel.setSubModule("Employee Separation");
        }


        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<SeparationDataResponseModel> call = apiInterface.getSeparationDetail(token);
        try {
            Response<SeparationDataResponseModel> edrmResponse = call.execute();
            AppModel.getInstance().appendLog(context, "Separation Detail Data Sync Called got Response code :" + edrmResponse.code() + "\n");
            if (edrmResponse.isSuccessful()) {
                final SeparationDataResponseModel edrm = edrmResponse.body();
                isSyncSuccessfull = true;

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(edrmResponse.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                if (edrm != null) {
                    try{
                        Thread SeparationDetailThread = new Thread(() -> {
                            AppModel.getInstance().appendLog(context, "Sync Separation Detail Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                            SyncEmployeeDetail(edrm.getUsers(), 0, true);
                            SyncResignReceived(edrm.getSeparationDetail(), 0, true);
                            SyncResignReceivedImages(edrm.getResignReceivedImages(), 0, true);
                            SyncSeparationDetail(edrm.getAllSeparations());
                            SyncPendingSeparations(edrm.getPendingApprovals());
                            AppModel.getInstance().appendLog(context, "Sync Separation Detail Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));
                        });
                        SeparationDetailThread.start();
//                        SeparationDetailThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"syncSeparationDetailData Error: "+e.getMessage());
                    }

                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "Separation Detail Data Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Separation Detail data " + e.getMessage());
        }

    }

    public void syncExpenseData(final Context context) {
        //TODO getAllUserExpenseSchools getLatestModifiedOnExpenseTransaction
        List<SchoolModel> schoolModels = DatabaseHelper.getInstance(context).getAllUserSchoolsForExpense();
        for (final SchoolModel model : schoolModels) {
            String dateFrom = ExpenseHelperClass.getInstance(context).getLatestModifiedOnExpenseData(model.getId());
            AppModel.getInstance().appendLog(context, "\nSyncing Expense Data");
            AppModel.getInstance().setSelectedSchool(context, model.getId());
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.ExpenseModuleValue));
                appSyncStatusModel.setSubModule("FE Transaction");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

            Call<ExpenseDataResponseModel> call = apiInterface.getExpensesData(model.getId(), dateFrom, token);

            try {
                Response<ExpenseDataResponseModel> edrmResponse = call.execute();
                AppModel.getInstance().appendLog(context, "Expense Data Sync Called got Response code :" + edrmResponse.code() + "\n");
                if (edrmResponse.isSuccessful()) {
                    final ExpenseDataResponseModel edrm = edrmResponse.body();
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(edrmResponse.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    if (edrm != null) {
                        try{
                            Thread SyncExpenseDataThread = new Thread(() -> {
                                List<String> allowedModules = null;
                                if (model.getAllowedModule_App() != null) {
                                    allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                                }

                                AppModel.getInstance().appendLog(context, "Sync Expense Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                                SyncSchoolPettyCashMonthlyLimits(edrm.getSchoolPettyCashMonthlyLimits(), model.getId());
                                SyncSubheadLimitsMonthly(edrm.getSubheadLimitsMonthly(), model.getId());
                                SyncExpenseTransactions(edrm.getTransactions(), model.getId());
                                SyncExpenseTransactionImages(edrm.getTransactionImages(), model.getId());
                                SyncAmountClosing(edrm.getAmountClosing(), model.getId());

                                AppModel.getInstance().appendLog(context, "Sync Expense Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                            });
                            SyncExpenseDataThread.start();
//                            SyncExpenseDataThread.join();
                        }catch (Exception e){
                            e.printStackTrace();
                            AppModel.getInstance().appendErrorLog(context,"syncExpenseData Error: "+e.getMessage());
                        }
                    }
                } else {
                    AppModel.getInstance().appendErrorLog(context, "Expense Data Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;

                }
            } catch (IOException e) {
                e.printStackTrace();
                areAllServicesSuccessful = false;
                failureCountForAllModules++;
                AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Expense data " + e.getMessage());
            }
        }
    }

    public void syncExpenseDataForSingleSchool(final Context context, SchoolModel model) {
        try {
            String dateFrom = ExpenseHelperClass.getInstance(context).getLatestModifiedOnExpenseData(model.getId());
            AppModel.getInstance().appendLog(context, "\nSyncing Expense Data for single school");
            AppModel.getInstance().setSelectedSchool(context, model.getId());
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.ExpenseModuleValue));
                appSyncStatusModel.setSubModule("FE Transaction");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ExpenseDataResponseModel> call = apiInterface.getExpensesData(model.getId(), dateFrom, token);
            try {
                Response<ExpenseDataResponseModel> edrmResponse = call.execute();
                AppModel.getInstance().appendLog(context, "Expense Data for single school Sync Called got Response code :" + edrmResponse.code() + "\n");
                if (edrmResponse.isSuccessful()) {
                    final ExpenseDataResponseModel edrm = edrmResponse.body();
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(edrmResponse.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    if (edrm != null) {
                        try{
                            Thread SyncExpenseDataForSingleSchoolThread = new Thread(() -> {
                                List<String> allowedModules = null;
                                if (model.getAllowedModule_App() != null) {
                                    allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                                }

                                AppModel.getInstance().appendLog(context, "Sync Expense Data for single school Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                                SyncSchoolPettyCashMonthlyLimits(edrm.getSchoolPettyCashMonthlyLimits(), model.getId());
                                SyncSubheadLimitsMonthly(edrm.getSubheadLimitsMonthly(), model.getId());
                                SyncExpenseTransactions(edrm.getTransactions(), model.getId());
                                SyncExpenseTransactionImages(edrm.getTransactionImages(), model.getId());
                                SyncAmountClosing(edrm.getAmountClosing(), model.getId());

                                AppModel.getInstance().appendLog(context, "Sync Expense Data for single school Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                            });
                            SyncExpenseDataForSingleSchoolThread.start();
//                            SyncExpenseDataForSingleSchoolThread.join();
                        }catch (Exception e){
                            e.printStackTrace();
                            AppModel.getInstance().appendErrorLog(context,"syncExpenseDataForSingleSchool Error: "+e.getMessage());
                        }
                    }
                } else {
                    AppModel.getInstance().appendErrorLog(context, "Expense Data for single school Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;

                }
            } catch (IOException e) {
                e.printStackTrace();
                areAllServicesSuccessful = false;
                failureCountForAllModules++;
                AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Expense data for single school " + e.getMessage());
            }
        }catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Expense data for single school " + e.getMessage());
        }

    }

    public void syncEmployeeData(final Context context) {
        List<SchoolModel> schoolModels = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployee();
        for (final SchoolModel model : schoolModels) {
//            String dateFrom = DatabaseHelper.getInstance(context).getLastModifiedOnForEmployees(model.getId());
            //25-02-2021 by Ahmad Meghani
            //removing modifiedOn logic as requested by Taimur bhai of TCF. Doing this because
            // the employee records we don't get from API are going to get marked as inactive.
            String dateFrom = "01-03-2017";
            AppModel.getInstance().appendLog(context, "\nSyncing Employee Data");
            AppModel.getInstance().setSelectedSchool(context, model.getId());
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
                appSyncStatusModel.setSubModule("Employees");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

            String employeeDetail_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE,
                    EmployeeHelperClass.MODIFIED_ON,
                    "id in (SELECT emp_detail_id from EmployeeSchool where SchoolID = " + model.getId() + " )" ,
                    Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
//            String employeeSchool_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
//                    context,
//                    EmployeeHelperClass.TABLE_EMPLOYEE_SCHOOL,
//                    EmployeeHelperClass.MODIFIED_ON,
//                    "SchoolID = " + model.getId(),
//                    Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            String employeeAttendance_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance,
                    EmployeeHelperClass.MODIFIED_ON,
                    "SchoolID = " + model.getId(),
                    Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            String employeeLeave_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.TABLE_EmployeesLeaves,
                    EmployeeHelperClass.MODIFIED_ON,
                    "SchoolID = " + model.getId(),
                    Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            String employeeQualHistory_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.TABLE_EmployeeQualificationDetail,
                    EmployeeHelperClass.MODIFIED_ON,
                    "SchoolID = " + model.getId(),
                    Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            String employeePosHistory_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.TABLE_EmployeePosition,
                    EmployeeHelperClass.MODIFIED_ON,
                    "SchoolID = " + model.getId(),Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            String employeeResign_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.EMPLOYEE_RESIGNATION_TABLE,
                    EmployeeHelperClass.MODIFIED_ON,
                    "schoolId = " + model.getId(),
                    Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            /*String employeeResignImages_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.TABLE_SeparationImages,
                    EmployeeHelperClass.MODIFIED_ON,
                    null);*/


//            Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(model.getId(),
//                    employeeDetail_ModifiedOn,
//                    employeeSchool_ModifiedOn,
//                    employeeAttendance_ModifiedOn,
//                    employeeLeave_ModifiedOn,
//                    employeeQualHistory_ModifiedOn,
//                    employeePosHistory_ModifiedOn,
//                    employeeResign_ModifiedOn,
//                    employeeResignImages_ModifiedOn,
//                    token);
//            Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(model.getId(), dateFrom, token);
            Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(model.getId(), employeeDetail_ModifiedOn,
//                    employeeSchool_ModifiedOn,
                    employeeAttendance_ModifiedOn, employeeLeave_ModifiedOn,
                    employeeQualHistory_ModifiedOn,employeePosHistory_ModifiedOn,
                    employeeResign_ModifiedOn, token);
            try {
                Response<EmployeeDataResponseModel> edrmResponse = call.execute();
                AppModel.getInstance().appendLog(context, "Employee Data Sync Called got Response code :" + edrmResponse.code() + "\n");
                if (edrmResponse.isSuccessful()) {
                    final EmployeeDataResponseModel edrm = edrmResponse.body();
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(edrmResponse.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    if (edrm != null) {
                        try{
                            Thread SyncEmployeeDataThread = new Thread(() -> {
                                List<String> allowedModules = null;
                                if (model.getAllowedModule_App() != null) {
                                    allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                                }

                                AppModel.getInstance().appendLog(context, "Sync Employee Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                                SyncEmployeeDetail(edrm.getUser(), model.getId(), false);
                                DownloadUserImages(model.getId());
                                SyncEmployeeSchool(edrm.getUserSchool(), model.getId());
                                if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                    SyncEmployeeTeacherLeave(edrm.getTeacherLeave(), model.getId());
                                }
                                if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                    SyncEmpTeacherAttendance(edrm.getTeacher_Attendance(), model.getId());
                                }
                                if (allowedModules != null && (allowedModules.contains(AppConstants.HRResignationModuleValue) ||
                                        allowedModules.contains(AppConstants.HRTerminationModuleValue))) {

                                    SyncResignReceived(edrm.getResignReceived(), model.getId(), false);
                                    SyncResignReceivedImages(edrm.getResignReceivedImages(), model.getId(), false);
                                }
                                SyncQualificationHistory(edrm.getQualificationHistory(), model.getId());
                                SyncPositionHistory(edrm.getPositionHistory(), model.getId());


                                SyncEmployeeDesignation(edrm.getDesignation());
                                SyncEmployeeLeaveType(edrm.getLeaveType());
                                SyncEmployeeResignReason(edrm.getResignReason());
                                SyncEmployeeResignType(edrm.getResignType());

                                AppModel.getInstance().appendLog(context, "Sync Employee Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                            });
                            SyncEmployeeDataThread.start();
//                            SyncEmployeeDataThread.join();
                        }catch (Exception e){
                            e.printStackTrace();
                            AppModel.getInstance().appendErrorLog(context,"syncEmployeeData Error: "+e.getMessage());
                        }
                    }
                } else {
                    AppModel.getInstance().appendErrorLog(context, "Employee Data Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;

                }
            } catch (IOException e) {
                e.printStackTrace();
                areAllServicesSuccessful = false;
                failureCountForAllModules++;
                AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Employee data " + e.getMessage());
            }
        }
    }

    public void syncEmployeeDataForSingleSchool(final Context context, SchoolModel model) {

//        String dateFrom = DatabaseHelper.getInstance(context).getLastModifiedOnForEmployees(model.getId());
        //25-02-2021 by Ahmad Meghani
        //removing modifiedOn logic as requested by Taimur bhai of TCF. Doing this because
        // the employee records we don't get from API are going to get marked as inactive.
        String dateFrom = "01-03-2017";
        AppModel.getInstance().appendLog(context, "\nSyncing Employee Data for single school");
        AppModel.getInstance().setSelectedSchool(context, model.getId());
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;

        AppSyncStatusModel appSyncStatusModel = null;

        if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
            appSyncStatusModel = new AppSyncStatusModel();
            appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
            appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            appSyncStatusModel.setSubModule("Employees");
        }


        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        String employeeDetail_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.EMPLOYEE_DETAIL_TABLE,
                EmployeeHelperClass.MODIFIED_ON,
                "id in (SELECT emp_detail_id from EmployeeSchool where SchoolID = " + model.getId() + " )" ,
                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
//        String employeeSchool_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
//                context,
//                EmployeeHelperClass.TABLE_EMPLOYEE_SCHOOL,
//                EmployeeHelperClass.MODIFIED_ON,
//                "SchoolID = " + model.getId(),
//                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String employeeAttendance_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EMPLOYEE_TEACHER_Attendance,
                EmployeeHelperClass.MODIFIED_ON,
                "SchoolID = " + model.getId(),
                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String employeeLeave_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EmployeesLeaves,
                EmployeeHelperClass.MODIFIED_ON,
                "SchoolID = " + model.getId(),
                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String employeeQualHistory_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EmployeeQualificationDetail,
                EmployeeHelperClass.MODIFIED_ON,
                "SchoolID = " + model.getId(),
                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String employeePosHistory_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.TABLE_EmployeePosition,
                EmployeeHelperClass.MODIFIED_ON,
                "SchoolID = " + model.getId(),
                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
        String employeeResign_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                EmployeeHelperClass.EMPLOYEE_RESIGNATION_TABLE,
                EmployeeHelperClass.MODIFIED_ON,
                "schoolId = " + model.getId(),
                Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
            /*String employeeResignImages_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                    context,
                    EmployeeHelperClass.TABLE_SeparationImages,
                    EmployeeHelperClass.MODIFIED_ON,
                    null);*/

//            Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(model.getId(),
//                    employeeDetail_ModifiedOn,
//                    employeeSchool_ModifiedOn,
//                    employeeAttendance_ModifiedOn,
//                    employeeLeave_ModifiedOn,
//                    employeeQualHistory_ModifiedOn,
//                    employeePosHistory_ModifiedOn,
//                    employeeResign_ModifiedOn,
//                    employeeResignImages_ModifiedOn,
//                    token);

//        Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(model.getId(), dateFrom, token);
        Call<EmployeeDataResponseModel> call = apiInterface.getEmployeeData(model.getId(),
                employeeDetail_ModifiedOn,
//                employeeSchool_ModifiedOn,
                employeeAttendance_ModifiedOn, employeeLeave_ModifiedOn,
                employeeQualHistory_ModifiedOn,employeePosHistory_ModifiedOn,
                employeeResign_ModifiedOn, token);
        try {
            Response<EmployeeDataResponseModel> edrmResponse = call.execute();
            AppModel.getInstance().appendLog(context, "Employee Data for single school Sync Called got Response code :" + edrmResponse.code() + "\n");
            if (edrmResponse.isSuccessful()) {
                final EmployeeDataResponseModel edrm = edrmResponse.body();
                isSyncSuccessfull = true;

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(edrmResponse.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                if (edrm != null) {
                    try{
                        Thread SyncEmployeeDataForSingleSchoolThread = new Thread(() -> {
                            List<String> allowedModules = null;
                            if (model.getAllowedModule_App() != null) {
                                allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                            }

                            AppModel.getInstance().appendLog(context, "Sync Employee Data for single school Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                            SyncEmployeeDetail(edrm.getUser(), model.getId(), false);
                            SyncEmployeeDesignation(edrm.getDesignation());
                            SyncEmployeeSchool(edrm.getUserSchool(), model.getId());
                            if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                SyncEmployeeTeacherLeave(edrm.getTeacherLeave(), model.getId());
                            }
                            if (allowedModules != null && (allowedModules.contains(AppConstants.HRResignationModuleValue) || allowedModules.contains(AppConstants.HRTerminationModuleValue))) {

                                SyncResignReceived(edrm.getResignReceived(), model.getId(), false);
                                SyncResignReceivedImages(edrm.getResignReceivedImages(), model.getId(), false);
                            }
                            SyncQualificationHistory(edrm.getQualificationHistory(), model.getId());
                            SyncPositionHistory(edrm.getPositionHistory(), model.getId());
                            SyncEmployeeLeaveType(edrm.getLeaveType());
                            SyncEmployeeResignReason(edrm.getResignReason());
                            SyncEmployeeResignType(edrm.getResignType());
                            if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                SyncEmpTeacherAttendance(edrm.getTeacher_Attendance(), model.getId());
                            }
                            AppModel.getInstance().appendLog(context, "Sync Employee Data for single school Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                        });
                        SyncEmployeeDataForSingleSchoolThread.start();
//                        SyncEmployeeDataForSingleSchoolThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"syncEmployeeDataForSingleSchool Error: "+e.getMessage());
                    }
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "Employee Data for single school Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Employee data for single school " + e.getMessage());
        }

    }



    public void SyncEmployeeDetail(ArrayList<EmployeeModel> emList, int schoolId, boolean fromSeparationDetail) {

        if (emList == null || emList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncEmployeeDetail", true);

        AppModel.getInstance().appendLog(context, "Total Employees:" + emList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(emList.size(),
                "Employee Detail", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        int downloadedCount = 0;

        for (EmployeeModel em : emList) {
            try {
                if (!EmployeeHelperClass.getInstance(context).FindEmployeeRecord(em.getId(), schoolId)) {
                    em.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = EmployeeHelperClass.getInstance(context).addEmployee(em);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (!EmployeeHelperClass.getInstance(context).IfEmployeeNotUploaded(em.getId(), schoolId)) {
                        em.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long i = EmployeeHelperClass.getInstance(context).updateEmployee(em);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }

                    if (em.getImagePath() != null) {
                        EmployeeHelperClass.getInstance(context).deleteUserImages(em.getId());

                        String name = em.getImagePath();
                        name = name.replace("\\", "/");
                        String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/";
                        em.setImagePath(fdir + name);

                        UserImageModel userImageModel = new UserImageModel();
                        userImageModel.setUser_id(em.getId());
                        userImageModel.setUser_image_path(em.getImagePath());
                        userImageModel.setUploaded_on(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        if (em.getModifiedOn() != null) {
                            String format = AppModel.getInstance().determineDateFormat(em.getModifiedOn());
                            if (format != null){
                                userImageModel.setModifiedOn(AppModel.getInstance().convertDatetoFormat(em.getModifiedOn(),format, "yyyy-MM-dd hh:mm:ss"));
                            }
                        }else{
                            userImageModel.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
                        }
                        long id = EmployeeHelperClass.getInstance(context).insertOrUpdateUserImage(userImageModel, context);
                        if (id > 0)
                            AppModel.getInstance().appendLog(context, "In UserImage updated. UserId = " + em.getId());
                        else
                            AppModel.getInstance().appendLog(context, "Error occurred in updating user image. UserId = " + em.getId());
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncEmployeeDetail Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
//        if (!fromSeparationDetail)
//            EmployeeHelperClass.getInstance(context).removeEmployeesNotExists(schoolId, emList);
        syncingLogsTime(startTime, schoolId, "SyncEmployeeDetail", false);
    }

    public void SyncSeparationDetail(ArrayList<EmployeePendingApprovalModel> esdmList) {

        if (esdmList == null || esdmList.size() == 0) {
            EmployeeHelperClass.getInstance(context).deleteOldPendingApprovals();
            return;
        }
        EmployeeHelperClass.getInstance(context).deleteOldPendingApprovals();

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, 0, "SyncSeparationDetail", true);

        AppModel.getInstance().appendLog(context, "Total Entries in Separation Detail: " + esdmList.size());
        AppModel.getInstance().appendErrorLog(context, "Total Entries in Separation Detail: " + esdmList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(esdmList.size(),
                "Separation Detail", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        int downloadedCount = 0;

        for (EmployeePendingApprovalModel esdm : esdmList) {
            try {
                if (!EmployeeHelperClass.getInstance(context).FindApprovalRecord(esdm.getId())) {
                    esdm.setUPLOADED_ON(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    esdm.setCREATED_ON_APP(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = EmployeeHelperClass.getInstance(context).addSeparationAprrovalRecord(esdm);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (EmployeeHelperClass.getInstance(context).IfApprovalRecordNotUploaded(esdm.getId())) {
                        esdm.setUPLOADED_ON(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long i = EmployeeHelperClass.getInstance(context).updateApprovalRecord(esdm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSeparationDetail Method. exception occurs: " + e.getMessage());
                AppModel.getInstance().appendLog(context, "In SyncSeparationDetail Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
//        EmployeeHelperClass.getInstance(context).removeSeparationDetailNotExist(esdmList);

        syncingLogsTime(startTime, 0, "SyncSeparationDetail", false);
    }

    public void SyncPendingSeparations(ArrayList<EmployeePendingApprovalModel> esdmList) {

        if (esdmList == null || esdmList.size() == 0) {
            EmployeeHelperClass.getInstance(context).deleteOldPendingSeparations();
            return;
        }

        EmployeeHelperClass.getInstance(context).deleteOldPendingSeparations();

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, 0, "SyncPendingSeparations", true);


        AppModel.getInstance().appendLog(context, "Total Entries in Pending Separation: " + esdmList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(esdmList.size(),
                "Pending Separations", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        int downloadedCount = 0;

        for (EmployeePendingApprovalModel esdm : esdmList) {
            try {
                if (!EmployeeHelperClass.getInstance(context).FindPendingSeparationRecord(esdm.getId())) {
                    esdm.setUPLOADED_ON(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    esdm.setCREATED_ON_APP(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = EmployeeHelperClass.getInstance(context).addPendingSeparationRecord(esdm);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (!EmployeeHelperClass.getInstance(context).IfPendingSeparationNotUploaded(esdm.getId())) {
                        esdm.setUPLOADED_ON(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long i = EmployeeHelperClass.getInstance(context).updatePendingSeparationRecord(esdm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncPendingSeparations Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        if (esdmList.size() > 0)
            AppModel.getInstance().showPendingApprovalNotification(context, "New Separation Approvals", esdmList.size() + " new Separation Approvals found.", 9999);
//        EmployeeHelperClass.getInstance(context).removePendingSeparationNotExist(esdmList);

        syncingLogsTime(startTime, 0, "SyncPendingSeparations", false);
    }

    public void SyncEmployeeDesignation(ArrayList<EmployeeDesignationModel> designations) {

        if (designations == null || designations.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, 0, "SyncEmployeeDesignation", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(designations.size(),
                "Employee Designation", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        EmployeeHelperClass.getInstance(context).addEmployeeDesignations(designations, syncDownloadUploadModel);


        syncingLogsTime(startTime, 0, "SyncEmployeeDesignation", false);

    }

    public void SyncEmployeeLeaveType(ArrayList<EmployeeLeaveTypeModel> leaveType) {

        if (leaveType == null || leaveType.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, 0, "SyncEmployeeLeaveType", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(leaveType.size(),
                "Employee Leave Type", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        EmployeeHelperClass.getInstance(context).addEmployeeLeaveType(leaveType, syncDownloadUploadModel);


        syncingLogsTime(startTime, 0, "SyncEmployeeLeaveType", false);
    }

    public void SyncEmployeeResignReason(ArrayList<EmployeeResignReasonModel> resignReason) {

        if (resignReason == null || resignReason.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, 0, "SyncEmployeeResignReason", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(resignReason.size(),
                "Employee Resign Reason", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        EmployeeHelperClass.getInstance(context).addEmployeeResignReason(resignReason, syncDownloadUploadModel);

        syncingLogsTime(startTime, 0, "SyncEmployeeResignReason", false);
    }

    public void SyncEmployeeResignType(ArrayList<EmployeeResignTypeModel> resignType) {

        if (resignType == null || resignType.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, 0, "SyncEmployeeResignType", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(resignType.size(),
                "Employee Resign Type", AppConstants.EMPLOYEE_MODULE, "Downloading", 0);

        EmployeeHelperClass.getInstance(context).addEmployeeResignType(resignType, syncDownloadUploadModel);

        syncingLogsTime(startTime, 0, "SyncEmployeeResignType", false);

    }



    public void SyncEmployeeSchool(ArrayList<EmployeeSchoolModel> empSchoolsList, int schoolid) {


        if (empSchoolsList == null || empSchoolsList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncEmployeeSchool", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(empSchoolsList.size(),
                "Employee School", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolid);

        EmployeeHelperClass.getInstance(context).addEmployeeSchool(empSchoolsList, schoolid, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncEmployeeSchool", false);
    }

    public void SyncEmployeeTeacherLeave(ArrayList<EmployeeLeaveModel> empLeavesList, int schoolid) {

        if (empLeavesList == null || empLeavesList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();

        syncingLogsTime(startTime, schoolid, "SyncEmployeeTeacherLeave", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(empLeavesList.size(),
                "Employee Teacher Leaves", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolid);

        EmployeeHelperClass.getInstance(context).addEmployeesLeaves(empLeavesList, schoolid, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncEmployeeTeacherLeave", false);
    }

    public void SyncResignReceived(ArrayList<EmployeeSeparationModel> ermList, int schoolId, boolean fromSeparation) {

        if (ermList == null || ermList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();

        syncingLogsTime(startTime, schoolId, "SyncResignReceived", true);

        AppModel.getInstance().appendLog(context, "Total Resigned Employees:" + ermList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(ermList.size(),
                "Resignation", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (EmployeeSeparationModel erm : ermList) {
            try {
                if (schoolId > 0) {
                    erm.setSchoolID(schoolId);
                }

                if (!EmployeeHelperClass.getInstance(context).FindResignedEmployeeRecord(erm.getId(), erm.getSchoolID())) {
                    erm.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                    long i = EmployeeHelperClass.getInstance(context).addResignedEmployee(erm);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (!EmployeeHelperClass.getInstance(context).IfResignedEmployeeNotUploaded(erm.getId(), erm.getSchoolID())) {
                        erm.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                        long i = EmployeeHelperClass.getInstance(context).updateResignedEmployee(erm);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncResignedEmployees Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
//        if (!fromSeparation)
//            EmployeeHelperClass.getInstance(context).removeResignedEmployeesNotExists(schoolId, ermList);


        syncingLogsTime(startTime, schoolId, "SyncResignReceived", false);
    }


    public void SyncResignReceivedImages(ArrayList<SeparationAttachmentsModel> samList, int schoolId, boolean fromSeparation) {

        if (samList == null)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncResignReceivedImages", true);

        AppModel.getInstance().appendLog(context, "Total Separation Images:" + samList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(samList.size(),
                "Resignation", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (SeparationAttachmentsModel sam : samList) {
            try {
                String name = sam.getSeparationAttachment();
                name = name.replace("\\", "/");
                String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/";
                sam.setSeparationAttachment(fdir + name);
                if (!EmployeeHelperClass.getInstance(context).FindSeparationAttachment(sam.getResignationID(), sam.getSeparationAttachment())) {
                    sam.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                    long i = EmployeeHelperClass.getInstance(context).addSeparationAttachment(sam);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (EmployeeHelperClass.getInstance(context).IfSepAttachmentNotUploaded(sam.getResignationID(), sam.getSeparationAttachment())) {
                        sam.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                        long i = EmployeeHelperClass.getInstance(context).updateSepImage(sam);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncResignReceivedImages Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
//        if (fromSeparation)
//            EmployeeHelperClass.getInstance(context).removeResignImageNotExists(schoolId, samList);


        syncingLogsTime(startTime, schoolId, "SyncResignReceivedImages", false);
    }

    public void SyncQualificationHistory(ArrayList<EmployeeQualificationDetailModel> eqdList, int schoolId) {

        if (eqdList == null || eqdList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncQualificationHistory", true);

        AppModel.getInstance().appendLog(context, "Total Entries in Qualification History" + eqdList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(eqdList.size(),
                "Qualification History", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (EmployeeQualificationDetailModel eqd : eqdList) {
            try {
                if (!EmployeeHelperClass.getInstance(context).FindQualificationHistoryRecord(eqd)) {
                    long i = EmployeeHelperClass.getInstance(context).addQualificationHistoryRecord(eqd);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    long i = EmployeeHelperClass.getInstance(context).updateQualificationHistoryRecord(eqd);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncQualificationHistory Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
//        EmployeeHelperClass.getInstance(context).removeQHRecordNotExists(schoolId, eqdList);


        syncingLogsTime(startTime, schoolId, "SyncQualificationHistory", false);
    }

    public void SyncPositionHistory(ArrayList<EmployeePositionModel> epmList, int schoolId) {

        if (epmList == null || epmList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncPositionHistory", true);

        AppModel.getInstance().appendLog(context, "Total Entries in Position History" + epmList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(epmList.size(),
                "Position History", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (EmployeePositionModel epm : epmList) {
            try {
                if (!EmployeeHelperClass.getInstance(context).FindPositionHistoryRecord(epm.getEmployee_Personal_Detail_ID(), epm.getPosition_Start_Date())) {
                    //eqd.setUploadedOn(SurveyAppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = EmployeeHelperClass.getInstance(context).addPositionHistoryRecord(epm);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    long i = EmployeeHelperClass.getInstance(context).updatePositionHistoryRecord(epm);

                    if (i > 0) {
                        downloadedCount++;
                    }
                }

            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncPositionHistory Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
//        EmployeeHelperClass.getInstance(context).removePHRecordNotExists(schoolId, epmList);

        syncingLogsTime(startTime, schoolId, "SyncPositionHistory", false);
    }

    public void SyncEmpTeacherAttendance(ArrayList<EmployeeTeacherAttendanceModel> etamList, int schoolId) {

        if (etamList == null || etamList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncEmpTeacherAttendance", true);

        AppModel.getInstance().appendLog(context, "Total Teacher Attendance:" + etamList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(etamList.size(),
                "Teacher Attendance", AppConstants.EMPLOYEE_MODULE, "Downloading", schoolId);

        EmployeeHelperClass.getInstance(context).addEmpTeacherAttendanceRecord(etamList, schoolId, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolId, "SyncEmpTeacherAttendance", false);
    }

    public void SyncTCTSubjects(ArrayList<TCTSubjectsModel> subjectsModelList, int schoolid) {

        if (subjectsModelList == null || subjectsModelList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncTCTSubjects", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(subjectsModelList.size(),
                "TCT Subjects", AppConstants.TCTENTRY_MODULE, "Downloading", schoolid);

        AppModel.getInstance().appendLog(context, "In SyncTCTSubjects Method");
        TCTHelperClass.getInstance(context).addOrUpdateTCTSubjects(subjectsModelList, syncDownloadUploadModel);


        syncingLogsTime(startTime, schoolid, "SyncTCTSubjects", false);
    }

    public void SyncEmpSubjectTagging(ArrayList<TCTEmpSubjectTaggingModel> tctEmpSubjectTaggingModels, int schoolid) {


        if (tctEmpSubjectTaggingModels == null || tctEmpSubjectTaggingModels.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncEmpSubjectTagging", true);


        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(tctEmpSubjectTaggingModels.size(),
                "Employee Subject Tagging", AppConstants.TCTENTRY_MODULE, "Downloading", schoolid);

        AppModel.getInstance().appendLog(context, "In SyncEmpSubjectTagging Method");
//        boolean isRemoved = TCTHelperClass.getInstance(context).removeExistingEmpSubTagging(schoolid);
        TCTHelperClass.getInstance(context).addEmpSubjectTagging(tctEmpSubjectTaggingModels, schoolid, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncEmpSubjectTagging", false);
    }

    public void SyncTCTEmpSubTagReason(ArrayList<TCTEmpSubjTagReasonModel> tctEmpSubjTagReasonModels, int schoolid) {

        if (tctEmpSubjTagReasonModels == null || tctEmpSubjTagReasonModels.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncTCTEmpSubTagReason", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(tctEmpSubjTagReasonModels.size(),
                "TCT Employee Subject Tagging Reason", AppConstants.TCTENTRY_MODULE, "Downloading", schoolid);

        AppModel.getInstance().appendLog(context, "In SyncTCTEmpSubTagReason Method");
        TCTHelperClass.getInstance(context).addOrUpdateTCTEmpSubTagReason(tctEmpSubjTagReasonModels, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncTCTEmpSubTagReason", false);
    }

    public void SyncTCTDesignations(ArrayList<TCTDesginationModel> tctDesignations, int schoolid) {

        if (tctDesignations == null || tctDesignations.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncTCTDesignations", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(tctDesignations.size(),
                "TCT Designations", AppConstants.TCTENTRY_MODULE, "Downloading", schoolid);

        AppModel.getInstance().appendLog(context, "In SyncTCTEmpSubTagReason Method");
        TCTHelperClass.getInstance(context).addOrUpdateTCTDesignations(tctDesignations, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncTCTDesignations", false);
    }

    public void SyncTCTLeaveTypes(ArrayList<TCTLeaveTypeModel> tctLeaveTypes, int schoolid) {

        if (tctLeaveTypes == null || tctLeaveTypes.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncTCTLeaveTypes", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(tctLeaveTypes.size(),
                "TCT Leave Types", AppConstants.TCTENTRY_MODULE, "Downloading", schoolid);

        AppModel.getInstance().appendLog(context, "In SyncTCTLeaveTypes Method");
        TCTHelperClass.getInstance(context).addOrUpdateTCTLeaveType(tctLeaveTypes, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncTCTLeaveTypes", false);
    }

    public void SyncTCTPhase(ArrayList<TCTPhaseModel> tctPhaseModels, int schoolid) {
        if (tctPhaseModels == null || tctPhaseModels.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolid, "SyncTCTPhase", true);

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(tctPhaseModels.size(),
                "TCT Phase", AppConstants.TCTENTRY_MODULE, "Downloading", schoolid);

        AppModel.getInstance().appendLog(context, "In SyncTCTPhase Method");
        TCTHelperClass.getInstance(context).addOrUpdateTCTPhase(tctPhaseModels, schoolid, syncDownloadUploadModel);

        syncingLogsTime(startTime, schoolid, "SyncTCTPhase", false);
    }

    // HR Uploads

    public void UploadEmployeeDetail(int schoolId) {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadEmployeeDetail Method");
            UploadEmployeeModel employeeModel = new UploadEmployeeModel();

            employeeModel.setEmployeesList(EmployeeHelperClass.getInstance(context).getAllEmployeesDetailForUpload(schoolId));
            if (employeeModel.getEmployeesList() == null || employeeModel.getEmployeesList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Employee Detail record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(employeeModel.getEmployeesList().size(),
                    "Employee Detail", AppConstants.EMPLOYEE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
                appSyncStatusModel.setSubModule("Edit Employee");
            }


            int uploadedCount = 0;

            for (UploadEmployeeModel uem : employeeModel.getEmployeesList()) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<ResponseBody> call = apiInterface.uploadEmployeeDetail(uem, token);
                final int hId = uem.getId();
                AppModel.getInstance().appendLog(context, "In UploadEmployeeDetail Method. Uploading Employee Detail with id:" + hId);
                Response<ResponseBody> response = call.execute();
                AppModel.getInstance().appendLog(context, "In UploadEmployeeDetail onResponse Method.responseCode:" + response.code() + " id:" + hId);
                if (response.isSuccessful()) {
                    try {
                        error_count = 0;
//                        isSyncSuccessfull = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                        ContentValues values = new ContentValues();
                        values.put(EmployeeHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long id = EmployeeHelperClass.getInstance(context).updateTableColumns(
                                EmployeeHelperClass.getInstance(context).EMPLOYEE_DETAIL_TABLE,
                                values, EmployeeHelperClass.getInstance(context).ID + " = " + hId);
                        if (id > 0) {
                            AppModel.getInstance().appendLog(context, "In UploadEmployeeDetail Method.Employee Detail Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                            uploadedCount++;
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In UploadEmployeeDetail Method. exception occurs: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {

                    if (response.code() == 502) {


                        JSONObject res = !response.errorBody().string().equals("null") &&
                                !response.errorBody().string().equals("") ?
                                new JSONObject(response.errorBody().string()) : new JSONObject();

                        String msg = res.has("Message") ?
                                res.getString("Message") : "";


                        AppModel.getInstance().appendErrorLog(context, "In UploadEmployeeDetail responseCode " + response.code() + " message: " + msg);


                        continue;
                    }

                    error_count++;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    AppModel.getInstance().appendErrorLog(context, "In UploadEmployeeDetail responseCode " + response.code() + " message: " + response.message());
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                        break;
                    }
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            failureCount++;
            areAllServicesSuccessful = false;
            isSyncSuccessfull = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "In UploadEmployeeDetail Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void UploadEmployeeTeacherLeaves(int schoolId) {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadEmployeeTeacherLeaves Method");
            EmployeeLeaveModel employeeLeaveModel = new EmployeeLeaveModel();

            employeeLeaveModel.setEmpLeavesList(EmployeeHelperClass.getInstance(context).getAllEmployeesLeavesForUpload(schoolId));
            if (employeeLeaveModel.getEmpLeavesList() == null || employeeLeaveModel.getEmpLeavesList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Employee Leave record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(employeeLeaveModel.getEmpLeavesList().size(),
                    "Employee Teacher Leaves", AppConstants.EMPLOYEE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HRAttendanceAndLeavesModuleValue));
                appSyncStatusModel.setSubModule("Employee Leaves");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadEmployeeTeacherLeaves(employeeLeaveModel.getEmpLeavesList(), token);

            try {
                final Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
                if (response.isSuccessful()) {
                    error_count = 0;
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

//                    SurveyAppModel.getInstance().appendLog(context, "Leaves Uploading Successful Code: " + response.code());
                    try {
                        EmployeeHelperClass.getInstance(context).updateServerIdInEmpTeacherLeave(response.body(), syncDownloadUploadModel);
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadLeaves " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                } else {
                    error_count++;
                    failureCount++;
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadLeaves responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                    }
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading Leaves " + e.getMessage());
                e.printStackTrace();
                failureCountForAllModules++;
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadEmpTeacherLeaves " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;

        }
    }

    public void uploadPendingApprovals() {

        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In uploadPendingApprovals Method");
            EmployeeSeparationDetailModel esdm = new EmployeeSeparationDetailModel();

            esdm.setEsdmList(EmployeeHelperClass.getInstance(context).getAllApprovalsForUpload());
            if (esdm.getEsdmList() == null || esdm.getEsdmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Pending Separation approval record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(esdm.getEsdmList().size(),
                    "Pending Approvals", AppConstants.EMPLOYEE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HRResignationModuleValue));
                appSyncStatusModel.setSubModule("Employee Approval");
            }


            int uploadedCount = 0;

            for (EmployeeSeparationDetailModel esdm1 : esdm.getEsdmList()) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<Boolean> call = apiInterface.uploadSeparationApproval(esdm1, token);

                try {
                    final Response<Boolean> response = call.execute();
                    if (response.isSuccessful()) {
                        error_count = 0;
                        isSyncSuccessfull = true;

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += response.raw().body().contentLength();

                        if (response.body()) {
                            AppModel.getInstance().appendLog(context, "Pending Separation Approval Uploading Successful Code: " + response.code());
                            try {
                                int hId = esdm1.getId();
                                ContentValues values = new ContentValues();
                                values.put(EmployeeHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                long id = EmployeeHelperClass.getInstance(context).updateTableColumns(
                                        EmployeeHelperClass.getInstance(context).PENDING_SEPARATION_TABLE,
                                        values, EmployeeHelperClass.getInstance(context).ID + " = " + hId);
                                if (id > 0) {
                                    AppModel.getInstance().appendLog(context, "In uploadPendingApprovals Method. Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                                    uploadedCount++;
                                }
                            } catch (Exception e) {
                                areAllServicesSuccessful = false;
                                AppModel.getInstance().appendErrorLog(context, "In uploadPendingApprovals " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        } else {
                            AppModel.getInstance().appendErrorLog(context, "Pending Separation Approvals record not updated " + response.message());
                        }
                    } else {
                        error_count++;
                        failureCount++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadPendingApprovals responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                        if (error_count >= 3) {
                            isSyncSuccessfull = false;
                        }
                    }

                } catch (Exception e) {
                    AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading PendingApprovals " + e.getMessage());
                    e.printStackTrace();
                    failureCountForAllModules++;
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadPendingApprovals " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }

    public void uploadResignation() {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadResignation Method");

            EmployeeSeparationModel erm = new EmployeeSeparationModel();

            erm.setErmList(EmployeeHelperClass.getInstance(context).getAllResignationsForUpload());
            if (erm.getErmList() == null || erm.getErmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Resignation record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(erm.getErmList().size(),
                    "Resignation", AppConstants.EMPLOYEE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HRResignationModuleValue));
                appSyncStatusModel.setSubModule("Employee Separation");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadResignReceived(erm.getErmList(), token);

            try {

                final Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
                if (response.isSuccessful()) {
                    error_count = 0;
                    //j
                    isSyncSuccessfull = true;
                    AppModel.getInstance().appendLog(context, "Resignation Uploading Successful Code: " + response.code());

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    try {
                        EmployeeHelperClass.getInstance(context).updateServerIdInResignation(response.body(), syncDownloadUploadModel);
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadResignation " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                } else {
                    error_count++;
                    failureCount++;
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadResignation responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                    }
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading Resignation " + e.getMessage());
                failureCountForAllModules++;
                e.printStackTrace();
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadResignation " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }

    public void uploadTeacherAttendance(int schoolId) {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In uploadTeacherAttendance Method");
            ArrayList<EmployeeTeacherAttendanceModel> uetamList = new ArrayList<>();
            EmployeeTeacherAttendanceModel etam = new EmployeeTeacherAttendanceModel();

            etam.setEtamList(EmployeeHelperClass.getInstance(context).getAllTeachersAttendanceForUpload(schoolId));
            if (etam.getEtamList() == null || etam.getEtamList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Teacher Attendance record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(etam.getEtamList().size(),
                    "Teacher Attendance", AppConstants.EMPLOYEE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HRAttendanceAndLeavesModuleValue));
                appSyncStatusModel.setSubModule("Employee Attendance");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadTeacherAttendance(etam.getEtamList(), token);

            try {

                final Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
                if (response.isSuccessful()) {
                    error_count = 0;
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

//                    SurveyAppModel.getInstance().appendLog(context, "Teacher Attendance Uploading Successful Code: " + response.code());
                    try {
                        EmployeeHelperClass.getInstance(context).updateServerIdInTeacherAttendance(response.body(), syncDownloadUploadModel);
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadTeacherAttendance " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                } else {
                    error_count++;
                    failureCount++;
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadTeacherAttendance responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                    }
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while syncing Fees Header " + e.getMessage());
                e.printStackTrace();
                failureCountForAllModules++;
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadTeacherAttendance " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }

    public void syncTCTEmployeeData(final Context context, int schoolId) {
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;

        AppSyncStatusModel appSyncStatusModel = null;

        if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
            appSyncStatusModel = new AppSyncStatusModel();
            appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
            appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.TCTModuleValue));
            appSyncStatusModel.setSubModule("TCT Registration");
        }


        AppModel.getInstance().appendLog(context, "\nSyncing TCT Employee Data");
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        String TCTPhase_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                TCTHelperClass.TABLE_TCT_PHASE,
                TCTHelperClass.Modified_On,
                null,
                Integer.parseInt(AppConstants.TCTModuleValue));
        String TCTEmpSubTagReason_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING_REASONS,
                TCTHelperClass.Modified_On,
                null,
                Integer.parseInt(AppConstants.TCTModuleValue));
        String EmpSubjectTagging_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                TCTHelperClass.TABLE_TCT_EMP_SUBJECT_TAGGING,
                TCTHelperClass.Modified_On,
                "SchoolID = " + schoolId,
                Integer.parseInt(AppConstants.TCTModuleValue));
        String TCTSubjects_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                TCTHelperClass.TABLE_TCT_SUBJECTS,
                TCTHelperClass.Modified_On,
                null,
                Integer.parseInt(AppConstants.TCTModuleValue));
        String LeaveTypes_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                TCTHelperClass.TABLE_TCT_LEAVES_TYPE,
                TCTHelperClass.Modified_On,
                null,
                Integer.parseInt(AppConstants.TCTModuleValue));
        String Designations_ModifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                TCTHelperClass.TABLE_TCT_DESIGNATIONS,
                TCTHelperClass.Modified_On,
                null,
                Integer.parseInt(AppConstants.TCTModuleValue));

        Call<TCTDataResponseModel> call = apiInterface.getTCTEmployeeData(schoolId, TCTPhase_ModifiedOn,
                TCTEmpSubTagReason_ModifiedOn, EmpSubjectTagging_ModifiedOn,
                TCTSubjects_ModifiedOn , LeaveTypes_ModifiedOn, Designations_ModifiedOn, token);
//        Call<TCTDataResponseModel> call = apiInterface.getTCTEmployeeData(schoolId, token);
        try {
            Response<TCTDataResponseModel> tctdrmResponse = call.execute();
            AppModel.getInstance().appendLog(context, "TCT Employee Data Sync Called got Response code :" + tctdrmResponse.code() + "\n");
            if (tctdrmResponse.isSuccessful()) {
                final TCTDataResponseModel tctdrm = tctdrmResponse.body();
                isSyncSuccessfull = true;

                if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                    appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                    appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                            appSyncStatusModel.getEndedOn()));
                    appSyncStatusModel.setDownloaded(AppModel.getInstance().convertBytesIntoKB(tctdrmResponse.raw().body().contentLength(), false));
                    SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                    AppConstants.isAppSyncTableFirstTime = true;
                }

                if (tctdrm != null) {
                    try{
                        Thread SyncTCTEmployeeDataThread = new Thread(() -> {
                            AppModel.getInstance().appendLog(context, "Sync Employee Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");
                            SyncTCTPhase(tctdrm.getPhase(), schoolId);
                            SyncTCTEmpSubTagReason(tctdrm.getSubjectTaggingReasons(), schoolId);
                            SyncEmpSubjectTagging(tctdrm.getEmployeesSubjectTagging(), schoolId);
                            SyncTCTSubjects(tctdrm.getSubjects(), schoolId);
                            SyncTCTDesignations(tctdrm.getDesignations(), schoolId);
                            SyncTCTLeaveTypes(tctdrm.getLeaveTypes(), schoolId);
                            AppModel.getInstance().appendLog(context, "Sync TCT Employee Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                        });
                        SyncTCTEmployeeDataThread.start();
//                        SyncTCTEmployeeDataThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"syncTCTEmployeeData Error: "+e.getMessage());
                    }
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "TCT Employee Data Sync Called got Response code :" + tctdrmResponse.code() + " Message " + tctdrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCountForAllModules++;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing TCT Employee data " + e.getMessage());
        }
    }

    public void UploadTCTEmployeeSubTag(int schoolId) {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadTCTEmployeeSubTag Method");
            UploadTCTEmployeeSubTagModel tctEmpSubTagModel = new UploadTCTEmployeeSubTagModel();

            tctEmpSubTagModel.setTctEmployeeSubTagModelList(TCTHelperClass.getInstance(context).getAllTCTEmpSubTagForUpload(schoolId));

            if (tctEmpSubTagModel.getTctEmployeeSubTagModelList() == null || tctEmpSubTagModel.getTctEmployeeSubTagModelList().size() == 0) {
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(tctEmpSubTagModel.getTctEmployeeSubTagModelList().size(),
                    "Employee Subject Tagging", AppConstants.EMPLOYEE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.TCTModuleValue));
                appSyncStatusModel.setSubModule("TCT Registration");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadTCTEmployeeSubTag(tctEmpSubTagModel.getTctEmployeeSubTagModelList(), token);

            try {
                final Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
                if (response.isSuccessful()) {
                    error_count = 0;
                    isSyncSuccessfull = true;

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    try {
                        TCTHelperClass.getInstance(context).updateUploadedOnInTCTEmpSubTag(response.body(), syncDownloadUploadModel);
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In UploadTCTEmployeeSubTag " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                } else {
                    error_count++;
                    failureCount++;
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In UploadTCTEmployeeSubTag responseCode " + response.code() + " message: " + response.message());

                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                    }
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading TCTEmployeeSubTag " + e.getMessage());
                failureCountForAllModules++;
                e.printStackTrace();
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In UploadTCTEmployeeSubTag " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;

        }
    }

    public void syncProgressUpdate(SyncDownloadUploadModel syncDownloadUploadModel, int downloadedCount) {
        try {
            if (syncDownloadUploadModel.getTotalFileSize() == 0)
                return;

            syncDownloadUploadModel.setCurrentFileSize(downloadedCount);

            int progress = (int) ((syncDownloadUploadModel.getCurrentFileSize() * 100) / syncDownloadUploadModel.getTotalFileSize());
            syncDownloadUploadModel.setProgress(progress);

//            SyncProgressHelperClass.getInstance(context).saveSyncProgressInLocalDB(syncDownloadUploadModel);

//            DataSync.getInstance(context).sendNotification(syncDownloadUploadModel);
            sendIntent(syncDownloadUploadModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendIntent(SyncDownloadUploadModel download) {

        Intent intent = new Intent(SyncProgressActivity.MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void onSyncProgressComplete() {
        try {
//            if (notificationBuilder != null && notificationManager != null) {
//                Thread.sleep(5000);
//                notificationManager.cancel(AppConstants.SYNCNotifyId);

                SyncDownloadUploadModel download = new SyncDownloadUploadModel();
                download.setProgress(101);
                sendIntent(download);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncProgressUpdate(SyncDownloadUploadModel syncDownloadUploadModel, int downloadedCount, boolean isShowWithNotification) {
        try {
            if (syncDownloadUploadModel.getTotalFileSize() == 0)
                return;

            syncDownloadUploadModel.setCurrentFileSize(downloadedCount);

            int progress = (int) ((syncDownloadUploadModel.getCurrentFileSize() * 100) / syncDownloadUploadModel.getTotalFileSize());
            syncDownloadUploadModel.setProgress(progress);

//            if (isShowWithNotification)
//                sendNotification(syncDownloadUploadModel);
//            else
                sendIntent(syncDownloadUploadModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadUserImages() {
        try {
            AppModel.getInstance().appendLog(context, "In uploadUserImages Method");
            List<UserImageModel> userImageModelList = EmployeeHelperClass.getInstance(context).getUserImagesForUpload();
            if (userImageModelList.size() == 0) {
                AppModel.getInstance().appendLog(context, "No User Images found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(userImageModelList.size(),
                    "User Image", AppConstants.IMAGE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HREmployeeListingModuleValue));
                appSyncStatusModel.setSubModule("Employee Image");
            }


            int uploadedCount = 0;

            for (UserImageModel userImage : userImageModelList) {
                try {
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In uploadUserImages Method.Uploading Image: user id:" + userImage.getUser_id());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    int responseCode = connectionClass.uploadFile(userImage.getUser_image_path(),
                            context.getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_USER_IMAGES + "?id=" + userImage.getUser_id());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In uploadUserImages Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(userImage.getUser_image_path()).length();

                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");
                        String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(ImagePath, fdir + "/" + name);
                        values.put(EmployeeHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        values.put(EmployeeHelperClass.getInstance(context).MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long id = EmployeeHelperClass.getInstance(context).updateTableColumns(
                                EmployeeHelperClass.getInstance(context).TABLE_UserImages, values,
                                EmployeeHelperClass.getInstance(context).Employee_Personal_Detail_ID + " = " + userImage.getUser_id() + " AND " +
                                        EmployeeHelperClass.getInstance(context).ImagePath + " = '" + userImage.getUser_image_path() + "'");
                        if (id > 0) {

                            //for progress update
                            uploadedCount++;

                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In uploadUserImages Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(userImage.getUser_image_path());
                            if (sourceFilename.exists()) {
//                                if (eim.getFiletype().toLowerCase().equals("p")) {
                                File updatedFileName = new File(fdir + "/" + name);
                                if (!updatedFileName.exists()) {
                                    try {
                                        String rootPath = updatedFileName.getParent();
                                        String fileName = updatedFileName.getName();


                                        assert rootPath != null;
                                        File root = new File(rootPath);
                                        if (!root.exists()) {
                                            root.mkdirs();
                                        }

                                        //It will move the file to the path and renamed it
                                        boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                        if (isNameChanged) {
                                            AppModel.getInstance().appendLog(context, "In uploadUserImages Method. Image name changed in local.");
                                        }

                                        AppModel.getInstance().appendLog(context, "In uploadUserImages Method. Image path changed in local.");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
//                                } else {
//                                    sourceFilename.delete();
//                                }
                            }

                        }
                    } else {
                        failureCount++;
                        error_count++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                        if (error_count == 3) {
                            isSyncSuccessfull = false;
                            break;
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadUserImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadUserImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void uploadSeparationImage() {
        try {
            AppModel.getInstance().appendLog(context, "In UploadSeparationImage Method");
            SeparationAttachmentsModel separationAttachmentsModel = new SeparationAttachmentsModel();
            List<SeparationAttachmentsModel> imagesList = EmployeeHelperClass.getInstance(context).getSeparationImagesForUpload(0);
            if (imagesList.size() == 0) {
                AppModel.getInstance().appendLog(context, "No Separation Images found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(imagesList.size(),
                    "Separation Image", AppConstants.IMAGE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.HRResignationModuleValue));
                appSyncStatusModel.setSubModule("Separation Image");
            }


            int uploadedCount = 0;

            for (SeparationAttachmentsModel sim : imagesList) {
                try {
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadSeparationImage Method.Uploading Image: resignation id:" + sim.getResignationID());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    int responseCode = connectionClass.uploadFile(sim.getSeparationAttachment(),
                            context.getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_SEPARATION_IMAGES + "?id=" + sim.getResignationID());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadSeparationImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(sim.getSeparationAttachment()).length();

                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");
                        String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(SeparationAttachment, fdir + "/" + name);
                        values.put(EmployeeHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long id = EmployeeHelperClass.getInstance(context).updateTableColumns(
                                EmployeeHelperClass.getInstance(context).TABLE_SeparationImages, values,
                                EmployeeHelperClass.getInstance(context).Employee_Resignation_Id + " = " + sim.getResignationID() + " AND " +
                                        EmployeeHelperClass.getInstance(context).SeparationAttachment + " = '" + sim.getSeparationAttachment() + "'");
                        if (id > 0) {

                            //for progress update
                            uploadedCount++;

                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In UploadSeparationImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(sim.getSeparationAttachment());
                            if (sourceFilename.exists()) {
//                                if (eim.getFiletype().toLowerCase().equals("p")) {
                                File updatedFileName = new File(fdir + "/" + name);
                                if (!updatedFileName.exists()) {
                                    try {
                                        String rootPath = updatedFileName.getParent();
                                        String fileName = updatedFileName.getName();


                                        assert rootPath != null;
                                        File root = new File(rootPath);
                                        if (!root.exists()) {
                                            root.mkdirs();
                                        }

                                        //It will move the file to the path and renamed it
                                        boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                        if (isNameChanged) {
                                            AppModel.getInstance().appendLog(context, "In UploadSeparationImage Method. Image name changed in local.");
                                        }

                                        AppModel.getInstance().appendLog(context, "In UploadSeparationImage Method. Image path changed in local.");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
//                                } else {
//                                    sourceFilename.delete();
//                                }
                            }

                        }
                    } else {
                        failureCount++;
                        error_count++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                        if (error_count == 3) {
                            isSyncSuccessfull = false;
                            break;
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void DownloadUserImages(int schoolId) {


        try {

            ArrayList<UserImageModel> allUserImagesList = EmployeeHelperClass.getInstance(context).getAllUserImagesForDownload();

            if (allUserImagesList != null) {

                long startTime = 0;
                startTime = System.currentTimeMillis();
                syncingLogsTime(startTime, schoolId, "DownloadUserImages", true);

                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(0,
                        "User Image", AppConstants.IMAGE_MODULE, "Downloading", schoolId);

                final int[] downloadedCount = {0};
                int totalSize = 0;

                for (UserImageModel image : allUserImagesList) {
                    if (image.getUser_image_path() != null) {
                        final File picture = new File(image.getUser_image_path());
                        if (!picture.exists()) {
                            totalSize++;
                            syncDownloadUploadModel.setTotalFileSize(totalSize);
                            String imagePath = image.getUser_image_path();
                            String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + imagePath.substring(imagePath.indexOf("HumanResources"));
                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    //Update sync progress
                                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount[0], false);

                                    Log.d("imageDownloadError", error.getMessage());
                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    downloadedCount[0]++;

                                    //Update sync progress
                                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount[0], false);

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
                }

                syncingLogsTime(startTime, schoolId, "DownloadUserImages", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void syncHelpData(Context context) {
        String token = "Bearer " + AppModel.getInstance().getToken(context);
        String modified_on = HelpHelperClass.getInstance(context).getLatestModifiedOn();

        AppModel.getInstance().appendLog(context, "\nSyncing Help Data");

        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<HelpDataResponseModel> call = apiInterface.getHelpData(modified_on, token);
        try {
            Response<HelpDataResponseModel> hdrmResponse = call.execute();
            AppModel.getInstance().appendLog(context, "Help Data Sync Called got Response code :" + hdrmResponse.code() + "\n");
            if (hdrmResponse.isSuccessful()) {
                final HelpDataResponseModel hdrm = hdrmResponse.body();
                isSyncSuccessfull = true;
                if (hdrm != null) {
                    try{
                        Thread SyncHelpDataThread = new Thread(() -> {
                            AppModel.getInstance().appendLog(context, "Sync Help Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");
//                        SyncFeedback(hdrm.getFeedback());
                            SyncUserManual(hdrm.getUserManual());
                            SyncPolicies(hdrm.getPolicies());
                            SyncFAQs(hdrm.getFAQs());
                            AppModel.getInstance().appendLog(context, "Sync Help Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                        });
                        SyncHelpDataThread.start();
//                        SyncHelpDataThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"syncHelpData Error: "+e.getMessage());
                    }
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "Help Data Sync Called got Response code :" + hdrmResponse.code() + " Message " + hdrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Help data " + e.getMessage());
        }
    }

    public void SyncFeedback(ArrayList<FeedbackModel> feedbackModels) {
        if (feedbackModels == null || feedbackModels.size() == 0)
            return;

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(feedbackModels.size(),
                "Feedback", AppConstants.HELP_MODULE, "Downloading", 0);

        AppModel.getInstance().appendLog(context, "In SyncTCTPhase Method");
        HelpHelperClass.getInstance(context).addFeedback(feedbackModels, syncDownloadUploadModel);
    }

    private void SyncUserManual(ArrayList<UserManualModel> userManualModels) {
        if (userManualModels == null || userManualModels.size() == 0)
            return;

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(userManualModels.size(),
                "User Manual", AppConstants.HELP_MODULE, "Downloading", 0);

        AppModel.getInstance().appendLog(context, "In SyncUserManual Method");
        HelpHelperClass.getInstance(context).addOrUpdateUserManual(userManualModels, syncDownloadUploadModel);
    }

    private void SyncPolicies(ArrayList<UserManualModel> policiesList) {
        if (policiesList == null || policiesList.size() == 0)
            return;

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(policiesList.size(),
                "Policies", AppConstants.HELP_MODULE, "Downloading", 0);

        AppModel.getInstance().appendLog(context, "In SyncPolicies Method");
        HelpHelperClass.getInstance(context).addOrUpdatePolicies(policiesList, syncDownloadUploadModel);
    }

    private void SyncFAQs(ArrayList<FAQsModel> faqsModels) {
        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(faqsModels.size(),
                "FAQs", AppConstants.HELP_MODULE, "Downloading", 0);

        AppModel.getInstance().appendLog(context, "In SyncFAQs Method");
        HelpHelperClass.getInstance(context).addOrUpdateFAQs(faqsModels, syncDownloadUploadModel);
    }

    public void UploadFeedback() {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadFeedback Method");
            UploadFeedbackModel feedbackModel = new UploadFeedbackModel();

            feedbackModel.setFeedbackModelList(HelpHelperClass.getInstance(context).getAllFeedbackForUpload());
            if (feedbackModel.getFeedbackModelList() == null || feedbackModel.getFeedbackModelList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Feedback record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(feedbackModel.getFeedbackModelList().size(),
                    "Feedback", AppConstants.HELP_MODULE, "Uploading", 0);

            int uploadedCount = 0;

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadFeedback(feedbackModel.getFeedbackModelList(), token);
            Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
            if (response.isSuccessful()) {
                error_count = 0;
                isSyncSuccessfull = true;

                AppModel.getInstance().appendLog(context, "Feedback Uploaded Successfully Response Code: " + response.code());
                assert response.body() != null;
                for (GeneralUploadResponseModel model : response.body()) {

                    ContentValues values = new ContentValues();
                    values.put(HelpHelperClass.getInstance(context).Uploaded_On, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long id = EmployeeHelperClass.getInstance(context).updateTableColumns(
                            HelpHelperClass.getInstance(context).TABLE_FEEDBACK,
                            values, HelpHelperClass.getInstance(context).ID + " = " + model.device_id);
                    if (id > 0) {
                        AppModel.getInstance().appendLog(context, "In UploadFeedback Method.Feedback Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                        uploadedCount++;
                    }

                    if (model.ErrorMessage != null && !model.ErrorMessage.isEmpty()) {
                        AppModel.getInstance().appendLog(context, "Feedback Error Message Successful Code: " + model.ErrorMessage);
                        AppModel.getInstance().appendErrorLog(context, "Feedback Error Message After successful upload: " + model.ErrorMessage);
                    }

                    //Update sync progress
                    syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
                }

            } else {
                areAllServicesSuccessful = false;
                error_count++;
                failureCount++;
                AppModel.getInstance().appendErrorLog(context, "In uploadFeedback responseCode " + response.code() + " message: " + response.message());
                if (error_count >= 3) {
                    isSyncSuccessfull = false;
                }
            }
        } catch (Exception e) {
            failureCount++;
            areAllServicesSuccessful = false;
            isSyncSuccessfull = false;
            AppModel.getInstance().appendErrorLog(context, "In uploadFeedback " + e.getLocalizedMessage());
            AppModel.getInstance().appendErrorLog(context, "In UploadFeedback Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void downloadFeeTransactionCSV(int schoolId, String tableName, AppSyncStatusModel appSyncStatusModel) {
        try {
            String dirPath = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Documents/";
//            String fileUrl = AppConstants.URL_GET_FEES_TRANSACTION_CSV;
            String token = "Bearer " + AppModel.getInstance().getToken(context);
            final int BUFFER_SIZE = 4096;

            String fileName = tableName + ".zip";

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.downloadFeeTransactionCSV(schoolId, tableName, token);
            try {
                final Response<ResponseBody> response = call.execute();
                final ResponseBody body = response.body();
                if (response.isSuccessful()) {
                    isSyncSuccessfull = true;
                    isFinanceSyncSuccessful = true;

                    appSyncStatusModel.setDownloaded(appSyncStatusModel.getDownloaded()
                            + AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));

                    try {
                        final String[] fileNameInZip = {""};
                        assert body != null;
                        InputStream inputStream = body.byteStream();

                        File zipFile = null;

                        File folder = new File(dirPath, fileName);
                        if (!folder.exists()) {
                            String f = folder.getParent();
                            File fo = new File(f);
                            //If directory not exists then create directory
                            if (!fo.exists())
                                fo.mkdir();

                            zipFile = new File(fo, fileName);
                            try {
                                zipFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(context, "Exception occur in downloadFeeTransactionCSV method when creating zip file:" + e.getMessage());
                            }


                        } else {
                            if (folder.delete()) {
                                AppModel.getInstance().appendLog(context, tableName + " Csv file is deleted to download new records");
                                return;
                            }
                        }

                        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
                        long totalSize = body.contentLength();

                        if (totalSize > 0) {

                            Thread unzipCSVThread = new Thread(() -> {
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int bufferLength = 0;
                                try {
                                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                                        fileOutputStream.write(buffer, 0, bufferLength);
                                    }

                                    //Unziping zip file
                                    String zipFilePath = folder.getAbsolutePath(); // sdcard:/TCF/TCF Documents/header.zip
                                    String destDirectory = folder.getParent(); // sdcard:/TCF/TCF Documents
                                    UnzipUtility unzipper = new UnzipUtility();

                                    fileNameInZip[0] = unzipper.unzip(zipFilePath, destDirectory);

                                    //Temporary create copy of csv file so tester can check it please don't delete this code
                                    String newCopyFile = destDirectory + File.separator + tableName + schoolId + ".csv"; //sdcard:/TCF/TCF Documents/header1065.csv or detail1065.csv
                                    String sourceFile = destDirectory + File.separator + fileNameInZip[0]; // sdcard:/TCF/TCF Documents/header.csv or detail.csv
                                    unzipper.createCopyOfCSVAndRenameIt(new File(newCopyFile), new File(sourceFile));

                                } catch (Exception ex) {
                                    // some errors occurred
                                    ex.printStackTrace();
                                    AppModel.getInstance().appendErrorLog(context, "In downloadFeeTransactionCSV in unzipCSVThread  Error " + ex.getMessage() + "\n");
                                }
                            });
                            unzipCSVThread.start();
                            unzipCSVThread.join();

                            //Put downloaded data into tables
                            Thread insertFeeThread = new Thread(() -> {
                                String csvFullPath = folder.getParent() + File.separator + fileNameInZip[0];
                                try {
                                    if (tableName.equals(AppConstants.TABLE_NAME_HEADER_FOR_CSV)) {
                                        List<SyncFeesHeaderModel> feesHeaderModelList = getFeesHeaderFromCSV(csvFullPath);

                                        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(feesHeaderModelList.size(),
                                                "Fee Header", AppConstants.FINANCE_MODULE, "Downloading", schoolId);

                                        FeesCollection.getInstance(context).addCSVDownloadedFeesHeader(feesHeaderModelList, schoolId, syncDownloadUploadModel);

                                        //delete csv and zip file
                                        deleteCSVAndZipFile(AppConstants.TABLE_NAME_HEADER_FOR_CSV, csvFullPath, folder);

                                    } else if (tableName.equals(AppConstants.TABLE_NAME_DETAIL_FOR_CSV)) {
                                        int downloadedCount = 0;

                                        ArrayList<FeesDetailUploadModel> feesDetailModelList = getFeesDetailFromCSV(csvFullPath);

                                        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(feesDetailModelList.size(),
                                                "Fee Detail", AppConstants.FINANCE_MODULE, "Downloading", schoolId);

                                        SQLiteDatabase DB = DatabaseHelper.getInstance(context).getDB();
                                        try {
//                                                DB.beginTransaction();      //it is commented because it is lagging the app
                                            for (FeesDetailUploadModel model : feesDetailModelList) {
                                                FeesHeaderModel feesHeaderModel = FeesCollection.getInstance(context).getFeesHeader(schoolId, model.getFeeHeader_id(), DB);

                                                long id = FeesCollection.getInstance(context).addCSVDownloadedFeesDetail(feesHeaderModel.getId(), model, DB);
                                                if (id > 0) {
                                                    downloadedCount++;
                                                }

                                                //Update sync progress
                                                syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                                            }
//                                                DB.setTransactionSuccessful();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            AppModel.getInstance().appendErrorLog(context, "In downloadFeeTransactionCSV feedetail insertion Error " + e.getMessage() + "\n");
                                        } finally {
//                                                DB.endTransaction();
                                        }

                                        //delete csv and zip file
                                        deleteCSVAndZipFile(AppConstants.TABLE_NAME_DETAIL_FOR_CSV, csvFullPath, folder);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    AppModel.getInstance().appendErrorLog(context, "In downloadFeeTransactionCSV insertFeeThread Error " + e.getMessage() + "\n");
                                }
                            });
                            insertFeeThread.start();
                            insertFeeThread.join();


                        } else {
                            AppModel.getInstance().appendErrorLog(context, "Error in downloadFeeTransactionCSV api content-length :" + totalSize);
                        }
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "In downloadFeeTransactionCSV Fees Header Sync after successfuly got Response code :" + response.code() + " Error " + e.getMessage() + "\n");

                        deleteZipFile(dirPath, fileName, fileName);
                    }

                } else {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    isFinanceSyncSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In downloadFeeTransactionCSV Fees Header Sync Called got Response code :" + response.code() + " Message " + response.message() + "\n");
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                isFinanceSyncSuccessful = false;

                deleteZipFile(dirPath, fileName, fileName);

                failureCountForAllModules++;
                e.printStackTrace();
                AppModel.getInstance().appendErrorLog(context, "Error in downloadFeeTransactionCSV method:" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "Error in downloadFeeTransactionCSV method:" + e.getMessage());
        }
    }

    private void deleteZipFile(String dirPath, String fileName, String tableName) {
        if (new File(dirPath, fileName).exists()) {
            if (new File(dirPath, fileName).delete()) {
                AppModel.getInstance().appendLog(context, tableName + " Csv file is deleted to download new records");
            }
        }
    }

    private void deleteCSVAndZipFile(String tableName, String csvFullPath, File folder) {
        File csvFile = new File(csvFullPath);
        if (csvFile.exists()) {
            if (csvFile.delete())
                AppModel.getInstance().appendLog(context, tableName + " csv deleted for new one to extract");
        }

        if (folder.delete()) {
            AppModel.getInstance().appendLog(context, tableName + " zip file deleted from:" + folder.getAbsolutePath());
        }
    }

    private List<SyncFeesHeaderModel> getFeesHeaderFromCSV(String filePath) {
        List<SyncFeesHeaderModel> feesHeaderModelList = new ArrayList<>();
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader buffer = new BufferedReader(file);
            String line = "";

            //Step over headers
            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                //Split by ","
                String[] tokens = line.split(",");

                //Read the data
                SyncFeesHeaderModel model = new SyncFeesHeaderModel();
                String id = tokens[0],
                        studentId = tokens[1],
                        academicSessionId = tokens[2],
                        forDate = tokens[3],
                        totalAmount = tokens[4],
                        transactionTypeId = tokens[5],
                        categoryId = tokens[6],
                        receiptNo = tokens[7],
                        schoolClassId = tokens[8],
                        cashDepositId = tokens[9],
                        createdBy = tokens[10],
                        createdOn = tokens[11],
                        modifiedOn = tokens[14],
                        createdOn_Server = tokens[12];

                if (id.length() > 0)
                    model.setId(Integer.parseInt(id));

                if (studentId.length() > 0)
                    model.setStudentId(Integer.parseInt(studentId));

                if (academicSessionId.length() > 0)
                    model.setAcademicSession_id(Integer.parseInt(academicSessionId));

                model.setForDate(forDate);

                if (totalAmount.length() > 0)
                    model.setTotalAmount(Double.parseDouble(totalAmount));

                if (transactionTypeId.length() > 0)
                    model.setTransactionType_id(Integer.parseInt(transactionTypeId));

                if (categoryId.length() > 0)
                    model.setCategory_id(Integer.parseInt(categoryId));

                model.setReceiptNo(receiptNo);

                if (schoolClassId.length() > 0)
                    model.setSchoolClassId(Integer.parseInt(schoolClassId));

                model.setCashDeposit_id(cashDepositId);

                if (createdBy.length() > 0)
                    model.setCreatedBy(Integer.parseInt(createdBy));


                model.setCreatedOn(createdOn);
                model.setCreatedOn_Server(createdOn_Server);

                model.setModified_on(modifiedOn);

//                model.setFdmList(getFeesDetailFromCSV(filePath));

                feesHeaderModelList.add(model);

            }

        } catch (IOException e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In getFeesHeaderFromCSV Error:" + e.getMessage());
        } finally {
            if (feesHeaderModelList != null) {
                AppModel.getInstance().appendLog(context, "In getFeesHeaderFromCSV list size:" + feesHeaderModelList.size());
            }
        }
        return feesHeaderModelList;
    }

//    private void downloadFeeDetailCSV(){
//        try {
//            String dirPath = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Documents/";
//            String fileUrl = AppConstants.URL_GET_FEES_TRANSACTION_CSV;
//
//            String fileName = new File(fileUrl).getName();
//            File folder = new File(dirPath, fileName);
//            if (!folder.exists()) {
//                String f = folder.getParent();
//                File fo = new File(f);
//                //If directory not exists then create directory
//                if (!fo.exists())
//                    fo.mkdir();
//
//                File csvFile = new File(fo, fileName);
//                try {
//                    csvFile.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
//                Call<ResponseBody> call = apiInterface.downloadFeeTransactionCSV(fileUrl);
//                try {
//                    final Response<ResponseBody> response = call.execute();
//                    final ResponseBody body = response.body();
//                    if (response.isSuccessful()) {
//                        try {
//                            assert body != null;
//                            InputStream inputStream = body.byteStream();
//                            FileOutputStream fileOutputStream = new FileOutputStream(csvFile);
//                            long totalSize = body.contentLength();
//
//                            byte[] buffer = new byte[4096];
//                            int bufferLength = 0;
//                            while ((bufferLength = inputStream.read(buffer)) > 0) {
//                                fileOutputStream.write(buffer, 0, bufferLength);
//                            }
//                            fileOutputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private ArrayList<FeesDetailUploadModel> getFeesDetailFromCSV(String filePath) {
        ArrayList<FeesDetailUploadModel> feesDetailModelList = new ArrayList<>();
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader buffer = new BufferedReader(file);
            String line = "";

            //Step over headers
            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                //Split by ","
                String[] tokens = line.split(",");

                //Read the data
                FeesDetailUploadModel model = new FeesDetailUploadModel();
                String feeHeaderId = tokens[1],
                        amount = tokens[2],
                        feeTypeId = tokens[3];

                if (feeHeaderId.length() > 0)
                    model.setFeeHeader_id(Integer.parseInt(feeHeaderId));

                if (amount.length() > 0)
                    model.setAmount(Integer.parseInt(amount));

                if (feeTypeId.length() > 0)
                    model.setFeeType_id(Integer.parseInt(feeTypeId));

                feesDetailModelList.add(model);

            }

        } catch (IOException e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(context, "In getFeesDetailFromCSV Error:" + e.getMessage());
        } finally {
            if (feesDetailModelList != null) {
                AppModel.getInstance().appendLog(context, "In getFeesDetailFromCSV list size:" + feesDetailModelList.size());
            }
        }
        return feesDetailModelList;
    }

    private static final int MEGABYTE = 1024 * 1024;

    public void setAverageDownloadTimeAndLatecy(NetworkConnectionInfo connectionInfo, List<String> urls) {
        final double[] rateBytesPerSecond = {0};
        final double[] meanSpeed = {0};
        final double[] totalTimeDifference = {0};
        for (String url : urls) {
            try {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.downloadAnyFile(url);

                long elapsedTime;

                long startTime = System.currentTimeMillis();
//                long startTime = System.nanoTime();
                final Response<ResponseBody> response = call.execute();
                final ResponseBody body = response.body();
                if (response.isSuccessful()) {

                    long endTime = System.currentTimeMillis();
//                    long endTime = System.nanoTime();

                    assert body != null;
                    try {
                        String fileName = new File(url).getName();
                        String path = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/" + fileName;
                        File folder = new File(path);
                        if (!folder.exists()) {
                            String f = folder.getParent();
                            File fo = new File(f);
                            if (!fo.exists())
                                fo.mkdir();

                            File imgFile = new File(fo, fileName);

                            try {
                                imgFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            InputStream inputStream = body.byteStream();
                            FileOutputStream fileOutputStream = new FileOutputStream(imgFile);

                            byte[] buffer = new byte[MEGABYTE];
                            int bufferLength = 0;
                            while ((bufferLength = inputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, bufferLength);
                            }
                            fileOutputStream.close();
                        }

                        File imgFile = new File(path);
                        if (imgFile.exists()) {
                            long totalSize = imgFile.length();  //bytes

                            // TimeUnit in miliseconds
                            elapsedTime = endTime - startTime;
//                            elapsedTime = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
                            totalTimeDifference[0] += elapsedTime;

                            if (elapsedTime > 0) {
//                                rateBytesPerSecond[0] += totalSize / elapsedTime;  //  bytes/seconds
                                meanSpeed[0] += (float) totalSize / ((float) elapsedTime / 1000);  //mean speed in bytes/second
                            }
                        }
                    } catch (IOException e) {
                        AppModel.getInstance().appendErrorLog(context, "Exception in setAverageDownloadTimeAndLatecy method after successfull responce code Error:" + e.getMessage());
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception in setAverageDownloadTimeAndLatecy method Error:" + e.getMessage());
                e.printStackTrace();
            }
        }

        double totalDownloadRates = meanSpeed[0];
//        double totalDownloadRates = rateBytesPerSecond[0];
        double averageDownloadTime = totalDownloadRates / 3;

        connectionInfo.setDownloadSpeed(averageDownloadTime);
        connectionInfo.setLatency(totalTimeDifference[0]);
    }

    public void setAverageUploadTimeAndLatency(NetworkConnectionInfo connectionInfo, List<String> images) {
        final double[] rateBytesPerSecond = {0};
        final double[] meanSpeed = {0};
        final double[] totalTimeDifference = {0};
        for (String image : images) {
            try {
                String token = "Bearer " + AppModel.getInstance().getToken(context);
                String path = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/" + image;

                File imgFile = new File(path);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", imgFile.getName(), RequestBody.create(MediaType.parse("image/*"),imgFile));

                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.uploadFileForTestConnectionInfo(filePart, token);

                long elapsedTime;
                long startTime = System.currentTimeMillis();
//                long startTime = System.nanoTime();

                final Response<ResponseBody> response = call.execute();
                final ResponseBody body = response.body();
                if (response.isSuccessful()) {
                    long endTime = System.currentTimeMillis();
//                    long endTime = System.nanoTime();

                    assert body != null;
                    long totalSize = imgFile.length(); //bytes

                    // TimeUnit into miliseconds
                    elapsedTime = endTime - startTime;
//                    elapsedTime = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
                    totalTimeDifference[0] += elapsedTime;

                    if (elapsedTime > 0) {
                        meanSpeed[0] += (float) totalSize / ((float) elapsedTime / 1000);  //mean speed in bytes/second
//                        rateBytesPerSecond[0] += totalSize / elapsedTime; //  bytes/seconds
                    }

                    //Delete images after upload to save space
                    if (imgFile.exists())
                        imgFile.delete();
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception in setAverageUploadTimeAndLatency method Error:" + e.getMessage());
                e.printStackTrace();
            }
        }

        double totalUploadRates = meanSpeed[0];
//        double totalUploadRates = rateBytesPerSecond[0];
        double averageUploadTime = totalUploadRates / 3;
        double avgLatency = (connectionInfo.getLatency() + totalTimeDifference[0]) / 6;

        connectionInfo.setUploadSpeed(averageUploadTime);
        connectionInfo.setLatency(avgLatency);
    }

    public void syncExpenseMetaDataForSingleSchool(final Context context, SchoolModel model) {
        AppModel.getInstance().appendLog(context, "\nSyncing Expense Metadata for single school");
        String modified_on = ExpenseHelperClass.getInstance(context).getLatestModifiedOnExpenseMetaData(model.getId());
        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

      Call<ExpenseDataResponseModel> call = apiInterface.getExpensesMetaData(model.getId(), modified_on, token);

        try {
            Response<ExpenseDataResponseModel> edrmResponse = call.execute();
            AppModel.getInstance().appendLog(context, "Expense Metadata for single school Sync Called got Response code :" + edrmResponse.code() + "\n");
            if (edrmResponse.isSuccessful()) {
                final ExpenseDataResponseModel edrm = edrmResponse.body();
                isSyncSuccessfull = true;
                if (edrm != null) {
                    try{
                        Thread SyncExpenseMetaDataForSingleSchoolThread = new Thread(() -> {
                          /*  List<String> allowedModules = null;
                            if (model.getAllowedModule_App() != null){
                                allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                            }*/
                            AppModel.getInstance().appendLog(context, "Sync Expense Metadata for single school Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                          /*  if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                          /*  if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                SyncEmployeeTeacherLeave(edrm.getTeacherLeave(), model.getId());
                            }*/
                            SyncHeads(edrm.getHeads(), model.getId());
                            SyncSubHeads(edrm.getSubheads(), model.getId());
                            SyncExpenseTransactionBucket(edrm.getTransactionBucket(), model.getId());
                            SyncExpenseTransactionCategory(edrm.getTransactionCategory(), model.getId());
                            SyncExpenseTransactionFlow(edrm.getTransactionFlow(), model.getId());

                            AppModel.getInstance().appendLog(context, "Sync Expense Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                        });
                        SyncExpenseMetaDataForSingleSchoolThread.start();
//                        SyncExpenseMetaDataForSingleSchoolThread.join();
                    }catch (Exception e){
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"syncExpenseMetaDataForSingleSchool Error: "+e.getMessage());
                    }
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "Expense Metadata for single school Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                isSyncSuccessfull = false;
                areAllServicesSuccessful = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Expense Metadata for single school " + e.getMessage());
        }

    }


    public void syncExpenseMetaData(final Context context) {
        //TODO getAllUserSchoolsforExpense
        List<SchoolModel> schoolModels = DatabaseHelper.getInstance(context).getAllUserSchoolsForExpense();
        for (final SchoolModel model : schoolModels) {
            AppModel.getInstance().appendLog(context, "\nSyncing Expense Data");
            String modified_on = ExpenseHelperClass.getInstance(context).getLatestModifiedOnExpenseMetaData(model.getId());
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

            Call<ExpenseDataResponseModel> call = apiInterface.getExpensesMetaData(model.getId(), modified_on, token);
//
            try {
                Response<ExpenseDataResponseModel> edrmResponse = call.execute();
                AppModel.getInstance().appendLog(context, "Expense Data Sync Called got Response code :" + edrmResponse.code() + "\n");
                if (edrmResponse.isSuccessful()) {
                    final ExpenseDataResponseModel edrm = edrmResponse.body();
                    isSyncSuccessfull = true;
                    if (edrm != null) {
                        try{
                            Thread SyncExpenseMetaDataThread = new Thread(() -> {
                          /*  List<String> allowedModules = null;
                            if (model.getAllowedModule_App() != null){
                                allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                            }*/
                                AppModel.getInstance().appendLog(context, "Sync Expense Data Downloading started at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a") + "\n");

                          /*  if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                          /*  if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                                SyncEmployeeTeacherLeave(edrm.getTeacherLeave(), model.getId());
                            }*/
                                SyncHeads(edrm.getHeads(), model.getId());
                                SyncSubHeads(edrm.getSubheads(), model.getId());
                                SyncExpenseTransactionBucket(edrm.getTransactionBucket(), model.getId());
                                SyncExpenseTransactionCategory(edrm.getTransactionCategory(), model.getId());
                                SyncExpenseTransactionFlow(edrm.getTransactionFlow(), model.getId());

                                AppModel.getInstance().appendLog(context, "Sync Expense Data Downloading completed at: " + AppModel.getInstance().getCurrentDateTime(" dd-MM-yyyy hh:mm:ss a"));

                            });
                            SyncExpenseMetaDataThread.start();
//                            SyncExpenseMetaDataThread.join();
                        }catch (Exception e){
                            e.printStackTrace();
                            AppModel.getInstance().appendErrorLog(context,"syncExpenseMetaData Error: "+e.getMessage());
                        }
                    }
                } else {
                    AppModel.getInstance().appendErrorLog(context, "Expense Data Sync Called got Response code :" + edrmResponse.code() + " Message " + edrmResponse.message() + "\n");
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;

                }
            } catch (IOException e) {
                e.printStackTrace();
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "Exception Occurred while syncing Expense data " + e.getMessage());
            }
        }
    }


    public void SyncHeads(ArrayList<ExpenseHeadsModel> eqdList, int schoolId) {
        if (eqdList == null || eqdList.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncHeads Method");
        AppModel.getInstance().appendLog(context, "Total Entries in Heads" + eqdList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(eqdList.size(),
                "Heads", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseHeadsModel expenseHeadsModel : eqdList) {
            try {
                expenseHeadsModel.setSchoolId(schoolId);
                if (!ExpenseHelperClass.getInstance(context).FindHeads(expenseHeadsModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addHeadRecord(expenseHeadsModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncQualificationHistory Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);
    }

    public void SyncSubHeads(ArrayList<ExpenseSubHeadsModel> shList, int schoolId) {
        if (shList == null || shList.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncSubHeads Method");
        AppModel.getInstance().appendLog(context, "Total Entries in SubHeads" + shList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(shList.size(),
                "SubHeads", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseSubHeadsModel subHeadsModel : shList) {
            try {
                subHeadsModel.setSchoolId(schoolId);
                if (!ExpenseHelperClass.getInstance(context).FindSubHeads(subHeadsModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addSubHeadRecord(subHeadsModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSubHeads Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);
    }

    public void SyncSubheadLimits(ArrayList<ExpenseSubheadLimitsModel> shList, int schoolId) {
        if (shList == null || shList.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncSubheadLimits Method");
        AppModel.getInstance().appendLog(context, "Total Entries in SubHeadsLimits" + shList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(shList.size(),
                "SubheadLimits", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseSubheadLimitsModel subheadLimitsModel : shList) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindSubHeadLimits(subheadLimitsModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addSubHeadLimitsRecord(subheadLimitsModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSubheadLimits Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);
    }

    public void SyncSubheadLimitsMonthly(ArrayList<ExpenseSubheadLimitsMonthlyModel> shmList, int schoolId) {

        if (shmList == null || shmList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncSubheadLimitsMonthly", true);

        AppModel.getInstance().appendLog(context, "Total Entries in SubHeadsLimitsMonthly" + shmList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(shmList.size(),
                "SubheadLimitsMonthly", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseSubheadLimitsMonthlyModel subheadLimitsMonthlyModel : shmList) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindSubHeadLimitsMonthly(subheadLimitsMonthlyModel)) {
                    subheadLimitsMonthlyModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = ExpenseHelperClass.getInstance(context).addSubHeadLimitsMonthlyRecord(subheadLimitsMonthlyModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSubheadLimitsMonthly Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);

        syncingLogsTime(startTime, schoolId, "SyncSubheadLimitsMonthly", false);
    }


    public void SyncSubheadExceptions(ArrayList<ExpenseSubheadExceptionLimitsModel> sheList, int schoolId) {
        if (sheList == null || sheList.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncSubheadExceptions", true);

        AppModel.getInstance().appendLog(context, "Total Entries in SubHeadExceptions" + sheList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(sheList.size(),
                "SubheadExceptions", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseSubheadExceptionLimitsModel subheadExceptionLimitsModel : sheList) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindSubHeadExceptions(subheadExceptionLimitsModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addSubHeadExceptions(subheadExceptionLimitsModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSubheadExceptions Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);
        syncingLogsTime(startTime, schoolId, "SyncSubheadExceptions", false);
    }


    public void SyncSchoolPettyCashLimits(ArrayList<ExpenseSchoolPettyCashLimitsModel> spcList, int schoolId) {
        if (spcList == null || spcList.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncSchoolPettyCashLimits Method");
        AppModel.getInstance().appendLog(context, "Total Entries in SchoolPettyCashLimits" + spcList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(spcList.size(),
                "SchoolPettyCashLimits", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseSchoolPettyCashLimitsModel schoolPettyCashLimitsModel : spcList) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindSchoolPettyCashLimits(schoolPettyCashLimitsModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addSchoolPettyCashLimits(schoolPettyCashLimitsModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSchoolPettyCashLimits Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);
    }


    public void SyncSchoolPettyCashMonthlyLimits(ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> spcmList, int schoolId) {

        if (spcmList == null || spcmList.size() == 0)
            return;

        long startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncSchoolPettyCashMonthlyLimits", true);

        AppModel.getInstance().appendLog(context, "Total Entries in SchoolPettyCashMonthlyLimits" + spcmList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(spcmList.size(),
                "SchoolPettyCashMonthlyLimits", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseSchoolPettyCashMonthlyLimitsModel schoolPettyCashMonthlyLimitsModel : spcmList) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindSchoolPettyCashMonthlyLimits(schoolPettyCashMonthlyLimitsModel)) {
                    schoolPettyCashMonthlyLimitsModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = ExpenseHelperClass.getInstance(context).addSchoolPettyCashMonthlyLimits(schoolPettyCashMonthlyLimitsModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncSchoolPettyCashMonthlyLimits Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
        //ExpenseHelperClass.getInstance(context).removeHeadRecordNotExists(schoolId, HeadsModel);

        syncingLogsTime(startTime, schoolId, "SyncSchoolPettyCashMonthlyLimits", false);
    }

    public void SyncExpenseTransactionCategory(ArrayList<ExpenseTransactionCategoryModel> spcmList, int schoolId) {
        if (spcmList == null || spcmList.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncExpenseTransactionCategory Method");
        AppModel.getInstance().appendLog(context, "Total Entries in ExpenseTransactionCategory" + spcmList.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(spcmList.size(),
                "ExpenseTransactionCategory", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseTransactionCategoryModel transactionCategoryModel : spcmList) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindExpenseTransactionCategory(transactionCategoryModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addExpenseTransactionCategory(transactionCategoryModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncExpenseTransactionCategory Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
    }

    public void SyncExpenseTransactionBucket(ArrayList<ExpenseTransactionBucketModel> models, int schoolId) {
        if (models == null || models.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncExpenseTransactionBucket Method");
        AppModel.getInstance().appendLog(context, "Total Entries in ExpenseTransactionBucket" + models.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(models.size(),
                "ExpenseTransactionBucket", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseTransactionBucketModel transactionBucketModel : models) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindExpenseTransactionBucket(transactionBucketModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addExpenseTransactionBucket(transactionBucketModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncExpenseTransactionBucket Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
    }

    public void SyncExpenseTransactionFlow(ArrayList<ExpenseTransactionFlowModel> models, int schoolId) {
        if (models == null || models.size() == 0)
            return;

        AppModel.getInstance().appendLog(context, "In SyncExpenseTransactionFlow Method");
        AppModel.getInstance().appendLog(context, "Total Entries in ExpenseTransactionFlow" + models.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(models.size(),
                "ExpenseTransactionFlow", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseTransactionFlowModel transactionFlowModel : models) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindExpenseTransactionFlow(transactionFlowModel)) {
                    long i = ExpenseHelperClass.getInstance(context).addExpenseTransactionFlowRecord(transactionFlowModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    downloadedCount++;
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncExpenseTransactionFlow Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }
    }

    public void SyncExpenseTransactions(ArrayList<ExpenseTransactionModel> models, int schoolId) {

        if (models == null || models.size() == 0)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncExpenseTransactions", true);

        AppModel.getInstance().appendLog(context, "Total Entries in ExpenseTransactions" + models.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(models.size(),
                "ExpenseTransactions", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseTransactionModel transactionModel : models) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindExpenseTransaction(transactionModel)) {
                    transactionModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = ExpenseHelperClass.getInstance(context).addExpenseTransaction(transactionModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
//                    if (ExpenseHelperClass.getInstance(context).IfTransactionRecordNotUploaded(transactionModel.getID())) {
                    transactionModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = ExpenseHelperClass.getInstance(context).updateTransactionRecord(transactionModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
//                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncExpenseTransactions Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }

        syncingLogsTime(startTime, schoolId, "SyncExpenseTransactions", false);
    }


    public void SyncExpenseTransactionImages(ArrayList<ExpenseTransactionImagesModel> models, int schoolId) {


        if (models == null)
            return;

        long startTime = 0;
        startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncExpenseTransactionImages", true);

        AppModel.getInstance().appendLog(context, "Total SyncExpenseTransactionImages:" + models.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(models.size(),
                "Expense", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseTransactionImagesModel imagesModel : models) {
            try {
                String path = imagesModel.getImagePath();
                path = path.replace("\\", "/");
                String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images/";
                imagesModel.setImagePath(fdir + path);
                if (!ExpenseHelperClass.getInstance(context).FindExpenseTransactionImages(imagesModel.getTransID(), imagesModel.getImagePath())) {
                    imagesModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = ExpenseHelperClass.getInstance(context).addExpenseTransactionImages(imagesModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (ExpenseHelperClass.getInstance(context).IfTransactionImageNotUploaded(imagesModel.getTransID(), imagesModel.getImagePath())) {
                        imagesModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                        long i = ExpenseHelperClass.getInstance(context).updateTransactionImage(imagesModel);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncExpenseTransactionImages Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }

        syncingLogsTime(startTime, schoolId, "SyncExpenseTransactionImages", false);
        /*
        if (fromSeparation)
            EmployeeHelperClass.getInstance(context).removeResignImageNotExists(schoolId, models);*/
    }
/*
    public void uploadExpenseTransactions() {

        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In uploadExpenseTransactions Method");
            ExpenseTransactionModel etm = new ExpenseTransactionModel();

            etm.setEtmList(ExpenseHelperClass.getInstance(context).getAllTransactionsForUpload());
            if (etm.getEtmList() == null || etm.getEtmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Expense Transactions record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(etm.getEtmList().size(),
                    "Expense Transactions", AppConstants.EXPENSE_MODULE,"Uploading", 0);

            int uploadedCount = 0;

            for (ExpenseTransactionModel etm1 : etm.getEtmList()) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<Boolean> call = apiInterface.uploadExpenseTransactions(etm1, token);

                try {
                    final Response<Boolean> response = call.execute();
                    if (response.isSuccessful()) {
                        error_count = 0;
                        isSyncSuccessfull = true;
                        if(response.body()){
                            AppModel.getInstance().appendLog(context, "Expense Transaction Uploading Successful Code: " + response.code());
                            try {
                                int hId = etm1.getID();
                                ContentValues values = new ContentValues();
                                values.put(ExpenseHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                long id = ExpenseHelperClass.getInstance(context).updateTableColumns(
                                        ExpenseHelperClass.TABLE_EXPENSE_TRANSACTIONS,
                                        values, ExpenseHelperClass.SERVER_ID + " = " + hId);
                                if (id > 0) {
                                    AppModel.getInstance().appendLog(context, "In uploadExpenseTransactions Method. Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                                    uploadedCount++;
                                }
                            } catch (Exception e) {
                                areAllServicesSuccessful = false;
                                AppModel.getInstance().appendErrorLog(context, "In uploadExpenseTransactions " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        } else {
                            AppModel.getInstance().appendErrorLog(context, "Expense Transaction record not updated " +response.message());
                        }
                    } else {
                        error_count++;
                        failureCount++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadExpenseTransactions responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                        if (error_count >= 3) {
                            isSyncSuccessfull = false;
                        }
                    }

                } catch (Exception e) {
                    AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading uploadExpenseTransactions " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadPendingApprovals " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }*/

    public void uploadExpenseTransactionImages(int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method");
            List<ExpenseTransactionImagesModel> imagesList = ExpenseHelperClass.getInstance(context).getTransactionImagesForUpload();
            if (imagesList.size() == 0) {
                AppModel.getInstance().appendLog(context, "No Transaction Images found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(imagesList.size(),
                    "ExpenseTransaction Images", AppConstants.IMAGE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;
            long appSyncStatusTotalUploadedSize = 0;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.ExpenseModuleValue));
                appSyncStatusModel.setSubModule("FE Images");
            }


            int uploadedCount = 0;

            for (ExpenseTransactionImagesModel sim : imagesList) {
                try {
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method.Uploading Image: transaction id:" + sim.getTransID());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    int responseCode = connectionClass.uploadFile(sim.getImagePath(),
                            context.getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_EXPENSE_TRANSACTION_IMAGE + "?ID=" + sim.getTransID() + "&imageType=" + sim.getImageType());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(sim.getImagePath()).length();

                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");
                        String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(ExpenseHelperClass.TRANSACTION_IMAGES_IMAGE_PATH, fdir + "/" + name);
                        values.put(EmployeeHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long id = ExpenseHelperClass.getInstance(context).updateTableColumns(
                                ExpenseHelperClass.TABLE_EXPENSE_TRANSACTION_IMAGES, values,
                                ExpenseHelperClass.TRANSACTIONS_IMAGES_TRANSACTION_ID + " = " + sim.getTransID() + " AND " +
                                        ExpenseHelperClass.TRANSACTION_IMAGES_IMAGE_PATH + " = '" + sim.getImagePath() + "'");
                        if (id > 0) {

                            //for progress update
                            uploadedCount++;

                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(sim.getImagePath());
                            if (sourceFilename.exists()) {
//                                if (eim.getFiletype().toLowerCase().equals("p")) {
                                File updatedFileName = new File(fdir + "/" + name);
                                if (!updatedFileName.exists()) {
                                    try {
                                        String rootPath = updatedFileName.getParent();
                                        String fileName = updatedFileName.getName();


                                        assert rootPath != null;
                                        File root = new File(rootPath);
                                        if (!root.exists()) {
                                            root.mkdirs();
                                        }

                                        //It will move the file to the path and renamed it
                                        boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                        if (isNameChanged) {
                                            AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method. Image name changed in local.");
                                        }

                                        AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method. Image path changed in local.");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
//                                } else {
//                                    sourceFilename.delete();
//                                }
                            }

                        }
                    } else {
                        failureCount++;
                        error_count++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                        if (error_count == 3) {
                            isSyncSuccessfull = false;
                            break;
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadExpenseTransactionImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void UploadExpenseSubheadMonthlyLimits(int schoolId) {

        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In uploadExpenseSubheadMonthlyLimits Method");
            ExpenseSubheadLimitsMonthlyModel etm = new ExpenseSubheadLimitsMonthlyModel();

            etm.setEslmList(ExpenseHelperClass.getInstance(context).getAllSubheadLimitsMonthlyForUpload());
            if (etm.getEslmList() == null || etm.getEslmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Expense SubheadMonthlyLimits record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(etm.getEslmList().size(),
                    "Expense SubheadMonthlyLimits", AppConstants.EXPENSE_MODULE, "Uploading", schoolId);

            int uploadedCount = 0;

            for (ExpenseSubheadLimitsMonthlyModel etm1 : etm.getEslmList()) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<Boolean> call = apiInterface.uploadExpenseSubHeadLimitsMonthly(etm1, token);

                try {
                    final Response<Boolean> response = call.execute();
                    if (response.isSuccessful()) {
                        error_count = 0;
                        isSyncSuccessfull = true;
                        if (response.body()) {
                            AppModel.getInstance().appendLog(context, "Expense SubheadMonthlyLimits Uploading Successful Code: " + response.code());
                            try {
                                int hId = etm1.getId();
                                ContentValues values = new ContentValues();
                                values.put(ExpenseHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                long id = ExpenseHelperClass.getInstance(context).updateTableColumns(
                                        ExpenseHelperClass.TABLE_EXPENSE_SUBHEAD_LIMITS_MONTHLY,
                                        values, ExpenseHelperClass.ID + " = " + hId);
                                if (id > 0) {
                                    AppModel.getInstance().appendLog(context, "In uploadSubheadMonthlyLimits Method. Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                                    uploadedCount++;
                                }
                            } catch (Exception e) {
                                areAllServicesSuccessful = false;
                                AppModel.getInstance().appendErrorLog(context, "In uploadExpenseSubheadMonthlyLimits " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        } else {
                            AppModel.getInstance().appendErrorLog(context, "Expense SubheadMonthlyLimits record not updated " + response.message());
                        }
                    } else {
                        error_count++;
                        failureCount++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadExpenseSubheadMonthlyLimits responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                        if (error_count >= 3) {
                            isSyncSuccessfull = false;
                        }
                    }

                } catch (Exception e) {
                    AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading uploadExpenseSubheadMonthlyLimits " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadExpenseSubheadMonthlyLimits " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }


    public void UploadExpenseSchoolPettyCashMonthlyLimits(int schoolId) {

        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In uploadExpenseSchoolPettyCashMonthlyLimits Method");
            ExpenseSchoolPettyCashMonthlyLimitsModel eslm = new ExpenseSchoolPettyCashMonthlyLimitsModel();

            eslm.setEslmList(ExpenseHelperClass.getInstance(context).getAllSchoolPettyCashMonthlyLimitsForUpload());
            if (eslm.getEslmList() == null || eslm.getEslmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Expense SchoolPettyCashMonthlyLimits record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(eslm.getEslmList().size(),
                    "Expense SchoolPettyCashMonthlyLimits", AppConstants.EXPENSE_MODULE, "Uploading", schoolId);

            int uploadedCount = 0;

            for (ExpenseSchoolPettyCashMonthlyLimitsModel etm1 : eslm.getEslmList()) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<Boolean> call = apiInterface.uploadExpenseSchoolPettyCashMonthlyLimits(etm1, token);

                try {
                    final Response<Boolean> response = call.execute();
                    if (response.isSuccessful()) {
                        error_count = 0;
                        isSyncSuccessfull = true;
                        if (response.body()) {
                            AppModel.getInstance().appendLog(context, "Expense SchoolPettyCashMonthlyLimits Uploading Successful Code: " + response.code());
                            try {
                                int hId = etm1.getId();
                                ContentValues values = new ContentValues();
                                values.put(ExpenseHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                long id = ExpenseHelperClass.getInstance(context).updateTableColumns(
                                        ExpenseHelperClass.TABLE_EXPENSE_SCHOOL_PETTYCASH_MONTHLY_LIMITS,
                                        values, ExpenseHelperClass.ID + " = " + hId);
                                if (id > 0) {
                                    AppModel.getInstance().appendLog(context, "In uploadExpenseSchoolPettyCashMonthlyLimits Method. Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                                    uploadedCount++;
                                }
                            } catch (Exception e) {
                                areAllServicesSuccessful = false;
                                AppModel.getInstance().appendErrorLog(context, "In uploadExpenseSchoolPettyCashMonthlyLimits " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        } else {
                            AppModel.getInstance().appendErrorLog(context, "Expense SchoolPettyCashMonthlyLimits record not updated " + response.message());
                        }
                    } else {
                        error_count++;
                        failureCount++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadExpenseSchoolPettyCashMonthlyLimits responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                        if (error_count >= 3) {
                            isSyncSuccessfull = false;
                        }
                    }

                } catch (Exception e) {
                    AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading uploadExpenseSchoolPettyCashMonthlyLimits " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadExpenseSchoolPettyCashMonthlyLimits " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }

    public void SyncAmountClosing(ArrayList<ExpenseAmountClosingModel> acm, int schoolId) {

        if (acm == null || acm.size() == 0)
            return;

        long startTime = System.currentTimeMillis();
        syncingLogsTime(startTime, schoolId, "SyncAmountClosing", true);


        AppModel.getInstance().appendLog(context, "Total Entries in AmountClosing" + acm.size());

        SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(acm.size(),
                "SchoolPettyCashMonthlyLimits", AppConstants.EXPENSE_MODULE, "Downloading", schoolId);

        int downloadedCount = 0;

        for (ExpenseAmountClosingModel expenseAmountClosingModel : acm) {
            try {
                if (!ExpenseHelperClass.getInstance(context).FindAmountClosing(expenseAmountClosingModel)) {
                    expenseAmountClosingModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long i = ExpenseHelperClass.getInstance(context).addAmountClosing(expenseAmountClosingModel);

                    if (i > 0) {
                        downloadedCount++;
                    }
                } else {
                    if (ExpenseHelperClass.getInstance(context).IfAmountClosingRecordNotUploaded(expenseAmountClosingModel.getID())) {
                        expenseAmountClosingModel.setUploadedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        long i = ExpenseHelperClass.getInstance(context).updateAmountClosingRecord(expenseAmountClosingModel);

                        if (i > 0) {
                            downloadedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                areAllServicesSuccessful = false;
                AppModel.getInstance().appendErrorLog(context, "In SyncAmountClosing Method. exception occurs: " + e.getMessage());
                e.printStackTrace();
            }

            //Update sync progress
            syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
        }

        syncingLogsTime(startTime, schoolId, "SyncAmountClosing", false);
    }


    public void UploadExpenseAmountClosing(int schoolId) {

        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In uploadExpenseAmountClosing Method");
            ExpenseAmountClosingModel acm = new ExpenseAmountClosingModel();

            acm.setEacm(ExpenseHelperClass.getInstance(context).getAllAmountClosingForUpload());
            if (acm.getEacm() == null || acm.getEacm().size() == 0) {
                AppModel.getInstance().appendLog(context, "No ExpenseAmountClosing record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(acm.getEacm().size(),
                    "ExpenseAmountClosing", AppConstants.EXPENSE_MODULE, "Uploading", schoolId);

            int uploadedCount = 0;

            for (ExpenseAmountClosingModel etm1 : acm.getEacm()) {
                ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
                String token = AppModel.getInstance().getToken(context);
                token = "Bearer " + token;
                Call<Boolean> call = apiInterface.uploadExpenseAmountClosing(etm1, token);

                try {
                    final Response<Boolean> response = call.execute();
                    if (response.isSuccessful()) {
                        error_count = 0;
                        isSyncSuccessfull = true;
                        if (response.body()) {
                            AppModel.getInstance().appendLog(context, "Expense AmountClosing Uploading Successful Code: " + response.code());
                            try {
                                int hId = etm1.getID();
                                ContentValues values = new ContentValues();
                                values.put(ExpenseHelperClass.getInstance(context).UPLOADED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                long id = ExpenseHelperClass.getInstance(context).updateTableColumns(
                                        ExpenseHelperClass.TABLE_EXPENSE_AMOUNT_CLOSING,
                                        values, ExpenseHelperClass.ID + " = " + hId);
                                if (id > 0) {
                                    AppModel.getInstance().appendLog(context, "In uploadExpenseAmountClosing Method. Uploaded. Id:" + id + " and uploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                                    uploadedCount++;
                                }
                            } catch (Exception e) {
                                areAllServicesSuccessful = false;
                                AppModel.getInstance().appendErrorLog(context, "In uploadExpenseAmountClosing " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        } else {
                            AppModel.getInstance().appendErrorLog(context, "Expense AmountClosing record not updated " + response.message());
                        }
                    } else {
                        error_count++;
                        failureCount++;
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadExpenseAmountClosing responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                        if (error_count >= 3) {
                            isSyncSuccessfull = false;
                        }
                    }

                } catch (Exception e) {
                    AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading uploadExpenseAmountClosing " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadExpenseAmountClosing " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }


    public void UploadExpenseTransactions(int schoolId) {
        error_count = 0;
        try {
            AppModel.getInstance().appendLog(context, "In UploadExpenseTransaction Method");

            ExpenseTransactionModel etm = new ExpenseTransactionModel();

            etm.setEtmList(ExpenseHelperClass.getInstance(context).getAllTransactionsForUpload());
            if (etm.getEtmList() == null || etm.getEtmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Expense Transaction record found for upload\n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(etm.getEtmList().size(),
                    "Expense", AppConstants.EXPENSE_MODULE, "Uploading", schoolId);

            AppSyncStatusModel appSyncStatusModel = null;

            if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                appSyncStatusModel = new AppSyncStatusModel();
                appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.ExpenseModuleValue));
                appSyncStatusModel.setSubModule("FE Transaction");
            }


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;
            Call<ArrayList<TransactionUploadResponseModel>> call = apiInterface.uploadExpenseTransactions(schoolId, etm.getEtmList(), token);

            try {

                final Response<ArrayList<TransactionUploadResponseModel>> response = call.execute();
                if (response.isSuccessful()) {
                    error_count = 0;
                    //j
                    isSyncSuccessfull = true;
                    AppModel.getInstance().appendLog(context, "Expense Transaction Uploading Successful Code: " + response.code());

                    if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                        appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                                appSyncStatusModel.getEndedOn()));
                        appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                        SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);
                        AppConstants.isAppSyncTableFirstTime = true;
                    }

                    try {
                        ExpenseHelperClass.getInstance(context).updateServerIdInTransaction(response.body(), syncDownloadUploadModel);
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        AppModel.getInstance().appendErrorLog(context, "In uploadTransaction " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                } else {
                    error_count++;
                    failureCount++;
                    areAllServicesSuccessful = false;
                    AppModel.getInstance().appendErrorLog(context, "In uploadTransaction responseCode " + response.code() + " message: " + response.message());
//                    SurveyAppModel.getInstance().showErrorNotification(context,"Error occurred in uploading records." ,3);
                    if (error_count >= 3) {
                        isSyncSuccessfull = false;
                    }
                }

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(context, "Exception1 Occurred while uploading Transaction " + e.getMessage());
                failureCountForAllModules++;
                e.printStackTrace();
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadTransaction " + e.getLocalizedMessage());
            e.printStackTrace();
            areAllServicesSuccessful = false;
            failureCount++;
            isSyncSuccessfull = false;
        }
    }

    public void syncingLogsTime(long startTime, int schoolId, String module, boolean isStartTime) {
        long elapsedTime = 0;
        if (isStartTime) {
            AppModel.getInstance().appendLog(context, "\nIn " + module + " Method Start time = "
                    + AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy hh:mm:ss a") + " SchoolId = " + schoolId);
        } else {
            elapsedTime = System.currentTimeMillis() - startTime;
            elapsedTime = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
            AppModel.getInstance().appendLog(context, "In " + module + " Method end time = "
                    + AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy hh:mm:ss a") + " SchoolId = " + schoolId + " elapsedTIme = " + elapsedTime + " Sec");
        }

    }

    public void uploadAppSyncStatus() {
        try {
            AppModel.getInstance().appendLog(context, "In uploadAppSyncStatus Method");

            AppSyncStatusModel assm = new AppSyncStatusModel();


            assm.setAssmList(SyncProgressHelperClass.getInstance(context).getAllAppSyncStatusForUpload());
            if (assm.getAssmList() == null || assm.getAssmList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No App Sync Status record found for upload\n");
                return;
            }

            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadAppSyncStatus(assm.getAssmList(), token);
            Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
            if (response.isSuccessful()) {
//                error_count = 0;
//                isSyncSuccessfull = true;

                AppModel.getInstance().appendLog(context, "App Sync Status Uploaded Successfully Response Code: " + response.code());
                assert response.body() != null;

                SyncProgressHelperClass.getInstance(context).deleteAppSyncStats(assm.getAssmList());
                AppModel.getInstance().appendLog(context, "In uploadAppSyncStatus Method.Feedback Uploaded.UploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//                for (GeneralUploadResponseModel model : response.body()) {

//                    ContentValues values = new ContentValues();
//                    values.put(SyncProgressHelperClass.getInstance(context).UploadedOn, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//                    long id = SyncProgressHelperClass.getInstance(context).updateTableColumns(
//                            SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS,
//                            values, SyncProgressHelperClass.getInstance(context).ID + " = " + model.device_id);


//                    if (model.ErrorMessage != null && !model.ErrorMessage.isEmpty()) {
//                        AppModel.getInstance().appendLog(context, "App Sync Status Error Message Successful Code: " + model.ErrorMessage);
//                        AppModel.getInstance().appendErrorLog(context, "App Sync Status Error Message After successful upload: " + model.ErrorMessage);
//                    }

                //Update sync progress
//                    syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
//                }

            } else {
//                areAllServicesSuccessful = false;
//                error_count++;
//                failureCount++;
                AppModel.getInstance().appendErrorLog(context, "In uploadFeedback responseCode " + response.code() + " message: " + response.message());
//                if (error_count >= 3) {
//                    isSyncSuccessfull = false;
//                }
            }

        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadTransaction " + e.getLocalizedMessage());
            e.printStackTrace();
//            areAllServicesSuccessful = false;
//            failureCount++;
//            isSyncSuccessfull = false;
        }
    }

    public void uploadAppSyncStatusMaster() {
        try {
            AppModel.getInstance().appendLog(context, "In uploadAppSyncStatusMaster Method");
            long lastRow = SyncProgressHelperClass.getInstance(context).getLastMasterRow();
            if (lastRow > 0) {
                ContentValues values = new ContentValues();
                values.put("endedOn", AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                values.put("userId", DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId());
                SyncProgressHelperClass.getInstance(context).updateTableColumns(SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS_MASTER,
                        values,
                        " syncId = " + lastRow);
            }
            if (lastRow > 0) {
                ContentValues values = new ContentValues();
                values.put(SyncProgressHelperClass.getInstance(context).SYNC_MASTER_ID, lastRow);
                SyncProgressHelperClass.getInstance(context).updateTableColumns(SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS,
                        values,
                        "(syncId IS NULL OR syncId = '' OR syncId = 0)");
            }

            ArrayList<AppSyncStatusMasterModel> masterModelList = new ArrayList<>();

            masterModelList = SyncProgressHelperClass.getInstance(context).getAllAppSyncStatusMasterForUpload();
            if (CollectionUtils.isEmpty(masterModelList)) {
                AppModel.getInstance().appendLog(context, "No App Sync Status record found for upload\n");
                return;
            }

            ArrayList<Integer> schoolsList = new ArrayList<>();
            for (SchoolModel model : AppModel.getInstance().getSchoolsForLoggedInUser(context)) {
                if (model.getAllowedModule_App() != null) {
                    List<String> allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                    if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                        schoolsList.add(model.getId());
                    }
                }
            }

            ArrayList<UploadCashInHandModel> cashInHandModels = new ArrayList<>();
            if(!CollectionUtils.isEmpty(schoolsList)) {
                for (int i= 0; i < schoolsList.size(); i++) {
                    cashInHandModels.add(new UploadCashInHandModel(
                            schoolsList.get(i),
                            Long.parseLong(FeesCollection.getInstance(context).getCashInHandData(schoolsList.get(i) + "").getTotal())
                    ));
                }

            }

            OnSyncUploadModel onSyncUploadModel = new OnSyncUploadModel();
            onSyncUploadModel.setAppSyncStatusMasterModels(masterModelList);
            onSyncUploadModel.setCashInHandList(cashInHandModels);


            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(context);
            token = "Bearer " + token;

            Call<ArrayList<GeneralUploadResponseModel>> call = apiInterface.uploadAppSyncStatusMaster(onSyncUploadModel, token);
            Response<ArrayList<GeneralUploadResponseModel>> response = call.execute();
            if (response.isSuccessful()) {
//                error_count = 0;
//                isSyncSuccessfull = true;

                AppModel.getInstance().appendLog(context, "App Sync Status Uploaded Successfully Response Code: " + response.code());
                assert response.body() != null;

//                SyncProgressHelperClass.getInstance(context).deleteAppSyncStats(assm.getAssmList());
                AppModel.getInstance().appendLog(context, "In uploadAppSyncStatusMaster Method.Feedback Uploaded.UploadedOn:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//                for (GeneralUploadResponseModel model : response.body()) {

                    /*ContentValues values = new ContentValues();
                    values.put(SyncProgressHelperClass.getInstance(context).UploadedOn, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    long id = SyncProgressHelperClass.getInstance(context).updateTableColumns(
                            SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS_MASTER,
                            values, "( " + SyncProgressHelperClass.getInstance(context).UploadedOn + " IS NULL OR " + SyncProgressHelperClass.getInstance(context).UploadedOn + " = '')");
                */

                if (!CollectionUtils.isEmpty(response.body())) {
                    for (GeneralUploadResponseModel responseModel: response.body()){
                        if (responseModel.getServer_id() > 0) {
                            ContentValues values = new ContentValues();
                            values.put(SyncProgressHelperClass.getInstance(context).SYNC_MASTER_ID, responseModel.getServer_id());
                            long id = SyncProgressHelperClass.getInstance(context).updateTableColumns(
                                    SyncProgressHelperClass.getInstance(context).TABLE_APP_SYNC_STATUS,
                                    values, SyncProgressHelperClass.getInstance(context).SYNC_MASTER_ID + " = " + responseModel.getDevice_id());

                            SyncProgressHelperClass.getInstance(context).deleteLastAppSyncMasterRecord(responseModel.getDevice_id());
                        }

                    }

                    uploadAppSyncStatus();
                }

//                    if (model.ErrorMessage != null && !model.ErrorMessage.isEmpty()) {
//                        AppModel.getInstance().appendLog(context, "App Sync Status Error Message Successful Code: " + model.ErrorMessage);
//                        AppModel.getInstance().appendErrorLog(context, "App Sync Status Error Message After successful upload: " + model.ErrorMessage);
//                    }

                //Update sync progress
//                    syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
//                }

            } else {
//                areAllServicesSuccessful = false;
//                error_count++;
//                failureCount++;
                AppModel.getInstance().appendErrorLog(context, "In uploadFeedback responseCode " + response.code() + " message: " + response.message());
//                if (error_count >= 3) {
//                    isSyncSuccessfull = false;
//                }
            }


        } catch (Exception e) {
            AppModel.getInstance().appendErrorLog(context, "In uploadTransaction " + e.getLocalizedMessage());
            e.printStackTrace();
//            areAllServicesSuccessful = false;
//            failureCount++;
//            isSyncSuccessfull = false;
        }
    }

    public void SyncReligion(final ArrayList<ReligionModel> rmList) {
        new Thread(() -> {
            if (rmList != null && rmList.size() > 0) {

                String startDate, endDate;

                AppModel.getInstance().appendErrorLog(context, "Religion count:" + rmList.size());

                AppModel.getInstance().appendLog(context, "Religion count:" + rmList.size());


                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(rmList.size(),
                        "Religion", AppConstants.SCHOOL_MODULE, "Downloading", 0);
                int downloadedCount = 0;

                for (ReligionModel rm : rmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                GlobalHelperClass.getInstance(context).TABLE_RELIGION,
                                GlobalHelperClass.getInstance(context).ID,
                                rm.getId())) {
                            long i = GlobalHelperClass.getInstance(context).addReligion(rm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = GlobalHelperClass.getInstance(context).updateReligion(rm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncReligion error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Religion record found");
            }
        }).start();

    }

    public void SyncNationality(final ArrayList<NationalityModel> nmList) {
        new Thread(() -> {
            if (nmList != null && nmList.size() > 0) {

                String startDate, endDate;

                AppModel.getInstance().appendErrorLog(context, "Nationality count:" + nmList.size());

                AppModel.getInstance().appendLog(context, "Nationality count:" + nmList.size());


                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(nmList.size(),
                        "Nationality", AppConstants.SCHOOL_MODULE, "Downloading", 0);
                int downloadedCount = 0;

                for (NationalityModel nm : nmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                GlobalHelperClass.getInstance(context).TABLE_NATIONALITY,
                                GlobalHelperClass.getInstance(context).ID,
                                nm.getId())) {
                            long i = GlobalHelperClass.getInstance(context).addNationality(nm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = GlobalHelperClass.getInstance(context).updateNationality(nm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncNationality error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Nationality record found");
            }
        }).start();

    }

    public void SyncElectiveSubject(final ArrayList<ElectiveSubjectModel> esmList) {
        new Thread(() -> {
            if (esmList != null && esmList.size() > 0) {

                String startDate, endDate;

                AppModel.getInstance().appendErrorLog(context, "Elective Subject count:" + esmList.size());

                AppModel.getInstance().appendLog(context, "Elective Subject count:" + esmList.size());


                SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(esmList.size(),
                        "Elective Subject", AppConstants.SCHOOL_MODULE, "Downloading", 0);
                int downloadedCount = 0;

                for (ElectiveSubjectModel esm : esmList) {
                    try {
                        if (!DatabaseHelper.getInstance(context).FindRecord(
                                GlobalHelperClass.getInstance(context).TABLE_ELECTIVE_SUBJECTS,
                                GlobalHelperClass.getInstance(context).ID,
                                esm.getId())) {
                            long i = GlobalHelperClass.getInstance(context).addElectiveSubject(esm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        } else {
                            long i = GlobalHelperClass.getInstance(context).updateElectiveSubject(esm);

                            if (i > 0) {
                                downloadedCount++;
                            }
                        }
                    } catch (Exception e) {
                        areAllServicesSuccessful = false;
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context, "Exception in SyncElectiveSubject error:" + e.getMessage());
                    }

                    //Update sync progress

                    syncProgressUpdate(syncDownloadUploadModel, downloadedCount);
                }
            } else {
                AppModel.getInstance().appendErrorLog(context, "No Elective Subject record found");
            }
        }).start();

    }

    public void UploadDeathCertImage(int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method");
            EnrollmentImageModel enrollmentImageModel = new EnrollmentImageModel();
            enrollmentImageModel.setEimList(
                    DatabaseHelper.getInstance(context).getAllDeathCertImage());
            if (enrollmentImageModel.getEimList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Records found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(enrollmentImageModel.getEimList().size(),
                    "Death Certificate Image", AppConstants.IMAGE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            int uploadedCount = 0;
            long appSyncStatusTotalUploadedSize = 0;

            for (EnrollmentImageModel eim : enrollmentImageModel.getEimList()) {
                try {
//                    eim = enrollmentImageModel.getEimList().get(0);
                    if (eim.getFilename() == null)
                        continue;

                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method.Uploading Image:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                        appSyncStatusModel = new AppSyncStatusModel();
                        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                        appSyncStatusModel.setSubModule("Death Certificate Image");
                    }


                    int responseCode = connectionClass.uploadFile(fdir + "/" + eim.getFilename(),
                            context.getString(R.string.BASE_URL) + AppConstants.URL_UPLOAD_ENROLLMENT_IMAGES + "?enrollment_id=" + eim.getId() + "&filetype=" + eim.getFiletype());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(fdir + "/" + eim.getFilename()).length();


                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");

                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).STUDENT_DEATH_CERT_IMAGE, name);
                        values.put(DatabaseHelper.getInstance(context).STUDENT_DEATH_CERT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDate());
                        values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, (String) null);
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_STUDENT, values, eim.getId());
                        if (id > 0) {
                            UploadStudents(schoolId);
                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(fdir + "/" + eim.getFilename());
                            if (sourceFilename.exists()) {
                                if (eim.getFiletype().toLowerCase().equals(AppConstants.DEATH_CERT_FILE_TYPE.toLowerCase())) {
                                    File updatedFileName = new File(fdir + "/" + name);
                                    if (!updatedFileName.exists()) {
                                        try {
                                            String rootPath = updatedFileName.getParent();
                                            String fileName = updatedFileName.getName();


                                            assert rootPath != null;
                                            File root = new File(rootPath);
                                            if (!root.exists()) {
                                                root.mkdirs();
                                            }

                                            //It will move the file to the path and renamed it
                                            boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                            if (isNameChanged) {
                                                AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method. Image name changed in local.");
                                            }

                                            AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method. Image path changed in local.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
                                } else {
                                    sourceFilename.delete();
                                }
                            }

                            uploadedCount++;
                        }
                    } else {
                        if (responseCode == 0) {
                            File image = new File(eim.getFilename());
                            if (!image.exists()) {
                                AppModel.getInstance().appendErrorLog(context, "DeathCert Image does not exists " + image.getName());
//                                ContentValues values = new ContentValues();
//                                values.put(DatabaseHelper.ENROLLMENT_REVIEW_STATUS, AppConstants.PROFILE_INCOMPLETE_KEY);
//                                DatabaseHelper.getInstance(context).updateTableColumns(DatabaseHelper.TABLE_ENROLLMENT, values, (int) eim.getEnrollment_id());
                            }
                        } else {
                            failureCount++;
                            error_count++;
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                            if (error_count == 3) {
                                isSyncSuccessfull = false;
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadDeathCertImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void UploadMedicalCertImage(int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method");
            EnrollmentImageModel enrollmentImageModel = new EnrollmentImageModel();
            enrollmentImageModel.setEimList(
                    DatabaseHelper.getInstance(context).getAllMedicalCertImage());
            if (enrollmentImageModel.getEimList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Records found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(enrollmentImageModel.getEimList().size(),
                    "Medical Certificate Image", AppConstants.IMAGE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            int uploadedCount = 0;
            long appSyncStatusTotalUploadedSize = 0;

            for (EnrollmentImageModel eim : enrollmentImageModel.getEimList()) {
                try {
//                    eim = enrollmentImageModel.getEimList().get(0);
                    if (eim.getFilename() == null)
                        continue;

                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method.Uploading Image:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                        appSyncStatusModel = new AppSyncStatusModel();
                        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                        appSyncStatusModel.setSubModule("Medical Certificate Image");
                    }


                    int responseCode = connectionClass.uploadFile(fdir + "/" + eim.getFilename(),
                            context.getString(R.string.BASE_URL) + AppConstants.URL_UPLOAD_ENROLLMENT_IMAGES + "?enrollment_id=" + eim.getId() + "&filetype=" + eim.getFiletype());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(fdir + "/" + eim.getFilename()).length();


                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");

                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).STUDENT_MEDICAL_CERT_IMAGE, name);
                        values.put(DatabaseHelper.getInstance(context).STUDENT_MEDICAL_CERT_IMAGE_UPLOADED_ON, AppModel.getInstance().getDate());
                        values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, (String) null);
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_STUDENT, values, eim.getId());
                        if (id > 0) {
                            UploadStudents(schoolId);
                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(fdir + "/" + eim.getFilename());
                            if (sourceFilename.exists()) {
                                if (eim.getFiletype().toLowerCase().equals(AppConstants.MEDICAL_CERT_FILE_TYPE.toLowerCase())) {
                                    File updatedFileName = new File(fdir + "/" + name);
                                    if (!updatedFileName.exists()) {
                                        try {
                                            String rootPath = updatedFileName.getParent();
                                            String fileName = updatedFileName.getName();


                                            assert rootPath != null;
                                            File root = new File(rootPath);
                                            if (!root.exists()) {
                                                root.mkdirs();
                                            }

                                            //It will move the file to the path and renamed it
                                            boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                            if (isNameChanged) {
                                                AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method. Image name changed in local.");
                                            }

                                            AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method. Image path changed in local.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
                                } else {
                                    sourceFilename.delete();
                                }
                            }

                            uploadedCount++;
                        }
                    } else {
                        if (responseCode == 0) {
                            File image = new File(eim.getFilename());
                            if (!image.exists()) {
                                AppModel.getInstance().appendErrorLog(context, "MedicalCert Image does not exists " + image.getName());
//                                ContentValues values = new ContentValues();
//                                values.put(DatabaseHelper.ENROLLMENT_REVIEW_STATUS, AppConstants.PROFILE_INCOMPLETE_KEY);
//                                DatabaseHelper.getInstance(context).updateTableColumns(DatabaseHelper.TABLE_ENROLLMENT, values, (int) eim.getEnrollment_id());
                            }
                        } else {
                            failureCount++;
                            error_count++;
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                            if (error_count == 3) {
                                isSyncSuccessfull = false;
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadMedicalCertImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void UploadBFormImage(int schoolId) {
        try {
            AppModel.getInstance().appendLog(context, "In UploadBFormImage Method");
            EnrollmentImageModel enrollmentImageModel = new EnrollmentImageModel();
            enrollmentImageModel.setEimList(
                    DatabaseHelper.getInstance(context).getAllBFormImage());
            if (enrollmentImageModel.getEimList().size() == 0) {
                AppModel.getInstance().appendLog(context, "No Records found for upload \n");
                return;
            }

            SyncDownloadUploadModel syncDownloadUploadModel = new SyncDownloadUploadModel(enrollmentImageModel.getEimList().size(),
                    "BForm Image", AppConstants.IMAGE_MODULE, "Uploading", 0);

            AppSyncStatusModel appSyncStatusModel = null;

            int uploadedCount = 0;
            long appSyncStatusTotalUploadedSize = 0;

            for (EnrollmentImageModel eim : enrollmentImageModel.getEimList()) {
                try {
//                    eim = enrollmentImageModel.getEimList().get(0);
                    if (eim.getFilename() == null)
                        continue;

                    String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                    HttpConnectionClass connectionClass = new HttpConnectionClass(context);
                    AppModel.getInstance().appendLog(context, "In UploadBFormImage Method.Uploading Image:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//                    AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);

                    if (!AppConstants.isAppSyncTableFirstTime) { //For AppSyncStatus
                        appSyncStatusModel = new AppSyncStatusModel();
                        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
                        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentModuleValue));
                        appSyncStatusModel.setSubModule("BForm Image");
                    }


                    int responseCode = connectionClass.uploadFile(fdir + "/" + eim.getFilename(),
                            context.getString(R.string.BASE_URL) +  AppConstants.URL_UPLOAD_ENROLLMENT_IMAGES + "?enrollment_id=" + eim.getId() + "&filetype=" + eim.getFiletype());
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        isSyncSuccessfull = true;
                        AppModel.getInstance().appendLog(context, "In UploadBFormImage Method. ResponseCode = " + responseCode);

                        if (!AppConstants.isAppSyncTableFirstTime && appSyncStatusModel != null) { //For AppSyncStatus
                            AppConstants.isAppSyncTableFirstTime = true;
                        }

                        appSyncStatusTotalUploadedSize += new File(fdir + "/" + eim.getFilename()).length();


                        String name = HttpConnectionClass.responseJson;
                        name = name.replace("\\", "/");

                        //image upload successfully.update uploaded_on field with current date.

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.getInstance(context).STUDENT_BFORM_IMAGE, name);
                        values.put(DatabaseHelper.getInstance(context).STUDENT_BFORM_IMAGE_UPLOADED_ON, AppModel.getInstance().getDate());
                        values.put(DatabaseHelper.getInstance(context).STUDENT_UPLOADED_ON, (String) null);
                        long id = DatabaseHelper.getInstance(context).updateTableColumns(
                                DatabaseHelper.getInstance(context).TABLE_STUDENT, values, eim.getId());
                        if (id > 0) {
                            UploadStudents(schoolId);
                            error_count = 0;
                            AppModel.getInstance().appendLog(context, "In UploadBFormImage Method.Image Uploaded. Image name:" + name + " and uploadedOn:" + AppModel.getInstance().getDate());
                            File sourceFilename = new File(fdir + "/" + eim.getFilename());
                            if (sourceFilename.exists()) {
                                if (eim.getFiletype().toLowerCase().equals(AppConstants.BFORM_FILE_TYPE.toLowerCase())) {
                                    File updatedFileName = new File(fdir + "/" + name);
                                    if (!updatedFileName.exists()) {
                                        try {
                                            String rootPath = updatedFileName.getParent();
                                            String fileName = updatedFileName.getName();


                                            assert rootPath != null;
                                            File root = new File(rootPath);
                                            if (!root.exists()) {
                                                root.mkdirs();
                                            }

                                            //It will move the file to the path and renamed it
                                            boolean isNameChanged = sourceFilename.renameTo(new File(rootPath + "/" + fileName));
                                            if (isNameChanged) {
                                                AppModel.getInstance().appendLog(context, "In UploadBFormImage Method. Image name changed in local.");
                                            }

                                            AppModel.getInstance().appendLog(context, "In UploadBFormImage Method. Image path changed in local.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
//                                    boolean isNameChanged = Filename.renameTo(new File(fdir + "/" + name));
//                                    if (isNameChanged)
//                                        AppModel.getInstance().appendLog(context, "In UploadEnrollmentsImage Method. Image name changed in local.");
                                } else {
                                    sourceFilename.delete();
                                }
                            }

                            uploadedCount++;
                        }
                    } else {
                        if (responseCode == 0) {
                            File image = new File(eim.getFilename());
                            if (!image.exists()) {
                                AppModel.getInstance().appendErrorLog(context, "BForm Image does not exists " + image.getName());
//                                ContentValues values = new ContentValues();
//                                values.put(DatabaseHelper.ENROLLMENT_REVIEW_STATUS, AppConstants.PROFILE_INCOMPLETE_KEY);
//                                DatabaseHelper.getInstance(context).updateTableColumns(DatabaseHelper.TABLE_ENROLLMENT, values, (int) eim.getEnrollment_id());
                            }
                        } else {
                            failureCount++;
                            error_count++;
                            areAllServicesSuccessful = false;
                            AppModel.getInstance().appendErrorLog(context, "Image Upload fail. ResponseCode = " + responseCode);
                            if (error_count == 3) {
                                isSyncSuccessfull = false;
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    isSyncSuccessfull = false;
                    areAllServicesSuccessful = false;
                    failureCount++;
                    failureCountForAllModules++;
                    AppModel.getInstance().appendLog(context, "In UploadBFormImage Method. exception occurs: " + e.getMessage());
                    e.printStackTrace();
                }

                //Update sync progress
                syncProgressUpdate(syncDownloadUploadModel, uploadedCount);
            }

            //Insert AppSyncStats Record
            appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
            appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                    appSyncStatusModel.getEndedOn()));
            appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(appSyncStatusTotalUploadedSize, false));
            SyncProgressHelperClass.getInstance(context).insertAppSyncRecords(appSyncStatusModel);

//            IProcessComplete pc = (IProcessComplete) syncAdapter;
//            pc.onProcessCompleted();
        } catch (Exception e) {
            areAllServicesSuccessful = false;
            AppModel.getInstance().appendLog(context, "In UploadBFormImage Method. exception occurs: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
