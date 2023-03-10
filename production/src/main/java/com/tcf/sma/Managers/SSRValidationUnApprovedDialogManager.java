package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.R;

/**
 * Created by Badar Arain on 2/14/2017.
 */

public class SSRValidationUnApprovedDialogManager extends Dialog implements View.OnClickListener {
    EditText un_approved_comments;
    SchoolAuditModel schoolAuditModel;
    String comments, visiDate;
    long L;
    int studentCount, classCount;
    private Activity activity;
    private LinearLayout unApprove, cancel;


    public SSRValidationUnApprovedDialogManager(Context context, String visitDate, int studentCount, int classCount) {
        super(context);
        this.activity = (Activity) context;
        this.visiDate = visitDate;
        this.studentCount = studentCount;
        this.classCount = classCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ssr_un_approved_);
        init();

    }

    private void init() {
        un_approved_comments = (EditText) findViewById(R.id.et_unapproval_comments);

        unApprove = (LinearLayout) findViewById(R.id.ll_unApprove);
        cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        unApprove.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cancel:
                dismiss();
                break;

            case R.id.ll_unApprove:
                comments = un_approved_comments.getText().toString();
                schoolAuditModel = new SchoolAuditModel();
                schoolAuditModel.setVisit_date(visiDate);
                schoolAuditModel.setApproved_by(DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                schoolAuditModel.setIs_approved(false);
                schoolAuditModel.setRemarks(comments);
                schoolAuditModel.setSchool_id(AppModel.getInstance().getSelectedSchool(activity));
                schoolAuditModel.setStudents_count(studentCount);
                schoolAuditModel.setClasses_count(classCount);
                DatabaseHelper.getInstance(activity).insertSchoolAuditRecordHeader(schoolAuditModel);
                Toast.makeText(activity, "Not Approved", Toast.LENGTH_SHORT).show();
                dismiss();
                activity.finish();
                break;
        }
    }
}
