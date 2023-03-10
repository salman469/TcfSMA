package com.tcf.sma.Adapters;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.AttendanceActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    public static ArrayList<AttendanceModel> am = null;
    public static int p = 0, a = 0;
    private Activity context;
    private boolean isAttendanceAvailable;
    private int roleId;

    public AttendanceAdapter(ArrayList<AttendanceModel> am, Activity context, boolean isAttendanceAvailable) {
        this.am = am;
        this.context = context;
        this.isAttendanceAvailable = isAttendanceAvailable;
        roleId = DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId();

    }

//    @Override
//    public void onViewRecycled(AttendanceViewHolder holder) {
//        super.onViewRecycled(holder);
//    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.attendance_cell, parent, false);
        return new AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AttendanceViewHolder holder, final int position) {
        if (roleId == AppConstants.roleId_103_V || roleId == AppConstants.roleId_7_AEM) {
            holder.absent.setEnabled(false);
            holder.present.setEnabled(false);
        }

        holder.tv_gr.setText(StudentModel.getInstance().getStudentsList().get(position).getGrNo());
        holder.tv_name.setText(StudentModel.getInstance().getStudentsList().get(position).getName());

        String currentClass = StudentModel.getInstance().getStudentsList().get(position).getCurrentClass();
        if (currentClass.toLowerCase().contains("class")) {
            holder.tvClass.setText(currentClass.split("\\-")[1] + "-" + StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        } else {
            holder.tvClass.setText(currentClass);
        }

        holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_app_green));
        if (am.get(position).getAttendance() != null) {
            if (am.get(position).getAttendance().equals("p")) {
                holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_app_green));
                holder.tv_attendance.setText("Present");
                holder.tv_attendance_head.setText("P");
                holder.ll_attendance.setBackgroundColor(ContextCompat.getColor(context,R.color.app_green));
//                holder.present.setChecked(true);
            } else if (am.get(position).getAttendance().equals("a")) {
                holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
                holder.tv_attendance.setText("Absent");
                holder.tv_attendance_head.setText("A");
                holder.ll_attendance.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
//                holder.absent.setChecked(true);
            }
        }
        if (!isAttendanceAvailable) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
            holder.present.setEnabled(false);
            holder.absent.setEnabled(false);
        }

        ((AttendanceActivity) context).setTotalAttendancecountText();
        holder.itemView.setOnClickListener(v -> {
            ColorDrawable cardColor = (ColorDrawable) holder.card.getBackground();
            int colorId = cardColor.getColor();
            if (colorId == ContextCompat.getColor(context, R.color.light_red_color)){
                am.get(position).setAttendance("p");

                ((AttendanceActivity) context).setTotalAttendancecountText();
//
                holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_app_green));
                holder.tv_attendance.setText("Present");
                holder.tv_attendance_head.setText("P");
                holder.ll_attendance.setBackgroundColor(ContextCompat.getColor(context,R.color.app_green));
            }
            else if(colorId == ContextCompat.getColor(context, R.color.light_app_green)){
                am.get(position).setAttendance("a");

                ((AttendanceActivity) context).setTotalAttendancecountText();

                holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
                holder.tv_attendance.setText("Absent");
                holder.tv_attendance_head.setText("A");
                holder.ll_attendance.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
            }
        });
//        holder.attendanceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (radioGroup.getCheckedRadioButtonId()) {
//                    case R.id.present:
//                        am.get(position).setAttendance("p");
//
//                        ((AttendanceActivity) context).setTotalAttendancecountText();
////
//                        holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_app_green));
//                        break;
//                    case R.id.absent:
//                        am.get(position).setAttendance("a");
//
//                        ((AttendanceActivity) context).setTotalAttendancecountText();
//
//                        holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
//                        break;
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (StudentModel.getInstance().getStudentsList() != null && StudentModel.getInstance().getStudentsList().size() != 0) {
            return StudentModel.getInstance().getStudentsList().size();
        } else {
            return 0;
        }
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gr, tv_name, tvClass,tv_attendance_head,tv_attendance;
        RadioGroup attendanceGroup;
        CardView card;
        RadioButton present, absent;
        LinearLayout ll_attendance;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            tv_gr = (TextView) itemView.findViewById(R.id.gr_No);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name_text);
            tvClass = (TextView) itemView.findViewById(R.id.tv_class_text);
            attendanceGroup = (RadioGroup) itemView.findViewById(R.id.rg_attendance);
            card = (CardView) itemView.findViewById(R.id.attendacnce_cell_card);
            present = (RadioButton) itemView.findViewById(R.id.present);
            absent = (RadioButton) itemView.findViewById(R.id.absent);

            ll_attendance = itemView.findViewById(R.id.ll_attendance);
            tv_attendance_head = itemView.findViewById(R.id.tv_attendance_head);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
        }
    }

}
