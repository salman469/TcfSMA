package com.tcf.sma.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.PromotionActivity;
import com.tcf.sma.Models.PromotionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    public static ArrayList<PromotionModel> mpma;
    Activity activity;

    public PromotionAdapter(ArrayList<PromotionModel> pma, Activity activity) {
        mpma = pma;
        this.activity = activity;
    }

    @Override
    public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.promotions_list_cell, parent, false);
        return new PromotionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PromotionViewHolder holder, final int position) {

        String currentClass = StudentModel.getInstance().getStudentsList().get(position).getCurrentClass();
        if (currentClass.toLowerCase().contains("class")) {
            holder.tv_class.setText(currentClass.split("\\-")[1] + "-" + StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        } else {
            holder.tv_class.setText(currentClass);
        }

        holder.tv_grNo.setText(StudentModel.getInstance().getStudentsList().get(position).getGrNo());
        holder.tv_StudentName.setText(StudentModel.getInstance().getStudentsList().get(position).getName());
        holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
        holder.isPromoted.setChecked(true);
        mpma.get(position).setIsPromoted("y");


//        if (mpma.get(position).getIsPromoted() != null) {
//            if (mpma.get(position).getIsPromoted().equals("y")) {
//                holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
//                holder.isPromoted.setChecked(true);
//            } else if (mpma.get(position).getIsPromoted().equals("n")) {
//                holder.notPromoted.setChecked(true);
//                holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_red_color));
//            }
//        }
        ((PromotionActivity) activity).setTotalPromotedCount();
        holder.rg_promotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_promoted:
                        mpma.get(position).setIsPromoted("y");
                        ((PromotionActivity) activity).setTotalPromotedCount();
                        holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
                        break;
                    case R.id.rb_notpromoted:
                        mpma.get(position).setIsPromoted("n");
                        ((PromotionActivity) activity).setTotalPromotedCount();
                        holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_red_color));
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (StudentModel.getInstance().getStudentsList() != null) {
            return StudentModel.getInstance().getStudentsList().size();
        } else {
            return 0;
        }
    }

    public class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class, tv_grNo, tv_StudentName;
        RadioButton isPromoted, notPromoted;
        RadioGroup rg_promotions;
        CardView card;

        public PromotionViewHolder(View itemView) {
            super(itemView);

            tv_class = (TextView) itemView.findViewById(R.id.tv_classname);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_StudentName = (TextView) itemView.findViewById(R.id.tv_name_text);
            card = (CardView) itemView.findViewById(R.id.promotion_cell_card);
            isPromoted = (RadioButton) itemView.findViewById(R.id.rb_promoted);
            notPromoted = (RadioButton) itemView.findViewById(R.id.rb_notpromoted);
            rg_promotions = (RadioGroup) itemView.findViewById(R.id.rg_promotions);
        }
    }
}
