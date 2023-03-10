package com.tcf.sma.Activities.Expense;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class WithdrawCashFromBank extends DrawerActivity implements AdapterView.OnItemSelectedListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("correction")){
            view = setActivityLayout(this, R.layout.demo2);
            setToolbar("Withdraw Cash Correction", this, false);
        }
        else{
            view = setActivityLayout(this, R.layout.activity_withdraw_cash_from_bank);
            setToolbar("Withdraw Cash from bank", this, false);
        }
        value = 60000;
        init(view);
        populateSpinners();

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (!et_amount.getText().toString().equals("")) {
                    if (Integer.parseInt(et_amount.getText().toString()) <= value) {
                        Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                    } else {
                        et_amount.setError("you have insufficient balance in your account");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_jvno.getText().toString().equals("")){
                    ShowToast("Please enter J.V no");
                    return;
                }
                if(et_checkno.getText().toString().equals("")){
                    ShowToast("Please enter check no");
                    return;
                }

                if(!et_amount.getText().toString().equals("")) {
                    int amount = Integer.parseInt(et_amount.getText().toString());
                    if (amount <= value) {
                        if(ExpenseAttachmentsAdapter.paths.size()<=0){
                            ShowToast("Please insert image");
                            return;
                        } else {
                            finish();
                        }
                    } else {
                        if(ExpenseAttachmentsAdapter.paths.size()<=0){
                            ShowToast("Please insert image");
                            return;
                        }
                    }
                }
                else {
                    ShowToast("Please enter amount");
                }
            }
        });
    }
    private void init(View view){
        btn_submit = view.findViewById(R.id.btn_submit);
        et_amount = view.findViewById(R.id.et_amountno);
        et_jvno = view.findViewById(R.id.tv_jvno);
        et_checkno = view.findViewById(R.id.tv_checkno);
        rv_check_images = view.findViewById(R.id.rv_check_images);
        rv_check_images.setNestedScrollingEnabled(false);
        rv_check_images.setHasFixedSize(true);

        rv_check_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ExpenseAttachmentsAdapter.paths.clear();
        expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(false,this,attachments);
        rv_check_images.setAdapter(expenseAttachmentsAdapter);

        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForEmployeeListing();
        if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectSchool.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            //If select school with id 0 is selected show any school by default
            if (schoolModels != null && schoolModels.size() > 1 &&
                    schoolModels.get(indexOfSelectedSchool).getName().equals(getResources().getString(R.string.select_school))){
                spinner_SelectSchool.setSelection(indexOfSelectedSchool + 1);
            }else {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool);
            }
        }

        if(DatabaseHelper.getInstance(WithdrawCashFromBank.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V ||
                DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM) {
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
}
