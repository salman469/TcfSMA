package com.tcf.sma.Activities.Expense;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.ImageCompression;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddExpense extends DrawerActivity implements RadioGroup.OnCheckedChangeListener , AdapterView.OnItemSelectedListener{

    View view;
    RadioGroup rg_expensetype,rg_cash;
    RadioButton rb_pettycash,rb_advance,rb_salary,rb_cashinhand,rb_bank;
    TextView tv_expensetype_amount,tv_expensetype;
    LinearLayout llcheckimagesfield,llcheckno,ll_checkimages,mid_layout_pettycash,ll_balance_advance,
            ll_lastclosing_advance,
            ll_receipt_heading,ll_receipt_images,
            ll_employee_sp,ll_employee_code,ll_expensehead_sp,
            ll_amount,ll_jvno,ll_expenseDate,ll_Attachments_sp;

    private List<SchoolModel> schoolModels;
    private Spinner spinner_SelectSchool,sp_expensehead,sp_employee,sp_employeecode,sp_attachments;
    private List<String> expenseheads,employees,employeecode, Images;
    private int schoolId = 0,expenseheadId = 0,employeeId = 0,employeecodeId = 0,attachment = 0,amt_pettycash = 45000, amt_pettycash_cashIhand =5000,amt_pettycash_bank=40000,
    amt_advance = 35000, amt_advance_cashInhand =15000,amt_advance_bank=20000,
    amt_salary = 40000;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private ArrayAdapter<String> ExpenseAdapter,EmployeeAdapter,EmployeeCodeAdapter,ImagesAdapter;
    private RecyclerView rv_receipt_images;
    private ExpenseAttachmentsAdapter expenseAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private ImageView iv_delete;
    private AppCompatImageView ivcheckimage;
    private AppCompatEditText et_amount,et_jvno,et_checkno;
    private KeyListener et_amount_listener;
    private RelativeLayout rl_checkimage,rl_addcheckimage;
    private Button btn_submit;
    private Boolean ischeckimage = false;
    private TextView txt_ExpenseDate;
    private DatePickerDialog mDatePickerDialog;
    private String strDate,_pickedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_add_expense);
        setToolbar("Add Expense", this, false);
        init(view);
        populateSpinners();
        setInitialDate();

        txt_ExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datepicker(R.id.txt_ExpenseDate);
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_checkimage.setVisibility(View.GONE);
                rl_addcheckimage.setVisibility(View.VISIBLE);
                ischeckimage = false;
            }
        });

        ivcheckimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage(ivcheckimage.getDrawable());
            }
        });

        rl_addcheckimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PickImageDialog.build(new PickSetup().setTitle("Attachments")
                            .setPickTypes(EPickType.CAMERA).setSystemDialog(true)).show(AddExpense.this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult pickResult) {
                            if (pickResult.getError() == null) {
                                rl_checkimage.setVisibility(View.VISIBLE);
                                ImageCompression imageCompression = new ImageCompression(AddExpense.this);
                                String path = pickResult.getPath();
                                imageCompression.execute(path);
                                ivcheckimage.setImageBitmap(pickResult.getBitmap());
                                rl_addcheckimage.setVisibility(View.GONE);
                                ischeckimage = true;
                            } else {
                                Toast.makeText(AddExpense.this, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(rg_cash.getVisibility() == View.VISIBLE) {
                    if (rb_pettycash.isChecked()) {
                        if (rb_cashinhand.isChecked()) {

                            if (!et_amount.getText().toString().equals("")) {
                                if (Integer.parseInt(et_amount.getText().toString()) <= (amt_pettycash - amt_pettycash_bank)) {
                                    Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                                    //finish();
                                } else {
                                    //ShowToast("you have insufficient balance in your account");
                                    et_amount.setError("you have insufficient balance in your account");
                                    //finish();
                                }
                            }
                        } else if (rb_bank.isChecked()) {
                            if (!et_amount.getText().toString().equals("")) {
                                if (Integer.parseInt(et_amount.getText().toString()) <= (amt_pettycash - amt_pettycash_cashIhand)) {
                                    Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                                    //finish();
                                } else {
                                    et_amount.setError("you have insufficient balance in your account");
                                    ///ShowToast("you have insufficient balance in your account");
                                    //finish();
                                }
                            }
                        } else {
                            et_amount.setError("Please select from cash in hand or bank first");
                        }
                    }
                    if (rb_advance.isChecked()) {
                        if (rb_cashinhand.isChecked()) {
                            if (!et_amount.getText().toString().equals("")) {
                                if (Integer.parseInt(et_amount.getText().toString()) <= (amt_advance - amt_advance_bank)) {
                                    Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                                    //finish();
                                } else {
                                    //ShowToast("you have insufficient balance in your account");
                                    et_amount.setError("you have insufficient balance in your account");
                                    //finish();
                                }
                            }
                        } else if (rb_bank.isChecked()) {
                            if (!et_amount.getText().toString().equals("")) {
                                if (Integer.parseInt(et_amount.getText().toString()) <= (amt_advance - amt_advance_cashInhand)) {
                                    Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                                    //finish();
                                } else {
                                    et_amount.setError("you have insufficient balance in your account");
                                    ///ShowToast("you have insufficient balance in your account");
                                    //finish();
                                }
                            }
                        } else {
                            et_amount.setError("Please select from cash in hand or bank first");
                        }
                    }
                    if (rb_salary.isChecked()) {
                        if (!et_amount.getText().toString().equals("")) {
                            if (Integer.parseInt(et_amount.getText().toString())<= amt_salary) {
                                Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                                //finish();
                            } else {
                                et_amount.setError("you have insufficient balance in your account");
                                ///ShowToast("you have insufficient balance in your account");
                                //finish();
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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_salary.isChecked()){
                    if(employeeId<=0){
                        ShowToast("Please select employee");
                        return;
                    }
                    if(employeecodeId<=0){
                        ShowToast("Please select employee code");
                        return;
                    }
                } else {
                    if (expenseheadId <= 0) {
                        ShowToast("Please select expense head");
                        return;
                    }
                }

                if(et_jvno.getText().toString().equals("")){
                    ShowToast("Please enter J.V no");
                    return;
                }
                 if(!et_amount.getText().toString().equals("")) {
                    int amount = Integer.parseInt(et_amount.getText().toString());
                    if (rb_pettycash.isChecked()) {
                        if (rb_cashinhand.isChecked()) {
                            if(attachment<=0){
                                ShowToast("Please select attachments");
                                return;
                            } else if(attachment==1){
                                if(!ischeckimage){
                                    ShowToast("Please insert check image");
                                    return;
                                }
                            } else if(attachment==2){
                                if(ExpenseAttachmentsAdapter.paths.size()<=0){
                                    ShowToast("Please insert receipt image");
                                    return;
                                }
                            }
                            /*if(ReceiptImagesAdapter.paths.size()<=0){
                                ShowToast("Please insert image");
                                return;
                            }*/
                            if (amount <= (amt_pettycash - amt_pettycash_cashIhand)) {
                                finish();
                            }
                        }
                        else if (rb_bank.isChecked()) {
                            if(attachment<=0){
                                ShowToast("Please select attachments");
                                return;
                            } else if(attachment==1){
                                if(!ischeckimage){
                                    ShowToast("Please insert check image");
                                    return;
                                }
                            } else if(attachment==2){
                                if(ExpenseAttachmentsAdapter.paths.size()<=0){
                                    ShowToast("Please insert receipt image");
                                    return;
                                }
                            }
                           /* if(!ischeckimage){
                                ShowToast("Please insert image");
                                return;
                            }*/
                            if (amount <= (amt_pettycash - amt_pettycash_bank)) {
                                finish();
                            }
                        }
                        else {
                            ShowToast("Please select cash in hand or bank");
                        }
                    }
                    if (rb_advance.isChecked()) {
                        if (rb_cashinhand.isChecked()) {
                            if(attachment<=0){
                                ShowToast("Please select attachments");
                                return;
                            } else if(attachment==1){
                                if(!ischeckimage){
                                    ShowToast("Please insert check image");
                                    return;
                                }
                            } else if(attachment==2){
                                if(ExpenseAttachmentsAdapter.paths.size()<=0){
                                    ShowToast("Please insert receipt image");
                                    return;
                                }
                            }
                           /* if(ReceiptImagesAdapter.paths.size()<=0){
                                ShowToast("Please insert image");
                                return;
                            }*/
                            if (amount <= (amt_advance - amt_advance_bank)) {
                                finish();
                            }
                        } else if (rb_bank.isChecked()) {
                            if(attachment<=0){
                                ShowToast("Please select attachments");
                                return;
                            } else if(attachment==1){
                                if(!ischeckimage){
                                    ShowToast("Please insert check image");
                                    return;
                                }
                            } else if(attachment==2){
                                if(ExpenseAttachmentsAdapter.paths.size()<=0){
                                    ShowToast("Please insert receipt image");
                                    return;
                                }
                            }
                           /* if(!ischeckimage){
                                ShowToast("Please insert image");
                                return;
                            }*/
                            if (amount <= (amt_advance - amt_advance_cashInhand)) {
                                finish();
                            } else {
                                ShowToast("you have insufficient balance in your account");
                                finish();
                            }
                        }
                        else {
                            ShowToast("Please select cash in hand or bank");
                        }
                    }
                    if (rb_salary.isChecked()) {
                        if(attachment<=0){
                            ShowToast("Please select attachments");
                            return;
                        } else if(attachment==1){
                            if(!ischeckimage){
                                ShowToast("Please insert check image");
                                return;
                            }
                        } else if(attachment==2){
                            if(ExpenseAttachmentsAdapter.paths.size()<=0){
                                ShowToast("Please insert receipt image");
                                return;
                            }
                        }
                        if (amount <= amt_salary) {
                            finish();
                        } /*else {
                            ShowToast("you have insufficient balance in your account");
                            finish();
                        }*/
                    }
                } else {
                    ShowToast("Please insert amount");
                }


            }
        });
    }

    private void init(View view){
        txt_ExpenseDate = view.findViewById(R.id.txt_ExpenseDate);
        et_jvno = view.findViewById(R.id.tv_jvno);
        et_amount = view.findViewById(R.id.tv_amount);
        et_amount_listener = et_amount.getKeyListener();
        et_checkno = view.findViewById(R.id.tv_checkno);
        ll_employee_sp = view.findViewById(R.id.ll_employee_sp);
        ll_Attachments_sp = view.findViewById(R.id.ll_Attachments_sp);
        ll_employee_code = view.findViewById(R.id.ll_employee_code);
        ll_expensehead_sp = view.findViewById(R.id.ll_expensehead_sp);
        ll_jvno = view.findViewById(R.id.ll_jvno);
        ll_expenseDate = view.findViewById(R.id.ll_expenseDate);
        ll_amount = view.findViewById(R.id.ll_amount);

        sp_expensehead = view.findViewById(R.id.sp_expensehead);
        sp_expensehead.setOnItemSelectedListener(this);
        sp_employee = view.findViewById(R.id.sp_employee);
        sp_employee.setOnItemSelectedListener(this);
        sp_attachments = view.findViewById(R.id.sp_attachments);
        sp_attachments.setOnItemSelectedListener(this);
        sp_employeecode = view.findViewById(R.id.sp_employeecode);
        sp_employeecode.setOnItemSelectedListener(this);

        btn_submit = view.findViewById(R.id.btn_submit);
        rl_addcheckimage = view.findViewById(R.id.rl_addcheckimage);
        ivcheckimage = view.findViewById(R.id.ivcheckimage);
        iv_delete = view.findViewById(R.id.iv_delete);
        rl_checkimage = view.findViewById(R.id.rl_checkimage);

        rv_receipt_images = view.findViewById(R.id.rv_receipt_images);
        rv_receipt_images.setNestedScrollingEnabled(false);
        rv_receipt_images.setHasFixedSize(true);

        rv_receipt_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ExpenseAttachmentsAdapter.paths.clear();
        expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(false,this,attachments);
        rv_receipt_images.setAdapter(expenseAttachmentsAdapter);

        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
        mid_layout_pettycash = view.findViewById(R.id.mid_layout_pettycash);
        llcheckno = view.findViewById(R.id.llcheckno);
        llcheckimagesfield = view.findViewById(R.id.llcheckimagesfield);
        ll_checkimages = view.findViewById(R.id.ll_checkimages);
        ll_receipt_heading = view.findViewById(R.id.ll_receipt_heading);
        ll_receipt_images = view.findViewById(R.id.ll_receipt_images);
        tv_expensetype_amount = view.findViewById(R.id.tv_expensetype_amount);
        tv_expensetype = view.findViewById(R.id.tv_expensetype);
        rg_expensetype = view.findViewById(R.id.rg_expensetype);
        rg_expensetype.setOnCheckedChangeListener(this);
        rg_cash = view.findViewById(R.id.rg_cash);
        rg_cash.setOnCheckedChangeListener(this);
        ll_balance_advance = view.findViewById(R.id.ll_balance_advance);
        ll_lastclosing_advance = view.findViewById(R.id.ll_lastclosing_advance);
        rb_pettycash = view.findViewById(R.id.rb_pettycash);
        rb_advance = view.findViewById(R.id.rb_advance);
        rb_salary = view.findViewById(R.id.rb_salary);
        rb_cashinhand = view.findViewById(R.id.rb_cashinhand);
        rb_bank = view.findViewById(R.id.rb_bank);

        tv_expensetype_amount.setText(String.format("%,d",amt_pettycash));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_pettycash) {

            ClearImagesBothReceiptAndCheck();

            //Amount Edittext Editable
            et_amount.setKeyListener(et_amount_listener);

            //Blank All fields and Spinners when change radio buttons
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            sp_employee.setSelection(0);
            sp_employeecode.setSelection(0);


            ll_employee_sp.setVisibility(View.GONE);
            ll_employee_code.setVisibility(View.GONE);

            ll_expensehead_sp.setVisibility(View.GONE);
            ll_expenseDate.setVisibility(View.GONE);
            ll_jvno.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            ll_amount.setVisibility(View.GONE);

            rg_cash.setVisibility(View.VISIBLE);
            mid_layout_pettycash.setVisibility(View.VISIBLE);

            //Balance/Last Closing Remove
            ll_balance_advance.setVisibility(View.GONE);
            ll_lastclosing_advance.setVisibility(View.GONE);

            //Attachments Spinner gone
            ll_Attachments_sp.setVisibility(View.GONE);

            //receipt heading and images Gone
            ll_receipt_images.setVisibility(View.GONE);
            ll_receipt_heading.setVisibility(View.GONE);

            llcheckno.setVisibility(View.GONE);
            llcheckimagesfield.setVisibility(View.GONE);
            ll_checkimages.setVisibility(View.GONE);


            tv_expensetype.setText("PETTY CASH REMAINING");
            tv_expensetype_amount.setText(String.format("%,d",amt_pettycash));

            rg_cash.clearCheck();

        } else if (checkedId == R.id.rb_advance) {

            ClearImagesBothReceiptAndCheck();

            //Amount Edittext not Editable
            et_amount.setKeyListener(et_amount_listener);

            //Blank All fields and Spinners when change radio buttons
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            sp_employee.setSelection(0);
            sp_employeecode.setSelection(0);


            ll_employee_sp.setVisibility(View.GONE);
            ll_employee_code.setVisibility(View.GONE);

            ll_expensehead_sp.setVisibility(View.GONE);
            ll_expenseDate.setVisibility(View.GONE);
            ll_jvno.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            ll_amount.setVisibility(View.GONE);

            rg_cash.setVisibility(View.VISIBLE);

            llcheckno.setVisibility(View.GONE);
            llcheckimagesfield.setVisibility(View.GONE);
            ll_checkimages.setVisibility(View.GONE);

            //Attachments Spinner gone
            ll_Attachments_sp.setVisibility(View.GONE);

            //receipt heading and images Gone
            ll_receipt_images.setVisibility(View.GONE);
            ll_receipt_heading.setVisibility(View.GONE);


            tv_expensetype.setText("ADVANCE REMAINING");
            tv_expensetype_amount.setText(String.format("%,d",amt_advance));

            rg_cash.clearCheck();

        } else if (checkedId == R.id.rb_salary) {

            ClearImagesBothReceiptAndCheck();

            //Amount Edittext not Editable
            et_amount.setTag(et_amount.getKeyListener());
            et_amount.setKeyListener(null);

            //Blank All fields and Spinners when change radio buttons
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            sp_employee.setSelection(0);
            sp_attachments.setSelection(0);
            sp_employeecode.setSelection(0);

            ll_employee_sp.setVisibility(View.VISIBLE);
            ll_employee_code.setVisibility(View.VISIBLE);

            ll_expensehead_sp.setVisibility(View.GONE);
            ll_expenseDate.setVisibility(View.VISIBLE);
            ll_jvno.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
            ll_amount.setVisibility(View.VISIBLE);

            rg_cash.setVisibility(View.GONE);

            //Balance/Last Closing Remove
            ll_balance_advance.setVisibility(View.GONE);
            ll_lastclosing_advance.setVisibility(View.GONE);

            //Attachments Spinner gone
            ll_Attachments_sp.setVisibility(View.VISIBLE);

            hideImagesLayout();

            tv_expensetype.setText("SALARY REMAINING");
            tv_expensetype_amount.setText(String.format("%,d",amt_salary));

            rg_cash.clearCheck();

        } else if(checkedId == R.id.rb_cashinhand) {
            if(rb_cashinhand.isChecked()) {

                ClearImagesBothReceiptAndCheck();

                //Blank All fields and Spinners when change radio buttons
                blankAllEdittext();
                sp_expensehead.setSelection(0);
                sp_employee.setSelection(0);
                sp_attachments.setSelection(0);
                sp_employeecode.setSelection(0);


                ll_expensehead_sp.setVisibility(View.VISIBLE);
                ll_expenseDate.setVisibility(View.VISIBLE);
                ll_jvno.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                ll_amount.setVisibility(View.VISIBLE);

                //Attachments Spinner visible
                ll_Attachments_sp.setVisibility(View.VISIBLE);

               /* llcheckno.setVisibility(View.GONE);
                llcheckimagesfield.setVisibility(View.GONE);
                ll_checkimages.setVisibility(View.GONE);

                //receipt heading and images Gone
                ll_receipt_images.setVisibility(View.VISIBLE);
                ll_receipt_heading.setVisibility(View.VISIBLE);*/
            }

            if(rb_pettycash.isChecked()){
                if(rb_cashinhand.isChecked())
                    tv_expensetype_amount.setText(String.format("%,d",amt_pettycash- amt_pettycash_bank));
            }
            if(rb_advance.isChecked()){
                if(rb_cashinhand.isChecked()) {
                    tv_expensetype_amount.setText(String.format("%,d",amt_advance - amt_advance_bank));
                    //Balance/Last Closing Visible for Advance
                    ll_balance_advance.setVisibility(View.VISIBLE);
                    ll_lastclosing_advance.setVisibility(View.VISIBLE);

                }
            }

        } else if(checkedId == R.id.rb_bank) {
            if(rb_bank.isChecked()) {

                ClearImagesBothReceiptAndCheck();

                //Blank All fields and Spinners when change radio buttons
                blankAllEdittext();
                sp_expensehead.setSelection(0);
                sp_employee.setSelection(0);
                sp_attachments.setSelection(0);
                sp_employeecode.setSelection(0);


                ll_expensehead_sp.setVisibility(View.VISIBLE);
                ll_expenseDate.setVisibility(View.VISIBLE);
                ll_jvno.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                ll_amount.setVisibility(View.VISIBLE);

                //Attachments Spinner visible
                ll_Attachments_sp.setVisibility(View.VISIBLE);

               /* llcheckno.setVisibility(View.VISIBLE);
                llcheckimagesfield.setVisibility(View.VISIBLE);
                ll_checkimages.setVisibility(View.VISIBLE);

                //receipt heading and images Gone
                ll_receipt_images.setVisibility(View.GONE);
                ll_receipt_heading.setVisibility(View.GONE);*/
            }

            if(rb_pettycash.isChecked()){
                if(rb_bank.isChecked())
                    tv_expensetype_amount.setText(String.format("%,d",amt_pettycash- amt_pettycash_cashIhand));
            }
            if(rb_advance.isChecked()){

                if(rb_bank.isChecked()) {
                    tv_expensetype_amount.setText(String.format("%,d",amt_advance - amt_advance_cashInhand));
                    //Balance/Last Closing Visible for Advance
                    ll_balance_advance.setVisibility(View.VISIBLE);
                    ll_lastclosing_advance.setVisibility(View.VISIBLE);
                }
            }
        }
    }

 /*   private void setInitialDate() {
        calendar = Calendar.getInstance();
        c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.US);
        String formattedDate = df.format(c);
        tv_date.setText(formattedDate);
    }
*/
    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForEmployeeListing();
        if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectSchool.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            //If select school with id 0 is selected show any school by default
            if (schoolModels != null && schoolModels.size() > 1 &&
                    schoolModels.get(indexOfSelectedSchool).getName().equals(getResources().getString(R.string.select_school))){
                spinner_SelectSchool.setSelection(indexOfSelectedSchool + 1);
            }else {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool);
            }
        }

        if(DatabaseHelper.getInstance(AddExpense.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V ||
                DatabaseHelper.getInstance(AddExpense.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM) {
            spinner_SelectSchool.setEnabled(false);
        }

            expenseheads = new ArrayList<>();
            expenseheads.add(0, "Select Expense Head");
            expenseheads.add(1, "School Party");
            expenseheads.add(2, "Plumber Work");
            expenseheads.add(3, "Gas Bill");
            expenseheads.add(4, "Electricity Bill");
            expenseheads.add(5, "Water Bill");

            ExpenseAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout_black, expenseheads);
            ExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_expensehead.setAdapter(ExpenseAdapter);
            sp_expensehead.setSelection(0);


            employees = new ArrayList<>();
            employees.add(0, "Select Employees");
            employees.add(1, "Wahaj");
            employees.add(2, "Asad");
            employees.add(3, "Usama");
            employees.add(4, "Moiz");
            employees.add(5, "Basit");

            EmployeeAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout_black, employees);
            EmployeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_employee.setAdapter(EmployeeAdapter);
            sp_employee.setSelection(0);

        employeecode = new ArrayList<>();
        employeecode.add(0, "Select Code");
        employeecode.add(1, "675087");
        employeecode.add(2, "625245");
        employeecode.add(3, "624785");
        employeecode.add(4, "644545");
        employeecode.add(5, "600009");

        EmployeeCodeAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout_black, employeecode);
        EmployeeCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_employeecode.setAdapter(EmployeeCodeAdapter);
        sp_employeecode.setSelection(0);


        Images = new ArrayList<>();
        Images.add(0, "Select Attachment Category");
        Images.add(1, "Check");
        Images.add(2, "Receipt");


        ImagesAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout_black, Images);
        ImagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_attachments.setAdapter(ImagesAdapter);
        sp_attachments.setSelection(0);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
        }
        if (adapterView.getId() == R.id.sp_employee) {
            employeeId = position;
            blankAllEdittext();
            sp_expensehead.setSelection(0);
            switch (employeeId){
                case 0:
                    et_amount.setText("");
                    break;
                case 1:
                    et_amount.setText(String.format("%,d",40000));
                    break;
                case 2:
                    et_amount.setText(String.format("%,d",50000));
                    break;
                case 3:
                    et_amount.setText(String.format("%,d",25000));
                    break;
                case 4:
                    et_amount.setText(String.format("%,d",20000));
                    break;
                case 5:
                    et_amount.setText(String.format("%,d",35000));
                    break;
            }
        }
        if (adapterView.getId() == R.id.sp_expensehead) {
            expenseheadId =  position;
            blankAllEdittext();
            sp_employee.setSelection(0);
            hideImagesLayout();
        }
        if (adapterView.getId() == R.id.sp_attachments) {
            attachment = position;
            //blankAllEdittext();
            //sp_expensehead.setSelection(0);
            switch (attachment){
                case 0:
                    hideImagesLayout();
                    break;
                case 1:
                    //check heading and images Gone
                    llcheckno.setVisibility(View.VISIBLE);
                    llcheckimagesfield.setVisibility(View.VISIBLE);
                    ll_checkimages.setVisibility(View.VISIBLE);
                    //receipt heading and images Gone
                    ll_receipt_images.setVisibility(View.GONE);
                    ll_receipt_heading.setVisibility(View.GONE);
                    break;
                case 2:
                    llcheckno.setVisibility(View.GONE);
                    llcheckimagesfield.setVisibility(View.GONE);
                    ll_checkimages.setVisibility(View.GONE);
                    //receipt heading and images Gone
                    ll_receipt_images.setVisibility(View.VISIBLE);
                    ll_receipt_heading.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void hideImagesLayout(){
        //check heading and images Gone
        llcheckno.setVisibility(View.GONE);
        llcheckimagesfield.setVisibility(View.GONE);
        ll_checkimages.setVisibility(View.GONE);
        //receipt heading and images Gone
        ll_receipt_images.setVisibility(View.GONE);
        ll_receipt_heading.setVisibility(View.GONE);
    }

    private void ClearImagesBothReceiptAndCheck(){
        ExpenseAttachmentsAdapter.paths.clear();
        attachments.clear();
        expenseAttachmentsAdapter.notifyDataSetChanged();

        rl_checkimage.setVisibility(View.GONE);
        rl_addcheckimage.setVisibility(View.VISIBLE);
        ischeckimage = false;
    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(AddExpense.this, previewImageFile);
        imagePreviewDialog.show();
    }

    public void ShowToast(String message) {
        Toast.makeText(AddExpense.this, message, Toast.LENGTH_SHORT).show();
    }


    private void blankAllEdittext(){
        et_jvno.setText("");
        et_amount.setText("");
        et_amount.setError(null);
        et_checkno.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().hasExtra("expense_type")){
            String expense_type = getIntent().getStringExtra("expense_type");
            if(expense_type.equals("pcash")) {
                rb_pettycash.setChecked(true);
                rb_advance.setVisibility(View.GONE);
                rb_salary.setVisibility(View.GONE);
                rb_pettycash.setBackground(ContextCompat.getDrawable(AddExpense.this, R.drawable.radio_curve_selector));
                // rb_advance.setBackground(ContextCompat.getDrawable(AddExpense.this, R.drawable.radio_curve_selector));

            }
            else if (expense_type.equals("salary")) {
                rb_salary.setChecked(true);
                rb_pettycash.setVisibility(View.GONE);
                rb_advance.setVisibility(View.GONE);
                rb_salary.setBackground(ContextCompat.getDrawable(AddExpense.this, R.drawable.radio_curve_selector));

            }
            else if(expense_type.equals("advance")) {
                rb_advance.setChecked(true);
                rb_pettycash.setVisibility(View.GONE);
                rb_salary.setVisibility(View.GONE);
                rb_advance.setBackground(ContextCompat.getDrawable(AddExpense.this, R.drawable.radio_curve_selector));

            }

        }
    }

    private void setInitialDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        String formattedDate = df.format(c);
        txt_ExpenseDate.setText(formattedDate);
    }

    private void Datepicker(int id) {


        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
            strDate = format.format(calendar.getTime());
            if(id == R.id.txt_ExpenseDate)
                txt_ExpenseDate.setText(strDate);

          /*  if(id == R.id.tv_end_date)
                tv_end_date.setText(strDate);*/

            String _year = String.valueOf(year);
            String _month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            _pickedDate = _date + "-" + _month + "-" + _year;

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        if (id == R.id.txt_ExpenseDate){
            //mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 15*24*60*60*1000);
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        /*if(id == R.id.tv_end_date){
            mDatePickerDialog.getDatePicker().setMinDate(milliseconds(tv_start_date.getText().toString()));
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }*/
        mDatePickerDialog.show();
    }

}
