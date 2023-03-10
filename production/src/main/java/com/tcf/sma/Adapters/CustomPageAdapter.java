package com.tcf.sma.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.appbar.AppBarLayout;
import com.tcf.sma.Activities.AreaManagerStudentSelection;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.UnApprovalDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Badar Jamal on 1/11/2017.
 */

public class CustomPageAdapter extends PagerAdapter {
    public ArrayList<StudentModel> students;
    TextView GrNo;
    ImageView header;
    AppBarLayout appBarLayout;
    private Activity activity;
    private TextView tv_student_name, tv_section_student_Gender, tv_student_GR_No, date_of_admission,
            tv_class, tv_section, tv_dob, tv_formB, tv_father_name, tv_father_cnic,
            tv_father_occupation, tv_mother_name, tv_mother_cnic, tv_mother_occupation,
            tv_guardian_name, tv_guardian_cnic, tv_guardian_occupation, tv_previous_school,
            tv_class_previous_school, tv_address, tv_contact;
    private Button approved;
    private Button unApproved;
    private int grno;

    public CustomPageAdapter(ArrayList<StudentModel> students, Context context, int grno) {
        this.activity = (Activity) context;
        this.students = students;
        this.grno = grno;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.activity_student_profile_validation, container, false);
        tv_student_name = (TextView) layout.findViewById(R.id.tv_student_name);
        tv_section_student_Gender = (TextView) layout.findViewById(R.id.tv_section_student_Gender);
        tv_student_GR_No = (TextView) layout.findViewById(R.id.tv_student_GR_No);
        date_of_admission = (TextView) layout.findViewById(R.id.date_of_admission);
        tv_class = (TextView) layout.findViewById(R.id.tv_class);
        tv_section = (TextView) layout.findViewById(R.id.tv_section);
        tv_dob = (TextView) layout.findViewById(R.id.tv_dob);
        tv_formB = (TextView) layout.findViewById(R.id.tv_formB);
        tv_father_name = (TextView) layout.findViewById(R.id.tv_father_name);
        tv_father_cnic = (TextView) layout.findViewById(R.id.tv_father_cnic);
        tv_father_occupation = (TextView) layout.findViewById(R.id.tv_father_occupation);
        tv_mother_name = (TextView) layout.findViewById(R.id.tv_mother_name);
        tv_mother_cnic = (TextView) layout.findViewById(R.id.tv_mother_cnic);
        tv_mother_occupation = (TextView) layout.findViewById(R.id.tv_mother_occupation);
        tv_guardian_name = (TextView) layout.findViewById(R.id.tv_guardian_name);
        tv_guardian_cnic = (TextView) layout.findViewById(R.id.tv_guardian_cnic);
        tv_guardian_occupation = (TextView) layout.findViewById(R.id.tv_guardian_occupation);
        tv_previous_school = (TextView) layout.findViewById(R.id.tv_previous_school);
        tv_class_previous_school = (TextView) layout.findViewById(R.id.tv_class_previous_school);
        tv_address = (TextView) layout.findViewById(R.id.tv_address);
        tv_contact = (TextView) layout.findViewById(R.id.tv_contact);
        header = (ImageView) layout.findViewById(R.id.header);
        appBarLayout = (AppBarLayout) layout.findViewById(R.id.appbar);
//        if (!StudentModel.getInstance().getStudentsList().get(position).isApproved()) {
//            appBarLayout.setBackgroundColor(activity.getResources().getColor(R.color.light_red_color));
//        }

        tv_student_name.setText(students.get(position).getName());
        tv_section_student_Gender.setText(students.get(position).getGender());
        tv_student_GR_No.setText(students.get(position).getGrNo());
        date_of_admission.setText(students.get(position).getEnrollmentDate());
        tv_class.setText(String.valueOf(students.get(position).getCurrentClass()));
        tv_section.setText(students.get(position).getCurrentSection());
        tv_dob.setText(students.get(position).getDob());
        tv_formB.setText(students.get(position).getFormB());
        tv_father_name.setText(students.get(position).getFathersName());
        tv_father_cnic.setText(students.get(position).getFatherNic());
        tv_father_occupation.setText(students.get(position).getFatherOccupation());
        tv_mother_name.setText(students.get(position).getMotherName());
        tv_mother_cnic.setText(students.get(position).getMotherNic());
        tv_mother_occupation.setText(students.get(position).getMotherOccupation());
        tv_guardian_name.setText(students.get(position).getGuardianName());
        tv_guardian_cnic.setText(students.get(position).getGuardianNic());
        tv_guardian_occupation.setText(students.get(position).getGuardianOccupation());
        tv_previous_school.setText(students.get(position).getPreviousSchoolName());
        tv_class_previous_school.setText(students.get(position).getPreviousSchoolClass());
        tv_address.setText(students.get(position).getAddress1());
        tv_contact.setText(students.get(position).getContactNumbers());
        try {
            header.setImageBitmap(AppModel.getInstance()
                    .setImage("P", students.get(position).getPictureName(),
                            activity, Integer.parseInt(students.get(position).getGrNo()),false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            header.setImageDrawable(activity.getResources().getDrawable(R.mipmap.profile_pic));
        }
        approved = (Button) layout.findViewById(R.id.btn_student_validation_approve);
        unApproved = (Button) layout.findViewById(R.id.btn_student_validation_unapprove);

        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox(students.get(position).getName(), position);
            }
        });

        unApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnApprovalDialogManager dialogManager = new UnApprovalDialogManager(activity, StudentModel.getInstance().getStudentsList().get(position).getId(), position, CustomPageAdapter.this);
                dialogManager.show();
            }
        });


        //((TextView) layout.findViewById(R.id.textView)).setText(modelArrayList.get(position).getStudentName());
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        /* int size=students.size();*/
        return students.size();

    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void messageBox(String StudentName, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage("Do you want to approve " + StudentName + " ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_IS_APPROVED, true);
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_UPLOADED_ON, (String) null);
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_APPROVED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_UNAPPROVED_COMMENTS, (String) null);
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_APPROVED_BY, DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());

                        DatabaseHelper.getInstance(activity).updateStudentByColumns(contentValues, StudentModel.getInstance().getStudentsList().get(position).getId());
                        if (AreaManagerStudentSelection.vp_studentSelection.getAdapter().getCount() == position + 1) {
                            AreaManagerStudentSelection.vp_studentSelection.setCurrentItem((position) - 1);
                            ((AreaManagerStudentSelection) activity).MessageBox("Approved!", true);
                            ((AreaManagerStudentSelection) activity).removePage(position);
                        } else {
                            AreaManagerStudentSelection.vp_studentSelection.setCurrentItem((position) + 1);
                            ((AreaManagerStudentSelection) activity).MessageBox("Approved!", true);
                            ((AreaManagerStudentSelection) activity).removePage(position);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
