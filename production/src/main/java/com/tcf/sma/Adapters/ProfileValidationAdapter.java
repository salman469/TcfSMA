package com.tcf.sma.Adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.AreaManagerStudentSelection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Badar Jamal on 2/6/2017.
 */

public class ProfileValidationAdapter extends RecyclerView.Adapter<ProfileValidationAdapter.ProfileValidationViewHolder> {
    public static ArrayList<StudentModel> smList;
    Activity activity;


    public ProfileValidationAdapter(ArrayList<StudentModel> smList, Activity activity) {
        this.smList = smList;
        this.activity = activity;

    }

    @Override
    public ProfileValidationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.student_view_list_cell, parent, false);
        return new ProfileValidationViewHolder(ItemView);

    }

    @Override
    public void onBindViewHolder(ProfileValidationViewHolder holder, int position) {

        String currentClass = StudentModel.getInstance().getStudentsList().get(position).getCurrentClass();
        if (currentClass.toLowerCase().contains("class")) {
            holder.tv_class_sec.setText(currentClass.split("\\-")[1] + "-" + StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        } else {
            holder.tv_class_sec.setText(currentClass);
        }
        holder.tv_grNo.setText(smList.get(position).getGrNo());
        holder.tv_student_name.setText(smList.get(position).getName());
        try {
            if (!smList.get(position).isApproved() && smList.get(position).getApproved_by() > 0) {
                holder.cv.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (smList != null) {
            return smList.size();
        } else {
            return 0;
        }

    }

    public class ProfileValidationViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class_sec, tv_grNo, tv_student_name;
        CardView cv;
        Intent intent;
        int count = 0;

        public ProfileValidationViewHolder(final View itemView) {
            super(itemView);
            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_student_name = (TextView) itemView.findViewById(R.id.tv_name_text);
            cv = (CardView) itemView.findViewById(R.id.st_card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppModel.getInstance().fragmentTag.equals(activity.getString(R.string.profile_validation))) {
                        count = getItemCount();
                        intent = new Intent(activity, AreaManagerStudentSelection.class);
                        intent.putExtra("count", count);
                        intent.putExtra("Postion", getAdapterPosition());
                        intent.putExtra("grno", smList.get(getAdapterPosition()).getGrNo());
                        (activity).startActivity(intent);
                    }
                }
            });


        }
    }
}
