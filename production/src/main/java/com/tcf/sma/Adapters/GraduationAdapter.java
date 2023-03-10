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

import com.tcf.sma.Activities.GraduationActivity;
import com.tcf.sma.Models.GraduationModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class GraduationAdapter extends RecyclerView.Adapter<GraduationAdapter.GraduationViewHolder> {
    public static ArrayList<GraduationModel> mgma;
    Activity activity;

    public GraduationAdapter(ArrayList<GraduationModel> gma, Activity activity) {
        mgma = gma;
        this.activity = activity;
    }

    @Override
    public GraduationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.graduation_list_cell, parent, false);
        return new GraduationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GraduationViewHolder holder, final int position) {

        String currentClass = StudentModel.getInstance().getStudentsList().get(position).getCurrentClass();
        if (currentClass.toLowerCase().contains("class")) {
            holder.tv_class.setText(currentClass.split("\\-")[1] + "-" + StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        } else {
            holder.tv_class.setText(currentClass);
        }

        holder.tv_grNo.setText(StudentModel.getInstance().getStudentsList().get(position).getGrNo());
        holder.tv_StudentName.setText(StudentModel.getInstance().getStudentsList().get(position).getName());
        holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
        holder.isGraduated.setChecked(true);
        mgma.get(position).setIsGraduated("y");

        ((GraduationActivity) activity).setTotalGraduatedCount();
        holder.rg_graduations.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_graduated:
                        mgma.get(position).setIsGraduated("y");
                        ((GraduationActivity) activity).setTotalGraduatedCount();
                        holder.card.setBackgroundColor(activity.getResources().getColor(R.color.light_app_green));
                        break;
                    case R.id.rb_notgraduated:
                        mgma.get(position).setIsGraduated("n");
                        ((GraduationActivity) activity).setTotalGraduatedCount();
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

    public class GraduationViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class, tv_grNo, tv_StudentName;
        RadioButton isGraduated, notGraduated;
        RadioGroup rg_graduations;
        CardView card;

        public GraduationViewHolder(View itemView) {
            super(itemView);

            tv_class = (TextView) itemView.findViewById(R.id.tv_classname);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_StudentName = (TextView) itemView.findViewById(R.id.tv_name_text);
            card = (CardView) itemView.findViewById(R.id.graduation_cell_card);
            isGraduated = (RadioButton) itemView.findViewById(R.id.rb_graduated);
            notGraduated = (RadioButton) itemView.findViewById(R.id.rb_notgraduated);
            rg_graduations = (RadioGroup) itemView.findViewById(R.id.rg_graduations);
        }
    }
}
