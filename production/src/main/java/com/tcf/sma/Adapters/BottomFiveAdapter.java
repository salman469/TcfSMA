package com.tcf.sma.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Badar Arain on 3/17/2017.
 */

public class BottomFiveAdapter extends RecyclerView.Adapter<BottomFiveAdapter.BottomFiveViewHolder> {
    Context context;
    ArrayList<BottomFiveStudentsModel> mmodelList;

    public BottomFiveAdapter(ArrayList<BottomFiveStudentsModel> modelLsit, Context context) {
        this.context = context;
        this.mmodelList = modelLsit;
    }

    @Override
    public void onViewRecycled(BottomFiveAdapter.BottomFiveViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public BottomFiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.bottom_five_cell, parent, false);
        return new BottomFiveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BottomFiveViewHolder holder, int position) {
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
                holder.bottom5Absent.setText("" + mmodelList.get(position).getStudentsAbsentCounting());

                holder.contactNumber.setText(mmodelList.get(position).getContactNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mmodelList.size();
    }

    public class BottomFiveViewHolder extends RecyclerView.ViewHolder {
        TextView bottom5Name, bottom5GrNo, bottom5ClassSec, bottom5Absent ,contactNumber;

        public BottomFiveViewHolder(View itemView) {
            super(itemView);
            bottom5Name = (TextView) itemView.findViewById(R.id.name5);
            bottom5GrNo = (TextView) itemView.findViewById(R.id.gr_No5);
            bottom5ClassSec = (TextView) itemView.findViewById(R.id.class_section5);
            bottom5Absent = (TextView) itemView.findViewById(R.id.absent5);
            contactNumber = (TextView) itemView.findViewById(R.id.contactNumber);

            contactNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String contactNumber = mmodelList.get(getAdapterPosition()).getContactNumber();
                    if (contactNumber != null && !contactNumber.isEmpty()){
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" + contactNumber));
                        context.startActivity(dialIntent);
                    }
                }
            });
        }
    }
}
