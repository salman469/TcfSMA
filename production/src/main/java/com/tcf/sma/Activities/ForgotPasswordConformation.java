package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tcf.sma.R;

public class ForgotPasswordConformation extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button changePassword;
    EditText passwoed, conformPassword;
    CheckBox showpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_confirmation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        intialize();
    }

    private void intialize() {
        passwoed = (EditText) findViewById(R.id.et_password);
        conformPassword = (EditText) findViewById(R.id.et_conform_passwordPassword);
        showpassword = (CheckBox) findViewById(R.id.show_password);

        changePassword = (Button) findViewById(R.id.bt_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        changePassword.setOnClickListener(this);
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwoed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conformPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    passwoed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conformPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change_password:
                startActivity(new Intent(ForgotPasswordConformation.this, LoginActivity.class));
                break;


        }

    }
}
