package com.tcf.sma.Activities.HR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.io.File;

import id.zelory.compressor.Compressor;

public class TerminationApproval extends DrawerActivity {

    private Button btn_Approve, btn_close;
    private TextView txt_ResStartDate, txt_ResDate, txt_ResName, txt_ResEmCode, txt_ResReason;
    private ImageView img_ResLetter, img_RecoForm;
    private View view;
    private int empDetailId;
    private EmployeeModel employeeModel;
    private EmployeeSeparationModel erm;
    private EmployeeResignReasonModel reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_termination_approval);

        view = setActivityLayout(this, R.layout.activity_termination_approval);
        setToolbar("Termination Approval", this, false);
        init(view);

        try {
            if (employeeModel != null && erm != null) {
                txt_ResName.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
                txt_ResEmCode.setText(employeeModel.getEmployee_Code());
                txt_ResStartDate.setText(AppModel.getInstance().convertDatetoFormat(erm.getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy"));
                txt_ResDate.setText(AppModel.getInstance().convertDatetoFormat(erm.getEmp_Resign_Date(), "yyyy-MM-dd", "dd-MMM-yy"));
                reason = EmployeeHelperClass.getInstance(view.getContext()).getResignReason(erm.getEmp_SubReasonID());
                txt_ResReason.setText(reason.getResignReason());
                Activity activity = (Activity) view.getContext();
                File f = new File(erm.getEmp_Resign_Form_IMG());
                img_RecoForm.setImageBitmap(AppModel.getInstance().rotateImage(Compressor.getDefault(view.getContext()).compressToBitmap(f), activity.getWindowManager().getDefaultDisplay()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void init(View view) {
        txt_ResName = view.findViewById(R.id.appr_tv_terminate_name);
        txt_ResEmCode = view.findViewById(R.id.appr_tv_terminate_empCode);
        txt_ResStartDate = view.findViewById(R.id.appr_tv_terminate_date);
        txt_ResDate = view.findViewById(R.id.appr_tv_terminate_Lastday);
        txt_ResReason = view.findViewById(R.id.appr_tv_terminate_reason);
        btn_Approve = view.findViewById(R.id.appr_btn_terminateApprove);
        btn_close = (Button) view.findViewById(R.id.appr_btnClose);
        img_RecoForm = (ImageView) view.findViewById(R.id.appr_iv_recomend_form);
        btn_Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder approvalAlertbox = new AlertDialog.Builder(TerminationApproval.this);

                approvalAlertbox.setMessage("Are you sure you want to Approve the Termination of " + employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
                approvalAlertbox.setTitle("Confirm");

                approvalAlertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long id = -1;
//                        long id = EmployeeHelperClass.getInstance(TerminationApproval.this).changeStatus(erm,2);
                        if (id > 0) {
                            Intent broadcastIntent = new Intent(AppConstants.Action_PendingSyncChanged);
                            broadcastIntent.putExtra("schoolid", AppModel.getInstance().getSpinnerSelectedSchool(TerminationApproval.this));
                            sendBroadcast(broadcastIntent);

                            Intent intent = new Intent(TerminationApproval.this, Approval_Dashboard_Listing.class);
                            intent.putExtra("resignType", 2);
                            MessageBox("Termination approved successfully", true, intent);
                        } else {
                            MessageBox("Something went wrong");
                        }
                    }
                });

                approvalAlertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                approvalAlertbox.show();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("empDetailId")) {
            empDetailId = intent.getIntExtra("empDetailId", 0);
            employeeModel = EmployeeHelperClass.getInstance(view.getContext()).getEmployee(empDetailId);
            int pdID = employeeModel.getId();
            erm = EmployeeHelperClass.getInstance(view.getContext()).getResignedEmployees(pdID);

            if (intent.hasExtra("isManage")) {
                if (intent.getBooleanExtra("isManage", false)) {
                    btn_Approve.setVisibility(View.INVISIBLE);
                    btn_Approve.setEnabled(false);
                    setToolbar("Employee Termination Details", this, false);
                }
            }
        }

    }
}
