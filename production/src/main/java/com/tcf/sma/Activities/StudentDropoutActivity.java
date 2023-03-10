package com.tcf.sma.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.WithdrawalModel;
import com.tcf.sma.Models.WithdrawalReasonModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StudentDropoutActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View view;
    Button btn_student_dropout;
    Spinner spinner_wr;
    int spinner_wr_value;
    ImageView header;
    ArrayAdapter<WithdrawalReasonModel> WithdrawalReasonAdapter;
    TextView tv_student_name, tv_section_student_Gender, tv_student_GR_No, date_of_admission,
            tv_class, tv_section, tv_dob, tv_formB, tv_father_name, tv_father_cnic,
            tv_father_occupation, tv_mother_name, tv_mother_cnic, tv_mother_occupation,
            tv_guardian_name, tv_guardian_cnic, tv_guardian_occupation, tv_previous_school,
            tv_class_previous_school, tv_address, tv_contact;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_dropout2);
        view = setActivityLayout(this, R.layout.activity_student_dropout);
        setToolbar(getString(R.string.student_dropout_text), this, false);

        init(view);
        initSpinners();
    }

    private void init(View v) {
        Intent intent = getIntent();
        if (intent != null)
            index = intent.getIntExtra("StudentDropoutIndex", -1);

//        StudentModel.getInstance().setStudent(StudentModel.getInstance().getStudentsList().get(index));

        spinner_wr = (Spinner) v.findViewById(R.id.spinner_wr);
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

        btn_student_dropout = (Button) v.findViewById(R.id.btn_student_dropout);
        btn_student_dropout.setOnClickListener(this);


//        tv_student_name.setText("Ali Saqib");
//        tv_section_student_Gender.setText("Male");
//        tv_student_GR_No.setText("516");
//        date_of_admission.setText("1 May 2010");
//        tv_class.setText("4");
//        tv_section.setText("D");
//        tv_dob.setText("16 Apr 1995");
//        tv_formB.setText("00-510-2143042");
//        tv_father_name.setText("Saqib Shahzad");
//        tv_father_cnic.setText("12345-1234567-8");
//        tv_father_occupation.setText("Technician");
//        tv_mother_name.setText("Shama Saqib");
//        tv_mother_cnic.setText("12345-1234567-8");
//        tv_mother_occupation.setText("House wife");
//        tv_guardian_name.setText("Shama Saqib");
//        tv_guardian_cnic.setText("12345-1234567-8");
//        tv_guardian_occupation.setText("House wife");
//        tv_previous_school.setText("The City School");
//        tv_class_previous_school.setText("4");
//        tv_address.setText("Saddar, Karachi, Pakistan");
//        tv_contact.setText("0335-5648526");


    }

    private void initSpinners() {
        WithdrawalReasonModel reasonModel = new WithdrawalReasonModel();
        reasonModel.setWrmList(DatabaseHelper.getInstance(this).getWithdrawalReasons(true));
        reasonModel.getWrmList().add(0, new WithdrawalReasonModel(0, "Select Reason"));


        WithdrawalReasonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reasonModel.getWrmList());
        WithdrawalReasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_wr.setAdapter(WithdrawalReasonAdapter);
        spinner_wr.setOnItemSelectedListener(this);

    }

    private void working() {
//        String admissionDate = SurveyAppModel.getInstance().convertDatetoFormat(StudentModel.getInstance().getStudentsList().get(index).getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
//        String DateOfBirth = SurveyAppModel.getInstance().convertDatetoFormat(StudentModel.getInstance().getStudentsList().get(index).getDob(), "yyyy-MM-dd", "dd-MM-yyyy");
        String addmissionDate = "";
        String DateOfBirth = "";
        if (!StudentModel.getInstance().getStudentsList().isEmpty() && StudentModel.getInstance().getStudentsList().size() > index) {
            if (StudentModel.getInstance().getStudentsList().get(index).getEnrollmentDate() != null)
                addmissionDate = AppModel.getInstance().convertDatetoFormat(StudentModel.getInstance().getStudentsList().get(index).getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
            if (StudentModel.getInstance().getStudentsList().get(index).getDob() != null)
                DateOfBirth = AppModel.getInstance().convertDatetoFormat(StudentModel.getInstance().getStudentsList().get(index).getDob(), "yyyy-MM-dd", "dd-MM-yyyy");
        }

        tv_student_name.setText(StudentModel.getInstance().getStudentsList().get(index).getName());
        tv_section_student_Gender.setText(StudentModel.getInstance().getStudentsList().get(index).getGender());
        tv_student_GR_No.setText(StudentModel.getInstance().getStudentsList().get(index).getGrNo());
//        date_of_admission.setText(addmissionDate);
        date_of_admission.setText(AppModel.getInstance().convertDatetoFormat(addmissionDate, "dd-MM-yyyy", "dd-MMM-yy"));
        tv_class.setText(String.valueOf(StudentModel.getInstance().getStudentsList().get(index).getCurrentClass()));
        tv_section.setText(StudentModel.getInstance().getStudentsList().get(index).getCurrentSection());
//        tv_dob.setText(DateOfBirth);
        tv_dob.setText(AppModel.getInstance().convertDatetoFormat(DateOfBirth, "dd-MM-yyyy", "dd-MMM-yy"));
        tv_formB.setText(StudentModel.getInstance().getStudentsList().get(index).getFormB());
        tv_father_name.setText(StudentModel.getInstance().getStudentsList().get(index).getFathersName());
        tv_father_cnic.setText(StudentModel.getInstance().getStudentsList().get(index).getFatherNic());
        tv_father_occupation.setText(StudentModel.getInstance().getStudentsList().get(index).getFatherOccupation());
        tv_mother_name.setText(StudentModel.getInstance().getStudentsList().get(index).getMotherName());
        tv_mother_cnic.setText(StudentModel.getInstance().getStudentsList().get(index).getMotherNic());
        tv_mother_occupation.setText(StudentModel.getInstance().getStudentsList().get(index).getMotherOccupation());
        tv_guardian_name.setText(StudentModel.getInstance().getStudentsList().get(index).getGuardianName());
        tv_guardian_cnic.setText(StudentModel.getInstance().getStudentsList().get(index).getGuardianNic());
        tv_guardian_occupation.setText(StudentModel.getInstance().getStudentsList().get(index).getGuardianOccupation());
        tv_previous_school.setText(StudentModel.getInstance().getStudentsList().get(index).getPreviousSchoolName());
        tv_class_previous_school.setText(StudentModel.getInstance().getStudentsList().get(index).getPreviousSchoolClass());
        tv_address.setText(StudentModel.getInstance().getStudentsList().get(index).getAddress1());
        tv_contact.setText(StudentModel.getInstance().getStudentsList().get(index).getContactNumbers());
        try {
            header.setImageBitmap(AppModel.getInstance()
                    .setImage("P",
                            StudentModel.getInstance().getStudentsList().get(index).getPictureName(),
                            this, Integer.parseInt(StudentModel.getInstance().getStudentsList().get(index).getGrNo()),
                            false));
        } catch (Exception e) {
            header.setImageDrawable(getResources().getDrawable(R.mipmap.profile_pic));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_student_dropout:
                if (!spinner_wr.getSelectedItem().toString().equals("Select Reason")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //  builder.setTitle("Dialog");
                    builder.setMessage("Are you sure you want to Withdraw/Dropout this student?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            withdrawStudent();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(this, "Please select reason for drop-off", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void withdrawStudent() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        WithdrawalModel wModel = new WithdrawalModel();
        wModel.setStudent_id(StudentModel.getInstance().getStudentsList().get(index).getId());
        wModel.setReason_id(spinner_wr_value);
        wModel.setCreated_by(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId()); // logged in user id to be used here. Using hardcoded now.
        wModel.setCreated_on(date);
        wModel.setSchool_id(AppModel.getInstance().getSelectedSchool(this));

        long i = DatabaseHelper.getInstance(this).withdrawStudent(wModel);

        if (i > 0) {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.getInstance(this).STUDENT_IS_ACTIVE, false);
            values.put(DatabaseHelper.getInstance(this).STUDENT_IS_WITHDRAWN, true);
            values.put(DatabaseHelper.getInstance(this).STUDENT_WITHDRAWN_ON, date);
            values.put(DatabaseHelper.getInstance(this).STUDENT_WITHDRAWN_REASON_ID, spinner_wr_value);
            values.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_BY, wModel.getCreated_by());
            values.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_ON, wModel.getCreated_on());
            values.put(DatabaseHelper.getInstance(this).STUDENT_UPLOADED_ON, (String) null);

            i = DatabaseHelper.getInstance(this).updateStudentByColumns(values, StudentModel.getInstance().getStudentsList().get(index).getId());
            if (i > 0) {
                //Important when any change in table call this method
                AppModel.getInstance().changeMenuPendingSyncCount(StudentDropoutActivity.this, false);

                MessageBox("Student Dropout Successfully!", true);


            } else {
                MessageBox("Something went wrong please try again later!");
                DatabaseHelper.getInstance(this).undoWithdrawalRecord();
            }
        } else {
            MessageBox("Something went wrong please try again later!");
        }


//        et_dateofadmission.getText().toString().trim();
//        ClassSpinner.getText().toString().trim();
//        SectionSpinner.getText().toString().trim();
//        et_fathername.getText().toString().trim();
//        et_fathercnic.getText().toString().trim();
//        et_father_occupation.getText().toString().trim();
//        et_mothername.getText().toString().trim();
//        et_mother_cnic.getText().toString().trim();
//        et_mother_occupation.getText().toString().trim();
//        et_guardian_name.getText().toString().trim();
//        et_guardian_cnic.getText().toString().trim();
//        et_guardian_occupation.getText().toString().trim();
//        et_previous_school.getText().toString().trim();
//        et_class_in_previous_school.getText().toString().trim();
//        et_address.getText().toString().trim();
//        et_contact.getText().toString().trim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        working();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_wr:
                spinner_wr_value = ((WithdrawalReasonModel) adapterView.getItemAtPosition(position)).getId();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
