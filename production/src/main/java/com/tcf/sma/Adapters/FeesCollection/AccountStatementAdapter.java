package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModelNewStructure;
import com.tcf.sma.Models.Fees_Collection.AccountStatementUnionModel;
import com.tcf.sma.R;

import java.util.List;

public class AccountStatementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AccountStatementModel> asmList;
    private Activity context;
    private String grNo;
    private String reciptNo;
    private String depositSlipNo;
    private List<AccountStatementUnionModel> unionModels;
    private List<AccountStatementModelNewStructure> modelNewStructureList;


    public AccountStatementAdapter(Activity context, List<AccountStatementModel> asmList) {
        this.context = context;
        this.asmList = asmList;
    }

    public AccountStatementAdapter(Activity context, List<AccountStatementModel> asmList, String grNo, String reciptNo, String depositSlipNo) {
        this.context = context;
        this.asmList = asmList;
        this.grNo = grNo;
        this.reciptNo = reciptNo;
        this.depositSlipNo = depositSlipNo;
    }

    public AccountStatementAdapter(Activity context, List<AccountStatementModel> asmList, String grNo, String reciptNo, List<AccountStatementUnionModel> unionModels, String depositSlipNo) {
        this.context = context;
        this.asmList = asmList;
        this.grNo = grNo;
        this.reciptNo = reciptNo;
        this.unionModels = unionModels;
        this.depositSlipNo = depositSlipNo;
    }

    public AccountStatementAdapter(Activity context, List<AccountStatementModel> asmList, String grNo, String reciptNo, String depositSlipNo, List<AccountStatementModelNewStructure> modelNewStructureList) {
        this.context = context;
        this.asmList = asmList;
        this.grNo = grNo;
        this.reciptNo = reciptNo;
        this.depositSlipNo = depositSlipNo;
        this.modelNewStructureList = modelNewStructureList;
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
                itemView = LayoutInflater.from(context).inflate(R.layout.account_statement_header, parent, false);
                return new AccountStatementViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_account_statement_item, parent, false);
                final AccountStatementItemViewHolder itemVH = new AccountStatementItemViewHolder(itemView);
                return itemVH;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position > 0) {
            AccountStatementItemViewHolder itemViewHolder = (AccountStatementItemViewHolder) holder;
            AccountStatementModel model = asmList.get(position);
            if (model.getTransactionCategoryId() > 0) {
                if (model.getTransactionCategoryId() == 1) {
                    itemViewHolder.etJVNO.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.etReceiptNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.etDepositSlipNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.etDate.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.etDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.etAmount.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.etBalance.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                    itemViewHolder.et_grNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
                } else if (model.getTransactionCategoryId() == 2) {
                    itemViewHolder.etJVNO.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.etReceiptNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.etDepositSlipNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.etDate.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.etDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.etAmount.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.etBalance.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                    itemViewHolder.et_grNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
                }
            }

            itemViewHolder.etJVNO.setText(model.getJvNo());
            itemViewHolder.et_grNo.setText(model.getGrno() + "");

//            itemViewHolder.etDate.setText(model.getDate());
            itemViewHolder.etDate.setText(AppModel.getInstance().convertDatetoFormat(model.getDate(), "dd-MM-yy", "dd-MMM-yy"));
            itemViewHolder.etDescription.setText(model.getDescription());
//            itemViewHolder.etDepositSlipNo.setText(model.getDepositDate());
            itemViewHolder.etDepositSlipNo.setText(AppModel.getInstance().convertDatetoFormat(model.getDepositDate(), "dd-MM-yy", "dd-MMM-yy"));
            itemViewHolder.etReceiptNo.setText(model.getReceiptNo());


            itemViewHolder.etAmount.setText((Double.valueOf(model.getAmount()).intValue() + ""));
            itemViewHolder.etBalance.setText(Double.valueOf(model.getBalance()).intValue() + "");

            if (!grNo.equals("") && model.getGrno().equalsIgnoreCase(grNo)) {
                itemViewHolder.et_grNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_yellow));
            }

            if (!depositSlipNo.trim().isEmpty() && model.getReceiptNo().equalsIgnoreCase(depositSlipNo)) {
                itemViewHolder.etReceiptNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_yellow));
            }

            if (!reciptNo.trim().isEmpty()) {
                for (int j = 0; j < modelNewStructureList.size(); j++) {
                    if (asmList.get(position).getId() == modelNewStructureList.get(j).getId()) {
                        if (reciptNo.equalsIgnoreCase(modelNewStructureList.get(j).getReceiptNo())) {
                            itemViewHolder.etDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_yellow));
                        }
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return asmList.size();
    }

    public class AccountStatementItemViewHolder extends RecyclerView.ViewHolder {


        EditText etJVNO, etReceiptNo, etDepositSlipNo, etDate, etDescription, etAmount, etBalance, et_grNo;

        public AccountStatementItemViewHolder(View itemView) {
            super(itemView);
            etJVNO = (EditText) itemView.findViewById(R.id.etJVNO);
            etReceiptNo = (EditText) itemView.findViewById(R.id.etReceiptNo);
            et_grNo = (EditText) itemView.findViewById(R.id.et_grNo);
            etDepositSlipNo = (EditText) itemView.findViewById(R.id.etDepositSlipNo);
            etDate = (EditText) itemView.findViewById(R.id.etDate);
            etDescription = (EditText) itemView.findViewById(R.id.etDescription);
            etAmount = (EditText) itemView.findViewById(R.id.etAmount);
            etBalance = (EditText) itemView.findViewById(R.id.etBalance);

        }
    }


    public class AccountStatementViewHolder extends RecyclerView.ViewHolder {


        public AccountStatementViewHolder(View itemView) {
            super(itemView);
        }
    }


}
