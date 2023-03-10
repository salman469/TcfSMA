package com.tcf.sma.Activities.Expense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PettyCashCorrection extends DrawerActivity {


    View view;
    private AppCompatTextView tv_expensehead, tv_jvno, tv_schoolname, tv_head, tv_amount, tv_subhead,
            tv_transactiontype, tv_bucket, tv_inout, tv_allowedfrom,tv_checkno, tv_total_amount;
    private AppCompatEditText et_corrected_amount;
    private LinearLayout llcheckno, ll_checkimages_heading, ll_receiptimages_heading;
    private RecyclerView rv_check_images, rv_receipt_images;
    private ExpenseAttachmentsAdapter expenseAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    int id;
    ExpenseTransactionModel etm;
    private double valueCashinhand = 0, valueCashinbank = 0, valueSalary = 0;
    private Button btn_correction_submit;
    String allowedfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_petty_cash_correction);
        setToolbar("Petty Cash Correction", this, false);
        init();
        getBundle();
        setValues();
        btn_correction_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Objects.requireNonNull(et_corrected_amount.getText()).toString().equals("")){
                    if(Validations()){
                        SubmitCorrectTransaction();
                    }
                }
            }
        });
    }

    private void init() {

        tv_total_amount = view.findViewById(R.id.tv_total_amount);
        et_corrected_amount = view.findViewById(R.id.et_corrected_amount);
        tv_expensehead = view.findViewById(R.id.tv_expensehead);
        tv_jvno = view.findViewById(R.id.tv_jvno);
        tv_schoolname = view.findViewById(R.id.tv_schoolname);
        tv_head = view.findViewById(R.id.tv_head);
        tv_amount = view.findViewById(R.id.tv_amount);
        tv_subhead = view.findViewById(R.id.tv_subhead);
        tv_transactiontype = view.findViewById(R.id.tv_transactiontype);
        tv_bucket = view.findViewById(R.id.tv_bucket);
        tv_inout = view.findViewById(R.id.tv_inout);
        tv_allowedfrom = view.findViewById(R.id.tv_allowedfrom);
        tv_checkno = view.findViewById(R.id.tv_checkno);

        llcheckno = view.findViewById(R.id.llcheckno);
        ll_checkimages_heading = view.findViewById(R.id.ll_checkimages_heading);
        ll_receiptimages_heading = view.findViewById(R.id.ll_receiptimages_heading);

        rv_receipt_images = view.findViewById(R.id.rv_receipt_images);
        rv_check_images = view.findViewById(R.id.rv_check_images);

        btn_correction_submit = view.findViewById(R.id.btn_correct_pettycash);
    }

    @SuppressLint("DefaultLocale")
    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tv_expensehead.setText(bundle.getString("eh"));
            id = bundle.getInt("id");
            etm = ExpenseHelperClass.getInstance(this).getSingleTransaction(id);

            tv_jvno.setText(String.valueOf(etm.getJvNo()));

            String school_name;
            school_name = DatabaseHelper.getInstance(this).getSchoolById(etm.getSchoolID()).getName();
            tv_schoolname.setText(school_name);

            tv_head.setText(bundle.getString("Head") != null && !bundle.getString("Head").equals("") ? bundle.getString("Head") : "");
            tv_subhead.setText(bundle.getString("SubHead") != null && !bundle.getString("SubHead").equals("") ? bundle.getString("SubHead") : "");
            tv_transactiontype.setText(bundle.getString("TransactionType") != null && !bundle.getString("TransactionType").equals("") ? bundle.getString("TransactionType") : "");

            allowedfrom = bundle.getString("Bucket") != null && !bundle.getString("Bucket").equals("") ? bundle.getString("Bucket") : "";
            tv_bucket.setText(allowedfrom);

            if(!allowedfrom.isEmpty()){
                if(allowedfrom.equalsIgnoreCase("Bank")) {
                    tv_allowedfrom.setText(getResources().getText(R.string.cheque_tag));
                    tv_checkno.setText(etm.getChequeNo());
                } else if(allowedfrom.equalsIgnoreCase("Cash")){
                    tv_allowedfrom.setText(getResources().getText(R.string.receipt_tag));
                    tv_checkno.setText(String.format("%d", etm.getReceiptNo()));
                }
            }

            tv_inout.setText(bundle.getString("IO") != null && !bundle.getString("IO").equals("") ? bundle.getString("IO") : "");
            tv_amount.setText(etm.getTransAmount() >= 0 ? AppModel.getInstance().formatNumberInCommas((long) etm.getTransAmount()) : "");
            tv_allowedfrom.setText(String.valueOf(etm.getChequeNo()));

            ExpenseAttachmentsAdapter.paths.clear();
            ExpenseAttachmentsAdapter.paths = ExpenseHelperClass.getInstance(this).getExpenseTransactionImages(etm.getID());

            if(ExpenseAttachmentsAdapter.paths.size()>0){
                ll_checkimages_heading.setVisibility(View.VISIBLE);
            }

            for(int i=0;i<ExpenseAttachmentsAdapter.paths.size();i++){
                attachments.add(ExpenseAttachmentsAdapter.paths.get(i).getSlip_path());
            }

            if (bundle.getString("Bucket") != null && !bundle.getString("Bucket").equals("")) {
                if (bundle.getString("Bucket").equalsIgnoreCase("bank")) {
                    ll_receiptimages_heading.setVisibility(View.GONE);
                    rv_receipt_images.setVisibility(View.GONE);


                    expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(true, this, attachments);
                    rv_check_images.setNestedScrollingEnabled(true);
                    rv_check_images.setHasFixedSize(true);
                    rv_check_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                    rv_check_images.setAdapter(expenseAttachmentsAdapter);

                } else if (bundle.getString("Bucket").equalsIgnoreCase("cash")) {
                    llcheckno.setVisibility(View.GONE);
                    rv_check_images.setVisibility(View.GONE);

                    rv_receipt_images.setNestedScrollingEnabled(false);
                    rv_receipt_images.setHasFixedSize(true);
                    rv_receipt_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                    expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(true, this, attachments);
                    rv_receipt_images.setAdapter(expenseAttachmentsAdapter);
                }
            }

        }
    }

    private void SubmitCorrectTransaction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Correction");
        builder.setMessage("Are you sure you want to submit correction?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ExpenseTransactionModel transactionModel = new ExpenseTransactionModel();
                transactionModel.setForDate(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                transactionModel.setTransAmount(etm.getTransAmount());
                transactionModel.setJvNo(etm.getJvNo());
                transactionModel.setChequeNo(etm.getChequeNo());
                transactionModel.setReceiptNo(etm.getReceiptNo());
                transactionModel.setSqlserverUser("");
                transactionModel.setCreatedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                transactionModel.setCreatedOnServer("");
                transactionModel.setModifiedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                transactionModel.setModifiedOnServer("");
                transactionModel.setCreatedBy(DatabaseHelper.getInstance(getApplicationContext()).getCurrentLoggedInUser().getId());
                transactionModel.setModifiedBy(DatabaseHelper.getInstance(getApplicationContext()).getCurrentLoggedInUser().getId());
                transactionModel.setCreatedFrom("A");
                transactionModel.setModifiedFrom("A");
                transactionModel.setUploadedOn((String) null);
                transactionModel.setSchoolID(etm.getSchoolID());
                transactionModel.setAcademicSessionID(etm.getAcademicSessionID());

                transactionModel.setSubHeadID(etm.getSubHeadID());
                transactionModel.setBucketID(etm.getBucketID());
                transactionModel.setCategoryID(AppConstants.TRANSACTION_CATEGORY_CORRECTION_EXPENSE);
                transactionModel.setFlowID(AppConstants.FLOW_IN_ID_EXPENSE);//for In


//                double SpentAmountPettyCashLimitsMOnthly = 0, SpentAmountSubheadLimitsMonthly = 0;


                double difference =  Double.parseDouble(Objects.requireNonNull(et_corrected_amount.getText()).toString().trim()) - etm.getTransAmount();

                //first subtract
//                SpentAmountPettyCashLimitsMOnthly = ExpenseHelperClass.getInstance(getApplicationContext()).getAvailableSpentAmountPettyCashLimitsMOnthly(etm.getSchoolID());
//                SpentAmountPettyCashLimitsMOnthly = ExpenseHelperClass.getInstance(getApplicationContext()).getSpentAmountofHeadfromTransaction(etm.getSchoolID(),2);
//                SpentAmountPettyCashLimitsMOnthly = SpentAmountPettyCashLimitsMOnthly - etm.getTransAmount();
//                SpentAmountPettyCashLimitsMOnthly += difference;
                //ExpenseHelperClass.getInstance(getApplicationContext()).updateSchoolPettyCashMonthlyLimits(getApplicationContext(), etm.getSchoolID(), SpentAmountPettyCashLimitsMOnthly);

//                SpentAmountSubheadLimitsMonthly = ExpenseHelperClass.getInstance(getApplicationContext()).getAvailableSpentAmountSubheadLimitsMOnthly(etm.getSchoolID(),etm.getSubHeadID(),allowedfrom);
//                SpentAmountSubheadLimitsMonthly = SpentAmountSubheadLimitsMonthly - etm.getTransAmount();
//                SpentAmountSubheadLimitsMonthly += difference;
                //ExpenseHelperClass.getInstance(getApplicationContext()).updateSubheadLimitsMonthly(getApplicationContext(), etm.getSubHeadID(), etm.getSchoolID(), allowedfrom, SpentAmountSubheadLimitsMonthly);

                ExpenseHelperClass.getInstance(getApplicationContext()).insertTransaction(transactionModel);

                //for second
                transactionModel.setTransAmount(Double.parseDouble(Objects.requireNonNull(et_corrected_amount.getText()).toString().trim()));
                transactionModel.setFlowID(2);//for Out

                ExpenseHelperClass.getInstance(getApplicationContext()).insertTransaction(transactionModel);

                AppModel.getInstance().changeMenuPendingSyncCount(getApplicationContext(), true);
                finish();
            }


        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    private boolean Validations() {
        int i = 0;
        double availableAmountPCLM = etm.getTransAmount() + ExpenseHelperClass.getInstance(this).getAvailableAmountPettyCashLimitsMOnthly(etm.getSchoolID());
        double availableAmountSLM = etm.getTransAmount() + ExpenseHelperClass.getInstance(this).getAvailableAmountSubheadLimitsMOnthly(etm.getSchoolID(), etm.getSubHeadID(), allowedfrom);
        if (Double.parseDouble(Objects.requireNonNull(et_corrected_amount.getText()).toString()) > availableAmountPCLM) {
            i++;
            ShowToast("Your amount is greater than petty cash limit");
        } else if (Double.parseDouble(et_corrected_amount.getText().toString()) > availableAmountSLM) {
            i++;
            ShowToast("Your amount is greater than subhead monthly limit");
        }

        if (!Strings.isEmptyOrWhitespace(etm.getChequeNo())) {
            if (Double.parseDouble(et_corrected_amount.getText().toString()) > valueCashinbank - valueSalary) {
                ShowToast("you have insufficient amount in your bank");
                i++;
            }
        }
        if (!Strings.isEmptyOrWhitespace(etm.getReceiptNo())) {
            if (Double.parseDouble(et_corrected_amount.getText().toString()) > valueCashinhand) {
                ShowToast("you have insufficient amount in your cash in hand");
                i++;
            }
        }
        return i == 0;
    }

    private void setValues(){
        //bucket id 2 for cash
        valueCashinhand = ExpenseHelperClass.getInstance(this).getAvailableLimitAmountFromTransaction(etm.getSchoolID(),2);
        //bucket id 1 for bank
        valueCashinbank = ExpenseHelperClass.getInstance(this).getAvailableLimitAmountFromTransaction(etm.getSchoolID(),1);
        valueSalary = ExpenseHelperClass.getInstance(this).getTotalSalary(etm.getSchoolID());
        tv_total_amount.setText(AppModel.getInstance().formatNumberInCommas((long) ExpenseHelperClass.getInstance(this).getAvailableAmountSubheadLimitsMOnthly(etm.getSchoolID(), etm.getSubHeadID(), allowedfrom)));
    }

}
