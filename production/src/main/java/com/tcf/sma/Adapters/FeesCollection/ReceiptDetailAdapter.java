package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.R;

import java.util.List;

public class ReceiptDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private List<ViewReceivablesModels> models;

    public ReceiptDetailAdapter(Activity context, List<ViewReceivablesModels> model) {
        this.context = context;
        this.models = model;
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
                itemView = LayoutInflater.from(context).inflate(R.layout.receipt_details_items_header, parent, false);
                return new ReceiptDetailAdapter.ReceiptHeadVH(itemView);
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_receipt_detail_item, parent, false);
                return new ReceiptDetailAdapter.RecieptItemVH(itemView);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position > 0 && position < models.size() + 1) {
            ReceiptDetailAdapter.RecieptItemVH itemVH = (ReceiptDetailAdapter.RecieptItemVH) holder;

            if (!models.get(position - 1).getAmountReceived().isEmpty())
                itemVH.etTotalAmount.setText(models.get(position - 1).getAmountReceived());

            if (!models.get(position - 1).getTodaySales().isEmpty())
                itemVH.etSale.setText(models.get(position - 1).getTodaySales());

            if (!models.get(position - 1).getTitle().isEmpty())
                itemVH.tv_title.setText(models.get(position - 1).getTitle());

        } else if (position == models.size() + 1) {
            ReceiptDetailAdapter.RecieptItemVH itemVH = (ReceiptDetailAdapter.RecieptItemVH) holder;
            itemVH.tv_title.setText(Html.fromHtml("<b>Total</b>"));
            itemVH.etSale.setText(getTotalSales());
            itemVH.etTotalAmount.setText(getTotal());
        }
    }

    public String getTotalSales() {
        int totalSales = 0;

        for (ViewReceivablesModels model : models) {
            totalSales += Double.parseDouble(model.getTodaySales());
        }
        return String.valueOf(totalSales);
    }

    public String getTotal() {
        double totalAmount = 0.0;

        for (ViewReceivablesModels model : models) {
            totalAmount += Double.parseDouble(model.getAmountReceived());
        }
        return String.valueOf(Math.round(totalAmount));
    }

    @Override
    public int getItemCount() {
        return models.size() + 2;
    }

    public class RecieptItemVH extends RecyclerView.ViewHolder {

        TextView tv_title;
        EditText etTotalAmount, etSale;
        int balance = 0;

        public RecieptItemVH(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            etTotalAmount = (EditText) itemView.findViewById(R.id.et_received);
            etSale = itemView.findViewById(R.id.et_sale);

        }
    }

    public class ReceiptHeadVH extends RecyclerView.ViewHolder {
        public ReceiptHeadVH(View itemView) {
            super(itemView);
        }
    }
}