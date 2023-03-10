package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.FeesCollectionReportModel;
import com.tcf.sma.R;

import java.util.List;

public class FeesCollectionReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private List<FeesCollectionReportModel> fcrmList;

    public FeesCollectionReportAdapter(Activity activity) {
        this.context = activity;
    }

    public FeesCollectionReportAdapter(Activity context, List<FeesCollectionReportModel> fcrmodel) {
        this.context = context;
        this.fcrmList = fcrmodel;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.header_fees_collection_report, parent, false);
                return new FeesCollectionReportAdapter.FeesCollectionHeaderVH(itemView);
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_fees_collection_report, parent, false);
                return new FeesCollectionReportAdapter.FeesCollectionItemVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Vholder, int position) {
        if (position > 0 && position < fcrmList.size() + 1) {
            FeesCollectionReportAdapter.FeesCollectionItemVH holder = (FeesCollectionReportAdapter.FeesCollectionItemVH) Vholder;
            if (position % 2 == 0) {
                holder.ll_view.setBackgroundResource(R.color.white);
            }

//          index of list is (position-1) for getting right index. 1st row is header row so -1 row.
            String collected = fcrmList.get(position - 1).getCollected() + "";
            collected = (collected.contains(".")) ? collected.split("\\.")[0] : collected;

            String receivable = fcrmList.get(position - 1).getDues() + "";
            receivable = (receivable.contains(".")) ? receivable.split("\\.")[0] : receivable;

            String target_amount = fcrmList.get(position - 1).getTarget() + "";
            target_amount = (target_amount.contains(".")) ? target_amount.split("\\.")[0] : target_amount;

            holder.tv_month.setText(AppModel.getInstance().
                    convertDatetoFormat(fcrmList.get(position - 1).getMonth(), "yyyy-MM", "MMM-yy"));
            holder.tv_collection.setText(collected);
            holder.tv_receivable.setText(receivable);
            holder.tv_target.setText(target_amount);

        } else if (position == fcrmList.size() + 1) {
            FeesCollectionReportAdapter.FeesCollectionItemVH holder = (FeesCollectionReportAdapter.FeesCollectionItemVH) Vholder;
            holder.tv_month.setText("");
            holder.tv_collection.setText("");
            holder.tv_receivable.setText("");
            holder.tv_target.setText("");
            holder.ll_view.setBackgroundResource(R.color.white);

            holder.tv_month.setTextColor(Color.BLACK);
            holder.tv_collection.setTextColor(Color.BLACK);
            holder.tv_receivable.setTextColor(Color.BLACK);
            holder.tv_target.setTextColor(Color.BLACK);

            holder.tv_month.setTypeface(holder.tv_month.getTypeface(), Typeface.BOLD);
            holder.tv_collection.setTypeface(holder.tv_collection.getTypeface(), Typeface.BOLD);
            holder.tv_receivable.setTypeface(holder.tv_receivable.getTypeface(), Typeface.BOLD);
            holder.tv_target.setTypeface(holder.tv_target.getTypeface(), Typeface.BOLD);

            FeesCollectionReportModel fcrm = getTotal();

            String totalReceivable = fcrm.getDues() + "";
            String totalCollected = fcrm.getCollected() + "";
            String totalTargetAmount = fcrm.getTarget() + "";
//            String total = fcrm.getTotal() +"";

            totalReceivable = (totalReceivable.contains(".")) ? totalReceivable.split("\\.")[0] : totalReceivable;
            totalCollected = (totalCollected.contains(".")) ? totalCollected.split("\\.")[0] : totalCollected;
            totalTargetAmount = (totalTargetAmount.contains(".")) ? totalTargetAmount.split("\\.")[0] : totalTargetAmount;
//            total = (total.contains(".")) ? total.split("\\.")[0] : total;

            holder.tv_month.setText("Total");
            holder.tv_collection.setText(totalCollected);
            holder.tv_receivable.setText(totalReceivable);
            holder.tv_target.setText(totalTargetAmount);
        }
    }

    private FeesCollectionReportModel getTotal() {
        int totaldues = 0;
        int totalcollected = 0;
        int totaltargetamount = 0;
        int balance = 0;
//        for (FeesCollectionReportModel model : fcrmList) {
//            totaldues += model.getDues();
//            totalcollected += model.getCollected();
//        }


//        int total = 0;

        FeesCollectionReportModel mod = new FeesCollectionReportModel();

        if (fcrmList != null) {
            for (FeesCollectionReportModel model : fcrmList) {
                totaldues += model.getDues();
                totalcollected += model.getCollected();
                totaltargetamount += model.getTarget();
//                total += model.getTotal();
            }
        }

        balance = totaldues - totalcollected;

        mod.setDues(totaldues);
        mod.setCollected(totalcollected);
        mod.setTarget(totaltargetamount);

        mod.setBalance(balance);
//        mod.setTotal(total);

        return mod;
    }

    @Override
    public int getItemCount() {
        return fcrmList == null || fcrmList.size() == 0 ? 0 : fcrmList.size() + 2;// one header and one total row included
    }


    public class FeesCollectionItemVH extends RecyclerView.ViewHolder {

        TextView tv_month, tv_target, tv_receivable, tv_collection;
        LinearLayout ll_view;

        public FeesCollectionItemVH(View itemView) {
            super(itemView);
            ll_view = (LinearLayout) itemView.findViewById(R.id.ll_view);
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
            tv_target = (TextView) itemView.findViewById(R.id.tv_target);
            tv_receivable = (TextView) itemView.findViewById(R.id.tv_receivable);
            tv_collection = (TextView) itemView.findViewById(R.id.tv_collection);
        }
    }

    public class FeesCollectionHeaderVH extends RecyclerView.ViewHolder {
//        LinearLayout stCardView;

        public FeesCollectionHeaderVH(View itemView) {
            super(itemView);
//            stCardView = (LinearLayout) itemView.findViewById(R.id.st_card_view);
        }
    }
}
