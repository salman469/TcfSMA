package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.Fees_Collection.SessionInfoModel;
import com.tcf.sma.R;

import java.util.List;

public class SessionInfoTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SessionInfoModel> models;
    private Activity context;

    public SessionInfoTableAdapter(Activity context, List<SessionInfoModel> model) {
        this.context = context;
        this.models = model;
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
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_session_info_header_item, parent, false);
                return new SessionInfoHeadVH(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_session_info_item, parent, false);

                final SessionInfoItemVH itemVH = new SessionInfoItemVH(itemView);

                return itemVH;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position > 0) {
            try {
                SessionInfoItemVH sessionHolder = (SessionInfoItemVH) holder;
                sessionHolder.tv_withdrawal.setText(models.get(position - 1).getWithdrawl() + "");
                sessionHolder.tv_grad.setText(models.get(position - 1).getGraduate() + "");
                sessionHolder.tv_tranfer.setText(models.get(position - 1).getTransfers() + "");
                sessionHolder.tv_reAd.setText(models.get(position - 1).getReAdmmission() + "");
                sessionHolder.tv_newAd.setText(models.get(position - 1).getNewAdmission() + "");
                sessionHolder.tv_sessionInfo.setText(models.get(position - 1).getSessionInfo() == null ? "" : models.get(position - 1).getSessionInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return (models.size() != 0) ? models.size() + 1 : 0;
    }

    public class SessionInfoItemVH extends RecyclerView.ViewHolder {

        TextView tv_sessionInfo, tv_newAd, tv_reAd, tv_tranfer, tv_grad, tv_withdrawal;

        public SessionInfoItemVH(View itemView) {
            super(itemView);
            tv_sessionInfo = (TextView) itemView.findViewById(R.id.tv_sessionInfo);
            tv_newAd = (TextView) itemView.findViewById(R.id.tv_newAd);
            tv_reAd = (TextView) itemView.findViewById(R.id.tv_reAd);
            tv_tranfer = (TextView) itemView.findViewById(R.id.tv_transfer);
            tv_grad = (TextView) itemView.findViewById(R.id.tv_graduate);
            tv_withdrawal = (TextView) itemView.findViewById(R.id.tv_withdrawal);
        }
    }


    public class SessionInfoHeadVH extends RecyclerView.ViewHolder {


        public SessionInfoHeadVH(View itemView) {
            super(itemView);
        }
    }

}
