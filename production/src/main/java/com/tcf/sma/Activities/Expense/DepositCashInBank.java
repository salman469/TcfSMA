package com.tcf.sma.Activities.Expense;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.DrawerActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DepositCashInBank extends DrawerActivity implements AdapterView.OnItemSelectedListener{


    View view;
    private List<SchoolModel> schoolModels;
    private Spinner spinner_SelectSchool;
    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private ImageView iv_delete;
    private AppCompatImageView ivcheckimage;
    private RelativeLayout rl_checkimage,rl_addcheckimage;
    private Button btn_submit;
    private AppCompatEditText et_jvno,et_depositslipno,et_amount;
    private Boolean ischeckimage = false;
    private TextView txt_currentdate;
    private DatePickerDialog mDatePickerDialog;
    private String strDate,_pickedDate;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().hasExtra("correction")){
            view = setActivityLayout(this, R.layout.demo1); //60,000
            setToolbar("Deposit Cash Correction", this, false);
        }
        else{
            view = setActivityLayout(this, R.layout.activity_deposit_cash_in_bank);//20,000
            setToolbar("Cash Deposit in bank", this, false);
        }
        value = 20000;
        init(view);
        setInitialDate();
        populateSpinners();

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
                    PickImageDialog.build(new PickSetup().setTitle("Separation Attachments")
                            .setPickTypes(EPickType.CAMERA).setSystemDialog(true)).show(DepositCashInBank.this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult pickResult) {
                            if (pickResult.getError() == null) {
                                rl_checkimage.setVisibility(View.VISIBLE);
                                ImageCompression imageCompression = new ImageCompression(DepositCashInBank.this);
                                String path = pickResult.getPath();
                                imageCompression.execute(path);
                                ivcheckimage.setImageBitmap(pickResult.getBitmap());
                                rl_addcheckimage.setVisibility(View.GONE);
                                ischeckimage = true;
                            } else {
                                Toast.makeText(DepositCashInBank.this, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
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
                if (!et_amount.getText().toString().equals("")) {
                    if (Integer.parseInt(et_amount.getText().toString()) <= value) {
                        Log.d("amountxDD",""+Integer.parseInt(et_amount.getText().toString()));
                    } else {
                        et_amount.setError("you have insufficient balance in your account");
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

                if(txt_currentdate.getText().toString().equals("")){
                    ShowToast("Please select date");
                    return;
                }
                if(et_jvno.getText().toString().equals("")){
                    ShowToast("Please enter J.V no");
                    return;
                }
                if(et_depositslipno.getText().toString().equals("")){
                    ShowToast("Please enter deposit no");
                    return;
                }

                if(!et_amount.getText().toString().equals("")) {
                    int amount = Integer.parseInt(et_amount.getText().toString());
                    if (amount <= value) {
                        if(!ischeckimage){
                            ShowToast("Please insert image");
                            return;
                        } else {
                            finish();
                        }
                    } else {
                        if(!ischeckimage){
                            ShowToast("Please insert image");
                            return;
                        }
                    }
                } else {
                    ShowToast("Please enter amount");
                }
            }
        });

        txt_currentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datepicker(R.id.txt_currentdate);
            }
        });
    }

    private void init(View view){
        et_jvno = view.findViewById(R.id.tv_jvno);
        et_depositslipno = view.findViewById(R.id.et_depositslipno);
        et_amount = view.findViewById(R.id.tv_amount);
        txt_currentdate = view.findViewById(R.id.txt_currentdate);
        rl_addcheckimage = view.findViewById(R.id.rl_addcheckimage);
        ivcheckimage = view.findViewById(R.id.ivcheckimage);
        iv_delete = view.findViewById(R.id.iv_delete);
        rl_checkimage = view.findViewById(R.id.rl_checkimage);
        btn_submit = view.findViewById(R.id.btn_submit);

        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
    }

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

        if(DatabaseHelper.getInstance(DepositCashInBank.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V ||
                DatabaseHelper.getInstance(DepositCashInBank.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM) {
            spinner_SelectSchool.setEnabled(false);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(DepositCashInBank.this, previewImageFile);
        imagePreviewDialog.show();
    }

    private void Datepicker(int id) {


        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
            strDate = format.format(calendar.getTime());
            if(id == R.id.txt_currentdate)
                txt_currentdate.setText(strDate);

            String _year = String.valueOf(year);
            String _month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            _pickedDate = _date + "-" + _month + "-" + _year;

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        if (id == R.id.txt_currentdate){
            mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 15*24*60*60*1000);
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        mDatePickerDialog.show();
    }

    private void setInitialDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        String formattedDate = df.format(c);
        txt_currentdate.setText(formattedDate);
    }

}
