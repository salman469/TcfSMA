package com.tcf.sma.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Adapters.AttendanceAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarSchool;
import com.tcf.sma.Interfaces.OnButtonEnableDisableListener;
import com.tcf.sma.Managers.AttendanceDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendanceActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnButtonEnableDisableListener {
    public TextView TotalPresent;
    public TextView TotalAbsent;
    View view;
    Button bt_submit, Search;
    EditText et_gr_no, et_student_Name, et_date;
    String attendanceDate;
    //    public TextView TotalLeaves;
    LinearLayout ll1, ll_schoolSpinner;
    RecyclerView rv_attendance;
    AttendanceAdapter attendanceAdapter;
    //    ArrayList<AttendanceModel> attendanceList = new ArrayList<>();
//    AttendanceModel attendanceModel;
    Spinner SchoolSpinner, ClassSpinner, SectionSpinner, ClassSectionSpinner, spn_activity;
    int SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, SchoolClassid;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    AttendanceModel model = new AttendanceModel();
    List<HolidayCalendarSchoolModel> holidays;
    ClassSectionModel classSectionModel;
    private boolean isAttendanceAvailable = true, doUpdate = false;
    private ProgressDialog pd;
    //For pendingAttendance
    private boolean isFromAttendancePending = false;
    private int schoolId, schoolClassId;
    private String forDate = "";
    private boolean isSelected = false;
    private OnButtonEnableDisableListener onButtonEnableDisableListener;

    private static String formatDate(int year, int month, int day, boolean showMonthName) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf;
        if (showMonthName)
            sdf = new SimpleDateFormat("dd-MMM-yy");
        else
            sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    private static Date formatStringToDate(String strdate) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {

            return format.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_attendance);
        setToolbar("Attendance", this, false);
//        handleIntent(getIntent());
        init(view);

        handleIntent(getIntent());
        initSpinners();
        holidays = HolidayCalendarSchool.getInstance(this).getAllHolidays(String.valueOf(schoolId));

        if (schoolId != 0 && !forDate.isEmpty() && schoolClassId != 0) {
            for (ClassSectionModel csm : classSectionModel.getClassAndSectionsList()) {
                if (csm.getSchoolClassId() == schoolClassId) {
                    ClassSpinnerValue = csm.getClassId();
                    SectionSpinnerValue = csm.getSectionId();
                    break;
                }
            }
            Search.callOnClick();
        }

    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("schoolId")) {
                schoolId = intent.getIntExtra("schoolId", 0);
                isFromAttendancePending = true;
                populateSchoolSpinner(schoolId);
            } else {
//                schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
                populateSchoolSpinner(0);
            }
            if (intent.hasExtra("schoolClassId")) {
                schoolClassId = intent.getIntExtra("schoolClassId", 0);
            }
            if (intent.hasExtra("forDate")) {
                forDate = intent.getStringExtra("forDate");
                et_date.setText(AppModel.getInstance().convertDatetoFormat(forDate, "yyyy-MM-dd", "dd-MMM-yy"));
            }


        } else {
//            schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
            populateSchoolSpinner(0);
        }
    }

    private void setCollapsing(final boolean isCollapsable) {

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    private void populateSchoolSpinner(int SchoolID) {

        if (SchoolID == 0) {
            SchoolModel schoolModel = new SchoolModel();
            schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
//        schoolModel.getSchoolsList().add(0, new SchoolModel(0, "Select School"));
            SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
            SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SchoolSpinner.setAdapter(SchoolAdapter);
            int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
            if (indexOfSelectedSchool > -1) {
                SchoolSpinner.setSelection(indexOfSelectedSchool);
                isSelected = true;
            }
            SchoolSpinner.setOnItemSelectedListener(this);
            schoolId = ((SchoolModel) SchoolSpinner.getSelectedItem()).getId();
        } else {
            SchoolModel schoolModel = new SchoolModel();
            schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
            SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
            SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SchoolSpinner.setAdapter(SchoolAdapter);
            int selectedSchoolIndex = getSelectedSchoolPosition(schoolModel.getSchoolsList(), SchoolID);
            SchoolSpinner.setSelection(selectedSchoolIndex);
            SchoolSpinner.setOnItemSelectedListener(this);
            schoolId = SchoolID;
        }

    }

    private void init(View view) {
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String date = df.format(CalendarsModel.getInstance().getTime());
        onButtonEnableDisableListener = this;
        String date;
        if (isFromAttendancePending)
            date = AppModel.getInstance().convertDatetoFormat(forDate, "yyyy-MM-dd", "dd-MM-yyyy");
        else
            date = AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy");
        et_gr_no = (EditText) view.findViewById(R.id.et_GrNo);
        et_student_Name = (EditText) view.findViewById(R.id.et_student_name);
        et_gr_no.setVisibility(View.GONE);
        et_student_Name.setVisibility(View.GONE);

        setCollapsing(false);

        et_date = (EditText) view.findViewById(R.id.et_date);
//        et_date.setText(date);
        et_date.setText(AppModel.getInstance().convertDatetoFormat(date, "dd-MM-yyyy", "dd-MMM-yy"));
        et_date.setOnClickListener(this);
        et_date.setVisibility(View.VISIBLE);

        bt_submit = (Button) view.findViewById(R.id.bt_submit);

        Search = (Button) view.findViewById(R.id.btnsearch);
        bt_submit.setOnClickListener(this);
        Search.setOnClickListener(this);

        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
//        spn_activity = (Spinner)view.findViewById(R.id.spn_activity);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);


        TotalPresent = (TextView) view.findViewById(R.id.tv_total_presents);
        TotalAbsent = (TextView) view.findViewById(R.id.tv_total_absents);
//        TotalLeaves = (TextView) view.findViewById(R.id.tv_total_leaves);
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);

        rv_attendance = (RecyclerView) view.findViewById(R.id.rv_attendance);
        rv_attendance.setNestedScrollingEnabled(false);
        rv_attendance.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_attendance.setLayoutManager(llm);

        ll_schoolSpinner = (LinearLayout) view.findViewById(R.id.ll_schoolSpinner);
        ll_schoolSpinner.setVisibility(View.VISIBLE);


    }

    /**
     * Method to initialize spinners with Meta Data
     * Created by Haseeb
     */
    private void initSpinners() {

        // Used for Area Manager
//        SchoolModel schoolModel = new SchoolModel();
//        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllSchools());
//        schoolModel.getSchoolsList().add(0, new SchoolModel(0, "Select School"));
//        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
//        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SchoolSpinner.setAdapter(SchoolAdapter);
//        SchoolSpinner.setOnItemSelectedListener(this);


        //Used for principal
        SchoolModel schoolModel = new SchoolModel();
        schoolModel = DatabaseHelper.getInstance(this).getSchoolById(schoolId);
        ArrayList<SchoolModel> smList = new ArrayList<>();
        smList.add(schoolModel);
        schoolModel.setSchoolsList(smList);
        SchoolSpinnerValue = schoolModel.getId();


        ClassModel classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, "Select Class"));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);

        SectionModel sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, "Select Section"));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);

        populateSchoolClassSpinner(schoolModel);

//        spn_activity.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.schoolActivity)));
    }

    private int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
        int index = 0;
        for (SchoolModel model : schoolModels) {
            if (model.getId() == appSchoolId)
                return index;
            index++;
        }
        return index;
    }

    private void populateSchoolClassSpinner(SchoolModel schoolModel) {

        classSectionModel = new ClassSectionModel();
        if (isFromAttendancePending)
            classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
        else
            classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolModel.getId())));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);
        if (isFromAttendancePending) {
            int index = getSelectedClassSectionPosition(classSectionModel.getClassAndSectionsList(), schoolClassId);
            ClassSectionSpinner.setSelection(index);
        }
    }

    private int getSelectedClassSectionPosition(ArrayList<ClassSectionModel> csmList, int schoolClassId) {
        int index = 0;
        for (ClassSectionModel model : csmList) {
            if (model.getSchoolClassId() == schoolClassId)
                return index;
            index++;
        }
        return index;
    }

    public void setTotalAttendancecountText() {
        int p = 0, a = 0;
        try {
            for (int i = 0; i < AttendanceAdapter.am.size(); i++) {
                if (AttendanceAdapter.am.get(i).getAttendance() != null) {
                    switch (AttendanceAdapter.am.get(i).getAttendance()) {
                        case "p":
                            p++;
                            break;
                        case "a":
                            a++;
                            break;
                    }
                }
            }
            TotalPresent.setText("" + p);
            TotalAbsent.setText("" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
//                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);
                if (isSelected) {
                    classSectionModel = new ClassSectionModel();
                    classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
                    classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                    ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                    ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                    ClassSectionSpinner.setOnItemSelectedListener(this);

                } else {
                    isSelected = true;
                }

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

                break;
            case R.id.spn_section:
                SectionSpinnerValue = ((SectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                hideViews();
                break;
            case R.id.spn_class_name:
                hideViews();
                ClassSpinnerValue = ((ClassModel) adapterView.getItemAtPosition(position)).getClassId();
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
    public void onClick(View view) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        switch (view.getId()) {
            case R.id.bt_submit:
                Log.i("submit","Count");
                if (AttendanceAdapter.am.size() > 0) {
                    setSubmitButtonEnabled(false);
                    String date = et_date.getText().toString().trim();
//                    attendanceDate = SurveyAppModel.getInstance().convertDatetoFormat(date, "dd-MM-yyyy", "yyyy-MM-dd");
//                    attendanceDate = AppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd hh:mm:ss a");
                    attendanceDate = AppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd");
                    AttendanceDialogManager adm = new AttendanceDialogManager(this, attendanceDate, schoolId, ClassSpinnerValue, SectionSpinnerValue, doUpdate, isFromAttendancePending, onButtonEnableDisableListener);
                    adm.setCancelable(false);
                    adm.show();
                }
                break;
            case R.id.btnsearch:


                CalendarsModel cm = DatabaseHelper.getInstance(AttendanceActivity.this).checkDateForAttendance(
                        AppModel.getInstance().convertDatetoFormat(et_date.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"), schoolId + "");

                if (ClassSectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_class_section))) {
                    MessageBox("Please select search parameter!");
                    hideViews();

                } else {

                    if (cm != null) {
                        try {
                            MessageBox("Selected Date is " + cm.getC_Holiday_Type_Name() + " due to " + cm.getC_Activity_Name() + ".\nCannot Mark Attendance on this date!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        String date = et_date.getText().toString().trim();
//                    attendanceDate = SurveyAppModel.getInstance().convertDatetoFormat(date, "dd-MM-yyyy", "yyyy-MM-dd");
                        attendanceDate = AppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd");
                        handleIntenseWorking working = new handleIntenseWorking();
                        working.execute();
                    }
                }

//                if (ClassSpinner.getSelectedItem().toString().equals(getString(R.string.select_class)) ||
//                        (SectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_section)))) {
//                    MessageBox("Please select class and section both!");
//                    hideViews();
//
//                } else {
//                    String date = et_date.getText().toString().trim();
////                    attendanceDate = SurveyAppModel.getInstance().convertDatetoFormat(date, "dd-MM-yyyy", "yyyy-MM-dd");
//                    attendanceDate = SurveyAppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd");
//                    handleIntenseWorking working = new handleIntenseWorking();
//                    working.execute();
//                }
                break;
            case R.id.et_date:
                Datepicker(this, et_date);
                hideViews();
                break;
        }

    }

    public void Datepicker(Activity context, final EditText tv_picker) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c1 = Calendar.getInstance();
                c1.set(year, monthOfYear, dayOfMonth);
                int dayOfWeek = c1.get(Calendar.DAY_OF_WEEK);
//                int dayOfWeek = c.get(CalendarsModel.DAY_OF_WEEK);

//                if (dayOfWeek == Calendar.SUNDAY)
//                    MessageBox("Cannot mark attendance for weekdays");
//                else if (selectedDateIsHoliday(c1)) {
//                    MessageBox("Cannot mark attendance for holidays");
//                } else
                tv_picker.setText(formatDate(year, monthOfYear, dayOfMonth, true));
                hideViews();
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }

    /**
     * Added By Mohammad Haseeb
     * Method to check if selected date is a holiday or not
     *
     * @param calendar
     * @return
     */
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

//            startDate = formatStringToDate(model.getStartDate());
//            endDate = formatStringToDate(model.getEndDate());
            date = formatStringToDate(selectDate);

            if (date.after(cal1.getTime()) && date.before(cal2.getTime()))
                return true;
        }
        return false;
    }

    public void setAdapter() {
        try {
            model = DatabaseHelper.getInstance(this).getLastAttendanceRecord();
            SchoolClassid = DatabaseHelper.getInstance(AttendanceActivity.this).getSchoolClassByIds(schoolId, ClassSpinnerValue, SectionSpinnerValue).getId();
            if (SchoolClassid > 0 && DatabaseHelper.getInstance(AttendanceActivity.this).FindAttendance(AppModel.getInstance().convertDatetoFormat(et_date.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"), SchoolClassid)) {
                bt_submit.setText("Update");
                doUpdate = true;
            } else {
                bt_submit.setText("Submit");
                doUpdate = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        isAttendanceAvailable = DatabaseHelper.getInstance(this).isAttendanceAvailable(attendanceDate);
        if (model != null) {
            if (AppModel.getInstance().daysBetween(AppModel.getInstance().StringToCalObject(AppModel.getInstance().getDate())
                    , AppModel.getInstance().StringToCalObject(attendanceDate)) > 30) {
                isAttendanceAvailable = false;
                MessageBox("Attendance records older than 30 days cannot be updated.");
            } else {
                isAttendanceAvailable = true;
            }
            if (!isAttendanceAvailable) {
                bt_submit.setVisibility(View.GONE);
            }
        }
        attendanceAdapter = new AttendanceAdapter(AttendanceModel.getInstance().getAmList(), this, isAttendanceAvailable);
        rv_attendance.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                rv_attendance.removeOnLayoutChangeListener(this);
                Log.e("tag", "updated " + AttendanceModel.getInstance().getAmList().size());
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });
        rv_attendance.setAdapter(attendanceAdapter);

        showViews();


    }

    public void hideViews() {
        ll1.setVisibility(View.GONE);
        bt_submit.setVisibility(View.GONE);
    }

    public void showViews() {

        ll1.setVisibility(View.VISIBLE);
        if (isAttendanceAvailable) {
            bt_submit.setVisibility(View.VISIBLE);
        }
    }

    private void clearList() {
        if (attendanceAdapter != null) {
            AttendanceModel.getInstance().setAmList(null);
//            setAdapter();

        }
    }

    private void setSubmitButtonEnabled(boolean enabled){
        bt_submit.setEnabled(enabled);
        bt_submit.setClickable(enabled);
    }

    @Override
    public void onSubmitClicked() {
        setSubmitButtonEnabled(true);
    }

    public class handleIntenseWorking extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
//          pd = ProgressDialog.show(AttendanceActivity.this, "", "Loading Attendance", false, false);
            pd = new ProgressDialog(AttendanceActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Loading Attendance...");
            pd.show();

        }

        @Override
        protected String doInBackground(String[] objects) {
            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(AttendanceActivity.this).
                    getAllSearchedStudents(schoolId, ClassSpinnerValue, SectionSpinnerValue, "", ""));

            ArrayList<StudentModel> attendanceStudentList;
            attendanceStudentList = DatabaseHelper.getInstance(AttendanceActivity.this).
                    getAttendanceForStudents(schoolId, ClassSpinnerValue, SectionSpinnerValue, attendanceDate);


            if (attendanceStudentList != null && attendanceStudentList.size() > 0) {
                for (int i = 0; i < attendanceStudentList.size(); i++) {
                    for (int j = 0; j < StudentModel.getInstance().getStudentsList().size(); j++) {
                        //if (attendanceStudentList.get(i).getId() == StudentModel.getInstance().getStudentsList().get(j).getId() && !StudentModel.getInstance().getStudentsList().contains(attendanceStudentList.get(i))) {
                        if (attendanceStudentList.get(i).getId() == StudentModel.getInstance().getStudentsList().get(j).getId() && StudentModel.getInstance().getStudentsList().contains(attendanceStudentList.get(i))) {
                            StudentModel.getInstance().getStudentsList().set(j, attendanceStudentList.get(i));
                            Log.d("duplication", "Student ID ==>  " + attendanceStudentList.get(i).getId() + " ==> " + StudentModel.getInstance().getStudentsList().get(j).getId());
                            Log.d("duplication", "GR Number ==>   " + attendanceStudentList.get(i).getGrNo() + " ==> " + StudentModel.getInstance().getStudentsList().get(j).getGrNo());

                        }
                    }
                }
            }


            AttendanceModel.getInstance().amList = new ArrayList<>();
            for (int i = 0; i < StudentModel.getInstance().getStudentsList().size(); i++) {
                AttendanceModel.getInstance().aModel = new AttendanceModel();
                if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() != null) {
                    switch (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus()) {
                        case "1":
                            AttendanceModel.getInstance().getaModel().setAttendance("a");
                            StudentModel.getInstance().getStudentsList().get(i).setAttendanceStatus("a");
                            break;
                        case "p":
                            AttendanceModel.getInstance().getaModel().setAttendance("p");
                            StudentModel.getInstance().getStudentsList().get(i).setAttendanceStatus("p");
                            break;
                    }
                } else {
                    AttendanceModel.getInstance().getaModel().setAttendance("p");
                    StudentModel.getInstance().getStudentsList().get(i).setAttendanceStatus("p");
                }
                AttendanceModel.getInstance().getAmList().add(AttendanceModel.getInstance().getaModel());
            }

            if (StudentModel.getInstance().getStudentsList().size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCollapsing(true);
                        setAdapter();
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCollapsing(false);
                        MessageBox("No results found");
                        hideViews();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                            pd = null;
                        }

                    }
                });

            }
            return null;
        }
    }
}
