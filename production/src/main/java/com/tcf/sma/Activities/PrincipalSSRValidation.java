package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.UnApprovalDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.R;

public class PrincipalSSRValidation extends DrawerActivity implements View.OnClickListener {

    public View view;
    Toolbar toolbar;
    UnApprovalDialogManager dialogManager;
    TextView tv_visiting_date, tvEnrollmentStatus, tvRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_principal_ssrvalidation);
        setToolbar(getString(R.string.profile_validation), this, false);
        init(view);
        working();
    }

    private void working() {
        SchoolAuditModel sam = DatabaseHelper.getInstance(this).getlastVisitedSchoolAudit(AppModel.getInstance().getSelectedSchool(this));
        if (sam != null) {
//            tv_visiting_date.setText(SurveyAppModel.getInstance().
//                    convertDatetoFormat(sam.getVisit_date(),"yyyy-MM-dd","dd-MM-yyyy"));
            tv_visiting_date.setText(AppModel.getInstance().
                    convertDatetoFormat(sam.getVisit_date(), "yyyy-MM-dd", "dd-MMM-yy"));
            String status = sam.is_approved() ? "Approved" : "Not Approved";
            tvEnrollmentStatus.setText(status);
            tvRemarks.setText(sam.getRemarks());
        }
    }

    private void init(View view) {
        tv_visiting_date = (TextView) view.findViewById(R.id.tv_visiting_date);
        tvEnrollmentStatus = (TextView) view.findViewById(R.id.tvEnrollmentStatus);
        tvRemarks = (TextView) view.findViewById(R.id.tvRemarks);

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.et_visting_date:
//                SurveyAppModel.getInstance().DatePicker(et_visiting_date, this);
//                break;
//            case R.id.btn_remarks:
//
//                //dialogManager=new UnApprovalDialogManager(this);
//                // dialogManager.show();
//                break;
//        }
    }
}
