package com.tcf.sma.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentAttendanceReportModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class StudentAttendanceReportAdapter extends RecyclerView.Adapter<StudentAttendanceReportAdapter.StudentViewHolder> {
    Activity activity;
    int countA = 0, countL = 0, countP = 0;
    int totalWorkingDays;
    private List<StudentAttendanceReportModel> sarList;
    private List<StudentModel> smList;
    private String attendanceTotal30Days = "0", attendanceTaken30Days = "0";
//    public StudentAttendanceReportAdapter(Activity activity,List<StudentAttendanceReportModel> studentAttendanceReportModelList) {
//        this.sarList=studentAttendanceReportModelList;
//        this.activity = activity;
//    }

    public StudentAttendanceReportAdapter(Activity activity, List<StudentModel> smList) {
        this.activity = activity;
        this.smList = smList;
    }

    public StudentAttendanceReportAdapter(Activity activity, List<StudentModel> smList, int totalWorkingDays) {
        this.activity = activity;
        this.smList = smList;
        this.totalWorkingDays = totalWorkingDays;
    }


    @Override
    public StudentAttendanceReportAdapter.StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.student_attendance_report_view_list_cell, parent, false);
        return new StudentAttendanceReportAdapter.StudentViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(StudentAttendanceReportAdapter.StudentViewHolder holder, int position) {

        String attendencePercentage = getAttendencePercentage(position);

        if (position % 2 == 0) {
            if (!smList.get(position).isActive()) {
                holder.st_card_view.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
            } else {
                holder.st_card_view.setBackgroundResource(R.color.light_app_green);
            }

        } else {
            if (!smList.get(position).isActive()) {
                holder.st_card_view.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
            } else {
                holder.st_card_view.setBackgroundResource(R.color.white);
            }
        }

        holder.tv_totalWorkingDays.setText(String.valueOf(totalWorkingDays));
        holder.tv_Present.setText(String.valueOf(countP));
        holder.tv_Absent.setText(String.valueOf(countA));
        holder.tv_AttendancePercentage.setText(attendencePercentage);

    }


    @Override
    public int getItemCount() {
        if (smList != null) {
            return smList.size();
        }
        return 0;
    }


    private String getAttendencePercentage(int position) {
        String percentage = "";
        //getting currentmonth and year


        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();

        String currYear = sdf3.format(calendar.getTime());
        String currMonth = sdf2.format(calendar.getTime());

        List<StudentAttendanceReportModel> attendencelist = DatabaseHelper.getInstance(activity)
                .getStudentOneYearAttendanceCount(smList.get(position).getId(),
                        AppModel.getInstance().getSelectedSchool(activity),
                        currYear);

        if (attendencelist != null && attendencelist.size() != 0) {
            for (int i = 0; i < attendencelist.size(); i++) {
                if (attendencelist.get(i).isAbsent()) {
                    countA++;
                } else {
                    countL++;
                }
            }
        }

        int totalDaysElapsed = DatabaseHelper.getInstance(activity).
                getAllAttendanceHeaderCountYearforClass(smList.get(position).getSchoolClassId(),
                        currYear);

        countP = Math.abs(totalDaysElapsed - attendencelist.size());
        percentage = countPercentages(totalWorkingDays);

        return percentage;
    }

    private String countPercentages(int days) {
        String percentage = "";
        double dabsentPercent = 0, dleavePercent = 0, dpresentPercent = 0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        dpresentPercent = ((countP / (double) days) * 100);
//        dabsentPercent = ((countA / (double) days) * 100);
//        dleavePercent = ((countL / (double) days) * 100);
        percentage = (df.format(dpresentPercent) + "%").equals("NaN%") ? "0%" : df.format(dpresentPercent) + "%";
        return percentage;
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tv_totalWorkingDays, tv_Present, tv_Absent, tv_AttendancePercentage;
        CardView st_card_view;

        public StudentViewHolder(View itemView) {
            super(itemView);

            tv_totalWorkingDays = (TextView) itemView.findViewById(R.id.tv_totalWorkingDays);
            tv_Present = (TextView) itemView.findViewById(R.id.tv_Present);
            tv_Absent = (TextView) itemView.findViewById(R.id.tv_Absent);
            tv_AttendancePercentage = (TextView) itemView.findViewById(R.id.tv_AttendancePercentage);
            st_card_view = (CardView) itemView.findViewById(R.id.st_card_view);
        }
    }
}
