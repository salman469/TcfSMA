package com.tcf.sma.Managers.FeesCollection;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

public class GRDialogManager extends Dialog {


    Button bt_ok, bt_cancel;
    EditText et_grNo;
    TextView tv_name, tv_sectionName, tv_className, tv_fathersName;
    ImageView iv_search;
    int schoolId;
    StudentDetailInterface sdi;
    StudentModel sm;

    public GRDialogManager(@NonNull Context context, int schoolId, StudentDetailInterface sdi) {
        super(context);
        this.schoolId = schoolId;
        this.sdi = sdi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);

        et_grNo = (EditText) findViewById(R.id.et_grNo);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);

        tv_className = (TextView) findViewById(R.id.tv_className);
        tv_sectionName = (TextView) findViewById(R.id.tv_sectionName);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_fathersName = (TextView) findViewById(R.id.tv_fathersName);


        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStudentFields();
                if (et_grNo.getText().toString().isEmpty()) {
                    sm = DatabaseHelper.getInstance(getContext()).
                            getStudentwithGR(Integer.valueOf(et_grNo.getText().toString()), schoolId);
                    sdi.onGetStudenDetail(sm);

                    if (sm != null)
                        populateStudentFields();
                }
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void populateStudentFields() {
        tv_className.setText(sm.getCurrentClass());
        tv_name.setText(sm.getName());
        tv_fathersName.setText(sm.getFathersName());
        tv_sectionName.setText(sm.getCurrentSection());
    }

    private void clearStudentFields() {
        tv_className.setText("");
        tv_name.setText("");
        tv_fathersName.setText("");
        tv_sectionName.setText("");
    }

    public interface StudentDetailInterface {
        void onGetStudenDetail(StudentModel studentModel);
    }
}
