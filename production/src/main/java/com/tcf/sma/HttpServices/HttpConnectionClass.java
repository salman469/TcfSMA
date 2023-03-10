package com.tcf.sma.HttpServices;

import android.content.Context;
import android.util.Log;

import com.tcf.sma.Models.AppModel;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Haseeb on 1/13/2016.
 */
public class HttpConnectionClass {
    private static final String LOG_TAG = "logtag";
    public static int responseCode;
    public static String responseJson = "";
    Context context;

    public HttpConnectionClass(Context context) {
        this.context = context;
    }

    public int sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(50 * 1000);
            conn.setConnectTimeout(50 * 1000);
            conn.setRequestProperty("Content-Type", "application/json"); //optional header
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(postDataParams));
            writer.flush();
            writer.close();
            os.close();


            responseCode = conn.getResponseCode();
            Log.d("Response Message --> ", conn.getResponseMessage());
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
                responseJson = response.toString();

            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                responseJson = "forbidden";
            }

//            switch (responseCode) {
//                case HttpsURLConnection.HTTP_OK:
//
//                    response = br.readLine();
//                case HttpURLConnection.HTTP_BAD_REQUEST:
//                    response = "Invalid username or password";
//                    break;
//                default:
//                    response = "error";
//                    break;
//            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseCode;
    }

    public String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));


            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }


    public int postJSONObject(String myurl, JSONObject parameters) {
        HttpURLConnection conn = null;
        responseCode = 0;
        try {
            boolean retry = false;

            do {
                StringBuffer response = null;
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30 * 1000);
                conn.setConnectTimeout(30 * 1000);

//                String token = SurveyAppModel.getInstance().getToken(context);
//                if (token != null) {
//                    conn.setRequestProperty("Authorization", "Bearer " + token);
//                }

                conn.setRequestProperty("Content-Type", "application/json");

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(parameters.toString());
                writer.close();
                out.close();
                responseCode = conn.getResponseCode();
                // Log.d("Logtag", "responseCode" + responseCode);

                switch (responseCode) {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        responseJson = response.toString();
                        retry = false;
                        break;
//                    case 401:
//                        if (!retry) {
//                            RetryLogin();
//                            retry = true;
//                        } else {
//                            retry = false;
//                        }
//                        break;

                }
            } while (retry);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return responseCode;
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    public void RetryLogin() {
//        try {
//            String password = SurveyAppModel.getInstance().getPassword(context);
//            String username = SurveyAppModel.getInstance().getUsername(context);
//            JSONObject data = new JSONObject();
//            data.put("login", "1");
//            data.put("token", password);
//            data.put("username", username);
//            data.put("deviceid", SurveyAppModel.getInstance().getFCMToken(context));
//
//            HttpConnectionClass loginConnection = new HttpConnectionClass(context);
//            int responseCode = loginConnection.postJSONObject(getString(R.string.SURVEY_BASE_URL) + AppConstants.URL_REGISTER, data);
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                JSONObject jsonRoot = new JSONObject(loginConnection.responseJson);
//                SurveyAppModel.getInstance().setToken(
//                        jsonRoot.getString("token").toString(),
//                        username,
//                        password,
//                        loginConnection.responseJson,
//                        context);
//            }
//        } catch (JSONException ex) {
//            ex.getStackTrace();
//        }
    }

    public int uploadFile(String sourceFileUri, String upLoadServerUri) {

//        String fdir = context.getDir("TCF", Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "TCF Images";
//        String fileName = fdir + '/' + sourceFileUri;
        String fileName = sourceFileUri;
        String name = fileName.substring(fileName.lastIndexOf("/") + 1);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);
        int serverResponseCode = 0;

        try {

            if (!new File(sourceFileUri).exists()) {
                AppModel.getInstance().appendLog(context, "\nFile not found named :" + sourceFileUri + "\n");
                return 0;
            }

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            String token = AppModel.getInstance().getToken(context);
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", name);
            conn.connect();

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //dos.writeBytes("Content-Disposition: form-data; name=\"profilepicture\"; filename=\"" + fileName + "\"");

            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + name + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = null;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseJson = response.toString();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (responseCode > 204) {
                AppModel.getInstance().appendErrorLog(context, "Upload image response message :" + serverResponseMessage + " response code " + serverResponseCode);
            }
            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            AppModel.getInstance().appendLog(context, "\nException generated in HTTP URL CONNECTION :" + ex.getMessage() + "\n");
            return serverResponseCode;
        }
        return serverResponseCode;
    }

    public int uploadFile(String sourceFileUri, String upLoadServerUri, String logFileName) {

//        String fdir = context.getDir("TCF", Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "TCF Images";
//        String fileName = fdir + '/' + sourceFileUri;
        String fileName = sourceFileUri;
        String name = fileName.substring(fileName.lastIndexOf("/") + 1);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);
        int serverResponseCode = 0;

        try {

            if (!new File(sourceFileUri).exists()) {
                AppModel.getInstance().appendLog(context, "\nFile not found named :" + sourceFileUri + "\n");
                return 0;
            }

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            String token = AppModel.getInstance().getToken(context);
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty(logFileName, name);
            conn.connect();

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //dos.writeBytes("Content-Disposition: form-data; name=\"profilepicture\"; filename=\"" + fileName + "\"");

            dos.writeBytes("Content-Disposition: form-data; name=\"" + logFileName + "\";filename=\"" + name + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = null;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseJson = response.toString();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (responseCode > 204) {
                AppModel.getInstance().appendErrorLog(context, "Upload response message :" + serverResponseMessage + " response code " + serverResponseCode);
            }
            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            AppModel.getInstance().appendLog(context, "\nException generated in HTTP URL CONNECTION :" + ex.getMessage() + "\n");
            return serverResponseCode;
        }
        return serverResponseCode;
    }

    public void ImageDownload(String requestURL, String pictureNamefromServer) {
        try {
            String fdir = context.getDir("TCF", Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "TCF Images";
            File dir = new File(fdir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, pictureNamefromServer);
            if (!file.exists()) {
                URL url = new URL(requestURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                FileOutputStream bos = new FileOutputStream(file);
                int i;

                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
                bos.flush();
                bos.close();
                bis.close();
            }

        } catch (Exception e) {
            Log.d("ImageDownload", e.getMessage());
        }

    }


//    public void ImageDownload(String requestURL, String pictureNamefromServer) {
//        try {
//            URL url = new URL(requestURL);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
//
//            String name = pictureNamefromServer.substring(pictureNamefromServer.lastIndexOf("/") + 1);
//            Log.d("dddname", name);
//            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AKUH/profilepictures/");
//            if (!path.exists()) {
//                path.mkdirs();
//            }
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AKUH/profilepictures/" + name);
//
//            if (!file.exists()) {
//                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
//                int i;
//
//
//                while ((i = bis.read()) != -1) {
//                    bos.write(i);
//                }
//                bos.flush();
//                bos.close();
//                bis.close();
//            }
//
//        } catch (Exception e) {
//        }
//
//    }
}
