package com.tcf.sma.Activities.FeesCollection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.DepositDetailAdapter;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.CashDepositModel;
import com.tcf.sma.Models.Fees_Collection.PreviousReceivableModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class CashDepositDetail extends DrawerActivity {
    RecyclerView rv_fees;
    List<ViewReceivablesModels> viewReceivablesModels = new ArrayList<>();
    List<ViewReceivablesModels> temp = new ArrayList<>();
    EditText slipNo, et_remarks;
    View screen;
    LinearLayout ll_content;
    Button bt_ok;
    ImageView search;

    String slip_no;
    int depositId, schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_cash_deposit_detail);
        setToolbar("Receive Cash", this, false);
        init();
    }

    private void getIntentData() {
        Intent i = getIntent();
        if (i.hasExtra("slip_no")) {
            slip_no = i.getStringExtra("slip_no");
            slipNo.setText(slip_no);
            slipNo.setSelection(slip_no.length());
            setToolbar("Deposit Slip", this, false);
        }
        if (i.hasExtra("depositId"))
            depositId = i.getIntExtra("depositId", 0);

        if (i.hasExtra("schoolId"))
            schoolId = i.getIntExtra("schoolId", 0);
    }

    void init() {
        ll_content = (LinearLayout) screen.findViewById(R.id.ll_content);
        slipNo = screen.findViewById(R.id.etSlipNo);
        et_remarks = screen.findViewById(R.id.et_remarks);
        rv_fees = (RecyclerView) screen.findViewById(R.id.rv_fees);
        rv_fees.setLayoutManager(new LinearLayoutManager(this));
        rv_fees.setNestedScrollingEnabled(false);
        search = screen.findViewById(R.id.iv_search);

        slipNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(slip_no))
                    ll_content.setVisibility(View.GONE);
                else ll_content.setVisibility(View.VISIBLE);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!slipNo.getText().toString().isEmpty()) {
                    if (!slip_no.equalsIgnoreCase(slipNo.getText().toString())) {
                        slip_no = slipNo.getText().toString();
                        populateReceivableList();
                    } else
                        Toast.makeText(CashDepositDetail.this, "Already showing data for the provided receipt no.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(CashDepositDetail.this, "Please enter a valid receipt no", Toast.LENGTH_SHORT).show();
            }
        });
        bt_ok = screen.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getIntentData();
        populateReceivableList();
    }

    private void populateReceivableList() {
        viewReceivablesModels = new ArrayList<>();
        int prevRecv = 0;


        List<FeeTypeModel> feeTypeModelList = FeesCollection.getInstance(this).getFeeTypes();
        List<PreviousReceivableModel> prevRecvs = FeesCollection.getInstance(this).getCashDepositRecord(String.valueOf(slip_no), AppModel.getInstance().getAllUserSchoolsForFinance(this));

        CashDepositModel cdm = FeesCollection.getInstance(this).getCashDepositRecordBy(String.valueOf(slip_no), schoolId + "");
        if (cdm != null) {
            et_remarks.setText(cdm.getRemarks());
        }

        for (int i = 0; i < feeTypeModelList.size(); i++) {

            if (i < 7) {

                for (int j = 0; j < prevRecvs.size(); j++) {
                    prevRecv = 0;
                    if (prevRecvs.get(j).getFeeType_id() == feeTypeModelList.get(i).getId()) {
                        if (prevRecvs.get(j).getTotalAmount() != null && prevRecvs.get(j).getTotalAmount() != "")
                            prevRecv = Integer.valueOf(prevRecvs.get(j).getTotalAmount());
                        else
                            prevRecv = 0;
                        break;
                    }
                }

                ViewReceivablesModels rm = new ViewReceivablesModels();
                rm.setBalance(prevRecv + "");
                rm.setPreviouslyReceived(prevRecv + "");
                rm.setAmountReceived("");
                rm.setTodaySales("");
                rm.setTitle("Fees Collection");

                viewReceivablesModels.add(rm);

            }
        }

        for (int i = 0; i < feeTypeModelList.size(); i++) {
            if (i < 7) {
                viewReceivablesModels.get(i).setTitle(feeTypeModelList.get(i).getName());
                viewReceivablesModels.get(i).setFeeTypeId(feeTypeModelList.get(i).getId());

                if (i < 3) {
                    viewReceivablesModels.get(i).setSalesDisabled(true);
                }
            }
        }
        rv_fees.setAdapter(new DepositDetailAdapter(this, viewReceivablesModels, schoolId));
        ll_content.setVisibility(View.VISIBLE);
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
