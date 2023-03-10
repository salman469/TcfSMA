package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tcf.sma.Activities.LoginActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Activities.School_list_Activity;
import com.tcf.sma.Activities.SearchSchoolActivity;
import com.tcf.sma.Activities.StudentProfileSearchActivity;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Interfaces.OnItemClickListener;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.EnabledModules;
import com.tcf.sma.Models.SchoolExpandableModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.model.SurveyAppModel;
//import com.tcf.sma.Survey.com.kcompute.dnaareasurvey.ProjectsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchoolExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<SchoolExpandableModel> schoolFilteredList;
    private ArrayList<SchoolExpandableModel> originalList;
    private OnItemClickListener listener;
    private boolean isVisitingForm;
    private boolean isUserViewer = true;


    public SchoolExpandableAdapter(Context context, ArrayList<SchoolExpandableModel> schoolGroupList) {
        this.context = context;
        this.originalList = schoolGroupList;
        this.schoolFilteredList = new ArrayList<>();
        this.schoolFilteredList.addAll(schoolGroupList);
    }


    public SchoolExpandableAdapter(Context context, ArrayList<SchoolExpandableModel> schoolGroupList, boolean isUserViewer, boolean isVisitingForm) {
        this.context = context;
        this.originalList = schoolGroupList;
        this.isVisitingForm = isVisitingForm;
        this.schoolFilteredList = new ArrayList<>();
        this.schoolFilteredList.addAll(schoolGroupList);
        this.isUserViewer = isUserViewer;
    }

    public SchoolExpandableAdapter(Context context, ArrayList<SchoolExpandableModel> schoolGroupList, OnItemClickListener listener, boolean isVisitingForm) {
        this.context = context;
        this.originalList = schoolGroupList;
        this.listener = listener;
        this.isVisitingForm = isVisitingForm;
        this.schoolFilteredList = new ArrayList<>();
        this.schoolFilteredList.addAll(schoolGroupList);

    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<SchoolModel> school = schoolFilteredList.get(groupPosition).getSchoolModels();
//        Log.e("schoolCount","1:=> "+school.size());
        return school.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        SchoolModel school = (SchoolModel) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.school_cell, null);
        }

        TextView schoolName = (TextView) view.findViewById(R.id.cell_schoolName);
        schoolName.setText(school.getId() + " - " + school.getName().trim());

        view.setOnClickListener(view1 -> {
            SchoolModel model = (SchoolModel) getChild(groupPosition, childPosition);
            if (isVisitingForm)
                try {
                    AppModel.getInstance().setSpinnerSelectedSchool(context,
                            model.getId());
                    context.startActivity(new Intent(context, SurveyAppModel.getInstance()
                            .selectedProject
                            .setSelectedSchool(model)
                            .popActivityFromStack())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else
                handleClick(model);
        });

        return view;
    }

    private void handleClick(SchoolModel schoolModel) {
        try {
            List<String> allowedModules = null;
            if (schoolModel.getAllowedModule_App() != null){
                allowedModules = Arrays.asList(schoolModel.getAllowedModule_App().split(","));
            }

            if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue)){
                AppModel.getInstance().setFinanceSyncingCompleted(context, schoolModel.getId());
            }

            if (AppModel.getInstance().isConnectedToInternet(context)) {

//                DataSync.getInstance(context).startSyncNotification();

                //Drop Table Calendar
                DatabaseHelper.getInstance(context).dropTable(DatabaseHelper.TABLE_CALENDAR);
//
//                                        //Create New Calendar Table
                DatabaseHelper.getInstance(context).createTable(DatabaseHelper.CREATE_TABLE_CALENDAR);
                if (isUserViewer) {
                    ((SearchSchoolActivity) context).runOnUiThread(() -> AppModel.getInstance().showLoader(context, "Loading School data", "Please wait..."));

                } else {
                    ((School_list_Activity) context).runOnUiThread(() -> AppModel.getInstance().showLoader(context, "Loading School data", "Please wait..."));
                }
                AppModel.getInstance().setSelectedSchool(context, schoolModel.getId());
                AppModel.getInstance().setSpinnerSelectedSchool(context, schoolModel.getId());
                AppModel.getInstance().setSearchedSchoolId(context, schoolModel.getId());

                int roleID = DatabaseHelper.getInstance(context).getCurrentLoggedInUser() != null ?
                        DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId() : 0;
                if (isUserViewer) {

                    AppModel.getInstance().syncMetaData(context, (SearchSchoolActivity) context, schoolModel.getId());

                    //TODO After adding module here change in AppModel saveAllowedModuleToPreference function
                    if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "1");

                    if (allowedModules != null && allowedModules.contains(AppConstants.HREmployeeListingModuleValue) ||
                            allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue) ||
                            allowedModules != null && allowedModules.contains(AppConstants.HRResignationModuleValue) ||
                            allowedModules != null && allowedModules.contains(AppConstants.HRTerminationModuleValue) ||
                            allowedModules != null && allowedModules.contains(AppConstants.TCTModuleValue)) {
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_Employee, "0");

                    } else {
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_Employee, "1");
                    }

                    if (allowedModules != null && allowedModules.contains(AppConstants.HREmployeeListingModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeListing, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeListing, "1");


                    if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeLeaveAndAttend, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeLeaveAndAttend, "1");


                    if (allowedModules != null && allowedModules.contains(AppConstants.HRResignationModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeResignation, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeResignation, "1");


                    if (allowedModules != null && allowedModules.contains(AppConstants.HRTerminationModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeTermination, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_EmployeeTermination, "1");

                    if ((roleID == AppConstants.roleId_27_P || roleID == AppConstants.roleId_101_ST ||  roleID == AppConstants.roleId_109_CM ) && allowedModules != null && allowedModules.contains(AppConstants.TCTModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_TCTEntry, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_TCTEntry, "1");

                    if (schoolModel.getAllowedModule_App().contains(AppConstants.ExpenseModuleValue)) {
                        new DataSync(context).syncExpenseMetaDataForSingleSchool(context, schoolModel);
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_Expense, "0");
                    }
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_Expense, "1");

                    if (allowedModules != null && allowedModules.contains(AppConstants.StudentVisitFormsModuleValue)) {
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_StudentVisitForm, "0");
                    } else {
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_StudentVisitForm, "1");
                    }

//                        //open Set Fees menu
//                        if (DatabaseHelper.getInstance(context).isRegionIsInFlagShipSchools()) {
//                            AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_Set_Fess, "0");
//                        } else {
//                            AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_Set_Fess, "1");
//                        }

                } else {
                    AppModel.getInstance().syncMetaData(context, (School_list_Activity) context, schoolModel.getId());

//                if (schoolModel.getAllowedModule_App().contains(AppConstants.FinanceModuleValue))
//                    SurveyAppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "0");
//                else
//                    SurveyAppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "1");

                }


            } else {
//                        Toast.makeText(activity, "No internet connectivity", Toast.LENGTH_SHORT).show();
                AppModel.getInstance().setSelectedSchool(context, schoolModel.getId());
                AppModel.getInstance().setSpinnerSelectedSchool(context, schoolModel.getId());
                AppModel.getInstance().setSearchedSchoolId(context, schoolModel.getId());


                if (isUserViewer) {
                    if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "1");


                } else {
                    if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "0");
                    else
                        AppModel.getInstance().writeToSharedPreferences(context, AppConstants.HIDE_FEES_COLLECTION, "1");


                    //Clear previous sync data:
                    context.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "false").commit();
                }

                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<SchoolModel> countryList = schoolFilteredList.get(groupPosition).getSchoolModels();
        Log.e("schoolCount","2:=> "+countryList.size());
        return countryList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return schoolFilteredList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return schoolFilteredList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        SchoolExpandableModel continent = (SchoolExpandableModel) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_school_region_area_head, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.tv_regionArea);
        String headerTitle = continent.getRegionArea() + ((continent.getSchoolModels() == null || continent.getSchoolModels().size() == 0) ? "" :
                "  (" + continent.getSchoolModels().size() + ")");
        heading.setText(headerTitle);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    public void filterData(String query) {

        query = query.toLowerCase();
        Log.v("MyListAdapter", String.valueOf(schoolFilteredList.size()));
        schoolFilteredList.clear();

        if (query.isEmpty())
            schoolFilteredList.addAll(originalList);
        else {

            for (SchoolExpandableModel continent : originalList) {

                List<SchoolModel> countryList = continent.getSchoolModels();
                List<SchoolModel> newList = new ArrayList<>();
                for (SchoolModel country : countryList) {
                    if ((country.getId() + "").contains(query) || country.getName().toLowerCase().contains(query) || (country.getArea() != null && country.getArea().toLowerCase().contains(query))
                            || (country.getRegion() != null && country.getRegion().toLowerCase().contains(query))) {
                        newList.add(country);
                    }
                }
                if (newList.size() > 0) {
                    SchoolExpandableModel nContinent = new SchoolExpandableModel();
                    nContinent.setArea(continent.getArea());
                    nContinent.setRegion(continent.getRegion());
                    nContinent.setRegionArea(continent.getRegionArea());
                    nContinent.setSchoolModels(newList);
                    schoolFilteredList.add(nContinent);
                }
            }
        }

        Log.v("MyListAdapter", String.valueOf(schoolFilteredList.size()));
        notifyDataSetChanged();

    }

}