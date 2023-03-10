package com.tcf.sma.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.UserInfo;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnChangePassword;
    Toolbar toolbar;
    EditText etCurrentPass, etNewPass, etRePass;
    TextView tvForgot, currPassShow, newPassShow, rePassShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");

        try {
            Intent intent = getIntent();
            int requestCode = intent.getExtras().getInt("requestCode");
            //Password date Expired
            if (requestCode == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Alert!")
                        .setMessage("Please Change Password your password is expired")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
            //Password change from Head Office
            else {
                new AlertDialog.Builder(this)
                        .setTitle("Alert!")
                        .setMessage("Please change your password with the password given by TCF Head Office")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initialize();
    }

    @Override
    public void onBackPressed() {
        if(getIntent().hasExtra("isFromLogin")) {
            if (!getIntent().getBooleanExtra("isFromLogin", true)) {
                super.onBackPressed();
                finish();
            }
        } else {
            super.onBackPressed();
            finish();
        }
    }

    private void initialize() {
        btnChangePassword = (Button) findViewById(R.id.bt_change_pass);
        etCurrentPass = (EditText) findViewById(R.id.et_current_password);
        etNewPass = (EditText) findViewById(R.id.et_new_password);
        etRePass = (EditText) findViewById(R.id.et_retype_password);
        tvForgot = (TextView) findViewById(R.id.forgot);

        btnChangePassword.setOnClickListener(this);
        tvForgot.setOnClickListener(this);

        currPassShow = (TextView) findViewById(R.id.currPassShow);
        newPassShow = (TextView) findViewById(R.id.newPassShow);
        rePassShow = (TextView) findViewById(R.id.rePassShow);

        currPassShow.setOnClickListener(this);
        newPassShow.setOnClickListener(this);
        rePassShow.setOnClickListener(this);

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_change_pass:

                if (AppModel.isConnnected(ChangePasswordActivity.this)) {
                    if (validation()) {

//                        long i = UserInfo.getInstance(this).changePassword(
//                                etNewPass.getText().toString().trim(),
//                                DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());

//                        if (i > 0) {

                        try {
//                                long j = UserInfo.getInstance(this).setPasswordChangeOnLogin(0, DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
//                                if (j < 0) {
//                                    Toast.makeText(getApplicationContext(), "Password Not Change Successfully!\nTry Again", Toast.LENGTH_SHORT).show();
//                                }
//                                else {

//                                    SurveyAppModel.getInstance().writeToSharedPreferences(this, AppConstants.Password_Changed, "1");
//                                final boolean[] passwordChangedSuccesfully = {false};
                            Thread uploadUserNewPasswordThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DataSync ds = new DataSync(ChangePasswordActivity.this);
                                    ds.uploadNewPassword(etNewPass.getText().toString().trim());
//                                        passwordChangedSuccesfully[0] =ds.uploadNewPassword();
                                }
                            });
                            uploadUserNewPasswordThread.start();
//                                if (passwordChangedSuccesfully[0]){
//                                    Toast.makeText(getApplicationContext(), "Password Successfully Changed", Toast.LENGTH_LONG).show();
//                                }
                            //Logout user
//                          //  Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
//                          //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                           //         | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                           //         | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                           // startActivity(intent);
//                            //SurveyAppModel.getInstance().disposeToken(ChangePasswordActivity.this);
//                           // SurveyAppModel.getInstance().removeSyncAccount(ChangePasswordActivity.this);
//                                startActivity(new Intent(ChangePasswordActivity.this, NewDashboardActivity.class));
//                                finish();

//                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Failed to Change Password Try Again!", Toast.LENGTH_SHORT).show();
//                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Change Password Please Connect to internet", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.forgot:
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Please Call BO/TCF Head office to reset your password")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.currPassShow:
                String currpassShowHideText = currPassShow.getText().toString();
                if (currpassShowHideText.equalsIgnoreCase("show")) {
                    etCurrentPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    currPassShow.setText("Hide");
                    currPassShow.setTextColor(getResources().getColor(R.color.light_red));

                } else if (currpassShowHideText.equalsIgnoreCase("hide")) {
                    etCurrentPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    currPassShow.setText("Show");
                    currPassShow.setTextColor(getResources().getColor(R.color.app_green));
                }
                break;
            case R.id.newPassShow:
                String newPassShowHideText = newPassShow.getText().toString();
                if (newPassShowHideText.equalsIgnoreCase("show")) {
                    etNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPassShow.setText("Hide");
                    newPassShow.setTextColor(getResources().getColor(R.color.light_red));
                } else if (newPassShowHideText.equalsIgnoreCase("hide")) {
                    etNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPassShow.setText("Show");
                    newPassShow.setTextColor(getResources().getColor(R.color.app_green));
                }
                break;
            case R.id.rePassShow:
                String rePassShowHideText = rePassShow.getText().toString();
                if (rePassShowHideText.equalsIgnoreCase("show")) {
                    etRePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    rePassShow.setText("Hide");
                    rePassShow.setTextColor(getResources().getColor(R.color.light_red));
                } else if (rePassShowHideText.equalsIgnoreCase("hide")) {
                    etRePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    rePassShow.setText("Show");
                    rePassShow.setTextColor(getResources().getColor(R.color.app_green));
                }
                break;
        }
    }

    private boolean validation() {
        ArrayList<String> prev_pass = new ArrayList<>();
        int i = 0;
        prev_pass = UserInfo.getInstance(this).getPreviousPasswords(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        String currentPass = AppModel.getInstance().encryptPassword(etCurrentPass.getText().toString().trim());
        String newPass = AppModel.getInstance().encryptPassword(etNewPass.getText().toString().trim());

        if (currentPass.isEmpty()) {
            etCurrentPass.setError("Field is Required");
            showToast(this,"Current Password Field is Required");
            i++;
        } else if (!prev_pass.get(0).equals(currentPass)) {
            etCurrentPass.setError("Password not matched please enter your current password");
            showToast(this,"Password not matched please enter your current password");
            i++;
        }

        if (etNewPass.getText().toString().trim().isEmpty()) {
            etNewPass.setError("Field is Required");
            showToast(this,"New Password Field is Required");
            i++;
        } else if (etNewPass.getText().toString().trim().length() < 8) {
            etNewPass.setError("Password Must be 8 characters long");
            showToast(this,"New Password Must be 8 characters long");
            i++;
        } else if (!isValidPassword(etNewPass.getText().toString().trim())) {
            etNewPass.setError("Password must Contains at least 1 number and 1 character");
            showToast(this,"New Password must Contains at least 1 number and 1 character");
            i++;
        } else if (prev_pass.contains(newPass)) {
            etNewPass.setError("You have entered previously used password");
            showToast(this,"You have entered previously used password in New Password");
            i++;
        }

        if (etRePass.getText().toString().trim().isEmpty()) {
            etRePass.setError("Field is Required");
            showToast(this,"Re-Type Password Field is Required");
            i++;
        } else if (!etNewPass.getText().toString().trim().equals(etRePass.getText().toString().trim())) {
            etRePass.setError("Password Not Matched");
            showToast(this,"Re-Type Password Not Matched");
            i++;
        }


        return i == 0;
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(\\w|[~!$&+,:;=?\\[\\]@#_|<>{}()^%*\\.\\-\\+]){5,15}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}