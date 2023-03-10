package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tcf.sma.Activities.AreaManagerStudentSelection;
import com.tcf.sma.Adapters.CustomPageAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;

/**
 * Created by Zubair Soomro on 2/14/2017.
 */

public class UnApprovalDialogManager extends Dialog implements View.OnClickListener {
    CustomPageAdapter pagerInstance;
    private Activity activity;
    private LinearLayout unApprove, cancel;
    private EditText et_unApprovalComments;
    private int id, position;

    public UnApprovalDialogManager(Context context, int id, int position, CustomPageAdapter pagerInstance) {
        super(context);
        this.activity = (Activity) context;
        this.id = id;
        this.position = position;
        this.pagerInstance = pagerInstance;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_student_unapproval);
        init();

    }

    private void init() {
        unApprove = (LinearLayout) findViewById(R.id.ll_unApprove);
        cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        unApprove.setOnClickListener(this);
        cancel.setOnClickListener(this);
        et_unApprovalComments = (EditText) findViewById(R.id.et_unapproval_comments);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cancel:
                dismiss();
                break;

            case R.id.ll_unApprove:
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_IS_APPROVED, false);
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_UPLOADED_ON, (String) null);
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_APPROVED_BY, DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_APPROVED_ON, AppModel.getInstance().getDate());
                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_UNAPPROVED_COMMENTS, et_unApprovalComments.getText().toString());
                DatabaseHelper.getInstance(activity).updateStudentByColumns(contentValues, id);
                ((AreaManagerStudentSelection) activity).MessageBox("UnApproved!", true);
                dismiss();
                if (AreaManagerStudentSelection.vp_studentSelection.getAdapter().getCount() == position + 1) {
                    AreaManagerStudentSelection.vp_studentSelection.setCurrentItem((position) - 1);
                    ((AreaManagerStudentSelection) activity).removePage(position);
                } else {
                    AreaManagerStudentSelection.vp_studentSelection.setCurrentItem((position) + 1);
                    ((AreaManagerStudentSelection) activity).removePage(position);
                }
                activity.finish();
                break;
        }
    }
}
