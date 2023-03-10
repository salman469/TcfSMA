package com.tcf.sma.Activities.Expense;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;
import com.tcf.sma.Models.RetrofitModels.Expense.SlipModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    private ImageView iv_delete_btoc, iv_delete_ctob;
    private AppCompatImageView ivcheckimage_btoc, ivcheckimage_ctob;
    private RelativeLayout rl_checkimage_btoc, rl_checkimage_ctob, rl_addcheckimage_btoc, rl_addcheckimage_ctob, inner_relativelayout1, inner_relativelayout2;
    private Button btn_submit;
    private AppCompatEditText et_jvno_btoc, et_jvno_ctob, et_checkno_btoc, et_depositslipno_ctob, et_amount_btoc, et_amount_ctob, et_comments_btoc, et_comments_ctob;
    private Boolean ischeckimage = false;
    private TextView tv_tocategory, txt_currentdate_btoc, txt_currentdate_ctob, tv_cashinbank, tv_cashinhand;

    private double valueCashinhand = 0, valueCashinbank = 0, valueSalary = 0;
    public static Spinner sp_category;
    List<String> category;
    ArrayAdapter<String> categoryAdapter;
    int schoolId = 0;
    private String image_Path = null;
    private Calendar calendar;
    private int academicSession_id;
    public List<SlipModel> paths;
    public SlipModel slipModel;
    private RecyclerView rv_receipt_images_ctob, rv_receipt_images_btoc;
    ExpenseAttachmentsAdapter expenseAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();

    public TransferFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance(String param) {
        TransferFragment fragment = new TransferFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.KEY_TRANSACTION_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance() {
        TransferFragment fragment = new TransferFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transfer, container, false);
        init(view);
        setInitialDate();
        populateCategorySpinner();
        populateRecyclerview();
        //Set School Id from Spinner
        setValues();
        bankToCash();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_category.getSelectedItemPosition() == 0) {
                    banktocashValidations();
                }
                if (sp_category.getSelectedItemPosition() == 1) {
                    cashtobankValidations();
                }
            }
        });

        return view;
    }

    private void init(View view) {
        tv_cashinbank = view.findViewById(R.id.tv_cashinbank);
        tv_cashinhand = view.findViewById(R.id.tv_cashinhand);

        txt_currentdate_btoc = view.findViewById(R.id.txt_currentdate_btoc);
        et_jvno_btoc = view.findViewById(R.id.et_jvno_btoc);
        et_amount_btoc = view.findViewById(R.id.et_amount_btoc);
        et_checkno_btoc = view.findViewById(R.id.et_checkno_btoc);
        et_comments_btoc = view.findViewById(R.id.et_comments_btoc);

        txt_currentdate_ctob = view.findViewById(R.id.txt_currentdate_ctob);
        et_jvno_ctob = view.findViewById(R.id.et_jvno_ctob);
        et_amount_ctob = view.findViewById(R.id.et_amount_ctob);
        et_depositslipno_ctob = view.findViewById(R.id.et_depositslipno_ctob);

        et_comments_ctob = view.findViewById(R.id.et_comments_ctob);

        btn_submit = view.findViewById(R.id.btn_submit);

        sp_category = view.findViewById(R.id.spinner_category);
        sp_category.setOnItemSelectedListener(this);

        tv_tocategory = view.findViewById(R.id.tv_tocategory);

        rv_receipt_images_ctob = view.findViewById(R.id.rv_receipt_images_ctob);
        rv_receipt_images_btoc = view.findViewById(R.id.rv_receipt_images_btoc);


        inner_relativelayout1 = view.findViewById(R.id.inner_relativelayout1);
        inner_relativelayout2 = view.findViewById(R.id.inner_relativelayout2);
    }

    private void populateRecyclerview() {

        ExpenseAttachmentsAdapter.paths.clear();
        expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(false, getActivity(),
                attachments);

        if (sp_category.getSelectedItemId() == 1) {

            rv_receipt_images_ctob.setHasFixedSize(true);
            rv_receipt_images_ctob.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            rv_receipt_images_ctob.setAdapter(expenseAttachmentsAdapter);
        } else if (sp_category.getSelectedItemId() == 0)

            rv_receipt_images_btoc.setHasFixedSize(true);
            rv_receipt_images_btoc.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            rv_receipt_images_btoc.setAdapter(expenseAttachmentsAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        if (adapterView.getId() == R.id.spinner_category) {
            if (position == 0) {
                tv_tocategory.setText("Cash");
                inner_relativelayout1.setVisibility(View.GONE);
                inner_relativelayout2.setVisibility(View.VISIBLE);
                clearBanktocashfields();
                populateRecyclerview();
            }
            if (position == 1) {
                tv_tocategory.setText("Bank");
                inner_relativelayout1.setVisibility(View.VISIBLE);
                inner_relativelayout2.setVisibility(View.GONE);
                clearCashtoBankfields();
                populateRecyclerview();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setInitialDate() {
        txt_currentdate_btoc.setText(setDate(0));
        txt_currentdate_ctob.setText(setDate(0));
    }

    private void setValues() {
        schoolId = ExpenseTabsFragment.schoolId;
        valueCashinhand = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId, 2);//2 for cash
        valueCashinbank = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId, 1);//1 for bank
        valueSalary = ExpenseHelperClass.getInstance(getActivity()).getTotalSalary(schoolId);

        tv_cashinhand.setText(AppModel.getInstance().formatNumberInCommas((long) valueCashinhand));
        tv_cashinbank.setText(AppModel.getInstance().formatNumberInCommas((long) valueCashinbank));

        academicSession_id = DatabaseHelper.getInstance(getActivity()).getAcademicSessionId(schoolId);
    }

    private String setDate(int states) {
        calendar = Calendar.getInstance();
        Date c = Calendar.getInstance().getTime();
        calendar.setTime(c);
        calendar.add(Calendar.DAY_OF_MONTH, states);
        c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void populateCategorySpinner() {
        category = new ArrayList<>();
        category.add(0, "Bank");
        category.add(1, "Cash");

        categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.new_spinner_layout2, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(categoryAdapter);

        if (getArguments() != null) {
            if (getArguments().getString(AppConstants.KEY_TRANSACTION_TYPE).equalsIgnoreCase("Withdraw")) {

                sp_category.setSelection(0);
            } else if (getArguments().getString(AppConstants.KEY_TRANSACTION_TYPE).equalsIgnoreCase("Deposit")) {
                sp_category.setSelection(1);

            } else {
                sp_category.setSelection(0);

            }
        }

    }

    private void ShowToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    private void submitBankToCash() {


        ExpenseTransactionModel transactionModel = new ExpenseTransactionModel();
        transactionModel.setForDate(AppModel.getInstance().convertDatetoFormat(txt_currentdate_btoc.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd"));
        transactionModel.setTransAmount(Double.parseDouble(et_amount_btoc.getText().toString()));
        transactionModel.setJvNo(Long.parseLong(et_jvno_btoc.getText().toString()));
        transactionModel.setChequeNo(et_checkno_btoc.getText().toString());
        transactionModel.setRemarks(Objects.requireNonNull(et_comments_btoc.getText()).toString());
        transactionModel.setSqlserverUser("");
        transactionModel.setCreatedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        transactionModel.setCreatedOnServer("");
        transactionModel.setModifiedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        transactionModel.setModifiedOnServer("");
        transactionModel.setCreatedBy(DatabaseHelper.getInstance(getActivity()).getCurrentLoggedInUser().getId());
        transactionModel.setModifiedBy(DatabaseHelper.getInstance(getActivity()).getCurrentLoggedInUser().getId());
        //transactionModel.setActive(true);
        transactionModel.setCreatedFrom("A");
        transactionModel.setModifiedFrom("A");
        transactionModel.setUploadedOn((String)null);
        transactionModel.setSchoolID(schoolId);
        transactionModel.setAcademicSessionID(academicSession_id);


        long transaction_id, transaction_image_id = 0;

        //46 for Deposit Cash
        transactionModel.setSubHeadID(AppConstants.SUBHEAD_ID_BANKTOCASH_EXPENSE);
        transactionModel.setCategoryID(AppConstants.TRANSACTION_CATEGORY_NORMAL_EXPENSE);
        transactionModel.setBucketID(AppConstants.BUCKET_BANK_ID_EXPENSE);//1 for Bank
        transactionModel.setFlowID(AppConstants.FLOW_OUT_ID_EXPENSE);//2 for Out
        //First Entry
        transaction_id = ExpenseHelperClass.getInstance(getActivity()).insertTransaction(transactionModel);

        for (int i = 0; i < ExpenseAttachmentsAdapter.paths.size(); i++) {
            transaction_image_id = ExpenseHelperClass.getInstance(getActivity()).insertExpenseTransactionImages((int) transaction_id, ExpenseAttachmentsAdapter.paths.get(i));
        }

        transactionModel.setBucketID(AppConstants.BUCKET_CASH_ID_EXPENSE);//2 for Cash
        transactionModel.setFlowID(AppConstants.FLOW_IN_ID_EXPENSE);//1 for In
        //Second Entry
        transaction_id = ExpenseHelperClass.getInstance(getActivity()).insertTransaction(transactionModel);



        if (transaction_image_id > 0 && transaction_id > 0) {
            ShowToast("Cash Withdrawal Successfully");
            //Important when any change in table call this method
            AppModel.getInstance().changeMenuPendingSyncCount(getActivity(), true);
            getActivity().finish();
        } else {
            ShowToast("Some thing went wrong");
        }
    }

    private void submitcashToBank() {

        ExpenseTransactionModel transactionModel = new ExpenseTransactionModel();
        transactionModel.setForDate(AppModel.getInstance().convertDatetoFormat(txt_currentdate_ctob.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"));
        transactionModel.setTransAmount(Double.parseDouble(et_amount_ctob.getText().toString()));
        transactionModel.setJvNo(Long.parseLong(et_jvno_ctob.getText().toString()));
        transactionModel.setReceiptNo(et_depositslipno_ctob.getText().toString());
        transactionModel.setRemarks(et_comments_ctob.getText().toString());
        transactionModel.setSqlserverUser("");
        transactionModel.setCreatedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        transactionModel.setCreatedOnServer("");
        transactionModel.setModifiedOnApp(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        transactionModel.setModifiedOnServer("");
        transactionModel.setCreatedBy(DatabaseHelper.getInstance(getActivity()).getCurrentLoggedInUser().getId());
        transactionModel.setModifiedBy(DatabaseHelper.getInstance(getActivity()).getCurrentLoggedInUser().getId());
        //transactionModel.setActive(true);
        transactionModel.setCreatedFrom("A");
        transactionModel.setModifiedFrom("A");
        transactionModel.setUploadedOn((String) null);
        transactionModel.setSchoolID(schoolId);
        transactionModel.setAcademicSessionID(academicSession_id);

        long transaction_id = -1, transaction_image_id = -1;

        //47 for Deposit Cash
        transactionModel.setSubHeadID(AppConstants.SUBHEAD_ID_CASHTOBANK_EXPENSE);//cash to bank
        transactionModel.setCategoryID(AppConstants.TRANSACTION_CATEGORY_NORMAL_EXPENSE);
        transactionModel.setBucketID(AppConstants.BUCKET_CASH_ID_EXPENSE);
        transactionModel.setFlowID(AppConstants.FLOW_OUT_ID_EXPENSE);
        //First Entry
        transaction_id = ExpenseHelperClass.getInstance(getActivity()).insertTransaction(transactionModel);

        for (int i = 0; i < ExpenseAttachmentsAdapter.paths.size(); i++) {
            transaction_image_id = ExpenseHelperClass.getInstance(getActivity()).insertExpenseTransactionImages((int) transaction_id, ExpenseAttachmentsAdapter.paths.get(i));
        }

        transactionModel.setBucketID(AppConstants.BUCKET_BANK_ID_EXPENSE);
        transactionModel.setFlowID(AppConstants.FLOW_IN_ID_EXPENSE);//1 for In
        //Second Entry
        transaction_id = ExpenseHelperClass.getInstance(getActivity()).insertTransaction(transactionModel);


        if (transaction_image_id > 0 && transaction_id > 0) {
            ShowToast("Cash Deposit Successfully");
            //Important when any change in table call this method
            AppModel.getInstance().changeMenuPendingSyncCount(getActivity(), true);
            getActivity().finish();
        } else {
            ShowToast("Some thing went wrong");
        }

    }

    private void bankToCash() {

        et_amount_btoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (!et_amount_btoc.getText().toString().equals("")) {
                    if (Long.parseLong(et_amount_btoc.getText().toString()) > valueCashinbank) {
                        et_amount_btoc.setError("you have insufficient balance in your account");
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

        et_amount_ctob.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (!et_amount_ctob.getText().toString().equals("")) {
                    if (Long.parseLong(et_amount_ctob.getText().toString()) > valueCashinhand) {
                        et_amount_ctob.setError("you have insufficient balance in your cash");
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
    }


    private void ClearImages() {
        ExpenseAttachmentsAdapter.paths.clear();
        attachments.clear();
        expenseAttachmentsAdapter.notifyDataSetChanged();
    }

    private void clearCashtoBankfields() {
        et_amount_ctob.setText("");
        et_jvno_ctob.setText("");
        et_depositslipno_ctob.setText("");
        ClearImages();
    }

    private void clearBanktocashfields() {
        et_amount_btoc.setText("");
        et_jvno_btoc.setText("");
        et_checkno_btoc.setText("");
        ClearImages();
    }

    private void banktocashValidations(){
        if (Objects.requireNonNull(et_jvno_btoc.getText()).toString().isEmpty()) {
            ShowToast("Please enter J.V no");
            return;
        }

        if (et_jvno_btoc.getText().toString().length()!=7) {
            et_jvno_btoc.setError("Please enter only 7 digits");
            return;
        }

        if (ExpenseHelperClass.getInstance(getActivity()).checkJvno(schoolId, Long.parseLong(et_jvno_btoc.getText().toString())) != -1) {
            ShowToast("Please enter unique J.V no");
            return;
        }

        if (!Objects.requireNonNull(et_amount_btoc.getText()).toString().isEmpty()) {

            if (Double.parseDouble(et_amount_btoc.getText().toString())<1000) {
                ShowToast("min amount should be atleast 1000");
                return;
            } else if(Double.parseDouble(et_amount_btoc.getText().toString())>99999){
                ShowToast("max amount should be less than 100000");
                return;
            }

            double amount = Double.parseDouble(et_amount_btoc.getText().toString());
            if(amount>0) {
                if (amount > (valueCashinbank - valueSalary)) {
                    ShowToast("you have insufficient amount in your bank");
                    return;
                }
            } else {
                ShowToast("please enter valid amount");
                return;
            }
        } else {
            ShowToast("Please enter amount");
            return;
        }

        if (Objects.requireNonNull(et_checkno_btoc.getText()).toString().isEmpty()) {
            ShowToast("Please enter cheque no");
            return;
        }

        if (et_checkno_btoc.getText().toString().length()<5) {
            et_checkno_btoc.setError("Please enter minimum 5 digits no");
            return;
        }

        if (ExpenseHelperClass.getInstance(getActivity()).checkSlipno(schoolId, Long.parseLong(et_checkno_btoc.getText().toString().trim()), 1) != -1) {
            ShowToast("Please enter unique cheque no");
            return;
        }

        if (ExpenseAttachmentsAdapter.paths.size() <= 0) {
            ShowToast("Please insert image");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Transfer");
        builder.setMessage("Are you sure you want to transfer?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitBankToCash();
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

    private void cashtobankValidations(){
        if (Objects.requireNonNull(et_jvno_ctob.getText()).toString().isEmpty()) {
            ShowToast("Please enter J.V no");
            return;
        }

        if (et_jvno_ctob.getText().toString().length()!=7) {
            et_jvno_ctob.setError("Please enter only 7 digits");
            return;
        }

        if (ExpenseHelperClass.getInstance(getActivity()).checkJvno(schoolId, Long.parseLong(et_jvno_ctob.getText().toString().trim())) != -1) {
            ShowToast("Please enter unique J.V no");
            return;
        }

        if (!Objects.requireNonNull(et_amount_ctob.getText()).toString().isEmpty()) {

            if (Double.parseDouble(et_amount_ctob.getText().toString())<1000) {
                ShowToast("min amount should be atleast 1000");
                return;
            } else if(Double.parseDouble(et_amount_ctob.getText().toString())>99999){
                ShowToast("max amount should be less than 100000");
                return;
            }

            double amount = Double.parseDouble(et_amount_ctob.getText().toString());
            if(amount>0) {
                if (amount > valueCashinhand) {
                    ShowToast("you have insufficient amount in your cash");
                    return;
                }
            } else {
                ShowToast("please enter valid amount");
                return;
            }
        } else {
            ShowToast("Please enter amount");
            return;
        }

        if (Objects.requireNonNull(et_depositslipno_ctob.getText()).toString().isEmpty()) {
            ShowToast("Please enter deposit no");
            return;
        }

        if (et_depositslipno_ctob.getText().toString().length()<5) {
            et_depositslipno_ctob.setError("Please enter minimum 5 digits no");
            return;
        }

        if (ExpenseHelperClass.getInstance(getActivity()).checkSlipno(schoolId, Long.parseLong(et_depositslipno_ctob.getText().toString().trim()), 0) != -1) {
            ShowToast("Please enter unique deposit no");
            return;
        }

        if (ExpenseAttachmentsAdapter.paths.size() <= 0) {
            ShowToast("Please insert image");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Transfer");
        builder.setMessage("Are you sure you want to transfer?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitcashToBank();
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
}
