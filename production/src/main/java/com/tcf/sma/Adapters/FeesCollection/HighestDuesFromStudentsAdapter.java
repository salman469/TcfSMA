package com.tcf.sma.Adapters.FeesCollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.R;

import java.util.ArrayList;


public class HighestDuesFromStudentsAdapter extends RecyclerView.Adapter<HighestDuesFromStudentsAdapter.HighestDuesFromStudentsViewHolder> {
    Context context;
    ArrayList<BottomFiveStudentsModel> mmodelList;

    public HighestDuesFromStudentsAdapter(ArrayList<BottomFiveStudentsModel> modelLsit, Context context) {
        this.context = context;
        this.mmodelList = modelLsit;
    }

    @Override
    public void onViewRecycled(HighestDuesFromStudentsAdapter.HighestDuesFromStudentsViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public HighestDuesFromStudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.bottom_five_cell, parent, false);
        return new HighestDuesFromStudentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HighestDuesFromStudentsViewHolder holder, int position) {
        try {
            if (mmodelList.get(position).getStudntsName() != null && mmodelList.get(position).getClassName() != null) {
                holder.bottom5Name.setText(mmodelList.get(position).getStudntsName());
                holder.bottom5GrNo.setText("" + mmodelList.get(position).getStudentGr_NO());
                String currentClass = mmodelList.get(position).getClassName();
                if (currentClass.toLowerCase().contains("class")) {
                    holder.bottom5ClassSec.setText(mmodelList.get(position).getClassName().split("-")[1] + "-" + " " + mmodelList.get(position).getSectionName());
                } else {
                    holder.bottom5ClassSec.setText(currentClass);
                }
                holder.bottom5Amount.setText("" + mmodelList.get(position).getStudentsAbsentCounting());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mmodelList.size();
    }

    public class HighestDuesFromStudentsViewHolder extends RecyclerView.ViewHolder {
        TextView bottom5Name, bottom5GrNo, bottom5ClassSec, bottom5Amount;

        public HighestDuesFromStudentsViewHolder(View itemView) {
            super(itemView);
            bottom5Name = (TextView) itemView.findViewById(R.id.name5);
            bottom5GrNo = (TextView) itemView.findViewById(R.id.gr_No5);
            bottom5ClassSec = (TextView) itemView.findViewById(R.id.class_section5);
            bottom5Amount = (TextView) itemView.findViewById(R.id.absent5);
        }
    }
}
