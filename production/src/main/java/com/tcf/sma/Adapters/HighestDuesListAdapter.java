package com.tcf.sma.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.CashReceived.CashReceiptActivity;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.FinanceCheckSum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Badar Arain on 3/17/2017.
 */

public class HighestDuesListAdapter extends RecyclerView.Adapter<HighestDuesListAdapter.BottomFiveViewHolder> {
    Context context;
    ArrayList<HighestDuesStudentsModel> mmodelList;
    private String schoolId;

    public HighestDuesListAdapter(ArrayList<HighestDuesStudentsModel> modelLsit, Context context) {
        this.context = context;
        this.mmodelList = modelLsit;
    }

    public HighestDuesListAdapter(ArrayList<HighestDuesStudentsModel> modelLsit, Context context, String schoolId) {
        this.context = context;
        this.mmodelList = modelLsit;
        this.schoolId = schoolId;
    }

    @Override
    public void onViewRecycled(HighestDuesListAdapter.BottomFiveViewHolder holder) {
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
//                holder.schoolName.setText("School: "+mmodelList.get(position).getSchoolName());
                holder.bottom5Name.setText(mmodelList.get(position).getStudntsName());
                holder.bottom5GrNo.setText(mmodelList.get(position).getStudentGr_NO());

                holder.bottom5ClassSec.setText(mmodelList.get(position).getClassName() + "-" + mmodelList.get(position).getSectionName());
                holder.bottom5Absent.setText(mmodelList.get(position).getAmount());
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
        LinearLayout ll;
        TextView schoolName, bottom5Name, bottom5GrNo, bottom5ClassSec, bottom5Absent;

        public BottomFiveViewHolder(View itemView) {
            super(itemView);
//            schoolName = (TextView) itemView.findViewById(R.id.schoolName);
            ll = itemView.findViewById(R.id.ll);
            bottom5Name = itemView.findViewById(R.id.name5);
            bottom5GrNo = itemView.findViewById(R.id.gr_No5);
            bottom5ClassSec = itemView.findViewById(R.id.class_section5);
            bottom5Absent = itemView.findViewById(R.id.absent5);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FinanceCheckSum.Instance(new WeakReference<>(context)).isChecksumSuccess(context, true)) {
                        Intent receiveCashIntent = new Intent(context, CashReceiptActivity.class);
                        receiveCashIntent.putExtra("gr", mmodelList.get(getAdapterPosition()).getStudentGr_NO());
                        receiveCashIntent.putExtra("schoolId", Integer.parseInt(schoolId));
                        context.startActivity(receiveCashIntent);
                    }
                }
            });
        }
    }
}
