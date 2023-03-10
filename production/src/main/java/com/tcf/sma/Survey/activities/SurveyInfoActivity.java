package com.tcf.sma.Survey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.mediaServices.ImageService;
import com.tcf.sma.Survey.model.SurveyAppModel;
import com.tcf.sma.databinding.ActivitySurveyInfoBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SurveyInfoActivity extends DrawerActivity {

    ActivitySurveyInfoBinding binding;
    int schoolId;
    int project_id;
    String project_name = "";

    DataHandler dbData;
    SurveyDBHandler dbSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurveyInfoBinding.inflate(getLayoutInflater());
//        setActionBar(getActionBar());
        setContentView(binding.getRoot());
//        setToolbar("Survey Info", this, false);
//        setToolbar("Survey Info", this, false);



        dbData = new DataHandler(getApplicationContext());
        dbSurvey = new SurveyDBHandler(getApplicationContext());

        project_id = SurveyAppModel.getInstance().selectedProject.get_key();
        project_name = SurveyAppModel.getInstance().selectedProject.get_name();

        schoolId = getIntent().getIntExtra("schoolId", 0);

        SchoolModel sm = DatabaseHelper.getInstance(this).getSchoolById(schoolId);
        SchoolExpandableModel areaRegionModel = DatabaseHelper.getInstance(this).getSchoolInfo(schoolId);
        binding.etArea.setText(areaRegionModel.getArea());
        binding.etRegion.setText(areaRegionModel.getRegion());
        binding.etSchoolId.setText(String.valueOf(sm.getId()));
        binding.etSchoolName.setText(sm.getName());
        binding.etEmisCode.setText(sm.getEMIS());
        binding.etPrincipalName.setText(sm.getPrincipalFirstName() + " " + sm.getPrincipalLastName());

//        binding.setSchoolModel(sm);

        UserModel userModel = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
        binding.etVisitorName.setText(userModel.getFirstname() + " " + userModel.getLastname());
        binding.etVisitorDesignation.setText(userModel.getDesignation());
        binding.etVisitDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy"));


        binding.btnBeginSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebView();
            }
        });



    }



    private void callWebView() {
        Intent webViewIntent = null;
        File temp_file = new File(ImageService.getNewFileName("frm_cache_", ".frm",this));
        long survey_id = dbSurvey.saveNewSurvey(temp_file.getPath(), dbData.getUser().getUsername(), "partial", project_id);

        //System.out.println(temp_file.getName()+"   "+survey_id+"   "+project_id+"   "+project_name);

        Project info = dbSurvey.getProject(project_id, false);

        if (Boolean.parseBoolean(info.getShowMapScreen())) {
//				webViewIntent = new Intent(this, SurveyMapActivity_New.class);
//            webViewIntent = new Intent(this, SurveyMapActivity.class);
            webViewIntent.putExtra("Building_permission", info.getShowBuildingScreen());
        } else {
            webViewIntent = new Intent(this, WebviewActivity.class);
        }

        String startDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        webViewIntent.putExtra("cached_file", temp_file.getName());
        webViewIntent.putExtra("local_survey_db_id", survey_id);
        webViewIntent.putExtra("project_id", project_id);
        webViewIntent.putExtra("project_name", project_name);
        webViewIntent.putExtra("DeviceTimestampStart", startDateandTime);
        webViewIntent.putExtra("start_activity_as", AppConstants.START_SURVEY_ACTIVITY);

        webViewIntent.putExtra("region", binding.etRegion.getText().toString());
        webViewIntent.putExtra("area", binding.etArea.getText().toString());
        webViewIntent.putExtra("schoolId", binding.etSchoolId.getText().toString());
        webViewIntent.putExtra("schoolName", binding.etSchoolName.getText().toString());
        webViewIntent.putExtra("emisCode", binding.etEmisCode.getText().toString());
        webViewIntent.putExtra("principalName", binding.etPrincipalName.getText().toString());
        webViewIntent.putExtra("visitDate", binding.etVisitDate.getText().toString());
        webViewIntent.putExtra("visitorName", binding.etVisitorName.getText().toString());
        webViewIntent.putExtra("visitorDesignation", binding.etVisitorDesignation.getText().toString());

        // custom dialog
//        if (info.needCategoryScreen.equals("1")) {
//            final Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.custom);
//            dialog.setTitle("Select Category");
//
//            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
//            btn_cancel.setVisibility(View.VISIBLE);
//            btn_cancel.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            catList = dbSurvey.retrieveCategory(project_id);
//
//            TextView tv_count = (TextView) dialog.findViewById(R.id.districtsCount);
//            tv_count.setText("No of Categories :" + catList.size());
//
//            dialog.show();
//        } else {
        startActivity(webViewIntent);
        finish();
//				changeIntent(webViewIntent);
//        }
    }
}