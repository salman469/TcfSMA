package com.tcf.sma.Activities.Expense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.HR.EmployeeListing;
import com.tcf.sma.Adapters.Expense.ExpenseClosingAdapter;
import com.tcf.sma.Adapters.HR.EmployeeListingAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseAmountClosingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseClosingItem;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClosingActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener{

    View view;
    private List<SchoolModel> schoolModels;
    private Spinner spinner_SelectSchool;
    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private TextView txt_closing_date, tv_limitAmount, tv_remaining, totalExpense, tv_areaName, tv_regionName;
    private AppCompatEditText et_comments;
    RecyclerView rv_closing;
    private String closingDate;
    private List<Integer> amountList;
    private List<ExpenseClosingItem> closingItemList = new ArrayList<>();
    private ExpenseClosingAdapter expenseClosingAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_closing);
        setToolbar("Petty Cash Closing",this, false);
        init(view);
        setInitialDate();
        populateSpinners();
    }

    public void goBack(View view){
        finish();
    }

    public void alertdialogOnSubmit(View view){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure you want to submit closing?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        submitClosing();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    private void init(View view){
        tv_areaName = view.findViewById(R.id.tv_areaName);
        tv_regionName = view.findViewById(R.id.tv_regionName);
        totalExpense = view.findViewById(R.id.totalExpense);
        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
        txt_closing_date = view.findViewById(R.id.txt_closing_date);
        tv_limitAmount = view.findViewById(R.id.tv_limitAmount);
        tv_remaining = view.findViewById(R.id.tv_remaining);
        et_comments = view.findViewById(R.id.et_comments);
        rv_closing = view.findViewById(R.id.rv_closing);

    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForExpense();
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

        if(DatabaseHelper.getInstance(ClosingActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V) {
            spinner_SelectSchool.setEnabled(false);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
            getData();
            setSchoolInfo(schoolId);
            AppModel.getInstance().setSpinnerSelectedSchool(this,schoolId);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setInitialDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        txt_closing_date.setText(formattedDate);
    }

    private void getData() {

        closingDate = ExpenseHelperClass.getInstance(this).getClosingDate(schoolId);
        amountList = ExpenseHelperClass.getInstance(this).getAmountsForClosing(schoolId, closingDate);
        int SpentAmountofHeadfromT = ExpenseHelperClass.getInstance(this).getSpentAmountofHeadfromTransactionforClosing(schoolId,2,closingDate);
        amountList.add(SpentAmountofHeadfromT);
        closingItemList = ExpenseHelperClass.getInstance(this).getPettyCashHeadsForClosing(schoolId,closingDate);

            //populate recyclerview
        rv_closing.setNestedScrollingEnabled(false);
        rv_closing.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_closing.setLayoutManager(linearLayoutManager);
        expenseClosingAdapter = new ExpenseClosingAdapter(this, closingItemList);
        rv_closing.setAdapter(expenseClosingAdapter);

        if(amountList.size()>0) {
            txt_closing_date.setText(AppModel.getInstance().convertDatetoFormat(closingDate, "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy"));
            tv_limitAmount.setText(String.format("Amount for Expense = Rs. %s/-", AppModel.getInstance().formatNumberInCommas(amountList.get(0))));
            totalExpense.setText(String.format("Rs. %s/-", AppModel.getInstance().formatNumberInCommas(amountList.get(1))));
            int remaining = amountList.get(0) - amountList.get(1);
            tv_remaining.setText(String.format("Amount for Expense Remaining = Rs. %s/-", AppModel.getInstance().formatNumberInCommas(remaining)));
        }
        }

    private void submitClosing(){

        ExpenseAmountClosingModel eac = new ExpenseAmountClosingModel();
        eac.setForDate(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        eac.setClosingAmount(amountList.get(1));
        eac.setSqlServerUser("");
        eac.setCreatedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        eac.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        eac.setSubhead_id(2); //2 for pettycash
        eac.setSchoolID(schoolId);

        ExpenseHelperClass.getInstance(this).insertAmountClosing(eac);
        finish();

    }

    private void setSchoolInfo(int SchoolID) {
        if (SchoolID != 0) {
            try {
                //setting the school info box
                SchoolExpandableModel mod = DatabaseHelper.getInstance(this).getSchoolInfo(schoolId);
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