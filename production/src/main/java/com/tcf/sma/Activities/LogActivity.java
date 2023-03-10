package com.tcf.sma.Activities;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tcf.sma.Helpers.MarshMallowPermission;
import com.tcf.sma.HttpServices.HttpConnectionClass;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Zubair Soomro on 9/29/2017.
 */

public class LogActivity extends DrawerActivity implements View.OnClickListener {
    TextView tv_log;
    LinearLayout ll_info, ll_error, ll_upload, ll_uploadProgress;
    View v_info, v_error;
    String date;
    ScrollView scroll;
    File data;
    File folderPath;
    File filePath;
    //    private ImageView iv_upload;
    MarshMallowPermission permission = new MarshMallowPermission(this);
    private Calendar c;
    private String CurrentDate;
    private View view;
    private int schoolid;
    private String deviceId;
    private ProgressBar pb_upload;
//    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.log_view);
        setToolbar("Log Report", this, false);

        tv_log = (TextView) view.findViewById(R.id.tv_log);
        ll_info = (LinearLayout) view.findViewById(R.id.ll_info);
        ll_error = (LinearLayout) view.findViewById(R.id.ll_error);
        v_info = (View) view.findViewById(R.id.view_info);
        v_error = (View) view.findViewById(R.id.view_error);
        scroll = (ScrollView) view.findViewById(R.id.scrl_down);
        ll_upload = view.findViewById(R.id.ll_upload);
        ll_uploadProgress = view.findViewById(R.id.ll_uploadProgress);
        pb_upload = view.findViewById(R.id.pb_upload);
//        iv_upload = view.findViewById(R.id.iv_upload);
//        TooltipCompat.setTooltipText(iv_upload, "Upload");

        data = StorageUtil.getSdCardPath(this);
        folderPath = new File(data + "/TCF/Logs");

        c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        CurrentDate = String.valueOf(mDay) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mYear);

        String fileName = "Info_logs_" + CurrentDate + ".txt";
        filePath = new File(folderPath.getPath() + "/" + fileName);

        String result = AppModel.getInstance().readFile(this, filePath.getPath());
        if (result != null) {
            tv_log.append(result);
        }
        scroll.post(new Runnable() {

            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        ll_upload.setOnClickListener(this);
//        iv_upload.setOnClickListener(this);
        ll_info.setOnClickListener(this);
        ll_error.setOnClickListener(this);

        schoolid = AppModel.getInstance().getSpinnerSelectedSchool(LogActivity.this);
        deviceId = AppModel.getInstance().getDeviceId(LogActivity.this);

//        alertDialog = new SpotsDialog.Builder()
//                .setContext(this)
//                .setMessage(R.string.upload)
//                .setCancelable(false)
//                .build();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_info:
                try {
                    String fileName = "Info_logs_" + CurrentDate + ".txt";
                    filePath = new File(folderPath.getPath() + "/" + fileName);
                    String result = AppModel.getInstance().readFile(this, filePath.getPath());
                    v_info.setBackgroundColor(getResources().getColor(R.color.app_green));
                    v_error.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    if (result != null) {
                        tv_log.setText(result);
                    }
                    scroll.post(new Runnable() {

                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.ll_error:
                try {
                    String fileName = "Error_logs_" + CurrentDate + ".txt";
                    filePath = new File(folderPath.getPath() + "/" + fileName);
                    String result1 = AppModel.getInstance().readFile(this, filePath.getPath());
                    v_info.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    v_error.setBackgroundColor(getResources().getColor(R.color.app_green));
                    if (result1 != null) {
                        tv_log.setText(result1);
                    }
                    scroll.post(new Runnable() {

                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_upload:
                if (!permission.checkPermissionForExternalStorage()) {
                    permission.requestPermissionForExternalStorage();
                } else {
                    saveFilesAsZip();
                }
                break;
        }


    }

    private void showProgressBar() {
        ll_uploadProgress.setVisibility(View.VISIBLE);
        pb_upload.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void hideProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_uploadProgress.setVisibility(View.GONE);
                pb_upload.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideProgressBar();
    }

    private void saveFilesAsZip() {

//            SurveyAppModel.getInstance().showLoader(this,"Uploading database/logs", "Please wait...");
//            alertDialog.show();

        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread dbThread = new Thread(() -> saveDBFleInZip());
                    dbThread.start();
                    dbThread.join();

                    Thread logThread = new Thread(() -> saveLogFleInZip());
                    logThread.start();
                    logThread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveFilesAsZip();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("We need external storage permission for further processing. Thankyou!")
                    .setPositiveButton("Request permission", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        permission.requestPermissionForExternalStorage();
                    }).setNegativeButton("Quit", (dialogInterface, i) -> finish()).create().show();
        }
    }

    private void saveDBFleInZip() {
        try {
//            runOnUiThread(() -> SurveyAppModel.getInstance().showLoader(LogActivity.this, "Uploading database data", "Please wait..."));

            String sourcePath = StorageUtil.getSdCardPath(this) + File.separator + "TCF" + File.separator + "Database";
            String backupDBPath = StorageUtil.getSdCardPath(this) + File.separator + "TCF" + File.separator + "upload_db_file.zip";
            zipFolder(sourcePath, backupDBPath);
            UploadDB(backupDBPath);
        } catch (Exception e) {
            e.printStackTrace();
//            SurveyAppModel.getInstance().hideLoader();
        }
    }

    private void saveLogFleInZip() {
        try {
//            runOnUiThread(() -> {
////                SurveyAppModel.getInstance().hideLoader();
////                SurveyAppModel.getInstance().showLoader(LogActivity.this, "Uploading log data", "Please wait...");
//            });

            String sourcePath = StorageUtil.getSdCardPath(this) + File.separator + "TCF" + File.separator + "Logs";
            String backupDBPath = StorageUtil.getSdCardPath(this) + File.separator + "TCF" + File.separator + "upload_log_files.zip";

            File file = new File(backupDBPath);
            if (file.exists()) {
                if (file.delete()) {
                    zipLogFolder(sourcePath, backupDBPath);
                    UploadLogs(backupDBPath);
                }
            } else {
                zipLogFolder(sourcePath, backupDBPath);
                UploadLogs(backupDBPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
//            SurveyAppModel.getInstance().hideLoader();
        }
    }

    private void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            Log.i("LogActivity", "Successfully created zip file for upload");
//            Toast.makeText(LogActivity.this, "Successfully Saved ", Toast.LENGTH_SHORT).show();
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    private void zipLogFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();

            if (files != null) {
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
            }

            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();

                //zip only last 20 files
                if (i == 19) {
                    break;
                }
            }
            Log.i("LogActivity", "Successfully created zip file for upload");
//            Toast.makeText(LogActivity.this, "Successfully Saved ", Toast.LENGTH_SHORT).show();
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    private void UploadLogs(String backupDBPath) {
//            File file = new File(backupDBPath);
        HttpConnectionClass connectionClass = new HttpConnectionClass(LogActivity.this);
//            SurveyAppModel.getInstance().appendLog(LogActivity.this, "In uploadLogs Method.Uploading Log:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//        AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(LogActivity.this, AppConstants.baseurlkey);
        int responseCode = connectionClass.uploadFile(backupDBPath,
                getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_AuditLogFiles + "?deviceId=" +
                        deviceId + "&fileType=L" + "&schoolId=" + schoolid, "upload_log_files");
//
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            Log.d("Success", "zz");
            AppModel.getInstance().appendLog(LogActivity.this, "In uploadLogs Method. ResponseCode = " + responseCode);

//                SurveyAppModel.getInstance().hideLoader();
//                alertDialog.dismiss();

            hideProgressBar();
            runOnUiThread(() -> {
                Toast.makeText(LogActivity.this, "Logs Successfully uploaded", Toast.LENGTH_SHORT).show();
            });

        } else {
            Log.d("Logs Upload", "true" + responseCode);
            AppModel.getInstance().appendErrorLog(LogActivity.this, "Logs Upload fail. ResponseCode = " + responseCode);

//                SurveyAppModel.getInstance().hideLoader();
//                alertDialog.dismiss();

            hideProgressBar();
            runOnUiThread(() -> {
                Toast.makeText(LogActivity.this, "Logs Upload fail. ResponseCode = " + responseCode, Toast.LENGTH_SHORT).show();
            });

        }
    }

    private boolean checkIfFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private void UploadDB(String backupDBPath) {

//            File file = new File(backupDBPath);
        HttpConnectionClass connectionClass = new HttpConnectionClass(LogActivity.this);
//            SurveyAppModel.getInstance().appendLog(LogActivity.this, "In uploadLogs Method.Uploading Log:" + eim.getFilename() + " with id:" + eim.getId() + " Type: " + eim.getFiletype());
//        AppConstants.BASE_URL = AppModel.getInstance().readFromSharedPreferences(LogActivity.this, AppConstants.baseurlkey);
        int responseCode = connectionClass.uploadFile(backupDBPath,
                getString(R.string.BASE_URL)/*AppConstants.BASE_URL*/ + AppConstants.URL_UPLOAD_AuditLogFiles + "?deviceId=" +
                        deviceId + "&fileType=D" + "&schoolId=" + schoolid, "upload_db_file");
//
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            Log.d("Success", "zz");
            AppModel.getInstance().appendLog(LogActivity.this, "In uploadDB Method. ResponseCode = " + responseCode);
            runOnUiThread(() -> {
//                    SurveyAppModel.getInstance().hideLoader();
                Toast.makeText(LogActivity.this, "DB Successfully uploaded", Toast.LENGTH_SHORT).show();
            });


        } else {
            Log.d("Logs Upload", "true" + responseCode);
            AppModel.getInstance().appendErrorLog(LogActivity.this, "DB Upload fail. ResponseCode = " + responseCode);
            runOnUiThread(() -> {
//                    SurveyAppModel.getInstance().hideLoader();
                Toast.makeText(LogActivity.this, "DB Upload fail. ResponseCode = " + responseCode, Toast.LENGTH_SHORT).show();
            });

        }

    }

//    private void UploadDB(String backupDBPath){
//       new Thread(() -> {
//           ApiInterface apiInterface = ApiClient.getClient(LogActivity.this).create(ApiInterface.class);
//
//           try {
//
//           File file = new File(backupDBPath);
//               // use the FileUtils to get the actual file by uri
////           File file = FileUtils.getFile(LogActivity.this, Uri.fromFile(f));
//
//               // create RequestBody instance from file
//               RequestBody requestFile =
//                       RequestBody.create(
//                               MediaType.parse(getContentResolver().getType(Uri.fromFile(file))),file);
//
//               // MultipartBody.Part is used to send also the actual file name
//               MultipartBody.Part body =
//                       MultipartBody.Part.createFormData("D", file.getName(), requestFile);
//
//               // add another part within the multipart request
//               String deviceIdString = SurveyAppModel.getInstance().getDeviceId(LogActivity.this);
//               RequestBody deviceId =
//                       RequestBody.create(
//                               MultipartBody.FORM, deviceIdString);
//
//               // finally, execute the request
//               Call<ResponseBody> call = apiInterface.uploadAuditLogFiles(deviceId, body, SurveyAppModel.getInstance().getToken(LogActivity.this));
//               call.enqueue(new Callback<ResponseBody>() {
//                   @Override
//                   public void onResponse(Call<ResponseBody> call,
//                                          Response<ResponseBody> response) {
//                       Log.v("Upload", "success");
//                   }
//
//                   @Override
//                   public void onFailure(Call<ResponseBody> call, Throwable t) {
//                       Log.e("Upload error:", t.getMessage());
//                   }
//               });
//           }catch (Exception e){
//               e.printStackTrace();
//           }
////           model.setPicture_slip_filename(file.getName());
//       }).start();
//    }

//    private static int BUFFER_SIZE = 6 * 1024;

//    private void saveDBFleInZip() {
//        try {
//            String backupDBPath = StorageUtil.getSdCardPath(this) + File.separator + "TCF" + File.separator + "Database" + File.separator;
//            final File backupDBFolder = new File(backupDBPath);
//            backupDBFolder.mkdirs();
//            final File backupDB = new File(backupDBFolder, DatabaseHelper.DATABASE_NAME + ".db");
//            String[] s = new String[1];
//            s[0] = backupDB.getAbsolutePath();
//            zip(s, backupDBPath + "/DB_"+CurrentDate+".zip");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void zip(String[] files, String zipFile) throws IOException {
//        BufferedInputStream origin = null;
//        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
//        try {
//            byte data[] = new byte[BUFFER_SIZE];
//
//            for (int i = 0; i < files.length; i++) {
//                FileInputStream fi = new FileInputStream(files[i]);
//                origin = new BufferedInputStream(fi, BUFFER_SIZE);
//                try {
//                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
//                    out.putNextEntry(entry);
//                    int count;
//                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
//                        out.write(data, 0, count);
//                    }
//                } finally {
//                    origin.close();
//                }
//            }
//        } finally {
//            out.close();
//        }
//    }
}
