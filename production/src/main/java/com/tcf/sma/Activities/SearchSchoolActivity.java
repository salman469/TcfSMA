package com.tcf.sma.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.tcf.sma.Adapters.SchoolExpandableAdapter;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.MetaDataResponseModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchSchoolActivity extends DrawerActivity implements Callback<MetaDataResponseModel> {

    View view;
    //    RecyclerView el_schools;
    ExpandableListView el_schoolList;
    //    SearchSchoolAdapter adapter;
    SchoolExpandableAdapter adapter;
    SchoolModel schoolModel;
    SearchView sv_school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_search_school);
        setToolbar("Search School", this, false);
        init(view);
    }

    private void init(View view) {
        sv_school = (SearchView) view.findViewById(R.id.sv_school);
        el_schoolList = (ExpandableListView) view.findViewById(R.id.el_schoollist);

//        el_schoolList.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        el_schools.setLayoutManager(llm);

        ArrayList<SchoolModel> schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchools();

        ArrayList<SchoolExpandableModel> ls = DatabaseHelper.getInstance(this).getSchoolByAreaRegionFromQuery();

//        schoolModel = new SchoolModel();
//        schoolModel.setSchoolsList(schoolModelList);
//        adapter = new SearchSchoolAdapter(this, schoolModel.getSchoolsList());
        Log.e("schoolCount","Activity:=>> SearchActivityActivity.java");
        adapter = new SchoolExpandableAdapter(this, ls);

        el_schoolList.setAdapter(adapter);

        for (SchoolExpandableModel sem : ls) {
            el_schoolList.expandGroup(ls.indexOf(sem));
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
                ds.SyncWithdrawalReasons(mdrs.getWithdrawal_Reason());
                ds.SyncCampus(mdrs.getCampuses());
                ds.SyncLocation(mdrs.getLocation());
                ds.SyncAreas(mdrs.getArea());
                ds.SyncRegion(mdrs.getRegion());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppModel.getInstance().hideLoader();
//                        AppModel.getInstance().showLoader(SearchSchoolActivity.this, "Loading Student Data", "Please wait...");
                    }
                });

//                AppModel.getInstance().syncStudentData(this, AppModel.getInstance().getSearchedSchoolId(this),
//                        DatabaseHelper.getInstance(this).getLatestModifiedOn(AppModel.getInstance().getSearchedSchoolId(this)));

                //Clear previous sync data:
                current_activity.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "false").commit();
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
