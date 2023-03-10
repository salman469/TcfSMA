package com.tcf.sma.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.BottomFiveSchoolAreaManagerModel;
import com.tcf.sma.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Badar Arain on 3/17/2017.
 */

public class BottomFiveAreaManagerAdapter extends RecyclerView.Adapter<BottomFiveAreaManagerAdapter.BottomFiveViewHolder> {
    public String year, month, dayOfmonth;
    Context context;
    View itemView;
    ArrayList<BottomFiveSchoolAreaManagerModel> mmodelList;
    private int StudentCount = 0;

    public BottomFiveAreaManagerAdapter(ArrayList<BottomFiveSchoolAreaManagerModel> modelList, Context context) {
        this.context = context;
        this.mmodelList = modelList;
    }

    @Override
    public void onViewRecycled(BottomFiveAreaManagerAdapter.BottomFiveViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public BottomFiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            itemView = LayoutInflater.from(context).inflate(R.layout.bottom_five_area_manger_cell, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BottomFiveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BottomFiveViewHolder holder, int position) {
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfDay = new SimpleDateFormat("dd");
        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        dayOfmonth = dfDay.format(Calendar.getInstance().getTime());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        if (mmodelList.get(position).getStudentSchoolName() != null && mmodelList.get(position).getSchoolID() != 0) {
            holder.bottom5SchoolName.setText(mmodelList.get(position).getStudentSchoolName());
            try {
                StudentCount = DatabaseHelper.getInstance(context).getAllStudentsCount(mmodelList.get(position).getSchoolID());
                int days = DatabaseHelper.getInstance(context).getAllAttendanceHeaderCountForMonth(mmodelList.get(position).getSchoolID(), month, year);
                int Totalabsents = DatabaseHelper.getInstance(context).getStudentAttendancebyMonth(mmodelList.get(position).getSchoolID(), month, year, "");
                int Total_student_days = days * StudentCount;
                int TotalPresents = Total_student_days - Totalabsents;
                double MonthPercent = ((double) TotalPresents / (double) Total_student_days) * 100;
                holder.bottom5SchoolStregthAbsent.setText((df.format(MonthPercent) + "%").equals("NaN%") ? "0%" : df.format(MonthPercent) + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public int getItemCount() {
        return mmodelList.size();
    }

    public class BottomFiveViewHolder extends RecyclerView.ViewHolder {

        TextView bottom5SchoolName, bottom5SchoolStregthAbsent;

        public BottomFiveViewHolder(View itemView) {
            super(itemView);

            bottom5SchoolName = (TextView) itemView.findViewById(R.id.school_name5);
            bottom5SchoolStregthAbsent = (TextView) itemView.findViewById(R.id.studetn_strength_absent5);
        }
    }
}
