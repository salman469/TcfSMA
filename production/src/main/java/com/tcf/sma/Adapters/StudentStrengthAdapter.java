package com.tcf.sma.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/12/2017.
 */

public class StudentStrengthAdapter extends RecyclerView.Adapter<StudentStrengthAdapter.StudentStrengthViewHolder> {
    ArrayList<ClassModel> modelList;
    Activity activity;
    int selectedSchoolID = 0;

    public StudentStrengthAdapter(ArrayList<ClassModel> modelList, Activity activity) {
        this.modelList = modelList;
        this.activity = activity;
        selectedSchoolID = AppModel.getInstance().getSelectedSchool(activity);
    }

    @Override
    public void onViewRecycled(StudentStrengthAdapter.StudentStrengthViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public StudentStrengthAdapter.StudentStrengthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enrollmentstatus_cell, parent, false);
        View ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_strength_list_cell, parent, false);
        return new StudentStrengthViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(StudentStrengthViewHolder holder, int position) {
        holder.tv_student_strength_class.setText(modelList.get(position).getName());
        holder.tv_student_strength_count.setText("" + DatabaseHelper.getInstance(activity).getStudentClassCount("" + AppModel.getInstance().getSelectedSchool(activity), modelList.get(position).getClassId()));


    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class StudentStrengthViewHolder extends RecyclerView.ViewHolder {
        TextView tv_student_strength_class, tv_student_strength_count, tv_visitingDate;


        public StudentStrengthViewHolder(View itemView) {
            super(itemView);
            tv_student_strength_class = (TextView) itemView.findViewById(R.id.tv_student_strength_class);
            tv_student_strength_count = (TextView) itemView.findViewById(R.id.tv_student_strength_count);
            tv_visitingDate = (TextView) itemView.findViewById(R.id.et_visting_date);
        }
    }
}
