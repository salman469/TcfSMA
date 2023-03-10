package com.tcf.sma.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.location.LocationRequest;
import com.speedchecker.android.sdk.SpeedcheckerSDK;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.Global.GlobalHelperClass;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.GpsLocationHelper;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.BaseUrlModel;
import com.tcf.sma.Models.RetrofitModels.AppModulesModel;
import com.tcf.sma.Models.RetrofitModels.HR.UserImageModel;
import com.tcf.sma.Models.RetrofitModels.LoginRequestModel;
import com.tcf.sma.Models.RetrofitModels.LoginResponseModel;
import com.tcf.sma.Models.RetrofitModels.MetaDataResponseModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.R;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
//import com.tcf.sma.Survey.com.kcompute.dnaareasurvey.ProjectsActivity;
import com.tcf.sma.Scheduler.WorkManager.InternetStatusWorkManager;
import com.tcf.sma.SyncClasses.SyncUtils;
import com.tcf.sma.utils.FinanceCheckSum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<MetaDataResponseModel> {
    EditText userName;
    EditText password;
    TextView forgot, signup;
    Button login;
    String name, pass;
    private Toolbar mActionBarToolbar;
    private String lastText = "@tcf.org.pk";
    private TextView tv_versionNumber, tv_passShow;
    private boolean onlyOneSchool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Login");
        SpeedcheckerSDK.init(this);
        init();
    }

    private void init() {
        userName = (EditText) findViewById(R.id.et_username);
        tv_versionNumber = (TextView) findViewById(R.id.versionNumber);
        tv_versionNumber.setText("Version: " + AppModel.getInstance().getAppVersionWithBuildNo(LoginActivity.this));

        String lastUsername = AppModel.getInstance().readFromSharedPreferences(this, "lusername");
        if (lastUsername != null && !lastUsername.equals("") && !lastUsername.equals("false")) {
            userName.setText(lastUsername);
        }
        password = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.bt_login);
        forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        login.setOnClickListener(this);


        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    AuthenticateUser();
                    fetchBaseUrl();
                    return true;
                } else {
                    return false;
                }
            }
        });

        tv_passShow = findViewById(R.id.tv_passShow);
        tv_passShow.setOnClickListener(this);

//        GpsLocationHelper.getInstance().askPermission(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login.setEnabled(false);
//                startActivity(new Intent(LoginActivity.this, ProjectsActivity.class));

                if (validation()) {
//                    SpeedcheckerSDK.askPermissions(LoginActivity.this);
                    login.setEnabled(true);
                    try {
                        boolean NewLogin = AppModel.getInstance().readBooleanFromSharedPreferences(LoginActivity.this,
                                AppConstants.NewLogin, true);
                        if (NewLogin) {
                            FinanceCheckSum.Instance(new WeakReference<Context>(LoginActivity.this)).setCheckSumApplicable(false);
                            FinanceCheckSum.Instance(new WeakReference<Context>(LoginActivity.this)).checkSumSuccessful(true);
                        }

                        fetchBaseUrl();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    login.setEnabled(true);
                }

//                AuthenticateUser();
                break;
            case R.id.forgot:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                /*new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Please Call BO/TCF Head office to reset your password")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();*/
                break;
            case R.id.tv_passShow:
                String passShowHideText = tv_passShow.getText().toString();
                try {
                    if (!password.getText().toString().trim().isEmpty()) {
                        password.post(() -> password.setSelection(password.length()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (passShowHideText.equalsIgnoreCase("show") && !password.getText().toString().trim().isEmpty()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tv_passShow.setText("Hide");
                    tv_passShow.setTextColor(getResources().getColor(R.color.light_red));
                } else if (passShowHideText.equalsIgnoreCase("hide")) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tv_passShow.setText("Show");
                    tv_passShow.setTextColor(getResources().getColor(R.color.app_green));
                }
                break;

        }
    }


    private void fetchBaseUrl() {
        SyncUtils.CreateSyncAccount(LoginActivity.this);
        name = userName.getText().toString().trim();
        name += "@tcf.org.pk";
        pass = password.getText().toString().trim();
        if (AppModel.getInstance().isConnectedToInternet(this)) {
            // If internet available use APi to authenticate and perform operations
            AppModel.getInstance().showLoader(this, false);
            if (!TextUtils.isEmpty(userName.getText().toString().trim())) {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<BaseUrlModel> call = apiInterface.fetchUrlApi(userName.getText().toString() + "@tcf.org.pk");
                call.enqueue(new Callback<BaseUrlModel>() {
                    @Override
                    public void onResponse(Call<BaseUrlModel> call, Response<BaseUrlModel> response) {
                        AppModel.getInstance().hideLoader();
                        if (response.isSuccessful()) {
                            BaseUrlModel baseUrlModel = response.body();

                            if (baseUrlModel.getBaseUrl() != null && !baseUrlModel.getBaseUrl().isEmpty()) {
                                String baseUrl = baseUrlModel.getBaseUrl();
                                baseUrl = baseUrl.substring(0, baseUrl.length() - 1); // to remove back slash
                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.baseurlkey, baseUrl);
                            } else {
                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.baseurlkey, getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/);
                            }

                            if (baseUrlModel.getImageUrl() != null && !baseUrlModel.getImageUrl().isEmpty()) {
                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.imagebaseurlkey, baseUrlModel.getImageUrl());
                            } else {
                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.imagebaseurlkey, getString(R.string.URL_DOWNLOAD_PROFILE_IMAGES)/*AppConstants.URL_DOWNLOAD_PROFILE_IMAGES*/);
                            }


                        } else {
                            AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.baseurlkey, getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/);
                            AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.imagebaseurlkey, getString(R.string.URL_DOWNLOAD_PROFILE_IMAGES)/*AppConstants.URL_DOWNLOAD_PROFILE_IMAGES*/);
                        }

                        AuthenticateUser();
                    }

                    @Override
                    public void onFailure(Call<BaseUrlModel> call, Throwable t) {
                        AppModel.getInstance().hideLoader();
                        AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.baseurlkey, getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/);
                        AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.imagebaseurlkey,  getString(R.string.URL_DOWNLOAD_PROFILE_IMAGES)/*AppConstants.URL_DOWNLOAD_PROFILE_IMAGES*/);
                        AuthenticateUser();
                    }
                });
            } else
                userName.setError("Username is required");
        } else {
            // internet not available check for previously login credentials from database
            if (DatabaseHelper.getInstance(this).getCurrentLoggedInUser() != null) {
                //if available give access
                UserModel userModel = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
                if (name.equals(userModel.getUsername()) && AppModel.getInstance().encryptPassword(pass)
                        .equals(userModel.getLastpassword())) {

                    AppModel.getInstance().writeBooleanToSharedPreferences(LoginActivity.this, AppConstants.logoutKey, false);
                    Intent intent = new Intent(LoginActivity.this, NewDashboardActivity.class);
                    startActivity(intent);
                    saveLastUsername(name);
                    finish();
                } else {
                    if (!name.equals(userModel.getUsername())) {
                        Toast.makeText(LoginActivity.this, "Failed Login attempt. Your UserID is incorrect Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    if (!AppModel.getInstance().encryptPassword(pass).equals(userModel.getLastpassword())) {
                        Toast.makeText(LoginActivity.this, "Failed Login attempt. Password is incorrect Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        // show toast messages for invalid credentials
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }

    private void AuthenticateUser() {
//        SyncUtils.CreateSyncAccount(LoginActivity.this);
        DataSync ds = new DataSync(LoginActivity.this);
//        name = userName.getText().toString().trim();
//        name += "@tcf.org.pk";
//        pass = password.getText().toString().trim();
        if (AppModel.getInstance().isConnectedToInternet(this)) {
            // If internet available use APi to authenticate and perform operations
            if (!TextUtils.isEmpty(userName.getText().toString().trim())) {
                if (!TextUtils.isEmpty(password.getText().toString().trim())) {
                    // Validation complete perform login process

                    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    LoginRequestModel rlm = new LoginRequestModel();
                    rlm.setUsername(name);
                    rlm.setToken(pass);
                    rlm.setDeviceId(androidId);
                    rlm.setNotificationKey(AppModel.getInstance().getFCMToken(this));
                    rlm.setAppDatetime(AppModel.getInstance().getDateTime());
                    rlm.setAppVersion(AppModel.getInstance().getApplicationVersion(LoginActivity.this));

                    ApiInterface apiInterface = ApiClient.getClient(LoginActivity.this).create(ApiInterface.class);
                    Call<LoginResponseModel> call = apiInterface.loginAPI(rlm);
                    AppModel.getInstance().showLoader(this, "Authenticating User", "Please wait...");
                    call.enqueue(new Callback<LoginResponseModel>() {

                        @Override
                        public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                            if (response.isSuccessful()) {

                                AppModel.getInstance().writeBooleanToSharedPreferences(LoginActivity.this, AppConstants.logoutKey, false);

                                //Set login time:
                                AppModel.getInstance().setDateWhenLogin(LoginActivity.this,
                                        AppModel.getInstance().getDate());

                                //get appversion and store in error log
                                AppModel.getInstance().appendErrorLog(LoginActivity.this, "AppVersion: " + AppModel.getInstance().getAppVersionWithBuildNo(LoginActivity.this));
                                //get device id and store in both logs
                                AppModel.getInstance().appendErrorLog(LoginActivity.this, "Device Id: " + AppModel.getInstance().getDeviceId(LoginActivity.this));
                                AppModel.getInstance().appendLog(LoginActivity.this, "Device Id: " + AppModel.getInstance().getDeviceId(LoginActivity.this));

                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this,
                                        AppConstants.NetworkConnectionFrequencyKey,
                                        String.valueOf(response.body() != null ? response.body().getNetworkConnectionFrequency() : 240));

                                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(InternetStatusWorkManager.class).build();

                                try {
                                    WorkManager.getInstance(LoginActivity.this).enqueue(oneTimeWorkRequest);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (response.body().getUserPicture() != null) {
                                    EmployeeHelperClass.getInstance(LoginActivity.this).deleteUserImages(response.body().getUserId());

                                    String name = response.body().getUserPicture();
                                    name = name.replace("\\", "/");
                                    String fdir = StorageUtil.getSdCardPath(LoginActivity.this).getAbsolutePath() + "/TCF/TCF Images/";
                                    response.body().setUserPicture(fdir + name);

                                    UserImageModel userImageModel = new UserImageModel();
                                    userImageModel.setUser_id(response.body().getUserId());
                                    userImageModel.setUser_image_path(response.body().getUserPicture());
                                    userImageModel.setUploaded_on(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                    long id = EmployeeHelperClass.getInstance(LoginActivity.this).insertOrUpdateUserImage(userImageModel, LoginActivity.this);
                                    if (id > 0)
                                        AppModel.getInstance().appendLog(LoginActivity.this, "In UserImage updated. UserId = " + response.body().getUserId());
                                    else
                                        AppModel.getInstance().appendLog(LoginActivity.this, "Error occurred in updating user image. UserId = " + response.body().getUserId());
                                }

                                final LoginResponseModel lrm = response.body();
                                final ArrayList<SchoolModel> rSchoolModel = lrm.getSchools();
                                final ArrayList<AppModulesModel> rAppModules = lrm.getAppModules();

                                if (rAppModules != null && rAppModules.size() != 0) {
                                    AppModel.getInstance().setEnabledModules(new WeakReference<>(LoginActivity.this), rAppModules);
                                }

                                if (rSchoolModel != null && rSchoolModel.size() != 0) {
                                    final UserModel um = new UserModel();
                                    um.setId(lrm.getUserId());
                                    um.setRoleId(lrm.getRoleId());
                                    um.setDepartment_Id(lrm.getDepartment_Id());
                                    um.setStatus(lrm.getStatus());
                                    um.setSession_token(lrm.getToken());
                                    um.setFirstname(lrm.getFirstname());
                                    um.setLastname(lrm.getLastname());
                                    um.setEmail(lrm.getEmail());
                                    um.setDesignation(lrm.getDesignation());
                                    um.setUsername(name);
                                    um.setLastpassword(AppModel.getInstance().encryptPassword(pass));
                                    um.setLastpassword_3(lrm.getLastPassword_3());
                                    um.setLastpassword_2(lrm.getLastPassword_2());
                                    um.setLastpassword_1(lrm.getLastPassword_1());
                                    try {
                                        if(lrm.getPassword_change_onlogin().toLowerCase().equals("false"))
                                            um.setPassword_change_on_login(0);
                                        else
                                            um.setPassword_change_on_login(1);
                                    } catch (Exception e) {
                                        um.setPassword_change_on_login(0);
                                        e.printStackTrace();
                                    }
//                                    um.setPassword_change_on_login();

//                                    if (lrm.getUserId() == 2222) {
//                                        SurveyAppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.HIDE_FEES_COLLECTION, "1");
//                                    } else {
//                                        SurveyAppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.HIDE_FEES_COLLECTION, "0");

//                                    }

                                    if (lrm.getRoleId() == AppConstants.roleId_27_P)
                                        um.setRole("P");
                                    if (lrm.getRoleId() == AppConstants.roleId_8_AM)
                                        um.setRole("AM");
                                    if (lrm.getRoleId() == AppConstants.roleId_9_AC)
                                        um.setRole("AC");
                                    if (lrm.getRoleId() == AppConstants.roleId_101_ST)
                                        um.setRole("ST"); //Senior Teacher
                                    if (lrm.getRoleId() == AppConstants.roleId_102_AA)
                                        um.setRole("AA"); // Admin Assistant
                                    if (lrm.getRoleId() == AppConstants.roleId_103_V)
                                        um.setRole("V"); //Viewer
                                    if (lrm.getRoleId() == AppConstants.roleId_7_AEM)
                                        um.setRole("AEM"); //AEM
                                    if (lrm.getRoleId() == AppConstants.roleId_109_CM)
                                        um.setRole("CM"); //Cluster Manager

                                    if (lrm.getRoleId() == AppConstants.roleId_27_P || lrm.getRoleId() == AppConstants.roleId_101_ST ||
                                            lrm.getRoleId() == AppConstants.roleId_102_AA || lrm.getRoleId() == AppConstants.roleId_103_V ||
                                            lrm.getRoleId() == AppConstants.roleId_109_CM ||
                                            lrm.getRoleId() == AppConstants.roleId_7_AEM) {

                                        try {
                                            Thread setTokenThread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AppModel.getInstance().setToken(LoginActivity.this, um, lrm.getToken(),
                                                            lrm.getFirstname(), name, pass,
                                                            String.valueOf(lrm.getRoleId()), rSchoolModel);
                                                }
                                            });
                                            setTokenThread.start();
                                            setTokenThread.join();

                                            Thread deleteSchoolThread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    for (SchoolModel sm : rSchoolModel) {
                                                    DatabaseHelper.getInstance(LoginActivity.this).deleteSchool(rSchoolModel);
//                                                    }
                                                }
                                            });
                                            deleteSchoolThread.start();
                                            deleteSchoolThread.join();


                                            final int schoolId = rSchoolModel.get(0).getId();
//                                        boolean onlyOneSchool = true;

                                            Thread addSchoolThread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    DatabaseHelper.getInstance(LoginActivity.this).addSchool(rSchoolModel);

                                                    for (SchoolModel sm : rSchoolModel) {
                                                        if (onlyOneSchool && schoolId != sm.getId())
                                                            onlyOneSchool = false;
                                                    }

                                                    AppModel.getInstance().syncMetaDataWithSchoolIdZero(LoginActivity.this);
                                                }
                                            });
                                            addSchoolThread.start();
                                            addSchoolThread.join();

                                            Thread performOperationsThread = new Thread(() -> {
                                                //this will tell that login button clicked
                                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.syncActionPerformedFrom, "login");

                                                //Save allowed modules to preference
                                                AppModel.getInstance().saveAllowedModuleToPreference(LoginActivity.this, rSchoolModel, um);


                                                //Drop Table Calendar
                                                DatabaseHelper.getInstance(LoginActivity.this).dropTable(DatabaseHelper.TABLE_CALENDAR);
//
//                                        //Create New Calendar Table
                                                DatabaseHelper.getInstance(LoginActivity.this).createTable(DatabaseHelper.CREATE_TABLE_CALENDAR);

                                                //set keepalive to start imedeatly
                                                AppModel.getInstance().writeToSharedPreferences(LoginActivity.this, AppConstants.START_KEEPALIVE, "0");

                                                //delete the calendar table
//                                        DatabaseHelper.getInstance(LoginActivity.this).deleteCalendar();

                                                //Temperory creating and inserting should be populated from api service
                                                FeesCollection.getInstance(LoginActivity.this).dropTempFeeTypeTable();
                                                FeesCollection.getInstance(LoginActivity.this).createFeeTypeTable();
                                                FeesCollection.getInstance(LoginActivity.this).insertTempDataInFeeTypeTable();

                                                FeesCollection.getInstance(LoginActivity.this).dropTempTransactionTypeTable();
                                                FeesCollection.getInstance(LoginActivity.this).createTransactionTypeTable();
                                                FeesCollection.getInstance(LoginActivity.this).insertTempDataInTransactionTypeTable();

                                                FeesCollection.getInstance(LoginActivity.this).dropTempTransactionCategoryTable();
                                                FeesCollection.getInstance(LoginActivity.this).createTransactionCategoryTable();
                                                FeesCollection.getInstance(LoginActivity.this).insertTempDataInTransactionCategoryTable();


                                                //App Sync Progress delete previous month records
                                                SyncProgressHelperClass.getInstance(LoginActivity.this).deletePreviousMonthsAppSyncStatusThatNotUploaded();

                                                final boolean isEmployeeHidden = AppModel.getInstance().readFromSharedPreferences(LoginActivity.this,
                                                        AppConstants.HIDE_Employee).equals("1");

                                                boolean isTCTEntryHidden = AppModel.getInstance().readFromSharedPreferences(LoginActivity.this,
                                                        AppConstants.HIDE_TCTEntry).equals("1");

                                                final boolean isExpenseHidden = AppModel.getInstance().readFromSharedPreferences(LoginActivity.this,
                                                        AppConstants.HIDE_Expense).equals("1");

//                                                    final boolean isEmployeeAtteHidden = SurveyAppModel.getInstance().readFromSharedPreferences(LoginActivity.this,
//                                                            AppConstants.HIDE_EmployeeAttendance).equals("1");

                                                if (!isEmployeeHidden) {
//                                                    List<EmployeeDesignationModel> edm = EmployeeHelperClass.getInstance(LoginActivity.this).getDesignations();
//                                                    if(edm.isEmpty()){
                                                    ds.syncEmployeeMetaData(LoginActivity.this);
//                                                    }

                                                    ds.uploadUserImages();
                                                    ds.syncSeparationDetailData(LoginActivity.this);

                                                    /*Thread separationImageDownloadThread = new Thread(() -> {
                                                        List<SchoolModel> schoolList = DatabaseHelper.getInstance(LoginActivity.this).getAllUserSchools();
                                                        for (SchoolModel model : schoolList) {
                                                            ds.DownloadSeparationImages(model.getId());
                                                        }
                                                    });
                                                    separationImageDownloadThread.start();*/


//                                                        EmployeeHelperClass.getInstance(LoginActivity.this).insertTempLeaveType();
//                                                    EmployeeHelperClass.getInstance(LoginActivity.this).insertTempEmployeesLeaveStatus();
//                                                    EmployeeHelperClass.getInstance(LoginActivity.this).insertTempEmployeesResignStatus();
//                                                        EmployeeHelperClass.getInstance(LoginActivity.this).insertTempEmployeesResignReason();
//                                                        EmployeeHelperClass.getInstance(LoginActivity.this).insertTempEmployeesResignType();
//                                                    EmployeeHelperClass.getInstance(LoginActivity.this).insertTempEmployeesTeacherAttendanceType();


//                                                    TCTHelperClass.getInstance(LoginActivity.this).insertTempTCTSubjects();
                                                    //Download if user is principal only for Metadata
                                                    if ((lrm.getRoleId() == AppConstants.roleId_27_P || lrm.getRoleId() == AppConstants.roleId_101_ST || lrm.getRoleId() == AppConstants.roleId_109_CM) && !isTCTEntryHidden) {
                                                        ds.syncTCTEmployeeData(LoginActivity.this, 0);
                                                    }
                                                }

                                                //TODO uncomment when expense module will be On
                                                if (!isExpenseHidden) {
                                                    //Download All expense data on login
                                                    if(lrm.getRoleId() != AppConstants.roleId_103_V)
                                                        ds.syncExpenseMetaData(LoginActivity.this);
                                                }

                                                //Download All help data on login
                                                ds.syncHelpData(LoginActivity.this);
                                                //when new login make this constant false
                                                AppConstants.feedbackDialogCanceledPressed = false;

//                                                    if (!isEmployeeAtteHidden) {
//                                                        EmployeeHelperClass.getInstance(LoginActivity.this).insertTempEmployeesTeacherAttendanceType();
//                                                    }


                                                AppModel.getInstance().setDateOfLogin(LoginActivity.this,
                                                        AppModel.getInstance().getDate());


                                                //Add Calendar Data
//                                        ArrayList<CalendarsModel> rCalendarsModel = lrm.getCalendars();
//                                        for (CalendarsModel cm:rCalendarsModel){
//                                            DatabaseHelper.getInstance(LoginActivity.this).addCalendar(cm);
//                                        }

                                                if (AppConstants.isSchoolAndClassSynced) {
                                                    if (onlyOneSchool) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                AppModel.getInstance().hideLoader();
                                                                AppModel.getInstance().showLoader(LoginActivity.this, "Loading School data", "Please wait...");
                                                            }
                                                        });
                                                        Log.d("SchoolIId", "" + rSchoolModel.get(0).getId());
                                                        AppModel.getInstance().setSelectedSchool(LoginActivity.this, rSchoolModel.get(0).getId());
                                                        AppModel.getInstance().setSpinnerSelectedSchool(LoginActivity.this, rSchoolModel.get(0).getId());

                                                        //if any role id has 1 school then set seacrchedschool id
                                                        AppModel.getInstance().setSearchedSchoolId(LoginActivity.this, rSchoolModel.get(0).getId());

                                                        if (Arrays.asList(rSchoolModel.get(0).getAllowedModule_App().split(",")).contains(AppConstants.FinanceModuleValue)) {
                                                            AppModel.getInstance().setFinanceSyncingCompleted(LoginActivity.this, rSchoolModel.get(0).getId());
                                                        }

                                                        AppModel.getInstance().syncMetaData(LoginActivity.this, LoginActivity.this, AppModel.getInstance().getSelectedSchool(LoginActivity.this));
//                                                    DataSync.syncEmployeeDataForSingleSchool(LoginActivity.this,AppModel.getInstance().getSelectedSchool(LoginActivity.this));


                                                    } else {
                                                        if (rSchoolModel.size() > 0) {
                                                            AppModel.getInstance().setSelectedSchool(LoginActivity.this, rSchoolModel.get(0).getId());
                                                            AppModel.getInstance().setSpinnerSelectedSchool(LoginActivity.this, rSchoolModel.get(0).getId());
                                                        }
                                                        Intent intent = new Intent(LoginActivity.this, School_list_Activity.class);
                                                        startActivity(intent);
                                                        AppModel.getInstance().hideLoader();
                                                        finish();
                                                    }
                                                } else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            MessageBox("Data not downloaded successfully. Please check your Internet connectivity");
                                                            AppModel.getInstance().disposeToken(LoginActivity.this);
                                                        }
                                                    });

                                                }
                                            });
                                            performOperationsThread.start();
                                            performOperationsThread.join();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        Toast.makeText(LoginActivity.this, "You are not authorized to use this App", Toast.LENGTH_LONG).show();
                                        AppModel.getInstance().hideLoader();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "User is not assigned to any school, therefore could not authenticate", Toast.LENGTH_LONG).show();
                                    AppModel.getInstance().hideLoader();
                                }
                                saveLastUsername(name);
                            } else {
                                //show error dialogs
                                String status = "", title = "Login Failed";
                                try {
                                    JSONObject js = new JSONObject(response.errorBody().string());
                                    if (js.has("status")) {
                                        status = js.getString("status");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 404) {
                                    if (!status.equalsIgnoreCase("null") && !status.isEmpty()) {
                                        AppModel.getInstance().showLoginErrorMessage(new WeakReference<>(LoginActivity.this), title, status);
//                                        Toast.makeText(LoginActivity.this, status, Toast.LENGTH_LONG).show();
                                    } else {
                                        AppModel.getInstance().showLoginErrorMessage(new WeakReference<>(LoginActivity.this), title, "Failed Login attempt. Your UserID is incorrect.");
//                                        Toast.makeText(LoginActivity.this, "Failed Login attempt. Your UserID is incorrect Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                } else if (response.code() == 403) {
                                    if (!status.equalsIgnoreCase("null") && !status.isEmpty()) {
                                        AppModel.getInstance().showLoginErrorMessage(new WeakReference<>(LoginActivity.this), title, status);
//                                        Toast.makeText(LoginActivity.this, status, Toast.LENGTH_LONG).show();
                                    } else {
                                        AppModel.getInstance().showLoginErrorMessage(new WeakReference<>(LoginActivity.this), title, "Failed Login attempt. Invalid Username or Password.");
//                                        Toast.makeText(LoginActivity.this, "Failed Login attempt. Password is incorrect Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    AppModel.getInstance().showLoginErrorMessage(new WeakReference<>(LoginActivity.this), title, "Cannot connect to the server");
//                                    Toast.makeText(LoginActivity.this, "Cannot connect to the server", Toast.LENGTH_LONG).show();
                                }
                                AppModel.getInstance().hideLoader();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
                            AppModel.getInstance().hideLoader();
                        }
                    });


                } else
                    password.setError("Password is required");

            } else
                userName.setError("Username is required");

        }
//        else {
//            // internet not available check for previously login credentials from database
//            if (DatabaseHelper.getInstance(this).getCurrentLoggedInUser() != null) {
//                //if available give access
//                UserModel userModel = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
//                if (name.equals(userModel.getUsername()) && AppModel.getInstance().encryptPassword(pass)
//                        .equals(userModel.getLastpassword())) {
//                    Intent intent = new Intent(LoginActivity.this, NewDashboardActivity.class);
//                    startActivity(intent);
//                    saveLastUsername(name);
//                    finish();
//                } else {
//                    if (!name.equals(userModel.getUsername())) {
//                        Toast.makeText(LoginActivity.this, "Failed Login attempt. Your UserID is incorrect Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                    if (!AppModel.getInstance().encryptPassword(pass).equals(userModel.getLastpassword())) {
//                        Toast.makeText(LoginActivity.this, "Failed Login attempt. Password is incorrect Please try again.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // show toast messages for invalid credentials
//                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//        }
    }

    @Override
    public void onResponse
            (Call<MetaDataResponseModel> call, Response<MetaDataResponseModel> response) {
        if (response.isSuccessful()) {
            AppModel.getInstance().appendErrorLog(LoginActivity.this, "Syncing metadata successful response code = " + response.code()
                    + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

            AppModel.getInstance().appendLog(LoginActivity.this, "Syncing metadata successful response code = " + response.code()
                    + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

            final MetaDataResponseModel mdrs = response.body();
            try {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (mdrs.getTCFClass() == null || mdrs.getTCFClass().size() == 0 || mdrs.getSection() == null || mdrs.getSection().size() == 0) {
                            AppConstants.isSchoolAndClassSynced = false;
                        } else {
                            AppConstants.isSchoolAndClassSynced = true;
                        }

                        DataSync ds = new DataSync(LoginActivity.this);
                        ds.SyncClasses(mdrs.getTCFClass());
                        ds.SyncSections(mdrs.getSection());
                        ds.SyncWithdrawalReasons(mdrs.getWithdrawal_Reason());
                        ds.SyncCampus(mdrs.getCampuses());
                        ds.SyncLocation(mdrs.getLocation());
                        ds.SyncAreas(mdrs.getArea());
                        ds.SyncRegion(mdrs.getRegion());
//                        ds.SyncSchoolSSRSummary(mdrs.getSchoolSSRSummary());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().appendLog(LoginActivity.this, "Syncing metadata ended current time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
                                AppModel.getInstance().hideLoader();
//                                if (DatabaseHelper.getInstance(LoginActivity.this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(LoginActivity.this)) <= 0)
//                                    AppModel.getInstance().showLoader(LoginActivity.this, "Loading Students Data", "Please wait...");
                            }
                        });

                        //check if students are already downloaded for the only school
//                        if (DatabaseHelper.getInstance(LoginActivity.this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(LoginActivity.this)) <= 0) {
//                            AppModel.getInstance().syncStudentData(LoginActivity.this,
//                                    AppModel.getInstance().getSelectedSchool(LoginActivity.this),
//                                    DatabaseHelper.getInstance(LoginActivity.this).getLatestModifiedOn(AppModel.getInstance().getSelectedSchool(LoginActivity.this)));
//                        }

//                        else {
//                            Intent in = new Intent(LoginActivity.this, NewDashboardActivity.class);
//                            startActivity(in);
//                            finish();
//                            AppModel.getInstance().startSyncService(LoginActivity.this);

//                        }
                    }
                });
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
                AppModel.getInstance().hideLoader();
            }
        } else {
            //show error dialogs
            Toast.makeText(this, "Error getting MetaData", Toast.LENGTH_LONG).show();

            JSONObject res = null;
            try {
                res = !response.errorBody().string().equals("null") &&
                        !response.errorBody().string().equals("") ?
                        new JSONObject(response.errorBody().string()) : new JSONObject();

                String msg = res.has("Message") ?
                        res.getString("Message") : "";

                AppModel.getInstance().appendErrorLog(LoginActivity.this, "Error getting MetaData response code = " + response.code()
                        + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a") + "Error message:" + msg);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (res == null) {
                AppModel.getInstance().appendErrorLog(LoginActivity.this, "Error getting MetaData response code = " + response.code()
                        + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
            }

            AppModel.getInstance().hideLoader();
            AppModel.getInstance().disposeToken(this);
        }
    }

    @Override
    public void onFailure(Call<MetaDataResponseModel> call, Throwable t) {
        Toast.makeText(this, "Sync fail", Toast.LENGTH_SHORT).show();
        Log.d("Sync Failed", t.getMessage());
        AppModel.getInstance().hideLoader();
    }


    private void saveLastUsername(String username) {
        AppModel.getInstance().writeToSharedPreferences(this, "lusername", username.split("@")[0]);
    }


    private boolean validation() {
        int i = 0;

        if (userName.getText().toString().isEmpty()) {
            String errorMessage = "Please enter username";
            userName.setError(errorMessage);
            userName.requestFocus();
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            i++;
        } else if (password.getText().toString().isEmpty()) {
            String errorMessage = "Please enter password";
            password.setError(errorMessage);
            password.requestFocus();
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            i++;
        } else if (!GpsLocationHelper.getInstance().isPermissionGranted(LoginActivity.this)) {
            i++;
            GpsLocationHelper.getInstance().checkLocationPermission(LoginActivity.this);
        }else if (!GpsLocationHelper.getInstance().isGpsOn(LoginActivity.this)){
            i++;
            GpsLocationHelper.getInstance().checkLocationPermission(LoginActivity.this);
        }

        return i == 0;
    }



    public void MessageBox(String message) {
        MessageBox(message, false, null);
    }

    public void MessageBox(String message, final boolean finishOnClose, final Intent intentToStart) {
        android.app.AlertDialog msg = new android.app.AlertDialog.Builder(this).create();
//        msg.setTitle("Warning");
        msg.setCancelable(false);
        msg.setMessage(message);
        msg.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (intentToStart != null) startActivity(intentToStart);
                if (finishOnClose) finish();
            }
        });
        try {
            msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.i("Login", "onActivityResult: GPS Enabled by user");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(LoginActivity.this, R.string.text_location_permission, Toast.LENGTH_LONG).show();
                        Log.i("Login", "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GpsLocationHelper.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.

                    GpsLocationHelper.getInstance().enableLoc(LoginActivity.this);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Intent settingintent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    settingintent.setData(Uri.fromParts("package",LoginActivity.this.getPackageName(), null));
                    startActivity(settingintent);
                    Toast.makeText(LoginActivity.this, R.string.text_location_permission, Toast.LENGTH_LONG).show();
                    Log.i("Login", "onRequestPermissionsResult: User denied permission");
                }
                return;
            }

        }
    }
}
