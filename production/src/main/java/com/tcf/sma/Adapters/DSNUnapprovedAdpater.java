package com.tcf.sma.Adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DSNUapprovedReason;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Badar Jamal on 2/6/2017.
 */

public class DSNUnapprovedAdpater extends RecyclerView.Adapter<DSNUnapprovedAdpater.DSNUnapprovedViewHolder> {
    public static ArrayList<StudentModel> mdsnList;
    Activity activity;


    public DSNUnapprovedAdpater(ArrayList<StudentModel> dsnList, Activity activity) {
        this.mdsnList = dsnList;
        this.activity = activity;

    }

    @Override
    public DSNUnapprovedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.student_view_list_cell, parent, false);
        return new DSNUnapprovedViewHolder(ItemView);

    }

    @Override
    public void onBindViewHolder(DSNUnapprovedViewHolder holder, int position) {
        if (mdsnList.get(position).getName() != null && mdsnList.get(position).getCurrentClass() != null && mdsnList.get(position).getGrNo() != null) {
            holder.tv_student_name.setText(mdsnList.get(position).getName());
            holder.tv_class_sec.setText(mdsnList.get(position).getCurrentClass());
            holder.tv_grNo.setText("" + mdsnList.get(position).getGrNo());

        }
    }

    @Override
    public int getItemCount() {
        return mdsnList.size();
    }


    public class DSNUnapprovedViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class_sec, tv_grNo, tv_student_name;
        Intent intent;
        int count = 0;

        public DSNUnapprovedViewHolder(final View itemView) {
            super(itemView);
            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_student_name = (TextView) itemView.findViewById(R.id.tv_name_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    count = getItemCount();
                    intent = new Intent(activity, DSNUapprovedReason.class);
                    intent.putExtra("id", mdsnList.get(getAdapterPosition()).getId());
                    intent.putExtra("Postion", getAdapterPosition());
                    intent.putExtra("grno", mdsnList.get(getAdapterPosition()).getGrNo());
                    intent.putExtra("studentName", mdsnList.get(getAdapterPosition()).getName());

                    (activity).startActivity(intent);
                }
            });


        }
    }
}
