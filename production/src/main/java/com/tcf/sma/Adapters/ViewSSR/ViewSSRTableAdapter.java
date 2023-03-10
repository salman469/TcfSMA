package com.tcf.sma.Adapters.ViewSSR;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.ViewSSR.ViewSSRTableModel;
import com.tcf.sma.R;

import java.util.List;

public class ViewSSRTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ViewSSRTableModel> models;
    private Activity mContext;

    public ViewSSRTableAdapter(Activity context, List<ViewSSRTableModel> model) {
        this.mContext = context;
        this.models = model;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewSSRTableModel obj;
        ViewSSRItemVH itemVH = null;
        double overall, male, female;

        if (position > 0 && (position <= models.size())) { // exclude header so -1 inside get(position -1)
            obj = models.get(position - 1);
            itemVH = (ViewSSRTableAdapter.ViewSSRItemVH) holder;

            if (obj == null) {
                male = 0;
                female = 0;
                overall = 0;
            } else {
                male = obj.getmMale();
                female = obj.getmFemale();
                overall = obj.getmOverall();

            }

            // indivisual row computation
            if (overall <= 0) {
                overall = 0;
                itemVH.tvOverall.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                obj.setmOverall(overall);
            }

            itemVH.tvMale.setText((int) male + "");

            itemVH.tvFemale.setText(
                    (int) female + "");

            itemVH.tvOverall.setText((int) overall + "");

            switch (position) {
                case 1:
                    itemVH.tvPurpose.setText(mContext.getString(R.string.previousMonthSSR));
                    break;
                case 2:
                    itemVH.tvPurpose.setText(mContext.getString(R.string.newAdmissionThisMonth));
                    break;
                case 3:
                    itemVH.tvPurpose.setText(mContext.getString(R.string.withDrawalsThisMonth));
                    break;
                case 4:
                    itemVH.tvPurpose.setText(mContext.getString(R.string.graduateThisMonth));
                    break;

            }
        } else if (position == models.size() + 1 && models.size() > 0) { // for total row
            itemVH = (ViewSSRTableAdapter.ViewSSRItemVH) holder;

            itemVH.tvPurpose.setText(mContext.getString(R.string.totalCurrentSSR));
            itemVH.tvPurpose.setTypeface(itemVH.tvPurpose.getTypeface(), Typeface.BOLD);

            try {
                computeSum(itemVH);
            } catch (NumberFormatException e) {
                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void computeSum(ViewSSRTableAdapter.ViewSSRItemVH itemVH) throws NumberFormatException {
        double male = 0, female = 0, overall = 0;

        for (int i = 0; i < models.size(); i++) {
            if (i <= 1) {
                male += models.get(i).getmMale();
                female += models.get(i).getmFemale();
                overall += models.get(i).getmOverall();
            } else {
                male -= models.get(i).getmMale();
                female -= models.get(i).getmFemale();
                overall -= models.get(i).getmOverall();
            }
        }

        try {
           /* itemVH.tvTotalDue.setText(
                    currencyFormat(String.valueOf(due)));*/

            itemVH.tvMale.setText((int) male + "");
            itemVH.tvFemale.setText((int) female + "");
            itemVH.tvOverall.setText((int) overall + "");

            /*itemVH.tvBalance.setText(
                    currencyFormat(String.valueOf(balance)));

            itemVH.tvCollected.setText(
                    currencyFormat(String.valueOf(collected)));*/

            if (overall == 0.0) {
                itemVH.tvOverall.setTextColor(mContext.getResources().getColor(R.color.red));
            }

            itemVH.tvMale.setTypeface(itemVH.tvMale.getTypeface(), Typeface.BOLD);
            itemVH.tvOverall.setTypeface(itemVH.tvOverall.getTypeface(), Typeface.BOLD);
            itemVH.tvFemale.setTypeface(itemVH.tvFemale.getTypeface(), Typeface.BOLD);
        } catch (Exception e) {
            throw new NumberFormatException();
        }
    }

    @Override
    public int getItemCount() {
        if (models.size() > 0)
            return models.size() + 2; // to create additional header row and last row for total values...
        else
            return models.size() + 1;// empty list so normal scenario only header shows
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.rowview_view_ssr_header, parent, false);
                return new ViewSSRHeadVH(itemView);

            default:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.rowview_view_ssr_item, parent, false);

                final ViewSSRTableAdapter.ViewSSRItemVH itemVH = new ViewSSRTableAdapter.ViewSSRItemVH(itemView);

                return itemVH;
        }
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

    public class ViewSSRItemVH extends RecyclerView.ViewHolder {

        TextView tvPurpose, tvMale, tvFemale, tvOverall;

        public ViewSSRItemVH(View itemView) {
            super(itemView);
            tvPurpose = (TextView) itemView.findViewById(R.id.tvPurpose);
            tvMale = (TextView) itemView.findViewById(R.id.tvMale);
            tvFemale = (TextView) itemView.findViewById(R.id.tvFemale);
            tvOverall = (TextView) itemView.findViewById(R.id.tvOverall);

            tvMale.setSelected(true);
            tvOverall.setSelected(true);
            tvFemale.setSelected(true);
            tvPurpose.setSelected(true);
        }
    }

    public class ViewSSRHeadVH extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewSSRHeadVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.wdtm);
            tv.setSelected(true);
        }
    }
}
