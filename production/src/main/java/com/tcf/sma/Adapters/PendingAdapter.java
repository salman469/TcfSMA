package com.tcf.sma.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.BottomFiveViewHolder> {
    Context context;
    ArrayList<HashMap<String, Integer>> pendingRecords;

    public PendingAdapter(ArrayList<HashMap<String, Integer>> pendingRecords, Context context) {
        this.context = context;
        this.pendingRecords = pendingRecords;
    }

    @Override
    public void onViewRecycled(PendingAdapter.BottomFiveViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public PendingAdapter.BottomFiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.bottom_five_cell, parent, false);
        return new PendingAdapter.BottomFiveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PendingAdapter.BottomFiveViewHolder holder, int position) {

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return pendingRecords.size();
    }

    public class BottomFiveViewHolder extends RecyclerView.ViewHolder {
        TextView bottom5Name, bottom5GrNo, bottom5ClassSec, bottom5Absent;

        public BottomFiveViewHolder(View itemView) {
            super(itemView);
//            schoolName = (TextView) itemView.findViewById(R.id.schoolName);
            bottom5Name = (TextView) itemView.findViewById(R.id.name5);
            bottom5GrNo = (TextView) itemView.findViewById(R.id.gr_No5);
            bottom5ClassSec = (TextView) itemView.findViewById(R.id.class_section5);
            bottom5Absent = (TextView) itemView.findViewById(R.id.absent5);
        }
    }
}
