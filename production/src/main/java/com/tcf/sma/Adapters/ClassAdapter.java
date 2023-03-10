package com.tcf.sma.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 1/4/2017.
 */

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    Activity activity;
    ArrayList<ClassModel> cm;

    public ClassAdapter(Activity activity, ArrayList<ClassModel> classModels) {
        this.activity = activity;
        this.cm = classModels;
    }

    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview;
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_cell, parent, false);
        return new ClassAdapter.ClassViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(ClassAdapter.ClassViewHolder holder, int position) {
        holder.cell_className.setText(cm.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (cm != null) {
            return cm.size();
        } else
            return 0;
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView cell_className;

        ClassViewHolder(View itemView) {
            super(itemView);
            cell_className = (TextView) itemView.findViewById(R.id.cell_className);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}