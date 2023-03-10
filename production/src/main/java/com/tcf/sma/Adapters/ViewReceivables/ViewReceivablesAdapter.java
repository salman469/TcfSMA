package com.tcf.sma.Adapters.ViewReceivables;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.ViewReceivables.ViewReceivablesModels;
import com.tcf.sma.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ViewReceivablesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int digitsRight = 100; // Represents division to be
    List<ViewReceivablesModels> models;
    ViewReceivablesAdapter.DatasetUpdateListener datasetUpdateListener;
    private Activity context;

    public ViewReceivablesAdapter(Activity context, List<ViewReceivablesModels> model, DatasetUpdateListener datasetUpdateListener) {
        this.context = context;
        this.models = model;
        this.datasetUpdateListener = datasetUpdateListener;
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

                itemView = LayoutInflater.from(context).inflate(R.layout.rowview_viewreceivables_header, parent, false);
                return new ViewReceivablesHeadVH(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.rowview_view_receivables_item, parent, false);

                final ViewReceivabelsItemVH itemVH = new ViewReceivabelsItemVH(itemView);

                return itemVH;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewReceivablesModels obj;
        ViewReceivabelsItemVH itemVH = null;
        double balance, collected, due;

        if (position > 0 && (position <= models.size())) { // exclude header so -1 inside get(position -1)
            obj = models.get(position - 1);
            itemVH = (ViewReceivabelsItemVH) holder;

            due = obj.getmDue();
            collected = obj.getmCollected();
            balance = due - collected;

            // indivisual row computation
            if (balance == 0) {
                itemVH.tvBalance.setTextColor(context.getResources().getColor(R.color.app_green));
            } else {
                obj.setmBalance(balance);
            }

            itemVH.tvTotalDue.setText(
                    String.valueOf((int) due));

            itemVH.tvCollected.setText(
                    String.valueOf((int) collected));

            itemVH.tvBalance.setText(
                    String.valueOf((int) balance));

            switch (position) {
                case 1:
                    itemVH.tvPurpose.setText("This Month");
                    break;
                case 2:
                    itemVH.tvPurpose.setText("Previous Month");
                    break;
                case 3:
                    itemVH.tvPurpose.setText("This Academic Year");
                    break;
                case 4:
                    itemVH.tvPurpose.setText("Previous Academic Year");
                    break;

            }
        } else if (position == models.size() + 1 && models.size() > 0) { // for total row
            itemVH = (ViewReceivabelsItemVH) holder;

            itemVH.tvPurpose.setText("Total");
            itemVH.tvPurpose.setTypeface(itemVH.tvPurpose.getTypeface(), Typeface.BOLD);

            try {
                computeSum(itemVH);
            } catch (NumberFormatException e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void computeSum(ViewReceivabelsItemVH itemVH) throws NumberFormatException {
        double due = 0, collected = 0, balance = 0;

        for (ViewReceivablesModels list : models) {
            due += list.getmDue();
            balance += list.getmBalance();
            collected += list.getmCollected();
        }

        try {
           /* itemVH.tvTotalDue.setText(
                    currencyFormat(String.valueOf(due)));*/

            itemVH.tvTotalDue.setText((int) due + "");
            itemVH.tvBalance.setText((int) balance + "");
            itemVH.tvCollected.setText((int) collected + "");

            if (balance == 0.0) {
                itemVH.tvBalance.setTextColor(context.getResources().getColor(R.color.app_green));
            }

            itemVH.tvTotalDue.setTypeface(itemVH.tvTotalDue.getTypeface(), Typeface.BOLD);
            itemVH.tvBalance.setTypeface(itemVH.tvBalance.getTypeface(), Typeface.BOLD);
            itemVH.tvCollected.setTypeface(itemVH.tvCollected.getTypeface(), Typeface.BOLD);
        } catch (Exception e) {
            throw new NumberFormatException();
        }
    }

    @Override
    public int getItemCount() {
        if (models.size() > 0)
            return models.size() + 2; // to create additional header row and last row for total values...
        else
            return models.size() + 1;// empty list so normal scenario only header shows
    }

    private String currencyFormat(String toFormat) {
        if (toFormat == null || toFormat.isEmpty())
            toFormat = "0";
        String clean = toFormat.toString().replaceAll("[,.]", "");
        try {
            double parsed = Double.parseDouble(clean);
            return NumberFormat.getCurrencyInstance(Locale.US)
                    .format((parsed / digitsRight)).replaceAll("[\\s]", "");
        } catch (NumberFormatException nfe) {
            Log.e(ViewReceivablesAdapter.class.getSimpleName(), nfe.toString());
            return "0.00";
        }
    }

    public interface DatasetUpdateListener {
        void onDataSetChanged(List<ViewReceivablesModels> models);
    }

    public class ViewReceivabelsItemVH extends RecyclerView.ViewHolder {

        TextView tvPurpose, tvTotalDue, tvCollected, tvBalance;

        public ViewReceivabelsItemVH(View itemView) {
            super(itemView);
            tvPurpose = (TextView) itemView.findViewById(R.id.tvPurpose);
            tvTotalDue = (TextView) itemView.findViewById(R.id.tvTotalDue);
            tvCollected = (TextView) itemView.findViewById(R.id.tvCollected);
            tvBalance = (TextView) itemView.findViewById(R.id.tvBalance);

            tvTotalDue.setSelected(true);
            tvBalance.setSelected(true);
            tvCollected.setSelected(true);
        }
    }

    public class ViewReceivablesHeadVH extends RecyclerView.ViewHolder {

        public ViewReceivablesHeadVH(View itemView) {
            super(itemView);
        }
    }

}
