package com.tcf.sma.Survey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.util.List;

public class SurveyReportActivity extends DrawerActivity {

    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private Spinner spinner_SelectSchool;

    private View view;
    private WebView webView;
    private int project_id;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("PowerBI_Link"))
//        {
//            view = setActivityLayout(this, R.layout.activity_survey_report);
//            setToolbar("Taaluq Student Data", this, false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else
            setContentView(R.layout.activity_survey_report);


        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        progressDialog = ProgressDialog.show(this, "", "Loading...", false, false);
        webView.setWebViewClient(new WebViewClient() {

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(SurveyReportActivity.this, "Error: " + error.getDescription().toString(), Toast.LENGTH_LONG).show();
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                }
            }
        });

        if (!getIntent().hasExtra("PowerBI_Link")) {
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
                    quotaUpdater.updateQuota(requiredStorage * 2);
    //                super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
                }
            });

            webView.getSettings().setDomStorageEnabled(true);

// Set cache size to 8 mb by default. should be more than enough
            webView.getSettings().setAppCacheMaxSize(1024*1024*8);

// This next one is crazy. It's the DEFAULT location for your app's cache
// But it didn't work for me without this line.
// UPDATE: no hardcoded path. Thanks to Kevin Hawkins
            String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
            webView.getSettings().setAppCachePath(appCachePath);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAppCacheEnabled(true);


            if ( !isNetworkAvailable() ) { // loading offline
                webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
            }
        }

        if (getIntent().hasExtra("PowerBI_Link")) {
            findViewById(R.id.schoolBar).setVisibility(View.GONE);
            webView.loadUrl(getIntent().getStringExtra("PowerBI_Link"));

        } else {
            if (getIntent().getExtras().containsKey("project_id"))
                project_id = getIntent().getExtras().getInt("project_id");
            if (getIntent().getExtras().containsKey("school_id"))
                schoolId = getIntent().getExtras().getInt("school_id");

            populateSchoolSpinner();
        }
    }


    private void populateSchoolSpinner() {
        try {
            schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
//        schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

            SchoolAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout, schoolModels);
            SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_SelectSchool = findViewById(R.id.spinner_SelectSchool);
            spinner_SelectSchool.setAdapter(SchoolAdapter);
            spinner_SelectSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View views, int position, long id) {
                    if (adapterView.getId() == R.id.spinner_SelectSchool) {
                        schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                        AppModel.getInstance().setSpinnerSelectedSchool(SurveyReportActivity.this,
                                schoolId);
                        AppModel.getInstance().setSchoolInfo(SurveyReportActivity.this, schoolId, (TextView) findViewById(R.id.tv_regionName), (TextView) findViewById(R.id.tv_areaName));
//                        setAdapter();
                        String URL_GET_SURVEY_REPORTS= getString(R.string.API_DOMAIN_REPORTS);
                        String url = URL_GET_SURVEY_REPORTS + project_id + "/" + AppConstants.LANGUAGE.toLowerCase() + "/?schoolId=" + schoolId;
                        webView.loadUrl(url);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            /*if(getIntent().hasExtra("schoolId")) {
                schoolId = getIntent().getIntExtra("schoolId",0);
            }else{
                SchoolModel schoolModel = SurveyAppModel.getInstance().selectedProject.getSelectedSchool();
                if(schoolModel != null) {
                    schoolId = schoolModel.getId();
                }
            }*/

            int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this, schoolId);
            if (indexOfSelectedSchool > -1) {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool);
            }

//            schoolId = ((SchoolModel) spinner_SelectSchool.getSelectedItem()).getId();

            AppModel.getInstance().setSchoolInfo(SurveyReportActivity.this, schoolId, (TextView) findViewById(R.id.tv_regionName), (TextView) findViewById(R.id.tv_areaName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if(getIntent().hasExtra("schoolId")) {
            int schoolId = getIntent().getIntExtra("schoolId",0);
            SchoolModel schoolModel = DatabaseHelper.getInstance(DashboardActivity.this).getSchoolById(schoolId);
            tv_schoolName.setText(schoolModel.getName());

            ArrayList<SchoolExpandableModel> schoolExpandableModels = DatabaseHelper.getInstance(this).getSchoolByAreaRegionFromQuery();
            tv_areaName.setText(schoolExpandableModels.get(0).getArea());
            tv_regionName.setText(schoolExpandableModels.get(0).getArea());
        }else{
            SchoolModel schoolModel = SurveyAppModel.getInstance().selectedProject.getSelectedSchool();
            if(schoolModel != null) {
                tv_schoolName.setText(schoolModel.getId() + "-" + schoolModel.getName());

                SchoolExpandableModel schoolExpandableModels = DatabaseHelper.getInstance(this).getSchoolInfo(schoolModel.getId());
                tv_areaName.setText(schoolExpandableModels.getArea());
                tv_regionName.setText(schoolExpandableModels.getRegion());
            }
        }*/

    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}