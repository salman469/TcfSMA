package com.tcf.sma.Adapters.Expense;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.Expense.PettyCashCorrection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionListingAdapter extends RecyclerView.Adapter<TransactionListingAdapter.MyViewHolder> {

    private ArrayList<TransactionListingModel> arrayList, arrayListforsearch;
    private Context acontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_head, tv_sub_head, tv_transaction_type, tv_jvno, tv_amount, tv_date;

        MyViewHolder(View v) {
            super(v);
            tv_head = (TextView) v.findViewById(R.id.tv_head);
            tv_sub_head = (TextView) v.findViewById(R.id.tv_sub_head);
            tv_transaction_type = (TextView) v.findViewById(R.id.tv_transaction_type);
            tv_jvno = v.findViewById(R.id.tv_jvno);
            tv_amount = (TextView) v.findViewById(R.id.tv_amount);
            tv_date = (TextView) v.findViewById(R.id.tv_date);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent;
                    int position = getAdapterPosition();
                    if (arrayList.get(position).getHead().equals("Petty Cash") && arrayList.get(position).getCategory_id() == 1 && arrayList.get(position).isActive() == 1) {
                        intent = new Intent(acontext, PettyCashCorrection.class);
                        bundle.putString("eh", arrayList.get(position).getSub_head());
                        bundle.putInt("id", arrayList.get(position).getId());
                        bundle.putString("Head", arrayList.get(position).getHead());
                        bundle.putString("SubHead", arrayList.get(position).getSub_head());
                        bundle.putString("TransactionType", arrayList.get(position).getTransactiontype());
                        bundle.putString("Bucket", arrayList.get(position).getBucket());
                        bundle.putString("IO", arrayList.get(position).getI_o());
                        intent.putExtras(bundle);
                        acontext.startActivity(intent);
                    } /*else if (arrayList.get(position).getHead().equals("Advance")) {
                        intent = new Intent(acontext, Advance.class);
                        bundle.putString("expensehead", arrayList.get(position).getExpensehead());
                        bundle.putString("jvno", arrayList.get(position).getJvno());
                        bundle.putString("school", arrayList.get(position).getSchool());
                        bundle.putString("Head", arrayList.get(position).getHead());
                        bundle.putString("SubHead", arrayList.get(position).getSub_head());
                        bundle.putString("TransactionType", arrayList.get(position).getTransactiontype());
                        bundle.putString("Bucket", arrayList.get(position).getBucket());
                        bundle.putString("IO", arrayList.get(position).getI_o());
                        bundle.putInt("Amount", arrayList.get(position).getAmount());
                        bundle.putString("Checkno", arrayList.get(position).getCheckno());
                        bundle.putString("Balance", arrayList.get(position).getBalance());
                        intent.putExtras(bundle);
                        acontext.startActivity(intent);
                    } else if(arrayList.get(position).getHead().equals("Salary")){
                        intent = new Intent(acontext, Salary.class);
                        bundle.putString("employeename", arrayList.get(position).getEmployeename());
                        bundle.putString("employeecode", arrayList.get(position).getEmployeecode());
                        bundle.putString("expensehead", arrayList.get(position).getExpensehead());
                        bundle.putString("jvno", arrayList.get(position).getJvno());
                        bundle.putString("school", arrayList.get(position).getSchool());
                        bundle.putString("Head", arrayList.get(position).getHead());
                        bundle.putString("SubHead", arrayList.get(position).getSub_head());
                        bundle.putString("TransactionType", arrayList.get(position).getTransactiontype());
                        bundle.putString("Bucket", arrayList.get(position).getBucket());
                        bundle.putString("IO", arrayList.get(position).getI_o());
                        bundle.putInt("Amount", arrayList.get(position).getAmount());
                        bundle.putString("Checkno", arrayList.get(position).getCheckno());
                        intent.putExtras(bundle);
                        acontext.startActivity(intent);
                    }*/
                }
            });
        }
    }

    public TransactionListingAdapter(Context context, ArrayList<TransactionListingModel> arrayList) {
        this.arrayList = arrayList;
        acontext = context;

    }

    @Override
    public TransactionListingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transaction_listing_data, parent, false);
        return new TransactionListingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransactionListingModel current = arrayList.get(position);
        holder.tv_head.setText(current.getHead());
        holder.tv_sub_head.setText(current.getSub_head());
        holder.tv_transaction_type.setText(String.format("%s (%s)", current.getBucket(), current.getTransactiontype()));
        holder.tv_jvno.setText(current.getJvno());
        holder.tv_date.setText(current.getDate());
        if (current.getI_o().toLowerCase().equals("in")) {
            holder.tv_amount.setTextColor(acontext.getResources().getColor(R.color.colorAccent));
            holder.tv_amount.setText(AppModel.getInstance().formatNumberInCommas(current.getAmount()));
        } else {
            holder.tv_amount.setTextColor(acontext.getResources().getColor(R.color.red_color));
            holder.tv_amount.setText(String.format("-%s", AppModel.getInstance().formatNumberInCommas(current.getAmount())));
        }

        try {
            if (current.isActive() == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(acontext, R.color.light_red_color));
            } else if (current.isActive() == 1) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(acontext, R.color.light_blue_color));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(acontext, R.color.light_app_green));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<TransactionListingModel> results = new ArrayList<TransactionListingModel>();
                if (arrayListforsearch == null)
                    arrayListforsearch = arrayList;
                if (constraint != null) {
                    if (arrayListforsearch != null & arrayListforsearch.size() > 0) {
                        for (final TransactionListingModel t : arrayListforsearch) {
                            if (t.getJvno().toLowerCase().contains(constraint.toString()))
                                results.add(t);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<TransactionListingModel>) results.values;
                notifyDataSetChanged();

            }
        };
    }

}