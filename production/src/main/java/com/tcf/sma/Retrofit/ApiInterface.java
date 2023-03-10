package com.tcf.sma.Retrofit;

import com.tcf.sma.Helpers.DbTables.Expense.ExpenseDataResponseModel;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.BaseUrlModel;
import com.tcf.sma.Models.CheckSumModel;
import com.tcf.sma.Models.ExpenseCheckSumModel;
import com.tcf.sma.Models.FeesHeaderUploadModel;
import com.tcf.sma.Models.Fees_Collection.GeneralUploadResponseModel;
import com.tcf.sma.Models.Fees_Collection.KeepAliveModel;
import com.tcf.sma.Models.Fees_Collection.SummaryModel;
import com.tcf.sma.Models.NetwrokConnection.NetworkConnectionInfo;
import com.tcf.sma.Models.OnSyncUploadModel;
import com.tcf.sma.Models.RetrofitModels.AttendanceUploadModel;
import com.tcf.sma.Models.RetrofitModels.EnrollmentUploadModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseAmountClosingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashMonthlyLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsMonthlyModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionUploadResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeTeacherAttendanceModel;
import com.tcf.sma.Models.RetrofitModels.HR.SeparationDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.HR.UploadEmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.UploadTCTEmployeeSubTagModel;
import com.tcf.sma.Models.RetrofitModels.Help.HelpDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.Help.UploadFeedbackModel;
import com.tcf.sma.Models.RetrofitModels.LoginRequestModel;
import com.tcf.sma.Models.RetrofitModels.LoginResponseModel;
import com.tcf.sma.Models.RetrofitModels.MetaDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.StudentDataResponseModel;
import com.tcf.sma.Models.RetrofitModels.StudentTransferUploadModel;
import com.tcf.sma.Models.RetrofitModels.SyncCashDepositModel;
import com.tcf.sma.Models.RetrofitModels.SyncCashInvoicesModel;
import com.tcf.sma.Models.RetrofitModels.UploadPromotionModel;
import com.tcf.sma.Models.RetrofitModels.UploadStudentsAuditModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.StudentTransferModel;
import com.tcf.sma.Models.StudentUploadResponseModel;
import com.tcf.sma.Models.SyncCashReceiptsModel;
import com.tcf.sma.Models.SyncFeesHeaderModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusMasterModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusModel;
import com.tcf.sma.Models.WithdrawalModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Zubair Soomro on 2/2/2017.
 */

public interface ApiInterface {

    @POST(AppConstants.URL_LOGIN)
    Call<LoginResponseModel> loginAPI(@Body LoginRequestModel postParams);

    @POST("https://siswebsrv2.tcf.org.pk:3443/api/ServerSettings")
    Call<BaseUrlModel> fetchUrlApi(@Query("UserName") String userNAme);

    @POST("api/metadata/get/{schoolId}")
    Call<MetaDataResponseModel> getMetaData(@Path("schoolId") int schoolId,
                                            @Query("classes_modifiedOn") String classes_modifiedOn,
                                            @Query("section_modifiedOn") String section_modifiedOn,
                                            @Query("school_classes_modifiedOn") String school_classes_modifiedOn,
                                            @Query("scholarship_category_modifiedOn") String scholarship_category_modifiedOn,
                                            @Query("withdrawal_reasons_modifiedOn") String withdrawal_reasons_modifiedOn,
                                            @Query("calendars_modifiedOn") String calendars_modifiedOn,
                                            @Query("campus_modifiedOn") String campus_modifiedOn,
                                            @Query("location_modifiedOn") String location_modifiedOn,
                                            @Query("areas_modifiedOn") String areas_modifiedOn,
                                            @Query("region_modifiedOn") String region_modifiedOn,
                                            @Header("Authorization") String token);

    @POST("api/metadata/get/{schoolId}")
    Call<MetaDataResponseModel> getMetaData(@Path("schoolId") int schoolId,
                                            @Query("classes_modifiedOn") String classes_modifiedOn,
                                            @Query("section_modifiedOn") String section_modifiedOn,
                                            @Query("withdrawal_reasons_modifiedOn") String withdrawal_reasons_modifiedOn,
                                            @Query("campus_modifiedOn") String campus_modifiedOn,
                                            @Query("location_modifiedOn") String location_modifiedOn,
                                            @Query("areas_modifiedOn") String areas_modifiedOn,
                                            @Query("region_modifiedOn") String region_modifiedOn,
                                            @Header("Authorization") String token);


    @POST("api/metadata/GetSummaryData/{schoolId}")
    Call<SummaryModel> getSummary(@Path("schoolId") int schoolId, @Header("Authorization") String token);

    //    @POST("api/studentdata/get/{schoolId}/{dateFrom}")
    @POST("api/studentdata/get/{schoolId}")
    Call<StudentDataResponseModel> getStudentData(@Path("schoolId") int schoolId,
                                                  @Query("dateFrom") String student_modifiedOn,
                                                  @Query("attendance_modifiedOn") String attendance_modifiedOn,
                                                  @Query("promotion_modifiedOn") String promotion_modifiedOn,
//                                                  @Query("withdrawal_modifiedOn") String withdrawal_modifiedOn,
                                                  @Query("enrollments_modifiedOn") String enrollments_modifiedOn,
//                                                  @Query("schoolaudits_modifiedOn") String schoolaudits_modifiedOn,
                                                  @Header("Authorization") String token);

//    @POST(AppConstants.URL_UPLOAD_STUDENTS)
//    Call<ResponseBody> uploadStudents(@Body StudentModel sm, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_STUDENTS)
    Call<StudentUploadResponseModel> uploadStudents(@Body StudentModel sm, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_STUDENT_FOR_TRANSFER)
    Call<ResponseBody> uploadStudentTransfer(@Body StudentTransferUploadModel sm, @Header("Authorization") String token);

    @POST(AppConstants.URL_SINGLE_STUDENT)
    Call<StudentModel> getSingleStudent(@Path("id") int studentId, @Header("Authorization") String token);

    @POST(AppConstants.URL_SINGLE_STUDENT)
    Call<StudentTransferModel> getSingleStudentForTransfer(@Path("id") int studentId, @Query("grno") String grNo, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_ENROLLMENTS)
    Call<ResponseBody> uploadEnrollments(@Body EnrollmentUploadModel eum, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_ATTENDANCE)
    Call<ResponseBody> uploadAttendance(@Body AttendanceUploadModel aum, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_PROMOTION)
    Call<ResponseBody> uploadPromotion(@Body UploadPromotionModel upm, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_WITHDRAWAL)
    Call<ResponseBody> uploadWithdrawal(@Body WithdrawalModel withdrawalModels, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_SCHOOL_AUDITS)
    Call<ResponseBody> uploadSchoolAudits(@Body UploadStudentsAuditModel saModel, @Header("Authorization") String token);

    @POST(AppConstants.URL_FEES_RECEIPT)
    Call<ArrayList<GeneralUploadResponseModel>> uploadFeesReceipt(@Body List<SyncCashReceiptsModel> appReceiptList, @Header("Authorization") String token);

    @POST(AppConstants.URL_FEES_HEADER)
    Call<ArrayList<GeneralUploadResponseModel>> uploadFeesHeader(@Body List<FeesHeaderUploadModel> appFeesHeaderList, @Header("Authorization") String token);

    //    @POST(AppConstants.URL_FEES_INVOICE)
    Call<ArrayList<GeneralUploadResponseModel>> uploadFeesInvoices(@Body List<SyncCashInvoicesModel> appReceiptList, @Header("Authorization") String token);

    @POST(AppConstants.URL_CASH_DEPOSIT)
    Call<ArrayList<GeneralUploadResponseModel>> uploadCashDeposit(@Body List<SyncCashDepositModel> cashDepositList, @Header("Authorization") String token);

    //    @POST(AppConstants.URL_GET_CASH_RECEIPTS)
    Call<ArrayList<SyncCashReceiptsModel>> getFeesReceipts(@Query("schoolYearId") int schoolId, @Query("lastServerId") int lastServerId, @Header("Authorization") String token);

    @POST(AppConstants.URL_GET_FEES_HEADERS)
    Call<ArrayList<SyncFeesHeaderModel>> getFeesHeader(@Query("academicSessionId") int academicSessionID, @Query("schoolId") int schoolId, @Query("lastServerId") int lastServerId, @Query("lastModifiedDate") String lastModifiedDate, @Header("Authorization") String token);

    //model is same for uploading and downwloading
    @POST(AppConstants.URL_GET_CASH_DEPOSITS)
    Call<ArrayList<SyncCashDepositModel>> getCashDeposits(@Query("schoolId") int schoolId, @Query("lastServerId") int lastServerId, @Query("lastModifiedOn") String lastModifiedOn, @Header("Authorization") String token);

    //    @POST(AppConstants.URL_GET_CASH_INVOICES)
    Call<ArrayList<SyncCashInvoicesModel>> getInvoices(@Query("schoolYearId") int schoolId, @Query("lastServerId") int lastServerId, @Header("Authorization") String token);

    @POST(AppConstants.URL_KEEP_ALIVE_TIMER)
    Call<ResponseBody> pingServer(@Body KeepAliveModel keepAliveModel);

    @POST(AppConstants.URL_FORGOT_PASSWORD + "/{employeeCode}/{cnic}")
    Call<ResponseBody> forgotPassword(@Path("employeeCode") String employeeCode, @Path("cnic") String cnic);


    @POST(AppConstants.URL_CHANGE_PASSWORD + "/{newpassword}")
    Call<ResponseBody> uploadNewPassword(@Path("newpassword") String newpassword, @Header("Authorization") String token);

    @POST(AppConstants.URL_CHECKSUM)
    Call<CheckSumModel> getCheckSum(@Query("schoolId") int schoolID, @Query("academicSession") int academciSessionId, @Header("Authorization") String token);

//    @POST("api/hrdata/get/{schoolId}/{dateFrom}")
//    Call<EmployeeDataResponseModel> getEmployeeData(@Path("schoolId") int schoolId, @Path("dateFrom") String dateFrom, @Header("Authorization") String token);

    //Metadata
    @POST("api/hrdata/get/{schoolId}")
    Call<EmployeeDataResponseModel> getEmployeeData(@Path("schoolId") int schoolId,
                                                    @Query("dateFrom") String employeeDetail_ModifiedOn,
                                                    @Query("designation_modifiedOn") String designation_modifiedOn,
                                                    @Query("leaveType_modifiedOn") String leaveType_modifiedOn,
                                                    @Query("resignReason_modifiedOn") String resignReason_modifiedOn,
                                                    @Query("resignType_modifiedOn") String resignType_modifiedOn,
                                                    @Header("Authorization") String token);

    //AllData
    @POST("api/hrdata/get/{schoolId}")
    Call<EmployeeDataResponseModel> getEmployeeData(@Path("schoolId") int schoolId,
                                                    @Query("dateFrom") String employeeDetail_ModifiedOn,
//                                                    @Query("employeeSchool_ModifiedOn") String employeeSchool_ModifiedOn,
                                                    @Query("employeeAttendance_ModifiedOn") String employeeAttendance_ModifiedOn,
                                                    @Query("employeeLeave_ModifiedOn") String employeeLeave_ModifiedOn,
                                                    @Query("employeeQualHistory_ModifiedOn") String employeeQualHistory_ModifiedOn,
                                                    @Query("employeePosHistory_ModifiedOn") String employeePosHistory_ModifiedOn,
                                                    @Query("employeeResign_ModifiedOn") String employeeResign_ModifiedOn,
//                                                    @Query("employeeResignImages_ModifiedOn") String employeeResignImages_ModifiedOn,
                                                    @Header("Authorization") String token);

    @POST("api/hrdata/GetSeparationDetail")
    Call<SeparationDataResponseModel> getSeparationDetail(@Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_EMPLOYEE_DETAIL)
    Call<ResponseBody> uploadEmployeeDetail(@Body UploadEmployeeModel employeeModel, @Header("Authorization") String token);

//    @POST(AppConstants.URL_UPLOAD_EMPLOYEE_TEACHER_LEAVES)
//    Call<ResponseBody> uploadEmployeeTeacherLeaves(@Body EmployeeLeaveModel employeeLeaveModel, @Header("Authorization") String token);

//    @POST(AppConstants.URL_UPLOAD_RESIGNED_RECEIVED)
//    Call<ResponseBody> uploadResignReceived(@Body EmployeeResignationModel employeeResignationModel, @Header("Authorization") String token);

//    @POST(AppConstants.URL_UPLOAD_TEACHER_ATTENDANCE)
//    Call<ResponseBody> uploadTeacherAttendance(@Body EmployeeTeacherAttendanceModel employeeTeacherAttendanceModel, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_RESIGNED_RECEIVED)
    Call<ArrayList<GeneralUploadResponseModel>> uploadResignReceived(@Body List<EmployeeSeparationModel> employeeSeparationModels, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_SEPARATION_APPROVAL)
    Call<Boolean> uploadSeparationApproval(@Body EmployeeSeparationDetailModel esdm, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_EMPLOYEE_TEACHER_LEAVES)
    Call<ArrayList<GeneralUploadResponseModel>> uploadEmployeeTeacherLeaves(@Body List<EmployeeLeaveModel> employeeLeaveModels, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_TEACHER_ATTENDANCE)
    Call<ArrayList<GeneralUploadResponseModel>> uploadTeacherAttendance(@Body List<EmployeeTeacherAttendanceModel> employeeTeacherAttendanceModels, @Header("Authorization") String token);

    //    @POST(AppConstants.URL_CHANGE_PASSWORD)
//    Call<ResponseBody> uploadNewPassword(@Body UploadUserModel uum, @Header("Authorization") String token);

//    @POST(AppConstants.URL_GET_HRTCT_METADATA)
//    Call<TCTDataResponseModel> getTCTEmployeeData(@Query("schoolId") int schoolId, @Header("Authorization") String token);

    @POST(AppConstants.URL_GET_HRTCT_METADATA)
    Call<TCTDataResponseModel> getTCTEmployeeData(@Query("schoolId") int schoolId,
                                                  @Query("TCTPhase_ModifiedOn") String TCTPhase_ModifiedOn,
                                                  @Query("TCTEmpSubTagReason_ModifiedOn") String TCTEmpSubTagReason_ModifiedOn,
                                                  @Query("EmpSubjectTagging_ModifiedOn") String EmpSubjectTagging_ModifiedOn,
                                                  @Query("TCTSubjects_ModifiedOn") String TCTSubjects_ModifiedOn,
                                                  @Query("LeaveTypes_ModifiedOn") String LeaveTypes_ModifiedOn,
                                                  @Query("Designations_ModifiedOn") String Designations_ModifiedOn,
                                                  @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_TCT_EMP_TAGGING)
    Call<ArrayList<GeneralUploadResponseModel>> uploadTCTEmployeeSubTag(@Body List<UploadTCTEmployeeSubTagModel> tctEmpSubjectTaggingModel, @Header("Authorization") String token);

    @POST(AppConstants.GET_HELP_DATA)
    Call<HelpDataResponseModel> getHelpData(@Query("dateFrom") String dateFrom, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_FEEDBACK)
    Call<ArrayList<GeneralUploadResponseModel>> uploadFeedback(@Body List<UploadFeedbackModel> uploadFeedbackModelList, @Header("Authorization") String token);

    @GET
    @Streaming
    Call<ResponseBody> downloadUserManual(@Url String fileUrl);

    @GET(AppConstants.URL_GET_FEES_TRANSACTION_CSV)
    @Streaming
    Call<ResponseBody> downloadFeeTransactionCSV(@Query("schoolId") int schoolId,
                                                 @Query("tableName") String tableName, @Header("Authorization") String token);

    @GET
    @Streaming
    Call<ResponseBody> downloadAnyFile(@Url String fileUrl);

    @POST(AppConstants.URL_UPLOAD_CONNECTION_INFO_RESULT)
    Call<ResponseBody> uploadConnectionInfoResult(@Body NetworkConnectionInfo networkConnectionInfo, @Header("Authorization") String token);

    @Multipart
    @POST(AppConstants.URL_UPLOAD_CONNECTION_INFO_UPLOAD_TEST)
    Call<ResponseBody> uploadFileForTestConnectionInfo(@Part MultipartBody.Part filePart, @Header("Authorization") String token);


    @POST(AppConstants.URL_GET_EXPENSE_EXPENSES_METADATA)
    Call<ExpenseDataResponseModel> getExpensesMetaData(@Path("schoolId") int schoolId, @Query("lastModifiedDate") String lastModifiedDate, @Header("Authorization") String token);


//    @POST(AppConstants.URL_GET_EXPENSE_EXPENSES_METADATA)
//    Call<ExpenseDataResponseModel> getExpensesMetaData(@Path("schoolId") int schoolId,
//                                                       @Query("headsModifiedDate") String headsModifiedDate,
//                                                       @Query("subheadsModifiedDate") String subheadsModifiedDate,
//                                                       @Query("transactionBucketModifiedDate") String transactionBucketModifiedDate,
//                                                       @Query("transactionCategoryModifiedDate") String transactionCategoryModifiedDate,
//                                                       @Query("transactionFlowModifiedDate") String transactionFlowModifiedDate,
//                                                       @Header("Authorization") String token);

    @POST(AppConstants.URL_GET_EXPENSE_EXPENSES)
    Call<ExpenseDataResponseModel> getExpensesData(@Path("schoolId") int schoolId, @Query("PCML_ModifiedOn") String PCML_ModifiedOn, @Header("Authorization") String token);

//    @POST(AppConstants.URL_GET_EXPENSE_EXPENSES)
//    Call<ExpenseDataResponseModel> getExpensesData(@Path("schoolId") int schoolId,
//                                                   @Query("PCML_ModifiedOn") String PCML_ModifiedOn,
//                                                   @Query("SHML_ModifiedOn") String SHML_ModifiedOn,
//                                                   @Query("ET_ModifiedOn") String ET_ModifiedOn,
////                                                   @Query("ETI_ModifiedOn") String ETI_ModifiedOn,
//                                                   @Query("AC_ModifiedOn") String AC_ModifiedOn,
//                                                   @Header("Authorization") String token);

    @POST(AppConstants.URL_EXPENSE_CHECKSUM)
    Call<ExpenseCheckSumModel> getExpenseCheckSum(@Query("schoolId") int schoolID, @Query("academicSession") int academciSessionId, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_EXPENSE_TRANSACTION)
    Call<ArrayList<TransactionUploadResponseModel>> uploadExpenseTransactions(@Path("schoolId") int schoolId, @Body List<ExpenseTransactionModel> expenseTransactionModels, @Header("Authorization") String token);

    @POST("")
    Call<Boolean> uploadExpenseSubHeadLimitsMonthly(@Body ExpenseSubheadLimitsMonthlyModel eslm, @Header("Authorization") String token);

    @POST("")
    Call<Boolean> uploadExpenseSchoolPettyCashMonthlyLimits(@Body ExpenseSchoolPettyCashMonthlyLimitsModel espm, @Header("Authorization") String token);

    @POST("")
    Call<Boolean> uploadExpenseAmountClosing(@Body ExpenseAmountClosingModel espm, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_APP_SYNC_STATUS)
    Call<ArrayList<GeneralUploadResponseModel>> uploadAppSyncStatus(@Body ArrayList<AppSyncStatusModel> appSyncStatusModels, @Header("Authorization") String token);

    @POST(AppConstants.URL_UPLOAD_APP_SYNC_STATUS_MASTER)
    Call<ArrayList<GeneralUploadResponseModel>> uploadAppSyncStatusMaster(@Body OnSyncUploadModel appSyncStatusMasterModels, @Header("Authorization") String token);

//    Call<ArrayList<GeneralUploadResponseModel>> uploadAppSyncStatusMaster(@Body ArrayList<AppSyncStatusMasterModel> appSyncStatusMasterModels, @Header("Authorization") String token);

}
