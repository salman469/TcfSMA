package com.tcf.sma.Adapters.FeesCollection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.DashboardViewReceivablesModels;
import com.tcf.sma.R;

import java.util.List;


public class ReceivableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    //    private String schoolid;
//    private List<FeeTypeModel> feeTypeModelList;
    private List<DashboardViewReceivablesModels> viewReceivablesModelsList;
    private String filterReceivableBy;

    public ReceivableAdapter(Context context, List<DashboardViewReceivablesModels> viewReceivablesModelsList, String filterReceivableBy) {
        this.context = context;
//        this.schoolid = schoolid;
//        this.feeTypeModelList = feeTypeModelList;
        this.viewReceivablesModelsList = viewReceivablesModelsList;
        this.filterReceivableBy = filterReceivableBy;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ReceivableHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_receivables_header, parent, false));

            case 1:
                return new ReceivableDataViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_receivables_data, parent, false));


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position > 0 && position < viewReceivablesModelsList.size() + 1) {
            int new_position = position - 1;
            ReceivableDataViewHolder receivablesDataViewHolder = (ReceivableDataViewHolder) holder;
            receivablesDataViewHolder.tv_header.setText(viewReceivablesModelsList.get(new_position).getTitle());

            double receivables = viewReceivablesModelsList.get(new_position).getAmountReceivable() != null
                    && !viewReceivablesModelsList.get(new_position).getAmountReceivable().isEmpty() ?
                    Double.parseDouble(viewReceivablesModelsList.get(new_position).getAmountReceivable()) : 0;

            double received = viewReceivablesModelsList.get(new_position).getAmountReceived() != null &&
                    !viewReceivablesModelsList.get(new_position).getAmountReceived().isEmpty() ?
                    Double.parseDouble(viewReceivablesModelsList.get(new_position).getAmountReceived()) : 0;

            double opening = viewReceivablesModelsList.get(new_position).getAmountOpening() != null &&
                    !viewReceivablesModelsList.get(new_position).getAmountOpening().isEmpty() ?
                    Double.parseDouble(viewReceivablesModelsList.get(new_position).getAmountOpening()) : 0;

            double closingActive = viewReceivablesModelsList.get(new_position).getAmountClosingActive() != null &&
                    !viewReceivablesModelsList.get(new_position).getAmountClosingActive().isEmpty() ?
                    Double.parseDouble(viewReceivablesModelsList.get(new_position).getAmountClosingActive()) : 0;

            double closingInActive = viewReceivablesModelsList.get(new_position).getAmountClosingInActive() != null &&
                    !viewReceivablesModelsList.get(new_position).getAmountClosingInActive().isEmpty() ?
                    Double.parseDouble(viewReceivablesModelsList.get(new_position).getAmountClosingInActive()) : 0;

            receivablesDataViewHolder.tv_receivables.setText(AppModel.getInstance().formatNumberInCommas(Math.round(receivables)));
            receivablesDataViewHolder.tv_received.setText(AppModel.getInstance().formatNumberInCommas(Math.round(received)));
            receivablesDataViewHolder.tv_opening.setText(AppModel.getInstance().formatNumberInCommas(Math.round(opening)));
            receivablesDataViewHolder.tv_ClosingActive.setText(AppModel.getInstance().formatNumberInCommas(Math.round(closingActive)));
            receivablesDataViewHolder.tv_ClosingInActive.setText(AppModel.getInstance().formatNumberInCommas(Math.round(closingInActive)));

            long balance = Math.round(opening) + Math.round(receivables) - Math.round(received);
            receivablesDataViewHolder.tv_balance.setText(AppModel.getInstance().formatNumberInCommas(balance));
            viewReceivablesModelsList.get(new_position).setBalance((int) balance);


        } else if (position == viewReceivablesModelsList.size() + 1) {
            ReceivableDataViewHolder receivablesDataViewHolder = (ReceivableDataViewHolder) holder;
            receivablesDataViewHolder.tv_header.setText("Total");
            receivablesDataViewHolder.tv_receivables.setText(AppModel.getInstance().formatNumberInCommas(getTotalReceivable()));
            receivablesDataViewHolder.tv_received.setText(AppModel.getInstance().formatNumberInCommas(getTotalReceived()));
            receivablesDataViewHolder.tv_opening.setText(AppModel.getInstance().formatNumberInCommas(getTotalOpening()));
            receivablesDataViewHolder.tv_balance.setText(AppModel.getInstance().formatNumberInCommas(getTotalBalance()));
            receivablesDataViewHolder.tv_ClosingActive.setText(AppModel.getInstance().formatNumberInCommas(getTotalClosingActive()));
            receivablesDataViewHolder.tv_ClosingInActive.setText(AppModel.getInstance().formatNumberInCommas(getTotalClosingInActive()));

            receivablesDataViewHolder.tv_header.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));
            receivablesDataViewHolder.tv_receivables.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));
            receivablesDataViewHolder.tv_received.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));
            receivablesDataViewHolder.tv_opening.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));
            receivablesDataViewHolder.tv_balance.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));
            receivablesDataViewHolder.tv_ClosingActive.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));
            receivablesDataViewHolder.tv_ClosingInActive.setTextColor(ContextCompat.getColor(context, R.color.grayheadingsdb));

            receivablesDataViewHolder.tv_header.setTypeface(receivablesDataViewHolder.tv_header.getTypeface(), Typeface.BOLD);
            receivablesDataViewHolder.tv_receivables.setTypeface(receivablesDataViewHolder.tv_receivables.getTypeface(), Typeface.BOLD);
            receivablesDataViewHolder.tv_received.setTypeface(receivablesDataViewHolder.tv_received.getTypeface(), Typeface.BOLD);
            receivablesDataViewHolder.tv_opening.setTypeface(receivablesDataViewHolder.tv_opening.getTypeface(), Typeface.BOLD);
            receivablesDataViewHolder.tv_balance.setTypeface(receivablesDataViewHolder.tv_balance.getTypeface(), Typeface.BOLD);
            receivablesDataViewHolder.tv_ClosingActive.setTypeface(receivablesDataViewHolder.tv_ClosingActive.getTypeface(), Typeface.BOLD);
            receivablesDataViewHolder.tv_ClosingInActive.setTypeface(receivablesDataViewHolder.tv_ClosingInActive.getTypeface(), Typeface.BOLD);
        }
    }

    private int getTotalBalance() {
        int amount = 0;
        for (DashboardViewReceivablesModels models : viewReceivablesModelsList) {
            amount += models.getBalance();
        }
        return Math.round(amount);
    }

    private int getTotalReceived() {
        double amount = 0.0;
        for (DashboardViewReceivablesModels models : viewReceivablesModelsList) {
            if (models.getAmountReceived() != null && !models.getAmountReceived().isEmpty()) {
                amount += Double.parseDouble(models.getAmountReceived());
            }
        }
        return (int) Math.round(amount);
    }

    private int getTotalReceivable() {
        double amount = 0.0;
        for (DashboardViewReceivablesModels models : viewReceivablesModelsList) {
            if (models.getAmountReceivable() != null && !models.getAmountReceivable().isEmpty()) {
                amount += Double.parseDouble(models.getAmountReceivable());
            }
        }
        return (int) Math.round(amount);
    }

    private int getTotalOpening() {
        double amount = 0.0;
        for (DashboardViewReceivablesModels models : viewReceivablesModelsList) {
            if (models.getAmountOpening() != null && !models.getAmountOpening().isEmpty()) {
                amount += Double.parseDouble(models.getAmountOpening());
            }
        }
        return (int) Math.round(amount);
    }

    private int getTotalClosingActive() {
        double amount = 0.0;
        for (DashboardViewReceivablesModels models : viewReceivablesModelsList) {
            if (models.getAmountClosingActive() != null && !models.getAmountClosingActive().isEmpty()) {
                amount += Double.parseDouble(models.getAmountClosingActive());
            }
        }
        return (int) Math.round(amount);
    }

    private int getTotalClosingInActive() {
        double amount = 0.0;
        for (DashboardViewReceivablesModels models : viewReceivablesModelsList) {
            if (models.getAmountClosingInActive() != null && !models.getAmountClosingInActive().isEmpty()) {
                amount += Double.parseDouble(models.getAmountClosingInActive());
            }
        }
        return (int) Math.round(amount);
    }

    @Override
    public int getItemCount() {
        return viewReceivablesModelsList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private class ReceivableHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tv_labelOpening, tv_labelBalance , tv_labelHeader , tv_labelReceivable , tv_labelReceived, tv_labelClosingActive, tv_labelClosingInActive;

        ReceivableHeaderViewHolder(View inflate) {
            super(inflate);
            tv_labelOpening = itemView.findViewById(R.id.tv_labelOpening);
            tv_labelBalance = itemView.findViewById(R.id.tv_labelBalance);
            tv_labelHeader = itemView.findViewById(R.id.tv_labelHeader);
            tv_labelReceivable = itemView.findViewById(R.id.tv_labelReceivable);
            tv_labelReceived = itemView.findViewById(R.id.tv_labelReceived);
            tv_labelClosingActive = itemView.findViewById(R.id.tv_labelClosingActive);
            tv_labelClosingInActive = itemView.findViewById(R.id.tv_labelClosingInActive);

            if (filterReceivableBy.equals("s")){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                tv_labelReceivable.setVisibility(View.VISIBLE);
                tv_labelReceived.setVisibility(View.VISIBLE);
                tv_labelOpening.setVisibility(View.VISIBLE);
                tv_labelBalance.setVisibility(View.VISIBLE);

                tv_labelClosingActive.setVisibility(View.GONE);
                tv_labelClosingInActive.setVisibility(View.GONE);

                tv_labelHeader.setLayoutParams(params);
                tv_labelReceivable.setLayoutParams(params);
                tv_labelReceived.setLayoutParams(params);
                tv_labelOpening.setLayoutParams(params);
                tv_labelBalance.setLayoutParams(params);

            } else if (filterReceivableBy.equals("c")){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.65f);
                tv_labelClosingActive.setVisibility(View.VISIBLE);
                tv_labelClosingInActive.setVisibility(View.VISIBLE);

                tv_labelReceived.setVisibility(View.GONE);
                tv_labelReceivable.setVisibility(View.GONE);
                tv_labelOpening.setVisibility(View.GONE);
                tv_labelBalance.setVisibility(View.GONE);

                tv_labelHeader.setLayoutParams(params);
                tv_labelClosingActive.setLayoutParams(params);
                tv_labelClosingInActive.setLayoutParams(params);
            }
            else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.65f);
                tv_labelReceivable.setVisibility(View.VISIBLE);
                tv_labelReceived.setVisibility(View.VISIBLE);

                tv_labelOpening.setVisibility(View.GONE);
                tv_labelBalance.setVisibility(View.GONE);
                tv_labelClosingActive.setVisibility(View.GONE);
                tv_labelClosingInActive.setVisibility(View.GONE);

                tv_labelHeader.setLayoutParams(params);
                tv_labelReceivable.setLayoutParams(params);
                tv_labelReceived.setLayoutParams(params);
            }
        }
    }

    private class ReceivableDataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_header, tv_receivables, tv_received, tv_balance, tv_opening, tv_ClosingActive, tv_ClosingInActive;
        CardView card_view;

        ReceivableDataViewHolder(View view) {
            super(view);
            card_view = itemView.findViewById(R.id.card_view);
            tv_header = itemView.findViewById(R.id.tv_header);
            tv_receivables = itemView.findViewById(R.id.tv_receivables);
            tv_received = itemView.findViewById(R.id.tv_received);
            tv_balance = itemView.findViewById(R.id.tv_balance);
            tv_opening = itemView.findViewById(R.id.tv_opening);
            tv_ClosingActive = itemView.findViewById(R.id.tv_ClosingActive);
            tv_ClosingInActive = itemView.findViewById(R.id.tv_ClosingInActive);

            if (filterReceivableBy.equals("s")){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                tv_receivables.setVisibility(View.VISIBLE);
                tv_received.setVisibility(View.VISIBLE);
                tv_balance.setVisibility(View.VISIBLE);
                tv_opening.setVisibility(View.VISIBLE);

                tv_ClosingActive.setVisibility(View.GONE);
                tv_ClosingInActive.setVisibility(View.GONE);

                tv_header.setLayoutParams(params);
                tv_receivables.setLayoutParams(params);
                tv_received.setLayoutParams(params);
                tv_balance.setLayoutParams(params);
                tv_opening.setLayoutParams(params);
            }
            else if (filterReceivableBy.equals("c")){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.65f);
                tv_ClosingActive.setVisibility(View.VISIBLE);
                tv_ClosingInActive.setVisibility(View.VISIBLE);

                tv_receivables.setVisibility(View.GONE);
                tv_received.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_opening.setVisibility(View.GONE);

                tv_header.setLayoutParams(params);
                tv_ClosingActive.setLayoutParams(params);
                tv_ClosingInActive.setLayoutParams(params);
            }
            else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.65f);
                tv_receivables.setVisibility(View.VISIBLE);
                tv_received.setVisibility(View.VISIBLE);

                tv_balance.setVisibility(View.GONE);
                tv_opening.setVisibility(View.GONE);
                tv_ClosingActive.setVisibility(View.GONE);
                tv_ClosingInActive.setVisibility(View.GONE);

                tv_header.setLayoutParams(params);
                tv_receivables.setLayoutParams(params);
                tv_received.setLayoutParams(params);
            }
        }
    }
}
