package com.tcf.sma.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.HR.Approval_Dashboard_Listing;
import com.tcf.sma.Activities.HR.ManageResignationsActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Adapters.HR.EmployeeInfoAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DashboardEmployeeApprovalsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tv_2subCount, tv_1subCount, tv_0subCount,tv_remainingCount,tv_mtt_completedCount,tv_ett_completedCount,tv_mtt_total,tv_ett_total ,progress, tv_pendingLeaves, tv_approvedLeaves, tv_lastApprovedLeaves, tv_pendingResignations, tv_approvedResignation,tv_ett_remainingCount,tv_mtt_remainingCount,
            tv_lastApprovedResignations, tv_pendingTerminations, tv_approvedTerminations, tv_lastApprovedTerminations, tv_noData, tv_attendanceTakenCount30Days,
            totalEmployeeCount, tv_label_lastthirtydays, AttendanceTaken_tvtitle;
    private LinearLayout ll_pendingLeaves, ll_approvedLeaves, ll_pendingResign, ll_approvedResign, ll_pendingTermin, ll_approvedTermin, ll_attendancetaken;
    private RecyclerView empInfo;
    private EmployeeInfoAdapter employeeInfoAdapter;

    private int pendingLeaves, approvedLeaves, pendingResignation, pendingTermination, approvedResignations, approvedTermination;

    private int schoolId = 0, roleID;
    private SchoolModel schoolModel = null;
    private CardView cv_tct, cv_resignation, cv_termination, cv_leaves, cv_empInfo, cv_attendancetaken;
    private ProgressBar profileProgress;
    List<EmployeeModel> totalEmployees;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_employee_approvals_dashboard, container, false);

        init();
        working();
        getEmpInfo();
        getTCTInfo();
        return view;
    }

    private void getTCTInfo() {
        int sub2Count = 0,ett_remaining=0,mtt_remaining=0, sub1Count = 0, sub0Count = 0,mtt_completed = 0,ett_completed = 0,mtt_total=0,ett_total=0,remaining = 0;
        List<TCTEmpSubjectTaggingModel> tctEmpSubjectTaggingModelList = TCTHelperClass.getInstance(getContext()).getEmpTakingTCT(schoolId);
        if (tctEmpSubjectTaggingModelList != null && tctEmpSubjectTaggingModelList.size() > 0) {
            cv_tct.setVisibility(View.VISIBLE);
            for (TCTEmpSubjectTaggingModel model: tctEmpSubjectTaggingModelList) {
                if (model.isMandatory()) {
                    if (model.getSubject1_ID() > 0 || model.getSubject2_ID() > 0)
                    {
                        mtt_completed++;
                        mtt_total++;
                    }
                    else if (model.getSubject1_ID() == 0 && model.getReasonID() > 0)
                    {
                        mtt_completed++;
                        mtt_total++;
                    }
                    else
                    {
                        mtt_total++;
                        mtt_remaining++;
                    }
                }
                // EDit By Muneeb
                else if(!model.isMandatory()){

                    if((model.getSubject1_ID()==0&& model.getReasonID()==0)||( model.getSubject1_ID()==0&&model.getSubject2_ID()==0)){
                        ett_remaining++;
                        ett_total++;
                    }else if((model.getSubject1_ID() > 0 || model.getSubject2_ID() > 0)||(model.getSubject1_ID() == 0 && model.getReasonID() > 0)){
                        ett_completed++;
                        ett_total++;
                    }

                }
//                if(model.getSubject1_ID() > 0 && model.getSubject2_ID() > 0) {
//                    sub2Count++;
//                }
//                else if (model.getSubject1_ID() > 0 || model.getSubject2_ID() > 0)
//                    sub1Count++;
//                else
//                    sub0Count++;
            }

//            tv_0subCount.setText(String.valueOf(sub0Count));
//            tv_1subCount.setText(String.valueOf(sub1Count));
//            tv_2subCount.setText(String.valueOf(sub2Count));
            tv_mtt_completedCount.setText(String.valueOf(mtt_completed));
            tv_ett_completedCount.setText(String.valueOf(ett_completed));
            tv_mtt_remainingCount.setText(String.valueOf(mtt_remaining));
            tv_ett_remainingCount.setText(String.valueOf(ett_remaining));
            tv_ett_total.setText(String.valueOf(ett_total));
            tv_mtt_total.setText(String.valueOf(mtt_total));
            //  tv_mtt_completedCount.setText(String.valueOf(remaining));
        } else {
            cv_tct.setVisibility(View.GONE);
        }
    }

    private void getEmpProfileStats() {
        int emailCount = EmployeeHelperClass.getInstance(getContext()).getEmployeeEmailStats(schoolId);
        int mobileCount = EmployeeHelperClass.getInstance(getContext()).getEmployeeMobileStats(schoolId);
        float numerator = emailCount + mobileCount;
        int denominator = totalEmployees.size() * 2;
        float progres = numerator / denominator;
        progres = progres * 100;
        profileProgress.setMax(100);
        profileProgress.setProgress(100);
        profileProgress.setSecondaryProgress((int) progres);
        int rounded = Math.round(progres);
        if(rounded >= 0 && rounded <= 100)
            progress.setText(rounded + "%");
        else {
            progress.setText("0%");
            profileProgress.setSecondaryProgress(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            working();
            getEmpProfileStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        roleID = DatabaseHelper.getInstance(view.getContext()).getCurrentLoggedInUser().getRoleId();

        tv_0subCount = view.findViewById(R.id.tv_0Sub_count);
        tv_1subCount = view.findViewById(R.id.tv_1Sub_count);
        tv_2subCount = view.findViewById(R.id.tv_2Sub_count);
//        This is for ETC Count
        tv_mtt_remainingCount = view.findViewById(R.id.tv_mtt_remainingCount);
        tv_ett_remainingCount = view.findViewById(R.id.tv_ett_remainingCount);
        tv_mtt_completedCount = view.findViewById(R.id.tv_mtt_completedCount);
        tv_ett_completedCount = view.findViewById(R.id.tv_ett_completedCount);
        tv_mtt_total = view.findViewById(R.id.tv_mtt_total);
        tv_ett_total = view.findViewById(R.id.tv_ett_total);
//        end here
        cv_tct = view.findViewById(R.id.cv_tct);

        tv_pendingLeaves = view.findViewById(R.id.tv_pendingLeaves);
        tv_approvedLeaves = view.findViewById(R.id.tv_approvedLeaves);
        tv_lastApprovedLeaves = view.findViewById(R.id.tv_lastApprovedLeaves);
        tv_pendingResignations = view.findViewById(R.id.tv_pendingResignations);
        tv_approvedResignation = view.findViewById(R.id.tv_approvedResignation);
        tv_lastApprovedResignations = view.findViewById(R.id.tv_lastApprovedResignations);
        tv_pendingTerminations = view.findViewById(R.id.tv_pendingTerminations);
        tv_approvedTerminations = view.findViewById(R.id.tv_approvedTerminations);
        tv_lastApprovedTerminations = view.findViewById(R.id.tv_lastApprovedTerminations);
        tv_attendanceTakenCount30Days = view.findViewById(R.id.tv_attendanceTaken30DaysCount);
        tv_label_lastthirtydays = view.findViewById(R.id.tv_label_lastthirtydays);
        AttendanceTaken_tvtitle = view.findViewById(R.id.AttendanceTaken_tvtitle);
        tv_ett_remainingCount=view.findViewById(R.id.tv_ett_remainingCount);
        tv_mtt_remainingCount=view.findViewById(R.id.tv_mtt_remainingCount);

        empInfo = view.findViewById(R.id.rv_empInfo);
        tv_noData = view.findViewById(R.id.tv_noData);

        progress = view.findViewById(R.id.tv_progress);
        profileProgress = view.findViewById(R.id.profileProgress);


        totalEmployeeCount = view.findViewById(R.id.total_count);

        cv_leaves = view.findViewById(R.id.cv_leaves);
        cv_resignation = view.findViewById(R.id.cv_resignation);
        cv_termination = view.findViewById(R.id.cv_termination);
        cv_empInfo = view.findViewById(R.id.cv_empInfo);
        cv_attendancetaken = view.findViewById(R.id.cv_attendance_taken);

        ll_pendingResign = view.findViewById(R.id.ll_pendingResign);
        ll_pendingResign.setOnClickListener(this);

        ll_approvedResign = view.findViewById(R.id.ll_approvedResign);
        ll_approvedResign.setOnClickListener(this);

        ll_pendingTermin = view.findViewById(R.id.ll_pendingTermin);
        ll_pendingTermin.setOnClickListener(this);

        ll_approvedTermin = view.findViewById(R.id.ll_approvedTermin);
        ll_approvedTermin.setOnClickListener(this);

        ll_pendingLeaves = view.findViewById(R.id.ll_pendingLeaves);
        ll_pendingLeaves.setOnClickListener(this);

        ll_approvedLeaves = view.findViewById(R.id.ll_approvedLeaves);
        ll_approvedLeaves.setOnClickListener(this);

        ll_attendancetaken = view.findViewById(R.id.ll_attendance_taken);
        ll_attendancetaken.setOnClickListener(this);
    }

    private void working() {
        schoolId = ((NewDashboardActivity) requireActivity()).schoolId;
        if (schoolId > 0) {
            schoolModel = DatabaseHelper.getInstance(view.getContext()).getSchoolById(schoolId);

            if (schoolModel != null) {
                List<String> allowedModules = null;
                if (schoolModel.getAllowedModule_App() != null) {
                    allowedModules = Arrays.asList(schoolModel.getAllowedModule_App().split(","));
                }

                if (allowedModules != null && allowedModules.contains(AppConstants.HREmployeeListingModuleValue)) {
                    cv_empInfo.setVisibility(View.VISIBLE);
                } else {
                    cv_empInfo.setVisibility(View.GONE);
                }

                if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                    cv_leaves.setVisibility(View.VISIBLE);
                    cv_attendancetaken.setVisibility(View.VISIBLE);
                } else {
                    cv_leaves.setVisibility(View.GONE);
                    cv_attendancetaken.setVisibility(View.GONE);
                }

                if (allowedModules != null && allowedModules.contains(AppConstants.HRResignationModuleValue)) {
                    cv_resignation.setVisibility(View.VISIBLE);
                } else {
                    cv_resignation.setVisibility(View.GONE);
                }

             /*   if (allowedModules != null && allowedModules.contains("9")) {
                    cv_termination.setVisibility(View.VISIBLE);
                } else {
                    cv_termination.setVisibility(View.GONE);
                }*/

            }

            calculateLeaves();
            calculateResignations();
            calculateTerminations();
            calculateAttendanceTaken30DaysCount();

        } else {
            tv_pendingLeaves.setText("");
            tv_approvedLeaves.setText("");
            tv_lastApprovedLeaves.setText("");
            tv_pendingResignations.setText("");
            tv_approvedResignation.setText("");
            tv_lastApprovedResignations.setText("");
            tv_pendingTerminations.setText("");
            tv_approvedTerminations.setText("");
            tv_lastApprovedTerminations.setText("");
            tv_attendanceTakenCount30Days.setText("");
        }
    }

    private void calculateAttendanceTaken30DaysCount() {
        if (!todayHoliday()) {
            populateLast30DaysAttendance();
        } else {
            tv_label_lastthirtydays.setTextColor(Color.BLACK);
            AttendanceTaken_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
            ll_attendancetaken.setBackgroundColor(Color.WHITE);
            tv_attendanceTakenCount30Days.setText("0/0");
        }

    }

    private void populateLast30DaysAttendance(){
        try {
            String attendanceTaken30Days = "0", attendanceTotal30Days = "0";
            if (schoolId > 0) {
                attendanceTaken30Days = EmployeeHelperClass.getInstance(getContext()).getAttendanceTakenCountFor30Days(schoolId + "");
            } else {
                attendanceTaken30Days = EmployeeHelperClass.getInstance(getContext()).getAttendanceTakenCountFor30Days(String.valueOf(AppModel.getInstance().getuserSchoolIDS(view.getContext())));
            }

            CalendarsModel calendarsModel = null;
            int totalcount = 0;
            if (schoolId != 0) {
                calendarsModel = DatabaseHelper.getInstance(view.getContext()).getTeacherLast30WorkingDays(schoolId);
                totalcount = (calendarsModel.getTotalDays() < 0 ? calendarsModel.getTotalDays() * -1 : calendarsModel.getTotalDays()) - calendarsModel.getTotalOffDays();
//                totalcount = totalcount * (attendanceTotalToday.isEmpty() == false ? Integer.valueOf(attendanceTotalToday) : 0);
            } else {
                List<SchoolModel> sm = DatabaseHelper.getInstance(view.getContext()).getAllUserSchoolsForEmployeeLeavesAndAttend();
//                String totalCountForToday = "";
                int sum = 0;
                for (SchoolModel mod : sm) {
                    calendarsModel = DatabaseHelper.getInstance(view.getContext()).getTeacherLast30WorkingDays(mod.getId());
                    sum = (calendarsModel.getTotalDays() < 0 ? calendarsModel.getTotalDays() * -1 : calendarsModel.getTotalDays()) - calendarsModel.getTotalOffDays();
//                    totalCountForToday = DatabaseHelper.getInstance(view.getContext()).getAttendanceTotalCountForToday(String.valueOf(mod.getId()));
                    try {
//                        totalcount += sum * Integer.valueOf(totalCountForToday);
                        totalcount += sum;
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


            if (Integer.parseInt(attendanceTaken30Days) < totalcount) {
                tv_label_lastthirtydays.setTextColor(getResources().getColor(R.color.light_red));
                AttendanceTaken_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_red));
                ll_attendancetaken.setBackgroundColor(getResources().getColor(R.color.light_red_color));
            } else {
                tv_label_lastthirtydays.setTextColor(Color.BLACK);
                AttendanceTaken_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
                ll_attendancetaken.setBackgroundColor(Color.WHITE);
            }

            tv_attendanceTakenCount30Days.setText(attendanceTaken30Days + "/" + attendanceTotal30Days);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void calculateLeaves() {
        pendingLeaves = EmployeeHelperClass.getInstance(view.getContext()).getPendingLeavesCount(schoolId);
        approvedLeaves = EmployeeHelperClass.getInstance(view.getContext()).getApprovedLeavesCount(schoolId);
        String lastApprovedLeave = EmployeeHelperClass.getInstance(view.getContext()).getLastApprovedLeave(schoolId);
        if(lastApprovedLeave!=null && !lastApprovedLeave.isEmpty())
            lastApprovedLeave = AppModel.getInstance().convertDatetoFormat(lastApprovedLeave, "yyyy-MM-dd hh:mm:ss", "dd-MMM-yy");

        tv_pendingLeaves.setText(pendingLeaves + "");
        tv_approvedLeaves.setText(approvedLeaves + "");
        tv_lastApprovedLeaves.setText(lastApprovedLeave+"");
    }

    private void calculateResignations() {
        pendingResignation = EmployeeHelperClass.getInstance(view.getContext()).getPendingApprovalsCount();
//        pendingResignation = EmployeeHelperClass.getInstance(view.getContext()).getPendingApprovals(0, "", "").size();
//        pendingResignation = EmployeeHelperClass.getInstance(view.getContext()).getPendingResignationCount(schoolId);
//        approvedResignations = EmployeeHelperClass.getInstance(view.getContext()).getApprovedResignationCount(schoolId);
//        String lastApprovedResign = EmployeeHelperClass.getInstance(view.getContext()).getLastApprovedResign(schoolId);
//        lastApprovedResign = AppModel.getInstance().convertDatetoFormat(lastApprovedResign, "yyyy-MM-dd hh:mm:ss", "dd-MMM-yy");

        tv_pendingResignations.setText(pendingResignation + "");
//        tv_approvedResignation.setText(approvedResignations + "");
//        tv_lastApprovedResignations.setText(lastApprovedResign);

    }

    private void calculateTerminations() {
        pendingTermination = EmployeeHelperClass.getInstance(view.getContext()).getPendingTerminationCount(schoolId);
        approvedTermination = EmployeeHelperClass.getInstance(view.getContext()).getApprovedTerminationCount(schoolId);
        String lastApprovedTermination = EmployeeHelperClass.getInstance(view.getContext()).getLastApprovedTermination(schoolId);
        if(lastApprovedTermination!=null && !lastApprovedTermination.isEmpty())
            lastApprovedTermination = AppModel.getInstance().convertDatetoFormat(lastApprovedTermination, "yyyy-MM-dd hh:mm:ss", "dd-MMM-yy");

        tv_pendingTerminations.setText(pendingTermination + "");
        tv_approvedTerminations.setText(approvedTermination + "");
        tv_lastApprovedTerminations.setText(lastApprovedTermination+"");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_pendingLeaves) {
            if (roleID != AppConstants.roleId_103_V) {
                /*if (pendingLeaves > 0) {
                    Intent intent = new Intent(view.getContext(), LeavesApprovalDashboardListing.class);
                    intent.putExtra("approvalStatus", 1);
                    intent.putExtra("schoolId", schoolId);
//                intent.putExtra("Flag",false);
                    startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "No Pending Leaves", Toast.LENGTH_SHORT).show();
                }*/
            }
        } else if (v.getId() == R.id.ll_approvedLeaves) {
            if (roleID != AppConstants.roleId_103_V) {
                /*if (approvedLeaves > 0) {
                    Intent intent = new Intent(view.getContext(), LeavesApprovalDashboardListing.class);
                    intent.putExtra("approvalStatus", 2);
                    intent.putExtra("schoolId", schoolId);
                    startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "No Approved Leaves", Toast.LENGTH_SHORT).show();
                }*/
            }
        } else if (v.getId() == R.id.ll_pendingResign) {
            if (pendingResignation > 0) {
                Intent intent = new Intent(view.getContext(), ManageResignationsActivity.class).putExtra("isViewSeparation", true);
                intent.putExtra("status", 1);
//                intent.putExtra("approvalStatus", 1);
//                intent.putExtra("schoolId", schoolId);
                startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "No Pending Approvals", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.ll_approvedResign) {
            if (approvedResignations > 0) {
                Intent intent = new Intent(view.getContext(), Approval_Dashboard_Listing.class);
                intent.putExtra("resignType", 1);
                intent.putExtra("approvalStatus", 3);
                intent.putExtra("schoolId", schoolId);
                startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "No Approved Resignations", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.ll_pendingTermin) {
            if (pendingTermination > 0) {
                Intent intent = new Intent(view.getContext(), Approval_Dashboard_Listing.class);
                intent.putExtra("resignType", 2);
                intent.putExtra("approvalStatus", 1);
                intent.putExtra("schoolId", schoolId);
                startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "No Pending Terminations", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.ll_approvedTermin) {
            if (approvedTermination > 0) {
                Intent intent = new Intent(view.getContext(), Approval_Dashboard_Listing.class);
                intent.putExtra("resignType", 2);
                intent.putExtra("approvalStatus", 3);
                intent.putExtra("schoolId", schoolId);
                startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "No Approved Terminations", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.ll_attendance_taken) {
            if (!todayHoliday()) {
                /*Intent last30daysAttIntent = new Intent(view.getContext(), EmployeeAttendancePendingActivity.class);
                last30daysAttIntent.putExtra("schoolId", schoolId);
                last30daysAttIntent.putExtra("day", "last30Days");
                startActivity(last30daysAttIntent);*/
            } else {
                try {
                    if (schoolId > 0) {
                        String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd");
                        CalendarsModel cm = DatabaseHelper.getInstance(view.getContext()).checkDateForEmployeeTeacherAttendance(
                                currentDate, schoolId + "");

                        if (cm != null) {
                            ((NewDashboardActivity) requireActivity()).MessageBox("Current Date is " +
                                    cm.getC_Holiday_Type_Name() + " due to " + cm.getC_Activity_Name());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void getEmpInfo() {
        try {
            List<EmployeeModel> em = EmployeeHelperClass.getInstance(getContext()).getEmpDesignationInfo(schoolId);
            totalEmployees = EmployeeHelperClass.getInstance(getContext()).getSearchedEmployees(schoolId);
            totalEmployeeCount.setText(totalEmployees.size() + "");

            empInfo.setLayoutManager(new LinearLayoutManager(view.getContext()));
            employeeInfoAdapter = new EmployeeInfoAdapter(getActivity(), em);
            empInfo.setAdapter(employeeInfoAdapter);

            if (em.size() <= 0) {
                tv_noData.setVisibility(View.VISIBLE);
                empInfo.setVisibility(View.GONE);
            } else {
                tv_noData.setVisibility(View.GONE);
                empInfo.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private boolean todayHoliday() {
        boolean isHoliday = false;

        String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd");
        if (schoolId > 0) {
            CalendarsModel cm = DatabaseHelper.getInstance(view.getContext()).checkDateForEmployeeTeacherAttendance(
                    currentDate, schoolId + "");

            if (cm != null) {
                isHoliday = true;
            } else {
                isHoliday = false;
            }
        }

        return isHoliday;
    }
}
