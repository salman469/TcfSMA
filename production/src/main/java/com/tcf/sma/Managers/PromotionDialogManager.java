package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tcf.sma.Activities.PromotionActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.PromotionDBModel;
import com.tcf.sma.Models.PromotionModel;
import com.tcf.sma.Models.PromotionStudentDBModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/1/2017.
 */

public class PromotionDialogManager extends Dialog implements View.OnClickListener {

    TextView tv_promoted, tv_non_promoted;
    LinearLayout ll_yes, ll_no;
    String promotedCount, nonPromotedCount;
    View rl_promotion_dialog;
    Activity activity;
    PromotionDBModel promotionDBModel;
    ArrayList<PromotionModel> pmList;
    private Ipromotions iPromote;
    private int classId, sectionId, nextClassId, nextSectionId;
    private SchoolClassesModel checkClassIfExists;
    private int schoolSpinnerValue;
    public PromotionDialogManager(Activity activity, String promotedCount, String nonPromotedCount, ArrayList<PromotionModel> pmList, int classId, int sectionId) {
        super(activity);
        this.activity = activity;
        this.promotedCount = promotedCount;
        this.nonPromotedCount = nonPromotedCount;
        this.pmList = pmList;
        this.classId = classId;
        this.sectionId = sectionId;
    }

    public PromotionDialogManager(Activity activity, String promotedCount, String nonPromotedCount, ArrayList<PromotionModel> pmList, int schoolSpinnerValue, int classId, int sectionId) {
        super(activity);
        this.activity = activity;
        this.promotedCount = promotedCount;
        this.nonPromotedCount = nonPromotedCount;
        this.pmList = pmList;
        this.classId = classId;
        this.sectionId = sectionId;
        this.schoolSpinnerValue = schoolSpinnerValue;
    }

    public PromotionDialogManager(Activity activity, String promotedCount, String nonPromotedCount, ArrayList<PromotionModel> pmList, int schoolSpinnerValue, int classId, int sectionId, int nextClassId, int nextSectionId, Ipromotions ipromotions) {
        super(activity);
        this.activity = activity;
        this.promotedCount = promotedCount;
        this.nonPromotedCount = nonPromotedCount;
        this.pmList = pmList;
        this.classId = classId;
        this.sectionId = sectionId;
        this.schoolSpinnerValue = schoolSpinnerValue;
        this.nextClassId = nextClassId;
        this.nextSectionId = nextSectionId;
        iPromote = ipromotions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_promotion_confirmation);
//        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View screen = layoutInflater.inflate(R.layout.dialog_attendance_confirmation, null, false);
//        screen.setBackground(R.drawable.rounded_corner);
        init();
//        setData();
    }

    void init() {

        tv_promoted = (TextView) findViewById(R.id.tv_promoted);
        tv_promoted.setText("Promoted :  " + promotedCount);
        tv_non_promoted = (TextView) findViewById(R.id.tv_non_promoted);
        tv_non_promoted.setText("Not-Promoted :  " + nonPromotedCount);
        ll_yes = (LinearLayout) findViewById(R.id.ll_yes);
        ll_no = (LinearLayout) findViewById(R.id.ll_no);
        ll_yes.setOnClickListener(this);
        ll_no.setOnClickListener(this);
    }

    private void setData() {

//        SurveyAppModel.getInstance().textViewSetText(activity,rl_attendace_dialog,R.id.tv_present);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_yes:
                dismiss();
                int pro = 0;
                int count = 1;
                long headerId = -1, id = -1;
                int promotedSchoolClassID = 0;
                int StudentCount = 0;
                int capacity = 0;
                int selectedStudentCount = 0;
                for (int i = 0; i < pmList.size(); i++) {
                    if (pmList.get(i).getIsPromoted().equals("y")) {
                        selectedStudentCount++;
                    }
                }
                SchoolClassesModel classModel = DatabaseHelper.getInstance(activity).getSchoolClassByIds(schoolSpinnerValue, classId, sectionId);
                checkClassIfExists = DatabaseHelper.getInstance(activity).getSchoolClassByIds(schoolSpinnerValue, nextClassId, nextSectionId);
                StudentCount = DatabaseHelper.getInstance(activity).getStudentCount(checkClassIfExists.getId());
                StudentCount = StudentCount + selectedStudentCount;
                capacity = DatabaseHelper.getInstance(activity).getMaxCapacityFromSchoolClass(checkClassIfExists.getSchoolId(), checkClassIfExists.getId());

                if (StudentCount <= capacity) {
                    if (checkClassIfExists != null) {
                        if (!checkClassIfExists.is_active()) {
                            DatabaseHelper.getInstance(activity).setSchoolClassActive(checkClassIfExists.getId());
                        }
                        promotedSchoolClassID = checkClassIfExists.getId();
                        PromotionDBModel promotionDBModel = new PromotionDBModel();
                        promotionDBModel.setCreated_by(DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                        promotionDBModel.setCreated_on(AppModel.getInstance().getDate());
                        promotionDBModel.setSchoolClassId(classModel.getId());
                        headerId = DatabaseHelper.getInstance(activity).insertPromotion(promotionDBModel);
                    }

                    for (int i = 0; i < pmList.size(); i++) {

                        if (pmList.get(i).getIsPromoted().equals("y")) {
                            int StudentId = StudentModel.getInstance().getStudentsList().get(i).getId();
                            //student promote to next class
                            pro++;
                            if (headerId > 0) {
                                //checking if new class have capacity for new students

                                PromotionStudentDBModel pdbsm = new PromotionStudentDBModel(headerId, StudentId, promotedSchoolClassID);
                                id = DatabaseHelper.getInstance(activity).insertPromotionStudent(pdbsm);
                                if (id > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put(DatabaseHelper.getInstance(activity).STUDENT_SCHOOL_CLASS_ID, promotedSchoolClassID);
                                    cv.put(DatabaseHelper.getInstance(activity).STUDENT_UPLOADED_ON, (String) null);
                                    cv.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                                    cv.put(DatabaseHelper.getInstance(activity).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
                                    if(DatabaseHelper.getInstance(activity).isNineOrTenClass(schoolSpinnerValue, nextClassId, sectionId) &&
                                    DatabaseHelper.getInstance(activity).isRegionIsInFlagShipSchool(schoolSpinnerValue)) {
                                        cv.put(DatabaseHelper.getInstance(activity).STUDENT_PROMOTION_STATUS, "T");
                                    }
                                    DatabaseHelper.getInstance(activity).updateStudentByColumns(cv, StudentId);
                                }
                            }
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Cannot Promote student more than Max Capacity:" + capacity);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }
                if (headerId > 0 && id > 0) {
                    ((PromotionActivity) activity).MessageBox(pro + " students promoted successfully");
                    iPromote.studentsPromoted();

                    //Important when any change in table call this method
                    AppModel.getInstance().changeMenuPendingSyncCount(activity, false);
                }

                break;
            case R.id.ll_no:
                dismiss();
                break;
        }
    }

    public interface Ipromotions {
        void studentsPromoted();
    }

}
