package com.tcf.sma.Adapters.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseClosingItem;
import com.tcf.sma.R;

import java.util.List;

public class ExpenseClosingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ExpenseClosingItem> closingItemList;

    public ExpenseClosingAdapter(Context context, List<ExpenseClosingItem> closingItemList) {
        this.context = context;
        this.closingItemList = closingItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ExpenseClosingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_closing_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ExpenseClosingViewHolder expenseClosingViewHolder = (ExpenseClosingViewHolder) holder;
        expenseClosingViewHolder.item_name.setText(position+1 + ". " + closingItemList.get(position).getHeadName());
        if(closingItemList.get(position).getFlow_id()==1) {
            expenseClosingViewHolder.amount.setText(String.format("%s/-", "-" + Double.valueOf(closingItemList.get(position).getSpentAmount())));
        } else {
            expenseClosingViewHolder.amount.setText(String.format("%s/-", Double.valueOf(closingItemList.get(position).getSpentAmount())));
        }
    }



    @Override
    public int getItemCount() {
        return closingItemList.size();
    }

    class ExpenseClosingViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, amount;


        ExpenseClosingViewHolder(@NonNull final View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.itemName);
            amount = itemView.findViewById(R.id.itemAmount);

        }

    }

}
