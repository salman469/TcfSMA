package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.R;

import java.util.List;

public class DepositDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private List<ViewReceivablesModels> models;
    private int schoolId;

    public DepositDetailAdapter(Activity context, List<ViewReceivablesModels> model, int schoolId) {
        this.context = context;
        this.models = model;
        this.schoolId = schoolId;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            default:
                return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.deposit_items_header, parent, false);
                return new DepositDetailAdapter.ReceiptHeadVH(itemView);
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_deposit_item, parent, false);
                return new DepositDetailAdapter.RecieptItemVH(itemView);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position > 0 && position < models.size() + 1) {
            DepositDetailAdapter.RecieptItemVH itemVH = (DepositDetailAdapter.RecieptItemVH) holder;

            if (!models.get(position - 1).getBalance().isEmpty())
                itemVH.etTotalAmount.setText(models.get(position - 1).getBalance());

            if (!models.get(position - 1).getTitle().isEmpty())
                itemVH.tv_title.setText(models.get(position - 1).getTitle());

        } else if (position == models.size() + 1) {
            DepositDetailAdapter.RecieptItemVH itemVH = (DepositDetailAdapter.RecieptItemVH) holder;
            itemVH.tv_title.setTypeface(itemVH.tv_title.getTypeface(), Typeface.BOLD);
            itemVH.tv_title.setText(context.getResources().getString(R.string.balance_after_reconciliation));
            itemVH.etTotalAmount.setTypeface(itemVH.etTotalAmount.getTypeface(), Typeface.BOLD);
//            itemVH.etTotalAmount.setText(getBalanceAfterReconciliation());
        } else if (position == models.size() + 2) {
            DepositDetailAdapter.RecieptItemVH itemVH = (DepositDetailAdapter.RecieptItemVH) holder;
            itemVH.tv_title.setTypeface(itemVH.tv_title.getTypeface(), Typeface.BOLD);
            itemVH.tv_title.setText("Total");
            itemVH.etTotalAmount.setTypeface(itemVH.etTotalAmount.getTypeface(), Typeface.BOLD);
            itemVH.etTotalAmount.setText(getTotal());
        }

    }

    private String getBalanceAfterReconciliation() {
        String cashInHand = FeesCollection.getInstance(context).getCashInHandData(schoolId + "").getTotal();
        int balAfterRec = 0;
        try {
            balAfterRec = Integer.parseInt(cashInHand) - Integer.parseInt(getTotal());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(balAfterRec);
    }

    public String getTotal() {
        double totalAmount = 0.0;

        for (ViewReceivablesModels model : models) {
            totalAmount += Double.parseDouble(model.getBalance());
        }
        String tAmount = String.valueOf(totalAmount);

        return tAmount.contains(".") ? tAmount.split("\\.")[0] : tAmount;
    }

    @Override
    public int getItemCount() {
        return models.size() + 3;
    }

    public class RecieptItemVH extends RecyclerView.ViewHolder {

        TextView tv_title;
        EditText etTotalAmount;
        int balance = 0;

        public RecieptItemVH(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            etTotalAmount = (EditText) itemView.findViewById(R.id.et_totalAmount);

        }
    }

    public class ReceiptHeadVH extends RecyclerView.ViewHolder {
        public ReceiptHeadVH(View itemView) {
            super(itemView);
        }
    }
}