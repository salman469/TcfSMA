package com.tcf.sma.Survey.httpServices;

import android.content.Context;
import android.os.AsyncTask;

import com.tcf.sma.Survey.model.SurveyAppModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.HashMap;


public class MediaUploadService extends AsyncTask<Object, Void, Boolean> implements IHttpPayloadService {
    int responseCode;
    String fileToUpload;
    String uploadUrl;
    HttpEventsHandler handler;
    HashMap<String, String> postValuesUsed;
    byte[] byteArrayToUpload = null;
    String byteArrayFilename = "";
    Object tagData;
    Context context;


    public MediaUploadService(String uploadFilename, HashMap<String, String> postValues, String Url, HttpEventsHandler handler, Object tag, Context context) {
        this.fileToUpload = uploadFilename;
        this.handler = handler;
        this.postValuesUsed = postValues;
        this.uploadUrl = Url;
        this.tagData = tag;
        this.context = context;
    }

    public MediaUploadService(byte[] uploadByteArray, String filename, HashMap<String, String> postValues, String Url, HttpEventsHandler handler, Object tag) {
        this.byteArrayToUpload = uploadByteArray;
        byteArrayFilename = filename;
        this.handler = handler;
        this.postValuesUsed = postValues;
        this.uploadUrl = Url;
        this.tagData = tag;
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    @Override
    protected Boolean doInBackground(Object... argList) {

        boolean returnflag = false;

        APIServiceManager task = new APIServiceManager();
        DefaultHttpClient httpclient = task.getClient();

        HttpResponse response;
        String url = task.prepareURL(uploadUrl);

        HttpPost httppost = new HttpPost(url);

        try {

            MultipartEntity entity = new MultipartEntity();

            ByteArrayBody bab;
            if (byteArrayToUpload != null) {
                bab = new ByteArrayBody(byteArrayToUpload, byteArrayFilename);
            } else {
                File file = new File(this.fileToUpload);
                if (!file.exists()) return false;

                byte[] byte_arr = readFile(file);
                String fileName = file.getName();
                bab = new ByteArrayBody(byte_arr, fileName);
            }

            entity.addPart("myFile", bab);
            Charset cs = Charset.forName("UTF-8");

            if (postValuesUsed != null && postValuesUsed.size() > 0) {
                for (String key : postValuesUsed.keySet()) {
                    entity.addPart(key.toString(), new StringBody(postValuesUsed.get(key), cs));
                }
            }

            httppost.setEntity(entity);
            response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: " + s);

            this.responseCode = response.getStatusLine().getStatusCode();
            if (this.responseCode >= 400) {
                SyncSurveys.retries++;
                SurveyAppModel.getInstance().errorLog.addLog("Error While uploading image " + fileToUpload, "" + this.responseCode + " Detailed error : " + s,context);
            }
            returnflag = true;
        } catch (ClientProtocolException e) {
            returnflag = false;
            SurveyAppModel.getInstance().errorLog.addLog("Uploading " + fileToUpload, e.getLocalizedMessage(),context);
        } catch (IOException e) {
            returnflag = false;
            SurveyAppModel.getInstance().errorLog.addLog("Uploading " + fileToUpload, e.getLocalizedMessage(),context);
        }

        return returnflag;
    }

    protected void onPostExecute(Boolean result) {

        SurveyAppModel.getInstance().isSurveySyncStarted = false;
        SurveyAppModel.getInstance().syncedMediaLimit--;

        if (responseCode == 401) {

            //handler.onMediaUploadFailed(responseCode);

            SurveyAppModel.getInstance().errorLog.addLog("Uploading " + fileToUpload, this.responseCode + "",context);

            HttpEventsArgs e = new HttpEventsArgs(handler, this, tagData, Integer.toString(responseCode));
            handler.onHttpUploadFailed(this, e);
        }

        if (result && (responseCode == 200 || responseCode == 201)) {
            //handler.onMediaUploaded();


            SurveyAppModel.getInstance().infoLog.addLog("Uploading " + fileToUpload, this.responseCode + "",context);

            HttpEventsArgs e = new HttpEventsArgs(handler, this, tagData, null);

            handler.onHttpUploadCompleted(this, e);

        } else {

            //handler.onMediaUploadFailed(responseCode);
            try {
                SurveyAppModel.getInstance().infoLog.addLog("Uploading " + fileToUpload, this.responseCode + "",context);
                HttpEventsArgs e = new HttpEventsArgs(handler, this, tagData, Integer.toString(responseCode));
                handler.onHttpUploadFailed(this, e);
            } catch (Exception e) {
                e.printStackTrace();
                SurveyAppModel.getInstance().errorLog.addLog("Uploading " + fileToUpload, e.getLocalizedMessage(),context);
            }
        }
    }

    @Override
    public String getRequestURL() {
        return null;
    }
}