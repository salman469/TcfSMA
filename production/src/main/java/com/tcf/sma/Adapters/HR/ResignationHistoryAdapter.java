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
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.util.List;

public class ResignationHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EmployeeSeparationModel> employeeSeparationModelList;
    private EmployeeModel em;
    private List<EmployeeModel> ems;
    private boolean isHistory = true;

    public ResignationHistoryAdapter(Context context, List<EmployeeSeparationModel> employeeSeparationModelList, EmployeeModel em) {
        this.context = context;
        this.employeeSeparationModelList = employeeSeparationModelList;
        this.em = em;
    }

    public ResignationHistoryAdapter(Context context, List<EmployeeSeparationModel> employeeSeparationModelList, boolean history) {
        this.context = context;
        this.employeeSeparationModelList = employeeSeparationModelList;
        this.isHistory = history;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 0:
//                return new resignationHistoryBar(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.custom_resignationhistory, parent, false));

//            case 1:
                return new resignationHistoryDataVH(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.new_resignationhistory_data, parent, false));
//        }

//        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int new_position) {

//        if (position > 0) {
            try {
//                int new_position = position - 1;
                resignationHistoryDataVH resignationDataVH = (resignationHistoryDataVH) holder;

                if (isHistory) {
//                    resignationDataVH.empName.setText(em.getFirst_Name() + " " + em.getLast_Name());
//                    resignationDataVH.empCode.setText(em.getEmployee_Code());
                } else {
                    EmployeeModel employeeModel = EmployeeHelperClass.getInstance(context).getEmployee(employeeSeparationModelList.get(new_position).getEmployee_Personal_Detail_ID());

//                    resignationDataVH.empName.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
//                    resignationDataVH.empCode.setText(employeeModel.getEmployee_Code());

                    /*resignationDataVH.card_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (employeeSeparationModelList.get(new_position).getEmp_Resign_Type() == 1) {
                                if (employeeSeparationModelList.get(new_position).getEmp_Status() == 1) {
                                    Intent dashboardIntent = new Intent(context, ResignationApproval.class);
                                    dashboardIntent.putExtra("empDetailId", employeeSeparationModelList.get(new_position).getEmployee_Personal_Detail_ID());
                                    context.startActivity(dashboardIntent);
                                    ((Activity) context).finish();
                                }

                            } else if (employeeSeparationModelList.get(new_position).getEmp_Resign_Type() == 2) {
                                if (employeeSeparationModelList.get(new_position).getEmp_Status() == 1) {
                                    Intent dashboardIntent = new Intent(context, TerminationApproval.class);
                                    dashboardIntent.putExtra("empDetailId", employeeSeparationModelList.get(new_position).getEmployee_Personal_Detail_ID());
                                    context.startActivity(dashboardIntent);
                                    ((Activity) context).finish();
                                }

                            }
                        }
                    });*/
                }

                resignationDataVH.empResignDate.setText("Submitted On: "+ AppModel.getInstance().convertDatetoFormat(employeeSeparationModelList.get(new_position).getEmp_Resign_Date(), "yyyy-MM-dd", "dd-MMM-yy"));
                resignationDataVH.empLWD.setText("Last Working Date: "+ AppModel.getInstance().convertDatetoFormat(employeeSeparationModelList.get(new_position).getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy"));
                int reasonId = employeeSeparationModelList.get(new_position).getEmp_SubReasonID();
                EmployeeResignReasonModel errmodel = EmployeeHelperClass.getInstance(context).getResignReason(reasonId);
                if (errmodel != null){
                    String reason = "Reason: " ;
                    if(errmodel.getResignReason() != null)
                        reason += errmodel.getResignReason();
                    if(errmodel.getSubReason() != null){
                        String subreason = " - " + errmodel.getSubReason();
                        reason += subreason;
                    }
                    resignationDataVH.empResignReason.setText(reason);
                }

                if (employeeSeparationModelList.get(new_position).getEmp_Resign_Type() == 1)
                    resignationDataVH.empResignType.setText("Resignation");
                else if (employeeSeparationModelList.get(new_position).getEmp_Resign_Type() == 2)
                    resignationDataVH.empResignType.setText("Termination");
                else if (employeeSeparationModelList.get(new_position).getEmp_Resign_Type() == 3)
                    resignationDataVH.empResignType.setText("Deceased");
                if (employeeSeparationModelList.get(new_position).getEmp_Status() == 1) {
                    resignationDataVH.empStatus.setText("Pending");
                    resignationDataVH.empStatus.setTextColor(context.getResources().getColor(R.color.app_green));
//                    resignationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
                } else if (employeeSeparationModelList.get(new_position).getEmp_Status() == 3) {
                    resignationDataVH.empStatus.setText("Approved");
                    resignationDataVH.empStatus.setTextColor(context.getResources().getColor(R.color.blue));
//                    resignationDataVH.card_view.setBackgroundResource(R.color.light_blue_color);
                } else if (employeeSeparationModelList.get(new_position).getEmp_Status() == 2) {
                    resignationDataVH.empStatus.setText("Rejected");
                    resignationDataVH.empStatus.setTextColor(context.getResources().getColor(R.color.red_color));
//                    resignationDataVH.card_view.setBackgroundResource(R.color.light_red_color);
                }








































            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }


    @Override
    public int getItemCount() {
        return employeeSeparationModelList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }

//    private class resignationHistoryBar extends RecyclerView.ViewHolder {
//
//        resignationHistoryBar(View inflate) {
//            super(inflate);
//        }
//    }

    private class resignationHistoryDataVH extends RecyclerView.ViewHolder {
        private TextView empName, empCode, empResignDate, empLWD, empResignReason, empStatus, empResignType;
//        private CardView card_view;

        resignationHistoryDataVH(View inflate) {
            super(inflate);
//            card_view = (CardView) itemView.findViewById(R.id.card_view);
//            empName = (TextView) itemView.findViewById(R.id.tv_empName);
//            empCode = (TextView) itemView.findViewById(R.id.tv_empCode);
            empResignDate = (TextView) itemView.findViewById(R.id.tv_empResignDate);
            empLWD = (TextView) itemView.findViewById(R.id.tv_empLWD);
            empResignReason = (TextView) itemView.findViewById(R.id.tv_empResignReason);
            empStatus = (TextView) itemView.findViewById(R.id.tv_empStatus);
            empResignType = (TextView) itemView.findViewById(R.id.tv_empResignType);

            if (!isHistory) {


            }
        }


    }

}
