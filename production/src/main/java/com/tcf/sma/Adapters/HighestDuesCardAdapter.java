package com.tcf.sma.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.CashReceived.CashReceiptActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.FinanceCheckSum;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by Badar Arain on 3/17/2017.
 */

public class HighestDuesCardAdapter extends RecyclerView.Adapter<HighestDuesCardAdapter.BottomFiveViewHolder> {
    Context context;
    ArrayList<HighestDuesStudentsModel> mmodelList;
    private int schoolId;

    public HighestDuesCardAdapter(ArrayList<HighestDuesStudentsModel> modelLsit, Context context) {
        this.context = context;
        this.mmodelList = modelLsit;
    }

    public HighestDuesCardAdapter(ArrayList<HighestDuesStudentsModel> modelLsit, Context context, int schoolId) {
        this.context = context;
        this.mmodelList = modelLsit;
        this.schoolId = schoolId;
    }

    @Override
    public void onViewRecycled(HighestDuesCardAdapter.BottomFiveViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public BottomFiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.new_weakest_attendance, parent, false);
        BottomFiveViewHolder holder = new BottomFiveViewHolder(itemView);

//        int parentWidth = parent.getMeasuredWidth();
//        int width = parentWidth / 2;
//        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//        holder.ll.setLayoutParams(params);
        return holder;
    }

    @Override
    public void onBindViewHolder(BottomFiveViewHolder holder, int position) {

        try {
            if (mmodelList.get(position).getStudntsName() != null && mmodelList.get(position).getClassName() != null) {
                holder.schoolName.setText("School: " + mmodelList.get(position).getSchoolName());
                holder.bottom5Name.setText("Name: " + mmodelList.get(position).getStudntsName());
                holder.bottom5GrNo.setText("GrNo: " + mmodelList.get(position).getStudentGr_NO());

                holder.bottom5ClassSec.setText("Class-Sec: " + mmodelList.get(position).getClassName() + "-" + mmodelList.get(position).getSectionName());
//                holder.bottom5Absent.setText("Amount: " + mmodelList.get(position).getAmount());
                String image = DatabaseHelper.getInstance(context).getStudentImageWithGR(Integer.parseInt(mmodelList.get(position).getStudentGr_NO()), schoolId);
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
                
                
                        
                holder.tv_amount.setText("Amount: " + mmodelList.get(position).getAmount());
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

        LinearLayout ll;
        TextView schoolName, bottom5Name, bottom5GrNo, bottom5ClassSec, bottom5Absent, tv_amount;
        CircleImageView studentImage;

        public BottomFiveViewHolder(View itemView) {
            super(itemView);
            schoolName = itemView.findViewById(R.id.schoolName);
            ll = itemView.findViewById(R.id.ll);
            bottom5Name = itemView.findViewById(R.id.name5);
            bottom5GrNo = itemView.findViewById(R.id.gr_No5);
            bottom5ClassSec = itemView.findViewById(R.id.class_section5);
            bottom5Absent = itemView.findViewById(R.id.absent5);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            studentImage = itemView.findViewById(R.id.studentImage);

            boolean FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(context,
                    AppConstants.FinanceSyncCompleted, false);

            ll.setOnClickListener(v -> {
                if (FinanceSyncCompleted) {
                    if (FinanceCheckSum.Instance(new WeakReference<>(context)).isChecksumSuccess(context, true)) {
                        Intent receiveCashIntent = new Intent(context, CashReceiptActivity.class);
                        receiveCashIntent.putExtra("gr", mmodelList.get(getAdapterPosition()).getStudentGr_NO());
                        receiveCashIntent.putExtra("schoolId", schoolId);
                        context.startActivity(receiveCashIntent);
                    }
                } else {
                    Toast.makeText(context, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
