package com.tcf.sma.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.StudentDropoutActivity;
import com.tcf.sma.Activities.StudentDropoutSearchActivity;
import com.tcf.sma.Activities.StudentProfileActivity;
import com.tcf.sma.Activities.StudentProfileSearchActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by Mohammad.Haseeb on 2/2/2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentModel> smList;
    Activity activity;
    int flag;
    int SectionId, ClassID, schoolId = 0;
    boolean openFinance;
    String category = "";
    String receivables = "";

    public StudentAdapter(ArrayList<StudentModel> smList, Activity activity, int flag, int ClassID, int SectionId) {
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
    }

    public StudentAdapter(ArrayList<StudentModel> smList, Activity activity, int flag, int ClassID, int SectionId, int schoolId) {
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.schoolId = schoolId;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
    }

    public StudentAdapter(ArrayList<StudentModel> smList, Activity activity, int flag, int ClassID, int SectionId, int schoolId, boolean openFincance) {
        this.smList = smList;
        this.activity = activity;
        this.flag = flag;
        this.schoolId = schoolId;
        this.ClassID = ClassID;
        this.SectionId = SectionId;
        this.openFinance = openFincance;
    }

    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(activity).inflate(R.layout.new_student_view_list_item, parent, false);
        return new StudentAdapter.StudentViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(StudentAdapter.StudentViewHolder holder, int position) {

        int progress = 0;
       /* if(smList.get(position).getContactNumbers() != null && !smList.get(position).getContactNumbers().trim().equals(""))
            progress += 80;
        if(smList.get(position).getAddress1() != null && !smList.get(position).getAddress1().trim().equals(""))
            progress += 20;
*/
        if(smList.get(position).getDob()!=null && !smList.get(position).getDob().equals("")){
            progress +=20;
        }
        if(smList.get(position).getFormB()!=null && !smList.get(position).getFormB().equals("")){
            progress +=10;
        }

        if(smList.get(position).getFathersName()!=null && !smList.get(position).getFathersName().equals("")){
            progress +=10;
        }
        if(smList.get(position).getFatherNic()!=null && !smList.get(position).getFatherNic().equals("")){
            progress +=20;
        }
        if(smList.get(position).getContactNumbers()!=null && !smList.get(position).getContactNumbers().equals("")){
            progress +=10;
        }
        if(smList.get(position).getFatherOccupation()!=null && !smList.get(position).getFatherOccupation().equals("")){
            progress +=10;
        }




        String currentClass = StudentModel.getInstance().getStudentsList().get(position).getCurrentClass();
        if (currentClass.toLowerCase().contains("class")) {
            holder.tv_class_sec.setText(currentClass.split("\\-")[1] + " " + StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        } else {
            holder.tv_class_sec.setText(currentClass+" "+StudentModel.getInstance().getStudentsList().get(position).getCurrentSection());
        }

        if(currentClass.contains("9")||currentClass.contains("10")) {
            holder.promotionStatus.setVisibility(View.VISIBLE);
            holder.promotionStatus.setText(getStatus(smList.get(position).getStudent_promotionStatus()));
        } else {
            holder.promotionStatus.setVisibility(View.GONE);
        }

        holder.tv_grNo.setText(" - " +smList.get(position).getGrNo());
//        smList.get(position).
        holder.tv_student_name.setText(changeStringCase(smList.get(position).getName()));
        if (smList.get(position).isActive()) {
//            holder.ll_1.setBackgroundResource(R.color.light_app_green);
            holder.tv_status.setText("Active");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.app_green));
            holder.tv_grNo.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tv_student_id.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tv_monthly_fee.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tv_admission_date.setTextColor(activity.getResources().getColor(R.color.black));
            if(StudentModel.getInstance().getStudentsList().get(position).getCurrentSection().contains("Blue"))
                holder.tv_class_sec.setTextColor(activity.getResources().getColor(R.color.blue_color));
            else if (StudentModel.getInstance().getStudentsList().get(position).getCurrentSection().contains("Green"))
                holder.tv_class_sec.setTextColor(activity.getResources().getColor(R.color.app_green));
            if(smList.get(position).getGender().equals("M"))
                holder.tv_student_name.setTextColor(activity.getResources().getColor(R.color.blue_color));
            else if (smList.get(position).getGender().equals("F"))
                holder.tv_student_name.setTextColor(activity.getResources().getColor(R.color.pink));
            receivables = DatabaseHelper.getInstance(activity).getAllhighestDuesOfStudent(String.valueOf(schoolId),String.valueOf(smList.get(position).getId()));
            holder.tv_receivables.setText("RS. "+receivables);
            try {
                if(Integer.parseInt(receivables)<=0){
                    holder.tv_receivables.setTextColor(activity.getResources().getColor(R.color.app_green));
                }
                else
                {
                    holder.tv_receivables.setTextColor(activity.getResources().getColor(R.color.red));
                }
            } catch (Exception e){
                holder.tv_receivables.setTextColor(activity.getResources().getColor(R.color.app_green));
            }

         /*   if(progress < 50)
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_profile_progress_red));
            else if(progress < 80)
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
            else
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_progress_bar_green));*/
//            holder.progressCount.setTextColor(activity.getResources().getColor(R.color.app_green));
//            holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_progress_bar_green));
        } else {
//            holder.ll_1.setBackgroundResource(R.color.light_red_color);
            holder.tv_status.setText("Withdrawn");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.red_color));

            holder.progressCount.setText("0%");
            holder.progressBar.setProgress(0);

            holder.tv_receivables.setTextColor(activity.getResources().getColor(R.color.app_green));
        }
        holder.tv_student_id.setText(smList.get(position).getId() + "");
        if (!Strings.isEmptyOrWhitespace(smList.get(position).getEnrollmentDate())) {
            String addmissionDate = "";
            //Format is checking because every time the format changed in db
            String format = AppModel.getInstance().determineDateFormat(smList.get(position).getEnrollmentDate());
            if (format != null) {
                addmissionDate = AppModel.getInstance().convertDatetoFormat(smList.get(position).getEnrollmentDate(), format, "dd-MMM-yy");
            }
            holder.tv_admission_date.setText(addmissionDate + " > Current");
//            holder.tv_admission_date.setText(AppModel.getInstance().convertDatetoFormat(smList.get(position).getEnrollmentDate(),
//                    "yyyy-MM-dd", "dd-MMM-yy") + " > Current");
        }

        if (openFinance || DatabaseHelper.getInstance(activity).isRegionIsInFlagShipSchool(schoolId)) {
            String monthlyFee = String.valueOf((int) smList.get(position).getActualFees());
            holder.tv_monthly_fee.setText("Rs. " + monthlyFee);

            ScholarshipCategoryModel model = Scholarship_Category.getInstance(activity).getScholarshipCategory(
                    schoolId,
                    (monthlyFee.isEmpty() ? -1 : Integer.parseInt(monthlyFee.trim())));

            if (model.getScholarship_category_description() != null && !model.getScholarship_category_description().isEmpty()) {
                holder.tv_category.setText("(" + model.getScholarship_category_description() + ")");
                category = "(" + model.getScholarship_category_description() + ")";
            } else {
                holder.tv_category.setText("");
            }
        }

//        is_withdrawl = 0 AND withdrawal_reason_id = 14  for graduated student
        if (smList.get(position).isActive() && !smList.get(position).isWithdrawal() && smList.get(position).getWithdrawalReasonId() == 14) {
//            holder.ll_1.setBackgroundResource(R.color.light_app_green);
            holder.tv_status.setText("Active");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.app_green));
            holder.tv_grNo.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tv_student_id.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tv_monthly_fee.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tv_admission_date.setTextColor(activity.getResources().getColor(R.color.black));
            if(currentClass.contains("Blue"))
                holder.tv_class_sec.setTextColor(activity.getResources().getColor(R.color.blue_color));
            else if (currentClass.contains("Green"))
                holder.tv_class_sec.setTextColor(activity.getResources().getColor(R.color.app_green));
            if(smList.get(position).getGender().equals("M"))
                holder.tv_student_name.setTextColor(activity.getResources().getColor(R.color.blue_color));
            else if (smList.get(position).getGender().equals("F"))
                holder.tv_student_name.setTextColor(activity.getResources().getColor(R.color.pink));


/*
            if(progress < 50)
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_profile_progress_red));
            else if(progress < 80)
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
            else
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_progress_bar_green));
*/

        } else if (!smList.get(position).isActive() && !smList.get(position).isWithdrawal() && smList.get(position).getWithdrawalReasonId() == 14) {
//            holder.ll_1.setBackgroundResource(R.color.light_blue_color);
            holder.tv_status.setText("Graduated");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.blue_color));
        }

        try {
            if (smList.get(position).getPictureName() != null && !smList.get(position).getPictureName().isEmpty()) {
                File f;
                String fdir = StorageUtil.getSdCardPath(activity).getAbsolutePath() + "/TCF/TCF Images";
                if (smList.get(position).getPictureName().contains("BodyPart"))
                    f = new File(fdir + "/" + smList.get(position).getPictureName());
                else
                    f = new File(fdir + "/" + smList.get(position).getPictureName());

                if (f.exists()) {
                    Bitmap bitmap = Compressor.getDefault(activity).compressToBitmap(f);
                    holder.studentProfileImage.setImageBitmap(bitmap);
                    progress += 20;
                } else {
                    holder.studentProfileImage.setImageResource(R.mipmap.profile_pic);
                }
            } else {
                holder.studentProfileImage.setImageResource(R.mipmap.profile_pic);
            }
//                header.setImageBitmap(SurveyAppModel.getInstance().setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo())));
        } catch (Exception e) {
            e.printStackTrace();
            holder.studentProfileImage.setImageResource(R.mipmap.profile_pic);
        }

        if (smList.get(position).isActive()) {
            holder.progressCount.setText(progress+"%");
            holder.progressBar.setProgress(progress);
            if(progress < 50)
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_profile_progress_red));
            else if(progress < 80)
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
            else
                holder.progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.employee_progress_bar_green));
        }

    }

    @Override
    public int getItemCount() {
        if (smList != null) {
            return smList.size();
        } else {
            return 0;
        }
    }

    private String getStatus(String status) {
        if(Strings.isEmptyOrWhitespace(status))
            return "N/A";
        switch (status) {
            case "T":
                return "To Be Verified";

            case "P":
                return "Available for Approval";

            case "A":
                return "Approved";

            case "F":
                return "Not Approved";

            default:
                return "N/A";
        }
    }

    public String countActiveStudents() {
        int studentCount = 0;
        for (StudentModel sm : StudentModel.getInstance().getStudentsList()) {
//            if (sm.isActive()) {
            studentCount++;
//            }
        }
        return " " + studentCount;
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_class_sec, tv_grNo, tv_student_name, tv_student_id, tv_admission_date,
                tv_monthly_fee, tv_category, tv_status, progressCount,tv_receivables, promotionStatus;
//        CardView stCardView;
        LinearLayout ll_1;
        ProgressBar progressBar;
        CircleImageView


                studentProfileImage;

        public StudentViewHolder(View itemView) {
            super(itemView);
            studentProfileImage = itemView.findViewById(R.id.studentImage);
            progressCount = itemView.findViewById(R.id.profileProgressCount);
            progressBar = itemView.findViewById(R.id.profileProgressBar);
            tv_class_sec = itemView.findViewById(R.id.tv_class_sec);
            tv_grNo = itemView.findViewById(R.id.gr_No);
            tv_student_name = itemView.findViewById(R.id.tv_name_text);
//            stCardView = itemView.findViewById(R.id.st_card_view);
            ll_1 = itemView.findViewById(R.id.student_listing_item);
            tv_status = itemView.findViewById(R.id.status);
            tv_student_id = itemView.findViewById(R.id.tv_student_id);
            tv_admission_date = itemView.findViewById(R.id.tv_admission_date);
            tv_receivables = itemView.findViewById(R.id.tv_receivables);
            tv_monthly_fee = itemView.findViewById(R.id.tv_monthly_fee);
            tv_category = itemView.findViewById(R.id.tv_category);
            promotionStatus = itemView.findViewById(R.id.promotionStatus);

            if (openFinance || DatabaseHelper.getInstance(activity).isRegionIsInFlagShipSchool(schoolId)) {
                tv_monthly_fee.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.VISIBLE);
            } else {
                tv_monthly_fee.setVisibility(View.GONE);
                tv_category.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ClassID < 0 || SectionId < 0) {
                        ClassSectionModel model = DatabaseHelper.getInstance(view.getContext()).getClassSectionBySchoolIdAndSchoolClassId(schoolId, smList.get(getAbsoluteAdapterPosition()).getSchoolClassId());
                        ClassID = model.getClassId();
                        SectionId = model.getSectionId();
                    }
                    if (flag == 0) { // if flag is 0 then Student Profile else Student Dropout
                        Intent intent = new Intent(activity, StudentProfileActivity.class);
                        intent.putExtra("StudentProfileIndex", getAdapterPosition());
                        intent.putExtra("StudentGrNo", Integer.valueOf(smList.get(getAdapterPosition()).getGrNo()));
                        intent.putExtra("classId", ClassID);
                        intent.putExtra("schoolId", schoolId);
                        intent.putExtra("sectionId", SectionId);
                        intent.putExtra("isFinanceOpen", ((StudentProfileSearchActivity) activity).isFinanceOpen);
                        if (!Strings.isEmptyOrWhitespace(smList.get(getAdapterPosition()).getEnrollmentDate())) {
                            String admDate = "";
                            //Format is checking because every time the format changed in db
                            String format = AppModel.getInstance().determineDateFormat(smList.get(getAdapterPosition()).getEnrollmentDate());
                            if (format != null) {
                                admDate = AppModel.getInstance().convertDatetoFormat(smList.get(getAdapterPosition()).getEnrollmentDate(), format, "dd-MMM-yy");
                            }
                            intent.putExtra("admissiondate", admDate + " > Current");
//                            intent.putExtra("admissiondate", AppModel.getInstance().convertDatetoFormat(smList.get(getAdapterPosition()).getEnrollmentDate(),
//                                    "yyyy-MM-dd", "dd-MMM-yy") + " > Current");
                        }

                        if (StudentModel.getInstance().getStudentsList().get(getAdapterPosition()).getCurrentClass().toLowerCase().contains("class")) {
                            intent.putExtra("class_sec",StudentModel.getInstance().getStudentsList().get(getAdapterPosition()).getCurrentClass().split("\\-")[1] + " " + StudentModel.getInstance().getStudentsList().get(getAdapterPosition()).getCurrentSection());
                        } else {
                            intent.putExtra("class_sec",StudentModel.getInstance().getStudentsList().get(getAdapterPosition()).getCurrentClass()+ " " + StudentModel.getInstance().getStudentsList().get(getAdapterPosition()).getCurrentSection());

                        }
                        if (openFinance || DatabaseHelper.getInstance(activity).isRegionIsInFlagShipSchool(schoolId)) {
                            intent.putExtra("category", category );
                        }
                        intent.putExtra("studentid", smList.get(getAdapterPosition()).getId() );
                        intent.putExtra("grno", smList.get(getAdapterPosition()).getGrNo() );
                        intent.putExtra("monthlyfees", (int) smList.get(getAdapterPosition()).getActualFees());
                        //intent.putExtra("receivables", "SectionId");
                        if (smList.get(getAdapterPosition()).isActive()){
                            intent.putExtra("status", "Active");
                        }
                        else {
                            intent.putExtra("status", "Withdrawn");
                        }
                        (activity).startActivity(intent);
                    } else {
                        Intent intent = new Intent(activity, StudentDropoutActivity.class);
                        intent.putExtra("StudentDropoutIndex", getAdapterPosition());
                        (activity).startActivity(intent);
                    }

                }
            });

            if (smList != null) {
                if (activity instanceof StudentProfileSearchActivity) {
                    ((StudentProfileSearchActivity) activity).totalCount.setText(countActiveStudents());
                } else if (activity instanceof StudentDropoutSearchActivity) {
                    ((StudentDropoutSearchActivity) activity).tv_total_students.setText(countActiveStudents());
                }
            } else {
                if (activity instanceof StudentProfileSearchActivity) {
                    ((StudentProfileSearchActivity) activity).totalCount.setText(" 0");
                } else if (activity instanceof StudentDropoutSearchActivity) {
                    ((StudentDropoutSearchActivity) activity).tv_total_students.setText(" 0");
                }
            }
        }
    }

    public String changeStringCase(String s) {

        final String DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString(); }
}
