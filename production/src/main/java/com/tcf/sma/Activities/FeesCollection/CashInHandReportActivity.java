package com.tcf.sma.Activities.FeesCollection;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.DepositHistoryAdapter;
import com.tcf.sma.Helpers.DbTables.FeesCollection.CashDeposit;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandReportModel;
import com.tcf.sma.R;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CashInHandReportActivity extends DrawerActivity implements View.OnClickListener{

    private View view;
    private TextView tv_Op_cashInHand, tv_CashReceived, tv_CashDeposit, tv_Cl_cashInHand,etToDate, etFromDate;
    private ImageView iv_search;

    private String fromDate, toDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cash_in_hand_report);

        view = setActivityLayout(this, R.layout.activity_cash_in_hand_report);
        setToolbar("Cash in Hand Report", this, false);

        init();
        populateByDefaultValues();
    }

    private void init(){
        tv_Op_cashInHand = view.findViewById(R.id.tv_Op_cashInHand);
        tv_CashReceived = view.findViewById(R.id.tv_CashReceived);
        tv_CashDeposit = view.findViewById(R.id.tv_CashDeposit);
        tv_Cl_cashInHand = view.findViewById(R.id.tv_Cl_cashInHand);
        etToDate = view.findViewById(R.id.etToDate);
        etFromDate = view.findViewById(R.id.etFromDate);
        iv_search = view.findViewById(R.id.iv_search);

        tv_CashReceived.setPaintFlags(tv_CashReceived.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_CashDeposit.setPaintFlags(tv_CashDeposit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        etToDate.setOnClickListener(this);
        etFromDate.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        tv_CashReceived.setOnClickListener(this);
        tv_CashDeposit.setOnClickListener(this);
    }

    private void populateByDefaultValues(){
        etFromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
        etToDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));

        fromDate = etFromDate.getText().toString().trim();
        toDate = etToDate.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etToDate:
                AppModel.getInstance().DatePicker(etToDate, CashInHandReportActivity.this);
                toDate = etToDate.getText().toString().trim();
                break;
            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(etFromDate, CashInHandReportActivity.this);
                fromDate = etFromDate.getText().toString().trim();
                break;
            case R.id.iv_search:
                getCashInHandReportData();
                break;
            case R.id.tv_CashReceived:
                Intent receiptBookIntent = new Intent(this,ReceiptBook.class);
                receiptBookIntent.putExtra("fromDate",fromDate);
                receiptBookIntent.putExtra("toDate",toDate);
                startActivity(receiptBookIntent);
                break;
            case R.id.tv_CashDeposit:
                Intent depositBookIntent = new Intent(this, DepositHistoryReport.class);
                depositBookIntent.putExtra("fromDate", fromDate);
                depositBookIntent.putExtra("toDate", toDate);
                startActivity(depositBookIntent);
                break;
        }
    }

    private void getCashInHandReportData() {
        fromDate = AppModel.getInstance().convertDatetoFormat(etFromDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        toDate = AppModel.getInstance().convertDatetoFormat(etToDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");

        AppModel.getInstance().showLoader(CashInHandReportActivity.this);
        new Thread(() -> {


            CashInHandReportModel cashInHandReportModel = CashDeposit.getInstance(CashInHandReportActivity.this).getCashInHandReport(fromDate, toDate);
            try {
                CashInHandReportActivity.this.runOnUiThread(() -> {
                    AppModel.getInstance().hideLoader();
                    if (cashInHandReportModel != null){
                        tv_Op_cashInHand.setText("" + Math.round(cashInHandReportModel.getOpeningCashInHand()));
                        tv_CashReceived.setText("" + Math.round(cashInHandReportModel.getCashReceived()));
                        tv_CashDeposit.setText("" + Math.round(cashInHandReportModel.getCashDeposit()));
                        tv_Cl_cashInHand.setText("" + Math.round(cashInHandReportModel.getClosingCashInHand()));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}