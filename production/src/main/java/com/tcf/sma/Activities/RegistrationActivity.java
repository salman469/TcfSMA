package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tcf.sma.R;

/**
 * Created by badar arain on 1/10/2017.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userName, password, conformPassword;
    Button register;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Sign Up");
        init();
    }

    private void init() {
        userName = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        conformPassword = (EditText) findViewById(R.id.et_conform_password);
        register = (Button) findViewById(R.id.bt_register);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                finish();
                break;
        }
    }
}