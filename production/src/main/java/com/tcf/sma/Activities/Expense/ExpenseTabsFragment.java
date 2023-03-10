package com.tcf.sma.Activities.Expense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.tcf.sma.Adapters.ViewPagerAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.List;

import static com.tcf.sma.Activities.Expense.AddNewRecordActivity.pager;


public class ExpenseTabsFragment extends Fragment implements  AdapterView.OnItemSelectedListener{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    View view;
    public static int schoolId = 0;
    TextView tv_tocategory;
    private List<SchoolModel> schoolModels;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private Spinner spinner_SelectSchool;
    private ImageView iv_swipeleft;
    private String transaction = "";
    private TextView tv_regionName,tv_areaName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_expense_tabs, container, false);
        init(view);
        setTabFragmentAdapter();
        populateSpinners();

        iv_swipeleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change screen to second tab (calculator)
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });
        return view;
    }

    private void init(View view){
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
        iv_swipeleft = view.findViewById(R.id.iv_swipeleft);
        tv_regionName = view.findViewById(R.id.tv_regionName);
        tv_areaName = view.findViewById(R.id.tv_areaName);
    }

    private void setupViewPager() {
        if (getArguments() != null && getArguments().getString(AppConstants.KEY_TRANSACTION_TYPE) !=null) {
            transaction = getArguments().getString(AppConstants.KEY_TRANSACTION_TYPE);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFrag(new IncomeFragment(), "Income");
        viewPagerAdapter.addFrag(TransferFragment.newInstance(transaction), "Transfer");
        viewPagerAdapter.addFrag(AddExpenseFragment.newInstance(transaction), "Expenses");


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        if (getArguments() != null && getArguments().getString(AppConstants.KEY_TRANSACTION_TYPE) !=null) {
            if (getArguments() != null && transaction.equalsIgnoreCase(AppConstants.VALUE_TRANSACTION_WITHDRAW) || transaction.equalsIgnoreCase(AppConstants.VALUE_TRANSACTION_DEPOSIT)) {
                viewPager.setCurrentItem(pager.getCurrentItem() + 1, true);
            } else  if (transaction.equalsIgnoreCase(AppConstants.VALUE_EXPENSE_TYPE_PETTYCASH) || transaction.equalsIgnoreCase(AppConstants.VALUE_EXPENSE_TYPE_ADVANCE) || transaction.equalsIgnoreCase(AppConstants.VALUE_EXPENSE_TYPE_SALARY)) {
                viewPager.setCurrentItem(pager.getCurrentItem() + 2, true);
            }
        }
    }

    public void setTabFragmentAdapter() {
        getActivity().runOnUiThread(() -> setupViewPager());
    }
    // TODO: Rename and change types and number of parameters
    public static ExpenseTabsFragment newInstance(String param) {
        ExpenseTabsFragment fragment = new ExpenseTabsFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.KEY_TRANSACTION_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(getActivity()).getAllUserSchoolsForExpense();
       /* if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));
*/
        SchoolAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectSchool.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, getActivity());
        if (indexOfSelectedSchool > -1) {
            //If select school with id 0 is selected show any school by default
            if (schoolModels != null && schoolModels.size() > 1 &&
                    schoolModels.get(indexOfSelectedSchool).getName().equals(getResources().getString(R.string.select_school))){
                spinner_SelectSchool.setSelection(indexOfSelectedSchool + 1);
            }else {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool);
            }
        }

        if(DatabaseHelper.getInstance(getActivity()).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V ||
                DatabaseHelper.getInstance(getActivity()).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM) {
            spinner_SelectSchool.setEnabled(false);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
            setSchoolInfo(schoolId);
            setTabFragmentAdapter();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setSchoolInfo(int SchoolID) {
        if (SchoolID != 0) {
            try {
                //setting the school info box
                SchoolExpandableModel mod = DatabaseHelper.getInstance(getActivity()).getSchoolInfo(schoolId);
                tv_regionName.setText(mod.getRegion());
//                tv_campusName.setText(mod.getCampus());
                tv_areaName.setText(mod.getArea());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tv_regionName.setText("");
//            tv_campusName.setText("");
            tv_areaName.setText("");
        }
    }

}
