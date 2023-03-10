package com.tcf.sma.Adapters.HR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.util.List;

public class SeparationHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EmployeeSeparationDetailModel> employeeSeparationModelList;

    public SeparationHistoryAdapter(Context context, List<EmployeeSeparationDetailModel> employeeSeparationModelList) {
        this.context = context;
        this.employeeSeparationModelList = employeeSeparationModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new separationHistoryBar(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_separationhistory, parent, false));

            case 1:
                return new separationHistoryDataVH(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_separationhistory_data, parent, false));
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position > 0) {
            try {
                int new_position = position - 1;
                separationHistoryDataVH separationDataVH = (separationHistoryDataVH) holder;
                int approverID = employeeSeparationModelList.get(new_position).getApprover_userId();
                EmployeeModel employeeModel = EmployeeHelperClass.getInstance(context).getEmployee(approverID);
                if(employeeModel!=null){
                    try {
                        separationDataVH.approverName.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
                        separationDataVH.designation.setText(employeeModel.getDesignation());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (employeeSeparationModelList.get(new_position).getEmp_status() == 1) {
                    separationDataVH.empStatus.setText("Pending");
                    separationDataVH.card_view.setBackgroundResource(R.color.light_blue_color);
                } else if (employeeSeparationModelList.get(new_position).getEmp_status() == 3) {
                    separationDataVH.empStatus.setText("Approved");
                    separationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
                } else if (employeeSeparationModelList.get(new_position).getEmp_status() == 5) {
                    separationDataVH.empStatus.setText("Auto Approved");
                    separationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
                } else if (employeeSeparationModelList.get(new_position).getEmp_status() == 2) {
                    separationDataVH.empStatus.setText("Rejected");
                    separationDataVH.card_view.setBackgroundResource(R.color.light_red_color);
                } else if (employeeSeparationModelList.get(new_position).getEmp_status() == 4) {
                    separationDataVH.empStatus.setText("Cancelled");
                    separationDataVH.card_view.setBackgroundResource(R.color.light_gray);
                }
                separationDataVH.remarks.setText(employeeSeparationModelList.get(new_position).getSeparation_Remarks());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemCount() {
        return employeeSeparationModelList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private class separationHistoryBar extends RecyclerView.ViewHolder {

        separationHistoryBar(View inflate) {
            super(inflate);
        }
    }

    private class separationHistoryDataVH extends RecyclerView.ViewHolder {
        private TextView approverName, designation, empStatus, remarks;
        private CardView card_view;

        separationHistoryDataVH(View inflate) {
            super(inflate);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            approverName = (TextView) itemView.findViewById(R.id.tv_approverName);
            designation = (TextView) itemView.findViewById(R.id.tv_designation);
            empStatus = (TextView) itemView.findViewById(R.id.tv_status);
            remarks = (TextView) itemView.findViewById(R.id.tv_remarks);
        }


    }

}
