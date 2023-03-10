package com.tcf.sma.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.AttendanceActivity;
import com.tcf.sma.Activities.FeesCollection.AttendancePendingActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.PendingAttendanceModel;
import com.tcf.sma.R;

import java.util.List;

/**
 * Created by Badar Arain on 3/17/2017.
 */

public class PendingAttendanceAdapter extends RecyclerView.Adapter<PendingAttendanceAdapter.PendingAttendanceViewHolder> {
    Context context;
    List<PendingAttendanceModel> models;
    boolean forToday;
    int roleID;

    public PendingAttendanceAdapter(List<PendingAttendanceModel> models, Context context, boolean forToday) {
        this.context = context;
        this.models = models;
        this.forToday = forToday;
        roleID = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();

    }

    @Override
    public void onViewRecycled(PendingAttendanceViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public PendingAttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.pending_attendance_cell, parent, false);
        return new PendingAttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PendingAttendanceViewHolder holder, final int position) {

        try {
            holder.tvSchool.setText(models.get(position).getSchool());
            holder.tvClass.setText(models.get(position).getClassName());
            holder.tvSection.setText(models.get(position).getSection());
            holder.tvAttendanceDate.setText(AppModel.getInstance().convertDatetoFormat(models.get(position).getDate(),
                    "yyyy-MM-dd", "dd-MMM-yy"));
//            if (!forToday) {
            if (models.get(position).isAttendaneMarked()) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_app_green));
//                    holder.tvSchool.setTextColor(ContextCompat.getColor(context, R.color.app_green));
//                    holder.tvSchool.setTextColor(ContextCompat.getColor(context,R.color.white_color));
//                    holder.tvClass.setTextColor(ContextCompat.getColor(context,R.color.white_color));
//                    holder.tvSection.setTextColor(ContextCompat.getColor(context,R.color.white_color));
//                    holder.tvAttendanceDate.setTextColor(ContextCompat.getColor(context,R.color.white_color));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
//                    holder.tvSchool.setTextColor(ContextCompat.getColor(context, R.color.light_red));
            }
//            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!forToday) {
//                        if (models.get(position).isAttendaneMarked()) {
//                            Toast.makeText(context, "Attendance is already taken for this date", Toast.LENGTH_SHORT).show();
//                        } else
                    try {
                        if (roleID != AppConstants.roleId_103_V && roleID != AppConstants.roleId_7_AEM) {
                            goToAttendanceActivity(position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    } else
//                        goToAttendanceActivity(position);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    private void goToAttendanceActivity(int position) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra("schoolId", models.get(position).getSchoolId());
        intent.putExtra("schoolClassId", models.get(position).getSchoolclass_id());
        intent.putExtra("forDate", models.get(position).getDate());
        ((AttendancePendingActivity) context).startActivity(intent);
    }

    public class PendingAttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvSchool, tvClass, tvSection, tvAttendanceDate;

        public PendingAttendanceViewHolder(View itemView) {
            super(itemView);
            tvSchool = (TextView) itemView.findViewById(R.id.tvSchool);
            tvClass = (TextView) itemView.findViewById(R.id.tvClass);
            tvSection = (TextView) itemView.findViewById(R.id.tvSection);
            tvAttendanceDate = (TextView) itemView.findViewById(R.id.tvAttendanceDate);
        }
    }
}
