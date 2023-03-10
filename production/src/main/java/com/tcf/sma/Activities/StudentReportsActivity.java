package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.StudentReportAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarSchool;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentReportsActivity extends DrawerActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    Button Search;
    View view;
    LinearLayout llResults, ll_schoolSpinner;
    StudentReportAdapter studentReportAdapterAdapter;
    private EditText et_gr_no, et_student_Name;
    private RecyclerView rv_searchList;
    private ClassSectionModel classSectionModel;
    private ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    private Spinner ClassSectionSpinner, SchoolSpinner;
    private int ClassSpinnerValue, SectionSpinnerValue;
    private int totalworkingdays = 0;
    private List<HolidayCalendarSchoolModel> holidays;
    private int SchoolSpinnerValue = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private SchoolModel schoolModel;
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
//        setContentView(R.layout.activity_student_reports);

        view = setActivityLayout(this, R.layout.activity_student_reports);
        setToolbar(getString(R.string.studentsReport), this, false);

        init(view);
        working();
    }

    private void init(View view) {
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
        Search = (Button) view.findViewById(R.id.btnsearch);
        rv_searchList = (RecyclerView) view.findViewById(R.id.rv_searchList);
        et_gr_no = (EditText) view.findViewById(R.id.et_GrNo);
        et_student_Name = (EditText) view.findViewById(R.id.et_student_name);
        et_student_Name.setVisibility(View.GONE);

        Search.setOnClickListener(this);
        rv_searchList.setNestedScrollingEnabled(false);
        rv_searchList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_searchList.setLayoutManager(llm);

        llResults = (LinearLayout) view.findViewById(R.id.llResults);

        ll_schoolSpinner = (LinearLayout) view.findViewById(R.id.ll_schoolSpinner);
        ll_schoolSpinner.setVisibility(View.VISIBLE);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            SchoolSpinner.setEnabled(false);
        }
    }

    private void working() {
        initSpinners();

    }

    private void initSpinners() {
        schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this));
//        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance());
        schoolModel.getSchoolsList().add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);
        SchoolSpinner.setOnItemSelectedListener(this);


        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);

//        holidays = HolidayCalendarSchool.getInstance(StudentReportsActivity.this).
//                getAllHolidays(SchoolSpinnerValue+"");
//        setTotalWorkingDays();
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
        prevYearDate.setTime(toDate);
        prevYearDate.add(Calendar.YEAR, -1);

        //1 year back date from current date
        Date fromDate = prevYearDate.getTime();

        totalworkingdays = getWorkingDaysBetweenTwoDates(fromDate, toDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearch:
//                if (et_gr_no.getText().toString().trim().length() == 0 &&
//                        SchoolSpinner.getSelectedItem().toString().equals(getString(R.string.select_school))) {
//                    MessageBox("Please select school and enter Gr No");
//                    clearList();
//                }
//                else if (ClassSectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_class_section))) {
//                    MessageBox("Please select class section");
//                    clearList();
//                }
                if (SchoolSpinner.getSelectedItem().toString().equals(getString(R.string.select_school))) {
                    MessageBox("Please select school");
                    clearList();
                } else {
//                    StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllStudents());
                    holidays = HolidayCalendarSchool.getInstance(StudentReportsActivity.this).
                            getAllHolidays(SchoolSpinnerValue + "");
                    setTotalWorkingDays();
                    StudentModel.getInstance().setStudentsList(
                            DatabaseHelper.getInstance(this).
                                    getAllSearchedStudentsInSchool(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), null));
                    if (StudentModel.getInstance().getStudentsList().size() > 0) {
                        setAdapter();

                    } else {
                        MessageBox("No results found");
                        clearList();
                    }

                }
                break;
        }
    }

    private void setAdapter() {
        studentReportAdapterAdapter = new StudentReportAdapter(SchoolSpinnerValue, StudentModel.getInstance().getStudentsList(), this,
                0, ClassSpinnerValue, SectionSpinnerValue, totalworkingdays);
        rv_searchList.setAdapter(studentReportAdapterAdapter);
        llResults.setVisibility(View.VISIBLE);
    }

    private void clearList() {
        if (studentReportAdapterAdapter != null) {
            StudentModel.getInstance().setStudentsList(null);
            setAdapter();
            llResults.setVisibility(View.GONE);
        }
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
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !selectedDateIsHoliday(startCal)) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                clearList();
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this, SchoolSpinnerValue);

                classSectionModel = new ClassSectionModel();
                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                ClassSectionSpinner.setOnItemSelectedListener(this);

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

                break;
            case R.id.spn_class_section_name:
                clearList();
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
        if (studentReportAdapterAdapter != null) {
//            StudentModel.getInstance().setStudentsList(DatabaseHelper.getInstance(this).getAllSearchedStudentsInSchool(SurveyAppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, et_gr_no.getText().toString().trim(), et_student_Name.getText().toString().trim()));
            StudentModel.getInstance().setStudentsList(
                    DatabaseHelper.getInstance(this).
                            getAllSearchedStudentsInSchool(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue,
                                    et_gr_no.getText().toString().trim(), null));
            setAdapter();

        }
    }
}
