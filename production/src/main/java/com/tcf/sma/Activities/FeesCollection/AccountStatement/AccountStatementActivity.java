package com.tcf.sma.Activities.FeesCollection.AccountStatement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.AccountStatementAdapter;
import com.tcf.sma.Adapters.StudentAutoCompleteAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModelNewStructure;
import com.tcf.sma.Models.Fees_Collection.AccountStatementUnionModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Mohammad Haseeb
 */
public class AccountStatementActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    View screen;
    LinearLayout llStudentInfo, ll_title;
    String grNo, toDate, fromDate;
    TextView tv_title;
    List<AccountStatementModel> asmList;
    List<AccountStatementUnionModel> unionModels;
    List<AccountStatementModelNewStructure> modelNewStructureList;
    List<SchoolModel> schoolModels;
    private RecyclerView rvAccountStatement;
    private Spinner spnSchools;
    private ImageView iv_search, iv_search_depositNo_and_receiptNo;
    private EditText et_grNo, et_depSlipNo, et_receiptNo;
    private TextView etFromDate, etToDate, tv_name, tv_className, tv_sectionName, tv_fathersName, tvNoDataFound;
    private int schoolId = 0;
    private AccountStatementAdapter adapter;
    private boolean isRunForFirstTime = false;
    private int roleID = 0;
    private AutoCompleteTextView et_std_name_grNo;
    private StudentAutoCompleteAdapter studentAdapter;
    private LinearLayout ll_depositAndReceiptNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_account_statement);
        setToolbar("Account Statement", this, false);
        init();
        populateSchoolSpinner();
        populateStudentAutocompleteAdapter();

//        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(schoolId);
//        studentAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
//                studentsList);
//        et_std_name_grNo.setAdapter(studentAdapter);

        et_std_name_grNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String std_grno = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getGrNo();
                String std_name = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getStudent_name();
                et_std_name_grNo.setText(std_name);
                grNo = std_grno;
                toDate = etToDate.getText().toString().trim();
                fromDate = etFromDate.getText().toString().trim();

                if (!fromDate.isEmpty())
                    fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                if (!toDate.isEmpty())
                    toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                AppModel.getInstance().showLoader(AccountStatementActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(getAccountStatement(grNo, fromDate, toDate, "", ""));
                        AccountStatementActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    }
                }).start();

                AppModel.getInstance().hideSoftKeyboard(AccountStatementActivity.this);
            }
        });
        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    grNo = "";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setAdapter(getAccountStatement(grNo, fromDate, toDate, et_depSlipNo.getText().toString(), et_receiptNo.getText().toString()));
                        }
                    }).start();
                }
            }
        });
//        grNo = et_std_name_grNo.getText().toString().trim();


//        grNo = et_grNo.getText().toString().trim();
        toDate = etToDate.getText().toString().trim();
        fromDate = etFromDate.getText().toString().trim();

        if (!fromDate.isEmpty())
            fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
        if (!toDate.isEmpty())
            toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");


//        SurveyAppModel.getInstance().showLoader(this);
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                setAdapter(getAccountStatement(grNo, fromDate, toDate));
//
//                AccountStatementActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SurveyAppModel.getInstance().hideLoader();
//                    }
//                });
//            }
//        }).start();

        Intent intent = getIntent();
        if (intent.hasExtra("StudentGrNo")/*intent.hasExtra("classId") && intent.hasExtra("sectionId") &&*/) {
            try {
//                classId = intent.getIntExtra("classId", 0);
//                sectionID = intent.getIntExtra("sectionId", 0);

                grNo = intent.getStringExtra("StudentGrNo");
//                et_grNo.setText(grNo);
                et_std_name_grNo.setText(grNo);
                iv_search.performClick();
                AppModel.getInstance().hideLoader();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppModel.getInstance().showLoader(this);


            new Thread(new Runnable() {
                @Override
                public void run() {

                    setAdapter(getAccountStatement(grNo, fromDate, toDate, et_depSlipNo.getText().toString(), et_receiptNo.getText().toString()));

                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppModel.getInstance().hideLoader();
                        }
                    });
                }
            }).start();
        }

    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(schoolId);
        studentAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
                studentsList);
        et_std_name_grNo.setAdapter(studentAdapter);
    }

    private List<AccountStatementModel> getAccountStatement(String grNo, String fromDate, String toDate, String depSlipNo, String receiptNo) {
//        unionModels = AppInvoice.getInstance(this).getAccountStatement(schoolId, grNo, fromDate, toDate, depSlipNo, receiptNo);
        modelNewStructureList = FeesCollection.getInstance(this).getAccountStatement(schoolId, grNo, fromDate, toDate, depSlipNo, receiptNo);
        asmList = new ArrayList<>();
        if (modelNewStructureList != null && modelNewStructureList.size() > 0) {
            if (grNo != null) {
                if (modelNewStructureList.get(0).getStudentName() != null && !modelNewStructureList.get(0).getStudentName().equals("")) {

                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateStudentFields(modelNewStructureList.get(0));
                            ll_title.setVisibility(View.GONE);
                            llStudentInfo.setVisibility(View.VISIBLE);
                            rvAccountStatement.setVisibility(View.VISIBLE);
                            ll_depositAndReceiptNo.setVisibility(View.VISIBLE);
                        }
                    });
                } else {

                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            llStudentInfo.setVisibility(View.GONE);
                            ll_title.setVisibility(View.GONE);
                            clearStudentFields();
//                            Toast.makeText(AccountStatementActivity.this, "Student Not found", Toast.LENGTH_LONG).show();
                            rvAccountStatement.setVisibility(View.GONE);
                            ll_depositAndReceiptNo.setVisibility(View.GONE);

                        }
                    });
                }

            } else {
                AccountStatementActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llStudentInfo.setVisibility(View.GONE);
                        ll_title.setVisibility(View.VISIBLE);
                        clearStudentFields();

                    }
                });
            }
            int i = 0;
//            for (AccountStatementUnionModel model : modelNewStructureList) {
//                if (model.getCreated_from().equals("1") && model.getTransaction_type().equals("R")) //correction
//                    checkFeesProperties(model, -1);
//                else //normal receipt
//                    checkFeesProperties(model, 1);
//
//
//            }
            for (AccountStatementModelNewStructure model : modelNewStructureList) {
                if (model.getTransaction_typeId() == 2 && model.getTransaction_categoryId() == 2) // 2 = correction 2 = Receipt
                    checkFeesProperties(model, 1);  // -1 should be 1
                else //normal receipt
                    checkFeesProperties(model, -1);  // 1 should be -1


            }
            double TotalAmount = 0;
            double PreviousYearAmount = 0;
            double TotalBalance = 0;
            for (AccountStatementModel model : asmList) {
                String balance;
                if (i == 0) {
                    // + (invoiceamount) - (feesreceipt);
                    TotalAmount += Double.valueOf(model.getAmount());
                    balance = model.getAmount();
                    model.setBalance(balance);
                } else {
                    TotalAmount += Double.valueOf(model.getAmount());
                    balance = String.valueOf(Double.valueOf(asmList.get(i - 1).getBalance()) + Double.valueOf(model.getAmount()));
                    model.setBalance(balance);
                }
                i++;
            }
            TotalBalance = TotalAmount - PreviousYearAmount;
            asmList.add(0, new AccountStatementModel(-1, "", "", "", "", "", "", "", "", ""));
            asmList.add(new AccountStatementModel(-4, "", "", "", AppModel.getInstance().getCurrentDateTime("dd-MM-yy"), "", "This Academic year", String.valueOf(TotalAmount), asmList.get(asmList.size() - 1).getBalance(), 0, ""));
            asmList.add(new AccountStatementModel(-3, "", "", "", AppModel.getInstance().getCurrentDateTime("dd-MM-yy"), "", "Previous Academic years", String.valueOf(PreviousYearAmount), asmList.get(asmList.size() - 1).getBalance(), 0, ""));
            asmList.add(new AccountStatementModel(-2, "", "", "", AppModel.getInstance().getCurrentDateTime("dd-MM-yy"), "", "Total Balance", String.valueOf(TotalBalance), asmList.get(asmList.size() - 1).getBalance(), 0, ""));

        } else {
            if (!et_std_name_grNo.getText().toString().trim().isEmpty()) {
//                boolean digitsOnly = TextUtils.isDigitsOnly(et_std_name_grNo.getText());
//                if (digitsOnly) {
//                    grNo = et_std_name_grNo.getText().toString();
                final StudentModel sm = DatabaseHelper.getInstance(this).getStudentwithGR(Integer.parseInt(grNo), schoolId);
                if (sm.getName() != null && sm.getCurrentClass() != null) {
                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_name.setText(sm.getName());
                            tv_fathersName.setText(sm.getFathersName());
                            tv_className.setText(sm.getCurrentClass());
                            tv_sectionName.setText(sm.getCurrentSection());
                            llStudentInfo.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            llStudentInfo.setVisibility(View.GONE);
                            clearStudentFields();
                        }
                    });
                }
//                } else {
//                    grNo = "";
//                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            llStudentInfo.setVisibility(View.GONE);
//                            clearStudentFields();
//                        }
//                    });
//                }

            }
        }
        return asmList;
    }

    private void checkFeesProperties(AccountStatementUnionModel model, int value) {
        /** If type is invoice **/
        if (model.getTransaction_type().equals("I")) {
            if (model.getFees_admission() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_admission());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.admission_fees) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));

            }
            if (model.getFees_books() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_books());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.books) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));
            }
            if (model.getFees_copies() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_copies());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.copies) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));
            }
            if (model.getFees_exam() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_exam());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.exam_fees) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));
            }
            if (model.getFees_tution() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_tution());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.monthly_fees) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));
            }
            if (model.getFees_uniform() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_uniform());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.uniform) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));
            }
            if (model.getFees_others() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_others());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.others) + data.get(1), data.get(2), model.getTransaction_type(), model.getGrNo()));
            }
        }
        /** If type is Receipt **/
        else if (model.getTransaction_type().equals("R")) {
            if (Math.abs(model.getFees_admission()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.admission_fees) + data.get(1), String.valueOf(model.getFees_admission() * value), model.getTransaction_type(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_books()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.books) + data.get(1), String.valueOf(model.getFees_books() * value), model.getTransaction_type(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_copies()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.copies) + data.get(1), String.valueOf(model.getFees_copies() * value), model.getTransaction_type(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_exam()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.exam_fees) + data.get(1), String.valueOf(model.getFees_exam() * value), model.getTransaction_type(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_tution()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.monthly_fees) + data.get(1), String.valueOf(model.getFees_tution() * value), model.getTransaction_type(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_uniform()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.uniform) + data.get(1), String.valueOf(model.getFees_uniform() * value), model.getTransaction_type(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_others()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.others) + data.get(1), String.valueOf(model.getFees_others() * value), model.getTransaction_type(), model.getGrNo()));
            }
        }
    }

    private void checkFeesProperties(AccountStatementModelNewStructure model, int value) {
        /** If type is invoice **/
        if (model.getTransaction_categoryId() == 1) {
            if (model.getFees_admission() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_admission());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.admission_fees) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));

            }
            if (model.getFees_books() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_books());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.books) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_copies() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_copies());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.copies) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_exam() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_exam());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.exam_fees) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_tution() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_tution());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.monthly_fees) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_uniform() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_uniform());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.uniform) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_others() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_others());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.others) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
        }
        /** If type is Receipt **/
        else if (model.getTransaction_categoryId() == 2) {
            if (Math.abs(model.getFees_admission()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.admission_fees) + data.get(1), String.valueOf(model.getFees_admission() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_books()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.books) + data.get(1), String.valueOf(model.getFees_books() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_copies()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.copies) + data.get(1), String.valueOf(model.getFees_copies() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_exam()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.exam_fees) + data.get(1), String.valueOf(model.getFees_exam() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_tution()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.monthly_fees) + data.get(1), String.valueOf(model.getFees_tution() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_uniform()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.uniform) + data.get(1), String.valueOf(model.getFees_uniform() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_others()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.others) + data.get(1), String.valueOf(model.getFees_others() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
        }
    }

    @NonNull
    private ArrayList<String> generateJvNoAndDescForReceipt(AccountStatementUnionModel model) {
        ArrayList<String> data = new ArrayList<>();
        String jvNo;
        String desc = "";

        if (model.getCreated_from() != null) {
            switch (model.getCreated_from()) {
                case "1":
                    jvNo = model.getSysId() > 0 ? "C" + String.valueOf(model.getSysId()) : "Pending";
                    desc = "- Correction Receipt  ";
                    data.add(jvNo);
                    data.add(desc);
                    break;
                default:
                    jvNo = model.getSysId() > 0 ? "R" + String.valueOf(model.getSysId()) : "Pending";
                    desc = "- Receipt " +
                            (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty() ? model.getReceiptNo() + " " : "");
                    data.add(jvNo);
                    data.add(desc);
                    break;
            }
        } else {
            jvNo = "R" + String.valueOf(model.getSysId());
            desc = "- Receipt " + (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty() ? model.getReceiptNo() + " - " : "");
            data.add(jvNo);
            data.add(desc);
        }

        return data;
    }

    private ArrayList<String> generateJvNoAndDescForReceipt(AccountStatementModelNewStructure model) {
        ArrayList<String> data = new ArrayList<>();
        String jvNo;
        String desc = "";

        if (model.getTransaction_typeId() > 0) {
            switch (model.getTransaction_typeId()) {
                case 2:
                    jvNo = model.getSysId() > 0 ? "C" + String.valueOf(model.getSysId()) : "Pending";
                    desc = "- Correction Receipt  ";
                    data.add(jvNo);
                    data.add(desc);
                    break;
                case 1:
                    jvNo = model.getSysId() > 0 ? "R" + String.valueOf(model.getSysId()) : "Pending";
                    desc = "- Receipt " +
                            (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty() ? model.getReceiptNo() + " " : "");
                    data.add(jvNo);
                    data.add(desc);
                    break;
            }
        } else {
            jvNo = "R" + String.valueOf(model.getSysId());
            desc = "- Receipt " + (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty() ? model.getReceiptNo() + " - " : "");
            data.add(jvNo);
            data.add(desc);
        }

        return data;
    }

    private ArrayList<String> generateJvNoAndDescForInvoice(AccountStatementUnionModel model, double amount) {
        ArrayList<String> data = new ArrayList<>();
        String jvNo;
        String desc = "";
        String strAmount;
        if (model.getSysId() > 0) {
            if (model.getCreated_from() != null) {
                switch (model.getCreated_from()) {
                    case "W":
                        jvNo = "W" + String.valueOf(model.getSysId());
//                        desc = "- Waiver Receipt " + model.getSysId();
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    case "B":
                        jvNo = "B" + String.valueOf(model.getSysId());
//                        desc = "- Waiver Receipt " + model.getSysId();
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    case "S":
                        jvNo = "C" + String.valueOf(model.getSysId());
//                        desc = "- Correction Receipt " + model.getSysId();
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    default:
                        jvNo = "V" + String.valueOf(model.getSysId());
//                        desc = "- Invoice " + model.getSysId();
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                }
            } else {
                jvNo = "V" + String.valueOf(model.getSysId());
//                desc = "- Invoice " + model.getSysId();
                strAmount = String.valueOf(amount);
                data.add(jvNo);
                data.add(strAmount);
            }
        } else {


            if (model.getCreated_from() != null) {
                switch (model.getCreated_from()) {
                    case "S":
                        jvNo = "Pending";
                        desc = "- Correction Invoice ";
                        strAmount = String.valueOf((amount) * (-1));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    case "W":
                        jvNo = "Pending";
                        desc = "- Waiver Invoice";
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    default:
                        jvNo = "Pending";
//            desc = "- Invoice ";
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                }
            }
        }
        return data;
    }

    private ArrayList<String> generateJvNoAndDescForInvoice(AccountStatementModelNewStructure model, double amount) {
        ArrayList<String> data = new ArrayList<>();
        String jvNo;
        String desc = "";
        String strAmount;
        if (model.getSysId() > 0) {
            if (model.getTransaction_typeId() > 0) {
                // 1 = Receipt/Normal, 2 = Correction , 3 = Waiver
                switch (model.getTransaction_typeId()) {
                    case 3:
                        jvNo = "W" + String.valueOf(model.getSysId());
//                        desc = "- Waiver Receipt " + model.getSysId();
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
//                    case 1:
//                        jvNo = "B" + String.valueOf(model.getSysId());
////                        desc = "- Waiver Receipt " + model.getSysId();
//                        strAmount = String.valueOf((amount * (-1)));
//                        data.add(jvNo);
//                        data.add(desc);
//                        data.add(strAmount);
//                        break;
                    case 2:
                        jvNo = "C" + String.valueOf(model.getSysId());
//                        desc = "- Correction Receipt " + model.getSysId();
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    default:
                        jvNo = "V" + String.valueOf(model.getSysId());
//                        desc = "- Invoice " + model.getSysId();
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                }
            } else {
                jvNo = "V" + String.valueOf(model.getSysId());
//                desc = "- Invoice " + model.getSysId();
                strAmount = String.valueOf(amount);
                data.add(jvNo);
                data.add(strAmount);
            }
        } else {


            if (model.getTransaction_typeId() > 0) {
                switch (model.getTransaction_typeId()) {
                    case 2:
                        jvNo = "Pending";
                        desc = "- Correction Invoice ";
                        strAmount = String.valueOf((amount) * (-1));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    case 3:
                        jvNo = "Pending";
                        desc = "- Waiver Invoice";
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    default:
                        jvNo = "Pending";
//            desc = "- Invoice ";
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                }
            }
        }
        return data;
    }


    public void init() {
        et_grNo = (EditText) screen.findViewById(R.id.et_grNo);
        etFromDate = (TextView) screen.findViewById(R.id.etFromDate);
        etToDate = (TextView) screen.findViewById(R.id.etToDate);
        et_depSlipNo = (EditText) screen.findViewById(R.id.et_depSlipNo);
        et_receiptNo = (EditText) screen.findViewById(R.id.et_receiptNo);
        etFromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
//        etFromDate.setText(SurveyAppModel.getInstance().getLastMonthFromCurrentDate("dd-MM-yy"));
        etToDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
//        etToDate.setText(SurveyAppModel.getInstance().getCurrentDateTime("dd-MM-yy"));
        iv_search = (ImageView) screen.findViewById(R.id.iv_search);
        tv_name = (TextView) screen.findViewById(R.id.tv_name);
        ll_title = (LinearLayout) screen.findViewById(R.id.ll_title);
        llStudentInfo = (LinearLayout) screen.findViewById(R.id.llStudentInfo);
        tv_className = (TextView) screen.findViewById(R.id.tv_className);
        tv_sectionName = (TextView) screen.findViewById(R.id.tv_sectionName);
        tv_fathersName = (TextView) screen.findViewById(R.id.tv_fathersName);
        tvNoDataFound = (TextView) screen.findViewById(R.id.tvNoDataFound);
        tv_title = (TextView) screen.findViewById(R.id.tv_title);
        et_std_name_grNo = (AutoCompleteTextView) screen.findViewById(R.id.et_std_name_grNo);
        iv_search_depositNo_and_receiptNo = (ImageView) screen.findViewById(R.id.iv_search_depositNo_and_receiptNo);
        ll_depositAndReceiptNo = (LinearLayout) screen.findViewById(R.id.ll_depositAndReceiptNo);

        etToDate.setOnClickListener(this);
        etFromDate.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_search_depositNo_and_receiptNo.setOnClickListener(this);
        spnSchools = (Spinner) screen.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }

        rvAccountStatement = (RecyclerView) screen.findViewById(R.id.rvAccountStatement);

        rvAccountStatement.setLayoutManager(new LinearLayoutManager(this));
        rvAccountStatement.setNestedScrollingEnabled(false);
//        rvAccountStatement.setAdapter(new ReceiptAdapter(this, receiptModels, this));

        et_grNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    grNo = et_grNo.getText().toString().trim();
                    toDate = etToDate.getText().toString().trim();
                    fromDate = etFromDate.getText().toString().trim();
//                    if (!fromDate.isEmpty())
//                        fromDate = SurveyAppModel.getInstance().convertDatetoFormat(fromDate, "dd-MM-yy", "yyyy-MM-dd");
//                    if (!toDate.isEmpty())
//                        toDate = SurveyAppModel.getInstance().convertDatetoFormat(toDate, "dd-MM-yy", "yyyy-MM-dd");
                    if (!fromDate.isEmpty())
                        fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                    if (!toDate.isEmpty())
                        toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");

                    AppModel.getInstance().showLoader(AccountStatementActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            setAdapter(getAccountStatement(grNo, fromDate, toDate, et_depSlipNo.getText().toString(), et_receiptNo.getText().toString()));

                            AccountStatementActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppModel.getInstance().hideLoader();
                                }
                            });
                        }
                    }).start();
                    AppModel.getInstance().hideSoftKeyboard(AccountStatementActivity.this);
                    return true;
                }
                return false;
            }
        });

    }

    private void clearStudentFields() {
        tv_name.setText("");
        tv_sectionName.setText("");
        tv_fathersName.setText("");
        tv_className.setText("");
    }

    private void populateStudentFields(AccountStatementUnionModel sm) {
        tv_name.setText(sm.getStudentName());
        tv_fathersName.setText(sm.getFatherName());
        tv_className.setText(sm.getClassName());
        tv_sectionName.setText(sm.getSection());
    }

    private void populateStudentFields(AccountStatementModelNewStructure sm) {
        tv_name.setText(sm.getStudentName());
        tv_fathersName.setText(sm.getFatherName());
        tv_className.setText(sm.getClassName());
        tv_sectionName.setText(sm.getSection());
    }

    private void populateSchoolSpinner() {
        schoolId = AppModel.getInstance().getSelectedSchool(this);
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();

        if (schoolModels.size() > 0) {
            tv_title.setText("Statement for School \n" + schoolModels.get(0).getName());
            ll_title.setVisibility(View.VISIBLE);
        } else {
            ll_title.setVisibility(View.GONE);
        }

        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            spnSchools.setSelection(indexOfSelectedSchool);
            schoolId = AppModel.getInstance().getSpinnerSelectedSchool(this);
        }
//        int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
//        spnSchools.setSelection(selectedSchoolIndex);


    }

    private int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
        int index = 0;
        for (SchoolModel model : schoolModels) {
            if (model.getId() == appSchoolId)
                return index;
            index++;
        }
        return index;
    }

    private void setAdapter(final List<AccountStatementModel> asmList) {
        if (asmList.size() > 0) {
            adapter = new AccountStatementAdapter(AccountStatementActivity.this, asmList,
                    grNo, et_receiptNo.getText().toString(),
                    et_depSlipNo.getText().toString(), modelNewStructureList);
//            adapter = new AccountStatementAdapter(AccountStatementActivity.this, asmList,
//                    grNo, et_receiptNo.getText().toString(),unionModels,
//                    et_depSlipNo.getText().toString());
//            adapter = new AccountStatementAdapter(AccountStatementActivity.this, asmList);

            AccountStatementActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rvAccountStatement.setAdapter(adapter);
                    rvAccountStatement.setVisibility(View.VISIBLE);
                    if (grNo == null || grNo.isEmpty()) {
                        ll_depositAndReceiptNo.setVisibility(View.GONE);
                    } else if (grNo != null) {
                        ll_depositAndReceiptNo.setVisibility(View.VISIBLE);
                    }

                }
            });
        } else {
            AccountStatementActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rvAccountStatement.setVisibility(View.GONE);
                    ll_depositAndReceiptNo.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSchools:
                if (isRunForFirstTime) {
                    schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
                    AppModel.getInstance().setSpinnerSelectedSchool(this,
                            schoolId);
                    tv_title.setText("Statement for School \n" + schoolModels.get(position).getName());
                    ll_title.setVisibility(View.VISIBLE);
                    llStudentInfo.setVisibility(View.GONE);
                    populateStudentAutocompleteAdapter();
                    clearStudentFields();
//                    et_grNo.setText("");
                    et_std_name_grNo.setText("");
                    grNo = "";
//                    grNo = et_std_name_grNo.getText().toString().trim();
//                    grNo = et_grNo.getText().toString().trim();
                    toDate = etToDate.getText().toString().trim();
                    fromDate = etFromDate.getText().toString().trim();
//                if (!fromDate.isEmpty())
//                    fromDate = SurveyAppModel.getInstance().convertDatetoFormat(fromDate, "dd-MM-yy", "yyyy-MM-dd");
//                if (!toDate.isEmpty())
//                    toDate = SurveyAppModel.getInstance().convertDatetoFormat(toDate, "dd-MM-yy", "yyyy-MM-dd");

                    if (!fromDate.isEmpty())
                        fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                    if (!toDate.isEmpty())
                        toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                    AppModel.getInstance().showLoader(AccountStatementActivity.this);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            setAdapter(getAccountStatement(grNo, fromDate, toDate, et_depSlipNo.getText().toString(), et_receiptNo.getText().toString()));

                            AccountStatementActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppModel.getInstance().hideLoader();
                                }
                            });
                        }
                    }).start();
                }

                isRunForFirstTime = true;

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                et_depSlipNo.setText("");
                et_receiptNo.setText("");
//                grNo = et_std_name_grNo.getText().toString().trim();
//                grNo = et_grNo.getText().toString().trim();
                toDate = etToDate.getText().toString().trim();
                fromDate = etFromDate.getText().toString().trim();
//                if (!fromDate.isEmpty())
//                    fromDate = SurveyAppModel.getInstance().convertDatetoFormat(fromDate, "dd-MM-yy", "yyyy-MM-dd");
//                if (!toDate.isEmpty())
//                    toDate = SurveyAppModel.getInstance().convertDatetoFormat(toDate, "dd-MM-yy", "yyyy-MM-dd");

                if (!fromDate.isEmpty())
                    fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                if (!toDate.isEmpty())
                    toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                AppModel.getInstance().showLoader(AccountStatementActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(getAccountStatement(grNo, fromDate, toDate, et_depSlipNo.getText().toString(), et_receiptNo.getText().toString()));

                        AccountStatementActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.iv_search_depositNo_and_receiptNo:
//                et_std_name_grNo.setText("");
//                grNo = et_std_name_grNo.getText().toString();
                if (grNo != null) {
                    toDate = etToDate.getText().toString().trim();
                    fromDate = etFromDate.getText().toString().trim();

                    if (!fromDate.isEmpty())
                        fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                    if (!toDate.isEmpty())
                        toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                    AppModel.getInstance().showLoader(AccountStatementActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            setAdapter(getAccountStatement(grNo, fromDate, toDate, "", ""));

                            AccountStatementActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppModel.getInstance().hideLoader();
                                }
                            });
                        }
                    }).start();
                } else {
                    ll_depositAndReceiptNo.setVisibility(View.GONE);
                }
                break;
            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(etFromDate, AccountStatementActivity.this);
                break;
            case R.id.etToDate:
                AppModel.getInstance().DatePicker(etToDate, AccountStatementActivity.this);
                break;
        }
    }

}
