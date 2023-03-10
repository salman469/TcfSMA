package com.tcf.sma.Managers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcf.sma.Activities.AttendanceActivity;
import com.tcf.sma.Adapters.AttendanceAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Interfaces.OnButtonEnableDisableListener;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendanceStudentModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Mohammad.Haseeb on 2/1/2017.
 */

public class AttendanceDialogManager extends Dialog implements View.OnClickListener {

    TextView tv_present, tv_absent, tv_on_leave;
    LinearLayout ll_yes, ll_no;
    View rl_attendace_dialog;
    Activity activity;
    String attendanceDate;
    int schoolId, classId, sectionId;
    int p = 0, a = 0, l = 0;
    boolean isFromAttendancePending;
    private boolean doUpdate;
    private OnButtonEnableDisableListener onButtonEnableDisableListener;

    public AttendanceDialogManager(Activity activity, String attendanceDate, int schoolId, int classId, int sectionId, boolean doUpdate, boolean isFromAttendancePending, OnButtonEnableDisableListener listener) {
        super(activity);
        this.activity = activity;
        this.attendanceDate = attendanceDate;
        this.schoolId = schoolId;
        this.classId = classId;
        this.sectionId = sectionId;
        this.doUpdate = doUpdate;
        this.isFromAttendancePending = isFromAttendancePending;
        this.onButtonEnableDisableListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_attendance_confirmation);
//        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View screen = layoutInflater.inflate(R.layout.dialog_attendance_confirmation, null, false);
//        screen.setBackground(R.drawable.rounded_corner);
        init();
        setData();
    }

    void init() {

        rl_attendace_dialog = findViewById(R.id.rl_attendance_dialog);
        tv_present = (TextView) findViewById(R.id.tv_present);
        tv_absent = (TextView) findViewById(R.id.tv_absent);
        tv_on_leave = (TextView) findViewById(R.id.tv_on_leave);
        ll_yes = (LinearLayout) findViewById(R.id.ll_yes);
        ll_no = (LinearLayout) findViewById(R.id.ll_no);

        ll_yes.setOnClickListener(this);
        ll_no.setOnClickListener(this);
    }

    private void setData() {
        setTotalAttendancecountText();

    }

    public void setTotalAttendancecountText() {

        try {
            for (int i = 0; i < AttendanceAdapter.am.size(); i++) {
                if (AttendanceAdapter.am.get(i).getAttendance() != null) {
                    switch (AttendanceAdapter.am.get(i).getAttendance()) {
                        case "p":
                            p++;
                            break;
                        case "a":
                            a++;
                            break;
                    }
                }
            }
            tv_present.setText(activity.getString(R.string.present) + " " + p);
            tv_absent.setText(activity.getString(R.string.absent) + " " + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_yes:
                if (doUpdate) {
                    updateAttendance();
                    onButtonEnableDisableListener.onSubmitClicked();
                } else {
                    addAttendance();
                    onButtonEnableDisableListener.onSubmitClicked();
//                dismiss();
//                activity.finish();
                }
                break;
            case R.id.ll_no:
                onButtonEnableDisableListener.onSubmitClicked();
                dismiss();
                break;
        }
    }

    private void updateAttendance() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String date = df.format(Calendar.getInstance().getTime());
        try {
            int SchoolClassId = DatabaseHelper.getInstance(activity).getSchoolClassByIds(schoolId, classId, sectionId).getId();
            AttendanceModel attendanceModel = new AttendanceModel();
            attendanceModel.setForDate(attendanceDate);
            attendanceModel.setCreatedBy(DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
            attendanceModel.setCreatedOn(date);
            attendanceModel.setSchoolId(SchoolClassId);
            attendanceModel.setUploadedOn(null);
            long hId = DatabaseHelper.getInstance(activity).findAttendanceHeaderId(attendanceDate, String.valueOf(SchoolClassId));
            if (hId > 0) {
                long id = DatabaseHelper.getInstance(activity).updateAttendance(attendanceModel, (int) hId);
                if (id > 0) {
                    if (AttendanceAdapter.am.size() >= p) {
                        AttendanceStudentModel asm;
                        for (int i = 0; i < AttendanceAdapter.am.size(); i++) {
                            switch (AttendanceAdapter.am.get(i).getAttendance()) {
                                case "a":
                                    asm = new AttendanceStudentModel();
                                    asm.setAttendanceId((int) hId);
                                    asm.setStudentId(StudentModel.getInstance().getStudentsList().get(i).getId());
                                    asm.setAbsent(true);
                                    asm.setReason("");
                                    if (DatabaseHelper.getInstance(activity).updateAttendanceStudent(asm) == 0) {
                                        DatabaseHelper.getInstance(activity).addAttendanceStudent(asm);
                                    }
                                    break;
                                case "l":
                                    asm = new AttendanceStudentModel();
                                    asm.setAttendanceId((int) hId);
                                    asm.setStudentId(StudentModel.getInstance().getStudentsList().get(i).getId());
                                    asm.setAbsent(false);
                                    asm.setReason("");
                                    if (DatabaseHelper.getInstance(activity).updateAttendanceStudent(asm) == 0) {
                                        DatabaseHelper.getInstance(activity).addAttendanceStudent(asm);
                                    }
                                    break;
                                case "p":
                                    DatabaseHelper.getInstance(activity).deleteStudentAttendance(StudentModel.getInstance().getStudentsList().get(i).getId(), (int) hId);
                                    break;
                            }
                        }
                    }

                } else {
                    ((AttendanceActivity) activity).MessageBox("Something went wrong. Cannot mark attendance!");
                }
                dismiss();
                if (hId > 0) {
                    ((AttendanceActivity) activity).MessageBox("Attendance mark successfully!", true);
                    //Important when any change in table call this method
                    AppModel.getInstance().changeMenuPendingSyncCount(activity, false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addAttendance() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String date = df.format(Calendar.getInstance().getTime());
        try {
            AttendanceModel attendanceModel = new AttendanceModel();
            attendanceModel.setForDate(attendanceDate);
            attendanceModel.setCreatedBy(DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());
            attendanceModel.setCreatedOn(date);
            attendanceModel.setSchoolId(DatabaseHelper.getInstance(activity).getSchoolClassByIds(schoolId, classId, sectionId).getId()); // school_class_id to be inserted here
//            attendanceModel.setSchoolId(schoolId); // school_class_id to be inserted here
            long hId = DatabaseHelper.getInstance(activity).addAttendance(attendanceModel);
            if (hId > 0) {
                if (AttendanceAdapter.am.size() >= p) {
                    AttendanceStudentModel asm;
                    for (int i = 0; i < AttendanceAdapter.am.size(); i++) {
                        switch (AttendanceAdapter.am.get(i).getAttendance()) {
                            case "a":
                                asm = new AttendanceStudentModel();
                                asm.setAttendanceId((int) hId);
                                asm.setStudentId(StudentModel.getInstance().getStudentsList().get(i).getId());
                                asm.setAbsent(true);
                                asm.setReason("");
                                DatabaseHelper.getInstance(activity).addAttendanceStudent(asm);
                                break;
                            case "l":
                                asm = new AttendanceStudentModel();
                                asm.setAttendanceId((int) hId);
                                asm.setStudentId(StudentModel.getInstance().getStudentsList().get(i).getId());
                                asm.setAbsent(false);
                                asm.setReason("");
                                DatabaseHelper.getInstance(activity).addAttendanceStudent(asm);
                                break;
                        }
                    }
                }

            } else {
                ((AttendanceActivity) activity).MessageBox("Something went wrong. Cannot mark attendance!");
            }
            dismiss();
            if (hId > 0) {
                ((AttendanceActivity) activity).MessageBox("Attendance mark successfully!", true);
                ((AttendanceActivity) activity).setAdapter();

                //Important when any change in table call this method
                AppModel.getInstance().changeMenuPendingSyncCount(activity, false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
