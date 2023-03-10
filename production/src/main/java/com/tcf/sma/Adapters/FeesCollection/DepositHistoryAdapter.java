package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.CashDepositDetail;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.DepositHistoryModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DepositHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    private List<DepositHistoryModel> dhmList;


    public DepositHistoryAdapter(Activity activity, List<DepositHistoryModel> dhmList) {
        this.activity = activity;
        this.dhmList = dhmList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else return position;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView;
        switch (viewType) {
            case 0:
                ItemView = LayoutInflater.from(activity).inflate(R.layout.header_deposit_history_list, parent, false);
                return new DepositHistoryAdapter.DepostHistoryHeaderVH(ItemView);

            default:
                ItemView = LayoutInflater.from(activity).inflate(R.layout.item_depost_history_list, parent, false);
                return new DepositHistoryAdapter.DepostHistoryItemVH(ItemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Vholder, final int position) {

        if (position > 0 && position < dhmList.size() + 1) {

            DepositHistoryAdapter.DepostHistoryItemVH holder = (DepositHistoryAdapter.DepostHistoryItemVH) Vholder;
//
//            // background color change in even odd sequence
            holder.stCardView.setBackgroundResource(R.color.light_app_green);
//            if (position % 2 == 0) {
//                holder.stCardView.setBackgroundResource(R.color.light_app_green);
//            } else {
//                holder.stCardView.setBackgroundResource(R.color.white);
//            }

//          index of list is (position-1) for getting right index. 1st row is header row so -1 row.

            String total = dhmList.get(position - 1).getTotal() + "";

            total = (total.contains(".")) ? total.split("\\.")[0] : total;

            holder.tv_date.setText(AppModel.getInstance().convertDatetoFormat(dhmList.get(position - 1).getDepost_date(), "yyyy-MM-dd", "dd-MMM-yy"));
            holder.tv_slipNo.setText(dhmList.get(position - 1).getDepost_slipNo() + "");
            holder.tv_depositedBy.setText(dhmList.get(position - 1).getDeposited_by());
            holder.tv_total.setText(total);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CashDepositDetail.class);
                    intent.putExtra("slip_no", dhmList.get(position - 1).getDepost_slipNo());
                    intent.putExtra("depositId", dhmList.get(position - 1).getDeposit_id());
                    intent.putExtra("schoolId", dhmList.get(position - 1).getSchoolid());
                    activity.startActivity(intent);
                }
            });
//
//
        } else if (position == dhmList.size() + 1) {
            DepositHistoryAdapter.DepostHistoryItemVH holder = (DepositHistoryAdapter.DepostHistoryItemVH) Vholder;
            holder.tv_date.setText("");
            holder.tv_slipNo.setText("");
            holder.tv_depositedBy.setText("");
            holder.tv_total.setText("");
            holder.stCardView.setBackgroundResource(R.color.white);

            holder.tv_depositedBy.setTextColor(Color.BLACK);
            holder.tv_total.setTextColor(Color.BLACK);

            holder.tv_depositedBy.setTypeface(holder.tv_depositedBy.getTypeface(), Typeface.BOLD);
            holder.tv_total.setTypeface(holder.tv_total.getTypeface(), Typeface.BOLD);

            DepositHistoryModel dhm = getTotal();

            String total = dhm.getTotal() + "";


            total = (total.contains(".")) ? total.split("\\.")[0] : total;

            holder.tv_depositedBy.setText("Total");
            holder.tv_total.setText(total);
        }
    }

    private DepositHistoryModel getTotal() {
        int total = 0;

        DepositHistoryModel mod = new DepositHistoryModel();

        if (dhmList != null) {
            for (DepositHistoryModel model : dhmList) {
                total += model.getTotal();
            }
        }
        mod.setTotal(total);
        return mod;
    }

    @Override
    public int getItemCount() {

        return dhmList == null || dhmList.size() == 0 ? 0 : dhmList.size() + 2;// one header and one total row included
//        return dhmList == null ||  dhmList.size() == 0 ? 0 : dhmList.size() + 1;
//        return scrm == null || scrm.size() == 0 ? 0 : scrm.size() + 2; // one header and one total row included
    }

    public class DepostHistoryItemVH extends RecyclerView.ViewHolder {
        LinearLayout stCardView;
        TextView tv_date, tv_slipNo, tv_depositedBy, tv_total;

        public DepostHistoryItemVH(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_slipNo = (TextView) itemView.findViewById(R.id.tv_slipNo);
            tv_depositedBy = (TextView) itemView.findViewById(R.id.tv_depositedBy);
            tv_total = (TextView) itemView.findViewById(R.id.tv_total);
            stCardView = (LinearLayout) itemView.findViewById(R.id.st_card_view);

        }
    }

    public class DepostHistoryHeaderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout stCardView, ll_slipNo, ll_total, ll_depositedBy, ll_date;
        ImageView iv_slipNo, iv_total, iv_depositedBy, iv_date;

        public DepostHistoryHeaderVH(View itemView) {
            super(itemView);
            stCardView = (LinearLayout) itemView.findViewById(R.id.st_card_view);
            ll_slipNo = itemView.findViewById(R.id.ll_slipNo);
            ll_total = itemView.findViewById(R.id.ll_total);
            ll_depositedBy = itemView.findViewById(R.id.ll_depositedBy);
            ll_date = itemView.findViewById(R.id.ll_date);

            iv_slipNo = itemView.findViewById(R.id.iv_slipNo);
            iv_total = itemView.findViewById(R.id.iv_total);
            iv_depositedBy = itemView.findViewById(R.id.iv_depositedBy);
            iv_date = itemView.findViewById(R.id.iv_date);

            ll_slipNo.setOnClickListener(this);
            ll_total.setOnClickListener(this);
            ll_depositedBy.setOnClickListener(this);
            ll_date.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_slipNo:
                    if (iv_slipNo.getDrawable().getConstantState() == iv_slipNo.getResources()
                            .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                        iv_slipNo.setImageResource(android.R.drawable.arrow_down_float);
                        Sort_In_Des(1);
                    } else {
                        iv_slipNo.setImageResource(android.R.drawable.arrow_up_float);
                        Sort_In_Asc(1);
                    }
                    break;
                case R.id.ll_total:
                    if (iv_total.getDrawable().getConstantState() == iv_total.getResources()
                            .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                        iv_total.setImageResource(android.R.drawable.arrow_down_float);
                        Sort_In_Des(2);
                    } else {
                        iv_total.setImageResource(android.R.drawable.arrow_up_float);
                        Sort_In_Asc(2);
                    }
                    break;
                case R.id.ll_depositedBy:
                    if (iv_depositedBy.getDrawable().getConstantState() == iv_depositedBy.getResources()
                            .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                        iv_depositedBy.setImageResource(android.R.drawable.arrow_down_float);
                        Sort_In_Des(3);
                    } else {
                        iv_depositedBy.setImageResource(android.R.drawable.arrow_up_float);
                        Sort_In_Asc(3);
                    }
                    break;
                case R.id.ll_date:
                    if (iv_date.getDrawable().getConstantState() == iv_date.getResources()
                            .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                        iv_date.setImageResource(android.R.drawable.arrow_down_float);
                        Sort_In_Des(4);
                    } else {
                        iv_date.setImageResource(android.R.drawable.arrow_up_float);
                        Sort_In_Asc(4);
                    }
                    break;
            }
        }

        private void Sort_In_Asc(int f) {
            if (f == 1) {        //SlipNo
                Collections.sort(dhmList, (dhm1, dhm2) -> {
                    return dhm1.getDepost_slipNo().compareTo(dhm2.getDepost_slipNo());
                });
            } else if (f == 2) {      //Total
                Collections.sort(dhmList, (dhm1, dhm2) -> {
                    return Double.compare(dhm1.getTotal(), dhm2.getTotal());
                });
            } else if (f == 3) {      //Deposited by
                Collections.sort(dhmList, (dhm1, dhm2) -> {
                    if (dhm1.getDeposited_by() != null && dhm2.getDeposited_by() != null) {
                        return Integer.compare(Integer.parseInt(dhm1.getDeposited_by()), Integer.parseInt(dhm2.getDeposited_by()));
                    }
                    return -1;
                });
//                Collections.sort(dhmList, (dhm1, dhm2) -> {
//                    return dhm1.getDeposited_by().compareTo(dhm2.getDeposited_by());
//                });
            } else if (f == 4) {      //Date
                Collections.sort(dhmList, (dhm1, dhm2) -> sortDateUsing(dhm1.getDepost_date(), dhm2.getDepost_date()));
            }

            notifyDataSetChanged();
        }

        private void Sort_In_Des(int f) {
            if (f == 1) {        //SlipNo
                Collections.sort(dhmList, (dhm1, dhm2) -> {
                    return dhm2.getDepost_slipNo().compareTo(dhm1.getDepost_slipNo());
                });
            } else if (f == 2) {      //Total
                Collections.sort(dhmList, (dhm1, dhm2) -> {
                    return Double.compare(dhm2.getTotal(), dhm1.getTotal());
                });
            } else if (f == 3) {      //Deposited by
                Collections.sort(dhmList, (dhm1, dhm2) -> {
                    if (dhm1.getDeposited_by() != null && dhm2.getDeposited_by() != null) {
                        return Integer.compare(Integer.parseInt(dhm2.getDeposited_by()), Integer.parseInt(dhm1.getDeposited_by()));
                    }
                    return -1;
                });
//                Collections.sort(dhmList, (dhm1, dhm2) -> {
//                    return dhm2.getDeposited_by().compareTo(dhm1.getDeposited_by());
//                });
            } else if (f == 4) {      //Date
                Collections.sort(dhmList, (dhm1, dhm2) -> sortDateUsing(dhm2.getDepost_date(), dhm1.getDepost_date()));
            }

            notifyDataSetChanged();
        }

        private int sortDateUsing(String Date, String Date1) {
            java.util.Date date = null, date1 = null;

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                if (Date != null && Date1 != null && !Date.isEmpty() && !Date1.isEmpty()) {
                    date = formatDate.parse(Date);
                    date1 = formatDate.parse(Date1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null && date1 != null) {
                return date.compareTo(date1);
            }
            return 0;
        }
    }
}
