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

import com.tcf.sma.Models.Fees_Collection.ViewReceivablesCorrectionModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.R;
import com.tcf.sma.utils.CustomRangeInputFilter;

import java.util.List;

public class CorrectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ViewReceivablesCorrectionModel> models;
    List<ViewReceivablesModels> recvModels;
    CorrectionAdapter.DatasetUpdateListener datasetUpdateListener;
    String correctionType = "C";
    private int transactionType;
    private Activity context;

    public CorrectionAdapter(Activity context, List<ViewReceivablesCorrectionModel> model, CorrectionAdapter.DatasetUpdateListener datasetUpdateListener, String correctionType) {
        this.context = context;
        this.models = model;
        this.correctionType = correctionType;
        this.datasetUpdateListener = datasetUpdateListener;


    }

    public CorrectionAdapter(Activity context, List<ViewReceivablesCorrectionModel> model, List<ViewReceivablesModels> viewReceivablesModels, DatasetUpdateListener datasetUpdateListener, int transactionType) {
        this.context = context;
        this.models = model;
        this.datasetUpdateListener = datasetUpdateListener;
        this.transactionType = transactionType;
        recvModels = viewReceivablesModels;


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
                itemView = LayoutInflater.from(context).inflate(R.layout.correction_header_items, parent, false);
                return new CorrectionVH(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_receipt_item_correction, parent, false);

                //add text change listeners for realtime calculations

                final CorrectionItemVH itemVH = new CorrectionItemVH(itemView);
                itemVH.et_oldRecvd.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        final int feeTypeId = models.get(itemVH.getAdapterPosition() - 1).getFeeTypeId();
                        int iPrevRecv = Integer.valueOf(itemVH.et_oldRecvd.getText().toString()) + Integer.valueOf(recvModels.get(itemVH.getAdapterPosition() - 1).getBalance());

                        if (isFeeType12(feeTypeId))
                            if (iPrevRecv == 0) {
                                itemVH.et_newRecvd.setEnabled(false);
                                itemVH.et_newRecvd.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_gray));
                            } else {
                                itemVH.et_newRecvd.setEnabled(true);
                                itemVH.et_newRecvd.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_black));
                            }

                        datasetUpdateListener.onDataSetChanged(models);
                    }
                });
                itemVH.et_newSale.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        final int feeTypeId = models.get(itemVH.getAdapterPosition() - 1).getFeeTypeId();
                        int iPrevRecv = Integer.valueOf(recvModels.get(itemVH.getAdapterPosition() - 1).getBalance()) + Integer.valueOf(itemVH.et_oldRecvd.getText().toString());


                        int balance = computeBalance(String.valueOf(iPrevRecv), editable.toString(), itemVH.et_newRecvd.getText().toString());

                        //business logic 1
                        if (itemVH.et_newRecvd.getText().toString().isEmpty()) {
                            if (computeBalance(String.valueOf(iPrevRecv),
                                    editable.toString(), "0") == 0 && itemVH.getAdapterPosition() != 3) {
                                if (!isFeeType456(feeTypeId)) {
                                    itemVH.et_newRecvd.setEnabled(false);
                                    itemVH.et_newRecvd.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_gray));
                                } else {
                                    itemVH.et_newRecvd.setEnabled(true);
                                    itemVH.et_newRecvd.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_black));
                                }
                            } else {
                                itemVH.et_newRecvd.setEnabled(true);
                                itemVH.et_newRecvd.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_black));
                            }
                        }


                        //business logic 2
                        if (isFeeType12(feeTypeId))
                            if ((itemVH.getAdapterPosition() > 3 && balance >= 0) || itemVH.getAdapterPosition() <= 3) {
                                models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(false);
                                itemVH.et_newRecvd.setTextColor(Color.BLACK);
                            } else {
                                models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(true);
                                itemVH.et_newRecvd.setTextColor(Color.RED);
                            }
                        if (!isFeeType456(feeTypeId))
                            models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");

                        if (isFeeType456(feeTypeId) && !editable.toString().equals(models.get(itemVH.getAdapterPosition() - 1).getOldSale()))
                            models.get(itemVH.getAdapterPosition() - 1).setNewSales(editable.toString() + "");
                        else if (isFeeType456(feeTypeId) && editable.toString().equals(models.get(itemVH.getAdapterPosition() - 1).getOldSale()))
                            models.get(itemVH.getAdapterPosition() - 1).setNewSales(editable.toString() + "");


                        if (isNumberGreaterThan5000(editable.toString()))
                            models.get(itemVH.getAdapterPosition() - 1).setSalesGreaterThan5000(true);
                        else
                            models.get(itemVH.getAdapterPosition() - 1).setSalesGreaterThan5000(false);

//                        itemVH.et_amountBalance.setText(balance + "");
//                        if (!isFeeType456(feeTypeId))
//                            updateBalance(balance, itemVH.et_amountBalance);

                        datasetUpdateListener.onDataSetChanged(models);
                    }
                });

                itemVH.et_newRecvd.addTextChangedListener(new TextWatcher() {

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
                        int newReceived = 0;
                        if (!editable.toString().equals("")) {
                            newReceived = Integer.parseInt(editable.toString());
                        }
                        int iPrevRecv = Integer.valueOf(recvModels.get(itemVH.getAdapterPosition() - 1).getBalance()) + Integer.valueOf(itemVH.et_oldRecvd.getText().toString());
                        if (!isFeeType456(feeTypeId)) {

                            balance = computeBalance(String.valueOf(iPrevRecv),
                                    itemVH.et_newSale.getText().toString(), editable.toString());
                        }

                        if (isFeeType456(feeTypeId)) {
                            //adding same amount in the sales today field
                            itemVH.et_newSale.setText(editable.toString());
                        }

                        //pos greater than 3 because 0 is header and another 3 are allowed to hold values
                        //that make the balance negative..

                        if (itemVH.et_newRecvd.getText().toString().isEmpty()) {
                            if (computeBalance(String.valueOf(iPrevRecv),
                                    itemVH.et_newSale.getText().toString(), editable.toString()) == 0
                                    && itemVH.getAdapterPosition() != 3) {
                                if (!isFeeType456(feeTypeId)) {
                                    itemVH.et_newRecvd.setEnabled(false);
                                    itemVH.et_newRecvd.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_gray));
                                } else {
                                    itemVH.et_newRecvd.setEnabled(true);
                                    itemVH.et_newRecvd.setBackground(
                                            context.getResources().getDrawable(R.drawable.textview_border_black));
                                }
                            } else {
                                itemVH.et_newRecvd.setEnabled(true);
                                itemVH.et_newRecvd.setBackground(
                                        context.getResources().getDrawable(R.drawable.textview_border_black));
                            }
                        }

                        models.get(itemVH.getAdapterPosition() - 1).setBalance(balance + "");
                        models.get(itemVH.getAdapterPosition() - 1).setNewReceived(editable.toString().equals("") ? "0" : editable.toString());

//                        itemVH.et_amountBalance.setText(balance + "");

//                        updateBalance(balance, itemVH.et_amountBalance);

                        if (isNumberGreaterThan5000(editable.toString()))
                            models.get(itemVH.getAdapterPosition() - 1).setAmountGreaterThan5000(true);
                        else
                            models.get(itemVH.getAdapterPosition() - 1).setAmountGreaterThan5000(false);
//                        (itemVH.getAdapterPosition() >= 1 && balance >= 0) || itemVH.getAdapterPosition() <= 3
                        if (isFeeType12(feeTypeId))
                            if (newReceived <= iPrevRecv) {
                                //received amount is not making balance negative
                                models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(false);
                                itemVH.et_newRecvd.setTextColor(Color.BLACK);
                            } else {
                                //received amount is making balance negative
                                models.get(itemVH.getAdapterPosition() - 1).setBalanceNegative(true);
                                itemVH.et_newRecvd.setTextColor(Color.RED);
                            }
                        datasetUpdateListener.onDataSetChanged(models);
                    }
                });
                return itemVH;
        }
    }

    private boolean isFeeType456(int feeTypeId) {
        return feeTypeId == 4 || feeTypeId == 5 || feeTypeId == 6 || feeTypeId == 7;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (position > 0) {
            int prevRec;
            int salesToday;
            int amountRecv;


            CorrectionAdapter.CorrectionItemVH itemVH = (CorrectionAdapter.CorrectionItemVH) holder;


//            if (models.get(position - 1).isSalesDisabled()) {
//                itemVH.et_todaySales.setBackground(context.getResources().getDrawable(R.drawable.textview_border_gray));
//                itemVH.et_todaySales.setEnabled(false);
//                itemVH.et_todaySales.setInputType(InputType.TYPE_NULL);
//            }


            if (!models.get(position - 1).getOldSale().isEmpty()) {
                amountRecv = Integer.valueOf(models.get(position - 1).getOldSale());
                itemVH.et_oldSale.setText(amountRecv + "");

            }
            if (!models.get(position - 1).getOldRecieved().isEmpty()) {
                amountRecv = Integer.valueOf(models.get(position - 1).getOldRecieved());
                itemVH.et_oldRecvd.setText(amountRecv + "");
                itemVH.et_newRecvd.setText(amountRecv + "");

            }
//
//
//            if (!models.get(position - 1).getPreviouslyReceived().isEmpty()) {
//                prevRec = Integer.valueOf(models.get(position - 1).getPreviouslyReceived());
//                itemVH.et_prerecv.setText(prevRec + "");
//            }
//

            if (!models.get(position - 1).getTitle().isEmpty()) {
                itemVH.tv_title.setText(models.get(position - 1).getTitle());
//                itemVH.tv_title.setText(models.get(position - 1).getTitle() + " (" + recvModels.get(position - 1).getBalance() + ")");
            }

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
//        pR = prevRecv;

        if (!todaySales.isEmpty())
            tS = Integer.valueOf(todaySales);

        if (!amountRecv.isEmpty())
            aR = Integer.valueOf(amountRecv);


        return pR - aR;


//        if (correctionType.equals("S"))
//            return pR + aR - tS;
//        else
//            return pR - aR + tS;

    }

    private boolean isFeeType12(int feeType) {
        return feeType == 1 || feeType == 2;
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

    public interface DatasetUpdateListener {
        void onDataSetChanged(List<ViewReceivablesCorrectionModel> models);
    }

    public class CorrectionItemVH extends RecyclerView.ViewHolder {
        TextView tv_title;
        EditText et_oldSale, et_oldRecvd, et_newSale, et_newRecvd;
        int balance = 0;

        public CorrectionItemVH(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            et_oldSale = (EditText) itemView.findViewById(R.id.et_oldSale);
            et_oldRecvd = (EditText) itemView.findViewById(R.id.et_oldRecvd);
            et_newSale = (EditText) itemView.findViewById(R.id.et_newSale);
            et_newRecvd = (EditText) itemView.findViewById(R.id.et_newRecvd);
            et_newRecvd.setFilters(new InputFilter[]{new CustomRangeInputFilter(0, 5000)});

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

