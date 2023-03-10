package com.tcf.sma.Activities.FeesCollection.AccountStatement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.ViewPagerAdapter;
import com.tcf.sma.Fragment.AccountStatementSchoolFragment;
import com.tcf.sma.Fragment.AccountStatementStudentFragment;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.List;

public class AccountStatementNewActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener {
    public int schoolId = 0;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Spinner spnSchools;
    private View screen;
    private List<SchoolModel> schoolModels;
    private boolean isRunForFirstTime = false;
    private int roleID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_account_statement_new);
        setToolbar("Account Statement", this, false);

        init();
        populateSchoolSpinner();
        setupViewPager();
    }

    private void init() {
        viewPager = (ViewPager) screen.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) screen.findViewById(R.id.tabs);
        spnSchools = (Spinner) screen.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }
    }

    private void setupViewPager() {
        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent.hasExtra("StudentGrNo")) {
            try {
                bundle = new Bundle();
                String grNo = intent.getStringExtra("StudentGrNo");
                bundle.putString("StudentGrNo", grNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AccountStatementSchoolFragment schoolFragment = new AccountStatementSchoolFragment();
        AccountStatementStudentFragment studentFragment = new AccountStatementStudentFragment();

        if (bundle != null) {
            studentFragment.setArguments(bundle);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPagerAdapter.addFrag(schoolFragment, "School");
        viewPagerAdapter.addFrag(studentFragment, "Student");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        if (intent.hasExtra("StudentGrNo")) {
            viewPager.setCurrentItem(1, true);
        }
    }

    private void populateSchoolSpinner() {
        schoolId = AppModel.getInstance().getSelectedSchool(this);
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);

//        if (schoolModels.size() > 0) {
//            tv_title.setText("Statement for School \n" + schoolModels.get(0).getName());
//            ll_title.setVisibility(View.VISIBLE);
//        } else {
//            ll_title.setVisibility(View.GONE);
//        }

        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            spnSchools.setSelection(indexOfSelectedSchool);
            schoolId = AppModel.getInstance().getSpinnerSelectedSchool(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSchools:
                schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);
                setupViewPager();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
