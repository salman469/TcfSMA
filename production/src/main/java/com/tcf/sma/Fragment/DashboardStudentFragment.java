package com.tcf.sma.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.tcf.sma.Activities.FeesCollection.AttendancePendingActivity;
import com.tcf.sma.Activities.NewAdmissionActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Activities.WeakestAttendanceActivity;
import com.tcf.sma.Adapters.BottomFiveCardAdapter;
import com.tcf.sma.Adapters.FeesCollection.SessionInfoTableAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarSchool;
import com.tcf.sma.Helpers.DbTables.FeesCollection.SessionInfo;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.Fees_Collection.AttendanceSummary;
import com.tcf.sma.Models.Fees_Collection.SessionInfoModel;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;
import com.tcf.sma.Models.PendingAttendanceModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.ViewSSR.ViewSSRTableModel;
import com.tcf.sma.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DashboardStudentFragment extends Fragment implements View.OnClickListener {

    private static DatabaseHelper sDbHelper;
    ArrayList<BottomFiveStudentsModel> bfList = new ArrayList<>();
    BottomFiveCardAdapter bottomFiveAdapter;
    private TextView CVssr_tvAttendancePercentage;
    private TextView tv_todayAttendance;
    private TextView tv_thisMonthAttendance;
    private TextView tv_thisSessionAttendance;
    private TextView CVAttendanceTaken_tvTodayAttendanceCount;
    private TextView CVAttendanceTaken_tv30DaysAttendanceCount;
    private TextView CVAttendanceTaken_tvtitle;
    private TextView CVSyncStatus_tvtitle;
    private TextView tv_lastthirtydays;
    private TextView tv_today;
    private TextView CVssr_tvStudentCount;
    private TextView CVssr_tvOperationsUtilization;
    private TextView CVssr_tvBoys;
    private TextView CVssr_tvGirls;
    private TextView CVssr_btnNewEnrollment;
    private TextView CVssr_tvtitle;
    private TextView tv_maxCapacity;
    private TextView maleRatio;
    private TextView femaleRatio;
    private TextView tv_noData, tv_avgSetFee, tv_difference, tv_FeeTarget;
    private RelativeLayout rl_accessiblity;
    private LinearLayout ll_attendance_taken, llAttendanceTaken_Count, llAttendanceTaken_Last30DaysCount
            ,ll1_accessibility , ll2_accessibility , ll3_accessibility,ll_content;
    private View view;
    private RecyclerView rv_schoolSSR, rv_bottom5;
    private String year, month, dayOfmonth;
    private int StudentCount = 0;
    private int schoolId = 0;
    private int roleID = 0;
    private List<HolidayCalendarSchoolModel> holidays;
    private List<SchoolModel> schoolModels;
    private boolean isTodayisHoliday = false;
    private ViewSSRTableModel mViewSSRTableModel;
    private List<SessionInfoModel> sessionInfoModels = new ArrayList<>();

    private CardView cv_fb;

    private static Date formatStringToDate(String strdate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return format.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_dashboard, container, false);

        init(view);
        working(view);
        setFederalBoardStatus(DatabaseHelper.getInstance(getContext()).isRegionIsInFlagShipSchool(schoolId));


        return view;
    }

    private void setFederalBoardStatus(boolean regionIsInFlagShipSchool) {
        cv_fb = view.findViewById(R.id.fb_cv);
        if(regionIsInFlagShipSchool) {
            cv_fb.setVisibility(View.VISIBLE);

            TextView approved = view.findViewById(R.id.approved_tvCount);
            approved.setText(DatabaseHelper.getInstance(getContext()).getStudentReviewStatusCount("A", schoolId));
            TextView availableForApproval = view.findViewById(R.id.available_for_approval_tvCount);
            availableForApproval.setText(DatabaseHelper.getInstance(getContext()).getStudentReviewStatusCount("P", schoolId));
            TextView notApproved = view.findViewById(R.id.not_pproved_tvCount);
            notApproved.setText(DatabaseHelper.getInstance(getContext()).getStudentReviewStatusCount("F", schoolId));
            TextView tbv = view.findViewById(R.id.to_be_verified_tvCount);
            tbv.setText(DatabaseHelper.getInstance(getContext()).getStudentReviewStatusCount("T", schoolId));
        } else {
            cv_fb.setVisibility(View.GONE);
        }
    }

    private void working(View view) {
        schoolId = ((NewDashboardActivity) requireActivity()).schoolId;
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(getContext());
        if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(new SchoolModel(0, "All"));

        if (schoolId > 0) {
            StudentCount = DatabaseHelper.getInstance(view.getContext()).getAllStudentsCount(String.valueOf(schoolId));
            holidays = HolidayCalendarSchool.getInstance(view.getContext()).getAllHolidays(String.valueOf(schoolId));
        }
//            StudentCount = DatabaseHelper.getInstance(this).getAllStudentsCount(SurveyAppModel.getInstance().getSelectedSchool(this));
        else {
            StudentCount = DatabaseHelper.getInstance(view.getContext()).getAllStudentsCount(AppModel.getInstance().getuserSchoolIDS(view.getContext()));
//                StudentCount = DatabaseHelper.getInstance(this).getAllStudentsCount(schoolId);
            holidays = HolidayCalendarSchool.getInstance(view.getContext()).getAllHolidays(String.valueOf(AppModel.getInstance().getuserSchoolIDS(view.getContext())));
//                holidays = HolidayCalendarSchool.getInstance(this).getAllHolidays(String.valueOf(schoolId));
        }

        try {
            populateSchoolSSRRecycler(schoolId);
            populateAverageSetFees();
            populateSSR();
//HOLIDAY CHECK has been removed as per the new requirements by TCF (Haris, Sprint_122), so directly
//            populateAttendanceTaken(); is called


            /*if (!todayHoliday()) {
                populateAttendanceTaken();
            }
            else {
                tv_today.setTextColor(ContextCompat.getColor(getActivity(), R.color.grayheadingsdb));
                CVAttendanceTaken_tvTodayAttendanceCount.setTextColor(getResources().getColor(R.color.white));
                CVAttendanceTaken_tvTodayAttendanceCount.setText("0/0");

                CVAttendanceTaken_tv30DaysAttendanceCount.setTextColor(getResources().getColor(R.color.white));
                tv_lastthirtydays.setTextColor(ContextCompat.getColor(getActivity(), R.color.grayheadingsdb));
                CVAttendanceTaken_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
                ll_attendance_taken.setBackgroundColor(Color.WHITE);
                CVAttendanceTaken_tv30DaysAttendanceCount.setText("0/0");
            }
*/
            populateAttendanceTaken();
            populateAttendance();
            setSchoolBottomFiveCount();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateAverageSetFees() {
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,0.7f);
        if (DatabaseHelper.getInstance(view.getContext()).isRegionIsInFlagShipSchool(schoolId)) {
            ll3_accessibility.setVisibility(View.VISIBLE);
            ll_content.setWeightSum(1.7f);
            ll1_accessibility.setLayoutParams(params2);
            ll2_accessibility.setLayoutParams(params2);
            ll3_accessibility.setLayoutParams(params3);
            ArrayList<StudentModel>studentList = DatabaseHelper.getInstance(view.getContext()).getAllStudentsInSchoolUsing(schoolId, 0, 0, "", "", 1, AppConstants.All, false);
            if (!CollectionUtils.isEmpty(studentList)  && studentList.size() > 0){
                double difference = 0, average = 0;
                SchoolModel sm = DatabaseHelper.getInstance(view.getContext()).getSchoolById(schoolId);

                average = (studentList.stream().mapToDouble(s -> s.getActualFees()).sum()) / studentList.size();
                tv_avgSetFee.setText(String.valueOf((int) AppModel.getInstance().round(average, 0)));

                if (sm.getTarget_Amount() != null && !sm.getTarget_Amount().isEmpty()) {
                    difference = Double.parseDouble(sm.getTarget_Amount()) - average;
                    tv_FeeTarget.setText(sm.getTarget_Amount());
                } else {
                    difference = 0 - average;
                    tv_FeeTarget.setText("0");
                }

//                tv_difference.setText(String.valueOf((int) AppModel.getInstance().round(difference, 0)));
            }
        } else {
            ll3_accessibility.setVisibility(View.GONE);
            ll_content.setWeightSum(2.0f);
            ll1_accessibility.setLayoutParams(params1);
            ll2_accessibility.setLayoutParams(params1);
            tv_FeeTarget.setText("0");
            tv_avgSetFee.setText("0");
            tv_difference.setText("0");
        }
    }

    private void populateAttendance() throws Exception {
        setSchoolAttendancePercent();
        setSchoolAttendanceForThisMonth();
        setSchoolAttendancePercentForThisSession();
    }

    private void init(View view) {
        sDbHelper = DatabaseHelper.getInstance(view.getContext());

        /*Attendance*/
        CVssr_tvAttendancePercentage = (TextView) view.findViewById(R.id.CVssr_tvAttendancePercentage);
        tv_todayAttendance = (TextView) view.findViewById(R.id.tv_todayAttendance);
        tv_thisMonthAttendance = (TextView) view.findViewById(R.id.tv_thisMonthAttendance);
        tv_thisSessionAttendance = (TextView) view.findViewById(R.id.tv_thisSessionAttendance);

        /*Attendance Taken*/
        CVAttendanceTaken_tvTodayAttendanceCount = (TextView) view.findViewById(R.id.CVAttendanceTaken_tvTodayAttendanceCount);
        CVAttendanceTaken_tv30DaysAttendanceCount = (TextView) view.findViewById(R.id.CVAttendanceTaken_tv30DaysAttendanceCount);

        llAttendanceTaken_Count = (LinearLayout) view.findViewById(R.id.llAttendanceTaken_Count);
        llAttendanceTaken_Last30DaysCount = (LinearLayout) view.findViewById(R.id.llAttendanceTaken_Last30DaysCount);
        llAttendanceTaken_Count.setOnClickListener(this);
        llAttendanceTaken_Last30DaysCount.setOnClickListener(this);

        tv_lastthirtydays = (TextView) view.findViewById(R.id.tv_lastthirtydays);
        CVAttendanceTaken_tvtitle = (TextView) view.findViewById(R.id.CVAttendanceTaken_tvtitle);
        CVSyncStatus_tvtitle = (TextView) view.findViewById(R.id.CVSyncStatus_tvtitle);
        ll_attendance_taken = (LinearLayout) view.findViewById(R.id.ll_attendance_taken);
        tv_today = (TextView) view.findViewById(R.id.tv_today);

        /*----------------SSR----------------*/
        CVssr_tvStudentCount = (TextView) view.findViewById(R.id.CVssr_tvStudentCount);
        CVssr_tvOperationsUtilization = (TextView) view.findViewById(R.id.CVssr_tvOperationsUtilization);
        tv_maxCapacity = (TextView) view.findViewById(R.id.tv_maxCapacity);
        CVssr_tvBoys = (TextView) view.findViewById(R.id.CVssr_tvBoys);
        CVssr_tvGirls = (TextView) view.findViewById(R.id.CVssr_tvGirls);
        CVssr_btnNewEnrollment = (TextView) view.findViewById(R.id.CVssr_btnNewEnrollment);
        rv_schoolSSR = (RecyclerView) view.findViewById(R.id.rv_sessionInfo);
        CVssr_btnNewEnrollment.setOnClickListener(this);
        CVssr_tvtitle = (TextView) view.findViewById(R.id.CVssr_tvtitle);
        maleRatio = (TextView) view.findViewById(R.id.maleRatio);
        femaleRatio = (TextView) view.findViewById(R.id.femaleRatio);
        rl_accessiblity = (RelativeLayout) view.findViewById(R.id.rl_accessiblity);
        tv_noData = (TextView) view.findViewById(R.id.tv_noData);

        /*----------------Average Set Fees----------------*/

        ll1_accessibility = view.findViewById(R.id.ll1_accessibility);
        ll2_accessibility = view.findViewById(R.id.ll2_accessibility);
        ll3_accessibility = view.findViewById(R.id.ll3_accessibility);
        ll_content = view.findViewById(R.id.ll_content);
        tv_FeeTarget = view.findViewById(R.id.tv_FeeTarget);
        tv_avgSetFee = view.findViewById(R.id.tv_avgSetFee);
        tv_difference = view.findViewById(R.id.tv_difference);

        /*----------------Bottom five attendance----------------*/

        TextView tvWeakestAttendanceSeeAll = (TextView) view.findViewById(R.id.tvWeakestAttendanceSeeAll);
        rv_bottom5 = (RecyclerView) view.findViewById(R.id.rv_bottom_five);
        rv_bottom5.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_bottom5.setLayoutManager(llm);
        tvWeakestAttendanceSeeAll.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(view.getContext()).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            CVssr_btnNewEnrollment.setBackgroundColor(Color.GRAY);
            CVssr_btnNewEnrollment.setEnabled(false);
        }
    }

    private void setSchoolAttendancePercent() {
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfDay = new SimpleDateFormat("dd");
        month = dfMonth.format(Calendar.getInstance().getTime());
        year = dfYear.format(Calendar.getInstance().getTime());
        dayOfmonth = dfDay.format(Calendar.getInstance().getTime());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        try {
            if (schoolId == 0) {
                if (DatabaseHelper.getInstance(view.getContext()).AttendanceExists(AppModel.getInstance().getSelectedSchool(view.getContext()), year + "-" + month + "-" + dayOfmonth)) {
                    int Totalabsents = DatabaseHelper.getInstance(view.getContext()).getStudentAttendancebyMonth(AppModel.getInstance().getSelectedSchool(view.getContext()), month, year, dayOfmonth);
                    int TotalPresents = StudentCount - Totalabsents;
                    double MonthPercent = ((double) TotalPresents / (double) StudentCount) * 100;
                    CVssr_tvAttendancePercentage.setText((df.format(MonthPercent) + "%").equals("NaN%") ? "0%" : df.format(MonthPercent) + "%");
                    tv_todayAttendance.setText((df.format(MonthPercent) + "%").equals("NaN%") ? "0%" : df.format(MonthPercent) + "%");
                } else {
                    CVssr_tvAttendancePercentage.setText("0%");
                    tv_todayAttendance.setText("0%");
                }
            } else {
//            if (DatabaseHelper.getInstance(this).AttendanceExists(SurveyAppModel.getInstance().getSelectedSchool(this), year + "-" + month + "-" + dayOfmonth)) {
                if (DatabaseHelper.getInstance(view.getContext()).AttendanceExists(schoolId, year + "-" + month + "-" + dayOfmonth)) {
//                int Totalabsents = DatabaseHelper.getInstance(this).getStudentAttendancebyMonth(SurveyAppModel.getInstance().getSelectedSchool(this), month, year, dayOfmonth);
                    int Totalabsents = DatabaseHelper.getInstance(view.getContext()).getStudentAttendancebyMonth(schoolId, month, year, dayOfmonth);
                    int TotalPresents = StudentCount - Totalabsents;
                    double MonthPercent = ((double) TotalPresents / (double) StudentCount) * 100;
                    CVssr_tvAttendancePercentage.setText((df.format(MonthPercent) + "%").equals("NaN%") ? "0%" : df.format(MonthPercent) + "%");
                    tv_todayAttendance.setText((df.format(MonthPercent) + "%").equals("NaN%") ? "0%" : df.format(MonthPercent) + "%");
                } else {
                    CVssr_tvAttendancePercentage.setText("0%");
                    tv_todayAttendance.setText("0%");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSchoolAttendanceForThisMonth() {
        double totalAttendancePerc = 0;
        AttendanceSummary attendanceSummary = null;
        try {
//            String currentDate = SurveyAppModel.getInstance().convertDatetoFormat(SurveyAppModel.getInstance().getDate(),
//                    "yyyy-MM-dd", "yyyy-M");
//            currentDate += "-01"; //day always 01
            String currentDate = AppModel.getInstance().getFirstDayOfMonth("yyyy-M-dd");

            if (schoolId > 0) {
                for (SchoolModel model : schoolModels) {
                    if (model.getId() == schoolId) {
                        attendanceSummary = AttendancePercentage.getInstance(view.getContext()).getAttendancePercentageforThisMonth(model.getId(),
                                model.getAcademic_Session_Id(), currentDate);
                        break;
                    }
                }

                try {
                    totalAttendancePerc = attendanceSummary.getTotalAttendancePercentage();
                } catch (Exception e) {
                    e.printStackTrace();
                    totalAttendancePerc = 0;
                }

                if (attendanceSummary == null) {
                    tv_thisMonthAttendance.setText("--");
                } else {
                    tv_thisMonthAttendance.setText(totalAttendancePerc + "%");
                }
            } else {
                totalAttendancePerc = 0;

                for (SchoolModel model : schoolModels) {
                    attendanceSummary = AttendancePercentage.getInstance(view.getContext()).getAttendancePercentageforThisMonth(model.getId(),
                            model.getAcademic_Session_Id(), currentDate);
                    totalAttendancePerc += attendanceSummary.getTotalAttendancePercentage();
                }

                if (attendanceSummary == null) {
                    tv_thisMonthAttendance.setText("--");
                } else {
                    tv_thisMonthAttendance.setText(totalAttendancePerc + "%");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setSchoolAttendancePercentForThisSession() {
        double totalAttendancePerc = 0;
        AttendanceSummary attendanceSummary = null;
        try {
            if (schoolId > 0) {
                for (SchoolModel model : schoolModels) {
                    if (model.getId() == schoolId) {
                        attendanceSummary = AttendancePercentage.getInstance(view.getContext()).getAttendancePercentageforThisSession(model.getId(),
                                model.getAcademic_Session_Id());
                        break;
                    }
                }

                totalAttendancePerc = attendanceSummary.getTotalAttendancePercentage();

                if (attendanceSummary == null) {
                    tv_thisSessionAttendance.setText("--");
                } else {
                    tv_thisSessionAttendance.setText(totalAttendancePerc + "%");
                }
            } else {
                totalAttendancePerc = 0;

                for (SchoolModel model : schoolModels) {
                    attendanceSummary = AttendancePercentage.getInstance(view.getContext()).getAttendancePercentageforThisSession(model.getId(),
                            model.getAcademic_Session_Id());
                    totalAttendancePerc += attendanceSummary.getTotalAttendancePercentage();
                }

                if (attendanceSummary == null) {
                    tv_thisSessionAttendance.setText("--");
                } else {
                    tv_thisSessionAttendance.setText(totalAttendancePerc + "%");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (schoolId == 0) {
//            tv_thisSessionAttendance.setText("0%");
//            return;
//        }
//        SchoolModel sm = DatabaseHelper.getInstance(this).getSchoolById(schoolId);
//        if (sm != null) {
//            CalendarsModel cm = DatabaseHelper.getInstance(this).getTotalDaysAndOffDaysFromSession(schoolId,
//                    sm.getAcademic_Session_Id());
//        try {
////                if (cm != null) {
////                    int totalDays = cm.getTotalDays();
////                    int totalOffDays = cm.getTotalOffDays();
////                    int totalWorkingDays = totalDays - totalOffDays;
////                    int totalSessionAttendanceMarkedForAbsent = DatabaseHelper.getInstance(this).getAttendanceForSession(schoolId + "",
////                            sm.getStart_date(), sm.getEnd_date());
////                    int TotalPresents = StudentCount - totalSessionAttendanceMarkedForAbsent;
////
////                    int denom = totalWorkingDays * StudentCount;
//
//            String start_date = "";
//            String end_date = "";
//            double sessionPercentage = 0;
//
//            if (schoolId > 0) {
//                for (SchoolModel model : schoolModels) {
//                    if (model.getId() == schoolId) {
//                        start_date = model.getStart_date();
//                        end_date = model.getEnd_date();
//                    }
//                }
//                sessionPercentage = 100 - DatabaseHelper.getInstance(this).getAbsentPercentageForThisSession(schoolId, start_date, end_date);
//
//            } else {
//                double absentPer = 0;
//                for (SchoolModel model : schoolModels) {
//                    start_date = model.getStart_date();
//                    end_date = model.getEnd_date();
//                    if (model.getId() != 0)
//                        absentPer += DatabaseHelper.getInstance(this).getAbsentPercentageForThisSession(model.getId(), start_date, end_date);
//                }
//
//                sessionPercentage = 100 - (absentPer / 2);
//            }
//
//
//            DecimalFormat df = new DecimalFormat();
//            df.setMaximumFractionDigits(2);
//
//            tv_thisSessionAttendance.setText((df.format(sessionPercentage) + "%").equals("NaN%") ? "0%" : df.format(sessionPercentage) + "%");
//        } catch (
//                Exception e) {
//            e.printStackTrace();
//        }
    }

    private boolean todayHoliday() {
        boolean isHoliday = false;

        String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd");
        if (schoolId > 0) {
            CalendarsModel cm = DatabaseHelper.getInstance(view.getContext()).checkDateForAttendance(
                    currentDate, schoolId + "");

            if (cm != null) {
                isHoliday = true;
            } else {
                isHoliday = false;
            }
        } else {
        }

        return isHoliday;
    }

    private void populateAttendanceTaken() throws Exception {
        String attendanceTakenToday = "0", attendanceTotalToday = "0";
        String attendanceTaken30Days = "0", attendanceTotal30Days = "0";
        List<PendingAttendanceModel> pendingAttendanceModels, pendingAttendanceModelsForLast30Days;
        if (schoolId > 0) {
            attendanceTakenToday = DatabaseHelper.getInstance(view.getContext()).getAttendanceTakenCountForToday(String.valueOf(schoolId));
            attendanceTotalToday = DatabaseHelper.getInstance(view.getContext()).getAttendanceTotalCountForToday(String.valueOf(schoolId));
            attendanceTaken30Days = DatabaseHelper.getInstance(view.getContext()).getAttendanceTakenCountFor30Days(String.valueOf(schoolId));
//            attendanceTotal30Days = DatabaseHelper.getInstance(this).getAttendanceTotalCountFor30Days(String.valueOf(schoolId));
            pendingAttendanceModels = DatabaseHelper.getInstance(view.getContext()).getPendingAttendanceForToday(String.valueOf(String.valueOf(schoolId)));
        } else {
            attendanceTakenToday = DatabaseHelper.getInstance(view.getContext()).getAttendanceTakenCountForToday(String.valueOf(AppModel.getInstance().getuserSchoolIDS(view.getContext())));
            attendanceTotalToday = DatabaseHelper.getInstance(view.getContext()).getAttendanceTotalCountForToday(String.valueOf(AppModel.getInstance().getuserSchoolIDS(view.getContext())));
            attendanceTaken30Days = DatabaseHelper.getInstance(view.getContext()).getAttendanceTakenCountFor30Days(String.valueOf(AppModel.getInstance().getuserSchoolIDS(view.getContext())));
//            attendanceTotal30Days = DatabaseHelper.getInstance(this).getAttendanceTotalCountFor30Days(String.valueOf(SurveyAppModel.getInstance().getuserSchoolIDS(this)));
            pendingAttendanceModels = DatabaseHelper.getInstance(view.getContext()).getPendingAttendanceForToday(String.valueOf(AppModel.getInstance().getuserSchoolIDS(view.getContext())));

//            attendanceTakenToday = DatabaseHelper.getInstance(this).getAttendanceTakenCountForToday(String.valueOf(schoolId));
//            attendanceTotalToday = DatabaseHelper.getInstance(this).getAttendanceTotalCountForToday(String.valueOf(schoolId));
//            attendanceTaken30Days = DatabaseHelper.getInstance(this).getAttendanceTakenCountFor30Days(String.valueOf(schoolId));
////            attendanceTotal30Days = DatabaseHelper.getInstance(this).getAttendanceTotalCountFor30Days(String.valueOf(SurveyAppModel.getInstance().getuserSchoolIDS(this)));
//            pendingAttendanceModels = DatabaseHelper.getInstance(this).getPendingAttendanceForToday(String.valueOf(schoolId));
        }
        try {


            /*For Today*/
            String formattedDate = AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(formattedDate));// all do
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            /*If selected date is not a sunday and in not a holiday than add in a list else not*/
            if (dayOfWeek != Calendar.SUNDAY && !selectedDateIsHoliday(cal))
                CVAttendanceTaken_tvTodayAttendanceCount.setText(attendanceTakenToday + "/" + attendanceTotalToday);
            else {
                CVAttendanceTaken_tvTodayAttendanceCount.setText("0/0");
                isTodayisHoliday = true;
            }

            try {
                if (Integer.parseInt(attendanceTakenToday) < Integer.parseInt(attendanceTotalToday)) {
                    tv_today.setTextColor(getResources().getColor(R.color.light_red));
                    CVAttendanceTaken_tvTodayAttendanceCount.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tv_today.setTextColor(ContextCompat.getColor(getActivity(), R.color.grayheadingsdb));
                    CVAttendanceTaken_tvTodayAttendanceCount.setTextColor(getResources().getColor(R.color.white));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*For Today*/

            /*For last 30 Days*/

//            List<AttendanceLast30DaysCountModel> countModelList = DatabaseHelper.getInstance(this).getAttendanceTakenCountForLast30Days();
//            pendingAttendanceModelsForLast30Days = new ArrayList<>();
//            int totalcount = 0;
//            for (AttendanceLast30DaysCountModel model : countModelList) {
//                String formatDate = SurveyAppModel.getInstance().convertDatetoFormat(model.getDate(), "yyyy-MM-dd", "dd-MM-yyyy");
//                Calendar cal1 = Calendar.getInstance();
////                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//                try {
//                    cal1.setTime(sdf.parse(formatDate));// all do
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                int day = cal1.get(Calendar.DAY_OF_WEEK);
//                /*If selected date is not a sunday and in not a holiday than add in a list else not*/
//                if (day != Calendar.SUNDAY && !selectedDateIsHoliday(cal1)) {
//                    for (PendingAttendanceModel model1 : pendingAttendanceModels) {
//                        totalcount++;
//
//                    }
//                }
//            }

            CalendarsModel calendarsModel = null;
            int totalcount = 0;
            if (schoolId != 0) {
                calendarsModel = DatabaseHelper.getInstance(view.getContext()).getLast30WorkingDays(schoolId);
                totalcount = (calendarsModel.getTotalDays() < 0 ? calendarsModel.getTotalDays() * -1 : calendarsModel.getTotalDays()) - calendarsModel.getTotalOffDays();
                totalcount = totalcount * (attendanceTotalToday.isEmpty() == false ? Integer.valueOf(attendanceTotalToday) : 0);
            } else {
                List<SchoolModel> sm = DatabaseHelper.getInstance(view.getContext()).getAllUserSchools();
                String totalCountForToday = "";
                int sum = 0;
                for (SchoolModel mod : sm) {
                    calendarsModel = DatabaseHelper.getInstance(view.getContext()).getLast30WorkingDays(mod.getId());
                    sum = (calendarsModel.getTotalDays() < 0 ? calendarsModel.getTotalDays() * -1 : calendarsModel.getTotalDays()) - calendarsModel.getTotalOffDays();
                    totalCountForToday = DatabaseHelper.getInstance(view.getContext()).getAttendanceTotalCountForToday(String.valueOf(mod.getId()));
                    try {
                        totalcount += sum * Integer.valueOf(totalCountForToday);
                    } catch (Exception e) {
                        totalcount += 0;
                    }

                }

            }

            //For junk data if totalcount is less than 0 then then automatically put zero in attendanceTotal30Days
            if (totalcount < 0) {
                totalcount = 0;
            }

            attendanceTotal30Days = String.valueOf(totalcount);
            try {
                if (Integer.parseInt(attendanceTaken30Days) < totalcount) {
                    CVAttendanceTaken_tv30DaysAttendanceCount.setTextColor(getResources().getColor(R.color.white));
                    tv_lastthirtydays.setTextColor(getResources().getColor(R.color.light_red));
                    CVAttendanceTaken_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_red));
                    ll_attendance_taken.setBackgroundColor(getResources().getColor(R.color.light_red_color));
                } else {
                    CVAttendanceTaken_tv30DaysAttendanceCount.setTextColor(getResources().getColor(R.color.white));
                    tv_lastthirtydays.setTextColor(ContextCompat.getColor(getActivity(), R.color.grayheadingsdb));
                    CVAttendanceTaken_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
                    ll_attendance_taken.setBackgroundColor(Color.WHITE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            CVAttendanceTaken_tv30DaysAttendanceCount.setText(attendanceTaken30Days + "/" + attendanceTotal30Days);

            /*For last 30 Days*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean selectedDateIsHoliday(Calendar calendar) {

        Date d = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String selectDate = sdf.format(d);

        for (HolidayCalendarSchoolModel model : holidays) {

            Date startDate = null, endDate = null, date;
            try {

                startDate = sdf.parse(model.getStartDate());
                endDate = sdf.parse(model.getEndDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(startDate);
            cal1.add(Calendar.DATE, -1);
            cal2.setTime(endDate);
            cal2.add(Calendar.DATE, 1);

            date = formatStringToDate(selectDate);

            if (date.after(cal1.getTime()) && date.before(cal2.getTime()))
                return true;
        }
        return false;
    }

    private void populateSSR() {
        double operationUtilization = 0;
        double capacity = 0;
        if (schoolId > 0)
            capacity = DatabaseHelper.getInstance(view.getContext()).getMaxCapacityForSchool(schoolId);
        else {
            for (SchoolModel sm : schoolModels) {
                capacity += DatabaseHelper.getInstance(view.getContext()).getMaxCapacityForSchool(sm.getId());
            }
        }

//        Operations Utilization formula is (Active Students / Capacity) *100.
        mViewSSRTableModel = sDbHelper.getSSRForDashboard(schoolId);
        if (mViewSSRTableModel != null) {
            CVssr_tvStudentCount.setText("" + Double.valueOf(mViewSSRTableModel.getmOverall()).intValue());
            CVssr_tvBoys.setText("" + Double.valueOf(mViewSSRTableModel.getmMale()).intValue());
            CVssr_tvGirls.setText("" + Double.valueOf(mViewSSRTableModel.getmFemale()).intValue());

            double total = mViewSSRTableModel.getmMale() + mViewSSRTableModel.getmFemale();
            double maleRatio = (mViewSSRTableModel.getmMale() / total) * 100;
            double femaleRatio = (mViewSSRTableModel.getmFemale() / total) * 100;

            this.maleRatio.setText(Math.round(maleRatio) + "");
            this.femaleRatio.setText(Math.round(femaleRatio) + "");

        }

        if (capacity == 0)
            operationUtilization = 0;
        else
            operationUtilization = ((StudentCount / capacity) * 100);


        if (operationUtilization <= 70) {
            rl_accessiblity.setBackgroundColor(getResources().getColor(R.color.light_red_color));
            CVssr_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_red));
        } else {
            rl_accessiblity.setBackgroundColor(getResources().getColor(R.color.white));
            CVssr_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
        }

        try {
            tv_maxCapacity.setText((int) capacity + "");
            CVssr_tvOperationsUtilization.setText((int) operationUtilization + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateSchoolSSRRecycler(int schoolId) {
        try {
            sessionInfoModels = SessionInfo.getInstance(view.getContext()).getSessionInfoForDashboard(schoolId);
            rv_schoolSSR.setLayoutManager(new LinearLayoutManager(view.getContext()));
            rv_schoolSSR.setAdapter(new SessionInfoTableAdapter(getActivity(), sessionInfoModels));

            if (sessionInfoModels.size() <= 0) {
                tv_noData.setVisibility(View.VISIBLE);
                rv_schoolSSR.setVisibility(View.GONE);
            } else {
                tv_noData.setVisibility(View.GONE);
                rv_schoolSSR.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSchoolBottomFiveCount() {
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        String startDate = AppModel.getInstance().getThisMonthStartingDate();
        String endDate = AppModel.getInstance().getThisMonthEndDate();

        if (schoolId > 0)
            bfList = DatabaseHelper.getInstance(view.getContext()).getWeakestAttendanceForDashboard(String.valueOf(schoolId), startDate, endDate);
        else {
            ArrayList<BottomFiveStudentsModel> multipleSchoolsWeakestAttendanceList = new ArrayList<>();
            for (SchoolModel model : schoolModels) {
                multipleSchoolsWeakestAttendanceList.addAll(DatabaseHelper.getInstance(view.getContext()).
                        getWeakestAttendanceForDashboard(String.valueOf(model.getId()), startDate, endDate));
            }

            //Asc from 10 - 100
            try {
                Collections.sort(multipleSchoolsWeakestAttendanceList, new Comparator<BottomFiveStudentsModel>() {
                    @Override
                    public int compare(BottomFiveStudentsModel BFSModel1, BottomFiveStudentsModel BFSModel2) {
                        return BFSModel1.getStudentsAbsentCounting() - BFSModel2.getStudentsAbsentCounting();
                    }
                });

                //Getting only five records
                bfList.clear();
                for (int i = 0; i < multipleSchoolsWeakestAttendanceList.size(); i++) {
                    bfList.add(multipleSchoolsWeakestAttendanceList.get(i));
                    if (i == 4) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            bfList = DatabaseHelper.getInstance(this).getWeakestAttendanceForDashboard(SurveyAppModel.getInstance().getuserSchoolIDS(this), startDate, endDate);
//            bfList = DatabaseHelper.getInstance(this).getBottomFiveStudent(schoolId+"", month, year);
        }

        if (bfList != null && bfList.size() != 0) {
            rv_bottom5.setVisibility(View.VISIBLE);
            bottomFiveAdapter = new BottomFiveCardAdapter(bfList, view.getContext(), true, schoolId);
            rv_bottom5.setAdapter(bottomFiveAdapter);
        } else {
            rv_bottom5.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CVssr_btnNewEnrollment:
                startActivity(new Intent(view.getContext(), NewAdmissionActivity.class));
                break;
            case R.id.tvWeakestAttendanceSeeAll:
                Intent weakestAttendanceIntent = new Intent(view.getContext(), WeakestAttendanceActivity.class);
                if (schoolId > 0)
                    weakestAttendanceIntent.putExtra("schoolId", String.valueOf(schoolId));
                else {
                    weakestAttendanceIntent.putExtra("schoolId", AppModel.getInstance().getuserSchoolIDS(view.getContext()));
//                    weakestAttendanceIntent.putExtra("schoolId", schoolId);
                }
                startActivity(weakestAttendanceIntent);
                break;
            case R.id.llAttendanceTaken_Count:
                /*If selected date is not a sunday and in not a holiday than add in a list else not*/
                if (!todayHoliday()) {
                    if (!isTodayisHoliday) {
                        Intent todayIntent = new Intent(view.getContext(), AttendancePendingActivity.class);
                        todayIntent.putExtra("schoolId", schoolId);
                        todayIntent.putExtra("day", "today");
                        startActivity(todayIntent);
                    } else
                        Toast.makeText(view.getContext(), "Cannot mark attendance for weekends", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (schoolId > 0) {
                            String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd");
                            CalendarsModel cm = DatabaseHelper.getInstance(view.getContext()).checkDateForAttendance(
                                    currentDate, schoolId + "");

                            ((NewDashboardActivity) getActivity()).MessageBox("Current Date is " + cm.getC_Holiday_Type_Name() + " due to " + cm.getC_Activity_Name());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*If selected date is not a sunday and in not a holiday than add in a list else not*/
//                if (!isTodayisHoliday) {
//                    Intent todayIntent = new Intent(NewDashboardActivity.this, AttendancePendingActivity.class);
//                    todayIntent.putExtra("schoolId", schoolId);
//                    todayIntent.putExtra("day", "today");
//                    startActivity(todayIntent);
//                } else
//                    Toast.makeText(current_activity, "Cannot mark attendance for weekends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.llAttendanceTaken_Last30DaysCount:
                if (!todayHoliday()) {
                    Intent last30daysAttIntent = new Intent(view.getContext(), AttendancePendingActivity.class);
                    last30daysAttIntent.putExtra("schoolId", schoolId);
                    last30daysAttIntent.putExtra("day", "last30Days");
                    startActivity(last30daysAttIntent);
                } else {
                    try {
                        if (schoolId > 0) {
                            String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd");
                            CalendarsModel cm = DatabaseHelper.getInstance(view.getContext()).checkDateForAttendance(
                                    currentDate, schoolId + "");

                            ((NewDashboardActivity) getActivity()).MessageBox("Current Date is " + cm.getC_Holiday_Type_Name() + " due to " + cm.getC_Activity_Name());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                Intent last30daysIntent = new Intent(NewDashboardActivity.this, AttendancePendingActivity.class);
//                last30daysIntent.putExtra("schoolId", schoolId);
//                last30daysIntent.putExtra("day", "last30Days");
//                startActivity(last30daysIntent);
                break;
        }
    }
}
