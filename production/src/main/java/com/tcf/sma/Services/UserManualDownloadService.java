package com.tcf.sma.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tcf.sma.Activities.SearchSchoolActivity;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManualDownloadService extends AsyncTask<String, Void, String> {

    private Context context;
    private LinearLayout ll_uploadProgress;

    public UserManualDownloadService(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, "Downloading please wait ....", Toast.LENGTH_SHORT).show();
        AppModel.getInstance().showLoader(context, "Downloading Manual", "Please wait...");
    }

    @Override
    protected String doInBackground(String... strings) {
        String dirPath = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Documents/";
        String fileUrl = strings[0];
        String fileName = strings[1];
        File folder = new File(dirPath,fileName);
        if (!folder.exists()) {
            String f = folder.getParent();
            File fo = new File(f);
            if (!fo.exists())
                fo.mkdir();

            File pdfFile = new File(fo, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            UserManualFileDownloader userManualFileDownloader = new UserManualFileDownloader(context);
            userManualFileDownloader.downloadFileUsingRetrofit(fileUrl, pdfFile);
//          UserManualFileDownloader.downloadFile(fileUrl, pdfFile);

        }
        return fileName;
    }

    @Override
    protected void onPostExecute(String fileName) {
        super.onPostExecute(fileName);

        AppModel.getInstance().hideLoader();
        HelpHelperClass.getInstance(context).openManual(fileName);
    }


}


 class UserManualFileDownloader {
     private static final int MEGABYTE = 1024 * 1024;
     private Context context;

     public UserManualFileDownloader(Context context) {
         this.context = context;
     }

     public void downloadFile(String fileUrl, File directory) throws FileNotFoundException {
         try {

             URL url = new URL(fileUrl);
             HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
             //urlConnection.setRequestMethod("GET");
             //urlConnection.setDoOutput(true);
             urlConnection.connect();

             InputStream inputStream = urlConnection.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(directory);
             int totalSize = urlConnection.getContentLength();

             byte[] buffer = new byte[MEGABYTE];
             int bufferLength = 0;
             while ((bufferLength = inputStream.read(buffer)) > 0) {
                 fileOutputStream.write(buffer, 0, bufferLength);
             }
             fileOutputStream.close();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             throw e;
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void downloadFileUsingRetrofit(String fileUrl, File directory) {
         try {
             ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
             Call<ResponseBody> call = apiInterface.downloadUserManual(fileUrl);
             try {
                 final Response<ResponseBody> response = call.execute();
                 final ResponseBody body = response.body();
                 if (response.isSuccessful()){
                     try {
                         assert body != null;
                         InputStream inputStream = body.byteStream();
                         FileOutputStream fileOutputStream = new FileOutputStream(directory);
                         long totalSize = body.contentLength();

                         byte[] buffer = new byte[MEGABYTE];
                         int bufferLength = 0;
                         while ((bufferLength = inputStream.read(buffer)) > 0) {
                             fileOutputStream.write(buffer, 0, bufferLength);
                         }
                         fileOutputStream.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}