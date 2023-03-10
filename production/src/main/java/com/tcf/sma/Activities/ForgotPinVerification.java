package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tcf.sma.R;

public class ForgotPinVerification extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button pinVerificationSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin_verification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verification");
        initialize();
    }

    private void initialize() {
        pinVerificationSubmit = (Button) findViewById(R.id.bt_pin_verification_submit);
        pinVerificationSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pin_verification_submit:
                startActivity(new Intent(ForgotPinVerification.this, ForgotPasswordConformation.class));
                break;
        }
    }
}
