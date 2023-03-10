package com.tcf.sma.Adapters.HR;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.HR.EmployeeListing;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.R;

import java.util.List;

public class EmployeeInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<EmployeeModel> employeeModel;
    private Activity context;

    public EmployeeInfoAdapter(Activity context, List<EmployeeModel> employeeModel) {
        this.context = context;
        this.employeeModel = employeeModel;
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_employee_info_header_item, parent, false);
                return new SessionInfoHeadVH(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_employee_info_item, parent, false);

                final SessionInfoItemVH itemVH = new SessionInfoItemVH(itemView);

                return itemVH;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position > 0) {
            try {
                SessionInfoItemVH sessionHolder = (SessionInfoItemVH) holder;
                sessionHolder.tv_designation.setText(employeeModel.get(holder.getAdapterPosition() - 1).getDesignation());
                sessionHolder.tv_count.setText(employeeModel.get(holder.getAdapterPosition() - 1).getEmpCount());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return (employeeModel.size() != 0) ? employeeModel.size() + 1 : 0;
    }

    public class SessionInfoItemVH extends RecyclerView.ViewHolder {

        TextView tv_designation, tv_count;

        public SessionInfoItemVH(View itemView) {
            super(itemView);
            tv_designation = (TextView) itemView.findViewById(R.id.tv_designation);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, EmployeeListing.class).putExtra("designation",employeeModel.get(getAdapterPosition() - 1).getDesignation()));
                }
            });
        }
    }


    public class SessionInfoHeadVH extends RecyclerView.ViewHolder {


        public SessionInfoHeadVH(View itemView) {
            super(itemView);
        }
    }

}
