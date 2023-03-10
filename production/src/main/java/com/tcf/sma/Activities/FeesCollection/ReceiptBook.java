package com.tcf.sma.Activities.FeesCollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.ReceiptListAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.ReceiptListModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.List;

public class ReceiptBook extends DrawerActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public Spinner SchoolSpinner;
    String fromDate, toDate = "", receiptNo = "", depositSlipNo = "";
    ArrayAdapter<SchoolModel> SchoolAdapter;
    private RecyclerView receiptsList;
    private View screen;
    private RadioGroup rg_deposited;
    private ReceiptListAdapter adapter;
    private EditText et_receiptNo, et_depositSlipNo;
    private Button btn_search;
    private TextView tv_toDate, tv_fromDate;
    private LinearLayout llResults;
    private int SchoolSpinnerValue = 0;
    private List<SchoolModel> schoolModels;
    private boolean isDeposited = true;
    private String deposited = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_cash_deposit_book);
        setToolbar("Receipt Book", this, false);
        init();
        getIntentExtra();
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("fromDate") && intent.hasExtra("toDate")){
                fromDate = AppModel.getInstance().convertDatetoFormat(intent.getStringExtra("fromDate"), "yyyy-MM-dd", "dd-MMM-yy");
                toDate = AppModel.getInstance().convertDatetoFormat(intent.getStringExtra("toDate"), "yyyy-MM-dd", "dd-MMM-yy");

                tv_fromDate.setText(fromDate);
                tv_toDate.setText(toDate);


                btn_search.performClick();

            }
        }
    }

    public void init() {
        receiptsList = screen.findViewById(R.id.rv_receipts);
        receiptsList.setNestedScrollingEnabled(false);
        receiptsList.setHasFixedSize(true);
        receiptsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        SchoolSpinner = screen.findViewById(R.id.spn_school);
        btn_search = screen.findViewById(R.id.btn_search);
        et_receiptNo = screen.findViewById(R.id.et_receiptNo);
        et_depositSlipNo = screen.findViewById(R.id.et_depositSlipNo);
        tv_fromDate = screen.findViewById(R.id.etFromDate);
        tv_toDate = screen.findViewById(R.id.etToDate);
        rg_deposited = screen.findViewById(R.id.rg_deposited);
        rg_deposited.setOnCheckedChangeListener(this);
        tv_fromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
        tv_toDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
        llResults = screen.findViewById(R.id.llResults);

        btn_search.setOnClickListener(this);
        tv_fromDate.setOnClickListener(this);
        tv_toDate.setOnClickListener(this);

        fromDate = tv_fromDate.getText().toString().trim();
        toDate = tv_toDate.getText().toString().trim();

        populateSchoolSpinnser();

        SchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(ReceiptBook.this,
                        SchoolSpinnerValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_receiptNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                btn_search.performClick();
                AppModel.getInstance().hideSoftKeyboard(ReceiptBook.this);
                return true;
            }
            return false;
        });

        et_depositSlipNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                btn_search.performClick();
                AppModel.getInstance().hideSoftKeyboard(ReceiptBook.this);
                return true;
            }
            return false;
        });
//        setAdapter();
        setCollapsing(false);
    }

    private void populateSchoolSpinnser() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();

//        SchoolSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                schoolModels));
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);

        if (schoolModels != null && schoolModels.size() > 1) {
            schoolModels.add(new SchoolModel(0, "All"));
            SchoolSpinnerValue = 0;

            SchoolSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModels));
            int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, SchoolSpinnerValue);
            SchoolSpinner.setSelection(selectedSchoolIndex);

        } else if (schoolModels != null && schoolModels.size() > 0) {
            SchoolSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModels));
            if (indexOfSelectedSchool > -1)
                SchoolSpinner.setSelection(indexOfSelectedSchool);
        }

    }

    public int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
        int index = 0;
        for (SchoolModel model : schoolModels) {
            if (model.getId() == appSchoolId)
                return index;
            index++;
        }
        return index;
    }

    private void setAdapter() {
        List<ReceiptListModel> receiptList = getList();
        if (receiptList.size() > 0) {
            setCollapsing(true);
            adapter = new ReceiptListAdapter(receiptList);
            receiptsList.setAdapter(adapter);
            llResults.setVisibility(View.VISIBLE);
        } else {
            MessageBox("No results found");
            clearList();
        }
    }

    private void clearList() {
        if (adapter != null) {
            List<ReceiptListModel> receiptList = null;
            adapter = new ReceiptListAdapter(receiptList);
            receiptsList.setAdapter(adapter);
            llResults.setVisibility(View.GONE);
        }
        setCollapsing(false);
    }

    public List<ReceiptListModel> getList() {
        fromDate = AppModel.getInstance().convertDatetoFormat(tv_fromDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        toDate = AppModel.getInstance().convertDatetoFormat(tv_toDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        receiptNo = et_receiptNo.getText().toString();
        depositSlipNo = et_depositSlipNo.getText().toString();

        return FeesCollection.getInstance(this).getPreviousReceipts(SchoolSpinnerValue, fromDate, toDate,
                receiptNo, deposited, depositSlipNo);
//        return FeesCollection.getInstance(this).getPreviousReceipts(SurveyAppModel.getInstance().getAllUserSchoolsForFinance(this),fromDate,toDate,
//                receiptNo);
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                setAdapter();
                break;
            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(tv_fromDate, ReceiptBook.this);
                fromDate = tv_fromDate.getText().toString().trim();
                break;
            case R.id.etToDate:
                AppModel.getInstance().DatePicker(tv_toDate, ReceiptBook.this);
                toDate = tv_toDate.getText().toString().trim();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_all) {
            deposited = "all";
        } else if (checkedId == R.id.rb_deposited) {
            deposited = "deposited";
        } else if (checkedId == R.id.rb_notDeposited) {
            deposited = "notdeposited";
        }
    }
}