package com.tcf.sma.Adapters.FeesCollection;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.FeesCollection.ReceiptDetails;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.ReceiptListModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ReceiptListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ReceiptListModel> list;

    public ReceiptListAdapter(List<ReceiptListModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 0:
                return new ReceiptHeaderVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipt_list_header, viewGroup, false));
            case 1:
                return new ReceiptVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipt_list_item, viewGroup, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder receiptVH, final int i) {
        if (receiptVH instanceof ReceiptVH) {
            ReceiptVH vh = (ReceiptVH) receiptVH;
            ReceiptListModel model = list.get(i - 1);
            if (model.getIsDeposited()) {
                vh.st_card_view.setBackgroundResource(R.color.light_app_green);
            } else {
                vh.st_card_view.setBackgroundResource(R.color.light_red_color);
            }
            vh.receiptNo.setText(model.getReceiptNo());
            vh.schoolId.setText(model.getSchoolId() + "");
            if (model.getStudent_gr_no() == 0)
                vh.grNo.setText("Transferred [" + model.getStudentId() + "]");
            else
                vh.grNo.setText(model.getStudent_gr_no() + "");

            vh.totalAmount.setText(Math.round(model.getTotalAmount()) + "");
            vh.tv_deposit_slip_no.setText(model.getDeposit_slip_no() + "");
            vh.createdBy.setText(model.getCreateBy());
            vh.createdOn.setText(AppModel.getInstance().convertDatetoFormat(model.getCreateOn(), "yyyy-MM-dd h:mm:ss a", "dd-MMM-yy"));
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(receiptVH.itemView.getContext(), ReceiptDetails.class);
                    in.putExtra("schoolId", list.get(i - 1).getSchoolId());
                    in.putExtra("headerId", list.get(i - 1).getReceiptId());
                    in.putExtra("receipt_no", list.get(i - 1).getReceiptNo());
                    in.putExtra("isDeposited", list.get(i - 1).getIsDeposited());
                    in.putExtra("depositSlipNo", list.get(i - 1).getDeposit_slip_no());
                    in.putExtra("studentId", list.get(i - 1).getStudentId());
                    in.putExtra("studentGrNo", list.get(i - 1).getStudent_gr_no());
                    in.putExtra("schoolClassId", list.get(i - 1).getSchoolClassId());
                    receiptVH.itemView.getContext().startActivity(in);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size() + 1;
        else return 0;
    }

    public class ReceiptVH extends RecyclerView.ViewHolder {
        TextView receiptNo, schoolId, grNo, totalAmount, createdBy, createdOn, tv_deposit_slip_no;
        LinearLayout st_card_view;

        public ReceiptVH(@NonNull View itemView) {
            super(itemView);
            st_card_view = itemView.findViewById(R.id.st_card_view);
            receiptNo = itemView.findViewById(R.id.tv_receiptNo);
            schoolId = itemView.findViewById(R.id.tv_schoolId);
            grNo = itemView.findViewById(R.id.tv_gr_no);
            totalAmount = itemView.findViewById(R.id.tv_total_amount);
            createdBy = itemView.findViewById(R.id.tv_createdBy);
            createdOn = itemView.findViewById(R.id.tv_createdOn);
            tv_deposit_slip_no = itemView.findViewById(R.id.tv_deposit_slip_no);
        }
    }

    public class ReceiptHeaderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout ll_receipt_no, ll_school_id, ll_gr_no, ll_depositSlip_no, ll_totalAmount, ll_created_by, ll_created_on;
        ImageView iv_receipt_no, iv_school_id, iv_gr_no, iv_depositSlip_no, iv_totalAmount, iv_created_by, iv_created_on;

        ReceiptHeaderVH(@NonNull View itemView) {
            super(itemView);
            ll_receipt_no = itemView.findViewById(R.id.ll_receipt_no);
            ll_school_id = itemView.findViewById(R.id.ll_school_id);
            ll_gr_no = itemView.findViewById(R.id.ll_gr_no);
            ll_depositSlip_no = itemView.findViewById(R.id.ll_depositSlip_no);
            ll_totalAmount = itemView.findViewById(R.id.ll_totalAmount);
            ll_created_by = itemView.findViewById(R.id.ll_created_by);
            ll_created_on = itemView.findViewById(R.id.ll_created_on);

            iv_receipt_no = itemView.findViewById(R.id.iv_receipt_no);
            iv_school_id = itemView.findViewById(R.id.iv_school_id);
            iv_gr_no = itemView.findViewById(R.id.iv_gr_no);
            iv_depositSlip_no = itemView.findViewById(R.id.iv_depositSlip_no);
            iv_totalAmount = itemView.findViewById(R.id.iv_totalAmount);
            iv_created_by = itemView.findViewById(R.id.iv_created_by);
            iv_created_on = itemView.findViewById(R.id.iv_created_on);

            ll_receipt_no.setOnClickListener(this);
            ll_school_id.setOnClickListener(this);
            ll_gr_no.setOnClickListener(this);
            ll_depositSlip_no.setOnClickListener(this);
            ll_totalAmount.setOnClickListener(this);
            ll_created_by.setOnClickListener(this);
            ll_created_on.setOnClickListener(this);
        }

        private void Sort_In_Asc(int f) {
            if (f == 1) {        //Receipt no
                Collections.sort(list, (rlm1, rlm2) -> {
                    if (rlm1.getReceiptNo() != null && rlm2.getReceiptNo() != null) {
                        return Integer.compare(Integer.parseInt(rlm1.getReceiptNo()), Integer.parseInt(rlm2.getReceiptNo()));
                    }
                    return -1;
                });
            } else if (f == 2) {      //School id
                Collections.sort(list, (rlm1, rlm2) -> Integer.compare(rlm1.getSchoolId(), rlm2.getSchoolId()));
            } else if (f == 3) {      //Gr no
                Collections.sort(list, (rlm1, rlm2) -> Integer.compare(rlm1.getStudent_gr_no(), rlm2.getStudent_gr_no()));
            } else if (f == 4) {      //Deposit slip no
                Collections.sort(list, (rlm1, rlm2) -> rlm1.getDeposit_slip_no().compareTo(rlm2.getDeposit_slip_no()));
            } else if (f == 5) {      //Total amount
                Collections.sort(list, (rlm1, rlm2) -> Double.compare(rlm1.getTotalAmount(), rlm2.getTotalAmount()));
            } else if (f == 6) {      //Created by
//                Collections.sort(list, (rlm1, rlm2) -> rlm1.getCreateBy().compareTo(rlm2.getCreateBy()));
                Collections.sort(list, (rlm1, rlm2) -> {
                    if (rlm1.getCreateBy() != null && rlm2.getCreateBy() != null) {
                        return Integer.compare(Integer.parseInt(rlm1.getCreateBy()), Integer.parseInt(rlm2.getCreateBy()));
                    }
                    return -1;
                });

            } else if (f == 7) {      //Created on
                Collections.sort(list, (rlm1, rlm2) -> sortDateUsing(rlm1.getCreateOn(), rlm2.getCreateOn(), "asc"));
            }

            notifyDataSetChanged();
        }

        private void Sort_In_Des(int f) {
            if (f == 1) {        //Receipt no
                Collections.sort(list, (rlm1, rlm2) -> {
                    if (rlm1.getReceiptNo() != null && rlm2.getReceiptNo() != null) {
                        return Integer.compare(Integer.parseInt(rlm2.getReceiptNo()), Integer.parseInt(rlm1.getReceiptNo()));
                    }
                    return -1;
                });
            } else if (f == 2) {      //School id
                Collections.sort(list, (rlm1, rlm2) -> Integer.compare(rlm2.getSchoolId(), rlm1.getSchoolId()));
            } else if (f == 3) {      //Gr no
                Collections.sort(list, (rlm1, rlm2) -> Integer.compare(rlm2.getStudent_gr_no(), rlm1.getStudent_gr_no()));
            } else if (f == 4) {      //Deposit slip no
                Collections.sort(list, (rlm1, rlm2) -> rlm2.getDeposit_slip_no().compareTo(rlm1.getDeposit_slip_no()));
            } else if (f == 5) {      //Total amount
                Collections.sort(list, (rlm1, rlm2) -> Double.compare(rlm2.getTotalAmount(), rlm1.getTotalAmount()));
            } else if (f == 6) {      //Created by
//                Collections.sort(list, (rlm1, rlm2) -> rlm2.getCreateBy().compareTo(rlm1.getCreateBy()));
                Collections.sort(list, (rlm1, rlm2) -> {
                    if (rlm1.getCreateBy() != null && rlm2.getCreateBy() != null) {
                        return Integer.compare(Integer.parseInt(rlm2.getCreateBy()), Integer.parseInt(rlm1.getCreateBy()));
                    }
                    return -1;
                });

            } else if (f == 7) {      //Created on
                Collections.sort(list, (rlm1, rlm2) -> sortDateUsing(rlm2.getCreateOn(), rlm1.getCreateOn(), "asc"));
            }

            notifyDataSetChanged();
        }

        private int sortDateUsing(String Date, String Date1, String order) {
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

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ll_receipt_no) {
                if (iv_receipt_no.getDrawable().getConstantState() == iv_receipt_no.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_receipt_no.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(1);
                } else {
                    iv_receipt_no.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(1);
                }
            } else if (v.getId() == R.id.ll_school_id) {
                if (iv_school_id.getDrawable().getConstantState() == iv_school_id.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_school_id.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(2);
                } else {
                    iv_school_id.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(2);
                }
            } else if (v.getId() == R.id.ll_gr_no) {
                if (iv_gr_no.getDrawable().getConstantState() == iv_gr_no.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_gr_no.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(3);
                } else {
                    iv_gr_no.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(3);
                }
            } else if (v.getId() == R.id.ll_depositSlip_no) {
                if (iv_depositSlip_no.getDrawable().getConstantState() == iv_depositSlip_no.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_depositSlip_no.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(4);
                } else {
                    iv_depositSlip_no.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(4);
                }
            } else if (v.getId() == R.id.ll_totalAmount) {
                if (iv_totalAmount.getDrawable().getConstantState() == iv_totalAmount.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_totalAmount.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(5);
                } else {
                    iv_totalAmount.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(5);
                }
            } else if (v.getId() == R.id.ll_created_by) {
                if (iv_created_by.getDrawable().getConstantState() == iv_created_by.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_created_by.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(6);
                } else {
                    iv_created_by.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(6);
                }
            } else if (v.getId() == R.id.ll_created_on) {
                if (iv_created_on.getDrawable().getConstantState() == iv_created_on.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_created_on.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(7);
                } else {
                    iv_created_on.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(7);
                }
            }
        }
    }
}
