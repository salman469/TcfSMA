package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.R;
import com.tcf.sma.utils.CustomRangeInputFilter;

import java.util.List;

public class WaiverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ViewReceivablesModels> models;
    WaiverAdapter.DatasetUpdateListener datasetUpdateListener;
    String correctionType = "C";
    private int transactionType;
    private Activity context;

    public WaiverAdapter(Activity context, List<ViewReceivablesModels> model, WaiverAdapter.DatasetUpdateListener datasetUpdateListener, String correctionType) {
        this.context = context;
        this.models = model;
        this.correctionType = correctionType;
        this.datasetUpdateListener = datasetUpdateListener;


    }

    public WaiverAdapter(Activity context, List<ViewReceivablesModels> model, WaiverAdapter.DatasetUpdateListener datasetUpdateListener, int transactionType) {
        this.context = context;
        this.models = model;
        this.datasetUpdateListener = datasetUpdateListener;
        this.transactionType = transactionType;


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
                itemView = LayoutInflater.from(context).inflate(R.layout.waiver_header_items, parent, false);
                final CorrectionVH vh = new CorrectionVH(itemView);
                if (transactionType == 3) {
                    vh.waiver_correction.setText("Waiver\nAmount");
                }
                return vh;

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_receipt_item_waiver, parent, false);

                //add text change listeners for realtime calculations

                final CorrectionItemVH itemVH = new CorrectionItemVH(itemView);
                itemVH.et_prerecv.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

//                        int balance = computeBalance(editable.toString(), itemVH.et_todaySales.getText().toString(),
//                                itemVH.et_amountRecv.getText().toString());
                        int balance = computeBalance(editable.toString(), "0",
                                itemVH.et_amountRecv.getText().toString());


                        models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");
                        models.get(itemVH.getAdapterPosition() - 1).setPreviouslyReceived(editable.toString() + "");

                        itemVH.et_amountBalance.setText(balance + "");
                        if (Integer.valueOf(editable.toString()) <= 0) {
                            itemVH.et_amountRecv.setBackground(context.getResources().getDrawable(R.drawable.textview_border_gray));
                            itemVH.et_amountRecv.setEnabled(false);
                        }

                        datasetUpdateListener.onDataSetChanged(models);

                    }


                });
//                itemVH.et_todaySales.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                        int balance = computeBalance(itemVH.et_prerecv.getText().toString(), editable.toString(),
//                                itemVH.et_amountRecv.getText().toString());
//
//
//                        models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");
//                        models.get(itemVH.getAdapterPosition() - 1).setTodaySales(editable.toString() + "");
//
//                        itemVH.et_amountBalance.setText(balance + "");
//
//
//                        datasetUpdateListener.onDataSetChanged(models);
//                    }
//                });

                itemVH.et_amountRecv.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

//                        int balance = computeBalance(itemVH.et_prerecv.getText().toString(),
//                                itemVH.et_todaySales.getText().toString(), editable.toString());
                        int balance = computeBalance(itemVH.et_prerecv.getText().toString(),
                                "0", editable.toString());


                        //pos greater than 3 because 0 is header and another 3 are allowed to hold values
                        //that make the balance negative..

                        models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");
                        models.get(itemVH.getAdapterPosition() - 1).setAmountReceived(editable.toString() + "");

                        if (balance >= 0) {
                            //received amount is not making balance negative
                            models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(false);
                            itemVH.et_amountRecv.setTextColor(Color.BLACK);
                        } else {
                            //received amount is making balance negative
                            models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(true);
                            itemVH.et_amountRecv.setTextColor(Color.RED);
                        }

                        itemVH.et_amountBalance.setText(balance + "");
                        datasetUpdateListener.onDataSetChanged(models);
                    }
                });
                return itemVH;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (position > 0) {
            int prevRec;
            int salesToday;
            int amountRecv;


            WaiverAdapter.CorrectionItemVH itemVH = (WaiverAdapter.CorrectionItemVH) holder;


//            if (models.get(position - 1).isSalesDisabled()) {
//                itemVH.et_todaySales.setBackground(context.getResources().getDrawable(R.drawable.textview_border_gray));
//                itemVH.et_todaySales.setEnabled(false);
//                itemVH.et_todaySales.setInputType(InputType.TYPE_NULL);
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

        if (transactionType == 2)
            return pR + aR - tS;
        else if (transactionType == 3)
            return pR - aR + tS;
        else
            return 0;

//        if (correctionType.equals("S"))
//            return pR + aR - tS;
//        else
//            return pR - aR + tS;

    }

    private boolean isFeeType456(int feeType) {
        return feeType == 4 || feeType == 5 || feeType == 6;
    }

    public interface DatasetUpdateListener {
        void onDataSetChanged(List<ViewReceivablesModels> models);
    }

    public class CorrectionItemVH extends RecyclerView.ViewHolder {
        //        EditText et_todaySales;
        TextView tv_title;
        EditText et_prerecv, et_amountRecv, et_amountBalance;
        int balance = 0;

        public CorrectionItemVH(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            et_amountBalance = (EditText) itemView.findViewById(R.id.et_amountBalance);
            et_prerecv = (EditText) itemView.findViewById(R.id.et_prevRecv);
//            et_todaySales = (EditText) itemView.findViewById(R.id.et_todaySales);
            et_amountRecv = (EditText) itemView.findViewById(R.id.et_amountRecv);
            et_amountRecv.setFilters(new InputFilter[]{new CustomRangeInputFilter(0, 13000)});

        }
    }

    public class CorrectionVH extends RecyclerView.ViewHolder {

        TextView waiver_correction;

        public CorrectionVH(View itemView) {
            super(itemView);
            waiver_correction = itemView.findViewById(R.id.tv_waiverorcorrection);
        }
    }
}

