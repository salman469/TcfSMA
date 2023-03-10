package com.tcf.sma.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.FeesCollection.CashInHandAndDeposit.CashInHandActivity;
import com.tcf.sma.Activities.HighestDuesStudentsListActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Adapters.FeesCollection.ReceivableAdapter;
import com.tcf.sma.Adapters.HighestDuesCardAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandModel;
import com.tcf.sma.Models.Fees_Collection.DashboardReceivableModel;
import com.tcf.sma.Models.Fees_Collection.DashboardViewReceivablesModels;
import com.tcf.sma.Models.Fees_Collection.PreviousReceivableModel;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.FinanceCheckSum;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class DashboardFinanceFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    ArrayList<HighestDuesStudentsModel> hdList = new ArrayList<>();
    HighestDuesCardAdapter highestDuesCardAdapter;
    private TextView CVCashInHand_tvAmount, CVCashInHand_selectedSchool_tvAmount, CVCashInHand_btnDepositAmount, CVCashInHand_tvDepositisDue,
            CVReceivables_tvTotalDue, CVReceivables_tvReceived, CVReceivables_tvBalance, CVAvgFeesCollection_tvTarget, CVAvgFeesCollection_tvLast30Days,
            CVAvgFeesCollection_tvThisSession, CVAvgFeesCollection_tvOffTarget, tvHighestDuesFromStudentSeeAll, tv_avgSetFee, tv_avgCollection,
            CVAvgFeesCollection_tvtitle, tv_SetFee;
    private View view;
    private RecyclerView rvHighestDuesFromStudent, rv_receivables;
    private LinearLayout ll_avgFeesCollection, ll_receivables, ll_dummy_header, ll_dummu_data;
    private RadioGroup rg_receivables;
    private int schoolId = 0;
    private List<SchoolModel> schoolModels;
    private boolean isFeesHidden;
    private int StudentCount = 0;
    private String filterReceivableBy = "t";
    boolean FinanceSyncCompleted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finance_dashboard, container, false);

        init(view);
        working(view);

        return view;
    }

    private void init(View view) {
        /*----------------CashInHandView----------------*/
        CVCashInHand_tvAmount = view.findViewById(R.id.CVCashInHand_tvAmount);
        CVCashInHand_selectedSchool_tvAmount = view.findViewById(R.id.CVCashInHand_selectedSchool_tvAmount);
        CVCashInHand_btnDepositAmount = view.findViewById(R.id.CVCashInHand_btnDepositAmount);
        CVCashInHand_tvDepositisDue = view.findViewById(R.id.CVCashInHand_tvDepositisDue);
        CVCashInHand_btnDepositAmount.setOnClickListener(this);

        /*Receivables*/
        ll_dummu_data = view.findViewById(R.id.dummyRecData);
        ll_dummy_header = view.findViewById(R.id.dummyRecHeader);
//        tv_noData = view.findViewById(R.id.tv_noData);
        CVReceivables_tvTotalDue = view.findViewById(R.id.CVReceivables_tvTotalDue);
        CVReceivables_tvReceived = view.findViewById(R.id.CVReceivables_tvReceived);
        CVReceivables_tvBalance = view.findViewById(R.id.CVReceivables_tvBalance);

        rv_receivables = view.findViewById(R.id.rv_receivables);
        rv_receivables.setHasFixedSize(true);
        rv_receivables.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        rg_receivables = view.findViewById(R.id.rg_receivables);
        rg_receivables.setOnCheckedChangeListener(this);

        ll_receivables = view.findViewById(R.id.ll_receivables);

        /*Avg Fees Collection*/
        CVAvgFeesCollection_tvLast30Days = view.findViewById(R.id.CVAvgFeesCollection_tvLast30Days);
        CVAvgFeesCollection_tvThisSession = view.findViewById(R.id.CVAvgFeesCollection_tvThisSession);
        CVAvgFeesCollection_tvOffTarget = view.findViewById(R.id.CVAvgFeesCollection_tvOffTarget);

        tv_SetFee = view.findViewById(R.id.tv_SetFee);
        CVAvgFeesCollection_tvtitle = view.findViewById(R.id.CVAvgFeesCollection_tvtitle);
        ll_avgFeesCollection = view.findViewById(R.id.ll_avgFeesCollection);
        CVAvgFeesCollection_tvTarget = view.findViewById(R.id.CVAvgFeesCollection_tvTarget);
        tv_avgSetFee = view.findViewById(R.id.tv_avgSetFee);
        tv_avgCollection = view.findViewById(R.id.tv_avgCollection);

        /*----------------Highest Dues From Students----------------*/

        tvHighestDuesFromStudentSeeAll = view.findViewById(R.id.tvHighestDuesFromStudentSeeAll);
        tvHighestDuesFromStudentSeeAll.setOnClickListener(this);
        rvHighestDuesFromStudent = view.findViewById(R.id.rvHighestDuesFromStudent);
        rvHighestDuesFromStudent.setHasFixedSize(true);
        LinearLayoutManager llm1 = new LinearLayoutManager(view.getContext());
        llm1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHighestDuesFromStudent.setLayoutManager(llm1);

        FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(getContext(),
                AppConstants.FinanceSyncCompleted, false);
    }

    private void working(View view) {
        schoolId = ((NewDashboardActivity) Objects.requireNonNull(getActivity())).schoolId;
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(getContext());
        if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(new SchoolModel(0, "All"));

        if (schoolId > 0) {
            StudentCount = DatabaseHelper.getInstance(view.getContext()).getAllStudentsCount(String.valueOf(schoolId));
        } else {
            StudentCount = DatabaseHelper.getInstance(view.getContext()).getAllStudentsCount(AppModel.getInstance().getuserSchoolIDS(view.getContext()));
//                StudentCount = DatabaseHelper.getInstance(view.getContext()).getAllStudentsCount(schoolId);
        }

        isFeesHidden = AppModel.getInstance().readFromSharedPreferences(view.getContext(), AppConstants.HIDE_FEES_COLLECTION).equals("1");

        try {
            populateAverageFeesCollection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            populateHighestDuesStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            populateCashInHand();
//            populateReceivables();
            populateReceivablesList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setCVCashInHand_btnDepositAmountEnabled(boolean enabled) {
        if (enabled) {
            CVCashInHand_btnDepositAmount.setEnabled(enabled);
            CVCashInHand_btnDepositAmount.setBackground(getResources().getDrawable(R.drawable.blue_button_rounded_bg));
        } else {
            CVCashInHand_btnDepositAmount.setEnabled(enabled);
            CVCashInHand_btnDepositAmount.setBackground(getResources().getDrawable(R.drawable.gray_button_rounded_bg));
        }
    }

    private void populateCashInHand() throws Exception {

        try {
            if (FinanceCheckSum.Instance(new WeakReference<>(view.getContext())).isChecksumSuccess(view.getContext(), false)) {

                //Enabled button
                setCVCashInHand_btnDepositAmountEnabled(true);
                String NewCashInHand = null;
                String NewAllCashInHand = null;

//                CashInHandModel cashInHandForAllSchools;
//                CashInHandModel cashInHandForSingleSchool;
                if (schoolId == 0) {
                    //all
//                    cashInHandForAllSchools = FeesCollection.getInstance(view.getContext()).getCashInHandData(AppModel.getInstance().getAllUserSchoolsForFinance(view.getContext()));
                    NewAllCashInHand = String.valueOf(FeesCollection.getInstance(view.getContext()).getCashInHand(0, DatabaseHelper.getInstance(view.getContext()).getAcademicSessionId(schoolModels.get(0).getId())));

                    if (isFeesHidden) {
                        NewAllCashInHand = null;
//                        cashInHandForAllSchools.setTotal(null);
                    }
                    if (NewAllCashInHand != null && Integer.parseInt(NewAllCashInHand) > 0) {
                        CVCashInHand_tvAmount.setText(Html.fromHtml("<p align=left><b>Total:</b> Rs. " + AppModel.getInstance().formatNumberInCommas(Long.parseLong(NewAllCashInHand)) + "</p>"));
                        if (Long.parseLong(NewAllCashInHand) > 5000)
                            CVCashInHand_tvDepositisDue.setVisibility(View.VISIBLE);
                        CVCashInHand_btnDepositAmount.setVisibility(View.VISIBLE);
                    } else {
                        CVCashInHand_tvAmount.setText(Html.fromHtml("<p align=left><b>Total:</b> Rs. " + AppModel.getInstance().formatNumberInCommas(Long.parseLong(NewAllCashInHand)) + "</p>"));
                        CVCashInHand_tvDepositisDue.setVisibility(View.GONE);
                        CVCashInHand_btnDepositAmount.setVisibility(View.GONE);
                        CVCashInHand_tvAmount.setTypeface(Typeface.DEFAULT_BOLD);
                    }

                    CVCashInHand_tvAmount.setGravity(Gravity.CENTER);
                    CVCashInHand_selectedSchool_tvAmount.setVisibility(View.GONE);
                } else {
                    //all
//                    cashInHandForAllSchools = FeesCollection.getInstance(view.getContext()).getCashInHandData(AppModel.getInstance().getAllUserSchoolsForFinance(view.getContext()));
                    NewAllCashInHand = String.valueOf(FeesCollection.getInstance(view.getContext()).getCashInHand(0, DatabaseHelper.getInstance(view.getContext()).getAcademicSessionId(schoolModels.get(0).getId())));
                    //single School
//                    cashInHandForSingleSchool = FeesCollection.getInstance(view.getContext()).getCashInHandData(schoolId + "");
                    NewCashInHand = String.valueOf(FeesCollection.getInstance(view.getContext()).getCashInHand(schoolId, DatabaseHelper.getInstance(view.getContext()).getAcademicSessionId(schoolModels.get(0).getId())));

                    if (isFeesHidden) {
//                        cashInHandForAllSchools.setTotal(null);
//                        cashInHandForSingleSchool.setTotal(null);

                        NewAllCashInHand = null;
                        NewCashInHand = null;
                    }

                    String text = "";
                    if (NewAllCashInHand != null && Integer.parseInt(NewAllCashInHand) > 0) {

                        if (NewCashInHand != null) {
                            CVCashInHand_selectedSchool_tvAmount.setText("This School: Rs. " + AppModel.getInstance().formatNumberInCommas(Long.parseLong(NewCashInHand)) );
                        } else {
                            CVCashInHand_selectedSchool_tvAmount.setText("This School: Rs. 0");
                        }

                        CVCashInHand_tvAmount.setText("All Schools: Rs.  " + AppModel.getInstance().formatNumberInCommas(Long.parseLong(NewAllCashInHand)));

                        if (Long.parseLong(NewAllCashInHand) > 5000) {
                            CVCashInHand_tvDepositisDue.setVisibility(View.VISIBLE);
                        }
                        CVCashInHand_btnDepositAmount.setVisibility(View.VISIBLE);


                        CVCashInHand_tvAmount.setGravity(Gravity.START);
                        CVCashInHand_tvAmount.setTypeface(Typeface.DEFAULT);
                        CVCashInHand_selectedSchool_tvAmount.setGravity(Gravity.START);
                        CVCashInHand_selectedSchool_tvAmount.setVisibility(View.VISIBLE);

                    } else {
                        CVCashInHand_tvAmount.setText(NewAllCashInHand != null
                                ? "All Schools: Rs.  " + AppModel.getInstance().formatNumberInCommas(Long.parseLong(NewAllCashInHand))
                                : "All Schools: Rs. 0");
                        CVCashInHand_selectedSchool_tvAmount.setText(NewCashInHand != null
                                ? "This School: Rs. " + AppModel.getInstance().formatNumberInCommas(Long.parseLong(NewCashInHand))
                                : "This School: Rs. 0");
                        CVCashInHand_tvDepositisDue.setVisibility(View.GONE);
                        CVCashInHand_btnDepositAmount.setVisibility(View.GONE);
//                        CVCashInHand_selectedSchool_tvAmount.setVisibility(View.GONE);

                        CVCashInHand_tvAmount.setTypeface(Typeface.DEFAULT_BOLD);
                        CVCashInHand_tvAmount.setGravity(Gravity.CENTER);
                    }


                }
            } else {
                CVCashInHand_tvAmount.setText("0");
                CVCashInHand_selectedSchool_tvAmount.setText("0");

                //Disabled button
                setCVCashInHand_btnDepositAmountEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private boolean isChecksumSuccessfull(){
//        int i = 0;
//        if (FinanceCheckSum.Instance(new WeakReference<>(view.getContext())).isCheckSumApplicable()) {
//            i++;
////            Toast.makeText(view.getContext(), "Check sum is pending, please wait for the sync to complete.", Toast.LENGTH_SHORT).show();
//        }else if (!FinanceCheckSum.Instance(new WeakReference<>(view.getContext())).isCheckSumSuccessfull()){
//            i++;
////            Toast.makeText(view.getContext(), "Disabled due to checksum failure/", Toast.LENGTH_SHORT).show();
//        }else if (FeesCollection.getInstance(view.getContext()).getClassSectionForFeeEntryBySchoolId(SurveyAppModel.getInstance().getAllUserSchoolsForFinance(view.getContext())).size() != 0) {
//            i++;
////            Toast.makeText(view.getContext(), "Enter fee entry first", Toast.LENGTH_SHORT).show();
//        }else if (FeesCollection.getInstance(view.getContext()).getUnuploadedCountFeeEntry(SurveyAppModel.getInstance().getAllUserSchoolsForFinance(view.getContext())) != 0){
//            i++;
////            Toast.makeText(view.getContext(), "Enter fee entry first", Toast.LENGTH_SHORT).show();
//        }
//
//        return i == 0;
//    }

    private void populateReceivablesList() {

        if (isFeesHidden) {
            ll_receivables.setVisibility(View.GONE);
            return;
        } else {
            ll_receivables.setVisibility(View.VISIBLE);
        }


        if (schoolId > 0) {
            List<FeeTypeModel> feeTypeModelList = FeesCollection.getInstance(view.getContext()).getFeeTypesForReceivables();

            List<DashboardReceivableModel> receivableList = FeesCollection.getInstance(view.getContext()).getReceivableForDashboard(schoolId + "", filterReceivableBy);

//            List<DashboardReceivableModel> receivedList = FeesCollection.getInstance(view.getContext()).getReceivedForDashboard(schoolId+"",filterReceivableBy);

            List<DashboardViewReceivablesModels> receivablesModelsList = new ArrayList<>();

            DashboardViewReceivablesModels receivablesModels = new DashboardViewReceivablesModels();
            for (FeeTypeModel feeTypeModel : feeTypeModelList) {
                boolean feeTypeFound = false;
                for (DashboardReceivableModel receivableModel : receivableList) {
                    if (receivableModel.getFeeType_id() == feeTypeModel.getId()) {

                        receivablesModels = new DashboardViewReceivablesModels();
                        receivablesModels.setTitle(feeTypeModel.getName());
                        receivablesModels.setFeeTypeId(receivableModel.getFeeType_id());
                        receivablesModels.setAmountReceivable(receivableModel.getTotalReceivable());
                        receivablesModels.setAmountReceived(receivableModel.getTotalReceived());
                        receivablesModels.setAmountOpening(receivableModel.getTotalOpening());
                        receivablesModels.setAmountClosingActive(receivableModel.getTotalClosingActive());
                        receivablesModels.setAmountClosingInActive(receivableModel.getTotalClosingInActive());
                        receivablesModelsList.add(receivablesModels);
                        feeTypeFound = true;
                        break;
                    }
                }
                if (!feeTypeFound) {
                    receivablesModels = new DashboardViewReceivablesModels();
                    receivablesModels.setTitle(feeTypeModel.getName());
                    receivablesModels.setFeeTypeId(feeTypeModel.getId());
                    receivablesModels.setAmountReceivable("");
                    receivablesModels.setAmountReceived("");
                    receivablesModels.setAmountOpening("");
                    receivablesModels.setAmountClosingActive("");
                    receivablesModels.setAmountClosingInActive("");
                    receivablesModelsList.add(receivablesModels);
                }
            }

//            double totalreceivable = FeesCollection.getInstance(view.getContext()).getReceivables(schoolId + "", 0);
//            double totalreceived = FeesCollection.getInstance(view.getContext()).getReceivables(schoolId + "", 1);

            if (receivableList != null && receivableList.size() > 0) {
                ReceivableAdapter receivableAdapter = new ReceivableAdapter(view.getContext(), receivablesModelsList,filterReceivableBy);
                rv_receivables.setAdapter(receivableAdapter);
                ll_dummu_data.setVisibility(View.GONE);
                ll_dummy_header.setVisibility(View.GONE);
                rv_receivables.setVisibility(View.VISIBLE);
            }
//            else {
//                ll_dummu_data.setVisibility(View.VISIBLE);
//                ll_dummy_header.setVisibility(View.VISIBLE);
//                rv_receivables.setVisibility(View.GONE);
//            }

//            for (int i = 0; i < receivablesModelsList.size(); i++) {
//                for (DashboardReceivableModel model : receivedList) {
//                    if (model.getFeeType_id() == receivablesModelsList.get(i).getFeeTypeId()) {
//                        receivablesModelsList.get(i).setAmountReceived(model.getTotalAmount());
//                        break;
//                    }
//                }
//            }

//            if ((receivableList != null && receivableList.size() > 0) || (receivedList != null && receivedList.size() > 0)) {
//                ReceivableAdapter receivableAdapter = new ReceivableAdapter(view.getContext(), receivablesModelsList);
//                rv_receivables.setAdapter(receivableAdapter);
//                tv_noData.setVisibility(View.GONE);
//                ll_receivables.setVisibility(View.VISIBLE);
//            }else {
//                tv_noData.setVisibility(View.VISIBLE);
//                ll_receivables.setVisibility(View.GONE);
//            }
        }
    }

    private void populateReceivables() throws Exception {

        if (isFeesHidden) {
            CVReceivables_tvTotalDue.setText("");
            CVReceivables_tvReceived.setText("");
            CVReceivables_tvBalance.setText("");
            return;
        }


        double totalDue = 0;
        double totalreceived = 0;

        if (schoolId > 0) {
            totalDue = FeesCollection.getInstance(view.getContext()).getReceivables(schoolId + "", 0);
            totalreceived = FeesCollection.getInstance(view.getContext()).getReceivables(schoolId + "", 1);
        } else {
            totalDue = FeesCollection.getInstance(view.getContext()).getReceivables(AppModel.getInstance().getAllUserSchoolsForFinance(view.getContext()), 0);
            totalreceived = FeesCollection.getInstance(view.getContext()).getReceivables(AppModel.getInstance().getAllUserSchoolsForFinance(view.getContext()), 1);
        }

        CVReceivables_tvTotalDue.setText("" + (int) totalDue);
        CVReceivables_tvReceived.setText("" + (int) totalreceived);

        long balance = 0;
        balance = (long) (totalDue - totalreceived);

        CVReceivables_tvBalance.setText("" + AppModel.getInstance().formatNumberInCommas(balance));

//        List<String> dues = AppInvoice.getInstance(view.getContext()).getDashReceivablesDues(schoolId);
//        List<String> received = AppInvoice.getInstance(view.getContext()).getDashReceivablesReceived(schoolId);

//        if (dues != null) {
//            for (String str : dues) {
//                if (str != null)
//                    totalDue += str.equals("") ? 0 : Math.abs(Double.valueOf(str));
//                else
//                    totalDue += 0;
//            }
//
//            CVReceivables_tvTotalDue.setText("" + totalDue);
//        }
//        if (received != null) {
//
//            for (String str : received) {
//                if (str != null)
//                    totalreceived += str.equals("") ? 0 : Math.abs(Double.valueOf(str));
//            }
//
//            CVReceivables_tvReceived.setText("" + totalreceived);
//        }
//        CVReceivables_tvBalance.setText("" + (totalDue - totalreceived));

    }

    private void populateAverageFeesCollection() throws Exception {

        if (isFeesHidden) {
            CVAvgFeesCollection_tvLast30Days.setText("");
        } else {
            SchoolModel sm = DatabaseHelper.getInstance(view.getContext()).getSchoolById(schoolId);

            if (sm != null){
                //Target Amount
                int targetAmount = !Strings.isEmptyOrWhitespace(sm.getTarget_Amount()) ? Integer.parseInt(sm.getTarget_Amount()) : 0;
                CVAvgFeesCollection_tvTarget.setText(targetAmount + "");

                //Avg Collection
                if (schoolId > 0) {
                    try {
                        int totalReceivables = FeesCollection.getInstance(view.getContext()).getNoOfMonthlyFeesRecords(schoolId, sm.getAcademic_Session_Id());
                        int numberOfMonths = DatabaseHelper.getInstance(view.getContext()).getMonthsBetweenCurrentDateAndFeesTransStartDate(schoolId, sm.getAcademic_Session_Id());

                        double avgMonthlyFee = (double) totalReceivables / (double) numberOfMonths;
                        if (numberOfMonths != 0) {
                            double avgMonthlyFeePerStudent = avgMonthlyFee / (double) StudentCount;
                            avgMonthlyFeePerStudent = Math.round(avgMonthlyFeePerStudent);
//                            tv_avgCollection.setText(String.format(Locale.US, "%.2f", avgMonthlyFeePerStudent) + "");
                            tv_avgCollection.setText(avgMonthlyFeePerStudent + "");
                        } else {
                            tv_avgCollection.setText("-");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //AVG Set Fee
                    int avgFeeTarget = (int) AppModel.getInstance().getFeeTargetAverage(new WeakReference<>(view.getContext()), schoolId);

                    if (avgFeeTarget < targetAmount) {
                        CVAvgFeesCollection_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_red));
                        tv_avgSetFee.setTextColor(getResources().getColor(R.color.light_red));
                        tv_SetFee.setTextColor(getResources().getColor(R.color.light_red));
                        ll_avgFeesCollection.setBackgroundColor(getResources().getColor(R.color.light_red_color));

                    } else {
                        CVAvgFeesCollection_tvtitle.setBackgroundColor(getResources().getColor(R.color.light_app_green));
                        tv_avgSetFee.setTextColor(Color.BLACK);
                        tv_SetFee.setTextColor(Color.BLACK);
                        ll_avgFeesCollection.setBackgroundColor(getResources().getColor(R.color.white));
                    }

                    tv_avgSetFee.setText(avgFeeTarget + "");
                } else {
                    CVAvgFeesCollection_tvTarget.setText("-");
                    tv_avgSetFee.setText("-");
                    tv_avgCollection.setText("-");
                }
            } else {
                CVAvgFeesCollection_tvTarget.setText("-");
                tv_avgSetFee.setText("-");
                tv_avgCollection.setText("-");
            }

//            List<PreviousReceivableModel> received = FeesCollection.getInstance(view.getContext())
//                    .getLast30DaysForAverageFeesCollection(schoolId);
//            double totalreceived = 0;
//
//            if (received.size() > 0) {
//                List<FeeTypeModel> feeTypeModelList = FeesCollection.getInstance(view.getContext()).getFeeTypes();
//                for (int i = 0; i < feeTypeModelList.size(); i++) {
//
//                    if (i < 7) {
//                        for (PreviousReceivableModel str : received) {
//                            if (str.getTotalAmount() != null)
//                                totalreceived += str.getTotalAmount().equals("") ? 0 : Math.abs(Double.valueOf(str.getTotalAmount()));
//                        }
//
//                    }
//                }
//
//                CVAvgFeesCollection_tvLast30Days.setText("" + String.format("%.2f", (totalreceived / StudentCount)));
//            }
//        List<String> received = AppInvoice.getInstance(view.getContext()).getLast30DaysForAverageFeesCollection(schoolId);
//        if (received != null) {
//
//            for (String str : received) {
//                if (str != null)
//                    totalreceived += str.equals("") ? 0 : Math.abs(Double.valueOf(str));
//            }
//
//            CVAvgFeesCollection_tvLast30Days.setText("" + String.format("%.2f", (totalreceived / StudentCount)));
//        }
        }

    }

    private long getHolidaysElapsedFrom() {
        List<String> uHolidays = getUniqueHolidays();
        long count = 0;
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date date1 = null;
        Date cDate = null;
        String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd'T'00:00:00");

        try {
            cDate = df1.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (String holidayDate : uHolidays) {
            try {
                date1 = df1.parse(holidayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date1 != null) {
                if (date1.before(cDate)) {
                    // (Date <= today)
                    count++;
                }
            }
        }
        return count;
    }

    private List<String> getUniqueHolidays() {
        List<CalendarsModel> OffdaysList = DatabaseHelper.getInstance(view.getContext()).getAllDatesForHolidays(schoolId + "");
        List<String> holidays = new ArrayList<>();
        for (CalendarsModel cm : OffdaysList) {
            holidays.add(cm.getActivity_Start_Date());
        }
        if (holidays.size() > 0) {
            Set<String> uHolidays = new HashSet<>(holidays);

            holidays.clear();
            holidays.addAll(uHolidays);
        }
        return holidays;
    }

    private void populateHighestDuesStudents() throws Exception {

        try {

            if (isFeesHidden) {
                return;
            }

            if (schoolId > 0)
                hdList = DatabaseHelper.getInstance(view.getContext()).gethighestDuesStudent(String.valueOf(schoolId));
            else {
                hdList = DatabaseHelper.getInstance(view.getContext()).gethighestDuesStudent(AppModel.getInstance().getuserSchoolIDS(view.getContext()));
//                hdList = DatabaseHelper.getInstance(view.getContext()).gethighestDuesStudent(schoolId + "");
            }
            if (hdList != null && hdList.size() != 0) {
                highestDuesCardAdapter = new HighestDuesCardAdapter(hdList, view.getContext(), schoolId);
                rvHighestDuesFromStudent.setAdapter(highestDuesCardAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CVCashInHand_btnDepositAmount:
                if (FinanceSyncCompleted) {
                    if (FinanceCheckSum.Instance(new WeakReference<>(getContext())).isChecksumSuccess(getContext(), true)) {
                        startActivity(new Intent(view.getContext(), CashInHandActivity.class));
                    }
                } else {
                    Toast.makeText(getContext(), "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvHighestDuesFromStudentSeeAll:
                if (isFeesHidden) break;
                if (hdList != null && hdList.size() > 0) {
                    Intent HighestDuesIntent = new Intent(view.getContext(), HighestDuesStudentsListActivity.class);
                    if (schoolId > 0)
                        HighestDuesIntent.putExtra("schoolId", String.valueOf(schoolId));
                    else {
                        HighestDuesIntent.putExtra("schoolId", AppModel.getInstance().getuserSchoolIDS(view.getContext()));
//                    HighestDuesIntent.putExtra("schoolId", schoolId);
                    }
                    startActivity(HighestDuesIntent);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_thisSession) {
            filterReceivableBy = "s";
            populateReceivablesList();
        } else if (checkedId == R.id.rb_thisMonth) {
            filterReceivableBy = "m";
            populateReceivablesList();
        } else if (checkedId == R.id.rb_today) {
            filterReceivableBy = "t";
            populateReceivablesList();
        } else if (checkedId == R.id.rb_closing) {
            filterReceivableBy = "c";
            populateReceivablesList();
        }
    }
}
