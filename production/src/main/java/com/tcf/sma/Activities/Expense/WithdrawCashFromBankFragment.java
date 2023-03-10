package com.tcf.sma.Activities.Expense;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawCashFromBankFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    View view;
    private List<SchoolModel> schoolModels;
    private Spinner spinner_SelectSchool;
    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;

    private RecyclerView rv_check_images;
    private ExpenseAttachmentsAdapter expenseAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private AppCompatEditText et_amount,et_jvno,et_checkno;
    private Button btn_submit;
    private int value=0;
    private Spinner sp_category;
    int caregoryID = 0;
    TextView tv_tocategory;
    List<String> category;
    ArrayAdapter<String> categoryAdapter;
    RelativeLayout inner_relativelayout1;

    public WithdrawCashFromBankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_withdraw_cash_from_bank, container, false);
        inner_relativelayout1 = view.findViewById(R.id.inner_relativelayout1);
        tv_tocategory = view.findViewById(R.id.tv_tocategory);
        sp_category = view.findViewById(R.id.spinner_category);
        sp_category.setOnItemSelectedListener(this);
        value = 60000;
        populateCategorySpinners();
        return view;
    }

    private void init(View view){
        btn_submit = view.findViewById(R.id.btn_submit);
        et_amount = view.findViewById(R.id.et_amountno);
        et_jvno = view.findViewById(R.id.tv_jvno);
        et_checkno = view.findViewById(R.id.tv_checkno);
        rv_check_images = view.findViewById(R.id.rv_check_images);
        rv_check_images.setNestedScrollingEnabled(false);
        rv_check_images.setHasFixedSize(true);

        rv_check_images.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ExpenseAttachmentsAdapter.paths.clear();
        expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(false,getActivity(),attachments);
        rv_check_images.setAdapter(expenseAttachmentsAdapter);

    }

    private void populateSchoolSpinners() {

        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);

        schoolModels = DatabaseHelper.getInstance(getActivity()).getAllUserSchoolsForEmployeeListing();
        if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(getActivity(), R.layout.new_spinner_layout_black2, schoolModels);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void ShowToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void populateCategorySpinners() {
        category = new ArrayList<>();
        category.add(0, "Bank");
        category.add(1, "Cash");

        categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.new_spinner_layout2, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(categoryAdapter);
        sp_category.setSelection(0);
        tv_tocategory.setText("Cash");

    }

    // TODO: Rename and change types and number of parameters
    public static WithdrawCashFromBankFragment newInstance() {
        WithdrawCashFromBankFragment fragment = new WithdrawCashFromBankFragment();
    /*    Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }
}
