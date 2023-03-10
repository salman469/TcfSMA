package com.tcf.sma.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.BottomFiveStudentsModel;
import com.tcf.sma.R;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by Badar Arain on 3/17/2017.
 */

public class BottomFiveCardAdapter extends RecyclerView.Adapter<BottomFiveCardAdapter.BottomFiveViewHolder> {
    Context context;
    ArrayList<BottomFiveStudentsModel> mmodelList;
    Boolean isWeakestAttendance;
    int schoolId;

    public BottomFiveCardAdapter(ArrayList<BottomFiveStudentsModel> modelLsit, Context context, Boolean isWeakestAttendance, int schoolId) {
        this.context = context;
        this.mmodelList = modelLsit;
        this.isWeakestAttendance = isWeakestAttendance;
        this.schoolId = schoolId;
    }

    @Override
    public void onViewRecycled(BottomFiveCardAdapter.BottomFiveViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public BottomFiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (!isWeakestAttendance) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.bottom_five_card_cell, parent, false);
            return new BottomFiveViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.new_weakest_attendance, parent, false);
            return new BottomFiveViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(BottomFiveViewHolder holder, final int position) {

        try {
            if (mmodelList.get(position).getStudntsName() != null && mmodelList.get(position).getClassName() != null) {
                holder.schoolName.setText("School: " + mmodelList.get(position).getSchoolId());
                holder.bottom5Name.setText("Name: " + mmodelList.get(position).getStudntsName());
                holder.bottom5GrNo.setText("GrNo: " + mmodelList.get(position).getStudentGr_NO());
                String currentClass = mmodelList.get(position).getClassName();
                if (currentClass.toLowerCase().contains("class")) {
                    holder.bottom5ClassSec.setText("Class-Sec: " + mmodelList.get(position).getClassName().split("-")[1] + "-" + " " + mmodelList.get(position).getSectionName());
                } else {
                    holder.bottom5ClassSec.setText("Class-Sec: " + currentClass);
                }

                if(isWeakestAttendance){
                    String image = DatabaseHelper.getInstance(context).getStudentImageWithGR(mmodelList.get(position).getStudentGr_NO(), schoolId);
                    if(image.trim().length() > 0){
                        try {
                            File f;
                            String fdir = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Images";
                            if (image.contains("BodyPart"))
                                f = new File(fdir + "/" + image);
                            else
                                f = new File(image);

                            if (f.exists()) {
                                Bitmap bitmap = Compressor.getDefault(context).compressToBitmap(f);
                                holder.studentImage.setImageBitmap(bitmap);
                            }

//                header.setImageBitmap(SurveyAppModel.getInstance().setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            holder.studentImage.setImageResource(R.mipmap.profile_pic);
                        }
                    }
                }



//                holder.bottom5Absent.setText("Attendance Percentage: " + (mmodelList.get(position).getStudentsAbsentCounting() < 0 ? 0 : mmodelList.get(position).getStudentsAbsentCounting()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        new AttendaceReportDialogManager((Activity) context, mmodelList.get(position).getStudentsID(), mmodelList.get(position).getSchoolClassId()).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mmodelList.size();
    }

    public class BottomFiveViewHolder extends RecyclerView.ViewHolder {
        TextView schoolName, bottom5Name, bottom5GrNo, bottom5ClassSec, bottom5Absent, tv_amount;
        CircleImageView studentImage;

        public BottomFiveViewHolder(View itemView) {
            super(itemView);
            schoolName = (TextView) itemView.findViewById(R.id.schoolName);
            bottom5Name = (TextView) itemView.findViewById(R.id.name5);
            bottom5GrNo = (TextView) itemView.findViewById(R.id.gr_No5);
            bottom5ClassSec = (TextView) itemView.findViewById(R.id.class_section5);
            bottom5Absent = (TextView) itemView.findViewById(R.id.absentPercentage);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_amount.setVisibility(View.GONE);
            studentImage = itemView.findViewById(R.id.studentImage);
        }
    }
}
