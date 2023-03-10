package com.tcf.sma.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcf.sma.Activities.HR.EmployeeDetailsActivity;
import com.tcf.sma.Activities.HR.EmployeeSeparation;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.R;

public class UserProfileActivity extends DrawerActivity implements View.OnClickListener {

    View view;
    TextView empName, empDesignation, empDOJ, empCode, empCNIC;
    EditText empMobileNum, empEmail;
    UserModel userModel;
    EmployeeModel employeeModel;
    ImageView backBtn;
    Button saveChangesBtn, btnResign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this,R.layout.activity_user_profile);
        setToolbar("Edit Profile",this,true);
        init(view);
    }

    private void init(View view) {
        backBtn = view.findViewById(R.id.back);
        saveChangesBtn = view.findViewById(R.id.btn_close);
        btnResign = view.findViewById(R.id.btn_resign);
        backBtn.setOnClickListener(this);
        saveChangesBtn.setOnClickListener(this);
        btnResign.setOnClickListener(this);

        empName = view.findViewById(R.id.empName);
        empDesignation = view.findViewById(R.id.empDesignation);
        empCNIC = view.findViewById(R.id.empCNIC);
        empCode = view.findViewById(R.id.empCode);
        empDOJ = view.findViewById(R.id.empDOJ);
        empEmail = view.findViewById(R.id.empEmail);
        empMobileNum = view.findViewById(R.id.empMobileNumber);

        try {
            userModel = DatabaseHelper.getInstance(this).getCurrentLoggedInUser();
            empName.setText(userModel.getFirstname()+" " + userModel.getLastname());
            empDesignation.setText(userModel.getDesignation());

            employeeModel = EmployeeHelperClass.getInstance(this).getEmployee(userModel.getId());
            empCNIC.setText(employeeModel.getNIC_No());
            empCode.setText(employeeModel.getEmployee_Code());
            empDOJ.setText(AppModel.getInstance().convertDatetoFormat(employeeModel.getDate_Of_Joining(), "yyyy-MM-dd", "dd-MMM-yy"));
            empEmail.setText(employeeModel.getEmail());
            empMobileNum.setText(employeeModel.getMobile_No());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(EmployeeHelperClass.getInstance(this).employeeIsInactive(userModel.getId())){
            if(EmployeeHelperClass.getInstance(this).getResignTypeByReason(EmployeeHelperClass.getInstance(view.getContext()).getResignedEmployee(employeeModel.getId()).getEmp_SubReasonID()) == 1)
                btnResign.setText("Update/Cancel Resignation");
            else {
                btnResign.setEnabled(false);
                btnResign.setText("Separation already in progress");
            }
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.back:
                onBackPressed();
                break;

            case R.id.btn_resign:
                Intent intent = new Intent(UserProfileActivity.this, EmployeeSeparation.class);
                intent.putExtra("empDetailId", userModel.getId());
                intent.putExtra("schoolId", AppModel.getInstance().getSpinnerSelectedSchool(this));
                startActivity(intent);
                finish();
                break;

            case R.id.btn_close:
                if(employeeModel.getEmail() == null)
                    employeeModel.setEmail("");
                if(employeeModel.getMobile_No() == null)
                    employeeModel.setMobile_No("");
                if(!employeeModel.getEmail().equals(empEmail.getText().toString()) || !employeeModel.getMobile_No().equals(empMobileNum.getText().toString())){
                    if (validate()) {
                        long code = EmployeeHelperClass.getInstance(UserProfileActivity.this).updateEmployeeDetail(employeeModel);
                        if (code > 0) {
                            //Important when any change in table call this method
                            AppModel.getInstance().changeMenuPendingSyncCount(UserProfileActivity.this, true);
                            MessageBox("Employee updated successfully", true);

                        } else {
                            MessageBox("Something went wrong");
                        }

                    }
                } else{
                    ShowToast("Already saved");
                    onBackPressed();
                }

                break;
        }
    }


    private boolean validate() {
        int allOk = 0;

        if (!empMobileNum.getText().toString().trim().isEmpty()) {
            String mobNum = empMobileNum.getText().toString();
            if (mobNum.length() == 11 && mobNum.substring(0, 2).equals("03")) {
                employeeModel.setMobile_No(mobNum);
            } else {
                empMobileNum.setError("Please Enter Valid Mobile Number");
                allOk++;
            }
        } else {
            employeeModel.setMobile_No("");
        }

        if (!empEmail.getText().toString().trim().isEmpty()) {
            String strEmail = empEmail.getText().toString();
            if (strEmail.length() >= 5 && strEmail.contains(".") && strEmail.contains("@")) {
                employeeModel.setEmail(strEmail);
            } else {
                empEmail.setError("Please Enter Valid Email Address");
                allOk++;
            }
        } else {
            employeeModel.setEmail("");
        }

        return allOk == 0;
    }
}

