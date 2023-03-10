package com.tcf.sma.Activities.FeesCollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.DepositHistoryAdapter;
import com.tcf.sma.Helpers.DbTables.FeesCollection.CashDeposit;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.DepositHistoryModel;
import com.tcf.sma.R;

import java.util.List;

public class DepositHistoryReport extends DrawerActivity implements View.OnClickListener {

    String fromDate, toDate = "", depositSlipNo = "";
    Button search;
    View view;
    private TextView tv_toDate, tv_fromDate;
    private RecyclerView rv_searchList;
    private List<DepositHistoryModel> depositHistoryModelList = null;
    private EditText etSlipNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_deposit_history);
        setToolbar(getString(R.string.depositHistoryReport), this, false);

        init(view);
        getIntentExtra();
    }

    private void init(View view) {
        search = (Button) view.findViewById(R.id.btn_search);
        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_depositList);
        tv_fromDate = (TextView) view.findViewById(R.id.etFromDate);
        tv_toDate = (TextView) view.findViewById(R.id.etToDate);
        etSlipNo = (EditText) view.findViewById(R.id.etSlipNo);
        tv_fromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
        tv_toDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));


        tv_fromDate.setOnClickListener(this);
        tv_toDate.setOnClickListener(this);

        fromDate = tv_fromDate.getText().toString().trim();
        toDate = tv_toDate.getText().toString().trim();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_searchList.setLayoutManager(llm);
        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setAdapter(new DepositHistoryAdapter(this, depositHistoryModelList));

        search.setOnClickListener(this);

        etSlipNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search.performClick();
                    AppModel.getInstance().hideSoftKeyboard(DepositHistoryReport.this);
                    return true;
                }
                return false;
            }
        });

    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("fromDate") && intent.hasExtra("toDate")){
                fromDate = AppModel.getInstance().convertDatetoFormat(intent.getStringExtra("fromDate"), "yyyy-MM-dd", "dd-MMM-yy");
                toDate = AppModel.getInstance().convertDatetoFormat(intent.getStringExtra("toDate"), "yyyy-MM-dd", "dd-MMM-yy");

                tv_fromDate.setText(fromDate);
                tv_toDate.setText(toDate);


                search.performClick();

            }
        }
    }

    private void getReportData() {


        //to do
        //add a total row in deposit history report.....

        fromDate = AppModel.getInstance().convertDatetoFormat(tv_fromDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        toDate = AppModel.getInstance().convertDatetoFormat(tv_toDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        depositSlipNo = etSlipNo.getText().toString();

        AppModel.getInstance().showLoader(DepositHistoryReport.this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                depositHistoryModelList = CashDeposit.getInstance(DepositHistoryReport.this)
                        .getDepositHistory(AppModel.getInstance().getAllUserSchoolsForFinance(DepositHistoryReport.this), fromDate, toDate,
                                depositSlipNo);

                try {

                    DepositHistoryReport.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv_searchList.setAdapter(new DepositHistoryAdapter(DepositHistoryReport.this, depositHistoryModelList));
                            AppModel.getInstance().hideLoader();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                getReportData();
                break;

            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(tv_fromDate, DepositHistoryReport.this);
                fromDate = tv_fromDate.getText().toString().trim();
                break;
            case R.id.etToDate:
                AppModel.getInstance().DatePicker(tv_toDate, DepositHistoryReport.this);
                toDate = tv_toDate.getText().toString().trim();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}