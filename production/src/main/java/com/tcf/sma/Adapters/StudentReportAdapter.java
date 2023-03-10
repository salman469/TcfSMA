package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.AccountStatement.AccountStatementActivity;
import com.tcf.sma.Activities.StudentProfileActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.StudentAttendanceReportDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentAttendanceReportModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mohammad.Haseeb on 2/2/2017.
 */

public class StudentReportAdapter extends RecyclerView.Adapter<StudentReportAdapter.StudentViewHolder> {
    ArrayList<StudentModel> smList;
    Activity activity;
    int flag;
    int SectionId, ClassID;
    private List<String> prevResvList = new ArrayList<>();
    private int totalWorkingDays;
    private int schoolId;

    public StudentReportAdapter(ArrayList<StudentModel> smList, Activity activity, int flag, int ClassID, int SectionId) {
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
    }

    public StudentReportAdapter(ArrayList<StudentModel> smList, Activity activity, int flag, int ClassID, int SectionId, int totalWorkingDays) {
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
        this.totalWorkingDays = totalWorkingDays;
    }

    public StudentReportAdapter(int schoolId, ArrayList<StudentModel> smList, Activity activity, int flag, int ClassID, int SectionId, int totalWorkingDays) {
        this.schoolId = schoolId;
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
        this.totalWorkingDays = totalWorkingDays;
    }

    @Override
    public StudentReportAdapter.StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.student_report_view_list_cell, parent, false);
        return new StudentReportAdapter.StudentViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(StudentReportAdapter.StudentViewHolder holder, int position) {

        // background color change in even odd sequence
        if (position % 2 == 0) {
            if (!smList.get(position).isActive()) {
                holder.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
            } else {
                holder.stCardView.setBackgroundResource(R.color.light_app_green);
            }

        } else {
            if (!smList.get(position).isActive()) {
                holder.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
            } else {
                holder.stCardView.setBackgroundResource(R.color.white);
            }
        }

        String currentClass = StudentModel.getInstance().getStudentsList().get(position).getCurrentClass();
        if (currentClass.toLowerCase().contains("class")) {
            holder.tv_class_sec.setText(currentClass.split("\\-")[1] + "-" + StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        } else {
            holder.tv_class_sec.setText(currentClass);
        }
        holder.tv_grNo.setText(smList.get(position).getGrNo());
        holder.tv_student_name.setText(smList.get(position).getName());
//        if (smList.get(position).isActive()) {
//            holder.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_app_green));
//        } else {
//            holder.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
//        }

//        prevResvList=AppInvoice.getInstance(activity).getPrevRecv(String.valueOf(SurveyAppModel.getInstance().getSelectedSchool(activity)),
//                smList.get(position).getGrNo());

//        prevResvList= FeesCollection.getInstance(activity).getPrevReceivable(schoolId+"",
//                smList.get(position).getGrNo());

        //For Total Dues
        int totalDues = 0;
        for (int i = 0; i < prevResvList.size(); i++) {
            if (prevResvList.get(i) != null && prevResvList.get(i) != "")
                totalDues += Integer.valueOf(prevResvList.get(i));
            else
                totalDues += 0;
        }
        holder.tv_total_dues.setText(String.valueOf(totalDues));

        //For Monthly Fee
        if (prevResvList.get(2) != null && prevResvList.get(2) != "")
            holder.tv_monthly_fee.setText(prevResvList.get(2));
        else
            holder.tv_monthly_fee.setText("0");

        //For Todays Attendence
        String presentCount = getAttendencePercentage(position);
        holder.tv_attendance.setText(presentCount);

        //        holder.tv_fee_category.setText();

    }

    private String getAttendencePercentage(int position) {
        String percentage = "";
        int countP = 0, countA = 0, countL = 0;
        //getting currentmonth and year


        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();

        String currYear = sdf3.format(calendar.getTime());
        String currMonth = sdf2.format(calendar.getTime());

//        List<StudentAttendanceReportModel> attendencelist=DatabaseHelper.getInstance(activity)
//                .getStudentOneYearAttendanceCount(smList.get(position).getId(),SurveyAppModel.getInstance().getSelectedSchool(activity),
//                        currYear);

        List<StudentAttendanceReportModel> attendencelist = DatabaseHelper.getInstance(activity)
                .getStudentOneYearAttendanceCount(smList.get(position).getId(), schoolId,
                        currYear);

        //not using right now it will be used if absent and leave required
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
                getAllAttendanceHeaderCountYearforClass(smList.get(position).getSchoolClassId(),
                        currYear);

        countP = Math.abs(totalDaysElapsed - attendencelist.size());
        percentage = countPercentages(totalWorkingDays, countP);
        return percentage;
    }

    private String getTodayAttendencePercent(int position) {
        String percentage = "";
        String attendanceTotal30Days = "0", attendanceTaken30Days = "0";
        attendanceTotal30Days = DatabaseHelper.getInstance(activity).
                getAttendanceTotalCountFor30Days(String.valueOf(AppModel.getInstance().getuserSchoolIDS(activity)));

        //getting currentmonth and year
        int countA = 0, countL = 0, countP = 0;

        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();

        String currYear = sdf3.format(calendar.getTime());
        String currMonth = sdf2.format(calendar.getTime());

        List<StudentAttendanceReportModel> attendencelist = DatabaseHelper.getInstance(activity)
                .getStudentAttendanceCount(smList.get(position).getId(), AppModel.getInstance().getSelectedSchool(activity),
                        currMonth, currYear);

        if (attendencelist != null && attendencelist.size() != 0) {
            for (int i = 0; i < attendencelist.size(); i++) {
                if (attendencelist.get(i).isAbsent()) {
                    countA++;
                } else {
                    countL++;
                }
            }
        }


//        attendanceTotal30Days = DatabaseHelper.getInstance(activity).
//                getAttendanceTotalCountFor30Days(String.valueOf(SurveyAppModel.getInstance().getuserSchoolIDS(activity)));
//        countP = Math.abs(Integer.parseInt(attendanceTotal30Days) - attendencelist.size());
//        attendanceTaken30Days = DatabaseHelper.getInstance(activity).getAttendanceTakenCountFor30Days(String.valueOf(SurveyAppModel.getInstance().getuserSchoolIDS(activity)));
////        int totalDaysElapsed = DatabaseHelper.getInstance(activity).
////                getAllAttendanceHeaderCountmonthforClass(smList.get(position).getSchoolClassId(), currMonth, currYear);
////        countP = Math.abs(totalDaysElapsed - attendencelist.size());
//
//        percentage=countPercentages(countA + countP + countL,countP);

        int totalDaysElapsed = DatabaseHelper.getInstance(activity).
                getAllAttendanceHeaderCountmonthforClass(smList.get(position).getSchoolClassId(), currMonth, currYear);
        countP = Math.abs(totalDaysElapsed - attendencelist.size());


        percentage = countPercentages(Integer.parseInt(attendanceTotal30Days), countP);

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

    @Override
    public int getItemCount() {
        if (smList != null) {
            return smList.size();
        }
        return 0;
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
        TextView tv_class_sec, tv_grNo, tv_student_name, tv_fee_category, tv_monthly_fee, tv_total_dues, tv_attendance;
        CardView stCardView;

        public StudentViewHolder(View itemView) {
            super(itemView);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_student_name = (TextView) itemView.findViewById(R.id.tv_name_text);
            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            tv_fee_category = (TextView) itemView.findViewById(R.id.tv_fee_category);
            tv_monthly_fee = (TextView) itemView.findViewById(R.id.tv_monthly_fee);
            tv_total_dues = (TextView) itemView.findViewById(R.id.tv_total_dues);
            tv_attendance = (TextView) itemView.findViewById(R.id.tv_attendance);
            stCardView = (CardView) itemView.findViewById(R.id.st_card_view);

            tv_student_name.setTextColor(ContextCompat.getColor(activity, R.color.blue_color));
            tv_student_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(activity, "Name:"+tv_student_name.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, StudentProfileActivity.class);
                    intent.putExtra("StudentProfileIndex", getAdapterPosition());
                    intent.putExtra("StudentGrNo", Integer.valueOf(smList.get(getAdapterPosition()).getGrNo()));
                    intent.putExtra("classId", ClassID);
                    intent.putExtra("sectionId", SectionId);
                    (activity).startActivity(intent);
                }
            });

            tv_total_dues.setTextColor(ContextCompat.getColor(activity, R.color.blue_color));
            tv_total_dues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(activity, "Dues:"+tv_total_dues.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, AccountStatementActivity.class);
//                    intent.putExtra("StudentProfileIndex", getAdapterPosition());
                    intent.putExtra("StudentGrNo", smList.get(getAdapterPosition()).getGrNo());
//                    intent.putExtra("classId", ClassID);
//                    intent.putExtra("sectionId", SectionId);
                    (activity).startActivity(intent);
                }
            });

            tv_attendance.setTextColor(ContextCompat.getColor(activity, R.color.blue_color));
            tv_attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    new AttendaceReportDialogManager(activity, smList.get(getAdapterPosition()).getId(),
//                            smList.get(getAdapterPosition()).getSchoolClassId()).show();

                    new StudentAttendanceReportDialogManager(activity,
                            smList, totalWorkingDays).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (flag == 0) { // if flag is 0 then Student Profile else Student Dropout
//                        Intent intent = new Intent(activity, StudentProfileActivity.class);
//                        intent.putExtra("StudentProfileIndex", getAdapterPosition());
//                        intent.putExtra("StudentGrNo", Integer.valueOf(smList.get(getAdapterPosition()).getGrNo()));
//                        intent.putExtra("classId", ClassID);
//                        intent.putExtra("sectionId", SectionId);
//                        (activity).startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(activity, StudentDropoutActivity.class);
//                        intent.putExtra("StudentDropoutIndex", getAdapterPosition());
//                        (activity).startActivity(intent);
//                    }

                }
            });
        }
    }
}
