package com.tcf.sma.Adapters.SyncProgress;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcf.sma.Activities.SyncProgress.PendingSyncActivity;
import com.tcf.sma.Models.SyncProgress.PendingSyncModel;
import com.tcf.sma.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PendingSyncAdapter extends RecyclerView.Adapter<PendingSyncAdapter.PendingSyncViewHolder> {
    List<PendingSyncModel> psmList;
    Activity activity;

    public PendingSyncAdapter(List<PendingSyncModel> psmList, Activity activity) {
        this.psmList = psmList;
        this.activity = activity;
    }

    @Override
    public PendingSyncAdapter.PendingSyncViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.pending_records_detail, parent, false);
        return new PendingSyncAdapter.PendingSyncViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingSyncViewHolder holder, int position) {

        holder.st_card_view.setBackgroundResource(R.color.light_app_green);

        if (psmList.get(position).getModule() != null && !psmList.get(position).getModule().isEmpty())
            holder.tv_module.setText(psmList.get(position).getModule());

        if (psmList.get(position).getSchoolId() > 0)
            holder.tv_schoolId.setText(psmList.get(position).getSchoolId() + "");

        if (psmList.get(position).getName() != null && !psmList.get(position).getName().isEmpty())
            holder.tv_name.setText(psmList.get(position).getName());

        if (psmList.get(position).getCreated_on() != null && !psmList.get(position).getCreated_on().isEmpty())
            holder.tv_createdOn.setText(psmList.get(position).getCreated_on());

        if (psmList.get(position).getModified_on() != null && !psmList.get(position).getModified_on().isEmpty())
            holder.tv_modifiedOn.setText(psmList.get(position).getModified_on());
    }


    @Override
    public int getItemCount() {
        if (psmList != null) {
            if (activity instanceof PendingSyncActivity) {
                ((PendingSyncActivity) activity).tv_total_pending_sync.setText(String.valueOf(psmList.size()));
            }
            return psmList.size();
        } else {
            if (activity instanceof PendingSyncActivity) {
                ((PendingSyncActivity) activity).tv_total_pending_sync.setText(" 0");
            }
            return 0;
        }
    }

    class PendingSyncViewHolder extends RecyclerView.ViewHolder {
        TextView tv_module, tv_schoolId, tv_name, tv_createdOn, tv_modifiedOn;
        CardView st_card_view;

        PendingSyncViewHolder(View itemView) {
            super(itemView);
            st_card_view = itemView.findViewById(R.id.st_card_view);
            tv_module = itemView.findViewById(R.id.tv_module);
            tv_schoolId = itemView.findViewById(R.id.tv_schoolId);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_createdOn = itemView.findViewById(R.id.tv_createdOn);
            tv_modifiedOn = itemView.findViewById(R.id.tv_modifiedOn);
        }
    }
}
