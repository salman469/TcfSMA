package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcf.sma.Activities.GraduationActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.GraduationModel;
import com.tcf.sma.Models.PromotionDBModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.WithdrawalReasonModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/1/2017.
 */

public class GraduationDialogManager extends Dialog implements View.OnClickListener {

    TextView tv_graduated, tv_non_graduated;
    LinearLayout ll_yes, ll_no;
    String graduatedCount, nonGraduatedCount;
    View rl_graduation_dialog;
    Activity activity;
    ArrayList<GraduationModel> gmList;
    private int classId, sectionId;
    private SchoolClassesModel checkClassIfExists;
    private int schoolSpinnerValue;

    public GraduationDialogManager(Activity activity, String graduatedCount, String nonGraduatedCount, ArrayList<GraduationModel> gmList, int schoolSpinnerValue, int classId, int sectionId) {
        super(activity);
        this.activity = activity;
        this.graduatedCount = graduatedCount;
        this.nonGraduatedCount = nonGraduatedCount;
        this.gmList = gmList;
        this.classId = classId;
        this.sectionId = sectionId;
        this.schoolSpinnerValue = schoolSpinnerValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_graduation_confirmation);
        init();
    }

    void init() {

        tv_graduated = (TextView) findViewById(R.id.tv_graduated);
        tv_graduated.setText("Graduated :  " + graduatedCount);
        tv_non_graduated = (TextView) findViewById(R.id.tv_non_graduated);
        tv_non_graduated.setText("Not-Graduated :  " + nonGraduatedCount);
        ll_yes = (LinearLayout) findViewById(R.id.ll_yes);
        ll_no = (LinearLayout) findViewById(R.id.ll_no);
        ll_yes.setOnClickListener(this);
        ll_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_yes:
                dismiss();
                int graduated = 0;
                long headerId = -1, id = -1;

                SchoolClassesModel classModel = DatabaseHelper.getInstance(activity).getSchoolClassByIds(schoolSpinnerValue, classId, sectionId);

                PromotionDBModel promotionDBModel = new PromotionDBModel();
                promotionDBModel.setCreated_by(DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                promotionDBModel.setCreated_on(AppModel.getInstance().getDate());
                promotionDBModel.setSchoolClassId(classModel.getId());
                headerId = DatabaseHelper.getInstance(activity).insertPromotion(promotionDBModel);


                for (int i = 0; i < gmList.size(); i++) {

                    if (gmList.get(i).getIsGraduated().equals("y")) {
                        int StudentId = StudentModel.getInstance().getStudentsList().get(i).getId();
                        //Student Graduation clause

                        ContentValues contentValues = new ContentValues();
                        ArrayList<WithdrawalReasonModel> list = DatabaseHelper.getInstance(activity).getWithdrawalReasons(false);
                        boolean isFound = false;
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).getReasonName().trim().toLowerCase().equals("graduated from school")) {
                                contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_WITHDRAWN_REASON_ID, list.get(j).getId());
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound) {
                            contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_WITHDRAWN_REASON_ID, 14);
                        }
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_IS_ACTIVE, false);
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_UPLOADED_ON, (String) null);
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_WITHDRAWN_ON, AppModel.getInstance().getDate());
                        contentValues.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                        id = DatabaseHelper.getInstance(activity).updateStudentByColumns(contentValues, StudentId);
                        graduated++;
                    }
                }
                if (headerId > 0 && id > 0) {
                    ((GraduationActivity) activity).MessageBox(graduated + " students graduated successfully", true);

                    //Important when any change in table call this method
                    AppModel.getInstance().changeMenuPendingSyncCount(activity, false);

                }

                break;
            case R.id.ll_no:
                dismiss();
                break;
        }
    }

}
