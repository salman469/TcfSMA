package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.R;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ViewReceivablesModels> models;
    DatasetUpdateListener datasetUpdateListener;
    private Activity context;
//    private boolean isListenerCalled1, isListenerCalled2, isListenerCalled3 = false;

    public ReceiptAdapter(Activity context, List<ViewReceivablesModels> model, DatasetUpdateListener datasetUpdateListener) {
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

    private boolean isNumberGreaterThan3000(String number) {

        if (number.equals(""))
            return false;

        int val = Integer.valueOf(number);
        if (val > 3000) {
            return true;
        }

        return false;
    }

    private boolean isNumberGreaterThan5000(String number) {

        if (number.equals(""))
            return false;

        int val = Integer.valueOf(number);
        if (val > 5000) {
            return true;
        }

        return false;
    }

    private synchronized void updateBalance(int balance, EditText editText) {
        editText.setText(balance + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.receipt_items_header, parent, false);
                return new ReceiptHeadVH(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_receipt_item, parent, false);


                //add text change listeners for realtime calculations

                final RecieptItemVH itemVH = new RecieptItemVH(itemView);

                itemVH.et_prerecv.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        final int feeTypeId = models.get(itemVH.getAdapterPosition() - 1).getFeeTypeId();

                        int balance = computeBalance(editable.toString(), itemVH.et_todaySales.getText().toString(),
                                itemVH.et_amountRecv.getText().toString());

                        if (itemVH.et_amountRecv.getText().toString().isEmpty()) {
                            if (computeBalance(editable.toString(),
                                    itemVH.et_todaySales.getText().toString(), "0") == 0 && itemVH.getAdapterPosition() != 3) {
                                if (!isFeeType456(feeTypeId)) {
                                    itemVH.et_amountRecv.setEnabled(false);
                                    itemVH.et_amountRecv.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_gray));
                                } else {
                                    itemVH.et_amountRecv.setEnabled(true);
                                    itemVH.et_amountRecv.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_black));
                                }
                            } else {
                                itemVH.et_amountRecv.setEnabled(true);
                                itemVH.et_amountRecv.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_black));
                            }
                        }

                        models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");
                        models.get(itemVH.getAdapterPosition() - 1).setPreviouslyReceived(editable.toString() + "");

//                        itemVH.et_amountBalance.setText(balance + "");
                        updateBalance(balance, itemVH.et_amountBalance);

                        datasetUpdateListener.onDataSetChanged(models);

                    }


                });
                itemVH.et_todaySales.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        final int feeTypeId = models.get(itemVH.getAdapterPosition() - 1).getFeeTypeId();

                        int balance = computeBalance(itemVH.et_prerecv.getText().toString(), editable.toString(),
                                itemVH.et_amountRecv.getText().toString());

                        //business logic 1
                        if (itemVH.et_amountRecv.getText().toString().isEmpty()) {
                            if (computeBalance(itemVH.et_prerecv.getText().toString(),
                                    editable.toString(), "0") == 0 && itemVH.getAdapterPosition() != 3) {
                                if (!isFeeType456(feeTypeId)) {
                                    itemVH.et_amountRecv.setEnabled(false);
                                    itemVH.et_amountRecv.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_gray));
                                } else {
                                    itemVH.et_amountRecv.setEnabled(true);
                                    itemVH.et_amountRecv.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_black));
                                }
                            } else {
                                itemVH.et_amountRecv.setEnabled(true);
                                itemVH.et_amountRecv.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_black));
                            }
                        }


                        //business logic 2
                        if ((itemVH.getAdapterPosition() > 3 && balance >= 0) || itemVH.getAdapterPosition() <= 3) {
                            models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(false);
                            itemVH.et_amountRecv.setTextColor(Color.BLACK);
                        } else {
                            models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(true);
                            itemVH.et_amountRecv.setTextColor(Color.RED);
                        }
                        if (!isFeeType456(feeTypeId))
                            models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");

                        models.get(itemVH.getAdapterPosition() - 1).setTodaySales(editable.toString() + "");

                        if (isNumberGreaterThan5000(editable.toString()))
                            models.get(itemVH.getAdapterPosition() - 1).setSalesGreaterThan5000(true);
                        else
                            models.get(itemVH.getAdapterPosition() - 1).setSalesGreaterThan5000(false);

//                        itemVH.et_amountBalance.setText(balance + "");
                        if (!isFeeType456(feeTypeId))
                            updateBalance(balance, itemVH.et_amountBalance);

                        datasetUpdateListener.onDataSetChanged(models);
                    }
                });
                itemVH.et_amountRecv.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        final int feeTypeId = models.get(itemVH.getAdapterPosition() - 1).getFeeTypeId();
                        int balance = 0;

                        if (!isFeeType456(feeTypeId)) {
                            balance = computeBalance(itemVH.et_prerecv.getText().toString(),
                                    itemVH.et_todaySales.getText().toString(), editable.toString());
                        }

                        if (isFeeType456(feeTypeId)) {
                            //adding same amount in the sales today field
                            itemVH.et_todaySales.setText(editable.toString());
                        }

                        //pos greater than 3 because 0 is header and another 3 are allowed to hold values
                        //that make the balance negative..

                        if (itemVH.et_amountRecv.getText().toString().isEmpty()) {
                            if (computeBalance(itemVH.et_prerecv.getText().toString(),
                                    itemVH.et_todaySales.getText().toString(), editable.toString()) == 0
                                    && itemVH.getAdapterPosition() != 3) {
                                if (!isFeeType456(feeTypeId)) {
                                    itemVH.et_amountRecv.setEnabled(false);
                                    itemVH.et_amountRecv.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_gray));
                                } else {
                                    itemVH.et_amountRecv.setEnabled(true);
                                    itemVH.et_amountRecv.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_black));
                                }
                            } else {
                                itemVH.et_amountRecv.setEnabled(true);
                                itemVH.et_amountRecv.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_black));
                            }
                        }

                        models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");
                        models.get(itemVH.getAdapterPosition() - 1).setAmountReceived(editable.toString() + "");

//                        itemVH.et_amountBalance.setText(balance + "");

                        updateBalance(balance, itemVH.et_amountBalance);

                        if (isNumberGreaterThan5000(editable.toString()))
                            models.get(itemVH.getAdapterPosition() - 1).setAmountGreaterThan5000(true);
                        else
                            models.get(itemVH.getAdapterPosition() - 1).setAmountGreaterThan5000(false);
//                        (itemVH.getAdapterPosition() >= 1 && balance >= 0) || itemVH.getAdapterPosition() <= 3
                        if (itemVH.getAdapterPosition() >= 1 && itemVH.getAdapterPosition() <= 2)
                            if (balance >= 0) {
                                //received amount is not making balance negative
                                models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(false);
                                itemVH.et_amountRecv.setTextColor(Color.BLACK);
                            } else {
                                //received amount is making balance negative
                                models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(true);
                                itemVH.et_amountRecv.setTextColor(Color.RED);
                            }
                        datasetUpdateListener.onDataSetChanged(models);
                    }
                });
                return itemVH;
        }
    }

    private void checkPreviousDuesAndSales() {

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position > 0) {
            int prevRec;
            int salesToday;
            int amountRecv;

            RecieptItemVH itemVH = (RecieptItemVH) holder;


//            if (models.get(position - 1).isSalesDisabled()) {
            itemVH.et_todaySales.setBackground(context.getResources().getDrawable(R.drawable.textview_border_gray));
            itemVH.et_todaySales.setEnabled(false);
            itemVH.et_todaySales.setInputType(InputType.TYPE_NULL);
//            }


            if (!models.get(position - 1).getAmountReceived().isEmpty()) {
                amountRecv = Integer.valueOf(models.get(position - 1).getAmountReceived());
                itemVH.et_amountRecv.setText(amountRecv + "");
            }

            if (!models.get(position - 1).getPreviouslyReceived().isEmpty()) {
                prevRec = Integer.valueOf(models.get(position - 1).getPreviouslyReceived());
                itemVH.et_prerecv.setText(prevRec + "");
            }

            if (!models.get(position - 1).getBalance().isEmpty())
                itemVH.et_amountBalance.setText(models.get(position - 1).getBalance());

            if (!models.get(position - 1).getTitle().isEmpty())
                itemVH.tv_title.setText(models.get(position - 1).getTitle());

        }

    }

    @Override
    public int getItemCount() {
        return models.size() + 1;
    }

    private int computeBalance(String prevRecv, String todaySales, String amountRecv) {

        int pR = 0;
        int tS = 0;
        int aR = 0;

        if (!prevRecv.isEmpty())
            pR = Integer.valueOf(prevRecv);

        if (!todaySales.isEmpty())
            tS = Integer.valueOf(todaySales);

        if (!amountRecv.isEmpty())
            aR = Integer.valueOf(amountRecv);

        return pR + tS - aR;
    }

    private boolean isFeeType456(int feeType) {
        return feeType == 4 || feeType == 5 || feeType == 6 || feeType == 7;
    }

    public interface DatasetUpdateListener {
        void onDataSetChanged(List<ViewReceivablesModels> models);
    }

    public class RecieptItemVH extends RecyclerView.ViewHolder {

        TextView tv_title;
        EditText et_prerecv, et_todaySales, et_amountRecv, et_amountBalance;
        int balance = 0;

        public RecieptItemVH(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            et_amountBalance = (EditText) itemView.findViewById(R.id.et_amountBalance);
            et_prerecv = (EditText) itemView.findViewById(R.id.et_prevRecv);
            et_todaySales = (EditText) itemView.findViewById(R.id.et_todaySales);
            et_amountRecv = (EditText) itemView.findViewById(R.id.et_amountRecv);

        }
    }

    public class ReceiptHeadVH extends RecyclerView.ViewHolder {


        public ReceiptHeadVH(View itemView) {
            super(itemView);
        }
    }
}
