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

import com.tcf.sma.R;

/**
 * Created by Zubair Soomro on 2/14/2017.
 */

public class SSRValidationApprovalDialogManager extends Dialog implements View.OnClickListener {
    private Activity activity;
    private LinearLayout unApprove, cancel;
    private EditText et_unApprovalComments;
    private int grno;

    public SSRValidationApprovalDialogManager(Context context) {
        super(context);
        this.activity = (Activity) context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ssr_unapproved_trial);
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
                Toast.makeText(activity, "Approved", Toast.LENGTH_SHORT).show();
                dismiss();
                activity.finish();

                break;
        }
    }
}
