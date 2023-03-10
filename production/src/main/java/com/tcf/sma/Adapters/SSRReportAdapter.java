package com.tcf.sma.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.SSRReportModel;
import com.tcf.sma.R;

import java.util.List;

public class SSRReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<SSRReportModel> ssrReportList;

    public SSRReportAdapter(Activity context, List<SSRReportModel> ssrReportList) {
        this.context = context;
        this.ssrReportList = ssrReportList;
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
                itemView = LayoutInflater.from(context).inflate(R.layout.header_ssr_report, parent, false);
                return new SSRReportAdapter.SSRHeaderVH(itemView);
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_ssr_report, parent, false);
                return new SSRReportAdapter.SSRItemVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Vholder, int position) {
        SSRReportAdapter.SSRItemVH holder = (SSRReportAdapter.SSRItemVH) Vholder;
        if (position > 0 && position < ssrReportList.size() + 1) {

            if (position % 2 == 0) {
                holder.ll_view.setBackgroundResource(R.color.white);

            }
        } else if (position == ssrReportList.size() + 1) {

        }
    }

    @Override
    public int getItemCount() {
        return ssrReportList != null || ssrReportList.size() == 0 ? 0 : ssrReportList.size() + 2;
    }

    public class SSRItemVH extends RecyclerView.ViewHolder {

        TextView tvMonthYear, tv_class_sec, tv_opening_ssr, tv_new_admission, tv_readmission, tv_sec_in,
                tv_sec_out, tv_withdrawal, tv_graduate, tv_current_ssr, tv_capacity, tv_max_ssr,
                tv_opr_utl, tv_turnover;
        LinearLayout ll_view;

        public SSRItemVH(View itemView) {
            super(itemView);
            tvMonthYear = (TextView) itemView.findViewById(R.id.tvMonthYear);
            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            tv_opening_ssr = (TextView) itemView.findViewById(R.id.tv_opening_ssr);
            tv_new_admission = (TextView) itemView.findViewById(R.id.tv_new_admission);
            tv_readmission = (TextView) itemView.findViewById(R.id.tv_readmission);
            tv_sec_in = (TextView) itemView.findViewById(R.id.tv_sec_in);
            tv_sec_out = (TextView) itemView.findViewById(R.id.tv_sec_out);
            tv_withdrawal = (TextView) itemView.findViewById(R.id.tv_withdrawal);
            tv_graduate = (TextView) itemView.findViewById(R.id.tv_graduate);
            tv_current_ssr = (TextView) itemView.findViewById(R.id.tv_current_ssr);
            tv_capacity = (TextView) itemView.findViewById(R.id.tv_capacity);
            tv_max_ssr = (TextView) itemView.findViewById(R.id.tv_max_ssr);
            tv_opr_utl = (TextView) itemView.findViewById(R.id.tv_opr_utl);
            tv_turnover = (TextView) itemView.findViewById(R.id.tv_turnover);

            ll_view = (LinearLayout) itemView.findViewById(R.id.ll_view);
        }
    }

    public class SSRHeaderVH extends RecyclerView.ViewHolder {

        public SSRHeaderVH(View itemView) {
            super(itemView);
        }
    }
}
