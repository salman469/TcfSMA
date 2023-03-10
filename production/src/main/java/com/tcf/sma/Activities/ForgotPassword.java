package com.tcf.sma.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity
        implements View.OnClickListener {
    Button generatePinCode;
    Toolbar toolbar;
    EditText cnic, employeeCode;
    int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");


        initialize();
    }


    private void initialize() {
        generatePinCode = (Button) findViewById(R.id.bt_generate_pin_code);
        generatePinCode.setOnClickListener(this);
        cnic = findViewById(R.id.et_cnic);
        employeeCode = findViewById(R.id.et_employeeCode);
//        int len;
        cnic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (len > cnic.getText().length() ){
                    len--;
                    return;
                }

                len = cnic.getText().length();
                if (len == 6 || len== 14) {
                    String number = cnic.getText().toString();
                    String dash = number.charAt(number.length() - 1) == '-' ? "" : "-";
                    number = number.substring(0, (len - 1)) + dash + number.substring((len - 1), number.length());
                    cnic.setText(number);
                    cnic.setSelection(number.length());
                }
            }
        });

    }

    public boolean validate() {
        int errorCount = 0;
        if (TextUtils.isEmpty(cnic.getText())) {
            cnic.setError("Enter CNIC");
            errorCount++;
        }

        if (TextUtils.isEmpty(employeeCode.getText())) {
            employeeCode.setError("Enter Employee Code");
            errorCount++;
        }

        return errorCount == 0;
    }

    private void enableGeneratePinSubmitButton(boolean enabled) {
        generatePinCode.setEnabled(enabled);
        generatePinCode.setClickable(enabled);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_generate_pin_code:
                if (validate()) {
                    if (!AppModel.getInstance().isConnectedToInternet(ForgotPassword.this)) {
                        Toast.makeText(this, "No internet connectivity!\n Please connect to internet", Toast.LENGTH_SHORT).show();
                    } else {
//                        enableGeneratePinSubmitButton(false);
                        new Thread(this::generatePin).start();
                    }
                }
                break;
        }

    }

    private void generatePin() {
        try {
            ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

            Call<ResponseBody> call = apiInterface.forgotPassword(employeeCode.getText().toString(), cnic.getText().toString());
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Password is sent to your mobile number", Toast.LENGTH_SHORT).show();

                    }
                });
//                MessageBox("Password is sent to your mobile number", true, null);
                finish();
            } else {
                Toast.makeText(this, Objects.requireNonNull(response.errorBody()).toString(), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Try logging in with last remembered password first.", Toast.LENGTH_SHORT).show());

            e.printStackTrace();
        }
    }


    public void MessageBox(String message, final boolean finishOnClose, final Intent intentToStart) {
        android.app.AlertDialog msg = new android.app.AlertDialog.Builder(this).create();
//        msg.setTitle("Warning");
        msg.setCancelable(false);
        msg.setMessage(message);
        msg.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (intentToStart != null) startActivity(intentToStart);
                if (finishOnClose) finish();
            }
        });
        try {
            msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
