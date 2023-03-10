package com.tcf.sma.Activities.FeesCollection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.PendingAttendanceAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.HolidayCalendarSchool;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AttendanceLast30DaysCountModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.HolidayCalendarSchoolModel;
import com.tcf.sma.Models.PendingAttendanceModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AttendancePendingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public ProgressDialog progressDialog;
    String userSchoolIds = "";
    private RecyclerView rvPendingAttendance;
    private List<PendingAttendanceModel> pendingAttendanceModels;
    private List<PendingAttendanceModel> pendingAttendanceModelsForLast30Days, pendingAttendanceModelsForToday;
    private List<HolidayCalendarSchoolModel> holidays;
    private Spinner spnSchools;
    private List<SchoolModel> schoolModels;
    private int schoolId = 0;
    private String day = "";
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
        setContentView(R.layout.activity_attendance_pending);
        init();
        handleIntent(getIntent());
        populateSchoolSpinner();
//        populateRecyclerView();
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("day")) {
                day = intent.getStringExtra("day");
            }
        }

        schoolId = intent.hasExtra("schoolId") ? intent.getIntExtra("schoolId", 0) : 0;

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRecyclerView();
    }

    private void init() {
        spnSchools = (Spinner) findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        rvPendingAttendance = (RecyclerView) findViewById(R.id.rvPendingAttendance);
        rvPendingAttendance.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvPendingAttendance.setLayoutManager(llm);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }
    }

    public void populateSchoolSpinner() {
//        schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);

        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchools();

        for (SchoolModel model : schoolModels) {
            userSchoolIds = userSchoolIds + String.valueOf(model.getId());
            userSchoolIds += ",";
        }
        if (schoolModels != null && schoolModels.size() > 1) {
            schoolModels.add(new SchoolModel(0, "All"));
        }
        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
        spnSchools.setSelection(selectedSchoolIndex);

        if (userSchoolIds.length() > 0) {
            userSchoolIds = userSchoolIds.substring(0, userSchoolIds.lastIndexOf(","));
            AppModel.getInstance().setuserSchool(this, userSchoolIds);
        }

    }

    public int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
        int index = 0;
        for (SchoolModel model : schoolModels) {
            if (model.getId() == appSchoolId)
                return index;
            index++;
        }
        return index;
    }

    private void populateRecyclerView() {
        if (day.equals("last30Days")) {
            GenerateLast30DaysAttendance asyncTask = new GenerateLast30DaysAttendance();
            asyncTask.execute("");
        } else {
            GenerateTodayAttendance todayAttendance = new GenerateTodayAttendance();
            todayAttendance.execute("");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
        populateRecyclerView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean isSchoolClassIdExist(int schoolclass_id, List<Integer> schoolClassIds) {
        for (int id : schoolClassIds) {
            if (id == schoolclass_id)
                return true;
        }
        return false;
    }
//
//    }

    private boolean generateAttendanceForDate(String date, List<Integer> schoolClassIds, boolean isAttendanceMarked) {
        //todo check here for if date is a holiday or not. If holiday than don't add this date for attendance taking. else add.
        // check here for if date is a holiday or not. If holiday than don't add this date for attendance taking. else add.
        String formattedDate = AppModel.getInstance().convertDatetoFormat(date, "yyyy-MM-dd", "dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(formattedDate));// all do
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        /*If selected date is not a sunday and in not a holiday than add in a list else not*/
        if (!selectedDateIsHoliday(cal)) {

            for (PendingAttendanceModel model : pendingAttendanceModels) {
                if (isSchoolClassIdExist(model.getSchoolclass_id(), schoolClassIds)) {
                    pendingAttendanceModelsForLast30Days.add(new PendingAttendanceModel(
                            model.getSchoolId(),
                            model.getClassName(),
                            model.getSection(),
                            model.getSchool(),
                            date, model.getSchoolclass_id(),
                            true));

                } else {
                    pendingAttendanceModelsForLast30Days.add(new PendingAttendanceModel(
                            model.getSchoolId(),
                            model.getClassName(),
                            model.getSection(),
                            model.getSchool(),
                            date, model.getSchoolclass_id(),
                            false));
                }
            }
        }
//        if (dayOfWeek != Calendar.SUNDAY && !selectedDateIsHoliday(cal)) {
//
//            for (PendingAttendanceModel model : pendingAttendanceModels) {
//                if (isSchoolClassIdExist(model.getSchoolclass_id(), schoolClassIds)) {
//                    pendingAttendanceModelsForLast30Days.add(new PendingAttendanceModel(
//                            model.getSchoolId(),
//                            model.getClassName(),
//                            model.getSection(),
//                            model.getSchool(),
//                            date, model.getSchoolclass_id(),
//                            true));
//
//                } else {
//                    pendingAttendanceModelsForLast30Days.add(new PendingAttendanceModel(
//                            model.getSchoolId(),
//                            model.getClassName(),
//                            model.getSection(),
//                            model.getSchool(),
//                            date, model.getSchoolclass_id(),
//                            false));
//                }
//            }
//        }
        return true;
    }

    private boolean generateAttendanceForDate(String date, boolean isAttendanceMarked) {
        // check here for if date is a holiday or not. If holiday than don't add this date for attendance taking. else add.
        String formattedDate = AppModel.getInstance().convertDatetoFormat(date, "yyyy-MM-dd", "dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(formattedDate));// all do
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        /*If selected date is not a sunday and in not a holiday than add in a list else not*/
        if (!selectedDateIsHoliday(cal)) {
            for (PendingAttendanceModel model : pendingAttendanceModels) {
                pendingAttendanceModelsForLast30Days.add(new PendingAttendanceModel(
                        model.getSchoolId(),
                        model.getClassName(),
                        model.getSection(),
                        model.getSchool(),
                        date, model.getSchoolclass_id(),
                        isAttendanceMarked));

            }
        }
//        if (dayOfWeek != Calendar.SUNDAY && !selectedDateIsHoliday(cal)) {
//            for (PendingAttendanceModel model : pendingAttendanceModels) {
//                pendingAttendanceModelsForLast30Days.add(new PendingAttendanceModel(
//                        model.getSchoolId(),
//                        model.getClassName(),
//                        model.getSection(),
//                        model.getSchool(),
//                        date, model.getSchoolclass_id(),
//                        isAttendanceMarked));
//
//            }
//        }
        return true;
    }

    private List<String> getUniqueHolidays() {
        List<CalendarsModel> OffdaysList = DatabaseHelper.getInstance(this).getAllDatesForHolidays(schoolId + "");
        List<String> holidays = new ArrayList<>();
        for (CalendarsModel cm : OffdaysList) {
            holidays.add(AppModel.getInstance()
                    .convertDatetoFormat(cm.getActivity_Start_Date(), "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy"));
        }

        if (holidays.size() > 0) {
            Set<String> uHolidays = new HashSet<>(holidays);

            holidays.clear();
            holidays.addAll(uHolidays);
        }
        return holidays;
    }

    private boolean generateAttendanceForTodayDate(String date, boolean isAttendanceMarked) {
        // check here for if date is a holiday or not. If holiday than don't add this date for attendance taking. else add.
//        String formattedDate = SurveyAppModel.getInstance().convertDatetoFormat(date, "yyyy-MM-dd", "dd-MM-yyyy");
//        CalendarsModel cal = CalendarsModel.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//        try {
//            cal.setTime(sdf.parse(formattedDate));// all do
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int dayOfWeek = cal.get(CalendarsModel.DAY_OF_WEEK);
//        /*If selected date is not a sunday and in not a holiday than add in a list else not*/
//        if (dayOfWeek != CalendarsModel.SUNDAY && !selectedDateIsHoliday(cal)) {
        for (PendingAttendanceModel model : pendingAttendanceModels) {
            pendingAttendanceModelsForToday.add(new PendingAttendanceModel(
                    model.getSchoolId(),
                    model.getClassName(),
                    model.getSection(),
                    model.getSchool(),
                    date, model.getSchoolclass_id(),
                    isAttendanceMarked));

        }
//        }
        return true;
    }

    private boolean generateAttendanceForTodayDate(String date, List<Integer> schoolClassIds, boolean isAttendanceMarked) {
        //todo check here for if date is a holiday or not. If holiday than don't add this date for attendance taking. else add.
        // check here for if date is a holiday or not. If holiday than don't add this date for attendance taking. else add.
//        String formattedDate = SurveyAppModel.getInstance().convertDatetoFormat(date, "yyyy-MM-dd", "dd-MM-yyyy");
//        CalendarsModel cal = CalendarsModel.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//        try {
//            cal.setTime(sdf.parse(formattedDate));// all do
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int dayOfWeek = cal.get(CalendarsModel.DAY_OF_WEEK);
//        /*If selected date is not a sunday and in not a holiday than add in a list else not*/
//        if (dayOfWeek != CalendarsModel.SUNDAY && !selectedDateIsHoliday(cal)) {

        for (PendingAttendanceModel model : pendingAttendanceModels) {
            if (isSchoolClassIdExist(model.getSchoolclass_id(), schoolClassIds)) {
                pendingAttendanceModelsForToday.add(new PendingAttendanceModel(
                        model.getSchoolId(),
                        model.getClassName(),
                        model.getSection(),
                        model.getSchool(),
                        date, model.getSchoolclass_id(),
                        true));

            } else {
                pendingAttendanceModelsForToday.add(new PendingAttendanceModel(
                        model.getSchoolId(),
                        model.getClassName(),
                        model.getSection(),
                        model.getSchool(),
                        date, model.getSchoolclass_id(),
                        false));
            }
        }
//        }
        return true;
    }

    /**
     * Added By Mohammad Haseeb
     * Method to check if selected date is a holiday or not
     *
     * @param calendar
     * @return
     */
//    private boolean selectedDateIsHoliday(Calendar calendar) {
//
//        Date d = calendar.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        String selectDate = sdf.format(d);
//
//        for (HolidayCalendarSchoolModel model : holidays) {
//
//            Date startDate = null, endDate = null, date;
//            try {
//
//                startDate = sdf.parse(model.getStartDate());
//                endDate = sdf.parse(model.getEndDate());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Calendar cal1 = Calendar.getInstance();
//            Calendar cal2 = Calendar.getInstance();
//            cal1.setTime(startDate);
//            cal1.add(Calendar.DATE, -1);
//            cal2.setTime(endDate);
//            cal2.add(Calendar.DATE, 1);
//
//            date = formatStringToDate(selectDate);
//
//            if (date.after(cal1.getTime()) && date.before(cal2.getTime()))
//                return true;
//        }
//        return false;
//    }
    private boolean selectedDateIsHoliday(Calendar calendar) {
        Date d = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String selectDate = sdf.format(d);

        List<String> holidaysList = getUniqueHolidays();

        for (String holidays : holidaysList) {
            if (holidays.equalsIgnoreCase(selectDate)) {
                return true;
            }
        }
        return false;
    }

    //
//    public List<Integer> getSchoolClassIdsForMarkedAttendance(String date) {
//        List<Integer> list = new ArrayList<>();
//        Cursor cursor = null;
//        String selectQuery;
//        try {
//
//            selectQuery = "select school_class_id from attendance where for_date = '" + date + "'";
//
//            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery(selectQuery, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    int schoolClassId = cursor.getInt(cursor.getColumnIndex("school_class_id"));
//                    list.add(schoolClassId);
//                }
//                while (cursor.moveToNext());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//
//        }
//
//        return list;
//    }
//
    public class GenerateLast30DaysAttendance extends AsyncTask<String, String, String> {
        Context cc;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(AttendancePendingActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            List<AttendanceLast30DaysCountModel> countModelList = DatabaseHelper.getInstance(AttendancePendingActivity.this).getAttendanceTakenCountForLast30Days();
            if (schoolId > 0) {
                pendingAttendanceModels = DatabaseHelper.getInstance(AttendancePendingActivity.this).getPendingAttendanceForToday(String.valueOf(String.valueOf(schoolId)));
                holidays = HolidayCalendarSchool.getInstance(AttendancePendingActivity.this).getAllHolidays(String.valueOf(schoolId));
            } else {
                pendingAttendanceModels = DatabaseHelper.getInstance(AttendancePendingActivity.this).getPendingAttendanceForToday(String.valueOf(AppModel.getInstance().getuserSchoolIDS(AttendancePendingActivity.this)));
                holidays = HolidayCalendarSchool.getInstance(AttendancePendingActivity.this).getAllHolidays(String.valueOf(AppModel.getInstance().getuserSchoolIDS(AttendancePendingActivity.this)));
            }

            pendingAttendanceModelsForLast30Days = new ArrayList<>();
            for (AttendanceLast30DaysCountModel model : countModelList) {
                if (model.getCount() == 0) {
                    generateAttendanceForDate(model.getDate(), false);
                } else {
                    List<Integer> schoolClassIds = DatabaseHelper.getInstance(AttendancePendingActivity.this).getSchoolClassIdsForMarkedAttendance(model.getDate());
                    if (schoolClassIds != null && schoolClassIds.size() > 0) {
                        generateAttendanceForDate(model.getDate(), schoolClassIds, false);
                    } else {
                        generateAttendanceForDate(model.getDate(), false);
                    }
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (pendingAttendanceModelsForLast30Days != null && pendingAttendanceModelsForLast30Days.size() > 0) {
                PendingAttendanceAdapter adapter = new PendingAttendanceAdapter(pendingAttendanceModelsForLast30Days, AttendancePendingActivity.this, false);
                rvPendingAttendance.setAdapter(adapter);
            }
            progressDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public class GenerateTodayAttendance extends AsyncTask<String, String, String> {
        Context cc;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(AttendancePendingActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            if (schoolId > 0)
                pendingAttendanceModels = DatabaseHelper.getInstance(AttendancePendingActivity.this).getPendingAttendanceForToday(String.valueOf(String.valueOf(schoolId)));
            else
                pendingAttendanceModels = DatabaseHelper.getInstance(AttendancePendingActivity.this).getPendingAttendanceForToday(String.valueOf(AppModel.getInstance().getuserSchoolIDS(AttendancePendingActivity.this)));

            List<AttendanceLast30DaysCountModel> countModelList = DatabaseHelper.getInstance(AttendancePendingActivity.this).getAttendanceTakenCountForToday();

            pendingAttendanceModelsForToday = new ArrayList<>();
            for (AttendanceLast30DaysCountModel model : countModelList) {
                if (model.getCount() == 0) {
                    generateAttendanceForTodayDate(model.getDate(), false);
                } else {
                    List<Integer> schoolClassIds = DatabaseHelper.getInstance(AttendancePendingActivity.this).getSchoolClassIdsForMarkedAttendance(model.getDate());
                    if (schoolClassIds != null && schoolClassIds.size() > 0) {
                        generateAttendanceForTodayDate(model.getDate(), schoolClassIds, false);
                    } else {
                        generateAttendanceForTodayDate(model.getDate(), false);
                    }
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (pendingAttendanceModelsForToday != null && pendingAttendanceModelsForToday.size() > 0) {
                PendingAttendanceAdapter adapter = new PendingAttendanceAdapter(pendingAttendanceModelsForToday, AttendancePendingActivity.this, true);
                rvPendingAttendance.setAdapter(adapter);
            }
            progressDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
