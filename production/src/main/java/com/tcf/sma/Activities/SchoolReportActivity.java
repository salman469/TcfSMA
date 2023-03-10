package com.tcf.sma.Activities;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.SchoolReportAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AttendancePercentage;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarSchool;
import com.tcf.sma.Managers.MonthYearPickerDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.Fees_Collection.AttendanceSummary;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;
import com.tcf.sma.Models.SchoolAttendanceReportModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SchoolReportActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    //    private int class_section_id=0;
    public TextView etMonthYear;
    View view;
    List<SchoolModel> schoolModels;
    CardView card3;
    Calendar cal;
    Button Search;
    String formatMonth, formatYear;
    List<SchoolAttendanceReportModel> schoolarm;
    private Spinner spnSchools, ClassSectionSpinner;
    private int schoolId = 0;
    private ClassSectionModel classSectionModel;
    private ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    private int ClassSpinnerValue = 0, SectionSpinnerValue = 0, schoolClass_id = 0;
    private RecyclerView rv_searchList;
    private SchoolReportAdapter schoolReportAdapter;
    private List<HolidayCalendarSchoolModel> holidays;
    private int totalworkingdays = 0;
    private int roleID = 0;

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
//        setContentView(R.layout.activity_school_report);

        view = setActivityLayout(this, R.layout.activity_school_report);
        setToolbar(getString(R.string.attendanceReport), this, false); //school Report is now attendance Report

        init(view);
        populateSpinners();
        initSpinners();
        working();
    }

    private void working() {
        cal = Calendar.getInstance();
    }

    private void setAdapter() {
        schoolReportAdapter = new SchoolReportAdapter(this, schoolarm);
        rv_searchList.setAdapter(schoolReportAdapter);
        card3.setVisibility(View.VISIBLE);
    }

    private void initSpinners() {
        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);

//        holidays = HolidayCalendarSchool.getInstance(SchoolReportActivity.this).
//                getAllHolidays(String.valueOf(SurveyAppModel.getInstance().getSelectedSchool(SchoolReportActivity.this)));
        holidays = HolidayCalendarSchool.getInstance(SchoolReportActivity.this).
                getAllHolidays(schoolId + "");
        setTotalWorkingDays();
    }

    private void setTotalWorkingDays() {

        //Getting start date and end date from current date
        String formattedDate = AppModel.getInstance().getCurrentDateTime("dd-MM-yyyy");
        Calendar current = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            current.setTime(sdf.parse(formattedDate));// all do
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Current date
        Date toDate = current.getTime();

        Calendar prevYearDate = Calendar.getInstance();
//        prevYearDate.setTime(toDate);
        prevYearDate.add(Calendar.YEAR, -1);
//        prevYearDate.set(current.DAY_OF_MONTH,1);


        //1 year back date from current date
        Date fromDate = prevYearDate.getTime();

        totalworkingdays = getWorkingDaysBetweenTwoDates(fromDate, toDate);
    }

    private void init(View view) {
        spnSchools = (Spinner) view.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);

        etMonthYear = (TextView) view.findViewById(R.id.etMonthYear);
        etMonthYear.setOnClickListener(this);

        etMonthYear.setText(AppModel.getInstance().getCurrentDateTime("MMMM - yyyy"));

        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);
//        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

//        llResults = (LinearLayout) view.findViewById(R.id.llResults);
        card3 = (CardView) view.findViewById(R.id.card3);
        Search = (Button) view.findViewById(R.id.btnsearch);
        Search.setOnClickListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }
    }

    private void populateSpinners() {
//        schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchools();

        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            spnSchools.setSelection(indexOfSelectedSchool);
            schoolId = AppModel.getInstance().getSpinnerSelectedSchool(this);
        } else if (indexOfSelectedSchool == -1) {
            schoolId = ((SchoolModel) spnSchools.getSelectedItem()).getId();
            int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
            spnSchools.setSelection(selectedSchoolIndex);
        }


    }

    private void clearList() {
        if (schoolReportAdapter != null) {
//            StudentModel.getInstance().setStudentsList(null);
            schoolarm.clear();
            setAdapter();
            card3.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etMonthYear:
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
//                pd.setListener(this,year,month);
                pd.setListener(this, cal);
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
                break;
            case R.id.btnsearch:

                SimpleDateFormat sdf1 = new SimpleDateFormat("M");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");

                formatMonth = sdf1.format(cal.getTime());
                formatYear = sdf2.format(cal.getTime());

                handleIntenseWorking handleWorking = new handleIntenseWorking();
                handleWorking.execute();

//                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
//
//                formatMonth = sdf1.format(cal.getTime());
//                formatYear = sdf2.format(cal.getTime());
//
//                handleWorking working = new handleWorking();
//                working.execute();
//
////                AttendanceModel.getInstance().setAmList(
////                        DatabaseHelper.getInstance(this).
////                                getAttendanceOfSchoolUsing(schoolId, class_section_id, formatMonth, formatYear));
////                if (AttendanceModel.getInstance().getAmList().size() > 0) {
////                    setAdapter();
////                    SurveyAppModel.getInstance().hideLoader();
////
////                } else {
////                    MessageBox("No results found");
////                    clearList();
////
////                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spnSchools:
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        schoolId);
                initSpinners();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

                break;
            case R.id.spn_class_section_name:
                ClassSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                SectionSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                schoolClass_id = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSchoolClassId();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
        etMonthYear.setText(AppConstants.monthArray[mm] + " - " + yy);
        cal.set(Calendar.MONTH, mm);
        cal.set(Calendar.YEAR, yy);
        cal.set(Calendar.DAY_OF_WEEK, dd);
    }

    public int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
//        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
//            return 0;
//        }
//
//        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
//            startCal.setTime(endDate);
//            endCal.setTime(startDate);
//        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !selectedDateIsHoliday(startCal)) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        return workDays;
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

    public class handleWorking extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            AppModel.getInstance().showLoader(SchoolReportActivity.this);
        }

        @Override
        protected void onPostExecute(String s) {
            AppModel.getInstance().hideLoader();
        }

        @Override
        protected String doInBackground(String[] objects) {
            List<StudentModel> studentsList = new ArrayList<>();
            List<SchoolModel> schoolModelList = DatabaseHelper.getInstance(SchoolReportActivity.this).getAllUserSchoolsById(schoolId);
            String academic_session = "";

            for (SchoolModel model : schoolModelList) {
                academic_session = model.getAcademic_session();
            }

            schoolarm = new ArrayList<>();
            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(SchoolReportActivity.this).
                    getAllSearchedStudents(schoolId, ClassSpinnerValue, SectionSpinnerValue, "", ""));

            ArrayList<StudentModel> attendanceStudentList;
            attendanceStudentList = DatabaseHelper.getInstance(SchoolReportActivity.this).
                    getAttendanceForSchoolReport(schoolId, ClassSpinnerValue, SectionSpinnerValue, formatMonth, formatYear);

            if (attendanceStudentList != null && attendanceStudentList.size() > 0) {
                for (int i = 0; i < attendanceStudentList.size(); i++) {
                    for (int j = 0; j < StudentModel.getInstance().getStudentsList().size(); j++) {
                        if (attendanceStudentList.get(i).getId() == StudentModel.getInstance().getStudentsList().get(j).getId() && !StudentModel.getInstance().getStudentsList().contains(attendanceStudentList.get(i))) {
//                            studentsList.add(attendanceStudentList.get(i));
                            StudentModel.getInstance().getStudentsList().set(j, attendanceStudentList.get(i));
                            Log.d("duplication", "Student ID ==>  " + attendanceStudentList.get(i).getId() + " ==> " + StudentModel.getInstance().getStudentsList().get(j).getId());
                            Log.d("duplication", "GR Number ==>   " + attendanceStudentList.get(i).getGrNo() + " ==> " + StudentModel.getInstance().getStudentsList().get(j).getGrNo());

                        }
                    }
                }
            }

            if (StudentModel.getInstance().getStudentsList().size() > 0 && StudentModel.getInstance().getStudentsList() != null) {
                if (ClassSpinnerValue > 0 && SectionSpinnerValue > 0) {
                    int countp = 0, countA = 0, countL = 0;
                    for (int i = 0; i < StudentModel.getInstance().getStudentsList().size(); i++) {
                        if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() == null) {
                            //Students are present at particular date
                            countp++;
                        } else if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() == "1") {
                            //Students are Absent at particular date
                            countA++;
                        } else if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() == "0") {
                            countL++;
                        }
                    }

                    SchoolAttendanceReportModel schoolAttendanceReportModel = new SchoolAttendanceReportModel();
//                    int totalDaysElapsed = DatabaseHelper.getInstance(SchoolReportActivity.this).getAllAttendanceHeaderCountmonthforClass(StudentModel.getInstance()
//                            .getStudentsList().get(0).getSchoolClassId(),formatMonth , formatYear);

                    double attendPerc = getAttendencePercentage(countp);
                    schoolAttendanceReportModel.setAttendance(attendPerc);
                    schoolAttendanceReportModel.setMonth(AppModel.getInstance().convertDatetoFormat(formatMonth, "MM", "MMM"));
                    schoolAttendanceReportModel.setYear(AppModel.getInstance().convertDatetoFormat(formatYear, "yyyy", "yy"));
                    String class_section = StudentModel.getInstance()
                            .getStudentsList().get(0).getCurrentClass() + " " +
                            StudentModel.getInstance().getStudentsList().get(0).getCurrentSection();
                    schoolAttendanceReportModel.setClass_section_name(class_section);

                    schoolAttendanceReportModel.setSession(academic_session);
                    schoolarm.add(schoolAttendanceReportModel);
//                    setAdapter();
                } else {
                    String class_section = "";

                    for (int h = 1; h < classSectionModel.getClassAndSectionsList().size(); h++) {
                        int countp = 0, countA = 0, countL = 0;
                        for (int i = 0; i < StudentModel.getInstance().getStudentsList().size(); i++) {
                            class_section = StudentModel.getInstance().getStudentsList().get(i).getCurrentClass()
                                    + " " + StudentModel.getInstance().getStudentsList().get(i).getCurrentSection();

                            if (classSectionModel.getClassAndSectionsList().get(h).getClass_section_name().equals(class_section)) {
                                if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() == null) {
                                    //Students are present at particular date
                                    countp++;
                                } else if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() == "1") {
                                    //Students are Absent at particular date
                                    countA++;
                                } else if (StudentModel.getInstance().getStudentsList().get(i).getAttendanceStatus() == "0") {
                                    countL++;
                                }
                            }
                        }
                        SchoolAttendanceReportModel schoolAttendanceReportModel = new SchoolAttendanceReportModel();
                        schoolAttendanceReportModel.setMonth(AppModel.getInstance().convertDatetoFormat(formatMonth, "MM", "MMM"));
                        schoolAttendanceReportModel.setYear(AppModel.getInstance().convertDatetoFormat(formatYear, "yyyy", "yy"));
                        schoolAttendanceReportModel.setClass_section_name(classSectionModel.getClassAndSectionsList().get(h).getClass_section_name());

//                        int totalDaysElapsed = DatabaseHelper.getInstance(SchoolReportActivity.this).
//                                getAllAttendanceHeaderCountmonthforClass(classSectionModel.getClassAndSectionsList().get(h).
//                                        getSchoolClassId(),formatMonth , formatYear);
                        double attendance = getAttendencePercentage(countp);
                        schoolAttendanceReportModel.setAttendance(attendance);
                        schoolAttendanceReportModel.setSession(academic_session);

                        schoolarm.add(schoolAttendanceReportModel);
//                        countp = 0;
                    }

//                    setAdapter();
                }
            }

            if (schoolarm.size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MessageBox("No results found");
                        clearList();
                    }
                });
            }

//            if (countp > 0) {
//                String attendPerc=getAttendencePercentage(countp);
//                SchoolAttendanceReportModel schoolAttendanceReportModel=new SchoolAttendanceReportModel();
//                schoolAttendanceReportModel.setAttendance(attendPerc);
//                schoolAttendanceReportModel.setMonth(formatMonth);
//                schoolAttendanceReportModel.setYear(formatYear);
//                schoolAttendanceReportModel.setClass_section_name();


//                List<AttendanceModel> amList=AttendanceModel.getInstance().getAmList();
//
//                SchoolAttendanceReportModel schoolAttendanceReportModel=new SchoolAttendanceReportModel();
//                for (AttendanceModel model : amList) {
//                    if (model.getAttendance().equals("p")) {
//                        schoolAttendanceReportModel.setMonth(formatMonth);
//                        schoolAttendanceReportModel.setYear(formatYear);
//                        schoolAttendanceReportModel.setClass_section_name(model.getClassSectionName());
//
//                        String attendance=getAttendencePercentage();
//                        schoolAttendanceReportModel.setAttendance(attendance);
//                    }
//                    schoolarm.add(schoolAttendanceReportModel);
//                }

//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MessageBox("No results found");
//                        SurveyAppModel.getInstance().hideLoader();
//                    }
//                });
//
//            }
            return null;
        }

        private double getAttendencePercentage(int countP) {
            double percentage = 0;
            percentage = countPercentages(totalworkingdays, countP);
//            percentage = countPercentages(days, countP);
            return percentage;
        }

        private double countPercentages(int days, int countP) {
            double percentage = 0;
            double dabsentPercent = 0, dleavePercent = 0, dpresentPercent = 0;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(1);
            dpresentPercent = ((countP / (double) days) * 100);
//        dabsentPercent = ((countA / (double) days) * 100);
//        dleavePercent = ((countL / (double) days) * 100);
            percentage = Double.parseDouble(df.format(dpresentPercent));
            if (Double.isNaN(percentage)) {
                percentage = 0;
            }

            return percentage;
        }
    }

    public class handleIntenseWorking extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            AppModel.getInstance().showLoader(SchoolReportActivity.this);
        }

        @Override
        protected void onPostExecute(String s) {
            AppModel.getInstance().hideLoader();
        }

        @Override
        protected String doInBackground(String[] objects) {
            try {
                String selectedDate = formatYear + "-" + formatMonth + "-" + "01";

                List<SchoolModel> schoolModelList = DatabaseHelper.getInstance(SchoolReportActivity.this).getAllUserSchoolsById(schoolId);
                int academicSessionId = 0;
                String academicSession = "";

                schoolarm = new ArrayList<>();

                for (SchoolModel model : schoolModelList) {
                    academicSessionId = model.getAcademic_Session_Id();
                    academicSession = model.getAcademic_session();
                }

                List<AttendanceSummary> attendanceSummary = AttendancePercentage.getInstance(current_activity)
                        .getAttendancePercentageforSelectedMonth(schoolId, schoolClass_id, academicSessionId, selectedDate);

                if (ClassSpinnerValue > 0 && SectionSpinnerValue > 0) {
                    for (AttendanceSummary model : attendanceSummary) {
                        ClassSectionModel sm = DatabaseHelper.getInstance(current_activity).getClassSectionBySchoolIdAndSchoolClassId(model.getSchoolId(),
                                model.getSchool_class_id());

                        SchoolAttendanceReportModel schoolAttendanceReportModel = new SchoolAttendanceReportModel();
                        schoolAttendanceReportModel.setMonth(AppModel.getInstance().convertDatetoFormat(formatMonth, "M", "MMM"));
                        schoolAttendanceReportModel.setYear(AppModel.getInstance().convertDatetoFormat(formatYear, "yyyy", "yy"));
                        schoolAttendanceReportModel.setClass_section_name(sm.getClass_section_name());
                        schoolAttendanceReportModel.setAttendance(model.getAttendancePercentage());
                        schoolAttendanceReportModel.setSchoolid(schoolId);
                        schoolAttendanceReportModel.setAcademicSessionId(model.getAcademic_session_id());
                        schoolAttendanceReportModel.setSession(academicSession);

                        schoolarm.add(schoolAttendanceReportModel);
                    }
                } else {
                    List<AttendanceSummary> attendanceSummaryList = AttendancePercentage.getInstance(current_activity)
                            .getAttendancePercentageforSelectedMonth(schoolId, 0,
                                    academicSessionId, selectedDate);

                    for (AttendanceSummary model : attendanceSummaryList) {
                        ClassSectionModel sm = DatabaseHelper.getInstance(current_activity).getClassSectionBySchoolIdAndSchoolClassId(model.getSchoolId(),
                                model.getSchool_class_id());

                        SchoolAttendanceReportModel schoolAttendanceReportModel = new SchoolAttendanceReportModel();
                        schoolAttendanceReportModel.setMonth(AppModel.getInstance().convertDatetoFormat(formatMonth, "M", "MMM"));
                        schoolAttendanceReportModel.setYear(AppModel.getInstance().convertDatetoFormat(formatYear, "yyyy", "yy"));
                        schoolAttendanceReportModel.setClass_section_name(sm.getClass_section_name());
                        schoolAttendanceReportModel.setAttendance(model.getAttendancePercentage());
                        schoolAttendanceReportModel.setSchoolid(schoolId);
                        schoolAttendanceReportModel.setAcademicSessionId(model.getAcademic_session_id());
                        schoolAttendanceReportModel.setSession(academicSession);

                        schoolarm.add(schoolAttendanceReportModel);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (schoolarm.size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MessageBox("No results found");
                        clearList();
                    }
                });
            }

            return null;
        }
    }
}
