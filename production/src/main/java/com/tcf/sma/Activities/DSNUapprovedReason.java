package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class DSNUapprovedReason extends DrawerActivity implements View.OnClickListener {
    View view;
    ImageView header;
    LinearLayout remarks, buttonHides;
    ArrayList<StudentModel> dsnlist = new ArrayList<>();
    Button editStudentInfo, updateStudentInfo, btn_report_student_view;
    TextView tv_student_name, tv_section_student_Gender, tv_student_GR_No, date_of_admission,
            tv_class, tv_section, tv_dob, tv_formB, tv_father_name, tv_father_cnic,
            tv_father_occupation, tv_mother_name, tv_mother_cnic, tv_mother_occupation,
            tv_guardian_name, tv_guardian_cnic, tv_guardian_occupation, tv_previous_school,
            tv_class_previous_school, tv_address, tv_contact, tv_unapproved_on, tv_unapproved_by, tv_remarks;
    int index = -1;
    String grNo, studentName;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_profile2);
        view = setActivityLayout(this, R.layout.activity_student_profile);
        setToolbar(getString(R.string.student_profile_text), this, false);
        init(view);
//        working();
    }

    private void init(View v) {
        index = getIntent().getIntExtra("Postion", -1);
        grNo = getIntent().getStringExtra("grno");
        studentName = getIntent().getStringExtra("studentName");
        studentId = getIntent().getIntExtra("id", -1);
//        StudentModel.getInstance().setStudent(StudentModel.getInstance().getStudentsList().get(index));
        editStudentInfo = (Button) view.findViewById(R.id.btn_edit_student_view);
        updateStudentInfo = (Button) view.findViewById(R.id.btn_update_student_view);
        btn_report_student_view = (Button) view.findViewById(R.id.btn_report_student_view);
        editStudentInfo.setOnClickListener(this);
        updateStudentInfo.setOnClickListener(this);
        btn_report_student_view.setOnClickListener(this);

        remarks = (LinearLayout) findViewById(R.id.lll123);
        buttonHides = (LinearLayout) findViewById(R.id.lll12345);
        tv_remarks = (TextView) findViewById(R.id.tv_remarks);
        tv_unapproved_by = (TextView) findViewById(R.id.tv_unapproved_by);
        tv_unapproved_on = (TextView) findViewById(R.id.tv_unapproved_on);
        tv_student_name = (TextView) v.findViewById(R.id.tv_student_name);
        tv_section_student_Gender = (TextView) v.findViewById(R.id.tv_section_student_Gender);
        tv_student_GR_No = (TextView) v.findViewById(R.id.tv_student_GR_No);
        date_of_admission = (TextView) v.findViewById(R.id.date_of_admission);
        tv_class = (TextView) v.findViewById(R.id.tv_class);
        tv_section = (TextView) v.findViewById(R.id.tv_section);
        tv_dob = (TextView) v.findViewById(R.id.tv_dob);
        tv_formB = (TextView) v.findViewById(R.id.tv_formB);
        tv_father_name = (TextView) v.findViewById(R.id.tv_father_name);
        tv_father_cnic = (TextView) v.findViewById(R.id.tv_father_cnic);
        tv_father_occupation = (TextView) v.findViewById(R.id.tv_father_occupation);
        tv_mother_name = (TextView) v.findViewById(R.id.tv_mother_name);
        tv_mother_cnic = (TextView) v.findViewById(R.id.tv_mother_cnic);
        tv_mother_occupation = (TextView) v.findViewById(R.id.tv_mother_occupation);
        tv_guardian_name = (TextView) v.findViewById(R.id.tv_guardian_name);
        tv_guardian_cnic = (TextView) v.findViewById(R.id.tv_guardian_cnic);
        tv_guardian_occupation = (TextView) v.findViewById(R.id.tv_guardian_occupation);
        tv_previous_school = (TextView) v.findViewById(R.id.tv_previous_school);
        tv_class_previous_school = (TextView) v.findViewById(R.id.tv_class_previous_school);
        tv_address = (TextView) v.findViewById(R.id.tv_address);
        tv_contact = (TextView) v.findViewById(R.id.tv_contact);
        header = (ImageView) v.findViewById(R.id.header);
    }

    private void working() {
        buttonHides.setVisibility(View.GONE);
        remarks.setVisibility(View.VISIBLE);
        dsnlist = DatabaseHelper.getInstance(this).getDashboardUnapprovedStudents(AppModel.getInstance().getSelectedSchool(this), studentId);
        if (dsnlist != null && dsnlist.size() != 0) {

            tv_student_name.setText(dsnlist.get(index).getName());
            tv_section_student_Gender.setText(dsnlist.get(index).getGender());
            tv_student_GR_No.setText(dsnlist.get(index).getGrNo());
            date_of_admission.setText(dsnlist.get(index).getEnrollmentDate());
            tv_class.setText(String.valueOf(dsnlist.get(index).getCurrentClass()));
            tv_section.setText(dsnlist.get(index).getCurrentSection());
            tv_dob.setText(dsnlist.get(index).getDob());
            tv_formB.setText(dsnlist.get(index).getFormB());
            tv_father_name.setText(dsnlist.get(index).getFathersName());
            tv_father_cnic.setText(dsnlist.get(index).getFatherNic());
            tv_father_occupation.setText(dsnlist.get(index).getFatherOccupation());
            tv_mother_name.setText(dsnlist.get(index).getMotherName());
            tv_mother_cnic.setText(dsnlist.get(index).getMotherNic());
            tv_mother_occupation.setText(dsnlist.get(index).getMotherOccupation());
            tv_guardian_name.setText(dsnlist.get(index).getGuardianName());
            tv_guardian_cnic.setText(dsnlist.get(index).getGuardianNic());
            tv_guardian_occupation.setText(dsnlist.get(index).getGuardianOccupation());
            tv_previous_school.setText(dsnlist.get(index).getPreviousSchoolName());
            tv_class_previous_school.setText(dsnlist.get(index).getPreviousSchoolClass());
            tv_address.setText(dsnlist.get(index).getAddress1());
            tv_contact.setText(dsnlist.get(index).getContactNumbers());

            if (dsnlist.get(index).getUnapprovedComments() != null) {
                tv_remarks.setText(dsnlist.get(index).getUnapprovedComments());
            }
            if (dsnlist.get(index).getApproved_by() != 0) {
                tv_unapproved_by.setText("" + dsnlist.get(index).getApproved_by());
            }
            if (dsnlist.get(index).getApproved_on() != null) {
                tv_unapproved_on.setText("" + dsnlist.get(index).getApproved_on());
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_student_view:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        working();
    }
}
