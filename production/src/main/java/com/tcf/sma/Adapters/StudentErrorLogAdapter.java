package com.tcf.sma.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentErrorFetchModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class StudentErrorLogAdapter extends RecyclerView.Adapter<StudentErrorLogAdapter.StudentErrorLogVH> {
    Context context;
    ArrayList<StudentErrorFetchModel> mmodelList;


    public StudentErrorLogAdapter(ArrayList<StudentErrorFetchModel> modelLsit, Context context) {
        this.context = context;
        this.mmodelList = modelLsit;
    }

    @Override
    public StudentErrorLogVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_error_log_2, parent, false);
        return new StudentErrorLogVH(itemView);
    }

    @Override
    public void onBindViewHolder(StudentErrorLogVH holder, int position) {

        String created_on = mmodelList.get(position).getCreated_on();
        String date = "", time = "";
        if (created_on != null && !created_on.isEmpty()) {
            String[] arr = created_on.split("\\s+");
            if (arr.length > 0)
                date = arr[0];
            if (arr.length > 1)
                time = arr[1];
        }

        holder.studentName.setText(mmodelList.get(position).getStudentName());
        holder.class_section.setText(mmodelList.get(position).getClassSection());
        holder.grNo.setText(mmodelList.get(position).getStudentGr());
        holder.message.setText(mmodelList.get(position).getMessage());
        holder.created_on.setText(date + "\n" + time);


    }

    @Override
    public int getItemCount() {
        return mmodelList.size();
    }

    public class StudentErrorLogVH extends RecyclerView.ViewHolder {
        TextView studentName, class_section, grNo, message, created_on;

        public StudentErrorLogVH(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.tv_studentName);
            class_section = (TextView) itemView.findViewById(R.id.tv_classSection);
            grNo = (TextView) itemView.findViewById(R.id.tv_studentGr);
            message = (TextView) itemView.findViewById(R.id.tv_message);
            created_on = (TextView) itemView.findViewById(R.id.tv_createdOn);


            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppModel.getInstance().maximizeFieldDialog(mmodelList.get(getAdapterPosition()).getMessage(), context);
                }
            });

        }
    }
}
