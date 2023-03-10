package com.tcf.sma.Survey.httpServices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tcf.sma.R;
import com.tcf.sma.Survey.activities.DashboardActivity;
import com.tcf.sma.Survey.model.SurveyAppModel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


public class APIServiceManager extends AsyncTask<Object, Void, Boolean> {

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    private static final DefaultHttpClient httpclient = getNewHttpClient();

    private static final String TAG = "httpService";

    public final String GET = "GET";
    public final String POST = "POST";
    public final int CONTENT_TYPE_URL_ENCODED = 0;
    public final int CONTENT_TYPE_TEXT_XML = 1;
    public String progressMsg = "Loading";
    public int RESPONSE_BYTES = 0;
    public int RESPONSE_INPUT_STREAM = 1;
    public String jsonBody = "";
    /*private HashMap<String, String> filesToUpload = new HashMap<String, String>();*/
    public boolean showProgress = true;
    Context context;
    SharedPreferences sharedpreferences;
    IHttpRequester iRequester = null;
    Activity instance;
    // A ProgressDialog object
    private ProgressDialog progressDialog;
    private HashMap<String, String> postValues;
    private String requestMethod;
    private String requestUrl;
    private int requestCode;
    private String responseString = "";
    private int responseCode;

    public static DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new APISSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
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
	
/*	public void addBinaryFile(String postVariableName, String sourceFile) {
		filesToUpload.put(postVariableName, sourceFile);
	}*/

    public DefaultHttpClient getClient() {
        return httpclient;
    }

    @Override
    protected Boolean doInBackground(Object... argList) {

        try {
            boolean returnflag = true;

            String url = (String) argList[0];
            int request_code = (Integer) argList[1];
            this.instance = (Activity) argList[2];
            this.iRequester = (IHttpRequester) argList[2];
            this.requestMethod = (String) argList[3];

            @SuppressWarnings("unchecked")
            HashMap<String, String> values = (HashMap<String, String>) argList[4];

            this.postValues = values;
            this.requestCode = request_code;
            this.requestUrl = url;

            DefaultHttpClient client = getClient();
//			HttpClient httpClient = client;

//			SurveyAppModel.client = client;

            instance.runOnUiThread(new Runnable() {
                public void run() {
                    // Create a new progress dialog
                    if (!instance.isFinishing()) {
                        if (showProgress) {

                            if (progressMsg.equals("Loading")) {

                            }
                            progressDialog = ProgressDialog.show(instance, "",
                                    progressMsg, false, false);
                        }
                    }
                }
            });

            if (!isNetworkAvailable())
                return false;

            if (!url.startsWith("https:"))
                url = prepareURL(url);

            System.out.println("doInBackground called url " + url);

            try {
                HttpResponse response;

                //client.getCookieStore().clear();

                System.out.println("Initial set of cookies:");

	/*			String userLocale = "";
				List<Cookie> cookies = client.getCookieStore().getCookies();
				for (int i = 0; i < cookies.size(); i++) {
	                if (cookies.get(i).getName().equalsIgnoreCase("user-prefs-locale"))
	                	userLocale = cookies.get(i).getValue();
	            }*/

                if (this.requestMethod.equals(GET)) {

                    HttpGet httpGet = new HttpGet(url);

                    response = client.execute(httpGet);

                } else if (this.requestMethod.equals(POST)) {

                    HttpPost httpPost = new HttpPost(url);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    if (values != null) {
                        for (Map.Entry<String, String> entry : values.entrySet()) {
                            nameValuePairs.add(new BasicNameValuePair(entry
                                    .getKey(), entry.getValue()));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                    }

                    response = client.execute(httpPost);
                } else {

                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new StringEntity(jsonBody, "UTF8"));
                    httpPost.setHeader("Content-type", "application/json");

                    SurveyAppModel.getInstance().infoLog.addLog("sending Survey", "Posting Survey",instance.getApplicationContext());

	/*		    	if(filesToUpload.size() != 0) {
			    		for(String k :filesToUpload.keySet()) {
			    			File f = new File(filesToUpload.get(k));
			    			if(f.exists()){
			    				try {
			    					httpPost.setEntity(new ByteArrayEntity(readFile(f)));
			    				}
			    				catch(Exception e) {

			    				}

			    			}
			    		}
			    	}*/


                    response = client.execute(httpPost);
                }

                /* setting a response code. */


                this.responseCode = response.getStatusLine().getStatusCode();

                if (responseCode >= 200 && responseCode <= 210)
                    SurveyAppModel.getInstance().infoLog.addLog(url, "Response Code : " + this.responseCode + ", Request Code : " + this.requestCode + "",instance.getApplicationContext());
                else
                    SurveyAppModel.getInstance().errorLog.addLog(url, this.responseCode + "",instance.getApplicationContext());

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(response.getStatusLine().getStatusCode());
                Header[] headers = response.getAllHeaders();
                for (int i = 0; i < headers.length; i++) {
                    System.out.println(headers[i]);
                }

                returnflag = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

                // Get the response
                StringBuilder str = new StringBuilder();

                if (returnflag) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
                    String line = "";

                    while ((line = rd.readLine()) != null) {
                        str.append(line);
                    }
                }

                this.responseString = str.toString();

            } catch (UnknownHostException ex) {

                System.out.println(TAG + " UnknownHostException ");
                SurveyAppModel.getInstance().errorLog.addLog("Exception API Service Manager", ex.getLocalizedMessage(),instance.getApplicationContext());
                ex.printStackTrace();
            } catch (IOException e) {

                returnflag = false;
                if (e != null) {
                    SurveyAppModel.getInstance().errorLog.addLog("Exception API Service Manager", e.getLocalizedMessage(),instance.getApplicationContext());
                    System.out.println(TAG + " responseCode " + responseCode);
                    e.printStackTrace();
                }
            } catch (NullPointerException ex) {
                SurveyAppModel.getInstance().errorLog.addLog("Exception API Service Manager", ex.getLocalizedMessage(),instance.getApplicationContext());
                returnflag = false;
            } catch (IndexOutOfBoundsException ex) {
                SurveyAppModel.getInstance().errorLog.addLog("Exception API Service Manager", ex.getLocalizedMessage(),instance.getApplicationContext());
                returnflag = false;
            } catch (Exception e) {
                e.printStackTrace();
                SurveyAppModel.getInstance().errorLog.addLog("Exception API Service Manager", e.getLocalizedMessage(),instance.getApplicationContext());
            } finally {

            }

            if (instance.getClass() == DashboardActivity.class)
                SurveyAppModel.getInstance().isSurveySyncStarted = false;


            if ((responseCode == 200 || responseCode == 201) && requestMethod.equals(AppConstants.GET)) {
                SurveyAppModel.getInstance().onRequestSuccess(responseString, request_code, instance);
            }

            return returnflag;
        } catch (Exception e) {
            e.printStackTrace();
            SurveyAppModel.getInstance().errorLog.addLog("Exception", e.getLocalizedMessage(), (Activity) argList[2]);
            return false;
        }
    }

    public String fixJsonResult(String json) {

        json = json.replace("} {", "} , {");
        json = json.replace("\n", "");
        json = json.replace("\r", "");
        json = json.replace("\t", "");
        json = json.replace("},]", "}]");
        json = json.replace(",}", "}");
        json = json.replace(", }", "}");
        json = json.replace("],]", "]]");

        json = json.replace("\\/", "/");

        return json;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) instance
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        return activeNetworkInfo != null
                && activeNetworkInfo.isConnected();
    }

    protected void onPostExecute(Boolean result) {

        Log.d("json response", responseString);

        // close the progress dialog
        if (showProgress) {
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            } catch (Exception e) {

            }
        }

// SHAHZAIB's Work 01/01/2015

        if (showProgress && !isNetworkAvailable()) {

            if (responseString.equals("")) {
                Toast.makeText(instance.getBaseContext(), "Connect to Internet",
                        Toast.LENGTH_LONG).show();

                iRequester.onRequestFailure(responseCode, requestCode);

                return;
            }
        }


        switch (responseCode) {

            case 200:
                break;

            case 0:

                result = false;
                if (showProgress && responseString.equals(""))
                    Toast.makeText(instance.getBaseContext(), "Unable to get response from server.  Please try again.", Toast.LENGTH_LONG).show();
                else
                    result = true;
                break;
        }

        if (result) {
            iRequester.onRequestSuccess(responseString, requestCode, context);
        } else {
            iRequester.onRequestFailure(responseCode, requestCode);
        }

    }

    // SHAHZAIB's Work 01/01/2015
    public String prepareURL(String url) {

//        url = context.getString(R.string.SURVEY_BASE_URL) + url;
        url = instance.getBaseContext().getString(R.string.SURVEY_BASE_URL) + url;
        return url;
    }

    public String fixDescriptionText(String obj) {

        obj = obj.replace("\\\"", "\"");
        obj = obj.replace("\\\\", "\\");
        obj = obj.replace("\\/", "/");
        obj = obj.replace("\\r\\n", "<br>");
        obj = obj.replace("\r\n", "<br>");
        obj = obj.replace("\\n", "<br>");
        obj = obj.replace("\n", "<br>");
        obj = obj.replace("\\t", "	");
        obj = obj.replace("\t", "	");

        return obj;
    }
}
