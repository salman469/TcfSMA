package com.tcf.sma.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.Fees_Collection.AttendanceSummary;
import com.tcf.sma.Models.SchoolAttendanceReportModel;
import com.tcf.sma.Models.StudentAttendanceReportModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mohammad.Haseeb on 2/2/2017.
 */

public class SchoolReportAdapter extends RecyclerView.Adapter<SchoolReportAdapter.StudentViewHolder> {
    ArrayList<StudentModel> smList;
    ArrayList<AttendanceModel> amList;
    Activity activity;
    int SectionId, ClassID;
    private List<String> prevResvList = new ArrayList<>();
    private int totalWorkingDays;
    private String for_date;
    private int class_sectionID, SchoolID;
    private List<SchoolAttendanceReportModel> schoolarm;

    public SchoolReportAdapter(ArrayList<StudentModel> smList, Activity activity, int ClassID, int SectionId, int totalWorkingDays) {
        this.smList = smList;
        this.activity = activity;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
        this.totalWorkingDays = totalWorkingDays;
    }

    public SchoolReportAdapter(int SchoolID, ArrayList<AttendanceModel> amList, Activity activity, int class_sectionID, int totalWorkingDays) {
        this.amList = amList;
        this.activity = activity;
        this.class_sectionID = class_sectionID;
        this.totalWorkingDays = totalWorkingDays;
        this.SchoolID = SchoolID;
    }

    public SchoolReportAdapter(Activity activity, List<SchoolAttendanceReportModel> schoolarm) {
        this.activity = activity;
        this.schoolarm = schoolarm;
    }

    @Override
    public SchoolReportAdapter.StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.school_report_view_list_cell, parent, false);
        return new SchoolReportAdapter.StudentViewHolder(ItemView);
    }

    @Override
    public int getItemCount() {
        if (schoolarm != null) {
            return schoolarm.size() + 2;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(SchoolReportAdapter.StudentViewHolder holder, int position) {

        if (position < schoolarm.size()) {
            if (position % 2 == 0) {
                holder.llSchoolResult.setBackgroundResource(R.color.light_app_green);
            } else {
                holder.llSchoolResult.setBackgroundResource(R.color.white);
            }

            String currentClassSection = schoolarm.get(position).getClass_section_name();
            try {
                holder.tv_class_sec.setText(currentClassSection);
//                if (currentClassSection.toLowerCase().contains("class")) {
                String[] split = currentClassSection.split("\\-");
                holder.tv_class_sec.setText(split[1]);
//                } else {
//                    holder.tv_class_sec.setText(currentClassSection.split(" ")[0]);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//            for_date = amList.get(position).getForDate();
//            holder.tvMonthYear.setText(SurveyAppModel.getInstance().convertDatetoFormat(for_date, "yyyy-MM-dd", "MMM-yy"));
            for_date = schoolarm.get(position).getMonth() + "-" + schoolarm.get(position).getYear();
            holder.tvMonthYear.setText(for_date);


            //For Total Attendence
            holder.tvAttendance.setText(String.valueOf(schoolarm.get(position).getAttendance()) + "%");
        } else if (position == schoolarm.size()) {
            holder.tvMonthYear.setText("");
            holder.tv_class_sec.setText("");
            holder.tvAttendance.setText("");
            holder.llSchoolResult.setBackgroundResource(R.color.gray_cell);

            holder.tvMonthYear.setTextColor(Color.BLACK);
            holder.tv_class_sec.setTextColor(Color.BLACK);
            holder.tvAttendance.setTextColor(Color.BLACK);

            holder.tv_class_sec.setTypeface(holder.tv_class_sec.getTypeface(), Typeface.BOLD);
            holder.tvAttendance.setTypeface(holder.tvAttendance.getTypeface(), Typeface.BOLD);

            double monthAttendance = 0;
            monthAttendance = getSchoolAttendanceForThisMonth(schoolarm.get(position - 1).getSchoolid(),
                    schoolarm.get(position - 1).getAcademicSessionId());

            holder.tv_class_sec.setText("Overall Attendance % This Month ");
            holder.tvAttendance.setText(monthAttendance + "%");

        } else if (position == schoolarm.size() + 1) {
            holder.tvMonthYear.setText("");
            holder.tv_class_sec.setText("");
            holder.tvAttendance.setText("");
            holder.llSchoolResult.setBackgroundResource(R.color.light_gray);

            holder.tvMonthYear.setTextColor(Color.BLACK);
            holder.tv_class_sec.setTextColor(Color.BLACK);
            holder.tvAttendance.setTextColor(Color.BLACK);

            holder.tv_class_sec.setTypeface(holder.tv_class_sec.getTypeface(), Typeface.BOLD);
            holder.tvAttendance.setTypeface(holder.tvAttendance.getTypeface(), Typeface.BOLD);

            double sessionAttendance = 0;
            sessionAttendance = getSessionAttendance(schoolarm.get(position - 2).getSchoolid(),
                    schoolarm.get(position - 2).getAcademicSessionId());

            holder.tv_class_sec.setText("Overall Attendance % This Session (" + schoolarm.get(position - 2).getSession() + ")");
            holder.tvAttendance.setText(String.valueOf(sessionAttendance) + "%");

//            double avgAttendance=0;
//            try {
//
//                for (int i=0;i<schoolarm.size();i++){
//                    avgAttendance+=schoolarm.get(i).getAttendance();
//                }
//                avgAttendance=avgAttendance/schoolarm.size();
//                DecimalFormat df = new DecimalFormat();
//                df.setMaximumFractionDigits(1);
//                avgAttendance= Double.parseDouble(df.format(avgAttendance));
//            }catch (Exception e){
//                e.printStackTrace();
//            }


        }


    }

    private double getSessionAttendance(int schoolid, int academicSessionId) {
        double totalAttendancePercThisSession = 0;
        try {
            AttendanceSummary attendanceSummary = AttendancePercentage.getInstance(activity)
                    .getAttendancePercentageforThisSession(schoolid,
                            academicSessionId);

            totalAttendancePercThisSession = attendanceSummary.getTotalAttendancePercentage();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalAttendancePercThisSession;
    }

    private double getSchoolAttendanceForThisMonth(int schoolid, int academicSessionId) {
        double totalAttendancePercThisMonth = 0;
        AttendanceSummary attendanceSummary = null;
        try {
            String currentDate = AppModel.getInstance().getFirstDayOfMonth("yyyy-M-dd");

            attendanceSummary = AttendancePercentage.getInstance(activity).getAttendancePercentageforThisMonth(schoolid,
                    academicSessionId, currentDate);

            totalAttendancePercThisMonth = attendanceSummary.getTotalAttendancePercentage();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalAttendancePercThisMonth;
    }


    private String getAttendencePercentage(int position) {
        String percentage = "";
        int countP = 0, countA = 0, countL = 0;
        Date date = null;

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");

        try {
            date = fmt.parse(for_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String year = sdf3.format(date);
        String month = sdf2.format(date);

        List<StudentAttendanceReportModel> attendencelist = DatabaseHelper.getInstance(activity)
                .getClassSectionAttendanceCount(SchoolID, class_sectionID,
                        month, year);

//        not using right now it will be used if absent and leave required and attendece of 1 month
        if (attendencelist != null && attendencelist.size() != 0) {
            for (int i = 0; i < attendencelist.size(); i++) {
                if (attendencelist.get(i).isAbsent()) {
                    countA++;
                } else {
                    countL++;
                }
            }
        }
//            -----------------

        int totalDaysElapsed = DatabaseHelper.getInstance(activity).
                getAllAttendanceHeaderCountforClassOrSchool(SchoolID, class_sectionID,
                        month, year);

        countP = Math.abs(totalDaysElapsed - attendencelist.size());
        percentage = countPercentages(totalWorkingDays, countP);
        return percentage;
    }

    private String countPercentages(int days, int countP) {
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


//    public String countActiveStudents() {
//        int studentCount = 0;
//        for (StudentModel sm : StudentModel.getInstance().getStudentsList()) {
////            if (sm.isActive()) {
//                studentCount++;
////            }
//        }
//        return " " + studentCount;
//    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class_sec, tvMonthYear, tvAttendance;
        LinearLayout llSchoolResult;

        public StudentViewHolder(View itemView) {
            super(itemView);

            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            llSchoolResult = (LinearLayout) itemView.findViewById(R.id.llSchoolResult);
            tvMonthYear = (TextView) itemView.findViewById(R.id.tvMonthYear);
            tvAttendance = (TextView) itemView.findViewById(R.id.tvAttendance);
        }
    }
}
