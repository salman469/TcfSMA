package com.tcf.sma.Adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Interfaces.IProcessComplete;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.CustomRangeInputFilter;

import java.util.ArrayList;

public class StudentFeeSetAdapter extends RecyclerView.Adapter<StudentFeeSetAdapter.StudentViewHolder> {
    int flag;
    int SectionId, ClassID, schoolId = 0;
    private ArrayList<StudentModel> smList;
    private Activity activity;
    private boolean doValidate = false;
    private IProcessComplete processComplete;


    public StudentFeeSetAdapter(ArrayList<StudentModel> smList, Activity activity, IProcessComplete processComplete, int flag, int ClassID, int SectionId, int schoolId) {
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.schoolId = schoolId;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
        this.processComplete = processComplete;
    }


    @Override
    public StudentFeeSetAdapter.StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.row_fee_set, parent, false);
        final StudentViewHolder vh = new StudentFeeSetAdapter.StudentViewHolder(ItemView);

        vh.et_student_monthlyfee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    if (!editable.toString().isEmpty()) {
//                        int monthlyfee = 0;
                        if (!editable.toString().equals("-")) {
//                            monthlyfee = Integer.valueOf(editable.toString());
                            if (!editable.toString().isEmpty() && Integer.parseInt(editable.toString()) < 10) {
                                vh.et_student_monthlyfee.setError("Monthly fee should be greater than 10");
                                smList.get(vh.getAdapterPosition()).setMonthlyfee(String.valueOf(editable.toString().isEmpty() ? 0 :
                                        Integer.parseInt(editable.toString())));
                                vh.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
                            } else {
                                smList.get(vh.getAdapterPosition()).setMonthlyfee(String.valueOf(editable.toString().isEmpty() ? 0 :
                                        Integer.parseInt(editable.toString())));
                                vh.et_student_monthlyfee.setError(null);
                                vh.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_app_green));
                            }
                        } else {
                            smList.get(vh.getAdapterPosition()).setMonthlyfee("");
                        }
                    } else {
                        smList.get(vh.getAdapterPosition()).setMonthlyfee("0");
                    }
                    processComplete.onProcessCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(StudentFeeSetAdapter.StudentViewHolder holder, int position) {


        //just check whether or not the fee has been entered. if entered then disable the edittext
        if (doValidate) {
            String monthlyFee = smList.get(position).getMonthlyfee();
            if (monthlyFee == null || monthlyFee.isEmpty()) {
                holder.et_student_monthlyfee.setError("Monthly fee should be greater than 10 and less than or equal to 1500");
            }
        }
        if (doValidate && position == smList.size() - 1) {
            doValidate = false;
        }

        holder.tv_grNo.setText(smList.get(position).getGrNo());
        holder.tv_student_name.setText(smList.get(position).getName());
        if (smList.get(position).getActualFees() < 10 || smList.get(position).getActualFees() > 1500) {
            holder.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red_color));
        } else {
            holder.stCardView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_app_green));
        }
        holder.tv_student_id.setText(smList.get(position).getId() + "");
        holder.et_student_monthlyfee.setText(smList.get(position).getMonthlyfee());
    }

    @Override
    public int getItemCount() {
        if (smList != null)
            return smList.size();
        else return 0;
    }

    public void validate() {
        doValidate = true;
        notifyDataSetChanged();
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_grNo, tv_student_name, tv_student_id;
        EditText et_student_monthlyfee;
        CardView stCardView;

        public StudentViewHolder(View itemView) {
            super(itemView);
            tv_grNo = (TextView) itemView.findViewById(R.id.gr_No);
            tv_student_name = (TextView) itemView.findViewById(R.id.tv_name_text);
            stCardView = (CardView) itemView.findViewById(R.id.st_card_view);
            tv_student_id = (TextView) itemView.findViewById(R.id.tv_student_id);
            et_student_monthlyfee = itemView.findViewById(R.id.et_student_monthlyfee);
            et_student_monthlyfee.setFilters(new InputFilter[]{new CustomRangeInputFilter(AppConstants.STUDENT_MONTHLY_FEE_LOWER_RANGE, AppConstants.STUDENT_MONTHLY_FEE_UPPER_RANGE)});
        }
    }
}
