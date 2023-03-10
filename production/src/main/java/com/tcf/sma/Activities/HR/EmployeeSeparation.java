package com.tcf.sma.Activities.HR;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HR.SeparationAttachmentsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Interfaces.OnButtonEnableDisableListener;
import com.tcf.sma.Managers.HR.EmployeeResignationDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EmployeeSeparation extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, OnButtonEnableDisableListener {
    private Button btn_Resign, btn_close, btn_update, btn_delete;
    private TextView lwdLabel, separationLabel, txt_ResStartDate, txt_ResDate, txt_ResName, txt_ResEmCode, tv_total_days, tv_totalWdays, res_letter_heading, noAttachmentFound;
    private RadioButton rb_resignation, rb_termination, rb_expired;
    private EditText cancelReason;
    private Spinner reason_spinner, subReason_spinner;
    private DatePickerDialog mDatePickerDialog;
    private List<CalendarsModel> OffdaysList;
    private LinearLayout update_delete_buttons, ll_next, ll_totalDays, ll_total_lwd, subReasonLayout, ll_resignDate;
    private Boolean uploaded = true;
    private RecyclerView rc_attachments;
    private SeparationAttachmentsAdapter separationAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private View view;
    private Date currentDate;
    private int empDetailId, schoolId, resignReasonID, resignTypeID;
    private String nfCurrentDate, sdfCurrentDate, _pickedDate, strDate, Resign_Letter_Path = null, Resign_Form_Path = null;
    private EmployeeModel employeeModel;
    private List<EmployeeResignReasonModel> reasonsList;
    private List<EmployeeResignReasonModel> subReasonsList;
    private ArrayAdapter<EmployeeResignTypeModel> sepTypeAdapter;
    private ArrayAdapter<EmployeeResignReasonModel> ReasonAdapter;
    private ArrayAdapter<EmployeeResignReasonModel> subReasonAdapter;
    private EmployeeSeparationModel empRM;
    private File image = null;
    private Uri mUri;
    private boolean letter = false, form = false;
    private long noOfDays, workingDays;
    private int offDaysFromCalendar;
    String lwp = "";
    int lwpValue = 0;
    EditText leaveWithoutPay, ResSubReason;
    TextView ResReaason, et_sepType;
    RadioGroup rg_separationType;
    long noticePeriodSD, noticePeriodED;
    SimpleDateFormat newFormat;
    private OnButtonEnableDisableListener onButtonEnableDisableListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_employee_separation);
        setToolbar("Employee Separation", this, false);
        init(view);
        working();
    }

    private void init(View view) {
        onButtonEnableDisableListener = this;
        newFormat = new SimpleDateFormat("dd-MMM-yy");
        separationLabel = view.findViewById(R.id.separationLabel);
        lwdLabel = view.findViewById(R.id.lwdLabel);


        subReasonLayout = view.findViewById(R.id.subReasonLayout);
        rb_resignation = view.findViewById(R.id.rb_resignation);
        rb_termination = view.findViewById(R.id.rb_termination);
        rb_expired = view.findViewById(R.id.rb_expired);
        noAttachmentFound = view.findViewById(R.id.noAttachmentsFound);
        rg_separationType = view.findViewById(R.id.rg_separationType);
        rg_separationType.setOnCheckedChangeListener(this);
        txt_ResName = view.findViewById(R.id.txt_ResName);
        ResReaason = view.findViewById(R.id.et_ResReason);
        ResSubReason = view.findViewById(R.id.et_ResSubReason);
        et_sepType = view.findViewById(R.id.et_SepType);
        res_letter_heading = view.findViewById(R.id.textView14);
        ll_total_lwd = view.findViewById(R.id.ll_total_lwd);
        ll_resignDate = view.findViewById(R.id.ll_resignDate);
        ll_totalDays = view.findViewById(R.id.ll_total_days);
        txt_ResEmCode = view.findViewById(R.id.txt_ResEmCode);
        ll_next = view.findViewById(R.id.next_ll);
        ll_next.setVisibility(View.GONE);
        txt_ResStartDate = view.findViewById(R.id.txt_ResStartDate);
        txt_ResDate = view.findViewById(R.id.txt_ResDate);
        tv_total_days = view.findViewById(R.id.txt_Total_Days);
        tv_totalWdays = view.findViewById(R.id.txt_Total_WDays);
        btn_Resign = view.findViewById(R.id.btn_Resign);
        btn_close =  view.findViewById(R.id.btn_close);
        btn_update = view.findViewById(R.id.Resign_Update);
        btn_delete = view.findViewById(R.id.Resign_Delete);
        leaveWithoutPay = view.findViewById(R.id.et_leaveWithoutPay);
        leaveWithoutPay.setText("");
        update_delete_buttons = view.findViewById(R.id.Update_Delete_Buttons);
        reason_spinner = view.findViewById(R.id.spin_ResReason);
        reason_spinner.setOnItemSelectedListener(this);
        subReason_spinner = view.findViewById(R.id.spin_ResSubReason);
        subReason_spinner.setOnItemSelectedListener(this);
        txt_ResDate.setOnClickListener(this);
        txt_ResStartDate.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_Resign.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        rc_attachments = view.findViewById(R.id.rv_attachments);
        rc_attachments.setNestedScrollingEnabled(false);
        rc_attachments.setHasFixedSize(true);
        rc_attachments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        SeparationAttachmentsAdapter.paths.clear();
        separationAttachmentsAdapter = new SeparationAttachmentsAdapter(false,this,attachments,0);
        rc_attachments.setAdapter(separationAttachmentsAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("empDetailId")) {
            empDetailId = intent.getIntExtra("empDetailId", 0);
            schoolId = intent.getIntExtra("schoolId", 0);
            employeeModel = EmployeeHelperClass.getInstance(view.getContext()).getEmployee(empDetailId);

            if (schoolId > 0) {
                OffdaysList = DatabaseHelper.getInstance(view.getContext()).getAllDatesForHolidaysForEmployee(schoolId + "");
            }
        }
        empRM = EmployeeHelperClass.getInstance(view.getContext()).getResignedEmployee(empDetailId);

    }

    private void setDates() {
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calender = Calendar.getInstance();

        currentDate = calender.getTime();
        nfCurrentDate = newFormat.format(currentDate);
        txt_ResDate.setText(nfCurrentDate);
        txt_ResStartDate.setText(nfCurrentDate);
        noticePeriodSD = System.currentTimeMillis();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_ResStartDate:
                Datepicker(R.id.txt_ResStartDate);
                break;

            case R.id.txt_ResDate:
                Datepicker(R.id.txt_ResDate);
                break;

            case R.id.btn_Resign:
                Separate(resignTypeID);
                break;

            case R.id.Resign_Update:
                if(leaveWithoutPay.getText() != null && !leaveWithoutPay.getText().toString().equals("")){
                    if(Integer.parseInt(leaveWithoutPay.getText().toString()) >= 0 && Integer.parseInt(leaveWithoutPay.getText().toString()) <=50)
                        updateResignatioon();
                } else {
                    ShowToast("Please Enter Leave Without Pay");
                }
                break;

            case R.id.Resign_Delete:
                cancelResignation();
                break;


            case R.id.btn_close:
                onBackPressed();
                break;
        }
    }

    private void cancelResignation() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.resignation_cancel_dialog, null);
        cancelReason = dialogView.findViewById(R.id.edt_comment);
        final EditText editText = dialogView.findViewById(R.id.edt_comment);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Cancel Reason")
                .setMessage("Are you sure you want to cancel the resignation of " + employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name())
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, (dialog1, which) -> dialog1.dismiss())
                .create();

        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                if (!editText.getText().toString().trim().isEmpty()) {
                    String resignCancelReason = cancelReason.getText().toString();

                    EmployeeSeparationModel esm = new EmployeeSeparationModel();
                    esm.setEmployee_Personal_Detail_ID(empDetailId);
                    esm.setEmp_Resign_Type(resignTypeID);
                    esm.setEmp_Resign_Cancel_Reason(resignCancelReason);
                    esm.setMODIFIED_BY(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
                    esm.setMODIFIED_ON(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    esm.setId(empRM.getServerId());

                    long id = EmployeeHelperClass.getInstance(EmployeeSeparation.this)
                            .empResignationCancelStatus(esm,
                                    view.getContext(), uploaded);

                    if (id > 0) {
                        Intent intent = new Intent(EmployeeSeparation.this, EmployeeDetailsActivity.class);
                        intent.putExtra("empDetailId", empDetailId);
                        MessageBox("Resignation deleted successfully", true, intent);
                    } else {
                        MessageBox("Something went wrong");
                    }
                    dialog.dismiss();

                } else {
                    Toast.makeText(EmployeeSeparation.this, "Please Enter Reason", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }

    private void updateResignatioon() {
        Log.i("submit","Count");
        setUpdateButtonEnabled(false);
        AlertDialog.Builder updateAlertbox = new AlertDialog.Builder(EmployeeSeparation.this);

        updateAlertbox.setMessage("Are you sure you want to update the Last Working Day of " + employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
        updateAlertbox.setTitle("Confirm");
        updateAlertbox.setCancelable(false);

        updateAlertbox.setPositiveButton("Yes", (dialog, which) -> {
            setUpdateButtonEnabled(true);
            long id = EmployeeHelperClass.getInstance(EmployeeSeparation.this)
                    .empResignationUpdateStatus(employeeModel,
                            view.getContext(),
                            AppModel.getInstance().convertDatetoFormat(txt_ResStartDate.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"),
                            Integer.parseInt(leaveWithoutPay.getText().toString()));
            if (id > 0) {
                Intent intent = new Intent(EmployeeSeparation.this, EmployeeDetailsActivity.class);
                intent.putExtra("empDetailId", empDetailId);
                MessageBox("Resignation updated successfully", true, intent);
            } else {
                MessageBox("Something went wrong");
            }
        });

        updateAlertbox.setNegativeButton("No", (dialog, which) -> {
            setUpdateButtonEnabled(true);
        });
        updateAlertbox.show();
    }


    private void Separate(int resignTypeID) {
        try {
            if (validate()) {
                Log.i("submit","Count");
                setSubmitButtonEnabled(false);
                EmployeeSeparationModel erm = new EmployeeSeparationModel();
                String days="";
                erm.setSchoolID(schoolId);
                erm.setEmployee_Personal_Detail_ID(employeeModel.getId());
                erm.setEmp_SubReasonID(resignReasonID);
                erm.setSubReasonText(ResSubReason.getText().toString());
                erm.setLwop(lwpValue);
                erm.setEmp_Resign_Date(AppModel.getInstance().convertDatetoFormat(txt_ResDate.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"));
                erm.setLastWorkingDay(AppModel.getInstance().convertDatetoFormat(txt_ResStartDate.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"));
                erm.setCREATED_ON_APP(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                erm.setCREATED_ON_SERVER("");
                erm.setCREATED_BY(DatabaseHelper.getInstance(EmployeeSeparation.this).getCurrentLoggedInUser().getId());
                erm.setMODIFIED_ON(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                erm.setMODIFIED_BY(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
                erm.setEmp_Status(3);
                erm.setActive(true);
                erm.setDeviceId(AppModel.getInstance().getDeviceId(EmployeeSeparation.this));
                erm.setUploadedOn(null);
                erm.setEmp_Resign_Cancel_Reason("");

                if(resignTypeID == 1){
                    days = tv_total_days.getText().toString();
                    erm.setEmp_Resign_Type(1);
                }
                else if (resignTypeID == 2) {
                    erm.setEmp_Resign_Type(2);
                }
                else if (resignTypeID == 3) {
                    erm.setEmp_Resign_Type(3);
                }
                if (employeeModel != null) {
                    EmployeeResignationDialogManager erdm = new EmployeeResignationDialogManager(EmployeeSeparation.this, employeeModel, erm, days, SeparationAttachmentsAdapter.paths,onButtonEnableDisableListener);
                    erdm.setCancelable(false);
                    erdm.show();
                }

            } else {
                lwp = leaveWithoutPay.getText().toString().trim();
                try {
                    lwpValue = Integer.parseInt(lwp);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String toast = "Please fill the following fields\n";
                if(txt_ResStartDate.getText().toString().equals("Select Date"))
                    toast += "Last Working Day\n";
                if(resignTypeID == 1 || resignTypeID == 2){
                    if(txt_ResDate.getText().toString().equals("Select Date")){
                        if(resignTypeID == 1)
                            toast += "Resignation Date\n";
                        else
                            toast += "Termination Date\n";
                    }

                }
                if (reason_spinner.getSelectedItem() == null || reason_spinner.getSelectedItem().toString().equals(getString(R.string.select_reason)) || reason_spinner.getSelectedItem().toString().equals("No Reason Found"))
                    toast += "Resign Reason\n";
                if (subReason_spinner.getSelectedItem() == null || subReason_spinner.getSelectedItem().toString().equals(getString(R.string.select_reason)) || subReason_spinner.getSelectedItem().toString().equals("No Reason Found"))
                    toast += "Sub Reason\n";
                else if(reason_spinner.getSelectedItem().toString().contains("Others") && (ResSubReason.getText() == null || ResSubReason.getText().toString().trim().equals("")))
                    toast += "Sub Reason Text\n";
                if(lwp.equals(""))
                    toast += "Leave Without Pay\n";
                else if(lwpValue < 0 || lwpValue > 50)
                    toast += "Leave Without Pay should be between 0-50\n";
                if (!(SeparationAttachmentsAdapter.paths.size() > 0))
                    toast += "Separation Attachments";
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate(){
        lwp = leaveWithoutPay.getText().toString().trim();
        try {
            lwpValue = Integer.parseInt(lwp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        boolean subReasonCheck;
        if(reason_spinner.getSelectedItem().toString().contains("Others")){
            subReasonCheck = ResSubReason.getText() == null || ResSubReason.getText().toString().trim().equals("");
        } else {
            subReasonCheck = subReason_spinner.getSelectedItem().toString().equals(getString(R.string.select_reason));
        }
        if(resignTypeID == 1 || resignTypeID == 2){
            return SeparationAttachmentsAdapter.paths.size() > 0 && (!lwp.equals("") && lwpValue >= 0 && lwpValue <= 50) && !txt_ResDate.getText().toString().equals("Select Date") && !txt_ResStartDate.getText().toString().equals("Select Date") && !reason_spinner.getSelectedItem().toString().equals(getString(R.string.select_reason)) && !subReasonCheck;
        } else if (resignTypeID == 3){
            return SeparationAttachmentsAdapter.paths.size() > 0 && (!lwp.equals("") && lwpValue >= 0 && lwpValue <= 50) && !txt_ResStartDate.getText().toString().equals("Select Date") && !reason_spinner.getSelectedItem().toString().equals(getString(R.string.select_reason)) && !subReasonCheck;
        }

        return true;
    }

   /* private void setResignRByDefaultValue(int resignReasonID) {
        populateReasonSpinner();
//        populateSubReasonSpinner(reasonsList.get(0).getResignReason());
        if (reasonsList.size() > 0) {
            for (EmployeeResignReasonModel model : reasonsList) {
                if (model.getID() == resignReasonID) {
                    reason_spinner.setSelection(reasonsList.indexOf(model));
                    break;
                }
            }
        }
    }*/

    private void setTextChangeListener() {

        txt_ResStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals("Select Date")) {
                    noOfDays = AppModel.getInstance().getDaysBetweenDates(nfCurrentDate, txt_ResStartDate.getText().toString(), "dd-MMM-yy") + 1;
                    offDaysFromCalendar = getAllOffDaysBetweenStartDateEndDate();

                    if(noOfDays < 0){
                        tv_total_days.setText(String.valueOf(0));
                        tv_totalWdays.setText(String.valueOf(0));
                    } else
                    {
                        workingDays = noOfDays - offDaysFromCalendar;
                        tv_total_days.setText(String.valueOf(noOfDays));
                        tv_totalWdays.setText(String.valueOf(workingDays));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        leaveWithoutPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() != 0){
                    if(Integer.parseInt(String.valueOf(charSequence)) < 0 || Integer.parseInt(String.valueOf(charSequence)) > 50){
                        leaveWithoutPay.setError("Value should be between 0 and 50");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void working() {
        setDates();
        setTextChangeListener();
        populateEmployeeData();
    }

    private void populateEmployeeData() {
        if (employeeModel != null) {
            txt_ResName.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
            txt_ResEmCode.setText(employeeModel.getEmployee_Code());
            if(employeeModel.getId() == DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId()){
                rb_termination.setVisibility(View.GONE);
                rb_expired.setVisibility(View.GONE);
                rb_resignation.setChecked(true);
                resignTypeID = 1;
                setSeparationDetails(resignTypeID);
                populateReasonSpinner();
            }

            if (EmployeeHelperClass.getInstance(view.getContext()).employeeIsInactive(empDetailId)) {
                if (empRM != null) {
                    try {
                        ll_next.setVisibility(View.VISIBLE);
                        btn_Resign.setVisibility(View.GONE);
                        try {
                            txt_ResDate.setEnabled(false);
                            txt_ResDate.setText(AppModel.getInstance().convertDatetoFormat(empRM.getEmp_Resign_Date(), "yyyy-MM-dd", "dd-MMM-yy"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            txt_ResStartDate.setText(AppModel.getInstance().convertDatetoFormat(empRM.getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy"));
                            String last_working_date = AppModel.getInstance().convertDatetoFormat(empRM.getLastWorkingDay(), "yyyy-MM-dd", "yyyy-MM-dd");
                            if(last_working_date.compareTo(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd")) < 0){
                                btn_delete.setVisibility(View.GONE);
                            } else {
                                btn_delete.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        setSepTypeByDefaultValue(empRM.getEmp_Resign_Type());
//                        setResignRByDefaultValue(empRM.getEmp_Resign_Reason());


                        reasonsList = EmployeeHelperClass.getInstance(this).getReasonAndSubReason(empRM.getEmp_SubReasonID());
                        ResReaason.setText(reasonsList.get(0).getResignReason());
                        if(reasonsList.get(0).getResignReason().contains("Others") && empRM.getSubReasonText() != null)
                            ResSubReason.setText(empRM.getSubReasonText());
                        else
                            ResSubReason.setText(reasonsList.get(0).getSubReason());
                        ResSubReason.setEnabled(false);
                        et_sepType.setText("Resignation");
                        ResReaason.setVisibility(View.VISIBLE);
                        ResSubReason.setVisibility(View.VISIBLE);
                        et_sepType.setVisibility(View.VISIBLE);
                        reason_spinner.setVisibility(View.GONE);
                        subReason_spinner.setVisibility(View.GONE);
                        rg_separationType.setVisibility(View.GONE);

                        leaveWithoutPay.setText(empRM.getLwop() + "");
//                        leaveWithoutPay.setEnabled(false);
/*
                        File formPath = new File(empRM.getEmp_Resign_Form_IMG());
                        File letterPath = new File(empRM.getEmp_Resign_Letter_IMG());

                        byte[] dataL = AppModel.getInstance().bitmapToByte(Compressor.getDefault(view.getContext()).compressToBitmap(letterPath));
                        Bitmap bitmapL = BitmapFactory.decodeByteArray(dataL, 0, dataL.length);
                        img_ResLetter.setImageBitmap(bitmapL);

                        byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(view.getContext()).compressToBitmap(formPath));
                        Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                        img_RecoForm.setImageBitmap(bitmapF);*/

                        noOfDays = AppModel.getInstance().getDaysBetweenDates(txt_ResDate.getText().toString(), txt_ResStartDate.getText().toString(), "dd-MMM-yy") + 1;
                        if(noOfDays < 0){
                            tv_total_days.setText(String.valueOf(0));
                            tv_totalWdays.setText(String.valueOf(0));
                        } else {
                            offDaysFromCalendar = getAllOffDaysBetweenStartDateEndDate();
                            workingDays = noOfDays - offDaysFromCalendar;

                            tv_total_days.setText(String.valueOf(noOfDays));
                            tv_totalWdays.setText(String.valueOf(workingDays));
                        }


                        if (empRM.getServerId() == 0) {
                            uploaded = false;
                        }

                        int id;
                        if(uploaded)
                            id = empRM.getServerId();
                        else
                            id = empRM.getId();
                        SeparationAttachmentsAdapter.paths = EmployeeHelperClass.getInstance(this).getSeparationAttachments(id);
                        List<String> images = SeparationAttachmentsAdapter.paths;
                        if(images != null && images.size() > 0){
                            SeparationAttachmentsAdapter adapter = new SeparationAttachmentsAdapter(true,this,images,0);
                            rc_attachments.setAdapter(adapter);
                        } else {
                            rc_attachments.setVisibility(View.GONE);
                            noAttachmentFound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setToolbar("Update/Delete Resignation", this, false);

                ll_next.setVisibility(View.VISIBLE);
//                img_ResLetter.setEnabled(false);
//                img_RecoForm.setEnabled(false);
                btn_Resign.setVisibility(View.GONE);
                update_delete_buttons.setVisibility(View.VISIBLE);
                btn_update.setEnabled(true);
                btn_delete.setEnabled(true);
            }
        }
    }

    private void Datepicker(int id) {


        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            strDate = newFormat.format(calendar.getTime());
            if(id == R.id.txt_ResStartDate)
                txt_ResStartDate.setText(strDate);
            else if(id == R.id.txt_ResDate){

                txt_ResDate.setText(strDate);
                noticePeriodSD = calendar.getTimeInMillis();
                try {
                    if(resignTypeID == 1 && newFormat.parse(txt_ResStartDate.getText().toString()).getTime() > noticePeriodSD + TimeUnit.DAYS.toMillis(30))
                        txt_ResStartDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
                    else if (resignTypeID == 2){
                        txt_ResStartDate.setText(strDate);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(resignTypeID == 1){
                    txt_ResStartDate.setEnabled(true);
                    txt_ResStartDate.setText("Select Date");
                }
            }

            String _year = String.valueOf(year);
            String _month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            _pickedDate = _date + "-" + _month + "-" + _year;

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        setDateRules(id);
        /*if(id == R.id.txt_ResStartDate){
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));
            mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 2000);
        }
        else if (id == R.id.txt_ResDate){
            mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(15));
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }*/
        mDatePickerDialog.show();
    }

    private void setDateRules(int id) {
        if(empRM == null){
            if(resignTypeID == 1){
                if(id == R.id.txt_ResStartDate){
                    mDatePickerDialog.getDatePicker().setMaxDate(noticePeriodSD + TimeUnit.DAYS.toMillis(30));
                    mDatePickerDialog.getDatePicker().setMinDate(noticePeriodSD);
                }
                else if (id == R.id.txt_ResDate){
                    mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
                    mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));
                }
            } else if (resignTypeID == 2){
                if(id == R.id.txt_ResStartDate){
                    mDatePickerDialog.getDatePicker().setMaxDate(noticePeriodSD + 2000);
                    mDatePickerDialog.getDatePicker().setMinDate(noticePeriodSD);
                }
                else if (id == R.id.txt_ResDate){
                    mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
                    mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 2000);
                }
            }  else if (resignTypeID == 3){
                if(id == R.id.txt_ResStartDate){
                    mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 2000);
                    mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(15));
                }
            }
        } else {
            if(id == R.id.txt_ResStartDate){

                Date convertedDate = new Date();
                try {
                    convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(empRM.getLastWorkingDay());
                    mDatePickerDialog.getDatePicker().setMaxDate(convertedDate.getTime() + TimeUnit.DAYS.toMillis(30));
                    mDatePickerDialog.getDatePicker().setMinDate(convertedDate.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /*   private void populateSeparationTypeSpinner() {
           sepTypeList = EmployeeHelperClass.getInstance(this).getResignType();
           sepTypeList.add(0,new EmployeeResignTypeModel(0,"Select Type"));

           sepTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sepTypeList);
           sepTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           sepType.setAdapter(sepTypeAdapter);
       }
   */
    private void populateReasonSpinner() {
        reasonsList = EmployeeHelperClass.getInstance(this).getResignReasons(resignTypeID);
        if(reasonsList.size() > 1)
            reasonsList.add(0, new EmployeeResignReasonModel(0, getString(R.string.select_reason)));
        else if(reasonsList.size() < 1)
            reasonsList.add(0, new EmployeeResignReasonModel(0, "No Reason Found"));

        ReasonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reasonsList);
        ReasonAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        reason_spinner.setAdapter(ReasonAdapter);
    }

    private void populateSubReasonSpinner(String reason) {
        subReasonLayout.setVisibility(View.VISIBLE);
        subReason_spinner.setVisibility(View.VISIBLE);
        ResSubReason.setVisibility(View.GONE);
        subReasonsList = EmployeeHelperClass.getInstance(this).getSubReasons(reason);
        if(subReasonsList.size() > 1)
            subReasonsList.add(0, new EmployeeResignReasonModel(0, getString(R.string.select_reason)));
        else if(subReasonsList.size() < 1)
            subReasonsList.add(0, new EmployeeResignReasonModel(0, "No Sub Reason Found"));
        else {
            if(subReasonsList.get(0).getResignReason() == null || subReasonsList.get(0).getResignReason().trim().equals("")){
                resignReasonID = subReasonsList.get(0).getID();

                if(reason.contains("Others")){
                    subReason_spinner.setVisibility(View.GONE);
                    ResSubReason.setVisibility(View.VISIBLE);
                } else {
                    subReasonLayout.setVisibility(View.GONE);
                }
            }

        }

        subReasonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subReasonsList);
        subReasonAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        subReason_spinner.setAdapter(subReasonAdapter);
    }

    private void setSeparationDetails(int resignTypeID) {
        if (resignTypeID == 1 || resignTypeID == 2 || resignTypeID ==3) {
            Calendar calendar = Calendar.getInstance();
            if(resignTypeID == 2) {
                btn_Resign.setText("Terminate");
                strDate = newFormat.format(calendar.getTime());
//                txt_ResStartDate.setText(strDate);
                txt_ResStartDate.setEnabled(false);

                txt_ResDate.setText("Select Date");
                txt_ResDate.setEnabled(true);
                ll_resignDate.setVisibility(View.VISIBLE);

                ll_totalDays.setVisibility(View.GONE);
                ll_total_lwd.setVisibility(View.GONE);
                separationLabel.setText("Termination Date");
                lwdLabel.setText("Last Working Day");

            } else if(resignTypeID == 3) {
                btn_Resign.setText("Death");
                strDate = newFormat.format(calendar.getTime());
//                txt_ResStartDate.setText(strDate);
                txt_ResStartDate.setText("Select Date");
                txt_ResStartDate.setEnabled(true);
                ll_resignDate.setVisibility(View.GONE);
//                txt_ResDate.setText("Select Date");
                txt_ResDate.setEnabled(false);
                lwdLabel.setText("Date of Death");

                ll_totalDays.setVisibility(View.GONE);
                ll_total_lwd.setVisibility(View.GONE);
            } else {
                btn_Resign.setText("Resign");
                strDate = newFormat.format(calendar.getTime());
                txt_ResStartDate.setText("Select Date");
                txt_ResStartDate.setEnabled(false);

                lwdLabel.setText("Last Working Day");
                txt_ResDate.setText("Select Date");
                txt_ResDate.setEnabled(true);

                ll_totalDays.setVisibility(View.VISIBLE);
                ll_total_lwd.setVisibility(View.VISIBLE);
                ll_resignDate.setVisibility(View.VISIBLE);
                separationLabel.setText("Resignation Date");
            }
            ll_next.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));
            ll_next.setVisibility(View.VISIBLE);
        } else {
            ll_next.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spin_ResReason:
//                resignReasonID = ((EmployeeResignReasonModel) parent.getItemAtPosition(position)).getID();
                populateSubReasonSpinner(((EmployeeResignReasonModel) parent.getItemAtPosition(position)).getResignReason());
                break;
            case R.id.spin_ResSubReason:
                resignReasonID = ((EmployeeResignReasonModel) parent.getItemAtPosition(position)).getID();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getAllOffDaysBetweenStartDateEndDate() {
        int days = 0;
        Set<String> uniqueStartDates = new HashSet<>();

        if(OffdaysList == null || OffdaysList.size() <=0)
            return 0;
        for (CalendarsModel cm : OffdaysList) {
            uniqueStartDates.add(cm.getActivity_Start_Date());
        }

        List<String> startDates = new ArrayList<>(uniqueStartDates);

        String startDate = AppModel.getInstance().convertDatetoFormat(nfCurrentDate, "dd-MMM-yy", "yyyy-MM-dd'T'hh:mm:ss");
        String endDate = AppModel.getInstance().convertDatetoFormat(txt_ResStartDate.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd'T'hh:mm:ss");

        List<String> datesBetweenStartDateEndDate = new ArrayList<>(AppModel.getInstance()
                .getDatesBetweenTwoDates(startDate, endDate, "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'00:00:00"));

        for (int i = 0; i < startDates.size(); i++) {
            for (int j = 0; j < datesBetweenStartDateEndDate.size(); j++) {
                if (startDates.get(i).equals(datesBetweenStartDateEndDate.get(j))) {
                    days++;
                }
            }
        }
        return days;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EmployeeSeparation.this, EmployeeDetailsActivity.class);
        intent.putExtra("empDetailId", empDetailId);
        intent.putExtra("schoolId", schoolId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        subReasonLayout.setVisibility(View.VISIBLE);
        switch(i){
            case R.id.rb_resignation:
                resignTypeID = 1;
                SeparationAttachmentsAdapter.paths.clear();
                attachments.clear();
                separationAttachmentsAdapter = new SeparationAttachmentsAdapter(false,this,attachments,resignTypeID);
                rc_attachments.setAdapter(separationAttachmentsAdapter);
                setSeparationDetails(resignTypeID);
                populateReasonSpinner();
                break;

            case R.id.rb_termination:
                resignTypeID = 2;
                SeparationAttachmentsAdapter.paths.clear();
                attachments.clear();
                separationAttachmentsAdapter = new SeparationAttachmentsAdapter(false,this,attachments,resignTypeID);
                rc_attachments.setAdapter(separationAttachmentsAdapter);
                setSeparationDetails(resignTypeID);
                populateReasonSpinner();
                break;

            case R.id.rb_expired:
                resignTypeID = 3;
                SeparationAttachmentsAdapter.paths.clear();
                attachments.clear();
                separationAttachmentsAdapter = new SeparationAttachmentsAdapter(false,this,attachments,resignTypeID);
                rc_attachments.setAdapter(separationAttachmentsAdapter);
                setSeparationDetails(resignTypeID);
                populateReasonSpinner();
                break;
        }

    }

    private void setSubmitButtonEnabled(boolean enabled){
        btn_Resign.setEnabled(enabled);
        btn_Resign.setClickable(enabled);
    }

    private void setUpdateButtonEnabled(boolean enabled){
        btn_update.setEnabled(enabled);
        btn_update.setClickable(enabled);
    }

    @Override
    public void onSubmitClicked() {
        setSubmitButtonEnabled(true);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RES_LETTER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//
//                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 550, 550, true);
//                img_ResLetter.setImageBitmap(bMapScaled);
//                byte[] Resign_Letter = AppModel.getInstance().bitmapToByte(bMapScaled);
//                Resign_Letter_Path = AppModel.getInstance().saveImageToStorage2(Resign_Letter, view.getContext(), employeeModel.getEmployee_Code(), 0, employeeModel.getEmployee_ID());
////
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == REC_FORM_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//
//                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 550, 550, true);
//                img_RecoForm.setImageBitmap(bMapScaled);
//                byte[] Resign_Form = AppModel.getInstance().bitmapToByte(bMapScaled);
//                Resign_Form_Path = AppModel.getInstance().saveImageToStorage2(Resign_Form, view.getContext(), employeeModel.getEmployee_Code(), 1, employeeModel.getEmployee_ID());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}

