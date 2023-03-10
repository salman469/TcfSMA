package com.tcf.sma.Managers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;
import com.tcf.sma.Activities.StudentTransferActivity;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentTransferModel;
import com.tcf.sma.R;

import java.util.Arrays;

public class StudentTransferBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView tv_GrNo, tv_Name, tv_previous_school, tv_previous_class_section,tv_monthly_fees;
    private ImageView std_img;
    private Button btn_continue;
    private LinearLayout ll_monthly_fees;

    private StudentTransferModel sm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_transfer_bottom_sheet_layout, container, false);
        return view;
    }


    public void setStudentDetails(StudentTransferModel sm) {
        this.sm = sm;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_GrNo = view.findViewById(R.id.tv_GrNo);
        tv_Name = view.findViewById(R.id.tv_Name);
        tv_previous_school = view.findViewById(R.id.tv_previous_school);
        tv_previous_class_section = view.findViewById(R.id.tv_previous_class_section);
        ll_monthly_fees = view.findViewById(R.id.ll_monthly_fees);
        tv_monthly_fees = view.findViewById(R.id.tv_monthly_fees);
        std_img = view.findViewById(R.id.std_img);

        btn_continue = view.findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);

        if (sm != null) {
            tv_GrNo.setText(sm.getGrNo());
            tv_Name.setText(sm.getStd_Name());
            tv_previous_school.setText(sm.getPreviousSchoolName());
            tv_previous_class_section.setText(sm.getPreviousSchoolClass());

            if (sm.getAllowedModule_App() != null && !sm.getAllowedModule_App().isEmpty() && Arrays.asList(sm.getAllowedModule_App().split(",")).contains(AppConstants.FinanceModuleValue)){
                ll_monthly_fees.setVisibility(View.VISIBLE);
                tv_monthly_fees.setText(sm.getActualFees()+"");
            }else {
                ll_monthly_fees.setVisibility(View.GONE);
            }

            try {
                if (sm.getPictureName() != null && !sm.getPictureName().isEmpty()) {
                    String url = AppModel.getInstance().readFromSharedPreferences(getActivity(), AppConstants.imagebaseurlkey) + sm.getPictureName();
                    Picasso.with(getContext()).load(url).noFade().into(std_img);
//                std_img.setImageBitmap(AppModel.getInstance().setImage("P", sm.getPictureName(), getActivity(), Integer.parseInt(sm.getGrNo())));
                }else {
                    std_img.setImageDrawable(getResources().getDrawable(R.mipmap.profile_pic));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continue) {
            if (sm != null) {
                dismiss();
                Intent intent = new Intent(getActivity(), StudentTransferActivity.class);
                intent.putExtra("studentModel", sm);
                startActivity(intent);
            }
        }
    }
}

