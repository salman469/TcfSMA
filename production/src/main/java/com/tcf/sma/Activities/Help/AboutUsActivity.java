package com.tcf.sma.Activities.Help;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.LoginActivity;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;


public class AboutUsActivity extends DrawerActivity {

    TextView tvVersionNumber, tv_appDetail, tv_contactDetails, tv_contactNumber, tv_whatsAppNo, tv_emailid,tvDeviceId;
    View view;
    String version = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_about_us);
        setToolbar("About Us", this, false);

        tvVersionNumber = (TextView) view.findViewById(R.id.versionNumber);
        tv_appDetail = (TextView) view.findViewById(R.id.tv_appDetail);
        tv_contactNumber = (TextView) view.findViewById(R.id.tv_contactNumber);
        tv_whatsAppNo = (TextView) view.findViewById(R.id.tv_whatsAppNo);
        tv_emailid = (TextView) view.findViewById(R.id.tv_emailid);
        tvDeviceId = (TextView) view.findViewById(R.id.tvDeviceId);

        tvVersionNumber.setText(AppModel.getInstance().getAppVersionWithBuildNo(AboutUsActivity.this));

        String detail = "The Citizens Foundation School Management Application (TCF SMA) has been " +
                "designed to facilitate TCF principals and admin assistants in day to day school processes." +
                " This App is an initiative to enable easy and transparent record keeping, timely transfer of data" +
                " and information to the concerned departments and greater visibility at student level.";

//        detail+="\nTCF App version "+version+" includes Students and Finance Collection and Deposit modules.";

        final String contactDetails = "", contactNumber = "021-111-722-282", whatsappCntNo = "0336 11 11 823", emailId = "raabta@tcf.org.pk";

        tv_appDetail.setText(detail);
        tvDeviceId.setText(AppModel.getInstance().getDeviceId(AboutUsActivity.this));

        tv_contactNumber.setText(contactNumber);
        tv_contactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactNumber));
                startActivity(intent);
            }
        });

        tv_whatsAppNo.setText(whatsappCntNo);

        tv_emailid.setText(emailId);
        tv_emailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", emailId, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }
}
