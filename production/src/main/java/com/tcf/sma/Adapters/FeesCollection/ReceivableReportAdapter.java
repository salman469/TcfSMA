package com.tcf.sma.Adapters.FeesCollection;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.ReceiptDetails;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.ReceiptListModel;
import com.tcf.sma.R;

import java.util.List;

public class ReceivableReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ReceiptListModel> receivableList;

    public ReceivableReportAdapter(List<ReceiptListModel> scrm) {
        this.receivableList = scrm;
    }

    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView;
        if (viewType == 0) {
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receivable_report_list_header, parent, false);
            return new ReceivableHeaderVH(ItemView);
        }
        ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receivable_report_list_item, parent, false);
        return new ReceivableItemVH(ItemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Vholder, int position) {

        if (position > 0 && position < receivableList.size() + 1) {

            ReceivableItemVH holder = (ReceivableItemVH) Vholder;

            // background color change in even odd sequence
            if (position % 2 == 0) {
                holder.stCardView.setBackgroundResource(R.color.light_app_green);
            } else {
                holder.stCardView.setBackgroundResource(R.color.white);
            }

            //index of list is (position-1) for getting right index. 1st row is header row so -1 row.

            String date = AppModel.getInstance().convertDatetoFormat(receivableList.get(position-1).getCreateOn(), "yyyy-MM-dd hh:mm:ss a", "dd/MMM/yy");
            holder.tv_date.setText(date);
            holder.tv_description.setText("");
            if(receivableList.get(position-1).getCategoryId() == 1)
                holder.tv_type.setText("Invoice");
            else if(receivableList.get(position-1).getCategoryId() == 2)
                holder.tv_type.setText("Receipt");
            else
                holder.tv_type.setText("Opening Balance");

            holder.tv_amount.setText(String.valueOf(receivableList.get(position-1).getTotalAmount()));
//            if (receivableList.get(position - 1).getReceivables() != 0)
                holder.tv_receivable.setText(receivableList.get(position - 1).getReceivables() + "");
//            holder.tv_receivable.setText("");


        }
    }

    @Override
    public int getItemCount() {
        return receivableList == null || receivableList.size() == 0 ? 0 : receivableList.size() + 1; // one header and one total row included
    }

    public class ReceivableItemVH extends RecyclerView.ViewHolder {
        LinearLayout stCardView;
        TextView tv_date, tv_description, tv_amount, tv_receivable, tv_type;

        public ReceivableItemVH(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_receivable = (TextView) itemView.findViewById(R.id.tv_receivable);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            stCardView = itemView.findViewById(R.id.st_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (receivableList.get(getAdapterPosition() - 1).getCategoryId() == 2) {
                        Intent in = new Intent(itemView.getContext(), ReceiptDetails.class);
                        in.putExtra("schoolId", receivableList.get(getAdapterPosition() - 1).getSchoolId());
                        in.putExtra("headerId", receivableList.get(getAdapterPosition() - 1).getReceiptId());
                        in.putExtra("receipt_no", receivableList.get(getAdapterPosition() - 1).getReceiptNo());
                        in.putExtra("isDeposited", receivableList.get(getAdapterPosition() - 1).getIsDeposited());
                        in.putExtra("depositSlipNo", receivableList.get(getAdapterPosition() - 1).getDeposit_slip_no());
                        in.putExtra("studentId", receivableList.get(getAdapterPosition() - 1).getStudentId());
                        in.putExtra("studentGrNo", receivableList.get(getAdapterPosition() - 1).getStudent_gr_no());
                        in.putExtra("schoolClassId", receivableList.get(getAdapterPosition() - 1).getSchoolClassId());
                        itemView.getContext().startActivity(in);
                    }
                }
            });

        }
    }

    public class ReceivableHeaderVH extends RecyclerView.ViewHolder {
        LinearLayout layout;

        public ReceivableHeaderVH(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.ll);
        }
    }
}
