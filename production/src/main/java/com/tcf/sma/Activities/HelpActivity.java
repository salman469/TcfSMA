package com.tcf.sma.Activities;

import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tcf.sma.Activities.Help.AboutUsActivity;
import com.tcf.sma.R;

public class HelpActivity extends DrawerActivity implements View.OnClickListener {

    private View view;
    private CardView cv1,cv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_help);
        view = setActivityLayout(this,R.layout.activity_help);
        setToolbar(getString(R.string.help_feedback),this,false);

        initialize();
    }

    private void initialize() {
        cv1 = view.findViewById(R.id.cv1);
        cv2 = view.findViewById(R.id.cv2);

        cv1.setOnClickListener(this);
        cv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv1:
                openAppInPlayStore();
                break;
            case R.id.cv2:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
        }
    }

    private void openAppInPlayStore() {
        Uri uri = Uri.parse("market://details?id="+ this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(goToMarket);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id"+this.getPackageName())));
        }
    }
}