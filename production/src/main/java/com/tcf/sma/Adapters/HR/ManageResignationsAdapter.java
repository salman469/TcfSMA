package com.tcf.sma.Adapters.HR;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.HR.ResignationApproval;
import com.tcf.sma.Activities.HR.TerminationApproval;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;

import java.util.List;

public class ManageResignationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public static List<EmployeeSeparationModel> resignationModelList;
    private boolean isViewSeparation;

    public ManageResignationsAdapter(Context context, List<EmployeeSeparationModel> resignationModelList, boolean isViewSeparation) {
        this.context = context;
        this.resignationModelList = resignationModelList;
        this.isViewSeparation = isViewSeparation;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        if(isViewSeparation)
            itemView = LayoutInflater.from(context).inflate(R.layout.custom_separation_view, viewGroup, false);
        else
            itemView = LayoutInflater.from(context).inflate(R.layout.custom_separation_approval, viewGroup, false);

        return new ResignationDataVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int new_position) {
//        if (position > 0) {

        ResignationDataVH ResignationDataVH = (ResignationDataVH) viewHolder;

        String resignDate = AppModel.getInstance().convertDatetoFormat(resignationModelList.get(new_position).getEmp_Resign_Date(), "yyyy-MM-dd", "dd-MMM-yyyy");
        String lastWorkingDate = AppModel.getInstance().convertDatetoFormat(resignationModelList.get(new_position).getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yyyy");
        ResignationDataVH.resignDate.setText("Resign date:\n" + resignDate);
        ResignationDataVH.lastWorkingDate.setText("Last Day:\n" + lastWorkingDate);
        if(isViewSeparation){
            String resignTypeAndStatus="";
            if(resignationModelList.get(new_position).getEmp_Resign_Type() == 1)
                resignTypeAndStatus = "Resignation";
            if(resignationModelList.get(new_position).getEmp_Resign_Type() == 2)
                resignTypeAndStatus = "Termination";
            if(resignationModelList.get(new_position).getEmp_Resign_Type() == 3)
                resignTypeAndStatus = "Death";

            int resignStatus = resignationModelList.get(new_position).getSep_status();
            if(resignStatus == 1){
                resignTypeAndStatus = resignTypeAndStatus.concat("\nPending");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_blue_color);
            } else if (resignStatus == 3) {
                resignTypeAndStatus = resignTypeAndStatus.concat("\nApproved");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
            } else if (resignStatus == 2) {
                resignTypeAndStatus = resignTypeAndStatus.concat("\nRejected");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_red_color);
            } else if (resignStatus == 4) {
                resignTypeAndStatus = resignTypeAndStatus.concat("\nCancelled");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_gray);
            } else if (resignStatus == 5) {
                resignTypeAndStatus = resignTypeAndStatus.concat("\nAuto Approved");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
            }


            ResignationDataVH.resignType.setText(resignTypeAndStatus);


        } else {

            if(resignationModelList.get(new_position).getEmp_Resign_Type() == 1)
                ResignationDataVH.resignType.setText("Resignation");
            if(resignationModelList.get(new_position).getEmp_Resign_Type() == 2){
                ResignationDataVH.resignType.setText("Termination");
                ResignationDataVH.resignType.setTypeface(ResignationDataVH.resignType.getTypeface(),Typeface.BOLD);
                ResignationDataVH.resignType.setTextColor(context.getResources().getColor(R.color.red_color));
            }
            if(resignationModelList.get(new_position).getEmp_Resign_Type() == 3)
                ResignationDataVH.resignType.setText("Death");


            int resignStatus = resignationModelList.get(new_position).getSep_status();
            if (resignStatus == 1) {
                ResignationDataVH.btnApproved.setVisibility(View.GONE);
                ResignationDataVH.btnReject.setVisibility(View.GONE);
                ResignationDataVH.btnPending.setVisibility(View.VISIBLE);
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_blue_color);
                ResignationDataVH.btnPending.setText("P");

            } else if (resignStatus == 3) {
                ResignationDataVH.btnReject.setVisibility(View.GONE);
                ResignationDataVH.btnPending.setVisibility(View.GONE);
                ResignationDataVH.btnApproved.setVisibility(View.VISIBLE);
                ResignationDataVH.btnApproved.setText("A");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
            } else if (resignStatus == 2) {
                ResignationDataVH.btnPending.setVisibility(View.GONE);
                ResignationDataVH.btnApproved.setVisibility(View.GONE);
                ResignationDataVH.btnReject.setVisibility(View.VISIBLE);
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_red_color);
                ResignationDataVH.btnReject.setText("R");
            } else if (resignStatus == 4) {
                ResignationDataVH.btnPending.setVisibility(View.GONE);
                ResignationDataVH.btnApproved.setVisibility(View.GONE);
                ResignationDataVH.btnReject.setVisibility(View.VISIBLE);
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_red_color);
                ResignationDataVH.btnReject.setText("C");
            } else if (resignStatus == 5) {
                ResignationDataVH.btnReject.setVisibility(View.GONE);
                ResignationDataVH.btnPending.setVisibility(View.GONE);
                ResignationDataVH.btnApproved.setVisibility(View.VISIBLE);
                ResignationDataVH.btnApproved.setText("AA");
                ResignationDataVH.card_view.setBackgroundResource(R.color.light_app_green);
            }
        }



        EmployeeModel employeeModel = EmployeeHelperClass.getInstance(context).getEmployee(resignationModelList.get(new_position).getEmployee_Personal_Detail_ID());

        ResignationDataVH.tv_designation.setText(employeeModel.getDesignation());
        ResignationDataVH.tv_empName.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
        ResignationDataVH.tv_empCode.setText(employeeModel.getEmployee_Code());


//        }
    }

    @Override
    public int getItemCount() {
        return resignationModelList.size();
    }


    public class ResignationDataVH extends RecyclerView.ViewHolder {

        private TextView resignDate, lastWorkingDate, tv_empName, tv_empCode, resignType, tv_designation;
        private Button btnApproved, btnPending, btnReject;
        private CardView card_view;
        private LinearLayout ll_cardView;

        ResignationDataVH(@NonNull View itemView) {
            super(itemView);

            resignType = itemView.findViewById(R.id.tv_resignType);
            card_view = itemView.findViewById(R.id.card_view);
            resignDate = (TextView) itemView.findViewById(R.id.tv_resignDate_data);
            lastWorkingDate = (TextView) itemView.findViewById(R.id.tv_lastWorkingDate_data);
//            status = (TextView) itemView.findViewById(R.id.tv_status_data);
            tv_empName = (TextView) itemView.findViewById(R.id.tv_empName);
            tv_empCode = itemView.findViewById(R.id.tv_empCode);
            btnApproved = itemView.findViewById(R.id.Btn_approved);
            btnPending = itemView.findViewById(R.id.Btn_pending);
            btnReject = itemView.findViewById(R.id.Btn_reject);
            tv_designation = itemView.findViewById(R.id.tv_empDesignation);
            ll_cardView = itemView.findViewById(R.id.ll_cardView);



//            if(isViewSeparation){
                card_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (resignationModelList.get(getAdapterPosition()).getEmp_Resign_Type() == 1) {
                        try {
                            Intent dashboardIntent = new Intent(context, ResignationApproval.class);
                            dashboardIntent.putExtra("resignationID", resignationModelList.get(getAdapterPosition()).getServer_id());
                            dashboardIntent.putExtra("isManage", true);
                            context.startActivity(dashboardIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        } else if (resignationModelList.get(getAdapterPosition()).getEmp_Resign_Type() == 2) {
//                            Intent dashboardIntent = new Intent(context, TerminationApproval.class);
//                            dashboardIntent.putExtra("empDetailId", resignationModelList.get(getAdapterPosition()).getEmployee_Personal_Detail_ID());
//                            dashboardIntent.putExtra("isManage", true);
//                            context.startActivity(dashboardIntent);
//
//                        }
                    }
                });

//            }
        }
    }
}
