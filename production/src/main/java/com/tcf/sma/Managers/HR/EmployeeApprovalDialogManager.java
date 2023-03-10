package com.tcf.sma.Managers.HR;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.tcf.sma.Activities.HR.EmployeeDetailsActivity;
import com.tcf.sma.Activities.HR.EmployeeListing;
import com.tcf.sma.Activities.HR.EmployeeSeparation;
import com.tcf.sma.Activities.HR.ManageResignationsActivity;
import com.tcf.sma.Adapters.HR.ManageResignationsAdapter;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.util.List;

public class EmployeeApprovalDialogManager extends Dialog implements View.OnClickListener {
    Activity activity;
    TextView tv_name, tv_emp_code, message, tv_designation, tv_alert;
    EditText et_remarks;
    LinearLayout ll_yes, ll_no;
    String name, employeeCode, designation;
    RelativeLayout rl_resignation_dialog;
    EmployeeModel em;
    EmployeeSeparationDetailModel esdm;
    int position;

    public EmployeeApprovalDialogManager(Activity activity, EmployeeModel em, EmployeeSeparationDetailModel esdm, int position) {
        super(activity);
        this.activity = activity;
        this.em = em;
        this.esdm = esdm;
        this.position = position;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_approval_confirmation);
        name = em.getFirst_Name() + " " + em.getLast_Name();
        employeeCode = em.getEmployee_Code();
        designation = em.getDesignation();
        init();

        if (esdm.getEmp_status() == 2) {
            rl_resignation_dialog.setBackgroundColor(ContextCompat.getColor(activity,R.color.light_red_color));
            tv_alert.setBackgroundColor(ContextCompat.getColor(activity,R.color.red_1));
            message.setText("Are you sure you want to reject the separation of " + name + "?");
            tv_alert.setText("Rejection Confirmation");
        } else if (esdm.getEmp_status() == 3) {
            rl_resignation_dialog.setBackgroundColor(ContextCompat.getColor(activity,R.color.light_app_green));
            tv_alert.setBackgroundColor(ContextCompat.getColor(activity,R.color.app_green));
            message.setText("Are you sure you want to approve the separation of " + name + "?");
            tv_alert.setText("Approval Confirmation");
        }

    }

    private void init() {
        tv_alert = findViewById(R.id.tv_alert);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText("Name:  " + name);
        tv_emp_code = (TextView) findViewById(R.id.tv_employee_code);
        tv_emp_code.setText("Employee Code:  " + employeeCode);
        tv_designation = findViewById(R.id.tv_designation);
        tv_designation.setText("Designation: " + designation);
        et_remarks = findViewById(R.id.tv_remarks);
        et_remarks.setText("");

        message = findViewById(R.id.tv_confirm_text);
        message.setText("Are you sure?");
        rl_resignation_dialog = findViewById(R.id.rl_resignation_dialog);
        ll_yes = (LinearLayout) findViewById(R.id.ll_yes);
        ll_no = (LinearLayout) findViewById(R.id.ll_no);
        ll_yes.setOnClickListener(this);
        ll_no.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_yes:
                String input = et_remarks.getText().toString().trim();
                if(!input.equals("")) {
                    dismiss();
                    if(input.contains("'"))
                    {
                        input = input.replace("'", "`");
                    }
                    esdm.setSeparation_Remarks(input);

                    long id = EmployeeHelperClass.getInstance(activity)
                            .changeStatus(esdm);
//                    long id = 1;

                    if (id > 0) {
                        if(esdm.getEmp_status() == 2){
                            Toast.makeText(activity, "Separation Rejected", Toast.LENGTH_SHORT).show();
                        } else if(esdm.getEmp_status() == 3){
                            Toast.makeText(activity, "Separation Approved", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ((EmployeeSeparation) activity).MessageBox("Something went wrong");
                    }
                } else {
                    Toast.makeText(activity, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                }
                break;
//
//                if (erm.getEmp_Resign_Type() == 1) {
//
//                    long id = EmployeeHelperClass.getInstance(activity)
//                            .empResignationStatus(erm,
//                                    em, images,
//                                    view.getContext());
//                    if (id > 0) {
//                    }
//                } else if (erm.getEmp_Resign_Type() == 2) {
//
//                    long id = EmployeeHelperClass.getInstance(activity)
//                            .empTerminationStatus(erm,
//                                    em,
//                                    view.getContext());
//                    if (id > 0) {
//                    } else {
//                        ((EmployeeSeparation) activity).MessageBox("Something went wrong");
//                    }
//                } else if (erm.getEmp_Resign_Type() == 3) {
//
//                    long id = EmployeeHelperClass.getInstance(activity)
//                            .empTerminationStatus(erm,
//                                    em,
//                                    view.getContext());
//                    if (id > 0) {
//                    } else {
//                        ((EmployeeSeparation) activity).MessageBox("Something went wrong");
//                    }
//                } else {
//                    Toast.makeText(activity, "Invalid Resign Type", Toast.LENGTH_SHORT).show();
//                }


            case R.id.ll_no:
                dismiss();
                break;
        }

    }
}
