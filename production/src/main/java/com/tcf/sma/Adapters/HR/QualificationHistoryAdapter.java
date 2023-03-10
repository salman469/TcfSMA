package com.tcf.sma.Adapters.HR;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.RetrofitModels.HR.EmployeeQualificationDetailModel;
import com.tcf.sma.R;

import java.util.List;

public class QualificationHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EmployeeQualificationDetailModel> qualificationDetailModelList;

    public QualificationHistoryAdapter(Context context, List<EmployeeQualificationDetailModel> qualificationDetailModelList) {
        this.context = context;
        this.qualificationDetailModelList = qualificationDetailModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 0:
//                return new QualificationBar(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.custom_qualificationhistory, parent, false));

//            case 1:
                return new QualificationData(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_qualificationhistory_data, parent, false));

//        }

//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int new_position) {

        try {
//        if (position > 0) {
//            int new_position = position - 1;
            QualificationData qualificationDataHolder = (QualificationData) holder;
            if (new_position % 2 == 1) {
                qualificationDataHolder.tv_type_data.setTextColor(context.getResources().getColor(R.color.blue_color));
                qualificationDataHolder.tv_instituteName_data.setTextColor(context.getResources().getColor(R.color.blue_color));
                qualificationDataHolder.tv_passYear_data.setTextColor(context.getResources().getColor(R.color.blue_color));
                qualificationDataHolder.tv_division_data.setTextColor(context.getResources().getColor(R.color.blue_color));
//                qualificationDataHolder.card_view.setBackgroundResource(R.color.light_app_green);
            }
//            else {
//                qualificationDataHolder.card_view.setBackgroundResource(R.color.new_light_green);
//            }

            String degree = qualificationDetailModelList.get(new_position).getQualification_Type() + " in " + qualificationDetailModelList.get(new_position).getSubject_Name() + " (" + qualificationDetailModelList.get(new_position).getDegree_Name() + ")";
            qualificationDataHolder.tv_type_data.setText(degree);
//            qualificationDataHolder.tv_level_data.setText(qualificationDetailModelList.get(new_position).getQualification_Level());
            qualificationDataHolder.tv_instituteName_data.setText(qualificationDetailModelList.get(new_position).getInstitute_Name());
//            qualificationDataHolder.tv_degree_data.setText();
//            qualificationDataHolder.tv_subject_data.setText();
            qualificationDataHolder.tv_passYear_data.setText(qualificationDetailModelList.get(new_position).getPassing_Year());
            String division = qualificationDetailModelList.get(new_position).getGrade_Division();
            if(division!=null && !division.isEmpty()) {
                if (division.length() == 1)
                    qualificationDataHolder.tv_division_data.setText(division + " Grade");
                else
                    qualificationDataHolder.tv_division_data.setText(division + " Division");
            } else {
                qualificationDataHolder.tv_division_data.setText("");
            }
//        }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return qualificationDetailModelList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }

//    private class QualificationBar extends RecyclerView.ViewHolder {
//        QualificationBar(View inflate) {
//            super(inflate);
//        }
//    }

    private class QualificationData extends RecyclerView.ViewHolder {
        TextView tv_type_data,  tv_instituteName_data, tv_passYear_data, tv_division_data;
//        CardView card_view;

        QualificationData(View inflate) {
            super(inflate);
//            card_view = (CardView) itemView.findViewById(R.id.card_view);
            tv_type_data = (TextView) itemView.findViewById(R.id.tv_type_data);
//            tv_level_data = (TextView) itemView.findViewById(R.id.tv_level_data);
            tv_instituteName_data = (TextView) itemView.findViewById(R.id.tv_instituteName_data);
//            tv_degree_data = (TextView) itemView.findViewById(R.id.tv_degree_data);
//            tv_subject_data = (TextView) itemView.findViewById(R.id.tv_subject_data);
            tv_passYear_data = (TextView) itemView.findViewById(R.id.tv_passYear_data);
            tv_division_data = (TextView) itemView.findViewById(R.id.tv_division_data);

        }
    }
}
