package com.tcf.sma.Models;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.multidex.BuildConfig;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.HR.ManageResignationsActivity;
import com.tcf.sma.Activities.HRTCT.EmployeeTCT_EntryActivity;
import com.tcf.sma.Activities.LoginActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Activities.StudentErrorActivity;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Helpers.DbTables.FeesCollection.SessionInfo;
import com.tcf.sma.Helpers.DbTables.Global.GlobalHelperClass;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
//import com.tcf.sma.Interfaces.CameraPickClick;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.RetrofitModels.AppModulesModel;
import com.tcf.sma.Models.RetrofitModels.MetaDataResponseModel;
import com.tcf.sma.R;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
import com.tcf.sma.SyncClasses.GenericAccountService;
import com.tcf.sma.SyncClasses.SyncUtils;
import com.tcf.sma.utils.MyLocation;
import com.vansuita.pickimage.listeners.IPickClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Mohammad.Haseeb on 12/15/2016.
 */

public class AppModel {

    private static AppModel instance;
    public String Token = "";
    public Account newAccount = null;
    public boolean IsDashboard = false;
    public String fragmentTag = "";
    public ArrayList<AttendanceModel> attendanceModels;
    public ProgressDialog progressDialog;
    public byte[] img1;
    public byte[] img2;
    public byte[] img3;
    public int photoFlag;
    public float btnPositionY;
    public float btnPositionX;
    private SharedPreferences sharedPref;

    public static AppModel getInstance() {
        if (instance == null)
            instance = new AppModel();

        return instance;

    }

    public static boolean isConnnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {

            Toast.makeText(context, "Network isn't Available", Toast.LENGTH_SHORT).show();
        }
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void setToken(Context context, UserModel um, String sessionToken, String name, String username, String password, String role, ArrayList<SchoolModel> smList) {

        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("session_token", sessionToken);
        editor.putString("name", name);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("role", role);
        editor.apply();

        DatabaseHelper.getInstance(context).deleteAllUser();
        DatabaseHelper.getInstance(context).deleteAllUserSchool();
        DatabaseHelper.getInstance(context).addUser(um);
//        for (SchoolModel sm : smList) {
        DatabaseHelper.getInstance(context).addUserSchool(smList);
//        }

    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(
                    Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFCMToken(String token, Context context) {

        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fcmtoken", token);
        editor.commit();
        DatabaseHelper.getInstance(context).updateUser(DatabaseHelper.getInstance(context).KEY_FCM_TOKEN, token);


    }

    public void setCustomDatabaseFiles(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Class[] argTypes = new Class[]{HashMap.class};
                Method setCustomDatabaseFiles = debugDB.getMethod("setCustomDatabaseFiles", argTypes);
                HashMap<String, Pair<File, String>> customDatabaseFiles = new HashMap<>();
                // set your custom database files
                customDatabaseFiles.put(DatabaseHelper.DATABASE_NAME + "fgg",
                        new Pair<>(new File(StorageUtil.getSdCardPath(context) + File.separator + "TCF" + File.separator + "Database" + File.separator + DatabaseHelper.DATABASE_NAME + ".db"), ""));

                Log.d("DBFilePath",
                        new File(StorageUtil.getSdCardPath(context) + File.separator + "TCF" + File.separator +
                                "Database" + File.separator + DatabaseHelper.DATABASE_NAME + ".db").getAbsolutePath());
                setCustomDatabaseFiles.invoke(null, customDatabaseFiles);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
    }

    public String getFCMToken(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String token = sharedPref.getString("fcmtoken", null);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getMonths() {

        List<String> dates = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");

        Calendar calendar = Calendar.getInstance();

        String currYear = sdf3.format(calendar.getTime());
        String currMonth = sdf2.format(calendar.getTime());

        int currMMMM = Integer.valueOf(currMonth);
        int currYYYY = Integer.valueOf(currYear);
        if (currMMMM < 6) {
            calendar.set(Calendar.MONTH, 6);
            calendar.set(Calendar.YEAR, currYYYY - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            dates.add(sdf.format(calendar.getTime()));

            do {
                calendar.add(Calendar.MONTH, 1);
                dates.add(sdf.format(calendar.getTime()));

                currMonth = sdf2.format(calendar.getTime());
                currMMMM = Integer.valueOf(currMonth);


            } while (currMMMM != 6);

        } else {
            calendar.set(Calendar.MONTH, 6);
            calendar.set(Calendar.DAY_OF_MONTH, 1);  //from date in this case will be from 1st july

            dates.add(sdf.format(calendar.getTime()));

            do {
                calendar.add(Calendar.MONTH, 1);
                dates.add(sdf.format(calendar.getTime()));

                currMonth = sdf2.format(calendar.getTime());
                currMMMM = Integer.valueOf(currMonth);

            } while (currMMMM != 6);

        }

        return dates;
    }

    public String[] getGraphFromToDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        String currYear = sdf3.format(calendar.getTime());
        String currMonth = sdf2.format(calendar.getTime());

        int currYYYY = Integer.valueOf(currYear);
        int currMMMM = Integer.valueOf(currMonth);

        String fromDate, toDate;

        if (currMMMM < 6) {
            calendar.set(Calendar.MONTH, 6);
            calendar.set(Calendar.YEAR, currYYYY - 1); //from date in this case will be from 1st july
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            fromDate = sdf.format(calendar.getTime());

            calendar1.set(Calendar.YEAR, currYYYY);
            calendar1.set(Calendar.MONTH, 6);      //to date in this case will be 31 or 30 june
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            calendar1.add(Calendar.DATE, -1);
            toDate = sdf.format(calendar1.getTime());
        } else {
            calendar.set(Calendar.MONTH, 6);
            calendar.set(Calendar.DAY_OF_MONTH, 1);  //from date in this case will be from 1st july
            fromDate = sdf.format(calendar.getTime());

            calendar1.set(Calendar.YEAR, currYYYY + 1);
            calendar1.set(Calendar.MONTH, 6);
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            calendar1.add(Calendar.DATE, -1);  //to date in this case will be 31st or 30 dec
            toDate = sdf.format(calendar1.getTime());
        }

        String[] dts = new String[2];
        dts[0] = fromDate;
        dts[1] = toDate;

        return dts;
    }

    public void setSelectedSchool(Context context, int schoolId) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("schoolId", schoolId);
        editor.commit();

    }

    public void setuserSchool(Context context, String userSchoolIds) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userSchoolIds", userSchoolIds);
        editor.commit();

    }

    public void disposeToken(Context context) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("session_token", null);
        editor.putString("name", null);
        editor.putString("username", null);
        editor.putString("password", null);
        editor.putString("role", null);
//        editor.putString(AppConstants.baseurlkey,null);
//        editor.putString(AppConstants.imagebaseurlkey,null);
        editor.apply();
    }

    public void removeSyncAccount(Context context) {
        Account account = GenericAccountService.GetAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager != null)
            accountManager.removeAccount(account, null, null);
    }

    public String getToken(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String token = sharedPref.getString("session_token", null);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSelectedSchool(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            int schoolId = sharedPref.getInt("schoolId", -1);
            return schoolId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setSpinnerSelectedSchool(Context context, int schoolId) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("spSchoolId", schoolId);
        editor.commit();

    }

    public int getSpinnerSelectedSchool(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            int schoolId = sharedPref.getInt("spSchoolId", -1);
            return schoolId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void clearSpinnerSelectedSchool(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("spSchoolId", 0);
        editor.commit();
    }

    public void setSearchedSchoolId(Context context, int schoolId) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("SearchedSchoolId", schoolId);
        editor.commit();

    }

    public int getSearchedSchoolId(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            int schoolId = sharedPref.getInt("SearchedSchoolId", -1);
            return schoolId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void clearSearchedSchoolId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SearchedSchoolId", 0);
        editor.commit();
    }

    public void clearSelectedSchool(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("schoolId", 0);
        editor.commit();
    }

    public String getuserSchoolIDS(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String userSchoolIds = sharedPref.getString("userSchoolIds", "");
            return userSchoolIds;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUsername(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String username = sharedPref.getString("username", "");

            if (username == null)
                return "";
            else
                return username;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPassword(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String password = sharedPref.getString("password", "");

            if (password == null)
                return "";
            else
                return password;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDateOfLogin(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String dateoflogin = sharedPref.getString("dateoflogin", "");

            if (dateoflogin == null)
                return "";
            else
                return dateoflogin;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setDateOfLogin(Context context, String date) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("dateoflogin", date);
        editor.commit();

    }

    public void writeToSharedPreferences(Context context, String key, String value) {

        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String readFromSharedPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "false");
    }

    public String readFromSharedPreferences(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public void writeBooleanToSharedPreferences(Context context, String key, boolean value) {

        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean readBooleanFromSharedPreferences(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public void writeLongToSharedPreferences(Context context, String key, long value) {

        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long readLongFromSharedPreferences(Context context, String key, long defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getLong(key, defaultValue);
    }

    public Bitmap rotateImage(byte[] data, Display d) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        int x = d.getWidth();
        int y = d.getHeight();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, y, x + 200, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public Bitmap rotateImage(Bitmap bitmap, Display d) {
        int x = d.getWidth();
        int y = d.getHeight();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, y, x + 200, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public String saveImageToStorage2(byte[] data, Context context, String grNumber, int flag, int schoolId) {
        String path = null;
        if (data != null) {
            File folder = new File(StorageUtil.getSdCardPath(context) +
                    File.separator + "TCF" + File.separator + "TCF Images");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                try {
                    File file = new File(folder, schoolId + "_" + grNumber + "_" + flag + ".JPG");
                    FileOutputStream outStream = new FileOutputStream(file);
                    outStream.write(data);
                    outStream.close();
                    path = folder.getAbsolutePath() + File.separator + schoolId + "_" + grNumber + "_" + flag + ".JPG";

                } catch (Exception e) {
                    Log.d("asdf", "adf");
                }
            } else {
                Log.d("file", "error");
            }
        }
        return path;
    }

    public void setFullScreenActivity(Activity mContext) {
        mContext.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setStatusBarColor(Activity context) {
        if (Build.VERSION.SDK_INT >= 21) {
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void DatePicker(final EditText date, Activity context) {
        try {
            if (!date.getText().toString().trim().isEmpty()) {
                final Calendar calendar = getCalendar(date.getText().toString());
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        date.setText(formatDate(year, month, dayOfmonth, true));
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            } else {
                final Calendar calendar = getCalendar(convertDatetoFormat(getDate(), "yyyy-MM-dd", "dd-MMM-yy"));
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        date.setText(formatDate(year, month, dayOfmonth, true));
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            final Calendar calendar = getCalendar(convertDatetoFormat(getDate(), "yyyy-MM-dd", "dd-MMM-yy"));
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                    date.setText(formatDate(year, month, dayOfmonth, true));
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
    }


    public void DatePicker2(final EditText date, Activity context) {
        try {
            if (!date.getText().toString().trim().isEmpty()) {
                final Calendar calendar = getCalendar(date.getText().toString());
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        date.setText(formatDate2(year, month, dayOfmonth, true));
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            } else {
                final Calendar calendar = getCalendar(convertDatetoFormat(getDate(), "yyyy-MM-dd", "dd-MMM-yyyy"));
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        date.setText(formatDate2(year, month, dayOfmonth, true));
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            final Calendar calendar = getCalendar(convertDatetoFormat(getDate(), "yyyy-MM-dd", "dd-MMM-yyyy"));
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                    date.setText(formatDate2(year, month, dayOfmonth, true));
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
    }

    public void datePickerForAdmission(final EditText date, Activity context) {
        try {
            if (!date.getText().toString().trim().isEmpty()) {
                final Calendar calendar = getCalendar(date.getText().toString());
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        date.setText(formatDate(year, month, dayOfmonth, true));
                    }
                }, mYear, mMonth, mDay);
                Calendar calendar1 = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());

                datePickerDialog.show();
            } else {
                final Calendar calendar = getCalendar(convertDatetoFormat(getDate(), "yyyy-MM-dd", "dd-MMM-yy"));
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        date.setText(formatDate(year, month, dayOfmonth, true));
                    }
                }, mYear, mMonth, mDay);
                Calendar calendar1 = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());

                datePickerDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            final Calendar calendar = getCalendar(convertDatetoFormat(getDate(), "yyyy-MM-dd", "dd-MMM-yy"));
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                    date.setText(formatDate(year, month, dayOfmonth, true));
                }
            }, mYear, mMonth, mDay);
            Calendar calendar1 = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());

            datePickerDialog.show();
        }
    }

    public void DatePicker(final EditText date, Activity context, Calendar selectedDate) {
        int mYear = selectedDate.get(Calendar.YEAR);
        int mMonth = selectedDate.get(Calendar.MONTH);
        int mDay = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                date.setText(formatDate(year, month, dayOfmonth, true));

            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public void DatePicker(final TextView date, Activity context) {
        final Calendar calendar = getCalendar(date.getText().toString());

        if (calendar != null) {
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                    date.setText(formatDate(year, month, dayOfmonth, true));
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
    }

    public Calendar getCalendar(String date) {
        Calendar calendar = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
            Date dated = sdf.parse(date);
            calendar = Calendar.getInstance();
            calendar.setTime(dated);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            return calendar;
        }
    }

    public boolean isConnectedToInternet(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public String getCurrentDateTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return (df.format(new Date()));
    }

    public String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(Calendar.getInstance().getTime());
    }

    public String getFirstDayOfMonth(String format) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }

    public String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        return df.format(Calendar.getInstance().getTime());
    }

    public String getDateTimeIn24Format() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return df.format(Calendar.getInstance().getTime());
    }

    public String convertDatetoFormat(String date, String fromFormat, String toFormat) {
        try {
            if (date != null && !date.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat(fromFormat);
                Date dtForDate = null;
                dtForDate = df.parse(date);
                date = DateFormat.format(toFormat, dtForDate).toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public void setEnabledModules(WeakReference<Context> mContext, ArrayList<AppModulesModel> list) {
        try {
            SharedPreferences.Editor preferences = mContext.get().
                    getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit();
            StringBuilder allowedModules = new StringBuilder();

            for (AppModulesModel model : list) {
                if (model.getMinVersion() != null && !Strings.isEmptyOrWhitespace(getAppVersion(mContext.get())) && Double.parseDouble(getAppVersion(mContext.get())) >= Double.parseDouble(model.getMinVersion())) {
//                if (model.getMinVersion() != null) {
                    allowedModules.append(model.getModule_ID()).append(",");
                }
//            else {
//                showMessage(mContext, "Info!", model.getModule_Name() + " Module has been disabled because current version of your app does not match our latest version");
//            }
            }
            allowedModules = new StringBuilder(allowedModules.toString().replaceAll(",$", ""));
            preferences.putString("allowed_modules", allowedModules.toString());
            preferences.apply();
        } catch (Exception e) {
            appendErrorLog(mContext.get(), "Error in setEnabledModules method: " + e.getMessage());
        }
    }

    public void showMessage(WeakReference<Context> mContext, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Okay", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public void showLoginErrorMessage(WeakReference<Context> mContext, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get());

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Try Again", (dialogInterface, i) -> dialogInterface.dismiss());
//        builder.setNeutralButton("Okay", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public EnabledModules getEnabledModules(WeakReference<Context> mContext) {
        EnabledModules modules = new EnabledModules();
        String allModules = mContext.get().
                getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).getString("allowed_modules", "");
        if (allModules != null) {
            List<String> allowedModules = Arrays.asList(allModules.split(","));
            if (allowedModules.contains(AppConstants.StudentModuleValue)) {
                modules.setModuleStudentEnabled(true);
            }
            if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                modules.setModuleFinanceEnabled(true);
            }
            if (allowedModules.contains(AppConstants.StudentPromotionModuleValue)) {
                modules.setModuleStudentPromotionEnabled(true);
            }
            if (allowedModules.contains(AppConstants.StudentGraduationModuleValue)) {
                modules.setModuleStudentGraduationEnabled(true);
            }
            if (allowedModules.contains(AppConstants.CalendarModuleValue)) {
                modules.setModuleCalendarEnabled(true);
            }

            if (allowedModules.contains(AppConstants.HREmployeeListingModuleValue)) {
                modules.setModuleHREnabled(true);
                modules.setModuleHREmployeeListingEnabled(true);
            }
            if (allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                modules.setModuleHREnabled(true);
                modules.setModuleHRAttendanceAndLeavesEnabled(true);
            }
            if (allowedModules.contains(AppConstants.HRResignationModuleValue)) {
                modules.setModuleHREnabled(true);
                modules.setModuleHRResignationEnabled(true);
            }
            if (allowedModules.contains(AppConstants.HRTerminationModuleValue)) {
                modules.setModuleHREnabled(true);
                modules.setModuleHRTerminationEnabled(true);
            }
            if (allowedModules.contains(AppConstants.TCTModuleValue)) {
                modules.setModuleHREnabled(true);
                modules.setModuleTCTEntryEnabled(true);
            }
            if (allowedModules.contains(AppConstants.StudentTransferModuleValue)) {
                modules.setModuleStudentTransferEnabled(true);
            }
            if (allowedModules.contains(AppConstants.StudentVisitFormsModuleValue)) {
                modules.setModuleStudentVisitFormsEnabled(true);
            }

            if (allowedModules.contains(AppConstants.ExpenseModuleValue)) {
                modules.setModuleExpenseEnabled(true);
            }

        }
        return modules;
    }

    public long getDaysBetweenDates(String passwordChangeDate, String currentDate, String DATE_FORMAT) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date passChangeDate, currDate;
        long numberOfDays = 0;
        try {
            passChangeDate = dateFormat.parse(passwordChangeDate);
            currDate = dateFormat.parse(currentDate);
            numberOfDays = getUnitBetweenDates(passChangeDate, currDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private long getUnitBetweenDates(Date passChangeDate, Date currDate, TimeUnit unit) {
        long timeDiff = currDate.getTime() - passChangeDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public String getLastSixMonthFromCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6); // to get previous year add -1
        Date nextMonth = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return (df.format(nextMonth));
    }

    public String getLastMonthFromCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // to get previous year add -1
        Date nextMonth = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return (df.format(nextMonth));
    }

    public String getLastWeekFromCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -7); // to get previous year add -1
        Date nextMonth = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return (df.format(nextMonth));
    }

    public String getYesterdayFromCurrentDate(String givenDate, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(givenDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            cal.add(Calendar.DAY_OF_WEEK, -1); // to get previous year add -1
            Date nextMonth = cal.getTime();
            return (sdf.format(nextMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return givenDate;
    }

    public Calendar StringToCalObject(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public String formatDate(int year, int month, int day, boolean showMonthName) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf;
        if (showMonthName)
            sdf = new SimpleDateFormat("dd-MMM-yyyy");//old one yy only
        else
            sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }


    public String formatDate2(int year, int month, int day, boolean showMonthName) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf;
        if (showMonthName)
            sdf = new SimpleDateFormat("dd-MMM-yyyy");
        else
            sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public void showLoader(Context context) {
        try {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoader(Context context, boolean cancelable) {
        try {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(cancelable);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showLoader(Context context, String title, String message) {
        try {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void syncMetaData(final Context context, Callback<MetaDataResponseModel> callback, final int schoolId) {

        String token = AppModel.getInstance().getToken(context);
        token = "Bearer " + token;

//        AppModel.getInstance().appendErrorLog(context, "Syncing metadata started with school id = " + 0
//                + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
//
//        AppModel.getInstance().appendLog(context, "Syncing metadata started with school id = " + 0
//                + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
//
        String classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_CLASS,
                DatabaseHelper.getInstance(context).CLASS_MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String section_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_SECTION,
                DatabaseHelper.getInstance(context).SECTION_MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String school_classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_SCHOOL_CLASS,
                DatabaseHelper.getInstance(context).SCHOOL_CLASS_MODIFIED_ON,
                DatabaseHelper.getInstance(context).SCHOOL_CLASS_SCHOOLID + "=" + schoolId, Integer.parseInt(AppConstants.GeneralModuleValue));

        String scholarship_category_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                Scholarship_Category.SCHOLARSHIP_CAT,
                Scholarship_Category.SCHOLARSHIP_CATEGORY_MODIFIED_ON,
                Scholarship_Category.SCHOOL_ID + "=" + schoolId, Integer.parseInt(AppConstants.GeneralModuleValue));

        String withdrawal_reasons_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.getInstance(context).TABLE_WITHDRAWAL_REASON,
                DatabaseHelper.getInstance(context).WITHDRAWAL_REASON_MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String calendars_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_CALENDAR,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String campus_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_CAMPUS,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String location_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_LOCATION,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String areas_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_AREA,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String region_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                context,
                DatabaseHelper.TABLE_REGION,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
//        Call<MetaDataResponseModel> call = apiInterface.getMetaData(0, token);
//        call.enqueue(callback); 

        Call<MetaDataResponseModel> call = apiInterface.getMetaData(schoolId,
                classes_modifiedOn, section_modifiedOn, school_classes_modifiedOn,
                scholarship_category_modifiedOn, withdrawal_reasons_modifiedOn, calendars_modifiedOn,
                campus_modifiedOn, location_modifiedOn, areas_modifiedOn, region_modifiedOn,
                token);

        AppModel.getInstance().appendErrorLog(context, "Syncing metadata started with school id = " + schoolId
                + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

        AppModel.getInstance().appendLog(context, "Syncing metadata started with school id = " + schoolId
                + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

        call.enqueue(new Callback<MetaDataResponseModel>() {
            @Override
            public void onResponse(Call<MetaDataResponseModel> call, Response<MetaDataResponseModel> response) {
                if (response.isSuccessful()) {
                    int targetFee = response.body().getTargetFee();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.SCHOOL_TARGET_AMOUNT, targetFee);
                    DatabaseHelper.getInstance(context).updateTableColumns(DatabaseHelper.TABLE_SCHOOL, values, schoolId);

                    DataSync ds = new DataSync(context);
                    ds.SyncSchoolClasses(response.body().getSchool_Classes(), schoolId);
                    ds.syncScholarshipCategory(response.body().getScholarship_Category(), schoolId);
                    ds.syncCalendar(response.body().getCalendars(), schoolId);

                    Intent in = new Intent(context.getApplicationContext(), NewDashboardActivity.class);
                    context.startActivity(in);
                    ((Activity) context).finish();
                    if (DatabaseHelper.getInstance(context).getStudentTableCount() > 0)
                        AppModel.getInstance().startSyncService(context, SyncProgressHelperClass.SYNC_TYPE_BAU_SYNC_ID);
                    else
                        AppModel.getInstance().startSyncService(context, SyncProgressHelperClass.SYNC_TYPE_FIRST_SYNC_ID);
                }
            }

            @Override
            public void onFailure(Call<MetaDataResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void syncMetaDataWithSchoolIdZero(final Activity activity) {

        String token = AppModel.getInstance().getToken(activity);
        token = "Bearer " + token;

        String classes_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.getInstance(activity.getApplicationContext()).TABLE_CLASS,
                DatabaseHelper.getInstance(activity.getApplicationContext()).CLASS_MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String section_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.getInstance(activity.getApplicationContext()).TABLE_SECTION,
                DatabaseHelper.getInstance(activity.getApplicationContext()).SECTION_MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));


        String withdrawal_reasons_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.getInstance(activity.getApplicationContext()).TABLE_WITHDRAWAL_REASON,
                DatabaseHelper.getInstance(activity.getApplicationContext()).WITHDRAWAL_REASON_MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String calendars_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.TABLE_CALENDAR,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String campus_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.TABLE_CAMPUS,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String location_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.TABLE_LOCATION,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String areas_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.TABLE_AREA,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

        String region_modifiedOn = AppModel.getInstance().getLastModifiedOn(
                activity.getApplicationContext(),
                DatabaseHelper.TABLE_REGION,
                DatabaseHelper.MODIFIED_ON,
                null, Integer.parseInt(AppConstants.GeneralModuleValue));

//        String religion_modifiedOn = AppModel.getInstance().getLastModifiedOn(
//                activity.getApplicationContext(),
//                GlobalHelperClass.getInstance(activity.getApplicationContext()).TABLE_RELIGION,
//                GlobalHelperClass.getInstance(activity.getApplicationContext()).modifiedOn,
//                null,Integer.parseInt(AppConstants.GeneralModuleValue));
//
//        String nationality_modifiedOn = AppModel.getInstance().getLastModifiedOn(
//                activity.getApplicationContext(),
//                GlobalHelperClass.getInstance(activity.getApplicationContext()).TABLE_NATIONALITY,
//                GlobalHelperClass.getInstance(activity.getApplicationContext()).modifiedOn,
//                null,Integer.parseInt(AppConstants.GeneralModuleValue));
//
//        String elective_subject_modifiedOn = AppModel.getInstance().getLastModifiedOn(
//                activity.getApplicationContext(),
//                GlobalHelperClass.getInstance(activity.getApplicationContext()).TABLE_ELECTIVE_SUBJECTS,
//                GlobalHelperClass.getInstance(activity.getApplicationContext()).modifiedOn,
//                null,Integer.parseInt(AppConstants.GeneralModuleValue));

        AppModel.getInstance().appendErrorLog(activity, "Syncing metadata started with school id = " + 0
                + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

        AppModel.getInstance().appendLog(activity, "Syncing metadata started with school id = " + 0
                + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

        ApiInterface apiInterface = ApiClient.getClient(activity).create(ApiInterface.class);
        Call<MetaDataResponseModel> call = apiInterface.getMetaData(0,
                classes_modifiedOn, section_modifiedOn, withdrawal_reasons_modifiedOn,
                campus_modifiedOn, location_modifiedOn, areas_modifiedOn, region_modifiedOn,
//                religion_modifiedOn,nationality_modifiedOn,elective_subject_modifiedOn
                token);
        try {
            Response<MetaDataResponseModel> response = call.execute();

            if (response.isSuccessful()) {
                AppModel.getInstance().appendErrorLog(activity, "Syncing metadata successful response code = " + response.code()
                        + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

                AppModel.getInstance().appendLog(activity, "Syncing metadata successful response code = " + response.code()
                        + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));

                AppConstants.isSchoolAndClassSynced = true;

                final MetaDataResponseModel mdrs = response.body();
                try {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {


//                            if (mdrs.getTCFClass() == null || mdrs.getTCFClass().size() == 0 || mdrs.getSection() == null || mdrs.getSection().size() == 0) {
//                                AppConstants.isSchoolAndClassSynced = false;
//                            } else {
//                                AppConstants.isSchoolAndClassSynced = true;
//                            }

                            DataSync ds = new DataSync(activity);
                            ds.SyncClasses(mdrs.getTCFClass());
                            ds.SyncSections(mdrs.getSection());
                            ds.SyncWithdrawalReasons(mdrs.getWithdrawal_Reason());
                            ds.SyncCampus(mdrs.getCampuses());
                            ds.SyncLocation(mdrs.getLocation());
                            ds.SyncAreas(mdrs.getArea());
                            ds.SyncRegion(mdrs.getRegion());
                            ds.SyncAcademicSessions(mdrs.getAcademicSession());
                            ds.SyncReligion(mdrs.getReligion());
                            ds.SyncNationality(mdrs.getNationality());
                            ds.SyncElectiveSubject(mdrs.getElectiveSubjects());
//                        ds.SyncSchoolSSRSummary(mdrs.getSchoolSSRSummary());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppModel.getInstance().appendLog(activity, "Syncing metadata ended current time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
//                                    AppModel.getInstance().hideLoader();
////                                if (DatabaseHelper.getInstance(LoginActivity.this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(LoginActivity.this)) <= 0)
////                                    AppModel.getInstance().showLoader(LoginActivity.this, "Loading Students Data", "Please wait...");
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
                Toast.makeText(activity, "Error getting MetaData", Toast.LENGTH_LONG).show();
                AppConstants.isSchoolAndClassSynced = false;

                JSONObject res = null;
                try {
                    res = !response.errorBody().string().equals("null") &&
                            !response.errorBody().string().equals("") ?
                            new JSONObject(response.errorBody().string()) : new JSONObject();

                    String msg = res.has("Message") ?
                            res.getString("Message") : "";

                    AppModel.getInstance().appendErrorLog(activity, "Error getting MetaData response code = " + response.code()
                            + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a") + "Error message:" + msg);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (res == null) {
                    AppModel.getInstance().appendErrorLog(activity, "Error getting MetaData response code = " + response.code()
                            + " current Time:" + AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
                }

                AppModel.getInstance().hideLoader();
                AppModel.getInstance().disposeToken(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(activity, "Error In syncMetaDataWithSchoolIdZero method error = " + e.getMessage());
            AppConstants.isSchoolAndClassSynced = false;
            AppModel.getInstance().hideLoader();
        }
    }

    public double getFeeTargetAverage(WeakReference<Context> mContext, int schoolId) {
        ArrayList<Double> feeList = DatabaseHelper.getInstance(mContext.get()).getStudentsFees(schoolId, 0, 0, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return feeList.stream().mapToDouble(value -> value).average().orElse(0.0);
        } else {
            double sum = 0.0;
            if (!feeList.isEmpty()) {
                for (Double mark : feeList) {
                    sum += mark;
                }
                return sum / feeList.size();
            }
            return sum;
        }
    }

//    public void syncStudentData(final Context context, final int schoolId, String dateFrom) {
//
//        AppModel.getInstance().setSelectedSchool(context, schoolId);
//        String token = AppModel.getInstance().getToken(context);
//        token = "Bearer " + token;
//        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
//        Call<StudentDataResponseModel> call = apiInterface.getStudentData(schoolId, dateFrom, token);
//        call.enqueue(new Callback<StudentDataResponseModel>() {
//            @Override
//            public void onResponse(Call<StudentDataResponseModel> call, final Response<StudentDataResponseModel> response) {
//                if (response.isSuccessful()) {
//
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//                                final StudentDataResponseModel sdrm = response.body();
//                                if (sdrm != null) {
//                                    final DataSync ds = new DataSync(context);
//
//                                    Thread studentThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ds.SyncStudents(sdrm.getStudent(), schoolId);
//                                        }
//                                    });
//                                    studentThread.start();
//                                    studentThread.join();
//
//                                    Thread attendanceThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ds.SyncAttendance(sdrm.getAttendance(), sdrm.getStudent_Attendance(), schoolId);
//                                        }
//                                    });
////
//                                    attendanceThread.start();
//                                    attendanceThread.join();
//
//                                    Thread promotionThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ds.SyncPromotion(sdrm.getPromotion(), sdrm.getPromotion_Student(),schoolId);
//                                        }
//                                    });
//                                    promotionThread.start();
//                                    promotionThread.join();
//
//                                    Thread withdrawalThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ds.SyncWithdrawal(sdrm.getWithdrawal(),schoolId);
//                                        }
//                                    });
//                                    withdrawalThread.start();
//                                    withdrawalThread.join();
//
//                                    Thread schoolAuditThread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ds.SyncSchoolAudits(sdrm.getSchoolAudit(),schoolId);
//
//                                            ((Activity) context).runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Intent intent = new Intent(context, NewDashboardActivity.class);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                    context.startActivity(intent);
//                                                    AppModel.getInstance().hideLoader();
//                                                    ((Activity) context).finish();
//                                                    startSyncService(context);
//
//                                                }
//                                            });
//                                        }
//                                    });
//                                    schoolAuditThread.start();
//                                    schoolAuditThread.join();
//                                }
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                                AppModel.getInstance().hideLoader();
//                            }
//                        }
//                    });
//                    thread.start();
//                } else {
//                    //show error dialogs
//                    Toast.makeText(context, "Error loading Students Data", Toast.LENGTH_LONG).show();
//                    AppModel.getInstance().hideLoader();
//                    if (((Activity) context) instanceof LoginActivity) {
//                        Intent intent = new Intent(context, SchoolDashboardActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        context.startActivity(intent);
//                        ((Activity) context).finish();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StudentDataResponseModel> call, Throwable t) {
//                AppModel.getInstance().hideLoader();
//            }
//        });
//    }

    public void appendLog(Context context, String text) {
//        if (true)
//            return;
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String CurrentDate = String.valueOf(mDay) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mYear);

        String fileName = "Info_logs_" + CurrentDate + ".txt";
        File data = StorageUtil.getSdCardPath(context);
        File folderPath = new File(data + "/TCF/Logs");
        File filePath = new File(folderPath.getPath() + "/" + fileName);

        // create app folder
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }


        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(filePath, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public int populateSelectedSchoolInDropDown(List<SchoolModel> list, Context context) {
        int schoolId = this.getSpinnerSelectedSchool(context);
        if (list != null) {
            for (SchoolModel sm : list) {
                if (sm.getId() == schoolId)
                    return list.indexOf(sm);
            }
        }
        return -1;
    }

    public int populateSelectedSchoolInDropDown(List<SchoolModel> list, Context context, int schoolId) {
        if (schoolId < 1)
            schoolId = this.getSpinnerSelectedSchool(context);
        if (list != null) {
            for (SchoolModel sm : list) {
                if (sm.getId() == schoolId)
                    return list.indexOf(sm);
            }
        }
        return -1;
    }

    public void appendErrorLog(Context context, String text) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String CurrentDate = String.valueOf(mDay) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mYear);

        String fileName = "Error_logs_" + CurrentDate + ".txt";
        File data = StorageUtil.getSdCardPath(context);
        File folderPath = new File(data + "/TCF/Logs");
        File filePath = new File(folderPath.getPath() + "/" + fileName);

        // create app folder
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }


        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(filePath, true));
            buf.append(AppModel.getInstance().getCurrentDateTime(" dd-MM hh:mm a") + ":- " + text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] bitmapToByte(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public String getResponseString(Context context, Response<ResponseBody> entity) {

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(entity.body().byteStream()), 65728);
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return sb.toString();
    }


    public Bitmap setImage(String pictureType, String ProfileName, Activity mActivity, int grNo, boolean rotateImageTo90Degree) throws FileNotFoundException {
        try {
            String filename = ProfileName;
            if (filename == null) {
                int enrId = DatabaseHelper.getInstance(mActivity).getEnrollmentIdfromGR(grNo, AppModel.getInstance().getSelectedSchool(mActivity));
                filename = DatabaseHelper.getInstance(mActivity).getEnrollmentImage(enrId, pictureType).getFilename();
                if (filename.contains("emulated") || filename.contains("storage")) {
                    String[] fileArray = filename.split("/");
                    filename = fileArray[fileArray.length - 1];
                }
            }
            File f = new File(StorageUtil.getSdCardPath(mActivity).getAbsolutePath() + "/TCF/TCF Images/" + filename);

            if (rotateImageTo90Degree)
                return AppModel.getInstance().rotateImage(Compressor.getDefault(mActivity).compressToBitmap(f), mActivity.getWindowManager().getDefaultDisplay());
            else
                return Compressor.getDefault(mActivity).compressToBitmap(f);

        } catch (Exception e) {
            throw new FileNotFoundException();
        }
    }

    public void showPendingApprovalNotification(Context context, String title, String message, int id) {
        try {
            //if notification is already showing
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                assert mNotificationManager != null;
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent notificationIntent = new Intent(context, ManageResignationsActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID_TCT_Notificaton)
                    .setSmallIcon(R.mipmap.ic_launcher_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);

            createNotificationChannel(context, AppConstants.CHANNEL_ID_TCT_Notificaton, "tctnotifi", "tctnotifi", false);

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showErrorNotification(Context context, String message, int id) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent in = new Intent(context, StudentErrorActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID_Error_Notificaton)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Data syncing error")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        createNotificationChannel(context, AppConstants.CHANNEL_ID_Error_Notificaton, "errorsync", "errorsync", false);

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }


    public void showLocationDisabledNotification(Context context) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent in = new Intent(Intent.ACTION_MAIN);
            in.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, "com.android.packageinstaller.permission.ui.ManagePermissionsActivity"));

            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_ONE_SHOT);
            }

            notificationBuilder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID_Error_Notificaton)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Location Disabled!")
                    .setContentText("TCF SMA requires you to enable the location permission.")
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);

        } else {
            notificationBuilder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID_Error_Notificaton)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("GPS Disabled!")
                    .setContentText("TCF SMA requires you to enable the GPS.")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
        }

        createNotificationChannel(context, AppConstants.CHANNEL_ID_Error_Notificaton, "errorlocation", "errorlocation", false);

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(1122 /* ID of notification */, notificationBuilder.build());
    }

    public void showErrorNotification(Context context, String message) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent in = new Intent(context, StudentErrorActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID_Error_Notificaton)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Data syncing error")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        createNotificationChannel(context, AppConstants.CHANNEL_ID_Error_Notificaton, "errorsync", "errorsync", false);

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void createNotificationChannel(Context context, String CHANNEL_ID, String channelName, String channelDesc, boolean disableSoundAndVibration) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);

            if (disableSoundAndVibration) {
                channel.setSound(null, null);
                channel.enableVibration(false);
            }
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void maximizeFieldDialog(String text, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Value");
        builder.setMessage(text);
        builder.setCancelable(true);
        builder.show();
    }

    private String getAppVersion() {
        return BuildConfig.VERSION_NAME;
//        return new StringBuilder(BuildConfig.VERSION_NAME).deleteCharAt(BuildConfig.VERSION_NAME.length() - 2).toString();
    }

    public String getAppVersion(Context context) {
        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            versionName = version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public String readFile(Context context, String fname) {            // VERSION 0.5.8 SHAZ 01/16/2015

        File file = new File(fname);

        FileInputStream fin = null;
        String s = "";

        try {

            // create FileInputStream object
            fin = new FileInputStream(file);

            byte fileContent[] = new byte[(int) file.length()];

            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);
            //create string from byte array
            s = new String(fileContent);

            //System.out.println("File content: " + s);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
        return s;
    }

    /**
     * Added By mohammad Haseeb
     * Method to get android Id
     *
     * @param context
     * @return androidID
     */
    @SuppressLint("HardwareIds")
    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String formatNumberInCommas(long number) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(number);
        return yourFormattedString;
    }

    public String encryptPassword(String password) {
        String sha1 = "";
        if (password == null)
            return sha1;
        else {

            try {
                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(password.getBytes("UTF-8"));
                sha1 = byteToHex(crypt.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sha1.toUpperCase();
    }

    public String getThisMonthStartingDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date thisMonthStartDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(thisMonthStartDate);
    }

    public String getThisMonthEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date thisMonthStartDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(thisMonthStartDate);
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public String[] getCashInHand(Context context, int schoolId) {
        String[] array = new String[2];
        //all
        array[0] = FeesCollection.getInstance(context).getCashInHandData(getAllUserSchoolsForFinance(context)).getTotal();
        //single School
//        array[1] = FeesCollection.getInstance(context).getCashInHandData(schoolId).getTotal();
        array[1] = String.valueOf(FeesCollection.getInstance(context).getCashInHand(schoolId, DatabaseHelper.getInstance(context).getAcademicSessionId(schoolId)));
        return array;
    }

    public void setCashinHand(Context context, int schoolId, String oldSRecieved, TextView totalTextView, TextView schoolTextView, String s) {
//        String[] cashInHands = getCashInHand(context, schoolId);
//        String cashInHandSchool = cashInHands[1];
//        String cashInHand = cashInHands[0];
        String cashInHandSchool = String.valueOf(FeesCollection.getInstance(context).getCashInHand(schoolId, DatabaseHelper.getInstance(context).getAcademicSessionId(schoolId)));
        String cashInHand = String.valueOf(FeesCollection.getInstance(context).getCashInHand(0, DatabaseHelper.getInstance(context).getAcademicSessionId(schoolId)));

        int total = 0;
        int oldRecieved;
        String sTotal = "0";
        if (s != null && !s.isEmpty())
            sTotal = s;


        if (oldSRecieved != null && !oldSRecieved.isEmpty())
            oldRecieved = Integer.valueOf(oldSRecieved.replaceAll(",", ""));
        else oldRecieved = 0;

        if (cashInHand != null && !cashInHand.equals("")) {
            int actualCash = (Integer.valueOf(cashInHand));
            total = Integer.parseInt(sTotal.replaceAll(",", ""));
            actualCash += total;
            actualCash -= oldRecieved;

            totalTextView.setText(actualCash + "");
            if (cashInHandSchool != null && !cashInHandSchool.equals("")) {
                actualCash = (Integer.valueOf(cashInHandSchool));
                actualCash += total;
                actualCash -= oldRecieved;
                schoolTextView.setText(actualCash + "");
            }
        } else {
            totalTextView.setText("0");
            schoolTextView.setText("0");
        }

        //hide cash_in_hand for school if account has only on school allowed for finance
        String[] array = getAllUserSchoolsForFinance(context).split(",");
        if (array.length > 1) {
            schoolTextView.setVisibility(View.VISIBLE);
        } else
            schoolTextView.setVisibility(View.GONE);
    }

    public ArrayList<SchoolModel> getSchoolsForLoggedInUser(Context context) {
        ArrayList<SchoolModel> schoolModelList = new ArrayList<>();
        int roleID = DatabaseHelper.getInstance(context).getCurrentLoggedInUser() != null ?
                DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId() : 0;

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            schoolModelList = DatabaseHelper.getInstance(context).getAllUserSchoolsById(getSearchedSchoolId(context));
        } else if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM || roleID == AppConstants.roleId_102_AA) {
            schoolModelList = DatabaseHelper.getInstance(context).getAllUserSchools();
        }
        return schoolModelList;
    }

    public String getAllUserSchoolsForFinance(Context context) {
        String ids = "";
        try {
            ArrayList<SchoolModel> list = getSchoolsForLoggedInUser(context);
            for (SchoolModel model : list) {
                if (model.getAllowedModule_App() != null) {
                    List<String> allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                    if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                        ids += model.getId() + ",";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids.replaceAll(",$", "");
    }

    public ArrayList<SchoolModel> getSchoolsForLoggedInUserForFinance(Context context) {
        ArrayList<SchoolModel> schoolModelList = new ArrayList<>();
        try {
            int roleID = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();

            if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                schoolModelList = DatabaseHelper.getInstance(context).getAllUserSchoolsById(getSearchedSchoolId(context));
            } else if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM || roleID == AppConstants.roleId_102_AA) {
                schoolModelList = DatabaseHelper.getInstance(context).getAllUserSchoolsForFinance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schoolModelList;
    }

    public ArrayList<SchoolModel> getSchoolsForLoggedInUserForEmpAttendAndLeave(Context context) {
        ArrayList<SchoolModel> schoolModelList = new ArrayList<>();
        try {
            int roleID = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();

            if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
                schoolModelList = DatabaseHelper.getInstance(context).getAllUserSchoolsById(getSearchedSchoolId(context));
            } else if (roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST || roleID == AppConstants.roleId_109_CM || roleID == AppConstants.roleId_102_AA) {
                schoolModelList = DatabaseHelper.getInstance(context).getAllUserSchoolsForEmployeeLeavesAndAttend();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schoolModelList;
    }

    public void logOff(Context context) {
        //TODO any change here should also copy paste on DrawerActivity setLogoutMenus method
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        AppModel.getInstance().disposeToken(context);
        AppModel.getInstance().clearAllowedModuleToken(context);
        AppModel.getInstance().removeSyncAccount(context);
        SharedPreferences prefs = context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE);
//                        String syncDate= prefs.getString("syncSuccessTime", "Nill");
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        AppModel.getInstance().clearSpinnerSelectedSchool(context);
        SessionInfo.getInstance(context).delete();
        AttendancePercentage.getInstance(context).delete();
        AppModel.getInstance().clearSelectedSchool(context);
        AppModel.getInstance().writeBooleanToSharedPreferences(context, AppConstants.logoutKey, true);
//        finish();
    }

    public void saveState(Context mContext, String KEY, String VALUE) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(KEY, VALUE);
        editor.apply();
    }

    public List<String> getDatesBetweenTwoDates(String startDate, String endDate, String fromFormat, String toFormat) {
        ArrayList<Date> dates = new ArrayList<Date>();
        SimpleDateFormat df1 = new SimpleDateFormat(fromFormat);

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(startDate);
            date2 = df1.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Calendar start = Calendar.getInstance();
            start.setTime(date1);

            Calendar end = Calendar.getInstance();
            end.setTime(date2);

            while (!start.after(end)) {
                dates.add(start.getTime());
                start.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<String> formatedDateList = new ArrayList<>();
        try {
            SimpleDateFormat df2 = new SimpleDateFormat(toFormat);
            for (Date date : dates) {
                formatedDateList.add(df2.format(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formatedDateList;
    }

    public String getApplicationVersion(Context context) {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
        return version;
    }

    public void startSyncService(Context context, int syncType) {
        try {
            if (!AppModel.getInstance().isConnectedToInternet(context)) {
                Toast.makeText(context, "Internet seems to be offline. Please connect to internet", Toast.LENGTH_SHORT).show();
                AppModel.getInstance().showErrorNotification(context, "No Internet Connection", 2);
            } else if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                if (ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                    ContentResolver.cancelSync(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY);
                }
                boolean NewLogin = AppModel.getInstance().readBooleanFromSharedPreferences(context,
                        AppConstants.NewLogin, true);

                Bundle b = new Bundle();
                b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

                if (NewLogin)
                    b.putBoolean("forceSync", false);
                else
                    b.putBoolean("forceSync", true);

                SyncUtils.TriggerRefresh(context, b, syncType);
                Toast.makeText(context, "Syncing data please wait...", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Sync is already running please wait for it to complete", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDateWhenLogin(Context context) {
        try {
            sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
            String dateoflogin = sharedPref.getString(AppConstants.LOGIN_TIME, "");

            if (dateoflogin == null || dateoflogin.equals(""))
                return "";
            else
                return dateoflogin;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setDateWhenLogin(Context context, String date) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AppConstants.LOGIN_TIME, date);
        editor.commit();

    }

    public boolean isDateIsFuture(String serverDate, String currentDate, String format) {
        SimpleDateFormat df1 = new SimpleDateFormat(format);

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(serverDate);
            date2 = df1.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Calendar serverD = Calendar.getInstance();
            serverD.setTime(date1);

            Calendar current = Calendar.getInstance();
            current.setTime(date2);

            if (current.after(serverD))
                return true;
            else if (current.equals(serverD))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveAllowedModuleToPreference(Context context, ArrayList<SchoolModel> schoolModels, UserModel um) {
        boolean OpenFinance = false, OpenPromotion = false, OpenGraduation = false, OpenEmployee = false, OpenEmployeeListing = false, OpenEmployeeLeaveAndAttend = false, OpenEmployeeResignation = false,
                OpenEmployeeTermination = false, OpenStudentTransfer = false, OpenStudentVisitForm = false, OpenTCTEntry = false, OpenExpense = false,
                OpenSetFees = false;
        //TODO After adding module here change in SchoolExpandableAdapter onHandleClick function
        try {
            //TODO Also change in elseif condition too:
            if (um.getRoleId() == AppConstants.roleId_27_P || um.getRoleId() == AppConstants.roleId_101_ST || um.getRoleId() == AppConstants.roleId_102_AA || um.getRoleId() == AppConstants.roleId_109_CM) {

                for (SchoolModel sm : schoolModels) {
                    if (sm.getAllowedModule_App() != null) {
                        List<String> allowedModules = Arrays.asList(sm.getAllowedModule_App().split(","));
                        if (allowedModules.contains(AppConstants.FinanceModuleValue) && !OpenFinance) {
                            OpenFinance = true;
                        }
                        if (allowedModules.contains(AppConstants.StudentPromotionModuleValue) && !OpenPromotion) {
                            OpenPromotion = true;
                        }
                        if (allowedModules.contains(AppConstants.StudentGraduationModuleValue) && !OpenGraduation) {
                            OpenGraduation = true;
                        }
                        if (allowedModules.contains(AppConstants.HREmployeeListingModuleValue) ||
                                allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue) ||
                                allowedModules.contains(AppConstants.HRResignationModuleValue) ||
                                allowedModules.contains(AppConstants.HRTerminationModuleValue) ||
                                allowedModules.contains(AppConstants.TCTModuleValue)
                                        && !OpenEmployee) {
                            OpenEmployee = true;
                        }
                        if (allowedModules.contains(AppConstants.HREmployeeListingModuleValue) && !OpenEmployeeListing) {
                            OpenEmployeeListing = true;
                        }
                        if (allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue) && !OpenEmployeeLeaveAndAttend) {
                            OpenEmployeeLeaveAndAttend = true;
                        }
                        if (allowedModules.contains(AppConstants.HRResignationModuleValue) && !OpenEmployeeResignation) {
                            OpenEmployeeResignation = true;
                        }
                        if (allowedModules.contains(AppConstants.HRTerminationModuleValue) && !OpenEmployeeTermination) {
                            OpenEmployeeTermination = true;
                        }

                        if (allowedModules.contains(AppConstants.StudentTransferModuleValue) && !OpenStudentTransfer) {
                            OpenStudentTransfer = true;
                        }

                        if (allowedModules.contains(AppConstants.StudentVisitFormsModuleValue) && !OpenStudentVisitForm) {
                            OpenStudentVisitForm = true;
                        }

                        //Only for principal
                        if ((um.getRoleId() == AppConstants.roleId_27_P || um.getRoleId() == AppConstants.roleId_109_CM || um.getRoleId() == AppConstants.roleId_101_ST) && allowedModules.contains(AppConstants.TCTModuleValue) && !OpenTCTEntry) {
                            OpenTCTEntry = true;
                        }

                        if (sm.getAllowedModule_App().contains(AppConstants.ExpenseModuleValue) && !OpenExpense) {
                            OpenExpense = true;
                        }

                        if (DatabaseHelper.getInstance(context).isRegionIsInFlagShipSchools() && !OpenSetFees) {
                            OpenSetFees = true;
                        }
                    }
                }
                if (OpenFinance) {
                    writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "1");
                }

                if (OpenPromotion) {
                    writeToSharedPreferences(context, AppConstants.HIDE_PROMOTION, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_PROMOTION, "1");
                }

                if (OpenGraduation) {
                    writeToSharedPreferences(context, AppConstants.HIDE_GRADUATION, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_GRADUATION, "1");
                }

                if (OpenEmployee) {
                    writeToSharedPreferences(context, AppConstants.HIDE_Employee, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_Employee, "1");
                }
                if (OpenEmployeeListing) {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeListing, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeListing, "1");
                }
                if (OpenEmployeeLeaveAndAttend) {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeLeaveAndAttend, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeLeaveAndAttend, "1");
                }
                if (OpenEmployeeResignation) {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeResignation, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeResignation, "1");
                }
                if (OpenEmployeeTermination) {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeTermination, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_EmployeeTermination, "1");
                }
                if (OpenStudentTransfer) {
                    writeToSharedPreferences(context, AppConstants.HIDE_StudentTransfer, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_StudentTransfer, "1");
                }

                if (OpenStudentVisitForm) {
                    writeToSharedPreferences(context, AppConstants.HIDE_StudentVisitForm, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_StudentVisitForm, "1");
                }

                if (OpenTCTEntry) {
                    writeToSharedPreferences(context, AppConstants.HIDE_TCTEntry, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_TCTEntry, "1");
                }

                if (OpenExpense) {
                    writeToSharedPreferences(context, AppConstants.HIDE_Expense, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_Expense, "1");
                }

                //open Set Fees menu
                if (OpenSetFees) {
                    writeToSharedPreferences(context, AppConstants.HIDE_Set_Fess, "0");
                } else {
                    writeToSharedPreferences(context, AppConstants.HIDE_Set_Fess, "1");
                }

            } else if (um.getRoleId() == AppConstants.roleId_103_V || um.getRoleId() == AppConstants.roleId_7_AEM) {
//                for (SchoolModel sm : schoolModels) {
                SchoolModel schoolModel = DatabaseHelper.getInstance(context).getSchoolById(getSearchedSchoolId(context));
                if (schoolModel != null) {
                    if (schoolModel.getAllowedModule_App() != null) {
                        List<String> allowedModules = Arrays.asList(schoolModel.getAllowedModule_App().split(","));

                        if (schoolModel.getId() == getSearchedSchoolId(context)) {
                            if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                                writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "0");
                            } else if (!allowedModules.contains(AppConstants.FinanceModuleValue)) {
                                writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "1");
                            }
//                        break;
                        }

                        if (allowedModules.contains(AppConstants.HREmployeeListingModuleValue) ||
                                allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue) ||
                                allowedModules.contains(AppConstants.HRResignationModuleValue) ||
                                allowedModules.contains(AppConstants.HRTerminationModuleValue)) {
                            writeToSharedPreferences(context, AppConstants.HIDE_Employee, "0");

                        } else {
                            writeToSharedPreferences(context, AppConstants.HIDE_Employee, "1");
                        }

                        if (allowedModules.contains(AppConstants.HREmployeeListingModuleValue))
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeListing, "0");
                        else
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeListing, "1");


                        if (allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue))
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeLeaveAndAttend, "0");
                        else
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeLeaveAndAttend, "1");


                        if (allowedModules.contains(AppConstants.HRResignationModuleValue))
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeResignation, "0");
                        else
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeResignation, "1");


                        if (allowedModules.contains(AppConstants.HRTerminationModuleValue))
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeTermination, "0");
                        else
                            writeToSharedPreferences(context, AppConstants.HIDE_EmployeeTermination, "1");

                        if (allowedModules.contains(AppConstants.ExpenseModuleValue))
                            writeToSharedPreferences(context, AppConstants.HIDE_Expense, "0");
                        else
                            writeToSharedPreferences(context, AppConstants.HIDE_Expense, "1");

                        if (allowedModules.contains(AppConstants.StudentVisitFormsModuleValue)) {
                            writeToSharedPreferences(context, AppConstants.HIDE_StudentVisitForm, "0");
                        } else {
                            writeToSharedPreferences(context, AppConstants.HIDE_StudentVisitForm, "1");
                        }

//                        //open Set Fees menu
//                        if (DatabaseHelper.getInstance(context).isRegionIsInFlagShipSchools()) {
//                            writeToSharedPreferences(context, AppConstants.HIDE_Set_Fess, "0");
//                        } else {
//                            writeToSharedPreferences(context, AppConstants.HIDE_Set_Fess, "1");
//                        }
                    }
                }

//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSchoolInfo(Context context, int SchoolID, TextView tv_regionName, TextView tv_areaName) {
        if (SchoolID != 0) {
            try {

                //setting the school info box
                SchoolExpandableModel mod = DatabaseHelper.getInstance(context).getSchoolInfo(SchoolID);
                tv_regionName.setText(mod.getRegion());
                tv_areaName.setText(mod.getArea());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tv_regionName.setText("");
            tv_areaName.setText("");
        }
    }

    public void showTCTReminderNotification(Context context) {
        // show notification if there is some pending tct of teachers
        boolean isTCTEntryHidden = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.HIDE_TCTEntry).equals("1");
        EnabledModules enabledModules = AppModel.getInstance().getEnabledModules(new WeakReference<>(context));
        if (!isTCTEntryHidden) {
            if (enabledModules.isModuleTCTEntryEnabled()) {
                StringBuilder msg = new StringBuilder(" Please fill the TCT Entry Form of School: ");
                int UnSubmittedSchools = 0;
                List<String> schoolIds = List.of(AppModel.getInstance().getAllUserSchoolsForTCTEntry(context).split(","));
                for (String id : schoolIds) {
                    if (TCTHelperClass.getInstance(context)
                            .anyUnSubmittedTCTEmployeeLeft(Integer.parseInt(id))) {
                        UnSubmittedSchools++;
                        if (UnSubmittedSchools > 1)
                            msg.append(", ");

                        msg.append(id);
                    }
                }
//                boolean isAllTCTSubNotTagged = TCTHelperClass.getInstance(context)
//                        .anyUnSubmittedTCTEmployeeLeft();
//                        .isAllTCTSubNotTagged(AppModel.getInstance().getAllUserSchoolsForTCTEntry(context));
//                if (isAllTCTSubNotTagged)
                if (UnSubmittedSchools > 0)
                    AppModel.getInstance().showTCTNotification(context, "TCT Entry Form", String.valueOf(msg), AppConstants.TCTNotifyId, new Intent(context, EmployeeTCT_EntryActivity.class), AppConstants.CHANNEL_ID_TCT_Notificaton);
            }
        }

    }

    public void showTCTNotification(Context context, String title, String message, int notificationId, Intent notificationIntent, String channelId) {
        try {
            //if notification is already showing
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                StatusBarNotification[] notifications = new StatusBarNotification[0];
                assert mNotificationManager != null;
                notifications = mNotificationManager.getActiveNotifications();

                for (StatusBarNotification notification : notifications) {
                    if (notification.getId() == notificationId) {
                        return;
                    }
                }
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //New TCT Change
//            Intent notificationIntent = new Intent(context, EmployeeTCT_EntryActivity.class);
//            Intent notificationIntent = new Intent(context, NewDashboardActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        PendingIntent.FLAG_UPDATE_CURRENT
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);

            createNotificationChannel(context, channelId, "tctnotifi", "tctnotifi", false);

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAllUserSchoolsForTCTEntry(Context context) {
        String ids = "";
        try {
            ArrayList<SchoolModel> list = getSchoolsForLoggedInUser(context);
            for (SchoolModel model : list) {
                List<String> allowedModules = null;
                if (model.getAllowedModule_App() != null) {
                    allowedModules = Arrays.asList(model.getAllowedModule_App().split(","));
                }

                if (allowedModules != null && allowedModules.contains(AppConstants.TCTModuleValue)) {
                    ids += model.getId() + ",";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids.replaceAll(",$", "");
    }

    public String getAllUserSchoolsCommaSeparated(Context context) {
        String ids = "";
        try {
            ArrayList<SchoolModel> list = DatabaseHelper.getInstance(context).getAllUserSchools();
            for (SchoolModel model : list) {
                ids += model.getId() + ",";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids.replaceAll(",$", "");
    }

    public String getAllUserSchoolsCommaSeparatedModuleVise(Context context, String module) {
        String ids = "";
        try {
            ArrayList<SchoolModel> list = DatabaseHelper.getInstance(context).getAllUserSchools();
            for (SchoolModel model : list) {
                if (model.getAllowedModule_App() != null) {
                    if (Arrays.asList(model.getAllowedModule_App().split(",")).contains(module)) {
                        ids += model.getId() + ",";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids.replaceAll(",$", "");
    }

    public void changeMenuPendingSyncCount(Context context, boolean startSync) {
        Intent intent = new Intent(AppConstants.Action_PendingSyncChanged);
        intent.putExtra("schoolid", AppModel.getInstance().getSpinnerSelectedSchool(context));
        intent.putExtra("startSync", startSync);
        context.sendBroadcast(intent);
    }

    public boolean isCurrentTimeBetween(String from, String to) {
        String currDate = AppModel.getInstance().getCurrentDateTime("HH:mm:ss");
        boolean isBetween = false;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        try {
            Date date_from = formatter.parse(from);
            Date date_to = formatter.parse(to);
            Date dateNow = formatter.parse(currDate);

            assert date_from != null;
            isBetween = date_from.before(dateNow) && date_to.after(dateNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return isBetween;
    }

    public String getAppVersionWithBuildNo(Context context) {
        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            versionName = version + context.getString(R.string.BUILD_NO)/*AppConstants.BUILD_NO*/;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public ErrorModel checkSchoolClassDataDownloaded(Context context, int schoolId) {
        ErrorModel model = new ErrorModel();
        ArrayList<ClassSectionModel> classSectionModels = DatabaseHelper.getInstance(context).getClassSectionBySchoolId(schoolId + "");

        if (schoolId > 0) {
            if (AppModel.getInstance().isConnectedToInternet(context) &&
                    ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY) &&
                    (classSectionModels == null || classSectionModels.size() == 0)) {
                model.setMessage("Please wait for the sync to complete");
                model.setErrorCode(1);
            } else if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY) &&
                    (classSectionModels == null || classSectionModels.size() == 0)) {
                model.setMessage("No records found for Class Section please sync again or login again");
                model.setErrorCode(2);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return model;
    }

    public void setFinanceSyncingCompleted(Context context, int schoolId) {
        //Check If first login for finance sync to complete
//        if (Arrays.asList(sm.getAllowedModule_App().split(",")).contains(AppConstants.FinanceModuleValue)) {
        boolean isFeeHeaderRecordsExists = FeesCollection.getInstance(context).getFeesHeaderCount(schoolId);
        if (!isFeeHeaderRecordsExists) {
            AppModel.getInstance().writeBooleanToSharedPreferences(context, AppConstants.FinanceSyncCompleted, false);
//                break;
        } else {
            AppModel.getInstance().writeBooleanToSharedPreferences(context, AppConstants.FinanceSyncCompleted, true);
        }
//        }
    }

    public int getBatteryPercentage(Context context) {
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else
            return 0;


    }

    public long getDurationBetween(String startDate, String endDate) {
        startDate = convertDatetoFormat(startDate, "dd/MM/yyyy hh:mm:ss aa", "hh:mm:ss aa");
        endDate = convertDatetoFormat(endDate, "dd/MM/yyyy hh:mm:ss aa", "hh:mm:ss aa");
        long Secs = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        try {
            if (!Strings.isEmptyOrWhitespace(startDate) && !Strings.isEmptyOrWhitespace(endDate)) {
                Date Date1 = sdf.parse(startDate);
                Date Date2 = sdf.parse(endDate);

                long millse = Date1.getTime() - Date2.getTime();
                long mills = Math.abs(millse);

//        int Hours = (int) (mills/(1000 * 60 * 60));
//        int Mins = (int) (mills/(1000*60)) % 60;
                Secs = (int) (mills / 1000) % 60;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Secs;
    }

    public double convertBytesIntoKB(long lengthInBytes, boolean shouldConvert) {
        double value = 0;
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        if (shouldConvert) {
            if (lengthInBytes > 0) {
                value = (double) lengthInBytes / 1024.0;
            }
        } else {
            value = (double) lengthInBytes;
        }
        return value;
    }

    public String getLastModifiedOn(Context context, String TABLE_NAME, String columnName, String whereClause, int module) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(modified_on) as modify_on from " + TABLE_NAME;

        if (!Strings.isEmptyOrWhitespace(whereClause))
            query += " WHERE " + whereClause;

        if (!columnName.equals("modified_on")) {
            query = query.replace("modified_on", columnName);
        }
        String modified_on = "",
                serverDateFormat = "dd-MM-yyyy",
                dateFormat1 = "yyyy-MM-dd'T'hh:mm:ss",
                dateFormat2 = "yyyy-MM-dd hh:mm:ss",
                dateFormat3 = "yyyy-MM-dd";

        String hardCodedModifiedOn = "";

        if (module == 0) {
            hardCodedModifiedOn = "01-03-2001";
        } else {
            hardCodedModifiedOn = "01-03-2017";
        }

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                modified_on = cursor.getString(cursor.getColumnIndex("modify_on"));
                Log.i("lastModifi", "Query: " + query + " modified_on:" + modified_on);
                if (!Strings.isEmptyOrWhitespace(modified_on)) {
                    if (isDateValid(modified_on, dateFormat1)) {
                        modified_on = convertDatetoFormat(modified_on, dateFormat1, serverDateFormat);
                    } else if (isDateValid(modified_on, dateFormat2)) {
                        modified_on = convertDatetoFormat(modified_on, dateFormat2, serverDateFormat);
                    } else if (isDateValid(modified_on, dateFormat3)) {
                        modified_on = convertDatetoFormat(modified_on, dateFormat3, serverDateFormat);
                    }
                    return modified_on;
                } else
                    return hardCodedModifiedOn;
            } else
                return hardCodedModifiedOn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hardCodedModifiedOn;
    }

    public boolean isDateValid(String dateString, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            sdf.parse(dateString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, File imageFile) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            OutputStream imageOutStream = null;

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Title");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri uri = inContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try {
                imageOutStream = inContext.getContentResolver().openOutputStream(uri);
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream);

                if (imageOutStream != null)
                    imageOutStream.close();

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(inContext, "Error in getImageUri : " + e.getMessage());
            }

            return uri;
        } else {
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

    }

    public Date convertStringToDate(String date, String fromFormat) {
        Date dtForDate = null;
        try {
            if (date != null && !date.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat(fromFormat);
                dtForDate = df.parse(date);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dtForDate;
    }

    public String convertDateToString(Date date, String toFormat) {
        String newDate = "";
        try {
            if (date != null) {
                newDate = DateFormat.format(toFormat, date).toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return newDate;
    }

    private final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
        {
            put("^\\d{8}$", "yyyyMMdd");
            put("^\\d{12}$", "yyyyMMddHHmm");
            put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
            put("^\\d{14}$", "yyyyMMddHHmmss");
            put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
            put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
            put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
            put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
            put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
            put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
            put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
            put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
            put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
            put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
            put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
            put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
            put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{2}[-+]\\d{2}:\\d{2}$", "yyyy-MM-dd'T'HH:mm:ss.SSS");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{1,3}$", "yyyy-MM-dd'T'HH:mm:ss.SSS");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd'T'hh:mm:ss");
        }
    };

    public String determineDateFormat(String dateString) { //Always check in regular expression list that specific date format present or not
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.matches(regexp) || dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null;
    }

    public String getAndroidVersionName() {
        String androidVersion = "";
        StringBuilder builder = new StringBuilder();
        builder.append("Android: ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;
            try {
                fieldValue = field.getInt(new Object());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" ").append(fieldName);
                builder.append(" sdk: ").append(fieldValue);
            }
        }

        androidVersion = builder.toString();
        Log.i("version", androidVersion);
        return androidVersion;
    }

    public void clearAllowedModuleToken(Context context) {
        sharedPref = context.getSharedPreferences(AppConstants.TCF_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("allowed_modules", "");
        editor.apply();
    }

    public String getLastModifiedOnInDateTimeFormat(Context context, String TABLE_NAME, String columnName, String whereClause, int module) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getDB();
        String query = "select max(modified_on) as modify_on from " + TABLE_NAME;

        if (!Strings.isEmptyOrWhitespace(whereClause))
            query += " WHERE " + whereClause;

        if (!columnName.equals("modified_on")) {
            query = query.replace("modified_on", columnName);
        }
        String modified_on = "",
                serverDateFormat = "dd-MM-yyyy'T'hh:mm:ss";

        String hardCodedModifiedOn = "";

        if (module == 0) {
            hardCodedModifiedOn = "01-03-2001T00:00:00";
        } else {
            hardCodedModifiedOn = "01-03-2017T00:00:00";
        }

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                modified_on = cursor.getString(cursor.getColumnIndex("modify_on"));
                Log.i("lastModifi", "Query: " + query + " modified_on:" + modified_on);
                if (!Strings.isEmptyOrWhitespace(modified_on)) {
                    String format = determineDateFormat(modified_on);
                    if (!Strings.isEmptyOrWhitespace(format)) {
                        modified_on = convertDatetoFormat(modified_on, format, serverDateFormat);
                        return modified_on;
                    } else
                        return hardCodedModifiedOn;
                } else
                    return hardCodedModifiedOn;
            } else
                return hardCodedModifiedOn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hardCodedModifiedOn;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public boolean isTodayHoliday(Context context) {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String selectDate = sdf.format(d);

        List<String> holidaysList = getUniqueHolidays(context);

        for (String holidays : holidaysList) {
            if (holidays.equalsIgnoreCase(selectDate)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getUniqueHolidays(Context context) {
        List<CalendarsModel> OffdaysList = DatabaseHelper.getInstance(context).getAllDatesForHolidays(String.valueOf(getSpinnerSelectedSchool(context)));
        List<String> holidays = new ArrayList<>();
        if (!OffdaysList.isEmpty()) {
            for (CalendarsModel cm : OffdaysList) {
                holidays.add(AppModel.getInstance()
                        .convertDatetoFormat(cm.getActivity_Start_Date(), "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy"));
            }
        }
        if (holidays.size() > 0) {
            Set<String> uHolidays = new HashSet<>(holidays);

            holidays.clear();
            holidays.addAll(uHolidays);
        }
        return holidays;
    }

    public String changeStringCase(String s) {

        final String DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

//    public void imageSelectionDialog(Context context, String title, IPickClick cameraPickClick){
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View dialogView = inflater.inflate(R.layout.image_selection_dialog, null);
//        LinearLayout camera = dialogView.findViewById(R.id.ll_camera_button);
//        LinearLayout gallery = dialogView.findViewById(R.id.ll_gallery_button);
//
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .setView(dialogView)
//                .setTitle(title)
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .create();
//
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cameraPickClick.onCameraClick();
//            }
//        });
//
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cameraPickClick.onGalleryClick();
//            }
//        });
//
////        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
////
////            @Override
////            public void onShow(DialogInterface dialogInterface) {
////
////                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
////
////                try {
////                    if (employeeModelList.get(position).getMobile_No() != null) {
////                        if (!employeeModelList.get(position).getMobile_No().isEmpty()) {
////                            mobileNumber.setText(employeeModelList.get(position).getMobile_No());
////                        }
////                    }
////
////                    if (employeeModelList.get(position).getEmail() != null) {
////                        if (!employeeModelList.get(position).getEmail().isEmpty()) {
////                            email.setText(employeeModelList.get(position).getEmail());
////                        }
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////
////                button.setOnClickListener(new View.OnClickListener() {
////
////
////                    @Override
////                    public void onClick(View view) {
////
////                        em.setId(employeeModelList.get(position).getId());
////
////                        if (validate()) {
////                            long id = EmployeeHelperClass.getInstance(context).updateEmployeeDetail(em);
////                            if (id > 0) {
////                                //Important when any change in table call this method
////                                AppModel.getInstance().changeMenuPendingSyncCount(context);
////
////                                        /*Intent leave_intent = new Intent(context, EmployeeDetailsActivity.class);
////                                        leave_intent.putExtra("empDetailId", employeeModelList.get(position).getId());
////                                        MessageBox("Employee updated successfully", true, leave_intent);*/
////                                if (employeeModelList.get(position).getMobile_No() != null) {
////                                    if (!employeeModelList.get(position).getMobile_No().isEmpty()) {
////                                        mobileNumber.setText(employeeModelList.get(position).getMobile_No());
////                                    }
////                                }
////
////                                if (employeeModelList.get(position).getEmail() != null) {
////                                    if (!employeeModelList.get(position).getEmail().isEmpty()) {
////                                        email.setText(employeeModelList.get(position).getEmail());
////
////                                    }
////                                }
////
////                                MessageBox("Employee updated successfully");
////
////
////                            } else {
////                                MessageBox("Something went wrong");
////                            }
////
////                            dialog.dismiss();
////
////                        }
////                    }
////                });
////            }
////        });
//        dialog.show();
//    }
}
