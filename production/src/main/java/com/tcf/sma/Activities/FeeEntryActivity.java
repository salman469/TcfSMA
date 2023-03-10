package com.tcf.sma.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Adapters.StudentFeeEntryAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Interfaces.IProcessComplete;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FeeEntryActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, IProcessComplete {
    public Spinner SchoolSpinner, ClassSpinner, SectionSpinner, ClassSectionSpinner, sp_filterBy;
    public TextView tvFeeTarget, tvAvgFees, tvDifferrence,tv_monthlyfee_total,tv_op_fee_total;
    public ClassModel classModel;
    public SectionModel sectionModel;
    public ClassSectionModel classSectionModel;
    View view;
    LinearLayout llResults, ll_filterBy, ll_save_fee;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    Button Search, btn_saveFee;
    RecyclerView rv_searchList;
    StudentFeeEntryAdapter studentAdapter;
    ImageView iv_stdId, iv_grno, iv_stdname;
    LinearLayout ll_stdId, ll_grno, ll_stdname, ll_admDate, ll_diff;
    int headerFailureCount = 0;
    private int SchoolSpinnerValue = 0, ClassSpinnerValue = 0, SectionSpinnerValue = 0;
    private RadioGroup mFilterRadioGroup;
    private String admission_type = null;
    private int is_active = 1; // By default active
    private int schoolId = 0, schoolYearId = 1, academicSession_id;
    private ArrayList<StudentModel> studentList;
    private ArrayList<Double> feeList = null;

    private int roleID = 0;
    private LinearLayout fee_entry_view_header;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_search);
        view = setActivityLayout(this, R.layout.activity_fee_entry);
        setToolbar("Fee Entry", this, false);

        init(view);
        working();
    }

    private void init(View view) {
        llResults = (LinearLayout) view.findViewById(R.id.llResults);
        btn_saveFee = (Button) view.findViewById(R.id.btn_saveFee);
        btn_saveFee.setOnClickListener(this);

        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        tvFeeTarget = view.findViewById(R.id.tv_feeTarget);
        tvAvgFees = view.findViewById(R.id.tv_avgFees);
        tvDifferrence = view.findViewById(R.id.tv_difference);
        tv_monthlyfee_total = view.findViewById(R.id.tv_monthlyfee_total);
        tv_op_fee_total = view.findViewById(R.id.tv_op_fee_total);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
        Search = (Button) view.findViewById(R.id.btnsearch);
        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);
        Search.setOnClickListener(this);
        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

        ll_diff = findViewById(R.id.ll_diff);

        mFilterRadioGroup = (RadioGroup) view.findViewById(R.id.RadioGroup);
        mFilterRadioGroup.setOnCheckedChangeListener(this);

        ll_filterBy = (LinearLayout) view.findViewById(R.id.ll_filterBy);
        sp_filterBy = (Spinner) view.findViewById(R.id.sp_filterBy);
        sp_filterBy.setOnItemSelectedListener(this);

        iv_stdId = (ImageView) view.findViewById(R.id.iv_stdId);
        iv_grno = (ImageView) view.findViewById(R.id.iv_grno);
        iv_stdname = (ImageView) view.findViewById(R.id.iv_stdname);


        ll_stdId = (LinearLayout) view.findViewById(R.id.ll_stdId);
        ll_grno = (LinearLayout) view.findViewById(R.id.ll_grno);
        ll_stdname = (LinearLayout) view.findViewById(R.id.ll_stdname);
        ll_admDate = (LinearLayout) view.findViewById(R.id.ll_admDate);

        fee_entry_view_header = (LinearLayout) view.findViewById(R.id.fee_entry_view_header);

        ll_stdId.setOnClickListener(this);
        ll_grno.setOnClickListener(this);
        ll_stdname.setOnClickListener(this);
        ll_admDate.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            SchoolSpinner.setEnabled(false);
        }


        setCollapsing(false);
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    private void working() {
        initSpinners();
        populateFilterSpinner();
    }

    private void populateFilterSpinner() {
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.studentInfo));

        sp_filterBy.setAdapter(filterAdapter);
    }

    /**
     * Method to initialize spinners with Meta Data
     * Created by Haseeb
     */
    private void initSpinners() {


        //Used for principal
        SchoolModel schoolModel = new SchoolModel();
        ArrayList<SchoolModel> smList = new ArrayList<>();
        smList = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);
//        smList = SurveyAppModel.getInstance().getSchoolsForLoggedInUser(this);
//        smList = DatabaseHelper.getInstance(this).getAllUserSchools();
        smList.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        smList = removeSchoolsThatDontHaveMonthlyFee(smList);
//        smList.add(schoolModel);
        schoolModel.setSchoolsList(smList);
        SchoolSpinnerValue = 0;

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);

        SchoolSpinner.setOnItemSelectedListener(this);


        classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, getString(R.string.select_class)));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);


        sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, getString(R.string.select_section)));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);


        classSectionModel = new ClassSectionModel();
//        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.setClassAndSectionsList(FeesCollection.getInstance(this).getClassSectionForFeeEntryBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);


        if (admission_type == null)
            admission_type = AppConstants.All;
    }

    private ArrayList<SchoolModel> removeSchoolsThatDontHaveMonthlyFee(ArrayList<SchoolModel> smList) {
        ArrayList<SchoolModel> schoolModels = new ArrayList<>();
        for (SchoolModel sm : smList){
            boolean isSchoolForFeeEntry = FeesCollection.getInstance(this).isSchoolForFeeEntry(sm);
            if (isSchoolForFeeEntry || sm.getId() == 0){
                schoolModels.add(sm);
            }
        }

        return schoolModels;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:

                if (SchoolSpinnerValue == 0) {
                    MessageBox("Please select search parameters!");
                    clearList();
                } else if (ClassSpinnerValue <= 0 && SectionSpinnerValue <= 0) {
                    MessageBox("Please select search parameters!");
                    clearList();
                } else {
                    studentList = DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, "", "", is_active, admission_type, true);
                    if (studentList.size() > 0) {
                        setCollapsing(true);
                        setAdapter();
                        populateHeaders();
                    } else {
                        setCollapsing(false);
                        MessageBox("No results found");
                        clearList();
                    }

                }
                break;
            case R.id.ll_admDate:
                showDialog("Please enter all previous dues to date, of each Student, including this month's fee if due");
                break;
            case R.id.btn_saveFee:
                saveFee();
                break;

        }

    }

    public double getFeeTargetAverage(WeakReference<Context> mContext, int schoolId) {
//        if (feeList == null)
//            feeList = DatabaseHelper.getInstance(mContext.get()).getStudentsFees(schoolId, 0, 0, true);
//
////            int totalStudents = DatabaseHelper.getInstance(mContext.get()).getStudentsFees(schoolId,ClassSpinnerValue,SectionSpinnerValue,false).size();
//        ArrayList<StudentModel> studentListHavingFeeEntry = DatabaseHelper.getInstance(this).getAllStudentsInSchoolForAvgFee(SchoolSpinnerValue);
        double sum = 0.0;
        if (studentList == null || studentList.size() == 0)
            return sum;
        for (int i = 0; i < studentList.size(); i++) {
            sum += studentList.get(i).getActualFees();
        }

        return sum / studentList.size();

//        int count = feeList.size();
//        if (!feeList.isEmpty()) {
//            for (Double mark : feeList) {
//                sum += mark;
//            }
//            if (!studentListHavingFeeEntry.isEmpty()) {
//                return sum / studentList.size();
//            }
////        if (!studentList.isEmpty()) {
////            for (StudentModel sm : studentList) {
////                if (sm.getMonthlyfee() != null && Double.parseDouble(sm.getMonthlyfee()) > 0) {
////                    sum += Double.parseDouble(sm.getMonthlyfee());
////                    count++;
////                }
////            }
////        }
////        return sum / count;
//
//        }

//        return sum;

    }

    private void populateHeaders() {
        double difference=0,average=0;
        SchoolModel sm = DatabaseHelper.getInstance(this).getSchoolById(schoolId);
        average = getFeeTargetAverage(new WeakReference<>(this), schoolId);
        if(sm.getTarget_Amount()!=null && !sm.getTarget_Amount().isEmpty())
        {
            difference = Double.parseDouble(sm.getTarget_Amount()) - average;
            tvFeeTarget.setText(sm.getTarget_Amount());
        }else {
            difference = 0 - average;
            tvFeeTarget.setText("0");
        }
        tvAvgFees.setText(String.valueOf((int) round(average, 0)));
        tvDifferrence.setText(String.valueOf((int) round(difference, 0)));
        if (difference <= 0) {
            ll_diff.setBackgroundColor(getResources().getColor(R.color.app_green));
        } else {
            ll_diff.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    private void setAdapter() {
        studentAdapter = new StudentFeeEntryAdapter(studentList, this, this, 0, ClassSpinnerValue, SectionSpinnerValue, SchoolSpinnerValue);
        rv_searchList.setAdapter(studentAdapter);
        llResults.setVisibility(View.VISIBLE);
        fee_entry_view_header.setVisibility(View.VISIBLE);
//        ll_filterBy.setVisibility(View.VISIBLE);
    }

    private void clearList() {
        if (studentAdapter != null) {
            studentList = null;
            setAdapter();
            llResults.setVisibility(View.GONE);
            fee_entry_view_header.setVisibility(View.GONE);
//            ll_filterBy.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();

                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(FeeEntryActivity.this,
                        SchoolSpinnerValue);
                classSectionModel = new ClassSectionModel();
//                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.setClassAndSectionsList(FeesCollection.getInstance(this).getClassSectionForFeeEntryBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                schoolYearId = DatabaseHelper.getInstance(FeeEntryActivity.this).getSchoolYearId(schoolId);
                academicSession_id = DatabaseHelper.getInstance(FeeEntryActivity.this).getAcademicSessionId(schoolId);
//                ClassSectionSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.spn_section:
                SectionSpinnerValue = ((SectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
            case R.id.spn_class_name:
                ClassSpinnerValue = ((ClassModel) adapterView.getItemAtPosition(position)).getClassId();
                break;
            case R.id.spn_class_section_name:
                ClassSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                SectionSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
            case R.id.sp_filterBy:
//                filterBy = sp_filterBy.getSelectedItem().toString();
//
//                sortInAscStudent();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public String getCurrentDate() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date date = Calendar.getInstance().getTime();
        return sdf1.format(date);
    }

    private FeesHeaderModel getFeesDue(double totalAmount, StudentModel sModel) {


        int userId = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId();
        FeesHeaderModel model = new FeesHeaderModel();

        //Category 1 for Invoice
        model.setCategory_id(3);
        model.setFor_date(getCurrentDate());
        model.setStudentId(sModel.getId());
        model.setCreatedOn(getCurrentDate());
        model.setFor_date(getCurrentDate());
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setSchool_id(schoolId);
        model.setSchoolClassId(sModel.getSchoolClassId());
        model.setCreatedBy(userId);
        model.setDeviceId(AppModel.getInstance().getDeviceId(this));
        model.setTransactionType_id(1);
//        model.setCreated_ref_id(et_receiptNo.getText().toString());

        model.setTotal_amount(totalAmount);
//        model.setFeeType_id(viewReceivablesModelsCorrection.get(i).getFeeTypeId());
//            model.setCreated_from("R");
        return model;
    }

    private void enableSaveFeeButton(boolean enabled) {
        btn_saveFee.setEnabled(enabled);
        btn_saveFee.setClickable(enabled);
    }

    public void saveFee() {
        enableSaveFeeButton(false);

        if (validate()) {
            Toast.makeText(this, "Student count: " +
                    "" + studentList.size(), Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Once the fee is saved, you are not allowed to edit it again. Are you sure, you want to save it?");
            dialog.setCancelable(false);
            dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                AppModel.getInstance().showLoader(FeeEntryActivity.this, "Fee Entry", "Adding Fees records...");
                new Thread(() -> {
//                    FeesCollection.getInstance(FeeEntryActivity.this).updateStudentsMonthlyFees(studentList);
                    for (StudentModel model : studentList) {
                        if (model != null) {
                            long feesHeaderId = FeesCollection.getInstance(FeeEntryActivity.this).insertFeesHeader(getFeesDue(Double.parseDouble(model.getEntryFee()), model));
                            if (feesHeaderId > 0) {
                                ViewReceivablesModels vRCV = new ViewReceivablesModels();
                                vRCV.setFeeTypeId(3);
                                vRCV.setTodaySales(String.valueOf(model.getEntryFee()));
                                FeesCollection.getInstance(FeeEntryActivity.this).insertFeesDetail(feesHeaderId, vRCV);
                            } else {
                                headerFailureCount++;
                            }
                        }
                    }
                    runOnUiThread(() -> {
                        if (headerFailureCount == 0) {
                            //Important when any change in table call this method
                            AppModel.getInstance().changeMenuPendingSyncCount(FeeEntryActivity.this, true);

                            Toast.makeText(FeeEntryActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FeeEntryActivity.this, "Error Occurred while inserting some records.", Toast.LENGTH_SHORT).show();
                            enableSaveFeeButton(true);
                            AppModel.getInstance().hideLoader();
                        }
                    });
                    dialogInterface.dismiss();
                }).start();

            });
            dialog.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                enableSaveFeeButton(true);
            });
            dialog.show();
        } else {
//            enableSaveFeeButton(true);
        }
    }

    private boolean validate() {
        studentAdapter.validate();
        int error = 0;
//        int checkMonthlyfeeError = 0;
        int checkOpFeeError = 0;
        for (StudentModel sm : studentList) {
//            if (sm.getMonthlyfee() == null || sm.getMonthlyfee() != null && sm.getMonthlyfee().isEmpty()) {
//                checkMonthlyfeeError++;
//            } else if (sm.getMonthlyfee() != null && Double.parseDouble(sm.getMonthlyfee()) < 10 || Double.parseDouble(sm.getMonthlyfee()) > 1500) {
//                checkMonthlyfeeError++;
//            }
            if (sm.getEntryFee() == null || sm.getEntryFee() != null && sm.getEntryFee().isEmpty()) {
                checkOpFeeError++;
            }
        }

        if (checkOpFeeError > 0) {
            showDialog("Please enter all the Opening balances for the students. Any of the opening balance field cannot be empty.");
            error++;
        }
//        if (checkMonthlyfeeError > 0 && checkOpFeeError > 0) {
//            showDialog("Monthly Fees and Opening balance cannot be empty. Monthly Fees should be greater than 10 and less than 1500, opening balance should be in between -7000 to 13000");
//            error++;
//        } else {
//            if (checkMonthlyfeeError > 0) {
//                showDialog("Please enter all Monthly fees. Fees should be greater than 10 and less than 1500");
//                error++;
//            }
//            if (checkOpFeeError > 0) {
//                showDialog("Please enter all the Opening balances for the students. Any of the opening balance field cannot be empty.");
//                error++;
//            }
//        }
//        SurveyAppModel.getInstance().hideLoader();
        return error == 0;
    }

    private void showDialog(String message) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                enableSaveFeeButton(true);
            }
        });
        dialog.show();

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        if (checkedId == R.id.rbAll) {
            admission_type = AppConstants.All;
        } else if (checkedId == R.id.rbNewAdmissions) {
            admission_type = AppConstants.NewAdmissions;
        } else if (checkedId == R.id.rbReadmissions) {
            admission_type = AppConstants.Readmissions;
        } else if (checkedId == R.id.rbPromotions) {
            admission_type = AppConstants.Promotions;
        } else if (checkedId == R.id.rbGraduations) {
            admission_type = AppConstants.Graduations;
        }
    }

//    private boolean validate(){
//        int error = 0;
//        if ()
//
//        return error == 0;
//    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit? entered data may get lost, Please submit before exiting.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FeeEntryActivity.super.onBackPressed();
            }
        });
        builder.create().show();
    }

    @Override
    public void onProcessCompleted() {
        populateHeaders();
        if (studentList != null && studentList.size() > 0){
            populateTotalEntry();
        }
    }

    private void populateTotalEntry() {
        long totalMonthlyFee = 0 , totalOpFee = 0;
        for (StudentModel model : studentList){
            totalMonthlyFee += model.getActualFees();
            totalOpFee += !Strings.isEmptyOrWhitespace(model.getEntryFee()) ? Integer.parseInt(model.getEntryFee()) : 0;
        }

        tv_monthlyfee_total.setText(""+totalMonthlyFee);
        tv_op_fee_total.setText(""+totalOpFee);
    }
}
