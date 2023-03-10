package com.tcf.sma.Activities.Expense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.R;

public class TransferToHoActivity extends DrawerActivity {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_transfer_to_ho);
        setToolbar("Transfer to Ho",this, false);
    }
}