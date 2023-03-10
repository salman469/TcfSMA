package com.tcf.sma.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.ClassReportModel;
import com.tcf.sma.R;

import java.util.List;

public class ClassReportAdapter extends RecyclerView.Adapter<ClassReportAdapter.ClassViewHolder> {
    Activity activity;
    private List<ClassReportModel> classReportModelList;

    public ClassReportAdapter(Activity activity, List<ClassReportModel> classReportModelList) {
        this.activity = activity;
        this.classReportModelList = classReportModelList;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.class_report_view_list_cell, parent, false);
        return new ClassViewHolder(ItemView);
    }

    @Override
    public int getItemCount() {
        if (classReportModelList != null) {
            return classReportModelList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {

        if (position % 2 == 0) {
            holder.llClassResult.setBackgroundResource(R.color.light_app_green);
        } else {
            holder.llClassResult.setBackgroundResource(R.color.white);
        }

        String currentClassSection = classReportModelList.get(position).getClass_section_name();
        try {
            holder.tv_class_sec.setText(currentClassSection);
            String[] split = currentClassSection.split("\\-");
            holder.tv_class_sec.setText(split[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        double operationUtl = 0;
        operationUtl = classReportModelList.get(position).getOptUtls();

        holder.tvActiveStudents.setText("" + classReportModelList.get(position).getStudentActiveCount());
        holder.tvCapacity.setText("" + classReportModelList.get(position).getCapacity());
        holder.tvOptUtls.setText((int) operationUtl + "%");
        holder.tvMaxCapacity.setText("" + classReportModelList.get(position).getMaxCapacity());

    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class_sec, tvActiveStudents, tvCapacity, tvOptUtls, tvMaxCapacity;
        LinearLayout llClassResult;

        public ClassViewHolder(View itemView) {
            super(itemView);

            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            llClassResult = (LinearLayout) itemView.findViewById(R.id.llClassResult);
            tvActiveStudents = (TextView) itemView.findViewById(R.id.tvActiveStudents);
            tvCapacity = (TextView) itemView.findViewById(R.id.tvCapacity);
            tvOptUtls = (TextView) itemView.findViewById(R.id.tvOptUtls);
            tvMaxCapacity = (TextView) itemView.findViewById(R.id.tvMaxCapacity);
        }
    }
}
