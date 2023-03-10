package com.tcf.sma.Fragment;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.Calendar.EventBottomSheetDialog;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class DashboardCalendarFragment extends Fragment {

    private TextView tv_sessionStartDate, tv_sessionEndDate, tv_workingDays, tv_daysElapsed, tv_daysRemaning, tv_sessionInfo;
    private CardView cv_session_info;
    private CalendarView calendarView;
    private View view;
    private int schoolId = 0;
    private List<CalendarsModel> OffdaysList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar_dashboard, container, false);

        init(view);
        working(view);
        return view;
    }

    private void init(View view) {
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        tv_sessionStartDate = (TextView) view.findViewById(R.id.tv_sessionStartDate);
        tv_sessionEndDate = (TextView) view.findViewById(R.id.tv_sessionEndDate);
        tv_workingDays = (TextView) view.findViewById(R.id.tv_workingDays);
        tv_daysElapsed = (TextView) view.findViewById(R.id.tv_daysElapsed);
        tv_daysRemaning = (TextView) view.findViewById(R.id.tv_daysRemaning);
        cv_session_info = (CardView) view.findViewById(R.id.cv_session_info);
        tv_sessionInfo = (TextView) view.findViewById(R.id.tv_sessionInfo);
    }

    private void working(View view) {
        schoolId = ((NewDashboardActivity) Objects.requireNonNull(getActivity())).schoolId;
        if (schoolId > 0) {
            OffdaysList = DatabaseHelper.getInstance(view.getContext()).getAllDatesForHolidays(schoolId + "");

            setSessionInfoTextviews(schoolId);
            populateCalendarEvents(schoolId);
        }

        calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();
            Date date = clickedDayCalendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
            String newDate = sdf.format(date);
            String details = "";
            Map<Integer, CalendarsModel> holidayNames = new HashMap<>();
            //            CalendarsModel calendarsModel = DatabaseHelper.getInstance(view.getContext()).checkDateForAttendance(newDate, schoolId + "");

            if (OffdaysList.size() > 0) {
                for (CalendarsModel calendarsModel : OffdaysList) {

                    if (calendarsModel.getActivity_Start_Date().equals(newDate)) {
                        holidayNames.put(calendarsModel.getC_Activity_Id(), calendarsModel);
//                        details += calendarsModel.getC_Holiday_Type_Name()+"\n";
//                        details += details.toLowerCase().replaceAll("\n"," ")
//                                .contains(calendarsModel.getC_Holiday_Type_Name()
//                                        .toLowerCase())?"":calendarsModel.getC_Holiday_Type_Name() + "\n";
                    }
                }
                if (holidayNames.size() > 0) {
                    List<CalendarsModel> cmList = new ArrayList<>(holidayNames.values());
                    EventBottomSheetDialog eventBottomSheetDialog = new EventBottomSheetDialog();
                    eventBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "bottomsheet");
                    eventBottomSheetDialog.setList(cmList);

//                    ((NewDashboardActivity) getActivity()).MessageBox("Selected day is: \n\n" + details);
                }
            }
        });
    }

    private void populateCalendarEvents(int schoolId) {
        try {
            new Thread(() -> {
//                OffdaysList = DatabaseHelper.getInstance(view.getContext()).getAllDatesForHolidays(schoolId + "");
//                    uniqueOffdaysList = new HashSet<>(OffdaysList);

                List<EventDay> events = new ArrayList<>();
                Map<String, String> dateAndHolidayTypeId = new HashMap<>();

                for (CalendarsModel cm : OffdaysList) {
                    if (dateAndHolidayTypeId.size() > 0) {
                        if (dateAndHolidayTypeId.containsKey(cm.Activity_Start_Date)) {
                            if (!dateAndHolidayTypeId.get(cm.Activity_Start_Date).contains(cm.getC_Holiday_Type_Id() + "")) {
                                dateAndHolidayTypeId.put(cm.Activity_Start_Date, dateAndHolidayTypeId.get(cm.Activity_Start_Date) +
                                        "," + cm.getC_Holiday_Type_Id());
                            }
                        } else {
                            dateAndHolidayTypeId.put(cm.getActivity_Start_Date(), cm.getC_Holiday_Type_Id() + "");
                        }

                    } else {
                        dateAndHolidayTypeId.put(cm.getActivity_Start_Date(), cm.getC_Holiday_Type_Id() + "");
                    }
//                        Drawable dotImage = getColorForHolidays(cm);
//                        events.add(new EventDay(calendar, dotImage));

                }

                for (Map.Entry<String, String> entry : dateAndHolidayTypeId.entrySet()) {
                    Calendar calendar = getCalendar(entry.getKey());
                    Drawable dotImage = getIconForHolidays(entry.getValue());
                    events.add(new EventDay(calendar, dotImage));
                }

                if (isAdded())
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> calendarView.setEvents(events));
//                Objects.requireNonNull(getActivity()).runOnUiThread(() -> compactCalendarView.addEvents(eventList));
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Drawable getIconForHolidays(String holidayTypeId) {
//        Drawable drawable = view.getResources().getDrawable(R.drawable.sample_one_icon);
//        GradientDrawable gradientDrawable = (GradientDrawable) drawable;

//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(view.getContext(),
//                R.drawable.sample_one_icon);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);

        LayerDrawable layerDrawable = (LayerDrawable) view.getResources()
                .getDrawable(R.drawable.sample_four_icons);
        try {

            String[] typeIds = holidayTypeId.split(",");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (typeIds.length == 1) {
                    layerDrawable.findDrawableByLayerId(R.id.dot1).setTint(getResources().getColor(getColorForHolidays(typeIds[0])));
                    layerDrawable.findDrawableByLayerId(R.id.dot2).setTint(getResources().getColor(R.color.transparent));
                    layerDrawable.findDrawableByLayerId(R.id.dot3).setTint(getResources().getColor(R.color.transparent));
                    layerDrawable.findDrawableByLayerId(R.id.dot4).setTint(getResources().getColor(R.color.transparent));
                } else if (typeIds.length == 2) {
                    layerDrawable.findDrawableByLayerId(R.id.dot1).setTint(getResources().getColor(getColorForHolidays(typeIds[0])));
                    layerDrawable.findDrawableByLayerId(R.id.dot2).setTint(getResources().getColor(getColorForHolidays(typeIds[1])));
                    layerDrawable.findDrawableByLayerId(R.id.dot3).setTint(getResources().getColor(R.color.transparent));
                    layerDrawable.findDrawableByLayerId(R.id.dot4).setTint(getResources().getColor(R.color.transparent));

                } else if (typeIds.length == 3) {
                    layerDrawable.findDrawableByLayerId(R.id.dot1).setTint(getResources().getColor(getColorForHolidays(typeIds[0])));
                    layerDrawable.findDrawableByLayerId(R.id.dot2).setTint(getResources().getColor(getColorForHolidays(typeIds[1])));
                    layerDrawable.findDrawableByLayerId(R.id.dot3).setTint(getResources().getColor(getColorForHolidays(typeIds[2])));
                    layerDrawable.findDrawableByLayerId(R.id.dot4).setTint(getResources().getColor(R.color.transparent));

                } else if (typeIds.length == 4) {
                    layerDrawable.findDrawableByLayerId(R.id.dot1).setTint(getResources().getColor(getColorForHolidays(typeIds[0])));
                    layerDrawable.findDrawableByLayerId(R.id.dot2).setTint(getResources().getColor(getColorForHolidays(typeIds[1])));
                    layerDrawable.findDrawableByLayerId(R.id.dot3).setTint(getResources().getColor(getColorForHolidays(typeIds[2])));
                    layerDrawable.findDrawableByLayerId(R.id.dot4).setTint(getResources().getColor(getColorForHolidays(typeIds[3])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DrawableCompat.wrap(layerDrawable);
    }

    private int getColorForHolidays(String holidayTypeId) {
        int color = R.color.light_red;  //Default Holiday for all

        switch (holidayTypeId) {
            case "1": //Working day for all
                color = R.color.light_green;
                break;
            case "2": //Holiday for students only
                color = R.color.dark_yellow;
                break;
            case "3": //Holiday for all except principal
                color = R.color.orange;
                break;
            case "4": //Holiday for all
                color = R.color.light_red;
                break;

//            case 1: //Working day for all
//                color = R.color.light_green;
//                DrawableCompat.setTint(wrappedDrawable, view.getResources().getColor(color));
//                break;
//            case 2: //Holiday for students only
//                color = R.color.yellow;
//                DrawableCompat.setTint(wrappedDrawable, view.getResources().getColor(color));
//                break;
//            case 3: //Holiday for all except principal
//                color = R.color.orange;
//                DrawableCompat.setTint(wrappedDrawable, view.getResources().getColor(color));
//                break;
//            case 4: //Holiday for all
//                color = R.color.light_red;
//                DrawableCompat.setTint(wrappedDrawable, view.getResources().getColor(color));
//                break;
        }

        return color;
    }

    private Calendar getCalendar(String date) {
        Calendar calendar = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
            date = AppModel.getInstance().convertDatetoFormat(date, "yyyy-MM-dd'T'hh:mm:ss", "dd-MMM-yy");
            Date dated = sdf.parse(date);
            calendar = Calendar.getInstance();
            calendar.setTime(dated);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            return calendar;
        }
    }

    private void setSessionInfoTextviews(int SchoolID) {
        try {
            List<SchoolModel> schoolModelList = DatabaseHelper.getInstance(view.getContext()).getAllUserSchoolsById(SchoolID);
            CalendarsModel cm = new CalendarsModel();

            String startDate = "", endDate = "";
            for (SchoolModel model : schoolModelList) {
                tv_sessionInfo.setText("Current Session (" + model.getAcademic_session() + ")");
                tv_sessionStartDate.setText(AppModel.getInstance().convertDatetoFormat(model.getStart_date(),
                        "yyyy-MM-dd hh:mm:ss a", "dd-MMM-yy"));
                tv_sessionEndDate.setText(AppModel.getInstance().convertDatetoFormat(model.getEnd_date(),
                        "yyyy-MM-dd hh:mm:ss a", "dd-MMM-yy"));
                cm = DatabaseHelper.getInstance(view.getContext()).getTotalDaysAndOffDaysFromSession(SchoolID,
                        model.getAcademic_Session_Id());

                startDate = model.getStart_date();
                endDate = model.getEnd_date();
            }

            if (cm != null) {
                int totalDays = cm.getTotalDays();
                int totalOffDays = cm.getTotalOffDays();
                int totalWorkingDays = totalDays - totalOffDays;
                String currentDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a");


                long daysElapsed = AppModel.getInstance().getDaysBetweenDates(startDate, currentDate, "yyyy-MM-dd");
                daysElapsed = daysElapsed - getHolidaysElapsedFrom();
//                long daysRemaning = SurveyAppModel.getInstance().getDaysBetweenDates(currentDate, endDate, "yyyy-MM-dd");
                long daysRemaning = totalWorkingDays - daysElapsed;


                tv_workingDays.setText(totalWorkingDays + "");
                tv_daysElapsed.setText(daysElapsed + "");
                tv_daysRemaning.setText(daysRemaning + "");
            } else {
                tv_workingDays.setText("");
                tv_daysElapsed.setText("");
                tv_daysRemaning.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
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
}
