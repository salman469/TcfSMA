package com.tcf.sma.Activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.StudentStrengthAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.SSRValidationApprovalDialogManager;
import com.tcf.sma.Managers.SSRValidationUnApprovedDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.Models.StudentStrengthModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class StudentStrengthReport extends DrawerActivity implements View.OnClickListener {
    StudentStrengthAdapter studentStrengthAdapter;
    ArrayList<ClassModel> modelArrayList;
    ArrayList<SchoolAuditModel> schoolAuditModels;
    SchoolAuditModel schoolAuditModel = new SchoolAuditModel();
    SSRValidationUnApprovedDialogManager ssrValidationUnApprovedDialogManager;
    SSRValidationApprovalDialogManager approvalDialogManager;
    StudentStrengthModel model;
    RecyclerView rv_student_Strngth;
    EditText et_visiting_date;
    Button not_Approved, approved;
    TextView tv_count;
    int selectedSchoolID, countSSr;
    private View view;
    private int Position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_student_strength_report);
        setToolbar("Strength Validation", this, false);
        init();
        working();

        modelArrayList = new ArrayList<>();
        selectedSchoolID = AppModel.getInstance().getSelectedSchool(this);
        modelArrayList = DatabaseHelper.getInstance(this).getAllClasses();

        studentStrengthAdapter = new StudentStrengthAdapter(modelArrayList, this);
        rv_student_Strngth.setAdapter(studentStrengthAdapter);
        countSSr = DatabaseHelper.getInstance(this).getAllStudentsCount(selectedSchoolID);
        tv_count.setText("" + countSSr);
    }

    public void init() {

        rv_student_Strngth = (RecyclerView) findViewById(R.id.rv_student_strength);
        et_visiting_date = (EditText) findViewById(R.id.et_visting_date);
        et_visiting_date.setInputType(InputType.TYPE_NULL);
        et_visiting_date.setText(AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy"));

        tv_count = (TextView) findViewById(R.id.tv_total_count);
        not_Approved = (Button) findViewById(R.id.btn_not_apparoved);
        approved = (Button) findViewById(R.id.btn_approved);

        et_visiting_date.setOnClickListener(this);
        not_Approved.setOnClickListener(this);
        approved.setOnClickListener(this);
    }

    private void working() {
        rv_student_Strngth.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_student_Strngth.setLayoutManager(llm);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_visting_date:
                AppModel.getInstance().DatePicker(et_visiting_date, this);
                break;
            case R.id.btn_not_apparoved:
                if (!et_visiting_date.getText().toString().equals("")) {
                    et_visiting_date.setError(null);
//                    Toast.makeText(this, "Date Selected", Toast.LENGTH_SHORT).show();
                    String visitDate = AppModel.getInstance().convertDatetoFormat(et_visiting_date.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");
                    ssrValidationUnApprovedDialogManager = new SSRValidationUnApprovedDialogManager(this, visitDate, countSSr, DatabaseHelper.getInstance(this).getAllClasses().size());
                    ssrValidationUnApprovedDialogManager.show();

                } else {
                    et_visiting_date.setError("Required");
                }


                break;
            case R.id.btn_approved:
                if (!et_visiting_date.getText().toString().equals("")) {
                    et_visiting_date.setError(null);
                    schoolAuditModel = new SchoolAuditModel();
                    String visitDate = AppModel.getInstance().convertDatetoFormat(et_visiting_date.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");
                    schoolAuditModel.setVisit_date(visitDate);
                    schoolAuditModel.setIs_approved(true);
                    schoolAuditModel.setSchool_id(AppModel.getInstance().getSelectedSchool(this));
                    schoolAuditModel.setApproved_by(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
                    schoolAuditModel.setStudents_count(countSSr);
                    schoolAuditModel.setClasses_count(DatabaseHelper.getInstance(this).getAllClasses().size());

                    long rowId = DatabaseHelper.getInstance(this).insertSchoolAuditRecordHeader(schoolAuditModel);
                    if (rowId > 0) {
                        finish();
                        Toast.makeText(this, "Approved", Toast.LENGTH_SHORT).show();
                    } else {
                        MessageBox("Something went wrong!");
                    }
                } else {
                    et_visiting_date.setError("Required");
                }

                break;


        }
    }
}