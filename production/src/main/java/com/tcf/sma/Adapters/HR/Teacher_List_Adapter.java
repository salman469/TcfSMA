package com.tcf.sma.Adapters.HR;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.R;

public class Teacher_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 0:
                return new TeacherDetailHeaderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_list_header, viewGroup, false));
            case 1:
                return new TeacherDetailViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_list_detail, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i % 2 == 1) {
            //holder.rootView.setBackgroundColor(Color.BLACK);
            viewHolder.itemView.setBackgroundResource(R.color.light_app_green);
        } else if (i == 0) {
            viewHolder.itemView.setBackgroundResource(R.color.light_gray);
        } else {
            //holder.rootView.setBackgroundColor(Color.WHITE);
            viewHolder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return 10;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else
            return 1;
    }


    private class TeacherDetailHeaderHolder extends RecyclerView.ViewHolder {
        public TeacherDetailHeaderHolder(View inflate) {
            super(inflate);
        }
    }

    private class TeacherDetailViewHolder extends RecyclerView.ViewHolder {
        TextView txt_TeacherEC, txt_TeachName, txt_TeachDesg;
        Spinner spin_CS, spin_Subject;

        public TeacherDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_TeacherEC = (TextView) itemView.findViewById(R.id.txt_TeacherEC);
            txt_TeachName = (TextView) itemView.findViewById(R.id.txt_TeachName);
            txt_TeachDesg = (TextView) itemView.findViewById(R.id.txt_TeachDesg);
            spin_CS = (Spinner) itemView.findViewById(R.id.spin_CS);
            spin_Subject = (Spinner) itemView.findViewById(R.id.spin_Subject);
        }
    }
}