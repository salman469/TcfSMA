package com.tcf.sma.Adapters.HRTCT;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.HRTCT.PreviousRegistrationsActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTPhaseModel;
import com.tcf.sma.R;

import java.util.List;

public class HRTCT_PhasesListingAdapter extends RecyclerView.Adapter<HRTCT_PhasesListingAdapter.HRTCT_PhasesListingVH>{

    private final List<TCTPhaseModel> tctPhaseModelList;

    public HRTCT_PhasesListingAdapter(List<TCTPhaseModel> tctPhaseModelList) {
        this.tctPhaseModelList = tctPhaseModelList;
    }

    @NonNull
    @Override
    public HRTCT_PhasesListingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phases_listing_item, parent, false);
        return new HRTCT_PhasesListingVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HRTCT_PhasesListingVH holder, int position) {
        String academicSession = DatabaseHelper.getInstance(holder.itemView.getContext()).getAcademicSession(tctPhaseModelList.get(position).getAcademicSession_id());
        if (Strings.isEmptyOrWhitespace(academicSession)) {
            holder.phaseName.setText(tctPhaseModelList.get(position).getTCT_Phase());
        } else {
            holder.phaseName.setText(academicSession + " (" + tctPhaseModelList.get(position).getTCT_Phase() + ")");
        }
        holder.testDate.setText(AppModel.getInstance().convertDatetoFormat(tctPhaseModelList.get(position).getTctTestDate(), "yyyy-MM-dd HH:mm:ss","dd MMM yyyy"));
        holder.startDate.setText(AppModel.getInstance().convertDatetoFormat(tctPhaseModelList.get(position).getStart_Date(), "yyyy-MM-dd HH:mm:ss","dd MMM yyyy"));
        holder.endDate.setText(AppModel.getInstance().convertDatetoFormat(tctPhaseModelList.get(position).getEnd_date(), "yyyy-MM-dd HH:mm:ss","dd MMM yyyy"));
    }

    @Override
    public int getItemCount() {
        return tctPhaseModelList.size();
    }


    public class HRTCT_PhasesListingVH extends RecyclerView.ViewHolder {
        TextView phaseName, testDate, startDate, endDate;
        public HRTCT_PhasesListingVH(@NonNull View itemView) {
            super(itemView);
            phaseName = itemView.findViewById(R.id.tv_phaseName);
            testDate = itemView.findViewById(R.id.tv_testDate);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), PreviousRegistrationsActivity.class).putExtra("phaseId",tctPhaseModelList.get(getAdapterPosition()).getId()));
                }
            });
        }
    }
}
