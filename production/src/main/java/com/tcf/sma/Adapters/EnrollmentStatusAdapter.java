package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.NewAdmissionActivity;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/12/2017.
 */

public class EnrollmentStatusAdapter extends RecyclerView.Adapter<EnrollmentStatusAdapter.EnrollmentStatusViewHolder> {
    ArrayList<EnrollmentModel> modelList = new ArrayList<>();
    Activity activity;

    public EnrollmentStatusAdapter(ArrayList<EnrollmentModel> modelList, Activity activity) {
        this.modelList = modelList;
        this.activity = activity;
    }

    @Override
    public EnrollmentStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enrollmentstatus_cell, parent, false);
        View ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enrollment_cell, parent, false);
        return new EnrollmentStatusViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(EnrollmentStatusViewHolder holder, final int position) {
        holder.tv_gr.setText("" + modelList.get(position).getENROLLMENT_GR_NO());
//        holder.tv_postDate.setText(SurveyAppModel.getInstance().
//                convertDatetoFormat(modelList.get(position).getENROLLMENT_CREATED_ON(),"yyyy-MM-dd","dd-MM-yyyy"));
        holder.tv_postDate.setText(AppModel.getInstance().
                convertDatetoFormat(modelList.get(position).getENROLLMENT_CREATED_ON(), "yyyy-MM-dd", "dd-MMM-yy"));
        final String status = modelList.get(position).getENROLLMENT_REVIEW_STATUS() == null ? "" : modelList.get(position).getENROLLMENT_REVIEW_STATUS();
        if (status != null) {
            holder.tv_status.setText(status);
            if (status.equals(AppConstants.PROFILE_INCOMPLETE_KEY)) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.blue_color));
                holder.tv_status.setText("Incomplete");
                holder.st_card_view.setBackgroundColor(activity.getResources().getColor(R.color.light_blue_color));
            } else if (status.equals(AppConstants.PROFILE_REJECTED_KEY)) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.red_color));
                holder.tv_status.setText("Rejected");
                holder.st_card_view.setBackgroundColor(activity.getResources().getColor(R.color.light_red_color));
            } else if (status.equals(AppConstants.PROFILE_COMPLETE_KEY)) {
                if (modelList.get(position).getENROLLMENT_UPLOADED_ON() == null || modelList.get(position).getENROLLMENT_UPLOADED_ON().equals("")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.app_green));
                    holder.st_card_view.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
                    holder.tv_status.setText("Pending Sync");
                } else {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.app_green));
                    holder.st_card_view.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
                    holder.tv_status.setText("Sent to HO");
                }

            } else if (status.equals(AppConstants.PROFILE_PENDING)) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.app_green));
                holder.st_card_view.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
                holder.tv_status.setText("Sent to HO");
            } else {
                if (modelList.get(position).getENROLLMENT_UPLOADED_ON() != null && !modelList.get(position).getENROLLMENT_UPLOADED_ON().equals("")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.app_green));
                    holder.st_card_view.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
                    holder.tv_status.setText("Sent to HO");
                }
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, NewAdmissionActivity.class);
                i.putExtra("gr", "" + modelList.get(position).getENROLLMENT_GR_NO());
                i.putExtra("enr_id", modelList.get(position).getID());
                i.putExtra("name", modelList.get(position).getStudentName());
                i.putExtra("class_section_id", modelList.get(position).getClass_section_id());
                i.putExtra("status", status);
                i.putExtra("comments", modelList.get(position).getENROLLMENT_REVIEW_COMMENTS());
                i.putExtra("school_id", modelList.get(position).getENROLLMENT_SCHOOL_ID());
                i.putExtra("uploaded_on", modelList.get(position).getENROLLMENT_UPLOADED_ON());
                i.putExtra("gender", modelList.get(position).getGender());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class EnrollmentStatusViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gr, tv_postDate, tv_status;
        CardView st_card_view;

        public EnrollmentStatusViewHolder(View itemView) {
            super(itemView);
            tv_gr = (TextView) itemView.findViewById(R.id.tv_gr_no);
            tv_postDate = (TextView) itemView.findViewById(R.id.tv_post_date);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            st_card_view = (CardView) itemView.findViewById(R.id.st_card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
