package com.tcf.sma.Adapters.HR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePositionModel;
import com.tcf.sma.R;

import java.util.List;


public class PositionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EmployeePositionModel> employeePositionModelList;

    public PositionHistoryAdapter(Context context, List<EmployeePositionModel> employeePositionModelList) {
        this.context = context;
        this.employeePositionModelList = employeePositionModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 0:
//                return new positionHistoryBar(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.custom_positionhistory, parent, false));

//            case 1:
                return new positionHistoryDataVH(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_positionhistory_data, parent, false));


//        }
//
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int new_position) {
//        if (position > 0) {
//            int new_position = position - 1;
            positionHistoryDataVH positionDataVH = (positionHistoryDataVH) holder;
            if (new_position % 2 == 1) {
                //holder.rootView.setBackgroundColor(Color.BLACK);
//                positionDataVH.card_view.setBackgroundResource(R.color.light_app_green);
                positionDataVH.startDate.setTextColor(context.getResources().getColor(R.color.blue_color));
                positionDataVH.position.setTextColor(context.getResources().getColor(R.color.blue_color));
            }
//            else {
//                //holder.rootView.setBackgroundColor(Color.WHITE);
//                positionDataVH.card_view.setBackgroundResource(R.color.new_light_green);
//            }
            String start_date = "";
            if (employeePositionModelList.get(new_position).getPosition_Start_Date() != null) {
                start_date = AppModel.getInstance().convertDatetoFormat(employeePositionModelList.get(new_position).getPosition_Start_Date()
                        , "yyyy-MM-dd", "dd-MMM-yy");
            }
            String end_date = "";
            if (employeePositionModelList.get(new_position).getPosition_End_Date() != null) {
                end_date = AppModel.getInstance().convertDatetoFormat(employeePositionModelList.get(new_position).getPosition_End_Date()
                        , "yyyy-MM-dd", "dd-MMM-yy");
            } else {
                end_date = "Current";
            }
            positionDataVH.startDate.setText(start_date + " - " + end_date);
//            positionDataVH.schoolName.setText(employeePositionModelList.get(new_position).getSchoolId() + "-" + employeePositionModelList.get(new_position).getSchool_Name());
            positionDataVH.position.setText(employeePositionModelList.get(new_position).getEmp_Designation());
//        }

    }

    @Override
    public int getItemCount() {
        return employeePositionModelList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }

//    private class positionHistoryBar extends RecyclerView.ViewHolder {
//
//        positionHistoryBar(View inflate) {
//            super(inflate);
//        }
//    }


    private class positionHistoryDataVH extends RecyclerView.ViewHolder {
        private TextView position, startDate;
//        private CardView card_view;

        positionHistoryDataVH(View inflate) {
            super(inflate);
//            card_view = (CardView) itemView.findViewById(R.id.card_view);
//            schoolName = (TextView) itemView.findViewById(R.id.tv_schoolname_data);
            position = (TextView) itemView.findViewById(R.id.tv_position_data);
            startDate = (TextView) itemView.findViewById(R.id.tv_startDate_data);
//            endDate = (TextView) itemView.findViewById(R.id.tv_endDate_data);
        }
    }
}
