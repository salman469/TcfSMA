package com.tcf.sma.Adapters.FeesCollection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.AccountStatement.AccountStatementActivity;
import com.tcf.sma.Activities.StudentProfileActivity;
import com.tcf.sma.Models.Fees_Collection.StudentCollectionReportModel;
import com.tcf.sma.R;

import java.util.List;

public class CollectionReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    private List<StudentCollectionReportModel> scrm;

    public CollectionReportAdapter(Activity activity, List<StudentCollectionReportModel> scrm) {
        this.activity = activity;
        this.scrm = scrm;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else return position;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView;
        switch (viewType) {
            case 0:
                ItemView = LayoutInflater.from(activity).inflate(R.layout.header_collection_report, parent, false);
                return new CollectionReportAdapter.CollectionHeaderVH(ItemView);

            default:
                ItemView = LayoutInflater.from(activity).inflate(R.layout.item_collection_report, parent, false);
                return new CollectionReportAdapter.CollectionItemVH(ItemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Vholder, int position) {

        if (position > 0 && position < scrm.size() + 1) {

            CollectionItemVH holder = (CollectionItemVH) Vholder;

            // background color change in even odd sequence
            if (position % 2 == 0) {
                holder.stCardView.setBackgroundResource(R.color.light_app_green);
            } else {
                holder.stCardView.setBackgroundResource(R.color.white);
            }

            //index of list is (position-1) for getting right index. 1st row is header row so -1 row.

            String balance = scrm.get(position - 1).getBalance() + "";
            String dues = scrm.get(position - 1).getDues() + "";
            String collected = scrm.get(position - 1).getCollected() + "";

            balance = (balance.contains(".")) ? balance.split("\\.")[0] : balance;
            dues = (dues.contains(".")) ? dues.split("\\.")[0] : dues;
            collected = (collected.contains(".")) ? collected.split("\\.")[0] : collected;

            holder.tv_dues.setText(dues);
            holder.tv_collected.setText(collected);
            holder.tv_remaining.setText(balance);
            holder.tv_classSection.setText(scrm.get(position - 1).getClassSectionName());
            holder.tv_name.setText(scrm.get(position - 1).getStudentName());
            holder.tv_grNo.setText(scrm.get(position - 1).getGrNo() + "");

        } else if (position == scrm.size() + 1) {
            CollectionItemVH holder = (CollectionItemVH) Vholder;
            holder.tv_name.setText("");
            holder.tv_remaining.setText("");
            holder.tv_grNo.setText("");
            holder.tv_collected.setText("");
            holder.tv_dues.setText("");
            holder.tv_classSection.setText("");
            holder.stCardView.setBackgroundResource(R.color.white);

            holder.tv_name.setTextColor(Color.BLACK);
            holder.tv_remaining.setTextColor(Color.BLACK);
            holder.tv_collected.setTextColor(Color.BLACK);
            holder.tv_dues.setTextColor(Color.BLACK);

            holder.tv_name.setTypeface(holder.tv_name.getTypeface(), Typeface.BOLD);
            holder.tv_remaining.setTypeface(holder.tv_remaining.getTypeface(), Typeface.BOLD);
            holder.tv_collected.setTypeface(holder.tv_collected.getTypeface(), Typeface.BOLD);
            holder.tv_dues.setTypeface(holder.tv_dues.getTypeface(), Typeface.BOLD);

            StudentCollectionReportModel srcm = getTotal();

            String remaining = srcm.getRemaining() + "";
            String dues = srcm.getDues() + "";
            String collected = srcm.getCollected() + "";

            remaining = (remaining.contains(".")) ? remaining.split("\\.")[0] : remaining;
            dues = (dues.contains(".")) ? dues.split("\\.")[0] : dues;
            collected = (collected.contains(".")) ? collected.split("\\.")[0] : collected;


            holder.tv_name.setText("Total");
            holder.tv_dues.setText(dues);
            holder.tv_collected.setText(collected);
            holder.tv_remaining.setText(remaining);

        }
    }

    private StudentCollectionReportModel getTotal() {
        int totaldues = 0;
        int totalremaining = 0;
        int totalcollected = 0;

        StudentCollectionReportModel mod = new StudentCollectionReportModel();

        if (scrm != null) {
            for (StudentCollectionReportModel model : scrm) {
                totaldues += model.getDues();
                totalcollected += model.getCollected();
                totalremaining += model.getBalance();
            }
        }

        mod.setDues(totaldues);
        mod.setCollected(totalcollected);
        mod.setRemaining(totalremaining);

        return mod;
    }

    @Override
    public int getItemCount() {
        return scrm == null || scrm.size() == 0 ? 0 : scrm.size() + 2; // one header and one total row included
    }

    public class CollectionItemVH extends RecyclerView.ViewHolder {
        LinearLayout stCardView;
        TextView tv_name, tv_grNo, tv_classSection, tv_dues, tv_collected, tv_remaining;

        public CollectionItemVH(View itemView) {
            super(itemView);
            tv_grNo = (TextView) itemView.findViewById(R.id.tv_grNo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_classSection = (TextView) itemView.findViewById(R.id.tv_classSection);
            tv_dues = (TextView) itemView.findViewById(R.id.tv_dues);
            tv_collected = (TextView) itemView.findViewById(R.id.tv_collected);
            tv_remaining = (TextView) itemView.findViewById(R.id.tv_remaining);
            stCardView = (LinearLayout) itemView.findViewById(R.id.st_card_view);

            tv_remaining.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(activity, AccountStatementActivity.class);
                    in.putExtra("StudentGrNo", scrm.get(getAdapterPosition() - 1).getGrNo() + "");
                    activity.startActivity(in);

                }
            });


            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(activity, "Name:"+tv_student_name.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, StudentProfileActivity.class);
                    intent.putExtra("StudentProfileIndex", getAdapterPosition() - 1);
                    intent.putExtra("StudentGrNo", scrm.get(getAdapterPosition() - 1).getGrNo());
                    intent.putExtra("classId", scrm.get(getAdapterPosition() - 1).getClassId());
                    intent.putExtra("schoolId", scrm.get(getAdapterPosition() - 1).getSchoolId());
                    intent.putExtra("sectionId", scrm.get(getAdapterPosition() - 1).getSecId());
                    (activity).startActivity(intent);
                }
            });

        }
    }

    public class CollectionHeaderVH extends RecyclerView.ViewHolder {
        LinearLayout stCardView;

        public CollectionHeaderVH(View itemView) {
            super(itemView);
            stCardView = (LinearLayout) itemView.findViewById(R.id.st_card_view);
        }
    }
}
