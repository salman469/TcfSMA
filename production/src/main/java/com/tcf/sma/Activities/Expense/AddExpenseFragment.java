package com.tcf.sma.Activities.Expense;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    View view;
    RadioGroup rg_expensetype, rg_cash;
    public static RadioButton rb_pettycash, rb_advance, rb_salary, rb_cashinhand, rb_bank;
    TextView tv_expensetype_amount, tv_expensetype, txt_attachment, tv_expense_subHead_amount;
    LinearLayout mid_layout_pettycash,
            ll_receipt_images,
            ll_employee_sp, ll_expensehead_sp,
            ll_amount, ll_jvno, ll_expenseDate, llcheckno, ll_depositslipno, ll_expense_subheadlimit;
    private Spinner sp_expensehead, sp_employee;
    private ArrayList<ExpenseSubheadModel> expenseSubheads;
    private int schoolId = 0, expenseSubheadSpinnerValue = 0, employeeSpinnerValue = 0, employeeId = 0, pettycash_id = 2, advance_id = 4, salary_id = 3;
    private double totalamount = 0,
            amt_salary = 0, valueCashinhand = 0, valueCashinbank = 0;
    private ArrayAdapter<ExpenseSubheadModel> ExpenseAdapter, ImagesAdapter;
    private RecyclerView rv_receipt_images;
    private ExpenseAttachmentsAdapter expenseAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private AppCompatEditText et_amount, et_jvno, et_checkno, et_depositno;
    private KeyListener et_amount_listener;
    private Button btn_submit;
    private TextView txt_ExpenseDate;
    private DatePickerDialog mDatePickerDialog;
    private String strDate, _pickedDate, allowedFromBank = "Bank", allowedFromCash = "Cash";
    private List<EmployeeModel> employeesList;
    private ArrayAdapter<EmployeeModel> EmployeeAdapter;
    private int salaray_user_id = 0, subhead_id = -1,academicSession_id;
    String allowedfrom = "";
    private double valueSalary = 0, SpentAmountSubheadLimitsMonthly = 0;
    private ExpenseSubheadModel expenseSubheadModel;
    private double SpentAmountPettyCashLimitsMOnthly = 0,getAvailableAmountPettyCashLimitsMOnthly=0,getAvailableAmountSubheadLimitsMOnthly=0;
    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        setValues();
        init(view);
        setInitialDate();
        SetOnClickListeners();
        return view;
    }

    private void init(View view) {
        txt_ExpenseDate = view.findViewById(R.id.txt_ExpenseDate);
        et_jvno = view.findViewById(R.id.tv_jvno);
        et_amount = view.findViewById(R.id.et_amount);
        et_amount_listener = et_amount.getKeyListener();
        et_checkno = view.findViewById(R.id.tv_checkno);
        et_depositno = view.findViewById(R.id.et_depositslipno);
        ll_employee_sp = view.findViewById(R.id.ll_employee_sp);

        ll_expensehead_sp = view.findViewById(R.id.ll_expensehead_sp);
        ll_jvno = view.findViewById(R.id.ll_jvno);
        ll_expenseDate = view.findViewById(R.id.ll_expenseDate);
        ll_amount = view.findViewById(R.id.ll_amount);

        sp_expensehead = view.findViewById(R.id.sp_expensehead);
        sp_expensehead.setOnItemSelectedListener(this);
        tv_expense_subHead_amount = view.findViewById(R.id.tv_expense_subHead_amount);
        ll_expense_subheadlimit = view.findViewById(R.id.ll_expense_subheadlimit);
        sp_employee = view.findViewById(R.id.sp_employee);
        sp_employee.setOnItemSelectedListener(this);
        btn_submit = view.findViewById(R.id.btn_submit);


        rv_receipt_images = view.findViewById(R.id.rv_receipt_images);
        rv_receipt_images.setNestedScrollingEnabled(false);
        rv_receipt_images.setHasFixedSize(true);

        rv_receipt_images.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ExpenseAttachmentsAdapter.paths.clear();
        expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(schoolId, false, getActivity(), attachments, true);
        rv_receipt_images.setAdapter(expenseAttachmentsAdapter);

        mid_layout_pettycash = view.findViewById(R.id.mid_layout_pettycash);
        llcheckno = view.findViewById(R.id.llcheckno);
        ll_depositslipno = view.findViewById(R.id.ll_depositslipno);

        ll_receipt_images = view.findViewById(R.id.ll_receipt_images);
        tv_expensetype_amount = view.findViewById(R.id.tv_expensetype_amount);
        tv_expensetype = view.findViewById(R.id.tv_expensetype);
        txt_attachment = view.findViewById(R.id.txt_attachment);
        rg_expensetype = view.findViewById(R.id.rg_expensetype);
        rg_expensetype.setOnCheckedChangeListener(this);
        rg_cash = view.findViewById(R.id.rg_cash);
        rg_cash.setOnCheckedChangeListener(this);
        rb_pettycash = view.findViewById(R.id.rb_pettycash);
        rb_advance = view.findViewById(R.id.rb_advance);
        rb_salary = view.findViewById(R.id.rb_salary);
        rb_cashinhand = view.findViewById(R.id.rb_cashinhand);
        rb_bank = view.findViewById(R.id.rb_bank);

        totalamount = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,1) + ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,2);
        tv_expensetype.setText("PETTY CASH REMAINING");
        tv_expensetype_amount.setText(AppModel.getInstance().formatNumberInCommas((long) totalamount));

        if (getArguments() != null) {
            if (getArguments().getString(AppConstants.KEY_EXPENSE_TYPE) != null && getArguments().getString(AppConstants.KEY_EXPENSE_TYPE).equalsIgnoreCase(AppConstants.VALUE_EXPENSE_TYPE_ADVANCE)) {
                //tv_tocategory.setText("Deposit");
                rb_advance.setChecked(true);
            } else if (getArguments().getString(AppConstants.KEY_EXPENSE_TYPE) != null && getArguments().getString(AppConstants.KEY_EXPENSE_TYPE).equalsIgnoreCase(AppConstants.VALUE_EXPENSE_TYPE_SALARY)) {
                rb_salary.setChecked(true);
                //tv_tocategory.setText("Cash");
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_pettycash) {

            onCheckedChangePettyCash();

        } else if (checkedId == R.id.rb_advance) {

            onCheckedChangeAdvance();

        } else if (checkedId == R.id.rb_salary) {

            onCheckedChangeSalary();

        } else if (checkedId == R.id.rb_cashinhand) {

            onCheckedChangeCashInHand();

        } else if (checkedId == R.id.rb_bank) {

            onCheckedChangeCashInBank();
        }
    }

    private void getAvailableAmount(){
        if (rb_pettycash.isChecked()) {
            getAvailableAmountPettyCashLimitsMOnthly = ExpenseHelperClass.getInstance(getActivity()).getAvailableAmountPettyCashLimitsMOnthly(schoolId);
            getAvailableAmountSubheadLimitsMOnthly = ExpenseHelperClass.getInstance(getActivity()).getAvailableAmountSubheadLimitsMOnthly(schoolId, subhead_id, allowedfrom);
        } else if(rb_advance.isChecked()) {
            getAvailableAmountSubheadLimitsMOnthly = ExpenseHelperClass.getInstance(getActivity()).getAvailableAmountSubheadLimitsMOnthly(schoolId, subhead_id, allowedfrom);
        }
    }

    private void AmountTextChangeListener() {

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (rg_cash.getVisibility() == View.VISIBLE) {

                    if(!s.toString().isEmpty()) {
                        if (rb_pettycash.isChecked()) {
                            if (Double.parseDouble(s.toString()) > getAvailableAmountPettyCashLimitsMOnthly) {
                                et_amount.setError("Your amount is greater than petty cash limit");
                            } else if (Double.parseDouble(s.toString()) > getAvailableAmountSubheadLimitsMOnthly) {
                                et_amount.setError("Your amount is greater than subhead monthly limit");
                            }
                        } else if (rb_advance.isChecked()) {
                            if (Double.parseDouble(s.toString()) > getAvailableAmountSubheadLimitsMOnthly) {
                                et_amount.setError("Your amount is greater than subhead monthly limit");
                            }
                        }
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

    private void populateExpenseSubheads(int schoolId, int head_id, String allowedFrom) {
        expenseSubheads = ExpenseHelperClass.getInstance(getActivity()).getExpenseSubheadsforSchool(schoolId, head_id, allowedFrom);
        if (expenseSubheads.size() > 0) {
            expenseSubheads.add(0,new ExpenseSubheadModel(0,getResources().getString(R.string.select_subhead)));
            ExpenseAdapter = new ArrayAdapter<ExpenseSubheadModel>(getActivity(), R.layout.new_spinner_layout_black, expenseSubheads);
            ExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_expensehead.setAdapter(ExpenseAdapter);
            sp_expensehead.setSelection(0);

        } else {
            sp_expensehead.setAdapter(null);
        }

    }

    private void populateEmployeeSpinner() {

        employeesList = ExpenseHelperClass.getInstance(getActivity()).getEmployeesForSalary(schoolId);
        if (employeesList.size() > 0) {
            employeesList.add(0,new EmployeeModel(0,"",getResources().getString(R.string.select_employee),""));
            EmployeeAdapter = new ArrayAdapter<EmployeeModel>(getActivity(), R.layout.new_spinner_layout_black, employeesList);
            EmployeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_employee.setAdapter(EmployeeAdapter);
            sp_employee.setSelection(0);
        } else {
            sp_employee.setAdapter(null);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.sp_employee) {
            employeeSpinnerValue = employeesList.get(position).getId();
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            et_amount.setText(String.valueOf(ExpenseHelperClass.getInstance(getContext()).getSalary(employeeSpinnerValue)));
            salaray_user_id = employeeSpinnerValue;
        }
        if (adapterView.getId() == R.id.sp_expensehead) {
            expenseSubheadSpinnerValue = ((ExpenseSubheadModel) adapterView.getItemAtPosition(position)).getSubhead_id();
            if(expenseSubheadSpinnerValue > 0) {

                expenseSubheadModel = (ExpenseSubheadModel) adapterView.getItemAtPosition(position);
                ll_expense_subheadlimit.setVisibility(View.VISIBLE);
                double subHeadAmount = expenseSubheadModel.getLimit_amount() - ExpenseHelperClass.getInstance(getActivity()).getSpentAmountofsubheadfromTransaction(schoolId,expenseSubheadSpinnerValue) ;
                tv_expense_subHead_amount.setText(AppModel.getInstance().formatNumberInCommas(Math.round(subHeadAmount)));
                subhead_id = expenseSubheadSpinnerValue;

                getAvailableAmount();

                AmountTextChangeListener();

            }
            else {
                ll_expense_subheadlimit.setVisibility(View.GONE);
            }
            blankAllEdittext();
            sp_employee.setSelection(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void ClearImagesBothReceiptAndCheck() {
        ExpenseAttachmentsAdapter.paths.clear();
        attachments.clear();
        expenseAttachmentsAdapter.notifyDataSetChanged();
    }

    public void ShowToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    private void blankAllEdittext() {
        et_jvno.setText("");
        et_amount.setText("");
        et_amount.setError(null);
        et_checkno.setText("");
        et_depositno.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity().getIntent().hasExtra("expense_type")) {
            String expense_type = getActivity().getIntent().getStringExtra("expense_type");
            if (expense_type.equals("pcash")) {
                rb_pettycash.setChecked(true);
                rb_advance.setVisibility(View.GONE);
                rb_salary.setVisibility(View.GONE);
            } else if (expense_type.equals("salary")) {
                rb_salary.setChecked(true);
                rb_pettycash.setVisibility(View.GONE);
                rb_advance.setVisibility(View.GONE);
            } else if (expense_type.equals("advance")) {
                rb_advance.setChecked(true);
                rb_pettycash.setVisibility(View.GONE);
                rb_salary.setVisibility(View.GONE);
            }

        }
    }

    private void SetOnClickListeners(){
        txt_ExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datepicker(R.id.txt_ExpenseDate);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_salary.isChecked()) {
                    if (employeeSpinnerValue <= 0) {
                        ShowToast("Please select employee");
                        return;
                    }

                } else {
                    if (expenseSubheadSpinnerValue <= 0) {
                        ShowToast("Please select expense head");
                        return;
                    }
                }

                if (et_jvno.getText().toString().equals("")) {
                    ShowToast("Please enter J.V no");
                    return;
                }

                if (et_jvno.getText().toString().length()!=7) {
                    ShowToast("Please enter only 7 digits J.V no");
                    return;
                }

                if (ExpenseHelperClass.getInstance(getActivity()).checkJvno(schoolId, Long.parseLong(et_jvno.getText().toString())) != -1) {
                    ShowToast("Please enter unique J.V no");
                    return;
                }


                if (!et_amount.getText().toString().equals("")) {

                    if(rb_advance.isChecked()||rb_pettycash.isChecked()) {
                        if (Double.parseDouble(et_amount.getText().toString()) < 1000) {
                            ShowToast("min amount should be atleast 1000");
                            return;
                        } else if (Double.parseDouble(et_amount.getText().toString()) > 99999) {
                            ShowToast("max amount should be less than 100000");
                            return;
                        }
                    }

                    if (rb_cashinhand.isChecked()) {

                        if (et_depositno.getText().toString().equals("")) {
                            ShowToast("Please enter deposit no");
                            return;
                        }

                        if (ExpenseHelperClass.getInstance(getActivity()).checkSlipno(schoolId, Long.parseLong(et_depositno.getText().toString().trim()), 0) != -1) {
                            ShowToast("Please enter unique deposit no");
                            return;
                        }

                    } else if (rb_bank.isChecked() || rb_salary.isChecked()) {

                        if (et_checkno.getText().toString().equals("")) {
                            ShowToast("Please enter cheque no");
                            return;
                        }

                        if (et_checkno.getText().toString().length()<5) {
                            ShowToast("Please enter minimum 5 digits cheque no");
                            return;
                        }

                        if (ExpenseHelperClass.getInstance(getActivity()).checkSlipno(schoolId, Long.parseLong(et_checkno.getText().toString().trim()), 1) != -1) {
                            ShowToast("Please enter unique cheque no");
                            return;
                        }

                    }

                    if (ExpenseAttachmentsAdapter.paths.size() <= 0) {
                        ShowToast("Please add attachments");
                        return;
                    }

                    if (rb_pettycash.isChecked()) {
                        if (rb_cashinhand.isChecked() || rb_bank.isChecked()) {
                            if (Validations()) {
                                SubmitTransaction();
                            }
                        } else {
                            ShowToast("Please select cash in hand or bank");
                        }
                    }
                    if (rb_advance.isChecked()) {
                        if (rb_cashinhand.isChecked() || rb_bank.isChecked()) {
                            if (Validations()) {
                                SubmitTransaction();
                            }
                        } else {
                            ShowToast("Please select cash in hand or bank");
                        }
                    }

                    if (rb_salary.isChecked()) {
                        if(valueCashinbank>Double.parseDouble(et_amount.getText().toString())) {
                            SubmitTransaction();
                        } else {
                            ShowToast("You have insufficient amount in bank");
                        }
                    }
                } else {
                    ShowToast("Please insert amount");
                }
            }
        });
    }

    private void setValues(){
        //Set School Id from Spinner
        schoolId = ExpenseTabsFragment.schoolId;
        //bucket id 2 for cash
        valueCashinhand = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,2);
        //bucket id 1 for bank
        valueCashinbank = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,1);
        valueSalary = ExpenseHelperClass.getInstance(getActivity()).getTotalSalary(schoolId);
        academicSession_id = DatabaseHelper.getInstance(getActivity()).getAcademicSessionId(schoolId);

    }

    private void setInitialDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        String formattedDate = df.format(c);
        txt_ExpenseDate.setText(formattedDate);
    }

    private void Datepicker(int id) {


        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
            strDate = format.format(calendar.getTime());
            if (id == R.id.txt_ExpenseDate)
                txt_ExpenseDate.setText(strDate);

          /*  if(id == R.id.tv_end_date)
                tv_end_date.setText(strDate);*/

            String _year = String.valueOf(year);
            String _month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            _pickedDate = _date + "-" + _month + "-" + _year;

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        if (id == R.id.txt_ExpenseDate) {
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        mDatePickerDialog.show();
    }


    public static AddExpenseFragment newInstance() {
        return new AddExpenseFragment();
    }

    private void SubmitTransaction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Expense");
        builder.setMessage("Are you sure you want to submit expense?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ExpenseTransactionModel transactionModel = new ExpenseTransactionModel();
                transactionModel.setForDate(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                transactionModel.setTransAmount(Double.parseDouble(et_amount.getText().toString()));
                transactionModel.setJvNo(Long.parseLong(et_jvno.getText().toString()));

                if (rb_pettycash.isChecked() || rb_advance.isChecked()) {
                    if (rb_bank.isChecked())
                        transactionModel.setChequeNo(et_checkno.getText().toString());
                    if (rb_cashinhand.isChecked())
                        transactionModel.setReceiptNo(et_depositno.getText().toString());
                }
                if (rb_salary.isChecked())
                    transactionModel.setChequeNo(et_checkno.getText().toString());

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

                if (rb_advance.isChecked() || rb_pettycash.isChecked()) {
                    transactionModel.setSubHeadID(expenseSubheadModel.getSubhead_id());
                    //SpentAmountSubheadLimitsMonthly = expenseSubheadModel.getSpent_amount();
                }

                if (rb_salary.isChecked()) {
                    transactionModel.setSalaryUserID(salaray_user_id);
                    transactionModel.setSubHeadID(AppConstants.SUBHEAD_ID_SALARY_EXPENSE);// for salary
                    transactionModel.setBucketID(AppConstants.BUCKET_BANK_ID_EXPENSE);//from bank
                    transactionModel.setFlowID(AppConstants.FLOW_OUT_ID_EXPENSE);//for Out
                }

                long transaction_id = -1, transaction_image_id = -1;

                transactionModel.setCategoryID(AppConstants.TRANSACTION_CATEGORY_NORMAL_EXPENSE);

                if (rb_bank.isChecked()) {
                    transactionModel.setBucketID(AppConstants.BUCKET_BANK_ID_EXPENSE);//for bank
                    transactionModel.setFlowID(AppConstants.FLOW_OUT_ID_EXPENSE);//for Out
                }

                if (rb_cashinhand.isChecked()) {
                    transactionModel.setBucketID(AppConstants.BUCKET_CASH_ID_EXPENSE);
                    transactionModel.setFlowID(AppConstants.FLOW_OUT_ID_EXPENSE);

                }

                transaction_id = ExpenseHelperClass.getInstance(getActivity()).insertTransaction(transactionModel);

            /*    //If Pettycash transaction occurs then spent smount will be update in Schoolpettycashmonthlylimits
                if (rb_pettycash.isChecked()) {
                    SpentAmountPettyCashLimitsMOnthly = ExpenseHelperClass.getInstance(getActivity()).getAvailableSpentAmountPettyCashLimitsMOnthly(schoolId);
                    SpentAmountPettyCashLimitsMOnthly = SpentAmountPettyCashLimitsMOnthly + Double.parseDouble(et_amount.getText().toString().trim());
                    ExpenseHelperClass.getInstance(getActivity()).updateSchoolPettyCashMonthlyLimits(getActivity(), schoolId, SpentAmountPettyCashLimitsMOnthly);
                }
                //If Pettycash and Advance transaction occurs then spent smount will be update in Subheadmonthlylimits
                if (rb_pettycash.isChecked() || rb_advance.isChecked()) {
                    SpentAmountSubheadLimitsMonthly = ExpenseHelperClass.getInstance(getActivity()).getAvailableAmountSubheadLimitsMOnthly(schoolId, subhead_id, allowedfrom);
                    SpentAmountSubheadLimitsMonthly = SpentAmountSubheadLimitsMonthly + Double.parseDouble(et_amount.getText().toString().trim());
                    ExpenseHelperClass.getInstance(getActivity()).updateSubheadLimitsMonthly(getActivity(), subhead_id, schoolId, allowedfrom, SpentAmountSubheadLimitsMonthly);
                }*/

//                if(rb_salary.isChecked()){
//                    ExpenseHelperClass.getInstance(getActivity()).markSalaryPaid(salaray_user_id);
//                }


                for (int i = 0; i < ExpenseAttachmentsAdapter.paths.size(); i++) {
                    transaction_image_id = ExpenseHelperClass.getInstance(getActivity()).insertExpenseTransactionImages((int) transaction_id, ExpenseAttachmentsAdapter.paths.get(i));
                }

                if (transaction_image_id > 0) {
                    ShowToast("Submit Successfully");
                    AppModel.getInstance().changeMenuPendingSyncCount(getActivity(), true);
                    getActivity().finish();
                } else {
                    ShowToast("Some thing went wrong");
                }

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

       /* if (i == 0) {
            return true;
        }*/

        if (rb_pettycash.isChecked()) {
            if (Double.parseDouble(et_amount.getText().toString()) > getAvailableAmountPettyCashLimitsMOnthly) {
                i++;
                ShowToast("Your amount is greater than petty cash limit");
            } else if (Double.parseDouble(et_amount.getText().toString()) > getAvailableAmountSubheadLimitsMOnthly) {
                i++;
                ShowToast("Your amount is greater than subhead monthly limit");
            }
        } else if (rb_advance.isChecked()) {
            if (Double.parseDouble(et_amount.getText().toString()) > getAvailableAmountSubheadLimitsMOnthly) {
                i++;
                ShowToast("Your amount is greater than subhead monthly limit");
            }
        }

        if (rb_bank.isChecked()) {
            if(Double.parseDouble(et_amount.getText().toString())>0) {
                if (Double.parseDouble(et_amount.getText().toString()) > valueCashinbank - valueSalary) {
                    ShowToast("you have insufficient amount in your bank");
                    i++;
                }
            } else {
                ShowToast("please enter valid amount");
                i++;
            }
        }
        if (rb_cashinhand.isChecked()) {
            if(Double.parseDouble(et_amount.getText().toString())>0) {
                if (Double.parseDouble(et_amount.getText().toString()) > valueCashinhand) {
                    ShowToast("you have insufficient amount in your cash in hand");
                    i++;
                }
            } else {
                ShowToast("please enter valid amount");
                i++;
            }
        }
        return i == 0;
    }

    private void onCheckedChangePettyCash(){
        ClearImagesBothReceiptAndCheck();

        //Jv no edittext shows move to next options
        et_jvno.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        //Amount Edittext Editable
        et_amount.setKeyListener(et_amount_listener);

        //Blank All fields and Spinners when change radio buttons
        blankAllEdittext();
        sp_expensehead.setSelection(0);
        sp_employee.setSelection(0);

        ll_employee_sp.setVisibility(View.GONE);

        ll_expensehead_sp.setVisibility(View.GONE);
        ll_expense_subheadlimit.setVisibility(View.GONE);

        ll_expenseDate.setVisibility(View.GONE);
        ll_jvno.setVisibility(View.GONE);
        //btn_submit.setVisibility(View.GONE);
        ll_amount.setVisibility(View.GONE);

        rg_cash.setVisibility(View.VISIBLE);
        mid_layout_pettycash.setVisibility(View.VISIBLE);



        //receipt heading and images Gone
        txt_attachment.setVisibility(View.GONE);
        ll_receipt_images.setVisibility(View.GONE);

        llcheckno.setVisibility(View.GONE);
        ll_depositslipno.setVisibility(View.GONE);

        totalamount = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,1) + ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,2);

        tv_expensetype.setText("PETTY CASH REMAINING");
        tv_expensetype_amount.setText(AppModel.getInstance().formatNumberInCommas((long) totalamount));

        rg_cash.clearCheck();
    }

    private void onCheckedChangeAdvance(){
        ClearImagesBothReceiptAndCheck();

        //Jv no edittext shows move to next options
        et_jvno.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        //Amount Edittext not Editable
        et_amount.setKeyListener(et_amount_listener);

        //Blank All fields and Spinners when change radio buttons
        blankAllEdittext();
        sp_expensehead.setSelection(0);
        sp_employee.setSelection(0);

        ll_employee_sp.setVisibility(View.GONE);
        ll_expensehead_sp.setVisibility(View.GONE);
        ll_expense_subheadlimit.setVisibility(View.GONE);
        ll_expenseDate.setVisibility(View.GONE);
        ll_jvno.setVisibility(View.GONE);
        ll_amount.setVisibility(View.GONE);

        rg_cash.setVisibility(View.VISIBLE);

        llcheckno.setVisibility(View.GONE);
        ll_depositslipno.setVisibility(View.GONE);

        //receipt heading and images Gone
        txt_attachment.setVisibility(View.GONE);
        ll_receipt_images.setVisibility(View.GONE);


        tv_expensetype.setText("ADVANCE REMAINING");
        //TODO
        totalamount = (int) (ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,1) + ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,2));
        tv_expensetype_amount.setText(AppModel.getInstance().formatNumberInCommas((long) totalamount));

        rg_cash.clearCheck();

    }

    private void onCheckedChangeSalary(){
        populateEmployeeSpinner();

        ClearImagesBothReceiptAndCheck();

        //Jv no edittext shows Done option
        et_jvno.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //Amount Edittext not Editable
        et_amount.setTag(et_amount.getKeyListener());
        et_amount.setKeyListener(null);

        //Blank All fields and Spinners when change radio buttons
        blankAllEdittext();
        sp_expensehead.setSelection(0);
        sp_employee.setSelection(0);

        ll_employee_sp.setVisibility(View.VISIBLE);

        ll_expensehead_sp.setVisibility(View.GONE);
        ll_expense_subheadlimit.setVisibility(View.GONE);
        ll_expenseDate.setVisibility(View.VISIBLE);
        ll_jvno.setVisibility(View.VISIBLE);
        ll_amount.setVisibility(View.VISIBLE);
        txt_attachment.setVisibility(View.VISIBLE);
        ll_receipt_images.setVisibility(View.VISIBLE);
        llcheckno.setVisibility(View.VISIBLE);
        ll_depositslipno.setVisibility(View.GONE);

        rg_cash.setVisibility(View.GONE);



        amt_salary = ExpenseHelperClass.getInstance(getActivity()).getTotalSalary(schoolId);
        tv_expensetype.setText("SALARY REMAINING");
        tv_expensetype_amount.setText(AppModel.getInstance().formatNumberInCommas((long) amt_salary));

        rg_cash.clearCheck();

    }


    private void onCheckedChangeCashInHand(){

        if (rb_cashinhand.isChecked()) {

            ClearImagesBothReceiptAndCheck();

            //Blank All fields and Spinners when change radio buttons
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            sp_employee.setSelection(0);

            ll_expensehead_sp.setVisibility(View.VISIBLE);
            ll_expense_subheadlimit.setVisibility(View.GONE);
            ll_expenseDate.setVisibility(View.VISIBLE);

            ll_jvno.setVisibility(View.VISIBLE);
            ll_amount.setVisibility(View.VISIBLE);
            txt_attachment.setVisibility(View.VISIBLE);
            ll_receipt_images.setVisibility(View.VISIBLE);
            llcheckno.setVisibility(View.GONE);
            ll_depositslipno.setVisibility(View.VISIBLE);

            valueCashinhand = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,2);
            tv_expensetype.setText("CASH IN HAND");
            tv_expensetype_amount.setText(AppModel.getInstance().formatNumberInCommas((long) valueCashinhand));
        }

        if (rb_pettycash.isChecked()) {
            if (rb_cashinhand.isChecked()) {
                populateExpenseSubheads(schoolId, pettycash_id, allowedFromCash);
                ll_expensehead_sp.setVisibility(View.VISIBLE);
            }
        }
        if (rb_advance.isChecked()) {
            if (rb_cashinhand.isChecked()) {
                populateExpenseSubheads(schoolId, advance_id, allowedFromCash);
                ll_expensehead_sp.setVisibility(View.VISIBLE);

            }
        }
        allowedfrom = "Cash";
    }

    private void onCheckedChangeCashInBank(){
        if (rb_bank.isChecked()) {

            ClearImagesBothReceiptAndCheck();

            //Blank All fields and Spinners when change radio buttons
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            ll_expense_subheadlimit.setVisibility(View.GONE);
            sp_employee.setSelection(0);

            txt_attachment.setVisibility(View.VISIBLE);
            ll_receipt_images.setVisibility(View.VISIBLE);
            ll_expenseDate.setVisibility(View.VISIBLE);
            ll_jvno.setVisibility(View.VISIBLE);
            ll_amount.setVisibility(View.VISIBLE);
            //ll_receipt_images.setVisibility(View.GONE);
            llcheckno.setVisibility(View.VISIBLE);
            ll_depositslipno.setVisibility(View.GONE);

            valueCashinbank = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,1);
            tv_expensetype.setText("CASH IN BANK");
            tv_expensetype_amount.setText(AppModel.getInstance().formatNumberInCommas((long) valueCashinbank));

            allowedfrom = "Bank";
        }

        if (rb_pettycash.isChecked()) {
            if (rb_bank.isChecked()) {
                populateExpenseSubheads(schoolId, pettycash_id, allowedFromBank);
                ll_expensehead_sp.setVisibility(View.VISIBLE);
            }
        }
        if (rb_advance.isChecked()) {
            if (rb_bank.isChecked()) {
                populateExpenseSubheads(schoolId, advance_id, allowedFromBank);
                ll_expensehead_sp.setVisibility(View.VISIBLE);
            }
        }
    }

    // TODO: Rename and change types and number of parameters
    public static AddExpenseFragment newInstance(String param) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.KEY_EXPENSE_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

}
