package com.tcf.sma.Activities.HR;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HR.SeparationAttachmentsAdapter;
import com.tcf.sma.Adapters.HR.SeparationHistoryAdapter;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ResignationApproval extends DrawerActivity {
    private TextView txt_ResStartDate, txt_ResDate, txt_ResName, txt_ResEmCode, txt_ResReason, txt_sepType, txt_subReason, txt_lwop, noAttachmentFound,
            txt_ResEmDesignation;
    private EmployeeModel employeeModel;
    private EmployeeSeparationModel erm;
    SeparationHistoryAdapter separationHistoryAdapter;
    List<EmployeeSeparationDetailModel> esdm;
    RecyclerView separationHistory;

    private RecyclerView rc_attachments;
    private SeparationAttachmentsAdapter separationAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = setActivityLayout(this, R.layout.activity_resignation_approval);
        setToolbar("Separation Details", this, false);
        init(view);


        if (employeeModel != null && erm != null) {
            try {
                txt_ResName.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
                txt_ResEmCode.setText(employeeModel.getEmployee_Code());
                txt_ResEmDesignation.setText(employeeModel.getDesignation());
                txt_ResStartDate.setText(AppModel.getInstance().convertDatetoFormat(erm.getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy"));
                txt_ResDate.setText(AppModel.getInstance().convertDatetoFormat(erm.getEmp_Resign_Date(), "yyyy-MM-dd", "dd-MMM-yy"));
                try {
                    txt_lwop.setText(erm.getLwop()+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int resignType = erm.getEmp_Resign_Type();
                String txtSepType = "";
                if (resignType==1)
                    txtSepType = "Resignation";
                else if (resignType == 2)
                    txtSepType = "Termination";
                else if (resignType == 3)
                    txtSepType = "Death";
                else
                    txtSepType = "Type not found";
                txt_sepType.setText(txtSepType);
                EmployeeResignReasonModel reason = EmployeeHelperClass.getInstance(view.getContext()).getResignReason(erm.getEmp_SubReasonID());
                if(reason!=null) {
                    txt_ResReason.setText(reason.getResignReason());
                    if(reason.getResignReason().contains("Others") && erm.getSubReasonText() != null)
                        txt_subReason.setText(erm.getSubReasonText());
                    else
                        txt_subReason.setText(reason.getSubReason());
                }
                else{
                    txt_ResReason.setHint("Reason not found");
                    txt_subReason.setHint("Sub Reason not found");
                }

               /* if(erm.getEmp_Resign_Form_IMG() != null){
                    File formPath = new File(erm.getEmp_Resign_Form_IMG());

                    byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(view.getContext()).compressToBitmap(formPath));
                    Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                    img_RecoForm.setImageBitmap(bitmapF);
                }
                if(erm.getEmp_Resign_Letter_IMG() != null){
                    File letterPath = new File(erm.getEmp_Resign_Letter_IMG());

                    byte[] dataL = AppModel.getInstance().bitmapToByte(Compressor.getDefault(view.getContext()).compressToBitmap(letterPath));
                    Bitmap bitmapL = BitmapFactory.decodeByteArray(dataL, 0, dataL.length);
                    img_ResLetter.setImageBitmap(bitmapL);
                }*/


//                img_RecoForm.setImageBitmap(SurveyAppModel.getInstance().rotateImage(Compressor.getDefault(view.getContext()).compressToBitmap(formPath), activity.getWindowManager().getDefaultDisplay()));
//                img_ResLetter.setImageBitmap(SurveyAppModel.getInstance().rotateImage(Compressor.getDefault(view.getContext()).compressToBitmap(letterPath), activity.getWindowManager().getDefaultDisplay()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void init(View view) {
        txt_ResName = view.findViewById(R.id.appr_txt_ResName);
        txt_ResEmCode = view.findViewById(R.id.appr_txt_ResEmCode);
        txt_ResEmDesignation = view.findViewById(R.id.appr_txt_ResEmDesignation);
        txt_ResStartDate = view.findViewById(R.id.appr_txt_ResStartDate);
        txt_ResDate = view.findViewById(R.id.appr_txt_ResDate);
        txt_ResReason = view.findViewById(R.id.appr_txt_Reason);
        txt_subReason = view.findViewById(R.id.appr_txt_subReason);
        txt_sepType = view.findViewById(R.id.sepType);
        noAttachmentFound = findViewById(R.id.noAttachmentsFound);
        txt_lwop = view.findViewById(R.id.appr_txt_lwop);
        Button btn_close = (Button) view.findViewById(R.id.appr_btn_close);
        separationHistory = view.findViewById(R.id.sepHistory);
        separationHistory.setNestedScrollingEnabled(false);
        separationHistory.setHasFixedSize(true);
        separationHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("resignationID")) {
            int resignationID = intent.getIntExtra("resignationID", 0);
            erm = EmployeeHelperClass.getInstance(view.getContext()).getSeparationRecord(resignationID);
            employeeModel = EmployeeHelperClass.getInstance(view.getContext()).getEmployee(erm.getEmployee_Personal_Detail_ID());
            esdm = EmployeeHelperClass.getInstance(view.getContext()).getSeparationHistory(resignationID);

            separationHistoryAdapter = new SeparationHistoryAdapter(ResignationApproval.this, esdm);
            separationHistory.setAdapter(separationHistoryAdapter);

            rc_attachments = view.findViewById(R.id.rv_attachments);
            rc_attachments.setNestedScrollingEnabled(false);
            rc_attachments.setHasFixedSize(true);
            rc_attachments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            SeparationAttachmentsAdapter.paths.clear();
            SeparationAttachmentsAdapter.paths = EmployeeHelperClass.getInstance(this).getSeparationAttachments(resignationID);
            attachments = SeparationAttachmentsAdapter.paths;
            if(attachments != null && attachments.size() > 0){
                SeparationAttachmentsAdapter adapter = new SeparationAttachmentsAdapter(true,this,attachments,0);
                rc_attachments.setAdapter(adapter);
            } else {
                rc_attachments.setVisibility(View.GONE);
                noAttachmentFound.setVisibility(View.VISIBLE);
            }



        }



    }
}
