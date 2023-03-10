package com.tcf.sma.Adapters.HRTCT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.R;

import java.util.List;

public class HRTCT_PreviousRegistrationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TCTEmpSubjectTaggingModel> empSubjectTaggingModels;

    public HRTCT_PreviousRegistrationAdapter(List<TCTEmpSubjectTaggingModel> empSubjectTaggingModels) {
        this.empSubjectTaggingModels = empSubjectTaggingModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_reg_item, parent, false);
        return new HRTCT_PrevRegAdapterVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        HRTCT_PreviousRegistrationAdapter.HRTCT_PrevRegAdapterVH HRTCT_PrevRegDataVH = (HRTCT_PreviousRegistrationAdapter.HRTCT_PrevRegAdapterVH) viewHolder;

        if (position == 0) {
            HRTCT_PrevRegDataVH.mandatory.setVisibility(View.VISIBLE);
            if (empSubjectTaggingModels.get(position).isMandatory())
                HRTCT_PrevRegDataVH.mandatory.setText("Mandatory Test Takers");
            else
                HRTCT_PrevRegDataVH.mandatory.setText("Remaining Eligible test takers");

        } else if (position > 0 && !empSubjectTaggingModels.get(position).isMandatory() && empSubjectTaggingModels.get(position - 1).isMandatory()) {
            HRTCT_PrevRegDataVH.mandatory.setText("Remaining Eligible test takers");
            HRTCT_PrevRegDataVH.mandatory.setVisibility(View.VISIBLE);
        } else
            HRTCT_PrevRegDataVH.mandatory.setVisibility(View.GONE);

        HRTCT_PrevRegDataVH.tv_empName.setText(empSubjectTaggingModels.get(position).getEMP_Name());
        HRTCT_PrevRegDataVH.tv_designation.setText(empSubjectTaggingModels.get(position).getDesignation_Name());
        if (!Strings.isEmptyOrWhitespace(empSubjectTaggingModels.get(position).getCNIC()))
            HRTCT_PrevRegDataVH.tv_comment.setText(empSubjectTaggingModels.get(position).getCNIC());
        else
            HRTCT_PrevRegDataVH.tv_comment.setText("");

        String subject1 = TCTHelperClass.getInstance(HRTCT_PrevRegDataVH.itemView.getContext()).getTCTSubjectById(empSubjectTaggingModels.get(position).getSubject1_ID());
        String subject2 = TCTHelperClass.getInstance(HRTCT_PrevRegDataVH.itemView.getContext()).getTCTSubjectById(empSubjectTaggingModels.get(position).getSubject2_ID());
        String reason = TCTHelperClass.getInstance(HRTCT_PrevRegDataVH.itemView.getContext()).getTCTReasonById(empSubjectTaggingModels.get(position).getReasonID());
        if (!Strings.isEmptyOrWhitespace(subject1))
            HRTCT_PrevRegDataVH.subject1.setText(subject1);
        else
            HRTCT_PrevRegDataVH.subject1.setText("-");
        if (!Strings.isEmptyOrWhitespace(subject2))
            HRTCT_PrevRegDataVH.subject2.setText(subject2);
        else
            HRTCT_PrevRegDataVH.subject2.setText("-");
        if (!Strings.isEmptyOrWhitespace(reason))
            HRTCT_PrevRegDataVH.reason.setText(reason);
        else
            HRTCT_PrevRegDataVH.reason.setText("-");

        if (empSubjectTaggingModels.get(position).isMandatory()) {
            if ((Strings.isEmptyOrWhitespace(subject2) && Strings.isEmptyOrWhitespace(subject1) && Strings.isEmptyOrWhitespace(reason))) {
                HRTCT_PrevRegDataVH.itemView.setBackgroundColor(ContextCompat.getColor(HRTCT_PrevRegDataVH.itemView.getContext(), R.color.light_red_color));
            }
        }


        if (empSubjectTaggingModels.get(position).getLeaveTypeID() > 0) {
            String leaveType = DatabaseHelper.getInstance(HRTCT_PrevRegDataVH.itemView.getContext()).getStringValueFromDB(
                    TCTHelperClass.TABLE_TCT_LEAVES_TYPE,
                    TCTHelperClass.TCT_TITLE,
                    TCTHelperClass.ID + " = " + empSubjectTaggingModels.get(position).getLeaveTypeID());

            HRTCT_PrevRegDataVH.tv_leaveType.setText(leaveType);
            HRTCT_PrevRegDataVH.ll_leave.setVisibility(View.VISIBLE);
        } else {
            HRTCT_PrevRegDataVH.ll_leave.setVisibility(View.GONE);
        }

        if (empSubjectTaggingModels.get(position).getNewDesignationId() > 0 && reason.equalsIgnoreCase("designation changed")) {
            String newDesignation = DatabaseHelper.getInstance(HRTCT_PrevRegDataVH.itemView.getContext()).getStringValueFromDB(
                    EmployeeHelperClass.TABLE_EmployeesDesignation,
                    EmployeeHelperClass.Designation_Name,
                    EmployeeHelperClass.ID + " = " + empSubjectTaggingModels.get(position).getNewDesignationId());

            HRTCT_PrevRegDataVH.tv_newDesignation.setText(newDesignation);
            HRTCT_PrevRegDataVH.ll_designation.setVisibility(View.VISIBLE);
        } else {
            HRTCT_PrevRegDataVH.ll_designation.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return empSubjectTaggingModels.size();
    }

    public static class HRTCT_PrevRegAdapterVH extends RecyclerView.ViewHolder {
        private TextView tv_empName, tv_designation, tv_newDesignation, tv_cnic, tv_leaveType, tv_comment, subject1, subject2, reason, mandatory;
        private CardView card_view;
        private LinearLayout ll_designation, ll_leave;

        HRTCT_PrevRegAdapterVH(@NonNull View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            tv_empName = itemView.findViewById(R.id.tv_empName);
            tv_designation = itemView.findViewById(R.id.tv_designation);
            tv_comment = itemView.findViewById(R.id.tv_comment);

            subject1 = itemView.findViewById(R.id.tv_subject1);
            subject2 = itemView.findViewById(R.id.tv_subject2);
            reason = itemView.findViewById(R.id.tv_reason);
            mandatory = itemView.findViewById(R.id.mandatory);

            tv_newDesignation = itemView.findViewById(R.id.tv_NewDesignation);
            tv_leaveType = itemView.findViewById(R.id.tv_leaveType);
            ll_leave = itemView.findViewById(R.id.ll_LeaveType);
            ll_designation = itemView.findViewById(R.id.ll_designationDropDown);
        }
    }
}
