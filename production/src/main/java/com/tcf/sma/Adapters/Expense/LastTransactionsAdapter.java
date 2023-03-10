package com.tcf.sma.Adapters.Expense;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.Expense.TransactionListing;
import com.tcf.sma.Models.RetrofitModels.Expense.LastTransactionsModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class LastTransactionsAdapter extends RecyclerView.Adapter<LastTransactionsAdapter.MyViewHolder> {

    private ArrayList<LastTransactionsModel> arrayList ;
    private Context acontext;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_head, tv_sub_head,tv_bucketntranstype,tv_amount,tv_date,tv_showmore;

        MyViewHolder(View v){
            super(v);
            tv_head= (TextView) v.findViewById(R.id.tv_head);
            tv_sub_head = (TextView) v.findViewById(R.id.tv_sub_head);
            tv_bucketntranstype = (TextView) v.findViewById(R.id.tv_bucketntranstype);
            tv_amount = (TextView) v.findViewById(R.id.tv_amount);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_showmore = (TextView) v.findViewById(R.id.tv_showmore);
            itemView.setOnClickListener(this);
            tv_showmore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == tv_showmore.getId()) {
                acontext.startActivity(new Intent(acontext, TransactionListing.class));
            }
        }
    }

    public LastTransactionsAdapter(Context context, ArrayList<LastTransactionsModel> arrayList) {
        this.arrayList = arrayList;
        acontext = context;

    }
    @Override
    public LastTransactionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_lasttransactions_data, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        if (position == arrayList.size() - 1){
            holder.tv_showmore.setVisibility(View.VISIBLE);
        }
        LastTransactionsModel current = arrayList.get(position);
        holder.tv_head.setText(current.getHead());
        holder.tv_sub_head.setText(current.getSub_head());
        holder.tv_bucketntranstype.setText(String.format("%s (%s)", current.getBucket(), current.getTransactiontype()));

        holder.tv_amount.setText(String.format("%,d", current.getAmount()));
        holder.tv_date.setText(current.getDate());
        if(current.getTransactiontype().equalsIgnoreCase("Income")){
            holder.tv_amount.setTextColor(acontext.getResources().getColor(R.color.colorAccent));
        }
        else {
            holder.tv_amount.setTextColor(acontext.getResources().getColor(R.color.red_color));
        }
    }

    @Override
    public int getItemCount() { return arrayList.size(); }

}