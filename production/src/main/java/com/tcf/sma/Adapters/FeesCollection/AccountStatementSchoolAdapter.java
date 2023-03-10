package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementSchoolModel;
import com.tcf.sma.R;

import java.util.List;

public class AccountStatementSchoolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AccountStatementSchoolModel> asmList;
    private Activity context;


    public AccountStatementSchoolAdapter(Activity context, List<AccountStatementSchoolModel> asmList) {
        this.context = context;
        this.asmList = asmList;
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
                itemView = LayoutInflater.from(context).inflate(R.layout.account_statement_school_header, parent, false);
                return new AccountStatementViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_account_statement_school_item, parent, false);
                final AccountStatementItemViewHolder itemVH = new AccountStatementItemViewHolder(itemView);
                return itemVH;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position > 0) {
            AccountStatementItemViewHolder itemViewHolder = (AccountStatementItemViewHolder) holder;
            AccountStatementSchoolModel model = asmList.get(position - 1);
//            if (model.getTransactionCategoryId() > 0) {
//                if (model.getTransactionCategoryId() == 1) {
//                    itemViewHolder.etJVNO.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.etReceiptNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.etDepositSlipNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.etDate.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.etDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.etAmount.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.etBalance.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                    itemViewHolder.et_grNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_green));
//                } else if (model.getTransactionCategoryId() == 2) {
//                    itemViewHolder.etJVNO.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.etReceiptNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.etDepositSlipNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.etDate.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.etDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.etAmount.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.etBalance.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                    itemViewHolder.et_grNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_red));
//                }
//            }

//            itemViewHolder.etJVNO.setText(model.getJvNo());
//            itemViewHolder.et_grNo.setText(model.getGrno() + "");

//            itemViewHolder.etDate.setText(model.getDate());
            itemViewHolder.etForDate.setText(AppModel.getInstance()
                    .convertDatetoFormat(model.getForDate(), "yyyy-MM-dd", "dd-MMM-yy"));
            itemViewHolder.etFeeType.setText(model.getFeeTypeName());
//            itemViewHolder.etDepositSlipNo.setText(model.getDepositDate());
//            itemViewHolder.etDepositSlipNo.setText(SurveyAppModel.getInstance().convertDatetoFormat(model.getDepositDate(), "dd-MM-yy", "dd-MMM-yy"));
//            itemViewHolder.etReceiptNo.setText(model.getReceiptNo());


            itemViewHolder.etReceivable.setText(model.getInvoice() + "");
            itemViewHolder.etReceived.setText(model.getReceipt() + "");
            itemViewHolder.etOpeningBalance.setText(model.getOpeningBalance() + "");

//            if (!grNo.equals("")  && model.getGrno().equalsIgnoreCase(grNo)) {
//                itemViewHolder.et_grNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_yellow));
//            }

//            if (!depositSlipNo.trim().isEmpty() && model.getReceiptNo().equalsIgnoreCase(depositSlipNo)) {
//                itemViewHolder.etReceiptNo.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_yellow));
//            }

//            if (!reciptNo.trim().isEmpty()) {
//                for (int j = 0; j < modelNewStructureList.size(); j++) {
//                    if (asmList.get(position).getId() == modelNewStructureList.get(j).getId()) {
//                        if (reciptNo.equalsIgnoreCase(modelNewStructureList.get(j).getReceiptNo())) {
//                            itemViewHolder.etDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_border_yellow));
//                        }
//                    }
//                }
//            }
        }

    }

    @Override
    public int getItemCount() {
        return asmList.size() + 1;
    }

    public class AccountStatementItemViewHolder extends RecyclerView.ViewHolder {


        EditText etOpeningBalance, etForDate, etFeeType, etReceivable, etReceived;

        public AccountStatementItemViewHolder(View itemView) {
            super(itemView);
            etOpeningBalance = itemView.findViewById(R.id.et_openingBalance);
            etForDate = itemView.findViewById(R.id.et_for_Date);
            etFeeType = itemView.findViewById(R.id.et_fee_type);
            etReceivable = itemView.findViewById(R.id.etReceivable);
            etReceived = itemView.findViewById(R.id.etReceived);

        }
    }


    public class AccountStatementViewHolder extends RecyclerView.ViewHolder {


        public AccountStatementViewHolder(View itemView) {
            super(itemView);
        }
    }


}
