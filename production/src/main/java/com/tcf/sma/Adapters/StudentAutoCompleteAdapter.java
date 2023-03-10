package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class StudentAutoCompleteAdapter extends ArrayAdapter<StudentAutoCompleteModel> {
    private final String MY_DEBUG_TAG = "StudentAutoCompleteAdapter";
    List<StudentAutoCompleteModel> StdACmdl;
    List<StudentAutoCompleteModel> filteredStudentList;
    private int viewResourceId;
    private Context context;
    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredStudentList = StdACmdl;
                } else {
                    ArrayList<StudentAutoCompleteModel> filteredList = new ArrayList<>();
                    for (StudentAutoCompleteModel row : StdACmdl) {

                        if ((row.getStudent_name() != null && row.getStudent_name().toLowerCase().contains(charString.toLowerCase())) ||
                                (row.getGrNo() != null && row.getGrNo().toLowerCase().contains(charString.toLowerCase()))
                        ) {
                            filteredList.add(row);
                        }
                    }

                    Collections.sort(filteredList, (s1, s2) -> Integer.compare(Integer.parseInt(s1.getGrNo()),Integer.parseInt(s2.getGrNo())));
                    filteredStudentList = filteredList;
                }
            } else {
                Collections.sort(StdACmdl, (s1, s2) -> Integer.compare(Integer.parseInt(s1.getGrNo()),Integer.parseInt(s2.getGrNo())));
                filteredStudentList = StdACmdl;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredStudentList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredStudentList = (ArrayList<StudentAutoCompleteModel>) filterResults.values;


            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // refresh the list with filtered data
                    notifyDataSetChanged();
                }
            });

        }
    };


    public StudentAutoCompleteAdapter(Context context, int viewResourceId, List<StudentAutoCompleteModel> model) {
        super(context, viewResourceId, model);
        this.context = context;
        this.StdACmdl = model;
        this.filteredStudentList = StdACmdl;
        this.viewResourceId = viewResourceId;
    }

    @Override
    public int getCount() {
        if (filteredStudentList != null) {
            return filteredStudentList.size();
        } else
            return 0;
    }

    @Nullable
    @Override
    public StudentAutoCompleteModel getItem(int position) {
        return filteredStudentList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }

        TextView stud_grNo = (TextView) v.findViewById(R.id.stud_grNo);
        TextView stud_Name = (TextView) v.findViewById(R.id.stud_Name);
        TextView stud_class_section = (TextView) v.findViewById(R.id.stud_class_section);

        try {
            StudentAutoCompleteModel student = filteredStudentList.get(position);
            if (student != null) {

                if (stud_grNo != null) {
                    stud_grNo.setText(student.getGrNo());
                }
                if (stud_Name != null) {
                    stud_Name.setText(student.getStudent_name());
                }
                if (stud_class_section != null) {
                    stud_class_section.setText(student.getClass_section());
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