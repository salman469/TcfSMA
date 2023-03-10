package com.tcf.sma.Activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Adapters.StudentFeeSetAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Interfaces.IProcessComplete;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class StudentFeeSetActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, IProcessComplete {
    public Spinner SchoolSpinner, ClassSectionSpinner;
    public TextView tvFeeTarget, tvAvgFees, tvDifferrence,tv_monthlyfee_total;
    public ClassSectionModel classSectionModel;
    View view;
    LinearLayout llResults;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    Button Search, btn_updateFee;
    RecyclerView rv_searchList;
    StudentFeeSetAdapter studentAdapter;
    ImageView iv_stdId, iv_grno, iv_stdname;
    LinearLayout ll_stdId, ll_grno, ll_stdname, ll_diff;
    int headerFailureCount = 0;
    private int SchoolSpinnerValue = 0, ClassSpinnerValue = 0, SectionSpinnerValue = 0;
    private int is_active = 1; // By default active
    private int schoolId = 0, schoolYearId = 1, academicSession_id;
    private ArrayList<StudentModel> studentList, previousStudentList = new ArrayList<>();
    private ArrayList<Double> feeList = null;

    private int roleID = 0;
    private LinearLayout fee_set_view_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_search);
        view = setActivityLayout(this, R.layout.activity_student_fee_set);
        setToolbar(getString(R.string.student_fee_set_text), this, false);

        init(view);
        working();
    }

    private void init(View view) {
        llResults = (LinearLayout) view.findViewById(R.id.llResults);
        btn_updateFee = (Button) view.findViewById(R.id.btn_updateFee);
        btn_updateFee.setOnClickListener(this);

        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);

        tvFeeTarget = view.findViewById(R.id.tv_feeTarget);
        tvAvgFees = view.findViewById(R.id.tv_avgFees);
        tvDifferrence = view.findViewById(R.id.tv_difference);
        tv_monthlyfee_total = view.findViewById(R.id.tv_monthlyfee_total);
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

        iv_stdId = (ImageView) view.findViewById(R.id.iv_stdId);
        iv_grno = (ImageView) view.findViewById(R.id.iv_grno);
        iv_stdname = (ImageView) view.findViewById(R.id.iv_stdname);


        ll_stdId = (LinearLayout) view.findViewById(R.id.ll_stdId);
        ll_grno = (LinearLayout) view.findViewById(R.id.ll_grno);
        ll_stdname = (LinearLayout) view.findViewById(R.id.ll_stdname);

        fee_set_view_header = (LinearLayout) view.findViewById(R.id.fee_set_view_header);

        ll_stdId.setOnClickListener(this);
        ll_grno.setOnClickListener(this);
        ll_stdname.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
//            SchoolSpinner.setEnabled(false);
//        }


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
    }

    /**
     * Method to initialize spinners with Meta Data
     * Created by Haseeb
     */
    private void initSpinners() {
        setSchoolAdapter();
        setClassSectionAdapter();
    }

    private void setSchoolAdapter(){
        SchoolModel schoolModel = new SchoolModel();
        ArrayList<SchoolModel> smList = new ArrayList<>();
        smList = AppModel.getInstance().getSchoolsForLoggedInUser(this);
        smList.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));
        smList = returnSchoolsThatDontHaveMonthlyFee(smList);
        schoolModel.setSchoolsList(smList);

        SchoolSpinnerValue = 0;

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);

        SchoolSpinner.setOnItemSelectedListener(this);
    }

    private void setClassSectionAdapter(){
        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);
    }

    private ArrayList<SchoolModel> returnSchoolsThatDontHaveMonthlyFee(ArrayList<SchoolModel> smList) {
        ArrayList<SchoolModel> schoolModels = new ArrayList<>();
        for (SchoolModel sm : smList){
            boolean isSchoolForFeeSet = FeesCollection.getInstance(this).isSchoolForFeeSet(sm);
            if ((DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(sm.getId()) && isSchoolForFeeSet)
                    || sm.getId() == 0){
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
                    studentList = DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, "", "", is_active, AppConstants.All, false);
                    studentList.forEach(studentModel -> studentModel.setMonthlyfee(String.valueOf((int) studentModel.getActualFees())));
                    previousStudentList = DatabaseHelper.getInstance(this).getAllStudentsInSchoolUsing(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, "", "", is_active, AppConstants.All, false);
                    previousStudentList.forEach(studentModel -> studentModel.setMonthlyfee(String.valueOf((int) studentModel.getActualFees())));
                    if (studentList.size() > 0) {
                        setCollapsing(true);
                        setAdapter();
                        populateTotalMonthlyFees();
                        populateTargetAvgFeesAndDiff();
                    } else {
                        setCollapsing(false);
                        MessageBox("No results found");
                        clearList();
                    }

                }
                break;
            case R.id.btn_updateFee:
                updateFee();
                break;

        }

    }

    private void setAdapter() {
        studentAdapter = new StudentFeeSetAdapter(studentList, this, this, 0, ClassSpinnerValue, SectionSpinnerValue, SchoolSpinnerValue);
        rv_searchList.setAdapter(studentAdapter);
        llResults.setVisibility(View.VISIBLE);
        fee_set_view_header.setVisibility(View.VISIBLE);
    }

    private void clearList() {
        if (studentAdapter != null) {
            studentList = null;
            setAdapter();
            llResults.setVisibility(View.GONE);
            fee_set_view_header.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();

                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(StudentFeeSetActivity.this,
                        SchoolSpinnerValue);
                classSectionModel = new ClassSectionModel();
                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                schoolYearId = DatabaseHelper.getInstance(StudentFeeSetActivity.this).getSchoolYearId(schoolId);
                academicSession_id = DatabaseHelper.getInstance(StudentFeeSetActivity.this).getAcademicSessionId(schoolId);
                break;
            case R.id.spn_class_section_name:
                ClassSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                SectionSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
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
        model.setTotal_amount(totalAmount);
        return model;
    }

    private void enableUpdateFeeButton(boolean enabled) {
        btn_updateFee.setEnabled(enabled);
        btn_updateFee.setClickable(enabled);
    }

    public void updateFee() {
        if (checkChanges()) {
            enableUpdateFeeButton(false);
            if (validate()) {
                Toast.makeText(this, "Student count: " +
                        "" + studentList.size(), Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Are you sure, you want to update fees?");
                dialog.setCancelable(false);
                dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                    enableUpdateFeeButton(true);
                    AppModel.getInstance().showLoader(StudentFeeSetActivity.this, "Set Fees", "Updating Fees records...");
                    new Thread(() -> {
                        boolean isUpdated = FeesCollection.getInstance(StudentFeeSetActivity.this)
                                .updateStdMonthlyFees(studentList.stream()
                                        .filter(studentModel -> Strings.isEmptyOrWhitespace(studentModel.getUploadedOn()))
                                        .collect(Collectors.toList()));

                        runOnUiThread(() -> {
                            if (isUpdated) {
                                Search.performClick();
                                //Important when any change in table call this method
                                AppModel.getInstance().changeMenuPendingSyncCount(StudentFeeSetActivity.this, false);

                                Toast.makeText(StudentFeeSetActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                                AppModel.getInstance().hideLoader();
                                finish();
                            } else {
                                Toast.makeText(StudentFeeSetActivity.this, "Error Occurred while inserting some records.", Toast.LENGTH_SHORT).show();
                                enableUpdateFeeButton(true);
                                AppModel.getInstance().hideLoader();
                            }
                        });
                        dialogInterface.dismiss();
                    }).start();

                });
                dialog.setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    enableUpdateFeeButton(true);
                });
                dialog.show();
            } else {
                enableUpdateFeeButton(true);
            }
        }else {
            ShowToast("Nothing is changed!");
        }
    }

    private boolean validate() {
        studentAdapter.validate();
        int error = 0;
        int checkMonthlyfeeError = 0;
        for (StudentModel sm : studentList) {
            if (Strings.isEmptyOrWhitespace(sm.getMonthlyfee())) {
                checkMonthlyfeeError++;
            } else if (!Strings.isEmptyOrWhitespace(sm.getMonthlyfee())
                    && Double.parseDouble(sm.getMonthlyfee()) < 10 || Double.parseDouble(sm.getMonthlyfee()) > 1500) {
                checkMonthlyfeeError++;
            }
        }
        if (checkMonthlyfeeError > 0) {
            showDialog("Please enter all Monthly fees. Fees should be greater than 10 and less than 1500");
            error++;
        }
        return error == 0;
    }

    private boolean checkChanges() {
        int i = 0;
//        List<StudentModel> tempStudentModelList = studentList.stream().filter(studentModel -> previousStudentList.stream()
//                .anyMatch(prevStudentModel -> prevStudentModel.getMonthlyfee().equals(studentModel.getMonthlyfee()))).collect(Collectors.toList());
//        if (!CollectionUtils.isEmpty(tempStudentModelList) && tempStudentModelList.size() > 0){
//            i++;
//        }

        for (int j = 0; j < previousStudentList.size(); j++) {
            StudentModel newModel = studentList.get(j);
            StudentModel prevModel = previousStudentList.get(j);
            if (!newModel.getMonthlyfee().equals(prevModel.getMonthlyfee())){
                i++;
                newModel.setUploadedOn((String) null);
            }
        }
        return i > 0;
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
                enableUpdateFeeButton(true);
            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit? entered data may get lost, Please submit before exiting.");
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            StudentFeeSetActivity.super.onBackPressed();
        });
        builder.create().show();
    }

    @Override
    public void onProcessCompleted() {
        if (studentList != null && studentList.size() > 0){
            populateTotalMonthlyFees();
            populateTargetAvgFeesAndDiff();
        }
    }

    private void populateTotalMonthlyFees() {
        long totalMonthlyFee = 0;

        totalMonthlyFee = studentList.stream().mapToLong(s-> Integer.parseInt(s.getMonthlyfee())).sum();
        tv_monthlyfee_total.setText(""+totalMonthlyFee);
    }

    private void populateTargetAvgFeesAndDiff(){
        double difference=0,average=0;
        SchoolModel sm = DatabaseHelper.getInstance(this).getSchoolById(schoolId);

        average = (studentList.stream().mapToDouble(s-> Integer.parseInt(s.getMonthlyfee())).sum()) / studentList.size();
        tvAvgFees.setText(String.valueOf((int) AppModel.getInstance().round(average, 0)));

        if(sm.getTarget_Amount()!=null && !sm.getTarget_Amount().isEmpty())
        {
            difference = average - Double.parseDouble(sm.getTarget_Amount());
            tvFeeTarget.setText(sm.getTarget_Amount());
        }else {
            difference = average - 0;
            tvFeeTarget.setText("0");
        }

        tvDifferrence.setText(String.valueOf((int) AppModel.getInstance().round(difference, 0)));
        if (difference <= 0) {
            ll_diff.setBackgroundColor(getResources().getColor(R.color.app_green));
        } else {
            ll_diff.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }
}