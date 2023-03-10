package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.StudentDropoutActivity;
import com.tcf.sma.Activities.StudentProfileActivity;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SearchModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/6/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    ArrayList<SearchModel> smm;
    Activity activity;

    public SearchAdapter(ArrayList<SearchModel> smm, Activity activity) {
        this.smm = smm;
        this.activity = activity;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.student_view_list_cell, parent, false);
        return new SearchViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.tv_grNo.setText(smm.get(position).getGr_no());
        holder.tv_student_name.setText(smm.get(position).getStudent_name());
//        holder.tv_school_name.setText(smm.get(position).getSchool_name());
//        holder.tv_class.setText(smm.get(position).getClass_name());
        holder.tv_class_sec.setText(smm.get(position).getClass_name().split("\\-")[1] + "-" + smm.get(position).getSection());
    }

    @Override
    public int getItemCount() {
        if (smm != null) {
            return smm.size();
        } else {
            return 0;
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class_sec, tv_grNo, tv_student_name, tv_school_name, tv_class, tv_section;

        public SearchViewHolder(View itemView) {
            super(itemView);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_student_name = (TextView) itemView.findViewById(R.id.tv_name_text);
//            tv_school_name = (TextView) itemView.findViewById(R.id.tv_school_text);
//            tv_class = (TextView) itemView.findViewById(R.id.tv_class_text);
//            tv_section = (TextView) itemView.findViewById(R.id.tv_section_text);
            tv_class_sec = (TextView) itemView.findViewById(R.id.tv_class_sec);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AppModel.getInstance().fragmentTag.equals(activity.getString(R.string.student_profile_text))) {
//                        Fragment fr = new StudentProfileFragment();
//                        FragmentChangeListener fc = (FragmentChangeListener) activity;
//                        fc.Replacefragment(fr, activity.getString(R.string.student_proifle_text));
                        (activity).startActivity(new Intent(activity, StudentProfileActivity.class));
                    } else if (AppModel.getInstance().fragmentTag.equals(activity.getString(R.string.student_dropout_text))) {
//                        Fragment fr = new StudentDropoutFragment();
//                        FragmentChangeListener fc = (FragmentChangeListener) activity;
//                        fc.Replacefragment(fr, activity.getString(R.string.student_dropout_text));
                        (activity).startActivity(new Intent(activity, StudentDropoutActivity.class));

                    }
                }
            });
        }
    }
}
