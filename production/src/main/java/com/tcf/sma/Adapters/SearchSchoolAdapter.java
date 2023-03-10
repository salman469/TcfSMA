package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Activities.SearchSchoolActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchSchoolAdapter extends RecyclerView.Adapter<SearchSchoolAdapter.SchoolViewHolder> implements Filterable {
    Activity activity;
    ArrayList<SchoolModel> Sclmdl;
    ArrayList<SchoolModel> filteredSchoolList;

    public SearchSchoolAdapter(Activity activity, ArrayList<SchoolModel> model) {
        this.activity = activity;
        this.Sclmdl = model;
        this.filteredSchoolList = Sclmdl;
    }

    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview;
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_cell, parent, false);
        return new SchoolViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(SchoolViewHolder holder, int position) {
        holder.schoolName.setText(filteredSchoolList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (filteredSchoolList != null) {
            return filteredSchoolList.size();
        } else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredSchoolList = Sclmdl;
                } else {
                    ArrayList<SchoolModel> filteredList = new ArrayList<>();
                    for (SchoolModel row : Sclmdl) {

                        if ((row.getName().toLowerCase().contains(charString.toLowerCase())) || (row.getArea() != null && row.getArea().toLowerCase().contains(charString.toLowerCase())) ||
                                (row.getRegion() != null && row.getRegion().toLowerCase().contains(charString.toLowerCase()))
                        ) {
                            filteredList.add(row);
                        }
                    }

                    filteredSchoolList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSchoolList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredSchoolList = (ArrayList<SchoolModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    class SchoolViewHolder extends RecyclerView.ViewHolder {
        TextView schoolName;

        SchoolViewHolder(View itemView) {
            super(itemView);
            schoolName = (TextView) itemView.findViewById(R.id.cell_schoolName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AppModel.getInstance().isConnectedToInternet(activity)) {

                        //Drop Table Calendar
                        DatabaseHelper.getInstance(activity).dropTable(DatabaseHelper.TABLE_CALENDAR);
//
//                      //Create New Calendar Table
                        DatabaseHelper.getInstance(activity).createTable(DatabaseHelper.CREATE_TABLE_CALENDAR);

                        AppModel.getInstance().showLoader(activity, "Loading School data", "Please wait...");
                        AppModel.getInstance().setSelectedSchool(activity, filteredSchoolList.get(getAdapterPosition()).getId());
                        AppModel.getInstance().setSpinnerSelectedSchool(activity, filteredSchoolList.get(getAdapterPosition()).getId());
                        AppModel.getInstance().setSearchedSchoolId(activity, filteredSchoolList.get(getAdapterPosition()).getId());
                        AppModel.getInstance().syncMetaData(activity, (SearchSchoolActivity) activity, filteredSchoolList.get(getAdapterPosition()).getId());

                        if (filteredSchoolList.get(getAdapterPosition()).getAllowedModule_App() != null) {
                            List<String> allowedModules = Arrays.asList(filteredSchoolList.get(getAdapterPosition()).getAllowedModule_App().split(","));
                            if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                                AppModel.getInstance().writeToSharedPreferences(activity, AppConstants.HIDE_FEES_COLLECTION, "0");
                            } else {
                                AppModel.getInstance().writeToSharedPreferences(activity, AppConstants.HIDE_FEES_COLLECTION, "1");
                            }
                        }

//                        ((SearchSchoolActivity) activity).enableExpandableList();

                    } else {
                        Toast.makeText(activity, "No internet connectivity", Toast.LENGTH_SHORT).show();
                        AppModel.getInstance().setSelectedSchool(activity, filteredSchoolList.get(getAdapterPosition()).getId());
                        AppModel.getInstance().setSpinnerSelectedSchool(activity, filteredSchoolList.get(getAdapterPosition()).getId());
                        AppModel.getInstance().setSearchedSchoolId(activity, filteredSchoolList.get(getAdapterPosition()).getId());

                        if (filteredSchoolList.get(getAdapterPosition()).getAllowedModule_App() != null) {
                            List<String> allowedModules = Arrays.asList(filteredSchoolList.get(getAdapterPosition()).getAllowedModule_App().split(","));
                            if (allowedModules.contains(AppConstants.FinanceModuleValue)) {
                                AppModel.getInstance().writeToSharedPreferences(activity, AppConstants.HIDE_FEES_COLLECTION, "0");
                            } else {
                                AppModel.getInstance().writeToSharedPreferences(activity, AppConstants.HIDE_FEES_COLLECTION, "1");
                            }
                        }

                        //Clear previous sync data:
                        activity.getSharedPreferences("syncPrefs", Context.MODE_PRIVATE).edit().putString("syncstatus", "false").commit();

                        Intent intent = new Intent(activity, NewDashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }

                }
            });
        }
    }


}
