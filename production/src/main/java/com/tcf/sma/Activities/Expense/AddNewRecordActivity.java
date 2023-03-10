package com.tcf.sma.Activities.Expense;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.R;


public class AddNewRecordActivity extends DrawerActivity {
    View view;
    MyPageAdapter pageAdapter;
    public static ViewPager pager;
    public String putextra = "";
    //public static boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_add_new_record);
        if (getIntent().hasExtra(AppConstants.KEY_TRANSACTION_TYPE)) {
            putextra = getIntent().getStringExtra(AppConstants.KEY_TRANSACTION_TYPE);
        }

        if (getIntent().hasExtra(AppConstants.KEY_EXPENSE_TYPE)) {
            putextra = getIntent().getStringExtra(AppConstants.KEY_EXPENSE_TYPE);
        }

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), putextra);
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(pageAdapter);
    }


    /**
     * Custom Page adapter
     */
    public static class MyPageAdapter extends FragmentStatePagerAdapter {
        String selectfragment = "";

        public MyPageAdapter(FragmentManager fm, String selectfragment) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.selectfragment = selectfragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ExpenseTabsFragment.newInstance(selectfragment);
//                case 1:
//                    return ExpenseCalculatorFragment.newInstance();

            }
            return null;
        }
    }
}