package com.tcf.sma.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.MarshMallowPermission;
//import com.tcf.sma.Interfaces.CameraPickClick;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import id.zelory.compressor.Compressor;

public class NewAdmissionActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback,
        RadioGroup.OnCheckedChangeListener {

    String uploaded_on = "";
    private View view;
    private MarshMallowPermission permission;
    private Button btn_capture, btn_nic_capture, btn_admission_capture, btn_remove,
            btn_nic_remove, btn_admission_remove,
            btn_preview, btn_nic_preview, btn_admission_preview;
    private ImageView iv_capture;
    private ImageView iv_nic_capture;
    private ImageView iv_admission_capture;
    private EditText et_GrNo, et_Name, et_monthly_fees;
    private Button btn_submit;
    private TextView tv_status, tv_comments;
    private long id = -1, id1;
    private int idUpdate;
    private String P, B, A;
    private LinearLayout llstatus, llcomments;
    private EnrollmentImageModel modelp;
    private EnrollmentImageModel modela;
    private EnrollmentImageModel modelb;
    private int enrollmentid;
    private int markComplete = 0;
    private String status;
    private Spinner SchoolSpinner, spn_class_section_name, GenderSpinner;
    private int classSectionId = 0;
    private ClassSectionModel classSectionModel;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private int SchoolSpinnerValue = 0;
    private SchoolModel schoolModel;
    private boolean isSelected = false;
    private int tempSchoolClassID = 0;
    private int studentId = 0;
    private ArrayAdapter<String> GenderAdapter;
    private String GenderText = "";
    private LinearLayout ll_gender, ll_monthly_fees;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female;
    private AppCompatTextView profilePictureInfo;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (AppModel.getInstance().img1 != null) {
                byte[] data = AppModel.getInstance().img1;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv_capture.setImageBitmap(bitmap);
            }
            if (AppModel.getInstance().img2 != null) {
                byte[] data = AppModel.getInstance().img2;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv_nic_capture.setImageBitmap(bitmap);
            }
            if (AppModel.getInstance().img3 != null) {
                byte[] data = AppModel.getInstance().img3;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv_admission_capture.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(NewAdmissionActivity.this, "Error in getting Image:" + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_new_admission);
        setToolbar("New Enrollment", this, false);
        init(view);

        try {
            if (getIntent().getStringExtra("gender") != null && !getIntent().getStringExtra("gender").isEmpty()) {
                String gender = getIntent().getStringExtra("gender");
                if (gender.toLowerCase().equals("m")) {
//                    GenderSpinner.setSelection(1);
                    rg_gender.check(R.id.rb_male);
                } else {
                    rg_gender.check(R.id.rb_female);
//                    GenderSpinner.setSelection(2);
                }
//                GenderSpinner.setEnabled(false);
//                GenderSpinner.setClickable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        uploaded_on = getIntent().hasExtra("uploaded_on") ? getIntent().getStringExtra("uploaded_on") : "";

        if (getIntent().hasExtra("status")) {
            String comments = "";
            modelp = null;
            modelb = null;
            modela = null;

            if (getIntent().hasExtra("comments")) {
                comments = getIntent().getStringExtra("comments");
            }
            if (getIntent().hasExtra("enr_id")) {
                enrollmentid = getIntent().getIntExtra("enr_id", 0);
                modelp = DatabaseHelper.getInstance(this).getEnrollmentImage(enrollmentid, "P");
                modelb = DatabaseHelper.getInstance(this).getEnrollmentImage(enrollmentid, "B");
                modela = DatabaseHelper.getInstance(this).getEnrollmentImage(enrollmentid, "A");
            }
            try {
                status = getIntent().getStringExtra("status");
                switch (status) {
                    case AppConstants.PROFILE_REJECTED_KEY:
                        tv_status.setText("Rejected");
                        if (comments.length() > 0)
                            tv_comments.setText(comments);
                        llcomments.setVisibility(View.VISIBLE);
                        llstatus.setVisibility(View.VISIBLE);
                        if (modelp != null && modelp.getReview_status() != null && modelp.getReview_status().equals("R")) {
                            iv_capture.setBackground(getResources().getDrawable(R.drawable.image_red_square));
                        }
                        if (modelb != null && modelb.getReview_status() != null && modelb.getReview_status().equals("R")) {
                            iv_nic_capture.setBackground(getResources().getDrawable(R.drawable.image_red_square));
                        }
                        if (modela != null && modela.getReview_status() != null && modela.getReview_status().equals("R")) {
                            iv_admission_capture.setBackground(getResources().getDrawable(R.drawable.image_red_square));
                        }
                        break;
                    case AppConstants.PROFILE_COMPLETE_KEY:
                        tv_status.setText("Complete");
                        if (comments.length() > 0)
                            tv_comments.setText(comments);
                        else
                            tv_comments.setText("Waiting for review");
                        llcomments.setVisibility(View.VISIBLE);
                        llstatus.setVisibility(View.VISIBLE);
                        break;
                    case AppConstants.PROFILE_INCOMPLETE_KEY:
                        tv_status.setText("In-complete");
                        llcomments.setVisibility(View.GONE);
                        break;
                    case AppConstants.PROFILE_APPROVED_KEY:
                        llcomments.setVisibility(View.VISIBLE);
                        llstatus.setVisibility(View.VISIBLE);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (getIntent().getStringExtra("grFromStudentProfileActivity") != null && !getIntent().getStringExtra("grFromStudentProfileActivity").equals("")) {

            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            et_GrNo.setText(getIntent().getStringExtra("grFromStudentProfileActivity"));
            et_GrNo.setEnabled(false);

            idUpdate = DatabaseHelper.getInstance(this).getEnrollmentIdfromGR(Integer.parseInt(getIntent().getStringExtra("grFromStudentProfileActivity")), SchoolSpinnerValue);

            if (idUpdate < 0) {
                btn_submit.setText("Submit");
            } else if (idUpdate > 0) {
                btn_submit.setText("Update");
                try {
                    EnrollmentImageModel enrollmentImageModel = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "P");
                    if (enrollmentImageModel.getFilename() != null && !enrollmentImageModel.getFilename().equals("")) {
                        File f;
                        String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                        if (enrollmentImageModel.getFilename().contains("BodyPart"))
                            f = new File(fdir + "/" + enrollmentImageModel.getFilename());
                        else
                            f = new File(fdir + "/" + enrollmentImageModel.getFilename());

                        Bitmap bitmap = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f), getWindowManager().getDefaultDisplay());
                        iv_capture.setImageBitmap(bitmap);
                    }

                    EnrollmentImageModel enrollmentImageModel1 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "B");
                    if (enrollmentImageModel1.getFilename() != null && !enrollmentImageModel1.getFilename().equals("")) {

                        File f1;
                        String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                        if (enrollmentImageModel1.getFilename().contains("BodyPart"))
                            f1 = new File(fdir + "/" + enrollmentImageModel1.getFilename());
                        else
                            f1 = new File(fdir + "/" + enrollmentImageModel1.getFilename());

                        Bitmap bitmap1 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f1), getWindowManager().getDefaultDisplay());
                        iv_nic_capture.setImageBitmap(bitmap1);
                    }

                    EnrollmentImageModel enrollmentImageModel2 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "A");
                    if (enrollmentImageModel2.getFilename() != null && !enrollmentImageModel2.getFilename().equals("")) {

                        File f2;
                        String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                        if (enrollmentImageModel2.getFilename().contains("BodyPart"))
                            f2 = new File(fdir + "/" + enrollmentImageModel2.getFilename());
                        else
                            f2 = new File(fdir + "/" + enrollmentImageModel2.getFilename());
                        Bitmap bitmap2 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f2), getWindowManager().getDefaultDisplay());
                        iv_admission_capture.setImageBitmap(bitmap2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (getIntent().getStringExtra("name") != null && !getIntent().getStringExtra("name").isEmpty()) {
            et_Name.setText(getIntent().getStringExtra("name"));
//            et_Name.setEnabled(false);
        }
        //Muhammad Salman Saleem
        if(getIntent().hasExtra("SchoolID")){
            SchoolSpinnerValue = getIntent().getIntExtra("SchoolID", 0);
            if(isFlagShipSchool()){
                setEditTextMaxLength(et_GrNo,5);
            }
            //Written by salman saleem//
        }

        if (getIntent().hasExtra("class_section_id") && getIntent().hasExtra("school_id")) {
            classSectionId = getIntent().getIntExtra("class_section_id", 0);
            SchoolSpinnerValue = getIntent().getIntExtra("school_id", 0);
            Log.d("school_id", SchoolSpinnerValue + "");
            Log.d("ClassID", classSectionId + "");

            classSectionModel = new ClassSectionModel();
            classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
            classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
            spn_class_section_name.setAdapter(new ArrayAdapter<ClassSectionModel>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList()));
            spn_class_section_name.setOnItemSelectedListener(this);

            setSpinner(SchoolSpinnerValue, classSectionId);
            isSelected = false;
            tempSchoolClassID = classSectionId;

        }


        if (getIntent().getStringExtra("gr") != null && !getIntent().getStringExtra("gr").equals("")) {
            setToolbar("Edit Enrollment", this, false);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            et_GrNo.setText(getIntent().getStringExtra("gr"));
//            et_GrNo.setEnabled(false);

            btn_submit.setText("Update");

            idUpdate = DatabaseHelper.getInstance(this).getEnrollmentIdfromGR(Integer.parseInt(getIntent().getStringExtra("gr")), SchoolSpinnerValue);
            EnrollmentImageModel enrollmentImageModel = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "P");
            try {
                if (enrollmentImageModel != null) {
                    if (enrollmentImageModel.getFilename() != null) {
                        if (!enrollmentImageModel.getFilename().equals("")) {
                            File f;
                            String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                            if (enrollmentImageModel.getFilename().contains("BodyPart"))
                                f = new File(fdir + "/" + enrollmentImageModel.getFilename());
                            else
                                f = new File(fdir + "/" + enrollmentImageModel.getFilename());

                            Bitmap bitmap = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f), getWindowManager().getDefaultDisplay());
                            iv_capture.setImageBitmap(bitmap);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            EnrollmentImageModel enrollmentImageModel1 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "B");
            try {
                if (enrollmentImageModel1 != null) {
                    if (enrollmentImageModel1.getFilename() != null) {
                        if (!enrollmentImageModel1.getFilename().equals("")) {
                            File f1;
                            String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                            if (enrollmentImageModel1.getFilename().contains("BodyPart"))
                                f1 = new File(fdir + "/" + enrollmentImageModel1.getFilename());
                            else
                                f1 = new File(fdir + "/" + enrollmentImageModel1.getFilename());

                            Bitmap bitmap1 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f1), getWindowManager().getDefaultDisplay());
                            iv_nic_capture.setImageBitmap(bitmap1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            EnrollmentImageModel enrollmentImageModel2 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "A");
            try {
                if (enrollmentImageModel2 != null) {
                    if (enrollmentImageModel2.getFilename() != null) {
                        if (!enrollmentImageModel2.getFilename().equals("")) {

                            File f2;
                            String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                            if (enrollmentImageModel2.getFilename().contains("BodyPart"))
                                f2 = new File(fdir + "/" + enrollmentImageModel2.getFilename());
                            else
                                f2 = new File(fdir + "/" + enrollmentImageModel2.getFilename());

                            Bitmap bitmap2 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f2), getWindowManager().getDefaultDisplay());
                            iv_admission_capture.setImageBitmap(bitmap2);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String allowedFinanceModule = ((SchoolModel) SchoolSpinner.getSelectedItem()).getAllowedModule_App();
            if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                    || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                if (getIntent().getStringExtra("gr") != null) {
                    int grNo = Integer.parseInt(getIntent().getStringExtra("gr"));
                    EnrollmentModel em = DatabaseHelper.getInstance(this).getEnrollmentfromGR(grNo, SchoolSpinnerValue);
                    if (em.getMonthly_fee() > 0) {
                        et_monthly_fees.setText((int) em.getMonthly_fee() + "");

                    }
                    ll_monthly_fees.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private void init(View rootView) {

        SchoolSpinner = (Spinner) rootView.findViewById(R.id.spn_school);
        btn_capture = (Button) rootView.findViewById(R.id.btn_capture);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit_new_student);
        btn_nic_capture = (Button) rootView.findViewById(R.id.btn_nic_capture);
        btn_admission_capture = (Button) rootView.findViewById(R.id.btn_admission_capture);
        btn_remove = (Button) rootView.findViewById(R.id.btn_remove);
        btn_nic_remove = (Button) rootView.findViewById(R.id.btn_nic_remove);
        btn_admission_remove = (Button) rootView.findViewById(R.id.btn_admission_remove);
        btn_preview = (Button) rootView.findViewById(R.id.btn_preview);
        btn_nic_preview = (Button) rootView.findViewById(R.id.btn_nic_preview);
        btn_admission_preview = (Button) rootView.findViewById(R.id.btn_admission_preview);
        String guidelineText =
                "Use White/Blue background.\n"+
                        "\n" +
                        "Picture should not be blur.\n"+
                        "\n" +
                        "Picture should be bright & with proper light.\n"+
                        "\n" +
                        "Take a close-up of the full face and shoulders.\n"+
                        "\n" +
                        "Picture should be within the green boundaries.\n";
//                "Eye level. Picture should show the student looking straight on, and have a close-up of the full face and shoulder\n" +
//                "\n" +
//                "Things to Avoid. Do not take a picture of a student polaroid picture. Student should be captured live. Student must not be wearing mask or covering face";


        profilePictureInfo = rootView.findViewById(R.id.profile_picture_guideline);
        profilePictureInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppModel.getInstance().showMessage(new WeakReference<>(NewAdmissionActivity.this),
                        "Instructions",
                        guidelineText);

            }
        });

        iv_capture = (ImageView) rootView.findViewById(R.id.iv_capture);
        iv_nic_capture = (ImageView) rootView.findViewById(R.id.iv_nic_capture);
        iv_admission_capture = (ImageView) rootView.findViewById(R.id.iv_admission_capture);
        et_GrNo = (EditText) rootView.findViewById(R.id.et_GrNo);
        et_Name = (EditText) rootView.findViewById(R.id.et_Name);
        spn_class_section_name = (Spinner) rootView.findViewById(R.id.spn_class_section_name);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_comments = (TextView) findViewById(R.id.tv_comments);
        llstatus = (LinearLayout) findViewById(R.id.llstatus);
        llcomments = (LinearLayout) findViewById(R.id.llcomments);

        btn_capture.setOnClickListener(this);
        btn_nic_capture.setOnClickListener(this);
        btn_admission_capture.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        btn_nic_remove.setOnClickListener(this);
        btn_admission_remove.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        btn_nic_preview.setOnClickListener(this);
        btn_admission_preview.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

//        GenderSpinner = (Spinner) view.findViewById(R.id.spn_gender);
//        GenderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.gender_array));
//        GenderSpinner.setAdapter(GenderAdapter);
//        GenderSpinner.setOnItemSelectedListener(this);
        rg_gender = (RadioGroup) view.findViewById(R.id.rg_gender);
        rb_male = (RadioButton) view.findViewById(R.id.rb_male);
        rb_female = (RadioButton) view.findViewById(R.id.rb_female);

        rg_gender.setOnCheckedChangeListener(this);
        rb_male.setChecked(true); //by default gender is male

        ll_gender = (LinearLayout) view.findViewById(R.id.ll_gender);
        ll_gender.setVisibility(View.VISIBLE);

        ll_monthly_fees = (LinearLayout) view.findViewById(R.id.ll_monthly_fees);
        et_monthly_fees = (EditText) view.findViewById(R.id.et_monthly_fees);


        populateSpinner();


    }

    private void setSpinner(int schoolid, int classSectionID) {
        for (SchoolModel sm : schoolModel.getSchoolsList()) {
            if (sm.getId() == schoolid) {
                SchoolSpinner.setSelection(schoolModel.getSchoolsList().indexOf(sm));
                break;
            }
        }

        for (ClassSectionModel csm : classSectionModel.getClassAndSectionsList()) {
            if (csm.getSchoolClassId() == classSectionID) {
                spn_class_section_name.setSelection(classSectionModel.getClassAndSectionsList().indexOf(csm));
                break;
            }
        }
    }

    private void populateSpinner() {

        schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
        schoolModel.getSchoolsList().add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1) {
            SchoolSpinner.setSelection(indexOfSelectedSchool);
            isSelected = true;
        }
        SchoolSpinner.setOnItemSelectedListener(this);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        spn_class_section_name.setAdapter(new ArrayAdapter<ClassSectionModel>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList()));
        spn_class_section_name.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_capture:
                AppModel.getInstance().photoFlag = 1;
                openCameraGalleryDialog("Profile Image");
//                openCamera();
                break;
            case R.id.btn_nic_capture:
                AppModel.getInstance().photoFlag = 2;
                try {
                    PickImageDialog.build(new PickSetup().setTitle("NIC Image")
                            .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult r) {

                            try {
                                if (r.getError() == null) {
                                    AppModel.getInstance().img2 = AppModel.getInstance().bitmapToByte(r.getBitmap());
                                    iv_nic_capture.setImageBitmap(r.getBitmap());
                                } else {
                                    Toast.makeText(NewAdmissionActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(NewAdmissionActivity.this,"Error in Imageview:" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(this,"Error in Imageview:" + e.getMessage());
                }
//                openCameraGalleryDialog("NIC Image");
                break;
            case R.id.btn_admission_capture:
                AppModel.getInstance().photoFlag = 3;
//                openCameraGalleryDialog("Admission Form Image");
                try {
                    PickImageDialog.build(new PickSetup().setTitle("Admission Form Image")
                            .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult r) {
                            try {
                                if (r.getError() == null) {
                                    AppModel.getInstance().img3 = AppModel.getInstance().bitmapToByte(r.getBitmap());
                                    iv_admission_capture.setImageBitmap(r.getBitmap());
                                } else {
                                    Toast.makeText(NewAdmissionActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(NewAdmissionActivity.this,"Error in Imageview:" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(this,"Error in Imageview:" + e.getMessage());
                }
                break;
            case R.id.iv_capture:
                break;
            case R.id.iv_nic_capture:
                break;
            case R.id.iv_admission_capture:
                break;

            case R.id.btn_preview:
                previewImage(iv_capture.getDrawable());
                break;
            case R.id.btn_nic_preview:
                previewImage(iv_nic_capture.getDrawable());
                break;
            case R.id.btn_admission_preview:
                previewImage(iv_admission_capture.getDrawable());
                break;

            case R.id.btn_remove:
                AppModel.getInstance().img1 = null;
                Picasso.with(this).load(R.mipmap.profile_pic).into(iv_capture);
                break;
            case R.id.btn_nic_remove:
                AppModel.getInstance().img2 = null;
                Picasso.with(this).load(R.mipmap.bform).into(iv_nic_capture);
                break;
            case R.id.btn_admission_remove:
                AppModel.getInstance().img3 = null;
                Picasso.with(this).load(R.mipmap.bform).into(iv_admission_capture);
                break;

            case R.id.btn_submit_new_student:
                Log.i("submit","Count");
                final int schoolId = SchoolSpinnerValue;
                String allowedFinanceModule = ((SchoolModel) SchoolSpinner.getSelectedItem()).getAllowedModule_App();
                int newEnrollmentCount = DatabaseHelper.getInstance(current_activity).getNewEnrollmentCount(schoolId, classSectionId);
                int StudentCount = DatabaseHelper.getInstance(current_activity).getStudentCount(classSectionId);
//                StudentCount = StudentCount + newEnrollmentCount;
                int capacity = DatabaseHelper.getInstance(current_activity).getMaxCapacityFromSchoolClass(schoolId, classSectionId);
//                int schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
                int createdBy = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId();
                if (btn_submit.getText().toString().equals("Update")) {

                    if (uploaded_on == null || uploaded_on.equals("")) {
                        if (status.equals(AppConstants.PROFILE_COMPLETE_KEY)) {
                            Toast.makeText(NewAdmissionActivity.this, "This student can't be updated", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (uploaded_on != null && !uploaded_on.equals("")) {
                        if (!status.equals(AppConstants.PROFILE_REJECTED_KEY)) {
                            Toast.makeText(NewAdmissionActivity.this, "This student can't be updated", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    //Update Enrollment
                    if (validate()) {
                        if (tempSchoolClassID == classSectionId || StudentCount < capacity) {
                            long isGrnoExists = DatabaseHelper.getInstance(NewAdmissionActivity.this).FindGRNOSTUDENTPROFILE(et_GrNo.getText().toString(), String.valueOf(schoolId));
                            String previousGrNo = getIntent().getStringExtra("gr");
                            if (isGrnoExists == -1 || et_GrNo.getText().toString().trim().equals(previousGrNo)){
                                setSubmitButtonEnabled(false);
                                et_GrNo.setError(null);
                                final EnrollmentModel enrollmentModel = new EnrollmentModel(Integer.parseInt(et_GrNo.getText().toString()), schoolId, createdBy, AppModel.getInstance().getDate(), null, null, null, null);
                                enrollmentModel.setGender(GenderText.toUpperCase());
                                enrollmentModel.setClass_section_id(classSectionId);
                                enrollmentModel.setStudentName(AppModel.getInstance().changeStringCase(et_Name.getText().toString()));
                                if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                                        || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                                    double monthlyFee = Double.valueOf(et_monthly_fees.getText().toString());
                                    enrollmentModel.setMonthly_fee(monthlyFee);
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Are you sure you want to update the Information?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setSubmitButtonEnabled(true);
                                        dialog.dismiss();

                                        //Save Student Image as Thumbnail
                                        Bitmap image1 = null;

                                        try {
                                            image1 = BitmapFactory.decodeByteArray(AppModel.getInstance().img1, 0, AppModel.getInstance().img1.length);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (image1 != null) {
                                            Bitmap thumbBitmap;
                                            float imageWidth = image1.getWidth();
                                            float imageHeight = image1.getHeight();

                                            if (imageHeight > imageWidth) {
                                                float targetWidth = (imageWidth / imageHeight) * 100;
                                                thumbBitmap = ThumbnailUtils.extractThumbnail(image1, (int) targetWidth, 100);
                                            } else {
                                                float targetHeight = (imageHeight / imageWidth) * 100;
                                                thumbBitmap = ThumbnailUtils.extractThumbnail(image1, 100, (int) targetHeight);
                                            }

                                            AppModel.getInstance().img1 = AppModel.getInstance().bitmapToByte(thumbBitmap);
                                        }

                                        String path1 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img1, NewAdmissionActivity.this, et_GrNo.getText().toString(), 1, SchoolSpinnerValue);
                                        String path2 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img2, NewAdmissionActivity.this, et_GrNo.getText().toString(), 2, SchoolSpinnerValue);
                                        String path3 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img3, NewAdmissionActivity.this, et_GrNo.getText().toString(), 3, SchoolSpinnerValue);
                                        AppModel.getInstance().img1 = null;
                                        AppModel.getInstance().img2 = null;
                                        AppModel.getInstance().img3 = null;
                                        AppModel.getInstance().photoFlag = -1;

                                        if (path1 != null) {
                                            if (DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollmentImage(new EnrollmentImageModel(idUpdate, path1, null, null, "P")) == 0) {
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollmentImage(new EnrollmentImageModel(idUpdate, path1, null, null, "P"));
                                            }
                                        }
                                        if (path2 != null) {
                                            if (DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollmentImage(new EnrollmentImageModel(idUpdate, path2, null, null, "B")) == 0) {
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollmentImage(new EnrollmentImageModel(idUpdate, path2, null, null, "B"));
                                            }

                                        }
                                        if (path3 != null) {
                                            if (DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollmentImage(new EnrollmentImageModel(idUpdate, path3, null, null, "A")) == 0) {
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollmentImage(new EnrollmentImageModel(idUpdate, path3, null, null, "A"));
                                            }
                                        }

                                        if (DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(idUpdate, "P") != null) {
                                            P = DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(idUpdate, "P").getFilename();
                                        }
                                        if (DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(idUpdate, "B") != null) {
                                            B = DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(idUpdate, "B").getFilename();
                                        }
                                        if (DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(idUpdate, "A") != null) {
                                            A = DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(idUpdate, "A").getFilename();
                                        }
                                        if (status != null && status.equals(AppConstants.PROFILE_REJECTED_KEY)) {
                                            modelp = DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(enrollmentid, "P");
                                            modelb = DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(enrollmentid, "B");
                                            modela = DatabaseHelper.getInstance(NewAdmissionActivity.this).getEnrollmentImage(enrollmentid, "A");
                                            if (modelp != null && modelp.getReview_status() != null && modelp.getReview_status().equals("R") /*|| modelp != null && modelp.getFilename().toLowerCase().contains("bodypart")*/) {
                                                markComplete++;
                                            }
                                            if (modelb != null && modelb.getReview_status() != null && modelb.getReview_status().equals("R") /*|| modelb != null && modelb.getFilename().toLowerCase().contains("bodypart")*/) {
                                                markComplete++;
                                            }
                                            if (modela != null && modela.getReview_status() != null && modela.getReview_status().equals("R") /*|| modela != null && modela.getFilename().toLowerCase().contains("bodypart")*/) {
                                                markComplete++;
                                            }
                                            if (markComplete == 0) {
                                                enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_COMPLETE_KEY);
                                                enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollment(enrollmentModel, idUpdate);
                                            } else {
                                                enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_REJECTED_KEY);
                                                enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollment(enrollmentModel, idUpdate);
                                            }
                                        } else {
                                            if (P != null && B != null && A != null) {
                                                enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_COMPLETE_KEY);
                                                enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollment(enrollmentModel, idUpdate);
                                            } else {
                                                enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_INCOMPLETE_KEY);
                                                enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
                                                DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollment(enrollmentModel, idUpdate);
                                            }
                                        }

                                        //Important when any change in table call this method
                                        AppModel.getInstance().changeMenuPendingSyncCount(NewAdmissionActivity.this, true);

                                        finish();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setSubmitButtonEnabled(true);
                                        dialog.dismiss();
//                                    SurveyAppModel.getInstance().img1 = null;
//                                    SurveyAppModel.getInstance().img2 = null;
//                                    SurveyAppModel.getInstance().img3 = null;
//                                    SurveyAppModel.getInstance().photoFlag = -1;
                                    }
                                });
                                builder.show();
                            }else {
                                et_GrNo.setError("GR No. " + et_GrNo.getText().toString() + " already exist!");
                            }
                        } else {
                            Toast.makeText(current_activity, "Cannot Update student more than Max Capacity:" + capacity + " and No. of Students are:" + StudentCount,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // New Enrollment
                    if (validate()) {
                        StudentCount = StudentCount + newEnrollmentCount;
                        if (StudentCount < capacity) {
                            final EnrollmentModel enrollmentModel = new EnrollmentModel(Integer.parseInt(et_GrNo.getText().toString()), schoolId, createdBy, AppModel.getInstance().getDate(), null, null, null, null);
                            enrollmentModel.setClass_section_id(classSectionId);
                            enrollmentModel.setStudentName(AppModel.getInstance().changeStringCase(et_Name.getText().toString()));
                            enrollmentModel.setGender(GenderText.toUpperCase());
                            if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                                    || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                                double monthlyFee = Double.valueOf(et_monthly_fees.getText().toString());
                                enrollmentModel.setMonthly_fee(monthlyFee);
                            }
                            id1 = DatabaseHelper.getInstance(NewAdmissionActivity.this).FindGRNOSTUDENTPROFILE(et_GrNo.getText().toString(), String.valueOf(schoolId));
                            if (id1 == -1) {
                                setSubmitButtonEnabled(false);

//                                id = DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollment(enrollmentModel);
//                                if (id == -1) {
//                                    et_GrNo.setError("GR No. " + et_GrNo.getText().toString() + " already exist!");
//                                }
//                                else {
                                et_GrNo.setError(null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Are you sure you want to Save the Information?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setSubmitButtonEnabled(true);
                                        dialog.dismiss();

                                        id = DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollment(enrollmentModel);
                                        Bitmap image1 = null;

                                        try {
                                            //Save Student Image as Thumbnail
                                            image1 = BitmapFactory.decodeByteArray(AppModel.getInstance().img1, 0, AppModel.getInstance().img1.length);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (image1 != null) {
                                            Bitmap thumbBitmap;
                                            float imageWidth = image1.getWidth();
                                            float imageHeight = image1.getHeight();

                                            if (imageHeight > imageWidth) {
                                                float targetWidth = (imageWidth / imageHeight) * 100;
                                                thumbBitmap = ThumbnailUtils.extractThumbnail(image1, (int) targetWidth, 100);
                                            } else {
                                                float targetHeight = (imageHeight / imageWidth) * 100;
                                                thumbBitmap = ThumbnailUtils.extractThumbnail(image1, 100, (int) targetHeight);
                                            }

                                            AppModel.getInstance().img1 = AppModel.getInstance().bitmapToByte(thumbBitmap);
                                        }


                                        String path1 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img1, NewAdmissionActivity.this, et_GrNo.getText().toString(), 1, SchoolSpinnerValue);
                                        String path2 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img2, NewAdmissionActivity.this, et_GrNo.getText().toString(), 2, SchoolSpinnerValue);
                                        String path3 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img3, NewAdmissionActivity.this, et_GrNo.getText().toString(), 3, SchoolSpinnerValue);
                                        AppModel.getInstance().img1 = null;
                                        AppModel.getInstance().img2 = null;
                                        AppModel.getInstance().img3 = null;
                                        AppModel.getInstance().photoFlag = -1;
                                        if (path1 != null && path2 != null && path3 != null) {
                                            enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_COMPLETE_KEY);
                                            DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollment(enrollmentModel, id);
                                        } else {
                                            enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_INCOMPLETE_KEY);
                                            DatabaseHelper.getInstance(NewAdmissionActivity.this).updateEnrollment(enrollmentModel, id);
                                        }
                                        DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollmentImage(new EnrollmentImageModel(id, path1, null, null, "P"));
                                        DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollmentImage(new EnrollmentImageModel(id, path2, null, null, "B"));
                                        DatabaseHelper.getInstance(NewAdmissionActivity.this).insertEnrollmentImage(new EnrollmentImageModel(id, path3, null, null, "A"));

                                        //Important when any change in table call this method
                                        AppModel.getInstance().changeMenuPendingSyncCount(NewAdmissionActivity.this, true);

                                        finish();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setSubmitButtonEnabled(true);
                                        dialog.dismiss();
//                                            SurveyAppModel.getInstance().img1 = null;
//                                            SurveyAppModel.getInstance().img2 = null;
//                                            SurveyAppModel.getInstance().img3 = null;
//                                            SurveyAppModel.getInstance().photoFlag = -1;
//                                            DatabaseHelper.getInstance(NewAdmissionActivity.this).deleteEnrollment(id);
                                    }
                                });
                                builder.show();
//                                }
                            } else {
                                et_GrNo.setError("GR No. " + et_GrNo.getText().toString() + " already exist!");
                            }
                        } else {
                            Toast.makeText(current_activity, "Cannot Enroll student more than Max Capacity:" + capacity + " and No. of Students are:" + StudentCount,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;


        }

    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(this, previewImageFile);
        imagePreviewDialog.show();
    }


    /**********************************Muhammad Salman Saleem**************/
    private boolean isFlagShipSchool(){
        return DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue);
    }
    /********************************Muhammad Salman Saleem**************/

    private void openCamera() {

        permission = new MarshMallowPermission(this);
        if (permission.checkPermissionForCamera()) {
            if (permission.checkPermissionForExternalStorage()) {
                captureImage();
            } else {
                permission.requestPermissionForExternalStorage();
            }
        } else {
            permission.requestPermissionForCamera();
        }

    }

    private void captureImage() {
        this.startActivity(new Intent(this, Camera2Activity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permission.requestPermissionForExternalStorage();
        } else if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            captureImage();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(i)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        SchoolSpinnerValue);

                if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                    if (!getIntent().hasExtra("gr")) {
                        String highestGrNo = DatabaseHelper.getInstance(NewAdmissionActivity.this).getHighestGrNO(SchoolSpinnerValue + "");
                        et_GrNo.setText(highestGrNo);
                    }
                }


                if (isSelected) {
                    classSectionModel = new ClassSectionModel();
                    classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                    classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                    spn_class_section_name.setAdapter(new ArrayAdapter<ClassSectionModel>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList()));
                    spn_class_section_name.setOnItemSelectedListener(this);

                    if (SchoolSpinnerValue > 0) {
                        String allowedFinanceModule = ((SchoolModel) adapterView.getItemAtPosition(i)).getAllowedModule_App();
                        if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                                || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                            ll_monthly_fees.setVisibility(View.VISIBLE);
                        } else {
                            ll_monthly_fees.setVisibility(View.GONE);
                        }
                    }
                } else {
                    isSelected = true;
                }

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null) {
                    MessageBox(errorModel.getMessage());
                }
                break;
            case R.id.spn_class_section_name:
                classSectionId = ((ClassSectionModel) adapterView.getItemAtPosition(i)).getSchoolClassId(); //class section Id = school class Id
                break;
            case R.id.spn_gender:
                GenderText = GenderAdapter.getItem(i);
                if (GenderText != null && GenderText.toLowerCase().equals("male")) {
                    GenderText = "m";
                } else if (GenderText != null && GenderText.toLowerCase().equals("female")) {
                    GenderText = "f";
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_male:
                GenderText = "m";
                break;
            case R.id.rb_female:
                GenderText = "f";
                break;
        }
    }

    //****************Muhammad Salman Saleem******************//
    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }
    //*********************************//

    private boolean validate() {
        int error = 0;
        String allowedFinanceModule = ((SchoolModel) SchoolSpinner.getSelectedItem()).getAllowedModule_App();
        if (et_GrNo.getText().toString().isEmpty()) {
            et_GrNo.setError("Required");
            error++;
        }

        if (et_Name.getText().toString().isEmpty()) {
            et_Name.setError("Required");
            error++;
        }

        if (SchoolSpinnerValue == 0) {
            Toast.makeText(NewAdmissionActivity.this, "Please select school", Toast.LENGTH_SHORT).show();
            error++;
        } else if (classSectionId == 0) {
            Toast.makeText(NewAdmissionActivity.this, "Please select class and section", Toast.LENGTH_SHORT).show();
            error++;
        }

        if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
            if (et_monthly_fees.getText().toString().isEmpty()) {
                Toast.makeText(NewAdmissionActivity.this, "Please enter monthly fees", Toast.LENGTH_SHORT).show();
                et_monthly_fees.setError("Required");
                error++;
            } else {
                int monthly_fee = Integer.parseInt(et_monthly_fees.getText().toString());
                if (monthly_fee > 1500) {
                    Toast.makeText(NewAdmissionActivity.this, "Monthly fees should not be greater then 1500", Toast.LENGTH_SHORT).show();
                    et_monthly_fees.setError("Monthly fees should not be greater then 1500");
                    error++;
                } else if (monthly_fee < 10) {
                    Toast.makeText(NewAdmissionActivity.this, "Monthly fees should not be less then 10", Toast.LENGTH_SHORT).show();
                    et_monthly_fees.setError("Monthly fees should not be less then 10");
                    error++;
                }
            }
        }

        return error == 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (result != null && result.getUri().getPath() != null)
                    AppModel.getInstance().img1 = AppModel.getInstance().bitmapToByte(BitmapFactory.decodeFile(result.getUri().getPath()));


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ShowToast(error.getMessage());
            }
        }
    }

    private void setSubmitButtonEnabled(boolean enabled){
        btn_submit.setEnabled(enabled);
        btn_submit.setClickable(enabled);
    }

    void openCameraGalleryDialog(String title){
        try {
            PickImageDialog dialog = PickImageDialog.build(new PickSetup().setTitle(title)
                    .setPickTypes(EPickType.CAMERA, EPickType.GALLERY));

            dialog.setOnClick(new IPickClick() {
                @Override
                public void onGalleryClick() {
                    dialog.onGalleryClick();
                }

                @Override
                public void onCameraClick() {
                    if (dialog.isVisible()) dialog.dismiss();
                    startActivity(new Intent(NewAdmissionActivity.this,CameraViewActivity.class));
                }
            }).setOnPickResult(new IPickResult() {
                @Override
                public void onPickResult(PickResult r) {
                    if (r.getError() == null) {
                        CropImage.activity(r.getUri())
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setFixAspectRatio(true)
                                .start(NewAdmissionActivity.this);
                    } else {
                        Toast.makeText(NewAdmissionActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).show(this);


        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(this,"Error in Imageview:" + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        AppModel.getInstance().photoFlag = -1;
        super.onBackPressed();
    }
}
