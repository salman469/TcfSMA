package com.tcf.sma.Managers.HR;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.HR.EmployeeDetailsActivity;
import com.tcf.sma.Activities.HR.EmployeeListing;
import com.tcf.sma.Activities.HR.EmployeeSeparation;
import com.tcf.sma.Activities.StudentProfileActivity;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Interfaces.OnButtonEnableDisableListener;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.util.List;

public class EmployeeResignationDialogManager extends Dialog implements View.OnClickListener {
    Activity activity;
    TextView tv_name, tv_emp_code, message, tv_last_working_day, tv_alert, tv_total_days;
    LinearLayout ll_yes, ll_no;
    String name, employeeCode, lastWorkingDay;
    String numOfDays;
    EmployeeModel em;
    EmployeeSeparationModel erm;
    List<String> images;
    private OnButtonEnableDisableListener onButtonEnableDisableListener;

    public EmployeeResignationDialogManager(Activity activity, EmployeeModel em, EmployeeSeparationModel erm, String numOfDays, List<String> images,OnButtonEnableDisableListener listener) {
        super(activity);
        this.activity = activity;
        this.numOfDays = numOfDays;
        this.em = em;
        this.erm = erm;
        this.images = images;
        this.onButtonEnableDisableListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_resignation_confirmation);
        name = em.getFirst_Name() + " " + em.getLast_Name();
        employeeCode = em.getEmployee_Code();
        lastWorkingDay = AppModel.getInstance().convertDatetoFormat(erm.getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy");
        init();

        if (erm.getEmp_Resign_Type() == 2) {
            tv_last_working_day.setVisibility(View.GONE);
            message.setText("Are you sure you want to terminate " + name + "?");
            tv_alert.setText("Termination Confirmation");
        } else if (erm.getEmp_Resign_Type() == 3) {
            tv_last_working_day.setVisibility(View.GONE);
            message.setText("Are you sure you want to mark " + name + " as deceased?");
            tv_alert.setText("Death Confirmation");
        }

    }

    private void init() {
        tv_alert = findViewById(R.id.tv_alert);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText("Name:  " + name);
        tv_emp_code = (TextView) findViewById(R.id.tv_employee_code);
        tv_emp_code.setText("Employee Code:  " + employeeCode);
        tv_last_working_day = findViewById(R.id.tv_last_working_day);
        tv_last_working_day.setText("Last Working Day: " + lastWorkingDay);

        tv_total_days = findViewById(R.id.tv_total_days);
        tv_total_days.setText("Total Days: " + numOfDays);
        message = findViewById(R.id.tv_confirm_text);
        message.setText("Are you sure you want to mark " + name + " resigned?");
        ll_yes = (LinearLayout) findViewById(R.id.ll_yes);
        ll_no = (LinearLayout) findViewById(R.id.ll_no);
        ll_yes.setOnClickListener(this);
        ll_no.setOnClickListener(this);

        if (erm.getEmp_Resign_Type() == 2 || erm.getEmp_Resign_Type() == 3) {
            tv_total_days.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_yes:
                onButtonEnableDisableListener.onSubmitClicked();
                dismiss();

                long id = EmployeeHelperClass.getInstance(activity)
                        .empResignationStatus(erm,
                                em, images,
                                view.getContext());

                if(id>0) {
                    //Important when any change in table call this method
                    AppModel.getInstance().changeMenuPendingSyncCount(activity.getApplicationContext(), true);

                    if (erm.getEmp_Resign_Type() == 1) {
                        Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
                        intent.putExtra("empDetailId", em.getId());
                        ((EmployeeSeparation) activity).MessageBox("Resignation Submitted", true, intent);
                    } else if (erm.getEmp_Resign_Type() == 2){
                        Intent intent = new Intent(activity, EmployeeListing.class);
                        ((EmployeeSeparation) activity).MessageBox("Termination Submitted", true, intent);
                    } else if (erm.getEmp_Resign_Type() == 3){
                        Intent intent = new Intent(activity, EmployeeListing.class);
                        ((EmployeeSeparation) activity).MessageBox("Death Submitted", true, intent);
                    }
                } else {
                    ((EmployeeSeparation) activity).MessageBox("Something went wrong");
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
                onButtonEnableDisableListener.onSubmitClicked();
                dismiss();
                break;
        }

    }
}
