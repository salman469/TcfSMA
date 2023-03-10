package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentAttendanceReportModel;
import com.tcf.sma.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Mohammad.Haseeb on 2/1/2017.
 */
public class AttendaceReportDialogManager extends Dialog {
    int SchoolClassId;
    int id;
    Activity activity;
    PieChart mPieChart;
    private TextView presentCount, leaveCount, absentCount, presentPercent, leavePercent, absentPercent, totalDaysCount, monthName;
    private int countA = 0, countL = 0, countP = 0, totalDays;
    private StudentAttendanceReportModel model = new StudentAttendanceReportModel();
    private ArrayList<StudentAttendanceReportModel> list = new ArrayList<>();
    private String month, year, dayOfmonth;
    private double dabsentPercent = 0, dleavePercent = 0, dpresentPercent = 0;
    public AttendaceReportDialogManager(Activity activity, int id, int SchooClassID) {
        super(activity);
        this.activity = activity;
        this.id = id;
        this.SchoolClassId = SchooClassID;
    }

    public ArrayList<StudentAttendanceReportModel> getList() {
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_attendance_report);
        init();
        populateList();
        //   DatabaseHelper.getInstance(activity).getStudentAttendanceCount(id,3,,);
    }

    private void populateList() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfDay = new SimpleDateFormat("dd");
        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        dayOfmonth = dfDay.format(Calendar.getInstance().getTime());

        list = DatabaseHelper.getInstance(activity).getStudentAttendanceCount(id, AppModel.getInstance().getSelectedSchool(activity), month, year);
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isAbsent()) {
                    countA++;
                } else {
                    countL++;
                }
            }
        }


        int totalDaysElapsed = DatabaseHelper.getInstance(activity).getAllAttendanceHeaderCountmonthforClass(SchoolClassId, month, year);
        countP = Math.abs(totalDaysElapsed - list.size());
//        countP = totalDaysElapsed - list.size();
        absentCount.setText("Absent's: " + countA);
        leaveCount.setText("Leave's: " + countL);
        presentCount.setText("Present's: " + countP);
        totalDaysCount.setText("" + (countA + countP + countL));
        monthName.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        countPercentages(countA + countP + countL);
    }

    void init() {
        presentCount = (TextView) findViewById(R.id.tv_present_count);
        presentPercent = (TextView) findViewById(R.id.tv_prssent_percent);
        leaveCount = (TextView) findViewById(R.id.tv_leave_count);
        leavePercent = (TextView) findViewById(R.id.tv_leave_percent);
        absentCount = (TextView) findViewById(R.id.tv_absent_count);
        absentPercent = (TextView) findViewById(R.id.tv_absent_percent);
        totalDaysCount = (TextView) findViewById(R.id.tv_total_days_count);
        monthName = (TextView) findViewById(R.id.tv_month_name);

    }

    public void countPercentages(int days) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        dpresentPercent = ((countP / (double) days) * 100);
        dabsentPercent = ((countA / (double) days) * 100);
        dleavePercent = ((countL / (double) days) * 100);
        presentPercent.setText((df.format(dpresentPercent) + "%").equals("NaN%") ? "0%" : df.format(dpresentPercent) + "%");
        absentPercent.setText((df.format(dabsentPercent) + "%").equals("NaN%") ? "0%" : df.format(dabsentPercent) + "%");
        leavePercent.setText((df.format(dleavePercent) + "%").equals("NaN%") ? "0%" : df.format(dleavePercent) + "%");

        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.addPieSlice(new PieModel("Absent", (float) dabsentPercent, activity.getResources().getColor(R.color.red_color)));
        mPieChart.addPieSlice(new PieModel("Present", (float) dpresentPercent, activity.getResources().getColor(R.color.app_green)));
        mPieChart.addPieSlice(new PieModel("Leave", (float) dleavePercent, activity.getResources().getColor(R.color.blue_color)));
        mPieChart.startAnimation();
    }

    public int daysCount(int iMonth, int iDay, int iYear) {
        int days = 0;
        Calendar c = Calendar.getInstance();
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);
        int daysInMonth = mycal.getMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            c.set(Calendar.MONTH, iMonth);
            c.set(Calendar.DAY_OF_MONTH, i);
            c.set(Calendar.YEAR, iYear);
            Log.d("Day: Date " + i, c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                days++;
            }
        }
        return days;
    }
}
