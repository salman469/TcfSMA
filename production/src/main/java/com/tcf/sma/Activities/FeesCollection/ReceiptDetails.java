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
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.FeesCollection.CashReceived.CashCorrectionActivity;
import com.tcf.sma.Adapters.FeesCollection.ReceiptDetailAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.PreviousReceivableModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.FinanceCheckSum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDetails extends DrawerActivity {
    RecyclerView rv_fees;
    List<ViewReceivablesModels> viewReceivablesModels = new ArrayList<>();
    List<ViewReceivablesModels> temp = new ArrayList<>();
    EditText receiptNo;
    View screen;
    LinearLayout ll_content;
    Button bt_ok, btCorrection;
    ImageView search;
    int school_id,receipt_id,student_id,student_gr_no,school_class_id;
    String depositSlipNo;
    String receipt_no;
    TextView tv_name, tv_className, tv_fathersName, tv_studentId, tv_gr;
    boolean FinanceSyncCompleted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_receipt_details);
        setToolbar("Receipt Details", this, false);
        init();
    }

    private void getIntentData() {
        Intent i = getIntent();
        if (i.hasExtra("schoolId")) {
            school_id = i.getIntExtra("schoolId", 0);
        }
        if (i.hasExtra("headerId"))
            receipt_id = i.getIntExtra("headerId", 0);

        if (i.hasExtra("receipt_no")) {
            receipt_no = i.getStringExtra("receipt_no");
            receiptNo.setText(receipt_no);
            receiptNo.setSelection(receipt_no.length());
        }

        if (i.hasExtra("isDeposited")) {
            if (i.getBooleanExtra("isDeposited", false)) {
                btCorrection.setEnabled(false);
                if (i.hasExtra("depositSlipNo")) {
                    depositSlipNo = i.getStringExtra("depositSlipNo");
                    btCorrection.setText("Deposit Slip No : " + depositSlipNo);
                }
            }
        }
        if (i.hasExtra("studentId"))
            student_id = i.getIntExtra("studentId", 0);

        if (i.hasExtra("studentGrNo"))
            student_gr_no = i.getIntExtra("studentGrNo", 0);

        if (i.hasExtra("schoolClassId"))
            school_class_id = i.getIntExtra("schoolClassId", 0);

    }

    void init() {
        ll_content = screen.findViewById(R.id.ll_content);
        receiptNo = screen.findViewById(R.id.etReceiptNo);
        search = screen.findViewById(R.id.iv_search);
        rv_fees = screen.findViewById(R.id.rv_fees);
        rv_fees.setLayoutManager(new LinearLayoutManager(this));
        rv_fees.setNestedScrollingEnabled(false);

        tv_studentId = screen.findViewById(R.id.tv_student_id);
        tv_gr = screen.findViewById(R.id.tv_gr);
        tv_name = (TextView) screen.findViewById(R.id.tv_name);
        tv_className = (TextView) screen.findViewById(R.id.tv_className);
        tv_fathersName = (TextView) screen.findViewById(R.id.tv_fathersName);

        FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(this,
                AppConstants.FinanceSyncCompleted, false);

        btCorrection = screen.findViewById(R.id.bt_correction);
        btCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FinanceSyncCompleted) {
                    if (FinanceCheckSum.Instance(new WeakReference<>(ReceiptDetails.this)).isChecksumSuccess(ReceiptDetails.this, true)) {
                        AppModel.getInstance().setSpinnerSelectedSchool(ReceiptDetails.this,
                                school_id);
                        Intent intent = new Intent(ReceiptDetails.this, CashCorrectionActivity.class);
                        intent.putExtra("receipt_no", receipt_no);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(ReceiptDetails.this, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        receiptNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(receipt_no))
                    ll_content.setVisibility(View.GONE);
                else ll_content.setVisibility(View.VISIBLE);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!receiptNo.getText().toString().isEmpty()) {
                    if (!receipt_no.equalsIgnoreCase(receiptNo.getText().toString())) {
                        receipt_no = receiptNo.getText().toString();
                        populateReceivableList();
                    } else
                        Toast.makeText(ReceiptDetails.this, "Already showing data for the provided receipt no.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ReceiptDetails.this, "Please enter a valid receipt no", Toast.LENGTH_SHORT).show();
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

    private void clearStudentFields() {
        tv_gr.setText("");
        tv_studentId.setText("");
        tv_name.setText("");
        tv_fathersName.setText("");
        tv_className.setText("");
    }

    private void populateStudentFields(StudentModel sm) {
        tv_studentId.setText(sm.getId() + "");
        tv_gr.setText(sm.getGrNo());
        tv_name.setText(sm.getName());
        tv_fathersName.setText(sm.getFathersName());
        tv_className.setText(sm.getCurrentClass() + " " + sm.getCurrentSection());
    }

    private void populateReceivableList() {
        viewReceivablesModels = new ArrayList<>();

        StudentModel sm = DatabaseHelper.getInstance(ReceiptDetails.this)
                .getStudentWithReceiptNo(!receipt_no.equals("") ? Long.parseLong(receipt_no) : -1, school_id);

        if(student_gr_no == 0){ //Transferred Student
            tv_studentId.setText(student_id + "");
            tv_gr.setText("Transferred ["+student_id+"]");
            tv_name.setText("Transferred ["+student_id+"]");
            tv_fathersName.setText("");
            tv_className.setText(DatabaseHelper.getInstance(ReceiptDetails.this).getSchoolClassBySchoolClassId(school_id,school_class_id).getClass_section_name());

            populateReceiptDetails();

            btCorrection.setVisibility(View.GONE);
        }
        else if (sm.getName() != null && !sm.getName().equals("")) {

            populateStudentFields(sm);

            populateReceiptDetails();
        } else {
            clearStudentFields();
            Toast.makeText(ReceiptDetails.this, "Receipt not found!", Toast.LENGTH_SHORT).show();
            ll_content.setVisibility(View.GONE);
        }
    }

    private void populateReceiptDetails() {
        int prevRecv = 0;
        List<FeeTypeModel> feeTypeModelList = FeesCollection.getInstance(this).getFeeTypes();
        List<PreviousReceivableModel> prevRecvs = FeesCollection.getInstance(this).getPrevReceivableWithReceiptNOForReceiptDetails(String.valueOf(school_id), receipt_no);

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
                rm.setAmountReceived(prevRecv + "");
                int feeType = feeTypeModelList.get(i).getId();
                if (feeType > 3 && feeType < 8)
                    rm.setTodaySales(prevRecv + "");
                else rm.setTodaySales("0");
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
        rv_fees.setAdapter(new ReceiptDetailAdapter(this, viewReceivablesModels));
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
