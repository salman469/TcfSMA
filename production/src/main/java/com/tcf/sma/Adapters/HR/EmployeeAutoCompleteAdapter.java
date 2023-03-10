package com.tcf.sma.Adapters.HR;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tcf.sma.Models.RetrofitModels.HR.EmployeeAutoCompleteModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAutoCompleteAdapter extends ArrayAdapter<EmployeeAutoCompleteModel> {

    List<EmployeeAutoCompleteModel> empAutoCompModel;
    List<EmployeeAutoCompleteModel> filterdEmpList;
    private int viewResourceId;
    private Context context;
    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterdEmpList = empAutoCompModel;
                } else {
                    ArrayList<EmployeeAutoCompleteModel> filteredList = new ArrayList<>();
                    for (EmployeeAutoCompleteModel row : empAutoCompModel) {

                        if ((row.getEmpFirstName() != null && row.getEmpFirstName().toLowerCase().contains(charString.toLowerCase())) ||
                                (row.getEmpLastName() != null && row.getEmpLastName().toLowerCase().contains(charString.toLowerCase()) ||
                                        (row.getEmpCode() != null && row.getEmpCode().toLowerCase().contains(charString.toLowerCase())))
                        ) {
                            filteredList.add(row);
                        }
                    }

                    filterdEmpList = filteredList;
                }
            } else {
                filterdEmpList = empAutoCompModel;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterdEmpList;
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            filterdEmpList = (ArrayList<EmployeeAutoCompleteModel>) filterResults.values;


            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // refresh the list with filtered data
                    notifyDataSetChanged();
                }
            });

        }
    };

    public EmployeeAutoCompleteAdapter(Context context, int viewResourceId, List<EmployeeAutoCompleteModel> model) {
        super(context, viewResourceId, model);
        this.context = context;
        this.empAutoCompModel = model;
        this.filterdEmpList = empAutoCompModel;
        this.viewResourceId = viewResourceId;
    }

    @Override
    public int getCount() {
        if (filterdEmpList != null) {
            return filterdEmpList.size();
        } else
            return 0;
    }

    @Nullable
    @Override
    public EmployeeAutoCompleteModel getItem(int position) {
        return filterdEmpList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }

        TextView emp_code = (TextView) v.findViewById(R.id.emp_code);
        TextView emp_firstName = (TextView) v.findViewById(R.id.emp_firstName);
        TextView emp_lastName = (TextView) v.findViewById(R.id.emp_LastName);

        try {
            EmployeeAutoCompleteModel employee = filterdEmpList.get(position);
            if (employee != null) {

                if (emp_code != null) {
                    emp_code.setText(employee.getEmpCode());
                }
                if (emp_firstName != null) {
                    emp_firstName.setText(employee.getEmpFirstName());
                }
                if (emp_lastName != null) {
                    emp_lastName.setText(employee.getEmpLastName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

}
