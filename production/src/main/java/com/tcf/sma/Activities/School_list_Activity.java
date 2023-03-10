package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.tcf.sma.Adapters.SchoolExpandableAdapter;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.MetaDataResponseModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class School_list_Activity extends DrawerActivity implements Callback<MetaDataResponseModel> {
    View view;
    ExpandableListView el_schools;
    SchoolExpandableAdapter adapter;
    SchoolModel schoolModel;
    ArrayList<SchoolExpandableModel> schoolExpandableModels;
    SearchView sv_school;
    boolean synStudent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_select_school);
        setToolbar("Select School", this, false);
        init(view);
        AppModel.getInstance().saveState(this, AppConstants.KEY_STATE, AppConstants.VALUE_SCHOOL_SELECT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserModel user = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
        if (user != null && user.getPassword_change_on_login() == 1) {
            startActivity(new Intent(this, ChangePasswordActivity.class).putExtra("isFromLogin", true));
        }
    }

    private void init(View view) {
        sv_school = (SearchView) view.findViewById(R.id.sv_school);
        el_schools = (ExpandableListView) view.findViewById(R.id.el_schoollist);

        ArrayList<SchoolModel> schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchools();

        for (SchoolModel model : schoolModelList) {
            if (DatabaseHelper.getInstance(this).getAllStudentsCount(model.getId()) > 0) {
                synStudent = false;
//                Intent in = new Intent(School_list_Activity.this, NewDashboardActivity.class);
//                startActivity(in);
//                finish();
                break;
            }
        }

        schoolExpandableModels = DatabaseHelper.getInstance(this).getSchoolByAreaRegionFromQuery();
        Log.e("schoolCount","Activity:=>> School_list_Activity.java");
        adapter = new SchoolExpandableAdapter(this, schoolExpandableModels, false, false);
        el_schools.setAdapter(adapter);

        for (SchoolExpandableModel sm : schoolExpandableModels) {
            el_schools.expandGroup(schoolExpandableModels.indexOf(sm));
        }

        sv_school.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.filterData(query);
                return false;
            }
        });

    }

    @Override
    public void onResponse(Call<MetaDataResponseModel> call, Response<MetaDataResponseModel> response) {
        if (response.isSuccessful()) {
            MetaDataResponseModel mdrs = response.body();
            try {
                DataSync ds = new DataSync(this);

                ds.SyncClasses(mdrs.getTCFClass());
                ds.SyncSections(mdrs.getSection());
//                ds.SyncSchoolClasses(mdrs.getSchool_Classes());
                ds.SyncWithdrawalReasons(mdrs.getWithdrawal_Reason());
                ds.SyncCampus(mdrs.getCampuses());
                ds.SyncLocation(mdrs.getLocation());
                ds.SyncAreas(mdrs.getArea());
                ds.SyncRegion(mdrs.getRegion());
                AppModel.getInstance().hideLoader();
//                ds.SyncSchoolSSRSummary(mdrs.getSchoolSSRSummary(), SurveyAppModel.getInstance().getSelectedSchool(School_list_Activity.this));
                ArrayList<Integer> userSchools = DatabaseHelper.getInstance(this).getAllUserSchool();
//                if (schoolModel.getSchoolsList().size() > 1) {
//                if (userSchools.size() > 1 && synStudent) {
//                    //Download student data
//                    AppModel.getInstance().showLoader(this, "Loading Students Data", "Please wait...");
//                    AppModel.getInstance().syncStudentData(this, AppModel.getInstance().getSelectedSchool(this),
//                            DatabaseHelper.getInstance(this).getLatestModifiedOn(AppModel.getInstance().getSelectedSchool(this)));
////                    startSyncService();
//                } else {
                    Intent intent = new Intent(this, NewDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("schoolId", Sclmdl.get(getAdapterPosition()).getId());
                    startActivity(intent);
                    finish();
                if (DatabaseHelper.getInstance(this).getStudentTableCount() > 0)
                    AppModel.getInstance().startSyncService(this, SyncProgressHelperClass.SYNC_TYPE_BAU_SYNC_ID);
                else
                    AppModel.getInstance().startSyncService(this, SyncProgressHelperClass.SYNC_TYPE_FIRST_SYNC_ID);
//                }

            } catch (Exception e) {
                e.printStackTrace();
                AppModel.getInstance().hideLoader();
            }
        } else {
            //show error dialogs
            Toast.makeText(this, "Sync error", Toast.LENGTH_SHORT).show();
            AppModel.getInstance().hideLoader();
        }
    }

    @Override
    public void onFailure(Call<MetaDataResponseModel> call, Throwable t) {
        Toast.makeText(this, "Sync fail", Toast.LENGTH_SHORT).show();
        AppModel.getInstance().hideLoader();
    }

}
