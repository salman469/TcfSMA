package com.tcf.sma.Activities.Expense;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Expense.TransactionListingAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingSearchModel;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionListing extends DrawerActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, SearchView.OnQueryTextListener {

    View view;
    private ArrayList<TransactionListingModel> transactionsList;
    private RecyclerView rv_transactionlisting;
    private TransactionListingAdapter transactionListingAdapter;
    private LinearLayout ll_expensehead_sp, ll_startdate, ll_enddate, rg_head;
    private CheckBox cb_pettycash, cb_advance, cb_salary;

    private CheckBox cb_transfer, cb_expense, cb_successful, cb_pending, cb_rejected;
    private CheckBox checkbox_bank, checkbox_cash;
    private RadioGroup  rg_sub_head_salary;
    private TextView tv_heading, tv_start_date, tv_end_date, tv_total_transactions, tv_areaName, tv_regionName;
    private SearchView sv_jvno;
    private List<String> expenseheads;
    private List<SchoolModel> schoolModels;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private Spinner spinner_SelectSchool, sp_expensehead;
    private ArrayAdapter<String> ExpenseAdapter;
    private int schoolId = 0;
    private DatePickerDialog mDatePickerDialog;
    private String strDate, _pickedDate;
    private Calendar calendar;
    TransactionListingSearchModel listingSearchModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_transaction_listing);
        setToolbar("Transactions", this, false);
        init(view);
        setInitialDate();
        populateSpinners();
        PopulateDataRecyclerview();
    }

    private void init(View view) {
        tv_areaName = view.findViewById(R.id.tv_areaName);
        tv_regionName = view.findViewById(R.id.tv_regionName);
        sv_jvno = view.findViewById(R.id.sv_jvno);
        tv_total_transactions = view.findViewById(R.id.tv_total_transactions);
        rg_head = view.findViewById(R.id.rg_head);
        listingSearchModel = new TransactionListingSearchModel();
        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
        sp_expensehead = view.findViewById(R.id.sp_expensehead);
        sp_expensehead.setOnItemSelectedListener(this);
        tv_heading = view.findViewById(R.id.tv_heading);
        rv_transactionlisting = view.findViewById(R.id.rv_transactionlisting);
        ll_expensehead_sp = view.findViewById(R.id.ll_expensehead_sp);
        ll_startdate = view.findViewById(R.id.ll_startdate);
        tv_start_date = view.findViewById(R.id.tv_start_date);
        ll_enddate = view.findViewById(R.id.ll_enddate);
        tv_end_date = view.findViewById(R.id.tv_end_date);
        checkbox_bank = view.findViewById(R.id.checkbox_bank);
        checkbox_cash = view.findViewById(R.id.checkbox_cash);
        cb_advance = view.findViewById(R.id.checkbox_advance);
        cb_salary = view.findViewById(R.id.checkbox_salary);
        cb_pending = view.findViewById(R.id.checkbox_pending);
        cb_rejected = view.findViewById(R.id.checkbox_rejected);
        cb_successful = view.findViewById(R.id.checkbox_successful);
        cb_transfer = view.findViewById(R.id.checkbox_transfer);
        cb_expense = view.findViewById(R.id.checkbox_expense);
        cb_pettycash = view.findViewById(R.id.checkbox_pettycash);


        checkbox_bank.setOnCheckedChangeListener(this);
        checkbox_cash.setOnCheckedChangeListener(this);
        cb_advance.setOnCheckedChangeListener(this);
        cb_salary.setOnCheckedChangeListener(this);
        cb_pending.setOnCheckedChangeListener(this);
        cb_rejected.setOnCheckedChangeListener(this);
        cb_successful.setOnCheckedChangeListener(this);
        cb_transfer.setOnCheckedChangeListener(this);
        cb_expense.setOnCheckedChangeListener(this);
        cb_pettycash.setOnCheckedChangeListener(this);

        ll_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datepicker(R.id.tv_start_date);
            }
        });

        ll_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv_start_date.getText().toString().equals("")) {
                    Datepicker(R.id.tv_end_date);
                } else {
                    ShowToast("Please select start date.");
                }
            }
        });

        sv_jvno.setOnQueryTextListener(this);
    }

    private void PopulateDataRecyclerview() {

        listingSearchModel.setSchoolId(schoolId);
        transactionsList = ExpenseHelperClass.getInstance(this).getFilteredTransactions(listingSearchModel, false);
        rv_transactionlisting.setNestedScrollingEnabled(false);
        rv_transactionlisting.setHasFixedSize(true);
        rv_transactionlisting.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        transactionListingAdapter = new TransactionListingAdapter(this, transactionsList);
        rv_transactionlisting.setAdapter(transactionListingAdapter);
        tv_total_transactions.setText(String.valueOf(transactionsList.size()));


        if(!sv_jvno.getQuery().toString().isEmpty())
        transactionListingAdapter.getFilter().filter(sv_jvno.getQuery().toString());
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
                    schoolModels.get(indexOfSelectedSchool).getName().equals(getResources().getString(R.string.select_school))) {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool + 1);
            } else {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool);
            }
        }

        if (DatabaseHelper.getInstance(TransactionListing.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V ||
                DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM) {
            spinner_SelectSchool.setEnabled(false);
        }

        expenseheads = new ArrayList<>();
        expenseheads.add(0, "Select Expense head");
        expenseheads.add(1, "School Party");
        expenseheads.add(2, "Plumber Work");
        expenseheads.add(3, "Gas Bill");
        expenseheads.add(4, "Electricity Bill");
        expenseheads.add(5, "Water Bill");

        ExpenseAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout_black, expenseheads);
        ExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_expensehead.setAdapter(ExpenseAdapter);
        sp_expensehead.setSelection(0);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
            setSchoolInfo(schoolId);
            PopulateDataRecyclerview();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void Datepicker(int id) {


        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            strDate = format.format(calendar.getTime());
            if (id == R.id.tv_start_date){
                tv_start_date.setText(strDate);
                listingSearchModel.setStartDate(AppModel.getInstance().convertDatetoFormat(tv_start_date.getText().toString(), "dd-MMM-yyyy","yyyy-MM-dd'T'hh:mm:ss"));

            }

            if (id == R.id.tv_end_date){
                tv_end_date.setText(strDate);
                listingSearchModel.setEndDate(AppModel.getInstance().convertDatetoFormat(tv_end_date.getText().toString(), "dd-MMM-yyyy","yyyy-MM-dd'T'hh:mm:ss"));

            }



            PopulateDataRecyclerview();

            String _year = String.valueOf(year);
            String _month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            _pickedDate = _date + "-" + _month + "-" + _year;

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        if (id == R.id.tv_start_date) {
            //mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 15*24*60*60*1000);
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        if (id == R.id.tv_end_date) {
            mDatePickerDialog.getDatePicker().setMinDate(milliseconds(tv_start_date.getText().toString()));
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        mDatePickerDialog.show();
    }

    private long milliseconds(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    private void setInitialDate() {
        tv_start_date.setText(setDate(-7));
        tv_end_date.setText(setDate(0));

        listingSearchModel.setStartDate(AppModel.getInstance().convertDatetoFormat(tv_start_date.getText().toString(), "dd-MMM-yyyy","yyyy-MM-dd'T'hh:mm:ss"));
        listingSearchModel.setEndDate(AppModel.getInstance().convertDatetoFormat(tv_end_date.getText().toString(), "dd-MMM-yyyy","yyyy-MM-dd'T'hh:mm:ss"));
    }

    private String setDate(int states) {
        calendar = Calendar.getInstance();
        Date c = Calendar.getInstance().getTime();
        calendar.setTime(c);
        calendar.add(Calendar.DAY_OF_MONTH, states);
        c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox_expense:
                if(isChecked){
                    rg_head.setVisibility(View.VISIBLE);
                } else {
                    rg_head.setVisibility(View.GONE);
                    listingSearchModel.setSubHead("");
                }
                cb_advance.setChecked(false);
                cb_salary.setChecked(false);
                break;

            case R.id.checkbox_transfer:
                if(!isChecked)
                    listingSearchModel.setSubheadId(0);
                break;
        }


        if((cb_pending.isChecked() && cb_rejected.isChecked() && cb_successful.isChecked()))
            listingSearchModel.setStatus("all");
        else {
            String status = "";
            if(cb_successful.isChecked())
                status += "active";
            if(cb_rejected.isChecked())
                status += "rejected";
            if(cb_pending.isChecked())
                status += "pending";

            listingSearchModel.setStatus(status);
        }

        if((checkbox_bank.isChecked() && checkbox_cash.isChecked())) {
            listingSearchModel.setBucket("all");
            cb_salary.setEnabled(true);
        }
        else {
            Boolean selected = false;
            String bucket = "";
            if(checkbox_cash.isChecked()){
                selected = true;
                bucket += "cash";
                cb_salary.setChecked(false);
                cb_salary.setEnabled(false);
            }
            if(checkbox_bank.isChecked()){
                bucket += "bank";
                selected = true;
                cb_salary.setEnabled(true);
            }
            if(!selected)
                cb_salary.setEnabled(true);
            listingSearchModel.setBucket(bucket);
        }

        if(cb_transfer.isChecked() && cb_expense.isChecked()) {
            listingSearchModel.setSubheadId(1);
            if((cb_pettycash.isChecked() && cb_advance.isChecked() && cb_salary.isChecked()) || (!cb_pettycash.isChecked() && !cb_advance.isChecked() && !cb_salary.isChecked()))
                listingSearchModel.setSubHead("all");
            else {
                String subhead = "";
                if(cb_pettycash.isChecked())
                    subhead += "pettycash";
                if(cb_advance.isChecked())
                    subhead += "advance";
                if(cb_salary.isChecked())
                    subhead += "salary";

                listingSearchModel.setSubHead(subhead);
            }
        }
        else {
            if(cb_transfer.isChecked()){
                listingSearchModel.setSubheadId(1);
            } else {
                listingSearchModel.setSubheadId(0);
            }

            if(cb_expense.isChecked()){
                if((cb_pettycash.isChecked() && cb_advance.isChecked() && cb_salary.isChecked()) || (!cb_pettycash.isChecked() && !cb_advance.isChecked() && !cb_salary.isChecked()))
                    listingSearchModel.setSubHead("all");
                else {
                    String subhead = "";
                    if(cb_pettycash.isChecked())
                        subhead += "pettycash";
                    if(cb_advance.isChecked())
                        subhead += "advance";
                    if(cb_salary.isChecked())
                        subhead += "salary";

                    listingSearchModel.setSubHead(subhead);
                }
            }
        }

        PopulateDataRecyclerview();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if ( TextUtils.isEmpty ( newText ) ) {
            transactionListingAdapter.getFilter().filter("");
        } else {
            transactionListingAdapter.getFilter().filter(newText.toString());
        }
        return true;
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
